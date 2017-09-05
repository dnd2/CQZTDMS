package com.infodms.dms.dao.crm.follow;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

import com.infodms.dms.common.Constant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.dao.common.JCDynaBeanCallBack;
import com.infoservice.po3.bean.DynaBean;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

public class FollowManageDao extends BaseDao<PO>{
	
	private static final FollowManageDao dao = new FollowManageDao();
	
	public static final FollowManageDao getInstance() {
		return dao;
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		return null;
	}
	
	/**
	 * 跟进查询
	 */
	public PageResult<Map<String, Object>> followFindQuery(
			String customerName,
			String telephone,String startDate,String endDate,
		    String userDealerId, String adviser, String adviserId, String groupId, String followType,String ctmRank,String finishStatus,
			int curPage, int pageSize) {
		
		StringBuffer sbSql = new StringBuffer();
		
		sbSql.append("SELECT A.FOLLOW_ID,\r\n");
		sbSql.append("       B.CUSTOMER_NAME,\r\n");
		sbSql.append("       B.CTM_RANK,\r\n");
		sbSql.append("       B.TELEPHONE,C3.NAME AS ADVISER,\r\n");
		sbSql.append("       TO_CHAR(A.FINISH_DATE, 'YYYY-MM-DD') AS FINISH_DATE,\r\n");
		sbSql.append("       TO_CHAR(A.FOLLOW_DATE, 'YYYY-MM-DD') AS FOLLOW_DATE,\r\n");
		sbSql.append("       C1.CODE_DESC AS OLD_LEVEL, \r\n");
		sbSql.append("       C2.CODE_DESC AS NEW_LEVEL, \r\n");
		sbSql.append("       C4.CODE_DESC AS FOLLOW_TYPE \r\n");
		sbSql.append("  FROM T_PC_FOLLOW A\r\n");
		sbSql.append("  LEFT JOIN T_PC_CUSTOMER B\r\n");
		sbSql.append("    ON A.CUSTOMER_ID = B.CUSTOMER_ID\r\n");
		sbSql.append("  LEFT JOIN TC_USER C3\r\n");
		sbSql.append("    ON B.ADVISER = C3.USER_ID\r\n");
		sbSql.append("  LEFT JOIN TC_CODE C4 ON A.FOLLOW_TYPE = C4.CODE_ID \r\n");
		sbSql.append("  LEFT JOIN TC_CODE C1\r\n");
		sbSql.append("    ON A.OLD_LEVEL = C1.CODE_ID LEFT JOIN TC_CODE C2 ON A.NEW_LEVEL = C2.CODE_ID  WHERE 1=1  AND A.OLD_LEVEL IS NOT NULL "); 

		if (Utility.testString(customerName)) {//客户姓名
			sbSql.append(" and b.customer_name like '%"+customerName+"%' ");
		}
		if (Utility.testString(ctmRank)) {//客户姓名
			sbSql.append(" and b.ctm_rank = "+ctmRank);
		}
		if (Utility.testString(telephone)) {//联系电话
			sbSql.append(" and b.telephone like '%"+telephone+"%' ");
		}
		if (Utility.testString(startDate)) {//跟进日期
			sbSql.append(" and to_char(a.FOLLOW_DATE,'yyyy-MM-dd') >= '"+startDate+"' ");
		}
		if (Utility.testString(endDate)) {//跟进日期
			sbSql.append(" and to_char(a.FOLLOW_DATE,'yyyy-MM-dd') <= '"+endDate+"' ");
		}
		if (Utility.testString(followType)) {//跟进方式
			sbSql.append(" and a.follow_type = '"+followType+"' ");
		}
		if(Utility.testString(userDealerId)){
			sbSql.append(" and b.dealer_id = '"+userDealerId+"' \n");
		}
		if(Utility.testString(finishStatus)){
			sbSql.append(" and a.task_status = '"+finishStatus+"' \n");
		}
		if(Utility.testString(adviser)){
			sbSql.append(" and b.adviser in ("+adviser+") \n");
		}
		if(Utility.testString(adviserId)){
			sbSql.append(" and b.adviser = '"+adviserId+"' \n");
		}
		if(Utility.testString(groupId)){
			sbSql.append(" and c3.group_id = '"+groupId+"' \n");
		}
		sbSql.append("order by a.FOLLOW_DATE desc");
		PageResult<Map<String, Object>> ps = pageQuery(sbSql.toString(), null, getFunName(), pageSize, curPage);
		return ps;
	}
	
	/*
	 * 获取客户基本信息
	 */
	public List<DynaBean> getCustomerInfo(String followId) {
		StringBuilder sql= new StringBuilder();
		sql.append("SELECT a.CUSTOMER_CODE,a.customer_name,a.telephone,TO_CHAR(C.FINISH_DATE,'yyyy-MM-dd') AS FINISH_DATE,\n" );
		sql.append("       A.ADDRESS,a.jc_way,a.buy_budget,a.ctm_prop,B.SERIES_NAME AS intent_VEHICLE,\n" );
		sql.append("       C.FOLLOW_ID,D1.CODE_DESC AS CTM_RANK,D2.CODE_DESC AS SALES_PROGRESS, C.FOLLOW_INFO,C.FOLLOW_PLAN,D3.CODE_DESC follow_type,to_char(c.follow_date,'yyyy-mm-dd') follow_date\n" );
		sql.append("  FROM T_PC_CUSTOMER A\n" );
		sql.append("  LEFT JOIN T_PC_FOLLOW C ON A.CUSTOMER_ID = C.CUSTOMER_ID\n" );
		sql.append("  LEFT JOIN T_PC_INTENT_VEHICLE B ON A.intent_vehicle = B.SERIES_ID\n" );
		sql.append("  LEFT JOIN TC_CODE D1 ON A.CTM_RANK = D1.CODE_ID\n" );
		sql.append("  LEFT JOIN TC_CODE D2 ON A.SALES_PROGRESS = D2.CODE_ID\n" );
		sql.append("  left join tc_code d3 on c.follow_type=d3.code_id\n" );
		sql.append(" WHERE 1=1");
		sql.append("   AND C.FOLLOW_ID = "+followId+"");
		
		return factory.select(sql.toString(), null, new JCDynaBeanCallBack());
	}
}