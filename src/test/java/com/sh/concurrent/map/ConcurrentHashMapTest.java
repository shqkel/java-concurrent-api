package com.sh.concurrent.map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ConcurrentHashMapTest {

    @DisplayName("싱글쓰레드 환경에서 Map 순회중 요소 제거하기")
    @Test
    public void test1() throws Exception {
        // given
        Map<String, Integer> map = new HashMap<>();
        map.put("key0", 0);
        map.put("key1", 1);
        map.put("key2", 2);
        // when & then
        assertThrows(ConcurrentModificationException.class, () -> {
            map.forEach((k, v) -> {
                if (k == "key2") {
                    map.remove(k);
                }
            });
        });
    }
    @DisplayName("싱글쓰레드 환경에서 Map 순회중 요소 제거하기 - ConcurentHashMap을 사용하면 ConcurrentModificationException를 던지지 않는다.")
    @Test
    public void test1_2() throws Exception {
        // given
        Map<String, Integer> map = new ConcurrentHashMap<>();
        map.put("key0", 0);
        map.put("key1", 1);
        map.put("key2", 2);
        // when & then
        assertDoesNotThrow(() -> {
            map.forEach((k, v) -> {
                if (k == "key2") {
                    map.remove(k);
                }
            });
        });
    }

    @DisplayName("멀티쓰레드를 통해 성씨별 개수를 세는 기능을 HashMap으로 구현해서 망하는 테스트")
    @Test
    public void test2() throws Exception {
        Map<String, Integer> map = new HashMap<>();
        ExecutorService executorService = Executors.newCachedThreadPool();
        // 여러 스레드에서 값 삽입
        List<String> familyNames = Arrays.asList("김", "김", "최", "이", "강", "남궁", "김", "정", "임", "임");
        for (int i = 0; i < familyNames.size(); i++) {
            final int I = i;
//            executorService.submit(() -> map.put(familyNames.get(I), 1));
            executorService.submit(() -> map.compute(familyNames.get(I), (k, v) -> (v == null ? 0 : v) + 1)); // 해당 key의 value가 존재/존재하지 않는 경우 모두 처리
        }
        // 값 조회
        Set<String> familyNameSet = new HashSet<>(familyNames);
        familyNameSet.forEach((familyName) -> System.out.println(familyName + " : " + map.get(familyName)));
        /*
        김 : 3
        이 : 1
        임 : 1
        강 : 1
        정 : 1
        최 : 1
        남궁 : 1
         */
    }

    @DisplayName("멀티쓰레드를 통해 성씨별 개수를 세는 기능을 ConcurrentHashMap으로 구현하는 테스트")
    @Test
    public void test2_2() throws Exception {
        ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();
        ExecutorService executorService = Executors.newCachedThreadPool();
        // 여러 스레드에서 값 삽입
        List<String> familyNames = Arrays.asList("김", "김", "최", "이", "강", "남궁", "김", "정", "임", "임");
        for (int i = 0; i < familyNames.size(); i++) {
            final int I = i;
//            executorService.submit(() -> map.put(familyNames.get(I), 1));
            executorService.submit(() -> map.compute(familyNames.get(I), (k, v) -> (v == null ? 0 : v) + 1)); // 해당 key의 value가 존재/존재하지 않는 경우 모두 처리
        }
        // 값 조회
        Set<String> familyNameSet = new HashSet<>(familyNames);
        familyNameSet.forEach((familyName) -> System.out.println(familyName + " : " + map.get(familyName)));
        /*
        김 : 3
        이 : 1
        임 : 2
        강 : 1
        정 : 1
        최 : 1
        남궁 : 1
         */
    }
}
