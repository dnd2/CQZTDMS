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

public class DealerCarStrorgeDao extends BaseDao{
	private ActionContext act = ActionContext.getContext();
	private AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
	public static final Logger logger = Logger.getLogger(DealerCarStrorgeDao.class);
	public static final DealerCarStrorgeDao dao = new DealerCarStrorgeDao();
	public static DealerCarStrorgeDao getInstance(){
		return dao;
		
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	public List<Map<String, Object>> getDealerCarStrorgeSelect(Map<String, String> map){
		List<Map<String, Object>> list = null;
		String areaId = map.get("areaId");
		String orgId = map.get("orgId");
		String dealerId = map.get("dealerId");
		String modelId = map.get("modelId");
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT /*+ all_rows */ vmg.SERIES_CODE,vmg.SERIES_NAME,vmg.SALES_MODEL_GROUP_CODE,vmg.SALES_MODEL_GROUP_NAME,\n");
		sql.append("vmg.MODEL_CODE,vmg.MODEL_NAME,\n");
		
		
		sql.append(" SUM(CASE WHEN months_between(sysdate,v.product_date) <= 3 THEN 1 ELSE 0 END) less_3_month,\n");
		sql.append("SUM(CASE WHEN (months_between(sysdate,v.product_date) >3 \n");
		sql.append("AND months_between(sysdate,v.product_date) <= 6) THEN 1 ELSE 0 END) to_6_month,\n");
		sql.append("SUM(CASE WHEN (months_between(sysdate,v.product_date) >6 \n");
		sql.append("AND months_between(sysdate,v.product_date) <= 9) THEN 1 ELSE 0 END) to_9_month,\n");
		sql.append("SUM(CASE WHEN (months_between(sysdate,v.product_date) >9 \n");
		sql.append("AND months_between(sysdate,v.product_date) <=12) THEN 1 ELSE 0 END) to_12_month,\n");
		sql.append("SUM(CASE WHEN (months_between(sysdate,v.product_date) >12 \n");
		sql.append("AND months_between(sysdate,v.product_date) <=24) THEN 1 ELSE 0 END) to_2_year,\n");
		sql.append("SUM(CASE WHEN months_between(sysdate,v.product_date) >24 THEN 1 ELSE 0 END) more_2_year,\n");
		
		
		
		sql.append(" SUM(CASE WHEN months_between(sysdate,v.product_date) <= 3 THEN 1 ELSE 0 END) +\n");
		sql.append("SUM(CASE WHEN (months_between(sysdate,v.product_date) >3 \n");
		sql.append("AND months_between(sysdate,v.product_date) <= 6) THEN 1 ELSE 0 END) +\n");
		sql.append("SUM(CASE WHEN (months_between(sysdate,v.product_date) >6 \n");
		sql.append("AND months_between(sysdate,v.product_date) <= 9) THEN 1 ELSE 0 END) +\n");
		sql.append("SUM(CASE WHEN (months_between(sysdate,v.product_date) >9 \n");
		sql.append("AND months_between(sysdate,v.product_date) <=12) THEN 1 ELSE 0 END) +\n");
		sql.append("SUM(CASE WHEN (months_between(sysdate,v.product_date) >12 \n");
		sql.append("AND months_between(sysdate,v.product_date) <=24) THEN 1 ELSE 0 END) +\n");
		sql.append("SUM(CASE WHEN months_between(sysdate,v.product_date) >24 THEN 1 ELSE 0 END) SUM_TOTAL\n");
		
		sql.append("FROM TM_VEHICLE V, tm_vhcl_material_group_r gr ,vw_material_group vmg\n");
		sql.append("WHERE V.LIFE_CYCLE = 10321003\n");
		sql.append("AND v.dealer_id >0\n");
		sql.append("AND gr.material_id = v.material_id\n");
		sql.append("AND vmg.PACKAGE_ID = gr.group_id\n");
		/*sql.append("AND GR.GROUP_ID IN\n");
		sql.append("(SELECT T1.GROUP_ID\n");
		sql.append("FROM TM_VHCL_MATERIAL_GROUP T1\n");
		sql.append("WHERE T1.STATUS = 10011001\n");
		sql.append("START WITH T1.GROUP_ID IN\n");
		sql.append("(SELECT TAP.MATERIAL_GROUP_ID\n");
		sql.append(" FROM TM_AREA_GROUP TAP\n");
		sql.append(" WHERE 1=1\n");
		if(Utility.testString(areaId)){
			sql.append("AND  TAP.AREA_ID IN   ("+areaId+")\n");
		}
		sql.append(" )\n");
		sql.append(" CONNECT BY PRIOR T1.GROUP_ID = T1.PARENT_GROUP_ID)\n");*/
		
		if(Utility.testString(areaId)){
			sql.append("AND  v.AREA_ID IN   ("+areaId+")\n");
		}
		
		
		if(Utility.testString(orgId)){
				sql.append("AND EXISTS (SELECT 1 FROM vw_org_dealer VOD \n");
				sql.append(" WHERE v.dealer_id = VOD.dealer_id \n");
				
				sql.append("AND  VOD.ROOT_ORG_ID IN   ("+orgId+")\n");
				
				sql.append(")\n");
		}
		
		if(Utility.testString(dealerId)){
			sql.append("AND v.dealer_id IN   ("+dealerId+")\n");
		}
		if(Utility.testString(modelId)){
			sql.append("and  (vmg.BRAND_ID IN ("+modelId+") OR vmg.SERIES_ID IN ("+modelId+") OR vmg.MODEL_ID IN ("+modelId+") OR vmg.PACKAGE_ID IN ("+modelId+"))\n");
		}
		sql.append("GROUP BY vmg.SERIES_CODE,vmg.SERIES_NAME,vmg.SALES_MODEL_GROUP_CODE,vmg.SALES_MODEL_GROUP_NAME,vmg.MODEL_CODE,vmg.MODEL_NAME\n");
		sql.append("ORDER BY vmg.SERIES_CODE,vmg.SERIES_NAME,vmg.SALES_MODEL_GROUP_CODE,vmg.SALES_MODEL_GROUP_NAME,vmg.MODEL_CODE,vmg.MODEL_NAME\n");
		
		list = super.pageQuery(sql.toString(), null, getFunName());
		return list;
	}
}
