package com.uni.bradford.bib.control;

/**
 * Class to deal with a single row within the ProfileActivity
 * 
 * @author Martin
 */
public class ProgramEntry
{
	private boolean partOf;
	private String name;
	private String description;
	
	/**
	 * Initialise the the profile list entry
	 * 
	 * @param partOf Was part of the study or not
	 * @param name Title of the study
	 * @param description Description of the study
	 */
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
