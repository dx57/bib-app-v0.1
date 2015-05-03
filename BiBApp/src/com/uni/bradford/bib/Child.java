package com.uni.bradford.bib;

import java.io.Serializable;
import java.util.ArrayList;

public class Child implements Serializable
{
	private static final long serialVersionUID = 5856826193060713777L;

	private String childId, pregnancy;
	private short birthOrder, genderId;
	private int monthOfBirth, yearOfBirth, maternalBaseQ, eclipse, primaryCare, education, bib1000, meDall, all_in;
    
    private ArrayList<ChildData> childData;
    
    public Child(String childId, 
    			 String pregnancy, 
    			 short birthOrder, 
    			 short genderId, 
    			 int monthOfBirth, 
    			 int yearOfBirth, 
    			 int maternalBaseQ, 
    			 int eclipse, 
    			 int primaryCare, 
    			 int education, 
    			 int bib1000, 
    			 int meDall, 
    			 int all_in)
    {
    	this.childId = childId;
		this.pregnancy = pregnancy;
		this.birthOrder = birthOrder;
		this.genderId = genderId;
		this.monthOfBirth = monthOfBirth; 
		this.yearOfBirth = yearOfBirth;
		this.maternalBaseQ = maternalBaseQ; 
		this.eclipse = eclipse;
		this.primaryCare = primaryCare; 
		this.education = education;
		this.bib1000 = bib1000;
		this.meDall = meDall; 
		this.all_in = all_in;
		
		this.childData = new ArrayList<ChildData>();
    }

    public String getIdentifier()
    {
    	String identifier = yearOfBirth + "-" + monthOfBirth;
    	
    	if (birthOrder > 1)
		{
			// Add counter to differ twins etc.
    		identifier += " [" + birthOrder + "]";				
		}
    	
    	return identifier;
    }
    
	public String getChildId()
	{
		return childId;
	}

	public String getPregnancy()
	{
		return pregnancy;
	}

	public short getBirthOrder()
	{
		return birthOrder;
	}

	public short getGenderId()
	{
		return genderId;
	}

	public int getMonthOfBirth()
	{
		return monthOfBirth;
	}

	public int getYearOfBirth()
	{
		return yearOfBirth;
	}

	public int getMaternalBaseQ()
	{
		return maternalBaseQ;
	}

	public int getEclipse()
	{
		return eclipse;
	}

	public int getPrimaryCare()
	{
		return primaryCare;
	}

	public int getEducation()
	{
		return education;
	}

	public int getBib1000()
	{
		return bib1000;
	}

	public int getMeDall()
	{
		return meDall;
	}

	public int getAll_in()
	{
		return all_in;
	}
	
	public void addChildData(ChildData newChildData)
	{		
		this.childData.add(newChildData);
	}
	
	public ChildData getChildData(int position)
	{		
		return this.childData.get(position);
	}
	
	public ChildData getLastChildData()
	{		
		return this.childData.get(this.childData.size()-1);
	}
}
