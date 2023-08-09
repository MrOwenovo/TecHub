package com.videoSite.service.impl;

import com.videoSite.entity.User;
import com.videoSite.mapper.UserMapper;
import com.videoSite.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
