package com.ice.chatserver.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* @author ice2020x
* @Date: 2021/12/18
* @Description: 接受修改备注信息的类
**/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ModifyFriendBeiZhuRequestVo {
    private String friendId;
    private String friendBeiZhuName;
}
