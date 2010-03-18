package com.tuit.ar.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.tuit.ar.R;
import com.tuit.ar.api.Twitter;
import com.tuit.ar.api.TwitterObserver;
import com.tuit.ar.api.TwitterRequest;
import com.tuit.ar.api.request.Options;

public class Login extends Activity implements TwitterObserver {
	private EditText username;
	private EditText password;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);

        Button login = (Button) findViewById(R.id.login);
        login.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) { login(); }
		});

        Twitter.getInstance().addObserver(this);
    }

    private void login() {
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
		Toast.makeText(this.getApplicationContext(), this.getString(R.string.unableToVerifyCredentials), Toast.LENGTH_LONG).show();
    }

	@Override
	public void requestHasFinished(TwitterRequest request) {
		switch (request.getUrl()) {
		case LOGIN:
			if (request.getStatusCode() < 400) Toast.makeText(this.getApplicationContext(), "Welcome", Toast.LENGTH_LONG).show();
			else loginFailed();
			break;

		default:
			break;
		}
	}

	@Override
    public void onDestroy() {
        Twitter.getInstance().removeObserver(this);
	}
}