package com.sh.concurrent.queue.number;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

import java.util.concurrent.*;
import java.util.stream.IntStream;

public class LinkedBlockingQueueTest {

    @Test
    public void test() throws InterruptedException {
        BlockingQueue<Integer> queue = new LinkedBlockingQueue<>(10); // capacity생략시 Integer.MAX_VALUE로 설정된다.
        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.submit(new Producer(queue));
        executorService.submit(new Consumer(queue));
        executorService.submit(new Consumer(queue));
        executorService.submit(new Consumer(queue));
        executorService.awaitTermination(10, TimeUnit.SECONDS);
    }
    /*
    pool-1-thread-1 : [1, 2, 3, 4, 5, 6, 7, 8, 9, 10]
    pool-1-thread-4 : 3
    pool-1-thread-3 : 1
    pool-1-thread-3 : 5
    pool-1-thread-2 : 2
    pool-1-thread-2 : 7
    pool-1-thread-3 : 6
    pool-1-thread-4 : 4
    pool-1-thread-3 : 9
    pool-1-thread-2 : 8
    pool-1-thread-4 : 10
    pool-1-thread-3 : 11
    pool-1-thread-2 : 12
    pool-1-thread-4 : 13
    pool-1-thread-3 : 14
    pool-1-thread-2 : 15
    pool-1-thread-4 : 16
    pool-1-thread-3 : 17
    pool-1-thread-2 : 18
    pool-1-thread-4 : 19
    pool-1-thread-3 : 20
     */

    @RequiredArgsConstructor
    static class Producer implements Runnable {
        private final BlockingQueue<Integer> queue;

        @Override
        public void run() {
            try {
                IntStream.rangeClosed(1, 10).forEach(queue::offer);
                System.out.println(Thread.currentThread().getName() + " : " + queue);

                IntStream.rangeClosed(11, 20).forEach((n) -> {
                    try {
                        Thread.sleep(500);
                        queue.put(n);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @RequiredArgsConstructor
    static class Consumer implements Runnable {
        private final BlockingQueue<Integer> queue;

        @Override
        public void run() {
            try {
                Thread.sleep(100);
//            while(!queue.isEmpty()){ // 10개만 소비후 끝
                while (true) {
                    System.out.println(Thread.currentThread().getName() + " : " + queue.take()); // 꺼내올 요소가 없으면 waiting
//                System.out.println(Thread.currentThread().getName() + " : " + queue.poll()); // 꺼내올 요소가 없으면 null 반환
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + " End!");
        }
    }
}