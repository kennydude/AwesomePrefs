package me.kennydude.awesomeprefs.compat;

import android.annotation.TargetApi;
import android.content.Context;
import android.view.View;
import android.widget.Switch;

/**
 * Methods to trick Dalvik
 * @author kennydude
 *
 */
@TargetApi(14)
public class Api14 {
	public static View getSwitch(Context c){
		return new Switch(c);
	}
}
