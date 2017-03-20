package com.example.memory10;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.bumptech.glide.Glide;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;


import android.content.Context;
import android.graphics.Bitmap.Config;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class AlbumsAdapter extends BaseAdapter {

	private List<PhotoUpImageBucket> arrayList;
	private LayoutInflater layoutInflater;
	private ImageLoader imageLoader = ImageLoader.getInstance();
	private DisplayImageOptions options;
	private Toast mToast;
	private Context context;
	private String TAG = AlbumsAdapter.class.getSimpleName();
	public AlbumsAdapter(Context context){
		this.context=context;
		layoutInflater = LayoutInflater.from(context);
		arrayList = new ArrayList<PhotoUpImageBucket>();
		
	};
	@Override
	public int getCount() {
		return arrayList.size();
	}

	@Override
	public Object getItem(int position) {
		return arrayList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder holder;
		if (convertView == null) {
			holder = new Holder();
			convertView = layoutInflater.inflate(R.layout.ablums_adapter_item, parent, false);
			holder.image = (ImageView) convertView.findViewById(R.id.image);
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.count = (TextView) convertView.findViewById(R.id.count);
			convertView.setTag(holder);
		}else {
			holder = (Holder) convertView.getTag();
		}
		holder.count.setText(""+arrayList.get(position).getCount());
		holder.name.setText(arrayList.get(position).getBucketName());
		//holder.image.setScaleType(ImageView.ScaleType.CENTER_CROP);
		//imageLoader.displayImage("file://"+arrayList.get(position).getImageList().get(0).getImagePath(),
		//		holder.image, options);
				File file = new File(arrayList.get(position).getImageList().get(0).getImagePath()) ;
				Glide
			    .with(context)
			    .load(file).placeholder(R.drawable.album_default_loading_pic).//加载中显示的图片
		        error(R.drawable.album_default_loading_pic)//
			    .into(holder.image);
		return convertView;
	}

	class Holder{
		ImageView image;
		TextView name;
		TextView count;
	}

	public void setArrayList(List<PhotoUpImageBucket> arrayList) {
		this.arrayList = arrayList;
	}
}
