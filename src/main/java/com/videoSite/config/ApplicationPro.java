package com.videoSite.config;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ToString
@Component
@ConfigurationProperties(prefix = "constant")
@Api(tags = "应用程序常量配置", description = "用于配置应用程序中的常量值")
public class ApplicationPro {

    public static String crossOriginValue = null;

    /**
     * 设置跨域值
     * @param crossOriginValue 跨域值
     */
    public void setCrossOriginValue(String crossOriginValue) {
        ApplicationPro.crossOriginValue = crossOriginValue;
    }

    /**
     * 获取跨域值
     * @return 跨域值
     */
    @ApiModelProperty(value = "跨域值", example = "http://example.com")
    public String getCrossOriginValue() {
        return ApplicationPro.crossOriginValue;
    }
}
