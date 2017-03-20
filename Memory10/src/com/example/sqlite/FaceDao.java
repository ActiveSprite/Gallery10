package com.example.sqlite;







import java.util.ArrayList;



import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;



public class FaceDao {
	private DBHelper helper;
	private SQLiteDatabase db;
	public FaceDao(Context context){
		helper=new DBHelper(context);
	}
	public void addface(Faces face){
		try{
		db=helper.getWritableDatabase();
		db.execSQL("insert into facedata(faceid,origpath,dstpath,savedate) values(?,?,?,?)",new
		    Object[]{face.faceid,face.origpath,face.dstpath,face.savedate});}
		catch(SQLException e){
			
		}

	}
	public void addpicture(Picture picture){
		try{
			db=helper.getWritableDatabase();
			db.execSQL("insert into picturetaked(pictureid,origpath,time,place) values(?,?,?,?)",new
			    Object[]{picture.pictureid,picture.origpath,picture.time,picture.place});}
			catch(SQLException e){
				
			}
	}
	public ArrayList<Picture> findbydate(String date){
		
			db=helper.getWritableDatabase();
			Cursor cursor=db.rawQuery("select * from picturetaked where time like?", 
	    			new String[]{"%"+date+"%"});
			Picture picture;
			ArrayList<Picture> picturebucket=new ArrayList<Picture>();
			if(cursor.moveToFirst()){
	    		do{
	    		  picture=new Picture();
	    		  picture.setdata(cursor.getString(cursor.getColumnIndex("pictureid")),
	    				  cursor.getString(cursor.getColumnIndex("origpath")),
	    				  cursor.getString(cursor.getColumnIndex("time")),
	    				  cursor.getString(cursor.getColumnIndex("place"))
	    				  );	    		 
	    		  picturebucket.add(picture);
	    		  Log.i("date",cursor.getString(cursor.getColumnIndex("time")));
//	    		  Log.i("id",cursor.getString(cursor.getColumnIndex("pictureid")));
//	    		  Log.i("id",cursor.getString(cursor.getColumnIndex("origpath")));
	    		}while(cursor.moveToNext());
	    		return picturebucket;
	    	}else{
	        	return null;}
		
	}
	public ArrayList<Picture> findbyplace(String place){

		db=helper.getWritableDatabase();
		Cursor cursor=db.rawQuery("select * from picturetaked where place like?", 
				new String[]{"%"+place+"%"});
		Picture picture;
		ArrayList<Picture> picturebucket=new ArrayList<Picture>();
		if(cursor.moveToFirst()){
    		do{
    		  picture=new Picture();
    		  picture.setdata(cursor.getString(cursor.getColumnIndex("pictureid")),
    				  cursor.getString(cursor.getColumnIndex("origpath")),
    				  cursor.getString(cursor.getColumnIndex("time")),
    				  cursor.getString(cursor.getColumnIndex("place"))
    				  ); 		 
    		  picturebucket.add(picture);
    		}while(cursor.moveToNext());
    		return picturebucket;
    	}else{
        	return null;}
	}
	public ArrayList<Picture> findpicture(){
		ArrayList<Picture> picturebucket=new ArrayList<Picture>();
		db=helper.getWritableDatabase();
		Picture picture;
    	Cursor cursor=db.rawQuery("select * from picturetaked", 
    			null);
    	if(cursor.moveToFirst()){
    		do{
    		 String pictureid=cursor.getString(cursor.getColumnIndex("pictureid"));
    		 String origpath=cursor.getString(cursor.getColumnIndex("origpath"));
    		 String time=cursor.getString(cursor.getColumnIndex("time"));
    		 //String pictureid=cursor.getString(cursor.getColumnIndex("pictureid"));
    		  picture=new Picture();
    		  picture.setdata(pictureid, origpath, time, null);
    		  picturebucket.add(picture);
    		}while(cursor.moveToNext());
    		return picturebucket;
    	}else{
        	return null;}
	}
	public void addfacecategory(String faceid,String origpath,String category){
		try{
			db=helper.getWritableDatabase();
			db.execSQL("insert into facecategory(faceid,origpath,category) values(?,?,?)",new
			    Object[]{faceid,origpath,category});}
			catch(SQLException e){
				
			}
		
		
	}
	public void addmodel(Model model){
		try{
		db=helper.getWritableDatabase();
		db.execSQL("insert into model(authid,displayname)values(?,?)",new
		    Object[]{model.authid,model.displayname});
		}
		catch(SQLException e){
			
		}
	}
	public void updateface(Faces face){
    	db=helper.getWritableDatabase();
    	db.execSQL("update facedata origpath=?,dstpath=?,savedate=? where faceid=?",new
    			Object[]{face.origpath,face.dstpath,face.faceid,face.savedate});
    }
	public void findfcecategory(){
		db=helper.getWritableDatabase();
    	Cursor cursor=db.rawQuery("select * from facecategory", 
    			null);
    	if(cursor.moveToFirst()){
    		do{
    		    Log.i("facepath",cursor.getString(cursor.getColumnIndex("origpath")));
    		    Log.i("facecategory",cursor.getString(cursor.getColumnIndex("category")));
    		}while(cursor.moveToNext());
    		
    	}
	}
	public ArrayList<Faces> getfacecategory(String m){
		ArrayList<Faces> facebucket=new ArrayList<Faces>();
		db=helper.getWritableDatabase();
		Faces face;
    	Cursor cursor=db.rawQuery("select * from facecategory where category=?", 
    			new String[]{m});
    	if(cursor.moveToFirst()){
    		do{
    		  face=new Faces();
    		  face.setfaceid(cursor.getString(cursor.getColumnIndex("faceid")));
    		  face.setorigpath(cursor.getString(cursor.getColumnIndex("origpath")));
    		  Log.i("get",cursor.getString(cursor.getColumnIndex("origpath")));
    		  facebucket.add(face);
    		}while(cursor.moveToNext());
    		return facebucket;
    	}else{
        	return null;}
	}
	public Model quermodel(String faceid){
		db=helper.getWritableDatabase();
		Cursor cursor=db.rawQuery("select * from facecategory where faceid=?", 
    			new String[]{faceid});
		Model model=new Model();
		cursor.moveToFirst();
		Log.i("delete",cursor.getString(cursor.getColumnIndex("category")));
		model.setdata(cursor.getString(cursor.getColumnIndex("category")), null);
		return model;
	}
	public void deletemodel(String authid){
		db=helper.getWritableDatabase();
		String[] s=new String[]{authid};
		db.delete("model", "authid=?", s);
		//db.execSQL("delete from model where authid=?",new Object[]{authid});
		Log.i("delete","删除成功");
        
	}
	public void deletefacecategory(String category){
		db=helper.getWritableDatabase();
		String[] s=new String[]{category};
		//db.execSQL("delete from facecategory where category=?",new Object[]{category});
		db.delete("facecategory", "category=?", s);
	}
	public ArrayList<Faces> findface(){
		ArrayList<Faces> facebucket=new ArrayList<Faces>();
		Faces face;
    	db=helper.getWritableDatabase();
    	Cursor cursor=db.rawQuery("select * from facedata", 
    		null);
		
    	if(cursor.moveToFirst()){
    		do{
    			face=new Faces();
    			face.setdata(cursor.getString(cursor.getColumnIndex("faceid")),cursor.getString(cursor.getColumnIndex("origpath")),
    					cursor.getString(cursor.getColumnIndex("dstpath")),cursor.getString(cursor.getColumnIndex("savedate")));
    			facebucket.add(face);
    		    Log.i("time",cursor.getString(cursor.getColumnIndex("savedate")));
    		}while(cursor.moveToNext());
    		return facebucket;
    	}else{
    	return null;}
    }
	public 	ArrayList<Model> findmodel(){
		ArrayList<Model> modelbucket=new ArrayList<Model>();
		Model model;
    	db=helper.getWritableDatabase();
    	Cursor cursor=db.rawQuery("select * from model",null);
    	
    	if(cursor.moveToFirst()){
    		do{
    			model=new Model();
    			model.setdata(cursor.getString(cursor.getColumnIndex("authid")), cursor.getString(cursor.getColumnIndex("displayname")));
    			modelbucket.add(model);
    			 Log.i("model",cursor.getString(cursor.getColumnIndex("authid")));            
    		}while(cursor.moveToNext());
    		return modelbucket;
    	}else{
    	return null;
    	}
	}
	
}
