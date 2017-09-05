package com.infodms.dms.util;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class PersisUtil {
	private static Map<String,String[][]> pers = new HashMap<String,String[][]>();
	
	public static String[][] getFieldsNames(String key,ResultSet rs){
		String[][] o = pers.get(key);
		ResultSetMetaData metaData = null ;
		try {
			metaData = rs.getMetaData();
			int len = metaData.getColumnCount() ;
			int len_ = 0 ;
			if (o != null && o[0] != null) {
				len_ = o[0].length;
			}
			if(o == null || len != len_){
				o = getFieldsNameType(rs);
				putFieldsNames(key,o);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("");
		}
		
		return o;
	}
	
	public static void putFieldsNames(String sql,String[][] names){
		pers.put(sql,names);
	}
	
	
	public static Map getMap(String key,ResultSet rs){
		String [][]fileds = getFieldsNames(key,rs);
		Map result = null;
		try {
				result = new HashMap();
			  for(int i=0; i<fileds[0].length; i++){
				  result.put(fileds[0][i], rs.getObject(i+1));
			    }
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException("");
		}
		return result;
	}
	 /**
	   * 根据结果集得到所有字段名称和类型
	   * @param   ResultSet rs 结果集
	   * @return  2唯字段数组，[0][]为字段名称[1][]为类型
	   */
	  public static String[][] getFieldsNameType(ResultSet rs){
		ResultSetMetaData metaData = null;
		try {
			metaData = rs.getMetaData();

			int i = metaData.getColumnCount();
			String[][] field = new String[2][i];
			for (int j = 0; j < i; j++) {
				field[0][j] = (metaData.getColumnName(j + 1)).toUpperCase();
				field[1][j] = metaData.getColumnClassName(j + 1);
			}
			return field;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException("");
		}
	  }
}

