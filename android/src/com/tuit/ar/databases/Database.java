package com.tuit.ar.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public abstract class Database extends SQLiteOpenHelper {
	/** Keep track of context so that we can load SQL from string resources */
	protected final Context mContext;

	public Database(Context context, String databaseName, int databaseVersion) {
		super(context, databaseName, null, databaseVersion);
		this.mContext = context;
		Model.db = this;
	}

	public void onCreate(SQLiteDatabase db, String[] sql) {
		if (sql == null) return;
		db.beginTransaction();
		try {
			execMultipleSQL(db, sql);
			db.setTransactionSuccessful();
		} catch (SQLException e) {
			Log.e("Error creating tables and debug data", e.toString());
			throw e;
		} finally {
			db.endTransaction();
		}
	}

	public void onUpgrade(SQLiteDatabase db, String[] sql) {
		if (sql == null) return;
		db.beginTransaction();
		try {
			execMultipleSQL(db, sql);
			db.setTransactionSuccessful();
		} catch (SQLException e) {
			Log.e("Error updating tables and debug data", e.toString());
			throw e;
		} finally {
			db.endTransaction();
		}
	}

	/**
	  * Execute all of the SQL statements in the String[] array
	  * @param db The database on which to execute the statements
	  * @param sql An array of SQL statements to execute
	  */
	private void execMultipleSQL(SQLiteDatabase db, String[] sql){
	     for( String s : sql )
	         if (s.trim().length()>0)
	             db.execSQL(s);
	}

	public Cursor query(String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit) {
		return getReadableDatabase().query(table, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
	}

	public Cursor rawQuery(String sql, String[] selectionArgs) {
		return getReadableDatabase().rawQuery(sql, selectionArgs);
	}

	public long replace(String table, String nullColumnHack, ContentValues values) {
		return getWritableDatabase().replace(table, nullColumnHack, values);
	}
	public long insert(String table, String nullColumnHack, ContentValues values) {
		return getWritableDatabase().insert(table, nullColumnHack, values);
	}

	public int update(String table, ContentValues values, String whereClause, String[] whereArgs) {
		return getWritableDatabase().update(table, values, whereClause, whereArgs);
	}

	public int delete(String table, String whereClause, String[] whereArgs) {
		return getWritableDatabase().delete(table, whereClause, whereArgs);
	}

}
