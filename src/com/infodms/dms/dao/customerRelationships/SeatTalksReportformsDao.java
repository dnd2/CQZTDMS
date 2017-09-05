package com.infodms.dms.dao.customerRelationships;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

import com.infodms.dms.common.Constant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.util.StringUtil;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;



@SuppressWarnings("unchecked")
public class SeatTalksReportformsDao extends BaseDao{

	private static final SeatTalksReportformsDao dao = new SeatTalksReportformsDao();
	
	public static final SeatTalksReportformsDao getInstance() {
		return dao;
	}
	
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		return null;
	}
	
	
	
	public PageResult<Map<String,Object>> querySeatTalksReportformsDao(String dealName,String dateStart,String dateEnd,String paiStart,String paiEnd,int pageSize,int curPage){
		String sql  = returnSQL(dealName, dateStart, dateEnd,paiStart,paiEnd);
		return (PageResult<Map<String, Object>>)this.pageQuery(sql.toString(),
				null,
				this.getFunName(),
				pageSize,
				curPage);
	}
	
	
	public List<Map<String,Object>> querySeatTalksReportformsDao(String dealName,String dateStart,String dateEnd,String paiStart,String paiEnd ){
		String sql  = returnSQL(dealName, dateStart, dateEnd,paiStart,paiEnd);
		return (List<Map<String, Object>>)this.pageQuery(sql,
				null,
				this.getFunName());
	}	
	
	private String returnSQL(String dealName,String dateStart,String dateEnd,String paiStart,String paiEnd){
		StringBuffer sbSql = new StringBuffer();
		// 艾春 9.13 添加 话务量统计分析 2013.11.6 去掉天数
		sbSql.append("SELECT T.ACC, T.NAME, T.TOTALC, T.INC, T.OUTC,\n" +
					"       DECODE(T.TOTALTALKTIME,NULL,NULL,\n" + 
//					"              TRUNC(T.TOTALTALKTIME / (24 * 3600)) || ' ' ||\n" + 
					"              TRUNC(T.TOTALTALKTIME / 3600) || ':' ||\n" + 
					"              MOD(TRUNC(T.TOTALTALKTIME / 60),60) || ':' || MOD(T.TOTALTALKTIME, 60)) TOTALTALKTIME,\n" + 
					"       DECODE(T.TOTALTALKTIME,NULL,NULL,TRUNC(T.TOTALTALKTIME / T.TOTALC, 2)) TOTALTALKTIMEAVG,\n" + 
					"       DECODE(T.INTALKTIME,NULL,NULL,\n" + 
//					"              TRUNC(T.INTALKTIME / (24 * 3600)) || ' ' ||\n" + 
					"              TRUNC(T.INTALKTIME / 3600) || ':' || MOD(TRUNC(T.INTALKTIME / 60),60) || ':' ||\n" + 
					"              MOD(T.INTALKTIME, 60)) INTALKTIME,\n" + 
					"       DECODE(T.INTALKTIME, NULL, NULL, TRUNC(T.INTALKTIME / T.INC, 2)) INTALKTIMEAVG,\n" + 
					"       DECODE(T.OUTTALKTIME,NULL,NULL,\n" + 
//					"              TRUNC(T.OUTTALKTIME / (24 * 3600)) || ' ' ||\n" + 
					"              TRUNC(T.OUTTALKTIME / 3600) || ':' ||\n" + 
					"              MOD(TRUNC(T.OUTTALKTIME / 60),60) || ':' || MOD(T.OUTTALKTIME, 60)) OUTTALKTIME,\n" + 
					"       DECODE(T.OUTTALKTIME, NULL, NULL, TRUNC(T.OUTTALKTIME / T.OUTC, 2)) OUTTALKTIMEAVG\n" + 
					"  FROM ("); 
		if( (!Utility.testString(dateStart)  &&  !Utility.testString(paiStart)) || (Utility.testString(dateStart) )   )
		{
			sbSql.append(	"SELECT NVL(A.SE_ACCOUNT, '合计') ACC, DECODE(A.SE_ACCOUNT,NULL,NULL,MAX(A.SE_NAME)) NAME,\n" + 
		          "               SUM(DECODE(R.SE_ID, NULL, NULL, 1)) TOTALC,\n" + 
		          "               SUM(TO_CHAR(R.CR_TALK_TIME, 'HH24') * 3600 +\n" + 
		          "                   TO_CHAR(R.CR_TALK_TIME, 'MI') * 60 +\n" + 
		          "                   TO_CHAR(R.CR_TALK_TIME, 'SS')) TOTALTALKTIME,\n" + 
		          "               SUM(DECODE(R.CR_CALL_TYPE, "+Constant.CALL_IN_TYPE+", 1, NULL)) INC,\n" + 
		          "               SUM(DECODE(R.CR_CALL_TYPE,"+Constant.CALL_IN_TYPE+",\n" + 
		          "                          TO_CHAR(R.CR_TALK_TIME, 'HH24') * 3600 +\n" + 
		          "                          TO_CHAR(R.CR_TALK_TIME, 'MI') * 60 +\n" + 
		          "                          TO_CHAR(R.CR_TALK_TIME, 'SS'),NULL)) INTALKTIME,\n" + 
		          "               SUM(DECODE(R.CR_CALL_TYPE, "+Constant.CALL_OUT_TYPE+", 1, NULL)) OUTC,\n" + 
		          "               SUM(DECODE(R.CR_CALL_TYPE,"+Constant.CALL_OUT_TYPE+",\n" + 
		          "                          TO_CHAR(R.CR_TALK_TIME, 'HH24') * 3600 +\n" + 
		          "                          TO_CHAR(R.CR_TALK_TIME, 'MI') * 60 +\n" + 
		          "                          TO_CHAR(R.CR_TALK_TIME, 'SS'),NULL)) OUTTALKTIME\n" + 
		          "          FROM TT_CRM_SEATS A LEFT JOIN TT_CRM_CALL_RECORD R ON A.SE_ID = R.SE_ID\n" + 
		          "         WHERE A.SE_IS_SEATS = "+Constant.IF_TYPE_YES+" and r.cr_income_type = "+Constant.CALLING_TYPE+"\n");
		    if(StringUtil.notNull(dateStart)){
		      sbSql.append(" and R.CR_STA_DATE >= to_date('"+dateStart+"','yyyy-MM-dd')\r\n");
		    }
		    if(StringUtil.notNull(dateEnd)){
		      sbSql.append(" and R.CR_END_DATE <= to_date('"+dateEnd+" 23:59:59','yyyy-MM-dd hh24:mi:ss')\r\n");
		    }
		    if(StringUtil.notNull(dealName)){
		      sbSql.append(" and a.SE_NAME like '%"+dealName+"%' \r\n");
		    }
		    sbSql.append(" GROUP BY ROLLUP(SE_ACCOUNT) ");
		}else if(Utility.testString(paiStart))
		{
			
			sbSql.append("SELECT\n" );
			sbSql.append(" NVL(M.SE_ACCOUNT, '合计') ACC, DECODE(M.SE_ACCOUNT,NULL,NULL,MAX(M.NAME)) NAME,\n" );
			sbSql.append("               SUM(M.TOTALC) TOTALC,\n" );
			sbSql.append("                sum(M.TOTALTALKTIME) TOTALTALKTIME,SUM(M.INC) INC,  \n" );
			sbSql.append("               SUM(M.INTALKTIME) INTALKTIME,\n" );
			sbSql.append("               SUM(M.OUTC) OUTC,\n" );
			sbSql.append("               SUM(M.OUTTALKTIME) OUTTALKTIME\n" );
			sbSql.append("from (\n" );
			sbSql.append("SELECT   A.SE_ACCOUNT , DECODE(A.SE_ACCOUNT,NULL,NULL,MAX(A.SE_NAME)) NAME,\n" );
			sbSql.append("               SUM(DECODE(R.SE_ID, NULL, NULL, 1)) TOTALC,\n" );
			sbSql.append("               SUM(TO_CHAR(R.CR_TALK_TIME, 'HH24') * 3600 +\n" );
			sbSql.append("                   TO_CHAR(R.CR_TALK_TIME, 'MI') * 60 +\n" );
			sbSql.append("                   TO_CHAR(R.CR_TALK_TIME, 'SS')) TOTALTALKTIME,\n" );
			sbSql.append("               SUM(DECODE(R.CR_CALL_TYPE, 95101001, 1, NULL)) INC,\n" );
			sbSql.append("               SUM(DECODE(R.CR_CALL_TYPE,95101001,\n" );
			sbSql.append("                          TO_CHAR(R.CR_TALK_TIME, 'HH24') * 3600 +\n" );
			sbSql.append("                          TO_CHAR(R.CR_TALK_TIME, 'MI') * 60 +\n" );
			sbSql.append("                          TO_CHAR(R.CR_TALK_TIME, 'SS'),NULL)) INTALKTIME,\n" );
			sbSql.append("               SUM(DECODE(R.CR_CALL_TYPE, 95101002, 1, NULL)) OUTC,\n" );
			sbSql.append("               SUM(DECODE(R.CR_CALL_TYPE,95101002,\n" );
			sbSql.append("                          TO_CHAR(R.CR_TALK_TIME, 'HH24') * 3600 +\n" );
			sbSql.append("                          TO_CHAR(R.CR_TALK_TIME, 'MI') * 60 +\n" );
			sbSql.append("                          TO_CHAR(R.CR_TALK_TIME, 'SS'),NULL)) OUTTALKTIME\n" );
			sbSql.append("          FROM TT_CRM_SEATS A LEFT JOIN TT_CRM_CALL_RECORD R ON A.SE_ID = R.SE_ID\n" );
			sbSql.append("         WHERE A.SE_IS_SEATS = "+Constant.IF_TYPE_YES+" and r.cr_income_type = "+Constant.CALLING_TYPE+" and R.CR_STA_DATE >= (to_date('"+paiStart+"','yyyy-MM-dd')+1)\n" );
			sbSql.append(" and R.CR_END_DATE <= (to_date('"+paiEnd+" 23:59:59','yyyy-MM-dd hh24:mi:ss'))\n" );
			if(StringUtil.notNull(dealName))
			{
				 if(StringUtil.notNull(dealName))
				 {
			      sbSql.append(" and a.SE_NAME like '%"+dealName+"%' \r\n");
			    }
			}
			sbSql.append(" GROUP BY SE_ACCOUNT\n" );
			sbSql.append("\n" );			
			sbSql.append(" UNION ALL\n" );
			sbSql.append("\n" );
			sbSql.append(" SELECT   A.SE_ACCOUNT , DECODE(A.SE_ACCOUNT,NULL,NULL,MAX(A.SE_NAME)) NAME,\n" );
			sbSql.append("               SUM(DECODE(R.SE_ID, NULL, NULL, 1)) TOTALC,\n" );
			sbSql.append("               SUM(TO_CHAR(R.CR_TALK_TIME, 'HH24') * 3600 +\n" );
			sbSql.append("                   TO_CHAR(R.CR_TALK_TIME, 'MI') * 60 +\n" );
			sbSql.append("                   TO_CHAR(R.CR_TALK_TIME, 'SS')) TOTALTALKTIME,\n" );
			sbSql.append("               SUM(DECODE(R.CR_CALL_TYPE, 95101001, 1, NULL)) INC,\n" );
			sbSql.append("               SUM(DECODE(R.CR_CALL_TYPE,95101001,\n" );
			sbSql.append("                          TO_CHAR(R.CR_TALK_TIME, 'HH24') * 3600 +\n" );
			sbSql.append("                          TO_CHAR(R.CR_TALK_TIME, 'MI') * 60 +\n" );
			sbSql.append("                          TO_CHAR(R.CR_TALK_TIME, 'SS'),NULL)) INTALKTIME,\n" );
			sbSql.append("               SUM(DECODE(R.CR_CALL_TYPE, 95101002, 1, NULL)) OUTC,\n" );
			sbSql.append("               SUM(DECODE(R.CR_CALL_TYPE,95101002,\n" );
			sbSql.append("                          TO_CHAR(R.CR_TALK_TIME, 'HH24') * 3600 +\n" );
			sbSql.append("                          TO_CHAR(R.CR_TALK_TIME, 'MI') * 60 +\n" );
			sbSql.append("                          TO_CHAR(R.CR_TALK_TIME, 'SS'),NULL)) OUTTALKTIME\n" );
			sbSql.append("          FROM    TT_CRM_SORT_SHIFT B ,TT_CRM_SEATS A LEFT JOIN TT_CRM_CALL_RECORD R ON A.SE_ID = R.SE_ID\n" );
			sbSql.append("         WHERE A.SE_IS_SEATS = "+Constant.IF_TYPE_YES+"  and r.cr_income_type = "+Constant.CALLING_TYPE+" and TRUNC(R.CR_STA_DATE) = to_date('"+paiStart+"','yyyy-mm-dd')\n");
			sbSql.append("          and R.CR_STA_DATE >= (SELECT MIN(ts.sta_date) FROM tt_crm_sort_shift ts WHERE ts.wt_type != "+Constant.SHIFT_TIMES_NIGHT_DAYS+" AND trunc(ts.duty_date) = to_date('"+paiStart+"','yyyy-MM-dd'))");
			sbSql.append(" and B.USER_ID = A.SE_USER_ID and B.WT_TYPE  !=  "+Constant.SHIFT_TIMES_NIGHT_DAYS+"\n" );
			if(StringUtil.notNull(dealName))
			{
				 if(StringUtil.notNull(dealName))
				 {
			      sbSql.append(" and a.SE_NAME like '%"+dealName+"%' \r\n");
			    }
			}
			sbSql.append(" and to_char(B.DUTY_DATE,'yyyy-mm-dd') = '"+paiStart+"'\n" );
			sbSql.append(" GROUP BY A.SE_ACCOUNT\n" );
			sbSql.append("\n" );
			sbSql.append("\n" );
			
			sbSql.append("UNION ALL\n" );
			sbSql.append("\n" );
			sbSql.append(" SELECT   A.SE_ACCOUNT , DECODE(A.SE_ACCOUNT,NULL,NULL,MAX(A.SE_NAME)) NAME,\n" );
			sbSql.append("               SUM(DECODE(R.SE_ID, NULL, NULL, 1)) TOTALC,\n" );
			sbSql.append("               SUM(TO_CHAR(R.CR_TALK_TIME, 'HH24') * 3600 +\n" );
			sbSql.append("                   TO_CHAR(R.CR_TALK_TIME, 'MI') * 60 +\n" );
			sbSql.append("                   TO_CHAR(R.CR_TALK_TIME, 'SS')) TOTALTALKTIME,\n" );
			sbSql.append("               SUM(DECODE(R.CR_CALL_TYPE, 95101001, 1, NULL)) INC,\n" );
			sbSql.append("               SUM(DECODE(R.CR_CALL_TYPE,95101001,\n" );
			sbSql.append("                          TO_CHAR(R.CR_TALK_TIME, 'HH24') * 3600 +\n" );
			sbSql.append("                          TO_CHAR(R.CR_TALK_TIME, 'MI') * 60 +\n" );
			sbSql.append("                          TO_CHAR(R.CR_TALK_TIME, 'SS'),NULL)) INTALKTIME,\n" );
			sbSql.append("               SUM(DECODE(R.CR_CALL_TYPE, 95101002, 1, NULL)) OUTC,\n" );
			sbSql.append("               SUM(DECODE(R.CR_CALL_TYPE,95101002,\n" );
			sbSql.append("                          TO_CHAR(R.CR_TALK_TIME, 'HH24') * 3600 +\n" );
			sbSql.append("                          TO_CHAR(R.CR_TALK_TIME, 'MI') * 60 +\n" );
			sbSql.append("                          TO_CHAR(R.CR_TALK_TIME, 'SS'),NULL)) OUTTALKTIME\n" );
			sbSql.append("          FROM    TT_CRM_SORT_SHIFT B ,TT_CRM_SEATS A LEFT JOIN TT_CRM_CALL_RECORD R ON A.SE_ID = R.SE_ID\n" );
			sbSql.append("         WHERE A.SE_IS_SEATS = "+Constant.IF_TYPE_YES+"  and r.cr_income_type = "+Constant.CALLING_TYPE+" and TRUNC(R.CR_STA_DATE) = to_date('"+paiStart+"','yyyy-mm-dd')\n" );
			sbSql.append("          and R.CR_STA_DATE >= (SELECT MIN(ts.sta_date) FROM tt_crm_sort_shift ts WHERE ts.wt_type = "+Constant.SHIFT_TIMES_NIGHT_DAYS+" AND trunc(ts.duty_date) = to_date('"+paiStart+"','yyyy-MM-dd'))");
			sbSql.append(" and B.USER_ID = A.SE_USER_ID and B.WT_TYPE  =  "+Constant.SHIFT_TIMES_NIGHT_DAYS+"\n" );
			if(StringUtil.notNull(dealName))
			{
				 if(StringUtil.notNull(dealName))
				 {
			      sbSql.append(" and a.SE_NAME like '%"+dealName+"%' \r\n");
			    }
			}
			
			sbSql.append(" and to_char(B.DUTY_DATE,'yyyy-mm-dd') = '"+paiStart+"'\n" );
			sbSql.append(" GROUP BY A.SE_ACCOUNT ");

			
			
			sbSql.append(" UNION ALL\n" );
			sbSql.append("\n" );
			sbSql.append(" SELECT   A.SE_ACCOUNT , DECODE(A.SE_ACCOUNT,NULL,NULL,MAX(A.SE_NAME)) NAME,\n" );
			sbSql.append("               SUM(DECODE(R.SE_ID, NULL, NULL, 1)) TOTALC,\n" );
			sbSql.append("               SUM(TO_CHAR(R.CR_TALK_TIME, 'HH24') * 3600 +\n" );
			sbSql.append("                   TO_CHAR(R.CR_TALK_TIME, 'MI') * 60 +\n" );
			sbSql.append("                   TO_CHAR(R.CR_TALK_TIME, 'SS')) TOTALTALKTIME,\n" );
			sbSql.append("               SUM(DECODE(R.CR_CALL_TYPE, 95101001, 1, NULL)) INC,\n" );
			sbSql.append("               SUM(DECODE(R.CR_CALL_TYPE,95101001,\n" );
			sbSql.append("                          TO_CHAR(R.CR_TALK_TIME, 'HH24') * 3600 +\n" );
			sbSql.append("                          TO_CHAR(R.CR_TALK_TIME, 'MI') * 60 +\n" );
			sbSql.append("                          TO_CHAR(R.CR_TALK_TIME, 'SS'),NULL)) INTALKTIME,\n" );
			sbSql.append("               SUM(DECODE(R.CR_CALL_TYPE, 95101002, 1, NULL)) OUTC,\n" );
			sbSql.append("               SUM(DECODE(R.CR_CALL_TYPE,95101002,\n" );
			sbSql.append("                          TO_CHAR(R.CR_TALK_TIME, 'HH24') * 3600 +\n" );
			sbSql.append("                          TO_CHAR(R.CR_TALK_TIME, 'MI') * 60 +\n" );
			sbSql.append("                          TO_CHAR(R.CR_TALK_TIME, 'SS'),NULL)) OUTTALKTIME\n" );
			sbSql.append("          FROM    TT_CRM_SORT_SHIFT B ,TT_CRM_SEATS A LEFT JOIN TT_CRM_CALL_RECORD R ON A.SE_ID = R.SE_ID\n" );
			sbSql.append("         WHERE A.SE_IS_SEATS = "+Constant.IF_TYPE_YES+"  and r.cr_income_type = "+Constant.CALLING_TYPE+" and TRUNC(R.CR_STA_DATE) = to_date('"+paiEnd+"','yyyy-mm-dd')+1" );
			sbSql.append("          and R.CR_STA_DATE <= (SELECT MIN(ts.end_date)+1/48 FROM tt_crm_sort_shift ts WHERE ts.wt_type = "+Constant.SHIFT_TIMES_NIGHT_DAYS+" AND trunc(ts.duty_date) = to_date('"+paiEnd+"','yyyy-MM-dd'))");
			if(StringUtil.notNull(dealName))
			{
				 if(StringUtil.notNull(dealName))
				 {
			      sbSql.append(" and a.SE_NAME like '%"+dealName+"%' \r\n");
			    }
			}
			sbSql.append(" and B.USER_ID = A.SE_USER_ID and B.WT_TYPE = "+Constant.SHIFT_TIMES_NIGHT_DAYS+"\n" );
			sbSql.append(" and to_char(B.DUTY_DATE,'yyyy-mm-dd') = '"+paiEnd+"'\n" );
			sbSql.append(" GROUP BY A.SE_ACCOUNT ) M\n" );
			sbSql.append(" GROUP by ROLLUP(M.SE_ACCOUNT)\n" );
			sbSql.append("\n" );
			sbSql.append("\n" );
		}
		
		sbSql.append(") T ");
		
		
		

		return sbSql.toString();
	}
	
	public List<Map<String,Object>> getCallRecordsByACC(String acc,String dateStart,String dateEnd){
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("select to_char(a.cr_talk_time,'HH24:Mi:SS') TALKTIME,a.cr_call_type CALLTYPE\r\n");
		sbSql.append("  from TT_CRM_CALL_RECORD a, Tt_Crm_Seats b\r\n");
		sbSql.append(" where a.se_id = b.se_id\r\n");
		sbSql.append("   and b.se_is_seats = "+Constant.IF_TYPE_YES+"\r\n");
		sbSql.append("   and b.se_account = '"+acc+"' \r\n"); 
		if(StringUtil.notNull(dateStart)){
			sbSql.append(" and a.CR_STA_DATE >= to_date('"+dateStart+"','yyyy-MM-dd')\r\n");
		}
		if(StringUtil.notNull(dateEnd)){
			sbSql.append(" and a.CR_END_DATE <= to_date('"+dateEnd+" 23:59:59','yyyy-MM-dd hh24:mi:ss')\r\n");
		}
		
		return (List<Map<String, Object>>)this.pageQuery(sbSql.toString(),
				null,
				this.getFunName());
	}
	

}
