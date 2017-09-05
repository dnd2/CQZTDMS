package com.infodms.dms.dao.sales.balancecentermanage.stockordermanage;

import java.sql.ResultSet;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.dao.sales.storageManage.CheckVehicleQueryDAO;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

/**
 * @Title : StockOrderConfirmDAO.java
 * @Package: com.infodms.dms.dao.sales.balancecentermanage.stockordermanage
 * @Description: 采购订单确认DAO
 * @date : 2010-6-22
 * @version: V1.0
 */
public class StockOrderConfirmDAO extends BaseDao {
	public static Logger logger = Logger.getLogger(CheckVehicleQueryDAO.class);
	private static final StockOrderConfirmDAO dao = new StockOrderConfirmDAO();

	public static final StockOrderConfirmDAO getInstance() {
		return dao;
	}

	/**
	 * @Title : getCheckQueryDLR
	 * @Description: 查询可进行确认的采购订单
	 * @param : @param year
	 * @param : @param week
	 * @param : @param areaId
	 * @param : @param dealerId
	 * @param : @param pageSize
	 * @param : @param curPage
	 * @throws
	 * @LastUpdate :2010-6-22
	 */
	public static PageResult<Map<String, Object>> getStockOrderConfirmPreList(String year, String week, String areaId, String dealerId, int pageSize, int curPage) {
		StringBuffer sql = new StringBuffer();

		sql.append("SELECT A.ORDER_ORG_NAME ORDER_ORG_NAME,\n");
		sql.append("       A.ORDER_ORG_ID,\n");
		sql.append("       A.BILLING_ORG_NAME,\n");
		sql.append("       A.ORDER_ID,\n");
		sql.append("       A.ORDER_NO,\n");
		sql.append("       A.QUOTA_DATE,\n");
		sql.append("       A.ORDER_TYPE,\n");
		sql.append("       A.ORDER_STATUS,\n");
		sql.append("       B.ORDER_AMOUNT\n");
		sql.append("  FROM (SELECT TD1.DEALER_SHORTNAME ORDER_ORG_NAME,\n");
		sql.append("               TD1.DEALER_ID ORDER_ORG_ID,\n");
		sql.append("               TD2.DEALER_SHORTNAME BILLING_ORG_NAME,\n");
		sql.append("               TSO.ORDER_ID,\n");
		sql.append("               TSO.ORDER_NO,\n");
		sql.append("               TSO.ORDER_YEAR || '年' || TSO.ORDER_WEEK || '周' QUOTA_DATE,\n");
		sql.append("               TSO.ORDER_TYPE,\n");
		sql.append("               TSO.ORDER_STATUS\n");
		sql.append("          FROM TT_VS_ORDER        TSO,\n");
		sql.append("               TM_DEALER             TD1,\n");
		sql.append("               TM_DEALER             TD2\n");
		sql.append("         WHERE TSO.ORDER_ORG_ID = TD1.DEALER_ID(+)\n");
		sql.append("           AND TSO.BILLING_ORG_ID = TD2.DEALER_ID(+)\n");
		if(null != year && !"".equals(year)) {
			sql.append("           AND TSO.ORDER_YEAR = " + year + "\n");
		}
		if(null != week && !"".equals(week)) {
			sql.append("           AND TSO.ORDER_WEEK = " + week + "\n");
		}
		sql.append("           AND TSO.AREA_ID = " + areaId + "\n");
		sql.append("           AND TSO.BILLING_ORG_ID = " + dealerId + "\n");
		sql.append("           AND TSO.ORDER_STATUS = " + Constant.ORDER_STATUS_07 + "\n");
		sql.append("           AND TSO.ORDER_TYPE = " + Constant.ORDER_TYPE_02 + ") A,\n");
		sql.append("       (SELECT TSO.ORDER_ID, SUM(TSOD.ORDER_AMOUNT) ORDER_AMOUNT\n");
		sql.append("          FROM TT_VS_ORDER TSO, TT_VS_ORDER_DETAIL TSOD\n");
		sql.append("         WHERE TSO.ORDER_ID = TSOD.ORDER_ID\n");
		/*if(null != year && !"".equals(year)) {
			sql.append("           AND TSO.ORDER_YEAR = " + year + "\n");
		}
		if(null != week && !"".equals(week)) {
			sql.append("           AND TSO.ORDER_WEEK = " + week + "\n");
		}*/
		sql.append("           AND TSO.AREA_ID = " + areaId + "\n");
		sql.append("           AND TSO.BILLING_ORG_ID = " + dealerId + "\n");
		sql.append("           AND TSO.ORDER_STATUS = " + Constant.ORDER_STATUS_07 + "\n");
		sql.append("           AND TSO.ORDER_TYPE = " + Constant.ORDER_TYPE_02 + "\n");
		sql.append("         GROUP BY TSO.ORDER_ID) B\n");
		sql.append(" WHERE A.ORDER_ID = B.ORDER_ID");
		return dao.pageQuery(sql.toString(), null, dao.getFunName(), pageSize, curPage);
	}

	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
}
