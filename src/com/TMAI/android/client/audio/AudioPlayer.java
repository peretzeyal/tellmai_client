package com.TMAI.android.client.audio;

import java.io.IOException;

import com.TMAI.android.client.MainActivity;
import com.TMAI.android.client.gui.GuiUtils;
import com.TMAI.android.client.gui.GuiUtils.RecordingButtonsState;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.util.Log;
import android.view.View;

public class AudioPlayer {

	private static final String TAG = "AudioPlayer";
	private MediaPlayer mediaPlayer;

	public void playAudio(String file) {
		playAudio(file, null);
	}
	
	public int playAudio(String file,final MainActivity mainActivity) {
		try {
			mediaPlayer = new MediaPlayer();
			mediaPlayer.reset();
			mediaPlayer.setDataSource(file);
			mediaPlayer.prepare();
			
			mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
				public void onCompletion(MediaPlayer arg0) {
					if (mainActivity != null){
						Log.d(TAG,"stop audio");
						GuiUtils.stopDuraionProgressBarTimer();
						GuiUtils.changeButtonState(mainActivity, RecordingButtonsState.STOPPED);

					}
				}
			});
			mediaPlayer.start();
			int duration = mediaPlayer.getDuration();
			if (duration>0){
				//return value in seconds
				duration= duration/1000;
			}
			return duration;
		} catch (IOException e) {
			Log.e(TAG,"problem playing audio file "+e);
		}
		return 0;
	}
	
	public void stopAudio(){
		if(mediaPlayer != null){
			mediaPlayer.stop();
			mediaPlayer.release();
			mediaPlayer = null;
		}
	}

}
