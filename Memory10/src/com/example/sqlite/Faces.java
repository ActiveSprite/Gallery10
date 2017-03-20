package com.example.sqlite;

import java.io.Serializable;

import android.graphics.Bitmap;

public class Faces implements Serializable{
	public String faceid;
	public String origpath;
	Bitmap bitmap;
	public String savedate;
	
	public String dstpath;
	//public String category;
	public void setdata(String faceid,String origpath,String dstpath,String savedate){
		this.faceid=faceid;
		this.origpath=origpath;
		this.dstpath=dstpath;
		this.savedate=savedate;
		//this.category=category;
	}
	public void setpicture(Bitmap bitmap){
		this.bitmap=bitmap;
	}
	public void setfaceid(String faceid){
		this.faceid=faceid;
	}
	public void setorigpath(String origpath){
		this.origpath=origpath;
	}
}
