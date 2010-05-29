package com.tuit.ar.api.request;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.NameValuePair;

public enum Options {
	LOGIN {
		public String toString() {
			return "account/verify_credentials";
		}

		@Override
		public boolean mustBeUnique() {
			return false;
		}
	}, FRIENDS_TIMELINE {
		public String toString() {
			return "statuses/home_timeline";
		}

		@Override
		public boolean mustBeUnique() {
			return true;
		}
	}, REPLIES_TIMELINE {
		public String toString() {
			return "statuses/mentions";
		}

		@Override
		public boolean mustBeUnique() {
			return true;
		}
	}, USER_TIMELINE {
		public String toString() {
			return "statuses/user_timeline";
		}

		@Override
		public boolean mustBeUnique() {
			return true;
		}
	}, FAVORITES_TIMELINE {
		public String toString() {
			return "favorites";
		}

		@Override
		public boolean mustBeUnique() {
			return true;
		}
	}, DIRECT_MESSAGES {
		public String toString() {
			return "direct_messages";
		}

		@Override
		public boolean mustBeUnique() {
			return true;
		}
	}, SEND_DIRECT_MESSAGE {
		public String toString() {
			return "direct_messages/new";
		}

		@Override
		public boolean mustBeUnique() {
			return false;
		}
	}, DELETE_DIRECT_MESSAGE {
		public String toString() {
			return "direct_messages/destroy";
		}

		@Override
		public boolean mustBeUnique() {
			return false;
		}
	}, POST_TWEET {
		public String toString() {
			return "statuses/update";
		}

		@Override
		public boolean mustBeUnique() {
			return false;
		}
	}, POST_TWEET_WITH_PHOTO {
		public String toString() {
			return "statuses/update_with_photo";
		}

		@Override
		public boolean mustBeUnique() {
			return false;
		}
	}, ADD_TO_FAVORITES {
		public String toString() {
			return "favorites/create/" + parameters.get("id");
		}

		@Override
		public boolean mustBeUnique() {
			return false;
		}
	}, REMOVE_FROM_FAVORITES {
		public String toString() {
			return "favorites/destroy/" + parameters.get("id");
		}

		@Override
		public boolean mustBeUnique() {
			return false;
		}
	}, FOLLOW {
		public String toString() {
			return "friendships/create";
		}

		@Override
		public boolean mustBeUnique() {
			return false;
		}
	}, UNFOLLOW {
		public String toString() {
			return "friendships/destroy";
		}

		@Override
		public boolean mustBeUnique() {
			return false;
		}
	};
	protected HashMap<String, String> parameters = null;

	public abstract boolean mustBeUnique();
	public void setParameters(ArrayList<NameValuePair> nvps) {
		parameters = new HashMap<String, String>();
		for (NameValuePair nvp : nvps) {
			parameters.put(nvp.getName(), nvp.getValue());
		}
	}
}
