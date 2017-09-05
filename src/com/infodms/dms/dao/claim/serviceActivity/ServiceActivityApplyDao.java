package com.infodms.dms.dao.claim.serviceActivity;

import java.sql.ResultSet;
import java.sql.Types;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.StringUtil;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;
import com.infoservice.po3.core.exceptions.DAOException;

public class ServiceActivityApplyDao extends BaseDao{
	public static Logger logger = Logger.getLogger(ServiceActivityApplyDao.class);
	private static final ServiceActivityApplyDao dao = new ServiceActivityApplyDao();

	public static final ServiceActivityApplyDao getInstance() {
		return dao;
	}

	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	/**
	 * 查询经销商基础数据
	 * @param dealerId
	 * @return
	 */
	public Map<String, Object> queryDealerInfo(String dealerId) {
		StringBuffer sql = new StringBuffer();

		sql.append(" SELECT T.DEALER_ID,\n");
		sql.append("          T.DEALER_CODE,\n");
		sql.append("          T.DEALER_NAME,\n");
		sql.append("          T1.ORG_NAME,\n");
		sql.append("          T1.ORG_CODE,T1.ORG_ID,\n");
		sql.append("          T1.ORG_ID,TO_CHAR(SYSDATE, 'yyyy-MM-dd') NOWDATE\n");
		sql.append("     FROM TM_DEALER T, TM_ORG T1\n");
		sql.append("    WHERE T.DEALER_ORG_ID = T1.ORG_ID\n");
		sql.append("      AND T.DEALER_ID = "+dealerId+"");
		return this.pageQueryMap(sql.toString(), null, null);
	}
	
public PageResult<Map<String, Object>> queryServiceActivityApply(Map<String, Object> map,String dealerId,int pageSize,int curPage){
		
		String applyNo = CommonUtils.checkNull(map.get("applyNo")); 
		String addStart = CommonUtils.checkNull(map.get("addStart"));
		String addEnd = CommonUtils.checkNull(map.get("addEnd"));
		String status = CommonUtils.checkNull(map.get("status"));
		
		StringBuffer sql = new StringBuffer();

		sql.append(" SELECT T.ID,\n");
		sql.append("         T.DEALER_CODE,\n");
		sql.append("         T.DEALER_NAME,\n");
		sql.append("         T.ORG_NAME,\n");
		sql.append("         TO_CHAR(T.CREATE_DATE, 'yyyy-MM-dd hh24:mi:ss') CRDATE,\n");
		sql.append("         T.STATUS,\n");
		sql.append("         T.APPLY_NO\n");
		sql.append("    FROM TT_SERVICE_ACTIVITY_APPLY T\n");
		sql.append("   WHERE 1 = 1\n");
		sql.append("     AND T.DEALER_ID = "+dealerId+"\n");
		sql.append("	AND T.STATUS>0\n");
		//查询条件
		if(StringUtil.notNull(status)){
			sql.append("     AND T.STATUS="+status+"\n");
		}		
		if(StringUtil.notNull(addStart)){
		sql.append("     AND T.CREATE_DATE>= TO_DATE('"+addStart+"', 'YYYY-MM-DD')\n");
		}
		if(StringUtil.notNull(addEnd)){
		sql.append("     AND T.CREATE_DATE<=\n");
		sql.append("        TO_DATE('"+addEnd+" 23:59:59', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(StringUtil.notNull(applyNo)){
		sql.append("     AND T.APPLY_NO LIKE '%"+applyNo+"%'");
		}
		sql.append("  ORDER BY T.CREATE_DATE DESC");

		return dao.pageQuery(sql.toString(), null, getFunName(),pageSize, curPage);
	}

public PageResult<Map<String, Object>> queryServiceActivityAudit(Map<String, Object> map,int pageSize,int curPage){
	
	String applyNo = CommonUtils.checkNull(map.get("applyNo")); 
	String addStart = CommonUtils.checkNull(map.get("addStart"));
	String addEnd = CommonUtils.checkNull(map.get("addEnd"));
	String status = CommonUtils.checkNull(map.get("status"));
	String dealerCode = CommonUtils.checkNull(map.get("dealerCode"));
	
	StringBuffer sql = new StringBuffer();

	sql.append(" SELECT T.ID,\n");
	sql.append("         T.DEALER_CODE,\n");
	sql.append("         T.DEALER_NAME,\n");
	sql.append("         T.ORG_NAME,\n");
	sql.append("         TO_CHAR(T.CREATE_DATE, 'yyyy-MM-dd hh24:mi:ss') CRDATE,\n");
	sql.append("         T.STATUS,\n");
	sql.append("         T.APPLY_NO\n");
	sql.append("    FROM TT_SERVICE_ACTIVITY_APPLY T\n");
	sql.append("   WHERE 1 = 1\n");
	sql.append("	AND T.STATUS NOT IN("+0+","+Constant.SERVICEACTIVITYAPPLY_STATUS_01+")\n");
	//查询条件
	if(StringUtil.notNull(status)){
		sql.append("     AND T.STATUS="+status+"\n");
	}	
	if(StringUtil.notNull(dealerCode)){
		sql.append("     AND T.DEALER_CODE='"+dealerCode+"'\n");
	}		
	if(StringUtil.notNull(addStart)){
	sql.append("     AND T.CREATE_DATE>= TO_DATE('"+addStart+"', 'YYYY-MM-DD')\n");
	}
	if(StringUtil.notNull(addEnd)){
	sql.append("     AND T.CREATE_DATE<=\n");
	sql.append("        TO_DATE('"+addEnd+" 23:59:59', 'YYYY-MM-DD HH24:MI:SS')\n");
	}
	if(StringUtil.notNull(applyNo)){
	sql.append("     AND T.APPLY_NO LIKE '%"+applyNo+"%'");
	}
	sql.append("  ORDER BY T.CREATE_DATE DESC");

	return dao.pageQuery(sql.toString(), null, getFunName(),pageSize, curPage);
}
	/**
	 * 查询经销商基础数据
	 * @param dealerId
	 * @return
	 */
	public Map<String, Object> queryserviceActivityInfo(String id) {
		StringBuffer sql = new StringBuffer();

		sql.append(" SELECT T.ID,\n");
		sql.append("        T.DEALER_ID,\n");
		sql.append("        T.DEALER_CODE,\n");
		sql.append("        T.DEALER_NAME,\n");
		sql.append("        T.ORG_NAME,\n");
		sql.append("        T.ORG_ID,\n");
		sql.append("        T.ORG_CODE,\n");
		sql.append(" 		TO_CHAR(T.START_DATE,'yyyy-MM-dd') START_DATE,\n");
		sql.append("        TO_CHAR(T.END_DATE,'yyyy-MM-dd') END_DATE,\n");
		sql.append("        T.ACTIVITY_CONTENT,\n");
		sql.append("        TO_CHAR(T.CREATE_DATE, 'yyyy-MM-dd') NOWDATE\n");
		sql.append("   FROM TT_SERVICE_ACTIVITY_APPLY T\n");
		sql.append("  WHERE 1 = 1\n");
		sql.append("    AND T.ID = "+id+"");

		return this.pageQueryMap(sql.toString(), null, null);
	}
	
	
	/**
	 * 查询处理结果数据
	 * @param cpid
	 * @return
	 */
	public List<Map<String, Object>> queryApplyRecord(long id) {
		StringBuffer sql = new StringBuffer();

		sql.append(" SELECT T.AUDIT_CONTENT,\n");
		sql.append("        F_GET_TCCODE_DESC(T.STATUS) STATUS,\n");
		sql.append("        TO_CHAR(T.CREATE_DATE, 'yyyy-MM-dd') CDDATE,\n");
		sql.append("        T1.NAME\n");
		sql.append("   FROM TT_SERVICE_ACTIVITY_RECORD T\n");
		sql.append("   LEFT JOIN TC_USER T1\n");
		sql.append("     ON T.CREATE_BY = T1.USER_ID\n");
		sql.append("  WHERE T.AC_ID = "+id+"\n");
		sql.append("  ORDER BY T.ID DESC");

		return this.pageQuery(sql.toString(), null, null);
	}
}
