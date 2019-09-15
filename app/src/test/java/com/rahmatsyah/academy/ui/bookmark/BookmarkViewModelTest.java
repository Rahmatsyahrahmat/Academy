package com.rahmatsyah.academy.ui.bookmark;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.paging.PagedList;

import com.rahmatsyah.academy.data.source.AcademyRepository;
import com.rahmatsyah.academy.data.source.local.entity.CourseEntity;
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

public class BookmarkViewModelTest {
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private AcademyRepository academyRepository = mock(AcademyRepository.class);
    private BookmarkViewModel viewModel;

    @Before
    public void setUp() {
        viewModel = new BookmarkViewModel(academyRepository);
    }

    @Test
    public void getBookmark() {

        MutableLiveData<Resource<PagedList<CourseEntity>>> dummyCourses = new MutableLiveData<>();
        PagedList<CourseEntity> pagedList = mock(PagedList.class);

        dummyCourses.setValue(Resource.success(pagedList));

        when(academyRepository.getBookmarkCourses()).thenReturn(dummyCourses);

        Observer<Resource<PagedList<CourseEntity>>> observer = mock(Observer.class);

        viewModel.getBookmarks().observeForever(observer);

        verify(observer).onChanged(Resource.success(pagedList));
    }
}