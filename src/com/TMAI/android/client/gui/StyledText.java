package com.TMAI.android.client.gui;

import java.util.ArrayList;
import java.util.List;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.style.TextAppearanceSpan;

/**
 * A Spanned with different sized text.
 */
public class StyledText implements Spanned{


	private Spanned spann = new SpannedString("");
	private String fullText = "";
	private List<StyleObject> styleObjectList = null;

	public StyledText() {
		super();
		styleObjectList= new ArrayList<StyleObject>();
	}

	public void addStyledText(String text, int textSize ){
		if (text!=null && !text.trim().equals("")){
			addStyledText(text, textSize, Color.WHITE);
		}
	}

	/**
	 * @param text - string text
	 * @param textSize - size of the text 
	 * @param color - int color of the text
	 */
	public void addStyledText(String text, int textSize ,int color){
		styleObjectList.add(new StyleObject(text, fullText.length(), fullText.length()+text.length(), color, textSize));
		fullText+=text;
	}

	private void executeStyleType(){
		spann = new SpannableString(fullText);
		Spannable textToSpan = (Spannable)spann;
		for (StyleObject styleObject : styleObjectList) {
			TextAppearanceSpan textAppearanceSpan = new TextAppearanceSpan(styleObject.getText(), 0, styleObject.getSize(), ColorStateList.valueOf(styleObject.getColor()), ColorStateList.valueOf(0));
			textToSpan.setSpan(textAppearanceSpan, styleObject.getStart(), styleObject.getEnd(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		}
		spann = textToSpan;
	}

	@Override
	public int getSpanEnd(Object obj) {
		return spann.getSpanEnd(obj);	
	}

	@Override
	public int getSpanFlags(Object obj) {
		return spann.getSpanFlags(obj);	
	}

	@Override
	public int getSpanStart(Object obj) {
		return spann.getSpanStart(obj);
	}

	@Override
	public <T> T[] getSpans(int arg0, int arg1, Class<T> arg2) {
		return (T[]) new  Spanned[]{spann};
	}

	@Override
	public int nextSpanTransition(int i, int j, Class class1) {
		// Never called since we only have one span		
		return 0;
	}

	@Override
	public char charAt(int i) {
		return spann.charAt(i);
	}

	@Override
	public int length() {
		return spann.length();
	}

	@Override
	public CharSequence subSequence(int i, int j) {
		return spann.subSequence(i, j);
	}

	public Spanned getSpanned(){
		executeStyleType();
		return spann;
	}


	public class StyleObject{

		private final String text;
		private final int start;
		private final int end;
		private final int color;
		private final int size;

		public StyleObject(String text, int start, int end, int color, int size) {
			super();
			this.text = text;
			this.start = start;
			this.end = end;
			this.color = color;
			this.size = size;
		}

		public String getText() {
			return text;
		}

		public int getStart() {
			return start;
		}

		public int getEnd() {
			return end;
		}

		public int getColor() {
			return color;
		}

		public int getSize() {
			return size;
		}
	}

}
