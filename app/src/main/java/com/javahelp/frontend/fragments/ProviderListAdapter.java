package com.javahelp.frontend.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.javahelp.R;
import com.javahelp.model.survey.SurveyResponse;
import com.javahelp.model.user.ProviderUserInfo;
import com.javahelp.model.user.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ProviderListAdapter extends ArrayAdapter<Map<User, SurveyResponse>> {
    private final Integer imgID = R.drawable.user48;

    public ProviderListAdapter(Context context, List<Map<User, SurveyResponse>> usersAndResponses) {
        super(context, R.layout.provider_list, usersAndResponses);
    }

    @SuppressLint("SetTextI18n")
    public View getView(int position, View view, ViewGroup parent) {
        // Check if an existing view is being reused, otherwise inflate the view
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.provider_list, parent, false);
        }

        TextView titleText = view.findViewById(R.id.title);
        ImageView imageView = view.findViewById(R.id.icon);
        TextView subtitleCert = view.findViewById(R.id.certified);
        TextView subtitleNumber = view.findViewById(R.id.number);
        TextView subtitleAddr = view.findViewById(R.id.address);
        TextView subtitleAttr0 = view.findViewById(R.id.attr0);
        TextView subtitleAttr1 = view.findViewById(R.id.attr1);
        TextView subtitleAttr2 = view.findViewById(R.id.attr2);
        TextView subtitleAttr3 = view.findViewById(R.id.attr3);


        Map<User, SurveyResponse> userAndResponse = getItem(position);
        User user = (User) userAndResponse.keySet().toArray()[0];
        SurveyResponse sr = userAndResponse.get(user);

        ProviderUserInfo info = (ProviderUserInfo) user.getUserInfo();

        titleText.setText(info.getPracticeName());
        imageView.setImageResource(imgID);
        if (info.isCertified()) {
            subtitleCert.setText("Certified");
        }
        else {
            subtitleCert.setText("Not Certified");
        }
        subtitleNumber.setText(info.getPhoneNumber());
        subtitleAddr.setText(info.getAddress());

        List<String> attributes = new ArrayList<>();
        if (sr.getAttributes().contains("attr0")) { attributes.add("attr0"); }
        if (sr.getAttributes().contains("attr1")) { attributes.add("attr1"); }
        if (sr.getAttributes().contains("attr2")) { attributes.add("attr2"); }
        if (sr.getAttributes().contains("attr3")) { attributes.add("attr3"); }
        for (int i = 0; i < attributes.size(); i++) {
            if (i == 0) { subtitleAttr0.setText(attributes.get(i)); }
            if (i == 1) { subtitleAttr1.setText(attributes.get(i)); }
            if (i == 2) { subtitleAttr2.setText(attributes.get(i)); }
            if (i == 3) { subtitleAttr3.setText(attributes.get(i)); }
        }


        return view;

    }
}