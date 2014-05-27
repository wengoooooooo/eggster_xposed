/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package areeb.xposed.eggster.ics;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;
import areeb.xposed.eggster.R;


import com.nineoldandroids.view.ViewHelper;

@SuppressLint("ShowToast")
public class PlatLogoActivity extends Activity {
	
	
	Toast mToast;
	ImageView mContent;
	Vibrator mZzz;
	int mCount;
	final Handler mHandler = new Handler();
	
	
	Runnable mSuperLongPress = new Runnable() {
		
		
		@TargetApi(Build.VERSION_CODES.HONEYCOMB)
		@SuppressLint("InlinedApi")
		public void run() {
			mCount++;
			mZzz.vibrate(50 * mCount);
			final float scale = 1f + 0.25f * mCount * mCount;
			ViewHelper.setScaleX(mContent, scale);
			ViewHelper.setScaleY(mContent, scale);
			//mContent.setScaleX(scale);
			//mContent.setScaleY(scale);
			if (mCount <= 3) {
				mHandler.postDelayed(mSuperLongPress,
						ViewConfiguration.getLongPressTimeout());
			} else {
				try {
					startActivity(new Intent(Intent.ACTION_MAIN)
							.setFlags(
									Intent.FLAG_ACTIVITY_NEW_TASK)
											.setClassName("areeb.xposed.eggster","areeb.xposed.eggster.ics.Nyandroid"));
				} catch (ActivityNotFoundException ex) {
					android.util.Log.e("PlatLogoActivity",
							"Couldn't find platlogo screensaver.");
				}
				finish();
			}
		}
	};

	@SuppressLint("ShowToast")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		mZzz = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
		
		SharedPreferences pref = getSharedPreferences("preferenceggs", Context.MODE_PRIVATE);
        String icsToast = pref.getString("ics_toast_text", getString(R.string.pref_default_ics_text));
		
		mToast = Toast.makeText(this, icsToast,
				Toast.LENGTH_SHORT);
		mContent = new ImageView(this);
		mContent.setImageResource(R.drawable.platlogoics);
		mContent.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
		mContent.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				final int action = event.getAction();
				if (action == MotionEvent.ACTION_DOWN) {
					mContent.setPressed(true);
					mHandler.removeCallbacks(mSuperLongPress);
					mCount = 0;
					mHandler.postDelayed(mSuperLongPress,
							2 * ViewConfiguration.getLongPressTimeout());
				} else if (action == MotionEvent.ACTION_UP) {
					if (mContent.isPressed()) {
						mContent.setPressed(false);
						mHandler.removeCallbacks(mSuperLongPress);
						mToast.show();
					}
				}
				return true;
			}
		});
		setContentView(mContent);
		
	}
}