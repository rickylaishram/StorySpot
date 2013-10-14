package com.its.spiders;

import java.util.Vector;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.its.utils.Utils;

public class CreateAdapter extends ArrayAdapter<CreateListItem>{
	
	private Context context;
	private int layoutResourceId;
	private Vector<CreateListItem> data;
	private Holder holder;
	private CreateListItem item;
	private View row;
	private int imageSize;
	
	public CreateAdapter(Context context, int layoutResourceId, Vector<CreateListItem> data) {
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
	        
	        holder.imageView = (ImageView) row.findViewById(R.id.iv_in_cr_img1);
	        holder.name = (TextView) row.findViewById(R.id.tv_in_cr_name);
	        holder.description = (TextView) row.findViewById(R.id.tv_in_cr_desc);
	        
	        row.setTag(holder);
        } else {
        	holder = (Holder) convertView.getTag();
        }
        
        item = data.elementAt(position);
        
        holder.imageView.setImageBitmap(BitmapFactory.decodeByteArray(item.file, 0, item.file.length));
        holder.name.setText(item.name);
        holder.description.setText(item.description);
        
        return row;
	}
	
	private static class Holder {
		public TextView name;
		public TextView description;
		public ImageView imageView;
	}
}
