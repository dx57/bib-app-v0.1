package com.uni.bradford.bib;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
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
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class LoginActivity extends Activity
{
	// GUI elements
	private Button btnLogin;
	private CheckBox cbRememberMe;
	private TextView tvForgotId;
	private EditText etLogin;
	
	// Logic
	private DataModel dataModel;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		LoadDataModelFromFileAsyncTask loadLocalTask = new LoadDataModelFromFileAsyncTask();
		loadLocalTask.execute();
		
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
		public void onClick(View view)
		{
			System.out.println("Button Login clicked");	
									
			// TODO: To make it secure, WebService has to be able to send messages without asking for the motherID!!!
			// boolean passwordCorrect = checkLoginId(etLogin.getText().toString());
			
			// TODO: If dataModel is null, we need a quick soap request to ask, if the loginID is correct (which is blocking)
			// TODO: If the check results in an ok response then we load the other data
			if ( (dataModel.getMother().getMotherId().equals(etLogin.getText().toString())) /* || (blockingLogincheckFunctionCall) */ )
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
	
	private boolean checkLoginId(String loginId)
	{
		try
		{
			// TODO: Load from file or receive from WebService
			String storedLoginId = "88d4266fd4e6338d13b845fcf289579d209c897823b9217da3e161936f031589";

			MessageDigest md = MessageDigest.getInstance("SHA-256");
			md.update(loginId.getBytes());
			byte byteData[] = md.digest();
			
			//convert the byte to hex format method 1
	        StringBuffer sb = new StringBuffer();
	        for (int i = 0; i < byteData.length; i++) 
	        {
	        	sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
	        }
	        
	        System.out.println("Strored hash: " + sb.toString());
	        
	        if(storedLoginId.equals(sb.toString()))
	        {
	        	return true;
	        }
		} 
		catch (NoSuchAlgorithmException e)
		{
			e.printStackTrace();
		}
		
		return false;
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
				// Get phoneId to attend in message
			    TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
				String phoneId = telephonyManager.getDeviceId(); 
			    
				// Send mail without blocking the GUI
				// TODO: Get sender, password and receiver from data model.. data model gets information from WebService
			    // TODO: Maybe also check for Internet connection?
				SendEmail sendEmail = new SendEmail("stellaleeuss@gmail.com", 
			    									"AskMeAgain", 
			    									"mjwalda@bradford.ac.uk",
			    									getResources().getString(R.string.fogot_id_mail_subject),
			    									getResources().getString(R.string.fogot_id_mail_text) + " " + phoneId + ".");
			    sendEmail.execute();
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
			
			dataModel.setRememberUser(isChecked);
		}	
	}
			
	@Override
	protected void onPause()
	{
		super.onPause();
	
		// Save all changes the activity did to the data model
		SaveDataModeToFilelAsyncTask saveTask = new SaveDataModeToFilelAsyncTask();
		saveTask.execute();
	}
	
	private class LoadDataModelFromFileAsyncTask extends AsyncTask<Void, Void, Void>
	{
		@Override
		protected Void doInBackground(Void... params)
		{		
			dataModel = DataModel.loadFromFile(LoginActivity.this.getFilesDir());
			
			if (dataModel == null)
			{
				LoadDataModelFromWebServiceAsyncTask loadRemoteTask = new LoadDataModelFromWebServiceAsyncTask();
				loadRemoteTask.execute();
			}
			
			return null; 
		}
		
		@Override
		protected void onPostExecute(Void result)
		{	
			// TODO: Only try to load dataModel from file.. because we do not know the loginId at this point
			if (dataModel != null)
			{
				// Update GUI
				cbRememberMe.setChecked(dataModel.isRememberUser());
	
				Toast toast = Toast.makeText(LoginActivity.this, "..loaded from file", Toast.LENGTH_SHORT);
				toast.show();
			}
		}
	}

	private class LoadDataModelFromWebServiceAsyncTask extends AsyncTask<Void, Void, Void>
	{
		@Override
		protected Void doInBackground(Void... params)
		{		
			// Get phoneId
			TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
			String deviceId = telephonyManager.getDeviceId(); 	
			
			dataModel = DataModel.loadFromWebService(deviceId, "B100001");

			return null; 
		}

		@Override
		protected void onPostExecute(Void result)
		{
			if (dataModel != null)
			{
				// Update GUI
				cbRememberMe.setChecked(dataModel.isRememberUser());
			}
			Toast toast = Toast.makeText(LoginActivity.this, "..loaded from WebService", Toast.LENGTH_SHORT);
			toast.show();
		}
	}
	
	private class SaveDataModeToFilelAsyncTask extends AsyncTask<Void, Void, Void>
	{			
		@Override
		protected Void doInBackground(Void... params)
		{	
			dataModel.saveToFile(dataModel, LoginActivity.this.getFilesDir());
			
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result)
		{
			 // Debug: Show in GUI
			 Toast toast = Toast.makeText(LoginActivity.this, "..saved to file", Toast.LENGTH_SHORT);
			 toast.show();
		}
	}
	
	private class SendEmail extends AsyncTask<Void, Void, Void> 
	{
		private String sender;
		private String senderPassword;
		private String receiver;
		private String subject;
		private String text;
	
		public SendEmail(String sender, String senderPassword, String receiver, String subject, String text)
		{
			this.sender = sender;
			this.senderPassword = senderPassword;
			this.receiver = receiver;
			this.subject = subject;
			this.text = text;
		}
		
		@Override
		protected Void doInBackground(Void... params) 
		{
			try 
			{   
				// Pass on mail arguments
				GMailSender gMailSender = new GMailSender(sender, senderPassword);
				gMailSender.sendMail(receiver, subject, text);
			} 
			catch (Exception e) 
			{   
				e.printStackTrace();
			} 

			return null;
		}
		
		protected void onPostExecute(Void result) 
		{
			// Debug: Show in GUI
			 Toast toast = Toast.makeText(LoginActivity.this, "..sent mail", Toast.LENGTH_SHORT);
			 toast.show();
		}
	}
}
