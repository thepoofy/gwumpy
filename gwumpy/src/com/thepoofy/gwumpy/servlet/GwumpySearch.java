/**
 * 
 */
package com.thepoofy.gwumpy.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.thepoofy.ca.dao.foursquare.VenuesDao;
import com.thepoofy.constants.VenueCategoryEnum;
import com.thepoofy.gwumpy.model.VenueSummary;
import com.thepoofy.util.Location;
import com.williamvanderhoef.foursquare.responses.VenueSearchResponse;

/**
 * @author Willum
 *
 */
@SuppressWarnings("serial")
public class GwumpySearch extends ServletBase
{
	private void handleResponse(HttpServletRequest request,HttpServletResponse response) throws IOException, ServletException
	{
		try
		{
			VenuesDao dao = new VenuesDao(null);
			
			Double lat = getParameterDouble(request, "lat", true);
			Double lng = getParameterDouble(request, "lng", true); //Double.parseDouble(request.getParameter("lng"));
			Integer llAcc = getParameterInteger(request, "llAcc", false);//Integer.parseInt(request.getParameter("llAcc"));
			String cat = getParameter(request, "category", false);
			
			Location loc = new Location();
			loc.setLatitude(lat);
			loc.setLongitude(lng);
			if(llAcc == null)
			{
				llAcc = 100;
			}
			loc.setAccuracy(llAcc);
			
			Integer radius = 400;
			
			//guarantees a category
			VenueCategoryEnum category = VenueCategoryEnum.find(cat);
			
			VenueSearchResponse vsr = dao.browse(loc, radius, category.getFsqId());
			
			List<VenueSummary> venues = VenueSummary.adaptVenueList(vsr);
			
			
			
			doResponse(venues, response);
		}
		catch(Throwable t)
		{
			doError(t, response);
		}
		
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doGet(HttpServletRequest request,HttpServletResponse response) throws IOException, ServletException
	{
		handleResponse(request, response);
	}
	/*
	 * (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doPost(HttpServletRequest request,HttpServletResponse response) throws IOException, ServletException
	{
		handleResponse(request, response);
	}


	
	
}
