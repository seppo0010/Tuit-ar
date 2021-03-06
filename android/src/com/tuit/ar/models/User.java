package com.tuit.ar.models;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.database.Cursor;

import com.tuit.ar.api.Twitter;
import com.tuit.ar.api.TwitterAccount;
import com.tuit.ar.api.TwitterAccountRequestsObserver;
import com.tuit.ar.api.TwitterRequest;
import com.tuit.ar.api.request.Options;
import com.tuit.ar.databases.Model;

public class User extends Model implements TwitterAccountRequestsObserver {
	static private HashMap<Long, WeakReference<User>> cache = new HashMap<Long, WeakReference<User>>();

	private static final String[] columns = new String[]{
		"description", "followers_count", "following", "friends_count", "id", "location", "name", "profile_image_url", "is_protected", "screen_name", "statuses_count", "url", "verified", "belongs_to_user"
	};
	private JSONObject dataSourceJSON;

	private String description;
	private int followers_count;
	private boolean following;
	private int friends_count;
	private long id;
	private String location;
	private String name;
	private String profile_image_url;
	private boolean is_protected;
	private String screen_name;
	private int statuses_count;
	private String url;
	private boolean verified;
	private long belongs_to_user;

	private TwitterRequest followRequest;

	static public User get(long id) {
		return cache.containsKey(id) ? cache.get(id).get() : null;
	}

	public User(Cursor query) {
		super();
		setDescription(query.getString(0));
		setFollowersCount(query.getInt(1));
		setFollowing(query.getInt(2) == 1);
		setFriendsCount(query.getInt(3));
		setId(query.getLong(4));
		setLocation(query.getString(5));
		setName(query.getString(6));
		setProfileImageUrl(query.getString(7));
		setProtected(query.getInt(8) == 1);
		setScreenName(query.getString(9));
		setStatusesCount(query.getInt(10));
		setUrl(query.getString(11));
		setVerified(query.getInt(12) == 1);
		setBelongsToUser(query.getLong(13));
		cache.put(this.getId(), new WeakReference<User>(this));
	}

	public User(JSONObject object) {
		super();
		this.dataSourceJSON = object;
		cache.put(this.getId(), new WeakReference<User>(this));
	}

	public String getDescription() {
		if (description != null || dataSourceJSON == null) return description;
		try {
			setDescription(dataSourceJSON.isNull("description") ? null : dataSourceJSON.getString("description"));
			return description;
		} catch (JSONException e) {
			return null;
		}
	}

	public void setDescription(String description) {
		this.description = sanitize(description);
	}

	public int getFollowersCount() {
		if (followers_count != 0 || dataSourceJSON == null) return followers_count;
		try {
			return followers_count = dataSourceJSON.getInt("followers_count");
		} catch (JSONException e) {
			return 0;
		}
	}

	public void setFollowersCount(int followersCount) {
		followers_count = followersCount;
	}

	public boolean isFollowing() {
		if (following != false || dataSourceJSON == null) return following;
		try {
			return following = dataSourceJSON.getBoolean("following");
		} catch (JSONException e) {
			return false;
		}
	}

	public void setFollowing(boolean following) {
		this.following = following;
	}

	public int getFriendsCount() {
		if (friends_count != 0 || dataSourceJSON == null) return friends_count;
		try {
			return friends_count = dataSourceJSON.getInt("friends_count");
		} catch (JSONException e) {
			return 0;
		}
	}

	public void setFriendsCount(int friendsCount) {
		friends_count = friendsCount;
	}

	public long getId() {
		if (id != 0 || dataSourceJSON == null) return id;
		try {
			return id = dataSourceJSON.getLong("id");
		} catch (JSONException e) {
			return 0;
		}
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getLocation() {
		if (location != null || dataSourceJSON == null) return location;
		try {
			setLocation(dataSourceJSON.isNull("location") ? null : dataSourceJSON.getString("location"));
			return location;
		} catch (JSONException e) {
			return null;
		}
	}

	public void setLocation(String location) {
		this.location = sanitize(location);
	}

	public String getName() {
		if (name != null || dataSourceJSON == null) return name;
		try {
			setName(dataSourceJSON.isNull("name") ? null : dataSourceJSON.getString("name"));
			return name;
		} catch (JSONException e) {
			return null;
		}
	}

	public void setName(String name) {
		this.name = sanitize(name);
	}

	public String getProfileImageUrl() {
		if (profile_image_url != null || dataSourceJSON == null) return profile_image_url;
		try {
			setProfileImageUrl(dataSourceJSON.isNull("profile_image_url") ? null : dataSourceJSON.getString("profile_image_url"));
			return profile_image_url;
		} catch (JSONException e) {
			return null;
		}
	}

	public void setProfileImageUrl(String profileImageUrl) {
		profile_image_url = sanitize(profileImageUrl);
	}

	public boolean isProtected() {
		if (is_protected != false || dataSourceJSON == null) return is_protected;
		try {
			return is_protected = dataSourceJSON.getBoolean("protected");
		} catch (JSONException e) {
			return false;
		}
	}

	public void setProtected(boolean isProtected) {
		is_protected = isProtected;
	}

	public String getScreenName() {
		if (screen_name != null || dataSourceJSON == null) return screen_name;
		try {
			setScreenName(dataSourceJSON.isNull("screen_name") ? null : dataSourceJSON.getString("screen_name"));
			return screen_name;
		} catch (JSONException e) {
			return null;
		}
	}

	public void setScreenName(String screenName) {
		screen_name = sanitize(screenName);
	}

	public int getStatusesCount() {
		if (statuses_count != 0 || dataSourceJSON == null) return statuses_count;
		try {
			return statuses_count = dataSourceJSON.getInt("statuses_count");
		} catch (JSONException e) {
			return 0;
		}
	}

	public void setStatusesCount(int statusesCount) {
		statuses_count = statusesCount;
	}

	public String getUrl() {
		if (url != null || dataSourceJSON == null) return url;
		try {
			setUrl(dataSourceJSON.isNull("url") ? null : dataSourceJSON.getString("url"));
			return url;
		} catch (JSONException e) {
			return null;
		}
	}

	public void setUrl(String url) {
		this.url = sanitize(url);
	}

	public boolean isVerified() {
		if (verified != false || dataSourceJSON == null) return verified;
		try {
			return verified = dataSourceJSON.getBoolean("verified");
		} catch (JSONException e) {
			return false;
		}
	}

	public void setVerified(boolean verified) {
		this.verified = verified;
	}

	public long getBelongsToUser() {
		return belongs_to_user;
	}

	public void setBelongsToUser(long belongsToUser) {
		belongs_to_user = belongsToUser;
	}

	@SuppressWarnings("unchecked")
	static public ArrayList<User> select(String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit) {
		return (ArrayList<User>)Model.select(User.class, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
	}

	@Override
	protected ContentValues getValues() {
		ContentValues fields = new ContentValues();
		fields.put(columns[0], getDescription());
		fields.put(columns[1], getFollowersCount());
		fields.put(columns[2], isFollowing() ? 1 : 0);
		fields.put(columns[3], getFriendsCount());
		fields.put(columns[4], getId());
		fields.put(columns[5], getLocation());
		fields.put(columns[6], getName());
		fields.put(columns[7], getProfileImageUrl());
		fields.put(columns[8], isProtected() ? 1 : 0);
		fields.put(columns[9], getScreenName());
		fields.put(columns[10], getStatusesCount());
		fields.put(columns[11], getUrl());
		fields.put(columns[12], isVerified() ? 1 : 0);
		fields.put(columns[13], getBelongsToUser());
		return fields;
	}

	public void stopFollowing() throws Exception {
		TwitterAccount account = Twitter.getInstance().getDefaultAccount();
		account.addRequestObserver(this);
		ArrayList<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("user_id", String.valueOf(getId())));
		followRequest = account.requestUrl(Options.UNFOLLOW, nvps, TwitterRequest.METHOD_POST);
	}

	public void follow() throws Exception {
		TwitterAccount account = Twitter.getInstance().getDefaultAccount();
		account.addRequestObserver(this);
		ArrayList<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("user_id", String.valueOf(getId())));
		followRequest = account.requestUrl(Options.FOLLOW, nvps, TwitterRequest.METHOD_POST);
	}

	public void requestHasFinished(TwitterRequest request) {
		if (request == followRequest) {
			if (request.getUrl().equals(Options.FOLLOW)) {
				setFollowing(true);
			} else {
				setFollowing(false);
			}
			TwitterAccount account = Twitter.getInstance().getDefaultAccount();
			account.removeRequestObserver(this);
		}
	}

	public void requestHasStarted(TwitterRequest request) {
	}

	static public int deleteBelongsToUser(String user) {
		return Model.delete(User.class, "belongs_to_user = ?", new String[]{user});
	}
}
