package com.ice.chatserver.service.impl;

import com.ice.chatserver.common.ConstValueEnum;
import com.ice.chatserver.common.R;
import com.ice.chatserver.common.ResultEnum;
import com.ice.chatserver.dao.AccountPoolDao;
import com.ice.chatserver.dao.GoodFriendDao;
import com.ice.chatserver.dao.UserDao;
import com.ice.chatserver.pojo.AccountPool;
import com.ice.chatserver.pojo.User;
import com.ice.chatserver.pojo.config.JwtInfo;
import com.ice.chatserver.pojo.vo.*;
import com.ice.chatserver.service.UserService;
import com.ice.chatserver.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

//用户的逻辑处理
@Slf4j
@Service
public class UserServiceImpl implements UserService {
    @Resource
    private UserDao userDao;
    
    @Resource
    private MongoTemplate mongoTemplate;
    
    @Resource
    private AccountPoolDao accountPoolDao;
    
    @Resource
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    
    @Resource
    private RedisTemplate<String, String> redisTemplate;
    @Resource
    GoodFriendDao goodFriendDao;
    
    //注册账号实现
    @Override
    public R register(RegisterRequestVo registerVo) {
        String code = registerVo.getCode();
        String password = registerVo.getPassword();
        String mobile = registerVo.getMobile();
        if (ObjectUtils.isEmpty(mobile) || !FormUtils.isMobile(mobile)) {
            return R.error().resultEnum(ResultEnum.LOGIN_PHONE_ERROR);
        }
        String checkCode = String.valueOf(redisTemplate.opsForValue().get(mobile));
        System.out.println(checkCode);
        if (!code.equals(checkCode)) {
            return R.error().resultEnum(ResultEnum.CODE_ERROR);
        }
        User existUser = userDao.findUserByUsername(registerVo.getMobile());
        if (existUser != null) {
            return R.error().resultEnum(ResultEnum.USER_HAS_EXIST);
        }
        else {
            //生成用户唯一标识账号code
            AccountPool accountPool = new AccountPool();
            //类型：用户
            accountPool.setType(ConstValueEnum.USERTYPE);
            //已使用状态
            accountPool.setStatus(ConstValueEnum.ACCOUNT_USED);
            accountPoolDao.save(accountPool);
            //密码加密
            String newPass = bCryptPasswordEncoder.encode(registerVo.getPassword());
            User user = new User();
            user.setUsername(registerVo.getMobile());
            user.setPassword(newPass);
            user.setCode(String.valueOf(accountPool.getCode() + ConstValueEnum.INITIAL_NUMBER));
            //设置默认头像
            user.setPhoto("https://chat-ice.oss-cn-beijing.aliyuncs.com/chat/9138f18c-1723-4d97-b027-c92c113bd707.jpg");
            user.setNickname(ChatServerUtil.randomNickname());
            userDao.save(user);
            if (user.getUserId() != null) {
                return R.ok().resultEnum(ResultEnum.REGISTER_SUCCESS).data("userCode", user.getCode());
            }
            else {
                return R.error().resultEnum(ResultEnum.REGISTER_FAILED);
            }
        }
    }
    
    //获取图像验证码
    @Override
    public R getVerificationCode(HttpServletRequest request, HttpServletResponse response) {
        try {
            int width = 200;
            int height = 69;
            BufferedImage verifyImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            //生成对应宽高的初始图片
            String randomText = VerifyCode.drawRandomText(width, height, verifyImg);
            String kaptchaOwner = ChatServerUtil.generateUUID();
            Cookie cookie = new Cookie("kaptchaOwner", kaptchaOwner);
            // 验证码有效时间为 60 s，注意多加8个小时，相差8个时区
            cookie.setMaxAge(1000 + 8 * 60 * 60);
            // 整个项目都有效，注意路径设置
            cookie.setPath("/");
            // 发送给客户端
            response.addCookie(cookie);
            // 将验证码存入Redis
            String redisKey = RedisKeyUtil.getKaptchaKey(kaptchaOwner);
            
            // redis 有效时间为 1000s
            redisTemplate.opsForValue().set(redisKey, randomText, 1000, TimeUnit.SECONDS);
            System.out.println("redisKey:" + redisKey + ",code:" + randomText);
            response.setContentType("image/jpeg");
            OutputStream os = response.getOutputStream();
            //输出图片流
            ImageIO.write(verifyImg, "png", os);
            os.flush();
            os.close();//关闭流
            
        } catch (IOException e) {
            //log.info("No converter for [class com.ice.chatserver.common.R] with preset Content-Type 'image/jpeg'");
        }
        return R.ok().message("验证码获取成功");
    }
    
    //获取用户信息
    @Override
    public R getUserInfo(String userId, HttpServletRequest request) {
        User user = userDao.findById(new ObjectId(userId)).orElse(null);
        final String currentUserId = getUserId(request);
        if (user != null) {
            if (StringUtils.isNoneBlank(currentUserId)) {
                final long count = mongoTemplate.count(new Query().addCriteria(Criteria.where("userM").is(currentUserId).and("userY").is(userId)), "goodfriends");
                user.setMyFriend(count > 0);
                final long count2 = mongoTemplate.count(new Query().addCriteria(Criteria.where("userY").is(currentUserId).and("userM").is(userId)), "goodfriends");
                user.setMyFriend(count2 > 0);
            }
            return R.ok().data("user", user).message("获取用户详细信息成功");
        }
        else {
            return R.error().message("获取用户详细信息失败");
        }
    }
    
    //获取当前登录的用户uid
    private String getUserId(HttpServletRequest request) {
        JwtInfo infoByJwtToke = JwtUtils.getInfoByJwtToken(request);
        if (org.apache.commons.lang3.ObjectUtils.isEmpty(infoByJwtToke)) {
            return "";
        }
        final String userId = infoByJwtToke.getUserId();
        if (StringUtils.isBlank(userId)) {
            return "";
        }
        return userId;
    }
    
    //添加新分组
    @Override
    public R addNewFenZu(NewFenZuRequestVo requestVo) {
        try {
            User user = userDao.findById(new ObjectId(requestVo.getUserId())).orElse(null);
            if (user == null) {
                return R.error().message("没有该用户！");
            }
            Map<String, ArrayList<String>> friendFenZu = user.getFriendFenZu();
            if (!friendFenZu.containsKey(requestVo.getFenZuName())) {
                friendFenZu.put(requestVo.getFenZuName(), new ArrayList<>());
                Update update = new Update();
                update.set("friendFenZu", friendFenZu);
                Query query = new Query();
                query.addCriteria(Criteria.where("_id").is(new ObjectId(requestVo.getUserId())));
                mongoTemplate.findAndModify(query, update, User.class);
            }
            return R.ok().message("添加分组成功");
        } catch (RuntimeException e) {
            e.printStackTrace();
            return R.error().message("添加分组失败");
        }
    }
    
    //修改好友备注
    @Override
    public R modifyFriendBeiZhu(ModifyFriendBeiZhuRequestVo requestVo, String userId) {
        User userInfo = userDao.findById(new ObjectId(userId)).orElse(null);
        if (userInfo == null) {
            return R.error().message("没有该用户");
        }
        Map<String, String> friendBeiZhuMap = userInfo.getFriendBeiZhu();
        friendBeiZhuMap.put(requestVo.getFriendId(), requestVo.getFriendBeiZhuName());
        //更新用户信息，查询条件
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(new ObjectId(userId)));
        Update update = new Update();
        update.set("friendBeiZhu", friendBeiZhuMap);
        mongoTemplate.findAndModify(query, update, User.class);
        return R.ok().message("修改备注成功");
    }
    
    //修改好友分组
    @Override
    public R modifyFriendFenZu(ModifyFriendFenZuRequestVo requestVo) {
        User userInfo = userDao.findById(new ObjectId(requestVo.getUserId())).orElse(null);
        if (userInfo == null) {
            return R.error().message("没有该用户");
        }
        System.out.println("requestVo" + requestVo.getNewFenZuName());
        boolean flag = false;
        Map<String, ArrayList<String>> friendFenZu = userInfo.getFriendFenZu();
        for (Map.Entry<String, ArrayList<String>> entry : friendFenZu.entrySet()) {
            Iterator<String> iterator = entry.getValue().iterator();
            while (iterator.hasNext()) {
                if (iterator.next().equals(requestVo.getFriendId())) {
                    if (!entry.getKey().equals(requestVo.getNewFenZuName())) {
                        iterator.remove();
                        flag = true;
                        break;
                    }
                }
            }
            if (flag) {
                break;
            }
        }
        
        String newFenZuName = requestVo.getNewFenZuName();
        ArrayList<String> strings = friendFenZu.get(newFenZuName);
        if (ObjectUtils.isEmpty(strings)) {
            strings = new ArrayList<String>();
        }
        strings.add(requestVo.getFriendId());
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(new ObjectId(requestVo.getUserId())));
        Update update = new Update();
        update.set("friendFenZu", friendFenZu);
        mongoTemplate.findAndModify(query, update, User.class);
        return R.ok().message("修改好友分组成功");
    }
    
    //删除分组
    @Override
    public R deleteFenZu(DelFenZuRequestVo requestVo) {
        User userInfo = userDao.findById(new ObjectId(requestVo.getUserId())).orElse(null);
        if (userInfo == null) {
            return R.error().message("没有该用户");
        }
        Map<String, ArrayList<String>> friendFenZuMap = userInfo.getFriendFenZu();
        friendFenZuMap.remove(requestVo.getFenZuName());
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(new ObjectId(requestVo.getUserId())));
        Update update = new Update();
        update.set("friendFenZu", friendFenZuMap);
        mongoTemplate.findAndModify(query, update, User.class);
        return R.ok().message("删除分组成功");
    }
    
    //更新分组
    @Override
    public R editFenZu(EditFenZuRequestVo requestVo) {
        User userInfo = userDao.findById(new ObjectId(requestVo.getUserId())).orElse(null);
        if (userInfo == null) {
            return R.error().message("没有该用户");
        }
        Map<String, ArrayList<String>> friendFenZuMap = userInfo.getFriendFenZu();
        ArrayList<String> oldFenZuUsers = friendFenZuMap.get(requestVo.getOldFenZu());
        friendFenZuMap.remove(requestVo.getOldFenZu());
        friendFenZuMap.put(requestVo.getNewFenZu(), oldFenZuUsers);
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(new ObjectId(requestVo.getUserId())));
        Update update = new Update();
        update.set("friendFenZu", friendFenZuMap);
        mongoTemplate.findAndModify(query, update, User.class);
        return R.ok().message("更新分组名成功");
    }
    
    //统计在线时长
    @Override
    public void updateOnlineTime(long onlineTime, String uid) {
        Update update = new Update();
        update.set("onlineTime", onlineTime);
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(new ObjectId(uid)));
        mongoTemplate.upsert(query, update, User.class);
    }
    
    //更新用户的配置
    @Override
    public boolean updateUserConfigure(UpdateUserConfigureRequestVo requestVo, String uid) {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(new ObjectId(uid)));
        Update update = new Update();
        update.set("opacity", requestVo.getOpacity())
                .set("blur", requestVo.getBlur())
                .set("notifySound", requestVo.getNotifySound())
                .set("bgColor", requestVo.getBgColor());
        return mongoTemplate.upsert(query, update, User.class).getModifiedCount() > 0;
    }
    
    //修改用户个人信息
    @Override
    public Map<String, Object> updateUserInfo(UpdateUserInfoRequestVo requestVo) {
        Map<String, Object> map = new HashMap<>();
        Integer code = null;
        String msg = null;
        Update update = new Update();
        boolean flag = false;
        if (requestVo.getField().equals("sex")) {
            String sexStr = requestVo.getValue().toString();
            if (!ChatServerUtil.isNumeric(sexStr)) {
                code = ResultEnum.ERROR_SETTING_GENDER.getCode();
                msg = ResultEnum.ERROR_SETTING_GENDER.getMessage();
                flag = true;
            }
            else {
                Integer sex = Integer.valueOf(sexStr);
                if (sex != 0 && sex != 1 && sex != 3) {
                    code = ResultEnum.ERROR_SETTING_GENDER.getCode();
                    msg = ResultEnum.ERROR_SETTING_GENDER.getMessage();
                    flag = true;
                }
                else {
                    update.set(requestVo.getField(), sex);
                }
            }
        }
        else if (requestVo.getField().equals("age")) {
            String age = requestVo.getValue().toString();
            if (!ChatServerUtil.isNumeric(age)) {
                code = ResultEnum.ERROR_SETTING_AGE.getCode();
                msg = ResultEnum.ERROR_SETTING_AGE.getMessage();
                flag = true;
            }
            else {
                update.set(requestVo.getField(), Integer.valueOf(age));
            }
        }
        else if (requestVo.getField().equals("email")) {
            String email = (String) requestVo.getValue();
            if (!ChatServerUtil.isEmail(email)) {
                code = ResultEnum.ERROR_SETTING_EMAIL.getCode();
                msg = ResultEnum.ERROR_SETTING_EMAIL.getMessage();
                flag = true;
            }
            else {
                update.set(requestVo.getField(), email);
            }
        }
        else {
            update.set(requestVo.getField(), requestVo.getValue());
        }
        if (!flag) {
            Query query = new Query();
            query.addCriteria(Criteria.where("_id").is(new ObjectId(requestVo.getUserId())));
            mongoTemplate.upsert(query, update, User.class);
        }
        else {
            map.put("code", code);
            map.put("msg", msg);
        }
        return map;
    }
    
    //修改密码
    @Override
    public Map<String, Object> updateUserPwd(UpdateUserPwdRequestVo requestVo) {
        Map<String, Object> map = new HashMap<>();
        Integer code = null;
        String msg = null;
        //两次密码不一致
        if (!requestVo.getReNewPwd().equals(requestVo.getNewPwd())) {
            code = ResultEnum.INCORRECT_PASSWORD_TWICE.getCode();
            msg = ResultEnum.INCORRECT_PASSWORD_TWICE.getMessage();
        }
        else {
            User userInfo = userDao.findById(new ObjectId(requestVo.getUserId())).orElse(null);
            assert userInfo != null;
            //使用matches方法（参数1：不经过加密的密码，参数2：已加密密码）
            if (!bCryptPasswordEncoder.matches(requestVo.getOldPwd(), userInfo.getPassword())) {
                code = ResultEnum.OLD_PASSWORD_ERROR.getCode();
                msg = ResultEnum.OLD_PASSWORD_ERROR.getMessage();
            }
            else {
                String bCryptNewPwd = bCryptPasswordEncoder.encode(requestVo.getNewPwd());
                //更新旧密码
                Update update = new Update();
                update.set("password", bCryptNewPwd);
                Query query = new Query();
                query.addCriteria(Criteria.where("_id").is(new ObjectId(requestVo.getUserId())));
                mongoTemplate.upsert(query, update, User.class);
                code = ResultEnum.SUCCESS.getCode();
                msg = "更新成功，请牢记你的新密码";
            }
        }
        map.put("code", code);
        map.put("msg", msg);
        return map;
    }
    
    // 获取全部用户
    @Override
    public List<User> getUserList() {
        return userDao.findAll();
    }
    
    //根据注册时间获取用户
    @Override
    public List<User> getUsersBySignUpTime(String lt, String rt) {
        Query query = new Query();
        query.addCriteria(Criteria.where("signUpTime").gte(DateUtil.parseDate(lt, DateUtil.yyyy_MM))
                .lte(DateUtil.parseDate(rt, DateUtil.yyyy_MM)));
        return mongoTemplate.find(query, User.class);
    }
    
    //修改用户状态
    @Override
    public void changeUserStatus(String uid, Integer status) {
        Update update = new Update();
        update.set("status", status);
        Query query = new Query();
        query.addCriteria(Criteria.where("uid").is(uid));
        mongoTemplate.findAndModify(query, update, User.class);
    }
    
    //搜索用户
    @Override
    public HashMap<String, Object> searchUser(SearchRequestVo requestVo, String uid) {
        Query query = new Query();
        query.addCriteria(
                        Criteria.where(requestVo.getType()).regex(Pattern.compile("^.*" + requestVo.getSearchContent() + ".*$", Pattern.CASE_INSENSITIVE))
                                .and("uid").ne(uid)
                ).with(Sort.by(Sort.Direction.DESC, "_id"))
                .skip((long) (requestVo.getPageIndex() - 1) * requestVo.getPageSize())
                .limit(requestVo.getPageSize());
        Query query1 = new Query();
        
        query1.addCriteria(
                Criteria.where(requestVo.getType()).regex(Pattern.compile("^.*" + requestVo.getSearchContent() + ".*$", Pattern.CASE_INSENSITIVE))
                        .and("uid").ne(uid)
        );
        long count = mongoTemplate.count(query1, User.class);
        HashMap<String, Object> map = new HashMap<String, Object>();
        List<User> users = mongoTemplate.find(query, User.class);
        map.put("total", count);
        map.put("list", users);
        return map;
    }
}