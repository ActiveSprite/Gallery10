package com.example.fragment;

import com.example.memory10.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

public class InstructionActivity extends Activity{
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.tips);
	}
}
