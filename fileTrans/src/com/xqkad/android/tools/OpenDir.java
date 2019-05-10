package com.xqkad.android.tools;

import java.io.*;
import java.text.Collator;
import java.util.*;

public class OpenDir {

    public static String[] opendir(String path, boolean onlydir){
        String[] re = null;
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
        re = new String[ds.length+fs.length];
        for (int i = 0;i<ds.length;i++){
            re[i] = ds[i];
        }
        for (int i = 0;i<fs.length;i++){
            re[i+ds.length] = fs[i];
        }
        return re;
    }

}
