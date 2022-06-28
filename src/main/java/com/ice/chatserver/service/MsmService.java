package com.ice.chatserver.service;

import com.ice.chatserver.common.R;

import javax.servlet.http.HttpServletRequest;

public interface MsmService {
    R sendMsg(HttpServletRequest request, String phone);
}