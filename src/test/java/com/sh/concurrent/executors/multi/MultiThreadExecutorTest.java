package com.sh.concurrent.executors.multi;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.function.BiFunction;

/**
 *
 */
public class MultiThreadExecutorTest {

    /**
     * 최대 쓰레드를 n개까지 만드는 풀.  동시에 일어나는 업무의 량이 비교적 일정할때 사용한다.
     */
    @DisplayName("Executors.newFixedThreadPool(nThreads:int)")
    @Test
    public void test1() {
        // 1. ExecutorService 구현체 생성
        ExecutorService executorService = Executors.newFixedThreadPool(5); // 쓰레드 객수 지정
        // 2. 제출
        executorService.submit(() -> System.out.println(Thread.currentThread().getName()));
        executorService.submit(() -> System.out.println(Thread.currentThread().getName()));
        executorService.submit(() -> System.out.println(Thread.currentThread().getName()));
        executorService.submit(() -> System.out.println(Thread.currentThread().getName()));

        // 생성한 쓰레드를 재사용해서 다음 작업을 수행한다.
        BiFunction<Integer, Integer, Runnable> adder = (n1, n2) -> (() -> System.out.println(Thread.currentThread().getName() + " : " + (n1 + n2)));
        executorService.submit(adder.apply(3, 7));
        executorService.submit(adder.apply(2, 6));
        executorService.submit(adder.apply(1, 5));
        executorService.submit(adder.apply(3, 1));

        // 3. 종료
        executorService.shutdown();

        /*
            pool-1-thread-1
            pool-1-thread-2
            pool-1-thread-3
            pool-1-thread-4
            pool-1-thread-5 : 10
            pool-1-thread-2 : 6
            pool-1-thread-3 : 4
            pool-1-thread-1 : 8

         */

    }

    /**
     * 쓰레드 수의 제한을 두지 않은 방식의 쓰레드풀 방식으로, 새로운 쓰레드 시작 요청이 들어올때마다 하나씩 쓰레드를 생성한다.
     * 업무가 종료된 쓰레드들은 바로 사라지지 않고 1분동안 살아있다가 다른 작업 요청이 없으면 사라진다.
     * 짧고 반복되는 스타일의 작업요청이 들어올 경우 유용하다.
     */
    @DisplayName("Executors.newCachedThreadPool()")
    @Test
    public void test() {
        ExecutorService executorService = Executors.newCachedThreadPool();
        for (int i = 0; i < 2; i++) {
            final int N = i;
            executorService.execute(() -> System.out.println("Task" + (N + 1)));  // 2개의 쓰레드 생성요청
        }
        // 현재 풀 사이즈 크기 확인
        System.out.println("cached thread pool size was " + ((ThreadPoolExecutor) executorService).getPoolSize());
        try {
            Thread.sleep( 61 * 1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        // 1분 1초 동안 기다림(60초 후 사용되지 않는 쓰레드가 제거되고 풀의 사이즈가 변경된다.)
        System.out.println("cached thread pool size was " + ((ThreadPoolExecutor) executorService).getPoolSize());
        executorService.shutdown();
        while (!executorService.isTerminated()) {

        }
        System.out.println("tasks are completed");
    }
}
