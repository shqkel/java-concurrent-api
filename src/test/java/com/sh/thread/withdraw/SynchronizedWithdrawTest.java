package com.sh.thread.withdraw;

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

    public static void main(String[] args) {
        // 공유자원
        Account acc = new Account();
        // 쓰레드
        new Thread(new ATM(acc), "ATM1").start();
        new Thread(new ATM(acc), "ATM2").start();
    }

}

