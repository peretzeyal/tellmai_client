package com.TMAI.android.client.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.MailTo;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.TMAI.android.client.R;
import com.TMAI.android.client.gui.GuiUtils;
import com.TMAI.android.client.prefs.Prefs;
import com.TMAI.android.client.utils.GeneralUtils;

public class DialogUtils {

	private static final String TAG = "DialogUtils";

	private static Dialog dialog;


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

		alert.setPositiveButton(context.getString(R.string.input_ok_button), new DialogInterface.OnClickListener() {
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

		alert.setNegativeButton(context.getString(R.string.input_cancel_button), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				// Canceled.
			}
		});

		alert.show();
	}

	/*	public static void createDialog(Context context,Set<Places> placesArray){
		Dialog dialog = null;
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
		View mainLayout = inflater.inflate(R.layout.places_dialog, null);
		LinearLayout listLayout = (LinearLayout) mainLayout.findViewById(R.id.places_dialog_linearLayout_list);
		listLayout.removeAllViews();
		Builder alertDialog = new AlertDialog.Builder(context);

		for (Places places : placesArray) {
			LinearLayout rowLayout = (LinearLayout) inflater.inflate(R.layout.places_dialog_row, null);
			TextView phoneNumber = (TextView) rowLayout.findViewById(R.id.places_dialog_row_text_tv);
			phoneNumber.setText(places.getName());
			listLayout.addView(rowLayout);
		}


		dialog = alertDialog.create();
		dialog.show();
	}

	public static void createDialog(Context context,List<Places> placesList) {
		Builder alertDialog = new AlertDialog.Builder(context);
		String title = context.getString(R.string.places_dialog_titel_text);
		alertDialog.setTitle(title);

		LayoutInflater mInflater = LayoutInflater.from(context);
        View mainView = mInflater.inflate(R.layout.places_dialog, null);

		LinearLayout numbersLayout = (LinearLayout) mainView.findViewById(R.id.places_dialog_linearLayout_list);
		numbersLayout.removeAllViews();
		LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		for (Places places : placesList) {
				LinearLayout homeLayout = (LinearLayout) vi.inflate(R.layout.places_dialog_row, null);
				TextView rowText = (TextView) homeLayout.findViewById(R.id.places_dialog_row_text_tv);
				rowText.setText(places.getName());
				OnClickListener homeListener = new OnClickListener() {
					@Override
					public void onClick(View v) {
						Log.d(TAG, "view selected"+v);
					}
				};
				homeLayout.setOnClickListener(homeListener);
				numbersLayout.addView(homeLayout);



		}
		alertDialog.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				if (dialog!=null){
					dialog.dismiss();
					dialog.cancel();
				}					}
		});
		//cancel button selected
		Button cancelBtn = (Button) mainView.findViewById(R.id.places_dialog_cancel_button);
		cancelBtn.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				if (dialog!=null){
					dialog.dismiss();
					dialog.cancel();
				}

			}
		});

		TextView t = (TextView) mainView.findViewById(R.id.select_phone_nr_not_found);
		if (count == 0) {
			t.setVisibility(View.VISIBLE);
		}
		else {
			t.setVisibility(View.GONE);

		}
		//only one number return this number
		if (count == 1) {
			returnPhoneNrSelected(activity,contactPhoneDetails.numberList.get(0).number,resultCode);
			return;

		}
		alertDialog.setView(mainView);
		dialog = alertDialog.create();
		alertDialog.show();
	}*/
}
