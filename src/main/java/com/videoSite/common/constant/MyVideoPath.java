package com.videoSite.common.constant;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 视频文件路径配置类，用于设置视频文件存储路径。
 **/
@Data
@Component
@ConfigurationProperties(prefix = "my-video-path")
@ApiModel("视频文件路径配置类")
public class MyVideoPath {

  @ApiModelProperty("视频文件存储路径")
  private String MYPATH;
}
