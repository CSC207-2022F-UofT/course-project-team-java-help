package com.javahelp.frontend.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.javahelp.R;
import com.javahelp.databinding.FragmentAccountBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AccountFragment#newInstance} method to
 * create an instance of this fragment.
 */
public class AccountFragment extends Fragment {

    FragmentAccountBinding binding;
    AccountFragmentViewModel viewModel;

    /**
     * Creates a new {@link AccountFragment}
     */
    public AccountFragment() {
        // Required empty public constructor
    }

    /**
     * Creates a new {@link AccountFragment}
     *
     * @return a new instance of {@link AccountFragment}
     */
    public static AccountFragment newInstance() {
        return new AccountFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(AccountFragmentViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_account, container, false);

        binding = DataBindingUtil.bind(v);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        binding.setAccountFragmentViewModel(viewModel);

        binding.deleteButton.setOnClickListener(view -> startActivity(new Intent(AccountFragment.this.getContext(), DeleteActivity.class)));

        viewModel.getRegistering().observe(getViewLifecycleOwner(),
                registering -> binding.progressBar.setVisibility(registering ? View.VISIBLE : View.GONE));

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel.getInfo();
    }
}