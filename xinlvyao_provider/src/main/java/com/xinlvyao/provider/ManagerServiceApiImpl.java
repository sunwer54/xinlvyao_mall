package com.xinlvyao.provider;

import com.xinlvyao.api.ManagerServiceApi;
import com.xinlvyao.mapper.ManagerMapper;
import com.xinlvyao.pojo.Manager;
import com.xinlvyao.pojo.ManagerExample;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
@Service
public class ManagerServiceApiImpl implements ManagerServiceApi {
    @Autowired
    private ManagerMapper managerMapper;
    @Override
    public Manager selectByUserName(String username) {
        ManagerExample exp = new ManagerExample();
        exp.createCriteria().andUsernameEqualTo(username);
        List<Manager> managers = managerMapper.selectByExample(exp);
        if (managers!=null&&managers.size()==1){
            return managers.get(0);
        }
        return null;
    }
}
