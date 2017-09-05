package com.infodms.dms.dao.parts.storageManager.partPositionMgr;

import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.infodms.dms.dao.common.BaseDao;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

public class PartPositionMgrDao extends BaseDao<PO> {

	private static final PartPositionMgrDao dao = new PartPositionMgrDao ();
	
	private PartPositionMgrDao(){}
	
	public static final PartPositionMgrDao getInstance() {
		return dao;
	}
	
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
	public PageResult<Map<String, Object>> getPartPositionQuery(Map<String, Object> map, int curPage, int pageSize)throws Exception{
		
		String position_code = (String)map.get("POSITION_CODE");
		String floor_ID = (String)map.get("FLOOR_ID");
		String shelf_ID = (String)map.get("SHELF_ID");
		String line_ID = (String)map.get("LINE_ID");
		String whId = (String)map.get("WH_ID");
		
		StringBuffer sql= new StringBuffer();		
		sql.append("SELECT TP.POSITION_ID,TP.POSITION_CODE,TP.STATUS,TP.STATE,TF.FLOOR_CODE,TS.SHELF_CODE,TL.LINE_CODE,TL.LINE_NAME");
		sql.append(" FROM TT_PART_LOCATION_LINE TL, TT_PART_LOCATION_SHELF TS, TT_PART_LOCATION_FLOOR TF, TT_PART_LOCATION_POSITION TP");
		sql.append(" WHERE TP.FLOOR_ID = TF.FLOOR_ID AND TF.SHELF_ID = TS.SHELF_ID AND TS.LINE_ID = TL.LINE_ID");
		
		if(position_code!=null&&!"".equals(position_code)){
			sql.append("   AND TP.POSITION_CODE  LIKE '%"+position_code+"%'\n" );
		}
		if(floor_ID!=null&&!"".equals(floor_ID)){
			sql.append("   AND TF.FLOOR_ID LIKE '%"+floor_ID+"%'\n" );
		}
		if(shelf_ID!=null&&!"".equals(shelf_ID)){
			sql.append("   AND TS.SHELF_ID LIKE '%"+shelf_ID+"%'\n" );
		}
		if(line_ID!=null&&!"".equals(line_ID)){
			sql.append("   AND TL.LINE_ID LIKE '%"+line_ID+"%'\n" );
		}
		if(whId!=null&&!"".equals(whId)){
			sql.append("   AND TL.WH_ID LIKE '%"+whId+"%'\n" );
		}
		sql.append("  ORDER BY TP.POSITION_ID");
		List<Object> params = new LinkedList<Object>();
		PageResult<Map<String, Object>> ps= dao.pageQuery(sql.toString(), params, getFunName(),pageSize,curPage);
		return ps;
	}
	
	public Map<String, Object> getObjectById(String id){
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT * FROM TT_PART_LOCATION_FLOOR T WHERE T.FLOOR_CODE = " + id);
		Map<String, Object> map = pageQueryMap(sql.toString(),null,getFunName());
		return map;
	}
	
	public String getCodeSequenceList(String lineId,String shelfId,String floorId){
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT TL.LINE_CODE,TS.SHELF_CODE,TF.FLOOR_CODE FROM TT_PART_LOCATION_LINE TL,TT_PART_LOCATION_SHELF TS,TT_PART_LOCATION_FLOOR TF " );
		sql.append(" WHERE TL.LINE_ID = "+lineId+" AND TS.SHELF_ID = "+shelfId+" AND TF.FLOOR_ID = "+floorId);
		sql.append(" AND TF.SHELF_ID = TS.SHELF_ID AND TS.LINE_ID = TL.LINE_ID ");
		List<Map<String,Object>> rs = super.pageQuery(sql.toString(), null, getFunName());
		String sequence = null;
		for(Map<String,Object> map : rs){
			String line_code = map.get("LINE_CODE").toString();
			String shelf_code = map.get("SHELF_CODE").toString();
			String floor_code = map.get("FLOOR_CODE").toString();			
			sequence = line_code + "-" + shelf_code + "-" + floor_code;
		}
		return sequence;
	}

}
