package com.rahmatsyah.academy.utils;

import java.util.concurrent.Executor;

public class InstantAppExecutor extends AppExecutors {
    private static Executor instant = Runnable::run;

    public InstantAppExecutor(){
        super(instant,instant,instant);
    }
}
