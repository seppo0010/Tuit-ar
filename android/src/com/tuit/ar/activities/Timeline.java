package com.tuit.ar.activities;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.tuit.ar.R;
import com.tuit.ar.models.ListElement;
import com.tuit.ar.models.Status;
import com.tuit.ar.models.User;
import com.tuit.ar.models.timeline.TimelineObserver;
import com.tuit.ar.services.Updater;

abstract public class Timeline extends ListActivity implements TimelineObserver {
	protected static final int MENU_NEW_TWEET = 0;   
	protected static final int MENU_REFRESH = 1;   
	protected static final int MENU_FRIENDS = 2;
	protected static final int MENU_REPLIES = 3;
	protected static final int MENU_DIRECT = 4;
	protected static final int MENU_PREFERENCES = 5;
	
	protected TimelineAdapter<? extends ListElement> timelineAdapter;
	protected boolean isVisible;
	protected long newestTweet = 0;

	abstract protected com.tuit.ar.models.Timeline getTimeline();

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.timeline);

		getTimeline().addObserver(this);
		getTimeline().refresh();
		this.startService(new Intent(this, Updater.class));
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

	@Override
	public void onDestroy() {
		super.onDestroy();
		getTimeline().removeObserver(this);
	}

	protected void showProfile(User user) {
		Profile.setUserToDisplay(user);
		startActivity(new Intent(getApplicationContext(), Profile.class));
	}

	protected void openLinksInBrowser(Status tweet) {
		final String[] urls = parseUrls(tweet.getMessage());
		if (urls.length == 0) {
			Toast.makeText(this, getString(R.string.noURLFound), Toast.LENGTH_SHORT).show();
		} else if (urls.length == 1) {
			this.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(urls[0].toString())));
		} else { // we have 2+ urls
			new AlertDialog.Builder(this).
			setTitle(getString(R.string.selectURL)).
			setItems(urls,
					new OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(urls[which])));
						}
			}).show();
		}
	}

	static protected String[] parseUrls(String message) {
        String [] parts = message.split("\\s");

        ArrayList<String> foundURLs = new ArrayList<String>();
        for( String item : parts ) try {
            foundURLs.add((new URL(item)).toString());
        } catch (MalformedURLException e) {
        }

        return (String[])foundURLs.toArray(new String[foundURLs.size()]);
	}

	protected void refresh() {
		getTimeline().refresh();
	}

	public void timelineRequestStarted(com.tuit.ar.models.Timeline timeline) {
		this.setProgressBarIndeterminateVisibility(true);
	}
	public void timelineRequestFinished(com.tuit.ar.models.Timeline timeline) {
		this.setProgressBarIndeterminateVisibility(false);
	}

	public void timelineUpdateHasFailed(com.tuit.ar.models.Timeline timeline) {
		if (isVisible) {
			Toast.makeText(this, getString(R.string.unableToFetchTimeline), Toast.LENGTH_SHORT).show();
		}
	}

	protected class TimelineAdapter<T> extends ArrayAdapter<T> 
	{
		protected Activity context;
		protected HashMap<View, TimelineElement> elements = new HashMap<View, TimelineElement>();
		protected ArrayList<T> tweets;

		public TimelineAdapter(Activity context, ArrayList<T> tweets)
		{
			super(context, R.layout.timeline_element, tweets);
			this.context = context;
			this.tweets = tweets;
		}

		public View getView(int position, View convertView, ViewGroup parent)
		{
			TimelineElement element = getTimelineElement(convertView);

			ListElement tweet = (ListElement) tweets.get(position);
			if (element.currentTweet == tweet) return element.getView();

			element.getUsername().setText("@" + tweet.getUsername());
			element.getMessage().setText(tweet.getText());
			element.getDate().setText(tweet.getDisplayDate());
			element.currentTweet = tweet;

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
		public ListElement currentTweet; 

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
