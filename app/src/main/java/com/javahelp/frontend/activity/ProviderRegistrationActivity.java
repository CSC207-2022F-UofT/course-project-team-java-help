package com.javahelp.frontend.activity;

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
import com.javahelp.databinding.ActivityPregBinding;
import com.javahelp.frontend.domain.user.login.LoginResult;
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

public class ProviderRegistrationActivity extends AppCompatActivity {

    private static final int REQUEST_INTERNET_REGISTER = 1;
    ProviderRegistrationViewmodel providerRegistrationViewmodel;
    ActivityPregBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_preg);
        providerRegistrationViewmodel = new ViewModelProvider(this).
                get(ProviderRegistrationViewmodel.class);
        binding.setPdata(providerRegistrationViewmodel);
        binding.setLifecycleOwner(this);

        providerRegistrationViewmodel.getRegisterResult().observe(this, this::updateOnRegisterResult);

        binding.signupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProviderRegistrationActivity.this,
                        FrontPage2Activity.class);
                startActivity(intent);
            }
        });

    }

    /**
     * Called when the Register button is clicked
     *
     * @param v {@link View} that was clicked
     */
    private void loginClick(View v) {
        providerRegistrationViewmodel.setUsername(binding.username.getText().toString());
        providerRegistrationViewmodel.setPassword1(binding.password.getText().toString());
        providerRegistrationViewmodel.setPassword2(binding.repassword.getText().toString());
        providerRegistrationViewmodel.setProviderUserInfo(binding.email.getText().toString(),
                binding.firstname.getText().toString(),
                binding.lastname.getText().toString(),
                binding.home.getText().toString(),
                binding.phonenumber.getText().toString());
        registerAttempt();
    }

    private void registerAttempt() {
        if (binding.username.getText().toString().isEmpty() || binding.password.getText().toString().isEmpty()
        || binding.repassword.getText().toString().isEmpty()) {
            providerRegistrationViewmodel.setRegisterError("Please enter username and password");
            return; // early return on invalid username or password
        }else if(!binding.password.getText().toString().equals(binding.repassword.getText().toString())){
            providerRegistrationViewmodel.setRegisterError("Passwords do not match");
            return;
            }else if(binding.email.getText().toString().isEmpty() || binding.firstname.getText().toString().isEmpty()
        || binding.lastname.getText().toString().isEmpty() || binding.home.getText().toString().isEmpty()
        ||binding.phonenumber.getText().toString().isEmpty()){
            providerRegistrationViewmodel.setRegisterError("Please enter your personal information");
            return;
        }
    }


    private void updateOnRegisterResult(Optional<RegisterResult> registerResult) {
        if(registerResult.isPresent()){
            updateOnRegisterResult(Optional.of(registerResult.get()));
        } else {
            binding.registerErrorText.setVisibility(View.GONE);
        }
    }

    private void updateOnPresentRegisterResult(RegisterResult result) {
        if (result.isSuccess()) {
            binding.registerErrorText.setText("Register successful");
            storeCredentials(result.getUser(), result.getToken());
            startActivity(new Intent(ProviderRegistrationActivity.this, FrontPageActivity.class));
        } else {
            binding.registerErrorText.setText(result.getErrorMessage());
        }
        binding.registerErrorText.setVisibility(View.VISIBLE);
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
                    providerRegistrationViewmodel.attemptRegister();
                } else {
                    Toast.makeText(this, "Internet access denied, cannot log in", Toast.LENGTH_LONG);
                }
                return;
        }
    }

}