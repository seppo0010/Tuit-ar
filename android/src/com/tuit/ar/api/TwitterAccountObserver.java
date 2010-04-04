package com.tuit.ar.api;

public interface TwitterAccountObserver {
	public void requestHasFinished(TwitterRequest request);
	public void requestHasStarted(TwitterRequest request);
}
