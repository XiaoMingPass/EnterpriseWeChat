package com.ys.wx.service.wx;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.ys.wx.model.wx.response.PartyListResponse.Department;
import com.ys.wx.model.wx.response.UserDetailsResponse.UserlistBean;

/**
 * Title :
 * Description :
 * Author : Jerry xu    date : 2017/1/11
 * Update :             date :
 * Version : 1.0.0
 */
public interface IWxServiceClient {

	/**
	 * 上传文件，增量覆盖（可以使部门或者人员的文件）
	 * @param file
	 * @Description:
	 * @author wangzequan 2017 上午9:46:17
	 */
    void uploadFile(final File file);
    
    /**
     * 获取token
     * @return
     * @Description:
     * @author wangzequan 2017 上午9:46:07
     */
    public  String getToken() ;

    /**
     * 得到部门id所下属的整个部门树的列表
     * @param access_token
     * @param id
     * @return
     * @Description:
     * @author wangzequan 2017 上午9:45:38
     */
    public ArrayList<Department> getParty(String access_token,int id);
    
    /**
     * 获取部门下的所有人员
     * @param access_token
     * @param department_id
     * @return
     * @Description:
     * @author wangzequan 2017 上午9:48:23
     */
    public   List<UserlistBean> getUserDetails(String access_token, int department_id) ;
    
    /**
     * 添加人员
     * @param access_token
     * @param userlistbean
     * @return
     * @Description:
     * @author wangzequan 2017 上午9:48:40
     */
    public boolean addUser(String access_token, UserlistBean userlistbean);
    
    /**
     * 依据ID删除部门
     * @param access_token
     * @param id
     * @return
     * @Description:
     * @author wangzequan 2017 上午9:43:08
     */
    public boolean deleteParty(String access_token, String id);
    /**
     * 依据人员id删除人员
     * @param access_token
     * @param userid
     * @return
     * @Description:
     * @author wangzequan 2017 上午9:43:37
     */
    public  List<UserlistBean> deleteUser(String access_token, String userid);
    /**
     * 更新用户
     * @param access_token
     * @param userlistbean
     * @return
     * @Description:
     * @author wangzequan 2017 上午9:43:55
     */
    public boolean updateUser(String access_token, UserlistBean userlistbean) ;
    /**
     * 通过文件方式增量更新人员
     * @param file
     * @Description:
     * @author wangzequan 2017 上午9:44:42
     */
    public void uploadFileSysUser(final File file);
    /**
     * 创建一个部门
     * @param access_token
     * @param department
     * @return
     * @Description:
     * @author wangzequan 2017 上午9:45:08
     */
    public boolean  createParty(String access_token , Department department);
    /**
     * 更新一个部门
     * @param access_token
     * @param department
     * @return
     * @Description:
     * @author wangzequan 2017 上午9:45:23
     */
    public  boolean updateOneParty(String access_token,   Department department) ;

}
