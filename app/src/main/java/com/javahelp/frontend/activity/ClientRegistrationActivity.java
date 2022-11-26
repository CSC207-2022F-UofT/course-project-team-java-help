package com.javahelp.frontend.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;

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

        //setting up spinner to create dropdown item for gender
        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this, R.array.genders, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        binding.gender1.setAdapter(adapter);

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