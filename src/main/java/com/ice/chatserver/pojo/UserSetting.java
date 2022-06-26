package com.ice.chatserver.pojo;

import java.io.Serializable;

/**
 * 
 * @TableName user_setting
 */
public class UserSetting implements Serializable {
    /**
     * 
     */
    private Integer id;

    /**
     * 
     */
    private Integer userId;

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

    /**
     * 
     */
    public Integer getId() {
        return id;
    }

    /**
     * 
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * 
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * 主题色
     */
    public String getPrimaryColor() {
        return primaryColor;
    }

    /**
     * 主题色
     */
    public void setPrimaryColor(String primaryColor) {
        this.primaryColor = primaryColor;
    }

    /**
     * 背景色
     */
    public String getBackgroundColor() {
        return backgroundColor;
    }

    /**
     * 背景色
     */
    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    /**
     * 置顶群聊
     */
    public String getChatTop() {
        return chatTop;
    }

    /**
     * 置顶群聊
     */
    public void setChatTop(String chatTop) {
        this.chatTop = chatTop;
    }

    /**
     * 公开状态: 1-公开 2-不公开
     */
    public Integer getPublicStatus() {
        return publicStatus;
    }

    /**
     * 公开状态: 1-公开 2-不公开
     */
    public void setPublicStatus(Integer publicStatus) {
        this.publicStatus = publicStatus;
    }

    /**
     * 通知状态 1-通知 2-不通知
     */
    public Integer getNotifyStatus() {
        return notifyStatus;
    }

    /**
     * 通知状态 1-通知 2-不通知
     */
    public void setNotifyStatus(Integer notifyStatus) {
        this.notifyStatus = notifyStatus;
    }

    /**
     * 音量大小
     */
    public Integer getVolume() {
        return volume;
    }

    /**
     * 音量大小
     */
    public void setVolume(Integer volume) {
        this.volume = volume;
    }

    /**
     * 消息通知 1-启用 2-禁用
     */
    public Integer getMessageNotify() {
        return messageNotify;
    }

    /**
     * 消息通知 1-启用 2-禁用
     */
    public void setMessageNotify(Integer messageNotify) {
        this.messageNotify = messageNotify;
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        UserSetting other = (UserSetting) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
            && (this.getPrimaryColor() == null ? other.getPrimaryColor() == null : this.getPrimaryColor().equals(other.getPrimaryColor()))
            && (this.getBackgroundColor() == null ? other.getBackgroundColor() == null : this.getBackgroundColor().equals(other.getBackgroundColor()))
            && (this.getChatTop() == null ? other.getChatTop() == null : this.getChatTop().equals(other.getChatTop()))
            && (this.getPublicStatus() == null ? other.getPublicStatus() == null : this.getPublicStatus().equals(other.getPublicStatus()))
            && (this.getNotifyStatus() == null ? other.getNotifyStatus() == null : this.getNotifyStatus().equals(other.getNotifyStatus()))
            && (this.getVolume() == null ? other.getVolume() == null : this.getVolume().equals(other.getVolume()))
            && (this.getMessageNotify() == null ? other.getMessageNotify() == null : this.getMessageNotify().equals(other.getMessageNotify()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getPrimaryColor() == null) ? 0 : getPrimaryColor().hashCode());
        result = prime * result + ((getBackgroundColor() == null) ? 0 : getBackgroundColor().hashCode());
        result = prime * result + ((getChatTop() == null) ? 0 : getChatTop().hashCode());
        result = prime * result + ((getPublicStatus() == null) ? 0 : getPublicStatus().hashCode());
        result = prime * result + ((getNotifyStatus() == null) ? 0 : getNotifyStatus().hashCode());
        result = prime * result + ((getVolume() == null) ? 0 : getVolume().hashCode());
        result = prime * result + ((getMessageNotify() == null) ? 0 : getMessageNotify().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", userId=").append(userId);
        sb.append(", primaryColor=").append(primaryColor);
        sb.append(", backgroundColor=").append(backgroundColor);
        sb.append(", chatTop=").append(chatTop);
        sb.append(", publicStatus=").append(publicStatus);
        sb.append(", notifyStatus=").append(notifyStatus);
        sb.append(", volume=").append(volume);
        sb.append(", messageNotify=").append(messageNotify);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}