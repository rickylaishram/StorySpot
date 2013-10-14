package com.its.utils;

import android.content.Context;
import android.util.Log;

import com.its.spiders.models.LocationStore;
import com.its.spiders.models.Story;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.SaveCallback;


public class CloudStore 
{
	private Context context;
	private final String applicationId = "BNLheQFekTjnAwow1ffuCbtbkiXyMde5InzeDA0C";
	private final String clientKey = "yaIHjQATazmDH3suV45vjAQJ3PVVswpDsgKqxY03";
	private boolean initialized;
	
	public CloudStore(Context context)
	{
		this.context = context;
		this.initialize();
	}
	
	public void setConext(Context context)
	{
		this.context = context;
		this.initialize();
	}
	
	private void initialize()
	{
		ParseObject.registerSubclass(LocationStore.class);
		ParseObject.registerSubclass(Story.class);
		
		Parse.initialize(context, applicationId, clientKey);
		initialized = true;
	}
	
	public void persistStory(Story story)
	{
		if ( !initialized )
		{
			this.initialize();
		}
		
		story.saveEventually();
	}	
	
	/*public byte[] getImage()
	{
		ParseFile image = Pars
	}*/
	
	public Boolean persistLocation(LocationStore location)
	{
		if (!initialized)
		{
			this.initialize();
		}
		
		location.saveInBackground(new SaveCallback() {
			
			@Override
			public void done(ParseException e) {
				Log.e("SAVE_LOCATION", "Saved");
				if(e!=null)
				Log.e("Nope",e.getMessage());
				
			}
		});
		return true;
	}

}
