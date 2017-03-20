package com.example.fragment;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.memory10.AlbumItemActivity;
import com.example.memory10.AlbumsAdapter;
import com.example.memory10.JsonParser;
import com.example.memory10.MemoryActivity;
import com.example.memory10.PhotoUpAlbumHelper;
import com.example.memory10.PhotoUpImageBucket;
import com.example.memory10.R;
import com.example.memory10.PhotoUpAlbumHelper.GetAlbumList;
import com.example.memory10.SearchActivity;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;
import com.iflytek.sunflower.FlowerCollector;

import android.content.ContentResolver;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;



public class FragmentFirst extends Fragment implements OnClickListener{
	
	//private ImageButton record;
    private RecognizerDialog mIatDialog;
    private static String TAG = MemoryActivity.class.getSimpleName();
	// ��HashMap�洢��д���
	private HashMap<String, String> mIatResults = new LinkedHashMap<String, String>();
	private Toast mToast;
	private SpeechRecognizer mIat;
	private String mEngineType = SpeechConstant.TYPE_CLOUD;
//	private EditText mResultText;
	//ContentResolver cr;
	private ListView listView;
	private AlbumsAdapter adapter;
	private PhotoUpAlbumHelper photoUpAlbumHelper;
	private List<PhotoUpImageBucket> list;
	//private Button btsearch;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		//�������ǵĲ���
		return inflater.inflate(R.layout.activity_main, container, false);
		
	}
	public void onActivityCreated(Bundle savedInstanceState) {
		  super.onActivityCreated(savedInstanceState);
		 initView();
		 loadData();
		 mIat = SpeechRecognizer.createRecognizer(getActivity(), mInitListener);
	     mIatDialog = new RecognizerDialog(getActivity(), null);
		}
	public void initView(){
   // 	mResultText = ((EditText)getActivity().findViewById(R.id.iat_text));
   // 	record=(ImageButton)getActivity().findViewById(R.id.record);
    	adapter = new AlbumsAdapter(getActivity());
    //	record.setOnClickListener(this);
    	mToast = Toast.makeText(getActivity(), "", Toast.LENGTH_SHORT);
    	listView=(ListView)getActivity().findViewById(R.id.list1);
    	listView.setAdapter(adapter);
    	//btsearch=(Button)getActivity().findViewById(R.id.bt_seaerch);
    	//btsearch.setOnClickListener(this);
    	onItemClick();
    }
	private void onItemClick(){
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(getActivity(),AlbumItemActivity.class);
				intent.putExtra("imagelist", list.get(position));
				startActivity(intent);
			}
		});
	}
	private void loadData(){
		photoUpAlbumHelper = PhotoUpAlbumHelper.getHelper();
		photoUpAlbumHelper.init(getActivity());
		photoUpAlbumHelper.setGetAlbumList(new GetAlbumList() {
			public void getAlbumList(List<PhotoUpImageBucket> list) {
				adapter.setArrayList(list);
				adapter.notifyDataSetChanged();
				FragmentFirst.this.list = list;
			}
		});
		photoUpAlbumHelper.execute(false);
	}
	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()){
//		  case R.id.record:
//			  mResultText.setText(null);// �����ʾ����
//			  mIatResults.clear();
//			  FlowerCollector.onEvent(getActivity(), "record");
//			  setParam();
//			  mIatDialog.setListener(mRecognizerDialogListener);
//			  mIatDialog.show();
//			  showTip("�뿪ʼ˵��");break;
//		  case R.id.bt_seaerch:
//			  Intent intent = new Intent(getActivity(),SearchActivity.class);
//			  if(!mResultText.getText().toString().isEmpty()){
//				intent.putExtra("searchtext", mResultText.getText().toString());
//				startActivity(intent);}
//			  break;
		  default:
				break;  
		}
	}
	
	public void setParam() {
		// ��ղ���
		//mIat.setParameter(SpeechConstant.PARAMS, null);
		mIat.setParameter(SpeechConstant.DOMAIN, "iat");    
		// ������д����
		mIat.setParameter(SpeechConstant.ENGINE_TYPE, mEngineType);
		// ���÷��ؽ����ʽ
		mIat.setParameter(SpeechConstant.RESULT_TYPE, "json");

		
		
			// ��������
		mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
			// ������������
			mIat.setParameter(SpeechConstant.ACCENT, "mandarin");
		

			// ��������ǰ�˵�:������ʱʱ�䣬���û��೤ʱ�䲻˵��������ʱ����
			//mIat.setParameter(SpeechConstant.VAD_BOS, mSharedPreferences.getString("iat_vadbos_preference", "4000"));
			
			// ����������˵�:��˵㾲�����ʱ�䣬���û�ֹͣ˵���೤ʱ���ڼ���Ϊ�������룬 �Զ�ֹͣ¼��
			//mIat.setParameter(SpeechConstant.VAD_EOS, mSharedPreferences.getString("iat_vadeos_preference", "1000"));
			
			// ���ñ�����,����Ϊ"0"���ؽ���ޱ��,����Ϊ"1"���ؽ���б��
			//mIat.setParameter(SpeechConstant.ASR_PTT, mSharedPreferences.getString("iat_punc_preference", "1"));
			
			// ������Ƶ����·����������Ƶ��ʽ֧��pcm��wav������·��Ϊsd����ע��WRITE_EXTERNAL_STORAGEȨ��
			// ע��AUDIO_FORMAT���������Ҫ���°汾������Ч
		mIat.setParameter(SpeechConstant.AUDIO_FORMAT,"wav");
		mIat.setParameter(SpeechConstant.ASR_AUDIO_PATH, Environment.getExternalStorageDirectory()+"/msc/iat.wav");
	}
	private void printResult(RecognizerResult results) {
		String text = JsonParser.parseIatResult(results.getResultString());

		String sn = null;// ��ȡjson����е�sn�ֶ�
		
		try {
			JSONObject resultJson = new JSONObject(results.getResultString());
			sn = resultJson.optString("sn");
		} catch (JSONException e) {
			e.printStackTrace();
		}

		mIatResults.put(sn, text);

		StringBuffer resultBuffer = new StringBuffer();
		for (String key : mIatResults.keySet()) {
			resultBuffer.append(mIatResults.get(key));
		}
	}
	
	private RecognizerDialogListener mRecognizerDialogListener = new RecognizerDialogListener() {
		public void onResult(RecognizerResult results, boolean isLast) {
			printResult(results);
		}

		/**
		 * ʶ��ص�����.
		 */
		public void onError(SpeechError error) {
			showTip(error.getPlainDescription(true));
		}

	};
	private InitListener mInitListener = new InitListener() {

		@Override
		public void onInit(int code) {
			Log.d(TAG, "SpeechRecognizer init() code = " + code);
			if (code != ErrorCode.SUCCESS) {
				showTip("��ʼ��ʧ�ܣ������룺" + code);
			}
		}
	};
	private void showTip(final String str) {
		mToast.setText(str);
		mToast.show();
	}
}