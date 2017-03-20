package com.example.face;

import java.util.ArrayList;
import java.util.List;



import com.example.sqlite.FaceBucket;
import com.example.sqlite.FaceDao;
import com.example.sqlite.Faces;
import com.example.sqlite.Model;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class LoadFace extends AsyncTask<Object, Integer, Object>{

	Context context;
	FaceDao facedao;
	ArrayList<Model> modellist;
	ArrayList<FaceBucket> FaceBucketllist;
	ArrayList<Faces> Facelist;
	GetFaceList getFacelist;
	public LoadFace(Context context){
		this.context=context;
		facedao=new FaceDao(context);
		modellist=new ArrayList<Model>();
		FaceBucketllist=new ArrayList<FaceBucket>();
	}
	public void readface(){
		modellist=facedao.findmodel();
		if(modellist!=null){
			for(int i=0;i<modellist.size();i++){
				Facelist=facedao.getfacecategory(modellist.get(i).authid);
				//Log.i("authid",modellist.get(i).authid);
				FaceBucket facebucket=new FaceBucket();
				facebucket.setfacelist(Facelist);
				FaceBucketllist.add(facebucket);
			}
			test();
	}
		
	}
	public void test(){
		for(int i=0;i<FaceBucketllist.size();i++){
			//Log.i("authid",FaceBucketllist.get(i).facelist.get(0).origpath);
		}
	}
	public void setGetFaceList(GetFaceList getFaceList2) {
		this.getFacelist = getFaceList2;
	}

	public interface GetFaceList{
		public void getFaceList(ArrayList<FaceBucket> list);
	}
		
	
	@Override
	protected Object doInBackground(Object... arg0) {
		// TODO Auto-generated method stub
		readface();
		ArrayList<FaceBucket> uselist=new ArrayList<FaceBucket>();
		uselist=FaceBucketllist;
		return uselist;
	}
	
	protected void onPostExecute(Object result) {
		super.onPostExecute(result);
		getFacelist.getFaceList((ArrayList<FaceBucket>)result);
	}

}
