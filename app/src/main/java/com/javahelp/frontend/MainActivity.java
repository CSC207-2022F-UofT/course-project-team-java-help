package com.javahelp.frontend;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.javahelp.R;

public class MainActivity extends AppCompatActivity {

    private Button buttonLogin;
    private  Button buttoncr;
    private  Button buttonpr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //find the login button by its id
        buttonLogin = (Button) findViewById(R.id.bot_login);
        //find the client register button by its id
        buttoncr = (Button) findViewById(R.id.bot_c_reg);
        //find the provider register button by its id
        buttonpr = (Button) findViewById(R.id.bot_p_reg);

        // link the button to the second acticity(front page)
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = null;
                intent = new Intent(MainActivity.this,FrontPageActivity.class);
                startActivity(intent);
            }
        });

        // link the button to the second acticity(front page)
        buttonpr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = null;
                intent = new Intent(MainActivity.this,PRegActivity.class);
                startActivity(intent);
            }
        });



    }

}
