package com.ice.chatserver.pojo.config;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
* @author ice2020x
* @Date: 2021/12/17
* @Description: 浏览器设置实体
**/
@Data
@NoArgsConstructor
public class BrowserSetting {
    private String browser;
    private String country;
    private String ip;
    private String os;
}
