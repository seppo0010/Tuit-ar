package com.tuit.ar.activities;

import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.Preference.OnPreferenceClickListener;

import com.tuit.ar.R;
import com.tuit.ar.models.Settings;
import com.tuit.ar.preferences.DialogPreference;
import com.tuit.ar.preferences.DialogPreferenceListener;
import com.tuit.ar.preferences.EditTextPreference;

public class Preferences extends PreferenceActivity {
	ListPreference updateInterval;
	CheckBoxPreference automaticUpdate;
	EditTextPreference filter;
	DialogPreference filterDelete;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(getString(R.string.preferences));
		addPreferencesFromResource(R.layout.preferences);

		automaticUpdate = (CheckBoxPreference) findPreference(Settings.AUTOMATIC_UPDATE);
		automaticUpdate.setPersistent(true);
		automaticUpdate.setDefaultValue(Settings.AUTOMATIC_UPDATE_DEFAULT);
		automaticUpdate.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			public boolean onPreferenceClick(Preference preference) {
				updateInterval.setEnabled(automaticUpdate.isChecked());
				updateSettings();
				return false;
			}
		});
		updateInterval = (ListPreference) findPreference(Settings.UPDATE_INTERVAL);
		updateInterval.setPersistent(true);
		updateInterval.setDefaultValue(Settings.UPDATE_INTERVAL_DEFAULT);
		updateInterval.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			public boolean onPreferenceClick(Preference preference) {
				updateSettings();
				return false;
			}
		});

		filter = (EditTextPreference)getPreferenceScreen().findPreference(Settings.FILTER);
		filter.setText(Settings.getInstance().getSharedPreferences(this).getString(Settings.FILTER, ""));
		filter.setDialogPreferenceListener(new DialogPreferenceListener() {
			public void onDialogClosed(boolean positiveValue) {
				if (positiveValue) {
					String text = filter.getEditText().getText().toString();
					Editor editor = Settings.getInstance().getSharedPreferences(Preferences.this).edit();
					editor.putString(Settings.FILTER, text);
					if (editor.commit()) {
						filter.setText(text);
						updateSettings();
					}
				}
			}
		});

		filterDelete = (com.tuit.ar.preferences.DialogPreference)findPreference(Settings.FILTER_DELETE);
		filterDelete.setDialogPreferenceListener(new DialogPreferenceListener() {
			public void onDialogClosed(boolean positiveValue) {
				if (positiveValue) {
					Editor editor = Settings.getInstance().getSharedPreferences(Preferences.this).edit();
					editor.putString(Settings.FILTER, "");
					if (editor.commit()) {
						updateSettings();
						filter.setText("");
					}
				}
			}
		});
	}

	protected void updateSettings() {
		Settings.getInstance().callObservers();
	}
}