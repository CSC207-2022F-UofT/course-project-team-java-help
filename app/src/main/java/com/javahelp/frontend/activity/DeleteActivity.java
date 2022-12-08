package com.javahelp.frontend.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.javahelp.R;
import com.javahelp.databinding.ActivityDeleteVerifyBinding;
import com.javahelp.frontend.domain.user.delete.DeleteResult;
import com.javahelp.frontend.domain.user.login.LoginResult;

import java.util.Optional;

/**
 * The {@link AppCompatActivity} for an account deletion.
 */
public class DeleteActivity extends AppCompatActivity {

    private static final int REQUEST_INTERNET_LOGIN = 1;
    private static final int REQUEST_INTERNET_DELETE = 2;

    ActivityDeleteVerifyBinding binding;
    DeleteViewModel deleteViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(DeleteActivity.this, R.layout.activity_delete_verify);
        deleteViewModel = new ViewModelProvider(this).get(DeleteViewModel.class);

        binding.setData(deleteViewModel);
        binding.setLifecycleOwner(this);

        deleteViewModel.isLoggingIn().observe(this, o -> {
            binding.progressBar.setVisibility(o ? View.VISIBLE : View.GONE);
            binding.verifyButton.setEnabled(!o);
        });

        deleteViewModel.getLoginResult().observe(this, this::updateOnLoginResult);

        binding.verifyButton.setOnClickListener(this::verifyClick);

        deleteViewModel.isDeleting().observe(this, o -> {
            binding.progressBar.setVisibility(o ? View.VISIBLE : View.GONE);
            binding.yesButton.setEnabled(!o);
            binding.noButton.setEnabled(!o);
        });

        deleteViewModel.getDeleteResult().observe(this, this::updateOnDeleteResult);

        binding.yesButton.setOnClickListener(this::yesClick);
        binding.noButton.setOnClickListener(this::noClick);

    }

    /**
     * Called when the verify button is clicked.
     * @param view: {@link View} that was clicked.
     */
    private void verifyClick(View view) {
        deleteViewModel.setPassword(binding.password.getText().toString());
        loginAttempt();
    }

    /**
     * Initiates a login request
     */
    private void loginAttempt() {
        if (binding.password.getText().toString().isEmpty()) {
            deleteViewModel.setLoginError("Please enter your password");
            return; // early return on missing password
        }
        requestPermissions(new String[]{Manifest.permission.INTERNET}, REQUEST_INTERNET_LOGIN);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_INTERNET_LOGIN:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    deleteViewModel.attemptLogin();
                } else {
                    Toast.makeText(this, "Internet access denied, cannot log in", Toast.LENGTH_LONG).show();
                }
                break;

            case REQUEST_INTERNET_DELETE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    deleteViewModel.attemptDelete();
                } else {
                    Toast.makeText(this, "Internet access denied, cannot delete", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    /**
     * Updates this {@link DeleteActivity} to reflect the passed login result
     *
     * @param result {@link Optional} {@link LoginResult} to update on
     */
    private void updateOnLoginResult(Optional<LoginResult> result) {
        if (result.isPresent()) {
            updateOnPresentLoginResult(result.get());
        } else {
            binding.deleteErrorText.setVisibility(View.GONE);
        }
    }

    /**
     * Updates the UI based on a {@link LoginResult}. This is called with non-null login results, meaning
     * a login has been attempted, and has either succeeded or failed. This is unlike updateOnLoginResult
     * which is called with an {@link Optional} {@link LoginResult}, and calls this method if that
     * {@link Optional} is present.
     *
     * @param result {@link LoginResult} of login attempt
     */
    private void updateOnPresentLoginResult(LoginResult result) {
        if (result.isSuccess()) {
            binding.deleteErrorText.setText("Verify successful");
            binding.confirm.setVisibility(View.VISIBLE);
            binding.inreversible.setVisibility(View.VISIBLE);
            binding.yesButton.setVisibility(View.VISIBLE);
            binding.noButton.setVisibility(View.VISIBLE);
        } else {
            binding.deleteErrorText.setText(result.getErrorMessage());
        }
        binding.deleteErrorText.setVisibility(View.VISIBLE);
    }

    /**
     * Called when the confirm deletion button is clicked.
     * @param view {@link View} that was clicked.
     */
    private void yesClick(View view) {
        requestPermissions(new String[]{Manifest.permission.INTERNET}, REQUEST_INTERNET_DELETE);
    }

    /**
     * Called when the cancel deletion button is clicked.
     * @param view {@link View} that was clicked.
     */
    private void noClick(View view) {
        startActivity(new Intent(DeleteActivity.this, FrontPageActivity.class));
    }

    /**
     * Updates this {@link DeleteActivity} to reflect the passed delete result.
     *
     * @param result {@link Optional} {@link DeleteResult} to update on.
     */
    private void updateOnDeleteResult(Optional<DeleteResult> result) {
        if (result.isPresent()) {
            updateOnPresentDeleteResult(result.get());
        } else {
            binding.deleteErrorText.setVisibility(View.GONE);
        }
    }

    /**
     * Updates the UI based on a {@link DeleteResult}.
     *
     * @param result {@link DeleteResult} of delete attempt
     */
    private void updateOnPresentDeleteResult(DeleteResult result) {
        if (result.isSuccess()) {
            startActivity(new Intent(DeleteActivity.this, LoginActivity.class));
        } else {
            Toast.makeText(this, result.getErrorMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
