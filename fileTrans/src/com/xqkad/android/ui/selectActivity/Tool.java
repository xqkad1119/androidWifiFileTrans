package com.xqkad.android.ui.selectActivity;

import android.content.Context;
import android.os.Handler;

import com.xqkad.android.*;
import com.xqkad.android.tools.StorageList;
import com.xqkad.android.ui.selectActivity.ItemData;
import com.xqkad.java.net.*;
import com.xqkad.java.io.*;

import java.io.*;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;


public class Tool {

    public static int getImageId(String name, int[] formats, int[] ids){
    	int format = MyFormat.getFormat(name);
    	for(int i=0; i<ids.length; i++){
    		if(format == formats[i])
    			return ids[i];
    	}
    	return -1;
    }
    /*
	public static int getImageId2(String name){
    	int id = 0;
    	
    	if(MyFormat.checkFormat(name, MyFormat.Images)) {
            id = R.drawable.pic;
        }else if(MyFormat.checkFormat(name, MyFormat.Videos)){
        	id = R.drawable.video;
        }else if(MyFormat.checkFormat(name, MyFormat.Audios)){
        	id = R.drawable.music;
        }else if(MyFormat.checkFormat(name, MyFormat.WebTexts)){
        	id = R.drawable.html;
        }else if(MyFormat.checkFormat(name, MyFormat.Texts)){
        	id = R.drawable.text;
        }else if(MyFormat.checkFormat(name, MyFormat.Packages)){
        	id = R.drawable.apk;
        }else if(MyFormat.checkFormat(name, MyFormat.Words)){
        	id = R.drawable.word;
        }else if(MyFormat.checkFormat(name, MyFormat.Excels)){
        	id = R.drawable.excel;
        }else if(MyFormat.checkFormat(name, MyFormat.Pdfs)){
        	id = R.drawable.pdf;
        }else if(MyFormat.checkFormat(name, MyFormat.PPTs)){
        	id = R.drawable.ppt;
        }else if(MyFormat.checkFormat(name, MyFormat.Rars)){
        	id = R.drawable.other;
        }else {
        	id = R.drawable.other;
        }
    	
    	return id;
    }
	*/
    public static ItemData[] openStorageList(String[] ps, int iconid){
        int length = 0;
        ItemData[] re = null;
        for(int i=0;i<ps.length;i++){
            String p = ps[i];
            long size = StorageList.getPathSize(p);
            //long size = Long.valueOf(str);
            if(size > 0)
                length++;
        }
        re = new ItemData[length];
        re[0] = new ItemData(iconid, ps[0], "ÄÚ²¿´æ´¢",ItemData.STORAGE);
        for(int i=1;i<length;i++){
            String p = ps[i];
            ItemData data = new ItemData(iconid, p, getStorageName(p),ItemData.STORAGE);
            re[i] = data;
        }
        return re;
    }
    
    public static ItemData[] getItemData(String path, boolean onlydir,
    		int[] formats, int[] iconids){
        ItemData[] re = null;
        ArrayList<String> list_dir = new ArrayList<String>();
        ArrayList<String> list_file = new ArrayList<String>();
        String[] ds = null;
        String[] fs = null;
        Comparator cmp = Collator.getInstance(java.util.Locale.CHINA);
        File readfile = new File(path);
        if(!readfile.isDirectory())
            return null;
        File[] files = readfile.listFiles();
        if(files == null)
            return null;
        for(File file : files){
            String name = file.getName();
            char a = name.charAt(0);
            if(a == '.')
                continue;
            if(file.isDirectory()){
                list_dir.add(name);
            }else if(!onlydir){
                list_file.add(name);
            }
        }
        ds = (String[])list_dir.toArray(new String[list_dir.size()]);
        fs = (String[])list_file.toArray(new String[list_file.size()]);
        Arrays.sort(ds,cmp);
        Arrays.sort(fs,cmp);
        //////////
        re = new ItemData[ds.length+fs.length];
        for (int i = 0;i<ds.length;i++){
            re[i] = new ItemData(getDirIconId(iconids),path+"/"+ds[i],ds[i], ItemData.DIR);
        }
        for (int i = 0;i<fs.length;i++){
            re[i+ds.length] = new ItemData(getImageId(fs[i],formats,iconids),path+"/"+fs[i], fs[i], ItemData.FILE);
        }
        return re;
    }

    public static int getDirIconId(int[] ids){
    	return ids != null ? ids[0] : -1;
    }
    
    private static String getStorageName(String path){
    	String r = "";
    	
    	String[] ss = path.split("/");
    	if(ss==null)
    		return path;
    	r = ss[ss.length-1];
    	if(r.contains("usb"))
    		r = "UÅÌ";
    	if(r.contains("sdcard"))
    		r = "sd¿¨";
    	return r;
    }
}

