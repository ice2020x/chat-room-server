package com.ice.chatserver.filter;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SensitiveFilterTest {

    @Autowired
    SensitiveFilter sensitiveFilter;

    @Test
    void filter() {
        String[] filter = sensitiveFilter.filter("操你妈，傻逼，狗");
        for (String s : filter) {
            System.out.println(s);
        }
    }
}