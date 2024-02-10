package com.sh.concurrent.synchronizers.exchanger;

import java.util.concurrent.Exchanger;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * 동시에
 */
public class ExchangerSimpleExample {

    private static final Exchanger<String> exchanger = new Exchanger<>();

    public static void main(String[] args) throws InterruptedException {
        Thread thread1 = new Thread(() -> {
            try {
                String out = "Thread 1 data";
//                String in = exchanger.exchange(out);
                String in = exchanger.exchange(out, 1, TimeUnit.SECONDS);
                System.out.println("Thread 1 received: " + in);
            } catch (InterruptedException | TimeoutException e) {
                e.printStackTrace();
            }
        });

        Thread thread2 = new Thread(() -> {
            try {
                String out = "Thread 2 data";
                Thread.sleep(999);
                String in = exchanger.exchange(out);
                System.out.println("Thread 2 received: " + in);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        thread1.start();
        thread2.start();
    }
}