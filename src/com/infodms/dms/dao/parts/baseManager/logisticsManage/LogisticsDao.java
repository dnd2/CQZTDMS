package com.infodms.dms.dao.parts.baseManager.logisticsManage;

import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.infodms.dms.common.Constant;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TtSalesLogiAreaPO;
import com.infodms.dms.po.TtSalesLogiPO;
import com.infoservice.infox.util.StringUtil;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

/**
 * <p>ClassName: LogisticsDao</p>
 * <p>Description: 物流商管理DAO</p>
 * <p>Author: MEpaper</p>
 * <p>Date: 2017年8月17日</p>
 */
public class LogisticsDao extends BaseDao<PO>  {

	private static final LogisticsDao dao = new LogisticsDao();

	public static final LogisticsDao getInstance() {
		return dao;
	}
	private LogisticsDao() {}

	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		return null;
	}
	
	/**
	 * 物流商信息查询
	 * 
	 * @param map
	 * @param curPage
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	public PageResult<Map<String, Object>> getLogisticsQuery(
			Map<String, Object> map, int curPage, int pageSize)
			throws Exception {

		String logiCode = (String) map.get("LOGI_CODE"); // 物流商代码
		String logiFullName = (String) map.get("LOGI_FULL_NAME");// 物流商名称
		String yieldly = (String) map.get("YIELDLY"); // 产地
		String status = (String) map.get("STATUS");// 状态
		String conPer = (String) map.get("CON_PER");// 联系人
		String poseId = (String) map.get("poseId");
		List<Object> params = new LinkedList<Object>();

		StringBuffer sql = new StringBuffer();
		sql.append("select TSL.LOGI_ID,TBA.AREA_NAME YIELDLY,TSL.LOGI_CODE,TSL.LOGI_NAME,\n");
		sql.append("TSL.LOGI_FULL_NAME,TSL.CON_PER,TSL.CON_TEL,TSL.STATUS,\n");
		sql.append("TSL.ADDRESS from TT_SALES_LOGI  TSL,TM_BUSINESS_AREA TBA WHERE TSL.YIELDLY=TBA.AREA_ID\n");
		sql.append(MaterialGroupManagerDao.getPoseIdBusinessSql("TSL.YIELDLY",
				poseId));// 车厂端查询列表产地数据权限
		if (logiCode != null && !"".equals(logiCode)) {
			sql.append("   AND TSL.LOGI_CODE  LIKE '%" + logiCode + "%'\n");
		}
		if (logiFullName != null && !"".equals(logiFullName)) {
			sql.append("   AND TSL.LOGI_FULL_NAME  LIKE '%" + logiFullName
					+ "%'\n");
		}
		if (yieldly != null && !"".equals(yieldly)) {
			sql.append("   AND TSL.YIELDLY = " + yieldly + "\n");
		}
		if (status != null && !"".equals(status)) {
			sql.append("   AND TSL.STATUS = " + status + "\n");
		}
		if (conPer != null && !"".equals(conPer)) {
			sql.append("   AND TSL.CON_PER LIKE '%" + conPer + "%'\n");
		}
		// sql.append(" ORDER BY TSM.MIL_ID DESC");
		PageResult<Map<String, Object>> ps = dao.pageQuery(sql.toString(),
				params, getFunName(), pageSize, curPage);
		return ps;
	}

	/**
	 * 物流商信息添加
	 * @param ttSalesLogiPO
	 */
	public void logisticsAdd(TtSalesLogiPO ttSalesLogiPO) {
		dao.insert(ttSalesLogiPO);
	}
	
	/**
	 * 物流商信息信息修改
	 * @param seachPO
	 * @param ttSalesLogiPO
	 */
	public void logisticsUpdate(TtSalesLogiPO seachPO,
			TtSalesLogiPO ttSalesLogiPO) {
		dao.update(seachPO, ttSalesLogiPO);
	}
	
	/**
	 * 物流商信息查询
	 * 
	 * @param map
	 * @param curPage
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	public PageResult<Map<String, Object>> dealerQuery(Map<String, Object> map,
			int curPage, int pageSize) throws Exception {

		String logiId = (String) map.get("logiId");
		String dealerCode = (String) map.get("dealerCode");
		String dealerName = (String) map.get("dealerName");
		List<Object> params = new LinkedList<Object>();

		StringBuffer sql = new StringBuffer();
		sql.append("select *\n");
		sql.append("from TM_DEALER TMD\n");
		sql.append("WHERE TMD.DEALER_TYPE=10771001\n");
		sql.append("AND not exists (SELECT 1 FROM TT_SALES_LOGI_DEALER_RELATION TTS WHERE TTS.DEALER_ID = TMD.DEALER_ID and tts.status=10011001 and TTS.LOGI_ID=");
		sql.append(logiId).append(")\n");
		if (!StringUtil.isEmpty(dealerCode)) {
			sql.append("AND TMD.DEALER_CODE LIKE'%");
			sql.append(dealerCode).append("%'\n");
		}
		if (!StringUtil.isEmpty(dealerName)) {
			sql.append("AND TMD.DEALER_NAME LIKE'%");
			sql.append(dealerName).append("%'\n");
		}
		PageResult<Map<String, Object>> ps = dao.pageQuery(sql.toString(),
				params, getFunName(), pageSize, curPage);
		return ps;
	}

	public PageResult<Map<String, Object>> queryRelation(
			Map<String, Object> map, int curPage, int pageSize)
			throws Exception {

		String logiId = (String) map.get("logiId");
		String dealerCode = (String) map.get("dealerCode");
		String dealerName = (String) map.get("dealerName");
		List<Object> params = new LinkedList<Object>();

		StringBuffer sql = new StringBuffer();
		sql.append("select TTS.ID,TMD.DEALER_CODE,TMD.DEALER_NAME,TMD.DEALER_SHORTNAME\n");
		sql.append("from TT_SALES_LOGI_DEALER_RELATION TTS,TM_DEALER TMD\n");
		sql.append("WHERE TTS.DEALER_ID = TMD.DEALER_ID\n");
		if (!StringUtil.isEmpty(dealerCode)) {
			sql.append("AND TMD.DEALER_CODE LIKE'%");
			sql.append(dealerCode).append("%'\n");
		}
		if (!StringUtil.isEmpty(dealerName)) {
			sql.append("AND TMD.DEALER_NAME LIKE'%");
			sql.append(dealerName).append("%'\n");
		}
		sql.append("and TTS.LOGI_ID=\n");
		sql.append(logiId).append("\n");
		sql.append("and TTS.STATUS=10011001\n");
		PageResult<Map<String, Object>> ps = dao.pageQuery(sql.toString(),
				params, getFunName(), pageSize, curPage);
		return ps;
	}

	/**
	 * Function : 根据里程ID查询里程信息
	 * 
	 * @param : 里程IDS
	 */
	public List<Map<String, Object>> getDisByDisIDS(String disIds, String logiId) {
		List<Map<String, Object>> list = null;
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("select TSCD.DIS_ID,\r\n");
		sbSql.append("       TR.REGION_NAME PROVINCE_NAME,\r\n");
		sbSql.append("       TR1.REGION_NAME CITY_NAME\r\n");
		sbSql.append("from TT_SALES_LOGI_AREA TLA,\r\n");
		sbSql.append("     TT_SALES_CITY_DIS TSCD,\r\n");
		sbSql.append("     TM_REGION TR,TM_REGION TR1\r\n");
		sbSql.append("WHERE TLA.DIS_ID=TSCD.DIS_ID\r\n");
		sbSql.append("AND TSCD.PROVINCE_ID=TR.REGION_ID\r\n");
		sbSql.append("AND TSCD.CITY_ID=TR1.REGION_ID\r\n");
		sbSql.append("AND TLA.LOGI_ID!=").append(logiId).append("\r\n");
		if (null != disIds && !"".equals(disIds)) {
			String[] array = disIds.split(",");
			int fu = array.length / 500;
			sbSql.append("   AND (TLA.DIS_ID IN(-1)");
			for (int kk = 0; kk <= fu; kk++) {
				sbSql.append("  OR TLA.DIS_ID IN(");
				for (int s = kk * 500; s < array.length; s++) {
					if (s == (kk + 1) * 500) {
						break;
					}
					sbSql.append(array[s] + ",");
				}
				sbSql.append("-1)");
			}
			sbSql.append(")");
		}
		list = dao.pageQuery(sbSql.toString(), null, dao.getFunName());
		return list;
	}
	
	/**
	 * 物流商与区域信息添加
	 * @param ttSalesLogiAreaPO
	 */
	public void logisticsAreaAdd(TtSalesLogiAreaPO ttSalesLogiAreaPO) {
		dao.insert(ttSalesLogiAreaPO);
	}
	/**
	 * 根据物流商ID得到物流商信息
	 * @param id
	 * @return
	 */
	public Map<String, Object> getSalesLogiMsg(String id) {
		StringBuffer sql = new StringBuffer();
		sql.append("select TSL.LOGI_ID,TBA.AREA_NAME YIELDLY,TBA.AREA_ID,TSL.LOGI_CODE,TSL.LOGI_NAME,\n");
		sql.append("TSL.LOGI_FULL_NAME,TSL.CON_PER,TSL.CON_TEL,TSL.STATUS,TSL.CORPORATION,TSL.REMARK,\n");
		sql.append("TSL.ADDRESS from TT_SALES_LOGI  TSL,TM_BUSINESS_AREA TBA WHERE TSL.YIELDLY=TBA.AREA_ID AND TSL.LOGI_ID=\n");
		sql.append(id);
		Map<String, Object> map = pageQueryMap(sql.toString(), null,
				getFunName());
		return map;
	}
	
	/**
	 * 根据物流商ID得到物流商与区域关联信息
	 * @param po
	 * @return
	 */
	public List<TtSalesLogiAreaPO> getLogisticsMata(TtSalesLogiAreaPO po) {
		return dao.factory.select(po);
	}
	
	/**
	 * 物流商与区域信息删除
	 * @param ttSalesLogiAreaPO
	 */
	public void logisticsAreaDelete(TtSalesLogiAreaPO ttSalesLogiAreaPO) {
		dao.delete(ttSalesLogiAreaPO);
	}
	
	/**
	 * 查询物流商（用于运输方式维护下拉列表）
	 * @return
	 * @throws Exception
	 */
	 public List<Map<String, Object>> selLogistics() throws Exception {
        try {
            StringBuffer sql = new StringBuffer("");
            sql.append("SELECT LOGI_CODE,LOGI_FULL_NAME FROM TT_SALES_LOGI WHERE STATUS=10011001  GROUP BY LOGI_CODE,LOGI_FULL_NAME ORDER BY 1 ");

            return pageQuery(sql.toString(), null, getFunName());
        } catch (Exception e) {
            throw e;
        }
    }
	
}
