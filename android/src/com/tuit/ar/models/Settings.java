package com.tuit.ar.models;

import java.util.ArrayList;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;



public class Settings {
	final static public String UPDATE_INTERVAL = "updateInterval";
	final static public String AUTOMATIC_UPDATE = "automaticUpdate";
	final static public String UPDATE_INTERVAL_DEFAULT = "5";
	final static public Boolean AUTOMATIC_UPDATE_DEFAULT = Boolean.TRUE;

	private ArrayList<SettingsObserver> observers = new ArrayList<SettingsObserver>(); 
	static private Settings instance;

	public static Settings getInstance() {
		if (instance == null)
			instance = new Settings();
		return instance;
	}
	private Settings() {
		super();
	}

	public SharedPreferences getSharedPreferences(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context);
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
}
