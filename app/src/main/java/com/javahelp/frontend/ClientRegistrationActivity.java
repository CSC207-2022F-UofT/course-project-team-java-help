package com.javahelp.frontend;

import androidx.annotation.ColorInt;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.javahelp.R;

import java.util.ArrayList;


public class ClientRegistrationActivity extends AppCompatActivity {

    EditText password, confirmpassword, firstname, lastname, user, email, home, phone;
    TextView passwordAlert, confirmAlert, emailAlert, userAlert, firstAlert, lastAlert, homeAlert, phoneAlert;
    private Button buttonsignup;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_registration);
        //find password, confirm password, name, email id, home address, phone, signup button
        user = findViewById(R.id.username);
        email = findViewById(R.id.email);
        password=findViewById(R.id.password);
        confirmpassword=findViewById(R.id.repassword);
        firstname = findViewById(R.id.firstname);
        lastname = findViewById(R.id.lastname);
        phone = findViewById(R.id.phonenumber);
        home = findViewById(R.id.home);
        buttonsignup =findViewById(R.id.signupbtn);
        //find the above variables alerts except for the button
        userAlert = findViewById(R.id.userAlert);
        emailAlert = findViewById(R.id.emailAlert);
        passwordAlert = findViewById(R.id.passAlert);
        confirmAlert = findViewById(R.id.confirmAlert);
        firstAlert = findViewById(R.id.firstnameAlert);
        lastAlert = findViewById(R.id.lastnameAlert);
        phoneAlert = findViewById(R.id.phoneAlert);
        homeAlert = findViewById(R.id.homeAlert);

        Spinner gender =findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this, R.array.genders, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        gender.setAdapter(adapter);

        buttonsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean answer1 = passwordValidate();
                boolean answer2 = fieldValidate();
                if(answer1 && answer2){
                    Intent intent = new Intent(ClientRegistrationActivity.this,FrontPageActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    @SuppressLint("ResourceAsColor")
    private boolean passwordValidate(){
        passwordAlert.setText("");
        confirmAlert.setText("");
        if (password.getText().toString().isEmpty()) {
            passwordAlert.setText("Field can't be empty");
            passwordAlert.setTextColor(Color.parseColor("#FF0000"));
            return false;
        }
        if (password.getText().toString().length()<5) {
            passwordAlert.setText("Password length needs be above 5.");
            passwordAlert.setTextColor(Color.parseColor("#FF0000"));
            return false;
        }
        if(!password.getText().toString().equals(confirmpassword.getText().toString())) {
            confirmAlert.setText("Passwords do not match! Please try again.");
            confirmAlert.setTextColor(Color.parseColor("#FF0000"));
            return false;
        }
        return true;
    }
    private boolean fieldValidate() {
        emailAlert.setText("");
        userAlert.setText("");
        phoneAlert.setText("");
        homeAlert.setText("");
        lastAlert.setText("");
        firstAlert.setText("");
        boolean found = true;

        if (user.getText().toString().isEmpty()) {
            userAlert.setText("Field can't be empty");
            userAlert.setTextColor(Color.parseColor("#FF0000"));
            found = false;
        }
        if (email.getText().toString().isEmpty()) {
            emailAlert.setText("Field can't be empty");
            emailAlert.setTextColor(Color.parseColor("#FF0000"));
            found = false;
        }
        if (phone.getText().toString().isEmpty()) {
            phoneAlert.setText("Field can't be empty");
            phoneAlert.setTextColor(Color.parseColor("#FF0000"));
            found = false;
        }
        if (home.getText().toString().isEmpty()) {
            homeAlert.setText("Field can't be empty");
            homeAlert.setTextColor(Color.parseColor("#FF0000"));
            found = false;
        }
        if (lastname.getText().toString().isEmpty()) {
            lastAlert.setText("Field can't be empty");
            lastAlert.setTextColor(Color.parseColor("#FF0000"));
            found = false;
        }
        if (firstname.getText().toString().isEmpty()) {
            firstAlert.setText("Field can't be empty");
            firstAlert.setTextColor(Color.parseColor("#FF0000"));
            found = false;
        }
        return found;
    }
}