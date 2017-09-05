package com.infodms.dms.dao.common;

import java.sql.ResultSet;
import java.util.List;

import com.infoservice.po3.bean.PO;

public class DataSetDAO extends BaseDao<PO> {

	private static DataSetDAO instance = null;
	
	private DataSetDAO() {
		
	}
	
	public static DataSetDAO getInstance() {
		if(instance == null) {
			instance = new DataSetDAO();
		} 
		return instance;
	}
	
	/**
	 * 获取TM_DATA_SET表中所有年份
	 * @param 
	 * @return 年份列表
	 * */
	public List<String> getAllYear() {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT DISTINCT TDS.SET_YEAR\n");
		sql.append("  FROM TM_DATE_SET TDS\n");  
		sql.append(" WHERE 1 = 1\n");  
		sql.append(" ORDER BY TDS.SET_YEAR\n");
		return this.selectTmDataSet(sql, "SET_YEAR");
	}
	
	/**
	 * 获取TM_DATA_SET表中所有月份
	 * @param year 选择的年份
	 * @return 月份列表
	 * */
	public List<String> getAllMonthByYear(String year) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT DISTINCT TDS.SET_MONTH\n");
		sql.append("  FROM TM_DATE_SET TDS\n");  
		sql.append(" WHERE 1 = 1\n");  
		if(year != null && !"".equals(year)) {
			sql.append("   AND TDS.SET_YEAR = ");
			sql.append(year);
			sql.append("\n");  
		}
		sql.append(" ORDER BY TO_DATE(TDS.SET_MONTH, 'mm')\n");
		return this.selectTmDataSet(sql, "SET_MONTH");
	}
	
	/**
	 * 获取TM_DATA_SET表中所有周度
	 * @param year 选择的年份
	 * @return 周度列表
	 * */
	public List<String> getAllWeekByYear(String year) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT DISTINCT TDS.SET_WEEK\n");
		sql.append("  FROM TM_DATE_SET TDS\n");  
		sql.append(" WHERE 1 = 1\n");  
		if(year != null && !"".equals(year)) {
			sql.append("   AND TDS.SET_YEAR = ");
			sql.append(year);
			sql.append("\n");  
		}
		sql.append(" ORDER BY LPAD(TDS.SET_WEEK, 2, '0')\n");
		return this.selectTmDataSet(sql, "SET_WEEK");
	}

	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
	

}
