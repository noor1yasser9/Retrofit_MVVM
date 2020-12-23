package com.nurbk.ps.photosfromtheworldprivate.model.entity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class Result<T> {

    public final Status status;
    public final String message;
    public final T data;

    private Result(Status status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public static <T> Result<T> success(@NonNull T data) {
        return new Result<>(Status.SUCCESS, null, data);
    }

    public static <T> Result<T> error(String msg, @Nullable T data) {
        return new Result<>(Status.ERROR, msg, data);
    }

    public static <T> Result<T> loading(@Nullable T data) {
        return new Result<>(Status.LOADING, null, data);
    }

    public static <T> Result<T> empty(@Nullable T data) {
        return new Result<>(Status.EMPTY, null, data);
    }

    public enum Status {SUCCESS, ERROR, LOADING, EMPTY}

}
