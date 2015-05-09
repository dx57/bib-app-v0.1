package com.uni.bradford.bib.model;

import java.io.Serializable;

/**
 * Class to structure all data related to ChildData
 * 
 * @author Martin
 */
public class ChildData implements Serializable
{
	private static final long serialVersionUID = 1110741657843195784L;

	private String source;
    private int ageDays;
    private Double weight, height, bmi;
    
    /**
	 * Init ChildData
	 * 
	 * @param source Source of this measure
	 * @param ageDays Age of the child at this measure
	 * @param weight Weight of the child
	 * @param height Height of the child
	 * @param bmi BMI of the child
	 */
    public ChildData(String source, int ageDays, Double weight, Double height, Double bmi)
    {
    	this.source = source;
    	this.ageDays = ageDays;
    	this.weight = weight;
    	this.height = height;
    	this.bmi = bmi;
    }

	public String getSource()
	{
		return source;
	}

	public int getAgeDays()
	{
		return ageDays;
	}

	public Double getWeight()
	{
		return weight;
	}

	public Double getHeight()
	{
		return height;
	}

	public Double getBmi()
	{
		return bmi;
	}
}
