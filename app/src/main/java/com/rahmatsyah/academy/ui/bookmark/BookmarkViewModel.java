package com.rahmatsyah.academy.ui.bookmark;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.PagedList;

import com.rahmatsyah.academy.data.source.AcademyRepository;
import com.rahmatsyah.academy.data.source.local.entity.CourseEntity;
import com.rahmatsyah.academy.utils.DataDummy;
import com.rahmatsyah.academy.vo.Resource;

import java.util.List;

public class BookmarkViewModel extends ViewModel {

    private AcademyRepository academyRepository;

    public BookmarkViewModel(AcademyRepository mAcademyRepository) {
        this.academyRepository = mAcademyRepository;
    }

    LiveData<Resource<PagedList<CourseEntity>>> getBookmarks() {
        return academyRepository.getBookmarkCourses();
    }

    void setBookmark(CourseEntity courseEntity){
        final boolean newState = !courseEntity.isBookmarked();
        academyRepository.setCourseBookmark(courseEntity,newState);
    }

}
