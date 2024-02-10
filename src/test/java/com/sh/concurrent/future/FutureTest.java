package com.sh.concurrent.future;

import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class FutureTest {
    @Test
    public void test() throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        Future<String> future = executorService.submit(() -> {
            // 작업1(비동기) 수행
            Thread.sleep(1000);
            return "Hello world";
        });
        // 작업2 수행
        System.out.println("////////////////////");

        String result = future.get(); // blocking
        System.out.println(result);
        // 작업3 수행
        System.out.println("////////////////////");

        executorService.shutdown();
    }
}
