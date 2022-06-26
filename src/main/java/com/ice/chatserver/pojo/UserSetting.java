package com.ice.chatserver.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;

/**
 * @TableName user_setting
 */
@Data
public class UserSetting implements Serializable {
    /**
     *
     */
    private Integer id;

    /**
     *
     */
    private String userId;

    /**
     * 主题色
     */
    private String primaryColor;

    /**
     * 背景色
     */
    private String backgroundColor;

    /**
     * 置顶群聊
     */
    private String chatTop;

    /**
     * 公开状态: 1-公开 2-不公开
     */
    private Integer publicStatus;

    /**
     * 通知状态 1-通知 2-不通知
     */
    private Integer notifyStatus;

    /**
     * 音量大小
     */
    private Integer volume;

    /**
     * 消息通知 1-启用 2-禁用
     */
    private Integer messageNotify;
    

    private static final long serialVersionUID = 1L;


}