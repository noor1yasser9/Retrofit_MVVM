package com.nurbk.ps.photosfromtheworldprivate.viewmodel;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;
import androidx.annotation.Nullable;
import com.nurbk.ps.photosfromtheworldprivate.R;
import com.nurbk.ps.photosfromtheworldprivate.model.entity.ApiError;
import com.nurbk.ps.photosfromtheworldprivate.model.entity.DataWrapper;
import com.nurbk.ps.photosfromtheworldprivate.model.entity.LoggedInUser;
import com.nurbk.ps.photosfromtheworldprivate.model.entity.Result;
import com.nurbk.ps.photosfromtheworldprivate.model.network.NetworkUtils;
import com.nurbk.ps.photosfromtheworldprivate.model.preferences.SharedPreferenceHelper;
//import com.nurbk.ps.photosfromtheworldprivate.viewmodel.viewstates.LoginFormState;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginViewModel {

    public final Context context;


    private final DataWrapper<Result> resultWrapper;


//    private final DataWrapper<LoginFormState> formStateDataWrapper;


    private final NetworkUtils networkUtils;
    private Call<LoggedInUser> loginApiCall;

    public LoginViewModel(Application application) {
        this.context = application.getApplicationContext();
        this.resultWrapper = new DataWrapper<>();
//        this.formStateDataWrapper = new DataWrapper<>();
        networkUtils = NetworkUtils.getInstance(context);
    }


    public void login(String username, String password) {

        if (!isDataValid(username, password)) {
            return;
        }

        resultWrapper.setResult(Result.loading(true));

        loginApiCall = networkUtils.getLoginApiInterface().login(username, password);
        loginApiCall.enqueue(new Callback<LoggedInUser>() {
            @Override
            public void onResponse(@Nullable Call<LoggedInUser> call, @Nullable Response<LoggedInUser> response) {
                if (response != null && response.body() != null && response.isSuccessful()) {
                    SharedPreferenceHelper.setToken(context, response.body().getToken());
                    resultWrapper.setResult(Result.success(response));
                } else {
                    ApiError apiError = networkUtils.parseHttpError(response);
                    resultWrapper.setResult(Result.error(apiError.getMessage(), apiError));
                }
            }

            @Override
            public void onFailure(@Nullable Call<LoggedInUser> call, @Nullable Throwable t) {
                ApiError apiError = networkUtils.parseNetworkError(t);
                resultWrapper.setResult(Result.error(apiError.getMessage(), apiError));

            }
        });


    }

    public void cancelLoginRequest() {
        loginApiCall.cancel();
    }


    private boolean isDataValid(String username, String password) {

        Integer usernameError = null;
        Integer passwordError = null;

        if (TextUtils.isEmpty(username)) {
            usernameError = R.string.error_empty_username;
        }

        if (TextUtils.isEmpty(password)) {
            passwordError = R.string.error_empty_password;
        }

//        formStateDataWrapper.setResult(new LoginFormState(usernameError, passwordError));

        if (usernameError == null && passwordError == null) {
//            formStateDataWrapper.setResult(new LoginFormState(true));
            return true;
        }

        return false;

    }


    @SuppressWarnings("rawtypes")
    public DataWrapper<Result> getResultWrapper() {
        return resultWrapper;
    }


//    public DataWrapper<LoginFormState> getFormStateDataWrapper() {
//        return formStateDataWrapper;
//    }

}
