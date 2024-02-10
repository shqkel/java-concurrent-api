package com.sh.concurrent.synchronizers.semaphore.binary.withdraw;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

import java.util.concurrent.*;

/**
 * <pre>
 * 하나의 계좌에서 출금메소드를 호출하는 두 쓰레드를 통해 동기화의 필요성과 사용법을 학습한다.
 * - 공유자원 : 멀티쓰레드 환경에서 각 쓰레드 공통으로 참조하는 객체 (Account)
 * - 동기화 Synchronization : 멀티쓰레드에서 공유자원을 사용하는 순서를 정해줘야 문제가 발생하지 않는다.
 * - 임계영역 Critical Section : 한번에 하나의 쓰레드만 사용하는 공유자원. Lock을 획득한 쓰레드만 접근이 가능하다.
 *
 * - 동기화처리1 - synchronized 메소드로 작성 (해당객체가 임계영역으로 설정됨)
 * - 동기화처리2 - synchronized 블럭 작성 (임계영역 객체를 지정). 좀더 적은 범위를 임계영역으로 설정할 수 있어 효율적이다.
 * </pre>
 */
public class SynchronizedWithdrawTest {

    @Test
    public void test() throws InterruptedException, ExecutionException {
        // 공유자원
        Semaphore semaphore = new Semaphore(1, true);
        Account acc = new Account(semaphore);
        // 쓰레드
        final int nThreads = 2;
        ExecutorService executorService = Executors.newFixedThreadPool(nThreads);
        for (int i = 0; i < nThreads; i++) {
            Future<?> future = executorService.submit(new Atm(acc));
            future.get(); // main thread 대기를 위해 명시적 호출 (test only)
        }
    }

    @RequiredArgsConstructor
    static class Account {
        private final Semaphore semaphore;
        @Getter
        private int balance = 1000;

        /**
         * <pre>
         * 출금메소드 동기화처리하지 않으면, 하나의 쓰레드에서 받아야할 정보의 무결성이 깨지고 있다.
         * </pre>
         *
         * @param money
         */
        public void withdraw(int money) {
            try {
                semaphore.acquire();
                System.out.println(Thread.currentThread().getName() + ">> 현재잔액 : " + balance);
                if (balance >= money) {
                    //로직처리에 더 시간이 걸리다고 가정!!
                    for (int i = 0; i < 10000000; i++) ;

                    balance -= money;
                    System.out.println(Thread.currentThread().getName() + ">> 출금 : " + money + "원, 잔액 : " + balance + "원");
                } else
                    System.out.println("잔액이 부족하여 출금할 수 없습니다.");
                semaphore.release();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @RequiredArgsConstructor
    static class Atm implements Runnable {

        public final Account acc;

        @Override
        public void run() {
            while (acc.getBalance() > 0) {
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

}

