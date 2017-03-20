package com.example.sqlite;

import java.io.Serializable;
import java.util.ArrayList;

public class FaceBucket implements Serializable{
  public ArrayList<Faces> facelist;
	public FaceBucket(){
		facelist=new ArrayList<Faces>();
	}
	public void setfacelist(ArrayList<Faces> facelist){
		this.facelist=facelist;
	}
}
