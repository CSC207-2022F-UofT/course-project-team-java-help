package com.javahelp.frontend.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.javahelp.R;
import com.javahelp.model.survey.Survey;
import com.javahelp.model.survey.SurveyQuestion;
import com.javahelp.model.survey.SurveyQuestionResponse;

/**
 * Activity for viewing and answering {@link Survey}
 */
public class SurveyActivity extends AppCompatActivity implements LifecycleOwner {

    static final String SURVEY_ID_KEY = "com.javahelp.frontend.view.SurveyActivity:surveyId";

    private String surveyId;
    private SurveyViewModel surveyViewModel;
    private RecyclerView surveyView;
    private SurveyAdapter surveyAdapter;

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();

        surveyId = intent.getExtras().getString(SURVEY_ID_KEY, null);

        if (surveyId == null) {
            Toast.makeText(this, "Invalid Survey ID", Toast.LENGTH_LONG).show();
            finish();
        }

        setContentView(R.layout.activity_survey);

        surveyViewModel = new ViewModelProvider(this).get(SurveyViewModel.class);
        surveyViewModel.getSurveyQuestions().forEach(liveData -> // for every survey response
                liveData.observe(this, surveyQuestion -> // bind response to update adapter
                        surveyAdapter.notifyItemChanged( // update adapter where corresponding
                                surveyViewModel.getSurveyQuestions().indexOf(liveData))));

        surveyAdapter = new SurveyAdapter(this);

        surveyView = findViewById(R.id.survey);
        surveyView.setAdapter(surveyAdapter);

        // this should maybe be moved into a request internet permission section block depending
        // on the eventual behaviour of the gateway this makes use of
        surveyViewModel.loadSurvey(surveyId);
    }

    /**
     * Listener for radio groups to call to update the {@link Survey} to reflect
     * changes
     * @param group {@link RadioGroup} that was changed
     * @param radioButtonId id of {@link RadioButton} that was clicked
     */
    private void radioButtonUpdateSurvey(RadioGroup group, int radioButtonId) {
        RadioButton button = group.findViewById(radioButtonId);
        int question = (Integer) group.getTag();
        int response = (Integer) button.getTag();
        surveyViewModel.setResponse(question, response);
    }

    /**
     * View holder class for a question in the survey
     */
    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView surveyQuestion;
        private final RadioGroup radioGroup;

        /**
         * Creates a new {@link ViewHolder}
         *
         * @param v {@link View} to create with
         */
        public ViewHolder(View v) {
            super(v);
            surveyQuestion = v.findViewById(R.id.survey_question);
            radioGroup = v.findViewById(R.id.survey_answers);
        }

        /**
         * Populates from the specified {@link SurveyQuestionResponse}
         *
         * @param index index of the question to populate from
         * @param q     {@link SurveyQuestion} to use
         * @param r     {@link SurveyQuestionResponse} to use, or null if not yet answered
         */
        private void populateFromQuestion(int index, SurveyQuestion q, SurveyQuestionResponse r) {
            surveyQuestion.setText(q.getQuestion());
            radioGroup.removeAllViews();
            radioGroup.setTag(index);
            radioGroup.setOnCheckedChangeListener(SurveyActivity.this::radioButtonUpdateSurvey);
            for (int i = 0; i < q.getNumberOfResponses(); i++) {
                String s = q.getAnswer(i);
                RadioButton b = new RadioButton(SurveyActivity.this);
                b.setId(View.generateViewId());
                b.setText(s);
                b.getTag(i);
                radioGroup.addView(b);
                if (r != null && i == r.getResponseNumber()) {
                    b.setChecked(true);
                }
            }
        }
    }

    /**
     * Adapter for {@link SurveyQuestion}s
     */
    public class SurveyAdapter extends RecyclerView.Adapter<SurveyActivity.ViewHolder> {

        private final LayoutInflater inflater;

        /**
         * Creates a new {@link SurveyAdapter}
         * @param context {@link Context} to use
         */
        SurveyAdapter(Context context) {
            super();
            inflater = LayoutInflater.from(context);
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = inflater.inflate(R.layout.view_survey_question, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Survey survey = SurveyActivity.this.surveyViewModel.getSurvey();
            SurveyQuestion question = survey.get(position);
            SurveyQuestionResponse response = SurveyActivity.this.surveyViewModel
                    .getSurveyQuestions().get(position).getValue();
            holder.populateFromQuestion(position, question, response);
        }

        @Override
        public int getItemCount() {
            Survey survey = SurveyActivity.this.surveyViewModel.getSurvey();
            return survey != null ? survey.size() : 0;
        }
    }
}