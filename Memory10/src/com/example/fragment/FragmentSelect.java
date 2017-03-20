package com.example.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import com.example.face.CardActivity;
import com.example.face.FaceActivity;
import com.example.face.Storage;
import com.example.memory10.AgeActivity;
import com.example.memory10.AlbumItemActivity;
import com.example.memory10.FaceBrowser;
import com.example.memory10.InputActivity;
import com.example.memory10.MemoryActivity;
import com.example.memory10.R;
import com.example.sqlite.ConstantState;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.ui.RecognizerDialog;

public class FragmentSelect extends Fragment implements OnClickListener{

	Storage store;
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		//引入我们的布局
		return inflater.inflate(R.layout.select_fragment, container, false);
		 
	}
	public void onActivityCreated(Bundle savedInstanceState) {
		  super.onActivityCreated(savedInstanceState);
		  store=new Storage(getActivity());
		  RelativeLayout ll=(RelativeLayout)getActivity().findViewById(R.id.Layout2);
		  RelativeLayout llT=(RelativeLayout)getActivity().findViewById(R.id.Layout3);
		  RelativeLayout llh=(RelativeLayout)getActivity().findViewById(R.id.Layout5);
		  RelativeLayout age=(RelativeLayout)getActivity().findViewById(R.id.Layout6);
		  RelativeLayout tips=(RelativeLayout)getActivity().findViewById(R.id.Layout4);
		  age.setOnClickListener(this);
		  RelativeLayout card=(RelativeLayout)getActivity().findViewById(R.id.Layout7);
		  age.setOnClickListener(this);
		  tips.setOnClickListener(this);
		  card.setOnClickListener(this);
		  ll.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				ConstantState.tag=0;
				store.execute();
				if(ConstantState.tag==0){
					arg0.setEnabled(false);
					Toast.makeText(getActivity(), "正在保存人脸信息，请稍等", Toast.LENGTH_SHORT).show();
				}else{
					arg0.setEnabled(true);
				}
			}

			
			  
		  });
		  llT.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					
					Intent intent = new Intent(getActivity(),FaceActivity.class);
					
					startActivity(intent);
					
				}

				
				  
			  });
		  llh.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(getActivity(),FaceBrowser.class);
					
					startActivity(intent);
					
				}

				
				  
			  });
		}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.Layout6:
			Intent intent = new Intent(getActivity(),AgeActivity.class);
			startActivity(intent);
			break;
		case R.id.Layout7:
			Intent intent1 = new Intent(getActivity(),CardActivity.class);
			startActivity(intent1);	
			break;
		case R.id.Layout4:
			Intent intent2 = new Intent(getActivity(),InstructionActivity.class);
		startActivity(intent2);	
			break;
			default:break;
		}
	}
	
	
}
