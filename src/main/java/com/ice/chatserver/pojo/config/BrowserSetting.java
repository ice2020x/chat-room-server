package com.ice.chatserver.pojo.config;

import lombok.Data;
import lombok.NoArgsConstructor;

//浏览器设置实体
@Data
@NoArgsConstructor
public class BrowserSetting {
    private String browser;
    private String country;
    private String ip;
    private String os;
}
