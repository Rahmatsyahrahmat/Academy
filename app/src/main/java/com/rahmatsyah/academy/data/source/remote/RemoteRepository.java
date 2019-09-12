package com.rahmatsyah.academy.data.source.remote;

import android.os.Handler;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.rahmatsyah.academy.data.source.remote.response.ContentResponse;
import com.rahmatsyah.academy.data.source.remote.response.CourseResponse;
import com.rahmatsyah.academy.data.source.remote.response.ModuleResponse;
import com.rahmatsyah.academy.utils.EspressoIdlingResource;
import com.rahmatsyah.academy.utils.JsonHelper;

import java.util.List;

public class RemoteRepository {

    private static RemoteRepository INSTANCE;
    private JsonHelper jsonHelper;
    private final long SERVICE_LATENCY_IN_MILLIS = 2000;

    private RemoteRepository(JsonHelper jsonHelper) {
        this.jsonHelper = jsonHelper;
    }

    public static RemoteRepository getInstance(JsonHelper helper) {
        if (INSTANCE == null) {
            INSTANCE = new RemoteRepository(helper);
        }
        return INSTANCE;
    }

    public LiveData<ApiResponse<List<CourseResponse>>> getAllCoursesAsLiveData() {
        EspressoIdlingResource.increment();

        MutableLiveData<ApiResponse<List<CourseResponse>>> coursesResult = new MutableLiveData<>();

        Handler handler = new Handler();
        handler.postDelayed(() -> {
            coursesResult.setValue(ApiResponse.success(jsonHelper.loadCourses()));
            if (!EspressoIdlingResource.getEspressoIdlingResource().isIdleNow()){
                EspressoIdlingResource.decrement();
            }
        }, SERVICE_LATENCY_IN_MILLIS);
        return coursesResult;
    }

    public LiveData<ApiResponse<List<ModuleResponse>>> getModulesByCourseAsLiveData(String courseId) {
        EspressoIdlingResource.increment();

        MutableLiveData<ApiResponse<List<ModuleResponse>>> modulesResult = new MutableLiveData<>();

        Handler handler = new Handler();
        handler.postDelayed(() -> {
            modulesResult.setValue(ApiResponse.success(jsonHelper.loadModule(courseId)));
            if (!EspressoIdlingResource.getEspressoIdlingResource().isIdleNow()) {
                EspressoIdlingResource.decrement();
            }
        }, SERVICE_LATENCY_IN_MILLIS);
        return modulesResult;
    }

    public LiveData<ApiResponse<ContentResponse>> getContentAsLiveData(String moduleId) {
        EspressoIdlingResource.increment();

        MutableLiveData<ApiResponse<ContentResponse>> contentResult = new MutableLiveData<>();

        Handler handler = new Handler();
        handler.postDelayed(() -> {
            contentResult.setValue(ApiResponse.success(jsonHelper.loadContent(moduleId)));
            if (!EspressoIdlingResource.getEspressoIdlingResource().isIdleNow()) {
                EspressoIdlingResource.decrement();
            }
        }, SERVICE_LATENCY_IN_MILLIS);
        return contentResult;
    }

}
