package com.rahmatsyah.academy.data.source;

import androidx.lifecycle.LiveData;
import androidx.paging.PagedList;

import com.rahmatsyah.academy.data.source.local.entity.CourseEntity;
import com.rahmatsyah.academy.data.source.local.entity.CourseWithModule;
import com.rahmatsyah.academy.data.source.local.entity.ModuleEntity;
import com.rahmatsyah.academy.vo.Resource;

import java.util.List;

public interface AcademyDataSource {

    LiveData<Resource<List<CourseEntity>>> getAllCourses();

    LiveData<Resource<CourseWithModule>> getCourseWithModules(String courseId);

    LiveData<Resource<List<ModuleEntity>>> getAllModulesByCourse(String courseId);

    LiveData<Resource<PagedList<CourseEntity>>> getBookmarkCourses();

    LiveData<Resource<ModuleEntity>> getContent(String courseId);

    void setCourseBookmark(CourseEntity course, boolean state);

    void setReadModule(ModuleEntity module);

}
