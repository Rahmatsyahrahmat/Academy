package com.rahmatsyah.academy.ui.detail;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.rahmatsyah.academy.data.source.AcademyRepository;
import com.rahmatsyah.academy.data.source.local.entity.CourseEntity;
import com.rahmatsyah.academy.data.source.local.entity.CourseWithModule;
import com.rahmatsyah.academy.data.source.local.entity.ModuleEntity;
import com.rahmatsyah.academy.utils.FakeDataDummy;
import com.rahmatsyah.academy.vo.Resource;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class DetailCourseViewModelTest {
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private DetailCourseViewModel viewModel;
    private AcademyRepository academyRepository = mock(AcademyRepository.class);
    private CourseEntity dummyCourse = FakeDataDummy.generateDummyCourses().get(0);
    private String courseId = dummyCourse.getCourseId();
    private ArrayList<ModuleEntity> dummyModules = FakeDataDummy.generateDummyModules(courseId);


    @Before
    public void setUp() {
        viewModel = new DetailCourseViewModel(academyRepository);
        viewModel.setCourseId(courseId);
    }

    @Test
    public void getCourseWithModule() {
        Resource<CourseWithModule> resource = Resource.success(FakeDataDummy.generateDummyCourseWithModules(dummyCourse,true));
        MutableLiveData<Resource<CourseWithModule>> courseEntities = new MutableLiveData<>();
        courseEntities.setValue(resource);

        when(academyRepository.getCourseWithModules(courseId)).thenReturn(courseEntities);

        Observer<Resource<CourseWithModule>> observer = mock(Observer.class);

        viewModel.courseModule.observeForever(observer);

        verify(observer).onChanged(resource);
    }


}