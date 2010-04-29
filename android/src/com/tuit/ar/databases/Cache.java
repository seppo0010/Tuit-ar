package com.tuit.ar.databases;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.tuit.ar.R;

public class Cache extends Database {
	/** The name of the database file on the file system */
	protected static final String DATABASE_NAME = "database";
	/** The version of the database that this class understands. */
	protected static final int DATABASE_VERSION = 1;

	public Cache(Context context) {
		super(context, DATABASE_NAME, DATABASE_VERSION);
	}

	public void onCreate(SQLiteDatabase db) {
		String[] sql = mContext.getString(R.string.cache_onCreate).split("\n");
		super.onCreate(db, sql);
	}

	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		super.onUpgrade(db, null);
	}
}
