package com.sh.concurrent.synchronizers.semaphore.binary.account;

public class WithDraw implements Runnable {

    Account account; // 공유 객체

    public WithDraw(Account account) {
        this.account = account;
    }

    @Override
    public void run() {
        for (int i = 0; i < Main.LOOP; i++) {
            account.withDraw(10); // 10원 입금
        }
    }


}