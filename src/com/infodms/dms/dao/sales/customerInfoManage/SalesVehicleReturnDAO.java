package com.infodms.dms.dao.sales.customerInfoManage;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import static com.infodms.dms.util.UserProvinceRelation.getDealerIds;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.businessUtil.GetVinUtil;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.ActionContextExtend;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

public class SalesVehicleReturnDAO extends BaseDao{

	public static Logger logger = Logger.getLogger(SalesReportDAO.class);
	private static final SalesVehicleReturnDAO dao = new SalesVehicleReturnDAO();

	public static final SalesVehicleReturnDAO getInstance() {
		return dao;
	}
	
	/***
	 *查询实销车辆列表
	 * 
	 * @param :
	 * @return
	 * @throws Exception
	 */
	public static PageResult<Map<String, Object>> getSalesedVehicleList(String 

areaId,String dealer_Id, String orgId, String dutyType,String vin, int pageSize,int curPage) {
		ActionContextExtend atx = (ActionContextExtend) ActionContext.getContext();
		String reqURL = atx.getRequest().getContextPath();
		
		StringBuilder sql= new StringBuilder();
		sql.append("SELECT TMD.DEALER_CODE,\n" );
		sql.append("       TMD.DEALER_SHORTNAME,\n" );
		sql.append("       TVM.MATERIAL_CODE,\n" );
		sql.append("       TVM.MATERIAL_NAME,\n" );
		sql.append("       TMV.VIN,\n" );
		sql.append("       TTC.CTM_NAME,\n" );
		sql.append("       TDAS.VEHICLE_ID,\n" );
		sql.append("       TDAS.SALES_DATE,\n" );
		sql.append("       TDAS.ORDER_ID\n" );
		sql.append("  FROM TT_DEALER_ACTUAL_SALES TDAS,\n" );
		sql.append("       TM_VEHICLE             TMV,\n" );
		sql.append("       TM_VHCL_MATERIAL       TVM,\n" );
		sql.append("       TT_CUSTOMER            TTC,\n" );
		sql.append("       TM_DEALER              TMD\n" );
		sql.append(" WHERE TDAS.VEHICLE_ID = TMV.VEHICLE_ID\n" );
		sql.append("   AND TMV.MATERIAL_ID = TVM.MATERIAL_ID\n" );
		sql.append("   AND TTC.CTM_ID = TDAS.CTM_ID\n" );
		sql.append("   AND TDAS.DEALER_ID = TMD.DEALER_ID");
		if("10431003".equals(dutyType)){
			sql.append(" AND DEALER_ID IN");
			sql.append("  (SELECT DEALER_ID FROM vw_org_dealer \n");
			sql.append("WHERE ROOT_ORG_ID="+orgId+")");
		}else if("10431004".equals(dutyType)){
			sql.append(" AND DEALER_ID IN\n");
			sql.append("  (SELECT DEALER_ID FROM vw_org_dealer \n");
			sql.append("WHERE PQ_ORG_ID="+orgId+")");
		}else{
			sql.append(" AND TDAS.DEALER_ID IN("+dealer_Id+")\n");
		}
		
		sql.append(" AND TMV.LIFE_CYCLE = "+Constant.VEHICLE_LIFE_04+"\n");  
		sql.append("   AND TMV.LOCK_STATUS = "+Constant.LOCK_STATUS_01+"\n");
		sql.append("   AND TDAS.IS_RETURN = "+Constant.IF_TYPE_NO+"\n");
		if (null != vin && !"".equals(vin)) {
			sql.append(GetVinUtil.getVins(vin, "TMV"));
		}
		sql.append(" ORDER BY TDAS.CREATE_DATE DESC\n");
		
		
		
		return dao.pageQuery(sql.toString(),null,dao.getFunName(),pageSize, 

curPage);
	}
	/***
	 *查询车辆实销信息
	 * 
	 * @param :
	 * @return
	 * @throws Exception
	 */
	public static Map<String,String> getSalesVehicleInfo(String vehicleId){
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT TMV.VEHICLE_ID,\n");
		sql.append("       TDS.ORDER_ID,\n");  
		sql.append("       G.GROUP_CODE SERIES_CODE,\n");  
		sql.append("       G.GROUP_NAME SERIES_NAME,\n");  
		sql.append("       G1.GROUP_CODE MODEL_CODE,\n");  
		sql.append("       G1.GROUP_NAME MODEL_NAME,\n");  
		sql.append("       TMV.VIN,\n");  
		sql.append("       TDW.WAREHOUSE_NAME,\n");  
		sql.append("       TO_CHAR(TDS.SALES_DATE, 'yyyy-MM-dd hh24:mi') SALES_DATE,\n");  
		sql.append("       TMV.ENGINE_NO, --发动机号\n");  
		sql.append("       TMV.COLOR, --颜色\n");  
		sql.append("       TDS.VEHICLE_NO, --车牌号\n");  
		sql.append("       TDS.CONTRACT_NO, --合同编号\n");  
		sql.append("       TO_CHAR(TDS.INVOICE_DATE,'yyyy-MM-dd')INVOICE_DATE, --开票日期\n");  
		sql.append("       TDS.INVOICE_NO, --发票编号\n");  
		sql.append("       TDS.INSURANCE_COMPANY, --保险公司\n");  
		sql.append("       TO_CHAR(TDS.INSURANCE_DATE,'yyyy-MM-dd')INSURANCE_DATE, --保险日期\n");  
		sql.append("       TO_CHAR(TDS.CONSIGNATION_DATE,'yyyy-MM-dd')CONSIGNATION_DATE, --车辆交付日期\n");  
		sql.append("       TDS.MILES, --交付时公里数\n");  
		sql.append("       TDS.PAYMENT, --付款方式\n");  
		sql.append("       TDS.PRICE, --价格\n");  
		sql.append("       TDS.MORTGAGE_TYPE, --按揭类型\n");
		sql.append("       TDS.SHOUFU_RATIO, --首付比例\n");
		sql.append("       TDS.LOANS_TYPE, --贷款方式\n");
		sql.append("       TDS.BANK, --按揭银行\n");
		sql.append("       TDS.MONEY, --贷款金额\n");
		sql.append("       TDS.LOANS_YEAR, --贷款年限\n");
		sql.append("       TDS.LV, --利率\n");
		sql.append("       TDS.THISCHANGE, --本品置换\n");
		sql.append("       TDS.LOANSCHANGE, --其他品牌置换\n");
		sql.append("       TDS.MEMO, --备注\n");  
		sql.append("       TDS.KNOW_ADDRESS, --地址\n");  
		sql.append("       TDS.SALES_RESON, --原因\n");  
		sql.append("       TDS.SALES_ADDRESS --销售地点\n");  
		sql.append("  FROM TM_VEHICLE             TMV,\n");  
		sql.append("       TM_VHCL_MATERIAL_GROUP G,\n");  
		sql.append("       TM_VHCL_MATERIAL_GROUP G1,\n");  
		sql.append("       TM_DEALER_WAREHOUSE    TDW,\n");  
		sql.append("       TT_DEALER_ACTUAL_SALES TDS\n");
		
		sql.append(" WHERE TMV.LIFE_CYCLE ="+Constant.VEHICLE_LIFE_04+" \n");  
		sql.append("   AND TMV.SERIES_ID = G.GROUP_ID\n");  
		sql.append("   AND TMV.MODEL_ID = G1.GROUP_ID\n");  
		sql.append("   AND TMV.VEHICLE_AREA = TDW.WAREHOUSE_ID(+)\n");  
		sql.append("   AND TMV.VEHICLE_ID = TDS.VEHICLE_ID\n");  
		sql.append("   AND TMV.VEHICLE_ID ="+vehicleId+"\n");  
		sql.append("AND TDS.IS_RETURN='"+Constant.IF_TYPE_NO+"'");
		return dao.pageQueryMap(sql.toString(), null, dao.getFunName());
	}
	/***
	 *查询车辆实销信息
	 * 
	 * @param :
	 * @return
	 * @throws Exception
	 */
	public static Map<String,String> getSalesVehicleInfoByOrderId(String order_id){
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT TMV.VEHICLE_ID,\n");
		sql.append("       TDS.ORDER_ID,\n");  
		sql.append("       G.GROUP_CODE SERIES_CODE,\n");  
		sql.append("       G.GROUP_NAME SERIES_NAME,\n");  
		sql.append("       G1.GROUP_CODE MODEL_CODE,\n");  
		sql.append("       G1.GROUP_NAME MODEL_NAME,\n");  
		sql.append("       TMV.VIN,\n");  
		sql.append("       TDW.WAREHOUSE_NAME,\n");  
		sql.append("       TO_CHAR(TDS.SALES_DATE, 'yyyy-MM-dd hh24:mi') SALES_DATE,\n");  
		sql.append("       TMV.ENGINE_NO, --发动机号\n");  
		sql.append("       TMV.COLOR, --颜色\n");  
		sql.append("       TDS.VEHICLE_NO, --车牌号\n");  
		sql.append("       TDS.CONTRACT_NO, --合同编号\n");  
		sql.append("       TO_CHAR(TDS.INVOICE_DATE,'yyyy-MM-dd')INVOICE_DATE, --开票日期\n");  
		sql.append("       TDS.INVOICE_NO, --发票编号\n");  
		sql.append("       TDS.INSURANCE_COMPANY, --保险公司\n");  
		sql.append("       TO_CHAR(TDS.INSURANCE_DATE,'yyyy-MM-dd')INSURANCE_DATE, --保险日期\n");  
		sql.append("       TO_CHAR(TDS.CONSIGNATION_DATE,'yyyy-MM-dd')CONSIGNATION_DATE, --车辆交付日期\n");  
		sql.append("       TDS.MILES, --交付时公里数\n");  
		sql.append("       TDS.PAYMENT, --付款方式\n");  
		sql.append("       TDS.PRICE, --价格\n");  
		sql.append("       TDS.MORTGAGE_TYPE, --按揭类型\n");
		sql.append("       TDS.SHOUFU_RATIO, --首付比例\n");
		sql.append("       TDS.LOANS_TYPE, --贷款方式\n");
		sql.append("       TDS.BANK, --按揭银行\n");
		sql.append("       TDS.MONEY, --贷款金额\n");
		sql.append("       TDS.LOANS_YEAR, --贷款年限\n");
		sql.append("       TDS.LV, --利率\n");
		sql.append("       TDS.THISCHANGE, --本品置换\n");
		sql.append("       TDS.LOANSCHANGE, --其他品牌置换\n");
		sql.append("       TDS.MEMO, --备注\n");  
		sql.append("       TDS.KNOW_ADDRESS, --地址\n");  
		sql.append("       TDS.SALES_RESON, --原因\n");  
		sql.append("       TDS.SALES_ADDRESS --销售地点\n");  
		sql.append("  FROM TM_VEHICLE             TMV,\n");  
		sql.append("       TM_VHCL_MATERIAL_GROUP G,\n");  
		sql.append("       TM_VHCL_MATERIAL_GROUP G1,\n");  
		sql.append("       TM_DEALER_WAREHOUSE    TDW,\n");  
		sql.append("       TT_DEALER_ACTUAL_SALES TDS\n");
		
		sql.append(" WHERE TMV.LIFE_CYCLE ="+Constant.VEHICLE_LIFE_04+" \n");  
		sql.append("   AND TMV.SERIES_ID = G.GROUP_ID\n");  
		sql.append("   AND TMV.MODEL_ID = G1.GROUP_ID\n");  
		sql.append("   AND TMV.VEHICLE_AREA = TDW.WAREHOUSE_ID(+)\n");  
		sql.append("   AND TMV.VEHICLE_ID = TDS.VEHICLE_ID\n");  
		sql.append("   AND TDS.ORDER_ID ="+order_id+"\n");  
		sql.append("AND TDS.IS_RETURN='"+Constant.IF_TYPE_NO+"'");
		return dao.pageQueryMap(sql.toString(), null, dao.getFunName());
	}
	
	
	/***
	 *查询车辆实销客户信息
	 * 
	 * @param :
	 * @return
	 * @throws Exception
	 */
	public static Map<String,String> getSalesCusInfo(String vehicleId){
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT TTC.CTM_TYPE, --客户类型\n");
		sql.append("       TDS.IS_FLEET, --是否集团客户\n");  
		sql.append("       TTC.CTM_FORM, --客户来源\n");  
		sql.append("       TTC.CTM_NAME, --客户姓名\n");  
		sql.append("       TTC.SEX, --性别\n");  
		sql.append("       TTC.CARD_TYPE, --证件类别\n");  
		sql.append("	   TTC.PROVINCE,TTC.CITY,TTC.TOWN, --省份 城市 县\n");
		sql.append("       TTC.CARD_NUM, --证件号码\n");  
		sql.append("       TTC.MAIN_PHONE, --主要联系电话\n");  
		sql.append("       TTC.OTHER_PHONE, --其他联系电话\n");  
		sql.append("       TO_CHAR(TTC.BIRTHDAY,'yyyy-MM-dd')BIRTHDAY,\n");  
		sql.append("       TTC.ADDRESS,\n");  
		sql.append("       TTC.COMPANY_NAME, --公司名称\n");  
		sql.append("       TTC.COMPANY_S_NAME, --公司简称\n");  
		sql.append("       TTC.COMPANY_PHONE, --公司电话\n");  
		sql.append("       TTC.LEVEL_ID, --公司规模\n");  
		sql.append("       TTC.KIND, --公司性质\n");  
		sql.append("       TTC.VEHICLE_NUM, --目前车辆数\n");  
		sql.append("       (select tcp.pact_name\n");  
		sql.append("    from TM_COMPANY_PACT TCP\n");  
		sql.append("    WHERE TDS.FLEET_ID = TCP.PACT_ID) PACT_NAME,");
		sql.append("       (SELECT FLEET_NAME\n");  
		sql.append("          FROM TM_FLEET TMF\n");  
		sql.append("         WHERE TMF.FLEET_ID = TDS.FLEET_ID) FLEET_NAME,\n");  
		sql.append("       (SELECT TFC.CONTRACT_NO\n");  
		sql.append("          FROM TT_FLEET_CONTRACT TFC\n");  
		sql.append("         WHERE TFC.CONTRACT_ID = TDS.CONTRACT_ID) CONTRACT_NO\n");  
		sql.append("  FROM TT_DEALER_ACTUAL_SALES TDS, TT_CUSTOMER TTC\n");  
		sql.append(" WHERE TDS.CTM_ID = TTC.CTM_ID\n");  
		sql.append("   AND TDS.VEHICLE_ID = "+vehicleId+"\n");
		sql.append("   AND TDS.IS_RETURN=' "+Constant.IF_TYPE_NO+"'\n");
		return dao.pageQueryMap(sql.toString(), null, dao.getFunName());
	}
	/***
	 *查询车辆实销客户信息
	 * 
	 * @param :
	 * @return
	 * @throws Exception
	 */
	public static Map<String,String> getSalesCusInfoByOrderId(String order_id){
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT TTC.CTM_TYPE, --客户类型\n");
		sql.append("       TDS.IS_FLEET, --是否集团客户\n");  
		sql.append("       TTC.CTM_FORM, --客户来源\n");  
		sql.append("       TTC.CTM_NAME, --客户姓名\n");  
		sql.append("       TTC.SEX, --性别\n");  
		sql.append("       TTC.CARD_TYPE, --证件类别\n");  
		sql.append("	   TTC.PROVINCE,TTC.CITY,TTC.TOWN, --省份 城市 县\n");
		sql.append("       TTC.CARD_NUM, --证件号码\n");  
		sql.append("       TTC.MAIN_PHONE, --主要联系电话\n");  
		sql.append("       TTC.OTHER_PHONE, --其他联系电话\n");  
		sql.append("       TO_CHAR(TTC.BIRTHDAY,'yyyy-MM-dd')BIRTHDAY,\n");  
		sql.append("       TTC.ADDRESS,\n");  
		sql.append("       TTC.COMPANY_NAME, --公司名称\n");  
		sql.append("       TTC.COMPANY_S_NAME, --公司简称\n");  
		sql.append("       TTC.COMPANY_PHONE, --公司电话\n");  
		sql.append("       TTC.LEVEL_ID, --公司规模\n");  
		sql.append("       TTC.KIND, --公司性质\n");  
		sql.append("       TTC.VEHICLE_NUM, --目前车辆数\n");  
		sql.append("       (select tcp.pact_name\n");  
		sql.append("    from TM_COMPANY_PACT TCP\n");  
		sql.append("    WHERE TDS.FLEET_ID = TCP.PACT_ID) PACT_NAME,");
		sql.append("       (SELECT FLEET_NAME\n");  
		sql.append("          FROM TM_FLEET TMF\n");  
		sql.append("         WHERE TMF.FLEET_ID = TDS.FLEET_ID) FLEET_NAME,\n");  
		sql.append("       (SELECT TFC.CONTRACT_NO\n");  
		sql.append("          FROM TT_FLEET_CONTRACT TFC\n");  
		sql.append("         WHERE TFC.CONTRACT_ID = TDS.CONTRACT_ID) CONTRACT_NO\n");  
		sql.append("  FROM TT_DEALER_ACTUAL_SALES TDS, TT_CUSTOMER TTC\n");  
		sql.append(" WHERE TDS.CTM_ID = TTC.CTM_ID\n");  
		sql.append("   AND TDS.ORDER_ID = "+order_id+"\n");
		sql.append("   AND TDS.IS_RETURN=' "+Constant.IF_TYPE_NO+"'\n");
		return dao.pageQueryMap(sql.toString(), null, dao.getFunName());
	}
	
	public static  PageResult<Map<String, Object>> getReturnReqList_DLR(String dealerIds,String areaId,String vin, String checkstartDate, String checkendDate, String salstartDate, String salendDate, String reqStartDate, String reqEndDate, int pageSize,int curPage) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT G.GROUP_CODE SERIES_CODE,\n");
		sql.append("       G.GROUP_NAME SERIES_NAME,\n");  
		sql.append("       G1.GROUP_CODE MODEL_CODE,\n");  
		sql.append("       G1.GROUP_NAME MODEL_NAME,\n");  
		sql.append("       TMV.VIN,\n");  
		sql.append("       TTR.AUDIT_STATUS,\n");  
		sql.append("       TO_CHAR(TTS.SALES_DATE, 'yyyy-MM-dd') SALES_DATE,\n");  
		sql.append("       TO_CHAR(TTR.REQ_DATE, 'yyyy-MM-dd') REQ_DATE,\n");  
		sql.append("       TTR.STATUS,\n");  
		sql.append("       TO_CHAR(TTR.CHK_DATE, 'yyyy-MM-dd') CHK_DATE,\n");  
		sql.append("       TTR.CHK_REMARK,\n");  
		sql.append("       TCU.NAME NAME,\n");
		sql.append("	   TTR.RETURN_REASON RETURN_REASON,");
		sql.append("	   TTR.RETURN_ID");
		sql.append("  FROM TT_VS_ACTUAL_SALES_RETURN TTR,\n");
		sql.append("       TT_DEALER_ACTUAL_SALES    TTS,\n");  
		sql.append("       TM_VEHICLE                TMV,\n");  
		sql.append("       TM_VHCL_MATERIAL_GROUP    G,\n");  
		sql.append("       TM_VHCL_MATERIAL_GROUP    G1,TC_USER TCU\n");  
		sql.append(" WHERE TTR.ORDER_ID = TTS.ORDER_ID\n");  
		sql.append("   AND TTR.UPDATE_BY =TCU.USER_ID(+)");
		sql.append("   AND TTS.VEHICLE_ID = TMV.VEHICLE_ID\n");  
		sql.append("   AND TMV.SERIES_ID = G.GROUP_ID\n");  
		sql.append("   AND TMV.MODEL_ID = G1.GROUP_ID\n"); 
		sql.append("   AND ttr.DEALER_ID IN ("+dealerIds+")\n");
		if (null != areaId && !"".equals(areaId)) {
			sql.append("   AND TMV.DEALER_ID IN ("+areaId+")\n");  
		}
		if (null != vin && !"".equals(vin)) {
			sql.append(GetVinUtil.getVins(vin, "TMV"));
		}
		
		if (null != checkstartDate && !"".equals(checkstartDate)) {
			sql.append("   AND TTR.CHK_DATE >= TO_DATE('"+ checkstartDate+" 00:00:00','yyyy-MM-dd HH24:MI:SS')\n");
		}
		if (null != checkendDate && !"".equals(checkendDate)) {
			sql.append("   AND TTR.CHK_DATE  <= TO_DATE('"+checkendDate+" 23:59:59','yyyy-MM-dd HH24:MI:SS')\n");
		}
		if (null != salstartDate && !"".equals(salstartDate)) {
			sql.append("   AND TTS.SALES_DATE >= TO_DATE('"+ salstartDate+" 00:00:00','yyyy-MM-dd HH24:MI:SS')\n");
		}
		if (null != salendDate && !"".equals(salendDate)) {
			sql.append("   AND TTS.SALES_DATE  <= TO_DATE('"+salendDate+" 23:59:59','yyyy-MM-dd HH24:MI:SS')\n");
		}
		if (null != reqStartDate && !"".equals(reqStartDate)) {
			sql.append("   AND TTR.REQ_DATE >= TO_DATE('"+ reqStartDate+" 00:00:00','yyyy-MM-dd HH24:MI:SS')\n");
		}
		if (null != reqEndDate && !"".equals(reqEndDate)) {
			sql.append("   AND TTR.REQ_DATE  <= TO_DATE('"+reqEndDate+" 23:59:59','yyyy-MM-dd HH24:MI:SS')\n");
		}
		sql.append(" ORDER BY TTR.CREATE_DATE DESC\n");
		
		return dao.pageQuery(sql.toString(),null,dao.getFunName(),pageSize, curPage);

	}
	
	public static  List<Map<String, Object>> getReturnReqList_DLR(String dealerIds,String areaId,String vin, String checkstartDate, String checkendDate, String salstartDate, String salendDate, String reqStartDate, String reqEndDate) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT G.GROUP_CODE SERIES_CODE,\n");
		sql.append("       G.GROUP_NAME SERIES_NAME,\n");  
		sql.append("       G1.GROUP_CODE MODEL_CODE,\n");  
		sql.append("       G1.GROUP_NAME MODEL_NAME,\n");  
		sql.append("       TMV.VIN,\n");  
		sql.append("       TO_CHAR(TTS.SALES_DATE, 'yyyy-MM-dd') SALES_DATE,\n");  
		sql.append("       TO_CHAR(TTR.REQ_DATE, 'yyyy-MM-dd') REQ_DATE,\n");  
		sql.append("       TTR.STATUS,\n");  
		sql.append("       TO_CHAR(TTR.CHK_DATE, 'yyyy-MM-dd') CHK_DATE,\n");  
		sql.append("       TTR.CHK_REMARK,\n");  
		sql.append("       TCU.NAME NAME,\n");
		sql.append("	   TTR.RETURN_REASON RETURN_REASON");
		sql.append("  FROM TT_VS_ACTUAL_SALES_RETURN TTR,\n");  
		sql.append("       TT_DEALER_ACTUAL_SALES    TTS,\n");  
		sql.append("       TM_VEHICLE                TMV,\n");  
		sql.append("       TM_VHCL_MATERIAL_GROUP    G,\n");  
		sql.append("       TM_VHCL_MATERIAL_GROUP    G1,TC_USER TCU\n");  
		sql.append(" WHERE TTR.ORDER_ID = TTS.ORDER_ID\n");  
		sql.append("   AND TTR.UPDATE_BY =TCU.USER_ID(+)");
		sql.append("   AND TTS.VEHICLE_ID = TMV.VEHICLE_ID\n");  
		sql.append("   AND TMV.SERIES_ID = G.GROUP_ID\n");  
		sql.append("   AND TMV.MODEL_ID = G1.GROUP_ID\n"); 
		sql.append("   AND ttr.DEALER_ID IN ("+dealerIds+")\n");
		if (null != areaId && !"".equals(areaId)) {
			sql.append("   AND TMV.DEALER_ID IN ("+areaId+")\n");  
		}
		if (null != vin && !"".equals(vin)) {
			sql.append(GetVinUtil.getVins(vin, "TMV"));
		}
		if (null != checkstartDate && !"".equals(checkstartDate)) {
			sql.append("   AND TTR.CHK_DATE >= TO_DATE('"+ checkstartDate+" 00:00:00','yyyy-MM-dd HH24:MI:SS')\n");
		}
		if (null != checkendDate && !"".equals(checkendDate)) {
			sql.append("   AND TTR.CHK_DATE  <= TO_DATE('"+checkendDate+" 23:59:59','yyyy-MM-dd HH24:MI:SS')\n");
		}
		if (null != salstartDate && !"".equals(salstartDate)) {
			sql.append("   AND TTS.SALES_DATE >= TO_DATE('"+ salstartDate+" 00:00:00','yyyy-MM-dd HH24:MI:SS')\n");
		}
		if (null != salendDate && !"".equals(salendDate)) {
			sql.append("   AND TTS.SALES_DATE  <= TO_DATE('"+salendDate+" 23:59:59','yyyy-MM-dd HH24:MI:SS')\n");
		}
		if (null != reqStartDate && !"".equals(reqStartDate)) {
			sql.append("   AND TTR.REQ_DATE >= TO_DATE('"+ reqStartDate+" 00:00:00','yyyy-MM-dd HH24:MI:SS')\n");
		}
		if (null != reqEndDate && !"".equals(reqEndDate)) {
			sql.append("   AND TTR.REQ_DATE  <= TO_DATE('"+reqEndDate+" 23:59:59','yyyy-MM-dd HH24:MI:SS')\n");
		}
		sql.append(" ORDER BY TTR.CREATE_DATE DESC\n");
		
		return dao.pageQuery(sql.toString(),null,dao.getFunName());

	}
	
	public static  PageResult<Map<String, Object>> getToReturnVehicleList(String  areaId,String vin,String dealerCode, int pageSize,int curPage) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT TTR.ORDER_ID,\n");
		sql.append("       TTR.RETURN_ID,\n");  
		sql.append("       TMV1.VEHICLE_ID,\n");  
		sql.append("       TMV1.SERIES_CODE,\n");  
		sql.append("       TMV1.SERIES_NAME,\n");  
		sql.append("       TMV1.MODEL_CODE,\n");  
		sql.append("       TMV1.MODEL_NAME,\n");  
		sql.append("       TMV1.VIN,\n");  
		sql.append("       TMV1.DEALER_SHORTNAME,\n");  
		sql.append("       TO_CHAR(TTS.SALES_DATE,'YYYY-MM-DD') SALES_DATE,\n");  
		sql.append("       TO_CHAR(TTR.REQ_DATE,'YYYY-MM-DD') REQ_DATE\n");  
		sql.append("  FROM TT_VS_ACTUAL_SALES_RETURN TTR,\n");  
		sql.append("       TT_DEALER_ACTUAL_SALES TTS,\n");  
		sql.append("       (SELECT TMD.DEALER_ID,\n");  
		sql.append("               TMD.DEALER_SHORTNAME,\n");  
		sql.append("               TMV.VEHICLE_ID,\n");  
		sql.append("               TMV.VIN,\n");  
		sql.append("               G1.GROUP_CODE        SERIES_CODE,\n");  
		sql.append("               G1.GROUP_NAME        SERIES_NAME,\n");  
		sql.append("               G2.GROUP_CODE        MODEL_CODE,\n");  
		sql.append("               G2.GROUP_NAME        MODEL_NAME\n");  
		sql.append("          FROM TM_VEHICLE             TMV,\n");  
		sql.append("               TM_VHCL_MATERIAL_GROUP G1,\n");  
		sql.append("               TM_VHCL_MATERIAL_GROUP G2,\n");  
		sql.append("               TM_DEALER              TMD\n");  
		sql.append("         WHERE TMV.SERIES_ID = G1.GROUP_ID\n");  
		sql.append("           AND TMV.MODEL_ID = G2.GROUP_ID\n");  
		sql.append("           AND TMD.DEALER_ID = TMV.DEALER_ID\n"); 
		sql.append("           AND TMV.AREA_ID = "+areaId+"\n");
		if (null != vin && !"".equals(vin)) {
			sql.append(GetVinUtil.getVins(vin, "TMV"));
		}
		if(null != dealerCode && !"".equals(dealerCode)) {
			String[] array = dealerCode.split(",");
			sql.append("   AND TMD.DEALER_CODE IN (\n");
			for (int i = 0; i < array.length; i++) {
				sql.append("'" + array[i] + "'");
				if (i != array.length - 1) {
					sql.append(",");
				}
			}
			sql.append(")\n");
		}

		sql.append("           AND LIFE_CYCLE = "+Constant.VEHICLE_LIFE_04+") TMV1\n");  
		sql.append(" WHERE TTR.DEALER_ID = TMV1.DEALER_ID\n");  
		sql.append("   AND TTS.ORDER_ID = TTR.ORDER_ID\n");  
		sql.append("   AND TTS.VEHICLE_ID = TMV1.VEHICLE_ID\n");  
		sql.append("   AND TTR.STATUS = "+Constant.RETURN_CHECK_STATUS_01+"\n");
		sql.append("ORDER BY TTR.CREATE_DATE\n");

//		else{
//			sql.append("SELECT TTS.ORDER_ID,\n");
//			sql.append("       TTR.RETURN_ID,\n");  
//			sql.append("       TMV1.VEHICLE_ID,\n");  
//			sql.append("       G.GROUP_CODE SERIES_CODE,\n");  
//			sql.append("       G.GROUP_NAME SERIES_NAME,\n");  
//			sql.append("       G1.GROUP_CODE MODEL_CODE,\n");  
//			sql.append("       G1.GROUP_NAME MODEL_NAME,\n");  
//			sql.append("       TMV1.VIN,\n");  
//			sql.append("       TMD.DEALER_SHORTNAME,\n");  
//			sql.append("       TO_CHAR(TTS.SALES_DATE, 'yyyy-MM-dd') SALES_DATE,\n");  
//			sql.append("       TO_CHAR(TTR.REQ_DATE, 'yyyy-MM-dd') REQ_DATE,\n");  
//			sql.append("       TTR.STATUS,\n");  
//			sql.append("       TTR.RETURN_REASON RETURN_REASON\n");  
//			sql.append("  FROM (SELECT REQ_DATE,\n");  
//			sql.append("               DEALER_ID,\n");  
//			sql.append("               ORDER_ID,\n");  
//			sql.append("               RETURN_ID,\n");  
//			sql.append("               STATUS,\n");  
//			sql.append("               RETURN_REASON,\n");  
//			sql.append("               CREATE_DATE\n");  
//			sql.append("          FROM TT_VS_ACTUAL_SALES_RETURN\n");  
//			sql.append("         WHERE STATUS = "+Constant.RETURN_CHECK_STATUS_01+") TTR,\n");  
//			sql.append("       TM_DEALER TMD,\n");  
//			sql.append("       TM_DEALER_BUSINESS_AREA TDBA,\n");  
//			sql.append("       TT_DEALER_ACTUAL_SALES TTS,\n");  
//			sql.append("       (SELECT VEHICLE_ID, VIN, SERIES_ID, MODEL_ID\n");  
//			sql.append("          FROM TM_VEHICLE TMV\n");  
//			sql.append("         WHERE LIFE_CYCLE = "+Constant.VEHICLE_LIFE_04+"\n");  
//			if (null != vin && !"".equals(vin)) {
//				sql.append(GetVinUtil.getVins(vin, "TMV"));
//			}
//			
//			sql.append("       )TMV1,\n");  
//			sql.append("       TM_VHCL_MATERIAL_GROUP G,\n");  
//			sql.append("       TM_VHCL_MATERIAL_GROUP G1\n");  
//			//sql.append("	   TM_AREA_GROUP TAG	");
//			sql.append(" WHERE TTR.DEALER_ID = TMD.DEALER_ID\n");  
//			if (null != dealerNo && !"".equals(dealerNo)) {
//				sql.append(" AND TMD.DEALER_CODE='"+dealerNo+"'");
//			}
//			// 状态
//			if (null != status && !"".endsWith(status) && !"-1".equals(status)) {
//				sql.append("   AND TTS.STATUS = " + status + "\n");
//			}
//			sql.append("   AND TTR.ORDER_ID = TTS.ORDER_ID\n");  
//			sql.append("   AND TTS.VEHICLE_ID = TMV1.VEHICLE_ID\n");  
//			sql.append("   AND G.GROUP_ID = TMV1.SERIES_ID\n");  
//			sql.append("   AND G1.GROUP_ID = TMV1.MODEL_ID\n");  
//			//sql.append("   AND TAG.MATERIAL_GROUP_ID=G.GROUP_ID\n"); 
//			//if(areaId!=null && areaId!=""){
//			//sql.append("   AND TAG.AREA_ID IN ("+areaId+")\n"); 
//			//}
//			sql.append("   AND TDBA.DEALER_ID=TMD.DEALER_ID\n");
//			sql.append("   AND TMD.DEALER_ID=TTS.DEALER_ID");
//			sql.append("   AND TDBA.AREA_ID=2010010100000004");
//			sql.append(" ORDER BY TTR.CREATE_DATE DESC\n");
//		}

		return dao.pageQuery(sql.toString(),null,dao.getFunName(),pageSize,curPage);

	}
	
	public static Map<String,String> getReturnReason(String return_id){
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT TTR.RETURN_REASON,TTR.RETURN_TYPE\n");
		sql.append("  FROM TT_VS_ACTUAL_SALES_RETURN TTR\n");  
		sql.append(" WHERE TTR.RETURN_ID = "+return_id+"\n");

		return dao.pageQueryMap(sql.toString(), null, dao.getFunName());
	}
	
	public static  PageResult<Map<String, Object>> getReturnCheckList(Long userId,String dutyType, String orgId, String checkstartDate,String checkendDate,String salstartDate,String salendDate,String reqStartDate,String reqEndDate,String entity,String vin,String status,String dealerCode,int pageSize,int curPage) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT TTR.ORDER_ID,\n");
		sql.append("       TTR.RETURN_ID,\n");  
		sql.append("       TMV1.VEHICLE_ID,\n");  
		sql.append("       TMV1.SERIES_CODE,\n");  
		sql.append("       TMV1.SERIES_NAME,\n");  
		sql.append("       TMV1.MODEL_CODE,\n");  
		sql.append("       TMV1.MODEL_NAME,\n");  
		sql.append("       TMV1.VIN,\n");  
		sql.append("       TTR.AUDIT_STATUS,\n  ");
		sql.append("       TMD.DEALER_SHORTNAME,\n");  
		sql.append("       TO_CHAR(TTS.SALES_DATE,'YYYY-MM-DD') SALES_DATE,\n");  
		sql.append("       TO_CHAR(TTR.REQ_DATE,'YYYY-MM-DD') REQ_DATE,\n");  
		sql.append("       TO_CHAR(TTR.CHK_DATE, 'yyyy-MM-dd') CHK_DATE,\n");  
		sql.append("       TTR.STATUS,\n");  
		sql.append("       TTR.CHK_REMARK,\n");  
		sql.append("       TTR.RETURN_REASON RETURN_REASON\n");  
		sql.append("  FROM TT_VS_ACTUAL_SALES_RETURN TTR,\n");  
		sql.append("       TT_DEALER_ACTUAL_SALES TTS,\n"); 
		sql.append("       vw_org_dealer VOD,\n");
		sql.append("       TM_DEALER TMD,\n");
		sql.append("       (SELECT TMV.VEHICLE_ID,\n");  
		sql.append("               TMV.VIN,\n");  
		sql.append("               G1.GROUP_CODE        SERIES_CODE,\n");  
		sql.append("               G1.GROUP_NAME        SERIES_NAME,\n");  
		sql.append("               G2.GROUP_CODE        MODEL_CODE,\n");  
		sql.append("               G2.GROUP_NAME        MODEL_NAME\n");  
		sql.append("          FROM TM_VEHICLE             TMV,\n");  
		//sql.append("               TM_BUSINESS_AREA 	  TBA,\n");  
		sql.append("               TM_VHCL_MATERIAL_GROUP G1,\n");  
		sql.append("               TM_VHCL_MATERIAL_GROUP G2\n");  
		sql.append("         WHERE TMV.SERIES_ID = G1.GROUP_ID\n");  
		//sql.append("           AND TMV.AREA_ID = TBA.AREA_ID\n"); 
		sql.append("           AND TMV.MODEL_ID = G2.GROUP_ID\n");  
		//sql.append("           AND TBA.PRODUCE_BASE IN (").append(entity).append(")\n");
		if (!"".equals(vin)) {
			sql.append(GetVinUtil.getVins(vin, "TMV"));
		}
		// sql.append("           AND TMV.AREA_ID = "+areaId+"\n");  
		sql.append("           ) TMV1\n");  
		sql.append(" WHERE TTR.DEALER_ID = TMD.DEALER_ID\n"); 
		sql.append("   AND TTR.DEALER_ID = VOD.DEALER_ID\n"); 
		sql.append("   AND TTS.ORDER_ID = TTR.ORDER_ID\n");  
		sql.append("   AND TTS.VEHICLE_ID = TMV1.VEHICLE_ID\n");  
		
		if (!"".equals(dealerCode)) {
			if(null != dealerCode && !"".equals(dealerCode)) {
				String[] array = dealerCode.split(",");
				sql.append("   AND TMD.DEALER_CODE IN (\n");
				for (int i = 0; i < array.length; i++) {
					sql.append("'" + array[i] + "'");
					if (i != array.length - 1) {
						sql.append(",");
					}
				}
				sql.append(")\n");
			}
		}
		
//		if(!CommonUtils.isNullString(orgId)) {
//			sql.append("   AND VOD.ROOT_ORG_ID = "+orgId+"\n");
//		}
//		
		if("10431001".equals(dutyType)){
			//车厂查询所有
		}else if("10431003".equals(dutyType)){
			if (null != orgId && !"".equals(orgId)) {
				sql.append("   AND VOD.ROOT_ORG_ID = "+orgId+"\n"); //待大区审核的数据
			}
		}else if("10431004".equals(dutyType)){
			if (null != orgId && !"".equals(orgId)) {
				sql.append("   AND VOD.PQ_ORG_ID = "+orgId+"\n"); //待小区审核的数据
			}
		}
		
		if (!"".equals(status)) {
			sql.append("   AND TTR.AUDIT_STATUS = "+status+"\n");
		}
		if (null != checkstartDate && !"".equals(checkstartDate)) {
			sql.append("   AND TTR.CHK_DATE >= TO_DATE('"+ checkstartDate+" 00:00:00','yyyy-MM-dd HH24:MI:SS')\n");
		}
		if (null != checkendDate && !"".equals(checkendDate)) {
			sql.append("   AND TTR.CHK_DATE  <= TO_DATE('"+checkendDate+" 23:59:59','yyyy-MM-dd HH24:MI:SS')\n");
		}
		if (null != salstartDate && !"".equals(salstartDate)) {
			sql.append("   AND TTS.SALES_DATE >= TO_DATE('"+ salstartDate+" 00:00:00','yyyy-MM-dd HH24:MI:SS')\n");
		}
		if (null != salendDate && !"".equals(salendDate)) {
			sql.append("   AND TTS.SALES_DATE  <= TO_DATE('"+salendDate+" 23:59:59','yyyy-MM-dd HH24:MI:SS')\n");
		}
		
		//2012-03-12 新增申请日期
		if (null != reqStartDate && !"".equals(reqStartDate)) {
			sql.append("   AND TTR.REQ_DATE >= TO_DATE('"+ reqStartDate+" 00:00:00','yyyy-MM-dd HH24:MI:SS')\n");
		}
		if (null != reqEndDate && !"".equals(reqEndDate)) {
			sql.append("   AND TTR.REQ_DATE  <= TO_DATE('"+reqEndDate+" 23:59:59','yyyy-MM-dd HH24:MI:SS')\n");
		}
		
		/*if(Constant.DUTY_TYPE_LARGEREGION.toString().equals(dutyType)) {
			sql.append(getDealerIds(userId, "TTR")) ;
		}*/
		
		sql.append("ORDER BY TTR.CHK_DATE DESC\n");


		
//		if(!areaId.equals("2010010100000004")){}else{
//			sql.append("SELECT TTS.ORDER_ID,\n");
//			sql.append("       TTR.RETURN_ID,\n");  
//			sql.append("       TMV.VEHICLE_ID,\n");  
//			sql.append("       G.GROUP_CODE SERIES_CODE,\n");  
//			sql.append("       G.GROUP_NAME SERIES_NAME,\n");  
//			sql.append("       G1.GROUP_CODE MODEL_CODE,\n");  
//			sql.append("       G1.GROUP_NAME MODEL_NAME,\n");  
//			sql.append("       TMV.VIN,\n");  
//			sql.append("       TMD.DEALER_SHORTNAME,\n");  
//			sql.append("       TO_CHAR(TTS.SALES_DATE, 'yyyy-MM-dd') SALES_DATE,\n");  
//			sql.append("       TO_CHAR(TTR.REQ_DATE, 'yyyy-MM-dd') REQ_DATE,\n");  
//			sql.append("       TO_CHAR(TTR.CHK_DATE, 'yyyy-MM-dd') CHK_DATE,\n");
//			sql.append("       TTR.STATUS,\n");  
//			sql.append("       TTR.CHK_REMARK,\n");  
//			sql.append("       TTR.RETURN_REASON RETURN_REASON\n");  
//			sql.append("  FROM TT_VS_ACTUAL_SALES_RETURN TTR,\n");  
//			sql.append("       TM_DEALER                 TMD,\n");  
//			sql.append("       TT_DEALER_ACTUAL_SALES    TTS,\n");  
//			sql.append("       TM_VEHICLE                TMV,\n");  
//			sql.append("       TM_VHCL_MATERIAL_GROUP    G,\n");  
//			sql.append("       TM_VHCL_MATERIAL_GROUP    G1,\n"); 
//			sql.append("       TM_DEALER_BUSINESS_AREA TDBA");
//			//sql.append("       TM_AREA_GROUP TAG");
//			sql.append(" WHERE TTR.DEALER_ID = TMD.DEALER_ID\n");  
//			//sql.append("   AND   G1.GROUP_ID=TAG.MATERIAL_GROUP_ID\n");
//			sql.append("   AND TTR.ORDER_ID = TTS.ORDER_ID\n");  
//			sql.append("   AND TTS.VEHICLE_ID = TMV.VEHICLE_ID\n");  
//			sql.append("   AND TMV.SERIES_ID = G.GROUP_ID\n");  
//			sql.append("   AND TMV.MODEL_ID = G1.GROUP_ID\n");  
//			sql.append("   AND TDBA.DEALER_ID=TMD.DEALER_ID\n");
//			sql.append("   AND TMD.DEALER_ID=TTS.DEALER_ID\n");
//			sql.append("   AND TDBA.AREA_ID=2010010100000004\n");
//			//sql.append(" AND TAG.AREA_ID IN ("+areaId+")\n");
//			sql.append("   AND TTR.STATUS IN ("+Constant.RETURN_CHECK_STATUS_02+", "+Constant.RETURN_CHECK_STATUS_03+")\n");  
//			//sql.append("   AND TTR.STATUS IN ("+Constant.RETURN_CHECK_STATUS_02+", "+Constant.RETURN_CHECK_STATUS_03+")\n");  
//			if (null != vin && !"".equals(vin)) {
//				sql.append(GetVinUtil.getVins(vin, "TMV"));
//			}
//			// 状态
//			if (null != status && !"".endsWith(status) && !"-1".equals(status)) {
//				//sql.append("   AND TTS.STATUS = " + status + "\n");
//				sql.append("   AND TTR.STATUS = " + status + "\n");
//			}
//			if (null != dealerNo && !"".equals(dealerNo)) {
//				sql.append(" AND TMD.DEALER_CODE='"+dealerNo+"'");
//			}
//			sql.append(" ORDER BY TTR.CREATE_DATE DESC\n");
//		}
		return dao.pageQuery(sql.toString(),null,dao.getFunName(),pageSize,curPage);
	}
	
	/**
	 * 获取实体
	 */
	public static List<Map<String, Object>> getEntity(Long poseId){
		List<Object> params = new ArrayList<Object>() ;
		
		StringBuffer sql = new StringBuffer("\n");

		sql.append("select distinct tba.produce_base, tcc.code_desc\n");
		sql.append("  from tm_business_area tba, tm_pose_business_area tpba, tc_code tcc\n");  
		sql.append(" where tba.area_id = tpba.area_id\n");  
		sql.append("   and tba.produce_base = tcc.code_id\n");  
		sql.append("   and tpba.pose_id = ?\n");
		params.add(poseId) ;

		
		return dao.pageQuery(sql.toString(), params, dao.getFunName());
	}
	
	public static List<Map<String, Object>> getScCostList(String vhclId) {
		StringBuffer sql = new StringBuffer("\n") ;
		
		sql.append("SELECT TAWA.VIN, SUM(NVL(TAWA.BALANCE_AMOUNT, 0)) SUM_B_AMOUNT\n");
		sql.append("  FROM TT_AS_WR_APPLICATION   TAWA,\n");  
		sql.append("       TT_DEALER_ACTUAL_SALES TDAS,\n");  
		sql.append("       TM_VEHICLE             TMV\n");  
		sql.append(" WHERE TAWA.VIN = TMV.VIN\n");  
		sql.append("   AND TDAS.VEHICLE_ID = TMV.VEHICLE_ID\n");  
		sql.append("   AND TDAS.IS_RETURN = ").append(Constant.IF_TYPE_NO).append(" --取得对应车辆最后一次实销的记录\n");  
		sql.append("   AND TDAS.SALES_DATE < TAWA.CREATE_DATE --取得最后一次实销后的车辆索赔记录\n");  
		sql.append("   AND TAWA.STATUS IN (").append(Constant.CLAIM_APPLY_ORD_TYPE_02).append(",").append(Constant.CLAIM_APPLY_ORD_TYPE_03).append(",").append(Constant.CLAIM_APPLY_ORD_TYPE_04).append(",").append(Constant.CLAIM_APPLY_ORD_TYPE_07).append(",").append(Constant.CLAIM_APPLY_ORD_TYPE_08).append(")\n");
		sql.append("   AND TMV.VEHICLE_ID = ").append(vhclId).append(" \n");  
		sql.append(" GROUP BY TAWA.VIN\n");
		
		List<Map<String, Object>> scCostList = dao.pageQuery(sql.toString(), null, dao.getFunName()) ;
		
		return scCostList ;
	}
	
	/**
	 * 判断实销后是否存在售后费用，true表示存在，flase表示不存在。
	 * @param vhclId
	 * @return
	 */
	public static boolean isScCost(String vhclId) {
		List<Map<String, Object>> scCostList = getScCostList(vhclId) ;
		
		if(null == scCostList || scCostList.size() <= 0) {
			return false ;
		} else {
			if(Double.parseDouble(scCostList.get(0).get("SUM_B_AMOUNT").toString()) <= 0) {
				return false ;
			} else {
				return true ;
			}
		}
	}
	
	public static Double getScCost(String vhclId) {
		List<Map<String, Object>> scCostList = getScCostList(vhclId) ;
		Double scCost = new Double("0") ;
		
		if(!CommonUtils.isNullList(scCostList)) {
			scCost = Double.parseDouble(scCostList.get(0).get("SUM_B_AMOUNT").toString()) ;
		}
		
		return scCost ;
	}
	
	
	/**
	 * 判断当前登录用户是否有对售后费用不为0的退车申请审核的权限，true表示有，false表示没有
	 * @param userId
	 * @return
	 */
	public static boolean isChkUser(Long userId) {
		StringBuffer sql = new StringBuffer("\n") ;
		
		sql.append("SELECT TTCHK.USER_ID FROM TT_SALES_RETURN_CHKUSER TTCHK WHERE TTCHK.USER_ID = ").append(userId).append(" \n");
		
		List<Map<String, Object>> chkUserList = dao.pageQuery(sql.toString(), null, dao.getFunName()) ;
		
		return (null != chkUserList && chkUserList.size() > 0) ? true : false ;
	}
	
	public static void updateVechile(Long vehicleId) {
		List<Object> params = new ArrayList<Object>() ;
		
		StringBuffer sql = new StringBuffer("\n") ;
		
		sql.append("UPDATE TM_VEHICLE\n");
		sql.append("   SET LOCK_STATUS   = ?,\n");  
		params.add(Constant.LOCK_STATUS_01) ;
		
		sql.append("       METER_MILE    = ?,\n");  
		params.add(0) ;
		
		sql.append("       FREE_TIMES    = ?,\n");  
		params.add(0) ;
		
		sql.append("       HISTORY_MILE  = ?,\n"); 
		params.add(0) ;
		
		sql.append("       START_MILEAGE = ?,\n");  
		params.add(0) ;
		
		sql.append("       MILEAGE       = ?,\n");  
		params.add(0) ;
		
		sql.append("       LIFE_CYCLE    = ?,\n");  
		params.add(Constant.VEHICLE_LIFE_03) ;
		
		sql.append("       CLAIM_TACTICS_ID = '',\n");  
		//params.add("''") ;
		
		sql.append("       PURCHASED_DATE  = ''\n");  
		//params.add("''") ;
		
		sql.append(" WHERE 1 = 1\n");  
		sql.append("   AND VEHICLE_ID = ?\n");
		params.add(vehicleId) ;
		
		dao.update(sql.toString(), params) ;
	}
	
	public static  PageResult<Map<String, Object>> getToReturnVehicleList_Entity(String dutyType, Long orgId, Long userId, String status, String entity, String vin, String dealerCode, int pageSize, int curPage) {
		List<Object> params = new ArrayList<Object>() ;
		
		StringBuffer sql = new StringBuffer("\n");
		
		sql.append("SELECT TTR.ORDER_ID,\n");
		sql.append("       TTR.RETURN_ID,\n");  
		sql.append("       TMV1.VEHICLE_ID,\n");  
		sql.append("       TMV1.SERIES_CODE,\n");  
		sql.append("       TMV1.SERIES_NAME,\n");  
		sql.append("       TMV1.MODEL_CODE,\n");  
		sql.append("       TMV1.MODEL_NAME,\n");  
		sql.append("       TMV1.VIN,\n");  
		sql.append("       TMV1.DEALER_SHORTNAME,\n");  
		sql.append("       TO_CHAR(TTS.SALES_DATE, 'YYYY-MM-DD') SALES_DATE,\n");  
		sql.append("       TO_CHAR(TTR.REQ_DATE, 'YYYY-MM-DD') REQ_DATE\n");  
		sql.append("  FROM TT_VS_ACTUAL_SALES_RETURN TTR,\n");  
		sql.append("       TT_DEALER_ACTUAL_SALES TTS,\n");  
		sql.append("       VW_ORG_DEALER	VOD,\n");
		sql.append("       (SELECT TMD.DEALER_ID,\n");  
		sql.append("               TMD.DEALER_SHORTNAME,\n");  
		sql.append("               TMV.VEHICLE_ID,\n");  
		sql.append("               TMV.VIN,\n");  
		sql.append("               G1.GROUP_CODE        SERIES_CODE,\n");  
		sql.append("               G1.GROUP_NAME        SERIES_NAME,\n");  
		sql.append("               G2.GROUP_CODE        MODEL_CODE,\n");  
		sql.append("               G2.GROUP_NAME        MODEL_NAME\n");  
		sql.append("          FROM TM_VEHICLE             TMV,\n");  
		sql.append("               TM_VHCL_MATERIAL_GROUP G1,\n");  
		sql.append("               TM_VHCL_MATERIAL_GROUP G2,\n");  
		sql.append("               TM_DEALER              TMD\n");  
		sql.append("         WHERE TMV.SERIES_ID = G1.GROUP_ID\n");  
		sql.append("           AND TMV.MODEL_ID = G2.GROUP_ID\n");  
		sql.append("           AND TMD.DEALER_ID = TMV.DEALER_ID\n");  
		
		if(null != dealerCode && !"".equals(dealerCode)) {
			dealerCode = "'" + dealerCode.replaceAll(",", "','") + "'" ;
			sql.append("   AND TMD.DEALER_CODE IN (").append(dealerCode).append(")\n");
		}
		
		sql.append("           AND LIFE_CYCLE = ?) TMV1\n"); 
		params.add(Constant.VEHICLE_LIFE_04) ;
		
		sql.append(" WHERE TTR.DEALER_ID = TMV1.DEALER_ID\n");  
		sql.append("   AND TTR.DEALER_ID = VOD.DEALER_ID\n");  
		sql.append("   AND TTS.ORDER_ID = TTR.ORDER_ID\n");  
		sql.append("   AND TTS.VEHICLE_ID = TMV1.VEHICLE_ID\n");  
		sql.append("   AND TTR.dealer_id = VOD.dealer_id\n");  

//		sql.append("   AND TTR.STATUS = ?\n");  
//		params.add(status) ;
		
		if (null != vin && !"".equals(vin)) {
			sql.append(GetVinUtil.getVins(vin, "TMV1"));
		}
		
		
		if("10431001".equals(dutyType)){
				//待车厂审核的数据
				sql.append(" AND TTR.AUDIT_STATUS=19941003");
		}else if("10431003".equals(dutyType)){
			if (null != orgId && orgId > 0) {
				sql.append("   AND VOD.root_org_id = ?\n"); //待大区审核的数据
				sql.append(" AND TTR.AUDIT_STATUS=19941001 ");
				params.add(orgId) ;
			}
		}else if("10431004".equals(dutyType)){
			if (null != orgId && orgId > 0) {
				sql.append("   AND VOD.PQ_ORG_ID = ?\n"); //待小区审核的数据
				sql.append(" AND TTR.AUDIT_STATUS=19941001 ");
				params.add(orgId) ;
			}
		}
		/*if(Constant.DUTY_TYPE_LARGEREGION.toString().equals(dutyType)) {
			sql.append(getDealerIds(userId, "TTS")) ;
		}*/
		
		sql.append(" ORDER BY TTR.CREATE_DATE\n");
		
		return dao.pageQuery(sql.toString(), params, dao.getFunName(), pageSize, curPage);
	}
	
	public List<Map<String, Object>> getActualSaleReturnList(Map<String, Object> map) {
		List<Object> params = new ArrayList<Object>() ;
		StringBuffer sql = new StringBuffer();
		
		String vin =  (String) map.get("vin");
		String dealerCode =  (String) map.get("dealerCode");
		String status = (String) map.get("status");					//状态
		String checkstartDate = (String) map.get("checkstartDate");		//审核开始日期
		String checkendDate = (String) map.get("checkendDate");  		//审核结束日期
		String salstartDate = (String) map.get("salstartDate");//实销开始日期
		String salendDate = (String) map.get("salendDate");//实销结束日期
		//2012-03-12 新增申请日期
		String reqStartDate = (String) map.get("reqStartDate"); //申请开始日期
		String reqEndDate = (String) map.get("reqEndDate"); //申请开始日期
		String orgId = (String) map.get("orgId") ;	// 大区id
		String entity = (String) map.get("entity");
		AclUserBean logonUser = (AclUserBean) map.get("logonUser");
		String dutyType = logonUser.getDutyType() ;
		
		if(Constant.DUTY_TYPE_LARGEREGION.toString().equals(dutyType)) {
			orgId = logonUser.getOrgId().toString() ;
		}
		
		if(CommonUtils.isNullString(entity)) {
			entity = getEntityStr(logonUser) ;
		}
		
		if (null != dealerCode && !"".equals(dealerCode)) {
			dealerCode = dealerCode.trim();
		}
		if (null != vin && !"".equals(vin)) {
			vin = vin.trim();
		}
		sql.append("SELECT TTR.ORDER_ID,\n");
		sql.append("       TTR.RETURN_ID,\n");  
		sql.append("       TMV1.VEHICLE_ID,\n");  
		sql.append("       TMV1.SERIES_CODE,\n");  
		sql.append("       TMV1.SERIES_NAME,\n");  
		sql.append("       TMV1.MODEL_CODE,\n");  
		sql.append("       TMV1.MODEL_NAME,\n");  
		sql.append("       TMV1.VIN,\n");  
		sql.append("       TMD.DEALER_SHORTNAME,\n");  
		sql.append("       TO_CHAR(TTS.SALES_DATE,'YYYY-MM-DD') SALES_DATE,\n");  
		sql.append("       TO_CHAR(TTR.REQ_DATE,'YYYY-MM-DD') REQ_DATE,\n");  
		sql.append("       TO_CHAR(TTR.CHK_DATE, 'yyyy-MM-dd') CHK_DATE,\n");  
		sql.append("       TTR.STATUS,\n");  
		sql.append("       TTR.CHK_REMARK,\n");  
		sql.append("       TTR.RETURN_REASON RETURN_REASON\n");  
		sql.append("  FROM TT_VS_ACTUAL_SALES_RETURN TTR,\n");  
		sql.append("       TT_DEALER_ACTUAL_SALES TTS,\n"); 
		sql.append("       vw_org_dealer VOD,\n");
		sql.append("               TM_DEALER              TMD,\n");  
		sql.append("       (SELECT \n");  
		//sql.append("               TMD.DEALER_SHORTNAME,\n");  
		sql.append("               TMV.VEHICLE_ID,\n");  
		sql.append("               TMV.VIN,\n");  
		sql.append("               G1.GROUP_CODE        SERIES_CODE,\n");  
		sql.append("               G1.GROUP_NAME        SERIES_NAME,\n");  
		sql.append("               G2.GROUP_CODE        MODEL_CODE,\n");  
		sql.append("               G2.GROUP_NAME        MODEL_NAME\n");  
		sql.append("          FROM TM_VEHICLE             TMV,\n");  
		sql.append("               TM_BUSINESS_AREA 	  TBA,\n");  
		sql.append("               TM_VHCL_MATERIAL_GROUP G1,\n");  
		sql.append("               TM_VHCL_MATERIAL_GROUP G2\n");  
	//	sql.append("               TM_DEALER              TMD\n");  
		sql.append("         WHERE TMV.SERIES_ID = G1.GROUP_ID\n");  
		sql.append("           AND TMV.AREA_ID = TBA.AREA_ID\n"); 
		sql.append("           AND TMV.MODEL_ID = G2.GROUP_ID\n");  
		//sql.append("           AND TMD.DEALER_ID = TMV.DEALER_ID\n");
		sql.append("           AND TBA.PRODUCE_BASE IN (").append(entity).append(")\n");
		if (!"".equals(vin)) {
			sql.append(GetVinUtil.getVins(vin, "TMV"));
		}
		if (!"".equals(dealerCode)) {
			if(null != dealerCode && !"".equals(dealerCode)) {
				String[] array = dealerCode.split(",");
				sql.append("   AND TMD.DEALER_CODE IN (\n");
				for (int i = 0; i < array.length; i++) {
					sql.append("'" + array[i] + "'");
					if (i != array.length - 1) {
						sql.append(",");
					}
				}
				sql.append(")\n");
			}
		}
		// sql.append("           AND TMV.AREA_ID = "+areaId+"\n");  
		sql.append("           ) TMV1\n");  
		sql.append(" WHERE TTR.DEALER_ID = TMD.DEALER_ID\n"); 
		sql.append("   AND TTR.DEALER_ID = VOD.DEALER_ID\n"); 
		sql.append("   AND TTS.ORDER_ID = TTR.ORDER_ID\n");  
		sql.append("   AND TTS.VEHICLE_ID = TMV1.VEHICLE_ID\n");  
		
		if(!CommonUtils.isNullString(orgId)) {
			sql.append("   AND VOD.ROOT_ORG_ID = "+orgId+"\n");
		}
		
		if (!"".equals(status)) {
			sql.append("   AND TTR.AUDIT_STATUS = "+status+"\n");
		}
		if (null != checkstartDate && !"".equals(checkstartDate)) {
			sql.append("   AND TTR.CHK_DATE >= TO_DATE('"+ checkstartDate+" 00:00:00','yyyy-MM-dd HH24:MI:SS')\n");
		}
		if (null != checkendDate && !"".equals(checkendDate)) {
			sql.append("   AND TTR.CHK_DATE  <= TO_DATE('"+checkendDate+" 23:59:59','yyyy-MM-dd HH24:MI:SS')\n");
		}
		if (null != salstartDate && !"".equals(salstartDate)) {
			sql.append("   AND TTS.SALES_DATE >= TO_DATE('"+ salstartDate+" 00:00:00','yyyy-MM-dd HH24:MI:SS')\n");
		}
		if (null != salendDate && !"".equals(salendDate)) {
			sql.append("   AND TTS.SALES_DATE  <= TO_DATE('"+salendDate+" 23:59:59','yyyy-MM-dd HH24:MI:SS')\n");
		}
		
		//2012-03-12 新增申请日期
		if (null != reqStartDate && !"".equals(reqStartDate)) {
			sql.append("   AND TTR.REQ_DATE >= TO_DATE('"+ reqStartDate+" 00:00:00','yyyy-MM-dd HH24:MI:SS')\n");
		}
		if (null != reqEndDate && !"".equals(reqEndDate)) {
			sql.append("   AND TTR.REQ_DATE  <= TO_DATE('"+reqEndDate+" 23:59:59','yyyy-MM-dd HH24:MI:SS')\n");
		}
		sql.append("ORDER BY TTR.CHK_DATE DESC\n");
		return dao.pageQuery(sql.toString(), params, dao.getFunName());
	}
	
	public String getEntityStr(AclUserBean logonUser) {
		
		List<Map<String, Object>> salesRetList = SalesVehicleReturnDAO.getEntity(logonUser.getPoseId()) ;
		
		StringBuffer entityStr = new StringBuffer("") ;
		
		if(!CommonUtils.isNullList(salesRetList)) {
			int len = salesRetList.size() ;
			
			for(int i=0; i<len; i++) {
				if(CommonUtils.isNullString(entityStr.toString())) {
					entityStr.append(salesRetList.get(i).get("PRODUCE_BASE").toString()) ;
				} else {
					entityStr.append(",").append(salesRetList.get(i).get("PRODUCE_BASE").toString()) ;
				}
			}
		}
		
		return entityStr.toString() ;
	}
	public static  List<Map<String, Object>> getReturnChks(String returnId) {
        StringBuilder sql= new StringBuilder();
        sql.append("SELECT TO_CHAR(TRC.CHK_DATE,'YYYY-MM-DD') CHK_DATE,\n" );
        sql.append("TRC.CHK_DESC,\n" );
        sql.append("TRC.STATUS,");
        sql.append(" TU.NAME, TMO.ORG_NAME\n" );
        sql.append("  FROM TT_RETURN_CHK TRC, TC_USER TU, TM_ORG TMO\n" );
        sql.append(" WHERE TRC.CHK_BY = TU.USER_ID\n" );
        sql.append("   AND TRC.CHK_ORG_ID = TMO.ORG_ID\n" );
        sql.append("   AND TRC.RETURN_ID ="+returnId+"\n");


		return dao.pageQuery(sql.toString(),null,dao.getFunName());

	}
	
	
	/*
	 * 通过 vin查看是否有维修数据
	 * */
	public  List<Map<String, Object>> getRepareRecorde(String vin) {
        StringBuilder sql= new StringBuilder();
        sql.append("select count(1) from tt_as_repair_order  r where r.vin = \'"+vin+"\' and r.repair_type_code!=11441008 \n" );
		return dao.pageQuery(sql.toString(),null,dao.getFunName());
	}
	
	/**
	 * 售后查询
	 * */
	public static  PageResult<Map<String, Object>> salePartmentGetToReturnVehicleList_Entity(String dutyType, Long orgId, Long userId, String status, String entity, String vin, String dealerCode, int pageSize, int curPage) {
		List<Object> params = new ArrayList<Object>() ;
		
		StringBuffer sql = new StringBuffer("\n");
		
		sql.append("SELECT TTR.ORDER_ID,\n");
		sql.append("       TTR.RETURN_ID,\n");  
		sql.append("       TMV1.VEHICLE_ID,\n");  
		sql.append("       TMV1.SERIES_CODE,\n");  
		sql.append("       TMV1.SERIES_NAME,\n");  
		sql.append("       TMV1.MODEL_CODE,\n");  
		sql.append("       TMV1.MODEL_NAME,\n");  
		sql.append("       TMV1.VIN,\n");  
		sql.append("       TMV1.DEALER_SHORTNAME,\n");  
		sql.append("       TO_CHAR(TTS.SALES_DATE, 'YYYY-MM-DD') SALES_DATE,\n");  
		sql.append("       TO_CHAR(TTR.REQ_DATE, 'YYYY-MM-DD') REQ_DATE\n");  
		sql.append("  FROM TT_VS_ACTUAL_SALES_RETURN TTR,\n");  
		sql.append("       TT_DEALER_ACTUAL_SALES TTS,\n");  
		sql.append("       VW_ORG_DEALER	VOD,\n");
		sql.append("       (SELECT TMD.DEALER_ID,\n");  
		sql.append("               TMD.DEALER_SHORTNAME,\n");  
		sql.append("               TMV.VEHICLE_ID,\n");  
		sql.append("               TMV.VIN,\n");  
		sql.append("               G1.GROUP_CODE        SERIES_CODE,\n");  
		sql.append("               G1.GROUP_NAME        SERIES_NAME,\n");  
		sql.append("               G2.GROUP_CODE        MODEL_CODE,\n");  
		sql.append("               G2.GROUP_NAME        MODEL_NAME\n");  
		sql.append("          FROM TM_VEHICLE             TMV,\n");  
		sql.append("               TM_VHCL_MATERIAL_GROUP G1,\n");  
		sql.append("               TM_VHCL_MATERIAL_GROUP G2,\n");  
		sql.append("               TM_DEALER              TMD\n");  
		sql.append("         WHERE TMV.SERIES_ID = G1.GROUP_ID\n");  
		sql.append("           AND TMV.MODEL_ID = G2.GROUP_ID\n");  
		sql.append("           AND TMD.DEALER_ID = TMV.DEALER_ID\n");  
		
		if(null != dealerCode && !"".equals(dealerCode)) {
			dealerCode = "'" + dealerCode.replaceAll(",", "','") + "'" ;
			sql.append("   AND TMD.DEALER_CODE IN (").append(dealerCode).append(")\n");
		}
		
		sql.append("           AND LIFE_CYCLE = ?) TMV1\n"); 
		params.add(Constant.VEHICLE_LIFE_04) ;
		
		sql.append(" WHERE TTR.DEALER_ID = TMV1.DEALER_ID\n");  
		sql.append("   AND TTR.DEALER_ID = VOD.DEALER_ID\n");  
		sql.append("   AND TTS.ORDER_ID = TTR.ORDER_ID\n");  
		sql.append("   AND TTS.VEHICLE_ID = TMV1.VEHICLE_ID\n");  
		sql.append("   AND TTR.dealer_id = VOD.dealer_id\n");  

//		sql.append("   AND TTR.STATUS = ?\n");  
//		params.add(status) ;
		
		if (null != vin && !"".equals(vin)) {
			sql.append(GetVinUtil.getVins(vin, "TMV1"));
		}
		
		
		if("10431001".equals(dutyType)){
				//待车厂审核的数据
				sql.append(" AND TTR.AUDIT_STATUS=19941006");
		}else if("10431003".equals(dutyType)){
			if (null != orgId && orgId > 0) {
				sql.append("   AND VOD.root_org_id = ?\n"); //待大区审核的数据
				sql.append(" AND TTR.AUDIT_STATUS=19941001 ");
				params.add(orgId) ;
			}
		}else if("10431004".equals(dutyType)){
			if (null != orgId && orgId > 0) {
				sql.append("   AND VOD.PQ_ORG_ID = ?\n"); //待小区审核的数据
				sql.append(" AND TTR.AUDIT_STATUS=19941001 ");
				params.add(orgId) ;
			}
		}
		/*if(Constant.DUTY_TYPE_LARGEREGION.toString().equals(dutyType)) {
			sql.append(getDealerIds(userId, "TTS")) ;
		}*/
		
		sql.append(" ORDER BY TTR.CREATE_DATE\n");
		
		return dao.pageQuery(sql.toString(), params, dao.getFunName(), pageSize, curPage);
	}
	
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}

	
}