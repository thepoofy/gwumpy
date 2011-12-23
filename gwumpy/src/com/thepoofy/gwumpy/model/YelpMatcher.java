package com.thepoofy.gwumpy.model;

import java.util.List;
import java.util.logging.Logger;

import com.thepoofy.gwumpy.yelp.model.Business;
import com.williamvanderhoef.foursquare.model.Venue;

public class YelpMatcher
{
	private static final Logger log = Logger.getLogger(YelpMatcher.class.getName());
	
	private Venue v;	//the foursquare venue to compare with
	
	public YelpMatcher(Venue v)
	{
		this.v = v;
		
	}
	
	/**
	 * 
	 * @param v
	 * @param yelp
	 */
	public Business findSimilarYelpPlace(List<Business> yelp)
	{
		int bestScore = 0;
		Business bestBiz = null;
		
		for(Business b : yelp)
		{
			int score = 0;
			score += comparePostalCode(b);
			score += compareName(b);
			score += compareAddress(b);
			
			if(score > bestScore)
			{
//				log.info(v.getName()+" : "+score);
				bestBiz = b;
				bestScore = score;
			}
		}
		
//		if(bestBiz != null)
//		{
//			log.info(bestBiz.getName()+" : "+bestScore);
//		}
//		else
//		{
//			log.info("no match for "+v.getName());
//		}
		
		return bestBiz;
	}
	
	
	private int comparePostalCode(Business b)
	{
		if (v.getLocation() != null 
				&& v.getLocation().getPostalCode() != null
				&& b.getLocation() != null
				&& b.getLocation().getPostal_code() != null)
		{
			return (b.getLocation().getPostal_code().substring(0, 5).equals(
					v.getLocation().getPostalCode().substring(0, 5))? 1: -10);
		}
		
		return 0;
	}
	
	private int compareName(Business b)
	{
		int score = 0;
		
		if (v.getName() != null
				&& b.getName() != null)
		{
			if(b.getName().equalsIgnoreCase(v.getName()))	//exact name match
				score+=10;
			else if( b.getName().startsWith(v.getName()))	//one name starts with the other name
				score +=5;
			else if( v.getName().startsWith(b.getName()))	//one name starts with the other name
				score +=5;
			else if( b.getName().toLowerCase().contains(v.getName().toLowerCase()))		//case ignorant search for the foursquare name within the yelp name
				score +=3;
			
			
		}
		
		return score;
	}
	
	private int compareAddress(Business b)
	{
		if (v.getLocation() != null 
				&& v.getLocation().getAddress() != null
				&& b.getLocation() != null
				&& b.getLocation().getAddress().size() > 0
				&& b.getLocation().getAddress() != null)
		{
			return (v.getLocation().getAddress().equals(
					b.getLocation().getAddress().get(0))? 20: -5);
		}
		
		return 0;
	}
}
