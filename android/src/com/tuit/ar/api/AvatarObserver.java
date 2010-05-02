package com.tuit.ar.api;

public interface AvatarObserver {
	public void avatarHasFinished(Avatar avatar);
	public void avatarHasFailed(Avatar avatar);
}
