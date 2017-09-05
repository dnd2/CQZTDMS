package com.infodms.dms.dao.sales.salesInfoManage;

import java.sql.ResultSet;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.dao.sales.ordermanage.reqquery.ReqQueryDao;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.businessUtil.GetVinUtil;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

/**   
 * @Title  : SalesInfoChangeQueryDAO.java
 * @Package: com.infodms.dms.dao.sales.salesInfoManage
 * @Description: 实销信息更改查询OEM
 * @date   : 2010-6-30 
 * @version: V1.0   
 */
public class SalesInfoChangeQueryDAO extends BaseDao{
	public Logger logger = Logger.getLogger(SalesInfoChangeQueryDAO.class);
	private ActionContext act = ActionContext.getContext();
	private static final SalesInfoChangeQueryDAO dao = new SalesInfoChangeQueryDAO ();
	public static final SalesInfoChangeQueryDAO getInstance() {
		return dao;
	}
	
	/** 
	* @Title	  : salesInfoChangeQueryList 
	* @Description: 实销信息更改查询:结果展示
	* @return     : PageResult<Map<String,Object>>
	* @throws 
	* @LastUpdate :2010-6-30
	*/
	public static PageResult <Map<String,Object>> salesInfoChangeQueryList(Map map,int pageSize,int curPage){
		String orgId = (String)map.get("orgId");	
		String customer_type = (String)map.get("customer_type");	//客户类型
		String customer_name = (String)map.get("customer_name");	//客户名称
		String vin = (String)map.get("vin");						//VIN
		String customer_phone = (String)map.get("customer_phone");	//客户电话
		String materialCode = (String)map.get("materialCode");		//选择物料组
		String is_fleet = (String)map.get("is_fleet");				//是否集团客户
		String fleet_name = (String)map.get("fleet_name");			//集团客户名称
		String contract_no = (String)map.get("contract_no");		//集团客户合同
		String startDate = (String)map.get("startDate");			//上报日期:开始
		String endDate = (String)map.get("endDate");				//上报日期 ：截止
		String dealerCode = (String)map.get("dealerCode");			//经销商代码
		String checkStatus = (String)map.get("checkStatus");		//审核状态
		String areaId = (String)map.get("areaId");					//业务范围

		StringBuffer sql = new StringBuffer();
		
		sql.append("SELECT DECODE(TTC.CTM_TYPE, 10831002, TTC.COMPANY_NAME, 10831001,TTC.CTM_NAME) C_NAME, --客户名称\n");
		sql.append("       TTC.CTM_TYPE, --客户类型\n");  
		sql.append("       TTS.IS_FLEET, --是否集团客户\n");  
		sql.append("       TTC.MAIN_PHONE, --主要联系电话\n");  
		sql.append("       TMVM.MATERIAL_CODE,\n");  
		sql.append("       TMVM.MATERIAL_NAME,\n");  
		sql.append("       TMV.VIN,\n");  
		sql.append("       TO_CHAR(TTS.SALES_DATE, 'yyyy-MM-dd') SALES_DATE, --实销时间\n");  
		sql.append("       TTS.STATUS,\n");  
		sql.append("       TMV.VEHICLE_ID,\n");  
		sql.append("       TTC.CTM_ID,\n");  
		sql.append("       TTC.CTM_EDIT_ID,\n");  
		sql.append("       TTS.LOG_ID,\n");  
		sql.append("       TTS.ORDER_ID\n");
		sql.append("  FROM TT_DEALER_ACTUAL_SALES_AUDIT TTS,\n");  
		sql.append("       TT_CUSTOMER_EDIT_LOG         TTC,\n");  
		sql.append("       TM_VEHICLE                   TMV,\n");  
		sql.append("       TM_VHCL_MATERIAL             TMVM,\n");  
		sql.append("       TM_DEALER                    TMD,\n");  
		sql.append("       TM_FLEET                     TF,\n");  
		sql.append("       TT_FLEET_CONTRACT            TFC\n");  
		sql.append(" WHERE TTS.CTM_ID = TTC.CTM_ID\n");  
		sql.append("   AND TTS.VEHICLE_ID = TMV.VEHICLE_ID\n");  
		sql.append("   AND TMV.MATERIAL_ID = TMVM.MATERIAL_ID\n");  
		sql.append("   AND TMV.DEALER_ID = TMD.DEALER_ID\n");  
		sql.append("   AND TTS.FLEET_ID = TF.FLEET_ID(+)\n");  
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
				sql.append("   AND ((TMV.PACKAGE_ID IN (SELECT G.GROUP_ID \n");
				sql.append("                               FROM TM_VHCL_MATERIAL_GROUP G \n");
				sql.append("                              WHERE G.GROUP_CODE IN ("+ buffer.toString() + "))) \n");
				sql.append("       OR (TMV.MODEL_ID IN (SELECT G.GROUP_ID \n");
				sql.append("                             FROM TM_VHCL_MATERIAL_GROUP G \n");
				sql.append("                            WHERE G.GROUP_CODE IN ("+ buffer.toString() + "))) \n");
				sql.append("       OR (TMV.SERIES_ID IN (SELECT G.GROUP_ID \n");
				sql.append("                              FROM TM_VHCL_MATERIAL_GROUP G \n");
				sql.append("                             WHERE G.GROUP_CODE IN ("+ buffer.toString() + ")))) \n");
			}
		}
		if (!"".equals(is_fleet)) {
			sql.append("   AND TTS.IS_FLEET = " + is_fleet + "\n");
		}
		if (!"".equals(fleet_name)) {
			sql.append("   AND TF.FLEET_NAME LIKE '%"+fleet_name.trim()+"%'\n");
		}
		if (!"".equals(contract_no)) {
			sql.append("   AND TFC.CONTRACT_NO LIKE '%"+contract_no.trim()+"%'\n");
		}
		if (null != startDate && !"".equals(startDate)) {
			sql.append("   AND TTS.SALES_DATE >= TO_DATE('"+startDate+" 00:00:00','yyyy-MM-dd HH24:MI:SS')\n");
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
		if (!"".equals(checkStatus)) {
			sql.append("   AND TTS.STATUS = "+checkStatus+"\n");
		}
		//sql.append("   AND TMV.AREA_ID = "+areaId+"\n");  
		
		if(!CommonUtils.isNullString(orgId)) {
			sql.append(ReqQueryDao.limitOrg("TMD", "dealer_id", orgId)) ;
		}
		
		sql.append(" ORDER BY TTS.SALES_DATE DESC\n");
		return dao.pageQuery(sql.toString(), null,dao.getFunName(),pageSize ,curPage );
	}
	
	
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
}
