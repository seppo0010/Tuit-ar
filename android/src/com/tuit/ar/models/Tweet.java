package com.tuit.ar.models;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

public class Tweet {
	private JSONObject dataSourceJSON;
	private Date createDate;
	private long dateMillis;

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

	public long getDateMillis() {
		if (dateMillis == 0) {
			Date date = this.getDate();
			if (date == null) return (long)0;
			dateMillis = date.getTime();
		}
		return dateMillis;
	}

	static public String calculateElapsed(long millis) {
		    int ageInSeconds = (int)((System.currentTimeMillis() - millis) / 1000);
		    
		    if (ageInSeconds < 0) {
		        return "just now";
		    }
		    if (ageInSeconds < 60) {
		        int n = ageInSeconds;
		        return "about " + n + " second" + (n > 1 ? "s" : "") + " ago";
		    }
		    if (ageInSeconds < 60 * 60) {
		        int n = (int) Math.floor(ageInSeconds/60);
		        return "about " + n + " minute" + (n > 1 ? "s" : "") + " ago";
		    }
		    if (ageInSeconds < 60 * 60 * 24) {
		        int n = (int) Math.floor(ageInSeconds/60/60);
		        return "about " + n + " hour" + (n > 1 ? "s" : "") + " ago";
		    }
		    if (ageInSeconds < 60 * 60 * 24 * 7) {
		        int n = (int) Math.floor(ageInSeconds/60/60/24);
		        return "about " + n + " day" + (n > 1 ? "s" : "") + " ago";
		    }
		    if (ageInSeconds < 60 * 60 * 24 * 31) {
		        int n = (int) Math.floor(ageInSeconds/60/60/24/7);
		        return "about " + n + " week" + (n > 1 ? "s" : "") + " ago";
		    }
		    if (ageInSeconds < 60 * 60 * 24 * 365) {
		        int n = (int) Math.floor(ageInSeconds/60/60/24/31);
		        return "about " + n + " month" + (n > 1 ? "s" : "") + " ago";
		    }
		    int n = (int)Math.floor(ageInSeconds/60/60/24/365);
		    return n + " year" + (n > 1 ? "s" : "") + " ago";
	}
}
