package com.sparta.junit5practice;

import org.junit.jupiter.api.*;

public class BeforeAfterTest {

    @BeforeEach
    void setUp() {
        System.out.println("각각의 테스트 코드가 실행되기 전에 수행");
    }

    @AfterEach
    void tearDown() {
        System.out.println("각각의 테스트 코드가 실행된 후에 수행");
    }

    @BeforeAll
    static void beforeAll() {
        System.out.println("모든 테스트 코드가 실행되기 전에 수행");
    }

    @AfterAll
    static void afterAll() {
        System.out.println("모든 테스트 코드가 실행된 후에 수행");
    }

    @Test
    public void test1() throws Exception {
        System.out.println("테스트 코드 작성1");
    }
    @Test
    public void test2() throws Exception {
        System.out.println("테스트 코드 작성2");
    }
}
