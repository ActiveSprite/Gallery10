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
	// 用HashMap存储听写结果
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
		//引入我们的布局
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
//			  mResultText.setText(null);// 清空显示内容
//			  mIatResults.clear();
//			  FlowerCollector.onEvent(getActivity(), "record");
//			  setParam();
//			  mIatDialog.setListener(mRecognizerDialogListener);
//			  mIatDialog.show();
//			  showTip("请开始说话");break;
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
		// 清空参数
		//mIat.setParameter(SpeechConstant.PARAMS, null);
		mIat.setParameter(SpeechConstant.DOMAIN, "iat");    
		// 设置听写引擎
		mIat.setParameter(SpeechConstant.ENGINE_TYPE, mEngineType);
		// 设置返回结果格式
		mIat.setParameter(SpeechConstant.RESULT_TYPE, "json");

		
		
			// 设置语言
		mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
			// 设置语言区域
			mIat.setParameter(SpeechConstant.ACCENT, "mandarin");
		

			// 设置语音前端点:静音超时时间，即用户多长时间不说话则当做超时处理
			//mIat.setParameter(SpeechConstant.VAD_BOS, mSharedPreferences.getString("iat_vadbos_preference", "4000"));
			
			// 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音
			//mIat.setParameter(SpeechConstant.VAD_EOS, mSharedPreferences.getString("iat_vadeos_preference", "1000"));
			
			// 设置标点符号,设置为"0"返回结果无标点,设置为"1"返回结果有标点
			//mIat.setParameter(SpeechConstant.ASR_PTT, mSharedPreferences.getString("iat_punc_preference", "1"));
			
			// 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
			// 注：AUDIO_FORMAT参数语记需要更新版本才能生效
		mIat.setParameter(SpeechConstant.AUDIO_FORMAT,"wav");
		mIat.setParameter(SpeechConstant.ASR_AUDIO_PATH, Environment.getExternalStorageDirectory()+"/msc/iat.wav");
	}
	private void printResult(RecognizerResult results) {
		String text = JsonParser.parseIatResult(results.getResultString());

		String sn = null;// 读取json结果中的sn字段
		
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
		 * 识别回调错误.
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
				showTip("初始化失败，错误码：" + code);
			}
		}
	};
	private void showTip(final String str) {
		mToast.setText(str);
		mToast.show();
	}
}