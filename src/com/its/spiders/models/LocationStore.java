package com.its.spiders.models;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;

@ParseClassName("Location")
public class LocationStore extends ParseObject {
	
	
	public LocationStore()
	{
		
	}
	
	public String getStoryTitle()
	{
		return getString("storyTitle");
	}
	
	public void setStoryTitle(String storyTitle)
	{
		put("storyTitle",storyTitle);
	}
	
	public void setStoryDescription(String description)
	{
		put("storyDescription",description);
	}
	
	public String getStoryDescription()
	{
		return getString("storyDescription");
	}
	
	public void setLocationIndex(int locationIndex)
	{
		put("locationIndex",locationIndex);
	}
	
	public int getLocationIndex()
	{
		return getInt("locationIndex");
	}
	
	public void setLocation(double latitude,double longitude)
	{
		ParseGeoPoint geoPoint = new ParseGeoPoint(latitude, longitude);
		put("location",geoPoint);
	}
		
	public String getName() {
		return getString("name");
	}
	public void setName(String name) {
		put("name",name);
	}
	public String getDescription() {
		return getString("description");
	}
	public void setDescription(String description) {
		put("description",description);
	}

	public byte[] getPhoto() throws IOException{
		//ParseFile photo = getParseFile(getName()+"-phot.jpgo");
		ParseFile photo = getParseFile("photo");
		try {
			return  photo.getData();
		} catch (ParseException e) {
			e.printStackTrace();
			throw new IOException();
		}
	}

	public void setPhoto(byte[] img) throws IOException {
		//put(getName()+"-photo.jpg", FileUtils.readFileToByteArray(photo));
		put("photo", img);
	}

}
