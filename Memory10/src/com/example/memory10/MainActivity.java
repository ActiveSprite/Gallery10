package com.example.memory10;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;


public class MainActivity extends Activity {

	 private final int SPLASH_DISPLAY_LENGHT = 3000; // —”≥Ÿ¡˘√Î  
	  
	    @Override  
	    protected void onCreate(Bundle savedInstanceState) {  
	        super.onCreate(savedInstanceState);
	        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
	        requestWindowFeature(Window.FEATURE_NO_TITLE);
	        setContentView(R.layout.splash);  
	        
	        new Handler().postDelayed(new Runnable() {  
	            public void run() {  
	                Intent mainIntent = new Intent(MainActivity.this,  
	                        MemoryActivity.class);  
	                MainActivity.this.startActivity(mainIntent);  
	                MainActivity.this.finish();  
	            }  
	  
	        }, SPLASH_DISPLAY_LENGHT);  
	  
	    }  

}
