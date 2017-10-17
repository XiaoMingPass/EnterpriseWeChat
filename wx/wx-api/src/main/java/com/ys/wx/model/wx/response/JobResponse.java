package com.ys.wx.model.wx.response;

import java.io.Serializable;
import java.util.List;

/**
 * Title :
 * Description :
 * Author : Jerry xu    date : 2017/4/12
 * Update :             date :
 * Version : 1.0.0
 */
public class JobResponse implements Serializable {

    /**
     * {
     * "errcode": 0,
     * "errmsg": "ok",
     * "status": 1,
     * "type": "replace_user",
     * "total": 3,
     * "percentage": 33,
     * "remaintime": 1,
     * "result": [{},{}]
     * }
     */

    public int             errcode;      //请求错误码，0表示成功
    public String          errmsg;       //请求错误信息
    public int             status;       //任务状态，整型，1表示任务开始，2表示任务进行中，3表示任务已完成
    public String          type;         //操作类型:1. sync_user(增量更新成员)2. replace_user(全量覆盖成员)3. replace_party(全量覆盖部门)
    public int             total;        //任务运行总条数
    public int             percentage;   //目前运行百分比，当任务完成时为100
    public int             remaintime;   //预估剩余时间（单位：分钟），当任务完成时为0
    public List<AttrsBean> result;

    public class AttrsBean {
        public String action;           //sync_user、replace_user操作类型（按位或）：1 表示修改，2 表示新增;replace_party操作类型（按位或）：1 新建部门 ，2 更改部门名称， 4 移动部门， 8 修改部门排序
        public String partyid;          //部门ID
        public String userid;           //成员UserID。对应管理端的帐号
        public String errcode;          //该(成员/部门)对应操作的结果错误码
        public String errmsg;           //错误信息，例如无权限错误，键值冲突，格式错误等
    }
}
