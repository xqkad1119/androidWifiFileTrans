package com.xqkad.filetrans;

import com.xqkad.java.io.MyFormat;

import android.content.Context;
import android.content.Intent;

public class Tool {
	
	public static Intent getSelectIntent(Context context){
		Intent intent = new Intent();
		int[] iconIds = new int[12];
		int[] formats = new int[12];
		
		iconIds[0] = R.drawable.wenjianjia;
		formats[0] = MyFormat.DIR;
		iconIds[1] = R.drawable.pic;
		formats[1] = MyFormat.IMAGE;
		iconIds[2] = R.drawable.video;
		formats[2] = MyFormat.VIDEO;
		iconIds[3] = R.drawable.music;
		formats[3] = MyFormat.AUDIO;
		iconIds[4] = R.drawable.apk;
		formats[4] = MyFormat.APK;
		iconIds[5] = R.drawable.excel;
		formats[5] = MyFormat.EXCEL;
		iconIds[6] = R.drawable.html;
		formats[6] = MyFormat.HTML;
		iconIds[7] = R.drawable.pdf;
		formats[7] = MyFormat.PDF;
		iconIds[8] = R.drawable.ppt;
		formats[8] = MyFormat.PPT;
		iconIds[9] = R.drawable.rar;
		formats[9] = MyFormat.RAR;
		iconIds[10] = R.drawable.text;
		formats[10] = MyFormat.TXT;
		iconIds[11] = R.drawable.word;
		formats[11] = MyFormat.WORD;
		
		intent.setClass(context,
				com.xqkad.android.ui.selectActivity.SelectActivity.class);
		intent.putExtra("iconIds", iconIds);
		intent.putExtra("formats", formats);
		
		return intent;
	}
}
