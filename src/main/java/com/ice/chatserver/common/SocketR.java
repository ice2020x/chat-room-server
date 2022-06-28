package com.ice.chatserver.common;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class SocketR extends R {
    private String eventCode;
    @Override
    public SocketR data(String key, Object value) {
        super.data(key, value);
        return this;
    }
    
    public SocketR event(String eventCode) {
        this.eventCode = eventCode;
        return this;
    }
    
    public static SocketR ok() {
        SocketR r = new SocketR();
        r.setSuccess(true);
        r.resultEnum(ResultEnum.SUCCESS);
        return r;
    }
    
    public static SocketR error() {
        SocketR r = new SocketR();
        r.setSuccess(false);
        r.resultEnum(ResultEnum.ERROR);
        return r;
    }
}
