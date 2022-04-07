package com.xinlvyao.managerservice;

import com.xinlvyao.pojo.Manager;

public interface ManagerService {
    public Manager selectByUserName(String username);
}
