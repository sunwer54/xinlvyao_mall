package com.xinlvyao.shiro.config;

import com.xinlvyao.shiro.realm.Base64Util;
import com.xinlvyao.shiro.realm.MyRealm;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;
import org.apache.shiro.mgt.SecurityManager;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

/**
 * shiro的配置类,替代以前的shiro.xml配置文件
 */
@Configuration
public class ShiroConfig{
	/**
	 * 配置shiro的过滤器链：对浏览器提交的请求进行拦截，决定哪些请求是否可放行，以及登出，以及免登录的记住我
	 */
	@Bean
	public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {
		ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
		// 添加安全管理器
		shiroFilterFactoryBean.setSecurityManager(securityManager);
		// 配置我们的登录请求地址(此处可以拦截用户提交的数据给 shiro处理)
		shiroFilterFactoryBean.setLoginUrl("/loginManager");
		//配置我们在登录页登录成功后的跳转地址，如果你访问的是非/login地址，则跳到您访问的地址
		shiroFilterFactoryBean.setSuccessUrl("/index");
		// 未授权跳转的界面;
		shiroFilterFactoryBean.setUnauthorizedUrl("/login");

		// 添加shiro内置过滤器(权限配置（过滤器链定义）（anon:放行; authc：表示访问该地址用户必须身份验证通过)
		Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
		//不需要身份认证的(放行),/** 表示放行其下所有静态资源的处理
		filterChainDefinitionMap.put("/js/**", "anon");
		filterChainDefinitionMap.put("/css/**", "anon");
		filterChainDefinitionMap.put("/login", "anon");//放行
		//所有地址都要身份认证
		filterChainDefinitionMap.put("/**", "authc");
		// 退出系统的过滤器（会销毁SessionManager中的数据）
		filterChainDefinitionMap.put("/logout", "logout");
		//filterChainDefinitionMap.put("/index","user");//记住我,免登录
		shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
		return shiroFilterFactoryBean;
	}

	/**
	 * 设置加密参数
	 * @return
	 */
	@Bean
	public HashedCredentialsMatcher hashedCredentialsMatcher() {
		HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
		hashedCredentialsMatcher.setHashAlgorithmName("md5");// 设置加密算法（散列算法:这里使用MD5算法）;
		hashedCredentialsMatcher.setHashIterations(2);// 设置加密迭代次数（默认一次）
		System.out.println("加密");
		return hashedCredentialsMatcher;
	}
	@Autowired
	private HashedCredentialsMatcher hashedCredentialsMatcher;

	/**
	 * 注册自定义的Realm,属性credentialsMatcher是AuthorizingRealm父类
	 * 的AuthenticatingRealm父类中的属性，把上面的加密设置到自定义的Realm里面
	 * @return
	 */
	@Bean
	public MyRealm myShiroRealm(){
		MyRealm myRealm = new MyRealm();
		myRealm.setCredentialsMatcher(hashedCredentialsMatcher);
		return myRealm;
	}
	@Autowired
	private MyRealm myRealm;

	/**
	 * 配置cookie：cookie是浏览器端的数据存储技术，设置cookie的有效时间
	 * 
	 * @return
	 */
	public SimpleCookie rememberMeCookie() {
		// 这个参数是cookie的名称，对应前端的checkbox的name = rememberMe
		SimpleCookie simpleCookie = new SimpleCookie("rememberMe");
		// <!-- 记住我cookie生效时间7天 ,单位秒;-->
		simpleCookie.setMaxAge(604800);
		return simpleCookie;
	}

	/**
	 * cookie管理对象;记住我功能
	 * @return
	 */
	public CookieRememberMeManager rememberMeManager() {
		CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
		cookieRememberMeManager.setCookie(rememberMeCookie());
		// rememberMe cookie加密的密钥 建议每个项目都不一样 默认AES算法 密钥长度(128 256 512 位)
		try {
			cookieRememberMeManager.setCipherKey(Base64Util.decode("3AvVhmFLUs0KTA3Kprsdag=="));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return cookieRememberMeManager;
	}

	/**
	 * 注册securityManager;属性realms是DefaultWebSecurityManager的间接父类RealmSecurityManager中的属性
	 * 把自定义的realm(里面封装了从数据库查询到的用户信息和盐)交给DefaultWebSecurityManager
	 * @return
	 */
	@Bean
	public SecurityManager securityManager() {
		DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
		securityManager.setRealm(myRealm);
		// 注入记住我管理器;（属性rememberMeManager是DefaultWebSecurityManager的父类
		// DefaultSecurityManager中的属性）
		securityManager.setRememberMeManager(rememberMeManager());
		return securityManager;
	}

	/**
	 * 开启shiro aop注解支持. 使用代理方式;所以需要开启代码支持;
	 * @PermissionAnnotation(permissionName = "boke:*")
	 * @param securityManager
	 * @return
	 */
	@Bean
	public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
		AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
		authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
		return authorizationAttributeSourceAdvisor;
	}

	@Bean(name = "simpleMappingExceptionResolver")
	public SimpleMappingExceptionResolver createSimpleMappingExceptionResolver() {
		SimpleMappingExceptionResolver r = new SimpleMappingExceptionResolver();
		Properties mappings = new Properties();
		mappings.setProperty("DatabaseException", "databaseError");// 数据库异常处理
		mappings.setProperty("UnauthorizedException", "403");
		r.setExceptionMappings(mappings); // None by default
		r.setDefaultErrorView("error"); // No default
		r.setExceptionAttribute("exception"); // Default is "exception"
		return r;
	}
}