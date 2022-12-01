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
import com.javahelp.model.user.ProviderUserInfo;
import com.javahelp.model.user.User;

import java.util.ArrayList;
import java.util.List;

public class ProviderListAdapter extends ArrayAdapter<User> {
    private final Integer imgID = R.drawable.user48;

    public ProviderListAdapter(Context context, List<User> users) {
        super(context, R.layout.provider_list, users);
    }

    @SuppressLint("SetTextI18n")
    public View getView(int position, View view, ViewGroup parent) {
        // Check if an existing view is being reused, otherwise inflate the view
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.provider_list, parent, false);
        }

        TextView titleText = (TextView) view.findViewById(R.id.title);
        ImageView imageView = (ImageView) view.findViewById(R.id.icon);
        TextView subtitleCert = (TextView) view.findViewById(R.id.certified);
        TextView subtitleNumber = (TextView) view.findViewById(R.id.number);
        TextView subtitleAddr = (TextView) view.findViewById(R.id.address);

        User user = getItem(position);
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

        return view;

    };
}