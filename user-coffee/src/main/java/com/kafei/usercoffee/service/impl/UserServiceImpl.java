package com.kafei.usercoffee.service.impl;

import com.kafei.usercoffee.dao.UserDao;
import com.kafei.usercoffee.model.Permission;
import com.kafei.usercoffee.model.Role;
import com.kafei.usercoffee.model.User;
import com.kafei.usercoffee.service.UserService;
import com.kafei.usercoffee.util.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    /**
     * @Description: 用户注册
     * @param ${tags}
     * @return ${return_type}
     * @throws
     * @author kafei
     * @date 2020/6/3 11:19
     */
    @Override
    public void userRegister(Map<String, Object> params) {
        String password = MD5Util.encode(params.get("password").toString());
        params.put("password",password);
        params.put("createDate",new Date());
        params.put("lastlogintime",new Date());
        userDao.userRegister(params);
    }

    @Override
    public Map<String,Object> roleInfoList(String page, String limit) {
        Map<String,Object> requestParam = new HashMap<>();
        Integer statr = (Integer.valueOf(page)-1)*Integer.valueOf(limit);//开始查询位置
        requestParam.put("statr",statr);
        requestParam.put("end",Integer.valueOf(limit));//结束位置
        List<Role> roleList = userDao.roleInfoList(requestParam);
        Integer roleCount = userDao.roleAllCount(requestParam);
        Map<String,Object> responseMap = new HashMap<>();
        responseMap.put("code",0);
        responseMap.put("msg","");
        responseMap.put("count",roleCount);
        responseMap.put("data",roleList);
        return responseMap;
    }

    @Override
    public Map<String, Object> delRole(String id) {
        Map<String, Object> responseMap = new HashMap<>();
        Integer results = userDao.delRole(Integer.valueOf(id));
        if(results!=1){
            responseMap.put("code",0);
            responseMap.put("msg","删除失败！");
        } else {
            responseMap.put("code",1);
            responseMap.put("msg","删除成功！");
        }
        return responseMap;
    }
}
