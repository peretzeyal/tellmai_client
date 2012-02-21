package com.TMAI.android.client.dialog;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.TMAI.android.client.BaseAppActivity;
import com.TMAI.android.client.BaseMainActivity;
import com.TMAI.android.client.MainActivity;
import com.TMAI.android.client.R;
import com.TMAI.android.client.connection.InfoConnection;
import com.TMAI.android.client.data.EntityValidationResult;
import com.TMAI.android.client.gui.StyledText;
import com.TMAI.android.client.prefs.Prefs;
import com.TMAI.android.client.utils.GeneralUtils;

public class DialogUtils {

	private static final String TAG = "DialogUtils";

	//private static Dialog dialog;
	private static Timer timeOut;
	private static int dialogCounter;
	private static BaseMainActivity baseMainActivity;


	public static void createToast(Context context, String msg){
		try
		{
			Toast toast = Toast.makeText(context, msg, 0);
			toast.setGravity(17, 0, 0);
			toast.show();
		}
		catch(Exception e)        {
			Log.w("GUIUtils", "can't create toast", e);
		}
	}

	public static void createInputDialog(BaseMainActivity baseMainActivityObject, String text){
		baseMainActivity = baseMainActivityObject;
		AlertDialog.Builder alert = new AlertDialog.Builder(baseMainActivity);
		String title = baseMainActivity.getString(R.string.input_titel_text);
		String message = baseMainActivity.getString(R.string.input_message_text);
		
		alert.setTitle(title);
		alert.setMessage(message);

		// Set an EditText view to get user input 
		final EditText input = new EditText(baseMainActivity);
		if (text!=null){
			input.setText(text);
		}
		alert.setView(input);

		alert.setPositiveButton(baseMainActivity.getString(R.string.input_ok_button), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				String entityID = input.getText().toString();
				new entityValidationTask().execute(entityID);
				// return value
/*				EntityValidationResult entityValidationResult = InfoConnection.entityValidation(entityID);
				switch (entityValidationResult.getEntityValidationResultType()) {
				case ENTITY_EXISTS:
					//entity id exists
					mainActivity.projectIDTV.setText(entityID);
					mainActivity.projectNameTV.setText(entityValidationResult.getEntityName());
					break;
				case ENTITY_DOES_NOT_EXISTS:
					//entity dosn't exists
					createToast(context, context.getString(R.string.input_project_id_error));
					//recall the dialog with the current invalid email
					createInputDialog(context, title, message, entityID, mainActivity);
					break;
				case CONNECTION_PROBLEM:
					//problem connecting to the server
					mainActivity.projectIDTV.setText(entityID);
					mainActivity.projectNameTV.setText("");
					break;
				}*/
			}
		});

		alert.setNegativeButton(baseMainActivity.getString(R.string.input_cancel_button), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				// Canceled.
				baseMainActivity.projectIDTV.setText("");
			}
		});

		alert.show();
	}
	
	public static class entityValidationTask extends AsyncTask<String, Void, EntityValidationResult> {
		private ProgressDialog pd;
		private String entityID;
		@Override
		protected void onPostExecute(EntityValidationResult entityValidationResult) {
			super.onPostExecute(entityValidationResult);
			if (pd != null) {
				pd.cancel();
			}
			switch (entityValidationResult.getEntityValidationResultType()) {
			case ENTITY_EXISTS:
				//entity id exists
				baseMainActivity.projectIDTV.setText(entityID);
				baseMainActivity.projectNameTV.setText(entityValidationResult.getEntityName());
				break;
			case ENTITY_DOES_NOT_EXISTS:
				//entity dosn't exists
				DialogUtils.createToast(baseMainActivity, baseMainActivity.getString(R.string.input_project_id_error));
				//recall the dialog with the current invalid email
				DialogUtils.createInputDialog(baseMainActivity, entityID);
				break;
			case CONNECTION_PROBLEM:
				//problem connecting to the server
				baseMainActivity.projectIDTV.setText(entityID);
				baseMainActivity.projectNameTV.setText("");
				break;
			}		
		}
		

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pd = new ProgressDialog(baseMainActivity);
			pd.setMessage(baseMainActivity.getResources().getString(R.string.entity_validation_progress_dialog_title));
			pd.setTitle(baseMainActivity.getResources().getString(R.string.entity_validation_progress_dialog_message));
			pd.setCancelable(false);
			pd.show();
		}

		@Override
		protected EntityValidationResult doInBackground(String... params) {
			entityID = params[0];
			EntityValidationResult entityValidationResult = InfoConnection.entityValidation(entityID);
			return entityValidationResult;
		}
	}	

	public static void createChangeEmailDialog(final Context context){
		createChangeEmailDialog(context,"");
	}

	/**
	 * @param context
	 * @param email - if the email is not empty it will be used in the displayed edit text
	 * 					else the current saved email will be used(if one exists)
	 */
	public static void createChangeEmailDialog(final Context context,String email){
		AlertDialog.Builder alert = new AlertDialog.Builder(context);

		alert.setTitle(context.getString(R.string.input_change_email_title_text));
		alert.setMessage(null);

		// Set an EditText view to get user input 
		final EditText input = new EditText(context);
		String currentEmail = Prefs.getContactEmail();
		if (!email.equals("")){
			currentEmail=email;
		}
		if (currentEmail!=null){
			input.setText(currentEmail);
		}
		alert.setView(input);

		alert.setPositiveButton(context.getString(R.string.input_change_email_ok_button), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				String email = input.getText().toString();
				// return value
				if(GeneralUtils.validateEmail(email)){
					Prefs.setContactEmail(email);
					createToast(context, context.getString(R.string.input_change_email_changed));
				}
				else{
					//email not valid
					createToast(context, context.getString(R.string.input_change_email_error));
					//recall the dialog with the current invalid email
					createChangeEmailDialog(context, email);
				}
			}
		});

		alert.setNegativeButton(context.getString(R.string.input_change_email_cancel_button), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				// Canceled.
			}
		});

		alert.show();
	}


	public static void createAnotherMemoDialog(final Activity activity){
		try {
			AlertDialog.Builder alert = new AlertDialog.Builder(activity);
			BaseAppActivity.appInForeground = false;

			int timeoutPeriod = 5;
			alert.setTitle(activity.getString(R.string.input_another_memo_title_text));
			String msg = activity.getString(R.string.input_another_memo_msg_text);
			msg = msg.replace("***", String.valueOf(timeoutPeriod));
			//alert.setMessage(msg); 
			TextView textView = new TextView(activity);
			textView.setGravity(Gravity.CENTER);
			//textView.setTextSize(20);
			StyledText styledText = new StyledText();
			styledText.addStyledText(msg,25);
			styledText.addStyledText(String.valueOf(timeoutPeriod), 30,Color.RED);
			styledText.addStyledText(" sec",25);
			textView.setText(styledText.getSpanned());
			alert.setView(textView);
			alert.setPositiveButton(activity.getString(R.string.input_another_memo_yes_button), new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					// return value
					dialog.cancel();
					if(timeOut!=null){
						timeOut.cancel();
					}
					BaseAppActivity.appInForeground = true;
					activity.finish();
					Intent intent = new Intent(activity,MainActivity.class);
					intent.putExtra(MainActivity.SECOND_FEEDBACK, true);
					activity.startActivity(intent);
				}
			});

			/*		alert.setNegativeButton(activity.getString(R.string.input_another_memo_no_button), new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					// Canceled.
					//go to background
					if(timeOut!=null){
						timeOut.cancel();
					}
					BaseAppActivity.appInForeground = false;
					activity.moveTaskToBack(true);
				}
			});*/

			Dialog dialog = alert.create();
			dialog.show();
			dialogTimeOutThread(activity, dialog, timeoutPeriod,textView);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param dialog - the dialog closed after the timeout
	 * @param periodSec - the period of time to wait before closing the dialog (-1 will disable the timer)
	 */
	private static void dialogTimeOutThread(final Activity activity,final Dialog dialog,final int periodSec,final TextView textView){
		if(periodSec == -1){
			return;
		}
		final Handler handler = new Handler();
		dialogCounter = periodSec;
		timeOut = new Timer();
		timeOut.schedule(new TimerTask() {
			@Override
			public void run() {
				dialogCounter--;
				if (dialogCounter<=0){
					if (dialog!=null){
						dialog.cancel();
					}
					if (timeOut!=null){
						timeOut.cancel();
						timeOut=null;
					}
					BaseAppActivity.appInForeground = false;
					activity.moveTaskToBack(true);
				}
				else{
					if (handler!=null){
						handler.post(new Runnable() {
							
							@Override
							public void run() {
								String msg = activity.getString(R.string.input_another_memo_msg_text);
								StyledText styledText = new StyledText();
								styledText.addStyledText(msg,25);
								styledText.addStyledText(String.valueOf(dialogCounter), 30,Color.RED);
								styledText.addStyledText(" sec",25);
								//msg = msg.replace("***", styledText.getSpanned());
								//((AlertDialog)dialog).setMessage(msg);	
								
								
								//textView.setGravity(Gravity.CENTER);
								textView.setText(styledText.getSpanned());
								//dialog.setContentView(textView);
								//dialog.set
								//((AlertDialog)dialog).setView(textView);
								//dialog.show();
							}
						});
						
					}
				}
			}
		}, 1000,1000);
	}

}
