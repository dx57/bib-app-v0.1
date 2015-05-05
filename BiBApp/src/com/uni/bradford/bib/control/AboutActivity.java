package com.uni.bradford.bib.control;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
 
import com.uni.bradford.bib.R;  

/**
 * Class to visualise information about the Born in Bradford project
 * 
 * @author Martin
 */
public class AboutActivity extends Activity 
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);

		// Change ActionBar color
		ActionBar bar = getActionBar();
		bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#0171bd")));
	}
}
