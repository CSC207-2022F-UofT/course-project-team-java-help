package com.javahelp.frontend.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

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