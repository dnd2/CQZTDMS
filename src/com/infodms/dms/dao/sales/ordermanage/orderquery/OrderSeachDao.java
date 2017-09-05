package com.infodms.dms.dao.sales.ordermanage.orderquery;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.sales.planmanage.PlanUtil.PlanUtil;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TmBusinessParaPO;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

public class OrderSeachDao  extends BaseDao<PO> {
	public static Logger logger = Logger.getLogger(OrderSeachDao.class);
	private static final OrderSeachDao dao = new OrderSeachDao();

	public static final OrderSeachDao getInstance() {
		return dao;
	}

	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public PageResult<Map<String, Object>> getOrderSeachQuery(Map<String, Object> map, int curPage, int pageSize) throws Exception {

		String orderYear = (String) map.get("orderYear");
		String orderWeek = (String) map.get("orderWeek");
		String areaId = (String) map.get("areaId");
		String area = (String) map.get("area");
		String groupCode = (String) map.get("groupCode");
		String dealerCode = (String) map.get("dealerCode");
		String orderType = (String) map.get("orderType");
		String orderNo = (String) map.get("orderNo");
		String companyId = (String) map.get("companyId");
		String reqStatus = (String) map.get("reqStatus");
		String orgCode = (String) map.get("orgCode");
		String dutyType = (String) map.get("dutyType");
		String orgId = (String) map.get("orgId");
		String beginTime = (String) map.get("beginTime");
		String endTime = (String) map.get("endTime");
		String operateType=(String)map.get("operateType");
		String dealerId=(String)map.get("dealerId");
		List<Object> params = new LinkedList<Object>();

		StringBuffer sql = new StringBuffer();

		sql.append("SELECT TVO.ORDER_ID,\n");
		sql.append("	   TVO.ORDER_ORG_ID,\n");
		sql.append("       TVDR.REQ_ID,\n");
		sql.append("       TMD.DEALER_CODE,\n");
		sql.append("       TMD.DEALER_SHORTNAME DEALER_NAME,\n");
		sql.append("       DECODE(TVDR.CALL_LEAVEL, " + Constant.DEALER_LEVEL_02 + ", TMD2.DEALER_CODE, TMD1.DEALER_CODE) ORDER_DEALER_CODE,\n");

		sql.append("       DECODE(TVDR.CALL_LEAVEL, " + Constant.DEALER_LEVEL_02 + ", TMD2.DEALER_SHORTNAME, TMD1.DEALER_SHORTNAME) ORDER_DEALER_NAME,\n");

		sql.append("       TVO.ORDER_NO,\n");
		sql.append("       TO_CHAR(TVO.RAISE_DATE, 'YYYY-MM-DD') RAISE_DATE,\n");
		sql.append("       TVO.ORDER_YEAR,\n");
		sql.append("       TVO.ORDER_WEEK,\n");
		sql.append("       TVO.ORDER_TYPE,\n");
		sql.append("       TC2.CODE_DESC REQ_STATUS,\n");
		sql.append("       NVL(TVO.VER, 0) VER,\n");
		sql.append("       TVDR.REQ_TOTAL_AMOUNT,\n");
		sql.append("       TVDR.REQ_TOTAL_PRICE,\n");
		sql.append("	   tvdr.F_AUDIT_TIME,\n");

		sql.append("       TVAT.TYPE_NAME FUND_TYPE,TVDR.DLVRY_REQ_NO,\n");
		sql.append("       NVL(SUM(TVOD.ORDER_AMOUNT), 0) ORDER_AMOUNT,\n");
		sql.append("       NVL(SUM(TVDRD.RESERVE_AMOUNT), 0) RESERVE_AMOUNT\n");
		sql.append("  FROM TT_VS_ORDER              TVO,\n");
		sql.append("       TT_VS_ORDER_DETAIL       TVOD,\n");
		sql.append("       TT_VS_DLVRY_REQ          TVDR,\n");
		sql.append("       TT_VS_DLVRY_REQ_DTL      TVDRD,\n");
		sql.append("       TM_DEALER                TMD,\n");
		sql.append("       TM_DEALER                TMD1,\n");
		sql.append("       TM_DEALER                TMD2,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP_R TVMGR,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP   TVMG,\n");
		sql.append("       TM_DEALER_ORG_RELATION   TDOR,\n");
		sql.append("	   VW_ORG_DEALER            VW,\n");
		sql.append("       TT_VS_ACCOUNT_TYPE       TVAT,TC_CODE TC2\n");
		sql.append(" WHERE TVO.ORDER_ID = TVOD.ORDER_ID\n");
		sql.append("   AND TVO.ORDER_ID = TVDR.ORDER_ID  AND TC2.CODE_ID=TVDR.REQ_STATUS\n");
		sql.append("   AND TVDR.REQ_ID = TVDRD.REQ_ID\n");
		sql.append("   AND TVOD.DETAIL_ID = TVDRD.ORDER_DETAIL_ID\n");
		sql.append("   AND TVO.BILLING_ORG_ID = TMD.DEALER_ID\n");
		sql.append("   AND TVDR.ORDER_DEALER_ID = TMD2.DEALER_ID(+)\n");
		sql.append("   AND TVO.ORDER_ORG_ID = TMD1.DEALER_ID\n");
		sql.append("   AND TVOD.MATERIAL_ID = TVMGR.MATERIAL_ID\n");
		sql.append("   AND TVMGR.GROUP_ID = TVMG.GROUP_ID\n");
		sql.append("   AND TMD.DEALER_ID = TDOR.DEALER_ID\n");
		sql.append("   AND TVDR.FUND_TYPE = TVAT.TYPE_ID\n");
		sql.append("   AND VW.DEALER_ID=TMD.DEALER_ID\n");
		sql.append("   \n");
		 //经销商
		if(Constant.DUTY_TYPE_DEALER.intValue()==Integer.parseInt(dutyType)){
			sql.append("  AND (VW.DEALER_ID ="+dealerId+" )\n");
			//大区
		}else if(Constant.DUTY_TYPE_LARGEREGION.intValue()==Integer.parseInt(dutyType)){
			sql.append(" AND VW.DEALER_ID IN (SELECT VW.DEALER_ID FROM VW_ORG_DEALER_ALL_NEW VW WHERE  VW.ROOT_ORG_ID="+orgId+")\n");
			//小区
		}else if(Constant.DUTY_TYPE_SMALLREGION.intValue()==Integer.parseInt(dutyType)){
			sql.append(" AND VW.DEALER_ID IN (SELECT VW.DEALER_ID FROM VW_ORG_DEALER_ALL_NEW VW WHERE  VW.PQ_ORG_ID="+orgId+") \n");
		}
		/*if(operateType!=null&&"1".equals(operateType)){
			sql.append("   AND TVDR.REQ_STATUS IN (" + Constant.ORDER_REQ_STATUS_01 + ", " + Constant.ORDER_REQ_STATUS_05 + ", " + Constant.ORDER_REQ_STATUS_09 + ","+Constant.ORDER_REQ_STATUS_BH+")\n");
		}
		if(operateType!=null&&"2".equals(operateType)){
			sql.append("   AND TVDR.REQ_STATUS IN (" + Constant.ORDER_REQ_STATUS_FINAL + ")\n");
		}*/
		sql.append("   AND TVO.COMPANY_ID = " + companyId + "\n");
		if (orderType != null && !"".equals(orderType)) {
			sql.append("   AND TVO.ORDER_TYPE = " + orderType + "\n");
		}
		if (orderNo != null && !"".equals(orderNo)) {
			sql.append("   AND TVO.ORDER_NO LIKE '%" + orderNo + "%'\n");
		}
		if (orderYear != null && !"".equals(orderYear)) {
			sql.append("   AND TVO.ORDER_YEAR =" + orderYear + "\n");
		}
		if (orderWeek != null && !"".equals(orderWeek)) {
			sql.append("   AND TVO.ORDER_WEEK = " + orderWeek + "\n");
		}
		if (reqStatus != null && !"".equals(reqStatus)) {
			sql.append("   AND TVDR.REQ_STATUS = " + reqStatus + "\n");
		}
		//注释 
		/*if (areaId != null && !"-1".equals(areaId) && !"".equals(areaId)) {
			sql.append("   AND TVO.AREA_ID = " + areaId + "\n");
		}
		
		if (area != null && !"-1".equals(area) && !"".equals(area)) {
			sql.append("   AND TVO.AREA_ID IN( " + area + ")\n");
		}*/
		if (groupCode != null && !"".equals(groupCode)) {
			sql.append(Utility.getConSqlByParamForEqual(groupCode, params, "TVMG", "GROUP_CODE"));
		}
		if (dealerCode != null && !"".equals(dealerCode)) {
			sql.append(Utility.getConSqlByParamForEqual(dealerCode, params, "TMD", "DEALER_CODE"));
		}
		if (orgCode != null && !"".equals(orgCode)) {
			sql.append("   AND TVO.BILLING_ORG_ID IN\n");
			sql.append("       (SELECT M.DEALER_ID\n");
			sql.append("          FROM TM_DEALER M\n");
			sql.append("        CONNECT BY PRIOR M.DEALER_ID = M.PARENT_DEALER_D\n");
			sql.append("         START WITH M.STATUS = " + Constant.STATUS_ENABLE + "\n");
			sql.append("                AND M.DEALER_ID IN\n");
			sql.append("                    (SELECT REL.DEALER_ID\n");
			sql.append("                       FROM TM_DEALER_ORG_RELATION REL, TM_ORG ORG\n");
			sql.append("                      WHERE REL.ORG_ID = ORG.ORG_ID\n");
			sql.append("                        AND ORG.ORG_CODE IN\n");
			sql.append("                            (" + PlanUtil.createSqlStr(orgCode) + ")))");

		}
		/*
		 * if(dutyType!=null&&!"".equals(dutyType)){
		 * if(dutyType.equals(String.valueOf(Constant.DUTY_TYPE_LARGEREGION))||dutyType.equals(String.valueOf(Constant.DUTY_TYPE_SMALLREGION))){
		 * if(orgId!=null&&!"".equals(orgId)){ sql.append(" AND TDOR.ORG_ID =
		 * "+orgId+"\n" ); } } }
		 */

		/*if (!CommonUtils.isNullString(orgId)) {
			sql.append("   AND TDOR.ORG_ID in (" + orgId + ")\n");
		}*/

		if (beginTime != null && !beginTime.equals("")) {
			sql.append("   AND TVO.RAISE_DATE >= TO_DATE('" + beginTime + " 00:00:00','YYYY-MM-DD HH24:mi:ss') \n");
		}
		if (endTime != null && !endTime.equals("")) {
			sql.append("   AND TVO.RAISE_DATE < =TO_DATE('" + endTime + " 23:59:59','YYYY-MM-DD HH24:mi:ss')\n");

		}
		sql.append(" GROUP BY TVO.ORDER_ID,\n");
		sql.append("          TVDR.REQ_ID,\n");
		sql.append("          TMD.DEALER_CODE,\n");
		sql.append("          TMD.DEALER_SHORTNAME,\n");
		sql.append("          DECODE(TVDR.CALL_LEAVEL, " + Constant.DEALER_LEVEL_02 + ", TMD2.DEALER_CODE, TMD1.DEALER_CODE),\n");

		sql.append("          DECODE(TVDR.CALL_LEAVEL, " + Constant.DEALER_LEVEL_02 + ", TMD2.DEALER_SHORTNAME, TMD1.DEALER_SHORTNAME),\n");
		sql.append("	   TVO.ORDER_ORG_ID,\n");
		sql.append("          TVO.ORDER_NO,\n");
		sql.append("          TVO.RAISE_DATE,\n");
		sql.append("          TVO.ORDER_YEAR,\n");
		sql.append("          TVO.ORDER_WEEK,TVDR.DLVRY_REQ_NO,\n");
		sql.append("          TVO.ORDER_TYPE,\n");
		sql.append("          TC2.CODE_DESC,\n");
		sql.append("          TVO.VER,\n");
		sql.append("          TVDR.REQ_TOTAL_AMOUNT,");
		sql.append("       	  TVDR.REQ_TOTAL_PRICE,\n");
		sql.append("          TVAT.TYPE_NAME,\n");
		sql.append("		tvdr.F_AUDIT_TIME\n");
		sql.append("   		  ORDER BY TVO.RAISE_DATE DESC	");


		PageResult<Map<String, Object>> ps = dao.pageQuery(sql.toString(), params, getFunName(), pageSize, curPage);
		return ps;
	}
	
	public Map<String, Object> getOrderInfoByReqId(String reqId) throws Exception {

		StringBuffer sql = new StringBuffer();
			sql.append("SELECT DECODE(TVDR.CALL_LEAVEL, 10851002,TMD5.DEALER_SHORTNAME, TMD1.DEALER_SHORTNAME) DEALER_NAME1,\n");
			sql.append("       TMD1.DEALER_CODE,                                                                            \n");
			sql.append("       TMD2.DEALER_TYPE,                                                                            \n");
			sql.append("       TVDR.REQ_TOTAL_PRICE,                                                                        \n");
			sql.append("       TMD2.DEALER_SHORTNAME DEALER_NAME2,                                                          \n");
			sql.append("       TMD3.DEALER_SHORTNAME DEALER_NAME3,                                                          \n");
			sql.append("       TVO.ORDER_NO,                                                                                \n");
			sql.append("       NVL(TVDR.VER, 0) VER,                                                                        \n");
			sql.append("       TVO.ORDER_YEAR,                                                                              \n");
			sql.append("       TVO.ORDER_WEEK,                                                                              \n");
			sql.append("       TVO.ORDER_TYPE,                                                                              \n");
			sql.append("       TVO.PRODUCT_COMBO_ID,                                                                        \n");
			sql.append("       TVDR.AREA_ID,                                                                                \n");
			sql.append("       TVO.DELIVERY_TYPE,                                                                           \n");
			sql.append("       TMF.FLEET_NAME,                                                                              \n");
			sql.append("       TVDR.FLEET_ADDRESS,                                                                          \n");
			sql.append("       TVO.DELIVERY_ADDRESS ADDRESS_ID,                                                             \n");
			sql.append("       TVA.ADDRESS,                                                                                 \n");
			sql.append("       TVA.RECEIVE_ORG,                                                                             \n");
			sql.append("       TCC1.CODE_DESC ORDER_TYPE_NAME,                                                              \n");
			sql.append("       TCC2.CODE_DESC DELIVERY_TYPE_NAME,                                                           \n");
			sql.append("       TCC3.PRICE_DESC PRICE_NAME,                                                                  \n");
			sql.append(" 	   TVAT.TYPE_ID,                                                                                 \n");
			sql.append("       TVAT.TYPE_NAME,                                                                              \n");
			sql.append("       TVO.REFIT_REMARK,                                                                            \n");
			sql.append("       TVO.PAY_REMARK,                                                                              \n");
			sql.append("       TVDR.REQ_REMARK ORDER_REMARK,                                                                \n");
			sql.append("       TVDR.FUND_TYPE FUND_TYPE_ID,                                                                 \n");
			sql.append("       TVO.ORDER_PRICE,                                                                             \n");
			sql.append("       TVO.PRICE_ID,                                                                                \n");
			sql.append("        '' PRICE_DESC,                                                                              \n");
			sql.append("       TVDR.OTHER_PRICE_REASON,                                                                     \n");
			sql.append("       TVDR.RECEIVER,                                                                               \n");
			sql.append("       TVDR.LINK_MAN,                                                                               \n");
			sql.append("       TVDR.TEL,                                                                                    \n");
			sql.append("       TVDR.DISCOUNT,                                                                               \n");
			sql.append("       TVO.ORDER_ORG_ID,                                                                            \n");
			sql.append("       TVO.BILLING_ORG_ID,                                                                          \n");
			sql.append("       TVDR.WAREHOUSE_ID,                                                                           \n");
			sql.append("       TW.WAREHOUSE_NAME,                                                                           \n");
			sql.append("       TVO.IS_FLEET,                                                                                \n");
			sql.append("       TVDR.REQ_TOTAL_AMOUNT,                                                                       \n");
			sql.append("       TVDR.IS_REBATE,                                                                              \n");
			sql.append("       TVDR.REBATE_AMOUNT,                                                                          \n");
			sql.append("       TVO.IS_CUSTOM_ADDR,                                                                          \n");
			sql.append("       TR1.REGION_NAME PROVICE,                                                                     \n");
			sql.append("       TR2.REGION_NAME CITY,                                                                        \n");
			sql.append("       TR3.REGION_NAME TOWN,                                                                        \n");
			sql.append("       TVO.CUSTOM_ADDR,                                                                             \n");
			sql.append("       TVO.CUSTOM_LINK_MAN,                                                                         \n");
			sql.append("       TVO.CUSTOM_TEL,                                                                              \n");
			sql.append("       TOR.ORG_NAME,                                                                                \n");
			sql.append("       TO_CHAR(TVO.RAISE_DATE, 'YYYY-MM-DD') RAISE_DATE,                                            \n");
			sql.append("       NVL(SUM(TVOD.ORDER_AMOUNT), 0) ORDER_AMOUNT,                                                 \n");
			sql.append("       NVL(SUM(TVOD.CHECK_AMOUNT), 0) CHECK_AMOUNT,                                                 \n");
			sql.append("       NVL(SUM(TVDRD.RESERVE_AMOUNT), 0) RESERVE_AMOUNT,                                            \n");
			sql.append("       NVL(TVDR.TMP_LICENSE_AMOUNT, 0) TMP_LICENSE_AMOUNT                                           \n");
			sql.append("  FROM TT_VS_ORDER            TVO,                                                                  \n");
			sql.append("       TT_VS_ORDER_DETAIL     TVOD,                                                                 \n");
			sql.append("       TT_VS_DLVRY_REQ        TVDR,                                                                 \n");
			sql.append("       TT_VS_DLVRY_REQ_DTL    TVDRD,                                                                \n");
			sql.append("       TM_DEALER              TMD1,                                                                 \n");
			sql.append("       TM_DEALER              TMD2,                                                                 \n");
			sql.append("       TM_DEALER              TMD3,                                                                 \n");
			sql.append("       TM_DEALER              TMD5,                                                                 \n");
			sql.append("       TC_CODE                TCC1,                                                                 \n");
			sql.append("       TC_CODE                TCC2,                                                                 \n");
			sql.append("       TT_VS_PRICE                TCC3,                                                             \n");
			sql.append("       TT_VS_ACCOUNT_TYPE     TVAT,                                                                 \n");
			sql.append("       TM_FLEET               TMF,                                                                  \n");
			sql.append("       TM_VS_ADDRESS          TVA,                                                                  \n");
			sql.append("       TM_DEALER_ORG_RELATION TDOR,                                                                 \n");
			sql.append("       TM_REGION              TR1,                                                                  \n");
			sql.append("        TM_REGION              TR2,                                                                 \n");
			sql.append("        TM_REGION              TR3,                                                                 \n");
			sql.append("       TM_WAREHOUSE        TW,                                                                      \n");
			sql.append("       TM_ORG                 TOR                                                                   \n");
			sql.append(" WHERE TVO.ORDER_ID = TVOD.ORDER_ID                                                                 \n");
			sql.append("   AND TVO.ORDER_ID = TVDR.ORDER_ID                                                                 \n");
			sql.append("   AND TVDR.REQ_ID = TVDRD.REQ_ID                                                                   \n");
			sql.append("   AND TVOD.DETAIL_ID = TVDRD.ORDER_DETAIL_ID                                                       \n");
			sql.append("   AND TVO.ORDER_ORG_ID = TMD1.DEALER_ID                                                            \n");
			sql.append("   AND TVDR.ORDER_DEALER_ID = TMD5.DEALER_ID(+)                                                     \n");
			sql.append("   AND TVO.BILLING_ORG_ID = TMD2.DEALER_ID                                                          \n");
			sql.append("   AND TVDR.RECEIVER = TMD3.DEALER_ID(+)                                                            \n");
			sql.append("   AND TVO.ORDER_TYPE = TCC1.CODE_ID                                                                \n");
			sql.append("   AND TVO.PRICE_ID=TCC3.PRICE_ID                                                                   \n");
			sql.append("   AND TVDR.FUND_TYPE = TVAT.TYPE_ID                                                                \n");
			sql.append("   AND TVO.DELIVERY_TYPE = TCC2.CODE_ID                                                             \n");
			sql.append("   AND TVA.ID(+) = TVDR.ADDRESS_ID                                                                  \n");
			sql.append("   AND TMF.FLEET_ID(+) = TVDR.FLEET_ID                                                              \n");
			sql.append("   AND TVO.BILLING_ORG_ID = TDOR.DEALER_ID                                                          \n");
			sql.append("   AND TDOR.ORG_ID = TOR.ORG_ID                                                                     \n");
			sql.append("   AND TVO.PROVINCE_ID=TR1.REGION_CODE(+)                                                           \n");
			sql.append("    AND TVO.CITY_ID=TR2.REGION_CODE(+)                                                              \n");
			sql.append("     AND TVO.TOWN_ID=TR3.REGION_CODE(+)                                                             \n");
			sql.append("   AND TVDR.WAREHOUSE_ID = TW.WAREHOUSE_ID(+)                                                       \n");
			sql.append("   AND TVDR.REQ_ID = "+reqId+"                                                               \n");
			sql.append(" GROUP BY DECODE(TVDR.CALL_LEAVEL, 10851002,TMD5.DEALER_SHORTNAME, TMD1.DEALER_SHORTNAME),          \n");
			sql.append("       TMD1.DEALER_CODE,                                                                            \n");
			sql.append("          TMD2.DEALER_TYPE,                                                                         \n");
			sql.append("          TVDR.REQ_TOTAL_PRICE,                                                                     \n");
			sql.append("          TMD2.DEALER_SHORTNAME,                                                                    \n");
			sql.append("          TMD3.DEALER_SHORTNAME,                                                                    \n");
			sql.append("          TVO.PRODUCT_COMBO_ID,                                                                     \n");
			sql.append("          TVO.ORDER_NO,                                                                             \n");
			sql.append("          TVDR.VER,                                                                                 \n");
			sql.append("          TVO.ORDER_YEAR,                                                                           \n");
			sql.append("          TVO.ORDER_WEEK,                                                                           \n");
			sql.append("          TVO.ORDER_TYPE,                                                                           \n");
			sql.append("          TVDR.AREA_ID,                                                                             \n");
			sql.append("          TVO.DELIVERY_TYPE,                                                                        \n");
			sql.append("          TMF.FLEET_NAME,                                                                           \n");
			sql.append("          TVDR.FLEET_ADDRESS,                                                                       \n");
			sql.append("          TVO.DELIVERY_ADDRESS,                                                                     \n");
			sql.append("          TVA.ADDRESS,                                                                              \n");
			sql.append("          TVA.RECEIVE_ORG,                                                                          \n");
			sql.append("          TCC1.CODE_DESC,                                                                           \n");
			sql.append("          TCC2.CODE_DESC,                                                                           \n");
			sql.append("          TVAT.TYPE_NAME,                                                                           \n");
			sql.append("          TVO.REFIT_REMARK,                                                                         \n");
			sql.append("          TVO.PAY_REMARK,                                                                           \n");
			sql.append("          TVDR.REQ_REMARK,                                                                          \n");
			sql.append("          TVDR.FUND_TYPE,                                                                           \n");
			sql.append("          TVO.ORDER_PRICE,                                                                          \n");
			sql.append("          TVO.PRICE_ID,                                                                             \n");
			sql.append("      TVAT.TYPE_ID,                                                                                 \n");
			sql.append("          TVDR.OTHER_PRICE_REASON,                                                                  \n");
			sql.append("          TVDR.RECEIVER,                                                                            \n");
			sql.append("          TVDR.LINK_MAN,                                                                            \n");
			sql.append("          TVDR.TEL,                                                                                 \n");
			sql.append("          TVDR.DISCOUNT,                                                                            \n");
			sql.append("          TVO.ORDER_ORG_ID,                                                                         \n");
			sql.append("          TVO.BILLING_ORG_ID,                                                                       \n");
			sql.append("          TVDR.WAREHOUSE_ID,                                                                        \n");
			sql.append("           TW.WAREHOUSE_NAME,                                                                       \n");
			sql.append("          TVO.IS_FLEET,                                                                             \n");
			sql.append("          TVDR.REQ_TOTAL_AMOUNT,                                                                    \n");
			sql.append("          TOR.ORG_NAME,                                                                             \n");
			sql.append("          TVDR.TMP_LICENSE_AMOUNT,                                                                  \n");
			sql.append("           TVDR.IS_REBATE,                                                                          \n");
			sql.append("          TVDR.REBATE_AMOUNT,                                                                       \n");
			sql.append("          TVO.IS_CUSTOM_ADDR,                                                                       \n");
			sql.append("          TR1.REGION_NAME,                                                                          \n");
			sql.append("          TR2.REGION_NAME,                                                                          \n");
			sql.append("          TR3.REGION_NAME,                                                                          \n");
			sql.append("          TVO.CUSTOM_ADDR,                                                                          \n");
			sql.append("          TVO.CUSTOM_LINK_MAN,                                                                      \n");
			sql.append("          TVO.CUSTOM_TEL,                                                                           \n");
			sql.append("          TCC3.PRICE_DESC,                                                                          \n");
			sql.append("          TVO.RAISE_DATE                                                                           \n");
		List<Map<String, Object>> list = dao.pageQuery(sql.toString(), null, getFunName());
		Map<String, Object> map = new HashMap<String, Object>();
		if (list.size() > 0) {
			map = list.get(0);
		}
		return map;
	}
	public List<Map<String, Object>> getorderResourceReserveDetailList(String warehouse_id,String reqId, String orderType, String companyId) throws Exception {
		StringBuffer sql = new StringBuffer();

		/*sql.append("SELECT TVDRD.DETAIL_ID,\n");
		sql.append("       TVDRD.MATERIAL_ID,\n");
		sql.append("       TVM.MATERIAL_CODE,\n");
		sql.append("       TVM.MATERIAL_NAME,\n");
		sql.append("       decode(TVDRD.PATCH_NO,'null','',TVDRD.PATCH_NO) SPECIAL_BATCH_NO,\n");
		sql.append("       NVL(TVDRD.REQ_AMOUNT, 0) REQ_AMOUNT,\n");
		sql.append("       NVL(TVDRD.DELIVERY_AMOUNT, 0) DELIVERY_AMOUNT,\n");
		sql.append("       TVDRD.ORDER_DETAIL_ID,\n");
		sql.append("       NVL(TVDRD.RESERVE_AMOUNT, 0) RESERVE_AMOUNT,\n");
		sql.append("       NVL(TVDRD.SINGLE_PRICE, 0) SINGLE_PRICE,\n");
		sql.append("       NVL(TVDRD.TOTAL_PRICE, 0) + NVL(TVDRD.DISCOUNT_PRICE, 0) TOTAL_PRICE,\n");
		sql.append("       NVL(TVDRD.DISCOUNT_RATE, 0) DISCOUNT_RATE,\n");
		sql.append("       NVL(TVDRD.DISCOUNT_S_PRICE, 0) DISCOUNT_S_PRICE,\n");
		sql.append("       NVL(TVDRD.DISCOUNT_PRICE, 0) DISCOUNT_PRICE,\n");
		sql.append("       TVDRD.VER,\n");
		sql.append("       NVL(VVR.STOCK_AMOUNT,0)-NVL(VVR.LOCK_AMOUT,0) AVA_STOCK,\n"); //可用资源数
		sql.append("       NVL(VVR.GENERAL_AMOUNT,0)-NVL(VVR.satisfy_general_order,0) GENERAL_AMOUNT,\n"); //未满足常规订单
		sql.append("       F_GETBATCH_NO(TVDRD.DETAIL_ID) BATCH_NO,\n");
		sql.append("       NVL(VVR.STOCK_AMOUNT,0)-NVL(VVR.LOCK_AMOUT,0) WARHOUSE_STOCK,\n");
		sql.append("       TVDR.DLVRY_REQ_NO DLVRY_REQ_NO\n");
		sql.append("  FROM TT_VS_ORDER_DETAIL TVOD,\n");
		sql.append("       TT_VS_DLVRY_REQ TVDR,\n");
		sql.append("       TM_VHCL_MATERIAL TVM,\n");
		sql.append("       TT_VS_DLVRY_REQ_DTL TVDRD\n");
		sql.append("       LEFT JOIN  VW_VS_RESOURCE_ENTITY_WEEK_NEW VVR ON TVDRD.MATERIAL_ID = VVR.MATERIAL_ID AND VVR.COMPANY_ID =" + companyId+"\n");
		sql.append(" WHERE TVDR.REQ_ID = TVDRD.REQ_ID\n");
		sql.append("   AND TVOD.DETAIL_ID = TVDRD.ORDER_DETAIL_ID\n");
		sql.append("   AND TVDRD.MATERIAL_ID = TVM.MATERIAL_ID\n");
		sql.append("   AND TVDRD.REQ_ID = " + reqId + "");*/
		sql.append("SELECT *                                                                            \n");
		sql.append("  FROM (SELECT TVDRD.DETAIL_ID,                                                     \n");
		sql.append("               TVDR.Dealer_Id,                                                      \n");
		sql.append("               VMI.series_id,                                                       \n");
		sql.append("               TVDRD.MATERIAL_ID,                                                   \n");
		sql.append("               TVM.MATERIAL_CODE,                                                   \n");
		sql.append("               TVM.MATERIAL_NAME,                                                   \n");
		sql.append("               TVDR.WAREHOUSE_ID,                                                   \n");
		sql.append("               decode(TVDRD.PATCH_NO, 'null', '', TVDRD.PATCH_NO) SPECIAL_BATCH_NO, \n");
		sql.append("               NVL(TVDRD.REQ_AMOUNT, 0) REQ_AMOUNT,                                 \n");
		sql.append("               NVL(TVDRD.DELIVERY_AMOUNT, 0) DELIVERY_AMOUNT,                       \n");
		sql.append("               TVDRD.ORDER_DETAIL_ID,                                               \n");
		sql.append("               NVL(TVDRD.RESERVE_AMOUNT, 0) RESERVE_AMOUNT,                         \n");
		sql.append("               NVL(TVDRD.SINGLE_PRICE, 0) SINGLE_PRICE,                             \n");
		sql.append("               NVL(TVDRD.TOTAL_PRICE, 0) + NVL(TVDRD.DISCOUNT_PRICE, 0) TOTAL_PRICE,\n");
		sql.append("               NVL(TVDRD.DISCOUNT_RATE, 0) DISCOUNT_RATE,                           \n");
		sql.append("               NVL(TVDRD.DISCOUNT_S_PRICE, 0) DISCOUNT_S_PRICE,                     \n");
		sql.append("               NVL(TVDRD.DISCOUNT_PRICE, 0) DISCOUNT_PRICE,                         \n");
		sql.append("               TVDRD.VER,                                                           \n");
		sql.append("               NVL(VVR.STOCK_AMOUNT, 0) - NVL(VVR.LOCK_AMOUT, 0) AVA_STOCK,         \n");
		sql.append("               NVL(VVR.GENERAL_AMOUNT, 0) -                                         \n");
		sql.append("               NVL(VVR.satisfy_general_order, 0) GENERAL_AMOUNT,                    \n");
		sql.append("               NVL(VVR.STOCK_AMOUNT, 0) - NVL(VVR.LOCK_AMOUT, 0) WARHOUSE_STOCK,    \n");
		sql.append("               TVDR.DLVRY_REQ_NO DLVRY_REQ_NO                                       \n");
		sql.append("          FROM TT_VS_ORDER_DETAIL  TVOD,                                            \n");
		sql.append("               TT_VS_DLVRY_REQ     TVDR,                                            \n");
		sql.append("               TM_VHCL_MATERIAL    TVM,                                             \n");
		sql.append("               VW_MATERIAL_INFO    VMI,                                             \n");
		sql.append("               TT_VS_DLVRY_REQ_DTL TVDRD                                            \n");
		sql.append("          LEFT JOIN (SELECT *                                                       \n");
		sql.append("                      FROM VW_VS_RESOURCE_ENTITY_WEEK_NEW                           \n");
		sql.append("                     WHERE WAREHOUSE_ID =                                           \n");
		sql.append("                           (SELECT WAREHOUSE_ID                                     \n");
		sql.append("                              FROM TT_VS_DLVRY_REQ                                  \n");
		sql.append("                             WHERE REQ_ID = "+reqId+")) VVR                         \n");
		sql.append("            ON TVDRD.MATERIAL_ID = VVR.MATERIAL_ID                                  \n");
		sql.append("           AND VVR.COMPANY_ID = "+companyId+"                                       \n");
		sql.append("         WHERE TVDR.REQ_ID = TVDRD.REQ_ID                                           \n");
		sql.append("           AND TVOD.DETAIL_ID = TVDRD.ORDER_DETAIL_ID                               \n");
		sql.append("           AND TVDRD.MATERIAL_ID = TVM.MATERIAL_ID                                  \n");
		sql.append("           AND TVDRD.REQ_ID = "+reqId+"                                             \n");
		sql.append("           AND TVDRD.MATERIAL_ID = VMI.material_id) INFO                            \n");
		sql.append("  LEFT JOIN VW_VS_PLAN_REQ_AMOUNT VPR                                               \n");
		sql.append("    ON INFO.DEALER_ID = VPR.DEALER_ID                                               \n");
		sql.append("   AND INFO.SERIES_ID = VPR.SERIES_ID                                               \n");
		List<Map<String, Object>> list = dao.pageQuery(sql.toString(), null, getFunName());
		return list;
	}
	
	public List<Map<String, Object>> getReqCheckList(String reqId) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT TO_CHAR(TVOC.CHECK_DATE, 'YYYY-MM-DD') CHECK_DATE,\n");
		sql.append("       TMO.ORG_NAME,\n");
		sql.append("       TCU.NAME USER_NAME,\n");
		sql.append("       TCC.CODE_DESC CHECK_STATUS,\n");
		sql.append("       TVOC.CHECK_DESC\n");
		sql.append("  FROM TT_VS_REQ_CHECK TVOC, TM_ORG TMO, TC_USER TCU, TC_CODE TCC\n");
		sql.append(" WHERE TVOC.CHECK_ORG_ID = TMO.ORG_ID\n");
		sql.append("   AND TVOC.CHECK_USER_ID = TCU.USER_ID\n");
		sql.append("   AND TVOC.CHECK_STATUS = TCC.CODE_ID\n");
		if (reqId != null && !"".equals(reqId)) {
			sql.append("   AND TVOC.REQ_ID = " + reqId + "\n");
		}
		sql.append(" ORDER BY TVOC.CHECK_DATE,TCC.CODE_ID ASC");

		List<Map<String, Object>> list = dao.pageQuery(sql.toString(), null, getFunName());
		return list;
	}
	
	public TmBusinessParaPO getTmBusinessParaPO(Integer paraId, Long companyId) {
		TmBusinessParaPO po = new TmBusinessParaPO();
		po.setParaId(paraId);
		List<PO> list = select(po);
		return list.size() != 0 ? (TmBusinessParaPO) list.get(0) : null;
	}

}
