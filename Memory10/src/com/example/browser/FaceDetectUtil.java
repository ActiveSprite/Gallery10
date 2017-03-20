package com.example.browser;
import java.io.ByteArrayOutputStream;

import org.json.JSONObject;

import com.facepp.error.FaceppParseException;
import com.facepp.http.HttpRequests;
import com.facepp.http.PostParameters;

import android.graphics.Bitmap;
import android.graphics.Matrix;



public class FaceDetectUtil {
	public interface FaceCallBack {
        void success(JSONObject result);

        void error(FaceppParseException exception);
    }

    public static void detect(final Bitmap bitmapDetect,
            final FaceCallBack faceCallBack) {

        new Thread(new Runnable() {

            @Override
            public void run() {

                try {
                    HttpRequests httpRequests = new HttpRequests(
                            Constant.FACE_API_KEY, Constant.FACE_API_SECRET,
                            true, true);

                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    float scale = Math.min(1, Math.min(
                            600f / bitmapDetect.getWidth(),
                            600f / bitmapDetect.getHeight()));
                    Matrix matrix = new Matrix();
                    matrix.postScale(scale, scale);

                    Bitmap imgSmall = Bitmap.createBitmap(bitmapDetect, 0, 0,
                            bitmapDetect.getWidth(), bitmapDetect.getHeight(),
                            matrix, false);                 

                    imgSmall.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    byte[] array = stream.toByteArray();

                    PostParameters params = new PostParameters();
                    params.setImg(array);

                    JSONObject result = httpRequests.detectionDetect(params);
                    if(faceCallBack!=null){
                        faceCallBack.success(result);
                    }
                } catch (FaceppParseException e) {
                    e.printStackTrace();
                    if(faceCallBack!=null){
                        faceCallBack.error(e);
                    }
                }
            }
        }).start();
    }

	

}
