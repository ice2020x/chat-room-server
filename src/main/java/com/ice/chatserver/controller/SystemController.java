package com.ice.chatserver.controller;

import com.ice.chatserver.common.R;
import com.ice.chatserver.filter.SensitiveFilter;
import com.ice.chatserver.pojo.FeedBack;
import com.ice.chatserver.pojo.FileSystem;
import com.ice.chatserver.pojo.SensitiveMessage;
import com.ice.chatserver.pojo.User;
import com.ice.chatserver.pojo.vo.FeedBackResultVo;
import com.ice.chatserver.pojo.vo.SensitiveMessageResultVo;
import com.ice.chatserver.pojo.vo.SystemUserResponseVo;
import com.ice.chatserver.service.FileService;
import com.ice.chatserver.service.OnlineUserService;
import com.ice.chatserver.service.SystemService;
import com.ice.chatserver.service.UserService;
import com.ice.chatserver.utils.SystemUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

//系统模块控制器
@RequestMapping("/system")
@RestController
public class SystemController {
    @Autowired
    FileService fileService;
    
    @Autowired
    UserService userService;
    
    @Autowired
    SystemService sysService;
    
    @Autowired
    OnlineUserService onlineUserService;
    
    @Autowired
    SensitiveFilter sensitiveFilter;
    
    //文件上传
    @PostMapping("/upload")
    public R upload(@RequestParam("file") MultipartFile file, String model) {
        if (file.isEmpty()) {
            return R.error().message("文件上传失败");
        }
        InputStream inputStream = null;
        try {
            inputStream = file.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String originalFilename = file.getOriginalFilename();
        FileSystem fileSystem = new FileSystem();
        fileSystem.setFileSize(file.getSize());
        FileSystem upload = fileService.upload(inputStream, model, originalFilename, fileSystem);
        return R.ok().message("文件上传成功").data("file", upload);
    }
    
    //获取头像列表
    @GetMapping("/getFaceImages")
    @ResponseBody
    public R getFaceImages() {
        ArrayList<String> files = new ArrayList<>();
        for (int i = 1; i <= 22; i++) {
            files.add("face" + i + ".jpg");
        }
        files.add("ronaldo1.jpg");
        return R.ok().data("files", files);
    }
    
    //获取系统用户列表
    @GetMapping("/getSysUsers")
    @ResponseBody
    public R getSysUsers() {
        List<SystemUserResponseVo> sysUsers = sysService.getSysUsers();
        // System.out.println("系统用户有：" + sysUsers);
        return R.ok().data("sysUsers", sysUsers);
    }
    
    //用户反馈
    @PostMapping("/addFeedBack")
    @ResponseBody
    public R addFeedBack(@RequestBody FeedBack feedBack) {
        System.out.println("反馈请求参数为：" + feedBack);
        sysService.addFeedBack(feedBack);
        return R.ok().message("感谢您的反馈！");
    }
    
    //消息过滤
    @PostMapping("/filterMessage")
    @ResponseBody
    public R filterMessage(@RequestBody SensitiveMessage sensitiveMessage) {
        String[] res = sensitiveFilter.filter(sensitiveMessage.getMessage());
        String filterContent = "";
        if (res != null) {
            filterContent = res[0];
            if ("1".equals(res[1])) {
                //判断出敏感词，插入到敏感词表中
                sysService.addSensitiveMessage(sensitiveMessage);
            }
        }
        return R.ok().data("message", filterContent);
    }
    
    //获取系统CPU、内存使用率
    @GetMapping("/sysSituation")
    @ResponseBody
    public R getSysInfo() {
        double cpuUsage = SystemUtil.getSystemCpuLoad();
        double memUsage = SystemUtil.getSystemMemLoad();
        return R.ok().data("cpuUsage", cpuUsage).data("memUsage", memUsage);
    }
    
    //获取所有用户
    @GetMapping("/getAllUser")
    @ResponseBody
    public R getAllUser() {
        List<User> userList = userService.getUserList();
        return R.ok().data("userList", userList);
    }
    
    //根据注册时间获取用户
    @GetMapping("/getUsersBySignUpTime")
    @ResponseBody
    public R getUsersBySignUpTime(String lt, String rt) {
        List<User> userList = userService.getUsersBySignUpTime(lt, rt);
        return R.ok().data("userList", userList);
    }
    
    //获取当前在线用户的个数
    @GetMapping("/countOnlineUser")
    @ResponseBody
    public R getOnlineUserNums() {
        int onlineUserCount = onlineUserService.countOnlineUser();
        return R.ok().data("onlineUserCount", onlineUserCount);
    }
    
    //更改用户状态
    @GetMapping("/changeUserStatus")
    @ResponseBody
    public R changeUserStatus(String uid, Integer status) {
        userService.changeUserStatus(uid, status);
        return R.ok();
    }
    
    //获取敏感消息列表
    @GetMapping("/getSensitiveMessageList")
    @ResponseBody
    public R getSensitiveMessageList() {
        List<SensitiveMessageResultVo> sensitiveMessageList = sysService.getSensitiveMessageList();
        return R.ok().data("sensitiveMessageList", sensitiveMessageList);
    }
    
    //获取反馈记录列表
    @GetMapping("/getFeedbackList")
    @ResponseBody
    public R getFeedbackList() {
        List<FeedBackResultVo> feedbackList = sysService.getFeedbackList();
        return R.ok().data("feedbackList", feedbackList);
    }
}
