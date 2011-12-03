package com.thepoofy.constants;

public enum VenueCategoryEnum
{
	FOOD("4d4b7105d754a06374d81259", "restaurants"),	//ANYTHING!
	AMERICAN("4bf58dd8d48988d14e941735","newamerican"),
	FRENCH("4bf58dd8d48988d10c941735","french"),
	ITALIAN("4bf58dd8d48988d110941735","italian"),
	JAPANESE("4bf58dd8d48988d111941735","japanese"),
	MEXICAN("4bf58dd8d48988d1c1941735","mexican"),
	PIZZA("4bf58dd8d48988d1ca941735","pizza"),
	THAI("4bf58dd8d48988d149941735","thai")
	;
	
	
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


	public static VenueCategoryEnum find(String name)
	{
		if(name == null)
		{
			return FOOD;
		}
		
		for(VenueCategoryEnum venue : VenueCategoryEnum.values()){
			if(venue.name().equalsIgnoreCase(name))
			{
				return venue;
			}
			if(venue.getYelpId().equals(name)){
				return venue;
			}
		}
		
		//DEFAULT TO FOOD
		return FOOD;
	}
	
}
