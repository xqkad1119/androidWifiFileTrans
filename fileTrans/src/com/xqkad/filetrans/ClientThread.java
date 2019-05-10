package com.xqkad.filetrans;


import android.content.Context;
import android.os.Handler;

import com.xqkad.java.io.IMsg;
import com.xqkad.java.io.MyIO;
import com.xqkad.java.net.MyPackage;
import com.xqkad.java.net.S;

import java.io.*;
import java.net.*;

public class ClientThread extends SocketThread {
    private String target_ip;
    private int target_port;

    public ClientThread(Context context, Handler handler, String targetip, int targetport) {
        super(context, handler);
        this.target_ip = targetip;
        this.target_port = targetport;
    }

    @Override
    public void run() {
    	try{
            if(createConnect())
                sendmsg(33,1,"���ӳɹ�");
            else{
                sendmsg(33,-1,"����ʧ��");
                return;
            }
            //enterwhile();
            receiv();
            sendmsg(3,1,"�����ѽ���");
        } catch(Exception e){
            sendmsg(200,-1,"�����ѶϿ�");
        }finally{
            close();
        }
    }

    private boolean createConnect(){
        MyPackage obj = null;
        try {
            socket = new Socket(target_ip, target_port);
            oos = new ObjectOutputStream(socket.getOutputStream());
            ois = new ObjectInputStream(socket.getInputStream());
            obj = new MyPackage(0);
            obj.name = getIp();
            oos.writeObject(obj);
            obj = (MyPackage) ois.readObject();
            if(obj.head == S.CONNECT_OK){
                oos.writeObject(new MyPackage(S.HEART));
                return true;
            }else
                return false;
        } catch (Exception e) {
            return false;
        }
    }

}
