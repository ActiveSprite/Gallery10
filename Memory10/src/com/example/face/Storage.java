package com.example.face;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import com.example.sqlite.ConstantState;
import com.example.sqlite.FaceBucket;
import com.example.sqlite.FaceDao;
import com.example.sqlite.Faces;






import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory.Options;
import android.graphics.Paint.Style;
import android.media.FaceDetector;
import android.media.FaceDetector.Face;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore.Images.Media;
import android.util.Log;
import android.widget.Toast;

public class Storage extends AsyncTask<Object, Integer, Object>{


	private ProgressDialog progressDialog;
    int r=0;
	private Toast mToast;
	private int facenumber;
	ContentResolver cr;
	Context context;
	int a;
	String s="/storage/emulated/0/tencent/MicroMsg/WeiXin/zly.jpg";
	String y="/storage/emulated/0/Download/Browser/hh.jpeg";
	String t="/storage/emulated/0/Customize/Wallpapers/267918.jpg";
	FaceDetector faceDetector = null;
	FaceDetector.Face[] face;
	final int N_MAX = 4;
	Bitmap srcImg = null;
	Bitmap srcFace = null;
	String mAuthid;
	private FaceDao facedao;
	GetTag gettag;
	public Storage(Context context){
		this.context=context;
		
		 cr =context.getContentResolver();
	        mToast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
	        progressDialog = new ProgressDialog(context);
	        progressDialog.setTitle("提示信息");
	        progressDialog.setMessage("正在保存，请稍后...");
	        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
	        facedao=new FaceDao(context);
	        progressDialog.setCanceledOnTouchOutside(false);        
	}
	
	protected void onPreExecute() {
		              progressDialog.show();
		          }
		  

	
	 void buildfacelist(){
					String columns[] = new String[] { Media._ID, Media.BUCKET_ID,
							Media.PICASA_ID, Media.DATA, Media.DISPLAY_NAME, Media.TITLE,
							Media.SIZE, Media.BUCKET_DISPLAY_NAME,Media.DATE_ADDED};
					Cursor cur = cr.query(Media.EXTERNAL_CONTENT_URI, columns, null, null,
							Media.DATE_MODIFIED+" desc");
					          a=0;
					   int a=cur.getCount();
					if (cur.moveToFirst()) {
						int photoIDIndex = cur.getColumnIndexOrThrow(Media._ID);
						int photoPathIndex = cur.getColumnIndexOrThrow(Media.DATA);
						int photoNameIndex=cur.getColumnIndexOrThrow(Media.DISPLAY_NAME);
						int photodateIndex=cur.getColumnIndexOrThrow(Media.DATE_ADDED);
						int total_length = 0;

						do{
							//try{
							String path = cur.getString(photoPathIndex);
							String id=cur.getString(photoIDIndex);
							long date=cur.getLong(photodateIndex);
							String time=convert(date);
							int value = (int)((total_length/(float)a)*100);
							publishProgress(value);
							total_length+=1;

							 //Log.i("count", String.valueOf(value));
							 Log.i("path",path);
							if(search(path)==false){
							 Initface(path);
							 detectFace(id,path,time);
							}
						//	}
						//	catch(Exception e){
								
						//	}
						}while(cur.moveToNext());
						
						
					}			
					cur.close();
			
		}
	 
	 public String convert(long time){
		 long y=time*1000;
		 String date=new java.text.SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new java.util.Date(y));
		 return date;
		 } 
	 
	 void Initface(String s){
		 try{
		    Options options = new Options();
			options.inJustDecodeBounds = true;
			srcImg = BitmapFactory.decodeFile(s, options);
			options.inSampleSize = Math.max(1, (int) Math.ceil(Math.max(
					(double) options.outWidth /1024f,
					(double) options.outHeight /1024f)));
			options.inJustDecodeBounds = false;
			srcImg = BitmapFactory.decodeFile(s, options);
			
			this.srcFace = srcImg.copy(Config.RGB_565, true);
			int w = srcFace.getWidth();
			int h = srcFace.getHeight();
			faceDetector = new FaceDetector(w, h, N_MAX);
			face = new FaceDetector.Face[N_MAX];}catch(Exception e){
				
			}
			
	 }
	 public boolean checkFace(Rect rect){
			int w = rect.width();
			int h = rect.height();
			int s = w*h;
			
			if(s < 10000){
				//Log.i(tag, "无效人脸，舍弃.");
				return false;
			}
			else{
				//Log.i(tag, "有效人脸，保存.");
				return true;	
			}
		}	
	 public Bitmap detectFace(String id,String origpath,String savedate){
			//		Drawable d = getResources().getDrawable(R.drawable.face_2);
			//		Log.i(tag, "Drawable尺寸 w = " + d.getIntrinsicWidth() + "h = " + d.getIntrinsicHeight());
			//		BitmapDrawable bd = (BitmapDrawable)d;
			//		Bitmap srcFace = bd.getBitmap();

			int nFace = faceDetector.findFaces(srcFace, face);
			//Log.i(tag, "检测到人脸：n = " + nFace);
			for(int i=0; i<nFace; i++){
				Face f  = face[i];
				PointF midPoint = new PointF();
				float dis = f.eyesDistance();
				f.getMidPoint(midPoint);
				int dd = (int)(dis);
				
				Point eyeLeft = new Point((int)(midPoint.x - dis/2), (int)midPoint.y);
				Point eyeRight = new Point((int)(midPoint.x + dis/2), (int)midPoint.y);
				Rect faceRect = new Rect((int)(midPoint.x - dd-20), (int)(midPoint.y - dd), (int)(midPoint.x + dd+20), (int)(midPoint.y + dd+30));
				//Log.i(tag, "左眼坐标 x = " + eyeLeft.x + "y = " + eyeLeft.y);
				if(checkFace(faceRect)){
					

					int w = srcFace.getWidth();
					int h = srcFace.getHeight();
					if(2*dd<w&&(2*dd+10)<h&&(midPoint.x - dd/2-dd/3)>0){
				try{
					String u=id+"face"+String.valueOf(i);
					String dstpath="/storage/emulated/0/corp/"+u+".jpg";
					Log.i("isExist",String.valueOf(presonUtil.fileIsExists(dstpath)));
					if(presonUtil.fileIsExists(dstpath)==false){
					Bitmap dstFace=Bitmap.createBitmap(srcFace, (int)(midPoint.x -dd-10), (int)(midPoint.y - dd),2*dd+20,3*dd);
					
					presonUtil.saveJpeg(dstFace,u);
					}
					Faces face=new Faces();
					face.setdata(u,origpath,dstpath,savedate);
					facedao.addface(face);
					}catch(Exception e){
					  }
					}
				}

			}
			
			//Log.i(tag, "保存完毕");

			//将绘制完成后的faceBitmap返回
			return srcFace;

		}
	
	 boolean search(String way){
		  int t=0;
		  String columns[] = new String[] { Media._ID, Media.BUCKET_ID,
					Media.PICASA_ID, Media.DATA, Media.DISPLAY_NAME, Media.TITLE,
					Media.SIZE, Media.BUCKET_DISPLAY_NAME };
		  String selection=Media.DATA+" like ?";
		  String[] selectionargs={"%/storage/emulated/0/corp/%"};
			Cursor cur = cr.query(Media.EXTERNAL_CONTENT_URI, columns,selection, selectionargs,
					Media.DATE_MODIFIED+" desc");
			if (cur.moveToFirst()) {
				int photoIDIndex = cur.getColumnIndexOrThrow(Media._ID);
				int photoPathIndex = cur.getColumnIndexOrThrow(Media.DATA);
				int photoNameIndex=cur.getColumnIndexOrThrow(Media.DISPLAY_NAME);
				do{
					String path = cur.getString(photoPathIndex);
					
					if(way.equals(path)){
						t++;
					}
					
				}while(cur.moveToNext());
				
			}
			if(t!=0){
				return true;
			}else{
				return false;
			}
			
	  }
	 public void setGetTag(GetTag gettag){
		 this.gettag=gettag;
	 }
	 public interface GetTag{
			public void getTag(int t);
		} 
	@Override
	protected Object doInBackground(Object... arg0) {
		// TODO Auto-generated method stub
		buildfacelist();
		
		return null;
	}
	
	protected void onProgressUpdate(Integer... values) {
		             // TODO Auto-generated method stub
		             //super.onProgressUpdate(values);
		            //Log.i("count",String.valueOf(values[0]));
		             progressDialog.setProgress(values[0]);
		         }


	protected void onPostExecute(Object result) {
		super.onPostExecute(result);
		facedao.findface();
		facedao.findmodel();
		 progressDialog.dismiss();
        r=1;
        //gettag.getTag(r);
        ConstantState.tag=r;
		Toast.makeText(context, "保存成功", Toast.LENGTH_LONG).show();
	}
	
	
	
}
