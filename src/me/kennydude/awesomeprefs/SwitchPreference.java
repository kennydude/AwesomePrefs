package me.kennydude.awesomeprefs;

import me.kennydude.awesomeprefs.compat.Api14;
import android.content.Context;
import android.os.Build;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

/**
 * SwitchPreference.
 * 
 * This will downgrade to a CheckBox below API 14
 * @author kennydude
 *
 */
public class SwitchPreference extends WidgetPreference<Boolean> {

	public SwitchPreference(Context c, PreferenceFragment f) {
		super(c, f);
		Value = false;
		DefaultValue = false;
	}
	
	@Override
	protected boolean isOn(){
		return Value;
	}

	@Override
	public View getWidget() {
		CompoundButton r;
		if(Build.VERSION.SDK_INT >= 14){
			r = (CompoundButton) Api14.getSwitch(getContext());
		} else { r = new CheckBox(getContext()); }
		
		if(Value != null) r.setChecked(Value);
		
		return r;
	}
	
	@Override
	public void onClick(View v){
		setValue(!Value);
		((CompoundButton)v.findViewById(R.id.widget)).setChecked(Value);
	}
	
	public void setChecked(Boolean value){ setValue(value); }

	@Override
	protected Class<?> getType() {
		return Boolean.class;
	}

}
