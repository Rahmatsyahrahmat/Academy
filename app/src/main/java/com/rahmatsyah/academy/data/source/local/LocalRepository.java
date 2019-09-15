package com.rahmatsyah.academy.data.source.local;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;

import com.rahmatsyah.academy.data.source.local.entity.CourseEntity;
import com.rahmatsyah.academy.data.source.local.entity.CourseWithModule;
import com.rahmatsyah.academy.data.source.local.entity.ModuleEntity;
import com.rahmatsyah.academy.data.source.local.room.AcademyDao;

import java.util.List;

public class LocalRepository {

    private final AcademyDao academyDao;

    private LocalRepository(AcademyDao academyDao){
        this.academyDao = academyDao;
    }

    private static LocalRepository INSTANCE;

    public static LocalRepository getInstance(AcademyDao academyDao){
        if (INSTANCE == null){
            INSTANCE = new LocalRepository(academyDao);
        }
        return INSTANCE;
    }

    public LiveData<List<CourseEntity>> getAllCourse(){
        return academyDao.getCourses();
    }

    public DataSource.Factory<Integer, CourseEntity> getBookmarkedCourses() {
        return academyDao.getBookmarkedCourse();
    }

    public LiveData<CourseWithModule> getCourseWithModules(final String courseId) {
        return academyDao.getCourseWithModuleById(courseId);
    }

    public LiveData<List<ModuleEntity>> getAllModulesByCourse(String courseId) {
        return academyDao.getModulesByCourseId(courseId);
    }

    public void insertCourses(List<CourseEntity> courses) {
        academyDao.insertCourses(courses);
    }

    public void insertModules(List<ModuleEntity> modules) {
        academyDao.insertModules(modules);
    }

    public void setCourseBookmark(CourseEntity course, boolean newState) {
        course.setBookmarked(newState);
        academyDao.updateCourse(course);
    }

    public LiveData<ModuleEntity> getModuleWithContent(String moduleId){
        return academyDao.getModuleById(moduleId);
    }

    public void updateContent(String content,String moduleId){
        academyDao.updateModuleByContent(content,moduleId);
    }

    public void setReadModule(final ModuleEntity module) {
        module.setRead(true);
        academyDao.updateModule(module);
    }
}
