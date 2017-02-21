package com.ys.wx.service.common;

import java.util.List;

import com.ys.wx.model.common.SynDataRecord;
import com.ys.wx.utils.pager.PagerModel;
import com.ys.wx.utils.pager.Query;




/**
 * 数据同步记录Service接口
 * @author wangzequan
 * @date 2016-11-23 10:43:57
 * @Copyright:Copyright (c) 二龙湖基地组织  2015 ~ 2016 版权所有
 */
public interface ISynDataRecordService {

	/**
	 * 通过id得到数据同步记录SynDataRecord
	 * @param id
	 * @return 
	 * @throws Exception
	 * @Description:
	 */
	public SynDataRecord getSynDataRecordById(String id) throws Exception;

	/**
	 * 通过ids批量得到数据同步记录SynDataRecord
	 * @param ids 如："'1','2','3','4'..."
	 * @return 
	 * @throws Exception
	 * @Description:
	 */
	public List<SynDataRecord> getSynDataRecordByIds(String ids) throws Exception;
	
	/**
	 * 通过ids批量得到数据同步记录SynDataRecord
	 * @param ids 
	 * @return 
	 * @throws Exception
	 * @Description:
	 */
	public List<SynDataRecord> getSynDataRecordByIdsList(List<String> ids) throws Exception;

	/**
	 * 得到所有数据同步记录SynDataRecord
	 * @param synDataRecord
	 * @return 
	 * @throws Exception
	 * @Description:
	 */
	public List<SynDataRecord> getAll(SynDataRecord synDataRecord) throws Exception;

	/**
	 * 分页查询数据同步记录SynDataRecord
	 * @param synDataRecord
	 * @param query
	 * @return
	 * @throws Exception
	 * @Description:
	 */
	public PagerModel<SynDataRecord> getPagerModelByQuery(SynDataRecord synDataRecord, Query query) throws Exception;

	/**
	 * 查询记录数
	 * @param synDataRecord
	 * @return
	 * @throws Exception
	 * @Description:
	 */
	public int getByPageCount(SynDataRecord synDataRecord)throws Exception ;
	
	/**
	 * 添加数据同步记录SynDataRecord
	 * @param synDataRecord
	 * @throws Exception
	 * @Description:
	 */
	public void insertSynDataRecord(SynDataRecord synDataRecord) throws Exception;
	
	/**
	 * 批量添加数据同步记录SynDataRecord
	 * @param synDataRecords
	 * @throws Exception
	 * @Description:
	 */
	public void insertSynDataRecordBatch(List<SynDataRecord> synDataRecords) throws Exception;

	/**
	 * 通过id删除数据同步记录SynDataRecord
	 * @param id
	 * @throws Exception
	 * @Description:
	 */
	public void delSynDataRecordById(String id) throws Exception;

	/**
	 * 通过id批量删除数据同步记录SynDataRecord
	 * @param ids 如："'1','2','3','4'..."
	 * @throws Exception
	 * @Description:
	 */
	public void delSynDataRecordByIds(String ids) throws Exception;
	
	/**
	 * 通过id批量删除数据同步记录SynDataRecord
	 * @param ids 
	 * @throws Exception
	 * @Description:
	 */
	public void delSynDataRecordByIdsList(List<String> ids) throws Exception;

	/**
	 * 通过id修改数据同步记录SynDataRecord
	 * @param synDataRecord
	 * @throws Exception
	 * @Description:
	 */
	public int updateSynDataRecord(SynDataRecord synDataRecord) throws Exception;
	
	/**
	 * 通过ids批量修改数据同步记录SynDataRecord
	 * @param ids 如："'1','2','3','4'..."
	 * @param synDataRecord
	 * @throws Exception
	 * @Description:
	 */
	public int updateSynDataRecordByIds(String ids,SynDataRecord synDataRecord) throws Exception;
	
	/**
	 * 通过ids批量修改数据同步记录SynDataRecord
	 * @param ids 
	 * @param synDataRecord
	 * @throws Exception
	 * @Description:
	 */
	public int updateSynDataRecordByIdsList(List<String> ids,SynDataRecord synDataRecord) throws Exception;
	
	/**
	 * 通过id批量修改数据同步记录SynDataRecord
	 * @param synDataRecords
	 * @throws Exception
	 * @Description:
	 */
	public int updateSynDataRecordList(List<SynDataRecord> synDataRecords) throws Exception;
	
	/**
	 * 得到最大结束时间
	 * @param synDataRecord
	 * @return
	 * @throws Exception
	 * @Description:
	 * @author wangzequan 2016 上午11:36:04
	 */
	public String getSynDataRecordMaxEndTime(SynDataRecord synDataRecord)throws Exception ;
	/**
	 * 通过类型修改时间戳
	 * @param synDataRecord
	 * @throws Exception
	 * @Description:
	 * @author v-wuqiang 2016 下午3:11:40
	 */
	public void updateSynDataRecordByType(SynDataRecord synDataRecord) throws Exception;
	//------------api------------
}
