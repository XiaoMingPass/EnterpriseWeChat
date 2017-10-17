package com.ys.wx.model.common;

import java.io.Serializable;

import com.mhome.tools.common.BaseModel;

/**
 * 序列号生成器类
 * @author CeaserWang
 * @date 2017-08-30 15:07:19
 */
public class Sequence extends BaseModel implements Serializable{
    
    /**
     * 序列名称 @See SequenceEnum
     */
    private String seqName;
    
    /**
     * 当前值
     */
    private Integer currentVal;
    
    /**
     * 序列步长
     */
    private Integer incrementVal;
	
    //temp
    /**
     * 下一个值
     */
    private String next;
    
	 /* //临时变量-s
    private String startTime; 
    private String endTime;
    //临时变量-e
    
    public String getStartTime() {
    	return startTime;
    }
    
    public void setStartTime(String startTime) {
    	this.startTime = startTime;
    }
    
    public String getEndTime() {
    	return endTime;
    }
    
    public void setEndTime(String endTime) {
    	this.endTime = endTime;
    }*/
	
    public String getSeqName()
    {
        return seqName;
    }
        
    public void setSeqName(String seqName)
    {
        this.seqName = seqName;
    }
        
    public Integer getCurrentVal()
    {
        return currentVal;
    }
        
    public void setCurrentVal(Integer currentVal)
    {
        this.currentVal = currentVal;
    }
        
    public Integer getIncrementVal()
    {
        return incrementVal;
    }
        
    public void setIncrementVal(Integer incrementVal)
    {
        this.incrementVal = incrementVal;
    }

	public String getNext() {
		return next;
	}

	public void setNext(String next) {
		this.next = next;
	}
        
}
