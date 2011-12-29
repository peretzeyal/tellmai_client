package com.TMAI.android.client.utils;

import java.io.File;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.TMAI.android.client.prefs.Prefs;

public class GeneralUtils {
	private static final String TAG = "GeneralUtils";

	private static final String AUDIO_SUB_FOLDER = "/data/data/TAMI/";

	/**
	 * @return String the audio folder to use for saving and reading audio files
	 *         null = no output folder exists for the application 
	 */
	public static String getAudioFolder(){
		String audioFolder = Prefs.getAduioFolder();
		if (audioFolder == null){
			if (!Environment.MEDIA_MOUNTED.equalsIgnoreCase(Environment.getExternalStorageState())){
				return null;
			}
			audioFolder = Environment.getExternalStorageDirectory().getAbsolutePath();
			audioFolder+= AUDIO_SUB_FOLDER;
			Prefs.setAduioFolder(audioFolder);
		}
		File folder = new File(audioFolder);
		if (!folder.exists()){
			folder.mkdirs();
		}
		return audioFolder;
	}

	public static void deleteAudioFile(String filePath){
		try {
			File file = new File(filePath);
			if (file.exists()){
				file.delete();
			}
		} catch (Exception e) {
			Log.w(TAG, "problem deleting audio file "+e);
		}
	}

	/**
	 * @param context
	 * @return returns the natives devices email 
	 */
	public static String getNativeContactEmail(Context context){
		Account[] accounts = AccountManager.get(context).getAccounts();
		for (Account account : accounts) {
			String possibleEmail = account.name;
			return possibleEmail;
		}
		return "";
	}
}
