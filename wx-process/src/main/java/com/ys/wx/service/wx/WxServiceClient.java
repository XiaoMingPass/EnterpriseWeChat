package com.ys.wx.service.wx;

import com.ys.wx.common.SpringContextHolder;
import com.ys.wx.constant.WxConstant;
import com.ys.wx.model.wx.request.PartyAndUserRequest;
import com.ys.wx.model.wx.response.*;
import com.ys.wx.model.wx.response.PartyListResponse.Department;
import com.ys.wx.model.wx.response.UserDetailsResponse.UserlistBean;
import com.ys.wx.utils.ApiService;
import com.ys.wx.utils.ReadProperty;
import com.ys.wx.utils.Retrofit.RetrofitManager;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Response;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Title : 微信常用功能实现
 * Description : 部门，人员增删查改
 * Author : Ceaser wang Jerry xu    date : 2016/12/29
 * Update :                         date :
 * Version : 1.0.0
 * Copyright (c) 二龙湖基地组织  2016 ~ 2017 版权所有
 */
@Service
public class WxServiceClient implements IWxServiceClient {

    //添加一个日志器
    private static final Logger       logger       = LoggerFactory.getLogger(WxServiceClient.class);
    private final static ReadProperty readProperty = SpringContextHolder.getBean("readProperty");

    //部门表上传成功标识
    private static boolean isSuccessParty = false;

    private String access_token = "";
    private String type         = "file";
    private String              mediaId;
    private PartyAndUserRequest request;

    //记录获取token的时间
    private long tokenTime;

    public static boolean isIsSuccessParty() {
        return isSuccessParty;
    }


    /**
     * 获取企业微信号AccessToken
     *
     * @return 企业微信号AccessToken
     */
    public String getToken() {
        try {
            // 执行同步请求
            Call<TokenResponse> call = RetrofitManager.getInstance()
                    .create(ApiService.class)
                    .getToken(readProperty.getValue(WxConstant.CORP_ID), readProperty.getValue(WxConstant.SECRET));
            Response<TokenResponse> response = call.execute();
            if (null == response.body()
                    || null == response.body().access_token
                    || null == response.body().expires_in) {
                logger.info("获取access_token异常。。。");
            } else {
                //logger.info("成功获取access_token\n" + response.body().access_token + "\n");
                //记录成功获取token的时间
                tokenTime = new Date().getTime();
                access_token = response.body().access_token;
                System.out.println(">>>>token is :" + response.body().access_token);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return access_token;
    }

    /**
     * 验证token是否有效
     *
     * @return token值是否有效，默认返回true
     */
    private boolean verifyTokenValid(long tokenTime) {

        long currentTime = new Date().getTime();
        return (currentTime - tokenTime) / 1000 <= 7200;

    }

    /**
     * 上传文件
     */
    public void uploadFile(final File file) {
        try {
            if (access_token.equals("") || !verifyTokenValid(tokenTime)) {
                getToken();
            }
            // 获取上传文件名称
            String fileName = file.getName();
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            MultipartBody.Part body = MultipartBody.Part.createFormData("media", fileName, requestFile);

            Call<MultiResponse> call = RetrofitManager.getInstance().create(ApiService.class)
                    .uploadFile(access_token, type, body);

            // 执行同步请求
            Response<MultiResponse> response = call.execute();

            //判断如果是party表，就上传party表，并同步party数据到微信

            if (null == response.body()
                    || null == response.body().type
                    || null == response.body().media_id
                    || null == response.body().created_at) {
                logger.info("上传异常。。。");
            } else {
                if (fileName.matches("party\\w+.csv")) {//上传party表
                    logger.info("上传party表成功。。。");
                    // 获取到成功上传文件返回media_id;
                    mediaId = response.body().media_id;
                    // 更新party表
                    updateParty(access_token, mediaId);
                } else if (fileName.matches("user\\w+.csv")) {//上传user表
                    logger.info("上传user表成功。。。");
                    // 获取到成功上传文件返回media_id;
                    mediaId = response.body().media_id;
                    // 更新User表
                    updateUser(access_token, mediaId);
                } else {
                    logger.info("文件错误，文件 ： " + file.getName());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } /*finally {
            if (null != file && file.isFile() && file.exists()) {
                boolean isDelete = file.delete();
                if (!isDelete) {
                    logger.info("上传后置文件删除失败，文件 ： " + file.getName());
                }
            }
        }*/
    }

    /**
     * 上传文件
     */
    public void uploadFileSysUser(final File file) {
        try {
            if (access_token.equals("") || !verifyTokenValid(tokenTime)) {
                getToken();
            }
            // 获取上传文件名称
            String fileName = file.getName();
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            MultipartBody.Part body = MultipartBody.Part.createFormData("media", fileName, requestFile);

            Call<MultiResponse> call = RetrofitManager.getInstance().create(ApiService.class)
                    .uploadFile(access_token, type, body);

            // 执行同步请求
            Response<MultiResponse> response = call.execute();

            //判断如果是party表，就上传party表，并同步party数据到微信

            if (null == response.body()
                    || null == response.body().type
                    || null == response.body().media_id
                    || null == response.body().created_at) {
                logger.info("上传异常。。。");
            } else {
                if (fileName.matches("party\\w+.csv")) {//上传party表
                    logger.info("上传party表成功。。。");
                    // 获取到成功上传文件返回media_id;
                    mediaId = response.body().media_id;
                    // 更新party表
                    updateParty(access_token, mediaId);
                } else if (fileName.matches("user\\w+.csv")) {//上传user表
                    logger.info("上传user表成功。。。");
                    // 获取到成功上传文件返回media_id;
                    mediaId = response.body().media_id;
                    // 更新User表
                    sysUser(access_token, mediaId);
                } else {
                    logger.info("文件错误，文件 ： " + file.getName());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } /*finally {
            if (null != file && file.isFile() && file.exists()) {
                boolean isDelete = file.delete();
                if (!isDelete) {
                    logger.info("上传后置文件删除失败，文件 ： " + file.getName());
                }
            }
        }*/
    }

    //*********************************************************===部门===*********************************************************//

    /**
     * 覆盖更新Party表(通过CSV文件)
     *
     * @param access_token 调用接口凭证
     * @param mediaId      上传的csv文件的media_id
     */
    private void updateParty(String access_token, String mediaId) {
        request = new PartyAndUserRequest();

        request.media_id = mediaId;

        try {
            Call<PartyAndUserResponse> call = RetrofitManager.getInstance()
                    .create(ApiService.class).updateParty(access_token, request);
            Response<PartyAndUserResponse> response = call.execute();

            if (null == response.body()) {
                logger.info("覆盖更新组织结构失败。。。");
            } else if (response.body().errcode == 0) {
                logger.info("覆盖更新组织结构成功。。。");
                // 标识部门表更新成功
                isSuccessParty = true;
            } else {
                logger.info("Error;Error;Error;>>>>>>>>>>>>>>>覆盖更新组织结构出错。。。");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 更新Party表(通过API)
     *
     * @param access_token 调用接口凭证
     * @param department   部门对象信息
     */
    public boolean updateOneParty(String access_token, Department department) {
        try {
            Call<PartyListResponse> call = RetrofitManager.getInstance()
                    .create(ApiService.class).updateParty(access_token, department);
            Response<PartyListResponse> response = call.execute();

            if (null == response.body()) {
                logger.info("更新组织结构失败。。。");
                return false;
            } else if (response.body().errcode == 0) {
                logger.info("更新组织结构成功。。。");
                // 标识部门表更新成功
                return true;
            } else {
                logger.info("Error;>>>>>>>>>>>>>>>更新组织结构出错。。。error_code: " + response.body().errcode + " errorMsg  : " + response.body().errmsg);
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 删除部门
     *
     * @param access_token 调用接口凭证
     * @param id           部门ID
     */
    public boolean deleteParty(String access_token, String id) {

        try {
            if (access_token.equals("") || !verifyTokenValid(tokenTime)) {
                getToken();
            }

            Call<PartyListResponse> call = RetrofitManager.getInstance()
                    .create(ApiService.class).deletePartyList(access_token, Integer.valueOf(id));
            Response<PartyListResponse> response = call.execute();

            if (null == response.body()) {
                logger.info("删除组织结构失败【无返回数据】。。。");
                return false;
            } else if (response.body().errcode == 0) {
                //logger.info("删除更新组织结构成功。。。");\
                logger.info("Error >>>>>>  id :  " + id + " error info : " + response.body().errmsg);
                return true;
            } else {
                logger.info("Error >>>>>>  id :  " + id + " error info : " + response.body().errmsg);
                //logger.info("Error;Error;Error;>>>>>>>>>>>>>>>删除组织结构失败。。。");
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 获取部门微信服务端list数据
     *
     * @param access_token 调用接口凭证
     */
    public ArrayList<Department> getParty(String access_token, int id) {
        ArrayList<Department> departments = null;
        try {
            if (access_token.equals("") || !verifyTokenValid(tokenTime)) {
                access_token = getToken();
            }
            // 参数id = 0，默认获取最外层的所有组织
            Call<PartyListResponse> call = RetrofitManager.getInstance()
                    .create(ApiService.class).getPartyList(access_token, id);
            Response<PartyListResponse> response = call.execute();

            if (null == response.body()) {
                logger.info("获取组织列表数据失败。。。");
            } else if (response.body().errcode == 0) {
                logger.info("获取组织列表数据成功。。。");
                departments = response.body().department;
                // System.out.println(response.body().department);
                // TODO 获取到组织结构，需要循环出组织中所有的子部门

                // 标识部门表更新成功
            } else {
                logger.info("获取组织列表数据异常。。。");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return departments;
    }

    /**
     * 创建部门
     *
     * @param access_token 调用接口凭证
     */
    public boolean createParty(String access_token, Department department) {

        try {
            if (access_token.equals("") || !verifyTokenValid(tokenTime)) {
                access_token = getToken();
            }
            // 参数id = 0，默认获取最外层的所有组织
            Call<PartyListResponse> call = RetrofitManager.getInstance()
                    .create(ApiService.class).createParty(access_token, department);
            Response<PartyListResponse> response = call.execute();

            if (null == response.body()) {
                logger.info("创建部门组织列表数据失败。。。");
                return false;
            } else if (response.body().errcode == 0) {
                logger.info("创建部门组织列表数据成功。。。");
                return true;
                // System.out.println(response.body().department);
                // TODO 获取到组织结构，需要循环出组织中所有的子部门

                // 标识部门表更新成功
            } else {
                logger.info("创建部门组织列表数据异常。。。errorCode : " + response.body().errcode + " error_msg:" + response.body().errmsg);
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    //*********************************************************===人员===*********************************************************//

    /**
     * 覆盖更新User表(通过CSV文件)
     *
     * @param access_token 调用接口凭证
     * @param mediaId      上传的csv文件的media_id
     */
    private void updateUser(String access_token, String mediaId) {
        request = new PartyAndUserRequest();

        request.media_id = mediaId;

        try {
            Call<PartyAndUserResponse> call = RetrofitManager.getInstance()
                    .create(ApiService.class).updateUser(access_token, request);
            Response<PartyAndUserResponse> response = call.execute();

            if (null == response.body()) {
                logger.info("覆盖人员信息失败。。。");
            } else if (response.body().errcode == 0) {
                logger.info("覆盖人员信息成功。。。");
                logger.info("覆盖信息成功返回:errmsg=" + response.body().errmsg);
            } else {
                logger.info("Error;Error;Error;>>>>>>>>>>>>>>>覆盖人员信息出错。。。");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 更新User表
     *
     * @param access_token 调用接口凭证
     * @param mediaId      上传的csv文件的media_id
     */
    private void sysUser(String access_token, String mediaId) {
        request = new PartyAndUserRequest();

        request.media_id = mediaId;

        try {
            Call<PartyAndUserResponse> call = RetrofitManager.getInstance()
                    .create(ApiService.class).spikeUser(access_token, request);
            Response<PartyAndUserResponse> response = call.execute();

            if (null == response.body()) {
                logger.info("更新人员信息失败。。。");
            } else if (response.body().errcode == 0) {
                logger.info("更新人员信息成功。。。");
                logger.info("更新信息成功返回:errmsg=" + response.body().errmsg);
            } else {
                logger.info("Error;Error;Error;>>>>>>>>>>>>>>>更新人员信息出错。。。");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 添加User
     *
     * @param access_token 调用接口凭证
     * @param userlistbean 人员列表对象信息
     */
    public boolean addUser(String access_token, UserlistBean userlistbean) {
        if (access_token.equals("") || !verifyTokenValid(tokenTime)) {
            access_token = getToken();
        }

        try {
            Call<PartyAndUserResponse> call = RetrofitManager.getInstance()
                    .create(ApiService.class).addUser(access_token, userlistbean);
            Response<PartyAndUserResponse> response = call.execute();

            if (null == response.body()) {
                logger.info("添加人员信息失败。。。");
                return false;
            } else if (response.body().errcode == 0) {
                logger.info("添加人员信息成功。。。");
                logger.info("添加信息成功返回:msg=" + response.body().errmsg);
                return true;
            } else {
                logger.info("Error;Error;Error;>>>>>>>>>>>>>>>添加人员信息出错。。。errcode : " + response.body().errcode + "    errmsg  : " + response.body().errmsg);
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 更新User
     *
     * @param access_token 调用接口凭证
     * @param userlistbean 人员列表对象信息
     */
    public boolean updateUser(String access_token, UserlistBean userlistbean) {
        if (access_token.equals("") || !verifyTokenValid(tokenTime)) {
            access_token = getToken();
        }

        try {
            Call<PartyAndUserResponse> call = RetrofitManager.getInstance()
                    .create(ApiService.class).updateUser(access_token, userlistbean);
            Response<PartyAndUserResponse> response = call.execute();

            if (null == response.body()) {
                logger.info("更新人员信息失败。。。");
                return false;
            } else if (response.body().errcode == 0) {
                logger.info("更新人员信息成功。。。");
                logger.info("更新信息成功返回:msg=" + response.body().errmsg);
                return true;
            } else {
                logger.info("Error;Error;Error;>>>>>>>>>>>>>>>更新人员信息出错。。。errcode : " + response.body().errcode + "    errmsg  : " + response.body().errmsg);
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }


    /**
     * 获取微信服务端部门成员(详情)
     *
     * @param access_token  调用接口凭证
     * @param department_id 获取的部门id
     */
    public List<UserlistBean> getUserDetails(String access_token, int department_id) {
        List<UserlistBean> userlist = null;
        try {
            if (access_token.equals("") || !verifyTokenValid(tokenTime)) {
                access_token = getToken();
            }
            // 参数id = 0，默认获取最外层的所有组织
            Call<UserDetailsResponse> call = RetrofitManager.getInstance()
                    .create(ApiService.class).getUserDetails(access_token, department_id, 1, 0);
            Response<UserDetailsResponse> response = call.execute();

            if (null == response.body()) {
                logger.info("获取部门成员数据失败。。。");
                System.out.println();
            } else if (response.body().errcode == 0) {
                userlist = response.body().userlist;
                logger.info("获取部门成员数据成功。。。");

                //TODO 获取部门成员详细信息成功

            } else {
                logger.info("获取部门成员数据异常。。。");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return userlist;
    }

    /**
     * 获取微信服务端部门成员(详情)
     *
     * @param access_token 调用接口凭证
     * @param userid       获取的部门id
     */
    public List<UserlistBean> deleteUser(String access_token, String userid) {
        List<UserlistBean> userlist = null;
        try {
            if (access_token.equals("") || !verifyTokenValid(tokenTime)) {
                access_token = getToken();
            }
            // 参数id = 0，默认获取最外层的所有组织
            Call<UserDetailsResponse> call = RetrofitManager.getInstance()
                    .create(ApiService.class).getDeleteUser(access_token, userid);
            Response<UserDetailsResponse> response = call.execute();

            if (null == response.body()) {
                logger.info("删除用户失败失败。。。");
                System.out.println();
            } else if (response.body().errcode == 0) {
                userlist = response.body().userlist;
                logger.info("删除用户（id:" + userid + "）成功。。。");
                //TODO 获取部门成员详细信息成功

            } else {
                logger.info("执行删除用户异常异常。。。error : " + response.body().errcode + " msg : " + response.body().errmsg);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return userlist;
    }

}
