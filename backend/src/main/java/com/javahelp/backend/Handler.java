package com.javahelp.backend;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.GetItemRequest;
import com.amazonaws.services.dynamodbv2.model.GetItemResult;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.regions.Regions;
import com.javahelp.backend.endpoint.APIGatewayResponse;

public class Handler implements RequestHandler<Map<String, Object>, APIGatewayResponse> {

	private static final Logger LOG = Logger.getLogger(Handler.class);

	@Override
	public APIGatewayResponse handleRequest(Map<String, Object> input, Context context) {
		// this function takes in input and context
		// context gives you some information about the broader environment your
		// function is running in

		LOG.info("received: " + input); // this line deals with the logger, which uses a popular library called log4j
										// you don't need to worry about this for now

		AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard() // initialize a new DynamoDB client
				.withRegion(Regions.US_EAST_1)
				.build();

		Map<String, AttributeValue> key = new HashMap<>(); // Create a new map to store the desired key
		key.put("id", new AttributeValue("test")); // this is the key of the item to find

		GetItemRequest r = new GetItemRequest().withTableName("javahelpBackendUsers") // specify table
			.withKey(key); // specify the key we want to locate

		GetItemResult result = client.getItem(r); // get the item with the specified key

		Map<String, AttributeValue> item = result.getItem(); // extract the item from the query result

		String name = "Item Does Not Exist"; // assume the item was not found

		if (item != null) { // check whether the item was found
			name = item.get("name").getS(); // update the name string if the item was found
		}

		// here the response is setup before being returned and sent back to whoever
		// called this function
		SimpleResponse response = new SimpleResponse("Hello World, And Hello AWS Lambda! " +
				"This is from within the backend module! " +
				"This was pushed by the new CD pipeline that ran on merge to main! " +
				(item != null ? ("Found a user named " + name + "!") : "Found no user!"));

		return APIGatewayResponse.builder() 	// this is a little utility class provided by the serverless framework, we
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
