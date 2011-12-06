/**
 * 
 */
package com.thepoofy.gwumpy.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.thepoofy.ca.dao.foursquare.VenuesDao;
import com.thepoofy.gwumpy.model.VenueSummary;
import com.thepoofy.gwumpy.nyc.NycInspectionApi;
import com.williamvanderhoef.foursquare.model.VenueDetails;
import com.williamvanderhoef.foursquare.model.VenueExternalLink;
import com.williamvanderhoef.foursquare.responses.VenueLinksResponse;
import com.williamvanderhoef.foursquare.responses.VenueResponse;

/**
 * @author Willum
 *
 */
@SuppressWarnings("serial")
public class GwumpyDetails extends ServletBase
{
	private void handleResponse(HttpServletRequest request,HttpServletResponse response) throws IOException, ServletException
	{
		try
		{
			VenuesDao dao = new VenuesDao(null);
			
			String fsqId = getParameter(request, "fsqId", true);
			
			VenueResponse venueResponse = dao.getDetails(fsqId);
			VenueDetails vd = venueResponse.getVenue();
			
			VenueLinksResponse linksResponse = dao.links(fsqId);
			
			NycInspectionApi nycApi = new NycInspectionApi();
			String restaurantGrade = nycApi.findGrade(vd);
			
			VenueSummary vs = VenueSummary.valueOf(vd);
			
			vs.setHealthCodeRating(restaurantGrade);
			
			if(linksResponse != null && linksResponse.getVenueLinks() != null)
			{
				for(VenueExternalLink links : linksResponse.getVenueLinks()){
				
					if("nyt".equalsIgnoreCase(links.getProvider().getId()))
					{
						vs.setNytimesReview(links.getUrl());
					}
				}
			}
			
			
			
//			List<VenueSummary> venues = VenueSummary.adaptVenueList(vsr);
			
			doResponse(vs, response);
		}
		catch(Throwable t)
		{
			doError(t, response);
		}
	}
	
	public void doGet(HttpServletRequest request,HttpServletResponse response) throws IOException, ServletException
	{
		handleResponse(request, response);
	}
	public void doPost(HttpServletRequest request,HttpServletResponse response) throws IOException, ServletException
	{
		handleResponse(request, response);
	}
}
