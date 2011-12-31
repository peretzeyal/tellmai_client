package com.TMAI.android.client;

import com.TMAI.android.client.utils.GeneralUtils;

import android.app.Activity;
import android.os.Bundle;

public class BaseAppActivity extends Activity{

	
	public static boolean appInForeground = false;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		appInForeground =true;
	}
/*	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		appInForeground =false;
	}

	@Override
	protected void onPause() {
		super.onPause();
		appInForeground =false;
	}

	@Override
	protected void onResume() {
		super.onResume();
		appInForeground =true;
	}*/
}
