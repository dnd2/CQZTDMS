package com.infodms.dms.dao.report;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.common.Utility;
import com.infodms.dms.dao.common.BaseDao;
import com.infoservice.po3.bean.PO;

public class FleetSalesAllDao extends BaseDao{
	
	public static final Logger logger = Logger.getLogger(FleetSalesAllDao.class);
	public static final FleetSalesAllDao dao = new FleetSalesAllDao();
	public static final FleetSalesAllDao getInstance(){
		return dao;
	}
	
	
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}

	
	public List<Map<String, Object>> getFleetSalesAllSelect(Map<String, String> map){
		List<Map<String, Object>> list = null;
		String insertBeginTime = map.get("insertBeginTime");
		String insertEndTime = map.get("insertEndTime");
		String groupCode = map.get("groupCode");
		String dealerId = map.get("dealerId");
		String orgId = map.get("orgId");
		String fleetType = map.get("fleetType");
		String modelId = map.get("modelId");
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT DISTINCT /*+ all_rows */ VOD.ROOT_ORG_NAME,--大区\n");
		sql.append("VOD.REGION_NAME,--省份\n");
		sql.append("VOD.DEALER_NAME,--经销商\n");
		sql.append("TO_CHAR(TDAS.CONSIGNATION_DATE,'YYYY-MM-DD')CONSIGNATION_DATE,\n");
		sql.append(" TF.FLEET_NAME,--大客户名称\n");
		sql.append("TF.MAIN_LINKMAN,--联系人\n");
		sql.append("TF.MAIN_PHONE,--联系电话\n");
		sql.append("TC.CODE_DESC FLEET_TYPE,--客户类型\n");
		sql.append("D.GROUP_NAME,--车系\n");
		sql.append("D1.GROUP_NAME GROUPMODEL,--车型\n");
		sql.append("TDAS.PRICE,--单价\n");
		sql.append(" TFIN.INTENT_POINT,--享受政策支持\n");
		sql.append("DECODE(TDAS.FLEET_STATUS,11221001,1,DECODE(TDAS.FLEET_STATUS,11221002,1,0)) SALESAMOUNT,--实际实销数量\n");
		sql.append(" DECODE(TDAS.FLEET_STATUS,11221001,'未审核',DECODE(TDAS.FLEET_STATUS,11221002,'审核通过',''))FLEET_STATUS,\n");
		sql.append("TF.REQ_REMARK--报备信息\n");
		sql.append(" FROM TM_FLEET TF,\n");
		sql.append(" TT_DEALER_ACTUAL_SALES TDAS,\n");
		sql.append("TM_VHCL_MATERIAL_GROUP D,\n");
		sql.append("TM_VHCL_MATERIAL_GROUP D1,\n");
		sql.append(" VW_ORG_DEALER VOD,\n");
		sql.append("TM_DEALER TMD,\n");
		sql.append("TM_COMPANY TMC,\n");
		sql.append("TM_ORG TMO,\n");
		sql.append("TT_FLEET_INTENT_NEW TFIN,\n");
		sql.append("TC_CODE TC,\n");
		sql.append("TM_VEHICLE TMV\n");
		sql.append("WHERE TF.IS_DEL = 0\n");
		sql.append("AND TF.SERIES_ID = D.GROUP_ID(+)\n");
		sql.append("AND TMV.VEHICLE_ID=TDAS.VEHICLE_ID\n");
		sql.append("AND TMD.COMPANY_ID=TF.DLR_COMPANY_ID\n");
		sql.append("AND TMC.COMPANY_ID=TF.DLR_COMPANY_ID\n");
		sql.append("AND VOD.DEALER_ID=F_GET_PID(TMD.DEALER_ID)\n");
		sql.append("AND TMC.COMPANY_TYPE=10531002\n");
		sql.append(" AND TF.STATUS <>11021001\n");
		sql.append("AND TMO.ORG_ID(+)=VOD.ROOT_ORG_ID\n");
		sql.append("AND TMO.IS_COUNT=0\n");
		if(Utility.testString(insertBeginTime)){
			sql.append("AND TO_DATE(TO_CHAR(TDAS.CONSIGNATION_DATE,'YYYY-MM-DD'),'yyyy-mm-dd') >= TO_DATE('"+insertBeginTime+"','yyyy-mm-dd')\n");
		}
		if(Utility.testString(insertEndTime)){
			sql.append("AND (TO_DATE('"+insertEndTime+"','yyyy-mm-dd'))>= TO_DATE(TO_CHAR(TDAS.CONSIGNATION_DATE,'YYYY-MM-DD'),'yyyy-mm-dd')\n");
		}
		if(Utility.testString(groupCode)){
			sql.append("AND D.GROUP_CODE ='"+groupCode+"'\n");
		}
		if(Utility.testString(dealerId)){
			sql.append("AND VOD.DEALER_ID IN ("+dealerId+")\n");
		}
		if(Utility.testString(orgId)){
			sql.append("AND VOD.ROOT_ORG_ID IN ("+orgId+")\n");
		}
		if(Utility.testString(fleetType)){
			sql.append(" AND TF.FLEET_TYPE IN ("+fleetType+")\n");
		}
		if(Utility.testString(modelId)){
			sql.append("AND D1.GROUP_ID IN ("+modelId+")\n");
		}
		
		sql.append("  AND TF.FLEET_ID=TFIN.FLEET_ID\n");
		sql.append("AND TDAS.FLEET_ID=TF.FLEET_ID\n");
		sql.append("AND TC.CODE_ID=TF.FLEET_TYPE\n");
		sql.append("AND TMV.MODEL_ID=D1.GROUP_ID\n");
		sql.append("AND D1.GROUP_LEVEL=3\n");
		sql.append("order by ROOT_ORG_NAME,REGION_NAME,DEALER_NAME\n");
		list = super.pageQuery(sql.toString(), null, getFunName());
		return list;
	}
}
