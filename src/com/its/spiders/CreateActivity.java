package com.its.spiders;

import java.util.List;
import java.util.Vector;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.its.spiders.models.*;
import com.its.utils.Constants;

public class CreateActivity extends Activity {
	
	private LocationManager locationManager;
	private LocationListener locationListener;
	private double longitude, latitude;
	private Vector<CreateListItem> MLIST = new Vector<CreateListItem>();
	private ArrayAdapter<CreateListItem> ADAPTER;
	private String story_name = null;
	
	private Integer index = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create);
		
		Parse.initialize(this,Constants.applicationId, Constants.clientKey);
		ParseObject.registerSubclass(LocationStore.class);
		
		Button btnTag = (Button) findViewById(R.id.btn_tag);
		Button btnSave = (Button) findViewById(R.id.btn_save);
		final EditText name = (EditText) findViewById(R.id.et_create_name);
		final EditText desc = (EditText) findViewById(R.id.et_create_description);
		ListView list = (ListView) findViewById(R.id.lv_photos);
		
		ADAPTER = new CreateAdapter(this, R.layout.inflator_create, MLIST);
		
		list.setAdapter(ADAPTER);
		
		/* Location part
		 * Continuously update while user is in activity
		 */
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		
		latitude = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLatitude();
		longitude = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLongitude();
		
		locationListener = new LocationListener() {
			
			@Override
			public void onStatusChanged(String provider, int status, Bundle extras) {}
			
			@Override
			public void onProviderEnabled(String provider) {}
			
			@Override
			public void onProviderDisabled(String provider) {
				/* If GPS is disable launch Locations Settings */
				Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
				startActivity(intent);
			}
			
			@Override
			public void onLocationChanged(Location location) {
				latitude = location.getLatitude();
				longitude = location.getLongitude();
			}
		};
		
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
		
		btnTag.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String str_name = name.getText().toString();
				String str_desc = desc.getText().toString();
				
				story_name = str_name;
				
				name.setEnabled(false);
				desc.setEnabled(false);
				
				Bundle bundle = new Bundle();
				bundle.putDouble("longitude", longitude);
				bundle.putDouble("latitude", latitude);
				bundle.putString("name", str_name);
				bundle.putString("description", str_desc);
				bundle.putInt("index", index);
				
				Intent mIntent = new Intent(CreateActivity.this, TagActivity.class);
				mIntent.putExtras(bundle);
				startActivityForResult(mIntent, 0);
			}
		});
		
		btnSave.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.create, menu);
		return true;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		index++;
		if(story_name != null) {
			

			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
		}
	}
}
