package com.javahelp.model.util.json;

import com.javahelp.model.user.ClientUserInfo;
import com.javahelp.model.user.ProviderUserInfo;
import com.javahelp.model.user.UserInfo;
import com.javahelp.model.user.UserType;

import jakarta.json.JsonObject;

/**
 * Converts {@link UserInfo} to {@link JsonObject}
 */
public class UserInfoConverter implements IJSONConverter<UserInfo> {

    private static UserInfoConverter instance = new UserInfoConverter(ClientUserInfoConverter.getInstance(),
            ProviderUserInfoConverter.getInstance());

    private ClientUserInfoConverter clientConverter;

    private ProviderUserInfoConverter providerConverter;

    /**
     * Private constructor
     *
     * @param client   {@link ClientUserInfoConverter} for {@link ClientUserInfo}
     * @param provider {@link ProviderUserInfoConverter} for {@link ProviderUserInfo}
     */
    private UserInfoConverter(ClientUserInfoConverter client, ProviderUserInfoConverter provider) {
        clientConverter = client;
        providerConverter = provider;
    }

    /**
     * @return instance of {@link UserInfoConverter}
     */
    public static UserInfoConverter getInstance() {
        return instance;
    }

    @Override
    public JsonObject toJSON(UserInfo input) {
        switch (input.getType()) {
            case CLIENT:
                return clientConverter.toJSON((ClientUserInfo) input);
            case PROVIDER:
                return providerConverter.toJSON((ProviderUserInfo) input);
        }
        return null;
    }

    @Override
    public UserInfo fromJSON(JsonObject object) {
        if (object.containsKey("type")) {
            UserType type = Enum.valueOf(UserType.class, object.getString("type"));
            switch (type) {
                case CLIENT:
                    return clientConverter.fromJSON(object);
                case PROVIDER:
                    return providerConverter.fromJSON(object);
            }
        }
        return null;
    }
}
