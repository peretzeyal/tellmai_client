package com.TMAI.android.client.audio;

import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;

import com.TMAI.android.client.MainActivity;
import com.TMAI.android.client.gui.GuiUtils;
import com.TMAI.android.client.gui.GuiUtils.RecordingButtonsState;
import com.TMAI.android.client.utils.GeneralUtils;

public class AudioRecorder {
	private static final String TAG = "AudioRecorder";


	final MediaRecorder recorder = new MediaRecorder();
	final String path;

	private MainActivity mainActivity;
	private static Timer connectionTimeOut;
	public static int connectionTimeOutPeriod = 45000; //we only allow up to 45 sec recordings


	/**
	 * Creates a new audio recording at the given path (relative to root of SD card).
	 */
	public AudioRecorder(MainActivity mainActivity, String path) {
		this.path = path;//sanitizePath(path);
		this.mainActivity = mainActivity;
	}

/*	private String sanitizePath(String path) {
		if (!path.startsWith("/")) {
			path = "/" + path;
		}
		if (!path.contains(".")) {
			path += ".3gp";
		}
		return Environment.getExternalStorageDirectory().getAbsolutePath() + path;
	}

	public String getAudioFilePath(){
		return path;
	}*/

	/**
	 * Starts a new recording.
	 */
	public void start() throws Exception {
		String state = android.os.Environment.getExternalStorageState();
		if(!state.equals(android.os.Environment.MEDIA_MOUNTED))  {
			throw new Exception("SD Card is not mounted.  It is " + state + ".");
		}

		// make sure the directory we plan to store the recording in exists
		File directory = new File(path).getParentFile();
		if (!directory.exists() && !directory.mkdirs()) {
			throw new Exception("Path to file could not be created.");
		}
		
		//if file exists delete it
		GeneralUtils.deleteFile(path);

		recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
		recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
		recorder.setOutputFile(path);
		recorder.prepare();
		recorder.start();
		
		Log.d(TAG, "startTimer thread");
		Thread thread = new Thread(new Runnable() {

			@Override
			public void run() {
				startTimer();				
			}
		});
		thread.run();
	}

	/**
	 * Stops a recording that has been previously started.
	 */
	public void stop() throws Exception {
		if (recorder != null){
			recorder.stop();
			recorder.release();
		}
		GuiUtils.stopDuraionProgressBarTimer();
		closeTimer();
		Log.d(TAG, "mainActivity end"+mainActivity);
		
		if (mainActivity!=null){
			GuiUtils.changeButtonState(mainActivity, RecordingButtonsState.STOPPED);
		}
	}

	public void closeTimer() {
		if (connectionTimeOut != null) {
			connectionTimeOut.cancel();
			connectionTimeOut.purge();
			connectionTimeOut = null;
		}
		Log.d(TAG, "closeTimer end");
	}

	public void startTimer() {
		Log.d(TAG, "startTimer");
		if (connectionTimeOut == null) {
			connectionTimeOut = new Timer();
			connectionTimeOut.schedule(new TimerTask() {
				@Override
				public void run() {
					Log.d(TAG, "closeTimer run");
					try {
						stop();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}, connectionTimeOutPeriod, connectionTimeOutPeriod);
		}
	}
}
