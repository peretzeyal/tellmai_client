package com.TMAI.android.client.data;

import org.json.JSONException;
import org.json.JSONObject;


public class Places {

	private String id;
	private String name;
	private double latitude;
	private double longitude;	

	public Places(String id, String name, double latitude, double longitude) {
		super();
		this.id = id;
		this.name = name;
		this.latitude = latitude;
		this.longitude = longitude;
	}
	
	public Places(JSONObject jsonObject) {
		super();
		try {
		this.id = jsonObject.getString("id");
		this.name = jsonObject.getString("name");
		JSONObject geometry = (JSONObject) jsonObject.get("geometry");
		JSONObject location = (JSONObject) geometry.get("location");
		this.latitude = Double.valueOf(location.getString("lat"));
		this.longitude = Double.valueOf(location.getString("lng"));
		} catch (JSONException e) {
			e.printStackTrace();
		};
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
}
