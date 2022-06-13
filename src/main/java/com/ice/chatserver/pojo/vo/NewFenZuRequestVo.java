package com.ice.chatserver.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* @author ice2020x
* @Date: 2021/12/18
* @Description: 接受添加分组的类
**/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewFenZuRequestVo {
    private String fenZuName;
    private String userId;
}
