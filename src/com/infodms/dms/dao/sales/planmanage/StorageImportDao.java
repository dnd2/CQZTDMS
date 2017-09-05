package com.infodms.dms.dao.sales.planmanage;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.dao.sales.ordermanage.orderreport.OrderReportDao;
import com.infodms.dms.po.TmBusinessParaPO;
import com.infodms.dms.po.TmDateSetPO;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

public class StorageImportDao extends BaseDao<PO> {
	
	public static Logger logger = Logger.getLogger(QuotaAssignDao.class);
	
	private static final StorageImportDao dao=new StorageImportDao();
	private static final OrderReportDao reportDao = new OrderReportDao();
	private static final QuotaAssignDao quotaAssignDao=QuotaAssignDao.getInstance();
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		return null;
	}
	public static final StorageImportDao getInstance() {
		return dao;
	}
	/**
	 * 获得车厂配额计算日期列表
	 * 
	 * @param paraId
	 * @return
	 */
	public List<Map<String, Object>> getStorageDateList(Long companyId) {
		TmDateSetPO dateSet = reportDao.getTmDateSetPO(new Date(), companyId);
		int year = Integer.parseInt(dateSet.getSetYear());
		int month = Integer.parseInt(dateSet.getSetMonth());

		TmBusinessParaPO para = reportDao.getTmBusinessParaPO(
				Constant.QUTOA_AHEAD_ISSUE_MONTH_PARA, companyId);
		int count = Integer.parseInt(para.getParaValue());
		month = month + count;
		if (month > 12) {
			month = month - 12;
			year++;
		}

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("code", year + "-" + month);
		map.put("name", year + "年" + month + "月");
		list.add(map);

		return list;
	}
	public List<Map<String, Object>> getTempDownWeek(Map<String, Object> map) {
		String companyId = map.get("companyId").toString() ;
		String year = map.get("year").toString() ;
		String month = map.get("month").toString() ;
		StringBuffer sql = new StringBuffer() ;
		sql.append("SELECT TEMP.SET_WEEK FROM (SELECT DISTINCT TO_NUMBER(TDS.SET_WEEK) SET_WEEK FROM TM_DATE_SET TDS WHERE TDS.SET_YEAR='" + year + "' AND TDS.SET_MONTH='" + month + "' AND TDS.COMPANY_ID='" + companyId + "') TEMP ORDER BY TEMP.SET_WEEK");
		List<Map<String, Object>> list = dao.pageQuery(sql.toString(), null,
				getFunName());
		return list;
	}
	/**
	 * 区域配额删除
	 * 
	 * @param map
	 * @return
	 */
	public int deleteOrgQuota(Map<String, Object> map) {
		String year = (String) map.get("year");
		String month = (String) map.get("month");
		String areaId = (String) map.get("areaId");
		String userId = (String) map.get("userId") ;

		StringBuffer sql = new StringBuffer();

		// 删除配额明细
		sql.append("DELETE FROM TT_VS_QUOTA_DETAIL TVQD\n");
		sql.append(" WHERE TVQD.QUOTA_ID IN (SELECT TVQ.QUOTA_ID\n");
		sql.append("                           FROM TT_VS_QUOTA TVQ\n");
		sql.append("                          WHERE TVQ.QUOTA_YEAR = " + year + "\n");
		sql.append("                            AND TVQ.QUOTA_MONTH = " + month + "\n");
		sql.append("                            AND TVQ.AREA_ID = " + areaId + "\n");
		sql.append("                            AND TVQ.QUOTA_TYPE = " + Constant.QUOTA_TYPE_03 + "\n");
		sql.append("                            AND TVQ.STATUS = " + Constant.QUOTA_STATUS_01 + "\n");
		//sql.append("                            AND TVQ.CREATE_BY = " + userId + "\n");
		sql.append("                            AND TVQ.DEALER_ID IS NULL)");

		delete(sql.toString(), null);

		sql = new StringBuffer();

		sql.append("DELETE FROM TT_VS_QUOTA TVQ\n");
		sql.append(" WHERE TVQ.QUOTA_YEAR = " + year + "\n");
		sql.append("   AND TVQ.QUOTA_MONTH = " + month + "\n");
		sql.append("   AND TVQ.AREA_ID = " + areaId + "\n");
		sql.append("   AND TVQ.QUOTA_TYPE = " + Constant.QUOTA_TYPE_03 + "\n");
		sql.append("   AND TVQ.STATUS = " + Constant.QUOTA_STATUS_01 + "\n");
		//sql.append("   AND TVQ.CREATE_BY = " + userId + "\n");
		sql.append("   AND TVQ.DEALER_ID IS NULL");

		return delete(sql.toString(), null);
	}
	/**
	 * 查询临时表中周次是否在工作日历中存在
	 * 
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> checkDateSetWeek(Map<String, Object> map) {
		String userId = (String) map.get("userId");
		String companyId = (String) map.get("companyId");

		List<Object> params = new LinkedList<Object>();
		StringBuffer sql = new StringBuffer("");

		sql.append("select p.ROW_NUMBER\n");
		sql.append("  from TMP_VS_QUOTA p\n");
		sql.append(" where 1 = 1\n");
		sql.append("   and p.USER_ID = " + userId + "\n");
		sql.append("   and not exists (select 1\n");
		sql.append("          from TM_DATE_SET tds\n");
		sql.append("         where tds.SET_YEAR = p.QUOTA_YEAR\n");
		sql.append("           and tds.SET_MONTH = p.QUOTA_MONTH\n");
		sql.append("           and tds.SET_WEEK = p.QUOTA_WEEK\n");
		sql.append("           and tds.COMPANY_ID = " + companyId + ")\n");
		sql.append(" order by TO_NUMBER(p.ROW_NUMBER) asc");

		List<Map<String, Object>> list = dao.pageQuery(sql.toString(), params,
				getFunName());

		return list;
	}
	/**
	 * 查询临时表中是否有相同周导入相同的配置
	 * 
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> talbeCheckDump(Map<String, Object> map) {
		String userId = (String) map.get("userId");
		String quotaType = (String) map.get("quotaType");
		List<Object> params = new LinkedList<Object>();
		StringBuffer sql = new StringBuffer("");

		sql
				.append("select p1.ROW_NUMBER ROW_NUMBER1, p2.ROW_NUMBER ROW_NUMBER2\n");
		sql.append("  from TMP_VS_QUOTA p1, TMP_VS_QUOTA p2\n");
		sql.append(" where \n");
		sql.append("   p1.GROUP_CODE = p2.GROUP_CODE\n");
		sql.append("   and p1.ROW_NUMBER <> p2.ROW_NUMBER\n");
		sql.append("   and p1.QUOTA_YEAR = p2.QUOTA_YEAR\n");
		sql.append("   and p1.QUOTA_MONTH = p2.QUOTA_MONTH\n");
		sql.append("   and p1.QUOTA_WEEK = p2.QUOTA_WEEK\n");
//		if (quotaType.equals(Constant.QUOTA_TYPE_01.toString())) {
//			sql.append("   and p1.ORG_CODE = p2.ORG_CODE\n");
//		} else {
//			sql.append("   and p1.DEALER_CODE = p2.DEALER_CODE\n");
//		}
		sql.append("   and p1.USER_ID = p2.USER_ID\n");
		sql.append("   and p1.USER_ID= ? \n");
		params.add(userId);
		sql.append("   order by TO_NUMBER(p1.ROW_NUMBER)\n");

		List<Map<String, Object>> list = dao.pageQuery(sql.toString(), params,
				getFunName());

		return list;
	}
	/**
	 * 配额导入校验车系是否与业务范围一致
	 * 
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> storageImportCheckGroupArea(
			Map<String, Object> map) {
		String userId = (String) map.get("userId");
		String groupArea = (String) map.get("groupArea");
		List<Object> params = new LinkedList<Object>();
		StringBuffer sql = new StringBuffer("");

		sql.append("select p.ROW_NUMBER\n");
		sql.append("  from TMP_VS_QUOTA p\n");
		sql.append(" where 1 = 1\n");
		sql.append("   and p.USER_ID = " + userId + "\n");
		sql.append("   and p.GROUP_CODE not in("
				+ groupArea + ")\n");
		sql.append(" order by TO_NUMBER(p.ROW_NUMBER) asc");

		return dao.pageQuery(sql.toString(), params, getFunName());
	}
	/**
	 * 导入完成数据展示
	 * @param map
	 * @param curPage
	 * @param pageSize
	 * @return
	 */
	public PageResult<Map<String, Object>> getStorageImportTempList(
			Map<String, Object> map, int curPage, int pageSize,String orgCode) {

		String year = (String) map.get("year");
		String month = (String) map.get("month");
		String userId = (String) map.get("userId");
		String companyId = (String) map.get("companyId");

		List<Map<String, Object>> list = quotaAssignDao.getWeekList(year, month, companyId);

		StringBuffer sql = new StringBuffer();

		sql.append("SELECT TMP.ORG_CODE,\n");
		sql.append("       TMP.ORG_NAME,\n");
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
		sql.append("  FROM (SELECT TVQ.ORG_CODE,\n");
		sql.append("               TVQ.ORG_NAME,\n");
		sql.append("               TVQ.GROUP_CODE,\n");
		sql.append("               TVQ.GROUP_NAME,\n");
		sql.append("               TVQ.QUOTA_AMT,\n");
		for (int i = 0; i < list.size(); i++) {
			Map<String, Object> mapGet = (Map<String, Object>) list.get(i);
			if (i != list.size() - 1) {
				sql.append("               DECODE(TVQ.QUOTA_WEEK, "
						+ (String) mapGet.get("SET_WEEK")
						+ ", TVQ.QUOTA_AMT, 0) W" + i + ",\n");
			} else {
				sql.append("               DECODE(TVQ.QUOTA_WEEK, "
						+ (String) mapGet.get("SET_WEEK")
						+ ", TVQ.QUOTA_AMT, 0) W" + i + "\n");
			}
		}
		sql.append("          FROM TMP_VS_QUOTA TVQ\n");
		sql.append("         WHERE TVQ.USER_ID = " + userId + "\n");
		if(orgCode!=null){
			sql.append("           AND TVQ.org_CODE = '"+orgCode+"'\n");
		}
		sql.append("           AND TVQ.DEALER_CODE IS NULL) TMP,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP TVMG1,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP TVMG2,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP TVMG3\n");
		sql.append(" WHERE TMP.GROUP_CODE = TVMG1.GROUP_CODE\n");
		sql.append("   AND TVMG1.PARENT_GROUP_ID = TVMG2.GROUP_ID\n");
		sql.append("   AND TVMG2.PARENT_GROUP_ID = TVMG3.GROUP_ID\n");
		sql
				.append(" GROUP BY TMP.ORG_CODE, TMP.ORG_NAME, TVMG3.GROUP_CODE, TVMG3.GROUP_NAME");

		PageResult<Map<String, Object>> ps = pageQuery(
				sql.toString(),
				null,
				"com.infodms.dms.dao.sales.StorageImportDao.getStorageImportTempList",
				pageSize, curPage);
		return ps;
	}
	/**
	 * 车厂导入主表
	 * @param map
	 * @return
	 */
	public int insertAreaQuota(Map<String, Object> map) {
		String companyId = (String) map.get("companyId");
		String userId = (String) map.get("userId");

		StringBuffer sql = new StringBuffer();

		sql.append("MERGE INTO TT_VS_QUOTA A\n");
		sql.append("USING (SELECT TVQ.AREA_ID,\n");
		sql.append("              TVQ.QUOTA_YEAR,\n");
		sql.append("              TVQ.QUOTA_MONTH,\n");
		sql.append("              TVQ.QUOTA_WEEK,\n");
		sql.append("              TOR.ORG_ID\n");
		sql.append("         FROM TMP_VS_QUOTA TVQ, TM_ORG TOR\n");
		sql.append("        WHERE TVQ.ORG_CODE = TOR.ORG_CODE\n");
		sql.append("          AND TVQ.USER_ID = " + userId + "\n");
		sql.append("        GROUP BY TVQ.AREA_ID,\n");
		sql.append("                 TVQ.QUOTA_YEAR,\n");
		sql.append("                 TVQ.QUOTA_MONTH,\n");
		sql.append("                 TVQ.QUOTA_WEEK,\n");
		sql.append("                 TOR.ORG_ID) C\n");
		sql
				.append("ON (A.AREA_ID = C.AREA_ID AND A.QUOTA_YEAR = C.QUOTA_YEAR AND A.QUOTA_MONTH = C.QUOTA_MONTH AND A.QUOTA_WEEK = C.QUOTA_WEEK AND A.ORG_ID = C.ORG_ID)\n");
		sql.append("WHEN  MATCHED THEN UPDATE SET A.CREATE_DATE=A.CREATE_DATE\n");
		sql.append("WHEN NOT MATCHED THEN\n");
		sql.append("  INSERT\n");
		sql.append("    (QUOTA_ID,\n");
		sql.append("     COMPANY_ID,\n");
		sql.append("     AREA_ID,\n");
		sql.append("     QUOTA_TYPE,\n");
		sql.append("     QUOTA_YEAR,\n");
		sql.append("     QUOTA_MONTH,\n");
		sql.append("     QUOTA_WEEK,\n");
		sql.append("     ORG_ID,\n");
		sql.append("     STATUS,\n");
		sql.append("     CREATE_BY,\n");
		sql.append("     CREATE_DATE)\n");
		sql.append("  VALUES\n");
		sql.append("    (F_GETID(),\n");
		sql.append("     " + companyId + ",\n");
		sql.append("     C.AREA_ID,\n");
		sql.append("     " + Constant.QUOTA_TYPE_03 + ",\n");
		sql.append("     C.QUOTA_YEAR,\n");
		sql.append("     C.QUOTA_MONTH,\n");
		sql.append("     C.QUOTA_WEEK,\n");
		sql.append("     C.ORG_ID,\n");
		sql.append("     " + Constant.QUOTA_STATUS_01 + ",\n");
		sql.append("     " + userId + ",\n");
		sql.append("     SYSDATE)");


		return update(sql.toString(), null);
	}
	/**
	 * 车厂导入临时表
	 * @param map
	 * @return
	 */
	public int insertAreaQuotaDetail(Map<String, Object> map) {
		String userId = (String) map.get("userId");

		StringBuffer sql = new StringBuffer();

		// 保存TT_VS_QUOTA_DETAIL
		sql.append("MERGE INTO TT_VS_QUOTA_DETAIL A\n");
		sql.append("USING (SELECT TVQ.QUOTA_ID   QUOTA_ID,\n");
		sql.append("              TVMG.GROUP_ID  GROUP_ID,\n");
		sql.append("              TPVP.QUOTA_AMT QUOTA_AMT\n");
		sql.append("         FROM TT_VS_QUOTA            TVQ,\n");
		sql.append("              TMP_VS_QUOTA           TPVP,\n");
		sql.append("              TM_ORG                 TOR,\n");
		sql.append("              TM_VHCL_MATERIAL_GROUP TVMG\n");
		sql.append("        WHERE TVQ.AREA_ID = TPVP.AREA_ID\n");
		sql.append("          AND TVQ.ORG_ID = TOR.ORG_ID\n");
		sql.append("          AND TOR.ORG_CODE = TPVP.ORG_CODE\n");
		sql.append("          AND TVMG.GROUP_CODE = TPVP.GROUP_CODE\n");
		sql.append("          AND TVQ.QUOTA_YEAR = TPVP.QUOTA_YEAR\n");
		sql.append("          AND TVQ.QUOTA_MONTH = TPVP.QUOTA_MONTH\n");
		sql.append("          AND TVQ.QUOTA_WEEK = TPVP.QUOTA_WEEK\n");
		sql.append("          AND TPVP.USER_ID = " + userId + ") C\n");
		sql
				.append("ON (A.QUOTA_ID = C.QUOTA_ID AND A.GROUP_ID = C.GROUP_ID)\n");
		sql.append("WHEN MATCHED THEN\n");
		sql.append("  UPDATE\n");
		sql.append("     SET QUOTA_AMT   = C.QUOTA_AMT,\n");
		sql.append("         UPDATE_DATE = SYSDATE,\n");
		sql.append("         UPDATE_BY   = " + userId + "\n");
		sql.append("WHEN NOT MATCHED THEN\n");
		sql.append("  INSERT\n");
		sql
				.append("    (DETAIL_ID, QUOTA_ID, GROUP_ID, QUOTA_AMT, CREATE_DATE, CREATE_BY)\n");
		sql.append("  VALUES\n");
		sql
				.append("    (F_GETID(), C.QUOTA_ID, C.GROUP_ID, C.QUOTA_AMT, SYSDATE, "
						+ userId + ")");

		return update(sql.toString(), null);
	}

}
