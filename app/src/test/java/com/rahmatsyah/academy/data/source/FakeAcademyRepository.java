package com.rahmatsyah.academy.data.source;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.rahmatsyah.academy.data.source.local.LocalRepository;
import com.rahmatsyah.academy.data.source.local.entity.ContentEntity;
import com.rahmatsyah.academy.data.source.local.entity.CourseEntity;
import com.rahmatsyah.academy.data.source.local.entity.CourseWithModule;
import com.rahmatsyah.academy.data.source.local.entity.ModuleEntity;
import com.rahmatsyah.academy.data.source.remote.ApiResponse;
import com.rahmatsyah.academy.data.source.remote.RemoteRepository;
import com.rahmatsyah.academy.data.source.remote.response.ContentResponse;
import com.rahmatsyah.academy.data.source.remote.response.CourseResponse;
import com.rahmatsyah.academy.data.source.remote.response.ModuleResponse;
import com.rahmatsyah.academy.utils.AppExecutors;
import com.rahmatsyah.academy.vo.Resource;

import java.util.ArrayList;
import java.util.List;

public class FakeAcademyRepository implements AcademyDataSource {

    private volatile static FakeAcademyRepository INSTANCE = null;

    private final LocalRepository localRepository;
    private final RemoteRepository remoteRepository;
    private final AppExecutors appExecutors;

    FakeAcademyRepository(@NonNull LocalRepository localRepository, @NonNull RemoteRepository remoteRepository, AppExecutors appExecutors) {
        this.localRepository = localRepository;
        this.remoteRepository = remoteRepository;
        this.appExecutors = appExecutors;
    }

    public static FakeAcademyRepository getInstance(LocalRepository localRepository, RemoteRepository remoteData, AppExecutors appExecutors) {
        if (INSTANCE == null) {
            synchronized (AcademyRepository.class) {
                if (INSTANCE == null) {
                    INSTANCE = new FakeAcademyRepository(localRepository, remoteData, appExecutors);
                }
            }
        }
        return INSTANCE;
    }


    @Override
    public LiveData<Resource<List<CourseEntity>>> getAllCourses() {
        return new NetworkBoundResource<List<CourseEntity>, List<CourseResponse>>(appExecutors) {
            @Override
            public LiveData<List<CourseEntity>> loadFromDB() {
                return localRepository.getAllCourse();
            }

            @Override
            public Boolean shouldFetch(List<CourseEntity> data) {
                return (data == null) || (data.size() == 0);
            }

            @Override
            public LiveData<ApiResponse<List<CourseResponse>>> createCall() {
                return remoteRepository.getAllCoursesAsLiveData();
            }

            @Override
            public void saveCallResult(List<CourseResponse> courseResponses) {
                List<CourseEntity> courseEntities = new ArrayList<>();

                for (CourseResponse courseResponse : courseResponses) {

                    courseEntities.add(new CourseEntity(courseResponse.getId(),
                            courseResponse.getTitle(),
                            courseResponse.getDescription(),
                            courseResponse.getDate(),
                            null, courseResponse.getImagePath()));
                }

                localRepository.insertCourses(courseEntities);
            }
        }.asLiveData();
    }

    @Override
    public LiveData<Resource<CourseWithModule>> getCourseWithModules(final String courseId) {
        return new NetworkBoundResource<CourseWithModule, List<ModuleResponse>>(appExecutors) {
            @Override
            protected LiveData<CourseWithModule> loadFromDB() {
                return localRepository.getCourseWithModules(courseId);
            }

            @Override
            protected Boolean shouldFetch(CourseWithModule courseWithModule) {
                return (courseWithModule == null || courseWithModule.moduleEntities == null) || (courseWithModule.moduleEntities.size() == 0);
            }

            @Override
            protected LiveData<ApiResponse<List<ModuleResponse>>> createCall() {
                return remoteRepository.getModulesByCourseAsLiveData(courseId);
            }

            @Override
            protected void saveCallResult(List<ModuleResponse> moduleResponses) {

                List<ModuleEntity> moduleEntities = new ArrayList<>();

                for (ModuleResponse moduleResponse : moduleResponses) {
                    moduleEntities.add(new ModuleEntity(moduleResponse.getModuleId(), courseId, moduleResponse.getTitle(), moduleResponse.getPosition(), null));
                }

                localRepository.insertModules(moduleEntities);
            }
        }.asLiveData();
    }

    @Override
    public LiveData<Resource<PagedList<CourseEntity>>> getBookmarkCourses() {
        return new NetworkBoundResource<PagedList<CourseEntity>, List<CourseResponse>>(appExecutors) {
            @Override
            protected LiveData<PagedList<CourseEntity>> loadFromDB() {
                return new LivePagedListBuilder<>(localRepository.getBookmarkedCourses(),20).build();
            }

            @Override
            protected Boolean shouldFetch(PagedList<CourseEntity> data) {
                return false;
            }

            @Override
            protected LiveData<ApiResponse<List<CourseResponse>>> createCall() {
                return null;
            }

            @Override
            protected void saveCallResult(List<CourseResponse> data) {

            }
        }.asLiveData();
    }


    @Override
    public LiveData<Resource<List<ModuleEntity>>> getAllModulesByCourse(String courseId) {
        return new NetworkBoundResource<List<ModuleEntity>, List<ModuleResponse>>(appExecutors) {
            @Override
            protected LiveData<List<ModuleEntity>> loadFromDB() {
                return localRepository.getAllModulesByCourse(courseId);
            }

            @Override
            protected Boolean shouldFetch(List<ModuleEntity> modules) {
                return (modules == null) || (modules.size() == 0);
            }

            @Override
            protected LiveData<ApiResponse<List<ModuleResponse>>> createCall() {
                return remoteRepository.getModulesByCourseAsLiveData(courseId);
            }

            @Override
            protected void saveCallResult(List<ModuleResponse> moduleResponses) {

                List<ModuleEntity> moduleEntities = new ArrayList<>();

                for (ModuleResponse moduleResponse : moduleResponses) {
                    moduleEntities.add(new ModuleEntity(moduleResponse.getModuleId(), courseId, moduleResponse.getTitle(), moduleResponse.getPosition(), null));
                }

                localRepository.insertModules(moduleEntities);

            }
        }.asLiveData();
    }


    @Override
    public LiveData<Resource<ModuleEntity>> getContent(String moduleId) {
        return new NetworkBoundResource<ModuleEntity, ContentResponse>(appExecutors) {
            @Override
            protected LiveData<ModuleEntity> loadFromDB() {
                return localRepository.getModuleWithContent(moduleId);
            }

            @Override
            protected Boolean shouldFetch(ModuleEntity moduleEntity) {
                return (moduleEntity.contentEntity == null);
            }

            @Override
            protected LiveData<ApiResponse<ContentResponse>> createCall() {
                return remoteRepository.getContentAsLiveData(moduleId);
            }

            @Override
            protected void saveCallResult(ContentResponse contentResponse) {

                localRepository.updateContent(contentResponse.getContent(), moduleId);
            }
        }.asLiveData();
    }

    @Override
    public void setCourseBookmark(CourseEntity course, boolean state) {

        Runnable runnable = () -> localRepository.setCourseBookmark(course, state);

        appExecutors.diskIO().execute(runnable);
    }

    @Override
    public void setReadModule(ModuleEntity module) {
        Runnable runnable = () -> localRepository.setReadModule(module);

        appExecutors.diskIO().execute(runnable);
    }
}
