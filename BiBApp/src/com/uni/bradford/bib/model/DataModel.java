package com.uni.bradford.bib.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.io.StreamCorruptedException;

import android.os.SystemClock;

/**
 * Class to structure all data
 * 
 * @author Martin
 */
public class DataModel implements Serializable
{
	private static final long serialVersionUID = -2250550860698843034L;

	public static final String FILE_NAME = "datamodel.dat";
	public static final int averageYear = 360;
	public static final int averageMonth = 30;
	
	// data from http://www.healthforallchildren.com/wp-content/uploads/2013/07/136637-Boys-Duo-Chart-4.jpg
											     /* m0,    m2,    m4,    m6,    m8,   m10 */
	public static final double[] AVERAGE_BOY  = { 51.5,  58.5,  64.0,  68.0,  71.0,  73.5, /*  0 year */
											 	  76.0,  78.0,  80.0,  82.0,  84.0,  85.5, /*  1 year */
												  87.0,  88.5,  90.0,  91.5,  93.0,  94.0, /*  2 year */
												  95.5,  97.0,  98.0,  99.0, 100.5, 101.5, /*  3 year */
												 102.5, 103.7, 105.0, 106.0, 107.4, 108.5, /*  4 year */
												 109.8, 110.9, 112.0, 113.0, 114.0, 115.0, /*  5 year */
												 116.0, 117.0, 118.0, 119.0, 120.0, 121.0, /*  6 year */
												 122.0, 123.0, 124.0, 125.0, 126.0, 127.0, /*  7 year */
												 127.9, 128.9, 129.9, 130.6, 131.5, 132.5, /*  8 year */
												 133.4, 134.2, 135.0, 135.9, 136.6, 137.5, /*  9 year */
												 138.4, 139.4, 140.1, 141.0, 141.9, 142.8  /* 10 year */
											    }; 
	
	// data from http://www.healthforallchildren.com/wp-content/uploads/2013/07/138664-Girls-Duo-Chart-4.jpg
    										    /* m0,    m2,    m4,    m6,    m8,   m10 */
	public static final double[] AVERAGE_GIRL = { 50.5,  56.0,  62.0,  66.0,  69.0,  71.5,  /*  0 year */
												  74.0,  76.5,  78.6,  80.7,  82.7,  84.4,  /*  1 year */
												  86.0,  87.5,  89.0,  90.5,  91.9,  93.1,  /*  2 year */
												  94.7,  95.9,  97.0,  98.1,  99.3, 100.4,  /*  3 year */
												 101.6, 102.8, 104.0, 105.2, 106.4, 107.7,  /*  4 year */
												 109.0, 110.0, 111.1, 112.2, 113.4, 114.3,  /*  5 year */
												 115.5, 116.4, 117.3, 118.2, 119.3, 120.3,  /*  6 year */
												 121.3, 122.3, 123.4, 124.3, 125.4, 126.5,  /*  7 year */
												 127.4, 128.4, 129.2, 130.1, 131.0, 132.0,  /*  8 year */
												 132.9, 133.8, 134.7, 135.7, 136.6, 137.5,  /*  9 year */
												 138.5, 139.5, 140.4, 141.3, 142.2, 143.1   /* 10 year */
											    };
	
	private long lastUpdate;
	private boolean tookSurvey;
	private boolean rememberUser;
	private String hashedLoginId;
	private String recipientMail;
	private String surveyUrl; 
	
	private Mother mother;
	
	/**
	 * Load data from local file
	 * 
	 * @param fileDir File directory for local file with serialised data model
	 */
	public static DataModel loadFromFile(File fileDir)
	{
		DataModel dataModel;
		
		InputStream fileInputStream = null;
		
		// Specify file
		File file = new File(fileDir, DataModel.FILE_NAME);
		if (file.exists()) 
		{
			// Load from file
			try
			{
				fileInputStream = new FileInputStream(file);
				ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
				dataModel = (DataModel)objectInputStream.readObject();
				fileInputStream.close();
				return dataModel;
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
		
		return null;
	}
	
	/**
	 * Save data to local file
	 * 
	 * @param dataModel Data model which should get saved
	 * @param fileDir File directory for local file with serialised data model
	 */
	public void saveToFile(DataModel dataModel, File fileDir)
	{
		OutputStream fileOutputStream = null;
		
		File file = new File(fileDir, DataModel.FILE_NAME);
		
		// Save to file
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
	}
	
	/**
	 * Load data from Webservice
	 * 
	 * @param deviceId Device ID to identify phone
	 * @param loginId Login ID to identify user
	 */
	public static DataModel loadFromWebService(String deviceId, String loginId)
	{
		// No local data model instance.. Load from WebService (first App start with Internet connection)
		
		// Debug: Measure time to init data model through WebService
		long start = SystemClock.uptimeMillis();
		
		DataModel dataModel = new DataModel();
							
		WebServiceInteraction wsi = new WebServiceInteraction(dataModel);
				
		// Ask for date of last database update
		wsi.getLastUpdate();
		
		// Ask for recipient mail to deal with login id requests
		wsi.getRecipientMail();
		
		// Ask for survey link
		wsi.getSurveyUrl();
		
		if (wsi.getMotherById(loginId, deviceId))
		{
			// Correct loginId and phoneId pair
			if (wsi.getChildIdByMotherId(dataModel.getMother().getMotherId()))
			{
				// Mother with children
				for (int i = 0; i < dataModel.getMother().getChildCount(); i++)
				{
					wsi.getChildGrowthById(dataModel.getMother().getChild(i).getChildId());
				}
			}
		}
		else
		{
			// Wrong loginId or phoneId
			return null;
		}
		
		// Debug: Measure time to init datamodel through WebService
		System.out.println("Initialisation with WebService took: " + (SystemClock.uptimeMillis() - start));
		
		return dataModel;
	}
	
	/**
	 * Load last update date from Webservice
	 * 
	 * @return Last time, the database was updated
	 */
	public static double checkForLastUpdate()
	{
		DataModel dataModel = new DataModel();
							
		WebServiceInteraction wsi = new WebServiceInteraction(dataModel);
		
		// Ask for date of last database update
		wsi.getLastUpdate();
		
		return dataModel.getLastUpdate();
	}
	
	public long getLastUpdate()
	{
		return lastUpdate;
	}
	
	public void setLastUpdate(long lastUpdate)
	{
		this.lastUpdate = lastUpdate;
	}

	public boolean isTookSurvey()
	{
		return tookSurvey;
	}

	public void setTookSurvey(boolean tookSurvey)
	{
		this.tookSurvey = tookSurvey;
	}

	public boolean isRememberUser()
	{
		return rememberUser;
	}

	public void setRememberUser(boolean rememberUser)
	{
		this.rememberUser = rememberUser;
	}

	public String getHashedLoginId()
	{
		return hashedLoginId;
	}

	public void setHashedLoginId(String hashedLoginId)
	{
		this.hashedLoginId = hashedLoginId;
	}

	public String getSurveyUrl()
	{
		return surveyUrl;
	}

	public void setSurveyUrl(String surveyUrl)
	{
		this.surveyUrl = surveyUrl;
	}

	public Mother getMother()
	{
		return mother;
	}

	public void setMother(Mother mother)
	{
		this.mother = mother;
	}

	public String getRecipientMail()
	{
		return recipientMail;
	}

	public void setRecipientMail(String recipientMail)
	{
		this.recipientMail = recipientMail;
	}
}
