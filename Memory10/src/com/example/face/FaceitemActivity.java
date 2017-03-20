package com.example.face;





import java.io.File;
import java.util.ArrayList;
import java.util.List;



import com.bumptech.glide.Glide;

import com.example.fragment.ShowImageActivty;
import com.example.memory10.R;
import com.example.sqlite.FaceBucket;
import com.example.sqlite.Faces;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class FaceitemActivity extends Activity{
	private GridView gridView;
	private FaceBucket FaceImageBucket;
	private MyAdapter adapter;
	 protected void onCreate(Bundle savedInstanceState) {
	    	SpeechUtility.createUtility(this, SpeechConstant.APPID +"=583e502f");
	        super.onCreate(savedInstanceState);
	        //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			//setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
	        setContentView(R.layout.face_item_activity);
	        gridView = (GridView) findViewById(R.id.album_item_gridvface);
	        Intent intent = getIntent();
			FaceImageBucket = (FaceBucket) intent.getSerializableExtra("imagelist");
			adapter = new MyAdapter(FaceImageBucket.facelist, FaceitemActivity.this);
			gridView.setAdapter(adapter);
			String pitem= getIntent().getStringExtra("searchtext");
			onItemClick();
	 }
	 private void onItemClick(){
		 gridView.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					Intent intent = new Intent(FaceitemActivity.this,ShowImageActivty.class);
					intent.putExtra("imagelist", FaceImageBucket.facelist);
					intent.putExtra("position", position);
					startActivity(intent);
					FaceitemActivity.this.overridePendingTransition(0, 0);
				}
			});
		}
	 
	class MyAdapter extends BaseAdapter{
		Context context;
		ArrayList<Faces> facelist=new ArrayList<Faces> ();
       public MyAdapter(ArrayList<Faces> list,Context context){
    	   this.context=context;
    	   this.facelist=list;
       }
		@Override
		public int getCount() {
			if(facelist!=null){
			return facelist.size();}
			else{
				return 0;
			}
		}

		@Override
		public Object getItem(int position) {
			return facelist.get(position);
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
			final Holder holder=new Holder(); ;
			if (convertView == null) {
				holder.imageView = new ImageView(context);
				holder.imageView.setLayoutParams(new ViewGroup.LayoutParams( 
				         (int) getRawSize(TypedValue.COMPLEX_UNIT_DIP, 115), 
				        (int) getRawSize(TypedValue.COMPLEX_UNIT_DIP, 115))); 

				holder.imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
				if(facelist.get(position)!=null){
				File file = new File(facelist.get(position).origpath) ;
				Log.i("position",String.valueOf(position));
				Glide
			    .with(context)
			    .load(file).placeholder(R.drawable.album_default_loading_pic).//加载中显示的图片
		        error(R.drawable.album_default_loading_pic)//
			    .into(holder.imageView);
				}
			}else {
				holder.imageView = (ImageView) convertView;
			}
				
			return holder.imageView;
		}
		public float getRawSize(int unit, float value) { 
	        Resources res = context.getResources(); 
	        return TypedValue.applyDimension(unit, value, res.getDisplayMetrics()); 
	    } 
		
	}
	class Holder{
		ImageView imageView;
	}
	
}
