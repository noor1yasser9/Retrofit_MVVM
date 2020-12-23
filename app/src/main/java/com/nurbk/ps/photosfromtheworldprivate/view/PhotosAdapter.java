package com.nurbk.ps.photosfromtheworldprivate.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.nurbk.ps.photosfromtheworldprivate.R;
import com.nurbk.ps.photosfromtheworldprivate.model.entity.Photo;

import java.util.List;

/**
 * {@link PhotosAdapter} exposes a list of {@link List<Photo>}
 * to a {@link RecyclerView}.
 */
    public class PhotosAdapter extends RecyclerView.Adapter<PhotosAdapter.PhotoViewHolder> {

    private final List<Photo> photos;
    private final OnLoadingRequestListener onLoadingRequestListener;

    public PhotosAdapter(List<Photo> photos, OnLoadingRequestListener onLoadingRequestListener) {
        this.photos = photos;
        this.onLoadingRequestListener = onLoadingRequestListener;
    }
    @NonNull
    @Override
    public PhotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
        return new PhotoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoViewHolder holder, int position) {
        if (position == photos.size() - 2) {
            onLoadingRequestListener.onLoadRequest();
        }
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return photos.size();
    }
    public interface OnLoadingRequestListener {
        void onLoadRequest();
    }
    class PhotoViewHolder extends RecyclerView.ViewHolder {

        private final TextView titleTextView;
        private final ImageView thumbImageView;
        private final TextView dateTextView;

        public PhotoViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.text_view_title);
            thumbImageView = itemView.findViewById(R.id.image_view_thumb);
            dateTextView = itemView.findViewById(R.id.text_view_date);
        }

        public void bind(int position) {
            Photo photo = photos.get(position);
            titleTextView.setText(photo.getTitle());
            dateTextView.setText(photo.getDate());
            Glide.with(thumbImageView).load(photo.getThumbUrl()).into(thumbImageView);
        }

    }

}