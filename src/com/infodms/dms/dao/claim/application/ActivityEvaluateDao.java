package com.infodms.dms.dao.claim.application;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import com.infodms.dms.bean.ActivityEvaluateBean;
import com.infodms.dms.dao.common.BaseDao;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

public class ActivityEvaluateDao extends BaseDao {
	public static Logger logger = Logger.getLogger(BalanceQueryDAO.class);
	private static final ActivityEvaluateDao dao = new ActivityEvaluateDao();

	public static final ActivityEvaluateDao getInstance() {
		return dao;
	}

	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		return null;
	}

	public PageResult<ActivityEvaluateBean> actEvaQuery(
			Long subjectId, int pageSize, int curPage) {
		StringBuffer sql = new StringBuffer();
		List<Long> ids = new ArrayList<Long>();
		List<String> nos = new ArrayList<String>();
		PageResult<ActivityEvaluateBean> ps = null;

		if (null == subjectId) {
			return null;
		} else {
			sql.append("SELECT R.SUBJECTID,\n" 
					+ "        D.DEALER_CODE DEALERCODE,\n"
					+ "        D.DEALER_NAME DEALERNAME,\n"
					+ "        SUM(NVL(R.BALANCE_AMOUNT, 0)) BALANCEAMOUNT\n"
					+ "  FROM TM_DEALER D\n"
					+ "  LEFT JOIN (SELECT S.SUBJECT_ID SUBJECTID, A.DEALER_ID, A.BALANCE_AMOUNT\n"
					+ "               FROM TT_AS_WR_APPLICATION   A,\n"
					+ "                    TT_AS_ACTIVITY         T,\n"
					+ "                    TT_AS_ACTIVITY_SUBJECT S\n"
					+ "              WHERE A.CAMPAIGN_CODE = T.ACTIVITY_CODE\n"
					+ "                AND T.SUBJECT_ID = S.SUBJECT_ID\n"
					+ "                AND S.ACTIVITY_TYPE != 10561001\n"
					+ "                AND A.STATUS = 10791013\n"
					+ "                AND s.subject_id =\n" + subjectId
					+ "             ) R ON D.DEALER_ID = R.DEALER_ID\n"
					+ " WHERE D.DEALER_LEVEL = 10851001\n"
					+ " GROUP BY R.SUBJECTID, D.DEALER_CODE, D.DEALER_NAME");
			sql.append(" order by d.dealer_code ");
			ps = pageQuery(ActivityEvaluateBean.class, sql.toString(), null,
					pageSize, curPage);
		}

		return ps;
	}
	
	public PageResult<Map<Long,String>> claimQuery(Long subjectId, int pageSize, int curPage) {
		StringBuffer sql = new StringBuffer();
		PageResult<Map<Long, String>> ps = null;

		if (null == subjectId) {
			return null;
		} else {
			sql.append("SELECT s.subject_id SUBJECTID, a.ID CLAIMID , A.CLAIM_NO CLAIMNO\n"
					+ "               FROM TT_AS_WR_APPLICATION   A,\n"
					+ "                    TT_AS_ACTIVITY         T,\n"
					+ "                    TT_AS_ACTIVITY_SUBJECT S\n"
					+ "              WHERE A.CAMPAIGN_CODE = T.ACTIVITY_CODE\n"
					+ "                AND T.SUBJECT_ID = S.SUBJECT_ID\n"
					+ "                AND S.ACTIVITY_TYPE != 10561001\n"
					+ "                AND A.STATUS = 10791013\n"
					+ "                AND s.subject_id =\n" + subjectId);
			ps = pageQuery(sql.toString(), null, pageSize, curPage);
		}

		return ps;
	}
}
