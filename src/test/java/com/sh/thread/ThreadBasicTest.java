package com.sh.thread;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * #test method issues
 * - System.out.print() - System.out.flush()를 연이어 호출해야한다.
 * - System.out.println()은 정상출력된다.
 * - Thread.sleep등을 사용하는 경우 생성한 쓰레드를 기다리도록 `생성쓰레드.join()` 처리해야 한다.
 */
public class ThreadBasicTest {


    @DisplayName("싱글쓰레드 작업 - main thread")
    @Test
    public void test() throws Exception {
        App app = new App();
        app.print('+');
        app.print('-');
    }

    /**
     * test코드의 print로 출력시 결과확인이 안되는 bug 있음.
     * - System.out.print() - System.out.flush()를 연이어 호출해준다.
     */
    static class App {
        public void print(char ch) {
            for (int i = 0; i < 100; i++) {
                System.out.print(ch);
                System.out.flush();
            }
        }
    }


    @DisplayName("쓰레드생성하는 방법 1 - Thread 상속")
    @Test
    public void test2() {
        Thread th1 = new FooThread('+');
        Thread th2 = new FooThread('|');
        th1.start(); // 새로운 callstack에 Thread#run을 호출
        th2.start();
    }

    static class FooThread extends Thread {
        private char ch;

        public FooThread(char ch) {
            this.ch = ch;
        }

        /**
         * 쓰레드가 할일 정의
         */
        @Override
        public void run() {
            new App().print(ch);
        }
    }


    @DisplayName("쓰레드생성하는 방법2 - Runnable 구현")
    @Test
    public void test3() {
        Thread th1 = new Thread(new BarThread('*'));
        Thread th2 = new Thread(new BarThread('$'));
        th1.setName("일꾼1");
        th2.setName("일꾼2");
        th1.start();
        th2.start();

    }

    static class BarThread implements Runnable {
        private char ch;

        public BarThread(char ch) {
            this.ch = ch;
        }

        @Override
        public void run() {
            new App().print(ch);
            System.out.println("<" + Thread.currentThread().getName() + " 종료>");
        }
    }

    @DisplayName("Thread.sleep(millis)를 통한 쓰레드 제어")
    @Test
    void test4() throws InterruptedException {
        Thread th1 = new Thread(new SleepThread(100, '='));
        Thread th2 = new Thread(new SleepThread(50, '|'));

        th1.start();
        th2.start();

        // test method에서는 생략불가. main thread가 생성한 타 쓰레드를 기다려야 한다.
        th1.join();
        th2.join();

    }

    static class SleepThread implements Runnable {
        private final long millis;
        private final char symbol;

        public SleepThread(long millis, char symbol) {
            this.millis = millis;
            this.symbol = symbol;
        }

        @Override
        public void run() {
            for (int i = 0; i < 1000; i++) {
                System.out.print(symbol);
                System.out.flush();
                // 쓰레드를 지연시키는 메소드 sleep
                // sleep(1000) 1초지연, millisecond
                try {
                    Thread.sleep(millis);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 쓰레드의 생명주기
     * - 쓰레드는 다음 상태를 가진다.
     * - NEW 스레드 객체가 생성, 아직 start() 메소드가 호출되지 않은 상태
     * - RUNNABLE 실행 또는 실행상태로 언제든지 변경가능한 상태
     * - TERMINATED 실행을 마친 상태 (run메소드를 모두 수행한 경우)
     * - WAITING 다른 스레드의 통지(notify, notifyAll)를 기다리는 상태
     * - TIMED_WAITING 주어진 시간 동안 기다리는 상태
     * - BLOCKED 사용하고자 하는 객체의 락이 풀릴 때까지 기다리는 상태
     * <p>
     * - 쓰레드는 생성후 실행/실행대기 상태를 거쳐 종료 상태에 이르게 된다.
     * - NEW -> RUNNABLE -> TERMINATED
     * - 쓰레드는 실행중 일시정지 상태로 변경될수 있다.
     */
    @DisplayName("쓰레드 상태관리")
    @Test
    void test5() {
        Runnable runnable = () -> {
            for (long i = 0; i < 1_000_000_000; i++) {
            }
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            for (long i = 0; i < 1_000_000_000; i++) {
            }
        };

        Thread th = new Thread(runnable, "마이쓰");
        while(true) {
			Thread.State state = th.getState();	// 스레드 상태 얻기
			System.out.println("상태: " + state);

			// 객체 생성 상태일 경우 실행 대기 상태로 만듬
			if(state == Thread.State.NEW) {
				th.start();
			}

			// 종료 상태일 경우 while문을 종료함
			if(state == Thread.State.TERMINATED) {
				break;
			}
			try {
				Thread.sleep(500);
			} catch(Exception e) {}
		}
    }
    /*
    상태: NEW
    상태: TIMED_WAITING
    상태: TIMED_WAITING
    상태: TIMED_WAITING
    상태: TIMED_WAITING
    상태: RUNNABLE
    상태: TERMINATED
     */

}
