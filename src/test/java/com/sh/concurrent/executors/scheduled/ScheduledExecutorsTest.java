package com.sh.concurrent.executors.scheduled;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.concurrent.*;
import java.util.function.Function;

public class ScheduledExecutorsTest {


    @DisplayName("Executors.newSingleThreadScheduledExecutor()")
    @Test
    public void test1() throws ExecutionException, InterruptedException, TimeoutException {
        // 1. ExecutorService 구현체 생성
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        // 2. 등록
        Function<String, Runnable> printerFactory = (msg) -> () -> System.out.println(msg + "[" + LocalDateTime.now() + "]");
        // Task1 : n초후 실행
        ScheduledFuture<?> future = executorService.schedule(printerFactory.apply("Hello world"), 3, TimeUnit.SECONDS);
        // Task2:  delay초이후,  n초주기로 실행
        executorService.scheduleAtFixedRate(printerFactory.apply("Byebye world"), 1, 1, TimeUnit.SECONDS);

        // 3.종료
        try {
            // 테스트메소드에서는 명시적 설정필요하다. Wait for the executor service to terminate
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS); // 9_220_000_000_000_000_000나노초 (9_220_000_000초) (≒ 106712일)
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Main thread finished!");
    }
}
