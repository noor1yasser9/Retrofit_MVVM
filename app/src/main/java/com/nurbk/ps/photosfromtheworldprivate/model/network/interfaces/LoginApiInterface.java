package com.nurbk.ps.photosfromtheworldprivate.model.network.interfaces;

import com.nurbk.ps.photosfromtheworldprivate.model.entity.LoggedInUser;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Retrofit API interface used to access login APIs
 */
public interface LoginApiInterface {

    String LOGIN_PATH = "login.php";
    String PARAM_USERNAME = "username";
    String PARAM_PASSWORD = "password";

    @FormUrlEncoded
    @POST(LOGIN_PATH)
    Call<LoggedInUser> login(@Field(PARAM_USERNAME) String username, @Field(PARAM_PASSWORD) String password);

}