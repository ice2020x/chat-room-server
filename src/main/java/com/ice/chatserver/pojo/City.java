package com.ice.chatserver.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//城市信息实体类
@Data
@NoArgsConstructor
@AllArgsConstructor
public class City {
    private String name = "广州市";
    private String code = "510000";
}