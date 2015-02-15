package com.uni.bradford.bib;

import java.util.ArrayList;




import android.app.Activity;
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
			System.out.println("Clicked on item in listview");
		}	
	}
	
	private class IvSocialMediaOnClickListener implements OnClickListener
	{
		@Override
		public void onClick(View view)
		{
			// TODO: Add behaviour
			switch(view.getId())
			{
			case R.id.ivSocialMedia1: 
			{
				System.out.println("Clicked social media 1");
				break;
			}
			case R.id.ivSocialMedia2: 
			{
				System.out.println("Clicked social media 2");
				break;
			}
			case R.id.ivSocialMedia3: 
			{
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
