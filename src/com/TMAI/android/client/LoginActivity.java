package com.TMAI.android.client;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.TMAI.android.client.prefs.Prefs;



public class LoginActivity extends Activity {
	private static final String TAG = "LoginActivity";

	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);
        
        Prefs.init(this);
        //TODO remove for production
/*        Location location = new Location("");
        location.setLatitude(32.806148);
        location.setLongitude(35.17042);*/


 /*       for (JSONObject iterable_element : jsonArray) {
        	name
		}*/

        if (!Prefs.isFirstLogin()){
        	if (Prefs.getContactEmail() == null){
        		//start TOS screen
        		startActivity(new Intent(this,TosActivity.class));
        		finish();
        	}
        	else{
        		//start main screen
        		startActivity(new Intent(this,MainActivity.class));
        		finish();
        	}
        }
    	//first login show logine screen

        initGUI();
    }

    //init login gui 
	private void initGUI() {
		Button okButton = (Button) findViewById(R.id.ok_button);
		okButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				setLocationAnswer(true);
			}
		});
		
		Button cancelButton = (Button) findViewById(R.id.cancel_button);
		cancelButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				setLocationAnswer(false);
			}
		});
	}
	
	private void setLocationAnswer(boolean value){
		Prefs.setFirstLogin(false);
		Prefs.setLocationApproved(value);
		startActivity(new Intent(LoginActivity.this,TosActivity.class));
		finish();
	}

}