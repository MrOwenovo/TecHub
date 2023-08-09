package com.videoSite.controller;

import cn.hutool.core.text.replacer.StrReplacer;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.videoSite.entity.User;
import com.videoSite.service.UserService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/user")
@Api(tags = "用户管理", description = "处理用户信息和页面跳转操作")
public class UserController {

    @Autowired
    private UserService userService;

    @ApiOperation(value = "根据用户名获取用户信息", notes = "根据用户名查询用户信息")
    @ApiImplicitParam(name = "username", value = "用户名", required = true, dataType = "String",dataTypeClass = String.class,paramType = "path")
    @GetMapping("/getUsersInfo/{username}")
    @ResponseBody
    public User getUsersByName(@PathVariable("username") String username) {
        User user = userService.getOne(new QueryWrapper<User>().eq("username", username));
        return user;
    }

    @ApiOperation(value = "设置用户个性签名", notes = "设置用户的个性签名")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", required = true, dataType = "String",dataTypeClass = String.class,paramType = "path"),
            @ApiImplicitParam(name = "signature", value = "个性签名", required = true, dataType = "String",dataTypeClass =String.class,paramType = "path")
    })
    @GetMapping("/setSignature/{username}/{signature}")
    @ResponseBody
    public void setSignature(@PathVariable("username") String username,
                             @PathVariable("signature") String signature) {
        User user = new User();
        user.setSignature(signature);
        userService.update(user, new QueryWrapper<User>().eq("username", username));
    }

    @ApiOperation(value = "跳转到用户主页", notes = "跳转到指定用户的主页")
    @ApiImplicitParam(name = "username", value = "用户名", required = true, dataType = "String",dataTypeClass = String.class,paramType = "path")
    @GetMapping("/toHome/{username}")
    public String toHome(@PathVariable("username") String username, ModelMap modelMap) {
        modelMap.addAttribute("username", username);
        return "user/home";
    }

    @ApiOperation(value = "跳转到用户粉丝页", notes = "跳转到指定用户的粉丝页")
    @ApiImplicitParam(name = "username", value = "用户名", required = true, dataType = "String",dataTypeClass = String.class,paramType = "path")
    @GetMapping("/toFans/{username}")
    public String toFans(@PathVariable("username") String username, ModelMap modelMap) {
        modelMap.addAttribute("username", username);
        return "user/fans";
    }

    @ApiOperation(value = "跳转到用户关注页", notes = "跳转到指定用户的关注页")
    @ApiImplicitParam(name = "username", value = "用户名", required = true, dataType = "String",dataTypeClass = String.class,paramType = "path")
    @GetMapping("/toFocus/{username}")
    public String toFocus(@PathVariable("username") String username, ModelMap modelMap) {
        modelMap.addAttribute("username", username);
        return "user/focus";
    }

    @ApiOperation(value = "跳转到用户视频页", notes = "跳转到指定用户的视频页")
    @ApiImplicitParam(name = "username", value = "用户名", required = true, dataType = "String",dataTypeClass = String.class,paramType = "path")
    @GetMapping("/toVideos/{username}")
    public String toVideos(@PathVariable("username") String username, ModelMap modelMap) {
        modelMap.addAttribute("username", username);
        return "user/videos";
    }

    @ApiOperation(value = "跳转到用户收藏页", notes = "跳转到指定用户的收藏页")
    @ApiImplicitParam(name = "username", value = "用户名", required = true, dataType = "String",dataTypeClass = String.class,paramType = "path")
    @GetMapping("/toCollection/{username}")
    public String toCollection(@PathVariable("username") String username, ModelMap modelMap) {
        modelMap.addAttribute("username", username);
        return "user/collection";
    }
}
