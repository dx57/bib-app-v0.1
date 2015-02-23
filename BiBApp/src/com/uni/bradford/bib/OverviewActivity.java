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
import android.widget.ImageView;
import android.widget.ListView;

public class OverviewActivity extends Activity
{
	// GUI
	private ListView lvOverview;
	private ImageView ivSocialMediaFacebook;
	private ImageView ivSocialMediaTwitter;
	private ImageView ivSocialMediaYoutube;
	
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
		
		listAdapter = new OverviewListViewAdapter(overviewList, this);
		lvOverview = (ListView)findViewById(R.id.lvOverview);
		lvOverview.setDivider(null);
		lvOverview.setAdapter(listAdapter);
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
