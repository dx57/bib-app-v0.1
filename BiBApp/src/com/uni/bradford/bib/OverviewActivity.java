package com.uni.bradford.bib;

import java.util.ArrayList;

import com.uni.bradford.bib.control.AboutActivity;
import com.uni.bradford.bib.control.ProfileActivity;
import com.uni.bradford.bib.control.SurveyActivity;

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

/**
 * Class to deal with in-App navigation
 * 
 * @author Martin
 */
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
				 
		// Init logic 
		LoadDataModelFromFileAsyncTask loadLocalTask = new LoadDataModelFromFileAsyncTask();
		loadLocalTask.execute();
		
		// Init local data-model
		overviewList = new ArrayList<OverviewEntry>(); 
		
		Intent intent = new Intent(OverviewActivity.this, HeightVisualActivity.class);
		overviewList.add(new OverviewEntry(getResources().getString(R.string.title_activity_height_visual), R.drawable.ic_height_visual_green, intent)); 
		
		intent = new Intent(OverviewActivity.this, HeightDiagramActivity.class);
		overviewList.add(new OverviewEntry(getResources().getString(R.string.title_activity_height_diagram), R.drawable.ic_height_diagram_green, intent));
		
		intent = new Intent(OverviewActivity.this, ProfileActivity.class); 
		overviewList.add(new OverviewEntry(getResources().getString(R.string.title_activity_profile), R.drawable.ic_profile_green, intent));
		
		intent = new Intent(OverviewActivity.this, SurveyActivity.class);	
		intent.putExtra("survey-url", "https://www.surveymonkey.com/s/HMP398J"); // TODO: Load from data model
		overviewList.add(new OverviewEntry(getResources().getString(R.string.title_activity_survey), R.drawable.ic_survey_green, intent, SURVEY_REQUEST));
		
		intent = new Intent(OverviewActivity.this, AboutActivity.class);
		overviewList.add(new OverviewEntry(getResources().getString(R.string.title_activity_about), R.drawable.ic_about_green, intent));
		
		// Connect to GUI views and setup
		ivSocialMediaFacebook = (ImageView)findViewById(R.id.ivSocialMediaFacebook);
		ivSocialMediaTwitter = (ImageView)findViewById(R.id.ivSocialMediaTwitter);
		ivSocialMediaYoutube = (ImageView)findViewById(R.id.ivSocialMediaYoutube);
		ivSocialMediaFacebook.setOnClickListener(new IvSocialMediaOnClickListener(getResources().getString(R.string.link_facebook)));
		ivSocialMediaTwitter.setOnClickListener(new IvSocialMediaOnClickListener(getResources().getString(R.string.link_twitter)));
		ivSocialMediaYoutube.setOnClickListener(new IvSocialMediaOnClickListener(getResources().getString(R.string.link_youtube)));
		
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
	
	/**
	 * Class to deal with social media links
	 */
	private class IvSocialMediaOnClickListener implements OnClickListener
	{
		private String socialMediaUrl;
		
		/**
		 * Initialise each listener with the according link
		 */
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
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
		// Check request code
	    if (requestCode == SURVEY_REQUEST) 
	    {
	    	// Check result code
	        if(resultCode == RESULT_OK)
	        {
	        	System.out.println("OverviewActivity: RESULT_OK");
	 
	        	// User completed survey (prohibit to take survey again)
	        	dataModel.setTookSurvey(false); // TODO: Set to true.. false only for development/test process
	        	
	        	updateGui();
	        }
	    }
	}
	
	/**
	 * Change GUI according to data model
	 */
	public void updateGui()
	{
		// Enable/disable user to take survey (again)
		Button btnEntry = (Button)lvOverview.getChildAt(3).findViewById(R.id.btnEntry);
    	btnEntry.setEnabled(!dataModel.isTookSurvey());
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
				
				// Toast toast = Toast.makeText(OverviewActivity.this, "..loaded from file", Toast.LENGTH_SHORT);
				// toast.show();
			}
		}
	}
	
	/**
	 * Class to save data model to local file
	 */
	private class SaveDataModeToFilelAsyncTask extends AsyncTask<Void, Void, Void>
	{			
		@Override
		protected Void doInBackground(Void... params)
		{	
			// Only save if data model is initialised
			if (dataModel != null)
			{
				// Save data model to local file
				dataModel.saveToFile(dataModel, OverviewActivity.this.getFilesDir());
			}
			
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result)
		{
			 // Debug: Show in GUI
			 // Toast toast = Toast.makeText(OverviewActivity.this, "..saved to file", Toast.LENGTH_SHORT);
			 // toast.show();
		}
	}
}
