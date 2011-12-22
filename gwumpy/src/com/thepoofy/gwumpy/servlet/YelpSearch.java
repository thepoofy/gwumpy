/**
 * 
 */
package com.thepoofy.gwumpy.servlet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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
		
		Iterator<Venue> itr = vsr.getVenues().iterator();
		while(itr.hasNext())
		{
			Venue v = itr.next();
			if(v.getStats().getCheckinsCount() < 25)
			{
				itr.remove();
			}
		}
		
		return vsr.getVenues();
	}
	
	private List<Business> findYelp(Location loc, Integer radius, VenueCategoryEnum category) throws Exception
	{
		Yelp dao = new Yelp();
		YelpSearchResults response = dao.search(loc.getLatitude(), loc.getLongitude(), radius, category.getYelpId(), 0);
		List<Business> bizList = new ArrayList<Business>();
		
		for(Business b : response.getBusinesses())
		{
			log.info(b.getName());
			bizList.add(b);
		}
//		bizList.addAll(response.getBusinesses());
		
		if(response.getTotal() > 20)
		{
			YelpSearchResults response1 = dao.search(loc.getLatitude(), loc.getLongitude(), radius, category.getYelpId(), 20);
			for(Business b : response1.getBusinesses())
			{
				log.info(b.getName());
				bizList.add(b);
			}
//			bizList.addAll(response1.getBusinesses());
		}
		
		
		return response.getBusinesses();
	}
	
	/**
	 * 
	 * @param v
	 * @param yelp
	 */
	private Business findSimilarYelpPlace(Venue v, List<Business> yelp)
	{
		int bestScore = 0;
		Business bestBiz = null;
		
		for(Business b : yelp)
		{
			int score = 0;
			score += comparePostalCode(v,b);
			score += compareName(v,b);
			score += compareAddress(v,b);
			
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
	
	
	private static int comparePostalCode(Venue v, Business b)
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
	
	private static int compareName(Venue v, Business b)
	{
		if (v.getName() != null
				&& b.getName() != null)
		{
			return (b.getName().equalsIgnoreCase(v.getName())? 10: -5);
		}
		
		return 0;
	}
	
	private static int compareAddress(Venue v, Business b)
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
