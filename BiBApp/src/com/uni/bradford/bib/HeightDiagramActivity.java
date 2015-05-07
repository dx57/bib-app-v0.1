package com.uni.bradford.bib;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MarkerView;

/**
 * Class to deal with user interaction for showing child growth information
 * 
 * @author Martin
 */
public class HeightDiagramActivity extends Activity
{
	// GUI
	private LineChart lcHeight;
	private Spinner sDiagramSelectChild;
	
	// Logic
	private DataModel dataModel;
	
	public static final int averageYear = 360;
	public static final int averageMonth = 30;

	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState); 
		setContentView(R.layout.activity_height_diagram);

		// Change ActionBar color
		ActionBar bar = getActionBar(); 
		bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#0171bd")));
		
		// Init logic
		LoadDataModelFromFileAsyncTask loadLocalTask = new LoadDataModelFromFileAsyncTask();
		loadLocalTask.execute();

		// Connect to GUI views and setup		
		lcHeight = (LineChart)findViewById(R.id.lcHeight);
		sDiagramSelectChild = (Spinner)findViewById(R.id.sDiagramSelectChild);

		// Do animate diagram build process within 1 sec
		lcHeight.animateY(1000);

		// Set y axis unit at cm
		lcHeight.setUnit(getResources().getString(R.string.cm));

		// Do not allow cross hairs highlight
		lcHeight.setHighlightEnabled(false);

		// Do not draw y values above circles
		lcHeight.setDrawYValues(false);

		// Do not need description
		lcHeight.setDescription("");

		// Do not start with 0 for y axis
		lcHeight.setStartAtZero(false);

		// create a custom MarkerView (extend MarkerView) and specify the layout to use for it
		MarkerView mvMarker = new DiagramMarkerView(this, R.layout.diagram_marker_view);

		// define an offset to change the original position of the marker
		mvMarker.setOffsets(-mvMarker.getMeasuredWidth() / 2, -mvMarker.getMeasuredHeight());

		// set the marker to the chart
		lcHeight.setMarkerView(mvMarker);
		
		// Add listener
		sDiagramSelectChild.setOnItemSelectedListener(new OnSpinnerSelectChildSelectedListener());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.height_diagram, menu);
		return true; 
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_share_diagram)
		{
			System.out.println("Clicked share");
						            
            // Get diagram as picture
            Bitmap bitmapOfCapture = lcHeight.getChartBitmap(); // Possible to add info text INTO picture (deal with FB)
		     
            // Setup intent for sharing
            Intent sendIntent = new Intent();
			sendIntent.setAction(Intent.ACTION_SEND);
			sendIntent.setType("image/jpeg");
			
			// Set text
			sendIntent.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.share_mail_text));
			sendIntent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.share_mail_subject));
			sendIntent.putExtra(Intent.EXTRA_TITLE, getResources().getString(R.string.share_mail_title));
			
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
			
			startActivity(Intent.createChooser(sendIntent, "Share Infographic:"));
			
			// Consume event
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	/**
	 * Change GUI according to data model
	 */
	public void updateGui()
	{
		// Create array for all children of a mother for spinner
		String[] children = new String[dataModel.getMother().getChildCount()];
		for (int i = 0; i < dataModel.getMother().getChildCount(); i++)
		{
			// Add children as YYYY-M format string.
			children[i] = dataModel.getMother().getChild(i).getIdentifier();
		}
		
		// Link spinner and children array
		ArrayAdapter<String> adapterChilds = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, children);
		adapterChilds.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sDiagramSelectChild.setAdapter(adapterChilds);
	}

	private void showGrowthDataForChild(int position)
	{
		// Reset diagram view
		lcHeight.clear();
		
		// Get selected child
		Child selectedChild = dataModel.getMother().getChild(position);
		
		// Construct x axis labels (x axis only based on interger values with String representation.. this is already the workaround!
		ArrayList<String> xVals = new ArrayList<String>();
		for (int i = 0; i < selectedChild.getLastChildData().getAgeDays()+10; i++) 
		{
			// Relativ rough age of child with little use of space
			String xLabelString = "";
			
			// Use average year
			if ( (i/averageYear) >= 1)
			{
				xLabelString = (i/averageYear) + getResources().getString(R.string.year_shurtcut);
			}
			
			// Use average month
			if ( ((i % averageYear)/averageMonth) >= 1)
			{
				if (xLabelString.length() > 0)
				{
					xLabelString += " ";
				}
				
				xLabelString += ((i % averageYear)/averageMonth) + getResources().getString(R.string.month_shurtcut);
			}
			
			if ( ((i % averageYear) % averageMonth) >= 1 )
			{
				if (xLabelString.length() > 0)
				{
					xLabelString += " ";
				}
				
				xLabelString += ((i % averageYear) % averageMonth) + getResources().getString(R.string.day_shurtcut);
			}
						
			xVals.add(xLabelString);
		}
		
		
		// Take all data of child for diagram
		ArrayList<Entry> yValuesOwnChild = new ArrayList<Entry>();
		for (int i = 0; i < selectedChild.getChildDataAmount(); i++)
		{
			// TODO: only debug
			System.out.println(selectedChild.getChildData(i).getHeight().floatValue() + " " + selectedChild.getChildData(i).getAgeDays());
			
			yValuesOwnChild.add(new Entry(selectedChild.getChildData(i).getHeight().floatValue(), selectedChild.getChildData(i).getAgeDays()));
		}
		
		// TODO: Only local approach until data gets from WebService
		ArrayList<Entry> yValsAverage = new ArrayList<Entry>();
		yValsAverage.add(new Entry((float) 50, averageYear*0));
		yValsAverage.add(new Entry((float) 70, averageYear*1));
		yValsAverage.add(new Entry((float) 80, averageYear*2));
		yValsAverage.add(new Entry((float) 88, averageYear*3));
		yValsAverage.add(new Entry((float) 96, averageYear*4));
		yValsAverage.add(new Entry((float) 100, averageYear*5));
		yValsAverage.add(new Entry((float) 106, averageYear*6));
		yValsAverage.add(new Entry((float) 112, averageYear*7));
		yValsAverage.add(new Entry((float) 119, averageYear*8));
		yValsAverage.add(new Entry((float) 126, averageYear*9));
		yValsAverage.add(new Entry((float) 130, averageYear*10));
		yValsAverage.add(new Entry((float) 136, averageYear*11));
		yValsAverage.add(new Entry((float) 141, averageYear*12));
		yValsAverage.add(new Entry((float) 145, averageYear*13));
		yValsAverage.add(new Entry((float) 150, averageYear*14));
		yValsAverage.add(new Entry((float) 153, averageYear*15));
		yValsAverage.add(new Entry((float) 155, averageYear*16));
		yValsAverage.add(new Entry((float) 156, averageYear*17));
		yValsAverage.add(new Entry((float) 156, averageYear*18));
		
		// Create a dataset for own child data
		LineDataSet ownChildDataSet = new LineDataSet(yValuesOwnChild, getResources().getString(R.string.own_child));
		ownChildDataSet.setColor(Color.parseColor("#0171bd"));
		ownChildDataSet.setCircleColor(Color.parseColor("#0171bd"));
		ownChildDataSet.setLineWidth(4f);
		ownChildDataSet.setCircleSize(8f);
		ownChildDataSet.setFillAlpha(65);
		ownChildDataSet.setFillColor(ColorTemplate.getHoloBlue());
		ownChildDataSet.setHighLightColor(Color.rgb(244, 117, 117));
		// ownChildDataSet.setDrawFilled(true);
		ownChildDataSet.setDrawCircles(true);
		ownChildDataSet.setDrawCubic(true);
		
		// create a dataset and give it a type
		LineDataSet averageDataSet = new LineDataSet(yValsAverage, getResources().getString(R.string.national_average));
		averageDataSet.setColor(Color.parseColor("#009933"));
		averageDataSet.setDrawCircles(false);
		averageDataSet.setLineWidth(4f);
		averageDataSet.setFillAlpha(65);
		averageDataSet.setHighLightColor(Color.rgb(244, 117, 117));
		averageDataSet.setDrawCubic(true);
		
		// Add datasets for line chart
		ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();
		dataSets.add(ownChildDataSet);
		dataSets.add(averageDataSet);

		// Create a data object with the datasets
		LineData data = new LineData(xVals, dataSets);

		// Set data
		lcHeight.setData(data);
		
		// Label marker is now not percise anymore, because there are so many elements on the x axis
	}
	
	/**
	 * Class to react on child selection
	 */
	private class OnSpinnerSelectChildSelectedListener implements OnItemSelectedListener
	{
		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
		{
			System.out.println("Selected child: " + position + " " + parent.getItemAtPosition(position).toString());
			
			showGrowthDataForChild(position);
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) { }
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
			dataModel = DataModel.loadFromFile(HeightDiagramActivity.this.getFilesDir());
						
			return null; 
		}
		
		@Override
		protected void onPostExecute(Void result)
		{	
			if (dataModel != null)
			{
				// Update GUI
				updateGui();
				
				// Toast toast = Toast.makeText(HeightDiagramActivity.this, "..loaded from file", Toast.LENGTH_SHORT);
				// toast.show();
			}
		}
	}
}
