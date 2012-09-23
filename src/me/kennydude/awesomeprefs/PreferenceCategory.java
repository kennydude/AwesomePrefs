package me.kennydude.awesomeprefs;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

public class PreferenceCategory extends BlankPreference {

	public PreferenceCategory(Context c, PreferenceFragment f) {
		super(c, f);
	}
	
	protected int getLayout(){
		return R.layout.preference_header;
	}
	
	@Override
	public View getView(){
		View r = getInflater().inflate(getLayout(), null);
		((TextView)r.findViewById(R.id.title)).setText(Title);
		return r;
	}
	
	public boolean needsDivider(){
		return false;
	}
	
	/**
	 * Adds a "child" preference. (don't tell anyone, but it just adds one after me)
	 * @param pref
	 */
	public void addPreference(Preference<?> pref){
		int me = getFragment().preferences.indexOf(this);
		getFragment().preferences.add(me+1, pref);
		getFragment().refreshPreferenceUI();
	}

}
