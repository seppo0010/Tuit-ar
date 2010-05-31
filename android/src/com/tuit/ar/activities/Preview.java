package com.tuit.ar.activities;

import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.ContentValues;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore.Images.Media;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.SurfaceHolder.Callback;

import com.tuit.ar.R;
public class Preview extends Activity implements Callback {
	private Camera camera;
	private boolean isPreviewRunning = false;
	private SimpleDateFormat timeStampFormat = new SimpleDateFormat("yyyyMMddHHmmssSS");
	private SurfaceView surfaceView;
	private SurfaceHolder surfaceHolder;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFormat(PixelFormat.TRANSLUCENT);
		setContentView(R.layout.preview);
		surfaceView = (SurfaceView)findViewById(R.id.surface);
		surfaceHolder = surfaceView.getHolder();
		surfaceHolder.addCallback(this);
		surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	}

	Camera.PictureCallback mPictureCallbackRaw = new Camera.PictureCallback() {
		public void onPictureTaken(byte[] data, Camera c) {
			camera.startPreview();
		}
	};


	Camera.ShutterCallback mShutterCallback = new Camera.ShutterCallback() {
		public void onShutter() {
			finish();
		}
	};


	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		ImageCaptureCallback camDemo = null;
		if(keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
			try {
				String filename = timeStampFormat.format(new Date());
				ContentValues values = new ContentValues();
				values.put(Media.TITLE, filename);
				values.put(Media.DESCRIPTION, "Image from Android Camera");
				Uri uri = getContentResolver().insert(Media.EXTERNAL_CONTENT_URI, values);
				camDemo = new ImageCaptureCallback( getContentResolver().openOutputStream(uri));
			} catch(Exception ex ){
			}
		}
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
			return super.onKeyDown(keyCode, event);

		}

		if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
			camera.takePicture(mShutterCallback, mPictureCallbackRaw, camDemo);
			return true;
		}


		return false;
	}

	protected void onResume()
	{
		Log.e(getClass().getSimpleName(), "onResume");
		super.onResume();
	}

	protected void onSaveInstanceState(Bundle outState)
	{
		super.onSaveInstanceState(outState);
	}

	protected void onStop()
	{
		super.onStop();
	}   

	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		if (isPreviewRunning) {
			camera.stopPreview();
		}
		Camera.Parameters p = camera.getParameters();
		p.setPreviewSize(width, height);
		camera.setParameters(p);
		try {
			camera.setPreviewDisplay(holder);
		} catch (IOException e) {
			e.printStackTrace();
		}
		camera.startPreview();
		isPreviewRunning = true;

	}

	public void surfaceCreated(SurfaceHolder holder) {
		camera = Camera.open();

	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		camera.stopPreview();
		isPreviewRunning = false;
		camera.release();

	}

	protected void onRestoreInstanceState(Bundle savedInstanceState)
	{
		super.onRestoreInstanceState(savedInstanceState);
	}

	private class ImageCaptureCallback implements PictureCallback  {

		private OutputStream filoutputStream;

		public ImageCaptureCallback(OutputStream filoutputStream) {
			this.filoutputStream = filoutputStream;
		}

		public void onPictureTaken(byte[] data, Camera camera) {
			try {
				filoutputStream.write(data);
				filoutputStream.flush();
				filoutputStream.close();
			} catch(Exception ex) {
				ex.printStackTrace();
			}
		}
	}
}