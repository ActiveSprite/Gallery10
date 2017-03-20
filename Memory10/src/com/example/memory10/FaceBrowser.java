package com.example.memory10;

import com.example.fragment.FaceFragment;
import com.example.fragment.PhotoFragment;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Window;

public class FaceBrowser extends FragmentActivity{
	private Fragment tab01;
	protected void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.browser);
        FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction transaction = fm.beginTransaction();
        if (tab01 == null) {
			tab01 = new FaceFragment();
			/*
			 * 将Fragment添加到活动中，public abstract FragmentTransaction add (int containerViewId, Fragment fragment)
			*containerViewId即为Optional identifier of the container this fragment is to be placed in. If 0, it will not be placed in a container.
			 * */
			transaction.add(R.id.id_content2, tab01);//将微信聊天界面的Fragment添加到Activity中
			transaction.show(tab01);
		}else {
			transaction.show(tab01);
		}
        transaction.commit();
    }
	
}
