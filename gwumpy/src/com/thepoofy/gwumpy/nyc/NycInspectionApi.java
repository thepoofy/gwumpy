package com.thepoofy.gwumpy.nyc;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

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
			Map results = mapper.readValue(response, Map.class);
			
			List<List<Object>> data = (List)results.get("data");
			if(data != null && !data.isEmpty() && data.get(0).size() > 23)
			{
				System.out.println(data.get(0));
				return (String)data.get(0).get(23);
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
	
}
