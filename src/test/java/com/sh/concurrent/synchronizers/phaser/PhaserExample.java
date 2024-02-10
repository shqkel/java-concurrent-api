package com.sh.concurrent.synchronizers.phaser;

import java.util.concurrent.Phaser;

public class PhaserExample {

    private static final Phaser phaser = new Phaser(1); // main 쓰레드만 하나 미리 등록

    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 3; i++) {
            new Thread(() -> {
                phaser.register(); // 현재 쓰레드를 Phaser의 party로 추가. 자동으로 parties값이 1씩 증가
                System.out.println(Thread.currentThread().getName() + " arrived at phase " + phaser.getPhase());
                phaser.arriveAndAwaitAdvance();
                System.out.println(Thread.currentThread().getName() + " released at phase " + phaser.getPhase());
            }).start();
        }

        phaser.arriveAndDeregister(); // 마지막 스레드가 도착했음을 알리고 Phaser를 해제합니다.
    }
}