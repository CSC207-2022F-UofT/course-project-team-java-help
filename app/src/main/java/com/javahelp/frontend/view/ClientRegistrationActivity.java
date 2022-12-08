package com.javahelp.frontend.view;



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
import java.util.Optional;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;


public class ClientRegistrationActivity extends AppCompatActivity {

    /**
     * Permission request code to get internet access for register
     */
    private static final int REQUEST_INTERNET_REGISTER = 1;

    ClientRegistrationVm clientRegistrationVm;
    ActivityClientRegistrationBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_client_registration);
        clientRegistrationVm = new ViewModelProvider(this).get(ClientRegistrationVm.class);
        binding.setCdata(clientRegistrationVm);
        binding.setLifecycleOwner(this);

        clientRegistrationVm.getRegisterResult().observe(this, this::updateOnRegisterResult);
        binding.signupbtn1.setOnClickListener(this::registerClick);
    }



    /**
     * Called when the register button is clicked
     *
     *@param v {@link View} that was clicked
     */
    private void registerClick(View v) {
        clientRegistrationVm.setUsername(binding.username1.getText().toString());
        clientRegistrationVm.setPassword1(binding.password1.getText().toString());
        clientRegistrationVm.setPassword2(binding.repassword1.getText().toString());
        clientRegistrationVm.setClientUserInfo(binding.email1.getText().toString(),
                binding.home1.getText().toString(),
                binding.phonenumber1.getText().toString(),
                binding.firstname1.getText().toString(),
                binding.lastname1.getText().toString());
        registerAttempt();

    }
    /**
     * Initiates a register request
     */
    private void registerAttempt() {
        if (binding.username1.getText().toString().isEmpty() || binding.password1.getText().toString().isEmpty()
                || binding.repassword1.getText().toString().isEmpty()) {
            clientRegistrationVm.setRegisterError("Please enter username and password");
            return;
        } else if (!binding.password1.getText().toString().equals(binding.repassword1.getText().toString())) {
            clientRegistrationVm.setRegisterError("Passwords do not match");
            return;
        } else if (binding.email1.getText().toString().isEmpty() || binding.lastname1.getText().toString().isEmpty()
                || binding.firstname1.getText().toString().isEmpty() || binding.home1.getText().toString().isEmpty()
                || binding.phonenumber1.getText().toString().isEmpty()) {
            clientRegistrationVm.setRegisterError("Please enter your personal information");
            return;
        }
        clientRegistrationVm.setRegisterError("Registering");
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
            binding.registerResultText1.setText("Register successful");
            storeCredentials(registerResult.getUser(), registerResult.getToken());
            Toast.makeText(this, "Register successful", Toast.LENGTH_SHORT);
            startActivity(new Intent(ClientRegistrationActivity.this, FrontPageActivity.class));

        } else {
            binding.registerResultText1.setText(registerResult.getErrorMessage());
        }
        binding.registerResultText1.setVisibility(View.VISIBLE);
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_INTERNET_REGISTER:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    clientRegistrationVm.attemptRegister();
                } else {
                    Toast.makeText(this, "Internet access denied, cannot log in", Toast.LENGTH_LONG);
                }
                return;
        }
    }

}