package com.infodms.dms.dao.sales.storageManage;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.common.Constant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TmBusinessAreaPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.businessUtil.GetVinUtil;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.po3.POFactory;
import com.infoservice.po3.POFactoryBuilder;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

public class CheckBillDAO extends BaseDao {
	public static Logger logger = Logger.getLogger(CheckBillDAO.class);
	private static POFactory factory = POFactoryBuilder.getInstance();
	private static final CheckBillDAO dao = new CheckBillDAO();

	public static final CheckBillDAO getInstance() {
		return dao;
	}

	/***************************************************************************
	 * 查询可验收入库的车辆列表
	 * 
	 * @param :
	 * @return
	 * @throws Exception
	 */

	public static PageResult <Map<String,Object>> getCheckList(String poseId,String dealer_Id,String vin, String dlvNo,String dlvryNo, int pageSize,int curPage){
		StringBuffer sql = new StringBuffer("");

		sql.append("SELECT TMV.VEHICLE_ID, --查询可验收入库的车辆列表\n");
		sql.append("       TMV.DEALER_ID, --开票方经销商ID\n");
		sql.append("       TTDED.DETAIL_ID AS TTDED_ID, --发运单明细ID\n");
		sql.append("       G.GROUP_NAME, --车型名称\n");
		sql.append("       TMVM.MATERIAL_CODE, --物料代码\n");
		sql.append("       TMVM.MATERIAL_NAME, --物料名称\n");
		sql.append("       TVDR.RECEIVER, --接收方\n");
		sql.append("       TVO.ORDER_ORG_ID, --订货方\n");
		sql.append("       TMV.VIN, --VIN\n");
		sql.append("       TMV.ENGINE_NO, --engine_no\n");
		sql.append("       TTDE.SENDCAR_ORDER_NUMBER, -- 发运单号\n");
		sql.append("       TBA.AREA_NAME, --车辆业务范围\n");
		sql.append("       TVDR.DLVRY_REQ_NO,\n");
		sql.append("       TTDE.SHIP_METHOD_CODE,\n");
		sql.append("       TTDE.MOTORMAN,\n");
		sql.append("       TTDE.MOTORMAN_PHONE,\n");

		sql.append("       TO_CHAR(TTDE.FLATCAR_ASSIGN_DATE, 'YYYY-MM-DD') AS DELIVERY_DATE,\n");
		sql.append("       TMW.WAREHOUSE_NAME,\n");
		sql.append("          TO_CHAR(TMV.PRODUCT_DATE,'YYYY-MM-DD') as PRODUCT_DATE,\n");
		sql.append("          TO_CHAR(TMV.FACTORY_DATE,'YYYY-MM-DD') as FACTORY_DATE,\n");
		sql.append("       TO_CHAR(TTDE.ARRIVE_DATE,'YYYY-MM-DD') as ARRIVE_DATE,\n");
		sql.append("       DECODE(SIGN(TRUNC(TTDE.ARRIVE_DATE, 'DD') - (SYSDATE)), -1, 1, 0) AS IS_RED\n");
		sql.append("  FROM TM_VEHICLE              TMV,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP  G,\n");
		sql.append("       TM_VHCL_MATERIAL        TMVM,\n");
		sql.append("       TT_VS_ORDER             TVO, --订单\n");
		sql.append("       TT_VS_DLVRY_REQ         TVDR, --发运申请\n");
		sql.append("       TT_VS_DLVRY_ERP_DTL     TTDED, --ERP发运子表\n");
		sql.append("       TT_VS_DLVRY_ERP         TTDE, --ERP发运主表\n");
		sql.append("       TM_BUSINESS_AREA        TBA,\n");
		sql.append("       TM_WAREHOUSE            TMW,\n");
		sql.append("       TM_DEALER_BUSINESS_AREA TDBA,\n");
		sql.append("       TM_POSE_BUSINESS_AREA   TPBA\n");
		sql.append(" WHERE TMV.MODEL_ID = G.GROUP_ID\n");
		sql.append("   AND TVO.ORDER_ID = TVDR.ORDER_ID\n");
		sql.append("   AND TVDR.REQ_ID = TTDED.REQ_ID\n");
		sql.append("   AND TMV.AREA_ID = TBA.AREA_ID\n");
		sql.append("   AND TMV.MATERIAL_ID = TMVM.MATERIAL_ID\n");
		sql.append("   AND TMV.VEHICLE_ID = TTDED.VEHICLE_ID\n");
		sql.append("   AND TTDE.SENDCAR_HEADER_ID = TTDED.SENDCAR_HEADER_ID\n");
		sql.append("   AND TMV.WAREHOUSE_ID = TMW.WAREHOUSE_ID\n");
		sql.append("   AND TDBA.DEALER_ID = TVDR.RECEIVER\n");
		sql.append("   AND TTDED.IS_RECEIVE = " + Constant.IS_RECEIVE_0 + "\n");
		sql.append("   AND TDBA.AREA_ID = TPBA.AREA_ID");

		if (null != vin && !"".equals(vin)) {
			sql.append(GetVinUtil.getVins(vin, "TMV"));
		}
		if (null != dlvNo && !"".equals(dlvNo)) {
			sql.append("   AND TVDR.DLVRY_REQ_NO like UPPER('%" + dlvNo + "%') \n");
		}

		if (null != dlvryNo && !"".equals(dlvryNo)) {
			sql.append("   AND TTDE.SENDCAR_ORDER_NUMBER like UPPER('%" + dlvryNo + "%') \n");
		}
		sql.append("   AND TMV.LIFE_CYCLE = "+Constant.VEHICLE_LIFE_08+"\n");  //车厂在途
		sql.append("	and TVDR.receiver IN ("+dealer_Id+")\n") ;
		sql.append("and tpba.pose_id = "+poseId+"\n");
		
		
		sql.append("GROUP BY TMV.AREA_ID,\n") ;
		sql.append("         TMV.VEHICLE_ID,\n") ;
		sql.append("         TMV.DEALER_ID,\n") ;
		sql.append("         TTDED.DETAIL_ID,\n") ;
		sql.append("         G.GROUP_NAME,\n") ;
		sql.append("         TMVM.MATERIAL_CODE,\n") ;
		sql.append("         TMVM.MATERIAL_NAME,\n") ;
		sql.append("         TVDR.RECEIVER,\n") ;
		sql.append("         TVO.ORDER_ORG_ID,\n") ;
		sql.append("         TMV.VIN,\n") ;
		sql.append("         TMV.ENGINE_NO,\n") ;
		sql.append("         TTDE.SENDCAR_ORDER_NUMBER,\n") ;
		sql.append("         TBA.AREA_NAME,\n") ;
		sql.append("         TTDE.FLATCAR_ASSIGN_DATE,\n") ;
		sql.append("         TMW.WAREHOUSE_NAME,\n") ;
		sql.append("         TVDR.DLVRY_REQ_NO,\n") ;
		sql.append("         TTDE.SHIP_METHOD_CODE,\n") ;
		sql.append("         TTDE.MOTORMAN,\n") ;
		sql.append("          TTDE.MOTORMAN_PHONE,\n") ;
		sql.append("          TMV.PRODUCT_DATE,\n") ;
		sql.append("          TMV.FACTORY_DATE,\n") ;
		sql.append("         TTDE.ARRIVE_DATE\n") ;
		sql.append("ORDER BY TTDE.FLATCAR_ASSIGN_DATE DESC") ;

		

		return dao.pageQuery(sql.toString(), null, "com.infodms.dms.dao.sales.storageManage.CheckVehicleDAO.getCheckList", pageSize, curPage);
	}
	/***************************************************************************
	 * 查询可验收入库的车辆列表
	 * 
	 * @param :
	 * @return
	 * @throws Exception
	 */

	public static PageResult <Map<String,Object>> getUnCheckList(String dutyType,String orgId,String poseId,String dealer_Id,String vin, String dlvNo,String dlvryNo, int pageSize,int curPage){
		StringBuffer sql = new StringBuffer("");

		sql.append("SELECT TMV.VEHICLE_ID, --查询可验收入库的车辆列表\n");
		sql.append("       TMV.DEALER_ID, --开票方经销商ID\n");
		sql.append("       TTDED.DETAIL_ID AS TTDED_ID, --发运单明细ID\n");
		sql.append("       G.GROUP_NAME, --车型名称\n");
		sql.append("       TMVM.MATERIAL_CODE, --物料代码\n");
		sql.append("       TMVM.MATERIAL_NAME, --物料名称\n");
		sql.append("       TVDR.RECEIVER, --接收方\n");
		sql.append("       TVO.ORDER_ORG_ID, --订货方\n");
		sql.append("       TMV.VIN, --VIN\n");
		sql.append("       vod.dealer_code, \n");
		sql.append("       vod.dealer_shortname, \n");
		sql.append("       TTDE.SENDCAR_ORDER_NUMBER, -- 发运单号\n");
		sql.append("       TBA.AREA_NAME, --车辆业务范围\n");
		sql.append("       TVDR.DLVRY_REQ_NO,\n");
		sql.append("       TTDE.SHIP_METHOD_CODE,\n");
		sql.append("       TTDE.MOTORMAN,\n");
		sql.append("       TTDE.MOTORMAN_PHONE,\n");
		sql.append("       TO_CHAR(TTDE.FLATCAR_ASSIGN_DATE, 'YYYY-MM-DD') AS DELIVERY_DATE,\n");
		sql.append("       TMW.WAREHOUSE_NAME,\n");
		sql.append("          TO_CHAR(TMV.PRODUCT_DATE,'YYYY-MM-DD') as PRODUCT_DATE,\n");
		sql.append("          TO_CHAR(TMV.FACTORY_DATE,'YYYY-MM-DD') as FACTORY_DATE,\n");
		sql.append("       TO_CHAR(TTDE.ARRIVE_DATE,'YYYY-MM-DD') as ARRIVE_DATE,\n");
		sql.append("       DECODE(SIGN(TRUNC(TTDE.ARRIVE_DATE, 'DD') - (SYSDATE)), -1, 1, 0) AS IS_RED\n");
		sql.append("  FROM TM_VEHICLE              TMV,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP  G,\n");
		sql.append("       TM_VHCL_MATERIAL        TMVM,\n");
		sql.append("       TT_VS_ORDER             TVO, --订单\n");
		sql.append("       TT_VS_DLVRY_REQ         TVDR, --发运申请\n");
		sql.append("       TT_VS_DLVRY_ERP_DTL     TTDED, --ERP发运子表\n");
		sql.append("       TT_VS_DLVRY_ERP         TTDE, --ERP发运主表\n");
		sql.append("       TM_BUSINESS_AREA        TBA,\n");
		sql.append("    	tm_dealer vod , \n");
		sql.append("       TM_WAREHOUSE            TMW,\n");
		sql.append("       TM_DEALER_BUSINESS_AREA TDBA,\n");
		sql.append("       TM_POSE_BUSINESS_AREA   TPBA\n");
		sql.append(" WHERE TMV.MODEL_ID = G.GROUP_ID\n");
		sql.append("   AND TVO.ORDER_ID = TVDR.ORDER_ID\n");
		sql.append("   AND TVDR.REQ_ID = TTDED.REQ_ID\n");
		sql.append("   AND TMV.AREA_ID = TBA.AREA_ID\n");
		sql.append("  and vod.dealer_id(+)=TVDR.RECEIVER\n");
		sql.append("   AND TMV.MATERIAL_ID = TMVM.MATERIAL_ID\n");
		sql.append("   AND TMV.VEHICLE_ID = TTDED.VEHICLE_ID\n");
		sql.append("   AND TTDE.SENDCAR_HEADER_ID = TTDED.SENDCAR_HEADER_ID\n");
		sql.append("   AND TMV.WAREHOUSE_ID = TMW.WAREHOUSE_ID\n");
		sql.append("   AND TDBA.DEALER_ID = TVDR.RECEIVER\n");
		sql.append("   AND TTDED.IS_RECEIVE = " + Constant.IS_RECEIVE_0 + "\n");
		sql.append("   AND TDBA.AREA_ID = TPBA.AREA_ID");

		if (null != vin && !"".equals(vin)) {
			sql.append(GetVinUtil.getVins(vin, "TMV"));
		}
		if (null != dlvNo && !"".equals(dlvNo)) {
			sql.append("   AND TVDR.DLVRY_REQ_NO like UPPER('%" + dlvNo + "%') \n");
		}

		if (null != dlvryNo && !"".equals(dlvryNo)) {
			sql.append("   AND TTDE.SENDCAR_ORDER_NUMBER like UPPER('%" + dlvryNo + "%') \n");
		}
		sql.append("   AND TMV.LIFE_CYCLE = "+Constant.VEHICLE_LIFE_08+"\n");  //车厂在途
		if(null != dealer_Id && !"".equals(dealer_Id)){
			sql.append(" AND TVDR.receiver IN\n" );
			sql.append("      (SELECT TD1.DEALER_ID\n" );
			sql.append("         FROM TM_DEALER TD1\n" );
			sql.append("        WHERE 1 = 1\n" );
			sql.append("        START WITH TD1.DEALER_ID IN ("+dealer_Id+")\n" );
			sql.append("       CONNECT BY PRIOR TD1.DEALER_ID = TD1.PARENT_DEALER_D)");
		}
		sql.append("and tpba.pose_id = "+poseId+"\n");
		//大区
		if("10431003".equals(dutyType)){
			sql.append("	and TVDR.receiver IN (select vod.dealer_id from  vw_org_dealer vod where vod.root_org_id="+orgId+" )\n") ;
		}
		//小区
		if("10431004".equals(dutyType)){
			sql.append("	and TVDR.receiver IN (select vod.dealer_id from  vw_org_dealer vod where vod.pq_org_id="+orgId+" )\n") ;
		}
		sql.append("GROUP BY TMV.AREA_ID,\n") ;
		sql.append("         TMV.VEHICLE_ID,\n") ;
		sql.append("         TMV.DEALER_ID,\n") ;
		sql.append("         TTDED.DETAIL_ID,\n") ;
		sql.append("         G.GROUP_NAME,\n") ;
		sql.append("         TMVM.MATERIAL_CODE,\n") ;
		sql.append("         TMVM.MATERIAL_NAME,\n") ;
		sql.append("         TVDR.RECEIVER,\n") ;
		sql.append("         TVO.ORDER_ORG_ID,\n") ;
		sql.append("         TMV.VIN,\n") ;
		sql.append("         TTDE.SENDCAR_ORDER_NUMBER,\n") ;
		sql.append("         TBA.AREA_NAME,\n") ;
		sql.append("         TTDE.FLATCAR_ASSIGN_DATE,\n") ;
		sql.append("         TMW.WAREHOUSE_NAME,\n") ;
		sql.append("         TVDR.DLVRY_REQ_NO,\n") ;
		sql.append("         TTDE.SHIP_METHOD_CODE,\n") ;
		sql.append("         TTDE.MOTORMAN,\n") ;
		sql.append("          TTDE.MOTORMAN_PHONE,\n") ;
		sql.append("         vod.dealer_code,\n") ;
		sql.append("          vod.dealer_shortname,\n") ;
		sql.append("          TMV.PRODUCT_DATE,\n") ;
		sql.append("          TMV.FACTORY_DATE,\n") ;
		sql.append("         TTDE.ARRIVE_DATE\n") ;
		
		
		sql.append("ORDER BY TTDE.FLATCAR_ASSIGN_DATE DESC") ;

		

		return dao.pageQuery(sql.toString(), null, "com.infodms.dms.dao.sales.storageManage.CheckVehicleDAO.getCheckList", pageSize, curPage);
	}
	/***************************************************************************
	 * 查询可验收入库的车辆列表
	 * 
	 * @param :
	 * @return
	 * @throws Exception
	 */

	public static List<Map<String,Object>> getCheckLists(String poseId,String dealer_Id,String vin, String dlvNo,String dlvryNo, int pageSize,int curPage){
		StringBuffer sql = new StringBuffer("");

		sql.append("SELECT TMV.VEHICLE_ID, --查询可验收入库的车辆列表\n");
		sql.append("       TMV.DEALER_ID, --开票方经销商ID\n");
		sql.append("       TTDED.DETAIL_ID AS TTDED_ID, --发运单明细ID\n");
		sql.append("       G.GROUP_NAME, --车型名称\n");
		sql.append("       TMVM.MATERIAL_CODE, --物料代码\n");
		sql.append("       TMVM.MATERIAL_NAME, --物料名称\n");
		sql.append("       TVDR.RECEIVER, --接收方\n");
		sql.append("       TVO.ORDER_ORG_ID, --订货方\n");
		sql.append("       TMV.VIN, --VIN\n");
		sql.append("       TTDE.SENDCAR_ORDER_NUMBER, -- 发运单号\n");
		sql.append("       TBA.AREA_NAME, --车辆业务范围\n");
		sql.append("       TVDR.DLVRY_REQ_NO,\n");
		sql.append("       TTDE.SHIP_METHOD_CODE,\n");
		sql.append("       TTDE.MOTORMAN,\n");
		sql.append("       TTDE.MOTORMAN_PHONE,\n");

		sql.append("       TO_CHAR(TTDE.FLATCAR_ASSIGN_DATE, 'YYYY-MM-DD') AS DELIVERY_DATE,\n");
		sql.append("       TMW.WAREHOUSE_NAME,\n");
		sql.append("          TO_CHAR(TMV.PRODUCT_DATE,'YYYY-MM-DD') as PRODUCT_DATE,\n");
		sql.append("          TO_CHAR(TMV.FACTORY_DATE,'YYYY-MM-DD') as FACTORY_DATE,\n");
		sql.append("       TO_CHAR(TTDE.ARRIVE_DATE,'YYYY-MM-DD') as ARRIVE_DATE,\n");
		sql.append("       DECODE(SIGN(TRUNC(TTDE.ARRIVE_DATE, 'DD') - (SYSDATE)), -1, 1, 0) AS IS_RED\n");
		sql.append("  FROM TM_VEHICLE              TMV,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP  G,\n");
		sql.append("       TM_VHCL_MATERIAL        TMVM,\n");
		sql.append("       TT_VS_ORDER             TVO, --订单\n");
		sql.append("       TT_VS_DLVRY_REQ         TVDR, --发运申请\n");
		sql.append("       TT_VS_DLVRY_ERP_DTL     TTDED, --ERP发运子表\n");
		sql.append("       TT_VS_DLVRY_ERP         TTDE, --ERP发运主表\n");
		sql.append("       TM_BUSINESS_AREA        TBA,\n");
		sql.append("       TM_WAREHOUSE            TMW,\n");
		sql.append("       TM_DEALER_BUSINESS_AREA TDBA,\n");
		sql.append("       TM_POSE_BUSINESS_AREA   TPBA\n");
		sql.append(" WHERE TMV.MODEL_ID = G.GROUP_ID\n");
		sql.append("   AND TVO.ORDER_ID = TVDR.ORDER_ID\n");
		sql.append("   AND TVDR.REQ_ID = TTDED.REQ_ID\n");
		sql.append("   AND TMV.AREA_ID = TBA.AREA_ID\n");
		sql.append("   AND TMV.MATERIAL_ID = TMVM.MATERIAL_ID\n");
		sql.append("   AND TMV.VEHICLE_ID = TTDED.VEHICLE_ID\n");
		sql.append("   AND TTDE.SENDCAR_HEADER_ID = TTDED.SENDCAR_HEADER_ID\n");
		sql.append("   AND TMV.WAREHOUSE_ID = TMW.WAREHOUSE_ID\n");
		sql.append("   AND TDBA.DEALER_ID = TVDR.RECEIVER\n");
		sql.append("   AND TTDED.IS_RECEIVE = " + Constant.IS_RECEIVE_0 + "\n");
		sql.append("   AND TDBA.AREA_ID = TPBA.AREA_ID");

		if (null != vin && !"".equals(vin)) {
			sql.append(GetVinUtil.getVins(vin, "TMV"));
		}
		if (null != dlvNo && !"".equals(dlvNo)) {
			sql.append("   AND TVDR.DLVRY_REQ_NO like UPPER('%" + dlvNo + "%') \n");
		}

		if (null != dlvryNo && !"".equals(dlvryNo)) {
			sql.append("   AND TTDE.SENDCAR_ORDER_NUMBER like UPPER('%" + dlvryNo + "%') \n");
		}
		sql.append("   AND TMV.LIFE_CYCLE = "+Constant.VEHICLE_LIFE_08+"\n");  //车厂在途
		sql.append("	and TVDR.receiver IN ("+dealer_Id+")\n") ;
		sql.append("and tpba.pose_id = "+poseId+"\n");
		
		
		sql.append("GROUP BY TMV.AREA_ID,\n") ;
		sql.append("         TMV.VEHICLE_ID,\n") ;
		sql.append("         TMV.DEALER_ID,\n") ;
		sql.append("         TTDED.DETAIL_ID,\n") ;
		sql.append("         G.GROUP_NAME,\n") ;
		sql.append("         TMVM.MATERIAL_CODE,\n") ;
		sql.append("         TMVM.MATERIAL_NAME,\n") ;
		sql.append("         TVDR.RECEIVER,\n") ;
		sql.append("         TVO.ORDER_ORG_ID,\n") ;
		sql.append("         TMV.VIN,\n") ;
		sql.append("         TTDE.SENDCAR_ORDER_NUMBER,\n") ;
		sql.append("         TBA.AREA_NAME,\n") ;
		sql.append("         TTDE.FLATCAR_ASSIGN_DATE,\n") ;
		sql.append("         TMW.WAREHOUSE_NAME,\n") ;
		sql.append("         TVDR.DLVRY_REQ_NO,\n") ;
		sql.append("         TTDE.SHIP_METHOD_CODE,\n") ;
		sql.append("         TTDE.MOTORMAN,\n") ;
		sql.append("          TTDE.MOTORMAN_PHONE,\n") ;
		sql.append("          TMV.PRODUCT_DATE,\n") ;
		sql.append("          TMV.FACTORY_DATE,\n") ;
		sql.append("         TTDE.ARRIVE_DATE\n") ;
		
		
		sql.append("ORDER BY TTDE.FLATCAR_ASSIGN_DATE DESC") ;
		List<Map<String,Object>> list=dao.pageQuery(sql.toString(), null, dao.getFunName());
		return list;
	}


	public PageResult<Map<String, Object>> getDealerVhclDetail(String areaIds, String dealerIds, String vin, String engineNo, Long companyId, int curPage, int pageSize) throws Exception {
		List<Object> params = new LinkedList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT DECODE(TMD.DEALER_CODE, NULL, ' ', TMD.DEALER_CODE) DEALER_CODE,\n");
		sql.append("       DECODE(TMD.DEALER_SHORTNAME, NULL, ' ', TMD.DEALER_SHORTNAME) DEALER_NAME,\n");
		sql.append("       TVM.MATERIAL_CODE,\n");
		sql.append("       TVM.MATERIAL_NAME,\n");
		sql.append("       TVM.COLOR_NAME,\n");
		sql.append("       TMV.VIN,\n");
		sql.append("	   TMV.ENGINE_NO,\n");
		sql.append("       TMV.VEHICLE_ID,");
		sql.append("       TVM.VHCL_PRICE,\n");
		sql.append("       TMV.NODE_CODE,\n");
		sql.append("       TMV.LIFE_CYCLE,\n");
		sql.append("       TMV.LOCK_STATUS,TMV.hegezheng_code\n");
		sql.append("  FROM TM_VEHICLE TMV,\n");
		// if(!"".equals(groupCode)&&groupCode!=null){
		// sql.append(" TM_VHCL_MATERIAL_GROUP TVMG,\n" );
		// }
		sql.append("	   TM_VHCL_MATERIAL TVM,\n");
		sql.append("(SELECT DISTINCT TW.WAREHOUSE_ID, TW.WAREHOUSE_NAME, TW.WAREHOUSE_TYPE\n");
		sql.append("  FROM TM_WAREHOUSE TW, TM_BUSINESS_AREA TBA\n");
		sql.append(" WHERE TW.ENTITY_CODE = TBA.ERP_CODE\n");
		sql.append("   AND TW.STATUS = " + Constant.STATUS_ENABLE + "\n");
		sql.append("   AND TW.COMPANY_ID = " + companyId + "\n");
		// if (!"".equals(areaId) && areaId!=null) {
		// sql.append(" AND TBA.AREA_ID = "+areaId+"\n");
		// }
		sql.append("   AND TBA.AREA_ID IN (" + areaIds + ")) TEMP,");
		sql.append("       TM_DEALER TMD\n");
		sql.append(" WHERE TMD.DEALER_ID(+) = TMV.DEALER_ID\n");
		sql.append("   AND TMV.MATERIAL_ID = TVM.MATERIAL_ID\n");
		sql.append("   AND TMV.WAREHOUSE_ID = TEMP.WAREHOUSE_ID(+)\n");
		sql.append("   AND TMV.OEM_COMPANY_ID = " + companyId + "\n");
		if (null != vin && !"".equals(vin)) {
			sql.append(GetVinUtil.getMyVins(vin, "TMV"));
		}
		if (null != engineNo && !"".equals(engineNo)) {
			sql.append("AND TMV.ENGINE_NO like '%");
			sql.append(engineNo);
			sql.append("%'\n");
		}
		// sql.append(" AND TMV.DEALER_ID IN ("+dealerIds+")");
		// if(!"".equals(groupCode)&&groupCode!=null){
		// sql.append(Utility.getConSqlByParamForEqual(groupCode, params,
		// "TVMG", "GROUP_CODE"));
		// sql.append(" AND (TMV.SERIES_ID=TVMG.GROUP_ID OR
		// TMV.MODEL_ID=TVMG.GROUP_ID OR TMV.PACKAGE_ID=TVMG.GROUP_ID)");
		// }
		// if(!"".equals(dealerCode)&&dealerCode!=null){
		// sql.append(Utility.getConSqlByParamForEqual(dealerCode, params,
		// "TMD", "DEALER_CODE"));
		// }
		/*
		 * if (!"".equals(areaId)) { sql.append("AND TMV.AREA_ID =
		 * "+areaId+"\n"); } if (!"".equals(areaIds) && "".equals(areaId)) {
		 * sql.append("AND TMV.AREA_ID IN ("+areaIds+")\n"); }
		 */
		sql.append("ORDER BY  TVM.MATERIAL_CODE,DEALER_CODE,TMV.LIFE_CYCLE");
		PageResult<Map<String, Object>> ps = dao.pageQuery(sql.toString(), params, getFunName(), pageSize, curPage);
		return ps;
	}

	public List<Map<String, Object>> getLoadDealerVhclDetail(String areaIds, String dealerIds, String vin, String engineNo, Long companyId, int curPage, int pageSize) throws Exception {
		List<Object> params = new LinkedList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT DECODE(TMD.DEALER_CODE, NULL, ' ', TMD.DEALER_CODE) DEALER_CODE,\n");
		sql.append("       DECODE(TMD.DEALER_SHORTNAME, NULL, ' ', TMD.DEALER_SHORTNAME) DEALER_NAME,\n");
		sql.append("       TVM.MATERIAL_CODE,\n");
		sql.append("       TVM.MATERIAL_NAME,\n");
		sql.append("       TVM.COLOR_NAME,\n");
		sql.append("       TMV.VIN,\n");
		sql.append("	   TMV.ENGINE_NO,\n");
		sql.append("       TMV.VEHICLE_ID,");
		sql.append("       TVM.VHCL_PRICE,\n");
		sql.append("       TMV.NODE_CODE,\n");
		sql.append("       TMV.HEGEZHENG_CODE,\n");
		sql.append("       TC1.CODE_DESC LIFE_CYCLE,\n");
		sql.append("       TC2.CODE_DESC LOCK_STATUS\n");
		sql.append("  FROM TM_VEHICLE TMV,TC_CODE TC1,TC_CODE TC2,\n");
		// if(!"".equals(groupCode)&&groupCode!=null){
		// sql.append(" TM_VHCL_MATERIAL_GROUP TVMG,\n" );
		// }
		sql.append("	   TM_VHCL_MATERIAL TVM,\n");
		sql.append("(SELECT DISTINCT TW.WAREHOUSE_ID, TW.WAREHOUSE_NAME, TW.WAREHOUSE_TYPE\n");
		sql.append("  FROM TM_WAREHOUSE TW, TM_BUSINESS_AREA TBA\n");
		sql.append(" WHERE TW.ENTITY_CODE = TBA.ERP_CODE\n");
		sql.append("   AND TW.STATUS = " + Constant.STATUS_ENABLE + "\n");
		sql.append("   AND TW.COMPANY_ID = " + companyId + "\n");
		// if (!"".equals(areaId) && areaId!=null) {
		// sql.append(" AND TBA.AREA_ID = "+areaId+"\n");
		// }
		sql.append("   AND TBA.AREA_ID IN (" + areaIds + ")) TEMP,");
		sql.append("       TM_DEALER TMD\n");
		sql.append(" WHERE TMD.DEALER_ID(+) = TMV.DEALER_ID\n");
		sql.append("   AND TMV.MATERIAL_ID = TVM.MATERIAL_ID\n");
		sql.append("   AND TMV.WAREHOUSE_ID = TEMP.WAREHOUSE_ID(+)\n");
		sql.append("   AND TMV.OEM_COMPANY_ID = " + companyId + "\n");
		if (null != vin && !"".equals(vin)) {
			sql.append(GetVinUtil.getMyVins(vin, "TMV"));
		}
		if (null != engineNo && !"".equals(engineNo)) {
			sql.append("AND TMV.ENGINE_NO like '%");
			sql.append(engineNo);
			sql.append("%'\n");
		}
		// sql.append(" AND TMV.DEALER_ID IN ("+dealerIds+")");
		// if(!"".equals(groupCode)&&groupCode!=null){
		// sql.append(Utility.getConSqlByParamForEqual(groupCode, params,
		// "TVMG", "GROUP_CODE"));
		// sql.append(" AND (TMV.SERIES_ID=TVMG.GROUP_ID OR
		// TMV.MODEL_ID=TVMG.GROUP_ID OR TMV.PACKAGE_ID=TVMG.GROUP_ID)");
		// }
		// if(!"".equals(dealerCode)&&dealerCode!=null){
		// sql.append(Utility.getConSqlByParamForEqual(dealerCode, params,
		// "TMD", "DEALER_CODE"));
		// }
		/*
		 * if (!"".equals(areaId)) { sql.append("AND TMV.AREA_ID =
		 * "+areaId+"\n"); } if (!"".equals(areaIds) && "".equals(areaId)) {
		 * sql.append("AND TMV.AREA_ID IN ("+areaIds+")\n"); }
		 */
		sql.append("   AND TC1.CODE_ID=TMV.LIFE_CYCLE AND TC2.CODE_ID=TMV.LOCK_STATUS \n");
		sql.append("ORDER BY  TVM.MATERIAL_CODE,DEALER_CODE,TMV.LIFE_CYCLE");
		List<Map<String, Object>> ps = dao.pageQuery(sql.toString(), params, getFunName());
		return ps;
	}

	/***************************************************************************
	 * 查询入库车辆详细信息
	 * 
	 * @param :
	 * @return
	 * @throws Exception
	 */
	public static Map<String, Object> getVehicleInfo(String vehicle_id) {
		StringBuffer sql = new StringBuffer("");
		sql.append("SELECT TMV.VEHICLE_ID,\n");
		sql.append("       TMV.VIN, --VIN\n");
		sql.append("       TO_CHAR(TTDE.FLATCAR_ASSIGN_DATE, 'YYYY-MM-DD') DELIVERY_DATE, --发车日期\n");
		sql.append("       G.GROUP_NAME AS MODEL_NAME, --车型\n");
		sql.append("       TMV.ENGINE_NO, --发动机号\n");
		sql.append("       TMV.COLOR, --颜色\n");
		sql.append("       TMW.WAREHOUSE_NAME\n");
		sql.append("  FROM TT_VS_DLVRY_ERP_DTL    TTDED, --ERP发运子表\n");
		sql.append("       TT_VS_DLVRY_ERP        TTDE, --ERP发运主表\n");
		sql.append("       TM_VEHICLE             TMV,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP G,\n");
		sql.append("       TM_WAREHOUSE           TMW\n");
		sql.append(" WHERE 1 = 1\n");
		sql.append("   AND TTDED.SENDCAR_HEADER_ID = TTDE.SENDCAR_HEADER_ID\n");
		sql.append("   AND TTDED.VEHICLE_ID = TMV.VEHICLE_ID\n");
		sql.append("   AND TMV.MODEL_ID = G.GROUP_ID\n");
		sql.append("   AND TMV.WAREHOUSE_ID = TMW.WAREHOUSE_ID\n");

		sql.append("   AND TTDED.VEHICLE_ID = " + vehicle_id + "\n");

		return dao.pageQueryMap(sql.toString(), null, "com.infodms.dms.dao.sales.storageManage.CheckVehicleDAO.getVehicleInfo");
	}

	/*
	 * 通过车辆ID查询车辆接收方
	 */
	public List<Map<String, Object>> getReceiver(String vehcleId) {
		StringBuffer sql = new StringBuffer("");

		sql.append("SELECT TMV.DEALER_ID,\n");
		sql.append("       TTO.ORDER_ORG_ID, --订货方\n");
		sql.append("       TTO.BILLING_ORG_ID, --付款方\n");
		sql.append("	   TTD.RECEIVER --收货方\n");
		sql.append("  FROM TM_VEHICLE      TMV,\n");
		sql.append("       TT_VS_DLVRY_MCH TTM,\n");
		sql.append("       TT_VS_DLVRY     TTD,\n");
		sql.append("       TT_VS_DLVRY_DTL TTDD,\n");
		sql.append("       TT_VS_ORDER     TTO\n");
		sql.append(" WHERE TTM.VEHICLE_ID = " + vehcleId + "\n");
		sql.append("   AND TMV.VEHICLE_ID = TTM.VEHICLE_ID\n");
		sql.append("   AND TTM.DELIVERY_DETAIL_ID = TTDD.DETAIL_ID\n");
		sql.append("   AND TTD.DELIVERY_ID = TTDD.DELIVERY_ID\n");
		sql.append("   AND TTD.ORDER_ID = TTO.ORDER_ID\n");

		return dao.pageQuery(sql.toString(), null, getFunName());
	}

	/***************************************************************************
	 * 根据vehicleId查询车辆订货方及付款方，判断系统是否自动执行调拨
	 * 
	 * @param :
	 * @return
	 * @throws Exception
	 */
	public static Map<String, Object> getVehicleOrder_Buy_ID(String vehicle_id) {
		StringBuffer sql = new StringBuffer("");
		sql.append("SELECT TMV.DEALER_ID,\n");
		sql.append("       TTO.ORDER_ORG_ID, --订货方\n");
		sql.append("       TTO.BILLING_ORG_ID, --付款方\n");
		sql.append("	   TTD.RECEIVER --收货方\n");
		sql.append("  FROM TM_VEHICLE      TMV,\n");
		sql.append("       TT_VS_DLVRY_MCH TTM,\n");
		sql.append("       TT_VS_DLVRY     TTD,\n");
		sql.append("       TT_VS_DLVRY_DTL TTDD,\n");
		sql.append("       TT_VS_ORDER     TTO\n");
		sql.append(" WHERE TTM.VEHICLE_ID = " + vehicle_id + "\n");
		sql.append("   AND TMV.VEHICLE_ID = TTM.VEHICLE_ID\n");
		sql.append("   AND TTM.DELIVERY_DETAIL_ID = TTDD.DETAIL_ID\n");
		sql.append("   AND TTD.DELIVERY_ID = TTDD.DELIVERY_ID\n");
		sql.append("   AND TTD.ORDER_ID = TTO.ORDER_ID\n");
		sql.append("   AND TTD.BILLING_SIDE = TMV.DEALER_ID\n");

		return dao.pageQueryMap(sql.toString(), null, dao.getFunName());
	}

	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}

	public static Map<String, Object> getParentDealerId(String dealerId) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT TD.PARENT_DEALER_D \n");

		sql.append("FROM TM_DEALER TD \n");

		sql.append("WHERE TD.DEALER_ID='" + dealerId.trim() + "'\n");

		Map<String, Object> rs = dao.pageQueryMap(sql.toString(), null, dao.getFunName());

		return rs;
	}

	public static List<Map<String, Object>> warehouseQuery(String dealerIds) {
		StringBuffer sql = new StringBuffer("");
		sql.append("SELECT TDW.WAREHOUSE_NAME, TDW.WAREHOUSE_ID\n");
		sql.append("  FROM TM_DEALER_WAREHOUSE TDW\n");
		sql.append(" WHERE TDW.STATUS = " + Constant.STATUS_ENABLE + "\n");
		sql.append("   AND TDW.DEALER_COMANY_ID IN\n");
		sql.append("       (SELECT TD.COMPANY_ID\n");
		sql.append("          FROM TM_DEALER TD\n");
		sql.append("         WHERE TD.DEALER_ID IN (" + dealerIds.trim() + "))\n");

		List<Map<String, Object>> rs = dao.pageQuery(sql.toString(), null, dao.getFunName());

		return rs;
	}

	public List<Map<String, Object>> getDelLevel(String dealerId) {
		StringBuffer sql = new StringBuffer("");

		sql.append("select tm.dealer_level from tm_dealer tm where tm.dealer_id in(" + dealerId + ")\n");

		return dao.pageQuery(sql.toString(), null, dao.getFunName());
	}

	// 特殊经销商判断
	public static List<Map<String, Object>> getSpecialDlr(String dealerIds) {
		StringBuffer sql = new StringBuffer("");

		sql.append("SELECT *\n");
		sql.append("  FROM TM_DEALER TD\n");
		sql.append(" WHERE TD.DEALER_TYPE = " + Constant.DEALER_TYPE_DVS + "\n");
		sql.append("   AND TD.DEALER_CLASS = " + Constant.DEALER_CLASS_TYPE_06 + "\n");
		sql.append("	AND TD.DEALER_ID IN (" + dealerIds + ")");

		return dao.pageQuery(sql.toString(), null, dao.getFunName());
	}

	// 结算中心判断
	public static List<Map<String, Object>> getDeal__(String dealerIds) {
		StringBuffer sql = new StringBuffer("");

		sql.append("SELECT *\n");
		sql.append("  FROM TM_DEALER TD\n");
		sql.append(" WHERE TD.DEALER_TYPE = " + Constant.DEALER_TYPE_JSZX + "\n");
		sql.append("	AND TD.DEALER_ID IN (" + dealerIds + ")");

		return dao.pageQuery(sql.toString(), null, dao.getFunName());
	}

	public List<Map<String, Object>> getOwnWarehouse(String ownid, String dealerId) {
		if (Utility.testString(ownid)) {
			StringBuffer sql = new StringBuffer("\n");
			sql.append("SELECT A.WAREHOUSE_ID, A.WAREHOUSE_NAME\n");
			sql.append("FROM TM_DEALER_WAREHOUSE A\n");
			if (ownid.equals("01")) {
				sql.append("WHERE 1 = 1 AND A.DEALER_ID IN ( ").append(dealerId).append(" ) ");
				sql.append(" AND A.WAREHOUSE_TYPE = ").append(Constant.DEALER_WAREHOUSE_TYPE_01).append("\n");
			} else if (ownid.equals("02")) {
				sql.append("WHERE 1 = 1 AND A.MANAGE_DEALER_ID in ( ").append(dealerId).append(" ) ");
				sql.append(" AND A.WAREHOUSE_TYPE = ").append(Constant.DEALER_WAREHOUSE_TYPE_02).append("\n");
			} else if (ownid.equals("03")) {
				sql.append("WHERE 1 = 1 AND A.DEALER_ID IN ( ").append(dealerId).append(" ) ");
				sql.append(" AND A.WAREHOUSE_TYPE = ").append(Constant.DEALER_WAREHOUSE_TYPE_02).append("\n");
			}
			List<Map<String, Object>> list = dao.pageQuery(sql.toString(), null, dao.getFunName());
			return list;
		} else {
			return null;
		}
	}

	/**
	 * 方法重载，查询当前登陆经销商下级经销商的自有库或代交库
	 * 
	 * @param ownid
	 * @param dealerId
	 * @return
	 */
	public List<Map<String, Object>> getOwnWarehouse(Long ownid, String dealerId) {
		if (null != ownid || !"".equals(ownid.toString())) {
			StringBuffer sql = new StringBuffer("\n");

			sql.append("SELECT TMDW.WAREHOUSE_ID, TMDW.WAREHOUSE_NAME\n");
			sql.append("  FROM TM_DEALER_WAREHOUSE TMDW, TM_DEALER TMD\n");
			if (ownid == 1) {
				sql.append(" WHERE TMDW.DEALER_ID = TMD.DEALER_ID --下级经销商自有库\n");
				sql.append("   AND TMDW.WAREHOUSE_TYPE = " + Constant.DEALER_WAREHOUSE_TYPE_01 + "\n");
			} else if (ownid == 2) {
				sql.append("WHERE TMDW.MANAGE_DEALER_ID = TMD.DEALER_ID  --下级经销商代管库\n");
				sql.append("AND TMDW.WAREHOUSE_TYPE = " + Constant.DEALER_WAREHOUSE_TYPE_02 + "\n");
			}
			sql.append("   AND TMDW.STATUS = " + Constant.STATUS_ENABLE + "\n");
			sql.append("   AND TMD.PARENT_DEALER_D in (" + dealerId + ")\n");

			List<Map<String, Object>> list = dao.pageQuery(sql.toString(), null, dao.getFunName());
			return list;
		} else {
			return null;
		}
	}

	public List<Map<String, Object>> getDlvryEROInfo(String sendcardId) {
		StringBuffer sql = new StringBuffer("");

		sql.append("SELECT TVDED.VIN, --车辆vin号\n");
		sql.append("       TVDED.ENGINER_ID, --引擎id\n");
		sql.append("       TVM.MATERIAL_NAME, --物料名称\n");
		sql.append("       TVM.MATERIAL_CODE --物料代码\n");
		sql.append("  FROM TT_VS_DLVRY_ERP_DTL TVDED, TM_VEHICLE TV, TM_VHCL_MATERIAL TVM\n");
		sql.append(" WHERE TVDED.VIN = TV.VIN\n");
		sql.append("   AND TV.MATERIAL_ID = TVM.MATERIAL_ID\n");
		sql.append("   AND TVDED.SENDCAR_HEADER_ID =" + sendcardId + "\n");

		return dao.pageQuery(sql.toString(), null, getFunName());
	}

	// 通过发运表汇总对应明细表中的发运数量和验收数量
	public List<Map<String, Object>> getAmount(Long dlvId) {
		StringBuffer sql = new StringBuffer("");

		sql.append("SELECT TVDD.DELIVERY_ID,\n");
		sql.append("       SUM(TVDD.DELIVERY_AMOUNT) DELIVERY_AMOUNT,\n");
		sql.append("       SUM(TVDD.INSPECTION_AMOUNT) INSPECTION_AMOUNT\n");
		sql.append("  FROM TT_VS_DLVRY_DTL TVDD\n");
		sql.append(" WHERE TVDD.DELIVERY_ID = '" + dlvId + "'\n");
		sql.append(" GROUP BY TVDD.DELIVERY_ID\n");

		return dao.pageQuery(sql.toString(), null, getFunName());
	}

	public static Map<String, Object> getChkVehlInfo(String vehlId) {
		List<Object> params = new ArrayList<Object>();

		StringBuffer sql = new StringBuffer("\n");

		sql.append("SELECT DECODE(TVDR.CALL_LEAVEL,\n");
		sql.append("              ?,\n");
		params.add(Constant.DEALER_LEVEL_02);

		sql.append("              TVDR.ORDER_DEALER_ID,\n");
		sql.append("              TVO.ORDER_ORG_ID) ORDER_ORG_ID, -- 采购方\n");
		sql.append("       TVO.BILLING_ORG_ID, -- 开票方\n");
		sql.append("       TVD.RECEIVER, --收货方\n");
		sql.append("       TMV.DEALER_ID,\n");
		sql.append("       TMV.VEHICLE_ID,\n");
		sql.append("       TVDM.MATCH_ID\n");
		sql.append("  FROM TT_VS_ORDER     TVO,\n");
		sql.append("       TT_VS_DLVRY_REQ TVDR,\n");
		sql.append("       TT_VS_DLVRY     TVD,\n");
		sql.append("       TT_VS_DLVRY_DTL TVDD,\n");
		sql.append("       TT_VS_DLVRY_MCH TVDM,\n");
		sql.append("       TM_VEHICLE      TMV\n");
		sql.append(" WHERE 1 = 1\n");
		sql.append("   AND TVO.ORDER_ID = TVDR.ORDER_ID\n");
		sql.append("   AND TVDR.REQ_ID = TVD.REQ_ID\n");
		sql.append("   AND TVD.DELIVERY_ID = TVDD.DELIVERY_ID\n");
		sql.append("   AND TVDD.DETAIL_ID = TVDM.DELIVERY_DETAIL_ID\n");
		sql.append("   AND TVDM.VEHICLE_ID = TMV.VEHICLE_ID\n");
		sql.append("   AND TVD.BILLING_SIDE = TMV.DEALER_ID -- 限制控制\n");
		sql.append("   AND TVDM.IF_INSPECTION = 0 -- 未验收的配车信息\n");
		sql.append("   AND TMV.VEHICLE_ID = ?\n");
		params.add(vehlId);

		return dao.pageQueryMap(sql.toString(), params, dao.getFunName());
	}

	public static Map<String, Object> getChkVehlInfo_SUZUKI(String vehlId) {
		List<Object> params = new ArrayList<Object>();

		StringBuffer sql = new StringBuffer("\n");

		sql.append("SELECT TVDR.RECEIVER, --收货方\n");
		sql.append("       TMV.VEHICLE_ID\n");
		sql.append("  FROM TT_VS_DLVRY_REQ     TVDR,\n");
		sql.append("       TT_VS_DLVRY_ERP_DTL TTDED, --ERP发运子表\n");
		sql.append("       TT_VS_DLVRY_ERP     TTDE, --ERP发运主表\n");
		sql.append("       TM_VEHICLE          TMV\n");
		sql.append(" WHERE 1 = 1\n");
		sql.append("   AND TVDR.REQ_ID = TTDED.REQ_ID\n");
		sql.append("   AND TTDE.SENDCAR_HEADER_ID = TTDED.SENDCAR_HEADER_ID\n");
		sql.append("   AND TTDED.VEHICLE_ID = TMV.VEHICLE_ID\n");
		sql.append("   AND TTDED.VEHICLE_ID = ?\n");

		params.add(vehlId);

		return dao.pageQueryMap(sql.toString(), params, dao.getFunName());
	}

	public List<Map<String, Object>> chkMaterial(String groupIds, String areaIds) {
		StringBuffer sql = new StringBuffer("\n");

		sql.append("select * from tm_area_group tag where tag.material_group_id in (").append(groupIds).append(") and tag.area_id in (").append(areaIds).append(")\n");

		return super.pageQuery(sql.toString(), null, super.getFunName());
	}

	public List<Map<String, Object>> getVin(String vehicleId) {
		StringBuffer sql = new StringBuffer("\n");

		sql.append("select tmv.vin from tm_vehicle tmv where tmv.vehicle_id in (").append(vehicleId).append(")");

		return super.pageQuery(sql.toString(), null, super.getFunName());
	}

	public TmBusinessAreaPO chkProductBase(String areaId) {
		TmBusinessAreaPO tba = new TmBusinessAreaPO();
		tba.setAreaId(Long.parseLong(areaId));

		TmBusinessAreaPO newTba = new TmBusinessAreaPO();
		newTba = (TmBusinessAreaPO) super.select(tba).get(0);

		return newTba;
	}

	public static Map<String, Object> getDealerId(String vehicle_id) {
		StringBuffer sql = new StringBuffer();
		sql.append("select tv.dealer_id\n");
		sql.append("  from tm_vehicle tv\n");
		if (vehicle_id != null && !"".equals(vehicle_id)) {
			sql.append(" where tv.vehicle_id = ");
			sql.append(vehicle_id);
			sql.append("\n");
		}
		Map<String, Object> rs = dao.pageQueryMap(sql.toString(), null, dao.getFunName());
		return rs;
	}

	// add by WHX,2012.09.18
	// ========================================================Start
	public Long tempVehiDeInsert(Map<String, String> map) {
		String vin = map.get("vin");
		String inDlr = map.get("inDlr");
		String outDlr = map.get("outDlr");
		String type = map.get("type");
		String remark = map.get("remark");
		String userId = map.get("userId");

		StringBuffer sql = new StringBuffer("\n");

		Long id = Long.parseLong(SequenceManager.getSequence(""));

		sql.append("insert into TEMP_VEHICLE_DE values (").append(vin).append(", ").append(inDlr).append(", ").append(outDlr).append(", ").append(type).append(", '").append(remark).append("', ").append(userId).append(", sysdate, null, null, ").append(id).append(")\n");

		super.insert(sql.toString());

		return id;
	}

	public void vehicleShiftLibrary(String vehicleIds, String warehouseId) {
		StringBuffer sql = new StringBuffer("\n");

		sql.append("UPDATE TM_VEHICLE TM SET TM.VEHICLE_AREA = '" + warehouseId + "' WHERE TM.VEHICLE_ID IN (" + vehicleIds + ")");
		super.update(sql.toString(), null);
	}

	public void tempVehiDeDelete(Long id) {
		StringBuffer sql = new StringBuffer("\n");

		sql.append("delete TEMP_VEHICLE_DE tvd where tvd.id = ").append(id).append("\n");

		super.delete(sql.toString(), null);
	}

	public Map<String, String> getGroupByVechileId(String vechile_id) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT TVG3.GROUP_CODE FROM TM_VHCL_MATERIAL_GROUP TVG1 ,TM_VHCL_MATERIAL_GROUP TVG2 ,TM_VHCL_MATERIAL_GROUP TVG3 ,TM_VHCL_MATERIAL_GROUP_R TVGR, TM_VEHICLE TV,TM_VHCL_MATERIAL TVM\n");
		sql.append("WHERE TVG1.PARENT_GROUP_ID=TVG2.GROUP_ID AND TVG2.PARENT_GROUP_ID=TVG3.GROUP_ID AND TVGR.MATERIAL_ID=TVM.MATERIAL_ID AND TVGR.GROUP_ID=TVG1.GROUP_ID AND TVM.MATERIAL_ID=TV.MATERIAL_ID\n");
		sql.append("AND  TV.VEHICLE_ID=" + vechile_id);
		return dao.pageQueryMap(sql.toString(), null, dao.getFunName());

	}
	public static PageResult<Map<String, Object>> getCheckQuery(String dealerCode,String orgId,String reqNo,String dutyType,String dealer_Id, String deliverystartDate, String deliveryendDate, String checkstartDate, String checkendDate, String vin, String dlvNo, int pageSize, int curPage) {

		StringBuilder sql= new StringBuilder();
		sql.append("SELECT DISTINCT TVDE.SENDCAR_HEADER_ID,\n" );
		sql.append("                TVDE.SENDCAR_ORDER_NUMBER,\n" );
		sql.append("                TD.DEALER_SHORTNAME,\n" );
		sql.append("                TVDE.SHIP_METHOD_CODE,\n" );
		sql.append("                TVDR.DLVRY_REQ_NO, \n");
		sql.append("                TVDE.FLATCAR_ASSIGN_DATE,\n" );
		sql.append("                TV.Vehicle_Id,\n" );
		sql.append("                TV.VIN\n" );
		sql.append("  FROM TT_VS_DLVRY_ERP     TVDE,\n" );
		sql.append("       TT_VS_DLVRY_ERP_DTL TVDED,\n" );
		sql.append("       TT_VS_DLVRY_REQ     TVDR,\n" );
		sql.append("       TT_VS_DLVRY_REQ_DTL TVDRD,\n" );
		sql.append("       TM_DEALER           TD,\n" );
		sql.append("       TM_VEHICLE          TV\n" );
		sql.append(" WHERE TVDE.SENDCAR_HEADER_ID = TVDED.SENDCAR_HEADER_ID\n" );
		sql.append("   AND TVDED.REQ_ID = TVDR.REQ_ID\n" );
		sql.append("   AND TVDR.REQ_ID = TVDRD.REQ_ID\n" );
		sql.append("   AND TD.DEALER_ID = TVDR.RECEIVER\n" );
		sql.append("   AND TV.VEHICLE_ID = TVDED.VEHICLE_ID\n");
		if(Constant.DUTY_TYPE_DEALER.toString().equals(dutyType)){
			sql.append("   AND TVDR.RECEIVER  in ( "+dealer_Id+")\n");
		}else if(Constant.DUTY_TYPE_SMALLREGION.toString().equals(dutyType)){
			sql.append("AND td.DEALER_ID IN");
			sql.append("  (SELECT VW.DEALER_ID FROM vw_org_dealer VW\n");
			sql.append("WHERE VW.PQ_ORG_ID="+orgId+")");
		}else if(Constant.DUTY_TYPE_LARGEREGION.toString().equals(dutyType)){
			sql.append("AND td.DEALER_ID IN");
			sql.append("  (SELECT VW.DEALER_ID FROM vw_org_dealer VW\n");
			sql.append("WHERE VW.ROOT_ORG_ID="+orgId+")");
		}
		
		
		// 发车日期
		if (null != deliverystartDate && !"".equals(deliverystartDate)) {
			sql.append("   AND TVDE.FLATCAR_ASSIGN_DATE >= TO_DATE('" + deliverystartDate + "','yyyy-MM-dd')\n");
		}
		if (null != deliveryendDate && !"".equals(deliveryendDate)) {
			sql.append("   AND TVDE.FLATCAR_ASSIGN_DATE <= (TO_DATE('" + deliveryendDate + "','yyyy-MM-dd')+1)\n");
		}
		
		if (null != dlvNo && !"".equals(dlvNo)) {
			sql.append("   AND TVDE.SENDCAR_ORDER_NUMBER like UPPER('%" + dlvNo + "%') \n");
		}
		if (null != reqNo && !"".equals(reqNo)) {
			sql.append("   AND TVDR.DLVRY_REQ_NO like UPPER('%" + reqNo + "%') \n");
		}
		if (null != vin && !"".equals(vin)) {
			sql.append("   AND TV.VIN like UPPER('%" + vin + "%') \n");
		}
		if(dealerCode!=null&&!"".equals(dealerCode)){
			String dealerCodes=CommonUtils.getSplitStringForIn(dealerCode);
			sql.append(" AND TD.DEALER_CODE IN("+dealerCodes+")");
		}
		 sql.append(" ORDER BY TVDE.FLATCAR_ASSIGN_DATE DESC\n");

		return dao.pageQuery(sql.toString(), null, "com.infodms.dms.dao.sales.storageManage.CheckVehicleQueryDAO.getCheckQueryDLR", pageSize, curPage);
	}
	/***************************************************************************
	 * 查询可验收入库的车辆列表
	 * 
	 * @param :
	 * @return
	 * @throws Exception
	 */

	public static Map<String,Object> getCheckDetailInfo(String vechile_id){

		StringBuilder sql= new StringBuilder();
		sql.append("SELECT DISTINCT TVDE.SENDCAR_HEADER_ID,\n" );
		sql.append("                TVDE.SENDCAR_ORDER_NUMBER, --交接单号\n" );
		sql.append("                TVDE.FLATCAR_ASSIGN_DATE, --发出日期\n" );
		sql.append("                TC.COMPANY_SHORTNAME, --出货地点\n" );
		sql.append("                TW.WAREHOUSE_NAME, --出货仓库\n" );
		sql.append("                TD.DEALER_SHORTNAME, --收货单位\n" );
		sql.append("                TD.LINK_MAN, --收货单位联系人\n" );
		sql.append("                TD.ADDRESS, --收货单位地址\n" );
		sql.append("                TD.PHONE, --收货单位电话\n" );
		sql.append("                TVDE.SHIP_METHOD_CODE, --运输公司\n" );
		sql.append("                TVDE.MOTORMAN, --运输公司联系人\n" );
		sql.append("                TVDE.MOTORMAN_PHONE, --运输公司联系电话\n" );
		sql.append("                TCD.CODE_DESC             DELIVRY_TYPE, --发运发生\n" );
		sql.append("                TVDE.SENDCAR_AMOUNT, --总数量\n" );
		sql.append("                TVDR.DLVRY_REQ_NO, --发运申请单号\n" );
		sql.append("                TVDE.ARRIVE_DATE, --预计天数\n" );
		sql.append("                TVO.ORDER_NO, --订单号\n" );
		sql.append("                TVM.MATERIAL_NAME, --物料名称\n" );
		sql.append("                TV.PRODUCT_DATE, --生产日期\n" );
		sql.append("                TV.FACTORY_DATE, --出厂日期\n" );
		sql.append("                TCD1.CODE_DESC            IS_RECEIVE, --是否接收\n" );
		sql.append("                TV.VIN\n" );
		sql.append("  FROM TT_VS_DLVRY_ERP     TVDE,\n" );
		sql.append("       TT_VS_DLVRY_ERP_DTL TVDED,\n" );
		sql.append("       TT_VS_DLVRY_REQ     TVDR,\n" );
		sql.append("       TT_VS_DLVRY_REQ_DTL TVDRD,\n" );
		sql.append("       TM_DEALER           TD,\n" );
		sql.append("       TM_VEHICLE          TV,\n" );
		sql.append("       TM_VHCL_MATERIAL    TVM,\n" );
		sql.append("       TM_WAREHOUSE        TW,\n" );
		sql.append("       TM_COMPANY          TC,\n" );
		sql.append("       TC_CODE             TCD,\n" );
		sql.append("       TT_VS_ORDER         TVO,\n" );
		sql.append("       TC_CODE             TCD1\n" );
		sql.append(" WHERE TVDE.SENDCAR_HEADER_ID = TVDED.SENDCAR_HEADER_ID\n" );
		sql.append("   AND TVDED.REQ_ID = TVDR.REQ_ID\n" );
		sql.append("   AND TVDR.REQ_ID = TVDRD.REQ_ID\n" );
		sql.append("   AND TD.DEALER_ID = TVDR.RECEIVER\n" );
		sql.append("   AND TV.VEHICLE_ID = TVDED.VEHICLE_ID\n" );
		sql.append("   AND TW.WAREHOUSE_ID = TV.WAREHOUSE_ID\n" );
		sql.append("   AND TVM.MATERIAL_ID = TV.MATERIAL_ID\n" );
		sql.append("   AND TCD.CODE_ID = TVDR.DELIVERY_TYPE\n" );
		sql.append("   AND TVO.ORDER_ID = TVDR.ORDER_ID\n" );
		sql.append("   AND TCD1.CODE_ID = TVDED.IS_RECEIVE\n" );
		sql.append("   AND TC.COMPANY_ID = 2010010100070674\n" );
		sql.append("   AND TV.VEHICLE_ID = "+vechile_id+"\n" );
		sql.append(" ORDER BY TVDE.FLATCAR_ASSIGN_DATE DESC");

		Map<String ,Object> m=dao.pageQueryMap(sql.toString(), null, dao.getFunName());
		return m;
	}
}
