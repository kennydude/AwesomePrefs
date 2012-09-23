package me.kennydude.awesomeprefs;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Standard preference with layout:
 * 
 * Text         Widget
 * Summary
 * @author kennydude
 *
 */
public abstract class WidgetPreference<T> extends Preference<T> {

	public WidgetPreference(Context c, PreferenceFragment f) {
		super(c, f);
	}
	
	protected void setXMLAttribute(String namespace, String key, String value, int resId){
		if(namespace.equals(ANDROID_NAMESPACE)){ // android:
			if(key.equals("icon")){
				if(value.startsWith("?")){
					Icon = getContext().getTheme().obtainStyledAttributes(new int[]{
						Integer.parseInt(value.substring(1))	
					}).getDrawable(0);
				} else
					Icon = getContext().getResources().getDrawable(resId);
			}
		}
		super.setXMLAttribute(namespace, key, value, resId);
	}
	
	protected Drawable Icon;

	@Override
	public View getView() {
		View r = this.getInflater().inflate(R.layout.preference_layout, null);
		
		((TextView)r.findViewById(R.id.title)).setText(Title);
		
		String summary = getSummary();
		if(summary.isEmpty()){
			((TextView)r.findViewById(R.id.summary)).setVisibility(View.GONE);
			((TextView)r.findViewById(R.id.summary)).setLayoutParams(new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.MATCH_PARENT,
					LinearLayout.LayoutParams.MATCH_PARENT
			));
			((TextView)r.findViewById(R.id.title)).setGravity(Gravity.CENTER_VERTICAL);
		} else
			((TextView)r.findViewById(R.id.summary)).setText(summary);
		
		View widget = getWidget();
		if(widget != null){ // null is allowed in case you don't want one
			widget.setClickable(clickableWidget());
			((NullView)r.findViewById(R.id.widget)).replace(widget);
		}
		
		widget = getIconView();
		if(widget != null){ // null is allowed in case you don't want one
			widget.setClickable(false);
			((NullView)r.findViewById(R.id.icon)).replace(widget);
		}
		return r;
	}
	
	/**
	 * The view that displays the icon in use
	 * @return
	 */
	public View getIconView(){
		if(Icon != null){
			ImageView iv = new ImageView(getContext());
			iv.setImageDrawable(Icon);
			return iv;
		}
		return null;
	}
	
	public abstract View getWidget();
	public boolean clickableWidget(){ return false; }
}
