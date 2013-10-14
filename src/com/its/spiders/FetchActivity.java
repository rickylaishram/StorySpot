package com.its.spiders;

import java.util.List;

import com.its.spiders.models.LocationStore;
import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ProgressCallback;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class FetchActivity extends Activity 
{
	
	private final String applicationId = "BNLheQFekTjnAwow1ffuCbtbkiXyMde5InzeDA0C";
	private final String clientKey = "yaIHjQATazmDH3suV45vjAQJ3PVVswpDsgKqxY03";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fetch_layout);
		
		Button fetchButton = (Button) findViewById(R.id.fetch_button);
		Parse.initialize(this, applicationId, clientKey);
		ParseObject.registerSubclass(LocationStore.class);
		
		fetchButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				LocationStore l = new LocationStore();
				
				ParseQuery<ParseObject> query = ParseQuery.getQuery("Location");
				query.whereEqualTo("name", "hdj");
				query.findInBackground(new FindCallback<ParseObject>() {
				    public void done(List<ParseObject> resultList, ParseException e) {
				        if (e == null) {
				        	
				        	for(ParseObject item: resultList) {
				        		if(item != null) {
				        			byte[] b = item.getBytes("photo");
				        			ImageView myImg = (ImageView) findViewById(R.id.fetched_image);
				        			myImg.setImageBitmap(BitmapFactory.decodeByteArray(b, 0, b.length));
				        			item.get("name");
				        		}
				        	}
				        	
				            Log.d("score", "Retrieved " + resultList.size() + " scores");
				        } else {
				            Log.d("score", "Error: " + e.getMessage());
				        }
				    }
				});
				
				/*ParseFile pf= (ParseFile)l.get("photo");
				pf.getDataInBackground(new GetDataCallback() {
					
					@Override
					public void done(byte[] data, ParseException e) {
						if(e!=null)
						{
							Log.e("Image fetch error", e.getMessage());
							return;
						}
						
						Log.e("Done", data.toString());
						
						
					}
				}, new ProgressCallback() {
					
					@Override
					public void done(Integer percentDone) {
						Log.e("Progress", ""+percentDone);
						
					}
				});*/
				
			}
		});
	}

}
