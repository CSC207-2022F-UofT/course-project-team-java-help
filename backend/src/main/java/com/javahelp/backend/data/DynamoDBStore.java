package com.javahelp.backend.data;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;

/**
 * Abstract repository built on AWS DynamoDB
 */
abstract class DynamoDBStore {

    private final Regions region;

    private final AmazonDynamoDB client;

    /**
     * Creates a new {@link DynamoDBStore}
     *
     * @param region {@link Regions} to be used
     */
    DynamoDBStore(Regions region) {
        this.region = region;
        this.client = AmazonDynamoDBClientBuilder.standard()
                .withRegion(Regions.US_EAST_1)
                .build();
    }

    /**
     * @return {@link AmazonDynamoDB} client
     */
    protected AmazonDynamoDB getClient() {
        return client;
    }
}
