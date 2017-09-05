package com.infodms.dms.dao.sales.storage.storagebase;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.infodms.dms.bean.TtSalesRoadBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TtSalesRoadPO;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

/**
 * 
 * @ClassName     : ReservoirRoadDao 
 * @Description   : 库道管理DAO 
 * @author        : ranjian
 * CreateDate     : 2013-4-2
 */
public class ReservoirRoadDao extends BaseDao<PO>{
	private static final ReservoirRoadDao dao = new ReservoirRoadDao ();
	public static final ReservoirRoadDao getInstance() {
		return dao;
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
	/**
	 * 库道信息查询
	 * @param map
	 * @param curPage
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	public PageResult<Map<String, Object>> getReservoirRoadQuery(Map<String, Object> map, int curPage, int pageSize)throws Exception{

		String yieldly = (String)map.get("YIELDLY");
		String areaName = (String)map.get("AREA_NAME");
		String roadName = (String)map.get("ROAD_NAME");	
		String poseId = (String)map.get("poseId");
		List<Object> params = new LinkedList<Object>();
		
		StringBuffer sql= new StringBuffer();

		sql.append("select TSR.ROAD_ID,TSR.ROAD_NAME,TSR.IN_STATUS,TSR.OUT_STATUS,TSA.AREA_CODE,\n");
		sql.append("TSA.AREA_NAME,TSA.YIELDLY AREA_ID,TBA.AREA_NAME YIELDLY,(SELECT COUNT(TSS.SIT_ID) SIT_ID FROM TT_SALES_SIT TSS \n");
		sql.append(" WHERE TSS.ROAD_ID=TSR.ROAD_NAME) SIT_ID  from TT_SALES_ROAD TSR,TM_BUSINESS_AREA TBA,TT_SALES_AREA TSA  \n");
		sql.append(" WHERE TSA.AREA_ID=TSR.AREA_ID AND TSA.YIELDLY=TBA.AREA_ID  \n");
		sql.append(MaterialGroupManagerDao.getPoseIdBusinessSql("TSA.YIELDLY",poseId));//车厂端查询列表产地数据权限
		if(yieldly!=null&&!"".equals(yieldly)){
			sql.append("   AND TSA.YIELDLY = "+yieldly+"\n" );
		}
		if(areaName!=null&&!"".equals(areaName)){
			sql.append("   AND TSA.AREA_ID ="+areaName+"\n" );
		}
		if(roadName!=null&&!"".equals(roadName)){
			sql.append("   AND TSR.ROAD_NAME  LIKE '%"+roadName+"%'\n" );
		}
		sql.append(" AND TSR.STATUS=").append(Constant.STATUS_ENABLE);
		sql.append(" ORDER BY TSA.YIELDLY,TO_NUMBER(TSA.AREA_NAME),TO_NUMBER(TSR.ROAD_NAME) ASC");
		PageResult<Map<String, Object>> ps= dao.pageQuery(sql.toString(), params, getFunName(),pageSize,curPage);
		return ps;
	}
	public List<Map<String, Object>> getReservoirValue(String yieldly)throws Exception{
		StringBuffer sql= new StringBuffer();
		sql.append("select TSA.AREA_ID,TSA.AREA_NAME from TT_SALES_AREA TSA WHERE 1=1 \n" );
		sql.append(" AND TSA.STATUS=").append(Constant.STATUS_ENABLE);//这里把无效的给加上了
		if(yieldly!=null && !"".equals(yieldly)){
			sql.append(" AND TSA.YIELDLY=").append(yieldly);
		}
		sql.append(" ORDER BY TSA.AREA_ID");
		List<Map<String, Object>> list= dao.pageQuery(sql.toString(), null, getFunName());
		return list;
	}
	/**
	 * 取得库道数
	 * @param yieldly
	 * @return
	 * @throws Exception
	 */
	public String getAreaRoadNum(String yieldly,String kAreaId)throws Exception{
		StringBuffer sbSql=new StringBuffer();
		sbSql.append("SELECT   COUNT (B.ROAD_ID) ROAD_NUM\n");
		sbSql.append("  FROM   TT_SALES_AREA A, TT_SALES_ROAD B\n");
		sbSql.append(" WHERE       A.AREA_ID = B.AREA_ID\n");
		sbSql.append("         AND B.STATUS = ?\n");
		sbSql.append("         AND A.AREA_ID = ?\n");
		sbSql.append("         AND A.YIELDLY = ?\n");
		List<Object> parm=new ArrayList<Object>();
		parm.add(Constant.STATUS_ENABLE);
		parm.add(Long.valueOf(kAreaId));
		parm.add(Long.valueOf(yieldly));
		List<Map<String, Object>> list= dao.pageQuery(sbSql.toString(), parm, getFunName());
		return list.get(0).get("ROAD_NUM").toString();
	}
	
	/**
	 * 取得库位数
	 * @param yieldly
	 * @return
	 * @throws Exception
	 */
	public String getRoadSitNum(String areaId,String roadId)throws Exception{
		StringBuffer sbSql=new StringBuffer();
		sbSql.append("SELECT   COUNT (C.SIT_ID) SIT_NUM\n");
		sbSql.append("  FROM   TT_SALES_ROAD B, TT_SALES_AREA D, TT_SALES_SIT C\n");
		sbSql.append(" WHERE       B.ROAD_ID = C.ROAD_ID\n");
		sbSql.append("         AND B.AREA_ID = D.AREA_ID\n");
		sbSql.append("         AND C.ROAD_ID = ?\n");
		sbSql.append("         AND B.AREA_ID = ?\n");
		sbSql.append("         AND C.STATUS = ?\n");
		
		List<Object> par=new ArrayList<Object>();
		par.add(Long.valueOf(roadId));
		par.add(Long.valueOf(areaId));
		par.add(Constant.STATUS_ENABLE);
		List<Map<String, Object>> list= dao.pageQuery(sbSql.toString(), par, getFunName());
		return list.get(0).get("SIT_NUM").toString();
	}
	
	public int getMaxRoad(Map<String, Object> mapParm) {
		String maxNum = "0" ;			
		StringBuffer sql = new StringBuffer("\n") ;		
		sql.append("SELECT MAX(TO_NUMBER(ROAD_NAME)) MAXNUM FROM TT_SALES_ROAD TSR,TT_SALES_AREA TSA WHERE TSR.AREA_ID=TSA.AREA_ID \n" );
		sql.append(" AND TSA.AREA_ID=").append(mapParm.get("areaName")).append("\n");
		sql.append(" AND TSA.YIELDLY=").append(mapParm.get("YIELDLY")).append(" \n" );
		sql.append(" AND TSR.STATUS=").append(Constant.STATUS_ENABLE);//这里把无效的给加上了

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
	 * @Description: 库道信息添加 
	 * @param      : @param ttSalesAreaPO      
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-3
	 */
	public void reservoirSalesRoadAdd(TtSalesRoadPO ttSalesRoadPO) {
		dao.insert(ttSalesRoadPO);
    }
	/**
	 * 
	 * @Title      : 
	 * @Description: 库道信息修改 
	 * @param      : @param seachSalesAreaPO
	 * @param      : @param ttSalesAreaPO      
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-3
	 */
	public void reservoirSalesRoadUpdate(TtSalesRoadPO salesRoadPO,TtSalesRoadBean ttSalesRoadBean) {
		String sql="update TT_SALES_ROAD  TSR " +
				"set TSR.STATUS="+salesRoadPO.getStatus() +
						",TSR.UPDATE_BY="+salesRoadPO.getUpdateBy()+
						",TSR.UPDATE_DATE=sysdate"+
						" where  AREA_ID="+ttSalesRoadBean.getAreaId()+
						"AND ROAD_NAME='"+ttSalesRoadBean.getRoadName()+"'" +
						"AND AREA_ID IN(SELECT AREA_ID FROM TT_SALES_AREA WHERE YIELDLY="+ttSalesRoadBean.getYieldly()+")";
		dao.update(sql, null);
    }
	/**
	 * 
	 * @Title      : 
	 * @Description: 入库状态改变
	 * @param      : @param seachttSalesRoadPO 查询条件PO
	 * @param      : @param ttSalesRoadPO  修改信息PO    
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-2
	 */
	public void getRoadInStatus(TtSalesRoadPO ttSalesRoadPO,String seachwhere) {
		String sql="update TT_SALES_ROAD  TSR " +
		"set TSR.IN_STATUS="+ttSalesRoadPO.getInStatus() +
				",TSR.UPDATE_BY="+ttSalesRoadPO.getUpdateBy()+
				",TSR.UPDATE_DATE=sysdate"+
				" where ROAD_ID IN("+seachwhere+")";
		if(seachwhere!=null && !"".equals(seachwhere)){
			dao.update(sql, null);
		}
    }
	
	/**
	 * 
	 * @Title      : 
	 * @Description: 出库状态改变
	 * @param      : @param seachttSalesRoadPO 查询条件PO
	 * @param      : @param ttSalesRoadPO  修改信息PO    
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-2
	 */
	public void getRoadOutStatus(TtSalesRoadPO ttSalesRoadPO,String seachwhere) {
		//dao.update(seachttSalesRoadPO, ttSalesRoadPO);
		String sql="update TT_SALES_ROAD  TSR " +
		"set TSR.OUT_STATUS="+ttSalesRoadPO.getOutStatus() +
				",TSR.UPDATE_BY="+ttSalesRoadPO.getUpdateBy()+
				",TSR.UPDATE_DATE=sysdate"+
				" where ROAD_ID IN("+seachwhere+")";
		if(seachwhere!=null && !"".equals(seachwhere)){
			dao.update(sql, null);
		}
    }
	/**
	 * 
	 * @Title      : 
	 * @Description: 库道信息修改（普通）
	 * @param      : @param ttSalesAreaPO      
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-3
	 */
	public void reservoirSalesRoadAdd(TtSalesRoadPO seach,TtSalesRoadPO ttSalesRoadPO) {
		dao.update(seach, ttSalesRoadPO);
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
		sbSql.append("SELECT A.ROAD_NAME\n");
		sbSql.append("  FROM TT_SALES_ROAD A, TT_SALES_SIT B\n");
		sbSql.append(" WHERE A.ROAD_ID = B.ROAD_ID\n");
		sbSql.append("   AND A.AREA_ID = ?\n");
		sbSql.append("   AND A.STATUS = ?\n");
		sbSql.append("   AND B.STATUS = ?\n");
		sbSql.append("   AND TO_NUMBER(A.ROAD_NAME) BETWEEN ? AND ?\n");
		sbSql.append("   AND B.VEHICLE_ID != ?\n");
		sbSql.append(" GROUP BY A.ROAD_NAME\n");
		sbSql.append(" ORDER BY TO_NUMBER(A.ROAD_NAME)"); 
		List<Map<String,Object>> list=dao.pageQuery(sbSql.toString(), params, getFunName());
		return list;
	}
	
}
