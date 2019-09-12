package com.rahmatsyah.academy.ui.detail;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.rahmatsyah.academy.data.source.AcademyRepository;
import com.rahmatsyah.academy.data.source.local.entity.CourseEntity;
import com.rahmatsyah.academy.data.source.local.entity.CourseWithModule;
import com.rahmatsyah.academy.data.source.local.entity.ModuleEntity;
import com.rahmatsyah.academy.utils.DataDummy;
import com.rahmatsyah.academy.vo.Resource;

import java.util.List;

public class DetailCourseViewModel extends ViewModel {

    private CourseEntity courseEntity;
    private AcademyRepository academyRepository;

    private MutableLiveData<String> courseId = new MutableLiveData<>();

    public DetailCourseViewModel(AcademyRepository academyRepository){
        this.academyRepository = academyRepository;
    }

    public LiveData<Resource<CourseWithModule>> courseModule = Transformations.switchMap(courseId, mCourseId -> academyRepository.getCourseWithModules(mCourseId));

    public void setCourseId(String courseId){
        this.courseId.setValue(courseId);
    }

    public String getCourseId(){
        if (courseId.getValue()==null) return null;
        return courseId.getValue();
    }

    void setBookmark(){
        if (courseModule.getValue() != null) {
            CourseWithModule courseWithModule = courseModule.getValue().data;

            if (courseWithModule != null) {
                CourseEntity courseEntity = courseWithModule.courseEntity;

                final boolean newState = !courseEntity.isBookmarked();
                academyRepository.setCourseBookmark(courseEntity, newState);
            }
        }
    }

}
