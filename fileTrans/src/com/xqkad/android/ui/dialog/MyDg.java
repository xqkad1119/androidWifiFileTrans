package com.xqkad.android.ui.dialog;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnShowListener;
import android.graphics.Color;
import android.view.ViewGroup;
import android.widget.*;

public class MyDg {
	public interface Impl{
		interface OnConfirm{
			public void OnConfirm(String str);
		}
		interface OnDengLu{
			public void OnDengLu(String str1, String str2);
		}
	}
	
	static final int backColor = Color.argb(255, 33, 33, 33);
	static final int textColor = Color.argb(255, 255, 255, 255);
	
	static final int titleSize = 24;
	static final int textSize = 28;
	static final int btnTextSize = 21;

	public static void startDgConfirm(Context context, String msg, 
			String title, final Impl.OnConfirm impl){
		startDgOneTV(context, msg, title, impl, 2);
	}
	
	public static void startDgMsg(Context context, String msg, 
			String title, final MyDg.Impl.OnConfirm impl){
		startDgOneTV(context, msg, title, impl, 1);
	}
	
	public static void startDgOneTV(Context context, String msg, 
			String title, final Impl.OnConfirm impl, int btnCount){
		//final Context f_context = context;
		final String f_msg = msg;
		AlertDialog dg = null;
		AlertDialog.Builder b = new AlertDialog.Builder(context);
		LinearLayout layout = new LinearLayout(context);
        int a = RelativeLayout.LayoutParams.WRAP_CONTENT;
        RelativeLayout layout_t = new RelativeLayout(context);
        RelativeLayout.LayoutParams lp_t = new RelativeLayout.LayoutParams(a,a);
        lp_t.addRule(RelativeLayout.CENTER_HORIZONTAL);
        layout.setOrientation(LinearLayout.VERTICAL);
		
		final TextView tt = new TextView(context);
        tt.setTextColor(textColor);
        tt.setTextSize(titleSize);
        tt.setText(""+title);
        tt.setLayoutParams(lp_t);
        layout_t.addView(tt);
        
        final TextView tv = new TextView(context);
        tv.setTextColor(textColor);
        tv.setTextSize(textSize);
        tv.setText(""+msg);
        
        layout.addView(layout_t);
        layout.addView(tv);
        b.setView(layout);
        b.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            	//startDgSave(f_context, SocketThread.savePath_def,"save", impl);
            	if(impl!=null)
            		impl.OnConfirm(f_msg);
            }
        });
        if(btnCount == 2){
        	b.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if(impl!=null)
                    	impl.OnConfirm(null);
                }
            });
        }
        dg = b.create();
        dg.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                if(impl!=null)
                	impl.OnConfirm(null);
            }
        });
        dg.setOnShowListener(new OnShow());
        dg.show();
    }
	
	public static void startDgOneET(Context context, String text, 
			String title, final Impl.OnConfirm impl){
		//final Context f_context = context;
		//final String f_msg = text;
		AlertDialog dg = null;
		AlertDialog.Builder b = new AlertDialog.Builder(context);
		LinearLayout layout = new LinearLayout(context);
        int a = RelativeLayout.LayoutParams.WRAP_CONTENT;
        RelativeLayout layout_t = new RelativeLayout(context);
        RelativeLayout.LayoutParams lp_t = new RelativeLayout.LayoutParams(a,a);
        lp_t.addRule(RelativeLayout.CENTER_HORIZONTAL);
        layout.setOrientation(LinearLayout.VERTICAL);
		
		final TextView tt = new TextView(context);
        tt.setTextColor(textColor);
        tt.setTextSize(titleSize);
        tt.setText(""+title);
        tt.setLayoutParams(lp_t);
        layout_t.addView(tt);
        
        final EditText et = new EditText(context);
        et.setTextColor(textColor);
        et.setTextSize(textSize);
        et.setText(""+text);
        
        layout.addView(layout_t);
        layout.addView(et);
        b.setView(layout);
        b.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            	//startDgSave(f_context, SocketThread.savePath_def,"save", impl);
            	if(impl!=null)
            		impl.OnConfirm(""+et.getText().toString());
            }
        });
        if(impl != null){
        	b.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if(impl!=null)
                    	impl.OnConfirm(null);
                }
            });
        }
        dg = b.create();
        dg.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                if(impl!=null)
                	impl.OnConfirm(null);
            }
        });
        dg.setOnShowListener(new OnShow());
        dg.show();
	}
	
	public static void startDgDenglu(Context context, String tv1_str, 
			String tv2_str, String et1_str, String et2_str, final Impl.OnDengLu impl){
		AlertDialog dg = null;
    	AlertDialog.Builder builder = new AlertDialog.Builder(context);
        ViewGroup.LayoutParams pr = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT );
        LinearLayout layout = new LinearLayout(context);
        LinearLayout layout_ip = new LinearLayout(context);
        LinearLayout layout_sc = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        final TextView tv_ip = new TextView(context);
        final TextView tv_sc = new TextView(context);
        final EditText et1 = new EditText(context);
        final EditText et2 = new EditText(context);
        tv_ip.setText(""+tv1_str);
        tv_sc.setText(""+tv2_str);
        et1.setText(""+et1_str);
        et2.setText(""+et2_str);
        et1.setLayoutParams(pr);
        et2.setLayoutParams(pr);
        tv_ip.setTextColor(textColor);
        tv_sc.setTextColor(textColor);
        et1.setTextColor(textColor);
        et2.setTextColor(textColor);
        layout_ip.addView(tv_ip);
        layout_ip.addView(et1);
        layout_sc.addView(tv_sc);
        layout_sc.addView(et2);
        layout.addView(layout_ip);
        layout.addView(layout_sc);
        builder.setView(layout);

        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            	if(impl==null)
            		return;
            	String str1 = et1.getText().toString();
            	String str2 = et2.getText().toString();
            	impl.OnDengLu(str1, str2);
            }
        });
        dg = builder.create();
        dg.setOnShowListener(new OnShow());
        dg.show();
	}
	
	static class OnShow implements OnShowListener{

		public OnShow(){}
		
		@Override
		public void onShow(DialogInterface dialog) {
			AlertDialog ad = (AlertDialog)dialog;
			Button bt1 = ad.getButton(AlertDialog.BUTTON_POSITIVE);
			Button bt2 = ad.getButton(AlertDialog.BUTTON_NEGATIVE);
			if(bt1!=null){
				bt1.setBackgroundColor(backColor);
				bt1.setTextColor(textColor);
				bt1.setTextSize(btnTextSize);
			}
			if(bt2!=null){
				bt2.setBackgroundColor(backColor);
				bt2.setTextColor(textColor);
				bt2.setTextSize(btnTextSize);
			}
		}
	}
}




