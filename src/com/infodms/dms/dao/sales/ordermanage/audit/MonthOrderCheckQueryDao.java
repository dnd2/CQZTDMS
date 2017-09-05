package com.infodms.dms.dao.sales.ordermanage.audit;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.dao.sales.storageManage.CheckVehicleDAO;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

public class MonthOrderCheckQueryDao extends BaseDao{
	public static Logger logger = Logger.getLogger(CheckVehicleDAO.class);
	private static final MonthOrderCheckQueryDao dao = new MonthOrderCheckQueryDao ();
	public static final MonthOrderCheckQueryDao getInstance() {
		return dao;
	}
	/**
	 * 月度订单审核查询展示(事业部)详细
	 * */
	public static PageResult <Map<String,Object>> monthOrderCheck_BUS_QueryList(Long orgId,String startYear,String startMonth,String endYear,String endMonth,String areaId,String groupCode,String dealerCode,String orderNO,String orderStatus, int pageSize,int curPage){
		StringBuffer sql = new StringBuffer("");
		sql.append("SELECT TMD.DEALER_CODE,\n");
		sql.append("       TMD.DEALER_SHORTNAME DEALER_NAME,\n");  
		sql.append("       TMD1.DEALER_code DEALER_code1,\n");  
		sql.append("       TMD1.DEALER_SHORTNAME DEALER_NAME1,\n");  
		sql.append("       TTO.ORDER_NO,\n");  
		sql.append("       TO_CHAR(TTO.RAISE_DATE, 'yyyy-MM-dd') RAISE_DATE,\n");  
		sql.append("       TTO.ORDER_STATUS,tvm.MATERIAL_CODE,\n");  
		sql.append("       SUM(TTOD.ORDER_AMOUNT) AS ORDER_AMOUNT,\n");  
		sql.append("       SUM(TTOD.CALL_AMOUNT) AS CALL_AMOUNT\n");
		sql.append("  FROM TT_VS_ORDER TTO,\n");  
		sql.append("       TM_DEALER TMD,\n");  
		sql.append("       TM_DEALER TMD1,\n");  
		sql.append("       TT_VS_ORDER_DETAIL TTOD,\n");  
		sql.append("       (select vod.dealer_id from vw_org_dealer vod where vod.root_org_id = "+orgId+") XX,\n");  
		sql.append("         TM_VHCL_MATERIAL TVM\n");
		sql.append(" WHERE TTO.ORDER_ORG_ID = TMD.DEALER_ID\n");  
		sql.append("   AND TTO.billing_org_id = TMD1.DEALER_ID\n");  
		sql.append("   AND TTO.ORDER_ID = TTOD.ORDER_ID\n");  
		sql.append("   AND TMD.DEALER_ID = XX.DEALER_ID\n");  
		sql.append("   AND TTOD.MATERIAL_ID = TVM.MATERIAL_ID\n");
		sql.append("   AND TTO.ORDER_STATUS IN ("+Constant.ORDER_STATUS_04+", "+Constant.ORDER_STATUS_08+","+Constant.ORDER_STATUS_09+" , "+Constant.ORDER_STATUS_05+")\n");  
		sql.append("   AND TTO.ORDER_TYPE = "+Constant.ORDER_TYPE_01+"\n");  
		if (null != areaId && !"".equals(areaId)) {
			sql.append("   AND TTO.AREA_ID ="+areaId+"\n");  
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
		if (null != groupCode && !"".equals(groupCode)) {
			String[] array = groupCode.split(",");
			sql.append("   AND TVM.MATERIAL_CODE IN (\n");
				for (int i = 0; i < array.length; i++) {
					sql.append("'"+ array[i]+"'");
					if (i != array.length - 1) {
						sql.append(",");
					}
				}
			sql.append(")\n");
		}
		
		if (null != orderNO && !"".equals(orderNO)) {
			sql.append("   AND TTO.ORDER_NO LIKE '%"+orderNO.trim()+"%'\n");  
		}
		if (!"".equals(orderStatus)) {
			sql.append("   AND TTO.ORDER_STATUS = "+orderStatus+"\n");
		}
		sql.append("   AND TTO.ORDER_YEAR >= "+startYear+"\n");  
		sql.append("   AND TTO.ORDER_YEAR <= "+endYear+"\n");  
		sql.append("   AND TTO.ORDER_MONTH >= "+startMonth+"\n");  
		sql.append("   AND TTO.ORDER_MONTH <= "+endMonth+"\n"); 
		sql.append(" GROUP BY TMD.DEALER_CODE,tvm.MATERIAL_CODE,TMD1.DEALER_CODE,\n");  
		sql.append("          TMD.DEALER_SHORTNAME,TMD1.DEALER_SHORTNAME,\n");  
		sql.append("          TTO.ORDER_NO,\n");  
		sql.append("          TTO.RAISE_DATE,\n");  
		sql.append("          TTO.ORDER_STATUS\n");  

		
		return dao.pageQuery(sql.toString(), null,dao.getFunName(),pageSize ,curPage );
	}
	
	/**
	 * 月度订单审核查询展示(事业部)汇总
	 * */
	public static PageResult <Map<String,Object>> monthOrderCheck_BUS_QueryList_Sum(Long orgId,String startYear,String startMonth,String endYear,String endMonth,String areaId,String groupCode,String dealerCode,String orderNO,String orderStatus, int pageSize,int curPage){
		StringBuffer sql = new StringBuffer("");
		sql.append("SELECT G2.GROUP_NAME,\n");
		sql.append("       TVM.MATERIAL_CODE,\n");  
		sql.append("       TVM.MATERIAL_NAME,\n");  
		sql.append("       TVM.COLOR_NAME,\n");  
		sql.append("       TTOD.ORDER_AMOUNT,\n");  
		sql.append("       NVL(TTOD.CALL_AMOUNT,0)CALL_AMOUNT\n");  
		sql.append("  FROM TT_VS_ORDER TTO,\n");  
		sql.append("       TT_VS_ORDER_DETAIL TTOD,\n");  
		sql.append("       TM_DEALER TMD,\n");  
		sql.append("       (select vod.dealer_id from vw_org_dealer vod where vod.root_org_id = "+orgId+") XX,\n");  
		sql.append("       TM_VHCL_MATERIAL TVM,\n");  
		sql.append("       TM_VHCL_MATERIAL_GROUP_R R,\n");  
		sql.append("       TM_VHCL_MATERIAL_GROUP G,\n");  
		sql.append("       TM_VHCL_MATERIAL_GROUP G1,\n");  
		sql.append("       TM_VHCL_MATERIAL_GROUP G2\n");  
		sql.append(" WHERE TTO.ORDER_ID = TTOD.ORDER_ID\n");  
		sql.append("   AND TTO.ORDER_ORG_ID = TMD.DEALER_ID\n");  
		sql.append("   AND TMD.DEALER_ID = XX.DEALER_ID\n");  
		sql.append("   AND TTOD.MATERIAL_ID = TVM.MATERIAL_ID\n");  
		sql.append("   AND TVM.MATERIAL_ID = R.MATERIAL_ID\n");  
		sql.append("   AND R.GROUP_ID = G.GROUP_ID\n");  
		sql.append("   AND G.PARENT_GROUP_ID = G1.GROUP_ID\n");  
		sql.append("   AND G1.PARENT_GROUP_ID = G2.GROUP_ID\n");  
		sql.append("   AND TTO.ORDER_STATUS IN ("+Constant.ORDER_STATUS_04+", "+Constant.ORDER_STATUS_08+","+Constant.ORDER_STATUS_09+" , "+Constant.ORDER_STATUS_05+")\n");  
		sql.append("   AND TTO.ORDER_TYPE = "+Constant.ORDER_TYPE_01+"\n");  
		if (null != areaId && !"".equals(areaId)) {
			sql.append("   AND TTO.AREA_ID ="+areaId+"\n");  
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
		if (null != groupCode && !"".equals(groupCode)) {
			String[] array = groupCode.split(",");
			sql.append("   AND TVM.MATERIAL_CODE IN (\n");
				for (int i = 0; i < array.length; i++) {
					sql.append("'"+ array[i]+"'");
					if (i != array.length - 1) {
						sql.append(",");
					}
				}
			sql.append(")\n");
		}
		if (null != orderNO && !"".equals(orderNO)) {
			sql.append("   AND TTO.ORDER_NO LIKE '%"+orderNO.trim()+"%'\n");  
		}
		if (!"".equals(orderStatus)) {
			sql.append("   AND TTO.ORDER_STATUS = "+orderStatus+"\n");
		}
		sql.append("   AND TTO.ORDER_YEAR >= "+startYear+"\n");  
		sql.append("   AND TTO.ORDER_YEAR <= "+endYear+"\n");  
		sql.append("   AND TTO.ORDER_MONTH >= "+startMonth+"\n");  
		sql.append("   AND TTO.ORDER_MONTH <= "+endMonth+"\n"); 
		return dao.pageQuery(sql.toString(), null,dao.getFunName(),pageSize ,curPage );
	}
	
	/**
	 * 月度订单审核查询展示(销售部)明细
	 * */
	public static PageResult <Map<String,Object>> monthOrderCheckQuery_SAL_QueryList(String orgId, String startYear, String startMonth,String endYear,String endMonth, String areaId,String groupCode, String dealerCode,String  orderNO,String orderStatus, int pageSize,int curPage){
		StringBuffer sql = new StringBuffer("");
		sql.append("SELECT TMD.DEALER_CODE,\n");
		sql.append("       TMD.DEALER_SHORTNAME DEALER_NAME,\n"); 
		sql.append("	   TMD1.DEALER_CODE dealer_code1,\n");
		sql.append("       TMD1.DEALER_SHORTNAME DEALER_NAME1,\n"); 
		sql.append("       TTO.ORDER_NO,\n");  
		sql.append("       TO_CHAR(TTO.RAISE_DATE, 'yyyy-MM-dd') RAISE_DATE,\n");  
		sql.append("       TTO.ORDER_STATUS,tvm.MATERIAL_code,\n");  
		sql.append("       SUM(TTOD.ORDER_AMOUNT) AS ORDER_AMOUNT,\n");  
		sql.append("       SUM(TTOD.CHECK_AMOUNT) AS CHECK_AMOUNT,\n");  
		sql.append("       SUM(TTOD.CALL_AMOUNT) AS CALL_AMOUNT\n");  
		sql.append("  FROM TT_VS_ORDER TTO, TM_DEALER TMD, tm_dealer tmd1, TT_VS_ORDER_DETAIL TTOD , TM_VHCL_MATERIAL TVM, vw_org_dealer TDOR\n");  
		sql.append(" WHERE TTO.ORDER_ORG_ID = TMD.DEALER_ID\n");  
		sql.append(" and TTO.billing_ORG_ID = TMD1.DEALER_ID\n");  
		sql.append("   AND TMD.DEALER_ID = TDOR.DEALER_ID\n");  
		sql.append("   AND TTO.ORDER_ID = TTOD.ORDER_ID\n");  
		sql.append("   AND TTOD.MATERIAL_ID = TVM.MATERIAL_ID\n");
		sql.append("   AND TTO.ORDER_STATUS IN ("+Constant.ORDER_STATUS_04+", "+Constant.ORDER_STATUS_08+", "+Constant.ORDER_STATUS_09+", "+Constant.ORDER_STATUS_05+")\n");  
		sql.append("   AND TTO.ORDER_TYPE = "+Constant.ORDER_TYPE_01+"\n");  
		if (null != areaId && !"".equals(areaId)) {
			sql.append("   AND TTO.AREA_ID ="+areaId+"\n");  
		}
		
		if(!CommonUtils.isNullString(orgId)) {
			sql.append("   AND TDOR.root_ORG_ID IN (").append(orgId).append(")\n");  
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
		if (null != groupCode && !"".equals(groupCode)) {
			String[] array = groupCode.split(",");
			sql.append("   AND TVM.MATERIAL_CODE IN (\n");
				for (int i = 0; i < array.length; i++) {
					sql.append("'"+ array[i]+"'");
					if (i != array.length - 1) {
						sql.append(",");
					}
				}
			sql.append(")\n");
		}
		if (null != orderNO && !"".equals(orderNO)) {
			sql.append("   AND TTO.ORDER_NO LIKE '%"+orderNO.trim()+"%'\n");  
		}
		if (!"".equals(orderStatus)) {
			sql.append("   AND TTO.ORDER_STATUS = "+orderStatus+"\n");
		}
		sql.append("   AND TTO.ORDER_YEAR >= "+startYear+"\n");  
		sql.append("   AND TTO.ORDER_YEAR <= "+endYear+"\n");  
		sql.append("   AND TTO.ORDER_MONTH >= "+startMonth+"\n");  
		sql.append("   AND TTO.ORDER_MONTH <= "+endMonth+"\n");  
		sql.append(" GROUP BY TMD.DEALER_CODE,tvm.MATERIAL_code,TMD1.DEALER_CODE,\n");  
		sql.append("          TMD.DEALER_SHORTNAME,TMD1.DEALER_SHORTNAME,\n");  
		sql.append("          TTO.ORDER_NO,\n");  
		sql.append("          TTO.RAISE_DATE,\n");  
		sql.append("          TTO.ORDER_STATUS\n");

		return dao.pageQuery(sql.toString(), null,dao.getFunName(),pageSize ,curPage );
	}
	/**
	 * 月度订单审核查询展示(销售部)明细
	 * */
	public static List <Map<String,Object>> monthOrderCheckQuery_SAL_QueryList_Load(String orgIds, String startYear, String startMonth,String endYear,String endMonth, String areaId,String groupCode, String dealerCode,String  orderNO,String orderStatus, int pageSize,int curPage){
		StringBuffer sql = new StringBuffer("");
		sql.append("SELECT TMD.DEALER_CODE,\n");
		sql.append("       TMD.DEALER_SHORTNAME DEALER_NAME,\n");  
		sql.append("	   TMD1.DEALER_CODE dealer_code1,\n");
		sql.append("       TMD1.DEALER_SHORTNAME DEALER_NAME1,\n");  
		sql.append("       TTO.ORDER_NO,\n");  
		sql.append("       TO_CHAR(TTO.RAISE_DATE, 'yyyy-MM-dd') RAISE_DATE,\n");  
		sql.append("       TC.CODE_DESC ORDER_STATUS,TVM.MATERIAL_CODE,\n");  
		sql.append("       SUM(TTOD.ORDER_AMOUNT) AS ORDER_AMOUNT,\n");  
		sql.append("       SUM(TTOD.CHECK_AMOUNT) AS CHECK_AMOUNT,\n");  
		sql.append("       SUM(TTOD.CALL_AMOUNT) AS CALL_AMOUNT\n");  
		sql.append("  FROM TT_VS_ORDER TTO, TM_DEALER TMD, tm_dealer tmd1,TC_CODE TC, TT_VS_ORDER_DETAIL TTOD , TM_VHCL_MATERIAL TVM, vw_org_dealer   TDOR\n");  
		sql.append(" WHERE TTO.ORDER_ORG_ID = TMD.DEALER_ID\n");
		sql.append("   AND TDOR.DEALER_ID = TMD.DEALER_ID\n"); 
		sql.append(" and TTO.billing_ORG_ID = TMD1.DEALER_ID\n");
		sql.append("   AND TC.CODE_ID = TTO.ORDER_STATUS");
		sql.append("   AND TTO.ORDER_ID = TTOD.ORDER_ID\n");  
		sql.append("   AND TTOD.MATERIAL_ID = TVM.MATERIAL_ID\n");
		sql.append("   AND TTO.ORDER_STATUS IN ("+Constant.ORDER_STATUS_04+", "+Constant.ORDER_STATUS_08+", "+Constant.ORDER_STATUS_09+", "+Constant.ORDER_STATUS_05+")\n");  
		sql.append("   AND TTO.ORDER_TYPE = "+Constant.ORDER_TYPE_01+"\n");  
		if (null != areaId && !"".equals(areaId)) {
			sql.append("   AND TTO.AREA_ID ="+areaId+"\n");  
		}
		
		if(!CommonUtils.isNullString(orgIds)) {
			sql.append("   AND TDOR.root_ORG_ID IN (").append(orgIds).append(")\n");  
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
		if (null != groupCode && !"".equals(groupCode)) {
			String[] array = groupCode.split(",");
			sql.append("   AND TVM.MATERIAL_CODE IN (\n");
				for (int i = 0; i < array.length; i++) {
					sql.append("'"+ array[i]+"'");
					if (i != array.length - 1) {
						sql.append(",");
					}
				}
			sql.append(")\n");
		}
		if (null != orderNO && !"".equals(orderNO)) {
			sql.append("   AND TTO.ORDER_NO LIKE '%"+orderNO.trim()+"%'\n");  
		}
		if (!"".equals(orderStatus)) {
			sql.append("   AND TTO.ORDER_STATUS = "+orderStatus+"\n");
		}
		sql.append("   AND TTO.ORDER_YEAR >= "+startYear+"\n");  
		sql.append("   AND TTO.ORDER_YEAR <= "+endYear+"\n");  
		sql.append("   AND TTO.ORDER_MONTH >= "+startMonth+"\n");  
		sql.append("   AND TTO.ORDER_MONTH <= "+endMonth+"\n");  
		sql.append(" GROUP BY TMD.DEALER_CODE,TVM.MATERIAL_CODE,TMD1.DEALER_CODE,\n");  
		sql.append("          TMD.DEALER_SHORTNAME,TMD1.DEALER_SHORTNAME,\n");  
		sql.append("          TTO.ORDER_NO,\n");  
		sql.append("          TTO.RAISE_DATE,\n");  
		sql.append("          TC.CODE_DESC\n");

		return dao.pageQuery(sql.toString(), null,dao.getFunName());
	}
	/**
	 * 月度订单审核查询展示(销售部)汇总
	 * */
	public static PageResult <Map<String,Object>> monthOrderCheckQuery_SAL_QueryList_Sum(String orgIds, String startYear, String startMonth,String endYear,String endMonth, String areaId,String groupCode, String dealerCode,String  orderNO,String orderStatus, int pageSize,int curPage){
		StringBuffer sql = new StringBuffer("");
		
		sql.append("SELECT G2.GROUP_NAME,\n");
		sql.append("       TVM.MATERIAL_CODE,\n");  
		sql.append("       TVM.MATERIAL_NAME,\n");  
		sql.append("       TVM.COLOR_NAME,\n");  
		sql.append("       TTOD.ORDER_AMOUNT,\n");  
		sql.append("       NVL(TTOD.CHECK_AMOUNT, 0) CHECK_AMOUNT,\n");  
		sql.append("       NVL(TTOD.CALL_AMOUNT, 0) CALL_AMOUNT\n");  
		sql.append("  FROM TT_VS_ORDER              TTO,\n");  
		sql.append("       TT_VS_ORDER_DETAIL       TTOD,\n");  
		sql.append("       TM_DEALER                TMD,\n");  
		sql.append("       vw_org_dealer   TDOR,\n");  
		sql.append("       TM_VHCL_MATERIAL         TVM,\n");  
		sql.append("       TM_VHCL_MATERIAL_GROUP_R R,\n");  
		sql.append("       TM_VHCL_MATERIAL_GROUP   G,\n");  
		sql.append("       TM_VHCL_MATERIAL_GROUP   G1,\n");  
		sql.append("       TM_VHCL_MATERIAL_GROUP   G2\n");  
		sql.append(" WHERE TTO.ORDER_ID = TTOD.ORDER_ID\n");  
		sql.append("   AND TDOR.DEALER_ID = TMD.DEALER_ID\n");  
		sql.append("   AND TTO.ORDER_ORG_ID = TMD.DEALER_ID\n");  
		sql.append("   AND TTOD.MATERIAL_ID = TVM.MATERIAL_ID\n");  
		sql.append("   AND TVM.MATERIAL_ID = R.MATERIAL_ID\n");  
		sql.append("   AND R.GROUP_ID = G.GROUP_ID\n");  
		sql.append("   AND G.PARENT_GROUP_ID = G1.GROUP_ID\n");  
		sql.append("   AND G1.PARENT_GROUP_ID = G2.GROUP_ID\n");  
		sql.append("   AND TTO.ORDER_STATUS IN ("+Constant.ORDER_STATUS_04+", "+Constant.ORDER_STATUS_08+", "+Constant.ORDER_STATUS_09+", "+Constant.ORDER_STATUS_05+")\n");  
		sql.append("   AND TTO.ORDER_TYPE = "+Constant.ORDER_TYPE_01+"\n");  
		if (null != areaId && !"".equals(areaId)) {
			sql.append("   AND TTO.AREA_ID ="+areaId+"\n");  
		}
		
		if(!CommonUtils.isNullString(orgIds)) {
			sql.append("   AND TDOR.root_ORG_ID IN (").append(orgIds).append(")\n");  
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
		if (null != groupCode && !"".equals(groupCode)) {
			String[] array = groupCode.split(",");
			sql.append("   AND TVM.MATERIAL_CODE IN (\n");
				for (int i = 0; i < array.length; i++) {
					sql.append("'"+ array[i]+"'");
					if (i != array.length - 1) {
						sql.append(",");
					}
				}
			sql.append(")\n");
		}
		if (null != orderNO && !"".equals(orderNO)) {
			sql.append("   AND TTO.ORDER_NO LIKE '%"+orderNO.trim()+"%'\n");  
		}
		if (!"".equals(orderStatus)) {
			sql.append("   AND TTO.ORDER_STATUS = "+orderStatus+"\n");
		}
		sql.append("   AND TTO.ORDER_YEAR >= "+startYear+"\n");  
		sql.append("   AND TTO.ORDER_YEAR <= "+endYear+"\n");  
		sql.append("   AND TTO.ORDER_MONTH >= "+startMonth+"\n");  
		sql.append("   AND TTO.ORDER_MONTH <= "+endMonth+"\n");
	 	sql.append("  GROUP BY  G2.GROUP_NAME,TVM.MATERIAL_CODE,TVM.MATERIAL_NAME,TVM.COLOR_NAME,TTOD.ORDER_AMOUNT,TTOD.CHECK_AMOUNT,TTOD.CALL_AMOUNT\n");
		sql.append("   ORDER BY TVM.MATERIAL_CODE\n");
		return dao.pageQuery(sql.toString(), null,dao.getFunName(),pageSize ,curPage );
	}
	/**
	 * 月度订单审核查询展示(销售部)汇总
	 * */
	public static List <Map<String,Object>> monthOrderCheckQuery_SAL_QueryList_Sum_Load(String orgIds, String startYear, String startMonth,String endYear,String endMonth, String areaId,String groupCode, String dealerCode,String  orderNO,String orderStatus, int pageSize,int curPage){
		StringBuffer sql = new StringBuffer("");
		
		sql.append("SELECT G2.GROUP_NAME,\n");
		sql.append("       TVM.MATERIAL_CODE,\n");  
		sql.append("       TVM.MATERIAL_NAME,\n");  
		sql.append("       TVM.COLOR_NAME,\n");  
		sql.append("       TTOD.ORDER_AMOUNT,\n");  
		sql.append("       NVL(TTOD.CHECK_AMOUNT, 0) CHECK_AMOUNT,\n");  
		sql.append("       NVL(TTOD.CALL_AMOUNT, 0) CALL_AMOUNT\n");  
		sql.append("  FROM TT_VS_ORDER              TTO,\n");  
		sql.append("       TT_VS_ORDER_DETAIL       TTOD,\n");  
		sql.append("       TM_DEALER                TMD,\n");  
		sql.append("       vw_org_dealer   TDOR,\n");  
		sql.append("       TM_VHCL_MATERIAL         TVM,\n");  
		sql.append("       TM_VHCL_MATERIAL_GROUP_R R,\n");  
		sql.append("       TM_VHCL_MATERIAL_GROUP   G,\n");  
		sql.append("       TM_VHCL_MATERIAL_GROUP   G1,\n");  
		sql.append("       TM_VHCL_MATERIAL_GROUP   G2\n");  
		sql.append(" WHERE TTO.ORDER_ID = TTOD.ORDER_ID\n");  
		sql.append("   AND TDOR.DEALER_ID = TMD.DEALER_ID\n");  
		sql.append("   AND TTO.ORDER_ORG_ID = TMD.DEALER_ID\n");  
		sql.append("   AND TTOD.MATERIAL_ID = TVM.MATERIAL_ID\n");  
		sql.append("   AND TVM.MATERIAL_ID = R.MATERIAL_ID\n");  
		sql.append("   AND R.GROUP_ID = G.GROUP_ID\n");  
		sql.append("   AND G.PARENT_GROUP_ID = G1.GROUP_ID\n");  
		sql.append("   AND G1.PARENT_GROUP_ID = G2.GROUP_ID\n");  
		sql.append("   AND TTO.ORDER_STATUS IN ("+Constant.ORDER_STATUS_04+", "+Constant.ORDER_STATUS_08+", "+Constant.ORDER_STATUS_09+", "+Constant.ORDER_STATUS_05+")\n");  
		sql.append("   AND TTO.ORDER_TYPE = "+Constant.ORDER_TYPE_01+"\n");  
		if (null != areaId && !"".equals(areaId)) {
			sql.append("   AND TTO.AREA_ID ="+areaId+"\n");  
		}
		
		if(!CommonUtils.isNullString(orgIds)) {
			sql.append("   AND TDOR.root_ORG_ID IN (").append(orgIds).append(")\n");  
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
		if (null != groupCode && !"".equals(groupCode)) {
			String[] array = groupCode.split(",");
			sql.append("   AND TVM.MATERIAL_CODE IN (\n");
				for (int i = 0; i < array.length; i++) {
					sql.append("'"+ array[i]+"'");
					if (i != array.length - 1) {
						sql.append(",");
					}
				}
			sql.append(")\n");
		}
		if (null != orderNO && !"".equals(orderNO)) {
			sql.append("   AND TTO.ORDER_NO LIKE '%"+orderNO.trim()+"%'\n");  
		}
		if (!"".equals(orderStatus)) {
			sql.append("   AND TTO.ORDER_STATUS = "+orderStatus+"\n");
		}
		sql.append("   AND TTO.ORDER_YEAR >= "+startYear+"\n");  
		sql.append("   AND TTO.ORDER_YEAR <= "+endYear+"\n");  
		sql.append("   AND TTO.ORDER_MONTH >= "+startMonth+"\n");  
		sql.append("   AND TTO.ORDER_MONTH <= "+endMonth+"\n");
	 	sql.append("  GROUP BY  G2.GROUP_NAME,TVM.MATERIAL_CODE,TVM.MATERIAL_NAME,TVM.COLOR_NAME,TTOD.ORDER_AMOUNT,TTOD.CHECK_AMOUNT,TTOD.CALL_AMOUNT\n");
		sql.append("   ORDER BY TVM.MATERIAL_CODE\n");
		return dao.pageQuery(sql.toString(), null,dao.getFunName());
	}
	
	/**
	 * 月度订单审核：查询可审核的订单列表(销售部)
	 * */
	public static PageResult <Map<String,Object>> monthOrderCheck_SAL_QueryList(String year, String month, String areaId, String dealerCode, String orderNO, int pageSize,int curPage){
		StringBuffer sql = new StringBuffer("");
		
		sql.append("SELECT TTO.ORDER_ID,\n");
		sql.append("       TMD.DEALER_CODE,\n");  
		sql.append("       TMD.DEALER_NAME,\n");  
		sql.append("       TTO.ORDER_NO,\n");  
		sql.append("       TO_CHAR(TTO.RAISE_DATE, 'yyyy-MM-dd') RAISE_DATE,\n");  
		sql.append("       SUM(TTOD.ORDER_AMOUNT) AS ORDER_AMOUNT\n");  
		sql.append("  FROM TT_VS_ORDER TTO, TM_DEALER TMD, TT_VS_ORDER_DETAIL TTOD\n");  
		sql.append(" WHERE TTO.ORDER_ORG_ID = TMD.DEALER_ID\n");  
		sql.append("   AND TTO.ORDER_ID = TTOD.ORDER_ID\n");  
		if (null != areaId && !"".equals(areaId)) {
			sql.append("   AND TTO.AREA_ID ="+areaId+"\n");  
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
		if (null != orderNO && !"".equals(orderNO)) {
			sql.append("   AND TTO.ORDER_NO LIKE '%"+orderNO.trim()+"%'\n");  
		}
		sql.append("   AND TTO.ORDER_YEAR = "+year+"\n");  
		sql.append("   AND TTO.ORDER_MONTH = "+month+"\n");  
		sql.append("   AND TTO.ORDER_STATUS = "+Constant.ORDER_STATUS_09+"\n");  
		sql.append(" GROUP BY TTO.ORDER_ID,\n");  
		sql.append("          TMD.DEALER_CODE,\n");  
		sql.append("          TMD.DEALER_NAME,\n");  
		sql.append("          TTO.ORDER_NO,\n");  
		sql.append("          TTO.RAISE_DATE\n");  
		sql.append(" ORDER BY TTO.ORDER_ID\n");
		return dao.pageQuery(sql.toString(), null,dao.getFunName(),pageSize ,curPage );
	}
	
	public static Map<String, Object> getOrderInfo(String orderId) {
		StringBuffer sql = new StringBuffer();

		sql.append("SELECT TMD.DEALER_CODE,\n");
		sql.append("       TMD.DEALER_SHORTNAME,\n");  
		sql.append("       TTO.ORDER_NO,\n");  
		sql.append("       TTO.ORDER_YEAR || '年' || TTO.ORDER_MONTH || '月' AS ORDERMONTH,\n");  
		sql.append("       TTO.ORDER_ID\n");
		sql.append("  FROM TT_VS_ORDER TTO, TM_DEALER TMD\n");  
		sql.append(" WHERE TTO.ORDER_ORG_ID = TMD.DEALER_ID\n");  
		sql.append("   AND TTO.ORDER_ID = "+orderId+"\n");


		Map<String, Object> orderInfo = dao.pageQueryMap(sql.toString(), null,dao.getFunName());
		return orderInfo;
	}
	
	public static List<Map<String, Object>> getDetailOrderList(String orderId) {

		StringBuffer sql = new StringBuffer();

		sql.append("SELECT TTOD.DETAIL_ID,\n");
		sql.append("       TVM.MATERIAL_CODE,\n");  
		sql.append("       TVM.MATERIAL_NAME,\n");  
		sql.append("       TTOD.ORDER_AMOUNT,\n");  
		sql.append("       G3.GROUP_NAME\n");  
		sql.append("  FROM TT_VS_ORDER_DETAIL       TTOD,\n");  
		sql.append("       TM_VHCL_MATERIAL         TVM,\n");  
		sql.append("       TM_VHCL_MATERIAL_GROUP_R R,\n");  
		sql.append("       TM_VHCL_MATERIAL_GROUP   G1,\n");  
		sql.append("       TM_VHCL_MATERIAL_GROUP   G2,\n");  
		sql.append("       TM_VHCL_MATERIAL_GROUP   G3\n");  
		sql.append(" WHERE TTOD.MATERIAL_ID = TVM.MATERIAL_ID\n");  
		sql.append("   AND TVM.MATERIAL_ID = R.MATERIAL_ID\n");  
		sql.append("   AND R.GROUP_ID = G1.GROUP_ID\n");  
		sql.append("   AND G1.PARENT_GROUP_ID = G2.GROUP_ID\n");  
		sql.append("   AND G2.PARENT_GROUP_ID = G3.GROUP_ID\n");  
		sql.append("   AND TTOD.ORDER_ID = "+orderId+"\n");  
		sql.append(" ORDER BY TTOD.DETAIL_ID\n");

		List<Map<String, Object>> list = dao.pageQuery(sql.toString(), null,dao.getFunName());
		return list;
	}
	/**
	 * 月度审核查询：事业部
	 * */
	public static List<Map<String, Object>> getMonthOrderCheckQuery_BUS_Query_Detail(String orderId) {

		StringBuffer sql = new StringBuffer();

		sql.append("SELECT TTOD.DETAIL_ID,\n");
		sql.append("       TVM.MATERIAL_CODE,\n");  
		sql.append("       TVM.MATERIAL_NAME,\n");  
		sql.append("       TTOD.ORDER_AMOUNT,\n");  
		sql.append("       G3.GROUP_NAME\n");  
		sql.append("  FROM TT_VS_ORDER_DETAIL       TTOD,\n");  
		sql.append("       TM_VHCL_MATERIAL         TVM,\n");  
		sql.append("       TM_VHCL_MATERIAL_GROUP_R R,\n");  
		sql.append("       TM_VHCL_MATERIAL_GROUP   G1,\n");  
		sql.append("       TM_VHCL_MATERIAL_GROUP   G2,\n");  
		sql.append("       TM_VHCL_MATERIAL_GROUP   G3\n");  
		sql.append(" WHERE TTOD.MATERIAL_ID = TVM.MATERIAL_ID\n");  
		sql.append("   AND TVM.MATERIAL_ID = R.MATERIAL_ID\n");  
		sql.append("   AND R.GROUP_ID = G1.GROUP_ID\n");  
		sql.append("   AND G1.PARENT_GROUP_ID = G2.GROUP_ID\n");  
		sql.append("   AND G2.PARENT_GROUP_ID = G3.GROUP_ID\n");  
		sql.append("   AND TTOD.ORDER_ID = "+orderId+"\n");  
		sql.append(" ORDER BY TTOD.DETAIL_ID\n");


		List<Map<String, Object>> list = dao.pageQuery(sql.toString(), null,dao.getFunName());
		return list;
	}
	
	/**
	 * 月度审核查询：销售部
	 * */
	public static List<Map<String, Object>> getMonthOrderCheckQuery_SAL_Query_Detail(String orderId) {

		StringBuffer sql = new StringBuffer();

		sql.append("SELECT TTOD.DETAIL_ID,\n");
		sql.append("       TVM.MATERIAL_CODE,\n");  
		sql.append("       TVM.MATERIAL_NAME,\n");  
		sql.append("       TTOD.ORDER_AMOUNT,\n");  
		sql.append("       G3.GROUP_NAME,\n");  
		sql.append("       TTOD.REGION_CHECK_AMOUNT CHECK_AMOUNT\n");
		sql.append("  FROM TT_VS_ORDER_DETAIL       TTOD,\n");  
		sql.append("       TM_VHCL_MATERIAL         TVM,\n");  
		sql.append("       TM_VHCL_MATERIAL_GROUP_R R,\n");  
		sql.append("       TM_VHCL_MATERIAL_GROUP   G1,\n");  
		sql.append("       TM_VHCL_MATERIAL_GROUP   G2,\n");  
		sql.append("       TM_VHCL_MATERIAL_GROUP   G3\n");  
		sql.append(" WHERE TTOD.MATERIAL_ID = TVM.MATERIAL_ID\n");  
		sql.append("   AND TVM.MATERIAL_ID = R.MATERIAL_ID\n");  
		sql.append("   AND R.GROUP_ID = G1.GROUP_ID\n");  
		sql.append("   AND G1.PARENT_GROUP_ID = G2.GROUP_ID\n");  
		sql.append("   AND G2.PARENT_GROUP_ID = G3.GROUP_ID\n");  
		sql.append("   AND TTOD.ORDER_ID = "+orderId+"\n");  
		sql.append(" ORDER BY TTOD.DETAIL_ID\n");


		List<Map<String, Object>> list = dao.pageQuery(sql.toString(), null,dao.getFunName());
		return list;
	}
	
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
}
