package com.kafei.usercoffee.service.impl;

import com.kafei.usercoffee.dao.UserDao;
import com.kafei.usercoffee.model.Permission;
import com.kafei.usercoffee.model.User;
import com.kafei.usercoffee.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author kafei
 * @Title: UserModulesServiceImpl
 * @Package com.kafei.usermodules.service.impl
 * @Description: 用户相关操作业务处理接口实现层
 * @date 2020/5/27 17:17
 */
@Service
public class UserServiceImpl implements UserService {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private UserDao userDao;

    /**
     * 根据用户名查询用户信息
     * @param username
     * @return
     */
    @Override
    public User queryUserInformationByUserName(String username){
       return userDao.queryUserInformationByUserName(username);
    }
    /**
     * 根据用户名查询用户权限信息
     * @param username
     * @return
     */
    @Override
    public List<Permission> queryUserPermissionByUserName(String username){
       return userDao.queryUserPermissionByUserName(username);
    }
}