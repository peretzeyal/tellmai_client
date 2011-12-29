package com.TMAI.android.client.gui;

import android.os.Handler;
import android.widget.Button;

import com.TMAI.android.client.BaseMainActivity;
import com.TMAI.android.client.MainActivity;

public class GuiUtils {
	private static final String TAG = "GuiUtils";

	public static Handler handler;

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
						break;
					case NO_RECORDING:
						mainActivity.startButton.setEnabled(false);
						mainActivity.stopButton.setEnabled(false);
						mainActivity.recordButton.setEnabled(true);
						mainActivity.sendButton.setEnabled(false);
						break;
					case RECORDING:
						mainActivity.startButton.setEnabled(false);
						mainActivity.stopButton.setEnabled(true);
						mainActivity.recordButton.setEnabled(false);
						mainActivity.sendButton.setEnabled(false);
						break;
					case PLAYING:
						mainActivity.startButton.setEnabled(false);
						mainActivity.stopButton.setEnabled(true);
						mainActivity.recordButton.setEnabled(false);
						mainActivity.sendButton.setEnabled(false);
						break;
					case STOPPED:
						mainActivity.startButton.setEnabled(true);
						mainActivity.stopButton.setEnabled(false);
						mainActivity.recordButton.setEnabled(true);
						mainActivity.sendButton.setEnabled(true);
						break;
					}
				}
			});
		}
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
