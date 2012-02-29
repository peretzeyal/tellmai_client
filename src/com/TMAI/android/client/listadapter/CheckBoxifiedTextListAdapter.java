package com.TMAI.android.client.listadapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.TMAI.android.client.R;

/** @author Steven Osborn - http://steven.bitsetters.com */
public class CheckBoxifiedTextListAdapter extends BaseAdapter {

	private class ViewHolder {
		TextView text;
		CheckBox checkbox;
	}
	
	/** Remember our context so we can use it when constructing views. */
	private Context mContext;
	private LayoutInflater mInflater;

	private List<CheckBoxifiedText> mItems = new ArrayList<CheckBoxifiedText>();

	public CheckBoxifiedTextListAdapter(Context context) {
		mContext = context;
		mInflater = LayoutInflater.from(context);
	}

	public void addItem(CheckBoxifiedText it) {
		mItems.add(it); 
	}

	public void setListItems(List<CheckBoxifiedText> lit) {
		mItems = lit; 
	}

	/** @return The number of items in the */
	public int getCount() { 
		return mItems.size(); 
	}

	public Object getItem(int position) { 
		return mItems.get(position); 
	}

	public void setChecked(boolean value, int position){
		mItems.get(position).setChecked(value);
	}
	public void selectAll(){
		for(CheckBoxifiedText cboxtxt: mItems)
			cboxtxt.setChecked(true);
		/* Things have changed, do a redraw. */
		this.notifyDataSetInvalidated();
	}
	public void deselectAll()
	{
		for(CheckBoxifiedText cboxtxt: mItems)
			cboxtxt.setChecked(false);
		/* Things have changed, do a redraw. */
		this.notifyDataSetInvalidated();
	}

	public boolean areAllItemsSelectable() { return false; }

	/** Use the array index as a unique id. */
	public long getItemId(int position) {
		return position;
	}

	/** @param convertView The old view to overwrite, if one is passed
	 * @returns a CheckBoxifiedTextView that holds wraps around an CheckBoxifiedText */
	public View getView(int position, View convertView, ViewGroup parent){
		
		
		
		ViewHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.places_filter_dialog_row, null);
			holder = new ViewHolder();
			holder.text = (TextView) convertView.findViewById(R.id.textView1);
			holder.checkbox = (CheckBox) convertView.findViewById(R.id.checkBox1);

			convertView.setTag(holder);
		} else {
			// Get the ViewHolder back to get fast access to the TextView
			// and the ImageView.
			holder = (ViewHolder) convertView.getTag();
		}
		CheckBoxifiedText src = mItems.get(position);
		holder.text.setText(src.getText());
		holder.checkbox.setChecked(src.isChecked());
		holder.checkbox.setTag(position);
		
		holder.checkbox.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				int index = Integer.valueOf(v.getTag().toString());
				mItems.get(index).setChecked(((CheckBox) v).isChecked());
			}
		});
/*		holder.checkbox.setOnCheckedChangeListener(new OnCheckedChangeListener() {			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				try {
					CheckBoxifiedText src = mItems.get(Integer.parseInt(buttonView.getTag().toString()));
					src.setChecked(isChecked);
				} catch (NumberFormatException e) {
					e.printStackTrace();
				}
				
			}
		});*/
		return convertView;
/*		ViewHolder btv;
		if (convertView == null) {
			btv = new ViewHolder();
		} else { // Reuse/Overwrite the View passed
			// We are assuming(!) that it is castable!
			CheckBoxifiedText src = mItems.get(position);
			btv = (ViewHolder) convertView;
			btv.setCheckBoxState(src.getChecked()); 
			btv = (ViewHolder) convertView;
			btv.setText(mItems.get(position).getText());
		}
		return btv;*/
	}
	
	public String[] getSelctedItems(){
		List<String> result = new ArrayList<String>();
		for (CheckBoxifiedText item : mItems) {
			if(item.isChecked()){
				result.add(item.getText());
			}
			
		}
		return (String[]) result.toArray(new String[0]);
	}
}