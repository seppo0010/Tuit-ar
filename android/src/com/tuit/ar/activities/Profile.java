package com.tuit.ar.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tuit.ar.R;
import com.tuit.ar.api.Avatar;
import com.tuit.ar.api.AvatarObserver;
import com.tuit.ar.models.User;

public class Profile extends Activity implements AvatarObserver {
	private ImageView avatar;
	private TextView nickname;
	static private User userToDisplay = null;

	static public void setUserToDisplay(User user) {
		userToDisplay = user;
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.profile);
		avatar = (ImageView)findViewById(R.id.avatar);
		avatar.setVisibility(View.INVISIBLE);
		nickname = (TextView)findViewById(R.id.nickname);
		if (userToDisplay != null) {
			Avatar avatar = new Avatar(userToDisplay.getProfileImageUrl());
			avatar.addRequestObserver(this);
			avatar.download();
			
			nickname.setText(userToDisplay.getScreenName());
			userToDisplay = null;
		}
	}

	@Override
	public void avatarHasFailed(Avatar avatar) {
		this.avatar.setVisibility(View.INVISIBLE);
	}

	@Override
	public void avatarHasFinished(Avatar avatar) {
		this.avatar.setImageBitmap(avatar.getResponse());
		this.avatar.setVisibility(View.VISIBLE);
	}

}
