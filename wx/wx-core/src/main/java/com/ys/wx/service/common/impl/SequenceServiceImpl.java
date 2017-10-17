package com.ys.wx.service.common.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.ys.tools.common.StringTools;
import com.ys.tools.pager.PagerModel;
import com.ys.tools.pager.Query;
import com.ys.ucenter.utils.CommUtils;
import com.ys.wx.dao.common.ISequenceDao;
import com.ys.wx.model.common.Sequence;
import com.ys.wx.service.common.ISequenceService;




/**
 * 序列号生成器Service实现
 * @author CeaserWang
 * @date 2017-08-30 15:07:19
 * 
 *          =======================================DB函数支持===================================
 *
			DROP TABLE IF EXISTS sequence;   
			CREATE TABLE sequence (       
			seq_name        VARCHAR(50) NOT NULL, -- 序列名称       
			current_val     INT         NOT NULL, -- 当前值       
			increment_val   INT         NOT NULL    DEFAULT 1, -- 步长(跨度)       
			PRIMARY KEY (seq_name)   ); 
			
			#example:
			INSERT INTO sequence VALUES ('ZSLBZ', '1000', '1');  
			
			CREATE FUNCTION currval(v_seq_name VARCHAR(50))   
			RETURNS INTEGER  
			BEGIN      
				DECLARE VALUE INTEGER;       
				SET VALUE = 0;       
				SELECT current_val INTO VALUE  FROM sequence WHERE seq_name = v_seq_name; 
			   RETURN VALUE; 
			END;
			
			
			CREATE FUNCTION nextval (v_seq_name VARCHAR(50))
				RETURNS INTEGER
			BEGIN
			    UPDATE sequence SET current_val = current_val + increment_val  WHERE seq_name = v_seq_name;
				RETURN currval(v_seq_name);
			END;
			
			#example:
			select concat('ZSLBZ',nextval('ZSLBZ')) as next

 */
@Service
public class SequenceServiceImpl implements ISequenceService {

	@Resource
	private ISequenceDao sequenceDao;

	@Override
	public Sequence getSequenceBySeq_name(String seqName) throws Exception {
		return StringUtils.isNotBlank(seqName) ? this.sequenceDao.getSequenceBySeq_name(seqName.trim()) : null;
	}
	
	/**
	 * @see SequenceEnum
	 */
	public synchronized String next(String seqName) throws Exception {
		return StringUtils.isNotBlank(seqName) ? this.sequenceDao.next(seqName.trim()) : null;
	}

	@Override
	public List<Sequence> getSequenceBySeq_names(String seqNames) throws Exception {
		seqNames = StringTools.converString(seqNames);
		return StringUtils.isNotBlank(seqNames) ? this.sequenceDao.getSequenceBySeq_names(seqNames) : null;
	}
	
	@Override
	public List<Sequence> getSequenceBySeq_namesList(List<String> seqNames) throws Exception {
		return CollectionUtils.isNotEmpty(seqNames) ? this.sequenceDao.getSequenceBySeq_namesList(seqNames) : null;
	}

	@Override
	public List<Sequence> getAll(Sequence sequence) throws Exception {
		return null != sequence ? this.sequenceDao.getAll(sequence) : null;
	}

	@Override
	public PagerModel<Sequence> getPagerModelByQuery(Sequence sequence, Query query)
			throws Exception {
		return (null != sequence && null != query) ? this.sequenceDao.getPagerModelByQuery(sequence, query) : null;
	}
	
	@Override
	public int getByPageCount(Sequence sequence)throws Exception {
		return (null != sequence) ? this.sequenceDao.getByPageCount(sequence) : 0;
	}

	@Override
	public void insertSequence(Sequence sequence) throws Exception {
		if(CommUtils.isNotEmpty(sequence.getSeqName()) && null!=sequence.getCurrentVal()){
			this.sequenceDao.insertSequence(sequence);
		}else{
			throw new RuntimeException("序列名称或者步长不能为空!");
		}
	}
	
	@Override
	public void insertSequenceBatch(List<Sequence> sequences) throws Exception {
		this.sequenceDao.insertSequenceBatch(sequences);
	}
	
	@Override
	public void delSequenceBySeq_name(String seqName) throws Exception {
		if (StringUtils.isNotBlank(seqName)) {
			this.sequenceDao.delSequenceBySeq_name(seqName.trim());
		}
	}
	
	@Override
	public void delSequenceBySeq_names(String seqNames) throws Exception {
		seqNames = StringTools.converString(seqNames);
		if(StringUtils.isNotBlank(seqNames)){
			this.sequenceDao.delSequenceBySeq_names(seqNames);
		}
	}
	
	@Override
	public void delSequenceBySeq_namesList(List<String> seqNames) throws Exception {
		if(CollectionUtils.isNotEmpty(seqNames)){
			this.sequenceDao.delSequenceBySeq_namesList(seqNames);
		}
	}

	@Override
	public int updateSequence(Sequence sequence) throws Exception {
		return this.sequenceDao.updateSequence(sequence);
	}
	
	@Override
	public int updateSequenceBySeq_names(String seqNames,Sequence sequence) throws Exception {
		return this.sequenceDao.updateSequenceBySeq_names(seqNames, sequence);
	}
	
	@Override
	public int updateSequenceBySeq_namesList(List<String> seqNames,Sequence sequence) throws Exception {
		return this.sequenceDao.updateSequenceBySeq_namesList(seqNames, sequence);
	}
	
	@Override
	public int updateSequenceList(List<Sequence> sequences) throws Exception {
		return this.sequenceDao.updateSequenceList(sequences);
	}
	
	/***
	 * 将"1,2,3,4,5..."这种形式的字符串转成"'1','2','3','4'..."这种形式
	 * @param strs
	 * @return
	 
	private String converString(String strs) {
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
	 * @return
	 
	private List<String> converStringToList(String strs) {
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
	
	//------------api------------
	
}

