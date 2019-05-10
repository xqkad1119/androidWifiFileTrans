package com.xqkad.android.ui.selectActivity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;


public class SelectAdapter extends BaseAdapter {
    public ItemData[] mData;
    private SelectActivity mContext;
    private int[] formats, iconIds;
    public static boolean cb_show = false;

    public SelectAdapter(ItemData[] data, Context context,
    		int[] formats, int[] iconIds){
        this.mData = data;
        this.mContext = (SelectActivity) context;
        this.formats = formats;
        this.iconIds = iconIds;
    }

    @Override
    public int getCount() {
        return mData.length;
    }

    @Override
    public Object getItem(int position) {
        return mData[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ItemData data = mData[position];
        ItemView holder = null;
        if(convertView != null){
            holder = (ItemView)convertView;
        }else{
            holder = new ItemView(mContext);
            convertView = holder;
            holder.setOnLongClickListener(new onLongClickListener());
            holder.setOnClickListener(new onClickListener());
        }
        holder.setView(data);
        holder.setTag(data);
        return holder;
    }

    private class onClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            ItemData data = (ItemData)v.getTag();
            if(data.type==data.FILE){
            	mContext.startdg(data.name);
            }else{
            	mContext.path = data.path;
            	mData = Tool.getItemData(mContext.path, false, formats, iconIds);
            	SelectAdapter.this.notifyDataSetChanged();
            }
        }
    }

    private class onLongClickListener implements View.OnLongClickListener{

        @Override
        public boolean onLongClick(View v) {
            ItemData data = (ItemData)v.getTag();
            if(data.type==data.STORAGE)
                return false;
            mContext.startdg(data.name);
            return true;
        }
    }

    private class ItemView extends LinearLayout {
        //private LinearLayout layout;
        private ImageView img;
        private TextView tv;

        public ItemView(Context context){
            super(context);
            Activity ac = (Activity) context;
            DisplayMetrics metrics = new DisplayMetrics();
            ac.getWindowManager().getDefaultDisplay().getMetrics(metrics);
            int scale = (int) metrics.density;
            AbsListView.LayoutParams lp_lv = new AbsListView.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            LinearLayout.LayoutParams lp32 = new LinearLayout.LayoutParams(
                    66*scale, 66*scale);
            //layout = new LinearLayout(context);
            this.setLayoutParams(lp_lv);
            this.setBackgroundColor(Color.argb(255, 55,55,55));
            img = new ImageView(context);
            img.setLayoutParams(lp32);
            tv = new TextView(context);
            tv.setTextColor(Color.WHITE);
            int m = 14*scale;
            lp.setMargins(m,m,m,m);
            tv.setLayoutParams(lp);
        }

        public void setView(int imgid, String text){
            this.removeAllViews();
            img.setImageResource(imgid);
            this.addView(img);
            tv.setText(text);
            this.addView(tv);
        }
        public void setView(ItemData data){
            int imgid = data.imgid;
            String text = data.name;
            
            this.removeAllViews();
            img.setImageResource(imgid);
            this.addView(img);
            tv.setText(text);
            this.addView(tv);
        }
    }
}