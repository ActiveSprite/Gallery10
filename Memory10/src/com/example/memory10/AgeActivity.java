package com.example.memory10;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.BitmapFactory.Options;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore.Images.ImageColumns;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.browser.FaceDetectUtil;
import com.facepp.error.FaceppParseException;

public class AgeActivity extends Activity {

	 final private static String TAG = "FaceDetect";
	    final private int PICTURE_CHOOSE = 1;

	    private ImageView imagePhoto = null;
	    private Bitmap bitmapPhoto =null;
       private ImageButton buttonDetect = null;
	    private TextView textState = null;
	    private View frameWait;


	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        requestWindowFeature(Window.FEATURE_NO_TITLE);
	        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
	                WindowManager.LayoutParams.FLAG_FULLSCREEN);
	        setContentView(R.layout.activity_face_detect);

	        ImageButton btnGetImage = (ImageButton) this.findViewById(R.id.btnGetImage);
	        btnGetImage.setOnClickListener(new MyOnClickListener());

	    //  frameWait = findViewById(R.id.frameWait);

	        textState = (TextView) this.findViewById(R.id.textState);

	        buttonDetect = (ImageButton) this.findViewById(R.id.buttonDetect);
	       //buttonDetect.setVisibility(View.INVISIBLE);
	        buttonDetect.setOnClickListener(new MyOnClickListener());

	        imagePhoto = (ImageView) this.findViewById(R.id.imagePhoto);
	        //imagePhoto.setImageBitmap(bitmapPhoto);
	    }

	    class MyOnClickListener implements View.OnClickListener {
	        @Override
	        public void onClick(View v) {
	            // TODO Auto-generated method stub
	            switch (v.getId()) {
	            case R.id.btnGetImage:
	                // ��ȡͼ��ͼƬ
	                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
	                photoPickerIntent.setType("image/*");
	                startActivityForResult(photoPickerIntent, PICTURE_CHOOSE);
	                break;
	            case R.id.buttonDetect:
	            	if(bitmapPhoto==null){
	            		Toast.makeText(AgeActivity.this, "����ѡ��ͼƬ", Toast.LENGTH_SHORT).show();
	            	}
	            	else{
	                //frameWait.setVisibility(View.VISIBLE);
	                FaceDetectUtil.detect(bitmapPhoto,
	                        new FaceDetectUtil.FaceCallBack() {

	                            @Override
	                            public void success(JSONObject result) {
	                                Message msg = Message.obtain();
	                                msg.what = MSG_SUCCESS;
	                                msg.obj = result;
	                                faceHandler.sendMessage(msg);
	                            }

	                            @Override
	                            public void error(FaceppParseException exception) {
	                                Message msg = Message.obtain();
	                                msg.what = MSG_ERROR;
	                                msg.obj = exception.getErrorMessage();
	                                faceHandler.sendMessage(msg);
	                            }
	                        });}
	                break;

	            default:
	                break;
	            }
	        }
	    }

	    private static final int MSG_SUCCESS = 0x111;
	    private static final int MSG_ERROR = 0x112;
	    private Handler faceHandler = new Handler() {
	        public void handleMessage(android.os.Message msg) {
	            switch (msg.what) {
	            case MSG_SUCCESS:
	                //frameWait.setVisibility(View.GONE);

	                JSONObject rst = (JSONObject) msg.obj;
	                Log.e(TAG, rst.toString());
	                prepareBitmap(rst);
	                imagePhoto.setImageBitmap(bitmapPhoto);
	                break;
	            case MSG_ERROR:
	                //frameWait.setVisibility(View.GONE);
	                String errorMsg = (String) msg.obj;
	                if (TextUtils.isEmpty(errorMsg)) {
	                    textState.setText("Error");
	                } else {
	                    textState.setText(errorMsg);
	                }
	                break;
	            default:
	                break;
	            }
	            super.handleMessage(msg);
	        }
	    };

	    private Bitmap getGendorBitmap(int age, boolean isMale) {
	        // frameWait.setVisibility(View.VISIBLE);
	        TextView textAge = (TextView)findViewById(R.id.textAge);
	        TextView textmale = (TextView)findViewById(R.id.textIsMale);
	   //   textAge.setVisibility(View.VISIBLE);
	        textAge.setText("" + age);
	        if (isMale) {
	        	textmale.setText("��");
	        } else {
	        	textmale.setText("Ů");
	        }
	        textAge.setDrawingCacheEnabled(true);
	        Bitmap bitmap = Bitmap.createBitmap(textAge.getDrawingCache());
	        textAge.destroyDrawingCache();
	        return bitmap;
	    }

	    protected void prepareBitmap(JSONObject rst) {
	        // ����
	        Paint paint = new Paint();
	        paint.setColor(Color.WHITE);
	        // paint.setStrokeWidth(Math.max(img.getWidth(),
	        // img.getHeight()) / 100f);
	        paint.setStrokeWidth(3);

	        // ����
	        Bitmap bitmap = Bitmap.createBitmap(bitmapPhoto.getWidth(),
	                bitmapPhoto.getHeight(), bitmapPhoto.getConfig());
	        Canvas canvas = new Canvas(bitmap);
	        canvas.drawBitmap(bitmapPhoto, new Matrix(), null);
	        try {
	            // find out all faces
	            JSONArray faceArray = rst.getJSONArray("face");
	            final int faceCount = faceArray.length();
	            textState.setText("Finished, " + faceCount + " faces.");
	            for (int i = 0; i < faceCount; ++i) {
	                float x, y, w, h;
	                // ��ȡ��������
	                JSONObject faceObject = faceArray.getJSONObject(i);
	                JSONObject positionObject = faceObject
	                        .getJSONObject("position");
	                x = (float) positionObject.getJSONObject("center").getDouble(
	                        "x");
	                y = (float) faceObject.getJSONObject("position")
	                        .getJSONObject("center").getDouble("y");

	                // ��ȡ������С
	                w = (float) positionObject.getDouble("width");
	                h = (float) positionObject.getDouble("height");

	                // ������������
	                x = x / 100 * bitmapPhoto.getWidth();
	                y = y / 100 * bitmapPhoto.getHeight();
	                w = w / 100 * bitmapPhoto.getWidth();
	                h = h / 100 * bitmapPhoto.getHeight();
	                canvas.drawLine(x - w / 2, y - h / 2, x - w / 2, y + h / 2,
	                        paint);
	                canvas.drawLine(x - w / 2, y - h / 2, x + w / 2, y - h / 2,
	                        paint);
	                canvas.drawLine(x + w / 2, y - h / 2, x + w / 2, y + h / 2,
	                        paint);
	                canvas.drawLine(x - w / 2, y + h / 2, x + w / 2, y + h / 2,
	                        paint);

	                // �Ա������
	                int age = faceObject.getJSONObject("attribute")
	                        .getJSONObject("age").getInt("value");
	                int range = faceObject.getJSONObject("attribute")
	                        .getJSONObject("age").getInt("range");
	                String gendorStr = faceObject.getJSONObject("attribute")
	                        .getJSONObject("gender").getString("value"); // Male-Female
	                Bitmap ageBitmap = getGendorBitmap(age + range,
	                        "Male".equals(gendorStr));

	                int ageWidth = ageBitmap.getWidth();
	                int ageHeight = ageBitmap.getHeight();

	                if (bitmap.getWidth() < imagePhoto.getWidth()
	                        && bitmap.getHeight() < imagePhoto.getHeight()) {
	                    float ratio = Math.max(bitmap.getWidth() * 1.0f
	                            / imagePhoto.getWidth(), bitmap.getHeight() * 1.0f
	                            / imagePhoto.getHeight());
	                    ageBitmap = Bitmap.createScaledBitmap(ageBitmap,
	                            (int) (ageWidth * ratio),
	                            (int) (ageHeight * ratio), false);
	                }

	                canvas.drawBitmap(ageBitmap, x - ageBitmap.getWidth() / 2, y
	                        - h / 2 - ageBitmap.getHeight(), null);
	            }

	            // save new image
	            bitmapPhoto = bitmap;

	        } catch (JSONException e) {
	            e.printStackTrace();
	           AgeActivity.this.runOnUiThread(new Runnable() {
	                public void run() {
	                    textState.setText("Error.");
	                }
	            });
	        }
	    }

	    @Override
	    protected void onActivityResult(int requestCode, int resultCode,
	            Intent intent) {
	        super.onActivityResult(requestCode, resultCode, intent);

	        // the image picker callback
	        if (requestCode == PICTURE_CHOOSE) {
	            if (intent != null) {
	                // Log.d(TAG, "idButSelPic Photopicker: " +
	                // intent.getDataString());
	                Cursor cursor = getContentResolver().query(intent.getData(),
	                        null, null, null, null);
	                cursor.moveToFirst();
	                int idx = cursor.getColumnIndex(ImageColumns.DATA);
	                String fileSrc = cursor.getString(idx);
	                // Log.d(TAG, "Picture:" + fileSrc);

	                // just read size
	                Options options = new Options();
	                options.inJustDecodeBounds = true;
	                bitmapPhoto = BitmapFactory.decodeFile(fileSrc, options);

	                // scale size to read
	                options.inSampleSize = Math.max(1, (int) Math.ceil(Math.max(
	                        (double) options.outWidth / 1024f,
	                        (double) options.outHeight / 1024f)));
	                options.inJustDecodeBounds = false;
	                bitmapPhoto = BitmapFactory.decodeFile(fileSrc, options);
	                //textState.setText("Clik Detect. ==>");

	                imagePhoto.setImageBitmap(bitmapPhoto);
	                buttonDetect.setVisibility(View.VISIBLE);
	            } else {
	                Log.d(TAG, "idButSelPic Photopicker canceled");
	            }
	        }
	    }

	    private class FaceppDetect {
	        DetectCallback callback = null;

	        public void setDetectCallback(DetectCallback detectCallback) {
	            callback = detectCallback;
	        }

	        public void detect(final Bitmap image) {

	            new Thread(new Runnable() {

	                public void run() {
	                    // zj: old position

	                }
	            }).start();
	        }
	    }

	    interface DetectCallback {
	        void detectResult(JSONObject rst);
	    }

}

