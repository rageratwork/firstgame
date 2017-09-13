package com.fourthwoods.firstgame;

/**
 * Copyright (c) 2013 David J. Rager
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 **/
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends Activity {
	private static int BASE_HEIGHT = 480;
	private AudioManager audioManager;
	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		int height;
		int width;

		if(Build.VERSION.SDK_INT < 13)
		{
			width = getWindowManager().getDefaultDisplay().getWidth();
			height = getWindowManager().getDefaultDisplay().getHeight();
		}
		else
		{
			Point size = new Point();
			getWindowManager().getDefaultDisplay().getSize(size);
			width = size.x;
			height = size.y;
		}
		
		// we are supposed to be in landscape so get the smaller value.
		// NOTE: this is here because the screen might start in portrait if the screen is off or locked.
		// I've only seen this happen during development launching via adb but just in case...
		height = height < width ? height : width;
		
		Settings.screen_height = height;
		
		double scale_factor = (double)height / (double)BASE_HEIGHT;
		Settings.scale_factor = scale_factor;
		
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		Typeface tf = Typeface.createFromAsset(getAssets(),"EraserRegular.ttf");

		TextView tv = (TextView) findViewById(R.id.baby);
		tv.setTypeface(tf);
		tv.setTextColor(Color.WHITE);
		tv = (TextView) findViewById(R.id.popit);
		tv.setTypeface(tf);
		tv.setTextColor(Color.WHITE);
		tv = (TextView) findViewById(R.id.findit);
		tv.setTypeface(tf);
		tv.setTextColor(Color.WHITE);
		tv = (TextView) findViewById(R.id.title);
		tv.setTypeface(tf);
		tv.setTextColor(Color.WHITE);
		tv = (TextView) findViewById(R.id.settings);
		tv.setTypeface(tf);
		tv.setTextColor(Color.WHITE);
		tv = (TextView) findViewById(R.id.exit);
		tv.setTypeface(tf);
		tv.setTextColor(Color.WHITE);
		tv = (TextView) findViewById(R.id.help);
		tv.setTypeface(tf);
		tv.setTextColor(Color.WHITE);
		tv = (TextView) findViewById(R.id.volume);
		tv.setTypeface(tf);
		tv.setTextColor(Color.WHITE);
		tv = (TextView) findViewById(R.id.volume);
		tv.setTypeface(tf);
		tv.setTextColor(Color.WHITE);
		tv = (TextView) findViewById(R.id.inside);
		tv.setTypeface(tf);
		tv.setTextColor(Color.WHITE);
		tv = (TextView) findViewById(R.id.outside);
		tv.setTypeface(tf);
		tv.setTextColor(Color.WHITE);
		
		audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
	    int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
	    int curVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);

	    SeekBar volControl = (SeekBar)findViewById(R.id.volumebar);
	    volControl.setMax(maxVolume);
	    volControl.setProgress(curVolume);
	    volControl.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
	        @Override
	        public void onStopTrackingTouch(SeekBar arg0) {
	        }

	        @Override
	        public void onStartTrackingTouch(SeekBar arg0) {
	        }

	        @Override
	        public void onProgressChanged(SeekBar arg0, int index, boolean arg2) {
	            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, index, 0);
	        }
	    });
	    
		Settings.reload(this);
		Sounds.reload(this);
	}

	@Override
	protected void onStart() {
	    int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
	    int curVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);

	    SeekBar volControl = (SeekBar)findViewById(R.id.volumebar);
	    volControl.setMax(maxVolume);
	    volControl.setProgress(curVolume);
	    
		super.onStart();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.action_settings:
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            break;
        }
 
        return true;
    }
	@Override
	public void onBackPressed() {
		doExit();
	}
	
	public void doSmash(View view) {
		Intent intent = new Intent(this, SmashActivity.class);
		startActivity(intent);
	}

	public void doSlash(View view) {
		Intent intent = new Intent(this, SlashActivity.class);
		startActivity(intent);
	}

	public void doFindit(View view) {
		Intent intent = new Intent(this, FinditActivity.class);
		startActivity(intent);
	}

	public void doSettings(View view) {
		Intent intent = new Intent(this, SettingsActivity.class);
		startActivity(intent);
	}
	
	public void doHelp(View view) {
		Intent intent = new Intent(this, HelpActivity.class);
		startActivity(intent);	
	}
	
	public void doExit(View view)
	{
		doExit();
	}

	private void doExit()
	{
		Settings.save(this);
		
		final Context context = getApplicationContext();
		MediaPlayer mp = Sounds.create(context, R.raw.kara_all_done);
		mp.start();

		AlertDialog alertDialog;
		String ok = getResources().getString(R.string.label_yes);
		String cancel = getResources().getString(R.string.label_no);
		alertDialog = new AlertDialog.Builder(this).setCancelable(false)
				.setPositiveButton(ok, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						MediaPlayer mp = Sounds.create(context, R.raw.kara_ok);
						mp.start();
						finish();
					}
				})
				.setNegativeButton(cancel, null).create();
    	String title = getResources().getString(R.string.dlg_confirm_title);
    	alertDialog.setTitle(title);
    	String msg = getResources().getString(R.string.dlg_confirm_exit);
    	alertDialog.setMessage(msg);
		alertDialog.show();		
	}
}
