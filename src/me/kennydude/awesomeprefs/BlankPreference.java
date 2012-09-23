package me.kennydude.awesomeprefs;

import android.content.Context;
import android.view.View;

public class BlankPreference extends WidgetPreference<String> {

	public BlankPreference(Context c, PreferenceFragment f) {
		super(c, f);
	}

	@Override
	public View getWidget() {
		return null;
	}

	@Override
	protected Class<?> getType() {
		return String.class;
	}

}
