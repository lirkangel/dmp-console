package com.example.externalsort;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;


public final class AsyncHelper {

    private AsyncHelper() {}

    public static void waitExecution(Future... futures) {
        for (Future f : futures) {
            waitExecution(f);
        }
    }

    public static <T> T waitExecution(Future<T> future) {
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void waitExecution(List<Future> futures) {
        waitExecution(futures.toArray(new Future[futures.size()]));
    }
}
