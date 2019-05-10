package com.xqkad.android.tools;

import java.lang.reflect.*;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.StatFs;
import android.os.storage.StorageManager;
//import android.support.annotation.RequiresApi;

public class StorageList {
	private Activity mActivity;
	private StorageManager mStorageManager;
	private Method mMethodGetPaths;
	public static String te;
	
	public static String gette(){
		return te == null ? "" : te;
	}
	public StorageList(Activity activity) {
		mActivity = activity;
		if (mActivity != null) {
			mStorageManager = (StorageManager) mActivity.getSystemService(
					Activity.STORAGE_SERVICE);
			try {
				mMethodGetPaths = mStorageManager.getClass().getMethod("getVolumePaths");
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			}
		}
	}
	public String[] getVolumePaths() {
		String[] paths = null;
		try {
			paths = (String[]) mMethodGetPaths.invoke(mStorageManager);
		} catch (Exception e) {} 
		return paths;
	}
	
	public static String[] getStoragePaths(Activity activity) {
		String[] paths = null;
		StorageManager mgr = null;
		Method meth = null;
		if(activity == null) 
			return null;
		try {
			mgr = (StorageManager) activity.getSystemService(
					Activity.STORAGE_SERVICE);
			meth = mgr.getClass().getMethod("getVolumePaths");
			paths = (String[]) meth.invoke(mgr);
		} catch (Exception e) {
			paths = null;
		} 
		return paths;
	}
	
	public static long getPathSize(String path){
		return getPathSize(path, false);
	}
	
	public static long getPathSize(String path, boolean isCanUseSize){
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2){
			return getPathSizeApi18(path, isCanUseSize);
		}else{
			return getPathSizeApiSup(path, isCanUseSize);
		}
	}
	
	//@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
	private static long getPathSizeApi18 (String path, boolean isCanUseSize){
		//api18	 https://blog.csdn.net/zwyzzz888/article/details/48789633
		StatFs sf = new StatFs(path);
		long blockSize = sf.getBlockSizeLong(); 
		long blockCount = sf.getBlockCountLong();// �ܴ�С
		long blockAvailable = sf.getAvailableBlocksLong(); // ��Ч��С
		long zsize = blockSize * blockCount ;
		long ysize = blockSize * blockAvailable ;
		if(isCanUseSize){
			return ysize;// ��Ч��С
		}else{
			return zsize;// �ܴ�С
		}
	}
	
	private static long getPathSizeApiSup (String path, boolean isCanUseSize){
		StatFs sf = new StatFs(path);
		long blockSize = sf.getBlockSize(); 
		long blockCount = sf.getBlockCount();// �ܴ�С
		long blockAvailable = sf.getAvailableBlocks(); 
		long zsize = blockSize * blockCount ;
		long ysize = blockSize * blockAvailable ;
		if(isCanUseSize){
			return ysize;
		}else{
			return zsize;
		}
	}
}
