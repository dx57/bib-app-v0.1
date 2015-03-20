package com.uni.bradford.bib;

import java.io.Serializable;
import java.util.ArrayList;

public class Mother implements Serializable
{
	private static final long serialVersionUID = -221146359928224861L;

	private String motherId, description, phoneId;
	private boolean primaryCare;
	private ArrayList<Child> children;
	
	public Mother(String motherId, String description, String phoneId, boolean primaryCare)
	{
		this.motherId = motherId;
		this.description = description;
		this.phoneId = phoneId;
		this.primaryCare = primaryCare;
		
		this.children = new ArrayList<Child>();
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
	
	public Child getChild(int position)
	{
		return this.children.get(position);
	}
}
