package com.videoSite.config;

import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ToString
@Component
@ConfigurationProperties(prefix = "constant")
public class ApplicationPro {

    public static String crossOriginValue = null;

    public void setCrossOriginValue(String crossOriginValue) {
        ApplicationPro.crossOriginValue = crossOriginValue;
    }

    public String getCrossOriginValue() {
        return ApplicationPro.crossOriginValue;
    }
}
