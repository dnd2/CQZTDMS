package com.infodms.dms.dao.crm.customerContactRecord;

import java.sql.ResultSet;
import java.util.Map;

import com.infodms.dms.dao.common.BaseDao;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

public class CustomerContactRecordDao extends BaseDao<PO> {

	private static final CustomerContactRecordDao dao = new CustomerContactRecordDao();

	public static final CustomerContactRecordDao getInstance() {
		return dao;
	}

	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		return null;
	}

	/**
	 * FUNCTION : 客户履历数据查询
	 * 
	 * @param :
	 * @return :
	 * @throws : LastUpdate : 2015-01-06
	 */
	public PageResult<Map<String, Object>> getGroupQueryList(Map<String, String> map, int pageSize, int curPage) 
	{
		String customerId = map.get("customerId");
		 
		if (null == customerId && "".equals(customerId))
		 
			return null;
		
		StringBuilder sql = new StringBuilder();
		sql.append(" \n");
		sql.append(" SELECT A1.TASK_TYPE,A1.TASK_INFO, A2.CUSTOMER_NAME,A2.TELEPHONE, \n"); 
		sql.append(" DECODE(A1.NEW_LEVEL,'60101001','H','60101002','A','60101003','B','60101004','C','60101005','O','60101006','E','60101007','L') AS NEW_LEVEL,  \n");
		sql.append("  A1.FINISH_DATE \n");
		sql.append(" FROM (   SELECT A.CUSTOMER_ID,  '跟进任务' AS TASK_TYPE, A.FOLLOW_ID AS TASK_ID, A.FOLLOW_INFO as TASK_INFO,A.NEW_LEVEL, TO_CHAR(A.FINISH_DATE,'yyyy-MM-dd') AS FINISH_DATE \n");
		sql.append(" 			FROM T_PC_FOLLOW A WHERE A.TASK_STATUS = 60171002 \n");
		sql.append(" 		UNION ALL \n");
		sql.append("   			SELECT B.CUSTOMER_ID,  '计划邀约任务' AS TASK_TYPE,  B.INVITE_ID AS TASK_ID,B.REMARK AS TASK_INFO,B.NEW_LEVEL, TO_CHAR(B.FINISH_DATE,'yyyy-MM-dd') AS FINISH_DATE \n");
		sql.append("                FROM T_PC_INVITE B WHERE B.TASK_STATUS = 60171002 \n");
		sql.append("     UNION ALL  \n");
		sql.append("          	SELECT C.CUSTOMER_ID, '邀约到店任务' AS TASK_TYPE, C.INVITE_SHOP_ID AS TASK_ID,C.REMARK AS TASK_INFO,C.NEW_LEVEL, TO_CHAR(C.FINISH_DATE,'yyyy-MM-dd') AS FINISH_DATE \n");
		sql.append("             	FROM T_PC_INVITE_SHOP C WHERE C.TASK_STATUS = 60171002 \n");
		sql.append("     UNION ALL \n");
		sql.append("           SELECT D.CUSTOMER_ID, '订单任务' AS TASK_TYPE, D.ORDER_ID AS TASK_ID,'' AS TASK_INFO, D.NEW_LEVEL, TO_CHAR(D.FINISH_DATE,'yyyy-MM-dd') AS FINISH_DATE \n");
		sql.append("             	FROM T_PC_ORDER D WHERE D.TASK_STATUS = 60171002 \n");
		sql.append("   	UNION ALL \n");
		sql.append("        	SELECT D2.CUSTOMER_ID, '退单任务' AS TASK_TYPE, D2.ORDER_ID AS TASK_ID,'' AS TASK_INFO, D2.NEW_LEVEL, TO_CHAR(D2.FINISH_DATE,'yyyy-MM-dd') AS FINISH_DATE \n");
		sql.append("          		FROM T_PC_ORDER D2 WHERE D2.TASK_STATUS = 60171002 AND D2.ORDER_STATUS=60231007  \n");
		sql.append("   	UNION ALL \n");
		sql.append("         	SELECT E.CUSTOMER_ID, '交车任务' AS TASK_TYPE, E.ORDER_DETAIL_ID AS TASK_ID,'' AS TASK_INFO, 'O' as NEW_LEVEL, TO_CHAR(E.ACT_DELV_DATE,'yyyy-MM-dd') AS FINISH_DATE \n");
		sql.append("            	FROM T_PC_ORDER_DETAIL E WHERE E.TASK_STATUS = 60171002  \n");
		sql.append("   	UNION ALL \n");
		sql.append("        	SELECT F.CUSTOMER_ID, '回访任务' AS TASK_TYPE, F.REVISIT_ID AS TASK_ID,'' AS TASK_INFO, '60101005' as NEW_LEVEL, TO_CHAR(F.FINISH_DATE,'yyyy-MM-dd') AS FINISH_DATE \n");
		sql.append("                FROM T_PC_REVISIT F WHERE F.TASK_STATUS = 60171001 \n");
		sql.append("    	) A1,T_PC_CUSTOMER A2  \n");
		sql.append("   WHERE A1.CUSTOMER_ID = A2.CUSTOMER_ID \n"); 
		
		if (null != customerId && !"".equals(customerId)) 
		{
			sql.append(" AND A1.CUSTOMER_ID =" + customerId.trim() + "\n");
		}

		PageResult<Map<String, Object>> ps = dao.pageQuery(sql.toString(),null, dao.getFunName(), pageSize, curPage);
		
		return ps;
	}
	
	 
}
