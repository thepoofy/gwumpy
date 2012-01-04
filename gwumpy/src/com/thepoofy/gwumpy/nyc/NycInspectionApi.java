package com.thepoofy.gwumpy.nyc;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;
import com.thepoofy.gwumpy.servlet.ServletBase;
import com.thepoofy.util.URLUtil;
import com.williamvanderhoef.foursquare.model.Venue;

public class NycInspectionApi
{

	/**
	 * 
	 * @param v
	 * @return
	 */
	public String findGrade(Venue v)
	{
//		http://nycopendata.socrata.com/api/views/zf7n-cm69/rows.json
		
		StringBuilder sb = new StringBuilder();
		sb.append("http://nycopendata.socrata.com/api/views/zf7n-cm69/rows.json");
		
		Map<String, String> params = new HashMap<String, String>();
		
		if(v.getLocation().getAddress() != null)
		{
			params.put("search", v.getLocation().getAddress());
		}
		
		String response = URLUtil.doGet(sb.toString(), URLUtil.convertToKeyValuePair(params));
		ObjectMapper mapper = new ObjectMapper();
		try
		{
//			System.out.println(response);
			Map results = mapper.readValue(response, Map.class);
			
			List<List<Object>> data = (List)results.get("data");
			if(data != null && !data.isEmpty() && data.get(0).size() > 23)
			{
				System.out.println(data.get(0));
				return ""+data.get(0).get(22);
			}
		}
		catch(JsonMappingException e)
		{
			e.printStackTrace();
		} catch (JsonParseException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
		
	}
	
	static{
		ObjectifyService.register(NycInspectionGrade.class);
	}
	
	private static final Logger log = Logger.getLogger(NycInspectionApi.class.getName());
	
	public static void saveGrade(NycInspectionGrade grade)
	{
		Objectify ofy = ObjectifyService.begin();

		NycInspectionGrade latestGrade = ofy.query(NycInspectionGrade.class).filter("camis", grade.camis).order("inspectionDate").limit(1).get();
		
		if(latestGrade == null)
		{
			ofy.put(grade);
			log.info("Inserted grade for "+grade.getDba()+" is "+grade.getCurrentGrade()+" on "+grade.getInspectionDate());
		}
		else
		{
			log.info("No action taken for "+grade.getDba());
		}
		
		assert grade.id != null;
		
		
//		
//		System.out.println("Inserted grade for "+grade.getDba()+" is "+grade.getCurrentGrade()+" on "+grade.getInspectionDate());
//		if(latestGrade != null)
//		{
//			System.out.println("Latest grade for "+latestGrade.getDba()+" is "+latestGrade.getCurrentGrade()+" on "+latestGrade.getInspectionDate());
//		}
//		else
//		{
//			System.out.println("Unable to fetch data.");
//		}
	}
	
}
