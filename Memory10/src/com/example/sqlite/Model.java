package com.example.sqlite;

import java.io.Serializable;
import java.util.ArrayList;

public class Model implements Serializable{

	public String authid;
	public String displayname;
	//ArrayList<Face> facelist=new ArrayList<Face>();
	public void setdata(String authid,String displayname){
		this.authid=authid;
		this.displayname=displayname;
	}
}
