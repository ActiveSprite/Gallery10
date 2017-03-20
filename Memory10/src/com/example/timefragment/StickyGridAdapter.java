package com.example.timefragment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.browser.SwitcherActivity;
import com.example.memory10.PhotoActivity;
import com.example.memory10.R;
import com.example.timefragment.MyImageView.OnMeasureListener;


import com.tonicartos.widget.stickygridheaders.StickyGridHeadersSimpleAdapter;

public class StickyGridAdapter extends BaseAdapter implements
		StickyGridHeadersSimpleAdapter {

	private List<GridItem> list;
	private LayoutInflater mInflater;
	private GridView mGridView;
	private Point mPoint = new Point(0, 0);//用来封装ImageView的宽和高的对象 
	Context context;
	public StickyGridAdapter(Context context, List<GridItem> list,
			GridView mGridView) {
		this.context=context;
		this.list = list;
		mInflater = LayoutInflater.from(context);
		this.mGridView = mGridView;
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

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder mViewHolder;
		if (convertView == null) {
			mViewHolder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.grid_item, parent, false);
			mViewHolder.mImageView = (MyImageView) convertView
					.findViewById(R.id.grid_item);
			convertView.setTag(mViewHolder);
			
			 //用来监听ImageView的宽和高  
			mViewHolder.mImageView.setOnMeasureListener(new OnMeasureListener() {  
                  
                @Override  
                public void onMeasureSize(int width, int height) {  
                    mPoint.set(width, height);  
                }  
            }); 
			
		} else {
			mViewHolder = (ViewHolder) convertView.getTag();
		}

		String path = list.get(position).getPath();
	
		File file = new File(path) ;
		Glide
	    .with(context)
	    .load(file).placeholder(R.drawable.album_default_loading_pic).//加载中显示的图片
        error(R.drawable.album_default_loading_pic)//
	    .into(mViewHolder.mImageView);
		mViewHolder.mImageView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, PhotoActivity.class);				

				intent.putExtra("imagelist",(ArrayList)list);
				
//				Toast.makeText(mcontext, "file://"+list.get(position).getImagePath(), Toast.LENGTH_LONG).show();
				intent.putExtra("position", position);

				context.startActivity(intent);
				((Activity) context).overridePendingTransition(0, 0);
				
//				
			}
		});
		return convertView;
	}
	

	@Override
	public View getHeaderView(int position, View convertView, ViewGroup parent) {
		HeaderViewHolder mHeaderHolder;
		if (convertView == null) {
			mHeaderHolder = new HeaderViewHolder();
			convertView = mInflater.inflate(R.layout.header, parent, false);
			mHeaderHolder.mTextView = (TextView) convertView
					.findViewById(R.id.header);
			convertView.setTag(mHeaderHolder);
		} else {
			mHeaderHolder = (HeaderViewHolder) convertView.getTag();
		}

		mHeaderHolder.mTextView.setText(list.get(position).getTime());
		
		return convertView;
	}
    
	public static class ViewHolder {
		public MyImageView mImageView;
	}

	public static class HeaderViewHolder {
		public TextView mTextView;
	}

	@Override
	public long getHeaderId(int position) {
		return list.get(position).getSection();
	}

}
