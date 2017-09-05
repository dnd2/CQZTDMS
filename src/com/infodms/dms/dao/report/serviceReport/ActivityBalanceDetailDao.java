package com.infodms.dms.dao.report.serviceReport;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.auditBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.dao.common.BaseDao;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

public class ActivityBalanceDetailDao extends BaseDao{
	public static final Logger logger = Logger.getLogger(ActivityBalanceDetailDao.class);
	public static final ActivityBalanceDetailDao dao = new ActivityBalanceDetailDao();
	public static final ActivityBalanceDetailDao getInstance(){
		return dao;
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public PageResult<Map<String, Object>> ActivityBalanceDetail(String dealerName,String subject_id,String type,String StratDate,String EndDate,int curPage, int pageSize){
		StringBuffer sql= new StringBuffer();
		sql.append("select DEALER_NAME,\n" );
		sql.append("       SUBJECT_NAME,");
		sql.append("       sum(BALANCE_AMOUNT) as BALANCE_AMOUNT,REPORT_DATE \n" );
		sql.append("    from (");

		sql.append(" SELECT  B.DEALER_SHORTNAME DEALER_NAME,F.SUBJECT_NAME,SUM(A.BALANCE_AMOUNT) BALANCE_AMOUNT, to_char(A.REPORT_DATE,'yyyy-mm') REPORT_DATE\n" );
		
		sql.append(" FROM TM_DEALER B,TT_AS_WR_APPLICATION A,TM_VHCL_MATERIAL_GROUP T, TT_AS_ACTIVITY G ,TT_AS_ACTIVITY_SUBJECT F \n" );
		sql.append(" WHERE  A.DEALER_ID = B.DEALER_ID  AND  A.CLAIM_TYPE != 10661002 AND T.GROUP_CODE = A.MODEL_CODE \n" );
		sql.append(" AND G.SUBJECT_ID =  F.SUBJECT_ID ");
		sql.append(" AND A.CAMPAIGN_CODE = G.ACTIVITY_CODE "); 
		sql.append(" AND  A.CLAIM_TYPE = "+Constant.CLA_TYPE_06 );
		sql.append(" AND A.STATUS = 10791013 ");
		if(type.equals("YX"))
		{
			sql.append(" AND A.CAMPAIGN_CODE not in (SELECT D.ACTIVITY_CODE from TT_AS_ACTIVITY D,TT_AS_ACTIVITY_SUBJECT E where D.SUBJECT_ID = E.SUBJECT_ID and E.ACTIVITY_TYPE = "+Constant.SERVICEACTIVITY_TYPE_01+")");
		}else if(type.equals("JS"))
		{
			sql.append(" AND A.CAMPAIGN_CODE  in (SELECT D.ACTIVITY_CODE from TT_AS_ACTIVITY D,TT_AS_ACTIVITY_SUBJECT E where D.SUBJECT_ID = E.SUBJECT_ID and E.ACTIVITY_TYPE = "+Constant.SERVICEACTIVITY_TYPE_01+")");
		}
		if(Utility.testString(dealerName)){
			sql.append(" AND B.DEALER_NAME LIKE '%"+dealerName+"%'\n");
		}
		
		if(Utility.testString(subject_id)){
			sql.append(" AND F.SUBJECT_ID ="+subject_id+"\n");
		}
		if(Utility.testString(StratDate))
		{
			sql.append(" AND  A.REPORT_DATE >= to_date('"+StratDate+"','yyyy-mm') \n");
		}
		
		if(Utility.testString(EndDate))
		{
			 String[] ends= EndDate.split("-");
			 int i = Integer.parseInt(ends[1])+1;
			 EndDate = ends[0]+ "-"+i;
			sql.append(" AND A.REPORT_DATE <= to_date('"+EndDate+"','yyyy-mm') \n");
		}
    	
		sql.append(" GROUP BY A.DEALER_ID,B.DEALER_SHORTNAME,F.SUBJECT_ID,F.SUBJECT_NAME,to_char(A.REPORT_DATE,'yyyy-mm') ");
		sql.append(" ) group by  DEALER_NAME ,SUBJECT_NAME,REPORT_DATE ");
		sql.append(" order by  REPORT_DATE desc ");
		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
		return ps;
	}
	
	public List<Map<String, Object>> getDeaerAct(String dealerName,String subject_id,String type,String Date,List<Map<String, Object>> list)
	{
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT A.DEALER_CODE\n" );
		for(int i = 0 ;i < list.size()  ;i++)
		{
			sql.append(",min(decode(A.SUBJECT_NAME,'"+list.get(i).get("SUBJECT_NAME").toString()+"',A.BALANCE_AMOUNT,null)) SUBJECT_NAME"+ i);
		}
		sql.append(" from (");
		sql.append("select DEALER_CODE,\n" );
		sql.append("       SUBJECT_NAME,");
		sql.append("       sum(BALANCE_AMOUNT) as BALANCE_AMOUNT,REPORT_DATE \n" );
		sql.append("    from (");

		sql.append(" SELECT  B.DEALER_SHORTNAME DEALER_NAME,B.DEALER_CODE DEALER_CODE,F.SUBJECT_NAME,SUM(A.BALANCE_AMOUNT) BALANCE_AMOUNT, to_char(A.REPORT_DATE,'yyyy-mm') REPORT_DATE\n" );
		
		sql.append(" FROM TM_DEALER B,TT_AS_WR_APPLICATION A,TM_VHCL_MATERIAL_GROUP T, TT_AS_ACTIVITY G ,TT_AS_ACTIVITY_SUBJECT F \n" );
		sql.append(" WHERE  A.DEALER_ID = B.DEALER_ID  AND  A.CLAIM_TYPE != 10661002 AND T.GROUP_CODE = A.MODEL_CODE \n" );
		sql.append(" AND G.SUBJECT_ID =  F.SUBJECT_ID ");
		sql.append(" AND A.CAMPAIGN_CODE = G.ACTIVITY_CODE "); 
		sql.append(" AND  A.CLAIM_TYPE = "+Constant.CLA_TYPE_06 );
		sql.append(" AND A.STATUS = 10791013 ");
		if(type.equals("YX"))
		{
			sql.append(" AND A.CAMPAIGN_CODE not in (SELECT D.ACTIVITY_CODE from TT_AS_ACTIVITY D,TT_AS_ACTIVITY_SUBJECT E where D.SUBJECT_ID = E.SUBJECT_ID and E.ACTIVITY_TYPE = "+Constant.SERVICEACTIVITY_TYPE_01+")");
		}else if(type.equals("JS"))
		{
			sql.append(" AND A.CAMPAIGN_CODE  in (SELECT D.ACTIVITY_CODE from TT_AS_ACTIVITY D,TT_AS_ACTIVITY_SUBJECT E where D.SUBJECT_ID = E.SUBJECT_ID and E.ACTIVITY_TYPE = "+Constant.SERVICEACTIVITY_TYPE_01+")");
		}
		if(Utility.testString(dealerName)){
			sql.append(" AND B.DEALER_NAME LIKE '%"+dealerName+"%'\n");
		}
		
		if(Utility.testString(subject_id)){
			sql.append(" AND F.SUBJECT_ID ="+subject_id+"\n");
		}
		if(Utility.testString(Date))
		{
			sql.append(" AND  to_char(A.REPORT_DATE,'yyyy-mm') = '"+Date+"' \n");
		}
		sql.append(" GROUP BY A.DEALER_ID,B.DEALER_SHORTNAME,B.DEALER_CODE,F.SUBJECT_ID,F.SUBJECT_NAME,to_char(A.REPORT_DATE,'yyyy-mm') ");
		sql.append(" ) group by  DEALER_NAME ,DEALER_CODE,SUBJECT_NAME,REPORT_DATE ");
		sql.append(" order by  REPORT_DATE desc ");
		sql.append(") A group by A.DEALER_CODE");

		
		List<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName());
		return ps;
	}
	
	
	public List<Map<String, Object>> inDate(String dealerName,String subject_id,String type,String StratDate,String EndDate)
	{
		StringBuffer sql= new StringBuffer();
		
		sql.append(" SELECT  to_char(A.REPORT_DATE,'yyyy-mm') REPORT_DATE \n" );
		
		sql.append(" FROM TM_DEALER B,TT_AS_WR_APPLICATION A,TM_VHCL_MATERIAL_GROUP T, TT_AS_ACTIVITY G ,TT_AS_ACTIVITY_SUBJECT F \n" );
		sql.append(" WHERE  A.DEALER_ID = B.DEALER_ID  AND  A.CLAIM_TYPE != 10661002 AND T.GROUP_CODE = A.MODEL_CODE \n" );
		sql.append(" AND G.SUBJECT_ID =  F.SUBJECT_ID ");
		sql.append(" AND A.CAMPAIGN_CODE = G.ACTIVITY_CODE "); 
		sql.append(" AND  A.CLAIM_TYPE = "+Constant.CLA_TYPE_06 );
		sql.append(" AND A.STATUS = 10791013 ");
		if(type.equals("YX"))
		{
			sql.append(" AND A.CAMPAIGN_CODE not in (SELECT D.ACTIVITY_CODE from TT_AS_ACTIVITY D,TT_AS_ACTIVITY_SUBJECT E where D.SUBJECT_ID = E.SUBJECT_ID and E.ACTIVITY_TYPE = "+Constant.SERVICEACTIVITY_TYPE_01+")");
		}else if(type.equals("JS"))
		{
			sql.append(" AND A.CAMPAIGN_CODE  in (SELECT D.ACTIVITY_CODE from TT_AS_ACTIVITY D,TT_AS_ACTIVITY_SUBJECT E where D.SUBJECT_ID = E.SUBJECT_ID and E.ACTIVITY_TYPE = "+Constant.SERVICEACTIVITY_TYPE_01+")");
		}
		if(Utility.testString(dealerName)){
			sql.append(" AND B.DEALER_NAME LIKE '%"+dealerName+"%'\n");
		}
		
		if(Utility.testString(subject_id)){
			sql.append(" AND F.SUBJECT_ID ="+subject_id+"\n");
		}
		if(Utility.testString(StratDate))
		{
			sql.append(" AND  A.REPORT_DATE >= to_date('"+StratDate+"','yyyy-mm') \n");
		}
		
		if(Utility.testString(EndDate))
		{
			 String[] ends= EndDate.split("-");
			 int i = Integer.parseInt(ends[1])+1;
			 EndDate = ends[0]+ "-"+i;
			sql.append(" AND A.REPORT_DATE <= to_date('"+EndDate+"','yyyy-mm') \n");
		}
		sql.append(" GROUP BY to_char(A.REPORT_DATE,'yyyy-mm') ");
		List<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName());
		return ps;
	}
	
	
	public List<Map<String, Object>> SubActivity(String dealerName,String subject_id,String type,String Date){
		StringBuffer sql= new StringBuffer();
		
		sql.append(" SELECT  F.SUBJECT_NAME \n" );
		
		sql.append(" FROM TM_DEALER B,TT_AS_WR_APPLICATION A,TM_VHCL_MATERIAL_GROUP T, TT_AS_ACTIVITY G ,TT_AS_ACTIVITY_SUBJECT F \n" );
		sql.append(" WHERE  A.DEALER_ID = B.DEALER_ID  AND  A.CLAIM_TYPE != 10661002 AND T.GROUP_CODE = A.MODEL_CODE \n" );
		sql.append(" AND G.SUBJECT_ID =  F.SUBJECT_ID ");
		sql.append(" AND A.CAMPAIGN_CODE = G.ACTIVITY_CODE "); 
		sql.append(" AND  A.CLAIM_TYPE = "+Constant.CLA_TYPE_06 );
		sql.append(" AND A.STATUS = 10791013 ");
		if(type.equals("YX"))
		{
			sql.append(" AND A.CAMPAIGN_CODE not in (SELECT D.ACTIVITY_CODE from TT_AS_ACTIVITY D,TT_AS_ACTIVITY_SUBJECT E where D.SUBJECT_ID = E.SUBJECT_ID and E.ACTIVITY_TYPE = "+Constant.SERVICEACTIVITY_TYPE_01+")");
		}else if(type.equals("JS"))
		{
			sql.append(" AND A.CAMPAIGN_CODE  in (SELECT D.ACTIVITY_CODE from TT_AS_ACTIVITY D,TT_AS_ACTIVITY_SUBJECT E where D.SUBJECT_ID = E.SUBJECT_ID and E.ACTIVITY_TYPE = "+Constant.SERVICEACTIVITY_TYPE_01+")");
		}
		if(Utility.testString(dealerName)){
			sql.append(" AND B.DEALER_NAME LIKE '%"+dealerName+"%'\n");
		}
		
		if(Utility.testString(subject_id)){
			sql.append(" AND F.SUBJECT_ID ="+subject_id+"\n");
		}
		if(Utility.testString(Date))
		{
			sql.append(" AND  to_char(A.REPORT_DATE,'yyyy-mm') = '"+Date+"' \n");
		}
		sql.append(" GROUP BY F.SUBJECT_NAME");
		List<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName());
		return ps;
	}
	
	
}
