package com.ys.wx.service.wx;

import com.mhome.tools.common.ReadProperty;
import com.mhome.tools.common.SpringContextHolder;
import com.ys.wx.constant.WxConstant;
import com.ys.wx.model.wx.request.PartyAndUserRequest;
import com.ys.wx.model.wx.response.*;
import com.ys.wx.model.wx.response.PartyListResponse.Department;
import com.ys.wx.model.wx.response.UserDetailsResponse.UserlistBean;
import com.ys.wx.model.wx.response.UserDetailsResponse.UserlistBean.ExtattrBean.AttrsBean;
import com.ys.wx.utils.ApiService;
import com.ys.wx.utils.CommUtils;
import com.ys.wx.utils.Retrofit.RetrofitManager;
import com.ys.wx.vo.Person;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import org.apache.commons.collections.CollectionUtils;
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
 * Title :
 * Description :
 * Author : Jerry xu    date : 2016/12/29
 * Update :             date :
 * Version : 1.0.0
 */
@Service
public class WxServiceClient implements IWxServiceClient {

    //添加一个日志器
    private static final Logger       logger       = LoggerFactory.getLogger(WxServiceClient.class);
    private final static ReadProperty readProperty = SpringContextHolder.getBean("readProperty");

    //部门表上传成功标识
    private static boolean isSuccessParty = false;

    private String access_token;
    private String type = "file";
    private String              mediaId;
    private PartyAndUserRequest request;
    private long                tokenTime; //记录获取token的时间

    public static boolean isIsSuccessParty() {
        return isSuccessParty;
    }

    public static void setIsSuccessParty(boolean isSuccessParty) {
        WxServiceClient.isSuccessParty = isSuccessParty;
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
            logger.error("WxServiceClient-getToken:" + e);
            e.printStackTrace();
        }
        return access_token;
    }

    /**
     * 上传文件
     */
    public void uploadFile(final File file) {
        try {
            if (!verifyTokenValid(tokenTime, access_token)) {
                access_token = getToken();
            }
            // 获取上传文件名称
            String fileName = file.getName();
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            MultipartBody.Part body = MultipartBody.Part.createFormData("media", fileName, requestFile);

            Call<MultiResponse> call = RetrofitManager.getInstance().create(ApiService.class)
                    .uploadFile(access_token, type, body);

            // 执行同步请求
            Response<MultiResponse> response = call.execute();

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
                    logger.info("media_id : "+mediaId);
                    logger.info("access_token : "+access_token);
                    // 更新party表
                    updateParty(access_token, mediaId);
                } else if (fileName.matches("user\\w+.csv")) {//上传user表
                    logger.info("上传user表成功。。。");
                    // 获取到成功上传文件返回media_id;
                    mediaId = response.body().media_id;
                    logger.info("media_id : "+mediaId);
                    logger.info("access_token : "+access_token);
                    // 更新User表
                    updateUser(access_token, mediaId);
                } else {
                    logger.info("文件错误，文件 ： " + file.getName());
                }
            }
        } catch (Exception e) {
            logger.error("WxServiceClient-uploadFile:" + e);
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
    public boolean uploadFileSysUser(final File file) {
        try {
            if (!verifyTokenValid(tokenTime, access_token)) {
                access_token = getToken();
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
                    return updateParty(access_token, mediaId);
                } else if (fileName.matches("user\\w+.csv")) {//上传user表
                    logger.info("上传user表成功。。。");
                    // 获取到成功上传文件返回media_id;
                    mediaId = response.body().media_id;
                    // 更新User表
                    return sysUser(access_token, mediaId);
                } else {
                    logger.info("文件错误，文件 ： " + file.getName());
                    return false;
                }
            }
        } catch (Exception e) {
            logger.error("WxServiceClient-uploadFileSysUser:" + e);
            e.printStackTrace();
        } /*finally {
            if (null != file && file.isFile() && file.exists()) {
                boolean isDelete = file.delete();
                if (!isDelete) {
                    logger.info("上传后置文件删除失败，文件 ： " + file.getName());
                }
            }
        }*/
        return true;
        
    }


    /**
     * 覆盖更新Party表
     *
     * @param access_token 调用接口凭证
     * @param mediaId      上传的csv文件的media_id
     */
    private boolean  updateParty(String access_token, String mediaId) {
        request = new PartyAndUserRequest();
        request.media_id = mediaId;
        
        logger.info("覆盖更新Party表-参数：mediaId ： "+mediaId);
        
        try {
            Call<PartyAndUserResponse> call = RetrofitManager.getInstance()
                    .create(ApiService.class).updateParty(access_token, request);
            Response<PartyAndUserResponse> response = call.execute();

            if (null == response.body()) {
                logger.info("覆盖更新组织结构失败。。。"+response);
                return false;
            } else if (response.body().errcode == 0) {
                logger.info("覆盖更新组织结构成功。。。"+response);
                // 标识部门表更新成功
                isSuccessParty = true;
                return true;
            } else {
                logger.info("Error;Error;Error;>>>>>>>>>>>>>>>覆盖更新组织结构出错。。。错误码:"+response.body().errcode);
                return false;
            }
        } catch (IOException e) {
            logger.error("WxServiceClient-updateParty:" + e);
            e.printStackTrace();
        }
        return true;
    }

    /**
     * 更新部门
     *
     * @param access_token 调用接口凭证
     * @param department   部门实体
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
                logger.info("Error;>>>>>>>>>>>>>>>更新组织结构出错。。。errorcode: " + response.body().errcode + " errorMsg  : " + response.body().errmsg);
                return false;
            }
        } catch (IOException e) {
            logger.error("WxServiceClient-updateOneParty:" + e);
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 删除部门
     *
     * @param access_token 调用接口凭证
     * @param id           部门id
     */
    public boolean deleteParty(String access_token, String id) {
        try {
            if (!verifyTokenValid(tokenTime, access_token)) {
                access_token = getToken();
            }
            Call<PartyListResponse> call = RetrofitManager.getInstance()
                    .create(ApiService.class).deletePartyList(access_token, Integer.valueOf(id));
            Response<PartyListResponse> response = call.execute();

            if (null == response.body()) {
                logger.info("删除组织结构失败【无返回数据】。。。");
                return false;
            } else if (response.body().errcode == 0) {
                //logger.info("删除更新组织结构成功。。。");\
                logger.info("Error >>>>>>  id :  " + id +"  error code : "+response.body().errcode+ " error info : " + response.body().errmsg);
                return true;
            } else {
                logger.info("Error >>>>>>  id :  " + id +"  error code : "+response.body().errcode + " error info : " + response.body().errmsg);
                //logger.info("Error;Error;Error;>>>>>>>>>>>>>>>删除组织结构失败。。。");
                return false;
            }
        } catch (IOException e) {
            logger.error("WxServiceClient-deleteParty:" + e);
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 覆盖更新User表
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
            logger.error("WxServiceClient-updateUser:" + e);
            e.printStackTrace();
        }
    }

    /**
     * 更新User表
     *
     * @param access_token 调用接口凭证
     * @param mediaId      上传的csv文件的media_id
     */
    private boolean  sysUser(String access_token, String mediaId) {
        request = new PartyAndUserRequest();

        request.media_id = mediaId;

        try {
            Call<PartyAndUserResponse> call = RetrofitManager.getInstance()
                    .create(ApiService.class).spikeUser(access_token, request);
            Response<PartyAndUserResponse> response = call.execute();

            if (null == response.body()) {
                logger.info("更新人员信息失败。。。");
                return false;
            } else if (response.body().errcode == 0) {
                logger.info("更新人员信息成功。。。");
                logger.info("更新信息成功返回:errmsg=" + response.body().errmsg);
                return true;
            } else {
                logger.info("Error;Error;Error;>>>>>>>>>>>>>>>更新人员信息出错。。。");
                return false;
            }
        } catch (IOException e) {
            logger.error("WxServiceClient-sysUser:" + e);
            e.printStackTrace();
        }
        return true;
    }

    /**
     * 添加User
     *
     * @param access_token 调用接口凭证
     * @param userlistbean 人员实体
     */
    public boolean addUser(String access_token, UserlistBean userlistbean) {
        try {
            if (!verifyTokenValid(tokenTime, access_token)) {
                access_token = getToken();
            }
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
            logger.error("WxServiceClient-addUser:" + e);
            e.printStackTrace();
        }
        return false;
    }
    /**
     * 添加User
     *
     * @param access_token 调用接口凭证
     * @param userlistbean 人员实体
     */
    public boolean addUser(String access_token, Person itemperson) {
    	try {
    		if (!verifyTokenValid(tokenTime, access_token)) {
    			access_token = getToken();
    		}
    		
    		
    		UserlistBean userlistbean = new UserDetailsResponse().new UserlistBean();
            if (null != itemperson) {
                if (null == userlistbean.department) {
                	userlistbean.department = new ArrayList<Integer>();
                }
                userlistbean.userid = itemperson.getIdcard();
                userlistbean.name = itemperson.getName();
                userlistbean.department.add(Integer.valueOf(itemperson.getDeptCode()));
                userlistbean.email = itemperson.getEmail();
                userlistbean.mobile = itemperson.getMobile();
                userlistbean.weixinid = itemperson.getWeixin();
                if (null != userlistbean.extattr && CollectionUtils.isNotEmpty(userlistbean.extattr.attrs)) {
                    List<AttrsBean> attrsBeans = userlistbean.extattr.attrs;
                    // 所在办公地 全称"; 简称"; 排序号"; 是否可见"; 固定电话"; 座机短号"; 工号"; 手机短号";
                    for (AttrsBean attrsBean : attrsBeans) {
                        if ("所在办公地".equals(attrsBean.name)) {
                            attrsBean.name = itemperson.getOfficeAddr();
                        } else if ("全称".equals(attrsBean.name)) {
                            attrsBean.name = itemperson.getFullName();
                        } else if ("简称".equals(attrsBean.name)) {
                            attrsBean.name = itemperson.getShortName();
                        } else if ("排序号".equals(attrsBean.name)) {
                            attrsBean.name = itemperson.getOrderNo() + "";
                        } else if ("是否可见".equals(attrsBean.name)) {
                            attrsBean.name = itemperson.getIsVisable();
                        } else if ("固定电话".equals(attrsBean.name)) {
                            attrsBean.name = itemperson.getPhone();
                        } else if ("座机短号".equals(attrsBean.name)) {
                            attrsBean.name = itemperson.getShortPhone();
                        } else if ("工号".equals(attrsBean.name)) {
                            attrsBean.name = itemperson.getNo();
                            
                        } else if ("手机短号".equals(attrsBean.name)) {
                            attrsBean.name = itemperson.getShortMobile();
                        }else if("职务".equals(attrsBean.name)){
                        	 attrsBean.name = itemperson.getPosition();
                        }else if("性别".equals(attrsBean.name)){
                        	if(CommUtils.isNotEmpty(itemperson.getPosition())){
                        		String sex = itemperson.getPosition();
                        		attrsBean.name = "1".equals(sex)?"男":"女";
                        	}
                       }
                    }
                }
            }  
    		
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
    		logger.error("WxServiceClient-addUser:" + e);
    		e.printStackTrace();
    	}
    	return false;
    }

    
    
    /**
     * 更新User
     *
     * @param access_token 调用接口凭证
     * @param userlistbean 更新用户信息实体
     */
    public boolean updateUser(String access_token, UserlistBean userlistbean) {
        try {
            if (!verifyTokenValid(tokenTime, access_token)) {
                access_token = getToken();
            }
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
            logger.error("WxServiceClient-updateUser:" + e);
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * 更新User
     *
     * @param access_token 调用接口凭证
     * @param userlistbean 更新用户信息实体
     */
    public boolean updateUser(String access_token, Person itemperson) {
    	try {
    		if (!verifyTokenValid(tokenTime, access_token)) {
    			access_token = getToken();
    		}
    		
    		UserlistBean userlistbean = new UserDetailsResponse().new UserlistBean();
            if (null != itemperson) {
                if (null == userlistbean.department) {
                	userlistbean.department = new ArrayList<Integer>();
                }
                userlistbean.userid = itemperson.getIdcard();
                userlistbean.department.add(Integer.valueOf(itemperson.getDeptCode()));
                userlistbean.email = itemperson.getEmail();
                userlistbean.mobile = itemperson.getMobile();
                userlistbean.weixinid = itemperson.getWeixin();
                if (null != userlistbean.extattr && CollectionUtils.isNotEmpty(userlistbean.extattr.attrs)) {
                    List<AttrsBean> attrsBeans = userlistbean.extattr.attrs;
                    // 所在办公地 全称"; 简称"; 排序号"; 是否可见"; 固定电话"; 座机短号"; 工号"; 手机短号";
                    for (AttrsBean attrsBean : attrsBeans) {
                        if ("所在办公地".equals(attrsBean.name)) {
                            attrsBean.name = itemperson.getOfficeAddr();
                        } else if ("全称".equals(attrsBean.name)) {
                            attrsBean.name = itemperson.getFullName();
                        } else if ("简称".equals(attrsBean.name)) {
                            attrsBean.name = itemperson.getShortName();
                        } else if ("排序号".equals(attrsBean.name)) {
                            attrsBean.name = itemperson.getOrderNo() + "";
                        } else if ("是否可见".equals(attrsBean.name)) {
                            attrsBean.name = itemperson.getIsVisable();
                        } else if ("固定电话".equals(attrsBean.name)) {
                            attrsBean.name = itemperson.getPhone();
                        } else if ("座机短号".equals(attrsBean.name)) {
                            attrsBean.name = itemperson.getShortPhone();
                        } else if ("工号".equals(attrsBean.name)) {
                            attrsBean.name = itemperson.getNo();
                        } else if ("手机短号".equals(attrsBean.name)) {
                            attrsBean.name = itemperson.getShortMobile();
                        }else if("职务".equals(attrsBean.name)){
                       	 	attrsBean.name = itemperson.getPosition();
                       }else if("性别".equals(attrsBean.name)){
                       	if(CommUtils.isNotEmpty(itemperson.getPosition())){
                       		String sex = itemperson.getPosition();
                       		attrsBean.name = "1".equals(sex)?"男":"女";
                       	}
                      }
                    }
                }
                
                Call<PartyAndUserResponse> call = RetrofitManager.getInstance().create(ApiService.class).updateUser(access_token, userlistbean);
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
            } 
    		
    	} catch (IOException e) {
    		logger.error("WxServiceClient-updateUser:" + e);
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
            if (!verifyTokenValid(tokenTime, access_token)) {
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
            } else {
                logger.info("获取组织列表数据异常。。。");
            }
        } catch (IOException e) {
            logger.error("WxServiceClient-getParty:" + e);
            e.printStackTrace();
        }
        return departments;
    }

    /**
     * 创建部门
     *
     * @param access_token 调用接口凭证
     * @param department   部门实体
     * @return
     */
    public boolean createParty(String access_token, Department department) {
        try {
            if (!verifyTokenValid(tokenTime, access_token)) {
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
            } else {
                logger.info("创建部门组织列表数据异常。。。errorCode : " + response.body().errcode + " errormsg:" + response.body().errmsg);
                return false;
            }
        } catch (IOException e) {
            logger.error("WxServiceClient-createParty:" + e);
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
        List<UserlistBean> userList = null;
        try {
            if (!verifyTokenValid(tokenTime, access_token)) {
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
                userList = response.body().userlist;
                logger.info("获取部门成员数据成功。。。");
            } else {
                logger.info("获取部门成员数据异常。。。");
            }
        } catch (IOException e) {
            logger.error("WxServiceClient-getUserDetails:" + e);
            e.printStackTrace();
        }
        return userList;
    }


    /**
     * 删除用户
     *
     * @param access_token 调用接口凭证
     * @param userid       用户id
     */
    public List<UserlistBean> deleteUser(String access_token, String userid) {
        try {
            if (!verifyTokenValid(tokenTime, access_token)) {
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
                logger.info("删除用户（id:" + userid + "）成功。。。");
                //TODO 获取部门成员详细信息成功

            } else {
                logger.info("执行删除用户异常异常。。。error : " + response.body().errcode + " msg : " + response.body().errmsg);
            }
        } catch (IOException e) {
            logger.error("WxServiceClient-deleteUser:" + e);
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 验证token是否有效
     *
     * @param tokenTime    记录token获取时间
     * @param access_token token值
     */
    public boolean verifyTokenValid(long tokenTime, String access_token) {
        if (access_token != null) {
            long currentTime = new Date().getTime();
            return (currentTime - tokenTime) / 1000 <= 7200;
        }
        return false;
    }

}
