package com.infodms.dms.dao.report;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.dao.common.BaseDao;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.po3.bean.PO;

public class TestStatusDao extends BaseDao{
	private ActionContext act = ActionContext.getContext();
	private AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
	public static final Logger logger = Logger.getLogger(TestStatusDao.class);
	public static final TestStatusDao dao = new TestStatusDao();
	public static TestStatusDao getInstance(){
		return dao;
		
	}
	
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
	public List<Map<String, Object>> getTestStatusDaoSelect(Map<String, String> map){
		String areaId = map.get("areaId");
		String dealerId = map.get("dealerId");
		String orgId = map.get("orgId");
		String modelId = map.get("modelId");
		List<Map<String, Object>> list = null;
		StringBuffer sql = new StringBuffer();
		sql.append("WITH X AS (SELECT O.COMPANY_ID,\n");
		sql.append("VDO.ROOT_ORG_ID AS ORG_ID,\n");
		sql.append("VDO.ROOT_ORG_CODE AS ORG_CODE,\n");
		sql.append("VDO.ROOT_ORG_NAME AS ORG_NAME,\n");
		sql.append("VDO.REGION_NAME,\n");
		sql.append("VDO.ROOT_DEALER_CODE,\n");
		sql.append("VDO.ROOT_DEALER_NAME,\n");
		sql.append("VDO.DEALER_ID,\n");
		sql.append("VDO.DEALER_CODE,\n");
		sql.append("VDO.DEALER_NAME,\n");
		sql.append("(SUM(NVL(DT.DELIVERY_AMOUNT, 0)) - SUM(NVL(DT.OUT_AMOUNT, 0))) AS NODE_BILL_AMOUNT\n");
		sql.append(" FROM TT_VS_DLVRY D, TT_VS_DLVRY_DTL DT, VW_ORG_DEALER VDO, TT_VS_ORDER O \n");
		sql.append(" WHERE D.DELIVERY_ID = DT.DELIVERY_ID\n");
		sql.append(" AND O.ORDER_ID = D.ORDER_ID\n");
		sql.append("AND D.DELIVERY_STATUS IN (10281004) --已开票\n");
		sql.append("AND O.ORDER_ORG_ID = VDO.DEALER_ID --按订货方来进行统计\n");
		if(Utility.testString(areaId)){
			sql.append("AND O.AREA_ID IN  ("+areaId+")\n");
		}
		
		if(Utility.testString(dealerId)){
			sql.append("AND VDO.root_dealer_id IN  ("+dealerId+")\n");
		}
		
		if(Utility.testString(orgId)){
			sql.append("AND VDO.ROOT_ORG_ID IN  ("+orgId+")\n");
		}
		
		if(Utility.testString(modelId)){
			sql.append("AND EXISTS (SELECT /*+ all_rows */ 1 FROM VW_MATERIAL_GROUP VW, TM_VHCL_MATERIAL_GROUP_R GR  WHERE VW.PACKAGE_ID = GR.GROUP_ID  AND GR.MATERIAL_ID = VW.MODEL_ID AND (VW.BRAND_ID IN ("+modelId+") OR VW.SERIES_ID IN ("+modelId+") OR VW.MODEL_ID IN ("+modelId+") OR VW.PACKAGE_ID IN ("+modelId+")))\n");
		}
		
		sql.append("GROUP BY O.COMPANY_ID,\n");
		sql.append("VDO.ROOT_ORG_ID,\n");
		sql.append("VDO.ROOT_ORG_CODE,\n");
		sql.append(" VDO.ROOT_ORG_NAME,\n");
		sql.append("VDO.REGION_NAME,\n");
		sql.append("VDO.ROOT_DEALER_ID,\n");
		sql.append("VDO.ROOT_DEALER_CODE,\n");
		sql.append("VDO.ROOT_DEALER_NAME,\n");
		sql.append("VDO.DEALER_ID,\n");
		sql.append(" VDO.DEALER_CODE,\n");
		sql.append("VDO.DEALER_NAME),\n");
		sql.append("Y AS (SELECT /*+ all_rows */ VOD.COMPANY_ID,\n");
		sql.append("VOD.ROOT_ORG_ID AS ORG_ID,\n");
		sql.append("VOD.ROOT_ORG_CODE AS ORG_CODE,\n");
		sql.append("VOD.ROOT_ORG_NAME AS ORG_NAME,\n");
		sql.append("VOD.REGION_NAME,\n");
		sql.append("VOD.ROOT_DEALER_CODE,\n");
		sql.append("VOD.ROOT_DEALER_NAME,\n");
		sql.append("VOD.DEALER_ID,\n");
		sql.append("VOD.DEALER_CODE,\n");
		sql.append("VOD.DEALER_NAME,\n");
		sql.append("SUM(CASE WHEN LIFE_CYCLE = 10321005 THEN  1 ELSE 0 END) DEING_AMOUNT, --在途\n");
		sql.append("SUM(CASE  WHEN LIFE_CYCLE = 10321003 THEN 1 ELSE 0 END) STOCK_AMOUNT --在库\n");
		sql.append("FROM TM_VEHICLE V, VW_ORG_DEALER VOD ,VW_MATERIAL_GROUP VW, TM_VHCL_MATERIAL_GROUP_R GR\n");
		sql.append(" WHERE VOD.DEALER_ID = V.DEALER_ID\n");
		sql.append("AND V.DEALER_ID > 0\n");
		sql.append("AND VW.PACKAGE_ID = GR.GROUP_ID\n");
		sql.append("AND GR.MATERIAL_ID = V.MATERIAL_ID\n");

		sql.append("and v.life_cycle in (10321005, 10321003)\n") ;

	
		if(Utility.testString(areaId)){
			sql.append("and v.area_id in (").append(areaId).append(")\n") ;
			/*sql.append(" AND GR.GROUP_ID IN--业务范围过滤\n");
			sql.append("(SELECT T1.GROUP_ID\n");
			sql.append("FROM TM_VHCL_MATERIAL_GROUP T1\n");
			sql.append("WHERE T1.STATUS = 10011001\n");
			sql.append("START WITH T1.GROUP_ID IN\n");
			sql.append("(SELECT TAP.MATERIAL_GROUP_ID\n");
			sql.append("FROM TM_AREA_GROUP TAP\n");
			sql.append("WHERE 1=1\n");
			sql.append("AND TAP.AREA_ID IN  ("+areaId+")\n");
			sql.append("  )\n");
			sql.append("CONNECT BY PRIOR T1.GROUP_ID = T1.PARENT_GROUP_ID)\n");*/
		}
		
		
		if(Utility.testString(modelId)){
			sql.append("AND EXISTS (SELECT 1 FROM VW_MATERIAL_GROUP VW, TM_VHCL_MATERIAL_GROUP_R GR  WHERE VW.PACKAGE_ID = GR.GROUP_ID  AND GR.MATERIAL_ID = V.MATERIAL_ID AND (VW.BRAND_ID IN ("+modelId+") OR VW.SERIES_ID IN ("+modelId+") OR VW.MODEL_ID IN ("+modelId+") OR VW.PACKAGE_ID IN ("+modelId+")))\n");
		}
		if(Utility.testString(dealerId)){
			sql.append("AND vod.dealer_id IN  ("+dealerId+")\n");
		}
		if(Utility.testString(orgId)){
			sql.append("AND vod.ROOT_ORG_ID IN  ("+orgId+")\n");
		}
		if(Utility.testString(modelId)){
			sql.append("AND (VW.BRAND_ID IN ("+modelId+") OR VW.SERIES_ID IN ("+modelId+") OR VW.MODEL_ID IN ("+modelId+") OR VW.PACKAGE_ID IN ("+modelId+"))\n");
		}
		sql.append("GROUP BY VOD.COMPANY_ID,\n");
		sql.append("VOD.ROOT_ORG_ID,\n");
		sql.append("VOD.ROOT_ORG_CODE,\n");
		sql.append("VOD.ROOT_ORG_NAME,\n");
		sql.append("VOD.REGION_NAME,\n");
		sql.append("VOD.ROOT_DEALER_CODE,\n");
		sql.append(" VOD.ROOT_DEALER_NAME,\n");
		sql.append("VOD.DEALER_ID,\n");
		sql.append("VOD.DEALER_CODE,\n");
		sql.append("VOD.DEALER_NAME)\n");
		sql.append("SELECT NVL(X.COMPANY_ID, Y.COMPANY_ID) COMPANY_ID,\n");
		sql.append("NVL(X.ORG_ID, Y.ORG_ID) ORG_ID,\n");
		sql.append(" NVL(X.ORG_CODE, Y.ORG_CODE) ORG_CODE,\n");
		sql.append("NVL(X.ORG_NAME, Y.ORG_NAME) ORG_NAME,\n");
		sql.append("NVL(X.REGION_NAME, Y.REGION_NAME) REGION_NAME,\n");
		sql.append("NVL(X.ROOT_DEALER_CODE, Y.ROOT_DEALER_CODE) ROOT_DEALER_CODE,\n");
		sql.append("NVL(X.ROOT_DEALER_NAME, Y.ROOT_DEALER_NAME) ROOT_DEALER_NAME,\n");
		sql.append("NVL(X.DEALER_CODE, Y.DEALER_CODE) DEALER_CODE,\n");
		sql.append("NVL(X.DEALER_NAME, Y.DEALER_NAME) DEALER_NAME,\n");
		sql.append("NVL(X.NODE_BILL_AMOUNT, 0) NODE_BILL_AMOUNT,\n");
		sql.append("NVL(Y.DEING_AMOUNT, 0) DEING_AMOUNT,\n");
		sql.append("NVL(Y.STOCK_AMOUNT, 0) STOCK_AMOUNT,\n");
		sql.append("NVL(NVL(X.NODE_BILL_AMOUNT, 0) + NVL(Y.DEING_AMOUNT, 0) + NVL(Y.STOCK_AMOUNT, 0),0) SUM_TOTAL\n");
		sql.append(" FROM  X FULL OUTER JOIN Y\n");
		sql.append("ON X.DEALER_ID = Y.DEALER_ID\n");
		sql.append("ORDER BY COMPANY_ID,ORG_CODE,REGION_NAME,ROOT_DEALER_CODE,DEALER_CODE\n");
		
		
		
		list = super.pageQuery(sql.toString(), null, getFunName());
		return list;
	}
}
