package com.infodms.dms.dao.sales.storage;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TmVehiclePO;
import com.infoservice.po3.bean.PO;

/**
 * @ClassName : VehicleSiteAdjustDao
 * @Description : 车辆位置调整DAO
 * @author : ranjian
 *         CreateDate : 2013-4-14
 */
public class FlexStoragePicDao extends BaseDao<PO> {
	private static final FlexStoragePicDao dao = new FlexStoragePicDao();
	
	public static final FlexStoragePicDao getInstance()
	{
		return dao;
	}
	
	@Override
	protected PO wrapperPO(ResultSet rs, int idx)
	{
		// TODO Auto-generated method stub
		return null;
	}
	/**
	 * @Title :
	 * @Description: 根据道产地获取库道位
	 * @param : @param params 查询参数params
	 * @param : @return 满足条件库道位信息
	 * @return :
	 * @throws :
	 *         LastDate : 2013-4-14
	 */
	public List<Map<String, Object>> getLeftList(List<Object> params) throws Exception
	{
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("SELECT TSA.AREA_ID,\n");
		sbSql.append("       TSA.AREA_CODE,\n");
		sbSql.append("       TSA.AREA_NAME,\n");
		sbSql.append("       TC.CODE_ID,\n");
		sbSql.append("       TC.CODE_DESC,\n");
		sbSql.append("       NVL(R.ROADMUM, 0) ROADCOUNT,\n");
		sbSql.append("       NVL(R1.VECOUNT, 0) VECOUNT,\n");
		sbSql.append("       NVL(R2.SITCOUNT, 0) SITCOUNT\n");
		sbSql.append("  FROM TT_SALES_AREA TSA,\n");
		sbSql.append("       TC_CODE TC,\n");
		sbSql.append("       (SELECT TSR.AREA_ID, COUNT(1) ROADMUM --库道数\n");
		sbSql.append("          FROM TT_SALES_ROAD TSR\n");
		sbSql.append("         WHERE TSR.STATUS = ?\n");
		sbSql.append("         GROUP BY TSR.AREA_ID) R,\n");
		sbSql.append("       (SELECT TSA.AREA_ID, COUNT(1) VECOUNT --车辆数\n");
		sbSql.append("          FROM TT_SALES_AREA TSA, TT_SALES_ROAD TSR, TT_SALES_SIT TSS\n");
		sbSql.append("         WHERE TSA.AREA_ID = TSR.AREA_ID\n");
		sbSql.append("           AND TSR.ROAD_ID = TSS.ROAD_ID\n");
		sbSql.append("           AND TSS.VEHICLE_ID != -1\n");
		sbSql.append("           AND TSR.STATUS = ?\n");
		sbSql.append("           AND TSS.STATUS = ?\n");
		sbSql.append("         GROUP BY TSA.AREA_ID) R1,\n");
		sbSql.append("       (SELECT TSA.AREA_ID, COUNT(1) SITCOUNT --可用库位\n");
		sbSql.append("          FROM TT_SALES_AREA TSA, TT_SALES_ROAD TSR, TT_SALES_SIT TSS\n");
		sbSql.append("         WHERE TSA.AREA_ID = TSR.AREA_ID\n");
		sbSql.append("           AND TSR.ROAD_ID = TSS.ROAD_ID\n");
		sbSql.append("           AND TSS.VEHICLE_ID = -1\n");
		sbSql.append("           AND TSR.STATUS = ?\n");
		sbSql.append("           AND TSS.STATUS = ?\n");
		sbSql.append("         GROUP BY TSA.AREA_ID) R2\n");
		sbSql.append(" WHERE TSA.TYPE = TC.CODE_ID\n");
		sbSql.append("   AND TSA.AREA_ID = R.AREA_ID(+)\n");
		sbSql.append("   AND TSA.AREA_ID = R1.AREA_ID(+)\n");
		sbSql.append("   AND TSA.AREA_ID = R2.AREA_ID(+)\n");
		sbSql.append("   AND TSA.STATUS = ?\n");
		sbSql.append("   AND TSA.YIELDLY = ?\n"); 
		sbSql.append("   ORDER BY TSA.AREA_ID"); 

		List<Map<String, Object>> list = dao.pageQuery(sbSql.toString(), params, getFunName());
		return list;
	}
	/**
	 * @Title :
	 * @Description: 根据道产地获取库区信息
	 * @param : @param params 查询参数params
	 * @param : @return 满足条件库区信息
	 * @return :
	 * @throws :
	 *         LastDate : 2013-4-14
	 */
	public List<Map<String, Object>> getYieldlyIdByArea(List<Object> params) throws Exception
	{
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("SELECT TSA.AREA_ID, TSA.AREA_NAME\n");
		sbSql.append("  FROM TT_SALES_AREA TSA\n");
		sbSql.append(" WHERE TSA.STATUS = ?\n");
		sbSql.append(" AND TSA.YIELDLY = ?"); 
		List<Map<String, Object>> list = dao.pageQuery(sbSql.toString(), params, getFunName());
		return list;
	}
	/**
	 * @Title :
	 * @Description: 根据道库区ID获取库道信息
	 * @param : @param params 查询参数params
	 * @param : @return 满足条件库道信息
	 * @return :
	 * @throws :
	 *         LastDate : 2013-4-14
	 */
	public List<Map<String, Object>> getAreaIdByRoad(List<Object> params) throws Exception
	{
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT TSR.ROAD_ID, TSR.ROAD_NAME,TSR.IN_STATUS,TSR.OUT_STATUS\r\n");
		sql.append("  FROM TT_SALES_ROAD TSR\r\n");
		sql.append(" WHERE TSR.STATUS = ?\r\n");
		sql.append("   AND TSR.AREA_ID = ? ORDER BY TO_NUMBER(TSR.ROAD_NAME)");
		List<Map<String, Object>> list = dao.pageQuery(sql.toString(), params, getFunName());
		return list;
	}
	
	/**
	 * @Title :
	 * @Description: 根据道库道ID获取库位信息
	 * @param : @param params 查询参数params ,type操作类型
	 * @param : @return 满足条件库位信息
	 * @return :
	 * @throws :
	 *         LastDate : 2013-4-14
	 */
	public List<Map<String, Object>> getRoadIdBySit(List<Object> params,String type,String sitId) throws Exception
	{
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("SELECT TSS.SIT_ID,\n");
		sbSql.append("       TSS.SIT_NAME,\n");
		sbSql.append("       TSS.VEHICLE_ID,\n");
		sbSql.append("       TSR.IN_STATUS,\n");
		sbSql.append("       TSR.OUT_STATUS,\n");
		sbSql.append("       TSR.ROAD_ID,\n");
		sbSql.append("       TSR.ROAD_NAME,\n");
		sbSql.append("       TSA.AREA_ID,\n");
		sbSql.append("       TSA.AREA_NAME,\n");
		sbSql.append("       TSSI.IMG_URL,\n");
		sbSql.append("       TSSI.IMG_NAME,\n");
		sbSql.append("       TV.SPECIAL_ORDER_NO, --特殊订单号，处理特殊订单时候用\n"); 
		sbSql.append("       CASE\n");
		sbSql.append("         WHEN TSS.VEHICLE_ID = -1 AND\n");
		sbSql.append("              (TSSI.IMG_URL || TSSI.IMG_NAME) IS NULL THEN\n");
		sbSql.append("          'nullSit' --空库位\n");
		sbSql.append("         WHEN TSS.VEHICLE_ID != -1 AND\n");
		sbSql.append("              (TSSI.IMG_URL || TSSI.IMG_NAME) IS NULL THEN\n");
		sbSql.append("          'notPicture' --无图片\n");
		sbSql.append("         WHEN TSS.VEHICLE_ID = -1 AND\n");
		sbSql.append("              (TSSI.IMG_URL || TSSI.IMG_NAME) IS NOT NULL THEN\n");
		sbSql.append("          'dataError' --数据错误\n");
		sbSql.append("         ELSE\n");
		sbSql.append("          TSSI.IMG_URL || TSSI.IMG_NAME --正常\n");
		sbSql.append("       END RE_URL,\n");
		sbSql.append("       DECODE(TO_CHAR(TV.ORG_STORAGE_DATE, 'YYYY-MM-DD'),\n");
		sbSql.append("              TO_CHAR(SYSDATE, 'YYYY-MM-DD'),\n");
		sbSql.append("              1,\n");
		sbSql.append("              0) TODAY, --回退时候用该字段\n");
		sbSql.append("       VMGM.SERIES_ID, --车系ID(配车用)\n");
		sbSql.append("       TV.MATERIAL_ID, --物料ID(配车用)\n");
		sbSql.append("       TO_CHAR(TV.ORG_STORAGE_DATE, 'YYYY-MM-DD') ORG_STORAGE_DATE, --入库时间(配车用)\n");
		sbSql.append("       TV.ENGINE_NO, --发动机号(配车用)\n");
		sbSql.append("       TV.VIN, --发动机号(配车用)\n");
		sbSql.append("       TV.LIFE_CYCLE, --车辆生命周期\n");
		sbSql.append("       TV.LOCK_STATUS, --车辆锁定状态\n");
		sbSql.append("       DECODE(TSAD.DETAIL_ID, '', 0, 1) CHECK_VEHICLE --是否已配车\n");
		sbSql.append("  FROM TT_SALES_SIT          TSS,\n");
		sbSql.append("       TT_SALES_ROAD         TSR,\n");
		sbSql.append("       TT_SALES_AREA         TSA,\n");
		sbSql.append("       TM_VEHICLE            TV,\n");
		sbSql.append("       TT_SALES_ALLOCA_DE    TSAD, --已配车明细表\n");
		sbSql.append("       VW_MATERIAL_GROUP_MAT VMGM,\n");
		sbSql.append("       TT_SALES_SERIES_IMG   TSSI\n");
		sbSql.append(" WHERE TSS.ROAD_ID = TSR.ROAD_ID\n");
		sbSql.append("   AND TSR.AREA_ID = TSA.AREA_ID\n");
		sbSql.append("   AND TV.VEHICLE_ID = TSAD.VEHICLE_ID(+)\n");
		sbSql.append("   AND TSS.VEHICLE_ID = TV.VEHICLE_ID(+)\n");
		sbSql.append("   AND TV.MATERIAL_ID = VMGM.MATERIAL_ID(+)\n");
		sbSql.append("   AND VMGM.SERIES_ID = TSSI.SERIES_ID(+)"); 
		sbSql.append("   AND TSS.STATUS = ?\r\n");
		sbSql.append("   AND TSS.ROAD_ID = ?\r\n");
		if(type!=null && type.equals("combobox")){//下拉框过滤
			if(sitId!=null){
				sbSql.append("   AND (TSS.VEHICLE_ID =").append(Constant.DEFAULT_VALUE).append(" or TSS.SIT_ID="+sitId+") \r\n");
			}else{
				sbSql.append("   AND TSS.VEHICLE_ID =").append(Constant.DEFAULT_VALUE).append("\r\n");
			}
			
		}
		sbSql.append(" ORDER BY TO_NUMBER(TSS.SIT_NAME)");
		List<Map<String, Object>> list = dao.pageQuery(sbSql.toString(), params, getFunName());
		return list;
	}
	
	/**
	 * @Title :
	 * @Description: 根车辆ID获取车辆信息
	 * @param : @param params 查询参数MAP
	 * @param : @return 满足条件的车辆信息
	 * @return :
	 * @throws :
	 *         LastDate : 2013-4-15
	 */
	public Map<String, Object> getVechileByVId(List<Object> params)
	{
		
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT TV.VEHICLE_ID,\n" );
		sql.append("       TBA.AREA_ID YIELDLY_ID,\n" );
		sql.append("       TBA.AREA_NAME YIELDLY, --产地\n" );
		sql.append("       TSBD.INVOICE_NO, --发票号\n" );
		sql.append("       TV.SPECIAL_ORDER_NO ORDER_NO, --提车单号\n" );
		sql.append("       VMGM.MODEL_CODE, --车型CODE\n" );
		sql.append("       VMGM.MODEL_NAME, --车型名称\n" );
		sql.append("       VMGM.PACKAGE_CODE, --配置CODE\n" );
		sql.append("       VMGM.PACKAGE_NAME, --配置名称\n" );
		sql.append("       VMGM.MATERIAL_CODE, --物料CODE\n" );
		sql.append("       VMGM.MATERIAL_NAME, --物料名称\n" );
		sql.append("       TV.VIN, --VIN\n" );
		sql.append("       TV.ENGINE_NO, --发动机号\n" );
		sql.append("       TO_CHAR(TV.PRODUCT_DATE, 'YYYY-MM-DD') PRODUCT_DATE, --生产日期\n" );
		sql.append("       TO_CHAR(TV.OFFLINE_DATE, 'YYYY-MM-DD') OFFLINE_DATE, --下线日期\n" );
		sql.append("       TO_CHAR(TV.ORG_STORAGE_DATE, 'YYYY-MM-DD') ORG_STORAGE_DATE, --入库日期\n" );
		sql.append("       TV.PER_ID, --接车员\n" );
		sql.append("       TSAP.PER_NAME, --接车员\n" );
		sql.append("       TSAP.PER_CODE, --接车员code\n" );
		sql.append("       TSA.AREA_ID, --库区ID\n" );
		sql.append("       TSA.AREA_NAME, --库区NAME\n" );
		sql.append("       TSR.ROAD_ID, --库道ID\n" );
		sql.append("       TSR.ROAD_NAME, --库道NAME\n" );
		sql.append("       TV.SIT_ID, --库位ID\n" );
		sql.append("       TSS.SIT_NAME, --库位NAME\n" );
		sql.append("       TV.SIT_CODE, --库位码\n" );
		sql.append("       TV.SD_NUMBER, --流水号\n" );
		sql.append("       TV.HEGEZHENG_CODE --合格证号\n" );
		sql.append("  FROM TM_VEHICLE            TV,\n" );
		sql.append("       TM_BUSINESS_AREA      TBA,\n" );
		sql.append("       VW_MATERIAL_GROUP_MAT VMGM,\n" );
		sql.append("       TT_SALES_ACCAR_PER    TSAP,\n" );
		sql.append("       TT_SALES_SIT          TSS,\n" );
		sql.append("       TT_SALES_ROAD         TSR,\n" );
		sql.append("       TT_SALES_AREA         TSA,\n" );
		sql.append("       (SELECT VEHICLE_ID,BO_DE_ID FROM TT_SALES_ALLOCA_DE WHERE STATUS!=?) TSAD,\n" );
		sql.append("       TT_SALES_BO_DETAIL    TSBD\n" );
		sql.append(" WHERE TV.YIELDLY = TBA.AREA_ID\n" );
		sql.append("   AND TV.MATERIAL_ID = VMGM.MATERIAL_ID\n" );
		sql.append("   AND TV.PER_ID = TSAP.PER_ID(+)\n" );
		sql.append("   AND TV.VEHICLE_ID = TSS.VEHICLE_ID\n" );
		sql.append("   AND TSS.ROAD_ID = TSR.ROAD_ID\n" );
		sql.append("   AND TSR.AREA_ID = TSA.AREA_ID\n" );
		sql.append("   AND TV.VEHICLE_ID = TSAD.VEHICLE_ID(+)\n" );
		sql.append("   AND TSAD.BO_DE_ID = TSBD.BO_DE_ID(+)\n" );
		sql.append("   AND TSS.STATUS = ?\n" );
		sql.append("   AND TSS.VEHICLE_ID = ? ");
		Map<String, Object> map = dao.pageQueryMap(sql.toString(), params, getFunName());
		return map;
		
		
		

	}
	
	/**
	 * @Title :FLEX (根据库位ID获取选中的区道位)
	 * @Description: 获取选中的区道位
	 * @param : sitId 库列表
	 * @return :库区信息
	 * @throws :
	 * @LastDate : 2013-6-14
	 */
	public Map<String, Object> getCheckBySitId(String sitId)
	{
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("SELECT TSA.AREA_ID, TSA.AREA_NAME,TSA.YIELDLY,TSR.ROAD_ID,TSR.ROAD_NAME,TSS.SIT_ID,TSS.SIT_NAME,TSS.VEHICLE_ID\n");
		sbSql.append("  FROM TT_SALES_AREA TSA, TT_SALES_ROAD TSR, TT_SALES_SIT TSS\n");
		sbSql.append(" WHERE TSA.AREA_ID = TSR.AREA_ID\n");
		sbSql.append("   AND TSR.ROAD_ID = TSS.ROAD_ID\n");
		sbSql.append("   AND TSS.SIT_ID = ?"); 
		List<Object> param=new ArrayList<Object>();
		param.add(sitId);
		Map<String, Object> map = dao.pageQueryMap(sbSql.toString(), param, getFunName());
		return map;
	}
	/**
	 * @Title :
	 * @Description: 调整车辆信息
	 * @param : @param tmVehiclePO
	 * @return :
	 * @throws :
	 *         LastDate : 2013-4-14
	 */
	public void vehicleUpdate(TmVehiclePO seachParam, TmVehiclePO tmVehiclePO)
	{
		dao.update(seachParam, tmVehiclePO);
	}
	/**
	 * @Title :
	 * @Description: 根库位ID获取库区，库位，库道信息
	 * @param : @param params 查询参数MAP
	 * @param : @return 满足条件的区，道，位信息
	 * @return :
	 * @throws :
	 *         LastDate : 2013-4-15
	 */
	public Map<String, Object> getStorageBySId(List<Object> params)
	{
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("SELECT TSS.SIT_NAME, --库位名称\n");
		sbSql.append("       TSS.ROAD_ID, --库道ID\n");
		sbSql.append("       TSS.VEHICLE_ID, --车辆ID\n");
		sbSql.append("       TSR.ROAD_NAME, --库道名称\n");
		sbSql.append("       TSR.IN_STATUS, --入库状态\n");
		sbSql.append("       TSR.OUT_STATUS, --出库状态\n");
		sbSql.append("       TSA.AREA_NAME AREA_NAME, --库区名称\n");
		sbSql.append("       TSA.TYPE AREA_TYPE, --库区类型\n");
		sbSql.append("       TSA.IN_STATUS, --入库状态\n");
		sbSql.append("       TSA.STATUS --库区状态\n");
		sbSql.append("  FROM TT_SALES_SIT TSS, TT_SALES_ROAD TSR, TT_SALES_AREA TSA\n");
		sbSql.append(" WHERE TSS.ROAD_ID = TSR.ROAD_ID\n");
		sbSql.append("   AND TSR.AREA_ID = TSA.AREA_ID\n");
		sbSql.append("   AND TSS.SIT_ID = ?");
		Map<String, Object> map = dao.pageQueryMap(sbSql.toString(), params, getFunName());
		return map;
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 库道状态修改
	 * @param      : userId  用户ID
	 * @param      : type  更改类型(1:入库锁定，2:入库正常，3:出库锁定，4：出库正常)
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-6-15
	 */
	public void changeStatus(Long userId,Long roadId,String type) {
		//入库处理语句
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("UPDATE TT_SALES_ROAD TSR SET\n");
		List<Object> params=new ArrayList<Object>();
		if(type.equals("1")){
			sbSql.append("    TSR.IN_STATUS = ").append(Constant.AUTO_IN_STATUS_01).append(",\n");
		}
		if(type.equals("2")){
			sbSql.append("   TSR.IN_STATUS = ").append(Constant.AUTO_IN_STATUS_02).append(",\n");
		}
		if(type.equals("3")){
			sbSql.append("   TSR.OUT_STATUS = ").append(Constant.AUTO_OUT_STATUS_01).append(",\n");
		}
		if(type.equals("4")){
			sbSql.append("   TSR.OUT_STATUS = ").append(Constant.AUTO_OUT_STATUS_02).append(",\n");
		}
		params.add(userId);
		params.add(roadId);
		sbSql.append("   TSR.UPDATE_BY = ").append(userId).append(",\n");
		sbSql.append("   TSR.UPDATE_DATE =SYSDATE\n");
		sbSql.append("   WHERE ROAD_ID = ").append(roadId);
		if(type!=null && type!=""){
			dao.update(sbSql.toString(), null);  
		}
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 获取可用车辆
	 * @param      : material_id  物料代码
	 * @param      : area_Id  产地ID
	 * @param      : area_Id  产地ID
	 * @return     : checkVe 已选车   
	 * @throws     :
	 * LastDate    : 2013-6-15
	 */
	public List<Map<String, Object>>  getEnableVehicle(String material_id,String area_Id,String[] checkVe,String orderType,String specialOrderNo){
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("SELECT TSA.AREA_ID,\n");
		sbSql.append("           TSA.AREA_NAME,\n");
		sbSql.append("           TSR.ROAD_ID,\n");
		sbSql.append("           TO_NUMBER(TSR.ROAD_NAME) ROAD_NAME,\n");
		sbSql.append("           TSS.SIT_ID,\n");
		sbSql.append("           TO_NUMBER(TSS.SIT_NAME) SIT_NAME,\n");
		sbSql.append("           WM.SERIES_ID,\n");
		sbSql.append("           WM.SERIES_NAME,\n");
		sbSql.append("           WM.MODEL_ID,\n");
		sbSql.append("           WM.MODEL_NAME,\n");
		sbSql.append("           WM.PACKAGE_ID,\n");
		sbSql.append("           WM.PACKAGE_NAME,\n");
		sbSql.append("           WM.MATERIAL_ID,\n");
		sbSql.append("           WM.MATERIAL_NAME,\n");
		sbSql.append("           WM.MATERIAL_CODE,\n");
		sbSql.append("           (SELECT CODE_DESC FROM TC_CODE X WHERE X.CODE_ID=TSR.OUT_STATUS) STATUS,"); 
		sbSql.append("           TV.VIN,\n");
		sbSql.append("           TO_CHAR(TV.ORG_STORAGE_DATE,'YYYY-MM-DD HH24:MI:SS') ORG_STORAGE_DATE,\n");
		sbSql.append("           TO_CHAR(TV.PRODUCT_DATE,'YYYY-MM-DD HH24:MI:SS') PRODUCT_DATE\n");
		sbSql.append("      FROM TT_SALES_AREA         TSA,\n");
		sbSql.append("           TT_SALES_ROAD         TSR,\n");
		sbSql.append("           TT_SALES_SIT          TSS,\n");
		sbSql.append("           TM_VEHICLE            TV,\n");
		sbSql.append("           VW_MATERIAL_GROUP_MAT WM\n");
		sbSql.append("     WHERE TSA.AREA_ID = TSR.AREA_ID\n");
		sbSql.append("       AND TSR.ROAD_ID = TSS.ROAD_ID\n");
		sbSql.append("       AND TSS.VEHICLE_ID = TV.VEHICLE_ID\n");
		sbSql.append("       AND TV.MATERIAL_ID = WM.MATERIAL_ID\n");
		sbSql.append("       AND TSA.YIELDLY = ?\n");
		sbSql.append("       AND TSR.STATUS = ?\n");
		sbSql.append("       AND TSS.STATUS = ?\n");
		//sbSql.append("       AND TSR.OUT_STATUS = ?\n");
		sbSql.append("       AND TV.LIFE_CYCLE = ?\n");
		sbSql.append("       AND TV.LOCK_STATUS = ?\n");
		sbSql.append("       AND TSA.OUT_STATUS=?\n");
		sbSql.append("       AND TV.VEHICLE_ID NOT IN(-1,"); 
		if(checkVe!=null && checkVe.length>0){//排除已选的
			for(int i=0;i<checkVe.length;i++){
				sbSql.append(checkVe[i]==""?Constant.DEFAULT_VALUE:checkVe[i]).append(",");	
			}
		}else{
			sbSql.append("-1,\n");
		}
		sbSql.append("-1)\n");
		List<Object> param=new ArrayList<Object>();
		param.add(area_Id);
		param.add(Constant.STATUS_ENABLE);
		param.add(Constant.STATUS_ENABLE);
		//param.add(Constant.AUTO_OUT_STATUS_01);//出库状态位正常的
		param.add(Constant.VEHICLE_LIFE_02);//车厂库存
		param.add(Constant.LOCK_STATUS_01);//正常状态
		param.add(Constant.OUT_STATUS_01);//可出库的
		sbSql.append("       AND WM.MATERIAL_ID = ?\n");
		param.add(material_id);
		sbSql.append("ORDER BY TV.ORG_STORAGE_DATE,TO_NUMBER(TSR.ROAD_NAME),TO_NUMBER(TSS.SIT_NAME) ASC\n");
		
		List<Map<String, Object>> list = dao.pageQuery(sbSql.toString(), param, getFunName());
		return list;
	}
	/**
	 * @Title :
	 * @Description: 根据组板明细ID 获取组板明细信息
	 * @param : 
	 * @param : 
	 * @return :
	 * @throws :
	 *         LastDate : 2013-4-15
	 */
	public Map<String, Object> getBObyBoId(Long boDeId)
	{
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("SELECT T1.MAT_ID,T1.ORDER_TYPE,\n");
		sbSql.append("       NVL(T1.BOARD_NUM, 0) BOARD_NUM,\n");
		sbSql.append("       NVL(T1.ALLOCA_NUM, 0) ALLOCA_NUM\n");
		sbSql.append("  FROM TT_SALES_BO_DETAIL T1\n");
		sbSql.append(" WHERE T1.BO_DE_ID = ?"); 
		List<Object> params=new ArrayList<Object>();
		params.add(boDeId);
		Map<String, Object> map = dao.pageQueryMap(sbSql.toString(), params, getFunName());
		return map;
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 根据车辆ID获取车辆
	 * @param      : vehicle_ids  车辆IDS
	 * @param      : 
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-6-15
	 */
	public List<Map<String, Object>>  getVehicleByIds(String[] vehicle_ids){
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("SELECT TSA.AREA_ID,\n");
		sbSql.append("       TSA.AREA_NAME,\n");
		sbSql.append("       TSR.ROAD_ID,\n");
		sbSql.append("       TSR.ROAD_NAME,\n");
		sbSql.append("       TSS.SIT_ID,\n");
		sbSql.append("       TSS.SIT_NAME,\n");
		sbSql.append("       TV.VIN,\n");
		sbSql.append("       TV.SD_NUMBER,\n");
		sbSql.append("       TV.ENGINE_NO,\n");
		sbSql.append("       TO_CHAR(TV.ORG_STORAGE_DATE, 'YYYY-MM-DD hh24:mi:ss') ORG_STORAGE_DATE,\n");
		sbSql.append("       TV.VEHICLE_ID\n");
		sbSql.append("  FROM TT_SALES_AREA TSA,\n");
		sbSql.append("       TT_SALES_ROAD TSR,\n");
		sbSql.append("       TT_SALES_SIT  TSS,\n");
		sbSql.append("       TM_VEHICLE    TV\n");
		sbSql.append(" WHERE TSA.AREA_ID = TSR.AREA_ID\n");
		sbSql.append("   AND TSR.ROAD_ID = TSS.ROAD_ID\n");
		sbSql.append("   AND TSS.VEHICLE_ID = TV.VEHICLE_ID\n");
		sbSql.append("   AND TV.VEHICLE_ID IN(-1,\n");
		if(vehicle_ids!=null && vehicle_ids.length>0){
			for(int i=0;i<vehicle_ids.length;i++){
				sbSql.append(vehicle_ids[i]==""?Constant.DEFAULT_VALUE:vehicle_ids[i]).append(",");	
			}
		}else{
			sbSql.append("-1,\n");
		}
		sbSql.append("-1)\n");
		sbSql.append(" ORDER BY TO_NUMBER(TSS.SIT_NAME)"); 
		List<Map<String, Object>> list = dao.pageQuery(sbSql.toString(),null, getFunName());
		return list;
	}
	/**
	 * Function : 查询车厂职位对应产地
	 * 
	 * @param : 职位ID
	 */
	public  List<Map<String, Object>> getPoseIdBusiness(String poseId,String yelId)
	{
		List<Map<String, Object>> list = null;
		
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT TBA.AREA_ID,\n");
		sql.append("       TBA.AREA_CODE,\n");
		sql.append("       TBA.AREA_NAME,\n");
		sql.append("       TPBA.POSE_ID\n");
		sql.append("  FROM TM_POSE_BUSINESS_AREA TPBA, TM_BUSINESS_AREA TBA\n");
		sql.append(" WHERE TPBA.AREA_ID = TBA.AREA_ID\n");
		sql.append("   AND TBA.STATUS = " + Constant.STATUS_ENABLE + "\n");
		sql.append("   AND TPBA.POSE_ID = " + poseId + "");
		if(yelId!=null && yelId!=""){
			sql.append("   AND TBA.AREA_ID = " + yelId + "");
		}
		list = dao.pageQuery(sql.toString(), null, dao.getFunName());
		return list;
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 根据车辆ID获取历史记录
	 * @param      : vehicle_ids  车辆IDS
	 * @param      : 
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-6-15
	 */
	public List<Map<String, Object>>  getVehicleHisById(String vId){
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("select D.AREA_NAME,\n");
		sbSql.append("       C.ROAD_NAME,\n");
		sbSql.append("       B.SIT_NAME,\n");
		sbSql.append("       TO_CHAR(A.CH_DATE, 'YYYY-MM-DD HH24:MI:SS') CH_DATE,\n");
		sbSql.append("       E.NAME\n");
		sbSql.append("  from TT_SALES_POS_HIS A,\n");
		sbSql.append("       TT_SALES_SIT     B,\n");
		sbSql.append("       TT_SALES_ROAD    C,\n");
		sbSql.append("       TT_SALES_AREA    D,\n");
		sbSql.append("       TC_USER          E\n");
		sbSql.append(" WHERE A.OLD_SIT_ID = B.SIT_ID\n");
		sbSql.append("   AND B.ROAD_ID = C.ROAD_ID\n");
		sbSql.append("   AND C.AREA_ID = D.AREA_ID\n");
		sbSql.append("   AND A.CH_PER = E.USER_ID\n");
		sbSql.append("   AND A.VEHICLE_ID ="+vId+"\n"); 
		sbSql.append("  ORDER BY A.CH_DATE DESC"); 
		List<Map<String, Object>> list = dao.pageQuery(sbSql.toString(),null, getFunName());
		return list;
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 根据车辆ID获取退车历史记录
	 * @param      : vehicle_ids  车辆IDS
	 * @param      : 
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-6-15
	 */
	public List<Map<String, Object>>  getRecVehicleHisById(String vId){
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("select D.AREA_NAME,\n");
		sbSql.append("       C.ROAD_NAME,\n");
		sbSql.append("       B.SIT_NAME,\n");
		sbSql.append("       TO_CHAR(A.REC_DATE, 'YYYY-MM-DD HH24:MI:SS') REC_DATE,\n");
		sbSql.append("       E.NAME,\n");
		sbSql.append("       F.CODE_DESC\n");
		sbSql.append("  from TT_SALES_REC_PROCESS_HIS A,\n");
		sbSql.append("       TT_SALES_SIT     B,\n");
		sbSql.append("       TT_SALES_ROAD    C,\n");
		sbSql.append("       TT_SALES_AREA    D,\n");
		sbSql.append("       TC_USER          E,\n");
		sbSql.append("       TC_CODE          F\n");
		sbSql.append(" WHERE A.SIT_ID = B.SIT_ID\n");
		sbSql.append("   AND B.ROAD_ID = C.ROAD_ID\n");
		sbSql.append("   AND C.AREA_ID = D.AREA_ID\n");
		sbSql.append("   AND A.REC_PER = E.USER_ID\n");
		sbSql.append("   AND A.REC_TYPE = F.CODE_ID\n");
		sbSql.append("   AND A.VEHICLE_ID ="+vId+"\n"); 
		sbSql.append("  ORDER BY A.REC_DATE DESC"); 
		List<Map<String, Object>> list = dao.pageQuery(sbSql.toString(),null, getFunName());
		return list;
	}

}
