package com.example.android.bakingapp.database;

import android.os.Looper;
import android.support.annotation.NonNull;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by micha on 8/14/2018.
 */

public class AppExecutors {

    // Instantiating initial variables
    private static final Object LOCK = new Object();
    private static AppExecutors sInstance;
    private final Executor diskIO;
    private final Executor mainThread;
    private final Executor networkIO;

    // Constructor for the class to be able to call the thread variables
    private AppExecutors(Executor diskIO, Executor mainThread, Executor networkIO) {
        this.diskIO = diskIO;
        this.mainThread = mainThread;
        this.networkIO = networkIO;
    }

    // Get instance of the thread. Create a new one if it is not created yet
    public static AppExecutors getsInstance() {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new AppExecutors(Executors.newSingleThreadExecutor(),
                        Executors.newFixedThreadPool(3),
                        new MainThreadExecutor());
            }
        }
        return sInstance;
    }

    // Get diskIO
    public Executor diskIO() { return diskIO; }

    // Get the main thread
    public Executor mainThread() { return mainThread; }

    // Get the network thread
    public Executor getNetworkIO() { return networkIO; }

    // Class to grab the main thread
    private static class MainThreadExecutor implements Executor {
        private android.os.Handler mainThreadHandler = new android.os.Handler(Looper.getMainLooper());

        @Override
        public void execute(@NonNull Runnable runnable) {
            mainThreadHandler.post(runnable);
        }
    }

}
