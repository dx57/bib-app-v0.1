package com.uni.bradford.bib.control;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.uni.bradford.bib.DataModel;
import com.uni.bradford.bib.R;

/**
 * Class to deal with user interaction for filling in the online survey
 * 
 * @author Martin
 */
public class SurveyActivity extends Activity
{
	// GUI
	private WebView wvSurvey;
	private ProgressBar pbLoadSurvey;
	private TextView tvInfoToSurvey;
	private TextView tvWait;
	private ImageView ivSurveyCompleted;
	
	// Logic
	private DataModel dataModel;
	private static final String SURVEY_COMPLETE = "survey-thanks";
	private boolean connected;
	private BroadcastReceiver networkStateBroadcastReceiver;

	
	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_survey);
		
		// Change ActionBar color
		ActionBar bar = getActionBar();
		bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor(getResources().getString(R.string.bib_blue))));
		
		// Init logic
		LoadDataModelFromFileAsyncTask loadLocalTask = new LoadDataModelFromFileAsyncTask();
		loadLocalTask.execute();
		
		// Init logic
		connected = false;
		networkStateBroadcastReceiver = new NetworkStateBroadcastReceiver();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
		registerReceiver(networkStateBroadcastReceiver, intentFilter);
		
		// Connect to GUI views and setup	
		wvSurvey = (WebView)findViewById(R.id.wvSurvey);
		pbLoadSurvey = (ProgressBar)findViewById(R.id.pbLoadSurvey);
		tvInfoToSurvey = (TextView)findViewById(R.id.tvInfoToSurvey);
		tvWait = (TextView)findViewById(R.id.tvWait);
		ivSurveyCompleted = (ImageView)findViewById(R.id.ivSurveyCompleted);
		
		// Must-Have for SurveyMonkey.. but might allow cross-side-scripting
		wvSurvey.getSettings().setJavaScriptEnabled(true);
		
		// Add listener
		wvSurvey.setWebViewClient(new SurveyWebViewClient());
	}
	
	@Override
	protected void onDestroy()    
	{
		super.onDestroy();
		
		// Do not listen for Internet connection changes
		unregisterReceiver(networkStateBroadcastReceiver);
	}
		
	/**
	 * Change GUI according to available data
	 */
	private void updateGui()
	{
		// Use SurveyMonkey to deal with survey options 
		wvSurvey.loadUrl(dataModel.getSurveyUrl()); 
	}
	
	/**
	 * Create an show dialog to warn user about no Internet connection
	 */
	private void showNoConnectionDialog()
	{	
		// Create and show dialog
		final NoInternetConnectionDialogBuilder noConnectionDialogBuilder = new NoInternetConnectionDialogBuilder(this);
		final AlertDialog alert = noConnectionDialogBuilder.create( );
		alert.show( );
	}
	
	/**
	 * Class to warn user when there is no Internet connection (needed for survey)
	 */
	private class NoInternetConnectionDialogBuilder extends AlertDialog.Builder
	{	
		/**
		 * Build dialog to warn user about no Internet connection
		 * 
		 * @param context Dialog context
		 */
		public NoInternetConnectionDialogBuilder(Context context)
		{
			super(context);
			
			// Configure dialog		
			this.setTitle(getResources().getString(R.string.no_internet_connection));
			this.setMessage(getResources().getString(R.string.no_internet_connection_text));
			
			// Setup buttons and add listener
			this.setNegativeButton(R.string.ok, new OnCancelClickListener());
		}
		
		/**
		 * Class to deal with user input for the dialog
		 */
		private class OnCancelClickListener implements DialogInterface.OnClickListener
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				// Leave online survey
				Intent changeToOverview = new Intent(SurveyActivity.this, OverviewActivity.class);
				changeToOverview.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(changeToOverview);

				// Prevent user to go back to survey screen by clicking back
				SurveyActivity.this.finish();
			}
		} 
	}
	
	/**
	 * Class to control the WebView
	 */
	private class SurveyWebViewClient extends WebViewClient
	{
		@Override
	    public boolean shouldOverrideUrlLoading(WebView  view, String  url)
		{
			if (url.contains(dataModel.getSurveyUrl()) || url.contains(SURVEY_COMPLETE))
			{
				// User started or finished survey.. do react (load new page)
				return false;
			}
			
			// User clicked an unexpected link.. do not react (do not load new page)
	        return true;
	    }
		
		@Override
		public void onPageFinished(WebView view, String url) 
		{
			System.out.println("load " + url + " complete.");
			
			// Only if user starts the survey
			if (!url.contains(SURVEY_COMPLETE))
			{
				// Hide progress bar and show loaded survey if Internet accessible
				tvInfoToSurvey.setVisibility(TextView.INVISIBLE);
				tvWait.setVisibility(TextView.INVISIBLE);
				pbLoadSurvey.setVisibility(ProgressBar.INVISIBLE);
				if (connected)
				{
					wvSurvey.setVisibility(WebView.VISIBLE);
				}
			}
		}
		
		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) 
		{
			System.out.println("started " + url);
			
			// Indicates the end of the survey
			if (url.contains(SURVEY_COMPLETE))
			{
				// Thank user and hide survey view
				wvSurvey.setVisibility(WebView.INVISIBLE);
				ivSurveyCompleted.setVisibility(ImageView.VISIBLE);
				
				// Remember that user took survey to prevent user take survey again 
				dataModel.setTookSurvey(true);
				
				// Save all changes the activity did to the data model
				SaveDataModeToFilelAsyncTask saveTask = new SaveDataModeToFilelAsyncTask();
				saveTask.execute();
			}			
	    }
	}
	
	/**
	 * Class to receive information on Internet availability changes
	 */
	public class NetworkStateBroadcastReceiver extends BroadcastReceiver  
	{
		@Override
		public void onReceive(Context context, Intent intent)
		{
			// Init class to get infos about Internet availability
			ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
			
			// Check for Internet availability
			if (networkInfo != null && networkInfo.isConnected())
			{
				System.out.println("Internet connection.");
				
				wvSurvey.setVisibility(WebView.VISIBLE);
			}
			else
			{
				System.out.println("No Internet connection.");
				
				wvSurvey.setVisibility(WebView.INVISIBLE);
				showNoConnectionDialog();
			}
		}
	}
	
	/**
	 * Class to load local data model
	 */
	private class LoadDataModelFromFileAsyncTask extends AsyncTask<Void, Void, Void>
	{
		@Override
		protected Void doInBackground(Void... params)
		{		
			// Load data model from file
			dataModel = DataModel.loadFromFile(SurveyActivity.this.getFilesDir());
						
			return null; 
		}
		
		@Override
		protected void onPostExecute(Void result)
		{	
			if (dataModel != null)
			{
				// Update GUI
				updateGui();
				
				// Toast toast = Toast.makeText(HeightDiagramActivity.this, "..loaded from file", Toast.LENGTH_SHORT);
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
				dataModel.saveToFile(dataModel, SurveyActivity.this.getFilesDir());
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
