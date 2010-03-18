package com.tuit.ar.activities;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.tuit.ar.R;
import com.tuit.ar.api.Twitter;
import com.tuit.ar.api.TwitterObserver;
import com.tuit.ar.api.TwitterRequest;
import com.tuit.ar.api.request.Options;
import com.tuit.ar.models.Tweet;

abstract public class Timeline extends ListActivity implements TwitterObserver {
	ArrayList<Tweet> tweets = new ArrayList<Tweet>();
	TimelineAdapter timelineAdapter; 
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timeline);
        
		this.setListAdapter(timelineAdapter = new TimelineAdapter(this));

		Twitter twitter = Twitter.getInstance();
        twitter.addObserver(this);
        try {
			twitter.requestUrl(this.getTimeline());
		} catch (Exception e) {
			failedToUpdate();
		}
    }

	public void requestHasFinished(TwitterRequest request) {
		try {
			JSONArray tweets = new JSONArray(new JSONTokener(request.getResponse()));
			int c = tweets.length();
			for (int i = 0; i < c; i++) {
				this.tweets.add(new Tweet(tweets.getJSONObject(i)));
			}
			timelineAdapter.notifyDataSetChanged();
		} catch (JSONException e) {
			failedToUpdate();
		}
	}

	protected void failedToUpdate() {
		Toast.makeText(this, "Unable to fetch your timeline", Toast.LENGTH_SHORT);
    }

    abstract protected Options getTimeline();

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
			element.getDate().setText("");

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
