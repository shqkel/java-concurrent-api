package com.sh.concurrent.queue.bread;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BreadConcurrentQueueTest {

    @DisplayName("Bread ConcurrentLinkedQueue를 사용한 예제 ")
    @Test
    public void test() throws Exception {
        AtomicInteger atomicBread = new AtomicInteger(10);
        BlockingQueue<Integer> breadQueue = new LinkedBlockingQueue<>(5); // capacity생략시 Integer.MAX_VALUE로 설정된다.
        Lock lock = new ReentrantLock();
        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.submit(new Producer(breadQueue, atomicBread));
        executorService.submit(new Producer(breadQueue, atomicBread));
        executorService.submit(new Consumer(breadQueue, atomicBread));
        executorService.submit(new Consumer(breadQueue, atomicBread));
        executorService.awaitTermination(10, TimeUnit.SECONDS);
        /*
        쓰레드 경합때문에 출력순서는 꼬일수 있지만, 중복된 빵을 생산하거나, 소비하지 않는다.

        pool-1-thread-3 ate 10-bread!
        pool-1-thread-2 made 9-bread!
        pool-1-thread-4 ate 9-bread!
        pool-1-thread-2 made 8-bread!
        pool-1-thread-1 made 10-bread!
        pool-1-thread-2 made 7-bread!
        pool-1-thread-1 made 6-bread!
        pool-1-thread-2 made 5-bread!
        pool-1-thread-4 ate 7-bread!
        pool-1-thread-3 ate 8-bread!
        pool-1-thread-4 ate 6-bread!
        pool-1-thread-2 made 3-bread!
        pool-1-thread-1 made 4-bread!
        pool-1-thread-2 made 2-bread!
        pool-1-thread-4 ate 4-bread!
        pool-1-thread-3 ate 5-bread!
        pool-1-thread-4 ate 3-bread!
        pool-1-thread-1 made 1-bread!
        pool-1-thread-4 ate 1-bread!
        pool-1-thread-3 ate 2-bread!
         */
    }

    @RequiredArgsConstructor
    static class Producer implements Runnable {
        private final BlockingQueue<Integer> queue;
        private final AtomicInteger atomicBread;

        @Override
        public void run() {
            while(atomicBread.get() > 0) {
                int id = atomicBread.getAndDecrement();
                try {
                    queue.put(id); // queue가 가득차있는 경우 waiting
//                    TimeUnit.MILLISECONDS.sleep(500);
                    System.out.println(Thread.currentThread().getName() + " made " + id + "-bread! ");
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
    @RequiredArgsConstructor
    static class Consumer implements Runnable {
        private final BlockingQueue<Integer> queue;
        private final AtomicInteger atomicBread;
        @Override
        public void run() {
            // 판매할 빵이 남았거나, 큐가 비어있지 않은 경우 실행
            while(atomicBread.get() > 0 || !queue.isEmpty()) {
                try {
                    int id = queue.take();
//                    TimeUnit.MILLISECONDS.sleep(300);
                    System.out.println(Thread.currentThread().getName() + " ate " + id + "-bread! "); // 꺼내올 요소가 없으면 waiting
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
