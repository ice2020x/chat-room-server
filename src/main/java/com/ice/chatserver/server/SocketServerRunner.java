package com.ice.chatserver.server;

/**
 * @author ice2020x
 * @date 2021-12-19 16:33
 * @description:
 */

import com.corundumstudio.socketio.SocketIOServer;
import com.ice.chatserver.pojo.SuperUser;
import com.ice.chatserver.pojo.SystemUser;
import com.ice.chatserver.service.SuperUserService;
import com.ice.chatserver.service.SystemService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
* @author ice2020x
* @Date: 2021/12/19
* @Description: netty服务器启动类
**/
@Component
@Slf4j
public class SocketServerRunner implements CommandLineRunner {

    @Autowired
    SuperUserService superUserService;

    @Resource
    private SocketIOServer socketIOServer;

    @Autowired
    SystemService systemService;
    @Override
    public void run(String... args) throws Exception {
        log.info("<-------------初始化一个系统用户------------------>");
        initSystem();
        log.info("<-------------初始化一个管理员用户----------------->");
        initSuper();
        log.info("<----------------服务启动中---------------------->");
        socketIOServer.start();
        log.info("<----------------服务启动成功-------------------->");
    }

    private void initSystem() {
        SystemUser systemUser = new SystemUser();
        systemUser.setCode("100000001");
        systemUser.setNickname("系统用户");
        systemUser.setStatus(1);
        systemService.notExistThenAddSystemUser(systemUser);
    }


    private void initSuper() {
        SuperUser superUser = new SuperUser();
        superUser.setAccount("admin");
        superUser.setPassword("admin");
        superUser.setRole(0);
        superUserService.notExistThenAddSuperUser(superUser);
    }


}
