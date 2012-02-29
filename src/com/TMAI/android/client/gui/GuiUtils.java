package com.TMAI.android.client.gui;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.TMAI.android.client.BaseMainActivity;
import com.TMAI.android.client.R;

public class GuiUtils {
	private static final String TAG = "GuiUtils";

	//private static Timer connectionTimer;
	public static Handler handler;
	public static ProgressThread progThread;

	public enum RecordingButtonsState{
		DISABLED,
		NO_RECORDING,
		RECORDING,
		PLAYING,
		STOPPED,
	}


	public static synchronized void changeButtonState(final BaseMainActivity mainActivity, final RecordingButtonsState recordingButtonsState){
		if (handler != null) {
			handler.post(new Runnable() {
				@Override
				public void run() {
					switch (recordingButtonsState) {
					case DISABLED:
						mainActivity.startButton.setEnabled(false);
						mainActivity.stopButton.setEnabled(false);
						mainActivity.recordButton.setEnabled(false);
						mainActivity.sendButton.setEnabled(false);
						showPlayButton(mainActivity, true);
						break;
					case NO_RECORDING:
						mainActivity.startButton.setEnabled(false);
						mainActivity.stopButton.setEnabled(false);
						mainActivity.recordButton.setEnabled(true);
						mainActivity.sendButton.setEnabled(false);
						showPlayButton(mainActivity, true);
						break;
					case RECORDING:
						mainActivity.startButton.setEnabled(false);
						mainActivity.stopButton.setEnabled(true);
						mainActivity.recordButton.setEnabled(false);
						mainActivity.sendButton.setEnabled(false);
						showPlayButton(mainActivity, false);
						break;
					case PLAYING:
						mainActivity.startButton.setEnabled(false);
						mainActivity.stopButton.setEnabled(true);
						mainActivity.recordButton.setEnabled(false);
						mainActivity.sendButton.setEnabled(false);
						showPlayButton(mainActivity, false);
						break;
					case STOPPED:
						mainActivity.startButton.setEnabled(true);
						mainActivity.stopButton.setEnabled(false);
						mainActivity.recordButton.setEnabled(true);
						mainActivity.sendButton.setEnabled(true);
						showPlayButton(mainActivity, true);
						break;
					}
					Log.d(TAG, recordingButtonsState.toString());
				}
			});
		}
	}

	private static synchronized void showPlayButton(BaseMainActivity mainActivity, boolean showPlay){
		if(showPlay){
			mainActivity.startButton.setVisibility(View.VISIBLE);
			mainActivity.stopButton.setVisibility(View.GONE);
		}
		else{
			mainActivity.startButton.setVisibility(View.GONE);
			mainActivity.stopButton.setVisibility(View.VISIBLE);
		}
	}


	public static void startDuraionProgressBarTimer(final BaseMainActivity mainActivity, int maxBarValue) {
		stopDuraionProgressBarTimer();
		Handler durationHandler = new Handler() {
			public void handleMessage(Message msg) {
				// Get the current value of the variable total from the message data
				// and update the progress bar.
				int total = msg.getData().getInt("total");
				mainActivity.duraionProgressBar.setProgress(total);

			}
		};
		mainActivity.duraionProgressBar.setProgress(0);
		mainActivity.duraionProgressBar.setMax(maxBarValue);
		progThread = new ProgressThread(durationHandler);
		progThread.start();

	}




	public static void stopDuraionProgressBarTimer() {
		if (progThread!=null){
			progThread.setStop();
		}
	}

	/**
	 * @param context
	 * @return date and time at the format of dd-MM-yy_kk-mm-ss
	 */
	public static String getDateAndTime(Context context){
		//String text = DateFormat.format("yyyy-MM-dd hh:mm:ss", new java.util.Date()).toString();
		String text = DateFormat.format("dd-MM-yy_kk-mm-ss", new java.util.Date()).toString();
		return text;
	}

	public static String getSelectedButtonText(Button button){
		return getSelectedButtonText(button,"");
	}

	public static String getSelectedButtonText(Button button, String postfix){
		if(button.isSelected()){
			return button.getText().toString()+postfix;
		}
		return "";
	}

}
