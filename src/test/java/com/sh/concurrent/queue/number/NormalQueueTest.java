package com.sh.concurrent.queue.number;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class NormalQueueTest {
    @Test
    public void test() throws InterruptedException {
        Queue<Integer> queue = new LinkedList<>();
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        executorService.submit(new Producer(queue));
        executorService.submit(new Consumer(queue));
        executorService.submit(new Consumer(queue));
        executorService.awaitTermination(3, TimeUnit.SECONDS);
    }
    /*
    pool-1-thread-1 : [1, 2, 3, 4, 5, 6, 7, 8, 9, 10]
    pool-1-thread-3 : 1
    pool-1-thread-2 : 1
    pool-1-thread-3 : 2
    pool-1-thread-2 : 3
    pool-1-thread-3 : 4
    pool-1-thread-2 : 5
    pool-1-thread-3 : 6
    pool-1-thread-3 : 8
    pool-1-thread-3 : 9
    pool-1-thread-2 : 7
    pool-1-thread-3 : 10
     */

    @RequiredArgsConstructor
    static class Producer implements Runnable {
        private final Queue<Integer> queue;

        @Override
        public void run() {
            try {
                IntStream.rangeClosed(1, 10).forEach(queue::offer);
                System.out.println(Thread.currentThread().getName() + " : " + queue);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @RequiredArgsConstructor
    static class Consumer implements Runnable {

        private final Queue<Integer> queue;

        @Override
        public void run() {
            try {
                Thread.sleep(100);
                while (!queue.isEmpty()) {
                    System.out.println(Thread.currentThread().getName() + " : " + queue.poll());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}