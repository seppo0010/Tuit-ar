package com.tuit.ar.activities;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.tuit.ar.R;
import com.tuit.ar.api.Twitter;
import com.tuit.ar.api.TwitterAccountRequestsObserver;
import com.tuit.ar.api.TwitterRequest;
import com.tuit.ar.api.request.Options;

public class NewDirectMessage extends Activity implements OnClickListener, TwitterAccountRequestsObserver {
	static private final int MAX_CHARS = 140;

	private String toUser;
	private EditText messageField;
	private TextView charCount;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.new_direct_message);
		Button send = (Button)findViewById(R.id.send);
		send.setOnClickListener(this);
		Twitter.getInstance().getDefaultAccount().addRequestObserver(this);

		Intent intent = getIntent();
		toUser = intent.getStringExtra("to_user");
		if (toUser != null) {
			TextView username = (TextView)findViewById(R.id.toUser);
			username.setText("To @" + toUser);
		}

		messageField = (EditText) findViewById(R.id.tweetMessage);
		charCount = (TextView) findViewById(R.id.charCount);

		messageField.setOnKeyListener(new OnKeyListener() {
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				updateCharCount();
				return false;
			}
		});
	}

	private void updateCharCount() {
		charCount.setText(String.valueOf(MAX_CHARS - messageField.getText().toString().length()));
	}

	public void onClick(View v) {
		if (toUser == null) {
			// FIXME: improve error message?
			sendFailed();
			return;
		}

		String message = messageField.getText().toString();

		if (message == null || message.length() == 0) {
			// FIXME: improve error message?
			sendFailed();
			return;
		}

		try {
			ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("text", message));
			params.add(new BasicNameValuePair("screen_name", toUser));
			TwitterRequest.Method method = TwitterRequest.Method.POST;
			Twitter.getInstance().getDefaultAccount().requestUrl(Options.SEND_DIRECT_MESSAGE, params, method);
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
    	if (request.getUrl().equals(Options.SEND_DIRECT_MESSAGE)) {
    		if (request.getStatusCode() >= 200 && request.getStatusCode() < 400) {
    			Toast.makeText(this, getString(R.string.messageSent), Toast.LENGTH_SHORT).show();
    			finish();
    		}
    		else sendFailed();
    	}
	}

	public void onDestroy() {
		super.onDestroy();
		Twitter.getInstance().getDefaultAccount().removeRequestObserver(this);
	}
}
