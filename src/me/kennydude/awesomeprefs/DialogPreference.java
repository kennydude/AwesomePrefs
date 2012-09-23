package me.kennydude.awesomeprefs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.view.View;

/**
 * Shows a dialog box to take the preference value
 * 
 * TODO: Make this nicer
 * 
 * @author kennydude
 */
public abstract class DialogPreference<T> extends WidgetPreference<T> {

	public DialogPreference(Context c, PreferenceFragment f) {
		super(c, f);
	}
	
	public abstract Dialog makeDialog(AlertDialog.Builder ab);
	
	protected String DTitle, DMessage, DPos, DNeg;
	
	protected void setXMLAttribute(String namespace, String key, String value, int resId){
		if(namespace.equals(ANDROID_NAMESPACE)){ // android:
			if(key.equals("dialogtitle")){
				DTitle = getString(value, resId);
			} else if(key.equals("dialogmessage")){
				DMessage = getString(value, resId);
			} else if(key.equals("positivebuttontext")){
				DPos = getString(value, resId);
			} else if(key.equals("negativebuttontext")){
				DNeg = getString(value, resId);
			}
		}
		super.setXMLAttribute(namespace, key, value, resId);
	}
	
	@Override
	public void onClick(View v){
		AlertDialog.Builder ab = new AlertDialog.Builder(getContext());
		ab.setTitle(Title); // TODO
		makeDialog(ab).show();
	}
	
	@Override
	public View getWidget() {
		return null;
	}

}
