package com.sh.concurrent.executors.single;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 단하나의 쓰레드를 생성한다. 주로 쓰레드 작업중에 예외상황이 발생한 경우 예외처리를 위한 쓰레드 생성용으로 많이 사용한다
 */
public class SingleThreadExecutorTest {

    @DisplayName("Executors.newSingleThreadExecutor()")
    @Test
    public void test() {
        // 1. ExecutorService 구현체 생성
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        // 2. 제출 (Runnable)
        executorService.submit(() -> System.out.println(Thread.currentThread().getName())); // pool-1-thread-1
        executorService.submit(() -> System.out.println(Thread.currentThread().getName())); // pool-1-thread-1
        // 3. 종료 (필수)
        executorService.shutdown(); // 현재 작업 마치면 종료!
//        executorService.shutdownNow(); // 즉시 중지 (작업 종료전에 중지기능)
    }
}
