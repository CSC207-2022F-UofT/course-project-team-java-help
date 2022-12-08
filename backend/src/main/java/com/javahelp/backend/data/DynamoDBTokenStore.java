package com.javahelp.backend.data;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.DeleteItemRequest;
import com.amazonaws.services.dynamodbv2.model.GetItemRequest;
import com.amazonaws.services.dynamodbv2.model.GetItemResult;
import com.amazonaws.services.dynamodbv2.model.PutItemRequest;
import com.javahelp.model.token.Token;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Implementation of {@link ITokenStore} through DynamoDB
 * <p></p>
 * Expects token items to have the following attributes:
 * <p></p>
 * - token: a string key
 * - user: the string ID of the user the token is for
 * - created: numeric creation date of the token as an epoch unix timestamp
 * - expiration: expiration date of the token in same format as creation
 * - tags: string tags for the token
 */
class DynamoDBTokenStore extends DynamoDBStore implements ITokenStore {

    private final String tableName;

    /**
     * Creates a {@link DynamoDBTokenStore}
     *
     * @param tableName {@link String} table name for this
     * @param region    {@link Regions} to use
     */
    DynamoDBTokenStore(String tableName, Regions region) {
        super(region);
        this.tableName = tableName;
    }

    @Override
    public Token create(Token object) {
        String id = UUID.randomUUID().toString();

        object.setToken(id);

        Map<String, AttributeValue> item = fromToken(object);

        PutItemRequest request = new PutItemRequest()
                .withTableName(tableName)
                .withItem(item);

        getClient().putItem(request);

        return object;
    }

    @Override
    public Token read(String id) {
        Map<String, AttributeValue> key = new HashMap<>();
        key.put("token", new AttributeValue(id));

        GetItemRequest request = new GetItemRequest()
                .withTableName(tableName)
                .withKey(key)
                .withConsistentRead(true);

        GetItemResult result = getClient().getItem(request);

        Map<String, AttributeValue> item = result.getItem();

        if (item == null) {
            return null; // early return on no result
        }

        Token t = fromDynamo(item);

        if (t == null) {
            throw new IllegalArgumentException("Provided ID must be ID of an item in this" +
                    "DynamoDBTokenStore's table, that satisfies DynamoDB token representation");
        }

        return t;
    }

    @Override
    public void update(Token object) {
        Map<String, AttributeValue> item = fromToken(object);

        PutItemRequest request = new PutItemRequest()
                .withTableName(tableName)
                .withItem(item);

        getClient().putItem(request);
    }

    @Override
    public void delete(String id) {
        Map<String, AttributeValue> key = new HashMap<>();
        key.put("token", new AttributeValue(id));

        DeleteItemRequest request = new DeleteItemRequest()
                .withTableName(tableName)
                .withKey(key);

        getClient().deleteItem(request);
    }

    /**
     * Creates an {@link Token} from the specified DynamoDB item
     *
     * @param item {@link Map} representing a DynamoDB item
     * @return the created {@link Token}, or null if item does not satisfy
     * conditions for a DynamoDB representation of a {@link Token}
     */
    private static Token fromDynamo(Map<String, AttributeValue> item) {

        if (!item.containsKey("token")
                || !item.containsKey("user")
                || !item.containsKey("created")
                || !item.containsKey("expiration")
                || !item.containsKey("tags")) { // assert preconditions
            return null;
        }

        String token = item.get("token").getS();

        String userId = item.get("user").getS();

        String createdS = item.get("created").getN();

        String expirationS = item.get("expiration").getN();

        String tags = item.get("tags").getS();

        long createdL, expirationL;

        try {
            createdL = Long.parseLong(createdS);
            expirationL = Long.parseLong(expirationS);
        } catch (NumberFormatException e) {
            return null;
        }

        Instant created = Instant.ofEpochMilli(createdL), expiration = Instant.ofEpochMilli(expirationL);

        return new Token(token, created, expiration, tags, userId);
    }

    /**
     * Creates a DynamoDB item
     * @param t {@link Token} to create the item from
     * @return {@link Map} representing the {@link Token}
     */
    private static Map<String, AttributeValue> fromToken(Token t) {
        Map<String, AttributeValue> item = new HashMap<>();

        item.put("token", new AttributeValue().withS(t.getToken()));
        item.put("created", new AttributeValue().withN(
                Long.toString(t.getIssuedDate().toEpochMilli())));
        item.put("expiration", new AttributeValue().withN(
                Long.toString(t.getExpiryDate().toEpochMilli())));
        item.put("user", new AttributeValue().withS(t.getUserID()));
        item.put("tags", new AttributeValue().withS(t.getTag()));

        return item;
    }
}
