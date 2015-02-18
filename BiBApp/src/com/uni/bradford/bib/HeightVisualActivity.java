package com.uni.bradford.bib;


import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class HeightVisualActivity extends Activity
{
	// GUI
	private ImageView ivOwnChildMiddleScale;
	private ImageView ivOwnChildBottomScale;
	private ImageView ivAverageChildMiddleScale;
	private ImageView ivAverageChildBottomScale;
		
	private SeekBar sbTimeLine;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_height_visual);	
		
		// Change ActionBar color
		ActionBar bar = getActionBar();
		bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#0171bd")));
		
		// Connect to GUI views
		ivOwnChildMiddleScale = (ImageView)findViewById(R.id.ivOwnChildMiddleScale);
		ivOwnChildBottomScale = (ImageView)findViewById(R.id.ivOwnChildBottomScale);
		ivAverageChildMiddleScale = (ImageView)findViewById(R.id.ivAverageChildMiddleScale);
		ivAverageChildBottomScale = (ImageView)findViewById(R.id.ivAverageChildBottomScale);
		
		sbTimeLine = (SeekBar)findViewById(R.id.sbTimeLine);
		sbTimeLine.setMax(100);
		
		// Add listener
		sbTimeLine.setOnSeekBarChangeListener(new OnSeekBarTimeLineChangeListener());
	}
	
	private class OnSeekBarTimeLineChangeListener implements OnSeekBarChangeListener
	{
		@Override
		public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
		{
			// TODO:
			// Idea: Visualisation of time with sandglass
			// Idea: Visualisation of time with timelapse of season/earth images fading in the background or rotation
			// Idea: Visualisation of time with calender leaves which fall off (fading out) in the background
			// Idea: On the right side a group of children (shortest, tallest, average).. 
					
			// Dummy behaviour
			ivOwnChildMiddleScale.getLayoutParams().height= (int)(sbTimeLine.getProgress( )*(float)1.1);
			ivOwnChildMiddleScale.requestLayout();
			ivOwnChildBottomScale.getLayoutParams().height= (int)(sbTimeLine.getProgress( )*(float)1.3);
			ivOwnChildBottomScale.requestLayout();
			
			ivAverageChildMiddleScale.getLayoutParams().height= (int)(sbTimeLine.getProgress( )*(float)1.5);
			ivAverageChildMiddleScale.requestLayout();
			ivAverageChildBottomScale.getLayoutParams().height= (int)(sbTimeLine.getProgress( )*(float)1.8);
			ivAverageChildBottomScale.requestLayout();
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) { }

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) { }
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.height_visual, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_share)
		{
			System.out.println("Clicked share");
			
			// TODO: Just to get going
			Intent sendIntent = new Intent();
			sendIntent.setAction(Intent.ACTION_SEND);
			sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
			sendIntent.setType("text/plain");
			startActivity(sendIntent);
			
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
