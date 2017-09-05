package com.infodms.dms.dao.sales.planmanage;

import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ActivitiesPlanChngProvinceCheckDao extends BaseDao{
	public static Logger logger = Logger.getLogger(ActivitiesPlanChngProvinceCheckDao.class);
	private static final ActivitiesPlanChngProvinceCheckDao dao = new ActivitiesPlanChngProvinceCheckDao();
	public static final ActivitiesPlanChngProvinceCheckDao getInstance() {
		return dao;
	}

	public PageResult<Map<String, Object>> activitiesChngQuery(Map<String,String> map,int curPage,int pageSize) throws Exception{
		List<Object> params = new LinkedList<Object>();
        String orgId=map.get("orgId");
        String campaignNo=map.get("campaignNo");
        String campaignName=map.get("campaignName");
        String campaignSubject=map.get("campaignSubject");
        String startDate=map.get("startDate");
        String endDate=map.get("endDate");

        StringBuilder sql= new StringBuilder();
        sql.append("SELECT TC.CAMPAIGN_ID,\n" );
        sql.append("       TC.CAMPAIGN_NO,\n" );
        sql.append("       TC.CAMPAIGN_NAME,\n" );
        sql.append("       TC.CAMPAIGN_SUBJECT,\n" );
        sql.append("       TO_CHAR(TC.START_DATE, 'YYYY-MM-DD') START_DATE,\n" );
        sql.append("       TO_CHAR(TC.END_DATE, 'YYYY-MM-DD') END_DATE,\n" );
        sql.append("       TCPR.SPACE_ID,\n" );
        sql.append("       TCPR.PLAN_ID,\n" );
        sql.append("       TCPR.CHECK_STATUS,\n" );
        sql.append("       TCPR.CHNG_TYPE,\n" );
        sql.append("       TD.DEALER_NAME,\n" );
        sql.append("       TCPR.REQ_ID,\n" );
        sql.append("       TO_CHAR(TCPR.REQ_DATE, 'YYYY-MM-DD') REQ_DATE,\n" );
        sql.append("       TCPR.REQ_NUM\n" );
        sql.append("FROM tt_campaign_plan_req TCPR,TT_CAMPAIGN TC,TM_DEALER TD\n" );
        sql.append("WHERE TCPR.CAMPAIGN_ID=TC.CAMPAIGN_ID\n" );
        sql.append("AND TCPR.DEALER_ID=TD.DEALER_ID\n" );
        sql.append("AND TCPR.DEALER_ID IN(\n" );
        sql.append("    SELECT TDOR.DEALER_ID FROM TM_DEALER_ORG_RELATION TDOR\n" );
        sql.append("    WHERE TDOR.ORG_ID="+orgId+")\n");
        sql.append("AND TCPR.CHECK_STATUS IN("+ Constant.CAMPAIGN_CHECK_STATUS_17+",")
                .append(Constant.CAMPAIGN_CHECK_STATUS_18+",")
                .append(Constant.CAMPAIGN_CHECK_STATUS_19+",")
                .append(Constant.CAMPAIGN_CHECK_STATUS_20+",")
                .append(Constant.CAMPAIGN_CHECK_STATUS_21+",")
                .append(Constant.CAMPAIGN_CHECK_STATUS_22+",")
                .append(Constant.CAMPAIGN_CHECK_STATUS_23+")\n");


		if(!"".equals(campaignNo)&&campaignNo!=null){
			sql.append("AND TC.CAMPAIGN_NO LIKE'%"+campaignNo+"%'\n");
		}
		if(!"".equals(campaignName)&&campaignName!=null){
			sql.append("AND TC.CAMPAIGN_NAME LIKE'%"+campaignName+"%'\n");
		}
		if(!"".equals(campaignSubject)&&campaignSubject!=null){
			sql.append("AND TC.CAMPAIGN_SUBJECT LIKE'%"+campaignSubject+"%'\n");
		}
		if(!"".equals(startDate)&&startDate!=null){
			sql.append("AND TRUNC(TC.START_DATE)>=TO_DATE('"+startDate+"','YYYY-MM-DD')\n");
		}
		if(!"".equals(endDate)&&endDate!=null){
			sql.append("AND TRUNC(TC.END_DATE)<=TO_DATE('"+endDate+"','YYYY-MM-DD')\n");
		}
        sql.append("ORDER BY TCPR.CREATE_DATE DESC\n");
		PageResult<Map<String, Object>> rs = dao.pageQuery(sql.toString(), params, getFunName(),pageSize, curPage);
		return rs;
	}
	/**
	 * Function         : 方案车型查询
	 * @param           : 方案ID
	 * @return          : 方案信息
	 * @throws          : Exception
	 * LastUpdate       : 2010-07-05
	 */
	public String getModelName(String campaignId) throws Exception{
		String modelName ="";
		StringBuffer sql= new StringBuffer();
		sql.append("select tvmg.group_name\n" );
		sql.append("  from tt_campaign_group_r tcgr, tm_vhcl_material_group tvmg\n" );
		sql.append(" where tcgr.campaign_model = tvmg.group_id\n" );
		sql.append("   and tcgr.campaign_id ="+campaignId+"\n");
		List<Map<String, Object>> rs = dao.pageQuery(sql.toString(), null, getFunName());
		if(rs.size()>0&&rs!=null){
			for(int i=0;i<rs.size();i++){
				Map<String,Object> map = rs.get(i);
				if("".endsWith(modelName)){
					modelName= map.get("GROUP_NAME").toString();
				}else{
					modelName= map.get("GROUP_NAME").toString()+","+modelName;
				}
			}
		}
		return modelName;
	}

    /**
     *
     * @return
     * @throws Exception
     */
	public static List<Map<String, Object>> getDealerPlanList(String planId) throws Exception {
        StringBuilder sql= new StringBuilder();
        sql.append("SELECT TD.DEALER_ID,\n" );
        sql.append("       TD.DEALER_CODE,\n" );
        sql.append("       TD.DEALER_SHORTNAME,\n" );
        sql.append("       TCP.PLAN_TYPE,\n" );
        sql.append("       TCP.PROJECT_NAME,\n" );
        sql.append("       TO_CHAR(TCP.EXECUTION_TIME_B, 'yyyy-mm-dd') EXECUTION_TIME_B,\n" );
        sql.append("       TO_CHAR(TCP.EXECUTION_TIME_E, 'yyyy-mm-dd') EXECUTION_TIME_E,\n" );
        sql.append("       TCP.ALL_COST,\n" );
        sql.append("       TCP.COMPANY_COST,\n" );
        sql.append("       TCP.COST_TYPE,\n" );
        sql.append("       TCP.TO_TEL_STORE_COUNT,\n" );
        sql.append("       TCP.CREATE_CARDS_COUNT,\n" );
        sql.append("       TCP.ORDER_COUNT,\n" );
        sql.append("       TCP.TURN_CAR_COUNT\n" );
        sql.append("  FROM TT_CAMPAIGN_PLAN TCP, TM_DEALER TD\n" );
        sql.append(" WHERE TCP.DEALER_ID = TD.DEALER_ID\n" );
        sql.append("   AND TCP.PAR_PLAN_ID = "+planId+"\n");



		return dao.pageQuery(sql.toString(), null, dao.getFunName());
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}

}
