package com.uni.bradford.bib;

import android.content.Intent;

public class OverviewEntry
{
	private String name;
	private int drawableRef;
	private Intent intent;
	private int requestCode;
	
	public OverviewEntry(String name, int drawableRef, Intent intent, int requestCode)
	{
		this.name = name;
		this.setDrawableRef(drawableRef);
		this.setIntent(intent);
		this.setRequestCode(requestCode);
	}
	
	public OverviewEntry(String name, int drawableRef, Intent intent)
	{
		this(name, drawableRef, intent, 0);
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

	public int getRequestCode()
	{
		return requestCode;
	}

	public void setRequestCode(int requestCode)
	{
		this.requestCode = requestCode;
	}
}
