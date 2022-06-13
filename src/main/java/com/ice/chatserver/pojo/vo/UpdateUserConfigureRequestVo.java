package com.ice.chatserver.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserConfigureRequestVo {
    private Double opacity = 0.75D; //聊天框透明度
    private Integer blur = 10; //模糊度
    private String notifySound = "default"; //提示音
    private String bgColor = "#fff"; //背景颜色
}
