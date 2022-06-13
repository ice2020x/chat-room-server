package com.ice.chatserver.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ice2020x
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class IsReadMessageRequestVo {
    private String roomId;
    private String userId;
}
