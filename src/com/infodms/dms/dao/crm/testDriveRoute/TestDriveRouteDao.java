package com.infodms.dms.dao.crm.testDriveRoute;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.dao.sales.usermng.UserManageDao;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

public class TestDriveRouteDao extends BaseDao<PO> {

	private static final TestDriveRouteDao dao = new TestDriveRouteDao();

	public static final TestDriveRouteDao getInstance() {
		return dao;
	}

	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		return null;
	}

	/**
	 * FUNCTION : 查询试驾路线数据
	 * 
	 * @param :
	 * @return :
	 * @throws : LastUpdate : 2014-12-19
	 */
	public PageResult<Map<String, Object>> getGroupQueryList(Map<String, String> map, int pageSize, int curPage) 
	{
		String routeName = map.get("routeName");
		
		String mileage = map.get("mileage");
		
		StringBuilder sql = new StringBuilder();
		
		sql.append("\n select tdr.route_id,tdr.route_name,tdr.start_line,tdr.end_line,");
		
		sql.append("          tdr.mileage,tdr.remarks \n");
		
		sql.append("   from t_pc_test_drive_route tdr  where 1 = 1  "); 
		
		if (null != routeName && !"".equals(routeName)) 
		{
			sql.append(" and tdr.route_name like '%" + routeName + "%'\n");
		}
		if (null != mileage && !"".equals(mileage)) 
		{
			sql.append(" and tdr.mileage = " + mileage);
		}
		
		sql.append("   order by tdr.create_date desc");

		PageResult<Map<String, Object>> ps = dao.pageQuery(sql.toString(),null, dao.getFunName(), pageSize, curPage);
		
		return ps;
	}
	
	public static List<Map<String, Object>> queryDataList(Map<String, String> map) {
		
		String codeId=map.get("codeId");
		
		Integer status=Constant.IF_TYPE_YES;
		
		StringBuilder sql= new StringBuilder();  
		
		sql.append(" select  tdr.route_id AS TREE_ID ,tdr.route_name AS TREE_NAME, " );
		
		sql.append("'" + codeId + "' AS PARENT_ID,1 AS TREE_LEVEL, 0 as NEXT_COUNT ");
		
		sql.append("  from t_pc_test_drive_route tdr  \n" );
		
		sql.append(" where tdr.status = '"+status+"'\n" );
		
		BaseDao dao=new  UserManageDao();
		
		List<Map<String, Object>> list=dao.pageQuery(sql.toString(), null, dao.getFunName());
		
		return list;
	}
}
