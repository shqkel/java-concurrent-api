package com.sh.concurrent.synchronizers.semaphore.binary.account;

public class Deposit implements Runnable {

    Account account; // 공유객체

    public Deposit(Account account) {
        this.account = account;
    }

    @Override
    public void run() {
        for (int i = 0; i < Main.LOOP; i++) {
            account.deposit(10); // Main.Loop 만큼 접근하여 10원 입금
        }
    }

}