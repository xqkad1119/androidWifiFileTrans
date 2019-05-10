package com.xqkad.filetrans;


import android.content.Context;
import android.os.Handler;

import com.xqkad.android.*;
import com.xqkad.android.tools.StorageList;
import com.xqkad.android.ui.dialog.MyDg;
import com.xqkad.java.net.*;
import com.xqkad.java.io.*;

import java.io.*;


public class ServerThread extends SocketThread {

    public ServerThread(Context context, Handler handler) {
        super(context, handler);
    }

    @Override
    public void run() {
        try{
        	if(!init())
        		return;
            String s = accept();
            sendmsg(2,2,s+"已连接");
            receiv();
        }catch (Exception e){
            sendmsg(200,-1,"连接已断开");
        }finally {
            sendmsg(3,1,"server close");
            close();
        }
    }

    private boolean init(){
        String ipstr = getIp();
        if(ipstr.equals("error")){
            sendmsg(1,-1,"");
            return false;
        }
        sendmsg(1,1,"本机ip "+ipstr);
        server = MyWifi.startTCPServerSocket(7777);
        if(server == null){
            sendmsg(2,-1,"");
            return false;
        }
        sendmsg(2,1,"端口号 "+server.getLocalPort());
        //
        sendmsg(3,1,"");
        return true;
    }

    private String accept()throws Exception{
        String r = "";
        MyPackage obj = null;

        socket = server.accept();
        oos = new ObjectOutputStream(socket.getOutputStream());
        ois = new ObjectInputStream(socket.getInputStream());
        obj = (MyPackage) ois.readObject();
        oos.writeObject(new MyPackage(S.CONNECT_OK));
        r += obj.name;
        return r;
    }

}
