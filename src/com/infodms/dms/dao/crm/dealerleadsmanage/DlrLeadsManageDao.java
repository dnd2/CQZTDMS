package com.infodms.dms.dao.crm.dealerleadsmanage;

import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.common.Constant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.dao.common.JCDynaBeanCallBack;
import com.infodms.dms.po.TPcLeadsAllotPO;
import com.infodms.dms.po.TPcLeadsDealerPO;
import com.infodms.dms.po.TPcLeadsPO;
import com.infodms.dms.po.TcUserPO;
import com.infodms.dms.po.TmDealerPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.po3.bean.DynaBean;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class DlrLeadsManageDao extends BaseDao {
	public static Logger logger = Logger.getLogger(DlrLeadsManageDao.class);
	private static final DlrLeadsManageDao dao = new DlrLeadsManageDao();

	public static final DlrLeadsManageDao getInstance() {
		return dao;
	}

	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		return null;
	}
	
	/**
	 * 是否已存在客户建档数据
	 */
	public List<Map<String, Object>> getHasCustomer(String telephone,String name,String dealerId) {
		
		List<Object> params = new LinkedList<Object>();
		StringBuffer sql = new StringBuffer("");
		sql.append("select A.DEALER_ID,A.ADVISER,A.CUSTOMER_ID,A.CTM_TYPE\r\n");
		sql.append("  from t_pc_customer A,TM_DEALER B \r\n");
		sql.append(" where A.DEALER_ID = to_char(B.DEALER_ID) AND a.TELEPHONE = '"+telephone+"'");
//		sql.append("   and a.CUSTOMER_NAME = '"+name+"'");
		if(dealerId == null) {
			sql.append("   and B.DEALER_ID = ''"); 
		} else {
			sql.append("   and B.DEALER_ID = '"+dealerId+"'"); 
		}

		return dao.pageQuery(sql.toString(), params, getFunName());
	}
	
	/**
	 * DLR销售线索分派查询
	 */
	
	public PageResult<Map<String, Object>> dlrLeadsAllotQuery(
			String customerName,
			String telephone,String startDate,String endDate,
			String leadsOrigin,String allotStatus, String userDealerId,
			int curPage, int pageSize) {
		
		StringBuffer sql = new StringBuffer();
		
		sql.append("select a.leads_code,a2.leads_allot_id,\r\n");
		sql.append("       c.code_desc as leads_origin,\r\n");
		sql.append("       to_char(a.create_date, 'yyyy-MM-dd') create_date,\r\n");
		sql.append("       b1.region_name as province,\r\n");
		sql.append("       b2.region_name as city,\r\n");
		sql.append("       b3.region_name as area,\r\n");
		sql.append("       a.customer_name,\r\n");
		sql.append("       a.telephone,\r\n");
		sql.append("       null AS DEALER_NAME,\r\n");
		sql.append("       e.name AS ADVISER,\r\n");
//		sql.append("       DECODE(D.DEALER_NAME,NULL,'',D.DEALER_NAME) AS DEALER_NAME,\r\n");
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
		sql.append(" where 1 = 1 and a2.dealer_id is not null and a2.adviser is null and a.leads_status = "+Constant.LEADS_STATUS_01+" and (a.leads_type="+Constant.LEADS_TYPE_01+"\n "); 
		sql.append(" or a.leads_type="+Constant.LEADS_TYPE_04+" ) \n");
		if (Utility.testString(customerName)) {//客户姓名
			sql.append(" and a.customer_name like '%"+customerName+"%' ");
		}
		if (Utility.testString(telephone)) {//联系电话
			sql.append(" and a.telephone like '%"+telephone+"%' ");
		}
		if (Utility.testString(startDate)) {//导入开始时间
			sql.append(" and a.create_date >= to_date('"+startDate+" 00:00:00','yyyy-MM-dd HH24:mi:ss') ");
		}
		if (Utility.testString(endDate)) {//导入结束时间
			sql.append(" and a.create_date <= to_date('"+endDate+" 23:59:59','yyyy-MM-dd HH24:mi:ss') ");
		}
		if(Utility.testString(leadsOrigin)){//线索来源
			sql.append(" and a.leads_origin = '"+leadsOrigin+"' \n");
		}
		if(Utility.testString(userDealerId)){
			sql.append(" and a2.dealer_id = '"+userDealerId+"' \n");
		}
//		if(Utility.testString(dealerCode)){
//			if (null != dealerCode && !"".equals(dealerCode)) {
//				String[] array = dealerCode.split(",");
//				sql.append("   AND EXISTS (SELECT J.DEALER_ID FROM TM_DEALER J WHERE J.DEALER_CODE=A.DEALER_ID AND J.DEALER_CODE IN (\n");
//				for (int i = 0; i < array.length; i++) {
//					sql.append("'" + array[i] + "'");
//					if (i != array.length - 1) {
//						sql.append(",");
//					}
//				}
//				sql.append("))\n");
//			}
//		}
		
		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
		return ps;
	}
	
	/**
	 * DLR销售线索查询
	 */
	
	public PageResult<Map<String, Object>> dlrLeadsFindQuery(
			String customerName,
			String telephone,String startDate,String endDate,String allotStartDate,String allotEndDate,
			String leadsOrigin,String allotStatus,String timeOut, String userDealerId, String leadsStatus, String adviser, String adviserId, String groupId,String jcway,String seriesId,
			int curPage, int pageSize) {
		
		StringBuffer sbSql = new StringBuffer();
		
		sbSql.append("select * from (select * from (\r\n");
		sbSql.append("select A.CUSTOMER_DESCRIBE,c3.code_desc as jc_way,c3.code_id as jc_way_code,e.group_id,a.leads_code,f.UP_SERIES_ID,f.series_name,a2.dealer_id,a2.LEADS_ALLOT_ID,a.leads_type,e.name as adviser,a2.adviser as adviserId,\r\n");
		sbSql.append("       c.code_desc as leads_origin,a.leads_origin as leads_origin_code,\r\n");
		sbSql.append("       c2.code_desc as leads_status,a.leads_status as leads_status_code,\r\n");
		sbSql.append("       to_char(a.collect_date, 'yyyy-MM-dd') collect_date,\r\n");
		sbSql.append("       to_char(a.create_date, 'yyyy-MM-dd HH24:mi:ss') create_date2,\r\n");
		sbSql.append("       a.customer_name,decode(a2.dealer_id,null,60281001,60281002) as allot_status,\r\n");
		sbSql.append("       a.telephone,\r\n");
		sbSql.append("       d.dealer_shortname AS DEALER_NAME,\r\n");
		sbSql.append("       to_char(a2.allot_dealer_date, 'yyyy-MM-dd HH24:mi:ss') as allot_dealer_date,\r\n");
		sbSql.append("       to_char(a2.allot_adviser_date, 'yyyy-MM-dd HH24:mi:ss') as allot_adviser_date,\r\n");
		sbSql.append("       a2.allot_adviser_date as allot_adviser_date2,\r\n");
		sbSql.append("       to_char(a2.confirm_date, 'yyyy-MM-dd HH24:mi:ss') as confirm_date,\r\n");
		sbSql.append("       a.remark,a.create_date,a2.if_confirm,a2.status,\r\n");
		//9：00-17:00之间分配给经销商的公司类信息，主管当日19:00前完成分配，顾问必须在当日完成处理。
		//17:00-次日9:00分配给经销商的公司类信息，主管次日11:00前完成分配，顾问必须在次日12:00前完成处理。
		sbSql.append("       decode(a2.allot_dealer_date,null,'10041002',case when to_char(a2.allot_dealer_date, 'HH24') >= 9 and to_char(a2.allot_dealer_date, 'HH24')<17\r\n");
		sbSql.append("         then (case when sysdate > to_date(to_char(a2.allot_dealer_date, 'yyyy-MM-dd') || ' 23:59:59','yyyy-MM-dd HH24:mi:ss') and a2.adviser is null then 10041001 else 10041002 end)\r\n");
		sbSql.append("          else (case when sysdate > to_date(to_char(a2.allot_dealer_date+1, 'yyyy-MM-dd') || ' 12:00:00','yyyy-MM-dd HH24:mi:ss') and a2.adviser is null then 10041001 else 10041002 end) end) as timeout\r\n");
		sbSql.append("  from t_pc_leads a\r\n");
		sbSql.append("  left join t_pc_leads_allot a2\r\n");
		sbSql.append("    on a.leads_code = a2.leads_code\r\n");
		sbSql.append("  LEFT JOIN TC_CODE C\r\n");
		sbSql.append("    ON A.LEADS_ORIGIN = C.CODE_ID\r\n");
		sbSql.append("  LEFT JOIN TC_CODE c2\r\n");
		sbSql.append("    ON A.leads_status = C2.CODE_ID\r\n");
		sbSql.append("  LEFT JOIN TC_CODE c3\r\n");
		sbSql.append("    ON A.jc_way = C3.CODE_ID\r\n");
		sbSql.append("  left join tm_dealer d\r\n");
		sbSql.append("    on a2.dealer_id = d.dealer_id\r\n");
		sbSql.append("  left join tc_user e on a2.adviser = e.user_id\r\n");
		sbSql.append("  left join t_pc_intent_vehicle f on a.intent_vehicle = f.series_id\r\n");
		sbSql.append(") aa where 1=1 and aa.dealer_id is not null  "); 

		if (Utility.testString(customerName)) {//客户姓名
			sbSql.append(" and aa.customer_name like '%"+customerName+"%' ");
		}
		if (Utility.testString(telephone)) {//联系电话
			sbSql.append(" and aa.telephone like '%"+telephone+"%' ");
		}
		if (Utility.testString(startDate)) {//导入开始时间
			sbSql.append(" and aa.create_date >= to_date('"+startDate+" 00:00:00','yyyy-MM-dd HH24:mi:ss') ");
		}
		if (Utility.testString(endDate)) {//导入结束时间
			sbSql.append(" and aa.create_date <= to_date('"+endDate+" 23:59:59','yyyy-MM-dd HH24:mi:ss') ");
		}
		if (Utility.testString(allotStartDate)) {//分派开始时间
			sbSql.append(" and aa.allot_adviser_date2 >= to_date('"+allotStartDate+" 00:00:00','yyyy-MM-dd HH24:mi:ss') ");
		}
		if (Utility.testString(allotEndDate)) {//分派结束时间
			sbSql.append(" and aa.allot_adviser_date2 <= to_date('"+allotEndDate+" 23:59:59','yyyy-MM-dd HH24:mi:ss') ");
		}
		if(Utility.testString(leadsOrigin)){//线索来源
			sbSql.append(" and aa.leads_origin_code = '"+leadsOrigin+"' \n");
		}
		if(Utility.testString(leadsStatus)){//线索状态
			sbSql.append(" and aa.leads_status_code = '"+leadsStatus+"' \n");
		}
		if (Utility.testString(jcway)) {//集客方式
			sbSql.append(" and aa.jc_way_code = '"+jcway+"' ");
		}
		 if(Utility.testString(seriesId)){
			sbSql.append("     		  AND aa.UP_SERIES_ID in ("+seriesId+")   \n" );
		    }
		if(Utility.testString(adviser)){//顾问
			sbSql.append(" and aa.adviserId = '"+adviser+"' \n");
		}
		if(Utility.testString(adviserId)){//顾问
			if("".equals(adviserId)){
				sbSql.append(" and aa.adviserId in (0) \n");
			}else{
				sbSql.append(" and aa.adviserId in ("+adviserId+") \n");
			}
		}
		if(Utility.testString(groupId)){//分组
			sbSql.append(" and aa.group_id = '"+groupId+"' \n");
		}
		if(allotStatus==null||"".equals(allotStatus)){
			
		} else if(allotStatus.equals(Constant.ALLOT_STATUS_01.toString())) {
			sbSql.append(" and aa.adviserId is null \n");
		} else if(allotStatus.equals(Constant.ALLOT_STATUS_02.toString())){
			sbSql.append(" and aa.adviserId is not null \n");
		}
		if(Utility.testString(timeOut)){
			sbSql.append(" and aa.timeout = '"+timeOut+"' \n");
		}
		if(Utility.testString(userDealerId)){
			sbSql.append(" and aa.dealer_id = '"+userDealerId+"' \n");
		}
		sbSql.append(" order by aa.if_confirm,aa.leads_status_code,aa.create_date desc) g order by rownum \n");
		
		PageResult<Map<String, Object>> ps = pageQuery(sbSql.toString(), null, getFunName(), pageSize, curPage);
		return ps;
	}
	
	/*
	 * 根据经销商获取对应的顾问列表
	 */
	public List<DynaBean> getAdviserBydealer(String dealerId) {
		StringBuffer sql = new StringBuffer("");
		
		sql.append("select a.user_id, '('||d.GROUP_NAME||')'||a.name as name\r\n");
		sql.append("  from tm_company b, tm_dealer c,tc_user a left join t_pc_group d on a.group_id = d.group_id\r\n");
		sql.append(" where a.company_id = b.company_id\r\n");
		sql.append("   and b.company_id = c.company_id\r\n");
		sql.append("   and a.pose_rank = '60281004'\r\n");
		if(Utility.testString(dealerId)){
			sql.append("   and c.dealer_id = '"+dealerId+"'"); 
		}
		;
		return factory.select(sql.toString(), null, new JCDynaBeanCallBack());
//		return select(sql.toString(), null, null);
	}
	
	/*
	 * 根据经销商获取对应的顾问列表2
	 */
	public List<DynaBean> getAdviserBydealer2(String dealerId,String userId) {
		StringBuffer sql = new StringBuffer("");
		
		sql.append("select a.user_id,");
		//sql.append(" '('||d.GROUP_NAME||')'|| ");
		sql.append(" a.name as name \r\n");
		sql.append("  from tm_company b, tm_dealer c,tc_user a ");
		//sql.append("  left join t_pc_group d on a.group_id = d.group_id \r\n");
		sql.append(" where a.company_id = b.company_id\r\n");
		sql.append("   and b.company_id = c.company_id\r\n");
		sql.append("   and a.pose_rank = '60281004'\r\n");
		sql.append("   AND a.user_status = '10011001'\r\n");
		
		if(Utility.testString(dealerId)){
			sql.append("   and c.dealer_id = '"+dealerId+"'"); 
		}
		if(Utility.testString(userId)){
			sql.append("   and a.user_id in ("+userId+")"); 
		}
		return factory.select(sql.toString(), null, new JCDynaBeanCallBack());
//		return select(sql.toString(), null, null);
	}
	
	/*
	 * 根据经销商获取对应的顾问列表（带保有客户数量）
	 */
	public List<DynaBean> getAdviserBydealer3(String dealerId) {
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
		sql.append("   and a.is_lock='0'\r\n");
		sql.append("   and a.user_status='10011001'\r\n");
		if(Utility.testString(dealerId)){
			sql.append("   and c.dealer_id = '"+dealerId+"'"); 
		}
		sql.append("   ) t1 left join (select * from t_pc_customer cu where cu.ctm_type in (60341002, 60341003) and cu.ctm_rank in (60101001, 60101002, 60101003, 60101004)) t2 on t1.user_id = t2.adviser\r\n");
		sql.append("   ) q group by q.user_id"); 

		return factory.select(sql.toString(), null, new JCDynaBeanCallBack());
//		return select(sql.toString(), null, null);
	}
	
	/*
	 * 根据经销商获取分组列表
	 */
	public List<DynaBean> getGroupBydealer(String dealerId) {
		StringBuffer sql = new StringBuffer("");
		
		sql.append("select * from t_pc_group a where 1=1  "); 
		sql.append("  and a.STATUS="+Constant.STATUS_ENABLE );
		if(Utility.testString(dealerId)){
			sql.append("   and a.dealer_id = '"+dealerId+"'"); 
		}
		return factory.select(sql.toString(), null, new JCDynaBeanCallBack());
//		return select(sql.toString(), null, null);
	}
	
	/**
	 * 顾问录入查询
	 */
	
	public PageResult<Map<String, Object>> adviserEnterFindQuery(
			String customerName,
			String telephone,String startDate,String endDate,
			String customerStatus,String allotStatus, String userDealerId,String adviser,
			int curPage, int pageSize) {
		
		StringBuffer sbSql = new StringBuffer();
		
		sbSql.append("select * from (\r\n");
		sbSql.append("select a.leads_code,f.series_name,a2.dealer_id,a2.LEADS_ALLOT_ID,a.leads_type,e.name as adviser,a2.adviser as adviserId,\r\n");
		sbSql.append("       c.code_desc as leads_origin,a.leads_origin as leads_origin_code,\r\n");
		sbSql.append("       c2.code_desc as leads_status,a.leads_status as leads_status_code,\r\n");
		sbSql.append("       to_char(a.collect_date, 'yyyy-MM-dd') collect_date,\r\n");
		sbSql.append("       a.customer_name,decode(a2.dealer_id,null,60281001,60281002) as allot_status,\r\n");
		sbSql.append("       a.telephone,to_char(a.come_date,'yyyy-MM-dd HH24:mi:ss') as come_date,to_char(a.leave_date,'yyyy-MM-dd HH24:mi:ss') as leave_date,a.customer_describe,\r\n");
		sbSql.append("       d.dealer_shortname AS DEALER_NAME,\r\n");
		sbSql.append("       to_char(a2.allot_dealer_date, 'yyyy-MM-dd HH24:mi:ss') as allot_dealer_date,\r\n");
		sbSql.append("       a.remark,a.create_date,\r\n");
		//9：00-17:00之间分配给经销商的公司类信息，主管当日19:00前完成分配，顾问必须在当日完成处理。
		//17:00-次日9:00分配给经销商的公司类信息，主管次日11:00前完成分配，顾问必须在次日12:00前完成处理。
		sbSql.append("       decode(a2.allot_dealer_date,null,'10041002',case when to_char(a2.allot_dealer_date, 'HH24') >= 9 and to_char(a2.allot_dealer_date, 'HH24')<17\r\n");
		sbSql.append("         then (case when sysdate > to_date(to_char(a2.allot_dealer_date, 'yyyy-MM-dd') || ' 23:59:59','yyyy-MM-dd HH24:mi:ss') and a2.adviser is null then 10041001 else 10041002 end)\r\n");
		sbSql.append("          else (case when sysdate > to_date(to_char(a2.allot_dealer_date+1, 'yyyy-MM-dd') || ' 12:00:00','yyyy-MM-dd HH24:mi:ss') and a2.adviser is null then 10041001 else 10041002 end) end) as timeout\r\n");
		sbSql.append("  from t_pc_leads a\r\n");
		sbSql.append("  left join t_pc_leads_allot a2\r\n");
		sbSql.append("    on a.leads_code = a2.leads_code\r\n");
		sbSql.append("  LEFT JOIN TC_CODE C\r\n");
		sbSql.append("    ON A.LEADS_ORIGIN = C.CODE_ID\r\n");
		sbSql.append("  LEFT JOIN TC_CODE c2\r\n");
		sbSql.append("    ON A.leads_status = C2.CODE_ID\r\n");
		sbSql.append("  left join tm_dealer d\r\n");
		sbSql.append("    on a2.dealer_id = d.dealer_id\r\n");
		sbSql.append("  left join tc_user e on a2.adviser = e.user_id\r\n");
		sbSql.append("  left join t_pc_intent_vehicle f on a.intent_vehicle = f.series_id\r\n");
		sbSql.append(") aa where 1=1 and aa.adviserId = '"+adviser+"' and aa.dealer_id is not null and aa.leads_type="+Constant.LEADS_TYPE_03+" "); 

		if (Utility.testString(customerName)) {//客户姓名
			sbSql.append(" and aa.customer_name like '%"+customerName+"%' ");
		}
		if (Utility.testString(telephone)) {//联系电话
			sbSql.append(" and aa.telephone = '"+telephone+"' ");
		}
		if (Utility.testString(startDate)) {//导入开始时间
			sbSql.append(" and aa.create_date >= to_date('"+startDate+"','yyyy-MM-dd') ");
		}
		if (Utility.testString(endDate)) {//导入结束时间
			sbSql.append(" and aa.create_date <= to_date('"+endDate+"','yyyy-MM-dd') ");
		}
		if(allotStatus==null||"".equals(allotStatus)){
			
		} else if(allotStatus.equals(Constant.ALLOT_STATUS_01.toString())) {
			sbSql.append(" and aa.adviserId is null \n");
		} else if(allotStatus.equals(Constant.ALLOT_STATUS_02.toString())){
			sbSql.append(" and aa.adviserId is not null \n");
		}
		if(Utility.testString(userDealerId)){
			sbSql.append(" and aa.dealer_id = '"+userDealerId+"' \n");
		}
		if(allotStatus==null||"".equals(allotStatus)){
			
		} else if(allotStatus.equals(Constant.ALLOT_STATUS_01.toString())) {
			sbSql.append(" and aa.adviserId is null \n");
		} else if(allotStatus.equals(Constant.ALLOT_STATUS_02.toString())){
			sbSql.append(" and aa.adviserId is not null \n");
		}
		if(Utility.testString(customerStatus)){
			sbSql.append(" and aa.leads_status_code = '"+customerStatus+"' \n");
		}
		sbSql.append(" order by aa.leads_status_code desc,aa.create_date desc \n");
//		if(Utility.testString(userDealerId)){
//			if (null != userDealerId && !"".equals(userDealerId)) {
//				String[] array = userDealerId.split(",");
//				sbSql.append("   AND EXISTS (SELECT J.DEALER_ID FROM TM_DEALER J WHERE J.DEALER_CODE=AA.DEALER_ID AND J.DEALER_CODE IN (\n");
//				for (int i = 0; i < array.length; i++) {
//					sbSql.append("'" + array[i] + "'");
//					if (i != array.length - 1) {
//						sbSql.append(",");
//					}
//				}
//				sbSql.append("))\n");
//			}
//		}
		
		PageResult<Map<String, Object>> ps = pageQuery(sbSql.toString(), null, getFunName(), pageSize, curPage);
		return ps;
	}
	/**
	 * DCRC录入查询
	 */
	
	public PageResult<Map<String, Object>> dcrcEnterFindQuery(
			String customerName,
			String telephone,String startDate,String endDate,
			String customerStatus,String allotStatus, String userDealerId, String adviser, String groupId,String jcway,
			int curPage, int pageSize) {
		
		StringBuffer sbSql = new StringBuffer();
		
		sbSql.append("select * from (\r\n");
		sbSql.append("select a.leads_code,c3.code_desc as jc_way,c3.code_id as jc_way_code,e.group_id,f.series_name,a2.dealer_id,a2.LEADS_ALLOT_ID,a.leads_type,e.name as adviser,a2.adviser as adviserId,\r\n");
		sbSql.append("       decode(c.code_desc,'自然来店','来店','来电线索','来电',c.code_desc) as leads_origin,a.leads_origin as leads_origin_code,\r\n");
		sbSql.append("       c2.code_desc as leads_status,a.leads_status as leads_status_code,\r\n");
		sbSql.append("       a2.if_confirm,\r\n");
		sbSql.append("       to_char(a.collect_date, 'yyyy-MM-dd') collect_date,a.come_date as come_date2,\r\n");
		sbSql.append("       a.customer_name,decode(a2.dealer_id,null,60281001,60281002) as allot_status,\r\n");
		sbSql.append("       a.telephone,to_char(a.come_date,'yyyy-MM-dd HH24:mi:ss') as come_date,to_char(a.leave_date,'yyyy-MM-dd HH24:mi:ss') as leave_date,a.customer_describe,\r\n");
		sbSql.append("       d.dealer_shortname AS DEALER_NAME,\r\n");
		sbSql.append("       to_char(a2.allot_dealer_date, 'yyyy-MM-dd HH24:mi:ss') as allot_dealer_date,\r\n");
		sbSql.append("       a.remark,a.create_date,\r\n");
		//9：00-17:00之间分配给经销商的公司类信息，主管当日19:00前完成分配，顾问必须在当日完成处理。
		//17:00-次日9:00分配给经销商的公司类信息，主管次日11:00前完成分配，顾问必须在次日12:00前完成处理。
		sbSql.append("       decode(a2.allot_dealer_date,null,'10041002',case when to_char(a2.allot_dealer_date, 'HH24') >= 9 and to_char(a2.allot_dealer_date, 'HH24')<17\r\n");
		sbSql.append("         then (case when sysdate > to_date(to_char(a2.allot_dealer_date, 'yyyy-MM-dd') || ' 23:59:59','yyyy-MM-dd HH24:mi:ss') and a2.adviser is null then 10041001 else 10041002 end)\r\n");
		sbSql.append("          else (case when sysdate > to_date(to_char(a2.allot_dealer_date+1, 'yyyy-MM-dd') || ' 12:00:00','yyyy-MM-dd HH24:mi:ss') and a2.adviser is null then 10041001 else 10041002 end) end) as timeout\r\n");
		sbSql.append("  from t_pc_leads a\r\n");
		sbSql.append("  left join t_pc_leads_allot a2\r\n");
		sbSql.append("    on a.leads_code = a2.leads_code\r\n");
		sbSql.append("  LEFT JOIN TC_CODE C\r\n");
		sbSql.append("    ON A.LEADS_ORIGIN = C.CODE_ID\r\n");
		sbSql.append("  LEFT JOIN TC_CODE c2\r\n");
		sbSql.append("    ON A.leads_status = C2.CODE_ID\r\n");
		sbSql.append("  LEFT JOIN TC_CODE c3\r\n");
		sbSql.append("    ON jc_way = C3.CODE_ID\r\n");
		sbSql.append("  left join tm_dealer d\r\n");
		sbSql.append("    on a2.dealer_id = d.dealer_id\r\n");
		sbSql.append("  left join tc_user e on a2.adviser = e.user_id\r\n");
		sbSql.append("  left join t_pc_intent_vehicle f on a.intent_vehicle = f.series_id\r\n");
		sbSql.append(") aa where 1=1 and aa.dealer_id is not null and aa.leads_type="+Constant.LEADS_TYPE_02+" "); 

		if (Utility.testString(customerName)) {//客户姓名
			sbSql.append(" and aa.customer_name like '%"+customerName+"%' ");
		}
		if (Utility.testString(telephone)) {//联系电话
			sbSql.append(" and aa.telephone like '%"+telephone+"%' ");
		}
		if (Utility.testString(startDate)) {//来店开始时间
			sbSql.append(" and aa.come_date2 >= to_date('"+startDate+" 00:00:00','yyyy-MM-dd HH24:mi:ss') ");
		}
		if (Utility.testString(endDate)) {//来店结束时间
			sbSql.append(" and aa.come_date2 <= to_date('"+endDate+" 23:59:59','yyyy-MM-dd HH24:mi:ss') ");
		}
		if (Utility.testString(adviser)) {//分派顾问
			sbSql.append(" and aa.adviserId = '"+adviser+"' ");
		}
		if (Utility.testString(groupId)) {//分组
			sbSql.append(" and aa.group_id = '"+groupId+"' ");
		}
		if (Utility.testString(jcway)) {//集客方式
			sbSql.append(" and aa.jc_way_code = '"+jcway+"' ");
		}

		if(allotStatus==null||"".equals(allotStatus)){
			
		} else if(allotStatus.equals(Constant.ALLOT_STATUS_01.toString())) {
			sbSql.append(" and aa.adviserId is null \n");
		} else if(allotStatus.equals(Constant.ALLOT_STATUS_02.toString())){
			sbSql.append(" and aa.adviserId is not null \n");
		}
		if(Utility.testString(userDealerId)){
			sbSql.append(" and aa.dealer_id = '"+userDealerId+"' \n");
		}
		if(allotStatus==null||"".equals(allotStatus)){
			
		} else if(allotStatus.equals(Constant.ALLOT_STATUS_01.toString())) {
			sbSql.append(" and aa.adviserId is null \n");
		} else if(allotStatus.equals(Constant.ALLOT_STATUS_02.toString())){
			sbSql.append(" and aa.adviserId is not null \n");
		}
		if(Utility.testString(customerStatus)){
			sbSql.append(" and aa.leads_status_code = '"+customerStatus+"' \n");
		}
		sbSql.append(" order by  aa.create_date desc ,aa.leads_status_code desc \n");
//		if(Utility.testString(userDealerId)){
//			if (null != userDealerId && !"".equals(userDealerId)) {
//				String[] array = userDealerId.split(",");
//				sbSql.append("   AND EXISTS (SELECT J.DEALER_ID FROM TM_DEALER J WHERE J.DEALER_CODE=AA.DEALER_ID AND J.DEALER_CODE IN (\n");
//				for (int i = 0; i < array.length; i++) {
//					sbSql.append("'" + array[i] + "'");
//					if (i != array.length - 1) {
//						sbSql.append(",");
//					}
//				}
//				sbSql.append("))\n");
//			}
//		}
		
		PageResult<Map<String, Object>> ps = pageQuery(sbSql.toString(), null, getFunName(), pageSize, curPage);
		return ps;
	}
	
	/*
	 * 根据线索获取基本信息
	 */
	public List<DynaBean> getInfoByleadsCode(String leadsCode, String leadsAllotId) {
		StringBuffer sql = new StringBuffer("");
				
		sql.append("select to_char(a.come_date,'yyyy-mm-dd hh24:mi:ss') come_date,a.leads_code,b.leads_allot_id,a.leads_origin,a.telephone,a.intent_vehicle,a.old_customer_name,a.old_telephone,a.old_vehicle_id,b.adviser,c2.code_desc as adviser2,a.customer_describe,a.come_meet,a.jc_way,c.code_desc as jc_way2, " +
				"a.customer_name, " +
				"a.buy_budget, c3.code_desc as buy_budget2," +
				"a.test_driving, c4.code_desc as test_driving2," +
				"a.buy_type,c5.code_desc as buy_type2, " +
				"a.customer_type,c6.code_desc as customer_type2 " +
				/*"a.intent_type,c7.code_desc as intent_type2 " +*/
				" from t_pc_leads a join t_pc_leads_allot b on a.leads_code = b.leads_code " +
				"left join tc_code c on a.jc_way = c.code_id " +
				"left join tc_code c2 on b.adviser = c2.code_id " +
				"left join tc_code c3 on a.buy_budget = c3.code_id "+
				"left join tc_code c4 on a.test_driving = c4.code_id "+
				"left join tc_code c5 on a.buy_type = c5.code_id "+
				"left join tc_code c6 on a.customer_type = c6.code_id "+
				"left join tc_code c7 on a.intent_type = c7.code_id "+
				"where a.leads_code = '"+leadsCode+"' and b.leads_allot_id = '"+leadsAllotId+"'");
		
		return factory.select(sql.toString(), null, new JCDynaBeanCallBack());
//		return select(sql.toString(), null, null);
	}
	
	/*
	 * dlr导入时，查询临时表数据（长安汽车） 结果集
	 */
	public List<Map<String, Object>> dlrSelectTmpLeadsManage(Long userId) {
		List<Object> params = new LinkedList<Object>();
		StringBuffer sql = new StringBuffer("");
		sql.append("select tpd.CUSTOMER_NAME,\n" );
		sql.append("       tpd.TELEPHONE,\n" );
		sql.append("       tpd.PROVINCE,\n" );
		sql.append("      tpd.CITY,\n" );
		sql.append("       tpd.AREA,\n" );
		sql.append("       tpd.LEADS_ORIGIN,\n" );
		sql.append("       tpd.COLLECT_DATE,\n" );
		sql.append("       tpd.DEALER_ID,\n" );
		sql.append("       td.dealer_code,    TPD.customer_describe\n" );
		sql.append("  from t_pc_leads_dealer tpd, tm_dealer td\n" );
		sql.append(" where td.dealer_id = tpd.dealer_id");
		sql.append("  and leads_type = 60141004 and tpd.create_by="+userId+"");

		return dao.pageQuery(sql.toString(), params, getFunName());
	}
	
	/*
	 * 插入主表数据
	 */
	public int insertLeads(Long userId) throws ParseException {
		
		TPcLeadsDealerPO po2 = new TPcLeadsDealerPO();
		po2.setCreateBy(userId.toString());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		List<TPcLeadsDealerPO> list = dao.select(po2);
		for(int i=0;i<list.size();i++) {
			po2 = list.get(i);
			TPcLeadsPO po = new TPcLeadsPO();
			//销售线索主键ID
			Long leadsCode = dao.getLongPK(po);
			
			//线索来源转换代码
			String origin = po2.getLeadsOrigin();
			if(origin=="客户中心"||"客户中心".equals(origin)) {
				origin = "60151003";
			} else if(origin=="官网"||"官网".equals(origin)) {
				origin = "60151004";
			} else if(origin=="网络媒体"||"网络媒体".equals(origin)) {
				origin = "60151006";
			} else if(origin=="车展/巡展/路演"||"车展/巡展/路演".equals(origin)) {
				origin = "60151007";
			} else if(origin=="品牌体验活动（上市活动、试乘试驾、大篷车等）"||"品牌体验活动（上市活动、试乘试驾、大篷车等）".equals(origin)) {
				origin = "60151008";
			} else if(origin=="亲朋/老客户介绍及其他"||"亲朋/老客户介绍及其他".equals(origin)) {
				origin = "60151016";
			} else if(origin=="商圈定展"||"商圈定展".equals(origin)) {
				origin = "60151019";
			} else if(origin=="新媒体（移动端APP、移动网站等）"||"新媒体（移动端APP、移动网站等）".equals(origin)) {
				origin = "60151020";
			} else if(origin=="汽车之家"||"汽车之家".equals(origin)) {
				origin = "60151017";
			} else if(origin=="易车网"||"易车网".equals(origin)) {
				origin = "60151018";
			} else {
				origin = "60151021";
			}
			Long originL = Long.parseLong(origin);
			// 插入数据SQL（销售线索表）
			StringBuffer sql = new StringBuffer();
			sql.append("insert into t_pc_leads a\r\n");
			sql.append("(leads_code,leads_type,LEADS_ORIGIN,\r\n");
			sql.append("customer_name,PROVINCE,city,area,\r\n");
			sql.append("collect_date,telephone,create_date,create_by\r\n");
			sql.append(",leads_status,customer_describe)\r\n");
			
			sql.append("values \r\n");
			sql.append("  ("+leadsCode+",\r\n");//主键
			sql.append("   "+Constant.LEADS_TYPE_04+",\r\n");//线索类别
			sql.append("   "+originL+",\r\n");//线索来源
			sql.append("   '"+po2.getCustomerName()+"',\r\n");
//			sql.append("   null,\r\n");//到店时间
//			sql.append("   null,\r\n");//离店时间
//			sql.append("   null,\r\n");//客户描述
			if(po2.getProvince()==null) {
				sql.append("   null,\r\n");
			} else {
				sql.append("   '"+po2.getProvince()+"',\r\n");
			}
			if(po2.getCity()==null) {
				sql.append("   '',\r\n");
			} else {
				sql.append("   '"+po2.getCity()+"',\r\n");
			}
			if(po2.getArea()==null) {
				sql.append("   null,\r\n");
			} else {
				sql.append("   '"+po2.getArea()+"',\r\n");
			}
			//收集时间
			if(po2.getCollectDate()==null) {
				sql.append("   null,\r\n");//收集时间
			} else {
				sql.append("   to_date('"+po2.getCollectDate()+"','yyyy-MM-dd'),\r\n");//收集时间
			}
			sql.append("   '"+po2.getTelephone()+"',\r\n");
			//sql.append("   null,\r\n");//备注
			sql.append("   sysdate,\r\n");
			sql.append("   '"+userId+"',\r\n");
			sql.append("   "+Constant.LEADS_STATUS_01+" ,'"+po2.getCustomerDescribe()+"')\r\n");//线索状态
//			sql.append("   null,\r\n");//战败备注
//			sql.append("   null,\r\n"); //失效备注
//			sql.append("   null,\r\n"); //意向车型
//			sql.append("   null,\r\n"); //客户ID
//			sql.append("   null,\r\n"); //战败车型
//			sql.append("   null,\r\n"); //战败原因
//			sql.append("   null,\r\n"); //集客方式
//			sql.append("   null,\r\n"); //趋前迎接
//			sql.append("   null)\r\n"); //客户性别
			
			update(sql.toString(), null);
			
			// 自动分派开始
			//判断开始：是否自动分配到顾问（根据已建档客户信息进行判断*电话+姓名+经销商ID*）//现已改为电话+经销商验证
			//曾经建过档案
			//是否在建档客户信息中存在数据(必须在同一个经销商才能自动分派到顾问)
			if(po2.getDealerId() == null) { 
				//若经销为空,不做处理,不分派
			} else {
				List<Map<String, Object>> getHasList = null;
				getHasList=getHasCustomerImp(po2.getTelephone(),po2.getCustomerName(),po2.getDealerId());
				
				if(getHasList.size()>0) {
					//已存在，则自动分派顾问，如果客户类型不属于战败、失效、保有将线索设为无效，并增加一个接触点信息
					//获取已建档的分派顾问
					String adviser = getHasList.get(0).get("ADVISER").toString();
					String dealerId = getHasList.get(0).get("DEALER_ID").toString();
					String customerId = getHasList.get(0).get("CUSTOMER_ID").toString();
					String ctmType = getHasList.get(0).get("CTM_TYPE").toString();
					//修改销售线索主表状态为无效
					TPcLeadsPO oldLeadsPo = new TPcLeadsPO();
					oldLeadsPo.setLeadsCode(leadsCode);
					TPcLeadsPO newLeadsPo = new TPcLeadsPO();
					newLeadsPo.setLeadsCode(leadsCode);
					if(!"60341001".equals(ctmType)&&!"60341004".equals(ctmType)&&!"60341005".equals(ctmType)) {
						newLeadsPo.setLeadsStatus(Constant.LEADS_STATUS_04.longValue());
					} else {
						newLeadsPo.setLeadsStatus(Constant.LEADS_STATUS_01.longValue());
					}
					dao.update(oldLeadsPo, newLeadsPo);
					
					TPcLeadsAllotPO allotPo = new TPcLeadsAllotPO();
					//线索分派主键ID
					Long allotCode = dao.getLongPK(allotPo);
					allotPo.setLeadsAllotId(allotCode);
					allotPo.setLeadsCode(leadsCode);
					allotPo.setCustomerName(po2.getCustomerName());
					allotPo.setTelephone(po2.getTelephone());
					allotPo.setRemark(null);
					allotPo.setCreateDate(new Date());
					allotPo.setCreateBy(userId.toString());
					if(!"60341001".equals(ctmType)&&!"60341004".equals(ctmType)&&!"60341005".equals(ctmType)) {
						allotPo.setStatus(Constant.STATUS_DISABLE);//设置为无效
					} else {
						allotPo.setStatus(Constant.STATUS_ENABLE);//设置为有效
					}
					allotPo.setDealerId(dealerId);
					allotPo.setAllotDealerDate(new Date());
					allotPo.setAdviser(adviser);
					allotPo.setAllotAdviserDate(new Date());
					allotPo.setAllotAgain(Constant.IF_TYPE_NO);
					allotPo.setOldLeadsCode(null);
					insert(allotPo);
					
					if(!"60341001".equals(ctmType)&&!"60341004".equals(ctmType)&&!"60341005".equals(ctmType)) {
						//增加接触点信息
						CommonUtils.addContackPoint(Constant.POINT_WAY_01, "公司类重复线索", customerId, adviser, dealerId);
						String repeatLeads = SequenceManager.getSequence("");
						//新增提醒信息
						CommonUtils.addRemindInfo(Constant.REMIND_TYPE_20.toString(), repeatLeads, customerId, dealerId, adviser, sdf.format(new Date()),"");
						//标记线索为重复线索
						CommonUtils.updateIfRepeat(allotCode.toString());
						CommonUtils.updateLeadStatus(leadsCode.toString());
					} else {
						//新增提醒信息
						CommonUtils.addRemindInfo(Constant.REMIND_TYPE_03.toString(), allotCode.toString(), customerId, dealerId, adviser, sdf.format(new Date()),"");
					}
				} else {//不存在建档，则自动分派到该经销商
					TPcLeadsAllotPO allotPo = new TPcLeadsAllotPO();
					//取经销商ID
					TmDealerPO dealerPo = new TmDealerPO();
					dealerPo.setDealerId(new Long(po2.getDealerId()));
					TmDealerPO dealerPo2 = (TmDealerPO)select(dealerPo).get(0);
					//线索分派主键ID
					Long allotCode = dao.getLongPK(allotPo);
					allotPo.setLeadsAllotId(allotCode);
					allotPo.setLeadsCode(leadsCode);
					allotPo.setCustomerName(po2.getCustomerName());
					allotPo.setTelephone(po2.getTelephone());
					allotPo.setRemark(null);
					allotPo.setCreateDate(new Date());
					allotPo.setCreateBy(userId.toString());
					allotPo.setStatus(Constant.STATUS_ENABLE);
					allotPo.setDealerId(dealerPo2.getDealerId().toString());
					allotPo.setAllotDealerDate(new Date());
					if(po2.getAdviser()!=null&&!"".equals(po2.getAdviser())){
						TcUserPO tu=new TcUserPO();
						tu.setAcnt(po2.getAdviser());
						tu=(TcUserPO) dao.select(tu).get(0);
						allotPo.setAdviser(tu.getUserId().toString());
						allotPo.setAllotAdviserDate(new Date());
					}
					
					allotPo.setAllotAgain(Constant.IF_TYPE_NO);
					allotPo.setOldLeadsCode(null);
					insert(allotPo);
					
				}
			}
		}
		return 1;
	}
	
	/**
	 * 是否已存在客户建档数据(导入时)
	 */
	public List<Map<String, Object>> getHasCustomerImp(String telephone,String name,String dealerId) {
		
		List<Object> params = new LinkedList<Object>();
		StringBuffer sql = new StringBuffer("");

		sql.append("select A.DEALER_ID,A.ADVISER,A.customer_id,A.CTM_TYPE \r\n");
		sql.append("  from t_pc_customer A,TM_DEALER B \r\n");
		sql.append(" where A.DEALER_ID = to_char(B.DEALER_ID) AND a.TELEPHONE = '"+telephone+"'");
		if(dealerId == null) {
			sql.append("   and B.DEALER_CODE = ''"); 
		} else {
			sql.append("   and B.DEALER_CODE = '"+dealerId+"'"); 
		}
		return dao.pageQuery(sql.toString(), params, getFunName());
	}
	
	/***
	 * 获取线索列表
	 * */
	public List<Map<String,Object>> getSourceList() {
		return dao.pageQuery("select CODE_ID,CODE_DESC from TC_CODE where type=6015 and status=10011001 ", null, null);
	}
	
	/**
	 * 获取性别编码
	 * */
	public String getSetMsg(String gendar) {
		Map<String,Object>  gendarMap = dao.pageQueryMap("select CODE_ID from TC_CODE where code_desc='"+gendar+"' and rownum= 1", null, getFunName());
		if (CommonUtils.isNullMap(gendarMap)) {
			return "";
		} else {
			return gendarMap.get("CODE_ID").toString();
		}
	}
	
	/**
	 * 获取车系是否存在
	 * */
	public String getSeriesIshas(String seriesName) {
		Map<String,Object> seriMap= dao.pageQueryMap("select SERIES_ID from t_pc_intent_vehicle where series_name='"+seriesName+"' and rownum= 1",null,getFunName());
		if (CommonUtils.isNullMap(seriMap)) {
			return "";
		} else {
			return seriMap.get("SERIES_ID").toString();
		}
	}
	
	/**
	 * 获取车型是否存在
	 * */
	public String getModelIshas(String modelName) {
		Map<String,Object>  series = dao.pageQueryMap("select SERIES_ID from t_pc_intent_vehicle where series_name='"+modelName+"' and rownum= 1",null,getFunName());
		if (series==null || series.size()==0) {
			return "";
		} else {
			return series.get("SERIES_ID").toString();
		}
	}
	
	/**
	 * 线索来源是否存在
	 * */
	public String getSourceIshas(String sourceName) {
		Map<String,Object>  sourceMap = dao.pageQueryMap("select CODE_ID from TC_CODE where code_desc='"+sourceName+"' and rownum= 1", null, getFunName());
		if (sourceMap==null || sourceMap.size()==0) {
			return "";
		} else {
			return sourceMap.get("CODE_ID").toString();
		}
	}
	
	
	
	
	
}