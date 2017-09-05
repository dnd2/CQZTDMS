package com.infodms.dms.dao.parts.storageManager.partFloorMgr;

import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.infodms.dms.dao.common.BaseDao;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

public class PartFloorMgrDao extends BaseDao<PO> {

	private static final PartFloorMgrDao dao = new PartFloorMgrDao ();
	
	private PartFloorMgrDao(){}
	
	public static final PartFloorMgrDao getInstance() {
		return dao;
	}
	
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
	public PageResult<Map<String, Object>> getPartFloorQuery(Map<String, Object> map, int curPage, int pageSize)throws Exception{
		
		String line_id = (String)map.get("LINE_ID");
		String shelf_id = (String)map.get("SHELF_ID");
		String floor_code = (String)map.get("FLOOR_CODE");
		String whId = (String)map.get("WH_ID");
		
		StringBuffer sql= new StringBuffer();		
		sql.append("SELECT TF.FLOOR_ID,\n");
		sql.append("       TF.FLOOR_CODE,\n");
		sql.append("       TS.SHELF_CODE,\n");
		sql.append("       TLL.LINE_CODE,\n");
		sql.append("       TLL.LINE_NAME,\n");
		sql.append("       TF.STATUS,\n");
		sql.append("       TF.STATE,\n");
		sql.append("       (SELECT COUNT(TP.POSITION_ID)\n");
		sql.append("          FROM TT_PART_LOCATION_POSITION TP\n");
		sql.append("         WHERE TP.FLOOR_ID = TF.FLOOR_ID\n");
		sql.append("           AND TF.SHELF_ID = TS.SHELF_ID\n");
		sql.append("           AND TS.LINE_ID = TLL.LINE_ID) POSITION_COUNT\n");
		sql.append("  FROM TT_PART_LOCATION_SHELF TS,\n");
		sql.append("       TT_PART_LOCATION_LINE  TLL,\n");
		sql.append("       TT_PART_LOCATION_FLOOR TF\n");
		sql.append(" WHERE TS.LINE_ID = TLL.LINE_ID\n");
		sql.append("   AND TF.SHELF_ID = TS.SHELF_ID");

		if(floor_code!=null&&!"".equals(floor_code)){
			sql.append("   AND TF.FLOOR_CODE  LIKE '%"+floor_code+"%'\n" );
		}
		if(shelf_id!=null&&!"".equals(shelf_id)){
			sql.append("   AND TS.SHELF_ID  LIKE '%"+shelf_id+"%'\n" );
		}
		if(line_id!=null&&!"".equals(line_id)){
			sql.append("   AND TLL.LINE_ID  LIKE '%"+line_id+"%'\n" );
		}
		if(whId!=null&&!"".equals(whId)){
			sql.append("   AND TLL.WH_ID LIKE '%"+whId+"%'\n" );
		}
		sql.append("   ORDER BY TO_NUMBER(TF.FLOOR_ID) ASC");
		List<Object> params = new LinkedList<Object>();
		PageResult<Map<String, Object>> ps= dao.pageQuery(sql.toString(), params, getFunName(),pageSize,curPage);
		return ps;
	}
	
	public List<Map<String,Object>> getShelfCodeList(){
		
		StringBuffer sql= new StringBuffer();
		sql.append(" SELECT T.SHELF_CODE,T.SHELF_ID FROM TT_PART_LOCATION_SHELF T");
		List<Map<String,Object>> rs = super.pageQuery(sql.toString(), null, getFunName());
		return rs;
		
	}
	
	public List<Map<String,Object>> getSubCodeList(String table,String column,String val){
		StringBuffer sql= new StringBuffer();
		sql.append(" SELECT * FROM "+table+" T WHERE T."+column+" = "+val);
		List<Map<String,Object>> rs = super.pageQuery(sql.toString(), null, getFunName());
		return rs;
		
	}

}
