package com.sh.concurrent.synchronizers.semaphore.binary.account;

import java.util.concurrent.Semaphore;

public class Account {

    private int balance = 1000; // 잔액
    Semaphore s; // 세마포어 객체 참조변수

    public Account(Semaphore s) { // 생성자
        this.s = s;
    }


    public void deposit(int money) {
//        try {
//            s.acquire(); // 세마포어 객체를 통한 동기화 검사

            // 임계 영역(critical section)
            System.out.println(Thread.currentThread().getName() + " : " + money + "원");

            balance += money;

            System.out.println("현재 잔액 : " + balance + "원");
            System.out.println();


//            s.release(); // Lock 해제
//        } catch (InterruptedException e) {
//        }

    }

    public void withDraw(int money) {

//        try {
//            s.acquire();  // 세마포어 객체를 통한 동기화 검사

            //임계영역
            System.out.println(Thread.currentThread().getName() + " : " + money + "원");

            balance -= money;

            System.out.println("현재 잔액 : " + balance + "원");
            System.out.println();

//            s.release(); // Lock 해제
//        } catch (InterruptedException e) {
//        }


    }

    // 계좌 속 잔액 출력
    public void printBalance() {
        System.out.println("현재 잔액 : " + balance);
    }

}