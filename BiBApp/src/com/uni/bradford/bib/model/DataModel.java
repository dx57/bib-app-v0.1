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

import com.uni.bradford.bib.WebServiceInteraction;

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
	
	private double lastUpdate;
	private boolean tookSurvey;
	private boolean rememberUser;
	private String hashedLoginId;
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
		dataModel.setSurveyUrl("https://www.surveymonkey.com/s/HMP398J"); // TODO: Load from WebService
							
		WebServiceInteraction wsi = new WebServiceInteraction(dataModel);
		
		// TODO: vs the all in one approach!!!
//		if (!wsi.getAverageChildBySex("0"))
//		{
//			// Wrong loginId or phoneId
//			return null;
//		}
		
		if (wsi.getMotherById(loginId, deviceId))
		{
			// Correct loginId and phoneId pair
			if (wsi.getChildIdByMotherId(loginId))
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
	
	public double getLastUpdate()
	{
		return lastUpdate;
	}
	
	public void setLastUpdate(double lastUpdate)
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
}