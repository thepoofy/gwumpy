package com.thepoofy.gwumpy.model;

import java.util.ArrayList;
import java.util.List;

import com.williamvanderhoef.foursquare.model.ImageDefinition;
import com.williamvanderhoef.foursquare.model.Venue;
import com.williamvanderhoef.foursquare.model.subtypes.Category;
import com.williamvanderhoef.foursquare.responses.VenueSearchResponse;

public class VenueSummary
{
	//name
	private String name;
	//distance formatted for the user
	private Double distance;
	//address
	private String address;

	//category
	private String category;
	private String categoryImgUrl;

	//people count
	private Integer hereNowCount;
	
	
	//DETAILS
	//star rating
	private String starRating;
	private String starRatingImgUrl;
	//$$$
	private String costRating;
	
	//ny times review
	private String nytimesReview;
	
	//health code rating
	private String healthCodeRating;
	
	private static VenueSummary valueOf(Venue v)
	{
		VenueSummary vs = new VenueSummary();
		
		vs.setName(v.getName());
		
		vs.setAddress(v.getLocation().getAddress());
		vs.setDistance(v.getLocation().getDistance());
		
		Category cat = v.getCategories().get(0);
		ImageDefinition imgDef = cat.getIcon();
		String img = imgDef.getPrefix()+imgDef.getSizes().get(0)+imgDef.getName();
		vs.setCategory(cat.getShortName());
		vs.setCategoryImgUrl(img);
		
		vs.setHereNowCount(v.getHereNow().getCount());
		
		return vs;
	}
	
	/**
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 * @return the distance
	 */
	public Double getDistance()
	{
		return distance;
	}

	/**
	 * @param distance the distance to set
	 */
	public void setDistance(Double distance)
	{
		this.distance = distance;
	}

	/**
	 * @return the address
	 */
	public String getAddress()
	{
		return address;
	}

	/**
	 * @param address the address to set
	 */
	public void setAddress(String address)
	{
		this.address = address;
	}

	/**
	 * @return the category
	 */
	public String getCategory()
	{
		return category;
	}

	/**
	 * @param category the category to set
	 */
	public void setCategory(String category)
	{
		this.category = category;
	}

	/**
	 * @return the categoryImgUrl
	 */
	public String getCategoryImgUrl()
	{
		return categoryImgUrl;
	}

	/**
	 * @param categoryImgUrl the categoryImgUrl to set
	 */
	public void setCategoryImgUrl(String categoryImgUrl)
	{
		this.categoryImgUrl = categoryImgUrl;
	}

	/**
	 * @return the hereNowCount
	 */
	public Integer getHereNowCount()
	{
		return hereNowCount;
	}

	/**
	 * @param hereNowCount the hereNowCount to set
	 */
	public void setHereNowCount(Integer hereNowCount)
	{
		this.hereNowCount = hereNowCount;
	}

	/**
	 * @return the starRating
	 */
	public String getStarRating()
	{
		return starRating;
	}

	/**
	 * @param starRating the starRating to set
	 */
	public void setStarRating(String starRating)
	{
		this.starRating = starRating;
	}

	/**
	 * @return the starRatingImgUrl
	 */
	public String getStarRatingImgUrl()
	{
		return starRatingImgUrl;
	}

	/**
	 * @param starRatingImgUrl the starRatingImgUrl to set
	 */
	public void setStarRatingImgUrl(String starRatingImgUrl)
	{
		this.starRatingImgUrl = starRatingImgUrl;
	}

	/**
	 * @return the costRating
	 */
	public String getCostRating()
	{
		return costRating;
	}

	/**
	 * @param costRating the costRating to set
	 */
	public void setCostRating(String costRating)
	{
		this.costRating = costRating;
	}

	/**
	 * @return the nytimesReview
	 */
	public String getNytimesReview()
	{
		return nytimesReview;
	}

	/**
	 * @param nytimesReview the nytimesReview to set
	 */
	public void setNytimesReview(String nytimesReview)
	{
		this.nytimesReview = nytimesReview;
	}

	/**
	 * @return the healthCodeRating
	 */
	public String getHealthCodeRating()
	{
		return healthCodeRating;
	}

	/**
	 * @param healthCodeRating the healthCodeRating to set
	 */
	public void setHealthCodeRating(String healthCodeRating)
	{
		this.healthCodeRating = healthCodeRating;
	}

	public static List<VenueSummary> adaptVenueList(VenueSearchResponse vsr)
	{
		List<VenueSummary> venues = new ArrayList<VenueSummary>();
		if(vsr == null || vsr.getVenues() == null || vsr.getVenues().isEmpty())
		{
			return venues;
		}
		
		for(Venue v : vsr.getVenues())
		{
			venues.add(valueOf(v));
		}
		
		return venues;
	}
	
}
