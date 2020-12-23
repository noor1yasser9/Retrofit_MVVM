package com.nurbk.ps.photosfromtheworldprivate.view;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.nurbk.ps.photosfromtheworldprivate.R;
import com.nurbk.ps.photosfromtheworldprivate.model.entity.ApiError;
import com.nurbk.ps.photosfromtheworldprivate.model.entity.Result;
import com.nurbk.ps.photosfromtheworldprivate.viewmodel.LoginViewModel;
//import com.nurbk.ps.photosfromtheworldprivate.viewmodel.viewstates.LoginFormState;

import java.util.Objects;
public class LoginActivity  extends AppCompatActivity {


    private TextInputLayout usernameInputLayout;
    private TextInputLayout passwordInputLayout;
    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private ProgressBar progressBar;

    private LoginViewModel loginViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Objects.requireNonNull(getSupportActionBar()).setTitle(getString(R.string.title_activity_login));

        // Create an instance of LoginViewModel class
        loginViewModel = new LoginViewModel(getApplication());

        usernameInputLayout = findViewById(R.id.input_layout_username);
        passwordInputLayout = findViewById(R.id.input_layout_password);
        usernameEditText = findViewById(R.id.edit_text_username);
        passwordEditText = findViewById(R.id.edit_text_password);
        passwordEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                login(v);
                return true;
            }
            return false;
        });
            loginButton = findViewById(R.id.button_login);
            progressBar = findViewById(R.id.progress_bar);

//        loginViewModel.getFormStateDataWrapper().addObserver((o, arg) -> {
//            LoginFormState formState = (LoginFormState) arg;
//            if (formState != null && !formState.isDataValid()) {
//                showValidationError(formState.getUsernameError(), formState.getPasswordError());
//            }
//        });

            loginViewModel.getResultWrapper().addObserver((o, arg) -> {
                @SuppressWarnings("rawtypes")
                Result result = (Result) arg;
            switch (result.status) {
                case SUCCESS:
                    loginSuccess();
                    break;
                case LOADING:
                    showLoading((Boolean) result.data);
                    break;
                case ERROR:
                    showError((ApiError) result.data);
                    break;
            }
        });
    }

    @Override
    protected void onDestroy() {
        loginViewModel.cancelLoginRequest();
        super.onDestroy();
    }


    public void login(View view) {

        // Get user input
        final String username = usernameEditText.getText().toString();
        final String password = passwordEditText.getText().toString();

        // Start login using presenter instance
        loginViewModel.login(username, password);

    }


    public void showLoading(boolean isLoading) {
        progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        loginButton.setVisibility(isLoading ? View.GONE : View.VISIBLE);
    }

    public void loginSuccess() {
        startActivity(new Intent(LoginActivity.this, PhotosActivity.class));
        finish();
    }


    public void showError(ApiError apiError) {
        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), apiError.getMessage(), Snackbar.LENGTH_INDEFINITE);
        snackbar.setTextColor(Color.RED);
        if (apiError.isRecoverable()) {
            snackbar.setAction(getString(R.string.retry), this::login);
        }
        snackbar.show();
        showLoading(false);
    }

    public void showValidationError(Integer usernameError, Integer passwordError) {
        usernameInputLayout.setError(usernameError != null ? getString(usernameError) : null);
        passwordInputLayout.setError(passwordError != null ? getString(passwordError) : null);
    }


}