package com.videoSite.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.videoSite.entity.Notification;
import com.videoSite.entity.Subscribe;
import com.videoSite.service.SubscribeService;
import com.videoSite.utils.GetCurrentUserUtil;
import com.videoSite.utils.NotificationUtil;
import com.videoSite.utils.RedisUtils;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/subscription")
@Api(tags = "订阅管理", description = "处理用户订阅相关操作")
public class SubscribeController {

    @Autowired
    private NotificationUtil notificationUtil;

    @Autowired
    private SubscribeService subscribeService;

    @ApiOperation(value = "订阅", notes = "订阅指定的 YouTuber")
    @ApiImplicitParam(name = "youtuber", value = "YouTuber 用户名", required = true, dataType = "String",dataTypeClass = String.class,paramType = "path")
    @GetMapping("/subscribe/{youtuber}")
    public void subscribe(@PathVariable("youtuber") String youtuber) {
        String currentSubscriber = GetCurrentUserUtil.getCurrentUserName();
        Map<String, Object> map = new HashMap<>();
        map.put("youtuber", youtuber);
        map.put("subscriber", currentSubscriber);

        // 该订阅关系不存在才保存
        if (subscribeService.getOne(new QueryWrapper<Subscribe>().allEq(map)) == null) {
            Subscribe subscribe = new Subscribe();
            subscribe.setSubscriber(currentSubscriber);
            subscribe.setYoutuber(youtuber);
            subscribe.setSubscribe_time(new Date());

            // 设置订阅通知
            String url = "/user/toHome/" + currentSubscriber;
            Notification notification = new Notification(currentSubscriber, youtuber, new Date(), url, "关注了你");
            notificationUtil.Notify(youtuber, notification);

            subscribeService.save(subscribe);
        } else {
            System.out.println("已订阅，不必重复订阅");
        }
    }

    @ApiOperation(value = "取消订阅", notes = "取消订阅指定的 YouTuber")
    @ApiImplicitParam(name = "youtuber", value = "YouTuber 用户名", required = true, dataType = "String",dataTypeClass = String.class,paramType = "path")
    @GetMapping("/undo_subscribe/{youtuber}")
    public void undo_subscribe(@PathVariable("youtuber") String youtuber) {
        Map<String, Object> map = new HashMap<>();
        map.put("youtuber", youtuber);
        map.put("subscriber", GetCurrentUserUtil.getCurrentUserName());
        System.out.println("取消订阅成功");
        subscribeService.remove(new QueryWrapper<Subscribe>().allEq(map));
    }

    @ApiOperation(value = "查询当前订阅状态", notes = "查询当前用户对指定 YouTuber 的订阅状态")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "youtuber", value = "YouTuber 用户名", required = true, dataType = "String",dataTypeClass = String.class,paramType = "path"),
            @ApiImplicitParam(name = "subscriber", value = "订阅者用户名", required = true, dataType = "String",dataTypeClass = String.class,paramType = "path")
    })
    @GetMapping("/status/{youtuber}/{subscriber}")
    public Integer status(@PathVariable("youtuber") String youtuber,
                          @PathVariable("subscriber") String subscriber) {
        Map<String, Object> map = new HashMap<>();
        map.put("youtuber", youtuber);
        map.put("subscriber", subscriber);
        Subscribe one = subscribeService.getOne(new QueryWrapper<Subscribe>().allEq(map));
        if (one != null) {
            return 0;
        } else {
            return 1;
        }
    }

    @ApiOperation(value = "查询已关注 YouTuber", notes = "查询当前用户已关注的 YouTuber 列表")
    @ApiImplicitParam(name = "subscriber", value = "订阅者用户名", required = true, dataType = "String",dataTypeClass = String.class,paramType = "path")
    @GetMapping("/search_focus/{subscriber}")
    public List<Subscribe> getYoutubers(@PathVariable("subscriber") String subscriber) {
        List<Subscribe> youtubers = subscribeService.list(new QueryWrapper<Subscribe>().eq("subscriber", subscriber));
        return youtubers;
    }

    @ApiOperation(value = "查询粉丝", notes = "查询指定 YouTuber 的粉丝列表")
    @ApiImplicitParam(name = "youtuber", value = "YouTuber 用户名", required = true, dataType = "String",dataTypeClass = String.class,paramType = "path")
    @GetMapping("/search_fans/{youtuber}")
    public List<Subscribe> getSubscribers(@PathVariable("youtuber") String youtuber) {
        List<Subscribe> youtubers = subscribeService.list(new QueryWrapper<Subscribe>().eq("youtuber", youtuber));
        return youtubers;
    }
}
