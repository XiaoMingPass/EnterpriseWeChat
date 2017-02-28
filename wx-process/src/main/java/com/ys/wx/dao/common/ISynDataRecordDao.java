package com.ys.wx.dao.common;

import com.ys.wx.model.common.SynDataRecord;
import com.ys.wx.utils.pager.PagerModel;
import com.ys.wx.utils.pager.Query;

import java.util.List;

/**
 * Title: 数据同步记录Dao接口
 * Description:
 * Author: Ceaser wang
 * Since: 2016-11-23 10:43:57
 * Version:1.1.0
 * Copyright: (c) 二龙湖基地组织  2015 ~ 2016 版权所有
 */
public interface ISynDataRecordDao {

    /**
     * 通过id得到数据同步记录SynDataRecord
     *
     * @param id
     * @return
     * @throws Exception
     */
    SynDataRecord getSynDataRecordById(String id) throws Exception;

    /**
     * 通过ids批量得到数据同步记录SynDataRecord
     *
     * @param ids 如："'1','2','3','4'..."
     * @return
     * @throws Exception
     * @Description:
     */
    List<SynDataRecord> getSynDataRecordByIds(String ids) throws Exception;

    /**
     * 通过ids批量得到数据同步记录SynDataRecord
     *
     * @param ids
     * @return
     * @throws Exception
     * @Description:
     */
    List<SynDataRecord> getSynDataRecordByIdsList(List<String> ids) throws Exception;

    /**
     * 得到所有数据同步记录SynDataRecord
     *
     * @param synDataRecord
     * @return
     * @throws Exception
     * @Description:
     */
    List<SynDataRecord> getAll(SynDataRecord synDataRecord) throws Exception;

    /**
     * 分页查询数据同步记录SynDataRecord
     *
     * @param synDataRecord
     * @param query
     * @return
     * @throws Exception
     * @Description:
     */
    PagerModel<SynDataRecord> getPagerModelByQuery(SynDataRecord synDataRecord, Query query) throws Exception;

    /**
     * 查询记录数
     *
     * @param synDataRecord
     * @return
     * @throws Exception
     * @Description:
     */
    int getByPageCount(SynDataRecord synDataRecord) throws Exception;

    /**
     * 添加数据同步记录SynDataRecord
     *
     * @param synDataRecord
     * @throws Exception
     * @Description:
     */
    void insertSynDataRecord(SynDataRecord synDataRecord) throws Exception;

    /**
     * 批量添加数据同步记录SynDataRecord
     *
     * @param synDataRecords
     * @throws Exception
     * @Description:
     */
    void insertSynDataRecordBatch(List<SynDataRecord> synDataRecords) throws Exception;

    /**
     * 通过id删除数据同步记录SynDataRecord
     *
     * @param id
     * @throws Exception
     * @Description:
     */
    void delSynDataRecordById(String id) throws Exception;

    /**
     * 通过id批量删除数据同步记录SynDataRecord
     *
     * @param ids 如："'1','2','3','4'..."
     * @throws Exception
     * @Description:
     */
    void delSynDataRecordByIds(String ids) throws Exception;

    /**
     * 通过id批量删除数据同步记录SynDataRecord
     *
     * @param ids
     * @throws Exception
     * @Description:
     */
    void delSynDataRecordByIdsList(List<String> ids) throws Exception;

    /**
     * 通过id修改数据同步记录SynDataRecord
     *
     * @param synDataRecord
     * @throws Exception
     * @Description:
     */
    int updateSynDataRecord(SynDataRecord synDataRecord) throws Exception;

    /**
     * 通过ids批量修改数据同步记录SynDataRecord
     *
     * @param ids           如："'1','2','3','4'..."
     * @param synDataRecord
     * @throws Exception
     * @Description:
     */
    int updateSynDataRecordByIds(String ids, SynDataRecord synDataRecord) throws Exception;

    /**
     * 通过ids批量修改数据同步记录SynDataRecord
     *
     * @param ids
     * @param synDataRecord
     * @throws Exception
     * @Description:
     */
    int updateSynDataRecordByIdsList(List<String> ids, SynDataRecord synDataRecord) throws Exception;

    /**
     * 通过id批量修改数据同步记录SynDataRecord
     *
     * @param synDataRecords
     * @throws Exception
     * @Description:
     */
    int updateSynDataRecordList(List<SynDataRecord> synDataRecords) throws Exception;

    /**
     * 得到最大结束时间
     *
     * @param synDataRecord
     * @return
     * @throws Exception
     * @Description:
     */
    String getSynDataRecordMaxEndTime(SynDataRecord synDataRecord) throws Exception;

    /**
     * 通过类型修改时间戳
     *
     * @param synDataRecord
     * @throws Exception
     * @Description:
     */
    void updateSynDataRecordByType(SynDataRecord synDataRecord) throws Exception;
}
