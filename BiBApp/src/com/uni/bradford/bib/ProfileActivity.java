package com.uni.bradford.bib;

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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;

public class ProfileActivity extends Activity
{
	// GUI
	private Spinner sSelectChild;
	private ListView lvProgram;
	private ProgramListViewAdapter listAdapter;
	private ImageView ivMap01check;
	private ImageView ivMap02check;
	private ImageView ivMap03check;
	private ImageView ivMap04check;
	private ImageView ivMap05check;
	private ImageView ivMap06check;
	private ImageView ivMap07check;
	
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
			ivMap01check.setVisibility(ImageView.VISIBLE);
		}
		else
		{
			programList.add(new ProgramEntry(false, programs[0], getResources().getString(R.string.hello_world)));
			ivMap01check.setVisibility(ImageView.INVISIBLE);
		}
		
		if (1 >= dataModel.getMother().getChild(position).getPrimaryCare())
		{
			programList.add(new ProgramEntry(true, programs[1], getResources().getString(R.string.hello_world)));
			ivMap02check.setVisibility(ImageView.VISIBLE);
		}
		else
		{
			programList.add(new ProgramEntry(false, programs[1], getResources().getString(R.string.hello_world)));
			ivMap02check.setVisibility(ImageView.INVISIBLE);
		}
		
		if (1 >= dataModel.getMother().getChild(position).getEducation())
		{
			programList.add(new ProgramEntry(true, programs[2], getResources().getString(R.string.hello_world)));
			ivMap03check.setVisibility(ImageView.VISIBLE);
		}
		else
		{
			programList.add(new ProgramEntry(false, programs[2], getResources().getString(R.string.hello_world)));
			ivMap03check.setVisibility(ImageView.INVISIBLE);
		} 
		
		if (1 >= dataModel.getMother().getChild(position).getBib1000())
		{
			programList.add(new ProgramEntry(true, programs[3], getResources().getString(R.string.hello_world)));
			ivMap04check.setVisibility(ImageView.VISIBLE);
		}
		else
		{
			programList.add(new ProgramEntry(false, programs[3], getResources().getString(R.string.hello_world)));
			ivMap04check.setVisibility(ImageView.INVISIBLE);
		}
		
		if (1 >= dataModel.getMother().getChild(position).getMeDall())
		{
			programList.add(new ProgramEntry(true, programs[4], "Mechanisms of the Development of Allergy")); // TODO: No static texts
			ivMap05check.setVisibility(ImageView.VISIBLE);
		}
		else
		{
			programList.add(new ProgramEntry(false, programs[4], "Mechanisms of the Development of Allergy")); // TODO: No static texts
			ivMap05check.setVisibility(ImageView.INVISIBLE);
		}
		
		if (1 >= dataModel.getMother().getChild(position).getAll_in())
		{
			programList.add(new ProgramEntry(true, programs[5], "Prevention of childhood obesity")); // TODO: No static texts
			ivMap07check.setVisibility(ImageView.VISIBLE);
		}
		else
		{
			programList.add(new ProgramEntry(false, programs[5], "Prevention of childhood obesity")); // TODO: No static texts
			ivMap07check.setVisibility(ImageView.INVISIBLE);
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
