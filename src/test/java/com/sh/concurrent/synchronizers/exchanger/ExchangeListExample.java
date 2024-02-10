package com.sh.concurrent.synchronizers.exchanger;

import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Exchanger;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * List 객체 자체를 교환한다 (리턴된 List의 hashcode를 통해 확인 가능)
 */
public class ExchangeListExample {
    public final static int LIST_CAPACITY = 5;
    private static Exchanger<List<Integer>> exchanger = new Exchanger<List<Integer>>();

    static class PushLoop implements Runnable {
        List<Integer> list = new LinkedList<>();
        private Scanner sc = new Scanner(System.in);

        public void run() {
            while (!Thread.interrupted()) {
                try {
                    // 리스트의 최대 크기를 범위내에서 사용자 입력값 추가
                    while (list.size() < LIST_CAPACITY) {
                        list.addLast(sc.nextInt());
                    }
                    // 리스트가 가득차면 메세지를 출력후, 다른 쓰레드의 비어있는 리스트와 교환한다.
                    // 다른 쓰레드의 Exchanger#exchange() 호출까지 대기
                    System.out.println("PushLoop : List is full...");
                    list = exchanger.exchange(list);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    static class EmptyLoop implements Runnable {
        List<Integer> list = new LinkedList<>();

        public void run() {
            while (!Thread.interrupted()) {
                try {
                    // 리스트가 비워질때까지 값을 계속 꺼낸다.
                    while (!list.isEmpty()) {
                        System.out.println(list.removeLast()); // 마지막 값부터 제거
                        Thread.sleep(500);
                    }
                    // 리스트가 비워지면 메세지를 출력후, 다른 쓰레드의 리스트와 교환한다.
                    // 다른 쓰레드의 Exchanger#exchange() 호출까지 대기
                    System.out.println("EmptyLoop : List is Empty...");
                    list = exchanger.exchange(list);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        executorService.execute(new PushLoop());
        executorService.execute(new EmptyLoop());
    }
}
