package com.sh.concurrent.future;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class CompletableFutureTest {

    /**
     * <pre>
     * CompletableFuture.runAsync(runnable: Runnable):  CompletableFuture&lt;Void>
     * - jdk7부터 지원하는  ForkJoinPool의 commonPool()부터 쓰레드를 가져와 Supplier 비동기 작업을 실행한다.
     * - ExecutorService 사용도 가능하다.
     * - 비동기 작업은 별도의 쓰레드를 통해 처리되지 때문에 main(Test worker)쓰레드와 출력순서는 랜덤하다.
     * </pre>
     */
    @Test
    public void runAsync() throws ExecutionException, InterruptedException {
        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            System.out.println("Thread: " + Thread.currentThread().getName()); // Thread: ForkJoinPool.commonPool-worker-1
        });

        System.out.println("Thread: " + Thread.currentThread().getName()); // Thread: Test worker
    }

    /**
     * <pre>
     * CompletableFuture.supplyAsync(supplier: Supplier&lt;U>):  CompletableFuture&lt;U>
     * - jdk7부터 지원하는  ForkJoinPool의 commonPool()부터 쓰레드를 가져와 Supplier 비동기 작업을 실행한다.
     * - ExecutorService 사용도 가능하다.
     * </pre>
     */
    @Test
    public void supplyAsync() throws ExecutionException, InterruptedException {
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            return "Thread: " + Thread.currentThread().getName();
        });

        System.out.println(future.get()); // Future와 마찬가지로 CompletableFuture#get도 blocking방식이다.
        System.out.println("Thread: " + Thread.currentThread().getName());
    }

    /**
     * <pre>
     * CompletableFuture#thenAccept(action: Consumer&lt;T>): CompletableFuture&lt;Void>
     * - 비동기 작업이후 Consumer 콜백실행
     * </pre>
     */
    @Test
    public void thenAccept() throws ExecutionException, InterruptedException {
        CompletableFuture<Void> future = CompletableFuture.supplyAsync(() -> {
            return "Thread: " + Thread.currentThread().getName();
        }).thenAccept(System.out::println);

        future.get();
        System.out.println("Thread: " + Thread.currentThread().getName());
    }

    /**
     * <pre>
     * CompletableFuture#thenApply(fn: Function&lt;T,U>): CompletableFuture&lt;U>
     * - 비동기 작업이후 Function 콜백실행
     * </pre>
     */
    @Test
    public void thenApply() throws ExecutionException, InterruptedException {
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            return "Thread: " + Thread.currentThread().getName();
        });
        CompletableFuture<String> future2 = future.thenApply(String::toUpperCase);
        System.out.println(future.get()); // Thread: ForkJoinPool.commonPool-worker-1
        System.out.println(future2.get()); // THREAD: FORKJOINPOOL.COMMONPOOL-WORKER-1
        System.out.println("Thread: " + Thread.currentThread().getName()); // Thread: Test worker
    }

    /**
     * <pre>
     * CompletableFuture#thenRun(action: Runnable): CompletableFuture&lt;Void>
     * - 비동기 작업이후 단순 Runnable 콜백실행
     * </pre>
     */
    @Test
    public void thenRun() throws ExecutionException, InterruptedException {
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            // 작업(비동기) 수행
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return "Foo";
        });
        future.thenRun(() -> {
            System.out.println("비동기 작업이 완료되었습니다.");
        });

        System.out.println(future.get()); // Foo
    }

    /**
     * <pre>
     * thenCompose(fn:Function&lt;T, CompletionStage&lt;U>>): CompletableFuture&lt;U>
     * - compose는 구성하다는 의미로, thenCompose는 비동기작업 결과물 Future간에 연관 관계가 있는 경우 사용한다.
     * - 이전 비동기 작업의 결과를 이용해서 다음 비동기 작업을 구성해야 할때 사용한다.
     * - CompletionStage는 CompletableFuture의 구현클래스이다.
     *
     * <img src="https://d.pr/i/kYC7xf+" alt="CompletionStage다이어그램">
     * </pre>
     */
    @Test
    void thenCompose() throws ExecutionException, InterruptedException {
        CompletableFuture<String> hello = CompletableFuture.supplyAsync(() -> {
            return "Hello";
        });
        // 아래 두 케이스는 모두 동일하다.
//        CompletableFuture<String> future = hello.thenCompose(this::thenWorld);
        CompletableFuture<String> future = hello.thenCompose((message) -> CompletableFuture.supplyAsync(() -> message + " World")); // js의 고차함수와 비슷하다.
        System.out.println(future.get());
    }

    private CompletableFuture<String> thenWorld(String message) {
        return CompletableFuture.supplyAsync(() -> {
            return message + " World";
        });
    }

    /**
     * <pre>
     * CompletableFuture#thenCombine(other: CompletionStage&lt;U> , fn:BiFunction&lt;T,U,V>): CompletableFuture&lt;V>
     * -두 비동기 작업(A, B)을 합쳐서 다음 비동기 작업(C)가 가능하다.
     * - allOf와 비슷하지만, 이전 비동기 작업 결과를 넘겨받을수 있다.
     * </pre>
     */
    @Test
    void thenCombine() throws ExecutionException, InterruptedException {
        CompletableFuture<String> hello = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return "Hello";
        });

        CompletableFuture<String> world = CompletableFuture.supplyAsync(() -> {
            return "World";
        });

        CompletableFuture<String> future = hello.thenCombine(world, (h, w) -> h + " " + w);
        System.out.println(future.get());
    }

    /**
     * <pre>
     * CompletableFuture.allOf(cfs: CompletableFuture&lt;?>....): CompletableFuture<Void>
     * - 모든 비동기작업이 완료되면 연이어 다른 비동기작업을 처리한다.
     * - 이전 비동기작업의 결과물을 콜백에서 넘겨받을수 없다.
     * </pre>
     */
    @Test
    void allOf() throws ExecutionException, InterruptedException {
        CompletableFuture<String> hello = CompletableFuture.supplyAsync(() -> {
            return "Hello";
        });

        CompletableFuture<String> world = CompletableFuture.supplyAsync(() -> {
            return "World";
        });

        // 예제1
        CompletableFuture.allOf(hello, world).thenRun(() -> {
            System.out.println("모든 작업이 완료되었습니다.");
        });

        // 예제2 : 이전 비동기작업 결과를 allOf로 부터 넘겨받을수 없으므로 미리 선언한 List를 사용한다.
        List<CompletableFuture<String>> futures = List.of(hello, world);
        CompletableFuture<List<String>> result =
                CompletableFuture.allOf(hello, world)
                        .thenApply(v -> {
                            System.out.println(v); // null 이전비동기작업 결과물을 가져올 수 없다.
                            return futures.stream()
                                    .map(CompletableFuture::join)
                                    .collect(Collectors.toList());
                        });
        result.get().forEach(System.out::println);
    }


    /**
     * <pre>
     * CompletableFuture.anyOf(cfs:  CompletableFuture&lt;?>...): CompletableFuture&lt;Object>
     * - 비동기 작업중 먼저 처리된 쪽의 값을 핸들링할 수 있다.
     * </pre>
     */
    @Test
    void anyOf() throws ExecutionException, InterruptedException {
        CompletableFuture<String> hello = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            return "Hello";
        });

        CompletableFuture<String> world = CompletableFuture.supplyAsync(() -> {
            return "World";
        });

        CompletableFuture<Void> future = CompletableFuture.anyOf(hello, world).thenAccept(System.out::println); // World 출력
        future.get();
    }

    /**
     * <pre>
     * CompletableFuture#exceptionally(fn:Function&lt;Throwable,T>): CompletableFuture&lt;T>
     * - 비동기작업중 예외발생했을 경우 분기처리 가능
     * </pre>
     */
    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void exceptionally(boolean doThrow) throws ExecutionException, InterruptedException {
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            if (doThrow) {
                throw new IllegalArgumentException("Invalid Argument");
            }

            return "Thread: " + Thread.currentThread().getName();
        }).exceptionally(e -> {
            return e.getMessage();
        });

        System.out.println(future.get());
        // true : java.lang.IllegalArgumentException: Invalid Argument
        // false : ForkJoinPool.commonPool-worker-1
    }

    /**
     * <pre>
     * CompletableFuture#handle(BiFunction&lt;T, Throwable, U>): CompletableFuture&lt;U>
     * - 정상처리, 예외처리를 한번에 처리하는 콜백을 전달한다.
     * - T: 이전 비동기작업 결과타입
     * - Throwable: 이전 비동기작업에서 던져질수 있는 예외타입
     * - U: 콜백 결과타입
     * </pre>
     */
    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void handle(boolean doThrow) throws ExecutionException, InterruptedException {
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            if (doThrow) {
                throw new IllegalArgumentException("Invalid Argument");
            }

            return "Thread: " + Thread.currentThread().getName();
        }).handle((result, e) -> {
            return e == null
                    ? result
                    : e.getMessage();
        });
        System.out.println(future.get());
        // true: java.lang.IllegalArgumentException: Invalid Argument
        // false: Thread: ForkJoinPool.commonPool-worker-1
    }
}