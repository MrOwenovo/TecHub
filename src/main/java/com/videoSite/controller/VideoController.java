package com.videoSite.controller;


import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.videoSite.common.constant.MyOss;
import com.videoSite.common.constant.MyVideoPath;
import com.videoSite.common.constant.OssClient;
import com.videoSite.common.dto.VideoDto;
import com.videoSite.entity.Video;
import com.videoSite.service.VideoService;
import com.videoSite.utils.FFmpegUtils;
import com.videoSite.utils.RedisUtils;
import io.swagger.annotations.*;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Controller
@RequestMapping("/video")
@Api(tags = "视频管理", description = "处理视频相关操作")
public class VideoController {

    @Autowired
    VideoService videoService;
    @Autowired
    MyOss oss;
    @Autowired
    OssClient ossClient;
    @Autowired
    RedisUtils redisUtils;
    @Autowired
    MyVideoPath myVideoPath;

    @ApiOperation(value = "获取所有视频信息", notes = "分页获取所有视频信息")
    @ApiImplicitParam(name = "pageNum", value = "页码", required = true, dataType = "Integer",dataTypeClass = Integer.class,paramType = "path")
    @GetMapping("/getAllVideos/{pageNum}")
    @ResponseBody
    public IPage<Video> getAllVideo(@PathVariable(value = "pageNum",required = false) Integer pageNum){
        Page<Video> videoPage=new Page<>(pageNum,6);
        IPage<Video> page = videoService.page(videoPage, new QueryWrapper<Video>().ne("status",2).orderByDesc("id"));
        return page;
    }

    @ApiOperation(value = "根据关键字搜索视频", notes = "分页根据标题关键字搜索视频信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "页码", required = true, dataType = "Integer",dataTypeClass = Integer.class,paramType = "path"),
            @ApiImplicitParam(name = "title", value = "标题关键字", required = true, dataType = "String",dataTypeClass = String.class,paramType = "path")
    })
    @GetMapping("/getKeyVideos/{pageNum}/{title}")
    @ResponseBody
    public IPage<Video> getKeyVideos(@PathVariable(value = "pageNum",required = false) Integer pageNum,
                                     @PathVariable(value = "title") String title){
        Page<Video> videoPage=new Page<>(pageNum,6);
        IPage<Video> page = videoService.page(videoPage, new QueryWrapper<Video>().ne("status",2).like("title",title).orderByDesc("id"));
        return page;
    }

    @ApiOperation(value = "跳转到视频播放页面", notes = "跳转到指定视频的播放页面")
    @ApiImplicitParam(name = "videoId", value = "视频ID", required = true, dataType = "Integer",dataTypeClass = Integer.class,paramType = "path")
    @GetMapping("/toVideo/{videoId}")
    public String toVideo(@PathVariable("videoId")Integer videoId, ModelMap modelMap) {
        Video video = videoService.getById(videoId);
        // 设置URL过期时间为半小时。
        Date expiration = new Date(new Date().getTime() + 1800 * 1000);
        // 生成以GET方法访问的签名URL，访客可以直接通过浏览器访问相关内容。
        URL url = ossClient.getOssClient().generatePresignedUrl(oss.getBUCKET_NAME(), video.getUrl(), expiration);
        // 关闭OSSClient。
        ossClient.getOssClient().shutdown();
         modelMap.addAttribute("url",url.toString());
         modelMap.addAttribute("videoId",videoId);
        return "video/video";
    }

    @ApiOperation(value = "根据视频ID获取视频信息", notes = "根据视频ID获取视频详细信息")
    @ApiResponses({
            @ApiResponse(code = 200, message = "获取成功"),
            @ApiResponse(code = 404, message = "视频不存在")
    })
    @ApiImplicitParam(name = "videoId", value = "视频ID", required = true, dataType = "Integer",dataTypeClass = Integer.class,paramType = "path")
    @GetMapping("/getVideoInfo/{videoId}")
    @ResponseBody
    public Video getVideoInfo(@PathVariable("videoId") Integer videoId){
        Video video=videoService.getById(videoId);
        return video;
    }

    @ApiOperation(value = "添加视频", notes = "上传视频并添加视频信息")
    @ApiResponses({
            @ApiResponse(code = 200, message = "上传成功"),
            @ApiResponse(code = 400, message = "上传失败")
    })
    @ApiImplicitParam(name = "videoDto", value = "视频信息", required = true, dataType = "VideoDto",dataTypeClass = VideoDto.class)
    @PostMapping("/addVideo")
    @ResponseBody
    public String addVideo( VideoDto videoDto) throws IOException {
        //第一步：处理并写入视频上传的视频，并获得视频路径
        OSS ossClient = this.ossClient.getOssClient();
        MultipartFile videoFile=videoDto.getVideoFile();
        if(!videoFile.isEmpty()){
            //获取文件类型及后缀
            String[] fileType =videoFile.getContentType().split("\\/");
            //如果用户上传的文件属于video类型才允许执行下面创建用户目录、存入数据库操作等一系列操作
            if (fileType[0].equals("video")){
                //获取文件名,这里设置新的文件名，为了避免用户上传多个同名视频发生文件名相同
                String suffix ="."+ fileType[1];
                String finalFilename = videoDto.getUsername() + new Date().getTime() + suffix;
                try {
                      long t1=System.currentTimeMillis();
                      String ossVideoObject=videoDto.getUsername()+"/"+finalFilename;
                      ossClient.putObject(oss.getBUCKET_NAME(),ossVideoObject, videoFile.getInputStream());
                      System.out.println(videoDto.getUsername()+"视频上传成功,用时："+(System.currentTimeMillis()-t1)/1000+"秒");
                      String  videoUrl=ossVideoObject;

                //第二步：通过ffmpeg获取视频时长及写入视频封面图片和获取路径
                 //临时存入本地磁盘
                File file = new File(myVideoPath.getMYPATH()+finalFilename);
                videoFile.transferTo(file);
                 //生成封面文件并获取在本地磁盘的路径
                String imgDir= FFmpegUtils.grabberVideoFramer(finalFilename,myVideoPath.getMYPATH());
                 //设置图片名称
                String imgFileName=videoDto.getUsername() + new Date().getTime()+".png";
                 //设置图片的oss名称
                String ossImgObject=videoDto.getUsername()+"/"+imgFileName;
                 //获取视频时长
                String videoTime=FFmpegUtils.formatDuration(FFmpegUtils.videoDuration(file));
                long t2=System.currentTimeMillis();
                //存入oss
                ossClient.putObject(oss.getBUCKET_NAME(),ossImgObject,new FileInputStream(imgDir));
                //设置oss访问权限为公开读
//                ossClient.setObjectAcl(oss.getBUCKET_NAME(), ossImgObject, CannedAccessControlList.PublicRead);
                System.out.println(videoDto.getUsername()+"图片上传成功,用时："+(System.currentTimeMillis()-t2)/1000+"秒");
                //获取oss公开读路径
                String imgUrl="https://"+oss.getBUCKET_IP()+"/"+videoDto.getUsername()+"/"+imgFileName;
                //第三步：此时可以封装进pojo对象存入数据库了
                    Video video = new Video();
                    video.setUsername(videoDto.getUsername());
                    video.setTitle(videoDto.getTitle());
                    video.setIntroduction(videoDto.getIntroduction());
                    video.setUrl(videoUrl);
                    video.setImg(imgUrl);
                    video.setTime(videoTime);
                    video.setStatus(videoDto.getStatus());
                    video.setPostTime(new Date());
                    videoService.save(video);
     //大功告成，返回页面
                    return "上传成功";
                } catch (OSSException oe) {
                    oe.printStackTrace();
                } catch (ClientException ce) {
                    ce.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    this.ossClient.getOssClient().shutdown();
                }
                }else {
                throw new RuntimeException("请上传mp4视频格式的文件");
              }
            }else {
            throw new RuntimeException("请选择上传的视频");
        }
        return "上传失败";
    }

    @ApiOperation(value = "获取用户发布的视频", notes = "分页获取指定用户发布的视频信息")
    @ApiResponses({
            @ApiResponse(code = 200, message = "获取成功"),
            @ApiResponse(code = 404, message = "用户不存在")
    })
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "页码", required = true, dataType = "Integer",dataTypeClass = Integer.class,paramType = "path"),
            @ApiImplicitParam(name = "username", value = "用户名", required = true, dataType = "String",dataTypeClass = String.class,paramType = "path")
    })
    @GetMapping("/get7VideosInfoPerPageByUsername/{pageNum}/{username}")
    @ResponseBody
    public  IPage<Video> get7VideosInfoPerPageByUsername(@PathVariable(value = "pageNum",required = false) Integer pageNum,@PathVariable("username") String username){
        Page<Video> videoPage=new Page<>(pageNum,7);
        IPage<Video> page = videoService.page(videoPage, new QueryWrapper<Video>().eq("username", username).orderByDesc("id"));
        return page;
    }
    @ApiOperation(value = "分页获取用户发布的8条视频信息", notes = "分页获取指定用户发布的8条视频信息，排除状态为2的视频")
    @ApiResponses({
            @ApiResponse(code = 200, message = "获取成功"),
            @ApiResponse(code = 404, message = "用户不存在")
    })
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "页码", required = true, dataType = "Integer",dataTypeClass = Integer.class,paramType = "path"),
            @ApiImplicitParam(name = "username", value = "用户名", required = true, dataType = "String",dataTypeClass = String.class,paramType = "path")
    })
    @GetMapping("/get8VideosInfoPerPageByUsername/{pageNum}/{username}")
    @ResponseBody
    public  IPage<Video> get8VideosInfoPerPageByUsername(@PathVariable(value = "pageNum",required = false) Integer pageNum,@PathVariable("username") String username){
        Page<Video> videoPage=new Page<>(pageNum,8);
        IPage<Video> page = videoService.page(videoPage, new QueryWrapper<Video>().eq("username", username).ne("status",2).orderByDesc("id"));
        return page;
    }
    @ApiOperation(value = "分页获取用户发布的8条视频信息", notes = "分页获取指定用户发布的8条视频信息")
    @ApiResponses({
            @ApiResponse(code = 200, message = "获取成功"),
            @ApiResponse(code = 404, message = "用户不存在")
    })
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "页码", required = true, dataType = "Integer",dataTypeClass = Integer.class,paramType = "path"),
            @ApiImplicitParam(name = "username", value = "用户名", required = true, dataType = "String",dataTypeClass = String.class,paramType = "path")
    })
    @GetMapping("/getOwn8VideosInfoPerPageByUsername/{pageNum}/{username}")
    @ResponseBody
    public  IPage<Video> getOwn8VideosInfoPerPageByUsername(@PathVariable(value = "pageNum",required = false) Integer pageNum,@PathVariable("username") String username){
        Page<Video> videoPage=new Page<>(pageNum,8);
        IPage<Video> page = videoService.page(videoPage, new QueryWrapper<Video>().eq("username", username).orderByDesc("id"));
        return page;
    }

    @ApiOperation(value = "根据视频ID获取视频信息", notes = "根据视频ID获取视频详细信息")
    @ApiResponses({
            @ApiResponse(code = 200, message = "获取成功"),
            @ApiResponse(code = 404, message = "视频不存在")
    })
    @ApiImplicitParam(name = "videoId", value = "视频ID", required = true, dataType = "Integer",dataTypeClass = Integer.class,paramType = "path")
    @GetMapping("/getVideoInfoById/{videoId}")
    @ResponseBody
    public Video getVideoInfoById(@PathVariable("videoId") Integer videoId){
        Video videoInfo = videoService.getById(videoId);
        return  videoInfo;
    }

    @ApiOperation(value = "根据用户名获取置顶视频信息", notes = "根据用户名获取已置顶的视频信息")
    @ApiResponses({
            @ApiResponse(code = 200, message = "获取成功"),
            @ApiResponse(code = 404, message = "用户不存在或没有置顶视频")
    })
    @ApiImplicitParam(name = "username", value = "用户名", required = true, dataType = "String",dataTypeClass = String.class,paramType = "path")
    @GetMapping("/getTopVideoInfoByUsernameAndStatus/{username}")
    @ResponseBody
    public Video getTopVideoInfoByUsername(@PathVariable("username") String username){
        Map<String,Object> map=new HashMap<>();
        map.put("username",username);
        map.put("status",3);
       Video videoInfo=videoService.getOne(new QueryWrapper<Video>().allEq(map));
       return videoInfo;
    }

    @ApiOperation(value = "设置视频置顶状态", notes = "将指定视频设置为置顶状态")
    @ApiImplicitParam(name = "videoId", value = "视频ID", required = true, dataType = "long",dataTypeClass = Long.class,paramType = "path")
    @GetMapping("/setTopVideoStatus/{videoId}")
    @ResponseBody
    public void setTopVideoStatus(@PathVariable("videoId")long videoId){
        Video video=new Video();
        video.setStatus(3);
        videoService.update(video,new QueryWrapper<Video>().eq("id",videoId));

    }

    @ApiOperation(value = "取消视频置顶状态", notes = "将指定视频的置顶状态取消")
    @ApiImplicitParam(name = "videoId", value = "视频ID", required = true, dataType = "long",dataTypeClass = Long.class,paramType = "path")
    @PostMapping("/cancelTopVideoStatus/{videoId}")
    public void cancelTopVideoStatus(@PathVariable("videoId") long videoId) {
        Video video = new Video();
        video.setStatus(0);
        videoService.update(video, new QueryWrapper<Video>().eq("id", videoId));
    }

}



