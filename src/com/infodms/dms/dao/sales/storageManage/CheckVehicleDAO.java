package com.infodms.dms.dao.sales.storageManage;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TmBusinessAreaPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.businessUtil.GetVinUtil;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.po3.POFactory;
import com.infoservice.po3.POFactoryBuilder;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

public class CheckVehicleDAO extends BaseDao {
	public static Logger logger = Logger.getLogger(CheckVehicleDAO.class);
	private static POFactory factory = POFactoryBuilder.getInstance();
	private static final CheckVehicleDAO dao = new CheckVehicleDAO();
	
	public static final CheckVehicleDAO getInstance() {
		return dao;
	}
	
	/***
	 * 查询可验收入库的车辆列表
	 * 
	 * @param :
	 * @return
	 * @throws Exception
	 */
	public static PageResult<Map<String, Object>> getCheckList(Map<String, Object> map, int pageSize, int curPage)
	{
		String vin =CommonUtils.checkNull((String)map.get("vin")); //
		String dlvNo = CommonUtils.checkNull((String)map.get("dlvNo")); // 
		String orderType = CommonUtils.checkNull((String)map.get("orderType")); //
		String poseId = CommonUtils.checkNull((String)map.get("poseId")); //
		String dealer_Id = CommonUtils.checkNull((String)map.get("dealerIds")); //
		String orderNo = CommonUtils.checkNull((String)map.get("orderNo"));
		String hgzNo = CommonUtils.checkNull((String)map.get("hgzNo"));
		List par=new ArrayList();
		StringBuffer sbSql=new StringBuffer();
		/*sbSql.append("SELECT F.DETAIL_ID,F.ASS_DETAIL_ID,\n");
		sbSql.append("       B.VEHICLE_ID,\n");
		sbSql.append("       A.BILL_ID,\n");
		sbSql.append("       A.BILL_NO,\n");
		sbSql.append("       H.ORDER_NO,\n");
		sbSql.append("       H.ORDER_TYPE,\n");
		sbSql.append("       C.MODEL_NAME,\n");
		sbSql.append("       C.MATERIAL_CODE,\n");
		sbSql.append("       C.MATERIAL_NAME,\n");
		sbSql.append("       B.VIN,\n");
		sbSql.append("       B.HGZ_NO,\n");
		sbSql.append("       TO_CHAR(A.BILL_CRT_DATE, 'yyyy-MM-dd') DELIVERY_DATE,\n");
		sbSql.append("       D.AREA_NAME\n");
		sbSql.append("  FROM TT_SALES_WAYBILL      A,\n");
		sbSql.append("       TM_VEHICLE            B,\n");
		sbSql.append("       VW_MATERIAL_GROUP_MAT C,\n");
		sbSql.append("       TM_BUSINESS_AREA      D,\n");
		sbSql.append("       TT_SALES_ALLOCA_DE    F,\n");
		sbSql.append("       TT_SALES_ASS_DETAIL   G,\n");
		sbSql.append("       TT_SALES_ASSIGN       H\n");
		sbSql.append(" WHERE A.BILL_ID = F.BILL_ID\n");
		sbSql.append("   AND F.VEHICLE_ID = B.VEHICLE_ID\n");
		sbSql.append("   AND B.MATERIAL_ID = C.MATERIAL_ID\n");
		sbSql.append("   AND B.YIELDLY = D.AREA_ID\n");
		sbSql.append("   AND F.ASS_DETAIL_ID = G.ASS_DETAIL_ID\n");
		sbSql.append("   AND G.ASS_ID = H.ASS_ID\n");
//		sbSql.append("   AND B.LIFE_CYCLE = ?\n");
		sbSql.append("   AND A.SEND_DEALER_ID = ?\n"); 
		sbSql.append("	AND B.LIFE_CYCLE = "+Constant.VEHICLE_LIFE_05+"\n");*/ 

		sbSql.append("SELECT\n");
		sbSql.append("       E.DTL_ID DETAIL_ID,\n");
		sbSql.append("       B.VEHICLE_ID,\n");
		sbSql.append("       A.BILL_ID,\n");
		sbSql.append("       A.BILL_NO,\n");
		sbSql.append("       E.ORDER_NO,\n");
		sbSql.append("       C.MODEL_NAME,\n");
		sbSql.append("       C.MATERIAL_CODE,\n");
		sbSql.append("       C.MATERIAL_NAME,\n");
		sbSql.append("       F.ORDER_TYPE,\n");
		sbSql.append("       B.VIN,\n");
		sbSql.append("       B.HGZ_NO,\n");
		sbSql.append("       TO_CHAR(A.BILL_CRT_DATE, 'yyyy-MM-dd') DELIVERY_DATE,\n");
		sbSql.append("       D.AREA_NAME,\n");
		sbSql.append("       (SELECT WAREHOUSE_NAME FROM tm_warehouse TW WHERE TW.warehouse_id=B.warehouse_id AND TW.STATUS=10011001) WAREHOUSE_NAME,\n");
		sbSql.append("       F.DEALER_NAME AS SUBMIT_ORDER_DEALER \n");
		sbSql.append("  FROM TT_SALES_WAYBILL      A,\n");
		sbSql.append("       TM_VEHICLE            B,\n");
		sbSql.append("       VW_MATERIAL_GROUP_MAT C,\n");
		sbSql.append("       TM_BUSINESS_AREA      D,\n");
		sbSql.append("       tt_sales_way_bill_dtl  E,\n");
		sbSql.append("       TT_VS_ORDER            F\n");
		sbSql.append(" WHERE A.BILL_ID = E.BILL_ID\n");
		sbSql.append("   AND E.VEHICLE_ID = B.VEHICLE_ID\n");
		sbSql.append("   AND B.MATERIAL_ID = C.MATERIAL_ID\n");
		sbSql.append("   AND B.YIELDLY = D.AREA_ID\n");
		sbSql.append("   AND F.Order_No=E.ORDER_NO\n");
		sbSql.append("   AND A.SEND_DEALER_ID = ?\n");
		sbSql.append("	AND B.LIFE_CYCLE = "+Constant.VEHICLE_LIFE_05+"\n");

//		par.add(Constant.VEHICLE_LIFE_05);
		par.add(Long.valueOf(dealer_Id));
		sbSql.append(MaterialGroupManagerDao.getDealerBusinessSql("B.YIELDLY", poseId));
		if (!"".equals(vin))
		{
			//sbSql.append(GetVinUtil.getVins(vin, "TV"));
			sbSql.append("AND B.VIN like ?\n");
			par.add("%"+vin.trim()+"%");
		}
		if (!"".equals(dlvNo))
		{
			sbSql.append(" AND A.BILL_NO LIKE ? \n");
			par.add("%"+dlvNo+"%");
		}
		if (!"".equals(orderType))
		{
			sbSql.append(" AND F.ORDER_TYPE=?\n");
			par.add(orderType);
		}
		if(!"".equals(orderNo)){
			sbSql.append(" AND F.ORDER_NO LIKE ?");
			par.add("%"+orderNo+"%");
		}
		if(!"".equals(hgzNo)){
			sbSql.append(" AND B.HGZ_NO = ?");
			par.add(hgzNo);
		}
		sbSql.append(" ORDER BY A.BILL_ID, B.VEHICLE_ID\n"); 
		return dao.pageQuery(sbSql.toString(), par,
						"com.infodms.dms.dao.sales.storageManage.CheckVehicleDAO.getCheckList", pageSize, curPage);
	}
	
	/***
	 * 下载可验收入库的车辆列表
	 * 
	 * @param :
	 * @return
	 * @throws Exception
	 */
	public static List<Map<String, Object>> downloadCheckList(String poseId, String dealer_Id, String vin,
					String dlvNo, String orderType)
	{
//		StringBuffer sql = new StringBuffer("");
//
//		sql.append("SELECT TMV.VEHICLE_ID,--查询可验收入库的车辆列表\n");
//		sql.append("	   TMV.DEALER_ID,--开票方经销商ID\n");
//		sql.append("       H.MODEL_NAME GROUP_NAME, --车型名称\n");
//		sql.append("       H.MATERIAL_CODE, --物料代码\n");
//		sql.append("       H.MATERIAL_NAME, --物料名称\n");
//		sql.append("       TTD.RECEIVER, --接收方\n");
//		sql.append("       TVO.ORDER_ORG_ID, --订货方\n");
//		sql.append("       TVO.ORDER_TYPE, --订单类型\n");
//		sql.append("       TMV.VIN, --VIN\n");
//		sql.append("       TTDM.ERP_SENDCAR_ID, --\n");
//		sql.append("       TVDE.SENDCAR_ORDER_NUMBER, -- ERP交接单号\n");
//		sql.append("       TTD.DELIVERY_NO, -- 发运单号\n");
//		sql.append("	   TTDM.Match_Id,\n");
//		sql.append("	   TBA.AREA_NAME, --产地\n");
//		sql.append("       TO_CHAR(TTD.DELIVERY_DATE, 'yyyy-MM-dd') AS DELIVERY_DATE,\n");
//		sql.append("       TMW.WAREHOUSE_NAME \n");
//
//		sql.append("  FROM TM_VEHICLE  TMV,\n");
//		sql.append("       VW_MATERIAL_GROUP_MAT H,\n");
////		sql.append("       TM_VHCL_MATERIAL       TMVM,\n");
//		sql.append("	   TT_VS_ORDER TVO ,--订单\n");
//		sql.append("       TT_SALES_ALLOCA_DE B,                                    --配车明细表\n");
////		sql.append("		TT_VS_DLVRY_REQ TVDR ,   --发运申请\n");
////		sql.append("       TT_VS_DLVRY_MCH        TTDM,	--发运配车\n");
////		sql.append("       TT_VS_DLVRY_DTL        TTDD,	--发运明细\n");
////		sql.append("       TT_VS_DLVRY            TTD,	--发运表\n");
////		sql.append("       TM_BUSINESS_AREA       TBA,	\n");
////		sql.append("       TM_WAREHOUSE TMW, \n");
////		sql.append("       TT_VS_DLVRY_ERP TVDE ,\n");
////		sql.append(" WHERE TMV.MODEL_ID = G.GROUP_ID\n");
////		sql.append(" and TTDM.ERP_SENDCAR_ID = TVDE.SENDCAR_HEADER_ID\n") ;
////		sql.append("		and tvo.order_id = tvdr.order_id\n");
////		sql.append("       and tvdr.req_id = ttd.req_id\n");
////		sql.append("   AND TMV.YIELDLY = TBA.AREA_ID\n");
////		sql.append("   AND TMV.MATERIAL_ID = TMVM.MATERIAL_ID\n");
////		sql.append("   AND TMV.VEHICLE_ID = TTDM.VEHICLE_ID\n");
////		sql.append("   AND TTDM.DELIVERY_DETAIL_ID = TTDD.DETAIL_ID\n");
////		sql.append("   AND TTDD.DELIVERY_ID = TTD.DELIVERY_ID\n");
////		sql.append("   AND TMV.WAREHOUSE_ID = TMW.WAREHOUSE_ID \n");
////		sql.append("   AND TTD.BILLING_SIDE = TMV.DEALER_ID\n");
//		sql.append(" WHERE       TMV.VEHICLE_ID = B.VEHICLE_ID\n");
//		sql.append("         AND B.BO_DE_ID = C.BO_DE_ID\n");
//		sql.append("         AND C.BO_ID = D.BO_ID\n");
//		sql.append("         AND D.BILL_ID = E.bill_id\n");
//		sql.append("         AND D.ASS_ID = F.ASS_ID\n");
//		sql.append("         AND F.ORDER_ID = G.ORDER_ID\n");
//		sql.append("         AND A.MATERIAL_ID = H.MATERIAL_ID\n");
//		sql.append("         AND A.YIELDLY=I.AREA_ID\n");
//		if (null != vin && !"".equals(vin) ) {
//			sql.append(GetVinUtil.getVins(vin,"TMV"));
//		}
//		if (null != dlvNo && !"".equals(dlvNo)) {
//			sql.append("   AND TTD.DELIVERY_NO like UPPER('%" + dlvNo + "%') \n");
//		}
//		if (null != orderType && !"".equals(orderType)) {
//			sql.append("   AND TVO.ORDER_TYPE = " + orderType + " \n");
//		}
//		sql.append("   AND TMV.LIFE_CYCLE = "+Constant.VEHICLE_LIFE_05+"\n");  //经销商在途
//		sql.append("	and TVDR.receiver IN ("+dealer_Id+")\n") ;
//		sql.append("	AND TTDM.IF_INSPECTION = 0\n") ;
//		sql.append("and tdba.dealer_id=tvdr.receiver\n");
//		sql.append("and tdba.area_id = tpba.area_id\n");
//		sql.append("and tpba.pose_id = "+poseId+"\n");
//		sql.append("GROUP BY TMV.AREA_ID,TMV.VEHICLE_ID,\n");
//		sql.append("TMV.DEALER_ID,\n");
//		sql.append("G.GROUP_NAME,\n");
//		sql.append("TMVM.MATERIAL_CODE,\n");
//		sql.append("TMVM.MATERIAL_NAME,\n");
//		sql.append("TTD.RECEIVER,\n");
//		sql.append("TVO.ORDER_ORG_ID,\n");
//		sql.append("TVO.ORDER_TYPE,\n");
//		sql.append("TMV.VIN,\n");
//		sql.append("TTDM.ERP_SENDCAR_ID,\n");
//		sql.append("TVDE.SENDCAR_ORDER_NUMBER,\n");
//		sql.append("TTD.DELIVERY_NO,\n");
//		sql.append("TTDM.Match_Id,\n");
//		sql.append("TBA.AREA_NAME,\n");
//		sql.append("TTD.DELIVERY_DATE,\n");
//		sql.append("TMW.WAREHOUSE_NAME\n");
//		sql.append(" ORDER BY TTD.DELIVERY_DATE DESC, TVDE.SENDCAR_ORDER_NUMBER\n");
//		System.out.println("检测经销商验收车辆SQL:"+sql);
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("SELECT  A.BO_ID,\n");
		sbSql.append("        A.BO_NO,\n");
		sbSql.append("        TV.VEHICLE_ID,\n");
		sbSql.append("        B.BILL_ID,D.BILL_NO,\n");
		sbSql.append("        B.ORDER_NO,\n");
		sbSql.append("        (SELECT CODE_DESC FROM TC_CODE WHERE CODE_ID = B.ORDER_TYPE) ORDER_TYPE,\n");
		sbSql.append("        VM.MODEL_NAME,\n");
		sbSql.append("        VM.MATERIAL_CODE,\n");
		sbSql.append("        VM.MATERIAL_NAME,\n");
		sbSql.append("        TV.VIN,\n");
		sbSql.append("        TO_CHAR(D.BILL_CRT_DATE,'yyyy-MM-dd') DELIVERY_DATE,\n");
		sbSql.append("        TBA.AREA_NAME\n");
		sbSql.append("  FROM  TT_SALES_BOARD A,\n");
		sbSql.append("        TT_SALES_BO_DETAIL B,\n");
		sbSql.append("        (SELECT * FROM TT_SALES_ALLOCA_DE WHERE STATUS IS NULL OR STATUS = "+Constant.STATUS_ENABLE+") C,\n");
		sbSql.append("        TT_SALES_WAYBILL D,\n");
		sbSql.append("        TM_VEHICLE TV,\n");
		sbSql.append("        VW_MATERIAL_GROUP_MAT VM,\n");
		sbSql.append("        TM_BUSINESS_AREA TBA\n");
		sbSql.append("  WHERE A.BO_ID = B.BO_ID\n");
		sbSql.append("    AND B.BO_DE_ID = C.BO_DE_ID\n");
		sbSql.append("    AND B.BILL_ID = D.BILL_ID(+) \n");
		sbSql.append("    AND C.VEHICLE_ID = TV.VEHICLE_ID\n");
		sbSql.append("    AND B.MAT_ID = VM.MATERIAL_ID\n");
		sbSql.append("    AND TV.YIELDLY = TBA.AREA_ID");
		sbSql.append("    AND TV.LIFE_CYCLE = " + Constant.VEHICLE_LIFE_05 + "\n");
		sbSql.append("    AND B.REC_DEALER_ID = " + dealer_Id + "   \n");
		sbSql.append(MaterialGroupManagerDao.getDealerBusinessSql("TV.YIELDLY", poseId));
		if (!"".equals(vin))
		{
			sbSql.append(GetVinUtil.getVins(vin, "TV"));
		}
		if (!"".equals(dlvNo))
		{
			sbSql.append(" AND D.BILL_NO LIKE '%" + dlvNo + "%' \n");
		}
		if (!"".equals(orderType))
		{
			sbSql.append(" AND B.ORDER_TYPE=" + orderType);
		}
		return dao.pageQuery(sbSql.toString(), null,
						"com.infodms.dms.dao.sales.storageManage.CheckVehicleDAO.downloadCheckList");
	}
	
	/***
	 * 查询经销商评价项目列表
	 * 
	 * @param :
	 * @return
	 * @throws Exception
	 */
	public static List<Map<String, Object>> getDealerCommentList(String type)
	{
		StringBuffer sql = new StringBuffer("");
		
		sql.append(" SELECT TC.CODE_ID, ");
		sql.append(" TC.CODE_DESC ");
		sql.append(" FROM TC_CODE TC ");
		sql.append(" WHERE TC.STATUS = ");
		sql.append(Constant.STATUS_ENABLE);
		sql.append(" AND TC.TYPE =  ");
		sql.append(type);
		sql.append(" ORDER BY TC.NUM ");
		return dao.pageQuery(sql.toString(), null,
						"com.infodms.dms.dao.sales.storageManage.CheckVehicleDAO.getDealerCommentList");
	}
	
	public PageResult<Map<String, Object>> getDealerVhclDetail(String vehicle_life,String areaId, String poseId, String dealerIds,
					String vin, String engineNo, Long companyId, int curPage, int pageSize) throws Exception
	{
		List<Object> params = new LinkedList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT DECODE(TMD.DEALER_CODE, NULL, ' ', TMD.DEALER_CODE) DEALER_CODE,\n");
		sql.append("       DECODE(TMD.DEALER_SHORTNAME, NULL, ' ', TMD.DEALER_SHORTNAME) DEALER_NAME,\n");
		sql.append("       TVM.MATERIAL_CODE,\n");
		sql.append("       TVM.MATERIAL_NAME,\n");
		sql.append("       TMV.COLOR COLOR_NAME,\n");
		sql.append("       TMV.VIN,\n");
		sql.append("	   TMV.ENGINE_NO,\n");
		sql.append("       TMV.VEHICLE_ID,");
		sql.append("       TVM.VHCL_PRICE,\n");
		sql.append("       TMV.NODE_CODE,\n");
		sql.append("       TMV.LIFE_CYCLE,\n");
		sql.append("	   to_char(tmv.purchased_date, 'yyyy-mm-dd') purchased_date,\n");
		sql.append("       TMV.LOCK_STATUS,TMV.hegezheng_code\n");
		sql.append("  FROM TM_VEHICLE TMV,\n");
		sql.append("	   TM_VHCL_MATERIAL TVM,\n");
		
//		sql.append("(SELECT DISTINCT TW.WAREHOUSE_ID, TW.WAREHOUSE_NAME, TW.WAREHOUSE_TYPE\n");
//		sql.append("  FROM TM_WAREHOUSE TW, TM_BUSINESS_AREA TBA\n");
//		sql.append(" WHERE TW.ENTITY_CODE = TBA.ERP_CODE\n");
//		sql.append("   AND TW.STATUS = "+Constant.STATUS_ENABLE+"\n");
//		sql.append("   AND TW.COMPANY_ID = "+companyId+"\n");
//
//		sql.append("   AND TBA.AREA_ID IN ("+areaIds+")) TEMP,");
		sql.append("   TM_DEALER TMD\n");
		sql.append(" WHERE TMD.DEALER_ID(+) = TMV.DEALER_ID\n");
		sql.append("   AND TMV.MATERIAL_ID = TVM.MATERIAL_ID\n");
		//sql.append(MaterialGroupManagerDao.getDealerBusinessSql("TMV.YIELDLY", poseId));
		if (null != vin && !"".equals(vin))
		{
			//sql.append(GetVinUtil.getMyVins(vin, "TMV"));
			sql.append("AND TMV.VIN LIKE ?\n");
			params.add("%"+vin+"%");
		}
		if (null != engineNo && !"".equals(engineNo))
		{
			sql.append("AND TMV.ENGINE_NO like ?\n");
			params.add("%"+engineNo+"%");
		}
		if(null != vehicle_life && !"".equals(vehicle_life)){
			sql.append(" AND TMV.LIFE_CYCLE = ?\n");
			params.add(vehicle_life);
		}
		sql.append(" AND TMV.DEALER_ID =?\n");
		params.add(Long.valueOf(dealerIds));
//		if(!"".equals(groupCode)&&groupCode!=null){
//			sql.append(Utility.getConSqlByParamForEqual(groupCode, params, "TVMG", "GROUP_CODE"));
//			sql.append(" AND (TMV.SERIES_ID=TVMG.GROUP_ID OR  TMV.MODEL_ID=TVMG.GROUP_ID OR TMV.PACKAGE_ID=TVMG.GROUP_ID)");
//		}
//		if(!"".equals(dealerCode)&&dealerCode!=null){
//			sql.append(Utility.getConSqlByParamForEqual(dealerCode, params, "TMD", "DEALER_CODE"));
//		}
//		/*if (!"".equals(areaId)) {
//			sql.append("AND TMV.AREA_ID = "+areaId+"\n");
//		}
		if (null != areaId && !"".equals(areaId))
		{
			sql.append("AND TMV.YIELDLY = ?\n");
			params.add(Long.valueOf(areaId));
		}
		sql.append("ORDER BY  TVM.MATERIAL_CODE,DEALER_CODE,TMV.LIFE_CYCLE");
		PageResult<Map<String, Object>> ps = dao.pageQuery(sql.toString(), params, getFunName(), pageSize, curPage);
		return ps;
	}
	
	public List<Map<String, Object>> getLoadDealerVhclDetail(String areaId, String poseId, String dealerIds,
					String vin, String engineNo, Long companyId, int curPage, int pageSize) throws Exception
	{
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
		sql.append("       to_char(tmv.purchased_date, 'yyyy-mm-dd') purchased_date,\n");
		sql.append("       TMV.HEGEZHENG_CODE,\n");
		sql.append("       TC1.CODE_DESC LIFE_CYCLE,\n");
		sql.append("       TC2.CODE_DESC LOCK_STATUS\n");
		sql.append("  FROM TM_VEHICLE TMV,TC_CODE TC1,TC_CODE TC2,\n");
//		if(!"".equals(groupCode)&&groupCode!=null){
//			sql.append("       TM_VHCL_MATERIAL_GROUP TVMG,\n" );
//		}
		sql.append("	   TM_VHCL_MATERIAL TVM,\n");
//		sql.append("(SELECT DISTINCT TW.WAREHOUSE_ID, TW.WAREHOUSE_NAME, TW.WAREHOUSE_TYPE\n");
//		sql.append("  FROM TM_WAREHOUSE TW, TM_BUSINESS_AREA TBA\n");
//		sql.append(" WHERE TW.ENTITY_CODE = TBA.ERP_CODE\n");
//		sql.append("   AND TW.STATUS = "+Constant.STATUS_ENABLE+"\n");
//		sql.append("   AND TW.COMPANY_ID = "+companyId+"\n");
//		if (!"".equals(areaId) && areaId!=null) {
//			sql.append("   AND TBA.AREA_ID = "+areaId+"\n");
//		}
//		sql.append("   AND TBA.AREA_ID IN ("+areaIds+")) TEMP,");
		sql.append("       TM_DEALER TMD\n");
		sql.append(" WHERE TMD.DEALER_ID(+) = TMV.DEALER_ID\n");
		sql.append("   AND TMV.MATERIAL_ID = TVM.MATERIAL_ID\n");
//		sql.append("   AND TMV.WAREHOUSE_ID = TEMP.WAREHOUSE_ID(+)\n");
//		sql.append("   AND TMV.OEM_COMPANY_ID = "+companyId+"\n");
//		if(!"".equals(groupCode)&&groupCode!=null){
//			sql.append(Utility.getConSqlByParamForEqual(groupCode, params, "TVMG", "GROUP_CODE"));
//			sql.append(" AND (TMV.SERIES_ID=TVMG.GROUP_ID OR  TMV.MODEL_ID=TVMG.GROUP_ID OR TMV.PACKAGE_ID=TVMG.GROUP_ID)");
//		}
//		if(!"".equals(dealerCode)&&dealerCode!=null){
//			sql.append(Utility.getConSqlByParamForEqual(dealerCode, params, "TMD", "DEALER_CODE"));
//		}
		/* if (!"".equals(areaId)) {
		 * sql.append("AND TMV.AREA_ID = "+areaId+"\n");
		 * }
		 * if (!"".equals(areaIds) && "".equals(areaId)) {
		 * sql.append("AND TMV.AREA_ID IN ("+areaIds+")\n");
		 * } */
		sql.append("   AND TC1.CODE_ID=TMV.LIFE_CYCLE AND TC2.CODE_ID=TMV.LOCK_STATUS \n");
		sql.append(MaterialGroupManagerDao.getDealerBusinessSql("TMV.YIELDLY", poseId));
		sql.append(" AND TMV.DEALER_ID = " + dealerIds + " \n");
		if (null != vin && !"".equals(vin))
		{
			sql.append(GetVinUtil.getMyVins(vin, "TMV"));
		}
		if (null != engineNo && !"".equals(engineNo))
		{
			sql.append("AND TMV.ENGINE_NO like '%");
			sql.append(engineNo);
			sql.append("%'\n");
		}
		if (null != areaId && !"".equals(areaId))
		{
			sql.append(" AND TMV.YIELDLY = " + areaId + "\n");
		}
		sql.append("ORDER BY  TVM.MATERIAL_CODE,DEALER_CODE,TMV.LIFE_CYCLE");
		List<Map<String, Object>> ps = dao.pageQuery(sql.toString(), params, getFunName());
		return ps;
	}
	
	/***
	 * 查询入库车辆详细信息
	 * 
	 * @param :
	 * @return
	 * @throws Exception
	 */
	public static Map<String, Object> getVehicleInfo(String vehicle_id)
	{
		StringBuffer sql = new StringBuffer();
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
		return dao.pageQueryMap(sql.toString(), null,
						"com.infodms.dms.dao.sales.storageManage.CheckVehicleDAO.getVehicleInfo");
	}
	
	/* 通过车辆ID查询车辆接收方 */
	public List<Map<String, Object>> getReceiver(String vehcleId)
	{
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
	
	/***
	 * 根据vehicleId查询车辆订货方及付款方，判断系统是否自动执行调拨
	 * 
	 * @param :
	 * @return
	 * @throws Exception
	 */
	public static Map<String, Object> getVehicleOrder_Buy_ID(String vehicle_id)
	{
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
	protected PO wrapperPO(ResultSet rs, int idx)
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	public static Map<String, Object> getParentDealerId(String dealerId)
	{
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT TD.PARENT_DEALER_D \n");
		
		sql.append("FROM TM_DEALER TD \n");
		
		sql.append("WHERE TD.DEALER_ID='" + dealerId.trim() + "'\n");
		
		Map<String, Object> rs = dao.pageQueryMap(sql.toString(), null, dao.getFunName());
		
		return rs;
	}
	
	public static List<Map<String, Object>> warehouseQuery(String dealerIds)
	{
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
	
	public List<Map<String, Object>> getDelLevel(String dealerId)
	{
		StringBuffer sql = new StringBuffer("");
		
		sql.append("select tm.dealer_level from tm_dealer tm where tm.dealer_id in(" + dealerId + ")\n");
		
		return dao.pageQuery(sql.toString(), null, dao.getFunName());
	}
	
	//特殊经销商判断
	public static List<Map<String, Object>> getSpecialDlr(String dealerIds)
	{
		StringBuffer sql = new StringBuffer("");
		
		sql.append("SELECT *\n");
		sql.append("  FROM TM_DEALER TD\n");
		sql.append(" WHERE TD.DEALER_TYPE = " + Constant.DEALER_TYPE_DVS + "\n");
		sql.append("   AND TD.DEALER_CLASS = " + Constant.DEALER_CLASS_TYPE_06 + "\n");
		sql.append("	AND TD.DEALER_ID IN (" + dealerIds + ")");
		
		return dao.pageQuery(sql.toString(), null, dao.getFunName());
	}
	
	//结算中心判断
	public static List<Map<String, Object>> getDeal__(String dealerIds)
	{
		StringBuffer sql = new StringBuffer("");
		
		sql.append("SELECT *\n");
		sql.append("  FROM TM_DEALER TD\n");
		sql.append(" WHERE TD.DEALER_TYPE = " + Constant.DEALER_TYPE_JSZX + "\n");
		sql.append("	AND TD.DEALER_ID IN (" + dealerIds + ")");
		
		return dao.pageQuery(sql.toString(), null, dao.getFunName());
	}
	
	public List<Map<String, Object>> getOwnWarehouse(String ownid, String dealerId)
	{
		if (Utility.testString(ownid))
		{
			StringBuffer sql = new StringBuffer("\n");
			sql.append("SELECT A.WAREHOUSE_ID, A.WAREHOUSE_NAME\n");
			sql.append("FROM TM_DEALER_WAREHOUSE A\n");
			if (ownid.equals("01"))
			{
				sql.append("WHERE 1 = 1 AND A.DEALER_ID IN ( ").append(dealerId).append(" ) ");
				sql.append(" AND A.WAREHOUSE_TYPE = ").append(Constant.DEALER_WAREHOUSE_TYPE_01).append("\n");
			}
			else if (ownid.equals("02"))
			{
				sql.append("WHERE 1 = 1 AND A.MANAGE_DEALER_ID in ( ").append(dealerId).append(" ) ");
				sql.append(" AND A.WAREHOUSE_TYPE = ").append(Constant.DEALER_WAREHOUSE_TYPE_02).append("\n");
			}
			else if (ownid.equals("03"))
			{
				sql.append("WHERE 1 = 1 AND A.DEALER_ID IN ( ").append(dealerId).append(" ) ");
				sql.append(" AND A.WAREHOUSE_TYPE = ").append(Constant.DEALER_WAREHOUSE_TYPE_02).append("\n");
			}
			List<Map<String, Object>> list = dao.pageQuery(sql.toString(), null, dao.getFunName());
			return list;
		}
		else
		{
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
	public List<Map<String, Object>> getOwnWarehouse(Long ownid, String dealerId)
	{
		if (null != ownid || !"".equals(ownid.toString()))
		{
			StringBuffer sql = new StringBuffer("\n");
			
			sql.append("SELECT TMDW.WAREHOUSE_ID, TMDW.WAREHOUSE_NAME\n");
			sql.append("  FROM TM_DEALER_WAREHOUSE TMDW, TM_DEALER TMD\n");
			if (ownid == 1)
			{
				sql.append(" WHERE TMDW.DEALER_ID = TMD.DEALER_ID --下级经销商自有库\n");
				sql.append("   AND TMDW.WAREHOUSE_TYPE = " + Constant.DEALER_WAREHOUSE_TYPE_01 + "\n");
			}
			else if (ownid == 2)
			{
				sql.append("WHERE TMDW.MANAGE_DEALER_ID = TMD.DEALER_ID  --下级经销商代管库\n");
				sql.append("AND TMDW.WAREHOUSE_TYPE = " + Constant.DEALER_WAREHOUSE_TYPE_02 + "\n");
			}
			sql.append("   AND TMDW.STATUS = " + Constant.STATUS_ENABLE + "\n");
			sql.append("   AND TMD.PARENT_DEALER_D in (" + dealerId + ")\n");
			
			List<Map<String, Object>> list = dao.pageQuery(sql.toString(), null, dao.getFunName());
			return list;
		}
		else
		{
			return null;
		}
	}
	
	public List<Map<String, Object>> getDlvryEROInfo(String sendcardId)
	{
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
	
	//通过发运表汇总对应明细表中的发运数量和验收数量
	public List<Map<String, Object>> getAmount(Long dlvId)
	{
		StringBuffer sql = new StringBuffer("");
		
		sql.append("SELECT TVDD.DELIVERY_ID,\n");
		sql.append("       SUM(TVDD.DELIVERY_AMOUNT) DELIVERY_AMOUNT,\n");
		sql.append("       SUM(TVDD.INSPECTION_AMOUNT) INSPECTION_AMOUNT\n");
		sql.append("  FROM TT_VS_DLVRY_DTL TVDD\n");
		sql.append(" WHERE TVDD.DELIVERY_ID = '" + dlvId + "'\n");
		sql.append(" GROUP BY TVDD.DELIVERY_ID\n");
		
		return dao.pageQuery(sql.toString(), null, getFunName());
	}
	
	public static Map<String, Object> getChkVehlInfo(String vehlId)
	{
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
	
	public List<Map<String, Object>> chkMaterial(String groupIds, String areaIds)
	{
		StringBuffer sql = new StringBuffer("\n");
		
		sql.append("select * from tm_area_group tag where tag.material_group_id in (").append(groupIds)
						.append(") and tag.area_id in (").append(areaIds).append(")\n");
		
		return super.pageQuery(sql.toString(), null, super.getFunName());
	}
	
	public List<Map<String, Object>> getVin(String vehicleId)
	{
		StringBuffer sql = new StringBuffer("\n");
		
		sql.append("select tmv.vin from tm_vehicle tmv where tmv.vehicle_id in (").append(vehicleId).append(")");
		
		return super.pageQuery(sql.toString(), null, super.getFunName());
	}
	
	public TmBusinessAreaPO chkProductBase(String areaId)
	{
		TmBusinessAreaPO tba = new TmBusinessAreaPO();
		tba.setAreaId(Long.parseLong(areaId));
		
		TmBusinessAreaPO newTba = new TmBusinessAreaPO();
		newTba = (TmBusinessAreaPO) super.select(tba).get(0);
		
		return newTba;
	}
	
	public static Map<String, Object> getDealerId(String vehicle_id)
	{
		List<Object> params = new ArrayList<Object>();
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("select tv.dealer_id from tm_vehicle tv where tv.vehicle_id = ?\n");
		params.add(vehicle_id);
		
		return dao.pageQueryMap(sbSql.toString(), params, dao.getFunName());
	}
	
	//add by WHX,2012.09.18
	//========================================================Start
	public Long tempVehiDeInsert(Map<String, String> map)
	{
		String vin = map.get("vin");
		String inDlr = map.get("inDlr");
		String outDlr = map.get("outDlr");
		String type = map.get("type");
		String remark = map.get("remark");
		String userId = map.get("userId");
		
		StringBuffer sql = new StringBuffer("\n");
		
		Long id = Long.parseLong(SequenceManager.getSequence(""));
		
		sql.append("insert into TEMP_VEHICLE_DE values (").append(vin).append(", ").append(inDlr).append(", ")
						.append(outDlr).append(", ").append(type).append(", '").append(remark).append("', ")
						.append(userId).append(", sysdate, null, null, ").append(id).append(")\n");
		
		super.insert(sql.toString());
		
		return id;
	}
	
	public void tempVehiDeDelete(Long id)
	{
		StringBuffer sql = new StringBuffer("\n");
		
		sql.append("delete TEMP_VEHICLE_DE tvd where tvd.id = ").append(id).append("\n");
		
		super.delete(sql.toString(), null);
	}
	
	//========================================================End
	
	//物流评价查询车辆对应的承运商
	public static List<Map<String, Object>> getCYHost(String orderId, String vin)
	{
		StringBuffer sql = new StringBuffer("");
		
		sql.append(" SELECT DE.SHIP_METHOD_CODE FROM TT_VS_DLVRY_ERP_DTL DED, TT_VS_DLVRY_ERP DE \n");
		sql.append(" WHERE DE.SENDCAR_HEADER_ID = DED.SENDCAR_HEADER_ID \n");
		sql.append(" AND DE.ORDER_NUMBER = " + orderId + " ");
		sql.append(" AND DED.VIN = '" + vin + "'");
		System.out.println("物流评价查询承运商：" + sql);
		return dao.pageQuery(sql.toString(), null, dao.getFunName());
	}
	
	//物流评价查询物流评价项目分数定义对应的分数值
	public static List<Map<String, Object>> getPointsValue(String pointId)
	{
		StringBuffer sql = new StringBuffer("");
		
		sql.append(" SELECT TC.CODE_DESC FROM TC_CODE TC WHERE TC.CODE_ID='" + pointId + "'");
		System.out.println("物流评价项目分数：" + sql);
		return dao.pageQuery(sql.toString(), null, dao.getFunName());
	}
	
	//物流评价查询物流总体评价项目分数定义对应的分数值
	public static List<Map<String, Object>> getTotalValue(String totalId)
	{
		StringBuffer sql = new StringBuffer("");
		
		sql.append(" SELECT TC.NUM FROM TC_CODE TC WHERE TC.CODE_ID='" + totalId + "'");
		System.out.println("物流总体评价项目分数：" + sql);
		return dao.pageQuery(sql.toString(), null, dao.getFunName());
	}
	
	//add by tanv 2013-01-15 start
	public Map<String, Object> getDealerIdRpc(String vehicle_id, String vin_rpc)
	{
		List<Object> params = new ArrayList<Object>();
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("SELECT TV.DEALER_ID, TV.VEHICLE_ID FROM TM_VEHICLE TV WHERE TV.VIN = ?"); 
		params.add(vin_rpc);
		
		return dao.pageQueryMap(sbSql.toString(), params, dao.getFunName());
	}
	
	//add by tanv 2013-01-15 end
	
	public static List<Map<String, Object>> getCheckedVehicles(String detailId, String dealerId)
	{
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT DISTINCT AC.VEHICLE_ID \n");
		sql.append(" FROM   TM_VEHICLE  A, \n");
		sql.append(" TT_SALES_ALLOCA_DE B,");
		sql.append(" TT_SALES_BO_DETAIL C,");
		sql.append(" TT_SALES_BOARD     D,\n");
		sql.append(" TT_SALES_WAYBILL   E,");
		sql.append(" TT_SALES_ASSIGN    F,");
		sql.append(" TT_VS_ORDER        G,\n");
		sql.append(" TT_VS_INSPECTION   AC,");
		sql.append(" TT_VS_ORDER_DETAIL O \n");
		sql.append(" WHERE   B.BO_DE_ID = C.BO_DE_ID \n");
		sql.append("   AND   A.VEHICLE_ID = B.VEHICLE_ID \n");
		sql.append("   AND   C.BO_ID = D.BO_ID \n");
		sql.append("   AND   D.BILL_ID = E.BILL_ID \n");
		sql.append("   AND   D.ASS_ID = F.ASS_ID \n");
		sql.append("   AND   F.ORDER_ID = G.ORDER_ID\n");
		sql.append("   AND   O.DETAIL_ID = C.OR_DE_ID \n");
		sql.append("   AND   A.VEHICLE_ID=AC.VEHICLE_ID\n");
		sql.append("   AND   AC.OPERATE_DEALER= " + dealerId + "\n");
		sql.append("   AND   G.DEALER_ID = " + dealerId + "\n");
		sql.append("   AND   O.DETAIL_ID = " + detailId + "\n");
		return dao.pageQuery(sql.toString(), null, dao.getFunName());
	}

	/**
	 * 修改分派表ProcessStatus状态【验收】
	 * @param params 参数list
	 * @return
	 * @throws Exception
	 */
	public void updateProcessStatus(String assNo,Long dealerId,Long logiId) throws Exception{
		StringBuffer sbSql= new StringBuffer();
		sbSql.append("UPDATE TT_SALES_ASS_DETAIL X\n");
		sbSql.append("   SET X.PROCESS_STATUS =\n");
		sbSql.append("       (SELECT CASE\n");
		sbSql.append("                 WHEN NVL(SUM(NVL(B.SEND_NUM, 0)), 0) =\n");
		sbSql.append("                      NVL(SUM(NVL(B.ACC_NUM, 0)), 0) AND\n");
		sbSql.append("                      NVL(SUM(NVL(B.ACC_NUM, 0)), 0) != 0 THEN\n");
		sbSql.append("                  ?\n");
		sbSql.append("                 WHEN NVL(SUM(NVL(B.SEND_NUM, 0)), 0) >\n");
		sbSql.append("                      NVL(SUM(NVL(B.ACC_NUM, 0)), 0) AND\n");
		sbSql.append("                      NVL(SUM(NVL(B.ACC_NUM, 0)), 0) != 0 THEN\n");
		sbSql.append("                  ?\n");
		sbSql.append("                 ELSE\n");
		sbSql.append("                  ?\n");
		sbSql.append("               END STATUS_TRACKING\n");
		sbSql.append("\n");
		sbSql.append("          FROM TT_SALES_ASSIGN A, TT_SALES_ASS_DETAIL B\n");
		sbSql.append("         WHERE A.ASS_ID = B.ASS_ID\n");
		sbSql.append("           AND A.ASS_NO = ?\n");
		sbSql.append("           AND A.DEALER_ID = ?\n");
		sbSql.append("           AND B.LOGI_ID = ?)\n");
		sbSql.append(" WHERE X.ASS_DETAIL_ID IN (SELECT S1.ASS_DETAIL_ID\n");
		sbSql.append("                             FROM TT_SALES_ASSIGN S, TT_SALES_ASS_DETAIL S1\n");
		sbSql.append("                            WHERE S.ASS_ID = S1.ASS_ID\n");
		sbSql.append("                              AND S.ASS_NO = ?\n");
		sbSql.append("                              AND S.DEALER_ID = ?\n");
		sbSql.append("                              AND S1.LOGI_ID = ?)");   
		List<Object> list1 = new ArrayList<Object>();
		list1.add(Constant.HANDLE_STATUS08);//完全验收
		list1.add(Constant.HANDLE_STATUS07);//部分验收
		list1.add(Constant.HANDLE_STATUS06);//完全生成运单
		list1.add(assNo);
		list1.add(dealerId);
		list1.add(logiId);
		list1.add(assNo);
		list1.add(dealerId);
		list1.add(logiId);
		factory.update(sbSql.toString(), list1);
	}
	/**
	 * 获取分派单号 发运经销商id 物流商id
	 * @param assDeId
	 * @return
	 */
	public Map<String, Object> getMoreMsg(String assDeId)
	{
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("SELECT C.ASS_NO, C.REC_DEALER_ID, B.LOGI_ID\n");
		sbSql.append("  FROM TT_SALES_ALLOCA_DE A, TT_SALES_ASS_DETAIL B, TT_SALES_ASSIGN C\n");
		sbSql.append(" WHERE A.ASS_DETAIL_ID = B.ASS_DETAIL_ID\n");
		sbSql.append("   AND B.ASS_ID = C.ASS_ID\n");
		sbSql.append("   AND A.ASS_DETAIL_ID =?\n");
		sbSql.append("   AND ROWNUM = 1"); 
		List<Object> list1 = new ArrayList<Object>();
		list1.add(assDeId);//分派明细ID
		return dao.pageQueryMap(sbSql.toString(), list1, dao.getFunName());
	}
	/**
	 * 获取运单下广宣品的明细
	 * @param wayBillId运单号
	 * @return 经销商ID
	 * @throws Exception
	 */
	public List<Map<String, Object>> getGxpPartMapByVin(String wayBillId,String dealerId) throws Exception{
		/******************************页面查询字段end***************************/
		StringBuffer sbSql= new StringBuffer();
		List<Object> params = new LinkedList<Object>();
		params.add(dealerId);//经销商ID
		//params.add(wayBillId);//vin
		sbSql.append("select a.pkg_no,a.vin,a.REPORT_QTY,REMARK from (\r\n");
		sbSql.append("SELECT T2.PKG_NO,\r\n");
		sbSql.append("       t2.vin,\r\n");
		sbSql.append("       SUM(T2.REPORT_QTY) REPORT_QTY, --数量\r\n");
		sbSql.append("       ZA_CONCAT(T2.DLINE_ID) DLINE_ID, --计划明细id(根据这里的id来设置VIN)\r\n");
		sbSql.append("       ZA_CONCAT(T2.IN_REMARK) REMARK --验收备注\r\n");
		sbSql.append("  FROM TT_PART_DPLAN_MAIN T1, TT_PART_DPLAN_DTL T2\r\n");
		sbSql.append(" WHERE T1.PLAN_ID = T2.PLAN_ID\r\n");
		sbSql.append("   AND EXISTS (SELECT 1\r\n");
		sbSql.append("          FROM TM_DEALER T\r\n");
		sbSql.append("         WHERE EXISTS (SELECT 1\r\n");
		sbSql.append("                  FROM TM_DEALER TD\r\n");
		sbSql.append("                 WHERE TD.COMPANY_ID = T.COMPANY_ID\r\n");
		sbSql.append("                   AND TD.DEALER_ID = ?)\r\n");
		sbSql.append("           AND T.DEALER_ID = T1.DEALER_ID\r\n");
		sbSql.append("           and t.dealer_type = 10771002\r\n");
		sbSql.append("           and t.status = 10011001)\r\n");
		sbSql.append("   AND T1.STATE = 92791004 --状态为已提交\r\n");
		sbSql.append("   AND T1.OUT_TYPE = 92781001 --出库类型为随车出库\r\n");
		sbSql.append(" GROUP BY T2.PKG_NO, t2.vin) a where 1=1"); 
		sbSql.append(" and A.VIN IN(select distinct l.bill_id\r\n");
		sbSql.append("  from tm_vehicle m, Tt_Sales_Alloca_De l WHERE 1=1 \r\n");
		sbSql.append(Utility.getConSqlByParamForEqual(wayBillId, params, "M", "VEHICLE_ID"));
		sbSql.append("and m.vehicle_id = l.vehicle_id)"); 
		List<Map<String, Object>> map= dao.pageQuery(sbSql.toString(), params, getFunName());
		return map;
	}
	/**
	 * 获取运单下配件的明细
	 * @param wayBillId运单号
	 * @return 经销商ID
	 * @throws Exception
	 */
	public List<Map<String, Object>> getPjPartMapByVin(String wayBillId) throws Exception{
		/******************************页面查询字段end***************************/
		StringBuffer sbSql= new StringBuffer();
		List<Object> params = new LinkedList<Object>();
		//params.add(dealerId);//经销商ID
		//params.add(wayBillId);//vin
		sbSql.append("SELECT B.*\r\n");
		sbSql.append("  FROM (SELECT DISTINCT T.PKG_NO, --装箱号,\r\n");
		sbSql.append("                        (T.LENGTH || '*' || T.WIDTH || '*' || T.HEIGHT) BZCC, --包装尺寸(长*宽*高),\r\n");
		sbSql.append("                        T.VOLUME, --体积,\r\n");
		sbSql.append("                        T.CH_WEIGHT, --计费重量,\r\n");
		sbSql.append("                        T.EQ_WEIGHT, --拆合重量,\r\n");
		sbSql.append("                        T.WEIGHT, --单箱重量,\r\n");
		sbSql.append("                        T.REMARK, --备注\r\n");
		sbSql.append("                        (SELECT SUM(P.PKG_QTY)\r\n");
		sbSql.append("                           FROM TT_PART_PKG_DTL P\r\n");
		sbSql.append("                          WHERE P.PKG_NO = T.PKG_NO\r\n");
		sbSql.append("                            and p.PICK_ORDER_ID = t.PICK_ORDER_ID) PKG_QTY --配件数量\r\n");
		sbSql.append("          FROM TT_PART_PKG_BOX_DTL T where 1=1\r\n");
		sbSql.append(" AND T.VIN IN( select distinct l.bill_id\r\n");
		sbSql.append("  from tm_vehicle m, Tt_Sales_Alloca_De l where 1=1\r\n");
		sbSql.append(Utility.getConSqlByParamForEqual(wayBillId, params, "M", "VEHICLE_ID"));
		sbSql.append("  and m.vehicle_id = l.vehicle_id)"); 	
		sbSql.append("\r\n");
		sbSql.append("        ) B\r\n");
		sbSql.append(" WHERE B.PKG_QTY > 0"); 
		List<Map<String, Object>> map= dao.pageQuery(sbSql.toString(), params, getFunName());
		return map;
	}
	
	/**
	 * 是否有随车品
	 * @param vehicleIdstr
	 * @return 经销商ID
	 * @throws Exception
	 */
	public List<Map<String, Object>> getSCPByVin(String vehicleIdstr) throws Exception{
		/******************************页面查询字段end***************************/
		StringBuffer sbSql= new StringBuffer();
		List<Object> params = new LinkedList<Object>();
		//params.add(dealerId);//经销商ID
		//params.add(wayBillId);//vin
		sbSql.append("select t.scp_isys from tt_sales_waybill t where "); 
		sbSql.append(" t.bill_id  IN( select distinct l.bill_id\r\n");
		sbSql.append("  from tm_vehicle m, Tt_Sales_Alloca_De l where 1=1\r\n");
		sbSql.append(Utility.getConSqlByParamForEqual(vehicleIdstr, params, "M", "VEHICLE_ID"));
		sbSql.append("  and m.vehicle_id = l.vehicle_id)"); 	
		List<Map<String, Object>> map= dao.pageQuery(sbSql.toString(), params, getFunName());
		return map;
	}
	
	public void updateSCPstatus(String vehicleIdstr,String remark){
		StringBuffer sbSql= new StringBuffer();
		List<Object> params = new LinkedList<Object>();
		params.add(Constant.IF_TYPE_YES);//是否验收 是
		params.add(remark);//remark
		sbSql.append("update tt_sales_waybill s\r\n");
		sbSql.append("   set s.scp_isys = ?, s.scp_remark = ?\r\n");
		sbSql.append(" where s.bill_id  IN( select distinct l.bill_id\r\n");
		sbSql.append("  from tm_vehicle m, Tt_Sales_Alloca_De l where 1=1\r\n");
		sbSql.append(Utility.getConSqlByParamForEqual(vehicleIdstr, params, "M", "VEHICLE_ID"));
		sbSql.append("  and m.vehicle_id = l.vehicle_id)"); 	
		dao.update(sbSql.toString(), params);

	}
	
	public List<Map<String, Object>> querySalesLogList(String orderId){
		String sql = " select a.log_id,a.log_status,a.order_id,b.org_name,tc.name,a.remark,a.log_date from tt_dealer_actual_sales_log a left join TM_ORG b on a.org_id=b.org_id "
				+" left join tc_user tc on tc.user_id= a.user_id "+
				"where order_id=? order by a.log_date desc";
		List<Object> params = new ArrayList<Object>();
		params.add(orderId);
		return pageQuery(sql, params, this.getFunName());
	}
	
	public Map<String,String> getUserInfo(AclUserBean logonUser)
	{
		StringBuffer sbSql = new StringBuffer();
		Map<String,String> info = new HashMap<String, String>();
//		info.put("dealerid", logonUser.getDealerId());
		
		sbSql.append("SELECT NAME,HAND_PHONE,PHONE\n");
		sbSql.append("  FROM TC_USER\n");
		sbSql.append(" WHERE USER_ID=").append(logonUser.getUserId());
		Map<String,String> info1 = dao.pageQueryMap(sbSql.toString(), null, dao.getFunName());
		info.put("deliverer", info1.get("NAME"));
		info.put("delivererMobile", info1.get("HAND_PHONE"));
		info.put("delivererPhone", info1.get("PHONE"));
		return info;
	}
	
	public Map<String, Object> getDealerCode_F(AclUserBean logonUser)
	{
		StringBuffer sbSql = new StringBuffer();
		String dealerCode = logonUser.getDealerCode().substring(0,logonUser.getDealerCode().length()-1);
		sbSql.append("SELECT DEALER_CODE,DEALER_ID,DEALER_NAME\n");
		sbSql.append("  FROM tm_dealer\n");
		sbSql.append(" WHERE DEALER_CODE like'%").append(dealerCode).append("%'");
		sbSql.append(" and DEALER_TYPE =10771002");
		sbSql.append(" and SERVICE_STATUS =13691002");
		
		return dao.pageQueryMap(sbSql.toString(), null, dao.getFunName());
	}
	public String getOrgCode(String dealer_id)
	{
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("SELECT ORG_CODE\n");
		sbSql.append("  FROM Vw_Org_Dealer\n");
		sbSql.append(" WHERE DEALER_ID =").append(dealer_id);
		
		return dao.pageQueryMap(sbSql.toString(), null, dao.getFunName()).get("ORG_CODE").toString();
	}
	
	public List<Map<String, Object>> getAddress(AclUserBean logonUser)
	{
		StringBuffer sbSql = new StringBuffer();

		sbSql.append("SELECT * FROM ( SELECT dealer_id, address, sex, link_man, tel, mobile_phone, address_type, id, status FROM tm_vs_address_more "
				+ "UNION ALL SELECT dealer_id, (SELECT region_name FROM tm_region WHERE region_code = province_id)"
				+ "|| (SELECT region_name FROM tm_region WHERE region_code = city_id)"
				+ "|| (SELECT region_name FROM tm_region WHERE region_code = area_id)"
				+ "|| address as address, sex, link_man, tel, mobile_phone, address_type, id, status FROM tm_vs_address where 1=1) a WHERE a.dealer_id"
				+ " = "+logonUser.getDealerId());
				sbSql.append(" AND a.address_type = 20481004\n");
				sbSql.append(" AND a.status = 10011001\n");
		
		return dao.pageQuery(sbSql.toString(), null, dao.getFunName());
	}
	
	public String getVar(String COMPANY_ID)
	{
		StringBuffer sbSql = new StringBuffer();

		sbSql.append("select max(ver) as ver from tc_company_log WHERE COMPANY_ID=").append(COMPANY_ID);
		Map<String, Object> map = dao.pageQueryMap(sbSql.toString(), null, dao.getFunName());
		if(map.get("VER")==null){
			return "";
		}else{
			return map.get("VER")+"";
		}
		
	}

	public boolean isCurrentDealer (String vehicleId, String dealer_id){
		StringBuffer sbSql = new StringBuffer();

		sbSql.append("select t1.dealer_id from tm_vehicle t,tt_vs_order t1 where  t.order_no = t1.order_no and t.vehicle_Id = ").append(vehicleId);
		Map<String, Object> map = dao.pageQueryMap(sbSql.toString(), null, dao.getFunName());
		if(dealer_id.equals(map.get("DEALER_ID").toString())){
			return true;
		}else{
			return false;
		}
	}
	
	public boolean isSaleDealer (String dealer_id){
		StringBuffer sbSql = new StringBuffer();

		sbSql.append("select t.dealer_type from tm_dealer t where  t.dealer_id =  ").append(dealer_id);
		Map<String, Object> map = dao.pageQueryMap(sbSql.toString(), null, dao.getFunName());
		if(map.get("DEALER_TYPE").toString().equals("10771001")){
			return true;
		}else{
			return false;
		}
	}
	
	public boolean isChecked (String vehicle_id){
		
		StringBuffer sbSql = new StringBuffer();

		sbSql.append("select count(1) as checked from TT_VS_INSPECTION t where  t.vehicle_id =  ").append(vehicle_id);
		Map<String, Object> map = dao.pageQueryMap(sbSql.toString(), null, dao.getFunName());
		if(map.get("CHECKED").toString().equals("0")){
			return false;
		}else{
			return true;
		}
	}
	
	public Map<String, String> getGroupByVechileId(String vechile_id) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT TVG3.GROUP_CODE FROM TM_VHCL_MATERIAL_GROUP TVG1 ,TM_VHCL_MATERIAL_GROUP TVG2 ,TM_VHCL_MATERIAL_GROUP TVG3 ,TM_VHCL_MATERIAL_GROUP_R TVGR, TM_VEHICLE TV,TM_VHCL_MATERIAL TVM\n");
		sql.append("WHERE TVG1.PARENT_GROUP_ID=TVG2.GROUP_ID AND TVG2.PARENT_GROUP_ID=TVG3.GROUP_ID AND TVGR.MATERIAL_ID=TVM.MATERIAL_ID AND TVGR.GROUP_ID=TVG1.GROUP_ID AND TVM.MATERIAL_ID=TV.MATERIAL_ID\n");
		sql.append("AND  TV.VEHICLE_ID=" + vechile_id);
		return dao.pageQueryMap(sql.toString(), null, dao.getFunName());

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
	 * 查询可验收入库的车辆列表
	 * 
	 * @param :
	 * @return
	 * @throws Exception
	 */

	public static List<Map<String,Object>> getCheckLists(String poseId,String dealer_Id,String vin, String dlvNo,String dlvryNo, int pageSize,int curPage){
		StringBuffer sql = new StringBuffer("");
		//sql.append("ELECT * FROM (                                  \n");
		sql.append("SELECT TMV.VEHICLE_ID, --查询可验收入库的车辆列表\n");
		//sql.append("      TMV.DEALER_ID, --开票方经销商ID           \n");
		sql.append("      TTDED.DETAIL_ID AS TTDED_ID, --发运单明细ID\n");
		sql.append("      G.GROUP_NAME, --车型名称                   \n");
		sql.append("      TMVM.MATERIAL_CODE, --物料代码             \n");
		sql.append("      TMVM.MATERIAL_NAME, --物料名称             \n");
		sql.append("      TVDR.RECEIVER, --接收方                    \n");
		sql.append("      TVDR.dealer_id, --订货方                   \n");
		sql.append("      TMV.VIN, --VIN                             \n");
		sql.append("      TMV.ENGINE_NO, --发动机号                  \n");
		sql.append("      TTDE.SENDCAR_ORDER_NUMBER, -- 发运单号     \n");
		sql.append("      TVDR.DLVRY_REQ_NO,                         \n");
		sql.append("      TTDE.SHIP_METHOD_CODE,                     \n");
		sql.append("      TTDE.MOTORMAN,                             \n");
		sql.append("      TTDE.MOTORMAN_PHONE,                       \n");
		sql.append("      TO_CHAR(TTDE.FLATCAR_ASSIGN_DATE, 'YYYY-MM-DD') AS DELIVERY_DATE,\n");
		sql.append("      TMW.WAREHOUSE_NAME,\n");
		sql.append("         TO_CHAR(TMV.PRODUCT_DATE,'YYYY-MM-DD') as PRODUCT_DATE,\n");
		sql.append("         TO_CHAR(TMV.FACTORY_DATE,'YYYY-MM-DD') as FACTORY_DATE,\n");
		sql.append("      TO_CHAR(TTDE.ARRIVE_DATE,'YYYY-MM-DD') as ARRIVE_DATE,    \n");
		sql.append("      DECODE(SIGN(TRUNC(TTDE.ARRIVE_DATE, 'DD') - (SYSDATE)), -1, 1, 0) AS IS_RED\n");
		sql.append(" FROM TM_VEHICLE              TMV,                      \n");
		sql.append("      TM_VHCL_MATERIAL_GROUP  G,                        \n");
		sql.append("      TM_VHCL_MATERIAL        TMVM,                     \n");
		sql.append("      TT_VS_DLVRY_REQ         TVDR, --发运申请          \n");
		sql.append("      TT_VS_DLVRY_ERP_DTL     TTDED, --ERP发运子表      \n");
		sql.append("      TT_VS_DLVRY_ERP         TTDE, --ERP发运主表       \n");
		sql.append("      TM_WAREHOUSE            TMW                       \n");
		sql.append("WHERE TMV.MODEL_ID = G.GROUP_ID                         \n");
		sql.append("  AND TVDR.REQ_ID = TTDED.REQ_ID                        \n");
		sql.append("  AND TMV.MATERIAL_ID = TMVM.MATERIAL_ID                \n");
		sql.append("  AND TMV.VEHICLE_ID = TTDED.VEHICLE_ID                 \n");
		sql.append("  AND TTDE.SENDCAR_HEADER_ID = TTDED.SENDCAR_HEADER_ID  \n");
		sql.append("  AND TMV.WAREHOUSE_ID = TMW.WAREHOUSE_ID               \n");
		sql.append("   AND TTDED.IS_RECEIVE = " + Constant.IS_RECEIVE_0 + "\n");
		sql.append("   AND TMV.LIFE_CYCLE = "+Constant.VEHICLE_LIFE_08+"\n");  //车厂在途
		sql.append("	and TVDR.receiver IN ("+dealer_Id+")\n") ;
		if (null != vin && !"".equals(vin)) {
			sql.append(GetVinUtil.getVins(vin, "TMV"));
		}
		if (null != dlvNo && !"".equals(dlvNo)) {
			sql.append("   AND TVDR.DLVRY_REQ_NO like UPPER('%" + dlvNo + "%') \n");
		}

		if (null != dlvryNo && !"".equals(dlvryNo)) {
			sql.append("   AND TTDE.SENDCAR_ORDER_NUMBER like UPPER('%" + dlvryNo + "%') \n");
		}
		sql.append("ORDER BY DELIVERY_DATE DESC, TTDED_ID ASC") ;
		/*sql.append("SELECT * FROM ( \n");
		sql.append("SELECT TMV.VEHICLE_ID, --查询可验收入库的车辆列表\n" );
		sql.append("       TMV.DEALER_ID, --开票方经销商ID\n" );
		sql.append("       TTDED.DETAIL_ID AS TTDED_ID, --发运单明细ID\n" );
		sql.append("       G.GROUP_NAME, --车型名称\n" );
		sql.append("       TMVM.MATERIAL_CODE, --物料代码\n" );
		sql.append("       TMVM.MATERIAL_NAME, --物料名称\n" );
		sql.append("       TVDR.RECEIVER, --接收方\n" );
		sql.append("       TVO.ORDER_ORG_ID, --订货方\n" );
		sql.append("       TMV.VIN, --VIN\n" );
		sql.append("       TMV.ENGINE_NO, --发动机号\n");
		sql.append("       TTDE.SENDCAR_ORDER_NUMBER, -- 发运单号\n" );
		sql.append("       TBA.AREA_NAME, --车辆业务范围\n" );
		sql.append("       TVDR.DLVRY_REQ_NO,\n" );
		sql.append("       TTDE.SHIP_METHOD_CODE,\n" );
		sql.append("       TTDE.MOTORMAN,\n" );
		sql.append("       TTDE.MOTORMAN_PHONE,\n" );
		sql.append("       TO_CHAR(TTDE.FLATCAR_ASSIGN_DATE, 'YYYY-MM-DD') AS DELIVERY_DATE,\n" );
		sql.append("       TMW.WAREHOUSE_NAME,\n" );
		sql.append("          TO_CHAR(TMV.PRODUCT_DATE,'YYYY-MM-DD') as PRODUCT_DATE,\n" );
		sql.append("          TO_CHAR(TMV.FACTORY_DATE,'YYYY-MM-DD') as FACTORY_DATE,\n" );
		sql.append("       TO_CHAR(TTDE.ARRIVE_DATE,'YYYY-MM-DD') as ARRIVE_DATE,\n" );
		sql.append("    ts.CYS_SSDD , \n" );
		sql.append("       DECODE(SIGN(TRUNC(TTDE.ARRIVE_DATE, 'DD') - (SYSDATE)), -1, 1, 0) AS IS_RED\n" );
	
		sql.append("  FROM TM_VEHICLE              TMV,\n" );
		sql.append("       TM_VHCL_MATERIAL_GROUP  G,\n" );
		sql.append("       TM_VHCL_MATERIAL        TMVM,\n" );
		sql.append("       TT_VS_ORDER             TVO, --订单\n" );
		sql.append("       TT_VS_DLVRY_REQ         TVDR, --发运申请\n" );
		sql.append("       TT_VS_DLVRY_ERP_DTL     TTDED, --ERP发运子表\n" );
		sql.append("       TT_VS_DLVRY_ERP         TTDE, --ERP发运主表\n" );
		sql.append("       TM_BUSINESS_AREA        TBA,\n" );
		sql.append("       TM_WAREHOUSE            TMW,\n" );
		sql.append("       TM_DEALER_BUSINESS_AREA TDBA,\n" );
		sql.append("       TM_POSE_BUSINESS_AREA   TPBA,\n" );
		sql.append("       (select t1.vin,t2.cys_ssdd from (select td.vin, max(th.rq) rq\n" );
		sql.append("  from v_cys_ztxx@dms2bcask th, v_cys_wlmx@dms2bcask td\n" );
		sql.append(" where th.ydh = td.ydh\n" );
		sql.append("   group by vin) t1,(select td.vin,th.cys_ssdd,th.rq\n" );
		sql.append("   from v_cys_ztxx@dms2bcask th, v_cys_wlmx@dms2bcask td where th.ydh=td.ydh) t2\n" );
		sql.append("   where t1.vin=t2.vin\n" );
		sql.append("   and t1.rq=t2.rq) ts\n" );
		sql.append("\n" );
		sql.append(" WHERE TMV.MODEL_ID = G.GROUP_ID\n" );
		sql.append("   AND TVO.ORDER_ID = TVDR.ORDER_ID\n" );
		sql.append("   AND TVDR.REQ_ID = TTDED.REQ_ID\n" );
		sql.append("   AND TMV.AREA_ID = TBA.AREA_ID\n" );
		sql.append("   AND TMV.MATERIAL_ID = TMVM.MATERIAL_ID\n" );
		sql.append("   AND TMV.VEHICLE_ID = TTDED.VEHICLE_ID\n" );
		sql.append("   AND TTDE.SENDCAR_HEADER_ID = TTDED.SENDCAR_HEADER_ID\n" );
		sql.append("   AND TMV.WAREHOUSE_ID = TMW.WAREHOUSE_ID\n" );
		sql.append("   AND TDBA.DEALER_ID = TVDR.RECEIVER\n" );
		sql.append("   and ts.vin(+)=tmv.vin");

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
		sql.append("         TMV.ENGINE_NO,\n");
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
		sql.append("         TTDE.ARRIVE_DATE,\n") ;
		sql.append("         ts.cys_ssdd\n") ;
		
	
		 * 真实发运交接单
		 
		sql.append(" UNION ALL \n");
		sql.append(" SELECT TMV.VEHICLE_ID, --查询可验收入库的车辆列表 \n");
		sql.append("       TMV.DEALER_ID, --开票方经销商ID \n");
		sql.append("       TTDED.DETAIL_ID AS TTDED_ID, --发运单明细ID \n");
		sql.append("       G.GROUP_NAME, --车型名称 \n");
		sql.append("       TMVM.MATERIAL_CODE, --物料代码 \n");
		sql.append("       TMVM.MATERIAL_NAME, --物料名称 \n");
		sql.append("       tvsdr.dealer_id, --接收方 \n");
		sql.append("       tvsdr.dealer_id, --订货方 \n");
		sql.append("       TMV.VIN, --VIN \n");
		sql.append("       TMV.ENGINE_NO, --发动机号\n");
		sql.append("       TTDE.SENDCAR_ORDER_NUMBER, -- 发运单号 \n");
		sql.append("       TBA.AREA_NAME, --车辆业务范围 \n");
		sql.append("       tvsdr.DLVRY_REQ_NO, \n");
		sql.append("       TTDE.SHIP_METHOD_CODE, \n");
		sql.append("       TTDE.MOTORMAN, \n");
		sql.append("       TTDE.MOTORMAN_PHONE, \n");
		sql.append("       TO_CHAR(TTDE.FLATCAR_ASSIGN_DATE, 'YYYY-MM-DD') AS DELIVERY_DATE, \n");
		sql.append("       TMW.WAREHOUSE_NAME, \n");
		sql.append("          TO_CHAR(TMV.PRODUCT_DATE,'YYYY-MM-DD') as PRODUCT_DATE, \n");
		sql.append("          TO_CHAR(TMV.FACTORY_DATE,'YYYY-MM-DD') as FACTORY_DATE, \n");
		sql.append("       TO_CHAR(TTDE.ARRIVE_DATE,'YYYY-MM-DD') as ARRIVE_DATE, \n");
		sql.append("       ts.CYS_SSDD ,  \n");
		sql.append("       DECODE(SIGN(TRUNC(TTDE.ARRIVE_DATE, 'DD') - (SYSDATE)), -1, 1, 0) AS IS_RED \n");
		sql.append("  FROM TM_VEHICLE              TMV, \n");
		sql.append("       TM_VHCL_MATERIAL_GROUP  G, \n");
		sql.append("       TM_VHCL_MATERIAL        TMVM, \n");
		sql.append("       TT_VS_ds_DLVRY_REQ         tvsdr, --真实发运申请 \n");
		sql.append("       TT_VS_DLVRY_ERP_DTL     TTDED, --ERP发运子表 \n");
		sql.append("       TT_VS_DLVRY_ERP         TTDE, --ERP发运主表 \n");
		sql.append("       TM_BUSINESS_AREA        TBA, \n");
		sql.append("       TM_WAREHOUSE            TMW, \n");
		sql.append("       TM_DEALER_BUSINESS_AREA TDBA, \n");
		sql.append("       TM_POSE_BUSINESS_AREA   TPBA, \n");
		sql.append("       (select t1.vin,t2.cys_ssdd from (select td.vin, max(th.rq) rq \n");
		sql.append("  from v_cys_ztxx@dms2bcask th, v_cys_wlmx@dms2bcask td \n");
		sql.append(" where th.ydh = td.ydh \n");
		sql.append("   group by vin) t1,(select td.vin,th.cys_ssdd,th.rq \n");
		sql.append("   from v_cys_ztxx@dms2bcask th, v_cys_wlmx@dms2bcask td where th.ydh=td.ydh) t2 \n");
		sql.append("   where t1.vin=t2.vin \n");
		sql.append("   and t1.rq=t2.rq) ts  \n");
		sql.append(" WHERE TMV.MODEL_ID = G.GROUP_ID \n");
		sql.append("   AND tvsdr.REQ_ID = TTDED.REQ_ID \n");
		sql.append("   AND TMV.AREA_ID = TBA.AREA_ID \n");
		sql.append("   AND TMV.MATERIAL_ID = TMVM.MATERIAL_ID \n");
		sql.append("   AND TMV.VEHICLE_ID = TTDED.VEHICLE_ID \n");
		sql.append("   AND TTDE.SENDCAR_HEADER_ID = TTDED.SENDCAR_HEADER_ID \n");
		sql.append("   AND TMV.WAREHOUSE_ID = TMW.WAREHOUSE_ID \n");
		sql.append("   AND TDBA.DEALER_ID = tvsdr.dealer_id \n");
		sql.append("   and ts.vin(+)=tmv.vin    \n");
		sql.append("   AND TTDED.IS_RECEIVE = " + Constant.IS_RECEIVE_0 + " \n");
		sql.append("   AND TDBA.AREA_ID = TPBA.AREA_ID  \n");
		
		
		if (null != vin && !"".equals(vin)) {
			sql.append(GetVinUtil.getVins(vin, "TMV"));
		}
		if (null != dlvNo && !"".equals(dlvNo)) {
			sql.append("   AND tvsdr.DLVRY_REQ_NO like UPPER('%" + dlvNo + "%') \n");
		}

		if (null != dlvryNo && !"".equals(dlvryNo)) {
			sql.append("   AND TTDE.SENDCAR_ORDER_NUMBER like UPPER('%" + dlvryNo + "%') \n");
		}
		sql.append("   AND TMV.LIFE_CYCLE = "+Constant.VEHICLE_LIFE_08+"\n");  //车厂在途
		sql.append("	and tvsdr.dealer_id IN ("+dealer_Id+")\n") ;
		sql.append("  and tpba.pose_id = "+poseId+"\n");
		
		sql.append("GROUP BY TMV.AREA_ID, \n");
		sql.append("         TMV.VEHICLE_ID, \n");
		sql.append("         TMV.DEALER_ID, \n");
		sql.append("         TTDED.DETAIL_ID, \n");
		sql.append("         G.GROUP_NAME, \n");
		sql.append("         TMVM.MATERIAL_CODE, \n");
		sql.append("         TMVM.MATERIAL_NAME, \n");
		sql.append("         tvsdr.dealer_id, \n");
		sql.append("         tvsdr.dealer_id, \n");
		sql.append("         TMV.VIN, \n");
		sql.append("         TMV.ENGINE_NO, \n");
		sql.append("         TTDE.SENDCAR_ORDER_NUMBER, \n");
		sql.append("         TBA.AREA_NAME, \n");
		sql.append("         TTDE.FLATCAR_ASSIGN_DATE, \n");
		sql.append("         TMW.WAREHOUSE_NAME, \n");
		sql.append("         tvsdr.DLVRY_REQ_NO, \n");
		sql.append("         TTDE.SHIP_METHOD_CODE, \n");
		sql.append("         TTDE.MOTORMAN, \n");
		sql.append("          TTDE.MOTORMAN_PHONE, \n");
		sql.append("          TMV.PRODUCT_DATE, \n");
		sql.append("          TMV.FACTORY_DATE, \n");
		sql.append("         TTDE.ARRIVE_DATE, \n");
		sql.append("         ts.cys_ssdd\n");
		sql.append(") TEMP \n");
		*/
		//sql.append("ORDER BY DELIVERY_DATE DESC, TTDED_ID ASC") ;
		List<Map<String,Object>> list=dao.pageQuery(sql.toString(), null, dao.getFunName());
		return list;
	}
	
	/***************************************************************************
	 * 查询可验收入库的车辆列表
	 * 
	 * @param :
	 * @return
	 * @throws Exception
	 */

	public static PageResult <Map<String,Object>> getCheckListVeh(String poseId,String dealer_Id,String vin, String dlvNo,String dlvryNo, int pageSize,int curPage){
		
		StringBuffer sql = new StringBuffer("");
		sql.append("SELECT TMV.VEHICLE_ID, --查询可验收入库的车辆列表\n");
		//sql.append("      TMV.DEALER_ID, --开票方经销商ID           \n");
		sql.append("      TTDED.DETAIL_ID AS TTDED_ID, --发运单明细ID\n");
		sql.append("      G.GROUP_NAME, --车型名称                   \n");
		sql.append("      TMVM.MATERIAL_CODE, --物料代码             \n");
		sql.append("      TMVM.MATERIAL_NAME, --物料名称             \n");
		sql.append("      TVDR.RECEIVER, --接收方                    \n");
		sql.append("      TVDR.dealer_id, --订货方                   \n");
		sql.append("      TMV.VIN, --VIN                             \n");
		sql.append("      TMV.ENGINE_NO, --发动机号                  \n");
		sql.append("      TTDE.SENDCAR_ORDER_NUMBER, -- 发运单号     \n");
		sql.append("      TVDR.DLVRY_REQ_NO,                         \n");
		sql.append("      TTDE.SHIP_METHOD_CODE,                     \n");
		//sql.append("     (select address from tt_sales_way_bill_address where address_id in (   \n");
		//sql.append("     		select max(address_id) from  tt_sales_way_bill_address group by  VEHICLE_ID   \n");
		//sql.append("     		) and  vehicle_id=  TMV.VEHICLE_ID) address,    \n");
		sql.append("      TTDE.MOTORMAN,                             \n");
		sql.append("      TTDE.MOTORMAN_PHONE,                       \n");
		sql.append("      TO_CHAR(TTDE.FLATCAR_ASSIGN_DATE, 'YYYY-MM-DD') AS DELIVERY_DATE,\n");
		sql.append("      TMW.WAREHOUSE_NAME,\n");
		sql.append("         TO_CHAR(TMV.PRODUCT_DATE,'YYYY-MM-DD') as PRODUCT_DATE,\n");
		sql.append("         TO_CHAR(TMV.FACTORY_DATE,'YYYY-MM-DD') as FACTORY_DATE,\n");
		sql.append("      TO_CHAR(TTDE.ARRIVE_DATE,'YYYY-MM-DD') as ARRIVE_DATE,    \n");
		sql.append("      DECODE(SIGN(TRUNC(TTDE.ARRIVE_DATE, 'DD') - (SYSDATE)), -1, 1, 0) AS IS_RED\n");
		sql.append(" FROM TM_VEHICLE              TMV,                      \n");
		sql.append("      TM_VHCL_MATERIAL_GROUP  G,                        \n");
		sql.append("      TM_VHCL_MATERIAL        TMVM,                     \n");
		sql.append("      TT_VS_DLVRY_REQ         TVDR, --发运申请          \n");
		sql.append("      TT_VS_DLVRY_ERP_DTL     TTDED, --ERP发运子表      \n");
		sql.append("      TT_VS_DLVRY_ERP         TTDE, --ERP发运主表       \n");
		//sql.append("      tt_sales_way_bill_address tswba,--车辆地址 \n");
		sql.append("      TM_WAREHOUSE            TMW                       \n");
		sql.append("WHERE TMV.MODEL_ID = G.GROUP_ID                         \n");
		sql.append("  AND TVDR.REQ_ID = TTDED.REQ_ID                        \n");
		sql.append("  AND TMV.MATERIAL_ID = TMVM.MATERIAL_ID                \n");
		sql.append("  AND TMV.VEHICLE_ID = TTDED.VEHICLE_ID                 \n");
		sql.append("  AND TTDE.SENDCAR_HEADER_ID = TTDED.SENDCAR_HEADER_ID  \n");
		sql.append("  AND TMV.WAREHOUSE_ID = TMW.WAREHOUSE_ID               \n");
		//sql.append("  AND TSWBA.ORDER_NO=TVDR.DLVRY_REQ_NO               \n");
		sql.append("   AND TTDED.IS_RECEIVE = " + Constant.IS_RECEIVE_0 + "\n");
		sql.append("   AND TMV.LIFE_CYCLE = "+Constant.VEHICLE_LIFE_08+"\n");  //车厂在途
		sql.append("	and TVDR.receiver IN ("+dealer_Id+")\n") ;
		if (null != vin && !"".equals(vin)) {
			sql.append(GetVinUtil.getVins(vin, "TMV"));
		}
		if (null != dlvNo && !"".equals(dlvNo)) {
			sql.append("   AND TVDR.DLVRY_REQ_NO like UPPER('%" + dlvNo + "%') \n");
		}

		if (null != dlvryNo && !"".equals(dlvryNo)) {
			sql.append("   AND TTDE.SENDCAR_ORDER_NUMBER like UPPER('%" + dlvryNo + "%') \n");
		}
		sql.append("ORDER BY TTDE.FLATCAR_ASSIGN_DATE DESC,  TTDED.DETAIL_ID ASC") ;
		
		PageResult<Map<String, Object>> ps=dao.pageQuery(sql.toString(), null, dao.getFunName(),pageSize,curPage);
		return ps ;
	}
	
	public PageResult<Map<String, Object>> getLogisticsList(
			Map<String, String> map, Integer pageSize, Integer curPage) {
		String vin=map.get("vin");
		StringBuilder sql= new StringBuilder();
		/*sql.append("select td.vin, th.rq,th.cys_ssdd,th.cph,th.jsyxx,th.jsydh\n" );
		sql.append("  from v_cys_ztxx@dms2bcask th, v_cys_wlmx@dms2bcask td\n" );
		sql.append(" where th.ydh = td.ydh\n" );
		sql.append("   and td.vin = '"+vin+"'\n" );
		sql.append("   order by th.rq desc ");*/
		sql.append("select * from tt_sales_way_bill_address where vin='"+vin+"' order by address_date desc");
		PageResult<Map<String, Object>> ps=dao.pageQuery(sql.toString(), null, dao.getFunName(),pageSize,curPage);
		return ps ;
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
	
	public void vehicleShiftLibrary(String vehicleIds, String warehouseId) {
		StringBuffer sql = new StringBuffer("\n");

		sql.append("UPDATE TM_VEHICLE TM SET TM.VEHICLE_AREA = '" + warehouseId + "' WHERE TM.VEHICLE_ID IN (" + vehicleIds + ")");
		super.update(sql.toString(), null);
	}
}
