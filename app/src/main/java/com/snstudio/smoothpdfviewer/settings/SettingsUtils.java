package com.snstudio.smoothpdfviewer.settings;

import android.app.Activity;

import androidx.preference.PreferenceManager;

public class SettingsUtils {
    static final String PREF_NIGHT_MODE = "pdf.view.night.mode";
    static final String PREF_PAGE_FLING = "pdf.view.page.fling";
    static final String PREF_ANTI_ALIASING = "pdf.view.anti.aliasing";
    static final String PREF_PAGE_SWIPE = "pdf.view.page.swipe";
    static final String PREF_PAGE_SNAP = "pdf.view.page.snap";

    public static boolean resolveNightMode(Activity activity) {
        return PreferenceManager.getDefaultSharedPreferences(activity).getBoolean(PREF_NIGHT_MODE, false);
    }

    public static boolean resolveFling(Activity activity) {
        return PreferenceManager.getDefaultSharedPreferences(activity).getBoolean(PREF_PAGE_FLING, false);
    }

    public static boolean resolveAntiAliasing(Activity activity) {
        return PreferenceManager.getDefaultSharedPreferences(activity).getBoolean(PREF_ANTI_ALIASING, true);
    }

    public static boolean resolvePageSwipe(Activity activity) {
        return PreferenceManager.getDefaultSharedPreferences(activity).getBoolean(PREF_PAGE_SWIPE, true);
    }

    public static boolean resolvePageSnap(Activity activity) {
        return PreferenceManager.getDefaultSharedPreferences(activity).getBoolean(PREF_PAGE_SNAP, false);
    }

}