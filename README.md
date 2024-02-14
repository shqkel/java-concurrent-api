# concurrent api
<details>
<summary>목차</summary>
- [노션 강의자료](#노션-강의자료)
- [저수준 thread 제어](#저수준-thread-제어)
- [고수준 thread 제어 - concurrent api](#고수준-thread-제어---concurrent-api)
	- [Executors](#executors)
		- [@com.sh.concurrent.executors.single](#comshconcurrentexecutorssingle)
		- [@com.sh.concurrent.executors.multi](#comshconcurrentexecutorsmulti)
- [Atomic](#atomic)
	- [@com.sh.concurrent.atomic.lock](#comshconcurrentatomiclock)
	- [@com.sh.concurrent.atomic.sequence](#comshconcurrentatomicsequence)
- [Locks](#locks)
	- [@com.sh.concurrent.locks](#comshconcurrentlocks)
- [Synchronizers](#synchronizers)
	- [Semaphore](#semaphore)
		- [1. Binary Semaphore](#1-binary-semaphore)
			- [@com.sh.concurrent.synchronizers.semaphore.binary.withdraw](#comshconcurrentsynchronizerssemaphorebinarywithdraw)
			- [@com.sh.concurrent.synchronizers.semaphore.biinary.account](#comshconcurrentsynchronizerssemaphorebiinaryaccount)
		- [2. Counting Semaphore](#2-counting-semaphore)
			- [@com.sh.concurrent.synchronizers.semaphore.counting.library](#comshconcurrentsynchronizerssemaphorecountinglibrary)
- [Future](#future)
	- [@com.sh.concurrent.future](#comshconcurrentfuture)
- [Queue](#queue)
	- [@com.sh.concurrent.queue.number](#comshconcurrentqueuenumber)
	- [@com.sh.concurrent.queue.bread](#comshconcurrentqueuebread)
- [Map](#map)
	- [@com.sh.concurrent.map](#comshconcurrentmap)
</details>

## 노션 강의자료
[Concurrent API](https://shqkel.notion.site/Concurrent-API-6667133af3234df9bf569e62a2853297?pvs=4)

## 저수준 thread 제어
####### [@com.sh.thread.ThreadBasicTest](https://github.com/shqkel/java-concurrent-api/tree/master/src/test/java/com/sh/thread)
- 쓰레드객체 생성하는 2가지 방법
- 쓰레드 상태제어

## 고수준 thread 제어 - concurrent api

### Executors
###### [@com.sh.concurrent.executors.single](https://github.com/shqkel/java-concurrent-api/tree/master/src/test/java/com/sh/concurrent/executors/single)
###### [@com.sh.concurrent.executors.multi](https://github.com/shqkel/java-concurrent-api/tree/master/src/test/java/com/sh/concurrent/executors/multi)
####### [@com.sh.concurrent.executors.scheduled](https://github.com/shqkel/java-concurrent-api/tree/master/src/test/java/com/sh/concurrent/executors/scheduled)


### Atomic
###### [@com.sh.concurrent.atomic.lock](https://github.com/shqkel/java-concurrent-api/tree/master/src/test/java/com/sh/concurrent/atomic/lock)
n개의 쓰레드간의 lock을 상호배제적으로 획득하는 예제
###### [@com.sh.concurrent.atomic.sequence](https://github.com/shqkel/java-concurrent-api/tree/master/src/test/java/com/sh/concurrent/atomic/sequence)
n개의 쓰레드간의 중복없이 채번하는 예제


### Locks
###### [@com.sh.concurrent.locks](https://github.com/shqkel/java-concurrent-api/tree/master/src/test/java/com/sh/concurrent/locks)
synchronized아닌 Lock객체를 사용해 임계영역을 설정하는 예제


### Synchronizers

#### Semaphore

##### 1. Binary Semaphore
###### [@com.sh.concurrent.synchronizers.semaphore.binary.withdraw](https://github.com/shqkel/java-concurrent-api/tree/master/src/test/java/com/sh/concurrent/synchronizers/semaphore/binary/withdraw)
출금만 진행하는 간단예제 

###### [@com.sh.concurrent.synchronizers.semaphore.biinary.account](https://github.com/shqkel/java-concurrent-api/tree/master/src/test/java/com/sh/concurrent/synchronizers/semaphore/binary/account)
입출금을 모두 진행하는 예제

##### 2. Counting Semaphore
###### [@com.sh.concurrent.synchronizers.semaphore.counting.library](https://github.com/shqkel/java-concurrent-api/tree/master/src/test/java/com/sh/concurrent/synchronizers/semaphore/counting/library)


### Future
###### [@com.sh.concurrent.future](https://github.com/shqkel/java-concurrent-api/tree/master/src/test/java/com/sh/concurrent/future)
* jdk1.5부터 사용가능한 Future 테스트 예제
* jdk1.8부터 사용가능한 Completable 테스트 예제


### Queue
###### [@com.sh.concurrent.queue.number](https://github.com/shqkel/java-concurrent-api/tree/master/src/test/java/com/sh/concurrent/queue/number)
다양한 Queue구현객체를 통해 Producer-Consumer 모델 테스트 예제
###### [@com.sh.concurrent.queue.bread](https://github.com/shqkel/java-concurrent-api/tree/master/src/test/java/com/sh/concurrent/queue/bread)
다중Producer-다중Consumer 테스트 예제 

### Map
###### [@com.sh.concurrent.map](https://github.com/shqkel/java-concurrent-api/tree/master/src/test/java/com/sh/concurrent/map)
ConcurrentHashMap 테스트 예제
