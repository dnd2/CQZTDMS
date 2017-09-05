package com.infodms.dms.dao.crm.report;

import java.sql.ResultSet;
import java.util.Map;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.dao.crm.order.OrderManageDao;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

public class ReportDao extends BaseDao<PO>{
private static final ReportDao dao = new ReportDao();
private ActionContext act = ActionContext.getContext();
RequestWrapper request = act.getRequest();
	public static final ReportDao getInstance() {
		return dao;
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		return null;
	}
	
	/**
	 * 每日销售简报查询
	 */
	public PageResult<Map<String, Object>> dailySalesReportQuery(
			String dealerCodes,String startDate,String endDate,
			int curPage, int pageSize) {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		StringBuffer sbSql = new StringBuffer();
		
		sbSql.append("select vw.ROOT_ORG_NAME,vw.PQ_ORG_NAME,vw.DEALER_CODE,\r\n");
		sbSql.append("       case when f1.dname is not null then f1.dname else(case when f2.dname is not null then f2.dname else(case when f3.name is not null then f3.name else (case when f4.name is not null then f4.name else (case when f5.name is not null then f5.name else f1.dname end) end) end ) end ) end as dname,\r\n");
		sbSql.append("       decode(f1.xscount, null, 0, f1.xscount) as leadsSum,\r\n");
		sbSql.append("       decode(f2.skcount, null, 0, f2.skcount) as firstSum,\r\n");
		sbSql.append("       decode(f3.yycount, null, 0, f3.yycount) as inviteSum,\r\n");
		sbSql.append("       decode(f4.ddcount, null, 0, f4.ddcount) as orderSum,\r\n");
		sbSql.append("       decode(f5.jccount, null, 0, f5.jccount) as deliverySum\r\n");
		sbSql.append("  from (select to_char(b.dealer_id) as dealer_id, max(c.dealer_name) as dname, count(1) as xscount --线索统计\r\n");
		sbSql.append("          from t_pc_leads a, t_pc_leads_allot b\r\n");
		sbSql.append("          left join tm_dealer c\r\n");
		sbSql.append("            on b.dealer_id = c.dealer_id\r\n");
		sbSql.append("         where a.leads_code = b.leads_code\r\n");
		sbSql.append("           and b.allot_again = 10041002\r\n");
		sbSql.append("           and a.leads_status in (60161001,60161002,60161003)\r\n");
		sbSql.append("           and b.dealer_id is not null\r\n");
		if(startDate!=null&&!"".equals(startDate)){
			sbSql.append("       and b.allot_dealer_date >= to_date('"+startDate+" 00:00:00','yyyy-MM-dd HH24:mi:ss') \r\n");
		}
		if(endDate!=null&&!"".equals(endDate)){
			sbSql.append("       and b.allot_dealer_date <= to_date('"+endDate+" 23:59:59','yyyy-MM-dd HH24:mi:ss') \r\n");
		}
		sbSql.append("         group by b.dealer_id) f1\r\n");
		sbSql.append("  full join (select to_char(b1.dealer_id) as dealer_id,\r\n");
		sbSql.append("                    max(c1.dealer_name) as dname,\r\n");
		sbSql.append("                    count(1) as skcount --首客统计\r\n");
		sbSql.append("               from t_pc_leads a1, t_pc_leads_allot b1\r\n");
		sbSql.append("               left join tm_dealer c1\r\n");
		sbSql.append("                 on b1.dealer_id = c1.dealer_id\r\n");
		sbSql.append("              where a1.leads_code = b1.leads_code\r\n");
		sbSql.append("                and b1.allot_again = 10041002\r\n");
		sbSql.append("                and a1.leads_status in (60161001,60161002,60161003)\r\n");
		sbSql.append("                and b1.dealer_id is not null\r\n");
		sbSql.append("                and b1.if_confirm = 60321002\r\n");
		sbSql.append("                and a1.jc_way in ('60021001', '60021003', '60021004', '60021008')\r\n");
		if(startDate!=null&&!"".equals(startDate)){
			sbSql.append("       and b1.confirm_date >= to_date('"+startDate+" 00:00:00','yyyy-MM-dd HH24:mi:ss') \r\n");
		}
		if(endDate!=null&&!"".equals(endDate)){
			sbSql.append("       and b1.confirm_date <= to_date('"+endDate+" 23:59:59','yyyy-MM-dd HH24:mi:ss') \r\n");
		}
		sbSql.append("              group by b1.dealer_id) f2\r\n");
		sbSql.append("    on case when f1.dealer_id is not null then f1.dealer_id else(case when f2.dealer_id is not null then f2.dealer_id else f1.dealer_id end ) end = f2.dealer_id\r\n");
		sbSql.append("  full join (select to_char(b1.dealer_id) as dealer_id,\r\n");
		sbSql.append("                    max(c1.dealer_name) as name,\r\n");
		sbSql.append("                    count(1) as yycount --邀约统计\r\n");
		sbSql.append("               from t_pc_leads a1, t_pc_leads_allot b1\r\n");
		sbSql.append("               left join tm_dealer c1\r\n");
		sbSql.append("                 on b1.dealer_id = c1.dealer_id\r\n");
		sbSql.append("              where a1.leads_code = b1.leads_code\r\n");
		sbSql.append("                and b1.allot_again = 10041002\r\n");
		sbSql.append("                and a1.leads_status in (60161001,60161002,60161003)\r\n");
		sbSql.append("                and b1.dealer_id is not null\r\n");
		sbSql.append("                and b1.if_confirm = 60321002\r\n");
		sbSql.append("                and a1.jc_way in ('60021002')\r\n");
		if(startDate!=null&&!"".equals(startDate)){
			sbSql.append("       and b1.confirm_date >= to_date('"+startDate+" 00:00:00','yyyy-MM-dd HH24:mi:ss') \r\n");
		}
		if(endDate!=null&&!"".equals(endDate)){
			sbSql.append("       and b1.confirm_date <= to_date('"+endDate+" 23:59:59','yyyy-MM-dd HH24:mi:ss') \r\n");
		}
		sbSql.append("              group by b1.dealer_id) f3\r\n");
		sbSql.append("    on case when f1.dealer_id is not null then f1.dealer_id else(case when f2.dealer_id is not null then f2.dealer_id else(case when f3.dealer_id is not null then f3.dealer_id else f1.dealer_id end ) end ) end = f3.dealer_id\r\n");
		sbSql.append("  full join (select to_char(b.dealer_id) as dealer_id,\r\n");
		sbSql.append("                    max(c.dealer_name) as name,\r\n");
		sbSql.append("                    count(1) as ddcount --订单统计\r\n");
		sbSql.append("               from t_pc_order a, t_pc_customer b\r\n");
		sbSql.append("               left join tm_dealer c\r\n");
		sbSql.append("                 on b.dealer_id = c.dealer_id\r\n");
		sbSql.append("              where a.order_status in\r\n");
		sbSql.append("                    (60231001, 60231002, 60231003, 60231004, 60231005)\r\n");
		sbSql.append("                and a.task_status = 60171002\r\n");
		sbSql.append("                and a.customer_id = b.customer_id\r\n");
		if(startDate!=null&&!"".equals(startDate)){
			sbSql.append("       and a.finish_date >= to_date('"+startDate+" 00:00:00','yyyy-MM-dd HH24:mi:ss') \r\n");
		}
		if(endDate!=null&&!"".equals(endDate)){
			sbSql.append("       and a.finish_date <= to_date('"+endDate+" 23:59:59','yyyy-MM-dd HH24:mi:ss') \r\n");
		}
		sbSql.append("              group by b.dealer_id) f4\r\n");
		sbSql.append("    on case when f1.dealer_id is not null then f1.dealer_id else(case when f2.dealer_id is not null then f2.dealer_id else(case when f3.dealer_id is not null then f3.dealer_id else (case when f4.dealer_id is not null then f4.dealer_id else f1.dealer_id end) end ) end ) end = f4.dealer_id\r\n");
		sbSql.append("  full join (select to_char(b.dealer_id) as dealer_id,\r\n");
		sbSql.append("                    max(c.dealer_name) as name,\r\n");
		sbSql.append("                    count(1) as jccount --交车统计\r\n");
		sbSql.append("               from t_pc_delvy a, t_pc_customer b\r\n");
		sbSql.append("               left join tm_dealer c\r\n");
		sbSql.append("                 on b.dealer_id = c.dealer_id\r\n");
		sbSql.append("              where a.status = 10011001\r\n");
		sbSql.append("                and a.customer_id = b.customer_id\r\n");
		if(startDate!=null&&!"".equals(startDate)){
			sbSql.append("       and a.DELIVERY_DATE >= to_date('"+startDate+" 00:00:00','yyyy-MM-dd HH24:mi:ss') \r\n");
		}
		if(endDate!=null&&!"".equals(endDate)){
			sbSql.append("       and a.DELIVERY_DATE <= to_date('"+endDate+" 23:59:59','yyyy-MM-dd HH24:mi:ss') \r\n");
		}
		sbSql.append("              group by b.dealer_id) f5\r\n");
		sbSql.append("    on case when f1.dealer_id is not null then f1.dealer_id else(case when f2.dealer_id is not null then f2.dealer_id else(case when f3.dealer_id is not null then f3.dealer_id else (case when f4.dealer_id is not null then f4.dealer_id else (case when f5.dealer_id is not null then f5.dealer_id else f1.dealer_id end) end) end ) end ) end = f5.dealer_id ");
		sbSql.append("    LEFT join VW_ORG_DEALER vw on case when f1.dealer_id is not null then f1.dealer_id else(case when f2.dealer_id is not null then f2.dealer_id else(case when f3.dealer_id is not null then f3.dealer_id else (case when f4.dealer_id is not null then f4.dealer_id else (case when f5.dealer_id is not null then f5.dealer_id else f1.dealer_id end) end) end ) end ) end = vw.DEALER_ID where 1=1 ");

		// 大区机构人员变动查询
		if ("10431003".equals(logonUser.getDutyType())) {
			sbSql.append(" AND case when f1.dealer_id is not null then f1.dealer_id else(case when f2.dealer_id is not null then f2.dealer_id else(case when f3.dealer_id is not null then f3.dealer_id else (case when f4.dealer_id is not null then f4.dealer_id else (case when f5.dealer_id is not null then f5.dealer_id else f1.dealer_id end) end) end ) end ) end IN");
			sbSql.append("  (SELECT VW.DEALER_ID FROM vw_org_dealer VW\n");
			sbSql.append(" WHERE VW.ROOT_ORG_ID="+logonUser.getOrgId()+")");
			// 小区机构人员变动查询
		} else if ("10431004".equals(logonUser.getDutyType())) {
			sbSql.append(" AND case when f1.dealer_id is not null then f1.dealer_id else(case when f2.dealer_id is not null then f2.dealer_id else(case when f3.dealer_id is not null then f3.dealer_id else (case when f4.dealer_id is not null then f4.dealer_id else (case when f5.dealer_id is not null then f5.dealer_id else f1.dealer_id end) end) end ) end ) end IN");
			sbSql.append("  (SELECT VW.DEALER_ID FROM vw_org_dealer VW\n");
			sbSql.append(" WHERE VW.PQ_ORG_ID="+logonUser.getOrgId()+")");
		}
		if(dealerCodes!=null&&!"".equals(dealerCodes)){
			boolean flag=dealerCodes.contains(",");
			if(flag){
				dealerCodes = dealerCodes.replace(",", "','");
			}
			sbSql.append("  AND  case when f1.dealer_id is not null then f1.dealer_id else(case when f2.dealer_id is not null then f2.dealer_id else(case when f3.dealer_id is not null then f3.dealer_id else (case when f4.dealer_id is not null then f4.dealer_id else (case when f5.dealer_id is not null then f5.dealer_id else f1.dealer_id end) end) end ) end ) end IN (SELECT TD1.DEALER_ID\n" );
			sbSql.append("                         FROM TM_DEALER TD1\n" );
			sbSql.append("                        WHERE 1 = 1\n" );
			sbSql.append("                        START WITH TD1.DEALER_CODE IN ('"+dealerCodes+"')\n" );
			sbSql.append("                       CONNECT BY PRIOR TD1.DEALER_ID = TD1.PARENT_DEALER_D\n" );
			sbSql.append("                       )\n" );
		}
		sbSql.append(" order by vw.ROOT_ORG_name,vw.PQ_ORG_name,vw.DEALER_ID");
		PageResult<Map<String, Object>> ps = pageQuery(sbSql.toString(), null, getFunName(), pageSize, curPage);
		return ps;
	}
	
	/**
	 * 建档客户分布表查询
	 */
	public PageResult<Map<String, Object>> ctmDistributeReportQuery(
			String dealerCodes,String startDate,String endDate,
			int curPage, int pageSize) {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		StringBuffer sbSql = new StringBuffer();
		
		sbSql.append("select max(vw.ROOT_ORG_NAME) as ROOT_ORG_NAME,max(vw.PQ_ORG_NAME) as PQ_ORG_NAME,max(vw.DEALER_CODE) as DEALER_CODE,max(vw.DEALER_NAME) as DEALER_NAME,\r\n");
		sbSql.append("       sum(decode(d.new_level, 60101005, 1, 0)) as O,\r\n");
		sbSql.append("       sum(decode(d.new_level, 60101001, 1, 0)) as H,\r\n");
		sbSql.append("       sum(decode(d.new_level, 60101002, 1, 0)) as A,\r\n");
		sbSql.append("       sum(decode(d.new_level, 60101003, 1, 0)) as B,\r\n");
		sbSql.append("       sum(decode(d.new_level, 60101004, 1, 0)) as C,\r\n");
		sbSql.append("       sum(decode(d.new_level, 60101006, 1, 0)) as E,\r\n");
		sbSql.append("       sum(decode(d.new_level, 60101007, 1, 0)) as L\r\n");
		sbSql.append(" from (\r\n");
		sbSql.append("select c.customer_id,\r\n");
		sbSql.append("       substr(c.finish_date, 0, 10) as finish_date,\r\n");
		sbSql.append("       substr(c.finish_date, 11) as new_level\r\n");
		sbSql.append("  from (select b.customer_id, max(b.finish_date) as finish_date\r\n");
		sbSql.append("          from (select a1.customer_id,\r\n");
		sbSql.append("                       to_char(a1.finish_date, 'yyyy-MM-dd') || '' ||\r\n");
		sbSql.append("                       a1.new_level finish_date\r\n");
		sbSql.append("                  from t_pc_follow a1\r\n");
		sbSql.append("                 where a1.task_status = 60171002\r\n");
		sbSql.append("                union all\r\n");
		sbSql.append("                select a2.customer_id,\r\n");
		sbSql.append("                       to_char(a2.finish_date, 'yyyy-MM-dd') || '' ||\r\n");
		sbSql.append("                       a2.new_level finish_date\r\n");
		sbSql.append("                  from t_pc_invite a2\r\n");
		sbSql.append("                 where a2.task_status = 60171002\r\n");
		sbSql.append("                union all\r\n");
		sbSql.append("                select a3.customer_id,\r\n");
		sbSql.append("                       to_char(a3.finish_date, 'yyyy-MM-dd') || '' ||\r\n");
		sbSql.append("                       a3.new_level finish_date\r\n");
		sbSql.append("                  from t_pc_invite_shop a3\r\n");
		sbSql.append("                 where a3.task_status = 60171002\r\n");
		sbSql.append("                union all\r\n");
		sbSql.append("                select a4.customer_id,\r\n");
		sbSql.append("                       to_char(a4.finish_date, 'yyyy-MM-dd') || '' ||\r\n");
		sbSql.append("                       a4.new_level finish_date\r\n");
		sbSql.append("                  from t_pc_order a4\r\n");
		sbSql.append("                 where a4.task_status = 60171002) b\r\n");
		sbSql.append("         group by b.customer_id) c ) d left join t_pc_customer e on d.customer_id = e.customer_id\r\n");
		sbSql.append("         left join VW_ORG_DEALER vw on e.dealer_id = vw.DEALER_ID\r\n");
		sbSql.append("         where 1=1\r\n");
		if(startDate!=null&&!"".equals(startDate)){
			sbSql.append("         and d.finish_date >= '"+startDate+"'\r\n");
		}
		if(endDate!=null&&!"".equals(endDate)){
			sbSql.append("         and d.finish_date <= '"+endDate+"'\r\n");
		}
		// 大区机构人员变动查询
		if ("10431003".equals(logonUser.getDutyType())) {
			sbSql.append(" AND e.dealer_id IN");
			sbSql.append("  (SELECT VW.DEALER_ID FROM vw_org_dealer VW\n");
			sbSql.append(" WHERE VW.ROOT_ORG_ID="+logonUser.getOrgId()+")");
			// 小区机构人员变动查询
		} else if ("10431004".equals(logonUser.getDutyType())) {
			sbSql.append(" AND e.dealer_id IN");
			sbSql.append("  (SELECT VW.DEALER_ID FROM vw_org_dealer VW\n");
			sbSql.append(" WHERE VW.PQ_ORG_ID="+logonUser.getOrgId()+")");
		}
		if(dealerCodes!=null&&!"".equals(dealerCodes)){
			boolean flag=dealerCodes.contains(",");
			if(flag){
				dealerCodes = dealerCodes.replace(",", "','");
			}
			sbSql.append("  AND e.dealer_id IN (SELECT TD1.DEALER_ID\n" );
			sbSql.append("                         FROM TM_DEALER TD1\n" );
			sbSql.append("                        WHERE 1 = 1\n" );
			sbSql.append("                        START WITH TD1.DEALER_CODE IN ('"+dealerCodes+"')\n" );
			sbSql.append("                       CONNECT BY PRIOR TD1.DEALER_ID = TD1.PARENT_DEALER_D\n" );
			sbSql.append("                       )\n" );
		}
		sbSql.append("         group by e.dealer_id\r\n");
		sbSql.append("         order by max(vw.ROOT_ORG_NAME),max(vw.PQ_ORG_NAME),max(vw.DEALER_NAME)"); 

		PageResult<Map<String, Object>> ps = pageQuery(sbSql.toString(), null, getFunName(), pageSize, curPage);
		return ps;
	}
	
	/**
	 * 销售排名报表查询
	 */
	public PageResult<Map<String, Object>> salesRankingReportQuery(
			String dealerCodes,String startDate,String endDate,
			int curPage, int pageSize) {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		StringBuffer sbSql = new StringBuffer();
		
		sbSql.append("SELECT RANK() OVER(ORDER BY D.CUT DESC) AS RANKING, D.ROOT_ORG_NAME,D.PQ_ORG_NAME,D.DEALER_CODE,D.DEALER_NAME,D.CUT,D.DEALER_ID\r\n");
		sbSql.append("  FROM (SELECT MAX(VW.ROOT_ORG_NAME) AS ROOT_ORG_NAME,\r\n");
		sbSql.append("               MAX(VW.PQ_ORG_NAME) AS PQ_ORG_NAME,\r\n");
		sbSql.append("               MAX(VW.DEALER_CODE) AS DEALER_CODE,\r\n");
		sbSql.append("               MAX(VW.DEALER_NAME) AS DEALER_NAME,\r\n");
		sbSql.append("               B.DEALER_ID,\r\n");
		sbSql.append("               COUNT(1) AS CUT\r\n");
		sbSql.append("          FROM T_PC_DELVY A, T_PC_CUSTOMER B\r\n");
		sbSql.append("          LEFT JOIN VW_ORG_DEALER VW\r\n");
		sbSql.append("            ON B.DEALER_ID = VW.DEALER_ID\r\n");
		sbSql.append("         WHERE A.CUSTOMER_ID = B.CUSTOMER_ID\r\n");
		if(startDate!=null&&!"".equals(startDate)){
			sbSql.append("         and A.DELIVERY_DATE >= to_date('"+startDate+" 00:00:00','yyyy-MM-dd HH24:mi:ss')\r\n");
		}
		if(endDate!=null&&!"".equals(endDate)){
			sbSql.append("         and A.DELIVERY_DATE <= to_date('"+endDate+" 23:59:59','yyyy-MM-dd HH24:mi:ss')\r\n");
		}
		// 大区机构人员变动查询
		if ("10431003".equals(logonUser.getDutyType())) {
			sbSql.append(" AND b.dealer_id IN");
			sbSql.append("  (SELECT VW.DEALER_ID FROM vw_org_dealer VW\n");
			sbSql.append(" WHERE VW.ROOT_ORG_ID="+logonUser.getOrgId()+")");
			// 小区机构人员变动查询
		} else if ("10431004".equals(logonUser.getDutyType())) {
			sbSql.append(" AND b.dealer_id IN");
			sbSql.append("  (SELECT VW.DEALER_ID FROM vw_org_dealer VW\n");
			sbSql.append(" WHERE VW.PQ_ORG_ID="+logonUser.getOrgId()+")");
		}
		if(dealerCodes!=null&&!"".equals(dealerCodes)){
			boolean flag=dealerCodes.contains(",");
			if(flag){
				dealerCodes = dealerCodes.replace(",", "','");
			}
			sbSql.append("  AND b.dealer_id IN (SELECT TD1.DEALER_ID\n" );
			sbSql.append("                         FROM TM_DEALER TD1\n" );
			sbSql.append("                        WHERE 1 = 1\n" );
			sbSql.append("                        START WITH TD1.DEALER_CODE IN ('"+dealerCodes+"')\n" );
			sbSql.append("                       CONNECT BY PRIOR TD1.DEALER_ID = TD1.PARENT_DEALER_D\n" );
			sbSql.append("                       )\n" );
		}
		sbSql.append("           AND A.STATUS = 10011001\r\n");
		sbSql.append("         GROUP BY B.DEALER_ID) D"); 

		PageResult<Map<String, Object>> ps = pageQuery(sbSql.toString(), null, getFunName(), pageSize, curPage);
		return ps;
	}
	
	/**
	 * 销售排名详细查询
	 */
	public PageResult<Map<String, Object>> salesRankingDetailQuery(
			String dealerId,String startDate,String endDate,
			int curPage, int pageSize) {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		StringBuffer sbSql = new StringBuffer();
		
		sbSql.append("SELECT RANK() OVER(ORDER BY D.CUT DESC) AS RANKING, D.ADVISER, D.CUT\r\n");
		sbSql.append("  FROM (SELECT MAX(C.NAME) AS ADVISER, COUNT(1) AS CUT\r\n");
		sbSql.append("          FROM T_PC_DELVY A, T_PC_CUSTOMER B, TC_USER C\r\n");
		sbSql.append("         WHERE A.CUSTOMER_ID = B.CUSTOMER_ID\r\n");
		sbSql.append("           AND B.ADVISER = C.USER_ID\r\n");
		if(startDate!=null&&!"".equals(startDate)){
			sbSql.append("         and A.DELIVERY_DATE >= to_date('"+startDate+" 00:00:00','yyyy-MM-dd HH24:mi:ss')\r\n");
		}
		if(endDate!=null&&!"".equals(endDate)){
			sbSql.append("         and A.DELIVERY_DATE <= to_date('"+endDate+" 23:59:59','yyyy-MM-dd HH24:mi:ss')\r\n");
		}
		sbSql.append("           and B.dealer_id = '"+dealerId+"'\r\n");
		sbSql.append("           AND A.STATUS = 10011001\r\n");
		sbSql.append("         GROUP BY B.ADVISER) D"); 

		PageResult<Map<String, Object>> ps = pageQuery(sbSql.toString(), null, getFunName(), pageSize, curPage);
		return ps;
	}
}
