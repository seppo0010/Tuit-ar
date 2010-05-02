package com.tuit.ar.api;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Avatar extends Request {
	private String url;
	Bitmap response = null;

	private ArrayList<AvatarObserver> observers = new ArrayList<AvatarObserver>(); 

	public Avatar(String url) {
		this.url = url;
	}

	public void addRequestObserver(AvatarObserver observer) {
		observers.add(observer);
	}

	public void removeRequestObserver(AvatarObserver observer) {
		observers.remove(observer);
	}

	public void download() {
		(new Thread() {
			public void run() {
				DefaultHttpClient http = new DefaultHttpClient();
				HttpRequestBase request = new HttpGet(url);
				final HttpParams params = new BasicHttpParams();
				HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
				HttpProtocolParams.setContentCharset(params, "UTF_8");
				HttpProtocolParams.setUseExpectContinue(params, false);
				http.setParams(params);	

				try {
					HttpResponse response = http.execute(request);
					HttpEntity resEntity = response.getEntity();
					int statusCode = (response.getStatusLine().getStatusCode());

					if (resEntity != null && statusCode >= 200 && statusCode < 300)
					{
						ByteArrayOutputStream output = new ByteArrayOutputStream();
						resEntity.writeTo(output);
						byte[] bytes = output.toByteArray();
						setResponse(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
					}
				} catch (Exception e) {

				}
				handler.post(runnable);
			}
		}).start();
	}

	protected void setResponse(Bitmap response) {
		this.response = response;		
	}

	public Bitmap getResponse() {
		return this.response;		
	}

	@Override
	protected void finishedRequest() {
		if (response == null) {
			requestFailed();
			return;
		}
		for (AvatarObserver observer : observers) {
			observer.avatarHasFinished(this);
		}
	}

	protected void requestFailed() {
		for (AvatarObserver observer : observers) {
			observer.avatarHasFailed(this);
		}
	}
}
