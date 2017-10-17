package com.ys.wx.dao.common.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.ys.tools.common.StringTools;
import com.ys.tools.common.UUIDGenerator;
import com.ys.tools.pager.PagerModel;
import com.ys.tools.pager.Query;
import com.ys.tools.template.MybatisTemplate;
import com.ys.wx.dao.common.ISequenceDao;
import com.ys.wx.model.common.Sequence;


	

/**
 * 序列号生成器Dao实现
 * @author CeaserWang
 * @date 2017-08-30 15:07:19
 */
@Repository
public class SequenceDaoImpl extends MybatisTemplate implements ISequenceDao {

	@Override
	public Sequence getSequenceBySeq_name(String seqName) throws Exception {
		return (Sequence)this.selectOne("SequenceXML.getSequenceBySeq_name", seqName);
	}
	
	@Override
	public List<Sequence> getSequenceBySeq_names(String seqNames) throws Exception {
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("seqNames", seqNames);
		return this.selectList("SequenceXML.getSequenceBySeq_names", params);
	}
	
	@Override
	public String next(String seqNames) throws Exception {
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("seqNames", seqNames);
		return this.selectOne("SequenceXML.next", params);
	}
	
	@Override
	public List<Sequence> getSequenceBySeq_namesList(List<String> seqNames) throws Exception {
		return this.selectList("SequenceXML.getSequenceBySeq_namesList", seqNames);
	}

	@Override
	public List<Sequence> getAll(Sequence sequence) throws Exception {
		return this.selectList("SequenceXML.getAll", sequence);
	}

	@Override
	public PagerModel<Sequence> getPagerModelByQuery(Sequence sequence, Query query)
			throws Exception {
		return this.getPagerModelByQuery(sequence, query, "SequenceXML.getPagerModelByQuery");
	}
	
	@Override
	public int getByPageCount(Sequence sequence)throws Exception {
		return this.selectOne("SequenceXML.getByPageCount", sequence);
	}

	@Override
	public void insertSequence(Sequence sequence) throws Exception {
		if (null != sequence) {
			sequence.setSeqName(UUIDGenerator.generate());
			sequence.setCreateTime(new Date());
			sequence.setUpdateTime(new Date());
			this.insert("SequenceXML.insertSequence", sequence);
		}
	}
	
	@Override
	public void insertSequenceBatch(List<Sequence> sequences) throws Exception {
		if(CollectionUtils.isNotEmpty(sequences)){
			for (Sequence sequence : sequences) {
				if (null != sequence) {
					sequence.setSeqName(UUIDGenerator.generate());
					sequence.setCreateTime(new Date());
				}
			}
			this.insert("SequenceXML.insertSequenceBatch", sequences);
		}
	}
	
	@Override
	public void delSequenceBySeq_name(String seqName) throws Exception {
		this.delete("SequenceXML.delSequenceBySeq_name", seqName);
	}
	
	@Override
	public void delSequenceBySeq_names(String seqNames) throws Exception {
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("seqNames", seqNames);
		this.delete("SequenceXML.delSequenceBySeq_names", params);
	}
	
	@Override
	public void delSequenceBySeq_namesList(List<String> seqNames) throws Exception {
		this.delete("SequenceXML.delSequenceBySeq_namesList", seqNames);
	}
	
	@Override
	public int updateSequence(Sequence sequence) throws Exception {
		if (null != sequence) {
			sequence.setUpdateTime(new Date());
			/**清理不需要更新的数据*/
		    this.cleanWhenUpdate(sequence);
			return this.update("SequenceXML.updateSequence", sequence);
		} else {
			return 0;
		}
	}
	
	@Override
	public int updateSequenceBySeq_names(String seqNames,Sequence sequence) throws Exception {
		seqNames = StringTools.converString(seqNames);
		if (StringUtils.isNotBlank(seqNames) && null != sequence) {
			sequence.setUpdateTime(new Date());
			Map<String,Object> params = new HashMap<String, Object>();
			params.put("seqNames", seqNames);
			/**清理不需要更新的数据*/
		    this.cleanWhenUpdate(sequence);
			params.put("sequence", sequence);
			return this.update("SequenceXML.updateSequenceBySeq_names", params);
		} else {
			return 0;
		}
		
	}
	
	@Override
	public int updateSequenceBySeq_namesList(List<String> seqNames,Sequence sequence) throws Exception {
		if (CollectionUtils.isNotEmpty(seqNames) && null != sequence) {
			sequence.setUpdateTime(new Date());
			Map<String,Object> params = new HashMap<String, Object>();
			params.put("seqNames", seqNames);
			/**清理不需要更新的数据*/
		    this.cleanWhenUpdate(sequence);
			params.put("sequence", sequence);
			return this.update("SequenceXML.updateSequenceBySeq_namesList", params);
		} else {
			return 0;
		}
	}
	
	@Override
	public int updateSequenceList(List<Sequence> sequences) throws Exception {
		if(CollectionUtils.isNotEmpty(sequences)){
			for (Sequence sequence : sequences) {
				if (null != sequence) {
					sequence.setUpdateTime(new Date());
					/**清理不需要更新的数据*/
					this.cleanWhenUpdate(sequence);
				}
			}
			return this.update("SequenceXML.updateSequenceList", sequences);
		} else {
			return 0;
		}
	}
	
	/**
	 * 清理不需要更新的数据
	 * @param orderRebok
	 * @Description:
	 * @author wentaoxiang 2016年6月1日 下午5:19:16
	 */
	private void cleanWhenUpdate(Sequence sequence) {
		sequence.setCreateTime(null);
		sequence.setCreator(null);
	}
	
	//------------api------------
}

