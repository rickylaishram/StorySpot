package com.its.spiders;

import java.io.File;
import java.util.Vector;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.its.utils.Utils;

public class GalleryAdapter extends ArrayAdapter<GalleryListItem>{
	private Context context;
	private int layoutResourceId;
	private Vector<GalleryListItem> data;
	private Holder holder;
	private GalleryListItem item;
	private View row;
	private int imageSize;
	
	public GalleryAdapter(Context context, int layoutResourceId, Vector<GalleryListItem> data) {
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
	        
	        holder.imageView = (ImageView) row.findViewById(R.id.im_photo);
	        
	        row.setTag(holder);
        } else {
        	holder = (Holder) convertView.getTag();
        }
        
        item = data.elementAt(position);
        //String filePath = item.filePath;
        //Bitmap photo = BitmapFactory.decodeFile(item.filePath);
        holder.imageView.setImageBitmap(Utils.decodeSampledBitmapFromFilePath(item.filePath, 400, 400));
                
        return row;
	}
	
	private static class Holder {
		public ImageView imageView;
	}
}
