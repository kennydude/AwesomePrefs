package android.support.v4.view;

import android.os.Build;
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

}
