package com.infodms.dms.dao.customerRelationships;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TtCrmCallRecordPO;
import com.infodms.dms.util.StringUtil;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

@SuppressWarnings("unchecked")
public class CallRecordDao extends BaseDao{

	private static final CallRecordDao dao = new CallRecordDao();
	
	public static final CallRecordDao getInstance() {
		return dao;
	}
	
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		return null;
	}
	
	public PageResult<Map<String,Object>> queryCallRecord(String account,String ext,String dateStartOne,String dateStartTwo,
			String number,String inComeType,String callType,String point,String missedType,String if_type,
			int pageSize,int curPage){
		
		StringBuffer sql = new StringBuffer();
		sql.append("select t.cr_number CRNUMBER,t.cr_sta_date STADATE,t.cr_end_date ENDDATE, \n");
		
		// 艾春 13.11.18 添加振铃时长字段, 13.11.21 添加坐席名字段, 13.11.30 添加等待时长
		sql.append(" t.CR_TALK_TIME TALKTIME, to_char(t.CR_RING_TIME,'HH24:MI:SS') RINGTIME, to_char(t.CR_WAIT_TIME,'HH24:MI:SS') WAITTIME, ts.se_seats_no SEATSNO, TS.SE_NAME SEATSNAME,t.cr_ext EXT,t.CR_MOVE_NUMBER MOVENUMBER, \n");
//		sql.append(" t.CR_TALK_TIME TALKTIME, ts.se_seats_no SEATSNO,t.cr_ext EXT,t.CR_MOVE_NUMBER MOVENUMBER, \n");
		// 艾春 13.11.18 添加振铃时长字段, 13.11.21 添加坐席名字段, 13.11.30 添加等待时长
		
		sql.append(" t.cr_call_type CALLTYPE, t.cr_income_type INCOMETYPE,t.cr_points POINTS,t.CR_RECORD_ADDR CR_RECORDADDR \n");
		sql.append(" from TT_CRM_CALL_RECORD t left join TT_CRM_SEATS ts on t.se_id = ts.se_id \n");
		sql.append(" where 1=1 \n");

		if(StringUtil.notNull(account)){
			sql.append(" and ts.se_seats_no like '%"+account+"%'\n");
		}
		
		if(StringUtil.notNull(if_type)){
			sql.append(" and ts.SE_IS_SEATS = \n"+ if_type);
		}
		
		if(StringUtil.notNull(ext)){
			sql.append(" and cr_ext like '%"+ext+"%'\n");
		}
		if(StringUtil.notNull(dateStartOne)){
			sql.append(" and t.cr_sta_date >= to_date('"+dateStartOne+"','yyyy-MM-DD') \n");
		}
		if(StringUtil.notNull(dateStartTwo)){
			sql.append(" and t.cr_sta_date <= to_date('"+dateStartTwo+" 23:59:59','yyyy-MM-dd hh24:mi:ss') \n");
		}
		if(StringUtil.notNull(number)){
			sql.append(" and t.cr_number like '%"+number+"%'\n");
		}
		if(StringUtil.notNull(inComeType)){
			sql.append(" and t.cr_income_type = "+inComeType+"\n");
		}
		if(StringUtil.notNull(callType)){
			sql.append(" and t.cr_call_type = "+callType+"\n");
		}
		if(StringUtil.notNull(point)){
			sql.append(" and t.cr_points = "+point+"\n");
		}
		// 艾春 2013.11.30 添加未接电话查询条件过滤
		if(StringUtil.notNull(missedType)){
			sql.append(" and t.CR_MISSED_TYPE = "+missedType+"\n");
		}
		// 艾春 2013.11.30 添加未接电话查询条件过滤
		sql.append(" order by t.cr_sta_date desc ");
		
		return (PageResult<Map<String, Object>>)this.pageQuery(sql.toString(),
				null,
				this.getFunName(),
				pageSize,
				curPage);
	}
	
	public List<TtCrmCallRecordPO> getnullcr_ext()
	{
		StringBuffer sql= new StringBuffer();
		sql.append("select * from tt_crm_call_record t where t.CR_EXT is null");
		List<TtCrmCallRecordPO> list= dao.select(TtCrmCallRecordPO.class, sql.toString(), null);
		return list;
	}
	
	public List<Map<String, Object>> queryCallRecord(String account,String ext,String dateStartOne,String dateStartTwo,
			String number,String inComeType,String callType,String point,String missedType){
		
		StringBuffer sql = new StringBuffer();
		sql.append("select t.cr_number CRNUMBER,t.cr_sta_date STADATE,t.cr_end_date ENDDATE, \n");
		sql.append(" t.CR_TALK_TIME TALKTIME, to_char(t.CR_RING_TIME,'HH24:MI:SS') RINGTIME, to_char(t.CR_WAIT_TIME,'HH24:MI:SS') WAITTIME ,ts.se_seats_no SEATSNO,t.cr_ext EXT,t.CR_MOVE_NUMBER MOVENUMBER, \n");
		sql.append(" t.cr_call_type CALLTYPE, t.cr_income_type INCOMETYPE,t.cr_points POINTS,t.CR_RECORD_ADDR CR_RECORDADDR \n");
		sql.append(" from TT_CRM_CALL_RECORD t left join TT_CRM_SEATS ts on t.se_id = ts.se_id \n");
		sql.append(" where 1=1 \n");

		if(StringUtil.notNull(account)){
			sql.append(" and ts.se_account like '%"+account+"%'\n");
		}
		if(StringUtil.notNull(ext)){
			sql.append(" and cr_ext like '%"+ext+"%'\n");
		}
		if(StringUtil.notNull(dateStartOne)){
			sql.append(" and t.cr_sta_date >= to_date('"+dateStartOne+"','yyyy-MM-DD') \n");
		}
		if(StringUtil.notNull(dateStartTwo)){
			sql.append(" and t.cr_sta_date <= to_date('"+dateStartTwo+" 23:59:59','yyyy-MM-dd hh24:mi:ss') \n");
		}
		if(StringUtil.notNull(number)){
			sql.append(" and t.cr_number like '%"+number+"%'\n");
		}
		if(StringUtil.notNull(inComeType)){
			sql.append(" and t.cr_income_type = '"+inComeType+"'\n");
		}
		if(StringUtil.notNull(callType)){
			sql.append(" and t.cr_call_type = '"+callType+"'\n");
		}	
		if(StringUtil.notNull(point)){
			sql.append(" and t.cr_points = '"+point+"'\n");
		}
		// 艾春 2013.11.30 添加未接电话查询条件过滤
		if(StringUtil.notNull(missedType)){
			sql.append(" and t.CR_MISSED_TYPE = "+missedType+"\n");
		}
		// 艾春 2013.11.30 添加未接电话查询条件过滤
		
		return pageQuery(sql.toString(), null, getFunName());
	}
	//坐席业务看板
	public PageResult<Map<String, Object>> querySeatsInformation(
			Integer pageSizeMax, int curPage) {
		StringBuffer sbSql = new StringBuffer();
		// 艾春 9.24 修改 坐席业务看板
		sbSql.append("SELECT E.ACNT ACNT,\n");
		sbSql.append("       E.NAME NAME,\n"); 
		sbSql.append("       D.LOGIN_DATE LOGINDATE,\n"); 
		sbSql.append("       F.CODE_DESC GENDER,\n"); 
		sbSql.append("       COUNT(1) TIMES,\n"); 
		sbSql.append("       H.CODE_DESC WORKSTATUS,\n"); 
		sbSql.append("       DECODE(LENGTH(TRUNC(SUM(TO_CHAR(A.CR_TALK_TIME, 'HH24') * 3600 + TO_CHAR(A.CR_TALK_TIME, 'MI') * 60 + TO_CHAR(A.CR_TALK_TIME, 'SS')) / 3600)), 1, '0', '') ||\n"); 
		sbSql.append("       TRUNC(SUM(TO_CHAR(A.CR_TALK_TIME, 'HH24') * 3600 + TO_CHAR(A.CR_TALK_TIME, 'MI') * 60 + TO_CHAR(A.CR_TALK_TIME, 'SS')) / 3600) || ':' ||\n"); 
		sbSql.append("       DECODE(LENGTH(TRUNC(MOD(SUM(TO_CHAR(A.CR_TALK_TIME, 'HH24') * 3600 + TO_CHAR(A.CR_TALK_TIME, 'MI') * 60 + TO_CHAR(A.CR_TALK_TIME, 'SS')), 3600) / 60)), 1, '0', '') ||\n"); 
		sbSql.append("       TRUNC(MOD(SUM(TO_CHAR(A.CR_TALK_TIME, 'HH24') * 3600 + TO_CHAR(A.CR_TALK_TIME, 'MI') * 60 + TO_CHAR(A.CR_TALK_TIME, 'SS')), 3600) / 60) || ':' ||\n"); 
		sbSql.append("       DECODE(LENGTH(MOD(SUM(TO_CHAR(A.CR_TALK_TIME, 'HH24') * 3600 + TO_CHAR(A.CR_TALK_TIME, 'MI') * 60 + TO_CHAR(A.CR_TALK_TIME, 'SS')), 60)), 1, '0', '')||\n"); 
		sbSql.append("       MOD(SUM(TO_CHAR(A.CR_TALK_TIME, 'HH24') * 3600 + TO_CHAR(A.CR_TALK_TIME, 'MI') * 60 + TO_CHAR(A.CR_TALK_TIME, 'SS')), 60) TALKTIME,\n"); 
		sbSql.append("       A.SE_ID SEID\n"); 
		sbSql.append("  FROM TT_CRM_CALL_RECORD A\n"); 
		sbSql.append("  LEFT JOIN TT_CRM_SEATS B ON A.SE_ID = B.SE_ID\n"); 
		// 艾春 13.11.22 修改 坐席关联登录日志 添加当日关联条件AND TRUNC(A.CR_STA_DATE) = D.LOG_DAYS
		sbSql.append("  LEFT JOIN (SELECT MAX(C.LOGIN_DATE) LOGIN_DATE, C.USER_ID, C.LOG_DAYS\n"); 
		sbSql.append("               FROM TT_CRM_SEATS_LOG C\n"); 
		sbSql.append("              GROUP BY C.USER_ID, C.LOG_DAYS) D ON B.SE_USER_ID = D.USER_ID AND TRUNC(A.CR_STA_DATE) = D.LOG_DAYS\n"); 
		// 艾春 13.11.22 修改 坐席关联登录日志AND TRUNC(A.CR_STA_DATE) = D.LOG_DAYS
		sbSql.append("  LEFT JOIN TC_USER E ON E.USER_ID = B.SE_USER_ID\n"); 
		sbSql.append("  LEFT JOIN TC_CODE F ON F.CODE_ID = E.GENDER\n"); 
		sbSql.append("  LEFT JOIN TC_CODE H ON H.CODE_ID = B.SE_WORK_STATUS\n"); 
		sbSql.append(" WHERE TRUNC(A.CR_STA_DATE) = TRUNC(SYSDATE)\n"); 
		sbSql.append(" GROUP BY E.ACNT, E.NAME, D.LOGIN_DATE, F.CODE_DESC, H.CODE_DESC, A.SE_ID\n"); 
		sbSql.append(" ORDER BY 7 DESC");

		return (PageResult<Map<String, Object>>)this.pageQuery(sbSql.toString(),
				null,
				this.getFunName(),
				pageSizeMax,
				curPage);
	}
	
	
	public List<Map<String,Object>> getSeatsInformationList(long seid){
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("select t.se_id SEID,to_char(T.CR_TALK_TIME ,'yyyy-MM-dd HH24:mm:ss') TALKTIME \r\n");
		sbSql.append("  from TT_CRM_CALL_RECORD t\r\n");
		sbSql.append(" where t.se_id = "+seid+"\r\n");
		sbSql.append("   and trunc(t.cr_sta_date) = trunc(sysdate)\r\n");
//		sbSql.append("   and trunc(t.cr_end_date) = trunc(sysdate)"); 
		return (List<Map<String, Object>>)this.pageQuery(sbSql.toString(),
				null,
				this.getFunName());
	}

	public PageResult<Map<String, Object>> queryNoAcntCallRecord(
			String dateStartOne, String dateStartTwo, String inComeType,
			String callType, String point, Integer pageSize, Integer curPage) {
		StringBuffer sbsql= new StringBuffer();
		sbsql.append("SELECT T.AUTOID,F.AUTOID f_AUTOID, F_GETID CR_ID,\n" );
		sbsql.append("       ---S.SE_ID SE_ID,\n" );
		sbsql.append("       DECODE(LENGTH(f.CALLER),4,NVL(f.CALLED,0),f.CALLER) CR_NUMBER,\n" );
		sbsql.append("       -- 修改开始时间\n" );
		sbsql.append("       TO_DATE(SUBSTR(F.SDATE,0,4) || '-' ||SUBSTR(F.SDATE,5,2) || '-' ||SUBSTR(F.SDATE,7,2)|| SUBSTR(F.STIME,0,2)|| ':' ||SUBSTR(F.STIME,3,2)|| ':' ||SUBSTR(F.STIME,5,2), 'YYYY-MM-DD HH24:MI:SS') CR_STA_DATE,\n" );
		sbsql.append("       --修改结束时间\n" );
		sbsql.append("       TO_DATE(SUBSTR(F.SDATE,0,4) || '-' ||SUBSTR(F.SDATE,5,2) || '-' ||SUBSTR(F.SDATE,7,2) || SUBSTR(F.ETIME,0,2)|| ':' ||SUBSTR(F.ETIME,3,2)|| ':' ||SUBSTR(F.ETIME,5,2), 'YYYY-MM-DD HH24:MI:SS') CR_END_DATE,\n" );
		sbsql.append("       --修改通话时长\n" );
		sbsql.append("       TRUNC(F.LEN_TALK / 3600) || ':' || TRUNC(MOD(F.LEN_TALK, 3600) / 60) || ':' || MOD(F.LEN_TALK, 60)  CR_TALK_TIME,\n" );
		sbsql.append("       TRUNC(F.LEN_RING / 3600) || ':' || TRUNC(MOD(F.LEN_RING, 3600) / 60) || ':' || MOD(F.LEN_RING, 60)  CR_RING_TIME,\n" );
		sbsql.append("       f.ext CR_EXT,\n" );
		sbsql.append("       DECODE(F.DIRECTION, 1, 95101001, 3, 95101001, 2, 95101002 , 4 , 95101002,null) CR_CALL_TYPE,\n" );
		sbsql.append("       DECODE(T.INFO3,'3',95111001,'2',95111002,'1',95111003,NULL)   CR_POINTS,\n" );
		sbsql.append("       --修改未接和已接\n" );
		sbsql.append("       CASE WHEN F.DIRECTION = 1 and F.LEN_TALK = 0 THEN 95091001\n" );
		sbsql.append("            ELSE  95091002 END  CR_INCOME_TYPE,\n" );
		sbsql.append("       --DECODE(T.MSG_TYPE, 11, 95091001, 95091002) CR_INCOME_TYPE,\n" );
		sbsql.append("       CASE WHEN F.DIRECTION = 1 and F.LEN_TALK = 0 then null else T.FILE_PATH end CR_RECORD_ADDR,\n" );
		sbsql.append("       SYSDATE CREATE_DATE,\n" );
		sbsql.append("       F.CALLID\n" );
		sbsql.append("  FROM CTS_HD F\n" );
		sbsql.append("  --join TT_CRM_SEATS S on f.GHID = s.SE_SEATS_NO\n" );
		sbsql.append("  left join  (SELECT A.AUTOID, A.CALLER, A.CALLED, A.MSG_TYPE,\n" );
		sbsql.append("               A.UCID, A.INFO3, A.FILE_PATH, A.OPID,\n" );
		sbsql.append("        DENSE_RANK() OVER(PARTITION BY A.UCID, a.CALLER, a.CALLED ORDER BY A.LEN DESC, A.AUTOID) R\n" );
		sbsql.append("        from LOG_TELHD A where a.flag = 0) T on T.UCID = F.CALLID  and f.CALLER = t.caller and f.CALLED = t.called AND T.R =  1\n" );
		sbsql.append(" WHERE f.falg = 0 and f.DEST_TYPE = 3 and f.GHID is null\n" );
		sbsql.append("   and f.DIRECTION in (1, 2) ");


		if(StringUtil.notNull(dateStartOne)){
			sbsql.append(" and TO_DATE(substr(F.SDATE,0,4) || '-' ||substr(F.SDATE,5,2) || '-' ||substr(F.SDATE,7,2) , 'yyyy-mm-dd') >= to_date('"+dateStartOne+"','yyyy-MM-DD') \n");
		}
		if(StringUtil.notNull(dateStartTwo)){
			sbsql.append(" and TO_DATE(substr(F.SDATE,0,4) || '-' ||substr(F.SDATE,5,2) || '-' ||substr(F.SDATE,7,2)|| substr(F.STIME,0,2)|| ':' ||substr(F.STIME,3,2)|| ':' ||substr(F.STIME,5,2), 'yyyy-mm-dd hh24:mi:ss') <= to_date('"+dateStartTwo+" 23:59:59','yyyy-MM-dd hh24:mi:ss') \n");
		}

		if(StringUtil.notNull(inComeType)){
			sbsql.append(" and CASE WHEN F.DIRECTION = 1 and F.LEN_TALK = 0 THEN 95091001\n" );
			sbsql.append("          ELSE  95091002 END   = ");
			sbsql.append("  '"+inComeType+"'\n");
		}
		if(StringUtil.notNull(callType)){
			sbsql.append(" and   DECODE(F.DIRECTION, 1, 95101001, 3, 95101001, 2, 95101002 , 4 , 95101002,null) = '"+callType+"'\n");

		}
		if(StringUtil.notNull(point)){
			sbsql.append(" and DECODE(T.INFO3,'3',"+Constant.PLEASED+",'2',"+Constant.GENERAL+","+Constant.YAWP+") = '"+point+"'\n");
		}
		sbsql.append(" order by TO_DATE(SUBSTR(F.SDATE, 0, 4) || '-' || SUBSTR(F.SDATE, 5, 2) || '-' ||\n" );
		sbsql.append("               SUBSTR(F.SDATE, 7, 2) || SUBSTR(F.STIME, 0, 2) || ':' ||\n" );
		sbsql.append("               SUBSTR(F.STIME, 3, 2) || ':' || SUBSTR(F.STIME, 5, 2),\n" );
		sbsql.append("               'YYYY-MM-DD HH24:MI:SS') desc");

		
		return (PageResult<Map<String, Object>>)this.pageQuery(sbsql.toString(),
				null,
				this.getFunName(),
				pageSize,
				curPage);
	}

	public List<Map<String, Object>> queryNoAcntCallRecord(String dateStartOne,
			String dateStartTwo, String inComeType, String callType,
			String point) {
		StringBuffer sbsql = new StringBuffer();
		
		sbsql.append("SELECT T.AUTOID,F.AUTOID f_AUTOID, F_GETID CR_ID,\n" );
		sbsql.append("       ---S.SE_ID SE_ID,\n" );
		sbsql.append("       DECODE(LENGTH(f.CALLER),4,NVL(f.CALLED,0),f.CALLER) CR_NUMBER,\n" );
		sbsql.append("       -- 修改开始时间\n" );
		sbsql.append("       TO_DATE(SUBSTR(F.SDATE,0,4) || '-' ||SUBSTR(F.SDATE,5,2) || '-' ||SUBSTR(F.SDATE,7,2)|| SUBSTR(F.STIME,0,2)|| ':' ||SUBSTR(F.STIME,3,2)|| ':' ||SUBSTR(F.STIME,5,2), 'YYYY-MM-DD HH24:MI:SS') CR_STA_DATE,\n" );
		sbsql.append("       --修改结束时间\n" );
		sbsql.append("       TO_DATE(SUBSTR(F.SDATE,0,4) || '-' ||SUBSTR(F.SDATE,5,2) || '-' ||SUBSTR(F.SDATE,7,2) || SUBSTR(F.ETIME,0,2)|| ':' ||SUBSTR(F.ETIME,3,2)|| ':' ||SUBSTR(F.ETIME,5,2), 'YYYY-MM-DD HH24:MI:SS') CR_END_DATE,\n" );
		sbsql.append("       --修改通话时长\n" );
		sbsql.append("       TRUNC(F.LEN_TALK / 3600) || ':' || TRUNC(MOD(F.LEN_TALK, 3600) / 60) || ':' || MOD(F.LEN_TALK, 60)  CR_TALK_TIME,\n" );
		sbsql.append("       TRUNC(F.LEN_RING / 3600) || ':' || TRUNC(MOD(F.LEN_RING, 3600) / 60) || ':' || MOD(F.LEN_RING, 60)  CR_RING_TIME,\n" );
		sbsql.append("       f.ext CR_EXT,\n" );
		sbsql.append("       DECODE(F.DIRECTION, 1, 95101001, 3, 95101001, 2, 95101002 , 4 , 95101002,null) CR_CALL_TYPE,\n" );
		sbsql.append("       DECODE(T.INFO3,'3',95111001,'2',95111002,'1',95111003,NULL)   CR_POINTS,\n" );
		sbsql.append("       --修改未接和已接\n" );
		sbsql.append("       CASE WHEN F.DIRECTION = 1 and F.LEN_TALK = 0 THEN 95091001\n" );
		sbsql.append("            ELSE  95091002 END  CR_INCOME_TYPE,\n" );
		sbsql.append("       --DECODE(T.MSG_TYPE, 11, 95091001, 95091002) CR_INCOME_TYPE,\n" );
		sbsql.append("       CASE WHEN F.DIRECTION = 1 and F.LEN_TALK = 0 then null else T.FILE_PATH end CR_RECORD_ADDR,\n" );
		sbsql.append("       SYSDATE CREATE_DATE,\n" );
		sbsql.append("       F.CALLID\n" );
		sbsql.append("  FROM CTS_HD F\n" );
		sbsql.append("  --join TT_CRM_SEATS S on f.GHID = s.SE_SEATS_NO\n" );
		sbsql.append("  left join  (SELECT A.AUTOID, A.CALLER, A.CALLED, A.MSG_TYPE,\n" );
		sbsql.append("               A.UCID, A.INFO3, A.FILE_PATH, A.OPID,\n" );
		sbsql.append("        DENSE_RANK() OVER(PARTITION BY A.UCID, a.CALLER, a.CALLED ORDER BY A.LEN DESC, A.AUTOID) R\n" );
		sbsql.append("        from LOG_TELHD A where a.flag = 0) T on T.UCID = F.CALLID  and f.CALLER = t.caller and f.CALLED = t.called AND T.R =  1\n" );
		sbsql.append(" WHERE f.falg = 0 and f.DEST_TYPE = 3 and f.GHID is null\n" );
		sbsql.append("   and f.DIRECTION in (1, 2) ");


		if(StringUtil.notNull(dateStartOne)){
			sbsql.append(" and TO_DATE(substr(F.SDATE,0,4) || '-' ||substr(F.SDATE,5,2) || '-' ||substr(F.SDATE,7,2) , 'yyyy-mm-dd') >= to_date('"+dateStartOne+"','yyyy-MM-DD') \n");
		}
		if(StringUtil.notNull(dateStartTwo)){
			sbsql.append(" and TO_DATE(substr(F.SDATE,0,4) || '-' ||substr(F.SDATE,5,2) || '-' ||substr(F.SDATE,7,2)|| substr(F.STIME,0,2)|| ':' ||substr(F.STIME,3,2)|| ':' ||substr(F.STIME,5,2), 'yyyy-mm-dd hh24:mi:ss') <= to_date('"+dateStartTwo+" 23:59:59','yyyy-MM-dd hh24:mi:ss') \n");
		}

		if(StringUtil.notNull(inComeType)){
			sbsql.append(" and CASE WHEN F.DIRECTION = 1 and F.LEN_TALK = 0 THEN 95091001\n" );
			sbsql.append("          ELSE  95091002 END   = ");
			sbsql.append("  '"+inComeType+"'\n");
		}
		if(StringUtil.notNull(callType)){
			sbsql.append(" and   DECODE(F.DIRECTION, 1, 95101001, 3, 95101001, 2, 95101002 , 4 , 95101002,null) = '"+callType+"'\n");

		}
		if(StringUtil.notNull(point)){
			sbsql.append(" and DECODE(T.INFO3,'3',"+Constant.PLEASED+",'2',"+Constant.GENERAL+","+Constant.YAWP+") = '"+point+"'\n");
		}
		sbsql.append(" order by TO_DATE(SUBSTR(F.SDATE, 0, 4) || '-' || SUBSTR(F.SDATE, 5, 2) || '-' ||\n" );
		sbsql.append("               SUBSTR(F.SDATE, 7, 2) || SUBSTR(F.STIME, 0, 2) || ':' ||\n" );
		sbsql.append("               SUBSTR(F.STIME, 3, 2) || ':' || SUBSTR(F.STIME, 5, 2),\n" );
		sbsql.append("               'YYYY-MM-DD HH24:MI:SS') desc");
		
		return pageQuery(sbsql.toString(), null, getFunName());
	}


}
