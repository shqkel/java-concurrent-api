package com.sh.concurrent.atomic.sequence;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class SequenceTest {

    @DisplayName("Atomic객체 없이 채번하기")
    @Test
    public void test1() {
        NumberGenerator numberGenerator = new NumberGenerator();
        final int nThreads = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(nThreads);
        for (int i = 0; i < nThreads; i++) {
            executorService.submit(() -> {
                System.out.println("%s - %s".formatted(Thread.currentThread().getName(), numberGenerator.nextN()));
            });
        }
        executorService.shutdown();
    }

    private static class NumberGenerator {
        Integer n = 0;

        public int nextN() {
            n++;
            return n;
        }
    }
    /*
    pool-1-thread-2 - 1
    pool-1-thread-1 - 1
    pool-1-thread-9 - 7
    pool-1-thread-8 - 2
    pool-1-thread-5 - 6
    pool-1-thread-4 - 4
    pool-1-thread-6 - 1
    pool-1-thread-7 - 5
    pool-1-thread-3 - 3
    pool-1-thread-10 - 8
     */

    @DisplayName("Atomic객체 이용해서 중복된 번호 없이 채번한다.")
    @Test
    public void test2() {
        AtomicNumberGenerator numberGenerator = new AtomicNumberGenerator();
        final int nThreads = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(nThreads);
        for (int i = 0; i < nThreads; i++) {
            executorService.submit(() -> {
                System.out.println("%s - %s".formatted(Thread.currentThread().getName(), numberGenerator.nextN()));
            });
        }
        executorService.shutdown();
    }

    private static class AtomicNumberGenerator {
        AtomicInteger atomicInteger = new AtomicInteger();

        public int nextN() {
            return atomicInteger.incrementAndGet();
        }
    }
}
