package com.thepoofy.gwumpy.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;

import com.williamvanderhoef.foursquare.adapters.JsonSyntaxException;
import com.williamvanderhoef.foursquare.parsers.JsonUtil;
import com.williamvanderhoef.foursquare.parsers.ResultsParser;

/**
 * 
 * @author williamvanderhoef
 *
 */
@SuppressWarnings("serial")
public class ServletBase extends HttpServlet
{
	private static final Logger log = Logger.getLogger(ServletBase.class.getName());
	
	
	static void doResponse(Object o, HttpServletResponse response) throws JsonSyntaxException, IOException
	{
		ResultsParser<Object> parser = JsonUtil.getParser(Object.class);
		String res = parser.toJson(o);
		
//		System.out.println("response: " +res);
		log.info(res);
		
		response.setContentType("application/json");
		response.getWriter().append(res);
		response.getWriter().flush();
		response.setStatus(200);
		
	}
	
	static void doError(Throwable t, HttpServletResponse response)
	{
//		System.out.println(t.getMessage());
//		t.printStackTrace();
		log.log(Level.SEVERE, t.getMessage(), t);
		response.setStatus(400);
		response.setContentType("application/json");
		
		try
		{
			ObjectMapper mapper = new ObjectMapper();
			
			Map<String, String> errors = new HashMap<String, String> ();
			errors.put("error", t.getMessage());
			
			response.getWriter().append(mapper.writeValueAsString(errors)).flush();
			
		} catch (IOException e)
		{
			log.log(Level.SEVERE, e.getMessage(), e);
		}
	}
	
	static Integer getParameterInteger(HttpServletRequest req, String param, boolean isRequired) throws Exception
	{
		String res = getParameter(req, param, isRequired);
		
		log.info("Param: "+param+": "+res);
		
		if(res != null && !res.isEmpty())
		{
			return Integer.parseInt(res);
		}
		else
		{
			return null;
		}
	}
	
	static Double getParameterDouble(HttpServletRequest req, String param, boolean isRequired)
	{
		String res = getParameter(req, param, isRequired);
		
		log.info("Param: "+param+": "+res);
		
		if(res != null && !res.isEmpty())
		{
			return Double.parseDouble(res);
		}
		else
		{
			return null;
		}
	}
	
	static String getParameter(HttpServletRequest req, String param, boolean isRequired)
	{
		String res = req.getParameter(param);
		
		if(res != null && !res.isEmpty())
		{
			return res;
		}
		else
		{
			if(isRequired)
			{
				throw new ParameterException(param);
			}
			return null;
		}
	}
}
