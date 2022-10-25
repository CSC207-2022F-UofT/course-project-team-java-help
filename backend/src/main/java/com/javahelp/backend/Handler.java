package com.javahelp.backend;

import java.util.Collections;
import java.util.Map;

import org.apache.log4j.Logger;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class Handler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

	private static final Logger LOG = Logger.getLogger(Handler.class);

	@Override
	public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
		// this function takes in input and context
		// context gives you some information about the broader environment your
		// function is running in

		LOG.info("received: " + input); // this line deals with the logger, which uses a popular library called log4j
										// you don't need to worry about this for now

		// here the response is setup before being returned and sent back to whoever
		// called this function
		SimpleResponse response = new SimpleResponse("Hello World, And Hello AWS Lambda! " +
				"This is from within the backend module! " +
				"This was pushed by the new CD pipeline!");

		return ApiGatewayResponse.builder() 	// this is a little utility class provided by the serverless framework, we
												// can use this
												// or write our own code to create and format responses

				.setStatusCode(200) // this line sets the http status code for the response, which basically tells
												// the receiver whether their request ran properly. 200 means it did

				.setObjectBody(response)		// here we set the actual content of the response, not just whether
												// it was successful

				.setHeaders(					// ignore this for now
					Collections.singletonMap("Using", "AWS Lambda & Serverless")) 

				.build();						// assembles the response into an object which is then returned
	}
}
