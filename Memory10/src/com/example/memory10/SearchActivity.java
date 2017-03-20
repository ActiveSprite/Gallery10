package com.example.memory10;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import com.bumptech.glide.Glide;

import com.example.face.FaceitemActivity;
import com.example.face.LoadFace;
import com.example.face.LoadFace.GetFaceList;
import com.example.fragment.FaceFragment;
import com.example.fragment.ShowImageActivty;
import com.example.memory10.AlbumItemAdapter.Holder;
import com.example.memory10.SearchTask.GetPictureList;
import com.example.sqlite.FaceBucket;
import com.example.sqlite.FaceDao;
import com.example.sqlite.Faces;
import com.example.sqlite.Picture;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;

import android.app.Activity;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap.Config;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore.Images.Media;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

public class SearchActivity extends Activity implements OnClickListener{
	
	Handler handler;
	
	private GridView gridView;
	MyAdapter adapter;
	String pitem;
	SearchTask searchtask;
	ArrayList<Picture> picturebucket=new ArrayList<Picture>();
	protected void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
       // this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	    setContentView(R.layout.face_item_activity);
	    
	    adapter=new MyAdapter(this);
	    gridView = (GridView) findViewById(R.id.album_item_gridvface);
	    gridView.setAdapter(adapter);
	    pitem= getIntent().getStringExtra("searchtext");
	    loadData();
	    
	}
	
	private void loadData(){
		searchtask= new SearchTask(this, pitem);
		searchtask.setGetPictureList(new GetPictureList() {
			
			@Override
			public void getPictureList(ArrayList<Picture> list) {
				// TODO Auto-generated method stub
				adapter.setArrayList( list);
				adapter.notifyDataSetChanged();
				SearchActivity.this.picturebucket = list;
				
			}
		});
		searchtask.execute();
	}
	 class MyAdapter extends BaseAdapter{
			Context context;
			private LayoutInflater layoutInflater;
			private ImageLoader imageLoader;
			private DisplayImageOptions options;
			ArrayList<Picture> list=new ArrayList<Picture>();
	       public MyAdapter(Context context){
	    	   this.context=context;
	    	   layoutInflater = LayoutInflater.from(context);
	    	   
	    	   imageLoader = ImageLoader.getInstance();
	    	   File cacheDir = StorageUtils.getOwnCacheDirectory(context, "imageloader/Cache"); 
	    	   ImageLoaderConfiguration config = new ImageLoaderConfiguration 
	    				.Builder(context) 
	    				.memoryCacheExtraOptions(480, 800) // max width, max height 
	    				.threadPoolSize(3)//线程池内加载的数量 
	    				.threadPriority(Thread.NORM_PRIORITY - 2)  //降低线程的优先级保证主UI线程不受太大影响
	    				.denyCacheImageMultipleSizesInMemory() 
	    				.memoryCache(new LruMemoryCache(10 * 1024 * 1024)) //建议内存设在5-10M,可以有比较好的表现
	    				.memoryCacheSize(5 * 1024 * 1024) 
	    				.discCacheSize(50 * 1024 * 1024) 
	    				.discCacheFileNameGenerator(new Md5FileNameGenerator()) 
	    				.tasksProcessingOrder(QueueProcessingType.LIFO) 
	    				.discCacheFileCount(100) //缓存的文件数量 
	    				.discCache(new UnlimitedDiscCache(cacheDir)) 
	    				.defaultDisplayImageOptions(DisplayImageOptions.createSimple()) 
	    				.imageDownloader(new BaseImageDownloader(context, 5 * 1000, 30 * 1000)) // connectTimeout (5 s), readTimeout (30 s)
	    				.writeDebugLogs() 
	    				.build();
	   		imageLoader.init(config);
	   		options = new DisplayImageOptions.Builder()
	   				.showStubImage(R.drawable.album_default_loading_pic) 
	   				.showImageForEmptyUri(R.drawable.album_default_loading_pic) 
	   				.showImageOnFail(R.drawable.album_default_loading_pic)
	   				.cacheInMemory(true) 
	   				.cacheOnDisc(true) 
	   				.bitmapConfig(Config.RGB_565)
	   				.imageScaleType(ImageScaleType.EXACTLY)
	   				.build(); 
	       }
			@Override
			public int getCount() {
				if(list!=null){
				return list.size();}
				else{
					return 0;
				}
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
		       // selectPic = id;  
		        super.notifyDataSetChanged();  
		    }  
			
			@Override
			public View getView(final int position, View convertView, ViewGroup parent) {
				Holder holder=null ;
				
				if (convertView == null) {
//					holder=new Holder();
//					convertView=new View(context);
					convertView = layoutInflater.inflate(R.layout.album_item_images_item_view, parent, false);
					holder = new Holder();
					holder.imageView = (ImageView) convertView.findViewById(R.id.image_item);
					convertView.setTag(holder);
					
				}else {
                    holder=(Holder)convertView.getTag();
                    
				}
				 ImageView imageView = holder.imageView;
		            

				if(list.get(position)!=null){
					File file = new File(list.get(position).origpath) ;
					
					Glide
				    .with(context)
				    .load(file).placeholder(R.drawable.album_default_loading_pic).//加载中显示的图片
			        error(R.drawable.album_default_loading_pic)//
				    .into(holder.imageView);
					convertView.setTag(holder);
				//	imageLoader.displayImage("file://"+list.get(position).origpath, holder.imageView, options);
					}
				holder.imageView.setOnClickListener(new View.OnClickListener(){

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						Toast.makeText(context, list.get(position).origpath, Toast.LENGTH_SHORT).show();
					}
					
				});
				return convertView;
			}
			public float getRawSize(int unit, float value) { 
		        Resources res = context.getResources(); 
		        return TypedValue.applyDimension(unit, value, res.getDisplayMetrics()); 
		    } 
			void setArrayList(ArrayList<Picture> list){
				this.list=list;
			}
			class Holder{
				ImageView imageView;
			}
		}
		
		
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
	}

}
