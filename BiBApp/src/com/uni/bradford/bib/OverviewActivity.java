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
	private ImageView ivSocialMedia1;
	private ImageView ivSocialMedia2;
	private ImageView ivSocialMedia3;
	
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
		overviewList.add(new OverviewEntry(getResources().getString(R.string.height), getResources().getString(R.string.height_detail), R.drawable.ic_launcher)); // TODO: Build that entries know which intent to call.. saves code
		overviewList.add(new OverviewEntry(getResources().getString(R.string.temperature), getResources().getString(R.string.temperature_detail), R.drawable.ic_launcher));
		overviewList.add(new OverviewEntry(getResources().getString(R.string.survey), getResources().getString(R.string.survey_detail), R.drawable.ic_launcher));
		overviewList.add(new OverviewEntry(getResources().getString(R.string.location), getResources().getString(R.string.location_detail), R.drawable.ic_launcher));
		overviewList.add(new OverviewEntry(getResources().getString(R.string.about), getResources().getString(R.string.about_detail), R.drawable.ic_launcher));
		overviewList.add(new OverviewEntry(getResources().getString(R.string.about), getResources().getString(R.string.about_detail), R.drawable.ic_launcher));
		overviewList.add(new OverviewEntry(getResources().getString(R.string.about), getResources().getString(R.string.about_detail), R.drawable.ic_launcher));
		overviewList.add(new OverviewEntry(getResources().getString(R.string.about), getResources().getString(R.string.about_detail), R.drawable.ic_launcher));
		
		// Connect to GUI views and setup
		ivSocialMedia1 = (ImageView)findViewById(R.id.ivSocialMedia1);
		ivSocialMedia2 = (ImageView)findViewById(R.id.ivSocialMedia2);
		ivSocialMedia3 = (ImageView)findViewById(R.id.ivSocialMedia3);
		IvSocialMediaOnClickListener ivSocialMediaOnClickListener = new IvSocialMediaOnClickListener();
		ivSocialMedia1.setOnClickListener(ivSocialMediaOnClickListener);
		ivSocialMedia2.setOnClickListener(ivSocialMediaOnClickListener);
		ivSocialMedia3.setOnClickListener(ivSocialMediaOnClickListener);
		
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
					
					break; 
				}
				case 4: 
				{
					// Change to About activity 
					Intent changeToAbout = new Intent(OverviewActivity.this, AboutActivity.class);
					startActivity(changeToAbout);
					
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
				case R.id.ivSocialMedia1: 
				{
					System.out.println("Clicked social media 1");
					
					// Start browser with BiB Facebook page
					Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.facebook.com/BornInBradford?fref=ts"));
					startActivity(browserIntent);
					
					break;
				}
				case R.id.ivSocialMedia2: 
				{
					System.out.println("Clicked social media 2");
					
					// Start browser with BiB Twitter page
					Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/BiBresearch"));
					startActivity(browserIntent);
					
					break;
				}
				case R.id.ivSocialMedia3: 
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
