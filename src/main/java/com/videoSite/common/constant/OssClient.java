package com.videoSite.common.constant;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import io.swagger.annotations.Api;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Data
@Api(tags = "阿里云OSS客户端", description = "用于创建阿里云OSS客户端实例")
public class OssClient {
    @Autowired
    MyOss oss;
    private OssClient(){};
    private OSS ossClient=null;

    /**
     * 获取阿里云OSS客户端实例
     * @return 阿里云OSS客户端实例
     */
    public OSS getOssClient(){
        if (ossClient==null){
            return new OSSClientBuilder().build(oss.ENDPOINT,oss.ACCESS_KEY_Id,oss.ACCESS_KEY_SECRET);
        }else {
            return ossClient;
        }
    }
}
