package com.its.spiders;

import java.util.List;

import com.google.android.gms.internal.bt;
import com.google.android.maps.MapActivity;
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
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.its.utils.Utils;

public class MapViewActivity extends Activity {
	
	private WebView webView;
	private LocationManager locationManager;
	private LocationListener locationListener;
	private double latitude;
	private double longitude;
	//private double prevLatitude = 0;
	//private double prevLongitude = 0;
	//private double moveThreshold = .001;
	
	private String name;
	private double pre_longitude;
	private double pre_latitude;
	private Integer page;
	private String description;
	private byte[] img;
	
	private Double goal_latitude;
	private Double goal_longitude;
	
	private TextView tv_name;
	private TextView tv_dist;
	private WebView webView1;
	private Button btn_next;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view);
		
		webView = (WebView) findViewById(R.id.webView1);
		tv_name = (TextView) findViewById(R.id.tv_view_name);
		tv_dist = (TextView) findViewById(R.id.tv_view_dist);
		btn_next = (Button) findViewById(R.id.btn_view_next);
	    
		Bundle bundle = getIntent().getExtras();
		name = bundle.getString("name");
		page = bundle.getInt("page");
		pre_latitude = bundle.getDouble("latitude");
		pre_longitude = bundle.getDouble("longitude");
		
		fetchData();
		
		Parse.initialize(this,Constants.applicationId, Constants.clientKey);
		ParseObject.registerSubclass(LocationStore.class);
		
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
				//if((Math.abs(location.getLongitude() - longitude) > 0.00001) || (Math.abs(location.getLatitude() - latitude) > 0.00001)) {
					latitude = location.getLatitude();
					longitude = location.getLongitude();
					
					Double dist = Utils.distFrom(latitude, longitude, goal_latitude, goal_longitude);
					// ???
					/*if (Math.abs( prevLatitude - latitude) >= moveThreshold || Math.abs( prevLongitude - longitude) >= moveThreshold) {
						loadWebView(latitude, longitude, goal_latitude, goal_longitude);
						prevLatitude = latitude;
						prevLongitude = longitude;
					}*/
					tv_dist.setText(dist+"");
					
					if(dist < 0.01) {
						btn_next.setEnabled(true);
					}
				//}
			}
		};
		
		btn_next.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Bundle bundle = new Bundle();
				bundle.putString("name", name);
				bundle.putInt("page", page);
				bundle.putString("description", description);
				bundle.putDouble("longitude", longitude);
				bundle.putDouble("latitude", latitude);
				bundle.putByteArray("img", img);
				
				Intent mIntent = new Intent(MapViewActivity.this, ReadActivity.class);
				mIntent.putExtras(bundle);
				startActivity(mIntent);
			}
		});
		
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.view, menu);
		return true;
	}
	
	private void loadWebView(Double start_latitude, Double start_longitude, Double stop_latitude, Double stop_longitude) {
		webView.getSettings().setJavaScriptEnabled(true);
	    webView.setWebViewClient(new WebViewClient());
	    webView.loadUrl("http://maps.google.com/maps?" +"saddr="+start_latitude+","+ start_longitude + "&daddr="+ stop_latitude+","+stop_longitude);
	}
	
	private void fetchData() {
		LocationStore l = new LocationStore();
		
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Location");
		query.whereEqualTo("storyTitle", name);
		//query.whereEqualTo("locationIndex", page);
		
		query.findInBackground(new FindCallback<ParseObject>() {
		    public void done(List<ParseObject> resultList, ParseException e) {
		        if (e == null) {
		        	for(ParseObject item: resultList) {
		        		if(item != null) {
		        			img = item.getBytes("photo");
		        			name = item.getString("name");
		        			description = item.getString("description");
		        			goal_latitude = item.getParseGeoPoint("location").getLatitude();
		        			goal_longitude = item.getParseGeoPoint("location").getLongitude();
		        			
		        			loadWebView(pre_latitude, pre_longitude, goal_latitude, goal_longitude);
		        			tv_name.setText(name);
		        			tv_dist.setText(Utils.distFrom(pre_latitude, pre_longitude, goal_latitude, goal_longitude)+"");
		        		
		        			if(Utils.distFrom(latitude, longitude, goal_latitude, goal_longitude) < 0.01) {
		        				btn_next.setEnabled(true);
		        			}
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
