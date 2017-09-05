package com.infodms.dms.dao.report;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.common.Utility;
import com.infodms.dms.dao.common.BaseDao;
import com.infoservice.po3.bean.PO;

public class FleetActTjDao extends BaseDao{
	public static final Logger logger = Logger.getLogger(FleetActTjDao.class);
	public static FleetActTjDao dao = new FleetActTjDao();
	public static FleetActTjDao getInstance(){
		return dao;
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}

	
	public List<Map<String, Object>> getFleetActTjSelect(Map<String, String> map){
		List<Map<String, Object>> list = null;
		StringBuffer sql = new StringBuffer();
		String contractNo = map.get("contractNo");
		String fleetName = map.get("fleetName");
		String fleetType = map.get("fleetType");
		String vin = map.get("vin");
		String groupCode = map.get("groupCode");
		String companyId = map.get("companyId");
		String checkStatus = map.get("checkStatus");
		String startDate = map.get("startDate");
		String endDate = map.get("endDate");

		sql.append("SELECT   /*+ all_rows */    XX.DEALER_CODE, --经销商代码\n");
		sql.append("XX.ROOT_ORG_NAME, --大区\n");
		sql.append(" XX.ROOT_DEALER_NAME,--上级单位\n");
		sql.append("XX.ROOT_DEALER_ID, --上级单位\n");
		sql.append("XX.REGION_NAME, --省份\n");
		sql.append("XX.CITY_NAME, --城市\n");
		sql.append("XX.TOWN, --省份\n");
		sql.append("XX.AREA_NAME, --业务范围\n");
		sql.append("TO_CHAR(XX.SALES_DATE,'YYYY-MM-DD') SALES_DATE, --销售日期\n");
		sql.append("TO_CHAR(XX.PRODUCT_DATE,'YYYY-MM-DD') PRODUCT_DATE, --生产日期\n");
		sql.append("TO_CHAR(XX.INVOICE_DATE,'YYYY-MM-DD') INVOICE_DATE, --开票日期\n");
		sql.append("XX.DEALER_SHORTNAME, --销售单位\n");
		sql.append("XX.CTM_NAME, --客户名称\n");
		sql.append("XX.CTM_TYPE, --客户类型\n");
		sql.append("XX.IS_FLEET, --是否集团客户\n");
		sql.append("XX.FLEET_NAME, --集团客户名称\n");
		sql.append("XX.CONTRACT_NO, --集团客户合同\n");
		sql.append("XX.SERNAME, --车型系列\n");
		sql.append(" XX.MODNAME, --车型类别\n");
		sql.append("XX.MODCODE, --车型编码\n");
		sql.append(" XX.PACKNAME, --状态\n");
		sql.append(" XX.PARTFLEET_TYPE,\n");
		sql.append("XX.FLEET_TYPE,\n");
		sql.append("XX.PARTFLEET_TYPE,\n");
		sql.append("XX.COLOR,\n");
		sql.append("XX.VIN\n");
		sql.append(" FROM (SELECT TMD.DEALER_CODE, --经销商代码\n");
		sql.append("VOD.ROOT_ORG_NAME, --大区\n");
		sql.append("VOD.ROOT_DEALER_NAME,--上级单位\n");
		sql.append("VOD.ROOT_DEALER_ID, --上级单位\n");
		sql.append("VOD.REGION_NAME, --省份\n");
		sql.append("VOD.CITY_NAME, --城市\n");
		sql.append("TMR.REGION_NAME TOWN, --省份\n");
		sql.append("TBA.AREA_NAME, --业务范围\n");
		sql.append(" TTS.SALES_DATE, --销售日期\n");
		sql.append(" TMV.PRODUCT_DATE, --生产日期\n");
		sql.append(" TTS.INVOICE_DATE, --开票日期\n");
		sql.append("TMD.DEALER_SHORTNAME, --销售单位\n");
		sql.append("TTC.CTM_NAME, --客户名称\n");
		sql.append(" TC1.CODE_DESC CTM_TYPE, --客户类型\n");
		sql.append("TTS.IS_FLEET, --是否集团客户\n");
		sql.append(" TF.FLEET_NAME, --集团客户名称\n");
		sql.append("TFC.CONTRACT_NO, --集团客户合同\n");
		sql.append(" TVMG.GROUP_NAME SERNAME, --车型系列\n");
		sql.append("TVMG1.GROUP_NAME MODNAME, --车型类别\n");
		sql.append(" TVMG1.GROUP_CODE MODCODE, --车型编码\n");
		sql.append("TVMG2.GROUP_NAME PACKNAME, --状态\n");
		sql.append(" TC2.CODE_DESC PARTFLEET_TYPE,\n");
		sql.append(" TC2.CODE_DESC FLEET_TYPE,\n");
		sql.append("TMV.COLOR, TMV.VIN\n");
		sql.append("FROM TT_DEALER_ACTUAL_SALES TTS,\n");
		sql.append(" TM_DEALER              TMD,\n");
		sql.append(" TT_CUSTOMER            TTC,\n");
		sql.append("TM_VEHICLE             TMV,\n");
		sql.append("TM_VHCL_MATERIAL       M,\n");
		sql.append("TM_BUSINESS_AREA       TBA,\n");
		sql.append(" TM_FLEET               TF,\n");
		sql.append("TT_FLEET_CONTRACT      TFC,\n");
		sql.append("VW_ORG_DEALER          VOD,\n");
		sql.append(" TM_VHCL_MATERIAL_GROUP TVMG,\n");
		sql.append("TM_VHCL_MATERIAL_GROUP TVMG1,\n");
		sql.append(" TM_VHCL_MATERIAL_GROUP TVMG2,\n");
		sql.append("TC_CODE TC1,TC_CODE TC2,TM_REGION TMR\n");
		sql.append(" WHERE TTS.DEALER_ID = TMD.DEALER_ID\n");
		sql.append(" AND TC2.CODE_ID=TF.FLEET_TYPE\n");
		sql.append("AND TC1.CODE_ID=TTC.CTM_TYPE\n");
		sql.append("AND TVMG.GROUP_ID = TMV.SERIES_ID\n");
		sql.append("AND TVMG1.GROUP_ID = TMV.MODEL_ID\n");
		sql.append(" AND TVMG2.GROUP_ID = TMV.PACKAGE_ID\n");
		sql.append("AND TBA.AREA_ID(+) = TMV.AREA_ID\n");
		sql.append(" AND VOD.DEALER_ID = TMD.DEALER_ID\n");
		sql.append("AND TTS.CTM_ID = TTC.CTM_ID\n");
		sql.append("AND TMR.REGION_CODE=TTC.TOWN\n");
		sql.append("AND TTS.VEHICLE_ID = TMV.VEHICLE_ID\n");
		sql.append(" AND TMV.MATERIAL_ID = M.MATERIAL_ID\n");
		sql.append("AND TTS.FLEET_ID = TF.FLEET_ID(+)\n");
		sql.append("AND TTS.IS_RETURN = '10041002'\n");
		sql.append(" AND TTS.IS_FLEET = 10041001\n");
		sql.append("AND TTS.CONTRACT_ID = TFC.CONTRACT_ID(+)\n");
		sql.append("  AND TMV.LIFE_CYCLE = 10321004\n");
		if(Utility.testString(contractNo)){
			sql.append("AND TFC.CONTRACT_NO ='"+contractNo+"'\n");
		}
		if(Utility.testString(fleetName)){
			sql.append("AND TTC.CTM_NAME ='"+fleetName+"'\n");
		}
		if(Utility.testString(fleetType)){
			sql.append("AND TF.FLEET_TYPE ='"+fleetType+"'\n");
		}
		if(Utility.testString(vin)){
			sql.append("AND TMV.VIN LIKE '%"+vin+"%'\n");
		}
		if(Utility.testString(groupCode)){
			sql.append(" AND TVMG2.GROUP_CODE in (");
			String groupCodes[]=groupCode.split(",");
			for(int i=0;i<groupCodes.length;i++){
			if(i!=groupCodes.length-1){
				sql.append("'"+groupCodes[i]+"',");}
			else{
				sql.append("'"+groupCodes[i]+"'");
			}
							}
			sql.append(") ");
		}
		if(Utility.testString(companyId)){
			sql.append("AND TTS.DLR_COMPANY_ID IN("+companyId+")\n");
		}
		if(Utility.testString(checkStatus)){
			sql.append("AND TTS.FLEET_STATUS ='"+checkStatus+"'\n");
		}
		if(Utility.testString(startDate)){
			sql.append("AND TTS.SALES_DATE >= TO_DATE('"+startDate+"','yyyy-mm-dd')\n");
		}
		if(Utility.testString(endDate)){
			sql.append("AND (TO_DATE('"+endDate+"','yyyy-mm-dd'))>= TTS.SALES_DATE\n");
		}
		sql.append("UNION  SELECT DISTINCT  TMD.DEALER_CODE, --经销商代码\n");
		sql.append("VOD.ROOT_ORG_NAME, --大区\n");
		sql.append("VOD.ROOT_DEALER_NAME,--上级单位\n");
		sql.append("VOD.ROOT_DEALER_ID, --上级单位\n");
		sql.append("VOD.REGION_NAME, --省份\n");
		sql.append("VOD.CITY_NAME, --城市\n");
		sql.append("TMR.REGION_NAME TOWN, --省份\n");
		sql.append("TBA.AREA_NAME, --业务范围\n");
		sql.append("TTS.SALES_DATE, --销售日期\n");
		sql.append("TMV.PRODUCT_DATE, --生产日期\n");
		sql.append("TTS.INVOICE_DATE, --开票日期\n");
		sql.append("TMD.DEALER_SHORTNAME, --销售单位\n");
		sql.append(" TTC.CTM_NAME, --客户名称\n");
		sql.append(" TC2.CODE_DESC CTM_TYPE, --客户类型\n");
		sql.append("TTS.IS_FLEET, --是否集团客户\n");
		sql.append("TCP.PACT_NAME FLEET_NAME, --集团客户名称\n");
		sql.append("TFC.CONTRACT_NO CONTRACT_NO, --集团客户合同\n");
		sql.append("TVMG.GROUP_NAME SERNAME, --车型系列\n");
		sql.append("TVMG1.GROUP_NAME MODNAME, --车型类别\n");
		sql.append("TVMG1.GROUP_CODE MODCODE, --车型编码\n");
		sql.append("TVMG2.GROUP_NAME PACKNAME, --状态\n");
		sql.append(" TC3.CODE_DESC PARTFLEET_TYPE,\n");
		sql.append("TCP.PACT_NAME FLEET_TYPE,\n");
		sql.append("TMV.COLOR, TMV.VIN\n");
		sql.append(" FROM TT_DEALER_ACTUAL_SALES TTS,\n");
		sql.append("TM_DEALER              TMD,\n");
		sql.append("TT_CUSTOMER            TTC,\n");
		sql.append("TM_VEHICLE             TMV,\n");
		sql.append("TM_VHCL_MATERIAL       M,\n");
		sql.append("TM_FLEET               TF,\n");
		sql.append("TT_FLEET_CONTRACT      TFC,\n");
		sql.append("TM_COMPANY_PACT        TCP,\n");
		sql.append(" VW_ORG_DEALER          VOD,\n");
		sql.append("TM_BUSINESS_AREA       TBA,\n");
		sql.append("TM_VHCL_MATERIAL_GROUP TVMG,\n");
		sql.append("TM_VHCL_MATERIAL_GROUP TVMG1,\n");
		sql.append("TM_VHCL_MATERIAL_GROUP TVMG2,\n");
		sql.append(" TC_CODE TC2,TC_CODE TC3,TM_REGION TMR\n");
		sql.append("WHERE  TTS.DEALER_ID = TMD.DEALER_ID\n");
		sql.append("AND TMR.REGION_CODE=TTC.TOWN\n");
		sql.append("AND TC3.CODE_ID=TCP.PARENT_FLEET_TYPE\n");
		sql.append("AND TC2.CODE_ID=TTC.CTM_TYPE\n");
		sql.append(" AND TVMG.GROUP_ID = TMV.SERIES_ID\n");
		sql.append(" AND TBA.AREA_ID(+) = TMV.AREA_ID\n");
		sql.append("AND TVMG1.GROUP_ID = TMV.MODEL_ID\n");
		sql.append("AND TVMG2.GROUP_ID = TMV.PACKAGE_ID\n");
		sql.append("AND VOD.DEALER_ID = TMD.DEALER_ID\n");
		sql.append(" AND TTS.CTM_ID = TTC.CTM_ID\n");
		sql.append("AND TTS.VEHICLE_ID = TMV.VEHICLE_ID\n");
		sql.append("AND TMV.MATERIAL_ID = M.MATERIAL_ID\n");
		sql.append("AND TTS.FLEET_ID = TCP.PACT_ID\n");
		sql.append("AND TTS.IS_RETURN = '10041002'\n");
		sql.append("AND TTS.IS_FLEET = 10041001\n");
		sql.append("AND TTS.CONTRACT_ID = TFC.CONTRACT_ID(+)\n");
		sql.append("AND TMV.LIFE_CYCLE = 10321004\n");
		if(Utility.testString(contractNo)){
			sql.append("AND TFC.CONTRACT_NO ='"+contractNo+"'\n");
		}
		if(Utility.testString(fleetName)){
			sql.append("AND TTC.CTM_NAME ='"+fleetName+"'\n");
		}
		if(Utility.testString(fleetType)){
			sql.append("AND TC3.CODE_ID ='"+fleetType+"'\n");
		}
		if(Utility.testString(vin)){
			sql.append("AND TMV.VIN LIKE '%"+vin+"%'\n");
		}
		if(Utility.testString(groupCode)){
			sql.append(" AND TVMG2.GROUP_CODE in (");
			String groupCodes[]=groupCode.split(",");
			for(int i=0;i<groupCodes.length;i++){
			if(i!=groupCodes.length-1){
				sql.append("'"+groupCodes[i]+"',");}
			else{
				sql.append("'"+groupCodes[i]+"'");
			}
							}
			sql.append(") ");
		}
		if(Utility.testString(companyId)){
			sql.append("AND TTS.DLR_COMPANY_ID IN("+companyId+")\n");
		}
		if(Utility.testString(checkStatus)){
			sql.append("AND TTS.FLEET_STATUS ='"+checkStatus+"'\n");
		}
		if(Utility.testString(startDate)){
			sql.append("AND TTS.SALES_DATE >= TO_DATE('"+startDate+"','yyyy-mm-dd')\n");
		}
		if(Utility.testString(endDate)){
			sql.append("AND (TO_DATE('"+endDate+"','yyyy-mm-dd'))>= TTS.SALES_DATE\n");
		}
		sql.append("     ) XX\n");
		sql.append("WHERE (XX.IS_FLEET=10041001 AND XX.FLEET_NAME IS NOT NULL) OR XX.IS_FLEET=10041002\n");
		
		list = super.pageQuery(sql.toString(), null, getFunName());
		return list;
	}
}
