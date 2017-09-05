package com.infodms.dms.dao.sales.tsilogmng;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


import com.infodms.dms.dao.common.BaseDao;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

public class TsiLogDao extends BaseDao {

	private TsiLogDao(){};
	
	/**
	 * 2015.1.7 艾春添加饿汉式单例
	 */
	private static final TsiLogDao instance = new TsiLogDao();	
	public static final TsiLogDao getTiLogDao(){
		return instance;
	}
	
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}


	public PageResult<Map<String, Object>> getTableList(String tableName, String tabName,int pageSize,
			int curPage) {
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT T.TABLE_NAME, T.COMMENTS\n" );
		sql.append(" FROM USER_TAB_COMMENTS T\n" );
		sql.append("where (T.TABLE_NAME LIKE 'TSI_EXP%' OR T.TABLE_NAME LIKE 'TSI_IMP%' OR T.TABLE_NAME LIKE 'TSI_TT%')\n" );
		if(null != tabName && !"".equals(tabName)){
			sql.append(" AND T.TABLE_NAME  like '%"+tabName+"%'\n");
		}
		if(null != tableName && !"".equals(tableName)){
			sql.append(" AND T.COMMENTS  like '%"+tableName+"%'\n");
		}
		sql.append(" ORDER BY T.COMMENTS\n" );
		return pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getTableInfo(String tableName) {
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT CC.COLUMN_NAME, CC.COMMENTS\n" );
		sql.append("  FROM USER_COL_COMMENTS CC, USER_TAB_COLUMNS TC\n" );
		sql.append(" WHERE CC.TABLE_NAME = TC.TABLE_NAME\n" );
		sql.append("   AND CC.COLUMN_NAME = TC.COLUMN_NAME\n" );
		sql.append("   AND CC.COMMENTS IS NOT NULL\n" );
		sql.append("   AND CC.COLUMN_NAME != 'ID'\n" );
		sql.append("   AND CC.COLUMN_NAME != 'IS_DEAL'\n");

		List<Object> params = new ArrayList<Object>();
		if(tableName != null && !"".equals(tableName)){
			sql.append("   and cc.table_name = ? ");
			params.add(tableName);
		}
		sql.append(" ORDER BY TC.COLUMN_ID");
		return pageQuery(sql.toString(), params, getFunName());
	}

	public PageResult<Map<String, Object>> queryTableContent(String tableName,
			String dateStart, String endDate, int pageSize, int curPage) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT * FROM ").append(tableName).append("  T \n");
		sql.append(" WHERE 1=1 ");
		
		if(null !=dateStart && !"".equals(dateStart)){
			sql.append(" AND  T.CREATE_DATE >= TO_DATE('"+dateStart+" 00:00:00','yyyy-mm-dd hh24:mi:ss') ");
		}
		
		if(null !=endDate && !"".equals(endDate)){
			sql.append(" AND  T.CREATE_DATE <= TO_DATE('"+endDate+" 23:59:59','yyyy-mm-dd hh24:mi:ss') ");
		}
		sql.append(" order by create_Date desc");
		return pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
	}
	
	
	public List<Map<String,Object>> exportTableContent(String tableName,
			String dateStart, String endDate){
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT * FROM ").append(tableName).append("  T \n");
		sql.append(" WHERE 1=1 ");
		
		if(null !=dateStart && !"".equals(dateStart)){
			sql.append(" AND  T.CREATE_DATE >= TO_DATE('"+dateStart+" 00:00:00','yyyy-mm-dd hh24:mi:ss') ");
		}
		
		if(null !=endDate && !"".equals(endDate)){
			sql.append(" AND  T.CREATE_DATE <= TO_DATE('"+endDate+" 23:59:59','yyyy-mm-dd hh24:mi:ss') ");
		}
		sql.append(" order by create_Date desc");
		return pageQuery(sql.toString(), null, getFunName());
	}
}
