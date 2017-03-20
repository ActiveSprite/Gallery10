package com.example.fragment;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import com.bumptech.glide.Glide;
import com.example.face.FaceitemActivity;
import com.example.face.LoadFace;
import com.example.face.LoadFace.GetFaceList;




import com.example.memory10.R;
import com.example.sqlite.ConstantState;
import com.example.sqlite.FaceBucket;
import com.example.sqlite.FaceDao;
import com.example.sqlite.Model;


import android.content.Context;
import android.content.Intent;
import android.opengl.Visibility;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;

import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class FaceFragment extends Fragment implements OnClickListener, OnItemLongClickListener{
	LoadFace loadface;
	private FacebucketAdapter adapter;
	protected List<FaceBucket> list;
	ListView facelist;
	CheckBox checkbox;
	Button quxiao;
	Button sure;
	FaceDao facedao;
	int t=0;
	RelativeLayout select;
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		//引入我们的布局
		
		return inflater.inflate(R.layout.activity_facelist, container, false);
		
	}
	
	public void onActivityCreated(Bundle savedInstanceState) {
		 super.onActivityCreated(savedInstanceState);
		 facelist=(ListView)getActivity().findViewById(R.id.listface);
		 select=(RelativeLayout)getActivity().findViewById(R.id.longdelete);
	     adapter=new FacebucketAdapter(getActivity());
	     facelist.setAdapter(adapter);
	     quxiao=(Button)getActivity().findViewById(R.id.quxiao);
	     quxiao.setOnClickListener(this);
	     sure=(Button)getActivity().findViewById(R.id.sure);
	     sure.setOnClickListener(this);
	     facelist.setOnItemLongClickListener(this);
	     facedao=new FaceDao(getActivity());
	    // checkbox.findViewById(R.id.item_cb);
	     onItemClick();
	   //OnItemLongClick();
		
		}
	
	 public void onStart(){
		  super.onStart();
		  loadData();
	  }
	 private void onItemClick(){
			facelist.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					if(t==0){
					Intent intent = new Intent(getActivity(),FaceitemActivity.class);
					intent.putExtra("imagelist", list.get(position));
					startActivity(intent);}else{
						Holder holder = (Holder) view.getTag();	
						holder.checkBox.toggle();
						ConstantState.isSelectedMap.put(position, holder.checkBox.isChecked());
						//adapter.notifyDataSetChanged();
					}
				}
			});
		}
	    private void loadData(){
			loadface = new LoadFace(getActivity());
			loadface.setGetFaceList(new GetFaceList() {
				public void getFaceList(ArrayList<FaceBucket> list) {
					adapter.setArrayList((ArrayList<FaceBucket>) list);
					adapter.notifyDataSetChanged();
					FaceFragment.this.list = list;
				}
			});
			loadface.execute();
		}
	    void onBackPressed(){
	    	super.getActivity().onBackPressed();  
            
	    }
	class  FacebucketAdapter extends BaseAdapter{
		   private ArrayList<FaceBucket> arrayList=new ArrayList<FaceBucket>();
		   private LayoutInflater layoutInflater;
		   Context context;
		   
	   public void setArrayList(ArrayList<FaceBucket> arrayList){
			   this.arrayList=arrayList;
			   initData();
			   layoutInflater = LayoutInflater.from(context);
		   }
		public FacebucketAdapter(Context context){
			this.context=context;
			
		}
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return arrayList.size();
		}
		
		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return arrayList.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}
      private void initData(){
    	  if(arrayList!=null){
    	  for(int i=0;i<arrayList.size();i++){
    	  ConstantState.isSelectedMap.put(i,false);
    	  ConstantState.isvisibleMap.put(i, CheckBox.INVISIBLE);
    	   }
    	 }
      }
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Holder holder;
			if (convertView == null){
				holder = new Holder();
				convertView = layoutInflater.inflate(R.layout.list_adpter_item, parent, false);
				holder.image = (ImageView) convertView.findViewById(R.id.imageface);
				holder.name = (TextView) convertView.findViewById(R.id.nameface);
				holder.count = (TextView) convertView.findViewById(R.id.countface);
				holder.checkBox = (CheckBox) convertView
						.findViewById(R.id.item_cb);
				
				convertView.setTag(holder);
			}else {
				holder = (Holder) convertView.getTag();
				int f=ConstantState.isvisibleMap.get(position);
				holder.checkBox.setVisibility(f);
				boolean y=ConstantState.isSelectedMap.get(position);
				holder.checkBox.setChecked(y);
				
			}
			if(arrayList.get(position).facelist!=null){
			holder.count.setText(""+arrayList.get(position).facelist.size());
			//holder.name.setText(arrayList.get(position).getBucketName());
			File file = new File(arrayList.get(position).facelist.get(0).origpath) ;
			Glide
		    .with(context)
		    .load(file).placeholder(R.drawable.album_default_loading_pic).//加载中显示的图片
	        error(R.drawable.album_default_loading_pic)//
		    .into(holder.image);
			}
			return convertView;
		}
		   
	   }
	   public   class Holder{
			ImageView image;
			TextView name;
			TextView count;
		     CheckBox checkBox;
		} 
	    
	
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()){
		case R.id.quxiao:
			select.setVisibility(View.GONE);
			t=0;
			for (int i = 0; i < list.size(); i++) {
			       ConstantState.isvisibleMap.put(i,CheckBox.INVISIBLE);
			       ConstantState.isSelectedMap.put(i,false);
			      
			     }
			adapter.notifyDataSetChanged();
			break;
		case R.id.sure:	
			if(list!=null){
			for (int i = 0; i < list.size(); i++) {
				  Boolean value = ConstantState.isSelectedMap.get(i);
			      if(value){
			    	  if(list!=null&&facelist!=null){
			    	  Model model=facedao.quermodel(list.get(i).facelist.get(0).faceid);
			    	  Log.i("wawa",model.authid);
			    	  facedao.findmodel();
			    	  facedao.deletemodel(model.authid);
			    	  facedao.deletefacecategory(model.authid);
			    	  
			    	  list.remove(i);
			    	  ConstantState.isSelectedMap.put(i, false);
			    	  }
			      }
			     }
			adapter.notifyDataSetChanged();
			}
			break;
		}
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		  ListView listView = (ListView) arg0;
		  int id = listView.getId();
	   switch (id) {
		case R.id.listface:
			 select.setVisibility(View.VISIBLE);
			 t=1;
		    for (int i = 0; i < list.size(); i++) {
		       ConstantState.isvisibleMap.put(i,CheckBox.VISIBLE);
		     }
		adapter.notifyDataSetChanged();
		break;
		default:
		break;
		}
		return false;
	}

}
