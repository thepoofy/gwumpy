/**
 * 
 */
package com.thepoofy.gwumpy.servlet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;

import com.thepoofy.ca.dao.foursquare.VenuesDao;
import com.thepoofy.constants.VenueCategoryEnum;
import com.thepoofy.gwumpy.yelp.Yelp;
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
	@Override
	List<Map<String, Object>> search(Location loc, Integer radius, VenueCategoryEnum category)
			throws Exception 
	{
		List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
		
		List<Venue> fsqPlaces = find4sq(loc, radius, category);
		List<Map<String, Object>> businessList = findYelp(loc, radius, category);
		
		for(Venue v : fsqPlaces)
		{
			Map<String, Object> place = new HashMap<String, Object>();
			
			place.put("fsq", v);
			
			Map<String, Object> biz = findSimilarYelpPlace(v, businessList);
			
			if(biz != null)
			{
				place.put("yelp", biz);
			}
			
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
	
	private List<Map<String, Object>> findYelp(Location loc, Integer radius, VenueCategoryEnum category) throws Exception
	{
		Yelp dao = new Yelp();
		String response = dao.search(null, loc.getLatitude(), loc.getLongitude(), radius, category.getYelpId());
		
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> yelpBusinesses = mapper.readValue(response, (new HashMap<String, Object>()).getClass());
		
		return (List<Map<String, Object>>)yelpBusinesses.get("businesses");
	}
	
	/**
	 * 
	 * @param v
	 * @param yelp
	 */
	private Map<String, Object> findSimilarYelpPlace(Venue v, List<Map<String, Object>> yelp)
	{
		for(Map<String, Object> y : yelp)
		{
//			if(v.getLocation().getPostalCode().subSequence(0, 5).equals(y.get("location").get("postal_code").subSequence(0, 5)))
//			{
//				
//			}
		}
		
		return null;
	}
	
	
	
}
