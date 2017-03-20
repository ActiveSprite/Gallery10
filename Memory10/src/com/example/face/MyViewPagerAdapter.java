package com.example.face;

import java.io.File;
import java.util.ArrayList;

import com.bumptech.glide.Glide;
import com.example.memory10.R;
import com.example.sqlite.Faces;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.LayoutParams;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

public class MyViewPagerAdapter extends PagerAdapter{
   ArrayList<Faces> facelist=new  ArrayList<Faces>();
   private Context mContext;
   public MyViewPagerAdapter( ArrayList<Faces> facelist,Context context){
	   this.facelist=facelist;
	   this.mContext=context;
   }
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if(facelist!=null){
		return facelist.size();}
		else{
			return 0;
		}
	}
	public View instantiateItem(ViewGroup container, final int position) {

		final ImageView imageView = new ImageView(mContext);
		//imageView.setImageResource(sDrawables[position]);
		//imageLoader.displayImage("file://"+list.get(position).getImagePath(),imageView, options);
		//imageView.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
		imageView.setScaleType(ScaleType.FIT_CENTER);
        File file = new File(facelist.get(position).origpath);
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
