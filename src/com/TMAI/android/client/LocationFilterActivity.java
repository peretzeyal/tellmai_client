package com.TMAI.android.client;


import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.TMAI.android.client.dialog.DialogUtils;
import com.TMAI.android.client.gui.GuiUtils;
import com.TMAI.android.client.listadapter.CheckBoxifiedText;
import com.TMAI.android.client.listadapter.CheckBoxifiedTextListAdapter;
import com.TMAI.android.client.location.PlacesInRadius;
import com.TMAI.android.client.prefs.Prefs;
import com.TMAI.android.client.utils.GeneralUtils;

public class LocationFilterActivity extends Activity {
    /** Called when the activity is first created. */
	
	private CheckBoxifiedTextListAdapter cbla;
	private ListView filterLV;
	private EditText radiusTV;
	
	private String[] items ;
	private String[] itemsChecked ;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.places_filter_dialog);

        initGUI();
        updateListData();
    }
    
    
    private void initGUI(){
        filterLV = (ListView) findViewById(R.id.filter_listView);
        
        radiusTV = (EditText) findViewById(R.id.radius_editText);
        radiusTV.setText(Prefs.getGooglePlacesRadius());
        
        Button saveButton = (Button) findViewById(R.id.save_button);
        saveButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				try {
					String[] selctedItems = cbla.getSelctedItems();
					String filter = GeneralUtils.join(selctedItems, "|");
					//Prefs.setGooglePlacesFilter(filter);
					Prefs.setGooglePlacesFilter(filter);
					Prefs.setGooglePlacesRadius(radiusTV.getText().toString());
					DialogUtils.createToast(LocationFilterActivity.this, LocationFilterActivity.this.getString(R.string.places_filter_toast_successful_change));

					finish();
				} catch (Exception e) {
					e.printStackTrace();
					DialogUtils.createToast(LocationFilterActivity.this, LocationFilterActivity.this.getString(R.string.places_filter_toast_unsuccessful_change));
				}
			}
		});
    }
    
    private void updateListData(){
        items =  GeneralUtils.split(PlacesInRadius.ORIGINAL_FILTER, '|', true);
        itemsChecked =  GeneralUtils.split(Prefs.getGooglePlacesFilter(), '|', true);
        cbla = new CheckBoxifiedTextListAdapter(this);
        for (String item : items) {
        	boolean checked = false;
        	for (String itemChecked : itemsChecked) {
        		if(itemChecked.equals(item)){
        			checked = true;
        			break;
        		}
        	}
        	cbla.addItem(new CheckBoxifiedText(item, checked));
		}
        filterLV.setAdapter(cbla);
    }
    
    /* Remember the other methods of the CheckBoxifiedTextListAdapter class!!!!
     * cbla.selectAll() :: Check all items in list
     * cbla.deselectAll() :: Uncheck all items
     */
}