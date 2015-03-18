package com.uni.bradford.bib;

import java.io.Serializable;

public class DataModel implements Serializable
{
	private static final long serialVersionUID = -2250550860698843034L;

	private double lastUpdate;
	private boolean tookSurvey;
	private boolean rememberUser;
	private String hashedLoginId;
	private String surveyUrl;
	
	private Mother mother;
	
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
