package com.ys.wx.utils;

import com.ys.wx.model.wx.request.PartyAndUserRequest;
import com.ys.wx.model.wx.response.MultiResponse;
import com.ys.wx.model.wx.response.PartyAndUserResponse;
import com.ys.wx.model.wx.response.PartyListResponse;
import com.ys.wx.model.wx.response.PartyListResponse.Department;
import com.ys.wx.model.wx.response.TokenResponse;
import com.ys.wx.model.wx.response.UserDetailsResponse;
import com.ys.wx.model.wx.response.UserDetailsResponse.UserlistBean;
import com.ys.wx.model.wx.response.UserListResponse;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * Title : 微信API接口
 * Description :
 * Author : Jerry xu    date : 2017/1/3
 * Update :             date :
 * Version : 1.0.0
 */
public interface ApiService {


    /**
     * 获取AccessToken
     *
     * @param corpid     企业Id
     * @param corpsecret 管理组的凭证密钥
     * @return
     */
    @GET("gettoken")
    Call<TokenResponse> getToken(@Query("corpid") String corpid, @Query("corpsecret") String corpsecret);


    /**
     * 上传临时素材文件
     *
     * @param access_token 调用接口凭证
     * @param type         媒体文件类型，分别有图片（image）、语音（voice）、视频（video），普通文件(file)
     * @param file         form-data中媒体文件标识，有filename、filelength、content-type等信息
     * @return
     */
    @Multipart
    @POST("media/upload")
    Call<MultiResponse> uploadFile(@Query("access_token") String access_token
            , @Query("type") String type
            , @Part MultipartBody.Part file);


    /**
     * 覆盖更新部门Party表
     *
     * @param access_token 调用接口凭证
     * @param request      media_id(是)上传的csv文件的media_id
     *                     callback(否)回调信息。如填写该项则任务完成后，通过callback推送事件给企业。具体请参考应用回调模式中的相应选项
     *                     url(否)企业应用接收企业号推送请求的访问协议和地址，支持http或https协议
     *                     token(否)用于生成签名
     *                     encodingaeskey(否)用于消息体的加密，是AES密钥的Base64编码
     * @return
     */
    @POST("batch/replaceparty")
    Call<PartyAndUserResponse> updateParty(@Query("access_token") String access_token
            , @Body PartyAndUserRequest request);


    /**
     * 覆盖更新用户User表(参数说明 {@see updateParty})
     *
     * @param access_token
     * @param request
     * @return
     */
    @POST("batch/replaceuser")
    Call<PartyAndUserResponse> updateUser(@Query("access_token") String access_token
            , @Body PartyAndUserRequest request);

    
    @POST("user/create")
    public Call<PartyAndUserResponse> addUser(@Query("access_token") String access_token, @Body UserlistBean userlistbean);
    
    @POST("user/update")
    public Call<PartyAndUserResponse> updateUser(@Query("access_token") String access_token, @Body UserlistBean userlistbean);
    

    /**
     * 增量更新用户User表(参数说明 {@see updateParty})
     *
     * @param access_token
     * @param request
     * @return
     */
    @POST("batch/syncuser")
    Call<PartyAndUserResponse> spikeUser(@Query("access_token") String access_token
            , @Body PartyAndUserRequest request);

    /**
     * 验证URL有效性
     *
     * @param msg_signature (是)微信加密签名，msg_signature结合了企业填写的token、请求中的timestamp、nonce参数、加密的消息体
     * @param timestamp     (是)时间戳
     * @param nonce         (是)随机数
     * @param echostr       (首次校验时必带)	加密的随机字符串，以msg_encrypt格式提供。需要解密并返回echostr明文，解密后有random、msg_len、msg、$CorpID四个字段，其中msg即为echostr明文
     * @return
     */
    @POST("wxpush")
    Call<PartyAndUserResponse> verifyURL(@Query("msg_signature") String msg_signature
            , @Query("timestamp") String timestamp
            , @Query("nonce") String nonce
            , @Query("echostr") String echostr);


    /**
     * 获取部门列表
     *
     * @param access_token 调用接口凭证
     * @param id           部门id。获取指定部门及其下的子部门
     * @return
     */
    @GET("department/list")
    Call<PartyListResponse> getPartyList(@Query("access_token") String access_token,
                                         @Query("id") int id);
    /**
     * 创建部门
     * @param access_token 调用接口凭证
     * @param id           部门id。获取指定部门及其下的子部门
     * @return
     */
    @POST("department/create")
    Call<PartyListResponse> createParty(@Query("access_token") String access_token,
    		@Body Department department);
    
    /**
     * 更新部门
     * @param access_token 调用接口凭证
     * @param id           部门id。获取指定部门及其下的子部门
     * @return
     */
    @POST("department/update")
    Call<PartyListResponse> updateParty(@Query("access_token") String access_token,
    		@Body Department department);

    /**
     * 删除部门
     *
     * @param access_token 调用接口凭证
     * @param id           部门id。（注：不能删除根部门；不能删除含有子部门、成员的部门）
     * @return
     */
    @GET("department/delete")
    Call<PartyListResponse> deletePartyList(@Query("access_token") String access_token,
                                        @Query("id") int id);

    /**
     * 获取部门成员(列表)
     *
     * @param access_token  (是)调用接口凭证
     * @param department_id (是)获取的部门id
     * @param fetch_child   (否)1/0：是否递归获取子部门下面的成员
     * @param status        (否)0获取全部成员，1获取已关注成员列表，2获取禁用成员列表，4获取未关注成员列表。status可叠加，未填写则默认为4
     * @return
     */
    @GET("user/simplelist")
    Call<UserListResponse> getUserList(@Query("access_token") String access_token,
                                       @Query("department_id") int department_id,
                                       @Query("fetch_child") int fetch_child,
                                       @Query("status") int status);

    /**
     * 获取部门成员(详情)
     *
     * @param access_token  (是)调用接口凭证
     * @param department_id (是)获取的部门id
     * @param fetch_child   (否)1/0：是否递归获取子部门下面的成员
     * @param status        (否)0获取全部成员，1获取已关注成员列表，2获取禁用成员列表，4获取未关注成员列表。status可叠加,未填写则默认为4
     * @return
     */
    @GET("user/list")
    Call<UserDetailsResponse> getUserDetails(@Query("access_token") String access_token,
                                             @Query("department_id") int department_id,
                                             @Query("fetch_child") int fetch_child,
                                             @Query("status") int status);

    
    /**
     * 删除用户
     * @param access_token (是)调用接口凭证
     * @param userid       用户id
     * @return
     * @Description:
     * @author wangzequan 2017 下午1:37:10
     */
    @GET("user/delete")
    Call<UserDetailsResponse> getDeleteUser(@Query("access_token") String access_token,
    		@Query("userid") String userid);
    
    
    

}
