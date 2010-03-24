package com.tuit.ar.api;

public interface TwitterObserver {
	public void requestHasFinished(TwitterRequest request);
	public void requestHasStarted(TwitterRequest request);
}
