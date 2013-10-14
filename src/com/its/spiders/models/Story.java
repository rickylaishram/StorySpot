package com.its.spiders.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("Story")
public class Story extends ParseObject {
	
	private LocationStore title;
	private LocationStore description;
		
	public Story()
	{
		
	}

	public LocationStore getTitle() {
		return title;
	}

	public void setTitle(LocationStore title) {
		this.title = title;
	}

	public LocationStore getDescription() {
		return description;
	}

	public void setDescription(LocationStore description) {
		this.description = description;
	}

}
