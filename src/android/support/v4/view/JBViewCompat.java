package android.support.v4.view;

import android.annotation.TargetApi;
import android.view.View;

/**
 * DO NOT USE ME DIRECTLY. USE VIEWCOMPAT ONLY!
 * @author kennydude
 *
 */
@TargetApi(16)
public class JBViewCompat {

	public static int getImportantForAccessibility(View view) {
		return view.getImportantForAccessibility();
	}

	public static void setImportantForAccessibility(View v,
			int importantForAccessibilityYes) {
		v.setImportantForAccessibility(importantForAccessibilityYes);
	}

	public static void postInvalidateOnAnimation(View v) {
		v.postInvalidateOnAnimation();
	}

}
