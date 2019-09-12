package com.rahmatsyah.academy.data.source;

import androidx.lifecycle.LiveData;

import com.rahmatsyah.academy.data.source.local.entity.CourseEntity;
import com.rahmatsyah.academy.data.source.local.entity.ModuleEntity;

import java.util.List;

public interface AcademyDataSource {

    LiveData<List<CourseEntity>> getAllCourses();

    LiveData<CourseEntity> getCourseWithModules(String courseId);

    LiveData<List<ModuleEntity>> getAllModulesByCourse(String courseId);

    LiveData<List<CourseEntity>> getBookmarkCourses();

    LiveData<ModuleEntity> getContent(String courseId, String moduleId);

}
