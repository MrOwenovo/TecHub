package com.videoSite.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.videoSite.entity.Notification;
import com.videoSite.entity.Subscribe;
import com.videoSite.entity.Video;
import com.videoSite.mapper.CollectionMapper;
import com.videoSite.utils.GetCurrentUserUtil;
import io.swagger.annotations.*;
import io.swagger.models.auth.In;
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
@RequestMapping("/collection")
@Api(tags = "收藏管理", description = "处理用户视频收藏相关操作")
public class CollectionController {
    @Autowired
    private CollectionMapper collectionMapper;

    @ApiOperation(value = "添加视频收藏", notes = "用户通过视频ID将视频添加到收藏夹")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "videoId", value = "视频ID", required = true, dataType = "int",dataTypeClass = Integer.class,paramType = "path")
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "成功添加视频到收藏夹"),
            @ApiResponse(code = 400, message = "视频已在收藏夹中"),
            @ApiResponse(code = 401, message = "未授权访问"),
            @ApiResponse(code = 403, message = "访问被拒绝")
    })
    @GetMapping("/addCollection/{videoId}")
    public void addCollection(@PathVariable("videoId") Integer videoId) {
        String username = GetCurrentUserUtil.getCurrentUserName();
        if (collectionMapper.isExisted(username, videoId) == null) {
            collectionMapper.addCollectionByUsername(username, videoId);
        } else {
            throw new IllegalStateException("视频已在收藏夹中");
        }
    }

    @ApiOperation(value = "取消视频收藏", notes = "用户通过视频ID将视频从收藏夹中移除")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "videoId", value = "视频ID", required = true, dataType = "int",dataTypeClass = Integer.class,paramType = "path")
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "成功取消视频收藏"),
            @ApiResponse(code = 400, message = "视频未在收藏夹中"),
            @ApiResponse(code = 401, message = "未授权访问"),
            @ApiResponse(code = 403, message = "访问被拒绝")
    })
    @GetMapping("/cancelCollection/{videoId}")
    public void cancelCollection(@PathVariable("videoId") Integer videoId) {
        String username = GetCurrentUserUtil.getCurrentUserName();
        if (collectionMapper.isExisted(username, videoId) != null) {
            collectionMapper.cancelCollectionByUsername(username, videoId);
        } else {
            throw new IllegalStateException("视频未在收藏夹中");
        }
    }

    @ApiOperation(value = "获取收藏视频列表", notes = "用户根据用户名获取收藏的视频列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "页码", dataType = "int",dataTypeClass = Integer.class,paramType = "path"),
            @ApiImplicitParam(name = "username", value = "用户名", required = true, dataType = "String",dataTypeClass = String.class,paramType = "path")
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "成功获取收藏视频列表"),
            @ApiResponse(code = 400, message = "无效的页码"),
            @ApiResponse(code = 401, message = "未授权访问"),
            @ApiResponse(code = 403, message = "访问被拒绝")
    })
    @GetMapping("/getCollection/{pageNum}/{username}")
    public IPage<Video> getCollection(@PathVariable(value = "pageNum", required = false) Integer pageNum,
                                      @PathVariable("username") String username) {
        if (pageNum == null || pageNum < 1) {
            throw new IllegalArgumentException("无效的页码");
        }
        Page<Video> videoPage = new Page<>(pageNum, 8);
        IPage<Video> page = collectionMapper.getCollectionByUsername(videoPage, username);
        return page;
    }

    @ApiOperation(value = "获取视频收藏状态", notes = "用户根据视频ID获取视频的收藏状态，1表示已收藏，0表示未收藏")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "videoId", value = "视频ID", required = true, dataType = "int",dataTypeClass = Integer.class,paramType = "path")
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "成功获取视频收藏状态"),
            @ApiResponse(code = 401, message = "未授权访问"),
            @ApiResponse(code = 403, message = "访问被拒绝")
    })
    @GetMapping("/getCollectionStatus/{videoId}")
    public Integer getCollectionStatus(@PathVariable("videoId") Integer videoId) {
        String username = GetCurrentUserUtil.getCurrentUserName();
        return collectionMapper.isExisted(username, videoId) == null ? 0 : 1;
    }
}
