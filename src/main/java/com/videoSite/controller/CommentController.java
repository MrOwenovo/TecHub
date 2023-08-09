package com.videoSite.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.videoSite.entity.Comment;
import com.videoSite.entity.Notification;
import com.videoSite.service.CommentService;
import com.videoSite.utils.NotificationUtil;
import com.videoSite.utils.RedisUtils;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.List;

@RestController
@RequestMapping("/comment")
@Api(tags = "评论管理", description = "处理视频评论相关操作")
public class CommentController {

    @Autowired
    private NotificationUtil notificationUtil;

    @Autowired
    private CommentService commentService;

    @ApiOperation(value = "发布评论", notes = "用户发布评论并通知视频作者")
    @ApiImplicitParam(name = "comment", value = "评论对象", required = true, dataType = "Comment",dataTypeClass = Comment.class,paramType = "body")
    @ApiResponses({
            @ApiResponse(code = 200, message = "成功发布评论并通知作者"),
            @ApiResponse(code = 400, message = "无效的评论"),
            @ApiResponse(code = 401, message = "未授权访问"),
            @ApiResponse(code = 403, message = "访问被拒绝")
    })
    @PostMapping("/postComment")
    public void postComment(@RequestBody Comment comment){
        String beNotifier = comment.getVideo_username();
        String url = "/video/toVideo/" + comment.getVideo_id();
        Notification notification = new Notification(comment.getCommentator(), beNotifier, comment.getCommentTime(), url, "评论了你的视频");
        notificationUtil.Notify(beNotifier, notification);
        commentService.save(comment);
    }

    @ApiOperation(value = "获取视频评论列表", notes = "用户获取指定视频的评论列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "页码", dataType = "int",dataTypeClass = Integer.class,paramType = "path"),
            @ApiImplicitParam(name = "video_id", value = "视频ID", required = true, dataType = "int",dataTypeClass = Integer.class,paramType = "path")
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "成功获取视频评论列表"),
            @ApiResponse(code = 400, message = "无效的页码"),
            @ApiResponse(code = 401, message = "未授权访问"),
            @ApiResponse(code = 403, message = "访问被拒绝")
    })
    @GetMapping("/getComment/{pageNum}/{video_id}")
    public IPage<Comment> getComment(@PathVariable("video_id") Integer video_id,
                                     @PathVariable(value = "pageNum",required = false) Integer pageNum){
        if (pageNum == null || pageNum < 1) {
            throw new IllegalArgumentException("无效的页码");
        }
        Page<Comment> commentPage = new Page<>(pageNum, 8);
        IPage<Comment> page = commentService.page(commentPage, new QueryWrapper<Comment>().eq("video_id", video_id).orderByDesc("id"));
        return page;
    }
}
