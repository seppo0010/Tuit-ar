package com.tuit.ar.models.timeline;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.tuit.ar.api.TwitterAccount;
import com.tuit.ar.api.TwitterRequest;
import com.tuit.ar.api.request.Options;
import com.tuit.ar.api.request.UniqueRequestException;

public class ViewConversation extends Status {
	public ViewConversation(TwitterAccount account, com.tuit.ar.models.Status first) {
		super(account);
		tweets.add(first);
	}

	@Override
	protected Options getTimeline() {
		Options tweet = Options.GET_TWEET;

		long nextTweetId = tweets.get(tweets.size()-1).getInReplyToStatusId();
		ArrayList <NameValuePair> nvps = new ArrayList <NameValuePair>();
		nvps.add(new BasicNameValuePair("id", String.valueOf(nextTweetId)));
		tweet.setParameters(nvps);
		return tweet;
	}

	public void refresh() {
		long nextTweetId = tweets.get(tweets.size()-1).getInReplyToStatusId();
		if (nextTweetId == 0) return;
		
		ArrayList <NameValuePair> nvps = new ArrayList <NameValuePair>();
		nvps.add(new BasicNameValuePair("id", String.valueOf(nextTweetId)));
		try {
			account.requestUrl(this.getTimeline(), nvps, TwitterRequest.METHOD_GET);
		} catch (UniqueRequestException e) {
			// I wanna ignore this exceptions
		} catch (Exception e) {
			failedToUpdate(e.getLocalizedMessage());
		}
	}

	public void requestHasFinished(TwitterRequest request) {
		if (!request.getUrl().equals(this.getTimeline())) return;
		try {
			JSONObject tweetJ = new JSONObject(new JSONTokener(request.getResponse()));
			if (tweetJ != null && tweetJ.has("error") == false) {
				com.tuit.ar.models.Status tweet = new com.tuit.ar.models.Status(tweetJ);
				//is_home INTEGER, is_reply INTEGER, belongs_to_user
				tweet.setBelongsToUser(account.getUser().getId());
				// FIXME: some kind of way to recognize this with more abstraction?
				if (request.getUrl().equals(Options.FRIENDS_TIMELINE) || request.getUrl().equals(Options.REPLIES_TIMELINE)) {
					tweet.setHome(request.getUrl().equals(Options.FRIENDS_TIMELINE));
					tweet.setReply(request.getUrl().equals(Options.REPLIES_TIMELINE));
				}

				this.tweets.add(tweet);
			}

			timelineChanged();
		} catch (Exception e) {
			failedToUpdate(e.getLocalizedMessage());
		}
		finishedUpdate();
	}
}
