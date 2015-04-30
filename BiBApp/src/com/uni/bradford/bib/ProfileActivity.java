package com.uni.bradford.bib;

import java.util.ArrayList;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

public class ProfileActivity extends Activity
{
	// GUI
	private Spinner sSelectChild;
	private ListView lvProgram;
	private ProgramListViewAdapter listAdapter;
	
	// Logic
	private DataModel dataModel;
	
	// Model 
	private ArrayList<ProgramEntry> programList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);
		
		// Change ActionBar color and icon
		ActionBar bar = getActionBar(); 
		bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#0171bd")));
		bar.setIcon(R.drawable.ic_profile_white); 
		
		// Init logic
		LoadDataModelFromFileAsyncTask loadLocalTask = new LoadDataModelFromFileAsyncTask();
		loadLocalTask.execute();
		
		// Init local data-model
		programList = new ArrayList<ProgramEntry>(); 
				
		// Connect to GUI views and setup	
		sSelectChild = (Spinner)findViewById(R.id.sProfileSelectChild);
		
		// Setup program list
		listAdapter = new ProgramListViewAdapter(programList, this);
		lvProgram = (ListView)findViewById(R.id.lvOverview);
		lvProgram.setDivider(null);
		lvProgram.setAdapter(listAdapter);
		
		// Add listener
		sSelectChild.setOnItemSelectedListener(new OnSpinnerSelectChildSelectedListener());
	}
	
	public void updateGui()
	{		
		String[] children = new String[dataModel.getMother().getChildCount()];
		for (int i = 0; i < dataModel.getMother().getChildCount(); i++)
		{
			children[i] = dataModel.getMother().getChild(i).getYearOfBirth() + 
					      "-" + dataModel.getMother().getChild(i).getMonthOfBirth();
		}
		
		ArrayAdapter<String> adapterChilds = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, children);
		adapterChilds.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sSelectChild.setAdapter(adapterChilds);
		
		changeProgramList(0);
	}
	
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
	
	private void changeProgramList(int position)
	{
		programList.clear();
		
		if (dataModel == null)
		{
			return;
		}
		
		String[] programs = getResources().getStringArray(R.array.programs);
		
		if (1 >= dataModel.getMother().getChild(position).getEclipse())
		{
			programList.add(new ProgramEntry(true, programs[0], getResources().getString(R.string.hello_world)));
		}
		else
		{
			programList.add(new ProgramEntry(false, programs[0], getResources().getString(R.string.hello_world)));
		}
		
		if (1 >= dataModel.getMother().getChild(position).getPrimaryCare())
		{
			programList.add(new ProgramEntry(true, programs[1], getResources().getString(R.string.hello_world)));
		}
		else
		{
			programList.add(new ProgramEntry(false, programs[1], getResources().getString(R.string.hello_world)));
		}
		
		if (1 >= dataModel.getMother().getChild(position).getEducation())
		{
			programList.add(new ProgramEntry(true, programs[2], getResources().getString(R.string.hello_world)));
		}
		else
		{
			programList.add(new ProgramEntry(false, programs[2], getResources().getString(R.string.hello_world)));
		}
		
		if (1 >= dataModel.getMother().getChild(position).getBib1000())
		{
			programList.add(new ProgramEntry(true, programs[3], getResources().getString(R.string.hello_world)));
		}
		else
		{
			programList.add(new ProgramEntry(false, programs[3], getResources().getString(R.string.hello_world)));
		}
		
		if (1 >= dataModel.getMother().getChild(position).getMeDall())
		{
			programList.add(new ProgramEntry(true, programs[4], getResources().getString(R.string.hello_world)));
		}
		else
		{
			programList.add(new ProgramEntry(false, programs[4], getResources().getString(R.string.hello_world)));
		}
		
		if (1 >= dataModel.getMother().getChild(position).getAll_in())
		{
			programList.add(new ProgramEntry(true, programs[5], getResources().getString(R.string.hello_world)));
		}
		else
		{
			programList.add(new ProgramEntry(false, programs[5], getResources().getString(R.string.hello_world)));
		}
	}
	
	private class LoadDataModelFromFileAsyncTask extends AsyncTask<Void, Void, Void>
	{
		@Override
		protected Void doInBackground(Void... params)
		{		
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
				
//				Toast toast = Toast.makeText(HeightDiagramActivity.this, "..loaded from file", Toast.LENGTH_SHORT);
//				toast.show();
			}
		}
	}
}
