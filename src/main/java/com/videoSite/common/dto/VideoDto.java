package com.videoSite.common.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
@ApiModel("视频上传信息DTO")
public class VideoDto {
    @ApiModelProperty(value = "视频标题", example = "Example Title")
    private String title;

    @ApiModelProperty(value = "用户名", example = "exampleUser")
    private String username;

    @ApiModelProperty(value = "视频文件", dataType = "MultipartFile")
    private MultipartFile videoFile;

    @ApiModelProperty(value = "视频简介", example = "This is an example video")
    private String introduction;

    @ApiModelProperty(value = "视频状态", example = "1")
    private int status;

    @ApiModelProperty(value = "视频封面图片", dataType = "MultipartFile")
    private MultipartFile img;
}
