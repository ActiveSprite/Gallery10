package com.example.face;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.UnsupportedEncodingException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



import com.example.memory10.R;
import com.example.sqlite.FaceDao;
import com.example.sqlite.Model;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.FaceRequest;
import com.iflytek.cloud.RequestListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechUtility;




import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory.Options;
import android.graphics.Paint.Style;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;


public class FaceActivity extends Activity implements OnClickListener{
    private ImageButton newbuild;
   // private ImageButton look;
    private ImageButton save;
    private final int REQUEST_PICTURE_CHOOSE = 1;
    private Bitmap mImage = null;
	private byte[] mImageData = null;
	private Toast mToast;
	private ImageView faceview;
	private EditText facename;
	private String mAuthid = null;
	private FaceRequest mFaceRequest;
	private ProgressDialog mProDialog;
	private Storage store;
	Button button;
	Verify mVerify;
	FaceDao facedao;
	RigisterHandler  rigisterHandler;
	int RESULT=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	SpeechUtility.createUtility(this, SpeechConstant.APPID +"=583e502f");
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_face);
        newbuild=(ImageButton)findViewById(R.id.newbuild);
        //look=(ImageButton)findViewById(R.id.look);
      
        mToast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
        faceview=(ImageView)findViewById(R.id.online_img);
        facename=(EditText)findViewById(R.id.facename);
        newbuild.setOnClickListener(this);
//        look.setOnClickListener(this);
       
      
        faceview.setOnClickListener(this);
        mFaceRequest = new FaceRequest(this);
        facedao=new FaceDao(this);
        mProDialog = new ProgressDialog(this);
		mProDialog.setCancelable(true);
		mProDialog.setTitle("���Ժ�");
		store=new Storage(this);
        
        mVerify=new Verify(this);
        rigisterHandler=new RigisterHandler();
		mProDialog.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				// cancel���ȿ�ʱ,ȡ�����ڽ��еĲ���
				if (null != mFaceRequest) {
					mFaceRequest.cancel();
				}
			}
		});
        
    }
    
    
    private void register(JSONObject obj) throws JSONException {
		int ret = obj.getInt("ret");
		if (ret != 0) {
			showTip("ע��ʧ��");
			RESULT=0;
			return;
			
		}
		if ("success".equals(obj.get("rst"))) {
			showTip("ע��ɹ�");
			RESULT=1;
		} else {
			showTip("ע��ʧ��");
			RESULT=0;
		}
		Message message = new Message();   
        message.what = RESULT;
          
        this.rigisterHandler.sendMessage(message);
	}

	private void verify(JSONObject obj) throws JSONException {
		int ret = obj.getInt("ret");
		if (ret != 0) {
			showTip("��֤ʧ��");
			return;
		}
		if ("success".equals(obj.get("rst"))) {
			if (obj.getBoolean("verf")) {
				showTip("ͨ����֤����ӭ������");
			} else {
				showTip("��֤��ͨ��");
			}
		} else {
			showTip("��֤ʧ��");
		}
	}

	private void detect(JSONObject obj) throws JSONException {
		int ret = obj.getInt("ret");
		if (ret != 0) {
			showTip("���ʧ��");
			return;
		}

		if ("success".equals(obj.get("rst"))) {
			JSONArray faceArray = obj.getJSONArray("face");

			Paint paint = new Paint();
			paint.setColor(Color.RED);
			paint.setStrokeWidth(Math.max(mImage.getWidth(), mImage.getHeight()) / 100f);

			Bitmap bitmap = Bitmap.createBitmap(mImage.getWidth(),
					mImage.getHeight(), Config.ARGB_8888);
			Canvas canvas = new Canvas(bitmap);
			canvas.drawBitmap(mImage, new Matrix(), null);
			for (int i = 0; i < faceArray.length(); i++) {
				float x1 = (float) faceArray.getJSONObject(i)
						.getJSONObject("position").getDouble("left");
				float y1 = (float) faceArray.getJSONObject(i)
						.getJSONObject("position").getDouble("top");
				float x2 = (float) faceArray.getJSONObject(i)
						.getJSONObject("position").getDouble("right");
				float y2 = (float) faceArray.getJSONObject(i)
						.getJSONObject("position").getDouble("bottom");
				Toast.makeText(this,String.valueOf(x1)+" "+String.valueOf(x2), Toast.LENGTH_SHORT);
				paint.setStyle(Style.STROKE);
				canvas.drawRect(new Rect((int)(x1), (int)(y1), (int)(x2), (int)(y2)), 
						paint);
			}

			mImage = bitmap;
			((ImageView) findViewById(R.id.online_img)).setImageBitmap(mImage);
		} else {
			showTip("���ʧ��");
		}
	}
    
    
    private RequestListener mRequestListener = new RequestListener() {

		@Override
		public void onEvent(int eventType, Bundle params) {
		}

		@Override
		public void onBufferReceived(byte[] buffer) {
			if (null != mProDialog) {
				mProDialog.dismiss();
			}

			try {
				String result = new String(buffer, "utf-8");
				Log.d("FaceDemo", result);
				
				JSONObject object = new JSONObject(result);
				String type = object.optString("sst");
				if ("reg".equals(type)) {
					register(object);
				} else if ("verify".equals(type)) {
					verify(object);
				} else if ("detect".equals(type)) {
					detect(object);
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
			if (null != mProDialog) {
				mProDialog.dismiss();
			}

			if (error != null) {
				switch (error.getErrorCode()) {
				case ErrorCode.MSP_ERROR_ALREADY_EXIST:
					showTip("authid�Ѿ���ע�ᣬ�����������");
					break;
					
				default:
					showTip(error.getPlainDescription(true));
					break;
				}
			}
		}
	};
    
	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()){
		  case R.id.online_img:Intent intent = new Intent();
			intent.setType("image/*");
			intent.setAction(Intent.ACTION_PICK);
			startActivityForResult(intent, REQUEST_PICTURE_CHOOSE);
		    break;
		  case R.id.newbuild:
			  mAuthid=facename.getText().toString();
			  if (TextUtils.isEmpty(mAuthid)) {
					showTip("authid����Ϊ��");
					return;
				}
				
				if (null != mImageData) {
					mProDialog.setMessage("ע����...");
					mProDialog.show();
					// �����û���ʶ����ʽΪ6-18���ַ�������ĸ�����֡��»�����ɣ����������ֿ�ͷ�����ܰ����ո񣩡�
					// ��������ʱ���ƶ˽�ʹ���û��豸���豸ID����ʶ�ն��û���
					rigisterHandler.setdata(mAuthid);
					mFaceRequest.setParameter(SpeechConstant.AUTH_ID, mAuthid);
					mFaceRequest.setParameter(SpeechConstant.WFR_SST, "reg");
					mFaceRequest.sendRequest(mImageData, mRequestListener);
					
					
				} else {
					showTip("��ѡ��ͼƬ����ע��");
				}
			  break;
//		  case R.id.look:
//			    facedao.findfcecategory();
//				facedao.findmodel();
//			  break;
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
				// ��Щ�Ͱ汾���ͷ��ص�UriģʽΪfile
				fileSrc = data.getData().getPath();
			} else {
				
				String[] proj = {MediaStore.Images.Media.DATA};
				Cursor cursor = getContentResolver().query(data.getData(), proj,
						null, null, null);
				cursor.moveToFirst();
				int idx = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
				fileSrc = cursor.getString(idx);
				cursor.close();
			}
			// ��ת��ͼƬ�ü�ҳ��
			FaceUtil.cropPicture(this,Uri.fromFile(new File(fileSrc)));
		}else if (requestCode == FaceUtil.REQUEST_CROP_IMAGE) {
			// ��ȡ��������
			Bitmap bmp = data.getParcelableExtra("data");
			// ���������ݲ�Ϊnull�����������أ���ֹ�ü�ʱδ����������
			if(null != bmp){
				FaceUtil.saveBitmapToFile(this, bmp);
			}
			// ��ȡͼƬ����·��
			fileSrc = FaceUtil.getImagePath(FaceActivity.this);
			Toast.makeText(this, fileSrc, Toast.LENGTH_SHORT).show();
			// ��ȡͼƬ�Ŀ�͸�
			Options options = new Options();
			options.inJustDecodeBounds = true;
			mImage = BitmapFactory.decodeFile(fileSrc, options);			
			// ѹ��ͼƬ
			options.inSampleSize = Math.max(1, (int) Math.ceil(Math.max(
					(double) options.outWidth / 1024f,
					(double) options.outHeight / 1024f)));
			options.inJustDecodeBounds = false;
			mImage = BitmapFactory.decodeFile(fileSrc, options);
			
			
			// ��mImageBitmapΪ����ͼƬ��Ϣ����������ȡ
			if(null == mImage) {
				showTip("ͼƬ��Ϣ�޷�������ȡ��");
				return;
			}
			
			// �����ֻ����ͼƬ����ת����������ת�Ƕ�
			int degree = FaceUtil.readPictureDegree(fileSrc);
			if (degree != 0) {
				// ��ͼƬ��תΪ���ķ���
				mImage = FaceUtil.rotateImage(degree, mImage);
			}

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			
			//�ɸ�������������״����ͼƬ����ѹ��
			mImage.compress(Bitmap.CompressFormat.JPEG, 80, baos);
			mImageData = baos.toByteArray();

			faceview.setImageBitmap(mImage);
		}
		
	}  
   class RigisterHandler extends Handler{
	   public String mAuthid;
		
		
		 int i;
		 public RigisterHandler(){
			 
		 }
		 public void setdata(String mAuthid){
			
			this.mAuthid=mAuthid;
		 }
		 public void handleMessage(Message msg){
			    super.handleMessage(msg);
			    if(RESULT==1){
			    Model model=new Model();
				model.setdata(mAuthid, "δ����");
				facedao.addmodel(model);
				mVerify.setcategory(mAuthid);
				mVerify.execute();
				}
			 
		 }
	   
	   
   }
	private void showTip(final String str) {
		mToast.setText(str);
		mToast.show();
	}
}
