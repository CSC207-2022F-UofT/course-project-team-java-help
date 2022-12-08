package com.javahelp.frontend.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.javahelp.R;
import com.javahelp.databinding.ActivityFgBinding;
import com.javahelp.frontend.fragments.AccountFragment;
import com.javahelp.frontend.fragments.HomeFragment;
import com.javahelp.frontend.fragments.SearchFragment;

public class FrontPageActivity extends AppCompatActivity {
    ActivityFgBinding binding;
    LoginViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFgBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new HomeFragment());

        //viewModel = new FrontPageActivityVm(this).get(LoginViewModel.class);
        //binding.setData(viewModel);
        //binding.setLifecycleOwner(this);

        binding.bottomNavigationView.setOnItemSelectedListener(item ->{
            switch(item.getItemId()){
                case R.id.home:
                    replaceFragment(new HomeFragment());
                    break;
                case R.id.account:
                    replaceFragment(new AccountFragment());
                    break;
                case R.id.search:
                    replaceFragment(new SearchFragment());
                    break;
            }
            return true;
        });
    }

    private void  replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}