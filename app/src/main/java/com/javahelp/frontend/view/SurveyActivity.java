package com.javahelp.frontend.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.javahelp.R;
import com.javahelp.model.survey.Survey;
import com.javahelp.model.survey.SurveyQuestion;
import com.javahelp.model.survey.SurveyResponse;

import java.util.ArrayList;
import java.util.List;

public class SurveyActivity extends AppCompatActivity implements LifecycleOwner {
    SurveyActivity context;
    SurveyViewModel surveyViewModel;
    RecyclerView surveyView;
    SurveyAdapter surveyAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);
        context = this;
        surveyView = findViewById(R.id.survey);
        surveyAdapter = new SurveyAdapter(context);
        surveyView.setAdapter(surveyAdapter);
        surveyViewModel = new ViewModelProvider(this).get(SurveyViewModel.class);
        surveyViewModel.getSurveyQuestionsLiveData().observe(context, surveyQuestionsUpdateObserver);
    }

    Observer<ArrayList<SurveyQuestion>> surveyQuestionsUpdateObserver = new Observer<ArrayList<SurveyQuestion>>() {
        @Override
        public void onChanged(ArrayList<SurveyQuestion> surveyQuestions) {
            surveyAdapter.updateSurveyQuestions(surveyQuestions);
        }
    };

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final CardView cardView;
        private final TextView surveyQuestion;
        private final RadioGroup radioGroup;

        public ViewHolder(View v) {
            super(v);
            cardView = (CardView) v.findViewById(R.id.survey_question_info);
            surveyQuestion = (TextView) v.findViewById(R.id.survey_question);
            radioGroup = (RadioGroup) v.findViewById(R.id.survey_answers);

         }

    }

    public static class SurveyAdapter extends RecyclerView.Adapter<SurveyActivity.ViewHolder> {
        private static final String TAG = "SurveyAdapter";
        private List<SurveyQuestion> questions;
        private List<SurveyResponse> responses;
        private LayoutInflater inflater;

        SurveyAdapter(Context context) {
            this.inflater = LayoutInflater.from(context);
            this.questions = new ArrayList<SurveyQuestion>();
            this.responses = new ArrayList<SurveyResponse>();
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = inflater.inflate(R.layout.view_survey_question, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            SurveyQuestion question = questions.get(position);
            holder.surveyQuestion.setText(question.getQuestion());
            holder.radioGroup.removeAllViews();

            for(int i = 0; i < question.getNumberOfResponses(); i++){
                RadioButton radioButton= new RadioButton(holder.cardView.getContext());
                radioButton.setText(question.getAnswer(i));
                holder.radioGroup.addView(radioButton);
            }
        }

        @Override
        public int getItemCount() {
            return questions.size();
        }

        public void updateSurveyQuestions(List<SurveyQuestion> surveyQuestions) {
            this.questions.clear();
            this.questions = surveyQuestions;
            notifyDataSetChanged();
        }

    }

}