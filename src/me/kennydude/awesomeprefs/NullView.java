package me.kennydude.awesomeprefs;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

/**
 * A null, replaceable view that does nothing
 * @author kennydude
 *
 */
public class NullView extends View {
	public NullView(Context context){super(context); }
	public NullView(Context context, AttributeSet attrs) {	super(context, attrs); }
	public NullView(Context context, AttributeSet attrs, int x) { super(context, attrs, x); }

	public void replace(View replacement){
		ViewParent daddy = this.getParent();
		if(daddy != null && replacement != null){
			ViewGroup parent = (ViewGroup) daddy;
			
			replacement.setId(getId());
			replacement.setPadding(getPaddingLeft(), getPaddingTop(), getPaddingRight(), getPaddingBottom());
			int index = parent.indexOfChild(this);
			parent.removeViewInLayout(this);
			
			ViewGroup.LayoutParams layoutParams = getLayoutParams();
			if(layoutParams != null){
				parent.addView(replacement, index, layoutParams);
			} else{
				parent.addView(replacement, index);
			}
			
		}
	}
	
}
