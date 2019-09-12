package com.rahmatsyah.academy.ui.academy;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.rahmatsyah.academy.data.source.AcademyRepository;
import com.rahmatsyah.academy.data.source.local.entity.CourseEntity;
import com.rahmatsyah.academy.utils.DataDummy;
import com.rahmatsyah.academy.vo.Resource;

import java.util.List;

public class AcademyViewModel extends ViewModel {
    private AcademyRepository academyRepository;

    private MutableLiveData<String> login = new MutableLiveData<>();

    public AcademyViewModel(AcademyRepository academyRepository){
        this.academyRepository = academyRepository;
    }

    LiveData<Resource<List<CourseEntity>>> courses = Transformations.switchMap(login,data->academyRepository.getAllCourses());

    void setUsername(String username){
        login.setValue(username);
    }
}
