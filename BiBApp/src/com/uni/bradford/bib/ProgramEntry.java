package com.uni.bradford.bib;


public class ProgramEntry
{
	private boolean partOf;
	private String name;
	private String description;
	
	public ProgramEntry(boolean partOf, String name, String description)
	{
		this.setPartOf(partOf);
		this.name = name;
		this.setDescription(description);
	}
	
	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public boolean isPartOf()
	{
		return partOf;
	}

	public void setPartOf(boolean partOf)
	{
		this.partOf = partOf;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}
}
