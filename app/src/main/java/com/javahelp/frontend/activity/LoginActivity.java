package com.javahelp.frontend.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.javahelp.R;
import com.javahelp.databinding.ActivityLoginBinding;
import com.javahelp.frontend.domain.user.login.LoginResult;
import com.javahelp.frontend.util.auth.SharedPreferencesAuthInformationProvider;
import com.javahelp.model.token.Token;
import com.javahelp.model.user.User;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 * Activity to log in a user
 */
public class LoginActivity extends AppCompatActivity {

    /**
     * Permission request code to get internet access for loggin in
     */
    private static final int REQUEST_INTERNET_LOGIN = 1;

    LoginViewModel viewModel;
    ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        binding.setData(viewModel);
        binding.setLifecycleOwner(this);

        viewModel.shouldStaySignedIn().observe(this, binding.staySignedIn::setChecked);

        viewModel.isLoggingIn().observe(this, o -> {
            binding.progressBar.setVisibility(o ? View.VISIBLE : View.GONE);
            binding.loginButton.setEnabled(!o);
        });

        viewModel.getLoginResult().observe(this, this::updateOnLoginResult);

        binding.loginButton.setOnClickListener(this::loginClick);
    }

    /**
     * Called when the login button is clicked
     *
     * @param v {@link View} that was clicked
     */
    private void loginClick(View v) {
        viewModel.setUsername(binding.username.getText().toString());
        viewModel.setPassword(binding.password.getText().toString());
        viewModel.setStaySignedIn(binding.staySignedIn.isChecked());
        loginAttempt();
    }

    /**
     * Updates this {@link LoginActivity} to reflect the passed login result
     *
     * @param result {@link Optional} {@link LoginResult} to update on
     */
    private void updateOnLoginResult(Optional<LoginResult> result) {
        if (result.isPresent()) {
            updateOnPresentLoginResult(result.get());
        } else {
            binding.loginErrorText.setVisibility(View.GONE);
        }
    }

    /**
     * Updates the UI based on a {@link LoginResult}. This is called with non-null login results, meaning
     * a login has been attempted, and has either succeeded or failed. This is unlike updateOnLoginResult
     * which is called with an {@link Optional} {@link LoginResult}, and calls this method if that
     * {@link Optional} is present.
     *
     * @param result {@link LoginResult} of login attempt
     */
    private void updateOnPresentLoginResult(LoginResult result) {
        if (result.isSuccess()) {
            binding.loginErrorText.setText("Login successful");
            storeCredentials(result.getUser(), result.getToken());
        } else {
            binding.loginErrorText.setText(result.getErrorMessage());
        }
        binding.loginErrorText.setVisibility(View.VISIBLE);
    }

    /**
     * Stores the accepted credentials
     *
     * @param user  {@link User} to store
     * @param token {@link Token} to store
     */
    private void storeCredentials(User user, Token token) {
        SharedPreferencesAuthInformationProvider credentialStore =
                new SharedPreferencesAuthInformationProvider(this);
        try {
            credentialStore.setTokenString(token.getToken());
            credentialStore.setUserID(user.getStringID());
        } catch (NoSuchPaddingException | IllegalBlockSizeException | NoSuchAlgorithmException | BadPaddingException | InvalidKeyException ignored) {
            Toast.makeText(this, "Error Storing Credentials", Toast.LENGTH_LONG);
        }
    }

    /**
     * Initiates a login request
     */
    private void loginAttempt() {
        if (binding.username.getText().toString().isEmpty() || binding.password.getText().toString().isEmpty()) {
            viewModel.setLoginError("Please enter username and password");
            return; // early return on invalid username or password
        }
        requestPermissions(new String[]{Manifest.permission.INTERNET}, REQUEST_INTERNET_LOGIN);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_INTERNET_LOGIN:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    viewModel.attemptLogin();
                } else {
                    Toast.makeText(this, "Internet access denied, cannot log in", Toast.LENGTH_LONG);
                }
                return;
        }
    }
}
