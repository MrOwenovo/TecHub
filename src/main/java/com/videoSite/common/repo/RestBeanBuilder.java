package com.videoSite.common.repo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;

import java.lang.ref.SoftReference;

@Data
@Builder(toBuilder = true)
@FieldDefaults(makeFinal = true)
@ApiModel("RestBean建造者")
public class RestBeanBuilder<T> {

    public final static boolean DEFAULT = false;

    public final static boolean USER_DEFINED = true;

    @Builder.Default
    @NonNull
    @ApiModelProperty("响应状态码")
    ResultCode code = ResultCode.SUCCESS;

    @ApiModelProperty("响应消息")
    String message;

    @Builder.Default
    @ApiModelProperty("是否自定义消息")
    boolean messageType = DEFAULT;

    @ApiModelProperty("响应实体数据")
    T data;

    /**
     * 将RestBeanBuilder对象转换为RestBean对象
     *
     * @return RestBean对象
     */
    public RestBean<T> ToRestBean() {
        SoftReference<Boolean> SoftSuccess = code.getCode() == 200 ? new SoftReference<>(true) : new SoftReference<>(false);
        if (messageType)
            return (RestBean<T>) RestBean.builder().success(SoftSuccess.get()).code(code.getCode()).message(message).data(data).build();
        return (RestBean<T>) RestBean.builder().success(SoftSuccess.get()).code(code.getCode()).message(code.getMessage()).data(data).build();
    }
}
