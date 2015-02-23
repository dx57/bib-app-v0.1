package com.uni.bradford.bib;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.webkit.WebView;

public class SurveyActivity extends Activity
{
	private WebView wvSurvey;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_survey);
		
		// Change ActionBar color and icon
		ActionBar bar = getActionBar();
		bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#0171bd")));
		bar.setIcon(R.drawable.ic_survey_white);
		
		// Connect to GUI views
		wvSurvey = (WebView)findViewById(R.id.wvSurvey);
		
		// Must-Have for SurveyMonkey
		wvSurvey.getSettings().setJavaScriptEnabled(true);
		
		// TODO: Ask David how to connect to a survey
		wvSurvey.loadUrl("https://www.surveymonkey.com/r/?sm=d9yyh03hx%2fRxh26ptsvay03MP0ZkErSidp5ni5TkqGw%3d");
	}
} 
