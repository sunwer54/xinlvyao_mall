package com.xinlvyao.managerservice.impl;

import com.xinlvyao.api.ManagerServiceApi;
import com.xinlvyao.managerservice.ManagerService;
import com.xinlvyao.pojo.Manager;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Service;

@Service
public class ManagerServiceImpl implements ManagerService {
    @Reference
    private ManagerServiceApi managerServiceApi;
    @Override
    public Manager selectByUserName(String username) {
        return managerServiceApi.selectByUserName(username);
    }
}
