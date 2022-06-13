package com.ice.chatserver.pojo.vo;

import com.ice.chatserver.pojo.config.BrowserSetting;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequestVo {
    private String username;
    private String password;
    private String cvCode;
}
