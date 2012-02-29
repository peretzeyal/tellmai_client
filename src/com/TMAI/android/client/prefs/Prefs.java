package com.TMAI.android.client.prefs;


import com.TMAI.android.client.location.PlacesInRadius;

import android.content.Context;
import android.content.SharedPreferences;

public class Prefs {
	private static final String TAG = "Prefs";
	
	private static final String SP_FILE_NAME = "spapp";
	private static final String FIRST_LOGIN = "first_login";
	private static final String LOCATION_QUESTION = "location_question";
	private static final String CONTACT_EMAIL = "contact_email";
	private static final String AUDIO_FOLDER = "audio_folder";
	private static final String OLD_SCREEN_TIMEOUT = "old_screen_timeout";
	private static final String GOOGLE_PLACES_FILTER = "goolge_places_filter";
	private static final String GOOGLE_PLACES_RADIUS = "goolge_places_radius";



	private static SharedPreferences sharedPreferences;
	private static SharedPreferences.Editor editor;;

	public static void init(Context context) {
		sharedPreferences = context.getSharedPreferences(SP_FILE_NAME, Context.MODE_PRIVATE);
		editor = sharedPreferences.edit();
	}	
	
	
	//first login answer
	public static boolean isFirstLogin() {
		return sharedPreferences.getBoolean(FIRST_LOGIN, true);
	}

	public static void setFirstLogin(boolean value) {
		editor.putBoolean(FIRST_LOGIN, value);
		editor.commit();
	}
	
	//location question answer
	public static boolean isLocationApproved() {
		return sharedPreferences.getBoolean(LOCATION_QUESTION, false);
	}

	public static void setLocationApproved(boolean value) {
		editor.putBoolean(LOCATION_QUESTION, value);
		editor.commit();
	}
	
	//contact email
	public static String getContactEmail() {
		return sharedPreferences.getString(CONTACT_EMAIL, null);
	}

	public static void setContactEmail(String value) {
		editor.putString(CONTACT_EMAIL, value);
		editor.commit();
	}
	
	//audio folder
	public static String getAduioFolder() {
		return sharedPreferences.getString(AUDIO_FOLDER, null);
	}

	public static void setAduioFolder(String value) {
		editor.putString(AUDIO_FOLDER, value);
		editor.commit();
	}

	//screen timeout
	public static int getOldScreenTimeout() {
		return sharedPreferences.getInt(OLD_SCREEN_TIMEOUT, -1);
	}

	public static void setOldScreenTimeout(int value) {
		editor.putInt(OLD_SCREEN_TIMEOUT, value);
		editor.commit();
	}
	
	//Google Places Filter
	public static String getGooglePlacesFilter() {
		return sharedPreferences.getString(GOOGLE_PLACES_FILTER, "");
	}

	public static void setGooglePlacesFilter(String value) {
		editor.putString(GOOGLE_PLACES_FILTER, value);
		editor.commit();
	}
	
	//Google Places Radius
	public static String getGooglePlacesRadius() {
		return sharedPreferences.getString(GOOGLE_PLACES_RADIUS, PlacesInRadius.searchRadius);
	}

	public static void setGooglePlacesRadius(String value) {
		editor.putString(GOOGLE_PLACES_RADIUS, value);
		editor.commit();
	}
	
}