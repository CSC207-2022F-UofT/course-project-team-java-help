package com.javahelp.frontend.domain.user.login;

import com.javahelp.frontend.domain.user.register.IRegisterOutput;
import com.javahelp.frontend.domain.user.register.RegisterResult;
import com.javahelp.model.token.Token;
import com.javahelp.model.user.ProviderUserInfo;
import com.javahelp.model.user.User;
import com.javahelp.model.user.UserPassword;

import org.junit.Before;

import java.util.Optional;

public class RegisterInteractorTest {
    Optional<RegisterResult> result = null;
    String errorOutput = "";
    IRegisterOutput output;
    ISaltDataAccess mockedSaltDataAccess;
    User user;
    UserPassword password;
    ProviderUserInfo userInfo;
    byte[] salt;

    @Before
    public void setup() {

        output = new IRegisterOutput() {
            @Override
            public void success(User user, Token token) {
                result = Optional.of(new RegisterResult(user, token));
            }

            @Override
            public void failure() {
                result = Optional.of(new RegisterResult("Failed to authenticate"));
            }

            @Override
            public void error(String errorMessage) {
                result = Optional.empty();
                errorOutput = errorMessage;
            }

            @Override
            public void abort() {
                result = Optional.empty();
            }
        };

    }

    }



