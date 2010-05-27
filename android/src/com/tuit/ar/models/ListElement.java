package com.tuit.ar.models;

import com.tuit.ar.databases.Model;

abstract public class ListElement extends Model {
	abstract public String getUsername();
	abstract public String getText();
	abstract public String getDisplayDate();


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
