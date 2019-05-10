package com.xqkad.java.io;

import java.io.*;
import java.text.*;
import java.util.ArrayList;


public class MyIO {
	
	public static void writeString(String path,String str){
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(path);
			out.write(str.getBytes());
		} catch (IOException e) {
		}finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {}
			}
		}
		
	}
	public static String readString(String path) {
		FileInputStream in = null;
		try {
			in = new FileInputStream(path);
			byte[] bytes = new byte[10240];
			int len = in.read(bytes);
			if(len == -1)
				return null;
			String str = new String(bytes, 0, len);
			return str;
		} catch (IOException e) {
			return null;
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {}
			}
		}
	}

	/**
	 * @return 0 error
	 */
	public static int createFile(String dir, String file){
		try{
			File d = new File(dir);
			if(!d.isDirectory()){
				if(!d.mkdirs())
					return 0;
			}

			File f = new File(dir, file);
			if(f.isFile())
				return 1;
			if(f.isDirectory())
				return 0;
			f.createNewFile();
			return 1;
		}catch(Exception e){
			return 0;
		}
	}

	/**
	 * 澶嶅埗鏂囦欢 鐢�?ile娴�
	 */
	public static String myCopy(String readpath, String writepath, int bufsize) {
		FileInputStream in = null;
		FileOutputStream out = null;
		try {
			in = new FileInputStream(readpath);
			out = new FileOutputStream(writepath);
		} catch (Exception e) {
			return "catch new in out";
		}
		byte[] bytes = new byte[bufsize];
		int len;
		try {
			len = in.read(bytes);
			while(len != (-1)) {
				out.write(bytes);
				len = in.read(bytes);
			}
			out.flush();
		} catch (Exception e) {
			return "catch read write";
		} finally {
			try {
				if (in != null)
					in.close();
				if (out != null)
					out.close();
			} catch (Exception e2) {
				return "catch in out close";
			}
		}
		return "ok";
	}
	
	/**
	 * 澶嶅埗鏂囦欢 鐢╞uffer娴�
	 */
	private static String copyFile(String readpath, String writepath, int bufsize, IMsg imsg){
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		try {
        	bis = new BufferedInputStream(new FileInputStream(readpath));
        	bos = new BufferedOutputStream(new FileOutputStream(writepath));
        } catch (Exception e) {
			return "catch new in out";
		}
		byte[] bytes = new byte[bufsize];
		int len = -1;
		try {
			while ((len = bis.read(bytes)) != -1) {
	            bos.write(bytes, 0, len);
	            if(imsg != null)
	            	imsg.sendMsg(len);
	        }
		} catch (Exception e) {
			return "catch read write";
		} finally {
			try {
				if (bis != null)
					bis.close();
				if (bos != null)
					bos.close();
			} catch (Exception e2) {
				return "catch in out close";
			}
		}
		return "ok";
    }

	public static String copyFile(String readpath, OutputStream out, int bufsize, IMsg imsg){
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		try {
			bis = new BufferedInputStream(new FileInputStream(readpath));
			bos = new BufferedOutputStream(out);
		} catch (Exception e) {
			return "catch new in out";
		}
		byte[] bytes = new byte[bufsize];
		int len = -1;
		try {
			while ((len = bis.read(bytes)) != -1) {
				bos.write(bytes, 0, len);
				if(imsg != null)
					imsg.sendMsg(len);
			}
		} catch (Exception e) {
			return "catch read write";
		} finally {
			try {
				if (bis != null)
					bis.close();
				if (bos != null)
					bos.close();
			} catch (Exception e2) {
				return "catch in out close";
			}
		}
		return "ok";
	}

	private static String copyDir(String readpath, String writepath, int bufsize, IMsg imsg) {
		try {
			File readDir=new File(readpath);
			File[] fs=readDir.listFiles();
			File writeDir=new File(writepath);
			if(!writeDir.exists())
				writeDir.mkdirs();
			for (File f : fs) {
				if(f.isFile()){
					copyFile(f.getPath(),writepath+"/"+f.getName(), bufsize, imsg);
				}else if(f.isDirectory()){
					copyDir(f.getPath(),writepath+"/"+f.getName(), bufsize, imsg);
				}
			}
			return "ok";
		} catch (Exception e) {
			return "catch copyDir";
		}
	}
	
	private static void readFile(String readpath, int bufsize, OnRead onread)throws Exception{
		onread.onRead(false, readpath, null, 0);
		FileInputStream fis = new FileInputStream(readpath);
		BufferedInputStream bis = new BufferedInputStream(fis);
		byte[] bytes = new byte[bufsize];
		int len = -1;
		while ((len = bis.read(bytes)) != -1) {
			if(onread != null)
				onread.onRead(false, readpath, bytes, len);
		}
		bis.close();
	}
	
	private static void readDir(String readpath, int bufsize, OnRead onread)throws Exception{
		File readfile = new File(readpath);
		onread.onRead(true, readfile.getPath(), null, 0);
		File[] fs = readfile.listFiles();
		for(File f : fs){
			if(f.isFile()){
				readFile(f.getPath(), bufsize, onread);
			}else if(f.isDirectory()){
				readDir(f.getPath(), bufsize, onread);
			}
		}
	}

	public static int copy(String readpath, String writedir, int bufsize, IMsg imsg){
		int re = 0;
		try{
			File readfile = new File(readpath);
			if(readfile.isFile())
				copyFile(readpath, writedir+"/"+readfile.getName(), bufsize, imsg);
			if(readfile.isDirectory())
				copyDir(readpath, writedir+"/"+readfile.getName(), bufsize, imsg);
			re = 1;
		}catch (Exception e){
			re = -1;
		}
		return re;
	}

	public static int read(String path, int bufsize, OnRead onread){
		int re = 0;
		try{
			File readfile = new File(path);
			if(readfile.isFile())
				readFile(path, bufsize, onread);
			if(readfile.isDirectory())
				readDir(path, bufsize, onread);
			re = 1;
		}catch (Exception e){
			re = -1;
		}
		return re;
	}
	
	// 绉诲姩鏂囦欢鎴栬�鐩綍
	public static boolean renameFile(String oldPath, String newPath) {
		try {
			File src = new File(oldPath);
			if (src.isFile()) {
				File dest = new File(newPath);
				src.renameTo(dest);
			} else {
				File dest = new File(newPath);
				File newFile = new File(dest.getAbsoluteFile() + "");
				src.renameTo(newFile);
			}
			return true;
		} catch (Exception e) {
			// TODO 鑷姩鐢熸垚鐨�catch 鍧�
			e.printStackTrace();
			return false;
		}
	}
	
	public static boolean deleteFile(File file, IMsg imsg) {
		try {
			if(file.exists()){
				if (file.isFile()) {
					if(imsg != null)
						imsg.sendMsg((int) file.length());
					file.delete();
					return true;
				}
				if (file.isDirectory()) {
					File[] childFiles = file.listFiles();
					if (childFiles == null || childFiles.length == 0) {
						file.delete();
						return true;
					}
					for (int i = 0; i < childFiles.length; i++) {
						deleteFile(childFiles[i], imsg);
					}
					file.delete();
				}
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	public static long getFileSize(File f) {
		try { 
			//FileInputStream fis = new FileInputStream(f); 
			//long size = fis.available();
			//fis.close(); 
			return f.length();
		} catch (Exception e) {
			return -1;
		}
	}
	
	public static long getDirectorySize(File f) {
		long size = 0;
		try {
			File[] flist = f.listFiles();
			for (int i = 0; i < flist.length; i++) {
				if (flist[i].isDirectory()) {
					size = size + getDirectorySize(flist[i]);
				} else {
					size = size + flist[i].length();
				}
			}
		} catch (Exception e) {
			size = -1;
		}
		return size;
	}

	public static long getSelectSize(String path, ArrayList<String> list) {
		long size = 0;
		for (String name : list) {
			File f = new File(path+"/"+name);
			if(f.isDirectory())
				size += MyIO.getDirectorySize(f);
			if(f.isFile())
				size += MyIO.getFileSize(f);
		}
		return size;
	}

	// String.format("%.2f", Float.valueOf(size)/1024/1024);
	public static String FormatFileSize(long fileSize) {
		DecimalFormat df = new DecimalFormat("#.##");// #浠ｈ〃鏁板瓧
		String fileSizeString = "";
		if (fileSize < 1024) {
			fileSizeString = df.format((double) fileSize) + "B";
		} else if (fileSize < 1048576) {
			fileSizeString = df.format((double) fileSize / 1024) + "KB";
		} else if (fileSize < 1073741824) {
			fileSizeString = df.format((double) fileSize / 1048576) + "MB";
		} else {
			fileSizeString = df.format((double) fileSize / 1073741824) + "GB";
		}
		return fileSizeString;
	}
	
	/**
	 * Proportion姣斾�?size1 / size2 * 100%
	 */
	public static int getProportion(float size1, float size2) {
		int result = (int) ( size1 / size2 * 100);
		if(size2 <= 0)
			return -1;
		return result;
	}
}
