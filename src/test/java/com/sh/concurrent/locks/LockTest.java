package com.sh.concurrent.locks;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LockTest {

    @DisplayName("락을 통한 동기화 없이 공유자원 접근하는 테스트")
    @Test
    public void test() {
        final SharedData sharedData = new SharedData(); // shared resource
        int nThread = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(nThread);
        for (int i = 0; i < nThread; i++) {
            executorService.submit(new TestRunner(sharedData));
        }
        /*
        실행결과: (매번 다르다)
        pool-1-thread-6 : 540
        pool-1-thread-5 : 440
        pool-1-thread-1 : 240
        pool-1-thread-2 : 240
        pool-1-thread-4 : 340
        pool-1-thread-7 : 640
        pool-1-thread-9 : 840
        pool-1-thread-10 : 940
        pool-1-thread-3 : 240
        pool-1-thread-8 : 740
         */
    }

    static class SharedData {
        @Getter
        private int value;

        public void increase() {
            value += 1;
        }
    }

    @RequiredArgsConstructor
    public class TestRunner implements Runnable {
        private final SharedData sharedData;

        @Override
        public void run() {
            for (int i = 0; i < 100; i++)
                sharedData.increase();
            System.out.println(Thread.currentThread().getName() + " : " + sharedData.getValue());
        }
    }

    @DisplayName("락을 사용해 동기화처리된 공유자원 접근테스트")
    @Test
    public void test2() {
        final SharedData sharedData = new SharedData(); // shared resource
        final Lock lock = new ReentrantLock(); // lock instance
        int nThread = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(nThread);
        for (int i = 0; i < nThread; i++) {
            executorService.submit(new TestSynchronizedRunner(sharedData, lock));
        }
        /*
        pool-1-thread-1 : 100
        pool-1-thread-4 : 200
        pool-1-thread-3 : 300
        pool-1-thread-2 : 400
        pool-1-thread-5 : 500
        pool-1-thread-6 : 600
        pool-1-thread-7 : 700
        pool-1-thread-8 : 800
        pool-1-thread-9 : 900
        pool-1-thread-10 : 1000
         */
    }

    @RequiredArgsConstructor
    private class TestSynchronizedRunner implements Runnable {
        private final SharedData sharedData;
        private final Lock lock;

        @Override
        public void run() {
            lock.lock(); // lock시작
            try {
                for (int i = 0; i < 100; i++)
                    sharedData.increase();
                System.out.println(Thread.currentThread().getName() + " : " + sharedData.getValue());
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unlock(); // unlock하지 않으면 다른 쓰레드는 계속 공유데이터를 사용할 수 없다.
            }
        }
    }
}
