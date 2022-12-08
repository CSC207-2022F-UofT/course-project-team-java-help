package com.javahelp.backend.endpoint.user;


import static com.javahelp.backend.endpoint.APIGatewayResponse.BAD_REQUEST;
import static com.javahelp.backend.endpoint.APIGatewayResponse.INTERNAL_SERVER_ERROR;
import static com.javahelp.backend.endpoint.APIGatewayResponse.OK;

import com.amazonaws.HttpMethod;
import com.javahelp.backend.data.ITokenStore;
import com.javahelp.backend.data.IUserStore;
import com.javahelp.backend.domain.user.login.ILoginInput;
import com.javahelp.backend.domain.user.login.LoginInteractor;
import com.javahelp.backend.domain.user.login.LoginResult;
import com.javahelp.backend.domain.user.register.IUserRegisterInputBoundary;
import com.javahelp.backend.domain.user.register.UserRegisterInteractor;
import com.javahelp.backend.domain.user.register.UserRegisterResult;
import com.javahelp.backend.domain.user.register.client.ClientRegisterInteractor;
import com.javahelp.backend.domain.user.register.client.IClientRegisterInputBoundary;
import com.javahelp.backend.domain.user.register.provider.IProviderRegisterInputBoundary;
import com.javahelp.backend.domain.user.register.provider.ProviderRegisterInteractor;
import com.javahelp.backend.endpoint.APIGatewayResponse;
import com.javahelp.backend.endpoint.HTTPHandler;
import com.javahelp.model.user.ClientUserInfo;
import com.javahelp.model.user.ProviderUserInfo;
import com.javahelp.model.user.User;
import com.javahelp.model.user.UserPassword;
import com.javahelp.model.user.UserType;
import com.javahelp.model.util.json.TokenConverter;
import com.javahelp.model.util.json.UserConverter;

import java.util.Map;

import jakarta.json.Json;
import jakarta.json.JsonObject;

/**
 * Handler to register a {@link User}
 */
public class RegisterHandler extends HTTPHandler implements IClientRegisterInputBoundary, IProviderRegisterInputBoundary, ILoginInput {

    private User user;

    private String saltHash;

    @Override
    public String[] requiredBodyFields() {
        return new String[]{"saltHash", "user"};
    }

    /**
     * @return {@link ClientUserInfo} for this request's {@link User}
     */
    private ClientUserInfo getClientInfo() {
        return (ClientUserInfo) user.getUserInfo();
    }

    /**
     * @return {@link ProviderUserInfo} for this request's {@link User}
     */
    private ProviderUserInfo getProviderInfo() {
        return (ProviderUserInfo) user.getUserInfo();
    }

    /**
     * @return whether the request's {@link User} is a client
     */
    private boolean isClient() {
        return user.getUserInfo().getType() == UserType.CLIENT;
    }

    @Override
    public APIGatewayResponse getResponse(JsonObject body, HttpMethod method, Map<String, String[]> headers, Map<String, String[]> parameters, Map<String, String> pathParameters) {
        user = null;
        saltHash = null; // reset in case this is not the first time the handler has been called

        try {
            user = UserConverter.getInstance().fromJSON(body.getJsonObject("user"));
            saltHash = body.getString("saltHash");
        } catch (ClassCastException ignored) {

        }

        if (user == null) {
            return APIGatewayResponse.error(BAD_REQUEST, "User field does not contain valid user object");
        } else if (saltHash == null) {
            return APIGatewayResponse.error(BAD_REQUEST, "Salt hash field does not contain a valid salt hash");
        }

        IUserStore userStore = IUserStore.getDefaultImplementation();

        UserRegisterInteractor<? extends IUserRegisterInputBoundary, ?>
                interactor = isClient() ?
                new ClientRegisterInteractor(userStore) :
                new ProviderRegisterInteractor(userStore);

        UserRegisterResult result = ((UserRegisterInteractor<IUserRegisterInputBoundary, ?>) interactor)
                .register(this);

        user = result.getUser();

        if (!result.isSuccess()) {
            return APIGatewayResponse.error(INTERNAL_SERVER_ERROR, result.getErrorMessage());
        }

        LoginInteractor loginInteractor = new LoginInteractor(
                IUserStore.getDefaultImplementation(), ITokenStore.getDefaultImplementation());

        LoginResult loginResult = loginInteractor.login(this);

        if (!loginResult.isSuccess()) {
            return APIGatewayResponse.error(INTERNAL_SERVER_ERROR, result.getErrorMessage());
        }

        JsonObject userJson = UserConverter.getInstance().toJSON(user);
        JsonObject tokenJson = TokenConverter.getInstance().toJSON(loginResult.getToken());

        JsonObject json = Json.createObjectBuilder()
                .add("user", userJson)
                .add("token", tokenJson)
                .build();

        return APIGatewayResponse.builder()
                .setJSONBody(json.toString())
                .setStatusCode(OK)
                .build();
    }

    @Override
    public String getEmailAddress() {
        return user.getUserInfo().getEmailAddress();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public String getID() {
        return user.getStringID();
    }

    @Override
    public String getEmail() {
        return user.getUserInfo().getEmailAddress();
    }

    @Override
    public UserPassword getPassword() {
        return new UserPassword(saltHash);
    }

    @Override
    public boolean stayLoggedIn() {
        return false;
    }

    @Override
    public String getSaltAndHash() {
        return saltHash;
    }

    @Override
    public String getAddress() {
        return isClient() ?
                getClientInfo().getAddress() : getProviderInfo().getAddress();
    }

    @Override
    public String getPhoneNumber() {
        return isClient() ? getClientInfo().getPhoneNumber() : getProviderInfo().getPhoneNumber();
    }

    @Override
    public String getPracticeName() {
        return getProviderInfo().getPracticeName();
    }

    @Override
    public boolean getCertified() {
        return getProviderInfo().isCertified();
    }

    @Override
    public String getFirstName() {
        return getClientInfo().getFirstName();
    }

    @Override
    public String getLastName() {
        return getClientInfo().getLastName();
    }
}
