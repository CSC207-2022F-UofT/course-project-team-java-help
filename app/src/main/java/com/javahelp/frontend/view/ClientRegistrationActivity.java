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
import com.javahelp.databinding.ActivityClientRegistrationBinding;
import com.javahelp.frontend.domain.user.register.RegisterResult;
import com.javahelp.frontend.util.auth.SharedPreferencesAuthInformationProvider;
import com.javahelp.model.token.Token;
import com.javahelp.model.user.User;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Permission;
import java.util.Optional;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 * Activity for registering clients
 */
public class ClientRegistrationActivity extends AppCompatActivity {

    /**
     * Permission request code to get internet access for register
     */
    private static final int REQUEST_INTERNET_REGISTER = 1;

    ClientRegistrationViewModel clientRegistrationViewModel;
    ActivityClientRegistrationBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_client_registration);
        clientRegistrationViewModel = new ViewModelProvider(this).get(ClientRegistrationViewModel.class);
        binding.setClientViewModel(clientRegistrationViewModel);
        binding.setLifecycleOwner(this);

        clientRegistrationViewModel.getRegisterResult().observe(this, this::updateOnRegisterResult);
        binding.signupbtn1.setOnClickListener(this::registerClick);
    }

    /**
     * Called when the register button is clicked
     *
     *@param v {@link View} that was clicked
     */
    private void registerClick(View v) {
        clientRegistrationViewModel.setUsername(binding.username1.getText().toString());
        clientRegistrationViewModel.setPassword(binding.password1.getText().toString());
        clientRegistrationViewModel.setEmail(binding.email1.getText().toString());
        clientRegistrationViewModel.setAddress(binding.home1.getText().toString());
        clientRegistrationViewModel.setPhone(binding.phonenumber1.getText().toString());
        clientRegistrationViewModel.setFirstName(binding.firstname1.getText().toString());
        clientRegistrationViewModel.setLastName(binding.lastname1.getText().toString());
        if (updateRegisterUI()) {
            requestPermissions(new String[]{Manifest.permission.INTERNET}, REQUEST_INTERNET_REGISTER);
        }
    }
    /**
     * Determines whether its appropriate to initiate a register request
     * and updates UI accordingly
     * @return whether a register request should be initiated
     */
    private boolean updateRegisterUI() {
        if (binding.username1.getText().toString().isEmpty() || binding.password1.getText().toString().isEmpty()
                || binding.repassword1.getText().toString().isEmpty()) {
            clientRegistrationViewModel.setRegisterError("Please enter username and password");
        } else if (!binding.password1.getText().toString().equals(binding.repassword1.getText().toString())) {
            clientRegistrationViewModel.setRegisterError("Passwords do not match");
        } else if (binding.email1.getText().toString().isEmpty() || binding.lastname1.getText().toString().isEmpty()
                || binding.firstname1.getText().toString().isEmpty() || binding.home1.getText().toString().isEmpty()
                || binding.phonenumber1.getText().toString().isEmpty()) {
            clientRegistrationViewModel.setRegisterError("Please enter your personal information");
        } else {
            binding.signupbtn1.setEnabled(false);
            binding.signupbtn1.setVisibility(View.GONE);
            binding.progressBar.setVisibility(View.VISIBLE);
            return true;
        }
        binding.progressBar.setVisibility(View.GONE);
        binding.signupbtn1.setEnabled(true);
        binding.signupbtn1.setVisibility(View.VISIBLE);
        return false;
    }

    /**
     * Updates this {@link RegisterResult} to reflect the passed register result
     *
     * @param registerResult {@link Optional} {@link RegisterResult} to update on
     */
    private void updateOnRegisterResult(Optional<RegisterResult> registerResult) {
        if(registerResult.isPresent()){
            updateOnPresentRegisterResult(registerResult.get());
        }else {
            binding.registerResultText1.setVisibility(View.GONE);
            binding.progressBar.setVisibility(View.GONE);
            binding.signupbtn1.setEnabled(true);
            binding.signupbtn1.setVisibility(View.VISIBLE);
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
            Intent intent = new Intent(ClientRegistrationActivity.this, FrontPageActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        } else {
            binding.registerResultText1.setText(registerResult.getErrorMessage());
        }
        binding.progressBar.setVisibility(View.GONE);
        binding.signupbtn1.setEnabled(true);
        binding.signupbtn1.setVisibility(View.VISIBLE);
        binding.registerResultText1.setVisibility(View.VISIBLE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_INTERNET_REGISTER:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    clientRegistrationViewModel.attemptRegister();
                } else {
                    Toast.makeText(this, "Internet access denied, cannot log in", Toast.LENGTH_LONG).show();
                }
                return;
        }
    }

}