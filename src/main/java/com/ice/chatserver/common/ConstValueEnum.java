package com.ice.chatserver.common;

public interface ConstValueEnum {
    public static final String FRIEND = "FRIEND";
    public static final String GROUP = "GROUP";
    public static final String VALIDATE = "VALIDATE";
    
    
    public static final Integer USERTYPE = 1; //用户
    public static final Integer GROUPTYPE = 2; //群聊
    
    public static final Integer ACCOUNT_USED = 1; //账号已使用
    public static final Integer ACCOUNT_NOT_USED = 0; //账号未使用
    public static final Integer ACCOUNT_NORMAL = 0; //账号正常
    public static final Integer ACCOUNT_FREEZED = 1; //账号冻结不可用
    public static final Integer ACCOUNT_CANCELED = 2; //账号注销不可用
    
    
    public static final Long INITIAL_NUMBER = 10000000L;//用户code字段初始值
    
    
    public static final char[] cvCodeList = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P',
            'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0'};
    
    public static final String[] nickNameList = {
            "C罗", "马塞洛", "梅西", "拉莫斯", "贝尔", "本泽马", "内马尔", "姆巴佩",
            "莱万多夫斯基", "纳瓦斯", "贝克汉姆", "小罗", "大罗", "德布劳内", "莫德里奇", "哈梅斯", "克罗斯",
            "曼珠机奇", "格列兹曼", "皮克", "瓦拉内", "卡瓦哈尔", "卡塞米罗", "哈维", "布冯", "卡西利亚斯", "埃克森",
            "阿扎尔", "苏亚雷斯", "博格巴", "吉格斯", "卡洛斯", "阿尔维斯", "拉姆", "阿坤罗", "菲力克斯", "迪巴拉",
            "伊布", "佩佩", "库尔图瓦", "奥布拉克", "特尔施特根", "伊瓜因", "欧文", "麦孔", "卡卡", "武磊", "冯潇霆",
            "张琳芃", "郝海东", "张玉宁", "保利尼奥", "卡尔德克", "大摩托", "小摩托", "奥古斯托", "比埃拉", "奥斯卡",
            "胡尔克", "李可", "吴曦", "拉什福德", "范迪克", "萨拉赫", "马内", "菲尔米诺", "阿诺德", "南野托斯",
            "孙兴慜", "孙继海", "卡瓦尼", "迪马利亚", "拉基蒂奇", "阿拉巴", "诺伊尔", "博阿滕", "凯恩", "埃里克森",
            "卢卡库", "戈丁", "桑切斯", "迷你罗"
    };
    
}
