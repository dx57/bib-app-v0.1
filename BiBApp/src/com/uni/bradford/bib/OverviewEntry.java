package com.uni.bradford.bib;

public class OverviewEntry
{
	private String name;
	private String information;
	private int drawableRef;
	
	public OverviewEntry(String name, String information, int drawableRef)
	{
		this.name = name;
		this.setInformation(information);
		this.setDrawableRef(drawableRef);
	}
	
	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getInformation()
	{
		return information;
	}

	public void setInformation(String information)
	{
		this.information = information;
	}

	public int getDrawableRef()
	{
		return drawableRef;
	}

	public void setDrawableRef(int drawableRef)
	{
		this.drawableRef = drawableRef;
	}
}
