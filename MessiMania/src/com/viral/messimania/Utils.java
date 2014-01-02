package com.viral.messimania;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class Utils {
	
	private static final String PREF_NAME = "Footballlive";
	
	/**
	 * Set preference
	 */
	public static void setPreference(Context context, String key, String value) {
		SharedPreferences preference = context.getSharedPreferences(PREF_NAME,
				Context.MODE_PRIVATE);
		Editor editor = preference.edit();
		editor.putString(key, value);
		editor.commit();
	}
	
	/**
	 * Set preference
	 */
	public static String getPreference(Context context, String key) {
		SharedPreferences preference = context.getSharedPreferences(PREF_NAME,
				Context.MODE_PRIVATE);
		return preference.getString(key, null);
	}
	
}
