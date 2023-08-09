package com.videoSite.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("m_video")
@ApiModel(value = "Video对象", description = "视频实体类")
public class Video implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "视频ID", example = "1")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "视频标题")
    private String title;

    @ApiModelProperty(value = "视频时长")
    private String time;

    @ApiModelProperty(value = "视频封面图片URL")
    private String img;

    @ApiModelProperty(value = "上传者用户名")
    private String username;

    @ApiModelProperty(value = "视频URL")
    private String url;

    @ApiModelProperty(value = "视频介绍")
    private String introduction;

    @ApiModelProperty(value = "上传时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date postTime;

    @ApiModelProperty(value = "可见性状态：0-对所有人可见，1-仅粉丝可见，2-仅自己可见，3-置顶", example = "0")
    private Integer status;

    // 其他字段...

}
