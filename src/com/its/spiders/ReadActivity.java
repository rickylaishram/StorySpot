package com.its.spiders;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ReadActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_read);
		
		Bundle bundle = getIntent().getExtras();
		final String name = bundle.getString("name");
		String description = bundle.getString("description");
		final Double latitude = bundle.getDouble("latitude");
		final Double longitude = bundle.getDouble("longitude");
		byte[] img = bundle.getByteArray("img");
		final Integer page = bundle.getInt("page");
		
		Bitmap bm = BitmapFactory.decodeByteArray(img, 0, img.length);
		
		TextView tv_name = (TextView) findViewById(R.id.tv_read_name);
		TextView tv_desc = (TextView) findViewById(R.id.tv_read_description);
		ImageView iv_img = (ImageView) findViewById(R.id.iv_read_photo);
		Button btn_next = (Button) findViewById(R.id.btn_read_next);
		
		tv_name.setText(name);
		tv_desc.setText(description);
		iv_img.setImageBitmap(bm);
		
		btn_next.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Bundle bundle = new Bundle();
				bundle.putString("name", name);
				bundle.putDouble("longitude", longitude);
				bundle.putDouble("latitude", latitude);
				bundle.putInt("page", page + 1);
				
				Intent mIntent = new Intent(ReadActivity.this, MapViewActivity.class);
				mIntent.putExtras(bundle);
				startActivity(mIntent);
			}
		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.read, menu);
		return true;
	}

}
