package com.example.face;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;




import com.example.sqlite.FaceDao;
import com.example.sqlite.Faces;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.FaceRequest;
import com.iflytek.cloud.IdentityVerifier;
import com.iflytek.cloud.RequestListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory.Options;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore.Images.Media;
import android.util.Log;
import android.widget.Toast;

public class Verify extends AsyncTask<Object, Integer, Object>{
	ContentResolver cr;
	Context context;
	private FaceRequest mFaceRequest;
	private boolean verify=false;
	private int successcount=0;
	MyHandler myHandler;
	private Toast mToast;
	private ProgressDialog progressDialog;
	Bitmap srcImg = null;
	Bitmap srcFace = null;
	int RESULT=0;
	private FaceDao facedao;
	String category;
	ArrayList<Faces> facelist=new ArrayList<Faces>();
	Verify(Context context){
		this.context=context;
		 cr =context.getContentResolver();
		 mToast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
		 progressDialog = new ProgressDialog(context);
	        progressDialog.setTitle("提示信息");
	        progressDialog.setMessage("正在保存，请稍后...");
	        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
	        facedao=new FaceDao(context);
	        progressDialog.setCanceledOnTouchOutside(false);
	        mFaceRequest = new FaceRequest(context);
	        myHandler = new MyHandler();
	}
	public void setcategory(String category){
		this.category=category;
	}
	private void register(JSONObject obj) throws JSONException {
		int ret = obj.getInt("ret");
		if (ret != 0) {
			showTip("注册失败");
			return;
		}
		if ("success".equals(obj.get("rst"))) {
			showTip("注册成功");
		} else {
			showTip("注册失败");
		}
	}

	private void verify(JSONObject obj) throws JSONException {
		int ret = obj.getInt("ret");
		if (ret != 0) {
			showTip("验证失败");
			verify=false;
			RESULT=0;
			return;
		}
		if ("success".equals(obj.get("rst"))) {
			if (obj.getBoolean("verf")) {
				showTip("通过验证，欢迎回来！");
				verify=true;
				RESULT=1;
				 Log.i("verify",String.valueOf(verify));
			} else {
				showTip("验证不通过");
				RESULT=0;
				verify=false;
			}
		} else {
			showTip("验证失败");
			RESULT=0;
			verify=false;
		}
		Message message = new Message();   
        message.what = RESULT;
          
        this.myHandler.sendMessage(message);
	}

	
	void search(String category){
		    facelist=facedao.findface();
		    if(facelist!=null){
			int a=facelist.size();		
				
				int total_length = 0;
				for(int i=0;i<a;i++){
					String origpath = facelist.get(i).origpath;
					String dstpath=facelist.get(i).dstpath;
					String id=facelist.get(i).faceid;
					 //Log.i("id",id);
					InitBitmap(dstpath);
					int value = (int)((total_length/(float)a)*100);
					publishProgress(value);
					total_length+=1;
					myHandler.setdata(id, origpath,category);
					if(srcFace!=null){
					faceverify(srcFace,category);}
					try{
						Thread.sleep(1000);
					}catch(Exception e){
					
					}
				}
				}
				
				
			

	  }
	public void InitBitmap(String path){
		Options options = new Options();
		options.inJustDecodeBounds = true;
		srcImg = BitmapFactory.decodeFile(path, options);
		options.inSampleSize = Math.max(1, (int) Math.ceil(Math.max(
				(double) options.outWidth /512f,
				(double) options.outHeight /512f)));
		options.inJustDecodeBounds = false;
		srcImg = BitmapFactory.decodeFile(path, options);
		try{
		this.srcFace = srcImg.copy(Config.RGB_565, true);}catch(Exception e){
			 
		}
	}
	 public void faceverify(Bitmap bitmap,String mAuthid){
		 byte[] mImageData = null;
		  ByteArrayOutputStream baos = new ByteArrayOutputStream();
			
			//可根据流量及网络状况对图片进行压缩
		  bitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos);	
		  mImageData = baos.toByteArray();
		  mFaceRequest.setParameter(SpeechConstant.AUTH_ID, mAuthid);
		  mFaceRequest.setParameter(SpeechConstant.WFR_SST, "verify");
		  mFaceRequest.sendRequest(mImageData, mRequestListener);
		 
	 }
	 private RequestListener mRequestListener = new RequestListener() {

			@Override
			public void onEvent(int eventType, Bundle params) {
			}

			@Override
			public void onBufferReceived(byte[] buffer) {
//				if (null != mProDialog) {
//					mProDialog.dismiss();
//				}

				try {
					String result = new String(buffer, "utf-8");
					Log.d("FaceDemo", result);
					
					JSONObject object = new JSONObject(result);
					String type = object.optString("sst");
					if ("reg".equals(type)) {
						register(object);
					} else if ("verify".equals(type)) {
						verify(object);
					} 
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JSONException e) {
					// TODO: handle exception
				}
			}

			@Override
			public void onCompleted(SpeechError error) {
//				if (null != mProDialog) {
//					mProDialog.dismiss();
//				}

				if (error != null) {
					switch (error.getErrorCode()) {
					case ErrorCode.MSP_ERROR_ALREADY_EXIST:
						showTip("authid已经被注册，请更换后再试");
						//Toast.makeText(context, "authid已经被注册，请更换后再试", Toast.LENGTH_SHORT).show();
						break;
						
					default:
						showTip(error.getPlainDescription(true));
						//Toast.makeText(context, error.getPlainDescription(true), Toast.LENGTH_SHORT).show();
						break;
					}
				}
			}
		};
		class MyHandler extends Handler{
			 public String faceid;
			 public String origpath;
			 public String category;
			
			 int i;
			 public MyHandler(){
				 
			 }
			 public void setdata(String faceid,String origpath,String category){
				this.faceid=faceid;
				this.origpath=origpath;
				this.category=category;
				
				//this.i=i;
			 }
			 public void handleMessage(Message msg){
				 super.handleMessage(msg);
				 if(verify==true){
					//Log.i("成功id",origpath);
					facedao.addfacecategory(faceid, origpath,category);
				 }
			 }
		 }
		protected void onPreExecute() {
	        // TODO Auto-generated method stub
	        // super.onPreExecute();
	        progressDialog.show();
	    }
	
	protected Object doInBackground(Object... arg0) {
		// TODO Auto-generated method stub
		search(category);
		return null;
	}
	protected void onProgressUpdate(Integer... values) {
        // TODO Auto-generated method stub
        //super.onProgressUpdate(values);
      // Log.i("count",String.valueOf(values[0]));
        progressDialog.setProgress(values[0]);
    }
	protected void onPostExecute(Object result) {
		super.onPostExecute(result);
		progressDialog.dismiss();
		
		Toast.makeText(context, "保存成功", Toast.LENGTH_LONG).show();
	}
	private void showTip(final String str) {
		mToast.setText(str);
		mToast.show();
	}
}
