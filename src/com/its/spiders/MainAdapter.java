package com.its.spiders;

import java.util.Vector;

import com.its.utils.Utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MainAdapter extends ArrayAdapter<MainListItem>{
	private Context context;
	private int layoutResourceId;
	private Vector<MainListItem> data;
	private Holder holder;
	private MainListItem item;
	private View row;
	private int imageSize;
	
	public MainAdapter(Context context, int layoutResourceId, Vector<MainListItem> data) {
		super(context, layoutResourceId, data);
		this.context			= context;
		this.layoutResourceId 	= layoutResourceId;
		this.data 				= data;
	}
	
	@Override
    public View getView(int position, View convertView, ViewGroup parent) {
        row = convertView;
        holder = new Holder();
        
        if(row == null) { 
	        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
	        row = inflater.inflate(layoutResourceId, parent, false);
	        
	        holder.name = (TextView) row.findViewById(R.id.tv_in_main_name);
	        holder.imageView1 = (ImageView) row.findViewById(R.id.iv_in_main_img1);
	        
	        row.setTag(holder);
        } else {
        	holder = (Holder) convertView.getTag();
        }
        
        item = data.elementAt(position);
        
        Bitmap bm = BitmapFactory.decodeByteArray(item.image, 0, item.image.length);
        bm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getWidth()*9/16);
        
        holder.imageView1.setImageBitmap(bm);
        holder.name.setText(item.name);
        
        return row;
	}
	
	private static class Holder {
		public TextView name;
		public ImageView imageView1;
	}
}
