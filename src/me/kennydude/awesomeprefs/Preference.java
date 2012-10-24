package me.kennydude.awesomeprefs;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.LayoutInflater;
import android.view.View;

/**
 * A preference
 * @author kennydude
 *
 */
public abstract class Preference<T extends Object> {
	public static final String ANDROID_NAMESPACE = "http://schemas.android.com/apk/res/android";
	public static final String AWESOME_NAMESPACE = "http://schemas.android.com/apk/awesomeprefs";
	
	/**
	 * Sorry, Java's fault not mine ;__;
	 * @return
	 */
	protected abstract Class<?> getType();
	
	public Preference(Context c, PreferenceFragment f){
		mContext = c;
		mFragment = f;
	}
	
	public static abstract class OnPreferenceChangeListener{
		public abstract boolean onPreferenceChange(@SuppressWarnings("rawtypes") Preference preference, Object newValue);
	}
	
	public static interface OnPreferenceClickListener{
		public boolean onPreferenceClick(@SuppressWarnings("rawtypes") Preference pref);
	}
	
	public String Class = "";
	public String VisibleIf = "";
	
	String getString(String value, int resId){
		if(resId != -1){
			return mContext.getResources().getString(resId);
		} else{
			return value;
		}
	}
	
	@SuppressWarnings("unchecked")
	T getResource(int resId){
		Class<?> type = getType();
		if(type.equals(String.class)){
			return (T) mContext.getResources().getString(resId);
		} else if(type.equals(Boolean.class)){
			return (T)((Boolean) mContext.getResources().getBoolean(resId));
		} else if(type.equals(Integer.class)){
			return (T)((Integer) mContext.getResources().getInteger(resId));
		}
		
		return null;
	}
	
	@SuppressWarnings("unchecked")
	T getResource(String def, int res){
		try{
			return getResource(res);
		} catch(Exception e){
			if(getType().equals(String.class))
				return (T) def;
			return null;
		}
	}
	
	Boolean getBoolean(String def, int res){
		if(res != -1){
			return mContext.getResources().getBoolean(res);
		} else{
			if(def.toLowerCase().equals("true")){
				return true;
			}
			return false;
		}
	}
	
	protected OnPreferenceChangeListener pcl;
	public void setOnPreferenceChangeListener(OnPreferenceChangeListener pcl){
		this.pcl = pcl;
	}
	
	protected OnPreferenceClickListener pclick;
	public void setOnPreferenceClickListener(OnPreferenceClickListener pcl){
		this.pclick = pcl;
	}
	
	public void setEnabled(Boolean on){
		Enabled = on;
		getFragment().refreshPreferenceUI();
	}
	
	public String getKey(){
		return Key;
	}
	public String getTitle(){
		return Title;
	}
	
	protected void setXMLAttribute(String namespace, String key, String value, int resId){
		if(namespace.equals(ANDROID_NAMESPACE)){ // android:
			if(key.equals("key")){ // no set methods. we are not refreshing
				Key = value.toLowerCase();
			} else if(key.equals("title")){
				Title = getString(value, resId);
			} else if(key.equals("summary")){
				Summary = getString(value, resId);
			} else if(key.equals("defaultvalue")){
				DefaultValue = getResource(value, resId);
			} else if(key.equals("dependency")){
				Dep = getString(value, resId);
			} else if(key.equals("enabled")){
				Enabled = getBoolean(value, resId);
			}
		} else if(namespace.equals(AWESOME_NAMESPACE)){ // aprefs:
			if(key.equals("class")){
				Class = getString(value, resId);
			} else if(key.equals("visibleif")){
				VisibleIf = getString(value, resId);
			}
		}
	}
	
	protected boolean isOn(){
		return Enabled;
	}
	
	/**
	 * Deletes Self
	 */
	public void remove(){
		getFragment().preferences.remove(this);
	}
	
	protected void setSubTagAttribute(String tag, String namespace, String key, String value, int resId){
		// Default: We do not accect sub-tags
	}
	
	protected String getDisplayValue(){
		return Value.toString();
	}
	
	public String getSummary(){
		if(Summary == null) return "";
		
		if(Value != null)
			if(getDisplayValue() != null)
				return Summary.replace("{value}", getDisplayValue());
		return Summary.replace("{value}", "");
	}
	
	public void setSummary(String value){
		Summary = value;
		getFragment().refreshPreferenceUI();
	}
	
	public boolean needsDivider(){
		return true;
	}
	
	Context mContext;
	PreferenceFragment mFragment;
	
	protected String Key = "", Title, Summary, Dep;
	protected T Value, DefaultValue;
	protected Boolean Enabled;
	
	public void setKey(String Key){
		this.Key = Key.toLowerCase();
		refresh();
	}
	
	public T getValue(){
		return Value;
	}
	
	public void setTitle(String t){ Title = t; getFragment().refreshPreferenceUI(); }
	
	SharedPreferences getPreferences(){
		return mFragment.getSharedPreferences();
	}
	public SharedPreferences getSharedPreferences(){ return getPreferences();}
	
	public void setValue(T value){
		if(this.pcl != null){
			if(!pcl.onPreferenceChange(this, value)){
				return;
			}
		}
		
		Value = value;
		
		Editor e = getPreferences().edit();
		if(String.class.equals(Value.getClass())){
			e.putString(Key, (String) Value);
		} else if(Boolean.class.equals(Value.getClass())){
			e.putBoolean(Key, (Boolean)Value);
		} else if(Integer.class.equals(Value.getClass())){
			e.putInt(Key, (Integer)Value);
		} else if(Long.class.equals(Value.getClass())){
			e.putLong(Key, (Long)Value);
		} else if(Float.class.equals(Value.getClass())){
			e.putFloat(Key, (Float)Value);
		}
		
		e.commit();
		refresh();
	}
	
	@SuppressWarnings("unchecked")
	protected void refresh(){
		
		SharedPreferences prefs = getPreferences();
		Value = (T) prefs.getAll().get(Key);
		if(Value == null) Value = DefaultValue;
		
		getFragment().refreshPreferenceUI();
		
	}
	
	public abstract View getView();
	protected LayoutInflater getInflater(){
		return LayoutInflater.from(mContext);
	}
	protected Context getContext(){ return mContext; }
	protected PreferenceFragment getFragment(){ return mFragment; }
	
	/**
	 * Fired when the view the preference is on, is tapped or clicked by the user
	 * @param theView
	 */
	protected void onClick(View theView){
		// By default we do nothing at all
	}

	/**
	 * Fired when your activity was returned
	 * @param resultCode
	 * @param data
	 */
	public void onActivityResult(int resultCode, Intent data) {
		// Nothing
	}
}
