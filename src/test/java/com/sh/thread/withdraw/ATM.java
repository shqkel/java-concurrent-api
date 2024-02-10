package com.sh.thread.withdraw;

public class ATM implements Runnable {

    public Account acc;

    public ATM(Account acc) {
        this.acc = acc;
    }

    @Override
    public void run() {
        while (acc.getBalace() > 0) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            int money = (int) (Math.random() * 3 + 1) * 100;
            acc.withdraw(money);
        }

        System.out.println("\t[" + Thread.currentThread().getName() + "]쓰레드 종료");
    }

}