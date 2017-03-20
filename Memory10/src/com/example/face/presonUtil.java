package com.example.face;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

public class presonUtil {
	static public String initSavePath(){
		File dateDir = Environment.getExternalStorageDirectory();
		String path = dateDir.getAbsolutePath() + "/corp/";
		File folder = new File(path);
		if(!folder.exists()) 		{
			folder.mkdir();
		}
		return path;
	}
	
static  public void saveJpeg(Bitmap bm,String s){
		
		long dataTake = System.currentTimeMillis();
		String jpegName = initSavePath() + s +".jpg";
		

		//File jpegFile = new File(jpegName);
		if(fileIsExists(jpegName)==false){
			Log.i("fileIsExists",String.valueOf(fileIsExists(jpegName)));
		try {
			FileOutputStream fout = new FileOutputStream(jpegName);
			BufferedOutputStream bos = new BufferedOutputStream(fout);

			//Bitmap newBM = bm.createScaledBitmap(bm, 600, 800, false);

			bm.compress(Bitmap.CompressFormat.JPEG, 100, bos);
			bos.flush();
			bos.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block

			e.printStackTrace();
		}
		}
	}

public static boolean fileIsExists(String strFile){
	try {
		File f=new File(strFile);
		if(!f.exists()){
			return false;
		}
		}catch(Exception e){
		return false;
	}
	return true;
	
}


}
