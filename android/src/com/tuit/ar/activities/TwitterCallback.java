package com.tuit.ar.activities;

import oauth.signpost.AbstractOAuthConsumer;
import oauth.signpost.OAuthConsumer;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import com.tuit.ar.R;
import com.tuit.ar.api.Twitter;
import com.tuit.ar.api.TwitterAccount;

public class TwitterCallback extends Activity {
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Uri uri = this.getIntent().getData();
		if (uri != null && uri.toString().startsWith(Twitter.CALLBACK_URL)) {
			String verifier = uri.getQueryParameter("oauth_verifier");  
  
			try {
				Twitter twitter = Twitter.getInstance();
				OAuthConsumer consumer = twitter.getConsumer();
				twitter.getProvider().retrieveAccessToken(consumer, verifier);
				TwitterAccount account = new TwitterAccount((AbstractOAuthConsumer) consumer);
				Twitter.getInstance().addAccount(account);
			} catch (Exception e) {
				Toast.makeText(this, getString(R.string.unableToAddAccount), Toast.LENGTH_SHORT).show();
			}
			startActivity(new Intent(this, SelectAccount.class));
		}
	}
}
