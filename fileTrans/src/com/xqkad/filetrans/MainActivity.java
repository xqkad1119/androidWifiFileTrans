package com.xqkad.filetrans;

import java.io.File;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.*;
import android.content.Intent;
import android.graphics.Color;
import android.os.*;
import android.app.*;
import android.app.AlertDialog.Builder;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.*;

import com.xqkad.android.tools.StorageList;
import com.xqkad.android.ui.dialog.MyDg;
import com.xqkad.android.ui.selectActivity.SelectActivity;
import com.xqkad.java.io.MyIO;

public class MainActivity extends Activity{

    private TextView tv1,tv2,tv3;
    private Button btn1,btn2;
    private int btn1_stat = 0;//0船舰连接 1关闭连接
    private ProgressDialog pd;
    private ServerThread server;
    private ClientThread client;
    private boolean selecting = false;//use onpause
    //private boolean disConnectClose = true;//use close server and create client
    public static String[] storagePaths;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        String str="";

        @Override
        public void handleMessage(Message msg) {
            //super.handleMessage(msg);
            switch (msg.what) {
                case 1://get host ip
                    if(msg.arg1 == -1){
                        tv1.setText("获取ip错误");
                        setbtn(-1);
                    }
                    else tv1.setText((String)msg.obj);
                    break;
                case 2://server start
                    if(msg.arg1 == -1)
                        tv2.setText("启动端口错误");
                    else if(msg.arg1 == 1){
                    	tv2.setText((String)msg.obj);
                    }else if(msg.arg1 == 2){
                    	setbtn(1);
                    	tv2.setText((String)msg.obj);
                    }
                    break;
                case 3://
                    if(msg.arg1 == -1)
                        setbtn(0);
                    tv3.setText((String)msg.obj);
                    break;
                case 33://client connect
                	if(msg.arg1 == 1){
                		tv1.setText("");
                		tv2.setText("");
                		tv3.setText("连接成功 ");
                		setbtn(1);
                	}else if(msg.arg1 == -1){
                		startDgMsg("连接失败","");
                		server = new ServerThread(MainActivity.this, handler);
                		server.start();
                		setbtn(0);
                	}
                    break;
                case 4://accept socket
                    setbtn(1);
                    tv2.setText((String)msg.obj);
                    break;
                case 5://send file
                    if(msg.arg1 == -1){
                        tv3.setText("发送被取消");
                    }else if(msg.arg1 == 0){
                        tv3.setText("开始发送");
                    }else if(msg.arg1 == 1){
                        tv3.setText("发送 ");
                    }else
                        tv3.setText((String)msg.obj);
                    break;
                case 51://requestsendfile
                    startDgReq(msg.obj.toString(),"接收文件");
                    break;
                case 100://requestsendfile
                    //startDgMsg("完成","");
                    tv3.setText(msg.obj.toString());
                    break;
                case 200:
                	startDgDisconnect();
                	break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_main);
        tv1 = (TextView) findViewById(R.id.tv1_main);
        tv2 = (TextView) findViewById(R.id.tv2_main);
        tv3 = (TextView) findViewById(R.id.tv3_main);
        btn1 = (Button) findViewById(R.id.btn1_main);
        btn2 = (Button) findViewById(R.id.btn2_main);
        init();
        //
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        selecting = false;
        if(data == null)
            return;
        String select = data.getStringExtra("select");
        try {
            if(client!=null)
                client.sendRequest(select);
            else if(server!=null)
                server.sendRequest(select);
        }catch(Exception e){
            startDgMsg("req error","");
        }
    }

    @Override
    protected void onPause() {
        if(!selecting){
            if(server != null)
                server.close();
            if(client != null)
                client.close();
            //finish();
        }
        super.onPause();
    }

    private void init(){
        if(storagePaths == null)
        	storagePaths = new StorageList(this).getVolumePaths();
        server = new ServerThread(this,handler);
        server.start();
        selecting = false;
        //disConnectClose = true;
        setbtn(0);
    }

    public void onClick(View v){
        if(v.getId()==R.id.btn1_main){
            if(btn1_stat == 0){
            	startDgConnect();
            	btn1_stat = 1;
            }else{
            	if(server != null)
                    server.close();
                if(client != null)
                    client.close();
                this.finish();
            }
        }else if(v.getId()==R.id.btn2_main){
            selecting = true;
            Intent intent = Tool.getSelectIntent(this);
            startActivityForResult(intent,0);
        }
    }
    
    public void setbtn(int a){
        if(a==-1){
            btn1.setText("创建连接");
            btn1.setEnabled(false);
            btn1.setTextColor(Color.argb(111,255,255,255));
            btn2.setEnabled(false);
            btn2.setTextColor(Color.argb(111,255,255,255));
            return;
        }
        if(a==0){
            btn1_stat = 0;
        	btn1.setText("创建连接");
            btn2.setEnabled(false);
            btn1.setTextColor(Color.argb(255,255,255,255));
            btn2.setTextColor(Color.argb(111,255,255,255));
        }else{
        	btn1.setText("关闭连接");
            btn2.setEnabled(true);
            btn2.setTextColor(Color.argb(255,255,255,255));
        }
    }

    void startPD(){
        if(pd == null)
            pd = new ProgressDialog(MainActivity.this);
        pd.setMax(100);
        //pd.setMessage("123123");
        pd.setIndeterminate(false);
        pd.setCancelable(false);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        //pd.setVolumeControlStream(TRIM_MEMORY_BACKGROUND);
        pd.show();
    }
    
    void startDgConnect(){
    	MyDg.startDgDenglu(this, "ip  ", "端口 ", "192.168.1.101", "7777", new MyDg.Impl.OnDengLu() {
			
			@Override
			public void OnDengLu(String str1, String str2) {
				try{
		            int targetport = Integer.valueOf(str2);
		            if(targetport<7000||targetport>8000)
		                throw new Exception();
		            server.close();
		            server = null;
		            client = new ClientThread(MainActivity.this, handler, str1, targetport);
		            client.start();
		        }catch(Exception e){
		            tv3.setText("555");
		        }
			}
		});
    }
    
    void startDgReq(String msg, String title){
		MyDg.startDgConfirm(this, msg, title, new MyDg.Impl.OnConfirm() {
					
			@Override
			public void OnConfirm(String str) {
				if(str == null){
					if(client!=null){
						client.receivResult(null);
					}else if(server!=null)
						server.receivResult(null);
				}else{
					MyDg.startDgOneET(MainActivity.this, SocketThread.savePath_def,"保存到:", new MyDg.Impl.OnConfirm() {
						
						@Override
						public void OnConfirm(String str) {
							if(client!=null){
								client.receivResult(str);
							}else if(server!=null)
								server.receivResult(str);
						}
					});
				}
				
			}
		});
    }
    
    void startDgMsg(String msg, String title){
    	MyDg.startDgMsg(this, msg, title, null);
    }
    
    void startDgDisconnect(){
        MyDg.startDgMsg(this, "连接已断开", "", new MyDg.Impl.OnConfirm() {
			
			@Override
			public void OnConfirm(String str) {
				if(server != null)
		            server.close();
		        if(client != null)
		            client.close();
		        MainActivity.this.finish();
			}
		});
    }
    
    private void sc(String s){
        tv1.setText(s);
    }

    private void sc(String s, int t){
        tv1.append(s);
    }

}
