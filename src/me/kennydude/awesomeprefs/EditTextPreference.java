package me.kennydude.awesomeprefs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.EditText;

public class EditTextPreference extends DialogPreference<String> {

	public EditTextPreference(Context c, PreferenceFragment f) {
		super(c, f);
		DefaultValue = "";
	}

	@Override
	public Dialog makeDialog(AlertDialog.Builder ab) {
		
		final EditText ed = new EditText(getContext());
		ed.setText(Value);
		
		ab.setView(ed);
		ab.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
			
		});
		ab.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				setValue(ed.getText().toString());
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
