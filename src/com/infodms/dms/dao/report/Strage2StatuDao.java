package com.infodms.dms.dao.report;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

import net.sf.ehcache.constructs.asynchronous.Command;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.po3.bean.PO;

public class Strage2StatuDao extends BaseDao{
	private ActionContext act = ActionContext.getContext();
	private AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
	public static final Logger logger = Logger.getLogger(Strage2StatuDao.class);
	public static final Strage2StatuDao dao = new Strage2StatuDao();
	public static Strage2StatuDao getInstance(){
		return dao;
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	public List<Map<String, Object>> getStrage2StatuSelect(Map<String, String> map){
		List<Map<String, Object>> list = null;
		String areaId = map.get("areaId");
		String dealerId = map.get("dealerId");
		String orgId = map.get("orgId");
		String modelId = map.get("modelId");
		String orgCode = map.get("orgCode");
		StringBuffer sql = new StringBuffer();
		
		sql.append("WITH X AS (SELECT /*+ all_rows */ VG.COMPANY_ID,\n");
		sql.append("VG.SERIES_ID,\n");
		sql.append("VG.SERIES_CODE,\n");
		sql.append("VG.SERIES_NAME,\n");
		sql.append("VG.SALES_MODEL_GROUP_ID,\n");
		sql.append("VG.SALES_MODEL_GROUP_CODE,\n");
		sql.append("VG.SALES_MODEL_GROUP_NAME,\n");
		sql.append("VG.MODEL_ID,\n");
		sql.append("VG.MODEL_CODE,\n");
		sql.append("VG.MODEL_NAME,\n");
		/*sql.append("VG.PACKAGE_ID,\n");
		sql.append("VG.PACKAGE_CODE,\n");
		sql.append("VG.PACKAGE_NAME,\n");*/
		sql.append("(SUM(NVL(DT.DELIVERY_AMOUNT, 0)) - SUM(NVL(DT.OUT_AMOUNT, 0))) AS NODE_BILL_AMOUNT\n");
		sql.append("FROM TT_VS_DLVRY D,\n");
		sql.append(" TT_VS_DLVRY_DTL DT,\n");
		sql.append("VW_MATERIAL_GROUP VG,\n");
		sql.append(" TM_VHCL_MATERIAL_GROUP_R GR\n");
		sql.append("WHERE D.DELIVERY_ID = DT.DELIVERY_ID\n");
		sql.append("AND D.DELIVERY_STATUS IN (10281004)\n");
		sql.append("AND GR.GROUP_ID = VG.PACKAGE_ID\n");
		sql.append("AND GR.MATERIAL_ID = DT.MATERIAL_ID\n");
		/*sql.append("AND GR.GROUP_ID IN\n");
		sql.append("(SELECT T1.GROUP_ID\n");
		sql.append(" FROM TM_VHCL_MATERIAL_GROUP T1\n");
		sql.append("WHERE T1.STATUS = 10011001\n");
		sql.append(" START WITH T1.GROUP_ID IN\n");
		sql.append("(SELECT TAP.MATERIAL_GROUP_ID\n");
		sql.append("FROM TM_AREA_GROUP TAP\n");
		sql.append("WHERE 1=1 \n");
		if(Utility.testString(areaId)){
			sql.append("AND TAP.AREA_ID IN    ("+areaId+")\n");
		}
		sql.append(")\n");
		sql.append("CONNECT BY PRIOR T1.GROUP_ID = T1.PARENT_GROUP_ID)\n");*/
		if(Utility.testString(areaId)){
			sql.append("AND exists (select 1 from tt_vs_order tvo where tvo.order_id = d.order_id and tvo.area_id in ("+areaId+"))\n");
		}
		if(Utility.testString(dealerId)){
			sql.append("AND EXISTS (SELECT 1 FROM VW_ORG_DEALER VOD ,TT_VS_ORDER O   WHERE O.ORDER_ORG_ID = VOD.DEALER_ID   AND O.ORDER_ID = D.ORDER_ID   AND VOD.DEALER_ID IN ("+dealerId+"))\n");
		}
		if(Utility.testString(orgId)){
			sql.append("AND EXISTS (SELECT 1 FROM VW_ORG_DEALER VOD ,TT_VS_ORDER O   WHERE O.ORDER_ORG_ID = VOD.DEALER_ID   AND O.ORDER_ID = D.ORDER_ID   AND VOD.root_org_id IN ("+orgId+"))\n");
		}
		if(Utility.testString(modelId)){
			sql.append("AND (VG.BRAND_ID IN ("+modelId+") OR VG.SERIES_ID IN ("+modelId+") OR VG.MODEL_ID IN ("+modelId+") OR VG.PACKAGE_ID IN ("+modelId+"))\n");
		}
		sql.append("GROUP BY VG.COMPANY_ID,\n");
		sql.append("VG.SERIES_ID,\n");
		sql.append("VG.SERIES_CODE,\n");
		sql.append(" VG.SERIES_NAME,\n");
		sql.append("VG.SALES_MODEL_GROUP_ID,\n");
		sql.append(" VG.SALES_MODEL_GROUP_CODE,\n");
		sql.append("VG.SALES_MODEL_GROUP_NAME,\n");
		sql.append("VG.MODEL_ID,\n");
		sql.append("VG.MODEL_CODE,\n");
		sql.append("VG.MODEL_NAME),\n");
		/*sql.append(" VG.PACKAGE_ID,\n");
		sql.append("VG.PACKAGE_CODE,\n");
		sql.append("VG.PACKAGE_NAME),\n");*/
		sql.append("Y AS (SELECT /*+ all_rows */ VG.COMPANY_ID,\n");
		sql.append("VG.SERIES_ID,\n");
		sql.append("VG.SERIES_CODE,\n");
		sql.append("VG.SERIES_NAME,\n");
		sql.append("VG.SALES_MODEL_GROUP_ID,\n");
		sql.append("VG.SALES_MODEL_GROUP_CODE,\n");
		sql.append("VG.SALES_MODEL_GROUP_NAME,\n");
		sql.append("VG.MODEL_ID,\n");
		sql.append("VG.MODEL_CODE,\n");
		sql.append("VG.MODEL_NAME,\n");
		/*sql.append("VG.PACKAGE_ID,\n");
		sql.append("VG.PACKAGE_CODE,\n");
		sql.append("VG.PACKAGE_NAME,\n");*/
		sql.append("SUM(CASE WHEN LIFE_CYCLE = 10321005 THEN  1 ELSE 0 END) DEING_AMOUNT, --在途\n");
		sql.append("SUM(CASE  WHEN LIFE_CYCLE = 10321003 THEN 1 ELSE 0 END) STOCK_AMOUNT --在库\n");
		sql.append("FROM TM_VEHICLE V, VW_MATERIAL_GROUP VG, TM_VHCL_MATERIAL_GROUP_R GR\n");
		sql.append("WHERE V.MATERIAL_ID = GR.MATERIAL_ID\n");
		sql.append("AND VG.PACKAGE_ID = GR.GROUP_ID\n");
		
		sql.append("and v.life_cycle in (10321005, 10321003)\n") ;
		
		if(!CommonUtils.isNullString(areaId))
			sql.append("and v.area_id in (").append(areaId).append(")\n") ;
		
		if(Utility.testString(dealerId)){
			sql.append("AND v.dealer_id IN    ("+dealerId+")\n");
		}
		if(Utility.testString(orgId)){
				sql.append("AND EXISTS  (SELECT 1 FROM vw_org_dealer VOD  WHERE v.dealer_id = VOD.dealer_id  AND VOD.ROOT_ORG_ID IN ("+orgId+"))\n");
		}
		if(Utility.testString(modelId)){
			sql.append("AND (VG.BRAND_ID IN ("+modelId+") OR VG.SERIES_ID IN ("+modelId+") OR VG.MODEL_ID IN ("+modelId+") OR VG.PACKAGE_ID IN ("+modelId+"))\n");
		}
		sql.append(" AND V.DEALER_ID > 0\n");
		sql.append("GROUP BY VG.COMPANY_ID,\n");
		sql.append("VG.SERIES_ID,\n");
		sql.append("VG.SERIES_CODE,\n");
		sql.append("VG.SERIES_NAME,\n");
		sql.append("VG.SALES_MODEL_GROUP_ID,\n");
		sql.append("VG.SALES_MODEL_GROUP_CODE,\n");
		sql.append("VG.SALES_MODEL_GROUP_NAME,\n");
		sql.append("VG.MODEL_ID,\n");
		sql.append("VG.MODEL_CODE,\n");
		sql.append("VG.MODEL_NAME)\n");
		/*sql.append("VG.PACKAGE_ID,\n");
		sql.append("VG.PACKAGE_CODE,\n");
		sql.append("VG.PACKAGE_NAME)\n");*/
		sql.append("SELECT NVL (X.COMPANY_ID, Y.COMPANY_ID) COMPANY_ID, NVL(X.SERIES_ID, Y.SERIES_ID) SERIES_ID, NVL(X.SERIES_CODE, Y.SERIES_CODE) SERIES_CODE, NVL(X.SERIES_NAME, Y.SERIES_NAME) SERIES_NAME, NVL(X.SALES_MODEL_GROUP_ID, Y.SALES_MODEL_GROUP_ID) SALES_MODEL_GROUP_ID, NVL(X.SALES_MODEL_GROUP_CODE, Y.SALES_MODEL_GROUP_CODE) SALES_MODEL_GROUP_CODE, NVL(X.SALES_MODEL_GROUP_NAME, Y.SALES_MODEL_GROUP_NAME) SALES_MODEL_GROUP_NAME, NVL(X.MODEL_ID, Y.MODEL_ID) MODEL_ID, NVL(X.MODEL_CODE, Y.MODEL_CODE) MODEL_CODE, NVL(X.MODEL_NAME, Y.MODEL_NAME) MODEL_NAME,  NVL(X.NODE_BILL_AMOUNT, 0) NODE_BILL_AMOUNT, NVL(Y.DEING_AMOUNT, 0) DEING_AMOUNT, NVL(Y.STOCK_AMOUNT, 0) STOCK_AMOUNT, NVL(X.NODE_BILL_AMOUNT, 0) + NVL(Y.DEING_AMOUNT, 0) + NVL(Y.STOCK_AMOUNT, 0) SUM_TOTAL\n");
		sql.append("FROM X\n");
		sql.append("  FULL OUTER JOIN Y\n");
		sql.append("ON X.MODEL_ID = Y.MODEL_ID\n");
		sql.append(" ORDER BY COMPANY_ID, SERIES_CODE, SALES_MODEL_GROUP_CODE, MODEL_CODE\n");
		
		list = super.pageQuery(sql.toString(), null, getFunName());
		return list;
	}
}
