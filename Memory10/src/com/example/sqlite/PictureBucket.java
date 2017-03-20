package com.example.sqlite;

import java.util.ArrayList;

public class PictureBucket {
  public ArrayList<Picture> picturebucket;
  public String Bucketname;
  public PictureBucket(){
	  picturebucket=new ArrayList<Picture>();
  }
  public void addpicture(Picture picture){
	  picturebucket.add(picture);
  }
}
