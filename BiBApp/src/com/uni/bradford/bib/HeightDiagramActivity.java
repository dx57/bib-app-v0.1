package com.uni.bradford.bib;

import java.util.ArrayList;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MarkerView;

public class HeightDiagramActivity extends Activity
{
	// GUI
	private LineChart lcHeight;
	private Spinner sDiagramSelectChild;
	private Spinner sDiagramSelectCriterion;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState); 
		setContentView(R.layout.activity_height_diagram);
		
		// TODO: Receive diagram content by intent parameters
		
		// Change ActionBar color and icon
		ActionBar bar = getActionBar(); 
		bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#0171bd")));
		bar.setIcon(R.drawable.ic_launcher); 
		
		// Connect to GUI views and setup		
		lcHeight = (LineChart)findViewById(R.id.lcHeight);
		sDiagramSelectChild = (Spinner)findViewById(R.id.sDiagramSelectChild);
		sDiagramSelectCriterion = (Spinner)findViewById(R.id.sDiagramSelectCriterion);
				
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
		
        //lcHeight.getChartBitmap(); // TODO: Use for share option
                
        
        // TODO: Just basic approach to get going.. not full functional
		setData(19);
				
        // TODO: Just basic approach to get going.. not full functional
		String[] childs = new String[] {"Child 2007", "Child 2009", "Child 2010"};
		ArrayAdapter<String> adapterChilds = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, childs);
		adapterChilds.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sDiagramSelectChild.setAdapter(adapterChilds);
		String[] criterion = new String[] {"Average", "Tallest", "Smallest"};
		ArrayAdapter<String> adapterCriterion = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, criterion);
		adapterCriterion.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sDiagramSelectCriterion.setAdapter(adapterCriterion);
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
		if (id == R.id.action_settings)
		{
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	// TODO: Only to get started
	private void setData(int count) 
	{
        ArrayList<String> xVals = new ArrayList<String>();
        for (int i = 0; i < count; i++) 
        {
        	xVals.add((i) + " yr");
        }
        
        ArrayList<Entry> yVals1 = new ArrayList<Entry>();

        yVals1.add(new Entry((float) 50, 0));
        yVals1.add(new Entry((float) 70, 1));
        yVals1.add(new Entry((float) 80, 2));
        yVals1.add(new Entry((float) 88, 3));
        yVals1.add(new Entry((float) 96, 4));
        yVals1.add(new Entry((float) 100, 5));
        yVals1.add(new Entry((float) 106, 6));
        yVals1.add(new Entry((float) 112, 7));
        yVals1.add(new Entry((float) 119, 8));
        yVals1.add(new Entry((float) 126, 9));
        yVals1.add(new Entry((float) 130, 10));
        yVals1.add(new Entry((float) 136, 11));
        yVals1.add(new Entry((float) 141, 12));
        yVals1.add(new Entry((float) 145, 13));
        yVals1.add(new Entry((float) 150, 14));
        yVals1.add(new Entry((float) 153, 15));
        yVals1.add(new Entry((float) 155, 16));
        yVals1.add(new Entry((float) 156, 17));
        yVals1.add(new Entry((float) 156, 18));
        
        
        ArrayList<Entry> yVals2 = new ArrayList<Entry>();

        yVals2.add(new Entry((float) 50+10, 0));
        yVals2.add(new Entry((float) 70+12, 1));
        yVals2.add(new Entry((float) 80+8, 2));
        yVals2.add(new Entry((float) 88+6, 3));
        yVals2.add(new Entry((float) 96+7, 4));
        yVals2.add(new Entry((float) 100+8, 5));
        yVals2.add(new Entry((float) 106+7, 6));
        yVals2.add(new Entry((float) 112+5, 7));
        yVals2.add(new Entry((float) 119+5, 8));
        yVals2.add(new Entry((float) 126+4, 9));
        yVals2.add(new Entry((float) 130+6, 10));
        yVals2.add(new Entry((float) 136+6, 11));
        yVals2.add(new Entry((float) 141+7, 12));
        yVals2.add(new Entry((float) 145+5, 13));
        yVals2.add(new Entry((float) 150+4, 14));
        yVals2.add(new Entry((float) 153+4, 15));
        yVals2.add(new Entry((float) 155+4, 16));
        yVals2.add(new Entry((float) 156+4, 17));
        yVals2.add(new Entry((float) 156+4, 18));

        
        // create a dataset and give it a type
        LineDataSet set1 = new LineDataSet(yVals2, "Average");
        set1.setColor(Color.parseColor("#0171bd"));
        set1.setCircleColor(Color.parseColor("#0171bd"));
        set1.setLineWidth(2f);
        set1.setCircleSize(4f);
        set1.setFillAlpha(65);
        set1.setFillColor(ColorTemplate.getHoloBlue());
        set1.setHighLightColor(Color.rgb(244, 117, 117));
        set1.setDrawFilled(true);
        set1.setDrawCircles(false);

        // create a dataset and give it a type
        LineDataSet set2 = new LineDataSet(yVals1, "Child 2007");
        set2.setColor(Color.parseColor("#009933"));
        set2.setCircleColor(Color.parseColor("#009933"));
        set2.setLineWidth(4f);
        set2.setCircleSize(6f);
        set2.setFillAlpha(65);
        set2.setHighLightColor(Color.rgb(244, 117, 117));
        set2.setDrawCubic(true);
        
        ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();
        dataSets.add(set1);
        dataSets.add(set2);
        
        // create a data object with the datasets
        LineData data = new LineData(xVals, dataSets);
        
        // set data
        lcHeight.setData(data);
        
        // Add listener
        sDiagramSelectChild.setOnItemSelectedListener(new OnSpinnerDiagramSelectChildSelectedListener());
        sDiagramSelectCriterion.setOnItemSelectedListener(new OnSpinnerDiagramSelectCriterionSelectedListener());
    }
	
	private class OnSpinnerDiagramSelectChildSelectedListener implements OnItemSelectedListener
	{
		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
		{
			// TODO: Add behaviour
			System.out.println("Selected child: " + position);
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent)
		{
			// TODO: Select the first		
		}
	}
	
	private class OnSpinnerDiagramSelectCriterionSelectedListener implements OnItemSelectedListener
	{
		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
		{
			// TODO: Add behaviour
			System.out.println("Selected criteria: " + position);
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent)
		{
			// TODO: Select the first		
		}
	}
}
