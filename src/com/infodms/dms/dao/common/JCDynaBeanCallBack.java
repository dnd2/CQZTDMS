/*
 * Copyright (c) 2005 Infoservice, Inc. All  Rights Reserved.
 * This software is published under the terms of the Infoservice Software
 * License version 1.0, a copy of which has been included with this
 * distribution in the LICENSE.txt file.
 * 
 * CreateDate  ： 2009-7-7 上午02:40:22
 * CreateBy    ： QiuMingJie
 * ProjectName ：InfoPO
 * PackageName ：com.infoservice.po3.callbackimpl
 * File_name   ：DyncBeanCallBack.java
 * Type_name   ：DyncBeanCallBack
 * Comment     ：			    
 */
package com.infodms.dms.dao.common;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infoservice.po3.bean.DynaBean;
import com.infoservice.po3.core.callback.DAOCallback;
import com.infoservice.po3.core.exceptions.DAOException;
import com.infoservice.po3.tools.POGenUtil;

/**
 * @author QiuMingJie
 *
 */
@SuppressWarnings({"unchecked"})
public class JCDynaBeanCallBack implements DAOCallback<DynaBean> {
	public Logger logger = Logger.getLogger(JCDynaBeanCallBack.class);
	private Map<String, String> names = new HashMap<String, String>();
	private Map<String, Class> calsses = new HashMap<String, Class>();
	private String parseName(String tname) {
		byte ascii = 'Z' - 'z';
		tname = tname.toLowerCase();
		StringBuilder sbd = new StringBuilder();
		for (int i = 0; i < tname.length(); i++) {
			if (tname.charAt(i) == '_' && i + 1 < tname.length()) {
				i++;
				if (tname.charAt(i) >= 'a' && tname.charAt(i) <= 'z') {
					sbd.append((char) (tname.charAt(i) + ascii));
				} else {
					sbd.append(tname.charAt(i));
				}
			} else {
				sbd.append(tname.charAt(i));
			}
		}
		return sbd.toString();
	}
	private void getBeanInfo(ResultSet rs){
		String colName = null;
		Class cls = null;
		try {
			ResultSetMetaData rsmd = rs.getMetaData();
			int colSize = rsmd.getColumnCount();
			for(int i = 1; i<= colSize ; i++){
				colName = rsmd.getColumnName(i);
				if(!colName.equals("R")){
					cls = POGenUtil.getJavaType(colName, rsmd.getColumnType(i), rsmd.getPrecision(i), rsmd.getScale(i) < 0 && rsmd.getPrecision(i) == 0 ? 0 : rsmd.getScale(i));
					//this.names.put(colName, parseName(colName));
					this.names.put(colName, colName);
					this.calsses.put(colName, cls);
				}
			}
		} catch (SQLException e) {
			logger.error(e.getMessage(),e);
			throw new DAOException(e);
		}catch(Exception e){
			logger.error(e.getMessage(),e);
			throw new DAOException(e);
		}
	}
	/* (non-Javadoc)
	 * @see com.infoservice.po3.core.callback.DAOCallback#wrapper(java.sql.ResultSet, int)
	 */
	public DynaBean wrapper(ResultSet rs, int idx) {
		DynaBean bean = new DynaBean();
		if(this.calsses.isEmpty() || this.names.isEmpty()){
			getBeanInfo(rs);
		}
		Class clz = null;
		try {
			for(String key : this.calsses.keySet()){
				clz = this.calsses.get(key);
				
				bean.put(this.names.get(key), rs.getObject(key));
//				if(clz.equals(String.class)){
//					bean.put(this.names.get(key), rs.getString(key));
//				}else if(clz.equals(Integer.class)){
//					bean.put(this.names.get(key), rs.getInt(key));
//				}else if(clz.equals(Long.class)){
//					bean.put(this.names.get(key), rs.getLong(key));
//				}else if(clz.equals(Double.class)){
//					bean.put(this.names.get(key), rs.getDouble(key));
//				}else if(clz.equals(Date.class)){
//					bean.put(this.names.get(key), rs.getTimestamp(key));
//				}else if(clz.equals(Boolean.class)){
//					bean.put(this.names.get(key), rs.getInt(key));
//				}else if(clz.equals(BigDecimal.class)){
//					bean.put(this.names.get(key), rs.getBigDecimal(key));
//				}else if(clz.equals(BigInteger.class)){
//					bean.put(this.names.get(key), rs.getBigDecimal(key).toBigInteger());
//				}else if(clz.equals(Blob.class)){
//					bean.put(this.names.get(key), "Blob.class 暂未处理");
//				}else if(clz.equals(Clob.class)){
//					bean.put(this.names.get(key), "Clob.class 暂未处理");
//				}else{
//					throw new DAOException("NOT SUPPORT TYPE :"+clz);
//				}
			}
		} catch (SQLException e) {
			throw new DAOException("rs.getXxx()出错:"+e.getErrorCode()+"原因:\n"+e.getMessage(),e);
		}
		return bean;
	}
}
