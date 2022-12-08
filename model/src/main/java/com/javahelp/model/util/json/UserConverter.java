package com.javahelp.model.util.json;

import com.javahelp.model.user.User;
import com.javahelp.model.user.UserInfo;

import jakarta.json.Json;
import jakarta.json.JsonObject;

/**
 * Converts {@link User}s to {@link JsonObject}s
 */
public class UserConverter implements IJSONConverter<User> {

    private static final UserConverter instance = new UserConverter(UserInfoConverter.getInstance());

    /**
     * Private constructor
     *
     * @param info {@link IJSONConverter} for {@link UserInfo} to use
     */
    private UserConverter(IJSONConverter<UserInfo> info) {
        userInfoConverter = info;

    }

    IJSONConverter<UserInfo> userInfoConverter;

    /**
     * @return instance of {@link UserConverter}
     */
    public static UserConverter getInstance() {
        return instance;
    }

    @Override
    public JsonObject toJSON(User input) {
        JsonObject userInfo = userInfoConverter.toJSON(input.getUserInfo());
        if (userInfo == null) {
            return null;
        }
        return Json.createObjectBuilder()
                .add("info", userInfo)
                .add("id", input.getStringID())
                .add("username", input.getUsername())
                .build();
    }

    @Override
    public User fromJSON(JsonObject object) {
        if (object.containsKey("info")
        && object.containsKey("id")
        && object.containsKey("username")) {
            String username = object.getString("username");
            String id = object.getString("id");
            JsonObject info = object.getJsonObject("info");
            UserInfo u = userInfoConverter.fromJSON(info);
            if (u == null) {
                return null;
            }
            return new User(id, u, username);
        }
        return null;
    }
}
