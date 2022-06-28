package com.ice.chatserver.service.impl;


import com.alibaba.fastjson.JSONException;
import com.github.qcloudsms.SmsSingleSender;
import com.github.qcloudsms.SmsSingleSenderResult;
import com.github.qcloudsms.httpclient.HTTPException;
import com.ice.chatserver.common.R;
import com.ice.chatserver.common.ResultEnum;
import com.ice.chatserver.handler.SmsProperties;
import com.ice.chatserver.service.MsmService;
import com.ice.chatserver.utils.FormUtils;
import com.ice.chatserver.utils.RandomUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Service
public class MsmServiceImpl implements MsmService {
    @Autowired
    SmsProperties smsProperties;
    
    @Autowired
    RedisTemplate<String, String> redisTemplate;
    
    @Override
    public R sendMsg(HttpServletRequest request, String phone) {
        if (ObjectUtils.isEmpty(phone)) {
            return R.error().resultEnum(ResultEnum.PHONE_EMPTY);
        }
        else if (!FormUtils.isMobile(phone)) {
            return R.error().resultEnum(ResultEnum.LOGIN_PHONE_ERROR);
        }
        return sendSMS(request, phone);
    }
    
    public R sendSMS(HttpServletRequest request, String phoneNumber) {
        // 短信应用SDK AppID  1400开头
        int appid = smsProperties.getAppid();
        // 短信应用SDK AppKey
        String appkey = smsProperties.getAppkey();
        // 短信模板ID，需要在短信应用中申请
        int templateId = smsProperties.getTemplateId();
        
        // 签名，使用的是签名内容，而不是签名ID
        String smsSign = smsProperties.getSmsSign();
        //随机生成六位验证码的工具类
        String code = RandomUtil.getSixBitRandom();
        redisTemplate.opsForValue().set(phoneNumber, code, 5, TimeUnit.MINUTES);
        System.out.println("验证码：" + code);
        try {
            //参数，一定要对应短信模板中的参数顺序和个数，
            String[] params = {code, "3"};
            //创建ssender对象
            SmsSingleSender ssender = new SmsSingleSender(appid, appkey);
            //发送
            SmsSingleSenderResult result = ssender.sendWithParam("86", phoneNumber, templateId, params, smsSign, "", "");
            if (result.result != 0) {
                return R.error().resultEnum(ResultEnum.SMS_SEND_ERROR);
            }
        } catch (HTTPException e) {
            // HTTP响应码错误
            e.printStackTrace();
        } catch (JSONException e) {
            // json解析错误
            e.printStackTrace();
        } catch (IOException e) {
            // 网络IO错误
            e.printStackTrace();
        } catch (Exception e) {
            // 网络IO错误
            e.printStackTrace();
        }
        return R.ok().message("短信发送成功");
    }
}