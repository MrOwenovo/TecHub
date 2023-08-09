package com.videoSite.common.repo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Builder(toBuilder = true)
@ApiModel("响应实体封装类")
public class RestBean<T> {

    @ApiModelProperty(
            value = "表示请求是否成功",
            example = "true",
            required = true
    )
    @NonNull
    boolean success;

    @ApiModelProperty(
            value = "状态码",
            example = "200",
            required = true
    )
    @NonNull
    int code;

    @ApiModelProperty(
            value = "状态码描述信息",
            example = "请求成功"
    )
    @NonNull
    String message;

    @ApiModelProperty(
            value = "响应实体数据"
    )
    T data;
}
