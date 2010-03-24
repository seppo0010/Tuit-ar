package com.tuit.ar.models.timeline;

import com.tuit.ar.models.Timeline;

public interface TimelineObserver {
	public void timelineHasChanged(Timeline timeline);
	public void timelineUpdateHasFailed(Timeline timeline);
}
