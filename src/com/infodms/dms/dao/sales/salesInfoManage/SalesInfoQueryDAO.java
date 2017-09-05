package com.infodms.dms.dao.sales.salesInfoManage;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.common.tag.DaoFactory;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.dao.sales.ordermanage.reqquery.ReqQueryDao;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.XHBUtil;
import com.infodms.dms.util.businessUtil.GetVinUtil;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

import flex.messaging.util.StringUtils;

/**
 * @Title : SalesInfoQueryDAO.java
 * @Package: com.infodms.dms.dao.sales.salesInfoManage
 * @Description: 实销信息查询DAO
 * @date : 2010-6-30
 * @version: V1.0
 */
public class SalesInfoQueryDAO extends BaseDao {
	public static Logger logger = Logger.getLogger(SalesInfoQueryDAO.class);
	private static final SalesInfoQueryDAO dao = new SalesInfoQueryDAO();

	public static final SalesInfoQueryDAO getInstance() {
		return dao;
	}
	private SalesInfoQueryDAO() {}
	
	/***
	 * 实销信息列表查询
	 * */
	@SuppressWarnings("unchecked")
	public static PageResult<Map<String, Object>> queryReportInfoTableList(Map<String,String> map,int pageSize, int curPage) {
		String dealerSql = (String)map.get("dealerSql");  //经销商的sql
		String customer_type = (String) map.get("customer_type"); 	// 客户类型
		String customer_name = (String) map.get("customer_name"); 	// 客户名称
		String vin = (String) map.get("vin"); 						// VIN
		String customer_phone = (String) map.get("customer_phone"); // 客户电话
		String materialCode = (String) map.get("materialCode"); 	// 选择物料组
		String startDate = (String) map.get("startDate"); 			// 上报日期:开始
		String endDate = (String) map.get("endDate"); 				// 上报日期 ：截止
		String dealerCode = (String) map.get("dealerCode"); 		// 经销商代码
		String payment = (String) map.get("payment"); 		// 购置方式
		String vchlPro = (String) map.get("vchlPro"); 		// 车辆性质
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT  TTS.ORDER_ID,TMD.DEALER_CODE, --经销商代码   \n");
		sql.append("		  TMD.DEALER_SHORTNAME, --经销商名称   \n");
		sql.append("       TMD.CREATE_DATE AS DEALER_CREATE_DATE, --系统开通时间   \n");
		sql.append("       TMD.DEALER_ID,    \n");
		sql.append("       TTC.CTM_NAME, --客户名称   \n");
		sql.append("       TTC.IS_SECOND, --是否二手车置换   \n");
		sql.append("       TTC.CTM_TYPE, --客户类型   \n");
		sql.append("       TTS.IS_FLEET, --是否集团客户   \n");
		sql.append("       TF.FLEET_NAME, --集团客户名称   \n");
		sql.append("       TFC.CONTRACT_NO, --集团客户合同   \n");
		sql.append("       TTC.MAIN_PHONE, --主要联系电话   \n");
		sql.append("       M.MATERIAL_CODE, --物料代码   \n");
		sql.append("       M.MATERIAL_NAME, --物料名称   \n");
		sql.append("       TVMG2.GROUP_NAME VT, --车型名称   \n");
		sql.append("       TVMG3.GROUP_NAME VS, --车系名称   \n");
		sql.append("       TVMG.Group_Name SN, --状态名称   \n");
		sql.append("       TMV.COLOR, --颜色名称   \n");
		sql.append("       TMV.VIN, --VIN   \n");
		sql.append("       TTS.LOANS_YEAR,    \n");
		sql.append("       TC5.CODE_DESC AS LOANS_TYPE,   \n");
		sql.append("       TTS.CAR_CHARACTOR, --车辆性质   \n");
		sql.append("       TTS.SHOUFU_RATIO,     \n");
		sql.append("       TTS.MORTGAGE_TYPE,    \n");
		sql.append("       TC4.CODE_DESC AS   PAYMENT,TTS.PRICE,   \n");
		sql.append("       TMV.ENGINE_NO, --VIN   \n");
		sql.append("       TO_CHAR(TMV.PRODUCT_DATE, 'yyyy-MM-dd') PRODUCT_DATE ,   \n");
		sql.append("       TO_CHAR(TTS.SALES_DATE, 'yyyy-MM-dd') SALES_DATE, --实销时间   \n");
		sql.append("       TTC.CTM_ID,   \n");
		sql.append("       TMV.VEHICLE_ID,   \n");
		sql.append("       VOD.ROOT_ORG_NAME ORG_NAME,VOD.REGION_NAME REGION_NAME   \n");
		sql.append("FROM TT_DEALER_ACTUAL_SALES TTS,   \n");
		sql.append("       TM_DEALER              TMD,   \n");
		sql.append("       TT_CUSTOMER            TTC,   \n");
		sql.append("       TM_VEHICLE             TMV,   \n");
		sql.append("       TM_VHCL_MATERIAL       M,   \n");
		sql.append("       TM_VHCL_MATERIAL_GROUP TVMG,   \n");
		sql.append("       TM_VHCL_MATERIAL_GROUP TVMG2,   \n");
		sql.append("       TM_VHCL_MATERIAL_GROUP TVMG3,   \n");
		sql.append("       TM_VHCL_MATERIAL_GROUP_R TVMGR,   \n");
		sql.append("       TM_FLEET               TF,   \n");
		sql.append("       TT_FLEET_CONTRACT      TFC,   \n");
		sql.append("       VW_ORG_DEALER VOD,   \n");
		sql.append("       TC_CODE TC4,   \n");
		sql.append("       TC_CODE TC5    \n");
		sql.append("WHERE TTS.DEALER_ID = TMD.DEALER_ID   \n");
		sql.append("   AND VOD.DEALER_ID=TMD.DEALER_ID    \n");
		sql.append("   AND TTS.PAYMENT= TC4.CODE_ID(+) AND TTS.LOANS_TYPE = TC5.CODE_ID(+)   \n");
		sql.append("   AND TTS.CTM_ID = TTC.CTM_ID AND TTS.VEHICLE_ID = TMV.VEHICLE_ID   \n");
		sql.append("   AND TMV.MATERIAL_ID = M.MATERIAL_ID AND TTS.FLEET_ID = TF.FLEET_ID(+)    \n");
		sql.append("   AND TTS.CONTRACT_ID = TFC.CONTRACT_ID(+)   \n");
		sql.append("   AND M.MATERIAL_ID = TVMGR.MATERIAL_ID   \n");
		sql.append("   AND TVMGR.GROUP_ID = TVMG.GROUP_ID   \n");
		sql.append("   AND TVMG.PARENT_GROUP_ID = TVMG2.GROUP_ID   \n");
		sql.append("   AND TVMG2.PARENT_GROUP_ID = TVMG3.GROUP_ID   \n");
		sql.append("   AND TMV.LIFE_CYCLE = 10321004   \n");
		if (!CommonUtils.isEmpty(startDate)) {
			sql.append("   AND TTS.SALES_DATE >= TO_DATE('"+startDate+" 00:00:00','yyyy-MM-dd HH24:MI:SS')   \n");
		}
		if (!CommonUtils.isEmpty(endDate)) {
			sql.append("   AND TTS.SALES_DATE  <= TO_DATE('"+endDate+" 23:59:59','yyyy-MM-dd HH24:MI:SS')   \n");
		}
		sql.append("   and exists ("+dealerSql+"  and v.dealer_id = TMD.dealer_id )   \n");
		if (!CommonUtils.isEmpty(dealerCode)) {
			sql.append("  and tmd.dealer_code='"+dealerCode+"'  \n");
		}
		if (!CommonUtils.isEmpty(customer_type)) {
			sql.append(" and ttc.CTM_TYPE="+customer_type+"  \n");
		}
		if (!CommonUtils.isEmpty(customer_name)) {
			sql.append("   AND ttc.CTM_NAME LIKE '%"+customer_name.trim()+"%'	\n");
		}
		if (!CommonUtils.isEmpty(customer_phone)) {
			sql.append("   AND TTC.MAIN_PHONE LIKE '%"+customer_phone.trim()+"%'\n");
		}
		if (!CommonUtils.isEmpty(vin)) {
			sql.append(" AND TMV.VIN='"+vin+"'  \n");	
		}
		if (!CommonUtils.isEmpty(payment)) {
			sql.append(" AND TTS.PAYMENT="+payment+" \n");	
		}
		if (!CommonUtils.isEmpty(vchlPro)) {
			sql.append(" AND TTS.CAR_CHARACTOR="+vchlPro+"  \n");	
		}
		if (!CommonUtils.isEmpty(materialCode)) {
			String[] materialCodes = materialCode.split(",");
			if (null != materialCodes && materialCodes.length > 0) {
				StringBuffer buffer = new StringBuffer();
				for (int i = 0; i < materialCodes.length; i++) {
					buffer.append("'").append(materialCodes[i]).append("'").append(",");
				}
				buffer = buffer.deleteCharAt(buffer.length() - 1);
				sql.append("   AND ((TMV.PACKAGE_ID IN (SELECT G.GROUP_ID\n");
				sql.append("FROM TM_VHCL_MATERIAL_GROUP G \n");
				sql.append("WHERE G.GROUP_CODE IN ("+ buffer.toString() + ")))\n");
				sql.append("       OR (TMV.MODEL_ID IN (SELECT G.GROUP_ID \n");
				sql.append("                             FROM TM_VHCL_MATERIAL_GROUP G \n");
				sql.append("                            WHERE G.GROUP_CODE IN ("+ buffer.toString() + "))) \n");
				sql.append("       OR (TMV.SERIES_ID IN (SELECT G.GROUP_ID \n");
				sql.append("                              FROM TM_VHCL_MATERIAL_GROUP G \n");
				sql.append("                             WHERE G.GROUP_CODE IN ("+ buffer.toString() + ")))) \n");
			}
		}
		return dao.pageQuery(sql.toString(), null, dao.getFunName(), pageSize,curPage);
	}
	
	/***
	 * 实销信息列表查询
	 * */
	@SuppressWarnings("unchecked")
	public static List<Map<String, Object>> queryReportInfoTableDownLoad(Map<String,String> map) {
		String dealerSql = (String)map.get("dealerSql");  //经销商的sql
		String customer_type = (String) map.get("customer_type"); 	// 客户类型
		String customer_name = (String) map.get("customer_name"); 	// 客户名称
		String vin = (String) map.get("vin"); 						// VIN
		String customer_phone = (String) map.get("customer_phone"); // 客户电话
		String materialCode = (String) map.get("materialCode"); 	// 选择物料组
		String startDate = (String) map.get("startDate"); 			// 上报日期:开始
		String endDate = (String) map.get("endDate"); 				// 上报日期 ：截止
		String dealerCode = (String) map.get("dealerCode"); 		// 经销商代码
		String payment = (String) map.get("payment"); 		// 购置方式
		String vchlPro = (String) map.get("vchlPro"); 		// 车辆性质
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT  TMD.DEALER_CODE, --经销商代码   \n");
		sql.append("		  TMD.DEALER_SHORTNAME, --经销商名称   \n");
		sql.append("       TMD.CREATE_DATE AS DEALER_CREATE_DATE, --系统开通时间   \n");
		sql.append("       TMD.DEALER_ID,    \n");
		sql.append("       TTC.CTM_NAME, --客户名称   \n");
		sql.append("       TTC.IS_SECOND, --是否二手车置换   \n");
		sql.append("       TTC.CTM_TYPE, --客户类型   \n");
		sql.append("       TTS.IS_FLEET, --是否集团客户   \n");
		sql.append("       TF.FLEET_NAME, --集团客户名称   \n");
		sql.append("       TFC.CONTRACT_NO, --集团客户合同   \n");
		sql.append("       TTC.MAIN_PHONE, --主要联系电话   \n");
		sql.append("       M.MATERIAL_CODE, --物料代码   \n");
		sql.append("       M.MATERIAL_NAME, --物料名称   \n");
		sql.append("       TVMG2.GROUP_NAME VT, --车型名称   \n");
		sql.append("       TVMG3.GROUP_NAME VS, --车系名称   \n");
		sql.append("       TVMG.Group_Name SN, --状态名称   \n");
		sql.append("       TMV.COLOR, --颜色名称   \n");
		sql.append("       TMV.VIN, --VIN   \n");
		sql.append("       TTS.LOANS_YEAR,    \n");
		sql.append("       TC5.CODE_DESC AS LOANS_TYPE,   \n");
		sql.append("       TTS.CAR_CHARACTOR, --车辆性质   \n");
		sql.append("       TTS.SHOUFU_RATIO,     \n");
		sql.append("       TTS.MORTGAGE_TYPE,    \n");
		sql.append("       TC4.CODE_DESC AS   PAYMENT,TTS.PRICE,   \n");
		sql.append("       TMV.ENGINE_NO, --VIN   \n");
		sql.append("       TO_CHAR(TMV.PRODUCT_DATE, 'yyyy-MM-dd') PRODUCT_DATE ,   \n");
		sql.append("       TO_CHAR(TTS.SALES_DATE, 'yyyy-MM-dd') SALES_DATE, --实销时间   \n");
		sql.append("       TTC.CTM_ID,   \n");
		sql.append("       TMV.VEHICLE_ID,   \n");
		sql.append("       VOD.ROOT_ORG_NAME ORG_NAME,VOD.REGION_NAME REGION_NAME   \n");
		sql.append("FROM TT_DEALER_ACTUAL_SALES TTS,   \n");
		sql.append("       TM_DEALER              TMD,   \n");
		sql.append("       TT_CUSTOMER            TTC,   \n");
		sql.append("       TM_VEHICLE             TMV,   \n");
		sql.append("       TM_VHCL_MATERIAL       M,   \n");
		sql.append("       TM_VHCL_MATERIAL_GROUP TVMG,   \n");
		sql.append("       TM_VHCL_MATERIAL_GROUP TVMG2,   \n");
		sql.append("       TM_VHCL_MATERIAL_GROUP TVMG3,   \n");
		sql.append("       TM_VHCL_MATERIAL_GROUP_R TVMGR,   \n");
		sql.append("       TM_FLEET               TF,   \n");
		sql.append("       TT_FLEET_CONTRACT      TFC,   \n");
		sql.append("       VW_ORG_DEALER VOD,   \n");
		sql.append("       TC_CODE TC4,   \n");
		sql.append("       TC_CODE TC5    \n");
		sql.append("WHERE TTS.DEALER_ID = TMD.DEALER_ID   \n");
		sql.append("   AND VOD.DEALER_ID=TMD.DEALER_ID    \n");
		sql.append("   AND TTS.PAYMENT= TC4.CODE_ID(+) AND TTS.LOANS_TYPE = TC5.CODE_ID(+)   \n");
		sql.append("   AND TTS.CTM_ID = TTC.CTM_ID AND TTS.VEHICLE_ID = TMV.VEHICLE_ID   \n");
		sql.append("   AND TMV.MATERIAL_ID = M.MATERIAL_ID AND TTS.FLEET_ID = TF.FLEET_ID(+)    \n");
		sql.append("   AND TTS.CONTRACT_ID = TFC.CONTRACT_ID(+)   \n");
		sql.append("   AND M.MATERIAL_ID = TVMGR.MATERIAL_ID   \n");
		sql.append("   AND TVMGR.GROUP_ID = TVMG.GROUP_ID   \n");
		sql.append("   AND TVMG.PARENT_GROUP_ID = TVMG2.GROUP_ID   \n");
		sql.append("   AND TVMG2.PARENT_GROUP_ID = TVMG3.GROUP_ID   \n");
		sql.append("   AND TMV.LIFE_CYCLE = 10321004   \n");
		if (!CommonUtils.isEmpty(startDate)) {
			sql.append("   AND TTS.SALES_DATE >= TO_DATE('"+startDate+" 00:00:00','yyyy-MM-dd HH24:MI:SS')   \n");
		}
		if (!CommonUtils.isEmpty(endDate)) {
			sql.append("   AND TTS.SALES_DATE  <= TO_DATE('"+endDate+" 23:59:59','yyyy-MM-dd HH24:MI:SS')   \n");
		}
		sql.append("   and exists ("+dealerSql+"  and v.dealer_id = TMD.dealer_id )   \n");
		if (!CommonUtils.isEmpty(dealerCode)) {
			sql.append("  and tmd.dealer_code='"+dealerCode+"'  \n");
		}
		if (!CommonUtils.isEmpty(customer_type)) {
			sql.append(" and ttc.CTM_TYPE="+customer_type+"  \n");
		}
		if (!CommonUtils.isEmpty(customer_name)) {
			sql.append("   AND ttc.CTM_NAME LIKE '%"+customer_name.trim()+"%'	\n");
		}
		if (!CommonUtils.isEmpty(customer_phone)) {
			sql.append("   AND TTC.MAIN_PHONE LIKE '%"+customer_phone.trim()+"%'\n");
		}
		if (!CommonUtils.isEmpty(vin)) {
			sql.append(" AND TMV.VIN='"+vin+"'  \n");	
		}
		if (!CommonUtils.isEmpty(payment)) {
			sql.append(" AND TTS.PAYMENT="+payment+" \n");	
		}
		if (!CommonUtils.isEmpty(vchlPro)) {
			sql.append(" AND TTS.CAR_CHARACTOR="+vchlPro+"  \n");	
		}
		if (!CommonUtils.isEmpty(materialCode)) {
			String[] materialCodes = materialCode.split(",");
			if (null != materialCodes && materialCodes.length > 0) {
				StringBuffer buffer = new StringBuffer();
				for (int i = 0; i < materialCodes.length; i++) {
					buffer.append("'").append(materialCodes[i]).append("'").append(",");
				}
				buffer = buffer.deleteCharAt(buffer.length() - 1);
				sql.append("   AND ((TMV.PACKAGE_ID IN (SELECT G.GROUP_ID\n");
				sql.append("FROM TM_VHCL_MATERIAL_GROUP G \n");
				sql.append("WHERE G.GROUP_CODE IN ("+ buffer.toString() + ")))\n");
				sql.append("       OR (TMV.MODEL_ID IN (SELECT G.GROUP_ID \n");
				sql.append("                             FROM TM_VHCL_MATERIAL_GROUP G \n");
				sql.append("                            WHERE G.GROUP_CODE IN ("+ buffer.toString() + "))) \n");
				sql.append("       OR (TMV.SERIES_ID IN (SELECT G.GROUP_ID \n");
				sql.append("                              FROM TM_VHCL_MATERIAL_GROUP G \n");
				sql.append("                             WHERE G.GROUP_CODE IN ("+ buffer.toString() + ")))) \n");
			}
		}
		return dao.pageQuery(sql.toString(), null, dao.getFunName());
	}

	/**
	 * @Title : queryReportInfoList
	 * @Description: 实销信息查询结果展示
	 * @return : PageResult<Map<String,Object>>
	 * @throws
	 * @LastUpdate :2010-6-30
	 */
	public static PageResult<Map<String, Object>> queryReportInfoList(Map<String,String> map,int pageSize, int curPage) {
		String customer_type = (String) map.get("customer_type"); 	// 客户类型
		String customer_name = (String) map.get("customer_name"); 	// 客户名称
		String vin = (String) map.get("vin"); 						// VIN
		String customer_phone = (String) map.get("customer_phone"); // 客户电话
		String materialCode = (String) map.get("materialCode"); 	// 选择物料组
		String is_fleet = (String) map.get("is_fleet"); 			// 是否集团客户
		String fleet_name = (String) map.get("fleet_name"); 		// 集团客户名称
		String contract_no = (String) map.get("contract_no"); 		// 集团客户合同
		String startDate = (String) map.get("startDate"); 			// 上报日期:开始
		String endDate = (String) map.get("endDate"); 				// 上报日期 ：截止
		String dealerCode = (String) map.get("dealerCode"); 		// 经销商代码
		String orgCode=(String)map.get("orgCode");					// 区域
		String areaId = (String)map.get("areaId");					// 产地
		String orgId = map.get("orgId") ;
		String poseId = map.get("poseId");
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT XX.DEALER_CODE,\n");
		sql.append("XX.DEALER_SHORTNAME,\n");
		sql.append("XX.CTM_NAME,\n");
		sql.append("xx.is_second,\n");
		sql.append("XX.CTM_TYPE,\n");
		sql.append("XX.IS_FLEET,\n");
		sql.append("XX.FLEET_NAME,\n");
		sql.append("XX.CONTRACT_NO, \n");
		sql.append("XX.MAIN_PHONE,\n");
		sql.append("XX.MATERIAL_CODE,\n");
		sql.append("XX.MATERIAL_NAME,\n");
		sql.append("XX.VT, --车型名称\n");
		sql.append("XX.VS, --车系名称\n");  
		sql.append("XX.SN, --状态名称\n");  
		sql.append("XX.COLOR,  --颜色名称\n");
		sql.append("XX.VIN,\n");
		sql.append("XX.CAR_CHARACTOR,\n");
		sql.append("XX.ENGINE_NO,\n");
		sql.append("XX.PRODUCT_DATE ,\n");
		sql.append("XX.SALES_DATE,\n");
		sql.append("XX.CTM_ID,\n");
		sql.append("XX.VEHICLE_ID,\n");
		sql.append("XX.ORG_NAME,\n");
		sql.append("XX.LOANS_TYPE,XX.LOANS_YEAR,\n");
		sql.append("(XX.SHOUFU_RATIO*100) as SHOUFU_RATIO,(SELECT CODE_DESC FROM TC_CODE WHERE CODE_ID=XX.MORTGAGE_TYPE) AS MORTGAGE_TYPE,XX.PAYMENT,XX.PRICE ,");
		sql.append("XX.DEALER_CREATE_DATE,\n");
		sql.append(" XX.REGION_NAME  FROM (\n");
		sql.append("SELECT TMD.DEALER_CODE, --经销商代码\n");
		sql.append("       TMD.DEALER_SHORTNAME, --经销商名称\n");  
		sql.append("       TMD.CREATE_DATE AS DEALER_CREATE_DATE, --系统开通时间\n"); 
		sql.append("       TMD.DEALER_ID, \n");  
		sql.append("       TTC.CTM_NAME, --客户名称\n");  
		sql.append("       TTC.is_second, --是否二手车置换\n");  
		sql.append("       TTC.CTM_TYPE, --客户类型\n");  
		sql.append("       TTS.IS_FLEET, --是否集团客户\n");  
		sql.append("       TF.FLEET_NAME, --集团客户名称\n");  
		sql.append("       TFC.CONTRACT_NO, --集团客户合同\n");  
		sql.append("       TTC.MAIN_PHONE, --主要联系电话\n");  
		sql.append("       M.MATERIAL_CODE, --物料代码\n");  
		sql.append("       M.MATERIAL_NAME, --物料名称\n"); 
		sql.append("	   TVMG2.GROUP_NAME VT, --车型名称\n");
		sql.append("       TVMG3.GROUP_NAME VS, --车系名称\n");  
		sql.append("       TVMG.Group_Name SN, --状态名称\n");  
		sql.append("       TMV.COLOR, --颜色名称\n");
		sql.append("       TMV.VIN, --VIN\n"); 
		sql.append("       TTS.LOANS_YEAR,       TC5.CODE_DESC AS LOANS_TYPE,\n"); 
		sql.append("TTS.CAR_CHARACTOR, --车辆性质\n");
		sql.append("  TTS.SHOUFU_RATIO,         TTS.MORTGAGE_TYPE,     TC4.CODE_DESC AS   PAYMENT,TTS.PRICE,\n");
		sql.append("       TMV.ENGINE_NO, --VIN\n"); 
		sql.append("	   TO_CHAR(TMV.PRODUCT_DATE, 'yyyy-MM-dd') PRODUCT_DATE ,\n");
		sql.append("       TO_CHAR(TTS.SALES_DATE, 'yyyy-MM-dd') SALES_DATE, --实销时间\n");  
		sql.append("       TTC.CTM_ID,\n");
		sql.append("       TMV.VEHICLE_ID,\n");
		sql.append("       VOD.ROOT_ORG_NAME ORG_NAME,VOD.REGION_NAME REGION_NAME \n");
		sql.append("  FROM TT_DEALER_ACTUAL_SALES TTS,\n");  
		sql.append("       TM_DEALER              TMD,\n");  
		sql.append("       TT_CUSTOMER            TTC,\n");  
		sql.append("       TM_VEHICLE             TMV,\n");  
		sql.append("       TM_VHCL_MATERIAL       M,\n");
		sql.append("	   TM_VHCL_MATERIAL_GROUP TVMG,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP TVMG2,\n");  
		sql.append("       TM_VHCL_MATERIAL_GROUP TVMG3,\n");  
		sql.append("       TM_VHCL_MATERIAL_GROUP_R TVMGR,\n");
		sql.append("       TM_FLEET               TF,\n");  
		sql.append("       TT_FLEET_CONTRACT      TFC,VW_ORG_DEALER VOD,TC_CODE TC4,TC_CODE TC5 \n");  
		sql.append(" WHERE TTS.DEALER_ID = TMD.DEALER_ID\n");  
		sql.append("   AND VOD.DEALER_ID=TMD.DEALER_ID   AND TTS.PAYMENT= TC4.CODE_ID(+) AND TTS.LOANS_TYPE = TC5.CODE_ID(+)\n");
//		sql.append("   AND TC1.CODE_ID = TTC.CTM_TYPE \n");
//		sql.append("   AND TC2.CODE_ID = TTS.IS_FLEET");
		sql.append("   AND TTS.CTM_ID = TTC.CTM_ID\n");  
		sql.append("   AND TTS.VEHICLE_ID = TMV.VEHICLE_ID\n");  
		sql.append("   AND TMV.MATERIAL_ID = M.MATERIAL_ID\n");  
		sql.append("   AND TTS.FLEET_ID = TF.FLEET_ID(+)\n");  
		sql.append("   AND TTS.IS_RETURN='"+Constant.IF_TYPE_NO+"'");
		sql.append("   AND TTS.CONTRACT_ID = TFC.CONTRACT_ID(+)\n");  
		sql.append(MaterialGroupManagerDao.getPoseIdBusinessSql("TMV.YIELDLY", poseId));
		if (!"".equals(customer_type)) {
			sql.append("   AND TTC.CTM_TYPE = "+customer_type+"\n");
		}
		if (!"".equals(customer_name)) {
			sql.append("   AND TTC.CTM_NAME LIKE '%"+customer_name.trim()+"%'\n");
		}
		if (!"".equals(vin)) {
			sql.append(GetVinUtil.getVins(vin, "TMV"));
		}
		if (!"".equals(customer_phone)) {
			sql.append("   AND TTC.MAIN_PHONE LIKE '%"+customer_phone.trim()+"%'\n");
		}
		if (null != materialCode && !"".equals(materialCode)) {
			String[] materialCodes = materialCode.split(",");
			if (null != materialCodes && materialCodes.length > 0) {
				StringBuffer buffer = new StringBuffer();
				for (int i = 0; i < materialCodes.length; i++) {
					buffer.append("'").append(materialCodes[i]).append("'").append(",");
				}
				buffer = buffer.deleteCharAt(buffer.length() - 1);
				sql.append("   AND ((TMV.PACKAGE_ID IN (SELECT G.GROUP_ID\n");
				sql.append("FROM TM_VHCL_MATERIAL_GROUP G \n");
				sql.append("WHERE G.GROUP_CODE IN ("+ buffer.toString() + ")))\n");
				sql.append("       OR (TMV.MODEL_ID IN (SELECT G.GROUP_ID \n");
				sql.append("                             FROM TM_VHCL_MATERIAL_GROUP G \n");
				sql.append("                            WHERE G.GROUP_CODE IN ("+ buffer.toString() + "))) \n");
				sql.append("       OR (TMV.SERIES_ID IN (SELECT G.GROUP_ID \n");
				sql.append("                              FROM TM_VHCL_MATERIAL_GROUP G \n");
				sql.append("                             WHERE G.GROUP_CODE IN ("+ buffer.toString() + ")))) \n");
			}
		}
		if (!"".equals(is_fleet)) {
			sql.append("   AND TTS.IS_FLEET = "+is_fleet+"\n");
		}
		if (!"".equals(fleet_name)) {
			sql.append("   AND TF.FLEET_NAME LIKE '%"+fleet_name+"%'\n");
		}
		if (!"".equals(contract_no)) {
			sql.append("   AND TFC.CONTRACT_NO LIKE '%"+contract_no+"%'\n");
		}
		if (null != startDate && !"".equals(startDate)) {
			sql.append("   AND TTS.SALES_DATE >= TO_DATE('"+ startDate+" 00:00:00','yyyy-MM-dd HH24:MI:SS')\n");
		}
		if (null != endDate && !"".equals(endDate)) {
			sql.append("   AND TTS.SALES_DATE  <= TO_DATE('"+endDate+" 23:59:59','yyyy-MM-dd HH24:MI:SS')\n");
		}
		if (null != dealerCode && !"".equals(dealerCode)) {
			String[] array = dealerCode.split(",");
			sql.append("   AND TMD.DEALER_CODE IN(\n");
			for (int i = 0; i < array.length; i++) {
				sql.append("'" + array[i] + "'");
				if (i != array.length - 1) {
					sql.append(",");
				}
			}
			sql.append(")\n");
		}
		if (!"".equals(orgCode)) {
			sql.append("AND TMD.DEALER_ID IN\n");
			sql.append("       (SELECT TD.DEALER_ID\n");  
			sql.append("          FROM TM_DEALER TD\n");  
			sql.append("         START WITH TD.DEALER_ID IN\n");  
			sql.append("                    (SELECT TDOR.DEALER_ID\n");  
			sql.append("                       FROM TM_DEALER_ORG_RELATION TDOR\n");  
			sql.append("                      WHERE TDOR.ORG_ID IN\n");  
			sql.append("                            (SELECT ORG_ID\n");  
			sql.append("                               FROM TM_ORG ORG\n");  
			sql.append("                              WHERE ORG.STATUS = "+Constant.STATUS_ENABLE+"\n");  
			sql.append("                              START WITH ORG_CODE = '"+orgCode+"'\n");  
			sql.append("                             CONNECT BY PRIOR ORG.ORG_ID = ORG.PARENT_ORG_ID))\n");  
			sql.append("        CONNECT BY PRIOR TD.DEALER_ID = TD.PARENT_DEALER_D)\n");

		}
		
		if(!CommonUtils.isNullString(orgId)) {
			sql.append(ReqQueryDao.limitOrg("TMD", "dealer_id", orgId)) ;
		}
		if(null != areaId && !"".equals(areaId)){
			sql.append("   AND TMV.YIELDLY in ("+areaId+")\n");
		}	
		/*if (null != areaId && !"".equals(areaId)) {
			sql.append("           and exists\n");  
			sql.append("         (select 1\n");  
			sql.append("                  from tm_dealer_business_area tdba\n");  
			sql.append("                 where 1 = 1\n");  
			sql.append("                   and tdba.dealer_id = TMD.dealer_id\n");  
			sql.append("                   and tdba.area_id IN (").append(areaId).append("))\n");  
		}*/
		sql.append("   AND TMV.LIFE_CYCLE = "+Constant.VEHICLE_LIFE_04+"\n");  
		sql.append("   AND M.MATERIAL_ID = TVMGR.MATERIAL_ID\n");
		sql.append("   AND TVMGR.GROUP_ID = TVMG.GROUP_ID\n");  
		sql.append("   AND M.MODEL_CODE = TVMG2.GROUP_CODE\n");  
		sql.append("   AND TVMG2.PARENT_GROUP_ID = TVMG3.GROUP_ID  \n");

		sql.append(" UNION \n");
		sql.append("SELECT TMD.DEALER_CODE, --经销商代码\n");
		sql.append("       TMD.DEALER_SHORTNAME, --经销商名称\n"); 
		sql.append("       TMD.CREATE_DATE AS DEALER_CREATE_DATE, --系统开通时间\n"); 
		sql.append("       TMD.DEALER_ID, \n");  
		sql.append("       TTC.CTM_NAME, --客户名称\n");  
		sql.append("       TTC.is_second, --是否二手车置换\n");  
		sql.append("       TTC.CTM_TYPE, --客户类型\n");  
		sql.append("       TTS.IS_FLEET, --是否集团客户\n");  
		sql.append("       TCP.PACT_NAME FLEET_NAME, --集团客户名称\n");  
		sql.append("       TCP.PACT_NAME CONTRACT_NO, --集团客户合同\n");  
		sql.append("       TTC.MAIN_PHONE, --主要联系电话\n");  
		sql.append("       M.MATERIAL_CODE, --物料代码\n");  
		sql.append("       M.MATERIAL_NAME, --物料名称\n");
		sql.append("	   TVMG2.GROUP_NAME VT,    --车型名称\n");
		sql.append("       TVMG3.GROUP_NAME VS,   --车系名称\n");  
		sql.append("       TVMG.Group_Name SN, --状态名称\n");  
		sql.append("       TMV.COLOR,       --颜色名称\n");
		sql.append("       TMV.VIN, --VIN\n"); 
		sql.append("       TTS.LOANS_YEAR,       TC5.CODE_DESC AS LOANS_TYPE,\n"); 
		sql.append("TTS.CAR_CHARACTOR, --车辆性质\n");
		sql.append("  TTS.SHOUFU_RATIO,         TTS.MORTGAGE_TYPE,     TC4.CODE_DESC AS   PAYMENT,TTS.PRICE,\n");
		sql.append("       TMV.ENGINE_NO, --VIN\n"); 
		sql.append("	   TO_CHAR(TMV.PRODUCT_DATE, 'yyyy-MM-dd') PRODUCT_DATE ,\n");
		sql.append("       TO_CHAR(TTS.SALES_DATE, 'yyyy-MM-dd') SALES_DATE, --实销时间\n");  
		sql.append("       TTC.CTM_ID,\n");
		sql.append("       TMV.VEHICLE_ID,\n");
		sql.append("       VOD.ROOT_ORG_NAME ORG_NAME,VOD.REGION_NAME REGION_NAME \n");
		sql.append("  FROM TT_DEALER_ACTUAL_SALES TTS,\n");  
		sql.append("       TM_DEALER              TMD,\n");  
		sql.append("       TT_CUSTOMER            TTC,\n");  
		sql.append("       TM_VEHICLE             TMV,\n");  
		sql.append("       TM_VHCL_MATERIAL       M,\n");  
		sql.append("	   TM_VHCL_MATERIAL_GROUP TVMG,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP TVMG2,\n");  
		sql.append("       TM_VHCL_MATERIAL_GROUP TVMG3,\n");  
		sql.append("       TM_VHCL_MATERIAL_GROUP_R TVMGR,\n");
		sql.append("       TM_FLEET               TF,\n");  
		sql.append("       TT_FLEET_CONTRACT      TFC,TM_COMPANY_PACT TCP,VW_ORG_DEALER VOD,TC_CODE TC4,TC_CODE TC5 \n");  
		sql.append(" WHERE TTS.DEALER_ID = TMD.DEALER_ID\n");  
		sql.append("   AND VOD.DEALER_ID=TMD.DEALER_ID AND  TTS.PAYMENT= TC4.CODE_ID(+)\n");
		sql.append("    AND TTS.LOANS_TYPE = TC5.CODE_ID(+)\n");  
		sql.append("   AND TTS.CTM_ID = TTC.CTM_ID\n");  
		sql.append("   AND TTS.VEHICLE_ID = TMV.VEHICLE_ID\n");  
		sql.append("   AND TMV.MATERIAL_ID = M.MATERIAL_ID\n");  
		sql.append("   AND TTS.FLEET_ID = TCP.PACT_ID\n");  
		sql.append("   AND TTS.IS_RETURN='"+Constant.IF_TYPE_NO+"'");
		sql.append("   AND TTS.CONTRACT_ID = TFC.CONTRACT_ID(+)\n");  
		sql.append(MaterialGroupManagerDao.getPoseIdBusinessSql("TMV.YIELDLY", poseId));
		if (!"".equals(customer_type)) {
			sql.append("   AND TTC.CTM_TYPE = "+customer_type+"\n");
		}
		if (!"".equals(customer_name)) {
			sql.append("   AND TTC.CTM_NAME LIKE '%"+customer_name.trim()+"%'\n");
		}
		if (!"".equals(vin)) {
			sql.append(GetVinUtil.getVins(vin, "TMV"));
		}
		if (!"".equals(customer_phone)) {
			sql.append("   AND TTC.MAIN_PHONE LIKE '%"+customer_phone.trim()+"%'\n");
		}
		if (null != materialCode && !"".equals(materialCode)) {
			String[] materialCodes = materialCode.split(",");
			if (null != materialCodes && materialCodes.length > 0) {
				StringBuffer buffer = new StringBuffer();
				for (int i = 0; i < materialCodes.length; i++) {
					buffer.append("'").append(materialCodes[i]).append("'").append(",");
				}
				buffer = buffer.deleteCharAt(buffer.length() - 1);
				sql.append("   AND ((TMV.PACKAGE_ID IN (SELECT G.GROUP_ID\n");
				sql.append("FROM TM_VHCL_MATERIAL_GROUP G \n");
				sql.append("WHERE G.GROUP_CODE IN ("+ buffer.toString() + ")))\n");
				sql.append("       OR (TMV.MODEL_ID IN (SELECT G.GROUP_ID \n");
				sql.append("                             FROM TM_VHCL_MATERIAL_GROUP G \n");
				sql.append("                            WHERE G.GROUP_CODE IN ("+ buffer.toString() + "))) \n");
				sql.append("       OR (TMV.SERIES_ID IN (SELECT G.GROUP_ID \n");
				sql.append("                              FROM TM_VHCL_MATERIAL_GROUP G \n");
				sql.append("                             WHERE G.GROUP_CODE IN ("+ buffer.toString() + ")))) \n");
			}
		}
		if (!"".equals(is_fleet)) {
			sql.append("   AND TTS.IS_FLEET = "+is_fleet+"\n");
		}
		if (!"".equals(fleet_name)) {
			sql.append("   AND TF.FLEET_NAME LIKE '%"+fleet_name+"%'\n");
		}
		if (!"".equals(contract_no)) {
			sql.append("   AND TFC.CONTRACT_NO LIKE '%"+contract_no+"%'\n");
		}
		if (null != startDate && !"".equals(startDate)) {
			sql.append("   AND TTS.SALES_DATE >= TO_DATE('"+ startDate+" 00:00:00','yyyy-MM-dd HH24:MI:SS')\n");
		}
		if (null != endDate && !"".equals(endDate)) {
			sql.append("   AND TTS.SALES_DATE  <= TO_DATE('"+endDate+" 23:59:59','yyyy-MM-dd HH24:MI:SS')\n");
		}
		if (null != dealerCode && !"".equals(dealerCode)) {
			String[] array = dealerCode.split(",");
			sql.append("   AND TMD.DEALER_CODE IN(\n");
			for (int i = 0; i < array.length; i++) {
				sql.append("'" + array[i] + "'");
				if (i != array.length - 1) {
					sql.append(",");
				}
			}
			sql.append(")\n");
		}
		if (!"".equals(orgCode)) {
			sql.append("AND TMD.DEALER_ID IN\n");
			sql.append("       (SELECT TD.DEALER_ID\n");  
			sql.append("          FROM TM_DEALER TD\n");  
			sql.append("         START WITH TD.DEALER_ID IN\n");  
			sql.append("                    (SELECT TDOR.DEALER_ID\n");  
			sql.append("                       FROM TM_DEALER_ORG_RELATION TDOR\n");  
			sql.append("                      WHERE TDOR.ORG_ID IN\n");  
			sql.append("                            (SELECT ORG_ID\n");  
			sql.append("                               FROM TM_ORG ORG\n");  
			sql.append("                              WHERE ORG.STATUS = "+Constant.STATUS_ENABLE+"\n");  
			sql.append("                              START WITH ORG_CODE = '"+orgCode+"'\n");  
			sql.append("                             CONNECT BY PRIOR ORG.ORG_ID = ORG.PARENT_ORG_ID))\n");  
			sql.append("        CONNECT BY PRIOR TD.DEALER_ID = TD.PARENT_DEALER_D)\n");

		}
		if(!CommonUtils.isNullString(orgId)) {
			sql.append(ReqQueryDao.limitOrg("TMD", "dealer_id", orgId)) ;
		}
		if(null != areaId && !"".equals(areaId)){
			sql.append("   AND TMV.YIELDLY = ("+areaId+")\n");
		}	
		/*if (null != areaId && !"".equals(areaId)) {
			sql.append("           and exists\n");  
			sql.append("         (select 1\n");  
			sql.append("                  from tm_dealer_business_area tdba\n");  
			sql.append("                 where 1 = 1\n");  
			sql.append("                   and tdba.dealer_id = TMD.dealer_id\n");  
			sql.append("                   and tdba.area_id IN (").append(areaId).append("))\n");  
		}*/
		sql.append("	AND M.MATERIAL_ID = TVMGR.MATERIAL_ID\n");
		sql.append("    AND TVMGR.GROUP_ID = TVMG.GROUP_ID\n");  
		sql.append("    AND M.MODEL_CODE = TVMG2.GROUP_CODE\n");  
		sql.append("    AND TVMG2.PARENT_GROUP_ID = TVMG3.GROUP_ID\n");

		sql.append("   AND TMV.LIFE_CYCLE = "+Constant.VEHICLE_LIFE_04+") XX\n");  
		sql.append("   WHERE (XX.IS_FLEET=10041001 AND XX.FLEET_NAME IS NOT NULL) OR XX.IS_FLEET=10041002"); 
		
		//sql.append(" ORDER BY TTS.SALES_DATE DESC\n");

//		
//			sql.append("SELECT TMD.DEALER_CODE,\n");
//			sql.append("       TMD.DEALER_SHORTNAME,\n");
//			sql.append("       DECODE(TTC.CTM_NAME, NULL, TTC.COMPANY_NAME,TTC.CTM_NAME) C_NAME, --客户名称\n");
//			sql.append("       TTC.CTM_TYPE, --客户类型\n");
//			sql.append("       TTS.IS_FLEET, --是否集团客户\n");
//			sql.append("       DECODE(TTC.CTM_NAME, NULL, TTC.COMPANY_PHONE,TTC.MAIN_PHONE) MAIN_PHONE, --主要联系电话\n");
//			sql.append("       TMVM.MATERIAL_CODE,\n");
//			sql.append("       TMVM.MATERIAL_NAME,\n");
//			sql.append("       TMV.VIN,\n");
//			sql.append("       TO_CHAR(TTS.SALES_DATE, 'yyyy-MM-dd') SALES_DATE, --实销时间\n");
//			sql.append("       TMV.VEHICLE_ID,\n");
//			sql.append("       TTC.CTM_ID\n");
//			sql.append("  FROM TT_DEALER_ACTUAL_SALES TTS,\n");
//			sql.append("       TT_CUSTOMER            TTC,\n");
//			sql.append("       TM_VEHICLE             TMV,\n");
//			sql.append("       TM_VHCL_MATERIAL       TMVM,\n");
//			sql.append("       TM_DEALER_BUSINESS_AREA TDBA,\n");
//			sql.append("       TM_ORG              TMO,\n");
//			sql.append("       TM_DEALER_ORG_RELATION TDOR,\n");
//			sql.append("       TM_DEALER              TMD\n");
//			sql.append(" WHERE TTS.CTM_ID = TTC.CTM_ID\n");
//			sql.append("   AND TTS.VEHICLE_ID = TMV.VEHICLE_ID\n");
//			sql.append("   AND TDBA.DEALER_ID=TMD.DEALER_ID\n");
//			sql.append("   AND TTS.DEALER_ID=TMD.DEALER_ID\n");
//			sql.append("   AND TDBA.AREA_ID=2010010100000004\n");
//			sql.append("   AND TMV.MATERIAL_ID = TMVM.MATERIAL_ID\n");
//			sql.append("AND TMO.ORG_ID=TDOR.ORG_ID AND TMD.DEALER_ID=TDOR.DEALER_ID\n");
//			sql.append("   AND TMV.LIFE_CYCLE = " + Constant.VEHICLE_LIFE_04
//							+ "\n");
//			sql.append("   AND TMD.DEALER_ID = TMV.DEALER_ID\n");
//			// 客户类型
//			if (null != orgId && !"".equals(orgId)&& !"-1".equals(orgId)) {
//				sql.append("AND TMO.ORG_CODE='"+orgId+"'\n");
//			}
//			// 客户类型
//			if (null != customer_type && !"".equals(customer_type)&& !"-1".equals(customer_type)) {
//				sql.append("   AND TTC.CTM_TYPE =" + customer_type + "\n");
//			}
//			// 客户名称
//			if (null != customer_name && !"".equals(customer_name)) {
//				sql.append("   AND (TTC.CTM_NAME LIKE '%" + customer_name.trim()+ "%' OR TTC.COMPANY_S_NAME LIKE '%" + customer_name.trim()+ "%')\n");
//			}
//			// VIN
//			if (null != vin && !"".equals(vin)) {
//				sql.append(GetVinUtil.getVins(vin, "TMV"));
//			}
//			// 状态
//			if (null != status && !"".endsWith(status) && !"-1".equals(status)) {
//				sql.append("   AND TTS.STATUS = " + status + "\n");
//			}
//			// 客户电话
//			if (null != customer_phone && !"".equals(customer_phone)) {
//				sql.append("   AND (TTC.MAIN_PHONE LIKE '%" + customer_phone.trim()+ "%' OR TTC.COMPANY_PHONE LIKE '%" + customer_phone.trim()+ "%')\n");
//			}
//			// 选择物料组
//			if (null != materialCode && !"".equals(materialCode)) {
//				String[] materialCodes = materialCode.split(",");
//				if (null != materialCodes && materialCodes.length > 0) {
//					StringBuffer buffer = new StringBuffer();
//					for (int i = 0; i < materialCodes.length; i++) {
//						buffer.append("'").append(materialCodes[i]).append("'").append(",");
//					}
//					buffer = buffer.deleteCharAt(buffer.length() - 1);
//					sql.append("   AND ((TMV.PACKAGE_ID IN (SELECT G.GROUP_ID\n");
//					sql.append("FROM TM_VHCL_MATERIAL_GROUP G \n");
//					sql.append("WHERE G.GROUP_CODE IN ("+ buffer.toString() + ")))\n");
//					sql.append("       OR (TMV.MODEL_ID IN (SELECT G.GROUP_ID \n");
//					sql.append("                             FROM TM_VHCL_MATERIAL_GROUP G \n");
//					sql.append("                            WHERE G.GROUP_CODE IN ("+ buffer.toString() + "))) \n");
//					sql.append("       OR (TMV.SERIES_ID IN (SELECT G.GROUP_ID \n");
//					sql.append("                              FROM TM_VHCL_MATERIAL_GROUP G \n");
//					sql.append("                             WHERE G.GROUP_CODE IN ("+ buffer.toString() + ")))) \n");
//				}
//			}
//			// 是否集团客户
//			if (null != is_fleet && !"".equals(is_fleet) && !"-1".equals(is_fleet)) {
//				sql.append("   AND TTS.IS_FLEET = " + is_fleet + "\n");
//			}
//
//			if (!is_fleet.equals(Constant.IF_TYPE_NO.toString())) {
//				// 集团客户名称
//				if (null != fleet_name && !"".equals(fleet_name)) {
//					sql.append("   AND TTC.COMPANY_S_NAME LIKE '%"+ fleet_name.trim() + "%'\n");
//				}
//				// 集团客户合同
//				if (null != contract_no && !"".equals(contract_no)) {
//					sql.append("   AND TTS.CONTRACT_NO LIKE '%"+ contract_no.trim() + "%'\n\n");
//				}
//			}
//			// 上报日期
//			if (null != startDate && !"".equals(startDate)) {
//				sql.append("   AND TTS.SALES_DATE >= TO_DATE('" + startDate+ "','yyyy-MM-dd')\n");
//			}
//			if (null != endDate && !"".equals(endDate)) {
//				sql.append("   AND TTS.SALES_DATE  <= (TO_DATE('" + endDate+ "','yyyy-MM-dd')+1)\n");
//			}
//
//			if (null != dealerCode && !"".equals(dealerCode)) {
//				String[] array = dealerCode.split(",");
//				sql.append("   AND TMD.DEALER_CODE IN(\n");
//				for (int i = 0; i < array.length; i++) {
//					sql.append("'" + array[i] + "'");
//					if (i != array.length - 1) {
//						sql.append(",");
//					}
//				}
//				sql.append(")\n");
//			}
//			sql.append(" ORDER BY TTS.SALES_DATE DESC\n");
		return dao.pageQuery(sql.toString(), null, dao.getFunName(), pageSize,curPage);
	}
	
	
	public List<Map<String, Object>> queryReportInfoExportList(Map<String,String> map) {
		String customer_type = (String) map.get("customer_type"); 	// 客户类型
		String customer_name = (String) map.get("customer_name"); 	// 客户名称
		String vin = (String) map.get("vin"); 						// VIN
		String customer_phone = (String) map.get("customer_phone"); // 客户电话
		String materialCode = (String) map.get("materialCode"); 	// 选择物料组
		String is_fleet = (String) map.get("is_fleet"); 			// 是否集团客户
		String fleet_name = (String) map.get("fleet_name"); 		// 集团客户名称
		String contract_no = (String) map.get("contract_no"); 		// 集团客户合同
		String startDate = (String) map.get("startDate"); 			// 上报日期:开始
		String endDate = (String) map.get("endDate"); 				// 上报日期 ：截止
		String dealerCode = (String) map.get("dealerCode"); 		// 经销商代码
		String orgCode=(String)map.get("orgCode");					// 区域
		String areaId = (String)map.get("areaId");					// 业务范围
		String orgId = map.get("orgId") ;
		String poseId = map.get("poseId");
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT XX.DEALER_CODE,\n");
		sql.append("XX.DEALER_SHORTNAME,\n");
		sql.append("XX.CTM_NAME,\n");
		sql.append("XX.IS_SECOND,\n");
		sql.append("XX.CTM_TYPE,\n");
		sql.append("XX.IS_FLEET,\n");
		sql.append("XX.FLEET_NAME,\n");
		sql.append("XX.CONTRACT_NO, \n");
		sql.append("XX.MAIN_PHONE,\n");
		sql.append("XX.MATERIAL_CODE,\n");
		sql.append("XX.MATERIAL_NAME,\n");
		sql.append("XX.VIN,\n");
		sql.append("XX.VT,    --车型名称\n");
		sql.append("XX.VS,   --车系名称\n");  
		sql.append("XX.SN, --状态名称\n");  
		sql.append("XX.COLOR,       --颜色名称\n");
		sql.append("XX.CAR_CHARACTOR,\n");
		sql.append("XX.CODE_DESC,\n");
		sql.append("XX.ENGINE_NO,\n");
		sql.append("XX.PRODUCT_DATE ,\n");
		//新增“付款方式”、“按揭类型”、“销售价格”、“首付比例”字段
		sql.append("(XX.SHOUFU_RATIO*100) as SHOUFU_RATIO,(SELECT CODE_DESC FROM TC_CODE WHERE CODE_ID=XX.MORTGAGE_TYPE) AS MORTGAGE_TYPE,XX.PAYMENT, XX.PRICE AS SALES_PRICE,\n");
		sql.append("XX.LOANS_TYPE,XX.LOANS_YEAR,");
		sql.append("XX.SALES_DATE,\n");
		sql.append("XX.CTM_ID,\n");
		sql.append("XX.VEHICLE_ID,\n");
		sql.append("XX.ORG_NAME,\n");
		sql.append("XX.DEALER_CREATE_DATE,\n");
		sql.append(" XX.REGION_NAME  FROM (\n");
		sql.append("SELECT TMD.DEALER_CODE, --经销商代码\n");
		sql.append("       TMD.DEALER_SHORTNAME, --经销商名称\n"); 
		sql.append("       tmd.CREATE_DATE as DEALER_CREATE_DATE,\n");
		sql.append("       TMD.DEALER_ID, \n");  
		sql.append("       TTC.CTM_NAME, --客户名称\n");  
		sql.append("       TC6.CODE_DESC AS  IS_SECOND, --是否二手车置换\n");  
		sql.append("       TC1.CODE_DESC CTM_TYPE, --客户类型\n");  
		sql.append("       TC2.CODE_DESC IS_FLEET, --是否集团客户\n");  
		//新增“付款方式”、“按揭类型”、“销售价格”、“首付比例”字段
		sql.append("   TTS.SHOUFU_RATIO,	       TTS.MORTGAGE_TYPE,	        TC4.CODE_DESC AS  PAYMENT, TTS.PRICE,\n");  
		sql.append("       TC3.CODE_DESC , \n");  
		sql.append("       TF.FLEET_NAME, --集团客户名称\n");  
		sql.append("       TFC.CONTRACT_NO, --集团客户合同\n");  
		sql.append("       decode(TTC.CTM_TYPE, ").append(Constant.CUSTOMER_TYPE_01).append(",TTC.MAIN_PHONE, TTC.company_phone) MAIN_PHONE, --主要联系电话\n");   
		sql.append("       M.MATERIAL_CODE, --物料代码\n");  
		sql.append("       M.MATERIAL_NAME, --物料名称\n");  
		sql.append("	   TVMG2.GROUP_NAME VT,    --车型名称\n");
		sql.append("       TVMG3.GROUP_NAME VS,   --车系名称\n");  
		sql.append("       TVMG.Group_Name SN, --状态名称\n");  
		sql.append("       TMV.COLOR,       --颜色名称\n");
		sql.append("       TMV.VIN, --VIN\n"); 
		sql.append(" TTS.LOANS_YEAR,      TC5.CODE_DESC AS LOANS_TYPE,");
		sql.append("TTS.CAR_CHARACTOR, --车辆性质\n");
		sql.append("       TMV.ENGINE_NO, --VIN\n"); 
		sql.append("	   TO_CHAR(TMV.PRODUCT_DATE, 'yyyy-MM-dd') PRODUCT_DATE ,\n");
		sql.append("       TO_CHAR(TTS.SALES_DATE, 'yyyy-MM-dd') SALES_DATE, --实销时间\n");  
		sql.append("       TTC.CTM_ID,\n");
		sql.append("       TMV.VEHICLE_ID,\n");
		sql.append("       VOD.ROOT_ORG_NAME ORG_NAME,VOD.REGION_NAME REGION_NAME \n");
		sql.append("  FROM TT_DEALER_ACTUAL_SALES TTS,\n");  
		sql.append("       TM_DEALER              TMD,\n");  
		sql.append("       TT_CUSTOMER            TTC,\n");  
		sql.append("       TM_VEHICLE             TMV,\n");  
		sql.append("       TM_VHCL_MATERIAL       M,\n"); 
		sql.append("	   TM_VHCL_MATERIAL_GROUP TVMG,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP TVMG2,\n");  
		sql.append("       TM_VHCL_MATERIAL_GROUP TVMG3,\n");  
		sql.append("       TM_VHCL_MATERIAL_GROUP_R TVMGR,\n");
		sql.append("       TM_FLEET               TF,\n");  
		sql.append("       TT_FLEET_CONTRACT      TFC,VW_ORG_DEALER VOD,TC_CODE TC1,TC_CODE TC2,TC_CODE TC3,TC_CODE TC4,TC_CODE TC5,TC_CODE TC6 \n");  
		sql.append(" WHERE TTS.DEALER_ID = TMD.DEALER_ID\n"); 
		sql.append("   AND M.MATERIAL_ID = TVMGR.MATERIAL_ID\n");
		sql.append("   AND TVMGR.GROUP_ID = TVMG.GROUP_ID\n");  
		sql.append("   AND M.MODEL_CODE = TVMG2.GROUP_CODE\n");  
		sql.append("   AND TVMG2.PARENT_GROUP_ID = TVMG3.GROUP_ID\n");
		sql.append("   AND VOD.DEALER_ID=TMD.DEALER_ID \n");
		sql.append("   AND TC1.CODE_ID = TTC.CTM_TYPE \n");
		sql.append("   AND  TTC.IS_SECOND=TC6.CODE_ID (+) \n");
		sql.append("   AND TC2.CODE_ID = TTS.IS_FLEET  AND TTS.PAYMENT= TC4.CODE_ID(+)  AND TTS.LOANS_TYPE = TC5.CODE_ID(+) ");
		sql.append("   AND TTS.CAR_CHARACTOR = tc3.code_id");
		sql.append("   AND TTS.CTM_ID = TTC.CTM_ID\n");  
		sql.append("   AND TTS.VEHICLE_ID = TMV.VEHICLE_ID\n");  
		sql.append("   AND TMV.MATERIAL_ID = M.MATERIAL_ID\n");  
		sql.append("   AND TTS.FLEET_ID = TF.FLEET_ID(+)\n");  
		sql.append("   AND TTS.IS_RETURN='"+Constant.IF_TYPE_NO+"'");
		sql.append("   AND TTS.CONTRACT_ID = TFC.CONTRACT_ID(+)\n");  
		sql.append(MaterialGroupManagerDao.getPoseIdBusinessSql("TMV.YIELDLY", poseId));
		if (!"".equals(customer_type)) {
			sql.append("   AND TTC.CTM_TYPE = "+customer_type+"\n");
		}
		if (!"".equals(customer_name)) {
			sql.append("   AND TTC.CTM_NAME LIKE '%"+customer_name.trim()+"%'\n");
		}
		if (!"".equals(vin)) {
			sql.append(GetVinUtil.getVins(vin, "TMV"));
		}
		if (!"".equals(customer_phone)) {
			sql.append("   AND TTC.MAIN_PHONE LIKE '%"+customer_phone.trim()+"%'\n");
		}
		if (null != materialCode && !"".equals(materialCode)) {
			String[] materialCodes = materialCode.split(",");
			if (null != materialCodes && materialCodes.length > 0) {
				StringBuffer buffer = new StringBuffer();
				for (int i = 0; i < materialCodes.length; i++) {
					buffer.append("'").append(materialCodes[i]).append("'").append(",");
				}
				buffer = buffer.deleteCharAt(buffer.length() - 1);
				sql.append("   AND ((TMV.PACKAGE_ID IN (SELECT G.GROUP_ID\n");
				sql.append("FROM TM_VHCL_MATERIAL_GROUP G \n");
				sql.append("WHERE G.GROUP_CODE IN ("+ buffer.toString() + ")))\n");
				sql.append("       OR (TMV.MODEL_ID IN (SELECT G.GROUP_ID \n");
				sql.append("                             FROM TM_VHCL_MATERIAL_GROUP G \n");
				sql.append("                            WHERE G.GROUP_CODE IN ("+ buffer.toString() + "))) \n");
				sql.append("       OR (TMV.SERIES_ID IN (SELECT G.GROUP_ID \n");
				sql.append("                              FROM TM_VHCL_MATERIAL_GROUP G \n");
				sql.append("                             WHERE G.GROUP_CODE IN ("+ buffer.toString() + ")))) \n");
			}
		}
		if (!"".equals(is_fleet)) {
			sql.append("   AND TTS.IS_FLEET = "+is_fleet+"\n");
		}
		if (!"".equals(fleet_name)) {
			sql.append("   AND TF.FLEET_NAME LIKE '%"+fleet_name+"%'\n");
		}
		if (!"".equals(contract_no)) {
			sql.append("   AND TFC.CONTRACT_NO LIKE '%"+contract_no+"%'\n");
		}
		if (null != startDate && !"".equals(startDate)) {
			sql.append("   AND TTS.SALES_DATE >= TO_DATE('"+ startDate+" 00:00:00','yyyy-MM-dd HH24:MI:SS')\n");
		}
		if (null != endDate && !"".equals(endDate)) {
			sql.append("   AND TTS.SALES_DATE  <= TO_DATE('"+endDate+" 23:59:59','yyyy-MM-dd HH24:MI:SS')\n");
		}
		if (null != dealerCode && !"".equals(dealerCode)) {
			String[] array = dealerCode.split(",");
			sql.append("   AND TMD.DEALER_CODE IN(\n");
			for (int i = 0; i < array.length; i++) {
				sql.append("'" + array[i] + "'");
				if (i != array.length - 1) {
					sql.append(",");
				}
			}
			sql.append(")\n");
		}
		if (!"".equals(orgCode)) {
			sql.append("AND TMD.DEALER_ID IN\n");
			sql.append("       (SELECT TD.DEALER_ID\n");  
			sql.append("          FROM TM_DEALER TD\n");  
			sql.append("         START WITH TD.DEALER_ID IN\n");  
			sql.append("                    (SELECT TDOR.DEALER_ID\n");  
			sql.append("                       FROM TM_DEALER_ORG_RELATION TDOR\n");  
			sql.append("                      WHERE TDOR.ORG_ID IN\n");  
			sql.append("                            (SELECT ORG_ID\n");  
			sql.append("                               FROM TM_ORG ORG\n");  
			sql.append("                              WHERE ORG.STATUS = "+Constant.STATUS_ENABLE+"\n");  
			sql.append("                              START WITH ORG_CODE = '"+orgCode+"'\n");  
			sql.append("                             CONNECT BY PRIOR ORG.ORG_ID = ORG.PARENT_ORG_ID))\n");  
			sql.append("        CONNECT BY PRIOR TD.DEALER_ID = TD.PARENT_DEALER_D)\n");

		}
		
		if(!CommonUtils.isNullString(orgId)) {
			sql.append(ReqQueryDao.limitOrg("TMD", "dealer_id", orgId)) ;
		}
		if(null != areaId && !"".equals(areaId)){
			sql.append("   AND TMV.YIELDLY = ("+areaId+")\n");
		}	 
		/*if (null != areaId && !"".equals(areaId)) {
			sql.append("           and exists\n");  
			sql.append("         (select 1\n");  
			sql.append("                  from tm_dealer_business_area tdba\n");  
			sql.append("                 where 1 = 1\n");  
			sql.append("                   and tdba.dealer_id = TMD.dealer_id\n");  
			sql.append("                   and tdba.area_id IN (").append(areaId).append("))\n");  
		}*/
		sql.append("   AND TMV.LIFE_CYCLE = "+Constant.VEHICLE_LIFE_04+"\n");  
		sql.append(" UNION \n");
		sql.append("SELECT TMD.DEALER_CODE, --经销商代码\n");
		sql.append("       TMD.DEALER_SHORTNAME, --经销商名称\n");
		sql.append("       tmd.CREATE_DATE as DEALER_CREATE_DATE,\n");
		sql.append("       TMD.DEALER_ID, \n");  
		sql.append("       TTC.CTM_NAME, --客户名称\n");  
		sql.append("      TC6.CODE_DESC  AS IS_SECOND, --是否二手车置换\n");  
		sql.append("       TC1.CODE_DESC CTM_TYPE, --客户类型\n");  
		sql.append("       TC2.CODE_DESC IS_FLEET, --是否集团客户\n"); 
		//新增“付款方式”、“按揭类型”、“销售价格”、“首付比例”字段
		sql.append("   TTS.SHOUFU_RATIO,	       TTS.MORTGAGE_TYPE,	       TC4.CODE_DESC AS  PAYMENT, TTS.PRICE,\n");  
		sql.append("       TC3.CODE_DESC , \n");  
		sql.append("       TCP.PACT_NAME FLEET_NAME, --集团客户名称\n");  
		sql.append("       TCP.PACT_NAME CONTRACT_NO, --集团客户合同\n");  
		sql.append("       decode(TTC.CTM_TYPE, ").append(Constant.CUSTOMER_TYPE_01).append(",TTC.MAIN_PHONE, TTC.company_phone) MAIN_PHONE, --主要联系电话\n");    
		sql.append("       M.MATERIAL_CODE, --物料代码\n");  
		sql.append("       M.MATERIAL_NAME, --物料名称\n");  
		sql.append("	   TVMG2.GROUP_NAME VT,    --车型名称\n");
		sql.append("       TVMG3.GROUP_NAME VS,   --车系名称\n");  
		sql.append("       TVMG.Group_Name SN, --状态名称\n");  
		sql.append("       TMV.COLOR,       --颜色名称\n");
		sql.append("       TMV.VIN, --VIN\n"); 
		sql.append("   TTS.LOANS_YEAR,       TC5.CODE_DESC AS LOANS_TYPE,");
		sql.append("TTS.CAR_CHARACTOR, --车辆性质\n");
		sql.append("       TMV.ENGINE_NO, --VIN\n"); 
		sql.append("	   TO_CHAR(TMV.PRODUCT_DATE, 'yyyy-MM-dd') PRODUCT_DATE ,\n");
		sql.append("       TO_CHAR(TTS.SALES_DATE, 'yyyy-MM-dd') SALES_DATE, --实销时间\n");  
		sql.append("       TTC.CTM_ID,\n");
		sql.append("       TMV.VEHICLE_ID,\n");
		sql.append("       VOD.ROOT_ORG_NAME ORG_NAME,VOD.REGION_NAME REGION_NAME \n");
		sql.append("  FROM TT_DEALER_ACTUAL_SALES TTS,\n");  
		sql.append("       TM_DEALER              TMD,\n");  
		sql.append("       TT_CUSTOMER            TTC,\n");  
		sql.append("       TM_VEHICLE             TMV,\n");  
		sql.append("       TM_VHCL_MATERIAL       M,\n"); 
		sql.append("	   TM_VHCL_MATERIAL_GROUP TVMG,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP TVMG2,\n");  
		sql.append("       TM_VHCL_MATERIAL_GROUP TVMG3,\n");  
		sql.append("       TM_VHCL_MATERIAL_GROUP_R TVMGR,\n");
		sql.append("       TM_FLEET               TF,\n");  
		sql.append("       TT_FLEET_CONTRACT      TFC,TM_COMPANY_PACT TCP,VW_ORG_DEALER VOD,TC_CODE TC1,TC_CODE TC2,TC_CODE TC3,TC_CODE TC4,TC_CODE TC5,TC_CODE TC6 \n");  
		sql.append(" WHERE TTS.DEALER_ID = TMD.DEALER_ID\n"); 
		sql.append("   AND M.MATERIAL_ID = TVMGR.MATERIAL_ID\n");
		sql.append("   AND TVMGR.GROUP_ID = TVMG.GROUP_ID\n");  
		sql.append("   AND M.MODEL_CODE = TVMG2.GROUP_CODE\n");  
		sql.append("   AND TVMG2.PARENT_GROUP_ID = TVMG3.GROUP_ID\n");
		sql.append("   AND TC1.CODE_ID = TTC.CTM_TYPE \n");
		sql.append("   AND  TTC.IS_SECOND =TC6.CODE_ID (+)\n");
		sql.append("   AND TC2.CODE_ID = TTS.IS_FLEET");
		sql.append("   AND TTS.CAR_CHARACTOR = tc3.code_id AND TTS.PAYMENT= TC4.CODE_ID(+)  AND TTS.LOANS_TYPE = TC5.CODE_ID(+) \n");
		sql.append("   AND VOD.DEALER_ID=TMD.DEALER_ID \n");
		sql.append("   AND TTS.CTM_ID = TTC.CTM_ID\n");  
		sql.append("   AND TTS.VEHICLE_ID = TMV.VEHICLE_ID\n");  
		sql.append("   AND TMV.MATERIAL_ID = M.MATERIAL_ID\n");  
		sql.append("   AND TTS.FLEET_ID = TCP.PACT_ID\n");  
		sql.append("   AND TTS.IS_RETURN='"+Constant.IF_TYPE_NO+"'");
		sql.append("   AND TTS.CONTRACT_ID = TFC.CONTRACT_ID(+)\n");  
		sql.append(MaterialGroupManagerDao.getPoseIdBusinessSql("TMV.YIELDLY", poseId));
		if (!"".equals(customer_type)) {
			sql.append("   AND TTC.CTM_TYPE = "+customer_type+"\n");
		}
		if (!"".equals(customer_name)) {
			sql.append("   AND TTC.CTM_NAME LIKE '%"+customer_name.trim()+"%'\n");
		}
		if (!"".equals(vin)) {
			sql.append(GetVinUtil.getVins(vin, "TMV"));
		}
		if (!"".equals(customer_phone)) {
			sql.append("   AND TTC.MAIN_PHONE LIKE '%"+customer_phone.trim()+"%'\n");
		}
		if (null != materialCode && !"".equals(materialCode)) {
			String[] materialCodes = materialCode.split(",");
			if (null != materialCodes && materialCodes.length > 0) {
				StringBuffer buffer = new StringBuffer();
				for (int i = 0; i < materialCodes.length; i++) {
					buffer.append("'").append(materialCodes[i]).append("'").append(",");
				}
				buffer = buffer.deleteCharAt(buffer.length() - 1);
				sql.append("   AND ((TMV.PACKAGE_ID IN (SELECT G.GROUP_ID\n");
				sql.append("FROM TM_VHCL_MATERIAL_GROUP G \n");
				sql.append("WHERE G.GROUP_CODE IN ("+ buffer.toString() + ")))\n");
				sql.append("       OR (TMV.MODEL_ID IN (SELECT G.GROUP_ID \n");
				sql.append("                             FROM TM_VHCL_MATERIAL_GROUP G \n");
				sql.append("                            WHERE G.GROUP_CODE IN ("+ buffer.toString() + "))) \n");
				sql.append("       OR (TMV.SERIES_ID IN (SELECT G.GROUP_ID \n");
				sql.append("                              FROM TM_VHCL_MATERIAL_GROUP G \n");
				sql.append("                             WHERE G.GROUP_CODE IN ("+ buffer.toString() + ")))) \n");
			}
		}
		if (!"".equals(is_fleet)) {
			sql.append("   AND TTS.IS_FLEET = "+is_fleet+"\n");
		}
		if (!"".equals(fleet_name)) {
			sql.append("   AND TF.FLEET_NAME LIKE '%"+fleet_name+"%'\n");
		}
		if (!"".equals(contract_no)) {
			sql.append("   AND TFC.CONTRACT_NO LIKE '%"+contract_no+"%'\n");
		}
		if (null != startDate && !"".equals(startDate)) {
			sql.append("   AND TTS.SALES_DATE >= TO_DATE('"+ startDate+" 00:00:00','yyyy-MM-dd HH24:MI:SS')\n");
		}
		if (null != endDate && !"".equals(endDate)) {
			sql.append("   AND TTS.SALES_DATE  <= TO_DATE('"+endDate+" 23:59:59','yyyy-MM-dd HH24:MI:SS')\n");
		}
		if (null != dealerCode && !"".equals(dealerCode)) {
			String[] array = dealerCode.split(",");
			sql.append("   AND TMD.DEALER_CODE IN(\n");
			for (int i = 0; i < array.length; i++) {
				sql.append("'" + array[i] + "'");
				if (i != array.length - 1) {
					sql.append(",");
				}
			}
			sql.append(")\n");
		}
		if (!"".equals(orgCode)) {
			sql.append("AND TMD.DEALER_ID IN\n");
			sql.append("       (SELECT TD.DEALER_ID\n");  
			sql.append("          FROM TM_DEALER TD\n");  
			sql.append("         START WITH TD.DEALER_ID IN\n");  
			sql.append("                    (SELECT TDOR.DEALER_ID\n");  
			sql.append("                       FROM TM_DEALER_ORG_RELATION TDOR\n");  
			sql.append("                      WHERE TDOR.ORG_ID IN\n");  
			sql.append("                            (SELECT ORG_ID\n");  
			sql.append("                               FROM TM_ORG ORG\n");  
			sql.append("                              WHERE ORG.STATUS = "+Constant.STATUS_ENABLE+"\n");  
			sql.append("                              START WITH ORG_CODE = '"+orgCode+"'\n");  
			sql.append("                             CONNECT BY PRIOR ORG.ORG_ID = ORG.PARENT_ORG_ID))\n");  
			sql.append("        CONNECT BY PRIOR TD.DEALER_ID = TD.PARENT_DEALER_D)\n");

		}
		
		if(!CommonUtils.isNullString(orgId)) {
			sql.append(ReqQueryDao.limitOrg("TMD", "dealer_id", orgId)) ;
		}
		if(null != areaId && !"".equals(areaId)){
			sql.append("   AND TMV.YIELDLY = ("+areaId+")\n");
		}	
		/*if (null != areaId && !"".equals(areaId)) {
			sql.append("           and exists\n");  
			sql.append("         (select 1\n");  
			sql.append("                  from tm_dealer_business_area tdba\n");  
			sql.append("                 where 1 = 1\n");  
			sql.append("                   and tdba.dealer_id = TMD.dealer_id\n");  
			sql.append("                   and tdba.area_id IN (").append(areaId).append("))\n");  
		}*/
		sql.append("   AND TMV.LIFE_CYCLE = "+Constant.VEHICLE_LIFE_04+") XX\n");  
		sql.append("   WHERE (XX.IS_FLEET='是' AND XX.FLEET_NAME IS NOT NULL) OR XX.IS_FLEET='否'");
		
		return dao.pageQuery(sql.toString(), null, dao.getFunName());
	}

	public static List<Map<String, Object>> getOemCalDetailExportList(Map map) {
		String customer_type = (String) map.get("customer_type"); // 客户类型
		String customer_name = (String) map.get("customer_name"); // 客户名称
		String vin = (String) map.get("vin"); // VIN
		String customer_phone = (String) map.get("customer_phone"); // 客户电话
		String materialCode = (String) map.get("materialCode"); // 选择物料组
		String is_fleet = (String) map.get("is_fleet"); // 是否大客户
		String fleet_name = (String) map.get("fleet_name"); // 大客户名称
		String contract_no = (String) map.get("contract_no"); // 大客户合同
		String startDate = (String) map.get("startDate"); // 上报日期:开始
		String endDate = (String) map.get("endDate"); // 上报日期 ：截止
		String dealerCode = (String) map.get("dealerCode"); // 经销商代码

		StringBuffer sql = new StringBuffer();
		sql.append("SELECT TMD.DEALER_CODE,\n");
		sql.append("       TMD.DEALER_SHORTNAME,\n");
		sql.append("       DECODE(TTC.CTM_NAME, NULL, TTC.COMPANY_S_NAME,TTC.CTM_NAME) C_NAME, --客户名称\n");
		sql.append("       TTC.CTM_TYPE, --客户类型\n");
		sql.append("       TTS.IS_FLEET, --是否大客户\n");
		sql.append("       DECODE(TTC.CTM_NAME, NULL, TTC.COMPANY_PHONE, TTC.MAIN_PHONE) MAIN_PHONE, --主要联系电话\n");
		sql.append("       TMVM.MATERIAL_CODE,\n");
		sql.append("       TMVM.MATERIAL_NAME,\n");
		sql.append("       TMV.VIN,\n");
		sql.append("       TO_CHAR(TTS.SALES_DATE, 'yyyy-MM-dd') SALES_DATE, --实销时间\n");
		sql.append("       TMV.VEHICLE_ID,\n");
		sql.append("       TTC.CTM_ID\n");
		sql.append("  FROM TT_DEALER_ACTUAL_SALES TTS,\n");
		sql.append("       TT_CUSTOMER            TTC,\n");
		sql.append("       TM_VEHICLE             TMV,\n");
		sql.append("       TM_VHCL_MATERIAL       TMVM,\n");
		sql.append("       TM_DEALER              TMD\n");

		sql.append(" WHERE TTS.CTM_ID = TTC.CTM_ID\n");
		sql.append("   AND TTS.VEHICLE_ID = TMV.VEHICLE_ID\n");
		sql.append("   AND TMV.MATERIAL_ID = TMVM.MATERIAL_ID\n");
		sql.append("   AND TMV.LIFE_CYCLE = " + Constant.VEHICLE_LIFE_04
						+ "\n");
		sql.append("   AND TMD.DEALER_ID = TMV.DEALER_ID\n");

		// 客户类型
		if (null != customer_type && !"".equals(customer_type)
				&& !"-1".equals(customer_type)) {
			sql.append("   AND TTC.CTM_TYPE =" + customer_type + "\n");
		}
		// 客户名称
		if (null != customer_name && !"".equals(customer_name)) {
			sql.append("   AND (TTC.CTM_NAME LIKE '%" + customer_name.trim()
					+ "%' OR TTC.COMPANY_S_NAME LIKE '%" + 

customer_name.trim()
					+ "%')\n");
		}
		// VIN
		if (null != vin && !"".equals(vin)) {
			sql.append(GetVinUtil.getVins(vin, "TMV"));
		}
		// 客户电话
		if (null != customer_phone && !"".equals(customer_phone)) {
			sql.append("   AND (TTC.MAIN_PHONE LIKE '%" + customer_phone.trim

()
					+ "%' OR TTC.COMPANY_PHONE LIKE '%" + 

customer_phone.trim()
					+ "%')\n");
		}
		// 选择物料组
		if (null != materialCode && !"".equals(materialCode)) {
			String[] materialCodes = materialCode.split(",");
			if (null != materialCodes && materialCodes.length > 0) {
				StringBuffer buffer = new StringBuffer();
				for (int i = 0; i < materialCodes.length; i++) {
					buffer.append("'").append(materialCodes

[i]).append("'")
							.append(",");
				}
				buffer = buffer.deleteCharAt(buffer.length() - 1);
				sql.append("   AND ((TMV.PACKAGE_ID IN (SELECT G.GROUP_ID \n");
				sql
						.append("                               FROM TM_VHCL_MATERIAL_GROUP G \n");
				sql
						.append("                 WHERE G.GROUP_CODE IN ("
								+ buffer.toString() + "))) \n");
				sql.append("       OR (TMV.MODEL_ID IN (SELECT G.GROUP_ID \n");
				sql
						.append("                             FROM TM_VHCL_MATERIAL_GROUP G \n");
				sql
						.append("                            WHERE G.GROUP_CODE IN ("							+ buffer.toString() + "))) \n");
				sql.append("       OR (TMV.SERIES_ID IN (SELECT G.GROUP_ID \n");
				sql
						.append("                        FROM TM_VHCL_MATERIAL_GROUP G \n");
				sql.append(" WHERE G.GROUP_CODE IN ("+ buffer.toString() + ")))) \n");
			}
		}
		// 是否大客户
		if (null != is_fleet && !"".equals(is_fleet) && !"-1".equals(is_fleet)) {
			sql.append("   AND TTS.IS_FLEET = " + is_fleet + "\n");
		}

		if (!is_fleet.equals(Constant.IF_TYPE_NO.toString())) {
			// 大客户名称
			if (null != fleet_name && !"".equals(fleet_name)) {
				sql.append("   AND TTC.COMPANY_S_NAME LIKE '%"
						+ fleet_name.trim() + "%'\n");
			}
			// 大客户合同
			if (null != contract_no && !"".equals(contract_no)) {
				sql.append("   AND TTS.CONTRACT_NO LIKE '%"
						+ contract_no.trim() + "%'\n\n");
			}
		}
		// 上报日期
		if (null != startDate && !"".equals(startDate)) {
			sql.append("   AND TTS.SALES_DATE >= TO_DATE('" + startDate
					+ "','yyyy-MM-dd')\n");
		}
		if (null != endDate && !"".equals(endDate)) {
			sql.append("   AND TTS.SALES_DATE  <= (TO_DATE('" + endDate
					+ "','yyyy-MM-dd')+1)\n");
		}

		if (null != dealerCode && !"".equals(dealerCode)) {
			String[] array = dealerCode.split(",");
			sql.append("   AND TMD.DEALER_CODE IN(\n");
			for (int i = 0; i < array.length; i++) {
				sql.append("'" + array[i] + "'");
				if (i != array.length - 1) {
					sql.append(",");
				}
			}
			sql.append(")\n");
		}
		sql.append(" ORDER BY TTS.SALES_DATE DESC\n");
		List<Map<String, Object>> list=dao.pageQuery(sql.toString(), null, 

dao.getFunName());
		return  list;
	}
	
	/**
	 * @Title : queryReportInfoList
	 * @Description: 实销信息查询结果展示
	 * @return : PageResult<Map<String,Object>>
	 * @throws
	 * @LastUpdate :2010-6-30
	 */
	public static PageResult<Map<String, Object>> queryReportInfoList_CVS(Map<String,String> map,int pageSize, int curPage,AclUserBean logonUser) {
		String customer_type = (String) map.get("customer_type"); 	// 客户类型
		String customer_name = (String) map.get("customer_name"); 	// 客户名称
		String vin = (String) map.get("vin"); 						// VIN
		String customer_phone = (String) map.get("customer_phone"); // 客户电话
		String materialCode = (String) map.get("materialCode"); 	// 选择物料组
		String is_fleet = (String) map.get("is_fleet"); 			// 是否集团客户
		String fleet_name = (String) map.get("fleet_name"); 		// 集团客户名称
		String contract_no = (String) map.get("contract_no"); 		// 集团客户合同
		String startDate = (String) map.get("startDate"); 			// 上报日期:开始
		String endDate = (String) map.get("endDate"); 				// 上报日期 ：截止
		String dealerCode = (String) map.get("dealerCode"); 		// 经销商代码
		String orgCode=(String)map.get("orgCode");					// 区域
		String areaId = (String)map.get("areaId");					// 业务范围
		String orgId = map.get("orgId") ;
		String poseId = map.get("poseId");
		String callcenterCheckStatus = map.get("callcenterCheckStatus");//呼叫中心审核状态
		String salesCheckStatus = map.get("salesCheckStatus");//销售部审核状态
		String carCharctor = map.get("SERVICEACTIVITY_CHARACTOR");
		String comm = map.get("comm");
		String onlySndDealer = map.get("onlySndDealer");
		String recDealerId = CommonUtils.checkNull(map.get("recDealerId"));
		
		String orderStartDate = CommonUtils.checkNull(map.get("orderStartDate"));
		String orderEndDate = CommonUtils.checkNull(map.get("orderEndDate"));
		String IS_REBATE = CommonUtils.checkNull(map.get("IS_REBATE"));
		StringBuffer sbSql=new StringBuffer();
		List<Object> params = new LinkedList<Object>();
		sbSql.append("SELECT   VOD.DEALER_CODE,                                         --经销商代码\n"); 
		//sbSql.append("         VOD.DEALER_NAME DEALER_SHORTNAME,                        --经销商名称\n");
		//wizard_lee 2014-04-28 经销商更名
		sbSql.append("         VOD.DEALER_NAME DEALER_SHORTNAME,                        --经销商名称\n"); 
		sbSql.append("		   decode((select dealer_shortname from tm_dealer t where t.dealer_id=TTS.Sales_Dealer),'',TTS.DEALER_NAME,(select dealer_shortname from tm_dealer t where t.dealer_id=TTS.Sales_Dealer)) sales_dealer_name,\n");//实销经销商
		sbSql.append("         VOD.DEALER_ID,\n");
		sbSql.append("         TTS.ORDER_ID,\n");
		sbSql.append("         decode(TTS.CALLCENTER_CHECK_STATUS,18011001,'未审核',18011002,'已审核',18011003,'已驳回') as CALLCENTER_CHECK_STATUS_DESC,\n");
		sbSql.append("         decode(TTS.SALES_CHECK_STATUS,19011002,'已审核',19011003,'已驳回',19011001,'未审核') as SALES_CHECK_STATUS_DESC,\n");
		//sbSql.append("         TTS.ORDER_ID,\n");
		sbSql.append("         TTS.CALLCENTER_CHECK_STATUS,\n"); 
		sbSql.append("         TTS.SALES_CHECK_STATUS,\n"); 
		sbSql.append("         TTS.ORDER_DATE,\n"); 
		sbSql.append("         VOD.ROOT_DEALER_CODE,VOD.ROOT_DEALER_NAME,");
		//sbSql.append("         TTC.CTM_NAME,                                              --客户名称\n"); 
		sbSql.append("decode(TTC.Ctm_Type,\n");
		sbSql.append("            '10831001',\n");
		sbSql.append("            TTC.CTM_NAME,\n");
		sbSql.append("            '10831002',\n");
		sbSql.append("            TTC.company_name) CTM_NAME,"); 

		sbSql.append("         TTC.CTM_TYPE,                                              --客户类型\n"); 
		sbSql.append("         F_GET_TCCODE_DESC(TTC.CTM_TYPE) CTM_TYPE_NAME,                 \n");
		sbSql.append("         TTS.IS_FLEET,                                          --是否集团客户\n"); 
		sbSql.append("         F_GET_TCCODE_DESC(TTS.IS_FLEET) IS_FLEET_NAME,                       \n");
		sbSql.append("         TCP.PACT_NAME FLEET_NAME,                              --集团客户名称\n"); 
		sbSql.append("         TCP.PACT_NO CONTRACT_NO,                             --集团客户合同\n"); 
		sbSql.append("         decode(TTC.Ctm_Type,'10831001',TTC.MAIN_PHONE,'10831002', TTC.company_phone) MAIN_PHONE,     --主要联系电话\n"); 
		sbSql.append("         M.MATERIAL_CODE,                                           --物料代码\n"); 
		sbSql.append("		   VMT.SERIES_NAME,\n");
		sbSql.append("		   VMT.MODEL_NAME,\n");
		sbSql.append("		   VMT.PACKAGE_NAME,\n");
		sbSql.append("		   VMT.COLOR_NAME,");
		sbSql.append("         M.MATERIAL_NAME,                                           --物料名称\n"); 
		sbSql.append("         TSC.NAME SALES_CON_NAME,\n"); 
		sbSql.append("         TMV.VIN,                                                        --VIN\n"); 
		sbSql.append("         TTS.CAR_CHARACTOR,                                         --车辆性质\n");  
		sbSql.append("         TTS.PAYMENT,                                         	  --付款方式\n");  
		sbSql.append("         TTS.MORTGAGE_TYPE,                                         --按揭类型\n");  
		sbSql.append("         TTS.PRICE,                                         		  --销售价格\n");  
		sbSql.append("         TTS.SHOUFU_RATIO,                                          --首付比例\n");  
		sbSql.append("         TTS.LOANS_TYPE,                                         	  --贷款方式\n");  
		sbSql.append("         TTS.LOANS_YEAR,                                         	  --贷款年限\n");  
		sbSql.append("         F_GET_TCCODE_DESC(CAR_CHARACTOR) CAR_CHARACTOR_NAME,                 \n");
		sbSql.append("         TMV.ENGINE_NO,                                                  --VIN\n"); 
		sbSql.append("         TO_CHAR (TMV.PRODUCT_DATE, 'yyyy-MM-dd') PRODUCT_DATE,\n"); 
		sbSql.append("         TO_CHAR (TTS.SALES_DATE, 'yyyy-MM-dd') SALES_DATE,         --实销时间\n"); 
		sbSql.append("         TTC.CTM_ID,\n"); 
		sbSql.append("         TMV.VEHICLE_ID,\n"); 
		sbSql.append("         TTC.CARD_TYPE,\n"); 
		sbSql.append("         F_GET_TCCODE_DESC(TTC.CARD_TYPE) CARD_TYPE_NAME,                 \n");
		sbSql.append("         TTC.CARD_NUM,\n"); 
		sbSql.append("         TTC.ADDRESS,\n");  
		sbSql.append("         TO_CHAR (TTS.CREATE_DATE, 'yyyy-MM-dd') CREATE_DATE,\n"); 
		sbSql.append("         TO_CHAR (TTS.invoice_date, 'yyyy-MM-dd') invoice_date,\n"); 
		sbSql.append("         VOD.ORG_NAME REGION_NAME,\n"); 
		sbSql.append("         decode((SELECT 1 abc FROM TM_VEHICLE_REBATE TVR WHERE TVR.VIN=TMV.VIN),'1','是','否') IS_REBATE, \n"); 
		sbSql.append("         VOD.ROOT_ORG_NAME ORG_NAME\n"); 
//		sbSql.append("  FROM   TT_DEALER_ACTUAL_SALES TTS,\n"); 
//		sbSql.append("         TT_SALES_CONSULTANT TSC,\n"); 
//		sbSql.append("         TT_CUSTOMER TTC,\n"); 
//		sbSql.append("         TM_VEHICLE TMV,\n"); 
//		sbSql.append("         TM_VHCL_MATERIAL M,\n"); 
//		sbSql.append("         TT_FLEET_CONTRACT TFC,\n"); 
//		sbSql.append("         TM_COMPANY_PACT TCP,\n"); 
//		sbSql.append("         VW_ORG_DEALER VOD,\n"); 
//		sbSql.append("         vw_material_group_mat VMT\n"); 
//		//sbSql.append("         TR_POSE_REGION_DEALER TPRD\n"); 
//		sbSql.append(" WHERE       TTS.DEALER_ID = VOD.DEALER_ID\n"); 
//		sbSql.append("         AND TTS.SALES_CON_ID = TSC.ID(+)\n"); 
//		//sbSql.append("         AND TPRD.DEALER_ID = VOD.DEALER_ID\n"); 
//		sbSql.append("         AND TTS.CTM_ID = TTC.CTM_ID\n"); 
//		sbSql.append("         AND TTS.VEHICLE_ID = TMV.VEHICLE_ID\n"); 
//		sbSql.append("         AND TMV.MATERIAL_ID = M.MATERIAL_ID\n"); 
//		sbSql.append("         AND VMT.MATERIAL_ID = TMV.MATERIAL_ID\n");
//		sbSql.append("         AND TTS.FLEET_ID = TCP.PACT_ID(+)\n"); 
//		//sbSql.append("         AND TPRD.POSE_ID = "+poseId+"\n"); 
//		sbSql.append("         AND TTS.IS_RETURN = 10041002\n"); 
//		sbSql.append("         AND TTS.CONTRACT_ID = TFC.CONTRACT_ID(+)\n"); 
		sbSql.append("  FROM TM_VEHICLE             TMV,\n");
		sbSql.append("       TT_DEALER_ACTUAL_SALES TTS,\n");
		sbSql.append("       TT_CUSTOMER            TTC,\n");
		sbSql.append("       TM_COMPANY_PACT        TCP,\n");
		sbSql.append("       VW_ORG_DEALER          VOD,\n");
		sbSql.append("       VW_MATERIAL_GROUP_MAT  VMT,\n");
		sbSql.append("       TM_VHCL_MATERIAL       M,\n");
		sbSql.append("       TT_FLEET_CONTRACT      TFC,\n");
		sbSql.append("       TT_SALES_CONSULTANT    TSC,\n");
		sbSql.append("       TM_BUSINESS_AREA       TBA\n");
		sbSql.append(" WHERE TTS.DEALER_ID = VOD.DEALER_ID\n");
		sbSql.append("   AND TTS.SALES_CON_ID = TSC.ID(+)\n");
		sbSql.append("   AND TTS.CTM_ID = TTC.CTM_ID\n");
		sbSql.append("   AND TTS.VEHICLE_ID = TMV.VEHICLE_ID\n");
		sbSql.append("   AND TMV.MATERIAL_ID = M.MATERIAL_ID\n");
		sbSql.append("   AND VMT.MATERIAL_ID = TMV.MATERIAL_ID\n");
		sbSql.append("   AND TTS.FLEET_ID = TCP.PACT_ID(+)\n");
		sbSql.append("   AND TTS.IS_RETURN = 10041002\n");
		sbSql.append("   AND TTS.CONTRACT_ID = TFC.CONTRACT_ID(+)\n");
		sbSql.append("   AND TBA.AREA_ID = TMV.YIELDLY\n");
		sbSql.append(MaterialGroupManagerDao.getPoseIdBusinessSql4AS("TMV.YIELDLY", poseId));
		sbSql.append(MaterialGroupManagerDao.getOrgDealerLimitSqlByServiceManager("VOD", logonUser));
		
		String __large_org = CommonUtils.checkNull(map.get("__large_org"));
		String __province_org = CommonUtils.checkNull(map.get("__province_org"));
		DaoFactory.getsql(sbSql, "VOD.ROOT_ORG_ID", __large_org,1);
		DaoFactory.getsql(sbSql, "VOD.ORG_ID", __province_org, 1);
		
		if (!"".equals(customer_type)) {
			sbSql.append("   AND TTC.CTM_TYPE = ?\n");
			params.add(customer_type);
		}
		if (!"".equals(customer_name)) {
			sbSql.append("   AND TTC.CTM_NAME LIKE ?\n");
			params.add("%"+customer_name.trim()+"%");
		}
		if (!"".equals(vin)) {
			sbSql.append(GetVinUtil.getVins(vin, "TMV"));
		}
		if (!"".equals(customer_phone)) {
			sbSql.append("   AND TTC.MAIN_PHONE LIKE ?\n");
			params.add("%"+customer_phone.trim()+"%");
		}
		if(!"".equals(materialCode)){
			sbSql.append(MaterialGroupManagerDao.getMaterialGroupQuerySqlByPar("TMV.PACKAGE_ID", materialCode, params));
		}
		if(!"".equals(is_fleet)){
			sbSql.append("   AND TTS.IS_FLEET=?\n" );
			params.add(is_fleet);
		}
		if(!"".equals(fleet_name)){
			sbSql.append("   AND TTC.CTM_NAME like ?\n" );
			params.add("%"+fleet_name+"%");
		}
		if(!"".equals(contract_no)){
			sbSql.append("   AND TCP.PACT_NO like ?\n" );
			params.add("%"+contract_no+"%");
		}
		if (null != startDate && !"".equals(startDate)) {
			sbSql.append("   AND TTS.SALES_DATE>=TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')\n" );
			params.add(startDate+" 00:00:00");
		}
		if (null != endDate && !"".equals(endDate)) {
			sbSql.append("   AND TTS.SALES_DATE<=TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')\n" );
			params.add(endDate+" 23:59:59");
		}
		if (null != orderStartDate && !"".equals(orderStartDate)) {
			sbSql.append("   AND TTS.ORDER_DATE>=TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')\n" );
			params.add(orderStartDate+" 00:00:00");
		}
		if (null != orderEndDate && !"".equals(orderEndDate)) {
			sbSql.append("   AND TTS.ORDER_DATE<=TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')\n" );
			params.add(orderEndDate+" 23:59:59");
		}
		
		if (!"".equals(orgCode)) {
			sbSql.append(Utility.getConSqlByParamForEqual(orgCode, params,"VOD", "ORG_CODE"));
		}
		if(!"".equals(dealerCode)){
			sbSql.append(Utility.getConSqlByParamForEqual(dealerCode, params,"VOD", "DEALER_CODE"));
		}
		if(!"".equals(areaId)){
			sbSql.append(" AND TMV.YIELDLY=?");
			params.add(areaId);
		}
		if(!"".equals(carCharctor)&&carCharctor != null){
			sbSql.append(" AND TTS.CAR_CHARACTOR = ?");
			params.add(carCharctor);
		}
		if("on".equals(onlySndDealer)){
			sbSql.append(" AND TTS.DEALER_ID<>TTS.SALES_DEALER \n");
		}
		//加入二网查询条件
		if(!"".equals(recDealerId) && null != recDealerId){
			if(!"-1".equals(recDealerId)){
				sbSql.append("   AND TTS.SALES_DEALER = "+recDealerId+"\n");
			}
		}
		
		if (!XHBUtil.IsNull(callcenterCheckStatus) || !XHBUtil.IsNull(salesCheckStatus)) {
			if(!XHBUtil.IsNull(callcenterCheckStatus)){
				sbSql.append(" AND TTS.CALLCENTER_CHECK_STATUS = ?");
				params.add(callcenterCheckStatus);
				
				if(comm!=null){
					if(comm.equals("1")){
						sbSql.append(" AND TTS.SALES_CHECK_STATUS = ?");
						params.add(Constant.SALESX_CHECK_STATUS_02);
					}
				}
			}
			if(!XHBUtil.IsNull(salesCheckStatus)){
				sbSql.append(" AND TTS.SALES_CHECK_STATUS = ?");
				params.add(salesCheckStatus);
			}
		} else {
			sbSql.append(" AND TMV.LIFE_CYCLE = 10321004\n");//为实销
		}
		if (Constant.IF_TYPE_YES.toString().equals(IS_REBATE)) {
			sbSql.append("AND EXISTS (SELECT 1 abc FROM TM_VEHICLE_REBATE TVR WHERE TVR.VIN=TMV.VIN)");
		} else if (Constant.IF_TYPE_NO.toString().equals(IS_REBATE)) {
			sbSql.append("AND NOT EXISTS (SELECT 1 abc FROM TM_VEHICLE_REBATE TVR WHERE TVR.VIN=TMV.VIN)");
		}
		return dao.pageQuery(sbSql.toString(), params, dao.getFunName(), pageSize,curPage);
	}
	public static PageResult<Map<String, Object>> querySumReportInfoList_CVS(Map<String,String> map,int pageSize, int curPage,AclUserBean logonUser) {
		String customer_type = (String) map.get("customer_type"); 	// 客户类型
		String customer_name = (String) map.get("customer_name"); 	// 客户名称
		String vin = (String) map.get("vin"); 						// VIN
		String customer_phone = (String) map.get("customer_phone"); // 客户电话
		String materialCode = (String) map.get("materialCode"); 	// 选择物料组
		String is_fleet = (String) map.get("is_fleet"); 			// 是否集团客户
		String fleet_name = (String) map.get("fleet_name"); 		// 集团客户名称
		String contract_no = (String) map.get("contract_no"); 		// 集团客户合同
		String startDate = (String) map.get("startDate"); 			// 上报日期:开始
		String endDate = (String) map.get("endDate"); 				// 上报日期 ：截止
		String dealerCode = (String) map.get("dealerCode"); 		// 经销商代码
		String orgCode=(String)map.get("orgCode");					// 区域
		String areaId = (String)map.get("areaId");					// 业务范围
		String orgId = map.get("orgId") ;
		String poseId = map.get("poseId");
		String callcenterCheckStatus = map.get("callcenterCheckStatus");//呼叫中心审核状态
		String salesCheckStatus = map.get("salesCheckStatus");//销售部审核状态
		String carCharctor = map.get("SERVICEACTIVITY_CHARACTOR");
		String comm = map.get("comm");
		String onlySndDealer = map.get("onlySndDealer");
		StringBuffer sbSql=new StringBuffer();
		List<Object> params = new LinkedList<Object>();
		String recDealerId = CommonUtils.checkNull(map.get("recDealerId"));
		String orderStartDate = CommonUtils.checkNull(map.get("orderStartDate"));
		String orderEndDate = CommonUtils.checkNull(map.get("orderEndDate"));
		
		sbSql.append("WITH ORG_DATA AS\n");
		sbSql.append(" (SELECT root_org_id, dealer_id, root_org_name, root_NAME_SORT\n");
		sbSql.append("   FROM VW_ORG_DEALER),\n");
		sbSql.append("info_ as\n");
		sbSql.append("(SELECT  VOD.root_org_id,VOD.DEALER_CODE,                                         --经销商代码\n"); 
		sbSql.append("         VOD.DEALER_NAME DEALER_SHORTNAME,                        --经销商名称\n"); 
		sbSql.append("		   decode((select dealer_shortname from tm_dealer t where t.dealer_id=TTS.Sales_Dealer),'',TTS.DEALER_NAME,(select dealer_shortname from tm_dealer t where t.dealer_id=TTS.Sales_Dealer)) sales_dealer_name,\n");//实销经销商
		sbSql.append("         VOD.DEALER_ID,\n");
		sbSql.append("         TTS.ORDER_ID,\n");
		sbSql.append("         decode(TTS.CALLCENTER_CHECK_STATUS,18011001,'未审核',18011002,'已审核',18011003,'已驳回') as CALLCENTER_CHECK_STATUS_DESC,\n");
		sbSql.append("         decode(TTS.SALES_CHECK_STATUS,19011002,'已审核',19011003,'已驳回',19011001,'未审核') as SALES_CHECK_STATUS_DESC,\n");
		sbSql.append("         TTS.CALLCENTER_CHECK_STATUS,\n"); 
		sbSql.append("         TTS.SALES_CHECK_STATUS,\n"); 
		sbSql.append("         VOD.ROOT_DEALER_CODE,VOD.ROOT_DEALER_NAME,");
		sbSql.append("         TTC.CTM_NAME,                                              --客户名称\n"); 
		sbSql.append("         TTC.CTM_TYPE,                                              --客户类型\n"); 
		sbSql.append("         F_GET_TCCODE_DESC(TTC.CTM_TYPE) CTM_TYPE_NAME,                 \n");
		sbSql.append("         TTS.IS_FLEET,                                          --是否集团客户\n"); 
		sbSql.append("         F_GET_TCCODE_DESC(TTS.IS_FLEET) IS_FLEET_NAME,                       \n");
		sbSql.append("         TCP.PACT_NAME FLEET_NAME,                              --集团客户名称\n"); 
		sbSql.append("         TCP.PACT_NO CONTRACT_NO,                             --集团客户合同\n"); 
		sbSql.append("         NVL(TTC.MAIN_PHONE, TTC.company_phone) MAIN_PHONE,     --主要联系电话\n"); 
		sbSql.append("         M.MATERIAL_CODE,                                           --物料代码\n"); 
		sbSql.append("		   VMT.SERIES_NAME,\n");
		sbSql.append("		   VMT.MODEL_NAME,\n");
		sbSql.append("		   VMT.PACKAGE_NAME,\n");
		sbSql.append("		   VMT.COLOR_NAME,");
		sbSql.append("         M.MATERIAL_NAME,                                           --物料名称\n"); 
		sbSql.append("         TSC.NAME SALES_CON_NAME,\n"); 
		sbSql.append("         TMV.VIN,                                                        --VIN\n"); 
		sbSql.append("         TTS.CAR_CHARACTOR,                                         --车辆性质\n");  
		sbSql.append("         TTS.PAYMENT,                                         	  --付款方式\n");  
		sbSql.append("         TTS.MORTGAGE_TYPE,                                         --按揭类型\n");  
		sbSql.append("         TTS.PRICE,                                         		  --销售价格\n");  
		sbSql.append("         TTS.SHOUFU_RATIO,                                          --首付比例\n");  
		sbSql.append("         TTS.LOANS_TYPE,                                         	  --贷款方式\n");  
		sbSql.append("         TTS.LOANS_YEAR,                                         	  --贷款年限\n");  
		sbSql.append("         F_GET_TCCODE_DESC(CAR_CHARACTOR) CAR_CHARACTOR_NAME,                 \n");
		sbSql.append("         TMV.ENGINE_NO,                                                  --VIN\n"); 
		sbSql.append("         TO_CHAR (TMV.PRODUCT_DATE, 'yyyy-MM-dd') PRODUCT_DATE,\n"); 
		sbSql.append("         TO_CHAR (TTS.SALES_DATE, 'yyyy-MM-dd') SALES_DATE,         --实销时间\n"); 
		sbSql.append("         TTC.CTM_ID,\n"); 
		sbSql.append("         TMV.VEHICLE_ID,\n"); 
		sbSql.append("         TTC.CARD_TYPE,\n"); 
		sbSql.append("         F_GET_TCCODE_DESC(TTC.CARD_TYPE) CARD_TYPE_NAME,                 \n");
		sbSql.append("         TTC.CARD_NUM,\n"); 
		sbSql.append("         TTC.ADDRESS,\n");  
		sbSql.append("         TO_CHAR (TTS.CREATE_DATE, 'yyyy-MM-dd') CREATE_DATE,\n"); 
		sbSql.append("         TO_CHAR (TTS.invoice_date, 'yyyy-MM-dd') invoice_date,\n"); 
		sbSql.append("         VOD.ORG_NAME REGION_NAME,\n"); 
		sbSql.append("         VOD.ROOT_ORG_NAME ORG_NAME\n"); 
		sbSql.append("  FROM   TT_DEALER_ACTUAL_SALES TTS,\n"); 
		sbSql.append("         TT_SALES_CONSULTANT TSC,\n"); 
		sbSql.append("         TT_CUSTOMER TTC,\n"); 
		sbSql.append("         TM_VEHICLE TMV,\n"); 
		sbSql.append("         TM_VHCL_MATERIAL M,\n"); 
		sbSql.append("         TT_FLEET_CONTRACT TFC,\n"); 
		sbSql.append("         TM_COMPANY_PACT TCP,\n"); 
		sbSql.append("         VW_ORG_DEALER VOD,\n"); 
		sbSql.append("         vw_material_group_mat VMT\n"); 
		sbSql.append(" WHERE       TTS.DEALER_ID = VOD.DEALER_ID\n"); 
		sbSql.append("         AND TTS.SALES_CON_ID = TSC.ID(+)\n"); 
		sbSql.append("         AND TTS.CTM_ID = TTC.CTM_ID\n"); 
		sbSql.append("         AND TTS.VEHICLE_ID = TMV.VEHICLE_ID\n"); 
		sbSql.append("         AND TMV.MATERIAL_ID = M.MATERIAL_ID\n"); 
		sbSql.append("         AND VMT.MATERIAL_ID = TMV.MATERIAL_ID\n");
		sbSql.append("         AND TTS.FLEET_ID = TCP.PACT_ID(+)\n"); 
		sbSql.append("         AND TTS.IS_RETURN = 10041002\n"); 
		sbSql.append("         AND TTS.CONTRACT_ID = TFC.CONTRACT_ID(+)\n"); 
		sbSql.append(MaterialGroupManagerDao.getPoseIdBusinessSql("TMV.YIELDLY", poseId));
		sbSql.append(MaterialGroupManagerDao.getOrgDealerLimitSql("VOD.DEALER_ID", logonUser));
		
		String __large_org = CommonUtils.checkNull(map.get("__large_org"));
		String __province_org = CommonUtils.checkNull(map.get("__province_org"));
		DaoFactory.getsql(sbSql, "VOD.ROOT_ORG_ID", __large_org,1);
		DaoFactory.getsql(sbSql, "VOD.ORG_ID", __province_org, 1);
		
		if (!"".equals(customer_type)) {
			sbSql.append("   AND TTC.CTM_TYPE = ?\n");
			params.add(customer_type);
		}
		if (!"".equals(customer_name)) {
			sbSql.append("   AND TTC.CTM_NAME LIKE ?\n");
			params.add("%"+customer_name.trim()+"%");
		}
		if (!"".equals(vin)) {
			sbSql.append(GetVinUtil.getVins(vin, "TMV"));
		}
		if (!"".equals(customer_phone)) {
			sbSql.append("   AND TTC.MAIN_PHONE LIKE ?\n");
			params.add("%"+customer_phone.trim()+"%");
		}
		if(!"".equals(materialCode)){
			sbSql.append(MaterialGroupManagerDao.getMaterialGroupQuerySqlByPar("TMV.PACKAGE_ID", materialCode, params));
		}
		if(!"".equals(is_fleet)){
			sbSql.append("   AND TTS.IS_FLEET=?\n" );
			params.add(is_fleet);
		}
		if(!"".equals(fleet_name)){
			sbSql.append("   AND TTC.CTM_NAME like ?\n" );
			params.add("%"+fleet_name+"%");
		}
		if(!"".equals(contract_no)){
			sbSql.append("   AND TCP.PACT_NO like ?\n" );
			params.add("%"+contract_no+"%");
		}
		if (null != startDate && !"".equals(startDate)) {
			sbSql.append("   AND TTS.SALES_DATE>=TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')\n" );
			params.add(startDate+" 00:00:00");
		}
		if (null != endDate && !"".equals(endDate)) {
			sbSql.append("   AND TTS.SALES_DATE<=TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')\n" );
			params.add(endDate+" 23:59:59");
		}
		if (null != orderStartDate && !"".equals(orderStartDate)) {
			sbSql.append("   AND TTS.ORDER_DATE>=TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')\n" );
			params.add(orderStartDate+" 00:00:00");
		}
		if (null != orderEndDate && !"".equals(orderEndDate)) {
			sbSql.append("   AND TTS.ORDER_DATE<=TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')\n" );
			params.add(orderEndDate+" 23:59:59");
		}
		if (!"".equals(orgCode)) {
			sbSql.append(Utility.getConSqlByParamForEqual(orgCode, params,"VOD", "ORG_CODE"));
		}
		if(!"".equals(dealerCode)){
			sbSql.append(Utility.getConSqlByParamForEqual(dealerCode, params,"VOD", "DEALER_CODE"));
		}
		if(!"".equals(areaId)){
			sbSql.append(" AND TMV.YIELDLY=?");
			params.add(areaId);
		}
		if(!"".equals(carCharctor)&&carCharctor != null){
			sbSql.append(" AND TTS.CAR_CHARACTOR = ?");
			params.add(carCharctor);
		}
		if("on".equals(onlySndDealer)){
			sbSql.append(" AND TTS.DEALER_ID<>TTS.SALES_DEALER \n");
		}
		
		//加入二网查询条件
		if(!"".equals(recDealerId) && null != recDealerId){
			if(!"-1".equals(recDealerId)){
				sbSql.append("   AND TTS.SALES_DEALER = "+recDealerId+"\n");
			}
		}
		
		if (!XHBUtil.IsNull(callcenterCheckStatus) || !XHBUtil.IsNull(salesCheckStatus)) {
			if(!XHBUtil.IsNull(callcenterCheckStatus)){
				sbSql.append(" AND TTS.CALLCENTER_CHECK_STATUS = ?");
				params.add(callcenterCheckStatus);
				
				if(comm!=null){
					if(comm.equals("1")){
						sbSql.append(" AND TTS.SALES_CHECK_STATUS = ?");
						params.add(Constant.SALESX_CHECK_STATUS_02);
					}
				}
			}
			if(!XHBUtil.IsNull(salesCheckStatus)){
				sbSql.append(" AND TTS.SALES_CHECK_STATUS = ?");
				params.add(salesCheckStatus);
			}
		} else {
			sbSql.append(" AND TMV.LIFE_CYCLE = 10321004\n");//为实销
		}
		sbSql.append(") select t.root_org_name,\n");
		sbSql.append("t.dealer_id,\n");
		sbSql.append("t1.DEALER_SHORTNAME,       \n");
		sbSql.append(" t1.model_name,\n");
		sbSql.append(" t1.package_name,\n");
		sbSql.append(" t1.color_name,\n");
		sbSql.append(" count(1) as total\n");
		sbSql.append("from ORG_DATA t, info_ t1\n");
		sbSql.append(" where t.root_org_id = t1.root_org_id\n");
		sbSql.append(" and t.dealer_id = t1.dealer_id\n");
		sbSql.append(" group by t.root_org_name,\n");
		sbSql.append(" t.dealer_id,\n");
		sbSql.append(" t1.DEALER_SHORTNAME,\n");
		sbSql.append(" t1.model_name,\n");
		sbSql.append(" t1.package_name,\n");
		sbSql.append(" t1.color_name,\n");
		sbSql.append(" t.root_name_sort\n");
		sbSql.append(" order by t.root_name_sort,\n");
		sbSql.append(" t.dealer_id,\n");
		sbSql.append(" t1.model_name,\n");
		sbSql.append(" t1.package_name,\n");
		sbSql.append(" t1.color_name\n");
		return dao.pageQuery(sbSql.toString(), params, dao.getFunName(), pageSize,curPage);
	}

	/**
	 * 经销商汇总导出
	 */
	public static PageResult<Map<String, Object>> queryDealerSumReportInfoList_CVS(Map<String,String> map,int pageSize, int curPage,AclUserBean logonUser) {
		String customer_type = (String) map.get("customer_type"); 	// 客户类型
		String customer_name = (String) map.get("customer_name"); 	// 客户名称
		String vin = (String) map.get("vin"); 						// VIN
		String customer_phone = (String) map.get("customer_phone"); // 客户电话
		String materialCode = (String) map.get("materialCode"); 	// 选择物料组
		String is_fleet = (String) map.get("is_fleet"); 			// 是否集团客户
		String fleet_name = (String) map.get("fleet_name"); 		// 集团客户名称
		String contract_no = (String) map.get("contract_no"); 		// 集团客户合同
		String startDate = (String) map.get("startDate"); 			// 上报日期:开始
		String endDate = (String) map.get("endDate"); 				// 上报日期 ：截止
		String dealerCode = (String) map.get("dealerCode"); 		// 经销商代码
		String orgCode=(String)map.get("orgCode");					// 区域
		String areaId = (String)map.get("areaId");					// 业务范围
		String poseId = map.get("poseId");
		String callcenterCheckStatus = map.get("callcenterCheckStatus");//呼叫中心审核状态
		String salesCheckStatus = map.get("salesCheckStatus");//销售部审核状态
		String carCharctor = map.get("SERVICEACTIVITY_CHARACTOR");
		String comm = map.get("comm");
		String onlySndDealer = map.get("onlySndDealer");
		StringBuffer sbSql=new StringBuffer();
		List<Object> params = new LinkedList<Object>();
		String recDealerId = CommonUtils.checkNull(map.get("recDealerId"));
		String orderStartDate = CommonUtils.checkNull(map.get("orderStartDate"));
		String orderEndDate = CommonUtils.checkNull(map.get("orderEndDate"));
		
		sbSql.append("WITH ORG_DATA AS\n");
		sbSql.append(" (SELECT root_org_id, dealer_id, root_org_name, root_NAME_SORT, REGION_NAME\n");
		sbSql.append("   FROM VW_ORG_DEALER),\n");
		sbSql.append("info_ as\n");
		sbSql.append("(SELECT  VOD.root_org_id,VOD.DEALER_CODE,                                         --经销商代码\n"); 
		sbSql.append("         VOD.DEALER_NAME DEALER_SHORTNAME,                        --经销商名称\n"); 
		sbSql.append("		   decode((select dealer_shortname from tm_dealer t where t.dealer_id=TTS.Sales_Dealer),'',TTS.DEALER_NAME,(select dealer_shortname from tm_dealer t where t.dealer_id=TTS.Sales_Dealer)) sales_dealer_name,\n");//实销经销商
		sbSql.append("         VOD.DEALER_ID,\n");
		sbSql.append("         TTS.ORDER_ID,\n");
		sbSql.append("         decode(TTS.CALLCENTER_CHECK_STATUS,18011001,'未审核',18011002,'已审核',18011003,'已驳回') as CALLCENTER_CHECK_STATUS_DESC,\n");
		sbSql.append("         decode(TTS.SALES_CHECK_STATUS,19011002,'已审核',19011003,'已驳回',19011001,'未审核') as SALES_CHECK_STATUS_DESC,\n");
		sbSql.append("         TTS.CALLCENTER_CHECK_STATUS,\n"); 
		sbSql.append("         TTS.SALES_CHECK_STATUS,\n"); 
		sbSql.append("         VOD.ROOT_DEALER_CODE,VOD.ROOT_DEALER_NAME,");
		sbSql.append("         TTC.CTM_NAME,                                              --客户名称\n"); 
		sbSql.append("         TTC.CTM_TYPE,                                              --客户类型\n"); 
		sbSql.append("         F_GET_TCCODE_DESC(TTC.CTM_TYPE) CTM_TYPE_NAME,                 \n");
		sbSql.append("         TTS.IS_FLEET,                                          --是否集团客户\n"); 
		sbSql.append("         F_GET_TCCODE_DESC(TTS.IS_FLEET) IS_FLEET_NAME,                       \n");
		sbSql.append("         TCP.PACT_NAME FLEET_NAME,                              --集团客户名称\n"); 
		sbSql.append("         TCP.PACT_NO CONTRACT_NO,                             --集团客户合同\n"); 
		sbSql.append("         NVL(TTC.MAIN_PHONE, TTC.company_phone) MAIN_PHONE,     --主要联系电话\n"); 
		sbSql.append("         M.MATERIAL_CODE,                                           --物料代码\n"); 
		sbSql.append("		   VMT.SERIES_NAME,\n");
		sbSql.append("		   VMT.MODEL_NAME,\n");
		sbSql.append("		   VMT.PACKAGE_NAME,\n");
		sbSql.append("		   VMT.COLOR_NAME,");
		sbSql.append("         M.MATERIAL_NAME,                                           --物料名称\n"); 
		sbSql.append("         TSC.NAME SALES_CON_NAME,\n"); 
		sbSql.append("         TMV.VIN,                                                        --VIN\n"); 
		sbSql.append("         TTS.CAR_CHARACTOR,                                         --车辆性质\n");  
		sbSql.append("         TTS.PAYMENT,                                         	  --付款方式\n");  
		sbSql.append("         TTS.MORTGAGE_TYPE,                                         --按揭类型\n");  
		sbSql.append("         TTS.PRICE,                                         		  --销售价格\n");  
		sbSql.append("         TTS.SHOUFU_RATIO,                                          --首付比例\n");  
		sbSql.append("         TTS.LOANS_TYPE,                                         	  --贷款方式\n");  
		sbSql.append("         TTS.LOANS_YEAR,                                         	  --贷款年限\n");  
		sbSql.append("         F_GET_TCCODE_DESC(CAR_CHARACTOR) CAR_CHARACTOR_NAME,                 \n");
		sbSql.append("         TMV.ENGINE_NO,                                                  --VIN\n"); 
		sbSql.append("         TO_CHAR (TMV.PRODUCT_DATE, 'yyyy-MM-dd') PRODUCT_DATE,\n"); 
		sbSql.append("         TO_CHAR (TTS.SALES_DATE, 'yyyy-MM-dd') SALES_DATE,         --实销时间\n"); 
		sbSql.append("         TTC.CTM_ID,\n"); 
		sbSql.append("         TMV.VEHICLE_ID,\n"); 
		sbSql.append("         TTC.CARD_TYPE,\n"); 
		sbSql.append("         F_GET_TCCODE_DESC(TTC.CARD_TYPE) CARD_TYPE_NAME,                 \n");
		sbSql.append("         TTC.CARD_NUM,\n"); 
		sbSql.append("         TTC.ADDRESS,\n");  
		sbSql.append("         TO_CHAR (TTS.CREATE_DATE, 'yyyy-MM-dd') CREATE_DATE,\n"); 
		sbSql.append("         TO_CHAR (TTS.invoice_date, 'yyyy-MM-dd') invoice_date,\n"); 
		sbSql.append("         VOD.ORG_NAME REGION_NAME,\n"); 
		sbSql.append("         VOD.ROOT_ORG_NAME ORG_NAME\n"); 
		sbSql.append("  FROM   TT_DEALER_ACTUAL_SALES TTS,\n"); 
		sbSql.append("         TT_SALES_CONSULTANT TSC,\n"); 
		sbSql.append("         TT_CUSTOMER TTC,\n"); 
		sbSql.append("         TM_VEHICLE TMV,\n"); 
		sbSql.append("         TM_VHCL_MATERIAL M,\n"); 
		sbSql.append("         TT_FLEET_CONTRACT TFC,\n"); 
		sbSql.append("         TM_COMPANY_PACT TCP,\n"); 
		sbSql.append("         VW_ORG_DEALER VOD,\n"); 
		sbSql.append("         vw_material_group_mat VMT\n"); 
		sbSql.append(" WHERE       TTS.DEALER_ID = VOD.DEALER_ID\n"); 
		sbSql.append("         AND TTS.SALES_CON_ID = TSC.ID(+)\n"); 
		sbSql.append("         AND TTS.CTM_ID = TTC.CTM_ID\n"); 
		sbSql.append("         AND TTS.VEHICLE_ID = TMV.VEHICLE_ID\n"); 
		sbSql.append("         AND TMV.MATERIAL_ID = M.MATERIAL_ID\n"); 
		sbSql.append("         AND VMT.MATERIAL_ID = TMV.MATERIAL_ID\n");
		sbSql.append("         AND TTS.FLEET_ID = TCP.PACT_ID(+)\n"); 
		sbSql.append("         AND TTS.IS_RETURN = 10041002\n"); 
		sbSql.append("         AND TTS.CONTRACT_ID = TFC.CONTRACT_ID(+)\n"); 
		sbSql.append(MaterialGroupManagerDao.getPoseIdBusinessSql("TMV.YIELDLY", poseId));
		sbSql.append(MaterialGroupManagerDao.getOrgDealerLimitSql("VOD.DEALER_ID", logonUser));
		
		String __large_org = CommonUtils.checkNull(map.get("__large_org"));
		String __province_org = CommonUtils.checkNull(map.get("__province_org"));
		DaoFactory.getsql(sbSql, "VOD.ROOT_ORG_ID", __large_org,1);
		DaoFactory.getsql(sbSql, "VOD.ORG_ID", __province_org, 1);
		
		if (!"".equals(customer_type)) {
			sbSql.append("   AND TTC.CTM_TYPE = ?\n");
			params.add(customer_type);
		}
		if (!"".equals(customer_name)) {
			sbSql.append("   AND TTC.CTM_NAME LIKE ?\n");
			params.add("%"+customer_name.trim()+"%");
		}
		if (!"".equals(vin)) {
			sbSql.append(GetVinUtil.getVins(vin, "TMV"));
		}
		if (!"".equals(customer_phone)) {
			sbSql.append("   AND TTC.MAIN_PHONE LIKE ?\n");
			params.add("%"+customer_phone.trim()+"%");
		}
		if(!"".equals(materialCode)){
			sbSql.append(MaterialGroupManagerDao.getMaterialGroupQuerySqlByPar("TMV.PACKAGE_ID", materialCode, params));
		}
		if(!"".equals(is_fleet)){
			sbSql.append("   AND TTS.IS_FLEET=?\n" );
			params.add(is_fleet);
		}
		if(!"".equals(fleet_name)){
			sbSql.append("   AND TTC.CTM_NAME like ?\n" );
			params.add("%"+fleet_name+"%");
		}
		if(!"".equals(contract_no)){
			sbSql.append("   AND TCP.PACT_NO like ?\n" );
			params.add("%"+contract_no+"%");
		}
		if (null != startDate && !"".equals(startDate)) {
			sbSql.append("   AND TTS.SALES_DATE>=TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')\n" );
			params.add(startDate+" 00:00:00");
		}
		if (null != endDate && !"".equals(endDate)) {
			sbSql.append("   AND TTS.SALES_DATE<=TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')\n" );
			params.add(endDate+" 23:59:59");
		}
		if (null != orderStartDate && !"".equals(orderStartDate)) {
			sbSql.append("   AND TTS.ORDER_DATE>=TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')\n" );
			params.add(orderStartDate+" 00:00:00");
		}
		if (null != orderEndDate && !"".equals(orderEndDate)) {
			sbSql.append("   AND TTS.ORDER_DATE<=TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')\n" );
			params.add(orderEndDate+" 23:59:59");
		}
		if (!"".equals(orgCode)) {
			sbSql.append(Utility.getConSqlByParamForEqual(orgCode, params,"VOD", "ORG_CODE"));
		}
		if(!"".equals(dealerCode)){
			sbSql.append(Utility.getConSqlByParamForEqual(dealerCode, params,"VOD", "DEALER_CODE"));
		}
		if(!"".equals(areaId)){
			sbSql.append(" AND TMV.YIELDLY=?");
			params.add(areaId);
		}
		if(!"".equals(carCharctor)&&carCharctor != null){
			sbSql.append(" AND TTS.CAR_CHARACTOR = ?");
			params.add(carCharctor);
		}
		if("on".equals(onlySndDealer)){
			sbSql.append(" AND TTS.DEALER_ID<>TTS.SALES_DEALER \n");
		}
		
		//加入二网查询条件
		if(!"".equals(recDealerId) && null != recDealerId){
			if(!"-1".equals(recDealerId)){
				sbSql.append("   AND TTS.SALES_DEALER = "+recDealerId+"\n");
			}
		}
		
		if (!XHBUtil.IsNull(callcenterCheckStatus) || !XHBUtil.IsNull(salesCheckStatus)) {
			if(!XHBUtil.IsNull(callcenterCheckStatus)){
				sbSql.append(" AND TTS.CALLCENTER_CHECK_STATUS = ?");
				params.add(callcenterCheckStatus);
				
				if(comm!=null){
					if(comm.equals("1")){
						sbSql.append(" AND TTS.SALES_CHECK_STATUS = ?");
						params.add(Constant.SALESX_CHECK_STATUS_02);
					}
				}
			}
			if(!XHBUtil.IsNull(salesCheckStatus)){
				sbSql.append(" AND TTS.SALES_CHECK_STATUS = ?");
				params.add(salesCheckStatus);
			}
		} else {
			sbSql.append(" AND TMV.LIFE_CYCLE = 10321004\n");//为实销
		}
		sbSql.append(") select t.REGION_NAME,\n");
		sbSql.append(" rank() over(order by count(1) DESC) rowno, \n");
		sbSql.append("t.dealer_id,\n");
		sbSql.append("t1.DEALER_SHORTNAME,       \n");
		sbSql.append(" count(1) as total\n");
		sbSql.append("from ORG_DATA t, info_ t1\n");
		sbSql.append(" where t.root_org_id = t1.root_org_id\n");
		sbSql.append(" and t.dealer_id = t1.dealer_id\n");
		sbSql.append(" group by t.REGION_NAME,\n");
		sbSql.append(" t.dealer_id,\n");
		sbSql.append(" t1.DEALER_SHORTNAME\n");
		sbSql.append(" order by count(1) DESC \n");
		return dao.pageQuery(sbSql.toString(), params, dao.getFunName(), pageSize,curPage);
	}
	public List<Map<String, Object>> queryReportInfoExportList_CVS(Map<String,String> map) {
		String customer_type = (String) map.get("customer_type"); 	// 客户类型
		String customer_name = (String) map.get("customer_name"); 	// 客户名称
		String vin = (String) map.get("vin"); 						// VIN
		String customer_phone = (String) map.get("customer_phone"); // 客户电话
		String materialCode = (String) map.get("materialCode"); 	// 选择物料组
		String is_fleet = (String) map.get("is_fleet"); 			// 是否集团客户
		String fleet_name = (String) map.get("fleet_name"); 		// 集团客户名称
		String contract_no = (String) map.get("contract_no"); 		// 集团客户合同
		String startDate = (String) map.get("startDate"); 			// 上报日期:开始
		String endDate = (String) map.get("endDate"); 				// 上报日期 ：截止
		String dealerCode = (String) map.get("dealerCode"); 		// 经销商代码
		String orgCode=(String)map.get("orgCode");					// 区域
		String areaId = (String)map.get("areaId");
		String poseId = map.get("postId");
		// 业务范围
		String orgId = map.get("orgId") ;
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT XX.DEALER_CODE,\n");
		sql.append("XX.DEALER_SHORTNAME,\n");
		sql.append("XX.CTM_NAME,\n");
		sql.append("XX.CTM_TYPE,\n");
		sql.append("XX.IS_FLEET,\n");
		sql.append("XX.FLEET_NAME,\n");
		sql.append("XX.CONTRACT_NO, \n");
		sql.append("XX.MAIN_PHONE,\n");
		sql.append("XX.MATERIAL_CODE,\n");
		sql.append("XX.MATERIAL_NAME,\n");
		sql.append("XX.VIN,\n");
		sql.append("XX.CAR_CHARACTOR,\n");
		sql.append("XX.ENGINE_NO,\n");
		sql.append("XX.PRODUCT_DATE ,\n");
		sql.append("XX.SALES_DATE,\n");
		sql.append("XX.CTM_ID,\n");
		sql.append("XX.VEHICLE_ID,\n");
		sql.append("XX.SALES_CON_NAME,\n");
		sql.append("XX.code_desc,\n");
		sql.append("XX.ORG_NAME,\n");
		sql.append("XX.DEALER_CREATE_DATE,\n");
		sql.append(" XX.REGION_NAME  FROM (\n");
		sql.append("SELECT TMD.DEALER_CODE, --经销商代码\n");
		//sql.append("       TMD.DEALER_SHORTNAME, --经销商名称\n"); 
		//wizard_lee 经销商更名 2014-04-28
		sql.append("       TTS.DEALER_SHORTNAME, --经销商名称\n"); 
		sql.append("       TMD.CREATE_DATE AS DEALER_CREATE_DATE, --系统开通时间\n"); 
		sql.append("       TMD.DEALER_ID, \n");  
		sql.append("       TTC.CTM_NAME, --客户名称\n");  
		sql.append("       TC1.CODE_DESC CTM_TYPE, --客户类型\n");  
		sql.append("       TC2.CODE_DESC IS_FLEET, --是否集团客户\n");  
		sql.append("       TC3.CODE_DESC,\n");  
		sql.append("       TF.FLEET_NAME, --集团客户名称\n");  
		sql.append("       TFC.CONTRACT_NO, --集团客户合同\n");  
		sql.append("       decode(TTC.CTM_TYPE, ").append(Constant.CUSTOMER_TYPE_01).append(",TTC.MAIN_PHONE, TTC.company_phone) MAIN_PHONE, --主要联系电话\n");  
		sql.append("       M.MATERIAL_CODE, --物料代码\n");  
		sql.append("       M.MATERIAL_NAME, --物料名称\n"); 
		sql.append("       TSC.NAME SALES_CON_NAME, \n");  
		sql.append("       TMV.VIN, --VIN\n"); 
		sql.append("TTS.CAR_CHARACTOR, --车辆性质\n");
		sql.append("       TMV.ENGINE_NO, --VIN\n"); 
		sql.append("	   TO_CHAR(TMV.PRODUCT_DATE, 'yyyy-MM-dd') PRODUCT_DATE ,\n");
		sql.append("       TO_CHAR(TTS.SALES_DATE, 'yyyy-MM-dd') SALES_DATE, --实销时间\n");  
		sql.append("       TTC.CTM_ID,\n");
		sql.append("       TMV.VEHICLE_ID,\n");
		sql.append("       VOD.ROOT_ORG_NAME ORG_NAME,VOD.REGION_NAME REGION_NAME \n");
		sql.append("  FROM TT_DEALER_ACTUAL_SALES TTS,\n");  
		sql.append("       TT_SALES_CONSULTANT    TSC,\n");  
		sql.append("       TM_DEALER              TMD,\n");  
		sql.append("       TT_CUSTOMER            TTC,\n");  
		sql.append("       TM_VEHICLE             TMV,\n");  
		sql.append("       TM_VHCL_MATERIAL       M,\n");  
		sql.append("       TM_FLEET               TF,\n");  
		sql.append("       TT_FLEET_CONTRACT      TFC,VW_ORG_DEALER VOD,TC_CODE TC1,TC_CODE TC2, tc_code tc3\n");  
		sql.append(" WHERE TTS.DEALER_ID = TMD.DEALER_ID\n");  
		sql.append("   AND TTS.SALES_CON_ID = TSC.ID(+) \n");
		sql.append("   AND VOD.DEALER_ID=TMD.DEALER_ID \n");
		sql.append("   AND TC1.CODE_ID = TTC.CTM_TYPE \n");
		sql.append("   AND TC2.CODE_ID = TTS.IS_FLEET");
		sql.append("   AND TC3.CODE_ID = TTS.CAR_CHARACTOR");
		sql.append("   AND TTS.CTM_ID = TTC.CTM_ID\n");  
		sql.append("   AND TTS.VEHICLE_ID = TMV.VEHICLE_ID\n");  
		sql.append("   AND TMV.MATERIAL_ID = M.MATERIAL_ID\n");  
		sql.append("   AND TTS.FLEET_ID = TF.FLEET_ID(+)\n");  
		sql.append("   AND TTS.IS_RETURN='"+Constant.IF_TYPE_NO+"'");
		sql.append("   AND TTS.CONTRACT_ID = TFC.CONTRACT_ID(+)\n");  
		sql.append(MaterialGroupManagerDao.getPoseIdBusinessSql("TMV.YIELDLY", poseId));
		if (!"".equals(customer_type)) {
			sql.append("   AND TTC.CTM_TYPE = "+customer_type+"\n");
		}
		if (!"".equals(customer_name)) {
			sql.append("   AND TTC.CTM_NAME LIKE '%"+customer_name.trim()+"%'\n");
		}
		if (!"".equals(vin)) {
			sql.append(GetVinUtil.getVins(vin, "TMV"));
		}
		if (!"".equals(customer_phone)) {
			sql.append("   AND TTC.MAIN_PHONE LIKE '%"+customer_phone.trim()+"%'\n");
		}
		if (null != materialCode && !"".equals(materialCode)) {
			String[] materialCodes = materialCode.split(",");
			if (null != materialCodes && materialCodes.length > 0) {
				StringBuffer buffer = new StringBuffer();
				for (int i = 0; i < materialCodes.length; i++) {
					buffer.append("'").append(materialCodes[i]).append("'").append(",");
				}
				buffer = buffer.deleteCharAt(buffer.length() - 1);
				sql.append("   AND ((TMV.PACKAGE_ID IN (SELECT G.GROUP_ID\n");
				sql.append("FROM TM_VHCL_MATERIAL_GROUP G \n");
				sql.append("WHERE G.GROUP_CODE IN ("+ buffer.toString() + ")))\n");
				sql.append("       OR (TMV.MODEL_ID IN (SELECT G.GROUP_ID \n");
				sql.append("                             FROM TM_VHCL_MATERIAL_GROUP G \n");
				sql.append("                            WHERE G.GROUP_CODE IN ("+ buffer.toString() + "))) \n");
				sql.append("       OR (TMV.SERIES_ID IN (SELECT G.GROUP_ID \n");
				sql.append("                              FROM TM_VHCL_MATERIAL_GROUP G \n");
				sql.append("                             WHERE G.GROUP_CODE IN ("+ buffer.toString() + ")))) \n");
			}
		}
		if (!"".equals(is_fleet)) {
			sql.append("   AND TTS.IS_FLEET = "+is_fleet+"\n");
		}
		if (!"".equals(fleet_name)) {
			sql.append("   AND TF.FLEET_NAME LIKE '%"+fleet_name+"%'\n");
		}
		if (!"".equals(contract_no)) {
			sql.append("   AND TFC.CONTRACT_NO LIKE '%"+contract_no+"%'\n");
		}
		if (null != startDate && !"".equals(startDate)) {
			sql.append("   AND TTS.SALES_DATE >= TO_DATE('"+ startDate+" 00:00:00','yyyy-MM-dd HH24:MI:SS')\n");
		}
		if (null != endDate && !"".equals(endDate)) {
			sql.append("   AND TTS.SALES_DATE  <= TO_DATE('"+endDate+" 23:59:59','yyyy-MM-dd HH24:MI:SS')\n");
		}
		if (null != dealerCode && !"".equals(dealerCode)) {
			String[] array = dealerCode.split(",");
			sql.append("   AND TMD.DEALER_CODE IN(\n");
			for (int i = 0; i < array.length; i++) {
				sql.append("'" + array[i] + "'");
				if (i != array.length - 1) {
					sql.append(",");
				}
			}
			sql.append(")\n");
		}
		if (null != areaId && !"".equals(areaId)) {
			sql.append(" AND TMV.YIELDLY = "+areaId+" \n");
		}
		if (!"".equals(orgCode)) {
			sql.append("AND TMD.DEALER_ID IN\n");
			sql.append("       (SELECT TD.DEALER_ID\n");  
			sql.append("          FROM TM_DEALER TD\n");  
			sql.append("         START WITH TD.DEALER_ID IN\n");  
			sql.append("                    (SELECT TDOR.DEALER_ID\n");  
			sql.append("                       FROM TM_DEALER_ORG_RELATION TDOR\n");  
			sql.append("                      WHERE TDOR.ORG_ID IN\n");  
			sql.append("                            (SELECT ORG_ID\n");  
			sql.append("                               FROM TM_ORG ORG\n");  
			sql.append("                              WHERE ORG.STATUS = "+Constant.STATUS_ENABLE+"\n");  
			sql.append("                              START WITH ORG_CODE = '"+orgCode+"'\n");  
			sql.append("                             CONNECT BY PRIOR ORG.ORG_ID = ORG.PARENT_ORG_ID))\n");  
			sql.append("        CONNECT BY PRIOR TD.DEALER_ID = TD.PARENT_DEALER_D)\n");

		}
		
		if(!CommonUtils.isNullString(orgId)) {
			sql.append(ReqQueryDao.limitOrg("TMD", "dealer_id", orgId)) ;
		}
		
		
		sql.append("   AND TMV.LIFE_CYCLE = "+Constant.VEHICLE_LIFE_04+"\n");  
		sql.append(" UNION all\n");
		sql.append("SELECT TMD.DEALER_CODE, --经销商代码\n");
		sql.append("       TMD.DEALER_SHORTNAME, --经销商名称\n"); 
		sql.append("       TMD.CREATE_DATE AS DEALER_CREATE_DATE, --系统开通时间\n"); 
		sql.append("       TMD.DEALER_ID, \n");  
		sql.append("       TTC.CTM_NAME, --客户名称\n");  
		sql.append("       TC1.CODE_DESC CTM_TYPE, --客户类型\n");  
		sql.append("       TC2.CODE_DESC IS_FLEET, --是否集团客户\n");  
		sql.append("       TC3.CODE_DESC , \n");  
		sql.append("       TCP.PACT_NAME FLEET_NAME, --集团客户名称\n");  
		sql.append("       TCP.PACT_NAME CONTRACT_NO, --集团客户合同\n");  
		sql.append("       decode(TTC.CTM_TYPE, ").append(Constant.CUSTOMER_TYPE_01).append(",TTC.MAIN_PHONE, TTC.company_phone) MAIN_PHONE, --主要联系电话\n");  
		sql.append("       M.MATERIAL_CODE, --物料代码\n");  
		sql.append("       M.MATERIAL_NAME, --物料名称\n"); 
		sql.append("       TSC.NAME SALES_CON_NAME, \n");  
		sql.append("       TMV.VIN, --VIN\n"); 
		sql.append("TTS.CAR_CHARACTOR, --车辆性质\n");
		sql.append("       TMV.ENGINE_NO, --VIN\n"); 
		sql.append("	   TO_CHAR(TMV.PRODUCT_DATE, 'yyyy-MM-dd') PRODUCT_DATE ,\n");
		sql.append("       TO_CHAR(TTS.SALES_DATE, 'yyyy-MM-dd') SALES_DATE, --实销时间\n");  
		sql.append("       TTC.CTM_ID,\n");
		sql.append("       TMV.VEHICLE_ID,\n");
		sql.append("       VOD.ROOT_ORG_NAME ORG_NAME,VOD.REGION_NAME REGION_NAME \n");
		sql.append("  FROM TT_DEALER_ACTUAL_SALES TTS,\n");  
		sql.append("       TT_SALES_CONSULTANT    TSC,\n");  
		sql.append("       TM_DEALER              TMD,\n");  
		sql.append("       TT_CUSTOMER            TTC,\n");  
		sql.append("       TM_VEHICLE             TMV,\n");  
		sql.append("       TM_VHCL_MATERIAL       M,\n");  
		//sql.append("       TM_FLEET               TF,\n");  
		sql.append("       TT_FLEET_CONTRACT      TFC,TM_COMPANY_PACT TCP,VW_ORG_DEALER VOD,TC_CODE TC1,TC_CODE TC2,tc_code tc3\n");  
		sql.append(" WHERE TTS.DEALER_ID = TMD.DEALER_ID\n");  
		sql.append("   AND TTS.SALES_CON_ID = TSC.ID(+) \n");
		sql.append("   AND TC1.CODE_ID = TTC.CTM_TYPE \n");
		sql.append("   AND TC2.CODE_ID = TTS.IS_FLEET");
		sql.append("   AND TC3.CODE_ID = TTS.CAR_CHARACTOR");
		sql.append("   AND VOD.DEALER_ID=TMD.DEALER_ID \n");
		sql.append("   AND TTS.CTM_ID = TTC.CTM_ID\n");  
		sql.append("   AND TTS.VEHICLE_ID = TMV.VEHICLE_ID\n");  
		sql.append("   AND TMV.MATERIAL_ID = M.MATERIAL_ID\n");  
		sql.append("   AND TTS.FLEET_ID = TCP.PACT_ID\n");  
		sql.append("   AND TTS.IS_RETURN='"+Constant.IF_TYPE_NO+"'");
		sql.append("   AND TTS.CONTRACT_ID = TFC.CONTRACT_ID(+)\n");  
		sql.append(MaterialGroupManagerDao.getPoseIdBusinessSql("TMV.YIELDLY", poseId));
		if (!"".equals(customer_type)) {
			sql.append("   AND TTC.CTM_TYPE = "+customer_type+"\n");
		}
		if (!"".equals(customer_name)) {
			sql.append("   AND TTC.CTM_NAME LIKE '%"+customer_name.trim()+"%'\n");
		}
		if (!"".equals(vin)) {
			sql.append(GetVinUtil.getVins(vin, "TMV"));
		}
		if (!"".equals(customer_phone)) {
			sql.append("   AND TTC.MAIN_PHONE LIKE '%"+customer_phone.trim()+"%'\n");
		}
		if (null != materialCode && !"".equals(materialCode)) {
			String[] materialCodes = materialCode.split(",");
			if (null != materialCodes && materialCodes.length > 0) {
				StringBuffer buffer = new StringBuffer();
				for (int i = 0; i < materialCodes.length; i++) {
					buffer.append("'").append(materialCodes[i]).append("'").append(",");
				}
				buffer = buffer.deleteCharAt(buffer.length() - 1);
				sql.append("   AND ((TMV.PACKAGE_ID IN (SELECT G.GROUP_ID\n");
				sql.append("FROM TM_VHCL_MATERIAL_GROUP G \n");
				sql.append("WHERE G.GROUP_CODE IN ("+ buffer.toString() + ")))\n");
				sql.append("       OR (TMV.MODEL_ID IN (SELECT G.GROUP_ID \n");
				sql.append("                             FROM TM_VHCL_MATERIAL_GROUP G \n");
				sql.append("                            WHERE G.GROUP_CODE IN ("+ buffer.toString() + "))) \n");
				sql.append("       OR (TMV.SERIES_ID IN (SELECT G.GROUP_ID \n");
				sql.append("                              FROM TM_VHCL_MATERIAL_GROUP G \n");
				sql.append("                             WHERE G.GROUP_CODE IN ("+ buffer.toString() + ")))) \n");
			}
		}
		if (!"".equals(is_fleet)) {
			sql.append("   AND TTS.IS_FLEET = "+is_fleet+"\n");
		}
		if (!"".equals(fleet_name)) {
			sql.append("   AND TF.FLEET_NAME LIKE '%"+fleet_name+"%'\n");
		}
		if (!"".equals(contract_no)) {
			sql.append("   AND TFC.CONTRACT_NO LIKE '%"+contract_no+"%'\n");
		}
		if (null != startDate && !"".equals(startDate)) {
			sql.append("   AND TTS.SALES_DATE >= TO_DATE('"+ startDate+" 00:00:00','yyyy-MM-dd HH24:MI:SS')\n");
		}
		if (null != endDate && !"".equals(endDate)) {
			sql.append("   AND TTS.SALES_DATE  <= TO_DATE('"+endDate+" 23:59:59','yyyy-MM-dd HH24:MI:SS')\n");
		}
		if (null != areaId && !"".equals(areaId)) {
			sql.append(" AND TMV.YIELDLY = "+areaId+"\n");
		}
		
		if (null != dealerCode && !"".equals(dealerCode)) {
			String[] array = dealerCode.split(",");
			sql.append("   AND TMD.DEALER_CODE IN(\n");
			for (int i = 0; i < array.length; i++) {
				sql.append("'" + array[i] + "'");
				if (i != array.length - 1) {
					sql.append(",");
				}
			}
			sql.append(")\n");
		}
		if (!"".equals(orgCode)) {
			sql.append("AND TMD.DEALER_ID IN\n");
			sql.append("       (SELECT TD.DEALER_ID\n");  
			sql.append("          FROM TM_DEALER TD\n");  
			sql.append("         START WITH TD.DEALER_ID IN\n");  
			sql.append("                    (SELECT TDOR.DEALER_ID\n");  
			sql.append("                       FROM TM_DEALER_ORG_RELATION TDOR\n");  
			sql.append("                      WHERE TDOR.ORG_ID IN\n");  
			sql.append("                            (SELECT ORG_ID\n");  
			sql.append("                               FROM TM_ORG ORG\n");  
			sql.append("                              WHERE ORG.STATUS = "+Constant.STATUS_ENABLE+"\n");  
			sql.append("                              START WITH ORG_CODE = '"+orgCode+"'\n");  
			sql.append("                             CONNECT BY PRIOR ORG.ORG_ID = ORG.PARENT_ORG_ID))\n");  
			sql.append("        CONNECT BY PRIOR TD.DEALER_ID = TD.PARENT_DEALER_D)\n");

		}
		
		if(!CommonUtils.isNullString(orgId)) {
			sql.append(ReqQueryDao.limitOrg("TMD", "dealer_id", orgId)) ;
		}
		
	
		sql.append("   AND TMV.LIFE_CYCLE = "+Constant.VEHICLE_LIFE_04+") XX\n");  
		sql.append("   WHERE (XX.IS_FLEET='是' AND XX.FLEET_NAME IS NOT NULL) OR XX.IS_FLEET='否'");
		
		return dao.pageQuery(sql.toString(), null, dao.getFunName());
	}
	
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * 根据主键查询实销信息
	 * @param orderId
	 * @return
	 */
	public List<Map<String, Object>> queryByOrderId(String orderId){
		String sql = "select * from tt_dealer_actual_sales where order_id = ?";
		List<Object> params = new ArrayList<Object>();
		params.add(orderId);
		return pageQuery(sql, params, this.getFunName());
	}
	
	/**
	 * @Title : queryReportInfoList
	 * @Description: 实销信息查询结果展示
	 * @return : PageResult<Map<String,Object>>
	 * @throws
	 * @LastUpdate :2010-6-30
	 */
	public static PageResult<Map<String, Object>> queryReportInfoList_CVS(Map<String,String> map,int pageSize, int curPage) {
		String customer_type = (String) map.get("customer_type"); 	// 客户类型
		String customer_name = (String) map.get("customer_name"); 	// 客户名称
		String vin = (String) map.get("vin"); 						// VIN
		String customer_phone = (String) map.get("customer_phone"); // 客户电话
		String materialCode = (String) map.get("materialCode"); 	// 选择物料组
		String is_fleet = (String) map.get("is_fleet"); 			// 是否集团客户
		String fleet_name = (String) map.get("fleet_name"); 		// 集团客户名称
		String contract_no = (String) map.get("contract_no"); 		// 集团客户合同
		String startDate = (String) map.get("startDate"); 			// 上报日期:开始
		String endDate = (String) map.get("endDate"); 				// 上报日期 ：截止
		String dealerCode = (String) map.get("dealerCode"); 		// 经销商代码
		String orgCode=(String)map.get("orgCode");					// 区域
		String areaId = (String)map.get("areaId");					// 业务范围
		String orgId = map.get("orgId") ;
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT XX.DEALER_CODE,\n");
		sql.append("XX.DEALER_SHORTNAME,\n");
		sql.append("XX.CTM_NAME,\n");
		sql.append("XX.CTM_TYPE,\n");
		sql.append("XX.IS_FLEET,\n");
		sql.append("XX.FLEET_NAME,\n");
		sql.append("XX.CONTRACT_NO, \n");
		sql.append("XX.MAIN_PHONE,\n");
		sql.append("XX.MATERIAL_CODE,\n");
		sql.append("XX.MATERIAL_NAME,\n");
		sql.append("XX.VIN,\n");
		sql.append("XX.CAR_CHARACTOR,\n");
		sql.append("XX.ENGINE_NO,\n");
		sql.append("XX.PRODUCT_DATE ,\n");
		sql.append("XX.SALES_DATE,\n");
		sql.append("XX.CTM_ID,\n");
		sql.append("XX.VEHICLE_ID,\n");
		sql.append("XX.ORG_NAME,\n");
		sql.append("XX.SALES_CON_NAME,\n");
		sql.append(" XX.REGION_NAME,XX.MONEY FROM (\n");
		sql.append("SELECT TMD.DEALER_CODE, --经销商代码\n");
		sql.append("       TMD.DEALER_SHORTNAME, --经销商名称\n");  
		sql.append("       TMD.DEALER_ID, \n");  
		sql.append("       TTC.CTM_NAME, --客户名称\n");  
		sql.append("       TTC.CTM_TYPE, --客户类型\n");  
		sql.append("       TTS.IS_FLEET, --是否集团客户\n");  
		sql.append("       TF.FLEET_NAME, --集团客户名称\n");  
		sql.append("       TFC.CONTRACT_NO, --集团客户合同\n");  
		sql.append("       decode(TTC.CTM_TYPE, ").append(Constant.CUSTOMER_TYPE_01).append(",TTC.MAIN_PHONE, TTC.company_phone) MAIN_PHONE, --主要联系电话\n");   
		sql.append("       M.MATERIAL_CODE, --物料代码\n");  
		sql.append("       M.MATERIAL_NAME, --物料名称\n");  
		sql.append("       TSC.NAME SALES_CON_NAME, \n");  
		sql.append("       TMV.VIN, --VIN\n"); 
		sql.append("TTS.CAR_CHARACTOR, --车辆性质\n");
		sql.append("       TMV.ENGINE_NO, --VIN\n"); 
		sql.append("	   TO_CHAR(TMV.PRODUCT_DATE, 'yyyy-MM-dd') PRODUCT_DATE ,\n");
		sql.append("       TO_CHAR(TTS.SALES_DATE, 'yyyy-MM-dd') SALES_DATE, --实销时间\n");  
		sql.append("       TTC.CTM_ID,\n");
		sql.append("       TMV.VEHICLE_ID,\n");
		sql.append("       VOD.ROOT_ORG_NAME ORG_NAME,VOD.REGION_NAME REGION_NAME,TTS.MONEY \n");
		sql.append("  FROM TT_DEALER_ACTUAL_SALES TTS,\n");  
		sql.append("       TM_DEALER              TMD,\n");  
		sql.append("       TT_SALES_CONSULTANT    TSC,\n");  
		sql.append("       TT_CUSTOMER            TTC,\n");  
		sql.append("       TM_VEHICLE             TMV,\n");  
		sql.append("       TM_VHCL_MATERIAL       M,\n");  
		sql.append("       TM_FLEET               TF,\n");  
		sql.append("       TT_FLEET_CONTRACT      TFC,vw_org_dealer VOD\n");  
		sql.append(" WHERE TTS.DEALER_ID = TMD.DEALER_ID\n");  
		sql.append("   AND TTS.SALES_CON_ID = TSC.ID(+) \n");
		sql.append("   AND VOD.DEALER_ID=TMD.DEALER_ID \n");
//		sql.append("   AND TC1.CODE_ID = TTC.CTM_TYPE \n");
//		sql.append("   AND TC2.CODE_ID = TTS.IS_FLEET");
		sql.append("   AND TTS.CTM_ID = TTC.CTM_ID\n");  
		sql.append("   AND TTS.VEHICLE_ID = TMV.VEHICLE_ID\n");  
		sql.append("   AND TMV.MATERIAL_ID = M.MATERIAL_ID\n");  
		sql.append("   AND TTS.FLEET_ID = TF.FLEET_ID(+)\n");  
		sql.append("   AND TTS.IS_RETURN='"+Constant.IF_TYPE_NO+"'");
		sql.append("   AND TTS.CONTRACT_ID = TFC.CONTRACT_ID(+)\n");  
		if (!"".equals(customer_type)) {
			sql.append("   AND TTC.CTM_TYPE = "+customer_type+"\n");
		}
		if (!"".equals(customer_name)) {
			sql.append("   AND TTC.CTM_NAME LIKE '%"+customer_name.trim()+"%'\n");
		}
		if (!"".equals(vin)) {
			sql.append(GetVinUtil.getVins(vin, "TMV"));
		}
		if (!"".equals(customer_phone)) {
			sql.append("   AND TTC.MAIN_PHONE LIKE '%"+customer_phone.trim()+"%'\n");
		}
		if (null != materialCode && !"".equals(materialCode)) {
			String[] materialCodes = materialCode.split(",");
			if (null != materialCodes && materialCodes.length > 0) {
				StringBuffer buffer = new StringBuffer();
				for (int i = 0; i < materialCodes.length; i++) {
					buffer.append("'").append(materialCodes[i]).append("'").append(",");
				}
				buffer = buffer.deleteCharAt(buffer.length() - 1);
				sql.append("   AND ((TMV.PACKAGE_ID IN (SELECT G.GROUP_ID\n");
				sql.append("FROM TM_VHCL_MATERIAL_GROUP G \n");
				sql.append("WHERE G.GROUP_CODE IN ("+ buffer.toString() + ")))\n");
				sql.append("       OR (TMV.MODEL_ID IN (SELECT G.GROUP_ID \n");
				sql.append("                             FROM TM_VHCL_MATERIAL_GROUP G \n");
				sql.append("                            WHERE G.GROUP_CODE IN ("+ buffer.toString() + "))) \n");
				sql.append("       OR (TMV.SERIES_ID IN (SELECT G.GROUP_ID \n");
				sql.append("                              FROM TM_VHCL_MATERIAL_GROUP G \n");
				sql.append("                             WHERE G.GROUP_CODE IN ("+ buffer.toString() + ")))) \n");
			}
		}
		if (!"".equals(is_fleet)) {
			sql.append("   AND TTS.IS_FLEET = "+is_fleet+"\n");
		}
		if (!"".equals(fleet_name)) {
			sql.append("   AND TF.FLEET_NAME LIKE '%"+fleet_name+"%'\n");
		}
		if (!"".equals(contract_no)) {
			sql.append("   AND TFC.CONTRACT_NO LIKE '%"+contract_no+"%'\n");
		}
		if (null != startDate && !"".equals(startDate)) {
			sql.append("   AND TTS.SALES_DATE >= TO_DATE('"+ startDate+" 00:00:00','yyyy-MM-dd HH24:MI:SS')\n");
		}
		if (null != endDate && !"".equals(endDate)) {
			sql.append("   AND TTS.SALES_DATE  <= TO_DATE('"+endDate+" 23:59:59','yyyy-MM-dd HH24:MI:SS')\n");
		}
		if (null != dealerCode && !"".equals(dealerCode)) {
			String[] array = dealerCode.split(",");
			sql.append("   AND TMD.DEALER_CODE IN(\n");
			for (int i = 0; i < array.length; i++) {
				sql.append("'" + array[i] + "'");
				if (i != array.length - 1) {
					sql.append(",");
				}
			}
			sql.append(")\n");
		}
		if (!"".equals(orgCode)) {
			sql.append("AND TMD.DEALER_ID IN\n");
			sql.append("       (SELECT TD.DEALER_ID\n");  
			sql.append("          FROM TM_DEALER TD\n");  
			sql.append("         START WITH TD.DEALER_ID IN\n");  
			sql.append("                    (SELECT TDOR.DEALER_ID\n");  
			sql.append("                       FROM TM_DEALER_ORG_RELATION TDOR\n");  
			sql.append("                      WHERE TDOR.ORG_ID IN\n");  
			sql.append("                            (SELECT ORG_ID\n");  
			sql.append("                               FROM TM_ORG ORG\n");  
			sql.append("                              WHERE ORG.STATUS = "+Constant.STATUS_ENABLE+"\n");  
			sql.append("                              START WITH ORG_CODE = '"+orgCode+"'\n");  
			sql.append("                             CONNECT BY PRIOR ORG.ORG_ID = ORG.PARENT_ORG_ID))\n");  
			sql.append("        CONNECT BY PRIOR TD.DEALER_ID = TD.PARENT_DEALER_D)\n");

		}
		
		if(!CommonUtils.isNullString(orgId)) {
			sql.append(ReqQueryDao.limitOrg("TMD", "dealer_id", orgId)) ;
		}
		
		if (null != areaId && !"".equals(areaId)) {
			sql.append("           and exists\n");  
			sql.append("         (select 1\n");  
			sql.append("                  from tm_dealer_business_area tdba\n");  
			sql.append("                 where 1 = 1\n");  
			sql.append("                   and tdba.dealer_id = TMD.dealer_id\n");  
			sql.append("                   and tdba.area_id IN (").append(areaId).append("))\n");  
		}
		
		sql.append("   AND TMV.LIFE_CYCLE = "+Constant.VEHICLE_LIFE_04+"\n");  
		sql.append(" UNION \n");
		sql.append("SELECT TMD.DEALER_CODE, --经销商代码\n");
		sql.append("       TMD.DEALER_SHORTNAME, --经销商名称\n"); 
		sql.append("       TMD.DEALER_ID, \n");  
		sql.append("       TTC.CTM_NAME, --客户名称\n");  
		sql.append("       TTC.CTM_TYPE, --客户类型\n");  
		sql.append("       TTS.IS_FLEET, --是否集团客户\n");  
		sql.append("       TCP.PACT_NAME FLEET_NAME, --集团客户名称\n");  
		sql.append("       TCP.PACT_NAME CONTRACT_NO, --集团客户合同\n");  
		sql.append("       decode(TTC.CTM_TYPE, ").append(Constant.CUSTOMER_TYPE_01).append(",TTC.MAIN_PHONE, TTC.company_phone) MAIN_PHONE, --主要联系电话\n");  
		sql.append("       M.MATERIAL_CODE, --物料代码\n");  
		sql.append("       M.MATERIAL_NAME, --物料名称\n");  
		sql.append("       TSC.NAME SALES_CON_NAME, \n");  
		sql.append("       TMV.VIN, --VIN\n"); 
		sql.append("TTS.CAR_CHARACTOR, --车辆性质\n");
		sql.append("       TMV.ENGINE_NO, --VIN\n"); 
		sql.append("	   TO_CHAR(TMV.PRODUCT_DATE, 'yyyy-MM-dd') PRODUCT_DATE ,\n");
		sql.append("       TO_CHAR(TTS.SALES_DATE, 'yyyy-MM-dd') SALES_DATE, --实销时间\n");  
		sql.append("       TTC.CTM_ID,\n");
		sql.append("       TMV.VEHICLE_ID,\n");
		sql.append("       VOD.ROOT_ORG_NAME ORG_NAME,VOD.REGION_NAME REGION_NAME,TTS.MONEY \n");
		sql.append("  FROM TT_DEALER_ACTUAL_SALES TTS,\n");  
		sql.append("       TT_SALES_CONSULTANT    TSC,\n");  
		sql.append("       TM_DEALER              TMD,\n");  
		sql.append("       TT_CUSTOMER            TTC,\n");  
		sql.append("       TM_VEHICLE             TMV,\n");  
		sql.append("       TM_VHCL_MATERIAL       M,\n");  
		sql.append("       TM_FLEET               TF,\n");  
		sql.append("       TT_FLEET_CONTRACT      TFC,TM_COMPANY_PACT TCP,vw_org_dealer VOD\n");  
		sql.append(" WHERE TTS.DEALER_ID = TMD.DEALER_ID\n"); 
		sql.append("   AND TTS.SALES_CON_ID = TSC.ID(+) \n");
		sql.append("   AND VOD.DEALER_ID=TMD.DEALER_ID \n");
		sql.append("   AND TTS.CTM_ID = TTC.CTM_ID\n");  
		sql.append("   AND TTS.VEHICLE_ID = TMV.VEHICLE_ID\n");  
		sql.append("   AND TMV.MATERIAL_ID = M.MATERIAL_ID\n");  
		sql.append("   AND TTS.FLEET_ID = TCP.PACT_ID\n");  
		sql.append("   AND TTS.IS_RETURN='"+Constant.IF_TYPE_NO+"'");
		sql.append("   AND TTS.CONTRACT_ID = TFC.CONTRACT_ID(+)\n");  
		if (!"".equals(customer_type)) {
			sql.append("   AND TTC.CTM_TYPE = "+customer_type+"\n");
		}
		if (!"".equals(customer_name)) {
			sql.append("   AND TTC.CTM_NAME LIKE '%"+customer_name.trim()+"%'\n");
		}
		if (!"".equals(vin)) {
			sql.append(GetVinUtil.getVins(vin, "TMV"));
		}
		if (!"".equals(customer_phone)) {
			sql.append("   AND TTC.MAIN_PHONE LIKE '%"+customer_phone.trim()+"%'\n");
		}
		if (null != materialCode && !"".equals(materialCode)) {
			String[] materialCodes = materialCode.split(",");
			if (null != materialCodes && materialCodes.length > 0) {
				StringBuffer buffer = new StringBuffer();
				for (int i = 0; i < materialCodes.length; i++) {
					buffer.append("'").append(materialCodes[i]).append("'").append(",");
				}
				buffer = buffer.deleteCharAt(buffer.length() - 1);
				sql.append("   AND ((TMV.PACKAGE_ID IN (SELECT G.GROUP_ID\n");
				sql.append("FROM TM_VHCL_MATERIAL_GROUP G \n");
				sql.append("WHERE G.GROUP_CODE IN ("+ buffer.toString() + ")))\n");
				sql.append("       OR (TMV.MODEL_ID IN (SELECT G.GROUP_ID \n");
				sql.append("                             FROM TM_VHCL_MATERIAL_GROUP G \n");
				sql.append("                            WHERE G.GROUP_CODE IN ("+ buffer.toString() + "))) \n");
				sql.append("       OR (TMV.SERIES_ID IN (SELECT G.GROUP_ID \n");
				sql.append("                              FROM TM_VHCL_MATERIAL_GROUP G \n");
				sql.append("                             WHERE G.GROUP_CODE IN ("+ buffer.toString() + ")))) \n");
			}
		}
		if (!"".equals(is_fleet)) {
			sql.append("   AND TTS.IS_FLEET = "+is_fleet+"\n");
		}
		if (!"".equals(fleet_name)) {
			sql.append("   AND TF.FLEET_NAME LIKE '%"+fleet_name+"%'\n");
		}
		if (!"".equals(contract_no)) {
			sql.append("   AND TFC.CONTRACT_NO LIKE '%"+contract_no+"%'\n");
		}
		if (null != startDate && !"".equals(startDate)) {
			sql.append("   AND TTS.SALES_DATE >= TO_DATE('"+ startDate+" 00:00:00','yyyy-MM-dd HH24:MI:SS')\n");
		}
		if (null != endDate && !"".equals(endDate)) {
			sql.append("   AND TTS.SALES_DATE  <= TO_DATE('"+endDate+" 23:59:59','yyyy-MM-dd HH24:MI:SS')\n");
		}
		if (null != dealerCode && !"".equals(dealerCode)) {
			String[] array = dealerCode.split(",");
			sql.append("   AND TMD.DEALER_CODE IN(\n");
			for (int i = 0; i < array.length; i++) {
				sql.append("'" + array[i] + "'");
				if (i != array.length - 1) {
					sql.append(",");
				}
			}
			sql.append(")\n");
		}
		if (!"".equals(orgCode)) {
			sql.append("AND TMD.DEALER_ID IN\n");
			sql.append("       (SELECT TD.DEALER_ID\n");  
			sql.append("          FROM TM_DEALER TD\n");  
			sql.append("         START WITH TD.DEALER_ID IN\n");  
			sql.append("                    (SELECT TDOR.DEALER_ID\n");  
			sql.append("                       FROM TM_DEALER_ORG_RELATION TDOR\n");  
			sql.append("                      WHERE TDOR.ORG_ID IN\n");  
			sql.append("                            (SELECT ORG_ID\n");  
			sql.append("                               FROM TM_ORG ORG\n");  
			sql.append("                              WHERE ORG.STATUS = "+Constant.STATUS_ENABLE+"\n");  
			sql.append("                              START WITH ORG_CODE = '"+orgCode+"'\n");  
			sql.append("                             CONNECT BY PRIOR ORG.ORG_ID = ORG.PARENT_ORG_ID))\n");  
			sql.append("        CONNECT BY PRIOR TD.DEALER_ID = TD.PARENT_DEALER_D)\n");

		}
		if(!CommonUtils.isNullString(orgId)) {
			sql.append(ReqQueryDao.limitOrg("TMD", "dealer_id", orgId)) ;
		}
		if (null != areaId && !"".equals(areaId)) {
			sql.append("           and exists\n");  
			sql.append("         (select 1\n");  
			sql.append("                  from tm_dealer_business_area tdba\n");  
			sql.append("                 where 1 = 1\n");  
			sql.append("                   and tdba.dealer_id = TMD.dealer_id\n");  
			sql.append("                   and tdba.area_id IN (").append(areaId).append("))\n");  
		}
		sql.append("   AND TMV.LIFE_CYCLE = "+Constant.VEHICLE_LIFE_04+") XX\n");  
		sql.append("   WHERE (XX.IS_FLEET=10041001 AND XX.FLEET_NAME IS NOT NULL) OR XX.IS_FLEET=10041002"); 
		
		//sql.append(" ORDER BY TTS.SALES_DATE DESC\n");

//		
//			sql.append("SELECT TMD.DEALER_CODE,\n");
//			sql.append("       TMD.DEALER_SHORTNAME,\n");
//			sql.append("       DECODE(TTC.CTM_NAME, NULL, TTC.COMPANY_NAME,TTC.CTM_NAME) C_NAME, --客户名称\n");
//			sql.append("       TTC.CTM_TYPE, --客户类型\n");
//			sql.append("       TTS.IS_FLEET, --是否集团客户\n");
//			sql.append("       DECODE(TTC.CTM_NAME, NULL, TTC.COMPANY_PHONE,TTC.MAIN_PHONE) MAIN_PHONE, --主要联系电话\n");
//			sql.append("       TMVM.MATERIAL_CODE,\n");
//			sql.append("       TMVM.MATERIAL_NAME,\n");
//			sql.append("       TMV.VIN,\n");
//			sql.append("       TO_CHAR(TTS.SALES_DATE, 'yyyy-MM-dd') SALES_DATE, --实销时间\n");
//			sql.append("       TMV.VEHICLE_ID,\n");
//			sql.append("       TTC.CTM_ID\n");
//			sql.append("  FROM TT_DEALER_ACTUAL_SALES TTS,\n");
//			sql.append("       TT_CUSTOMER            TTC,\n");
//			sql.append("       TM_VEHICLE             TMV,\n");
//			sql.append("       TM_VHCL_MATERIAL       TMVM,\n");
//			sql.append("       TM_DEALER_BUSINESS_AREA TDBA,\n");
//			sql.append("       TM_ORG              TMO,\n");
//			sql.append("       TM_DEALER_ORG_RELATION TDOR,\n");
//			sql.append("       TM_DEALER              TMD\n");
//			sql.append(" WHERE TTS.CTM_ID = TTC.CTM_ID\n");
//			sql.append("   AND TTS.VEHICLE_ID = TMV.VEHICLE_ID\n");
//			sql.append("   AND TDBA.DEALER_ID=TMD.DEALER_ID\n");
//			sql.append("   AND TTS.DEALER_ID=TMD.DEALER_ID\n");
//			sql.append("   AND TDBA.AREA_ID=2010010100000004\n");
//			sql.append("   AND TMV.MATERIAL_ID = TMVM.MATERIAL_ID\n");
//			sql.append("AND TMO.ORG_ID=TDOR.ORG_ID AND TMD.DEALER_ID=TDOR.DEALER_ID\n");
//			sql.append("   AND TMV.LIFE_CYCLE = " + Constant.VEHICLE_LIFE_04
//							+ "\n");
//			sql.append("   AND TMD.DEALER_ID = TMV.DEALER_ID\n");
//			// 客户类型
//			if (null != orgId && !"".equals(orgId)&& !"-1".equals(orgId)) {
//				sql.append("AND TMO.ORG_CODE='"+orgId+"'\n");
//			}
//			// 客户类型
//			if (null != customer_type && !"".equals(customer_type)&& !"-1".equals(customer_type)) {
//				sql.append("   AND TTC.CTM_TYPE =" + customer_type + "\n");
//			}
//			// 客户名称
//			if (null != customer_name && !"".equals(customer_name)) {
//				sql.append("   AND (TTC.CTM_NAME LIKE '%" + customer_name.trim()+ "%' OR TTC.COMPANY_S_NAME LIKE '%" + customer_name.trim()+ "%')\n");
//			}
//			// VIN
//			if (null != vin && !"".equals(vin)) {
//				sql.append(GetVinUtil.getVins(vin, "TMV"));
//			}
//			// 状态
//			if (null != status && !"".endsWith(status) && !"-1".equals(status)) {
//				sql.append("   AND TTS.STATUS = " + status + "\n");
//			}
//			// 客户电话
//			if (null != customer_phone && !"".equals(customer_phone)) {
//				sql.append("   AND (TTC.MAIN_PHONE LIKE '%" + customer_phone.trim()+ "%' OR TTC.COMPANY_PHONE LIKE '%" + customer_phone.trim()+ "%')\n");
//			}
//			// 选择物料组
//			if (null != materialCode && !"".equals(materialCode)) {
//				String[] materialCodes = materialCode.split(",");
//				if (null != materialCodes && materialCodes.length > 0) {
//					StringBuffer buffer = new StringBuffer();
//					for (int i = 0; i < materialCodes.length; i++) {
//						buffer.append("'").append(materialCodes[i]).append("'").append(",");
//					}
//					buffer = buffer.deleteCharAt(buffer.length() - 1);
//					sql.append("   AND ((TMV.PACKAGE_ID IN (SELECT G.GROUP_ID\n");
//					sql.append("FROM TM_VHCL_MATERIAL_GROUP G \n");
//					sql.append("WHERE G.GROUP_CODE IN ("+ buffer.toString() + ")))\n");
//					sql.append("       OR (TMV.MODEL_ID IN (SELECT G.GROUP_ID \n");
//					sql.append("                             FROM TM_VHCL_MATERIAL_GROUP G \n");
//					sql.append("                            WHERE G.GROUP_CODE IN ("+ buffer.toString() + "))) \n");
//					sql.append("       OR (TMV.SERIES_ID IN (SELECT G.GROUP_ID \n");
//					sql.append("                              FROM TM_VHCL_MATERIAL_GROUP G \n");
//					sql.append("                             WHERE G.GROUP_CODE IN ("+ buffer.toString() + ")))) \n");
//				}
//			}
//			// 是否集团客户
//			if (null != is_fleet && !"".equals(is_fleet) && !"-1".equals(is_fleet)) {
//				sql.append("   AND TTS.IS_FLEET = " + is_fleet + "\n");
//			}
//
//			if (!is_fleet.equals(Constant.IF_TYPE_NO.toString())) {
//				// 集团客户名称
//				if (null != fleet_name && !"".equals(fleet_name)) {
//					sql.append("   AND TTC.COMPANY_S_NAME LIKE '%"+ fleet_name.trim() + "%'\n");
//				}
//				// 集团客户合同
//				if (null != contract_no && !"".equals(contract_no)) {
//					sql.append("   AND TTS.CONTRACT_NO LIKE '%"+ contract_no.trim() + "%'\n\n");
//				}
//			}
//			// 上报日期
//			if (null != startDate && !"".equals(startDate)) {
//				sql.append("   AND TTS.SALES_DATE >= TO_DATE('" + startDate+ "','yyyy-MM-dd')\n");
//			}
//			if (null != endDate && !"".equals(endDate)) {
//				sql.append("   AND TTS.SALES_DATE  <= (TO_DATE('" + endDate+ "','yyyy-MM-dd')+1)\n");
//			}
//
//			if (null != dealerCode && !"".equals(dealerCode)) {
//				String[] array = dealerCode.split(",");
//				sql.append("   AND TMD.DEALER_CODE IN(\n");
//				for (int i = 0; i < array.length; i++) {
//					sql.append("'" + array[i] + "'");
//					if (i != array.length - 1) {
//						sql.append(",");
//					}
//				}
//				sql.append(")\n");
//			}
//			sql.append(" ORDER BY TTS.SALES_DATE DESC\n");
		return dao.pageQuery(sql.toString(), null, dao.getFunName(), pageSize,curPage);
	}

}