package me.kennydude.awesomeprefs;

import android.content.Context;

/**
 * Danger header!
 * @author kennydude
 *
 */
public class DangerPreferenceCategory extends PreferenceCategory {

	public DangerPreferenceCategory(Context c, PreferenceFragment f) {
		super(c, f);
	}
	
	@Override
	protected int getLayout(){
		return R.layout.preference_danger_header;
	}

}
