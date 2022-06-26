package com.ice.chatserver.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.ice.chatserver.mapper")
public class MybatisConfig {
}
