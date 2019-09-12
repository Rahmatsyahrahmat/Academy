package com.rahmatsyah.academy.di;

import android.app.Application;

import com.rahmatsyah.academy.data.source.AcademyRepository;
import com.rahmatsyah.academy.data.source.local.LocalRepository;
import com.rahmatsyah.academy.data.source.local.room.AcademyDatabase;
import com.rahmatsyah.academy.utils.AppExecutors;
import com.rahmatsyah.academy.utils.JsonHelper;
import com.rahmatsyah.academy.data.source.remote.RemoteRepository;

public class Injection {
    public static AcademyRepository provideRepository(Application application){
        AcademyDatabase database = AcademyDatabase.getInstance(application);

        LocalRepository localRepository = LocalRepository.getInstance(database.academyDao());
        RemoteRepository remoteRepository = RemoteRepository.getInstance(new JsonHelper(application));
        AppExecutors appExecutors = new AppExecutors();

        return AcademyRepository.getInstance(localRepository, remoteRepository, appExecutors);
    }
}
