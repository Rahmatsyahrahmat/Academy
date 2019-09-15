package com.rahmatsyah.academy.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.rahmatsyah.academy.data.source.AcademyRepository;
import com.rahmatsyah.academy.di.Injection;
import com.rahmatsyah.academy.ui.academy.AcademyViewModel;
import com.rahmatsyah.academy.ui.bookmark.BookmarkViewModel;
import com.rahmatsyah.academy.ui.detail.DetailCourseViewModel;
import com.rahmatsyah.academy.ui.reader.CourseReaderViewModel;

public class ViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private static volatile ViewModelFactory instance;

    private final AcademyRepository academyRepository;

    private ViewModelFactory(AcademyRepository academyRepository){
        this.academyRepository = academyRepository;
    }

    public static ViewModelFactory getInstance(Application application){
        if (instance==null){
            synchronized (ViewModelFactory.class){
                if (instance==null){
                    instance = new ViewModelFactory(Injection.provideRepository(application));
                }
            }
        }
        return instance;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {

        if (modelClass.isAssignableFrom(AcademyViewModel.class)) {
            //noinspection unchecked
            return (T) new AcademyViewModel(academyRepository);
        } else if (modelClass.isAssignableFrom(DetailCourseViewModel.class)) {
            //noinspection unchecked
            return (T) new DetailCourseViewModel(academyRepository);
        } else if (modelClass.isAssignableFrom(BookmarkViewModel.class)) {
            //noinspection unchecked
            return (T) new BookmarkViewModel(academyRepository);
        } else if (modelClass.isAssignableFrom(CourseReaderViewModel.class)) {
            //noinspection unchecked
            return (T) new CourseReaderViewModel(academyRepository);
        }

        throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
    }
}
