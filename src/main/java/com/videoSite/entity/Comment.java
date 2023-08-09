package com.videoSite.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@ApiModel("评论实体")
public class Comment implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("评论ID")
    private Long id;

    @ApiModelProperty("视频ID")
    private Long video_id;

    @ApiModelProperty("视频所有者用户名")
    private String video_username;

    @ApiModelProperty("评论者用户名")
    private String commentator;

    @ApiModelProperty("评论时间")
    private Date commentTime;

    @ApiModelProperty("评论内容")
    private String commentInfo;
}
