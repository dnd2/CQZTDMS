package com.infodms.dms.util.sequenceUitl;

import java.util.ArrayList;
import java.util.List;

import com.infoservice.po3.POFactoryBuilder;

/**
 * @Title: CHANADMS
 *
 * @Description:
 *
 * @Copyright: Copyright (c) 2010
 *
 * @Company: www.infoservice.com.cn
 * @Date: 2010-5-18
 *
 * @author added by lishuai 
 * @mail   lishuai103@yahoo.cn
 * @version 1.0
 * @remark 
 */
public class SequenceManager
{
	/**
	 * Function：获得公共序列方法
	 * @param  ：_shortName为空--返回16位系统生成序列
	 *           _shortName不为空则返回18位的工单号
	 * LastUpdate：	2010-5-18
	 */
	@SuppressWarnings("unchecked")
	public static String getSequence(String _shortName){
		List shortName=new ArrayList();
		shortName.add(_shortName);
		String ret = new String();
		if(_shortName==null||"".equals(_shortName)){
			ret=POFactoryBuilder.getInstance().callFunction("F_GETID",java.sql.Types.VARCHAR,null).toString();
		}else{
			ret=POFactoryBuilder.getInstance().callFunction("F_CREATEID",java.sql.Types.VARCHAR,shortName).toString();
		}
		if(ret==null) return "";
		if(ret instanceof String){
			return String.valueOf(ret);
		}else{
			return "";
		}
	}

	/**
	 * Function：获得公共单号方法
	 * @param  ：
	 *           _shortName不为空则返回20位的工单号
	 * LastUpdate：	2010-9-3
	 */
	
	public static String getSequence2(String _shortName){
		List shortName2=new ArrayList();
		shortName2.add(_shortName);
		String ret = new String();
		if(_shortName==null||"".equals(_shortName)){
			ret=POFactoryBuilder.getInstance().callFunction("F_GETID",java.sql.Types.VARCHAR,null).toString();
		}else{
			ret=POFactoryBuilder.getInstance().callFunction("F_CREATENO",java.sql.Types.VARCHAR,shortName2).toString();
		}
		if(ret==null) return "";
		if(ret instanceof String){
			return String.valueOf(ret);
		}else{
			return "";
		}
	}

	public static String getSequence3(String _shortName){
		List shortName2=new ArrayList();
		shortName2.add(_shortName);
		String ret = new String();
		if(_shortName==null||"".equals(_shortName)){
			ret=POFactoryBuilder.getInstance().callFunction("F_GETID",java.sql.Types.VARCHAR,null).toString();
		}else{
			ret=POFactoryBuilder.getInstance().callFunction("F_GET_CREATENO",java.sql.Types.VARCHAR,shortName2).toString();
		}
		if(ret==null) return "";
		if(ret instanceof String){
			return String.valueOf(ret);
		}else{
			return "";
		}
	}
	
	public static String getSequence4(String _shortName){
		List shortName2=new ArrayList();
		shortName2.add(_shortName);
		String ret = new String();
		if(_shortName==null||"".equals(_shortName)){
			ret=POFactoryBuilder.getInstance().callFunction("F_GETID",java.sql.Types.VARCHAR,null).toString();
		}else{
			ret=POFactoryBuilder.getInstance().callFunction("F_GET_LONG_NO",java.sql.Types.VARCHAR,shortName2).toString();
		}
		if(ret==null) return "";
		if(ret instanceof String){
			return String.valueOf(ret);
		}else{
			return "";
		}
	}
		
	/**
	 * @param args
	 * void
	 * 2010-5-18
	 */
	public static void main(String[] args)
	{
		// TODO Auto-generated method stub

	}
	
	public static String getSequence_F_GETID_L6() {
		String ret = POFactoryBuilder.getInstance().callFunction("F_GETID_L6", java.sql.Types.VARCHAR, null).toString();
		
		return String.valueOf(ret);
	}
	
	/**
	 * Function：获得公共序列方法
	 * 
	 * @param ：_shortName为空--返回16位系统生成序列
	 *           返回客户编码： 2010-5-18
	 */
	@SuppressWarnings("unchecked")
	public static String getCtmNo() {
		String ret = new String();
		ret = POFactoryBuilder.getInstance().callFunction("F_GET_NO", java.sql.Types.VARCHAR, null).toString();
		if (ret == null)
			return "";
		if (ret instanceof String) {
			return String.valueOf(ret);
		} else {
			return "";
		}
	}

}
