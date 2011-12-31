package com.TMAI.android.client.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Environment;
import android.util.Log;

import com.TMAI.android.client.data.MemoInfo;

public class GeneralUtils {
	private static final String TAG = "GeneralUtils";

	private static final String AUDIO_SUB_FOLDER = "/data/data/TAMI/";
	private static final String OLD_AUDIO_SUB_FOLDER = "/data/data/TAMI/files/";

	/**
	 * @return String the audio folder to use for saving and reading audio files
	 *         null = no output folder exists for the application 
	 */
	public static String getAudioFolder(){
		return createFolder(AUDIO_SUB_FOLDER);
	}

	/**
	 * @return String the audio folder to use for saving and reading unsent audio files
	 *         null = no output folder exists for the application 
	 */
	public static String getOldAudioFolder(){
		return createFolder(OLD_AUDIO_SUB_FOLDER);
	}

	private static String createFolder(String folderPath){
		if (!Environment.MEDIA_MOUNTED.equalsIgnoreCase(Environment.getExternalStorageState())){
			return null;
		}
		String audioFolder = Environment.getExternalStorageDirectory().getAbsolutePath();
		audioFolder+= folderPath;
		//create folder if needed
		File folder = new File(audioFolder);
		if (!folder.exists()){
			folder.mkdirs();
		}
		return audioFolder;


	}


	/**
	 * @param origPath - the current folder the file exists in
	 * @param origFileName - the current file name
	 * @param newPath - the new folder for the file
	 * @param newFileName - the new file name
	 * @return - true if successful
	 */
	public static boolean moveFile(String origPath,String origFileName,String newPath,String newFileName){
		try {
			File file = new File(origPath+origFileName);
			if (!file.exists()){
				return false;
			}
			return file.renameTo( new File(newPath+newFileName));
		} catch (Exception e) {
			return false;

		}	
	}

	public static void deleteFile(String filePath){
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
		AccountManager accountManager = AccountManager.get(context);
		Account[] accounts =  accountManager.getAccountsByType("com.google");
		//Account[] accounts = AccountManager.get(context).getAccounts();
		for (Account account : accounts) {
			String possibleEmail = account.name;
			return possibleEmail;
		}
		return "";
	}


	/**
	 * @param email - email string to validate
	 * @return true - the email pattern is correct
	 */
	public static boolean validateEmail(String email){
		String pattern = "^([A-Za-z0-9_\\-\\.])+\\@([A-Za-z0-9_\\-\\.])+\\.([A-Za-z]{2,})";
		return validateUsingRegEx(pattern, email);
	}

	private static boolean validateUsingRegEx(String patternStr, String value){
		Pattern pattern = Pattern.compile(patternStr);
		Matcher matcher = pattern.matcher(value);
		return matcher.matches();
	}

	/**
	 * @param filePath
	 * @return file without the extention
	 */
	public static String removeExtention(String filePath) {
	    // These first few lines the same as Justin's
	    File file = new File(filePath);

	    // if it's a directory, don't remove the extention
	    if (file.isDirectory()) return filePath;

	    String name = file.getName();

	    // Now we know it's a file - don't need to do any special hidden
	    // checking or contains() checking because of:
	    final int lastPeriodPos = name.lastIndexOf('.');
	    if (lastPeriodPos == -1)
	    {
	        // No period after first character - return name as it was passed in
	        return filePath;
	    }
	    else
	    {
	        // Remove the last period and everything after it
	        File renamed = new File(file.getParent(), name.substring(0, lastPeriodPos));
	        return renamed.getPath();
	    }
	}
	

	/**
	 * @param memoInfo - the object to save
	 * @param path -  the location for the saving object
	 * @param objectName - the object file name
	 */
	public static void saveMemoInfo(MemoInfo memoInfo, String path ,String objectName) {
		FileOutputStream f_out = null;
		try {
			File dbFile = new File(path+objectName);
			if (!dbFile.exists()) {
				dbFile.mkdirs();
				dbFile.delete();
			}
			f_out = new FileOutputStream(path+objectName);
		}
		catch (FileNotFoundException e) {
			Log.w(TAG, "file not found :" + e);
			return;
		}
		try {
			ObjectOutputStream obj_out = new ObjectOutputStream(f_out);
			obj_out.writeObject(memoInfo);
		}
		catch (IOException e) {
			Log.w(TAG, "file not saved :" + e);
			return;
		}
	}

	/**

	 * @param fullPath - the object path with the file name
	 * @return - the object for the device null if it dosen't exists
	 */
	public static MemoInfo readMemoInfo(String fullPath) {
		MemoInfo memoInfo = null;
		try {
			FileInputStream fin = new FileInputStream(fullPath);
			ObjectInputStream ois = new ObjectInputStream(fin);
			memoInfo = (MemoInfo) ois.readObject();
			ois.close();
		}
		catch (Exception e) {
			Log.w(TAG, "file not loaded correctly :" + e);
		}
		return memoInfo;
	}
	
	public static boolean isDataConnectionAvilable(Context context){
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		boolean connected = false;
		if (cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected()) {
			connected = true;
		}
		return connected;
	}
}
