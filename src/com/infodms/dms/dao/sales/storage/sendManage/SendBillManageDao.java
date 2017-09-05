package com.infodms.dms.dao.sales.storage.sendManage;

import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.infodms.dms.common.Constant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.util.XHBUtil;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

import flex.messaging.io.ArrayList;

/**
 * 运单管理Dao
 * 
 * @author Administrator
 *         2013-5-16
 */
public class SendBillManageDao extends BaseDao<PO> {
	private static final SendBillManageDao dao = new SendBillManageDao();
	
	public static final SendBillManageDao getInstance()
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
	 * 分页查询需回厂确认运单列表
	 * 
	 * @param map
	 * @param curPage
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	public PageResult<Map<String, Object>> getSendBillReturnList(Map<String, Object> map, int curPage, int pageSize)
					throws Exception
	{
		String dealerCode = (String) map.get("dealerCode");
		String billNo = (String) map.get("billNo");
		String areaId = (String) map.get("areaId");
		String poseId = (String) map.get("poseId");
		String logiName = (String) map.get("logiName");
		
		String sendStartDate=(String)map.get("sendStartDate");
		String sendEndDate=(String)map.get("sendEndDate");
		
		List<Object> params = new LinkedList<Object>();
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("SELECT A.BILL_NO,\n");
		sbSql.append("       A.BILL_ID,\n");
		sbSql.append("       B.LOGI_ID,\n");
		sbSql.append("       TMD.DEALER_CODE,\n");
		sbSql.append("       TMD.DEALER_NAME,\n");
//		sbSql.append("       A.INVOICE,\n");
		sbSql.append("       TO_CHAR(A.BILL_CRT_DATE, 'YYYY-MM-DD') BILL_CRT_DATE,\n");
		sbSql.append("       B.LOGI_FULL_NAME,\n");
		sbSql.append("       B.LOGI_NAME,\n");
		sbSql.append("       A.VEH_NUM,\n");
		sbSql.append("       A.ADDRESS_ID,\n");
		sbSql.append("       NVL(TD.BACKCRM_NUM,0) BACKCRM_NUM");
		sbSql.append("  FROM TT_SALES_WAYBILL A LEFT JOIN (SELECT td.bill_id,COUNT(1) backcrm_num FROM TT_SALES_BACKCRM_DETAIL td WHERE td.STATUS="+Constant.STATUS_ENABLE+" group BY td.bill_id) td ON a.bill_id=td.bill_id, TT_SALES_LOGI B, TM_DEALER TMD\n");
		sbSql.append(" WHERE A.LOGI_ID = B.LOGI_ID\n");
		sbSql.append("   AND A.send_DEALER_ID = TMD.DEALER_ID\n");
		sbSql.append("   AND NOT EXISTS (SELECT 1 FROM TT_SALES_BO_DETAIL WHERE BILL_ID = A.BILL_ID AND SEND_TYPE = " + Constant.TRANSPORT_TYPE_01 + ")\n");
		sbSql.append("   AND A.STATUS = " + Constant.STATUS_ENABLE + "\n");
		sbSql.append("   AND  A.SEND_STATUS = " + Constant.SEND_STATUS_01 + " \n");
		sbSql.append("   AND  A.IS_CONFIRM = " + Constant.IF_TYPE_YES + " \n");
		sbSql.append(MaterialGroupManagerDao.getPoseIdBusinessSqlByPar("A.AREA_ID", poseId,params));
		if (!"".equals(dealerCode))
		{
			sbSql.append(Utility.getConSqlByParamForEqual(dealerCode, params,"TMD", "DEALER_CODE"));
		}
		if (!"".equals(billNo))
		{
			sbSql.append(" AND A.BILL_NO LIKE ?\n");
			params.add("%"+billNo+"%");
		}
		if (!"".equals(areaId))
		{
			sbSql.append(" AND A.AREA_ID = ?\n");
			params.add(areaId);
		}
		if (!"".equals(logiName))
		{
			sbSql.append(" AND A.LOGI_ID = ?\n");
			params.add(logiName);
		}
		if(!"".equals(sendStartDate)){
			sbSql.append(" AND A.BILL_CRT_DATE>=TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')\n");
			params.add(sendStartDate+" 00:00:00");
		}
		if(!"".equals(sendEndDate)){
			sbSql.append(" AND A.BILL_CRT_DATE<=TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')\n");
			params.add(sendEndDate+" 23:59:59");
		}
		sbSql.append("    ORDER BY A.BILL_ID \n");
		PageResult<Map<String, Object>> ps = dao.pageQuery(sbSql.toString(), params, getFunName(), pageSize, curPage);
		return ps;
	}
	
	/**
	 * 回厂确认运单详细信息
	 * 
	 * @param billId
	 * @return
	 */
	public Map<String, Object> getSendBillInfoById(Long billId)
	{
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT  A.BILL_NO,A.BILL_ID,B.LOGI_ID,A.SEND_DEALER_ID,A.ADDRESS_ID, \n");
		sql.append("         TMD.DEALER_CODE,\n");
		sql.append("         TMD.DEALER_NAME,\n");
		sql.append("         A.INVOICE,\n");
		sql.append("         TO_CHAR(A.BILL_CRT_DATE,'yyyy-MM-dd') BILL_CRT_DATE,\n");
		sql.append("         A.BILL_CRT_DATE CREATE_DATE,\n");
		sql.append("         B.LOGI_FULL_NAME,\n");
		sql.append("         B.LOGI_NAME,\n");
		sql.append("         C.NAME, \n");
		sql.append("         TR.REGION_ID CITY_ID,\n");
		sql.append("         A.AREA_ID,");
		sql.append("         A.VEH_NUM, \n");
		sql.append("         NVL(TD.BACKCRM_NUM,0) BACKCRM_NUM,");
		sql.append("         A.VEH_NUM-NVL(TD.BACKCRM_NUM,0) NO_CRM_NUM");
		sql.append("   FROM  TT_SALES_WAYBILL A\n");
		sql.append("         LEFT JOIN (SELECT td.bill_id,COUNT(1) backcrm_num FROM TT_SALES_BACKCRM_DETAIL td WHERE td.STATUS="+Constant.STATUS_ENABLE+" group BY td.bill_id) td ON a.bill_id=td.bill_id,");
		sql.append("         TT_SALES_LOGI B,");
		sql.append("         TC_USER C,");
		sql.append("         TM_REGION TR,");
		sql.append("         TM_VS_ADDRESS D, \n");
		sql.append("         TM_DEALER TMD");
		sql.append("  WHERE  A.LOGI_ID = B.LOGI_ID \n");
		sql.append("    AND  A.SEND_DEALER_ID = TMD.DEALER_ID \n");
		sql.append("    AND  A.BILL_CRT_PER = C.USER_ID \n");
		sql.append("    AND  A.ADDRESS_ID = D.ID \n");
		sql.append("    AND  D.AREA_ID = TR.REGION_CODE \n");
		sql.append("    AND  A.BILL_ID = ?\n");
		List<Object> params = new LinkedList<Object>();
		params.add(billId);
		return dao.pageQueryMap(sql.toString(), params, dao.getFunName());
	}
	
	/**
	 * 分页查询需要结算的运单列表
	 * 
	 * @param map
	 * @param curPage
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	public PageResult<Map<String, Object>> getSendBillSettlementList(Map<String, Object> map, int curPage, int pageSize)
					throws Exception
	{
		String dealerCode = (String) map.get("dealerCode");
		String billNo = (String) map.get("billNo");
		String areaId = (String) map.get("areaId");
		String poseId = (String) map.get("poseId");
		String logiId = (String) map.get("logiId");
		String countyId = (String)map.get("countyId");// 区县
		String cityId = (String)map.get("CITY_ID");// 地市
		String provinceId = (String)map.get("PROVINCE_ID");// 省份
		String hasDistance = (String) map.get("hasDistance");
		String sendStartDate=(String)map.get("sendStartDate");
		String sendEndDate=(String)map.get("sendEndDate");
		String isSettlement =  (String)map.get("isSettlement");
		String lastOutDateStart = (String)map.get("lastOutDateStart");
		String lastOutDateEnd  =  (String)map.get("lastOutDateEnd");
		String vin = (String)map.get("vin");
		
		List<Object> params = new LinkedList<Object>();
		params.add(Constant.TRANSPORT_TYPE_01);
		params.add(Constant.STATUS_ENABLE);
		
		StringBuffer sql = new StringBuffer();
		
		sql.append("SELECT distinct A.BILL_NO,\n");
		sql.append("       A.BILL_ID,\n");
		sql.append("       B.LOGI_ID,\n");
		sql.append("       A.AREA_ID,\n");
		sql.append("       TMD.DEALER_CODE,\n");
		sql.append("       TMD.DEALER_NAME,\n");
		sql.append("       A.INVOICE,\n");
		sql.append("       TO_CHAR(A.BILL_CRT_DATE, 'YYYY-MM-DD') BILL_CRT_DATE,\n");
		sql.append("       TO_CHAR(A.CONFIRM_DATE, 'YYYY-MM-DD') CONFIRM_DATE,\n");
		//sql.append("       TO_CHAR(A.BACK_CRM_DATE, 'YYYY-MM-DD') BACK_CRM_DATE,\n");
		sql.append("       B.LOGI_FULL_NAME,\n");
		sql.append("       B.LOGI_NAME,\n");
		sql.append("       TSBD.VEHICLE_ID,TV.VIN,VW.SERIES_NAME,VW.MODEL_NAME,VW.PACKAGE_NAME,VW.COLOR_NAME,\n");
		sql.append("       tsbd.back_crm_date,F_GET_TCUSER_NAME(tsbd.back_crm_per) back_crm_per,\n");
		sql.append("       TO_CHAR(ad.out_date, 'YYYY-MM-DD') out_date,");
		sql.append("       A.VEH_NUM,TBA.AREA_NAME, TR2.REGION_NAME|| TR1.REGION_NAME || TR3.REGION_NAME ADDRESS, \n");
		sql.append("       (SELECT T.ARRIVE_DAYS*24\n");
		sql.append("         FROM TT_SALES_CITY_DIS T\n");
		sql.append("          WHERE T.YIELDLY = A.AREA_ID AND T.CITY_ID = TR3.REGION_ID) HD_HOURSE,\n");
		sql.append("	   ROUND(TO_NUMBER(tsbd.back_crm_date - (A.CONFIRM_DATE+(SELECT MIN(TI.ASS_DAYS)\n" );
		sql.append("           FROM TT_SALES_LOGI_INTREVAL TI\n" );
		sql.append("            WHERE TI.STATUS = 10011001\n" );
		sql.append("            AND A.VEH_NUM > TI.BEGIN_NUM\n" );
		sql.append("            AND A.VEH_NUM <= TI.END_NUM)))*24) SJ_HOURSE,\n");
		sql.append("	   ROUND(TO_NUMBER(tsbd.back_crm_date - (A.CONFIRM_DATE+(SELECT MIN(TI.ASS_DAYS)\n" );
		sql.append("           FROM TT_SALES_LOGI_INTREVAL TI\n" );
		sql.append("            WHERE TI.STATUS = 10011001\n" );
		sql.append("            AND A.VEH_NUM > TI.BEGIN_NUM\n" );
		sql.append("            AND A.VEH_NUM <= TI.END_NUM)))*24) -\n");
		sql.append("       (SELECT T.ARRIVE_DAYS*24\n");
		sql.append("         FROM TT_SALES_CITY_DIS T\n");
		sql.append("          WHERE T.YIELDLY = A.AREA_ID AND T.CITY_ID = TR3.REGION_ID) CQ_HOURSE\n");
		sql.append("  FROM TT_SALES_WAYBILL A, TT_SALES_LOGI B, TM_DEALER TMD,tt_sales_alloca_de ad, tt_sales_bo_detail bd,\n");
		sql.append("  TM_VS_ADDRESS TVA, TM_REGION TR1,TM_REGION TR2,TM_REGION TR3 ,TM_BUSINESS_AREA TBA, \n");
		sql.append("  TT_SALES_BACKCRM_DETAIL TSBD,TM_VEHICLE TV,VW_MATERIAL_GROUP_MAT VW");
		sql.append(" WHERE A.LOGI_ID = B.LOGI_ID\n");
		sql.append("   AND ad.bo_de_id = bd.bo_de_id \n");
		sql.append("   and bd.bill_id = a.bill_id \n");
		sql.append("   AND A.SEND_DEALER_ID = TMD.DEALER_ID\n");
		sql.append("   AND A.BILL_ID = TSBD.BILL_ID\n");
		sql.append("   AND TSBD.STATUS=10011001\n");
		sql.append("   AND NVL(TSBD.BAL_ID,0)<=0\n");
		sql.append("   AND TSBD.VEHICLE_ID=TV.VEHICLE_ID\n");
		sql.append("   AND TV.MATERIAL_ID=VW.MATERIAL_ID");
		sql.append("   AND NOT EXISTS (SELECT 1 FROM TT_SALES_BO_DETAIL WHERE BILL_ID = A.BILL_ID AND SEND_TYPE = ?) \n");
		sql.append("   AND B.LOGI_NAME != '自提'\n");
		sql.append("   AND A.STATUS = ?\n");
		//sql.append("   AND (A.BAL_ID IS NULL OR A.BAL_ID = -1)");
		sql.append("   AND A.ADDRESS_ID = TVA.ID AND TVA.CITY_ID = TR1.REGION_CODE AND TVA.PROVINCE_ID = TR2.REGION_CODE AND TBA.AREA_ID = A.AREA_ID AND TVA.AREA_ID = TR3.REGION_CODE \n");
		sql.append(MaterialGroupManagerDao.getPoseIdBusinessSqlByPar("A.AREA_ID", poseId,params));
		
		if (null != dealerCode && !"".equals(dealerCode))
		{
			sql.append(Utility.getConSqlByParamForEqual(dealerCode, params,"TMD", "DEALER_CODE"));
		}
		if (billNo != null && !"".equals(billNo))
		{
			sql.append(" AND A.BILL_NO LIKE ?\n");
			params.add("%"+billNo+"%");
		}
		if (areaId != null && !"".equals(areaId))
		{
			sql.append(" AND A.AREA_ID = ?\n");
			params.add(areaId);
		}
		if (logiId != null && !"".equals(logiId))
		{
			sql.append(" AND A.LOGI_ID =?\n");
			params.add(logiId);
		}
		if(provinceId!=null&&!"".equals(provinceId)){
			sql.append(" AND TR2.REGION_CODE =?\n");	
			params.add(provinceId);
		}
		if(cityId!=null&&!"".equals(cityId)){
			sql.append(" AND TR1.REGION_CODE=?\n");
			params.add(cityId);
		}
		if(countyId!=null&&!"".equals(countyId)){
			sql.append(" AND TR3.REGION_NAME LIKE ?\n");
			params.add("%"+countyId+"%");
		}
		
		if(hasDistance!=null&&!"".equals(hasDistance)){
			if("yes".equals(hasDistance)){
				sql.append("AND  EXISTS(select 1 from TT_SALES_CITY_DIS dis ");
				sql.append("WHERE dis.province_id = TR2.REGION_ID and TR3.REGION_ID  = dis.city_id  )");
			}else if("no".equals(hasDistance)){
				sql.append("AND NOT EXISTS(select 1 from TT_SALES_CITY_DIS dis ");
				sql.append("WHERE dis.province_id = TR2.REGION_ID and TR3.REGION_ID  = dis.city_id  )");
			}
		}
		
		if(isSettlement!=null&&!"".equals(isSettlement)){
		
			if("no".equals(isSettlement)){
				sql.append("   AND A.SEND_STATUS = ?\n");
				params.add(Constant.SEND_STATUS_02);
			}
		}
		if(!"".equals(lastOutDateStart)){
			sql.append(" AND  ad.out_date >=TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')\n");
			params.add(lastOutDateStart+" 00:00:00");
		}
		
		if(!"".equals(lastOutDateEnd)){
			sql.append(" AND  ad.out_date <=TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')\n");
			params.add(lastOutDateEnd+"  23:59:59");
		}
		
		if(!"".equals(sendStartDate)){
			sql.append(" AND A.CONFIRM_DATE>=TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')\n");
			params.add(sendStartDate+" 00:00:00");
		}
		if(!"".equals(sendEndDate)){
			sql.append(" AND A.CONFIRM_DATE<=TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')\n");
			params.add(sendEndDate+" 23:59:59");
		}
		if (!XHBUtil.IsNull(vin)) {
			sql.append(" AND TV.VIN LIKE ?");
			params.add("%"+vin+"%");
		}
		
		
		
		PageResult<Map<String, Object>> ps = dao.pageQuery(sql.toString(), params, getFunName(), pageSize, curPage);
		return ps;
	}
	
	/**
	 * 得到正常省市运费车辆明细
	 * @return
	 */
	public List<Map<String, Object>> getDetailListNormal(String billIds,String vehicleIds){
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("SELECT T.*,ROWNUM ROW_NUM FROM ");
		sbSql.append("(SELECT G.MATERIAL_ID,\n");
		sbSql.append("        VMG.GROUP_CODE,\n");
		sbSql.append("        VMG.GROUP_NAME,\n");
		sbSql.append("        G.MATERIAL_CODE,\n");
		sbSql.append("        G.MATERIAL_NAME,\n");
		sbSql.append("        B.BO_NO,\n");
		sbSql.append("        TV.VIN,TV.PASS_NO,F_GET_TMDEALER_NAME(TV.DEALER_ID) DEALER_NAME,A.AREA_ID,TO_CHAR(TV.VEHICLE_ID) VEHICLE_ID,\n");
		sbSql.append("        TV.ENGINE_NO,\n");
		sbSql.append("        NVL(F.DISTANCE,0) DISTANCE, NVL(F.SINGLE_PLACE,0) AMOUNT,\n");
		sbSql.append("        TO_CHAR(D.OUT_DATE,'YYYY-MM-DD') OUT_DATE,\n");
		sbSql.append("        TO_CHAR(D.ACC_DATE,'YYYY-MM-DD') STORAGE_DATE,\n");
		sbSql.append("        TO_CHAR(A.BILL_CRT_DATE,'yyyy-MM-dd hh24:mi:ss') BILL_CRT_DATE, \n");
		sbSql.append("        CASE WHEN TO_CHAR(F.FUEL_BEGIN_DATE,'YYYY-MM-DD') <= TO_CHAR(A.CONFIRM_DATE,'YYYY-MM-DD') AND TO_CHAR(F.FUEL_END_DATE,'YYYY-MM-DD') >= TO_CHAR(A.CONFIRM_DATE,'YYYY-MM-DD')\n");
        sbSql.append("        THEN nvl(F.fuel_coefficient,0) \n");
        sbSql.append("        ELSE 0 END FUEL_COEFFICIENT,\n");
		sbSql.append("        CASE WHEN TO_CHAR(F.FUEL_BEGIN_DATE,'YYYY-MM-DD') <= TO_CHAR(A.CONFIRM_DATE,'YYYY-MM-DD') AND TO_CHAR(F.FUEL_END_DATE,'YYYY-MM-DD') >= TO_CHAR(A.CONFIRM_DATE,'YYYY-MM-DD')\n");
        sbSql.append("        THEN NVL(F.DISTANCE,0)*(NVL(F.SINGLE_PLACE,0)+nvl(F.fuel_coefficient,0)) \n");
        sbSql.append("        ELSE NVL(F.DISTANCE,0)*NVL(F.SINGLE_PLACE,0) END SETTLE_AMOUNT,\n");
        sbSql.append("	   ROUND(TO_NUMBER(CRM.back_crm_date - (A.CONFIRM_DATE+(SELECT MIN(TI.ASS_DAYS)\n" );
		sbSql.append("           FROM TT_SALES_LOGI_INTREVAL TI\n" );
		sbSql.append("            WHERE TI.STATUS = 10011001\n" );
		sbSql.append("            AND A.VEH_NUM > TI.BEGIN_NUM\n" );
		sbSql.append("            AND A.VEH_NUM <= TI.END_NUM)))*24) - F.ARRIVE_DAYS*24 CQ_HOURSE\n");
        
		sbSql.append("FROM    TT_SALES_WAYBILL A,\n");
		sbSql.append("        TT_SALES_BOARD B,\n");
		sbSql.append("        TT_SALES_BO_DETAIL C,\n");
		sbSql.append("        TT_SALES_ALLOCA_DE D,\n");
		sbSql.append("        TM_VS_ADDRESS E,\n");
		sbSql.append("        TT_SALES_CITY_DIS F,\n");
		sbSql.append("        TM_VHCL_MATERIAL G,\n");
		sbSql.append("        TM_REGION J,\n");
		sbSql.append("        TM_VHCL_MATERIAL_GROUP VMG,\n");
		sbSql.append("        TM_VEHICLE TV,\n");
		sbSql.append("        TT_SALES_BACKCRM_DETAIL CRM\n");
		sbSql.append("WHERE   A.BILL_ID = C.BILL_ID\n");
		sbSql.append("AND     B.BO_ID = C.BO_ID\n");
		sbSql.append(" AND    C.BO_DE_ID = D.BO_DE_ID\n");
		sbSql.append(" AND    D.VEHICLE_ID = TV.VEHICLE_ID\n");
		sbSql.append(" AND    TV.MATERIAL_ID = G.MATERIAL_ID\n");
		sbSql.append(" AND    TV.SERIES_ID = VMG.GROUP_ID\n");
		sbSql.append(" AND    TV.VEHICLE_ID = CRM.VEHICLE_ID AND CRM.STATUS=10011001\n");
		sbSql.append(" AND    CRM.ADDRESS_ID = E.ID\n");
		sbSql.append(" AND    E.AREA_ID = J.REGION_CODE\n");
		sbSql.append(" AND    J.REGION_ID = F.CITY_ID\n");
		sbSql.append(" AND    A.AREA_ID = F.YIELDLY\n");
		sbSql.append(" AND    TV.SERIES_ID = F.CAR_TIE_ID\n");
		sbSql.append(" AND    TV.VEHICLE_ID IN ("+vehicleIds+")\n");
		List<Object> params = new LinkedList<Object>();
		if(billIds != null && !"".equals(billIds)){
			sbSql.append(Utility.getConSqlByParamForEqual(billIds, params,"A", "BILL_ID"));
		}
		sbSql.append(" ) T"); 	
		List<Map<String, Object>> list = dao.pageQuery(sbSql.toString(), params, dao.getFunName());
		return list;
	}
	/**
	 * 得到特殊省市运费车辆明细
	 * @return
	 */
	public List<Map<String, Object>> getDetailListSpecial(String billIds,String vehicleIds){
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("SELECT T.*,ROWNUM ROW_NUM FROM ");
		sbSql.append("(SELECT G.GROUP_ID,\n");
		sbSql.append("        G.GROUP_CODE,\n");
		sbSql.append("        G.GROUP_NAME,\n");
		sbSql.append("        VM.MATERIAL_CODE,\n");
		sbSql.append("        VM.MATERIAL_NAME,\n");
		sbSql.append("        B.BO_NO,\n");
		sbSql.append("        TV.VIN,TV.PASS_NO,F_GET_TMDEALER_NAME(TV.DEALER_ID) DEALER_NAME,A.AREA_ID,TO_CHAR(TV.VEHICLE_ID) VEHICLE_ID,\n");
		sbSql.append("        TV.ENGINE_NO, NVL(TSCD.DISTANCE,0) DISTANCE, NVL(TCF.AMOUNT,0) AMOUNT,\n");
		sbSql.append("        TO_CHAR(D.OUT_DATE,'YYYY-MM-DD') OUT_DATE,\n");
		sbSql.append("        TO_CHAR(D.ACC_DATE,'YYYY-MM-DD') STORAGE_DATE,\n");
		sbSql.append("        TO_CHAR(A.BILL_CRT_DATE,'yyyy-MM-dd hh24:mi:ss') BILL_CRT_DATE, \n");
		sbSql.append("        CASE WHEN TO_CHAR(TSCD.FUEL_BEGIN_DATE,'YYYY-MM-DD') <= TO_CHAR(A.CONFIRM_DATE,'YYYY-MM-DD') AND TO_CHAR(TSCD.FUEL_END_DATE,'YYYY-MM-DD') >= TO_CHAR(A.CONFIRM_DATE,'YYYY-MM-DD')\n");
        sbSql.append("        THEN nvl(TSCD.fuel_coefficient,0) \n");
        sbSql.append("        ELSE 0 END FUEL_COEFFICIENT,\n");
		sbSql.append("        CASE WHEN TO_CHAR(TSCD.FUEL_BEGIN_DATE,'YYYY-MM-DD') <= TO_CHAR(A.CONFIRM_DATE,'YYYY-MM-DD') AND TO_CHAR(TSCD.FUEL_END_DATE,'YYYY-MM-DD') >= TO_CHAR(A.CONFIRM_DATE,'YYYY-MM-DD')\n");
        sbSql.append("        THEN NVL(TSCD.DISTANCE,0)*(NVL(TSCD.SINGLE_PLACE,0)+nvl(TSCD.fuel_coefficient,0)) \n");
        sbSql.append("        ELSE NVL(TSCD.DISTANCE,0)*NVL(TSCD.SINGLE_PLACE,0) END SETTLE_AMOUNT\n");
		sbSql.append(" FROM   TT_SALES_WAYBILL A,\n");
		sbSql.append("        TT_SALES_BOARD B,\n");
		sbSql.append("        TT_SALES_BO_DETAIL C,\n");
		sbSql.append("        TT_SALES_ALLOCA_DE D,\n");
		sbSql.append("        TM_VS_ADDRESS E,\n");
		sbSql.append("        TM_VEHICLE TV,\n");
		sbSql.append("        TT_SALES_BACKCRM_DETAIL CRM,\n");
		sbSql.append("        TM_VHCL_MATERIAL_GROUP G,\n");
		sbSql.append("        TM_VHCL_MATERIAL VM, TM_REGION TR, TT_SALES_CITY_DIS TSCD,\n");
		sbSql.append("         TM_SPECIAL_CITY_FARE TCF");
		sbSql.append(" WHERE  A.BILL_ID = C.BILL_ID\n");
		sbSql.append(" AND    B.BO_ID = C.BO_ID\n");
		sbSql.append(" AND    C.BO_DE_ID = D.BO_DE_ID\n");
		sbSql.append(" AND    D.VEHICLE_ID = TV.VEHICLE_ID\n");
		sbSql.append(" AND    TV.SERIES_ID = G.GROUP_ID\n");
		sbSql.append(" AND    TV.MATERIAL_ID = VM.MATERIAL_ID\n");
		sbSql.append(" AND    A.AREA_ID = TCF.YIELDLY -- AND TV.SERIES_ID = TCF.SERIES_ID \n");
		sbSql.append(" AND    TV.VEHICLE_ID = CRM.VEHICLE_ID AND CRM.STATUS=10011001\n");
		sbSql.append(" AND    CRM.ADDRESS_ID = E.ID\n");
		sbSql.append(" AND    TV.SERIES_ID = TSCD.CAR_TIE_ID\n");
		sbSql.append(" AND    TV.VEHICLE_ID IN ("+vehicleIds+")\n");
		List<Object> params = new LinkedList<Object>();
		if(billIds != null && !"".equals(billIds)){
			sbSql.append(Utility.getConSqlByParamForEqual(billIds, params,"A", "BILL_ID"));
		}
		sbSql.append(" AND    E.AREA_ID = TCF.CITY_ID AND E.AREA_ID = TR.REGION_CODE AND TR.REGION_ID = TSCD.CITY_ID AND A.AREA_ID = TSCD.YIELDLY) T");
	
		List<Map<String, Object>> list = dao.pageQuery(sbSql.toString(), params, dao.getFunName());
		return list;
	}
	
	/**
	 * 得到一般省市运费车辆明细(按车系)
	 * @return
	 */
	public List<Map<String, Object>> getSeriesListNormal(String billIds,String vehicleIds){
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("SELECT T.*,ROWNUM ROW_NUM FROM ");
		sbSql.append("(SELECT AA.GROUP_ID,\n");
		sbSql.append("        AA.GROUP_CODE,\n");
		sbSql.append("        AA.GROUP_NAME,\n");
		sbSql.append("        TO_CHAR(NVL(SUM(AA.COUNT),0)) COUNT,\n");
		sbSql.append("        COUNT(*) VEHICLE_NUM \n");
		sbSql.append(" FROM\n");
		sbSql.append("(SELECT G.GROUP_ID,\n");
		sbSql.append("        G.GROUP_CODE,\n");
		sbSql.append("        G.GROUP_NAME,\n");
		sbSql.append("        CASE WHEN TO_CHAR(F.FUEL_BEGIN_DATE,'YYYY-MM-DD') <= TO_CHAR(A.CONFIRM_DATE,'YYYY-MM-DD') AND TO_CHAR(F.FUEL_END_DATE,'YYYY-MM-DD') >= TO_CHAR(A.CONFIRM_DATE,'YYYY-MM-DD')\n");
        sbSql.append("        THEN NVL(F.DISTANCE,0)*(NVL(F.SINGLE_PLACE,0)+nvl(F.fuel_coefficient,0)) \n");
        sbSql.append("        ELSE NVL(F.DISTANCE,0)*NVL(F.SINGLE_PLACE,0) END COUNT\n");
		//sbSql.append("        (NVL(F.DISTANCE,0)*NVL(F.SINGLE_PLACE,0))*(1 + nvl(F.fuel_coefficient,0))  COUNT\n");
		sbSql.append("FROM    TT_SALES_WAYBILL A,\n");
		sbSql.append("        TT_SALES_BOARD B,\n");
		sbSql.append("        TT_SALES_BO_DETAIL C,\n");
		sbSql.append("        TT_SALES_ALLOCA_DE D,\n");
		sbSql.append("        TM_VS_ADDRESS E,\n");
		sbSql.append("        TT_SALES_CITY_DIS F,\n");
		sbSql.append("        TM_VHCL_MATERIAL_GROUP G,\n");
		sbSql.append("        TM_REGION J,\n");
		sbSql.append("        TM_VEHICLE TV,\n");
		sbSql.append("        TT_SALES_BACKCRM_DETAIL CRM\n");
		sbSql.append("WHERE   A.BILL_ID = C.BILL_ID\n");
		sbSql.append("AND     B.BO_ID = C.BO_ID\n");
		sbSql.append(" AND    C.BO_DE_ID = D.BO_DE_ID\n");
		sbSql.append(" AND    D.VEHICLE_ID = TV.VEHICLE_ID\n");
		sbSql.append(" AND    TV.SERIES_ID = G.GROUP_ID\n");
		sbSql.append(" AND    TV.VEHICLE_ID = CRM.VEHICLE_ID AND CRM.STATUS=10011001\n");
		sbSql.append(" AND    CRM.ADDRESS_ID = E.ID\n");
		sbSql.append(" AND    E.AREA_ID = J.REGION_CODE\n");
		sbSql.append(" AND    J.REGION_ID = F.CITY_ID\n");
		sbSql.append(" AND    A.AREA_ID = F.YIELDLY\n");
		sbSql.append(" AND    TV.SERIES_ID = F.CAR_TIE_ID\n");
		sbSql.append(" AND    TV.VEHICLE_ID IN ("+vehicleIds+")\n");
		List<Object> params = new LinkedList<Object>();
		if(billIds != null && !"".equals(billIds)){
			sbSql.append(Utility.getConSqlByParamForEqual(billIds, params,"A", "BILL_ID"));
		}
		sbSql.append(" ) AA\n");
		sbSql.append(" GROUP BY AA.GROUP_ID,AA.GROUP_CODE,AA.GROUP_NAME) T\n");
		List<Map<String, Object>> list = dao.pageQuery(sbSql.toString(), params, dao.getFunName());
		return list;
	}
	
	/**
	 * 得到特殊省市运费车辆明细(按车系)
	 * @return
	 */
	public List<Map<String, Object>> getSeriesListSpecial(String billIds,String vehicleIds){
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("SELECT  T.*,ROWNUM ROW_NUM FROM \n");
		sbSql.append("(SELECT  AA.GROUP_ID,\n");
		sbSql.append("        AA.GROUP_CODE,\n");
		sbSql.append("        AA.GROUP_NAME,\n");
		sbSql.append("        TO_CHAR(NVL(SUM(AA.COUNT),0)) COUNT,\n");
		sbSql.append("        COUNT(*) VEHICLE_NUM \n");
		sbSql.append(" FROM\n");
		sbSql.append("( SELECT G.GROUP_ID,\n");
		sbSql.append("        G.GROUP_CODE,\n");
		sbSql.append("        G.GROUP_NAME,\n");
		sbSql.append("        CASE WHEN TO_CHAR(TSCD.FUEL_BEGIN_DATE,'YYYY-MM-DD') <= TO_CHAR(A.CONFIRM_DATE,'YYYY-MM-DD') AND TO_CHAR(TSCD.FUEL_END_DATE,'YYYY-MM-DD') >= TO_CHAR(A.CONFIRM_DATE,'YYYY-MM-DD')\n");
        sbSql.append("        THEN NVL(TSCD.DISTANCE,0)*(NVL(TSCD.SINGLE_PLACE,0)+nvl(TSCD.fuel_coefficient,0)) \n");
        sbSql.append("        ELSE NVL(TSCD.DISTANCE,0)*NVL(TSCD.SINGLE_PLACE,0) END COUNT\n");
		//sbSql.append("        (NVL(TSCD.DISTANCE,0)*NVL(TSCD.SINGLE_PLACE,0))*(1 + nvl(TSCD.fuel_coefficient,0))  COUNT\n");
		sbSql.append(" FROM   TT_SALES_WAYBILL A,\n");
		sbSql.append("        TT_SALES_BOARD B,\n");
		sbSql.append("        TT_SALES_BO_DETAIL C,\n");
		sbSql.append("        TT_SALES_ALLOCA_DE D,\n");
		sbSql.append("        TM_VS_ADDRESS E,\n");
		sbSql.append("        TM_VEHICLE TV,\n");
		sbSql.append("        TT_SALES_BACKCRM_DETAIL CRM,\n");
		sbSql.append("        TM_REGION TR, TT_SALES_CITY_DIS TSCD,\n");
		sbSql.append("        TM_VHCL_MATERIAL_GROUP G,\n");
		sbSql.append("        TM_SPECIAL_CITY_FARE TCF\n");
		sbSql.append(" WHERE  A.BILL_ID = C.BILL_ID\n");
		sbSql.append(" AND    B.BO_ID = C.BO_ID\n");
		sbSql.append(" AND    C.BO_DE_ID = D.BO_DE_ID\n");
		sbSql.append(" AND    D.VEHICLE_ID = TV.VEHICLE_ID\n");
		sbSql.append(" AND    TV.SERIES_ID = G.GROUP_ID\n");
		sbSql.append(" AND    A.AREA_ID = TCF.YIELDLY --AND TV.SERIES_ID = TCF.SERIES_ID \n");
		sbSql.append(" AND    TV.VEHICLE_ID = CRM.VEHICLE_ID AND CRM.STATUS=10011001\n");
		sbSql.append(" AND    CRM.ADDRESS_ID = E.ID\n");
		sbSql.append(" AND    E.AREA_ID = TCF.CITY_ID\n");
		sbSql.append(" AND    TV.SERIES_ID = TSCD.CAR_TIE_ID\n");
		sbSql.append(" AND    TV.VEHICLE_ID IN ("+vehicleIds+")\n");
		List<Object> params = new LinkedList<Object>();
		if(billIds != null && !"".equals(billIds)){
			sbSql.append(Utility.getConSqlByParamForEqual(billIds, params,"A", "BILL_ID"));
		}
		sbSql.append(" AND    E.AREA_ID = TR.REGION_CODE AND TR.REGION_ID = TSCD.CITY_ID AND A.AREA_ID = TSCD.YIELDLY )AA\n");
		sbSql.append(" GROUP BY AA.GROUP_ID,AA.GROUP_CODE,AA.GROUP_NAME) T\n");
		List<Map<String, Object>> list = dao.pageQuery(sbSql.toString(), params, dao.getFunName());
		return list;
	}
	
	/**
	 * 运单结算金额按车系汇总查询
	 * 
	 * @param billIDs
	 * @param poseId
	 * @return
	 */
	public List<Map<String, Object>> getSendBillSettlementDetail(String billIds, String tbillIDs,String poseId,String vehicleIds)
	{
		List<Object> params = new LinkedList<Object>();
		
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("SELECT A.*,ROWNUM ROW_NUM FROM ");
		sbSql.append("(SELECT AA.GROUP_ID,\n");
		sbSql.append("       AA.GROUP_CODE,\n");
		sbSql.append("       AA.GROUP_NAME,\n");
		sbSql.append("       TO_CHAR(SUM(AA.COUNT)) COUNT,\n");
		sbSql.append("       COUNT(*) VEHICLE_NUM\n");
		sbSql.append("FROM\n");
		sbSql.append("(SELECT  G.GROUP_ID,\n");
		sbSql.append("        G.GROUP_CODE,\n");
		sbSql.append("        G.GROUP_NAME,\n");		
		sbSql.append("        CASE WHEN TO_CHAR(F.FUEL_BEGIN_DATE,'YYYY-MM-DD') <= TO_CHAR(A.CONFIRM_DATE,'YYYY-MM-DD') AND TO_CHAR(F.FUEL_END_DATE,'YYYY-MM-DD') >= TO_CHAR(A.CONFIRM_DATE,'YYYY-MM-DD')\n");
        sbSql.append("        THEN NVL(F.DISTANCE,0)*(NVL(F.SINGLE_PLACE,0)+nvl(F.fuel_coefficient,0)) \n");
        sbSql.append("        ELSE NVL(F.DISTANCE,0)*NVL(F.SINGLE_PLACE,0) END COUNT\n");
		//sbSql.append("        (NVL(F.DISTANCE,0)*NVL(F.SINGLE_PLACE,0))*(1 + nvl(F.fuel_coefficient,0))  COUNT\n");
		sbSql.append("FROM    TT_SALES_WAYBILL A,\n");
		sbSql.append("        TT_SALES_BOARD B,\n");
		sbSql.append("        TT_SALES_BO_DETAIL C,\n");
		sbSql.append("        TT_SALES_ALLOCA_DE D,\n");
		sbSql.append("        TM_VS_ADDRESS E,\n");
		sbSql.append("        TT_SALES_CITY_DIS F,\n");
		sbSql.append("        TM_VHCL_MATERIAL_GROUP G,\n");
		sbSql.append("        TM_REGION J,\n");
		sbSql.append("        TM_VEHICLE TV,\n");
		sbSql.append("        TT_SALES_BACKCRM_DETAIL CRM\n");
		sbSql.append("WHERE   A.BILL_ID = C.BILL_ID\n");
		sbSql.append("AND     B.BO_ID = C.BO_ID\n");
		sbSql.append(" AND    C.BO_DE_ID = D.BO_DE_ID\n");
		sbSql.append(" AND    D.VEHICLE_ID = TV.VEHICLE_ID\n");
		sbSql.append(" AND    TV.SERIES_ID = G.GROUP_ID\n");
		sbSql.append(" AND    TV.VEHICLE_ID = CRM.VEHICLE_ID AND CRM.STATUS=10011001\n");
		sbSql.append(" AND    CRM.ADDRESS_ID = E.ID\n");
		sbSql.append(" AND    E.AREA_ID = J.REGION_CODE\n");
		sbSql.append(" AND    J.REGION_ID = F.CITY_ID\n");
		sbSql.append(" AND    A.AREA_ID = F.YIELDLY\n");
		sbSql.append(" AND    TV.SERIES_ID = F.CAR_TIE_ID\n");
		sbSql.append(" AND    TV.VEHICLE_ID IN ("+vehicleIds+")\n");
		sbSql.append(Utility.getConSqlByParamForEqual(billIds, params,"A", "BILL_ID"));
		sbSql.append(" UNION ALL\n");
		sbSql.append(" SELECT G.GROUP_ID,\n");
		sbSql.append("        G.GROUP_CODE,\n");
		sbSql.append("        G.GROUP_NAME,\n");
		sbSql.append("        TSCD.fuel_coefficient AS  fuel_coefficient, \n");
		sbSql.append("        CASE WHEN TO_CHAR(TSCD.FUEL_BEGIN_DATE,'YYYY-MM-DD') <= TO_CHAR(A.CONFIRM_DATE,'YYYY-MM-DD') AND TO_CHAR(TSCD.FUEL_END_DATE,'YYYY-MM-DD') >= TO_CHAR(A.CONFIRM_DATE,'YYYY-MM-DD')\n");
        sbSql.append("        THEN NVL(TSCD.DISTANCE,0)*(NVL(TSCD.SINGLE_PLACE,0)+nvl(TSCD.fuel_coefficient,0)) \n");
        sbSql.append("        ELSE NVL(TSCD.DISTANCE,0)*NVL(TSCD.SINGLE_PLACE,0) END SETTLE_AMOUNT\n");
		//sbSql.append("        (NVL(TSCD.DISTANCE,0)*NVL(I.AMOUNT,0))*(1 + nvl(TSCD.fuel_coefficient,0))  SETTLE_AMOUNT\n");
		sbSql.append(" FROM   TT_SALES_WAYBILL A,\n");
		sbSql.append("        TT_SALES_BOARD B,\n");
		sbSql.append("        TT_SALES_BO_DETAIL C,\n");
		sbSql.append("        TT_SALES_ALLOCA_DE D,\n");
		sbSql.append("        TM_VS_ADDRESS E,\n");
		sbSql.append("        TM_VEHICLE TV,\n");
		sbSql.append("        TT_SALES_BACKCRM_DETAIL CRM,\n");
		sbSql.append("        TM_REGION TR, TT_SALES_CITY_DIS TSCD,\n");
		sbSql.append("        TM_VHCL_MATERIAL_GROUP G,\n");
		sbSql.append("        TM_SPECIAL_CITY_FARE TCF\n");
		sbSql.append(" WHERE  A.BILL_ID = C.BILL_ID\n");
		sbSql.append(" AND    B.BO_ID = C.BO_ID\n");
		sbSql.append(" AND    C.BO_DE_ID = D.BO_DE_ID\n");
		sbSql.append(" AND    D.VEHICLE_ID = TV.VEHICLE_ID\n");
		sbSql.append(" AND    TV.SERIES_ID = G.GROUP_ID\n");
		sbSql.append(" AND    A.AREA_ID = TCF.YIELDLY  -- AND TV.SERIES_ID = TCF.SERIES_ID \n");
		sbSql.append(" AND    TV.VEHICLE_ID = CRM.VEHICLE_ID AND CRM.STATUS=10011001\n");
		sbSql.append(" AND    CRM.ADDRESS_ID = E.ID\n");
		sbSql.append(" AND    E.AREA_ID = TCF.CITY_ID\n");
		sbSql.append(" AND    E.AREA_ID = TR.REGION_CODE AND TR.REGION_ID = TSCD.CITY_ID AND A.AREA_ID = TSCD.YIELDLY \n");
		sbSql.append(" AND    TV.SERIES_ID = TSCD.CAR_TIE_ID\n");
		sbSql.append(" AND    TV.VEHICLE_ID IN ("+vehicleIds+")\n");
		sbSql.append(Utility.getConSqlByParamForEqual(tbillIDs, params,"A", "BILL_ID"));
		sbSql.append(" )AA\n");
	//	sbSql.append(" GROUP BY AA.GROUP_ID,AA.GROUP_CODE,AA.GROUP_NAME)A\n");
		List<Map<String, Object>> list = dao.pageQuery(sbSql.toString(), params, dao.getFunName());
		return list;
	}
	
	/**
	 * 运单结算金额按车辆明细查询
	 * 
	 * @param billIds
	 * @param poseId
	 * @return
	 */
	public List<Map<String, Object>> getSendBIllSettleVehicleDetail(String billIDs, String tbillIds,String poseId,String vehicleIds)
	{
		List<Object> params = new LinkedList<Object>();
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("SELECT T.*,ROWNUM ROW_NUM FROM ");
		sbSql.append("(SELECT G.MATERIAL_ID,\n");
		sbSql.append("        VMG.GROUP_CODE,\n");
		sbSql.append("        VMG.GROUP_NAME,\n");
		sbSql.append("        G.MATERIAL_CODE,\n");
		sbSql.append("        G.MATERIAL_NAME,\n");
		sbSql.append("        B.BO_NO,\n");
		sbSql.append("        TV.VIN,TV.PASS_NO,F_GET_TMDEALER_NAME(TV.DEALER_ID) DEALER_NAME,A.AREA_ID,TO_CHAR(TV.VEHICLE_ID) VEHICLE_ID,\n");
		sbSql.append("        TV.ENGINE_NO,\n");
		sbSql.append("        NVL(F.DISTANCE,0) DISTANCE, NVL(F.SINGLE_PLACE,0) AMOUNT,\n");
		sbSql.append("        TO_CHAR(D.OUT_DATE,'YYYY-MM-DD') OUT_DATE,\n");
		sbSql.append("        TO_CHAR(D.ACC_DATE,'YYYY-MM-DD') STORAGE_DATE,\n");
		sbSql.append("        TO_CHAR(A.BILL_CRT_DATE,'yyyy-MM-dd hh24:mi:ss') BILL_CRT_DATE, \n");
		sbSql.append("        CASE WHEN TO_CHAR(F.FUEL_BEGIN_DATE,'YYYY-MM-DD') <= TO_CHAR(A.CONFIRM_DATE,'YYYY-MM-DD') AND TO_CHAR(F.FUEL_END_DATE,'YYYY-MM-DD') >= TO_CHAR(A.CONFIRM_DATE,'YYYY-MM-DD')\n");
        sbSql.append("        THEN nvl(F.fuel_coefficient,0) \n");
        sbSql.append("        ELSE 0 END FUEL_COEFFICIENT,\n");
		sbSql.append("        CASE WHEN TO_CHAR(F.FUEL_BEGIN_DATE,'YYYY-MM-DD') <= TO_CHAR(A.CONFIRM_DATE,'YYYY-MM-DD') AND TO_CHAR(F.FUEL_END_DATE,'YYYY-MM-DD') >= TO_CHAR(A.CONFIRM_DATE,'YYYY-MM-DD')\n");
        sbSql.append("        THEN NVL(F.DISTANCE,0)*(NVL(F.SINGLE_PLACE,0)+nvl(F.fuel_coefficient,0)) \n");
        sbSql.append("        ELSE NVL(F.DISTANCE,0)*NVL(F.SINGLE_PLACE,0) END SETTLE_AMOUNT\n");
		sbSql.append("FROM    TT_SALES_WAYBILL A,\n");
		sbSql.append("        TT_SALES_BOARD B,\n");
		sbSql.append("        TT_SALES_BO_DETAIL C,\n");
		sbSql.append("        TT_SALES_ALLOCA_DE D,\n");
		sbSql.append("        TM_VS_ADDRESS E,\n");
		sbSql.append("        TT_SALES_CITY_DIS F,\n");
		sbSql.append("        TM_VHCL_MATERIAL G,\n");
		sbSql.append("        TM_REGION J,\n");
		sbSql.append("        TM_VHCL_MATERIAL_GROUP VMG,\n");
		sbSql.append("        TM_VEHICLE TV,\n");
		sbSql.append("        TT_SALES_BACKCRM_DETAIL CRM\n");
		sbSql.append("WHERE   A.BILL_ID = C.BILL_ID\n");
		sbSql.append("AND     B.BO_ID = C.BO_ID\n");
		sbSql.append(" AND    C.BO_DE_ID = D.BO_DE_ID\n");
		sbSql.append(" AND    D.VEHICLE_ID = TV.VEHICLE_ID\n");
		sbSql.append(" AND    TV.MATERIAL_ID = G.MATERIAL_ID\n");
		sbSql.append(" AND    TV.SERIES_ID = VMG.GROUP_ID\n");
		sbSql.append(" AND    TV.VEHICLE_ID = CRM.VEHICLE_ID AND CRM.STATUS=10011001\n");
		sbSql.append(" AND    CRM.ADDRESS_ID = E.ID\n");
		sbSql.append(" AND    E.AREA_ID = J.REGION_CODE\n");
		sbSql.append(" AND    J.REGION_ID = F.CITY_ID\n");
		sbSql.append(" AND    A.AREA_ID = F.YIELDLY\n");
		sbSql.append(" AND    TV.SERIES_ID = F.CAR_TIE_ID\n");
		sbSql.append(" AND    TV.VEHICLE_ID IN ("+vehicleIds+")\n");
		sbSql.append(Utility.getConSqlByParamForEqual(billIDs, params,"A", "BILL_ID"));
		sbSql.append(" UNION ALL\n");
		sbSql.append(" SELECT G.GROUP_ID,\n");
		sbSql.append("        G.GROUP_CODE,\n");
		sbSql.append("        G.GROUP_NAME,\n");
		sbSql.append("        VM.MATERIAL_CODE,\n");
		sbSql.append("        VM.MATERIAL_NAME,\n");
		sbSql.append("        B.BO_NO,\n");
		sbSql.append("        TV.VIN,A.AREA_ID,TO_CHAR(TV.VEHICLE_ID) VEHICLE_ID,\n");
		sbSql.append("        TV.ENGINE_NO, NVL(TSCD.DISTANCE,0) DISTANCE, NVL(TCF.AMOUNT,0) AMOUNT,NVL(TSCD.FUEL_COEFFICIENT,0) FUEL_COEFFICIENT,\n");
		sbSql.append("        TO_CHAR(D.OUT_DATE,'YYYY-MM-DD') OUT_DATE,\n");
		sbSql.append("        TO_CHAR(D.ACC_DATE,'YYYY-MM-DD') STORAGE_DATE,\n");
		sbSql.append("        TO_CHAR(A.BILL_CRT_DATE,'yyyy-MM-dd hh24:mi:ss') BILL_CRT_DATE, \n");		
		sbSql.append("        CASE WHEN TO_CHAR(TSCD.FUEL_BEGIN_DATE,'YYYY-MM-DD') <= TO_CHAR(A.CONFIRM_DATE,'YYYY-MM-DD') AND TO_CHAR(TSCD.FUEL_END_DATE,'YYYY-MM-DD') >= TO_CHAR(A.CONFIRM_DATE,'YYYY-MM-DD')\n");
        sbSql.append("        THEN NVL(TSCD.DISTANCE,0)*(NVL(TSCD.SINGLE_PLACE,0)+nvl(TSCD.fuel_coefficient,0)) \n");
        sbSql.append("        ELSE NVL(TSCD.DISTANCE,0)*NVL(TSCD.SINGLE_PLACE,0) END SETTLE_AMOUNT\n");
		sbSql.append(" FROM   TT_SALES_WAYBILL A,\n");
		sbSql.append("        TT_SALES_BOARD B,\n");
		sbSql.append("        TT_SALES_BO_DETAIL C,\n");
		sbSql.append("        TT_SALES_ALLOCA_DE D,\n");
		sbSql.append("        TM_VS_ADDRESS E,\n");
		sbSql.append("        TM_VEHICLE TV,\n");
		sbSql.append("        TT_SALES_BACKCRM_DETAIL CRM,\n");
		sbSql.append("        TM_VHCL_MATERIAL_GROUP G,\n");
		sbSql.append("        TM_VHCL_MATERIAL VM, TM_REGION TR, TT_SALES_CITY_DIS TSCD, \n");
		sbSql.append("        TM_SPECIAL_CITY_FARE TCF\n");
		sbSql.append(" WHERE  A.BILL_ID = C.BILL_ID\n");
		sbSql.append(" AND    B.BO_ID = C.BO_ID\n");
		sbSql.append(" AND    C.BO_DE_ID = D.BO_DE_ID\n");
		sbSql.append(" AND    D.VEHICLE_ID = TV.VEHICLE_ID\n");
		sbSql.append(" AND    TV.SERIES_ID = G.GROUP_ID\n");
		sbSql.append(" AND    TV.MATERIAL_ID = VM.MATERIAL_ID\n");
		sbSql.append(" AND    A.AREA_ID = TCF.YIELDLY \n");
		sbSql.append(" AND    TV.VEHICLE_ID = CRM.VEHICLE_ID AND CRM.STATUS=10011001\n");
		sbSql.append(" AND    CRM.ADDRESS_ID = E.ID \n");
		sbSql.append(" AND    E.AREA_ID = TCF.CITY_ID AND E.AREA_ID = TR.REGION_CODE AND TR.REGION_ID = TSCD.CITY_ID AND A.AREA_ID = TSCD.YIELDLY \n");
		sbSql.append(" AND    TV.SERIES_ID = TSCD.CAR_TIE_ID\n");
		sbSql.append(" AND    TV.VEHICLE_ID IN ("+vehicleIds+")\n");
		sbSql.append(Utility.getConSqlByParamForEqual(tbillIds, params,"A", "BILL_ID"));
		sbSql.append(" )T");
		return dao.pageQuery(sbSql.toString(), params, dao.getFunName());
	}
	
	 /**
     * 根据运单及产地查询每个产地下，该物流商的运费和
     * @param areaId
     * @param billIDs
     * @return
     */
	public List<Map<String, Object>>  getAmountVhecleNumByAreaId(String billIds,String areaId,String tbillIDs,String billIDs,String poseId){
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("SELECT TO_CHAR(COUNT(AA.VIN)) VEHICLE_NUM,TO_CHAR(SUM(NVL(AA.SETTLE_AMOUNT,0))) SUM_SETTLE_AMOUNT \n");
		sbSql.append(" FROM (SELECT G.MATERIAL_ID,\n");
		sbSql.append("        G.MATERIAL_CODE,\n");
		sbSql.append("        G.MATERIAL_NAME,\n");
		sbSql.append("        VMG.GROUP_CODE,\n");
		sbSql.append("        VMG.GROUP_NAME,\n");
		sbSql.append("        B.BO_NO,\n");
		sbSql.append("        TV.VIN,A.AREA_ID,\n");
		sbSql.append("        TV.ENGINE_NO,\n");
		sbSql.append("        TO_CHAR(D.OUT_DATE,'YYYY-MM-DD') OUT_DATE,\n");
		sbSql.append("        TO_CHAR(D.ACC_DATE,'YYYY-MM-DD') STORAGE_DATE,\n");
		sbSql.append("        F.fuel_coefficient AS  fuel_coefficient \n");
		sbSql.append("       (NVL(F.DISTANCE,0)*NVL(I.AMOUNT,0))*(1 + nvl(F.fuel_coefficient,0))  SETTLE_AMOUNT\n");
		sbSql.append("FROM    TT_SALES_WAYBILL A,\n");
		sbSql.append("        TT_SALES_BOARD B,\n");
		sbSql.append("        TT_SALES_BO_DETAIL C,\n");
		sbSql.append("        TT_SALES_ALLOCA_DE D,\n");
		sbSql.append("        TM_VS_ADDRESS E,\n");
		sbSql.append("        TT_SALES_CITY_DIS F,\n");
		sbSql.append("        TM_VHCL_MATERIAL G,\n");
		sbSql.append("        TT_SALES_MILSET H,\n");
		sbSql.append("        TT_SALES_FARE I,\n");
		sbSql.append("        TM_REGION J,\n");
		sbSql.append("        TM_VHCL_MATERIAL_GROUP VMG,\n");
		sbSql.append("        TM_VEHICLE TV\n");
		sbSql.append("WHERE   A.BILL_ID = C.BILL_ID\n");
		sbSql.append("AND     B.BO_ID = C.BO_ID\n");
		sbSql.append(" AND    C.BO_DE_ID = D.BO_DE_ID\n");
		sbSql.append(" AND    D.VEHICLE_ID = TV.VEHICLE_ID\n");
		sbSql.append(" AND    TV.MATERIAL_ID = G.MATERIAL_ID\n");
		sbSql.append(" AND    TV.SERIES_ID = VMG.GROUP_ID\n");
		sbSql.append(" AND    A.ADDRESS_ID = E.ID\n");
		sbSql.append(" AND    E.AREA_ID = J.REGION_CODE\n");
		sbSql.append(" AND    J.REGION_ID = F.CITY_ID\n");
		sbSql.append(" AND    A.AREA_ID = F.YIELDLY\n");
		sbSql.append(" AND (F.DISTANCE >= H.MIL_START AND F.DISTANCE < H.MIL_END AND H.YIELDLY = F.YIELDLY)\n");
		sbSql.append(" AND    H.MIL_ID = I.MIL_ID\n");
		sbSql.append(" AND    I.GROUP_ID  = TV.SERIES_ID\n");
		sbSql.append(" AND    A.BILL_ID IN ("+billIds+")\n");
		if(!"".equals(billIDs)){
			sbSql.append(" AND    A.BILL_ID IN ("+billIDs+")\n");
		}else{
			sbSql.append(" AND    A.BILL_ID NOT IN ("+tbillIDs+")\n");
		}	
		if(areaId != null && !"".equals(areaId)){
		sbSql.append(" AND A.AREA_ID = "+areaId+"\n");
		}
		sbSql.append(" UNION ALL\n");
		sbSql.append(" SELECT G.GROUP_ID,\n");
		sbSql.append("        G.GROUP_CODE,\n");
		sbSql.append("        G.GROUP_NAME,\n");
		sbSql.append("        VM.MATERIAL_CODE,\n");
		sbSql.append("        VM.MATERIAL_NAME,\n");
		sbSql.append("        B.BO_NO,\n");
		sbSql.append("        TV.VIN,A.AREA_ID,\n");
		sbSql.append("        TV.ENGINE_NO,\n");
		sbSql.append("        TO_CHAR(D.OUT_DATE,'YYYY-MM-DD') OUT_DATE,\n");
		sbSql.append("        TO_CHAR(D.ACC_DATE,'YYYY-MM-DD') STORAGE_DATE,\n");
		sbSql.append("        TSCD.fuel_coefficient AS  fuel_coefficient \n");
		sbSql.append("       (NVL(F.DISTANCE,0)*NVL(I.AMOUNT,0))*(1 + fuel_coefficient)  SETTLE_AMOUNT\n");
		sbSql.append("        TO_CHAR(NVL(TCF.AMOUNT,0)*NVL(TSCD.DISTANCE,0)) SETTLE_AMOUNT\n");
		sbSql.append(" FROM   TT_SALES_WAYBILL A,\n");
		sbSql.append("        TT_SALES_BOARD B,\n");
		sbSql.append("        TT_SALES_BO_DETAIL C,\n");
		sbSql.append("        TT_SALES_ALLOCA_DE D,\n");
		sbSql.append("        TM_VS_ADDRESS E,\n");
		sbSql.append("        TM_VEHICLE TV,\n");
		sbSql.append("        TM_VHCL_MATERIAL_GROUP G,\n");
		sbSql.append("        TM_VHCL_MATERIAL VM, TM_REGION TR, TT_SALES_CITY_DIS TSCD,\n");
		sbSql.append("        TM_SPECIAL_CITY_FARE TCF\n");
		sbSql.append(" WHERE  A.BILL_ID = C.BILL_ID\n");
		sbSql.append(" AND    B.BO_ID = C.BO_ID\n");
		sbSql.append(" AND    C.BO_DE_ID = D.BO_DE_ID\n");
		sbSql.append(" AND    D.VEHICLE_ID = TV.VEHICLE_ID\n");
		sbSql.append(" AND    TV.SERIES_ID = G.GROUP_ID\n");
		sbSql.append(" AND    TV.MATERIAL_ID = VM.MATERIAL_ID\n");
		sbSql.append(" AND    A.AREA_ID = TCF.YIELDLY\n");
		sbSql.append(" AND    A.ADDRESS_ID = E.ID\n");
		sbSql.append(" AND    E.AREA_ID = TCF.CITY_ID AND E.AREA_ID = TR.REGION_CODE AND TR.REGION_ID = TSCD.CITY_ID AND A.AREA_ID = TSCD.YIELDLY \n"); 
		sbSql.append(" AND    A.BILL_ID IN ("+billIds+")\n");
		if(!"".equals(tbillIDs)){
			sbSql.append(" AND    A.BILL_ID IN ("+tbillIDs+")\n");
		}else{
			sbSql.append(" AND    A.BILL_ID NOT IN ("+billIDs+")");
		}	
		if(areaId != null && !"".equals(areaId)){
		sbSql.append(" AND A.AREA_ID = "+areaId+"\n");
		}
		sbSql.append("    ) AA"); 
		return dao.pageQuery(sbSql.toString(), null, getFunName());
	}
	
	public PageResult<Map<String, Object>> getSettlementList(Map<String, Object> map, int curPage, int pageSize)
					throws Exception
	{
		StringBuffer sbSql = new StringBuffer();
		String poseId = (String) map.get("poseId");
		String areaId = (String) map.get("areaId");
		String logiId = (String) map.get("logiId");
		String balNo = (String) map.get("balNo");
		String balStartDate = (String) map.get("balStartDate");
		String balEndDate = (String) map.get("balEndDate");
		sbSql.append("SELECT  B.LOGI_FULL_NAME,\n");
		sbSql.append("        B.LOGI_NAME,\n");
		sbSql.append("        A.BAL_NO,\n");
		sbSql.append("        A.BAL_ID,\n");
		sbSql.append("        TO_CHAR(A.BAL_DATE,'yyyy-MM-dd') BAL_DATE,\n");
		sbSql.append("        A.BAL_COUNT,\n");
		sbSql.append("        A.BAL_AMOUNT,\n");
		sbSql.append("        C.NAME \n");
		sbSql.append("  FROM  TT_SALES_BALANCE A,\n");
		sbSql.append("        TT_SALES_LOGI    B,\n");
		sbSql.append("        TC_USER          C\n");
		sbSql.append(" WHERE  A.LOGI_ID = B.LOGI_ID\n");
		sbSql.append("   AND  A.BAL_PER = C.USER_ID\n");
		sbSql.append(MaterialGroupManagerDao.getPoseIdBusinessSql("A.AREA_ID", poseId));
		if (areaId != null && !"".equals(areaId))
		{
			sbSql.append("   AND  A.AREA_ID = " + areaId + "\n");
		}
		if (logiId != null && !"".equals(logiId))
		{
			sbSql.append("   AND  A.LOGI_ID = " + logiId + "\n");
		}
		if (balNo != null && !"".equals(balNo))
		{
			sbSql.append("   AND  A.BAL_NO LIKE '%" + balNo + "%'\n");
		}
		if (balStartDate != null && !"".equals(balStartDate))
		{
			sbSql.append("   AND  A.BAL_DATE >= TO_DATE('" + balStartDate + "','yyyy-MM-dd')\n");
		}
		if (balEndDate != null && !"".equals(balEndDate))
		{
			sbSql.append("   AND  A.BAL_DATE <= TO_DATE('" + balEndDate + "','yyyy-MM-dd')+1\n");
		}
		sbSql.append(" ORDER BY A.BAL_DATE ASC");
		PageResult<Map<String, Object>> ps = dao.pageQuery(sbSql.toString(), null, getFunName(), pageSize, curPage);
		return ps;
	}
	
	public List<Map<String, Object>> getVehicleDetailOnly(String billIds)
	{
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("SELECT T.*,ROWNUM ROW_NUM FROM \n");
		sbSql.append("(SELECT G.MATERIAL_ID,\n");
		sbSql.append("        G.MATERIAL_CODE,\n");
		sbSql.append("        G.MATERIAL_NAME,\n");
		sbSql.append("        VMG.GROUP_CODE,\n");
		sbSql.append("        VMG.GROUP_NAME,\n");
		sbSql.append("        B.BO_NO,\n");
		sbSql.append("        TV.VIN,A.AREA_ID,\n");
		sbSql.append("        TV.ENGINE_NO, '0' DISTANCE,'0' AMOUNT, '0' SETTLE_AMOUNT,\n");
		sbSql.append("        TO_CHAR(D.OUT_DATE,'YYYY-MM-DD') OUT_DATE,\n");
		sbSql.append("        TO_CHAR(D.ACC_DATE,'YYYY-MM-DD') STORAGE_DATE\n");
		sbSql.append("FROM    TT_SALES_WAYBILL A,\n");
		sbSql.append("        TT_SALES_BOARD B,\n");
		sbSql.append("        TT_SALES_BO_DETAIL C,\n");
		sbSql.append("        TT_SALES_ALLOCA_DE D,\n");
		sbSql.append("        TM_VHCL_MATERIAL G,\n");
		sbSql.append("        TM_VHCL_MATERIAL_GROUP VMG,\n");
		sbSql.append("        TM_VEHICLE TV\n");
		sbSql.append("WHERE   A.BILL_ID = C.BILL_ID\n");
		sbSql.append("AND     B.BO_ID = C.BO_ID\n");
		sbSql.append(" AND    C.BO_DE_ID = D.BO_DE_ID\n");
		sbSql.append(" AND    D.VEHICLE_ID = TV.VEHICLE_ID\n");
		sbSql.append(" AND    TV.MATERIAL_ID = G.MATERIAL_ID\n");
		sbSql.append(" AND    TV.SERIES_ID = VMG.GROUP_ID");
		sbSql.append(" AND    A.BILL_ID IN (" + billIds + "))T\n");
		return dao.pageQuery(sbSql.toString(), null, dao.getFunName());
	}
	
	public List<Map<String, Object>> getVehicleBySeries(String billIds,String areaId)
	{
		StringBuffer sbSql = new StringBuffer();
		if(!"".equals(areaId)){
			sbSql.append("SELECT TO_CHAR(SUM(NVL(T.VEHICLE_NUM,0))) VEHICLE_NUM FROM \n");
			sbSql.append("(SELECT AA.GROUP_ID,\n");
			sbSql.append("       AA.GROUP_CODE,\n");
			sbSql.append("       AA.GROUP_NAME,\n");
			sbSql.append("       COUNT(*) VEHICLE_NUM\n");
			sbSql.append("FROM\n");
			sbSql.append("(SELECT  G.GROUP_ID,\n");
			sbSql.append("        G.GROUP_CODE,\n");
			sbSql.append("        G.GROUP_NAME\n");
			sbSql.append("FROM    TT_SALES_WAYBILL A,\n");
			sbSql.append("        TT_SALES_BOARD B,\n");
			sbSql.append("        TT_SALES_BO_DETAIL C,\n");
			sbSql.append("        TT_SALES_ALLOCA_DE D,\n");
			sbSql.append("        TM_VHCL_MATERIAL_GROUP G,\n");
			sbSql.append("        TM_VEHICLE TV\n");
			sbSql.append("WHERE   A.BILL_ID = C.BILL_ID\n");
			sbSql.append("AND     B.BO_ID = C.BO_ID\n");
			sbSql.append(" AND    C.BO_DE_ID = D.BO_DE_ID\n");
			sbSql.append(" AND    D.VEHICLE_ID = TV.VEHICLE_ID\n");
			sbSql.append(" AND    TV.SERIES_ID = G.GROUP_ID\n");
			sbSql.append(" AND    A.BILL_ID IN (" + billIds + ")\n");
			sbSql.append(" )AA\n");
			sbSql.append(" GROUP BY AA.GROUP_ID,AA.GROUP_CODE,AA.GROUP_NAME)T");
		}else{
			sbSql.append("SELECT T.*, ROWNUM ROW_NUM FROM \n");
			sbSql.append("(SELECT AA.GROUP_ID,\n");
			sbSql.append("       AA.GROUP_CODE,\n");
			sbSql.append("       AA.GROUP_NAME,\n");
			sbSql.append("       COUNT(*) VEHICLE_NUM,'0' COUNT\n");
			sbSql.append("FROM\n");
			sbSql.append("(SELECT  G.GROUP_ID,\n");
			sbSql.append("        G.GROUP_CODE,\n");
			sbSql.append("        G.GROUP_NAME\n");
			sbSql.append("FROM    TT_SALES_WAYBILL A,\n");
			sbSql.append("        TT_SALES_BOARD B,\n");
			sbSql.append("        TT_SALES_BO_DETAIL C,\n");
			sbSql.append("        TT_SALES_ALLOCA_DE D,\n");
			sbSql.append("        TM_VHCL_MATERIAL_GROUP G,\n");
			sbSql.append("        TM_VEHICLE TV\n");
			sbSql.append("WHERE   A.BILL_ID = C.BILL_ID\n");
			sbSql.append("AND     B.BO_ID = C.BO_ID\n");
			sbSql.append(" AND    C.BO_DE_ID = D.BO_DE_ID\n");
			sbSql.append(" AND    D.VEHICLE_ID = TV.VEHICLE_ID\n");
			sbSql.append(" AND    TV.SERIES_ID = G.GROUP_ID\n");
			sbSql.append(" AND    A.BILL_ID IN (" + billIds + ")\n");
			sbSql.append(" )AA\n");
			sbSql.append(" GROUP BY AA.GROUP_ID,AA.GROUP_CODE,AA.GROUP_NAME)T");
		}	
		return dao.pageQuery(sbSql.toString(), null, dao.getFunName());
	}
	
	/**
	 * 判断该运单计算运费是否是特殊省市
	 * 
	 * @param billId
	 * @return
	 */
	public List<Map<String, Object>> getSpecialFare(String billId)
	{
		String sql = "SELECT  A.FARE_ID FROM TM_SPECIAL_CITY_FARE A,TT_SALES_WAYBILL B,TM_VS_ADDRESS TVA, \n"
						+"TT_SALES_BO_DETAIL TSBD, TT_SALES_ALLOCA_DE TSAD, TM_VEHICLE TV, TM_VHCL_MATERIAL_GROUP VMG \n"
						+ "WHERE B.AREA_ID = A.YIELDLY AND B.ADDRESS_ID = TVA.ID \n"
						+ "AND TVA.AREA_ID = A.CITY_ID AND B.BILL_ID =" + billId
						+" AND TSBD.BILL_ID = B.BILL_ID AND TSAD.BO_DE_ID = TSBD.BO_DE_ID \n"
						+" AND TSAD.VEHICLE_ID = TV.VEHICLE_ID AND TV.SERIES_ID = VMG.GROUP_ID AND TV.SERIES_ID = A.SERIES_ID";
		return dao.pageQuery(sql, null, getFunName());
	}
	
	/**
	 * 判断该运单里程数是否已经维护
	 * 
	 * @param billId
	 * @return
	 */
	public List<Map<String, Object>> checkDistance(String billId)
	{
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("SELECT TSCD.DIS_ID,TO_CHAR(TSCD.DISTANCE) DISTANCE \n");
		sbSql.append("FROM   TT_SALES_WAYBILL TSW,\n");
		sbSql.append("       TT_SALES_CITY_DIS TSCD,\n");
		sbSql.append("       TM_VS_ADDRESS TVA,\n");
		sbSql.append("       TM_REGION TR\n");
		sbSql.append("WHERE  TSW.ADDRESS_ID = TVA.ID\n");
		sbSql.append("AND    TVA.AREA_ID = TR.REGION_CODE\n");
		sbSql.append("AND    TR.REGION_ID = TSCD.CITY_ID\n");
		sbSql.append("AND    TSW.AREA_ID = TSCD.YIELDLY\n"); 
		sbSql.append("AND    TSW.BILL_ID = "+billId+"\n"); 
		return dao.pageQuery(sbSql.toString(), null, getFunName());
	}
	
	/**
	 * 汇总运单中的车辆车系信息
	 * @param billId
	 * @return
	 */
	public List<Map<String, Object>> getSeriesByBillId(String billId){
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("SELECT  GROUP_CODE,GROUP_NAME,TO_CHAR(GROUP_ID) GROUP_ID \n");
		sbSql.append("FROM    TT_SALES_BO_DETAIL TSBD,\n");
		sbSql.append("        TT_SALES_ALLOCA_DE TSAD,\n");
		sbSql.append("        TM_VEHICLE TV,\n");
		sbSql.append("        TM_VHCL_MATERIAL_GROUP VMG\n");
		sbSql.append("WHERE   TSBD.BO_DE_ID = TSAD.BO_DE_ID\n");
		sbSql.append("AND     TSAD.VEHICLE_ID = TV.VEHICLE_ID\n");
		sbSql.append("AND     TV.SERIES_ID = VMG.GROUP_ID\n");
		sbSql.append("AND     TSBD.BILL_ID = "+billId+"\n");
		sbSql.append("GROUP BY GROUP_ID,GROUP_CODE,GROUP_NAME"); 
		return dao.pageQuery(sbSql.toString(), null, getFunName());
	}
	
	/**
	 * 根据产地联动物流公司下拉框查询
	 * @param areaIds
	 * @return
	 */
	public List<Map<String, Object>> getLogiListByArea(String areaIds){
		StringBuffer  sql = new StringBuffer();
		sql.append("SELECT LOGI_ID, LOGI_CODE,LOGI_NAME,LOGI_FULL_NAME FROM TT_SALES_LOGI WHERE 1 = 1 \n");
		if(areaIds != null && !"".equals(areaIds)){
			sql.append("AND YIELDLY IN ("+areaIds+")\n");
		}
		return dao.pageQuery(sql.toString(), null, getFunName());
		
	}
	
	public List<Map<String, Object>> getSendBillCheckDate(String billId){
		StringBuffer  sbSql = new StringBuffer();
		sbSql.append("SELECT  TSAD.VEHICLE_ID,TV.VIN,TO_CHAR(TSAD.ACC_DATE,'yyyy-MM-dd HH:mi:ss') ACC_DATE \n");
		sbSql.append("  FROM    TT_SALES_WAYBILL TSW,\n");
		sbSql.append("          TT_SALES_BO_DETAIL TSBD,\n");
		sbSql.append("          TT_SALES_ALLOCA_DE TSAD,\n");
		sbSql.append("          TM_VEHICLE TV\n");
		sbSql.append(" WHERE    TSW.BILL_ID = TSBD.BILL_ID\n");
		sbSql.append(" AND      TSBD.BO_DE_ID = TSAD.BO_DE_ID\n");
		sbSql.append(" AND      TSAD.IS_ACC = "+Constant.IF_TYPE_YES+"\n");
		sbSql.append(" AND      TSW.BILL_ID = "+billId+"\n");
		sbSql.append(" AND      TSAD.VEHICLE_ID = TV.VEHICLE_ID\n");
		sbSql.append(" AND      ROWNUM = 1\n");
		sbSql.append(" ORDER BY TSAD.ACC_DATE DESC"); 
		return dao.pageQuery(sbSql.toString(), null, dao.getFunName());
	} 
	
	/**
	 * 运单结算查看(按车辆明细)
	 * @param billIds
	 * @return
	 */
	public List<Map<String, Object>> getBalDetailByVclDetail(String billIds){
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("SELECT T.*,ROWNUM ROW_NUM FROM ");
		sbSql.append("( SELECT  TSB.BO_NO,\n");
		sbSql.append("        VM.MATERIAL_CODE,\n");
		sbSql.append("        VM.MATERIAL_NAME,\n");
		sbSql.append("        VMG.GROUP_CODE,\n");
		sbSql.append("        VMG.GROUP_NAME,\n");
		sbSql.append("        TV.VIN,\n");
		sbSql.append("        TV.ENGINE_NO,\n");
		sbSql.append("        TO_CHAR(TSAD.OUT_DATE,'YYYY-MM-DD') OUT_DATE,\n");
		sbSql.append("        TO_CHAR(TSAD.ACC_DATE,'YYYY-MM-DD') STORAGE_DATE,\n");
		sbSql.append("        TSAD.SEND_DISTANCE,\n");
		sbSql.append("        TSAD.SINGLE_PRICE,\n");
		sbSql.append("        TO_CHAR(TSW.BILL_CRT_DATE,'yyyy-MM-dd hh24:mi:ss') BILL_CRT_DATE, \n");
		sbSql.append("        TSAD.BAL_AMOUNT\n");
		sbSql.append("  FROM  TT_SALES_WAYBILL TSW,\n");
		sbSql.append("        TT_SALES_BOARD TSB,\n");
		sbSql.append("        TT_SALES_BO_DETAIL TSBD,\n");
		sbSql.append("        TT_SALES_ALLOCA_DE TSAD,\n");
		sbSql.append("        TM_VEHICLE TV,\n");
		sbSql.append("        TM_VHCL_MATERIAL_GROUP VMG,\n");
		sbSql.append("        TM_VHCL_MATERIAL VM,\n");
		sbSql.append("        TT_SALES_BACKCRM_DETAIL BAK");
		sbSql.append(" WHERE  TSW.BILL_ID = TSBD.BILL_ID\n");
		sbSql.append(" AND    TSBD.BO_ID = TSB.BO_ID\n");
		sbSql.append(" AND    TSBD.BO_DE_ID = TSAD.BO_DE_ID\n");
		sbSql.append(" AND    TSAD.VEHICLE_ID = TV.VEHICLE_ID\n");
		sbSql.append(" AND    TV.SERIES_ID = VMG.GROUP_ID\n");
		sbSql.append(" AND    TV.VEHICLE_ID = BAK.VEHICLE_ID\n");
		if(!"".equals(billIds)){
			sbSql.append(" AND BAK.BAL_ID IN ("+billIds+")\n");
		}
		sbSql.append(" AND    TV.MATERIAL_ID = VM.MATERIAL_ID ) T"); 	
		return dao.pageQuery(sbSql.toString(), null, dao.getFunName());
	}
	
	public List<Map<String, Object>> getBalDetailByVclSeries(String billIds){
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("SELECT B.*,ROWNUM ROW_NUM FROM (\n");
		sbSql.append("SELECT A.GROUP_CODE,\n");
		sbSql.append("       A.GROUP_NAME,COUNT(A.VIN) VEHICLE_NUM,\n");
		sbSql.append("SUM(DECODE(A.PROVINCE_ID,460000,270,540000,NVL(A.BAL_AMOUNT, 0)*0.15,0)) FJ_AMOUNT,"); //这里是附加费用，西藏上浮15%，海南每台加270
		sbSql.append("       SUM(NVL(A.BAL_AMOUNT,0)) BAL_AMOUNT FROM\n");
		sbSql.append("(SELECT  TSB.BO_NO,\n");
		sbSql.append("        VM.MATERIAL_CODE,\n");
		sbSql.append("        VM.MATERIAL_NAME,\n");
		sbSql.append("        VMG.GROUP_CODE,\n");
		sbSql.append("        VMG.GROUP_NAME,\n");
		sbSql.append("        TV.VIN,\n");
		sbSql.append("        TV.ENGINE_NO,\n");
		sbSql.append("        TO_CHAR(TSAD.OUT_DATE,'YYYY-MM-DD') OUT_DATE,\n");
		sbSql.append("        TO_CHAR(TSAD.ACC_DATE,'YYYY-MM-DD') STORAGE_DATE,\n");
		sbSql.append("        TSAD.SEND_DISTANCE,\n");
		sbSql.append("        TSAD.SINGLE_PRICE,\n");
		sbSql.append("        TVA.PROVINCE_ID,"); 
		sbSql.append("        TSAD.BAL_AMOUNT\n");
		sbSql.append("  FROM  TT_SALES_WAYBILL TSW,\n");
		sbSql.append("        TT_SALES_BOARD TSB,\n");
		sbSql.append("        TT_SALES_BO_DETAIL TSBD,\n");
		sbSql.append("        TT_SALES_ALLOCA_DE TSAD,\n");
		sbSql.append("        TM_VEHICLE TV,\n");
		sbSql.append("        TM_VHCL_MATERIAL_GROUP VMG,\n");
		sbSql.append("        TM_VHCL_MATERIAL VM,\n");
		sbSql.append("        TT_SALES_BACKCRM_DETAIL BAK,\n");
		sbSql.append("        TM_VS_ADDRESS          TVA"); 
		sbSql.append(" WHERE  TSW.BILL_ID = TSBD.BILL_ID\n");
		sbSql.append(" AND    TSBD.BO_ID = TSB.BO_ID\n");
		sbSql.append(" AND    TSBD.BO_DE_ID = TSAD.BO_DE_ID\n");
		sbSql.append(" AND    TSAD.VEHICLE_ID = TV.VEHICLE_ID\n");
		sbSql.append(" AND    TV.SERIES_ID = VMG.GROUP_ID\n");
		sbSql.append(" AND    TV.MATERIAL_ID = VM.MATERIAL_ID\n");
		sbSql.append(" AND    TV.VEHICLE_ID = BAK.VEHICLE_ID\n");
		sbSql.append(" AND    TSBD.ADDRESS_ID=TVA.ID\n"); 
		sbSql.append(" AND    bak.BAL_ID IN ("+billIds+")) A\n");
		sbSql.append(" GROUP BY A.GROUP_CODE,A.GROUP_NAME ) B"); 	
		return dao.pageQuery(sbSql.toString(), null, dao.getFunName());
	}
	
	/**
	 * 运单打印明细查询（车辆明细）
	 * @param bal_id
	 * @return
	 */
	public List<Map<String, Object>> getPrintVehicleDetail(String bal_id){
		StringBuffer sbSql = new StringBuffer();
//		sbSql.append("SELECT T.*,ROWNUM ROW_NUM FROM \n");
//		sbSql.append("(SELECT  BAL.BAL_ID,BAL.BAL_NO,TO_CHAR(TSW.CREATE_DATE,'MM-dd') CREATE_DATE,\n");
//		sbSql.append("        TSW.BILL_NO,TMD.DEALER_NAME,\n");
//		sbSql.append("        VMG.GROUP_CODE,\n");
//		sbSql.append("        VMG.GROUP_NAME,\n");
//		sbSql.append("        NVL(TSAD.SEND_DISTANCE,0) DISTANCE,\n");
//		sbSql.append("        NVL(TSAD.SINGLE_PRICE,0) SEND_FARE,\n");
//		sbSql.append("        NVL(TSAD.BAL_AMOUNT,0) AMOUNT,\n");
//		sbSql.append("        TR.REGION_NAME\n");
//		sbSql.append("  FROM  TT_SALES_BALANCE BAL,\n");
//		sbSql.append("        TT_SALES_WAYBILL TSW,\n");
//		sbSql.append("        TT_SALES_BO_DETAIL TSBD,\n");
//		sbSql.append("        TT_SALES_ALLOCA_DE TSAD,\n");
//		sbSql.append("        TM_VEHICLE TV,\n");
//		sbSql.append("        TM_VHCL_MATERIAL_GROUP VMG,\n");
//		sbSql.append("        TM_DEALER TMD,\n");
//		sbSql.append("        TM_VS_ADDRESS TVA,\n");
//		sbSql.append("        TM_REGION TR\n");
//		sbSql.append(" WHERE  TSAD.VEHICLE_ID = TV.VEHICLE_ID\n");
//		sbSql.append(" AND    TV.SERIES_ID = VMG.GROUP_ID\n");
//		sbSql.append(" AND    TSAD.BO_DE_ID = TSBD.BO_DE_ID\n");
//		sbSql.append(" AND    TSBD.BILL_ID = TSW.BILL_ID\n");
//		sbSql.append(" AND    TSW.BAL_ID = BAL.BAL_ID\n");
//		sbSql.append(" AND    TSW.SEND_DEALER_ID = TMD.DEALER_ID\n");
//		sbSql.append(" AND    TSW.ADDRESS_ID = TVA.ID\n");
//		sbSql.append(" AND    TVA.CITY_ID = TR.REGION_CODE\n");
//		sbSql.append(" AND    BAL.BAL_ID = "+bal_id+"\n");
//		sbSql.append(" ORDER BY TSW.BILL_NO ) T"); 
		sbSql.append("SELECT T.*,ROWNUM ROW_NUM FROM\n");
		sbSql.append("(SELECT TO_CHAR(TSW.CREATE_DATE,'MM-dd') CREATE_DATE,\n");
		sbSql.append("        TSW.BILL_NO,TMD.DEALER_NAME,\n");
		sbSql.append("        VMG.GROUP_CODE,\n");
		sbSql.append("        VMG.GROUP_NAME,\n");
		sbSql.append("        NVL(TSAD.SEND_DISTANCE,0) DISTANCE,\n");
		sbSql.append("        NVL(TSAD.SINGLE_PRICE,0) SEND_FARE,\n");
		sbSql.append("        COUNT(*) COUNT,\n");
		sbSql.append("        SUM(NVL(TSAD.BAL_AMOUNT,0)) AMOUNT,\n");
		sbSql.append("        TR1.REGION_NAME||TR.REGION_NAME REGION_NAME \n");
		sbSql.append("  FROM  TT_SALES_BALANCE BAL,\n");
		sbSql.append("        TT_SALES_WAYBILL TSW,\n");
		sbSql.append("        TT_SALES_BO_DETAIL TSBD,\n");
		sbSql.append("        TT_SALES_ALLOCA_DE TSAD,\n");
		sbSql.append("        TM_VEHICLE TV,\n");
		sbSql.append("        TM_VHCL_MATERIAL_GROUP VMG,\n");
		sbSql.append("        TM_DEALER TMD,\n");
		sbSql.append("        TM_VS_ADDRESS TVA,\n");
		sbSql.append("        TM_REGION TR,TM_REGION TR1\n");
		sbSql.append(" WHERE  TSAD.VEHICLE_ID = TV.VEHICLE_ID\n");
		sbSql.append(" AND    TV.SERIES_ID = VMG.GROUP_ID\n");
		sbSql.append(" AND    TSAD.BO_DE_ID = TSBD.BO_DE_ID(+)\n");
		sbSql.append(" AND    TSBD.BILL_ID = TSW.BILL_ID(+)\n");
		sbSql.append(" AND    TSW.BAL_ID = BAL.BAL_ID\n");
		sbSql.append(" AND    TSW.SEND_DEALER_ID = TMD.DEALER_ID\n");
		sbSql.append(" AND    TSW.ADDRESS_ID = TVA.ID(+)\n");
		sbSql.append(" AND    TVA.AREA_ID = TR.REGION_CODE AND TVA.CITY_ID = TR1.REGION_CODE \n");
		sbSql.append(" AND    BAL.BAL_ID = "+bal_id+"\n");
		sbSql.append(" GROUP BY BAL.BAL_NO,TO_CHAR(TSW.CREATE_DATE,'MM-dd'),\n");
		sbSql.append("          BILL_NO,TMD.DEALER_NAME,VMG.GROUP_CODE,\n");
		sbSql.append("          VMG.GROUP_NAME,TSAD.SEND_DISTANCE,\n");
		sbSql.append("          TSAD.SINGLE_PRICE, TR.REGION_NAME,TR1.REGION_NAME\n");
		sbSql.append(" ORDER BY TSW.BILL_NO ) T"); 
		return dao.pageQuery(sbSql.toString(), null, dao.getFunName());
	}
	
	/**
	 * 得到结算单基础信息
	 * @param bal_id
	 * @return
	 */
	public Map<String, Object> getBalDetail(String bal_id){
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("SELECT  SL.LOGI_FULL_NAME,\n");
		sbSql.append("        TO_CHAR(BAL.CREATE_DATE,'yyyy') YEAR,\n");
		sbSql.append("        TO_CHAR(BAL.CREATE_DATE,'MM') MONTH,\n");
		sbSql.append("        BAL.BAL_NO\n");
		sbSql.append("  FROM  TT_SALES_BALANCE BAL,\n");
		sbSql.append("        TT_SALES_LOGI SL\n");
		sbSql.append(" WHERE  BAL.LOGI_ID = SL.LOGI_ID\n");
		sbSql.append(" AND    BAL.BAL_ID = "+bal_id+"\n"); 
		return dao.pageQueryMap(sbSql.toString(), null, dao.getFunName());
	}
	
	public void batchReturnCheck(List<Object> param,String bill_id){
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("UPDATE TT_SALES_WAYBILL T SET T.REMARK = ?,\n");
		sbSql.append("                              T.ARR_DATE = ?,\n");
		sbSql.append("                              T.BACK_CRM_DATE = ?,\n");
		sbSql.append("                              T.BACK_CRM_PER = ?,\n");
		sbSql.append("                              T.SEND_STATUS = "+Constant.SEND_STATUS_02+"\n");
		sbSql.append("                       WHERE  T.BILL_ID IN ("+bill_id+")\n"); 
		dao.update(sbSql.toString(), param);
	}

	/**根据运单信息查询出是否在有在途维护的记录s**/
	public List<Map<String,Object>> getVechileInof(String billId) {
		StringBuffer sql = new StringBuffer();
		sql.append("select addr.detail_id from  tt_vehicle_address addr where addr.bill_id = ?");
		List<Object> params = new ArrayList();
		params.add(billId);
		return dao.pageQuery(sql.toString(), params, getFunName());
	}
	
	
	public  List<Map<String, Object>> queryVehicleStatus(String billId){
		StringBuffer sql = new StringBuffer();
		List<Object> parasList = new ArrayList();
		sql.append("SELECT  veh.vehicle_id,bill.bill_no,bill.veh_num \n");
		sql.append(" FROM  tt_sales_bo_detail det,tt_sales_waybill   bill,tt_sales_alloca_de alloca, \n");
		sql.append("       tm_vehicle         veh \n");
		sql.append(" WHERE det.bill_id = bill.bill_id \n");
		sql.append("   AND  det.bo_de_id = alloca.bo_de_id \n");
		sql.append("   AND alloca.vehicle_id = veh.vehicle_id \n");
		sql.append("   AND bill.bill_id = ? ");
		sql.append("   AND veh.LIFE_CYCLE = ? ");
		parasList.add(billId);
		parasList.add(Constant.VEHICLE_LIFE_03);
		return dao.pageQuery(sql.toString(), parasList, getFunName());
	}
	
	public List<Map<String, Object>> queryWayBillByBillIds(String billIds){
		String sql = "SELECT tsw.*,tsad.vehicle_id,TSAD.IS_ACC,F_GET_TCCODE_DESC(TSAD.IS_ACC) IS_ACC_STR," +
				" TSAD.ACC_DATE,F_GET_TCUSER_NAME(TSAD.ACC_BY) ACC_BY,TV.VIN,TV.PASS_NO,VW.SERIES_NAME,VW.MODEL_NAME,VW.PACKAGE_NAME,VW.COLOR_NAME" +
				" FROM TT_SALES_WAYBILL tsw" +
				" LEFT JOIN tt_sales_bo_detail tsbd ON tsw.bill_id=tsbd.bill_id" +
				" AND tsbd.board_num > 0 LEFT JOIN tt_sales_alloca_de tsad" +
				" ON tsbd.bo_de_id=tsad.bo_de_id AND tsad.status=10011001 LEFT JOIN TM_VEHICLE TV ON TSAD.VEHICLE_ID=TV.VEHICLE_ID" +
				" LEFT JOIN VW_MATERIAL_GROUP_MAT VW ON TV.MATERIAL_ID=VW.MATERIAL_ID" +
				" LEFT JOIN TT_SALES_BACKCRM_DETAIL BKD ON TSAD.VEHICLE_ID=BKD.VEHICLE_ID AND BKD.STATUS="+Constant.STATUS_ENABLE+" WHERE BKD.BACKCRM_ID IS NULL AND tsw.BILL_ID IN ("+billIds+")"+
				" ORDER BY TV.PASS_NO ASC";
		return pageQuery(sql, null, getFunName());
	}
	
	/**
	 * 模糊查找VIN
	 * @author liufazhong
	 */
	public List<Map<String, Object>> queryByVin(String billId,String vin){
		String sql = "SELECT tsw.*,tsad.vehicle_id,TSAD.IS_ACC,F_GET_TCCODE_DESC(TSAD.IS_ACC) IS_ACC_STR," +
		" TSAD.ACC_DATE,F_GET_TCUSER_NAME(TSAD.ACC_BY) ACC_BY,TV.VIN,VW.SERIES_NAME,VW.MODEL_NAME,VW.PACKAGE_NAME,VW.COLOR_NAME" +
		" FROM TT_SALES_WAYBILL tsw" +
		" LEFT JOIN tt_sales_bo_detail tsbd ON tsw.bill_id=tsbd.bill_id" +
		" AND tsbd.board_num > 0 LEFT JOIN tt_sales_alloca_de tsad" +
		" ON tsbd.bo_de_id=tsad.bo_de_id AND tsad.status=10011001 LEFT JOIN TM_VEHICLE TV ON TSAD.VEHICLE_ID=TV.VEHICLE_ID" +
		" LEFT JOIN VW_MATERIAL_GROUP_MAT VW ON TV.MATERIAL_ID=VW.MATERIAL_ID" +
		" LEFT JOIN TT_SALES_BACKCRM_DETAIL BKD ON TSAD.VEHICLE_ID=BKD.VEHICLE_ID AND BKD.STATUS="+Constant.STATUS_ENABLE+" WHERE BKD.BACKCRM_ID IS NULL AND tsw.BILL_ID="+billId+"";
		if (!XHBUtil.IsNull(vin)) {
			sql += " AND tv.VIN like '%"+vin+"%'";
		}
		return pageQuery(sql, null, getFunName());
	}
	
	/**
	 * 根据车辆ID查询确认状态
	 * @author liufazhong
	 */
	public List<Map<String, Object>> queryBackCrmByVehicleId(String vehicleId){
		String sql = "SELECT * FROM TT_SALES_BACKCRM_DETAIL WHERE VEHICLE_ID = "+vehicleId+" AND STATUS="+Constant.STATUS_ENABLE;
		return pageQuery(sql, null, getFunName());
	}
	
	/**
	 * 获取已经生成数量
	 * @author liufazhong
	 */
	public List<Map<String, Object>> queryCountCrmByBillId(String billId) {
		String sql = "SELECT COUNT(1) COUNT_BILL FROM TT_SALES_BACKCRM_DETAIL WHERE BILL_ID IN ("+billId+") AND STATUS="+Constant.STATUS_ENABLE;
		return pageQuery(sql, null, getFunName());
	}
	
	public List<Map<String, Object>> queryCountBalByBillId(String billId) {
		String sql = "SELECT COUNT(1) COUNT_BILL FROM TT_SALES_BACKCRM_DETAIL WHERE NVL(BAL_ID,0) > 0 AND BILL_ID IN ("+billId+") AND STATUS="+Constant.STATUS_ENABLE;
		return pageQuery(sql, null, getFunName());
	}
	
	/**
	 * 根据主键查询运单
	 * @author liufazhong
	 */
	public List<Map<String, Object>> queryWayBillById(String billId) {
		String sql = "SELECT SUM(VEH_NUM) VEH_NUM FROM TT_SALES_WAYBILL WHERE BILL_ID IN ("+billId+")";
		return pageQuery(sql, null, getFunName());
	}
	
	/**
	 * 查询车辆是否已经结算
	 * @author liufazhong
	 */
	public List<Map<String, Object>> queryBillType(String vehicleIds){
		String sql = "SELECT * FROM TT_SALES_BACKCRM_DETAIL WHERE NVL(BAL_ID,0) > 0 AND VEHICLE_ID IN ("+vehicleIds+") AND STATUS="+Constant.STATUS_ENABLE;
		return pageQuery(sql, null, getFunName());
	}
	
	/**
	 * 更新ID到回厂确认表
	 * @author liufazhong
	 */
	public int updateBalIdToBackCrm(String vehicleIds,String balId) {
		String sql = "UPDATE TT_SALES_BACKCRM_DETAIL SET BAL_ID = "+balId+" WHERE VEHICLE_ID IN ("+vehicleIds+")";
		return update(sql, null);
	}
}
