package com.nurbk.ps.photosfromtheworldprivate.model.network.interfaces;

import com.nurbk.ps.photosfromtheworldprivate.model.entity.Photo;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PhotosApiInterface {
    String DATA_PATH = "data.php";
    String PARAM_PAGE = "page";

    @GET(DATA_PATH)
    Call<List<Photo>> getPhotos(@Query(PARAM_PAGE) int page);
}
