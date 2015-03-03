package com.uni.bradford.bib;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends Activity
{
	// GUI elements
	private Button btnLogin;
	private CheckBox cbRememberMe;
	private TextView tvForgotId;
	private EditText etLogin;
	
	// Logic
	private boolean rememberUser;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		// Init activity logic
		rememberUser = false;
		
		// Connect to GUI views
		btnLogin = (Button)findViewById(R.id.btnLogin);
		cbRememberMe = (CheckBox)findViewById(R.id.cbRememberMe);
		tvForgotId = (TextView)findViewById(R.id.tvForgotID);
		etLogin = (EditText)findViewById(R.id.etLogin);
		
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
			
			boolean passwordCorrect = true;
			if (passwordCorrect)
			{
				// Change to overview activity 
				Intent changeToOverview = new Intent(LoginActivity.this, OverviewActivity.class);
				changeToOverview.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(changeToOverview);

				// Prevent user to go back to splash screen
				LoginActivity.this.finish();
			}
			else
			{
				showWrongPasswordDialog();
			}
		}	
	}
	
	private class OnTvForgotIdClickListener implements OnClickListener
	{
		@Override
		public void onClick(View view)
		{							    
		    showForgotIdDialog();
		}	
	}
	
	private class ForgotIdDialogBuilder extends AlertDialog.Builder
	{	
		public ForgotIdDialogBuilder(Context context)
		{
			super(context);
			
			// Configure dialog		
			this.setTitle(getResources().getString(R.string.forgot_login_id));
			this.setMessage(getResources().getString(R.string.forgot_login_id_text));
			
			// Setup buttons and add listener
			this.setPositiveButton(R.string.mail_button, new OnSendLoginIdRequestClickListener());
			this.setNegativeButton(R.string.cancel, new OnCancelClickListener());
		}
		
		private class OnSendLoginIdRequestClickListener implements DialogInterface.OnClickListener
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				// TODO: Try OmaGotchi approach for automated mail
				// TODO: Change to BiB address!
				String[] reciverList = new String[]{ "mjwalda@bradford.ac.uk" }; 
				
				Intent intent = new Intent(Intent.ACTION_SENDTO);
				intent.setType("plain/text");
				
				// Only email Apps should handle this
			    intent.setData(Uri.parse("mailto:")); 
			    intent.putExtra(Intent.EXTRA_EMAIL, reciverList);
			    
			    // Get phoneId to attend in message
			    TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
				String phoneId = telephonyManager.getDeviceId(); 
			    
				// Define mail content
			    intent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.fogot_id_mail_subject));
			    intent.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.fogot_id_mail_text) + " " + phoneId + ".");
			    
			    // Only start intent if there is an App to handle it 
			    if (intent.resolveActivity(getPackageManager()) != null) 
			    {
			        startActivity(intent);
			    }
			}
		} 
		
		private class OnCancelClickListener implements DialogInterface.OnClickListener
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				// Nothing to do
			}
		} 
	}
	
	private void showForgotIdDialog()
	{	
		// Create and show dialog
		final ForgotIdDialogBuilder loginDialogBuilder = new ForgotIdDialogBuilder(this);
		final AlertDialog alert = loginDialogBuilder.create( );
		alert.show( );
	}
	
	private class WrongPasswordDialogBuilder extends AlertDialog.Builder
	{	
		public WrongPasswordDialogBuilder(Context context)
		{
			super(context);
			
			// Configure dialog		
			this.setTitle(getResources().getString(R.string.wrong_password));
			this.setMessage(getResources().getString(R.string.wrong_password_text));
			
			// Setup buttons and add listener
			this.setNegativeButton(R.string.try_again, new OnCancelClickListener());
		}
		
		private class OnCancelClickListener implements DialogInterface.OnClickListener
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				// Reset text input
				etLogin.setText("");
			}
		} 
	}
	
	private void showWrongPasswordDialog()
	{	
		// Create and show dialog
		final WrongPasswordDialogBuilder loginDialogBuilder = new WrongPasswordDialogBuilder(this);
		final AlertDialog alert = loginDialogBuilder.create( );
		alert.show( );
	}
	
	private class OnCbRememberMeChangeListener implements OnCheckedChangeListener
	{
		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
		{
			System.out.println("Checker RememberMe clicked state: " + isChecked);
			
			rememberUser = isChecked;
		}	
	}
}
