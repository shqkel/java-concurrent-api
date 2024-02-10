package com.sh.concurrent.synchronizers.countdownlatch;

import java.util.concurrent.CountDownLatch;
import java.util.stream.Stream;

public class Example1 {
    public static void main(String[] args) {
        CountDownLatch countDownLatch = new CountDownLatch(5);

        System.out.printf("[%s] workers start... ", Thread.currentThread().getName()).println();
        Stream.generate(() -> new Thread(new Worker(countDownLatch)))
                .limit(5)
                .forEach(Thread::start);

        try {
            countDownLatch.await(); // countDownLatch#countDown이 5번호출까지 대기
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.printf("[%s] workers finished!", Thread.currentThread().getName()).println();

    }

    static class Worker implements Runnable {
        final CountDownLatch countDownLatch;

        public Worker(CountDownLatch countDownLatch) {
            this.countDownLatch = countDownLatch;
        }

        @Override
        public void run() {
            System.out.printf("[%s] worker start... ", Thread.currentThread().getName()).println();
            countDownLatch.countDown();
        }
    }
}

/*
    실행결과 : 쓰레드 호출 순서는 매번 다르다.
        [Thread-0] worker start...
        [Thread-4] worker start...
        [Thread-3] worker start...
        [main] workers start...
        [Thread-2] worker start...
        [Thread-1] worker start...
        [main] workers finished!
 */
