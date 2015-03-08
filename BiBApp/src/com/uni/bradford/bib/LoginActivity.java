package com.uni.bradford.bib;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.StreamCorruptedException;
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
	private static final String FILE_NAME = "datamodel.dat";
	private boolean rememberUser;
	private DataModel dataModel;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		// Init activity logic
		rememberUser = false;
		
		LoadDataModeFromFilelAsyncTask loadTask = new LoadDataModeFromFilelAsyncTask();
		loadTask.execute();
		
		// TODO: Make loadDataModelFromFile event-based
				
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
			
			boolean passwordCorrect = checkLoginId(etLogin.getText().toString());
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
			// TODO Auto-generated catch block
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
			
	@Override
	protected void onPause()
	{
		super.onPause();
	
		// Save all changes the activity did to the data model
		SaveDataModeToFilelAsyncTask saveTask = new SaveDataModeToFilelAsyncTask(dataModel);
		saveTask.execute();
	}
	
	private class LoadDataModeFromFilelAsyncTask extends AsyncTask<Void, Void, Void>
	{
		private DataModel dataModel;

		@Override
		protected Void doInBackground(Void... params)
		{
			dataModel = null;
			InputStream fileInputStream = null;
			
			File file = new File(LoginActivity.this.getFilesDir(), FILE_NAME);
			if (file.exists())
			{
				try
				{
					fileInputStream = new FileInputStream(file);
					ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
					dataModel = (DataModel)objectInputStream.readObject();
					fileInputStream.close();
				} 
				catch(FileNotFoundException e)
				{
					e.printStackTrace();
				}
				catch(StreamCorruptedException e)
				{
					e.printStackTrace();
				}
				catch(IOException e)
				{
					e.printStackTrace();
				} 
				catch (ClassNotFoundException e)
				{
					e.printStackTrace();
				}			
			}
			
			if (dataModel == null)
			{
				// No local data model instance.. request model from WebService
				// TODO: Request Model form WebService
				dataModel = new DataModel();
				dataModel.setSurveyUrl("www.google.de");
			}
					
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result)
		{
			 // Debug: Show in GUI
			 LoginActivity.this.dataModel = this.dataModel;
			 Toast toast = Toast.makeText(LoginActivity.this, "..loaded", Toast.LENGTH_SHORT);
			 toast.show();
		}
	}
	
	private class SaveDataModeToFilelAsyncTask extends AsyncTask<Void, Void, Void>
	{
		private DataModel dataModel;
		
		public SaveDataModeToFilelAsyncTask(DataModel dataModel)
		{
			this.dataModel = dataModel;
		}
		
		@Override
		protected Void doInBackground(Void... params)
		{
			OutputStream fileOutputStream = null;
			
			File file = new File(LoginActivity.this.getFilesDir(), FILE_NAME);
			
			try
			{
				file.createNewFile();
				fileOutputStream = new FileOutputStream(file);
				ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
				objectOutputStream.writeObject(dataModel);
				fileOutputStream.close();		
			} 
			catch (IOException e)
			{
				e.printStackTrace();
			}
			
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result)
		{
			 // Debug: Show in GUI
			 Toast toast = Toast.makeText(LoginActivity.this, "..saved", Toast.LENGTH_SHORT);
			 toast.show();
		}
	}
}
