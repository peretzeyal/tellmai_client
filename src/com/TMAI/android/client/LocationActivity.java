package com.TMAI.android.client;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import com.TMAI.android.client.data.Places;
import com.TMAI.android.client.dialog.DialogUtils;
import com.TMAI.android.client.location.DeviceLocation;
import com.TMAI.android.client.location.PlacesInRadius;

/**
 * @author Administrator
 *
 */
public class LocationActivity extends BaseAppActivity{

	public static final int LOCATION_ACTIVITY = 101;
	public static final String LOCATION_NAME = "location_name";

	private static final int MENU_FILTER = 1011;
	private static final int MENU_REFRESH = 1012;

	/*public static final String LOCATION_NAME_GOOGLE = "location_name";
	public static final String LOCATION_NAME_FOURSQUARE = "name";*/

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.places_dialog);

		//PlacesDialog.createDialog(this, placesList);
		initGUI();
	}



	private void initGUI() {
		Button cancelButton = (Button) findViewById(R.id.places_dialog_cancel_button);
		cancelButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				returnLocationName("");
			}
		});
		new GooglePlacesConnectionTask().execute();
	}


	/**
	 * @param location
	 * @return the location name can't contain spaces (all spaces will be replaced with underscore)
	 */
	private String cleanLocationName(String location){
		location = location.trim();
		location = location.replace(" ", "_");
		return location;
	}

	private void returnLocationName(String name){
		//return the value to main activity
		Intent intent = new Intent();
		intent.putExtra(LOCATION_NAME, cleanLocationName(name));
		setResult(LOCATION_ACTIVITY, intent);
		finish();
	}

	public List<Places> getPlacesArrayList(JSONArray jsonArray){
		List<Places> placesList = new ArrayList<Places>();
		for (int i=0;i<jsonArray.length();i++){
			try {
				JSONObject jsonObject = (JSONObject) jsonArray.get(i);
				placesList.add(new Places(jsonObject));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return placesList;
	}


	private class GooglePlacesConnectionTask extends AsyncTask<Void, Void, Void> {
		ProgressDialog pd;
		List<Places> placesList = null;
		JSONArray jsonArray = null;
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			if (jsonArray!=null){
				final List<Places> placesList = getPlacesArrayList(jsonArray);
				List<String> nameList = new ArrayList<String>();
				for (Places places : placesList) {
					nameList.add(places.getName());
				}

				ListView locationListView = (ListView) findViewById(R.id.places_dialog_list_lv);
				ArrayAdapter<String> adp = new ArrayAdapter<String>(LocationActivity.this, android.R.layout.simple_list_item_1, nameList);
				locationListView.setAdapter(adp);

				locationListView.setOnItemClickListener(new OnItemClickListener() {
					public void onItemClick (AdapterView<?> parent, View view, int position, long id){
						returnLocationName(placesList.get(position).getName());
					}
				});
			}
			else{
				//unable to get the current location
				DialogUtils.createToast(LocationActivity.this, getString(R.string.toast_retrieving_location_error));
			}
			if (pd != null) {
				pd.cancel();
			}
		}


		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pd = new ProgressDialog(LocationActivity.this);
			pd.setMessage(getResources().getString(R.string.places_progress_dialog_title));
			pd.setTitle(getResources().getString(R.string.places_progress_dialog_message));
			pd.setCancelable(false);
			pd.show();
		}

		@Override
		protected Void doInBackground(Void... params) {
			try {
				Location location = DeviceLocation.getLocation(LocationActivity.this);
				if (location==null){
					return null;
				}
				/*location.setLatitude(40.714100);
				location.setLongitude(-74.006300);*/
				PlacesInRadius placesInRadius = new PlacesInRadius(location);
				jsonArray = placesInRadius.getPlaces();
			} catch (Exception e) {
				Context context = LocationActivity.this;
				DialogUtils.createToast(context, context.getString(R.string.toast_connection_problem));
				e.printStackTrace();
			}
			return null;
		}

	}

	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, MENU_FILTER, 0, R.string.places_menu_filter);
		menu.add(0, MENU_REFRESH, 0, R.string.places_menu_refresh);


		return true;
	}

	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		case MENU_FILTER:
			Intent intent = new Intent(LocationActivity.this,LocationFilterActivity.class);
			startActivity(intent);

//			startActivityForResult(intent, LocationFilterActivity1.LOCATION_FILTER_ACTIVITY);
			break;
		case MENU_REFRESH:
			new GooglePlacesConnectionTask().execute();
			break;
		}
		return true;
	}
	
/*	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == LocationFilterActivity1.LOCATION_FILTER_ACTIVITY){
			if (data != null){
				String locationName = data.getStringExtra(LocationFilterActivity1.LOCATION_FILTER_NAME);
				if (locationName!=null){
					//TODO refresh
				}
			}
		}
	}*/
	
}
