package com.tweetphoto.api;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.FileEntity;
import org.apache.http.impl.client.DefaultHttpClient;

//FIXME: replacing TweetPhoto library because of inconsistency with the response

public class ConcreteTweetPhoto extends TweetPhoto {
	public Profile signIn(String apiKey, String serviceName, Boolean isOAuth, String userIdentity, String userSecret) {
		return super.signIn(apiKey, serviceName, isOAuth, userIdentity, userSecret);
	}

	public ConcreteTweetPhotoResponse concreteUploadPhoto(FileEntity photo, String comment, String tags, double latitude, double longitude, String mimeType) {
		String urlToRequest 			= "http://tweetphotoapi.com/api/tpapi.svc/upload2";
		DefaultHttpClient httpClient 	= new DefaultHttpClient();
		httpClient.addRequestInterceptor(preemptiveAuth, 0);
		httpClient.getCredentialsProvider().setCredentials(new AuthScope("tweetphotoapi.com", 80, AuthScope.ANY_REALM), new UsernamePasswordCredentials(m_tokenIdentifier, m_tokenSecret)); 
		HttpPost httpPost 				= new HttpPost(urlToRequest);

		httpPost.addHeader("TPSERVICE", m_ServiceName);
		httpPost.addHeader("TPISOAUTH", m_isOAuth ? "True" : "False");
		httpPost.addHeader("TPSERVICE", m_ServiceName);
		httpPost.addHeader("TPAPIKEY",  m_APIKey);
		httpPost.addHeader("TPPOST",    "True");
		httpPost.addHeader("TPMIMETYPE", mimeType);
		httpPost.addHeader("TPUTF8",    "True");
		httpPost.addHeader("TPMSG",     Base64Coder.encodeString(comment));

		if (latitude!=0.0 && longitude!=0.0) {
			httpPost.addHeader("TPLAT", "" +latitude);
			httpPost.addHeader("TPLONG", "" + longitude);
		}

		if (tags!=null && tags.length()>0) {
			httpPost.addHeader("TPTAGS", Base64Coder.encodeString(tags));
		}

		try {	        
			httpPost.setEntity(photo);

			HttpResponse httpResp = httpClient.execute(httpPost);  

			m_httpStatus = httpResp.getStatusLine().getStatusCode();

			if (m_httpStatus != 201) {
				ByteArrayOutputStream ostream = new ByteArrayOutputStream();
				httpResp.getEntity().writeTo(ostream);
//				Log.e(TAG, "Upload Photo Error: (" + m_httpStatus + ") Data: " + ostream.toString());
			} else {
				InputStream content = httpResp.getEntity().getContent();
				String xml = convertStreamToString(content);
				ConcreteDomTweetPhotoResponseParser responseParser = new ConcreteDomTweetPhotoResponseParser();
				responseParser.m_Xml = xml;
				ConcreteTweetPhotoResponse response = (ConcreteTweetPhotoResponse)responseParser.parse();				
				content.close(); 
				return response;
			}

		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}

		return null;
	}
}
