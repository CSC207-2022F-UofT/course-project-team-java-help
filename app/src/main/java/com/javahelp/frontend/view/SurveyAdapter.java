package com.javahelp.frontend.activity;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.javahelp.R;
import com.javahelp.model.survey.SurveyQuestion;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class SurveyAdapter extends RecyclerView.Adapter<SurveyAdapter.ViewHolder> {
    private static final String TAG = "SurveyAdapter";

    private List<SurveyQuestion> questions;
    private LayoutInflater inflater;
    private View.OnClickListener clickListener;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final CardView cardView;
        private final TextView surveyQuestion;
        private final RadioButton surveyAnswer1;
        private final RadioButton surveyAnswer2;
        private final RadioButton surveyAnswer3;
        private final RadioButton surveyAnswer4;


        public ViewHolder(View v) {
            super(v);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "Element " + getAdapterPosition() + " clicked.");
                }
            });
            cardView = (CardView) v.findViewById(R.id.survey_question_info);
            surveyQuestion = (TextView) v.findViewById(R.id.survey_question);
            surveyAnswer1 = (RadioButton) v.findViewById(R.id.question_answer_1);
            surveyAnswer2 = (RadioButton) v.findViewById(R.id.question_answer_2);
            surveyAnswer3 = (RadioButton) v.findViewById(R.id.question_answer_3);
            surveyAnswer4 = (RadioButton) v.findViewById(R.id.question_answer_4);
        }

    }

    SurveyAdapter(Context context, List<SurveyQuestion> questions) {
        this.inflater = LayoutInflater.from(context);
        this.questions = questions;
    }

    @Override
    public SurveyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.activity_survey_question, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SurveyAdapter.ViewHolder holder, int position) {
        String question = questions.get(position).getQuestion();
        List<String> answers = (List) questions.get(position).getAnswers();
        holder.surveyQuestion.setText(question);
        holder.surveyAnswer1.setText(answers.get(0));
        holder.surveyAnswer2.setText(answers.get(1));
        holder.surveyAnswer3.setText(answers.get(2));
        holder.surveyAnswer4.setText(answers.get(3));
    }

    @Override
    public int getItemCount() {
        return questions.size();
    }

    void setClickListener(View.OnClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

}
