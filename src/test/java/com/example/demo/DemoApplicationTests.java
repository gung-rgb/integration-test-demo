package com.example.demo;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class DemoApplicationTests {

    static IntegrationTestRunner RUNNER = new IntegrationTestRunner();

    @BeforeAll
    static void beforeAll() throws Exception {
        RUNNER.beforeAll();
    }

    @AfterAll
    static void afterAll() throws Exception {
        RUNNER.afterAll();
    }

    @Test
    void runTests() throws Exception {
        RUNNER.runTests();
    }

}
