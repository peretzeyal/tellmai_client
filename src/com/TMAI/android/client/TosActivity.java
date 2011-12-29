package com.TMAI.android.client;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.TMAI.android.client.prefs.Prefs;

public class TosActivity extends Activity{
	
	//gui objects 
	private Button withEmailButton;
	private EditText emailInput;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tos_screen);

        initGUI();
    }

	private void initGUI() {
		Button withoutEmailButton = (Button) findViewById(R.id.without_email_button);
		withoutEmailButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Prefs.setContactEmail("");
				startActivity(new Intent(TosActivity.this,MainActivity.class));
        		finish();
			}
		});
		
		withEmailButton = (Button) findViewById(R.id.with_email_button);
		withEmailButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				String email = emailInput.getText().toString();
				if(validateEmail(email)){
					Prefs.setContactEmail(email);
					startActivity(new Intent(TosActivity.this,MainActivity.class));
	        		finish();
				}
				else{
				//email is not valid
					
				}
				
			}
		});
		withEmailButton.setEnabled(false);
		
		emailInput = (EditText) findViewById(R.id.email_input_et);
		emailInput.addTextChangedListener(new TextWatcher() {
			
			public void onTextChanged(CharSequence s, int start, int before, int count) {	
			}
			
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {				
			}
			
			public void afterTextChanged(Editable email) {
				String emailString = email.toString();
				withEmailButton.setEnabled(validateEmail(emailString));
			}
		});
	}

	
    public static boolean validateEmail(String email)
    {
        String pattern = "^([A-Za-z0-9_\\-\\.])+\\@([A-Za-z0-9_\\-\\.])+\\.([A-Za-z]{2,})";
        return validateUsingRegEx(pattern, email);
    }

    private static boolean validateUsingRegEx(String patternStr, String value)
    {
        Pattern pattern = Pattern.compile(patternStr);
        Matcher matcher = pattern.matcher(value);
        return matcher.matches();
    }
}
