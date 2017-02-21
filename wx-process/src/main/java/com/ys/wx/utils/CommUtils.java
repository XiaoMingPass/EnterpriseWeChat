package com.ys.wx.utils;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

public class CommUtils {
	
	public static boolean isEmpty(String st){
		if(null == st || "".equals(st) || "null".equals(st)){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 判断字符创不为空
	 * @param st
	 * @return
	 * @Description:
	 * @author wangzequan 2016 上午11:23:43
	 */
	public static boolean isNotEmpty(String st){
		return !isEmpty(st);
	}
	
	/**
	 * 转化一个对象名称为去掉前后空格,带缺省值
	 * 
	 * @param obj
	 * @return String
	 */
	public static String toStr(Object obj) {
		if (obj == null) {
			return "";
		} else {
			return obj.toString().trim();
		}
	}
	
	/**
	 * 将一个对象字串转化为一个数字
	 * @param obj Object
	 * @return int
	 */
	public static int toInt(Object obj)	{
		try	{
			if (obj == null || obj.toString().equals(""))
			{
				return 0;
			}
			else
			{
				return Integer.parseInt(obj.toString().trim());
			}
		}
		catch (Exception ex) {
			return 0;
		}
	}
	
	/**
	 * 按照format格式化显示日期时间
	 * @param source 日期的字符串
	 * @param format 日期的格式 
	 * @return
	 */
	public static String getDateString(Date source, String format)
	{
		if (source == null)	return "";
		try	{
			DateFormat formatter = new SimpleDateFormat(format);
			return formatter.format(source);
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	
	/**
     * 将字串转成日期和时间，
     * @param String date
     * @param String format 字串格式: yyyy-MM-dd HH:mm:ss
     * @return Date
     */
    public static Date getDateString(String date,String format) {
        try {
        	if(isEmpty(date)) return null;
            DateFormat formatter = new SimpleDateFormat(format);
            return (Date) formatter.parse(date);
        } catch (Exception e) {
        	e.printStackTrace();
            return null;
        }
    }

	/**
	 * 得到给定日期的后 n 个月的日期
	 * 
	 * @param month
	 *            月份
	 * 
	 * @return
	 */
	public static String getDateAfterMonth(Date date, int month) {
		if(CommUtils.isNull(date)) return null;
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MONTH, month);
		
		return new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
	}
	
	/**
	 * 获取当前日期后面n个小时的时间值(单位Mill)
	 * @param date(日期是24小时制)
	 * @param hour
	 * @return
	 */
	public static Long getDateAfterHour(Date date,int hour) {
		if(CommUtils.isNull(date)) return 0L;
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		long time = cal.getTimeInMillis() + hour*60*60*1000;
		return time;
	}
	
	/**
	 * 获取当前日期后面n个小时的时间值(单位Mill)
	 * @param date(日期是24小时制)
	 * @param hour
	 * @return
	 */
	public static Date getCurrDateTime() {
		Date date = new Date();
		return date;
	}
	
	/**
	 * 获取某一个日期的微秒时间值
	 * @param date
	 * @return
	 */
	public static Long getCurrDateTimeInMillis(Date date) {
		if(CommUtils.isNull(date)) return null;
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.getTimeInMillis();
	}

	/**
	 * 产生给定位数的随机数
	 * 
	 * @param count
	 *            随机数的位数
	 * @return
	 */
	public static String getRandom(int count) {
		Random random = new Random();
		String strNum = "";
		while (true) {
			strNum = strNum + random.nextInt(10);
			if (strNum.length() == count)
				break;
		}
		return strNum;
	}

	public static String encodeUrl(String encodeURL, String charsetEncode) {
		String url = "";
		if (CommUtils.isEmpty(encodeURL) || CommUtils.isEmpty(charsetEncode))
			return url;
		try {
			url = URLEncoder.encode(encodeURL, charsetEncode);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return url;
	}

	public static String encodeUrl(String encodeURL) {
		return encodeUrl(encodeURL, "utf-8");
	}

	public static String decodeUrl(String decodeURL, String charsetEncode) {
		String url = "";
		if (CommUtils.isEmpty(decodeURL) || CommUtils.isEmpty(charsetEncode))
			return url;
		try {
			url = URLDecoder.decode(decodeURL, charsetEncode);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return url;
	}

	public static String decodeUrl(String decodeURL) {
		return decodeUrl(decodeURL, "utf-8");
	}
	
	/**
	 * 
	 * @return
	 * @Description:获取web项目根目录
	 */
	public static String getWebRootPath(){
		String classPath=Thread.currentThread().getContextClassLoader().getResource("/").getPath();
		int webinf=classPath.indexOf("WEB-INF");
		return classPath.substring(0, webinf);
	}
	
	public static String escapeHtml(String str) {
		if (isEmpty(str))
			return "";
		str = str.trim();
		str = str.replaceAll("&", "&amp;");
		str = str.replaceAll("\"", "&quot;");
		str = str.replaceAll(" ", "&nbsp;");
		str = str.replaceAll("\\*", "&#42;");
		str = str.replaceAll("<", "&lt;");
		str = str.replaceAll(">", "&gt;");
		str = str.replaceAll("\'", "&#039;");
		str = str.replaceAll("\r\n", "<br/>");
		str = str.replaceAll("\n", "<br/>");
		str = str.replaceAll("\r", "<br/>");
		str = str.replaceAll("\\(", "&#40;");
		str = str.replaceAll("\\)", "&#41;");
		str = str.replaceAll("\\{", "&#123;");
		str = str.replaceAll("\\}", "&#125;");
		str = str.replaceAll("\\[", "&#91;");
		str = str.replaceAll("\\]", "&#93;");
		str = str.replaceAll("style", "");
		
		return str;
	}
	
	/**
	* 格式化HTML文本
	* @param content
	* @return
	*/
	public static String rhtml(String content) {
		if(StringUtils.isBlank(content))
			return content;
		String html = content;
		html = StringUtils.replace(html, "&amp;","&");
		html = StringUtils.replace(html, "&lt;","<");
		html = StringUtils.replace(html, "&gt;",">");
		html = StringUtils.replace(html, "&nbsp;"," ");
		html = StringUtils.replace(html, "<!--#if-->", "</#if>");
		return html;
	}
	
	/**
	 * 判断此字符串(对象转化)是否为空、空字符串，或"null"
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNull(Object obj) {
		if (obj == null) return true;
		
		String szTmp = obj.toString();
		
		if (szTmp.equals("") || szTmp.equalsIgnoreCase("null"))
			return true;
		else
			return false;
	}
	
    /**
     * 返回客户端IP地址
     * @param request HttpServletRequest
     * @return 客户端IP地址，若等于""，则表示取IP失败
     */
    public static String getRealRemoteIP(HttpServletRequest request){
        String ret = "";
        String cdnSrcIp = request.getHeader("Cdn-Src-Ip");
        if(cdnSrcIp == null){
            String ipNginx = request.getHeader("X_REAL_IP");
            String ip = null;
            if (ipNginx != null) {
                ip = ipNginx;
            } else {
                ip = request.getRemoteAddr();
            }
            
            if (ip!=null) {
                ret = ip;
            }
        }else {
            ret = cdnSrcIp.trim();
        }
        
        if(ret.startsWith("0:0:0:0:"))
        {
            ret = "127.0.0.1";
        }
        return ret;
    }
    
    /**
     * 获取密码的强、中、弱度
     * @param pwd 密码
     * @return 0 弱 、 1 中、2 高 
     */
    public static int getPwdIntensiion(String pwd) {
		int score=0;
		Pattern p1 = Pattern.compile("[a-z]+");
		Pattern p2 = Pattern.compile("[A-Z]+");
		Pattern p3 = Pattern.compile("\\d+");
		Pattern p4 = Pattern.compile("[!,@,#,$,%,^,&,*,?,_,~]+");

		Matcher m1 = p1.matcher(pwd);
		Matcher m2 = p2.matcher(pwd);
		Matcher m3 = p3.matcher(pwd);
		Matcher m4 = p4.matcher(pwd);
		
		if(pwd.length()>2){
			// 有小写字母有数字
			if(m1.find() && m3.find() && pwd.length()>13) {score+=5;}
			// 有大写字母有数字
			if(m2.find() && m3.find()) {score+=7;}
			// 有小写字母和大写字母
			if(m1.find() && m2.find()) {score+=7;}
			//有大小写字母和数字
			if(m1.find() && m2.find() && m3.find()) {score+=10;}
			// 有特殊字符
			if(m4.find()) {score+=15;}
			// 有小写字母或者大写字母有数字超过12位
			if(m1.find() && m3.find() && pwd.length()>12) {score+=10;}
			if(m2.find() && m3.find() && pwd.length()>12) {score+=10;}
		}
		if(score<5){
			return 0;
		}else if(score>=5 && score<20){
			return 1;
		}else if(score>=20){
			return 2;
		}
    	return 0;
    }
    
    /**得到用户的操作系统信息*/
    public static String getUserOsInfo(HttpServletRequest request){
    	String Agent = request.getHeader("User-Agent");  
    	StringTokenizer st = new StringTokenizer(Agent,";");  
    	st.nextToken();  
    	//得到用户的浏览器名  
    	String userbrowser = st.nextToken();  
    	//得到用户的操作系统名  
    	String useros = st.nextToken();  
    	return useros;
    }
	
    /** 设置字符串为空时的默认值
     * create by wangzequan
     *  */
    public static String defaultIfEmpty(String paran,String defaultStr){
    	return (null==paran || "null".equals(paran) || "".equals(paran)) ?  defaultStr : paran;
    }
    /**
	 * 时间输出为时间格式
	 * @param dateStr
	 * @return
	 */
    public static Date parseStrToDateShort(String dateStr){
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM");
		if(StringUtils.isNotBlank(dateStr)){
			try {
				return sdf.parse(dateStr);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	/**
	 * 时间格式输出为字符串格式
	 * @param dateStr
	 * @return
	 */
    public static String parseDateToStrShort(Date  date){
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM");
		if(null!=date){
			try {
				return sdf.format(date);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	/**
	 * 时间输出为时间格式
	 * @param dateStr
	 * @return
	 */
    public static Date parseStrToDate(String dateStr){
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		if(StringUtils.isNotBlank(dateStr)){
			try {
				return sdf.parse(dateStr);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
    
    public static Date parseStrToLongDate(String dateStr){
    	SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	if(StringUtils.isNotBlank(dateStr)){
    		try {
    			return sdf.parse(dateStr);
    		} catch (ParseException e) {
    			e.printStackTrace();
    		}
    	}
    	return null;
    }
	/**
	 * 时间格式输出为字符串格式
	 * @param dateStr
	 * @return
	 */
    public static String parseDateToStr(Date  date){
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		if(null!=date){
			try {
				return sdf.format(date);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
    
	/**
	 * 
	 * @param str 原始字符串
	 * @param spliter 原始字符串分隔符
	 * @param splitsize 格式化之后一行几个元素
	 * @param newSpliter 格式化换行占位符
	 * @return
	 * @Description:
	 * @author wangzequan 2016 上午11:15:39
	 */
	public static String formatStr(String str,String spliter,int splitsize,String newSpliter){
		if(CommUtils.isEmpty(str) || CommUtils.isEmpty(spliter) || splitsize<=0){
			return "";
		}
		String [] strarr = str.split(spliter);
		List<String> strList = Arrays.asList(strarr);
		StringBuffer returnstr = new StringBuffer();
		for(int i=0;i<strList.size();i++){
			if(i!=0 && i%splitsize==0){
				returnstr.append(newSpliter).append(strList.get(i));
			}else{
				if(i==0){
					returnstr.append(strList.get(i));
				}else{
					returnstr.append(",").append(strList.get(i));
				}
			}
		}
 		return returnstr.toString();
	}
	
    
    /** 
     * @Description:把list转换为一个用逗号分隔的字符串 
     */  
    public static String listToString(List list) {  
        StringBuilder sb = new StringBuilder();  
        if (list != null && list.size() > 0) {  
            for (int i = 0; i < list.size(); i++) {  
                if (i < list.size() - 1) {  
                    sb.append("'"+list.get(i) + "'" + ",");  
                } else {  
                    sb.append("'"+list.get(i)+"'");  
                }  
            }  
        }  
        return sb.toString();  
    } 
    
    /**
     * 得到未来时间与当前时间相差几天  参数为空的时候返回0
     * @param paramDate
     * @return
     * @Description:
     * @author wangzequan 2016 下午3:53:11
     */
    public static long getFutureCurrentDayDifference(Date paramDate){
    	if(null!=paramDate){
    		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    		Date d1 = new Date();
    		Date d2 = paramDate;
    		long diff = d2.getTime() - d1.getTime();
    		long days = diff / (1000 * 60 * 60 * 24);
    		return days;
    	}else{
    		return 0;
    	}
    }

    /**
     * 时间的加减 
     * @param failTime
     * @param addMonths 正数  为加N个月 负数则减N个月
     * @return
     * @Description:
     * @author wangzequan 2016 下午3:40:21
     */
	public static Date addOrSubMonth(Date failTime,int addMonths) {
		if(null!=failTime){
			Calendar calender = Calendar.getInstance();
			calender.setTime(failTime);
			calender.add(Calendar.MONTH, addMonths);
			return calender.getTime();
		}else {
			return null;
		}
	}
	
	/**
	 * 是否是Windows操作系统
	 * @return
	 * @Description:
	 * @author wangzequan 2016 下午5:56:57
	 */
	public static boolean   isWindowOs(){
		Properties prop = System.getProperties();
		String os = prop.getProperty("os.name");
		return os.startsWith("win") || os.startsWith("Win");
	}
	
	/**
	 * 是否是Linux操作系统
	 * @return
	 * @Description:
	 * @author wangzequan 2016 下午5:56:57
	 */
	public static boolean   isLinuxOs(){
		Properties prop = System.getProperties();
		String os = prop.getProperty("os.name");
		return os.indexOf("linux")>=0 || os.indexOf("Linux")>=0 ;  
	}

	public static String defaultIfEmpty(Double doubelnum, String defaultStr) {
		return (null==doubelnum) ?  defaultStr : String.valueOf(doubelnum) ;
	}
	
	public static Double formatNum(Double num){
		  Double dnum = 0.0;
		  try {
			java.text.DecimalFormat   df=new   java.text.DecimalFormat("#.##");   
			  String strnum =  df.format(num);
			  if(CommUtils.isNotEmpty(strnum)){
				  dnum = Double.valueOf(strnum);
			  }
		} catch (Exception e) {
			e.printStackTrace();
		}
		return   dnum;
	}
	
	public static Date setHoursMin(String hourmin){
		Calendar cal = Calendar.getInstance();
		if(StringUtils.isNotBlank(hourmin)){
			String[] sourceStrArray = hourmin.split(":"); 
			if(sourceStrArray != null && sourceStrArray.length>=2){
				cal.set(1970, 1, 1, Integer.parseInt(sourceStrArray[0]), Integer.parseInt(sourceStrArray[1]));
				return cal.getTime();
			}
		}
		return null;
	}
	
}
