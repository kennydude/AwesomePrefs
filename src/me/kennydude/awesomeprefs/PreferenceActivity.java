package me.kennydude.awesomeprefs;

import org.xmlpull.v1.XmlPullParser;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;

/**
 * Preference Activity.
 * 
 * This allows you to create a screen for you to have a basic stab at
 * preferences
 * 
 * @author kennydude
 * 
 */
public class PreferenceActivity extends Activity {
	boolean is_phone;
	boolean is_phone_headers = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		if(getString(R.string.awesomepref_layout).equals("phone")){
			// Phone layout
			is_phone = true;
			
			if(getIntent().hasExtra("screen")){
				String screen = getIntent().getStringExtra("screen");
				if(screen.startsWith("fragment:")){
					setContentView(R.layout.preference_fragment);
					
					Bundle extras = new Bundle();
					if(getIntent().hasExtra("extras"))
						extras = getIntent().getBundleExtra("extras");
					
					Fragment f = Fragment.instantiate(this, screen.substring(9), extras);
			        FragmentTransaction transaction = getFragmentManager().beginTransaction();
			        //transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
			        transaction.add(0, f);
			        transaction.commit();
			        getFragmentManager().executePendingTransactions();
			        
			        setTitle(getIntent().getStringExtra("title"));
				} else if(screen.startsWith("xml:")){
					// tood
				}
			} else{
				setContentView(R.layout.preference_activity_single);
				is_phone_headers = true;
			}
		} else{
			// Tablet layout
			is_phone = false;
			
			// todo
		}
	}
	
	int getHeaderId(){
		return is_phone ? R.id.preference_fragment : R.id.preference_fragment;//_headers;
	}
	
	/**
	 * Add headers if they should be added
	 * @param resource
	 */
	public void addHeaders(int resource){
		addHeaders(getResources().getXml(resource));
	}
	
	public void addHeaders(XmlPullParser xml){
		if(is_phone == true && is_phone_headers == false) return;
		
		((PreferenceFragment)getFragmentManager().findFragmentById(getHeaderId()))
			.addPreferencesFromXML(xml);
		
		getHeaderFragment().noRefresh = true;
		
		finishAddingPreferences();
		
		getHeaderFragment().noRefresh = false;
		getHeaderFragment().refreshPreferenceUI();
	}
	public void finishAddingPreferences(){}
	
	public PreferenceFragment getHeaderFragment(){
		return ((PreferenceFragment)getFragmentManager().findFragmentById(getHeaderId()));
	}

	public void SwitchPrefrenceScreen(String title, String switchString, Bundle extras) {
		if(is_phone){
			Intent intent = new Intent(this, getClass());
			intent.putExtra("screen", switchString);
			intent.putExtra("title", title);
			if(extras != null)
				intent.putExtra("extras", extras);
			startActivity(intent);
		} else{
			// TODO
		}
	}
	
}
