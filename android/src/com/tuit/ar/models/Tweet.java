package com.tuit.ar.models;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

public class Tweet {
	private JSONObject dataSourceJSON;
	private Date createDate;

	public Tweet(JSONObject object) {
		this.dataSourceJSON = object;
	}

	public String getId() {
		try {
			return dataSourceJSON.getString("id");
		} catch (JSONException e) {
			return null;
		}
	}

	public String getUsername() {
		try {
			return dataSourceJSON.getJSONObject("user").getString("screen_name");
		} catch (JSONException e) {
			return null;
		}
	}

	public String getMessage() {
		try {
			return dataSourceJSON.getString("text");
		} catch (JSONException e) {
			return null;
		}
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
}
