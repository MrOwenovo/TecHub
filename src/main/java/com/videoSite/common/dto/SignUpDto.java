package com.videoSite.common.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("用户注册信息DTO")
public class SignUpDto implements Serializable {
    @ApiModelProperty(value = "用户名", example = "exampleUser")
    private String username;

    @ApiModelProperty(value = "密码", example = "examplePassword")
    private String password;

    @ApiModelProperty(value = "电子邮箱", example = "example@example.com")
    private String email;
}
