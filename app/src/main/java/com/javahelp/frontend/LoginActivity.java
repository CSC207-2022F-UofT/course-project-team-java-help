package com.javahelp.frontend;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.javahelp.R;
import com.javahelp.databinding.ActivityLoginBinding;


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

        viewModel.getUsername().observe(this, o -> {
//            binding.username.setText(o);
        });
        viewModel.getPassword().observe(this, o -> {
//            binding.password.setText(o);
        });
        viewModel.shouldStaySignedIn().observe(this, binding.staySignedIn::setChecked);
        viewModel.isLoggingIn().observe(this, o -> {
            binding.progressBar.setVisibility(o ? View.VISIBLE : View.GONE);

        });
        viewModel.getLoginError().observe(this, o -> {
            binding.loginErrorText.setVisibility(o.isPresent() ? View.VISIBLE : View.GONE);
            o.ifPresent(s -> binding.loginErrorText.setText(s));
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

    private boolean usernameChecker() {
        if (binding.username.getText().toString().isEmpty()) {
            return false;
        }
        return true;
    }

    private void loginAttempt() {
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
