package com.javahelp.frontend.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.javahelp.R;
import com.javahelp.databinding.ActivityClientRegistrationBinding;


public class ClientRegistrationActivity extends AppCompatActivity {
    ClientRegistrationVm clientRegistrationVm;
    ActivityClientRegistrationBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_client_registration);
        clientRegistrationVm = new ViewModelProvider(this).get(ClientRegistrationVm.class);
        binding.setCdata(clientRegistrationVm);
        binding.setLifecycleOwner(this);

        binding.signupbtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ClientRegistrationActivity.this,
                        FrontPageActivity.class);
                startActivity(intent);
            }
        });

    }
}