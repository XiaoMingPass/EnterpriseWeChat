package com.ys.wx.dao.common;

import java.util.List;

import com.ys.tools.pager.PagerModel;
import com.ys.tools.pager.Query;
import com.ys.wx.model.common.Sequence;



/**
 * 序列号生成器Dao接口
 * @author CeaserWang
 * @date 2017-08-30 15:07:19
 */
public interface ISequenceDao {

	/**
	 * 通过seqName得到序列号生成器Sequence
	 * @param seqName
	 * @return 
	 * @throws Exception
	 * @Description:
	 */
	public Sequence getSequenceBySeq_name(String seqName) throws Exception;

	/**
	 * 得到下一个序列
	 * @param seqNames
	 * @return
	 * @throws Exception
	 */
	public String next(String seqNames) throws Exception;
	
	/**
	 * 通过seqNames批量得到序列号生成器Sequence
	 * @param seqNames 如："'1','2','3','4'..."
	 * @return 
	 * @throws Exception
	 * @Description:
	 */
	public List<Sequence> getSequenceBySeq_names(String seqNames) throws Exception;
	
	/**
	 * 通过seqNames批量得到序列号生成器Sequence
	 * @param seqNames 
	 * @return 
	 * @throws Exception
	 * @Description:
	 */
	public List<Sequence> getSequenceBySeq_namesList(List<String> seqNames) throws Exception;

	/**
	 * 得到所有序列号生成器Sequence
	 * @param sequence
	 * @return 
	 * @throws Exception
	 * @Description:
	 */
	public List<Sequence> getAll(Sequence sequence) throws Exception;

	/**
	 * 分页查询序列号生成器Sequence
	 * @param sequence
	 * @param query
	 * @return
	 * @throws Exception
	 * @Description:
	 */
	public PagerModel<Sequence> getPagerModelByQuery(Sequence sequence, Query query) throws Exception;
	
	/**
	 * 查询记录数
	 * @param sequence
	 * @return
	 * @throws Exception
	 * @Description:
	 */
	public int getByPageCount(Sequence sequence)throws Exception ;

	/**
	 * 添加序列号生成器Sequence
	 * @param sequence
	 * @throws Exception
	 * @Description:
	 */
	public void insertSequence(Sequence sequence) throws Exception;
	
	/**
	 * 批量添加序列号生成器Sequence
	 * @param sequences
	 * @throws Exception
	 * @Description:
	 */
	public void insertSequenceBatch(List<Sequence> sequences) throws Exception;

	/**
	 * 通过seqName删除序列号生成器Sequence
	 * @param seqName
	 * @throws Exception
	 * @Description:
	 */
	public void delSequenceBySeq_name(String seqName) throws Exception;
	
	/**
	 * 通过seqName批量删除序列号生成器Sequence
	 * @param seqNames 如："'1','2','3','4'..."
	 * @throws Exception
	 * @Description:
	 */
	public void delSequenceBySeq_names(String seqNames) throws Exception;
	
	/**
	 * 通过seqName批量删除序列号生成器Sequence
	 * @param seqNames 
	 * @throws Exception
	 * @Description:
	 */
	public void delSequenceBySeq_namesList(List<String> seqNames) throws Exception;

	/**
	 * 通过seqName修改序列号生成器Sequence
	 * @param sequence
	 * @throws Exception
	 * @Description:
	 */
	public int updateSequence(Sequence sequence) throws Exception;
	
	/**
	 * 通过seqNames批量修改序列号生成器Sequence
	 * @param seqNames 如："'1','2','3','4'..."
	 * @param sequence
	 * @throws Exception
	 * @Description:
	 */
	public int updateSequenceBySeq_names(String seqNames,Sequence sequence) throws Exception;
	
	/**
	 * 通过seqNames批量修改序列号生成器Sequence
	 * @param seqNames 
	 * @param sequence
	 * @throws Exception
	 * @Description:
	 */
	public int updateSequenceBySeq_namesList(List<String> seqNames,Sequence sequence) throws Exception;
	
	/**
	 * 通过seqName批量修改序列号生成器Sequence
	 * @param sequences
	 * @throws Exception
	 * @Description:
	 */
	public int updateSequenceList(List<Sequence> sequences) throws Exception;
	
	//------------api------------
}
