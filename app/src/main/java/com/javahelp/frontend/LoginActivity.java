package com.javahelp.frontend;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.javahelp.R;
import com.javahelp.databinding.ActivityLoginBinding;
import com.javahelp.frontend.util.auth.SharedPreferencesAuthInformationProvider;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;


public class LoginActivity extends AppCompatActivity {

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

        viewModel.getLoginResult().observe(this, o -> {
            if (o.isPresent()) {
                if (o.get().isSuccess()) {
                    binding.loginErrorText.setText("Login successful");
                    SharedPreferencesAuthInformationProvider credentialStore =
                            new SharedPreferencesAuthInformationProvider(this);
                    try {
                        credentialStore.setTokenString(o.get().getToken().getToken());
                        credentialStore.setUserID(o.get().getUser().getStringID());
                    } catch (NoSuchPaddingException | IllegalBlockSizeException | NoSuchAlgorithmException | BadPaddingException | InvalidKeyException ignored) {
                        // quiet fail for now, maybe show a toast or something in the future
                    }
                } else {
                    binding.loginErrorText.setText(o.get().getErrorMessage());
                }
                binding.loginErrorText.setVisibility(View.VISIBLE);
            } else {
                binding.loginErrorText.setVisibility(View.GONE);
            }
        });

        binding.loginButton.setOnClickListener(view -> {
            viewModel.setUsername(binding.username.getText().toString());
            viewModel.setPassword(binding.password.getText().toString());
            viewModel.setStaySignedIn(binding.staySignedIn.isChecked());
            loginAttempt();
        });

        // link the button to the provider Registeration page
        binding.botPReg.setOnClickListener(view -> {
            Intent intent = null;
            intent = new Intent(LoginActivity.this, ProviderRegistrationActivity.class);
            startActivity(intent);
        });

        // link the button to the client Registeration page
        binding.botCReg.setOnClickListener(view -> {
            Intent intent = null;
            intent = new Intent(LoginActivity.this, ClientRegistrationActivity.class);
            startActivity(intent);
        });
    }

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

                }
                return;
        }
    }
}
