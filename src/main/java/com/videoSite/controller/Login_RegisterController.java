package com.videoSite.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.videoSite.common.dto.SignUpDto;
import com.videoSite.entity.User;
import com.videoSite.service.UserService;
import com.videoSite.utils.MD5utils;
import io.swagger.annotations.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
@Api(tags = "用户登录与注册", description = "处理用户登录和注册相关操作")
public class Login_RegisterController {

    private static final String regEx1 = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";

    @Autowired
    private UserService userService;

    @ApiOperation(value = "首页", notes = "跳转至首页")
    @GetMapping("/index")
    public String index(){
        return "index";
    }

    @ApiOperation(value = "用户登录页面", notes = "跳转至用户登录页面")
    @GetMapping("/userLogin")
    public String login(){
        return "login";
    }

    @ApiOperation(value = "用户注册", notes = "用户提交注册信息进行注册")
    @ApiImplicitParam(name = "signUpDto", value = "用户注册信息", required = true, dataType = "SignUpDto",dataTypeClass = SignUpDto.class,paramType = "body")
    @ApiResponses({
            @ApiResponse(code = 200, message = "成功注册并跳转至登录页面"),
            @ApiResponse(code = 400, message = "无效的注册信息"),
            @ApiResponse(code = 401, message = "未授权访问"),
            @ApiResponse(code = 403, message = "访问被拒绝")
    })
    @PostMapping ("/save")
    public String save( SignUpDto signUpDto, ModelMap modelMap){
        User user = userService.getOne(new QueryWrapper<User>().eq("username", signUpDto.getUsername()));
        if (user != null) {
            modelMap.addAttribute("repeated_username", signUpDto.getUsername());
            return "register";
        }
        User email = userService.getOne(new QueryWrapper<User>().eq("email", signUpDto.getEmail()));
        if (email != null) {
            modelMap.addAttribute("repeated_email", signUpDto.getEmail());
            return "register";
        }
        Pattern p = Pattern.compile(regEx1);
        Matcher m = p.matcher(signUpDto.getEmail());
        if (!m.matches()) {
            modelMap.addAttribute("invalid_email", signUpDto.getEmail());
            return "register";
        }
        user = new User();
        BeanUtils.copyProperties(signUpDto, user);
        user.setPassword(MD5utils.encode(signUpDto.getPassword()));
        user.setStatus(1);
        userService.save(user);
        modelMap.addAttribute("newUsername", user.getUsername());
        modelMap.addAttribute("newPassword", signUpDto.getPassword());
        return "login";
    }

    @ApiOperation(value = "用户登出", notes = "用户登出并跳转至登录页面")
    @GetMapping("/logOut")
    public String logOut(){
        return "login";
    }

    @ApiOperation(value = "管理员页面", notes = "跳转至管理员页面")
    @GetMapping("/manager")
    public String manager(){
        return "manager";
    }
}
