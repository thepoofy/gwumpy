/**
 * 
 */
package com.thepoofy.gwumpy.servlet;

import java.io.IOException;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;

import au.com.bytecode.opencsv.CSVReader;

import com.thepoofy.gwumpy.nyc.NycInspectionApi;
import com.thepoofy.gwumpy.nyc.NycInspectionGrade;

/**
 * @author Willum
 *
 */
@SuppressWarnings("serial")
public class NycRestaurantUpload extends ServletBase
{
	/**
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ServletException
	 */
	private void handleResponse(HttpServletRequest request,HttpServletResponse response) throws IOException, ServletException
	{
		
		//SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.440000000");
		
		try
		{
			String inspectionRecord = getParameter(request, "inspection", false);
			
			CSVReader reader = new CSVReader(new StringReader(inspectionRecord));
			
			List<NycInspectionGrade> grades = new ArrayList<NycInspectionGrade>();
			
			String [] nextLine;
			while ((nextLine = reader.readNext()) != null)
			{
				NycInspectionGrade grade = new NycInspectionGrade();
				int i=0;
				grade.setCamis(nextLine[i++]);
				grade.setDba(nextLine[i++]);
				grade.setBoro(nextLine[i++]);
				grade.setBuilding(nextLine[i++]);
				grade.setStreet(nextLine[i++]);
				grade.setZipCode(nextLine[i++]);
				grade.setPhone(nextLine[i++]);
				grade.setCuisineCode(nextLine[i++]);
				grade.setInspectionDate(nextLine[i++]);
				grade.setAction(nextLine[i++]);
				grade.setViolationCode(nextLine[i++]);
				grade.setScore(nextLine[i++]);
				grade.setCurrentGrade(nextLine[i++]);
				grade.setGradeDate(nextLine[i++]);
				grade.setRecordDate(nextLine[i++]);
				
				
				ObjectMapper mapper = new ObjectMapper();
//				System.out.println(mapper.writeValueAsString(grade));
				
//				System.out.println("Saving now.");
				
				NycInspectionApi.saveGrade(grade);
				
				grades.add(grade);
			}
			
			doResponse(grades, response);
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
