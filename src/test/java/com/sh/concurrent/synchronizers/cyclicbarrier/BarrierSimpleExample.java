package com.sh.concurrent.synchronizers.cyclicbarrier;

import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class BarrierSimpleExample {

    private final static int PARTIES = 3;
    private static CyclicBarrier cyclicBarrier = new CyclicBarrier(PARTIES);

    public static void main(String[] args) throws InterruptedException {
        int nThread = PARTIES;
        ExecutorService executorService = Executors.newFixedThreadPool(nThread);
        for (int i = 0; i < nThread; i++) {
            executorService.submit(() -> {
                try {
                    Thread.sleep(new Random().nextInt(5000)); // 0 ~ 5초 대기후
                    System.out.println("Thread " + Thread.currentThread().getName() + " arrived");
                    cyclicBarrier.await();
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
                System.out.println("Thread " + Thread.currentThread().getName() + " released");
            });
        }
    }
}