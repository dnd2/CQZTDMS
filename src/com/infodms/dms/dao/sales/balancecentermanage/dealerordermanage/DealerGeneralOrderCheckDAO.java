package com.infodms.dms.dao.sales.balancecentermanage.dealerordermanage;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;
import com.infoservice.po3.core.impl.Common;

public class DealerGeneralOrderCheckDAO extends BaseDao{
	private static final DealerGeneralOrderCheckDAO dao = new DealerGeneralOrderCheckDAO();

	public static final DealerGeneralOrderCheckDAO getInstance() {
		return dao;
	}
	
	public static PageResult<Map<String, Object>> getDealerOrderCheckInit_Query(Map<String, Object> map, int pageSize, int curPage) {
		String areaId = map.get("areaId").toString() ;
		String dealerCode = map.get("dealerCode").toString() ;
		String dealerId = map.get("dealerId").toString() ;
		String orderNo = map.get("orderNo").toString() ;
		String orderYear1 = map.get("orderYear1").toString() ;
		String orderWeek1 = map.get("orderWeek1").toString() ;
		String orderMonth1 = map.get("orderMonth1").toString() ;
		String orderYear2 = map.get("orderYear2").toString() ;
		String orderWeek2 = map.get("orderWeek2").toString() ;
		String orderMonth2 = map.get("orderMonth2").toString() ;
		String groupCode = map.get("groupCode").toString() ;
		
		List<Object> param = new ArrayList<Object>() ;
		
		StringBuffer sql = new StringBuffer("\n") ;
		
		sql.append("SELECT DISTINCT TVDR.REQ_ID,\n");
		sql.append("       TMD.DEALER_SHORTNAME ODLRID,\n");  
		sql.append("       TMD1.DEALER_SHORTNAME BDLRID,\n");  
		sql.append("       TVO.ORDER_NO,\n");  
		sql.append("       TO_CHAR(TVDR.CREATE_DATE, 'YYYY-MM-DD') CALLDATE,\n");  
		sql.append("       TO_CHAR(TVO.ORDER_YEAR) || '年' || TO_CHAR(TVO.ORDER_MONTH) || '月' ORDERDATE,\n");  
		sql.append("       TO_CHAR(TVO.ORDER_YEAR) || '年' || TO_CHAR(TVO.ORDER_WEEK) || '周' ORDER_WEEK,\n");  
		sql.append("       TVO.ORDER_TYPE,\n");  
		sql.append("       TVDR.REQ_STATUS,\n");  
		sql.append("       TVDR.REQ_TOTAL_AMOUNT,\n");  
		sql.append("       TVDR.AREA_ID\n");
		sql.append("  FROM TT_VS_ORDER TVO, TT_VS_DLVRY_REQ TVDR, TM_DEALER TMD, TM_DEALER TMD1, TT_VS_DLVRY_REQ_DTL TVDRD\n");  
		sql.append(" WHERE TVO.ORDER_ID = TVDR.ORDER_ID\n");  
		sql.append("   AND TVDR.REQ_ID = TVDRD.REQ_ID\n");  
		sql.append("   AND TVDR.ORDER_DEALER_ID = TMD.DEALER_ID\n");  
		sql.append("   AND TVDR.BIL_DEALER_ID = TMD1.DEALER_ID\n");  
		sql.append("   AND TVO.ORDER_TYPE = ?\n");  
		param.add(Constant.ORDER_TYPE_01) ;
		
		sql.append("   AND TVDR.REQ_STATUS = ?\n");
		param.add(Constant.ORDER_REQ_STATUS_YSH) ;
		
		sql.append("   AND TVO.ORDER_STATUS = ?\n");
		param.add(Constant.ORDER_STATUS_05) ;
		
		if(!CommonUtils.isNullString(orderMonth1) && !CommonUtils.isNullString(orderMonth2)) {
			sql.append("   AND TO_NUMBER(TVO.ORDER_YEAR || '00') + TO_NUMBER(TVO.ORDER_MONTH) >= ? AND TO_NUMBER(TVO.ORDER_YEAR || '00') + TO_NUMBER(TVO.ORDER_MONTH) <= ?\n");
			param.add(Integer.parseInt(orderYear1 + "00") + Integer.parseInt(orderMonth1)) ;
			param.add(Integer.parseInt(orderYear2 + "00") + Integer.parseInt(orderMonth2)) ;
		}
		
		if(!CommonUtils.isNullString(orderWeek1) && !CommonUtils.isNullString(orderWeek2)) {
			sql.append("   AND TO_NUMBER(TVO.ORDER_YEAR || '00') + TO_NUMBER(TVO.ORDER_WEEK) >= ? AND TO_NUMBER(TVO.ORDER_YEAR || '00') + TO_NUMBER(TVO.ORDER_WEEK) <= ?\n");
			param.add(Integer.parseInt(orderYear1 + "00") + Integer.parseInt(orderWeek1)) ;
			param.add(Integer.parseInt(orderYear2 + "00") + Integer.parseInt(orderWeek2)) ;
		}
			
		
		sql.append("   AND TVDR.BIL_DEALER_ID IN (").append(dealerId).append(")\n"); 
		
		if(!CommonUtils.isNullString(areaId)) {
			sql.append("   AND TVDR.AREA_ID = ?\n");
			param.add(areaId) ;
		}
		
		if(!CommonUtils.isNullString(dealerCode)) {
			dealerCode = "'" + dealerCode.replaceAll(",", "','") + "'";
			
			sql.append("   AND TMD.DEALER_CODE IN (").append(dealerCode).append(")\n");
		}
		
		if(!CommonUtils.isNullString(orderNo)) {
			sql.append("   AND TVO.ORDER_NO LIKE '%").append(orderNo).append("%'\n");
		}
		
		if(!CommonUtils.isNullString(groupCode)) {
			groupCode = "'" + groupCode.replaceAll(",", "','") + "'";
			
			sql.append("   AND TVDRD.MATERIAL_ID IN (select tvmgr.material_id from tm_vhcl_material_group_r tvmgr, tm_vhcl_material_group tvmg where tvmg.group_id = tvmgr.group_id and tvmg.group_code in (").append(groupCode).append("))\n");
		}
		
		return dao.pageQuery(sql.toString(), param, dao.getFunName(), pageSize, curPage);
		/*sql.append("SELECT DISTINCT TVDR.REQ_ID,\n");
		sql.append("       TMD.DEALER_SHORTNAME ODLRID,\n");  
		sql.append("       TMD1.DEALER_SHORTNAME BDLRID,\n");  
		sql.append("       TVO.ORDER_NO,\n");  
		sql.append("       TO_CHAR(TVDR.CREATE_DATE, 'YYYY-MM-DD') CALLDATE,\n");  
		sql.append("       TO_CHAR(TVO.ORDER_YEAR) || '年' || TO_CHAR(TVO.ORDER_MONTH) || '月' ORDERDATE,\n");  
		sql.append("       TO_CHAR(TVO.ORDER_YEAR) || '年' || TO_CHAR(TVO.ORDER_WEEK) || '周' ORDER_WEEK,\n");  
		sql.append("       TVO.ORDER_TYPE,\n");  
		sql.append("       TVDR.REQ_STATUS,\n");  
		sql.append("       TVDR.REQ_TOTAL_AMOUNT,\n");  
		sql.append("       TVDR.AREA_ID\n");
		sql.append("  FROM TT_VS_ORDER TVO, TT_VS_DLVRY_REQ TVDR, TM_DEALER TMD, TM_DEALER TMD1, TT_VS_DLVRY_REQ_DTL TVDRD\n");  
		sql.append(" WHERE TVO.ORDER_ID = TVDR.ORDER_ID\n");  
		sql.append("   AND TVDR.REQ_ID = TVDRD.REQ_ID\n");  
		sql.append("   AND TVDR.ORDER_DEALER_ID = TMD.DEALER_ID\n");  
		sql.append("   AND TVDR.BIL_DEALER_ID = TMD1.DEALER_ID\n");  
		sql.append("   AND TVO.ORDER_TYPE = "+Constant.ORDER_TYPE_01+"\n");  
		//param.add(Constant.ORDER_TYPE_01) ;
		
		sql.append("   AND TVDR.REQ_STATUS = "+Constant.ORDER_REQ_STATUS_YSH+"\n");
		//param.add(Constant.ORDER_REQ_STATUS_YSH) ;
		
		sql.append("   AND TVO.ORDER_STATUS = "+Constant.ORDER_STATUS_05+"\n");
		//param.add(Constant.ORDER_STATUS_05) ;
		
		if(!CommonUtils.isNullString(orderMonth1) && !CommonUtils.isNullString(orderMonth2)) {
			sql.append("   AND TO_NUMBER(TVO.ORDER_YEAR || '00') + TO_NUMBER(TVO.ORDER_MONTH) >= "+Integer.parseInt(orderYear1 + "00") + Integer.parseInt(orderMonth1)+" AND TO_NUMBER(TVO.ORDER_YEAR || '00') + TO_NUMBER(TVO.ORDER_MONTH) <= "+Integer.parseInt(orderYear2 + "00") + Integer.parseInt(orderMonth2)+"\n");
			//param.add(Integer.parseInt(orderYear1 + "00") + Integer.parseInt(orderMonth1)) ;
			//param.add(Integer.parseInt(orderYear2 + "00") + Integer.parseInt(orderMonth2)) ;
		}
		
		if(!CommonUtils.isNullString(orderWeek1) && !CommonUtils.isNullString(orderWeek2)) {
			//sql.append("   AND TO_NUMBER(TVO.ORDER_YEAR || '00') + TO_NUMBER(TVO.ORDER_WEEK) >= ? AND TO_NUMBER(TVO.ORDER_YEAR || '00') + TO_NUMBER(TVO.ORDER_WEEK) <= ?\n");
			sql.append("AND TO_NUMBER(to_char(TVO.ORDER_YEAR) || '00') + TVO.ORDER_WEEK = "+Integer.parseInt(orderYear1 + "00") + Integer.parseInt(orderWeek1)+" AND TO_NUMBER(to_char(TVO.ORDER_YEAR) || '00') + TVO.ORDER_WEEK <= "+Integer.parseInt(orderYear2 + "00") + Integer.parseInt(orderWeek2)+"\n");
			//param.add(Integer.parseInt(orderYear1 + "00") + Integer.parseInt(orderWeek1)) ;
			//param.add(Integer.parseInt(orderYear2 + "00") + Integer.parseInt(orderWeek2)) ;
		}
			
		
		sql.append("   AND TVDR.BIL_DEALER_ID IN ("+dealerId+")\n"); 
		//param.add(dealerId) ;
		
		if(!CommonUtils.isNullString(areaId)) {
			sql.append("   AND TVDR.AREA_ID = "+areaId+"\n");
			//param.add(areaId) ;
		}
		
		if(!CommonUtils.isNullString(dealerCode)) {
			sql.append("   AND TMD.DEALER_CODE = "+dealerCode+"\n");
			//param.add(dealerCode) ;
		}
		
		if(!CommonUtils.isNullString(orderNo)) {
			sql.append("   AND TVO.ORDER_NO = "+orderNo+"\n");
			//param.add(orderNo) ;
		}
		
		if(!CommonUtils.isNullString(groupCode)) {
			sql.append("   AND TVDRD.MATERIAL_ID IN (select tvmgr.material_id from tm_vhcl_material_group_r tvmgr, tm_vhcl_material_group tvmg where tvmg.group_id = tvmgr.group_id and tvmg.group_code in ("+groupCode+"))\n");
			groupCode = "'" + groupCode.replaceAll(",", "','") + "'";
			//param.add(groupCode) ;
		}
		
		return dao.pageQuery(sql.toString(), null, dao.getFunName(), pageSize, curPage);*/
	}
	
	public static Map<String, Object> orderInfoQuery(String reqId) {
		List<Object> param = new ArrayList<Object>() ;
		
		StringBuffer sql = new StringBuffer("\n") ;
		
		sql.append("SELECT TVO.ORDER_ID,\n");
		sql.append("       TVO.ORDER_NO, --订单号\n");  
		sql.append("       TBA.AREA_NAME, --业务范围\n");  
		sql.append("       TVO.ORDER_YEAR || '年' || TVO.ORDER_MONTH || '月' ORDER_MONTH, --微车常规订单订单月度\n");  
		sql.append("       TVO.ORDER_YEAR || '年' || NVL(TVO.ORDER_WEEK, 0) || '周' ORDER_WEEK, --轿车常规订单订单周度\n");  
		sql.append("       TVO.ORDER_TYPE, --订单类型\n");  
		sql.append("       SUM(NVL(TVOD.ORDER_AMOUNT, 0)) ORDER_AMOUNT, --常规订单提报数量\n");  
		sql.append("       SUM(NVL(TVOD.CHECK_AMOUNT, 0)) CHECK_AMOUNT, --常规订单审核通过数量\n");  
		sql.append("       SUM(NVL(TVOD.CALL_AMOUNT, 0)) CALL_AMOUNT, --常规订单申请数量\n");  
		sql.append("       SUM(NVL(TVOD.DELIVERY_AMOUNT, 0)) DELIVERY_AMOUNT --常规订单发运数量\n");  
		sql.append("  FROM TT_VS_DLVRY_REQ    TVDR,\n");  
		sql.append("       TT_VS_ORDER        TVO,\n");  
		sql.append("       TT_VS_ORDER_DETAIL TVOD,\n");  
		sql.append("       TM_BUSINESS_AREA   TBA\n");  
		sql.append(" WHERE TVDR.ORDER_ID = TVO.ORDER_ID\n");  
		sql.append("   AND TVO.ORDER_ID = TVOD.ORDER_ID\n");  
		sql.append("   AND TBA.AREA_ID = TVO.AREA_ID\n");  
		sql.append("   AND TVO.ORDER_STATUS = ?\n");  
		param.add(Constant.ORDER_STATUS_05) ;
		
		sql.append("   AND TVO.ORDER_TYPE = ?\n");  
		param.add(Constant.ORDER_TYPE_01) ;
		
		sql.append("   AND TVDR.REQ_ID = ?\n");  
		param.add(reqId) ;
		
		sql.append(" GROUP BY TVO.ORDER_ID,\n");  
		sql.append("          TVO.ORDER_NO,\n");  
		sql.append("          TBA.AREA_NAME,\n");  
		sql.append("          TVO.ORDER_YEAR || '年' || TVO.ORDER_MONTH || '月',\n");  
		sql.append("          TVO.ORDER_YEAR || '年' || NVL(TVO.ORDER_WEEK, 0) || '周',\n");  
		sql.append("          TVO.ORDER_TYPE\n");

		Map<String, Object> orderMap = dao.pageQueryMap(sql.toString(), param, dao.getFunName()) ;
		
		return orderMap ;
	}
	
	public static Map<String, Object> reqInfoQuery(String reqId) {
		List<Object> param = new ArrayList<Object>() ;
		
		StringBuffer sql = new StringBuffer("\n") ;
		
		sql.append("SELECT TVDR.REQ_ID,\n");
		sql.append("       TVDR.IS_FLEET, --是否集团客户\n");  
		sql.append("       TVDR.FLEET_ID, --集团客户id\n");  
		sql.append("       TVDR.FLEET_ADDRESS, --集团客户地址\n");  
		sql.append("       TVDR.DELIVERY_TYPE, --发运方式\n");  
		sql.append("       TVDR.LINK_MAN, --联系人\n");  
		sql.append("       TVDR.TEL, --联系电话\n");  
		sql.append("       TVDR.ADDRESS_ID, --地址id\n");  
		sql.append("       TVA.ADDRESS, --地址\n");  
		sql.append("       TVDR.REQ_REMARK, --备注信息\n");  
		sql.append("       TD1.DEALER_SHORTNAME C_NAME, --采购方简称\n");  
		sql.append("       TVDR.DLVRY_REQ_NO, --发运申请单号\n"); 
		sql.append("       TVDR.AREA_ID, --业务范围id\n"); 
		sql.append("       TBA.AREA_NAME, --业务范围\n");  
		sql.append("       TVDR.FUND_TYPE, --资金类型\n"); 
		sql.append("       TVDR.BIL_DEALER_ID K_ID, --开票方id\n"); 
		sql.append("       TD2.DEALER_SHORTNAME K_NAME, --开票方简称\n");  
		sql.append("       TD3.DEALER_SHORTNAME S_NAME, --收货方简称\n");  
		sql.append("       TO_CHAR(TVDR.REQ_DATE, 'YYYY-MM-DD') REQ_DATE, --申请日期\n");  
		sql.append("       TVDR.REQ_TOTAL_AMOUNT --申请数量\n");  
		sql.append("  FROM TT_VS_DLVRY_REQ  TVDR,\n");  
		sql.append("       TM_BUSINESS_AREA TBA,\n");  
		sql.append("       TM_VS_ADDRESS    TVA,\n");  
		sql.append("       TM_DEALER        TD1,\n");  
		sql.append("       TM_DEALER        TD2,\n");  
		sql.append("       TM_DEALER        TD3\n");  
		sql.append(" WHERE TVDR.AREA_ID = TBA.AREA_ID\n");  
		sql.append("   AND TVDR.ADDRESS_ID = TVA.ID(+)\n");  
		sql.append("   AND TVDR.ORDER_DEALER_ID = TD1.DEALER_ID\n");  
		sql.append("   AND TVDR.BIL_DEALER_ID = TD2.DEALER_ID\n");  
		sql.append("   AND TVDR.RECEIVER = TD3.DEALER_ID(+)\n");  
		sql.append("   AND TVDR.REQ_ID = ?\n");
		
		param.add(reqId) ;

		Map<String, Object> reqMap = dao.pageQueryMap(sql.toString(), param, dao.getFunName()) ;
		
		return reqMap ;
	}
	
	public static List<Map<String, Object>> reqDtlInfoQuery(String reqId) {
		List<Object> param = new ArrayList<Object>() ;
		
		StringBuffer sql = new StringBuffer("\n") ;
		
		sql.append("SELECT TVDRD.DETAIL_ID   REQ_DTL, --发运申请明细id\n");
		sql.append("       TVOD.DETAIL_ID    ORDER_DTL, --订单明细id\n");  
		sql.append("       VMG.SERIES_NAME, --车系名称\n");  
		sql.append("       TVDRD.MATERIAL_ID, --物料id\n");
		sql.append("       TVM.MATERIAL_CODE, --物料代码\n");  
		sql.append("       TVM.MATERIAL_NAME, --物料名称\n");  
		sql.append("       NVL(TVOD.CHECK_AMOUNT, 0) CHECK_AMOUNT, --订单审核数量\n");  
		sql.append("       NVL(TVOD.CALL_AMOUNT, 0) CALL_AMOUNT, --订单已申请数量\n");  
		sql.append("       NVL(TVDRD.REQ_AMOUNT, 0) REQ_AMOUNT --发运申请数量\n");  
		sql.append("  FROM TT_VS_DLVRY_REQ_DTL      TVDRD,\n");  
		sql.append("       TT_VS_ORDER_DETAIL       TVOD,\n");  
		sql.append("       TM_VHCL_MATERIAL         TVM,\n");  
		sql.append("       TM_VHCL_MATERIAL_GROUP_R TVMGR,\n");  
		sql.append("       VW_MATERIAL_GROUP        VMG\n");  
		sql.append(" WHERE TVDRD.ORDER_DETAIL_ID = TVOD.DETAIL_ID\n");  
		sql.append("   AND TVDRD.MATERIAL_ID = TVM.MATERIAL_ID\n");  
		sql.append("   AND TVDRD.MATERIAL_ID = TVMGR.MATERIAL_ID\n");  
		sql.append("   AND TVMGR.GROUP_ID = VMG.PACKAGE_ID\n");  
		sql.append("   AND TVDRD.REQ_ID = ?\n");
		
		param.add(reqId) ;
		
		List<Map<String, Object>> reqDtlList = dao.pageQuery(sql.toString(), param, dao.getFunName()) ;

		return reqDtlList ;
	}
	
	public static Map<String,String> getFleetInfo(String reqId){
		List<Object> param = new ArrayList<Object>() ;
		
		StringBuffer sql = new StringBuffer("\n");
		
		sql.append("SELECT TMF.FLEET_ID, TMF.FLEET_NAME, NVL(TMF.ADDRESS, '--') ADDRESS\n");
		sql.append("  FROM TT_VS_DLVRY_REQ TVDR, TM_FLEET TMF\n");  
		sql.append(" WHERE TVDR.FLEET_ID = TMF.FLEET_ID\n");  
		sql.append("   AND TVDR.REQ_ID = ?\n");
		
		param.add(reqId) ;

		return dao.pageQueryMap(sql.toString(), param, dao.getFunName());
	}
	
	public static List<Map<String, Object>> reqChngQuery(String reqId) {
		List<Object> param = new ArrayList<Object>() ;
		
		StringBuffer sql = new StringBuffer("\n") ;
		
		sql.append("SELECT TCL.LOG_ID,\n");
		sql.append("       TCL.REQ_ID,\n");  
		sql.append("       TCL.CHNG_TYPE,\n");  
		sql.append("       TO_CHAR(TCL.CHNG_DATE, 'YYYY-MM-DD') CHNG_DATE,\n");  
		sql.append("       TCU.NAME\n");  
		sql.append("  FROM TT_VS_REQ_CHNG_LOG TCL, TC_USER TCU\n");  
		sql.append(" WHERE TCL.USER_ID = TCU.USER_ID\n");  
		sql.append("   AND TCL.REQ_ID = ?\n");
		param.add(reqId) ;
		
		return dao.pageQuery(sql.toString(), param, dao.getFunName()) ;
	}
	
	public static List<Map<String, Object>> reqChkQuery(String reqId) {
		List<Object> param = new ArrayList<Object>() ;
		
		StringBuffer sql = new StringBuffer("\n") ;
		
		sql.append("SELECT TVRC.CHECK_ID,\n");
		sql.append("       TVRC.REQ_ID,\n");  
		sql.append("       TVRC.CHECK_ORG_ID,\n");  
		sql.append("       TCP.POSE_NAME,\n");  
		sql.append("       TCU.NAME,\n");  
		sql.append("       TO_CHAR(TVRC.CHECK_DATE, 'YYYY-MM-DD') CHECK_DATE,\n");  
		sql.append("       TVRC.CHECK_STATUS,\n");  
		sql.append("       TVRC.CHECK_DESC\n");  
		sql.append("  FROM TT_VS_REQ_CHECK TVRC, TC_USER TCU, TC_POSE TCP\n");  
		sql.append(" WHERE TVRC.CHECK_USER_ID = TCU.USER_ID\n");  
		sql.append("   AND TVRC.CHECK_POSITION_ID = TCP.POSE_ID\n");  
		sql.append("   AND TVRC.REQ_ID = ?\n");
		param.add(reqId) ;
		
		return dao.pageQuery(sql.toString(), param, dao.getFunName()) ;
	}

	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}

}
