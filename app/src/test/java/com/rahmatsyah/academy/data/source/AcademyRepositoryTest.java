package com.rahmatsyah.academy.data.source;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import androidx.paging.PagedList;

import com.rahmatsyah.academy.data.source.local.LocalRepository;
import com.rahmatsyah.academy.data.source.local.entity.CourseEntity;
import com.rahmatsyah.academy.data.source.local.entity.CourseWithModule;
import com.rahmatsyah.academy.data.source.local.entity.ModuleEntity;
import com.rahmatsyah.academy.data.source.remote.RemoteRepository;
import com.rahmatsyah.academy.data.source.remote.response.ContentResponse;
import com.rahmatsyah.academy.data.source.remote.response.CourseResponse;
import com.rahmatsyah.academy.data.source.remote.response.ModuleResponse;
import com.rahmatsyah.academy.utils.FakeDataDummy;
import com.rahmatsyah.academy.utils.InstantAppExecutor;
import com.rahmatsyah.academy.utils.LiveDataTestUtil;
import com.rahmatsyah.academy.utils.PageListUtil;
import com.rahmatsyah.academy.vo.Resource;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AcademyRepositoryTest {
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private LocalRepository local = mock(LocalRepository.class);
    private RemoteRepository remote = mock(RemoteRepository.class);
    private InstantAppExecutor instantAppExecutors = mock(InstantAppExecutor.class);
    private FakeAcademyRepository academyRepository = new FakeAcademyRepository(local, remote, instantAppExecutors);


    private ArrayList<CourseResponse> courseResponses = FakeDataDummy.generateRemoteDummyCourses();
    private List<CourseEntity> courseEntities = FakeDataDummy.generateDummyCourses();
    private String courseId = courseResponses.get(0).getId();
    private ArrayList<ModuleResponse> moduleResponses = FakeDataDummy.generateRemoteDummyModules(courseId);
    private String moduleId = moduleResponses.get(0).getModuleId();
    private ContentResponse content = FakeDataDummy.generateRemoteDummyContent(moduleId);

    @Before
    public void setUp() {

    }

    @After
    public void tearDown() {

    }

    @Test
    public void getAllCourses() {

        MutableLiveData<List<CourseEntity>> dummyCourses = new MutableLiveData<>();
        dummyCourses.setValue(FakeDataDummy.generateDummyCourses());

        when(local.getAllCourse()).thenReturn(dummyCourses);

        Resource<List<CourseEntity>> result = LiveDataTestUtil.getValue(academyRepository.getAllCourses());

        verify(local).getAllCourse();
        assertNotNull(result.data);
        assertEquals(courseResponses.size(), result.data.size());
    }

    @Test
    public void getAllModulesByCourse() {
        MutableLiveData<List<ModuleEntity>> dummyModules = new MutableLiveData<>();
        dummyModules.setValue(FakeDataDummy.generateDummyModules(courseId));

        when(local.getAllModulesByCourse(courseId)).thenReturn(dummyModules);

        Resource<List<ModuleEntity>> result = LiveDataTestUtil.getValue(academyRepository.getAllModulesByCourse(courseId));

        verify(local).getAllModulesByCourse(courseId);
        assertNotNull(result.data);
        assertEquals(moduleResponses.size(), result.data.size());
    }

    @Test
    public void getBookmarkedCourses() {
        DataSource.Factory<Integer, CourseEntity> dataSource = mock(DataSource.Factory.class);

        when(local.getBookmarkedCourses()).thenReturn(dataSource);
        academyRepository.getBookmarkCourses();
        Resource<PagedList<CourseEntity>> result = Resource.success(PageListUtil.mockPagedList(courseEntities));

        verify(local).getBookmarkedCourses();
        assertNotNull(result.data);
        assertEquals(courseEntities.size(),result.data.size());
    }

    @Test
    public void getContent() {
        MutableLiveData<ModuleEntity> dummyEntity = new MutableLiveData<>();
        dummyEntity.setValue(FakeDataDummy.generateDummyModuleWithContent(moduleId));

        when(local.getModuleWithContent(courseId)).thenReturn(dummyEntity);

        Resource<ModuleEntity> result = LiveDataTestUtil.getValue(academyRepository.getContent(courseId));

        verify(local).getModuleWithContent(courseId);
        assertNotNull(result);

        assertNotNull(result.data);
        assertNotNull(result.data.contentEntity);
        assertNotNull(result.data.contentEntity.getContent());
        assertEquals(content.getContent(), result.data.contentEntity.getContent());
    }


    @Test
    public void getCourseWithModules() {
        MutableLiveData<CourseWithModule> dummyEntity = new MutableLiveData<>();
        dummyEntity.setValue(FakeDataDummy.generateDummyCourseWithModules(FakeDataDummy.generateDummyCourses().get(0), false));

        when(local.getCourseWithModules(courseId)).thenReturn(dummyEntity);

        Resource<CourseWithModule> result = LiveDataTestUtil.getValue(academyRepository.getCourseWithModules(courseId));

        verify(local).getCourseWithModules(courseId);
        assertNotNull(result.data);
        assertNotNull(result.data.courseEntity.getTitle());
        assertEquals(courseResponses.get(0).getTitle(), result.data.courseEntity.getTitle());
    }
}