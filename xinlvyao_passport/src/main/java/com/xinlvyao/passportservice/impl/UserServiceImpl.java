package com.xinlvyao.passportservice.impl;

import com.xinlvyao.api.UserServiceApi;
import com.xinlvyao.passportservice.UserService;
import com.xinlvyao.pojo.TbUser;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Reference
    private UserServiceApi userServiceApi;
    @Override
    public TbUser loginUser(TbUser user) {
        return userServiceApi.selectUser(user);
    }
}
