package com.tuit.ar.models.timeline;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.tuit.ar.api.TwitterAccount;
import com.tuit.ar.api.TwitterRequest;
import com.tuit.ar.api.request.Options;
import com.tuit.ar.api.request.UniqueRequestException;

public class User extends Status {
	private com.tuit.ar.models.User user = null;

	public User(TwitterAccount account, com.tuit.ar.models.User user) {
		super(account);
		this.user = user;
	}

	@Override
	public void refresh() {
		ArrayList <NameValuePair> nvps = new ArrayList <NameValuePair>();
		if (newestTweet > 0) nvps.add(new BasicNameValuePair("since_id", String.valueOf(newestTweet)));
		nvps.add(new BasicNameValuePair("count", "25"));
		nvps.add(new BasicNameValuePair("screen_name", user.getScreenName()));
		nvps.add(new BasicNameValuePair("include_rts", "false"));
		try {
			account.requestUrl(this.getTimeline(), nvps, TwitterRequest.Method.GET);
		} catch (UniqueRequestException e) {
			// I wanna ignore this exceptions
		} catch (Exception e) {
			failedToUpdate();
		}
	}

	@Override
	protected Options getTimeline() {
		return Options.USER_TIMELINE;
	}
}
