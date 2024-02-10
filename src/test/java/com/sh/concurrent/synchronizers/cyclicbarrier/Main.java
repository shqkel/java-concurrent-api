package com.sh.concurrent.synchronizers.cyclicbarrier;

import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    private final static int THREADS_COUNT = 5; // CyclicBarrier#await() 호출되어야 할 횟수
    private static CyclicBarrier cyclicBarrier = new CyclicBarrier(THREADS_COUNT);

    public static class RandomSleepRunnable implements Runnable {
        private int id = 0;
        private static Random random = new Random(System.currentTimeMillis()); // seed

        public RandomSleepRunnable(int id) {
            this.id = id;
        }

        @Override
        public void run() {
            System.out.println("Thread(" + id + ") : Start");
            // 1000ms 에서 2000ms 사이의 딜레이 값을 랜덤하게 생성.
            int delay = random.nextInt(1001) + 1000;
            try {
                System.out.println("Thread(" + id + ") : Sleeping " + delay + "ms");
                // 랜덤하게 주어진 값을 이용하여 딜레이를 준다.
                Thread.sleep(delay);
                System.out.println("Thread(" + id + ") : Woke up.");

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                // 대기. cyclicBarrier 를 생성할 때, 인자값으로 준 count 개수만큼
                // await를 호출한다면 모든 쓰레드의 wait 상태가 종료된다.
                cyclicBarrier.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
            System.out.println("Thread(" + id + ") : Finished.");
        }
    }

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(THREADS_COUNT);
        for(int i = 0; i < THREADS_COUNT; ++i) {
            executorService.execute(new RandomSleepRunnable(i));
        }
    }
}

/*
Thread(0) : Start
Thread(2) : Start
Thread(4) : Start
Thread(3) : Start
Thread(1) : Start
Thread(2) : Sleeping 1103ms
Thread(3) : Sleeping 1381ms
Thread(1) : Sleeping 1442ms
Thread(0) : Sleeping 1324ms
Thread(4) : Sleeping 1174ms
Thread(2) : Woke up.
Thread(4) : Woke up.
Thread(0) : Woke up.
Thread(3) : Woke up.
Thread(1) : Woke up.
Thread(3) : Finished.
Thread(1) : Finished.
Thread(0) : Finished.
Thread(4) : Finished.
Thread(2) : Finished.
 */
