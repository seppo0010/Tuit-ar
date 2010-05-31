package com.tuit.ar.models;

import java.util.ArrayList;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;



public class Settings extends BroadcastReceiver {
	final static public String UPDATE_INTERVAL = "updateInterval";
	final static public String AUTOMATIC_UPDATE = "automaticUpdate";
	final static public String FILTER = "filter";
	final static public String FILTER_DELETE = "deleteFilter";
	public static final String SHOW_AVATAR = "showAvatar";
	
	final static public String UPDATE_INTERVAL_DEFAULT = "5";
	final static public Boolean AUTOMATIC_UPDATE_DEFAULT = Boolean.TRUE;
	final static public Boolean SHOW_AVATAR_DEFAULT = Boolean.FALSE;

	static private ArrayList<SettingsObserver> observers = new ArrayList<SettingsObserver>(); 
	static private Settings instance;

	public static Settings getInstance() {
		if (instance == null)
			instance = new Settings();
		return instance;
	}

	public Settings() {
		super();
		instance = this;
	}

	public String getSettingsName(Context context) {
		ConnectivityManager manager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = manager.getActiveNetworkInfo();
		if (networkInfo == null) return "none";
		else return networkInfo.getTypeName();
	}

	public SharedPreferences getSharedPreferences(Context context) {
		return context.getSharedPreferences(getSettingsName(context), Context.MODE_PRIVATE);
	}
	
	public void addObserver(SettingsObserver observer) {
		observers.add(observer);
	}

	public void removeObserver(SettingsObserver observer) {
		observers.remove(observer);
	}

	public void callObservers() {
		for (SettingsObserver observer : observers) {
			observer.settingsHasChanged(this);
		}
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		ConnectivityManager manager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		// Not sure what this does, but according documentation, if this is disabled, we should avoid all background data
		if (intent.getAction().equals(android.net.ConnectivityManager.ACTION_BACKGROUND_DATA_SETTING_CHANGED)) {
			SharedPreferences preferences = getSharedPreferences(context);
			if (manager.getBackgroundDataSetting() == false && preferences.getBoolean(Settings.AUTOMATIC_UPDATE, Settings.AUTOMATIC_UPDATE_DEFAULT)) {
				Editor editor = preferences.edit();
				editor.putBoolean(Settings.AUTOMATIC_UPDATE, false);
				if (editor.commit()) {
					callObservers();
				}
			}
		} else if (intent.getAction().equals(android.net.ConnectivityManager.CONNECTIVITY_ACTION)) {
			callObservers();
		}
	}
}
