package com.thepoofy.ca.dao.foursquare;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.auth.InvalidCredentialsException;

import com.thepoofy.constants.Constants;
import com.thepoofy.util.Location;
import com.williamvanderhoef.foursquare.model.VenueDetails;
import com.williamvanderhoef.foursquare.model.subtypes.Results;
import com.williamvanderhoef.foursquare.responses.VenueResponse;
import com.williamvanderhoef.foursquare.responses.VenueSearchResponse;

/**
 * 
 * @author <a href="mailto://william.vanderhoef@gmail.com">William Vanderhoef</a>
 *
 */
public class VenuesDao
{
	private final FoursquareDao dao;
	
	/**
	 * 
	 * @param accessToken
	 */
	public VenuesDao(String accessToken)
	{
		dao = new FoursquareDao("venues", accessToken);
	}
	
	/**
	 * Venue Detail
	 * http://developer.foursquare.com/docs/venues/venues.html
	 * 
	 * @param venueId
	 * @return
	 * @throws IOException
	 * @throws HttpConnectionException
	 * @throws InvalidCredentialsException
	 */
	public VenueDetails getDetails(Integer venueId) throws IOException, InvalidCredentialsException, Exception
	{
		if(!Constants.MOCK_DATA)
		{
			return dao.execute(venueId.toString(), null, new Results<VenueResponse>(){}).getVenue();
		}
		else
		{
			//TODO update this if we start using this endpoint
			return null;
		}
	}
	
	public VenueSearchResponse browse(Location loc, Integer radius, String category) throws IOException, InvalidCredentialsException, Exception
	{
		return search(loc, radius,  null, 100, "browse", category);
	}
	
	/**
	 * Search Venues
	 * http://developer.foursquare.com/docs/venues/search.html
	 * 
	 * 
	 * @throws InvalidCredentialsException 
	 * @throws HttpConnectionException 
	 * @throws IOException 
	 * @throws Exception
	 */
	public VenueSearchResponse search(Location loc, Integer radius, 
			String query, 
			Integer limit, 
			String intent,
			String category
			) 
	
		throws IOException, InvalidCredentialsException, Exception
	{
		Map<String, String>params = new HashMap<String,String>();
		
		
		if(loc != null)
		{
			params.put("ll", ""+loc.getLatitude()+","+loc.getLongitude());
			params.put("llAcc", ""+loc.getAccuracy());
		}
		else
		{
			throw new Exception("Latitude and Longitude are required for Venue Search");
		}
		
		if(query != null)
		{
			params.put("query", query);
		}
		if(radius != null)
		{
			params.put("radius", ""+radius);
		}
		if(limit != null)
		{
			params.put("limit", limit.toString());
		}
		if(intent != null)
		{
			params.put("intent", intent);
		}
		if(category != null)
		{
			params.put("categoryId", category);
		}
		
		if(!Constants.MOCK_DATA)
		{
			return dao.execute(null,"search",params, new Results<VenueSearchResponse>(){});
		}
		else
		{
			return new VenueSearchResponse();
		}
	}
	
	
}
