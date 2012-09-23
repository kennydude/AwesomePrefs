package me.kennydude.awesomeprefs;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

public class HeaderPreference extends BlankPreference {
	String switchString;
	Intent fireIntent;
	Bundle switchExtras;

	public HeaderPreference(Context c, PreferenceFragment f) {
		super(c, f);
	}
	
	@Override
	protected void setXMLAttribute(String namespace, String key, String value, int resId){
		if(namespace.equals(ANDROID_NAMESPACE)){
			if(key.equals("fragment")){
				switchString = "fragment:" + value;
				return;
			}
		}
		super.setXMLAttribute(namespace, key, value, resId);
	}
	
	protected void setSubTagAttribute(String tag, String namespace, String key, String value, int resId){
		if(tag.equals("intent")){
			if(fireIntent == null){ fireIntent = new Intent(); }
			if(namespace.equals(ANDROID_NAMESPACE)){
				if(key.equals("action")){
					fireIntent.setAction(getString(value, resId));
				} else if(key.equals("data")){
					fireIntent.setData(Uri.parse(getString(value, resId)));
				} else if(key.equals("targetclass")){
					pClass = value;
					setIntentComponent();
				} else if(key.equals("targetpackage")){
					pName = value;
					setIntentComponent();
				}
			}
		}
	}
	
	public void setIntent(Intent fire){
		this.fireIntent = fire;
	}
	
	void setIntentComponent(){
		// Check if we can, and if we can set the class name
		if(pName != null && pClass != null){
			fireIntent.setClassName(pName, pClass);
		}
	}
	
	public void setFragment(Class<? extends PreferenceFragment> fragment, Bundle extras){
		fireIntent = null;
		switchString = "fragment:" + fragment.getName();
		switchExtras = extras;
	}
	
	String pName, pClass;
	
	@Override
	protected void onClick(View v){
		if(fireIntent != null){
			getContext().startActivity(fireIntent);
		} else{
			getFragment().SwitchPreferenceScreen(Title, switchString, switchExtras);
		}
	}

}
