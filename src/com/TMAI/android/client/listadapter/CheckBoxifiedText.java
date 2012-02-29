package com.TMAI.android.client.listadapter;

public class CheckBoxifiedText {
	private String text;
	private boolean checked;
	
	public CheckBoxifiedText(String text, boolean checked) {
		super();
		this.text = text;
		this.checked = checked;
	}
	
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public boolean isChecked() {
		return checked;
	}
	public void setChecked(boolean checked) {
		this.checked = checked;
	}
}
