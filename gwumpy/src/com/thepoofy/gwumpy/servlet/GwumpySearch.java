/**
 * 
 */
package com.thepoofy.gwumpy.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.thepoofy.ca.dao.foursquare.VenuesDao;
import com.thepoofy.constants.VenueCategoryEnum;
import com.thepoofy.gwumpy.model.VenueSummary;
import com.thepoofy.util.Location;
import com.williamvanderhoef.foursquare.adapters.JsonSyntaxException;
import com.williamvanderhoef.foursquare.parsers.JsonUtil;
import com.williamvanderhoef.foursquare.parsers.ResultsParser;
import com.williamvanderhoef.foursquare.responses.VenueSearchResponse;

/**
 * @author Willum
 *
 */
@SuppressWarnings("serial")
public class GwumpySearch extends HttpServlet
{
	private void handleResponse(HttpServletRequest request,HttpServletResponse response) throws IOException, ServletException
	{
		try
		{
			VenuesDao dao = new VenuesDao(null);
			
			Double lat = getParameterDouble(request, "lat", true);
			Double lng = getParameterDouble(request, "lng", true); //Double.parseDouble(request.getParameter("lng"));
			Integer llAcc = getParameterInteger(request, "llAcc", false);//Integer.parseInt(request.getParameter("llAcc"));
//			String dist = request.getParameter("distance");
			String cat = getParameter(request, "category", false);
//			String price = request.getParameter("price");
			
			Location loc = new Location();
			loc.setLatitude(lat);
			loc.setLongitude(lng);
			if(llAcc == null)
			{
				llAcc = 100;
			}
			loc.setAccuracy(llAcc);
			
			Integer radius = 400;
//			if("3".equals(dist))
//			{
//				radius = 1600;
//			}
//			else if("1".equals(dist))
//			{
//				radius = 400;
//			}
			
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
	
	public void doGet(HttpServletRequest request,HttpServletResponse response) throws IOException, ServletException
	{
		handleResponse(request, response);
	}
	public void doPost(HttpServletRequest request,HttpServletResponse response) throws IOException, ServletException
	{
		handleResponse(request, response);
	}
	
	private void doResponse(Object o, HttpServletResponse response) throws JsonSyntaxException, IOException
	{
		ResultsParser<Object> parser = JsonUtil.getParser(Object.class);
		String res = parser.toJson(o);
		
		System.out.println("response: " +res);
		
		response.setContentType("application/json");
		response.getWriter().append(res);
		response.getWriter().flush();
		response.setStatus(200);
		
	}
	
	private void doError(Throwable t, HttpServletResponse response)
	{
		System.out.println(t.getMessage());
		t.printStackTrace();
		response.setStatus(400);
		response.setContentType("application/json");
		
		try
		{
			ResultsParser<Map> parser = JsonUtil.getParser(Map.class);
			Map<String, String> errors = new HashMap<String, String> ();
			errors.put("error", t.getMessage());
			
			response.getWriter().append(parser.toJson(errors)).flush();
			
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonSyntaxException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private Integer getParameterInteger(HttpServletRequest req, String param, boolean isRequired) throws Exception
	{
		String res = getParameter(req, param, isRequired);
		
		if(res != null && !res.isEmpty())
		{
			return Integer.parseInt(res);
		}
		else
		{
			return null;
		}
	}
	
	private Double getParameterDouble(HttpServletRequest req, String param, boolean isRequired) throws Exception
	{
		String res = getParameter(req, param, isRequired);
		
		if(res != null && !res.isEmpty())
		{
			return Double.parseDouble(res);
		}
		else
		{
			return null;
		}
	}
	
	private String getParameter(HttpServletRequest req, String param, boolean isRequired) throws Exception
	{
		if(req.getParameter(param) != null)
		{
			return req.getParameter(param);
		}
		else
		{
			if(isRequired)
			{
				throw new Exception("Paramter "+param+" is required.");
			}
			return null;
		}
	}
}
