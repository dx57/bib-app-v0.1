package com.uni.bradford.bib;

public class OverviewEntry
{
	private String name;
	private int drawableRef;
	
	public OverviewEntry(String name, int drawableRef)
	{
		this.name = name;
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

	public int getDrawableRef()
	{
		return drawableRef;
	}

	public void setDrawableRef(int drawableRef)
	{
		this.drawableRef = drawableRef;
	}
}
