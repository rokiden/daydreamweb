package com.rokiden.daydreamweb;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;


public class Preferences extends PreferenceActivity implements OnSharedPreferenceChangeListener {

    private ListPreference preferenceInterval;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
        preferenceInterval = (ListPreference) getPreferenceScreen().findPreference("pref_key_interval");
    }

    @Override
    public void onResume() {
        super.onResume();
        updateListPreferenceSummaries();
        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        updateListPreferenceSummaries();
    }

    private void updateListPreferenceSummaries() {
        preferenceInterval.setSummary("Refresh interval: " + preferenceInterval.getEntry());
    }
}
