package com.pandaism.util.thread;

import java.util.HashMap;
import java.util.Map;

public class ThreadManager {
    private Map<String, Thread> threads;

    public ThreadManager() {
        this.threads = new HashMap<>();
    }

    public void runThread(String thread) {
        this.threads.get(thread).start();
    }

    public void stopThread(String thread) {
        try {
            this.threads.get(thread).join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
