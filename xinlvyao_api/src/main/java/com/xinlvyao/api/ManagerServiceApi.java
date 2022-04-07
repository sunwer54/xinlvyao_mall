package com.xinlvyao.api;

import com.xinlvyao.pojo.Manager;

public interface ManagerServiceApi {
    public Manager selectByUserName(String username);
}
