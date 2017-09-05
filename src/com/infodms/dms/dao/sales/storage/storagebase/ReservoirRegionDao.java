package com.infodms.dms.dao.sales.storage.storagebase;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TtSalesAreaMatPO;
import com.infodms.dms.po.TtSalesAreaPO;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

/**
 * 
 * @ClassName     : reservoirRegionDao 
 * @Description   : 库区管理DAO 
 * @author        : ranjian
 * CreateDate     : 2013-4-1
 */
public class ReservoirRegionDao extends BaseDao<PO>{
	private static final ReservoirRegionDao dao = new ReservoirRegionDao ();
	public static final ReservoirRegionDao getInstance() {
		return dao;
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
	/**
	 * 库区信息查询
	 * @param map
	 * @param curPage
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	public PageResult<Map<String, Object>> getReservoirRegionQuery(Map<String, Object> map, int curPage, int pageSize)throws Exception{
		
		String areaCode = (String)map.get("AREA_CODE");
		String areaName = (String)map.get("AREA_NAME");
		String dutyPer = (String)map.get("DUTY_PER");
		String dutyTel = (String)map.get("DUTY_TEL");
		String type = (String)map.get("TYPE");
		String yieldly = (String)map.get("YIELDLY");	
		String poseId = (String)map.get("poseId");
		String remark = (String)map.get("REMARK");
		List<Object> params = new LinkedList<Object>();
		
		StringBuffer sql= new StringBuffer();
		
		sql.append("select TS.AREA_ID,TS.AREA_CODE,TS.AREA_NAME,TS.DUTY_PER,TS.DUTY_TEL,TS.TYPE, TS.REMARK,TS.STATUS, \n");
		sql.append("TS.YIELDLY YIELDLY_ID,TBA.AREA_NAME YIELDLY,TS.IN_STATUS,TS.OUT_STATUS, \n");
		sql.append(" (SELECT COUNT(TSR.ROAD_ID) ROAD_COUNT FROM TT_SALES_ROAD TSR WHERE TSR.AREA_ID=TS.AREA_ID) ROAD_COUNT,\n");
		sql.append("(SELECT COUNT(TSS.SIT_ID) SIT_ID FROM TT_SALES_SIT TSS ,TT_SALES_ROAD TSR WHERE TSS.ROAD_ID=TSR.ROAD_ID AND TSR.AREA_ID=TS.AREA_ID) SIT_COUNT\n");

		sql.append(" from TT_SALES_AREA TS,TM_BUSINESS_AREA TBA  where TS.YIELDLY=TBA.AREA_ID \n");
		sql.append(MaterialGroupManagerDao.getPoseIdBusinessSql("TS.YIELDLY",poseId));//车厂端查询列表产地数据权限
		
		if(areaCode!=null&&!"".equals(areaCode)){
			sql.append("   AND TS.AREA_CODE  like ? \n" );
			params.add("%"+areaCode+"%");
		}
		if(areaName!=null&&!"".equals(areaName)){
			sql.append("   AND TS.AREA_NAME LIKE ? \n" );
			params.add("%"+areaName+"%");
		}
		
		if(dutyPer!=null&&!"".equals(dutyPer)){
			sql.append("   AND TS.DUTY_PER  LIKE ? \n" );
			params.add("%"+dutyPer+"%");
		}
		if(dutyTel!=null&&!"".equals(dutyTel)){
			sql.append("   AND TS.DUTY_TEL LIKE ? \n" );
			params.add("%"+dutyTel+"%");
		}
		if(type!=null&&!"".equals(type)){
			sql.append("   AND TS.TYPE = "+type+"\n" );
		}
		if(yieldly!=null&&!"".equals(yieldly)){
			sql.append("   AND TS.YIELDLY = "+yieldly+"\n" );
		}
		if(remark!=null&&!"".equals(remark)){
			sql.append("   AND TS.REMARK LIKE ? \n" );
			params.add("%"+remark+"%");
		}
		sql.append(" ORDER BY TS.YIELDLY,TO_NUMBER(TS.AREA_NAME) ASC");
		PageResult<Map<String, Object>> ps= dao.pageQuery(sql.toString(), params, getFunName(),pageSize,curPage);
		return ps;
	}
	/**
	 * 库区信息查询车系信息
	 * @param map
	 * @param curPage
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	public PageResult<Map<String, Object>> getReservoirRegionQueryByMata(Map<String, Object> map, int curPage, int pageSize)throws Exception{
		String yeId=(String)map.get("yeId");
		StringBuffer sbSql= new StringBuffer();
		sbSql.append("SELECT AG.RELATION_ID, TG.GROUP_ID, TG.GROUP_CODE, TG.GROUP_NAME\n");
		sbSql.append("  FROM TM_AREA_GROUP AG, TM_VHCL_MATERIAL_GROUP TG\n");
		sbSql.append(" WHERE AG.MATERIAL_GROUP_ID = TG.GROUP_ID\n");
		sbSql.append("   AND AG.AREA_ID = ?\n");
		sbSql.append(" ORDER BY AG.CREATE_DATE DESC, TG.CREATE_DATE DESC"); 
		List<Object> params=new ArrayList<Object>();
		params.add(yeId);
		PageResult<Map<String, Object>> ps= dao.pageQuery(sbSql.toString(), params, getFunName(),pageSize,curPage);
		return ps;
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 库区信息添加 
	 * @param      : @param ttSalesAreaPO      
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-2
	 */
	public void reservoirSalesAreaAdd(TtSalesAreaPO ttSalesAreaPO) {
		dao.insert(ttSalesAreaPO);
    }
	/**
	 * 
	 * @Title      : 
	 * @Description: 库区与车系信息表添加 
	 * @param      : @param ttSalesAreaMatPO      
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-2
	 */
	public void reservoirSalesAreaMataAdd(TtSalesAreaMatPO ttSalesAreaMatPO) {
		dao.insert(ttSalesAreaMatPO);
    }
	/**
	 * 
	 * @Title      : 
	 * @Description: 库区信息修改 
	 * @param      : @param seachSalesAreaPO
	 * @param      : @param ttSalesAreaPO      
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-2
	 */
	public  void reservoirSalesAreaUpdate(TtSalesAreaPO seachSalesAreaPO,TtSalesAreaPO ttSalesAreaPO) {
		dao.update(seachSalesAreaPO, ttSalesAreaPO);
    }
	/**
	 * 
	 * @Title      : 
	 * @Description: 根据库区ID删除库区与车系信息
	 * @param      : @param seachSalesAreaPO      
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-2
	 */
	public void reservoirSalesAreaDelete(TtSalesAreaPO seachSalesAreaPO) {
		List<Long> params=new ArrayList<Long>();
		params.add(seachSalesAreaPO.getAreaId());
		dao.delete("delete from TT_SALES_AREA_MAT where AREA_ID=?", params);
    }
	/**
	 * 
	 * @Title      : 
	 * @Description: 根据库区ID得到库区与车系关联信息
	 * @param      : @param id
	 * @param      : @return     库区信息列表 
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-2
	 */
	public  List<TtSalesAreaMatPO>  getReservoirMata(TtSalesAreaMatPO po){
		return dao.factory.select(po);
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 根据库区ID得到库区信息
	 * @param      : @param id
	 * @param      : @return     库区信息列表 
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-2
	 */
	public Map<String, Object> getReservoirMsg(String id){
		StringBuffer sql= new StringBuffer();
		sql.append("select TS.AREA_ID,TS.AREA_CODE,TS.AREA_NAME,TS.DUTY_PER,TS.DUTY_TEL,TS.TYPE,\n");
		sql.append("TS.YIELDLY,TS.IN_STATUS,TS.OUT_STATUS,TS.REMARK,TS.STATUS from TT_SALES_AREA TS where TS.AREA_ID=\n");
		sql.append(id);
		Map<String, Object> map = pageQueryMap(sql.toString(),null,getFunName());
		return map;
	}
}
