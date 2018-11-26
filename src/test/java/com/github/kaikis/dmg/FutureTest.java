package com.github.kaikis.dmg;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class FutureTest {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        System.out.println();
        Callable<Integer> callable = () -> {
            Thread.sleep(1000);
            return 1;
        };

        FutureTask<Integer> futureTask = new FutureTask<>(callable);
        Thread thread = new Thread(futureTask);
        thread.start();
        System.out.println(futureTask.get());
    }
}
