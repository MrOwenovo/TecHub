package com.videoSite.controller;

import com.videoSite.utils.RedisUtils;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/notification")
@Api(tags = "通知管理", description = "处理用户通知相关操作")
public class NotificationController {

    @Autowired
    private RedisUtils redisUtils;

    @ApiOperation(value = "获取通知信息", notes = "根据被通知者获取通知信息列表")
    @ApiImplicitParam(name = "beNotifier", value = "被通知者用户名", required = true, dataType = "String",dataTypeClass = String.class,paramType = "path")
    @GetMapping("/getNotifications/{beNotifier}")
    public List<Object> getNotifications(@PathVariable("beNotifier") String beNotifier){
        List<Object> list = redisUtils.lGet(beNotifier, 0, -1);
        return list;
    }

    @ApiOperation(value = "获取通知数量", notes = "根据被通知者获取未读通知数量")
    @ApiImplicitParam(name = "beNotifier", value = "被通知者用户名", required = true, dataType = "String",dataTypeClass = String.class,paramType = "path")
    @GetMapping("/getNotificationsNum/{beNotifier}")
    public Integer getNotificationsNum(@PathVariable("beNotifier") String beNotifier){
        Integer num = (Integer) redisUtils.get(beNotifier + "_num");
        return num;
    }

    @ApiOperation(value = "标记通知为已读", notes = "将通知列表标记为已读状态")
    @ApiImplicitParam(name = "beNotifier", value = "被通知者用户名", required = true, dataType = "String",dataTypeClass = String.class,paramType = "path")
    @GetMapping("/clearNotifications/{beNotifier}")
    public void clearNotifications(@PathVariable("beNotifier") String beNotifier){
        redisUtils.del(beNotifier);
    }

    @ApiOperation(value = "清除通知数量", notes = "将未读通知数量清零")
    @ApiImplicitParam(name = "beNotifier", value = "被通知者用户名", required = true, dataType = "String",dataTypeClass = String.class,paramType = "path")
    @GetMapping("/clearNotificationsNum/{beNotifier}")
    public void clearNotificationsNum(@PathVariable("beNotifier") String beNotifier){
        beNotifier = beNotifier + "_num";
        redisUtils.del(beNotifier);
    }
}
