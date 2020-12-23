package com.nurbk.ps.photosfromtheworldprivate.model.network;

import android.content.Context;
import android.text.TextUtils;

import com.nurbk.ps.photosfromtheworldprivate.R;
import com.nurbk.ps.photosfromtheworldprivate.model.entity.ApiError;
import com.nurbk.ps.photosfromtheworldprivate.model.entity.Photo;
import com.nurbk.ps.photosfromtheworldprivate.model.network.interfaces.LoginApiInterface;
import com.nurbk.ps.photosfromtheworldprivate.model.network.interfaces.PhotosApiInterface;
import com.nurbk.ps.photosfromtheworldprivate.model.preferences.SharedPreferenceHelper;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttp;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkUtils {

    private static final Object LOCK = new Object();
    private static NetworkUtils instance;
    private final String BASE_URL = "https://omaralbelbaisy.com/api/";
    private final String PARAM_TOKEN = "Token";
    private Retrofit retrofit = null;
    private final LoginApiInterface loginApiInterface;
    private final PhotosApiInterface photosApiInterface;
    private final Context context;


    private NetworkUtils(final Context context) {
        this.context = context.getApplicationContext();
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.level(HttpLoggingInterceptor.Level.BASIC);

        Interceptor interceptor = chain -> {
            Request.Builder builder = chain.request().newBuilder();
            String token = SharedPreferenceHelper.getToken(context);
            if (!TextUtils.isEmpty(token)) {
                builder.addHeader(PARAM_TOKEN, token);
            }
            return chain.proceed(builder.build());
        };

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor)
                .addInterceptor(interceptor)
                .readTimeout(30, TimeUnit.SECONDS)
                .connectTimeout(30, TimeUnit.SECONDS)
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        loginApiInterface = retrofit.create(LoginApiInterface.class);
        photosApiInterface = retrofit.create(PhotosApiInterface.class);
    }

    public static synchronized NetworkUtils getInstance(Context context) {
        if (instance == null) {
            synchronized (LOCK) {
                if (instance == null) instance = new NetworkUtils(context);
            }
        }
        return instance;
    }

    public LoginApiInterface getLoginApiInterface() {
        return loginApiInterface;
    }

    public PhotosApiInterface getPhotosApiInterface() {
        return photosApiInterface;
    }


    public ApiError parseHttpError(Response<?> response) {
        Converter<ResponseBody, ApiError> converter = retrofit.responseBodyConverter(ApiError.class, new Annotation[0]);
        ApiError error;
        try {
            assert response.errorBody() != null;
            error = converter.convert(response.errorBody());
            if (error != null) {
                error.setCode(response.code());
            }
        } catch (NullPointerException | IOException e) {
            return new ApiError(context.getString(R.string.error_connection_failed));
        }
        return error;
    }

    public ApiError parseNetworkError(Throwable throwable) {
        if (throwable instanceof IOException) {
            return new ApiError(context.getString(R.string.error_connection_failed));
        } else {
            return new ApiError(context.getString(R.string.error_unexpected));
        }
    }

}
