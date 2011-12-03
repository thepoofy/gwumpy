package com.thepoofy.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.List;

import com.google.appengine.api.urlfetch.HTTPHeader;
import com.google.appengine.api.urlfetch.HTTPMethod;
import com.google.appengine.api.urlfetch.HTTPRequest;
import com.google.appengine.api.urlfetch.HTTPResponse;
import com.google.appengine.api.urlfetch.URLFetchService;
import com.google.appengine.api.urlfetch.URLFetchServiceFactory;
import com.google.appengine.repackaged.com.google.common.util.Base64;

public class URLUtil
{
	
	/**
	 * 
	 * @param query
	 */
	public static String callAuthenticated(URL query, String username, String password) throws IOException
	{
		HttpURLConnection conn = (HttpURLConnection)query.openConnection();
		
		try
		{
			setBasicAuth(conn, username, password);
			
			return processStream(conn.getInputStream());
		}
		catch(IOException ioe)
		{
			ioe.printStackTrace(System.err);
			
			return processStream(conn.getErrorStream());
		}
		
	}
	
	private static String processStream(InputStream is) throws IOException
	{
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		
		
		StringBuilder sb = new StringBuilder();
		while (reader.ready())
		{
			sb.append(reader.readLine());
		}

		reader.close();
		
		return sb.toString();
	}
	
	
	
    /**
     * adapted from: http://stackoverflow.com/questions/1341081/using-http-basic-auth-with-google-app-engine-urlfetch-service
     * 
     * Preemptively set the Authorization header to use Basic Auth.
     * @param connection The HTTP connection
     * @param username Username
     * @param password Password
     */
    private static void setBasicAuth(HttpURLConnection connection, String username, String password) 
    {
        StringBuilder buf = new StringBuilder(username);
        buf.append(':');
        buf.append(password);
        byte[] bytes = null;
        try 
        {
        	bytes = buf.toString().getBytes("ISO-8859-1");
        } 
        catch (java.io.UnsupportedEncodingException uee) 
        {
        	assert false;
        }

        String header = "Basic " + Base64.encode(bytes);
        connection.setRequestProperty("Authorization", header);
        
        connection.setRequestProperty("User-Agent", "gwumpy");
    }

	
	/**
	 * Takes advantage of the Googe App Engine Fetch Service
	 * @param uri
	 * @return NULL if a problem occurs
	 */
	public static String loadPage(String uri, HTTPMethod method)
	{
		URLFetchService fetcher = URLFetchServiceFactory.getURLFetchService();
		
		String results = null;
		try
		{
			System.out.println("Loading: " + uri);
			
			HTTPRequest request = new HTTPRequest(new URL(uri), method);
			request.setHeader(new HTTPHeader("User-Agent", "williamvanderhoef.com v2.0 beta"));
			
			HTTPResponse res = fetcher.fetch(request);
			
			if (res.getResponseCode() == 200)
			{
				results = new String(res.getContent(),"UTF-8"); 
			}
			else
			{
				System.err.println("Unable to load page: " + uri + "\nResponse Code:" + res.getResponseCode());
			}
		}
		catch(Exception e)
		{
			e.printStackTrace(System.err);
		}
		return results;
	}
	
	
	public static String doPost(String address, List<KeyValuePair>params) {
		try {
			// Construct data
			StringBuilder data = new StringBuilder();
			for(KeyValuePair param : params)
			{
				data.append(URLEncoder.encode(param.key, "UTF-8"))
				.append("=")
				.append(URLEncoder.encode(param.value, "UTF-8"))
				.append("&");
			}
			
				
			// Send data
			URL url = new URL(address);
			URLConnection conn = url.openConnection();
			conn.setDoOutput(true);
			OutputStreamWriter outputStreamWriter = new OutputStreamWriter(conn.getOutputStream());
			outputStreamWriter.write(data.toString());
			outputStreamWriter.flush();

			// Get the response
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			
			StringBuilder sb = new StringBuilder();
			
			while(br.ready())
			{
				sb.append(br.readLine());
			}
			
			outputStreamWriter.close();
			br.close();
			
			return sb.toString();
			
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return null;
	}
	
	public static String doGet(String address, List<KeyValuePair>params) {
		try {
			// Construct data
			StringBuilder data = new StringBuilder();
			for(KeyValuePair param : params)
			{
				data.append(URLEncoder.encode(param.key, "UTF-8"))
				.append("=")
				.append(URLEncoder.encode(param.value, "UTF-8"))
				.append("&");
			}
			
				
			// Send data
			URL url = new URL(address+"?"+data.toString());
			System.out.println(url.toExternalForm());
			URLConnection conn = url.openConnection();

			// Get the response
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			
			StringBuilder sb = new StringBuilder();
			
			while(br.ready())
			{
				sb.append(br.readLine());
			}
			
			br.close();
			
			return sb.toString();
			
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return null;
	}
	
}
