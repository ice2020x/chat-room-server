package com.ice.chatserver.service.Impl;

import com.ice.chatserver.pojo.vo.SearchRequestVo;
import com.ice.chatserver.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceImplTest {

    @Autowired
    private UserService userService;

    @Test
    void searchUser() {

        SearchRequestVo searchRequestVo = new SearchRequestVo();

        searchRequestVo.setSearchContent("伊瓜");
        searchRequestVo.setType("nickname");
        searchRequestVo.setPageIndex(0);
        searchRequestVo.setPageSize(9);
        HashMap<String, Object> stringObjectHashMap = userService.searchUser(searchRequestVo, "61be0e3ae7fd6865cbcd74c7");
        for (String s : stringObjectHashMap.keySet()) {
            System.out.println(stringObjectHashMap.get(s));
        }

    }
}