package com.coffee.kafeisummary.service.impl;

import com.coffee.kafeisummary.dao.SysUserDao;
import com.coffee.kafeisummary.dao.UploadDao;
import com.coffee.kafeisummary.pojo.SysFilePojo;
import com.coffee.kafeisummary.pojo.SysUserPojo;
import com.coffee.kafeisummary.service.SysUserService;
import com.coffee.kafeisummary.topic.TopicSender;
import com.coffee.kafeisummary.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * @author kafei
 * @Title: SysUserServiceImpl
 * @Package com.coffee.kafeisummary.service.impl
 * @Description: 用户信息操作接口实现
 * @date 2020/6/23 15:43
 */
@Service
public class SysUserServiceImpl implements SysUserService {

    @Autowired
    private SysUserDao sysUserDao;
    @Autowired
    TopicSender topicSender;
    @Autowired
    private UploadDao uploadDao;
    @Value("${image.tomcat.path}")
    private String path;
    @Value("${image.tomcat.httpAddr}")
    private String httpAddr;
    /**
     * 用户信息列表查询接口实现方法
     *
     * @param page
     * @param limit
     * @return
     */
    @Override
    public Map<String, Object> sysUserInfoList(String page, String limit,Map<String, Object> requestMaps) {
        //开始查询位置
        Integer start = (Integer.parseInt(page) - 1) * Integer.parseInt(limit);
        requestMaps.put("start", start);
        //结束位置
        requestMaps.put("end", Integer.valueOf(limit));
        List<SysUserPojo> sysUserPojoList = sysUserDao.sysUserInfoList(requestMaps);
        Integer sysUserAllCount = sysUserDao.sysUserAllCount(requestMaps);
        Map<String, Object> responseMap = new HashMap<>(4);
        responseMap.put("code", 0);
        responseMap.put("msg", "");
        responseMap.put("count", sysUserAllCount);
        responseMap.put("data", sysUserPojoList);
        return responseMap;
    }

    /**
     * 删除用户信息
     *
     * @param userIds
     * @return
     */
    @Override
    public Map<String, Object> delSysUser(String userIds) {
        Map<String, Object> responseMap = new HashMap<>();
        String[] idsArray = userIds.split(",");
        List<String> idList = new ArrayList<>(idsArray.length);
        Collections.addAll(idList, idsArray);
        Integer results = sysUserDao.delSysUser(idList);
        if (results != 0) {
            responseMap.put("code", 200);
            responseMap.put("msg", "删除用户成功！");
        } else {
            responseMap.put("code", 201);
            responseMap.put("msg", "删除用户失败！");
        }
        return responseMap;
    }

    /**
     * 编辑用户信息
     *
     * @param requestMaps
     * @return
     */
    @Override
    public Map<String, Object> editSysUserInfo(Map<String, Object> requestMaps) {
        Map<String, Object> responseMap = new HashMap<>();
        requestMaps.put("login_ip", "127.0.0.1");
        requestMaps.put("create_by", "咖啡");
        requestMaps.put("update_by", "咖啡");
        Integer results = sysUserDao.editSysUserInfo(requestMaps);
        if (results != 0) {
            responseMap.put("code", 200);
            responseMap.put("msg", "用户编辑成功！");
        } else {
            responseMap.put("code", 201);
            responseMap.put("msg", "用户编辑失败！");
        }
        return responseMap;
    }

    /**
     * 添加用户信息
     *
     * @param requestMaps
     * @return
     */
    @Override
    public Map<String, Object> addSysUserInfo(Map<String, Object> requestMaps) {
        Map<String, Object> responseMap = new HashMap<>();
        requestMaps.put("login_ip", "127.0.0.1");
        requestMaps.put("create_by", "咖啡");
        requestMaps.put("update_by", "咖啡");
        Integer results = sysUserDao.addSysUserInfo(requestMaps);
        if (results != 0) {
            responseMap.put("code", 200);
            responseMap.put("msg", "用户添加成功！");
        } else {
            responseMap.put("code", 201);
            responseMap.put("msg", "用户添加失败！");
        }
        return responseMap;
    }

    /**
     * 上传用户头像
     *
     * @param request
     * @return
     */
    @Override
    public Map<String, Object> uploadAvatarIcon(HttpServletRequest request) {
        Map<String, Object> responseMap = new HashMap<>();
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        //判断 request 是否有文件上传,即多部分请求
        if (multipartResolver.isMultipart(request)) {
            //转换成多部分request
            MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
            //取得request中的所有文件名
            Iterator<String> iter = multiRequest.getFileNames();
            while (iter.hasNext()) {
                //取得上传文件
                MultipartFile file = multiRequest.getFile(iter.next());
                if (file != null) {
                    //取得当前上传文件的文件名称
                    String myFileName = file.getOriginalFilename();
                    //如果名称不为“”,说明该文件存在，否则说明该文件不存在
                    assert myFileName != null;
                    if (!"".equals(myFileName.trim())) {
                        System.out.println(myFileName);
                        //重命名上传后的文件名  按照年月日时分秒进行命名
                        String fileName = DateUtil.getImgPath("yyyyMMddHHmmss") +
                                Integer.toHexString(new Random().nextInt()) + "." + myFileName.
                                substring(myFileName.lastIndexOf(".") + 1);
                        //定义上传路径
                        try {
                            //获取文件保存路径
                            String addr = path+"\\image";
                            File fileDir = new File(addr);
                            //如果不存在 则创建
                            if (!fileDir.exists()) {
                                fileDir.mkdirs();
                            }
                            String filePath = addr +"\\"+ fileName;
                            //存文件
                            File localFile = new File(filePath);
                            file.transferTo(localFile);
                            String httpPath = httpAddr+"image/"+fileName;
                            System.out.println(httpPath);
                            responseMap.put("code", 200);
                            responseMap.put("msg", "上传成功！");
                            responseMap.put("filePath", httpPath);
                            responseMap.put("path", filePath);
                        } catch (IllegalStateException | IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                            responseMap.put("code", 201);
                            responseMap.put("msg", "上传失败！"+e.getMessage());
                            return responseMap;
                        }
                    }
                } else {
                    responseMap.put("code", 201);
                    responseMap.put("msg", "上传失败！");
                }
            }
        } else {
            responseMap.put("code", 201);
            responseMap.put("msg", "上传失败！");
        }
        return responseMap;
    }

    @Override
    public Map<String, Object> canceluploadAvatarIcon(String filePtah) {
        topicSender.sendAvatar("".equals(filePtah)?"0":filePtah);
        return null;
    }

    /**
     * 根据用户id查询用户信息
     * @param userId
     * @return
     */
    @Override
    public Map<String, Object> getSysUserInfoById(String userId) {
        Map<String, Object> responseMap = new HashMap<>();
        Map<String, Object> requestMaps = new HashMap<>();
        requestMaps.put("userId", userId);
        SysUserPojo sysUserPojo = sysUserDao.getSysUserInfoById(requestMaps);
        if (sysUserPojo != null) {
            requestMaps.put("avatar","".equals(sysUserPojo.getAvatar())?"upload":sysUserPojo.getAvatar());
            List<SysFilePojo> sysFilePojoList = uploadDao.selectSysFileInfo(requestMaps);
            if(sysFilePojoList.size()<1){
                responseMap.put("code", 201);
                responseMap.put("msg", "失败！");
            } else {
                SysFilePojo sysFilePojo = sysFilePojoList.get(0);
                responseMap.put("code", 200);
                responseMap.put("msg", "成功！");
                responseMap.put("data", sysUserPojo);
                responseMap.put("filePath", sysFilePojo.getFile_path());
            }
        } else {
            responseMap.put("code", 201);
            responseMap.put("msg", "失败！");
        }
        return responseMap;
    }
}
