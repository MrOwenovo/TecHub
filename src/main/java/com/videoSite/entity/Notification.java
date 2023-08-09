package com.videoSite.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@ApiModel(description = "通知实体类")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Notification implements Serializable {

     @ApiModelProperty(value = "通知者")
     private String notifier;

     @ApiModelProperty(value = "被通知者")
     private String beNotifier;

     @ApiModelProperty(value = "通知时间")
     private Date notifyTime;

     @ApiModelProperty(value = "通知链接")
     private String url;

     @ApiModelProperty(value = "通知消息")
     private String msg;
}
