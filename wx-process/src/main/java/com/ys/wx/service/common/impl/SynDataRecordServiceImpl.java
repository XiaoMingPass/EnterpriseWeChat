package com.ys.wx.service.common.impl;

import com.ys.wx.common.StringTools;
import com.ys.wx.dao.common.ISynDataRecordDao;
import com.ys.wx.model.common.SynDataRecord;
import com.ys.wx.service.common.ISynDataRecordService;
import com.ys.wx.utils.pager.PagerModel;
import com.ys.wx.utils.pager.Query;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Title: 数据同步记录Service实现
 * Description:
 * Author: Ceaser wang
 * Since: 2016-11-23 10:43:57
 * Version:1.1.0
 * Copyright: (c) 二龙湖基地组织  2016 ~ 2017 版权所有
 */
@Service
public class SynDataRecordServiceImpl implements ISynDataRecordService {

    @Resource
    private ISynDataRecordDao synDataRecordDao;

    public SynDataRecord getSynDataRecordById(String id) throws Exception {
        return StringUtils.isNotBlank(id) ? this.synDataRecordDao.getSynDataRecordById(id.trim()) : null;
    }

    public List<SynDataRecord> getSynDataRecordByIds(String ids) throws Exception {
        ids = StringTools.convertString(ids);
        return StringUtils.isNotBlank(ids) ? this.synDataRecordDao.getSynDataRecordByIds(ids) : null;
    }

    public List<SynDataRecord> getSynDataRecordByIdsList(List<String> ids) throws Exception {
        return CollectionUtils.isNotEmpty(ids) ? this.synDataRecordDao.getSynDataRecordByIdsList(ids) : null;
    }

    public List<SynDataRecord> getAll(SynDataRecord synDataRecord) throws Exception {
        return null != synDataRecord ? this.synDataRecordDao.getAll(synDataRecord) : null;
    }

    public PagerModel<SynDataRecord> getPagerModelByQuery(SynDataRecord synDataRecord, Query query)
            throws Exception {
        return (null != synDataRecord && null != query) ? this.synDataRecordDao.getPagerModelByQuery(synDataRecord, query) : null;
    }

    public int getByPageCount(SynDataRecord synDataRecord) throws Exception {
        return (null != synDataRecord) ? this.synDataRecordDao.getByPageCount(synDataRecord) : 0;
    }

    public String getSynDataRecordMaxEndTime(SynDataRecord synDataRecord) throws Exception {
        return (null != synDataRecord) ? this.synDataRecordDao.getSynDataRecordMaxEndTime(synDataRecord) : "";
    }

    public void insertSynDataRecord(SynDataRecord synDataRecord) throws Exception {
        this.synDataRecordDao.insertSynDataRecord(synDataRecord);
    }

    public void insertSynDataRecordBatch(List<SynDataRecord> synDataRecords) throws Exception {
        this.synDataRecordDao.insertSynDataRecordBatch(synDataRecords);
    }

    public void delSynDataRecordById(String id) throws Exception {
        if (StringUtils.isNotBlank(id)) {
            this.synDataRecordDao.delSynDataRecordById(id.trim());
        }
    }

    public void delSynDataRecordByIds(String ids) throws Exception {
        ids = StringTools.convertString(ids);
        if (StringUtils.isNotBlank(ids)) {
            this.synDataRecordDao.delSynDataRecordByIds(ids);
        }
    }

    public void delSynDataRecordByIdsList(List<String> ids) throws Exception {
        if (CollectionUtils.isNotEmpty(ids)) {
            this.synDataRecordDao.delSynDataRecordByIdsList(ids);
        }
    }

    public int updateSynDataRecord(SynDataRecord synDataRecord) throws Exception {
        return this.synDataRecordDao.updateSynDataRecord(synDataRecord);
    }

    public int updateSynDataRecordByIds(String ids, SynDataRecord synDataRecord) throws Exception {
        return this.synDataRecordDao.updateSynDataRecordByIds(ids, synDataRecord);
    }

    public int updateSynDataRecordByIdsList(List<String> ids, SynDataRecord synDataRecord) throws Exception {
        return this.synDataRecordDao.updateSynDataRecordByIdsList(ids, synDataRecord);
    }

    public int updateSynDataRecordList(List<SynDataRecord> synDataRecords) throws Exception {
        return this.synDataRecordDao.updateSynDataRecordList(synDataRecords);
    }

    public void updateSynDataRecordByType(SynDataRecord synDataRecord) throws Exception {
        this.synDataRecordDao.updateSynDataRecordByType(synDataRecord);
    }
    /***
     * 将"1,2,3,4,5..."这种形式的字符串转成"'1','2','3','4'..."这种形式
     * @param strs
     * @return private String convertString(String strs) {
    if (StringUtils.isNotBlank(strs)) {
    String[] idStrs = strs.trim().split(",");
    if (null != idStrs && idStrs.length > 0) {
    StringBuffer sbf = new StringBuffer("");
    for (String str : idStrs) {
    if (StringUtils.isNotBlank(str)) {
    sbf.append("'").append(str.trim()).append("'").append(",");
    }
    }
    if (sbf.length() > 0) {
    sbf = sbf.deleteCharAt(sbf.length() - 1);
    return sbf.toString();
    }
    }
    }
    return "";
    }*/

    /***
     * 将"1,2,3,4,5..."这种形式的字符串转成List<String> 集合
     *
     * @param strs
     * @return private List<String> converStringToList(String strs) {
    if (StringUtils.isNotBlank(strs)) {
    String[] idStrs = strs.trim().split(",");
    if (null != idStrs && idStrs.length > 0) {
    List<String> strsList = new ArrayList<String>();
    for (String str : idStrs) {
    if (StringUtils.isNotBlank(str)) {
    strsList.add(str.trim());
    }
    }
    if (strsList.size() > 0) {
    return strsList;
    }
    }
    }
    return null;
    }
     */


}

