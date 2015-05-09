package com.uni.bradford.bib.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Class to structure all data related to a mother
 * 
 * @author Martin
 */
public class Mother implements Serializable
{
	private static final long serialVersionUID = -221146359928224861L;

	private String motherId, description, phoneId;
	private boolean primaryCare;
	private ArrayList<Child> children;
	
	/**
	 * Init mother
	 * 
	 * @param motherId Unique mother identification
	 * @param description Mother description
	 * @param phoneId Unique phone identification
	 * @param primaryCare Tells whether or not mother was part of the primaryCare study
	 */
	public Mother(String motherId, String description, String phoneId, boolean primaryCare)
	{
		this.motherId = motherId;
		this.description = description;
		this.phoneId = phoneId;
		this.primaryCare = primaryCare;
		
		this.children = new ArrayList<Child>();
	}
	
	/**
	 * Load data from local file
	 * 
	 * @param childID Child identification
	 * @return Position of the stated childID within the list
	 */
	public int getChildPos(String childID)
	{
		for (int i = 0; i < children.size(); i++)
		{
			if (children.get(i).getChildId().equals(childID))
			{
				return i;
			}
		}
		
		return -1;
	}

	public String getMotherId()
	{
		return motherId;
	}

	public String getDescription()
	{
		return description;
	}

	public String getPhoneId()
	{
		return phoneId;
	}

	public boolean getPrimaryCare()
	{
		return primaryCare;
	}
	
	public int getChildCount()
	{
		return this.children.size();
	}
	
	public void addChild(Child newChild)
	{
		this.children.add(newChild);
	}
	
	public Child getChild(int position)
	{
		return this.children.get(position);
	}
}
