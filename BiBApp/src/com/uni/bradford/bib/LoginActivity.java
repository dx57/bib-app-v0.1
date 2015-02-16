package com.uni.bradford.bib;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

public class LoginActivity extends Activity
{
	// GUI elements
	private Button btnLogin;
	private CheckBox cbRememberMe;
	private TextView tvForgotId;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		// Connect to GUI views
		btnLogin = (Button)findViewById(R.id.btnLogin);
		cbRememberMe = (CheckBox)findViewById(R.id.cbRememberMe);
		tvForgotId = (TextView)findViewById(R.id.tvForgotID);
		
		// Add listener
		btnLogin.setOnClickListener(new OnBtnLoginClickListener());
		cbRememberMe.setOnCheckedChangeListener(new OnCbRememberMeChangeListener());
		tvForgotId.setOnClickListener(new OnTvForgotIdClickListener());
	}
	
	private class OnBtnLoginClickListener implements OnClickListener
	{
		@Override
		public void onClick(View v)
		{
			// TODO: Add behaviour
			System.out.println("Button Login clicked");	
			
			// Change to overview activity 
			Intent changeToOverview = new Intent(LoginActivity.this, OverviewActivity.class);
			changeToOverview.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(changeToOverview);
			
			// Prevent user to go back to splash screen
			LoginActivity.this.finish();
		}	
	}
	
	private class OnTvForgotIdClickListener implements OnClickListener
	{
		@Override
		public void onClick(View v)
		{
			// TODO: Add behaviour
			System.out.println("TextView Login clicked");			
		}	
	}
	
	private class OnCbRememberMeChangeListener implements OnCheckedChangeListener
	{
		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
		{
			// TODO: Add behaviour
			System.out.println("Checker RememberMe clicked state: " + isChecked);
		}	
	}

}
