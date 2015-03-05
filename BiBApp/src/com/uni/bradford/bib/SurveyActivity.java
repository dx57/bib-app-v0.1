package com.uni.bradford.bib;

import android.app.ActionBar;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

public class SurveyActivity extends Activity
{
	// GUI
	private WebView wvSurvey;
	private ProgressBar pbLoadSurvey;
	private TextView tvInfoToSurvey;
	
	// Logic
	private static final String SURVEY_COMPLETE = "survey-thanks";
	private boolean tookSurvey;
	private String surveyUrl;
	private BroadcastReceiver networkStateBroadcastReceiver;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_survey);
		
		// Init logic
		tookSurvey = false;
		networkStateBroadcastReceiver = new NetworkStateBroadcastReceiver();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
		registerReceiver(networkStateBroadcastReceiver, intentFilter);
		
		// Change ActionBar color and icon
		ActionBar bar = getActionBar();
		bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#0171bd")));
		bar.setIcon(R.drawable.ic_survey_white);
		
		// Connect to GUI views and setup	
		wvSurvey = (WebView)findViewById(R.id.wvSurvey);
		pbLoadSurvey = (ProgressBar)findViewById(R.id.pbLoadSurvey);
		tvInfoToSurvey = (TextView)findViewById(R.id.tvInfoToSurvey);
		
		// Must-Have for SurveyMonkey.. but might allow cross-side-scripting
		wvSurvey.getSettings().setJavaScriptEnabled(true);
		
		// Use SurveyMonkey to deal with survey options 
		surveyUrl = getIntent().getStringExtra(OverviewListViewAdapter.SURVEY_URL);
		wvSurvey.loadUrl(surveyUrl);
		
		// Add listener
		wvSurvey.setWebViewClient(new SurveyWebViewClient());
		
		// TODO: Check for Internet connection and aleart user if no connection
	}
	
	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		
		unregisterReceiver(networkStateBroadcastReceiver);
	}
	
	private class SurveyWebViewClient extends WebViewClient
	{
		@Override
	    public boolean shouldOverrideUrlLoading(WebView  view, String  url)
		{
			if (url.contains(surveyUrl) || url.contains(SURVEY_COMPLETE))
			{
				// User started or finished survey.. do react
				return false;
			}
			
			// User clicked an unexpected link.. do not react
	        return true;
	    }
		
		public void onPageFinished(WebView view, String url) 
		{
			System.out.println("load " + url + " complete.");
			
			// Only if user starts the survey
			if (!url.contains(SURVEY_COMPLETE))
			{
				// Hide progress bar and show loaded survey
				tvInfoToSurvey.setVisibility(TextView.INVISIBLE);
				pbLoadSurvey.setVisibility(ProgressBar.INVISIBLE);
				wvSurvey.setVisibility(WebView.VISIBLE);
			}
		}
		
		public void onPageStarted(WebView view, String url, Bitmap favicon) 
		{
			System.out.println("started " + url);
			
			// Indicates the end of the survey
			if (url.contains(SURVEY_COMPLETE))
			{
				// Thank user and hide unimportant views
				wvSurvey.setVisibility(WebView.INVISIBLE);
				tvInfoToSurvey.setText(R.string.survey_thanks);
				tvInfoToSurvey.setVisibility(TextView.VISIBLE);
				
				tookSurvey = true;
			}			
	    }
	}
	
	// TODO: Extract class to own file, because I will need it at least also for the login screen
	public class NetworkStateBroadcastReceiver extends BroadcastReceiver  
	{
		@Override
		public void onReceive(Context context, Intent intent)
		{
			ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
			
			if (networkInfo != null && networkInfo.isConnected())
			{
				
				System.out.println("CONNECTED!!!!");
			}
			else
			{
				// TODO: Add Alert dialog here
				
				System.out.println("DIS-CONNECTED!!!!");
			}
			
		}
		
	}
	
	@Override
	public void onBackPressed() 
	{
		System.out.println("Back button pressed.");

		Intent returnToOverview = new Intent();
		
		// Check if user took the survey
		if (tookSurvey)
		{
			System.out.println("RESULT_OK");
			setResult(RESULT_OK, returnToOverview);
		}
		else
		{
			System.out.println("RESULT_CANCELED");
			setResult(RESULT_CANCELED, returnToOverview);
		}
		
		finish();
	}
} 
