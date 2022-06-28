package com.ice.chatserver.config;

import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.Transport;
import com.corundumstudio.socketio.annotation.SpringAnnotationScanner;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//netty-io配置类（参考https://blog.csdn.net/crazyda/article/details/112395571）
@Configuration
public class SocketIOConfig {
    @Value("${socketio.port}")
    private Integer port;
    
    @Value("${socketio.workCount}")
    private int workCount;
    
    @Value("${socketio.allowCustomRequests}")
    private boolean allowCustomRequests;
    
    @Value("${socketio.upgradeTimeout}")
    private int upgradeTimeout;
    
    @Value("${socketio.pingTimeout}")
    private int pingTimeout;
    
    @Value("${socketio.pingInterval}")
    private int pingInterval;
    
    @Value("${socketio.maxFramePayloadLength}")
    private int maxFramePayloadLength;
    
    @Value("${socketio.maxHttpContentLength}")
    private int maxHttpContentLength;
    
    @Bean("socketIOServer")
    public SocketIOServer socketIOServer() {
        com.corundumstudio.socketio.Configuration config = new com.corundumstudio.socketio.Configuration();
        //设置端口
        config.setPort(port);
        com.corundumstudio.socketio.SocketConfig socketConfig = new com.corundumstudio.socketio.SocketConfig();
        //端口复用
        socketConfig.setReuseAddress(true);
        config.setSocketConfig(socketConfig);
        //设置员工线程数
        config.setWorkerThreads(workCount);
        config.setAllowCustomRequests(allowCustomRequests);
        config.setUpgradeTimeout(upgradeTimeout);
        config.setPingTimeout(pingTimeout);
        config.setPingInterval(pingInterval);
        //设置http内容长度现在
        config.setMaxHttpContentLength(maxHttpContentLength);
        //设置最大http内容程度限制
        config.setMaxFramePayloadLength(maxFramePayloadLength);
        //指定传输协议为WebSocket 这样不用自己处理协议
        config.setTransports(Transport.WEBSOCKET);
        return new SocketIOServer(config);
    }
    
    //开启SocketIOServer注解支持，比如 @OnConnect、@OnEvent
    @Bean
    public SpringAnnotationScanner springAnnotationScanner(SocketIOServer socketServer) {
        return new SpringAnnotationScanner(socketServer);
    }
}
