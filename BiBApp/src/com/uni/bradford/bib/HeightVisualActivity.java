package com.uni.bradford.bib;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import android.widget.TextView;

public class HeightVisualActivity extends Activity
{
	// GUI
	private ImageView ivOwnChild;
	private ImageView ivCompareChild;
	private Spinner sSelectChild;
	private TextView tvOwnChildHeight;
	private TextView tvCompareChildHeight;
	private TextView tvCurrent;
	private TextView tvStartDate;
	private TextView tvEndDate;
	private SeekBar sbTimeLine;
	
	// Logic
	private int maxHeight;
	private int minHeight;
	
	private DataModel dataModel;
	
	@Override 
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_height_visual);	
			
		// Change ActionBar color and icon
		ActionBar bar = getActionBar();
		bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#0171bd")));
		bar.setIcon(R.drawable.ic_height_visual_white);
		
		// Init logic
		LoadDataModelFromFileAsyncTask loadLocalTask = new LoadDataModelFromFileAsyncTask();
		loadLocalTask.execute();
		
		// Connect to GUI views
		ivOwnChild = (ImageView)findViewById(R.id.ivOwnChild);
		ivCompareChild = (ImageView)findViewById(R.id.ivCompareChild);
		sSelectChild = (Spinner)findViewById(R.id.sSelectChild);
		tvOwnChildHeight = (TextView)findViewById(R.id.tvOwnChildHeight);
		tvCompareChildHeight = (TextView)findViewById(R.id.tvCompareChildHeight);
		tvCurrent = (TextView)findViewById(R.id.tvCurrent);
		tvStartDate = (TextView)findViewById(R.id.tvStartDate);
		tvEndDate = (TextView)findViewById(R.id.tvEndDate);
				
		// Init timeline
		sbTimeLine = (SeekBar)findViewById(R.id.sbTimeLine);
		sbTimeLine.setMax(100);
		
		// Init measurement borders
		maxHeight = ivOwnChild.getHeight();
		minHeight = ivCompareChild.getHeight();
		
		// Add listener
		ivOwnChild.getViewTreeObserver().addOnGlobalLayoutListener(new OnIvOwnChildGlobalLayoutListener() );
		ivCompareChild.getViewTreeObserver().addOnGlobalLayoutListener(new OnIvCompareChildGlobalLayoutListener() );
		sbTimeLine.setOnSeekBarChangeListener(new OnSeekBarTimeLineChangeListener());
		sSelectChild.setOnItemSelectedListener(new OnSpinnerSelectChildSelectedListener());
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
			// Idea: Use Android animation to "draw line" on StopTrackingTouch with imageView of a pen		
			
			// Dummy behaviour
			float onePercent = (maxHeight - minHeight) / (float)sbTimeLine.getMax();
			
			ivOwnChild.getLayoutParams().height = (int)(minHeight + progress*((float)onePercent/2));
			ivOwnChild.requestLayout();
			tvOwnChildHeight.setText(ivOwnChild.getLayoutParams().height + " " + getResources().getString(R.string.cm));
			
			ivCompareChild.getLayoutParams().height = (int)(minHeight + progress*(float)onePercent);
			ivCompareChild.requestLayout();
			tvCompareChildHeight.setText(ivCompareChild.getLayoutParams().height + " " + getResources().getString(R.string.cm));
						
			tvCurrent.setText(progress + " month");
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) { }

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) { }
	}
	
	private class OnSpinnerSelectChildSelectedListener implements OnItemSelectedListener
	{
		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
		{
			System.out.println("Selected child: " + position + " " + parent.getItemAtPosition(position).toString());
			
			// Adjust slider info
			if (dataModel.getMother().getChildCount() > 0)
			{
				tvStartDate.setText(dataModel.getMother().getChild(position).getYearOfBirth() + "");
				
				int yearsOld = (int)(dataModel.getMother().getChild(position).getLastChildData().getAgeDays() / 360);
				tvEndDate.setText(dataModel.getMother().getChild(position).getYearOfBirth() + yearsOld + "");
				
				// Init child representation
				ivOwnChild.setImageResource(R.drawable.own_child);
				ivCompareChild.setImageResource(R.drawable.average_child);
			}
			
			// TODO: Change dataset for visualisation
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) { }
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
		if (id == R.id.action_share_visual)
		{
			System.out.println("Clicked share");
			
			// Capture relevant view 
			View viewForCapture = findViewById(R.id.rlHeightVisual);
			
			// Workaround to get always the new screen capture
			viewForCapture.setDrawingCacheEnabled(false);
            viewForCapture.setDrawingCacheEnabled(true);
            
            // TODO: It might be possible to add info text INTO the picture (deal with Facebook)
            Bitmap bitmapOfCapture = viewForCapture.getDrawingCache();
		     
            // Setup intent for sharing
            Intent sendIntent = new Intent();
			sendIntent.setAction(Intent.ACTION_SEND);
			sendIntent.setType("image/jpeg");
			
			// Set text
			sendIntent.putExtra(Intent.EXTRA_TEXT, getResources().getText(R.string.share_mail_text));
			sendIntent.putExtra(Intent.EXTRA_SUBJECT, getResources().getText(R.string.share_mail_subject));
			sendIntent.putExtra(Intent.EXTRA_TITLE, getResources().getText(R.string.share_mail_title));
			
			// Convert
			ByteArrayOutputStream bytes = new ByteArrayOutputStream();
			bitmapOfCapture.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
			
			// Use temorary file for share intent
			File file = new File (Environment.getExternalStorageDirectory() + File.separator + "temporary_file.jpg");
			
			try 
			{
				// Save
				file.setWritable(true);
			    file.createNewFile();
			    FileOutputStream fo = new FileOutputStream(file);
			    fo.write(bytes.toByteArray());
			    fo.close();
			} 
			catch (IOException e) 
			{                       
			    e.printStackTrace();
			}
			
			// Append
			sendIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + Environment.getExternalStorageDirectory().getPath() + "/temporary_file.jpg"));
			
			startActivity(Intent.createChooser(sendIntent, "Share Infographic:"));
			
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
		
	private class OnIvOwnChildGlobalLayoutListener implements OnGlobalLayoutListener
	{		
		@Override
		public void onGlobalLayout()
		{			
			// Now the size of the image view is fix
			maxHeight = ivOwnChild.getHeight();
			
			// Get rid of listener
			ivOwnChild.getViewTreeObserver().removeOnGlobalLayoutListener(this);
		} 
	}
	
	private class OnIvCompareChildGlobalLayoutListener implements OnGlobalLayoutListener
	{		
		@Override
		public void onGlobalLayout()
		{			
			// Now the size of the image view is fix
			minHeight = ivCompareChild.getHeight();
			
			// Get rid of listener
			ivCompareChild.getViewTreeObserver().removeOnGlobalLayoutListener(this);
		} 
	}
	
	public void updateGui()
	{		
		// Init selection menu 
		String[] children = new String[dataModel.getMother().getChildCount()];
		for (int i = 0; i < dataModel.getMother().getChildCount(); i++)
		{
			children[i] = dataModel.getMother().getChild(i).getYearOfBirth() + 
					      "-" + dataModel.getMother().getChild(i).getMonthOfBirth();
		}
		
		ArrayAdapter<String> adapterChilds = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, children);
		adapterChilds.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sSelectChild.setAdapter(adapterChilds);   
				
		// Init slider info
		if (dataModel.getMother().getChildCount() > 0)
		{
			tvStartDate.setText(dataModel.getMother().getChild(0).getYearOfBirth() + "");
			
			int yearsOld = (int)(dataModel.getMother().getChild(0).getLastChildData().getAgeDays() / 360);
			tvEndDate.setText(dataModel.getMother().getChild(0).getYearOfBirth() + yearsOld + "");
			
			// Init child representation
			ivOwnChild.setImageResource(R.drawable.own_child);
		}
	}
	
	private class LoadDataModelFromFileAsyncTask extends AsyncTask<Void, Void, Void>
	{
		@Override
		protected Void doInBackground(Void... params)
		{		
			dataModel = DataModel.loadFromFile(HeightVisualActivity.this.getFilesDir());
						
			return null; 
		}
		
		@Override
		protected void onPostExecute(Void result)
		{	
			if (dataModel != null)
			{
				// Update GUI
				updateGui();
				
//				Toast toast = Toast.makeText(HeightVisualActivity.this, "..loaded from file", Toast.LENGTH_SHORT);
//				toast.show();
			}
		}
	}
}
