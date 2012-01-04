package com.thepoofy.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import au.com.bytecode.opencsv.CSVReader;

import com.thepoofy.gwumpy.nyc.NycInspectionGrade;

public class RestaurantGradeUploaderUtil {

//	private static final String ADDRESS = "http://127.0.0.1:8888/RestaurantGradeUpload";
	private static final String ADDRESS = "http://gwumpymobile.appspot.com/RestaurantGradeUpload";
	private static int uploadCounter = 0;
	
	
	public static void main(String [] args)
	{
		Map<String, NycInspectionGrade> gradeMap = new HashMap<String, NycInspectionGrade>();
		Map<String, String> csvMap = new HashMap<String, String>();
		
		try
		{
			BufferedReader reader = new BufferedReader(new FileReader("C:\\allGrades.txt"));
			if(reader.ready())
			{
				reader.readLine();
			}
			
			
			
			while(reader.ready())
			{
				String csvRecord = reader.readLine();
				
				NycInspectionGrade grade = fromCsv(csvRecord);
				
				if(isBetter(gradeMap, grade))
				{
					csvMap.put(grade.getCamis(), csvRecord);
				}	
			}
		}
		catch(Exception e)
		{
			e.printStackTrace(System.err);
			
			return;
		}
		
		Set<Map.Entry<String, String>> entries = csvMap.entrySet();
		
		System.out.println("Found "+entries.size());
		
		try
		{
			for(Map.Entry<String, String> entry: entries)
			{

				if(uploadCounter >= 2813)
				{
					upload(entry.getValue());
				}
				else
				{
					uploadCounter++;
				}
					
			}
		}
		catch(Exception e)
		{
			e.printStackTrace(System.err);
		}
		
	}
	
	
	private static void upload(String csvRecord)throws Exception, InterruptedException
	{
		List<KeyValuePair> params = new ArrayList<KeyValuePair>();
		
		params.add(new KeyValuePair("inspection", csvRecord));
		
		String response = URLUtil.doStandalonePost(ADDRESS, params);
		
		if(response == null)
		{
			throw new Exception("Failed to upload.");
		}
		
//		System.out.println(response);
		System.out.println("Finished: "+uploadCounter++);
		
//		Thread.sleep(500);
	}
	
	
	
	private static boolean isBetter(Map<String, NycInspectionGrade> gradeMap, NycInspectionGrade grade)
	{
		if(grade.getCurrentGrade() == null || grade.getCurrentGrade().isEmpty())
		{
			return false;
		}
		
		NycInspectionGrade prevGrade = gradeMap.get(grade.getCamis());
		if(prevGrade == null)
		{
			gradeMap.put(grade.getCamis(), grade);
			
			return true;
		}
		
		if(prevGrade.getInspectionDate().before(grade.getInspectionDate()))
		{
			gradeMap.put(grade.getCamis(), grade);
			
			return true;
		}
		
		return false;
	}
	
	
	private static NycInspectionGrade fromCsv(String inspectionRecord) throws IOException
	{
		CSVReader reader = new CSVReader(new StringReader(inspectionRecord));
		
		NycInspectionGrade grade = new NycInspectionGrade();
		
		String [] nextLine = null;
		
		List<String[]>records = reader.readAll();
		
		if(records != null && !records.isEmpty())
		{
			nextLine = records.get(0);
		}
		
		if(nextLine != null && nextLine.length > 10)
		{
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
		}
		
		return grade;
		
	}
}
