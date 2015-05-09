package com.uni.bradford.bib.control;

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

import com.uni.bradford.bib.R;
import com.uni.bradford.bib.model.Child;
import com.uni.bradford.bib.model.DataModel;

/**
 * Class to deal with user interaction for showing child growth information
 * 
 * @author Martin
 */
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
	private double lowestHeight;
	private double highestHeight;
	
	private DataModel dataModel;
	
	@Override 
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_height_visual);	
			
		// Change ActionBar color
		ActionBar bar = getActionBar();
		bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor(getResources().getString(R.string.bib_blue))));
		
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
		
		// Init measurement borders
		maxHeight = ivOwnChild.getHeight();
		minHeight = ivCompareChild.getHeight();
		
		// Add listener
		ivOwnChild.getViewTreeObserver().addOnGlobalLayoutListener(new OnIvOwnChildGlobalLayoutListener() );
		ivCompareChild.getViewTreeObserver().addOnGlobalLayoutListener(new OnIvCompareChildGlobalLayoutListener() );
		sbTimeLine.setOnSeekBarChangeListener(new OnSeekBarTimeLineChangeListener());
		sSelectChild.setOnItemSelectedListener(new OnSpinnerSelectChildSelectedListener());
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
            
            // It might be possible to add info text INTO the picture (deal with Facebook)
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
			
			// Use temporary file for share intent
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
			
			startActivity(Intent.createChooser(sendIntent, getResources().getString(R.string.share_menu_text)));
			
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	/**
	 * Change GUI according to data model
	 */
	public void updateGui()
	{	
		// Init relative height
		lowestHeight = dataModel.getMother().getChild(0).getChildData(0).getHeight();
		highestHeight = dataModel.getMother().getChild(0).getLastChildData().getHeight();
		
		// Init slider description
		tvCurrent.setText(constructRelativeAge(dataModel.getMother().getChild(0).getChildData(0).getAgeDays()));
		
		// Create array for all children of a mother for spinner
		String[] children = new String[dataModel.getMother().getChildCount()];
		for (int i = 0; i < dataModel.getMother().getChildCount(); i++)
		{
			children[i] = dataModel.getMother().getChild(i).getIdentifier();
		}
		
		// Link spinner and children array
		ArrayAdapter<String> adapterChilds = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, children);
		adapterChilds.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sSelectChild.setAdapter(adapterChilds);   
				
		// Init slider info
		if (dataModel.getMother().getChildCount() > 0)
		{
			tvStartDate.setText(dataModel.getMother().getChild(0).getYearOfBirth() + "");
			
			int yearsOld = (int)(dataModel.getMother().getChild(0).getLastChildData().getAgeDays() / DataModel.averageYear);
			tvEndDate.setText(dataModel.getMother().getChild(0).getYearOfBirth() + yearsOld + "");
			
			tvCurrent.setText( sbTimeLine.getProgress() + " month");
			
			// Init child representation
			ivOwnChild.setImageResource(R.drawable.own_child);
		}
	}
	
	/**
	 * Calculate the relative age for a given amount of days
	 */
	public String constructRelativeAge(int ageDays)
	{
		// Relative rough age of child with little use of space
		String relativeAgeString = "";
		
		// Unlikely
		if (ageDays == 0)
		{
			return "0" + getResources().getString(R.string.day_shurtcut);
		}

		// Use average year
		if ( (ageDays/DataModel.averageYear) >= 1)
		{
			// Add amount of years to relative age
			relativeAgeString = (ageDays/DataModel.averageYear) + getResources().getString(R.string.year_shurtcut);
		}

		// Use average month
		if ( ((ageDays % DataModel.averageYear)/DataModel.averageMonth) >= 1)
		{
			if (relativeAgeString.length() > 0)
			{
				relativeAgeString += " ";
			}

			// Add amount of month to relative age 
			relativeAgeString += ((ageDays % DataModel.averageYear)/DataModel.averageMonth) + getResources().getString(R.string.month_shurtcut);
		}

		if ( ((ageDays % DataModel.averageYear) % DataModel.averageMonth) >= 1 )
		{
			if (relativeAgeString.length() > 0)
			{
				relativeAgeString += " ";
			}

			// Add amount of days to relative age 
			relativeAgeString += ((ageDays % DataModel.averageYear) % DataModel.averageMonth) + getResources().getString(R.string.day_shurtcut);
		}

		return relativeAgeString;
	}
	
	/**
	 * Class to obtain maximal height of the child representation in pixel
	 */
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
	
	/**
	 * Class to obtain minimal height of the child representation in pixel
	 */
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
	
	/**
	 * Class to change initial visualisation after child selection
	 */
	private class OnSpinnerSelectChildSelectedListener implements OnItemSelectedListener
	{
		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
		{
			System.out.println("Selected child: " + position + " " + parent.getItemAtPosition(position).toString());
			
			// Adjust slider info
			if (dataModel.getMother().getChildCount() > 0)
			{
				// Visualise first year with growth data
				tvStartDate.setText(dataModel.getMother().getChild(position).getYearOfBirth() + "");
				
				// Calculate and visualise last year with growth data
				int yearsOld = (int)(dataModel.getMother().getChild(position).getLastChildData().getAgeDays() / DataModel.averageYear);
				tvEndDate.setText(dataModel.getMother().getChild(position).getYearOfBirth() + yearsOld + "");
				
				// Init child representation
				ivOwnChild.setImageResource(R.drawable.own_child);
				ivCompareChild.setImageResource(R.drawable.average_child);
				
				sbTimeLine.setMax(dataModel.getMother().getChild(position).getChildDataAmount()-1);
				
				lowestHeight = dataModel.getMother().getChild(position).getChildData(0).getHeight();
				highestHeight = dataModel.getMother().getChild(position).getLastChildData().getHeight();
			}
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) { }
	}
	
	private class OnSeekBarTimeLineChangeListener implements OnSeekBarChangeListener
	{
		@Override
		public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
		{
			// Idea: Visualisation of time with sandglass, timelapse of season/earth fading bg or rotation, calender leaves fall off 
			// Idea: On the right side a group of children (shortest, tallest, average).. and animation to "draw line" over head with pen		
			
			Child selectedChild = dataModel.getMother().getChild(sSelectChild.getSelectedItemPosition());
			
			// Convert child's height in cm to pixel height for the image representation  
			double percentageQuotationCm = selectedChild.getChildData(progress).getHeight() - lowestHeight;
			double percentageRate = (percentageQuotationCm * 100) / (highestHeight - lowestHeight);
			double percentageQuotationPixel = ((maxHeight - minHeight) * percentageRate) / (double)100;
			
			// TODO: Only for debug
			System.out.println("lowestHeight:             " + lowestHeight);
			System.out.println("highestHeight:            " + highestHeight);
			System.out.println("percentageQuotationCm:    " + percentageQuotationCm);
			System.out.println("percentageRate:           " + percentageRate);
			System.out.println("percentageQuotationPixel: " + percentageQuotationPixel);
			
			// Use convertet height to adjust child image
			ivOwnChild.getLayoutParams().height = (int)(minHeight + percentageQuotationPixel);
			ivOwnChild.requestLayout();
			tvOwnChildHeight.setText(selectedChild.getChildData(progress).getHeight() + " " + getResources().getString(R.string.cm));
			
			// Dummy behaviour
			float onePercentForPixel = (maxHeight - minHeight) / (float)100;
			
			// TODO: Only until Dan Mason provides the new dataset with national average
			ivCompareChild.getLayoutParams().height = (int)(minHeight + progress*(float)onePercentForPixel);
			ivCompareChild.requestLayout();
			tvCompareChildHeight.setText(ivCompareChild.getLayoutParams().height + " " + getResources().getString(R.string.cm));
			
			// Update text representation for slider progress
			tvCurrent.setText(constructRelativeAge(selectedChild.getChildData(progress).getAgeDays()));
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) { }

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) { }
	}
	
	/**
	 * Class to load data model from local file
	 */
	private class LoadDataModelFromFileAsyncTask extends AsyncTask<Void, Void, Void>
	{
		@Override
		protected Void doInBackground(Void... params)
		{	
			// Load data model from file
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
				
				// Toast toast = Toast.makeText(HeightVisualActivity.this, "..loaded from file", Toast.LENGTH_SHORT);
				// toast.show();
			}
		}
	}
}
