package com.tuit.ar.models;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;

import com.tuit.ar.api.Twitter;
import com.tuit.ar.api.TwitterAccount;
import com.tuit.ar.api.TwitterAccountRequestsObserver;
import com.tuit.ar.api.TwitterRequest;
import com.tuit.ar.api.request.Options;
import com.tuit.ar.databases.Model;

public class DirectMessage extends ListElement implements TwitterAccountRequestsObserver {
	private static final String[] columns = new String[]{
		"id", "sender_id", "date", "text", "belongs_to_user"
	};

	private JSONObject dataSourceJSON;
	private User sender = null;
	private Date createDate = null;
	private long dateMillis = 0;
	private String text = null;
	private long belongs_to_user;
	private long id;

	protected TwitterRequest deleteRequest;

	public DirectMessage(Cursor query) {
		super();
		setId(query.getLong(0));
		setSenderId(query.getLong(1));
		setDateMillis(query.getLong(2) * 1000);
		setText(query.getString(3));
		setBelongsToUser(query.getLong(4));
	}

	protected void setSenderId(long sender_id) {
		ArrayList<User> searchUser = User.select("id = ?", new String[] { String.valueOf(sender_id) }, null, null, null, "1");
		if (searchUser.size() > 0) sender = searchUser.get(0);
	}

	public DirectMessage(JSONObject jsonObject) {
		super();
		dataSourceJSON = jsonObject;
	}

	public User getSender() {
		if (sender != null) return sender;
		try {
			return sender = new User(dataSourceJSON.getJSONObject("sender"));
		} catch (JSONException e) {
			return null;
		}
	}

	public void setSender(User sender) {
		this.sender = sender;
	}

	public long getId() {
		if (id != 0) return id;
		try {
			return id = dataSourceJSON.getLong("id");
		} catch (JSONException e) {
			return 0;
		}
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getText() {
		if (text != null) return text;
		try {
			setText(dataSourceJSON.getString("text"));
			return text;
		} catch (Exception e) {
			return "";
		}
	}

	public void setText(String text) {
		this.text = sanitize(text);
	}

	public long getBelongsToUser() {
		return belongs_to_user;
	}

	public void setBelongsToUser(long belongsToUser) {
		belongs_to_user = belongsToUser;
	}

	public Date getDate() {
		try {
			if (createDate != null) return createDate;
			SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM d hh:mm:ss Z yyyy");
			return createDate = sdf.parse(dataSourceJSON.getString("created_at"));
		} catch (Exception e) {
			return null;
		}
	}

	public void setDateMillis(long date) {
		dateMillis = date;
	}

	public long getDateMillis() {
		if (dateMillis == 0) {
			Date date = this.getDate();
			if (date == null) return (long)0;
			dateMillis = date.getTime();
		}
		return dateMillis;
	}

	@Override
	protected ContentValues getValues() {
		ContentValues fields = new ContentValues();
		fields.put(columns[0], getId());
		fields.put(columns[1], getSender().getId());
		fields.put(columns[2], getDateMillis() / 1000);
		fields.put(columns[3], getText());
		fields.put(columns[4], getBelongsToUser());
		return fields;
	}

	@Override
	public String getUsername() {
		return getSender().getScreenName();
	}

	@Override
	public String getDisplayDate() {
		return calculateElapsed(getDateMillis());
	}

	@SuppressWarnings("unchecked")
	static public ArrayList<DirectMessage> select(String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit) {
		return (ArrayList<DirectMessage>)Model.select(DirectMessage.class, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
	}

	public void deleteFromServer() throws Exception {
		TwitterAccount account = Twitter.getInstance().getDefaultAccount();
		account.addRequestObserver(this);
		ArrayList<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("id", String.valueOf(getId())));
		deleteRequest = account.requestUrl(Options.DELETE_DIRECT_MESSAGE, nvps, TwitterRequest.METHOD_POST);
		com.tuit.ar.models.timeline.DirectMessages timeline = com.tuit.ar.models.timeline.DirectMessages.getInstance(Twitter.getInstance().getDefaultAccount());
		timeline.startedUpdate();
	}

	public void requestHasFinished(TwitterRequest request) {
		if (request == deleteRequest) {
			com.tuit.ar.models.timeline.DirectMessages timeline = com.tuit.ar.models.timeline.DirectMessages.getInstance(Twitter.getInstance().getDefaultAccount());
			timeline.deleteTweet(this);
			timeline.finishedUpdate();
			this.delete();
			TwitterAccount account = Twitter.getInstance().getDefaultAccount();
			account.removeRequestObserver(this);
		}
	}

	public int delete() {
		return db.delete("directmessage", "id = ? AND belongs_to_user = ?", new String[] {String.valueOf(getId()), String.valueOf(getBelongsToUser())});
	}

	public void requestHasStarted(TwitterRequest request) {
	}

	public long replace() {
		try {
			User user = getSender();
			user.setBelongsToUser(getBelongsToUser());
			user.replace();
		} catch (SQLiteException e) {}

		return super.replace();
	}

	@Override
	public String getAvatarUrl() {
		try {
			return getSender().getProfileImageUrl();
		} catch (Exception e) {
			return null;
		}
	}
}
