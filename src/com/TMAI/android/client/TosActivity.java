package com.TMAI.android.client;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.TMAI.android.client.prefs.Prefs;
import com.TMAI.android.client.utils.GeneralUtils;

public class TosActivity extends BaseAppActivity{
	private static final String TAG = "TosActivity";

	
/*	//gui objects 
	private Button withEmailButton;
	private EditText emailInput;*/
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tos_screen);

        Prefs.init(this);

        initGUI();
        
        if (!Prefs.isFirstLogin()){
        	//TOS was never accepted
        	startActivity(new Intent(TosActivity.this,MainActivity.class));
    		finish();
        }
    }

	private void initGUI() {
		Button acceptButton = (Button) findViewById(R.id.accept_button);
		Button rejectButton = (Button) findViewById(R.id.reject_button);
		
		acceptButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				//saving tos accept
				Prefs.setFirstLogin(false);
				String email = GeneralUtils.getNativeContactEmail(TosActivity.this);
				Prefs.setContactEmail(email);
				startActivity(new Intent(TosActivity.this,MainActivity.class));
        		finish();
			}
		});
		
		rejectButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				//closing the application
				Log.d(TAG, "Closing "+getString(R.string.app_name));
				android.os.Process.killProcess(android.os.Process.myPid());
			}
		});
		
		/*		Button withoutEmailButton = (Button) findViewById(R.id.without_email_button);
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
		});*/
	}

	/* 	
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
    
   private String getEmail(Context context) {
        AccountManager accountManager = AccountManager.get(context); 
        Account account = getAccount(accountManager);

        if (account == null) {
          return null;
        } else {
          return account.name;
        }
      }
    
    private Account getAccount(AccountManager accountManager) {
        Account[] accounts = accountManager.getAccountsByType("com.google");
        Account account;
        if (accounts.length > 0) {
          account = accounts[0];      
        } else {
          account = null;
        }
        return account;
      }*/
}
