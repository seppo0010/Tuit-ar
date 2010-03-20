package com.tuit.ar.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.Preference.OnPreferenceClickListener;
import android.util.Log;

import com.tuit.ar.R;

public class Preferences extends PreferenceActivity {
	ListPreference updateInterval;
	CheckBoxPreference automaticUpdate;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.layout.preferences);

		automaticUpdate = (CheckBoxPreference) findPreference("automaticUpdate");
		automaticUpdate.setPersistent(true);
		automaticUpdate.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			@Override
			public boolean onPreferenceClick(Preference preference) {
				updateInterval.setEnabled(automaticUpdate.isChecked());
				return false;
			}
		});
		updateInterval = (ListPreference) findPreference("updateInterval");
		updateInterval.setPersistent(true);
		SharedPreferences customSharedPreference = PreferenceManager.getDefaultSharedPreferences(this);
		Log.d("au", String.valueOf(customSharedPreference.getBoolean("automaticUpdate", Boolean.TRUE)));
		Log.d("ui", String.valueOf(customSharedPreference.getString("updateInterval", "")));
	}
}