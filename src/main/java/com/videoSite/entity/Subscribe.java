package com.videoSite.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(description = "订阅信息")
public class Subscribe implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO) // 添加主键注解
    @ApiModelProperty(value = "主键ID")
    private Long id; // 主键字段


    @ApiModelProperty(value = "YouTuber 名称", example = "youtuber123")
    private String youtuber;

    @ApiModelProperty(value = "订阅者名称", example = "subscriber456")
    private String subscriber;

    @ApiModelProperty(value = "订阅时间", example = "2023-08-10 12:00:00")
    private Date subscribe_time;

}
