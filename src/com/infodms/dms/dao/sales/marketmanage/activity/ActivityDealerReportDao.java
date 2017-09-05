package com.infodms.dms.dao.sales.marketmanage.activity;

import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.po3.bean.PO;

import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ActivityDealerReportDao extends BaseDao{
	public static Logger logger = Logger.getLogger(ActivityDealerReportDao.class);
	private static final ActivityDealerReportDao dao = new ActivityDealerReportDao();
	public static final ActivityDealerReportDao getInstance() {
		return dao;
	}

	public List<Map<String,Object>> getDealerActivityList(Map<String,String> map) throws Exception{
		List<Object> params = new LinkedList<Object>();

        String startDate=map.get("startDate");
        String endDate=map.get("endDate");
        String dealerCode=map.get("dealerCode");

        StringBuilder sql= new StringBuilder();
        sql.append("SELECT DISTINCT TPA.ACTIVITY_ID,\n" );
        sql.append("                TD.ROOT_ORG_NAME,\n" );
        sql.append("                TD.DEALER_SHORTNAME,\n" );
        sql.append("                TD.PQ_ORG_NAME,\n" );
        sql.append("                TPA.START_DATE,\n" );
        sql.append("                TPA.CHARGE_MAN,\n" );
        sql.append("   				TC.CODE_DESC ACTIVITY_TYPE,");
        sql.append("                TPA.ACTIVITY_MONTH,\n" );
        sql.append("                TPA.END_DATE,\n" );
        sql.append("                TPA.ACTIVITY_NAME,\n" );
        sql.append("                TPA.ACTIVITY_THEME,\n" );
        sql.append("                TPA.TOTAL_FCLIENT,\n" );
        sql.append("                TPA.ACT_TOTAL_FCLIENT,\n" );
        sql.append("                TPA.TOTAL_MCLIENT,\n" );
        sql.append("                TPA.ACT_TOTAL_MCLIENT,\n" );
        sql.append("                TPA.TOTAL_AIMCARD,\n" );
        sql.append("                TPA.ACT_TOTAL_AIMCARD,\n" );
        sql.append("                TPA.TOTAL_AIMORDER,\n" );
        sql.append("                TPA.ACT_TOTAL_AIMORDER,\n" );
        sql.append("                TPA.ADDRESS,\n" );
        sql.append("                TPA.CHARGE_MAN\n" );
        sql.append("  FROM TT_dealer_ACTIVITY TPA, VW_ORG_DEALER TD,TC_CODE TC \n" );
        sql.append(" WHERE 1 = 1\n" );
        sql.append("   AND TPA.DEALER_ID = TD.DEALER_ID");
        sql.append("   AND TPA.ACTIVITY_TYPE = TC.CODE_ID");
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
