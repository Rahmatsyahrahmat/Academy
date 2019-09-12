package com.rahmatsyah.academy.vo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Objects;

import static com.rahmatsyah.academy.vo.Status.ERROR;
import static com.rahmatsyah.academy.vo.Status.LOADING;
import static com.rahmatsyah.academy.vo.Status.SUCCESS;

public class Resource<T> {

    @NonNull
    public final Status status;

    @Nullable
    public final String message;

    @Nullable
    public final T data;

    public Resource(@NonNull Status status, @Nullable T data, @Nullable String message) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public static <T> Resource<T> success(@Nullable T data){
        return new Resource<>(SUCCESS,data,null);
    }

    public static <T> Resource<T> error(String message, @Nullable T data){
        return new Resource<>(ERROR,data,message);
    }

    public static <T> Resource<T> loading(@Nullable T data){
        return new Resource<>(LOADING,data,null);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj){
            return true;
        }
        if (obj == null||getClass() != obj.getClass()){
            return false;
        }

        Resource<?> resource = (Resource<?>) obj;

        if (status != resource.status){
            return false;
        }

        if (!Objects.equals(message, resource.message)){
            return false;
        }

        return Objects.equals(data, resource.data);
    }

    @Override
    public int hashCode() {
        int result = status.hashCode();
        result = 31*result+(message!=null?message.hashCode():0);
        result = 31*result+(data!=null?data.hashCode():0);
        return result;
    }

    @Override
    public String toString() {
        return "Resource{" +
                "status=" + status +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
