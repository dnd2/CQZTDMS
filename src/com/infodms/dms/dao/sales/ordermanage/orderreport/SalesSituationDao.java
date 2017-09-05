/**
 * @Title: OrderReport.java
 * 
 * @Description:
 * 
 * @Copyright: Copyright (c) 2010
 * 
 * @Company: www.infoservice.com.cn
 * @Date: 2010-5-24
 * 
 * @author yuyong
 * @mail yuyong@infoservice.com.cn
 * @version 1.0
 * @remark
 */
package com.infodms.dms.dao.sales.ordermanage.orderreport;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

public class SalesSituationDao extends BaseDao<PO> {
	public static Logger logger = Logger.getLogger(SalesSituationDao.class);
	private static final SalesSituationDao dao = new SalesSituationDao();

	public PageResult<Map<String, Object>> getSalesSituationList(String dealerId, String startDate, String endDate, int curPage, int pageSize) {

		StringBuffer sql = new StringBuffer();

		sql.append("SELECT T.SITUATION_ID,\n");
		sql.append("       T.SITUATION_NO,\n");
		sql.append("       T.CREATE_DATE,\n");
		sql.append("       T.STATE,\n");
		sql.append("       (SELECT TC.CODE_DESC FROM TC_CODE TC WHERE TC.CODE_ID = T.STATE) AS CODE_DESC\n");
		sql.append("  FROM TT_VS_SALES_SITUATION T\n");
		sql.append(" WHERE T.DEALER_ID = '" + dealerId + "'\n");
		sql.append("   AND T.STATUS = '" + Constant.STATUS_ENABLE + "'\n");

		if (startDate != null && !"".equals(startDate)) {
			sql.append("   AND TRUNC(T.CREATE_DATE, 'DD') >= TO_DATE('" + startDate + "', 'YYYY-MM-DD')\n");
		}

		if (endDate != null && !"".equals(endDate)) {
			sql.append("   AND TRUNC(T.CREATE_DATE, 'DD') <= TO_DATE('" + endDate + "', 'YYYY-MM-DD')\n");
		}

		sql.append(" ORDER BY T.SITUATION_ID DESC");

		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, "com.infodms.dms.dao.sales.OrderReportDao.getGeneralOrderReportList", pageSize, curPage);
		return ps;
	}

	public PageResult<Map<String, Object>> getSalesSituationListOrg(String dutyType, String orgId, String startDate, String endDate, int curPage, int pageSize) {

		StringBuffer sql = new StringBuffer();

		sql.append("SELECT T.SITUATION_ID,\n");
		sql.append("       T.SITUATION_NO,\n");
		sql.append("       T.CREATE_DATE,\n");
		sql.append("       T.STATE,\n");
		sql.append("       (SELECT TC.CODE_DESC FROM TC_CODE TC WHERE TC.CODE_ID = T.STATE) AS CODE_DESC,\n");
		sql.append("       ORG.ROOT_ORG_NAME,\n");
		sql.append("       ORG.PQ_ORG_NAME,\n");
		sql.append("       ORG.DEALER_CODE,\n");
		sql.append("       ORG.DEALER_NAME\n");
		sql.append("  FROM TT_VS_SALES_SITUATION T, VW_ORG_DEALER ORG\n");
		sql.append(" WHERE T.DEALER_ID = ORG.DEALER_ID\n");
		
		if (startDate != null && !"".equals(startDate)) {
			sql.append("   AND TRUNC(T.CREATE_DATE, 'DD') >= TO_DATE('" + startDate + "', 'YYYY-MM-DD')\n");
		}

		if (endDate != null && !"".equals(endDate)) {
			sql.append("   AND TRUNC(T.CREATE_DATE, 'DD') <= TO_DATE('" + endDate + "', 'YYYY-MM-DD')\n");
		}
		   
		sql.append("   AND T.STATE = " + Constant.FORECAST_STATUS_CONFIRM + "\n");
		sql.append("   AND ORG.DEALER_ID IN\n");
		sql.append("       (SELECT VOD.DEALER_ID\n");
		sql.append("          FROM VW_ORG_DEALER VOD\n");
		sql.append("         WHERE VOD.PQ_ORG_ID = " + orgId + "\n");
		sql.append("        UNION ALL\n");
		sql.append("        SELECT VOD.DEALER_ID\n");
		sql.append("          FROM VW_ORG_DEALER VOD\n");
		sql.append("         WHERE VOD.ROOT_ORG_ID = " + orgId + "\n");
		sql.append("        UNION ALL\n");
		sql.append("        SELECT VOD.DEALER_ID\n");
		sql.append("          FROM VW_ORG_DEALER VOD\n");
		sql.append("         WHERE VOD.COMPANY_ID = " + orgId + ")\n");
		sql.append(" ORDER BY T.SITUATION_ID DESC");

		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, "com.infodms.dms.dao.sales.OrderReportDao.getGeneralOrderReportList", pageSize, curPage);
		return ps;
	}

	public List<Map<String, Object>> getSalesSituationModList(String situationId) {

		StringBuffer sql = new StringBuffer();
		sql.append("SELECT D.DETAIL_ID,\n");
		sql.append("       D.MATERIAL_ID,\n");
		sql.append("       DR.DEALER_ID,\n");
		sql.append("      M.GROUP_CODE MATERIAL_CODE,\n");
		sql.append("       M.GROUP_NAME MATERIAL_NAME,\n");
		sql.append("       D.ORDER_AMOUNT,\n");
		sql.append("       D.RET_AMOUNT,\n");
		sql.append("       D.POOR_AMOUNT\n");
		sql.append("  FROM TT_VS_SALES_SITUATION_DTL D,TT_VS_SALES_SITUATION DR, TM_VHCL_MATERIAL_GROUP_PRE M\n");
		sql.append(" WHERE D.MATERIAL_ID = M.GROUP_PRE_ID\n");
		sql.append(" AND  DR.SITUATION_ID = D.SITUATION_ID\n");
		sql.append("   AND D.SITUATION_ID = '" + situationId + "'");

		List<Map<String, Object>> ps = pageQuery(sql.toString(), null, "com.infodms.dms.dao.sales.OrderReportDao.getGeneralOrderReportList");
		return ps;
	}

	protected PO wrapperPO(ResultSet rs, int idx) {
		return null;
	}

	public static SalesSituationDao getInstance() {
		return dao;
	}
}
