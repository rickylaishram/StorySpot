package com.its.spiders;

import java.util.List;
import java.util.Vector;

import com.its.spiders.models.LocationStore;
import com.its.utils.Constants;
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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class MainActivity extends Activity {
	
	private Vector<MainListItem> MLIST = new Vector<MainListItem>();
	private ArrayAdapter<MainListItem> ADAPTER;
	private List<ParseObject> parseObjects;
	
	private LocationManager locationManager;
	private LocationListener locationListener;
	private double latitude;
	private double longitude;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
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
		
		Parse.initialize(this,Constants.applicationId, Constants.clientKey);
		ParseObject.registerSubclass(LocationStore.class);
		
		ListView list = (ListView) findViewById(R.id.lv_story);
		list.setDivider(null);
		
		ADAPTER = new MainAdapter(this, R.layout.inflator_story, MLIST);
		
		list.setAdapter(ADAPTER);
		
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				
				locationManager.removeUpdates(locationListener);
				
				String story = MLIST.get(arg2).name;
				
				Bundle bundle = new Bundle();
				bundle.putString("name", story);
				bundle.putDouble("longitude", longitude);
				bundle.putDouble("latitude", latitude);
				bundle.putInt("page", 1);
				
				Intent mIntent = new Intent(MainActivity.this, MapViewActivity.class);
				mIntent.putExtras(bundle);
				startActivity(mIntent);
			}
		
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	    case R.id.main_create:
	        launchCreateActivity();
	        return true;
	    default:
	        return super.onOptionsItemSelected(item);
	    }
	}
	
	private void launchCreateActivity() {
		Intent mIntent = new Intent(this, CreateActivity.class);
		startActivity(mIntent);
	}
	
	@Override
	public void onResume() {
		super.onResume();
	
		LocationStore l = new LocationStore();
			
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Location");
		query.whereExists("storyTitle");
		query.orderByDescending("createdAt");
		query.setLimit(1000);
		
		query.findInBackground(new FindCallback<ParseObject>() {
			public void done(List<ParseObject> resultList, ParseException e) {
				if (e == null) {
					parseObjects = resultList;
					MLIST.clear();
					Vector<String> stories = new Vector<String>();
					
					for(ParseObject item: resultList) {
			        	if((item != null) && !(stories.contains(item.get("storyTitle")))) {
			        		byte[] b = item.getBytes("photo");
			        		String name = item.getString("storyTitle");
			        		String desc = item.getString("storyDescription");
			        		if(b != null) {
			        			MainListItem cItem = new MainListItem(name, desc, b);
			        			MLIST.add(cItem);
			        			ADAPTER.notifyDataSetChanged();
			        		}
			        		stories.add(name);
			        	}
					}
			        	
					Log.d("score", "Retrieved " + resultList.size() + " scores");
					} else {
			        // Todo
					}
			  	}
			});
		}
}
