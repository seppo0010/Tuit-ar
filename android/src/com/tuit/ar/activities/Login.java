package com.tuit.ar.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TextView.OnEditorActionListener;

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

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
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
        Twitter.getInstance().addObserver(this);
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