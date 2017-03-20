package com.example.fragment;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;


import com.bumptech.glide.Glide;
import com.example.fragment.FaceFragment.FacebucketAdapter;
import com.example.fragment.FaceFragment.Holder;
import com.example.memory10.MainActivity;
import com.example.memory10.MemoryActivity;
import com.example.memory10.R;
import com.example.sqlite.FaceBucket;
import com.example.sqlite.FaceDao;
import com.example.sqlite.Picture;
import com.example.sqlite.PictureBucket;


import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore.Images.Media;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class FragmentBytime extends Fragment implements OnClickListener{
	FaceDao facedao;
	ListView facelist;
	private MAdapter adapter;
	View v;
	ContentResolver cr;
	ArrayList<Picture> picturelist=new ArrayList<Picture>(); 
	ArrayList<PictureBucket> list=new ArrayList<PictureBucket> ();
	HashMap<String, PictureBucket> picturebucketList = new HashMap<String, PictureBucket>();
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		//引入我们的布局
		v=inflater.inflate(R.layout.time_fragment, container, false);
		return v;
		
	}
	public void onActivityCreated(Bundle savedInstanceState) {
		  super.onActivityCreated(savedInstanceState);
		  facedao=new FaceDao(getActivity());
		  facelist=(ListView)getActivity().findViewById(R.id.listtime);
		  cr =getActivity().getContentResolver();
		}
	void search(){
		  int t=0;
		  String columns[] = new String[] { Media._ID, Media.BUCKET_ID,
					Media.PICASA_ID, Media.DATA, Media.DISPLAY_NAME, Media.TITLE,
					Media.SIZE, Media.DATE_ADDED };
		  String selection=Media.DATA+" like ?";
		  String[] selectionargs={"%/storage/emulated/0/DCIM/Camera/%"};
			Cursor cur = cr.query(Media.EXTERNAL_CONTENT_URI, columns,selection, selectionargs,
					Media.DATE_MODIFIED+" desc");
			if (cur.moveToFirst()) {
				int photoIDIndex = cur.getColumnIndexOrThrow(Media._ID);
				int photoPathIndex = cur.getColumnIndexOrThrow(Media.DATA);
				int phototimeIndex=cur.getColumnIndexOrThrow(Media.DATE_ADDED);
				
				do{
					long time = cur.getLong(phototimeIndex);
					String path = cur.getString(photoPathIndex);
					String id=cur.getString(photoIDIndex);
					String datetime=convert(time);
					Log.i("time",convert(time));
//					Log.i("path",cur.getString(photoPathIndex));
					Picture picture=new Picture();
					picture.setdata(id, path, datetime, null);
					facedao.addpicture(picture);
				}while(cur.moveToNext());
				
			}
			
			
	  }
	public String convert(long time){
		 long y=time*1000;
		 String date=new java.text.SimpleDateFormat("yyyy年MM月dd日").format(new java.util.Date(y));
		 return date;
		 } 
	public void onStart(){
		super.onStart();
		new Handler().post(new Runnable() {  
            public void run() {
               search();
              adapter=new MAdapter(getActivity());
       		  list=getImagesBucketList();
       		  adapter.setArrayList(list);
              //adapter.notifyDataSetChanged();
       		  facelist.setAdapter(adapter);
            	 Log.i("lance","hshha");
            }  
  
        }); 
	}
    public void sort(){
	     picturelist=facedao.findpicture();
	     if(picturelist!=null){
	     for(int i=0;i<picturelist.size();i++){
	    	 String pictureid=picturelist.get(i).pictureid;
	    	 String origpath=picturelist.get(i).origpath;
	    	 String time =picturelist.get(i).time;
	    	 String place=picturelist.get(i).place;
	    	 PictureBucket bucket = picturebucketList.get(time);
				if (bucket == null) {
					bucket = new PictureBucket();
					picturebucketList.put(time, bucket);
					bucket.Bucketname = time;
				} 
				Picture pictureItem = new Picture();
				pictureItem.setdata(pictureid, origpath, time, place);
//				imageItem.setThumbnailPath(thumbnailList.get(_id));
				bucket.addpicture(pictureItem);
	     }
	     }
	
	    
	
   }
    
    public ArrayList<PictureBucket> getImagesBucketList() {
	    sort();
		ArrayList<PictureBucket> tmpList = new ArrayList<PictureBucket>();
		Iterator<Entry<String, PictureBucket>> itr = picturebucketList.entrySet().iterator();
		while (itr.hasNext()) {
			Entry<String, PictureBucket> entry = (Map.Entry<String, PictureBucket>) itr.next();
			tmpList.add(entry.getValue());
		}
		MapComparator se=new MapComparator();
		Comparator<PictureBucket> descComparator = Collections.reverseOrder(se);
		Collections.sort(tmpList,descComparator);
		
		
		return tmpList;
	}
    
    class  MAdapter extends BaseAdapter{
		   private ArrayList<PictureBucket> arrayList=new ArrayList<PictureBucket>();
		   private LayoutInflater layoutInflater;
		   Context context;
		   public void setArrayList(ArrayList<PictureBucket> arrayList2){
			   this.arrayList=arrayList2;
			   layoutInflater = LayoutInflater.from(context);
		   }
		public MAdapter(Context context){
			this.context=context;
		}
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return arrayList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return arrayList.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Holder holder;
			if (convertView == null){
				holder = new Holder();
				convertView = layoutInflater.inflate(R.layout.list_adpter_item, parent, false);
				holder.image = (ImageView) convertView.findViewById(R.id.imageface);
				holder.name = (TextView) convertView.findViewById(R.id.nameface);
				holder.count = (TextView) convertView.findViewById(R.id.countface);
				convertView.setTag(holder);
			}else {
				holder = (Holder) convertView.getTag();
			}
			if(arrayList.get(position).picturebucket!=null){
			holder.count.setText(""+arrayList.get(position).picturebucket.size());
			holder.name.setText(arrayList.get(position).Bucketname);
			File file = new File(arrayList.get(position).picturebucket.get(0).origpath) ;
			Glide
		    .with(context)
		    .load(file).placeholder(R.drawable.album_default_loading_pic).//加载中显示的图片
	        error(R.drawable.album_default_loading_pic)//
		    .into(holder.image);
			}
			return convertView;
		}
		   
	   }
	   class Holder{
			ImageView image;
			TextView name;
			TextView count;
		} 
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
	}
	class MapComparator implements Comparator<PictureBucket>{

	    public int compare(PictureBucket lhs, PictureBucket rhs) {
	        return lhs.Bucketname.compareTo(rhs.Bucketname);
	    }

	}
}
