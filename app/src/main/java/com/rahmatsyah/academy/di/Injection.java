package com.rahmatsyah.academy.di;

import android.app.Application;

import com.rahmatsyah.academy.data.source.AcademyRepository;
import com.rahmatsyah.academy.data.source.local.LocalRepository;
import com.rahmatsyah.academy.data.source.remote.JsonHelper;
import com.rahmatsyah.academy.data.source.remote.RemoteRepository;

public class Injection {
    public static AcademyRepository provideRepository(Application application){
        LocalRepository localRepository = new LocalRepository();
        RemoteRepository remoteRepository = RemoteRepository.getInstance(new JsonHelper(application));
        return AcademyRepository.getInstance(localRepository,remoteRepository);
    }
}
