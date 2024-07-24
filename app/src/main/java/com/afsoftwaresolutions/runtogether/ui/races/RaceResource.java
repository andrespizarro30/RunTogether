package com.afsoftwaresolutions.runtogether.ui.races;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class RaceResource<T> {

    @NonNull
    public final Status status;

    @Nullable
    public final T data;

    @Nullable
    public final String message;


    public RaceResource(@NonNull Status status, @Nullable T data, @Nullable String message) {
        this.status = status;
        this.data = data;
        this.message = message;
    }

    public static <T> RaceResource<T> success (@Nullable T data) {
        return new RaceResource<>(Status.SUCCESS, data, null);
    }

    public static <T> RaceResource<T> error(@NonNull String msg, @Nullable T data) {
        return new RaceResource<>(Status.ERROR, data, msg);
    }

    public static <T> RaceResource<T> loading(@Nullable T data) {
        return new RaceResource<>(Status.LOADING, data, null);
    }

    public enum Status { SUCCESS, ERROR, LOADING}

}
