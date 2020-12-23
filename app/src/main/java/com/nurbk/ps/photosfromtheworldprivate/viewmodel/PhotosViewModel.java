package com.nurbk.ps.photosfromtheworldprivate.viewmodel;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;

import com.nurbk.ps.photosfromtheworldprivate.model.entity.ApiError;
import com.nurbk.ps.photosfromtheworldprivate.model.entity.DataWrapper;
import com.nurbk.ps.photosfromtheworldprivate.model.entity.Photo;
import com.nurbk.ps.photosfromtheworldprivate.model.entity.Result;
import com.nurbk.ps.photosfromtheworldprivate.model.network.NetworkUtils;
//import com.nurbk.ps.photosfromtheworldprivate.viewmodel.viewstates.LoadingIndicators;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PhotosViewModel {

    /**
     * Content reference
     */
    public final Context context;

    /**
     * Result wrapper
     */
    @SuppressWarnings("rawtypes")
    private final DataWrapper<Result> resultDataWrapper;

    /**
     * Data references
     */
    private final NetworkUtils networkUtils;
    private Call<List<Photo>> photoApiCall;
    private final List<Photo> photos;

    /**
     * Pagination variables
     */
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int nextPage;

    public PhotosViewModel(Application application) {
        this.context = application.getApplicationContext();
        this.resultDataWrapper = new DataWrapper<>();
        networkUtils = NetworkUtils.getInstance(application);
        photos = new ArrayList<>();
    }

    /**
     * Get list of photos
     *
     * @return complete list of photos
     */
    public List<Photo> getData() {
        return photos;
    }

    /**
     * Clear the list and reload the first page
     */
    public void refresh() {
        nextPage = 0;
        isLoading = false;
        isLastPage = false;
        photos.clear();
        resultDataWrapper.setResult(Result.success(photos));
        loadNextPage();
    }

    /**
     * Retry loading the current page
     */
    public void retry() {
        isLoading = false;
        loadNextPage();
    }

    /**
     * Load the next page of data
     */
    public void loadNextPage() {
        if (isLoading || isLastPage) {
            return;
        }
        int currentPage = nextPage;
        if (currentPage == 0) {
//            resultDataWrapper.setResult(Result.loading(new LoadingIndicators(true, false)));
        } else {
//            resultDataWrapper.setResult(Result.loading(new LoadingIndicators(false, true)));
        }
        isLoading = true;
        photoApiCall = networkUtils.getPhotosApiInterface().getPhotos(currentPage);
        photoApiCall.enqueue(new Callback<List<Photo>>() {
            @Override
            public void onResponse(@NonNull Call<List<Photo>> call, @NotNull Response<List<Photo>> response) {
                if (response.isSuccessful()) {
                    List<Photo> localPhotos = response.body();
                    if (localPhotos != null && localPhotos.size() > 0) {
                        resultDataWrapper.setResult(Result.empty(false));
                        photos.addAll(localPhotos);
                        nextPage++;
                        resultDataWrapper.setResult(Result.success(photos));
                    } else {
                        if (currentPage == 0) {
                            resultDataWrapper.setResult(Result.empty(true));
                            photos.clear();
                        }
                        isLastPage = true;
                    }
                } else {
                    ApiError apiError = networkUtils.parseHttpError(response);
                    resultDataWrapper.setResult(Result.error(apiError.getMessage(), apiError));
                }
                isLoading = false;
//                resultDataWrapper.setResult(Result.loading(new LoadingIndicators(false, false)));
            }

            @Override
            public void onFailure(@NotNull Call<List<Photo>> call, @NotNull Throwable t) {
                ApiError apiError = networkUtils.parseNetworkError(t);
                resultDataWrapper.setResult(Result.error(apiError.getMessage(), apiError));
                isLoading = false;
//                resultDataWrapper.setResult(Result.loading(new LoadingIndicators(false, false)));
            }
        });
    }


    /**
     * Cancel ongoing API call
     */
    public void cancelRequest() {
        photoApiCall.cancel();
    }


    /**
     * Get photos result wrapper
     *
     * @return result wrapper
     */
    @SuppressWarnings("rawtypes")
    public DataWrapper<Result> getResultDataWrapper() {
        return resultDataWrapper;
    }

}