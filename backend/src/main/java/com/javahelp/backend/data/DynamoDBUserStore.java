package com.javahelp.backend.data;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.DeleteItemRequest;
import com.amazonaws.services.dynamodbv2.model.GetItemRequest;
import com.amazonaws.services.dynamodbv2.model.GetItemResult;
import com.amazonaws.services.dynamodbv2.model.PutItemRequest;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import com.amazonaws.services.dynamodbv2.model.UpdateItemRequest;
import com.javahelp.model.user.ClientUserInfo;
import com.javahelp.model.user.ProviderUserInfo;
import com.javahelp.model.user.User;
import com.javahelp.model.user.UserInfo;
import com.javahelp.model.user.UserPassword;
import com.javahelp.model.user.UserType;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;

/**
 * Implementation of {@link IUserStore} through DynamoDB.
 * <p>
 * Expects {@link User} + {@link UserPassword} representations of the form:
 * - id: The string ID of the user
 * - username: The string username of the user
 * - infotype: The type of the user, either CLIENT or PROVIDER
 * - salthash: Base64 string of the concatenated salt and hash (+ salt length)
 * - userinfo: lazy Base64 serialized UserInfo
 * <p></p>
 * The structure of the items in this table should not be depended on outside of this class
 * as elements, especially the Base64 encoded serialization of {@link UserInfo} may change in the
 * future
 */
public class DynamoDBUserStore extends DynamoDBStore implements IUserStore {

    private String tableName;

    /**
     * Creates a new {@link DynamoDBStore}
     *
     * @param tableName {@link String} table name to be used
     * @param region    {@link Regions} to be used
     */
    public DynamoDBUserStore(String tableName, Regions region) {
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

    public List<User> query(HashMap<String, ArrayList<Integer>> constraint) {
        String question = (String) constraint.keySet().toArray()[0];
        Integer answer = constraint.get(question).get(0);

        String keyConditionExpression = String.format("%s = :question", question);
        Map<String, AttributeValue> expressionAttributeValues = new HashMap<>();
        expressionAttributeValues.put(":question", new AttributeValue(String.valueOf(answer)));

        ScanRequest scanRequest = new ScanRequest()
                .withTableName(this.tableName)
                .withFilterExpression(keyConditionExpression)
                .withExpressionAttributeValues(expressionAttributeValues);

        ScanResult result = getClient().scan(scanRequest);

        List<User> userList = new ArrayList<>();
        for (Map<String, AttributeValue> item : result.getItems()) {
            User user = userFromDynamo(item);
            userList.add(user);
        }

        return userList;
    }

    /**
     * @param item {@link Map} containing representation of {@link User} and {@link UserPassword}
     * @return {@link User} contained or null if invalid representation
     */
    private static User userFromDynamoDoc(Map<String, Object> item) {
        if (!item.containsKey("id") ||
                !item.containsKey("username") ||
                !item.containsKey("infotype") || // not actually needed in current implementation
                !item.containsKey("userinfo")) {
            return null;
        }

        String id = (String) item.get("id"), username = (String) item.get("username");

        UserInfo info = (UserInfo) item.get("userinfo");

        return new User(id, info, username);
    }

    @Override
    public void update(User object) {

        Map<String, AttributeValue> key = new HashMap<>();
        key.put("id", new AttributeValue(object.getStringID()));

        UpdateItemRequest request = new UpdateItemRequest()
                .withTableName(tableName)
                .withKey(key)
                .withUpdateExpression("SET username=:unameval, " +
                        "infotype=:typeval, " +
                        "userinfo=:infoval")
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

        String userInfoValue = encodeUserInfo(u.getUserInfo());

        if (userInfoValue == null) {
            // fail fast on invalid userinfo representation
            throw new IllegalArgumentException("User must have a UserInfo supported by this" +
                    "implementation");
        }

        user.put("userinfo", new AttributeValue().withS(userInfoValue));

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

        String infoVal = encodeUserInfo(info);

        if (infoVal != null) {
            user.put(":infoval", new AttributeValue().withS(infoVal));
        }

        return user;
    }

    /**
     * @param item {@link Map} containing representation of {@link User} and {@link UserPassword}
     * @return {@link User} contained or null if invalid representation
     */
    private static User userFromDynamo(Map<String, AttributeValue> item) {
        if (!item.containsKey("id") ||
                !item.containsKey("username") ||
                !item.containsKey("infotype") || // not actually needed in current implementation
                !item.containsKey("userinfo")) {
            return null;
        }

        String id = item.get("id").getS(), username = item.get("username").getS();

        String encodedUserInfo = item.get("userinfo").getS();

        UserInfo info = decodeUserInfo(encodedUserInfo);

        if (info == null) {
            return null;
        }

        return new User(id, info, username);
    }

    /**
     * Encodes a {@link UserInfo} as a Base64 {@link String}
     *
     * @param u {@link UserInfo} to encode
     * @return Base64 encoded {@link String} representing a {@link UserInfo}
     * or null if the encoding fails
     */
    private static String encodeUserInfo(UserInfo u) {
        boolean isClient = u.getType() == UserType.CLIENT;

        Serializable s = isClient ?
                new SerializableClientUserInfo((ClientUserInfo) u) :
                new SerializableProviderUserInfo((ProviderUserInfo) u);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try {
            ObjectOutputStream objectStream = new ObjectOutputStream(outputStream);
            objectStream.writeObject(s);
            objectStream.flush();
            objectStream.close();

            byte[] serialized = outputStream.toByteArray();

            return Base64.getEncoder().encodeToString(serialized);
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * Decodes a {@link UserInfo} from a Base64 encoded {@link String}
     *
     * @param s {@link String} to decode from
     * @return {@link UserInfo} created, or null
     */
    private static UserInfo decodeUserInfo(String s) {
        byte[] serialized = Base64.getDecoder().decode(s);

        ByteArrayInputStream inputStream = new ByteArrayInputStream(serialized);

        try {
            ObjectInputStream objectStream = new ObjectInputStream(inputStream);
            Supplier<UserInfo> info = (Supplier<UserInfo>) objectStream.readObject();
            objectStream.close();

            return info.get();
        } catch (IOException | ClassNotFoundException e) {
            return null;
        }
    }

    /**
     * Serializable wrapper around fields of a {@link ProviderUserInfo}
     */
    private static class SerializableProviderUserInfo implements Serializable, Supplier<UserInfo> {

        String email, practice, address, phoneNumber;
        HashMap<String, Integer> attributeMap;

        boolean certified;

        /**
         * Instantiates a {@link SerializableProviderUserInfo}
         */
        public SerializableProviderUserInfo() {

        }

        /**
         * Constructs a {@link SerializableProviderUserInfo} object
         *
         * @param base {@link ProviderUserInfo} to construct from
         */
        public SerializableProviderUserInfo(ProviderUserInfo base) {
            email = base.getEmailAddress();
            practice = base.getPracticeName();
            address = base.getAddress();
            phoneNumber = base.getPhoneNumber();
            certified = base.isCertified();
            attributeMap = base.getAllAttribute();
        }

        @Override
        public UserInfo get() {
            ProviderUserInfo p = new ProviderUserInfo(email, address, phoneNumber, practice);
            p.setCertified(certified);
            return p;
        }
    }

    /**
     * Serializable wrapper around fields of a {@link ClientUserInfo}
     */
    private static class SerializableClientUserInfo implements Serializable, Supplier<UserInfo> {

        String email, first, last, address, phoneNumber;

        /**
         * Constructs a {@link SerializableClientUserInfo} object
         *
         * @param base {@link ClientUserInfo} to construct from
         */
        public SerializableClientUserInfo(ClientUserInfo base) {
            email = base.getEmailAddress();
            first = base.getFirstName();
            last = base.getLastName();
            address = base.getAddress();
            phoneNumber = base.getPhoneNumber();
        }

        /**
         * Instantiates a {@link SerializableClientUserInfo}
         */
        public SerializableClientUserInfo() {

        }

        @Override
        public UserInfo get() {
            return new ClientUserInfo(email, address, phoneNumber, first, last);
        }
    }
}
