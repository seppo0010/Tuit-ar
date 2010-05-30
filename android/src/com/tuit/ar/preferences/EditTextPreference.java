package com.tuit.ar.preferences;

import android.content.Context;
import android.util.AttributeSet;

public class EditTextPreference extends android.preference.EditTextPreference {
	private DialogPreferenceListener dialogPreferenceListener = null;

	public EditTextPreference(Context context) {
		super(context);
	}

	public EditTextPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public EditTextPreference(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public DialogPreferenceListener getOnDialogPreferenceListener() {
		return dialogPreferenceListener;
	}

	public void setDialogPreferenceListener(DialogPreferenceListener listener) {
		this.dialogPreferenceListener = listener;
	}

	public void onClick(android.content.DialogInterface dialog, int which) {
		super.onClick(dialog, which);
		// FIXME: is -2 allright?
		if (dialogPreferenceListener != null) dialogPreferenceListener.onDialogClosed(-2 != which);
	}
}
