package com.xqkad.java.net;

import java.io.*;

public class MyPackage implements Serializable{
	public int head;
	public String name;
	public long all_size;
	public long finish_size;
	public byte[] data;
	public int datalen;
	public boolean isDir;


	public MyPackage(int head){
		this.head = head;
	}

	public MyPackage(int head, byte[] data){
		this.head = head;
		this.data = data;
	}
}
