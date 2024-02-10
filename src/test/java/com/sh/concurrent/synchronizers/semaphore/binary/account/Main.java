package com.sh.concurrent.synchronizers.semaphore.binary.account;

import java.util.concurrent.Semaphore;

public class Main {
    static final int LOOP = 10; // 각 스레드 반복 접근 횟수

    public static void main(String[] args) {
        Semaphore s = new Semaphore(1, true); //세마포어 객체생성 (permit = 1 : 공유자원 1개, fair = true : FIFO)
        Account account = new Account(s); // 공유객체 생성

        //스레드 생성
        Thread depositThread1 = new Thread(new Deposit(account));
        Thread withDrawThread1 = new Thread(new WithDraw(account));
        Thread depositThread2 = new Thread(new Deposit(account));
        Thread withDrawThread2 = new Thread(new WithDraw(account));

        //스레드 이름 설정
        depositThread1.setName("지훈 입금");
        withDrawThread1.setName("지훈 출금");
        depositThread2.setName("민정 입금");
        withDrawThread2.setName("민정 출금");

        //스레드 실행
        depositThread1.start();
        withDrawThread1.start();
        depositThread2.start();
        withDrawThread2.start();
        // 메인스레드 정지
        try {
            depositThread1.join();
            withDrawThread1.join();
            depositThread2.join();
            withDrawThread2.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        //잔액 출력
        account.printBalance();
    }
}
