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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;

public class OverviewActivity extends Activity
{
	// GUI
	private ListView lvOverview;
	private ImageView ivSocialMediaFacebook;
	private ImageView ivSocialMediaTwitter;
	private ImageView ivSocialMediaYoutube;
	
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
		
		// Init local data-model
		overviewList = new ArrayList<OverviewEntry>();
		overviewList.add(new OverviewEntry(getResources().getString(R.string.height), getResources().getString(R.string.height_detail1), R.drawable.ic_ruler_green)); // TODO: Build that entries know which intent to call.. saves code
		overviewList.add(new OverviewEntry(getResources().getString(R.string.height), getResources().getString(R.string.height_detail2), R.drawable.ic_ruler_green));
		overviewList.add(new OverviewEntry(getResources().getString(R.string.survey), getResources().getString(R.string.survey_detail), R.drawable.ic_survey_green));
		overviewList.add(new OverviewEntry(getResources().getString(R.string.about), getResources().getString(R.string.about_detail), R.drawable.ic_about_green));
		overviewList.add(new OverviewEntry(getResources().getString(R.string.about), getResources().getString(R.string.about_detail), R.drawable.ic_about_green));
		overviewList.add(new OverviewEntry(getResources().getString(R.string.about), getResources().getString(R.string.about_detail), R.drawable.ic_about_green));
		overviewList.add(new OverviewEntry(getResources().getString(R.string.about), getResources().getString(R.string.about_detail), R.drawable.ic_about_green));
		
		// Connect to GUI views and setup
		ivSocialMediaFacebook = (ImageView)findViewById(R.id.ivSocialMediaFacebook);
		ivSocialMediaTwitter = (ImageView)findViewById(R.id.ivSocialMediaTwitter);
		ivSocialMediaYoutube = (ImageView)findViewById(R.id.ivSocialMediaYoutube);
		IvSocialMediaOnClickListener ivSocialMediaOnClickListener = new IvSocialMediaOnClickListener();
		ivSocialMediaFacebook.setOnClickListener(ivSocialMediaOnClickListener);
		ivSocialMediaTwitter.setOnClickListener(ivSocialMediaOnClickListener);
		ivSocialMediaYoutube.setOnClickListener(ivSocialMediaOnClickListener);
		
		OverviewListViewAdapter listAdapter = new OverviewListViewAdapter(overviewList);
		lvOverview = (ListView)findViewById(R.id.lvOverview);
		lvOverview.setAdapter(listAdapter);
		
		lvOverview.setOnItemClickListener(new LvOverviewOnItemClickListener());
	}
	
	private class LvOverviewOnItemClickListener implements OnItemClickListener
	{
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id)
		{
			// TODO: Add behaviour
			System.out.println("Clicked on item in listview: " + position);
			
			switch(position)
			{
				case 0: 
				{
					// Change to HeightVisual activity 
					Intent changeToHeightVisual = new Intent(OverviewActivity.this, HeightVisualActivity.class);
					startActivity(changeToHeightVisual);
					
					break;
				}
				case 1: 
				{
					// Change to diagram height activity 
					Intent changeToDiagramHeight = new Intent(OverviewActivity.this, HeightDiagramActivity.class);
					startActivity(changeToDiagramHeight);
					
					break;
				}
				case 2: 
				{
					// Change to Survey activity 
					Intent changeToSurvey = new Intent(OverviewActivity.this, SurveyActivity.class);
					startActivity(changeToSurvey);
					
					break; 
				}
				case 3: 
				{
					// Change to About activity 
					Intent changeToAbout = new Intent(OverviewActivity.this, AboutActivity.class);
					startActivity(changeToAbout);
					
					break; 
				}
				case 4: 
				{
									
					break;
				}
				default:
				{
					System.out.println("Default: No behaviour specified");
					break;
				}
			}
		}	
	}
	
	private class IvSocialMediaOnClickListener implements OnClickListener
	{
		@Override
		public void onClick(View view)
		{
			// TODO: Check for Internet connection?
			
			switch(view.getId())
			{
				case R.id.ivSocialMediaFacebook: 
				{
					System.out.println("Clicked social media 1");
					
					// Start browser with BiB Facebook page
					Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.facebook.com/BornInBradford?fref=ts"));
					startActivity(browserIntent);
					
					break;
				}
				case R.id.ivSocialMediaTwitter: 
				{
					System.out.println("Clicked social media 2");
					
					// Start browser with BiB Twitter page
					Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/BiBresearch"));
					startActivity(browserIntent);
					
					break;
				}
				case R.id.ivSocialMediaYoutube: 
				{
					// Start browser with BiB Youtube page
					Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/user/BorninBradford2011"));
					startActivity(browserIntent);
					
					System.out.println("Clicked social media 3");
					break;
				}
				default:
				{
					System.out.println("Error");
					break;
				}
			}
			
		}
	}
	
}
