package com.tuit.ar.activities;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.tuit.ar.R;
import com.tuit.ar.api.Twitter;
import com.tuit.ar.api.TwitterObserver;
import com.tuit.ar.api.TwitterRequest;
import com.tuit.ar.api.request.Options;

public class NewTweet extends Activity implements OnClickListener, TwitterObserver {
	private String replyToUser;
	private String replyToTweetId;
	private EditText messageField;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.new_tweet);
		Button send = (Button)findViewById(R.id.send);
		send.setOnClickListener(this);
		Twitter.getInstance().addObserver(this);

		Intent intent = getIntent();
		replyToTweetId = intent.getStringExtra("reply_to_id");
		replyToUser = intent.getStringExtra("reply_to_username");
		messageField = (EditText) findViewById(R.id.tweetMessage);
		if (replyToUser != null) {
			TextView username = (TextView)findViewById(R.id.replyToUsername);
			username.setText("In reply to: @" + replyToUser);
			messageField.setText("@" + replyToUser + " ");
		}
	}

	@Override
	public void onClick(View v) {
		String message = messageField.getText().toString();
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("status", message));
		params.add(new BasicNameValuePair("source", getString(R.string.twitterSource)));
		if (replyToTweetId != null) params.add(new BasicNameValuePair("in_reply_to_status_id", replyToTweetId));
		TwitterRequest.Method method = TwitterRequest.Method.POST;
		try {
			Twitter.getInstance().requestUrl(Options.POST_TWEET, params, method);
		} catch (Exception e) {
			sendFailed();
		}
	}
	public void sendFailed() {
		Toast.makeText(this, getString(R.string.unableToPost), Toast.LENGTH_SHORT).show();
	}

	public void requestHasStarted(TwitterRequest request) {
		setProgressBarIndeterminateVisibility(true);
	}

	public void requestHasFinished(TwitterRequest request) {
		setProgressBarIndeterminateVisibility(false);
    	if (request.getUrl().equals(Options.POST_TWEET)) {
    		if (request.getStatusCode() >= 200 && request.getStatusCode() < 400) {
    			Toast.makeText(this, getString(R.string.messageSent), Toast.LENGTH_SHORT);
    			finish();
    			try {
    				Twitter.getInstance().requestUrl(Options.FRIENDS_TIMELINE);
    			} catch (Exception e) {}
    		}
    		else sendFailed();
    	}
	}

	public void onDestroy() {
		super.onDestroy();
		Twitter.getInstance().removeObserver(this);
	}
}
