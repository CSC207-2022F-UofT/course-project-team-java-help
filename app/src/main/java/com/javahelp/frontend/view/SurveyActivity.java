package com.javahelp.frontend.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;

import com.javahelp.R;

public class SurveyActivity extends AppCompatActivity {

    SurveyViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);
    }

    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        switch(view.getId()) {
            case R.id.question_answer_1:
                if (checked)
                    break;
            case R.id.question_answer_2:
                if (checked)
                    break;
            case R.id.question_answer_3:
                if (checked)
                    break;
            case R.id.question_answer_4:
                if (checked)
                    break;
        }
    }
}