package com.infodms.dms.dao.sales.storage.storagebase;

import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TtSalesLogiAreaPO;
import com.infodms.dms.po.TtSalesLogiPO;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

/**
 * 
 * @ClassName     : LogisticsDao 
 * @Description   : 物流商管理DAO 
 * @author        : ranjian
 * CreateDate     : 2013-4-7
 */
public class LogisticsDao extends BaseDao<PO>{
	private static final LogisticsDao dao = new LogisticsDao ();
	public static final LogisticsDao getInstance() {
		return dao;
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
	/**
	 * 物流商信息查询
	 * @param map
	 * @param curPage
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	public PageResult<Map<String, Object>> getLogisticsQuery(Map<String, Object> map, int curPage, int pageSize)throws Exception{
		
		String logiCode = (String)map.get("LOGI_CODE"); // 物流商代码
		String logiFullName = (String)map.get("LOGI_FULL_NAME");//物流商名称
		String yieldly = (String)map.get("YIELDLY");	// 产地
		String status = (String)map.get("STATUS");// 状态
		String conPer = (String)map.get("CON_PER");// 联系人
		String poseId = (String)map.get("poseId");
		String conTel = (String)map.get("CON_TEL");//联系电话
		List<Object> params = new LinkedList<Object>();
		
		StringBuffer sql= new StringBuffer();

		sql.append("select TSL.LOGI_ID,\n");
		sql.append("       TBA.AREA_NAME YIELDLY,\n");
		sql.append("       TSL.LOGI_CODE,\n");
		sql.append("       TSL.LOGI_NAME,\n");
		sql.append("       TSL.LOGI_FULL_NAME,\n");
		sql.append("       TSL.CON_PER,\n");
		sql.append("       TSL.CON_TEL,\n");
		sql.append("       TSL.STATUS,\n");
		sql.append("       TSL.ADDRESS,\n");
		sql.append("       tp.POSE_ID\n");
		sql.append("  from TT_SALES_LOGI TSL\n");
		sql.append("  inner join TM_BUSINESS_AREA TBA on TSL.YIELDLY = TBA.AREA_ID\n");
		sql.append("  inner join tc_pose tp on tp.logi_id=tsl.logi_id\n");
		sql.append("       and tp.pose_type="+Constant.SYS_USER_SGM+"\n");
		sql.append("       and tp.pose_status="+Constant.STATUS_ENABLE+"\n");
		sql.append("       and tp.pose_bus_type="+Constant.POSE_BUS_TYPE_WL+"\n");
		sql.append(" WHERE 1=1");
		sql.append(MaterialGroupManagerDao.getPoseIdBusinessSql("TSL.YIELDLY",poseId));//车厂端查询列表产地数据权限
		if(logiCode!=null&&!"".equals(logiCode)){
			sql.append("   AND TSL.LOGI_CODE  LIKE ? \n" );
			params.add("%"+logiCode+"%");
		}
		if(logiFullName!=null&&!"".equals(logiFullName)){
			sql.append("   AND TSL.LOGI_FULL_NAME  LIKE ? \n" );
			params.add("%"+logiFullName+"%");
		}
		if(yieldly!=null&&!"".equals(yieldly)){
			sql.append("   AND TSL.YIELDLY = "+yieldly+"\n" );
		}
		if(status!=null&&!"".equals(status)){
			sql.append("   AND TSL.STATUS = "+status+"\n" );
		}
		if(conPer!=null&&!"".equals(conPer)){
			sql.append("   AND TSL.CON_PER LIKE ? \n" );
			params.add("%"+conPer+"%");
		}
		if(conTel!=null&&!"".equals(conTel)){
			sql.append("   AND TSL.CON_TEL = "+conTel+"\n" );
		}
		sql.append("  order by tsl.create_date");
		PageResult<Map<String, Object>> ps= dao.pageQuery(sql.toString(), params, getFunName(),pageSize,curPage);
		return ps;
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 物流商信息添加 
	 * @param      : @param ttSalesLogiPO      
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-7
	 */
	public void logisticsAdd(TtSalesLogiPO ttSalesLogiPO) {
		dao.insert(ttSalesLogiPO);
    }
	/**
	 * 
	 * @Title      : 
	 * @Description: 物流商与区域信息添加 
	 * @param      : @param ttSalesLogiPO      
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-7
	 */
	public void logisticsAreaAdd(TtSalesLogiAreaPO ttSalesLogiAreaPO) {
		dao.insert(ttSalesLogiAreaPO);
    }
	/**
	 * 
	 * @Title      : 
	 * @Description: 物流商与区域信息删除
	 * @param      : @param ttSalesLogiPO      
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-7
	 */
	public void logisticsAreaDelete(TtSalesLogiAreaPO ttSalesLogiAreaPO) {
		dao.delete(ttSalesLogiAreaPO);
    }
	/**
	 * 
	 * @Title      : 
	 * @Description: 物流商信息信息修改 
	 * @param      : @param seachPO
	 * @param      : @param ttSalesLogiPO      
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-7
	 */
	public void logisticsUpdate(TtSalesLogiPO seachPO,TtSalesLogiPO ttSalesLogiPO) {
		dao.update(seachPO, ttSalesLogiPO);
    }
	
	/**
	 * 里程信息列表显示
	 * @param map
	 * @param curPage
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	public PageResult<Map<String, Object>> getCityMileageQuery(Map<String, Object> map, int curPage, int pageSize)throws Exception{
		
		String yieldly = (String)map.get("YIELDLY");	// 产地
		String provinceId = (String)map.get("PROVINCE");// 省份
		String disIds = (String)map.get("disIds");// 里程IDS
		String disIds_o = (String)map.get("disIds_o");// 省份显示页面传回
		String isShow = (String)map.get("isShow");// 显示页面返回
		
		String provinceSeach = (String)map.get("provinceSeach");	// 省
		String citySeach = (String)map.get("citySeach");// 市
		String countySeach = (String)map.get("countySeach");	//地区
		
		List<Object> params = new LinkedList<Object>();
		StringBuffer sbSql= new StringBuffer();
		sbSql.append("SELECT TSCD.DIS_ID,\r\n");
		sbSql.append("       TSCD.DISTANCE,\r\n");
		sbSql.append("       TR2.REGION_NAME PROVINCE_NAME,\r\n");
		sbSql.append("       TR3.REGION_NAME CITY_NAME,\r\n");
		sbSql.append("       TR3.REGION_ID   CITY_ID,\r\n");
		sbSql.append("       TR4.REGION_NAME P_CITY_NAME\r\n");
		sbSql.append("  FROM TT_SALES_CITY_DIS TSCD, TM_REGION TR2, TM_REGION TR3,TM_REGION TR4\r\n");
		sbSql.append(" WHERE TSCD.PROVINCE_ID = TR2.REGION_ID\r\n");
		sbSql.append("   AND TSCD.CITY_ID = TR3.REGION_ID\r\n"); 
		sbSql.append("   AND TR3.PARENT_ID=TR4.REGION_ID\r\n");
		if(provinceSeach!=null&&!"".equals(provinceSeach)){
			sbSql.append("   AND EXISTS(SELECT REGION_ID FROM TM_REGION T WHERE T.REGION_CODE=? AND T.REGION_ID=TSCD.PROVINCE_ID)\n" );
			params.add(provinceSeach);
		}
		if(citySeach!=null&&!"".equals(citySeach)){
			sbSql.append("AND EXISTS(SELECT T1.REGION_ID\n");
			sbSql.append("       FROM TM_REGION T, TM_REGION T1\n");
			sbSql.append("      WHERE T.REGION_ID = T1.PARENT_ID\n");
			sbSql.append("        AND T.REGION_CODE = ?\n");
			sbSql.append("        AND T1.REGION_ID = TSCD.CITY_ID\n)"); 
			params.add(citySeach);
		}
		if(countySeach!=null&&!"".equals(countySeach)){
			sbSql.append("   AND EXISTS(SELECT REGION_ID FROM TM_REGION T WHERE T.REGION_NAME=? AND T.REGION_ID=TSCD.CITY_ID)\n" );		
			params.add(countySeach);
		}
		if(yieldly!=null&&!"".equals(yieldly)){
			sbSql.append("   AND TSCD.YIELDLY = ?\n" );
			params.add(yieldly);
		}
		if(provinceId!=null&&!"".equals(provinceId)){
			sbSql.append(" AND TR2.REGION_CODE =?\n");
			params.add(provinceId);
	    }
		if(!isShow.equals("true")){
			if (null != disIds && !"".equals(disIds)) {				
				sbSql.append(Utility.getConSqlByParamForEqual(disIds, params,"TSCD", "DIS_ID"));
			}else{
				sbSql.append("   AND (TSCD.DIS_ID IN(-1))");	
			}
		}else{
			sbSql.append("   AND TSCD.DIS_ID NOT IN (SELECT DIS_ID\r\n");
			sbSql.append("          FROM TT_SALES_LOGI_AREA TSLA\r\n");
			sbSql.append("         WHERE 1=1");			
		/*	if (null != disIds_o && !"".equals(disIds_o)) {
				sbSql.append(Utility.getConSqlByParamForEqual(disIds_o, params,"TSLA", "DIS_ID"));
			}*/
			sbSql.append(" )");		
			
		}
		
		PageResult<Map<String, Object>> ps= dao.pageQuery(sbSql.toString(), params, getFunName(),pageSize,curPage);		
		return ps;
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 根据物流商ID得到物流商信息
	 * @param      : @param id
	 * @param      : @return     物流商信息 
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-9
	 */
	public Map<String, Object> getSalesLogiMsg(String id){
		StringBuffer sql= new StringBuffer();
		sql.append("select TSL.LOGI_ID,TBA.AREA_NAME YIELDLY,TBA.AREA_ID,TSL.LOGI_CODE,TSL.LOGI_NAME,\n");
		sql.append("TSL.LOGI_FULL_NAME,TSL.CON_PER,TSL.CON_TEL,TSL.STATUS,TSL.CORPORATION,TSL.REMARK,\n");
		sql.append("TSL.ADDRESS from TT_SALES_LOGI  TSL,TM_BUSINESS_AREA TBA WHERE TSL.YIELDLY=TBA.AREA_ID AND TSL.LOGI_ID=\n");
		sql.append(id);
		Map<String, Object> map = pageQueryMap(sql.toString(),null,getFunName());
		return map;
	}
	/**
	 * 获取车厂组织
	 * @param companyId
	 * @return
	 */
	public  List<Map<String, Object>> getOemOrg(String companyId)
	{
		List<Map<String, Object>> list = null;
		StringBuffer sql= new StringBuffer();
		sql.append("select *\n");
		sql.append("  from tm_org t\n");
		sql.append(" where t.org_type = "+Constant.ORG_TYPE_OEM+"\n");
		sql.append("   and t.company_id ="+companyId+"\n");
		sql.append("   and t.status = "+Constant.STATUS_ENABLE+"\n");
		sql.append("   and t.duty_type = "+Constant.DUTY_TYPE_COMPANY+"");
		list=dao.pageQuery(sql.toString(), null, dao.getFunName());
		return list;
	}
	
	public  List<Map<String, Object>> getProvinceList(String logiId)
	{
		List<Map<String, Object>> list = null;
		StringBuffer sql= new StringBuffer();
		sql.append("select *\n");
		sql.append("  from tm_org t\n");
		sql.append(" where t.duty_type = "+Constant.DUTY_TYPE_SMALLREGION+"\n");
		//sql.append("   and t.status = "+Constant.STATUS_ENABLE+"\n");
		if (null != logiId && !"".equals(logiId)) {
			sql.append("   and (t.logi_id is null or t.logi_id="+logiId+" or \n");
			sql.append("      t.logi_id in (select tsl.logi_id\n" );
			sql.append("                       from tt_sales_logi tsl\n" );
			sql.append("                      where tsl.status = "+Constant.STATUS_DISABLE+"))\n");
		}else{
			sql.append("and (t.logi_id is null or\n" );
			sql.append("      t.logi_id in (select tsl.logi_id\n" );
			sql.append("                       from tt_sales_logi tsl\n" );
			sql.append("                      where tsl.status = "+Constant.STATUS_DISABLE+"))\n");
		}
		
		list=dao.pageQuery(sql.toString(), null, dao.getFunName());
		return list;
	}
	/**
	 * 获取省份列表
	 * @return
	 */
	public  List<Map<String, Object>> getTmRegionLevel2()
	{
		List<Map<String, Object>> list = null;
		StringBuffer sql= new StringBuffer();
		sql.append("select t.*\n");
		sql.append("  from tm_region t\n");
		sql.append(" where t.region_type = "+Constant.REGION_TYPE_02+"\n");
		sql.append("   and t.status = "+Constant.STATUS_ENABLE+"\n");
		sql.append(" order by t.start_letter");

		list=dao.pageQuery(sql.toString(), null, dao.getFunName());
		return list;
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 根据物流商ID得到物流商与区域关联信息
	 * @param      : @param id
	 * @param      : @return     物流商与区域关联信息 
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-9
	 */
	public  List<TtSalesLogiAreaPO>  getLogisticsMata(TtSalesLogiAreaPO po){
		return dao.factory.select(po);
	}
	/**
	 * Function         : 查询车厂职位对应物流商
	 * @param           : 职位ID
	 */
	public  List<Map<String, Object>> getLogiByArea(String areaId)
	{
		List<Map<String, Object>> list = null;
		StringBuffer sql= new StringBuffer();	
		sql.append("SELECT TSL.LOGI_CODE,TSL.LOGI_NAME,TO_CHAR(TSL.LOGI_ID) LOGI_ID  FROM TT_SALES_LOGI TSL\n" );
		sql.append(" WHERE TSL.STATUS = "+Constant.STATUS_ENABLE+"\n" );
		sql.append("	AND TSL.LOGI_ID IN (SELECT TP.LOGI_ID\n" );
		sql.append("                         FROM TC_POSE TP\n" );
		sql.append("                        WHERE TP.POSE_STATUS = "+Constant.STATUS_ENABLE+"\n" );
		sql.append("                          AND TP.POSE_BUS_TYPE = "+Constant.POSE_BUS_TYPE_WL+")\n");

		sql.append("   AND TSL.YIELDLY in(").append(areaId==""?Constant.DEFAULT_VALUE:areaId).append(")");
		list=dao.pageQuery(sql.toString(), null, dao.getFunName());
		return list;
	}
	
	/**
	 * Function         : 查询车厂职位对应物流商
	 * @param           : 职位ID
	 */
	public  List<Map<String, Object>> getLogiByWarehouse(String poseId,String poseBusType)
	{
		List<Map<String, Object>> list = null;
		StringBuffer sql= new StringBuffer();
		if(poseBusType.equals(String.valueOf(Constant.POSE_BUS_TYPE_WL))){//物流商
			sql.append("SELECT tw.warehouse_id area_id, tw.warehouse_name area_name\n" );
			sql.append("  FROM TC_POSE TP, TT_SALES_LOGI TSL, TM_WAREHOUSE TW\n" );
			sql.append(" WHERE TP.LOGI_ID = TSL.LOGI_ID\n" );
			sql.append("   AND TSL.YIELDLY = TW.AREA_ID\n" );
			sql.append("   AND TP.POSE_STATUS = 10011001\n" );
			sql.append("   AND TP.POSE_BUS_TYPE = 10781007\n" );
			sql.append("   AND TW.WAREHOUSE_TYPE = 14011001\n" );
			sql.append("   AND TP.POSE_ID = '"+poseId+"'\n");
		}else{
			sql.append("SELECT t.warehouse_id area_id, t.warehouse_name area_name\n" );
			sql.append("  FROM tm_warehouse t\n" );
			sql.append(" WHERE t.status = "+Constant.STATUS_ENABLE+"\n" );
			sql.append("   AND t.warehouse_type = "+Constant.SALES_WAREHOUSE_TYPE_IN);
		}
		
		list=dao.pageQuery(sql.toString(), null, dao.getFunName());
		return list;
	}
	
	//查询是否属于物流商
	public  List<Map<String, Object>> getLogiByPoseId(String areaId,AclUserBean logonUser)
	{
		List<Object> params = new LinkedList<Object>();
		List<Map<String, Object>> list = null;
		StringBuffer sql= new StringBuffer();	
		sql.append("SELECT TSL.LOGI_CODE,TSL.LOGI_NAME,TO_CHAR(TSL.LOGI_ID) LOGI_ID  FROM TT_SALES_LOGI TSL\n" );
		sql.append(" WHERE TSL.STATUS = "+Constant.STATUS_ENABLE+"\n" );
		sql.append("   AND TSL.YIELDLY in(").append(areaId==""?Constant.DEFAULT_VALUE:areaId).append(")");
		sql.append(MaterialGroupManagerDao.getPoseIdLogiSqlByPar("TSL.LOGI_ID",logonUser.getPoseId().toString(),params));//物流判断（是否是物流公司）

		list=dao.pageQuery(sql.toString(), params, dao.getFunName());
		return list;
	}
	/**
	 * Function         : 根据里程ID查询里程信息
	 * @param           : 里程IDS
	 */
	public  List<Map<String, Object>> getDisByDisIDS(String disIds,String logiId)
	{
		List<Map<String, Object>> list = null;
		StringBuffer sbSql= new StringBuffer();	
		sbSql.append("select TSCD.DIS_ID,\r\n");
		sbSql.append("       TR.REGION_NAME PROVINCE_NAME,\r\n");
		sbSql.append("       TR1.REGION_NAME CITY_NAME\r\n");
		sbSql.append("from TT_SALES_LOGI_AREA TLA,\r\n");
		sbSql.append("     TT_SALES_CITY_DIS TSCD,\r\n");
		sbSql.append("     TM_REGION TR,TM_REGION TR1\r\n");
		sbSql.append("WHERE TLA.DIS_ID=TSCD.DIS_ID\r\n");
		sbSql.append("AND TSCD.PROVINCE_ID=TR.REGION_ID\r\n");
		sbSql.append("AND TSCD.CITY_ID=TR1.REGION_ID\r\n");
		sbSql.append("AND TLA.LOGI_ID!=").append(logiId).append("\r\n"); 
		if (null != disIds && !"".equals(disIds)) {
			String[] array = disIds.split(",");		
			int fu=array.length/500;
			sbSql.append("   AND (TLA.DIS_ID IN(-1)");
			for(int kk=0;kk<=fu;kk++){
				sbSql.append("  OR TLA.DIS_ID IN(");
					for(int s=kk*500;s<array.length;s++){
						if(s==(kk+1)*500){
							break;
						}
						sbSql.append(array[s]+",");
					}
				sbSql.append("-1)");
			}
			sbSql.append(")");
		}
		list=dao.pageQuery(sbSql.toString(), null, dao.getFunName());
		return list;
	}
	
	/**
	 *  在途位置维护取得物流商
	 * @return
	 */
	public  List<Map<String, Object>> getLogiName(){
		List<Map<String, Object>> list = null;
		StringBuffer sql= new StringBuffer();
		
		sql.append("SELECT TSL.LOGI_CODE,TSL.LOGI_NAME,LOGI_ID FROM TT_SALES_LOGI TSL\n" );
		sql.append(" WHERE TSL.STATUS = "+Constant.STATUS_ENABLE+"\n" );
		
		list=dao.pageQuery(sql.toString(), null, dao.getFunName());
		return list;
	}
	
	/**
	 * 查询区间数据
	 * @author liufazhong
	 */
	public List<Map<String, Object>> queryLogiInterval(){
		String sql = "SELECT TSLI.*,F_GET_TCUSER_NAME(TSLI.UPDATE_BY) UPDATE_BY_DESC FROM TT_SALES_LOGI_INTREVAL TSLI";
		return pageQuery(sql, null, getFunName());
	}
	
	/**
	 * 
	 * @param companyId
	 * @return
	 */
	public  List<Map<String, Object>> getPoseDealer(String smallOrgIds)
	{
		List<Map<String, Object>> list = null;
		StringBuffer sql= new StringBuffer();
		sql.append("select d.dealer_id\n" );
		sql.append("  from tm_dealer d, tm_dealer_org_relation dor, tm_org o\n" );
		sql.append(" where d.dealer_id = dor.dealer_id\n" );
		sql.append("   and dor.org_id = o.org_id\n" );
		sql.append("   and dor.business_type = "+Constant.ORG_TYPE_OEM+"\n" );
		sql.append("   and o.duty_type = "+Constant.DUTY_TYPE_SMALLREGION+"\n" );
		sql.append("   and o.org_id in ("+smallOrgIds+")");
		list=dao.pageQuery(sql.toString(), null, dao.getFunName());
		return list;
	}
}
