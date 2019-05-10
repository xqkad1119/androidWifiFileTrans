package com.xqkad.android.tools;

import java.io.File;

import android.content.Intent;
import android.net.Uri;

import com.xqkad.java.io.MyFormat;

public class OpenFile {
	
	private static Intent getHtmlFileIntent(File file) {
		Uri uri = Uri.parse(file.toString()).buildUpon()
				.encodedAuthority("com.android.htmlfileprovider")
				.scheme("content").encodedPath(file.toString()).build();
		Intent intent = new Intent("android.intent.action.VIEW");
		intent.setDataAndType(uri, "text/html");
		return intent;
	}

	private static Intent getImageFileIntent(File file) {
		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Uri uri = Uri.fromFile(file);
		intent.setDataAndType(uri, "image/*");
		return intent;
	}

	private static Intent getPdfFileIntent(File file) {
		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Uri uri = Uri.fromFile(file);
		intent.setDataAndType(uri, "application/pdf");
		return intent;
	}

	private static Intent getTextFileIntent(File file) {
		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Uri uri = Uri.fromFile(file);
		intent.setDataAndType(uri, "text/plain");
		return intent;
	}

	private static Intent getAudioFileIntent(File file) {
		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.putExtra("oneshot", 0);
		intent.putExtra("configchange", 0);
		Uri uri = Uri.fromFile(file);
		intent.setDataAndType(uri, "audio/*");
		return intent;
	}

	private static Intent getVideoFileIntent(File file) {
		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.putExtra("oneshot", 0);
		intent.putExtra("configchange", 0);
		Uri uri = Uri.fromFile(file);
		intent.setDataAndType(uri, "video/*");
		return intent;
	}

	private static Intent getChmFileIntent(File file) {
		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Uri uri = Uri.fromFile(file);
		intent.setDataAndType(uri, "application/x-chm");
		return intent;
	}

	private static Intent getWordFileIntent(File file) {
		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Uri uri = Uri.fromFile(file);
		intent.setDataAndType(uri, "application/msword");
		return intent;
	}

	private static Intent getExcelFileIntent(File file) {
		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Uri uri = Uri.fromFile(file);
		intent.setDataAndType(uri, "application/vnd.ms-excel");
		return intent;
	}

	private static Intent getPPTFileIntent(File file) {
		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Uri uri = Uri.fromFile(file);
		intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
		return intent;
	}

	private static Intent getApkFileIntent(File file) {
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(android.content.Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(file),
				"application/vnd.android.package-archive");
		return intent;
	}

	private static Intent getOtherFileIntent(File file) {
		/*
		 * Intent intent = new Intent();
		 * intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		 * intent.setAction(android.content.Intent.ACTION_DEFAULT);
		 */
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_LAUNCHER);
		// intent.setDataAndType(Uri.fromFile(file),
		// "application/vnd.android.package-archive");
		return intent;
	}
	
	public static Intent getIntent(File file) {
		Intent intent;
		String filename = file.getName();
		if (MyFormat.checkFormat(filename, MyFormat.Images)) {
			intent = OpenFile.getImageFileIntent(file);
		} else if (MyFormat.checkFormat(filename, MyFormat.WebTexts)) {
			intent = OpenFile.getHtmlFileIntent(file);
		} else if (MyFormat.checkFormat(filename, MyFormat.Packages)) {
			intent = OpenFile.getApkFileIntent(file);
		} else if (MyFormat.checkFormat(filename, MyFormat.Audios)) {
			intent = OpenFile.getAudioFileIntent(file);
		} else if (MyFormat.checkFormat(filename, MyFormat.Videos)) {
			intent = OpenFile.getVideoFileIntent(file);
		} else if (MyFormat.checkFormat(filename, MyFormat.Texts)) {
			intent = OpenFile.getTextFileIntent(file);
		} else if (MyFormat.checkFormat(filename, MyFormat.Pdfs)) {
			intent = OpenFile.getPdfFileIntent(file);
		} else if (MyFormat.checkFormat(filename, MyFormat.Words)) {
			intent = OpenFile.getWordFileIntent(file);
		} else if (MyFormat.checkFormat(filename, MyFormat.Excels)) {
			intent = OpenFile.getExcelFileIntent(file);
		} else if (MyFormat.checkFormat(filename, MyFormat.PPTs)) {
			intent = OpenFile.getPPTFileIntent(file);
		} else if(MyFormat.checkFormat(filename, MyFormat.Rars)){
			intent = OpenFile.getOtherFileIntent(file);
		}else {
			intent = null;
		}
		return intent;
	}

}
