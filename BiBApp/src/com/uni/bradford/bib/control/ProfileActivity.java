package com.uni.bradford.bib.control;

import java.util.ArrayList;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;

import com.uni.bradford.bib.R;
import com.uni.bradford.bib.model.DataModel;

/**
 * Class to deal with user interaction for showing profile information
 * 
 * @author Martin
 */
public class ProfileActivity extends Activity
{
	// GUI
	private ImageView ivMap01check;
	private ImageView ivMap02check;
	private ImageView ivMap03check;
	private ImageView ivMap04check;
	private ImageView ivMap05check;
	private ImageView ivMap06check;
	private ImageView ivMap07check;
	
	private Spinner sSelectChild;
	
	private ListView lvProgram;
	private BaseAdapter listAdapter;
	
	// Logic
	private DataModel dataModel;
	private ArrayList<ProgramEntry> programList;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);
		
		// Change ActionBar color
		ActionBar bar = getActionBar(); 
		bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor(getResources().getString(R.string.bib_blue))));
		
		// Init logic
		LoadDataModelFromFileAsyncTask loadLocalTask = new LoadDataModelFromFileAsyncTask();
		loadLocalTask.execute();
		
		// Init local data-model
		programList = new ArrayList<ProgramEntry>(); 
				
		// Connect to GUI views and setup	
		sSelectChild = (Spinner)findViewById(R.id.sProfileSelectChild);
		
		ivMap01check = (ImageView)findViewById(R.id.ivMap01check);
		ivMap02check = (ImageView)findViewById(R.id.ivMap02check);
		ivMap03check = (ImageView)findViewById(R.id.ivMap03check);
		ivMap04check = (ImageView)findViewById(R.id.ivMap04check);
		ivMap05check = (ImageView)findViewById(R.id.ivMap05check);
		ivMap06check = (ImageView)findViewById(R.id.ivMap06check);
		ivMap07check = (ImageView)findViewById(R.id.ivMap07check);
		
		// Setup program list
		listAdapter = new ProgramListViewAdapter(programList, this);
		lvProgram = (ListView)findViewById(R.id.lvOverview);
		lvProgram.setDivider(null);
		lvProgram.setAdapter(listAdapter);
		
		// Add listener
		sSelectChild.setOnItemSelectedListener(new OnSpinnerSelectChildSelectedListener());
	}
	
	/**
	 * Change GUI according to available data
	 */
	private void updateGui()
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
		sSelectChild.setAdapter(adapterChilds);
		
		// Init results for preselection
		changeProgramList(0);
	}

	/**
	 * Fill list and draw infographic according to programs the selected child took part in
	 * 
	 * @param position Position of the selected child within the selection Spinner
	 */
	private void changeProgramList(int position)
	{
		// Clear list content for prior child
		programList.clear();
		
		// Make sure dataModel was initialised
		if (dataModel == null)
		{
			return;
		}
		
		// Load different program names
		String[] programNames = getResources().getStringArray(R.array.program_names);
		String[] programDescription = getResources().getStringArray(R.array.program_descriptions);
		
		// Fill list and draw infographic according to programs the selected child took part in
		if (1 == dataModel.getMother().getChild(position).getEclipse())
		{
			programList.add(new ProgramEntry(true, programNames[0], programDescription[0]));
			ivMap01check.setVisibility(ImageView.VISIBLE);
		}
		else
		{
			programList.add(new ProgramEntry(false, programNames[0], programDescription[0]));
			ivMap01check.setVisibility(ImageView.INVISIBLE);
		}
		
		if (1 == dataModel.getMother().getChild(position).getPrimaryCare())
		{
			programList.add(new ProgramEntry(true, programNames[1], programDescription[1]));
			ivMap02check.setVisibility(ImageView.VISIBLE);
		}
		else
		{
			programList.add(new ProgramEntry(false, programNames[1], programDescription[1]));
			ivMap02check.setVisibility(ImageView.INVISIBLE);
		}
		
		if (1 == dataModel.getMother().getChild(position).getEducation())
		{
			programList.add(new ProgramEntry(true, programNames[2], programDescription[2]));
			ivMap03check.setVisibility(ImageView.VISIBLE);
		}
		else
		{
			programList.add(new ProgramEntry(false, programNames[2], programDescription[2]));
			ivMap03check.setVisibility(ImageView.INVISIBLE);
		} 
		
		if (1 == dataModel.getMother().getChild(position).getBib1000())
		{
			programList.add(new ProgramEntry(true, programNames[3], programDescription[3]));
			ivMap04check.setVisibility(ImageView.VISIBLE);
		}
		else
		{
			programList.add(new ProgramEntry(false, programNames[3], programDescription[3]));
			ivMap04check.setVisibility(ImageView.INVISIBLE);
		}
		
		if (1 == dataModel.getMother().getChild(position).getMeDall())
		{
			programList.add(new ProgramEntry(true, programNames[4], programDescription[4]));
			ivMap05check.setVisibility(ImageView.VISIBLE);
		}
		else
		{
			programList.add(new ProgramEntry(false, programNames[4], programDescription[4]));
			ivMap05check.setVisibility(ImageView.INVISIBLE);
		}
		
		if (1 == dataModel.getMother().getChild(position).getAll_in())
		{
			programList.add(new ProgramEntry(true, programNames[5], programDescription[5]));
			ivMap07check.setVisibility(ImageView.VISIBLE);
		}
		else
		{
			programList.add(new ProgramEntry(false, programNames[5], programDescription[5]));
			ivMap07check.setVisibility(ImageView.INVISIBLE);
		}
	}
	
	/**
	 * Class to react on child selection
	 */
	private class OnSpinnerSelectChildSelectedListener implements OnItemSelectedListener
	{
		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
		{
			changeProgramList(position);
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) { }
	}
	
	/**
	 * Class to load local data model
	 */
	private class LoadDataModelFromFileAsyncTask extends AsyncTask<Void, Void, Void>
	{
		@Override
		protected Void doInBackground(Void... params)
		{		
			// Load data model from file
			dataModel = DataModel.loadFromFile(ProfileActivity.this.getFilesDir());
						
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
