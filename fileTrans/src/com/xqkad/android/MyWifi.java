package com.xqkad.android;

import android.content.*;
import android.net.wifi.*;

import java.net.*;
import java.util.*;

public class MyWifi {
    //private static String ip;
    public static String getip(Context context){
        WifiManager mgr = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = mgr.getConnectionInfo();
        int t = info.getIpAddress();
        if(t == 0)
            return "error";
        return intIP2StringIP(t);
    }

    public static ServerSocket startTCPServerSocket(int tryport){
        ServerSocket ss=null;
        for(int i=0;i<124;i++){
            try {
                ss = new ServerSocket(i+tryport);
                return ss;
            } catch (Exception e) {}
        }
        return null;
    }

    public static String getLocalIpv6() {
        //for ipv6, ipv4, 10.0.2.15
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress();
                        //fe80::66cc:2eff:fe4a:5fec%wlan0
                        //192.168.1.100
                        //10.0.2.15
                    }
                }
            }
        } catch (Exception e) {
            return "error";
        }
        return "error";
    }

    public static String intIP2StringIP(int ip) {
        return (ip & 0xFF) + "." +
                ((ip >> 8) & 0xFF) + "." +
                ((ip >> 16) & 0xFF) + "." +
                (ip >> 24 & 0xFF);
    }
}
