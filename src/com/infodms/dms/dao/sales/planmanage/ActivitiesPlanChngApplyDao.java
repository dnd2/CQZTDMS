package com.infodms.dms.dao.sales.planmanage;

import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ActivitiesPlanChngApplyDao extends BaseDao{
	public static Logger logger = Logger.getLogger(ActivitiesPlanChngApplyDao.class);
	private static final ActivitiesPlanChngApplyDao dao = new ActivitiesPlanChngApplyDao();
	public static final ActivitiesPlanChngApplyDao getInstance() {
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
        sql.append("       NVL(TCP.CHNG_STATUS,0) REQ_STATUS,\n" );
        sql.append("       NVL(TCS.STATUS,0) SUM_STATUS\n" );
        sql.append("  FROM TT_CAMPAIGN_PLAN TCP, TT_CAMPAIGN TC,TT_CAMPAIGN_SUMMERY TCS\n" );
        sql.append(" WHERE TCP.CAMPAIGN_ID = TC.CAMPAIGN_ID\n" );
        sql.append("   AND TCP.DEALER_ID = "+dealerId+"\n" );
        sql.append("   AND TCP.CHECK_STATUS NOT IN("+ Constant.CAMPAIGN_CHECK_STATUS_24+")\n");
        sql.append("   AND TCP.PLAN_ID = TCS.PLAN_ID(+)\n" );
        sql.append("   AND (TCP.CHNG_STATUS IS NULL OR TCP.CHNG_STATUS IN("+Constant.CAMPAIGN_CHECK_STATUS_16+","+Constant.CAMPAIGN_CHECK_STATUS_19+","+Constant.CAMPAIGN_CHECK_STATUS_21+","+Constant.CAMPAIGN_CHECK_STATUS_22+","+Constant.CAMPAIGN_CHECK_STATUS_23+"))\n" );

        sql.append("--未进行活动总结,活动总结处于被驳回状态下 才可以进行变更申请\n" );
        sql.append("AND (\n" );
        sql.append("    NOT EXISTS(\n" );
        sql.append("      SELECT 1 FROM TT_CAMPAIGN_SUMMERY TCS\n" );
        sql.append("      WHERE TCP.PLAN_ID=TCS.PLAN_ID\n" );
        sql.append("    )\n" );
        sql.append("    OR\n" );
        sql.append("    EXISTS(\n" );
        sql.append("      SELECT 1 FROM TT_CAMPAIGN_SUMMERY TCS2\n" );
        sql.append("      WHERE TCP.PLAN_ID=TCS2.PLAN_ID\n" );
        sql.append("      AND TCS2.STATUS IN(\n" );
        sql.append("            13291001,13291005,13291007\n" );
        sql.append("      )\n" );
        sql.append("    )\n" );
        sql.append(")");

        //变更申请类型为活动取消,并且车厂审核已通过,则不能再次进行变更申请
        sql.append("AND NOT EXISTS(\n" );
        sql.append("    SELECT 1 FROM TT_CAMPAIGN_PLAN_REQ M\n" );
        sql.append("    WHERE TCP.PLAN_ID=M.PLAN_ID\n" );
        sql.append("    AND M.CHNG_TYPE=13271002\n" );
        sql.append("    AND M.CHECK_STATUS=11261022\n" );
        sql.append(")\n" );

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
	public static List<Map<String, Object>> getCheckRecordList(String planId,String reqId) throws Exception {
        StringBuilder sql= new StringBuilder();
        sql.append("SELECT B.ORG_NAME,\n" );
        sql.append("       C.NAME,\n" );
        sql.append("       TO_CHAR(A.CHECK_DATE, 'YYYY-MM-DD') CHECK_DATE,\n" );
        sql.append("       A.CHECK_RESULT,\n" );
        sql.append("       A.CHECK_DESC,\n" );
        sql.append("       A.RECORD_ID\n" );
        sql.append("  FROM TT_CAMPAIGN_CHECK_RECORD A, TM_ORG B, TC_USER C\n" );
        sql.append(" WHERE A.ORG_ID = B.ORG_ID\n" );
        sql.append("   AND A.CREATE_BY = C.USER_ID\n");
        sql.append("   AND A.PLAN_ID="+planId+"\n");
        sql.append("   AND A.REQ_ID="+reqId+"\n");
        sql.append("   ORDER BY A.CHECK_DATE ASC\n");


		return dao.pageQuery(sql.toString(), null, dao.getFunName());
	}
    public void updatePlanChng(Map<String,String> map){
         String reqId=map.get("reqId");
         String subm=map.get("subm");
         String userId=map.get("userId");
         String chngType=map.get("chngType");
         String remark=map.get("remark");


            StringBuilder sql= new StringBuilder();
            sql.append("UPDATE TT_CAMPAIGN_PLAN_REQ REQ\n" );
            sql.append("   SET REQ.UPDATE_BY ="+userId+",\n" );
            sql.append("    REQ.UPDATE_DATE = SYSDATE,\n" );
            if(subm.equals("0")){
                sql.append("    REQ.CHECK_STATUS ="+Constant.CAMPAIGN_CHECK_STATUS_16+",\n" );
            }else {
                sql.append("    REQ.CHECK_STATUS ="+Constant.CAMPAIGN_CHECK_STATUS_17+",\n" );
                sql.append("    REQ.REQ_DATE=SYSDATE,\n" );
            }
            sql.append("    REQ.CHNG_TYPE ="+chngType+",\n" );
            sql.append("    REQ.REMARK ='"+remark+"',\n" );
            sql.append("    REQ.TO_PLACE_COUNT = NULL,\n" );
            sql.append("    REQ.TO_TEL_STORE_COUNT = NULL,\n" );
            sql.append("    REQ.CREATE_CARDS_COUNT = NULL,\n" );
            sql.append("    REQ.ORDER_COUNT = NULL,\n" );
            sql.append("    REQ.TURN_CAR_COUNT = NULL,\n" );
            sql.append("    REQ.PROJECT_NAME = NULL,\n" );
            sql.append("    REQ.PLAN_TYPE = NULL,\n" );
            sql.append("    REQ.COMPANY_COST = NULL,\n" );
            sql.append("    REQ.ALL_COST = NULL,\n" );
            sql.append("    REQ.COST_TYPE = NULL,\n" );
            sql.append("    REQ.EXECUTION_TIME_B = NULL,\n" );
            sql.append("    REQ.EXECUTION_TIME_E = NULL\n" );
            sql.append("WHERE REQ.REQ_ID="+reqId+"");

         dao.update(sql.toString(),null);

    }
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}

}
