package com.example.face;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import javax.net.ssl.SSLException;

import org.json.JSONObject;



import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

public class IDTask extends AsyncTask<String, JSONObject, JSONObject>{
	String info = "";
	GetInfo getinfo;
	
	private final static int CONNECT_TIME_OUT = 30000;
    private final static int READ_OUT_TIME = 50000;
    private static String boundaryString = getBoundary();
    File file;
    HashMap<String, String> map = new HashMap<String, String>();
    HashMap<String, byte[]> byteMap = new HashMap<>();
    public IDTask(){
    	
//    	ByteArrayOutputStream baos = new ByteArrayOutputStream();
//		
//		//可根据流量及网络状况对图片进行压缩
//		mImage.compress(Bitmap.CompressFormat.JPEG, 80, baos);
//		mImageData = baos.toByteArray();
    	file = new File("/storage/emulated/0/corp/idinfo/ifd.jpg");
		byte[] buff = getBytesFromFile(file);
		
		map.put("api_key", "aTfOiZgkooseszMr8Azg1aXsRk-OvuVc");
        map.put("api_secret", "SIlXiw--Xmj_fGz7vklIlH9tmJAeoy6K");
        byteMap.put("image_file",buff);
    }
    public byte[] getBytesFromFile(File f) {
        if (f == null) {
            return null;
        }
        try {
            FileInputStream stream = new FileInputStream(f);
            ByteArrayOutputStream out = new ByteArrayOutputStream(1000);
            byte[] b = new byte[1000];
            int n;
            while ((n = stream.read(b)) != -1)
                out.write(b, 0, n);
            stream.close();
            out.close();
            return out.toByteArray();
        } catch (IOException e) {
        }
        return null;
    }
	private static String getBoundary() {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for(int i = 0; i < 32; ++i) {
            sb.append("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789_-".charAt(random.nextInt("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789_".length())));
        }
        return sb.toString();
    }
	protected JSONObject doInBackground(String... params) {
		// TODO Auto-generated method stub
		String url = params[0];
		JSONObject rjson = null;
			info = "HttpPost返回结果: ";
			//reps = doPost(url);
			try {
				Log.i("message","lala");
				  rjson=post(url, map, byteMap);
				// Log.d("json","rjson="+rjson);
		        //    str = new String(bacd);
//		            Log.i("message",str);
//		            System.out.println(str);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return rjson;
	}
	public void setGetInfo(GetInfo getinfo) {
		this.getinfo = getinfo;
	}

	public interface GetInfo{
		public void getInfo(JSONObject rjson);
	}
	protected void onPostExecute(JSONObject result) {
		//Log.i("message","finish()");
		Log.d("json","rjson="+result);
		getinfo.getInfo(result);
}
	
    protected JSONObject post(String url, HashMap<String, String> map, HashMap<String, byte[]> fileMap) throws Exception {
        HttpURLConnection conne;
        URL url1 = new URL(url);
        JSONObject rjson=null;
        conne = (HttpURLConnection) url1.openConnection();
        conne.setDoOutput(true);
        conne.setUseCaches(false);
        conne.setRequestMethod("POST");
        conne.setConnectTimeout(CONNECT_TIME_OUT);
        conne.setReadTimeout(READ_OUT_TIME);
        conne.setRequestProperty("accept", "*/*");
        conne.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundaryString);
        conne.setRequestProperty("connection", "Keep-Alive");
        conne.setRequestProperty("user-agent", "Mozilla/4.0 (compatible;MSIE 6.0;Windows NT 5.1;SV1)");
        DataOutputStream obos = new DataOutputStream(conne.getOutputStream());
        Iterator iter = map.entrySet().iterator();
        while(iter.hasNext()){
            Map.Entry<String, String> entry = (Map.Entry) iter.next();
            String key = entry.getKey();
            String value = entry.getValue();
            obos.writeBytes("--" + boundaryString + "\r\n");
            obos.writeBytes("Content-Disposition: form-data; name=\"" + key
                    + "\"\r\n");
            obos.writeBytes("\r\n");
            obos.writeBytes(value + "\r\n");
        }
        if(fileMap != null && fileMap.size() > 0){
            Iterator fileIter = fileMap.entrySet().iterator();
            while(fileIter.hasNext()){
                Map.Entry<String, byte[]> fileEntry = (Map.Entry<String, byte[]>) fileIter.next();
                obos.writeBytes("--" + boundaryString + "\r\n");
                obos.writeBytes("Content-Disposition: form-data; name=\"" + fileEntry.getKey()
                        + "\"; filename=\"" + encode(" ") + "\"\r\n");
                obos.writeBytes("\r\n");
                obos.write(fileEntry.getValue());
                obos.writeBytes("\r\n");
            }
        }
        obos.writeBytes("--" + boundaryString + "--" + "\r\n");
        obos.writeBytes("\r\n");
        obos.flush();
        obos.close();
        InputStream ins = null;
        int code = conne.getResponseCode();
        try{
            if(code == 200){
                ins = conne.getInputStream();
                BufferedReader br=new BufferedReader(new InputStreamReader(ins));
                String str=null;
                StringBuffer buffer=new StringBuffer();
                while((str=br.readLine())!=null){
                	buffer.append(str);
                }
                //ins.close();
                //br.close();
                 rjson=new JSONObject(buffer.toString());
                Log.d("zxy","rjson="+rjson);
                //Log.i("message","lala");
            }else{
                ins = conne.getErrorStream();
                Log.d("zxy","error");
            }
        }catch (SSLException e){
            e.printStackTrace();
           //return new byte[0];
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buff = new byte[4096];
        int len;
        while((len = ins.read(buff)) != -1){
            baos.write(buff, 0, len);
        }
        byte[] bytes = baos.toByteArray();
        ins.close();
        Log.i("result",String.valueOf(bytes));
        return rjson;
    }
    private String encode(String value) throws Exception{
        return URLEncoder.encode(value, "UTF-8");
    }
	
}