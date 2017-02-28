package com.ys.wx.service.wx;

import com.ys.wx.model.wx.response.PartyListResponse.Department;
import com.ys.wx.model.wx.response.UserDetailsResponse.UserlistBean;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Title : 微信常用功能接口
 * Description :
 * Author : Ceaser wang Jerry xu    date : 2017/1/11
 * Update :                         date :
 * Version : 1.0.0
 * Copyright (c) 二龙湖基地组织  2016 ~ 2017 版权所有
 */
public interface IWxServiceClient {

    /**
     * 获取token
     */
    String getToken();

    /**
     * 上传文件，增量覆盖（可以使部门或者人员的文件）
     *
     * @param file csv文件
     */
    void uploadFile(final File file);

    //*********************************************************===部门===*********************************************************//

    /**
     * 得到部门id所下属的整个部门树的列表
     *
     * @param access_token 调用接口凭证
     * @param id           所在部门id
     * @return 当前部门下的部门列表
     */
    ArrayList<Department> getParty(String access_token, int id);

    /**
     * 依据ID删除部门
     *
     * @param access_token 调用接口凭证
     * @param id           部门Id
     * @return
     */
    boolean deleteParty(String access_token, String id);

    /**
     * 创建一个部门
     *
     * @param access_token 调用接口凭证
     * @param department   部门对象
     * @return
     */
    boolean createParty(String access_token, Department department);

    /**
     * 更新一个部门
     *
     * @param access_token 调用接口凭证
     * @param department   部门对象
     * @return
     */
    boolean updateOneParty(String access_token, Department department);

    //*********************************************************===人员===*********************************************************//

    /**
     * 获取部门下的所有人员
     *
     * @param access_token  调用接口凭证
     * @param department_id 所在部门id
     * @return 所在部门下人员列表
     */
    List<UserlistBean> getUserDetails(String access_token, int department_id);

    /**
     * 添加人员
     *
     * @param access_token 调用接口凭证
     * @param userlistbean 人员列表对象
     * @return
     */
    boolean addUser(String access_token, UserlistBean userlistbean);

    /**
     * 依据人员id删除人员
     *
     * @param access_token 调用接口凭证
     * @param userid       人员Id
     * @return
     */
    List<UserlistBean> deleteUser(String access_token, String userid);

    /**
     * 更新用户
     *
     * @param access_token 调用接口凭证
     * @param userlistbean 人员列表对象
     * @return
     */
    boolean updateUser(String access_token, UserlistBean userlistbean);

    /**
     * 通过文件方式增量更新人员
     *
     * @param file
     */
    void uploadFileSysUser(final File file);

}
