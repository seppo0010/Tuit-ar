package com.tuit.ar.models;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import com.tuit.ar.databases.Model;

import android.content.ContentValues;

public class DirectMessage extends ListElement {
	private static final String[] columns = new String[]{
		"id", "sender_id", "date", "text", "belongs_to_user"
	};

	private JSONObject dataSourceJSON;
	private User sender = null;
	private Date createDate = null;
	private long dateMillis = 0;
	private String text = null;
	private long belongs_to_user;
	private long id;

	public DirectMessage(JSONObject jsonObject) {
		super();
		dataSourceJSON = jsonObject;
	}

	public User getSender() {
		if (sender != null) return sender;
		try {
			return sender = new User(dataSourceJSON.getJSONObject("sender"));
		} catch (JSONException e) {
			return null;
		}
	}

	public void setSender(User sender) {
		this.sender = sender;
	}

	public long getId() {
		if (id != 0) return id;
		try {
			return id = dataSourceJSON.getLong("id");
		} catch (JSONException e) {
			return 0;
		}
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getText() {
		if (text != null) return text;
		try {
			setText(dataSourceJSON.getString("text"));
			return text;
		} catch (Exception e) {
			return "";
		}
	}

	public void setText(String text) {
		this.text = sanitize(text);
	}

	public long getBelongsToUser() {
		return belongs_to_user;
	}

	public void setBelongsToUser(long belongsToUser) {
		belongs_to_user = belongsToUser;
	}

	public Date getDate() {
		try {
			if (createDate != null) return createDate;
			SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM d hh:mm:ss Z yyyy");
			return createDate = sdf.parse(dataSourceJSON.getString("created_at"));
		} catch (Exception e) {
			return null;
		}
	}

	public long getDateMillis() {
		if (dateMillis == 0) {
			Date date = this.getDate();
			if (date == null) return (long)0;
			dateMillis = date.getTime();
		}
		return dateMillis;
	}

	@Override
	protected ContentValues getValues() {
		ContentValues fields = new ContentValues();
		fields.put(columns[0], getId());
		fields.put(columns[1], getSender().getId());
		fields.put(columns[2], getDateMillis() / 1000);
		fields.put(columns[3], getText());
		fields.put(columns[4], getBelongsToUser());
		return fields;
	}

	@Override
	public String getUsername() {
		return getSender().getScreenName();
	}

	@Override
	public String getDisplayDate() {
		return calculateElapsed(getDateMillis());
	}

	@SuppressWarnings("unchecked")
	static public ArrayList<DirectMessage> select(String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit) {
		return (ArrayList<DirectMessage>)Model.select(DirectMessage.class, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
	}
}
