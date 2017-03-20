package com.example.memory10;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import com.bumptech.glide.Glide;
import com.example.browser.SwitcherActivity;
import com.example.browser.ViewPagerAdapter;
import com.example.browser.ZoomTutorial;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.PauseOnScrollListener;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;


import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap.Config;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ImageView.ScaleType;

public class AlbumItemAdapter extends BaseAdapter {
	private List<PhotoUpImageItem> list;
	private LayoutInflater layoutInflater;
	private ImageLoader imageLoader;
	private DisplayImageOptions options;
	 private int selectPic = -1;
	 private Context mcontext;
	 private Toast mToast;
//	 ZoomTutorial mZoomTutorial;
//	 ViewPager expandedView;
	public AlbumItemAdapter(List<PhotoUpImageItem> list,Context context){
		this.list = list;
		this.mcontext=context;

		layoutInflater = LayoutInflater.from(context);
		
	}
	
	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	
	
	
	public void setNotifyDataChange(int id) {  
        selectPic = id;  
        super.notifyDataSetChanged();  
    }  
	
	
	

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final Holder holder=new Holder(); ;
		if (convertView == null) {
//			convertView = layoutInflater.inflate(R.layout.album_item_images_item_view, parent, false);
//			holder = new Holder();
//			holder.imageView = (ImageView) convertView.findViewById(R.id.image_item);
			holder.imageView = new ImageView(mcontext);
			holder.imageView.setLayoutParams(new ViewGroup.LayoutParams( 
			         LayoutParams.FILL_PARENT, 
			        (int) getRawSize(TypedValue.COMPLEX_UNIT_DIP, 110))); 
			holder.imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

		}else {
			holder.imageView = (ImageView) convertView;
		}
		
		
        
	//	imageLoader.displayImage("file://"+list.get(position).getImagePath(), holder.imageView, options);
		
		File file = new File(list.get(position).getImagePath()) ;
		Log.i("position",String.valueOf(position));
		Log.i("paths",list.get(position).getImagePath());
		Glide
	    .with(mcontext)
	    .load(file).placeholder(R.drawable.album_default_loading_pic).//加载中显示的图片
        error(R.drawable.album_default_loading_pic)//
	    .into(holder.imageView);
		holder.imageView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mcontext, SwitcherActivity.class);				
//				intent.putExtra("images", list.get(position).getImagePath());
//				intent.putExtra("imagelist", );
				intent.putExtra("imagelist",(ArrayList)list);
				
//				Toast.makeText(mcontext, "file://"+list.get(position).getImagePath(), Toast.LENGTH_LONG).show();
				intent.putExtra("position", position);
//				int[] location = new int[2];
//				holder.imageView.getLocationOnScreen(location);
//				intent.putExtra("locationX", location[0]);
//				intent.putExtra("locationY", location[1]);
//
//				intent.putExtra("width", holder.imageView.getWidth());
//				intent.putExtra("height", holder.imageView.getHeight());
				mcontext.startActivity(intent);
				((Activity) mcontext).overridePendingTransition(0, 0);
				
//				setViewPagerAndZoom(holder.imageView, position);
			}
		});
		

		
		
		return holder.imageView;
	}
	public void setViewPagerAndZoom(View v ,int position) {
		ViewPager expandedView = (ViewPager)((Activity)mcontext).findViewById(R.id.detail_view);
		View containerView = (FrameLayout)((Activity)mcontext).findViewById(R.id.container);
		ZoomTutorial mZoomTutorial = new ZoomTutorial(containerView, expandedView);
		
		ViewPagerAdapter adapter = new ViewPagerAdapter(mcontext, 
				list,mZoomTutorial);
		
		expandedView.setAdapter(adapter);
		expandedView.setCurrentItem(position);
		mZoomTutorial.zoomImageFromThumb(v);
		
	}
	public float getRawSize(int unit, float value) { 
        Resources res = mcontext.getResources(); 
        return TypedValue.applyDimension(unit, value, res.getDisplayMetrics()); 
    } 

	
	class Holder{
		ImageView imageView;
		
	}
	
}