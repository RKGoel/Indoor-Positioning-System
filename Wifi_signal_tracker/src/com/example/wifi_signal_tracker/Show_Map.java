package com.example.wifi_signal_tracker;

import com.example.wifi_signal_tracker.JavascriptInterface;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;


public class Show_Map extends Activity implements OnClickListener{
	private WebView picView;
	@SuppressLint("JavascriptInterface")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map);
		//JavascriptInterface obj=new JavascriptInterface(getApplicationContext());
		
		//Log.e("map_x", " "+getIntent().getExtras().getDouble("x"));
		//Log.e("map_y", " "+getIntent().getExtras().getDouble("y"));
		//obj.setX(getIntent().getExtras().getDouble("x"));
		//obj.setY(getIntent().getExtras().getDouble("y"));
		
		picView = (WebView)findViewById(R.id.pic_view);
		picView.setBackgroundColor(0);
		Button pickBtn = (Button)findViewById(R.id.pick_btn);
		pickBtn.setOnClickListener(this);
		
		picView.getSettings().setJavaScriptEnabled(true);
		picView.addJavascriptInterface(new JavascriptInterface(getIntent().getExtras().getDouble("x"),getIntent().getExtras().getDouble("y")), "Android");
		picView.getSettings().setBuiltInZoomControls(true);
		picView.getSettings().setUseWideViewPort(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v.getId() == R.id.pick_btn) {
			
			picView.loadUrl("file:///android_asset/mypicture2.jpg");
			picView.loadUrl("file:///android_asset/marker.png");
			picView.loadUrl("file:///android_asset/jquery-1.12.0.min.js");
			Log.e(" pic-view-x", ""+getIntent().getExtras().getDouble("x"));
			picView.loadUrl("javascript:sample('"+getIntent().getExtras().getDouble("x")+"')");
			//picView.loadUrl("javascript:sample('"+getIntent().getExtras().getDouble("y")+"')");
			picView.loadUrl("file:///android_asset/imagepage.html");
		}
		
	}

}
