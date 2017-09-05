package com.infodms.yxdms.dao.afterSales;

import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.infodms.dms.common.Constant;
import com.infodms.dms.po.TtAsServiceAuthAuditPO;
import com.infodms.dms.po.TtAsServiceAuthContentPO;
import com.infodms.dms.po.TtAsServiceOrderPO;
import com.infodms.dms.po.TtAsServiceOutProjectPO;
import com.infodms.dms.po.TtAsServicePartPO;
import com.infodms.dms.po.TtAsServiceProjectPO;
import com.infodms.dms.po.TtAsServiceWarnDayPO;
import com.infodms.dms.po.TtAsServiceWarnNumPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infodms.yxdms.dao.IBaseDao;
import com.infoservice.po3.POFactory;
import com.infoservice.po3.POFactoryBuilder;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

public class ServiceOrderDao extends IBaseDao<PO>{

	private static final ServiceOrderDao dao = new ServiceOrderDao();
	public static final ServiceOrderDao getInstance(){
		if (dao == null) {
			return new ServiceOrderDao();
		}
		return dao;
	}
	
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
    
	/**
	 * 售后服务工单查询
	 * 
	 * @param parMap
	 * @return boolean
	 * @throws Exception
	 */
	public PageResult<Map<String, Object>> serviceOrderQuery(
			Map<String, Object> params, Integer pageSize, Integer curPage)
			throws Exception {
		StringBuffer sql = new StringBuffer();
		
		String serviceOrderCode = CommonUtils.checkNull(params.get("serviceOrderCode"));
		String licenseNo = CommonUtils.checkNull(params.get("licenseNo"));
		String vin = CommonUtils.checkNull(params.get("vin"));
		String repairType = CommonUtils.checkNull(params.get("repairType"));
		String status = CommonUtils.checkNull(params.get("status"));
		String createDateBegin = CommonUtils.checkNull(params.get("createDateBegin"));
		String createDateEnd = CommonUtils.checkNull(params.get("createDateEnd"));
		
//		String loginUserId = params.get("loginUserId").toString();
		String loginDealerId = CommonUtils.checkNull(params.get("loginDealerId"));
		
		sql.append( "SELECT A.SERVICE_ORDER_ID,--服务工单ID\n" +
					"       A.SERVICE_ORDER_CODE, --工单号\n" + 
					"       A.REPAIR_TYPE, --维修类型\n" + 
					"       F_GET_TC_CODE(A.REPAIR_TYPE) REPAIR_TYPE_NAME, --维修类型名称\n" + 
					"       B.LICENSE_NO, --车牌号\n" + 
					"       A.VIN, --VIN\n" + 
					"       C.MODEL_NAME,--车型名称\n" + 
					"       E.CTM_NAME, --车主\n" + 
					"       TO_CHAR(A.CREATE_DATE, 'YYYY-MM-DD') CREATE_DATE, --工单创建时间\n" + 
					"       A.MILEAGE, --进厂里程数\n" + 
					"       A.IS_CAN_CLAIM, --是否可以生成索赔单\n" + 
					"       F.ACTIVITY_TYPE, --服务活动类型\n" + 
					"       A.STATUS, --工单状态\n" + 
					"       F_GET_TC_CODE(A.STATUS) STATUS_NAME --工单状态名称\n" + 
					"  FROM TT_AS_SERVICE_ORDER A\n" + 
					"  LEFT JOIN TM_VEHICLE B ON A.VIN = B.VIN\n" + 
					"  LEFT JOIN (SELECT DISTINCT PACKAGE_ID, PACKAGE_NAME, MODEL_NAME, SERIES_NAME ,BRAND_NAME FROM VW_MATERIAL_GROUP_MAT) C ON B.PACKAGE_ID = C.PACKAGE_ID\n" + 
					"  LEFT JOIN TT_DEALER_ACTUAL_SALES D ON B.VEHICLE_ID = D.VEHICLE_ID\n" + 
					"  LEFT JOIN TT_CUSTOMER E ON D.CTM_ID = E.CTM_ID\n" + 
					"  LEFT JOIN TT_AS_WR_ACTIVITY F ON A.ACTIVITY_ID = F.ACTIVITY_ID\n" +
					" WHERE 1 = 1\n");
        
		if (!"".equals(serviceOrderCode)) {
			sql.append(" AND UPPER(A.SERVICE_ORDER_CODE) LIKE UPPER('%"+serviceOrderCode+"%') \n") ;  
		}
		if (!"".equals(licenseNo)) {
			sql.append(" AND UPPER(B.LICENSE_NO) LIKE UPPER('%"+licenseNo+"%') \n") ;  
		}
		if (!"".equals(vin)) {
			sql.append(" AND UPPER(A.VIN) LIKE UPPER('%"+vin+"%') \n") ;  
		}
		if (!"".equals(repairType)) {
			sql.append(" AND A.REPAIR_TYPE = "+repairType+" \n") ;  
		}
		if (!"".equals(status)) {
			sql.append(" AND A.STATUS = "+status+" \n") ;  
		}
		if (!"".equals(createDateBegin)) {
			sql.append(" AND A.CREATE_DATE >= TO_DATE('"+createDateBegin+"','YYYY-MM-DD') \n") ;  
		}
		if (!"".equals(createDateEnd)) {
			sql.append(" AND A.CREATE_DATE <= TO_DATE('"+createDateEnd+"','YYYY-MM-DD') \n") ;  
		}
		if (!"".equals(loginDealerId)) {
			sql.append(" AND A.DEALER_ID = "+loginDealerId+" \n"); 
		}else{
			sql.append(" AND A.DEALER_ID IS NULL \n"); 
		}
		
		sql.append(" ORDER BY A.CREATE_DATE DESC NULLS LAST ");
		
		System.out.println("sql:"+sql);
		
		return dao.pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
	}
	
	/**
	 * 获取经销商加价率
	 * 
	 * @param parMap
	 * @return boolean
	 * @throws Exception
	 */
	public Map<String, Object> getPartFareRate(Map<String, Object> params)
			throws Exception {
		StringBuffer sql = new StringBuffer();
		String loginDealerId = CommonUtils.checkNull(params.get("loginDealerId"));
		
		sql.append( "SELECT TDP.PARAMETER_VALUE / 100  PART_FARE_RATE\n" +
					"  FROM TM_DOWN_PARAMETER TDP\n" + 
					" WHERE TDP.PARAMETER_CODE = "+Constant.CLAIM_BASIC_PARAMETER_09+"\n" + 
					"   AND TDP.DEALER_ID = " + loginDealerId + "\n" + 
					"   AND ROWNUM <= 1");
		
//		Map<String,Object> partFareRateMap =  dao.pageQueryMap(sql.toString(), null, getFunName());
//		String partFareRate = CommonUtils.checkNull(partFareRateMap.get("PART_FARE_RATE"));
		
		return dao.pageQueryMap(sql.toString(), null, getFunName());
	}
	
	/**
	 * 查询车辆首保信息是否正常
	 * 
	 * @param vin
	 * @return boolean
	 * @throws Exception
	 */
	public Map<String, Object> getValidateMaintainInfo(Map<String, Object> params)
			throws Exception {
		StringBuffer sql = new StringBuffer();
		String vin = CommonUtils.checkNull(params.get("vin"));
		
		sql.append("SELECT A.VIN\n") ;
		sql.append("  FROM TM_VEHICLE A\n") ;
		sql.append("  JOIN TT_AS_SERVICE_ORDER B\n") ;
		sql.append("    ON A.VIN = B.VIN\n") ;
		sql.append(" WHERE NVL(A.FREE_TIMES, 0) = 0\n") ;
		sql.append("   AND B.REPAIR_TYPE = "+Constant.REPAIR_TYPE_04+"\n") ;
		sql.append("   AND B.STATUS <> "+Constant.SERVICE_ORDER_STATUS_09+"\n") ;
		
		if (!"".equals(vin)) {
			sql.append(" AND UPPER(A.VIN) = UPPER('"+vin+"') \n") ;  
		}else{
			sql.append(" AND 1=2 \n") ;  
		}
		
	    //只要取到一条即可
	    sql.append(" AND ROWNUM = 1 \n") ;
	    System.out.println("sql:"+sql);
		Map<String, Object> result  = dao.pageQueryMap(sql.toString(), null, getFunName());
		return result;
	}
	
	/**
	 * 查询数据库中是否已存在车辆信息数据
	 * 
	 * @param vin
	 * @return boolean
	 * @throws Exception
	 */
	public Map<String, Object> getVehicleInfo(Map<String, Object> params)
			throws Exception {
		StringBuffer sql = new StringBuffer();
		String vin = CommonUtils.checkNull(params.get("vin"));
//		String repairType = CommonUtils.checkNull(params.get("repairType"));
		String lifeCycle = CommonUtils.checkNull(params.get("lifeCycle"));
		String isPdi = CommonUtils.checkNull(params.get("isPdi"));
		
		sql.append( "SELECT A.VEHICLE_ID, --唯一标识\n" +
					"       A.VIN, --VIN\n" + 
					"       A.ENGINE_NO, --发动机号\n" + 
					"       A.LICENSE_NO, --车牌号\n" + 
					"       A.MILEAGE, --行驶里程数\n" + 
					"       B.BRAND_NAME, --品牌\n" + 
					"       B.SERIES_NAME, --车系名称\n" + 
					"       B.MODEL_ID,--车型ID\n" + 
					"       B.MODEL_NAME, --车型名称\n" + 
					"       H.WRGROUP_ID,--车型组ID\n" + 
					"       A.AREA_ID, --产地\n" + 
					"       C.AREA_NAME, --产地名称\n" + 
					"       TO_CHAR(A.PURCHASED_DATE,'YYYY-MM-DD') PURCHASED_DATE, --购车日期\n" + 
					"       TO_CHAR(A.PRODUCT_DATE,'YYYY-MM-DD') PRODUCT_DATE, --生产日期\n" + 
					"       E.CTM_NAME,--车主姓名\n" + 
					"       F.RULE_ID,--三包规则ID\n" + 
					"       G.RULE_NAME,--三包规则名称\n" + 
					"       B.PACKAGE_NAME, --配置\n" + 
					"       A.LIFE_CYCLE,--车辆状态\n" + 
					"       F_GET_TC_CODE(A.LIFE_CYCLE) LIFE_CYCLE,--车辆状态名称\n" + 
					"       NVL(A.FREE_TIMES,0) + 1 CUR_FREE_TIMES, --当前保养次数,等于历史保养次数加1\n" + 
					"       TO_CHAR(NVL(I.FREE,0),'FM999990.00') FREE, --首保费用\n" + 
					"       NVL(I.NEW_CAR_FEE,0) NEW_CAR_FEE --PDI金额\n" + 
					"  FROM TM_VEHICLE A\n" + 
					"  LEFT JOIN (SELECT DISTINCT PACKAGE_ID, PACKAGE_NAME, MODEL_ID, MODEL_NAME, SERIES_NAME ,BRAND_NAME FROM VW_MATERIAL_GROUP_MAT) B ON A.PACKAGE_ID = B.PACKAGE_ID\n" + 
					"  LEFT JOIN TM_BUSINESS_AREA C ON A.AREA_ID = C.AREA_ID\n" + 
					"  LEFT JOIN TT_DEALER_ACTUAL_SALES D ON A.VEHICLE_ID = D.VEHICLE_ID AND D.IS_RETURN = "+Constant.IF_TYPE_NO+"\n" + 
					"  LEFT JOIN TT_CUSTOMER E ON D.CTM_ID = E.CTM_ID\n" + 
					"  LEFT JOIN TT_AS_WR_GAME F  ON  A.CLAIM_TACTICS_ID = F.ID\n" + 
					"  LEFT JOIN TT_AS_WR_RULE G ON F.RULE_ID = G.ID\n" + 
					"  LEFT JOIN TT_AS_WR_MODEL_ITEM H ON A.PACKAGE_ID = H.MODEL_ID\n" + 
					"  LEFT JOIN TT_AS_WR_MODEL_GROUP I ON H.WRGROUP_ID = I.WRGROUP_ID --索赔车型组\n" + 
					" WHERE 1=1 \n");
		
		if (!"".equals(vin)) {
			sql.append(" AND UPPER(A.VIN) = UPPER('"+vin+"') \n") ;  
		}else{
			sql.append(" AND 1=2 \n") ;  
		}
		if (!"".equals(lifeCycle)) {
			sql.append(" AND A.LIFE_CYCLE = "+lifeCycle+" \n") ; 
		}
		if (!"".equals(isPdi)) {
			sql.append(" AND A.IS_PDI = "+isPdi+" \n") ;  
		}
		
	    //只要取到一条即可
	    sql.append(" AND ROWNUM = 1 \n") ;
	    System.out.println("sql:"+sql);
		Map<String, Object> result  = dao.pageQueryMap(sql.toString(), null, getFunName());
		return result;
	}
	
	/**
	 * 售后服务-服务活动查询
	 * 
	 * @param parMap
	 * @return boolean
	 * @throws Exception
	 */
	public PageResult<Map<String, Object>> serviceActivityQuery(
			Map<String, Object> params, Integer pageSize, Integer curPage)
			throws Exception {
		StringBuffer sql = new StringBuffer();
		
		String vin = CommonUtils.checkNull(params.get("vin"));//vin
		String mileage = CommonUtils.checkNull(params.get("mileage"));//进站里程数
		String purchasedDate = CommonUtils.checkNull(params.get("purchasedDate"));//购车时间
		String wrgroupId = CommonUtils.checkNull(params.get("wrgroupId"));//车型组
//		String loginUserId = params.get("loginUserId").toString();
		
		String activityCode = CommonUtils.checkNull(params.get("activityCode"));//活动编码
		String activityName = CommonUtils.checkNull(params.get("activityName"));//活动名称
		String activityType = CommonUtils.checkNull(params.get("activityType"));//活动类型
		
		String loginDealerId = CommonUtils.checkNull(params.get("loginDealerId"));
		
		if(loginDealerId.equals("")) loginDealerId = "-1";
		
		sql.append("SELECT A.ACTIVITY_ID, --活动ID\n") ;
		sql.append("       A.ACTIVITY_NAME, --活动名称\n") ;
		sql.append("       A.ACTIVITY_CODE, --活动编码\n") ;
		sql.append("       A.ACTIVITY_TYPE, --活动类型\n") ;
		sql.append("       F_GET_TC_CODE(A.ACTIVITY_TYPE) ACTIVITY_TYPE_NAME, --活动类型名称\n") ;
		sql.append("       TO_CHAR(A.ACTIVITY_DISCOUNT/100,'FM999990.00') ACTIVITY_DISCOUNT, --折扣率\n") ;
		sql.append("       DECODE(A.ACTIVITY_TYPE,\n") ;
		sql.append("              "+Constant.SERVICEACTIVITY_TYPE_NEW_01+",--技术升级\n") ;
		sql.append("              '0.00',\n") ;
		sql.append("              "+Constant.SERVICEACTIVITY_TYPE_NEW_02+",--送保养\n") ;
		sql.append("              TO_CHAR(A.MAINTAIN_MONEY, 'FM999990.00'),\n") ;
		sql.append("              "+Constant.SERVICEACTIVITY_TYPE_NEW_03+",--送检测\n") ;
		sql.append("              TO_CHAR(A.DETECTION_MONEY, 'FM999990.00')\n") ;
		sql.append("              ) ACTIVITY_MONEY, --服务活动金额\n") ;
		sql.append("       DECODE(A.ACTIVITY_TYPE,\n") ;
		sql.append("              "+Constant.SERVICEACTIVITY_TYPE_NEW_01+",\n") ;
		sql.append("              1, --技术升级\n") ;
		sql.append("              "+Constant.SERVICEACTIVITY_TYPE_NEW_02+",\n") ;
		sql.append("              MAINTAIN_NUMBER, --送保养\n") ;
		sql.append("              "+Constant.SERVICEACTIVITY_TYPE_NEW_03+",\n") ;
		sql.append("              1 --送检测\n") ;
		sql.append("              ) ACTIVITY_NUM, --服务活动次数\n") ;
		sql.append("       A.FAULT_DESCRIPTION FAULT_DESC, --故障描述\n") ;
		sql.append("       A.FAULT_PROBLEM FAULT_REASON, --故障原因\n") ;
		sql.append("       A.MAINTENANCE_MEASURES REPAIR_METHOD --维修措施\n") ;
		sql.append("  FROM TT_AS_WR_ACTIVITY A --服务活动管理\n") ;
		sql.append("  LEFT JOIN TT_AS_WR_ACTIVITY_DEALER B --活动经销商表\n") ;
		sql.append("    ON A.ACTIVITY_ID = B.ACTIVITY_ID\n") ;
		sql.append("  JOIN TT_AS_WR_ACTIVITY_VIN C --活动VIN\n") ;
		sql.append("    ON A.ACTIVITY_ID = C.ACTIVITY_ID\n") ;
		sql.append("  LEFT JOIN (SELECT ACTIVITY_ID, COUNT(1) ACTIVITY_NUM\n") ;
		sql.append("               FROM TT_AS_SERVICE_ORDER\n") ;
		sql.append("              WHERE 1 = 1\n") ;
		sql.append("                AND STATUS <> "+Constant.SERVICE_ORDER_STATUS_09+" --工单状态不为废弃\n") ;
		sql.append("                AND REPAIR_TYPE = "+Constant.REPAIR_TYPE_05+" --服务活动\n") ;
		sql.append("                AND ACTIVITY_ID IS NOT NULL\n") ;
		sql.append("                AND VIN = '"+vin+"'\n") ;
		sql.append("              GROUP BY ACTIVITY_ID) D\n") ;
		sql.append("    ON A.ACTIVITY_ID = D.ACTIVITY_ID\n") ;
		sql.append(" WHERE A.ACTIVITY_STRATE_DATE <= TRUNC(SYSDATE) --有效开始时间\n") ;
		sql.append("   AND A.ACTIVITY_END_DATE >= TRUNC(SYSDATE) --有效结束时间\n") ;
		sql.append("   AND A.ACTIVITY_STATUS = "+Constant.SERVICEACTIVITY_STATUS_NEW_02+" --已经发布\n") ;
		sql.append("   AND NVL(B.DEALER_ID, "+loginDealerId+") = "+loginDealerId+" --活动下发经销商为空或者匹配当前经销商\n") ;
		sql.append("   AND DECODE(A.ACTIVITY_TYPE,\n") ;
		sql.append("              "+Constant.SERVICEACTIVITY_TYPE_NEW_01+",\n") ;
		sql.append("              1, --技术升级\n") ;
		sql.append("              "+Constant.SERVICEACTIVITY_TYPE_NEW_02+",\n") ;
		sql.append("              MAINTAIN_NUMBER, --送保养\n") ;
		sql.append("              "+Constant.SERVICEACTIVITY_TYPE_NEW_03+",\n") ;
		sql.append("              1 --送检测\n") ;
		sql.append("              ) > NVL(D.ACTIVITY_NUM, 0)\n") ;
		//活动编码
		if(!activityCode.equals("")){
			sql.append("   AND A.ACTIVITY_CODE = "+activityCode+" --活动编码\n") ;
		}
		//活动类型
		if(!activityName.equals("")){
			sql.append("   AND A.ACTIVITY_NAME = "+activityName+" --活动名称\n") ;
		}
		//活动类型
		if(!activityType.equals("")){
			sql.append("   AND A.ACTIVITY_TYPE IN ("+activityType+") --指定活动类型\n") ;
		}
		//vin
		if(!vin.equals("")){
			sql.append("   AND C.VIN = '"+vin+"' --指定VIN\n") ;
		}else{
			sql.append("   AND 1=2\n") ;
		}
		sql.append("UNION\n") ;
		sql.append("SELECT A.ACTIVITY_ID, --活动ID\n") ;
		sql.append("       A.ACTIVITY_NAME, --活动名称\n") ;
		sql.append("       A.ACTIVITY_CODE, --活动编码\n") ;
		sql.append("       A.ACTIVITY_TYPE, --活动类型\n") ;
		sql.append("       F_GET_TC_CODE(A.ACTIVITY_TYPE) ACTIVITY_TYPE_NAME, --活动类型名称\n") ;
		sql.append("       TO_CHAR(A.ACTIVITY_DISCOUNT/100,'FM999990.00') ACTIVITY_DISCOUNT, --折扣率\n") ;
		sql.append("       DECODE(A.ACTIVITY_TYPE,\n") ;
		sql.append("              "+Constant.SERVICEACTIVITY_TYPE_NEW_01+",\n") ;
		sql.append("              '0.00', --技术升级\n") ;
		sql.append("              "+Constant.SERVICEACTIVITY_TYPE_NEW_02+",\n") ;
		sql.append("              TO_CHAR(A.MAINTAIN_MONEY, 'FM999990.00'), --送保养\n") ;
		sql.append("              "+Constant.SERVICEACTIVITY_TYPE_NEW_03+",\n") ;
		sql.append("              TO_CHAR(A.DETECTION_MONEY, 'FM999990.00') --送检测\n") ;
		sql.append("              ) ACTIVITY_MONEY, --服务活动金额\n") ;
		sql.append("       DECODE(A.ACTIVITY_TYPE,\n") ;
		sql.append("              "+Constant.SERVICEACTIVITY_TYPE_NEW_01+",\n") ;
		sql.append("              1, --技术升级\n") ;
		sql.append("              "+Constant.SERVICEACTIVITY_TYPE_NEW_02+",\n") ;
		sql.append("              MAINTAIN_NUMBER, --送保养\n") ;
		sql.append("              "+Constant.SERVICEACTIVITY_TYPE_NEW_03+",\n") ;
		sql.append("              1 --送检测\n") ;
		sql.append("              ) ACTIVITY_NUM, --服务活动次数\n") ;
		sql.append("       A.FAULT_DESCRIPTION FAULT_DESC, --故障描述\n") ;
		sql.append("       A.FAULT_PROBLEM FAULT_REASON, --故障原因\n") ;
		sql.append("       A.MAINTENANCE_MEASURES REPAIR_METHOD --维修措施\n") ;
		sql.append("  FROM TT_AS_WR_ACTIVITY A --服务活动管理\n") ;
		sql.append("  LEFT JOIN TT_AS_WR_ACTIVITY_DEALER B --活动经销商表\n") ;
		sql.append("    ON A.ACTIVITY_ID = B.ACTIVITY_ID\n") ;
		sql.append("  LEFT JOIN TT_AS_WR_ACTIVITY_MODEL C --服务活动车型设置\n") ;
		sql.append("    ON A.ACTIVITY_ID = C.ACTIVITY_ID\n") ;
		sql.append("  LEFT JOIN (SELECT ACTIVITY_ID, COUNT(1) ACTIVITY_NUM\n") ;
		sql.append("               FROM TT_AS_SERVICE_ORDER\n") ;
		sql.append("              WHERE 1 = 1\n") ;
		sql.append("                AND STATUS <> "+Constant.SERVICE_ORDER_STATUS_09+" --工单状态不为废弃\n") ;
		sql.append("                AND REPAIR_TYPE = "+Constant.REPAIR_TYPE_05+" --服务活动\n") ;
		sql.append("                AND ACTIVITY_ID IS NOT NULL\n") ;
		sql.append("                AND VIN = '"+vin+"'\n") ;
		sql.append("              GROUP BY ACTIVITY_ID) D\n") ;
		sql.append("    ON A.ACTIVITY_ID = D.ACTIVITY_ID\n") ;
		sql.append(" WHERE A.ACTIVITY_STRATE_DATE <= TRUNC(SYSDATE) --有效开始时间\n") ;
		sql.append("   AND A.ACTIVITY_END_DATE >= TRUNC(SYSDATE) --有效结束时间\n") ;
		sql.append("   AND A.ACTIVITY_STATUS = "+Constant.SERVICEACTIVITY_STATUS_NEW_02+" --已经发布\n") ;
		sql.append("   AND DECODE(A.ACTIVITY_TYPE,\n") ;
		sql.append("              "+Constant.SERVICEACTIVITY_TYPE_NEW_01+",\n") ;
		sql.append("              1, --技术升级\n") ;
		sql.append("              "+Constant.SERVICEACTIVITY_TYPE_NEW_02+",\n") ;
		sql.append("              MAINTAIN_NUMBER, --送保养\n") ;
		sql.append("              "+Constant.SERVICEACTIVITY_TYPE_NEW_03+",\n") ;
		sql.append("              1 --送检测\n") ;
		sql.append("              ) > NVL(D.ACTIVITY_NUM, 0)\n") ;
		//进站里程数
		if(!mileage.equals("")){
			sql.append("   AND NVL(A.ACTIVITY_STRATE_MILEAGE,"+mileage+") <= "+mileage+" --活动开始里程为空或者小于进站里程\n") ;
			sql.append("   AND NVL(A.ACTIVITY_END_MILEAGE,"+mileage+") >= "+mileage+" --活动结束里程为空或者大于进站里程\n") ;
		}else{
			sql.append("   AND 1=2 --进站里程不能为空\n") ;
		}
		//购车日期
		if(!purchasedDate.equals("")){
			sql.append("   AND NVL(TRUNC(A.ACTIVITY_SALES_STRATE_DATE), TRUNC(TO_DATE('"+purchasedDate+"','YYYY-MM-DD'))) <= TRUNC(TO_DATE('"+purchasedDate+"','YYYY-MM-DD')) --活动实销开始时间为空或者小于购车日期\n") ;
			sql.append("   AND NVL(TRUNC(A.ACTIVITY_SALES_END_DATE), TRUNC(TO_DATE('"+purchasedDate+"','YYYY-MM-DD'))) >= TRUNC(TO_DATE('"+purchasedDate+"','YYYY-MM-DD')) --活动实销结束时间为空或者大于购车日期\n") ;
		}
		sql.append("   AND NVL(B.DEALER_ID, "+loginDealerId+") = "+loginDealerId+" --活动下发经销商为空或者匹配当前经销商\n") ;
		//活动编码
		if(!activityCode.equals("")){
			sql.append("   AND A.ACTIVITY_CODE = "+activityCode+" --活动编码\n") ;
		}
		//活动类型
		if(!activityName.equals("")){
			sql.append("   AND A.ACTIVITY_NAME = "+activityName+" --活动名称\n") ;
		}
		//活动类型
		if(!activityType.equals("")){
			sql.append("   AND A.ACTIVITY_TYPE IN ("+activityType+") --指定活动类型\n") ;
		}
		if(!wrgroupId.equals("")){
			sql.append("   AND NVL(C.MODE_ID, "+wrgroupId+") = "+wrgroupId+" --指定车型组\n") ;
		}else{
			sql.append("   AND 1=2\n") ;
		}
//		sql.append(" ORDER BY A.CREATE_DATE DESC NULLS LAST ");
		
		System.out.println("sql:"+sql);
		
		return dao.pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
	}
	
	/**
	 * 售后服务-维修项目查询
	 * 
	 * @param parMap
	 * @return boolean
	 * @throws Exception
	 */
	public PageResult<Map<String, Object>> serviceProjectQuery(
			Map<String, Object> params, Integer pageSize, Integer curPage)
			throws Exception {
		StringBuffer sql = new StringBuffer();
		
		String wrgroupId = CommonUtils.checkNull(params.get("wrgroupId"));
		String labourCode = CommonUtils.checkNull(params.get("labourCode"));
		String cnDes = CommonUtils.checkNull(params.get("cnDes"));
		
//		String loginUserId = params.get("loginUserId").toString();
		String loginDealerId = CommonUtils.checkNull(params.get("loginDealerId"));
		
		if(loginDealerId.equals("")) loginDealerId = "-1";
		
		sql.append( "SELECT A.ID LABOUR_ID,--工时ID\n" +
					"       B.WRGROUP_NAME, --车型组名称\n" + 
					"       A.LABOUR_CODE, --工时代码\n" + 
					"       A.CN_DES, --工时名称\n" + 
					"       TO_CHAR(A.LABOUR_QUOTIETY, 'FM999999') LABOUR_QUOTIETY, --工时数\n" + 
					"       TO_CHAR(A.LABOUR_HOUR, 'FM999990.00') LABOUR_HOUR, --工时定额\n" + 
					"       NVL(C.LABOUR_PRICE, 0) LABOUR_PRICE --工时单价\n" + 
					"  FROM TT_AS_WR_WRLABINFO A\n" + 
					"  LEFT JOIN TT_AS_WR_MODEL_GROUP B ON A.WRGROUP_ID = B.WRGROUP_ID\n" + 
					"  LEFT JOIN (SELECT TALPI.GROUP_ID,\n" + 
					"                    TALPI.LABOUR_ID,\n" + 
					"                    SUM(TALPI.CHANGE_LABOUR_PRICE) LABOUR_PRICE\n" + 
					"               FROM TT_AS_LABOUR_PRICE_INFO TALPI\n" + 
					"              WHERE TALPI.DEALER_ID = "+loginDealerId+"\n" + 
					"                AND TALPI.POLICY_DATE >= TRUNC(SYSDATE)\n" + 
					"              GROUP BY TALPI.GROUP_ID, TALPI.LABOUR_ID) C ON A.WRGROUP_ID = C.GROUP_ID\n" + 
					"   AND A.ID = C.LABOUR_ID\n" + 
					" WHERE 1=1\n" + 
					"   AND A.IS_DEL = 0\n");
        
		if (!"".equals(wrgroupId)) {
			sql.append(" AND A.WRGROUP_ID = "+wrgroupId+" \n") ;  
		}else{
			sql.append(" AND 1=2 \n") ;  
		}
		if (!"".equals(labourCode)) {
			sql.append(" AND UPPER(A.LABOUR_CODE) LIKE UPPER('%"+labourCode+"%') \n") ;  
		}
		if (!"".equals(cnDes)) {
			sql.append(" AND UPPER(A.CN_DES) LIKE UPPER('%"+cnDes+"%') \n") ;  
		}
		sql.append(" ORDER BY A.CREATE_DATE DESC NULLS LAST ");
		
		System.out.println("sql:"+sql);
		
		return dao.pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
	}
	
	/**
	 * 售后服务-服务活动维修项目查询
	 * 
	 * @param parMap
	 * @return boolean
	 * @throws Exception
	 */
	public List<Map<String, Object>> serviceActivityProjectQuery(Map<String, Object> params)
			throws Exception {
		StringBuffer sql = new StringBuffer();
		
		String activityId = CommonUtils.checkNull(params.get("activityId"));//服务活动ID
		String wrgroupId = CommonUtils.checkNull(params.get("wrgroupId"));
		
//		String loginUserId = params.get("loginUserId").toString();
		String loginDealerId = CommonUtils.checkNull(params.get("loginDealerId"));
		
		if(loginDealerId.equals("")) loginDealerId = "-1";
		
		sql.append( "SELECT A.ID LABOUR_ID,--工时ID\n" +
					"       B.WRGROUP_NAME, --车型组名称\n" + 
					"       A.LABOUR_CODE, --工时代码\n" + 
					"       A.CN_DES, --工时名称\n" + 
					"       TO_CHAR(A.LABOUR_QUOTIETY, 'FM999999') LABOUR_QUOTIETY, --工时数\n" + 
					"       TO_CHAR(A.LABOUR_HOUR, 'FM999990.00') LABOUR_HOUR, --工时定额\n" + 
					"       NVL(C.LABOUR_PRICE, 0) LABOUR_PRICE --工时单价\n" + 
					"  FROM TT_AS_WR_WRLABINFO A\n" + 
					"  LEFT JOIN TT_AS_WR_MODEL_GROUP B ON A.WRGROUP_ID = B.WRGROUP_ID\n" + 
					"  LEFT JOIN (SELECT TALPI.GROUP_ID,\n" + 
					"                    TALPI.LABOUR_ID,\n" + 
					"                    SUM(TALPI.CHANGE_LABOUR_PRICE) LABOUR_PRICE\n" + 
					"               FROM TT_AS_LABOUR_PRICE_INFO TALPI\n" + 
					"              WHERE TALPI.DEALER_ID = "+loginDealerId+"\n" + 
					"                AND TALPI.POLICY_DATE >= TRUNC(SYSDATE)\n" + 
					"              GROUP BY TALPI.GROUP_ID, TALPI.LABOUR_ID) C ON A.WRGROUP_ID = C.GROUP_ID\n" + 
					"   AND A.ID = C.LABOUR_ID\n" + 
					"   JOIN TT_AS_WR_ACTIVITY_HOURS D ON A.LABOUR_CODE = D.HOURS_CODE \n" +
					" WHERE 1=1\n" + 
					"   AND A.IS_DEL = 0\n");
		
		if (!"".equals(activityId)) {
			sql.append(" AND D.ACTIVITY_ID = "+activityId+" \n") ;  
		}else{
			sql.append(" AND 1=2 \n") ;  
		}
		if (!"".equals(wrgroupId)) {
			sql.append(" AND A.WRGROUP_ID = "+wrgroupId+" \n") ;  
		}else{
			sql.append(" AND 1=2 \n") ;  
		}
		
		sql.append(" ORDER BY A.CREATE_DATE DESC NULLS LAST ");
		
		System.out.println("sql:"+sql);
		List<Map<String,Object>> result  = dao.pageQuery(sql.toString(), null, getFunName());
		return result;
	}
	
	/**
	 * 售后服务-维修配件查询
	 * 
	 * @param parMap
	 * @return boolean
	 * @throws Exception
	 */
	public PageResult<Map<String, Object>> servicePartQuery(
			Map<String, Object> params, Integer pageSize, Integer curPage)
			throws Exception {
		StringBuffer sql = new StringBuffer();
		
		String repairType = CommonUtils.checkNull(params.get("repairType"));
		String ruleId = CommonUtils.checkNull(params.get("ruleId"));
//		String modelId = CommonUtils.checkNull(params.get("modelId"));
		String mileage = CommonUtils.checkNull(params.get("mileage"));
		String arrivalDate = CommonUtils.checkNull(params.get("arrivalDate"));
		String repairDateBegin = CommonUtils.checkNull(params.get("repairDateBegin"));//维修开始时间
		String vin = CommonUtils.checkNull(params.get("vin"));//vin
		String purchasedDate = CommonUtils.checkNull(params.get("purchasedDate"));
		String partCode = CommonUtils.checkNull(params.get("partCode"));
		String partCname = CommonUtils.checkNull(params.get("partCname"));
		String partFareRate = CommonUtils.checkNull(params.get("partFareRate"));
		String _isThreeGuarantee = CommonUtils.checkNull(params.get("_isThreeGuarantee"));
		
//		String loginUserId = params.get("loginUserId").toString();
		String loginDealerId = CommonUtils.checkNull(params.get("loginDealerId"));
		String str = "";
		
		if(loginDealerId.equals("")) loginDealerId = "-1";
		if(!ruleId.equals("")){
			str = " AND C.RULE_ID = " + ruleId;
		}else{
			str = " AND 1=2 ";
		}
		if(repairType.equals(Constant.REPAIR_TYPE_09.toString())){//备件维修
			sql.append( "SELECT NVL(CASE WHEN ADD_MONTHS(C.REPAIR_DATE_BEGIN,A.WR_MONTHS) \n" + 
				    "                                > TRUNC(TO_DATE('"+repairDateBegin+"','YYYY-MM-DD HH24:MI:SS'))  --上次维修开始时间+三包期大于本次维修开始时间，精确到天  \n" +
					"            AND A.WR_MILEAGE >= ("+mileage+"-C.MILEAGE) --三包里程大于本次进站里程-上次进站里程\n" +
					"            THEN "+Constant.PART_BASE_FLAG_YES+" \n" + 
					"            ELSE "+Constant.PART_BASE_FLAG_NO+" END,"+Constant.PART_BASE_FLAG_NO+") IS_THREE_GUARANTEES,--是否三包\n" + 
					"       A.PART_ID,--配件ID\n" + 
					"       A.PART_CODE,--配件代码\n" + 
					"       A.PART_NAME PART_CNAME,--配件名称\n" + 
					"       A.PART_WAR_TYPE,--配件三包类型\n" +
					"       B.SALE_PRICE1,--配件实际调拨价\n" + 
					"       "+partFareRate+" PART_FARE_RATE,--经销商加价率\n" + 
					"       ROUND(B.SALE_PRICE1 * (1 + ("+partFareRate+")),2) PART_PRICE, --配件单价\n" + 
					"       C.PART_NUM PART_NUM_MAX --配件最大数量\n" + 
					"  FROM TM_PT_PART_BASE A --配件使用基础信息表\n" + //TT_PART_DEFINE配件新增基础信息表
					"  LEFT JOIN TT_PART_SALES_PRICE B ON A.PART_ID = B.PART_ID\n" + 
					"  JOIN (SELECT TASP.SERVICE_PART_ID,\n" +
					"               TASO.VIN,\n" + 
					"               TASO.REPAIR_DATE_BEGIN,\n" + 
					"               TASO.MILEAGE,\n" + 
					"               TASP.PART_ID,\n" + 
					"               TASP.PART_CODE,\n" + 
					"               TASP.PART_NUM\n" + 
					"          FROM TT_AS_SERVICE_ORDER TASO\n" + 
					"          JOIN TT_AS_SERVICE_PART TASP\n" + 
					"            ON TASO.SERVICE_ORDER_ID = TASP.SERVICE_ORDER_ID\n" + 
					"           AND TASP.PART_PAYMENT_METHOD = 11801001 --必须是自费\n" + 
					"           AND TASP.PART_USE_TYPE = 80321002 --必须是更换\n" + 
					"         WHERE 1 = 1\n" + 
					"           AND TASO.REPAIR_DATE_BEGIN =\n" + 
					"               (SELECT MAX(TASO1.REPAIR_DATE_BEGIN)\n" + 
					"                  FROM TT_AS_SERVICE_ORDER TASO1\n" + 
					"                  JOIN TT_AS_SERVICE_PART TASP1\n" + 
					"                    ON TASO1.SERVICE_ORDER_ID = TASP1.SERVICE_ORDER_ID\n" + 
					"                 WHERE TASO.VIN = TASO1.VIN\n" + 
					"                   AND TASP.PART_ID = TASP1.PART_ID\n" + 
					"                   AND TASP1.PART_PAYMENT_METHOD = 11801001 --必须是自费\n" + 
					"                   AND TASP1.PART_USE_TYPE = 80321002 --必须是更换\n" + 
					"                )) C ON A.PART_ID = C.PART_ID\n" + //TM_PT_PART_BASE
					" WHERE 1=1\n");
					if (!"".equals(_isThreeGuarantee)) {
						sql.append( "   AND NVL(CASE WHEN ADD_MONTHS(C.REPAIR_DATE_BEGIN,A.WR_MONTHS) \n" + 
								    "                                > TRUNC(TO_DATE('"+repairDateBegin+"','YYYY-MM-DD HH24:MI:SS'))  --上次维修开始时间+三包期大于本次维修开始时间，精确到天  \n" +
									"            AND A.WR_MILEAGE >= ("+mileage+"-C.MILEAGE) --三包里程大于本次进站里程-上次进站里程\n" +
									"            THEN "+Constant.PART_BASE_FLAG_YES+" \n" + 
									"            ELSE "+Constant.PART_BASE_FLAG_NO+" END,"+Constant.PART_BASE_FLAG_NO+") = " + _isThreeGuarantee + "\n") ;  
					}
					if(!vin.equals("")){
						sql.append( "   AND C.VIN = '"+vin+"'\n");
					}else{
						sql.append( "   AND 1=2\n");
					}
		}else{//非备件维修
			sql.append( "SELECT NVL(CASE WHEN ADD_MONTHS(TO_DATE('"+purchasedDate+"','YYYY-MM-DD'),C.CLAIM_MONTH) \n" + 
				    "                                > TRUNC(TO_DATE('"+arrivalDate+"','YYYY-MM-DD HH24:MI:SS'))  --购车时间+三包期大于进厂日期，精确到天  \n" +
					"            AND C.CLAIM_MELIEAGE >= "+mileage+" --三包里程大于进厂里程\n" +
					"            THEN "+Constant.PART_BASE_FLAG_YES+" \n" + 
					"            ELSE "+Constant.PART_BASE_FLAG_NO+" END,"+Constant.PART_BASE_FLAG_NO+") IS_THREE_GUARANTEES,--是否三包\n" + 
					"       A.PART_ID,--配件ID\n" + 
					"       A.PART_CODE,--配件代码\n" + 
					"       A.PART_NAME PART_CNAME,--配件名称\n" + 
					"       A.PART_WAR_TYPE,--配件三包类型\n" +
					"       B.SALE_PRICE1,--配件实际调拨价\n" + 
					"       "+partFareRate+" PART_FARE_RATE,--经销商加价率\n" + 
					"       ROUND(B.SALE_PRICE1 * (1 + ("+partFareRate+")),2) PART_PRICE, --配件单价\n" + 
					"       999 PART_NUM_MAX --配件最大数量\n" + 
					"  FROM TM_PT_PART_BASE A --配件使用基础信息表\n" + //TT_PART_DEFINE配件新增基础信息表
					"  LEFT JOIN TT_PART_SALES_PRICE B ON A.PART_ID = B.PART_ID\n" + 
					"  LEFT JOIN TT_AS_WR_RULE_LIST C ON A.PART_CODE = C.PART_CODE " + str +"\n" + //TM_PT_PART_BASE
					" WHERE 1=1\n");
			if (!"".equals(_isThreeGuarantee)) {
				sql.append(" AND NVL(CASE WHEN ADD_MONTHS(TO_DATE('"+purchasedDate+"','YYYY-MM-DD'),C.CLAIM_MONTH) \n" + 
						   "                            > TRUNC(TO_DATE('"+arrivalDate+"','YYYY-MM-DD HH24:MI:SS'))  --购车时间+三包期大于进厂日期，精确到天  \n" +
						   "               AND C.CLAIM_MELIEAGE >= "+mileage+" --三包里程大于进厂里程\n" +
						   "               THEN "+Constant.PART_BASE_FLAG_YES+" \n" + 
						   "               ELSE "+Constant.PART_BASE_FLAG_NO+" END,"+Constant.PART_BASE_FLAG_NO+") = " + _isThreeGuarantee + "\n") ;  
			}
		}
		
        
//		if (!"".equals(modelId)) {
//			sql.append(" AND A.MODEL_ID like '%"+modelId+"%' \n") ;  
//		}else{
//			sql.append(" AND 1=2 \n") ;  
//		}
		if (!"".equals(partCode)) {
			sql.append(" AND UPPER(A.PART_CODE) LIKE UPPER('%"+partCode+"%') \n") ;  
		}
		if (!"".equals(partCname)) {
			sql.append(" AND UPPER(A.PART_NAME) LIKE UPPER('%"+partCname+"%') \n") ;  
		}
		
		sql.append(" ORDER BY A.CREATE_DATE DESC NULLS LAST ");
		
		System.out.println("sql:"+sql);
		
		return dao.pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
	}
	
	/**
	 * 售后服务-服务活动维修配件查询
	 * 
	 * @param parMap
	 * @return boolean
	 * @throws Exception
	 */
	public List<Map<String, Object>> serviceActivityPartQuery(Map<String, Object> params)
			throws Exception {
		StringBuffer sql = new StringBuffer();
		
		String activityId = CommonUtils.checkNull(params.get("activityId"));//服务活动ID
		String wrgroupId = CommonUtils.checkNull(params.get("wrgroupId"));//车型组ID
		
		String repairType = CommonUtils.checkNull(params.get("repairType"));
		String ruleId = CommonUtils.checkNull(params.get("ruleId"));
//		String modelId = CommonUtils.checkNull(params.get("modelId"));
		String mileage = CommonUtils.checkNull(params.get("mileage"));
		String arrivalDate = CommonUtils.checkNull(params.get("arrivalDate"));
//		String repairDateBegin = CommonUtils.checkNull(params.get("repairDateBegin"));//维修开始时间
//		String vin = CommonUtils.checkNull(params.get("vin"));//vin
		String purchasedDate = CommonUtils.checkNull(params.get("purchasedDate"));
//		String partCode = CommonUtils.checkNull(params.get("partCode"));
//		String partCname = CommonUtils.checkNull(params.get("partCname"));
		String partFareRate = CommonUtils.checkNull(params.get("partFareRate"));
//		String _isThreeGuarantee = CommonUtils.checkNull(params.get("_isThreeGuarantee"));
		
//		String loginUserId = params.get("loginUserId").toString();
		String loginDealerId = CommonUtils.checkNull(params.get("loginDealerId"));
		String str = "";
		
		if(loginDealerId.equals("")) loginDealerId = "-1";
		if(!ruleId.equals("")){
			str = " AND C.RULE_ID = " + ruleId;
		}else{
			str = " AND 1=2 ";
		}
		
		sql.append( "SELECT NVL(CASE WHEN ADD_MONTHS(TO_DATE('"+purchasedDate+"','YYYY-MM-DD'),C.CLAIM_MONTH) \n" + 
			    "                                > TRUNC(TO_DATE('"+arrivalDate+"','YYYY-MM-DD HH24:MI:SS'))  --购车时间+三包期大于进厂日期，精确到天  \n" +
				"            AND C.CLAIM_MELIEAGE >= "+mileage+" --三包里程大于进厂里程\n" +
				"            THEN "+Constant.PART_BASE_FLAG_YES+" \n" + 
				"            ELSE "+Constant.PART_BASE_FLAG_NO+" END,"+Constant.PART_BASE_FLAG_NO+") IS_THREE_GUARANTEES,--是否三包\n" + 
				"       A.PART_ID,--配件ID\n" + 
				"       A.PART_CODE,--配件代码\n" + 
				"       A.PART_NAME PART_CNAME,--配件名称\n" + 
				"       A.PART_WAR_TYPE,--配件三包类型\n" +
				"       D.APPLY_PART_COUNT PART_NUM,--配件数量\n" +
				"       B.SALE_PRICE1,--配件实际调拨价\n" + 
				"       "+partFareRate+" PART_FARE_RATE,--经销商加价率\n" + 
				"       ROUND(B.SALE_PRICE1 * (1 + ("+partFareRate+")),2) PART_PRICE, --配件单价\n" + 
//				"       D.APPLY_PART_COUNT*ROUND(B.SALE_PRICE1 * (1 + (0.15)),2) PART_PRICE_TOTAL,--配件金额\n" +
				"       D.PART_USE_TYPE, --使用类型\n" +
				"       F_GET_TC_CODE(D.PART_USE_TYPE) PART_USE_TYPE_NAME,--使用类型名称\n" +
				"       D.IS_MAIN IS_MAIN_PART,--是否主因件\n" +
				"       F_GET_TC_CODE(D.IS_MAIN) IS_MAIN_PART_NAME,--是否主因件名称\n" +
				"       D.PART_MAIN_ID RELATION_MAIN_PART ,--关联主因件\n" +
//				"       E.PART_CODE RELATION_MAIN_PART_CODE,--关联主因件代码\n" + 
				"       NVL(E.PART_NAME,'请选择') RELATION_MAIN_PART_NAME,--关联主因件名称\n" + 
				"       D.ACTIVITY_HOURS_CODE RELATION_LABOUR, --关联工时\n" +
//				"       F.LABOUR_CODE RELATION_LABOUR_CODE, --关联工时代码\n" +
				"       F.CN_DES RELATION_LABOUR_NAME, --关联工时名称\n" + 
				"       999 PART_NUM_MAX --配件最大数量\n" + 
				"  FROM TM_PT_PART_BASE A --配件使用基础信息表\n" + //TT_PART_DEFINE配件新增基础信息表
				"  LEFT JOIN TT_PART_SALES_PRICE B ON A.PART_ID = B.PART_ID\n" + 
				"  LEFT JOIN TT_AS_WR_RULE_LIST C ON A.PART_CODE = C.PART_CODE " + str +"\n" + //TM_PT_PART_BASE
				"  JOIN TT_AS_WR_ACTIVITY_PART D ON A.PART_ID = D.PART_ID " +
				"  LEFT JOIN TM_PT_PART_BASE E ON D.PART_MAIN_ID = E.PART_ID\n" + //关联主因件
				"  LEFT JOIN TT_AS_WR_WRLABINFO F ON D.ACTIVITY_HOURS_CODE = F.LABOUR_CODE AND F.WRGROUP_ID = "+wrgroupId+"\n" + 
				" WHERE 1=1\n");
		if(!activityId.equals("")){
			sql.append(" AND D.ACTIVITY_ID = " + activityId);
		}else{
			sql.append( " AND 1=2 ");
		}
		sql.append(" ORDER BY D.ID ");
		
		System.out.println("sql:"+sql);
		
		List<Map<String,Object>> result  = dao.pageQuery(sql.toString(), null, getFunName());
		return result;
	}
	
	/**
	 * 根据车型获取失效模式
	 * 
	 * @param vin
	 * @return boolean
	 * @throws Exception
	 */
	public List<Map<String, Object>> getFailureMode(Map<String, Object> params)
			throws Exception {
		StringBuffer sql = new StringBuffer();
		String partCode = CommonUtils.checkNull(params.get("partCode"));
		
		sql.append( "SELECT DISTINCT B.FAILURE_MODE_CODE FAILURE_MODE_CODE,\n" +
					"       B.FAILURE_MODE_NAME --故障法定名称\n" + 
					"  FROM TT_AS_WR_FAULT_PARTS A\n" + 
					"  JOIN TT_AS_WR_FAULT_MODE_DETAIL B\n" + 
					"    ON A.FAULT_ID = B.FAULT_ID\n" + 
					" WHERE 1=1 \n");
		//暂时屏蔽
//		if (!"".equals(partCode)) {
//			sql.append(" AND A.PART_CODE = UPPER('"+partCode+"') \n") ;  
//		}else{
//			sql.append(" AND 1=2 \n") ;  
//		}
	    System.out.println("sql:"+sql);
	    List<Map<String,Object>> result  = dao.pageQuery(sql.toString(), null, getFunName());
		return result;
	}
	
	/**
	 * 售后服务-外出项目查询
	 * 
	 * @param parMap
	 * @return boolean
	 * @throws Exception
	 */
	public PageResult<Map<String, Object>> serviceOutProjectQuery(
			Map<String, Object> params, Integer pageSize, Integer curPage)
			throws Exception {
		StringBuffer sql = new StringBuffer();
		
		String feeCode = CommonUtils.checkNull(params.get("feeCode"));
		String feeName = CommonUtils.checkNull(params.get("feeName"));
		
//		String loginUserId = params.get("loginUserId").toString();
//		String loginDealerId = CommonUtils.checkNull(params.get("loginDealerId"));
		
		sql.append( "SELECT A.FEE_ID, A.FEE_CODE, A.FEE_NAME\n" +
					"  FROM TT_AS_WR_OTHERFEE A\n" + 
					" WHERE 1=1 ");
        
//		if (!"".equals(modelId)) {
//			sql.append(" AND A.MODEL_ID like '%"+modelId+"%' \n") ;  
//		}else{
//			sql.append(" AND 1=2 \n") ;  
//		}
		if (!"".equals(feeCode)) {
			sql.append(" AND UPPER(A.FEE_CODE) LIKE UPPER('%"+feeCode+"%') \n") ;  
		}
		if (!"".equals(feeName)) {
			sql.append(" AND UPPER(A.FEE_NAME) LIKE UPPER('%"+feeName+"%') \n") ;  
		}
		sql.append(" ORDER BY A.FEE_NAME ");
		
		System.out.println("sql:"+sql);
		
		return dao.pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
	}
	
	/**
	 * 售后服务-外出维修查询
	 * 
	 * @param parMap
	 * @return boolean
	 * @throws Exception
	 */
	public PageResult<Map<String, Object>> serviceOutRepairQuery(
			Map<String, Object> params, Integer pageSize, Integer curPage)
			throws Exception {
		StringBuffer sql = new StringBuffer();
		
		String vin = CommonUtils.checkNull(params.get("vin"));//车架号
		String egressNo = CommonUtils.checkNull(params.get("egressNo"));//申请单号
		String eLicenseNo = CommonUtils.checkNull(params.get("eLicenseNo"));//派车车牌号
//		String eStartDate = CommonUtils.checkNull(params.get("eStartDate"));//救援开始时间
//		String eEndDate = CommonUtils.checkNull(params.get("eEndDate"));//救援结束时间
		
//		String loginUserId = params.get("loginUserId").toString();
		String loginDealerId = CommonUtils.checkNull(params.get("loginDealerId"));
		
		sql.append( "SELECT A.ID EGRESS_ID, --外出维修申请单ID\n" +
					"       A.EGRESS_NO, --外出维修申请单号\n" + 
					"       A.VIN, --车架号\n" + 
					"       TO_CHAR(A.E_START_DATE,'YYYY-MM-DD HH24:MI') E_START_DATE, --救援开始时间\n" + 
					"       TO_CHAR(A.E_END_DATE,'YYYY-MM-DD HH24:MI') E_END_DATE, --救援结束时间\n" + 
					"       A.E_LICENSE_NO, --派车车牌号\n" + 
					"       A.E_NAME, --外出人\n" + 
					"       B.REGION_NAME || '(省)' || C.REGION_NAME || '(市)' || D.REGION_NAME || A.TOWN DESTINATION, --出差目的地\n" + 
					"       A.RELIEF_MILEAGE, --单程救急里程\n" + 
					"       TO_CHAR(A.CREATE_DATE, 'YYYY-MM-DD') CREATE_DATE\n" + 
					"  FROM TT_AS_EGRESS A --外出维修申请单\n" + 
					"  LEFT JOIN TM_REGION B\n" + 
					"    ON A.PROVINCE = B.REGION_CODE\n" + 
					"  LEFT JOIN TM_REGION C\n" + 
					"    ON A.CITY = C.REGION_CODE\n" + 
					"  LEFT JOIN TM_REGION D\n" + 
					"    ON A.COUNTY = D.REGION_CODE\n" + 
					" WHERE 1 = 1" +
					"   AND A.IS_MAKE_UP = " + Constant.PART_BASE_FLAG_YES +"--已补录\n" +
					"   AND A.IS_RLATION_ORDER = " + Constant.PART_BASE_FLAG_NO +"--未关联维修单\n");
        
		if (!"".equals(loginDealerId)) {
			sql.append(" AND A.DEALER_ID = "+loginDealerId+" \n") ;  
		}else{
			sql.append(" AND 1=2 ");
		}
		if (!"".equals(vin)) {
			sql.append(" AND A.VIN = '"+vin+"' \n") ;  
		}else{
			sql.append(" AND 1=2 ");
		}
		if (!"".equals(egressNo)) {
			sql.append(" AND UPPER(A.EGRESS_NO) LIKE UPPER('%"+egressNo+"%') \n") ;  
		}
		if (!"".equals(eLicenseNo)) {
			sql.append(" AND UPPER(A.E_LICENSE_NO) LIKE UPPER('%"+eLicenseNo+"%') \n") ;  
		}
		
		sql.append(" ORDER BY A.CREATE_DATE DESC ");
		
		System.out.println("sql:"+sql);
		
		return dao.pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
	}
	
	/**
	 * 获取外出维修信息
	 * 
	 * @param vin
	 * @return boolean
	 * @throws Exception
	 */
	public Map<String, Object> getServiceOutRepairInfo(Map<String, Object> params)
			throws Exception {
		StringBuffer sql = new StringBuffer();
		String egressId = CommonUtils.checkNull(params.get("egressId"));
		
		sql.append( "SELECT A.ID EGRESS_ID, --外出维修申请单ID\n" +
					"       A.EGRESS_NO, --外出维修申请单号\n" + 
					"       A.VIN, --车架号\n" + 
					"       TO_CHAR(A.E_START_DATE,'YYYY-MM-DD HH24:MI') E_START_DATE, --救援开始时间\n" + 
					"       TO_CHAR(A.E_END_DATE,'YYYY-MM-DD HH24:MI') E_END_DATE, --救援结束时间\n" + 
					"       A.E_LICENSE_NO, --派车车牌号\n" + 
					"       A.E_NAME, --外出人\n" + 
					"       B.REGION_NAME || '(省)' || C.REGION_NAME || '(市)' || D.REGION_NAME || A.TOWN DESTINATION, --出差目的地\n" + 
					"       A.RELIEF_MILEAGE, --单程救急里程\n" + 
					"       TO_CHAR(A.CREATE_DATE, 'YYYY-MM-DD') CREATE_DATE\n" + 
					"  FROM TT_AS_EGRESS A --外出维修申请单\n" + 
					"  LEFT JOIN TM_REGION B\n" + 
					"    ON A.PROVINCE = B.REGION_CODE\n" + 
					"  LEFT JOIN TM_REGION C\n" + 
					"    ON A.CITY = C.REGION_CODE\n" + 
					"  LEFT JOIN TM_REGION D\n" + 
					"    ON A.COUNTY = D.REGION_CODE\n" + 
					" WHERE 1 = 1");
		
		if (!"".equals(egressId)) {
			sql.append(" AND A.ID = "+egressId+" \n") ;  
		}else{
			sql.append(" AND 1=2 \n") ;  
		}
	    //只要取到一条即可
	    sql.append(" AND ROWNUM = 1 \n") ;
	    System.out.println("sql:"+sql);
		Map<String, Object> result  = dao.pageQueryMap(sql.toString(), null, getFunName());
		return result;
	}
	
	/**
	 * 获取售后服务工单信息
	 * 
	 * @param vin
	 * @return boolean
	 * @throws Exception
	 */
	public Map<String, Object> getServiceOrderInfo(Map<String, Object> params)
			throws Exception {
		StringBuffer sql = new StringBuffer();
		String serviceOrderId = CommonUtils.checkNull(params.get("serviceOrderId"));
		
		sql.append( "SELECT A.SERVICE_ORDER_ID, --售后服务工单ID\n" +
					"       A.SERVICE_ORDER_CODE, --售后服务工单代码\n" + 
					"       A.DEALER_ID, --经销商ID\n" + 
					"       B.DEALER_CODE, --经销商代码\n" + 
					"       B.DEALER_NAME, --经销商名称\n" + 
					"       B.PHONE DEALER_PHONE, --经销商电话\n" + 
					"       A.REPAIR_TYPE, --维修类型\n" + 
					"       F_GET_TC_CODE(A.REPAIR_TYPE) REPAIR_TYPE_NAME, --维修类型名称\n" + 
					"       TO_CHAR(A.ARRIVAL_DATE, 'YYYY-MM-DD HH24:MI') ARRIVAL_DATE, --进站时间\n" + 
					"       A.PURCHASED_DATE, --购车日期\n" + 
					"       A.PURCHASED_MONTHS, --购车时长（月）\n" + 
					"       TO_CHAR(A.REPAIR_DATE_BEGIN, 'YYYY-MM-DD HH24:MI') REPAIR_DATE_BEGIN, --维修开始时间\n" + 
					"       TO_CHAR(A.REPAIR_DATE_END, 'YYYY-MM-DD HH24:MI') REPAIR_DATE_END, --维修结束时间\n" + 
					"       A.REPAIR_DAYS, --维修时长（日）\n" + 
					"       A.MILEAGE, --进站里程数\n" + 
					"       A.RECEPTIONIST_MAN, --接待员\n" + 
					"       A.VIN, --车架号\n" + 
					"       A.DELIVERER_MAN_NAME, --送修人姓名\n" + 
					"       A.DELIVERER_MAN_PHONE, --送修人电话\n" + 
					"       A.EGRESS_ID, --外出维修单ID\n" + 
					"       A.AGREE_MAINTAIN_COST, --同意首保费用\n" + 
					"       CASE WHEN A.AGREE_MAINTAIN_COST= "+Constant.AGREE_MAINTAIN_COST_01+"\n " + 
					"            THEN 'CHECKED' ELSE '' END AGREE_MAINTAIN_COST_CHECKED, --同意首保费用选中\n" + 
					"       A.FAULT_DESC, --故障描述\n" + 
					"       A.FAULT_REASON, --故障原因\n" + 
					"       A.REPAIR_METHOD, --维修措施\n" + 
					"       A.REMARK, --申请备注\n" + 
					"       A.PDI_REMARK, --PDI备注\n" + 
					"       TO_CHAR(A.APPLY_LABOUR_HOUR,'FM99999990.00') APPLY_LABOUR_HOUR, --工时数量\n" + 
					"       TO_CHAR(A.APPLY_LABOUR_PRICE,'FM99999990.00') APPLY_LABOUR_PRICE, --工时金额\n" + 
					"       A.APPLY_PART_NUM, --配件数量\n" + 
					"       TO_CHAR(A.APPLY_PART_PRICE,'FM99999990.00') APPLY_PART_PRICE, --配件金额\n" + 
					"       TO_CHAR(A.APPLY_FEE_PRICE,'FM99999990.00') APPLY_FEE_PRICE, --外出金额\n" + 
					"       TO_CHAR(A.APPLY_MAINTAIN_PRICE,'FM99999990.00') APPLY_MAINTAIN_PRICE, --首保金额\n" + 
					"       TO_CHAR(A.APPLY_PDI_PRICE,'FM99999990.00') APPLY_PDI_PRICE, --PDI金额\n" + 
					"       TO_CHAR(A.APPLY_PRICE_TOTAL,'FM99999990.00') APPLY_PRICE_TOTAL --总金额\n" + 
					"  FROM TT_AS_SERVICE_ORDER A\n" + 
					"  JOIN TM_DEALER B\n" + 
					"    ON A.DEALER_ID = B.DEALER_ID\n" + 
					" WHERE 1=1\n");
		
		if (!"".equals(serviceOrderId)) {
			sql.append(" AND A.SERVICE_ORDER_ID = "+serviceOrderId+" \n") ;  
		}else{
			sql.append(" AND 1=2 \n") ;  
		}
	    //只要取到一条即可
	    sql.append(" AND ROWNUM = 1 \n") ;
	    System.out.println("sql:"+sql);
		Map<String, Object> result  = dao.pageQueryMap(sql.toString(), null, getFunName());
		return result;
	}
	
	/**
	 * 获取维修项目列表
	 * 
	 * @param vin
	 * @return boolean
	 * @throws Exception
	 */
	public List<Map<String, Object>> getServiceOrderProjectList(Map<String, Object> params)
			throws Exception {
		String serviceOrderId = CommonUtils.checkNull(params.get("serviceOrderId"));
		
		StringBuffer sql = new StringBuffer();
		sql.append( "SELECT A.SERVICE_PROJECT_ID, --维修项目ID\n" +
					"       A.SERVICE_ORDER_ID, --售后服务工单ID\n" + 
					"       A.LABOUR_ID, --工时ID\n" + 
					"       A.LABOUR_CODE, --工时代码\n" + 
					"       A.CN_DES, --工时名称\n" + 
					"       A.LABOUR_HOUR, --工时定额\n" + 
					"       TO_CHAR(A.LABOUR_PRICE,'FM99999990.00') LABOUR_PRICE, --工时单价\n" + 
					"       TO_CHAR(A.LABOUR_HOUR*A.LABOUR_PRICE,'FM99999990.00') LABOUR_PRICE_TOTAL,--工时金额\n" + 
					"       A.LABOUR_PAYMENT_METHOD, --付费方式\n" + 
					"       F_GET_TC_CODE(A.LABOUR_PAYMENT_METHOD) LABOUR_PAYMENT_METHOD_NAME --付费方式名称\n" + 
					"  FROM TT_AS_SERVICE_PROJECT A" +
					" WHERE 1=1\n");
		
		if (!"".equals(serviceOrderId)) {
			sql.append(" AND A.SERVICE_ORDER_ID = "+serviceOrderId+" \n") ;  
		}else{
			sql.append(" AND 1=2 \n") ;  
		}
		sql.append(" ORDER BY A.SERVICE_PROJECT_ID  \n") ;  
	    System.out.println("sql:"+sql);
	    List<Map<String,Object>> result  = dao.pageQuery(sql.toString(), null, getFunName());
		return result;
	}
	
	/**
	 * 获取维修配件列表
	 * 
	 * @param vin
	 * @return boolean
	 * @throws Exception
	 */
	public List<Map<String, Object>> getServiceOrderPartList(Map<String, Object> params)
			throws Exception {
		String serviceOrderId = CommonUtils.checkNull(params.get("serviceOrderId"));
		
		StringBuffer sql = new StringBuffer();
		sql.append( "SELECT A.SERVICE_PART_ID, --维修配件ID\n" +
					"       A.SERVICE_ORDER_ID, --售后服务工单ID\n" + 
					"       DECODE(A.IS_THREE_GUARANTEE, 10041001, 'CHECKED', '') IS_THREE_GUARANTEE, --是否三包\n" + 
					"       A.PART_ID, --配件ID\n" + 
					"       A.PART_CODE, --配件代码\n" + 
					"       A.PART_CNAME, --配件名称\n" + 
					"       A.PART_WAR_TYPE, --配件三包类型\n" + 
					"       A.PART_NUM, --配件数量\n" + 
					"       A.SALE_PRICE1, --配件实际调拨价\n" + 
					"       A.PART_FARE_RATE, --经销商配件加价率\n" + 
					"       TO_CHAR(A.PART_PRICE,'FM99999990.00') PART_PRICE, --配件单价\n" + 
					"       TO_CHAR(A.PART_NUM * PART_PRICE,'FM99999990.00') PART_PRICE_TOTAL, --配件总金额\n" + 
					"       A.FAILURE_MODE_CODE, --失效模式\n" + 
					"       D.FAILURE_NAME,--失效模式名称\n" + 
					"       A.PART_PAYMENT_METHOD, --付费方式\n" + 
					"       F_GET_TC_CODE(A.PART_PAYMENT_METHOD) PART_PAYMENT_METHOD_NAME,--付费方式名称\n" + 
					"       A.IS_MAIN_PART, --是否主因件\n" + 
					"       F_GET_TC_CODE(A.IS_MAIN_PART) IS_MAIN_PART_NAME,--是否主因件名称\n" + 
					"       A.RELATION_MAIN_PART, --关联主因件\n" + 
					"       NVL(C.PART_CODE,'-/-') RELATION_MAIN_PART_CODE,--关联主因件代码\n" + 
					"       NVL(C.PART_NAME,'-/-') RELATION_MAIN_PART_NAME,--关联主因件名称\n" + 
					"       A.RELATION_LABOUR, --关联工时\n" + 
					"       B.LABOUR_CODE RELATION_LABOUR_CODE, --关联工时代码\n" + 
					"       B.CN_DES RELATION_LABOUR_NAME --关联工时名称\n" + 
					"  FROM TT_AS_SERVICE_PART A\n" + 
					"  LEFT JOIN TT_AS_WR_WRLABINFO B ON A.RELATION_LABOUR = B.ID\n" + 
					"  LEFT JOIN TM_PT_PART_BASE C ON A.RELATION_MAIN_PART = C.PART_ID\n" + //TT_PART_DEFINE
					"  LEFT JOIN TM_PT_FAILURE_MODE D ON A.FAILURE_MODE_CODE = D.FAILURE_CODE\n" +
		            " WHERE 1=1 \n");
		if (!"".equals(serviceOrderId)) {
			sql.append(" AND A.SERVICE_ORDER_ID = "+serviceOrderId+" \n") ;  
		}else{
			sql.append(" AND 1=2 \n") ;  
		}
		sql.append(" ORDER BY A.SERVICE_PART_ID  \n") ;  
	    System.out.println("sql:"+sql);
	    List<Map<String,Object>> result  = dao.pageQuery(sql.toString(), null, getFunName());
		return result;
	}
	
	/**
	 * 获取外出项目列表
	 * 
	 * @param vin
	 * @return boolean
	 * @throws Exception
	 */
	public List<Map<String, Object>> getServiceOrderOutProjectList(Map<String, Object> params)
			throws Exception {
		String serviceOrderId = CommonUtils.checkNull(params.get("serviceOrderId"));
		
		StringBuffer sql = new StringBuffer();
		sql.append( "SELECT A.SERVICE_OUT_PROJECT_ID,--售后服务工单外出项目ID\n" +
					"       A.SERVICE_ORDER_ID,--售后服务工单ID\n" + 
					"       A.FEE_ID,--项目ID\n" + 
					"       A.FEE_CODE,--项目代码\n" + 
					"       A.FEE_NAME,--项目名称\n" + 
					"       TO_CHAR(A.FEE_PRICE, 'FM99999990.00') FEE_PRICE,--金额(元)\n" + 
					"       A.FEE_REMARK,--备注\n" + 
					"       A.FEE_PAYMENT_METHOD,--付费方式\n" + 
					"       F_GET_TC_CODE(A.FEE_PAYMENT_METHOD) FEE_PAYMENT_METHOD,--付费方式名称\n" + 
					"       A.FEE_RELATION_MAIN_PART,--关联主因件\n" + 
					"       B.PART_CODE FEE_RELATION_MAIN_PART_CODE,--关联主因件代码\n" + 
					"       B.PART_NAME FEE_RELATION_MAIN_PART_NAME,--关联主因件名称\n" + 
					"       A.CREATE_BY,--创建人\n" + 
					"       A.CREATE_DATE--创建时间\n" + 
					"  FROM TT_AS_SERVICE_OUT_PROJECT A\n" + 
					"  JOIN TM_PT_PART_BASE B\n" + //TT_PART_DEFINE
					"    ON A.FEE_RELATION_MAIN_PART = B.PART_ID\n" + 
					" WHERE 1 = 1\n");
		if (!"".equals(serviceOrderId)) {
			sql.append(" AND A.SERVICE_ORDER_ID = "+serviceOrderId+" \n") ;  
		}else{
			sql.append(" AND 1=2 \n") ;  
		}
		sql.append(" ORDER BY A.SERVICE_OUT_PROJECT_ID  \n") ;  
	    System.out.println("sql:"+sql);
	    List<Map<String,Object>> result  = dao.pageQuery(sql.toString(), null, getFunName());
		return result;
	}
	
	/**
	 * 售后服务工单保存
	 * 
	 * @param vin
	 * @return boolean
	 * @throws Exception
	 */
	public void serviceOrderSave(Map<String, Object> params) throws Exception {
		
		String serviceOrderId = CommonUtils.checkNull(params.get("serviceOrderId"));
		String serviceOrderCode = CommonUtils.checkNull(params.get("serviceOrderCode"));
		String dealerId = CommonUtils.checkNull(params.get("dealerId"));
		String repairType = CommonUtils.checkNull(params.get("repairType"));
		String arrivalDate = CommonUtils.checkNull(params.get("arrivalDate"));
		String purchasedDate = CommonUtils.checkNull(params.get("purchasedDate"));
		String purchasedDays = CommonUtils.checkNull(params.get("purchasedDays"));
		String purchasedMonths = CommonUtils.checkNull(params.get("purchasedMonths"));
		String mileage = CommonUtils.checkNull(params.get("mileage"));
		String repairDateBegin = CommonUtils.checkNull(params.get("repairDateBegin"));
		String repairDateEnd = CommonUtils.checkNull(params.get("repairDateEnd"));
		String repairDays = CommonUtils.checkNull(params.get("repairDays"));
		String curFreeTimes = CommonUtils.checkNull(params.get("curFreeTimes"));
		String receptionistMan = CommonUtils.checkNull(params.get("receptionistMan"));
		
		String vin = CommonUtils.checkNull(params.get("vin"));
//		String engineNo = CommonUtils.checkNull(params.get("engineNo"));
//		String licenseNo = CommonUtils.checkNull(params.get("licenseNo"));
		
		String delivererManName = CommonUtils.checkNull(params.get("delivererManName"));
		String delivererManPhone = CommonUtils.checkNull(params.get("delivererManPhone"));
		
		String activityId = CommonUtils.checkNull(params.get("activityId"));
		String egressId = CommonUtils.checkNull(params.get("egressId"));
		
		String faultDesc = CommonUtils.checkNull(params.get("faultDesc"));
		String faultReason = CommonUtils.checkNull(params.get("faultReason"));
		String repairMethod = CommonUtils.checkNull(params.get("repairMethod"));
		String remark = CommonUtils.checkNull(params.get("remark"));
		
		String pdiRemark = CommonUtils.checkNull(params.get("pdiRemark"));
		
		String applyLabourHour = CommonUtils.checkNull(params.get("applyLabourHour"));
		String applyLabourPrice = CommonUtils.checkNull(params.get("applyLabourPrice"));
		String applyPartNum = CommonUtils.checkNull(params.get("applyPartNum"));
		String applyPartPrice = CommonUtils.checkNull(params.get("applyPartPrice"));
		String applyFeePrice = CommonUtils.checkNull(params.get("applyFeePrice"));
		String applyActivityPrice = CommonUtils.checkNull(params.get("applyActivityPrice"));
		String applyMaintainPrice = CommonUtils.checkNull(params.get("applyMaintainPrice"));
		String applyPdiPrice = CommonUtils.checkNull(params.get("applyPdiPrice"));
		String applyPriceTotal = CommonUtils.checkNull(params.get("applyPriceTotal"));
		
		String isCanClaim = CommonUtils.checkNull(params.get("isCanClaim"));
		String createBy = CommonUtils.checkNull(params.get("createBy"));
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");//小写的mm表示的是分钟  
		
		TtAsServiceOrderPO po = new TtAsServiceOrderPO();
		
		po.setServiceOrderId(Long.parseLong(serviceOrderId));
		po.setServiceOrderCode(serviceOrderCode);
		po.setDealerId(Long.parseLong(dealerId));
		po.setRepairType(Integer.parseInt(repairType));
		po.setArrivalDate(sdf.parse(arrivalDate));
		po.setPurchasedDate(purchasedDate.equals("")?null:sdf.parse(purchasedDate+" 00:00"));
		po.setPurchasedDays(Integer.parseInt(purchasedDays));
		po.setPurchasedMonths(Integer.parseInt(purchasedMonths));
		po.setMileage(Double.parseDouble(mileage));
		po.setRepairDateBegin(sdf.parse(repairDateBegin));
		po.setRepairDateEnd(sdf.parse(repairDateEnd));
		po.setRepairDays(Integer.parseInt(repairDays));
		po.setCurFreeTimes(curFreeTimes.equals("")?null:Integer.parseInt(curFreeTimes));
		po.setReceptionistMan(receptionistMan);
		
		po.setVin(vin);
//		po.setEngineNo(engineNo);
//		po.setLicenseNo(licenseNo);
		
		po.setDelivererManName(delivererManName);
		po.setDelivererManPhone(delivererManPhone);
		
		po.setActivityId(activityId.equals("")?null:Long.parseLong(activityId));
		po.setEgressId(egressId.equals("")?null:Long.parseLong(egressId));
		
		po.setFaultDesc(faultDesc);
		po.setFaultReason(faultReason);
		po.setRepairMethod(repairMethod);
		po.setRemark(remark);
		
		po.setPdiRemark(pdiRemark);
		
		po.setApplyLabourHour(applyLabourHour.equals("")?null:Float.parseFloat(applyLabourHour));
		po.setApplyLabourPrice(applyLabourPrice.equals("")?null:Float.parseFloat(applyLabourPrice));
		po.setApplyPartNum(applyPartNum.equals("")?null:Integer.parseInt(applyPartNum));
		po.setApplyPartPrice(applyPartPrice.equals("")?null:Float.parseFloat(applyPartPrice));
		po.setApplyFeePrice(applyFeePrice.equals("")?null:Float.parseFloat(applyFeePrice));
		po.setApplyActivityPrice(applyActivityPrice.equals("")?null:Float.parseFloat(applyActivityPrice));
		po.setApplyMaintainPrice(applyMaintainPrice.equals("")?null:Float.parseFloat(applyMaintainPrice));
		po.setApplyPdiPrice(applyPdiPrice.equals("")?null:Float.parseFloat(applyPdiPrice));
		po.setApplyPriceTotal(applyPriceTotal.equals("")?null:Float.parseFloat(applyPriceTotal));
		
		po.setIsCanClaim(Integer.parseInt(isCanClaim));
		po.setCreateBy(Long.parseLong(createBy));
		po.setCreateDate(new Date());
		dao.insert(po);
	}
	
	//售后服务工单-更新车辆信息记录
	public void vehicleUpdate(Map<String, Object> params) throws Exception {
		
		String vin = CommonUtils.checkNull(params.get("vin"));
		String repairType = CommonUtils.checkNull(params.get("repairType"));
		String mileage = CommonUtils.checkNull(params.get("mileage"));
		String curFreeTimes = CommonUtils.checkNull(params.get("curFreeTimes"));
		String licenseNo = CommonUtils.checkNull(params.get("licenseNo"));
		String updateBy = CommonUtils.checkNull(params.get("updateBy"));
		
		List<Object> parList = new ArrayList<Object>();
		
		StringBuffer sql = new StringBuffer();
		//更新售后服务工单表状态
		sql = new StringBuffer();
		sql.append( "UPDATE TM_VEHICLE A\n" +
					"   SET A.LICENSE_NO = ?,\n");
//					"       A.MILEAGE = ?,\n"); //为了测试方便暂时屏蔽，后期取消屏蔽
		if(repairType.equals(Constant.REPAIR_TYPE_04.toString())){//维修类型是保养,更新保养次数
	    	sql.append( "   A.FREE_TIMES = "+curFreeTimes+",\n");
	    }
	    if(repairType.equals(Constant.REPAIR_TYPE_08.toString())){//维修类型是PDI,更新是否做过PDI
	    	sql.append( "   A.IS_PDI = "+Constant.IF_TYPE_YES+",\n");
	    }
	    sql.append( "       A.UPDATE_BY = ?,\n" + 
					"       A.UPDATE_DATE = SYSDATE\n" + 
					" WHERE A.VIN = ?\n");
		parList.clear();
		parList.add(licenseNo);
//		parList.add(mileage);
		parList.add(updateBy);
		parList.add(vin);
		dao.update(sql.toString(),parList);
	}
	
	//售后服务工单-维修项目保存
	public void serviceOrderProjectSave(Map<String, Object> params) throws Exception {
		
		String serviceProjectId = "";
		String serviceOrderId = CommonUtils.checkNull(params.get("serviceOrderId"));
		String[] labourIds = (String[]) params.get("labourIds");
		String[] labourCodes = (String[]) params.get("labourCodes");
		String[] cnDess = (String[]) params.get("cnDess");
		String[] labourHours = (String[]) params.get("labourHours");
		String[] labourPrices = (String[]) params.get("labourPrices");
		String[] labourPaymentMethods = (String[]) params.get("labourPaymentMethods");
		
		String createBy = CommonUtils.checkNull(params.get("createBy"));
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");//小写的mm表示的是分钟  
		//插入新数据之前先删除数据
		TtAsServiceProjectPO delPo = new TtAsServiceProjectPO();
		delPo.setServiceOrderId(Long.parseLong(serviceOrderId));
		dao.delete(delPo);
		//循环插入数据
		for(int i=1;i<labourIds.length;i++){
			TtAsServiceProjectPO po = new TtAsServiceProjectPO();
			serviceProjectId = SequenceManager.getSequence("");
			
			po.setServiceProjectId(Long.parseLong(serviceProjectId));
			po.setServiceOrderId(Long.parseLong(serviceOrderId));
			po.setLabourId(Long.parseLong(labourIds[i]));
			po.setLabourCode(labourCodes[i]);
			po.setCnDes(cnDess[i]);
			po.setLabourHour(Float.parseFloat(labourHours[i]));
			po.setLabourPrice(Double.parseDouble(labourPrices[i]));
			po.setLabourPaymentMethod(Integer.parseInt(labourPaymentMethods[i]));
			
			po.setCreateBy(Long.parseLong(createBy));
			po.setCreateDate(new Date());
			dao.insert(po);
		}
	}
	
	//售后服务工单-维修配件保存
	public void serviceOrderPartSave(Map<String, Object> params) throws Exception {
		
		String servicePartId = "";
		String serviceOrderId = CommonUtils.checkNull(params.get("serviceOrderId"));
		String[] isThreeGuarantees = (String[]) params.get("isThreeGuarantees");
		String[] partIds = (String[]) params.get("partIds");
		String[] partCodes = (String[]) params.get("partCodes");
		String[] partCnames = (String[]) params.get("partCnames");
		String[] partWarTypes = (String[]) params.get("partWarTypes");
		String[] partNums = (String[]) params.get("partNums");
		String[] salePrice1s = (String[]) params.get("salePrice1s");
		String partFareRate = CommonUtils.checkNull(params.get("partFareRate"));
		String[] partPrices = (String[]) params.get("partPrices");
		String[] failureModeCodes = (String[]) params.get("failureModeCodes");
		String[] partPaymentMethods = (String[]) params.get("partPaymentMethods");
		String[] partUseTypes = (String[]) params.get("partUseTypes");
		String[] isMainParts = (String[]) params.get("isMainParts");
		String[] relationMainParts = (String[]) params.get("relationMainParts");
		String[] relationLabours = (String[]) params.get("relationLabours");
		
		String createBy = CommonUtils.checkNull(params.get("createBy"));
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");//小写的mm表示的是分钟  
		//插入新数据之前先删除数据
		TtAsServicePartPO delPo = new TtAsServicePartPO();
		delPo.setServiceOrderId(Long.parseLong(serviceOrderId));
		dao.delete(delPo);
		//循环插入数据
		for(int i=1;i<partIds.length;i++){
			TtAsServicePartPO po = new TtAsServicePartPO();
			servicePartId = SequenceManager.getSequence("");
			po.setServicePartId(Long.parseLong(servicePartId));
			po.setServiceOrderId(Long.parseLong(serviceOrderId));
			po.setIsThreeGuarantee(Integer.parseInt(isThreeGuarantees[i]));
			po.setPartId(Long.parseLong(partIds[i]));
			po.setPartCode(partCodes[i]);
			po.setPartCname(partCnames[i]);
			po.setPartWarType(Integer.parseInt(partWarTypes[i]));
			po.setPartNum(Integer.parseInt(partNums[i]));
			po.setSalePrice1(Double.parseDouble(salePrice1s[i]));
			po.setPartFareRate(Double.parseDouble(partFareRate));
			po.setPartPrice(Double.parseDouble(partPrices[i]));
			po.setFailureModeCode(failureModeCodes[i]);
			po.setPartPaymentMethod(Integer.parseInt(partPaymentMethods[i]));
			po.setPartUseType(Integer.parseInt(partUseTypes[i]));
			po.setIsMainPart(Integer.parseInt(isMainParts[i]));
			po.setRelationMainPart(relationMainParts[i]==null?null:Long.parseLong(relationMainParts[i]));
			po.setRelationLabour(Long.parseLong(relationLabours[i]));
			
			po.setCreateBy(Long.parseLong(createBy));
			po.setCreateDate(new Date());
			dao.insert(po);
		}
	}
	
	//售后服务工单-外出项目保存
	public void serviceOrderOutProjectSave(Map<String, Object> params) throws Exception {
		
		String serviceOutProjecgtId = "";
		String serviceOrderId = CommonUtils.checkNull(params.get("serviceOrderId"));
		String[] feeIds = (String[]) params.get("feeIds");
		String[] feeCodes = (String[]) params.get("feeCodes");
		String[] feeNames = (String[]) params.get("feeNames");
		String[] feePrices = (String[]) params.get("feePrices");
		String[] feeRemarks = (String[]) params.get("feeRemarks");
		String[] feePaymentMethods = (String[]) params.get("feePaymentMethods");
		String[] feeRelationMainParts = (String[]) params.get("feeRelationMainParts");
		
		String createBy = CommonUtils.checkNull(params.get("createBy"));
//			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");//小写的mm表示的是分钟  
		//插入新数据之前先删除数据
		TtAsServiceOutProjectPO delPo = new TtAsServiceOutProjectPO();
		delPo.setServiceOrderId(Long.parseLong(serviceOrderId));
		dao.delete(delPo);
		//循环插入数据
		for(int i=1;i<feeIds.length;i++){
			TtAsServiceOutProjectPO po = new TtAsServiceOutProjectPO();
			serviceOutProjecgtId = SequenceManager.getSequence("");
			po.setServiceOutProjectId(Long.parseLong(serviceOutProjecgtId));
			po.setServiceOrderId(Long.parseLong(serviceOrderId));
			po.setFeeId(Long.parseLong(feeIds[i]));
			po.setFeeCode(feeCodes[i]);
			po.setFeeName(feeNames[i]);
			po.setFeePrice(Double.parseDouble(feePrices[i]));
			po.setFeeRemark(feeRemarks[i]);
			po.setFeePaymentMethod(Integer.parseInt(feePaymentMethods[i]));
			po.setFeeRelationMainPart(Long.parseLong(feeRelationMainParts[i]));
			po.setCreateBy(Long.parseLong(createBy));
			po.setCreateDate(new Date());
			dao.insert(po);
		}
	}
	
	//售后服务工单-外出维修单更新
	public void serviceOrderEgressUpdate(Map<String, Object> params) throws Exception {
		
		String egressId = CommonUtils.checkNull(params.get("egressId"));
		String isRlationOrder = CommonUtils.checkNull(params.get("isRlationOrder"));
		String updateBy = CommonUtils.checkNull(params.get("updateBy"));
		
		List<Object> parList = new ArrayList<Object>();
		
		StringBuffer sql = new StringBuffer();
		//更新售后服务工单表状态
		sql = new StringBuffer();
		sql.append( "UPDATE TT_AS_EGRESS A\n" +
					"   SET A.IS_RLATION_ORDER = ?,\n" + 
					"       A.UPDATE_BY = ?,\n" + 
					"       A.UPDATE_DATE = SYSDATE\n" + 
					" WHERE A.ID = ?\n");
		parList.clear();
		parList.add(isRlationOrder);
		parList.add(updateBy);
		parList.add(egressId);
		dao.update(sql.toString(),parList);
	}
	
	//售后服务工单-车辆表是否PDI更新
	public void vehicleIsPdiUpdate(Map<String, Object> params) throws Exception {
		
		String vin = CommonUtils.checkNull(params.get("vin"));
		String isPdi = CommonUtils.checkNull(params.get("isPdi"));
		String updateBy = CommonUtils.checkNull(params.get("updateBy"));
		
		List<Object> parList = new ArrayList<Object>();
		
		StringBuffer sql = new StringBuffer();
		//更新售后服务工单表状态
		sql = new StringBuffer();
		sql.append( "UPDATE TM_VEHICLE A\n" +
					"   SET A.IS_PDI = ?,\n" + 
					"       A.UPDATE_BY = ?,\n" + 
					"       A.UPDATE_DATE = SYSDATE\n" + 
					" WHERE A.VIN = ?\n");
		parList.clear();
		parList.add(isPdi);
		parList.add(updateBy);
		parList.add(vin);
		dao.update(sql.toString(),parList);
	}
	
	//售后服务工单-整车维修天数预警保存
	public void serviceOrderWarnDaySave(Map<String, Object> params) throws Exception {
		
		String serviceOrderId = CommonUtils.checkNull(params.get("serviceOrderId"));
		String createBy = CommonUtils.checkNull(params.get("createBy"));
		
		String vin = "";
		String purchasedMonths = "";
		String mileage = "";
		String repairDays = "0";
		String repairDaysTotal = "0";
		
		StringBuffer sql = new StringBuffer();
		
		sql.append( "SELECT A.SERVICE_ORDER_ID,\n" +
					"       A.SERVICE_ORDER_CODE,\n" + 
					"       A.VIN,\n" + 
					"       A.PURCHASED_MONTHS,\n" + 
					"       A.MILEAGE,\n" + 
					"       NVL(A.REPAIR_DAYS,0) REPAIR_DAYS,\n" + 
					"       (SELECT NVL(SUM(TASO.REPAIR_DAYS), 0) + NVL(A.REPAIR_DAYS,0)\n" + 
					"          FROM TT_AS_SERVICE_ORDER TASO\n" + 
					"         WHERE A.VIN = TASO.VIN\n" + 
					"           AND TASO.STATUS = "+Constant.SERVICE_ORDER_STATUS_08+") REPAIR_DAYS_TOTAL\n" +//已结算
					"  FROM TT_AS_SERVICE_ORDER A\n" + 
					" WHERE 1=1\n" +//未上报 A.STATUS = "+Constant.SERVICE_ORDER_STATUS_01+"
					"   AND A.SERVICE_ORDER_ID = " + serviceOrderId);
		Map<String, Object> serviceOrderMap = dao.pageQueryMap(sql.toString(), null, getFunName());
		if(serviceOrderMap!=null){
			vin = CommonUtils.checkNull(serviceOrderMap.get("VIN"));
			purchasedMonths = CommonUtils.checkNull(serviceOrderMap.get("PURCHASED_MONTHS"));
			mileage = CommonUtils.checkNull(serviceOrderMap.get("MILEAGE"));
			repairDays = CommonUtils.checkNull(serviceOrderMap.get("REPAIR_DAYS"));
			repairDaysTotal = CommonUtils.checkNull(serviceOrderMap.get("REPAIR_DAYS_TOTAL"));
		}
		
		sql = new StringBuffer();
		sql.append( "INSERT INTO TT_AS_SERVICE_WARN_DAY A\n" +
					"  (WARN_DAY_ID,\n" + 
					"   SERVICE_ORDER_ID,\n" + 
					"   VIN,\n" + 
					"   WARN_RULE_ID,\n" + 
					"   REPAIR_DAYS,\n" + 
					"   CREATE_BY,\n" + 
					"   CREATE_DATE)\n" + 
					"  SELECT F_GETID(), --单次维修天数预警\n" + 
					"         "+serviceOrderId+" SERVICE_ORDER_ID, --维修工单ID\n" + 
					"         '"+vin+"' VIN, --VIN\n" + 
					"         A.ID WARN_RULE_ID, --预警规则ID\n" + 
					"         "+repairDays+" REPAIR_DAYS, --预警天数\n" + 
					"         "+createBy+",\n" + 
					"         SYSDATE\n" +  
					"    FROM TT_AS_WARNING_TIME A --预警时间规则维护\n" + 
					"   WHERE A.WAINING_TYPE = "+Constant.SWANINGTIME_TYPE_01+"\n" + 
					"     AND A.STATUS = "+Constant.STATUS_ENABLE+"\n" + 
					"     AND A.VALID_MILEAGE >= "+mileage+"--规则有效里程数大于等于进站里程数\n" + 
					"     AND A.VALID_DATE >= "+purchasedMonths+" --规则有效月份数大于等于购车月份数\n" + 
					"     AND A.WARNING_TIME_START <= "+repairDays+"  --规则预警最小天数小于等于维修天数\n" + 
					"     AND A.WARNING_TIME_END >= "+repairDays+" --规则预警最大天数大于维修天数\n" + 
					"     AND A.STATUS = "+Constant.STATUS_ENABLE+"\n" +
					"   UNION ALL \n" +
					"  SELECT F_GETID(),--维修总天数预警\n" + 
					"         "+serviceOrderId+" SERVICE_ORDER_ID, --维修工单ID\n" + 
					"         '"+vin+"' VIN, --VIN\n" + 
					"         A.ID WARN_RULE_ID, --预警规则ID\n" + 
					"         "+repairDaysTotal+" REPAIR_DAYS, --预警天数\n" + 
					"         "+createBy+",\n" + 
					"         SYSDATE\n" + 
					"    FROM TT_AS_WARNING_TIME A --预警时间规则维护\n" + 
					"   WHERE A.WAINING_TYPE = "+Constant.SWANINGTIME_TYPE_02+"\n" + 
					"     AND A.STATUS = "+Constant.STATUS_ENABLE+"\n" + 
					"     AND A.VALID_MILEAGE >= "+mileage+"--规则有效里程数大于等于进站里程数\n" + 
					"     AND A.VALID_DATE >= "+purchasedMonths+" --规则有效月份数大于等于购车月份数\n" + 
					"     AND A.WARNING_TIME_START <= "+repairDaysTotal+"  --规则预警最小天数小于等于维修天数\n" + 
					"     AND A.WARNING_TIME_END >= "+repairDaysTotal+" --规则预警最大天数大于维修天数\n" +
					"     AND A.STATUS = "+Constant.STATUS_ENABLE+"\n");
		
		dao.insert(sql.toString());
	}
	
	//售后服务工单-配件维修次数预警保存
	public void serviceOrderWarnNumSave(Map<String, Object> params) throws Exception {
		
		String serviceOrderId = CommonUtils.checkNull(params.get("serviceOrderId"));
		String createBy = CommonUtils.checkNull(params.get("createBy"));
		
		String vin = "";
		String purchasedMonths = "";
		String mileage = "";
		String partId = "";
		String partCode = "";
		String failureModeCode = "";
		
		String repairNumTotal = "0";//配件维修累积次数
		String failureModeNumTotal = "0";//配件失效模式累积次数
		
		//轮询每个配件,检查预警次数
		StringBuffer sql = new StringBuffer();
		sql.append( "SELECT A.SERVICE_ORDER_ID,\n" +
					"       A.SERVICE_ORDER_CODE,\n" + 
					"       A.VIN,\n" + 
					"       A.PURCHASED_MONTHS,\n" + 
					"       A.MILEAGE,\n" + 
					"       B.SERVICE_PART_ID,\n" + 
					"       B.PART_ID,\n" + 
					"       B.PART_CODE,\n" + 
					"       B.FAILURE_MODE_CODE\n" + 
					"  FROM TT_AS_SERVICE_ORDER A\n" + 
					"  JOIN TT_AS_SERVICE_PART B\n" + 
					"    ON A.SERVICE_ORDER_ID = B.SERVICE_ORDER_ID\n" + 
					" WHERE 1=1\n" + //A.STATUS = "+Constant.SERVICE_ORDER_STATUS_01+" --未上报
					"   AND B.PART_WAR_TYPE = "+Constant.PART_WR_TYPE_1+" --常用件\n" + 
					"   AND B.PART_PAYMENT_METHOD = "+Constant.PAY_TYPE_02+" --索赔\n" + 
					"   AND B.IS_MAIN_PART = "+Constant.PART_BASE_FLAG_YES+" --主因件\n" +
					"   AND A.SERVICE_ORDER_ID = "+serviceOrderId+" \n");
		List<Map<String,Object>> serviceOrderPartList = dao.pageQuery(sql.toString(), null, getFunName());
		if(serviceOrderPartList!=null&&serviceOrderPartList.size()>0){
			for(int i=0;i<serviceOrderPartList.size();i++){
				vin = CommonUtils.checkNull(serviceOrderPartList.get(i).get("VIN"));
				purchasedMonths = CommonUtils.checkNull(serviceOrderPartList.get(i).get("PURCHASED_MONTHS"));
				mileage = CommonUtils.checkNull(serviceOrderPartList.get(i).get("MILEAGE"));
				partId = CommonUtils.checkNull(serviceOrderPartList.get(i).get("PART_ID"));
				partCode = CommonUtils.checkNull(serviceOrderPartList.get(i).get("PART_CODE"));
				failureModeCode = CommonUtils.checkNull(serviceOrderPartList.get(i).get("FAILURE_MODE_CODE"));
				
				//先查出当前VIN已结算维修工单的维修配件数量
				sql = new StringBuffer();
				sql.append( "SELECT COUNT(1) REPAIR_NUM,\n" +
							"       NVL(SUM(DECODE(B.FAILURE_MODE_CODE, '"+failureModeCode+"', 1,0)),0) FAILURE_MODE_NUM\n" + 
							"  FROM TT_AS_SERVICE_ORDER A\n" + 
							"  JOIN TT_AS_SERVICE_PART B\n" + 
							"    ON A.SERVICE_ORDER_ID = B.SERVICE_ORDER_ID\n" + 
							" WHERE B.PART_ID = "+partId+"\n" + 
							"   AND B.PART_WAR_TYPE = "+Constant.PART_WR_TYPE_1+" --常用件\n" + 
							"   AND B.PART_PAYMENT_METHOD = "+Constant.PAY_TYPE_02+" --索赔\n" + 
							"   AND B.IS_MAIN_PART = "+Constant.PART_BASE_FLAG_YES+" --主因件\n" + 
							"   AND A.VIN = '"+vin+"'\n" +
							"   AND A.STATUS = "+Constant.SERVICE_ORDER_STATUS_08+"--工单已结算\n");
				Map<String, Object> map = dao.pageQueryMap(sql.toString(), null, getFunName());
				if(map!=null){
					if(map.get("REPAIR_NUM")!=null){
						repairNumTotal = CommonUtils.checkNull(map.get("REPAIR_NUM"));
					}
					if(map.get("FAILURE_MODE_NUM")!=null){
						failureModeNumTotal = CommonUtils.checkNull(map.get("FAILURE_MODE_NUM"));
					}
				}
				//配件维修累积次数加1
				repairNumTotal = (Integer.parseInt(repairNumTotal) + 1) + "";
				//配件失效模式累积次数加1
				failureModeNumTotal = (Integer.parseInt(failureModeNumTotal) + 1) + "";
				
				sql = new StringBuffer();
				sql.append( "INSERT INTO TT_AS_SERVICE_WARN_NUM A\n" +
							"  (WARN_NUM_ID,\n" + 
							"   SERVICE_ORDER_ID,\n" + 
							"   VIN,\n" + 
							"   WARN_RULE_ID,\n" + 
							"   PART_ID,\n" + 
							"   PART_CODE,\n" + 
							"   FAILURE_MODE_CODE,\n" + 
							"   REPAIR_NUM,\n" + 
							"   CREATE_BY,\n" + 
							"   CREATE_DATE)\n" + 
							"  SELECT F_GETID(), --总成预警\n" + 
							"         "+serviceOrderId+" SERVICE_ORDER_ID, --维修工单ID\n" + 
							"         '"+vin+"' VIN, --VIN\n" + 
							"         A.WARNING_REPAIR_ID WARN_RULE_ID, --预警规则ID\n" + 
							"         "+partId+" PART_ID,--预警配件ID\n" +
							"         '"+partCode+"' PART_CODE,--预警配件代码\n" +
							"         '"+failureModeCode+"' FAILURE_MODE_CODE,--预警配件失效模式代码\n" +
							"         "+repairNumTotal+" REPAIR_NUM,--预警配件维修次数\n" +
							"         "+createBy+",\n" + 
							"         SYSDATE\n" + 
							"    FROM TT_AS_WARNING_REPAIR A --三包预警规则维护\n" +
							"    JOIN TT_AS_WARNING_REPAIR_DETAIL B --三包预警规则维护明细\n" + 
							"      ON A.WARNING_REPAIR_ID = B.WARNING_REPAIR_ID\n" + 
							"    JOIN TT_AS_WR_PARTS_ASSEMBLY C --总成维护\n" + 
							"      ON B.CHANGE_CODE = C.PARTS_ASSEMBLY_CODE\n" + 
							"     AND C.STATUS = 10011001\n" + 
							"    JOIN TT_AS_WR_PART_LEGAL D --零件法定名称维护\n" + 
							"      ON C.PARTS_ASSEMBLY_ID = D.PARTS_ASSEMBLY_ID\n" + 
							"     AND D.STATUS = 10011001\n" +
							"    JOIN TT_AS_WR_PART_LEGALL_DETAIL E --零件法定名称明细\n" + 
							"      ON D.PART_LEGAL_ID = E.PART_LEGAL_ID\n" + 
//							"     AND E.STATUS = 10041001\n" +
							"   WHERE A.WARNING_TYPE = "+Constant.WANINGTIME_TYPE_01+"\n" + 
							"     AND A.VALID_MILEAGE >= "+mileage+"--规则有效里程数大于等于进站里程数\n" + 
							"     AND A.VALID_DATE >= "+purchasedMonths+" --规则有效月份数大于等于购车月份数\n" + 
							"     AND A.WARNING_NUM_START <= "+repairNumTotal+"  --规则预警最小次数小于等于配件维修次数\n" + 
							"     AND A.WARNING_NUM_END >= "+repairNumTotal+" --规则预警最大次数大于等于配件维修次数\n" + 
							"     AND E.PART_CODE = '" + partCode + "' \n" +
							"     AND A.STATUS = "+Constant.STATUS_ENABLE+"\n" +
							"   UNION ALL \n" +
							"  SELECT F_GETID(), --严重安全故障模式预警\n" + 
							"         "+serviceOrderId+" SERVICE_ORDER_ID, --维修工单ID\n" + 
							"         '"+vin+"' VIN, --VIN\n" + 
							"         A.WARNING_REPAIR_ID WARN_RULE_ID, --预警规则ID\n" + 
							"         "+partId+" PART_ID,--预警配件ID\n" +
							"         '"+partCode+"' PART_CODE,--预警配件代码\n" +
							"         '"+failureModeCode+"' FAILURE_MODE_CODE,--预警配件失效模式代码\n" +
							"         "+failureModeNumTotal+" REPAIR_NUM,--预警配件维修次数\n" +
							"         "+createBy+",\n" + 
							"         SYSDATE\n" + 
							"    FROM TT_AS_WARNING_REPAIR A --三包预警规则维护\n" +
							"    JOIN TT_AS_WARNING_REPAIR_DETAIL B --三包预警规则维护明细\n" + 
							"      ON A.WARNING_REPAIR_ID = B.WARNING_REPAIR_ID\n" + 
							"    JOIN TT_AS_WR_FAULT_LEGAL C --严重安全性能故障法定名称维护\n" + 
							"      ON B.CHANGE_CODE = C.FAULT_CODE\n" + 
							"    JOIN TT_AS_WR_FAULT_MODE_DETAIL D --故障法定名称失效信息\n" + 
							"      ON C.FAULT_ID = D.FAULT_ID\n" + 
							"     AND D.FAILURE_MODE_CODE = '"+failureModeCode+"'\n" + 
							"     AND D.STATUS = 10011001\n" + 
							"    JOIN TT_AS_WR_FAULT_PARTS E --故障法定名称配件信息\n" + 
							"      ON C.FAULT_ID = E.FAULT_ID\n" + 
							"     AND E.STATUS = 10011001" +
							"   WHERE A.WARNING_TYPE = "+Constant.WANINGTIME_TYPE_02+"\n" + 
							"     AND A.VALID_MILEAGE >= "+mileage+"--规则有效里程数大于等于进站里程数\n" + 
							"     AND A.VALID_DATE >= "+purchasedMonths+" --规则有效月份数大于等于购车月份数\n" + 
							"     AND A.WARNING_NUM_START <= "+failureModeNumTotal+"  --规则预警最小次数小于等于配件维修次数\n" + 
							"     AND A.WARNING_NUM_END >= "+failureModeNumTotal+" --规则预警最大次数大于等于配件维修次数\n" + 
							"     AND E.PART_CODE = '" + partCode + "' \n" +
							"     AND A.STATUS = "+Constant.STATUS_ENABLE+"\n" +
							"   UNION ALL \n" +
							"  SELECT F_GETID(), --同一质量问题预警\n" + 
							"         "+serviceOrderId+" SERVICE_ORDER_ID, --维修工单ID\n" + 
							"         '"+vin+"' VIN, --VIN\n" + 
							"         A.WARNING_REPAIR_ID WARN_RULE_ID, --预警规则ID\n" + 
							"         "+partId+" PART_ID,--预警配件ID\n" +
							"         '"+partCode+"' PART_CODE,--预警配件代码\n" +
							"         '"+failureModeCode+"' FAILURE_MODE_CODE,--预警配件失效模式代码\n" +
							"         "+repairNumTotal+" REPAIR_NUM,--预警配件维修次数\n" +
							"         "+createBy+",\n" + 
							"         SYSDATE\n" + 
							"    FROM TT_AS_WARNING_REPAIR A --三包预警规则维护\n" +
							"   WHERE A.WARNING_TYPE = "+Constant.WANINGTIME_TYPE_03+"\n" + 
							"     AND A.VALID_MILEAGE >= "+mileage+"--规则有效里程数大于等于进站里程数\n" + 
							"     AND A.VALID_DATE >= "+purchasedMonths+" --规则有效月份数大于等于购车月份数\n" + 
							"     AND A.WARNING_NUM_START <= "+repairNumTotal+"  --规则预警最小次数小于等于配件维修次数\n" + 
							"     AND A.WARNING_NUM_END >= "+repairNumTotal+" --规则预警最大次数大于等于配件维修次数\n" +
							"     AND A.STATUS = "+Constant.STATUS_ENABLE+"\n");
				System.out.println("sql:"+sql);
				dao.insert(sql.toString());
			}
		}
	}
	
	//售后服务工单-预授权内容信息保存
	public void serviceOrderAuthSave(Map<String, Object> params) throws Exception {
		
		String serviceOrderId = CommonUtils.checkNull(params.get("serviceOrderId"));
		
		String createBy = CommonUtils.checkNull(params.get("createBy"));
		
//		String priceTotal = "0";
		String labourPriceTotal = "0";
		String partPriceTotal = "0";
		
		StringBuffer sql = new StringBuffer();
		//获取维修工单索赔工时的总费用
		sql = new StringBuffer();
		sql.append( "SELECT NVL(SUM(B.LABOUR_HOUR * B.LABOUR_PRICE),0) LABOUR_PRICE_TOTAL\n" +
					"               FROM TT_AS_SERVICE_ORDER A\n" + 
					"               JOIN TT_AS_SERVICE_PROJECT B\n" + 
					"                 ON A.SERVICE_ORDER_ID = B.SERVICE_ORDER_ID\n" + 
					"                AND B.LABOUR_PAYMENT_METHOD = 11801002 --只针对索赔\n" + 
					"              WHERE A.SERVICE_ORDER_ID = " + serviceOrderId);
		Map<String, Object> labourPriceTotalMap = dao.pageQueryMap(sql.toString(), null, getFunName());
		if(labourPriceTotalMap!=null&&!CommonUtils.checkNull(labourPriceTotalMap.get("LABOUR_PRICE_TOTAL")).equals("")){
			labourPriceTotal = CommonUtils.checkNull(labourPriceTotalMap.get("LABOUR_PRICE_TOTAL"));
		}
		//获取维修工单索赔配件的总费用
		sql = new StringBuffer();
		sql.append( "SELECT NVL(SUM(B.PART_NUM * B.PART_PRICE),0) PART_PRICE_TOTAL\n" +
					"               FROM TT_AS_SERVICE_ORDER A\n" + 
					"               JOIN TT_AS_SERVICE_PART B\n" + 
					"                 ON A.SERVICE_ORDER_ID = B.SERVICE_ORDER_ID\n" + 
					"                AND B.PART_PAYMENT_METHOD = 11801002 --只针对索赔\n" + 
					"              WHERE A.SERVICE_ORDER_ID = " + serviceOrderId);
		Map<String, Object> partPriceTotalMap = dao.pageQueryMap(sql.toString(), null, getFunName());
		if(partPriceTotalMap!=null&&!CommonUtils.checkNull(partPriceTotalMap.get("PART_PRICE_TOTAL")).equals("")){
			partPriceTotal = CommonUtils.checkNull(partPriceTotalMap.get("PART_PRICE_TOTAL"));
		}
		
//		DecimalFormat format = new DecimalFormat("#.00");//保留2位小数
//		priceTotal = format.format(Double.parseDouble(labourPriceTotal) +  Double.parseDouble(partPriceTotal));
		
		sql = new StringBuffer();
		sql.append( "INSERT INTO TT_AS_SERVICE_AUTH_CONTENT A\n" +
					"  (AUTH_CONTENT_ID,\n" + 
					"   SERVICE_ORDER_ID,\n" + 
					"   AUTH_CONTENT_TYPE,\n" + 
					"   AUTH_CONTENT_VALUE,\n" + 
					"   CREATE_BY,\n" + 
					"   CREATE_DATE)\n" + 
					"SELECT F_GETID() AUTH_CONTENT_ID,\n" + 
					"       A.SERVICE_ORDER_ID,\n" + 
					"       "+Constant.AUTH_CONTENT_TYPE_01+" AUTH_CONTENT_TYPE, --授权内容类型\n" + 
					"       B.ID AUTH_CONTENT_VALUE, --授权内容值\n" + 
					"       "+createBy+" CREATE_BY,\n" + 
					"       SYSDATE CREATE_DATE\n" + 
					"  FROM TT_AS_SERVICE_ORDER A --售后服务工单表\n" + 
					"  JOIN TT_AS_WR_WOOR_LEVEL B --索赔授权监控工单类型\n" + 
					"    ON A.REPAIR_TYPE = B.NUM\n" + 
					"   AND B.WOOR_LEVEL IS NOT NULL\n" +
					" WHERE A.SERVICE_ORDER_ID = "+serviceOrderId+"\n" + 
					"UNION ALL\n" + 
					"SELECT F_GETID(),\n" + 
					"       A.SERVICE_ORDER_ID,\n" + 
					"       "+Constant.AUTH_CONTENT_TYPE_02+" AUTH_CONTENT_TYPE, --授权内容类型\n" + 
					"       C.ID AUTH_CONTENT_VALUE, --授权内容值\n" + 
					"       "+createBy+" CREATE_BY,\n" + 
					"       SYSDATE CREATE_DATE\n" + 
					"  FROM TT_AS_SERVICE_ORDER A --售后服务工单表\n" + 
					"  JOIN TT_AS_SERVICE_PROJECT B --售后服务工单维修项目表\n" + 
					"    ON A.SERVICE_ORDER_ID = B.SERVICE_ORDER_ID\n" + 
					"  JOIN TT_AS_WR_AUTHMONITORLAB C --索赔授权监控工时\n" + 
					"    ON B.LABOUR_CODE = C.LABOUR_OPERATION_NO\n" + 
					"   AND C.IS_DEL = 0\n" +//未删除
					"   AND C.APPROVAL_LEVEL IS NOT NULL\n" +
					" WHERE A.SERVICE_ORDER_ID = "+serviceOrderId+"\n" + 
					"UNION ALL\n" + 
					"SELECT F_GETID(),\n" + 
					"       A.SERVICE_ORDER_ID,\n" + 
					"       "+Constant.AUTH_CONTENT_TYPE_03+" AUTH_CONTENT_TYPE, --授权内容类型\n" + 
					"       C.ID AUTH_CONTENT_VALUE, --授权内容值\n" + 
					"       "+createBy+" CREATE_BY,\n" + 
					"       SYSDATE CREATE_DATE\n" + 
					"  FROM TT_AS_SERVICE_ORDER A --售后服务工单表\n" + 
					"  JOIN TT_AS_SERVICE_PART B --售后服务工单维修项目表\n" + 
					"    ON A.SERVICE_ORDER_ID = B.SERVICE_ORDER_ID\n" + 
					"  JOIN TT_AS_WR_AUTHMONITORPART C --索赔授权监控配件\n" + 
					"    ON B.PART_CODE = C.PART_CODE\n" + 
					"   AND C.IS_DEL = 0\n" +//未删除
					"   AND C.APPROVAL_LEVEL IS NOT NULL\n" +
					" WHERE A.SERVICE_ORDER_ID = "+serviceOrderId+"\n" + 
					"UNION ALL\n" + 
					"SELECT F_GETID(),\n" + 
					"       A.SERVICE_ORDER_ID,\n" + 
					"       "+Constant.AUTH_CONTENT_TYPE_04+" AUTH_CONTENT_TYPE, --授权内容类型\n" + 
					"       B.RULE_ELEMENT AUTH_CONTENT_VALUE, --授权内容值\n" + 
					"       "+createBy+" CREATE_BY,\n" + 
					"       SYSDATE CREATE_DATE\n" + 
					"  FROM TT_AS_SERVICE_ORDER A --售后服务工单表\n" + 
					"  JOIN TT_AS_WR_RULEMAPPING B --索赔授权监控规则\n" + 
					"    ON F_AS_SERVICE_ORDER_RULEMAPPING("+labourPriceTotal+","+partPriceTotal+", B.RULE_ELEMENT) = 1 --通过工单ID和授权规则匹配明细\n" + 
					"   AND B.PRIOR_LEVEL IS NOT NULL\n" +
					" WHERE A.SERVICE_ORDER_ID = "+serviceOrderId+"\n" + 
					"UNION ALL\n" + 
					"SELECT F_GETID(),\n" + 
					"       A.SERVICE_ORDER_ID,\n" + 
					"       "+Constant.AUTH_CONTENT_TYPE_05+" AUTH_CONTENT_TYPE, --授权内容类型\n" + 
					"       C.ID AUTH_CONTENT_VALUE, --授权内容值\n" + 
					"       "+createBy+" CREATE_BY,\n" + 
					"       SYSDATE CREATE_DATE\n" + 
					"  FROM TT_AS_SERVICE_ORDER A --售后服务工单表\n" + 
					"  JOIN TT_AS_SERVICE_WARN_DAY B --整车维修天数预警快照表\n" + 
					"    ON A.SERVICE_ORDER_ID = B.SERVICE_ORDER_ID\n" + 
					"  JOIN TT_AS_WARNING_TIME C --整车维修天数预警快照表\n" + 
					"    ON B.WARN_RULE_ID = C.ID\n" + 
					" WHERE A.SERVICE_ORDER_ID = "+serviceOrderId+"\n" + 
					"   AND C.APPROVAL_LEVER_CODE IS NOT NULL\n" +
					"UNION ALL\n" + 
					"SELECT F_GETID(),\n" + 
					"       A.SERVICE_ORDER_ID,\n" + 
					"       "+Constant.AUTH_CONTENT_TYPE_05+" AUTH_CONTENT_TYPE, --授权内容类型\n" + 
					"       C.WARNING_REPAIR_ID AUTH_CONTENT_VALUE, --授权内容值\n" + 
					"       "+createBy+" CREATE_BY,\n" + 
					"       SYSDATE CREATE_DATE\n" + 
					"  FROM TT_AS_SERVICE_ORDER A --售后服务工单表\n" + 
					"  JOIN TT_AS_SERVICE_WARN_NUM B --整车维修次数预警快照表\n" + 
					"    ON A.SERVICE_ORDER_ID = B.SERVICE_ORDER_ID\n" + 
					"  JOIN TT_AS_WARNING_REPAIR C --三包预警规则维护\n" + 
					"    ON B.WARN_RULE_ID = C.WARNING_REPAIR_ID\n" + 
					" WHERE A.SERVICE_ORDER_ID = "+serviceOrderId+"\n" +
					"   AND C.PPROVAL_LEVER_CODE IS NOT NULL\n");
		System.out.println("sql:"+sql);
		dao.insert(sql.toString());
	}
	
	//售后服务工单-保养预授权内容信息保存
	public void serviceOrderAuthMaintainSave(Map<String, Object> params) throws Exception {
		
		String serviceOrderId = CommonUtils.checkNull(params.get("serviceOrderId"));
		
		String createBy = CommonUtils.checkNull(params.get("createBy"));
		
		StringBuffer sql = new StringBuffer();
		sql.append("INSERT INTO TT_AS_SERVICE_AUTH_CONTENT A\n") ;
		sql.append("  (AUTH_CONTENT_ID,\n") ;
		sql.append("   SERVICE_ORDER_ID,\n") ;
		sql.append("   AUTH_CONTENT_TYPE,\n") ;
		sql.append("   AUTH_CONTENT_VALUE,\n") ;
		sql.append("   CREATE_BY,\n") ;
		sql.append("   CREATE_DATE)\n") ;
		sql.append("  SELECT F_GETID() AUTH_CONTENT_ID,\n") ;
		sql.append("         A.SERVICE_ORDER_ID,\n") ;
		sql.append("         "+Constant.AUTH_CONTENT_TYPE_01+" AUTH_CONTENT_TYPE, --授权内容类型\n") ;
		sql.append("         F.ID AUTH_CONTENT_VALUE, --授权内容值\n") ;
		sql.append("         "+createBy+" CREATE_BY,\n") ;
		sql.append("         SYSDATE CREATE_DATE\n") ;
		sql.append("    FROM TT_AS_SERVICE_ORDER A\n") ;
		sql.append("    JOIN TM_VEHICLE B\n") ;
		sql.append("      ON A.VIN = B.VIN\n") ;
		sql.append("    JOIN TT_AS_WR_MODEL_ITEM C\n") ;
		sql.append("      ON B.PACKAGE_ID = C.MODEL_ID\n") ;
		sql.append("    JOIN TT_AS_WR_MODEL_GROUP D\n") ;
		sql.append("      ON C.WRGROUP_ID = D.WRGROUP_ID\n") ;
		sql.append("    JOIN TT_AS_WR_QAMAINTAIN E\n") ;
		sql.append("      ON D.QAMAINTAIN_ID = E.ID\n") ;
		sql.append("    JOIN TT_AS_WR_WOOR_LEVEL F\n") ;
		sql.append("      ON F.NUM = "+Constant.REPAIR_TYPE_04+"\n") ;
		sql.append("     AND F.WOOR_LEVEL IS NOT NULL\n") ;
		sql.append("   WHERE 1 = 1\n") ;
		sql.append("    AND CASE WHEN A.PURCHASED_DAYS <= E.MAX_DAYS AND A.MILEAGE <= E.END_MILEAGE THEN 1 ELSE 0 END = 0 --首保超期\n") ;
		sql.append("    AND A.CUR_FREE_TIMES = 1\n");//当前保养次数为1即为首保的才需预授权
		sql.append("    AND A.SERVICE_ORDER_ID = "+serviceOrderId+"\n");
		System.out.println("sql:"+sql);
		dao.insert(sql.toString());
	}
	
	//售后服务工单-预授权审核信息保存
	public void serviceOrderAuthAuditSave(Map<String, Object> params) throws Exception {
		
		String serviceOrderId = CommonUtils.checkNull(params.get("serviceOrderId"));
		
		String createBy = CommonUtils.checkNull(params.get("createBy"));
		
		StringBuffer sql = new StringBuffer();
		
		//授权级别信息
		sql = new StringBuffer();
		sql.append( "SELECT A.APPROVAL_LEVEL_CODE,--预授权级别\n" +
					"       A.APPROVAL_LEVEL_NAME,\n" + 
					"       A.APPROVAL_LEVEL_TIER\n" + 
					"  FROM TT_AS_WR_AUTHINFO A\n" + 
					" WHERE A.APPROVAL_LEVEL_CODE <> 100\n" + 
					"   AND A.TYPE = 0\n" + 
					" ORDER BY TO_NUMBER(A.APPROVAL_LEVEL_CODE)");
	    List<Map<String, Object>> approvalLevelList = dao.pageQuery(sql.toString(), null, getFunName());
	    
	    //授权内容信息
	    sql = new StringBuffer();
	    sql.append( "  SELECT DISTINCT B.APPROVAL_LEVEL --预授权级别\n" + 
//	    		    "  SELECT A.SERVICE_ORDER_ID, --售后服务工单ID\n" + 
//					"         A.AUTH_CONTENT_ID,\n" + 
//					"         B.APPROVAL_LEVEL\n" + 
					"    FROM TT_AS_SERVICE_AUTH_CONTENT A --售后服务工单-预授权内容表\n" + 
					"    JOIN (SELECT ID AUTH_CONTENT_VALUE, WOOR_LEVEL APPROVAL_LEVEL\n" + 
					"            FROM TT_AS_WR_WOOR_LEVEL --索赔授权监控工单类型\n" + 
					"          UNION\n" + 
					"          SELECT ID, APPROVAL_LEVEL\n" + 
					"            FROM TT_AS_WR_AUTHMONITORLAB --索赔授权监控工时\n" + 
					"          UNION\n" + 
					"          SELECT ID, APPROVAL_LEVEL\n" + 
					"            FROM TT_AS_WR_AUTHMONITORPART --索赔授权监控配件\n" + 
					"          UNION\n" + 
					"          SELECT RULE_ELEMENT, ROLE\n" + 
					"            FROM TT_AS_WR_RULEMAPPING --索赔授权监控规则\n" + 
					"          UNION\n" + 
					"          SELECT ID, APPROVAL_LEVER_CODE\n" + 
					"            FROM TT_AS_WARNING_TIME --预警时间规则维护\n" + 
					"          UNION\n" + 
					"          SELECT WARNING_REPAIR_ID, PPROVAL_LEVER_CODE\n" + 
					"            FROM TT_AS_WARNING_REPAIR --三包预警规则维护\n" + 
					"          ) B\n" + 
					"      ON A.AUTH_CONTENT_VALUE = B.AUTH_CONTENT_VALUE\n" + 
					"   WHERE A.SERVICE_ORDER_ID = "+serviceOrderId+"\n");
        List<Map<String, Object>> authContentList = dao.pageQuery(sql.toString(), null, getFunName());
        //如果工单含有预授权内容，则匹配预授权级别，写入售后工单预授权审核信息
		if(approvalLevelList!=null&&authContentList!=null&&approvalLevelList.size()>0&&authContentList.size()>0){
			a:for(int i=0;i<approvalLevelList.size();i++){
				for(int j=0;j<authContentList.size();j++){
					if(CommonUtils.checkNull(authContentList.get(j).get("APPROVAL_LEVEL")).indexOf(CommonUtils.checkNull(approvalLevelList.get(i).get("APPROVAL_LEVEL_CODE")))>-1){
						sql = new StringBuffer();
						sql.append( "INSERT INTO TT_AS_SERVICE_AUTH_AUDIT A\n" + 
									"  (AUTH_AUDIT_ID,\n" + 
									"   SERVICE_ORDER_ID,\n" + 
//									"   AUTH_CONTENT_ID,\n" + 
									"   APPROVAL_LEVEL,\n" + 
									"   CREATE_BY,\n" + 
									"   CREATE_DATE)\n" + 
									"   VALUES\n" + 
									"  (F_GETID(), --授权审核ID\n" + 
									"   "+serviceOrderId+", --售后服务工单ID\n" + 
//									"         A.AUTH_CONTENT_ID,\n" + 
									"   "+approvalLevelList.get(i).get("APPROVAL_LEVEL_CODE").toString()+",\n" + 
									"   "+createBy+", --创建人\n" + 
									"   SYSDATE)\n" );
						System.out.println("sql:"+sql);
						dao.insert(sql.toString());
						//一个级别只写一条数据即可
						continue a;
					}
				}
			}
		}
	}
	
	//售后服务工单上报
	public void serviceOrderSubSave(Map<String, Object> params) throws Exception {
		
		String serviceOrderId = CommonUtils.checkNull(params.get("serviceOrderId"));
		String updateBy = CommonUtils.checkNull(params.get("updateBy"));
		String status = "";
		
		String approvalLevel = "";
		List<Object> parList = new ArrayList<Object>();
		//得到预授权审核最小审核级别
		StringBuffer sql = new StringBuffer();
		sql.append( "SELECT MIN(TO_NUMBER(A.APPROVAL_LEVEL)) APPROVAL_LEVEL\n" +
					"  FROM TT_AS_SERVICE_AUTH_AUDIT A\n" + 
					" WHERE 1 = 1\n" + 
					"   AND A.AUTH_AUDIT_STATUS IS NULL\n" + 
					"   AND A.SERVICE_ORDER_ID = "+serviceOrderId+"\n");
		Map<String, Object> map = dao.pageQueryMap(sql.toString(), null, getFunName());
		if(map!=null&&!CommonUtils.checkNull(map.get("APPROVAL_LEVEL")).equals("")){//存在预授权审核最小级别
			approvalLevel = CommonUtils.checkNull(map.get("APPROVAL_LEVEL"));
			status = Constant.SERVICE_ORDER_STATUS_03.toString();//预授权审核中
			//更新预授权审核表审核状态
			sql = new StringBuffer();
			sql.append( "UPDATE TT_AS_SERVICE_AUTH_AUDIT A\n" +
						"   SET A.AUTH_AUDIT_STATUS = ?\n" + 
						" WHERE A.SERVICE_ORDER_ID = ?\n" + 
						"   AND A.APPROVAL_LEVEL = ?\n" + 
						"   AND A.AUTH_AUDIT_STATUS IS NULL\n");
			parList.clear();
			parList.add(Constant.AUTH_AUDIT_STATUS_01);
			parList.add(serviceOrderId);
			parList.add(approvalLevel);
			dao.update(sql.toString(),parList);
		}else{//不存在预授权审核最小级别
			status = Constant.SERVICE_ORDER_STATUS_02.toString();//未结算
		}
		//更新售后服务工单表状态
		sql = new StringBuffer();
		sql.append( "UPDATE TT_AS_SERVICE_ORDER A\n" +
					"   SET A.STATUS = ?,\n" + 
					"       A.UPDATE_BY = ?,\n" + 
					"       A.UPDATE_DATE = SYSDATE\n" + 
					" WHERE A.SERVICE_ORDER_ID = ?\n");
		parList.clear();
		parList.add(status);
		parList.add(updateBy);
		parList.add(serviceOrderId);
		dao.update(sql.toString(),parList);
	}
	
	//售后服务工单结算
	public void serviceOrderSettlementSave(Map<String, Object> params) throws Exception {
		
		String serviceOrderId = CommonUtils.checkNull(params.get("serviceOrderId"));
		String updateBy = CommonUtils.checkNull(params.get("updateBy"));
		
		List<Object> parList = new ArrayList<Object>();
		
		StringBuffer sql = new StringBuffer();
		//更新售后服务工单表状态
		sql = new StringBuffer();
		sql.append( "UPDATE TT_AS_SERVICE_ORDER A\n" +
					"   SET A.STATUS = ?,\n" + 
					"       A.UPDATE_BY = ?,\n" + 
					"       A.UPDATE_DATE = SYSDATE\n" + 
					" WHERE A.SERVICE_ORDER_ID = ?\n");
		parList.clear();
		parList.add(Constant.SERVICE_ORDER_STATUS_08);
		parList.add(updateBy);
		parList.add(serviceOrderId);
		dao.update(sql.toString(),parList);
	}
	
	//售后服务工单-整车维修天数预警检查 根据售后服务工单-整车维修天数预警的SQL修改得到
	public List<Map<String, Object>> serviceOrderWarnDayCheck(Map<String, Object> params) throws Exception {
		
		String serviceOrderId = CommonUtils.checkNull(params.get("serviceOrderId"));
		String vin = CommonUtils.checkNull(params.get("vin"));
		String purchasedMonths = CommonUtils.checkNull(params.get("purchasedMonths"));
		String mileage = CommonUtils.checkNull(params.get("mileage"));
		String repairDays = CommonUtils.checkNull(params.get("repairDays"));
		String repairDaysTotal = "0";
		String createBy = CommonUtils.checkNull(params.get("createBy"));
		
		StringBuffer sql = new StringBuffer();
		//先查出当前VIN已结算维修工单的维修总天数
		sql = new StringBuffer();
		sql.append( "SELECT NVL(SUM(A.REPAIR_DAYS),0) REPAIR_DAYS\n" +
					"  FROM TT_AS_SERVICE_ORDER A\n" + 
					" WHERE A.VIN = '"+vin+"'\n" + 
					"   AND A.STATUS = "+Constant.SERVICE_ORDER_STATUS_08+"--工单已结算\n");
		Map<String, Object> map = dao.pageQueryMap(sql.toString(), null, getFunName());
		if(map!=null&&map.get("REPAIR_DAYS")!=null){
			repairDaysTotal = CommonUtils.checkNull(map.get("REPAIR_DAYS"));
		}
		repairDaysTotal = (Integer.parseInt(repairDays) + Integer.parseInt(repairDaysTotal)) + "";
		sql = new StringBuffer();
		sql.append( "  SELECT --单次维修天数预警\n" + 
					"         "+serviceOrderId+" SERVICE_ORDER_ID, --维修工单ID\n" + 
					"         '"+vin+"' VIN, --VIN\n" + 
					"         A.ID WARN_RULE_ID, --预警规则ID\n" + 
					"         "+repairDays+" REPAIR_DAYS, --预警天数\n" + 
					"         "+createBy+",\n" + 
					"         SYSDATE\n" + 
					"    FROM TT_AS_WARNING_TIME A --预警时间规则维护\n" + 
					"   WHERE A.WAINING_TYPE = "+Constant.SWANINGTIME_TYPE_01+"\n" + 
					"     AND A.STATUS = "+Constant.STATUS_ENABLE+"\n" + 
					"     AND A.VALID_MILEAGE >= "+mileage+"--规则有效里程数大于等于进站里程数\n" + 
					"     AND A.VALID_DATE >= "+purchasedMonths+" --规则有效月份数大于等于购车月份数\n" + 
					"     AND A.WARNING_TIME_START <= "+repairDays+"  --规则预警最小天数小于等于维修天数\n" + 
					"     AND A.WARNING_TIME_END >= "+repairDays+" --规则预警最大天数大于维修天数\n" + 
					"   UNION ALL \n" +
					"  SELECT --维修总天数预警\n" + 
					"         "+serviceOrderId+" SERVICE_ORDER_ID, --维修工单ID\n" + 
					"         '"+vin+"' VIN, --VIN\n" + 
					"         A.ID WARN_RULE_ID, --预警规则ID\n" + 
					"         "+repairDaysTotal+" REPAIR_DAYS, --预警天数\n" + 
					"         "+createBy+",\n" + 
					"         SYSDATE\n" + 
					"    FROM TT_AS_WARNING_TIME A --预警时间规则维护\n" + 
					"   WHERE A.WAINING_TYPE = "+Constant.SWANINGTIME_TYPE_02+"\n" + 
					"     AND A.STATUS = "+Constant.STATUS_ENABLE+"\n" + 
					"     AND A.VALID_MILEAGE >= "+mileage+"--规则有效里程数大于等于进站里程数\n" + 
					"     AND A.VALID_DATE >= "+purchasedMonths+" --规则有效月份数大于等于购车月份数\n" + 
					"     AND A.WARNING_TIME_START <= "+repairDaysTotal+"  --规则预警最小天数小于等于维修天数\n" + 
					"     AND A.WARNING_TIME_END >= "+repairDaysTotal+" --规则预警最大天数大于维修天数\n");
		
		List<Map<String,Object>> result  = dao.pageQuery(sql.toString(), null, getFunName());
		return result;
	}
	
	//售后服务工单-配件维修次数预警检查 根据售后服务工单-配件维修次数预警的SQL修改得到
	public List<Map<String, Object>> serviceOrderWarnNumCheck(Map<String, Object> params) throws Exception {
		
		String serviceOrderId = CommonUtils.checkNull(params.get("serviceOrderId"));
		String vin = CommonUtils.checkNull(params.get("vin"));
		String purchasedMonths = CommonUtils.checkNull(params.get("purchasedMonths"));
		String mileage = CommonUtils.checkNull(params.get("mileage"));
		String[] partIds = (String[]) params.get("partIds");
		String[] partCodes = (String[]) params.get("partCodes");
		String[] partWarTypes = (String[]) params.get("partWarTypes");
		String[] failureModeCodes = (String[]) params.get("failureModeCodes");
		String[] partPaymentMethods = (String[]) params.get("partPaymentMethods");
		String[] isMainParts = (String[]) params.get("isMainParts");
		
		String repairNumTotal = "0";//配件维修累积次数
		String failureModeNumTotal = "0";//配件失效模式累积次数s
		String createBy = CommonUtils.checkNull(params.get("createBy"));
		
		List<Map<String,Object>> result = new ArrayList<Map<String,Object>>();
		StringBuffer sql = new StringBuffer();
		for(int i=1;i<partIds.length;i++){
			//当前配件非常用件,不需要预警
			if(!partWarTypes[i].equals(Constant.PART_WR_TYPE_1.toString())){
				continue;
			}
			//当前配件非索赔件，不需要预警
			if(!partPaymentMethods[i].equals(Constant.PAY_TYPE_02.toString())){
				continue;
			}
			//当前配非主因件，不需要预警
			if(!isMainParts[i].equals(Constant.PART_BASE_FLAG_YES.toString())){
				continue;
			}
			//先查出当前VIN已结算维修工单的维修配件数量
			sql = new StringBuffer();
			sql.append( "SELECT COUNT(1) REPAIR_NUM,\n" +
						"       NVL(SUM(DECODE(B.FAILURE_MODE_CODE, '"+failureModeCodes[i]+"', 1,0)),0) FAILURE_MODE_NUM\n" + 
						"  FROM TT_AS_SERVICE_ORDER A\n" + 
						"  JOIN TT_AS_SERVICE_PART B\n" + 
						"    ON A.SERVICE_ORDER_ID = B.SERVICE_ORDER_ID\n" + 
						" WHERE B.PART_ID = "+partIds[i]+"\n" + 
						"   AND B.PART_WAR_TYPE = "+Constant.PART_WR_TYPE_1+" --常用件\n" + 
						"   AND B.PART_PAYMENT_METHOD = "+Constant.PAY_TYPE_02+" --索赔\n" + 
						"   AND B.IS_MAIN_PART = "+Constant.PART_BASE_FLAG_YES+" --主因件\n" + 
						"   AND A.VIN = '"+vin+"'\n" +
						"   AND A.STATUS = "+Constant.SERVICE_ORDER_STATUS_08+"--工单已结算\n");
			Map<String, Object> map = dao.pageQueryMap(sql.toString(), null, getFunName());
			if(map!=null){
				if(map.get("REPAIR_NUM")!=null){
					repairNumTotal = CommonUtils.checkNull(map.get("REPAIR_NUM"));
				}
				if(map.get("FAILURE_MODE_NUM")!=null){
					failureModeNumTotal = CommonUtils.checkNull(map.get("FAILURE_MODE_NUM"));
				}
			}
			//配件维修累积次数加1
			repairNumTotal = (Integer.parseInt(repairNumTotal) + 1) + "";
			//配件失效模式累积次数加1
			failureModeNumTotal = (Integer.parseInt(failureModeNumTotal) + 1) + "";
			
			sql = new StringBuffer();
			sql.append( "  SELECT --总成预警\n" + 
						"         "+serviceOrderId+" SERVICE_ORDER_ID, --维修工单ID\n" + 
						"         '"+vin+"' VIN, --VIN\n" + 
						"         A.WARNING_REPAIR_ID WARN_RULE_ID, --预警规则ID\n" + 
						"         "+partIds[i]+" PART_ID,--预警配件ID\n" +
						"         '"+partCodes[i]+"' PART_CODE,--预警配件代码\n" +
						"         '"+failureModeCodes[i]+"' FAILURE_MODE_CODE,--预警配件失效模式代码\n" +
						"         "+repairNumTotal+" REPAIR_NUM,--预警配件维修次数\n" +
						"         "+createBy+",\n" + 
						"         SYSDATE\n" + 
						"    FROM TT_AS_WARNING_REPAIR A --三包预警规则维护\n" +
						"    JOIN TT_AS_WARNING_REPAIR_DETAIL B --三包预警规则维护明细\n" + 
						"      ON A.WARNING_REPAIR_ID = B.WARNING_REPAIR_ID\n" + 
						"    JOIN TT_AS_WR_PARTS_ASSEMBLY C --总成维护\n" + 
						"      ON B.CHANGE_CODE = C.PARTS_ASSEMBLY_CODE\n" + 
						"     AND C.STATUS = 10011001\n" + 
						"    JOIN TT_AS_WR_PART_LEGAL D --零件法定名称维护\n" + 
						"      ON C.PARTS_ASSEMBLY_ID = D.PARTS_ASSEMBLY_ID\n" +
						"     AND D.STATUS = 10011001\n" +
						"    JOIN TT_AS_WR_PART_LEGALL_DETAIL E --零件法定名称明细\n" + 
						"      ON D.PART_LEGAL_ID = E.PART_LEGAL_ID\n" + 
//						"     AND E.STATUS = 10041001\n" +
						"   WHERE A.WARNING_TYPE = "+Constant.WANINGTIME_TYPE_01+"\n" + 
						"     AND A.VALID_MILEAGE >= "+mileage+"--规则有效里程数大于等于进站里程数\n" + 
						"     AND A.VALID_DATE >= "+purchasedMonths+" --规则有效月份数大于等于购车月份数\n" + 
						"     AND A.WARNING_NUM_START <= "+repairNumTotal+"  --规则预警最小次数小于等于配件维修次数\n" + 
						"     AND A.WARNING_NUM_END >= "+repairNumTotal+" --规则预警最大次数大于等于配件维修次数\n" + 
						"     AND E.PART_CODE = '" + partCodes[i] + "' \n" +
						"   UNION ALL \n" +
						"  SELECT --严重安全故障模式预警\n" + 
						"         "+serviceOrderId+" SERVICE_ORDER_ID, --维修工单ID\n" + 
						"         '"+vin+"' VIN, --VIN\n" + 
						"         A.WARNING_REPAIR_ID WARN_RULE_ID, --预警规则ID\n" + 
						"         "+partIds[i]+" PART_ID,--预警配件ID\n" +
						"         '"+partCodes[i]+"' PART_CODE,--预警配件代码\n" +
						"         '"+failureModeCodes[i]+"' FAILURE_MODE_CODE,--预警配件失效模式代码\n" +
						"         "+failureModeNumTotal+" REPAIR_NUM,--预警配件维修次数\n" +
						"         "+createBy+",\n" + 
						"         SYSDATE\n" + 
						"    FROM TT_AS_WARNING_REPAIR A --三包预警规则维护\n" +
						"    JOIN TT_AS_WARNING_REPAIR_DETAIL B --三包预警规则维护明细\n" + 
						"      ON A.WARNING_REPAIR_ID = B.WARNING_REPAIR_ID\n" + 
						"    JOIN TT_AS_WR_FAULT_LEGAL C --严重安全性能故障法定名称维护\n" + 
						"      ON B.CHANGE_CODE = C.FAULT_CODE\n" + 
						"    JOIN TT_AS_WR_FAULT_MODE_DETAIL D --故障法定名称失效信息\n" + 
						"      ON C.FAULT_ID = D.FAULT_ID\n" + 
						"     AND D.FAILURE_MODE_CODE = '"+failureModeCodes[i]+"'\n" + 
						"     AND D.STATUS = 10011001\n" + 
						"    JOIN TT_AS_WR_FAULT_PARTS E --故障法定名称配件信息\n" + 
						"      ON C.FAULT_ID = E.FAULT_ID\n" + 
						"     AND E.STATUS = 10011001" +
						"   WHERE A.WARNING_TYPE = "+Constant.WANINGTIME_TYPE_02+"\n" + 
						"     AND A.VALID_MILEAGE >= "+mileage+"--规则有效里程数大于等于进站里程数\n" + 
						"     AND A.VALID_DATE >= "+purchasedMonths+" --规则有效月份数大于等于购车月份数\n" + 
						"     AND A.WARNING_NUM_START <= "+failureModeNumTotal+"  --规则预警最小次数小于等于配件维修次数\n" + 
						"     AND A.WARNING_NUM_END >= "+failureModeNumTotal+" --规则预警最大次数大于等于配件维修次数\n" + 
						"     AND E.PART_CODE = '" + partCodes[i] + "' \n" +
						"   UNION ALL \n" +
						"  SELECT --同一质量问题预警\n" + 
						"         "+serviceOrderId+" SERVICE_ORDER_ID, --维修工单ID\n" + 
						"         '"+vin+"' VIN, --VIN\n" + 
						"         A.WARNING_REPAIR_ID WARN_RULE_ID, --预警规则ID\n" + 
						"         "+partIds[i]+" PART_ID,--预警配件ID\n" +
						"         '"+partCodes[i]+"' PART_CODE,--预警配件代码\n" +
						"         '"+failureModeCodes[i]+"' FAILURE_MODE_CODE,--预警配件失效模式代码\n" +
						"         "+repairNumTotal+" REPAIR_NUM,--预警配件维修次数\n" +
						"         "+createBy+",\n" + 
						"         SYSDATE\n" + 
						"    FROM TT_AS_WARNING_REPAIR A --三包预警规则维护\n" +
						"   WHERE A.WARNING_TYPE = "+Constant.WANINGTIME_TYPE_03+"\n" + 
						"     AND A.VALID_MILEAGE >= "+mileage+"--规则有效里程数大于等于进站里程数\n" + 
						"     AND A.VALID_DATE >= "+purchasedMonths+" --规则有效月份数大于等于购车月份数\n" + 
						"     AND A.WARNING_NUM_START <= "+repairNumTotal+"  --规则预警最小次数小于等于配件维修次数\n" + 
						"     AND A.WARNING_NUM_END >= "+repairNumTotal+" --规则预警最大次数大于等于配件维修次数\n" );
			System.out.println("sql:"+sql);
			List<Map<String,Object>> warnNumList  = dao.pageQuery(sql.toString(), null, getFunName());
			result.addAll(warnNumList);
		}
		return result;
	}
	
	/**
	 * 公用方法-查询TC_CODE的NAME
	 * 
	 * @param vin
	 * @return boolean
	 * @throws Exception
	 */
	public String getTcCodeName(String codeId)
			throws Exception {
		StringBuffer sql = new StringBuffer();
		String codeDesc = "";
		sql.append( "SELECT A.CODE_ID,\n" +
					"       A.CODE_DESC\n" + 
					"  FROM TC_CODE A\n" + 
					" WHERE 1 = 1");
		
		if (!"".equals(codeId)) {
			sql.append(" AND A.CODE_ID = "+codeId+" \n") ;  
		}else{
			sql.append(" AND 1=2 \n") ;  
		}
	    //只要取到一条即可
	    sql.append(" AND ROWNUM = 1 \n") ;
	    System.out.println("sql:"+sql);
		Map<String, Object> map  = dao.pageQueryMap(sql.toString(), null, getFunName());
		if(map!=null){
			codeDesc = CommonUtils.checkNull(map.get("CODE_DESC"));
		}
		return codeDesc;
	}
	
	
}
