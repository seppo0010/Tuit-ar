package com.tuit.ar.api;

public interface TwitterAccountRequestsObserver {
	public void requestHasFinished(TwitterRequest request);
	public void requestHasStarted(TwitterRequest request);
}
