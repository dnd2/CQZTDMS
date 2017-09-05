package com.infodms.dms.dao.report;

import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.infodms.dms.common.Constant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.dao.common.BaseDao;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

/**
 * 
 * @ClassName : DateTimeCheckDao
 * @Description : 授信详细信息DAO
 * @author : ranjian
 *         CreateDate : 2013-9-12
 */
public class AwardFaithDao extends BaseDao<PO> {
	private static final AwardFaithDao dao = new AwardFaithDao();

	public static final AwardFaithDao getInstance() {
		return dao;
	}

	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 授信详细信息查询列表
	 * 
	 * @param map
	 * @param curPage
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	public PageResult<Map<String, Object>> getAwardFaithQuery(Map<String, Object> map, int curPage, int pageSize) throws Exception {
		Object[] obj = getSQL(map);
		PageResult<Map<String, Object>> ps = dao.pageQuery(obj[0].toString(), (List<Object>) obj[1], getFunName(), pageSize, curPage);
		return ps;
	}

	/**
	 * 授信详细信息导出
	 * 
	 * @param map
	 * @return 授信详细信息
	 * @throws Exception
	 */
	public List<Map<String, Object>> getAwardFaithExport(Map<String, Object> map) throws Exception {
		Object[] obj = getSQL(map);
		List<Map<String, Object>> list = dao.pageQuery(obj[0].toString(), (List<Object>) obj[1], getFunName());
		return list;
	}

	//get sql
	public Object[] getSQL(Map<String, Object> map) {
		/****************************** 页面查询字段start **************************/
		String dealerCode = (String) map.get("dealerCode"); // 经销商CODE
		String staType = (String) map.get("staType"); // 统计类型
		String ORDER_STARTDATE = (String) map.get("ORDER_STARTDATE"); //
		String ORDER_ENDDATE = (String) map.get("ORDER_ENDDATE"); // 
		/****************************** 页面查询字段end ***************************/
		List<Object> params = new LinkedList<Object>();
		StringBuffer sbSql = new StringBuffer();

		params.add(Constant.ORDER_STATUS_11);

		/*if (!"".equals(staType) && staType.equals(Constant.ACCOUNT_TYPE_04.toString()))
		{//--深圳发展银行
			params.add(Constant.ACCOUNT_TYPE_04);
		}*/
		 if (!"".equals(staType) && staType.equals(Constant.ACCOUNT_TYPE_03.toString()))
		{//--中信银行
			params.add(Constant.ACCOUNT_TYPE_03);
		}
		else
		{//招融
			//params.add(Constant.ACCOUNT_TYPE_06);
		}

		sbSql.append("SELECT ROWNUM,\n");
		sbSql.append("       C.DEALER_NAME,\n");
		sbSql.append("       TO_CHAR(C.INVO_DATE,'YYYY-MM-DD') INVO_DATE,C.INVOICE_NO,\n");
		sbSql.append("       F.HEGEZHENG_CODE,\n");
		sbSql.append("       F.ENGINE_NO,\n");
		sbSql.append("       H.ERP_MODEL MODEL_CODE,\n");
		sbSql.append("       H.TRIM_CODE,\n");
		sbSql.append("       H.ERP_PACKAGE,\n");
		sbSql.append("       F.VIN,\n");
		sbSql.append("       H.COLOR_NAME,\n");
		sbSql.append("       '' REMARK,\n");
		sbSql.append("       C.PRICE_COUNT\n");
		sbSql.append("  FROM TT_SALES_ALLOCA_DE A,\n");
		sbSql.append("       TT_SALES_BO_DETAIL B,\n");
		sbSql.append("       TM_VEHICLE F,\n");
		sbSql.append("       TM_VHCL_MATERIAL H,\n");
		sbSql.append("       (\n");
		sbSql.append("         SELECT C.DETAIL_ID,D.INVO_DATE,E.DEALER_NAME,D.INVOICE_NO,\n");
		sbSql.append("                DECODE(NVL(D.CHK_NUM, 0),\n");
		sbSql.append("                       0,\n");
		sbSql.append("                       0,\n");
		sbSql.append("                       ROUND(NVL(D.ORDER_YF_PRICE, 0) / NVL(D.CHK_NUM, 0), 2)) PRICE_COUNT\n");
		sbSql.append("           FROM TT_VS_ORDER_DETAIL C, TT_VS_ORDER D,TM_DEALER E\n");
		sbSql.append("          WHERE C.ORDER_ID = D.ORDER_ID AND D.DEALER_ID=E.DEALER_ID\n");
		sbSql.append("            AND D.ORDER_STATUS = ?\n");
		sbSql.append("            AND D.FUND_TYPE_ID = ?\n");

		if (!"".equals(ORDER_STARTDATE))
		{
			sbSql.append("   AND D.INVO_DATE>=TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')\n");
			params.add(ORDER_STARTDATE + " 00:00:00");
		}
		if (!"".equals(ORDER_ENDDATE))
		{
			sbSql.append("   AND D.INVO_DATE<=TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')\n");
			params.add(ORDER_ENDDATE + " 23:59:59");
		}

		sbSql.append("          GROUP BY C.DETAIL_ID,D.CHK_NUM,D.ORDER_YF_PRICE,D.INVO_DATE,E.DEALER_NAME,D.INVOICE_NO\n");
		sbSql.append("       ) C\n");
		sbSql.append(" WHERE A.BO_DE_ID = B.BO_DE_ID\n");
		sbSql.append("   AND B.OR_DE_ID = C.DETAIL_ID\n");
		sbSql.append("   AND A.VEHICLE_ID = F.VEHICLE_ID\n");
		sbSql.append("   AND B.MAT_ID = H.MATERIAL_ID\n");
		sbSql.append("   AND A.IS_SEND = ?");
		params.add(Constant.IF_TYPE_YES);

		if (!"".equals(dealerCode))
		{
			sbSql.append(Utility.getConSqlByParamForEqual(dealerCode, params, "E", "DEALER_CODE"));
		}

		Object[] arr = new Object[2];
		arr[0] = sbSql;
		arr[1] = params;
		return arr;
	}

	/**
	 * 查询 资金类型
	 * 
	 * @param map
	 * @return 资金类型信息
	 * @throws Exception
	 */
	public List<Map<String, Object>> getFundType() throws Exception {
		List<Object> params = new LinkedList<Object>();
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("SELECT T.CODE_ID, T.CODE_DESC\n");
		sbSql.append("  FROM TC_CODE T\n");
		sbSql.append(" WHERE T.TYPE = ?\n");
		sbSql.append("   AND T.CODE_ID IN (?, ?, ?)\n");
		sbSql.append(" ORDER BY T.CODE_ID");
		params.add(Constant.ACCOUNT_TYPE);
		params.add(Constant.ACCOUNT_TYPE_03);
		/*params.add(Constant.ACCOUNT_TYPE_04);
		params.add(Constant.ACCOUNT_TYPE_06);*/
		List<Map<String, Object>> list = dao.pageQuery(sbSql.toString(), params, getFunName());
		return list;
	}
}
