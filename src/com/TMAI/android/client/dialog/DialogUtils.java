package com.TMAI.android.client.dialog;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.MailTo;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.TMAI.android.client.BaseAppActivity;
import com.TMAI.android.client.MainActivity;
import com.TMAI.android.client.R;
import com.TMAI.android.client.TosActivity;
import com.TMAI.android.client.gui.GuiUtils;
import com.TMAI.android.client.prefs.Prefs;
import com.TMAI.android.client.utils.GeneralUtils;

public class DialogUtils {

	private static final String TAG = "DialogUtils";

	private static Dialog dialog;
	private static Timer timeOut;


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

	public static void createInputDialog(Context context, String title, String message,final TextView view ){
		AlertDialog.Builder alert = new AlertDialog.Builder(context);

		alert.setTitle(title);
		alert.setMessage(message);

		// Set an EditText view to get user input 
		final EditText input = new EditText(context);
		alert.setView(input);

		alert.setPositiveButton(context.getString(R.string.input_ok_button), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				String value = input.getText().toString();
				// return value
				view.setText(value);
			}
		});

		alert.setNegativeButton(context.getString(R.string.input_cancel_button), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				// Canceled.
				view.setText("");
			}
		});

		alert.show();
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
		alert.setMessage(context.getString(R.string.input_change_email_message_text));

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
		AlertDialog.Builder alert = new AlertDialog.Builder(activity);
		BaseAppActivity.appInForeground = false;

		alert.setTitle(activity.getString(R.string.input_another_memo_title_text));
		alert.setMessage(activity.getString(R.string.input_another_memo_msg_text)); 

		alert.setPositiveButton(activity.getString(R.string.input_another_memo_yes_button), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				// return value
				dialog.cancel();
				if(timeOut!=null){
					timeOut.cancel();
				}
				BaseAppActivity.appInForeground = true;
				activity.finish();
				activity.startActivity(new Intent(activity,MainActivity.class));
			}
		});

		alert.setNegativeButton(activity.getString(R.string.input_another_memo_no_button), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				// Canceled.
				//go to background
				if(timeOut!=null){
					timeOut.cancel();
				}
				BaseAppActivity.appInForeground = false;
				activity.moveTaskToBack(true);
			}
		});

		Dialog dialog = alert.create();
		dialog.show();
		dialogTimeOutThread(activity, dialog, 5);
	}
	
	/**
	 * @param dialog - the dialog closed after the timeout
	 * @param periodSec - the period of time to wait before closing the dialog (-1 will disable the timer)
	 */
	private static void dialogTimeOutThread(final Activity activity,final Dialog dialog,final int periodSec){
		if(periodSec == -1){
			return;
		}
		timeOut = new Timer();
		timeOut.schedule(new TimerTask() {
			@Override
			public void run() {
				if (dialog!=null){
					dialog.cancel();
				}
				BaseAppActivity.appInForeground = false;
				activity.moveTaskToBack(true);
			}
		}, periodSec*1000);
	}

}
