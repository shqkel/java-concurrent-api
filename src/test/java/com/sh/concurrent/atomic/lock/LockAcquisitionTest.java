package com.sh.concurrent.atomic.lock;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

public class LockAcquisitionTest {

    @DisplayName("여러 쓰레드간의 공유자원의 락을 상호배제적(Mutual Exclusive)으로 획득 실패하는 테스트")
    @Test
    public void test1() {
        final Resource resource = new Resource(); // shared resource
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 10; i++) {
            executorService.submit(new TestRunnable(resource));
        }
    }
    /*
    하나이상의 쓰레드가 true, 즉 임계영역으로 설정된 자원에 하나이상의 쓰레드가 접근 가능해지므로 동기화에 실패한다.
    pool-1-thread-9 - false
    pool-1-thread-8 - false
    pool-1-thread-2 - true
    pool-1-thread-7 - false
    pool-1-thread-4 - false
    pool-1-thread-5 - false
    pool-1-thread-6 - false
    pool-1-thread-1 - true
    pool-1-thread-3 - true
    pool-1-thread-10 - false
     */

    static class Resource {
        private boolean locked = false;

        public boolean tryLock() {
            if (!locked) {
                // 비용이 큰 작업을 수행하도록 추가
                for (int i = 0; i < 10_000; i++) ;

                return locked = true;
            }
            return false;
        }
    }

    class TestRunnable implements Runnable {
        private final Resource resource;

        public TestRunnable(Resource resource) {
            this.resource = resource;
        }

        @Override
        public void run() {
            System.out.println(Thread.currentThread().getName() + " - " + resource.tryLock());
        }
    }

    @DisplayName("여러 쓰레드간의 공유자원의 락을 상호배제적(Mutual Exclusive)으로 획득하는 테스트")
    @Test
    public void test2() {
        final AtomicResource resource = new AtomicResource(); // shared resource
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 10; i++) {
            executorService.submit(new TestRunnable2(resource));
        }
    }
    /*
    pool-1-thread-4 - false
    pool-1-thread-6 - false
    pool-1-thread-1 - true
    pool-1-thread-3 - false
    pool-1-thread-2 - false
    pool-1-thread-5 - false
    pool-1-thread-8 - false
    pool-1-thread-9 - false
    pool-1-thread-10 - false
    pool-1-thread-7 - false
     */

    static class TestRunnable2 implements Runnable {
        private final AtomicResource resource;

        public TestRunnable2(AtomicResource resource) {
            this.resource = resource;
        }

        @Override
        public void run() {
            System.out.println(Thread.currentThread().getName() + " - " + resource.tryLock());
        }
    }

    /**
     * CAS(Compare And Swap) 알고리즘을 이용해서 쓰레드간 블럭킹 없이 값을 수정한다.
     * 수정시나리오는 다음과 같다.
     * 1. 메모리값을 가져와서 예상되는 기존값과 같을때만 정상처리로 간주해 값을 변경한다.
     * 2. 만약 메모리값이 기존값과 다를때는 다른 쓰레드가 중간에 끼어든 것이므로 값을 변경 하지 않는다.
     */
    private class AtomicResource {
        private AtomicBoolean locked = new AtomicBoolean(false);

        public boolean tryLock() {
            if (!locked.get()) {
                // 비용이 큰 작업을 수행하도록 추가
                for (int i = 0; i < 10_000; i++);

                return locked.compareAndSet(false, true);
            }
            return false;
        }
    }
}