package com.example.browser;

import java.io.File;
import java.util.List;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.memory10.PhotoUpImageItem;
import com.example.memory10.R;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;

import android.content.Context;
import android.graphics.Bitmap.Config;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.LayoutParams;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class ViewPagerAdapter extends PagerAdapter {
	List<PhotoUpImageItem> list;
	
	private Context mContext;
	private ZoomTutorial mZoomTutorial; 
	
	public ViewPagerAdapter( Context context ,List<PhotoUpImageItem> list,ZoomTutorial zoomTutorial) {
		this.list = list;
		this.mContext = context;
		this.mZoomTutorial = zoomTutorial;
		
		
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public View instantiateItem(ViewGroup container, final int position) {

		final ImageView imageView = new ImageView(mContext);
		//imageView.setImageResource(sDrawables[position]);
		//imageLoader.displayImage("file://"+list.get(position).getImagePath(),imageView, options);
		File file = new File(list.get(position).getImagePath()) ;
		Glide
	    .with(mContext)
	    .load(file).placeholder(R.drawable.album_default_loading_pic).//加载中显示的图片
        error(R.drawable.album_default_loading_pic)
        .diskCacheStrategy(DiskCacheStrategy.RESULT)
       //保存最终图片
        . thumbnail(0.1f)
	    .into(imageView);
		container.addView(imageView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		
		imageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				mZoomTutorial.closeZoomAnim(position);
			}
		});
		return imageView;
	}
	


	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View) object);
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == object;
	}

}
