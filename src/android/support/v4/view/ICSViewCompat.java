package android.support.v4.view;

import android.support.v4.view.ViewPager.MyAccessibilityDelegate;

public class ICSViewCompat {

	public static void setAccessibilityDelegate(ViewPager viewPager,
			MyAccessibilityDelegate myAccessibilityDelegate) {
		viewPager.setAccessibilityDelegate(myAccessibilityDelegate);
	}

}
