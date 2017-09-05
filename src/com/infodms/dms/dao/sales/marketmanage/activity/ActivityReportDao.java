package com.infodms.dms.dao.sales.marketmanage.activity;

import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.po3.bean.PO;

import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ActivityReportDao extends BaseDao{
	public static Logger logger = Logger.getLogger(ActivityReportDao.class);
	private static final ActivityReportDao dao = new ActivityReportDao();
	public static final ActivityReportDao getInstance() {
		return dao;
	}

	public List<Map<String,Object>> getActivityList(Map<String,String> map) throws Exception{
		List<Object> params = new LinkedList<Object>();

        String startDate=map.get("startDate");
        String endDate=map.get("endDate");
        String dealerCode=map.get("dealerCode");
        String status=map.get("status");
        

        StringBuilder sql= new StringBuilder();
        sql.append("SELECT DISTINCT TPA.ACTIVITY_ID,\n" );
        sql.append("                TR.ORG_NAME,\n" );
        sql.append("                TD.DEALER_SHORTNAME,\n" );
        sql.append("                TD.PQ_ORG_NAME,\n" );
        sql.append("                TPA.START_DATE,\n" );
        sql.append("                TPA.CHARGE_MAN,\n" );
        sql.append("                TPA.ACTIVITY_MONTH,\n" );
        sql.append("                TPA.END_DATE,\n" );
        sql.append("                TPA.ACTIVITY_NAME,\n" );
        sql.append("                TPA.ACTIVITY_THEME,\n" );
        sql.append("                TVMG.GROUP_NAME,\n" );
        sql.append("                TPA.TOTAL_PRE,\n" );
        sql.append("                TPA.TOTAL_SUPPORT,\n" );
        sql.append("                TPAS.FCLIENT,\n" );
        sql.append("                NVL(TPAS.ACT_FCLIENT,'')ACT_AIMCARD,\n" );
        sql.append("                TO_NUMBER(TRIM(TO_CHAR(TPAS.ACT_FCLIENT / TPAS.FCLIENT,\n" );
        sql.append("                                       '9999999.99'))) * 100 || '%' FRATE,\n" );
        sql.append("                TO_NUMBER(TRIM(TO_CHAR(TPA.TOTAL_PRE / TPAS.ACT_FCLIENT,\n" );
        sql.append("                                       '9999999.99'))) FCOST,\n" );
        sql.append("\n" );
        sql.append("                TPAS.MIXCLIENT,\n" );
        sql.append("                NVL(TPAS.ACT_MIXCLIENT,'') ACT_MIXCLIENT,\n" );
        sql.append("                TO_NUMBER(TRIM(TO_CHAR(TPAS.ACT_MIXCLIENT / TPAS.MIXCLIENT,\n" );
        sql.append("                                       '9999999.99'))) * 100 || '%' MXRATE,\n" );
        sql.append("                TO_NUMBER(TRIM(TO_CHAR(TPA.TOTAL_PRE / TPAS.ACT_MIXCLIENT,\n" );
        sql.append("                                       '9999999.99'))) MXCOST,\n" );
        sql.append("                TPAS.AIMCARD,\n" );
        sql.append("                NVL(TPAS.ACT_AIMCARD,'')ACT_AIMCARD,\n" );
        sql.append("                TO_NUMBER(TRIM(TO_CHAR(TPAS.ACT_AIMCARD / TPAS.AIMCARD,\n" );
        sql.append("                                       '9999999.99'))) * 100 || '%' CARDRATE,\n" );
        sql.append("                TO_NUMBER(TRIM(TO_CHAR(TPA.TOTAL_PRE / TPAS.ACT_AIMCARD,\n" );
        sql.append("                                       '9999999.99'))) CARDCOST,\n" );
        sql.append("\n" );
        sql.append("                TPAS.AIMORDER,\n" );
        sql.append("                TPAS.ACT_AIMORDER,\n" );
        sql.append("\n" );
        sql.append("                TO_NUMBER(TRIM(TO_CHAR(TPAS.AIMORDER / TPAS.ACT_AIMORDER,\n" );
        sql.append("                                       '9999999.99'))) * 100 || '%' ORDERRATE,\n" );
        sql.append("                TO_NUMBER(TRIM(TO_CHAR(TPA.TOTAL_PRE / TPAS.ACT_AIMORDER,\n" );
        sql.append("                                       '9999999.99'))) ORDERCOST,\n" );
        sql.append("\n" );
        sql.append("                TPA.REASON_ANALY,\n" );
        sql.append("                TPA.SUGGEST_TIP\n" );
        sql.append("  FROM TT_PLAN_ACTIVITY       TPA,\n" );
        sql.append("       TM_ORG                 TR,\n" );
        sql.append("       VW_ORG_DEALER          TD,\n" );
        sql.append("       TT_PLAN_ACTIVITY_SUB   TPAS,\n" );
        sql.append("       TT_ACTIVITY_CAR        TAC,\n" );
        sql.append("       TM_VHCL_MATERIAL_GROUP TVMG\n" );
        sql.append(" WHERE TPA.ROOT_ORG_ID = TR.ORG_ID\n" );
        sql.append("   AND TPAS.ACTIVITY_ID = TPA.ACTIVITY_ID\n" );
        sql.append("   AND TPAS.DEALER_ID = TD.DEALER_ID\n" );
        sql.append("   AND TAC.CAMPAIGN_ID = TPA.ACTIVITY_ID\n" );
        sql.append("   AND TVMG.GROUP_ID = TAC.CAMPAIGN_MODEL\n" );
        if(!CommonUtils.isNullString(status)){
            sql.append("     AND TPA.STATUS="+status+"\n" );
        }
        if(!CommonUtils.isNullString(startDate)){
            sql.append("     AND TRUNC(TPA.START_DATE)>=TO_DATE('"+startDate+"','YYYY-MM-DD')\n" );
        }
        if(!CommonUtils.isNullString(endDate)){
            sql.append("     AND TRUNC(TPA.END_DATE)<=TO_DATE('"+endDate+"','YYYY-MM-DD')\n" );
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
		 sql.append(" ORDER BY TPA.ACTIVITY_ID");
		List<Map<String,Object>> rs = dao.pageQuery(sql.toString(), params, getFunName());
		return rs;
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}

}
