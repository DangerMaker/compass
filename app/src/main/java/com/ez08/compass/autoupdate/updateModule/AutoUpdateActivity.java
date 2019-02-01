package com.ez08.compass.autoupdate.updateModule;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;

import com.ez08.compass.R;

public class AutoUpdateActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_update);

	}

	@Override
	protected void onResume() {
		super.onResume();
		AutoUpdateModule.setUpdateActivity(this);
		Intent intent = getIntent();
		final AutoUpdatePacket up = (AutoUpdatePacket) intent
				.getSerializableExtra("up");
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				AutoUpdateManager.getInstance().checkUpdate(up);
			}
		});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			this.finish();
		}
		return super.onKeyDown(keyCode, event);
	}

}