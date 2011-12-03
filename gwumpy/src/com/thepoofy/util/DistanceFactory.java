package com.thepoofy.util;


public class DistanceFactory
{
	
	public static Distance create(int meters)
	{
		//TODO use context to find user preference for Metric/Imperial
		
		return new MetricDistance(meters);
	}
	

}
