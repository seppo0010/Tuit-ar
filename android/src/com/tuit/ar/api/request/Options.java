package com.tuit.ar.api.request;

public enum Options {
	LOGIN {
		public String toString() {
			return "account/verify_credentials";
		}
	}, FRIENDS_TIMELINE {
		public String toString() {
			return "statuses/home_timeline";
		}
	}
}
