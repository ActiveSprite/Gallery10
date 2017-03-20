package com.example.memory10;

import java.util.ArrayList;

import com.example.face.LoadFace.GetFaceList;
import com.example.sqlite.FaceBucket;
import com.example.sqlite.FaceDao;
import com.example.sqlite.Picture;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.MediaStore.Images.Media;
import android.util.Log;
import android.widget.Toast;

public class SearchTask extends AsyncTask<Object, Integer, Object>{
	FaceDao facedao;
	ContentResolver cr;
	String time;
	Context context;
	GetPictureList getPicturelist;
	ArrayList<Picture> picturebucket=new ArrayList<Picture>();
	public SearchTask(Context context,String time){
		this.context=context;
		facedao=new FaceDao(context);
		this.time=time;
		cr =context.getContentResolver();
	}
	void search(){
		  int t=0;
		  String columns[] = new String[] { Media._ID, Media.BUCKET_ID,
					Media.PICASA_ID, Media.DATA, Media.DISPLAY_NAME, Media.TITLE,
					Media.SIZE, Media.DATE_ADDED, Media.DESCRIPTION };
		  String selection=Media.DATA+" like ?";
		  String[] selectionargs={"%/storage/emulated/0/DCIM/%"};
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
					String place=cur.getString(photoIDIndex);
					String datetime=convert(time);
					//Log.i("place",cur.getString(cur.getColumnIndexOrThrow(Media.DESCRIPTION)));
					Picture picture=new Picture();
					picture.setdata(id, path, datetime, null);
					facedao.addpicture(picture);
				}while(cur.moveToNext());
				
			}
			
			
	  }
	void settime(String time){
		this.time=time;
	}
	void searchdate(String time){
		picturebucket=facedao.findbydate(time);
	}
	 public String convert(long time){
		 long y=time*1000;
		 String date=new java.text.SimpleDateFormat("yyyy年MM月dd日").format(new java.util.Date(y));
		 return date;
		 } 
	 public void setGetPictureList(GetPictureList getPictureList2) {
			this.getPicturelist= getPictureList2;
		}
	@Override
	protected Object doInBackground(Object... arg0) {
		// TODO Auto-generated method stub
		search();
		searchdate(time);
		return picturebucket;
	}
	public interface GetPictureList{
		public void getPictureList(ArrayList<Picture> list);
	}
	protected void onPostExecute(Object result) {
		super.onPostExecute(result);
		getPicturelist.getPictureList((ArrayList<Picture>)result);
	}
}
