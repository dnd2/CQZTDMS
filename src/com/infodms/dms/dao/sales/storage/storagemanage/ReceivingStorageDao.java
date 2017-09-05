package com.infodms.dms.dao.sales.storage.storagemanage;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.common.Constant;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.common.BaseDao;
//import com.infodms.dms.po.TiExpBusVehStorePO;
import com.infodms.dms.po.TmVehiclePO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.businessUtil.GetVinUtil;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

/**
 * 
 * @ClassName     : ReceivingStorageDao 
 * @Description   : 接车入库DAO 
 * @author        : ranjian
 * CreateDate     : 2013-4-11
 */
public class ReceivingStorageDao extends BaseDao{
	public Logger logger = Logger.getLogger(ReceivingStorageDao.class);
	private static ReceivingStorageDao dao ;
	public static  ReceivingStorageDao getInstance() {
		if(null == dao){
			dao = new ReceivingStorageDao();
		}
		return dao;
	}
	//private ReceivingStorageDao(){}

	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
	/**
	 * 接车入库信息查询
	 * @param map
	 * @param curPage
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	public PageResult<Map<String, Object>> getReceivingStorageQuery(Map<String, Object> map, int curPage, int pageSize)throws Exception{
		
		String groupCode = (String)map.get("groupCode"); // 物料组
		String materialCode = (String)map.get("materialCode"); // 物料
		String areaName = (String)map.get("areaName"); // 库区
		String roadName = (String)map.get("roadName"); // 库道
		String sitName = (String)map.get("sitName"); // 库位
		String storageDays = (String)map.get("storageDays"); // 超过天数
		String offlineStartDate = (String)map.get("offlineStartDate"); // 下线日期开始
		String offlineEndDate = (String)map.get("offlineEndDate"); // 下线日期结束
		String orgStartDate = (String)map.get("orgStartDate"); // 入库日期开始
		String orgEndDate = (String)map.get("orgEndDate"); // 入库日期结束
		String vin = (String)map.get("vin"); // vin
		String areaIds = (String)map.get("areaIds"); // vin
		List<Object> params = new LinkedList<Object>();
		
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT\n");
		sql.append("TV.VEHICLE_ID,\n");//--车辆ID（操作时候用）
		//sql.append("--列表信息字段\n");
		sql.append("VMG.MODEL_NAME,\n");// --车型
		sql.append(" VMG.PACKAGE_NAME,\n");// --配置
		sql.append("TVM.MATERIAL_CODE,\n");// --物料CODE
		sql.append("TVM.MATERIAL_NAME,\n");// --物料名称
		sql.append("TV.VIN,\n");	//	--底盘号（VIN）
		sql.append("TV.ENGINE_NO,\n");//--发动机号
		sql.append("TO_CHAR(TV.PRODUCT_DATE, 'YYYY-MM-DD') PRODUCT_DATE,\n");// --生产日期
		sql.append("TO_CHAR(TV.OFFLINE_DATE, 'YYYY-MM-DD') OFFLINE_DATE,\n");// --下线日期
		sql.append("TSA.AREA_NAME, \n");//--库区名称
		sql.append("TSR.ROAD_NAME,\n");// --库道名称
		sql.append("TSS.SIT_NAME,\n");// --库位名称
		sql.append("TV.LIFE_CYCLE,\n");//--库存状态
		sql.append("TO_CHAR(TV.ORG_STORAGE_DATE, 'YYYY-MM-DD') ORG_STORAGE_DATE,\n");// --入库日期
		sql.append("CEIL(SYSDATE - TV.ORG_STORAGE_DATE) STORAGE_DAYS\n");// --库存天数
		//sql.append("--查询条件字段\n");//
		//sql.append("TVM.MATERIAL_ID,\n");// --物料ID
		//sql.append("TVM.MATERIAL_CODE,\n");// --物料CODE
		//sql.append("TVM.MATERIAL_NAME \n");//--物料NAME
		sql.append("FROM TM_VEHICLE               TV,\n");// --车辆信息表
		sql.append("TT_SALES_SIT             TSS,\n");// --库位表
		sql.append("VW_MATERIAL_GROUP        VMG, \n");//--物料试图
		sql.append("TT_SALES_ROAD            TSR,\n");// --库道表
		sql.append("TT_SALES_AREA            TSA, \n");//--库区表
		sql.append("TM_VHCL_MATERIAL         TVM,\n");// --物料表
		sql.append("TM_VHCL_MATERIAL_GROUP_R TVMGR, \n");//--物料物料组关联表
		sql.append("TM_BUSINESS_AREA TBA ");//--车厂职位业务表
		sql.append("WHERE TV.SIT_ID = TSS.SIT_ID\n");
		sql.append("AND TSS.ROAD_ID = TSR.ROAD_ID\n");
		sql.append("AND TSR.AREA_ID = TSA.AREA_ID\n");
		sql.append("AND TV.MATERIAL_ID = TVM.MATERIAL_ID\n");
		sql.append("AND TVM.MATERIAL_ID = TVMGR.MATERIAL_ID\n");
		sql.append("AND TVMGR.GROUP_ID = VMG.PACKAGE_ID\n");
		sql.append("AND TV.YIELDLY=TBA.AREA_ID\n");//权限控制
		
		if(groupCode!=null&& !"".equals(groupCode)){
			sql.append(MaterialGroupManagerDao.getMaterialGroupQuerySql("VMG.PACKAGE_ID", groupCode));
		}
		if(materialCode!=null && !"".equals(materialCode)){
			sql.append(MaterialGroupManagerDao.getMaterialQuerySql("TVM.MATERIAL_ID", materialCode));
		}
		if(areaName!=null&&!"".equals(areaName)){//库区过滤
			sql.append("   AND TSA.AREA_ID = "+areaName+"\n" );
		}
		if(roadName!=null&&!"".equals(roadName)){//库道过滤
			sql.append("   AND TSR.ROAD_ID = "+roadName+"\n" );
		}
		if(sitName!=null&&!"".equals(sitName)){//库位过滤
			sql.append("   AND TSS.SIT_ID ="+sitName+"\n" );
		}
		if(storageDays!=null&&!"".equals(storageDays)){//超过天数过滤
			sql.append("   AND CEIL(SYSDATE - TV.ORG_STORAGE_DATE) >"+storageDays+"\n" );
		}
		if(offlineStartDate!=null&&!"".equals(offlineStartDate)){//下线日期开始过滤
			sql.append("   AND TO_CHAR(TV.OFFLINE_DATE,'YYYY-MM-DD')>='"+offlineStartDate+"'\n" );
		}
		if(offlineEndDate!=null&&!"".equals(offlineEndDate)){//下线日期结束过滤
			sql.append("   AND TO_CHAR(TV.OFFLINE_DATE,'YYYY-MM-DD')<='"+offlineEndDate+"'\n" );
		}
		if(orgStartDate!=null&&!"".equals(orgStartDate)){// 入库日期开始过滤
			sql.append("   AND TO_CHAR(TV.ORG_STORAGE_DATE,'YYYY-MM-DD')>='"+orgStartDate+"'\n" );
		}
		if(orgEndDate!=null&&!"".equals(orgEndDate)){// 入库日期结束过滤
			sql.append("   AND TO_CHAR(TV.ORG_STORAGE_DATE,'YYYY-MM-DD')<='"+orgEndDate+"'\n" );
		}
		if(vin!=null&&!"".equals(vin)){//vin过滤
			GetVinUtil.getVins(vin, "TV");
			//sql.append("   AND TV.VIN LIKE '%"+vin+"%'\n" );
		}
		sql.append("AND TV.YIELDLY in(").append(areaIds==""?Constant.DEFAULT_VALUE:areaIds).append(")\n");//权限控制
		sql.append("ORDER BY TSA.AREA_ID,TO_NUMBER(TSR.ROAD_NAME),TO_NUMBER(TSS.SIT_NAME)");//排序
		PageResult<Map<String, Object>> ps= dao.pageQuery(sql.toString(), params, getFunName(),pageSize,curPage);
		return ps;
	}

	/**
	 * 查询出物料信息
	 * @return
	 */
	public List<Map<String,Object>> getGroupList(){
		StringBuffer sql=new StringBuffer();
		sql.append("select t.MATERIAL_CODE,t.material_name\n");
		sql.append(" from tm_vhcl_material t where t.STATUS = "+Constant.STATUS_ENABLE+"\n"); 
		return pageQuery(sql.toString(), null,dao.getFunName());

	}
	
	public Map<String, Object> getVehicleInfo(String materialCode) {
		StringBuffer sql = new StringBuffer();
		sql.append("   select  mat.MATERIAL_ID,mat.SERIES_ID, mat.MODEL_ID, mat.PACKAGE_ID  \n");
		sql.append("    from vw_material_group_mat  mat\n");
		sql.append("    where mat.MATERIAL_CODE = ? \n");
		List<Object> params= new ArrayList<Object>();
		params.add(materialCode);
		return pageQueryMap(sql.toString(), params, getFunName());
	}
	
	public Map<String, Object> getVehicleInfoByMaterialId(String materialId) {
		StringBuffer sql = new StringBuffer();
		sql.append("   select  mat.MATERIAL_ID,mat.SERIES_ID, mat.MODEL_ID, mat.PACKAGE_ID  \n");
		sql.append("    from vw_material_group_mat  mat\n");
		sql.append("    where mat.MATERIAL_ID = ? \n");
		List<Object> params= new ArrayList<Object>();
		params.add(materialId);
		return pageQueryMap(sql.toString(), params, getFunName());
	}
	
	public List<Map<String,Object>> getCarPartList(){
		StringBuffer sql=new StringBuffer();
		sql.append("select ta.PER_CODE,ta.PER_NAME from  TT_SALES_ACCAR_PER  ta\n");
		sql.append("        where ta.STATUS = "+Constant.PERSON_STATUS_01+"\n");  
		return pageQuery(sql.toString(), null,dao.getFunName());

	}
	
	/**
	 * 
	 * @Title      : 
	 * @Description: 根据物料CODE获取车系ID和对应的产地
	 * @param      : @param params 查询参数map
	 * @param      : @return     车系ID和产地列表 
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-11
	 */
	public Map<String, Object> getYieldlyAndSeries(List<Object> params){
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT TAG.AREA_ID AS YIELDLY,\n");//--产地ID
		sql.append("TBA.AREA_NAME YIELDLY_NAME,\n");//--产地名称
		sql.append("VMGMG.SERIES_ID,\n");//--车系ID
		sql.append("VMGMG.SERIES_CODE,\n");//--车系CODE
		sql.append("VMGMG.SERIES_NAME,\n");//--车系名称
		sql.append("VMGMG.MODEL_ID,\n");//--车型ID
		sql.append("VMGMG.MODEL_CODE,\n");//--车型CODE
		sql.append("VMGMG.MODEL_NAME,\n");//--车型名称
		sql.append("VMGMG.PACKAGE_ID,\n");//--配置ID
		sql.append("VMGMG.PACKAGE_CODE,\n");//--配置CODE
		sql.append("VMGMG.PACKAGE_NAME,\n");//--配置名称
		sql.append("VMGMG.MATERIAL_ID,\n");//--物料ID
		sql.append("VMGMG.MATERIAL_CODE,\n");//--物料CODE
		sql.append("VMGMG.MATERIAL_NAME,\n");//--物料名称
		sql.append(" TVM.COLOR_NAME,\n");//--颜色
		sql.append(" TVM.MODEL_YEAR\n");// --年型
		sql.append(" FROM VW_MATERIAL_GROUP_MAT VMGMG, TM_AREA_GROUP TAG,TM_BUSINESS_AREA TBA,TM_VHCL_MATERIAL TVM\n");
		sql.append(" WHERE VMGMG.SERIES_ID = TAG.MATERIAL_GROUP_ID\n");
		sql.append("AND TAG.AREA_ID=TBA.AREA_ID\n");
		sql.append("AND VMGMG.MATERIAL_ID=TVM.MATERIAL_ID\n");
		sql.append("AND VMGMG.MATERIAL_CODE=?\n");	
		Map<String, Object> map = pageQueryMap(sql.toString(),params,getFunName());
		return map;
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 根据物料车系ID和产地获取库区
	 * @param      : @param params 查询参数List
	 * @param      : @return     满足条件的库区
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-11
	 */
	public List<Map<String, Object>> getRegionByYieldlyAndSeries(List<Object> params){
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT TSA.AREA_ID,TSA.AREA_NAME,TSAM.MAT_ID,TSA.YIELDLY YID,TBA.AREA_NAME YNAME\n");
		sql.append("FROM TT_SALES_AREA TSA,\n");//库区表
		sql.append("TT_SALES_AREA_MAT TSAM,\n");//库区与车系关系表
		sql.append("TM_BUSINESS_AREA TBA\n");//产地表
		sql.append(" WHERE TSA.AREA_ID = TSAM.AREA_ID\n");//获取下能入库的车系关系
		sql.append(" AND TSA.YIELDLY = TBA.AREA_ID\n");//与产地表关联
		sql.append(" AND TSA.IN_STATUS = ?\n");//可以自动入库
		sql.append("AND TSA.YIELDLY = ?\n");//产地第一步取得
		sql.append("AND TSA.STATUS=?\n");//有效的
		sql.append("AND TSA.TYPE=?\n");//库区类型位正常库区的
		sql.append("AND TSAM.MAT_ID=?\n");//车系ID 第一步取得	
		sql.append("ORDER BY TSA.AREA_ID ASC");//根据ID排的序
		List<Map<String, Object>> list = pageQuery(sql.toString(),params,getFunName());
		return list;
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 根据库区ID获取库道信息
	 * @param      : @param params 查询参数List
	 * @param      : @return     满足条件的库道信息
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-12
	 */
	public List<Map<String, Object>> getRoadByAreaId(List<Object> params){
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT TSR.ROAD_ID,TSR.ROAD_NAME FROM TT_SALES_ROAD TSR WHERE TSR.STATUS=? \n");//有效的状态
		sql.append("AND TSR.IN_STATUS=?\n");//--正常，未锁定
		sql.append("AND TSR.AREA_ID=?\n");
		sql.append(" ORDER BY TO_NUMBER(TSR.ROAD_NAME) ASC\n");//根据库位name排升序
		List<Map<String, Object>> list = pageQuery(sql.toString(),params,getFunName());
		return list;
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 根据道ID获取库位信息
	 * @param      : params 查询参数List
	 * @param      : type [nullsit notnullsit]
	 * @param      : @return     满足条件的库位信息
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-12
	 */
	public List<Map<String, Object>> getSitByRoadId(List<Object> params,String type){
		StringBuffer sbSql= new StringBuffer();
		sbSql.append("SELECT TSS.SIT_ID,\n");
		sbSql.append("               TSS.SIT_NAME,\n");
		sbSql.append("               TSS.VEHICLE_ID,\n");
		sbSql.append("               VMGM.MATERIAL_CODE,\n");
		sbSql.append("               TPD.ORG_ID\n");
		sbSql.append("          FROM TT_SALES_SIT          TSS,\n");
		sbSql.append("               TM_VEHICLE            TV,\n");
		sbSql.append("               VW_MATERIAL_GROUP_MAT VMGM,\n");
		sbSql.append("               TM_PLAN_DETAIL        TPD\n");
		sbSql.append("         WHERE TSS.VEHICLE_ID = TV.VEHICLE_ID(+)\n");
		sbSql.append("           AND TV.MATERIAL_ID = VMGM.MATERIAL_ID(+)\n");
		sbSql.append("           AND TV.PLAN_DETAIL_ID = TPD.PLAN_DETAIL_ID(+)\n");
		sbSql.append("           AND TSS.STATUS = ?\n");
		sbSql.append("           AND TSS.ROAD_ID = ? --有效的状态\n");
		if(type.equals("notnullsit")){
			sbSql.append("           AND TSS.VEHICLE_ID != ?--不是空库位(无车)\n");
			
		}
		sbSql.append(" ORDER BY TO_NUMBER(TSS.SIT_NAME) ASC --根据库位name排升序"); 
		List<Map<String, Object>> list = pageQuery(sbSql.toString(),params,getFunName());
		return list;
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 根据道产地获取接车员信息
	 * @param      : @param params 查询参数List
	 * @param      : @return     满足条件接车员信息
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-12
	 */
	public List<Map<String, Object>> getPerValue(Map<String,Object> map)throws Exception{
		Integer status = Integer.parseInt(map.get("status").toString()); // 产地
		String yieldly = (String)map.get("yieldly"); // 产地
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT TSAP.PER_ID,TSAP.PER_NAME FROM TT_SALES_ACCAR_PER TSAP WHERE TSAP.STATUS=").append(status); 
		sql.append("AND TSAP.YIELDLY in(").append(yieldly==""?Constant.DEFAULT_VALUE:yieldly).append(")");
		sql.append(" ORDER BY TSAP.PER_NAME");
		List<Map<String, Object>> list= dao.pageQuery(sql.toString(), null, getFunName());
		return list;
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 添加车辆信息
	 * @param      : @param tmVehiclePO      
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-12
	 */
	public void vehicleAdd(TmVehiclePO tmVehiclePO) {
		dao.insert(tmVehiclePO);
    }
	/**
	 * 
	 * @Title      : 
	 * @Description: 根据VIN号获取VIN详细信息
	 * @param      : @param params   params    
	 * @return     : VIN详细信息   
	 * @throws     :
	 * LastDate    : 2013-4-12
	 */
	public Map<String,Object> getVinDetail(List<Object> params){	
		StringBuffer sbSql= new StringBuffer();
		sbSql.append("SELECT VMGM.SERIES_CODE,\n");
		sbSql.append("       VMGM.SERIES_NAME,\n");
		sbSql.append("       VMGM.MODEL_CODE,\n");
		sbSql.append("       VMGM.MODEL_NAME,\n");
		sbSql.append("       VMGM.PACKAGE_CODE,\n");
		sbSql.append("       VMGM.PACKAGE_NAME,\n");
		sbSql.append("       VMGM.MATERIAL_CODE,\n");
		sbSql.append("       VMGM.MATERIAL_NAME,\n");
		sbSql.append("       VMGM.COLOR_CODE,\n");
		sbSql.append("       VMGM.COLOR_NAME,\n");
		sbSql.append("       TM.VIN,\n");
		sbSql.append("       TM.ENGINE_NO,\n");
		sbSql.append("       TM.GEARBOX_NO,\n");
		sbSql.append("       TM.REARAXLE_NO\n");
		sbSql.append("  FROM TM_VEHICLE TM, VW_MATERIAL_GROUP_MAT VMGM\n");
		sbSql.append(" WHERE TM.MATERIAL_ID = VMGM.MATERIAL_ID\n");
		sbSql.append("   AND TM.VIN = ?"); 
		Map<String, Object> list= dao.pageQueryMap(sbSql.toString(), params, getFunName());
		return list;
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 根据车辆ID号获取车辆详细信息
	 * @param      : @param params   params    
	 * @return     : 车辆详细信息   
	 * @throws     :
	 * LastDate    : 2013-4-12
	 */
	public Map<String,Object> getVecihleDetail(List<Object> params){//此方法还未实现 ，
		
		return null;
	}
	/**
	 * 车辆信息查询
	 * @param params VIN号
	 * @return 车俩信息
	 * @throws Exception
	 */
	public Map<String, Object> getVehicleQuery(List<Object> params)throws Exception{
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT TV.VEHICLE_ID,TV.VIN,TV.SPECIAL_ORDER_NO,TV.DEALER_ID FROM TT_SALES_VEHICEL_IN TV WHERE TV.VIN =?");
		Map<String, Object> list= dao.pageQueryMap(sql.toString(), params, getFunName());
		return list;
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 修改车辆信息
	 * @param      : @param tmVehiclePO      
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-12
	 */
	public void vehicleUpdate(TmVehiclePO seachPO,TmVehiclePO tmVehiclePO) {
		dao.update(seachPO, tmVehiclePO);
    }
	
	/**
	 * 根据汇总订单号查询计划数量和已入库数量
	 * @param params 汇总订单号
	 * @return 计划数量和已入库数量
	 * @throws Exception
	 */
	public Map<String, Object> getCountByTotalOrderId(List<Object> params)throws Exception{
		StringBuffer sbSql= new StringBuffer();
		sbSql.append("SELECT TPD.PLAN_DETAIL_ID,\n");
		sbSql.append("       NVL(TPD.PLAN_NUM, 0) PLAN_NUM,\n");
		sbSql.append("       NVL(TPD.IN_NUM, 0) IN_NUM,\n");
		sbSql.append("       TPD.MAI_ID,\n");
		sbSql.append("       TPD.ORG_ID,\n");
		sbSql.append("       TPD.IS_FLEET,\n");//是否是集团客户
		sbSql.append("       TPD.ERP_NAME,\n");//ERP_NAME(集团客户定做车用)
		sbSql.append("       VMGM.ERP_MODEL,\n");//车型
		sbSql.append("       VMGM.ERP_PACKAGE,\n");//配置
		sbSql.append("       VMGM.MATERIAL_CODE,\n");
		sbSql.append("       VMGM.SERIES_ID\n");//车系
		sbSql.append("  FROM TM_PLAN_DETAIL TPD, VW_MATERIAL_GROUP_MAT VMGM,TM_PRO_PLAN A\n");
		sbSql.append(" WHERE \n");
		sbSql.append("   TPD.MAI_ID = VMGM.MATERIAL_ID\n");
		sbSql.append("   AND TPD.PLAN_ID = A.PLAN_ID\n");
		sbSql.append("   AND TPD.CHECK_STATUS = ?\n");
		sbSql.append("   AND TPD.TOTAL_ORDER_NO = ?"); 
		sbSql.append("   AND A.YIELDLY IN(?)"); 
		Map<String, Object> list= dao.pageQueryMap(sbSql.toString(), params, getFunName());
		return list;
	}
	/**
	 * 查询是否有订单号（九江） 无系统自动添加个
	 * @param params 车型，配置，装备状态
	 * @return 计划
	 * @throws Exception
	 */
	public Map<String,Object> getCountByTotalOrderIdJJ(Long matId,String orgId)throws Exception{
		StringBuffer sbSql= new StringBuffer();
		sbSql.append("SELECT S.TOTAL_ORDER_NO,A.MATERIAL_ID\n");
		sbSql.append("  FROM TM_PLAN_DETAIL S, TM_VHCL_MATERIAL A\n");
		sbSql.append(" WHERE S.MAI_ID = A.MATERIAL_ID\n");
		sbSql.append("   AND A.MATERIAL_ID = ?\n");
		sbSql.append("   AND S.ORG_ID = ? AND ROWNUM=1\n");
		List<Object> params=new ArrayList<Object>();
		params.add(matId);
		params.add(orgId);
		Map<String, Object> map= dao.pageQueryMap(sbSql.toString(), params, getFunName());
		return map;
	}
	/**
	 * 获取站点
	 * @param params 汇总订单号
	 * @return 计划数量和已入库数量
	 * @throws Exception
	 */
	public Map<String, Object> getOrgId(String orgId)throws Exception{
		StringBuffer sbSql= new StringBuffer();
		sbSql.append("SELECT A.NEW_ORG_ID  FROM TT_SALES_REGION_REL A WHERE A.OLD_SYS_CODE = ? AND ROWNUM=1"); 
		List<Object> params=new ArrayList<Object>();
		params.add(orgId);
		Map<String, Object> map= dao.pageQueryMap(sbSql.toString(), params, getFunName());
		return map;
	} 
	/**
	 * 获取erpname
	 * @param dzcode modelGroup ccode
	 * @return erpname
	 * @throws Exception
	 */
	public Map<String, Object> getErpName(String dzcode,String modelGroup,String ccode,String coCode)throws Exception{
		StringBuffer sbSql= new StringBuffer();
		sbSql.append("SELECT A.ERP_CODE OLD_ERP_CODE,substr(A.ERP_CODE, 0, 3) || ? ||\n");
		sbSql.append("       substr(A.ERP_CODE, 7, length(A.ERP_CODE)) ERP_CODE\n");
		sbSql.append("  FROM TT_SALES_HFERP_MAP A"); 
		sbSql.append(" WHERE A.DZ_CODE = ?\n");
		sbSql.append("   AND A.CHOOSE_CODE = ?");
		sbSql.append("   AND A.MODEL_GROUP = ? AND ROWNUM=1\n");
		List<Object> params=new ArrayList<Object>();
		params.add(coCode);
		params.add(dzcode);
		params.add(ccode);
		params.add(modelGroup);
		Map<String, Object> map= dao.pageQueryMap(sbSql.toString(), params, getFunName());
		return map;
	} 
	/**
	 * get newColorCode
	 * @param coCode
	 * @return newColorCode
	 * @throws Exception
	 */
	public Map<String, Object> getColorName(String coCode)throws Exception{
		StringBuffer sbSql= new StringBuffer();
		sbSql.append("select A.CHOOSE_COLOR from TT_SALES_COLOR_MAP A WHERE A.COLOR_CODE = ? AND ROWNUM=1"); 
		List<Object> params=new ArrayList<Object>();
		params.add(coCode);
		Map<String, Object> map= dao.pageQueryMap(sbSql.toString(), params, getFunName());
		return map;
	} 
	 

	/**
	 * 
	 * @Title      : 
	 * @Description: 修改详细计划表已入库数量
	 * @param      : @param list 参数列表      
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-27
	 */
	public void inNumUpdate(List<Object> params) {
		StringBuffer sql=new StringBuffer();
		sql.append("UPDATE TM_PLAN_DETAIL  TPD SET TPD.IN_NUM=NVL(TPD.IN_NUM,0)+1 WHERE TPD.PLAN_DETAIL_ID=?\n");
		dao.update(sql.toString(), params);
    }
	/**
	 * 
	 * @Title      : 
	 * @Description: 根据道ID获取库位数量
	 * @param      : @param params 查询参数List
	 * @param      : @return     满足条件的库位信息
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-12
	 */
	public int getSitCountByRoadId(List<Object> params){
		int sitCount =0;
		StringBuffer sbSql= new StringBuffer();
		sbSql.append("SELECT COUNT(1) SIT_COUNT\n");
		sbSql.append("  FROM TT_SALES_SIT TSS\n");
		sbSql.append(" WHERE TSS.STATUS = ?\n");
		sbSql.append("   AND TSS.ROAD_ID = ?"); 
		List<Map<String, Object>> list = pageQuery(sbSql.toString(),params,getFunName());
		if(list!=null && list.size()>0){
			sitCount=Integer.parseInt(list.get(0).get("SIT_COUNT").toString());
		}
		return sitCount;
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 根据库位ID获取单条库位信息
	 * @param      : @param param map列表   
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-5
	 */

	public List<Map<String, Object>> getSitMsgByRoadId(List<Object> param)throws Exception{
		StringBuffer sbSql= new StringBuffer();
		sbSql.append("SELECT T.SIT_ID, T.SIT_NAME\n");
		sbSql.append("  FROM TT_SALES_SIT T\n");
		sbSql.append(" WHERE T.STATUS = ?\n");
		sbSql.append("   AND T.ROAD_ID = ?"); 
		List<Map<String, Object>> list= dao.pageQuery(sbSql.toString(), param, getFunName());
		return list;
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 获取同物料同省份的暂用的库道信息
	 * @param      : @param params 查询参数List
	 * @param      : @return     满足条件的库道
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-7-6
	 */
	public List<Map<String, Object>> getRoadByhaves(Map<String, Object> map){
		StringBuffer sbSql= new StringBuffer();
		sbSql.append("\n");
		sbSql.append("SELECT MAX_SITNAME, SUM_COUNT, ROAD_ID\n");
		sbSql.append("  FROM (SELECT C.ROAD_ID, MAX_SITNAME, SUM_COUNT, maTime\n");
		sbSql.append("          FROM tt_Sales_road X1,\n");
		sbSql.append("               tt_Sales_area X2,\n");
		sbSql.append("               (SELECT A.ROAD_ID,\n");
		sbSql.append("                       max(maxTime) maTime,\n");
		sbSql.append("                       DECODE(Length(MAX(NVL(TO_NUMBER(A.SIT_NAME), 0) + 1)),\n");
		sbSql.append("                              1,\n");
		sbSql.append("                              '0' || MAX(NVL(TO_NUMBER(A.SIT_NAME), 0) + 1),\n");
		sbSql.append("                              MAX(NVL(TO_NUMBER(A.SIT_NAME), 0)) + 1) MAX_SITNAME,\n");
		sbSql.append("                       (SELECT Decode(Length(max(NVL(TO_NUMBER(SIT_NAME), 0))),\n");
		sbSql.append("                                      1,\n");
		sbSql.append("                                      '0' || max(NVL(TO_NUMBER(SIT_NAME), 0)),\n");
		sbSql.append("                                      max(NVL(TO_NUMBER(SIT_NAME), 0))) SUM_COUNT\n");
		sbSql.append("                          FROM TT_SALES_SIT\n");
		sbSql.append("                         WHERE ROAD_ID = A.ROAD_ID\n");
		sbSql.append("                           AND STATUS = ?) SUM_COUNT\n");
		sbSql.append("                  from tt_sales_sit A,\n");
		sbSql.append("                       (SELECT B.ROAD_ID, max(D.Org_Storage_Date) maxTime\n");
		sbSql.append("                          FROM TT_SALES_AREA     A, --库区表\n");
		sbSql.append("                               TT_SALES_ROAD     B, --库道表\n");
		sbSql.append("                               TT_SALES_SIT      C, --库位表\n");
		sbSql.append("                               TM_VEHICLE        D, --车辆表\n");
		sbSql.append("                               TM_PLAN_DETAIL    E, --生产计划明细表\n");
		sbSql.append("                               TM_AREA_GROUP     F, --产地跟物料组关系表（车系）\n");
		sbSql.append("                               TT_SALES_AREA_MAT G --库区跟物料组关系表(车系)\n");
		sbSql.append("                         WHERE A.AREA_ID = B.AREA_ID\n");
		sbSql.append("                           AND B.ROAD_ID = C.ROAD_ID\n");
		sbSql.append("                           AND C.VEHICLE_ID = D.VEHICLE_ID\n");
		sbSql.append("                           AND D.PLAN_DETAIL_ID = E.PLAN_DETAIL_ID\n");
		sbSql.append("                           AND A.YIELDLY = F.AREA_ID\n");
		sbSql.append("                           AND A.AREA_ID = G.AREA_ID\n");
		sbSql.append("                           AND A.IN_STATUS = ? --可以自动入库\n");
		sbSql.append("                           AND A.YIELDLY = ? --产地\n");
		sbSql.append("                           AND A.STATUS = ? --库区有效的\n");
		sbSql.append("                           AND A.TYPE = ? --库区类型位正常库区的\n");
		sbSql.append("                           AND F.MATERIAL_GROUP_ID = ? --车系ID\n");
		sbSql.append("                           AND G.MAT_ID = ? --车系ID\n");
		sbSql.append("                           AND B.STATUS = ? --库道有效的\n");
		sbSql.append("                           AND B.IN_STATUS = ? --库道入库状态位正常的\n");
		sbSql.append("                           AND C.STATUS = ? --库位有效的\n");
		sbSql.append("                           AND E.ORG_ID = ? --同省份\n");
		sbSql.append("                           AND D.MATERIAL_ID = ? --同物料\n");
		if(map.get("IS_FLEET")!=null && map.get("IS_FLEET").toString().equals(Constant.IF_TYPE_YES.toString())){//集团客户定做车
			sbSql.append("                           AND D.PLAN_DETAIL_ID = ? --同生产计划的\n");
		}
		sbSql.append("                         GROUP BY B.ROAD_ID) B\n");
		sbSql.append("                 WHERE A.ROAD_ID = B.ROAD_ID\n");
		sbSql.append("                   AND A.VEHICLE_ID != ?\n");
		sbSql.append("                 GROUP BY A.ROAD_ID) C\n");
		sbSql.append("         WHERE C.ROAD_ID = X1.ROAD_ID\n");
		sbSql.append("           AND X1.AREA_ID = X2.AREA_ID\n");
		sbSql.append("         ORDER BY C.maTime desc)\n");
		sbSql.append(" WHERE ROWNUM = 1"); 
		/*sbSql.append("SELECT C.ROAD_ID, MAX_SITNAME, SUM_COUNT\n");
		sbSql.append("  FROM tt_Sales_road X1,\n");
		sbSql.append("       tt_Sales_area X2,\n");
		sbSql.append("       ("); 
		sbSql.append("\n");
		sbSql.append("SELECT A.ROAD_ID,\n");
		sbSql.append("       DECODE(Length(MAX(NVL(TO_NUMBER(A.SIT_NAME),0))+1),\n");
		sbSql.append("              1,\n");
		sbSql.append("              '0' || MAX(NVL(TO_NUMBER(A.SIT_NAME),0))+1,\n");
		sbSql.append("              MAX(NVL(TO_NUMBER(A.SIT_NAME),0))+1) MAX_SITNAME,\n");
		sbSql.append("       (SELECT Decode(Length(max(NVL(TO_NUMBER(SIT_NAME),0))),\n");
		sbSql.append("                      1,\n");
		sbSql.append("                      '0' || max(NVL(TO_NUMBER(SIT_NAME),0)),\n");
		sbSql.append("                      max(NVL(TO_NUMBER(SIT_NAME),0))) SUM_COUNT\n");
		sbSql.append("          FROM TT_SALES_SIT\n");
		sbSql.append("         WHERE ROAD_ID = A.ROAD_ID\n");
		sbSql.append("           AND STATUS = ?) SUM_COUNT\n");
		sbSql.append("  from tt_sales_sit A,\n");
		sbSql.append("       (SELECT B.ROAD_ID\n");
		sbSql.append("          FROM TT_SALES_AREA     A, --库区表\n");
		sbSql.append("               TT_SALES_ROAD     B, --库道表\n");
		sbSql.append("               TT_SALES_SIT      C, --库位表\n");
		sbSql.append("               TM_VEHICLE        D, --车辆表\n");
		sbSql.append("               TM_PLAN_DETAIL    E, --生产计划明细表\n");
		sbSql.append("               TM_AREA_GROUP     F, --产地跟物料组关系表（车系）\n");
		sbSql.append("               TT_SALES_AREA_MAT G --库区跟物料组关系表(车系)\n");
		sbSql.append("         WHERE A.AREA_ID = B.AREA_ID\n");
		sbSql.append("           AND B.ROAD_ID = C.ROAD_ID\n");
		sbSql.append("           AND C.VEHICLE_ID = D.VEHICLE_ID\n");
		sbSql.append("           AND D.PLAN_DETAIL_ID = E.PLAN_DETAIL_ID\n");
		sbSql.append("           AND A.YIELDLY = F.AREA_ID\n");
		sbSql.append("           AND A.AREA_ID = G.AREA_ID\n");
		sbSql.append("           AND A.IN_STATUS = ? --可以自动入库\n");
		sbSql.append("           AND A.YIELDLY = ? --产地\n");
		sbSql.append("           AND A.STATUS = ? --库区有效的\n");
		sbSql.append("           AND A.TYPE = ? --库区类型位正常库区的\n");
		sbSql.append("           AND F.MATERIAL_GROUP_ID = ? --车系ID\n");
		sbSql.append("           AND G.MAT_ID = ? --车系ID\n");
		sbSql.append("           AND B.STATUS = ? --库道有效的\n");
		sbSql.append("           AND B.IN_STATUS = ? --库道入库状态位正常的\n");
		sbSql.append("           AND C.STATUS = ? --库位有效的\n");
		sbSql.append("           AND E.ORG_ID = ? --同省份\n");
		sbSql.append("           AND D.MATERIAL_ID = ? --同物料\n");
		sbSql.append("         GROUP BY B.ROAD_ID) B\n");
		sbSql.append(" WHERE A.ROAD_ID = B.ROAD_ID\n");
		sbSql.append("   AND A.VEHICLE_ID != ?\n");
		sbSql.append(" GROUP BY A.ROAD_ID"); 
		sbSql.append(") C\n");
		sbSql.append(" WHERE C.ROAD_ID = X1.ROAD_ID\n");
		sbSql.append("   AND X1.AREA_ID = X2.AREA_ID\n");
		sbSql.append(" ORDER BY TO_NUMBER(X2.AREA_NAME), TO_NUMBER(X1.ROAD_NAME)"); */
		List<Object> params=new ArrayList<Object>();
		params.add(Constant.STATUS_ENABLE);//库位有效的
		params.add(Constant.IN_STATUS_01);//可以自动入库
		params.add(map.get("YIELDLY"));//产地
		params.add(Constant.STATUS_ENABLE);//库区有效的
		params.add(Constant.RES_TYPE_01);//库区类型位正常库区的
		params.add(map.get("SERIES_ID"));//车系ID
		params.add(map.get("SERIES_ID"));//车系ID
		params.add(Constant.STATUS_ENABLE);//库道有效的
		params.add(Constant.AUTO_IN_STATUS_01);//库道入库状态位正常的
		params.add(Constant.STATUS_ENABLE);//库位有效的
		params.add(map.get("ORG_ID"));//同省份(省份ID)
		params.add(map.get("MAI_ID"));//同物料
		if(map.get("IS_FLEET")!=null && map.get("IS_FLEET").toString().equals(Constant.IF_TYPE_YES.toString())){
			params.add(map.get("PLAN_DETAIL_ID"));//生产计划明细ID
		}
		params.add(Constant.DEFAULT_VALUE);//默认库位车辆ID
		List<Map<String, Object>> list = pageQuery(sbSql.toString(),params,getFunName());
		return list;
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 获取同物料同省份的暂用的库道信息
	 * @param      : @param params 查询参数List
	 * @param      : @return     满足条件的库道
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-7-6
	 */
	public Map<String, Object> getNullRoad(Map<String,Object> map){
		StringBuffer sbSql= new StringBuffer();
		sbSql.append("SELECT *\n");
		sbSql.append("  FROM (SELECT A.AREA_ID,\n");
		sbSql.append("               A.AREA_NAME,\n");
		sbSql.append("               B.ROAD_ID,\n");
		sbSql.append("               B.ROAD_NAME,\n");
		sbSql.append("               C.SIT_ID,\n");
		sbSql.append("               C.SIT_NAME\n");
		sbSql.append("          FROM TT_SALES_AREA     A, --库区表\n");
		sbSql.append("               TT_SALES_ROAD     B, --库道表\n");
		sbSql.append("               TT_SALES_SIT      C, --库位表\n");
		sbSql.append("               TM_AREA_GROUP     F, --产地跟物料组关系表（车系）\n");
		sbSql.append("               TT_SALES_AREA_MAT G --库区跟物料组关系表(车系)\n");
		sbSql.append("         WHERE A.AREA_ID = B.AREA_ID\n");
		sbSql.append("           AND B.ROAD_ID = C.ROAD_ID\n");
		sbSql.append("           AND A.YIELDLY = F.AREA_ID\n");
		sbSql.append("           AND A.AREA_ID = G.AREA_ID\n");
		sbSql.append("           AND A.IN_STATUS = ? --可以自动入库\n");
		sbSql.append("           AND A.YIELDLY = ? --产地\n");
		sbSql.append("           AND A.STATUS = ? --库区有效的\n");
		sbSql.append("           AND A.TYPE = ? --库区类型位正常库区的\n");
		sbSql.append("           AND F.MATERIAL_GROUP_ID = ? --车系ID\n");
		sbSql.append("           AND G.MAT_ID = ? --车系ID\n");
		sbSql.append("           AND B.STATUS = ? --库道有效的\n");
		sbSql.append("           AND B.IN_STATUS = ? --库道入库状态位正常的\n");
		sbSql.append("           AND C.STATUS = ? --库位有效的\n");
		sbSql.append("           AND TO_NUMBER(C.SIT_NAME) = 1\n");
		sbSql.append("           AND NOT EXISTS (SELECT 1\n");
		sbSql.append("                  FROM TT_SALES_SIT K\n");
		sbSql.append("                 WHERE K.ROAD_ID = B.ROAD_ID\n");
		sbSql.append("                   AND K.VEHICLE_ID != ?)\n");
		sbSql.append("         ORDER BY TO_NUMBER(A.AREA_NAME), TO_NUMBER(B.ROAD_NAME), TO_NUMBER(C.SIT_NAME))\n");
		sbSql.append(" WHERE ROWNUM = 1");  
		List<Object> params=new ArrayList<Object>();
		params.add(Constant.IN_STATUS_01);//可以自动入库
		params.add(map.get("YIELDLY"));//产地
		params.add(Constant.STATUS_ENABLE);//库区有效的
		params.add(Constant.RES_TYPE_01);//库区类型位正常库区的
		params.add(map.get("SERIES_ID"));//车系ID
		params.add(map.get("SERIES_ID"));//车系ID
		params.add(Constant.STATUS_ENABLE);//库道有效的
		params.add(Constant.AUTO_IN_STATUS_01);//库道入库状态位正常的
		params.add(Constant.STATUS_ENABLE);//库位有效的
		params.add(Constant.DEFAULT_VALUE);//默认库位车辆ID
		Map<String, Object> mapRE = pageQueryMap(sbSql.toString(),params,getFunName());
		return mapRE;
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 根据库位ID判断是否是最后一位
	 * @param      : 
	 * @param      : 
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-7-6
	 */
	public boolean getLastSit(Long roadId){
		boolean bo=false;
		StringBuffer sbSql= new StringBuffer();
		sbSql.append("SELECT COUNT(1) NUMS\n");
		sbSql.append("  FROM TT_SALES_SIT A\n");
		sbSql.append(" WHERE A.VEHICLE_ID = ?\n");
		sbSql.append("   AND A.STATUS = ?\n");
		sbSql.append("   AND A.ROAD_ID = ?"); 
		List<Object> params=new ArrayList<Object>();
		params.add(Constant.DEFAULT_VALUE);//默认库位车辆ID
		params.add(Constant.STATUS_ENABLE);//库位有效的
		params.add(roadId);//库道ID
		int s = Integer.parseInt(pageQueryMap(sbSql.toString(),params,getFunName()).get("NUMS").toString());
		if(s==1){
			bo=true;
		}
		return bo;
		
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 根据VIN获取退车历史信息
	 * @param      : @param param map列表   
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-5
	 */

	public List<Map<String, Object>> getHisVehicle(List<Object> param)throws Exception{
		StringBuffer sbSql= new StringBuffer();
		sbSql.append("SELECT A.RETREATDES\n");
		sbSql.append("  FROM TM_VEHICLE_RETREAT_HISTORY A\n");
		sbSql.append(" WHERE A.VIN = ?\n");
		sbSql.append("   AND A.ISRETREAT = ?\n");
		sbSql.append(" ORDER BY A.RETREAT_DATE DESC");  
		List<Map<String, Object>> list= dao.pageQuery(sbSql.toString(), param, getFunName());
		return list;
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 根据库道ID和库位名称查找库，道，位
	 * @param      : 
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-8-25
	 */

	public List<Map<String, Object>> getARS(List<Object> param)throws Exception{
		StringBuffer sbSql= new StringBuffer();
		sbSql.append("SELECT A.AREA_ID, A.AREA_NAME, B.ROAD_ID, B.ROAD_NAME, C.SIT_ID, C.SIT_NAME\n");
		sbSql.append("  FROM TT_SALES_AREA A, TT_SALES_ROAD B, TT_SALES_SIT C\n");
		sbSql.append(" WHERE A.AREA_ID = B.AREA_ID\n");
		sbSql.append("   AND B.ROAD_ID = C.ROAD_ID\n");
		sbSql.append("   AND B.ROAD_ID = ?\n");
		sbSql.append("   AND C.SIT_NAME = ?\n");
		sbSql.append("   AND A.STATUS = ?\n");
		sbSql.append("   AND B.STATUS = ?\n");
		sbSql.append("   AND C.STATUS = ?"); 
		List<Map<String, Object>> list= dao.pageQuery(sbSql.toString(), param, getFunName());
		return list;
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 判断是否是昌铃车
	 * @param      : 
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-8-25
	 */

	public List<Map<String, Object>> getCLQuery(List<Object> param)throws Exception{
		StringBuffer sbSql= new StringBuffer();
		sbSql.append("Select a.BRAND_ID, a.EXPORT_SALES_FLAG\n");
		sbSql.append("  from VW_MATERIAL_GROUP_MAT A\n");
		sbSql.append(" where a.MATERIAL_ID = ? group by a.BRAND_ID,a.EXPORT_SALES_FLAG"); 
		List<Map<String, Object>> list= dao.pageQuery(sbSql.toString(), param, getFunName());
		return list;
	}
	
	
	public List<Map<String, Object>> getEableSit(String vin,boolean isSpecialFlag,String speNo,String opFlag,String materialId) {
		StringBuffer sql = new StringBuffer();
		 List<Object> params = new ArrayList<Object>();
        params.add(Constant.STATUS_ENABLE);
        params.add(Constant.AUTO_IN_STATUS_01);
        params.add(Constant.STATUS_ENABLE);
        params.add(Constant.STATUS_ENABLE);
		sql.append(" with x_t as\n");
		sql.append(" (select t.road_id, MIN(to_number(t.sit_name)) sit_name\n");
		sql.append("    from tt_sales_sit t\n");
		sql.append("   where t.vehicle_id != -1\n");
		sql.append("   group by t.road_id)\n");
		sql.append("   select distinct area.area_id,road.road_id,sit.sit_id, \n");
		sql.append("          area.area_name ,road.road_name,sit.sit_name \n");
		sql.append(" 	from  tt_sales_area area, tt_sales_road road, tt_sales_sit sit\n");
		sql.append("   where  road.area_id = area.area_id\n");
		sql.append("     and sit.road_id = road.road_id\n");
		sql.append("     and area.type = "+Constant.RES_TYPE_01+"\n");
		sql.append("	 and area.status = ? \n");
		sql.append("     and road.in_status = ?\n");
		sql.append("     and road.status = ?\n");
		sql.append("	 and sit.status = ?\n");
		sql.append(" 	 and ( sit.vehicle_id = -1 or sit.vehicle_id  is null)\n");
		sql.append("      and sit.road_id in (\n");
		// 首先查同物料的库道,如果为特殊订单，则同一个订单的停在同一个道
		sql.append("                   select s.road_id\n");
		sql.append("                     from tt_sales_sit s, x_t c, tm_vehicle d\n");
		sql.append("                    where s.road_id = c.road_id\n");
		sql.append("                      and c.sit_name = to_number(s.sit_name)\n");
		sql.append("                      and s.vehicle_id = d.vehicle_id\n");
		 if(isSpecialFlag){//如果为特殊订单，则以特殊订单号为条件
				sql.append("	          and d.SPECIAL_ORDER_NO = ?)\n");
				params.add(speNo);
		}
	   if("scan".equals(opFlag)&& !isSpecialFlag){
		   sql.append("                   and d.material_id =\n");
			sql.append("                          (select f.material_id\n");
			sql.append("                             from tt_sales_vehicel_in f\n");
			sql.append("                            where f.vin = ?))\n");
		    params.add(vin);
		}else if(!isSpecialFlag){//如果不是特殊订单，那么按照同种物料 
			sql.append("                   and d.material_id = ?)\n");
			params.add(materialId);		
		}
		sql.append(" order by  area.area_name ,road.road_name,sit.sit_name \n");
		return pageQuery(sql.toString(), params, getFunName());
	}
	
	public List<Map<String, Object>> getEableSitSy(String vin,Long dealerId) {
		StringBuffer sql = new StringBuffer();
		List<Object> params = new ArrayList<Object>();
		sql.append(" with x_t as\n");
		sql.append(" (select t.road_id, MIN(to_number(t.sit_name)) sit_name\n");
		sql.append("    from tt_sales_sit t\n");
		sql.append("   where t.vehicle_id != -1\n");
		sql.append("   group by t.road_id)\n");
		sql.append("   select distinct area.area_id,road.road_id,sit.sit_id, \n");
		sql.append("          area.area_name ,road.road_name,sit.sit_name \n");
		sql.append(" 	from  tt_sales_area area, tt_sales_road road, tt_sales_sit sit\n");
		sql.append("   where  road.area_id = area.area_id\n");
		sql.append("     and sit.road_id = road.road_id\n");
		sql.append("     and area.type = "+Constant.RES_TYPE_05+"\n");
		sql.append(" 	 and ( sit.vehicle_id = -1 or sit.vehicle_id  is null)\n");
		sql.append("      and sit.road_id in (\n");
		// 首先查同物料的库道
		sql.append("                   select s.road_id\n");
		sql.append("                     from tt_sales_sit s, x_t c, tm_vehicle d\n");
		sql.append("                    where s.road_id = c.road_id\n");
		sql.append("                      and c.sit_name = to_number(s.sit_name)\n");
		sql.append("                      and s.vehicle_id = d.vehicle_id\n");
		sql.append("                   and d.material_id =\n");
		sql.append("                          (select f.material_id\n");
		sql.append("                             from tt_sales_vehicel_in f\n");
		sql.append("                            where f.vin = ?))\n");
		params.add(vin);
		sql.append(" order by  area.area_name ,road.road_name,sit.sit_name \n");
		return pageQuery(sql.toString(), params, getFunName());
	}
	
	//判断车辆是否已经在库存中了
	public List<Map<String, Object>> getInSitVeh(String vin) {
		StringBuffer sql = new StringBuffer();
		sql.append(" 	SELECT 1 \n ");
		sql.append("    	from tt_sales_vehicel_in vi,tm_vehicle tv \n ");
		sql.append("        where tv.vin = vi.vin  and vi.vin = ?  and tv.sit_code is not null\n");
//		sql.append("        and vi.is_storage = " + Constant.VEHICLE_IN_STATUS_01 );
		List<Object> params = new ArrayList<Object>();
		params.add(vin);
		return pageQuery(sql.toString(), params, getFunName());
	}
	public List<Map<String, Object>> getNullRoadList() {
		StringBuffer sql = new StringBuffer();
		sql.append(" with aa as(\n");
		sql.append("         select a.road_id, count(1) all_sit from tt_sales_road a,tt_Sales_sit  b where a.road_id=b.road_id  and a.status = 10011001 \n");
		sql.append("         group by a.road_id),\n");
		sql.append(" bb as(\n");
		sql.append(" select a.road_id,count(1) null_sit from tt_sales_road a,tt_Sales_sit  b where a.road_id=b.road_id and b.vehicle_id=-1   and a.status = 10011001 \n");
		sql.append(" group by a.road_id)\n");
		sql.append(" 		select  area.area_id,e.road_id,sit.sit_id,area.area_name,e.road_name,sit.sit_name\n");
		sql.append("          from   tt_sales_area         area,\n");
		sql.append(" 				 tt_sales_sit          sit,\n");
		sql.append(" 				 (select a.road_id,a.area_id,a.road_name,b.all_sit,c.null_sit from tt_sales_road a, aa b,bb c where a.road_id=b.road_id and a.road_id=c.road_id and b.all_sit=c.null_sit and a.in_status=?) e\n");
		sql.append(" 		 where e.area_id = area.area_id	 \n");
		sql.append(" 		   and e.road_id = sit.road_id\n");
		sql.append(" 		   and area.status = ?\n");
		sql.append("           and area.type = "+Constant.RES_TYPE_01+"\n");
		sql.append(" 		   and sit.status = ?\n");
		sql.append(" 	  group by  area.area_id,e.road_id,sit.sit_id,area.area_name,e.road_name,sit.sit_name\n");
		sql.append(" 	  order by area.area_name,e.road_name,sit.sit_name\n");
		
	    List<Object> params = new ArrayList<Object>();
	    params.add(Constant.AUTO_IN_STATUS_01);
        params.add(Constant.STATUS_ENABLE);
        params.add(Constant.STATUS_ENABLE);
		return pageQuery(sql.toString(), params, getFunName());
	}
	
	public List<Map<String, Object>> getNullRoadListSy() {
		StringBuffer sql = new StringBuffer();
		sql.append(" with aa as(\n");
		sql.append("         select a.road_id, count(1) all_sit from tt_sales_road a,tt_Sales_sit  b where a.road_id=b.road_id\n");
		sql.append("         group by a.road_id),\n");
		sql.append(" bb as(\n");
		sql.append(" select a.road_id,count(1) null_sit from tt_sales_road a,tt_Sales_sit  b where a.road_id=b.road_id and b.vehicle_id=-1 \n");
		sql.append(" group by a.road_id)\n");
		sql.append(" 		select  area.area_id,e.road_id,sit.sit_id,area.area_name,e.road_name,sit.sit_name\n");
		sql.append("          from   tt_sales_area         area,\n");
		sql.append(" 				 tt_sales_sit          sit,\n");
		sql.append(" 				 (select a.road_id,a.area_id,a.road_name,b.all_sit,c.null_sit from tt_sales_road a, aa b,bb c where a.road_id=b.road_id and a.road_id=c.road_id and b.all_sit=c.null_sit) e\n");
		sql.append(" 		 where e.area_id = area.area_id	 \n");
		sql.append(" 		   and e.road_id = sit.road_id\n");
		sql.append("           and area.type = "+Constant.RES_TYPE_05+"\n");
		sql.append(" 	  group by  area.area_id,e.road_id,sit.sit_id,area.area_name,e.road_name,sit.sit_name\n");
		sql.append(" 	  order by area.area_name,e.road_name,sit.sit_name\n");
		
	    List<Object> params = new ArrayList<Object>();
		return pageQuery(sql.toString(), params, getFunName());
	}
	
	
	public List<Map<String, Object>> getInVehInfo(String vin) {
		StringBuffer sql = new StringBuffer();
		sql.append("select  mat.MATERIAL_CODE,\n");
		sql.append("	    mat.MATERIAL_NAME,\n");
		sql.append("		mat.SERIES_NAME,\n");
		sql.append("	    mat.SERIES_CODE,\n");
		sql.append("		mat.MODEL_CODE,\n");
		sql.append("	    mat.MODEL_NAME,\n");
		sql.append("	    mat.PACKAGE_CODE,\n");
		sql.append("		mat.PACKAGE_NAME,\n");
		sql.append("        vh.engine_no,vh.hegezheng_code,to_char(sysdate,'yyyy-mm-dd') ORG_STORAGE_DATE ");
		sql.append("	from tt_sales_vehicel_in            vh,\n");
		sql.append("		 vw_material_group_mat mat\n");
		sql.append("	 where vh.material_id = mat.MATERIAL_ID\n");
		sql.append("          and vh.vin = ?\n");
		
		List<Object> params = new ArrayList<Object>();
		params.add(vin);
		return pageQuery(sql.toString(), params, getFunName());
	}
	

	public List<Map<String, Object>> getMatarielByCode(String materialCode) {
		StringBuffer sql = new StringBuffer();
		sql.append("  SELECT MAT.MATERIAL_ID FROM VW_MATERIAL_GROUP_MAT MAT WHERE MAT.material_code = ? ");
		List<Object> list = new ArrayList<Object>();
		list.add(materialCode);
		return  pageQuery(sql.toString(), list, getFunName());
		
	}
	public PageResult<Map<String, Object>> importQuery(String usrId,
			int pageSize, Integer curPage) {
		StringBuffer sql = new StringBuffer();
		sql.append("	 SELECT * ");
		sql.append("	  FROM	Tmp_Tt_Sales_Storage tt\n");
		sql.append("	  WHERE	 tt.USER_ID = ?\n");
		
		List<Object> params= new ArrayList<Object>();
		params.add(usrId);
		return dao.pageQuery(sql.toString(), params, getFunName(), pageSize, curPage);
	}
	
	public List<Map<String, Object>> getYieldlyAndWarehouse(String materialId) {
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT ba.area_id,\n" );
		sql.append("       ba.area_code,\n" );
		sql.append("       ba.area_name,\n" );
		sql.append("       wh.warehouse_id,\n" );
		sql.append("       wh.warehouse_code,\n" );
		sql.append("       wh.warehouse_name\n" );
		sql.append("  FROM vw_material_group_mat t,\n" );
		sql.append("       tm_area_group         ag,\n" );
		sql.append("       tm_business_area      ba,\n" );
		sql.append("       tm_warehouse          wh\n" );
		sql.append(" WHERE t.series_id = ag.material_group_id\n" );
		sql.append("   AND ag.area_id = ba.area_id\n" );
		sql.append("   AND ba.area_id = wh.area_id\n" );
		sql.append("   AND wh.warehouse_type = 14011001\n" );
		sql.append("   AND t.MATERIAL_ID = ?");		
		List<Object> params= new ArrayList<Object>();
		params.add(materialId);
		return dao.pageQuery(sql.toString(), params, getFunName());
	}
	
	public PageResult<Map<String, Object>> importQueryYeidly(String usrId,
			int pageSize, Integer curPage) {
		StringBuffer sql = new StringBuffer();
		sql.append("	 SELECT * ");
		sql.append("	  FROM	TMP_TSI_YEIDLY_VEHICLE_EXP tt\n");
		sql.append("	  WHERE	 tt.create_by = ?\n");
		
		List<Object> params= new ArrayList<Object>();
		params.add(usrId);
		return dao.pageQuery(sql.toString(), params, getFunName(), pageSize, curPage);
	}
	public PageResult<Map<String, Object>> importQueryStorage(String usrId,
			int pageSize, Integer curPage) {
		StringBuffer sql = new StringBuffer();
		sql.append("	 SELECT * ");
		sql.append("	  FROM	TMP_TSI_IMP_STORAGE_VEHICLE tt\n");
		sql.append("	  WHERE	 tt.create_by = ?\n");
		
		List<Object> params= new ArrayList<Object>();
		params.add(usrId);
		return dao.pageQuery(sql.toString(), params, getFunName(), pageSize, curPage);
	}
	/**保存接车入库车辆信息
	 * @param vehId */
	public void saveVehicleInfo(Long vehId, String vin,Long createBy) {
		
		StringBuffer sql = new StringBuffer();
		sql.append(" INSERT INTO tm_vehicle(VEHICLE_ID,vin,series_id,model_id,package_id,product_date,create_date,\n");
		sql.append("  storage_date,org_type,org_id,vn,node_code,node_date,location,vehicle_type,remark,license_no,\n");
		sql.append("  gearbox_no,rearaxle_no,model_year,factory_date,license_date,start_mileage,mileage,meter_mile,\n");
		sql.append("  history_mile,ver,warehouse_id,transfer_no,org_storage_date,special_batch_no,vehicle_area,\n");
		sql.append("  oem_company_id,batch_no,free_times,area_id,wr_end_date,sit_id,per_id,sit_code,offline_date,\n");
		sql.append("  plan_detail_id,arriv_date,out_detail_id,out_status,sd_number,pin,dealer_id,material_id,life_cycle,\n");
		sql.append("  lock_status,engine_no,color,create_by,yieldly,claim_tactics_id,hegezheng_code,purchased_date,\n");
		sql.append("  process_type,prov_id,hgz_no,import_people,import_date,is_pass_status,no_pass_remark,invoice_no,ERP_ORDER_ID,special_order_no)\n");
		sql.append("  SELECT "+vehId+",vin,series_id,model_id,package_id,product_date,sysdate,\n");
		sql.append("  storage_date,org_type,org_id,vn,node_code,node_date,location,vehicle_type,remark,license_no,\n");
		sql.append("  gearbox_no,rearaxle_no,model_year,factory_date,license_date,start_mileage,mileage,meter_mile,\n");
		sql.append("  history_mile,ver,warehouse_id,transfer_no,org_storage_date,special_batch_no,vehicle_area,\n");
		sql.append("  oem_company_id,batch_no,free_times,area_id,wr_end_date,sit_id,per_id,sit_code,offline_date,\n");
		sql.append("  plan_detail_id,arriv_date,out_detail_id,out_status,sd_number,pin,dealer_id,material_id,life_cycle,\n");
		sql.append("  lock_status,engine_no,color,"+createBy+",yieldly,claim_tactics_id,hegezheng_code,purchased_date,\n");
		sql.append("  process_type,prov_id,hgz_no,import_people,import_date,is_pass_status,no_pass_remark,invoice_no,erp_no,special_order_no\n");
		sql.append("  FROM  tt_sales_vehicel_in tsvi where tsvi.vin = ?");
		List<Object> params = new ArrayList<Object>();
		params.add(vin);
		dao.update(sql.toString(), params);
	}
	
	public List<Map<String, Object>>   getImportVin(String vin, Long poseId){
		List<Object> params = new ArrayList<Object>();
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT st.*, decode(x.material_group_id,null,'0','1') flag\n" );
		sql.append("  FROM tm_vehicle st\n" );
		sql.append("  LEFT JOIN (SELECT ag.material_group_id, ag.area_id\n" );
		sql.append("               FROM tm_area_group         ag,\n" );
		sql.append("                    tm_business_area      ba,\n" );
		sql.append("                    tm_pose_business_area pba\n" );
		sql.append("              WHERE ag.area_id = ba.area_id\n" );
		sql.append("                AND ba.area_id = pba.area_id\n" );
		sql.append("                AND pba.pose_id = ?) x\n");
		sql.append("       ON st.series_id = x.material_group_id and st.yieldly = x.area_id\n");
		params.add(poseId);
		sql.append("   where st.vin = ?");		
		params.add(vin);
		return pageQuery(sql.toString(), params, getFunName());
	}
	public List<Map<String, Object>>   getImportVinYeidly(String vin, Long poseId){
		List<Object> params = new ArrayList<Object>();
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT st.*, decode(x.material_group_id,null,'0','1') flag\n" );
		sql.append("  FROM tm_vehicle st\n" );
		sql.append("  LEFT JOIN (SELECT ag.material_group_id, ag.area_id\n" );
		sql.append("               FROM tm_area_group         ag,\n" );
		sql.append("                    tm_business_area      ba,\n" );
		sql.append("                    tm_pose_business_area pba\n" );
		sql.append("              WHERE ag.area_id = ba.area_id\n" );
		sql.append("                AND ba.area_id = pba.area_id\n" );
		sql.append("                AND pba.pose_id = ?) x\n");
		sql.append("       ON st.series_id = x.material_group_id and st.yieldly = x.area_id\n");
		params.add(poseId);
		sql.append("   where st.vin = ?");		
		params.add(vin);
		return pageQuery(sql.toString(), params, getFunName());
	}
	public List<Map<String, Object>>   getTiExpBusVehStore(String materialCode,
			String erpOrderNo,String isRead){
		StringBuffer sql = new StringBuffer();
		sql.append("select t.*\n");
		sql.append("  from Tsi_Exp_Bus_Veh_Store t\n");
		sql.append(" where t.material = '"+materialCode+"'\n");
		if(!"".equals(erpOrderNo)&&null!=erpOrderNo){
			sql.append("   and t.orderid='"+erpOrderNo+"'\n");
		}else{
			sql.append("   and t.orderid is null\n");
		}
		sql.append("   and t.is_read ="+isRead+"\n");
		sql.append("order by t.create_date desc");
		return pageQuery(sql.toString(), null, getFunName());
	}
	/**
	 * 查询导入数据中的重复数据
	 * @return
	 */
	public List<Map<String, Object>>   getTmpRepeatRecord(){
		StringBuffer sql= new StringBuffer();
		sql.append("select count(t.id) re_num, t.vin\n" );
		sql.append("     from TMP_TSI_YEIDLY_VEHICLE_EXP t\n" );
		sql.append("   having count(t.id) > 1\n" );
		sql.append("    group by t.vin");
		return pageQuery(sql.toString(), null, getFunName());
	}
	/**
	 * 查询导入数据中的重复数据
	 * @return
	 */
	public List<Map<String, Object>>   getTmpRepeatRecordStorage(){
		StringBuffer sql= new StringBuffer();
		sql.append("select count(t.id) re_num, t.vin\n" );
		sql.append("     from TMP_TSI_IMP_STORAGE_VEHICLE t\n" );
		sql.append("   having count(t.id) > 1\n" );
		sql.append("    group by t.vin");
		return pageQuery(sql.toString(), null, getFunName());
	}
	public Map<String, Object> getSepcialNo(String orderNo) {
		StringBuffer sql = new StringBuffer();
		sql.append(" select 1 from tt_sa_order t where t.order_type = ? and t.order_no = ?  ");
		List<Object> params = new ArrayList<Object>();
		params.add(Constant.ORDER_TYPE_03);
		params.add(orderNo);
		return pageQueryMap(sql.toString(), params, getFunName());
	}
	
	public Map<String, Object> getAccPersonId(String perCode) {
		StringBuffer sql = new StringBuffer();
		sql.append(" select per.per_id from tt_sales_accar_per per where per.per_code  = ? ");
		List<Object> params = new ArrayList<Object>();
		params.add(perCode);
		return pageQueryMap(sql.toString(), params, getFunName());
	}
	
	/**
	 * 根据VIN查询区道位
	 * @author liufazhong
	 */
	public List<Map<String, Object>> queryAreaRoadSitInfo(String [] vins){
		StringBuffer sql = new StringBuffer("SELECT A.AREA_CODE,A.AREA_NAME,B.ROAD_NAME,C.SIT_NAME,D.VIN FROM TT_SALES_AREA a," +
				"TT_SALES_ROAD b,TT_SALES_SIT c,TM_VEHICLE d WHERE a.area_id=b.area_id AND b.road_id=c.road_id and d.vehicle_id=c.vehicle_id");
		List<Object> params = new ArrayList<Object>();
		if (vins != null && vins.length > 0) {
			sql.append(" and d.vin in(");
			for (int i = 0; i < vins.length; i++) {
				if (i == vins.length -1) {
					sql.append("?");
				} else {
					sql.append("?,");
				}
				params.add(vins[i]);
			}
			sql.append(")");
		}
		return pageQuery(sql.toString(), params, getFunName());
	}
	
	
	public PageResult<Map<String, Object>> queryMesVehicle(
		Map<String, String> conditions, Integer curPage, Integer pageSize) {
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT TI.VIN,\n" );
		sql.append("       MAT.MATERIAL_CODE,\n" );
		sql.append("       MAT.MATERIAL_NAME,\n" );
		sql.append("       TO_CHAR(TI.OFFLINE_DATE, 'YYYY-MM-DD HH24:MI:SS') OFFLINE_DATE,\n" );
		sql.append("       TO_CHAR(TI.PRODUCT_DATE, 'YYYY-MM-DD HH24:MI:SS') PRODUCT_DATE,\n" );
		sql.append("       TI.HEGEZHENG_CODE,\n" );
		sql.append("       TI.SPECIAL_ORDER_NO,\n" );
		sql.append("       TI.ERP_NO,\n" );
		sql.append("       TI.ENGINE_NO,\n" );
		sql.append("      case when ti.is_storage is null or ti.is_storage = 15271002 then '否' when  ti.is_storage = 15271001 then '是' end is_storage"); 
		sql.append("  FROM TT_SALES_VEHICEL_IN TI, VW_MATERIAL_GROUP_MAT MAT, TM_VEHICLE TM\n" );
		sql.append(" WHERE TI.MATERIAL_ID = MAT.MATERIAL_ID\n" );
		sql.append("   AND TI.VIN = TM.VIN(+) \n");
		List<Object> params = new ArrayList<Object>();
		String vin = conditions.get("VIN");
		if(null != vin && !"".equals(vin)){
			sql.append(" AND TI.VIN LIKE '%"+vin+"%' \n");
		}
		
		String isStorage = conditions.get("IS_STORAGE");
		if(null != isStorage && !"".equals(isStorage)){
			if(isStorage.equals(String.valueOf(Constant.VEHICLE_IN_STATUS_01))){
				sql.append("AND TI.IS_STORAGE = ? AND TM.SIT_CODE IS NOT NULL "); 
			}else{
				sql.append("AND ((TI.IS_STORAGE = ? AND TM.SIT_CODE IS NULL) OR TI.IS_STORAGE IS NULL) "); 
			}
			params.add(isStorage);
		}
		
		String startDate = conditions.get("START_DATE");
		if(null != startDate && !"".equals(startDate)){
			sql.append(" AND TI.CREATE_DATE >= TO_DATE('"+startDate+" 00:00:00','yyyy-mm-dd hh24:mi:ss')");
		}
		
		
		String endDate = conditions.get("END_DATES");
		if(null != endDate && !"".equals(endDate)){
			sql.append(" AND TI.CREATE_DATE <= TO_DATE('"+endDate+" 23:59:59','yyyy-mm-dd hh24:mi:ss')");
		}
		
		String materailCode = conditions.get("MATERAIL_CODE");
		if(null != materailCode && !"".equals(materailCode)){
			sql.append(" AND MAT.MATERIAL_CODE LIKE '%"+materailCode+"%'"); 
		}
		
		String erpOrderNum = conditions.get("ERPORDER_NUM");
		if(null != erpOrderNum && !"".equals(erpOrderNum)){
			sql.append("  AND TI.ERP_NO LIKE  '%"+erpOrderNum+"%'"); 
		}
		
		return pageQuery(sql.toString(), params, getFunName(), pageSize, curPage);
	}
	
	public List<Map<String, Object>> exportMesVehicle(Map<String, String> conditions){
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT TI.VIN,\n" );
		sql.append("       MAT.MATERIAL_CODE,\n" );
		sql.append("       MAT.MATERIAL_NAME,\n" );
		sql.append("       TO_CHAR(TI.OFFLINE_DATE, 'YYYY-MM-DD HH24:MI:SS') OFFLINE_DATE,\n" );
		sql.append("       TO_CHAR(TI.PRODUCT_DATE, 'YYYY-MM-DD HH24:MI:SS') PRODUCT_DATE,\n" );
		sql.append("       TI.HEGEZHENG_CODE,\n" );
		sql.append("       TI.SPECIAL_ORDER_NO,\n" );
		sql.append("       TI.ERP_NO,\n" );
		sql.append("       TI.ENGINE_NO,\n" );
		sql.append("      case when ti.is_storage is null or ti.is_storage = 15271002 then '否' when  ti.is_storage = 15271001 then '是' end is_storage"); 
		sql.append("  FROM TT_SALES_VEHICEL_IN TI, VW_MATERIAL_GROUP_MAT MAT, TM_VEHICLE TM\n" );
		sql.append(" WHERE TI.MATERIAL_ID = MAT.MATERIAL_ID\n" );
		sql.append("   AND TI.VIN = TM.VIN(+) \n");
		List<Object> params = new ArrayList<Object>();
		String vin = conditions.get("VIN");
		if(null != vin && !"".equals(vin)){
			sql.append(" AND TI.VIN LIKE '%"+vin+"%' \n");
		}
		
		String isStorage = conditions.get("IS_STORAGE");
		if(null != isStorage && !"".equals(isStorage)){
			if(isStorage.equals(String.valueOf(Constant.VEHICLE_IN_STATUS_01))){
				sql.append("AND TI.IS_STORAGE = ? AND TM.SIT_CODE IS NOT NULL "); 
			}else{
				sql.append("AND ((TI.IS_STORAGE = ? AND TM.SIT_CODE IS NULL) OR TI.IS_STORAGE IS NULL) "); 
			}
			params.add(isStorage);
		}
		
		String startDate = conditions.get("START_DATE");
		if(null != startDate && !"".equals(startDate)){
			sql.append(" AND TI.CREATE_DATE >= TO_DATE('"+startDate+" 00:00:00','yyyy-mm-dd hh24:mi:ss')");
		}
		
		
		String endDate = conditions.get("END_DATES");
		if(null != endDate && !"".equals(endDate)){
			sql.append(" AND TI.CREATE_DATE <= TO_DATE('"+endDate+" 23:59:59','yyyy-mm-dd hh24:mi:ss')");
		}
		
		String materailCode = conditions.get("MATERAIL_CODE");
		if(null != materailCode && !"".equals(materailCode)){
			sql.append(" AND MAT.MATERIAL_CODE LIKE '%"+materailCode+"%'"); 
		}
		
		String erpOrderNum = conditions.get("ERPORDER_NUM");
		if(null != erpOrderNum && !"".equals(erpOrderNum)){
			sql.append("  AND TI.ERP_NO LIKE  '%"+erpOrderNum+"%'"); 
		}
		
		return pageQuery(sql.toString(), params, getFunName());
	
	}
	
	public Map<String,Object> queryVinMater(String vin){
		StringBuffer sql= new StringBuffer();
		sql.append("  select tm.vin,mat.MATERIAL_CODE,tm.vehicle_id,tm.life_cycle\n" );
		sql.append("    from  tm_vehicle tm,\n" );
		sql.append("          vw_material_group_mat mat\n" );
		sql.append("    where tm.material_id = mat.MATERIAL_ID\n" );
		sql.append("     and tm.vin = ?");
		List<Object> params = new ArrayList<Object>();
		params.add(vin);
		return dao.pageQueryMap(sql.toString(), params, getFunName());
	}
	
//	public void updateEntryQnt(TiExpBusVehStorePO conditions) {
//		StringBuffer sql= new StringBuffer();
//		sql.append("UPDATE  TI_EXP_BUS_VEH_STORE TI SET TI.ENTRY_QNT = (\n" );
//		sql.append("SELECT COUNT(1)\n" );
//		sql.append("  FROM TI_EXP_BUS_VEH_STORE T ,TI_EXP_BUS_VEH_STORE_DET DT\n" );
//		sql.append(" WHERE T.REV_ID =DT.REV_ID\n" );
//		sql.append("  AND T.IS_READ = 0\n" );
//		sql.append("  AND T.MATERIAL = '"+conditions.getMaterial()+"'\n" );
//		sql.append(" AND T.ORDERID =  '"+conditions.getOrderid()+"'\n" );
//		sql.append(" GROUP BY T.MATERIAL\n" );
//		sql.append(") WHERE TI.MATERIAL  = '"+conditions.getMaterial()+"'\n" );
//		sql.append("   AND TI.IS_READ = 0\n" );
//		sql.append("  AND TI.ORDERID = '"+conditions.getOrderid()+"'\n" );
//		
//		this.update(sql.toString(), null);
//	}

	public Map<String, Object> getSuperMaterial(String vin,
			String specialOrderNo) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT DISTINCT VCR.MATNR,tsod.material_id\n");
		sql.append("  FROM tt_sa_order tso  JOIN  tt_sa_order_detail tsod ON tsod.Order_Id = tso.order_id\n");
		sql.append("  JOIN  tt_sa_choosematch_relation  scr ON  scr.detail_id =  tsod.detail_id\n");
		sql.append("  JOIN tt_vs_choosematch_relation vcr ON scr.choosematch_id = vcr.choosematch_id AND vcr.result_id = scr.result_id\n");
		sql.append("  JOIN  tm_vehicle tm ON tm.material_id = tsod.material_id\n");
		sql.append("  WHERE tm.vin = ? \n");
		sql.append("      AND tso.Order_No =? "); 
		List<Object> params = new ArrayList<Object>();
		params.add(vin);
		params.add(specialOrderNo);
		return this.pageQueryMap(sql.toString(), params, getFunName());
	}

	public Map<String, Object> getSpecialVehicleInfo(String specialOrderNo) {
		StringBuffer sql= new StringBuffer();
		sql.append("select sod.material_id,mat.MODEL_ID,mat.PACKAGE_ID,mat.SERIES_ID\n");
		sql.append("  from tt_sa_order so\n");
		sql.append("  join tt_sa_order_detail sod\n");
		sql.append("    on so.order_id = sod.order_id\n");
		sql.append(" JOIN vw_material_group_mat mat ON mat.material_id = sod.material_id"); 
		sql.append("  where so.order_no = ?");
		List<Object> params = new ArrayList<Object>();
		params.add(specialOrderNo);
		return this.pageQueryMap(sql.toString(), params, getFunName());
	}
	
	/**
	 * 
	 * @Title      : 
	 * @Description: 获取ERP_tm_vehicle信息
	 * @param      : 
	 * @param      : @return     获取ERP_tm_vehicle信息
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-11
	 */
	public List<Map<String, Object>> getErpTmVehicle(){
		StringBuffer sbSql= new StringBuffer();
		sbSql.append("SELECT T.VIN,\n");
		sbSql.append("       T.COMMISSIONNO,\n");
		sbSql.append("       T.ACTIONCODE,\n");
		sbSql.append("       T.ACTIONDATE,\n");
		sbSql.append("       T.MATERIALCODE,\n");
		sbSql.append("       T.REMARK,\n");
		sbSql.append("       T.TYPE,\n");
		sbSql.append("       T.STATUS,\n");
		sbSql.append("       T.TIME1,\n");
		sbSql.append("       T.TIME2,\n");
		sbSql.append("       T.TIME3,\n");
		sbSql.append("       T.TIME4\n");
		sbSql.append("  FROM Z_TM_VEHICLE T\n");
		sbSql.append(" WHERE T.ACTIONCODE = '质检下线'\n");
		sbSql.append("   AND T.STATUS = '未处理'"); 
		List<Map<String, Object>> list = pageQuery(sbSql.toString(),null,getFunName());
		return list;
	}
	
	/**
	 * 
	 * @Title      : 
	 * @Description: 根据物料代码获取物料组信息
	 * @param      : @params 查询参数matCode
	 * @param      : @return    获取物料组信息
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-11
	 */
	public Map<String, Object> getMatGroup(String matCode){
		StringBuffer sbSql= new StringBuffer();
		sbSql.append("SELECT *\n");
		sbSql.append(" FROM VW_MATERIAL_GROUP_MAT A\n");
		sbSql.append("  WHERE A.MATERIAL_CODE =?"); 
		List<Object> list6=new ArrayList<Object>();
		list6.add(matCode);//物料代码
		Map<String, Object> map = pageQueryMap(sbSql.toString(),list6,getFunName());
		return map;
	}
	
	public List<Map<String, Object>> getSitMsg(String matCode) {
		StringBuffer sbSql= new StringBuffer();
		sbSql.append("SELECT E.SIT_ID,A.AREA_NAME, D.ROAD_NAME, E.SIT_NAME\n");
		sbSql.append("  FROM TT_SALES_AREA A, TT_SALES_ROAD D, TT_SALES_SIT E\n");
		sbSql.append(" WHERE A.AREA_ID = D.AREA_ID\n");
		sbSql.append("   AND D.ROAD_ID = E.ROAD_ID\n");
		sbSql.append("   AND E.VEHICLE_ID = -1\n");
		sbSql.append("   AND EXISTS (SELECT 1\n");
		sbSql.append("          FROM TT_SALES_AREA_MAT B, VW_MATERIAL_GROUP_MAT C\n");
		sbSql.append("         WHERE B.MAT_ID = C.SERIES_ID\n");
		sbSql.append("           AND B.AREA_ID = A.AREA_ID\n");
		sbSql.append("           AND C.MATERIAL_CODE = ?)\n");
		sbSql.append(" ORDER BY A.AREA_NAME, D.ROAD_NAME, E.SIT_NAME"); 
		List<Object> list6=new ArrayList<Object>();
		list6.add(matCode);//物料代码
		List<Map<String, Object>> list = pageQuery(sbSql.toString(),list6,getFunName());
		return list;
	}
	
	/**
	 * 导入差异结果查询
	 * @param conditions
	 * @param curPage
	 * @param pageSize
	 * @return
	 */
	public PageResult<Map<String, Object>> importDiffQuery(Map<String,Object> map,Integer curPage, Integer pageSize) {
		String sql = getDiffQuerySql(map);
		return pageQuery(sql, null, getFunName(), pageSize, curPage);		
	}
	
	/**
	 * 导入差异结果导出
	 * @return
	 */
	public List<Map<String, Object>> importDiffExport(Map<String,Object> map) {
		String sql = getDiffQuerySql(map);
		List<Map<String, Object>> list = pageQuery(sql,null,getFunName());
		return list;
	}
	
	/**
	 * 返回导入差异结果查询语句
	 * @return
	 */
	private String getDiffQuerySql(Map<String,Object> map){
		String startdate = CommonUtils.checkNull(map.get("startdate")); //导入日期开始
		String endDate = CommonUtils.checkNull(map.get("endDate")); // 导入日期结束
		
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT *\n" );
		sql.append("  FROM (SELECT '基地等待' imp_type,\n" );
		sql.append("               vin,\n" );
		sql.append("               u.NAME NAME,\n" );
		sql.append("               t.create_date,\n" );
		sql.append("               '' in_type,\n" );
		sql.append("               material_code,\n" );
		sql.append("               material_name,\n" );
		sql.append("               color_code,\n" );
		sql.append("               color_name,\n" );
		sql.append("               engine_no,\n" );
		sql.append("               hegezheng_code,gearbox_no,\n" );
		sql.append("               product_date,\n" );
		sql.append("               offline_date\n" );
		sql.append("          FROM tmp_ti_yeildly_vhcl t, tc_user u\n" );
		sql.append("         WHERE t.create_by = u.user_id\n" );
		sql.append("        UNION ALL\n" );
		sql.append("        SELECT '三方等待' imp_type,\n" );
		sql.append("               vin,\n" );
		sql.append("               u.NAME yname,\n" );
		sql.append("               t.create_date,\n" );
		sql.append("               t.in_type,\n" );
		sql.append("               '',\n" );
		sql.append("               '',\n" );
		sql.append("               '',\n" );
		sql.append("               '',\n" );
		sql.append("               '','',\n" );
		sql.append("               '',\n" );
		sql.append("               NULL,\n" );
		sql.append("               NULL\n" );
		sql.append("          FROM tmp_ti_storage_vhcl t, tc_user u\n" );
		sql.append("         WHERE t.create_by = u.user_id\n" );
		sql.append("         ORDER BY 4 DESC) TT\n" );
		sql.append(" WHERE 1 = 1\n");
		if(startdate.equals("")&&endDate.equals("")){
			sql.append("AND TO_CHAR(TT.CREATE_DATE, 'YYYY-MM-DD') =TO_CHAR(sysdate, 'YYYY-MM-DD')\n");//默认当前日期
		}else{
			if(!startdate.equals("")&&null!=startdate){
				sql.append("AND TT.CREATE_DATE>=TO_DATE('"+startdate+" 00:00:00','YYYY-MM-DD HH24:MI:SS')\n"); 
			}
			if(!endDate.equals("")&&null!=endDate){
				sql.append("AND TT.CREATE_DATE<=TO_DATE('"+endDate+" 23:59:59','YYYY-MM-DD HH24:MI:SS')\n"); 
			}
		}
		
		return sql.toString();
	}
}

