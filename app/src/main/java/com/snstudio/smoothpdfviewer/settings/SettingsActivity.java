package com.snstudio.smoothpdfviewer.settings;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;

import com.snstudio.smoothpdfviewer.R;

import org.jetbrains.annotations.NotNull;


public class SettingsActivity extends AppCompatActivity {
    public static String pdfUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings, new SettingsFragment())
                    .commit();
        }
        defineActionBar();
        pdfUri = getIntent().getExtras().getString("pdfUri");
    }

    private void defineActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setBackgroundDrawable(new ColorDrawable(getResources().
                    getColor(R.color.action_bar_color, getTheme())));
            Spannable title = new SpannableString(actionBar.getTitle());
            title.setSpan(new ForegroundColorSpan(getResources()
                            .getColor(R.color.main_text_color, getTheme())),
                    0, title.length(),
                    Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            actionBar.setTitle(title);
        }
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull @NotNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {

        private SharedPreferences.OnSharedPreferenceChangeListener mListenerOptions;

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);

            mListenerOptions = (sharedPreferences, key) -> {
                if (SettingsUtils.PREF_NIGHT_MODE.equals(key) ||
                        SettingsUtils.PREF_PAGE_FLING.equals(key) ||
                        SettingsUtils.PREF_ANTI_ALIASING.equals(key) ||
                        SettingsUtils.PREF_PAGE_SWIPE.equals(key) ||
                        SettingsUtils.PREF_PAGE_SNAP.equals(key)) {
                    if (pdfUri != null)
                        restartActivity(requireActivity());
                }
            };
        }

        private void restartActivity(Activity activity) {
            Intent returnIntent = new Intent();
            returnIntent.putExtra("pdfUri", SettingsActivity.pdfUri);
            activity.setResult(Activity.RESULT_OK, returnIntent);
        }

        @Override
        public void onResume() {
            super.onResume();
            getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(mListenerOptions);
        }

        @Override
        public void onPause() {
            getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(mListenerOptions);
            super.onPause();
        }

    }
}