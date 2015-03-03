package com.uni.bradford.bib;

import java.util.ArrayList;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
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
	
	private OverviewListViewAdapter listAdapter;
	
	// Model
	private ArrayList<OverviewEntry> overviewList;
	private String surveyUrl;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_overview);
		
		// Change ActionBar color and icon
		ActionBar bar = getActionBar();
		bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#0171bd")));
		bar.setIcon(R.drawable.ic_launcher);
		
		// Init local data-model
		overviewList = new ArrayList<OverviewEntry>();
		overviewList.add(new OverviewEntry(getResources().getString(R.string.title_activity_height_visual), R.drawable.ic_ruler_green)); // TODO: Build that entries know which intent to call.. saves code
		overviewList.add(new OverviewEntry(getResources().getString(R.string.title_activity_height_diagram), R.drawable.ic_ruler_green));
		overviewList.add(new OverviewEntry(getResources().getString(R.string.title_activity_survey), R.drawable.ic_survey_green));
		overviewList.add(new OverviewEntry(getResources().getString(R.string.title_activity_about), R.drawable.ic_about_green));
		overviewList.add(new OverviewEntry(getResources().getString(R.string.my_profile), R.drawable.ic_about_green));
		
		// TODO: Load from data model
		surveyUrl = "https://www.surveymonkey.com/s/HMP398J";
		
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
	    if (requestCode == 1) 
	    {
	        if(resultCode == RESULT_OK)
	        {
	        	// TODO: Disable survey button
	        	// Do when listview is rearanged
	        	
	        	
	        	System.out.println("OverviewActivity: RESULT_OK");
	        }
	    }
	}
	
	public String getSurveyUrl()
	{
		return surveyUrl;
	}
	
}
