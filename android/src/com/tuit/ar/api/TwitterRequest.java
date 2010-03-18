package com.tuit.ar.api;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.Handler;

import com.tuit.ar.api.request.Options;


public class TwitterRequest {
	private Runnable runnable = new Runnable() {
		@Override
		public void run() {
			finishedRequest();
		}
	};
	private Handler handler = new Handler();
	private Options url = null;
	private int statusCode;
	private String response;

	public Options getUrl() { return url; }
	public void setUrl(Options url) { this.url = url; } 
	public int getStatusCode() { return statusCode; }
	public void setStatusCode(int statusCode) { this.statusCode = statusCode; }
	public String getResponse() { return response; }
	public void setResponse(String response) { this.response = response; }

	private void finishedRequest() {
		Twitter.getInstance().finishedRequest(this);
	}

	TwitterRequest(final Options url) throws Exception {
		setUrl(url);
		final StringEntity entity = new StringEntity("");
		(new Thread() {
			public void run() {
				DefaultHttpClient http = new DefaultHttpClient();

				HttpPost post = new HttpPost("http" + (Twitter.isSecure ? "s" :"") + "://" + Twitter.BASE_URL + url.toString() + ".json");
				post.setEntity(entity);

				String username = Twitter.getInstance().getUsername();
				String password = Twitter.getInstance().getPassword();
				if (username != null && password != null) {
					BasicCredentialsProvider credentials = new BasicCredentialsProvider();
					credentials.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(username, password));
					http.setCredentialsProvider(credentials);
				}

				try {
					HttpResponse response = http.execute(post);
					HttpEntity resEntity = response.getEntity();
					setStatusCode(response.getStatusLine().getStatusCode());
					
					//if (resEntity != null && statusCode >= 200 && statusCode < 300)
					{
						ByteArrayOutputStream output = new ByteArrayOutputStream();
						resEntity.writeTo(output);
						byte[] bytes = output.toByteArray();
						setResponse(new String(bytes));
					}
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				handler.post(runnable);
			}
		}).start();
	}
}
