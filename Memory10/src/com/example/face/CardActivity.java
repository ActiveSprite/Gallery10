package com.example.face;

import java.io.ByteArrayOutputStream;
import java.io.File;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.face.IDTask.GetInfo;
import com.example.memory10.R;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class CardActivity extends  Activity implements OnClickListener{
	
	public static final String FURL = "https://api-cn.faceplusplus.com/cardpp/v1/ocridcard";
	private final int REQUEST_PICTURE_CHOOSE = 1;
	public static final int MAX_IDCARD_SIZE = 1920*1080;
	private Bitmap mImage = null;
	private byte[] mImageData = null;
	private Button btn_photo;
	private Button btn_recog_fornt;
	private String SD_Path, DirPath, OriPath;
	private ImageView iv_img;
	private TextView tv_name, tv_nation, tv_sex, tv_birth; 
	private TextView tv_address, tv_number, tv_office, tv_date;
	private Bitmap picBitmapOri;
	private boolean isImage;
	 private Bitmap bitmapPhoto =null;
	private TextHandler handler=new TextHandler();
	

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{   requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_idcard_photo_recog);
		
		
		
		btn_photo = (Button)this.findViewById(R.id.btnPhoto);   
		btn_photo.setOnClickListener(this);
	
		
		btn_recog_fornt = (Button)this.findViewById(R.id.btn_recog_front);
		btn_recog_fornt.setOnClickListener(this);
	
		
		tv_name = (TextView)this.findViewById(R.id.tv_name);
		tv_nation = (TextView)this.findViewById(R.id.tv_nation);
		tv_sex = (TextView)this.findViewById(R.id.tv_sex);
		tv_birth = (TextView)this.findViewById(R.id.tv_birth);
		tv_address = (TextView)this.findViewById(R.id.tv_address);
		tv_number = (TextView)this.findViewById(R.id.tv_number);
		
		tv_office = (TextView)this.findViewById(R.id.tv_office);
		tv_date = (TextView)this.findViewById(R.id.tv_date);
		
		iv_img = (ImageView)this.findViewById(R.id.iv_pic); 
		iv_img.setClickable(true);
		iv_img.setOnClickListener(this);
		isImage = false;
		
		SD_Path = Environment.getExternalStorageDirectory().getAbsolutePath();
		DirPath = SD_Path + File.separator + "APIX" + File.separator + "IDCardReader";
		OriPath = DirPath + File.separator + "oriphoto.jpg";
		
		File dir = new File(DirPath);
		dir.mkdirs();
		
		
	}
     void loaddata(){
	     
     IDTask idtask=new IDTask();
     idtask.setGetInfo(new GetInfo() {
		
		@Override
		public void getInfo(JSONObject rjson) {
			// TODO Auto-generated method stub
			Message message = new Message();   
	        message.obj = rjson;
	        handler.sendMessage(message);
		}
	});
     idtask.execute(FURL);
     
     }
	class TextHandler extends Handler{
		
		 public void handleMessage(Message msg){
			 super.handleMessage(msg);
			 JSONObject json=(JSONObject) msg.obj;
			
			 try {
				JSONObject js=json.getJSONArray("cards").getJSONObject(0);
				String name=js.getString("name");
				tv_name.setText(name);
				tv_sex.setText(js.getString("gender"));
				tv_address.setText(js.getString("address"));
				tv_birth.setText(js.getString("birthday"));
				tv_number.setText(js.getString("id_card_number"));
				tv_nation.setText(js.getString("race"));
				
				Log.i("name",name);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 
			 
			 
			try{
				JSONObject js=json.getJSONArray("cards").getJSONObject(0);
				tv_office.setText(js.getString("issued_by"));
				tv_date.setText(js.getString("valid_date"));
			}catch(Exception e){
				
			}
		 }
		
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()){
		  case R.id.btnPhoto:Intent intent = new Intent();
			intent.setType("image/*");
			intent.setAction(Intent.ACTION_PICK);
			startActivityForResult(intent, REQUEST_PICTURE_CHOOSE);
		    break;
		  case R.id.btn_recog_front:
			  if(bitmapPhoto!=null){
			  //new IDTask(bitmapPhoto).execute(FURL);
				  loaddata();
				  
				  }
			  else{
				  Toast.makeText(this, "图片信息不能为空", Toast.LENGTH_SHORT).show();
			  }
			  break;
		 
		  default:
			  break;  
		}
	}
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
		if (resultCode != RESULT_OK) {
			return;
		}
		
		String fileSrc = null;
		if (requestCode == REQUEST_PICTURE_CHOOSE) {
			if ("file".equals(data.getData().getScheme())) {
				// 有些低版本机型返回的Uri模式为file
				fileSrc = data.getData().getPath();
			} else {
				
				String[] proj = {MediaStore.Images.Media.DATA};
				Cursor cursor = getContentResolver().query(data.getData(), proj,
						null, null, null);
				cursor.moveToFirst();
				int idx = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
				fileSrc = cursor.getString(idx);
				cursor.close();
				Options options = new Options();
                options.inJustDecodeBounds = true;
                bitmapPhoto = BitmapFactory.decodeFile(fileSrc, options);

                // scale size to read
                options.inSampleSize = Math.max(1, (int) Math.ceil(Math.max(
                        (double) options.outWidth / 1024f,
                        (double) options.outHeight / 1024f)));
                options.inJustDecodeBounds = false;
                bitmapPhoto = BitmapFactory.decodeFile(fileSrc, options);
                CardUtil.saveBitmapToFile(this, bitmapPhoto);
                iv_img.setImageBitmap(bitmapPhoto);
			}
			// 跳转到图片裁剪页面
			//FaceUtil.cropPicture(this,Uri.fromFile(new File(fileSrc)));
		}else if (requestCode == CardUtil.REQUEST_CROP_IMAGE) {
			// 获取返回数据
			Bitmap bmp = data.getParcelableExtra("data");
			// 若返回数据不为null，保存至本地，防止裁剪时未能正常保存
			if(null != bmp){
				CardUtil.saveBitmapToFile(this, bmp);
			}
			// 获取图片保存路径
			fileSrc = CardUtil.getImagePath(CardActivity.this);
			Toast.makeText(this, fileSrc, Toast.LENGTH_SHORT).show();
			// 获取图片的宽和高
			Options options = new Options();
			options.inJustDecodeBounds = true;
			mImage = BitmapFactory.decodeFile(fileSrc, options);			
			// 压缩图片
			options.inSampleSize = Math.max(1, (int) Math.ceil(Math.max(
					(double) options.outWidth / 1024f,
					(double) options.outHeight / 1024f)));
			options.inJustDecodeBounds = false;
			mImage = BitmapFactory.decodeFile(fileSrc, options);
			
			
			// 若mImageBitmap为空则图片信息不能正常获取
			if(null == mImage) {
				Toast.makeText(this, "图片信息不能正常获取", Toast.LENGTH_SHORT).show();
				return;
			}
			
			// 部分手机会对图片做旋转，这里检测旋转角度
			int degree = CardUtil.readPictureDegree(fileSrc);
			if (degree != 0) {
				// 把图片旋转为正的方向
				mImage = CardUtil.rotateImage(degree, mImage);
			}

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			
			//可根据流量及网络状况对图片进行压缩
			mImage.compress(Bitmap.CompressFormat.JPEG, 80, baos);
			mImageData = baos.toByteArray();

			iv_img.setImageBitmap(mImage);
		}
		
	}  
	
	
}
