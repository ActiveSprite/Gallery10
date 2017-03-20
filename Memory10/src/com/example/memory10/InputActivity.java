package com.example.memory10;

import com.example.memory10.SearchActivity.MyAdapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.GridView;

public class InputActivity extends Activity implements OnClickListener{
	private EditText mResultText;
protected void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	    setContentView(R.layout.input_activity);
	    mResultText = ((EditText)findViewById(R.id.searchtext));
	}
public void searchpicture(View v){
	Intent intent=new Intent(InputActivity.this,SearchActivity.class);
	if(!mResultText.getText().toString().isEmpty()){
		intent.putExtra("searchtext", mResultText.getText().toString());
		startActivity(intent);}
	 
}
@Override
public void onClick(View arg0) {
	// TODO Auto-generated method stub
	
}
}
