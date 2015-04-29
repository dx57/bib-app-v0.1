package com.uni.bradford.bib;

import java.util.ArrayList;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

public class OverviewActivity extends Activity
{
	// GUI
	private ListView lvOverview;
	private ImageView ivSocialMediaFacebook;
	private ImageView ivSocialMediaTwitter;
	private ImageView ivSocialMediaYoutube;
	
	// Logic	
	public static final int SURVEY_REQUEST = 1;
	private DataModel dataModel;
	
	private OverviewListViewAdapter listAdapter;
	
	// Model 
	private ArrayList<OverviewEntry> overviewList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_overview);
		
		// Change ActionBar color and icon
		ActionBar bar = getActionBar(); 
		bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#0171bd")));
		bar.setIcon(R.drawable.ic_launcher);
				 
		// Init logic 
		LoadDataModelFromFileAsyncTask loadLocalTask = new LoadDataModelFromFileAsyncTask();
		loadLocalTask.execute();
		
		// Init local data-model
		overviewList = new ArrayList<OverviewEntry>(); 
		
		Intent intent = new Intent(OverviewActivity.this, HeightVisualActivity.class);
		overviewList.add(new OverviewEntry(getResources().getString(R.string.title_activity_height_visual), R.drawable.ic_ruler_green, intent)); 
		
		intent = new Intent(OverviewActivity.this, HeightDiagramActivity.class);
		overviewList.add(new OverviewEntry(getResources().getString(R.string.title_activity_height_diagram), R.drawable.ic_ruler_green, intent));
		
		intent = new Intent(OverviewActivity.this, SurveyActivity.class);	
		intent.putExtra("survey-url", "https://www.surveymonkey.com/s/HMP398J"); // TODO: Load from data model
		overviewList.add(new OverviewEntry(getResources().getString(R.string.title_activity_survey), R.drawable.ic_survey_green, intent, SURVEY_REQUEST));
		
		intent = new Intent(OverviewActivity.this, AboutActivity.class);
		overviewList.add(new OverviewEntry(getResources().getString(R.string.title_activity_about), R.drawable.ic_about_green, intent));
		
		intent = new Intent(OverviewActivity.this, ProfileActivity.class); // TODO: Change
		overviewList.add(new OverviewEntry(getResources().getString(R.string.my_profile), R.drawable.ic_profile_green, intent));
		
		// Connect to GUI views and setup
		ivSocialMediaFacebook = (ImageView)findViewById(R.id.ivSocialMediaFacebook);
		ivSocialMediaTwitter = (ImageView)findViewById(R.id.ivSocialMediaTwitter);
		ivSocialMediaYoutube = (ImageView)findViewById(R.id.ivSocialMediaYoutube);
		ivSocialMediaFacebook.setOnClickListener(new IvSocialMediaOnClickListener("http://www.facebook.com/BornInBradford?fref=ts"));
		ivSocialMediaTwitter.setOnClickListener(new IvSocialMediaOnClickListener("https://twitter.com/BiBresearch"));
		ivSocialMediaYoutube.setOnClickListener(new IvSocialMediaOnClickListener("http://www.youtube.com/user/BorninBradford2011"));
		
		// Setup navigation list
		listAdapter = new OverviewListViewAdapter(overviewList, this);
		lvOverview = (ListView)findViewById(R.id.lvOverview);
		lvOverview.setDivider(null);
		lvOverview.setAdapter(listAdapter);
	}
	
	@Override
	protected void onPause()
	{
		super.onPause();

		// Save all changes the activity did to the data model
		SaveDataModeToFilelAsyncTask saveTask = new SaveDataModeToFilelAsyncTask();
		saveTask.execute();
	}
	
	private class IvSocialMediaOnClickListener implements OnClickListener
	{
		private String socialMediaUrl;
		
		public IvSocialMediaOnClickListener(String socialMediaUrl)
		{
			this.socialMediaUrl = socialMediaUrl;
		}
		
		@Override
		public void onClick(View view)
		{
			// Start browser with selected BiB social media page
			Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(socialMediaUrl));
			startActivity(browserIntent);
		}
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
	    if (requestCode == SURVEY_REQUEST) 
	    {
	        if(resultCode == RESULT_OK)
	        {
	        	System.out.println("OverviewActivity: RESULT_OK");
	 
	        	// TODO: set to true again.. just for user testing session
	        	dataModel.setTookSurvey(false);
	        	
	        	updateGui();
	        }
	    }
	}
	
	public void updateGui()
	{
		// TODO: Get correct list entry position automatically
		Button btnEntry = (Button)lvOverview.getChildAt(2).findViewById(R.id.btnEntry);
    	btnEntry.setEnabled(!dataModel.isTookSurvey());
	}
	
	private class LoadDataModelFromFileAsyncTask extends AsyncTask<Void, Void, Void>
	{
		@Override
		protected Void doInBackground(Void... params)
		{		
			dataModel = DataModel.loadFromFile(OverviewActivity.this.getFilesDir());
						
			return null; 
		}
		
		@Override
		protected void onPostExecute(Void result)
		{	
			if (dataModel != null)
			{
				// Update GUI
				updateGui();
				
//				Toast toast = Toast.makeText(OverviewActivity.this, "..loaded from file", Toast.LENGTH_SHORT);
//				toast.show();
			}
		}
	}
	
	private class SaveDataModeToFilelAsyncTask extends AsyncTask<Void, Void, Void>
	{			
		@Override
		protected Void doInBackground(Void... params)
		{	
			if (dataModel != null)
			{
				dataModel.saveToFile(dataModel, OverviewActivity.this.getFilesDir());
			}
			
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result)
		{
			 // Debug: Show in GUI
//			 Toast toast = Toast.makeText(OverviewActivity.this, "..saved to file", Toast.LENGTH_SHORT);
//			 toast.show();
		}
	}
}
