package com.infoservice.dms.chana.dao;

import java.sql.ResultSet;

import com.infodms.dms.common.Utility;
import com.infodms.dms.dao.common.BaseDao;
import com.infoservice.dms.chana.common.DEConstant;
import com.infoservice.po3.bean.PO;

public class CommonDao extends BaseDao<PO> implements DEConstant {

	private static final CommonDao dao = new CommonDao();
	
	public static final CommonDao getInstance() {
		return dao;
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * 
	* @Title: updateSending 
	* @Description: TODO(更新接口的状态从未发送到发送中) 
	* @param @param tableName 表名
	* @param @param colName where字段
	* @param @param colValue where值
	* @param @return    设定文件 
	* @return int    返回类型 
	* @throws
	 */
	public int updateSending(String tableName, String colName, String colValue) {
		StringBuffer sql = new StringBuffer("");
		sql.append(" UPDATE ").append(tableName);
		sql.append(" SET IF_STATUS = ").append(IF_STATUS_1);
		sql.append(" WHERE IF_STATUS = ").append(IF_STATUS_0);
		if (Utility.testString(colName)) {
			sql.append(" AND ").append(colName).append(" = '").append(colValue).append("'");
		}
		return update(sql.toString(), null);
	}
	
	/**
	 * 
	* @Title: updateComplete 
	* @Description: TODO(更新接口的状态从发送中到已完成) 
	* @param @param tableName 表名
	* @param @param colName where字段
	* @param @param colValue where值
	* @param @return    设定文件 
	* @return int    返回类型 
	* @throws
	 */
	public int updateComplete(String tableName, String colName, String colValue) {
		StringBuffer sql = new StringBuffer("");
		sql.append(" UPDATE ").append(tableName);
		sql.append(" SET IF_STATUS = ").append(IF_STATUS_2);
		sql.append(" WHERE 1 = 1");
		//sql.append(" WHERE IF_STATUS = ").append(IF_STATUS_1);
		if (Utility.testString(colName)) {
			sql.append(" AND ").append(colName).append(" = '").append(colValue).append("'");
		}
		return update(sql.toString(), null);
	}
	/**
	 * 
	* @Title: updateComplete 
	* @Description: TODO(更新接口的状态从发送中到已完成 根据id值进行更新) 
	* @param @param tableName 表名
	* @param @param colName where字段
	* @param @param colValue where值
	* @param @return    设定文件 
	* @return int    返回类型 
	* @throws
	 */
	public int updateComplete(String tableName, String colName, Integer colValue) {
		StringBuffer sql = new StringBuffer("");
		sql.append(" UPDATE ").append(tableName);
		sql.append(" SET IF_STATUS = ").append(IF_STATUS_2);
		sql.append(" WHERE 1 = 1");
		sql.append(" AND ").append(colName).append(" = ").append(colValue).append("");
		return update(sql.toString(), null);
	}

	/**
	 * 
	* @Title: updateSendingFromComplete 
	* @Description: TODO(更新接口的状态从已完成到发送中) 
	* @param @param tableName 表名
	* @param @param colName where字段
	* @param @param colValue where值
	* @param @return    设定文件 
	* @return int    返回类型 
	* @throws
	 */
	public int updateSendingFromComplete(String tableName, String colName, String colValue) {
		StringBuffer sql = new StringBuffer("");
		sql.append(" UPDATE ").append(tableName);
		sql.append(" SET IF_STATUS = ").append(IF_STATUS_1);
		sql.append(" WHERE IF_STATUS = ").append(IF_STATUS_2);
		if (Utility.testString(colName)) {
			sql.append(" AND ").append(colName).append(" = '").append(colValue).append("'");
		}
		return update(sql.toString(), null);
	}
}
