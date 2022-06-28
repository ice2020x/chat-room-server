package com.ice.chatserver.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

//其实可以前端直接验证的
public class FormUtils {
    private static final Pattern P;
    
    static {
        P = Pattern.compile("^[1][3,4,5,7,8,9][0-9]{9}$");
    }
    
    //手机的一个正则表达式一个验证
    public static boolean isMobile(String str) {
        Matcher m = P.matcher(str);
        return m.matches();
    }
}