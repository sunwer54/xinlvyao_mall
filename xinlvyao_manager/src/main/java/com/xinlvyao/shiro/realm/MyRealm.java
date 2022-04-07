package com.xinlvyao.shiro.realm;

import com.xinlvyao.managerservice.ManagerService;
import com.xinlvyao.pojo.Manager;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

public class MyRealm extends AuthorizingRealm {
    @Autowired
    private ManagerService manageService;
    //认证用户身份的(登录)
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        System.out.println("登录用户的提交的数据"+authenticationToken);
        //获取用户的提交数据
        String username = (String)authenticationToken.getPrincipal();

        Manager manager = manageService.selectByUserName(username);
        if(manager != null){
            System.out.println("getPassword:"+manager.getPassword());
            SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(manager, manager.getPassword(), ByteSource.Util.bytes("jd"), "haha");
            return info;
        }
        return null;
    }

    //授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return null;
    }
}
