package com.tuit.ar.api;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

import android.os.Handler;

import com.tuit.ar.api.request.Options;
import com.tuit.ar.api.request.UniqueRequestException;


public class TwitterRequest extends Request {
	static private int BUFFER_SIZE = 1024;
	static public final int METHOD_GET = 0;
	static public final int METHOD_POST = 1;

	protected Runnable runnable = new Runnable() {
		public void run() {
			finishedRequest();
		}
	};
	protected Handler handler = new Handler();

	private Options url = null;
	private int statusCode;
	private String response;
	private final TwitterAccount account;
	private Exception communicationException = null;

	public Options getUrl() { return url; }
	public void setUrl(Options url) { this.url = url; } 
	public int getStatusCode() { return statusCode; }
	public void setStatusCode(int statusCode) { this.statusCode = statusCode; }
	public String getResponse() { return response; }
	public void setResponse(String response) { this.response = response; }

	protected void finishedRequest() {
		account.finishedRequest(this);
	}

	public TwitterRequest(TwitterAccount _account, final Options url, final ArrayList <NameValuePair> nvps,
			final int method) throws Exception {
		// this avoid having 2 request of the same kind at a time
		if (url.mustBeUnique() && _account.hasUrlOnRequest(url)) throw new UniqueRequestException();
		account = _account;
		setUrl(url);
		run(url, nvps, method);
	}

	protected void run(final Options url, final ArrayList <NameValuePair> nvps, final int method) {
		(new Thread() {
			public void run() {
				account.addUrlOnRequest(url);
				DefaultHttpClient http = new DefaultHttpClient();

				String full_url = "http" + (Twitter.isSecure ? "s" :"") + "://" + Twitter.BASE_URL + url.toString() + ".json";
				HttpRequestBase request;
				if (method == TwitterRequest.METHOD_POST) {
					request = new HttpPost(full_url);
					try {
						((HttpPost) request).setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
				}
				else {
					String queryString = "";
					if (nvps != null && nvps.size() > 0) {
						try {
							UrlEncodedFormEntity entities = new UrlEncodedFormEntity(nvps, HTTP.UTF_8);
							ByteArrayOutputStream output = new ByteArrayOutputStream();
							InputStream input = entities.getContent();
							int read = 0;
							byte[] buffer = new byte[BUFFER_SIZE];
							while ((read = input.read(buffer, 0, BUFFER_SIZE)) != -1) {
								output.write(buffer, 0, read);
							}
							queryString = "?" + new String(output.toByteArray());
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					request = new HttpGet(full_url + queryString);
				}

				final HttpParams params = new BasicHttpParams();
				HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
				HttpProtocolParams.setContentCharset(params, "UTF_8");
				HttpProtocolParams.setUseExpectContinue(params, false);
				http.setParams(params);	

				try {
					account.getConsumer().sign(request);
					HttpResponse response = http.execute(request);
					HttpEntity resEntity = response.getEntity();
					setStatusCode(response.getStatusLine().getStatusCode());

					//if (resEntity != null && statusCode >= 200 && statusCode < 300)
					{
						ByteArrayOutputStream output = new ByteArrayOutputStream();
						resEntity.writeTo(output);
						byte[] bytes = output.toByteArray();
						setResponse(new String(bytes));
					}
				} catch (Exception e) {
					e.printStackTrace();
					communicationException = e;
				}
				handler.post(runnable);
				account.removeUrlOnRequest(url);
			}
		}).start();
	}

	public String getErrorMessage() {
		return communicationException.getMessage();
	}
}
