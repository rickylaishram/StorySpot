package com.its.spiders;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Vector;

import org.apache.commons.io.FileUtils;

import android.location.Address;
import android.location.Geocoder;
//import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
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
import android.widget.Toast;

import com.its.utils.CloudStore;
import com.its.spiders.models.LocationStore;
import com.its.spiders.models.Story;

import com.its.utils.Utils;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.SaveCallback;

public class TagActivity extends Activity {
	
	private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
	private Uri fileUri;
	private Vector<GalleryListItem> MLIST = new Vector<GalleryListItem>();
	private ArrayAdapter<GalleryListItem> ADAPTER;
	private File image;
	private static int PIC_REQUEST;
	private Context CTX;
	public static final int MEDIA_TYPE_IMAGE = 1;
	
	private Double longitude;
	private Double latitude;
	private String name;
	private String description;
	private String story_name;
	private String story_description;
	private Integer index;
	
	private EditText et_name;
	private EditText et_desc;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tag);
		
		Bundle bundle = getIntent().getExtras();
		longitude = bundle.getDouble("longitude");
		latitude = bundle.getDouble("latitude");
		
		story_name = bundle.getString("name");
		story_description = bundle.getString("description");
		index = bundle.getInt("index");
		
		CTX = this;
		
		ADAPTER = new GalleryAdapter(this, R.layout.inflator_gallery, MLIST);
		
		final Button btnCamera = (Button) findViewById(R.id.btn_camera);
		Button btnNext = (Button) findViewById(R.id.btn_next);
		ListView llGallery = (ListView) findViewById(R.id.lv_photos);
		
		et_desc = (EditText) findViewById(R.id.et_loc_desc);
		et_name = (EditText) findViewById(R.id.et_name);
		
		llGallery.setAdapter(ADAPTER);
		
		btnCamera.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				//btnCamera.setEnabled(false);
				
				PIC_REQUEST = (int) System.currentTimeMillis();
				// create Intent to take a picture and return control to the calling application
			    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			    
			    File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Spiders");
			    if(!dir.exists()) {
			    	dir.mkdirs();
			    }
			    
			    image = new File(dir.getPath() + File.separator + PIC_REQUEST + ".jpg");
			    fileUri = Uri.fromFile(image); // create a file to save the image
			    intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file name
			    
			    // start the image capture Intent
			    startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
			}
		});
		
		btnNext.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				if(image != null) {
					CloudStore store = new CloudStore(CTX);
					
					LocationStore location = new LocationStore();
					location.setStoryTitle(story_name);
					location.setStoryDescription(story_description);
					location.setLocationIndex(index);
					location.setName(et_name.getText().toString());
					location.setDescription(et_desc.getText().toString());
					location.setLocation(latitude, longitude);
					
					Bitmap bm = Utils.decodeSampledBitmapFromFilePath(image.getPath(), 500, 500);
					
					File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + File.separator + "Spiders" + File.separator + "temp.jpg");
					
					
					/*ByteArrayOutputStream stream = new ByteArrayOutputStream();
					bm.compress(Bitmap.CompressFormat.JPEG, 85, stream);
					byte[] img = stream.toByteArray();
					
					ParseFile pFile = new ParseFile(PIC_REQUEST+".jpg", img);
					//pFile.put("ImageFile")
					//pFile.saveInBackground();
					
					ParseObject imgUpload = new ParseObject("ImageUpload");
					imgUpload.put("ImageName", PIC_REQUEST+".jpg");
					imgUpload.put("ImageFile", pFile);
					imgUpload.saveInBackground(new SaveCallback() {
						
						@Override
						public void done(ParseException e) {
							Log.e("Img", "done");
							
						}
					});*/
					
					FileOutputStream fOut;
					try {
						fOut = new FileOutputStream(file);
						bm.compress(Bitmap.CompressFormat.JPEG, 85, fOut);
					    fOut.flush();
					    fOut.close();
					} catch (FileNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				    bm.recycle();
					
					try {
						location.setPhoto(FileUtils.readFileToByteArray(file));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					if(store.persistLocation(location)) {
						finish();
					}
					Log.e("Bleh", "Bligh");
				} else {
					Toast.makeText(CTX, "Please take a photo first", Toast.LENGTH_SHORT).show();
				}
			}
		});
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.tag, menu);
		return true;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
	        if (resultCode == RESULT_OK) {
	        	//File dir = new File(Environment.getExternalStorageDirectory(), "Spiders");
	        	File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Spiders");
	        	File reimage = new File(dir.getPath() + File.separator + PIC_REQUEST + ".jpg");
	        	image = reimage;
	        	
			    GalleryListItem item = new GalleryListItem(reimage.getPath());
	        	MLIST.add(item);
	        	ADAPTER.notifyDataSetChanged();
	        }else if (resultCode == RESULT_CANCELED) {
	            // User cancelled the image capture
	        } else {
	            // Image capture failed, advise user
	        }
	    }
	}
}
