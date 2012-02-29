package com.TMAI.android.client.location;

import java.io.IOException;
import java.net.URLEncoder;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.TMAI.android.client.prefs.Prefs;

import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

public class PlacesInRadius {

	//private String result = "";  
	//Google Places data
	private String googleSearchURL = "https://maps.googleapis.com/maps/api/place/search/json?";
	private String googleAPIKey = "AIzaSyDept8zDAOUIUw4O-BQMx_cJvwr6iTI398"; 
	public static  String searchRadius = "150";
	private String filter = "";
	public static String ORIGINAL_FILTER = "accounting||airport|amusement_park|art_gallery|bakery|bank|bar|beauty_salon|bicycle_store|book_store|bowling_alley"
		+"|cafe|car_dealer|car_rental|car_repair|car_wash|casino|clothing_store|convenience_store|department_store"
		+"|electronics_store|establishment|florist|food|furniture_store|gas_station|grocery_or_supermarket|gym|hair_care"
		+"|hardware_store|home_goods_store|hospital|insurance_agency|jewelry_store|laundry|library|liquor_store|lodging"
		+"|meal_delivery|meal_takeaway|movie_rental|movie_theater|museum|night_club|pet_store|pharmacy"
		+"|post_office|restaurant|shoe_store|shopping_mall|spa|store|taxi_stand|travel_agency|university|zoo";

	//Foursquare Venues data
	private String foursquareSearchURL = "https://api.foursquare.com/v2/venues/search?";
	private String foursquareAPIKey = "FR3P0NPRJHPMCAPYKJD4CSGOXVSP1SGJXIAWEZONTEGZ5LLV"; 

	private enum SerachType{
		GOOGLE_PLACES,
		FOURSQUARE_VANUES;
	}
	private SerachType currentSearch = SerachType.GOOGLE_PLACES;


	private Location myLocation;
	final String TAG = getClass().getSimpleName();  

	public PlacesInRadius(Location location){
		myLocation = location;
	}

	public JSONArray getPlaces(){
		String requestResult = null;
		switch (currentSearch) {
		case FOURSQUARE_VANUES:
			requestResult = callGoogleWebService(buildURLFoursquareVenues(myLocation));
			break;
		case GOOGLE_PLACES:
			requestResult = callGoogleWebService(buildURLForGooglePlaces(myLocation));
			break;
		}

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
		filter = Prefs.getGooglePlacesFilter();
		searchRadius = Prefs.getGooglePlacesRadius();
		String lat = String.valueOf(location.getLatitude());
		String lon = String.valueOf(location.getLongitude());
		String url = googleSearchURL + "location=" + lat + "," + lon + "&" +
		"radius=" + searchRadius + "&" + "sensor=true" +
		"&" + "key=" + googleAPIKey+"&" + "types=" + URLEncoder.encode(filter);
		return url;
	}

	private String buildURLFoursquareVenues(Location location){
		String lat = String.valueOf(location.getLatitude());
		String lon = String.valueOf(location.getLongitude());
		String url = foursquareSearchURL + "ll=" + lat + "," + lon + "&" +
		"radius=" + searchRadius + "&" +
		"&" + "oauth_token=" + foursquareAPIKey+"&" + "intent=checkin"  ;
		return url;
	}


	private JSONArray convertJSONtoArray(String rawJSON){
		JSONArray results = null;
		try {
			JSONObject completeJSONObj = new JSONObject(rawJSON);
			String json = completeJSONObj.toString();
			Log.d(TAG,json);
			switch (currentSearch) {
			case FOURSQUARE_VANUES:
				results = praseFoursquareJason(completeJSONObj);
				break;
			case GOOGLE_PLACES:
				results = completeJSONObj.getJSONArray("results");
				break;
			}
			//JSONObject results = completeJSONObj.getJSONObject("results");
			//results = completeJSONObj.getJSONArray("results");
		} catch (JSONException e) {
			Log.d(TAG,"JSON parsing error - fix it:" + e.getMessage());
		}
		return results;
	}

	private JSONArray praseFoursquareJason(JSONObject completeJSONObj){
		JSONArray results = null;
		try {
			JSONObject jsonObject = completeJSONObj.getJSONObject("response");
			JSONArray groups = jsonObject.getJSONArray("groups");
			results = new JSONArray();
			for (int i = 0; i < groups.length(); i++) {
				JSONObject group = (JSONObject) groups.get(i);
				JSONArray items = (JSONArray) group.getJSONArray("items");
				for (int j = 0; j < items.length(); j++) {
					JSONObject item = (JSONObject) items.get(j);
					String id = item.getString("id");
					String name = item.getString("name");
					String type = group.getString("type");
					JSONObject location = (JSONObject) item.getJSONObject("location");
					int distance  = location.getInt("distance");

					
					
					
/*					int insetIndex = 0;
					for (; insetIndex < results.length(); insetIndex++) {
						JSONObject insetIndexObject = results.getJSONObject(insetIndex);
						if(insetIndexObject.getInt("distance")>distance){
							break;
						}
					}
					
					for (int k=results.length(); k < insetIndex; k--) {
						
					}
					*/
					JSONObject jObject = new JSONObject();
					//TODO add filter by type
					jObject.accumulate("id", id);
					jObject.accumulate("name", name);
					jObject.accumulate("distance", distance);
					results. put(jObject);

				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return results;
	}


}
