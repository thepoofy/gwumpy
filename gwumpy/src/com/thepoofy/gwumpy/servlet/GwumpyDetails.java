/**
 * 
 */
package com.thepoofy.gwumpy.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.thepoofy.ca.dao.foursquare.VenuesDao;
import com.thepoofy.gwumpy.model.VenueSummary;
import com.thepoofy.gwumpy.nyc.NycInspectionApi;
import com.williamvanderhoef.foursquare.adapters.JsonSyntaxException;
import com.williamvanderhoef.foursquare.model.VenueDetails;
import com.williamvanderhoef.foursquare.model.VenueExternalLink;
import com.williamvanderhoef.foursquare.parsers.JsonUtil;
import com.williamvanderhoef.foursquare.parsers.ResultsParser;
import com.williamvanderhoef.foursquare.responses.VenueLinksResponse;
import com.williamvanderhoef.foursquare.responses.VenueResponse;

/**
 * @author Willum
 *
 */
@SuppressWarnings("serial")
public class GwumpyDetails extends HttpServlet
{
	private void handleResponse(HttpServletRequest request,HttpServletResponse response) throws IOException, ServletException
	{
		System.out.println("ASDF ASDF ASDF ASDF");
		
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
		
		if(res != null)
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
		
		if(res != null)
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
