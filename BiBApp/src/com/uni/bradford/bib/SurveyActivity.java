package com.uni.bradford.bib;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_survey);
		
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
		
		// TODO: Get survey via intent parameter to receive recent survey
		// TODO: Use start activity for result to disable button if user took survey
		// Use SurveyMonkey to deal with survey options 
		wvSurvey.loadUrl("https://www.surveymonkey.com/s/HMP398J");
		
		// Add listener
		wvSurvey.setWebViewClient(new SurveyWebViewClient());
	}
	
	private class SurveyWebViewClient extends WebViewClient
	{
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
			}
	    }
	}
} 
