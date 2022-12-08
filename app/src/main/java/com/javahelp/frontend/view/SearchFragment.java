package com.javahelp.frontend.view;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Switch;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.chip.Chip;
import com.javahelp.R;
import com.javahelp.frontend.domain.user.login.LoginResult;
import com.javahelp.frontend.domain.user.search.SearchResult;
import com.javahelp.model.survey.Survey;
import com.javahelp.model.survey.SurveyQuestion;
import com.javahelp.model.survey.SurveyQuestionResponse;
import com.javahelp.model.survey.SurveyResponse;
import com.javahelp.model.user.ClientUserInfo;
import com.javahelp.model.user.ProviderUserInfo;
import com.javahelp.model.user.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment {
    SearchFragmentVM viewModel;
    View contentView;
    ProgressBar progressBar;
    Switch smartSwitch;
    Chip chip0, chip1, chip2, chip3;
    ListView listView;

    List<Map<User, SurveyResponse>> usersAndResponses;

    public SearchFragment() {}

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment SearchFragment.
     */
    public static SearchFragment newInstance() {
        SearchFragment fragment = new SearchFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(SearchFragmentVM.class);
        viewModel.setUserID("test");

        usersAndResponses = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        contentView = inflater.inflate(R.layout.fragment_search, container, false);

        progressBar = contentView.findViewById(R.id.progressBar2);

        smartSwitch = contentView.findViewById(R.id.switch1);
        smartSwitch.setOnClickListener(this::rankClick);

        chip0 = contentView.findViewById(R.id.chip0);
        chip0.setOnClickListener(this::chip0Click);
        chip1 = contentView.findViewById(R.id.chip1);
        chip1.setOnClickListener(this::chip1Click);
        chip2 = contentView.findViewById(R.id.chip2);
        chip2.setOnClickListener(this::chip2Click);
        chip3 = contentView.findViewById(R.id.chip3);
        chip3.setOnClickListener(this::chip3Click);

        viewModel.getIsFiltering().observe(getViewLifecycleOwner(), this::updateOnNewSearch);
        viewModel.getIsSearching().observe(getViewLifecycleOwner(), this::updateProviderList);
        viewModel.getIsSearching().observe(getViewLifecycleOwner(), o -> {
            progressBar.setVisibility(o ? View.VISIBLE : View.GONE);
            chip0.setEnabled(!o);
            chip1.setEnabled(!o);
            chip2.setEnabled(!o);
            chip3.setEnabled(!o);
            smartSwitch.setEnabled(!o);
        });

        listView = contentView.findViewById(R.id.listview);

        return contentView;
    }

    /**
     * Updates this {@link SearchFragment} with new list of providers
     *
     * @param isFiltering {@link boolean} whether new filters are applied
     */
    private void updateOnNewSearch(boolean isFiltering) {
        if (isFiltering)
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.INTERNET) ==
                        PackageManager.PERMISSION_GRANTED) {
                updateOnPresentSearch();
        }
    }

    /**
     * Updates the UI based on a {@link SearchResult}. This is called with non-null login results, meaning
     * a login has been attempted, and has either succeeded or failed. This is unlike updateOnLoginResult
     * which is called with an {@link Optional} {@link LoginResult}, and calls this method if that
     * {@link Optional} is present.
     *
     */
    private void updateOnPresentSearch() {
        viewModel.search();
    }

    private void updateProviderList(boolean isSearching) {
        if (!isSearching && viewModel.getUsers() != null && viewModel.getSurveyResponses() != null) {
            usersAndResponses = new ArrayList<>();
            List<User> users = viewModel.getUsers();
            List<SurveyResponse> surveyResponses = viewModel.getSurveyResponses();
            if (users.size() != 0 && surveyResponses.size() != 0) {
                for (int i = 0; i < users.size(); i++) {
                    Map<User, SurveyResponse> uAndSr = new HashMap<>();
                    uAndSr.put(users.get(i), surveyResponses.get(i));
                    usersAndResponses.add(uAndSr);
                }
            }
        }

        ProviderListAdapter listAdapter = new ProviderListAdapter(requireActivity().getApplicationContext(),
                usersAndResponses);
        listView.setAdapter(listAdapter);
    }

    private void rankClick(View v) { viewModel.setIsRanking(); }

    private void chip0Click(View v) {
        viewModel.setFilter("attr0");
    }
    private void chip1Click(View v) {
        viewModel.setFilter("attr1");
    }
    private void chip2Click(View v) {
        viewModel.setFilter("attr2");
    }
    private void chip3Click(View v) {
        viewModel.setFilter("attr3");
    }

    private User generateRandomClient() {
        Random random = new Random();
        int userID = random.nextInt(100);
        int phoneNumber = random.nextInt(9999);
        ClientUserInfo clientInfo = new ClientUserInfo(
                String.format("test.client.%s@mail.com", userID),
                String.format("%s Client road", userID),
                String.format("+1-647-674-%04d", phoneNumber),
                String.format("Number %s", userID),
                "Client");

        return new User(String.format("test_client_%s", userID),
                clientInfo,
                String.format("test_client_%s", userID));
    }

    private User generateRandomProvider() {
        Random random = new Random();
        int userID = random.nextInt(100);
        int phoneNumber = random.nextInt(9999);
        ProviderUserInfo providerInfo = new ProviderUserInfo(
                String.format("test.client.%s@mail.com", userID),
                String.format("%s Client road", userID),
                String.format("+1-647-674-%04d", phoneNumber),
                String.format("Number %s Provider", userID)
        );

        return new User(String.format("test_provider_%s", userID),
                providerInfo,
                String.format("test_provider_%s", userID));
    }

    private SurveyResponse generateRandomSurveyResponse(Survey survey) {
        Random random = new Random();
        Map<SurveyQuestion, SurveyQuestionResponse> map = new HashMap<>();
        for (int i = 0; i < survey.size(); i++) {
            int randAnswer = random.nextInt(4);
            SurveyQuestionResponse response = new SurveyQuestionResponse(survey.get(i), randAnswer);
            map.put(survey.get(i), response);
        }

        int randResponseID = random.nextInt(999);
        return new SurveyResponse(String.format("response_%s", randResponseID), survey, map);
    }

    private Survey setupSurvey() {
        List<String> responses = new ArrayList<>();
        responses.add("This is the first response");
        responses.add("This is the second response");
        responses.add("This is the third response");
        responses.add("This is the fourth response");

        List<SurveyQuestion> questions = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            SurveyQuestion question = new SurveyQuestion(String.format("Question %s", i), responses);
            question.setAnswerAttribute(0, "attr0");
            question.setAnswerAttribute(1, "attr1");
            question.setAnswerAttribute(2, "attr2");
            question.setAnswerAttribute(3, "attr3");

            questions.add(question);
        }
        return new Survey("survey_v1", "Test Survey", questions);
    }
}