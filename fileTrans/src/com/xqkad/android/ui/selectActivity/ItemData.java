package com.xqkad.android.ui.selectActivity;

public class ItemData{
    public int 		imgid;
    public String 	path;
    public String 	name;
    public int 		type;
    
    public static final int
    				STORAGE = 0,
    				DIR		= 1,
    				FILE	= 2;
    
    public ItemData(int imgid, String path, String name, int type){
        this.imgid = imgid;
        this.path = path;
        this.name = name;
        this.type = type;
    }
}
