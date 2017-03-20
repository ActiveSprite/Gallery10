package com.example.browser;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap.Config;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.Toast;
import android.widget.ViewSwitcher.ViewFactory;



import com.example.memory10.PhotoUpImageBucket;
import com.example.memory10.PhotoUpImageItem;
import com.example.memory10.R;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;



public class SwitcherActivity extends Activity {

	    private ImageSwitcher imsw;
	    //private ImageView imageview;
	    private ImageLoader imageLoader = ImageLoader.getInstance();
	    private int mPosition;
		private int mLocationX;
		private int mLocationY;
		private int mWidth;
		private int mHeight;
		SmoothImageView imageView = null;
		private DisplayImageOptions options;
		
		ViewPager expandedView;
		ArrayList<PhotoUpImageItem> imglist;
		SwicherAdapter adapter;
	    @Override  
	    protected void onCreate(Bundle savedInstanceState) {  
	        super.onCreate(savedInstanceState); 
	        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
	        requestWindowFeature(Window.FEATURE_NO_TITLE);
	       setContentView(R.layout.swicher_activity);
	       expandedView=(ViewPager)findViewById(R.id.switchviewPager);
	    }  
	    public void onStart(){
	    	  super.onStart();
	    	Intent intent = getIntent();
	    	imglist = ( ArrayList<PhotoUpImageItem>) intent.getSerializableExtra("imagelist");
	      int position=getIntent().getIntExtra("position", 0);
	      adapter=new SwicherAdapter(imglist,this);
	      expandedView.setAdapter(adapter);
	      expandedView.setCurrentItem(position);
	      
	      }  
	    public void onBackPressed() {
			finish();
		}

		@Override
		protected void onPause() {
			super.onPause();
			if (isFinishing()) {
				overridePendingTransition(0, 0);
			}
		}


}