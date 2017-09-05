package com.infodms.dms.dao.sales.customerInfoManage;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.common.Constant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.dao.sales.storageManage.CheckVehicleDAO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.businessUtil.GetVinUtil;
import com.infoservice.po3.POFactory;
import com.infoservice.po3.POFactoryBuilder;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;
import com.infoservice.po3.core.callback.DAOCallback;

public class SalesReportDAO extends BaseDao {
	public static Logger logger = Logger.getLogger(SalesReportDAO.class);
	private static POFactory factory = POFactoryBuilder.getInstance();
	private static CheckVehicleDAO dao = CheckVehicleDAO.getInstance();
	private static SalesReportDAO salesReportDAO = new SalesReportDAO();
	
	public static final SalesReportDAO getInstance() {
		if (salesReportDAO == null) {
			salesReportDAO = new SalesReportDAO();
		}
		return salesReportDAO;
	}
	private SalesReportDAO() {}
	
	/***
	 *查询可"实销上报"的车辆列表
	 * 
	 * @param :
	 * @return
	 * @throws Exception
	 */
	public static PageResult<Map<String, Object>> getCanReportVehiclet(
			String areaId,String dealer_Id, String materialCode, String vin, int pageSize,
			int curPage) {
		StringBuffer sql = new StringBuffer("");

		sql.append("SELECT TMV.VEHICLE_ID,\n");
		sql.append("       TO_CHAR(TMV.PRODUCT_DATE,'yyyy-MM-dd') PRODUCT_DATE, --生产日期\n");
		sql.append("       TMVM.MATERIAL_CODE, --物料代码\n");
		sql.append("       G.GROUP_NAME PACKAGE_NAME, --配置\n");
		sql.append("       TMVM.COLOR_NAME COLOR, --颜色\n");
		sql.append("       TMV.VIN, --VIN\n");
		sql.append("       TMV.ENGINE_NO, --VIN\n");
		sql.append("       TO_CHAR(TMV.STORAGE_DATE, 'yyyy-MM-dd') STORAGE_DATE --验收入库日期\n");
		sql.append("  FROM TM_VEHICLE TMV, TM_VHCL_MATERIAL TMVM, TM_VHCL_MATERIAL_GROUP G\n");
		sql.append(" WHERE TMV.MATERIAL_ID = TMVM.MATERIAL_ID\n");
		sql.append("   AND TMV.PACKAGE_ID = G.GROUP_ID\n");
		sql.append("   AND TMV.LIFE_CYCLE = " + Constant.VEHICLE_LIFE_03+ "\n");
		sql.append("   AND TMV.LOCK_STATUS = "+Constant.LOCK_STATUS_01+"\n");  
		sql.append("   AND TMV.DEALER_ID IN (" + dealer_Id + ")\n");
		/*sql.append("and not exists (select 1\n");
		sql.append("          from tt_vs_vehicle_transfer tvvt\n");  
		sql.append("         where tvvt.vehicle_id = tmv.vehicle_id\n");  
		sql.append("           and tvvt.check_status = ").append(Constant.DISPATCH_STATUS_01).append(")\n");*/

		if (null != areaId && !"".equals(areaId)) {
			sql.append("   AND TMV.AREA_ID IN ("+areaId+")");
		}
		if (null != materialCode && !"".equals(materialCode)) {
			String[] materialCodes = materialCode.split(",");
			if (null != materialCodes && materialCodes.length > 0) {
				StringBuffer buffer = new StringBuffer();
				for (int i = 0; i < materialCodes.length; i++) {
					buffer.append("'").append(materialCodes[i]).append("'")
							.append(",");
				}
				buffer = buffer.deleteCharAt(buffer.length() - 1);
				sql.append("   AND ((TMV.PACKAGE_ID IN (SELECT G.GROUP_ID \n");
				sql.append("                               FROM TM_VHCL_MATERIAL_GROUP G \n");
				sql.append("                              WHERE G.GROUP_CODE IN (" + buffer.toString() + "))) \n");
				sql.append("       OR (TMV.MODEL_ID IN (SELECT G.GROUP_ID \n");
				sql.append("                             FROM TM_VHCL_MATERIAL_GROUP G \n");
				sql.append("                            WHERE G.GROUP_CODE IN (" + buffer.toString() + "))) \n");
				sql.append("       OR (TMV.SERIES_ID IN (SELECT G.GROUP_ID \n");
				sql.append("                              FROM TM_VHCL_MATERIAL_GROUP G \n");
				sql.append("                             WHERE G.GROUP_CODE IN (" + buffer.toString() + ")))) \n");
			}

		}

		if (null != vin && !"".equals(vin)) {
			sql.append(GetVinUtil.getVins(vin, "TMV"));
		}
		sql.append(" ORDER BY TMV.STORAGE_DATE DESC,TMV.VEHICLE_ID\n");

		return dao.pageQuery(sql.toString(),null,"com.infodms.dms.dao.sales.customerInfoManage.SalesReportDAO.getCanReportVehiclet",pageSize, curPage);
	}
	
	/***
	 * 查询可"实销上报"的车辆列表
	 * 
	 * @param :
	 * @return
	 * @throws Exception
	 */
	public static PageResult<Map<String, Object>> getCanReportVehiclet(Map<String, Object> map, int pageSize, int curPage)
	{
		/****************************** 页面查询字段start **************************/
		String materialCode = CommonUtils.checkNull((String) map.get("materialCode")); // 物料编码
		String vin = CommonUtils.checkNull((String) map.get("vin")); // vin
		String areaId = CommonUtils.checkNull((String) map.get("areaId")); // 产地
		String storageStartdate = CommonUtils.checkNull((String) map.get("storageStartdate")); // 验收开始时间
		String storageEnddate = CommonUtils.checkNull((String) map.get("storageEnddate")); // 验收结束时间
		String poseId = CommonUtils.checkNull((String) map.get("poseId"));
		String dealer_Id = CommonUtils.checkNull((String) map.get("dealer_Id"));// 经销商ID
		String hgz_no = CommonUtils.checkNull((String) map.get("hgz_no"));// 合格证扫描
		List<Object> params = new ArrayList<Object>();
		/****************************** 页面查询字段end ***************************/
		StringBuffer sbSql = new StringBuffer("");
		
		sbSql.append("SELECT TMV.VEHICLE_ID,\n");
		sbSql.append("       TO_CHAR(TMV.PRODUCT_DATE, 'yyyy-MM-dd') PRODUCT_DATE, --生产日期\n");
		sbSql.append("       VTM.MATERIAL_CODE, --物料代码\n");
		sbSql.append("       VTM.MATERIAL_NAME, --物料代码\n");
		sbSql.append("       VTM.SERIES_NAME,\n");
		sbSql.append("       VTM.MODEL_NAME,\n");
		sbSql.append("       VTM.PACKAGE_NAME, --配置\n");
		sbSql.append("       VTM.COLOR_NAME COLOR, --颜色\n");
		sbSql.append("       TMV.VIN, --VIN\n");
		sbSql.append("       TMV.HGZ_NO,\n");
		sbSql.append("       TMV.ENGINE_NO,\n");
		sbSql.append("       TO_CHAR(TMV.STORAGE_DATE, 'yyyy-MM-dd') STORAGE_DATE, --验收入库日期\n");
		sbSql.append("       TDS.CALLCENTER_CHECK_STATUS, --呼叫中心审核状态\n");
		sbSql.append("       TDS.SALES_CHECK_STATUS --销售部审核状态\n");
		sbSql.append("  FROM TM_VEHICLE             TMV,\n");
		sbSql.append("       TT_DEALER_ACTUAL_SALES TDS,\n");
		sbSql.append("       VW_MATERIAL_GROUP_MAT  VTM\n");
		sbSql.append(" WHERE TMV.MATERIAL_ID = VTM.MATERIAL_ID\n");
		sbSql.append("   AND TMV.VEHICLE_ID = TDS.VEHICLE_ID(+)"); 
		sbSql.append("   AND TDS.VEHICLE_ID(+) = 10041002");
		sbSql.append("   AND TMV.LIFE_CYCLE = " + Constant.VEHICLE_LIFE_03 + "\n");
		sbSql.append("   AND TMV.LOCK_STATUS = " + Constant.LOCK_STATUS_01 + "\n");
		sbSql.append("   AND TMV.DEALER_ID = ?\n");
		
		params.add(Long.valueOf(dealer_Id));
		
		if (null != materialCode && !"".equals(materialCode))
		{
			sbSql.append(MaterialGroupManagerDao.getMaterialGroupQuerySqlByPar("TMV.PACKAGE_ID", materialCode, params));
		}
		
		if (null != vin && !"".equals(vin))
		{
			sbSql.append("AND TMV.VIN LIKE ? \n");
			params.add("%" + vin + "%");
		}
		if (null != areaId && !"".equals(areaId))
		{
			sbSql.append(" AND TMV.YIELDLY = ?\n");
			params.add(Long.valueOf(areaId));
		}
		if (!storageStartdate.equals(""))
		{
			sbSql.append("AND TMV.STORAGE_DATE>=TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')\n");
			params.add(storageStartdate + " 00:00:00");
		}
		if (!storageEnddate.equals(""))
		{
			sbSql.append("AND TMV.STORAGE_DATE<=TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')\n");
			params.add(storageEnddate + " 23:59:59");
		}
		if (!hgz_no.equals(""))
		{
			sbSql.append("AND TMV.HGZ_NO=?\n");
			params.add(hgz_no);
		}
		sbSql.append(" ORDER BY TMV.STORAGE_DATE,TMV.VEHICLE_ID DESC");
		
		return dao.pageQuery(sbSql.toString(), params, dao.getFunName(), pageSize, curPage);
	}
	
	/***
	 * 实销信息上报:填写上报信息--查看车辆信息
	 * 
	 * @param :
	 * @return
	 * @throws Exception
	 */
	public static Map<String, Object> getVehicleInfo(String vehicleId)
	{
		StringBuffer sql = new StringBuffer("");
		sql.append("SELECT TMV.VEHICLE_ID,                         \n");
		sql.append("	   TMV.VIN,                          --VIN \n");
		sql.append("	   TMV.DEALER_ID,                          \n");
		sql.append("	   decode(TMV.REC_DEALER_ID,null,TMV.DEALER_ID,TMV.REC_DEALER_ID) REC_DEALER_ID,\n");
		sql.append("	   decode(TMV.REC_DEALER_NAME,'',TMV.DEALER_NAME,TMV.REC_DEALER_NAME) REC_DEALER_NAME,\n");
		sql.append("	   decode(TMV.REC_DEALER_SHORTNAME,'',TMV.DEALER_SHORTNAME,TMV.REC_DEALER_SHORTNAME) REC_DEALER_SHORTNAME,\n");
		sql.append("	   TMV.HGZ_NO,                          --合格证扫描码 \n");
		sql.append("       TMV.ENGINE_NO,                    --发动机号 \n");
		sql.append("       G1.GROUP_NAME SERIES_NAME,        --车系 \n");
		sql.append("       G2.GROUP_NAME MODEL_NAME,         --车型 \n");
		sql.append("       TMVM.MATERIAL_CODE,               --物料代码 \n");
		sql.append("       TMVM.MATERIAL_NAME,               --物料名称 \n");
		sql.append("       TMVM.COLOR_NAME AS COLOR,                        --颜色 \n");
		sql.append("       SALES.IS_OLD_CTM,                 --新/老客户 \n");
		sql.append("       SALES.CAR_CHARACTOR,              --车辆性质 \n");
		sql.append("       SALES.ORDER_ID,                   --实销ID \n");
		sql.append("       SALES.SALES_CHECK_STATUS,                  \n");
		sql.append("       SALES.CALLCENTER_CHECK_STATUS,             \n");
		sql.append("       G2.GROUP_ID                       --车型id\n");
		// sql.append("       SALES.ORDER_ID");
		sql.append("  FROM TM_VEHICLE TMV, \n");
		sql.append("       TM_VHCL_MATERIAL_GROUP G1, \n");
		sql.append("       TM_VHCL_MATERIAL_GROUP G2, \n");
		sql.append("       TM_VHCL_MATERIAL TMVM, \n");
		sql.append("       TT_DEALER_ACTUAL_SALES SALES \n");
		sql.append(" WHERE     TMV.SERIES_ID = G1.GROUP_ID(+) \n");
		sql.append("       AND TMV.MODEL_ID = G2.GROUP_ID(+) \n");
		sql.append("       AND TMV.MATERIAL_ID = TMVM.MATERIAL_ID(+) \n");
		sql.append("	   AND TMV.VEHICLE_ID = SALES.VEHICLE_ID(+)\n");
		sql.append("       AND TMV.VEHICLE_ID = " + vehicleId + " \n");
		sql.append("       ORDER BY SALES.ORDER_ID DESC ");
		// sql.append("       AND SALES.IS_RETURN='"+Constant.IF_TYPE_NO+"'\n");
		return dao.pageQueryMap(sql.toString(), null, dao.getFunName());
	}
	
	public static List<Map<String, Object>> getDealerCalDetailExportList(Map map, String dealerId)
	{
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
		String areaId = (String) map.get("areaId");
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT DECODE(TTC.CTM_NAME, NULL, TTC.COMPANY_NAME, TTC.CTM_NAME) C_NAME, --客户名称\n");
		sql.append(" 	   TTS.SALES_CON_NAME NAME,\n");
		sql.append("       TMD.DEALER_CODE, --经销商代码\n");
		sql.append("       TMD.DEALER_SHORTNAME, --经销商简称\n");
		sql.append("       TC4.CODE_DESC AS IS_SECOND, --是否二手车置换\n");
		sql.append("       TC1.CODE_DESC CTM_TYPE, --客户类型\n");
		sql.append("       TTC.ADDRESS, --联系人地址\n");
		sql.append("       TC2.CODE_DESC IS_FLEET, --是否大客户\n");
		
		sql.append("       tmf.fleet_name, --集团客户名称\n");
		
		sql.append("       DECODE(TTC.Ctm_Type, " + Constant.CUSTOMER_TYPE_02 + ", TTC.COMPANY_PHONE, TTC.MAIN_PHONE) MAIN_PHONE, --主要联系电话\n");
		sql.append("       TMVM.MATERIAL_CODE,\n");
		sql.append("       TMVM.MATERIAL_NAME,\n");
		sql.append("	   TVMG2.GROUP_NAME VT, --�������\n");
		sql.append("       TVMG3.Group_Name VS, --��ϵ���\n");
		sql.append("       TVMG.Group_Name SN, --״̬���\n");
		sql.append("       TMV.COLOR, --��ɫ���\n");
		sql.append("       TMV.VIN,\n");
		sql.append("        TTS.LOANS_YEAR,  TC3.CODE_DESC as LOANS_TYPE,\n");
		sql.append("       TMV.ENGINE_NO, --VIN\n");
		sql.append("       TO_CHAR(TTS.SALES_DATE, 'yyyy-MM-dd') SALES_DATE, --实销时间\n");
		sql.append("       TO_CHAR(TMV.PRODUCT_DATE, 'yyyy-MM-dd') PRODUCT_DATE, --生产日期\n");
		
		// zhumingwei 2011-09-13 begin
		sql.append("       TMV.Color,        --颜色\n");
		sql.append("       (select code.code_desc from tc_code code where code.code_id=ttc.Ctm_Type) Ctm_Type, --客户类型\n");
		sql.append("       (select code.code_desc from tc_code code where code.code_id=TTC.Income) Income,    --家庭月收入\n");
		sql.append("       (select code.code_desc from tc_code code where code.code_id=ttc.profession) profession,  --职业\n");
		sql.append("       (select code.code_desc from tc_code code where code.code_id=ttc.sex)  sex ,  --性别\n");
		sql.append("       (select code.code_desc from tc_code code where code.code_id=ttc.education) education,   --教育程度\n");
		sql.append("       (select code.code_desc from tc_code code where code.code_id=ttc.ctm_form) know_address ,  --了解途径\n");
		sql.append("       (select code.code_desc from tc_code code where code.code_id=tts.sales_reson) sales_reson,  --购买原因\n");
		sql.append("       (select code.code_desc from tc_code code where code.code_id=tts.sales_address) sales_address,   --购买用途\n");
		sql.append("       TO_CHAR(tts.consignation_date, 'yyyy-MM-dd') consignation_date,    --车辆交付日期\n");
		sql.append("       (select code.code_desc from tc_code code where code.code_id=tts.payment) payment, --付款方式\n");
		// zhumingwei 2011-09-13 end
		
		sql.append("       TMV.VEHICLE_ID,\n");
		sql.append("  (TTS.SHOUFU_RATIO*100) as SHOUFU_RATIO,       (SELECT CODE_DESC FROM TC_CODE WHERE CODE_ID=TTS.MORTGAGE_TYPE) AS MORTGAGE_TYPE ,          TTS.PRICE AS SALES_PRICE,      ");
		sql.append("       TTC.CTM_ID\n");
		sql.append("  FROM TT_DEALER_ACTUAL_SALES TTS,\n");
		sql.append("       TT_CUSTOMER            TTC,\n");
		sql.append("       TM_VEHICLE             TMV,\n");
		sql.append("       TM_VHCL_MATERIAL       TMVM,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP   TVMG,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP   TVMG2,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP   TVMG3,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP_R TVMGR,\n");
		sql.append("       TM_DEALER              TMD,\n");
		sql.append("       tm_fleet               tmf,\n");
		sql.append("       TC_CODE       		  TC1,\n");
		sql.append("       TC_CODE       		  TC2,\n");
		sql.append("       TC_CODE       		  TC3,\n");
		sql.append("       TC_CODE       		  TC4\n");
		sql.append(" WHERE TTS.CTM_ID = TTC.CTM_ID\n");
		sql.append("   AND TMVM.MATERIAL_ID = TVMGR.MATERIAL_ID\n");
		sql.append("   AND TVMGR.GROUP_ID = TVMG.GROUP_ID\n");
		sql.append("   AND TVMG.PARENT_GROUP_ID = TVMG2.GROUP_ID\n");
		sql.append("   AND TVMG2.PARENT_GROUP_ID = TVMG3.GROUP_ID\n");
		sql.append("   AND TTS.LOANS_TYPE=   TC3.CODE_ID(+)\n");
		sql.append("   AND TTS.VEHICLE_ID = TMV.VEHICLE_ID\n");
		sql.append("   AND TTC.CTM_TYPE = TC1.CODE_ID\n");
		sql.append("   AND TTS.IS_FLEET = TC2.CODE_ID\n");
		sql.append("   AND TTC.IS_SECOND = TC4.CODE_ID(+)\n");
		sql.append("   AND TTS.DEALER_ID = TMD.DEALER_ID\n");
		
		sql.append("and tts.fleet_id = tmf.fleet_id(+)");
		
		sql.append("   AND  TTS.IS_RETURN =" + Constant.IF_TYPE_NO + "\n");
		if (areaId != null && areaId != "")
		{
			sql.append("   AND TMV.AREA_ID='" + areaId + "'");
		}
		sql.append("   AND TMV.MATERIAL_ID = TMVM.MATERIAL_ID\n");
		sql.append("   AND TMV.LIFE_CYCLE = " + Constant.VEHICLE_LIFE_04 + "\n");
		sql.append("   AND TMV.DEALER_ID IN (" + dealerId + ")\n");
		// 客户类型
		if (null != customer_type && !"".equals(customer_type) && !"-1".equals(customer_type))
		{
			sql.append("   AND TTC.CTM_TYPE =" + customer_type + "\n");
		}
		// 客户名称
		if (null != customer_name && !"".equals(customer_name))
		{
			sql.append("   AND (TTC.CTM_NAME LIKE '%" + customer_name.trim() + "%' OR TTC.COMPANY_S_NAME LIKE '%" + customer_name.trim() + "%')\n");
		}
		// VIN
		if (null != vin && !"".equals(vin))
		{
			sql.append(GetVinUtil.getVins(vin, "TMV"));
		}
		// 客户电话
		if (null != customer_phone && !"".equals(customer_phone))
		{
			sql.append("   AND (TTC.MAIN_PHONE LIKE '%" + customer_phone.trim() + "%' OR TTC.COMPANY_PHONE LIKE '%" + customer_phone.trim() + "%')\n");
		}
		// 选择物料组
		if (null != materialCode && !"".equals(materialCode))
		{
			String[] materialCodes = materialCode.split(",");
			if (null != materialCodes && materialCodes.length > 0)
			{
				StringBuffer buffer = new StringBuffer();
				for (int i = 0; i < materialCodes.length; i++)
				{
					buffer.append("'").append(materialCodes[i]).append("'").append(",");
				}
				buffer = buffer.deleteCharAt(buffer.length() - 1);
				sql.append("   AND ((TMV.PACKAGE_ID IN (SELECT G.GROUP_ID \n");
				sql.append("                               FROM TM_VHCL_MATERIAL_GROUP G \n");
				sql.append("                              WHERE G.GROUP_CODE IN (" + buffer.toString() + "))) \n");
				sql.append("       OR (TMV.MODEL_ID IN (SELECT G.GROUP_ID \n");
				sql.append("                             FROM TM_VHCL_MATERIAL_GROUP G \n");
				sql.append("                            WHERE G.GROUP_CODE IN (" + buffer.toString() + "))) \n");
				sql.append("       OR (TMV.SERIES_ID IN (SELECT G.GROUP_ID \n");
				sql.append("                              FROM TM_VHCL_MATERIAL_GROUP G \n");
				sql.append("                             WHERE G.GROUP_CODE IN (" + buffer.toString() + ")))) \n");
			}
		}
		// 是否大客户
		if (null != is_fleet && !"".equals(is_fleet) && !"-1".equals(is_fleet))
		{
			sql.append("   AND TTS.IS_FLEET = " + is_fleet + "\n");
		}
		if (!is_fleet.equals(Constant.IF_TYPE_NO.toString()))
		{
			// 大客户名称
			if (null != fleet_name && !"".equals(fleet_name))
			{
				sql.append("   AND TTC.COMPANY_S_NAME LIKE '%" + fleet_name.trim() + "%'\n");
			}
			// 大客户合同
			if (null != contract_no && !"".equals(contract_no))
			{
				sql.append("   AND TTS.CONTRACT_NO LIKE '%" + contract_no.trim() + "%'\n\n");
			}
		}
		// 上报日期
		if (null != startDate && !"".equals(startDate))
		{
			sql.append("   AND TTS.SALES_DATE >= TO_DATE('" + startDate + "','yyyy-MM-dd')\n");
		}
		if (null != endDate && !"".equals(endDate))
		{
			sql.append("   AND TTS.SALES_DATE  <= (TO_DATE('" + endDate + "','yyyy-MM-dd')+1)\n");
		}
		sql.append(" ORDER BY TTS.SALES_DATE DESC\n");
		List<Map<String, Object>> list = dao.pageQuery(sql.toString(), null, dao.getFunName());
		return list;
	}
	
	public static List<Map<String, Object>> getDealerCalDetailExportList_CVS(Map map, String dealerId)
	{
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
		String areaId = (String) map.get("areaId");
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT DECODE(TTC.CTM_NAME, NULL, TTC.COMPANY_NAME, TTC.CTM_NAME) C_NAME, --客户名称\n");
		sql.append("       TMD.DEALER_SHORTNAME, --经销商简称\n");
		sql.append("       TC1.CODE_DESC CTM_TYPE, --客户类型\n");
		sql.append("       TTC.ADDRESS, --联系人地址\n");
		sql.append("       TC2.CODE_DESC IS_FLEET, --是否大客户\n");
		sql.append("       DECODE(TTC.Ctm_Type, " + Constant.CUSTOMER_TYPE_02 + ", TTC.COMPANY_PHONE, TTC.MAIN_PHONE) MAIN_PHONE, --主要联系电话\n");
		sql.append("       TMVM.MATERIAL_CODE,\n");
		sql.append("       TMVM.MATERIAL_NAME,\n");
		sql.append("       TMV.VIN,\n");
		sql.append("       TSC.NAME SALES_CON_NAME,\n");
		sql.append("       TMV.ENGINE_NO, --VIN\n");
		sql.append("       TO_CHAR(TTS.SALES_DATE, 'yyyy-MM-dd') SALES_DATE, --实销时间\n");
		sql.append("       TO_CHAR(TMV.PRODUCT_DATE, 'yyyy-MM-dd') PRODUCT_DATE, --生产日期\n");
		sql.append("       TMV.VEHICLE_ID,\n");
		sql.append("       TTC.CTM_ID,\n");
		sql.append("	TC3.CODE_DESC IS_OWN_SALE\n");
		sql.append("  FROM TT_DEALER_ACTUAL_SALES TTS,\n");
		sql.append("       TT_CUSTOMER            TTC,\n");
		sql.append("       TT_SALES_CONSULTANT    TSC,\n");
		sql.append("       TM_VEHICLE             TMV,\n");
		sql.append("       TM_VHCL_MATERIAL       TMVM,\n");
		sql.append("       TM_DEALER              TMD,\n");
		sql.append("       TC_CODE       		  TC1,\n");
		sql.append("       TC_CODE       		  TC2,\n");
		sql.append("		TC_CODE             TC3\n");
		sql.append(" WHERE TTS.CTM_ID = TTC.CTM_ID\n");
		sql.append("   AND TTS.SALES_CON_ID = TSC.ID(+)\n");
		sql.append("   AND TTS.VEHICLE_ID = TMV.VEHICLE_ID\n");
		sql.append("   AND TTC.CTM_TYPE = TC1.CODE_ID\n");
		sql.append("   AND TTS.IS_FLEET = TC2.CODE_ID\n");
		sql.append("   AND TTS.DEALER_ID = TMD.DEALER_ID\n");
		sql.append("   AND  TTS.IS_RETURN =" + Constant.IF_TYPE_NO + "\n");
		if (areaId != null && areaId != "")
		{
			sql.append("   AND TMV.AREA_ID='" + areaId + "'");
		}
		sql.append("   AND TMV.MATERIAL_ID = TMVM.MATERIAL_ID\n");
		sql.append("   AND TMV.LIFE_CYCLE = " + Constant.VEHICLE_LIFE_04 + "\n");
		sql.append("   AND TMV.DEALER_ID IN (" + dealerId + ")\n");
		// 客户类型
		if (null != customer_type && !"".equals(customer_type) && !"-1".equals(customer_type))
		{
			sql.append("   AND TTC.CTM_TYPE =" + customer_type + "\n");
		}
		// 客户名称
		if (null != customer_name && !"".equals(customer_name))
		{
			sql.append("   AND (TTC.CTM_NAME LIKE '%" + customer_name.trim() + "%' OR TTC.COMPANY_S_NAME LIKE '%" + customer_name.trim() + "%')\n");
		}
		// VIN
		if (null != vin && !"".equals(vin))
		{
			sql.append(GetVinUtil.getVins(vin, "TMV"));
		}
		// 客户电话
		if (null != customer_phone && !"".equals(customer_phone))
		{
			sql.append("   AND (TTC.MAIN_PHONE LIKE '%" + customer_phone.trim() + "%' OR TTC.COMPANY_PHONE LIKE '%" + customer_phone.trim() + "%')\n");
		}
		// 选择物料组
		if (null != materialCode && !"".equals(materialCode))
		{
			String[] materialCodes = materialCode.split(",");
			if (null != materialCodes && materialCodes.length > 0)
			{
				StringBuffer buffer = new StringBuffer();
				for (int i = 0; i < materialCodes.length; i++)
				{
					buffer.append("'").append(materialCodes[i]).append("'").append(",");
				}
				buffer = buffer.deleteCharAt(buffer.length() - 1);
				sql.append("   AND ((TMV.PACKAGE_ID IN (SELECT G.GROUP_ID \n");
				sql.append("                               FROM TM_VHCL_MATERIAL_GROUP G \n");
				sql.append("                              WHERE G.GROUP_CODE IN (" + buffer.toString() + "))) \n");
				sql.append("       OR (TMV.MODEL_ID IN (SELECT G.GROUP_ID \n");
				sql.append("                             FROM TM_VHCL_MATERIAL_GROUP G \n");
				sql.append("                            WHERE G.GROUP_CODE IN (" + buffer.toString() + "))) \n");
				sql.append("       OR (TMV.SERIES_ID IN (SELECT G.GROUP_ID \n");
				sql.append("                              FROM TM_VHCL_MATERIAL_GROUP G \n");
				sql.append("                             WHERE G.GROUP_CODE IN (" + buffer.toString() + ")))) \n");
			}
		}
		// 是否大客户
		if (null != is_fleet && !"".equals(is_fleet) && !"-1".equals(is_fleet))
		{
			sql.append("   AND TTS.IS_FLEET = " + is_fleet + "\n");
		}
		if (!is_fleet.equals(Constant.IF_TYPE_NO.toString()))
		{
			// 大客户名称
			if (null != fleet_name && !"".equals(fleet_name))
			{
				sql.append("   AND TTC.COMPANY_S_NAME LIKE '%" + fleet_name.trim() + "%'\n");
			}
			// 大客户合同
			if (null != contract_no && !"".equals(contract_no))
			{
				sql.append("   AND TTS.CONTRACT_NO LIKE '%" + contract_no.trim() + "%'\n\n");
			}
		}
		// 上报日期
		if (null != startDate && !"".equals(startDate))
		{
			sql.append("   AND TTS.SALES_DATE >= TO_DATE('" + startDate + "','yyyy-MM-dd')\n");
		}
		if (null != endDate && !"".equals(endDate))
		{
			sql.append("   AND TTS.SALES_DATE  <= (TO_DATE('" + endDate + "','yyyy-MM-dd')+1)\n");
		}
		sql.append("	AND TTS.IS_OWN_SALE = TC3.CODE_ID\n");
		sql.append(" ORDER BY TTS.SALES_DATE DESC\n");
		List<Map<String, Object>> list = dao.pageQuery(sql.toString(), null, dao.getFunName());
		return list;
	}
	
	/***
	 * 实销信息查询:查询实销历史信息
	 * 
	 * @param :
	 * @return
	 * @throws Exception
	 */
	public static PageResult<Map<String, Object>> queryReportInfoList(String areaId, String dealerId, Map map, int pageSize, int curPage)
	{
		
		String customer_type = (String) map.get("customer_type"); // 客户类型
		String customer_name = (String) map.get("customer_name"); // 客户名称
		String vin = (String) map.get("vin"); // VIN
		String customer_phone = (String) map.get("customer_phone"); // 客户电话
		String materialCode = (String) map.get("materialCode"); // 选择物料组
		String is_fleet = (String) map.get("is_fleet"); // 是否集团客户
		String fleet_name = (String) map.get("fleet_name"); // 集团客户名称
		String contract_no = (String) map.get("contract_no"); // 集团客户合同
		String startDate = (String) map.get("startDate"); // 上报日期:开始
		String endDate = (String) map.get("endDate"); // 上报日期 ：截止
		String poseId = (String) map.get("poseId");
		
		StringBuffer sql = new StringBuffer("\n");
		List par = new ArrayList();
		
		sql.append("SELECT TTS.ORDER_ID,DECODE(TTC.CTM_NAME, NULL, TTC.COMPANY_NAME, TTC.CTM_NAME) C_NAME, --客户名称\n");
		sql.append("  TTS.SALES_CON_NAME NAME,\n");
		sql.append("       TMD.DEALER_SHORTNAME, --经销商简称\n");
		sql.append("       TMD.DEALER_NAME,\n");// 经销商名称
		sql.append("       TMD.DEALER_CODE,\n"); // 经销商CODE
		sql.append("       TTC.CTM_TYPE, --客户类型\n");
		sql.append(" (SELECT CODE_DESC FROM TC_CODE WHERE CODE_ID = TTC.CTM_TYPE) CTM_TYPE_CN,\n");
		sql.append("       TTC.IS_SECOND, --是否二手车置换\n");
		sql.append(" (SELECT CODE_DESC FROM TC_CODE WHERE CODE_ID = TTC.IS_SECOND) IS_SECOND_CN, \n");
		sql.append("       TTS.IS_FLEET, --是否集团客户\n");
		sql.append(" (SELECT CODE_DESC FROM TC_CODE WHERE CODE_ID = TTS.IS_FLEET) IS_FLEET_CN,\n");
		sql.append("       DECODE(TTC.Ctm_Type, " + Constant.CUSTOMER_TYPE_02 + ", TTC.COMPANY_PHONE, TTC.MAIN_PHONE) MAIN_PHONE, --主要联系电话\n");
		sql.append("       TMVM.MATERIAL_CODE,\n");
		sql.append("       TMVM.MATERIAL_NAME,\n");
		sql.append("       TMV.VIN,\n");
		sql.append("	   TVMG2.GROUP_NAME VT,    --车型名称\n");
		sql.append("       TVMG3.GROUP_NAME VS,   --车系名称\n");
		sql.append("       TVMG.GROUP_NAME SN, --状态名称\n");
		sql.append("       TMV.COLOR,       --颜色名称\n");
		sql.append("       TTS.LOANS_YEAR,TTS.LOANS_TYPE,\n");
		sql.append("  (SELECT CODE_DESC FROM TC_CODE WHERE CODE_ID = TTS.LOANS_TYPE ) LOANS_TYPE_CN ,\n");
		sql.append("       TMV.ENGINE_NO, --VIN\n");
		sql.append("		TTS.CAR_CHARACTOR, \n");
		sql.append("       TO_CHAR(TTS.SALES_DATE, 'yyyy-MM-dd') SALES_DATE, --实销时间\n");
		sql.append("       TMV.VEHICLE_ID,\n");
		sql.append("       TO_CHAR(TMV.PRODUCT_DATE,'yyyy-MM-dd') PRODUCT_DATE, --生产日期\n");
		sql.append("     (TTS.SHOUFU_RATIO*100) as SHOUFU_RATIO,  TC4.CODE_DESC AS   PAYMENT,     (SELECT CODE_DESC FROM TC_CODE WHERE CODE_ID=TTS.MORTGAGE_TYPE) AS MORTGAGE_TYPE ,  TTS.PRICE,\n");
		sql.append("       TTC.CTM_ID\n");
		sql.append("  FROM TT_DEALER_ACTUAL_SALES TTS,\n");
		sql.append("       TT_CUSTOMER            TTC,\n");
		sql.append("       TM_VEHICLE             TMV,\n");
		sql.append("       TM_DEALER       		  TMD,\n");
		sql.append("	   TM_VHCL_MATERIAL_GROUP TVMG,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP TVMG2,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP TVMG3,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP_R TVMGR,\n");
		sql.append("       TM_VHCL_MATERIAL       TMVM,TC_CODE TC4\n");
		sql.append(" WHERE TTS.CTM_ID = TTC.CTM_ID\n");
		sql.append("   AND TMVM.MATERIAL_ID = TVMGR.MATERIAL_ID\n");
		sql.append("   AND TVMGR.GROUP_ID = TVMG.GROUP_ID\n");
		sql.append("   AND TVMG.PARENT_GROUP_ID = TVMG2.GROUP_ID\n");
		sql.append("   AND TVMG2.PARENT_GROUP_ID = TVMG3.GROUP_ID\n");
		sql.append("   AND TTS.VEHICLE_ID = TMV.VEHICLE_ID AND TC4.CODE_ID(+)=TTS.PAYMENT\n");
		sql.append("   AND TMV.MATERIAL_ID = TMVM.MATERIAL_ID\n");
		sql.append("   AND TTS.DEALER_ID = TMD.DEALER_ID\n");
		sql.append("   AND TTS.IS_RETURN =" + Constant.IF_TYPE_NO + "\n");
		sql.append("   AND TMV.LIFE_CYCLE = " + Constant.VEHICLE_LIFE_04 + "\n");
		// sql.append("   AND TMV.DEALER_ID IN (" + dealerId + ")\n");
		
		sql.append(Utility.getConSqlByParamForEqual(dealerId, par, "tmv", "dealer_id"));
		// sql.append(MaterialGroupManagerDao.getDealerBusinessSql("TMV.YIELDLY",
		// poseId));
		// 客户类型
		if (null != customer_type && !"".equals(customer_type) && !"-1".equals(customer_type))
		{
			sql.append("   AND TTC.CTM_TYPE =?\n");
			par.add(customer_type);
		}
		if (areaId != "" && areaId != null)
		{
			sql.append("   AND TMV.YIELDLY = " + areaId + "\n");
			par.add(Long.valueOf(areaId));
		}
		// 客户名称
		if (null != customer_name && !"".equals(customer_name))
		{
			sql.append("   AND (TTC.CTM_NAME LIKE ? OR TTC.COMPANY_S_NAME LIKE ? )\n");
			par.add("%" + customer_name.trim() + "%");
			par.add("%" + customer_name.trim() + "%");
		}
		// VIN
		if (null != vin && !"".equals(vin))
		{
			// sql.append(GetVinUtil.getVins(vin, "TMV"));
			sql.append("AND TMV.VIN LIKE ?\n");
			par.add("%" + vin + "%");
		}
		// 客户电话
		if (null != customer_phone && !"".equals(customer_phone))
		{
			sql.append("   AND (TTC.MAIN_PHONE LIKE ? OR TTC.COMPANY_PHONE LIKE ?)\n");
			par.add("%" + customer_phone.trim() + "%");
			par.add("%" + customer_phone.trim() + "%");
			
		}
		// 选择物料组
		if (null != materialCode && !"".equals(materialCode))
		{
			// String[] materialCodes = materialCode.split(",");
			// if (null != materialCodes && materialCodes.length > 0) {
			// StringBuffer buffer = new StringBuffer();
			// for (int i = 0; i < materialCodes.length; i++) {
			// buffer.append("'").append(materialCodes[i]).append("'")
			// .append(",");
			// }
			// buffer = buffer.deleteCharAt(buffer.length() - 1);
			// sql.append("   AND ((TMV.PACKAGE_ID IN (SELECT G.GROUP_ID \n");
			// sql.append("                               FROM TM_VHCL_MATERIAL_GROUP G \n");
			// sql.append("                              WHERE G.GROUP_CODE IN ("
			// + buffer.toString() + "))) \n");
			// sql.append("       OR (TMV.MODEL_ID IN (SELECT G.GROUP_ID \n");
			// sql.append("                             FROM TM_VHCL_MATERIAL_GROUP G \n");
			// sql.append("                            WHERE G.GROUP_CODE IN ("
			// + buffer.toString() + "))) \n");
			// sql.append("       OR (TMV.SERIES_ID IN (SELECT G.GROUP_ID \n");
			// sql.append("                              FROM TM_VHCL_MATERIAL_GROUP G \n");
			// sql.append("                             WHERE G.GROUP_CODE IN ("
			// + buffer.toString() + ")))) \n");
			// }
			sql.append(MaterialGroupManagerDao.getMaterialGroupQuerySqlByPar("TMV.PACKAGE_ID", materialCode, par));
		}
		// 是否集团客户
		if (null != is_fleet && !"".equals(is_fleet) && !"-1".equals(is_fleet))
		{
			sql.append("   AND TTS.IS_FLEET = ?\n");
			par.add(is_fleet);
		}
		if (!is_fleet.equals(Constant.IF_TYPE_NO.toString()))
		{
			// 集团客户名称
			if (null != fleet_name && !"".equals(fleet_name))
			{
				sql.append("   AND TTC.CTM_NAME LIKE ?\n");
				par.add("%" + fleet_name.trim() + "%");
			}
			// 集团客户合同
			if (null != contract_no && !"".equals(contract_no))
			{
				sql.append("   AND TTS.CONTRACT_NO LIKE ?\n");
				par.add("%" + contract_no.trim() + "%");
			}
		}
		// 上报日期
		if (null != startDate && !"".equals(startDate))
		{
			sql.append("   AND TTS.SALES_DATE >= TO_DATE(?,'yyyy-MM-dd hh24:mi:ss')\n");
			par.add(startDate + " 00:00:00");
		}
		if (null != endDate && !"".equals(endDate))
		{
			sql.append("   AND TTS.SALES_DATE  <= (TO_DATE(?,'yyyy-MM-dd hh24:mi:ss')+1)\n");
			par.add(endDate + " 23:59:59");
		}
		sql.append(" ORDER BY TTS.SALES_DATE DESC\n");
		
		return dao.pageQuery(sql.toString(), par, dao.getFunName(), pageSize, curPage);
	}
	
	/***
	 * 实销信息查询:查询实销历史信息
	 * 
	 * @param :
	 * @return
	 * @throws Exception
	 */
	public static PageResult<Map<String, Object>> queryReportInfoList_CVS(String areaId, String seachType,String dealerId, Map map, int pageSize, int curPage)
	{
		
		String customer_type = (String) map.get("customer_type"); // 客户类型
		String customer_name = (String) map.get("customer_name"); // 客户名称
		String vin = (String) map.get("vin"); // VIN
		String customer_phone = (String) map.get("customer_phone"); // 客户电话
		String materialCode = (String) map.get("materialCode"); // 选择物料组
		String is_fleet = (String) map.get("is_fleet"); // 是否集团客户
		String fleet_name = (String) map.get("fleet_name"); // 集团客户名称
		String contract_no = (String) map.get("contract_no"); // 集团客户合同
		String startDate = (String) map.get("startDate"); // 上报日期:开始
		String endDate = (String) map.get("endDate"); // 上报日期 ：截止
		String startDate_SX = (String) map.get("startDate_SX");
		String endDate_SX = (String) map.get("endDate_SX");
		String poseId = (String) map.get("poseId");
		String callcenterCheckStatus = (String)map.get("callcenterCheckStatus");//呼叫中心审核状态
		String salesCheckStatus = (String)map.get("salesCheckStatus");//销售部审核状态
		
		String orderStartDate = (String) map.get("orderStartDate");
		String orderEndDate = (String) map.get("orderEndDate");
		String IS_REBATE = CommonUtils.checkNull(map.get("IS_REBATE"));
		
		List par = new ArrayList();
		StringBuffer sql = new StringBuffer("\n");
		
		sql.append("SELECT DECODE(TTC.CTM_NAME, NULL, TTC.COMPANY_NAME, TTC.CTM_NAME) C_NAME, --客户名称\n");
		sql.append("       TMD.DEALER_SHORTNAME, --经销商简称\n");
		sql.append("       TMD.DEALER_NAME,\n");// 经销商名称
		sql.append("       TMD.DEALER_CODE,\n"); // 经销商CODE
		sql.append("(select dealer_name from tm_dealer t where t.dealer_id=TTS.Sales_Dealer)sales_dealer_name,\n");//实销经销商
		sql.append("       TTC.CTM_TYPE, --客户类型\n");
		sql.append(" (SELECT CODE_DESC FROM TC_CODE WHERE CODE_ID = TTC.CTM_TYPE) CTM_TYPE_CN, \n");
		sql.append("       TTS.IS_FLEET, --是否集团客户\n");
		sql.append(" (SELECT CODE_DESC FROM TC_CODE WHERE CODE_ID = TTS.IS_FLEET ) IS_FLEET_CN, \n");
		// sql.append("       DECODE(TTC.Ctm_Type, " + Constant.CUSTOMER_TYPE_02
		// + ", TTC.COMPANY_PHONE, TTC.MAIN_PHONE) MAIN_PHONE, --主要联系电话\n");
		sql.append("       decode(TTC.Ctm_Type,'10831001',TTC.MAIN_PHONE,'10831002', TTC.company_phone) MAIN_PHONE,\n");
		sql.append("       TMVM.MATERIAL_CODE,\n");
		sql.append("       TMVM.MATERIAL_NAME,\n");
		sql.append("		VMT.SERIES_NAME,\n");
		sql.append("		VMT.MODEL_NAME,\n");
		sql.append("		VMT.PACKAGE_NAME,\n");
		sql.append("		VMT.COLOR_NAME,");
		sql.append("       TMV.VIN,\n");
		sql.append("       TMV.ENGINE_NO, --VIN\n");
		sql.append("	   (select code_desc from tc_code where code_id = TTS.CAR_CHARACTOR) CAR_CHARACTOR, \n");
		sql.append("	   TSC.NAME SALES_CON_NAME, \n");
		sql.append("       TO_CHAR(TTS.SALES_DATE, 'yyyy-mm-dd hh24:mi:ss') SALES_DATE, --实销时间\n");
		sql.append("       TMV.VEHICLE_ID,\n");
		sql.append("       TO_CHAR(TMV.PRODUCT_DATE,'yyyy-MM-dd') PRODUCT_DATE, --生产日期\n");
		sql.append("       TTC.CTM_ID,\n");
		sql.append("	   TO_CHAR(TTS.INVOICE_DATE,'yyyy-mm-dd') INVOICE_DATE,\n");
		sql.append("	   TTS.CALLCENTER_CHECK_STATUS,\n");
		sql.append("	   TTS.SALES_CHECK_STATUS,\n");
		sql.append("	   TTS.ORDER_DATE,\n");
		sql.append("	   F_GET_TCCODE_DESC(TTC.CARD_TYPE) CARD_TYPE_NAME,\n"); 
		sql.append("	   TTC.CARD_NUM,\n"); 
		sql.append("       decode((SELECT 1 abc FROM TM_VEHICLE_REBATE TVR WHERE TVR.VIN=TMV.VIN),'1','是','否') IS_REBATE, \n"); 
		sql.append("	   TTC.ADDRESS\n"); 
		sql.append("  FROM TT_DEALER_ACTUAL_SALES TTS,\n");
		sql.append("       TT_CUSTOMER            TTC,\n");
		sql.append("       TT_SALES_CONSULTANT    TSC,\n");
		sql.append("       TM_VEHICLE             TMV,\n");
		sql.append("       TM_DEALER       		  TMD,\n");
		sql.append("       TM_VHCL_MATERIAL       TMVM,\n");
		sql.append("	   vw_material_group_mat VMT");
		sql.append(" WHERE TTS.CTM_ID = TTC.CTM_ID\n");
		sql.append("   AND TTS.VEHICLE_ID = TMV.VEHICLE_ID\n");
		sql.append("   AND TTS.SALES_CON_ID = TSC.ID(+)\n");
		sql.append("   AND TMV.MATERIAL_ID = TMVM.MATERIAL_ID\n");
		sql.append("   AND VMT.MATERIAL_ID = TMV.MATERIAL_ID\n");
		sql.append("   AND TTS.DEALER_ID = TMD.DEALER_ID\n");
		sql.append("   AND TTS.IS_RETURN =" + Constant.IF_TYPE_NO + "\n");
		sql.append("   AND TMV.LIFE_CYCLE = " + Constant.VEHICLE_LIFE_04 + "\n");
		sql.append(Utility.getConSqlByParamForEqual(dealerId, par, "TMV", "DEALER_ID"));
		
		if (!"".equals(seachType) && "0".equals(seachType)){
			sql.append(" AND (TTS.SALES_DEALER=? OR TTS.SALES_DEALER IS　NULL ) \n");
			par.add(dealerId);
		}
		// 客户类型
		if (null != customer_type && !"".equals(customer_type) && !"-1".equals(customer_type))
		{
			sql.append("   AND TTC.CTM_TYPE =?\n");
			par.add(customer_type);
		}
		if (areaId != "" && areaId != null)
		{
			sql.append("   AND TMV.YIELDLY = ?\n");
			par.add(Long.valueOf(areaId));
		}
		// 客户名称
		if (null != customer_name && !"".equals(customer_name))
		{
			sql.append("   AND (TTC.CTM_NAME LIKE ? OR TTC.COMPANY_S_NAME LIKE ?)\n");
			par.add("%" + customer_name.trim() + "%");
			par.add("%" + customer_name.trim() + "%");
		}
		// VIN
		if (null != vin && !"".equals(vin))
		{
			sql.append(GetVinUtil.getVins(vin, "TMV"));
		}
		// 客户电话
		if (null != customer_phone && !"".equals(customer_phone))
		{
			sql.append("   AND (TTC.MAIN_PHONE LIKE ? OR TTC.COMPANY_PHONE LIKE ?)\n");
			par.add("%" + customer_phone.trim() + "%");
			par.add("%" + customer_phone.trim() + "%");
		}
		// 选择物料组
		if (null != materialCode && !"".equals(materialCode))
		{
			// String[] materialCodes = materialCode.split(",");
			// if (null != materialCodes && materialCodes.length > 0) {
			// StringBuffer buffer = new StringBuffer();
			// for (int i = 0; i < materialCodes.length; i++) {
			// buffer.append("'").append(materialCodes[i]).append("'")
			// .append(",");
			// }
			// buffer = buffer.deleteCharAt(buffer.length() - 1);
			// sql.append("   AND ((TMV.PACKAGE_ID IN (SELECT G.GROUP_ID \n");
			// sql.append("                               FROM TM_VHCL_MATERIAL_GROUP G \n");
			// sql.append("                              WHERE G.GROUP_CODE IN ("
			// + buffer.toString() + "))) \n");
			// sql.append("       OR (TMV.MODEL_ID IN (SELECT G.GROUP_ID \n");
			// sql.append("                             FROM TM_VHCL_MATERIAL_GROUP G \n");
			// sql.append("                            WHERE G.GROUP_CODE IN ("
			// + buffer.toString() + "))) \n");
			// sql.append("       OR (TMV.SERIES_ID IN (SELECT G.GROUP_ID \n");
			// sql.append("                              FROM TM_VHCL_MATERIAL_GROUP G \n");
			// sql.append("                             WHERE G.GROUP_CODE IN ("
			// + buffer.toString() + ")))) \n");
			// }
			sql.append(MaterialGroupManagerDao.getMaterialGroupQuerySqlByPar("TMV.PACKAGE_ID", materialCode, par));
		}
		// 是否集团客户
		if (null != is_fleet && !"".equals(is_fleet) && !"-1".equals(is_fleet))
		{
			sql.append("   AND TTS.IS_FLEET = ?\n");
			sql.append(is_fleet);
		}
		if (!is_fleet.equals(Constant.IF_TYPE_NO.toString()))
		{
			// 集团客户名称
			if (null != fleet_name && !"".equals(fleet_name))
			{
				sql.append("   AND TTC.CTM_NAME LIKE ?\n");
				sql.append("%" + fleet_name.trim() + "%");
			}
			// 集团客户合同
			if (null != contract_no && !"".equals(contract_no))
			{
				sql.append("   AND TTS.CONTRACT_NO LIKE ?\n");
				sql.append("%" + contract_no.trim() + "%");
			}
		}
		// 上报日期
		if (null != startDate && !"".equals(startDate))
		{
			sql.append("   AND TTS.SALES_DATE >= TO_DATE(?,'yyyy-MM-dd hh24:mi:ss')\n");
			par.add(startDate + " 00:00:00");
		}
		if (null != endDate && !"".equals(endDate))
		{
			sql.append("   AND TTS.SALES_DATE  <= (TO_DATE(?,'yyyy-MM-dd hh24:mi:ss'))\n");
			par.add(endDate + " 23:59:59");
		}
		if (null != startDate_SX && !"".equals(startDate_SX))
		{
			sql.append("   AND TTS.INVOICE_DATE >= TO_DATE(?,'yyyy-MM-dd hh24:mi:ss')\n");
			par.add(startDate_SX + " 00:00:00");
		}
		if (null != endDate_SX && !"".equals(endDate_SX))
		{
			sql.append("   AND TTS.INVOICE_DATE  <= (TO_DATE(?,'yyyy-MM-dd hh24:mi:ss'))\n");
			par.add(endDate_SX + " 23:59:59");
		}
		if (null != orderStartDate && !"".equals(orderStartDate))
		{
			sql.append("   AND TTS.ORDER_DATE >= TO_DATE(?,'yyyy-MM-dd hh24:mi:ss')\n");
			par.add(orderStartDate + " 00:00:00");
		}
		if (null != orderEndDate && !"".equals(orderEndDate))
		{
			sql.append("   AND TTS.ORDER_DATE  <= (TO_DATE(?,'yyyy-MM-dd hh24:mi:ss'))\n");
			par.add(orderEndDate + " 23:59:59");
		}
		if(!"".equals(callcenterCheckStatus)){
			sql.append(" AND TTS.CALLCENTER_CHECK_STATUS = ?");
			par.add(callcenterCheckStatus);
		}
		if(!"".equals(salesCheckStatus)){
			sql.append(" AND TTS.SALES_CHECK_STATUS = ?");
			par.add(salesCheckStatus);
		}

		if (Constant.IF_TYPE_YES.toString().equals(IS_REBATE)) {
			sql.append("AND EXISTS (SELECT 1 abc FROM TM_VEHICLE_REBATE TVR WHERE TVR.VIN=TMV.VIN)");
		} else if (Constant.IF_TYPE_NO.toString().equals(IS_REBATE)) {
			sql.append("AND NOT EXISTS (SELECT 1 abc FROM TM_VEHICLE_REBATE TVR WHERE TVR.VIN=TMV.VIN)");
		}
		// sql.append("AND TTS.IS_OWN_SALE = TC.CODE_ID\n");
		sql.append(" ORDER BY TTS.ORDER_ID,TTS.DEALER_ID,TTS.SALES_DATE DESC\n");
		//sql.append(" ORDER BY TTS.DEALER_ID, TTS.SALES_DATE DESC\n");
		
		return dao.pageQuery(sql.toString(), par, dao.getFunName(), pageSize, curPage);
	}
	public static PageResult<Map<String, Object>> querySumReportInfoList_CVS(String areaId, String seachType,String dealerId, Map map, int pageSize, int curPage)
	{
		
		String customer_type = (String) map.get("customer_type"); // 客户类型
		String customer_name = (String) map.get("customer_name"); // 客户名称
		String vin = (String) map.get("vin"); // VIN
		String customer_phone = (String) map.get("customer_phone"); // 客户电话
		String materialCode = (String) map.get("materialCode"); // 选择物料组
		String is_fleet = (String) map.get("is_fleet"); // 是否集团客户
		String fleet_name = (String) map.get("fleet_name"); // 集团客户名称
		String contract_no = (String) map.get("contract_no"); // 集团客户合同
		String startDate = (String) map.get("startDate"); // 上报日期:开始
		String endDate = (String) map.get("endDate"); // 上报日期 ：截止
		String startDate_SX = (String) map.get("startDate_SX");
		String endDate_SX = (String) map.get("endDate_SX");
		String poseId = (String) map.get("poseId");
		String callcenterCheckStatus = (String)map.get("callcenterCheckStatus");//呼叫中心审核状态
		String salesCheckStatus = (String)map.get("salesCheckStatus");//销售部审核状态
		
		String orderStartDate = (String) map.get("orderStartDate");
		String orderEndDate = (String) map.get("orderEndDate");
		
		List par = new ArrayList();
		StringBuffer sql = new StringBuffer("\n");
		sql.append("WITH \n");
		sql.append("info_ as\n");
		sql.append("(SELECT tmd.dealer_id,DECODE(TTC.CTM_NAME, NULL, TTC.COMPANY_NAME, TTC.CTM_NAME) C_NAME, --客户名称\n");
		sql.append("       TMD.DEALER_SHORTNAME, --经销商简称\n");
		sql.append("       TMD.DEALER_NAME,\n");// 经销商名称
		sql.append("       TMD.DEALER_CODE,\n"); // 经销商CODE
		sql.append("(select dealer_name from tm_dealer t where t.dealer_id=TTS.Sales_Dealer)sales_dealer_name,\n");//实销经销商
		sql.append("       TTC.CTM_TYPE, --客户类型\n");
		sql.append(" (SELECT CODE_DESC FROM TC_CODE WHERE CODE_ID = TTC.CTM_TYPE) CTM_TYPE_CN, \n");
		sql.append("       TTS.IS_FLEET, --是否集团客户\n");
		sql.append(" (SELECT CODE_DESC FROM TC_CODE WHERE CODE_ID = TTS.IS_FLEET ) IS_FLEET_CN, \n");
		sql.append("       NVL(MAIN_PHONE,COMPANY_PHONE) MAIN_PHONE ,\n");
		sql.append("       TMVM.MATERIAL_CODE,\n");
		sql.append("       TMVM.MATERIAL_NAME,\n");
		sql.append("		VMT.SERIES_NAME,\n");
		sql.append("		VMT.MODEL_NAME,\n");
		sql.append("		VMT.PACKAGE_NAME,\n");
		sql.append("		VMT.COLOR_NAME,");
		sql.append("       TMV.VIN,\n");
		sql.append("       TMV.ENGINE_NO, --VIN\n");
		sql.append("	   (select code_desc from tc_code where code_id = TTS.CAR_CHARACTOR) CAR_CHARACTOR, \n");
		sql.append("	   TSC.NAME SALES_CON_NAME, \n");
		sql.append("       TO_CHAR(TTS.SALES_DATE, 'yyyy-mm-dd hh24:mi:ss') SALES_DATE, --实销时间\n");
		sql.append("       TMV.VEHICLE_ID,\n");
		sql.append("       TO_CHAR(TMV.PRODUCT_DATE,'yyyy-MM-dd') PRODUCT_DATE, --生产日期\n");
		sql.append("       TTC.CTM_ID,\n");
		sql.append("	   TO_CHAR(TTS.INVOICE_DATE,'yyyy-mm-dd') INVOICE_DATE,\n");
		sql.append("	   TTS.CALLCENTER_CHECK_STATUS,\n");
		sql.append("	   TTS.SALES_CHECK_STATUS,\n"); 
		sql.append("	   TTS.ORDER_DATE,\n"); 
		sql.append("	   F_GET_TCCODE_DESC(TTC.CARD_TYPE) CARD_TYPE_NAME,\n"); 
		sql.append("	   TTC.CARD_NUM,\n"); 
		sql.append("	   TTC.ADDRESS\n"); 
		sql.append("  FROM TT_DEALER_ACTUAL_SALES TTS,\n");
		sql.append("       TT_CUSTOMER            TTC,\n");
		sql.append("       TT_SALES_CONSULTANT    TSC,\n");
		sql.append("       TM_VEHICLE             TMV,\n");
		sql.append("       TM_DEALER       		  TMD,\n");
		sql.append("       TM_VHCL_MATERIAL       TMVM,\n");
		sql.append("	   vw_material_group_mat VMT");
		sql.append(" WHERE TTS.CTM_ID = TTC.CTM_ID\n");
		sql.append("   AND TTS.VEHICLE_ID = TMV.VEHICLE_ID\n");
		sql.append("   AND TTS.SALES_CON_ID = TSC.ID(+)\n");
		sql.append("   AND TMV.MATERIAL_ID = TMVM.MATERIAL_ID\n");
		sql.append("   AND VMT.MATERIAL_ID = TMV.MATERIAL_ID\n");
		sql.append("   AND TTS.DEALER_ID = TMD.DEALER_ID\n");
		sql.append("   AND TTS.IS_RETURN =" + Constant.IF_TYPE_NO + "\n");
		sql.append("   AND TMV.LIFE_CYCLE = " + Constant.VEHICLE_LIFE_04 + "\n");
		sql.append(Utility.getConSqlByParamForEqual(dealerId, par, "TMV", "DEALER_ID"));
		
		if (!"".equals(seachType) && "0".equals(seachType)){
			sql.append(" AND (TTS.SALES_DEALER=? OR TTS.SALES_DEALER IS　NULL ) \n");
			par.add(dealerId);
		}
		// 客户类型
		if (null != customer_type && !"".equals(customer_type) && !"-1".equals(customer_type))
		{
			sql.append("   AND TTC.CTM_TYPE =?\n");
			par.add(customer_type);
		}
		if (areaId != "" && areaId != null)
		{
			sql.append("   AND TMV.YIELDLY = ?\n");
			par.add(Long.valueOf(areaId));
		}
		// 客户名称
		if (null != customer_name && !"".equals(customer_name))
		{
			sql.append("   AND (TTC.CTM_NAME LIKE ? OR TTC.COMPANY_S_NAME LIKE ?)\n");
			par.add("%" + customer_name.trim() + "%");
			par.add("%" + customer_name.trim() + "%");
		}
		// VIN
		if (null != vin && !"".equals(vin))
		{
			sql.append(GetVinUtil.getVins(vin, "TMV"));
		}
		// 客户电话
		if (null != customer_phone && !"".equals(customer_phone))
		{
			sql.append("   AND (TTC.MAIN_PHONE LIKE ? OR TTC.COMPANY_PHONE LIKE ?)\n");
			par.add("%" + customer_phone.trim() + "%");
			par.add("%" + customer_phone.trim() + "%");
		}
		// 选择物料组
		if (null != materialCode && !"".equals(materialCode))
		{
			sql.append(MaterialGroupManagerDao.getMaterialGroupQuerySqlByPar("TMV.PACKAGE_ID", materialCode, par));
		}
		// 是否集团客户
		if (null != is_fleet && !"".equals(is_fleet) && !"-1".equals(is_fleet))
		{
			sql.append("   AND TTS.IS_FLEET = ?\n");
			sql.append(is_fleet);
		}
		if (!is_fleet.equals(Constant.IF_TYPE_NO.toString()))
		{
			// 集团客户名称
			if (null != fleet_name && !"".equals(fleet_name))
			{
				sql.append("   AND TTC.CTM_NAME LIKE ?\n");
				sql.append("%" + fleet_name.trim() + "%");
			}
			// 集团客户合同
			if (null != contract_no && !"".equals(contract_no))
			{
				sql.append("   AND TTS.CONTRACT_NO LIKE ?\n");
				sql.append("%" + contract_no.trim() + "%");
			}
		}
		// 上报日期
		if (null != startDate && !"".equals(startDate))
		{
			sql.append("   AND TTS.SALES_DATE >= TO_DATE(?,'yyyy-MM-dd hh24:mi:ss')\n");
			par.add(startDate + " 00:00:00");
		}
		if (null != endDate && !"".equals(endDate))
		{
			sql.append("   AND TTS.SALES_DATE  <= (TO_DATE(?,'yyyy-MM-dd hh24:mi:ss'))\n");
			par.add(endDate + " 23:59:59");
		}
		if (null != startDate_SX && !"".equals(startDate_SX))
		{
			sql.append("   AND TTS.INVOICE_DATE >= TO_DATE(?,'yyyy-MM-dd hh24:mi:ss')\n");
			par.add(startDate_SX + " 00:00:00");
		}
		if (null != endDate_SX && !"".equals(endDate_SX))
		{
			sql.append("   AND TTS.INVOICE_DATE  <= (TO_DATE(?,'yyyy-MM-dd hh24:mi:ss'))\n");
			par.add(endDate_SX + " 23:59:59");
		}
		if (null != orderStartDate && !"".equals(orderStartDate))
		{
			sql.append("   AND TTS.ORDER_DATE >= TO_DATE(?,'yyyy-MM-dd hh24:mi:ss')\n");
			par.add(orderStartDate + " 00:00:00");
		}
		if (null != orderEndDate && !"".equals(orderEndDate))
		{
			sql.append("   AND TTS.ORDER_DATE  <= (TO_DATE(?,'yyyy-MM-dd hh24:mi:ss'))\n");
			par.add(orderEndDate + " 23:59:59");
		}
		if(!"".equals(callcenterCheckStatus)){
			sql.append(" AND TTS.CALLCENTER_CHECK_STATUS = ?");
			par.add(callcenterCheckStatus);
		}
		if(!"".equals(salesCheckStatus)){
			sql.append(" AND TTS.SALES_CHECK_STATUS = ?");
			par.add(salesCheckStatus);
		}
		
		sql.append(" ORDER BY TTS.ORDER_ID,TTS.DEALER_ID,TTS.SALES_DATE DESC\n");
		sql.append(") select \n");
		sql.append("t1.dealer_id,\n");
		sql.append("t1.DEALER_SHORTNAME,       \n");
		sql.append(" t1.model_name,\n");
		sql.append(" t1.package_name,\n");
		sql.append(" t1.color_name,\n");
		sql.append(" count(1) as total\n");
		sql.append("from info_ t1\n");
		sql.append("  \n");
		sql.append("  \n");
		sql.append(" group by \n");
		sql.append(" t1.dealer_id,\n");
		sql.append(" t1.DEALER_SHORTNAME,\n");
		sql.append(" t1.model_name,\n");
		sql.append(" t1.package_name,\n");
		sql.append(" t1.color_name\n");
		sql.append(" \n");
		sql.append(" order by \n");
		sql.append(" t1.dealer_id,\n");
		sql.append(" t1.model_name,\n");
		sql.append(" t1.package_name,\n");
		sql.append(" t1.color_name\n");
		
		return dao.pageQuery(sql.toString(), par, dao.getFunName(), pageSize, curPage);
	}
	public static List getPartentCompany(String dealers)
	{
		StringBuffer sql = new StringBuffer("");
		sql.append(" SELECT TMD.COMPANY_ID FROM TM_DEALER TMD WHERE TMD.DEALER_ID IN(\n");
		sql.append(" SELECT VOD1.DEALER_ID\n");
		sql.append(" FROM VW_ORG_DEALER VOD1\n");
		sql.append(" WHERE VOD1.ROOT_DEALER_ID IN\n");
		sql.append(" (SELECT VOD.ROOT_DEALER_ID\n");
		sql.append(" FROM VW_ORG_DEALER VOD\n");
		sql.append(" WHERE VOD.DEALER_ID = " + dealers + "))\n");
		
		List list = factory.select(sql.toString(), null, new DAOCallback() {
			List mylist = new ArrayList();
			
			public List wrapper(ResultSet rs, int idx)
			{
				int count = 0;
				try
				{
					count = rs.getInt("COMPANY_ID");
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
				mylist.add(count);
				return mylist;
			}
		});
		return list;
	}
	
	/***
	 * 实销信息更改申请:查询可申请更改的信息
	 * 
	 * @param :
	 * @return
	 * @throws Exception
	 */
	public static PageResult<Map<String, Object>> queryReportInfoList_Change(String areaId, String dealerId, Map map, int pageSize, int curPage, String poseId)
	{
		
		String customer_type = (String) map.get("customer_type"); // 客户类型
		String customer_name = (String) map.get("customer_name"); // 客户名称
		String vin = (String) map.get("vin"); // VIN
		String customer_phone = (String) map.get("customer_phone"); // 客户电话
		String materialCode = (String) map.get("materialCode"); // 选择物料组
		String is_fleet = (String) map.get("is_fleet"); // 是否集团客户
		String fleet_name = (String) map.get("fleet_name"); // 集团客户名称
		String contract_no = (String) map.get("contract_no"); // 集团客户合同
		String startDate = (String) map.get("startDate"); // 上报日期:开始
		String endDate = (String) map.get("endDate"); // 上报日期 ：截止
		
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT DECODE(TTC.CTM_NAME, NULL, TTC.COMPANY_NAME, TTC.CTM_NAME) C_NAME, --客户名称\n");
		sql.append("       TTC.CTM_TYPE, --客户类型\n");
		sql.append("       TTS.IS_FLEET, --是否集团客户\n");
		sql.append("       DECODE(TTC.CTM_NAME, NULL, TTC.COMPANY_PHONE, TTC.MAIN_PHONE) MAIN_PHONE, --主要联系电话\n");
		sql.append("	   TTS.SALES_CHECK_STATUS, \n");
		sql.append("	   TTS.CALLCENTER_CHECK_STATUS, \n");
		sql.append("       TMVM.MATERIAL_CODE,\n");
		sql.append("       TMVM.MATERIAL_NAME,\n");
		sql.append("	   VMT.SERIES_NAME,\n");
		sql.append("	   VMT.MODEL_NAME,\n");
		sql.append("	   VMT.PACKAGE_NAME,\n");
		sql.append("	   VMT.COLOR_NAME,");
		sql.append("       TMV.VIN,\n");
		sql.append("       TO_CHAR(TTS.SALES_DATE, 'yyyy-MM-dd') SALES_DATE, --实销时间\n");
		sql.append("       TMV.VEHICLE_ID,\n");
		sql.append("       TTC.CTM_ID\n");
		sql.append("  FROM TT_DEALER_ACTUAL_SALES TTS,\n");
		sql.append("       TT_CUSTOMER            TTC,\n");
		sql.append("       TM_VEHICLE             TMV,\n");
		sql.append("       TM_VHCL_MATERIAL       TMVM,\n");
		sql.append("       vw_material_group_mat  VMT\n");
		sql.append(" WHERE TTS.CTM_ID = TTC.CTM_ID\n");
		sql.append("   AND TTS.VEHICLE_ID = TMV.VEHICLE_ID\n");
		sql.append("   AND TMV.MATERIAL_ID = TMVM.MATERIAL_ID\n");
		sql.append("   AND VMT.MATERIAL_ID = TMV.MATERIAL_ID\n");
		sql.append("   AND TTS.CALLCENTER_CHECK_STATUS = "+Constant.CALLCENTER_CHECK_STATUS_02+" \n");
		sql.append("   AND TTS.SALES_CHECK_STATUS = "+Constant.SALESX_CHECK_STATUS_02+" \n");
		sql.append("   AND TTS.IS_RETURN!='" + Constant.IF_TYPE_YES + "'\n");
		if (areaId != null && areaId != "")
		{
			sql.append("   AND TMV.YIELDLY = " + areaId + "\n");
		}
		sql.append("   AND TMV.LOCK_STATUS = " + Constant.LOCK_STATUS_01 + "\n");
		sql.append("   AND TMV.LIFE_CYCLE = " + Constant.VEHICLE_LIFE_04 + "\n");
		sql.append("   AND TMV.DEALER_ID = " + dealerId + "\n");
		// 客户类型
		if (null != customer_type && !"".equals(customer_type) && !"-1".equals(customer_type))
		{
			sql.append("   AND TTC.CTM_TYPE =" + customer_type + "\n");
		}
		// 客户名称
		if (null != customer_name && !"".equals(customer_name))
		{
			sql.append("   AND (TTC.CTM_NAME LIKE '%" + customer_name.trim() + "%' OR TTC.COMPANY_S_NAME LIKE '%" + customer_name.trim() + "%')\n");
		}
		// VIN
		if (null != vin && !"".equals(vin))
		{
			sql.append(GetVinUtil.getVins(vin, "TMV"));
		}
		// 客户电话
		if (null != customer_phone && !"".equals(customer_phone))
		{
			sql.append("   AND (TTC.MAIN_PHONE LIKE '%" + customer_phone.trim() + "%' OR TTC.COMPANY_PHONE LIKE '%" + customer_phone.trim() + "%')\n");
		}
		// 选择物料组
		if (null != materialCode && !"".equals(materialCode))
		{
			String[] materialCodes = materialCode.split(",");
			if (null != materialCodes && materialCodes.length > 0)
			{
				StringBuffer buffer = new StringBuffer();
				for (int i = 0; i < materialCodes.length; i++)
				{
					buffer.append("'").append(materialCodes[i]).append("'").append(",");
				}
				buffer = buffer.deleteCharAt(buffer.length() - 1);
				sql.append("   AND ((TMV.PACKAGE_ID IN (SELECT G.GROUP_ID \n");
				sql.append("                               FROM TM_VHCL_MATERIAL_GROUP G \n");
				sql.append("                              WHERE G.GROUP_CODE IN (" + buffer.toString() + "))) \n");
				sql.append("       OR (TMV.MODEL_ID IN (SELECT G.GROUP_ID \n");
				sql.append("                             FROM TM_VHCL_MATERIAL_GROUP G \n");
				sql.append("                            WHERE G.GROUP_CODE IN (" + buffer.toString() + "))) \n");
				sql.append("       OR (TMV.SERIES_ID IN (SELECT G.GROUP_ID \n");
				sql.append("                              FROM TM_VHCL_MATERIAL_GROUP G \n");
				sql.append("                             WHERE G.GROUP_CODE IN (" + buffer.toString() + ")))) \n");
			}
		}
		// 是否集团客户
		if (null != is_fleet && !"".equals(is_fleet) && !"-1".equals(is_fleet))
		{
			sql.append("   AND TTS.IS_FLEET = " + is_fleet + "\n");
		}
		if (!is_fleet.equals(Constant.IF_TYPE_NO.toString()))
		{
			// 集团客户名称
			if (null != fleet_name && !"".equals(fleet_name))
			{
				sql.append("   AND TTC.COMPANY_S_NAME LIKE '%" + fleet_name.trim() + "%'\n");
			}
			// 集团客户合同
			if (null != contract_no && !"".equals(contract_no))
			{
				sql.append("   AND TTS.CONTRACT_NO LIKE '%" + contract_no.trim() + "%'\n\n");
			}
		}
		// 上报日期
		if (null != startDate && !"".equals(startDate))
		{
			sql.append("   AND TTS.SALES_DATE >= TO_DATE('" + startDate + "','yyyy-MM-dd')\n");
		}
		if (null != endDate && !"".equals(endDate))
		{
			sql.append("   AND TTS.SALES_DATE  <= (TO_DATE('" + endDate + "','yyyy-MM-dd')+1)\n");
		}
		sql.append("AND TMV.LOCK_STATUS!=10241005");
		sql.append(" AND NOT EXISTS (SELECT 1 FROM TT_DEALER_ACTUAL_SALES_AUDIT TA WHERE TA.CTM_ID = TTC.CTM_ID AND TA.STATUS IN(" + Constant.SALES_INFO_CHANGE_STATUS_01 + ","+Constant.SALES_INFO_CHANGE_STATUS_04+"))\n");
		sql.append(MaterialGroupManagerDao.getDealerBusinessSql("TMV.YIELDLY", poseId));
		sql.append(" ORDER BY TTS.SALES_DATE DESC\n");
		return dao.pageQuery(sql.toString(), null, dao.getFunName(), pageSize, curPage);
	}
	
	/***
	 * 实销信息上报:查询客户列表
	 * 
	 * @param :
	 * @return
	 * @throws Exception
	 */
	public static PageResult<Map<String, Object>> getCustomerList(String dealerId, String customerName, int pageSize, int curPage)
	{
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT TTC.CTM_ID,\n");
		sql.append("       TTC.CTM_NAME,\n");
		sql.append("       TTC.SEX,\n");
		sql.append("       TTC.MAIN_PHONE,\n");
		sql.append("       TTC.OTHER_PHONE,\n");
		sql.append("       TO_CHAR(TTC.BIRTHDAY,'yyyy-MM-dd')BIRTHDAY,\n");
		sql.append("       TTC.IS_MARRIED\n");
		sql.append("  FROM TT_CUSTOMER TTC\n");
		sql.append(" WHERE TTC.DLR_COMPANY_ID IN\n");
		sql.append("       (SELECT TMD.COMPANY_ID\n");
		sql.append("          FROM TM_DEALER TMD\n");
		sql.append("         WHERE TMD.DEALER_ID IN (" + dealerId + "))\n");
		sql.append("           AND TTC.CTM_TYPE = " + Constant.CUSTOMER_TYPE_01 + "\n");
		if (null != customerName && !"".equals(customerName))
		{
			sql.append("       AND TTC.CTM_NAME LIKE '%" + customerName.trim() + "%'\n");
		}
		sql.append("ORDER BY TTC.CTM_NAME\n");
		
		return dao.pageQuery(sql.toString(), null, dao.getFunName(), pageSize, curPage);
	}
	
	/***
	 * 实销信息上报:查询集团客户列表
	 * 
	 * @param :
	 * @return
	 * @throws Exception
	 */
	public static PageResult<Map<String, Object>> getFleetList(String fleet_name, Long dlrCompanyId, Long oemCompanyId, int pageSize, int curPage)
	{
		// Integer i = CommonUtils.getNowSys(oemCompanyId); //判断是哪个系统0是微车，1是轿车
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT TEMP.FLEET_ID,\n");
		sql.append("       TEMP.FLEET_NAME,\n");
		sql.append("       TEMP.FLEET_TYPE,\n");
		sql.append("       TEMP.PURPOSE,\n");
		sql.append("       TEMP.REGION,\n");
		sql.append("       TEMP.MAIN_LINKMAN,\n");
		sql.append("       TEMP.MAIN_PHONE,\n");
		sql.append("       TEMP.PACT_NAME,\n");
		sql.append("       TEMP.TYPE\n");
		sql.append("  FROM (SELECT TCP.PACT_ID FLEET_ID,\n");
		sql.append("               TCP.PACT_NAME FLEET_NAME,\n");
		sql.append("               NULL FLEET_TYPE,\n");
		sql.append("               NULL PURPOSE,\n");
		sql.append("               NULL REGION,\n");
		sql.append("               NULL MAIN_LINKMAN,\n");
		sql.append("               NULL MAIN_PHONE,\n");
		sql.append("               tcp.pacT_name,\n");
		sql.append("               'PACT' TYPE\n");
		sql.append("          FROM TM_COMPANY_PACT TCP\n");
		sql.append("         WHERE TCP.STATUS = " + Constant.STATUS_ENABLE + "\n");
		sql.append("           AND TCP.OEM_COMPANY_ID = " + oemCompanyId + "\n");
		sql.append("           AND TCP.IS_ALLOW_APPLY = ").append(Constant.IF_TYPE_NO).append("\n");
		if (null != fleet_name && !"".equals(fleet_name))
		{
			sql.append("           AND TCP.PACT_NAME LIKE '%" + fleet_name.trim() + "%'\n");
		}
		sql.append("        UNION\n");
		sql.append("        SELECT DISTINCT TMF.FLEET_ID,\n");
		sql.append("               TMF.FLEET_NAME,\n");
		sql.append("               TMF.FLEET_TYPE,\n");
		sql.append("               TMF.PURPOSE,\n");
		sql.append("               TMF.REGION,\n");
		sql.append("               TMF.MAIN_LINKMAN,\n");
		sql.append("               TMF.MAIN_PHONE,\n");
		sql.append("               tcp.pacT_name,\n");
		sql.append("               'FLEET' TYPE\n");
		sql.append("          FROM TM_FLEET TMF,TM_COMPANY_PACT tcp\n");
		sql.append("         WHERE TMF.DLR_COMPANY_ID = " + dlrCompanyId + "\n");
		//
		sql.append("	       and tmf.pact_id = tcp.pact_id(+)\n");
		sql.append("           AND TMF.STATUS = " + Constant.FLEET_INFO_TYPE_03 + "\n");
		if (null != fleet_name && !"".equals(fleet_name))
		{
			sql.append("           AND TMF.FLEET_NAME LIKE '%" + fleet_name.trim() + "%'\n");
		}
		sql.append(") TEMP\n");
		sql.append(" ORDER BY TEMP.TYPE DESC, TEMP.FLEET_ID ASC");
		// StringBuffer sql = new StringBuffer();
		//
		// sql.append("SELECT TEMP.FLEET_ID,\n");
		// sql.append("       TEMP.FLEET_NAME,\n");
		// sql.append("       TEMP.FLEET_TYPE,\n");
		// sql.append("       TEMP.PURPOSE,\n");
		// sql.append("       TEMP.REGION,\n");
		// sql.append("       TEMP.MAIN_LINKMAN,\n");
		// sql.append("       TEMP.MAIN_PHONE,\n");
		// sql.append("       TEMP.TYPE\n");
		// sql.append("  FROM (SELECT TCP.PACT_ID FLEET_ID,\n");
		// sql.append("               TCP.PACT_NAME FLEET_NAME,\n");
		// sql.append("               NULL FLEET_TYPE,\n");
		// sql.append("               NULL PURPOSE,\n");
		// sql.append("               NULL REGION,\n");
		// sql.append("               NULL MAIN_LINKMAN,\n");
		// sql.append("               NULL MAIN_PHONE,\n");
		// sql.append("               'PACT' TYPE\n");
		// sql.append("          FROM TM_COMPANY_PACT TCP\n");
		// sql.append("         WHERE TCP.STATUS = "+Constant.STATUS_ENABLE+"\n");
		// sql.append("           AND TCP.OEM_COMPANY_ID = "+oemCompanyId+"\n");
		// if (null != fleet_name && !"".equals(fleet_name)) {
		// sql.append("           AND TCP.PACT_NAME LIKE '%" + fleet_name.trim()
		// + "%'\n");
		// }
		// sql.append("        UNION\n");
		// sql.append("        SELECT DISTINCT TMF.FLEET_ID,\n");
		// sql.append("               TMF.FLEET_NAME,\n");
		// sql.append("               TMF.FLEET_TYPE,\n");
		// sql.append("               TMF.PURPOSE,\n");
		// sql.append("               TMF.REGION,\n");
		// sql.append("               TMF.MAIN_LINKMAN,\n");
		// sql.append("               TMF.MAIN_PHONE,\n");
		// sql.append("               'FLEET' TYPE\n");
		// sql.append("          FROM TM_FLEET TMF,TT_FLEET_CONTRACT A\n");
		// sql.append("         WHERE TMF.DLR_COMPANY_ID IN( SELECT TMD.COMPANY_ID FROM TM_DEALER TMD WHERE TMD.DEALER_ID IN(\n");
		// sql.append(" SELECT VOD1.DEALER_ID\n");
		// sql.append(" FROM VW_ORG_DEALER VOD1\n");
		// sql.append(" WHERE VOD1.ROOT_DEALER_ID IN\n");
		// sql.append(" (SELECT VOD.ROOT_DEALER_ID\n");
		// sql.append(" FROM VW_ORG_DEALER VOD\n");
		// sql.append(" WHERE VOD.DEALER_ID IN ( SELECT TMD.DEALER_ID FROM TM_DEALER TMD,TM_DEALER_BUSINESS_AREA TDBA WHERE TMD.DEALER_ID IN("+dealerIds+") AND TMD.DEALER_ID=TDBA.DEALER_ID AND TDBA.AREA_ID NOT IN(2010010100000004,2010010100000005,2010010100000006)))))\n");
		// sql.append("	       AND TMF.CON_STATUS = 1    AND A.FLEET_ID=TMF.FLEET_ID  AND A.STATUS="+Constant.FLEET_CON_STATUS_03+"\n");
		// sql.append("           AND TMF.STATUS = "+Constant.FLEET_INFO_TYPE_03+"\n");
		// if (null != fleet_name && !"".equals(fleet_name)) {
		// sql.append("           AND TMF.FLEET_NAME LIKE '%" +
		// fleet_name.trim()
		// + "%'\n");
		// }
		// sql.append(") TEMP\n");
		// sql.append(" ORDER BY TEMP.TYPE DESC, TEMP.FLEET_ID ASC");
		
		/*
		 * sql.append("SELECT TMF.FLEET_ID,\n");
		 * sql.append("       TMF.FLEET_NAME, --客户姓名\n");
		 * sql.append("       TMF.FLEET_TYPE, --客户类型\n");
		 * sql.append("       TMF.PURPOSE, --购车用途\n");
		 * sql.append("       TMF.REGION, --区域\n");
		 * sql.append("       TMF.MAIN_LINKMAN, --主要联系人\n");
		 * sql.append("       TMF.MAIN_PHONE --主要联系人电话\n");
		 * sql.append("  FROM TM_FLEET TMF\n");
		 * sql.append(" WHERE TMF.DLR_COMPANY_ID =" + dlrCompanyId + "\n"); if
		 * (null != fleet_name && !"".equals(fleet_name)) {
		 * sql.append("       AND TMF.FLEET_NAME LIKE '%" + fleet_name.trim() +
		 * "%'\n"); } sql.append(" ORDER BY TMF.CREATE_DATE DESC\n");
		 */
		
		return dao.pageQuery(sql.toString(), null, dao.getFunName(), pageSize, curPage);
	}
	
	/**
	 * 查询 大客户信息
	 * 
	 * @param fleetId
	 *            ID 必填
	 * @return
	 */
	public Map<String, Object> queryFleetInfo(Long fleetId)
	{
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT TEMP.FLEET_ID,\n");
		sql.append("       TEMP.FLEET_NAME,\n");
		sql.append("       TEMP.FLEET_TYPE,\n");
		sql.append("       TEMP.PURPOSE,\n");
		sql.append("       TEMP.REGION,\n");
		sql.append("       TEMP.MAIN_LINKMAN,\n");
		sql.append("       TEMP.MAIN_PHONE,\n");
		sql.append("       TEMP.PACT_NAME,\n");
		sql.append("       TEMP.TYPE\n");
		sql.append("  FROM (SELECT TCP.PACT_ID FLEET_ID,\n");
		sql.append("               TCP.PACT_NAME FLEET_NAME,\n");
		sql.append("               NULL FLEET_TYPE,\n");
		sql.append("               NULL PURPOSE,\n");
		sql.append("               NULL REGION,\n");
		sql.append("               NULL MAIN_LINKMAN,\n");
		sql.append("               NULL MAIN_PHONE,\n");
		sql.append("               tcp.pacT_name,\n");
		sql.append("               'PACT' TYPE\n");
		sql.append("          FROM TM_COMPANY_PACT TCP\n");
		sql.append("        UNION\n");
		sql.append("        SELECT DISTINCT TMF.FLEET_ID,\n");
		sql.append("               TMF.FLEET_NAME,\n");
		sql.append("               TMF.FLEET_TYPE,\n");
		sql.append("               TMF.PURPOSE,\n");
		sql.append("               TMF.REGION,\n");
		sql.append("               TMF.MAIN_LINKMAN,\n");
		sql.append("               TMF.MAIN_PHONE,\n");
		sql.append("               tcp.pacT_name,\n");
		sql.append("               'FLEET' TYPE\n");
		sql.append("          FROM TM_FLEET TMF,TM_COMPANY_PACT tcp\n");
		sql.append("         WHERE tmf.pact_id = tcp.pact_id(+)\n");
		sql.append(") TEMP WHERE TEMP.FLEET_ID = " + fleetId + "\n");
		sql.append(" ORDER BY TEMP.TYPE DESC, TEMP.FLEET_ID ASC");
		
		return pageQueryMap(sql.toString(), null, this.getFunName());
	}
	
	/***
	 * 实销信息上报:查询集团客户合同列表
	 * 
	 * @param :
	 * @return
	 * @throws Exception
	 */
	public static PageResult<Map<String, Object>> getFleetContractList(String contract_no, String contract_amount, String startDate, String endDate, String fleet_id, int pageSize, int curPage)
	{
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT TTFC.CONTRACT_ID,\n");
		sql.append("       TTFC.CONTRACT_NO, --合同编号\n");
		sql.append("       TTFC.CONTRACT_AMOUNT, --合同数量\n");
		sql.append("       TTFC.DISCOUNT, --支持点位\n");
		sql.append("       TO_CHAR(TTFC.START_DATE, 'yyyy-MM-dd') START_DATE, --有效期起\n");
		sql.append("       TO_CHAR(TTFC.END_DATE, 'yyyy-MM-dd') END_DATE --有效期止\n");
		sql.append("  FROM TT_FLEET_CONTRACT TTFC\n");
		sql.append(" WHERE TTFC.FLEET_ID =" + fleet_id + "\n");
		if (null != contract_no && !"".equals(contract_no))
		{
			sql.append("    AND TTFC.CONTRACT_NO LIKE '%" + contract_no + "%'\n");
		}
		if (null != contract_amount && !"".equals(contract_amount))
		{
			sql.append("    AND TTFC.CONTRACT_AMOUNT LIKE '%" + contract_amount.trim() + "%'\n");
		}
		if (null != startDate && !"".equals(startDate))
		{
			sql.append("    AND TTFC.START_DATE >= TO_DATE('" + startDate + "','yyyy-MM-dd')\n");
		}
		if (null != endDate && !"".equals(endDate))
		{
			sql.append("    AND TTFC.END_DATE <= TO_DATE('" + endDate + "','yyyy-MM-dd')\n");
		}
		sql.append(" ORDER BY TTFC.CREATE_DATE DESC\n");
		
		return dao.pageQuery(sql.toString(), null, dao.getFunName(), pageSize, curPage);
	}
	
	/**
	 * 实销信息上报:查询大客户合同列表
	 * 
	 * @param fleet_id
	 * @return
	 */
	public static List<Map<String, Object>> getFleetContractList(String fleet_id)
	{
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT TTFC.CONTRACT_ID,\n");
		sql.append("       TTFC.CONTRACT_NO, --合同编号\n");
		sql.append("       TTFC.CONTRACT_AMOUNT, --合同数量\n");
		sql.append("       TTFC.DISCOUNT, --支持点位\n");
		sql.append("       TO_CHAR(TTFC.START_DATE, 'yyyy-MM-dd') START_DATE, --有效期起\n");
		sql.append("       TO_CHAR(TTFC.END_DATE, 'yyyy-MM-dd') END_DATE --有效期止\n");
		sql.append("  FROM TT_FLEET_CONTRACT TTFC\n");
		sql.append(" WHERE TTFC.FLEET_ID =" + fleet_id + "\n");
		sql.append(" ORDER BY TTFC.CREATE_DATE DESC\n");
		
		return dao.pageQuery(sql.toString(), null, dao.getFunName());
	}
	
	/***
	 * 实销信息上报:查询联系人列表(分页)
	 * 
	 * @param :
	 * @return
	 * @throws Exception
	 */
	public static PageResult<Map<String, Object>> getLinkManList(String linkManName, String ctm_id, int pageSize, int curPage)
	{
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT TTL.LM_ID,\n");
		sql.append("       TTL.NAME,\n");
		sql.append("       TTL.MAIN_PHONE,\n");
		sql.append("       TTL.OTHER_PHONE,\n");
		sql.append("       TTL.CONTRACT_REASON\n");
		sql.append("  FROM TT_LINKMAN TTL\n");
		sql.append(" WHERE TTL.CTM_ID = " + ctm_id + "\n");
		if (null != linkManName && !"".equals(linkManName))
		{
			sql.append("       AND TTL.NAME LIKE '%" + linkManName.trim() + "%'\n");
		}
		sql.append(" ORDER BY TTL.NAME\n");
		
		return dao.pageQuery(sql.toString(), null, dao.getFunName(), pageSize, curPage);
	}
	
	/***
	 * 实销信息上报:查询联系人列表
	 * 
	 * @param :
	 * @return
	 * @throws Exception
	 */
	public static List<Map<String, Object>> getLink_List(Long oldCustomerId)
	{
		if (null != oldCustomerId && 0 != oldCustomerId)
		{
			StringBuffer sql = new StringBuffer();
			sql.append("SELECT TTL.LM_ID,\n");
			sql.append("       TTL.NAME,\n");
			sql.append("       TTL.MAIN_PHONE,\n");
			sql.append("       TTL.OTHER_PHONE,\n");
			sql.append("       TTL.CONTRACT_REASON\n");
			sql.append("  FROM TT_LINKMAN TTL\n");
			sql.append(" WHERE TTL.CTM_ID = " + oldCustomerId + "\n");
			sql.append(" ORDER BY TTL.NAME\n");
			return dao.pageQuery(sql.toString(), null, dao.getFunName());
		}
		else
		{
			return null;
		}
		
	}
	
	public static Map<String, Object> getSaleDLRInfo(String vhId)
	{
		StringBuffer sql = new StringBuffer("\n");
		
		sql.append("SELECT TMD.DEALER_CODE, TMD.DEALER_NAME, TO_CHAR(TTDAS.SALES_DATE,'yyyy-mm-dd') SALES_DATE\n");
		sql.append("  FROM TT_DEALER_ACTUAL_SALES TTDAS, TM_DEALER TMD\n");
		sql.append(" WHERE TTDAS.DEALER_ID = TMD.DEALER_ID\n");
		sql.append("   AND TTDAS.VEHICLE_ID = " + vhId + "\n");
		
		return dao.pageQueryMap(sql.toString(), null, dao.getFunName());
	}
	
	public static Map<String, Object> getServiceactivityCharactor(String vehicleId)
	{
		StringBuffer sb= new StringBuffer();
		sb.append("SELECT T.VEHICLE_TYPE FROM TT_SPECIAL_VEHICLE_REPORT T WHERE T.VEHICLE_ID =" + vehicleId + "");
		return dao.pageQueryMap(sb.toString(), null, dao.getFunName());
	}
	
	/**
	 * 个人电话CHECK（相同车架号：即实销信息更改）
	 */
	public boolean checkPersonPhoneIsRepeat(String phone,String vehicleId,String name, String cardNum, boolean isMainPhone) {
		boolean repeatFlag = false;
		StringBuffer main_phone= new StringBuffer();
		main_phone.append("select T.CARD_NUM,T.CTM_NAME \n" );
		main_phone.append("  from tt_customer t, Tt_Dealer_Actual_Sales t1\n" );
		main_phone.append(" where t.ctm_id = t1.ctm_id\n" );
		main_phone.append("   and t1.vehicle_Id != '"+vehicleId+"'\n" );
		if (isMainPhone) {
			main_phone.append("   and t.main_phone='").append(phone).append("' \n");
		} else {
			main_phone.append("   and t.other_phone='").append(phone).append("' \n");
		}
		
		List<Map<String, Object>> mainPhoneList = salesReportDAO.pageQuery(main_phone.toString(), null, dao.getFunName());
		if(mainPhoneList == null || mainPhoneList.isEmpty()){
			repeatFlag = true;
			return repeatFlag;
		} else {
			for (Map<String, Object> map : mainPhoneList) {
				if (name.equals(map.get("CTM_NAME")) && cardNum.equals(map.get("CARD_NUM"))) {
					repeatFlag = true;
					return repeatFlag;
				}
			}
		}
		
		return repeatFlag;
	}

	/**
	 * 公司电话CHECK（相同车架号：即实销信息更改）
	 */
	public boolean checkCompanyPhoneIsRepeat(String phone,String vehicleId,String name, boolean isCompanyPhone) {
		boolean repeatFlag = false;
		
		StringBuffer company_phone= new StringBuffer();
		company_phone.append("select T.CTM_NAME \n" );
		company_phone.append("  from tt_customer t, Tt_Dealer_Actual_Sales t1\n" );
		company_phone.append(" where t.ctm_id = t1.ctm_id\n" );
		company_phone.append("   and t1.vehicle_Id != '"+vehicleId+"'\n" );
		if (isCompanyPhone) {
			company_phone.append("   and t.company_phone='").append(phone).append("' \n");
		} else {
			company_phone.append("   and t.link_man_phone='").append(phone).append("' \n");
		}
		List<Map<String, Object>> companyPhoneList = salesReportDAO.pageQuery(company_phone.toString(), null, dao.getFunName());
		
		if(companyPhoneList == null || companyPhoneList.isEmpty()){
			repeatFlag = true;
			return repeatFlag;
		} else {
			for (Map<String, Object> map : companyPhoneList) {
				if (name.equals(map.get("CTM_NAME"))) {
					repeatFlag = true;
					return repeatFlag;
				}
			}
		}
		
		return repeatFlag;
	}
	
	/**
	 * 个人客户的电话CHECK(不同车架号：实销上报)
	 */
	public boolean checkPersonPhone(String phone,String name,String cardNum, boolean isMainPhone) {
		boolean repeatFlag = false;
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT T.CARD_NUM,T.CTM_NAME \n");
		sql.append("FROM  TT_CUSTOMER T \n");
		if (isMainPhone) {
			sql.append("WHERE T.MAIN_PHONE = '").append(phone).append("' \n");
		} else {
			sql.append("WHERE T.OTHER_PHONE = '").append(phone).append("' \n");
		}
		List<Map<String, Object>> phoneLst = dao.pageQuery(sql.toString(), null, dao.getFunName());
		if (phoneLst == null || phoneLst.isEmpty()) {
			repeatFlag = true;
			return repeatFlag;
		} else {
			for (Map<String, Object> map : phoneLst) {
				String CARD_NUM = (String) map.get("CARD_NUM");
				String CTM_NAME = (String) map.get("CTM_NAME");
				if (CARD_NUM.equals(cardNum) && CTM_NAME.equals(name)) {
					repeatFlag = true;
					return repeatFlag;
				}
			}
		}
		return repeatFlag;
	}
	
	/**
	 * 公司客户的电话CHECK(不同车架号：实销上报)
	 */
	public boolean checkCompanyPhone(String phone,String name,boolean isCompanyPhone) {
		boolean repeatFlag = false;
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT T.CTM_NAME \n");
		sql.append("FROM  TT_CUSTOMER T \n");
		if (isCompanyPhone) {
			sql.append("WHERE T.COMPANY_PHONE = '").append(phone).append("' \n");
		} else {
			sql.append("WHERE T.link_man_phone = '").append(phone).append("' \n");
		}
		List<Map<String, Object>> phoneList = dao.pageQuery(sql.toString(), null, dao.getFunName());
		if(phoneList == null || phoneList.isEmpty()){
			repeatFlag = true;
			return repeatFlag;
		} else {
			for (Map<String, Object> map : phoneList) {
				String CTM_NAME = (String) map.get("CTM_NAME");
				if (CTM_NAME.equals(name)) {
					repeatFlag = true;
					return repeatFlag;
				}
			}
		}
		return repeatFlag;
	}
	
	@Override
	protected PO wrapperPO(ResultSet rs, int idx)
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * 查询销售人员的信息
	 * @param name
	 * @param dlrCompanyId
	 * @param oemCompanyId
	 * @param pageSize
	 * @param curPage
	 * @return
	 * @throws Exception
	 */
	public static PageResult<Map<String, Object>> getSalesManList
	(String name, Long dlrCompanyId, Long oemCompanyId, int pageSize, int curPage)throws Exception{
		StringBuilder sql= new StringBuilder();
		sql.append("SELECT TVP.NAME,TVP.GENDER,TVP.MOBILE,VW.DEALER_NAME,\n" );
		sql.append("  TVP.PERSON_ID \n");
		sql.append("  FROM TT_VS_PERSON TVP,vw_org_dealer VW\n" );
		sql.append(" WHERE VW.DEALER_ID=TVP.DEALER_ID\n" );
		sql.append("   AND TVP.POSITION = 99961001\n" );
		sql.append("   AND TVP.IS_INVESTOR = 99951002\n") ;
		sql.append("   AND TVP.POSITION_STATUS = 99941001\n") ;
		sql.append("   AND TVP.NAME LIKE '%"+name+"%'\n" );
		sql.append("   AND TVP.DEALER_ID IN ("+dlrCompanyId+")");

		return  dao.pageQuery(sql.toString(), null, dao.getFunName(), pageSize,
				curPage);
	}
	
	public static PageResult<Map<String, Object>> getActivitiesList(Map<String,String> map,int pageSize ,int curPage ) throws Exception{
		List<Object> params = new LinkedList<Object>();
        String dealerId=map.get("dealerId");
        String campaignName=map.get("campaignName");


        StringBuilder sql= new StringBuilder();

        sql.append("SELECT TC.CAMPAIGN_ID,\n" );
        sql.append("       TC.CAMPAIGN_NO,\n" );
        sql.append("       TC.CAMPAIGN_NAME,\n" );
        sql.append("       TC.CAMPAIGN_SUBJECT,\n" );
        sql.append("       TO_CHAR(TC.START_DATE, 'YYYY-MM-DD') START_DATE,\n" );
        sql.append("       TO_CHAR(TC.END_DATE, 'YYYY-MM-DD') END_DATE,\n" );
        sql.append("       TCP.SPACE_ID,\n" );
        sql.append("       TCP.PLAN_ID,\n" );
        sql.append("       TCP.CHECK_STATUS\n" );
        sql.append("  FROM TT_CAMPAIGN_PLAN TCP, TT_CAMPAIGN TC\n" );
        sql.append(" WHERE TCP.CAMPAIGN_ID = TC.CAMPAIGN_ID\n" );
        sql.append("   AND TCP.DEALER_ID = "+dealerId+"\n" );
        sql.append("   AND TCP.CHECK_STATUS NOT IN("+ Constant.CAMPAIGN_CHECK_STATUS_24+")\n");

        //变更申请类型为活动取消 并且等待审核和审核通过状态下 则不能作为实销上报的市场活动
        sql.append("AND NOT EXISTS(\n" );
        sql.append("    SELECT 1 FROM TT_CAMPAIGN_PLAN_REQ M\n" );
        sql.append("    WHERE TCP.PLAN_ID=M.PLAN_ID\n" );
        sql.append("    AND M.CHNG_TYPE=13271002\n" );
        sql.append("    AND M.CHECK_STATUS IN(11261017,11261018,11261020,11261022)\n" );
        sql.append(")\n" );
        //活动总结处于等待审核和审核通过状态下 则不能作为实销上报的市场活动
        sql.append("AND NOT EXISTS(\n" );
        sql.append("    SELECT 1 FROM TT_CAMPAIGN_SUMMERY N\n" );
        sql.append("    WHERE TCP.PLAN_ID=N.PLAN_ID\n" );
        sql.append("    AND N.STATUS IN(13291002,13291003,13291004,13291006)\n" );
        sql.append(")");

		if(!"".equals(campaignName)&&campaignName!=null){
			sql.append("AND TC.CAMPAIGN_NAME LIKE'%"+campaignName+"%'\n");
		}

		PageResult<Map<String, Object>> rs = dao.pageQuery(sql.toString(), params, dao.getFunName(),pageSize, curPage);
		return rs;
	}
	
	/***
	 *实销信息查询:查询实销历史信息
	 * 
	 * @param :
	 * @return
	 * @throws Exception
	 */
	public static PageResult<Map<String, Object>> queryReportInfoList_CVS(String areaId, String dealerId, Map map, int pageSize, int curPage) {

		String customer_type = (String) map.get("customer_type"); // 客户类型
		String customer_name = (String) map.get("customer_name"); // 客户名称
		String vin = (String) map.get("vin"); // VIN
		String customer_phone = (String) map.get("customer_phone"); // 客户电话
		String materialCode = (String) map.get("materialCode"); // 选择物料组
		String is_fleet = (String) map.get("is_fleet"); // 是否集团客户
		String fleet_name = (String) map.get("fleet_name"); // 集团客户名称
		String contract_no = (String) map.get("contract_no"); // 集团客户合同
		String startDate = (String) map.get("startDate"); // 上报日期:开始
		String endDate = (String) map.get("endDate"); // 上报日期 ：截止
		String poseId=(String)map.get("poseId");

		StringBuffer sql = new StringBuffer("\n");
		
		sql.append("SELECT TTS.ORDER_ID,DECODE(TTC.CTM_NAME, NULL, TTC.COMPANY_NAME, TTC.CTM_NAME) C_NAME, --客户名称\n");
		sql.append("       TMD.DEALER_SHORTNAME, --经销商简称\n");
		sql.append("       TTC.CTM_TYPE, --客户类型\n");
		sql.append("       TTS.IS_FLEET, --是否集团客户\n");
		sql.append("       DECODE(TTC.Ctm_Type, " + Constant.CUSTOMER_TYPE_02 + ", TTC.COMPANY_PHONE, TTC.MAIN_PHONE) MAIN_PHONE, --主要联系电话\n");
		sql.append("       TMVM.MATERIAL_CODE,\n");
		sql.append("       TMVM.MATERIAL_NAME,\n");
		sql.append("       TMV.VIN,\n");
		sql.append("       TMV.ENGINE_NO, --VIN\n");
		sql.append("	   TTS.CAR_CHARACTOR, \n") ;
		sql.append("	   TSC.NAME SALES_CON_NAME, \n") ;
		sql.append("       TO_CHAR(TTS.SALES_DATE, 'yyyy-MM-dd') SALES_DATE, --实销时间\n");
		sql.append("       TMV.VEHICLE_ID,\n");
		sql.append("       TO_CHAR(TMV.PRODUCT_DATE,'yyyy-MM-dd') PRODUCT_DATE, --生产日期\n");
		sql.append("       TTC.CTM_ID\n");
		sql.append("  FROM TT_DEALER_ACTUAL_SALES TTS,\n");
		sql.append("       TT_CUSTOMER            TTC,\n");
		sql.append("       TT_SALES_CONSULTANT    TSC,\n");
		sql.append("       TM_VEHICLE             TMV,\n");
		sql.append("       TM_DEALER       		  TMD,\n");
		sql.append("       TM_VHCL_MATERIAL       TMVM\n");
		sql.append(" WHERE TTS.CTM_ID = TTC.CTM_ID\n");
		sql.append("   AND TTS.VEHICLE_ID = TMV.VEHICLE_ID\n");
		sql.append("   AND TTS.SALES_CON_ID = TSC.ID(+)\n");
		sql.append("   AND TMV.MATERIAL_ID = TMVM.MATERIAL_ID\n");
		sql.append("   AND TTS.DEALER_ID = TMD.DEALER_ID\n");
		sql.append("   AND TTS.IS_RETURN ="+Constant.IF_TYPE_NO+"\n");
		sql.append("   AND TMV.LIFE_CYCLE = " + Constant.VEHICLE_LIFE_04 + "\n");
		sql.append("   AND TMV.DEALER_ID IN (" + dealerId + ")\n");
		// 客户类型
		if (null != customer_type && !"".equals(customer_type)
				&& !"-1".equals(customer_type)) {
			sql.append("   AND TTC.CTM_TYPE =" + customer_type + "\n");
		}
		if(areaId!="" && areaId!=null){
			sql.append("   AND TMV.AREA_ID IN ("+areaId+")\n");
		}
		// 客户名称
		if (null != customer_name && !"".equals(customer_name)) {
			sql.append("   AND (TTC.CTM_NAME LIKE '%" + customer_name.trim()
					+ "%' OR TTC.COMPANY_S_NAME LIKE '%" + customer_name.trim()
					+ "%')\n");
		}
		// VIN
		if (null != vin && !"".equals(vin)) {
			sql.append(GetVinUtil.getVins(vin, "TMV"));
		}
		// 客户电话
		if (null != customer_phone && !"".equals(customer_phone)) {
			sql.append("   AND (TTC.MAIN_PHONE LIKE '%" + customer_phone.trim()
					+ "%' OR TTC.COMPANY_PHONE LIKE '%" + customer_phone.trim()
					+ "%')\n");
		}
		// 选择物料组
		if (null != materialCode && !"".equals(materialCode)) {
			String[] materialCodes = materialCode.split(",");
			if (null != materialCodes && materialCodes.length > 0) {
				StringBuffer buffer = new StringBuffer();
				for (int i = 0; i < materialCodes.length; i++) {
					buffer.append("'").append(materialCodes[i]).append("'")
							.append(",");
				}
				buffer = buffer.deleteCharAt(buffer.length() - 1);
				sql.append("   AND ((TMV.PACKAGE_ID IN (SELECT G.GROUP_ID \n");
				sql.append("                               FROM TM_VHCL_MATERIAL_GROUP G \n");
				sql.append("                              WHERE G.GROUP_CODE IN (" + buffer.toString() + "))) \n");
				sql.append("       OR (TMV.MODEL_ID IN (SELECT G.GROUP_ID \n");
				sql.append("                             FROM TM_VHCL_MATERIAL_GROUP G \n");
				sql.append("                            WHERE G.GROUP_CODE IN (" + buffer.toString() + "))) \n");
				sql.append("       OR (TMV.SERIES_ID IN (SELECT G.GROUP_ID \n");
				sql.append("                              FROM TM_VHCL_MATERIAL_GROUP G \n");
				sql.append("                             WHERE G.GROUP_CODE IN (" + buffer.toString() + ")))) \n");
			}
		}
		// 是否集团客户
		if (null != is_fleet && !"".equals(is_fleet) && !"-1".equals(is_fleet)) {
			sql.append("   AND TTS.IS_FLEET = " + is_fleet + "\n");
		}
		if (!is_fleet.equals(Constant.IF_TYPE_NO.toString())) {
			// 集团客户名称
			if (null != fleet_name && !"".equals(fleet_name)) {
				sql.append("   AND TTC.CTM_NAME LIKE '%"
						+ fleet_name.trim() + "%'\n");
			}
			// 集团客户合同
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
		sql.append(" ORDER BY TTS.SALES_DATE DESC\n");

		return dao.pageQuery(sql.toString(), null, dao.getFunName(), pageSize, curPage);
	}
	
	/**
	 * 实销信息上报:验证大客户需求车系
	 * @param fleet_id
	 * @return
	 */
	public static int getCheckSeriesGroup(String fleet_id,String group_id) {
		int count=0;
		StringBuilder sql= new StringBuilder();
		sql.append("SELECT COUNT(*) COUNT\n" );
		sql.append("  FROM (SELECT DISTINCT TVMP1.GROUP_ID\n" );
		sql.append("          FROM TM_VHCL_MATERIAL         TVM,\n" );
		sql.append("               TM_VHCL_MATERIAL_GROUP   TVMP1,\n" );
		sql.append("               TM_VHCL_MATERIAL_GROUP   TVMP2,\n" );
		sql.append("               TM_VHCL_MATERIAL_GROUP   TVMP3,\n" );
		sql.append("               TM_VHCL_MATERIAL_GROUP_R TVMGR,\n" );
		sql.append("                TT_FLEET_SUPPORT_INFO  TFRD\n" );
		sql.append("         WHERE TVMP1.GROUP_ID = TVMP2.PARENT_GROUP_ID\n" );
		sql.append("           AND TVMP2.GROUP_ID = TVMP3.PARENT_GROUP_ID\n" );
		sql.append("           AND TVM.MATERIAL_ID = TVMGR.MATERIAL_ID\n" );
		sql.append("           AND TVMGR.GROUP_ID = TVMP3.GROUP_ID\n" );
		sql.append("           AND TFRD.MATER_ID = TVM.MATERIAL_ID\n" );
		sql.append("           AND TFRD.FLEET_ID = "+fleet_id+") C\n" );
		sql.append(" WHERE C.GROUP_ID = "+group_id+"");

		List<Map<String,Object>> list=dao.pageQuery(sql.toString(), null, dao.getFunName());
		count=Integer.parseInt(list.get(0).get("COUNT").toString());
		return count ;
	}
	
	/***
	 *实销信息更改申请:查询可申请更改的信息
	 * 
	 * @param :
	 * @return
	 * @throws Exception
	 */
	public static PageResult<Map<String, Object>> queryReportInfoList_Change(String areaId,
			String dealerId, Map map, int pageSize, int curPage) {

		String customer_type = (String) map.get("customer_type"); // 客户类型
		String customer_name = (String) map.get("customer_name"); // 客户名称
		String vin = (String) map.get("vin"); // VIN
		String customer_phone = (String) map.get("customer_phone"); // 客户电话
		String materialCode = (String) map.get("materialCode"); // 选择物料组
		String is_fleet = (String) map.get("is_fleet"); // 是否集团客户
		String fleet_name = (String) map.get("fleet_name"); // 集团客户名称
		String contract_no = (String) map.get("contract_no"); // 集团客户合同
		String startDate = (String) map.get("startDate"); // 上报日期:开始
		String endDate = (String) map.get("endDate"); // 上报日期 ：截止

		StringBuffer sql = new StringBuffer();
		sql
				.append("SELECT DECODE(TTC.CTM_TYPE, 10831002, TTC.COMPANY_NAME,10831001, TTC.CTM_NAME) C_NAME, --客户名称\n");
		sql.append("       TTC.CTM_TYPE, --客户类型\n");
		sql.append("       TTS.IS_FLEET, --是否集团客户\n");
		sql.append("       TD.DEALER_SHORTNAME, --经销商名称\n");
		sql.append("       TD.DEALER_CODE, --经销商代码\n");
		sql
				.append("       DECODE(TTC.CTM_NAME, NULL, TTC.COMPANY_PHONE, TTC.MAIN_PHONE) MAIN_PHONE, --主要联系电话\n");
		sql.append("       TMVM.MATERIAL_CODE,\n");
		sql.append("       TMVM.MATERIAL_NAME,\n");
		sql.append("       TMV.VIN,\n");
		sql
				.append("       TO_CHAR(TTS.SALES_DATE, 'yyyy-MM-dd') SALES_DATE, --实销时间\n");
		sql.append("       TMV.VEHICLE_ID,\n");
		sql.append("       TTC.CTM_ID,\n");
		sql.append("       TTS.ORDER_ID\n");
		sql.append("  FROM TT_DEALER_ACTUAL_SALES TTS,\n");
		sql.append("       TT_CUSTOMER            TTC,\n");
		sql.append("       TM_VEHICLE             TMV,\n");
		sql.append("       TM_VHCL_MATERIAL       TMVM,\n");
		sql.append("       TM_DEALER       TD\n");
		sql.append(" WHERE TTS.CTM_ID = TTC.CTM_ID\n");
		sql.append("   AND TTS.VEHICLE_ID = TMV.VEHICLE_ID\n");
		sql.append("   AND TTS.DEALER_ID = TD.DEALER_ID \n");
		sql.append("   AND TMV.MATERIAL_ID = TMVM.MATERIAL_ID\n");
		sql.append("   AND TTS.IS_RETURN!='"+Constant.IF_TYPE_YES+"'\n");
		if(areaId!=null&&areaId!=""){
			sql.append("   AND TMV.AREA_ID IN ("+areaId+")\n");
		}
		sql.append("   AND TMV.LOCK_STATUS = " + Constant.LOCK_STATUS_01 + "\n");
		sql.append("   AND TMV.LIFE_CYCLE = " + Constant.VEHICLE_LIFE_04 + "\n");
		sql.append("   AND TTS.DEALER_ID IN (" + dealerId + ")\n");
		// 客户类型
		if (null != customer_type && !"".equals(customer_type)
				&& !"-1".equals(customer_type)) {
			sql.append("   AND TTC.CTM_TYPE =" + customer_type + "\n");
		}
		// 客户名称
		if (null != customer_name && !"".equals(customer_name)) {
			sql.append("   AND (TTC.CTM_NAME LIKE '%" + customer_name.trim()
					+ "%' OR TTC.COMPANY_S_NAME LIKE '%" + customer_name.trim()
					+ "%')\n");
		}
		// VIN
		if (null != vin && !"".equals(vin)) {
			sql.append(GetVinUtil.getVins(vin, "TMV"));
		}
		// 客户电话
		if (null != customer_phone && !"".equals(customer_phone)) {
			sql.append("   AND (TTC.MAIN_PHONE LIKE '%" + customer_phone.trim()
					+ "%' OR TTC.COMPANY_PHONE LIKE '%" + customer_phone.trim()
					+ "%')\n");
		}
		// 选择物料组
		if (null != materialCode && !"".equals(materialCode)) {
			String[] materialCodes = materialCode.split(",");
			if (null != materialCodes && materialCodes.length > 0) {
				StringBuffer buffer = new StringBuffer();
				for (int i = 0; i < materialCodes.length; i++) {
					buffer.append("'").append(materialCodes[i]).append("'")
							.append(",");
				}
				buffer = buffer.deleteCharAt(buffer.length() - 1);
				sql.append("   AND ((TMV.PACKAGE_ID IN (SELECT G.GROUP_ID \n");
				sql
						.append("                               FROM TM_VHCL_MATERIAL_GROUP G \n");
				sql
						.append("                              WHERE G.GROUP_CODE IN ("
								+ buffer.toString() + "))) \n");
				sql.append("       OR (TMV.MODEL_ID IN (SELECT G.GROUP_ID \n");
				sql
						.append("                             FROM TM_VHCL_MATERIAL_GROUP G \n");
				sql
						.append("                            WHERE G.GROUP_CODE IN ("
								+ buffer.toString() + "))) \n");
				sql.append("       OR (TMV.SERIES_ID IN (SELECT G.GROUP_ID \n");
				sql
						.append("                              FROM TM_VHCL_MATERIAL_GROUP G \n");
				sql
						.append("                             WHERE G.GROUP_CODE IN ("
								+ buffer.toString() + ")))) \n");
			}
		}
		// 是否集团客户
		if (null != is_fleet && !"".equals(is_fleet) && !"-1".equals(is_fleet)) {
			sql.append("   AND TTS.IS_FLEET = " + is_fleet + "\n");
		}
		if (!is_fleet.equals(Constant.IF_TYPE_NO.toString())) {
			// 集团客户名称
			if (null != fleet_name && !"".equals(fleet_name)) {
				sql.append("   AND TTC.COMPANY_S_NAME LIKE '%"
						+ fleet_name.trim() + "%'\n");
			}
			// 集团客户合同
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
		sql.append("AND TMV.LOCK_STATUS!=10241005");
		sql
				.append(" AND NOT EXISTS (SELECT 1 FROM TT_DEALER_ACTUAL_SALES_AUDIT TA WHERE TA.CTM_ID = TTC.CTM_ID AND TA.STATUS="
						+ Constant.SALES_INFO_CHANGE_STATUS_01 + ")\n");
		sql.append(" ORDER BY TTS.SALES_DATE DESC\n");
		return dao.pageQuery(sql.toString(), null, dao.getFunName(), pageSize,
				curPage);
	}
	
	/***
	 *实销信息上报:填写上报信息--查看车辆信息
	 * 
	 * @param :
	 * @return
	 * @throws Exception
	 */
	public static Map<String, Object> getVehicleInfoS(String order_id) {
		StringBuffer sql = new StringBuffer("");
		sql.append("SELECT TMV.VEHICLE_ID,                         \n");
		sql.append("	   TMV.VIN,                          --VIN \n");
		sql.append("	   TMV.DEALER_ID,                          \n");
		sql.append("       TMV.ENGINE_NO,                    --发动机号 \n");
		sql.append("        TMVM.MATERIAL_ID MATERIAL_ID ,     ");
		sql.append("        G1.GROUP_ID GROUP_ID ,     ");
		sql.append("       G1.GROUP_NAME SERIES_NAME,        --车系 \n");
		sql.append("       G2.GROUP_NAME MODEL_NAME,         --车型 \n");
		sql.append("       TMVM.MATERIAL_CODE,               --物料代码 \n");
		sql.append("       TMVM.MATERIAL_NAME,               --物料名称 \n");
		sql.append("       TMVM.COLOR_NAME AS COLOR,                        --颜色 \n");
		sql.append("       SALES.IS_OLD_CTM,                 --新/老客户 \n");
		sql.append("       SALES.CAR_CHARACTOR,              --车辆性质 \n");
		sql.append("       SALES.ORDER_ID,                   --实销ID \n");
		sql.append("       G2.GROUP_ID,                       --车型id\n");
		sql.append("       SALES.ORDER_ID");
		sql.append("  FROM TM_VEHICLE TMV, \n");
		sql.append("       TM_VHCL_MATERIAL_GROUP G1, \n");
		sql.append("       TM_VHCL_MATERIAL_GROUP G2, \n");
		sql.append("       TM_VHCL_MATERIAL TMVM, \n");
		sql.append("       TT_DEALER_ACTUAL_SALES SALES \n");
		sql.append(" WHERE     TMV.SERIES_ID = G1.GROUP_ID \n");
		sql.append("       AND TMV.MODEL_ID = G2.GROUP_ID \n");
		sql.append("       AND TMV.MATERIAL_ID = TMVM.MATERIAL_ID \n");
		sql.append("	   AND TMV.VEHICLE_ID = SALES.VEHICLE_ID(+)\n");
		sql.append("       AND SALES.ORDER_ID = " + order_id + " \n");
		sql.append("       ORDER BY SALES.ORDER_ID DESC ");
		//sql.append("       AND SALES.IS_RETURN='"+Constant.IF_TYPE_NO+"'\n");
		return dao.pageQueryMap(sql.toString(), null, dao.getFunName());
	}
	public static Map<String, Object> getSaleDLRInfoByOderId(String orderId) {
		StringBuffer sql = new StringBuffer("\n") ;
		
		sql.append("SELECT TMD.DEALER_CODE, TMD.DEALER_NAME, TO_CHAR(TTDAS.SALES_DATE,'yyyy-mm-dd') SALES_DATE\n");
		sql.append("  FROM TT_DEALER_ACTUAL_SALES TTDAS, TM_DEALER TMD\n");  
		sql.append(" WHERE TTDAS.DEALER_ID = TMD.DEALER_ID\n");  
		sql.append("   AND TTDAS.ORDER_ID = " + orderId + "\n");

		return dao.pageQueryMap(sql.toString(), null, dao.getFunName()) ;
	}
}
