package com.xinlvyao.provider;

import com.xinlvyao.api.UserServiceApi;
import com.xinlvyao.mapper.TbUserMapper;
import com.xinlvyao.pojo.TbUser;
import com.xinlvyao.pojo.TbUserExample;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
@Service
public class UserServiceApiImpl implements UserServiceApi {
    @Autowired
    private TbUserMapper userMapper;
    @Override
    public TbUser selectUser(TbUser user) {
        TbUserExample exp = new TbUserExample();
        exp.createCriteria().andUsernameEqualTo(user.getUsername()).andPasswordEqualTo(user.getPassword());
        List<TbUser> users = userMapper.selectByExample(exp);
        if (users!=null&&users.size()!=0){
            return users.get(0);
        }
        return null;
    }
}
