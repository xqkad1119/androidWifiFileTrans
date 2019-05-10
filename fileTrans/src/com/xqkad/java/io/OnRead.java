package com.xqkad.java.io;

public interface OnRead {
	public void onRead(boolean isDir, String path, byte[] b, int len)throws Exception;
}
