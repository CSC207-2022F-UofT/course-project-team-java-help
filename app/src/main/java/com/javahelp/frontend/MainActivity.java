package com.javahelp.frontend;

import androidx.appcompat.app.AppCompatActivity;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.javahelp.BR;
import com.javahelp.R;
import com.javahelp.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {
    MyViewModel myViewModel;
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //setContentView(R.layout.activity_main);
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        myViewModel = new ViewModelProvider(this).get(MyViewModel.class);
        binding.setData(myViewModel);
        binding.setLifecycleOwner(this);


        // link the button to the second acticity(front page)
        binding.botLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = null;
                if (usernameChecker()) {
                    intent = new Intent(MainActivity.this,FrontPageActivity.class);
                    startActivity(intent);
                    finish();
                }
                else{
                    String inv ="username invalid";
                    Toast.makeText(getApplicationContext(), inv , Toast.LENGTH_SHORT).show();
                }
            }
        });

        // link the button to the provider Registeration page
        binding.botPReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = null;
                intent = new Intent(MainActivity.this,ProviderRegistrationActivity.class);
                startActivity(intent);
            }
        });

        // link the button to the client Registeration page
        binding.botCReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = null;
                intent = new Intent(MainActivity.this,ClientRegistrationActivity.class);
                startActivity(intent);
            }
        });





    }

    private boolean usernameChecker(){
        if(binding.et1.getText().toString().isEmpty()){
            return false;
        }
        return true;
    }



}
