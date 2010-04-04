package com.tuit.ar.activities;

import oauth.signpost.OAuthProvider;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthProvider;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.exception.OAuthNotAuthorizedException;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.tuit.ar.R;
import com.tuit.ar.activities.timeline.Friends;
import com.tuit.ar.api.Twitter;
import com.tuit.ar.api.TwitterObserver;
import com.tuit.ar.api.TwitterRequest;
import com.tuit.ar.api.request.Options;

public class Login extends Activity implements TwitterObserver {
	private EditText username;
	private EditText password;
	private Button login;
	private static String CONSUMER_KEY = "wLR28XpgOKbmpaFFvtMVA";
	private static String CONSUMER_SECRET = "WOMQjlVaeuTs0eVQfTqXRxfLD58gbGCQrZdDIrQUSU";
	private static String CALLBACK_URL = "tuitar://login";
	private static OAuthProvider provider;
	private static CommonsHttpOAuthConsumer consumer;


	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		Uri uri = this.getIntent().getData();  
		if (uri != null && uri.toString().startsWith(CALLBACK_URL)) {  
		    String verifier = uri.getQueryParameter("oauth_verifier");  
		    // this will populate token and token_secret in consumer  
		    try {
				provider.retrieveAccessToken(consumer, verifier);
			    String a = consumer.getToken();
			    String b = consumer.getTokenSecret();
			    Log.d("token", a + " - " + b);
			} catch (OAuthMessageSignerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (OAuthNotAuthorizedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (OAuthExpectationFailedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (OAuthCommunicationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return;
		}  
        consumer = new CommonsHttpOAuthConsumer(  
                CONSUMER_KEY, CONSUMER_SECRET);  
          
        provider = new CommonsHttpOAuthProvider("http://twitter.com/oauth/request_token", "http://twitter.com/oauth/access_token",  
                "http://twitter.com/oauth/authorize");
        String authUrl;
		try {
			authUrl = provider.retrieveRequestToken(consumer, CALLBACK_URL);
	        this.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(authUrl)));  
		} catch (OAuthMessageSignerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OAuthNotAuthorizedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OAuthExpectationFailedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OAuthCommunicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  

        /*        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.main);

        setTitle(getString(R.string.login));

		username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);

        login = (Button) findViewById(R.id.login);
        login.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) { login(); }
		});

        password.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				login();
				return true;
			}
		});
        Twitter.getInstance().addObserver(this);*/
    }

    private void login() {
    	username.setEnabled(false);
    	password.setEnabled(false);
    	login.setEnabled(false);
        Twitter twitter = Twitter.getInstance();
        twitter.setUsername(username.getText().toString());
        twitter.setPassword(password.getText().toString());
        try {
        	twitter.requestUrl(Options.LOGIN);
		} catch (Exception e) {
			loginFailed();
		}
    }
    private void loginFailed() {
        Twitter twitter = Twitter.getInstance();
        twitter.clearCredentials();
    	username.setEnabled(true);
    	password.setEnabled(true);
    	login.setEnabled(true);
		Toast.makeText(this.getApplicationContext(), this.getString(R.string.unableToVerifyCredentials), Toast.LENGTH_LONG).show();
    }

	public void requestHasStarted(TwitterRequest request) {
		this.setProgressBarIndeterminateVisibility(true);
	}

    @Override
    public void requestHasFinished(TwitterRequest request) {
		this.setProgressBarIndeterminateVisibility(false);
    	if (request.getUrl().equals(Options.LOGIN)) {
    		if (request.getStatusCode() >= 200 && request.getStatusCode() < 400) {
    			Intent intent = new Intent(this.getApplicationContext(), Friends.class);
    			this.startActivity(intent);		
    		}
    		else loginFailed();
    	}
    }

	@Override
    public void onDestroy() {
		super.onDestroy();
        Twitter.getInstance().removeObserver(this);
	}
}