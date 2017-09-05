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

public class ActivitiesPlanSummeryCostApplyDao extends BaseDao{
	public static Logger logger = Logger.getLogger(ActivitiesPlanSummeryCostApplyDao.class);
	private static final ActivitiesPlanSummeryCostApplyDao dao = new ActivitiesPlanSummeryCostApplyDao();
	public static final ActivitiesPlanSummeryCostApplyDao getInstance() {
		return dao;
	}

	public PageResult<Map<String, Object>> activitiesQuery(Map<String,String> map,int curPage,int pageSize) throws Exception{
		List<Object> params = new LinkedList<Object>();
        String dealerId=map.get("dealerId");
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
        sql.append("       TCP.SPACE_ID,\n" );
        sql.append("       TCP.PLAN_ID,\n" );
        sql.append("       TCP.CHECK_STATUS,\n" );
        sql.append("       NVL(TCS.STATUS,0) SUMMERY_STATUS,\n" );
        sql.append("       NVL(TCR.CHECK_STATUS,0) REQ_STATUS\n" );
        sql.append("  FROM TT_CAMPAIGN_PLAN TCP, TT_CAMPAIGN TC,TT_CAMPAIGN_SUMMERY TCS,TT_CAMPAIGN_PLAN_REQ TCR\n" );
        sql.append(" WHERE TCP.CAMPAIGN_ID = TC.CAMPAIGN_ID\n" );
        sql.append(" AND TCP.CAMPAIGN_ID = TCS.CAMPAIGN_ID(+)\n" );
        sql.append("   AND TCP.DEALER_ID = "+dealerId+"\n" );
        sql.append("   AND TCP.CHECK_STATUS NOT IN("+ Constant.CAMPAIGN_CHECK_STATUS_24+")\n");
        sql.append("   AND TCP.REQ_ID=TCR.REQ_ID(+)\n" );

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
    /**
     *
     * @return
     * @throws Exception
     */
	public static List<Map<String, Object>> getCheckRecordList(String planId) throws Exception {
        StringBuilder sql= new StringBuilder();
        sql.append("SELECT B.ORG_NAME,\n" );
        sql.append("       C.NAME,\n" );
        sql.append("       TO_CHAR(A.CHECK_DATE, 'YYYY-MM-DD') CHECK_DATE,\n" );
        sql.append("       A.CHECK_STATUS,\n" );
        sql.append("       A.SUBMIT_COST,\n" );
        sql.append("       A.CHECK_DESC,\n" );
        sql.append("       A.CHECK_ID\n" );
        sql.append("  FROM TT_CAMPAIGN_SUMMERY_CHK A, TM_ORG B, TC_USER C\n" );
        sql.append(" WHERE A.CHECK_ORG_ID = B.ORG_ID\n" );
        sql.append("   AND A.CREATE_BY = C.USER_ID\n");
        sql.append("   AND A.PLAN_ID="+planId+"\n");
        sql.append("   ORDER BY A.CHECK_DATE ASC\n");


		return dao.pageQuery(sql.toString(), null, dao.getFunName());
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}

}
