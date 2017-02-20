package com.sfsu.investickation;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v7.widget.Toolbar;

/**
 * Contains all the app related settings for the application.
 */
public class SettingsActivity extends MainBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // FIXME : mToolbar hidden
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_top_base);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }

        // Display the fragment as the main content for Account Settings
        getFragmentManager().beginTransaction().replace(R.id.settings_fragment_container,
                new SettingsFragment()).commit();
        // getFragmentManager().beginTransaction().replace(android.R.id.content,
        //  new SettingsFragment()).commit();

    }

    @Override
    public void onBackPressed() {
        int count = getSupportFragmentManager().getBackStackEntryCount();
        if (count == 0) {
            Intent homeIntent = new Intent(SettingsActivity.this, MainActivity.class);
            startActivity(homeIntent);
            finish();
            super.onBackPressed();
        } else if (count > 0) {
            getSupportFragmentManager().popBackStack();
        }
    }

    /**
     * Account Settings Fragment that allows users to tweak some app related settings.
     */
    public static class SettingsFragment extends PreferenceFragment {

        private static final String TAG = "~!@#$SettingsFrag";
        // Strong reference
        SharedPreferences.OnSharedPreferenceChangeListener mListener =
                new SharedPreferences.OnSharedPreferenceChangeListener() {
                    public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
                        if (key.equals(R.string.KEY_PREF_ACTIVITIES_COUNT)) {
                            Preference pref = findPreference(key);
                            pref.setSummary(prefs.getString(key, ""));
                        } else if (key.equals(R.string.KEY_PREF_RECEIVE_NOTIFICATIONS)) {
                            Preference pref = findPreference(key);
                            pref.setSummary(prefs.getString(key, ""));
                        }
                    }
                };

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            addPreferencesFromResource(R.xml.pref_user_settings);
        }

        @Override
        public void onResume() {
            super.onResume();
            getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(mListener);
        }

        @Override
        public void onPause() {
            super.onPause();
            getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(mListener);
        }
    }
}