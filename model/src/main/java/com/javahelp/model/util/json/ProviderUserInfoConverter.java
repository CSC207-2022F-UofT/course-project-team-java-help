package com.javahelp.model.util.json;

import com.javahelp.model.user.ProviderUserInfo;
import com.javahelp.model.user.UserType;

import jakarta.json.Json;
import jakarta.json.JsonObject;

/**
 * Converts {@link ProviderUserInfo} to {@link JsonObject}
 */
public class ProviderUserInfoConverter implements IJSONConverter<ProviderUserInfo> {

    private static final ProviderUserInfoConverter instance = new ProviderUserInfoConverter();

    /**
     * Private constructor
     */
    private ProviderUserInfoConverter() {

    }

    /**
     * @return {@link ProviderUserInfoConverter} instance
     */
    public static ProviderUserInfoConverter getInstance() {
        return instance;
    }

    @Override
    public JsonObject toJSON(ProviderUserInfo input) {
        return Json.createObjectBuilder()
                .add("practiceName", input.getPracticeName())
                .add("email", input.getEmailAddress())
                .add("address", input.getAddress())
                .add("certified", input.isCertified())
                .add("phoneNumber", input.getPhoneNumber())
                .add("type", input.getType().name())
                .build();
    }

    @Override
    public ProviderUserInfo fromJSON(JsonObject object) {
        if (object.containsKey("email") && object.containsKey("practiceName")
                && object.containsKey("certified") && object.containsKey("address")
                && object.containsKey("phoneNumber") && object.containsKey("type")
                && UserType.PROVIDER.name().equals(object.getString("type"))) {
            String email = object.getString("email");
            String practiceName = object.getString("practiceName");
            boolean certified = object.getBoolean("certified");
            String address = object.getString("address");
            String phoneNumber = object.getString("phoneNumber");
            ProviderUserInfo p = new ProviderUserInfo(email, address, phoneNumber, practiceName);
            p.setCertified(certified);
            return p;
        }
        return null;
    }
}
