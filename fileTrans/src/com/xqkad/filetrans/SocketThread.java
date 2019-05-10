package com.xqkad.filetrans;

import android.content.Context;
import android.os.*;

import com.xqkad.java.io.*;
import com.xqkad.java.net.*;
import com.xqkad.android.MyWifi;
import com.xqkad.android.tools.StorageList;

import java.io.*;
import java.net.*;

public class SocketThread extends Thread{
    private Handler handler;
    protected Context context;
    protected ServerSocket server;
    protected Socket socket;
    protected ObjectInputStream ois;
    protected ObjectOutputStream oos;
    protected MyPackage send_obj;

    public static String savePath_def = "内部存储/1xqkad/rec";
    public static String savePath;
    protected String saveName;
    protected BufferedOutputStream savebos;
    protected long transAllSize, transFinishSize;
    protected String sendPath, sendName;
    protected boolean flag, heart, sendMSG;
    private static String ip;

    public String getIp(){
        if(ip == null)
            ip = MyWifi.getip(context);
        return ip;
    }

    public SocketThread(Context context, Handler handler){
        this.context = context;
        this.handler = handler;
        sendMSG = true;
    }

    public void sendRequest(String select) throws Exception {
        File sendFile = new File(select);
        if(!sendFile.exists())
            throw new Exception();
        send_obj = new MyPackage(S.REQUEST_SEND_FILE);
        sendPath = sendFile.getParent();
        sendName = sendFile.getName();
        send_obj.name = sendName;
        if(sendFile.isDirectory()){
            send_obj.all_size = MyIO.getDirectorySize(sendFile);
            send_obj.isDir = true;
        }else{
            send_obj.all_size = MyIO.getFileSize(sendFile);
            send_obj.isDir = false;
        }
        transAllSize = send_obj.all_size;
        transFinishSize = 0;
        //send_obj.finish_size = 0;
        heart = false;
    }

    private void sendFile() throws Exception {
        OnRead onread = new OnRead() {
            MyPackage obj;
            int flen = 0;

            @Override
            public void onRead(boolean isDir, String path, byte[] b, int len)throws Exception {
                obj = new MyPackage(S.FILE, b);
                obj.isDir = isDir;
                obj.name = new String(path.toCharArray(), sendPath.length()+1, path.length()-sendPath.length()-1);

                obj.datalen = len;
                obj.finish_size = flen;flen+=len;
                oos.writeObject(obj);
                oos.flush();
                oos.reset();
                transFinishSize += len;
                sendmsg(3, 1, "发送 "+MyIO.getProportion(transFinishSize, transAllSize)+" %");
            }
        };
        MyIO.read(sendPath+"/"+sendName, 2048, onread);
        sendPath = null;
        sendName = null;
        oos.writeObject(new MyPackage(S.SEND_FILE_FINISH));
        sendmsg(100,1,"发送完成");
    }

    private void onReceivRequestFile(MyPackage obj)throws Exception {
    	String sz = MyIO.FormatFileSize(obj.all_size);
        if (sendPath == null){
        	sendmsg(51, 1, obj.name+" 大小 "+sz);
            transAllSize = obj.all_size;
            send_obj = new MyPackage(S.HEART);
        }else
            send_obj = new MyPackage(S.RESULT_SEND_FILE_ERROR);
        oos.writeObject(send_obj);
    }
    
    public void receivResult(String path){
        String path2 = "";
        if(path == null){
            send_obj = new MyPackage(S.RESULT_SEND_FILE_ERROR);
            heart = false;
            transAllSize = 0;
            return;
        }
        if(path.startsWith("内部存储")){
            path2 = new String(path.toCharArray(),4,path.length()-4);
            path2 = MainActivity.storagePaths[0]+path2;
        }
        File f = new File(path2);
        if(!f.isDirectory())
            f.mkdirs();
        long sz = StorageList.getPathSize(path2,true) - 100*1024*1024;
        if(f.isDirectory() && f.canWrite() && transAllSize < sz){
        	savePath = path2;
            send_obj = new MyPackage(S.RESULT_SEND_FILE_OK);
        }else{
            send_obj = new MyPackage(S.RESULT_SEND_FILE_ERROR);
            transAllSize = 0;
            sendmsg(3,1,"er savepath"+path2);
        }
        heart = false;
    }

    private void receivFile(MyPackage obj) throws Exception{
        //sendmsg(3, 1, "正在接收中!");
    	if(obj.name.equals(saveName)){
            if(obj.data!=null){
            	savebos.write(obj.data, 0, obj.datalen);
            	transFinishSize += obj.datalen;
            	sendmsg(3, 1, "接收 "+MyIO.getProportion(transFinishSize, transAllSize)+" %");
            }
        }else{
            saveName = obj.name;
            if(savebos!=null)
            	savebos.flush();
            createFile(obj);
        }
    }

    private void createFile(MyPackage obj) throws Exception{
        String pathname = savePath+"/"+saveName;
        File f = new File(pathname);
        FileOutputStream fos = null;
        if(obj.isDir){
            f.mkdirs();
        }else{
            fos = new FileOutputStream(pathname);
            savebos = new BufferedOutputStream(fos);
        }
    }

    private void receivFinish() throws Exception {
        saveName = null;
        savePath = null;
        savebos.close();
        savebos = null;
        transAllSize = 0;
        transFinishSize = 0;
        oos.writeObject(new MyPackage(S.HEART));
        sendmsg(100,1,"接收完成");
    }

    protected void receiv() throws Exception{
        MyPackage obj = null,
                heart_obj = new MyPackage(S.HEART);
        heart = true;
        flag = true;

        while (flag) {
            obj = (MyPackage) ois.readObject();
            switch (obj.head) {
                case S.HEART:
                    if(heart){
                        Thread.sleep(333);
                        oos.writeObject(heart_obj);
                    }else{
                        oos.writeObject(send_obj);
                        heart = true;
                    }
                    break;

                case S.REQUEST_SEND_FILE:
                    onReceivRequestFile(obj);
                    break;

                case S.RESULT_SEND_FILE_OK:
                	sendFile();
                    break;

                case S.RESULT_SEND_FILE_ERROR:
                    break;

                case S.FILE:
                    receivFile(obj);
                    break;

                case S.SEND_FILE_FINISH:
                    receivFinish();
                    break;
                default:
                    break;
            }
        }
    }

    public void close(){
        flag = false;
        savebos = null;
        sendMSG = false;
        //pauseOver();
        try {
            if (server != null){
                server.close();
                server = null;
            }
            if(socket != null){
            	socket.close();
            	socket = null;
            }
        }catch (Exception e){}
    }

    protected void sendmsg(int what, int arg1, Object obj){
        if(!sendMSG)
        	return;
    	Message msg = Message.obtain();
        msg.what = what;
        msg.arg1 = arg1;
        msg.obj = obj;
        handler.sendMessage(msg);
    }
}
