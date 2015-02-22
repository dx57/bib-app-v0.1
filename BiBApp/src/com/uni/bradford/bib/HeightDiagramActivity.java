package com.uni.bradford.bib;

import java.util.ArrayList;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class HeightDiagramActivity extends Activity
{
	private LineChart lcHeight;
	private Spinner sDiagramSelectChild;
	private Spinner sDiagramSelectCriterion;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState); 
		setContentView(R.layout.activity_height_diagram);
		
		// Change ActionBar color and icon
		ActionBar bar = getActionBar(); 
		bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#0171bd")));
		bar.setIcon(R.drawable.ic_launcher); 
		
		// Connect to GUI views and setup		
		lcHeight = (LineChart)findViewById(R.id.lcHeight);
		lcHeight.animateY(1000);
//		lcHeight.setDragOffsetX(100);
//		lcHeight.setDrawUnitsInChart(true);
		lcHeight.setOffsets(10, 10, 10, 10);
//		lcHeight.setPadding(100, 100, 100, 100);
		lcHeight.setUnit("cm");
		setData(19, 100);
		//lcHeight.getChartBitmap(); // TODO: Use for share option
		lcHeight.setDrawBorder(false);
		lcHeight.setHighlightEnabled(false);
		lcHeight.invalidate();
		
		sDiagramSelectChild = (Spinner)findViewById(R.id.sDiagramSelectChild);
		sDiagramSelectCriterion = (Spinner)findViewById(R.id.sDiagramSelectCriterion);
		
		// TODO: Just basic approach to get going.. not full functional
		String[] childs = new String[] {"Child 2007", "Child 2009", "Child 2010"};
		ArrayAdapter<String> adapterChilds = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, childs);
		adapterChilds.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sDiagramSelectChild.setAdapter(adapterChilds);
		String[] criterion = new String[] {"Average", "Tallest", "Smallest"};
		ArrayAdapter<String> adapterCriterion = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, criterion);
		adapterCriterion.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sDiagramSelectCriterion.setAdapter(adapterCriterion);
		
		
		// it might be possible to use the same approach for the visual height
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
	private void setData(int count, float range) 
	{

        ArrayList<String> xVals = new ArrayList<String>();
        for (int i = 0; i < count; i++) 
        {
        	xVals.add((i) + " yr");
        }
        
        ArrayList<Entry> yVals = new ArrayList<Entry>();

        // TODO: Debug data-set for first impression
        yVals.add(new Entry((float) 50, 0));
        yVals.add(new Entry((float) 70, 1));
        yVals.add(new Entry((float) 80, 2));
        yVals.add(new Entry((float) 88, 3));
        yVals.add(new Entry((float) 96, 4));
        yVals.add(new Entry((float) 100, 5));
        yVals.add(new Entry((float) 106, 6));
        yVals.add(new Entry((float) 112, 7));
        yVals.add(new Entry((float) 119, 8));
        yVals.add(new Entry((float) 126, 9));
        yVals.add(new Entry((float) 130, 10));
        yVals.add(new Entry((float) 136, 11));
        yVals.add(new Entry((float) 141, 12));
        yVals.add(new Entry((float) 145, 13));
        yVals.add(new Entry((float) 150, 14));
        yVals.add(new Entry((float) 153, 15));
        yVals.add(new Entry((float) 155, 16));
        yVals.add(new Entry((float) 156, 17));
        yVals.add(new Entry((float) 156, 18));
        
        // create a dataset and give it a type
        LineDataSet set1 = new LineDataSet(yVals, "Child 2007");
        set1.setColor(Color.parseColor("#0171bd"));
        set1.setCircleColor(Color.parseColor("#0171bd"));
        set1.setLineWidth(2f);
        set1.setCircleSize(4f);
        set1.setFillAlpha(65);
        set1.setFillColor(ColorTemplate.getHoloBlue());
        set1.setHighLightColor(Color.rgb(244, 117, 117));
        set1.setDrawFilled(true);
        
        
        
        ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();
        dataSets.add(set1); // add the datasets

        // create a data object with the datasets
        LineData data = new LineData(xVals, dataSets);

        // set data
        lcHeight.setData(data);
    }
}
