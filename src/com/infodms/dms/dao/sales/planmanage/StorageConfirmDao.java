package com.infodms.dms.dao.sales.planmanage;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;


public class StorageConfirmDao extends BaseDao<PO>{
	public static Logger logger = Logger.getLogger(QuotaAssignDao.class);
	private static final StorageConfirmDao dao=new StorageConfirmDao();
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
	public static final StorageConfirmDao getInstance() {
		return dao;
	}
	/**
	 * 区域配额确认下发按业务范围分组查询
	 * 
	 * @param map
	 * @param curPage
	 * @param pageSize
	 * @return
	 */
	public PageResult<Map<String, Object>> getStorageConfirmQueryList(
			Map<String, Object> map, int curPage, int pageSize) {

		String companyId = (String) map.get("companyId");
		String areaIds = (String) map.get("areaIds");

		StringBuffer sql = new StringBuffer();

		sql.append("SELECT DISTINCT TBA.AREA_NAME,\n");
		sql.append("                TVQ.AREA_ID,\n");
		sql.append("                TVQ.QUOTA_YEAR,\n");
		sql.append("                TVQ.QUOTA_MONTH,\n");
		sql
				.append("                TVQ.QUOTA_YEAR || '-' || TVQ.QUOTA_MONTH QUOTA_DATE,\n");
		sql.append("                TVQ.QUOTA_TYPE\n");
		sql.append("  FROM TT_VS_QUOTA TVQ, TM_BUSINESS_AREA TBA\n");
		sql.append(" WHERE TVQ.AREA_ID = TBA.AREA_ID\n");
		sql.append("   AND TVQ.COMPANY_ID = " + companyId + "\n");
		sql.append("   AND TVQ.AREA_ID IN (" + areaIds + ")\n");
		sql.append("   AND TVQ.STATUS = " + Constant.QUOTA_STATUS_01 + "\n");
		sql.append("   AND TVQ.QUOTA_TYPE = " + Constant.QUOTA_TYPE_03 + "");

		PageResult<Map<String, Object>> ps = pageQuery(
				sql.toString(),
				null,
				"com.infodms.dms.dao.sales.StorageConfirmDao.getStorageConfirmQueryList",
				pageSize, curPage);
		return ps;
	}
	/**
	 * 查询车厂确认信息
	 * @param map
	 * @param curPage
	 * @param pageSize
	 * @return
	 */
	public PageResult<Map<String, Object>> getStorageQueryList(
			Map<String, Object> map, int curPage, int pageSize) {

		String year = (String) map.get("quotaYear");
		String month = (String) map.get("quotaMonth");
		String areaId = (String) map.get("areaId");
		String companyId = (String) map.get("companyId");

		List<Map<String, Object>> list = getWeekList(year, month, companyId);

		StringBuffer sql = new StringBuffer();

		sql.append("SELECT TOR.ORG_ID,\n");
		sql.append("       TOR.ORG_CODE,\n");
		sql.append("       TOR.ORG_NAME,\n");
		sql.append("       TVMG3.GROUP_ID,\n");
		sql.append("       TVMG3.GROUP_CODE,\n");
		sql.append("       TVMG3.GROUP_NAME,\n");
		sql.append("       SUM(TMP.QUOTA_AMT) QUOTA_AMT,\n");
		for (int i = 0; i < list.size(); i++) {
			if (i != list.size() - 1) {
				sql.append("       SUM(TMP.W" + i + ") W" + i + ",\n");
			} else {
				sql.append("       SUM(TMP.W" + i + ") W" + i + "\n");
			}
		}
		sql.append("  FROM (SELECT TVQ.ORG_ID,\n");
		sql.append("               TVQD.GROUP_ID,\n");
		sql.append("               TVQD.QUOTA_AMT,\n");
		for (int i = 0; i < list.size(); i++) {
			Map<String, Object> mapGet = (Map<String, Object>) list.get(i);
			if (i != list.size() - 1) {
				sql.append("               DECODE(TVQ.QUOTA_WEEK, "
						+ (String) mapGet.get("SET_WEEK")
						+ ", TVQD.QUOTA_AMT, 0) W" + i + ",\n");
			} else {
				sql.append("               DECODE(TVQ.QUOTA_WEEK, "
						+ (String) mapGet.get("SET_WEEK")
						+ ", TVQD.QUOTA_AMT, 0) W" + i + "\n");
			}
		}
		sql.append("          FROM TT_VS_QUOTA TVQ, TT_VS_QUOTA_DETAIL TVQD\n");
		sql.append("         WHERE TVQ.QUOTA_ID = TVQD.QUOTA_ID\n");
		sql.append("           AND TVQ.QUOTA_YEAR = " + year + "\n");
		sql.append("           AND TVQ.QUOTA_MONTH = " + month + "\n");
		sql.append("           AND TVQ.COMPANY_ID = " + companyId + "\n");
		sql.append("           AND TVQ.AREA_ID = " + areaId + "\n");
		sql.append("           AND TVQ.STATUS = " + Constant.QUOTA_STATUS_01
				+ "\n");
		sql.append("           AND TVQ.QUOTA_TYPE = " + Constant.QUOTA_TYPE_03
				+ ") TMP,\n");
		sql.append("       TM_ORG TOR,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP TVMG1,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP TVMG2,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP TVMG3\n");
		sql.append(" WHERE TMP.GROUP_ID = TVMG1.GROUP_ID\n");
		sql.append("   AND TVMG1.PARENT_GROUP_ID = TVMG2.GROUP_ID\n");
		sql.append("   AND TVMG2.PARENT_GROUP_ID = TVMG3.GROUP_ID\n");
		sql.append("   AND TMP.ORG_ID = TOR.ORG_ID\n");
		sql.append(" GROUP BY TOR.ORG_ID,\n");
		sql.append("          TOR.ORG_CODE,\n");
		sql.append("          TOR.ORG_NAME,\n");
		sql.append("          TVMG3.GROUP_ID,\n");
		sql.append("          TVMG3.GROUP_CODE,\n");
		sql.append("          TVMG3.GROUP_NAME");

		PageResult<Map<String, Object>> ps = pageQuery(
				sql.toString(),
				null,
				"com.infodms.dms.dao.sales.QuotaAssignDao.getAreaQuotaIssueQueryList",
				pageSize, curPage);
		return ps;
	}
	/**
	 * 获取车厂确认数量（点击数量连接时执行）
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getStorageSureSerieAmount(
			Map<String, Object> map) {
		String quotaYear = (String) map.get("quotaYear");
		String quotaMonth = (String) map.get("quotaMonth");
		String areaId = (String) map.get("areaId");
		String orgId = (String) map.get("orgId");
		String groupId = (String) map.get("groupId");
		String companyId = (String) map.get("companyId");

		StringBuffer sql = new StringBuffer();

		sql.append("SELECT TOR.ORG_CODE,\n");
		sql.append("       TOR.ORG_NAME,\n");
		sql.append("       TVMG3.GROUP_CODE SERIE_CODE,\n");
		sql.append("       TVMG3.GROUP_NAME SERIE_NAME,\n");
		sql.append("       SUM(TVQD.QUOTA_AMT) QUOTA_AMT\n");
		sql.append("  FROM TT_VS_QUOTA            TVQ,\n");
		sql.append("       TT_VS_QUOTA_DETAIL     TVQD,\n");
		sql.append("       TM_ORG                 TOR,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP TVMG1,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP TVMG2,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP TVMG3\n");
		sql.append(" WHERE TVQ.QUOTA_ID = TVQD.QUOTA_ID\n");
		sql.append("   AND TVQ.ORG_ID = TOR.ORG_ID\n");
		sql.append("   AND TVQD.GROUP_ID = TVMG1.GROUP_ID\n");
		sql.append("   AND TVMG1.PARENT_GROUP_ID = TVMG2.GROUP_ID\n");
		sql.append("   AND TVMG2.PARENT_GROUP_ID = TVMG3.GROUP_ID\n");
		sql.append("   AND TVQ.COMPANY_ID = " + companyId + "\n");
		sql.append("   AND TVQ.QUOTA_YEAR = " + quotaYear + "\n");
		sql.append("   AND TVQ.QUOTA_MONTH = " + quotaMonth + "\n");
		sql.append("   AND TVQ.AREA_ID = " + areaId + "\n");
		sql.append("   AND TVQ.STATUS = " + Constant.QUOTA_STATUS_01 + "\n");
		sql.append("   AND TVQ.QUOTA_TYPE = " + Constant.QUOTA_TYPE_03 + "\n");
		sql.append("   AND TOR.ORG_ID = " + orgId + "\n");
		sql.append("   AND TVMG3.GROUP_ID = " + groupId + "\n");
		sql
				.append(" GROUP BY TOR.ORG_CODE, TOR.ORG_NAME, TVMG3.GROUP_CODE, TVMG3.GROUP_NAME");

		List<Map<String, Object>> results = pageQuery(sql.toString(), null,
				"com.infodms.dms.dao.sales.QuotaAssignDao.getAreaQuotaIssueSerieAmount");
		return results;
	}
	/**
	 * 获取车厂确认明星
	 * @param map
	 * @param curPage
	 * @param pageSizeMax
	 * @return
	 */
	public PageResult<Map<String, Object>> getStorageSureDetailList(
			Map<String, Object> map, Integer curPage, Integer pageSizeMax) {
		String quotaYear = (String) map.get("quotaYear");
		String quotaMonth = (String) map.get("quotaMonth");
		String areaId = (String) map.get("areaId");
		String orgId = (String) map.get("orgId");
		String groupId = (String) map.get("groupId");
		String companyId = (String) map.get("companyId");

		StringBuffer sql = new StringBuffer();

		sql
				.append("SELECT TVQ.QUOTA_YEAR || '-' || TVQ.QUOTA_MONTH || '-' || TVQ.QUOTA_WEEK QUOTA_DATE,\n");
		sql.append("       TVMG1.GROUP_CODE,\n");
		sql.append("       TVMG1.GROUP_NAME,\n");
		sql.append("       TVQD.QUOTA_AMT\n");
		sql.append("  FROM TT_VS_QUOTA            TVQ,\n");
		sql.append("       TT_VS_QUOTA_DETAIL     TVQD,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP TVMG1,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP TVMG2,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP TVMG3\n");
		sql.append(" WHERE TVQ.QUOTA_ID = TVQD.QUOTA_ID\n");
		sql.append("   AND TVQD.GROUP_ID = TVMG1.GROUP_ID\n");
		sql.append("   AND TVMG1.PARENT_GROUP_ID = TVMG2.GROUP_ID\n");
		sql.append("   AND TVMG2.PARENT_GROUP_ID = TVMG3.GROUP_ID\n");
		sql.append("   AND TVQ.COMPANY_ID = " + companyId + "\n");
		sql.append("   AND TVQ.QUOTA_YEAR = " + quotaYear + "\n");
		sql.append("   AND TVQ.QUOTA_MONTH = " + quotaMonth + "\n");
		sql.append("   AND TVQ.AREA_ID = " + areaId + "\n");
		sql.append("   AND TVQ.STATUS = " + Constant.QUOTA_STATUS_01 + "\n");
		sql.append("   AND TVQ.QUOTA_TYPE = " + Constant.QUOTA_TYPE_01 + "\n");
		sql.append("   AND TVQ.ORG_ID = " + orgId + "\n");
		sql.append("   AND TVMG3.GROUP_ID = " + groupId + "");

		PageResult<Map<String, Object>> ps = pageQuery(
				sql.toString(),
				null,
				"com.infodms.dms.dao.sales.QuotaAssignDao.getAreaQuotaIssueDetailList",
				pageSizeMax, curPage);
		return ps;
	}
	/**
	 * 根据年、月获得所有周度列表
	 * 
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getWeekList(String year, String month,
			String companyId) {

		StringBuffer sql = new StringBuffer();

		sql.append("SELECT DISTINCT T.SET_YEAR, T.SET_MONTH, T.SET_WEEK\n");
		sql.append("  FROM TM_DATE_SET T\n");
		sql.append(" WHERE T.SET_YEAR = " + year + "\n");
		sql.append("   AND T.SET_MONTH = " + month + "\n");
		sql.append("   AND T.COMPANY_ID = " + companyId + "\n");
		sql.append(" ORDER BY T.SET_WEEK ASC");

		List<Map<String, Object>> results = pageQuery(sql.toString(), null,
				"com.infodms.dms.dao.sales.QuotaAssignDao.getWeekList");
		return results;
	}
}
