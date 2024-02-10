package com.sh.thread.withdraw;

public class Account {

    private int balance = 1000;

    public int getBalace() {
        return balance;
    }


    /**
     * <pre>
     * 출금메소드 동기화처리하지 않으면, 하나의 쓰레드에서 받아야할 정보의 무결성이 깨지고 있다.
     *
     * 해결
     * - 1. withdraw메소드를 synchronized 처리한다.
     * - 2. 조건처리문을 synchronized블럭안에 넣는다. 이때 lock을 거는 대상은 이 객체이다.
     * </pre>
     *
     * @param money
     */
    public synchronized void withdraw(int money) {
        System.out.println(Thread.currentThread().getName() + ">> 현재잔액 : " + balance);
        if (balance >= money) {
            //로직처리에 더 시간이 걸리다고 가정!!
            for (int i = 0; i < 10000000; i++) ;

            balance -= money;
            System.out.println(Thread.currentThread().getName() + ">> 출금 : " + money + "원, 잔액 : " + balance + "원");
        } else
            System.out.println("잔액이 부족하여 출금할 수 없습니다.");

        // synchronized 블럭 이용
//		synchronized(this){
//			if(balance >= money){
//				//로직처리에 더 시간이 걸리다고 가정!!
//				for(int i=0; i<10000; i++);
//				balance -= money;
//				System.out.println(Thread.currentThread().getName()+">> 출금 : "+money+"원, 잔액 : "+balance);
//			}
//			else
//				System.out.println("잔액이 부족하여 출금할 수 없습니다.");
//		}
    }
}
