package com.TMAI.android.client.location;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.location.Location;
import android.util.Log;

public class PlacesInRadius {

	//private String result = "";  
	private String googleAPIKey = "AIzaSyDept8zDAOUIUw4O-BQMx_cJvwr6iTI398"; 
	private String searchRadius = "1000";
	private String filter = "";
	private Location myLocation;
	final String TAG = getClass().getSimpleName();  

	public PlacesInRadius(Location location){
		myLocation = location;
	}

	public JSONArray getPlaces(){
		String requestResult = callGoogleWebService(buildURLForGooglePlaces(myLocation));
		JSONArray jsonArray = convertJSONtoArray(requestResult);
		return jsonArray;
	}


	private String callGoogleWebService(String url){ 
		String result = null;
		HttpClient httpclient = new DefaultHttpClient();  
		HttpGet request = new HttpGet(url);  
		//request.addHeader("deviceId", deviceId);  
		ResponseHandler<String> handler = new BasicResponseHandler();  
		try {  
			result = httpclient.execute(request, handler);  
		} catch (ClientProtocolException e) {  
			e.printStackTrace();  
		} catch (IOException e) {  
			e.printStackTrace();  
		}  
		httpclient.getConnectionManager().shutdown();  
		return result;
	}//callWebService

	private String buildURLForGooglePlaces(Location location){
		String baseUrl = "https://maps.googleapis.com/maps/api/place/search/json?";
		String lat = String.valueOf(location.getLatitude());
		String lon = String.valueOf(location.getLongitude());
		String url = baseUrl + "location=" + lat + "," + lon + "&" +
		"radius=" + searchRadius + "&" + "sensor=true" +
		"&" + "key=" + googleAPIKey+"&" + "types=" + filter;
		return url;
	}

	private JSONArray convertJSONtoArray(String rawJSON){
		JSONArray results = null;
		try {
			JSONObject completeJSONObj = new JSONObject(rawJSON);
			String json = completeJSONObj.toString();
			Log.d(TAG,json);
			//JSONObject results = completeJSONObj.getJSONObject("results");
			results = completeJSONObj.getJSONArray("results");
		} catch (JSONException e) {
			Log.d(TAG,"JSON parsing error - fix it:" + e.getMessage());
		}
		return results;
	}
}
