package com.example.memory10;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;


import com.example.face.Storage;
import com.example.face.Storage.GetTag;
import com.example.fragment.FaceFragment;
import com.example.fragment.FragmentBytime;
import com.example.fragment.FragmentFirst;
import com.example.fragment.FragmentInterest;
import com.example.fragment.FragmentSelect;
import com.example.fragment.PhotoFragment;
import com.example.memory10.PhotoUpAlbumHelper.GetAlbumList;
import com.example.sqlite.ConstantState;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;
import com.iflytek.sunflower.FlowerCollector;








import android.app.Activity;



import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore.Images.Media;
import android.provider.MediaStore.Images.Thumbnails;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;



public class MemoryActivity  extends FragmentActivity implements OnClickListener{
	private LinearLayout mTabWeixin;
	private LinearLayout mTabFrd;
	private LinearLayout mTabAddress;
	private LinearLayout mTabSetting;
	//�ײ�4�������ؼ��е�ͼƬ��ť
	private ImageButton mImgWeixin;
	private ImageButton mImgFrd;
	private ImageButton mImgAddress;
	private ImageButton mImgSetting;
	private ImageButton search;
//	private Button bt;
	private Fragment tab01;
	private Fragment tab02;
	private Fragment tab03;
	private Fragment tab04;
	private Fragment tab05;
	
	Storage store;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	SpeechUtility.createUtility(this, SpeechConstant.APPID +"=583e502f");
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_fragment);
        initView();//��ʼ�����е�view
		initEvents();
		setSelect(0);
		//Loaddata();
    }
    private void initEvents() {
		mTabWeixin.setOnClickListener(this);
		mTabFrd.setOnClickListener(this);
		mTabAddress.setOnClickListener(this);
		search.setOnClickListener(this);
		//mTabSetting.setOnClickListener(this);
	//	bt.setOnClickListener(this);
	}

	private void initView() {
		mTabWeixin = (LinearLayout)findViewById(R.id.id_tab_weixin);
		mTabFrd = (LinearLayout)findViewById(R.id.id_tab_frd);
		mTabAddress = (LinearLayout)findViewById(R.id.id_tab_address);
//		mTabSetting = (LinearLayout)findViewById(R.id.id_tab_setting);
		mImgWeixin = (ImageButton)findViewById(R.id.id_tab_weixin_img);
		mImgFrd = (ImageButton)findViewById(R.id.id_tab_frd_img);
		mImgAddress = (ImageButton)findViewById(R.id.id_tab_address_img);
		search = (ImageButton)findViewById(R.id.searchbutton);
//		mImgSetting = (ImageButton)findViewById(R.id.id_tab_setting_img);
//		bt=(Button)findViewById(R.id.buttontime);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View v) {
		resetImg();
		switch (v.getId()) {
		case R.id.id_tab_weixin://�����΢�Ű�ťʱ���л�ͼƬΪ��ɫ���л�fragmentΪ΢���������
			setSelect(0);
			break;
		case R.id.id_tab_frd:
			setSelect(1);
			break;
		case R.id.id_tab_address:
			setSelect(3);
			break;
			
		case R.id.searchbutton:
			//setSelect(3);
			Intent intent=new Intent(MemoryActivity.this,InputActivity.class);
			
				startActivity(intent);
			break;
//		case R.id.buttontime:
//			setSelect(4);
//			break;
		default:
			
			break;
		}
		
	}

	/*
	 * ��ͼƬ����Ϊ��ɫ�ģ��л���ʾ���ݵ�fragment
	 * */
	private void setSelect(int i) {
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction transaction = fm.beginTransaction();//����һ������
		hideFragment(transaction);//�����Ȱ����е�Fragment�����ˣ�Ȼ�������ٿ�ʼ�������Ҫ��ʾ��Fragment
		switch (i) {
		case 0:
			if (tab01 == null) {
				tab01 = new PhotoFragment();
				/*
				 * ��Fragment��ӵ���У�public abstract FragmentTransaction add (int containerViewId, Fragment fragment)
				*containerViewId��ΪOptional identifier of the container this fragment is to be placed in. If 0, it will not be placed in a container.
				 * */
				transaction.add(R.id.id_content, tab01);//��΢����������Fragment��ӵ�Activity��
			}else {
				transaction.show(tab01);
			}
			mImgWeixin.setImageResource(R.drawable.camera_pressed);
			break;
		case 1:
			if (tab02 == null) {
				tab02 = new FragmentFirst();
				/*
				 * ��Fragment��ӵ���У�public abstract FragmentTransaction add (int containerViewId, Fragment fragment)
				*containerViewId��ΪOptional identifier of the container this fragment is to be placed in. If 0, it will not be placed in a container.
				 * */
				transaction.add(R.id.id_content, tab02);//��΢����������Fragment��ӵ�Activity��
			}else {
				transaction.show(tab02);
			}
			mImgFrd.setImageResource(R.drawable.picture);
			break;
		case 2:
			if (tab03 == null) {
				tab03 = new FragmentInterest();
				/*
				 * ��Fragment��ӵ���У�public abstract FragmentTransaction add (int containerViewId, Fragment fragment)
				*containerViewId��ΪOptional identifier of the container this fragment is to be placed in. If 0, it will not be placed in a container.
				 * */
				transaction.add(R.id.id_content, tab03);//��΢����������Fragment��ӵ�Activity��
			}else {
				transaction.show(tab03);
			}
			mImgAddress.setImageResource(R.drawable.more);
			break;
		case 3:
			if (tab04 == null) {
				tab04 = new FragmentSelect();
				/*
				 * ��Fragment��ӵ���У�public abstract FragmentTransaction add (int containerViewId, Fragment fragment)
				*containerViewId��ΪOptional identifier of the container this fragment is to be placed in. If 0, it will not be placed in a container.
				 * */
				transaction.add(R.id.id_content, tab04);//��΢����������Fragment��ӵ�Activity��
			}else {
				transaction.show(tab04);
			}
			mImgAddress.setImageResource(R.drawable.more);
			break;
			
//		case 4:
//			if (tab05 == null) {
//				tab05 = new FragmentBytime();
//				/*
//				 * ��Fragment��ӵ���У�public abstract FragmentTransaction add (int containerViewId, Fragment fragment)
//				*containerViewId��ΪOptional identifier of the container this fragment is to be placed in. If 0, it will not be placed in a container.
//				 * */
//				transaction.add(R.id.id_content, tab05);//��΢����������Fragment��ӵ�Activity��
//			}else {
//				transaction.show(tab05);
//			}
//			//mImgSetting.setImageResource(R.drawable.tab_settings_pressed);
//			break;
		default:
			break;
		}
		transaction.commit();//�ύ����
	}

	/*
	 * �������е�Fragment
	 * */
	private void hideFragment(FragmentTransaction transaction) {
		if (tab01 != null) {
			transaction.hide(tab01);
		}
		if (tab02 != null) {
			transaction.hide(tab02);
		}
		if (tab03 != null) {
			transaction.hide(tab03);
		}
		if (tab04 != null) {
			transaction.hide(tab04);
		}
		if(tab05!=null){
			transaction.hide(tab05);
		}
	}

	private void resetImg() {
		mImgWeixin.setImageResource(R.drawable.camera);
		mImgFrd.setImageResource(R.drawable.pitcure_pressed);
		mImgAddress.setImageResource(R.drawable.more_pressed);
		//mImgSetting.setImageResource(R.drawable.tab_settings_normal);
	}
}
