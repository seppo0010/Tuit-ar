package com.tuit.ar.activities;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.tuit.ar.R;
import com.tuit.ar.models.Tweet;
import com.tuit.ar.models.timeline.TimelineObserver;
import com.tuit.ar.services.Updater;

abstract public class Timeline extends ListActivity implements TimelineObserver {
	ArrayList<Tweet> tweets = new ArrayList<Tweet>();
	TimelineAdapter timelineAdapter;
	protected boolean isVisible;
	protected String newestTweet = "";

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.timeline);

		this.setListAdapter(timelineAdapter = new TimelineAdapter(this));

		getTimeline().addObserver(this);
		getTimeline().refresh();
		this.startService(new Intent(this, Updater.class));
	}

	abstract protected com.tuit.ar.models.Timeline getTimeline();

	protected void refresh() {
		getTimeline().refresh();
	}

    protected void onResume() {
    	super.onResume();
    	isVisible = true;
    	timelineAdapter.notifyDataSetChanged();
    }

    protected void onPause() {
    	super.onPause();
    	isVisible = false;
    }

	public void timelineHasChanged(com.tuit.ar.models.Timeline timeline) {
		if (tweets.size() == 0)
			tweets.addAll(getTimeline().getTweets());
		else
			tweets.addAll(0, getTimeline().getTweetsNewerThan(tweets.get(0)));
		if (isVisible)
			timelineAdapter.notifyDataSetChanged();
	}

	public void timelineUpdateHasFailed(com.tuit.ar.models.Timeline timeline) {
		Toast.makeText(this, "Unable to fetch your timeline", Toast.LENGTH_SHORT);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		getTimeline().removeObserver(this);
	}

	protected class TimelineAdapter extends ArrayAdapter<Tweet> 
	{
		Activity context;
		HashMap<View, TimelineElement> elements = new HashMap<View, TimelineElement>();

		public TimelineAdapter(Activity context)
		{
			super(context, R.layout.timeline_element, tweets);
			this.context = context;
		}

		public View getView(int position, View convertView, ViewGroup parent)
		{
			TimelineElement element = getTimelineElement(convertView);

			Tweet tweet = tweets.get(position);
			element.getUsername().setText(tweet.getUsername());
			element.getMessage().setText(tweet.getMessage());
			element.getDate().setText(tweet.getDate().toString());

			return element.getView();
		}

		private TimelineElement getTimelineElement(View convertView) {
			if (convertView == null)
			{
				convertView = View.inflate(this.context, R.layout.timeline_element, null);
			}

			if (!elements.containsKey(convertView)) {
				elements.put(convertView, new TimelineElement(convertView));
			}
			return elements.get(convertView);
		}
	}

	protected class TimelineElement {
		private View view;
		private TextView username;
		private TextView message;
		private TextView date;

		public TimelineElement(View view) {
			super();
			this.view = view;
		}

		public TextView getUsername() {
			if (username != null) return username;
			else return username = (TextView)view.findViewById(R.id.username);
		}

		public TextView getMessage() {
			if (message != null) return message;
			else return message = (TextView)view.findViewById(R.id.message);
		}

		public TextView getDate() {
			if (date != null) return date;
			else return date = (TextView)view.findViewById(R.id.date);
		}

		public View getView() { return view; }
	}
}
