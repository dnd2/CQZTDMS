package com.infodms.dms.dao.sales.marketmanage.activity;

import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.dao.sales.usermng.UserManageDao;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

public class ActivitySelectDao  extends BaseDao{

	public Logger logger = Logger.getLogger(UserManageDao.class);
	
	private static final ActivitySelectDao dao = new ActivitySelectDao();

	public static final ActivitySelectDao getInstance() {
		return dao;
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		return null;
	}
	/**
	 * 查询项目执行方
	 * @param param
	 * @param pageSize
	 * @param curPage
	 * @return
	 */
	public PageResult<Map<String,Object>> activityMakerQuery(Map<String,String> param,int pageSize,int curPage){
		PageResult<Map<String,Object>> list=null;
		String makerName=param.get("makerName");
		StringBuilder sql= new StringBuilder();
		sql.append("SELECT TM.MAKER_ID,\n" );
		sql.append("       TM.MAKER_CODE,\n" );
		sql.append("       TM.MAKER_NAME,\n" );
		sql.append("       TM.CREATE_DATE,\n" );
		sql.append("       TM.CREATE_BY\n" );
		sql.append("  FROM TM_MARKET_MAKER TM\n" );
		sql.append("  WHERE 1=1\n" );
		if(null!=makerName&&!"".equals(makerName)){
			sql.append(" AND  TM.MAKER_NAME LIKE '"+makerName+"'");
		}
		list=pageQuery(sql.toString(), null, dao.getFunName(), pageSize, curPage);
		return list;
	}
	/**
	 * 市场活动申请查询
	 * @param param
	 * @param pageSize
	 * @param curPage
	 * @return
	 */
	public PageResult<Map<String,Object>> activitySelectQuery(Map<String,String> param,int curPage,int pageSize){
		PageResult<Map<String,Object>> list=null;
		String activityCode = param.get("campaignNo");
		String activityName = param.get("campaignName");
		String activityTheme = param.get("campaignSubject");
		String startDate = param.get("startDate");
		String endDate = param.get("endDate");
		String dutyType=param.get("dutyType");
		String orgId=param.get("orgId");
		String dealerId=param.get("dealerId");
		String status=param.get("status");
		StringBuilder sql= new StringBuilder();
		sql.append("SELECT DISTINCT TPA.ACTIVITY_CODE,\n" );
		sql.append("       TPA.ACTIVITY_NAME,\n" );
		sql.append("       TPA.ACTIVITY_ID,\n" );
		sql.append("       TPA.ACTIVITY_THEME,\n" );
		sql.append("       TO_CHAR(TPA.START_DATE, 'YYYY-MM-DD') START_DATE,\n" );
		sql.append("       TO_CHAR(TPA.END_DATE, 'YYYY-MM-DD') END_DATE,\n" );
		sql.append("       TPA.STATUS\n" );
		sql.append("  FROM TT_PLAN_ACTIVITY TPA,TT_PLAN_ACTIVITY_SUB TPAS\n" );
		sql.append(" WHERE 1 = 1\n");
		sql.append(" AND  TPA.ACTIVITY_ID = TPAS.ACTIVITY_ID\n");
		if(null!=activityCode&&!"".equals(activityCode)){
			sql.append("   AND TPA.ACTIVITY_CODE LIKE '%"+activityCode+"%'\n");
		}
		if(null!=status&&!"".equals(status)){
			sql.append("   AND TPA.STATUS = "+status+"\n");
		}
		if(null!=activityName&&!"".equals(activityName)){
			sql.append("   AND TPA.ACTIVITY_NAME LIKE '%"+activityName+"%'\n");
		}
		if(null!=activityTheme&&!"".equals(activityTheme)){
			sql.append("   AND TPA.ACTIVITY_THEME LIKE '%"+activityTheme+"%'\n");
		}
		 if(!CommonUtils.isNullString(startDate)){
	            sql.append("     AND TRUNC(TPA.START_DATE)>=TO_DATE('"+startDate+"','YYYY-MM-DD')\n" );
	       }
	     if(!CommonUtils.isNullString(endDate)){
	            sql.append("     AND TRUNC(TPA.END_DATE)<=TO_DATE('"+endDate+"','YYYY-MM-DD')\n" );
	     }
	     if(Constant.DUTY_TYPE_LARGEREGION.toString().equals(dutyType)){
	    	sql.append("AND TPAS.DEALER_ID IN");
	 		sql.append("  (SELECT DEALER_ID FROM vw_org_dealer ");
	 		sql.append("WHERE ROOT_ORG_ID="+orgId+")");
	     }else if(Constant.DUTY_TYPE_SMALLREGION.toString().equals(dutyType)){
	    	 sql.append("AND TPAS.DEALER_ID IN");
		 		sql.append("  (SELECT DEALER_ID FROM vw_org_dealer ");
		 		sql.append("WHERE PQ_ORG_ID="+orgId+")");
	     }else if(Constant.DUTY_TYPE_DEALER.toString().endsWith(dutyType)){
	    	 sql.append("AND TPAS.DEALER_ID IN("+dealerId+")");
	     }
	     sql.append(" ORDER BY TPA.ACTIVITY_ID  desc  "); 

		list=pageQuery(sql.toString(), null, dao.getFunName(), pageSize, curPage);
		return list;
	}
	/**
	 * 查询项目执行方
	 * @param param
	 * @param pageSize
	 * @param curPage
	 * @return
	 */
	public List<Map<String,Object>> getActivitySubList(Map<String,String> param){
		List<Map<String,Object>> list=null;
		String activityId=param.get("activityId");
		StringBuilder sql= new StringBuilder();
		sql.append("SELECT TS.SUB_ID,\n" );
		sql.append("       TS.ACTIVITY_ID,\n" );
		sql.append("       TS.DEALER_ID,\n" );
		sql.append("       TD.DEALER_CODE,\n" );
		sql.append("       TD.DEALER_SHORTNAME,\n" );
		sql.append("       TS.FCLIENT,\n" );
		sql.append("       TS.MIXCLIENT,\n" );
		sql.append("       TS.AIMCARD,\n" );
		sql.append("       TS.AIMORDER,\n" );
		sql.append("       TS.ACT_FCLIENT,\n" );
		sql.append("       TS.ACT_MIXCLIENT,\n" );
		sql.append("       TS.ACT_AIMCARD,\n" );
		sql.append("       TS.ACT_AIMORDER,\n" );
		sql.append("       TO_NUMBER(TRIM(TO_CHAR(TS.AIMORDER / TS.ACT_AIMORDER,\n" );
		sql.append("                                       '9999999.99'))) * 100 || '%' ORDERRATE,\n" );
		sql.append("                                         TO_NUMBER(TRIM(TO_CHAR(TS.ACT_AIMCARD / TS.AIMCARD,\n" );
		sql.append("                                       '9999999.99'))) * 100 || '%' CARDRATE,\n" );
		sql.append("                                                  TO_NUMBER(TRIM(TO_CHAR(TS.ACT_MIXCLIENT / TS.MIXCLIENT,\n" );
		sql.append("                                       '9999999.99'))) * 100 || '%' MXRATE,\n" );
		sql.append("                                             TO_NUMBER(TRIM(TO_CHAR(TS.ACT_FCLIENT / TS.FCLIENT,\n" );
		sql.append("                                       '9999999.99'))) * 100 || '%' FRATE\n" );
		sql.append("  FROM TT_PLAN_ACTIVITY_SUB TS, TM_DEALER TD\n" );
		sql.append(" WHERE 1 = 1\n" );
		sql.append("   AND TD.DEALER_ID = TS.DEALER_ID");
		sql.append(" AND TS.ACTIVITY_ID = "+activityId);
		list=dao.pageQuery(sql.toString(), null, dao.getFunName());
		return list;
	}
	
	/**
	 * 查询审核记录
	 * 	  @param param
	 * @param pageSize
	 * @param curPage
	 * @return
	 */
	public List<Map<String,Object>> getAuditList(Map<String,String> param){
		List<Map<String,Object>> list=null;
		String activityId=param.get("activityId");
		StringBuilder sql= new StringBuilder();
		sql.append("SELECT TO_CHAR(TS.CREATE_DATE, 'YYYY-MM-DD') CHECK_DATE,\n" );
		sql.append("       TR.ORG_NAME,\n" );
		sql.append("       TU.NAME USER_NAME,\n" );
		sql.append("       TCD.CODE_DESC CHECK_STATUS,\n" );
		sql.append("       TS.AUDIT_REMARK CHECK_DESC\n" );
		sql.append("  FROM TT_ACTIVITY_LOG TS,\n" );
		sql.append("       TC_USER         TU,\n" );
		sql.append("       TM_COMPANY      TC,\n" );
		sql.append("       TM_ORG          TR,\n" );
		sql.append("       TC_CODE         TCD\n" );
		sql.append(" WHERE TS.CREATE_BY = TU.USER_ID\n" );
		sql.append("   AND TC.COMPANY_ID = TU.COMPANY_ID\n" );
		sql.append("   AND TR.ORG_ID = TC.OEM_COMPANY_ID\n" );
		sql.append("   AND TCD.CODE_ID = TS.AFTER_STATUS");
		sql.append("   AND TS.ACTIVITY_ID = "+activityId);
		sql.append("    ORDER BY TS.CREATE_DATE ");

		list=dao.pageQuery(sql.toString(), null, dao.getFunName());
		return list;
	}
	public List<Map<String,Object>> getActivityList(Map<String,String> map) throws Exception{
		List<Object> params = new LinkedList<Object>();

        String startDate=map.get("startDate");
        String endDate=map.get("endDate");
        String dealerCode=map.get("dealerCode");
        String activityId=map.get("activityId");

        StringBuilder sql= new StringBuilder();
    	sql.append("SELECT\n" );
		sql.append("        TS.TOTAL_FCLIENT,\n" );
		sql.append("        TS.TOTAL_MCLIENT,\n" );
		sql.append("        TS.TOTAL_AIMCARD,\n" );
		sql.append("        TS.TOTAL_AIMORDER,\n" );
		sql.append("        TS.ACT_TOTAL_FCLIENT,\n" );
		sql.append("        TS.ACT_TOTAL_MCLIENT,\n" );
		sql.append("        TS.ACT_TOTAL_AIMCARD,\n" );
		sql.append("        TS.ACT_TOTAL_AIMORDER,\n" );
		sql.append("       TO_NUMBER(TRIM(TO_CHAR(TS.ACT_TOTAL_AIMORDER / TS.TOTAL_AIMORDER,\n" );
		sql.append("                                       '9999999.9999'))) * 100 || '%' ORDERRATE,\n" );
		sql.append("                                         TO_NUMBER(TRIM(TO_CHAR(TS.ACT_TOTAL_AIMCARD/ TS.TOTAL_AIMCARD,\n" );
		sql.append("                                       '9999999.9999'))) * 100 || '%' CARDRATE,\n" );
		sql.append("                                                  TO_NUMBER(TRIM(TO_CHAR(TS.ACT_TOTAL_MCLIENT / TS.TOTAL_MCLIENT,\n" );
		sql.append("                                       '9999999.9999'))) * 100 || '%' MXRATE,\n" );
		sql.append("                                             TO_NUMBER(TRIM(TO_CHAR(TS.ACT_TOTAL_FCLIENT / TS.TOTAL_FCLIENT,\n" );
		sql.append("                                       '9999999.9999'))) * 100 || '%' FRATE\n" );
		sql.append("  FROM TT_PLAN_ACTIVITY TS");
		
		sql.append(" WHERE TS.ACTIVITY_ID = "+activityId);
        if(!CommonUtils.isNullString(startDate)){
            sql.append("     AND TRUNC(TS.START_DATE)>=TO_DATE('"+startDate+"','YYYY-MM-DD')\n" );
        }
        if(!CommonUtils.isNullString(endDate)){
            sql.append("     AND TRUNC(TS.END_DATE)<=TO_DATE('"+endDate+"','YYYY-MM-DD')\n" );
        }
		 sql.append(" ORDER BY TS.ACTIVITY_ID DESC");
		List<Map<String,Object>> rs = dao.pageQuery(sql.toString(), params, getFunName());
		return rs;
	}
	
}
