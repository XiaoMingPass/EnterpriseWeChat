package com.ys.wx.dao.common.impl;

import com.ys.wx.common.StringTools;
import com.ys.wx.dao.common.ISynDataRecordDao;
import com.ys.wx.model.common.SynDataRecord;
import com.ys.wx.utils.MybatisTemplate;
import com.ys.wx.utils.UUIDGenerator;
import com.ys.wx.utils.pager.PagerModel;
import com.ys.wx.utils.pager.Query;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Title: 数据同步记录Dao实现
 * Description:
 * Author: Ceaser wang
 * Since: 2016-11-23 10:43:57
 * Version:1.1.0
 * Copyright: (c) 二龙湖基地组织  2015 ~ 2016 版权所有
 */
@Repository
public class SynDataRecordDaoImpl extends MybatisTemplate implements ISynDataRecordDao {

    public SynDataRecord getSynDataRecordById(String id) throws Exception {
        return (SynDataRecord) this.selectOne("SynDataRecordXML.getSynDataRecordById", id);
    }

    public List<SynDataRecord> getSynDataRecordByIds(String ids) throws Exception {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("ids", ids);
        return this.selectList("SynDataRecordXML.getSynDataRecordByIds", params);
    }


    public List<SynDataRecord> getSynDataRecordByIdsList(List<String> ids) throws Exception {
        return this.selectList("SynDataRecordXML.getSynDataRecordByIdsList", ids);
    }


    public List<SynDataRecord> getAll(SynDataRecord synDataRecord) throws Exception {
        return this.selectList("SynDataRecordXML.getAll", synDataRecord);
    }


    public PagerModel<SynDataRecord> getPagerModelByQuery(SynDataRecord synDataRecord, Query query)
            throws Exception {
        return this.getPagerModelByQuery(synDataRecord, query, "SynDataRecordXML.getPagerModelByQuery");
    }


    public int getByPageCount(SynDataRecord synDataRecord) throws Exception {
        return this.selectOne("SynDataRecordXML.getByPageCount", synDataRecord);
    }


    public String getSynDataRecordMaxEndTime(SynDataRecord synDataRecord) throws Exception {
        return this.selectOne("SynDataRecordXML.getSynDataRecordMaxEndTime", synDataRecord);
    }


    public void insertSynDataRecord(SynDataRecord synDataRecord) throws Exception {
        if (null != synDataRecord) {
            synDataRecord.setId(UUIDGenerator.generate());
            synDataRecord.setOperateTime(new Date());
            this.insert("SynDataRecordXML.insertSynDataRecord", synDataRecord);
        }
    }


    public void insertSynDataRecordBatch(List<SynDataRecord> synDataRecords) throws Exception {
        if (CollectionUtils.isNotEmpty(synDataRecords)) {
            for (SynDataRecord synDataRecord : synDataRecords) {
                if (null != synDataRecord) {
                    synDataRecord.setId(UUIDGenerator.generate());
                    synDataRecord.setOperateTime(new Date());
                }
            }
            this.insert("SynDataRecordXML.insertSynDataRecordBatch", synDataRecords);
        }
    }


    public void delSynDataRecordById(String id) throws Exception {
        this.delete("SynDataRecordXML.delSynDataRecordById", id);
    }


    public void delSynDataRecordByIds(String ids) throws Exception {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("ids", ids);
        this.delete("SynDataRecordXML.delSynDataRecordByIds", params);
    }


    public void delSynDataRecordByIdsList(List<String> ids) throws Exception {
        this.delete("SynDataRecordXML.delSynDataRecordByIdsList", ids);
    }


    public int updateSynDataRecord(SynDataRecord synDataRecord) throws Exception {
        if (null != synDataRecord) {
            return this.update("SynDataRecordXML.updateSynDataRecord", synDataRecord);
        } else {
            return 0;
        }
    }


    public int updateSynDataRecordByIds(String ids, SynDataRecord synDataRecord) throws Exception {
        ids = StringTools.convertString(ids);
        if (StringUtils.isNotBlank(ids) && null != synDataRecord) {
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("ids", ids);
            params.put("synDataRecord", synDataRecord);
            return this.update("SynDataRecordXML.updateSynDataRecordByIds", params);
        } else {
            return 0;
        }

    }


    public int updateSynDataRecordByIdsList(List<String> ids, SynDataRecord synDataRecord) throws Exception {
        if (CollectionUtils.isNotEmpty(ids) && null != synDataRecord) {
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("ids", ids);
            params.put("synDataRecord", synDataRecord);
            return this.update("SynDataRecordXML.updateSynDataRecordByIdsList", params);
        } else {
            return 0;
        }
    }


    public int updateSynDataRecordList(List<SynDataRecord> synDataRecords) throws Exception {
        if (CollectionUtils.isNotEmpty(synDataRecords)) {
            return this.update("SynDataRecordXML.updateSynDataRecordList", synDataRecords);
        } else {
            return 0;
        }
    }

    public void updateSynDataRecordByType(SynDataRecord synDataRecord) throws Exception {
        this.update("SynDataRecordXML.updateSynDataRecordByType", synDataRecord);
    }

}

