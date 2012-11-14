package me.kennydude.awesomeprefs;

import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;


import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

/**
 * A part of the screen which will contain preferences
 * 
 * Also headers, as we mush them together
 * 
 * @author kennydude
 *
 */
public class PreferenceFragment extends Fragment {
	protected List<Preference<?>> preferences = new ArrayList<Preference<?>>();
	protected List<Integer> activityRequests;
	
	public PreferenceFragment(){}
	
	public void addPreferencesFromResource(int resource){
		addPreferencesFromXML(getResources().getXml(resource));
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(activityRequests == null) return;
		preferences.get(activityRequests.remove(requestCode)).onActivityResult(resultCode, data);
	}
	
	/**
	 * Starts an activity for a result. Handles setting an requestCode for you
	 * @param i Intent to launch
	 * @param whom Who are you?
	 */
	public void startActivityForResult(Preference<?> whom, Intent i){
		if(activityRequests == null) activityRequests = new ArrayList<Integer>();
		
		activityRequests.add(preferences.indexOf(whom));
		super.startActivityForResult(i, activityRequests.size()-1);
	}
	
	@Override
	public void onCreate(Bundle b){
		super.onCreate(b);
		pref_name =  getActivity().getPackageName() + "_preferences";
	}

	public void addPreference(Preference<?> pref){
		preferences.add(pref);
		refreshPreferenceUI();
	}
	
	/**
	 * Add preferences via XML file. See the documentation for more
	 * Should be mostly compatible with Android Preferences if not 100%
	 * 
	 * @param xml use getResources().getXml(R.xml.id)
	 */
	public void addPreferencesFromXML(XmlPullParser xml) {
		try{
			noRefresh = true;
			int eventType = xml.getEventType();
			while (eventType != XmlPullParser.END_DOCUMENT) {
				if (eventType == XmlPullParser.START_TAG) {
					
					// We only care about this one
					try{
						// 0: Overrides.
						String name = xml.getName();
						if(name.equals("Preference")){
							name = "BlankPreference";
						} else if(name.equals("CheckBoxPreference")){
							name = "SwitchPreference";
						} else if(name.equals("header")){
							name = "HeaderPreference";
						} else if(name.equals("PreferenceScreen")){
							if(xml.getDepth() == 1){ // skip root <PrefScreen>
								eventType = xml.next();
								continue;
							}
							
							name = "HeaderPreference";
						} else if(name.equals("intent") || name.equals("action")){
							Preference<?> pref = preferences.get(preferences.size()-1);
							AttributeSet attrs = Xml.asAttributeSet(xml);
							String type = xml.getName();
							
							int total = attrs.getAttributeCount()-1;
							for(int i = 0; i <= total; i++){
								String namespace = xml.getAttributeNamespace(i).toLowerCase();
								String key = xml.getAttributeName(i).toLowerCase();
								String value = xml.getAttributeValue(i);
								
								int res =  attrs.getAttributeResourceValue(i, -1);
								
								pref.setSubTagAttribute(type, namespace, key, value, res);
							}
							
							eventType = xml.next();
							continue;
						}
						
						// 1. Try to get a new class for it
						if(name.indexOf(".") == -1){ name = "me.kennydude.awesomeprefs." + name; }
						Preference<?> type = (Preference<?>)Class.forName(name)
										.getConstructor(Context.class, PreferenceFragment.class)
										.newInstance(getActivity(), this);
						
						AttributeSet attrs = Xml.asAttributeSet(xml);
						
						int total = attrs.getAttributeCount()-1;
						for(int i = 0; i <= total; i++){
							String namespace = xml.getAttributeNamespace(i).toLowerCase();
							String key = xml.getAttributeName(i).toLowerCase();
							String value = xml.getAttributeValue(i);
							int res =  attrs.getAttributeResourceValue(i, -1);
							
							type.setXMLAttribute(namespace, key, value, res);
						}
						type.refresh();
						preferences.add(type);
						
					} catch(Exception e){
						Log.e("awesomepref", "Could not load preference of tag type: " + xml.getName());
						e.printStackTrace();
					}
					
				}
				eventType = xml.next();
			}
			
			noRefresh = false;
			getActivity().runOnUiThread(new Runnable(){

				public void run() {
					refreshPreferenceUI();
				}
				
			});
		} catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@Override
	public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		return inflater.inflate(R.layout.preference_fragment, null);
	}
	
	boolean noRefresh = false;
	
	public void holdRefresh(){ noRefresh = true; }
	public void releaseRefresh(){ noRefresh = false; refreshPreferenceUI(); }
	public void removeAll(){ preferences.clear(); refreshPreferenceUI(); }
	
	void refreshPreferenceUI(){
		if(noRefresh) return;
		Log.d("ui", "pref");
		
		ScrollView sv = (ScrollView)getActivity().findViewById(R.id.preference_fragment_scroll);
		int y = 0;
		if(sv != null)
			y = sv.getScrollY();
		
		LinearLayout lf = (LinearLayout) getActivity().findViewById(R.id.preference_fragment_content);
		lf.removeAllViews();
		
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		
		int id = 0;
		for(final Preference<?> pref : preferences){
			View v = pref.getView();
			v.setTag(pref.Key);
			v.setOnClickListener(new OnClickListener(){

				public void onClick(View theView) {
					if(pref.pclick != null)
						if(!pref.pclick.onPreferenceClick(pref))
							return;
					
					pref.onClick(theView);
				}
				
			});
			if(pref.Dep != null){
				if(!findPreference(pref.Dep).isOn()){
					v.setEnabled(false);
				}
			}
			if(pref.VisibleIf != ""){
				if(!this.getSharedPreferences().getBoolean(pref.VisibleIf, false)){
					continue;
				}
			}
			lf.addView(v, params);
			
			boolean nextDiv = true;
			if(preferences.size()-1 > id){
				nextDiv = preferences.get(id+1).needsDivider();
			}
			
			if(pref.needsDivider() && nextDiv){
				ImageView sep = (ImageView) LayoutInflater.from(getActivity()).inflate(R.layout.preference_divider, null);
				lf.addView(sep, params);
			}
			id++;
		}
		
		if(sv != null)
			sv.scrollTo(0, y);
		
	}
	
	@SuppressWarnings("rawtypes")
	public Preference findPreference(String key){
		key = key.toLowerCase();
		for(Preference<?> pref : preferences){
			if(pref.Key.equals(key)){
				return pref;
			}
		}
		return null;
	}
	
	public Preference<?>[] findPreferencesByClass(String className){
		List<Preference<?>> p = new ArrayList<Preference<?>>();
		for(Preference<?> pref : preferences){
			if(pref.Class.equals(className)){
				p.add(pref);
			}
		}
		return p.toArray(new Preference<?>[]{});
	}
	
	/**
	 * Switch screen
	 * @param switchString String of what to switch to. May begin with "fragment:" or "xml:"
	 */
	protected void SwitchPreferenceScreen(String title, String switchString, Bundle extras){
		((PreferenceActivity)getActivity()).SwitchPrefrenceScreen(title, switchString, extras);
	}
	
	String pref_name;
	
	public SharedPreferences getSharedPreferences(){
		return PreferenceManager.getDefaultSharedPreferences(getActivity());
	}
}
