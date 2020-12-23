package com.nurbk.ps.photosfromtheworldprivate.view;


import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.snackbar.Snackbar;
import com.nurbk.ps.photosfromtheworldprivate.R;
import com.nurbk.ps.photosfromtheworldprivate.model.entity.ApiError;
import com.nurbk.ps.photosfromtheworldprivate.model.entity.Result;
import com.nurbk.ps.photosfromtheworldprivate.viewmodel.PhotosViewModel;
//import com.nurbk.ps.photosfromtheworldprivate.viewmodel.viewstates.LoadingIndicators;

import java.util.Objects;


public class PhotosActivity extends AppCompatActivity {


    private ProgressBar progressBar;
    private ProgressBar bottomProgressBar;
    private TextView emptyTextView;
    private PhotosAdapter photosAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Snackbar snackbar;


    private PhotosViewModel photosViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos);

        // Create an instance of PhotosViewModel class
        photosViewModel = new PhotosViewModel(getApplication());

        progressBar = findViewById(R.id.progress_bar);
        bottomProgressBar = findViewById(R.id.bottom_progress_bar);
        emptyTextView = findViewById(R.id.text_view_empty);

        RecyclerView photosRecyclerView = findViewById(R.id.recycler_view_photos);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        photosRecyclerView.setLayoutManager(linearLayoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, RecyclerView.VERTICAL);
        dividerItemDecoration.setDrawable(Objects.requireNonNull(ContextCompat.getDrawable(this, R.drawable.shape_space_devider)));
        photosRecyclerView.addItemDecoration(dividerItemDecoration);

        photosAdapter = new PhotosAdapter(photosViewModel.getData(), () -> photosViewModel.loadNextPage());
        photosRecyclerView.setAdapter(photosAdapter);

        // Init pull to refresh components to reload the data once pull event fired
        swipeRefreshLayout = findViewById(R.id.swipe_to_refresh);
        swipeRefreshLayout.setOnRefreshListener(() -> photosViewModel.refresh());

        // Load the first page of data
        photosViewModel.loadNextPage();

        photosViewModel.getResultDataWrapper().addObserver((o, arg) -> {
            @SuppressWarnings("rawtypes")
            Result result = (Result) arg;
            switch (result.status) {
                case SUCCESS:
                    updateData();
                    showLoading(false,false);
                    hideError();
                    break;
                case LOADING:
//                    LoadingIndicators loadingIndicators = (LoadingIndicators) result.data;
                    showLoading(true,true);
                    break;
                case ERROR:
                    showError((ApiError) result.data);
                    break;
                case EMPTY:
                    showEmptyMessage((Boolean) result.data);
                    break;
            }
        });

    }

    @Override
    protected void onDestroy() {
        photosViewModel.cancelRequest();
        super.onDestroy();
    }


    public void updateData() {
        photosAdapter.notifyDataSetChanged();
    }

    public void showLoading(boolean isMainLoading, boolean isBottomLoading) {
        if (!isMainLoading && !isBottomLoading) {
            swipeRefreshLayout.setRefreshing(false);
        }
        progressBar.setVisibility(isMainLoading ? View.VISIBLE : View.GONE);
        bottomProgressBar.setVisibility(isBottomLoading ? View.VISIBLE : View.GONE);
    }

    public void showError(ApiError apiError) {
        snackbar = Snackbar.make(findViewById(android.R.id.content), apiError.getMessage(), Snackbar.LENGTH_INDEFINITE);
        snackbar.setTextColor(Color.RED);
        if (apiError.isRecoverable()) {
            snackbar.setAction(getString(R.string.retry), v -> photosViewModel.retry());
        }
        snackbar.show();
        showLoading(false, false);
    }


    private void hideError() {
        if (snackbar != null) {
            snackbar.dismiss();
        }
    }

    public void showEmptyMessage(boolean isEmpty) {
        emptyTextView.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
    }

}