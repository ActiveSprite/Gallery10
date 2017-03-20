package com.example.fragment;

import java.io.File;
import java.util.ArrayList;

import com.bumptech.glide.Glide;

import com.example.face.MyViewPagerAdapter;
import com.example.memory10.R;
import com.example.sqlite.FaceBucket;
import com.example.sqlite.Faces;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap.Config;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ImageView.ScaleType;


public class ShowImageActivty extends Activity {

    private ImageSwitcher imsw;
    //private ImageView imageview;
   
    private int mPosition;
	private int mLocationX;
	private int mLocationY;
	private int mWidth;
	private int mHeight;
	ImageView imageView = null;
	ViewPager expandedView;
	private Toast mToast;
	MyViewPagerAdapter adapter;
	ArrayList<Faces> faceimglist;
    @Override  
    protected void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        final String pitem= getIntent().getStringExtra("images");
        setContentView(R.layout.viewpager_activity); 
//        File file = new File(pitem) ;
//		Glide
//	    .with(this)
//	    .load(file).placeholder(R.drawable.album_default_loading_pic).//加载中显示的图片
//        error(R.drawable.album_default_loading_pic)//
//	    .into(imageView);
       // Toast.makeText(this, "file://"+pitem, Toast.LENGTH_LONG).show();   
    }  
    
    public void onStart(){
  	  super.onStart();
  	Intent intent = getIntent();
  	faceimglist = (ArrayList<Faces>) intent.getSerializableExtra("imagelist");
  	Log.i("Faces",faceimglist.get(0).origpath);
    int position=getIntent().getIntExtra("position", 0);
    expandedView = (ViewPager)findViewById(R.id.viewPager);
    adapter=new MyViewPagerAdapter(faceimglist,this);
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
