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

/**
 * 
 * @author williamvanderhoef
 *
 */
@SuppressWarnings("serial")
public class ServletBase extends HttpServlet
{
	protected static final Logger log = Logger.getLogger(ServletBase.class.getName());
	
	
	protected static void doResponse(Object o, HttpServletResponse response)
	{
		try
		{
			Map<String, Object> results = new HashMap<String, Object>();
			results.put("data", o);
			results.put("status", 200);
			
			ObjectMapper mapper = new ObjectMapper();
			mapper.writeValue(response.getWriter(), results);
			
//			log.info(mapper.writeValueAsString(o));
			
			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/json");
//			response.getWriter().append(res);
			response.getWriter().flush();
			response.setStatus(200);
			
		}
		catch(IOException e)
		{
			doError(e, response);
		}
	}
	
	protected static void doError(Throwable t, HttpServletResponse response)
	{
		log.log(Level.SEVERE, t.getClass().getName(), t);
		t.printStackTrace();
		
		Map<String, Object> results = new HashMap<String, Object>();
		results.put("data", null);
		results.put("status", 500);
		results.put("msg", "Oops Gwumpy slipped and fell. Our developers are checking on him now.");	
		
		response.setStatus(500);
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json");
		
		ObjectMapper mapper = new ObjectMapper();
		
		try
		{
			mapper.writeValue(response.getWriter(), results);
		}
		catch(IOException e)
		{
			response.setStatus(500);
		}
		
		
	}
	
	protected static Integer getParameterInteger(HttpServletRequest req, String param, boolean isRequired) throws Exception
	{
		String res = getParameter(req, param, isRequired);
		
//		log.info("Param: "+param+": "+res);
		
		if(res != null && !res.isEmpty())
		{
			return Integer.parseInt(res);
		}
		else
		{
			return null;
		}
	}
	
	protected static Double getParameterDouble(HttpServletRequest req, String param, boolean isRequired)
	{
		String res = getParameter(req, param, isRequired);
		
//		log.info("Param: "+param+": "+res);
		
		if(res != null && !res.isEmpty())
		{
			return Double.parseDouble(res);
		}
		else
		{
			return null;
		}
	}
	
	protected static String getParameter(HttpServletRequest req, String param, boolean isRequired)
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
