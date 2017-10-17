package com.ys.wx.service.wx;

import com.ys.wx.model.wx.response.PartyListResponse.Department;
import com.ys.wx.model.wx.response.UserDetailsResponse.UserlistBean;
import com.ys.wx.vo.Person;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Title : EnterpriseWeChat 操作
 * Description :
 * Author : Jerry xu    date : 2017/1/11
 * Update :             date :
 * Version : 1.0.0
 */
public interface IWxServiceClient {

    /**
     * 获取token
     */
    String getToken();

    /**
     * 验证token是否有效
     */
    boolean verifyTokenValid(long tokenTime, String access_token);

    /**
     * 上传文件，增量覆盖（可以使部门或者人员的文件）
     */
    void uploadFile(final File file);

    /**
     * 得到企业顶级下整棵树列表
     */
    ArrayList<Department> getParty(String access_token, int id);

    /**
     * 获取部门下的所有人员(详情)
     */
    List<UserlistBean> getUserDetails(String access_token, int department_id);

    /**
     * 添加人员
     */
    boolean addUser(String access_token, UserlistBean userlistbean);

    /**
     * 添加人员
     */
    public boolean addUser(String access_token, Person itemperson);
    
    /**
     * 依据ID删除部门
     */
    boolean deleteParty(String access_token, String id);

    /**
     * 依据人员id删除人员
     */
    List<UserlistBean> deleteUser(String access_token, String userid);

    /**
     * 更新用户
     */
    boolean updateUser(String access_token, UserlistBean userlistbean);
    
    /**
     * 更新用户
     */
    public boolean updateUser(String access_token, Person itemperson);

    /**
     * 通过文件方式增量更新人员
     */
    boolean uploadFileSysUser(final File file);

    /**
     * 创建一个部门
     */
    boolean createParty(String access_token, Department department);

    /**
     * 更新一个部门
     */
    boolean updateOneParty(String access_token, Department department);

}
