package com.javahelp.backend.data;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.DeleteItemRequest;
import com.amazonaws.services.dynamodbv2.model.GetItemRequest;
import com.amazonaws.services.dynamodbv2.model.GetItemResult;
import com.amazonaws.services.dynamodbv2.model.PutItemRequest;
import com.amazonaws.services.dynamodbv2.model.QueryRequest;
import com.amazonaws.services.dynamodbv2.model.QueryResult;
import com.amazonaws.services.dynamodbv2.model.UpdateItemRequest;
import com.javahelp.model.user.ClientUserInfo;
import com.javahelp.model.user.ProviderUserInfo;
import com.javahelp.model.user.User;
import com.javahelp.model.user.UserInfo;
import com.javahelp.model.user.UserPassword;
import com.javahelp.model.user.UserType;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Implementation of {@link IUserStore} through DynamoDB.
 * <p>
 * Expects {@link User} + {@link UserPassword} representations of the form:
 * - id: The string ID of the user
 * - username: The string username of the user
 * - infotype: The type of the user, either CLIENT or PROVIDER
 * - salthash: Base64 string of the concatenated salt and hash (+ salt length)
 * - user info attributes with corresponding names
 * <p></p>
 * The structure of the items in this table should not be depended on outside of this class
 * as elements, especially the Base64 encoded serialization of {@link UserInfo} may change in the
 * future
 */
public class DynamoDBUserStore extends DynamoDBStore implements IUserStore {

    private static final String CLIENT_UPDATE = "SET username=:unameval, " +
            "infotype=:typeval, " +
            "firstName=:firstnameval, " +
            "lastName=:lastnameval, " +
            "address=:addressval, " +
            "phoneNumber=:phonenumberval, " +
            "email=:emailval " +
            "REMOVE certified, practiceName",
            PROVIDER_UPDATE = "SET username=:unameval, " +
                    "infotype=:typeval, " +
                    "practiceName=:practicenameval, " +
                    "certified=:certifiedval, " +
                    "address=:addressval, " +
                    "phoneNumber=:phonenumberval, " +
                    "email=:emailval " +
                    "REMOVE firstName, lastName";

    private String tableName;

    /**
     * Creates a new {@link DynamoDBStore}
     *
     * @param tableName {@link String} table name to be used
     * @param region    {@link Regions} to be used
     */
    DynamoDBUserStore(String tableName, Regions region) {
        super(region);
        this.tableName = tableName;
    }

    @Override
    public User read(String id) {
        Map<String, AttributeValue> key = new HashMap<>();
        key.put("id", new AttributeValue(id));

        GetItemRequest request = new GetItemRequest()
                .withTableName(tableName)
                .withKey(key);

        GetItemResult result = getClient().getItem(request);

        if (result.getItem() == null) {
            return null;
        }

        return userFromDynamo(result.getItem());
    }

    @Override
    public User readByUsername(String username) {
        Map<String, AttributeValue> values = new HashMap<>();
        values.put(":usernameval", new AttributeValue().withS(username));

        QueryRequest request = new QueryRequest()
                .withTableName(tableName)
                .withIndexName("username-index")
                .withKeyConditionExpression("username=:usernameval")
                .withExpressionAttributeValues(values);

        QueryResult result = getClient().query(request);

        if (result.getCount() == 0) {
            return null;
        }

        return userFromDynamo(result.getItems().get(0));
    }

    @Override
    public User readByEmail(String email) {
        Map<String, AttributeValue> values = new HashMap<>();
        values.put(":emailval", new AttributeValue().withS(email));

        QueryRequest request = new QueryRequest()
                .withTableName(tableName)
                .withIndexName("email-index")
                .withKeyConditionExpression("email=:emailval")
                .withExpressionAttributeValues(values);

        QueryResult result = getClient().query(request);

        if (result.getCount() == 0) {
            return null;
        }

        return userFromDynamo(result.getItems().get(0));
    }

    @Override
    public void update(User object) {

        Map<String, AttributeValue> key = new HashMap<>();
        key.put("id", new AttributeValue(object.getStringID()));

        UpdateItemRequest request = new UpdateItemRequest()
                .withTableName(tableName)
                .withKey(key)
                .withUpdateExpression(getUpdateString(object))
                .withExpressionAttributeValues(updateFromUser(object));

        getClient().updateItem(request);
    }

    @Override
    public void delete(String id) {
        Map<String, AttributeValue> key = new HashMap<>();
        key.put("id", new AttributeValue(id));

        DeleteItemRequest request = new DeleteItemRequest()
                .withTableName(tableName)
                .withKey(key);

        getClient().deleteItem(request);
    }

    @Override
    public User create(User u, UserPassword password) {
        String id = UUID.randomUUID().toString();

        u.setStringID(id);

        Map<String, AttributeValue> item = fromUserAndPassword(u, password);

        PutItemRequest request = new PutItemRequest()
                .withTableName(tableName)
                .withItem(item);

        getClient().putItem(request);

        return u;
    }

    @Override
    public void updatePassword(String userId, UserPassword password) {
        Map<String, AttributeValue> key = new HashMap<>();
        key.put("id", new AttributeValue(userId));

        UpdateItemRequest request = new UpdateItemRequest()
                .withTableName(tableName)
                .withKey(key)
                .withUpdateExpression("SET salthash=:salthashval")
                .withExpressionAttributeValues(updateFromUserPassword(password));

        getClient().updateItem(request);
    }

    @Override
    public UserPassword readPassword(String userId) {
        Map<String, AttributeValue> key = new HashMap<>();
        key.put("id", new AttributeValue(userId));

        GetItemRequest request = new GetItemRequest()
                .withTableName(tableName)
                .withKey(key);

        GetItemResult result = getClient().getItem(request);

        if (result.getItem() == null) {
            return null;
        }

        return userPasswordFromDynamo(result.getItem());
    }

    /**
     * Gets the update {@link String} for the specified {@link User}
     *
     * @param u {@link User} to update
     * @return {@link String} for update
     */
    private static String getUpdateString(User u) {
        return u.getUserInfo().getType() == UserType.CLIENT ? CLIENT_UPDATE : PROVIDER_UPDATE;
    }

    /**
     * Represents a {@link User} and {@link UserPassword} as an object in a DynamoDB table
     *
     * @param u {@link User} to represent
     * @param p {@link UserPassword} to represent
     * @return {@link Map} containing the representation, or null if cannot represent
     */
    private static Map<String, AttributeValue> fromUserAndPassword(User u, UserPassword p) {
        Map<String, AttributeValue> user = new HashMap<>();

        user.put("id", new AttributeValue().withS(u.getStringID()));
        user.put("username", new AttributeValue().withS(u.getUsername()));
        user.put("infotype", new AttributeValue().withS(u.getUserInfo().getType().name()));

        UserInfo info = u.getUserInfo();

        if (info.getType() == UserType.CLIENT) {
            ClientUserInfo c = (ClientUserInfo) info;
            user.put("firstName", new AttributeValue().withS(c.getFirstName()));
            user.put("lastName", new AttributeValue().withS(c.getLastName()));
            user.put("address", new AttributeValue().withS(c.getAddress()));
            user.put("phoneNumber", new AttributeValue().withS(c.getPhoneNumber()));
            user.put("email", new AttributeValue().withS(c.getEmailAddress()));
        } else {
            ProviderUserInfo i = (ProviderUserInfo) info;
            user.put("practiceName", new AttributeValue().withS(i.getPracticeName()));
            user.put("certified", new AttributeValue().withN(i.isCertified() ? "1" : "0"));
            user.put("address", new AttributeValue().withS(i.getAddress()));
            user.put("phoneNumber", new AttributeValue().withS(i.getPhoneNumber()));
            user.put("email", new AttributeValue().withS(i.getEmailAddress()));
        }

        user.put("salthash", new AttributeValue().withS(p.getBase64SaltHash()));

        return user;
    }

    /**
     * Represents the update fields for an {@link UpdateItemRequest} for a
     * {@link UserPassword}
     *
     * @param p {@link UserPassword} to update
     * @return {@link Map} containing representation of {@link UserPassword}
     */
    private static Map<String, AttributeValue> updateFromUserPassword(UserPassword p) {
        Map<String, AttributeValue> password = new HashMap<>();

        password.put(":salthashval", new AttributeValue().withS(p.getBase64SaltHash()));

        return password;
    }

    /**
     * @param item {@link Map} containing representation of {@link User} and {@link UserPassword}
     * @return {@link UserPassword} contained or null if invalid representation
     */
    private static UserPassword userPasswordFromDynamo(Map<String, AttributeValue> item) {
        if (!item.containsKey("salthash")) {
            return null;
        }

        return new UserPassword(item.get("salthash").getS());
    }

    /**
     * Represents the update fields for an {@link UpdateItemRequest}
     * for a {@link User} as a {@link Map}
     *
     * @param u {@link User} to update
     * @return {@link Map} with the corresponding fields
     */
    private static Map<String, AttributeValue> updateFromUser(User u) {
        Map<String, AttributeValue> user = new HashMap<>();

        user.put(":unameval", new AttributeValue().withS(u.getUsername()));
        user.put(":typeval", new AttributeValue().withS(u.getUserInfo().getType().name()));

        UserInfo info = u.getUserInfo();

        if (info.getType() == UserType.CLIENT) {
            ClientUserInfo c = (ClientUserInfo) info;
            user.put(":firstnameval", new AttributeValue().withS(c.getFirstName()));
            user.put(":lastnameval", new AttributeValue().withS(c.getLastName()));
            user.put(":addressval", new AttributeValue().withS(c.getAddress()));
            user.put(":phonenumberval", new AttributeValue().withS(c.getPhoneNumber()));
            user.put(":emailval", new AttributeValue().withS(c.getEmailAddress()));
        } else {
            ProviderUserInfo p = (ProviderUserInfo) info;
            user.put(":practicenameval", new AttributeValue().withS(p.getPracticeName()));
            user.put(":certifiedval", new AttributeValue().withN(p.isCertified() ? "1" : "0"));
            user.put(":addressval", new AttributeValue().withS(p.getAddress()));
            user.put(":phonenumberval", new AttributeValue().withS(p.getPhoneNumber()));
            user.put(":emailval", new AttributeValue().withS(p.getEmailAddress()));
        }

        return user;
    }

    /**
     * @param item {@link Map} containing representation of {@link User} and {@link UserPassword}
     * @return {@link User} contained or null if invalid representation
     */
    private static User userFromDynamo(Map<String, AttributeValue> item) {
        boolean isClient;
        if (!item.containsKey("id") ||
                !item.containsKey("email") ||
                !item.containsKey("username") ||
                !item.containsKey("infotype") ||
                (!(isClient = matchesClient(item)) && !matchesProvider(item))) {
            return null;
        }

        String id = item.get("id").getS(), username = item.get("username").getS();

        UserInfo info;

        if (isClient) {
            String email = item.get("email").getS(),
                    phoneNumber = item.get("phoneNumber").getS(),
                    firstName = item.get("firstName").getS(),
                    lastName = item.get("lastName").getS(),
                    address = item.get("address").getS();
            info = new ClientUserInfo(email, address, phoneNumber, firstName, lastName);
        } else {
            String email = item.get("email").getS(),
                    phoneNumber = item.get("phoneNumber").getS(),
                    practiceName = item.get("practiceName").getS(),
                    address = item.get("address").getS();
            boolean certified = !"0".equals(item.get("certified").getN());
            info = new ProviderUserInfo(email, address, phoneNumber, practiceName);
            ((ProviderUserInfo) info).setCertified(certified);
        }

        return new User(id, info, username);
    }

    /**
     * @param item {@link Map} to consider
     * @return whether the map matches the expected fields in a provider
     */
    private static boolean matchesProvider(Map<String, AttributeValue> item) {
        return item.containsKey("infotype") &&
                "PROVIDER".equals(item.get("infotype").getS()) &&
                item.containsKey("address") &&
                item.containsKey("phoneNumber") &&
                item.containsKey("certified") &&
                item.containsKey("practiceName");
    }

    /**
     * @param item {@link Map} to consider
     * @return whether the map matches the expected fields in a provider
     */
    private static boolean matchesClient(Map<String, AttributeValue> item) {
        return item.containsKey("infotype") &&
                "CLIENT".equals(item.get("infotype").getS()) &&
                item.containsKey("address") &&
                item.containsKey("firstName") &&
                item.containsKey("lastName") &&
                item.containsKey("phoneNumber");
    }
}
