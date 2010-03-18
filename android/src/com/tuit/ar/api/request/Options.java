package com.tuit.ar.api.request;

public enum Options {
	LOGIN {
		public String toString() {
			return "account/verify_credentials";
		}
	}, PUBLIC_TIMELINE
}
