package com.infodms.dms.dao.sysbusinesparams.businesparamsmanage;

import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

public class CRMSeatLogDAO extends BaseDao<PO>{
    public static Logger logger = Logger.getLogger(CRMSeatLogDAO.class);
    private static CRMSeatLogDAO dao = new CRMSeatLogDAO();
	public static final CRMSeatLogDAO getInstance() {
		return dao;
	}

	/**
	 * @FUNCTION : 登陆日志查询实际执行
	 * @author : andyzhou
	 * @param :
	 * @return :
	 * @throws :
	 * @LastDate : 2013-7-1
	 */
	public PageResult<Map<String, Object>> getMainList(String id, String checkSDate, String checkEDate, int pageSize, int curPage){
		List<Object> params = new LinkedList<Object>();
		StringBuffer sql = new StringBuffer("");
		sql.append(
				"SELECT T3.NAME,\n" +
				"       T2.SE_EXT,\n" + 
				"       T2.SE_IS_MANAMGER,\n" + 
				"       T2.SE_LEVEL,\n" + 
				"       TO_CHAR(T1.LOGIN_DATE, 'HH24:MI:SS') LOGIN_DATE,\n" + 
				"       TO_CHAR(T1.LOGOUT_DATE, 'HH24:MI:SS') LOGOUT_DATE,\n" + 
				"       to_char(T1.LOG_DAYS,'YYYY-MM-DD') LOG_DAYS,\n" + 
				"       T1.IP,\n" + 
				"       T1.HOSTNAME\n" + 
				"  FROM TT_CRM_SEATS_LOG T1, TT_CRM_SEATS T2, TC_USER T3\n" + 
				" WHERE T1.USER_ID = T2.SE_USER_ID\n" + 
				"   AND T1.USER_ID = T3.USER_ID \n");
        if(!id.equals("")){
        	sql.append("and t3.user_id = "+id+"  \n");
        }
		if (!checkSDate.equals("")){
			sql.append("and (trunc(t1.LOGIN_DATE) >=to_date('" + checkSDate + "','yyyy-mm-dd') OR (t1.LOGIN_DATE IS NULL AND t1.log_days >= to_date('"+checkSDate+"','yyyy-mm-dd'))) \n");
		}
		if (!checkEDate.equals("")){
			sql.append("and (trunc(t1.LOGIN_DATE) <=to_date('" + checkEDate + " 23:59:59','yyyy-mm-dd hh24:mi:ss') or  (t1.LOGIN_DATE IS NULL AND t1.log_days <= to_date('"+checkEDate+"','yyyy-mm-dd') + 0.5) ) \n");
		}
		// 艾春 13.11.22 修改坐席日志排序功能
		sql.append("order by t1.log_days DESC, t1.login_date DESC, t1.logout_date DESC  \n");
		logger.info(sql.toString());
		return dao.pageQuery(sql.toString(), params, getFunName() + System.currentTimeMillis(), pageSize, curPage);
	}	
	
	public List<Map<String,Object>> getSeats() {
		String sql = "SELECT T.SE_USER_ID ID, T.SE_NAME NAME FROM TT_CRM_SEATS T WHERE t.se_is_manamger = "+Constant.se_is_manamger_2;
		return dao.pageQuery(sql, null, this.getFunName());
	}
	
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}	
}
