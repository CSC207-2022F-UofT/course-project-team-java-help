package com.javahelp.frontend.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

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

        binding.signupbtn1.setOnClickListener(this::registerClick);
    }

    /**
     * Called when the register button is clicked
     *
     *@param v {@link View} that was clicked
     */
    private void registerClick(View v) {
        startActivity(new Intent(ClientRegistrationActivity.this, FrontPageActivity.class));
    }

}