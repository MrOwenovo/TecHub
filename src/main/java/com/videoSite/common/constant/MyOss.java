package com.videoSite.common.constant;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 阿里云OSS配置类，用于获取OSS客户端实例。
 **/
@Data
@Component
@ConfigurationProperties(prefix = "my-oss")
@ApiModel("阿里云OSS配置类")
public class MyOss {

     @ApiModelProperty("OSS服务端点")
     String ENDPOINT;

     @ApiModelProperty("OSS访问密钥ID")
     String ACCESS_KEY_Id;

     @ApiModelProperty("OSS访问密钥密钥")
     String ACCESS_KEY_SECRET;

     @ApiModelProperty("OSS存储桶名称")
     String BUCKET_NAME;

     @ApiModelProperty("OSS存储桶IP地址")
     String BUCKET_IP;

     /**
      * 构建并返回OSS客户端实例
      *
      * @return OSS客户端实例
      */
     public OSS build() {
          return new OSSClientBuilder().build(ENDPOINT, ACCESS_KEY_Id, ACCESS_KEY_SECRET);
     }
}
