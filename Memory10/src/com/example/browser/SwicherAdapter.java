package com.example.browser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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
import com.example.timefragment.ZoomImageView;

public class SwicherAdapter extends PagerAdapter{
	    ArrayList<PhotoUpImageItem> list;
	   
	   private Context mContext;
	   public SwicherAdapter( ArrayList<PhotoUpImageItem> list,Context context){
		   this.list=list;
		   this.mContext=context;
	   }
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			if(list!=null){
			return list.size();}
			else{
				return 0;
			}
		}
		public View instantiateItem(ViewGroup container, final int position) {

			final ZoomImageView imageView = new ZoomImageView(mContext);
			//imageView.setImageResource(sDrawables[position]);
			//imageLoader.displayImage("file://"+list.get(position).getImagePath(),imageView, options);
			//imageView.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
			imageView.setScaleType(ScaleType.FIT_CENTER);
	        File file = new File(list.get(position).getImagePath());
			Glide
		    .with(mContext)
		    .load(file).placeholder(R.drawable.album_default_loading_pic).//加载中显示的图片
	        error(R.drawable.album_default_loading_pic)//
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
