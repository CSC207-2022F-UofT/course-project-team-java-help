package com.javahelp.backend.endpoint.login;

import static com.javahelp.backend.endpoint.APIGatewayResponse.BAD_REQUEST;
import static com.javahelp.backend.endpoint.APIGatewayResponse.OK;

import com.amazonaws.HttpMethod;
import com.javahelp.backend.data.ITokenStore;
import com.javahelp.backend.data.IUserStore;
import com.javahelp.backend.domain.user.login.ILoginInput;
import com.javahelp.backend.domain.user.login.LoginInteractor;
import com.javahelp.backend.domain.user.login.LoginResult;
import com.javahelp.backend.endpoint.APIGatewayResponse;
import com.javahelp.backend.endpoint.HTTPHandler;
import com.javahelp.model.user.UserPassword;

import java.util.Map;

import jakarta.json.JsonObject;

/**
 * Handler for logging in and retrieving a token
 * <p>
 * Expects POST requests with JSON body to /login meeting the following:
 * <ul>
 *      <li>Contains one of email, username, or id keys with string value</li>
 *      <li>Contains boolean stayLoggedIn key</li>
 *      <li>Contains saltHash key with base64 encoded salt + hash</li>
 * </ul>
 */
public class LoginHandler extends HTTPHandler implements ILoginInput {

    private String username, email, id;

    private boolean stayLoggedIn;

    private UserPassword password;

    @Override
    public APIGatewayResponse getResponse(JsonObject body, HttpMethod method, Map<String, String[]> headers, Map<String, String[]> parameters) {

        email = body.getString("email", null);
        username = body.getString("username", null);
        id = body.getString("id", null);

        if (email == null && username == null && id == null) {
            return APIGatewayResponse.error(BAD_REQUEST, "Request must contain one of \"id\", \"email\", \"username\"");
        }

        String saltHash = body.getString("saltHash");
        password = new UserPassword(saltHash);

        stayLoggedIn = body.getBoolean("stayLoggedIn");

        LoginInteractor interactor = new LoginInteractor(IUserStore.getDefaultImplementation(),
                ITokenStore.getDefaultImplementation());

        LoginResult result = interactor.login(this);

        LoginResponse response;

        if (result.isSuccess()) {
            response = new LoginResponse(result.getUser(), result.getToken());
        } else {
            response = new LoginResponse(result.getErrorMessage());
        }

        return APIGatewayResponse.builder()
                .setStatusCode(OK)
                .setObjectBody(response)
                .build();
    }

    @Override
    public String[] requiredBodyFields() {
        return new String[] {"saltHash", "stayLoggedIn"};
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getID() {
        return id;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public UserPassword getPassword() {
        return password;
    }

    @Override
    public boolean stayLoggedIn() {
        return stayLoggedIn;
    }
}
