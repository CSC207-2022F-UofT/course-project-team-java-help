package com.javahelp.frontend.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;

import com.javahelp.R;
import com.javahelp.databinding.ActivityPregBinding;

public class ProviderRegistrationActivity extends AppCompatActivity {
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

        //setting up spinner to create dropdown item for gender
        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this, R.array.genders, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        binding.gender.setAdapter(adapter);
        binding.signupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProviderRegistrationActivity.this,
                        FrontPage2Activity.class);
                startActivity(intent);
            }
        });

    }
}