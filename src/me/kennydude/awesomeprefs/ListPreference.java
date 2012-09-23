package me.kennydude.awesomeprefs;

import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;

public class ListPreference extends DialogPreference<String> {
	String[] displayValues, actualValues;
	
	@Override
	protected void setXMLAttribute(String namespace, String key, String value, int resId){
		if(namespace.equals(ANDROID_NAMESPACE)){
			if(key.equals("entries")){
				displayValues = getContext().getResources().getStringArray(resId); // Expecting resource
			} else if(key.equals("entryvalues")){
				actualValues = getContext().getResources().getStringArray(resId); // Expecting resource
			}
		}
		super.setXMLAttribute(namespace, key, value, resId);
	}
	
	@Override
	public String getSummary(){
		String r;
		if(!(r = super.getSummary()).equals("")) return r;
		
		return getDisplayValue();
	}

	public ListPreference(Context c, PreferenceFragment f) {
		super(c, f);
	}
	
	protected String getDisplayValue(){
		int p = getPos();
		if(displayValues.length > p){
			return displayValues[p];
		}
		
		return Value.toString();
	}
	
	int getPos(){
		int p = 0;
		for(CharSequence val : actualValues){
			if(val.equals(Value)){
				return p;
			}
			p++;
		}
		return 0;
	}
	
	public CharSequence[] getEntries(){
		return actualValues;
	}
	
	public int findIndexOfValue(String needle){
		int p = 0;
		for(CharSequence val : actualValues){
			if(val.equals(needle)){
				return p;
			}
			p++;
		}
		return -1;
	}

	@Override
	public Dialog makeDialog(Builder ab) {
		ab.setSingleChoiceItems(displayValues, getPos(), new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				setValue(actualValues[which].toString());
			}
			
		});
		ab.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
			
		});
		return ab.create();
	}

	@Override
	protected Class<?> getType() {
		return String.class;
	}

}
