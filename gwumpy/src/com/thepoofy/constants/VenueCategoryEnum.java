package com.thepoofy.constants;

public enum VenueCategoryEnum
{
	FOOD("", "food");

	private final String fsqId;
	private final String yelpId;
	
	private VenueCategoryEnum(String fsqId, String yelpId)
	{
		this.fsqId = fsqId;
		this.yelpId = yelpId;
	}

	/**
	 * @return the fsqId
	 */
	public String getFsqId()
	{
		return fsqId;
	}

	/**
	 * @return the yelpId
	 */
	public String getYelpId()
	{
		return yelpId;
	}

	
}
