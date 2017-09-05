package com.infodms.dms.dao.sales.storage.storagebase;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.common.Constant;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TtSalesSitPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.XHBUtil;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

/**
 * 
 * @ClassName     : ReservoirPositionDao 
 * @Description   : 库位管理DAO 
 * @author        : ranjian
 * CreateDate     : 2013-4-5
 */
public class ReservoirPositionDao extends BaseDao<PO>{
	public static Logger logger = Logger.getLogger(ReservoirPositionDao.class);
	private static final ReservoirPositionDao dao = new ReservoirPositionDao ();
	public static final ReservoirPositionDao getInstance() {
		return dao;
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
	/**
	 * 库位信息查询
	 * @param map
	 * @param curPage
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	public PageResult<Map<String, Object>> getReservoirPositionQuery(Map<String, Object> map, int curPage, int pageSize)throws Exception{

		String yieldly = (String)map.get("YIELDLY");
		String areaName = (String)map.get("AREA_NAME");
		String roadName = (String)map.get("ROAD_NAME");	
		String poseId = (String)map.get("poseId");
		List<Object> params = new LinkedList<Object>();
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT TSS.SIT_ID,TSA.AREA_CODE,\n");
		sql.append("TSA.AREA_NAME,TSR.ROAD_NAME,TSS.SIT_NAME  FROM TT_SALES_SIT TSS, \n");
		sql.append("  TT_SALES_ROAD TSR,TT_SALES_AREA TSA WHERE TSS.ROAD_ID=TSR.ROAD_ID   \n");
		sql.append("  AND TSR.AREA_ID=TSA.AREA_ID   \n");
		sql.append(MaterialGroupManagerDao.getPoseIdBusinessSql("TSA.YIELDLY",poseId));//车厂端查询列表产地数据权限
		if(yieldly!=null&&!"".equals(yieldly)){
			sql.append("   AND TSA.YIELDLY = "+yieldly+"\n" );
		}
		if(areaName!=null&&!"".equals(areaName)){
			sql.append("   AND TSA.AREA_ID ="+areaName+"\n" );
		}
		if(roadName!=null&&!"".equals(roadName)){
			sql.append("   AND TSR.ROAD_ID ="+roadName+"\n" );
		}
		sql.append(" AND TSS.STATUS=").append(Constant.STATUS_ENABLE);
		sql.append(" ORDER BY TSA.YIELDLY,TO_NUMBER(TSA.AREA_NAME),TO_NUMBER(TSR.ROAD_NAME),TO_NUMBER(TSS.SIT_NAME) ASC");
		PageResult<Map<String, Object>> ps= dao.pageQuery(sql.toString(), params, getFunName(),pageSize,curPage);
		return ps;
	}
	public List<Map<String, Object>> getReservoirValue(String yieldly)throws Exception{
		StringBuffer sql= new StringBuffer();
		sql.append("select TSA.AREA_ID,TSA.AREA_NAME from TT_SALES_AREA TSA WHERE 1=1 \n" );
		sql.append(" AND TSA.STATUS=").append(Constant.STATUS_ENABLE);//这里把无效的给加上了
		if(yieldly!=null && !"".equals(yieldly)){
			sql.append(" AND TSA.YIELDLY in(").append(yieldly).append(")\n");
		}
		sql.append(" ORDER BY TSA.AREA_ID");
		List<Map<String, Object>> list= dao.pageQuery(sql.toString(), null, getFunName());
		return list;
	}
	public int getMaxPosition(Map<String, Object> mapParm) {
		String maxNum = "0" ;			
		StringBuffer sql = new StringBuffer("\n") ;		
		sql.append(" select MAX(TO_NUMBER(SIT_NAME)) MAXNUM from TT_SALES_SIT TSS where 1=1 \n" );
		sql.append(" AND TSS.ROAD_ID=").append(mapParm.get("roadName")).append("\n");
		sql.append(" AND TSS.STATUS=").append(Constant.STATUS_ENABLE);//这里把无效的给加上了

		Map<String, Object> map = dao.pageQueryMap(sql.toString(), null, dao.getFunName()) ;
		
		if(!CommonUtils.isNullMap(map)) {
			if(map.get("MAXNUM")!=null){
				maxNum = map.get("MAXNUM").toString() ;
			}
		}		
		return Integer.parseInt(maxNum) ;
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 库位信息添加 
	 * @param      : @param ttSalesAreaPO      
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-5
	 */
	public void reservoirSalesPositionAdd(TtSalesSitPO ttSalesSitPO) {
		dao.insert(ttSalesSitPO);
    }
	/**
	 * 
	 * @Title      : 
	 * @Description: 库位信息修改 
	 * @param      : @param seachSalesAreaPO
	 * @param      : @param ttSalesAreaPO      
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-5
	 */
	public void reservoirSalesPositionUpdate(TtSalesSitPO ttSalesSitPO,TtSalesSitPO seachPo) {
		String sql="update TT_SALES_SIT  TSS " +
				"set TSS.STATUS="+ttSalesSitPO.getStatus() +
						",TSS.UPDATE_BY="+ttSalesSitPO.getUpdateBy()+
						",TSS.UPDATE_DATE=sysdate"+
						" where  ROAD_ID="+seachPo.getRoadId()+
						"AND SIT_NAME='"+seachPo.getSitName()+"'" ;
		dao.update(sql, null);
    }
	/**
	 * 
	 * @Title      : 
	 * @Description: 库位信息修改 (基本)
	 * @param      : @param seachPo
	 * @param      : @param ttSalesSitPO      
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-5
	 */
	public int sitUpdate(TtSalesSitPO seachPo,TtSalesSitPO ttSalesSitPO) {
		
		return dao.factory.update(seachPo, ttSalesSitPO);
    }	
	/**
	 * 
	 * @Title      : 
	 * @Description: 根据ID获取单条库位信息
	 * @param      : @param param map列表   
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-5
	 */

	public Map<String, Object> getSitById(List<Object> param)throws Exception{
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT TSS.SIT_ID,TSS.ROAD_ID,\n");
		sql.append("TSS.SIT_NAME,TSS.VEHICLE_ID  FROM TT_SALES_SIT TSS WHERE TSS.STATUS=? AND TSS.SIT_ID=?\n");
		Map<String, Object> map= dao.pageQueryMap(sql.toString(), param, getFunName());
		return map;
	}
	
	/**
	 * 根据VEHICLE_ID查询库道信息
	 * @author liufazhong
	 */
	public Map<String, Object> getSitByVehicleId(String vehicleId) throws Exception {
		StringBuffer sql = new StringBuffer("SELECT * FROM TT_SALES_SIT WHERE 1=1");
		List<Object> params = new ArrayList<Object>();
		if (!XHBUtil.IsNull(vehicleId)) {
			sql.append(" AND VEHICLE_ID = ?");
			params.add(vehicleId);
		}
		return dao.pageQueryMap(sql.toString(), params, getFunName());
	}
	
	/**
	 * 获取车辆信息
	 * @author liufazhong
	 */
	public Map<String, Object> getVehicleInfo(String vehicleId) throws Exception {
		StringBuffer sql = new StringBuffer("SELECT * FROM TM_VEHICLE WHERE 1=1");
		List<Object> params = new ArrayList<Object>();
		if (!XHBUtil.IsNull(vehicleId)) {
			sql.append(" AND VEHICLE_ID = ?");
			params.add(vehicleId);
		}
		return dao.pageQueryMap(sql.toString(), params, getFunName());
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 根据vechicle_id获取单条库位信息
	 * @param      : @param param map列表   
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-5
	 */

	public Map<String, Object> getVehicleById(List<Object> param)throws Exception{
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT TSS.SIT_ID,TSS.ROAD_ID,\n");
		sql.append("TSS.SIT_NAME,TSS.VEHICLE_ID  FROM TT_SALES_SIT TSS WHERE TSS.STATUS=? AND TSS.VEHICLE_ID=?\n");
		Map<String, Object> map= dao.pageQueryMap(sql.toString(), param, getFunName());
		return map;
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 查询库位上是否有车
	 * @param      : @param ttSalesAreaPO      
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-3
	 */
	public List<Map<String,Object>> getSitIsHaveVehicle(List<Object> params){
		StringBuffer sbSql = new StringBuffer("\n") ;		
		sbSql.append("SELECT A.SIT_NAME\n");
		sbSql.append("  FROM TT_SALES_SIT A\n");
		sbSql.append(" WHERE A.ROAD_ID = ?\n");
		sbSql.append("   AND A.STATUS = ?\n");
		sbSql.append("   AND TO_NUMBER(A.SIT_NAME) BETWEEN ? AND ?\n");
		sbSql.append("   AND A.VEHICLE_ID != ?\n");
		sbSql.append(" ORDER BY TO_NUMBER(A.SIT_NAME)"); 
		List<Map<String,Object>> list=dao.pageQuery(sbSql.toString(), params, getFunName());
		return list;
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 根据区间获取库道ID
	 * @param      : @param ttSalesAreaPO      
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-3
	 */
	public List<Map<String,Object>> getRoadIdByRd(String startRoad,String endRoad,String areaId){
		StringBuffer sbSql = new StringBuffer("\n") ;		
		sbSql.append("SELECT A.ROAD_ID,A.ROAD_NAME\n");
		sbSql.append("  FROM TT_SALES_ROAD A\n");
		sbSql.append(" WHERE A.AREA_ID = "+areaId+"\n");
		sbSql.append("   AND A.STATUS = "+Constant.STATUS_ENABLE+"\n");
		sbSql.append("   AND TO_NUMBER(A.ROAD_NAME) BETWEEN TO_NUMBER("+startRoad+") AND TO_NUMBER("+endRoad+")"); 
		List<Object> param=new ArrayList<Object>();
		param.add(areaId);
		param.add(Constant.STATUS_ENABLE);
		param.add(startRoad);
		param.add(endRoad);
		List<Map<String,Object>> list=dao.pageQuery(sbSql.toString(), null, getFunName());
		return list;
	}
	
	/**
	 * 获取区道位以及车辆信息
	 * @author liufazhong
	 */
	public List<Map<String, Object>> getSit(String yieldlyId,String areaId,String startRoad,String endRoad){
		StringBuffer sql = new StringBuffer("SELECT A.AREA_CODE,A.AREA_NAME,B.ROAD_NAME,C.SIT_NAME,D.VIN" +
				" FROM TT_SALES_AREA a,TT_SALES_ROAD b,TT_SALES_SIT c,TM_VEHICLE D" +
				" WHERE a.area_id=b.area_id AND b.road_id=c.road_id AND C.VEHICLE_ID = D.VEHICLE_ID(+)");
		List<Object> params = new ArrayList<Object>();
		if (!XHBUtil.IsNull(yieldlyId)) {
			sql.append(" AND A.YIELDLY = ?");
			params.add(yieldlyId);
		}
		if (!XHBUtil.IsNull(areaId)) {
			sql.append(" AND A.AREA_ID = ?");
			params.add(areaId);
		}
		if (!XHBUtil.IsNull(startRoad)) {
			sql.append(" AND TO_NUMBER(B.ROAD_NAME) >= TO_NUMBER(?)");
			params.add(startRoad);
		}
		if (!XHBUtil.IsNull(endRoad)) {
			sql.append(" AND TO_NUMBER(B.ROAD_NAME) <= TO_NUMBER(?)");
			params.add(endRoad);
		}
		return pageQuery(sql.toString(), params, getFunName());
	}
	
	public List<Map<String, Object>> getSitStartToEnd(String yieldlyId,String areaId,String startRoad,String endRoad,String startSit,String endSit){
		StringBuffer sql = new StringBuffer("SELECT A.AREA_CODE,A.AREA_NAME,B.ROAD_NAME,C.SIT_NAME,D.VIN" +
				" FROM TT_SALES_AREA a,TT_SALES_ROAD b,TT_SALES_SIT c,TM_VEHICLE D" +
				" WHERE a.area_id=b.area_id AND b.road_id=c.road_id AND C.VEHICLE_ID = D.VEHICLE_ID(+)");
		List<Object> params = new ArrayList<Object>();
		if (!XHBUtil.IsNull(yieldlyId)) {
			sql.append(" AND A.YIELDLY = ?");
			params.add(yieldlyId);
		}
		if (!XHBUtil.IsNull(areaId)) {
			sql.append(" AND A.AREA_ID = ?");
			params.add(areaId);
		}
		if (!XHBUtil.IsNull(startRoad)) {
			sql.append(" AND TO_NUMBER(B.ROAD_NAME) >= TO_NUMBER(?)");
			params.add(startRoad);
		}
		if (!XHBUtil.IsNull(endRoad)) {
			sql.append(" AND TO_NUMBER(B.ROAD_NAME) <= TO_NUMBER(?)");
			params.add(endRoad);
		}
		if (!XHBUtil.IsNull(startSit)) {
			sql.append(" AND TO_NUMBER(C.SIT_NAME) >= TO_NUMBER(?)");
			params.add(startSit);
		}
		if (!XHBUtil.IsNull(endSit)) {
			sql.append(" AND TO_NUMBER(C.SIT_NAME) <= TO_NUMBER(?)");
			params.add(endSit);
		}
		return pageQuery(sql.toString(), params, getFunName());
	}
}
