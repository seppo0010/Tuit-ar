package com.tuit.ar.api;

import android.os.Handler;

public abstract class Request {
	protected Runnable runnable = new Runnable() {
		public void run() {
			finishedRequest();
		}
	};
	protected Handler handler = new Handler();

	abstract protected void finishedRequest();
}
