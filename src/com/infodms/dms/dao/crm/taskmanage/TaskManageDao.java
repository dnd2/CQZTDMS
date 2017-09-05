package com.infodms.dms.dao.crm.taskmanage;

import java.sql.ResultSet;
import java.util.Calendar;
import java.util.List;

import org.apache.log4j.Logger;

import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.dao.common.JCDynaBeanCallBack;
import com.infodms.dms.dao.crm.dealerleadsmanage.DlrLeadsManageDao;
import com.infoservice.po3.bean.DynaBean;
import com.infoservice.po3.bean.PO;

public class TaskManageDao extends BaseDao {
	public static Logger logger = Logger.getLogger(DlrLeadsManageDao.class);
	private static final DlrLeadsManageDao dao = new DlrLeadsManageDao();

	public static final DlrLeadsManageDao getInstance() {
		return dao;
	}

	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/*
	 * 根据顾问获取基盘客户信息
	 */
	public List<DynaBean> getCustomerInfoByAdviser(String dealerId, String userId) {
		StringBuffer sbSql = new StringBuffer("");
		
		sbSql.append("select b.name as adviser,\r\n");
		sbSql.append("       sum(case when  a.ctm_rank = 60101005 then 1 else 0 end) AS O,\r\n");
		sbSql.append("       --sum(decode(a.ctm_rank, 60101005, 1, 0)) as O,\r\n");
		sbSql.append("       sum(decode(a.ctm_rank, 60101001, 1, 0)) as H,\r\n");
		sbSql.append("       sum(decode(a.ctm_rank, 60101002, 1, 0)) as A,\r\n");
		sbSql.append("       sum(decode(a.ctm_rank, 60101003, 1, 0)) as B,\r\n");
		sbSql.append("       sum(decode(a.ctm_rank, 60101004, 1, 0)) as C,\r\n");
		sbSql.append("       sum(case when  a.ctm_rank = 60101006 then 1 else 0 end) AS E,\r\n");
		sbSql.append("       --sum(decode(a.ctm_rank, 60101006, 1, 0)) as E,\r\n");
		sbSql.append("       sum(case when  a.ctm_rank = 60101007 then 1 else 0 end) AS L\r\n");
		sbSql.append("       --sum(decode(a.ctm_rank, 60101007, 1, 0)) as L\r\n");
		sbSql.append("  from t_pc_customer a,\r\n");
		sbSql.append("       tc_user b \r\n");
		
		sbSql.append(" where 1 = 1\r\n");
		sbSql.append("   and a.ADVISER = b.user_id\r\n");
		sbSql.append("    and a.create_date>=trunc(sysdate, 'mm')  \r\n");
		sbSql.append("    and a.create_date< trunc(add_months(sysdate, 1), 'mm')   \r\n");
		if(userId!=null&&!"".equals(userId)) {
			sbSql.append(" and a.adviser in ("+userId+") ");
		}
		if(dealerId!=null&&!"".equals(dealerId)) {
			sbSql.append(" and a.dealer_id = '"+dealerId+"' ");
		}
		sbSql.append(" group by b.name"); 

		return factory.select(sbSql.toString(), null, new JCDynaBeanCallBack());
//		return select(sql.toString(), null, null);
	}
	
	/*
	 * 根据车系获取基盘客户信息
	 */
	public List<DynaBean> getCustomerInfoBySeries(String dealerId, String userId) {
		StringBuffer sbSql = new StringBuffer("");
		
		sbSql.append("select b.series_name as SERIES_NAME,\r\n");
		sbSql.append("       sum(case when  a.ctm_rank = 60101005 then 1 else 0 end) AS O,\r\n");
		sbSql.append("       --sum(decode(a.ctm_rank, 60101005, 1, 0)) as O,\r\n");
		sbSql.append("       sum(decode(a.ctm_rank, 60101001, 1, 0)) as H,\r\n");
		sbSql.append("       sum(decode(a.ctm_rank, 60101002, 1, 0)) as A,\r\n");
		sbSql.append("       sum(decode(a.ctm_rank, 60101003, 1, 0)) as B,\r\n");
		sbSql.append("       sum(decode(a.ctm_rank, 60101004, 1, 0)) as C,\r\n");
		sbSql.append("       sum(case when  a.ctm_rank = 60101006 then 1 else 0 end) AS E,\r\n");
		sbSql.append("       --sum(decode(a.ctm_rank, 60101006, 1, 0)) as E,\r\n");
		sbSql.append("       sum(case when  a.ctm_rank = 60101007 then 1 else 0 end) AS L\r\n");
		sbSql.append("       --sum(decode(a.ctm_rank, 60101007, 1, 0)) as L\r\n");
		sbSql.append("  from t_pc_customer a,\r\n");
		sbSql.append("       t_pc_intent_Vehicle b \r\n");
		
		sbSql.append(" where 1 = 1\r\n");
		sbSql.append("   and a.intent_vehicle = b.series_id\r\n");
		sbSql.append("    and a.create_date>=trunc(sysdate, 'mm')  \r\n");
		sbSql.append("    and a.create_date< trunc(add_months(sysdate, 1), 'mm')   \r\n");
		if(userId!=null&&!"".equals(userId)) {
			sbSql.append(" and a.adviser in ("+userId+") ");
		}
		if(dealerId!=null&&!"".equals(dealerId)) {
			sbSql.append(" and a.dealer_id = '"+dealerId+"' ");
		}
		sbSql.append(" group by b.series_id,b.series_name order by b.series_id"); 

		return factory.select(sbSql.toString(), null, new JCDynaBeanCallBack());
	}
	
	/*
	 * 获取销售排名信息(当月)
	 */
	public List<DynaBean> getAdviserSalesRanking(String dealerId, String userId) {
		//获取当月
		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH )+1;
		String curTime = year + "-" + month;

		StringBuffer sql = new StringBuffer("");
		
		sql.append("SELECT RANK() OVER(ORDER BY D.CUT DESC) AS RANKING, D.ADVISER, D.CUT\r\n");
		sql.append("  FROM (SELECT MAX(C.NAME) AS ADVISER, COUNT(1) AS CUT\r\n");
		sql.append("          FROM T_PC_DELVY A, T_PC_CUSTOMER B, TC_USER C\r\n");
		sql.append("         WHERE A.CUSTOMER_ID = B.CUSTOMER_ID\r\n");
		sql.append("           AND B.ADVISER = C.USER_ID\r\n");
		sql.append("           AND TO_CHAR(A.DELIVERY_DATE,'yyyy-MM') = to_char(to_date('"+curTime+"','yyyy-MM'),'yyyy-MM')\r\n");
		if(dealerId!=null&&!"".equals(dealerId)) {
			sql.append(" and B.dealer_id = '"+dealerId+"' ");
		}
		sql.append("           AND A.STATUS = 10011001\r\n");
		sql.append(" GROUP BY B.ADVISER) D"); 

		
		return factory.select(sql.toString(), null, new JCDynaBeanCallBack());
	}
	
	/*
	 * 根据经销商获取基盘客户信息
	 */
	public List<DynaBean> getCustomerInfoByDealer(String dealerId, String userId) {
		StringBuffer sbSql = new StringBuffer("");
		
		sbSql.append("select '一级经销商' as dealer_level,'1' as descnum,'1000' as descnum2,\r\n");
		sbSql.append("       sum(case when  a.ctm_rank = 60101005 then 1 else 0 end) AS O,\r\n");
		sbSql.append("       --sum(decode(a.ctm_rank, 60101005, 1, 0)) as O,\r\n");
		sbSql.append("       sum(decode(a.ctm_rank, 60101001, 1, 0)) as H,\r\n");
		sbSql.append("       sum(decode(a.ctm_rank, 60101002, 1, 0)) as A,\r\n");
		sbSql.append("       sum(decode(a.ctm_rank, 60101003, 1, 0)) as B,\r\n");
		sbSql.append("       sum(decode(a.ctm_rank, 60101004, 1, 0)) as C,\r\n");
		sbSql.append("       sum(case when  a.ctm_rank = 60101006 then 1 else 0 end) AS E,\r\n");
		sbSql.append("       --sum(decode(a.ctm_rank, 60101006, 1, 0)) as E,\r\n");
		sbSql.append("       sum(case when  a.ctm_rank = 60101007 then 1 else 0 end) AS L\r\n");
		sbSql.append("       --sum(decode(a.ctm_rank, 60101007, 1, 0)) as L\r\n");
		sbSql.append("  from t_pc_customer a,\r\n");
		sbSql.append("       tm_dealer b \r\n");
		
		sbSql.append(" where 1 = 1\r\n");
		sbSql.append("   and a.dealer_id = b.dealer_id\r\n");
		sbSql.append("    and a.create_date>=trunc(sysdate, 'mm')  \r\n");
		sbSql.append("    and a.create_date< trunc(add_months(sysdate, 1), 'mm')   \r\n");
		if(dealerId!=null&&!"".equals(dealerId)) {
			sbSql.append(" and a.dealer_id = '"+dealerId+"' ");
		}
		sbSql.append(" group by b.dealer_id\r\n");
		sbSql.append("\r\n");
		sbSql.append("union all\r\n");
		sbSql.append("\r\n");
		sbSql.append("select e.group_name as dealer_level,'2' as descnum,to_char(max(e.group_id)) as descnum2,\r\n");
		sbSql.append("       sum(case when  a.ctm_rank = 60101005 then 1 else 0 end) AS O,\r\n");
		sbSql.append("       --sum(decode(a.ctm_rank, 60101005, 1, 0)) as O,\r\n");
		sbSql.append("       sum(decode(a.ctm_rank, 60101001, 1, 0)) as H,\r\n");
		sbSql.append("       sum(decode(a.ctm_rank, 60101002, 1, 0)) as A,\r\n");
		sbSql.append("       sum(decode(a.ctm_rank, 60101003, 1, 0)) as B,\r\n");
		sbSql.append("       sum(decode(a.ctm_rank, 60101004, 1, 0)) as C,\r\n");
		sbSql.append("       sum(case when  a.ctm_rank = 60101006 then 1 else 0 end) AS E,\r\n");
		sbSql.append("       --sum(decode(a.ctm_rank, 60101006, 1, 0)) as E,\r\n");
		sbSql.append("       sum(case when  a.ctm_rank = 60101007 then 1 else 0 end) AS L\r\n");
		sbSql.append("       --sum(decode(a.ctm_rank, 60101007, 1, 0)) as L\r\n");
		sbSql.append("  from t_pc_customer a, tc_user b, t_pc_group e \r\n");
		
		sbSql.append(" where 1 = 1\r\n");
		sbSql.append("   and a.adviser = b.user_id\r\n");
		sbSql.append("   and b.group_id = e.group_id\r\n");
		sbSql.append("    and a.create_date>=trunc(sysdate, 'mm')  \r\n");
		sbSql.append("    and a.create_date< trunc(add_months(sysdate, 1), 'mm')   \r\n");
		if(dealerId!=null&&!"".equals(dealerId)) {
			sbSql.append(" and a.dealer_id = '"+dealerId+"' ");
		}
		sbSql.append(" group by e.group_name\r\n");
		sbSql.append("order by descnum,descnum2"); 

		return factory.select(sbSql.toString(), null, new JCDynaBeanCallBack());
//		return select(sql.toString(), null, null);
	}
	
	/*
	 * 根据经销商获取基盘客户信息
	 */
	public List<DynaBean> getCustomerInfoByDealer2(String dealerId, String userId) {
		StringBuffer sbSql = new StringBuffer("");
		
		sbSql.append("select '一级经销商' as dealer_level,'1' as descnum,'1000' as descnum2,\r\n");
		sbSql.append("       sum(case when  d.new_level = 60101005 then 1 else 0 end) AS O,\r\n");
		sbSql.append("       --sum(decode(d.new_level, 60101005, 1, 0)) as O,\r\n");
		sbSql.append("       sum(decode(d.new_level, 60101001, 1, 0)) as H,\r\n");
		sbSql.append("       sum(decode(d.new_level, 60101002, 1, 0)) as A,\r\n");
		sbSql.append("       sum(decode(d.new_level, 60101003, 1, 0)) as B,\r\n");
		sbSql.append("       sum(decode(d.new_level, 60101004, 1, 0)) as C,\r\n");
		sbSql.append("       sum(case when  d.new_level = 60101006 then 1 else 0 end) AS E,\r\n");
		sbSql.append("       --sum(decode(d.new_level, 60101006, 1, 0)) as E,\r\n");
		sbSql.append("       sum(case when  d.new_level = 60101007 then 1 else 0 end) AS L\r\n");
		sbSql.append("       --sum(decode(d.new_level, 60101007, 1, 0)) as L\r\n");
		sbSql.append("  from t_pc_customer a,\r\n");
		sbSql.append("       tm_dealer b,\r\n");
		sbSql.append("       (select c.customer_id,\r\n");
		sbSql.append("               substr(c.finish_date, 0, 19) as finish_date,\r\n");
		sbSql.append("               substr(c.finish_date, 20) as new_level\r\n");
		sbSql.append("          from (select b.customer_id, max(b.finish_date) as finish_date\r\n");
		sbSql.append("                  from (select a1.customer_id,\r\n");
		sbSql.append("                               to_char(a1.finish_date, 'yyyy-MM-dd HH24:mi:ss') || '' ||\r\n");
		sbSql.append("                               a1.new_level finish_date\r\n");
		sbSql.append("                          from t_pc_follow a1\r\n");
		sbSql.append("                         where a1.task_status = 60171002\r\n");
		sbSql.append("   and a1.finish_date>=trunc(sysdate, 'mm') \r\n");
		sbSql.append("   and a1.finish_date< trunc(add_months(sysdate, 1), 'mm') \r\n");
		sbSql.append("                        union all\r\n");
		sbSql.append("                        select a2.customer_id,\r\n");
		sbSql.append("                               to_char(a2.finish_date, 'yyyy-MM-dd HH24:mi:ss') || '' ||\r\n");
		sbSql.append("                               a2.new_level finish_date\r\n");
		sbSql.append("                          from t_pc_invite a2\r\n");
		sbSql.append("                         where a2.task_status = 60171002\r\n");
		sbSql.append("   and a2.finish_date>=trunc(sysdate, 'mm') \r\n");
		sbSql.append("   and a2.finish_date< trunc(add_months(sysdate, 1), 'mm') \r\n");
		sbSql.append("                        union all\r\n");
		sbSql.append("                        select a3.customer_id,\r\n");
		sbSql.append("                               to_char(a3.finish_date, 'yyyy-MM-dd HH24:mi:ss') || '' ||\r\n");
		sbSql.append("                               a3.new_level finish_date\r\n");
		sbSql.append("                          from t_pc_invite_shop a3\r\n");
		sbSql.append("                         where a3.task_status = 60171002\r\n");
		sbSql.append("   and a3.finish_date>=trunc(sysdate, 'mm') \r\n");
		sbSql.append("   and a3.finish_date< trunc(add_months(sysdate, 1), 'mm') \r\n");
		sbSql.append("                        union all\r\n");
		sbSql.append("                        select a4.customer_id,\r\n");
		sbSql.append("                               to_char(a4.finish_date, 'yyyy-MM-dd HH24:mi:ss') || '' ||\r\n");
		sbSql.append("                               a4.new_level finish_date\r\n");
		sbSql.append("                          from t_pc_order a4\r\n");
		sbSql.append("                         where a4.task_status = 60171002 \r\n");
		sbSql.append("   and a4.finish_date>=trunc(sysdate, 'mm') \r\n");
		sbSql.append("   and a4.finish_date< trunc(add_months(sysdate, 1), 'mm') \r\n");
		sbSql.append(" ) b\r\n");
		sbSql.append("                 group by b.customer_id) c) d\r\n");
		sbSql.append(" where 1 = 1\r\n");
		sbSql.append("   and a.dealer_id = b.dealer_id\r\n");
		sbSql.append("   and a.customer_id = d.customer_id\r\n");
		if(dealerId!=null&&!"".equals(dealerId)) {
			sbSql.append(" and a.dealer_id = (select b.PARENT_DEALER_D from tm_dealer b where dealer_id = "+dealerId+")\r\n ");
		}
		sbSql.append(" group by b.dealer_id\r\n");
		sbSql.append("\r\n");
		sbSql.append("union all\r\n");
		sbSql.append("\r\n");
		sbSql.append("select '二级经销商' as dealer_level,'3' as descnum,'2000' as descnum2,\r\n");
		sbSql.append("       sum(case when  d.new_level = 60101005 then 1 else 0 end) AS O,\r\n");
		sbSql.append("       --sum(decode(d.new_level, 60101005, 1, 0)) as O,\r\n");
		sbSql.append("       sum(decode(d.new_level, 60101001, 1, 0)) as H,\r\n");
		sbSql.append("       sum(decode(d.new_level, 60101002, 1, 0)) as A,\r\n");
		sbSql.append("       sum(decode(d.new_level, 60101003, 1, 0)) as B,\r\n");
		sbSql.append("       sum(decode(d.new_level, 60101004, 1, 0)) as C,\r\n");
		sbSql.append("       sum(case when  d.new_level = 60101006 then 1 else 0 end) AS E,\r\n");
		sbSql.append("       --sum(decode(d.new_level, 60101006, 1, 0)) as E,\r\n");
		sbSql.append("       sum(case when  d.new_level = 60101007 then 1 else 0 end) AS L\r\n");
		sbSql.append("       --sum(decode(d.new_level, 60101007, 1, 0)) as L\r\n");
		sbSql.append("  from t_pc_customer a,\r\n");
		sbSql.append("       tm_dealer b,\r\n");
		sbSql.append("       (select c.customer_id,\r\n");
		sbSql.append("               substr(c.finish_date, 0, 10) as finish_date,\r\n");
		sbSql.append("               substr(c.finish_date, 11) as new_level\r\n");
		sbSql.append("          from (select b.customer_id, max(b.finish_date) as finish_date\r\n");
		sbSql.append("                  from (select a1.customer_id,\r\n");
		sbSql.append("                               to_char(a1.finish_date, 'yyyy-MM-dd') || '' ||\r\n");
		sbSql.append("                               a1.new_level finish_date\r\n");
		sbSql.append("                          from t_pc_follow a1\r\n");
		sbSql.append("                         where a1.task_status = 60171002\r\n");
		sbSql.append("   and a1.finish_date>=trunc(sysdate, 'mm') \r\n");
		sbSql.append("   and a1.finish_date< trunc(add_months(sysdate, 1), 'mm') \r\n");
		sbSql.append("                        union all\r\n");
		sbSql.append("                        select a2.customer_id,\r\n");
		sbSql.append("                               to_char(a2.finish_date, 'yyyy-MM-dd') || '' ||\r\n");
		sbSql.append("                               a2.new_level finish_date\r\n");
		sbSql.append("                          from t_pc_invite a2\r\n");
		sbSql.append("                         where a2.task_status = 60171002\r\n");
		sbSql.append("   and a2.finish_date>=trunc(sysdate, 'mm') \r\n");
		sbSql.append("   and a2.finish_date< trunc(add_months(sysdate, 1), 'mm') \r\n");
		sbSql.append("                        union all\r\n");
		sbSql.append("                        select a3.customer_id,\r\n");
		sbSql.append("                               to_char(a3.finish_date, 'yyyy-MM-dd') || '' ||\r\n");
		sbSql.append("                               a3.new_level finish_date\r\n");
		sbSql.append("                          from t_pc_invite_shop a3\r\n");
		sbSql.append("                         where a3.task_status = 60171002\r\n");
		sbSql.append("   and a3.finish_date>=trunc(sysdate, 'mm') \r\n");
		sbSql.append("   and a3.finish_date< trunc(add_months(sysdate, 1), 'mm') \r\n");
		sbSql.append("                        union all\r\n");
		sbSql.append("                        select a4.customer_id,\r\n");
		sbSql.append("                               to_char(a4.finish_date, 'yyyy-MM-dd') || '' ||\r\n");
		sbSql.append("                               a4.new_level finish_date\r\n");
		sbSql.append("                          from t_pc_order a4\r\n");
		sbSql.append("                         where a4.task_status = 60171002 \r\n");
		sbSql.append("   and a4.finish_date>=trunc(sysdate, 'mm') \r\n");
		sbSql.append("   and a4.finish_date< trunc(add_months(sysdate, 1), 'mm') \r\n");
		sbSql.append(" ) b\r\n");
	    sbSql.append("                 group by b.customer_id) c) d\r\n");
		sbSql.append(" where 1 = 1\r\n");
		sbSql.append("   and a.dealer_id = b.dealer_id\r\n");
		sbSql.append("   and a.customer_id = d.customer_id\r\n");
		if(dealerId!=null&&!"".equals(dealerId)) {
			sbSql.append(" and a.dealer_id = '"+dealerId+"' ");
		}
		sbSql.append(" group by b.dealer_id\r\n");
		sbSql.append("\r\n");
		sbSql.append("union all\r\n");
		sbSql.append("\r\n");
		sbSql.append("select e.group_name as dealer_level,'2' as descnum,to_char(max(e.group_id)) as descnum2,\r\n");
		sbSql.append("       sum(case when  d.new_level = 60101005 then 1 else 0 end) AS O,\r\n");
		sbSql.append("       --sum(decode(d.new_level, 60101005, 1, 0)) as O,\r\n");
		sbSql.append("       sum(decode(d.new_level, 60101001, 1, 0)) as H,\r\n");
		sbSql.append("       sum(decode(d.new_level, 60101002, 1, 0)) as A,\r\n");
		sbSql.append("       sum(decode(d.new_level, 60101003, 1, 0)) as B,\r\n");
		sbSql.append("       sum(decode(d.new_level, 60101004, 1, 0)) as C,\r\n");
		sbSql.append("       sum(case when  d.new_level = 60101006 then 1 else 0 end) AS E,\r\n");
		sbSql.append("       --sum(decode(d.new_level, 60101006, 1, 0)) as E,\r\n");
		sbSql.append("       sum(case when  d.new_level = 60101007 then 1 else 0 end) AS L\r\n");
		sbSql.append("       --sum(decode(d.new_level, 60101007, 1, 0)) as L\r\n");
		sbSql.append("  from t_pc_customer a, tc_user b, t_pc_group e,\r\n");
		sbSql.append("       (select c.customer_id,\r\n");
		sbSql.append("               substr(c.finish_date, 0, 10) as finish_date,\r\n");
		sbSql.append("               substr(c.finish_date, 11) as new_level\r\n");
		sbSql.append("          from (select b.customer_id, max(b.finish_date) as finish_date\r\n");
		sbSql.append("                  from (select a1.customer_id,\r\n");
		sbSql.append("                               to_char(a1.finish_date, 'yyyy-MM-dd') || '' ||\r\n");
		sbSql.append("                               a1.new_level finish_date\r\n");
		sbSql.append("                          from t_pc_follow a1\r\n");
		sbSql.append("                         where a1.task_status = 60171002\r\n");
		sbSql.append("   and a1.finish_date>=trunc(sysdate, 'mm') \r\n");
		sbSql.append("   and a1.finish_date< trunc(add_months(sysdate, 1), 'mm') \r\n");
		sbSql.append("                        union all\r\n");
		sbSql.append("                        select a2.customer_id,\r\n");
		sbSql.append("                               to_char(a2.finish_date, 'yyyy-MM-dd') || '' ||\r\n");
		sbSql.append("                               a2.new_level finish_date\r\n");
		sbSql.append("                          from t_pc_invite a2\r\n");
		sbSql.append("                         where a2.task_status = 60171002\r\n");
		sbSql.append("   and a2.finish_date>=trunc(sysdate, 'mm') \r\n");
		sbSql.append("   and a2.finish_date< trunc(add_months(sysdate, 1), 'mm') \r\n");
		sbSql.append("                        union all\r\n");
		sbSql.append("                        select a3.customer_id,\r\n");
		sbSql.append("                               to_char(a3.finish_date, 'yyyy-MM-dd') || '' ||\r\n");
		sbSql.append("                               a3.new_level finish_date\r\n");
		sbSql.append("                          from t_pc_invite_shop a3\r\n");
		sbSql.append("                         where a3.task_status = 60171002\r\n");
		sbSql.append("   and a3.finish_date>=trunc(sysdate, 'mm') \r\n");
		sbSql.append("   and a3.finish_date< trunc(add_months(sysdate, 1), 'mm') \r\n");
		sbSql.append("                        union all\r\n");
		sbSql.append("                        select a4.customer_id,\r\n");
		sbSql.append("                               to_char(a4.finish_date, 'yyyy-MM-dd') || '' ||\r\n");
		sbSql.append("                               a4.new_level finish_date\r\n");
		sbSql.append("                          from t_pc_order a4\r\n");
		sbSql.append("                         where a4.task_status = 60171002 \r\n");
		sbSql.append("   and a4.finish_date>=trunc(sysdate, 'mm') \r\n");
		sbSql.append("   and a4.finish_date< trunc(add_months(sysdate, 1), 'mm') \r\n");
		sbSql.append(" ) b\r\n");
		sbSql.append("                 group by b.customer_id) c) d\r\n");
		sbSql.append(" where 1 = 1\r\n");
		sbSql.append("   and a.adviser = b.user_id\r\n");
		sbSql.append("   and b.group_id = e.group_id\r\n");
		sbSql.append("   and a.customer_id = d.customer_id\r\n");
		if(dealerId!=null&&!"".equals(dealerId)) {
			sbSql.append(" and a.dealer_id = '"+dealerId+"' ");
		}
		sbSql.append(" group by e.group_name\r\n");
		sbSql.append("order by descnum,descnum2"); 

		return factory.select(sbSql.toString(), null, new JCDynaBeanCallBack());
//		return select(sql.toString(), null, null);
	}
	
	/*
	 * 获取未确认有效的销售线索信息
	 */
	public List<DynaBean> getLeadsInfo(String dealerId, String userId, String startDate, String endDate) {
		StringBuffer sbSql = new StringBuffer("");
		
		sbSql.append("select a.customer_name,a.LEADS_CODE,a.LEADS_TYPE,b.LEADS_ALLOT_ID,\r\n");
		sbSql.append("       a.telephone,\r\n");
		sbSql.append("       to_char(a.come_date, 'MM-dd HH24:mi') as come_date,\r\n");
		sbSql.append("       to_char(a.leave_date, 'MM-dd HH24:mi') as leave_date,\r\n");
		sbSql.append("       a.customer_describe,\r\n");
		sbSql.append("       decode(d.code_desc,'自然来店','来店','来电线索','来电',d.code_desc) as code_desc,\r\n");
		sbSql.append("       c.name,\r\n");
		sbSql.append("       to_char(b.allot_adviser_date, 'yyyy-MM-dd') as allot_adviser_date\r\n");
		sbSql.append("  from t_pc_leads a, t_pc_leads_allot b, tc_user c,tc_code d\r\n");
		sbSql.append(" where a.leads_code = b.leads_code\r\n");
		sbSql.append("   and b.adviser = c.user_id\r\n");
		sbSql.append("   and a.leads_origin = d.code_id \r\n"); 
		if(userId!=null&&!"".equals(userId)) {
			sbSql.append(" and b.adviser in ("+userId+") \r\n");
		}
		if(dealerId!=null&&!"".equals(dealerId)) {
			sbSql.append(" and b.dealer_id = '"+dealerId+"' ");
		}
		if((startDate==null||"".equals(startDate))&&(endDate==null||"".equals(endDate))) {
			sbSql.append(" and to_char(b.ALLOT_ADVISER_DATE,'yyyy-MM-dd') = to_char(sysdate,'yyyy-MM-dd') ");//当日的分派线索
		} else if((startDate==null||"".equals(startDate))&&(endDate!=null&&!"".equals(endDate))) {
			sbSql.append(" and to_char(b.ALLOT_ADVISER_DATE,'yyyy-MM-dd') <= '"+endDate+"' ");
		} else if((startDate!=null&&!"".equals(startDate))&&(endDate==null||"".equals(endDate))) {
			sbSql.append(" and to_char(b.ALLOT_ADVISER_DATE,'yyyy-MM-dd') >= '"+startDate+"' ");
		} else {
			sbSql.append(" and b.ALLOT_ADVISER_DATE<= to_date('"+endDate+" 23:59:59','yyyy-mm-dd hh24:mi:ss') ");
			sbSql.append(" and b.ALLOT_ADVISER_DATE>= to_date('"+startDate+" 00:00:00','yyyy-mm-dd hh24:mi:ss') ");
		}
		sbSql.append(" and b.if_confirm = "+Constant.ADVISER_CONFIRM_01+" ");// 待确认
		sbSql.append(" and b.status = "+Constant.STATUS_ENABLE+" ");// 有效
		sbSql.append(" and a.leads_status not in ("+Constant.LEADS_STATUS_04+") order by b.ALLOT_ADVISER_DATE desc ");// 不为无效
		return factory.select(sbSql.toString(), null, new JCDynaBeanCallBack());
	}
	
	/*
	 * 获取所有有效销售线索信息
	 */
	public List<DynaBean> getAllLeadsInfo(String dealerId, String userId, String startDate, String endDate) {
		StringBuffer sbSql = new StringBuffer("");
		
		sbSql.append("select a.LEADS_CODE \r\n");
		sbSql.append("  from t_pc_leads a, t_pc_leads_allot b, tc_user c,tc_code d\r\n");
		sbSql.append(" where a.leads_code = b.leads_code\r\n");
		sbSql.append("   and b.adviser = c.user_id\r\n");
		sbSql.append("   and a.leads_origin = d.code_id \r\n"); 
		if(userId!=null&&!"".equals(userId)) {
			sbSql.append(" and b.adviser in ("+userId+") \r\n");
		}
		if(dealerId!=null&&!"".equals(dealerId)) {
			sbSql.append(" and b.dealer_id = '"+dealerId+"' ");
		}
		if((startDate==null||"".equals(startDate))&&(endDate==null||"".equals(endDate))) {
			sbSql.append(" and to_char(b.ALLOT_ADVISER_DATE,'yyyy-MM-dd') = to_char(sysdate,'yyyy-MM-dd') ");//当日的分派线索
		} else if((startDate==null||"".equals(startDate))&&(endDate!=null&&!"".equals(endDate))) {
			sbSql.append(" and to_char(b.ALLOT_ADVISER_DATE,'yyyy-MM-dd') <= '"+endDate+"' ");
		} else if((startDate!=null&&!"".equals(startDate))&&(endDate==null||"".equals(endDate))) {
			sbSql.append(" and to_char(b.ALLOT_ADVISER_DATE,'yyyy-MM-dd') >= '"+startDate+"' ");
		} else {
			sbSql.append(" and to_char(b.ALLOT_ADVISER_DATE,'yyyy-MM-dd') <= '"+endDate+"' ");
			sbSql.append(" and to_char(b.ALLOT_ADVISER_DATE,'yyyy-MM-dd') >= '"+startDate+"' ");
		}
//		sbSql.append(" and b.status = "+Constant.STATUS_ENABLE+" ");// 有效
		return factory.select(sbSql.toString(), null, new JCDynaBeanCallBack());
	}
	
	/*
	 * 获取所有邀约信息
	 */
	public List<DynaBean> getAllInviteInfo(String dealerId, String userId, String startDate, String endDate) {
		StringBuffer sbSql = new StringBuffer("");
		
		sbSql.append("SELECT A.INVITE_ID \r\n");
		sbSql.append("  FROM T_PC_INVITE A, t_pc_customer b, T_PC_INTENT_VEHICLE C,tc_code d, tc_user e\r\n");
		sbSql.append(" WHERE A.CUSTOMER_ID = B.CUSTOMER_ID\r\n");
		sbSql.append("   AND B.INTENT_VEHICLE = c.series_id\r\n");
		sbSql.append("   AND a.old_level = d.code_id\r\n");
		sbSql.append("   and b.adviser = e.user_id\r\n");
		sbSql.append("   and (a.DIRECTOR_AUDIT = "+Constant.DIRECTOR_AUDIT_02+" or a.DIRECTOR_AUDIT = "+Constant.DIRECTOR_AUDIT_04+")"); 
		if(userId!=null&&!"".equals(userId)) {
			sbSql.append(" and b.adviser in ("+userId+") \r\n");
		}
		if(dealerId!=null&&!"".equals(dealerId)) {
			sbSql.append(" and b.dealer_id = '"+dealerId+"' ");
		}
		if((startDate==null||"".equals(startDate))&&(endDate==null||"".equals(endDate))) {
			sbSql.append(" and to_char(A.PLAN_INVITE_DATE,'yyyy-MM-dd') = to_char(sysdate,'yyyy-MM-dd') ");//当日的跟进任务
		} else if((startDate==null||"".equals(startDate))&&(endDate!=null&&!"".equals(endDate))) {
			sbSql.append(" and to_char(A.PLAN_INVITE_DATE,'yyyy-MM-dd') <= '"+endDate+"' ");
		} else if((startDate!=null&&!"".equals(startDate))&&(endDate==null||"".equals(endDate))) {
			sbSql.append(" and to_char(A.PLAN_INVITE_DATE,'yyyy-MM-dd') >= '"+startDate+"' ");
		} else {
			sbSql.append(" and to_char(A.PLAN_INVITE_DATE,'yyyy-MM-dd') <= '"+endDate+"' ");
			sbSql.append(" and to_char(A.PLAN_INVITE_DATE,'yyyy-MM-dd') >= '"+startDate+"' ");
		}
		sbSql.append("   union all\r\n");
		sbSql.append("SELECT A.INVITE_ID \r\n");
		sbSql.append("  FROM T_PC_INVITE_shop A, t_pc_customer b, T_PC_INTENT_VEHICLE C,tc_code d, tc_user e\r\n");
		sbSql.append(" WHERE A.CUSTOMER_ID = B.CUSTOMER_ID\r\n");
		sbSql.append("   AND B.INTENT_VEHICLE = c.series_id\r\n");
		sbSql.append("   AND a.old_level = d.code_id\r\n");
		sbSql.append("   and b.adviser = e.user_id"); 
		if(userId!=null&&!"".equals(userId)) {
			sbSql.append(" and b.adviser in ("+userId+") \r\n");
		}
		if(dealerId!=null&&!"".equals(dealerId)) {
			sbSql.append(" and b.dealer_id = '"+dealerId+"' ");
		}
		if((startDate==null||"".equals(startDate))&&(endDate==null||"".equals(endDate))) {
			sbSql.append(" and to_char(A.INVITE_SHOP_DATE,'yyyy-MM-dd') = to_char(sysdate,'yyyy-MM-dd') ");//当日的跟进任务
		} else if((startDate==null||"".equals(startDate))&&(endDate!=null&&!"".equals(endDate))) {
			sbSql.append(" and to_char(A.INVITE_SHOP_DATE,'yyyy-MM-dd') <= '"+endDate+"' ");
		} else if((startDate!=null&&!"".equals(startDate))&&(endDate==null||"".equals(endDate))) {
			sbSql.append(" and to_char(A.INVITE_SHOP_DATE,'yyyy-MM-dd') >= '"+startDate+"' ");
		} else {
			sbSql.append(" and to_char(A.INVITE_SHOP_DATE,'yyyy-MM-dd') <= '"+endDate+"' ");
			sbSql.append(" and to_char(A.INVITE_SHOP_DATE,'yyyy-MM-dd') >= '"+startDate+"' ");
		}

		return factory.select(sbSql.toString(), null, new JCDynaBeanCallBack());
	}
	
	/*
	 * 获取所有回访信息
	 */
	public List<DynaBean> getAllRevisitInfo(String dealerId, String userId, String startDate, String endDate) {
		StringBuffer sbSql = new StringBuffer("");
		
		sbSql.append("SELECT A.REVISIT_ID \r\n");
		sbSql.append("      FROM T_PC_REVISIT A, T_PC_CUSTOMER B, T_PC_INTENT_VEHICLE C,TC_CODE D, TC_USER E\r\n");
		sbSql.append("     WHERE A.CUSTOMER_ID = B.CUSTOMER_ID\r\n");
		sbSql.append("       AND B.INTENT_VEHICLE = C.SERIES_ID\r\n");
		sbSql.append("       AND B.ADVISER = E.USER_ID\r\n");
		sbSql.append("       AND A.Task_Status<>'60171003' "); 
		sbSql.append("       AND A.REVISIT_TYPE = D.CODE_ID"); 
		if(userId!=null&&!"".equals(userId)) {
			sbSql.append(" and b.adviser in ("+userId+") \r\n");
		}
		if(dealerId!=null&&!"".equals(dealerId)) {
			sbSql.append(" and b.dealer_id = '"+dealerId+"' ");
		}
		if((startDate==null||"".equals(startDate))&&(endDate==null||"".equals(endDate))) {
			sbSql.append(" and to_char(a.REVISIT_DATE,'yyyy-MM-dd') = to_char(sysdate,'yyyy-MM-dd') ");//当日的跟进任务
		} else if((startDate==null||"".equals(startDate))&&(endDate!=null&&!"".equals(endDate))) {
			sbSql.append(" and a.REVISIT_DATE <= to_date('"+endDate+" 23:59:59','yyyy-MM-dd HH24:mi:ss') ");
		} else if((startDate!=null&&!"".equals(startDate))&&(endDate==null||"".equals(endDate))) {
			sbSql.append(" and a.REVISIT_DATE >= to_date('"+startDate+" 00:00:00','yyyy-MM-dd HH24:mi:ss') ");
		} else {
			sbSql.append(" and a.REVISIT_DATE <= to_date('"+endDate+" 23:59:59','yyyy-MM-dd HH24:mi:ss') ");
			sbSql.append(" and a.REVISIT_DATE >= to_date('"+startDate+" 00:00:00','yyyy-MM-dd HH24:mi:ss') ");
		}

		return factory.select(sbSql.toString(), null, new JCDynaBeanCallBack());
	}
	
	/*
	 * 获取所有跟进信息
	 */
	public List<DynaBean> getAllFollowInfo(String dealerId, String userId, String startDate, String endDate) {
		StringBuffer sbSql = new StringBuffer("");
		
		sbSql.append("select A.FOLLOW_ID \r\n");
		sbSql.append("  from t_pc_follow a, t_pc_customer b, t_pc_intent_vehicle c, tc_code d, tc_user e\r\n");
		sbSql.append(" where a.customer_id = b.customer_id\r\n");
		sbSql.append("   and b.intent_vehicle = c.series_id\r\n");
		sbSql.append("   and a.old_level = d.code_id\r\n");
		sbSql.append("   and b.adviser = e.user_id\r\n");

		if(userId!=null&&!"".equals(userId)) {
			sbSql.append(" and b.adviser in ("+userId+") \r\n");
		}
		if(dealerId!=null&&!"".equals(dealerId)) {
			sbSql.append(" and b.dealer_id = '"+dealerId+"' ");
		}
		if((startDate==null||"".equals(startDate))&&(endDate==null||"".equals(endDate))) {
			sbSql.append(" and to_char(a.follow_date,'yyyy-MM-dd') = to_char(sysdate,'yyyy-MM-dd') ");//当日的跟进任务
		} else if((startDate==null||"".equals(startDate))&&(endDate!=null&&!"".equals(endDate))) {
			sbSql.append(" and a.follow_date <= to_date('"+endDate+"','yyyy-MM-dd') ");
		} else if((startDate!=null&&!"".equals(startDate))&&(endDate==null||"".equals(endDate))) {
			sbSql.append(" and a.follow_date >= to_date('"+startDate+"','yyyy-MM-dd') ");
		} else {
			sbSql.append(" and a.follow_date <= to_date('"+endDate+"','yyyy-MM-dd') ");
			sbSql.append(" and a.follow_date >= to_date('"+startDate+"','yyyy-MM-dd') ");
		}
		return factory.select(sbSql.toString(), null, new JCDynaBeanCallBack());
	}
	
	/*
	 * 获取所有订单信息
	 */
	public List<DynaBean> getAllOrderInfo(String dealerId, String userId, String startDate, String endDate) {
		StringBuffer sbSql = new StringBuffer("");
		
		sbSql.append("SELECT A.ORDER_ID \r\n");
		sbSql.append("  FROM T_PC_ORDER A, T_PC_CUSTOMER B, T_PC_INTENT_VEHICLE C, TC_USER D\r\n");
		sbSql.append(" WHERE A.CUSTOMER_ID = B.CUSTOMER_ID\r\n");
		sbSql.append("   AND B.INTENT_VEHICLE = C.SERIES_ID\r\n");
		sbSql.append("   AND B.ADVISER = D.USER_ID"); 
//		sbSql.append("   and a.order_status = "+Constant.TPC_ORDER_STATUS_01+""); 
		if(userId!=null&&!"".equals(userId)) {
			sbSql.append(" and b.adviser in ("+userId+") \r\n");
		}
		if(dealerId!=null&&!"".equals(dealerId)) {
			sbSql.append(" and b.dealer_id = '"+dealerId+"' ");
		}
		if((startDate==null||"".equals(startDate))&&(endDate==null||"".equals(endDate))) {
			sbSql.append(" and to_char(A.ORDER_DATE,'yyyy-MM-dd') = to_char(sysdate,'yyyy-MM-dd') ");//当日的跟进任务
		} else if((startDate==null||"".equals(startDate))&&(endDate!=null&&!"".equals(endDate))) {
			sbSql.append(" and to_char(A.ORDER_DATE,'yyyy-MM-dd') <= '"+endDate+"' ");
		} else if((startDate!=null&&!"".equals(startDate))&&(endDate==null||"".equals(endDate))) {
			sbSql.append(" and to_char(A.ORDER_DATE,'yyyy-MM-dd') >= '"+startDate+"' ");
		} else {
			sbSql.append(" and to_char(A.ORDER_DATE,'yyyy-MM-dd') <= '"+endDate+"' ");
			sbSql.append(" and to_char(A.ORDER_DATE,'yyyy-MM-dd') >= '"+startDate+"' ");
		}

		return factory.select(sbSql.toString(), null, new JCDynaBeanCallBack());
	}
	
	/*
	 * 获取所有交车信息
	 */
	public List<DynaBean> getAllDeliveryInfo(String dealerId, String userId, String startDate, String endDate) {
		StringBuffer sbSql = new StringBuffer("");
		
		sbSql.append("SELECT A.ORDER_DETAIL_ID AS DETAIL_ID \r\n");
		sbSql.append("  FROM T_PC_ORDER_DETAIL A\r\n");
		sbSql.append("  LEFT JOIN T_PC_CUSTOMER B ON A.CUSTOMER_ID = B.CUSTOMER_ID\r\n");
		sbSql.append("  LEFT JOIN TC_USER D ON B.ADVISER = D.USER_ID\r\n");
		sbSql.append("  LEFT JOIN T_PC_INTENT_VEHICLE C ON A.INTENT_MODEL = C.SERIES_ID\r\n");
		sbSql.append("  LEFT JOIN TM_VHCL_MATERIAL E ON A.MATERIAL = E.MATERIAL_ID\r\n");
		sbSql.append("  LEFT JOIN TM_VHCL_MATERIAL_GROUP F ON A.INTENT_MODEL = F.GROUP_ID\r\n");
		sbSql.append("  WHERE 1=1"); 
		sbSql.append("    AND A.DELIVERY_NUMBER<= A.NUM "); 
		sbSql.append(" and (a.task_status=60171001 or a.task_status=60171002)");
		sbSql.append(" and  a.remark is null ");
		if(userId!=null&&!"".equals(userId)) {
			sbSql.append(" and b.adviser in ("+userId+") \r\n");
		}
		if(dealerId!=null&&!"".equals(dealerId)) {
			sbSql.append(" and b.dealer_id = '"+dealerId+"' ");
		}
		if((startDate==null||"".equals(startDate))&&(endDate==null||"".equals(endDate))) {
			sbSql.append(" and to_char(A.DELIVERY_DATE,'yyyy-MM-dd') = to_char(sysdate,'yyyy-MM-dd') ");//当日的跟进任务
		} else if((startDate==null||"".equals(startDate))&&(endDate!=null&&!"".equals(endDate))) {
			sbSql.append(" and to_char(A.DELIVERY_DATE,'yyyy-MM-dd') <= '"+endDate+"' ");
		} else if((startDate!=null&&!"".equals(startDate))&&(endDate==null||"".equals(endDate))) {
			sbSql.append(" and to_char(A.DELIVERY_DATE,'yyyy-MM-dd') >= '"+startDate+"' ");
		} else {
			sbSql.append(" and to_char(A.DELIVERY_DATE,'yyyy-MM-dd') <= '"+endDate+"' ");
			sbSql.append(" and to_char(A.DELIVERY_DATE,'yyyy-MM-dd') >= '"+startDate+"' ");
		}

		return factory.select(sbSql.toString(), null, new JCDynaBeanCallBack());
	}
	
	/*
	 * 获取跟进任务信息
	 */
	public List<DynaBean> getFollowInfo(String dealerId, String userId, String startDate, String endDate) {
		StringBuffer sbSql = new StringBuffer("");
		
		sbSql.append("select A.FOLLOW_ID,A.CUSTOMER_ID,b.customer_name, b.telephone, c.series_name, d.code_desc, e.name, to_char(a.follow_date,'yyyy-MM-dd') as follow_date,a.RESTART_TYPE\r\n");
		sbSql.append("  from t_pc_follow a, t_pc_customer b, t_pc_intent_vehicle c, tc_code d, tc_user e\r\n");
		sbSql.append(" where a.customer_id = b.customer_id\r\n");
		sbSql.append("   and b.intent_vehicle = c.series_id\r\n");
		sbSql.append("   and a.old_level = d.code_id\r\n");
		sbSql.append("   and b.ADVISER = e.user_id\r\n");
		sbSql.append("   and a.task_status = "+Constant.TASK_STATUS_01+""); 
		if(userId!=null&&!"".equals(userId)) {
			sbSql.append(" and b.adviser in ("+userId+") \r\n");
		}
		if(dealerId!=null&&!"".equals(dealerId)) {
			sbSql.append(" and b.dealer_id = '"+dealerId+"' ");
		}
		if((startDate==null||"".equals(startDate))&&(endDate==null||"".equals(endDate))) {
			sbSql.append(" and to_char(a.follow_date,'yyyy-MM-dd') = to_char(sysdate,'yyyy-MM-dd') ");//当日的跟进任务
		} else if((startDate==null||"".equals(startDate))&&(endDate!=null&&!"".equals(endDate))) {
			sbSql.append(" and a.follow_date <= to_date('"+endDate+"','yyyy-MM-dd') ");
		} else if((startDate!=null&&!"".equals(startDate))&&(endDate==null||"".equals(endDate))) {
			sbSql.append(" and a.follow_date >= to_date('"+startDate+"','yyyy-MM-dd') ");
		} else {
			sbSql.append(" and a.follow_date <= to_date('"+endDate+" 23:59:59','yyyy-MM-dd hh24:mi:ss') ");
			sbSql.append(" and a.follow_date >= to_date('"+startDate+" 00:00:00','yyyy-MM-dd hh24:mi:ss') order by a.follow_date desc ");
		}
		return factory.select(sbSql.toString(), null, new JCDynaBeanCallBack());
	}
	
	/*
	 * 获取邀约任务信息
	 */
	public List<DynaBean> getInviteInfo(String dealerId, String userId, String startDate, String endDate) {
		StringBuffer sbSql = new StringBuffer("");
		
		sbSql.append("SELECT '' AS INVITE_SHOP_ID,\r\n");
		sbSql.append("       A.INVITE_ID,\r\n");
		sbSql.append("       B.CUSTOMER_ID,\r\n");
		sbSql.append("       B.CUSTOMER_NAME,\r\n");
		sbSql.append("       B.TELEPHONE,\r\n");
		sbSql.append("       c.series_name,\r\n");
		sbSql.append("       D.CODE_DESC,\r\n");
		sbSql.append("       '电话邀约' AS invite_type,\r\n");
		sbSql.append("       e.name,\r\n");
		sbSql.append("       TO_CHAR(A.PLAN_INVITE_DATE,'yyyy-MM-dd') AS PLAN_DATE,A.RESTART_TYPE\r\n");
		sbSql.append("  FROM T_PC_INVITE A, t_pc_customer b, T_PC_INTENT_VEHICLE C,tc_code d, tc_user e\r\n");
		sbSql.append(" WHERE A.CUSTOMER_ID = B.CUSTOMER_ID\r\n");
		sbSql.append("   AND B.INTENT_VEHICLE = c.series_id\r\n");
		sbSql.append("   AND a.old_level = d.code_id\r\n");
		sbSql.append("   and b.ADVISER = e.user_id\r\n");
		sbSql.append("   and a.task_status = "+Constant.TASK_STATUS_01+""); 
		sbSql.append("   and (a.DIRECTOR_AUDIT = "+Constant.DIRECTOR_AUDIT_02+" or a.DIRECTOR_AUDIT = "+Constant.DIRECTOR_AUDIT_04+")"); 
		if(userId!=null&&!"".equals(userId)) {
			sbSql.append(" and b.adviser in ("+userId+") \r\n");
		}
		if(dealerId!=null&&!"".equals(dealerId)) {
			sbSql.append(" and b.dealer_id = '"+dealerId+"' ");
		}
		if((startDate==null||"".equals(startDate))&&(endDate==null||"".equals(endDate))) {
			sbSql.append(" and to_char(A.PLAN_INVITE_DATE,'yyyy-MM-dd') = to_char(sysdate,'yyyy-MM-dd') ");//当日的跟进任务
		} else if((startDate==null||"".equals(startDate))&&(endDate!=null&&!"".equals(endDate))) {
			sbSql.append(" and to_char(A.PLAN_INVITE_DATE,'yyyy-MM-dd') <= '"+endDate+"' ");
		} else if((startDate!=null&&!"".equals(startDate))&&(endDate==null||"".equals(endDate))) {
			sbSql.append(" and to_char(A.PLAN_INVITE_DATE,'yyyy-MM-dd') >= '"+startDate+"' ");
		} else {
			sbSql.append(" and A.PLAN_INVITE_DATE <= to_date('"+endDate+" 23:59:59','yyyy-mm-dd hh24:mi:ss') ");
			sbSql.append(" and A.PLAN_INVITE_DATE >= to_date('"+startDate+" 00:00:00','yyyy-mm-dd hh24:mi:ss') ");
		}
		sbSql.append("   union all\r\n");
		sbSql.append("SELECT TO_CHAR(A.INVITE_SHOP_ID),\r\n");
		sbSql.append("       A.INVITE_ID,\r\n");
		sbSql.append("       B.CUSTOMER_ID,\r\n");
		sbSql.append("       B.CUSTOMER_NAME,\r\n");
		sbSql.append("       B.TELEPHONE,\r\n");
		sbSql.append("       c.series_name,\r\n");
		sbSql.append("       D.CODE_DESC,\r\n");
		sbSql.append("       '邀约到店' AS invite_type,\r\n");
		sbSql.append("       e.name,\r\n");
		sbSql.append("       TO_CHAR(A.INVITE_SHOP_DATE,'yyyy-MM-dd') AS PLAN_DATE,'' AS RESTART_TYPE\r\n");
		sbSql.append("  FROM T_PC_INVITE_shop A, t_pc_customer b, T_PC_INTENT_VEHICLE C,tc_code d, tc_user e\r\n");
		sbSql.append(" WHERE A.CUSTOMER_ID = B.CUSTOMER_ID\r\n");
		sbSql.append("   AND B.INTENT_VEHICLE = c.series_id\r\n");
		sbSql.append("   AND a.old_level = d.code_id\r\n");
		sbSql.append("   and b.ADVISER = e.user_id"); 
		sbSql.append("   and a.task_status = "+Constant.TASK_STATUS_01+""); 
		if(userId!=null&&!"".equals(userId)) {
			sbSql.append(" and b.adviser in ("+userId+") \r\n");
		}
		if(dealerId!=null&&!"".equals(dealerId)) {
			sbSql.append(" and b.dealer_id = '"+dealerId+"' ");
		}
		if((startDate==null||"".equals(startDate))&&(endDate==null||"".equals(endDate))) {
			sbSql.append(" and to_char(A.INVITE_SHOP_DATE,'yyyy-MM-dd') = to_char(sysdate,'yyyy-MM-dd') ");//当日的跟进任务
		} else if((startDate==null||"".equals(startDate))&&(endDate!=null&&!"".equals(endDate))) {
			sbSql.append(" and to_char(A.INVITE_SHOP_DATE,'yyyy-MM-dd') <= '"+endDate+"' ");
		} else if((startDate!=null&&!"".equals(startDate))&&(endDate==null||"".equals(endDate))) {
			sbSql.append(" and to_char(A.INVITE_SHOP_DATE,'yyyy-MM-dd') >= '"+startDate+"' ");
		} else {
			sbSql.append(" and A.INVITE_SHOP_DATE<= to_date('"+endDate+" 23:59:59','yyyy-mm-dd hh24:mi:ss')");
			sbSql.append(" and A.INVITE_SHOP_DATE>= to_date('"+startDate+" 00:00:00','yyyy-mm-dd hh24:mi:ss') order by PLAN_DATE desc ");
		}

		return factory.select(sbSql.toString(), null, new JCDynaBeanCallBack());
	}
	
	/*
	 * 获取回访任务信息
	 */
	public List<DynaBean> getRevisitInfo(String dealerId, String userId, String startDate, String endDate) {
		StringBuffer sbSql = new StringBuffer("");
		
		sbSql.append("SELECT A.REVISIT_ID,A.CUSTOMER_ID,B.CUSTOMER_NAME, B.TELEPHONE,TO_CHAR(A.BUY_DATE,'yyyy-MM-dd') AS BUY_DATE, C.SERIES_NAME,D.CODE_DESC, E.NAME, TO_CHAR(A.REVISIT_DATE,'YYYY-MM-DD') AS REVISIT_DATE\r\n");
		sbSql.append("      FROM T_PC_REVISIT A, T_PC_CUSTOMER B, T_PC_INTENT_VEHICLE C,TC_CODE D, TC_USER E\r\n");
		sbSql.append("     WHERE A.CUSTOMER_ID = B.CUSTOMER_ID\r\n");
		sbSql.append("       AND B.INTENT_VEHICLE = C.SERIES_ID\r\n");
		sbSql.append("       AND B.ADVISER = E.USER_ID\r\n");
		sbSql.append("       AND A.REVISIT_TYPE = D.CODE_ID"); 
		sbSql.append("   and a.task_status = "+Constant.TASK_STATUS_01+""); 
		if(userId!=null&&!"".equals(userId)) {
			sbSql.append(" and b.adviser in ("+userId+") \r\n");
		}
		if(dealerId!=null&&!"".equals(dealerId)) {
			sbSql.append(" and b.dealer_id = '"+dealerId+"' ");
		}
		if((startDate==null||"".equals(startDate))&&(endDate==null||"".equals(endDate))) {
			sbSql.append(" and to_char(a.REVISIT_DATE,'yyyy-MM-dd') = to_char(sysdate,'yyyy-MM-dd') ");//当日的跟进任务
		} else if((startDate==null||"".equals(startDate))&&(endDate!=null&&!"".equals(endDate))) {
			sbSql.append(" and a.REVISIT_DATE <= to_date('"+endDate+" 23:59:59','yyyy-MM-dd HH24:mi:ss') ");
		} else if((startDate!=null&&!"".equals(startDate))&&(endDate==null||"".equals(endDate))) {
			sbSql.append(" and a.REVISIT_DATE >= to_date('"+startDate+" 00:00:00','yyyy-MM-dd HH24:mi:ss') ");
		} else {
			sbSql.append(" and a.REVISIT_DATE <= to_date('"+endDate+" 23:59:59','yyyy-MM-dd HH24:mi:ss') ");
			sbSql.append(" and a.REVISIT_DATE >= to_date('"+startDate+" 00:00:00','yyyy-MM-dd HH24:mi:ss') order by a.REVISIT_DATE desc ");
		}
		return factory.select(sbSql.toString(), null, new JCDynaBeanCallBack());
	}
	
	/*
	 * 获取订单任务信息
	 */
	public List<DynaBean> getOrderInfo(String dealerId, String userId, String startDate, String endDate) {
		StringBuffer sbSql = new StringBuffer("");
		
		sbSql.append("SELECT A.ORDER_ID,A.ORDER_STATUS,\r\n");
		sbSql.append("       B.CUSTOMER_NAME,B.CUSTOMER_ID, \r\n");
		sbSql.append("       B.TELEPHONE,\r\n");
		sbSql.append("       C.SERIES_NAME,\r\n");
		sbSql.append("       D.NAME,\r\n");
		sbSql.append("       TO_CHAR(A.ORDER_DATE,'YYYY-MM-DD') AS ORDER_DATE\r\n");
		sbSql.append("  FROM T_PC_ORDER A, T_PC_CUSTOMER B, T_PC_INTENT_VEHICLE C, TC_USER D\r\n");
		sbSql.append(" WHERE A.CUSTOMER_ID = B.CUSTOMER_ID\r\n");
		sbSql.append("   AND B.INTENT_VEHICLE = C.SERIES_ID\r\n");
		sbSql.append("   AND B.ADVISER = D.USER_ID"); 
		sbSql.append("   and a.task_status = "+Constant.TASK_STATUS_01+"");
		sbSql.append("   and (a.order_status = "+Constant.TPC_ORDER_STATUS_01+" OR a.order_status = "+Constant.TPC_ORDER_STATUS_02+" OR a.order_status = "+Constant.TPC_ORDER_STATUS_07+")"); 
		if(userId!=null&&!"".equals(userId)) {
			sbSql.append(" and b.adviser in ("+userId+") \r\n");
		}
		if(dealerId!=null&&!"".equals(dealerId)) {
			sbSql.append(" and b.dealer_id = '"+dealerId+"' ");
		}
		if((startDate==null||"".equals(startDate))&&(endDate==null||"".equals(endDate))) {
			sbSql.append(" and to_char(A.ORDER_DATE,'yyyy-MM-dd') = to_char(sysdate,'yyyy-MM-dd') ");//当日的跟进任务
		} else if((startDate==null||"".equals(startDate))&&(endDate!=null&&!"".equals(endDate))) {
			sbSql.append(" and to_char(A.ORDER_DATE,'yyyy-MM-dd') <= '"+endDate+"' ");
		} else if((startDate!=null&&!"".equals(startDate))&&(endDate==null||"".equals(endDate))) {
			sbSql.append(" and to_char(A.ORDER_DATE,'yyyy-MM-dd') >= '"+startDate+"' ");
		} else {
			sbSql.append(" and A.ORDER_DATE <=to_date('"+endDate+" 23:59;59','yyyy-mm-dd hh24:mi:ss') ");
			sbSql.append(" and A.ORDER_DATE >= to_date('"+startDate+" 00:00:00','yyyy-mm-dd hh24:mi:ss') order by A.ORDER_DATE desc");
		}

		return factory.select(sbSql.toString(), null, new JCDynaBeanCallBack());
	}
	
	/*
	 * 获取交车任务信息
	 */
	public List<DynaBean> getDeliveryInfo(String dealerId, String userId, String startDate, String endDate) {
		StringBuffer sbSql = new StringBuffer("");
		
		sbSql.append("SELECT A.ORDER_DETAIL_ID AS DETAIL_ID,A.ORDER_ID,B.CUSTOMER_NAME,B.CUSTOMER_ID,B.TELEPHONE,DECODE(F.SERIES_NAME,NULL,E.MATERIAL_NAME,F.SERIES_NAME) AS BUYMODEL,D.NAME,TO_CHAR(A.DELIVERY_DATE,'YYYY-MM-DD') AS DELIVERY_DATE\r\n");
		sbSql.append("  FROM T_PC_ORDER A1,T_PC_ORDER_DETAIL A\r\n");
		sbSql.append("  LEFT JOIN T_PC_CUSTOMER B ON A.CUSTOMER_ID = B.CUSTOMER_ID\r\n");
		sbSql.append("  LEFT JOIN TC_USER D ON B.ADVISER = D.USER_ID\r\n");
		sbSql.append("  LEFT JOIN T_PC_INTENT_VEHICLE C ON A.INTENT_MODEL = C.SERIES_ID\r\n");
		sbSql.append("  LEFT JOIN TM_VHCL_MATERIAL E ON A.MATERIAL = E.MATERIAL_ID\r\n");
		sbSql.append("  LEFT JOIN T_PC_INTENT_VEHICLE F ON A.INTENT_MODEL = F.SERIES_ID\r\n");
		sbSql.append("  WHERE 1=1 AND A1.ORDER_ID = A.ORDER_ID AND (A1.ORDER_STATUS = "+Constant.TPC_ORDER_STATUS_01+" OR A1.ORDER_STATUS = "+Constant.TPC_ORDER_STATUS_02+")"); 
		sbSql.append("    AND A.TASK_STATUS = "+Constant.TASK_STATUS_01+"");
		sbSql.append("    AND A.DELIVERY_NUMBER< A.NUM "); 
		if(userId!=null&&!"".equals(userId)) {
			sbSql.append(" and b.adviser in ("+userId+") \r\n");
		}
		if(dealerId!=null&&!"".equals(dealerId)) {
			sbSql.append(" and b.dealer_id = '"+dealerId+"' ");
		}
		if((startDate==null||"".equals(startDate))&&(endDate==null||"".equals(endDate))) {
			sbSql.append(" and to_char(A.DELIVERY_DATE,'yyyy-MM-dd') = to_char(sysdate,'yyyy-MM-dd') ");//当日的跟进任务
		} else if((startDate==null||"".equals(startDate))&&(endDate!=null&&!"".equals(endDate))) {
			sbSql.append(" and to_char(A.DELIVERY_DATE,'yyyy-MM-dd') <= '"+endDate+"' ");
		} else if((startDate!=null&&!"".equals(startDate))&&(endDate==null||"".equals(endDate))) {
			sbSql.append(" and to_char(A.DELIVERY_DATE,'yyyy-MM-dd') >= '"+startDate+"' ");
		} else {
			sbSql.append(" and A.DELIVERY_DATE<= to_date('"+endDate+" 23:59:59','yyyy-mm-dd hh24:mi:ss') ");
			sbSql.append(" and A.DELIVERY_DATE>= to_date('"+startDate+" 00:00:00','yyyy-mm-dd hh24:mi:ss') order by A.DELIVERY_DATE desc");
		}

		return factory.select(sbSql.toString(), null, new JCDynaBeanCallBack());
	}
	
	/*
	 * 获取意向车型一级列表
	 */
	public List<DynaBean> getIntentVehicleA() {
		StringBuffer sql = new StringBuffer("");
		
		sql.append("select SERIES_CODE,SERIES_ID AS MAINID,SERIES_NAME AS NAME,UP_SERIES_ID AS PARENTID from t_pc_intent_Vehicle where up_series_ID is null and status=10011001");
		
		return factory.select(sql.toString(), null, new JCDynaBeanCallBack());
	}
	
	/*
	 * 获取意向车型二级列表
	 */
	public List<DynaBean> getIntentVehicleB() {
		StringBuffer sql = new StringBuffer("");
		
		sql.append("select SERIES_CODE,SERIES_ID AS MAINID,SERIES_NAME AS NAME,UP_SERIES_ID AS PARENTID from t_pc_intent_Vehicle where up_series_ID is not null and status=10011001");
		
		return factory.select(sql.toString(), null, new JCDynaBeanCallBack());
	}
	/*
	 * 获取战败车型一级列表
	 */
	public List<DynaBean> getDefeatVehicleA() {
		StringBuffer sql = new StringBuffer("");
		
		sql.append("select SERIES_ID AS MAINID,SERIES_NAME AS NAME,UP_SERIES_ID AS PARENTID from t_pc_defeat_Vehicle where up_series_ID is null and status=10011001 order by series_name ");
		
		return factory.select(sql.toString(), null, new JCDynaBeanCallBack());
	}
	
	/*
	 * 获取战败车型二级列表
	 */
	public List<DynaBean> getDefeatVehicleB() {
		StringBuffer sql = new StringBuffer("");
		
		sql.append("select SERIES_ID AS MAINID,SERIES_NAME AS NAME,UP_SERIES_ID AS PARENTID from t_pc_defeat_Vehicle where up_series_ID is not null and status=10011001");
		
		return factory.select(sql.toString(), null, new JCDynaBeanCallBack());
	}
	
	/*
	 * 获取战败原因一级列表
	 */
	public List<DynaBean> getDefeatReasonA() {
		StringBuffer sql = new StringBuffer("");
		
		sql.append("select REASON_ID AS MAINID,REASON_NAME AS NAME,UP_REASON_ID AS PARENTID from t_pc_defeat_REASON where up_REASON_ID is null and status=10011001");
		
		return factory.select(sql.toString(), null, new JCDynaBeanCallBack());
	}
	
	/*
	 * 获取战败原因二级列表
	 */
	public List<DynaBean> getDefeatReasonB() {
		StringBuffer sql = new StringBuffer("");
		
		sql.append("select DEFEAT_REASON_ID,REASON_ID AS MAINID,REASON_NAME AS NAME,UP_REASON_ID AS PARENTID from t_pc_defeat_REASON where up_REASON_id is not null and status=10011001");
		
		return factory.select(sql.toString(), null, new JCDynaBeanCallBack());
	}
	
	/*
	 * 获取一级意向车型对应二级列表
	 */
	public List<DynaBean> getIntentVehicleAB(String seriesCode) {
		StringBuffer sql = new StringBuffer("");
		
		sql.append("select SERIES_CODE,SERIES_ID AS MAINID,SERIES_NAME AS NAME,UP_SERIES_ID AS PARENTID from t_pc_intent_Vehicle where up_series_ID is not null and status=10011001 and SERIES_ID= '"+seriesCode+"'");
		
		return factory.select(sql.toString(), null, new JCDynaBeanCallBack());
	}
	
	/*
	 * 获取一级意向车型对应二级列表
	 */
	public List<DynaBean> getIntentVehicleAB2(String seriesCode) {
		StringBuffer sql = new StringBuffer("");
		
		sql.append("select SERIES_CODE,SERIES_ID AS MAINID,SERIES_NAME AS NAME,UP_SERIES_ID AS PARENTID from t_pc_intent_Vehicle where up_series_ID is not null and status=10011001 and up_series_ID= '"+seriesCode+"'");
		
		return factory.select(sql.toString(), null, new JCDynaBeanCallBack());
	}
	
	/*
	 * 从线索中获取客户姓名和联系电话
	 */
	public List<DynaBean> getNameAndTelephone(String leadsCode) {
		StringBuffer sql = new StringBuffer("");
		
		sql.append("select a.CUSTOMER_NAME,a.TELEPHONE,a.LEADS_ORIGIN,a.SEX,a.jc_way as jc_way_code,q.code_desc as jc_way,a.intent_vehicle intent_vehicle,a.intent_car intent_car,a.old_customer_name old_customer_name,a.old_telephone old_telephone,a.old_vehicle_id old_vehicle_id  from t_pc_leads a left join tc_code q on a.jc_way=q.code_id where a.leads_code = '"+leadsCode+"'");
		
		return factory.select(sql.toString(), null, new JCDynaBeanCallBack());
//		return select(sql.toString(), null, null);
	}
	
	/*
	 * 从线索中获取客户姓名和联系电话
	 */
	public List<DynaBean> getNameAndTelephoneByCustomerId(String customerId) {
		StringBuffer sql = new StringBuffer("");
		
		sql.append("select a.CUSTOMER_NAME,a.TELEPHONE from t_pc_customer a where a.customer_id = '"+customerId+"'");
		
		return factory.select(sql.toString(), null, new JCDynaBeanCallBack());
//		return select(sql.toString(), null, null);
	}
	
	/*
	 * 从线索分派表中获取信息
	 */
	public List<DynaBean> getLeadsAllotInfo(String leadsAllotId) {
		StringBuffer sql = new StringBuffer("");
		
		sql.append("select a.leads_code,\r\n");
		sql.append("       c.code_desc,\r\n");
		sql.append("       to_char(a.come_date, 'yyyy-MM-dd HH24:mi:ss') as come_date,\r\n");
		sql.append("       to_char(a.leave_date, 'yyyy-MM-dd HH24:mi:ss') as leave_date,\r\n");
		sql.append("       d.name,a.customer_name,a.telephone,\r\n");
		sql.append("       a.customer_describe\r\n");
		sql.append("  from t_pc_leads a, t_pc_leads_allot b, tc_code c, tc_user d\r\n");
		sql.append(" where a.leads_code = b.leads_code\r\n");
		sql.append("   and a.leads_origin = c.code_id\r\n");
		sql.append("   and b.adviser = d.user_id\r\n");
		sql.append("   and a.leads_type in("+Constant.LEADS_TYPE_02+","+Constant.LEADS_TYPE_03+","+Constant.LEADS_TYPE_04+")"); 
		sql.append("   and b.leads_allot_id = '"+leadsAllotId+"' \r\n");
		
		return factory.select(sql.toString(), null, new JCDynaBeanCallBack());
//		return select(sql.toString(), null, null);
	}
	
	/*
	 * 获取客户基本信息
	 */
	public List<DynaBean> getCustomerInfo(String customerId) {
		StringBuffer sbSql = new StringBuffer("");
		
		sbSql.append("select a.CUSTOMER_CODE,\r\n");
		sbSql.append("       a.customer_name,\r\n");
		sbSql.append("       a.telephone,\r\n");
		sbSql.append("       a.jc_way,\r\n");
		sbSql.append("       b4.code_desc     as jc_way2,\r\n");
		sbSql.append("       a.buy_budget as budget,\r\n");
		sbSql.append("       b5.code_desc     as budget2,\r\n");
		sbSql.append("       a.ctm_prop,\r\n");
		sbSql.append("       b3.code_desc     as ctm_prop2,\r\n");
		sbSql.append("       a.intent_vehicle,\r\n");
		sbSql.append("       a.CTM_RANK,\r\n");
		sbSql.append("       b1.code_desc     as CTM_RANK2,\r\n");
		sbSql.append("       a.SALES_PROGRESS,\r\n");
		sbSql.append("       b2.code_desc     as SALES_PROGRESS2\r\n");
		sbSql.append("  from t_pc_customer a\r\n");
		sbSql.append("  left join tc_code b1 on a.ctm_rank = b1.code_id\r\n");
		sbSql.append("  left join tc_code b2 on a.sales_progress = b2.code_id\r\n");
		sbSql.append("  left join tc_code b3 on a.ctm_prop = b3.code_id\r\n");
		sbSql.append("  left join tc_code b4 on a.jc_way = b4.code_id\r\n");
		sbSql.append("  left join tc_code b5 on a.buy_budget = b5.code_id\r\n");
		sbSql.append(" where 1=1");
		sbSql.append(" and a.customer_id = '"+customerId+"'"); 

		
		return factory.select(sbSql.toString(), null, new JCDynaBeanCallBack());
//		return select(sql.toString(), null, null);
	}
	
	/*
	 * 从销售线索表获取客户基本信息
	 */
	public List<DynaBean> getCustomerInfoByLeads(String leadsCode) {
		StringBuffer sbSql = new StringBuffer("");
		
		sbSql.append("select a.intent_vehicle, " +
				" buy_budget,\n" + 
				" nvl(c3.code_desc,'--请选择--') as buy_budget2,\n" +
				" a.test_driving,\n" + 
				" c4.code_desc as test_driving2,\n" + 
				" a.buy_type,\n" + 
				" c5.code_desc as buy_type2,\n" + 
				" a.customer_type,\n" + 
				" c6.code_desc as customer_type2 \n" + 
				/*" a.intent_type,\n" + 
				" c7.code_desc as intent_type2 "+*/
				"  from t_pc_leads a " +
				"  left join tc_code c3 on a.buy_budget = c3.code_id \n" +
				"  left join tc_code c4 on a.test_driving = c4.code_id \n" + 
				"  left join tc_code c5 on a.buy_type = c5.code_id \n" + 
				"  left join tc_code c6 on a.customer_type = c6.code_id \n" + 
				"  left join tc_code c7 on a.intent_type = c7.code_id "+
				"where a.leads_code = '"+leadsCode+"' \r\n");
		
		return factory.select(sbSql.toString(), null, new JCDynaBeanCallBack());
//		return select(sql.toString(), null, null);
	}
	
	/*
	 * 获取客户基本信息2
	 */
	public List<DynaBean> getCustomerInfo2(String customerId) {
		StringBuffer sql = new StringBuffer("");
		
		sql.append("select a.CUSTOMER_CODE,a.customer_name,a.telephone," +
				"a.jc_way,a.buy_budget,a.ctm_prop,a.intent_vehicle,c.series_name," +
				"a.CTM_RANK,a.SALES_PROGRESS,a.ADDRESS,b.code_desc,a.PAPER_NO,a.BUY_WAY,b2.code_desc as buy_way2" +
				" from t_pc_customer a left join tc_code b on a.PAPER_TYPE = b.code_id left join tc_code b2 on a.buy_way = b2.code_id left join t_pc_intent_vehicle c on a.intent_vehicle=c.series_id where 1=1 and a.customer_id = '"+customerId+"'");
		
		return factory.select(sql.toString(), null, new JCDynaBeanCallBack());
//		return select(sql.toString(), null, null);
	}
	
	/*
	 * 获取客户基本信息2
	 */
	public List<DynaBean> getOldCustomerInfo(String customerId) {
		StringBuffer sql = new StringBuffer("");
		
		sql.append("select tplm.link_man,tplm.link_phone,tplm.old_vehicle_id,tplm.relation_code from t_pc_link_man tplm where tplm.ctm_id = '"+customerId+"'");
		
		return factory.select(sql.toString(), null, new JCDynaBeanCallBack());
//		return select(sql.toString(), null, null);
	}
	
	/*
	 * 获取车主基本信息
	 */
	public List<DynaBean> getOwnerInfo(String orderId) {
		StringBuffer sbSql = new StringBuffer("");
		
		sbSql.append("SELECT A.OWNER_NAME,A.ORDER_STATUS,\r\n");
		sbSql.append("       A.OWNER_PHONE,\r\n");
		sbSql.append("       C1.CODE_DESC AS PAPER_TYPE,\r\n");
		sbSql.append("       A.OWNER_PAPER_TYPE AS OWNER_PAPER_TYPE,\r\n");
		sbSql.append("       A.OWNER_PAPER_NO,\r\n");
		sbSql.append("       D1.REGION_NAME AS PRO,\r\n");
		sbSql.append("       D2.REGION_NAME AS CITY,\r\n");
		sbSql.append("       D3.REGION_NAME AS AREA,\r\n");
		sbSql.append("       A.OWNER_PROVINCE AS PRO2,\r\n");
		sbSql.append("       A.OWNER_CITY AS CITY2,\r\n");
		sbSql.append("       A.OWNER_AREA AS AREA2,\r\n");
		sbSql.append("       A.OWNER_ADDRESS,\r\n");
		sbSql.append("       C2.CODE_DESC AS PRODUCT_SALE,\r\n");
		sbSql.append("       A.NEW_PRODUCT_SALE AS PRODUCT_SALE2,\r\n");
		sbSql.append("       C3.CODE_DESC AS SALES_PROGRESS,\r\n");
		sbSql.append("       B.SALES_PROGRESS AS SALES_PROGRESS2,\r\n");
		sbSql.append("       C4.CODE_DESC AS CTM_RANK,\r\n");
		sbSql.append("       B.CTM_RANK AS CTM_RANK2,\r\n");
		sbSql.append("       C5.CODE_DESC AS DEAL_TYPE,\r\n");
		sbSql.append("       A.DEAL_TYPE AS DEAL_TYPE2,\r\n");
		sbSql.append("       C6.CODE_DESC AS IF_DRIVE,\r\n");
		sbSql.append("       B.IF_DRIVE AS IF_DRIVE2\r\n");
		sbSql.append("  FROM T_PC_ORDER A\r\n");
		sbSql.append("  LEFT JOIN T_PC_CUSTOMER B ON A.CUSTOMER_ID = B.CUSTOMER_ID\r\n");
		sbSql.append("  LEFT JOIN TC_CODE C1 ON TO_CHAR(A.OWNER_PAPER_TYPE) = C1.CODE_ID\r\n");
		sbSql.append("  LEFT JOIN TC_CODE C2 ON TO_CHAR(A.NEW_PRODUCT_SALE) = C2.CODE_ID\r\n");
		sbSql.append("  LEFT JOIN TC_CODE C3 ON TO_CHAR(B.SALES_PROGRESS) = C3.CODE_ID\r\n");
		sbSql.append("  LEFT JOIN TC_CODE C4 ON TO_CHAR(B.CTM_RANK) = C4.CODE_ID\r\n");
		sbSql.append("  LEFT JOIN TC_CODE C5 ON TO_CHAR(A.DEAL_TYPE) = C5.CODE_ID\r\n");
		sbSql.append("  LEFT JOIN TC_CODE C6 ON TO_CHAR(B.IF_DRIVE) = C6.CODE_ID\r\n");
		sbSql.append("  LEFT JOIN TM_REGION D1 ON A.OWNER_PROVINCE = D1.REGION_CODE\r\n");
		sbSql.append("  LEFT JOIN TM_REGION D2 ON A.OWNER_CITY = D2.REGION_CODE\r\n");
		sbSql.append("  LEFT JOIN TM_REGION D3 ON A.OWNER_AREA = D3.REGION_CODE\r\n");
		sbSql.append(" WHERE 1 = 1 AND A.ORDER_ID = "+orderId+""); 

		
		return factory.select(sbSql.toString(), null, new JCDynaBeanCallBack());
//		return select(sql.toString(), null, null);
	}
	
	/*
	 * 获取车主基本信息2
	 */
	public List<DynaBean> getOwnerInfo2(String orderId) {
		StringBuffer sbSql = new StringBuffer("");
		
		sbSql.append("SELECT DECODE(A.OWNER_NAME, NULL, B.CUSTOMER_NAME, A.OWNER_NAME) AS OWNER_NAME,\r\n");
		sbSql.append("           DECODE(A.OWNER_PHONE, NULL, B.TELEPHONE, A.OWNER_PHONE) AS PHONE,\r\n");
		sbSql.append("           DECODE(A.OWNER_PAPER_TYPE, NULL, B.PAPER_TYPE, A.OWNER_PAPER_TYPE) AS PAPER_TYPE,\r\n");
		sbSql.append("           DECODE(C1.CODE_DESC, NULL, C2.CODE_DESC, C1.CODE_DESC) AS PAPER_TYPE2,\r\n");
		sbSql.append("           DECODE(A.OWNER_PAPER_NO, NULL, B.PAPER_NO, A.OWNER_PAPER_NO) AS PAPER_NO,\r\n");
		sbSql.append("           DECODE(A.OWNER_PROVINCE, NULL, B.PROVICE_ID, A.OWNER_PROVINCE) AS PROVINCE,\r\n");
		sbSql.append("           DECODE(A.OWNER_CITY, NULL, B.CITY_ID, A.OWNER_CITY) AS CITY,\r\n");
		sbSql.append("           DECODE(A.OWNER_AREA, NULL, B.TOWN_ID, A.OWNER_AREA) AS AREA,\r\n");
		sbSql.append("           DECODE(A.OWNER_ADDRESS, NULL, B.ADDRESS, A.OWNER_ADDRESS) AS ADDRESS\r\n");
		sbSql.append("      FROM T_PC_ORDER A\r\n");
		sbSql.append("      LEFT JOIN T_PC_CUSTOMER B ON A.CUSTOMER_ID = B.CUSTOMER_ID\r\n");
		sbSql.append("      LEFT JOIN TC_CODE C2 ON B.PAPER_TYPE = C2.CODE_ID\r\n");
		sbSql.append("      LEFT JOIN TC_CODE C1 ON A.OWNER_PAPER_TYPE = C1.CODE_ID"); 

		sbSql.append(" WHERE 1 = 1 AND A.ORDER_ID = "+orderId+""); 

		
		return factory.select(sbSql.toString(), null, new JCDynaBeanCallBack());
//		return select(sql.toString(), null, null);
	}
	
	/*
	 * 获取Table1信息
	 */
	public List<DynaBean> getTable1Info(String orderId,String orderStatus) {
		StringBuffer sbSql = new StringBuffer("");
		
		sbSql.append("SELECT Distinct A.INTENT_MODEL,A.ORDER_ID,TO_CHAR(A.ORDER_DETAIL_ID) AS DETAIL_ID,\r\n");
		sbSql.append("       B.SERIES_NAME AS GROUP_NAME,\r\n");
		sbSql.append("       B.SERIES_CODE AS GROUP_CODE,\r\n");
		sbSql.append("       C.COLOR_CODE AS COLORID,\r\n");
		sbSql.append("       C.COLOR_NAME AS COLOR,\r\n");
		sbSql.append("       A.INTENT_COLOR,\r\n");
		sbSql.append("       A.PRICE,\r\n");
		sbSql.append("       A.NUM,\r\n");
		sbSql.append("       A.AMOUNT,\r\n");
		sbSql.append("       A.DEPOSIT,\r\n");
		sbSql.append("       A.EARNEST,\r\n");
		sbSql.append("       TO_CHAR(A.BALANCE_DATE, 'YYYY-MM-DD') AS BALANCE_DATE,\r\n");
		sbSql.append("       TO_CHAR(A.DELIVERY_DATE, 'YYYY-MM-DD') AS DELIVERY_DATE,\r\n");
		sbSql.append("      b.IS_FOREIGN,\r\n");
		sbSql.append("       A.DELIVERY_NUMBER,A.ORDER_DETAIL_ID\r\n");
		sbSql.append("  FROM T_PC_ORDER_DETAIL A\r\n");
		sbSql.append("  LEFT JOIN T_PC_INTENT_VEHICLE B\r\n");
		sbSql.append("    ON A.INTENT_MODEL = B.SERIES_ID\r\n");
		sbSql.append("  LEFT JOIN vw_material_info C ON A.INTENT_MODEL = C.model_id\r\n");
		sbSql.append("  WHERE A.INTENT_MODEL IS NOT NULL");
		sbSql.append("    AND A.Intent_Color = c.color_code");
		sbSql.append("    AND A.ORDER_ID = "+orderId+"");
		if("60231007".equals(orderStatus)) {//退单状态
			sbSql.append("    AND A.TASK_STATUS IN ("+Constant.TASK_STATUS_01+","+Constant.TASK_STATUS_02+","+Constant.TASK_STATUS_03+")");
		} else {
			sbSql.append("    AND A.TASK_STATUS IN ("+Constant.TASK_STATUS_01+","+Constant.TASK_STATUS_02+")");
		}
		 
		
		return factory.select(sbSql.toString(), null, new JCDynaBeanCallBack());
	}
	
	/*
	 * 获取Table2信息
	 */
	public List<DynaBean> getTable2Info(String orderId,String orderStatus) {
		StringBuffer sbSql = new StringBuffer("");
		
		sbSql.append("SELECT C.VIN,A.MATERIAL,A.ORDER_ID,TO_CHAR(A.ORDER_DETAIL_ID) AS DETAIL_ID,C.VEHICLE_ID,\r\n");
		sbSql.append("       B.MATERIAL_NAME,\r\n");
		sbSql.append("       B.COLOR_NAME AS COLOR,\r\n");
		sbSql.append("       B.COLOR_CODE,\r\n");
		sbSql.append("       A.PRICE,\r\n");
		sbSql.append("       A.NUM,\r\n");
		sbSql.append("       A.AMOUNT,\r\n");
		sbSql.append("       A.DEPOSIT,\r\n");
		sbSql.append("       A.EARNEST,\r\n");
		sbSql.append("       TO_CHAR(A.BALANCE_DATE, 'YYYY-MM-DD') AS BALANCE_DATE,\r\n");
		sbSql.append("       TO_CHAR(A.DELIVERY_DATE, 'YYYY-MM-DD') AS DELIVERY_DATE,\r\n");
		sbSql.append("       A.DELIVERY_NUMBER,A.ORDER_DETAIL_ID\r\n");
		sbSql.append("  FROM T_PC_ORDER_DETAIL A\r\n");
		sbSql.append("  LEFT JOIN TM_VHCL_MATERIAL B \r\n");
		sbSql.append("    ON A.MATERIAL = B.MATERIAL_ID\r\n");
		sbSql.append("  LEFT JOIN TM_VEHICLE C ON A.VEHICLE_ID = C.VEHICLE_ID\r\n");
		sbSql.append("  WHERE A.MATERIAL IS NOT NULL");
		sbSql.append("    AND A.ORDER_ID = "+orderId+""); 
		if("60231007".equals(orderStatus)) {//退单状态
			sbSql.append("    AND A.TASK_STATUS IN ("+Constant.TASK_STATUS_01+","+Constant.TASK_STATUS_02+","+Constant.TASK_STATUS_03+")");
		} else {
			sbSql.append("    AND A.TASK_STATUS IN ("+Constant.TASK_STATUS_01+","+Constant.TASK_STATUS_02+")");
		}
		
		return factory.select(sbSql.toString(), null, new JCDynaBeanCallBack());
	}
	
	/*
	 * 获取Table1信息(订单审核记录表中取)
	 */
	public List<DynaBean> getTable1InfoByAudit(String orderId) {
		StringBuffer sbSql = new StringBuffer("");
		
		sbSql.append("SELECT A.INTENT_MODEL,A.ORDER_ID,TO_CHAR(A.ORDER_DETAIL_ID) AS DETAIL_ID,\r\n");
		sbSql.append("       B.SERIES_NAME AS GROUP_NAME,\r\n");
		sbSql.append("       B.SERIES_CODE AS GROUP_CODE,\r\n");
		sbSql.append("       C.CODE_DESC AS COLOR,\r\n");
		sbSql.append("       A.INTENT_COLOR,\r\n");
		sbSql.append("       A.PRICE,\r\n");
		sbSql.append("       A.NUM,\r\n");
		sbSql.append("       A.AMOUNT,\r\n");
		sbSql.append("       A.DEPOSIT,\r\n");
		sbSql.append("       A.EARNEST,\r\n");
		sbSql.append("       TO_CHAR(A.BALANCE_DATE, 'YYYY-MM-DD') AS BALANCE_DATE,\r\n");
		sbSql.append("       TO_CHAR(A.DELIVERY_DATE, 'YYYY-MM-DD') AS DELIVERY_DATE,\r\n");
		sbSql.append("       A.DELIVERY_NUMBER\r\n");
		sbSql.append("  FROM T_PC_ORDER_AUDIT A1,T_PC_ORDER_DETAIL_AUDIT A\r\n");
		sbSql.append("  LEFT JOIN T_PC_INTENT_VEHICLE B\r\n");
		sbSql.append("    ON A.INTENT_MODEL = B.SERIES_ID\r\n");
		sbSql.append("  LEFT JOIN TC_CODE C ON A.INTENT_COLOR = C.CODE_ID\r\n");
		sbSql.append("  WHERE A1.ORDER_AUDIT_ID = A.ORDER_AUDIT_ID AND A1.MANAGER_AUDIT = "+Constant.DIRECTOR_AUDIT_01+" AND A.INTENT_MODEL IS NOT NULL ");
		sbSql.append("    AND A1.ORDER_ID = "+orderId+""); 
		
		return factory.select(sbSql.toString(), null, new JCDynaBeanCallBack());
	}
	
	/*
	 * 获取Table2信息(订单审核记录表中取)
	 */
	public List<DynaBean> getTable2InfoByAudit(String orderId) {
		StringBuffer sbSql = new StringBuffer("");
		
		sbSql.append("SELECT C.VIN,A.MATERIAL,A.ORDER_ID,TO_CHAR(A.ORDER_DETAIL_ID) AS DETAIL_ID,C.VEHICLE_ID,\r\n");
		sbSql.append("       B.MATERIAL_NAME,\r\n");
		sbSql.append("       B.COLOR_NAME AS COLOR,\r\n");
		sbSql.append("       B.COLOR_CODE,\r\n");
		sbSql.append("       A.PRICE,\r\n");
		sbSql.append("       A.NUM,\r\n");
		sbSql.append("       A.AMOUNT,\r\n");
		sbSql.append("       A.DEPOSIT,\r\n");
		sbSql.append("       A.EARNEST,\r\n");
		sbSql.append("       TO_CHAR(A.BALANCE_DATE, 'YYYY-MM-DD') AS BALANCE_DATE,\r\n");
		sbSql.append("       TO_CHAR(A.DELIVERY_DATE, 'YYYY-MM-DD') AS DELIVERY_DATE,\r\n");
		sbSql.append("       A.DELIVERY_NUMBER\r\n");
		sbSql.append("  FROM T_PC_ORDER_AUDIT A1,T_PC_ORDER_DETAIL_AUDIT A\r\n");
		sbSql.append("  LEFT JOIN TM_VHCL_MATERIAL B \r\n");
		sbSql.append("    ON A.MATERIAL = B.MATERIAL_ID\r\n");
		sbSql.append("  LEFT JOIN TM_VEHICLE C ON A.VIN = C.VEHICLE_ID\r\n");
		sbSql.append("  WHERE A1.ORDER_AUDIT_ID = A.ORDER_AUDIT_ID AND A1.MANAGER_AUDIT = "+Constant.DIRECTOR_AUDIT_01+" AND A.MATERIAL IS NOT NULL");
		sbSql.append("    AND A.ORDER_ID = "+orderId+""); 
		
		return factory.select(sbSql.toString(), null, new JCDynaBeanCallBack());
	}
	
	/*
	 * 获取计划邀约信息
	 */
	public List<DynaBean> getInviteInfo(String inviteId) {
		StringBuffer sql = new StringBuffer("");
		
		sql.append("SELECT A.INVITE_ID,A.AUDIT_REMARK,A.DIRECTOR_AUDIT,A.REQUIREMENT,A.INVITE_TARGET,A.TRUST_DESIGN,A.SCENE_DESIGN,\r\n");
		sql.append("       A.INVITE_TYPE,B1.CODE_DESC AS INVITE_TYPE2,\r\n");
		sql.append("       A.INVITE_WAY,B2.CODE_DESC AS INVITE_WAY2,\r\n");
		sql.append("       A.REMARK,\r\n");
		sql.append("       TO_CHAR(A.PLAN_MEET_DATE, 'YYYY-MM-DD') AS PLAN_MEET_DATE \r\n");
		sql.append("  FROM T_PC_INVITE A LEFT JOIN TC_CODE B1 ON A.INVITE_TYPE = B1.CODE_ID LEFT JOIN TC_CODE B2 ON A.INVITE_WAY = B2.CODE_ID \r\n");
		sql.append(" WHERE 1=1 AND A.INVITE_ID = '"+inviteId+"'"); 

		
		return factory.select(sql.toString(), null, new JCDynaBeanCallBack());
//		return select(sql.toString(), null, null);
	}
	
	/*
	 * 获取邀约到店信息
	 */
	public List<DynaBean> getInviteShopInfo(String inviteShopId) {
		StringBuffer sql = new StringBuffer("");
		
		sql.append("SELECT \r\n");
		sql.append("       TO_CHAR(A.INVITE_SHOP_DATE, 'YYYY-MM-DD') AS INVITE_SHOP_DATE,A.INVITE_ID \r\n");
		sql.append("  FROM T_PC_INVITE_SHOP A\r\n");
		sql.append(" WHERE A.INVITE_SHOP_ID = '"+inviteShopId+"'"); 

		
		return factory.select(sql.toString(), null, new JCDynaBeanCallBack());

	}
	
	/*
	 * 获取意向颜色列表
	 */
	public List<DynaBean> getColorList(String intentVehicle) {
		StringBuffer sql = new StringBuffer("");
		sql.append(" select * from (select '0' code_id,'请选择' code_desc from dual union all " );
		sql.append(" select distinct m.color_code as CODE_ID,m.color_name as CODE_DESC from  VW_MATERIAL_INFO m where m.model_id = '"+intentVehicle+"'");
		sql.append(" ) order by code_id asc ");
		return factory.select(sql.toString(), null, new JCDynaBeanCallBack());

	}
	
	
	/*
	 * 获取意向颜色列表
	 */
	public List<DynaBean> getColorList() {
		StringBuffer sql = new StringBuffer("");
		
		sql.append("select TO_CHAR(CODE_ID) AS CODE_ID,CODE_DESC from tc_code where type=6006");
		
		return factory.select(sql.toString(), null, new JCDynaBeanCallBack());
//		return select(sql.toString(), null, null);
	}

	
	/*
	 * 根据车型获取预售列表
	 */
	public List<DynaBean> getPresellList(String intentVehicle) {
		StringBuffer sql = new StringBuffer("");
		
		sql.append("select NEW_PRODUCT_SALE from t_pc_intent_vehicle tpv  where tpv.series_id='"+intentVehicle+"' ");
		
		return factory.select(sql.toString(), null, new JCDynaBeanCallBack());
	}
	
	/*
	 * 获取曾经首客信息
	 */
	public List<DynaBean> getShouKeCount(String telephone, String dealerId, String leadCode) {
		StringBuffer sql = new StringBuffer("");
		
		sql.append("select count(1) skCount,nvl(sum(confirm),0) clCount, to_char(min(allot_adviser_date),'YYYY-MM-DD') adDate from ( ");
		sql.append(" select tpla.telephone,tpl.customer_name,decode(tpla.if_confirm,60321001,0,1) confirm,tpla.allot_adviser_date from t_pc_leads tpl,t_pc_leads_allot tpla ");
		sql.append(" where tpl.leads_code=tpla.leads_code and tpla.telephone='"+telephone+"' and tpla.dealer_id = '"+dealerId+"' and tpl.jc_way in ('60021001', '60021003', '60021004','60021008') ");
		if(leadCode!=null&&!"".equals(leadCode)){
	        	sql.append(" and tpl.leads_Code !='"+leadCode+"' ");
	       } 
		sql.append("  ) ");
		return factory.select(sql.toString(), null, new JCDynaBeanCallBack());
	}
	

	/*
	 * 获取老客户车架号信息
	 */
	public List<DynaBean> getLaokeVin(String oldVehicleId) {
		StringBuffer sql = new StringBuffer("");
		sql.append("select count(1) oldVin from tt_dealer_actual_sales tdas,tm_vehicle tmv where tdas.vehicle_id=tmv.vehicle_id and tdas.is_return='10041002' and tmv.vin='"+oldVehicleId+"' ");
	
		return factory.select(sql.toString(), null, new JCDynaBeanCallBack());
	}
	
	/*
	 * 获取老客户是否有过推荐客户信息
	 */
	public List<DynaBean> getRecommendCustomer(String telePhone) {
		StringBuffer sql = new StringBuffer("");
		sql.append("select count(1) beCount from t_pc_link_man tplm "); 
	    sql.append(" where tplm.ctm_id in(select tpc.customer_id from t_pc_customer tpc where tpc.telephone='"+telePhone+"') ");
	
		return factory.select(sql.toString(), null, new JCDynaBeanCallBack());
	}
	
	
	/*
	 * 获取订单页面处老客户是否有过推荐客户信息
	 */
	public List<DynaBean> getOrderOldCustomer(String old_vehicle_id) {
		StringBuffer sql = new StringBuffer("");
		sql.append("select count(1) oldCount from t_pc_link_man tplm "); 
	    sql.append(" where tplm.old_vehicle_id='"+old_vehicle_id+"'  ");
	
		return factory.select(sql.toString(), null, new JCDynaBeanCallBack());
	}
	
	
	/*
	 * 获取老客户是否有过推荐客户信息根据客户ID
	 */
	public List<DynaBean> getRecommendCustomerId(String customerId) {
		StringBuffer sql = new StringBuffer("");
		sql.append("select count(1) beCount from t_pc_link_man tplm "); 
	    sql.append(" where tplm.ctm_id ='"+customerId+"' ");
	
		return factory.select(sql.toString(), null, new JCDynaBeanCallBack());
	}
	
	/*
	 * 删除修改之家旧的老客户对应关系!
	 */
	public void deleteLinkMan(String customerId,String relation_code ) {
		StringBuffer sql = new StringBuffer("");
		sql.append("delete from t_pc_link_man tplm where tplm.ctm_id='"+customerId+"' and tplm.relation_code='"+relation_code+"' "); 
	
		delete(sql.toString(), null);
	}
	
	/*
	 * 获取车主与老客户的关联信息
	 */
	public List<DynaBean> getLinkMan(String telePhone) {
		StringBuffer sql = new StringBuffer("");
		sql.append("select tplm.link_man,tplm.link_phone,tplm.old_vehicle_id,tplm.relation_code from t_pc_link_man tplm "); 
	    sql.append(" where tplm.relation_code in('60581001','60581002') and tplm.ctm_id in(select tpc.customer_id from t_pc_customer tpc where tpc.telephone='"+telePhone+"') ");
	
		return factory.select(sql.toString(), null, new JCDynaBeanCallBack());
	}
	
}