/**
 * 
 */
package com.thepoofy.gwumpy.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.thepoofy.constants.VenueCategoryEnum;
import com.thepoofy.util.Location;

/**
 * @author Willum
 *
 */
@SuppressWarnings("serial")
public abstract class VenueSearch<T> extends ServletBase
{
	abstract List<T> search(Location loc, Integer radius, VenueCategoryEnum category) throws Exception;
	
	private void handleResponse(HttpServletRequest request,HttpServletResponse response) throws IOException, ServletException
	{
		try
		{
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
			
			List<T> venues = search(loc, radius, category);
			
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
