package com.ice.chatserver.handler;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "tenxunyun.sms")
public class SmsProperties {
    
    private Integer appid;
    
    private String appkey;
    
    private Integer templateId;
    
    private String smsSign;
}
