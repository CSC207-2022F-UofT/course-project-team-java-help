package com.javahelp.model.util.json;

import com.javahelp.model.user.ClientUserInfo;
import com.javahelp.model.user.UserType;

import jakarta.json.Json;
import jakarta.json.JsonObject;

/**
 * Converts {@link com.javahelp.model.user.ClientUserInfo} to {@link jakarta.json.JsonObject}
 */
public class ClientUserInfoConverter implements IJSONConverter<ClientUserInfo> {

    @Override
    public JsonObject toJSON(ClientUserInfo input) {
        return Json.createObjectBuilder()
                .add("email", input.getEmailAddress())
                .add("firstName", input.getFirstName())
                .add("lastName", input.getLastName())
                .add("address", input.getAddress())
                .add("phoneNumber", input.getPhoneNumber())
                .add("type", input.getType().name())
                .build();
    }

    @Override
    public ClientUserInfo fromJSON(JsonObject object) {
        if (object.containsKey("email") && object.containsKey("firstName")
                && object.containsKey("lastName") && object.containsKey("address")
                && object.containsKey("phoneNumber") && object.containsKey("type")
                && UserType.CLIENT.name().equals(object.getString("type"))) {
            String email = object.getString("email");
            String firstName = object.getString("firstName");
            String lastName = object.getString("lastName");
            String address = object.getString("address");
            String phoneNumber = object.getString("phoneNumber");
            return new ClientUserInfo(email, address, phoneNumber, firstName, lastName);
        }
        return null;
    }
}
