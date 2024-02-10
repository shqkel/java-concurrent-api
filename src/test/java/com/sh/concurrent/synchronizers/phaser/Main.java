package com.sh.concurrent.synchronizers.phaser;

import java.util.concurrent.Phaser;


public class Main {

    static class MyTask implements Runnable {
        private Phaser phaser;
        private String taskName;

        public MyTask(Phaser phaser, String taskName) {
            this.phaser = phaser;
            this.taskName = taskName;
            phaser.register(); // Phaser에 현재 스레드를 등록합니다.
        }

        @Override
        public void run() {
            System.out.println(taskName + " starting...");
            phaser.arriveAndAwaitAdvance(); // 모든 스레드가 도달할 때까지 대기합니다.
            System.out.println(taskName + " ending...");
        }
    }

    public static void main(String[] args) {
        Phaser phaser = new Phaser(1); // 파이저를 생성하고 초기 파티원 수를 1로 설정합니다.

        // 여러 작업을 동시에 실행하기 위해 스레드 생성
        Thread thread1 = new Thread(new MyTask(phaser, "Task 1"));
        Thread thread2 = new Thread(new MyTask(phaser, "Task 2"));
        Thread thread3 = new Thread(new MyTask(phaser, "Task 3"));

        // 각각의 스레드 시작
        thread1.start();
        thread2.start();
        thread3.start();

        // 모든 스레드가 등록되었음을 알리고 진행을 재개합니다.
        phaser.arriveAndDeregister();
    }
}