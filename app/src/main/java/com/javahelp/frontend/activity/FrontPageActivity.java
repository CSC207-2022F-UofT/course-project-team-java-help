package com.javahelp.frontend.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.javahelp.R;
import com.javahelp.databinding.ActivityFgBinding;
import com.javahelp.frontend.fragments.AccountFragment;
import com.javahelp.frontend.fragments.HomeFragment;
import com.javahelp.frontend.fragments.SearchFragment;

public class FrontPageActivity extends AppCompatActivity {

    ActivityFgBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFgBinding.inflate(getLayoutInflater());
        setContentView(binding.bottomNavigationView);
        replaceFragment(new HomeFragment());

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