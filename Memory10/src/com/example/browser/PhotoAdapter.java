package com.example.browser;

import java.io.File;
import java.util.ArrayList;

import uk.co.senab.photoview.PhotoView;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.LayoutParams;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.bumptech.glide.Glide;
import com.example.memory10.PhotoUpImageItem;
import com.example.memory10.R;
import com.example.timefragment.GridItem;
import com.example.timefragment.ZoomImageView;

public class PhotoAdapter extends PagerAdapter{
    ArrayList<GridItem> list;
	   
   private Context mContext;
   public PhotoAdapter( ArrayList<GridItem> list,Context context){
	   this.list=list;
	   this.mContext=context;
   }
	@Override
	public int getCount() {		
		if(list!=null){
		return list.size();}
		else{
			return 0;
		}
	}
	public View instantiateItem(ViewGroup container, final int position) {

		final ZoomImageView imageView = new ZoomImageView(mContext);
		
		imageView.setScaleType(ScaleType.FIT_CENTER);
        File file = new File(list.get(position).getPath());
		Glide
	    .with(mContext)
	    .load(file).placeholder(R.drawable.album_default_loading_pic).
        error(R.drawable.album_default_loading_pic)
	    .into(imageView);
		container.addView(imageView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
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
