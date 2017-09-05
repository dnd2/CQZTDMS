package com.infodms.dms.dao.sales.customerInfoManage;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

public class SalesDailyReportDAO extends BaseDao{
	
	public Logger logger = Logger.getLogger(SalesDailyReportDAO.class);
	private ActionContext act = ActionContext.getContext();
	private static final SalesDailyReportDAO dao = new SalesDailyReportDAO();
	public static final SalesDailyReportDAO getInstance() {
		return dao;
	}

	
	
	public PageResult<Map<String,Object>> seachDaily(String dealerCodes,String startDate,String endDate,int pageSize,
			int curPage,String status,Long orgId,Long dailyAudit){ 
		
		StringBuffer sql=new StringBuffer();
		sql.append("SELECT DISTINCT DEA.DEALER_ID,DEA.DEALER_CODE,DEA.DEALER_NAME,DR.STATUS,DR.CONTENT_ID,TO_CHAR(DR.CREATE_DATE,'yyyy-MM-dd') CREATE_DATE\n");
		sql.append("FROM TT_DAILY_REPORT DR,TM_DEALER DEA,TM_ORG TOR,vw_org_dealer TOR\n");
		sql.append("WHERE 1=1\n");//INSERT_TIME <=TO_DATE('"+date+" 23:59:59','yyyy-MM-dd HH24:MI:SS')\n");
		sql.append("AND DEA.DEALER_ID=DR.DLR_ID\n");
		sql.append("AND TOR.ROOT_DEALER_NAME=DEA.DEALER_NAME");
		if(!"0".equals(dailyAudit.toString())){
			sql.append("  AND DR.STATUS="+dailyAudit);
		}else{
			sql.append("  AND DR.STATUS IN( '"+Constant.DAILY_STATUS_CONFIRM+"','"+Constant.DAILY_STATUS_UNCONFIRM+"')");
		}
		
		if(!"".equals(dealerCodes)&&dealerCodes!=null){
			sql.append("  AND DEA.DEALER_CODE IN('"+dealerCodes+"')");
		}
		if(null!=startDate&&!"".equals(startDate)){
			sql.append("  AND DR.CREATE_DATE>=TO_DATE('"+startDate+"','YYYY-MM-DD')");
		}
		if(null!=endDate&&!"".equals(endDate)){
			sql.append("  AND DR.CREATE_DATE<=TO_DATE('"+endDate+"','YYYY-MM-DD')");
		}
		/*if(!status.equals(Constant.DUTY_TYPE_DEALER.toString())&&!status.equals(Constant.DUTY_TYPE_SMALLREGION.toString())){
			sql.append("  AND DR.STATUS="+Constant.FORECAST_STATUS_CONFIRM);
		}*/
		if(status.equals(Constant.DUTY_TYPE_LARGEREGION.toString())){
			sql.append("  AND TOR.ROOT_ORG_ID="+orgId);
		}
		if(status.equals(Constant.DUTY_TYPE_SMALLREGION.toString())){
			sql.append("  AND TOR.PQ_ORG_ID="+orgId);
		}
		sql.append("  ORDER BY CREATE_DATE DESC");
		return dao.pageQuery(sql.toString(),null,"com.infodms.dms.dao.sales.customerInfoManage.SalesReportDAO.getCanReportVehiclet",pageSize, curPage);

	}
	//  select * from tt_daily_report where insert_time=TO_DATE('2013-02-06','YYYY-MM-DD');
	
	
	public List<Map<String,Object>> queryDaily(String carType,String contentId,String notContain){ 
		StringBuffer sql=new StringBuffer();
		sql.append("SELECT DAILY_REPORT_ID,\n");
		sql.append("DLR_ID,\n");
		sql.append("CAR_TYPE,\n");
		sql.append("FIRST_PASSENGER,\n");
		sql.append("INVITE_PASSENGER,\n");
		sql.append("CALL_PASSENGER,\n");
		sql.append("TEST_DRIVER,\n");
		sql.append("DELIVERY,\n");
		sql.append("LARGER_DELIVERY,\n");
		sql.append("SECOND_DELIVERY,\n");
		sql.append("O_LEVEL_ORDER,\n");
		sql.append("H_LEVEL_ORDER,\n");
		sql.append("A_LEVEL_ORDER,\n");
		sql.append("B_LEVEL_ORDER,\n");
		sql.append("C_LEVEL_ORDER,\n");
		sql.append("CREATE_CARD,\n");
		sql.append("LOST,\n");
		sql.append("REAL_STOCK,\n");
		sql.append("REGULAR_RECOMMEND,\n");
		sql.append("OLD_CAR_REPLACE,\n");
		sql.append("H_RETAIN,\n");
		sql.append("A_RETAIN,\n");
		sql.append("B_RETAIN,\n");
		sql.append("C_RETAIN,\n");
		sql.append("UNCOMMIT_ORDER,\n");
		sql.append("TO_CHAR(CREATE_DATE,'yyyy-MM-dd') CREATE_DATE,\n");
		sql.append("CONTENT_ID,\n");
		sql.append("STATUS\n");
		sql.append("FROM TT_DAILY_REPORT DR\n");
		sql.append("WHERE 1=1 \n");
		 if(null!=carType&&!"".equals(carType)){
				sql.append(" AND  trim(CAR_TYPE)="+"'"+carType+"'\n");
		 }
		 if(null!=notContain&&!"".equals(notContain)){
				sql.append(" AND  trim(CAR_TYPE)!="+"'"+notContain+"'\n");
		 }
		//INSERT_TIME <=TO_DATE('"+date+" 23:59:59','yyyy-MM-dd HH24:MI:SS')\n");
		sql.append(" AND CONTENT_ID="+contentId+"\n");
		sql.append(" ORDER BY  DR.daily_report_id ");
		return dao.pageQuery(sql.toString(), null, dao.getFunName());

	}
	
	//select DEALER_NAME from TM_DEALER where DEALER_ID IN (select DLR_ID from TT_DAILY_REPORT where CONTENT_ID=2013020719365006)
	public List<Map<String,Object>> queryDealerName(String contentId){
		StringBuffer sql=new StringBuffer();
		sql.append("SELECT DEALER_NAME\n");
		sql.append("FROM TM_DEALER\n");
		sql.append("where DEALER_ID IN\n");
		sql.append("(select DLR_ID from TT_DAILY_REPORT\n");
		sql.append("where CONTENT_ID="+contentId+")");
		return dao.pageQuery(sql.toString(), null, dao.getFunName());
		
	}
	
	
	
	public List<Map<String,Object>> queryDailyReportOrg(Map<String,String> map){
		 String startDate=map.get("startDate");
	     String endDate=map.get("endDate");
	     String dutyType=map.get("dutyType");
	     String orgId=map.get("orgId");
	     String dealerId=map.get("dealerId");
		StringBuffer sql=new StringBuffer();
		sql.append("SELECT ORG.PQ_ORG_NAME,\n") ;
		sql.append("       ORG.DEALER_CODE,\n") ;
		sql.append("       ORG.DEALER_NAME,\n") ;
		sql.append("       V.CAR_TYPE,\n") ;
		sql.append("       V.CREATE_DATE,\n") ;
		sql.append("       V.FIRST_PASSENGER,\n") ;
		sql.append("       V.INVITE_PASSENGER,\n") ;
		sql.append("       V.CALL_PASSENGER,\n") ;
		sql.append("       V.TEST_DRIVER,\n") ;
		sql.append("       V.DELIVERY,\n") ;
		sql.append("       V.LARGER_DELIVERY,\n") ;
		sql.append("       V.SECOND_DELIVERY,\n") ;
		sql.append("       V.O_LEVEL_ORDER,\n") ;
		sql.append("       V.H_LEVEL_ORDER,\n") ;
		sql.append("       V.A_LEVEL_ORDER,\n") ;
		sql.append("       V.B_LEVEL_ORDER,\n") ;
		sql.append("       V.C_LEVEL_ORDER,\n") ;
		sql.append("       V.CREATE_CARD,\n") ;
		sql.append("       V.LOST,\n") ;
		sql.append("       V.REAL_STOCK,\n") ;
		sql.append("       V.REGULAR_RECOMMEND,\n") ;
		sql.append("       V.OLD_CAR_REPLACE,\n") ;
		sql.append("       V.H_RETAIN,\n") ;
		sql.append("       V.A_RETAIN,\n") ;
		sql.append("       V.B_RETAIN,\n") ;
		sql.append("       V.C_RETAIN,\n") ;
		sql.append("       V.UNCOMMIT_ORDER\n") ;
		sql.append("  FROM (SELECT DAILY_REPORT_ID,\n") ;
		sql.append("decode(td1.dealer_level,10851001,td1.dealer_id,10851002,td2.dealer_id) as  dlr_id,\n");

		sql.append("               CAR_TYPE,\n") ;
		sql.append("               FIRST_PASSENGER,\n") ;
		sql.append("               INVITE_PASSENGER,\n") ;
		sql.append("               CALL_PASSENGER,\n") ;
		sql.append("               TEST_DRIVER,\n") ;
		sql.append("               DELIVERY,\n") ;
		sql.append("               LARGER_DELIVERY,\n") ;
		sql.append("               SECOND_DELIVERY,\n") ;
		sql.append("               O_LEVEL_ORDER,\n") ;
		sql.append("               H_LEVEL_ORDER,\n") ;
		sql.append("               A_LEVEL_ORDER,\n") ;
		sql.append("               B_LEVEL_ORDER,\n") ;
		sql.append("               C_LEVEL_ORDER,\n") ;
		sql.append("               CREATE_CARD,\n") ;
		sql.append("               LOST,\n") ;
		sql.append("               REAL_STOCK,\n") ;
		sql.append("               REGULAR_RECOMMEND,\n") ;
		sql.append("               OLD_CAR_REPLACE,\n") ;
		sql.append("               H_RETAIN,\n") ;
		sql.append("               A_RETAIN,\n") ;
		sql.append("               B_RETAIN,\n") ;
		sql.append("               C_RETAIN,\n") ;
		sql.append("               UNCOMMIT_ORDER,\n") ;
		sql.append("               dr.CREATE_DATE CREATE_DATE,\n") ;
		sql.append("               CONTENT_ID,\n") ;
		sql.append("               dr.STATUS,\n") ;
		sql.append("               DECODE(TRIM(DR.CAR_TYPE),\n") ;
		sql.append("                      '奥拓',\n") ;
		sql.append("                      0,\n") ;
		sql.append("                      '羚羊',\n") ;
		sql.append("                      1,\n") ;
		sql.append("                      '雨燕',\n") ;
		sql.append("                      2,\n") ;
		sql.append("                      '天语两厢',\n") ;
		sql.append("                      3,\n") ;
		sql.append("                      '天语三厢',\n") ;
		sql.append("                      4,\n") ;
		sql.append("                      '锋驭',\n") ;
		sql.append("                      5,\n") ;
		sql.append("                      '小计',\n") ;
		sql.append("                      6) AS SORT\n") ;
		sql.append("          FROM TT_DAILY_REPORT DR,tm_dealer td1,tm_dealer td2\n") ;
		sql.append("         WHERE 1 = 1\n") ;
		sql.append("and dr.dlr_id=td1.dealer_id(+)\n" );
		sql.append("             and td1.parent_dealer_d=td2.dealer_id(+)\n");
		sql.append("           AND DR.STATUS = '13201002') V,\n") ;
		sql.append("       VW_ORG_DEALER ORG\n") ;
		sql.append(" WHERE V.DLR_ID = ORG.DEALER_ID\n") ;
		  if(null!=startDate&&!"".equals(startDate)){
		    	 sql.append("                                                                                 AND V.CREATE_DATE >=\n" );
			     sql.append("                                                                                     TO_DATE('"+startDate+"  00:00:00 ',\n" );
			     sql.append("                                                                                             'YYYY-MM-DD hh24:mi:ss')\n" );
		     }
		     if(null!=endDate&&!"".equals(endDate)){
		    	 sql.append("                                                                                 AND V.CREATE_DATE <=\n" );
			     sql.append("                                                                                     TO_DATE('"+endDate+"  23:59:59',\n" );
			     sql.append("                                                                                             'YYYY-MM-DD hh24:mi:ss')");

		     }
		//大区
	     if(Constant.DUTY_TYPE_LARGEREGION.intValue()==Integer.parseInt(dutyType)){
			sql.append(" AND ORG.DEALER_ID IN (SELECT VW.DEALER_ID FROM vw_org_dealer VW WHERE  VW.ROOT_ORG_ID="+orgId+")");
			//小区
	     	}else if(Constant.DUTY_TYPE_SMALLREGION.intValue()==Integer.parseInt(dutyType)){
			sql.append(" AND ORG.DEALER_ID IN (SELECT VW.DEALER_ID FROM vw_org_dealer VW WHERE  VW.PQ_ORG_ID="+orgId+")");
	     }
	   //大区
	     if(Constant.DUTY_TYPE_DEALER.intValue()==Integer.parseInt(dutyType)){
	    	 sql.append("AND ORG.DEALER_ID IN\n" );
	 		sql.append("      (SELECT TD1.DEALER_ID\n" );
	 		sql.append("         FROM TM_DEALER TD1\n" );
	 		sql.append("        WHERE TD1.STATUS = 10011001\n" );
	 		sql.append("        START WITH TD1.DEALER_ID IN ("+dealerId+")\n" );
	 		sql.append("       CONNECT BY PRIOR TD1.DEALER_ID = TD1.PARENT_DEALER_D)\n");
	     }
		sql.append(" ORDER BY ORG.PQ_ORG_NAME, ORG.DEALER_CODE, V.SORT") ;
		return dao.pageQuery(sql.toString(), null, dao.getFunName());
		
	}
	
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}

}
