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

public class DealerStrorgeDao extends BaseDao{
	private ActionContext act = ActionContext.getContext();
	private AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
	public static final Logger logger = Logger.getLogger(DealerStrorgeDao.class);
	public static final DealerStrorgeDao dao = new DealerStrorgeDao();
	public static DealerStrorgeDao getInstance(){
		return dao;
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	public List<Map<String, Object>> getDealerStrorgeSelect(Map<String, String> map){
		List<Map<String, Object>> list = null;
		String areaId = map.get("areaId");
		String dealerId = map.get("dealerId");
		String orgId = map.get("orgId");
		String modelId = map.get("modelId");
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT /*+ all_rows */ vod.ROOT_ORG_CODE,NVL(vod.ROOT_ORG_NAME,0) ROOT_ORG_NAME,NVL(vod.REGION_NAME,0) REGION_NAME,vod.ROOT_DEALER_CODE,\n");
		sql.append("vod.ROOT_DEALER_NAME,vod.DEALER_CODE,vod.DEALER_NAME,\n");
		sql.append(" SUM(CASE WHEN months_between(sysdate,v.product_date) <= 3 THEN 1 ELSE 0 END) less_3_month,\n");
		sql.append("SUM(CASE WHEN (months_between(sysdate,v.product_date) >3 \n");
		sql.append("AND months_between(sysdate,v.product_date) <= 6) THEN 1 ELSE 0 END) to_6_month,\n");
		sql.append("SUM(CASE WHEN (months_between(sysdate,v.product_date) >6 \n");
		sql.append(" AND months_between(sysdate,v.product_date) <= 9) THEN 1 ELSE 0 END) to_9_month,\n");
		sql.append("SUM(CASE WHEN (months_between(sysdate,v.product_date) >9 \n");
		sql.append("AND months_between(sysdate,v.product_date) <=12) THEN 1 ELSE 0 END) to_12_month,\n");
		sql.append("SUM(CASE WHEN (months_between(sysdate,v.product_date) >12 \n");
		sql.append("AND months_between(sysdate,v.product_date) <=24) THEN 1 ELSE 0 END) to_2_year,\n");
		sql.append("SUM(CASE WHEN months_between(sysdate,v.product_date) >24 THEN 1 ELSE 0 END) more_2_year,\n");
		
		
		sql.append("SUM(CASE WHEN months_between(sysdate,v.product_date) <= 3 THEN 1 ELSE 0 END) +\n");
		sql.append("SUM(CASE WHEN (months_between(sysdate,v.product_date) >3 \n");
		sql.append("AND months_between(sysdate,v.product_date) <= 6) THEN 1 ELSE 0 END) +\n");
		sql.append("SUM(CASE WHEN (months_between(sysdate,v.product_date) >6 \n");
		sql.append(" AND months_between(sysdate,v.product_date) <= 9) THEN 1 ELSE 0 END) +\n");
		sql.append("SUM(CASE WHEN (months_between(sysdate,v.product_date) >9 \n");
		sql.append("AND months_between(sysdate,v.product_date) <=12) THEN 1 ELSE 0 END)+\n");
		sql.append("SUM(CASE WHEN (months_between(sysdate,v.product_date) >12\n");
		sql.append("AND months_between(sysdate,v.product_date) <=24) THEN 1 ELSE 0 END) +\n");
		sql.append("SUM(CASE WHEN months_between(sysdate,v.product_date) >24 THEN 1 ELSE 0 END) SUM_TOTAL\n");
		
		
		sql.append("FROM TM_VEHICLE V, VW_ORG_DEALER VOD\n");
		sql.append("WHERE V.LIFE_CYCLE = 10321003\n");
		sql.append("AND VOD.DEALER_ID = V.DEALER_ID \n");
		if(Utility.testString(dealerId)){
			sql.append("AND vod.root_dealer_id IN    ("+dealerId+")\n");
		}
		if(Utility.testString(orgId)){
			sql.append("AND  VOD.ROOT_ORG_ID IN   ("+orgId+")\n");
		}
		
		if(Utility.testString(areaId)){
			sql.append("AND V.AREA_ID IN    ("+areaId+")\n");
		}
		
		sql.append("AND EXISTS (SELECT 1 FROM VW_MATERIAL_GROUP VW, TM_VHCL_MATERIAL_GROUP_R GR \n");
		sql.append("WHERE VW.PACKAGE_ID = GR.GROUP_ID \n");
		sql.append("AND GR.MATERIAL_ID = V.MATERIAL_ID\n");
		if(Utility.testString(modelId)){
			sql.append("AND (VW.BRAND_ID IN ("+modelId+") OR VW.SERIES_ID IN ("+modelId+") OR VW.MODEL_ID IN ("+modelId+") OR VW.PACKAGE_ID IN ("+modelId+"))\n");
		}
		sql.append("  )\n");
		sql.append("GROUP BY vod.ROOT_ORG_CODE,vod.ROOT_ORG_NAME,vod.REGION_NAME,vod.ROOT_DEALER_CODE,\n");
		sql.append("vod.ROOT_DEALER_NAME,vod.DEALER_CODE,vod.DEALER_NAME\n");
		sql.append("ORDER BY vod.ROOT_ORG_CODE,vod.REGION_NAME,vod.ROOT_DEALER_CODE\n");
		
		
		list = super.pageQuery(sql.toString(), null, getFunName());
		return list;
	}
}
