/**
 * 
 */
package com.thepoofy.gwumpy.servlet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.thepoofy.ca.dao.foursquare.VenuesDao;
import com.thepoofy.constants.VenueCategoryEnum;
import com.thepoofy.gwumpy.yelp.Yelp;
import com.thepoofy.gwumpy.yelp.model.Business;
import com.thepoofy.gwumpy.yelp.model.YelpSearchResults;
import com.thepoofy.util.Location;
import com.williamvanderhoef.foursquare.model.Venue;
import com.williamvanderhoef.foursquare.responses.VenueSearchResponse;

/**
 * @author Willum
 *
 */
@SuppressWarnings("serial")
public class YelpSearch extends VenueSearch<Map<String, Object>>
{
	private static final Logger log = Logger.getLogger(YelpSearch.class.getName());
	
	
	@Override
	List<Map<String, Object>> search(Location loc, Integer radius, VenueCategoryEnum category)
			throws Exception 
	{
		List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
		
		List<Venue> fsqPlaces = find4sq(loc, radius, category);
		List<Business> businessList = findYelp(loc, radius, category);
		
		for(Venue v : fsqPlaces)
		{
			Map<String, Object> place = new HashMap<String, Object>();
			
			place.put("fsq", v);
			
			Business biz = findSimilarYelpPlace(v, businessList);
			place.put("yelp", biz);
			
			data.add(place);
		}
		
		
		
		return data;
	}

	private List<Venue> find4sq(Location loc, Integer radius, VenueCategoryEnum category) throws Exception
	{
		VenuesDao dao = new VenuesDao(null);
		
		VenueSearchResponse vsr = dao.browse(loc, radius, category.getFsqId());
		
		return vsr.getVenues();
	}
	
	private List<Business> findYelp(Location loc, Integer radius, VenueCategoryEnum category) throws Exception
	{
		Yelp dao = new Yelp();
		YelpSearchResults response = dao.search(null, loc.getLatitude(), loc.getLongitude(), radius, category.getYelpId());
		
		return response.getBusinesses();
	}
	
	/**
	 * 
	 * @param v
	 * @param yelp
	 */
	private Business findSimilarYelpPlace(Venue v, List<Business> yelp)
	{
		for(Business b : yelp)
		{
			if(v.getLocation().getPostalCode().subSequence(0, 5).equals(b.getLocation().getPostal_code().subSequence(0, 5)))
			{
				log.info(b.getName());
				
				return b;
			}
		}
		
		return null;
	}
	
	
	
}
