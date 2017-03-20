package com.example.memory10;

import java.util.ArrayList;

import com.example.browser.ZoomTutorial;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

public class AlbumItemActivity extends Activity implements OnClickListener{

	private GridView gridView;
	
	private PhotoUpImageBucket photoUpImageBucket;
	private ArrayList<PhotoUpImageItem> selectImages;
	private AlbumItemAdapter adapter;
	ViewPager expandedView;
	View containerView;
	ZoomTutorial mZoomTutorial;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.album_item_images);
	    expandedView = (ViewPager)findViewById(R.id.detail_view);
	    containerView = (FrameLayout)findViewById(R.id.container);
	    ZoomTutorial mZoomTutorial = new ZoomTutorial(containerView, expandedView);
		init();
		setListener();
	}
	private void init(){
		gridView = (GridView) findViewById(R.id.album_item_gridv);
		
		selectImages = new ArrayList<PhotoUpImageItem>();
		
		Intent intent = getIntent();
		photoUpImageBucket = (PhotoUpImageBucket) intent.getSerializableExtra("imagelist");
		adapter = new AlbumItemAdapter(photoUpImageBucket.getImageList(), AlbumItemActivity.this);
		gridView.setAdapter(adapter);
	}
	private void setListener(){
		
		gridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				
				adapter.notifyDataSetChanged();
				adapter.setNotifyDataChange(position);	
			}
		});
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
	}
	
	

}
