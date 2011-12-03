package com.thepoofy.util;

public class Location
{
	private Double latitude;
	private Double longitude;
	private Integer accuracy;
	
	/**
	 * @return the accuracy
	 */
	public Integer getAccuracy()
	{
		return accuracy;
	}
	/**
	 * @param accuracy the accuracy to set
	 */
	public void setAccuracy(Integer accuracy)
	{
		this.accuracy = accuracy;
	}
	/**
	 * @return the latitude
	 */
	public Double getLatitude()
	{
		return latitude;
	}
	/**
	 * @param latitude the latitude to set
	 */
	public void setLatitude(Double latitude)
	{
		this.latitude = latitude;
	}
	/**
	 * @return the longitude
	 */
	public Double getLongitude()
	{
		return longitude;
	}
	/**
	 * @param longitude the longitude to set
	 */
	public void setLongitude(Double longitude)
	{
		this.longitude = longitude;
	}
	
	
}
