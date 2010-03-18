package com.tuit.ar.models;

import org.json.JSONException;
import org.json.JSONObject;

public class Tweet {
	private JSONObject dataSourceJSON;

	public Tweet(JSONObject object) {
		this.dataSourceJSON = object;
	}

	public String getId() {
		try {
			return dataSourceJSON.getString("id");
		} catch (JSONException e) {
			return "";
		}
	}

	public String getUsername() {
		try {
			return dataSourceJSON.getJSONObject("user").getString("screen_name");
		} catch (JSONException e) {
			return "";
		}
	}

	public String getMessage() {
		try {
			return dataSourceJSON.getString("text");
		} catch (JSONException e) {
			return "";
		}
	}
}
