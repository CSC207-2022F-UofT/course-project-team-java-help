package com.javahelp.frontend.view;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.javahelp.R;
import com.javahelp.databinding.ActivityProviderRegistrationBinding;
import com.javahelp.frontend.domain.user.register.RegisterResult;

import java.util.Optional;


/**
 * Activity to register a provider account
 */
public class ProviderRegistrationActivity extends AppCompatActivity {

    /**
     * Permission request code to get internet access for register
     */
    private static final int REQUEST_INTERNET_REGISTER = 1;

    ProviderRegistrationViewModel providerRegistrationViewModel;
    ActivityProviderRegistrationBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_provider_registration);
        providerRegistrationViewModel = new ViewModelProvider(this).get(ProviderRegistrationViewModel.class);
        binding.setProviderData(providerRegistrationViewModel);
        binding.setLifecycleOwner(this);

        providerRegistrationViewModel.getRegisterResult().observe(this, this::updateOnRegisterResult);
        binding.signupbtn.setOnClickListener(this::registerClick);
    }

    /**
     * Called when the register button is clicked
     *
     * @param v {@link View} that was clicked
     */
    private void registerClick(View v) {
        providerRegistrationViewModel.setUsername(binding.username.getText().toString());
        providerRegistrationViewModel.setPassword(binding.password.getText().toString());
        providerRegistrationViewModel.setEmail(binding.email.getText().toString());
        providerRegistrationViewModel.setAddress(binding.home.getText().toString());
        providerRegistrationViewModel.setPhone(binding.phonenumber.getText().toString());
        providerRegistrationViewModel.setPracticeName(binding.practicename.getText().toString());
        if (updateRegisterUI()) {
            requestPermissions(new String[]{Manifest.permission.INTERNET}, REQUEST_INTERNET_REGISTER);
        }
    }

    /**
     * Determines whether its appropriate to initiate a register request
     * and updates UI accordingly
     *
     * @return whether a register request should be initiated
     */
    private boolean updateRegisterUI() {
        if (binding.username.getText().toString().isEmpty() || binding.password.getText().toString().isEmpty()
                || binding.repassword.getText().toString().isEmpty()) {
            providerRegistrationViewModel.setRegisterError("Please enter username and password");
        } else if (!binding.password.getText().toString().equals(binding.repassword.getText().toString())) {
            providerRegistrationViewModel.setRegisterError("Passwords do not match");
        } else if (binding.email.getText().toString().isEmpty() || binding.practicename.getText().toString().isEmpty()
                || binding.phonenumber.getText().toString().isEmpty() || binding.home.getText().toString().isEmpty()) {
            providerRegistrationViewModel.setRegisterError("Please enter your business information");
        } else {
            binding.signupbtn.setEnabled(false);
            binding.signupbtn.setVisibility(View.GONE);
            binding.progressBar.setVisibility(View.VISIBLE);
            return true;
        }
        binding.progressBar.setVisibility(View.GONE);
        binding.signupbtn.setEnabled(true);
        binding.signupbtn.setVisibility(View.VISIBLE);
        return false;
    }

    /**
     * Updates this {@link RegisterResult} to reflect the passed register result
     *
     * @param registerResult {@link Optional} {@link RegisterResult} to update on
     */
    private void updateOnRegisterResult(Optional<RegisterResult> registerResult) {
        if (registerResult.isPresent()) {
            updateOnPresentRegisterResult(registerResult.get());
        } else {
            binding.registerResultText.setVisibility(View.GONE);
            binding.progressBar.setVisibility(View.GONE);
            binding.signupbtn.setEnabled(true);
            binding.signupbtn.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Updates the UI based on a {@link RegisterResult}. This is called with non-null register results, meaning
     * a registration has been attempted, and has either succeeded or failed. This is unlike updateOnRegisterResult
     * which is called with an {@link Optional} {@link RegisterResult}, and calls this method if that
     * {@link Optional} is present.
     *
     * @param registerResult {@link RegisterResult} of login attempt
     */
    private void updateOnPresentRegisterResult(RegisterResult registerResult) {
        if (registerResult.isSuccess()) {
            Intent intent = new Intent(ProviderRegistrationActivity.this, FrontPage2Activity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        } else {
            binding.registerResultText.setText(registerResult.getErrorMessage());
        }
        binding.progressBar.setVisibility(View.GONE);
        binding.signupbtn.setEnabled(true);
        binding.signupbtn.setVisibility(View.VISIBLE);
        binding.registerResultText.setVisibility(View.VISIBLE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_INTERNET_REGISTER:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    providerRegistrationViewModel.attemptRegister();
                } else {
                    Toast.makeText(this, "Internet access denied, cannot log in", Toast.LENGTH_LONG).show();
                }
                return;
        }
    }

}