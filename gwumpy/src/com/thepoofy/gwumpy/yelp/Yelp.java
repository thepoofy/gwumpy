package com.thepoofy.gwumpy.yelp;

import org.scribe.builder.ServiceBuilder;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;

import com.thepoofy.constants.Constants;

/**
 * Example code based on code from Nicholas Smith at
 * http://imnes.blogspot.com/2011/01/how-to-use-yelp-v2-from-java-including.html
 * For a more complete example (how to integrate with GSON, etc) see the blog
 * post above.
 * 
 * Example for accessing the Yelp API.
 */
public class Yelp {

	OAuthService service;
	Token accessToken;

	/**
	 * Setup the Yelp API OAuth credentials.
	 * 
	 * OAuth credentials are available from the developer site, under Manage API access (version 2 API).
	 * 
	 */
	public Yelp() 
	{
		this.service = new ServiceBuilder()
				.provider(YelpApi2.class)
				.apiKey(Constants.YELP_CONSUMER_KEY)
				.apiSecret(Constants.YELP_CONSUMER_SECRET)
				.build();
		
		this.accessToken = new Token(Constants.YELP_TOKEN, Constants.YELP_TOKEN_SECRET);
	}

	/**
	 * Search with term and location.
	 * 
	 * @param term
	 * @param latitude
	 * @param longitude
	 * 
	 * @return JSON string response
	 */
	public String search(String term, double latitude, double longitude, int radius, String category) {
		OAuthRequest request = new OAuthRequest(Verb.GET,
				"http://api.yelp.com/v2/search");
		
		request.addQuerystringParameter("ll", latitude + "," + longitude);
		request.addQuerystringParameter("radius_filter", ""+radius);
		request.addQuerystringParameter("category_filter", category);
		request.addQuerystringParameter("limit", "100");
		
		if(term != null)
		{
			request.addQuerystringParameter("term", term);
		}
		
		
		this.service.signRequest(this.accessToken, request);
		Response response = request.send();
		return response.getBody();
	}

	// // CLI
	// public static void main(String[] args)
	// {
	// // Update tokens here from Yelp developers site, Manage API access.
	// String consumerKey = "";
	// String consumerSecret = "";
	// String token = "";
	// String tokenSecret = "";
	//
	// Yelp yelp = new Yelp(consumerKey, consumerSecret, token, tokenSecret);
	// String response = yelp.search("burritos", 30.361471, -87.164326);
	//
	// System.out.println(response);
	// }
}