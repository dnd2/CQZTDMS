package com.infodms.dms.dao.sales.planmanage;

import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ActivitiesPlanImplementQueryDao extends BaseDao{
	public static Logger logger = Logger.getLogger(ActivitiesPlanImplementQueryDao.class);
	private static final ActivitiesPlanImplementQueryDao dao = new ActivitiesPlanImplementQueryDao();
	public static final ActivitiesPlanImplementQueryDao getInstance() {
		return dao;
	}

	public PageResult<Map<String, Object>> getImplementList(Map<String,String> map,int curPage,int pageSize) throws Exception{
		List<Object> params = new LinkedList<Object>();
        String orgId=map.get("orgId");
        String campaignNo=map.get("campaignNo");
        String campaignName=map.get("campaignName");
        String dutyType=map.get("dutyType");
        String dealerCode=map.get("dealerCode");
        String startDate=map.get("startDate");
        String endDate=map.get("endDate");

        StringBuilder sql= new StringBuilder();
        sql.append("SELECT TC.CAMPAIGN_ID,\n" );
        sql.append("       TC.CAMPAIGN_NO,\n" );
        sql.append("       TC.CAMPAIGN_NAME,\n" );
        sql.append("       NVL(TCPR.CHECK_STATUS, 0) REQ_STATUS,\n" );
        sql.append("       NVL(TCS.STATUS, 0) SUMMERY_STATUS,\n" );
        sql.append("       TD.DEALER_ID,\n" );
        sql.append("       TD.DEALER_CODE,\n" );
        sql.append("       TD.DEALER_NAME,\n" );
        sql.append("       TD.DEALER_SHORTNAME,\n" );
        sql.append("       TCP.PLAN_TYPE,\n" );
        sql.append("       TCP.PROJECT_NAME,\n" );
        sql.append("       TO_CHAR(TCP.EXECUTION_TIME_B, 'YYYY-MM-DD') BEGIN_DATE,\n" );
        sql.append("       TO_CHAR(TCP.EXECUTION_TIME_E, 'YYYY-MM-DD') END_DATE,\n" );
        sql.append("       TCP.ALL_COST,\n" );
        sql.append("       TCP.COMPANY_COST,\n" );
        sql.append("       TCP.COST_TYPE,\n" );
        sql.append("       TCP.TO_PLACE_COUNT,\n" );
        sql.append("       TCP.TO_TEL_STORE_COUNT,\n" );
        sql.append("       TCP.CREATE_CARDS_COUNT,\n" );
        sql.append("       TCP.ORDER_COUNT,\n" );
        sql.append("       TCP.TURN_CAR_COUNT,\n" );
        sql.append("       NVL(TCPR.CHNG_TYPE,0) CHNG_TYPE,\n" );
        sql.append("       NVL(TCS.SUMMERY_COST_TYPE,0) SUMMERY_COST_TYPE,\n" );
        sql.append("       NVL(TO_CHAR(TCPR.REQ_DATE, 'YYYY-MM-DD'),0) REQ_DATE,\n" );
        sql.append("       NVL(TCPR.REQ_NUM,0) REQ_NUM\n" );
        sql.append("  FROM TT_CAMPAIGN_PLAN     TCP,\n" );
        sql.append("       TT_CAMPAIGN_PLAN_REQ TCPR,\n" );
        sql.append("       TT_CAMPAIGN_SUMMERY  TCS,\n" );
        sql.append("       TT_CAMPAIGN          TC,\n" );
        sql.append("       TM_DEALER            TD\n" );
        sql.append(" WHERE TCP.PLAN_ID = TCPR.PLAN_ID(+)\n" );
        sql.append("   AND TCP.PLAN_ID = TCS.PLAN_ID(+)\n" );
        sql.append("   AND TCP.CAMPAIGN_ID = TC.CAMPAIGN_ID\n" );
        sql.append("   AND TCP.DEALER_ID IS NOT NULL\n" );
        sql.append("   AND TCP.CHECK_STATUS = "+Constant.CAMPAIGN_CHECK_STATUS_15+" --小区已下发\n" );
        sql.append("   AND TCP.DEALER_ID = TD.DEALER_ID\n");

        //如果是大区用户 只显示大区下边所有经销商
        if(dutyType.equals(Constant.DUTY_TYPE_LARGEREGION.toString())){
            sql.append("AND TCP.DEALER_ID IN(\n" );
            sql.append("    SELECT VOD.DEALER_ID FROM VW_ORG_DEALER VOD\n" );
            sql.append("    WHERE VOD.ROOT_ORG_ID="+orgId+")\n");
        //如果是小区用户 只显示小区下边所有经销商
        }else if(dutyType.equals(Constant.DUTY_TYPE_SMALLREGION.toString())){
            sql.append("AND TCP.DEALER_ID IN(\n" );
            sql.append("    SELECT TDOR.DEALER_ID FROM TM_DEALER_ORG_RELATION TDOR\n" );
            sql.append("    WHERE TDOR.ORG_ID="+orgId+")\n");
        }
		if(!"".equals(dealerCode)&&dealerCode!=null){
            String arr[]=dealerCode.split(",");
            String str="";

            for(int i=0;i<arr.length;i++){
                str+="'"+arr[i]+"',";
            }
            str=str.substring(0,str.lastIndexOf(","));

			sql.append("AND TD.DEALER_CODE IN("+str+")\n");
		}
		if(!"".equals(campaignNo)&&campaignNo!=null){
			sql.append("AND TC.CAMPAIGN_NO LIKE'%"+campaignNo+"%'\n");
		}
		if(!"".equals(campaignName)&&campaignName!=null){
			sql.append("AND TC.CAMPAIGN_NAME LIKE'%"+campaignName+"%'\n");
		}
		if (!CommonUtils.isNullString(startDate)) {
			sql.append(" AND TRUNC(TCP.EXECUTION_TIME_B) >= TO_DATE('").append(startDate).append("','yyyy-mm-dd')\n");
		}

		if (!CommonUtils.isNullString(endDate)) {
			sql.append(" AND TRUNC(TCP.EXECUTION_TIME_E) <= TO_DATE('").append(endDate).append("','yyyy-mm-dd')\n");
		}
        sql.append("ORDER BY TCP.CREATE_DATE DESC,TD.DEALER_CODE\n");

		PageResult<Map<String, Object>> rs = dao.pageQuery(sql.toString(), params, getFunName(),pageSize, curPage);
		return rs;
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}

}
