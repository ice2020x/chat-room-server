package com.ice.chatserver.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ice.chatserver.pojo.UserSetting;
import com.ice.chatserver.service.UserSettingService;
import com.ice.chatserver.mapper.UserSettingMapper;
import org.springframework.stereotype.Service;

/**
*
*/
@Service
public class UserSettingServiceImpl extends ServiceImpl<UserSettingMapper, UserSetting>
implements UserSettingService{

}
