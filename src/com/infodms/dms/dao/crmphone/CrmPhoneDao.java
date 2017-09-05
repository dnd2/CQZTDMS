package com.infodms.dms.dao.crmphone;

import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.common.Constant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.dao.common.JCDynaBeanCallBack;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.po3.bean.DynaBean;
import com.infoservice.po3.bean.PO;

public class CrmPhoneDao extends BaseDao {
	public static Logger logger = Logger.getLogger(CrmPhoneDao.class);
	private static final CrmPhoneDao dao = new CrmPhoneDao();

	public static final CrmPhoneDao getInstance() {
		return dao;
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
	/*
	 * 获取已建档客户基本信息
	 */
	public List<DynaBean> customerInfoQuery(String telephone,String dealerId) {
		
		StringBuffer sql = new StringBuffer();
		
		sql.append("SELECT A.CUSTOMER_NAME,A.COME_REASON,A.IF_DRIVE,A.BUY_TYPE,A.CUSTOMER_ID,\r\n");
		sql.append("       A.TELEPHONE,\r\n");
		sql.append("       A.JC_WAY,\r\n");
		sql.append("       C1.CODE_DESC AS JC_WAY2,\r\n");
		sql.append("       A.BUY_BUDGET,\r\n");
		sql.append("       A.CTM_PROP,\r\n");
		sql.append("       C2.CODE_DESC AS CTM_PROP2,\r\n");
		sql.append("       C3.CODE_DESC AS IF_DRIVE2,\r\n");
		sql.append("       C4.CODE_DESC AS COME_REASON2,\r\n");
		sql.append("       C5.CODE_DESC AS BUY_TYPE2,\r\n");
		sql.append("       C6.CODE_DESC AS CTM_RANK2,\r\n");
		sql.append("       C7.CODE_DESC AS SALES_PROGRESS2,\r\n");
		sql.append("       C8.CODE_DESC AS BUY_BUDGET2,\r\n");
		sql.append("       A.INTENT_VEHICLE,\r\n");
		sql.append("       A.CTM_RANK,\r\n");
		sql.append("       A.PROVICE_ID,\r\n");
		sql.append("       A.CITY_ID,\r\n");
		sql.append("       A.TOWN_ID,B.UP_SERIES_ID,A.SALES_PROGRESS \r\n");
		sql.append("  FROM T_PC_INTENT_VEHICLE B,T_PC_CUSTOMER A LEFT JOIN TC_CODE C1 ON A.JC_WAY = C1.CODE_ID" +
				" LEFT JOIN TC_CODE C2 ON A.CTM_PROP = C2.CODE_ID " +
				" LEFT JOIN TC_CODE C3 ON A.IF_DRIVE = C3.CODE_ID " +
				" LEFT JOIN TC_CODE C4 ON A.COME_REASON = C4.CODE_ID " +
				" LEFT JOIN TC_CODE C5 ON A.BUY_TYPE = C5.CODE_ID " +
				" LEFT JOIN TC_CODE C6 ON A.CTM_RANK = C6.CODE_ID " +
				" LEFT JOIN TC_CODE C7 ON A.SALES_PROGRESS = C7.CODE_ID " +
				" LEFT JOIN TC_CODE C8 ON A.BUY_BUDGET = C8.CODE_ID " +
				"where 1=1 AND A.INTENT_VEHICLE = B.SERIES_ID AND A.CTM_TYPE IN(60341002,60341003) "); 

//			sql.append(" and a.customer_name = '"+customerName+"' ");
			sql.append(" and a.telephone = '"+telephone+"' ");
		if (Utility.testString(dealerId)) {//经销商ID
			sql.append(" and a.dealer_id = '"+dealerId+"' ");
		}
	//	if (Utility.testString(adviser)) {//顾问
	//		sql.append(" and a.adviser = '"+adviser+"' ");
	//	}
		return factory.select(sql.toString(), null, new JCDynaBeanCallBack());
	}
	


	/*
	 * 验证用户名密码是否正确
	 */
	public List<Map<String, Object>> doLogin(String userId,String password) {
		
		List<Object> params = new LinkedList<Object>();
		StringBuffer sql = new StringBuffer("");

		sql.append("select * from tc_user where acnt = '"+userId+"' and password =md5('"+password+"')\r\n");

		return dao.pageQuery(sql.toString(), params, getFunName());
	}
	
	/*
	 * 修改密码
	 */
	public void modifyPassword(String userId,String password) {
		
		StringBuffer sql = new StringBuffer("");

		sql.append("update Tc_User SET Password=md5('"+password+"') WHERE 1=1  AND User_Id='"+userId+"'\r\n");

		update(sql.toString(), null);
	}
	
	/*
	 * 获取用户基本信息
	 */
	public List<DynaBean> getUserInfo(String userId) {
		StringBuffer sbSql = new StringBuffer("");
		
		sbSql.append("select tu.user_id,tu.user_type,\r\n");
		sbSql.append("       tu.name,\r\n");
		sbSql.append("       tu.acnt,\r\n");
		sbSql.append("       tr.org_id,\r\n");
		sbSql.append("       tp.pose_name,\r\n");
		sbSql.append("       td.dealer_id\r\n");
		sbSql.append("  from tc_user           tu,\r\n");
		sbSql.append("       tm_company        tm,\r\n");
		sbSql.append("       tm_dealer         td,\r\n");
		sbSql.append("       vw_org_dealer_all vod,\r\n");
		sbSql.append("       tm_org            tr,\r\n");
		sbSql.append("       tc_pose           tp,\r\n");
		sbSql.append("       tr_user_pose      trp\r\n");
		sbSql.append(" where tm.company_id = tu.company_id\r\n");
		sbSql.append("   and td.company_id = tm.company_id\r\n");
		sbSql.append("   and vod.DEALER_ID = td.dealer_id\r\n");
		sbSql.append("   and tr.company_id = tm.company_id\r\n");
		sbSql.append("   and trp.user_id = tu.user_id\r\n");
		sbSql.append("   and trp.pose_id = tp.pose_id\r\n");
		sbSql.append("   and tu.acnt = '"+userId+"'"); 

		
		return factory.select(sbSql.toString(), null, new JCDynaBeanCallBack());
	}
	
	/*
	 * 获取当日未处理线索数量
	 */
	public List<DynaBean> getLeadsNum(String userId,String dealerId,String startDate,String endDate) {
		StringBuffer sbSql = new StringBuffer("");
		
		sbSql.append("select count(*) as num\r\n");
		sbSql.append("  from t_pc_leads a, t_pc_leads_allot b, tc_user c, tc_code d\r\n");
		sbSql.append(" where a.leads_code = b.leads_code\r\n");
		sbSql.append("   and b.adviser = c.user_id\r\n");
		sbSql.append("   and a.leads_origin = d.code_id\r\n");
		if(userId!=null&&!"".equals(userId)) {
			sbSql.append(" and b.adviser in ("+userId+") \r\n");
		}
		sbSql.append("   and b.dealer_id = '"+dealerId+"'\r\n");
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
		sbSql.append("   and b.if_confirm = 60321001\r\n");
		sbSql.append("   and b.status = 10011001\r\n");
		sbSql.append("   and a.leads_status not in (60161004) order by b.ALLOT_ADVISER_DATE desc"); 

		return factory.select(sbSql.toString(), null, new JCDynaBeanCallBack());
	}
	
	/*
	 * 获取当日未处理跟进数量
	 */
	public List<DynaBean> getFollowNum(String userId,String dealerId,String startDate,String endDate) {
		StringBuffer sbSql = new StringBuffer("");
		
		sbSql.append("select count(*) as num\r\n");
		sbSql.append("  from t_pc_follow         a,\r\n");
		sbSql.append("       t_pc_customer       b,\r\n");
		sbSql.append("       t_pc_intent_vehicle c,\r\n");
		sbSql.append("       tc_code             d,\r\n");
		sbSql.append("       tc_user             e\r\n");
		sbSql.append(" where a.customer_id = b.customer_id\r\n");
		sbSql.append("   and b.intent_vehicle = c.series_id\r\n");
		sbSql.append("   and a.old_level = d.code_id\r\n");
		sbSql.append("   and b.ADVISER = e.user_id\r\n");
		sbSql.append("   and a.task_status = 60171001\r\n");
		if(userId!=null&&!"".equals(userId)) {
			sbSql.append(" and b.adviser in ("+userId+") \r\n");
		}
		sbSql.append("   and b.dealer_id = '"+dealerId+"'\r\n");
		if((startDate==null||"".equals(startDate))&&(endDate==null||"".equals(endDate))) {
			sbSql.append(" and to_char(a.follow_date,'yyyy-MM-dd') = to_char(sysdate,'yyyy-MM-dd') ");//当日的跟进任务
		} else if((startDate==null||"".equals(startDate))&&(endDate!=null&&!"".equals(endDate))) {
			sbSql.append(" and a.follow_date <= to_date('"+endDate+"','yyyy-MM-dd') ");
		} else if((startDate!=null&&!"".equals(startDate))&&(endDate==null||"".equals(endDate))) {
			sbSql.append(" and a.follow_date >= to_date('"+startDate+"','yyyy-MM-dd') ");
		} else {
			sbSql.append(" and a.follow_date <= to_date('"+endDate+"','yyyy-MM-dd') ");
			sbSql.append(" and a.follow_date >= to_date('"+startDate+"','yyyy-MM-dd') order by a.follow_date desc ");
		}

		return factory.select(sbSql.toString(), null, new JCDynaBeanCallBack());
	}
	
	/*
	 * 获取当日未处理邀约数量
	 */
	public List<DynaBean> getInviteNum(String userId,String dealerId,String startDate,String endDate) {
		StringBuffer sbSql = new StringBuffer("");
		
		sbSql.append("SELECT COUNT(*) AS NUM FROM (\r\n");
		sbSql.append("SELECT '' AS INVITE_SHOP_ID,\r\n");
		sbSql.append("       A.INVITE_ID,\r\n");
		sbSql.append("       B.CUSTOMER_ID,\r\n");
		sbSql.append("       B.CUSTOMER_NAME,\r\n");
		sbSql.append("       B.TELEPHONE,\r\n");
		sbSql.append("       c.series_name,\r\n");
		sbSql.append("       D.CODE_DESC,\r\n");
		sbSql.append("       '电话邀约' AS invite_type,\r\n");
		sbSql.append("       e.name,\r\n");
		sbSql.append("       TO_CHAR(A.PLAN_INVITE_DATE, 'yyyy-MM-dd') AS PLAN_DATE,\r\n");
		sbSql.append("       A.RESTART_TYPE\r\n");
		sbSql.append("  FROM T_PC_INVITE         A,\r\n");
		sbSql.append("       t_pc_customer       b,\r\n");
		sbSql.append("       T_PC_INTENT_VEHICLE C,\r\n");
		sbSql.append("       tc_code             d,\r\n");
		sbSql.append("       tc_user             e\r\n");
		sbSql.append(" WHERE A.CUSTOMER_ID = B.CUSTOMER_ID\r\n");
		sbSql.append("   AND B.INTENT_VEHICLE = c.series_id\r\n");
		sbSql.append("   AND a.old_level = d.code_id\r\n");
		sbSql.append("   and b.ADVISER = e.user_id\r\n");
		sbSql.append("   and a.task_status = 60171001\r\n");
		sbSql.append("   and (a.DIRECTOR_AUDIT = 60191002 or a.DIRECTOR_AUDIT = 60191004)\r\n");
		if(userId!=null&&!"".equals(userId)) {
			sbSql.append(" and b.adviser in ("+userId+") \r\n");
		}
		sbSql.append("   and b.dealer_id = '"+dealerId+"'\r\n");
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
		sbSql.append("union all\r\n");
		sbSql.append("SELECT TO_CHAR(A.INVITE_SHOP_ID),\r\n");
		sbSql.append("       A.INVITE_ID,\r\n");
		sbSql.append("       B.CUSTOMER_ID,\r\n");
		sbSql.append("       B.CUSTOMER_NAME,\r\n");
		sbSql.append("       B.TELEPHONE,\r\n");
		sbSql.append("       c.series_name,\r\n");
		sbSql.append("       D.CODE_DESC,\r\n");
		sbSql.append("       '邀约到店' AS invite_type,\r\n");
		sbSql.append("       e.name,\r\n");
		sbSql.append("       TO_CHAR(A.INVITE_SHOP_DATE, 'yyyy-MM-dd') AS PLAN_DATE,\r\n");
		sbSql.append("       '' AS RESTART_TYPE\r\n");
		sbSql.append("  FROM T_PC_INVITE_shop    A,\r\n");
		sbSql.append("       t_pc_customer       b,\r\n");
		sbSql.append("       T_PC_INTENT_VEHICLE C,\r\n");
		sbSql.append("       tc_code             d,\r\n");
		sbSql.append("       tc_user             e\r\n");
		sbSql.append(" WHERE A.CUSTOMER_ID = B.CUSTOMER_ID\r\n");
		sbSql.append("   AND B.INTENT_VEHICLE = c.series_id\r\n");
		sbSql.append("   AND a.old_level = d.code_id\r\n");
		sbSql.append("   and b.ADVISER = e.user_id\r\n");
		sbSql.append("   and a.task_status = 60171001\r\n");
		if(userId!=null&&!"".equals(userId)) {
			sbSql.append(" and b.adviser in ("+userId+") \r\n");
		}
		sbSql.append("   and b.dealer_id = '"+dealerId+"'\r\n");
		if((startDate==null||"".equals(startDate))&&(endDate==null||"".equals(endDate))) {
			sbSql.append(" and to_char(A.INVITE_SHOP_DATE,'yyyy-MM-dd') = to_char(sysdate,'yyyy-MM-dd')) ");//当日的跟进任务
		} else if((startDate==null||"".equals(startDate))&&(endDate!=null&&!"".equals(endDate))) {
			sbSql.append(" and to_char(A.INVITE_SHOP_DATE,'yyyy-MM-dd') <= '"+endDate+"' )");
		} else if((startDate!=null&&!"".equals(startDate))&&(endDate==null||"".equals(endDate))) {
			sbSql.append(" and to_char(A.INVITE_SHOP_DATE,'yyyy-MM-dd') >= '"+startDate+"' )");
		} else {
			sbSql.append(" and to_char(A.INVITE_SHOP_DATE,'yyyy-MM-dd') <= '"+endDate+"' ");
			sbSql.append(" and to_char(A.INVITE_SHOP_DATE,'yyyy-MM-dd') >= '"+startDate+"' ) order by PLAN_DATE desc");
		}

		return factory.select(sbSql.toString(), null, new JCDynaBeanCallBack());
	}
	
	/*
	 * 获取当日未处理订单数量
	 */
	public List<DynaBean> getOrderNum(String userId,String dealerId,String startDate,String endDate) {
		StringBuffer sbSql = new StringBuffer("");
		
		sbSql.append("SELECT COUNT(*) AS NUM\r\n");
		sbSql.append("  FROM T_PC_ORDER A, T_PC_CUSTOMER B, T_PC_INTENT_VEHICLE C, TC_USER D\r\n");
		sbSql.append(" WHERE A.CUSTOMER_ID = B.CUSTOMER_ID\r\n");
		sbSql.append("   AND B.INTENT_VEHICLE = C.SERIES_ID\r\n");
		sbSql.append("   AND B.ADVISER = D.USER_ID\r\n");
		sbSql.append("   and a.task_status = 60171001\r\n");
		sbSql.append("   and (a.order_status = 60231001 OR a.order_status = 60231002 OR\r\n");
		sbSql.append("       a.order_status = 60231007)\r\n");
		if(userId!=null&&!"".equals(userId)) {
			sbSql.append(" and b.adviser in ("+userId+") \r\n");
		}
		sbSql.append("   and b.dealer_id = '"+dealerId+"'\r\n");
		if((startDate==null||"".equals(startDate))&&(endDate==null||"".equals(endDate))) {
			sbSql.append(" and to_char(A.ORDER_DATE,'yyyy-MM-dd') = to_char(sysdate,'yyyy-MM-dd') ");//当日的跟进任务
		} else if((startDate==null||"".equals(startDate))&&(endDate!=null&&!"".equals(endDate))) {
			sbSql.append(" and to_char(A.ORDER_DATE,'yyyy-MM-dd') <= '"+endDate+"' ");
		} else if((startDate!=null&&!"".equals(startDate))&&(endDate==null||"".equals(endDate))) {
			sbSql.append(" and to_char(A.ORDER_DATE,'yyyy-MM-dd') >= '"+startDate+"' ");
		} else {
			sbSql.append(" and to_char(A.ORDER_DATE,'yyyy-MM-dd') <= '"+endDate+"' ");
			sbSql.append(" and to_char(A.ORDER_DATE,'yyyy-MM-dd') >= '"+startDate+"' order by A.ORDER_DATE desc");
		}

		return factory.select(sbSql.toString(), null, new JCDynaBeanCallBack());
	}
	
	/*
	 * 获取当日未处理交车数量
	 */
	public List<DynaBean> getDeliveryNum(String userId,String dealerId,String startDate,String endDate) {
		StringBuffer sbSql = new StringBuffer("");
		
		sbSql.append("SELECT COUNT(*) AS NUM\r\n");
		sbSql.append("  FROM T_PC_ORDER A1, T_PC_ORDER_DETAIL A\r\n");
		sbSql.append("  LEFT JOIN T_PC_CUSTOMER B\r\n");
		sbSql.append("    ON A.CUSTOMER_ID = B.CUSTOMER_ID\r\n");
		sbSql.append("  LEFT JOIN TC_USER D\r\n");
		sbSql.append("    ON B.ADVISER = D.USER_ID\r\n");
		sbSql.append("  LEFT JOIN T_PC_INTENT_VEHICLE C\r\n");
		sbSql.append("    ON A.INTENT_MODEL = C.SERIES_ID\r\n");
		sbSql.append("  LEFT JOIN TM_VHCL_MATERIAL E\r\n");
		sbSql.append("    ON A.MATERIAL = E.MATERIAL_ID\r\n");
		sbSql.append("  LEFT JOIN T_PC_INTENT_VEHICLE F\r\n");
		sbSql.append("    ON A.INTENT_MODEL = F.SERIES_ID\r\n");
		sbSql.append(" WHERE 1 = 1\r\n");
		sbSql.append("   AND A1.ORDER_ID = A.ORDER_ID\r\n");
		sbSql.append("   AND (A1.ORDER_STATUS = 60231001 OR A1.ORDER_STATUS = 60231002)\r\n");
		sbSql.append("   AND A.TASK_STATUS = 60171001\r\n");
		sbSql.append("   AND A.DELIVERY_NUMBER < A.NUM\r\n");
		if(userId!=null&&!"".equals(userId)) {
			sbSql.append(" and b.adviser in ("+userId+") \r\n");
		}
		sbSql.append("   and b.dealer_id = '"+dealerId+"'\r\n");
		if((startDate==null||"".equals(startDate))&&(endDate==null||"".equals(endDate))) {
			sbSql.append(" and to_char(A.DELIVERY_DATE,'yyyy-MM-dd') = to_char(sysdate,'yyyy-MM-dd') ");//当日的跟进任务
		} else if((startDate==null||"".equals(startDate))&&(endDate!=null&&!"".equals(endDate))) {
			sbSql.append(" and to_char(A.DELIVERY_DATE,'yyyy-MM-dd') <= '"+endDate+"' ");
		} else if((startDate!=null&&!"".equals(startDate))&&(endDate==null||"".equals(endDate))) {
			sbSql.append(" and to_char(A.DELIVERY_DATE,'yyyy-MM-dd') >= '"+startDate+"' ");
		} else {
			sbSql.append(" and to_char(A.DELIVERY_DATE,'yyyy-MM-dd') <= '"+endDate+"' ");
			sbSql.append(" and to_char(A.DELIVERY_DATE,'yyyy-MM-dd') >= '"+startDate+"' order by A.DELIVERY_DATE desc ");
		}

		return factory.select(sbSql.toString(), null, new JCDynaBeanCallBack());
	}
	
	/*
	 * 获取当日未处理回访数量
	 */
	public List<DynaBean> getRevisitNum(String userId,String dealerId,String startDate,String endDate) {
		StringBuffer sbSql = new StringBuffer("");
		
		sbSql.append("SELECT COUNT(*) AS NUM\r\n");
		sbSql.append("  FROM T_PC_REVISIT        A,\r\n");
		sbSql.append("       T_PC_CUSTOMER       B,\r\n");
		sbSql.append("       T_PC_INTENT_VEHICLE C,\r\n");
		sbSql.append("       TC_CODE             D,\r\n");
		sbSql.append("       TC_USER             E\r\n");
		sbSql.append(" WHERE A.CUSTOMER_ID = B.CUSTOMER_ID\r\n");
		sbSql.append("   AND B.INTENT_VEHICLE = C.SERIES_ID\r\n");
		sbSql.append("   AND B.ADVISER = E.USER_ID\r\n");
		sbSql.append("   AND A.REVISIT_TYPE = D.CODE_ID\r\n");
		sbSql.append("   and a.task_status = 60171001\r\n");
		if(userId!=null&&!"".equals(userId)) {
			sbSql.append(" and b.adviser in ("+userId+") \r\n");
		}
		sbSql.append("   and b.dealer_id = '"+dealerId+"'\r\n");
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
	 * 获取未处理线索信息
	 */
	public List<DynaBean> getLeadsInfo(String userId,String dealerId,String startDate,String endDate) {
		StringBuffer sbSql = new StringBuffer("");
		
		sbSql.append("select nvl(a.customer_id,'') as customer_id,nvl(a.customer_name,'') as customer_name,a.LEADS_CODE,a.LEADS_TYPE,b.LEADS_ALLOT_ID,\r\n");
		sbSql.append("       nvl(a.telephone,'') as telephone,\r\n");
		sbSql.append("       to_char(a.come_date, 'MM-dd HH24:mm') as come_date,\r\n");
		sbSql.append("       to_char(a.leave_date, 'MM-dd HH24:mm') as leave_date,\r\n");
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
			sbSql.append(" and to_char(b.ALLOT_ADVISER_DATE,'yyyy-MM-dd') <= '"+endDate+"' ");
			sbSql.append(" and to_char(b.ALLOT_ADVISER_DATE,'yyyy-MM-dd') >= '"+startDate+"' ");
		}
		sbSql.append(" and b.if_confirm = "+Constant.ADVISER_CONFIRM_01+" ");// 待确认
		sbSql.append(" and b.status = "+Constant.STATUS_ENABLE+" ");// 有效
		sbSql.append(" and a.leads_status not in ("+Constant.LEADS_STATUS_04+") order by b.ALLOT_ADVISER_DATE desc ");// 不为无效

		return factory.select(sbSql.toString(), null, new JCDynaBeanCallBack());
	}
	
	/*
	 * 获取未处理跟进信息
	 */
	public List<DynaBean> getFollowInfo(String userId,String dealerId,String startDate,String endDate) {
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
			sbSql.append(" and a.follow_date <= to_date('"+endDate+"','yyyy-MM-dd') ");
			sbSql.append(" and a.follow_date >= to_date('"+startDate+"','yyyy-MM-dd') order by a.follow_date desc ");
		}

		return factory.select(sbSql.toString(), null, new JCDynaBeanCallBack());
	}
	
	/*
	 * 获取跟进详细信息
	 */
	public List<DynaBean> getFollowDetailInfo(String customerId) {
		StringBuffer sbSql = new StringBuffer("");
		
		sbSql.append("select a.CUSTOMER_ID,a.dealer_id,\r\n");
		sbSql.append("       a.customer_name,\r\n");
		sbSql.append("       a.telephone,\r\n");
		sbSql.append("       a.jc_way,\r\n");
		sbSql.append("       b4.code_desc     as jc_way2,\r\n");
		sbSql.append("       a.buy_budget as budget,\r\n");
		sbSql.append("       b5.code_desc     as budget2,\r\n");
		sbSql.append("       a.ctm_prop,\r\n");
		sbSql.append("       b3.code_desc     as ctm_prop2,\r\n");
		sbSql.append("       a.intent_vehicle,\r\n");
		sbSql.append("       c.series_name,\r\n");
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
		sbSql.append("  left join t_pc_intent_vehicle c on a.intent_vehicle = c.series_id\r\n");
		sbSql.append(" where 1=1 and a.customer_id = '"+customerId+"'"); 


		return factory.select(sbSql.toString(), null, new JCDynaBeanCallBack());
	}
	
	/*
	 * 获取电话邀约详细信息
	 */
	public List<DynaBean> getInviteDetailInfo(String customerId) {
		StringBuffer sbSql = new StringBuffer("");
		
		sbSql.append("select a.CUSTOMER_ID,a.dealer_id,\r\n");
		sbSql.append("       a.customer_name,\r\n");
		sbSql.append("       a.telephone,\r\n");
		sbSql.append("       a.jc_way,\r\n");
		sbSql.append("       b4.code_desc     as jc_way2,\r\n");
		sbSql.append("       a.buy_budget as budget,\r\n");
		sbSql.append("       b5.code_desc     as budget2,\r\n");
		sbSql.append("       a.ctm_prop,\r\n");
		sbSql.append("       b3.code_desc     as ctm_prop2,\r\n");
		sbSql.append("       a.intent_vehicle,\r\n");
		sbSql.append("       c.series_name,\r\n");
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
		sbSql.append("  left join t_pc_intent_vehicle c on a.intent_vehicle = c.series_id\r\n");
		sbSql.append(" where 1=1 and a.customer_id = '"+customerId+"'"); 


		return factory.select(sbSql.toString(), null, new JCDynaBeanCallBack());
	}
	
	/*
	 * 获取电话邀约详细信息2
	 */
	public List<DynaBean> getInviteDetailInfo2(String inviteId) {
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
	 * 获取邀约到店信息中预约到店时间
	 */
	public List<DynaBean> getPreMeetDate(String inviteShopId) {
		StringBuffer sql = new StringBuffer("");
		
		sql.append("SELECT \r\n");
		sql.append("       TO_CHAR(A.INVITE_SHOP_DATE, 'YYYY-MM-DD') AS INVITE_SHOP_DATE,A.INVITE_ID \r\n");
		sql.append("  FROM T_PC_INVITE_SHOP A\r\n");
		sql.append(" WHERE A.INVITE_SHOP_ID = '"+inviteShopId+"'"); 

		
		return factory.select(sql.toString(), null, new JCDynaBeanCallBack());
	}
	
	/*
	 * 获取意向等级下拉列表
	 */
	public List<DynaBean> getCtmRankInfo() {
		StringBuffer sbSql = new StringBuffer("");
		
		sbSql.append("select a.code_id,a.code_desc from tc_code a where a.type='6010' order by a.code_id"); 

		return factory.select(sbSql.toString(), null, new JCDynaBeanCallBack());
	}
	
	/*
	 * 获取意向车型下拉列表
	 */
	public List<DynaBean> getIntentVehicleInfo() {
		StringBuffer sbSql = new StringBuffer("");
		
		sbSql.append("select TO_CHAR(a.series_id) AS series_id,a.series_name,a.up_series_id from t_pc_intent_vehicle a"); 

		return factory.select(sbSql.toString(), null, new JCDynaBeanCallBack());
	}
	
	/*
	 * 获取战败车型下拉列表
	 */
	public List<DynaBean> getDefeatVehicleInfo() {
		StringBuffer sbSql = new StringBuffer("");
		
		sbSql.append("select TO_CHAR(a.series_id) AS series_id,a.series_name,a.up_series_id from t_pc_defeat_vehicle a"); 

		return factory.select(sbSql.toString(), null, new JCDynaBeanCallBack());
	}
	
	/*
	 * 获取销售流程进度下拉列表
	 */
	public List<DynaBean> getSalesProgressInfo() {
		StringBuffer sbSql = new StringBuffer("");
		
		sbSql.append("select a.code_id,a.code_desc from tc_code a where a.type='6037'"); 

		return factory.select(sbSql.toString(), null, new JCDynaBeanCallBack());
	}
	
	/*
	 * 获取购车预算进度下拉列表
	 */
	public List<DynaBean> getBuyBudgetInfo() {
		StringBuffer sbSql = new StringBuffer("");
		
		sbSql.append("select a.code_id,a.code_desc from tc_code a where a.type='6050' order by a.code_id"); 

		return factory.select(sbSql.toString(), null, new JCDynaBeanCallBack());
	}
	
	/*
	 * 获取客户类型下拉列表
	 */
	public List<DynaBean> getCustomerTypeInfo() {
		StringBuffer sbSql = new StringBuffer("");
		
		sbSql.append("select a.code_id,a.code_desc from tc_code a where a.type='6035'"); 

		return factory.select(sbSql.toString(), null, new JCDynaBeanCallBack());
	}
	
	/*
	 * 获取来店契机下拉列表
	 */
	public List<DynaBean> getCollectFashionInfo() {
		StringBuffer sbSql = new StringBuffer("");
		
		sbSql.append("select a.code_id,a.code_desc from tc_code a where a.type='6003'"); 

		return factory.select(sbSql.toString(), null, new JCDynaBeanCallBack());
	}
	
	/*
	 * 获取集客方式下拉列表
	 */
	public List<DynaBean> getJcWayInfo() {
		StringBuffer sbSql = new StringBuffer("");
		
		sbSql.append("select a.code_id,a.code_desc from tc_code a where a.type='6002' and a.code_id not in ('60021005','60021002') "); 

		return factory.select(sbSql.toString(), null, new JCDynaBeanCallBack());
	}
	
	/*
	 * 获取购买类型下拉列表
	 */
	public List<DynaBean> getBuyTypeInfo() {
		StringBuffer sbSql = new StringBuffer("");
		
		sbSql.append("select a.code_id,a.code_desc from tc_code a where a.type='6009'"); 

		return factory.select(sbSql.toString(), null, new JCDynaBeanCallBack());
	}
	
	/*
	 * 获取跟进方式下拉列表
	 */
	public List<DynaBean> getFollowTypeInfo() {
		StringBuffer sbSql = new StringBuffer("");
		
		sbSql.append("select a.code_id,a.code_desc from tc_code a where a.type='6046'"); 

		return factory.select(sbSql.toString(), null, new JCDynaBeanCallBack());
	}
	
	/*
	 * 获取邀约方式下拉列表
	 */
	public List<DynaBean> getInviteTypeInfo() {
		StringBuffer sbSql = new StringBuffer("");
		
		sbSql.append("select a.code_id,a.code_desc from tc_code a where a.type='6047'"); 

		return factory.select(sbSql.toString(), null, new JCDynaBeanCallBack());
	}
	
	/*
	 * 获取战败原因下拉列表
	 */
	public List<DynaBean> getDefeatReasonInfo() {
		StringBuffer sbSql = new StringBuffer("");
		
		sbSql.append("select a.code_id,a.code_desc,case when length(a.type)>4 then a.type else '' end as up_code_id from tc_code a where a.type like '6033%'"); 

		return factory.select(sbSql.toString(), null, new JCDynaBeanCallBack());
	}
	
	/*
	 * 获取邀约类型下拉列表
	 */
	public List<DynaBean> getInviteWayInfo() {
		StringBuffer sbSql = new StringBuffer("");
		
		sbSql.append("select a.code_id,a.code_desc from tc_code a where a.type='6021'"); 

		return factory.select(sbSql.toString(), null, new JCDynaBeanCallBack());
	}
	
	/*
	 * 获取未处理回访信息
	 */
	public List<DynaBean> getRevisitInfo(String userId,String dealerId,String startDate,String endDate) {
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
	 * 获取未处理订单信息
	 */
	public List<DynaBean> getOrderInfo(String userId,String dealerId,String startDate,String endDate) {
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
			sbSql.append(" and to_char(A.ORDER_DATE,'yyyy-MM-dd') <= '"+endDate+"' ");
			sbSql.append(" and to_char(A.ORDER_DATE,'yyyy-MM-dd') >= '"+startDate+"' order by A.ORDER_DATE desc ");
		}

		return factory.select(sbSql.toString(), null, new JCDynaBeanCallBack());
	}
	
	/*
	 * 获取未处理交车信息
	 */
	public List<DynaBean> getDeliveryInfo(String userId,String dealerId,String startDate,String endDate) {
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
			sbSql.append(" and to_char(A.DELIVERY_DATE,'yyyy-MM-dd') <= '"+endDate+"' ");
			sbSql.append(" and to_char(A.DELIVERY_DATE,'yyyy-MM-dd') >= '"+startDate+"' order by A.DELIVERY_DATE desc ");
		}

		return factory.select(sbSql.toString(), null, new JCDynaBeanCallBack());
	}
	
	/*
	 * 获取未处理邀约信息
	 */
	public List<DynaBean> getInviteInfo(String userId,String dealerId,String startDate,String endDate) {
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
			sbSql.append(" and to_char(A.PLAN_INVITE_DATE,'yyyy-MM-dd') <= '"+endDate+"' ");
			sbSql.append(" and to_char(A.PLAN_INVITE_DATE,'yyyy-MM-dd') >= '"+startDate+"' ");
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
			sbSql.append(" and to_char(A.INVITE_SHOP_DATE,'yyyy-MM-dd') <= '"+endDate+"' ");
			sbSql.append(" and to_char(A.INVITE_SHOP_DATE,'yyyy-MM-dd') >= '"+startDate+"' order by PLAN_DATE desc");
		}

		return factory.select(sbSql.toString(), null, new JCDynaBeanCallBack());
	}
	
	/*
	 * 获取提醒信息
	 */
	public List<DynaBean> getRemindInfo(String userId,String dealerId) {
		StringBuffer sbSql = new StringBuffer("");
		
		sbSql.append("SELECT A.CUSTOMER_ID,A.BEREMIND_ID,C.CUSTOMER_NAME,A.REMIND_NUM,\r\n");
		sbSql.append("       A.REMIND_TYPE,\r\n");
		sbSql.append("       B.CODE_DESC AS REMIND_TYPE_NAME,\r\n");
		sbSql.append("       TO_CHAR(A.REMIND_DATE, 'YYYY-MM-DD') AS REMIND_DATE\r\n");
		sbSql.append("  FROM T_PC_REMIND A\r\n");
		sbSql.append("  LEFT JOIN TC_CODE B\r\n");
		sbSql.append("    ON A.REMIND_TYPE = B.CODE_ID\r\n");
		sbSql.append("    LEFT JOIN T_PC_CUSTOMER C ON A.CUSTOMER_ID = C.CUSTOMER_ID\r\n");
		sbSql.append(" WHERE A.REMIND_STATUS = "+Constant.TASK_STATUS_01+"");
		
		if (Utility.testString(dealerId)) {//经销商
			sbSql.append(" and a.dealer_id = '"+dealerId+"' ");
		}
		if (Utility.testString(userId)) {//顾问
			sbSql.append(" and a.adviser = '"+userId+"' ");
		} else {
			sbSql.append(" AND A.REMIND_TYPE IN("+Constant.REMIND_TYPE_01+","+Constant.REMIND_TYPE_10+","+Constant.REMIND_TYPE_11+","+Constant.REMIND_TYPE_13+","+Constant.REMIND_TYPE_14+","+Constant.REMIND_TYPE_15+")");
		}
		sbSql.append(" ORDER BY A.REMIND_DATE DESC ");
		
		return factory.select(sbSql.toString(), null, new JCDynaBeanCallBack());
	}
	
	/*
	 * 获取经理待分派线索列表
	 */
	public List<DynaBean> getLeadsAllotInfo(String dealerId) {
		StringBuffer sql = new StringBuffer("");
		
		sql.append("select to_char(a.leads_code) as leads_code,to_char(a2.leads_allot_id) as leads_allot_id,\r\n");
		sql.append("       c.code_desc as leads_origin,\r\n");
		sql.append("       to_char(a.create_date, 'yyyy-MM-dd') create_date,\r\n");
		sql.append("       b1.region_name as province,\r\n");
		sql.append("       b2.region_name as city,\r\n");
		sql.append("       b3.region_name as area,\r\n");
		sql.append("       a.customer_name,\r\n");
		sql.append("       a.telephone,\r\n");
		sql.append("       null AS DEALER_NAME,\r\n");
		sql.append("       e.name AS ADVISER,\r\n");
		sql.append("       a.remark,f.series_name\r\n");
		sql.append("  from t_pc_leads a\r\n");
		sql.append("  left join t_pc_leads_allot a2 on a.leads_code = a2.leads_code\r\n");
		sql.append("  left join tm_region b1 on a.province = b1.region_code\r\n");
		sql.append("  left join tm_region b2 on a.city = b2.region_code\r\n");
		sql.append("  left join tm_region b3 on a.area = b3.region_code");
		sql.append("  LEFT JOIN TC_CODE C ON A.LEADS_ORIGIN = C.CODE_ID"); 
		sql.append("  left join tm_dealer d on a2.dealer_id = d.dealer_code");
		sql.append("  left join tc_user e on a2.adviser = e.user_id");
		sql.append("  left join t_pc_intent_vehicle f on a.intent_vehicle = f.series_id");
		sql.append(" where 1 = 1 and a2.dealer_id is not null and a2.adviser is null and a.leads_status = "+Constant.LEADS_STATUS_01+" and a.leads_type in ("+Constant.LEADS_TYPE_01+","+Constant.LEADS_TYPE_04+")"); 
		sql.append(" and a2.dealer_id = '"+dealerId+"' \n");
		
		return factory.select(sql.toString(), null, new JCDynaBeanCallBack());
	}
	
	/*
	 * 获取顾问下拉列表
	 */
	public List<DynaBean> getAdviserList(String dealerId) {
		StringBuffer sql = new StringBuffer("");
		
		sql.append("select q.user_id,max(q.name)||' '||count(1) as name from (\r\n");
		sql.append("select * from (\r\n");
		sql.append("select a.user_id, '(' || d.GROUP_NAME || ')' || a.name as name\r\n");
		sql.append("  from tm_company b, tm_dealer c, tc_user a\r\n");
		sql.append("  left join t_pc_group d\r\n");
		sql.append("    on a.group_id = d.group_id\r\n");
		sql.append(" where a.company_id = b.company_id\r\n");
		sql.append("   and b.company_id = c.company_id\r\n");
		sql.append("   and a.pose_rank = '60281004'\r\n");
		if(Utility.testString(dealerId)){
			sql.append("   and c.dealer_id = '"+dealerId+"'"); 
		}
		sql.append("   ) t1 left join (select * from t_pc_customer cu where cu.ctm_type in (60341002, 60341003) and cu.ctm_rank in (60101001, 60101002, 60101003, 60101004)) t2 on t1.user_id = t2.adviser\r\n");
		sql.append("   ) q group by q.user_id"); 

		return factory.select(sql.toString(), null, new JCDynaBeanCallBack());
	}
	
	/*
	 * 获取销售线索信息(未确认，有效)
	 */
	public List<DynaBean> getLeadsInfo(String dealerId,String startDate,String endDate) {
		StringBuffer sbSql = new StringBuffer("");
		
		sbSql.append("select a.customer_name,a.LEADS_CODE,a.LEADS_TYPE,b.LEADS_ALLOT_ID,\r\n");
		sbSql.append("       a.telephone,\r\n");
		sbSql.append("       to_char(a.come_date, 'MM-dd HH24:mm') as come_date,\r\n");
		sbSql.append("       to_char(a.leave_date, 'MM-dd HH24:mm') as leave_date,\r\n");
		sbSql.append("       a.customer_describe,\r\n");
		sbSql.append("       decode(d.code_desc,'自然来店','来店','来电线索','来电',d.code_desc) as code_desc,\r\n");
		sbSql.append("       c.name,\r\n");
		sbSql.append("       to_char(b.allot_adviser_date, 'yyyy-MM-dd') as allot_adviser_date\r\n");
		sbSql.append("  from t_pc_leads a, t_pc_leads_allot b, tc_user c,tc_code d\r\n");
		sbSql.append(" where a.leads_code = b.leads_code\r\n");
		sbSql.append("   and b.adviser = c.user_id\r\n");
		sbSql.append("   and a.leads_origin = d.code_id \r\n"); 
		if(dealerId!=null&&!"".equals(dealerId)) {
			sbSql.append(" and b.dealer_id in ('"+dealerId+"') ");
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
		sbSql.append(" and b.if_confirm = "+Constant.ADVISER_CONFIRM_01+" ");// 待确认
		sbSql.append(" and b.status = "+Constant.STATUS_ENABLE+" ");// 有效
		sbSql.append(" and a.leads_status not in ("+Constant.LEADS_STATUS_04+") ");// 不为无效
		
		return factory.select(sbSql.toString(), null, new JCDynaBeanCallBack());
	}
	
	/*
	 * 获取所有销售线索的数量
	 */
	public List<DynaBean> getAllLeadsInfo(String userId,String dealerId,String startDate,String endDate) {
		StringBuffer sbSql = new StringBuffer("");
		
		sbSql.append("select a.customer_name,a.LEADS_CODE,a.LEADS_TYPE,b.LEADS_ALLOT_ID,\r\n");
		sbSql.append("       a.telephone,\r\n");
		sbSql.append("       to_char(a.come_date, 'yyyy-MM-dd HH24:mm;ss') as come_date,\r\n");
		sbSql.append("       to_char(a.leave_date, 'yyyy-MM-dd HH24:mm;ss') as leave_date,\r\n");
		sbSql.append("       a.customer_describe,\r\n");
		sbSql.append("       d.code_desc,\r\n");
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
			sbSql.append(" and b.dealer_id in ('"+dealerId+"') ");
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
		
		return factory.select(sbSql.toString(), null, new JCDynaBeanCallBack());
	}
	
	/*
	 * 获取跟进任务信息
	 */
	public List<DynaBean> getFollowInfo(String dealerId,String startDate,String endDate) {
		StringBuffer sbSql = new StringBuffer("");
		
		sbSql.append("select A.FOLLOW_ID,A.CUSTOMER_ID,b.customer_name, b.telephone, c.series_name, d.code_desc, e.name, to_char(a.follow_date,'yyyy-MM-dd') as follow_date,a.RESTART_TYPE\r\n");
		sbSql.append("  from t_pc_follow a, t_pc_customer b, t_pc_intent_vehicle c, tc_code d, tc_user e\r\n");
		sbSql.append(" where a.customer_id = b.customer_id\r\n");
		sbSql.append("   and b.intent_vehicle = c.series_id\r\n");
		sbSql.append("   and a.old_level = d.code_id\r\n");
		sbSql.append("   and b.ADVISER = e.user_id\r\n");
		sbSql.append("   and a.task_status = "+Constant.TASK_STATUS_01+"");
		
		if(dealerId!=null&&!"".equals(dealerId)) {
			sbSql.append(" and b.dealer_id in ('"+dealerId+"') ");
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
	 * 获取所有跟进的数量
	 */
	public List<DynaBean> getAllFollowInfo(String userId,String dealerId,String startDate,String endDate) {
		StringBuffer sbSql = new StringBuffer("");
		
		sbSql.append("select A.FOLLOW_ID,A.CUSTOMER_ID,b.customer_name, b.telephone, c.series_name, d.code_desc, e.name, to_char(a.follow_date,'yyyy-MM-dd') as follow_date\r\n");
		sbSql.append("  from t_pc_follow a, t_pc_customer b, t_pc_intent_vehicle c, tc_code d, tc_user e\r\n");
		sbSql.append(" where a.customer_id = b.customer_id\r\n");
		sbSql.append("   and b.intent_vehicle = c.series_id\r\n");
		sbSql.append("   and a.old_level = d.code_id\r\n");
		sbSql.append("   and b.adviser = e.user_id\r\n");
		
		if(userId!=null&&!"".equals(userId)) {
			sbSql.append(" and b.adviser in ("+userId+") \r\n");
		}
		if(dealerId!=null&&!"".equals(dealerId)) {
			sbSql.append(" and b.dealer_id in ('"+dealerId+"') ");
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
	 * 获取邀约任务信息
	 */
	public List<DynaBean> getInviteInfo(String dealerId,String startDate,String endDate) {
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
		
		if(dealerId!=null&&!"".equals(dealerId)) {
			sbSql.append(" and b.dealer_id in ('"+dealerId+"') ");
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
		
		if(dealerId!=null&&!"".equals(dealerId)) {
			sbSql.append(" and b.dealer_id in ('"+dealerId+"') ");
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
	 * 获取所有邀约任务信息
	 */
	public List<DynaBean> getAllInviteInfo(String userId,String dealerId,String startDate,String endDate) {
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
		sbSql.append("       TO_CHAR(A.PLAN_INVITE_DATE,'yyyy-MM-dd') AS PLAN_DATE\r\n");
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
			sbSql.append(" and b.dealer_id in ('"+dealerId+"') ");
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
		sbSql.append("SELECT TO_CHAR(A.INVITE_SHOP_ID),\r\n");
		sbSql.append("       A.INVITE_ID,\r\n");
		sbSql.append("       B.CUSTOMER_ID,\r\n");
		sbSql.append("       B.CUSTOMER_NAME,\r\n");
		sbSql.append("       B.TELEPHONE,\r\n");
		sbSql.append("       c.series_name,\r\n");
		sbSql.append("       D.CODE_DESC,\r\n");
		sbSql.append("       '邀约到店' AS invite_type,\r\n");
		sbSql.append("       e.name,\r\n");
		sbSql.append("       TO_CHAR(A.INVITE_SHOP_DATE,'yyyy-MM-dd') AS PLAN_DATE\r\n");
		sbSql.append("  FROM T_PC_INVITE_shop A, t_pc_customer b, T_PC_INTENT_VEHICLE C,tc_code d, tc_user e\r\n");
		sbSql.append(" WHERE A.CUSTOMER_ID = B.CUSTOMER_ID\r\n");
		sbSql.append("   AND B.INTENT_VEHICLE = c.series_id\r\n");
		sbSql.append("   AND a.old_level = d.code_id\r\n");
		sbSql.append("   and b.adviser = e.user_id"); 
		
		if(dealerId!=null&&!"".equals(dealerId)) {
			sbSql.append(" and b.dealer_id in ('"+dealerId+"') ");
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
	 * 获取订单任务信息
	 */
	public List<DynaBean> getOrderInfo(String dealerId,String startDate,String endDate) {
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
		
		if(dealerId!=null&&!"".equals(dealerId)) {
			sbSql.append(" and b.dealer_id in ('"+dealerId+"') ");
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
	 * 获取所有订单任务信息
	 */
	public List<DynaBean> getAllOrderInfo(String userId,String dealerId,String startDate,String endDate) {
		StringBuffer sbSql = new StringBuffer("");
		
		sbSql.append("SELECT A.ORDER_ID,\r\n");
		sbSql.append("       B.CUSTOMER_NAME,B.CUSTOMER_ID, \r\n");
		sbSql.append("       B.TELEPHONE,\r\n");
		sbSql.append("       C.SERIES_NAME,\r\n");
		sbSql.append("       D.NAME,\r\n");
		sbSql.append("       TO_CHAR(A.ORDER_DATE,'YYYY-MM-DD') AS ORDER_DATE\r\n");
		sbSql.append("  FROM T_PC_ORDER A, T_PC_CUSTOMER B, T_PC_INTENT_VEHICLE C, TC_USER D\r\n");
		sbSql.append(" WHERE A.CUSTOMER_ID = B.CUSTOMER_ID\r\n");
		sbSql.append("   AND B.INTENT_VEHICLE = C.SERIES_ID\r\n");
		sbSql.append("   AND B.ADVISER = D.USER_ID"); 
//		sbSql.append("   and a.order_status = "+Constant.TPC_ORDER_STATUS_01+""); 
		
		if(userId!=null&&!"".equals(userId)) {
			sbSql.append(" and b.adviser in ("+userId+") \r\n");
		}
		if(dealerId!=null&&!"".equals(dealerId)) {
			sbSql.append(" and b.dealer_id in ('"+dealerId+"') ");
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
	 * 获取交车任务信息
	 */
	public List<DynaBean> getDeliveryInfo(String dealerId,String startDate,String endDate) {
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
		
		if(dealerId!=null&&!"".equals(dealerId)) {
			sbSql.append(" and b.dealer_id in ('"+dealerId+"') ");
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
	 * 获取所有交车任务信息
	 */
	public List<DynaBean> getAllDeliveryInfo(String userId,String dealerId,String startDate,String endDate) {
		StringBuffer sbSql = new StringBuffer("");
		
		sbSql.append("SELECT A.ORDER_DETAIL_ID AS DETAIL_ID,A.ORDER_ID,B.CUSTOMER_NAME,B.CUSTOMER_ID,B.TELEPHONE,DECODE(F.GROUP_NAME,NULL,E.MATERIAL_NAME,F.GROUP_NAME) AS BUYMODEL,D.NAME,TO_CHAR(A.DELIVERY_DATE,'YYYY-MM-DD') AS DELIVERY_DATE\r\n");
		sbSql.append("  FROM T_PC_ORDER_DETAIL A\r\n");
		sbSql.append("  LEFT JOIN T_PC_CUSTOMER B ON A.CUSTOMER_ID = B.CUSTOMER_ID\r\n");
		sbSql.append("  LEFT JOIN TC_USER D ON B.ADVISER = D.USER_ID\r\n");
		sbSql.append("  LEFT JOIN T_PC_INTENT_VEHICLE C ON A.INTENT_MODEL = C.SERIES_ID\r\n");
		sbSql.append("  LEFT JOIN TM_VHCL_MATERIAL E ON A.MATERIAL = E.MATERIAL_ID\r\n");
		sbSql.append("  LEFT JOIN TM_VHCL_MATERIAL_GROUP F ON A.INTENT_MODEL = F.GROUP_ID\r\n");
		sbSql.append("  WHERE 1=1"); 
		sbSql.append("    AND A.DELIVERY_NUMBER<= A.NUM "); 
		
		if(userId!=null&&!"".equals(userId)) {
			sbSql.append(" and b.adviser in ("+userId+") \r\n");
		}
		if(dealerId!=null&&!"".equals(dealerId)) {
			sbSql.append(" and b.dealer_id in ('"+dealerId+"') ");
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
	 * 获取回访任务信息
	 */
	public List<DynaBean> getRevisitInfo(String dealerId,String startDate,String endDate) {
		StringBuffer sbSql = new StringBuffer("");
		
		sbSql.append("SELECT A.REVISIT_ID,A.CUSTOMER_ID,B.CUSTOMER_NAME, B.TELEPHONE,TO_CHAR(A.BUY_DATE,'yyyy-MM-dd') AS BUY_DATE, C.SERIES_NAME,D.CODE_DESC, E.NAME, TO_CHAR(A.REVISIT_DATE,'YYYY-MM-DD') AS REVISIT_DATE\r\n");
		sbSql.append("      FROM T_PC_REVISIT A, T_PC_CUSTOMER B, T_PC_INTENT_VEHICLE C,TC_CODE D, TC_USER E\r\n");
		sbSql.append("     WHERE A.CUSTOMER_ID = B.CUSTOMER_ID\r\n");
		sbSql.append("       AND B.INTENT_VEHICLE = C.SERIES_ID\r\n");
		sbSql.append("       AND B.ADVISER = E.USER_ID\r\n");
		sbSql.append("       AND A.REVISIT_TYPE = D.CODE_ID"); 
		sbSql.append("   and a.task_status = "+Constant.TASK_STATUS_01+""); 
		
		if(dealerId!=null&&!"".equals(dealerId)) {
			sbSql.append(" and b.dealer_id in ('"+dealerId+"') ");
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
	 * 获取所有回访任务信息
	 */
	public List<DynaBean> getAllRevisitInfo(String userId,String dealerId,String startDate,String endDate) {
		StringBuffer sbSql = new StringBuffer("");
		
		sbSql.append("SELECT A.REVISIT_ID,A.CUSTOMER_ID,B.CUSTOMER_NAME, B.TELEPHONE, C.SERIES_NAME,D.CODE_DESC, E.NAME, TO_CHAR(A.REVISIT_DATE,'YYYY-MM-DD') AS REVISIT_DATE\r\n");
		sbSql.append("      FROM T_PC_REVISIT A, T_PC_CUSTOMER B, T_PC_INTENT_VEHICLE C,TC_CODE D, TC_USER E\r\n");
		sbSql.append("     WHERE A.CUSTOMER_ID = B.CUSTOMER_ID\r\n");
		sbSql.append("       AND B.INTENT_VEHICLE = C.SERIES_ID\r\n");
		sbSql.append("       AND B.ADVISER = E.USER_ID\r\n");
		sbSql.append("       AND A.REVISIT_TYPE = D.CODE_ID"); 
		
		if(userId!=null&&!"".equals(userId)) {
			sbSql.append(" and b.adviser in ("+userId+") \r\n");
		}
		if(dealerId!=null&&!"".equals(dealerId)) {
			sbSql.append(" and b.dealer_id in ('"+dealerId+"') ");
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
	 * 潜客等级统计数据按车型
	 */
	public List<DynaBean> getCustomerInfoBySeries(String userId,String dealerId) {
		StringBuffer sbSql = new StringBuffer("");
		
		sbSql.append("select b.series_name as SERIES_NAME,\r\n");
		sbSql.append("       sum(case when to_char(to_date(d.finish_date,'yyyy-MM-dd HH24:mi:ss'),'MM') = to_char(sysdate,'MM') and d.new_level = 60101005 then 1 else 0 end) AS O,\r\n");
		sbSql.append("       --sum(decode(d.new_level, 60101005, 1, 0)) as O,\r\n");
		sbSql.append("       sum(decode(d.new_level, 60101001, 1, 0)) as H,\r\n");
		sbSql.append("       sum(decode(d.new_level, 60101002, 1, 0)) as A,\r\n");
		sbSql.append("       sum(decode(d.new_level, 60101003, 1, 0)) as B,\r\n");
		sbSql.append("       sum(decode(d.new_level, 60101004, 1, 0)) as C,\r\n");
		sbSql.append("       sum(case when to_char(to_date(d.finish_date,'yyyy-MM-dd HH24:mi:ss'),'MM') = to_char(sysdate,'MM') and d.new_level = 60101006 then 1 else 0 end) AS E,\r\n");
		sbSql.append("       --sum(decode(d.new_level, 60101006, 1, 0)) as E,\r\n");
		sbSql.append("       sum(case when to_char(to_date(d.finish_date,'yyyy-MM-dd HH24:mi:ss'),'MM') = to_char(sysdate,'MM') and d.new_level = 60101007 then 1 else 0 end) AS L\r\n");
		sbSql.append("       --sum(decode(d.new_level, 60101007, 1, 0)) as L\r\n");
		sbSql.append("  from t_pc_customer a,\r\n");
		sbSql.append("       t_pc_intent_Vehicle b,\r\n");
		sbSql.append("       (select c.customer_id,\r\n");
		sbSql.append("               substr(c.finish_date, 0, 19) as finish_date,\r\n");
		sbSql.append("               substr(c.finish_date, 20) as new_level\r\n");
		sbSql.append("          from (select b.customer_id, max(b.finish_date) as finish_date\r\n");
		sbSql.append("                  from (select a1.customer_id,\r\n");
		sbSql.append("                               to_char(a1.finish_date, 'yyyy-MM-dd HH24:mi:ss') || '' ||\r\n");
		sbSql.append("                               a1.new_level finish_date\r\n");
		sbSql.append("                          from t_pc_follow a1\r\n");
		sbSql.append("                         where a1.task_status = 60171002\r\n");
		sbSql.append("                        union all\r\n");
		sbSql.append("                        select a2.customer_id,\r\n");
		sbSql.append("                               to_char(a2.finish_date, 'yyyy-MM-dd HH24:mi:ss') || '' ||\r\n");
		sbSql.append("                               a2.new_level finish_date\r\n");
		sbSql.append("                          from t_pc_invite a2\r\n");
		sbSql.append("                         where a2.task_status = 60171002\r\n");
		sbSql.append("                        union all\r\n");
		sbSql.append("                        select a3.customer_id,\r\n");
		sbSql.append("                               to_char(a3.finish_date, 'yyyy-MM-dd HH24:mi:ss') || '' ||\r\n");
		sbSql.append("                               a3.new_level finish_date\r\n");
		sbSql.append("                          from t_pc_invite_shop a3\r\n");
		sbSql.append("                         where a3.task_status = 60171002\r\n");
		sbSql.append("                        union all\r\n");
		sbSql.append("                        select a4.customer_id,\r\n");
		sbSql.append("                               to_char(a4.finish_date, 'yyyy-MM-dd HH24:mi:ss') || '' ||\r\n");
		sbSql.append("                               a4.new_level finish_date\r\n");
		sbSql.append("                          from t_pc_order a4\r\n");
		sbSql.append("                         where a4.task_status = 60171002) b\r\n");
		sbSql.append("                 group by b.customer_id) c) d\r\n");
		sbSql.append(" where 1 = 1\r\n");
		sbSql.append("   and a.intent_vehicle = b.series_id\r\n");
		sbSql.append("   and a.customer_id = d.customer_id\r\n");
		if(userId!=null&&!"".equals(userId)) {
			sbSql.append(" and a.adviser in ("+userId+") \r\n");
		}
		if(dealerId!=null&&!"".equals(dealerId)) {
			sbSql.append(" and a.dealer_id in ("+dealerId+") \r\n");
		}

		sbSql.append(" group by b.series_id,b.series_name order by b.series_id"); 
		
		return factory.select(sbSql.toString(), null, new JCDynaBeanCallBack());
	}
	
	/*
	 * 潜客等级统计数据按经销商
	 */
	public List<DynaBean> getCustomerInfoByDealer(String userId,String dealerId) {
		StringBuffer sbSql = new StringBuffer("");
		
		sbSql.append("select '一级经销商' as dealer_level,'1' as descnum,'1000' as descnum2,\r\n");
		sbSql.append("       sum(case when to_char(to_date(d.finish_date,'yyyy-MM-dd HH24:mi:ss'),'MM') = to_char(sysdate,'MM') and d.new_level = 60101005 then 1 else 0 end) AS O,\r\n");
		sbSql.append("       --sum(decode(d.new_level, 60101005, 1, 0)) as O,\r\n");
		sbSql.append("       sum(decode(d.new_level, 60101001, 1, 0)) as H,\r\n");
		sbSql.append("       sum(decode(d.new_level, 60101002, 1, 0)) as A,\r\n");
		sbSql.append("       sum(decode(d.new_level, 60101003, 1, 0)) as B,\r\n");
		sbSql.append("       sum(decode(d.new_level, 60101004, 1, 0)) as C,\r\n");
		sbSql.append("       sum(case when to_char(to_date(d.finish_date,'yyyy-MM-dd HH24:mi:ss'),'MM') = to_char(sysdate,'MM') and d.new_level = 60101006 then 1 else 0 end) AS E,\r\n");
		sbSql.append("       --sum(decode(d.new_level, 60101006, 1, 0)) as E,\r\n");
		sbSql.append("       sum(case when to_char(to_date(d.finish_date,'yyyy-MM-dd HH24:mi:ss'),'MM') = to_char(sysdate,'MM') and d.new_level = 60101007 then 1 else 0 end) AS L\r\n");
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
		sbSql.append("                        union all\r\n");
		sbSql.append("                        select a2.customer_id,\r\n");
		sbSql.append("                               to_char(a2.finish_date, 'yyyy-MM-dd HH24:mi:ss') || '' ||\r\n");
		sbSql.append("                               a2.new_level finish_date\r\n");
		sbSql.append("                          from t_pc_invite a2\r\n");
		sbSql.append("                         where a2.task_status = 60171002\r\n");
		sbSql.append("                        union all\r\n");
		sbSql.append("                        select a3.customer_id,\r\n");
		sbSql.append("                               to_char(a3.finish_date, 'yyyy-MM-dd HH24:mi:ss') || '' ||\r\n");
		sbSql.append("                               a3.new_level finish_date\r\n");
		sbSql.append("                          from t_pc_invite_shop a3\r\n");
		sbSql.append("                         where a3.task_status = 60171002\r\n");
		sbSql.append("                        union all\r\n");
		sbSql.append("                        select a4.customer_id,\r\n");
		sbSql.append("                               to_char(a4.finish_date, 'yyyy-MM-dd HH24:mi:ss') || '' ||\r\n");
		sbSql.append("                               a4.new_level finish_date\r\n");
		sbSql.append("                          from t_pc_order a4\r\n");
		sbSql.append("                         where a4.task_status = 60171002) b\r\n");
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
		sbSql.append("       sum(case when to_char(to_date(d.finish_date,'yyyy-MM-dd'),'MM') = to_char(sysdate,'MM') and d.new_level = 60101005 then 1 else 0 end) AS O,\r\n");
		sbSql.append("       --sum(decode(d.new_level, 60101005, 1, 0)) as O,\r\n");
		sbSql.append("       sum(decode(d.new_level, 60101001, 1, 0)) as H,\r\n");
		sbSql.append("       sum(decode(d.new_level, 60101002, 1, 0)) as A,\r\n");
		sbSql.append("       sum(decode(d.new_level, 60101003, 1, 0)) as B,\r\n");
		sbSql.append("       sum(decode(d.new_level, 60101004, 1, 0)) as C,\r\n");
		sbSql.append("       sum(case when to_char(to_date(d.finish_date,'yyyy-MM-dd'),'MM') = to_char(sysdate,'MM') and d.new_level = 60101006 then 1 else 0 end) AS E,\r\n");
		sbSql.append("       --sum(decode(d.new_level, 60101006, 1, 0)) as E,\r\n");
		sbSql.append("       sum(case when to_char(to_date(d.finish_date,'yyyy-MM-dd'),'MM') = to_char(sysdate,'MM') and d.new_level = 60101007 then 1 else 0 end) AS L\r\n");
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
		sbSql.append("                        union all\r\n");
		sbSql.append("                        select a2.customer_id,\r\n");
		sbSql.append("                               to_char(a2.finish_date, 'yyyy-MM-dd') || '' ||\r\n");
		sbSql.append("                               a2.new_level finish_date\r\n");
		sbSql.append("                          from t_pc_invite a2\r\n");
		sbSql.append("                         where a2.task_status = 60171002\r\n");
		sbSql.append("                        union all\r\n");
		sbSql.append("                        select a3.customer_id,\r\n");
		sbSql.append("                               to_char(a3.finish_date, 'yyyy-MM-dd') || '' ||\r\n");
		sbSql.append("                               a3.new_level finish_date\r\n");
		sbSql.append("                          from t_pc_invite_shop a3\r\n");
		sbSql.append("                         where a3.task_status = 60171002\r\n");
		sbSql.append("                        union all\r\n");
		sbSql.append("                        select a4.customer_id,\r\n");
		sbSql.append("                               to_char(a4.finish_date, 'yyyy-MM-dd') || '' ||\r\n");
		sbSql.append("                               a4.new_level finish_date\r\n");
		sbSql.append("                          from t_pc_order a4\r\n");
		sbSql.append("                         where a4.task_status = 60171002) b\r\n");
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
	}
	
	/*
	 * 潜客等级统计数据按经销商2
	 */
	public List<DynaBean> getCustomerInfoByDealer2(String userId,String dealerId) {
		StringBuffer sbSql = new StringBuffer("");
		
		sbSql.append("select '一级经销商' as dealer_level,'1' as descnum,'1000' as descnum2,\r\n");
		sbSql.append("       sum(case when to_char(to_date(d.finish_date,'yyyy-MM-dd HH24:mi:ss'),'MM') = to_char(sysdate,'MM') and d.new_level = 60101005 then 1 else 0 end) AS O,\r\n");
		sbSql.append("       --sum(decode(d.new_level, 60101005, 1, 0)) as O,\r\n");
		sbSql.append("       sum(decode(d.new_level, 60101001, 1, 0)) as H,\r\n");
		sbSql.append("       sum(decode(d.new_level, 60101002, 1, 0)) as A,\r\n");
		sbSql.append("       sum(decode(d.new_level, 60101003, 1, 0)) as B,\r\n");
		sbSql.append("       sum(decode(d.new_level, 60101004, 1, 0)) as C,\r\n");
		sbSql.append("       sum(case when to_char(to_date(d.finish_date,'yyyy-MM-dd HH24:mi:ss'),'MM') = to_char(sysdate,'MM') and d.new_level = 60101006 then 1 else 0 end) AS E,\r\n");
		sbSql.append("       --sum(decode(d.new_level, 60101006, 1, 0)) as E,\r\n");
		sbSql.append("       sum(case when to_char(to_date(d.finish_date,'yyyy-MM-dd HH24:mi:ss'),'MM') = to_char(sysdate,'MM') and d.new_level = 60101007 then 1 else 0 end) AS L\r\n");
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
		sbSql.append("                        union all\r\n");
		sbSql.append("                        select a2.customer_id,\r\n");
		sbSql.append("                               to_char(a2.finish_date, 'yyyy-MM-dd HH24:mi:ss') || '' ||\r\n");
		sbSql.append("                               a2.new_level finish_date\r\n");
		sbSql.append("                          from t_pc_invite a2\r\n");
		sbSql.append("                         where a2.task_status = 60171002\r\n");
		sbSql.append("                        union all\r\n");
		sbSql.append("                        select a3.customer_id,\r\n");
		sbSql.append("                               to_char(a3.finish_date, 'yyyy-MM-dd HH24:mi:ss') || '' ||\r\n");
		sbSql.append("                               a3.new_level finish_date\r\n");
		sbSql.append("                          from t_pc_invite_shop a3\r\n");
		sbSql.append("                         where a3.task_status = 60171002\r\n");
		sbSql.append("                        union all\r\n");
		sbSql.append("                        select a4.customer_id,\r\n");
		sbSql.append("                               to_char(a4.finish_date, 'yyyy-MM-dd HH24:mi:ss') || '' ||\r\n");
		sbSql.append("                               a4.new_level finish_date\r\n");
		sbSql.append("                          from t_pc_order a4\r\n");
		sbSql.append("                         where a4.task_status = 60171002) b\r\n");
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
		sbSql.append("       sum(case when to_char(to_date(d.finish_date,'yyyy-MM-dd'),'MM') = to_char(sysdate,'MM') and d.new_level = 60101005 then 1 else 0 end) AS O,\r\n");
		sbSql.append("       --sum(decode(d.new_level, 60101005, 1, 0)) as O,\r\n");
		sbSql.append("       sum(decode(d.new_level, 60101001, 1, 0)) as H,\r\n");
		sbSql.append("       sum(decode(d.new_level, 60101002, 1, 0)) as A,\r\n");
		sbSql.append("       sum(decode(d.new_level, 60101003, 1, 0)) as B,\r\n");
		sbSql.append("       sum(decode(d.new_level, 60101004, 1, 0)) as C,\r\n");
		sbSql.append("       sum(case when to_char(to_date(d.finish_date,'yyyy-MM-dd'),'MM') = to_char(sysdate,'MM') and d.new_level = 60101006 then 1 else 0 end) AS E,\r\n");
		sbSql.append("       --sum(decode(d.new_level, 60101006, 1, 0)) as E,\r\n");
		sbSql.append("       sum(case when to_char(to_date(d.finish_date,'yyyy-MM-dd'),'MM') = to_char(sysdate,'MM') and d.new_level = 60101007 then 1 else 0 end) AS L\r\n");
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
		sbSql.append("                        union all\r\n");
		sbSql.append("                        select a2.customer_id,\r\n");
		sbSql.append("                               to_char(a2.finish_date, 'yyyy-MM-dd') || '' ||\r\n");
		sbSql.append("                               a2.new_level finish_date\r\n");
		sbSql.append("                          from t_pc_invite a2\r\n");
		sbSql.append("                         where a2.task_status = 60171002\r\n");
		sbSql.append("                        union all\r\n");
		sbSql.append("                        select a3.customer_id,\r\n");
		sbSql.append("                               to_char(a3.finish_date, 'yyyy-MM-dd') || '' ||\r\n");
		sbSql.append("                               a3.new_level finish_date\r\n");
		sbSql.append("                          from t_pc_invite_shop a3\r\n");
		sbSql.append("                         where a3.task_status = 60171002\r\n");
		sbSql.append("                        union all\r\n");
		sbSql.append("                        select a4.customer_id,\r\n");
		sbSql.append("                               to_char(a4.finish_date, 'yyyy-MM-dd') || '' ||\r\n");
		sbSql.append("                               a4.new_level finish_date\r\n");
		sbSql.append("                          from t_pc_order a4\r\n");
		sbSql.append("                         where a4.task_status = 60171002) b\r\n");
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
		sbSql.append("       sum(case when to_char(to_date(d.finish_date,'yyyy-MM-dd'),'MM') = to_char(sysdate,'MM') and d.new_level = 60101005 then 1 else 0 end) AS O,\r\n");
		sbSql.append("       --sum(decode(d.new_level, 60101005, 1, 0)) as O,\r\n");
		sbSql.append("       sum(decode(d.new_level, 60101001, 1, 0)) as H,\r\n");
		sbSql.append("       sum(decode(d.new_level, 60101002, 1, 0)) as A,\r\n");
		sbSql.append("       sum(decode(d.new_level, 60101003, 1, 0)) as B,\r\n");
		sbSql.append("       sum(decode(d.new_level, 60101004, 1, 0)) as C,\r\n");
		sbSql.append("       sum(case when to_char(to_date(d.finish_date,'yyyy-MM-dd'),'MM') = to_char(sysdate,'MM') and d.new_level = 60101006 then 1 else 0 end) AS E,\r\n");
		sbSql.append("       --sum(decode(d.new_level, 60101006, 1, 0)) as E,\r\n");
		sbSql.append("       sum(case when to_char(to_date(d.finish_date,'yyyy-MM-dd'),'MM') = to_char(sysdate,'MM') and d.new_level = 60101007 then 1 else 0 end) AS L\r\n");
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
		sbSql.append("                        union all\r\n");
		sbSql.append("                        select a2.customer_id,\r\n");
		sbSql.append("                               to_char(a2.finish_date, 'yyyy-MM-dd') || '' ||\r\n");
		sbSql.append("                               a2.new_level finish_date\r\n");
		sbSql.append("                          from t_pc_invite a2\r\n");
		sbSql.append("                         where a2.task_status = 60171002\r\n");
		sbSql.append("                        union all\r\n");
		sbSql.append("                        select a3.customer_id,\r\n");
		sbSql.append("                               to_char(a3.finish_date, 'yyyy-MM-dd') || '' ||\r\n");
		sbSql.append("                               a3.new_level finish_date\r\n");
		sbSql.append("                          from t_pc_invite_shop a3\r\n");
		sbSql.append("                         where a3.task_status = 60171002\r\n");
		sbSql.append("                        union all\r\n");
		sbSql.append("                        select a4.customer_id,\r\n");
		sbSql.append("                               to_char(a4.finish_date, 'yyyy-MM-dd') || '' ||\r\n");
		sbSql.append("                               a4.new_level finish_date\r\n");
		sbSql.append("                          from t_pc_order a4\r\n");
		sbSql.append("                         where a4.task_status = 60171002) b\r\n");
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
	}
	
	/**
	 * 未完成任务数量
	 * @param map
	 * @return
	 */
	public List<DynaBean> getNoDoNum() {
		StringBuffer sbSql = new StringBuffer("");
		
		sbSql.append("select TU.USER_ID user_id, tu.name, ts.outs, ts.normals, ts.outs+ts.normals as cunt\r\n");
		sbSql.append("  from (select t.user_id, sum(t.out_count) outs, sum(normal_count) normals\r\n");
		sbSql.append("          from (select user_id, 1 out_count,0 normal_count\r\n");
		sbSql.append("                  from t_pc_remind a, tc_user b\r\n");
		sbSql.append("                 where a.adviser = to_char(b.user_id)\r\n");
		sbSql.append("                   and A.REMIND_STATUS = 60171001\r\n");
		sbSql.append("                   and a.remind_date < sysdate\r\n");
		sbSql.append("                union all\r\n");
		sbSql.append("                select user_id, 0 out_count,1 normal_count\r\n");
		sbSql.append("                  from t_pc_remind a, tc_user b\r\n");
		sbSql.append("                 where a.adviser = to_char(b.user_id)\r\n");
		sbSql.append("                   and A.REMIND_STATUS = 60171001\r\n");
		sbSql.append("                   and a.remind_date >= sysdate) t\r\n");
		sbSql.append("         where 1 = 1\r\n");
		sbSql.append("         group by t.user_id) ts,\r\n");
		sbSql.append("       tc_user tu\r\n");
		sbSql.append(" where tu.user_id = ts.user_id"); 

		return factory.select(sbSql.toString(), null, new JCDynaBeanCallBack());
	}
	
	/*
	 * 从线索中获取客户姓名和联系电话
	 */
	public List<DynaBean> getNameAndTelephone(String leadsCode) {
		StringBuffer sbSql = new StringBuffer("");
		
		sbSql.append("select a.CUSTOMER_NAME,a.CUSTOMER_DESCRIBE,\r\n");
		sbSql.append("       a.TELEPHONE,\r\n");
		sbSql.append("       a.LEADS_ORIGIN,\r\n");
		sbSql.append("       a.SEX,\r\n");
		sbSql.append("       a.jc_way        as jc_way_id,\r\n");
		sbSql.append("       q.code_desc     as jc_way,\r\n");
		sbSql.append("		 a.buy_budget as buy_budget_id, q3.code_desc as buy_budget,\r\n");
		sbSql.append("		 a.test_driving as test_driving_id, q4.code_desc as test_driving,\r\n");
		sbSql.append("		 a.buy_type as buy_type_id, q5.code_desc as buy_type,\r\n");
		sbSql.append("		 a.customer_type as customer_type_id, q6.code_desc as customer_type,\r\n");
		sbSql.append("       q2.code_desc    as leads_origin2,\r\n");
		sbSql.append("       to_char(a.come_date,'yyyy-MM-dd HH24:mi:ss') as come_date,\r\n");
		sbSql.append("       to_char(a.leave_date,'yyyy-MM-dd HH24:mi:ss') as leave_date,\r\n");
		sbSql.append("       a.intent_vehicle as intent_vehicle_id,\r\n");
		sbSql.append("       d.series_name as intent_vehicle\r\n");
		sbSql.append("  from t_pc_leads a\r\n");
		sbSql.append("  left join tc_code q\r\n");
		sbSql.append("    on a.jc_way = q.code_id\r\n");
		sbSql.append("  left join tc_code q2\r\n");
		sbSql.append("    on a.leads_origin = q2.code_id\r\n");
		sbSql.append("  left join tc_code q3\r\n");
		sbSql.append("    on a.buy_budget = q3.code_id\r\n");
		sbSql.append("  left join tc_code q4\r\n");
		sbSql.append("    on a.test_driving = q4.code_id\r\n");
		sbSql.append("  left join tc_code q5\r\n");
		sbSql.append("    on a.buy_type = q5.code_id\r\n");
		sbSql.append("  left join tc_code q6\r\n");
		sbSql.append("    on a.customer_type = q6.code_id\r\n");
		sbSql.append("  left join t_pc_intent_vehicle d on a.intent_vehicle = d.series_id");
		sbSql.append("  where a.leads_code ='"+leadsCode+"'"); 
		return factory.select(sbSql.toString(), null, new JCDynaBeanCallBack());
	}
	
	/*
	 * 从线索分派中的顾问名称
	 */
	public List<DynaBean> getAdviserName(String leadsAllotId) {
		StringBuffer sbSql = new StringBuffer("");
		
		sbSql.append("select b.name as adviser_NAME\r\n");
		sbSql.append("  from t_pc_leads_allot a\r\n");
		sbSql.append("  left join tc_user b\r\n");
		sbSql.append("    on a.adviser = b.user_id"); 
		sbSql.append("    where a.leads_allot_id = '"+leadsAllotId+"'"); 

		return factory.select(sbSql.toString(), null, new JCDynaBeanCallBack());
	}
	
	/*
	 * 是否已存在客户建档数据
	 */
	public List<Map<String, Object>> getHasCustomer(String telephone,String dealerId) {
		
		List<Object> params = new LinkedList<Object>();
		StringBuffer sql = new StringBuffer("");

		sql.append("select A.DEALER_ID,A.ADVISER,A.CUSTOMER_ID,A.CTM_TYPE\r\n");
		sql.append("  from t_pc_customer A,TM_DEALER B \r\n");
		sql.append(" where A.DEALER_ID = to_char(B.DEALER_ID) AND a.TELEPHONE = '"+telephone+"'");
		if(dealerId == null) {
			sql.append("   and B.DEALER_ID = ''"); 
		} else {
			sql.append("   and B.DEALER_ID = '"+dealerId+"'"); 
		}

		return dao.pageQuery(sql.toString(), params, getFunName());
	}
	
	/*
	 * 潜客等级统计数据按车型
	 */
	public List<DynaBean> getControlReport(Map<String, String> map) {
		String startDate=map.get("startDate");
		String endDate=map.get("endDate");
		String dealerId=map.get("dealerId");
		String userId=map.get("userId");
		
		StringBuffer sbSql = new StringBuffer("");
		
		sbSql.append("SELECT XX.DEALER_ID,\r\n");
		sbSql.append("       sum(clcount) + sum(wclcount) clcount,\r\n");
		sbSql.append("       sum(clcount) wclcount,\r\n");
		sbSql.append("       sum(cecount) cecount,\r\n");
		sbSql.append("       sum(sk) sk,\r\n");
		sbSql.append("       sum(yy) yy,\r\n");
		sbSql.append("       sum(jk) jk,\r\n");
		sbSql.append("       sum(DD) DD,\r\n");
		sbSql.append("       sum(JC) jc "); 
		sbSql.append("from (select \r\n");
		sbSql.append("       jd.*\r\n");
		sbSql.append("  from (select DEALER_ID,\r\n");
		sbSql.append("               Adviser,\r\n");
		sbSql.append("               sum(clcount) clcount,\r\n");
		sbSql.append("               sum(wclcount) wclcount,\r\n");
		sbSql.append("               sum(cecount) cecount,\r\n");
		sbSql.append("               sum(sk) sk,\r\n");
		sbSql.append("               sum(yy) yy,\r\n");
		sbSql.append("               sum(jk) jk,\r\n");
		sbSql.append("               sum(DD) DD,\r\n");
		sbSql.append("               sum(JC) jc\r\n");
		sbSql.append("          from ("); 
		sbSql.append(" SELECT to_char(a.DEALER_ID) DEALER_ID,\r\n");
		sbSql.append("       to_char(a.Adviser) Adviser,\r\n");
		sbSql.append("       a.clcount,\r\n");
		sbSql.append("       a.wclcount,\r\n");
		sbSql.append("       DECODE(b.cecount, '', 0, b.cecount) cecount,\r\n");
		sbSql.append("       0 sk,\r\n");
		sbSql.append("       0 yy,\r\n");
		sbSql.append("       0 jk,\r\n");
		sbSql.append("       0 DD,\r\n");
		sbSql.append("       0 JC\r\n");
		sbSql.append("  FROM (SELECT A.DEALER_ID,\r\n");
		sbSql.append("               A.ADVISER,\r\n");
		sbSql.append("               decode(IF_CONFIRM, 60321001, 0, 1) CLCOUNT,\r\n");
		sbSql.append("               decode(IF_CONFIRM, 60321002, 0, 1) WCLCOUNT,\r\n");
		sbSql.append("               a.leads_code\r\n");
		sbSql.append("          from (SELECT B.DEALER_ID, B.ADVISER, B.IF_CONFIRM, tl.leads_code\r\n");
		sbSql.append("                  FROM T_PC_LEADS_ALLOT B, T_PC_LEADS TL\r\n");
		sbSql.append("                  LEFT JOIN T_PC_INTENT_VEHICLE TPIV\r\n");
		sbSql.append("                    ON Tl.INTENT_VEHICLE = TPIV.SERIES_ID\r\n");
		sbSql.append("                 WHERE TL.LEADS_CODE = B.LEADS_CODE -- AND  B.IF_CONFIRM  ='60321002'\r\n");
		if(!CommonUtils.isNullString(startDate)){
			sbSql.append("                   AND TL.CREATE_DATE >=\r\n");
			sbSql.append("                       TO_DATE('"+startDate+" 00:00:00', 'YYYY-MM-DD HH24:MI:SS')\r\n");
		}
		if(!CommonUtils.isNullString(endDate)){
			sbSql.append("                   AND TL.CREATE_DATE <=\r\n");
			sbSql.append("                       TO_DATE('"+endDate+" 23:59:59', 'YYYY-MM-DD HH24:MI:SS')\r\n");
		}
		if(!CommonUtils.isNullString(dealerId)){
			sbSql.append("                   AND B.DEALER_ID = '"+dealerId+"'\r\n");
		}
		sbSql.append(") A) A\r\n");
//		sbSql.append("                   AND TL.CREATE_DATE >=\r\n");
//		sbSql.append("                       TO_DATE('2015-04-01 00:00:00', 'YYYY-MM-DD HH24:MI:SS')\r\n");
//		sbSql.append("                   AND TL.CREATE_DATE <=\r\n");
//		sbSql.append("                       TO_DATE('2015-04-10 23:59:59', 'YYYY-MM-DD HH24:MI:SS')) A) A\r\n");
		sbSql.append("  LEFT JOIN (SELECT B.DEALER_ID,\r\n");
		sbSql.append("                    B.ADVISER,\r\n");
		sbSql.append("                    Tl.LEADS_STATUS,\r\n");
		sbSql.append("                    COUNT(1) AS CECOUNT,\r\n");
		sbSql.append("                    tl.leads_code\r\n");
		sbSql.append("               FROM TC_CODE TC, T_PC_LEADS_ALLOT B, T_PC_LEADS TL\r\n");
		sbSql.append("               LEFT JOIN T_PC_INTENT_VEHICLE TPIV\r\n");
		sbSql.append("                 ON Tl.INTENT_VEHICLE = TPIV.SERIES_ID\r\n");
		sbSql.append("              WHERE TC.CODE_ID = TL.LEADS_ORIGIN\r\n");
		sbSql.append("                AND TL.LEADS_CODE = B.LEADS_CODE\r\n");
		sbSql.append("                AND B.IF_CONFIRM = '60321002'\r\n");
		sbSql.append("                and ceil((B.CONFIRM_DATE - tl.Create_Date) * 24) <= 24\r\n");
		if(!CommonUtils.isNullString(startDate)){
			sbSql.append("                   AND TL.CREATE_DATE >=\r\n");
			sbSql.append("                       TO_DATE('"+startDate+" 00:00:00', 'YYYY-MM-DD HH24:MI:SS')\r\n");
		}
		if(!CommonUtils.isNullString(endDate)){
			sbSql.append("                   AND TL.CREATE_DATE <=\r\n");
			sbSql.append("                       TO_DATE('"+endDate+" 23:59:59', 'YYYY-MM-DD HH24:MI:SS')\r\n");
		}
		if(!CommonUtils.isNullString(dealerId)){
			sbSql.append("                   AND B.DEALER_ID = '"+dealerId+"'\r\n");
		}
//		sbSql.append("                AND TL.CREATE_DATE >=\r\n");
//		sbSql.append("                    TO_DATE('2015-04-01 00:00:00', 'YYYY-MM-DD HH24:MI:SS')\r\n");
//		sbSql.append("                AND TL.CREATE_DATE <=\r\n");
//		sbSql.append("                    TO_DATE('2015-04-10 23:59:59', 'YYYY-MM-DD HH24:MI:SS')\r\n");
		sbSql.append("              GROUP BY B.DEALER_ID,\r\n");
		sbSql.append("                       B.ADVISER,\r\n");
		sbSql.append("                       Tl.LEADS_STATUS,\r\n");
		sbSql.append("                       tl.leads_code) B\r\n");
		sbSql.append("    ON A.leads_code = B.leads_code\r\n");
		sbSql.append("UNION ALL\r\n");
		sbSql.append("select to_char(a.DEALER_ID) DEALER_ID,\r\n");
		sbSql.append("       to_char(a.Adviser) Adviser,\r\n");
		sbSql.append("       0 clcount,\r\n");
		sbSql.append("       0 wclcount,\r\n");
		sbSql.append("       0 cecount,\r\n");
		sbSql.append("       decode(a.JC_WAY, 60021002, 0, 1) sk,\r\n");
		sbSql.append("       decode(a.JC_WAY, 60021002, 1, 0) yy,\r\n");
		sbSql.append("       0 JK,\r\n");
		sbSql.append("       0 DD,\r\n");
		sbSql.append("       0 JC\r\n");
		sbSql.append("  from (SELECT TO_NUMBER(TPLA.DEALER_ID) DEALER_ID, TPLA.Adviser, tpl.JC_WAY\r\n");
		sbSql.append("          FROM T_PC_LEADS_ALLOT TPLA, T_PC_LEADS TPL\r\n");
		sbSql.append("          LEFT JOIN T_PC_INTENT_VEHICLE TPIV\r\n");
		sbSql.append("            ON TPL.INTENT_VEHICLE = TPIV.SERIES_ID\r\n");
		sbSql.append("         WHERE TPL.LEADS_CODE = TPLA.LEADS_CODE\r\n");
		sbSql.append("           AND TPL.JC_WAY IN\r\n");
		sbSql.append("               ('60021001', '60021002', '60021003', '60021004','60021008')\r\n");
		sbSql.append("           and TPLA.if_confirm = '60321002'\r\n");
		sbSql.append("           AND tpl.LEADS_ORIGIN = '60151011'\r\n");
		if(!CommonUtils.isNullString(startDate)){
			sbSql.append("                   AND TPL.CREATE_DATE >=\r\n");
			sbSql.append("                       TO_DATE('"+startDate+" 00:00:00', 'YYYY-MM-DD HH24:MI:SS')\r\n");
		}
		if(!CommonUtils.isNullString(endDate)){
			sbSql.append("                   AND TPL.CREATE_DATE <=\r\n");
			sbSql.append("                       TO_DATE('"+endDate+" 23:59:59', 'YYYY-MM-DD HH24:MI:SS')\r\n");
		}
		if(!CommonUtils.isNullString(dealerId)){
			sbSql.append("                   AND TPLA.DEALER_ID = '"+dealerId+"'\r\n");
		}
		sbSql.append(" ) a\r\n");
//		sbSql.append("           AND TPL.CREATE_DATE >=\r\n");
//		sbSql.append("               TO_DATE('2015-04-01 00:00:00', 'YYYY-MM-DD HH24:MI:SS')\r\n");
//		sbSql.append("           AND TPL.CREATE_DATE <=\r\n");
//		sbSql.append("               TO_DATE('2015-04-10 23:59:59', 'YYYY-MM-DD HH24:MI:SS')) a\r\n");
		sbSql.append("UNION ALL\r\n");
		sbSql.append("SELECT to_char(TPLA.DEALER_ID) DEALER_ID,\r\n");
		sbSql.append("       to_char(tpla.adviser) adviser,\r\n");
		sbSql.append("       0 clcount,\r\n");
		sbSql.append("       0 wclcount,\r\n");
		sbSql.append("       0 cecount,\r\n");
		sbSql.append("       0 sk,\r\n");
		sbSql.append("       0 yy,\r\n");
		sbSql.append("       1 JK,\r\n");
		sbSql.append("       0 DD,\r\n");
		sbSql.append("       0 JC\r\n");
		sbSql.append("  FROM T_PC_LEADS_ALLOT TPLA, T_PC_LEADS TPL\r\n");
		sbSql.append("  LEFT JOIN T_PC_INTENT_VEHICLE TPIV\r\n");
		sbSql.append("    ON TPL.INTENT_VEHICLE = TPIV.SERIES_ID\r\n");
		sbSql.append(" WHERE TPL.LEADS_CODE = TPLA.LEADS_CODE\r\n");
		sbSql.append("   AND TPL.JC_WAY IN ('60021001', '60021003', '60021004', '60021008')\r\n");
		sbSql.append("   AND TPL.LEADS_STATUS = 60161001\r\n");
		sbSql.append("   and TPLA.if_confirm = '60321002'\r\n");
		sbSql.append("   AND tpl.LEADS_ORIGIN = '60151011'\r\n");
		sbSql.append("   AND TPLA.IF_CONFIRM = 60321002\r\n");
		if(!CommonUtils.isNullString(startDate)){
			sbSql.append("                   AND TPL.CREATE_DATE >=\r\n");
			sbSql.append("                       TO_DATE('"+startDate+" 00:00:00', 'YYYY-MM-DD HH24:MI:SS')\r\n");
		}
		if(!CommonUtils.isNullString(endDate)){
			sbSql.append("                   AND TPL.CREATE_DATE <=\r\n");
			sbSql.append("                       TO_DATE('"+endDate+" 23:59:59', 'YYYY-MM-DD HH24:MI:SS')\r\n");
		}
		if(!CommonUtils.isNullString(dealerId)){
			sbSql.append("                   AND TPLA.DEALER_ID = '"+dealerId+"'\r\n");
		}
//		sbSql.append("   AND TPL.CREATE_DATE >=\r\n");
//		sbSql.append("       TO_DATE('2015-04-01 00:00:00', 'YYYY-MM-DD HH24:MI:SS')\r\n");
//		sbSql.append("   AND TPL.CREATE_DATE <=\r\n");
//		sbSql.append("       TO_DATE('2015-04-10 23:59:59', 'YYYY-MM-DD HH24:MI:SS')\r\n");
		sbSql.append("UNION ALL\r\n");
		sbSql.append("SELECT to_char(TPC.DEALER_ID) DEALER_ID,\r\n");
		sbSql.append("       to_char(tpc.adviser) adviser,\r\n");
		sbSql.append("       0 clcount,\r\n");
		sbSql.append("       0 wclcount,\r\n");
		sbSql.append("       0 cecount,\r\n");
		sbSql.append("       0 sk,\r\n");
		sbSql.append("       0 yy,\r\n");
		sbSql.append("       0 jk,\r\n");
		sbSql.append("       1 DD,\r\n");
		sbSql.append("       0 JC\r\n");
		sbSql.append("  FROM T_PC_ORDER TPO, T_PC_CUSTOMER TPC\r\n");
		sbSql.append("  LEFT JOIN T_PC_INTENT_VEHICLE TPIV\r\n");
		sbSql.append("    ON TPC.INTENT_VEHICLE = TPIV.SERIES_ID\r\n");
		sbSql.append(" WHERE TPO.CUSTOMER_ID = TPC.CUSTOMER_ID  and tpo.order_status <> '60231007' AND  tpo.TASK_STATUS=60171002 \r\n");
		if(!CommonUtils.isNullString(startDate)){
			sbSql.append("                   AND TPO.order_date >=\r\n");
			sbSql.append("                       TO_DATE('"+startDate+" 00:00:00', 'YYYY-MM-DD HH24:MI:SS')\r\n");
		}
		if(!CommonUtils.isNullString(endDate)){
			sbSql.append("                   AND TPO.order_date <=\r\n");
			sbSql.append("                       TO_DATE('"+endDate+" 23:59:59', 'YYYY-MM-DD HH24:MI:SS')\r\n");
		}
		if(!CommonUtils.isNullString(dealerId)){
			sbSql.append("                   AND TPC.DEALER_ID = '"+dealerId+"'\r\n");
		}
//		sbSql.append("   AND TPC.CREATE_DATE >=\r\n");
//		sbSql.append("       TO_DATE('2015-04-01 00:00:00', 'YYYY-MM-DD HH24:MI:SS')\r\n");
//		sbSql.append("   AND TPC.CREATE_DATE <=\r\n");
//		sbSql.append("       TO_DATE('2015-04-10 23:59:59', 'YYYY-MM-DD HH24:MI:SS')\r\n");
		sbSql.append("UNION ALL\r\n");
		sbSql.append("SELECT to_char(TPC.DEALER_ID) DEALER_ID,\r\n");
		sbSql.append("       to_char(tpc.adviser) adviser,\r\n");
		sbSql.append("       0 clcount,\r\n");
		sbSql.append("       0 wclcount,\r\n");
		sbSql.append("       0 cecount,\r\n");
		sbSql.append("       0 sk,\r\n");
		sbSql.append("       0 yy,\r\n");
		sbSql.append("       0 JK,\r\n");
		sbSql.append("       0 DD,\r\n");
		sbSql.append("       1 JC\r\n");
		sbSql.append("  FROM T_PC_DELVY TPD, T_PC_CUSTOMER TPC\r\n");
		sbSql.append("  LEFT JOIN T_PC_INTENT_VEHICLE TPIV\r\n");
		sbSql.append("    ON TPC.INTENT_VEHICLE = TPIV.SERIES_ID\r\n");
		sbSql.append(" WHERE TPD.CUSTOMER_ID = TPC.CUSTOMER_ID  and tpd.status <> 10011002 \r\n");
		if(!CommonUtils.isNullString(startDate)){
			sbSql.append("                   AND tpd.DELIVERY_DATE >=\r\n");
			sbSql.append("                       TO_DATE('"+startDate+" 00:00:00', 'YYYY-MM-DD HH24:MI:SS')\r\n");
		}
		if(!CommonUtils.isNullString(endDate)){
			sbSql.append("                   AND tpd.DELIVERY_DATE <=\r\n");
			sbSql.append("                       TO_DATE('"+endDate+" 23:59:59', 'YYYY-MM-DD HH24:MI:SS')\r\n");
		}
		if(!CommonUtils.isNullString(dealerId)){
			sbSql.append("                   AND TPC.DEALER_ID = '"+dealerId+"'\r\n");
		}
//		sbSql.append("   AND TPC.CREATE_DATE >=\r\n");
//		sbSql.append("       TO_DATE('2015-04-01 00:00:00', 'YYYY-MM-DD HH24:MI:SS')\r\n");
//		sbSql.append("   AND TPC.CREATE_DATE <=\r\n");
//		sbSql.append("       TO_DATE('2015-04-10 23:59:59', 'YYYY-MM-DD HH24:MI:SS')\r\n");
		sbSql.append(" )group by DEALER_ID, Adviser) jd\r\n");
		if(!CommonUtils.isNullString(userId)){
			sbSql.append("                   WHERE JD.ADVISER IN ("+userId+") \r\n");
		}
		sbSql.append(" ) XX GROUP BY XX.DEALER_ID");
		System.out.println("```````````````````````````"+sbSql.toString());
		return factory.select(sbSql.toString(), null, new JCDynaBeanCallBack());
	}
	
	/*
	 * 获取合作经销商列表
	 */
	public List<DynaBean> getCompanyGroupList(String dealerId) {
		StringBuffer sbSql = new StringBuffer("");
		
		sbSql.append("SELECT B.DEALER_IDS, B.DEALER_CODES, VW.DEALER_SHORTNAME\r\n");
		sbSql.append("  FROM T_PC_COMPANY_GROUP B\r\n");
		sbSql.append("  LEFT JOIN VW_ORG_DEALER_ALL VW\r\n");
		sbSql.append("    ON B.DEALER_CODES = VW.DEALER_CODE\r\n");
		sbSql.append(" WHERE 1 = 1\r\n");
		sbSql.append("   AND B.PAR_DEALER_ID = '"+dealerId+"'"); 
		
		return factory.select(sbSql.toString(), null, new JCDynaBeanCallBack());
	}
}
