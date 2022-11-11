package com.javahelp.frontend;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.javahelp.R;

public class MainActivity extends AppCompatActivity {

    //control statement of login botton
    private Button Buttomcg;
    private EditText inputUsername;
    private EditText inputPassword;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //find the control
        Buttomcg = findViewById(R.id.bot_c_reg);
        inputUsername = findViewById(R.id.et_1);
        inputPassword = findViewById(R.id.et_2);

        //implement the jump
        Buttomcg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = null;
                intent = new Intent(MainActivity.this,FunctionActivity.class);
                startActivity(intent);
            }
        });



    }
    private void onClick(View v){
        String username = inputUsername.getText().toString();
        String password = inputPassword.getText().toString();
        Intent intent = null;

        if(username.equals("123") && password.equals("321")){
            intent = new Intent(MainActivity.this, FunctionActivity.class);
            startActivity(intent);
        }else{

        }

    }

}
