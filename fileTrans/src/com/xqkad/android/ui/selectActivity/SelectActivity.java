package com.xqkad.android.ui.selectActivity;

import android.app.*;
import android.content.*;
import android.graphics.Color;
import android.os.*;
import android.widget.*;
import android.view.ViewGroup.*;

import com.xqkad.android.tools.OpenDir;
import com.xqkad.android.tools.StorageList;
import com.xqkad.android.ui.dialog.MyDg;

public class SelectActivity extends Activity {
    private ListView lv;
    private SelectAdapter adapter;
    public String path;
    private String[] storagePaths;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        storagePaths = getStoragePaths();
        path = "";
        initContent(this);
    }

    @Override
    public void onBackPressed() {
        if(path.equals("")){
            super.onBackPressed();
            return;
        }
        for(String p : storagePaths){
            if(path.equals(p)){
                path = "";
                setAdapter();
                return;
            }
        }
        String[] ps = path.split("/");
        path = "";
        for(int i=0;i<ps.length-1;i++){
            if(!ps[i].equals(""))
                path+="/"+ps[i];
        }
        setAdapter();
    }

    private void initContent(Context context){
        RelativeLayout layout = new RelativeLayout(context);
        RelativeLayout.LayoutParams lpm_parent = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
        RelativeLayout.LayoutParams lpm = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
        lv = new ListView(context);
        //TextView tv = new TextView(context);
        //////////////////
        layout.setLayoutParams(lpm_parent);
        layout.setBackgroundColor(Color.argb(255,33,33,33));
        lv.setLayoutParams(lpm);
        setAdapter();
        lv.setAdapter(adapter);
        layout.addView(lv);
//        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//            }
//        });
//        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//                return false;
//            }
//        });
        setContentView(layout);
    }

    private void setAdapter() {
        //Log.d("mytag", path);
        ItemData[] data = null;
        int[] formats = getIntent().getIntArrayExtra("formats");
        int[] ids = getIntent().getIntArrayExtra("iconIds");
        
        if(path.equals("")){
            data = Tool.openStorageList(storagePaths, Tool.getDirIconId(ids));
        }else
            data = Tool.getItemData(path,false, formats, ids);
        if(adapter == null) {
            adapter = new SelectAdapter(data, this, formats, ids);
            lv.setAdapter(adapter);
        }else{
            adapter.mData = data;
            adapter.notifyDataSetChanged();
        }
    }

    public void startdg(String text){
        final String select = path+"/"+text;
        
        MyDg.startDgConfirm(this, "选择 "+text, "", new MyDg.Impl.OnConfirm() {
			
			@Override
			public void OnConfirm(String text) {
				if(text == null)
					return;
				result(select, 0);
                SelectActivity.this.finish();
			}
		});
    }
    
    public void startdg2(String text){
        final String select = path+"/"+text;
        AlertDialog dg = null;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("选择 "+text);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                result(select, 0);
                SelectActivity.this.finish();
            }
        });
        builder.setNegativeButton("取消",null);
        dg = builder.show();
    }

    public void result(String select, int resultCode){
        Intent intent = new Intent();
        intent.putExtra("select",select);
        setResult(resultCode,intent);
    }

    public String[] getStoragePaths(){
        if(storagePaths == null)
            storagePaths = new StorageList(this).getVolumePaths();
        return storagePaths;
    }
}


