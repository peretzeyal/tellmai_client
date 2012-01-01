package com.TMAI.android.client.gui;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.format.DateFormat;
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


	public static void changeButtonState(final BaseMainActivity mainActivity, final RecordingButtonsState recordingButtonsState){
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
				}
			});
		}
	}

	private static void showPlayButton(BaseMainActivity mainActivity, boolean showPlay){
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
		Handler durationHandler = new Handler() {
			public void handleMessage(Message msg) {
				// Get the current value of the variable total from the message data
				// and update the progress bar.
				int total = msg.getData().getInt("total");
				mainActivity.duraionProgressBar.setProgress(total);
				/*	            if (total <= 0){
	                dismissDialog(typeBar);
	                progThread.setState(ProgressThread.DONE);
	            }*/
			}
		};
		mainActivity.duraionProgressBar.setProgress(0);
		mainActivity.duraionProgressBar.setMax(maxBarValue);
		progThread = new ProgressThread(durationHandler);
		progThread.start();
		/*		final long connectionTimerPeriodSec =  1000;
		mainActivity.duraionProgressBar.st
		if (connectionTimer == null) {
			connectionTimer = new Timer();
			connectionTimer.schedule(new TimerTask() {
				@Override
				public void run() {
					setDurationBar(mainActivity);
				}
			}, connectionTimerPeriodSec, connectionTimerPeriodSec);
		}*/
	}

	/*	private static void setDurationBar(final BaseMainActivity mainActivity){
		if (handler != null) {
			handler.post(new Runnable() {
				@Override
				public void run() {
					int currentProgressState = mainActivity.duraionProgressBar.getProgress();
					mainActivity.duraionProgressBar.setProgress(currentProgressState+1);
				}
			});
		}
	}*/


	public static void stopDuraionProgressBarTimer() {
		if (progThread!=null){
			progThread.setStop();
		}
		/*		
		    if (connectionTimer != null) {
			connectionTimer.cancel();
			connectionTimer.purge();
			connectionTimer = null;
		}*/
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
