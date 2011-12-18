package com.thepoofy.gwumpy.servlet;

/**
 * 
 * @author William Vanderhoef william.vanderhoef@gmail.com
 *
 */
public class ParameterException extends RuntimeException 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3910506039443608823L;

	public ParameterException()
	{
		super();
	}
	
	public ParameterException(String paramName)
	{
		super("Missing parameter: "+paramName);
	}

}
