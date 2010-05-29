package com.tuit.ar.api.request;

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
	public abstract boolean mustBeUnique();
}
