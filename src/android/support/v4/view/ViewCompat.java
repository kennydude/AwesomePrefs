package android.support.v4.view;

import android.os.Build;
import android.support.v4.view.ViewPager.MyAccessibilityDelegate;
import android.view.View;

/**
 * Minimal implementation of ViewCompat
 * @author kennydude
 *
 */
public class ViewCompat {
	public static final int IMPORTANT_FOR_ACCESSIBILITY_AUTO = 0;
	public static final int IMPORTANT_FOR_ACCESSIBILITY_YES = 1;

	public static int getImportantForAccessibility(View view) {
		if(Build.VERSION.SDK_INT >= 16) return JBViewCompat.getImportantForAccessibility(view);
		return 0;
	}

	public static void setImportantForAccessibility(
			View v, int importantForAccessibilityYes) {
		if(Build.VERSION.SDK_INT >= 16) JBViewCompat.setImportantForAccessibility(v, importantForAccessibilityYes);
	}

	public static void postInvalidateOnAnimation(View v) {
		if(Build.VERSION.SDK_INT >= 16) JBViewCompat.postInvalidateOnAnimation(v);
		else v.invalidate();
	}

	public static void setAccessibilityDelegate(ViewPager viewPager,
			MyAccessibilityDelegate myAccessibilityDelegate) {
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) ICSViewCompat.setAccessibilityDelegate(viewPager, myAccessibilityDelegate);
	}

	public static void postOnAnimation(View viewPager,
			Runnable mEndScrollRunnable) {
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) JBViewCompat.postOnAnimation(viewPager, mEndScrollRunnable);
		else viewPager.postDelayed(mEndScrollRunnable, getFrameTime());
	}
	
	private static final long FAKE_FRAME_TIME = 10;
	static long getFrameTime() {
         return FAKE_FRAME_TIME;
    }

}
