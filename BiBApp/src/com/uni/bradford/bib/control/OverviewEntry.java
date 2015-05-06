package com.uni.bradford.bib.control;

import android.content.Intent;

/**
 * Class to deal with a single row within the ActivityOverview
 * 
 * @author Martin
 */
public class OverviewEntry
{
	private String name;
	private int drawableRef;
	private Intent intent;
	
	/**
	 * Initialise the the overview list entry
	 * 
	 * @param name Title of the linked screen
	 * @param drawableRef Reference on Drawable resource for screen icon
	 * @param intent Intent reference for the correlated Activity
	 */
	public OverviewEntry(String name, int drawableRef, Intent intent)
	{
		this.name = name;
		this.setDrawableRef(drawableRef);
		this.setIntent(intent);
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

	public Intent getIntent()
	{
		return intent;
	}

	public void setIntent(Intent intent)
	{
		this.intent = intent;
	}
}
