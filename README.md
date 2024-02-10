# concurrent api

## 저수준 thread 제어
[@com.sh.thread.ThreadBasicTest]()
- 쓰레드객체 생성하는 2가지 방법
- 쓰레드 상태제어

## 고수준 thread 제어 - concurrent api

### Executors
[@com.sh.concurrent.executors.single]()
[@com.sh.concurrent.executors.multi]()
[@com.sh.concurrent.executors.scheduled]()


### Atomic
###### [@com.sh.concurrent.atomic.lock]()
n개의 쓰레드간의 lock을 상호배제적으로 획득하는 예제
###### [@com.sh.concurrent.atomic.sequence]()
n개의 쓰레드간의 중복없이 채번하는 예제


### Locks
###### [@com.sh.concurrent.locks]()
synchronized아닌 Lock객체를 사용해 임계영역을 설정하는 예제


### Synchronizers

#### Semaphore

##### 1. Binary Semaphore
###### [@com.sh.concurrent.synchronizers.semaphore.binary.withdraw]()
출금만 진행하는 간단예제 

###### [@com.sh.concurrent.synchronizers.semaphore.biinary.account]()
입출금을 모두 진행하는 예제

##### 2. Counting Semaphore
###### [@com.sh.concurrent.synchronizers.semaphore.counting.library]()


### Future
###### [@com.sh.concurrent.future]()
* jdk1.5부터 사용가능한 Future 테스트 예제
* jdk1.8부터 사용가능한 Completable 테스트 예제


### Queue
###### [@com.sh.concurrent.queue.number]()
다양한 Queue구현객체를 통해 Producer-Consumer 모델 테스트 예제
###### [@com.sh.concurrent.queue.bread]()
다중Producer-다중Consumer 테스트 예제 

### Map
###### [@com.sh.concurrent.map]()
ConcurrentHashMap 테스트 예제
