package com.infodms.dms.dao.sales.ordermanage.carSubmission;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.ResultSet;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import jxl.Workbook;
import jxl.format.Alignment;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TtVsOrderPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.XHBUtil;
import com.infoservice.po3.POFactory;
import com.infoservice.po3.POFactoryBuilder;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

/**
 * <ul>
 * <li>文件名称: CarSubmissionQueryDao.java</li>
 * <li>文件描述:</li>
 * <li>版权所有: 版权所有(C)2012-2013</li>
 * <li>内容摘要:</li>
 * <li>完成日期: 2013-5-8 上午9:35:17</li>
 * <li>修改记录:</li>
 * </ul>
 * 
 * @version 1.0
 * @author wangsongwei
 */
public class CarSubmissionQueryDao extends BaseDao{

	private static final CarSubmissionQueryDao dao = new CarSubmissionQueryDao();

	private static POFactory factory = POFactoryBuilder.getInstance();

	public static final CarSubmissionQueryDao getInstance() {
		return dao;
	}

	/**
	 * 方法描述 ： 创建下载模板<br/>
	 * 
	 * @param content
	 * @param os
	 * @throws ParseException
	 * @author wangsongwei
	 */
	public void createXlsFile(List<List<Object>> content, OutputStream os, String sheetName) throws ParseException {
		try
		{
			WritableWorkbook workbook = Workbook.createWorkbook(os);
			WritableSheet sheet = workbook.createSheet(sheetName, 0);
			WritableFont font = new WritableFont(WritableFont.ARIAL, 10, WritableFont.NO_BOLD);
			WritableCellFormat wcf = new WritableCellFormat(font);
			wcf.setAlignment(Alignment.CENTRE);
			for (int i = 0; i < content.size(); i++)
			{
				for (int j = 0; j < content.get(i).size(); j++)
				{
					// 添加单元格
					sheet.addCell(new Label(j, i, content.get(i).get(j) != null ? content.get(i).get(j).toString() : "", wcf));
					if (j > 3)
					{
						sheet.setColumnView(j, 15);
					}
				}

			}
			workbook.write();
			workbook.close();
		}
		catch (RowsExceededException e)
		{
			e.printStackTrace();
		}
		catch (WriteException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * 方法描述 ： 获取经销商的账户信息<br/>
	 * 
	 * @param dealerId
	 *            经销商ID
	 * @param yieldlyId
	 *            产地ID
	 * @param finType
	 *            资金账户类型ID
	 * @return TT_SALES_FIN_ACC
	 * @author wangsongwei
	 */
	public Map<String, Object> getDealerAccountInfo(String dealerId, String yieldlyId, String finType) throws Exception {

		StringBuffer sbSql = new StringBuffer();

		sbSql.append("-- 查询经销商的账户信息								\n");
		sbSql.append("SELECT NVL(ACC.VER,0) VER, NVL(ACC.AMOUNT,0) AMOUNT, ACC.ACC_ID,ACC.DEALER_ID,ACC.YIELDLY,ACC.FIN_TYPE,NVL(ACC.FREEZE_AMOUNT,0) FREEZE_AMOUNT,NVL(ACC.AMOUNT,0) USEFUL_AMOUNT		\n");
		sbSql.append("FROM TT_SALES_FIN_ACC ACC							\n");
		sbSql.append("WHERE ACC.DEALER_ID =	" + dealerId + "			\n");
		sbSql.append("      AND ACC.YIELDLY = " + yieldlyId + " 		\n");
		sbSql.append("      AND ACC.FIN_TYPE = " + finType + " 			\n");

		return dao.pageQueryMap(sbSql.toString(), null, getFunName());
	}

	/**
	 * 方法描述 ：获取经销商对应产地的承诺 <br/>
	 * 
	 * @param yieldly
	 * @param dealerId
	 * @param series
	 * @return
	 * @author wangsongwei
	 */
	public List<Map<String, Object>> getDealerPromise(String yieldly, String dealerId, String series, String fundType) throws Exception {

		StringBuffer sbSql = new StringBuffer();

		sbSql.append("-- 承诺表\n");
		sbSql.append("WITH TVO AS (\n");
		sbSql.append("     SELECT TSPD.PRO_ID,TSPD.DETAIL_ID,SUM(NVL(TPOU.USE_NUM,0)) USE_NUM FROM TT_SALES_PROMISE_DET TSPD, TT_PROM_ORD_USE TPOU\n");
		sbSql.append("     WHERE TSPD.DETAIL_ID = TPOU.DETAIL_ID(+)\n");
		sbSql.append("     GROUP BY TSPD.PRO_ID,TSPD.DETAIL_ID\n");
		sbSql.append(")\n");
		sbSql.append("SELECT TSPD.DETAIL_ID,\n");
		sbSql.append("       (NVL(TSPD.APPLY_NUM, 0) - NVL(TVO.USE_NUM,0)) USE_NUM\n");
		sbSql.append("  FROM TT_SALES_PROMISE TSP, TVO, TT_SALES_PROMISE_DET TSPD");
		sbSql.append("\n");
		sbSql.append("WHERE\n");
		sbSql.append("       TSP.STATUS = ?\n");
		sbSql.append("       AND TSP.PRO_ID = TSPD.PRO_ID\n");
		sbSql.append("       AND TVO.PRO_ID = TSP.PRO_ID\n");
		sbSql.append("       AND TSPD.DETAIL_ID = TVO.DETAIL_ID(+)\n");
		sbSql.append("       AND TSP.DEALER_ID = ?\n");
		sbSql.append("       AND TSP.YIELDLY = ?\n");
		sbSql.append("       AND TSPD.GROUP_ID = ?");
		sbSql.append("		 AND TSP.PRO_TYPE = ?");
		sbSql.append("		 AND TO_CHAR(TSP.CHK_DATE, 'YYYY-MM') = TO_CHAR(SYSDATE, 'YYYY-MM')\n");
		sbSql.append("		 AND TO_CHAR(TSP.PRO_DATE,'YYYY-MM-DD') >= TO_CHAR(SYSDATE,'YYYY-MM-DD')");

		List<Object> params = new ArrayList<Object>();
		params.add(Constant.PROMISE_STATUS_07);
		params.add(dealerId);
		params.add(yieldly);
		params.add(series);
		params.add(fundType);

		return dao.pageQuery(sbSql.toString(), params, getFunName());
	}

	/**
	 * 方法描述 ： 查询物料价格<br/>
	 * 
	 * @author wangsongwei
	 */
	public PageResult<Map<String, Object>> getMaterialPriceList(Map<String, Object> infoMap, Integer curPage, Integer pageSize) throws Exception {

		String materialCode = infoMap.get("materialCode").toString();
		String materialGroup = infoMap.get("materialGroup").toString();
		String dealerId = infoMap.get("dealerId").toString();

		StringBuffer sbSql = new StringBuffer();

		sbSql.append("-- 经销商价格查询 OEM\n");
		sbSql.append("SELECT\n");
		sbSql.append("       TMP2.SERIES_NAME,\n");
		sbSql.append("       TMP2.MODEL_NAME,\n");
		sbSql.append("       TVM.MATERIAL_ID,\n");
		sbSql.append("       TVM.MATERIAL_CODE,\n");
		sbSql.append("       TVM.MATERIAL_NAME,\n");
		sbSql.append("       NVL(TVM.VHCL_PRICE,0) VHCL_PRICE,\n");
		sbSql.append("       TMP2.PACKAGE_ID\n");
		sbSql.append("FROM\n");
		sbSql.append("       TM_VHCL_MATERIAL TVM,\n");
		sbSql.append("       TM_VHCL_MATERIAL_GROUP TVMG,\n");
		sbSql.append("       TM_VHCL_MATERIAL_GROUP_R TVMGR,\n");

		if (!dealerId.equals(""))
		{
			sbSql.append("       (\n");
			sbSql.append("                 SELECT   TR.REGION_ID,\n");
			sbSql.append("                          TR.REGION_NAME,\n");
			sbSql.append("                          TD.DEALER_ID,\n");
			sbSql.append("                          TD.PROVINCE_ID,\n");
			sbSql.append("                          TVM.MATERIAL_ID,\n");
			sbSql.append("                          NVL(TSAP.AMOUNT, 0) PRO_PRICE\n");
			sbSql.append("                 FROM\n");
			sbSql.append("                          TM_VHCL_MATERIAL              TVM,\n");
			sbSql.append("                          TM_REGION                     TR,\n");
			sbSql.append("                          TT_SALES_AREA_PRICE           TSAP,\n");
			sbSql.append("                          TM_DEALER                     TD\n");
			sbSql.append("                 WHERE\n");
			sbSql.append("                          TR.REGION_CODE = TSAP.AREA_ID(+)\n");
			sbSql.append("                          AND TR.REGION_TYPE = " + Constant.REGION_TYPE_02 + "\n");
			sbSql.append("                          AND TD.PROVINCE_ID = TR.REGION_CODE\n");
			sbSql.append("                 GROUP BY\n");
			sbSql.append("                          TR.REGION_ID,\n");
			sbSql.append("                          TR.REGION_NAME,\n");
			sbSql.append("                          TD.DEALER_ID,\n");
			sbSql.append("                          TD.PROVINCE_ID,\n");
			sbSql.append("                          TVM.MATERIAL_ID,\n");
			sbSql.append("                          TSAP.AMOUNT\n");
			sbSql.append("        ) TMP1,       -- 省份价格调整表\n");
		}
		sbSql.append("        (\n");
		sbSql.append("                SELECT T.SERIES_ID, T.SERIES_NAME, T.MODEL_ID, T.MODEL_NAME, T.MODEL_ID, T.PACKAGE_ID\n");
		sbSql.append("                FROM vw_material_group T\n");
		sbSql.append("                GROUP BY T.SERIES_ID, T.SERIES_NAME, T.MODEL_ID, T.MODEL_NAME, T.MODEL_ID, T.PACKAGE_ID\n");
		sbSql.append("        ) TMP2\n");
		sbSql.append("WHERE\n");
		sbSql.append("       TVM.MATERIAL_ID = TVMGR.MATERIAL_ID\n");
		sbSql.append("       AND\n");
		sbSql.append("       TVMGR.GROUP_ID = TVMG.GROUP_ID\n");
		sbSql.append("       AND\n");
		sbSql.append("       TVMG.GROUP_ID = TMP2.PACKAGE_ID\n");

		if (!dealerId.equals(""))
		{
			sbSql.append("       AND\n");
			sbSql.append("       TVM.MATERIAL_ID = TMP1.MATERIAL_ID\n");
			sbSql.append("       AND\n");
			sbSql.append("       TMP1.DEALER_ID = " + dealerId + "\n");
		}

		if (!materialCode.equals(""))
		{
			sbSql.append(MaterialGroupManagerDao.getMaterialQuerySql("TVM.MATERIAL_ID", materialCode));
		}

		if (!materialGroup.equals(""))
		{
			sbSql.append(MaterialGroupManagerDao.getMaterialGroupQuerySql("TVMG.GROUP_ID", materialGroup));
		}

		return dao.pageQuery(sbSql.toString(), null, getFunName(), pageSize, curPage);
	}

	/**
	 * 方法描述 ： 查询车系的物料数据详细数据
	 * <p/>
	 * <li>可用数量 = 物料总数 - 订单预留 - 车厂预留 - 订单已提交状态</li>
	 * <p/>
	 * <li>物料价格 = 物料的基础价格 + 省份的调整价格</li>
	 * 
	 * @param dealerId
	 *            经销商id
	 * @param yieldly
	 *            产地ID
	 * @param finType
	 *            资金账户类型ID
	 * @param materialCode
	 *            物料编码
	 * @param materialId
	 *            物料ID
	 * @return
	 * @throws RuntimeException
	 * @author wangsongwei
	 */
	public Map<String, Object> getMaterialResourceInfo(String dealerId, String yieldly, String finType, String materialCode, String materialId, String createId, String orderType) throws Exception {
		List<Object> params = new ArrayList<Object>();
		// 如果是承诺类型的订单默认使用承兑汇票的资金类型
		// finType = finType.equals(Constant.ACCOUNT_TYPE_05.toString()) ?
		// Constant.ACCOUNT_TYPE_02.toString() : finType;

		String specalRate = "0";
		Map<String, Object> seriesRateMap = null;
		Map<String, Object> modelRateMap = null;
		boolean isf = false;

		StringBuffer sbSql = new StringBuffer();

		sbSql.append("SELECT NVL(B.DIS_RATE,0)     DIS_RATE, NVL(B.IS_F,0) IS_F\n");
		sbSql.append("  FROM TM_VHCL_MATERIAL      A,\n");
		sbSql.append("       VW_MATERIAL_GROUP_MAT A1,\n");
		sbSql.append("       TT_SPECIAL_RATE_SET   B\n");
		sbSql.append(" WHERE A.MATERIAL_ID = A1.MATERIAL_ID\n");
		sbSql.append("   AND A1.SERIES_ID = B.GROUP_ID\n");
		sbSql.append("   AND B.GROUP_LEVEL = ?\n");
		// sbSql.append("   AND NVL(B.DIS_RATE,0) <> 0\n");
		sbSql.append("   AND B.FIN_TYPE = ?\n");
		sbSql.append("   AND A1.MATERIAL_CODE = ?\n");
		sbSql.append(" GROUP BY B.DIS_RATE,B.IS_F");

		params.add(2);
		params.add(finType);
		params.add(materialCode);
		seriesRateMap = dao.pageQueryMap(sbSql.toString(), params, getFunName()); // 查询车系的折扣率是否有上下浮动的设置

		if (seriesRateMap != null)
		{
			specalRate = seriesRateMap.get("DIS_RATE").toString();
			isf = seriesRateMap.get("IS_F").toString().equals("0") ? false : true;
		}
		else
		{
			sbSql.delete(0, sbSql.length());
			params.clear();
			params.add(3);
			params.add(finType);
			params.add(materialCode);

			sbSql.append("SELECT NVL(B.DIS_RATE,0)     DIS_RATE, NVL(B.IS_F,0) IS_F\n");
			sbSql.append("  FROM TM_VHCL_MATERIAL      A,\n");
			sbSql.append("       VW_MATERIAL_GROUP_MAT A1,\n");
			sbSql.append("       TT_SPECIAL_RATE_SET   B\n");
			sbSql.append(" WHERE A.MATERIAL_ID = A1.MATERIAL_ID\n");
			sbSql.append("   AND A1.MODEL_ID = B.GROUP_ID\n");
			sbSql.append("   AND B.GROUP_LEVEL = ?\n");
			// sbSql.append("   AND NVL(B.DIS_RATE,0) <> 0\n");
			sbSql.append("   AND B.FIN_TYPE = ?\n");
			sbSql.append("   AND A1.MATERIAL_CODE = ?\n");
			sbSql.append(" GROUP BY B.DIS_RATE, B.IS_F");

			modelRateMap = dao.pageQueryMap(sbSql.toString(), params, getFunName()); // 查询车型的折扣率是否有上下浮动的设置

			if (modelRateMap != null)
			{
				specalRate = modelRateMap.get("DIS_RATE").toString();
				isf = modelRateMap.get("IS_F").toString().equals("0") ? false : true;
			}
		}

		createId = CommonUtils.checkNull(createId);

		sbSql.delete(0, sbSql.length());
		if (!"".equals(createId))
		{
			sbSql.append("WITH ");
			sbSql.append(ResourceSqlDao.getAsSqlSpecialResourceQuery(createId)); // 物料库存资源
			sbSql.append(",\n");
			sbSql.append("MATERIAL_PRICE AS(\n");
			sbSql.append("    SELECT TCOD.STAND_PRICE, TCOD.MAI_ID\n");
			sbSql.append("      FROM TM_CUS_ORDER TCO, TM_CUS_ORDER_DETAIL TCOD\n");
			sbSql.append("     WHERE TCO.CUS_ORDER_ID = TCOD.CUS_ORDER_ID\n");
			sbSql.append("       AND TCO.CUS_ORDER_ID = " + createId + "\n");
			sbSql.append(")");
			sbSql.append("SELECT TVM.MATERIAL_ID,\n");
			sbSql.append("       TVM.MATERIAL_CODE,\n");
			sbSql.append("       TVM.MATERIAL_NAME,\n");
			sbSql.append("       TVM.COLOR_NAME,\n");
			sbSql.append("       VMG.SERIES_ID,\n");
			sbSql.append("       VMG.SERIES_NAME,\n");
			sbSql.append("       VMG.MODEL_CODE,\n");
			sbSql.append("       VMG.MODEL_NAME,\n");
			sbSql.append("       VMG.PACKAGE_CODE,\n");
			sbSql.append("       VMG.PACKAGE_NAME,\n");
			sbSql.append("		 NVL(MP.STAND_PRICE,0) C_PRICE,\n");
			sbSql.append("       NVL(VSA.RESOURCE_AMOUNT, 0) RESOURCE_AMOUNT,\n");
			sbSql.append("       NVL(VSA.RESOURCE_AMOUNT, 0) RESOURCE_AMOUNT_STATUS,\n");

			if ((seriesRateMap != null || modelRateMap != null) && isf)
			{
				sbSql.append("      " + specalRate + " + \n");
				sbSql.append("       NVL((" + ResourceSqlDao.getSqlFinAccRateQuery(finType, yieldly) + "), 0)  DIS_VALUE\n");
			}
			else if ((seriesRateMap != null || modelRateMap != null) && !isf)
			{
				sbSql.append("      " + specalRate + " AS DIS_VALUE\n");
			}
			else
			{
				sbSql.append("       NVL((" + ResourceSqlDao.getSqlSeriesRateQuery("VMG.SERIES_ID", yieldly) + "), 0) + NVL(("
						+ ResourceSqlDao.getSqlFinAccRateQuery(finType, yieldly) + "), 0)  DIS_VALUE\n");
			}

			sbSql.append("  FROM TM_VHCL_MATERIAL      TVM,\n");
			sbSql.append("       VW_MATERIAL_GROUP_MAT VMG,\n");
			sbSql.append("		 MATERIAL_PRICE MP,	\n");
			sbSql.append("       VEHICLE_STOCK_ALL     VSA\n");
			sbSql.append(" WHERE TVM.MATERIAL_ID = VMG.MATERIAL_ID\n");
			sbSql.append("   AND TVM.MATERIAL_ID = MP.MAI_ID\n");
			sbSql.append("	 AND TVM.MATERIAL_ID = VSA.MATERIAL_ID(+)");

		}
		else
		{
			sbSql.append("WITH ");
			sbSql.append(ResourceSqlDao.getAsSqlNormalResourceQuery()); // 物料库存资源
			sbSql.append("-- 查询物料信息	\n");
			sbSql.append("SELECT TVM.MATERIAL_ID,\n");
			sbSql.append("       TVM.MATERIAL_CODE,\n");
			sbSql.append("       TVM.MATERIAL_NAME,\n");
			sbSql.append("       TVM.COLOR_NAME,\n");
			sbSql.append("       VMG.SERIES_ID,\n");
			sbSql.append("       VMG.SERIES_NAME,\n");
			sbSql.append("       VMG.MODEL_CODE,\n");
			sbSql.append("       VMG.MODEL_NAME,\n");
			sbSql.append("       VMG.PACKAGE_CODE,\n");
			sbSql.append("       VMG.PACKAGE_NAME,\n");
//			//如果订单类型为中转库，那么判断车系类型，如果是商用车价格定为5000，否则定为7000  add by zxy 20140304
//			if(orderType != null && (Constant.ORDER_TYPE_04.toString()).equals(orderType)){
//				sbSql.append("       DECODE(VMG.MODEL_TYPE,'"+ Constant.MODEL_TYPE_S +"','"+ Constant.OUT_MODEL_PRICE_5000 +"','"+ Constant.OUT_MODEL_PRICE_7000 +"') C_PRICE,\n");
//			}else{
				sbSql.append("		 NVL(TVM.VHCL_PRICE,0) + NVL((" + ResourceSqlDao.getSqlRegionPriceQuery(dealerId, yieldly) + "),0) C_PRICE,\n");
//			}
			sbSql.append("       NVL(VSA.RESOURCE_AMOUNT, 0) RESOURCE_AMOUNT,\n");
			sbSql.append("          -- 可用数量统计\n");
			sbSql.append("          CASE WHEN NVL(VSA.RESOURCE_AMOUNT, 0) > 20\n");
			sbSql.append("               THEN '有'\n");
			sbSql.append("               ELSE TO_CHAR(NVL(VSA.RESOURCE_AMOUNT, 0))\n");
			sbSql.append("          END RESOURCE_AMOUNT_STATUS,\n");
//
//			//如果订单类型为中转库，那么折扣率直接为0  add by zxy 20140305
//			if(orderType != null && (Constant.ORDER_TYPE_04.toString()).equals(orderType)){
//				sbSql.append("       0 DIS_VALUE \n");
//			}else{
				if ((seriesRateMap != null || modelRateMap != null) && isf)
				{
					sbSql.append("      " + specalRate + " + \n");
					sbSql.append("       NVL((" + ResourceSqlDao.getSqlFinAccRateQuery(finType, yieldly) + "), 0)  DIS_VALUE\n");
				}
				else if ((seriesRateMap != null || modelRateMap != null) && !isf)
				{
					sbSql.append("      " + specalRate + " AS DIS_VALUE \n");
				}
				else
				{
					sbSql.append("       NVL((" + ResourceSqlDao.getSqlSeriesRateQuery("VMG.SERIES_ID", yieldly) + "), 0) + NVL(("
							+ ResourceSqlDao.getSqlFinAccRateQuery(finType, yieldly) + "), 0) DIS_VALUE\n");
				}
//			}
			
			sbSql.append("  FROM TM_VHCL_MATERIAL      TVM,\n");
			sbSql.append("       VW_MATERIAL_GROUP_MAT VMG,\n");
			sbSql.append("       VEHICLE_STOCK_ALL     VSA\n");
			sbSql.append(" WHERE TVM.MATERIAL_ID = VMG.MATERIAL_ID\n");
			sbSql.append("   AND TVM.MATERIAL_ID = VSA.MATERIAL_ID(+)");
		}

		if (materialCode != null && !"".equals(materialCode))
		{
			sbSql.append("       AND TVM.MATERIAL_CODE = '" + materialCode + "'									\n");
		}

		if (materialId != null && !"".equals(materialId))
		{
			sbSql.append("       AND TVM.MATERIAL_ID = '" + materialId + "'										\n");
		}

		return dao.pageQueryMap(sbSql.toString(), null, getFunName());
	}

	/**
	 * 方法描述 ： 查询提车单的审核结果 <br/>
	 * 
	 * @param orderId
	 *            提车单ID
	 * @return
	 * @author wangsongwei
	 */
	public List<Map<String, Object>> getOrderCheckResult(String orderId) throws Exception {

		StringBuffer sbSql = new StringBuffer();

		sbSql.append("SELECT TORG.ORG_NAME, TP.POSE_NAME, TU.NAME,TO_CHAR(TVOC.CHECK_DATE,'yyyy-mm-dd hh24:mi:ss') CHK_DATE , TVOC.*				\n");
		sbSql.append("FROM TT_VS_ORDER_CHECK TVOC, TM_ORG TORG, TC_POSE TP, TC_USER TU	\n");
		sbSql.append("WHERE																\n");
		sbSql.append("     TVOC.CHECK_ORG_ID = TORG.ORG_ID								\n");
		sbSql.append("     AND															\n");
		sbSql.append("     TVOC.CHECK_POSITION_ID = TP.POSE_ID							\n");
		sbSql.append("     AND															\n");
		sbSql.append("     TVOC.CHECK_USER_ID = TU.USER_ID								\n");
		sbSql.append("     AND															\n");
		sbSql.append("     TVOC.ORDER_ID = " + orderId + "								\n");
		sbSql.append("ORDER BY TVOC.CHECK_DATE DESC										\n");

		return dao.pageQuery(sbSql.toString(), null, getFunName());
	}

	/**
	 * 方法描述 ： 获取提车单明细统计数据<br/>
	 * 
	 * @author wangsongwei
	 */
	public PageResult<Map<String, Object>> getOrderCountDetailList(Map<String, Object> infoMap, AclUserBean logonUser, int curPage, int pageSize) throws Exception {
		String groupCode = infoMap.get("groupCode").toString();
		String materialCode = infoMap.get("materialCode").toString();
		String orderType = infoMap.get("orderType").toString();
		String orderStatus = infoMap.get("orderStatus").toString();
		String orderNo = infoMap.get("orderNo").toString();
		String startdate = infoMap.get("startdate").toString();
		String endDate = infoMap.get("endDate").toString();
		String vin = infoMap.get("vin").toString();
		String invoiceNo = infoMap.get("invoiceNo").toString();
		String areaId = infoMap.get("areaId").toString();
		String dealerCode = infoMap.get("dealerCode").toString();
		String cstartdate = infoMap.get("cstartdate").toString();
		String cendDate = infoMap.get("cendDate").toString();
		String isout = infoMap.get("isout").toString();

		StringBuffer sbSql = new StringBuffer();

		sbSql.append("WITH INVO_AMOUNT AS (\n");
		sbSql.append("SELECT\n");
		sbSql.append("      TVO.ORDER_ID,             -- 订单流水号		\n");
		sbSql.append("      SUM(NVL(TVOD.CHECK_AMOUNT,0)) INVO_AMOUNT			  -- 审核数量			\n");
		sbSql.append("FROM												\n");
		sbSql.append("      TT_VS_ORDER TVO,\n");
		sbSql.append("      TT_VS_ORDER_DETAIL TVOD,\n");
		sbSql.append("      TM_DEALER TM,\n");
		sbSql.append("      TM_VHCL_MATERIAL_GROUP TVMG,\n");
		sbSql.append("      TM_VHCL_MATERIAL_GROUP_R TVMGR,\n");
		sbSql.append("      TM_VHCL_MATERIAL TVM\n");
		sbSql.append("WHERE\n");
		sbSql.append("      TVO.ORDER_ID = TVOD.ORDER_ID\n");
		sbSql.append("      AND\n");
		sbSql.append("      TVO.DEALER_ID = TM.DEALER_ID\n");
		sbSql.append("      AND\n");
		sbSql.append("      TVM.MATERIAL_ID = TVOD.MATERIAL_ID\n");
		sbSql.append("      AND\n");
		sbSql.append("      TVM.MATERIAL_ID = TVMGR.MATERIAL_ID\n");
		sbSql.append("      AND\n");
		sbSql.append("      TVMG.GROUP_ID = TVMGR.GROUP_ID\n");

		String orderStatusKey = Constant.ORDER_STATUS_07 + "," + Constant.ORDER_STATUS_08 + "," + Constant.ORDER_STATUS_09 + "," + Constant.ORDER_STATUS_11;
		sbSql.append("		AND TVO.ORDER_STATUS IN (" + orderStatusKey + ")\n");

		if(isout.equals("1")) {
			sbSql.append(" AND EXISTS (\n");
			sbSql.append(" 		SELECT 1 FROM TM_DEALER A, TM_DEALER_ORG_RELATION B, TM_ORG C");
			sbSql.append(" 		 WHERE A.DEALER_ID = TVO.DEALER_ID AND A.DEALER_ID = B.DEALER_ID AND B.ORG_ID = C.ORG_ID\n");
			sbSql.append("		   	   AND C.ORG_ID IN(2013080510045491,2013082525987270,2013082525987271,2013082525987272,2013082525987273,2013082525987274,2013082525987275,2013082525987276,2013082956190007)\n");
			sbSql.append(")\n");
		} else {
			sbSql.append(" AND NOT EXISTS (\n");
			sbSql.append(" 		SELECT 1 FROM TM_DEALER A, TM_DEALER_ORG_RELATION B, TM_ORG C");
			sbSql.append(" 		 WHERE A.DEALER_ID = TVO.DEALER_ID AND A.DEALER_ID = B.DEALER_ID AND B.ORG_ID = C.ORG_ID\n");
			sbSql.append("		   	   AND C.ORG_ID IN(2013080510045491,2013082525987270,2013082525987271,2013082525987272,2013082525987273,2013082525987274,2013082525987275,2013082525987276,2013082956190007)\n");
			sbSql.append(")\n");
		}
		
		if (!orderStatus.equals("") && !orderStatusKey.contains(orderStatus))
		{
			sbSql.append("AND TVO.ORDER_STATUS = " + orderStatus + "\n");
		}

		sbSql.append(MaterialGroupManagerDao.getOrgDealerLimitSql("TVO.DEALER_ID", logonUser));
		sbSql.append(MaterialGroupManagerDao.getPoseIdBusinessSql("TVO.AREA_ID", logonUser.getPoseId().toString()));

		if (!vin.equals(""))
		{
			sbSql.append("AND EXISTS(\n");
			sbSql.append("    SELECT 1\n");
			sbSql.append("      FROM TT_SALES_BO_DETAIL TSBD,\n");
			sbSql.append("           TT_SALES_ALLOCA_DE TSAD,\n");
			sbSql.append("           TM_VEHICLE         TV\n");
			sbSql.append("     WHERE TSAD.BO_DE_ID = TSBD.BO_DE_ID\n");
			sbSql.append("       AND TSAD.VEHICLE_ID = TV.VEHICLE_ID\n");
			sbSql.append("		 AND TSBD.OR_DE_ID = TVOD.DETAIL_ID\n");
			sbSql.append("       AND TV.VIN = '" + vin + "'\n");
			sbSql.append(")");
		}

		if (!orderType.equals(""))
		{
			sbSql.append("AND TVO.ORDER_TYPE = " + orderType + "\n");
		}

		if (!orderNo.equals(""))
		{
			sbSql.append("AND TVO.ORDER_NO LIKE '%" + orderNo + "%'\n");
		}
		if (!invoiceNo.equals(""))
		{
			sbSql.append("AND TVO.INVOICE_NO LIKE '%" + invoiceNo + "%'\n");
		}
		if (!areaId.equals(""))
		{
			sbSql.append("AND TVO.AREA_ID = " + areaId + "\n");
		}

		if (!startdate.equals(""))
		{
			sbSql.append("AND TVO.RAISE_DATE >= TO_DATE('" + startdate + " 00:00:00','yyyy-mm-dd hh24:mi:ss')\n");
		}

		if (!endDate.equals(""))
		{
			sbSql.append("AND TVO.RAISE_DATE <= TO_DATE('" + endDate + " 23:59:59','yyyy-mm-dd hh24:mi:ss')\n");
		}

		if (!cstartdate.equals(""))
		{
			sbSql.append("AND TVO.INVO_DATE >= TO_DATE('" + cstartdate + " 00:00:00','yyyy-mm-dd hh24:mi:ss')\n");
		}

		if (!cendDate.equals(""))
		{
			sbSql.append("AND TVO.INVO_DATE <= TO_DATE('" + cendDate + " 23:59:59','yyyy-mm-dd hh24:mi:ss')\n");
		}

		if (!groupCode.equals(""))
		{
			sbSql.append(MaterialGroupManagerDao.getMaterialGroupQuerySql("TVMG.GROUP_ID", groupCode));
		}
		else if (!materialCode.equals(""))
		{
			sbSql.append(MaterialGroupManagerDao.getMaterialQuerySql("TVM.MATERIAL_ID", materialCode));
		}
		if (!dealerCode.equals(""))
		{
			sbSql.append("AND TM.DEALER_CODE IN(" + getSqlQueryCondition(dealerCode) + ")\n");
		}
		sbSql.append("GROUP BY TVO.ORDER_ID\n");
		sbSql.append(")\n");

		sbSql.append("-- 订单汇总明细查询\n");
		sbSql.append("SELECT\n");
		sbSql.append("		TM.DEALER_CODE,\n");
		sbSql.append("      TM.DEALER_SHORTNAME, TM.DEALER_NAME,          -- 经销商名称		\n");
		sbSql.append("      TVO.ORDER_ID,             -- 订单流水号		\n");
		sbSql.append("      TVO.ORDER_NO,             -- 订单流水号		\n");
		sbSql.append("      VMT.SERIES_NAME,             -- 车系		\n");
		sbSql.append("      TORG.ORG_NAME,             -- 省份		\n");
		sbSql.append("      TO_CHAR(TVO.RAISE_DATE,'yyyy-MM-dd HH24:MI:SS') RAISE_DATE,           -- 提报日期			\n");
		sbSql.append("      TVO.ORDER_TYPE,           -- 订单类型			\n");
		sbSql.append("      TVO.ORDER_STATUS,         -- 订单状态			\n");
		sbSql.append("      SUM(NVL(TVOD.ORDER_AMOUNT,0)) ORDER_AMOUNT,              -- 订单提报数量		\n");
		sbSql.append("      SUM(NVL(TVOD.CHECK_AMOUNT,0)) CHECK_AMOUNT,			  -- 审核数量			\n");
		sbSql.append("      NVL(JJ.INVO_AMOUNT,0) INVO_AMOUNT,			  -- 审核数量			\n");
		sbSql.append("      SUM(NVL(TVOD.BOARD_NUMBER,0)) BOARD_NUMBER,			\n");
		sbSql.append("      SUM(NVL(TVOD.MATCH_AMOUNT,0)) MATCH_AMOUNT,							\n");
		sbSql.append("      SUM(NVL(TVOD.OUT_AMOUNT,0)) OUT_AMOUNT,							\n");
		sbSql.append("      SUM(NVL(TVOD.DELIVERY_AMOUNT,0)) DELIVERY_AMOUNT,	\n");
		sbSql.append("      SUM(NVL(TVOD.ACC_AMOUNT,0)) ACC_AMOUNT,	\n");
		sbSql.append("		(SELECT TVA.ADDRESS FROM TM_VS_ADDRESS TVA WHERE TVA.ID = TVO.DELIV_ADD_ID ) ADDRESS \n");
		sbSql.append("FROM												\n");
		sbSql.append("      TT_VS_ORDER TVO,\n");
		sbSql.append("      TT_VS_ORDER_DETAIL TVOD,\n");
		sbSql.append("      TM_DEALER TM,\n");
		sbSql.append("      TM_VHCL_MATERIAL_GROUP TVMG,\n");
		sbSql.append("      TM_VHCL_MATERIAL_GROUP_R TVMGR,\n");
		sbSql.append("      TM_VHCL_MATERIAL TVM,\n");
		sbSql.append("		VW_MATERIAL_GROUP_MAT VMT,\n");
		sbSql.append("      TM_ORG TORG, TM_DEALER_ORG_RELATION TDOR,\n");
		sbSql.append("		INVO_AMOUNT JJ\n");
		sbSql.append("WHERE\n");
		sbSql.append("      TVO.ORDER_ID = TVOD.ORDER_ID\n");
		sbSql.append("      AND TVO.ORDER_ID = JJ.ORDER_ID(+)\n");
		sbSql.append("      AND TVO.DEALER_ID = TM.DEALER_ID\n");
		sbSql.append("      AND TM.DEALER_ID = TDOR.DEALER_ID(+)\n");
		sbSql.append("      AND TDOR.ORG_ID = TORG.ORG_ID(+)\n");
		sbSql.append("		AND VMT.MATERIAL_ID = TVOD.MATERIAL_ID\n");
		sbSql.append("      AND TVM.MATERIAL_ID = TVOD.MATERIAL_ID\n");
		sbSql.append("      AND TVM.MATERIAL_ID = TVMGR.MATERIAL_ID\n");
		sbSql.append("      AND TVMG.GROUP_ID = TVMGR.GROUP_ID\n");

		orderStatusKey = Constant.ORDER_STATUS_02 + "," + Constant.ORDER_STATUS_04 + "," + Constant.ORDER_STATUS_05 + "," + Constant.ORDER_STATUS_06 + ","
				+ Constant.ORDER_STATUS_07 + "," + Constant.ORDER_STATUS_08 + "," + Constant.ORDER_STATUS_09 + "," + Constant.ORDER_STATUS_11 + "," + Constant.ORDER_STATUS_12;

		sbSql.append("		AND TVO.ORDER_STATUS IN (" + orderStatusKey + ")\n");

		if(isout.equals("1")) {
			sbSql.append(" AND EXISTS (\n");
			sbSql.append(" 		SELECT 1 FROM TM_DEALER A, TM_DEALER_ORG_RELATION B, TM_ORG C");
			sbSql.append(" 		 WHERE A.DEALER_ID = TVO.DEALER_ID AND A.DEALER_ID = B.DEALER_ID AND B.ORG_ID = C.ORG_ID\n");
			sbSql.append("		   	   AND C.ORG_ID IN(2013080510045491,2013082525987270,2013082525987271,2013082525987272,2013082525987273,2013082525987274,2013082525987275,2013082525987276,2013082956190007)\n");
			sbSql.append(")\n");
		} else {
			sbSql.append(" AND NOT EXISTS (\n");
			sbSql.append(" 		SELECT 1 FROM TM_DEALER A, TM_DEALER_ORG_RELATION B, TM_ORG C");
			sbSql.append(" 		 WHERE A.DEALER_ID = TVO.DEALER_ID AND A.DEALER_ID = B.DEALER_ID AND B.ORG_ID = C.ORG_ID\n");
			sbSql.append("		   	   AND C.ORG_ID IN(2013080510045491,2013082525987270,2013082525987271,2013082525987272,2013082525987273,2013082525987274,2013082525987275,2013082525987276,2013082956190007)\n");
			sbSql.append(")\n");
		}
		
		sbSql.append(MaterialGroupManagerDao.getOrgDealerLimitSql("TVO.DEALER_ID", logonUser));
		sbSql.append(MaterialGroupManagerDao.getPoseIdBusinessSql("TVO.AREA_ID", logonUser.getPoseId().toString()));

		if (!vin.equals(""))
		{
			sbSql.append("AND EXISTS(\n");
			sbSql.append("    SELECT 1\n");
			sbSql.append("      FROM TT_SALES_BO_DETAIL TSBD,\n");
			sbSql.append("           TT_SALES_ALLOCA_DE TSAD,\n");
			sbSql.append("           TM_VEHICLE         TV\n");
			sbSql.append("     WHERE TSAD.BO_DE_ID = TSBD.BO_DE_ID\n");
			sbSql.append("       AND TSAD.VEHICLE_ID = TV.VEHICLE_ID\n");
			sbSql.append("		 AND TSBD.OR_DE_ID = TVOD.DETAIL_ID\n");
			sbSql.append("       AND TV.VIN = '" + vin + "'\n");
			sbSql.append(")");
		}

		if (!orderType.equals(""))
		{
			sbSql.append("AND TVO.ORDER_TYPE = " + orderType + "\n");
		}

		if (!orderStatus.equals(""))
		{
			sbSql.append("AND TVO.ORDER_STATUS = " + orderStatus + "\n");
		}

		if (!orderNo.equals(""))
		{
			sbSql.append("AND TVO.ORDER_NO LIKE '%" + orderNo + "%'\n");
		}
		if (!invoiceNo.equals(""))
		{
			sbSql.append("AND TVO.INVOICE_NO LIKE '%" + invoiceNo + "%'\n");
		}
		if (!areaId.equals(""))
		{
			sbSql.append("AND TVO.AREA_ID = " + areaId + "\n");
		}
		if (!startdate.equals(""))
		{
			sbSql.append("AND TVO.RAISE_DATE >= TO_DATE('" + startdate + " 00:00:00','yyyy-mm-dd hh24:mi:ss')\n");
		}

		if (!endDate.equals(""))
		{
			sbSql.append("AND TVO.RAISE_DATE <= TO_DATE('" + endDate + " 23:59:59','yyyy-mm-dd hh24:mi:ss')\n");
		}

		if (!cstartdate.equals(""))
		{
			sbSql.append("AND TVO.INVO_DATE >= TO_DATE('" + cstartdate + " 00:00:00','yyyy-mm-dd hh24:mi:ss')\n");
		}

		if (!cendDate.equals(""))
		{
			sbSql.append("AND TVO.INVO_DATE <= TO_DATE('" + cendDate + " 23:59:59','yyyy-mm-dd hh24:mi:ss')\n");
		}

		if (!groupCode.equals(""))
		{
			sbSql.append(MaterialGroupManagerDao.getMaterialGroupQuerySql("TVMG.GROUP_ID", groupCode));
		}
		else if (!materialCode.equals(""))
		{
			sbSql.append(MaterialGroupManagerDao.getMaterialQuerySql("TVM.MATERIAL_ID", materialCode));
		}
		if (!dealerCode.equals(""))
		{
			sbSql.append("AND TM.DEALER_CODE IN(" + getSqlQueryCondition(dealerCode) + ")\n");
		}
		sbSql.append("GROUP BY\n");
		sbSql.append("TM.DEALER_CODE,TORG.ORG_NAME,VMT.SERIES_NAME,");
		sbSql.append("TM.DEALER_SHORTNAME,");
		sbSql.append("      TVO.DEALER_ID, TVO.DELIV_ADD_ID,\n");
		sbSql.append("      TM.DEALER_NAME,\n");
		sbSql.append("      TVO.ORDER_ID,\n");
		sbSql.append("      TVO.ORDER_NO,\n");
		sbSql.append("      TVO.RAISE_DATE,\n");
		sbSql.append("      TVO.ORDER_TYPE,\n");
		sbSql.append("      TVO.ORDER_STATUS, JJ.INVO_AMOUNT\n");

		return dao.pageQuery(sbSql.toString(), null, getFunName(), pageSize, curPage);
	}

	/**
	 * 方法描述 ： 获取提车单明细统计数据<br/>
	 * 
	 * @author wangsongwei
	 */
	public Map<String, Object> getOrderCountAllList(Map<String, Object> infoMap, AclUserBean logonUser) throws Exception {
		
		List<Object> params = new ArrayList<Object>();
		
		String groupCode = infoMap.get("groupCode").toString();
		String materialCode = infoMap.get("materialCode").toString();
		String orderType = infoMap.get("orderType").toString();
		String orderStatus = infoMap.get("orderStatus").toString();
		String orderNo = infoMap.get("orderNo").toString();
		String startdate = infoMap.get("startdate").toString();
		String endDate = infoMap.get("endDate").toString();
		String vin = infoMap.get("vin").toString();
		String invoiceNo = infoMap.get("invoiceNo").toString();
		String areaId = infoMap.get("areaId").toString();
		String dealerCode = infoMap.get("dealerCode").toString();
		String cstartdate = infoMap.get("cstartdate").toString();
		String cendDate = infoMap.get("cendDate").toString();
		String isout = infoMap.get("isout").toString();

		StringBuffer sbSql = new StringBuffer();

		sbSql.append("SELECT\n");
		sbSql.append("      SUM(NVL(TVOD.ORDER_AMOUNT,0)) ORDER_AMOUNT,              -- 订单提报数量		\n");
		sbSql.append("      SUM(NVL(TVOD.CHECK_AMOUNT,0)) CHECK_AMOUNT,			  	 -- 审核数量			\n");
		sbSql.append("		SUM(DECODE(TVO.ORDER_STATUS,\n");
		sbSql.append("           '10211007',\n");
		sbSql.append("           NVL(TVOD.CHECK_AMOUNT, 0),\n");
		sbSql.append("           '10211008',\n");
		sbSql.append("           NVL(TVOD.CHECK_AMOUNT, 0),\n");
		sbSql.append("           '10211009',\n");
		sbSql.append("           NVL(TVOD.CHECK_AMOUNT, 0),\n");
		sbSql.append("           '10211011',\n");
		sbSql.append("           NVL(TVOD.CHECK_AMOUNT, 0),\n");
		sbSql.append("           0)) INVO_AMOUNT, -- 开票数量\n"); 
		sbSql.append("      SUM(NVL(TVOD.BOARD_NUMBER,0)) BOARD_NUMBER,			\n");
		sbSql.append("      SUM(NVL(TVOD.MATCH_AMOUNT,0)) MATCH_AMOUNT,							\n");
		sbSql.append("      SUM(NVL(TVOD.OUT_AMOUNT,0)) OUT_AMOUNT,							\n");
		sbSql.append("      SUM(NVL(TVOD.DELIVERY_AMOUNT,0)) DELIVERY_AMOUNT,	\n");
		sbSql.append("      SUM(NVL(TVOD.ACC_AMOUNT,0)) ACC_AMOUNT	\n");
		sbSql.append("FROM												\n");
		sbSql.append("      TT_VS_ORDER TVO,\n");
		sbSql.append("      TT_VS_ORDER_DETAIL TVOD,\n");
		sbSql.append("      TM_DEALER TM,\n");
		sbSql.append("      TM_VHCL_MATERIAL_GROUP TVMG,\n");
		sbSql.append("      TM_VHCL_MATERIAL_GROUP_R TVMGR,\n");
		sbSql.append("      TM_VHCL_MATERIAL TVM\n");
		sbSql.append("WHERE\n");
		sbSql.append("      TVO.ORDER_ID = TVOD.ORDER_ID\n");
		sbSql.append("      AND TVO.DEALER_ID = TM.DEALER_ID\n");
		sbSql.append("      AND TVM.MATERIAL_ID = TVOD.MATERIAL_ID\n");
		sbSql.append("      AND TVM.MATERIAL_ID = TVMGR.MATERIAL_ID\n");
		sbSql.append("      AND TVMG.GROUP_ID = TVMGR.GROUP_ID\n");
		sbSql.append("		AND TVO.ORDER_STATUS NOT IN (10211000, 10211001, 10211003, 10211013)\n");
		
		if(isout.equals("1")) {
			sbSql.append(" AND EXISTS (\n");
			sbSql.append(" 		SELECT 1 FROM TM_DEALER A, TM_DEALER_ORG_RELATION B, TM_ORG C");
			sbSql.append(" 		 WHERE A.DEALER_ID = TVO.DEALER_ID AND A.DEALER_ID = B.DEALER_ID AND B.ORG_ID = C.ORG_ID\n");
			sbSql.append("		   	   AND C.ORG_ID IN(2013080510045491,2013082525987270,2013082525987271,2013082525987272,2013082525987273,2013082525987274,2013082525987275,2013082525987276,2013082956190007)\n");
			sbSql.append(")\n");
		} else {
			sbSql.append(" AND NOT EXISTS (\n");
			sbSql.append(" 		SELECT 1 FROM TM_DEALER A, TM_DEALER_ORG_RELATION B, TM_ORG C");
			sbSql.append(" 		 WHERE A.DEALER_ID = TVO.DEALER_ID AND A.DEALER_ID = B.DEALER_ID AND B.ORG_ID = C.ORG_ID\n");
			sbSql.append("		   	   AND C.ORG_ID IN(2013080510045491,2013082525987270,2013082525987271,2013082525987272,2013082525987273,2013082525987274,2013082525987275,2013082525987276,2013082956190007)\n");
			sbSql.append(")\n");
		}

		sbSql.append(MaterialGroupManagerDao.getOrgDealerLimitSql("TVO.DEALER_ID", logonUser));
		sbSql.append(MaterialGroupManagerDao.getPoseIdBusinessSql("TVO.AREA_ID", logonUser.getPoseId().toString()));

		if (!vin.equals(""))
		{
			sbSql.append("AND EXISTS(\n");
			sbSql.append("    SELECT 1\n");
			sbSql.append("      FROM TT_SALES_BO_DETAIL TSBD,\n");
			sbSql.append("           TT_SALES_ALLOCA_DE TSAD,\n");
			sbSql.append("           TM_VEHICLE         TV\n");
			sbSql.append("     WHERE TSAD.BO_DE_ID = TSBD.BO_DE_ID\n");
			sbSql.append("       AND TSAD.VEHICLE_ID = TV.VEHICLE_ID\n");
			sbSql.append("		 AND TSBD.OR_DE_ID = TVOD.DETAIL_ID\n");
			sbSql.append("       AND TV.VIN = ?\n");
			sbSql.append(")");
			
			params.add(vin);
		}

		if (!orderType.equals(""))
		{
			sbSql.append("AND TVO.ORDER_TYPE = ?\n");
			params.add(orderType);
		}

		if (!orderStatus.equals(""))
		{
			sbSql.append("AND TVO.ORDER_STATUS = ?\n");
			params.add(orderStatus);
		}

		if (!orderNo.equals(""))
		{
			sbSql.append("AND TVO.ORDER_NO LIKE ?\n");
			params.add(orderNo);
		}
		if (!invoiceNo.equals(""))
		{
			sbSql.append("AND TVO.INVOICE_NO LIKE ?\n");
			params.add(invoiceNo);
		}
		if (!areaId.equals(""))
		{
			sbSql.append("AND TVO.AREA_ID = ?\n");
			params.add(areaId);
		}
		if (!startdate.equals(""))
		{
			sbSql.append("AND trunc(TVO.RAISE_DATE) >= TO_DATE(?,'yyyy-mm-dd')\n");
			params.add(startdate);
		}

		if (!endDate.equals(""))
		{
			sbSql.append("AND trunc(TVO.RAISE_DATE) <= TO_DATE(?,'yyyy-mm-dd')\n");
			params.add(endDate);
		}

		if (!cstartdate.equals(""))
		{
			sbSql.append("AND TVO.INVO_DATE >= TO_DATE(?,'yyyy-mm-dd')\n");
			params.add(cstartdate);
		}

		if (!cendDate.equals(""))
		{
			sbSql.append("AND TVO.INVO_DATE <= TO_DATE(?,'yyyy-mm-dd')\n");
			params.add(cendDate);
		}

		if (!groupCode.equals(""))
		{
			sbSql.append(MaterialGroupManagerDao.getMaterialGroupQuerySql("TVMG.GROUP_ID", groupCode));
		}
		else if (!materialCode.equals(""))
		{
			sbSql.append(MaterialGroupManagerDao.getMaterialQuerySql("TVM.MATERIAL_ID", materialCode));
		}
		if (!dealerCode.equals(""))
		{
			sbSql.append("AND TM.DEALER_CODE IN(" + getSqlBuffer(dealerCode, params) + ")\n");
		}

		return dao.pageQueryMap(sbSql.toString(), params, getFunName());
	}

	/**
	 * 方法描述 ： 获取提车单信息统计数据<br/>
	 * 
	 * @author wangsongwei
	 */
	public PageResult<Map<String, Object>> getOrderCountList(Map<String, Object> infoMap, AclUserBean logonUser, int curPage, int pageSize) throws Exception {

		String groupCode = infoMap.get("groupCode").toString();
		String materialCode = infoMap.get("materialCode").toString();
		String orderType = infoMap.get("orderType").toString();
		String orderStatus = infoMap.get("orderStatus").toString();
		String orderNo = infoMap.get("orderNo").toString();
		String startdate = infoMap.get("startdate").toString();
		String endDate = infoMap.get("endDate").toString();
		String vin = infoMap.get("vin").toString();
		String invoiceNo = infoMap.get("invoiceNo").toString();
		String areaId = infoMap.get("areaId").toString();
		String dealerCode = infoMap.get("dealerCode").toString();
		String cstartdate = infoMap.get("cstartdate").toString();
		String cendDate = infoMap.get("cendDate").toString();
		String isout = infoMap.get("isout").toString();

		StringBuffer sbSql = new StringBuffer();

		sbSql.append("-- 订单汇总查询									\n");
		sbSql.append("WITH INVO_AMOUNT AS (							\n");
		sbSql.append("SELECT										\n");
		sbSql.append("      TVM.MATERIAL_CODE,    -- 物料编号\n");
		sbSql.append("      SUM(NVL(TVOD.CHECK_AMOUNT,0)) INVO_AMOUNT	\n");
		sbSql.append("FROM\n");
		sbSql.append("      TT_VS_ORDER TVO,\n");
		sbSql.append("      TT_VS_ORDER_DETAIL TVOD,\n");
		sbSql.append("      TM_VHCL_MATERIAL_GROUP TVMG,\n");
		sbSql.append("      TM_VHCL_MATERIAL_GROUP_R TVMGR,\n");
		sbSql.append("      TM_DEALER TM,\n");
		sbSql.append("      TM_VHCL_MATERIAL TVM\n");
		sbSql.append("WHERE\n");
		sbSql.append("      TVO.ORDER_ID = TVOD.ORDER_ID\n");
		sbSql.append("       AND TVO.DEALER_ID=TM.DEALER_ID\n");
		sbSql.append("      AND TVM.MATERIAL_ID = TVOD.MATERIAL_ID\n");
		sbSql.append("      AND TVM.MATERIAL_ID = TVMGR.MATERIAL_ID\n");
		sbSql.append("      AND TVMG.GROUP_ID = TVMGR.GROUP_ID\n");

		String orderStatusKey = Constant.ORDER_STATUS_07 + "," + Constant.ORDER_STATUS_08 + "," + Constant.ORDER_STATUS_09 + "," + Constant.ORDER_STATUS_11;

		if(isout.equals("1")) {
			sbSql.append(" AND EXISTS (\n");
			sbSql.append(" 		SELECT 1 FROM TM_DEALER A, TM_DEALER_ORG_RELATION B, TM_ORG C");
			sbSql.append(" 		 WHERE A.DEALER_ID = TVO.DEALER_ID AND A.DEALER_ID = B.DEALER_ID AND B.ORG_ID = C.ORG_ID\n");
			sbSql.append("		   	   AND C.ORG_ID IN(2013080510045491,2013082525987270,2013082525987271,2013082525987272,2013082525987273,2013082525987274,2013082525987275,2013082525987276,2013082956190007)\n");
			sbSql.append(")\n");
		} else {
			sbSql.append(" AND NOT EXISTS (\n");
			sbSql.append(" 		SELECT 1 FROM TM_DEALER A, TM_DEALER_ORG_RELATION B, TM_ORG C");
			sbSql.append(" 		 WHERE A.DEALER_ID = TVO.DEALER_ID AND A.DEALER_ID = B.DEALER_ID AND B.ORG_ID = C.ORG_ID\n");
			sbSql.append("		   	   AND C.ORG_ID IN(2013080510045491,2013082525987270,2013082525987271,2013082525987272,2013082525987273,2013082525987274,2013082525987275,2013082525987276,2013082956190007)\n");
			sbSql.append(")\n");
		}
		
		sbSql.append("		AND TVO.ORDER_STATUS IN (" + orderStatusKey + ")\n");
		if (!orderStatus.equals("") && !orderStatusKey.contains(orderStatus))
		{
			sbSql.append("AND TVO.ORDER_STATUS = " + orderStatus + "\n");
		}

		sbSql.append(MaterialGroupManagerDao.getOrgDealerLimitSql("TVO.DEALER_ID", logonUser));
		sbSql.append(MaterialGroupManagerDao.getPoseIdBusinessSql("TVO.AREA_ID", logonUser.getPoseId().toString()));

		if (!vin.equals(""))
		{
			sbSql.append("AND EXISTS(");
			sbSql.append("SELECT 1\n");
			sbSql.append("  FROM TT_SALES_ALLOCA_DE TSAD,\n");
			sbSql.append("       TM_VEHICLE         TV,\n");
			sbSql.append("       TT_SALES_BO_DETAIL TSBD,\n");
			sbSql.append("       TT_VS_ORDER_DETAIL TVOD\n");
			sbSql.append(" WHERE TSAD.VEHICLE_ID = TV.VEHICLE_ID\n");
			sbSql.append("   AND TSBD.BO_DE_ID = TSAD.BO_DE_ID\n");
			sbSql.append("   AND TSBD.OR_DE_ID = TVOD.DETAIL_ID\n");
			sbSql.append("	 AND TVOD.ORDER_ID = TVO.ORDER_ID\n");
			sbSql.append("   AND TV.VIN = '" + vin + "'\n");
			sbSql.append("GROUP BY TVOD.ORDER_ID");
			sbSql.append(")");
		}

		if (!orderType.equals(""))
		{
			sbSql.append("AND TVO.ORDER_TYPE = " + orderType + "\n");
		}

		if (!orderNo.equals(""))
		{
			sbSql.append("AND TVO.ORDER_NO LIKE '%" + orderNo + "%'\n");
		}
		if (!invoiceNo.equals(""))
		{
			sbSql.append("AND TVO.INVOICE_NO LIKE '%" + invoiceNo + "%'\n");
		}
		if (!areaId.equals(""))
		{
			sbSql.append("AND TVO.AREA_ID = " + areaId + "\n");
		}
		if (!startdate.equals(""))
		{
			sbSql.append("AND TVO.RAISE_DATE >= TO_DATE('" + startdate + " 00:00:00','yyyy-mm-dd hh24:mi:ss')\n");
		}

		if (!endDate.equals(""))
		{
			sbSql.append("AND TVO.RAISE_DATE <= TO_DATE('" + endDate + " 23:59:59','yyyy-mm-dd hh24:mi:ss')\n");
		}

		if (!cstartdate.equals(""))
		{
			sbSql.append("AND TVO.INVO_DATE >= TO_DATE('" + cstartdate + " 00:00:00','yyyy-mm-dd hh24:mi:ss')\n");
		}

		if (!cendDate.equals(""))
		{
			sbSql.append("AND TVO.INVO_DATE <= TO_DATE('" + cendDate + " 23:59:59','yyyy-mm-dd hh24:mi:ss')\n");
		}

		if (!groupCode.equals(""))
		{
			sbSql.append(MaterialGroupManagerDao.getMaterialGroupQuerySql("TVMG.GROUP_ID", groupCode));
		}
		else if (!materialCode.equals(""))
		{
			sbSql.append(MaterialGroupManagerDao.getMaterialQuerySql("TVM.MATERIAL_ID", materialCode));
		}
		if (!dealerCode.equals(""))
		{
			sbSql.append("AND TM.DEALER_CODE IN(" + getSqlQueryCondition(dealerCode) + ")\n");
		}
		sbSql.append(" GROUP BY TVM.MATERIAL_CODE\n");
		sbSql.append(")\n");
		// ------------------------------------------------------------------------
		sbSql.append("-- 订单汇总查询									\n");
		sbSql.append("SELECT										\n");
		sbSql.append("      TVM.MATERIAL_CODE,    -- 物料编号\n");
		sbSql.append("      TVM.MATERIAL_NAME,    -- 物料名称\n");
		sbSql.append("      SUM(NVL(TVOD.ORDER_AMOUNT,0)) ORDER_AMOUNT,              -- 订单提报数量		\n");
		sbSql.append("      SUM(NVL(TVOD.CHECK_AMOUNT,0)) CHECK_AMOUNT,			  -- 审核数量			\n");
		sbSql.append("		NVL(JJ.INVO_AMOUNT,0) INVO_AMOUNT, \n");
		sbSql.append("      SUM(NVL(TVOD.DELIVERY_AMOUNT,0)) DELIVERY_AMOUNT, -- 发运数量\n");
		sbSql.append("      SUM(NVL(TVOD.BOARD_NUMBER,0)) BOARD_NUMBER,    -- 组板数量\n");
		sbSql.append("      SUM(NVL(TVOD.MATCH_AMOUNT,0)) MATCH_AMOUNT,    -- 配车数量\n");
		sbSql.append("      SUM(NVL(TVOD.OUT_AMOUNT,0)) OUT_AMOUNT,       -- 出库数量\n");
		sbSql.append("      SUM(NVL(TVOD.ACC_AMOUNT,0)) ACC_AMOUNT	\n");
		sbSql.append("FROM\n");
		sbSql.append("      TT_VS_ORDER TVO,\n");
		sbSql.append("      TT_VS_ORDER_DETAIL TVOD,\n");
		sbSql.append("      TM_VHCL_MATERIAL_GROUP TVMG,\n");
		sbSql.append("      TM_VHCL_MATERIAL_GROUP_R TVMGR,\n");
		sbSql.append("      TM_VHCL_MATERIAL TVM,\n");
		sbSql.append("      TM_DEALER TM,\n");
		sbSql.append("		INVO_AMOUNT JJ\n");
		sbSql.append("WHERE\n");
		sbSql.append("      TVO.ORDER_ID = TVOD.ORDER_ID\n");
		sbSql.append("      AND TVM.MATERIAL_CODE = JJ.MATERIAL_CODE(+)\n");
		sbSql.append("      AND TVM.MATERIAL_ID = TVOD.MATERIAL_ID\n");
		sbSql.append("      AND TVM.MATERIAL_ID = TVMGR.MATERIAL_ID\n");
		sbSql.append("       AND TVO.DEALER_ID=TM.DEALER_ID\n");
		sbSql.append("      AND TVMG.GROUP_ID = TVMGR.GROUP_ID\n");

		orderStatusKey = Constant.ORDER_STATUS_02 + "," + Constant.ORDER_STATUS_04 + "," + Constant.ORDER_STATUS_05 + "," + Constant.ORDER_STATUS_06 + ","
				+ Constant.ORDER_STATUS_07 + "," + Constant.ORDER_STATUS_08 + "," + Constant.ORDER_STATUS_09 + "," + Constant.ORDER_STATUS_11 + "," + Constant.ORDER_STATUS_12;

		if(isout.equals("1")) {
			sbSql.append(" AND EXISTS (\n");
			sbSql.append(" 		SELECT 1 FROM TM_DEALER A, TM_DEALER_ORG_RELATION B, TM_ORG C");
			sbSql.append(" 		 WHERE A.DEALER_ID = TVO.DEALER_ID AND A.DEALER_ID = B.DEALER_ID AND B.ORG_ID = C.ORG_ID\n");
			sbSql.append("		   	   AND C.ORG_ID IN(2013080510045491,2013082525987270,2013082525987271,2013082525987272,2013082525987273,2013082525987274,2013082525987275,2013082525987276,2013082956190007)\n");
			sbSql.append(")\n");
		} else {
			sbSql.append(" AND NOT EXISTS (\n");
			sbSql.append(" 		SELECT 1 FROM TM_DEALER A, TM_DEALER_ORG_RELATION B, TM_ORG C");
			sbSql.append(" 		 WHERE A.DEALER_ID = TVO.DEALER_ID AND A.DEALER_ID = B.DEALER_ID AND B.ORG_ID = C.ORG_ID\n");
			sbSql.append("		   	   AND C.ORG_ID IN(2013080510045491,2013082525987270,2013082525987271,2013082525987272,2013082525987273,2013082525987274,2013082525987275,2013082525987276,2013082956190007)\n");
			sbSql.append(")\n");
		}
		
		sbSql.append("		AND TVO.ORDER_STATUS IN (" + orderStatusKey + ")\n");

		sbSql.append(MaterialGroupManagerDao.getOrgDealerLimitSql("TVO.DEALER_ID", logonUser));
		sbSql.append(MaterialGroupManagerDao.getPoseIdBusinessSql("TVO.AREA_ID", logonUser.getPoseId().toString()));

		if (!orderStatus.equals(""))
		{
			sbSql.append("AND TVO.ORDER_STATUS = " + orderStatus + "\n");
		}

		if (!vin.equals(""))
		{
			sbSql.append("AND EXISTS(");
			sbSql.append("SELECT 1\n");
			sbSql.append("  FROM TT_SALES_ALLOCA_DE TSAD,\n");
			sbSql.append("       TM_VEHICLE         TV,\n");
			sbSql.append("       TT_SALES_BO_DETAIL TSBD,\n");
			sbSql.append("       TT_VS_ORDER_DETAIL TVOD\n");
			sbSql.append(" WHERE TSAD.VEHICLE_ID = TV.VEHICLE_ID\n");
			sbSql.append("   AND TSBD.BO_DE_ID = TSAD.BO_DE_ID\n");
			sbSql.append("   AND TSBD.OR_DE_ID = TVOD.DETAIL_ID\n");
			sbSql.append("	 AND TVOD.ORDER_ID = TVO.ORDER_ID\n");
			sbSql.append("   AND TV.VIN = '" + vin + "'\n");
			sbSql.append("GROUP BY TVOD.ORDER_ID");
			sbSql.append(")");
		}

		if (!orderType.equals(""))
		{
			sbSql.append("AND TVO.ORDER_TYPE = " + orderType + "\n");
		}

		if (!orderNo.equals(""))
		{
			sbSql.append("AND TVO.ORDER_NO LIKE '%" + orderNo + "%'\n");
		}
		if (!invoiceNo.equals(""))
		{
			sbSql.append("AND TVO.INVOICE_NO LIKE '%" + invoiceNo + "%'\n");
		}
		if (!areaId.equals(""))
		{
			sbSql.append("AND TVO.AREA_ID = " + areaId + "\n");
		}
		if (!startdate.equals(""))
		{
			sbSql.append("AND TVO.RAISE_DATE >= TO_DATE('" + startdate + " 00:00:00','yyyy-mm-dd hh24:mi:ss')\n");
		}

		if (!endDate.equals(""))
		{
			sbSql.append("AND TVO.RAISE_DATE <= TO_DATE('" + endDate + " 23:59:59','yyyy-mm-dd hh24:mi:ss')\n");
		}

		if (!cstartdate.equals(""))
		{
			sbSql.append("AND TVO.INVO_DATE >= TO_DATE('" + cstartdate + " 00:00:00','yyyy-mm-dd hh24:mi:ss')\n");
		}

		if (!cendDate.equals(""))
		{
			sbSql.append("AND TVO.INVO_DATE <= TO_DATE('" + cendDate + " 23:59:59','yyyy-mm-dd hh24:mi:ss')\n");
		}

		if (!groupCode.equals(""))
		{
			sbSql.append(MaterialGroupManagerDao.getMaterialGroupQuerySql("TVMG.GROUP_ID", groupCode));
		}
		else if (!materialCode.equals(""))
		{
			sbSql.append(MaterialGroupManagerDao.getMaterialQuerySql("TVM.MATERIAL_ID", materialCode));
		}
		if (!dealerCode.equals(""))
		{
			sbSql.append("AND TM.DEALER_CODE IN(" + getSqlQueryCondition(dealerCode) + ")\n");
		}
		sbSql.append(" GROUP BY TVM.MATERIAL_CODE, TVM.MATERIAL_NAME, JJ.INVO_AMOUNT\n");

		return dao.pageQuery(sbSql.toString(), null, getFunName(), pageSize, curPage);

	}

	/**
	 * 方法描述 ： 查询提车单的发运数据列表 <br/>
	 * 
	 * @param infoMap
	 * @param curPage
	 * @param pageSize
	 * @return
	 * @author wangsongwei
	 */
	public PageResult<Map<String, Object>> getOrderDespatchList(Map<String, Object> infoMap, Integer curPage, Integer pageSize) throws Exception {

		List<Object> params = new ArrayList<Object>();

		String orderType = CommonUtils.checkNull(infoMap.get("orderType"));
		String startdate = CommonUtils.checkNull(infoMap.get("startdate"));
		String endDate = CommonUtils.checkNull(infoMap.get("endDate"));
		String orderNo = CommonUtils.checkNull(infoMap.get("orderNo"));
		String dealerOrderNo = CommonUtils.checkNull(infoMap.get("dealerOrderNo"));
		String deliveryType = CommonUtils.checkNull(infoMap.get("deliveryType"));
		String dealerId = CommonUtils.checkNull(infoMap.get("dealerId"));
		String materialCode = CommonUtils.checkNull(infoMap.get("materialCode"));
		String orderStatus = CommonUtils.checkNull(infoMap.get("orderStatus"));
		String expStatusCode = CommonUtils.checkNull(infoMap.get("expStatusCode"));
		String dealerName = CommonUtils.checkNull(infoMap.get("dealerName"));
		String accountType = CommonUtils.checkNull(infoMap.get("accountType"));
		StringBuffer sbSql = new StringBuffer();

		sbSql.append("  SELECT   TVO.ORDER_ID,TSA.ORDER_NO DERLER_ORDER_NO, \n");
		sbSql.append("           TVO.ORDER_TYPE,\n");
		sbSql.append("           F_GET_TCCODE_DESC(TVO.ORDER_TYPE) ORDER_TYPE_DESC,\n");
		sbSql.append("           TVO.ORDER_NO,max(tc.code_desc)   FUND_TYPE_ID ,\n");
		sbSql.append("           TVO.ORDER_STATUS,\n");
		sbSql.append("           TVO.DELIVERY_TYPE,\n");
		sbSql.append("           F_GET_TCCODE_DESC(TVO.DELIVERY_TYPE) DELIVERY_TYPE_DESC,\n");
		sbSql.append("           TVO.REBATE_PRICE,\n");
		sbSql.append("           NVL (SUM (TVOD.ORDER_AMOUNT), 0) ORDER_AMOUNT,\n");
		sbSql.append("           NVL (SUM (TVOD.CHECK_AMOUNT), 0) CHECK_AMOUNT,\n");
		sbSql.append("           NVL (SUM (TVOD.ACC_AMOUNT), 0) ACC_AMOUNT,\n");
		sbSql.append("           NVL (SUM (TVOD.DELIVERY_AMOUNT), 0) DELIVERY_AMOUNT,\n");
		sbSql.append("           NVL (SUM (TVOD.MATCH_AMOUNT), 0) MATCH_AMOUNT,\n");
		sbSql.append("           NVL (TVO.ORDER_YF_PRICE, 0) ORDER_YF_PRICE,\n");
		sbSql.append("           (SELECT   TVA.ADDRESS\n");
		sbSql.append("              FROM   TM_VS_ADDRESS TVA\n");
		sbSql.append("             WHERE   TVA.ID = TVO.DELIV_ADD_ID)\n");
		sbSql.append("              ADDRESS\n");
		sbSql.append("    FROM   TT_VS_ORDER TVO,TT_SA_ORDER TSA, tc_code tc , TT_VS_ORDER_DETAIL TVOD, TM_VHCL_MATERIAL TVM, TM_DEALER TD\n");
		sbSql.append("   WHERE  tc.code_id = TVO.FUND_TYPE_ID  and  TVO.ORDER_ID = TVOD.ORDER_ID(+) AND TVOD.MATERIAL_ID = TVM.MATERIAL_ID(+) AND TVO.WR_ORDER_ID = TSA.ORDER_ID AND TVO.DEALER_ID = TD.DEALER_ID\n");

		if (!orderType.equals(""))
		{
			sbSql.append("     AND TVO.ORDER_TYPE = ?\n");
			params.add(orderType);
		}
		if (!accountType.equals(""))
		{
			sbSql.append("     AND TVO.FUND_TYPE_ID = ?\n");
			params.add(accountType);
		}
		
		

		if (!expStatusCode.equals(""))
		{
			sbSql.append("		AND TVO.ORDER_STATUS NOT IN(" + getSqlBuffer(expStatusCode, params) + ")");
		}

		if (!orderNo.equals(""))
		{
			sbSql.append("     AND TVO.ORDER_NO LIKE '%" + orderNo + "%'\n");
		}
		
		if (!dealerOrderNo.equals("")) {
			sbSql.append(" AND TSA.ORDER_NO LIKE '%"+dealerOrderNo+"%'\n");
		}

		if (!startdate.equals(""))
		{
			sbSql.append("     AND TVO.RAISE_DATE >= to_date('" + startdate + " 00:00:00','yyyy-mm-dd hh24:mi:ss')\n");
		}

		if (!endDate.equals(""))
		{
			sbSql.append("     AND TVO.RAISE_DATE <= to_date('" + endDate + " 23:59:59','yyyy-mm-dd hh24:mi:ss')\n");
		}

		if (!materialCode.equals(""))
		{
			sbSql.append(MaterialGroupManagerDao.getMaterialQuerySql("TVM.MATERIAL_ID", materialCode));
		}

		if (!deliveryType.equals(""))
		{
			sbSql.append("     AND TVO.DELIVERY_TYPE = ?\n");
			params.add(deliveryType);
		}

		if (!dealerId.equals(""))
		{
			sbSql.append(" AND TVO.DEALER_ID = ?\n");
			params.add(dealerId);
		}

		if (!orderStatus.equals(""))
		{
			sbSql.append(" AND TVO.ORDER_STATUS = ?\n");
			params.add(orderStatus);
		}
		if(!"".equals(dealerName)){
			sbSql.append(" AND TD.DEALER_NAME LIKE '%"+dealerName+"%'\n");
		}
		sbSql.append("GROUP BY   TVO.ORDER_ID,TSA.ORDER_NO,\n");
		sbSql.append("           TVO.ORDER_TYPE,\n");
		sbSql.append("           TVO.ORDER_NO,\n");
		sbSql.append("           TVO.ORDER_YF_PRICE,\n");
		sbSql.append("           TVO.DELIV_ADD_ID,\n");
		sbSql.append("           TVO.CHK_NUM,\n");
		sbSql.append("           TVO.ORDER_STATUS,\n");
		sbSql.append("           TVO.REBATE_PRICE,\n");
		sbSql.append("           TVO.DELIVERY_TYPE\n");

		return dao.pageQuery(sbSql.toString(), params, getFunName(), pageSize, curPage);
	}
	
	public List<Map<String, Object>> getOrderDespatchListDetail(Map<String, Object> infoMap){
		String orderType = CommonUtils.checkNull(infoMap.get("orderType"));
		String startdate = CommonUtils.checkNull(infoMap.get("startdate"));
		String endDate = CommonUtils.checkNull(infoMap.get("endDate"));
		String orderNo = CommonUtils.checkNull(infoMap.get("orderNo"));
		String dealerOrderNo = CommonUtils.checkNull(infoMap.get("dealerOrderNo"));
		String deliveryType = CommonUtils.checkNull(infoMap.get("deliveryType"));
		String dealerId = CommonUtils.checkNull(infoMap.get("dealerId"));
		String materialCode = CommonUtils.checkNull(infoMap.get("materialCode"));
		String orderStatus = CommonUtils.checkNull(infoMap.get("orderStatus"));
		String expStatusCode = CommonUtils.checkNull(infoMap.get("expStatusCode"));
		String dealerName = CommonUtils.checkNull(infoMap.get("dealerName"));
		String accountType = CommonUtils.checkNull(infoMap.get("accountType"));
		
		StringBuffer sql = new StringBuffer("SELECT TVO.ORDER_NO ORDER_NO_S,\n");
        sql.append("TSO.ORDER_NO,\n");
        sql.append("td.dealer_code,\n");
        sql.append("TD.DEALER_NAME,\n");
        sql.append("to_char(tso.sub_date,'yyyy-mm-dd') SUB_DATE,\n");
        sql.append("VM.SERIES_NAME,\n");
        sql.append("VM.MODEL_NAME,\n");
        sql.append("VM.PACKAGE_NAME,tc.code_desc  FUND_TYPE_ID , \n");
        sql.append("VM.COLOR_NAME,\n");
        sql.append("TVOD.CHECK_AMOUNT,\n");
        sql.append("tvod.discount_s_price,\n");
        sql.append("tvod.total_price\n");
        sql.append("FROM    tc_code tc  , TT_VS_ORDER TVO\n");
        sql.append("LEFT JOIN TT_SA_ORDER TSO\n");
        sql.append("ON TVO.WR_ORDER_ID = TSO.ORDER_ID\n");
        sql.append("LEFT JOIN TM_DEALER TD\n");
        sql.append("ON TVO.DEALER_ID = TD.DEALER_ID\n");
        sql.append("LEFT JOIN TT_VS_ORDER_DETAIL TVOD\n");
        sql.append("ON TVO.ORDER_ID = TVOD.ORDER_ID\n");
        sql.append("LEFT JOIN VW_MATERIAL_GROUP_MAT VM\n");
        sql.append("ON TVOD.MATERIAL_ID = VM.MATERIAL_ID\n");
        sql.append("WHERE 1=1 and tc.code_id =TVO.FUND_TYPE_ID   ");
        List<Object> params = new ArrayList<Object>();
        if (!orderType.equals(""))
		{
        	sql.append("     AND TVO.ORDER_TYPE = ?\n");
			params.add(orderType);
		}
		if (!accountType.equals(""))
		{
			sql.append("     AND TVO.FUND_TYPE_ID = ?\n");
			params.add(accountType);
		}
		
		

		if (!expStatusCode.equals(""))
		{
			sql.append("		AND TVO.ORDER_STATUS NOT IN(" + getSqlBuffer(expStatusCode, params) + ")");
		}

		if (!orderNo.equals(""))
		{
			sql.append("     AND TVO.ORDER_NO LIKE '%" + orderNo + "%'\n");
		}
		
		if (!dealerOrderNo.equals("")) {
			sql.append(" AND TSA.ORDER_NO LIKE '%"+dealerOrderNo+"%'\n");
		}

		if (!startdate.equals(""))
		{
			sql.append("     AND TVO.RAISE_DATE >= to_date('" + startdate + " 00:00:00','yyyy-mm-dd hh24:mi:ss')\n");
		}

		if (!endDate.equals(""))
		{
			sql.append("     AND TVO.RAISE_DATE <= to_date('" + endDate + " 23:59:59','yyyy-mm-dd hh24:mi:ss')\n");
		}

		if (!materialCode.equals(""))
		{
			sql.append(MaterialGroupManagerDao.getMaterialQuerySql("TVM.MATERIAL_ID", materialCode));
		}

		if (!deliveryType.equals(""))
		{
			sql.append("     AND TVO.DELIVERY_TYPE = ?\n");
			params.add(deliveryType);
		}

		if (!dealerId.equals(""))
		{
			sql.append(" AND TVO.DEALER_ID = ?\n");
			params.add(dealerId);
		}

		if (!orderStatus.equals(""))
		{
			sql.append(" AND TVO.ORDER_STATUS = ?\n");
			params.add(orderStatus);
		}
		if(!"".equals(dealerName)){
			sql.append(" AND TD.DEALER_NAME LIKE '%"+dealerName+"%'\n");
		}
        sql.append(" ORDER BY tvo.raise_date ASC");
        return pageQuery(sql.toString(), params, getFunName());
	}
	
	
	public PageResult<Map<String, Object>> getsalesSumList(String materialCode,String orderType,String startdate,String endDate, 
 Integer curPage, Integer pageSize) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT distinct\n");
		sql.append("           TD.DEALER_NAME,TD.Dealer_Shortname,\n");
		sql.append("           TVO.ORDER_TYPE,\n");
		sql.append("           F_GET_TCCODE_DESC(TVO.ORDER_TYPE) ORDER_TYPE_DESC,\n");
		sql.append("           TVO.DELIVERY_TYPE,\n");
		sql.append("           F_GET_TCCODE_DESC(TVO.DELIVERY_TYPE) DELIVERY_TYPE_DESC,\n");
		sql.append("           NVL (SUM (TVO.REBATE_PRICE), 0) REBATE_PRICE,\n");
		sql.append("           NVL (SUM (TVOD.ORDER_AMOUNT), 0) ORDER_AMOUNT,\n");
		sql.append("           NVL (SUM (TVOD.CHECK_AMOUNT), 0) CHECK_AMOUNT,\n");
		sql.append("           NVL (SUM (TVOD.ACC_AMOUNT), 0) ACC_AMOUNT,\n");
		sql.append("           NVL (SUM (TVOD.DELIVERY_AMOUNT), 0) DELIVERY_AMOUNT,\n");
		sql.append("           NVL (SUM (TVOD.MATCH_AMOUNT), 0) MATCH_AMOUNT,\n");
		sql.append("           NVL (SUM(TVO.ORDER_YF_PRICE), 0) ORDER_YF_PRICE,\n");
		sql.append("           (SELECT   TVA.ADDRESS\n");
		sql.append("              FROM   TM_VS_ADDRESS TVA\n");
		sql.append("             WHERE   TVA.ID = TVO.DELIV_ADD_ID)\n");
		sql.append("              ADDRESS\n");
		sql.append("    FROM   TT_VS_ORDER TVO , TT_VS_ORDER_DETAIL TVOD, TM_VHCL_MATERIAL TVM,TM_DEALER TD\n");
		sql.append("   WHERE   TVO.ORDER_ID = TVOD.ORDER_ID(+) AND TVOD.MATERIAL_ID = TVM.MATERIAL_ID(+) AND TVO.DEALER_ID=TD.DEALER_ID(+)\n");
		
		//物料
		if (!materialCode.equals(""))
		{
			sql.append(MaterialGroupManagerDao.getMaterialQuerySql("TVM.MATERIAL_ID", materialCode));
		}
		//订单类型
		if(!orderType.equals(""))
		{
			sql.append("		AND TVO.ORDER_TYPE = "+orderType+" \n"); 
		}
		//提报开始时间
		if (!startdate.equals(""))
		{
			sql.append("     AND TVO.RAISE_DATE >= to_date('" + startdate + " 00:00:00','yyyy-mm-dd hh24:mi:ss')\n");
		}
		//提报结束时间
		if (!endDate.equals(""))
		{
			sql.append("     AND TVO.RAISE_DATE <= to_date('" + endDate + " 23:59:59','yyyy-mm-dd hh24:mi:ss')\n");
		}
		
		sql.append(" GROUP BY  TVO.ORDER_TYPE,\n");
		sql.append("           TVO.DELIV_ADD_ID,\n");
		sql.append("           TVO.DELIVERY_TYPE,TD.DEALER_NAME,TD.Dealer_Shortname\n"); 

		
		return dao.pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
	}

	/**
	 * 方法描述 : 获取提车单的详细信息列表 <br/>
	 * 
	 * @author wangsongwei
	 */
	public List<Map<String, Object>> getOrderDetailList(String orderid) {

		StringBuffer sql = new StringBuffer("");

		sql.append("SELECT \n");
		sql.append("	TVOD.MATERIAL_ID, \n");
		sql.append("	TVM.MATERIAL_CODE, \n");
		sql.append("	TVO.* \n");
		sql.append("FROM \n");
		sql.append("	TT_VS_ORDER TVO, TT_VS_ORDER_DETAIL TVOD, TM_VHCL_MATERIAL TVM \n");
		sql.append("WHERE \n");
		sql.append("	TVO.ORDER_ID(+) = TVOD.ORDER_ID \n");
		sql.append("	AND \n");
		sql.append("	TVOD.MATERIAL_ID = TVM.MATERIAL_ID \n");
		sql.append("	AND \n");
		sql.append("	TVO.ORDER_ID = " + orderid + " \n");

		return super.pageQuery(sql.toString(), null, getFunName());
	}

	/**
	 * 方法描述 : 查询订单信息<br/>
	 * 
	 * @param infoMap
	 *            查询参数集
	 * @param curPage
	 *            当前页
	 * @param pageSize
	 *            每页记录数
	 * @return
	 * @author wangsongwei
	 */
	public PageResult<Map<String, Object>> getOrderList(Map<String, Object> map, Integer curPage, Integer pageSize) {
		String orderNo = CommonUtils.checkNull(map.get("orderNo")); // 订单编号
		String orgId = CommonUtils.checkNull(map.get("orgId")); // 区域ID
		String orgCode = CommonUtils.checkNull(map.get("orgCode"));
		String dealerCode = CommonUtils.checkNull(map.get("dealerCode")); // 经销商代码
		String dealerId = CommonUtils.checkNull(map.get("dealerId")); // 经销商代码
		String startdate = CommonUtils.checkNull(map.get("startdate")); // 提报日期
		String endDate = CommonUtils.checkNull(map.get("endDate")); // 提报日期
		String groupCode = CommonUtils.checkNull(map.get("groupCode")); // 物料组编号
		String orderType = CommonUtils.checkNull(map.get("orderType")); // 订单类型
		String reqStatus = CommonUtils.checkNull(map.get("reqStatus")); // 订单状态
		String expstatus = CommonUtils.checkNull(map.get("expstatus")); // 不需要查询的订单状态
		String poseId = map.get("poseId").toString();

		StringBuffer sbSql = new StringBuffer();
		sbSql.append("WITH DETAIL AS (");
		sbSql.append("SELECT B.DETAIL_ID,\n");
		sbSql.append("		" + Constant.ORDER_INVOICE_TYPE_01 + " AS ORDER_TYPE_DJ,\n");
		sbSql.append("		 A.ORDER_TYPE,\n");
		sbSql.append("		 A.ORDER_STATUS,");
		sbSql.append("       A.ORDER_ID,\n");
		sbSql.append("       A.ORDER_NO,\n");
		sbSql.append("       E.AREA_NAME,\n");
		sbSql.append("       D.DEALER_CODE,\n");
		sbSql.append("       D.DEALER_SHORTNAME DEALER_NAME,\n");
		sbSql.append("       TO_CHAR(A.RAISE_DATE, 'YYYY-MM-DD HH24:MI:SS') RAISE_DATE,\n");
		sbSql.append("       A.FUND_TYPE_ID,\n");
		sbSql.append("       A.INVO_TYPE,\n");
		sbSql.append("       A.INVOICE_NO,\n");
		sbSql.append("       A.INVOICE_NO_VER INVOICE_VER,\n");
		sbSql.append("       C.SERIES_NAME,\n");
		sbSql.append("       C.MODEL_NAME,\n");
		sbSql.append("       C.PACKAGE_NAME,\n");
		sbSql.append("       B.DISCOUNT_RATE,\n");
		sbSql.append("       B.SINGLE_PRICE,\n");
		sbSql.append("       NVL(B.CHK_PRICE,0) CHK_PRICE,\n");
		sbSql.append("       NVL(A.CHK_NUM,0) CHK_NUM,\n");
		sbSql.append("       NVL(B.DIS_AMOUNT,0) DIS_AMOUNT,\n");
		sbSql.append("       NVL(A.REBATE_PRICE,0) REBATE_PRICE,\n");
		sbSql.append("       NVL(A.ORDER_YF_PRICE,0) ORDER_YF_PRICE\n");
		sbSql.append("  FROM TT_VS_ORDER           A,\n");
		sbSql.append("       TT_VS_ORDER_DETAIL    B,\n");
		sbSql.append("       VW_MATERIAL_GROUP_MAT C,\n");
		sbSql.append("       TM_DEALER             D,\n");
		sbSql.append("       TM_BUSINESS_AREA      E,\n");
		sbSql.append("		 TM_ORG                F,\n");
		sbSql.append("		 TM_DEALER_ORG_RELATION G, \n");
		sbSql.append("		 TT_VS_ORDER_RESOURCE_RESERVE H \n");
		sbSql.append("WHERE A.ORDER_ID = B.ORDER_ID \n");
		sbSql.append("   AND B.MATERIAL_ID = C.MATERIAL_ID(+)\n");
		sbSql.append("   AND A.DEALER_ID = D.DEALER_ID(+)\n");
		sbSql.append("   AND A.AREA_ID = E.AREA_ID(+)\n");
		sbSql.append("	 AND D.DEALER_ID = G.DEALER_ID(+)\n");
		sbSql.append("   AND G.ORG_ID = F.ORG_ID(+)");
		sbSql.append("	 AND B.DETAIL_ID = H.ORDER_DETAIL_ID(+) ");
		sbSql.append("	 AND NVL(H.ALLOCA_NUM,0) = 0 ");

		sbSql.append(MaterialGroupManagerDao.getPoseIdBusinessSql("A.AREA_ID", poseId)); // 产地查看权限限制

		if (!expstatus.equals(""))
		{
			sbSql.append("   AND A.ORDER_STATUS NOT IN (" + expstatus + ")\n");
		}

		if (!reqStatus.equals(""))
		{
			sbSql.append("   AND A.ORDER_STATUS IN (" + reqStatus + ")\n");
		}

		if (!orgCode.equals(""))
		{
			sbSql.append(MaterialGroupManagerDao.getOrgQuerySql("F.ORG_ID", orgCode));
		}

		if (!dealerCode.equals(""))
		{
			sbSql.append("   AND D.DEALER_CODE IN (" + getSqlQueryCondition(dealerCode) + ")\n");
		}

		if (!startdate.equals(""))
		{
			sbSql.append("   AND A.RAISE_DATE >= TO_DATE('" + startdate + "','YYYY-MM-DD HH24:MI:SS')\n");
		}

		if (!endDate.equals(""))
		{
			sbSql.append("   AND A.RAISE_DATE <= TO_DATE('" + endDate + "','YYYY-MM-DD HH24:MI:SS')\n");
		}

		if (!orderNo.equals(""))
		{
			sbSql.append("       AND A.ORDER_NO LIKE '%" + orderNo + "%'\n");
		}

		if (!groupCode.equals(""))
		{
			sbSql.append(MaterialGroupManagerDao.getGroupQuerySql("A.ORDER_ID", groupCode, Constant.ORDER_INVOICE_TYPE_01.toString()));
		}

		sbSql.append(" GROUP BY A.ORDER_NO,\n");
		sbSql.append("		 	A.ORDER_TYPE,\n");
		sbSql.append("		 	A.ORDER_STATUS,");
		sbSql.append("       	A.ORDER_ID,\n");
		sbSql.append("          E.AREA_NAME,\n");
		sbSql.append("          D.DEALER_CODE,\n");
		sbSql.append("          D.DEALER_SHORTNAME,\n");
		sbSql.append("          A.RAISE_DATE,\n");
		sbSql.append("          A.FUND_TYPE_ID,\n");
		sbSql.append("          A.INVO_TYPE,\n");
		sbSql.append("       	A.INVOICE_NO,\n");
		sbSql.append("       	A.INVOICE_NO_VER,\n");
		sbSql.append("          C.SERIES_NAME,\n");
		sbSql.append("          C.MODEL_NAME,\n");
		sbSql.append("          C.PACKAGE_NAME,\n");
		sbSql.append("       	B.DETAIL_ID,\n");
		sbSql.append("          B.DISCOUNT_RATE,\n");
		sbSql.append("          B.SINGLE_PRICE,\n");
		sbSql.append("          B.CHK_PRICE,\n");
		sbSql.append("          A.CHK_NUM,\n");
		sbSql.append("          B.DIS_AMOUNT,\n");
		sbSql.append("          A.REBATE_PRICE,\n");
		sbSql.append("          A.ORDER_YF_PRICE\n");
		sbSql.append(")\n");
		sbSql.append("SELECT * FROM DETAIL D1 WHERE D1.DETAIL_ID = (SELECT D2.DETAIL_ID FROM DETAIL D2 WHERE D1.ORDER_ID = D2.ORDER_ID AND ROWNUM = 1)");
		sbSql.append(" ORDER BY D1.RAISE_DATE ASC");

		return pageQuery(sbSql.toString(), null, getFunName(), pageSize, curPage);
	}

	/**
	 * 方法描述 : 查询订单信息<br/>
	 * 
	 * @param infoMap
	 *            查询参数集
	 * @param curPage
	 *            当前页
	 * @param pageSize
	 *            每页记录数
	 * @return
	 * @author wangsongwei
	 */
	public PageResult<Map<String, Object>> getAllOrderList(Map<String, Object> map, Integer curPage, Integer pageSize) {
		String orderNo = CommonUtils.checkNull(map.get("orderNo")); // 订单编号
		String orgId = CommonUtils.checkNull(map.get("orgId")); // 区域ID
		String orgCode = CommonUtils.checkNull(map.get("orgCode"));
		String dealerCode = CommonUtils.checkNull(map.get("dealerCode")); // 经销商代码
		String dealerId = CommonUtils.checkNull(map.get("dealerId")); // 经销商代码
		String startdate = CommonUtils.checkNull(map.get("startdate")); // 提报日期
		String endDate = CommonUtils.checkNull(map.get("endDate")); // 提报日期
		String cstartdate = CommonUtils.checkNull(map.get("cstartdate")); // 提报日期
		String cendDate = CommonUtils.checkNull(map.get("cendDate")); // 提报日期
		String groupCode = CommonUtils.checkNull(map.get("groupCode")); // 物料组编号
		String orderTypeD = CommonUtils.checkNull(map.get("orderTypeD")); // 单据类型
		String reqStatus = CommonUtils.checkNull(map.get("reqStatus")); // 订单状态
		String expstatus = CommonUtils.checkNull(map.get("expstatus")); // 不需要查询的订单状态
		String reqOrderType = CommonUtils.checkNull(map.get("reqOrderType"));
		String expOrderType = CommonUtils.checkNull(map.get("expOrderType"));
		String orderDjType = CommonUtils.checkNull(map.get("orderDjType"));
		String invoiceNo = CommonUtils.checkNull(map.get("invoiceNo"));
		String poseId = map.get("poseId").toString();

		AclUserBean logonUser = (AclUserBean) map.get("logonUser");

		StringBuffer sbSql = new StringBuffer();
		List par = new ArrayList();
		Integer chooseDealer = logonUser.getChooseDealer() == null ? 0 : logonUser.getChooseDealer();
		sbSql.append("select t.order_id,\r\n");
		sbSql.append("       t.order_no,\r\n");
		sbSql.append("       t.order_type,\r\n");
		sbSql.append("       t.dealer_id,\r\n");
		sbSql.append("       a.dealer_name,\r\n");
		sbSql.append("       a.dealer_shortname,\r\n");
		sbSql.append("       t.fund_type_id,\r\n");
		sbSql.append("       t.order_price,\r\n");
		sbSql.append("       t.order_yf_price,\r\n");
		sbSql.append("       t.deliv_add_id,\r\n");
		sbSql.append("       c.address,\r\n");
		sbSql.append("       nvl(t.chk_num, 0) chk_num,\r\n");
		sbSql.append("       t.order_status\r\n");
		sbSql.append("  from tt_vs_order t, tm_dealer a, tm_vs_address c\r\n");
		sbSql.append(" where t.dealer_id = a.dealer_id\r\n");
		sbSql.append("   and t.deliv_add_id = c.id(+)\r\n");
		sbSql.append("   and t.order_status = 10211004"); 
		if (!orderNo.equals(""))
		{
			sbSql.append("   AND t.ORDER_NO  LIKE '%" + reqStatus + "%'\n");
		}
		return pageQuery(sbSql.toString(), par, getFunName(), pageSize, curPage);
	}

	/**
	 * 方法描述 : 查询订单信息<br/>
	 * 
	 * @param infoMap
	 *            查询参数集
	 * @param curPage
	 *            当前页
	 * @param pageSize
	 *            每页记录数
	 * @return
	 * @author wangsongwei
	 */
	public PageResult<Map<String, Object>> getAllZhongZhuanOrderList(Map<String, Object> map, Integer curPage, Integer pageSize) {
		String orderNo = CommonUtils.checkNull(map.get("orderNo")); // 订单编号
		String orgId = CommonUtils.checkNull(map.get("orgId")); // 区域ID
		String orgCode = CommonUtils.checkNull(map.get("orgCode"));
		String dealerCode = CommonUtils.checkNull(map.get("dealerCode")); // 经销商代码
		String dealerId = CommonUtils.checkNull(map.get("dealerId")); // 经销商代码
		String startdate = CommonUtils.checkNull(map.get("startdate")); // 提报日期
		String endDate = CommonUtils.checkNull(map.get("endDate")); // 提报日期
		String groupCode = CommonUtils.checkNull(map.get("groupCode")); // 物料组编号
		String orderTypeD = CommonUtils.checkNull(map.get("orderTypeD")); // 单据类型
		String reqStatus = CommonUtils.checkNull(map.get("reqStatus")); // 订单状态
		String expstatus = CommonUtils.checkNull(map.get("expstatus")); // 不需要查询的订单状态
		String reqOrderType = CommonUtils.checkNull(map.get("reqOrderType"));
		String expOrderType = CommonUtils.checkNull(map.get("expOrderType"));
		String orderDjType = CommonUtils.checkNull(map.get("orderDjType"));
		String poseId = map.get("poseId").toString();

		AclUserBean logonUser = (AclUserBean) map.get("logonUser");

		StringBuffer sbSql = new StringBuffer();

		sbSql.append("WITH MAT_DETAIL1 AS (\n");
		sbSql.append("     SELECT A1.ORDER_ID," + Constant.ORDER_INVOICE_TYPE_01 + " AS ORDER_TYPE_DJ,\n");
		sbSql.append("            A1.DISCOUNT_RATE,\n");
		sbSql.append("            A1.SINGLE_PRICE,\n");
		sbSql.append("			  SUM(A1.CHECK_AMOUNT) CHK_NUM,\n");
		sbSql.append("            NVL(A1.CHK_PRICE, 0) CHK_PRICE,\n");
		sbSql.append("            SUM(NVL(A1.DIS_AMOUNT, 0)*((100-A1.DISCOUNT_RATE)/100)) DIS_AMOUNT,\n"); // 折后总额
		sbSql.append("            A2.SERIES_NAME,\n");
		sbSql.append("            A2.MODEL_NAME,\n");
		sbSql.append("            A2.PACKAGE_NAME\n");
		sbSql.append("       FROM TT_VS_ORDER_DETAIL A1, VW_MATERIAL_GROUP_MAT A2\n");
		sbSql.append("      WHERE A1.MATERIAL_ID = A2.MATERIAL_ID(+)\n");
		sbSql.append("      GROUP BY A1.ORDER_ID,\n");
		sbSql.append("            A1.DISCOUNT_RATE,\n");
		sbSql.append("            A1.SINGLE_PRICE,\n");
		sbSql.append("            A1.CHK_PRICE,\n");
		sbSql.append("            A2.SERIES_NAME,\n");
		sbSql.append("            A2.MODEL_NAME,\n");
		sbSql.append("            A2.PACKAGE_NAME\n");
		sbSql.append("	   UNION ALL\n");
		sbSql.append("     SELECT A1.ORDER_ID," + Constant.ORDER_INVOICE_TYPE_02 + " AS ORDER_TYPE_DJ,\n");
		sbSql.append("            A1.DISCOUNT_RATE,\n");
		sbSql.append("            A1.SINGLE_PRICE,\n");
		sbSql.append("            SUM(A1.OUT_AMOUNT) CHK_NUM,\n");
		sbSql.append("            NVL(A1.CHK_PRICE, 0) CHK_PRICE,\n");
		sbSql.append("            SUM(NVL(A1.DIS_AMOUNT, 0)*((100-A1.DISCOUNT_RATE)/100)) DIS_AMOUNT,\n");
		sbSql.append("            A2.SERIES_NAME,\n");
		sbSql.append("            A2.MODEL_NAME,\n");
		sbSql.append("            A2.PACKAGE_NAME\n");
		sbSql.append("       FROM TT_OUT_ORDER_DETAIL A1, VW_MATERIAL_GROUP_MAT A2\n");
		sbSql.append("      WHERE A1.MATERIAL_ID = A2.MATERIAL_ID(+)\n");
		sbSql.append("      GROUP BY A1.ORDER_ID,\n");
		sbSql.append("            A1.DISCOUNT_RATE,\n");
		sbSql.append("            A1.SINGLE_PRICE,\n");
		sbSql.append("            A1.CHK_PRICE,\n");
		sbSql.append("            A2.SERIES_NAME,\n");
		sbSql.append("            A2.MODEL_NAME,\n");
		sbSql.append("            A2.PACKAGE_NAME\n");
		sbSql.append(")\n");
		sbSql.append("SELECT " + Constant.ORDER_INVOICE_TYPE_02 + " AS ORDER_TYPE_DJ,\n");
		sbSql.append("       A.ORDER_ID,\n");
		sbSql.append("       " + Constant.ORDER_INVOICE_TYPE_02 + " ORDER_TYPE,\n");
		sbSql.append("       A.ORDER_NO,\n");
		sbSql.append("       A.ORDER_STATUS,\n");
		sbSql.append("       TO_CHAR(A.INVO_DATE,'YYYY-MM-DD') INVO_DATE,\n");
		sbSql.append("       E.AREA_NAME,\n");
		sbSql.append("       D.DEALER_CODE,\n");
		sbSql.append("       D.DEALER_SHORTNAME DEALER_NAME,\n");
		sbSql.append("       TO_CHAR(A.RAISE_DATE, 'YYYY-MM-DD HH24:MI:SS') RAISE_DATE,\n");
		sbSql.append("       A.FUND_TYPE_ID,\n");
		sbSql.append("       A.INVO_TYPE,\n");
		sbSql.append("       A.INVOICE_NO,\n");
		sbSql.append("       A.INVOICE_VER INVOICE_VER,\n");
		sbSql.append("       B.SERIES_NAME,\n");
		sbSql.append("       B.MODEL_NAME,\n");
		sbSql.append("       B.PACKAGE_NAME,\n");
		sbSql.append("       B.DISCOUNT_RATE,\n");
		sbSql.append("       B.SINGLE_PRICE,\n");
		sbSql.append("       ((NVL(B.SINGLE_PRICE,0)*NVL(B.CHK_NUM,0)-NVL(A.ORDER_YF_PRICE, 0)) / (NVL(B.SINGLE_PRICE,0)*NVL(B.CHK_NUM,0)))*100 ZHDIS_RATE,\n");
		sbSql.append("       ((NVL(B.SINGLE_PRICE,0)*NVL(B.CHK_NUM,0)-NVL(A.ORDER_YF_PRICE, 0)) / (NVL(B.SINGLE_PRICE,0)*NVL(B.CHK_NUM,0)))*100 ZHDIS_RATE_CHECK,\n");
		sbSql.append("       NVL(B.CHK_PRICE, 0) CHK_PRICE,\n");
		sbSql.append("       NVL(B.CHK_NUM,0) CHK_NUM,\n");
		sbSql.append("       NVL(B.DIS_AMOUNT, 0) DIS_AMOUNT,\n");
		sbSql.append("       NVL(A.REBATE_PRICE, 0) REBATE_PRICE,\n");
		sbSql.append("       NVL(A.ORDER_YF_PRICE, 0) ORDER_YF_PRICE,\n");
		sbSql.append("       A.ORDER_REMARK,\n");
		sbSql.append("       A.RES_REMARK, -- 资源审核备注\n");
		sbSql.append("       A.TOW_REMARK, -- 二次审核\n");
		sbSql.append("       '' AS DJC_REMARK, -- 代交车审核\n");
		sbSql.append("       A.FIN_REMARK, -- 财务审核\n");
		sbSql.append("       '' AS PLAN_REMARK, -- 计划组板\n");
		sbSql.append("       '' AS PLAN_CHK_REMARK -- 计划组板审核\n");
		sbSql.append("  FROM TT_OUT_ORDER            A,\n");
		sbSql.append("       MAT_DETAIL1             B,\n");
		sbSql.append("       TM_DEALER              D,\n");
		sbSql.append("       TM_BUSINESS_AREA       E,\n");
		sbSql.append("       TM_ORG                 F,\n");
		sbSql.append("       TM_DEALER_ORG_RELATION G\n");
		sbSql.append(" WHERE A.ORDER_ID = B.ORDER_ID\n");
		sbSql.append("	 AND B.ORDER_TYPE_DJ = " + Constant.ORDER_INVOICE_TYPE_02 + "	\n");
		sbSql.append("   AND A.DEALER_ID = D.DEALER_ID(+)\n");
		sbSql.append("   AND A.AREA_ID = E.AREA_ID(+)\n");
		sbSql.append("   AND D.DEALER_ID = G.DEALER_ID(+)\n");
		sbSql.append("   AND G.ORG_ID = F.ORG_ID(+)\n");

		sbSql.append(MaterialGroupManagerDao.getPoseIdBusinessSql("A.AREA_ID", poseId)); // 产地查看权限限制
		sbSql.append(MaterialGroupManagerDao.getOrgDealerLimitSql("A.DEALER_ID", logonUser));// 组织机构限制

		if (!reqStatus.equals(""))
		{
			sbSql.append("   AND A.ORDER_STATUS IN (" + reqStatus + ")\n");
		}
		else if (!expstatus.equals(""))
		{
			sbSql.append("   AND A.ORDER_STATUS NOT IN (" + expstatus + ")\n");
		}

		if (!orgCode.equals(""))
		{
			sbSql.append(MaterialGroupManagerDao.getOrgQuerySql("F.ORG_ID", orgCode));
		}

		if (!dealerCode.equals(""))
		{
			sbSql.append("   AND D.DEALER_CODE IN (" + getSqlQueryCondition(dealerCode) + ")\n");
		}

		if (!startdate.equals(""))
		{
			sbSql.append("   AND A.RAISE_DATE >= TO_DATE('" + startdate + "','YYYY-MM-DD HH24:MI:SS')\n");
		}

		if (!endDate.equals(""))
		{
			sbSql.append("   AND A.RAISE_DATE <= TO_DATE('" + endDate + "','YYYY-MM-DD HH24:MI:SS')\n");
		}

		if (!orderNo.equals(""))
		{
			sbSql.append("       AND A.ORDER_NO LIKE '%" + orderNo + "%'\n");
		}

		if (!groupCode.equals(""))
		{
			sbSql.append(MaterialGroupManagerDao.getGroupQuerySql("A.ORDER_ID", groupCode, Constant.ORDER_INVOICE_TYPE_02.toString()));
		}
		sbSql.append("");

		return pageQuery(sbSql.toString(), null, getFunName(), pageSize, curPage);
	}

	/**
	 * 方法描述 : 查询订单信息<br/>
	 * 
	 * @param infoMap
	 *            查询参数集
	 * @param curPage
	 *            当前页
	 * @param pageSize
	 *            每页记录数
	 * @return
	 * @author wangsongwei
	 */
	public PageResult<Map<String, Object>> getAllResOrderList(Map<String, Object> map, Integer curPage, Integer pageSize) {
		String orderNo = CommonUtils.checkNull(map.get("orderNo")); // 订单编号
		String orgId = CommonUtils.checkNull(map.get("orgId")); // 区域ID
		String orgCode = CommonUtils.checkNull(map.get("orgCode"));
		String dealerCode = CommonUtils.checkNull(map.get("dealerCode")); // 经销商代码
		String dealerId = CommonUtils.checkNull(map.get("dealerId")); // 经销商代码
		String startdate = CommonUtils.checkNull(map.get("startdate")); // 提报日期
		String endDate = CommonUtils.checkNull(map.get("endDate")); // 提报日期
		String groupCode = CommonUtils.checkNull(map.get("groupCode")); // 物料组编号
		String orderTypeD = CommonUtils.checkNull(map.get("orderTypeD")); // 单据类型
		String reqStatus = CommonUtils.checkNull(map.get("reqStatus")); // 订单状态
		String expstatus = CommonUtils.checkNull(map.get("expstatus")); // 不需要查询的订单状态
		String poseId = map.get("poseId").toString();

		AclUserBean logonUser = (AclUserBean) map.get("logonUser");

		StringBuffer sbSql = new StringBuffer();

		sbSql.append("WITH MAT_DETAIL1 AS (\n");
		sbSql.append("     SELECT A1.ORDER_ID,\n");
		sbSql.append("            A1.DISCOUNT_RATE,\n");
		sbSql.append("            A1.SINGLE_PRICE,\n");
		sbSql.append("            NVL(A1.CHK_PRICE, 0) CHK_PRICE,\n");
		sbSql.append("            SUM(NVL(A1.DIS_AMOUNT, 0)*((100-A1.DISCOUNT_RATE)/100)) DIS_AMOUNT,\n"); // 折后总额
		sbSql.append("            A2.SERIES_NAME,\n");
		sbSql.append("            A2.MODEL_NAME,\n");
		sbSql.append("            A2.PACKAGE_CODE,\n");
		sbSql.append("            A2.PACKAGE_NAME\n");
		sbSql.append("       FROM TT_VS_ORDER_DETAIL A1, VW_MATERIAL_GROUP_MAT A2\n");
		sbSql.append("      WHERE A1.MATERIAL_ID = A2.MATERIAL_ID(+)\n");
		sbSql.append("      GROUP BY A1.ORDER_ID,\n");
		sbSql.append("            A1.DISCOUNT_RATE,\n");
		sbSql.append("            A1.SINGLE_PRICE,\n");
		sbSql.append("            A1.CHK_PRICE,\n");
		sbSql.append("            A2.SERIES_NAME,\n");
		sbSql.append("            A2.MODEL_NAME,\n");
		sbSql.append("            A2.PACKAGE_CODE,\n");
		sbSql.append("            A2.PACKAGE_NAME\n");
		sbSql.append(")\n");
		sbSql.append("SELECT " + Constant.ORDER_INVOICE_TYPE_01 + " AS ORDER_TYPE_DJ,\n");
		sbSql.append("       A.ORDER_ID,\n");
		sbSql.append("       A.ORDER_TYPE,\n");
		sbSql.append("       A.ORDER_NO,\n");
		sbSql.append("       A.ORDER_STATUS,\n");
		sbSql.append("       TO_CHAR(A.INVO_DATE,'YYYY-MM-DD') INVO_DATE,\n");
		sbSql.append("       E.AREA_NAME,\n");
		sbSql.append("       D.DEALER_CODE,\n");
		sbSql.append("       D.DEALER_SHORTNAME DEALER_NAME,\n");
		sbSql.append("       TO_CHAR(A.RAISE_DATE, 'YYYY-MM-DD HH24:MI:SS') RAISE_DATE,\n");
		sbSql.append("       A.FUND_TYPE_ID,\n");
		sbSql.append("       A.INVO_TYPE,\n");
		sbSql.append("       A.INVOICE_NO,\n");
		sbSql.append("       A.INVOICE_NO_VER INVOICE_VER,\n");
		sbSql.append("       B.SERIES_NAME,\n");
		sbSql.append("       B.MODEL_NAME,\n");
		sbSql.append("       B.PACKAGE_CODE,\n");
		sbSql.append("       B.PACKAGE_NAME,\n");
		sbSql.append("       B.DISCOUNT_RATE,\n");
		sbSql.append("       B.SINGLE_PRICE,\n");
		sbSql.append("       ((NVL(B.CHK_PRICE,0)*NVL(A.SUB_NUM,0)-NVL(A.ORDER_YF_PRICE, 0)) / (NVL(B.CHK_PRICE,0)*NVL(A.SUB_NUM,0)))*100 ZHDIS_RATE,\n");
		sbSql.append("       NVL(B.CHK_PRICE, 0) CHK_PRICE,\n");
		sbSql.append("       NVL(A.SUB_NUM, 0) CHK_NUM,\n");
		sbSql.append("       NVL(B.DIS_AMOUNT, 0) DIS_AMOUNT,\n");
		sbSql.append("       NVL(A.REBATE_PRICE, 0) REBATE_PRICE,\n");
		sbSql.append("       NVL(A.ORDER_YF_PRICE, 0) ORDER_YF_PRICE,\n");
		sbSql.append("       A.ORDER_REMARK,\n");
		sbSql.append("       A.RES_REMARK, -- 资源审核备注\n");
		sbSql.append("       A.TOW_REMARK, -- 二次审核\n");
		sbSql.append("       A.DJC_REMARK, -- 代交车审核\n");
		sbSql.append("       A.FIN_REMARK, -- 财务审核\n");
		sbSql.append("       A.PLAN_REMARK, -- 计划组板\n");
		sbSql.append("       A.PLAN_CHK_REMARK -- 计划组板审核\n");
		sbSql.append("  FROM TT_VS_ORDER            A,\n");
		sbSql.append("       MAT_DETAIL1            B,\n");
		sbSql.append("       TM_DEALER              D,\n");
		sbSql.append("       TM_BUSINESS_AREA       E,\n");
		sbSql.append("       TM_ORG                 F,\n");
		sbSql.append("       TM_DEALER_ORG_RELATION G\n");
		sbSql.append(" WHERE A.ORDER_ID = B.ORDER_ID\n");
		sbSql.append("   AND A.DEALER_ID = D.DEALER_ID(+)\n");
		sbSql.append("   AND A.AREA_ID = E.AREA_ID(+)\n");
		sbSql.append("   AND D.DEALER_ID = G.DEALER_ID(+)\n");
		sbSql.append("   AND G.ORG_ID = F.ORG_ID(+)\n");
		sbSql.append("AND NOT EXISTS(\n");
		sbSql.append("SELECT 1\n");
		sbSql.append("  FROM TT_VS_ORDER                  TVO,\n");
		sbSql.append("       TT_VS_ORDER_DETAIL           TVOD,\n");
		sbSql.append("       TT_VS_ORDER_RESOURCE_RESERVE ORR\n");
		sbSql.append(" WHERE TVO.ORDER_ID = A.ORDER_ID\n");
		sbSql.append("   AND TVO.ORDER_ID = TVOD.ORDER_ID\n");
		sbSql.append("   AND TVOD.DETAIL_ID = ORR.ORDER_DETAIL_ID\n");
		sbSql.append("   AND NVL(ORR.ALLOCA_NUM, 0) <> 0\n");
		sbSql.append(")");

		sbSql.append(MaterialGroupManagerDao.getPoseIdBusinessSql("A.AREA_ID", poseId)); // 产地查看权限限制
		sbSql.append(MaterialGroupManagerDao.getOrgDealerLimitSql("A.DEALER_ID", logonUser));// 组织机构限制

		if (!reqStatus.equals(""))
		{
			sbSql.append("   AND A.ORDER_STATUS IN (" + reqStatus + ")\n");
		}
		else if (!expstatus.equals(""))
		{
			sbSql.append("   AND A.ORDER_STATUS NOT IN (" + expstatus + ")\n");
		}

		if (!orgCode.equals(""))
		{
			sbSql.append(MaterialGroupManagerDao.getOrgQuerySql("F.ORG_ID", orgCode));
		}

		if (!dealerCode.equals(""))
		{
			sbSql.append("   AND D.DEALER_CODE IN (" + getSqlQueryCondition(dealerCode) + ")\n");
		}

		if (!startdate.equals(""))
		{
			sbSql.append("   AND A.RAISE_DATE >= TO_DATE('" + startdate + "','YYYY-MM-DD HH24:MI:SS')\n");
		}

		if (!endDate.equals(""))
		{
			sbSql.append("   AND A.RAISE_DATE <= TO_DATE('" + endDate + "','YYYY-MM-DD HH24:MI:SS')\n");
		}

		if (!orderNo.equals(""))
		{
			sbSql.append("       AND A.ORDER_NO LIKE '%" + orderNo + "%'\n");
		}

		if (!groupCode.equals(""))
		{
			sbSql.append(MaterialGroupManagerDao.getGroupQuerySql("A.ORDER_ID", groupCode, Constant.ORDER_INVOICE_TYPE_01.toString()));
		}

		return pageQuery(sbSql.toString(), null, getFunName(), pageSize, curPage);
	}

	/**
	 * 方法描述 : 查询已提报订单的物料详细数据
	 * 
	 * @param orderId
	 *            提车单ID
	 * @return
	 * @author wangsongwei
	 */
	public List<Map<String, Object>> getOrderMaterialDetailList(String orderId) throws Exception {
		List<Object> params = new ArrayList<Object>();
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("\r\n");
		sbSql.append("WITH ORDER_DETAIL AS\r\n");
		sbSql.append(" (SELECT TVOD.ORDER_ID,\r\n");
		sbSql.append("         TVOD.DETAIL_ID,\r\n");
		sbSql.append("         TVOD.MATERIAL_ID,\r\n");
		sbSql.append("         TVM.MATERIAL_CODE,\r\n");
		sbSql.append("         TVM.MATERIAL_NAME,\r\n");
		sbSql.append("         TVM.COLOR_NAME,\r\n");
		sbSql.append("         TVOD.SINGLE_PRICE,\r\n");
		sbSql.append("         TVOD.DISCOUNT_S_PRICE,\r\n");
		sbSql.append("         TVOD.DISCOUNT_RATE,\r\n");
		sbSql.append("         TVOD.DISCOUNT_PRICE,\r\n");
		sbSql.append("         TVOD.TOTAL_PRICE,\r\n");
		sbSql.append("         NVL(TVOD.ORDER_AMOUNT,0) ORDER_AMOUNT,\r\n");
		sbSql.append("         NVL(TVOD.CHECK_AMOUNT,0) CHECK_AMOUNT,\r\n");
		sbSql.append("         NVL(TVM.VHCL_PRICE, 0) +\r\n");
		sbSql.append("         NVL((SELECT P2.AMOUNT\r\n");
		sbSql.append("               FROM TM_REGION P1, TT_SALES_AREA_PRICE P2, TM_DEALER P3\r\n");
		sbSql.append("              WHERE P1.REGION_CODE = P2.AREA_ID\r\n");
		sbSql.append("                AND P1.REGION_CODE = P3.PROVINCE_ID\r\n");
		sbSql.append("                AND P3.DEALER_ID = TVO.DEALER_ID\r\n");
		sbSql.append("                AND P2.YIELDLY = TVO.AREA_ID\r\n");
		sbSql.append("                AND P1.REGION_TYPE = 10541002\r\n");
		sbSql.append("                AND P2.STATUS = 10011001),\r\n");
		sbSql.append("             0) C_PRICE,\r\n");
		sbSql.append("         NVL((SELECT TSFD.DIS_VALUE\r\n");
		sbSql.append("               FROM TT_SALES_FIN_DIS TSFD\r\n");
		sbSql.append("              WHERE TSFD.YIELDLY = TVO.AREA_ID\r\n");
		sbSql.append("                AND TSFD.FIN_TYPE = TVO.FUND_TYPE_ID),\r\n");
		sbSql.append("             0) DIS_VALUE,\r\n");
		sbSql.append("         VMT.SERIES_ID,\r\n");
		sbSql.append("         VMT.SERIES_NAME,\r\n");
		sbSql.append("         VMT.MODEL_NAME,\r\n");
		sbSql.append("         VMT.PACKAGE_NAME,\r\n");
		sbSql.append("         VMT.PACKAGE_CODE\r\n");
		sbSql.append("    FROM TT_VS_ORDER       TVO,\r\n");
		sbSql.append("         TT_VS_ORDER_DETAIL TVOD,\r\n");
		sbSql.append("         TM_VHCL_MATERIAL          TVM,\r\n");
		sbSql.append("         VW_MATERIAL_GROUP_MAT     VMT\r\n");
		sbSql.append("   WHERE TVO.ORDER_ID = TVOD.ORDER_ID\r\n");
		sbSql.append("     AND TVOD.MATERIAL_ID = TVM.MATERIAL_ID\r\n");
		sbSql.append("     AND TVOD.MATERIAL_ID = VMT.MATERIAL_ID)\r\n");
		sbSql.append("SELECT D.ORDER_ID,\r\n");
		sbSql.append("       D.DETAIL_ID,\r\n");
		sbSql.append("       D.MATERIAL_ID,\r\n");
		sbSql.append("       D.MATERIAL_CODE,\r\n");
		sbSql.append("       D.MATERIAL_NAME,\r\n");
		sbSql.append("       D.COLOR_NAME,\r\n");
		sbSql.append("       D.ORDER_AMOUNT,\r\n");
		sbSql.append("       D.SINGLE_PRICE,\r\n");
		sbSql.append("       D.DISCOUNT_S_PRICE,\r\n");
		sbSql.append("       D.DISCOUNT_RATE,\r\n");
		sbSql.append("       D.DISCOUNT_PRICE,\r\n");
		sbSql.append("       D.TOTAL_PRICE,\r\n");
		sbSql.append("       D.C_PRICE,\r\n");
		sbSql.append("       D.DIS_VALUE,\r\n");
		sbSql.append("       D.ORDER_AMOUNT,\r\n");
		sbSql.append("       D.CHECK_AMOUNT,\r\n");
		sbSql.append("       D.SERIES_ID,\r\n");
		sbSql.append("       D.SERIES_NAME,\r\n");
		sbSql.append("       D.MODEL_NAME,\r\n");
		sbSql.append("       D.PACKAGE_NAME,\r\n");
		sbSql.append("       D.PACKAGE_CODE,\r\n");
		sbSql.append("       1 RESOURCE_AMOUNT_STATUS\r\n");
		sbSql.append("  FROM ORDER_DETAIL D\r\n");
		sbSql.append(" WHERE D.ORDER_ID = ?"); 
		sbSql.append("		 ORDER BY D.MATERIAL_ID  ASC\r\n"); 
		params.add(orderId);
		return dao.pageQuery(sbSql.toString(), params, getFunName());
	}

	// /**
	// * 方法描述 ： 查询提车单的物料明细数据 <br/>
	// *
	// * @param orderId
	// * @return
	// * @throws Exception
	// * @author wangsongwei
	// */
	// public List<Map<String, Object>> getOrderMaterialSimpleInfoList(String
	// orderId) throws Exception
	// {
	// List<Map<String, Object>> list = null;
	//
	// try
	// {
	// StringBuffer sbSql = new StringBuffer();
	//
	// sbSql.append("\n -- 订单物料明细查询 包括发运的数量\n");
	// sbSql.append("SELECT                          \n");
	// sbSql.append("   TVOD.DETAIL_ID,              \n");
	// sbSql.append("   TVMG.GROUP_ID,               \n");
	// sbSql.append("   TVMG.GROUP_NAME,             \n");
	// sbSql.append("   TVM.MATERIAL_CODE,           \n");
	// sbSql.append("   TVM.MATERIAL_NAME,           \n");
	// sbSql.append("   NVL(TVOD.ORDER_AMOUNT,0) ORDER_AMOUNT,           -- 提报数量 \n");
	// sbSql.append("   NVL(TVOD.CHECK_AMOUNT,0) CHECK_AMOUNT,           -- 审核数量 \n");
	// sbSql.append("   NVL(TVOD.DELIVERY_AMOUNT,0) DELIVERY_AMOUNT         -- 发运数量 \n");
	// sbSql.append("FROM                                        \n");
	// sbSql.append("   TT_VS_ORDER TVO,                         \n");
	// sbSql.append("   TT_VS_ORDER_DETAIL TVOD,                 \n");
	// sbSql.append("   TM_VHCL_MATERIAL TVM,                    \n");
	// sbSql.append("   TM_VHCL_MATERIAL_GROUP TVMG,             \n");
	// sbSql.append("   TM_VHCL_MATERIAL_GROUP_R TVMGR           \n");
	// sbSql.append("WHERE                                       \n");
	// sbSql.append("   TVOD.MATERIAL_ID = TVM.MATERIAL_ID       \n");
	// sbSql.append("   AND                                      \n");
	// sbSql.append("   TVM.MATERIAL_ID = TVMGR.MATERIAL_ID      \n");
	// sbSql.append("   AND                                      \n");
	// sbSql.append("   TVMGR.GROUP_ID = TVMG.GROUP_ID           \n");
	// sbSql.append("   AND                                      \n");
	// sbSql.append("   TVOD.ORDER_ID = TVO.ORDER_ID");
	// sbSql.append("   AND                                      \n");
	// sbSql.append("   TVO.ORDER_ID = " + orderId);
	//
	// list = dao.pageQuery(sbSql.toString(), null, getFunName());
	// }
	// catch (Exception ex)
	// {
	// throw ex;
	// }
	//
	// return list;
	// }

	/**
	 * 方法描述 : 查询已提报订单的物料详细数据 伴随预测数量一起
	 * 
	 * @param orderId
	 *            提车单ID
	 * @return
	 * @author wangsongwei
	 */
	public List<Map<String, Object>> getOrderMaterialDetailWithCheckList(String orderId) throws Exception {

		TtVsOrderPO tvop_sql = new TtVsOrderPO();
		tvop_sql.setOrderId(Long.valueOf(orderId));
		List<?> tvopList = dao.select(tvop_sql);
		StringBuffer sbSql = new StringBuffer();
		if (tvopList.size() > 0)
		{
			TtVsOrderPO tvop = (TtVsOrderPO) tvopList.get(0);
			sbSql.append("WITH ORDER_RESOURCE AS\r\n");
			sbSql.append(" (SELECT SUM(A.AMOUNT) CHECK_AMOUNT, B.DETAIL_ID\r\n");
			sbSql.append("    FROM TT_VS_ORDER_RESOURCE_RESERVE A, TT_VS_ORDER_DETAIL B, TT_VS_ORDER C\r\n");
			sbSql.append("   WHERE A.ORDER_DETAIL_ID = B.DETAIL_ID\r\n");
			sbSql.append("     AND B.ORDER_ID = C.ORDER_ID\r\n");
			sbSql.append("     AND A.RESERVE_STATUS = 11581001\r\n");
			sbSql.append("   GROUP BY B.DETAIL_ID),\r\n");
			sbSql.append("VEHICLE_STOCK_ALL AS\r\n");
			sbSql.append("(\r\n");
			sbSql.append(" --根据物料码获取可用的资源\r\n");
			sbSql.append(" SELECT A.MATERIAL_ID,\r\n");
			sbSql.append("         NVL(A.STOCK_AMOUNT, 0) - NVL(B.RESAVE_AMOUNT, 0) -\r\n");
			sbSql.append("         NVL(C.LOCK_AMOUNT, 0) RESOURCE_AMOUNT\r\n");
			sbSql.append("   FROM (SELECT TV.MATERIAL_ID, COUNT(1) STOCK_AMOUNT\r\n");
			sbSql.append("            FROM TM_VEHICLE TV\r\n");
			sbSql.append("           WHERE TV.LIFE_CYCLE IN (10321002,10321008,10321009)\r\n");
			sbSql.append("             AND TV.LOCK_STATUS IN (10241001,10241008)\r\n");
			sbSql.append("           GROUP BY TV.MATERIAL_ID) A, -- 车厂正常库存的汇总数\r\n");
			sbSql.append("         (SELECT TVORR.MATERIAL_ID,\r\n");
			sbSql.append("                 SUM(NVL(TVORR.AMOUNT, 0) - NVL(TVORR.ALLOCA_NUM, 0)) RESAVE_AMOUNT\r\n");
			sbSql.append("            FROM TT_VS_ORDER_RESOURCE_RESERVE TVORR\r\n");
			sbSql.append("           GROUP BY TVORR.MATERIAL_ID) B, -- 提车单的资源预留汇总数 = 总数 - 已配车数\r\n");
			sbSql.append("         (SELECT TCRK.MATERIAL_ID,\r\n");
			sbSql.append("                 TCRK.KEEP_ID,\r\n");
			sbSql.append("                 SUM(NVL(TCRK.NUM, 0)) LOCK_AMOUNT\r\n");
			sbSql.append("            FROM TT_COMP_RES_KEEP TCRK\r\n");
			sbSql.append("           WHERE TCRK.NUM <> 0\r\n");
			sbSql.append("           GROUP BY TCRK.MATERIAL_ID, TCRK.KEEP_ID) C\r\n");
			sbSql.append("  WHERE A.MATERIAL_ID = B.MATERIAL_ID(+)\r\n");
			sbSql.append("    AND A.MATERIAL_ID = C.MATERIAL_ID(+)\r\n");
			sbSql.append("    )"); 
			sbSql.append("SELECT TVOD.DETAIL_ID, -- 订单明细表ID\r\n");
			sbSql.append("       TVOD.MATERIAL_ID, -- 物料ID\r\n");
			sbSql.append("       TVM.MATERIAL_CODE, -- 物料CODE\r\n");
			sbSql.append("       TVM.MATERIAL_NAME, -- 物料名称\r\n");
			sbSql.append("       TVM.COLOR_NAME, -- 物料颜色\r\n");
			sbSql.append("       NVL(TVOD.ORDER_AMOUNT, 0) SUB_NUM, -- 提交数量\r\n");
			sbSql.append("       NVL(ORDER_RESOURCE.CHECK_AMOUNT, 0) CHECK_AMOUNT, -- 审核数量\r\n");
			sbSql.append("       NVL((SELECT T2.FORECAST_AMOUNT\r\n");
			sbSql.append("             FROM TT_VS_MONTHLY_FORECAST        T1,\r\n");
			sbSql.append("                  TT_VS_MONTHLY_FORECAST_DETAIL T2\r\n");
			sbSql.append("            WHERE T1.AREA_ID = TVO.AREA_ID\r\n");
			sbSql.append("              AND T2.FORECAST_ID = T1.FORECAST_ID\r\n");
			sbSql.append("              AND T2.MATERIAL_ID = TVOD.MATERIAL_ID\r\n");
			sbSql.append("              AND T1.DEALER_ID = TVO.DEALER_ID\r\n");
			sbSql.append("              AND TO_NUMBER(TO_CHAR(TVO.RAISE_DATE, 'yyyy')) =\r\n");
			sbSql.append("                  T1.FORECAST_YEAR\r\n");
			sbSql.append("              AND TO_NUMBER(TO_CHAR(TVO.RAISE_DATE, 'mm')) =\r\n");
			sbSql.append("                  T1.FORECAST_MONTH),\r\n");
			sbSql.append("           0) PRE_NUM, -- 预测数量\r\n");
			sbSql.append("       NVL((SELECT COUNT(TVO1.ORDER_ID)\r\n");
			sbSql.append("             FROM TT_VS_ORDER TVO1, TT_VS_ORDER_DETAIL TVOD1\r\n");
			sbSql.append("            WHERE TVO1.DEALER_ID = TVO.DEALER_ID\r\n");
			sbSql.append("              AND TVO1.ORDER_ID = TVOD1.ORDER_ID\r\n");
			sbSql.append("              AND TVO1.AREA_ID = TVO.AREA_ID\r\n");
			sbSql.append("              AND TVOD1.MATERIAL_ID = TVOD.MATERIAL_ID\r\n");
			sbSql.append("              AND TO_CHAR(TVO1.RAISE_DATE, 'yyyy-mm') =\r\n");
			sbSql.append("                  TO_CHAR(SYSDATE, 'yyyy-mm')),\r\n");
			sbSql.append("           0) INVO_NUM1, -- 启票数量    NVL(TVOD.CHECK_AMOUNT,0)         CHECK_AMOUNT,        -- 审核数量\r\n");
			sbSql.append("       NVL((SELECT SUM(NVL(T.CHK_NUM, 0)) CHK_NUM\r\n");
			sbSql.append("             FROM TT_VS_ORDER T, TT_VS_ORDER_DETAIL T1\r\n");
			sbSql.append("            WHERE T.ORDER_ID = TVO.ORDER_ID\r\n");
			sbSql.append("              AND T.ORDER_ID = T1.ORDER_ID\r\n");
			sbSql.append("              AND T1.MATERIAL_ID = TVOD.MATERIAL_ID\r\n");
			sbSql.append("            GROUP BY T.ORDER_ID, T1.MATERIAL_ID),\r\n");
			sbSql.append("           0) INVO_NUM, -- 已审核数量\r\n");
			sbSql.append("       NVL(TVOD.SINGLE_PRICE, 0) SINGLE_PRICE, -- 物料单价\r\n");
			sbSql.append("       NVL(TVOD.CHK_PRICE, 0) CHK_PRICE, -- 物料审核单价\r\n");
			sbSql.append("       NVL(TVOD.DISCOUNT_S_PRICE, 0) DISCOUNT_S_PRICE, -- 物料折后单价\r\n");
			sbSql.append("       NVL(TVOD.DISCOUNT_RATE, 0) DISCOUNT_RATE, -- 物料折扣率\r\n");
			sbSql.append("       NVL(TVOD.DISCOUNT_PRICE, 0) DISCOUNT_PRICE, -- 物料折扣额\r\n");
			sbSql.append("       NVL(TVOD.TOTAL_PRICE, 0) TOTAL_PRICE, -- 物料订单总价\r\n");
			sbSql.append("       NVL(TVM.VHCL_PRICE, 0) +\r\n");
			sbSql.append("       NVL((SELECT TSAP.AMOUNT\r\n");
			sbSql.append("             FROM TM_REGION TR, TT_SALES_AREA_PRICE TSAP, TM_DEALER TD\r\n");
			sbSql.append("            WHERE TR.REGION_CODE = TSAP.AREA_ID(+)\r\n");
			sbSql.append("              AND TR.REGION_TYPE = 10541002\r\n");
			sbSql.append("              AND TSAP.STATUS = 10011001\r\n");
			sbSql.append("              AND TD.PROVINCE_ID = TR.REGION_CODE\r\n");
			sbSql.append("              AND TD.DEALER_ID = TVO.DEALER_ID\r\n");
			sbSql.append("              AND TSAP.YIELDLY = TVO.AREA_ID),\r\n");
			sbSql.append("           0) C_PRICE, -- 物料目前的标准价格\r\n");
			sbSql.append("       NVL((SELECT TSD.DIS_VALUE\r\n");
			sbSql.append("             FROM TT_SALES_DIS TSD\r\n");
			sbSql.append("            WHERE 1 = 1\r\n");
			sbSql.append("              AND TSD.GROUP_ID = VMG.SERIES_ID\r\n");
			sbSql.append("              AND TSD.YIELDLY = TVO.AREA_ID AND TSD.DEALER_ID=TVO.DEALER_ID),\r\n");
			sbSql.append("           0) + NVL((SELECT TSFD.DIS_VALUE\r\n");
			sbSql.append("                      FROM TT_SALES_FIN_DIS TSFD\r\n");
			sbSql.append("                     WHERE 1 = 1\r\n");
			sbSql.append("                       AND TSFD.FIN_TYPE = TVO.FUND_TYPE_ID\r\n");
			sbSql.append("                       AND TSFD.YIELDLY = TVO.AREA_ID),\r\n");
			sbSql.append("                    0) DIS_VALUE, -- 物料目前的标准折扣率\r\n");
			sbSql.append("       VMG.SERIES_ID, -- 车系ID\r\n");
			sbSql.append("       VMG.SERIES_NAME, -- 车系\r\n");
			sbSql.append("       VMG.MODEL_NAME, -- 车型\r\n");
			sbSql.append("       NVL(VVOR.RESOURCE_AMOUNT, 0) + NVL(TVOD.CHECK_AMOUNT, 0) RESOURCE_AMOUNT, -- 物料可用数量即车辆的数量\r\n");
			sbSql.append("       -- 可用数量统计\r\n");
			sbSql.append("       (CASE\r\n");
			sbSql.append("         WHEN NVL(VVOR.RESOURCE_AMOUNT, 0) + NVL(TVOD.CHECK_AMOUNT, 0) > 20 THEN\r\n");
			sbSql.append("          '有'\r\n");
			sbSql.append("         WHEN NVL(VVOR.RESOURCE_AMOUNT, 0) + NVL(TVOD.CHECK_AMOUNT, 0) < 0 THEN\r\n");
			sbSql.append("          '无'\r\n");
			sbSql.append("         ELSE\r\n");
			sbSql.append("          TO_CHAR(NVL(VVOR.RESOURCE_AMOUNT, 0) + NVL(TVOD.CHECK_AMOUNT, 0))\r\n");
			sbSql.append("       END) RESOURCE_AMOUNT_STATUS\r\n");
			sbSql.append("  FROM TT_VS_ORDER TVO,\r\n");
			sbSql.append("\r\n");
			sbSql.append("       TT_VS_ORDER_DETAIL TVOD,\r\n");
			sbSql.append("\r\n");
			sbSql.append("       TM_VHCL_MATERIAL         TVM,\r\n");
			sbSql.append("       VW_MATERIAL_GROUP        VMG,\r\n");
			sbSql.append("       VEHICLE_STOCK_ALL        VVOR,\r\n");
			sbSql.append("       ORDER_RESOURCE,\r\n");
			sbSql.append("       TM_VHCL_MATERIAL_GROUP_R TVMGR\r\n");
			sbSql.append(" WHERE TVOD.MATERIAL_ID = TVM.MATERIAL_ID(+)\r\n");
			sbSql.append("   AND TVOD.ORDER_ID = TVO.ORDER_ID\r\n");
			sbSql.append("   AND TVM.MATERIAL_ID = TVMGR.MATERIAL_ID(+)\r\n");
			sbSql.append("   AND TVMGR.GROUP_ID = VMG.PACKAGE_ID(+)\r\n");
			sbSql.append("   AND TVM.MATERIAL_ID = VVOR.MATERIAL_ID(+)\r\n");
			sbSql.append("   AND TVOD.DETAIL_ID = ORDER_RESOURCE.DETAIL_ID(+)\r\n");
			sbSql.append("\r\n");
			sbSql.append("   AND TVO.ORDER_ID = "+orderId+"\r\n"); 
		}
		return dao.pageQuery(sbSql.toString(), null, getFunName());
	}

	/**
	 * 方法描述 ： 物料省份分配详情 - 管理组织机构TM_ORG
	 * <p>
	 * 资源数量 = 保留数量 - 已配车数量
	 * 
	 * @param materialId
	 *            物料ID
	 * @return
	 * @author wangsongwei
	 */
	public List<Map<String, Object>> getOrderMaterialPriv(String materialId, String orderDetailId, String poseId) throws Exception {
		List<Object> params = new ArrayList<Object>();

		StringBuffer sbSql = new StringBuffer();

		sbSql.append("SELECT TVO.ORDER_TYPE FROM TT_VS_ORDER_DETAIL TVOD, TT_VS_ORDER TVO WHERE TVO.ORDER_ID = TVOD.ORDER_ID AND TVOD.DETAIL_ID = ? GROUP BY TVO.ORDER_TYPE");

		params.add(orderDetailId);

		List<Map<String, Object>> mapList = dao.pageQuery(sbSql.toString(), params, getFunName());

		Map<String, Object> map = mapList.get(0);
		String orderType = map.get("ORDER_TYPE").toString();

		sbSql.delete(0, sbSql.length());
		sbSql.append("-- 计划资源分配\n");
		sbSql.append("WITH VEHICLE_STOCK_ALL AS\n");
		sbSql.append("(\n");
		sbSql.append("    -- 车厂未出库的库存，已经按照省份和订做车类型进行了分组\n");
		sbSql.append("    -- 注意：带入查询条件需要加入产地查询权限\n");
		sbSql.append("    -- START BY 销售部常规生产车\n");
		sbSql.append("    SELECT\n");
		sbSql.append("           1 AS PRODUCT_TYPE,\n");
		sbSql.append("           TV.MATERIAL_ID,\n");
		sbSql.append("           -1 AS PLAN_DETAIL_ID,\n");
		sbSql.append("           TV.YIELDLY,\n");
		sbSql.append("           TPD.ORG_ID,\n");
		sbSql.append("           COUNT(TV.VEHICLE_ID) STOCK_AMOUNT -- 物料ID、车辆统计\n");
		sbSql.append("      FROM TM_VEHICLE TV, TM_PLAN_DETAIL TPD\n");
		sbSql.append("     WHERE TV.PLAN_DETAIL_ID = TPD.PLAN_DETAIL_ID\n");
		sbSql.append("       AND TV.LIFE_CYCLE = 10321002 -- 车厂库存（生命周期 - TC_CODE）\n");
		sbSql.append("       AND TV.LOCK_STATUS = 10241001 -- 正常状态和配车锁定状态\n");
		sbSql.append("       AND (TPD.IS_FLEET IS NULL OR TPD.IS_FLEET = 10041002)\n");
		sbSql.append("       AND TPD.ORG_ID = -2222222222\n");
		sbSql.append("     GROUP BY TV.MATERIAL_ID, TPD.ORG_ID, TPD.PLAN_DETAIL_ID, TV.YIELDLY\n");
		sbSql.append("    UNION ALL -- START BY 省份常规生产车\n");
		sbSql.append("    SELECT\n");
		sbSql.append("           1 AS PRODUCT_TYPE,\n");
		sbSql.append("           TV.MATERIAL_ID,\n");
		sbSql.append("           -1 AS PLAN_DETAIL_ID,\n");
		sbSql.append("           TV.YIELDLY,\n");
		sbSql.append("           TPD.ORG_ID,\n");
		sbSql.append("           COUNT(TV.VEHICLE_ID) STOCK_AMOUNT -- 物料ID、车辆统计\n");
		sbSql.append("      FROM TM_VEHICLE TV, TM_PLAN_DETAIL TPD\n");
		sbSql.append("     WHERE TV.PLAN_DETAIL_ID = TPD.PLAN_DETAIL_ID\n");
		sbSql.append("       AND TV.LIFE_CYCLE = 10321002 --车厂库存（生命周期 - TC_CODE）\n");
		sbSql.append("       AND TV.LOCK_STATUS = 10241001 --正常状态（锁定状态 - TC_CODE）\n");
		sbSql.append("       AND (TPD.IS_FLEET IS NULL OR TPD.IS_FLEET = 10041002)\n");
		sbSql.append("       AND TPD.ORG_ID <> -2222222222\n");
		sbSql.append("     GROUP BY TV.MATERIAL_ID, TPD.ORG_ID, TPD.PLAN_DETAIL_ID, TV.YIELDLY\n");
		sbSql.append("    UNION ALL -- START BY 省份集团客户订做车\n");
		sbSql.append("    SELECT\n");
		sbSql.append("           -1 AS PRODUCT_TYPE,\n");
		sbSql.append("           TV.MATERIAL_ID,\n");
		sbSql.append("           TPD.PLAN_DETAIL_ID,\n");
		sbSql.append("           TV.YIELDLY,\n");
		sbSql.append("           TPD.ORG_ID,\n");
		sbSql.append("           COUNT(TV.VEHICLE_ID) STOCK_AMOUNT -- 物料ID、车辆统计\n");
		sbSql.append("      FROM TM_VEHICLE TV, TM_PLAN_DETAIL TPD\n");
		sbSql.append("     WHERE TV.PLAN_DETAIL_ID = TPD.PLAN_DETAIL_ID\n");
		sbSql.append("       AND TV.LIFE_CYCLE = 10321002 --车厂库存（生命周期 - TC_CODE）\n");
		sbSql.append("       AND TV.LOCK_STATUS = 10241001 --正常状态（锁定状态 - TC_CODE）\n");
		sbSql.append("       AND TPD.IS_FLEET = 10041001\n");
		sbSql.append("     GROUP BY TV.MATERIAL_ID, TPD.ORG_ID, TPD.PLAN_DETAIL_ID, TV.YIELDLY\n");
		sbSql.append("),\n");
		sbSql.append("COMP_KEEP AS (\n");
		sbSql.append("     SELECT 1 AS PRODUCT_TYPE,\n");
		sbSql.append("            -1 AS PLAN_DETAIL_ID,\n");
		sbSql.append("            H.MATERIAL_ID,\n");
		sbSql.append("            H.ORG_ID,\n");
		sbSql.append("            NVL(H.NUM,0) NUM\n");
		sbSql.append("       FROM TT_COMP_RES_KEEP H\n");
		sbSql.append("),\n");
		sbSql.append("ORDER_RESOURCE_SUB AS(\n");
		sbSql.append("     SELECT 1 AS PRODUCT_TYPE,\n");
		sbSql.append("            -1 AS PLAN_DETAIL_ID,\n");
		sbSql.append("            TVOD.MATERIAL_ID,\n");
		sbSql.append("            SUM(TVORR.AMOUNT) ORDER_AMOUNT,\n");
		sbSql.append("            TVORR.ORG_ID\n");
		sbSql.append("       FROM TT_VS_ORDER_DETAIL TVOD, TT_VS_ORDER_RESOURCE_RESERVE TVORR\n");
		sbSql.append("      WHERE TVOD.DETAIL_ID = TVORR.ORDER_DETAIL_ID\n");
		sbSql.append("        AND TVORR.CREATE_ID IS NULL\n");
		sbSql.append("        AND TVORR.RESERVE_STATUS = 11581001\n");
		sbSql.append("        AND TVOD.DETAIL_ID = ?\n");
		sbSql.append("      GROUP BY TVOD.MATERIAL_ID,TVORR.ORG_ID\n");
		sbSql.append("     UNION ALL\n");
		sbSql.append("     SELECT -1 AS PRODUCT_TYPE,\n");
		sbSql.append("            TVORR.CREATE_ID AS PLAN_DETAIL_ID,\n");
		sbSql.append("            TVOD.MATERIAL_ID,\n");
		sbSql.append("            TVORR.AMOUNT ORDER_AMOUNT,\n");
		sbSql.append("            TVORR.ORG_ID\n");
		sbSql.append("       FROM TT_VS_ORDER_DETAIL TVOD, TT_VS_ORDER_RESOURCE_RESERVE TVORR\n");
		sbSql.append("      WHERE TVOD.DETAIL_ID = TVORR.ORDER_DETAIL_ID\n");
		sbSql.append("        AND TVORR.CREATE_ID IS NOT NULL\n");
		sbSql.append("        AND TVORR.RESERVE_STATUS = 11581001\n");
		sbSql.append("        AND TVOD.DETAIL_ID = ?\n");
		sbSql.append("),\n");
		sbSql.append("ORDER_RESOURCE_SUB_ALL AS(\n");
		sbSql.append("     SELECT 1 AS PRODUCT_TYPE,\n");
		sbSql.append("            -1 AS PLAN_DETAIL_ID,\n");
		sbSql.append("            TVOD.MATERIAL_ID,\n");
		sbSql.append("            SUM(NVL(TVORR.AMOUNT,0) - NVL(TVORR.ALLOCA_NUM,0)) ORDER_AMOUNT,\n");
		sbSql.append("            TVORR.ORG_ID\n");
		sbSql.append("       FROM TT_VS_ORDER_DETAIL TVOD, TT_VS_ORDER_RESOURCE_RESERVE TVORR\n");
		sbSql.append("      WHERE TVOD.DETAIL_ID = TVORR.ORDER_DETAIL_ID\n");
		sbSql.append("        AND TVORR.CREATE_ID IS NULL\n");
		sbSql.append("        AND TVORR.RESERVE_STATUS = 11581001\n");
		sbSql.append("      GROUP BY TVOD.MATERIAL_ID,TVORR.ORG_ID\n");
		sbSql.append("     UNION ALL\n");
		sbSql.append("     SELECT -1 AS PRODUCT_TYPE,\n");
		sbSql.append("            TVORR.CREATE_ID AS PLAN_DETAIL_ID,\n");
		sbSql.append("            TVOD.MATERIAL_ID,\n");
		sbSql.append("            (NVL(TVORR.AMOUNT,0) - NVL(TVORR.ALLOCA_NUM,0)) ORDER_AMOUNT,\n");
		sbSql.append("            TVORR.ORG_ID\n");
		sbSql.append("       FROM TT_VS_ORDER_DETAIL TVOD, TT_VS_ORDER_RESOURCE_RESERVE TVORR\n");
		sbSql.append("      WHERE TVOD.DETAIL_ID = TVORR.ORDER_DETAIL_ID\n");
		sbSql.append("        AND TVORR.CREATE_ID IS NOT NULL\n");
		sbSql.append("        AND TVORR.RESERVE_STATUS = 11581001\n");
		sbSql.append("		  AND TVOD.DETAIL_ID = ?\n");
		sbSql.append("	    GROUP BY TVORR.CREATE_ID, TVORR.AMOUNT, TVORR.ALLOCA_NUM, TVORR.ORG_ID, TVOD.MATERIAL_ID");
		sbSql.append(")\n");
		sbSql.append("SELECT TVM.MATERIAL_ID,\n");
		sbSql.append("       TVM.MATERIAL_CODE,\n");
		sbSql.append("       TVM.MATERIAL_NAME,\n");
		sbSql.append("       VSA.PLAN_DETAIL_ID,\n");
		sbSql.append("       SUM(NVL(VSA.STOCK_AMOUNT, 0) - NVL(CK.NUM, 0)) - NVL(ORSA.ORDER_AMOUNT, 0) STOCK_AMOUNT,\n");
		sbSql.append("       SUM(NVL(VSA.STOCK_AMOUNT, 0) - NVL(CK.NUM, 0)) - NVL(ORSA.ORDER_AMOUNT, 0) RESOURCE_AMOUNT,\n");
		sbSql.append("       NVL(ORS.ORDER_AMOUNT, 0) ORDER_RESERVE_AMOUNT,\n");
		sbSql.append("       ORS.ORDER_AMOUNT,\n");
		sbSql.append("       VSA.ORG_ID,\n");
		sbSql.append("       CASE\n");
		sbSql.append("         WHEN VSA.ORG_ID = -2222222222 THEN\n");
		sbSql.append("          '销售部'\n");
		sbSql.append("         ELSE\n");
		sbSql.append("          TO_CHAR(TORG.ORG_NAME)\n");
		sbSql.append("       END REGION_NAME\n");
		sbSql.append("  FROM VEHICLE_STOCK_ALL  VSA,\n");
		sbSql.append("       COMP_KEEP          CK,\n");
		sbSql.append("       TM_VHCL_MATERIAL   TVM,\n");
		sbSql.append("       TM_ORG   TORG,\n");
		sbSql.append("		 ORDER_RESOURCE_SUB_ALL ORSA,");
		sbSql.append("       ORDER_RESOURCE_SUB ORS\n");
		sbSql.append(" WHERE VSA.MATERIAL_ID = TVM.MATERIAL_ID\n");
		sbSql.append("   AND VSA.ORG_ID = TORG.ORG_ID(+)\n");
		sbSql.append("   AND VSA.MATERIAL_ID = CK.MATERIAL_ID(+)\n");
		sbSql.append("   AND VSA.ORG_ID = CK.ORG_ID(+)\n");
		sbSql.append("	 AND VSA.ORG_ID = ORS.ORG_ID(+)\n");
		sbSql.append("	 AND VSA.ORG_ID = ORSA.ORG_ID(+)\n");
		sbSql.append("   AND VSA.MATERIAL_ID = ORSA.MATERIAL_ID(+)\n");
		sbSql.append("   AND VSA.PRODUCT_TYPE = ORSA.PRODUCT_TYPE(+)\n");
		sbSql.append("   AND VSA.PLAN_DETAIL_ID = ORSA.PLAN_DETAIL_ID(+)\n");
		sbSql.append("   AND VSA.MATERIAL_ID = ORS.MATERIAL_ID(+)\n");
		sbSql.append("   AND VSA.PRODUCT_TYPE = ORS.PRODUCT_TYPE(+)\n");
		sbSql.append("   AND VSA.PLAN_DETAIL_ID = ORS.PLAN_DETAIL_ID(+)\n");
		sbSql.append("   AND VSA.MATERIAL_ID = ?\n");
		if (orderType.equals(Constant.ORDER_TYPE_02.toString()))
		{
			sbSql.append("   AND VSA.PRODUCT_TYPE = -1");
		}
		else
		{
			sbSql.append("   AND VSA.PRODUCT_TYPE = 1");
		}

		sbSql.append(MaterialGroupManagerDao.getPoseIdBusinessSql("VSA.YIELDLY", poseId));

		sbSql.append(" GROUP BY TVM.MATERIAL_ID,TVM.MATERIAL_CODE,TVM.MATERIAL_NAME,VSA.PLAN_DETAIL_ID,ORS.ORDER_AMOUNT,ORSA.ORDER_AMOUNT,VSA.ORG_ID,TORG.ORG_NAME");

		sbSql.append(" HAVING SUM(NVL(VSA.STOCK_AMOUNT, 0) - NVL(CK.NUM, 0)) - NVL(ORSA.ORDER_AMOUNT, 0) > 0");

		params.clear();
		params.add(orderDetailId);
		params.add(orderDetailId);
		params.add(orderDetailId);
		params.add(materialId);

		return dao.pageQuery(sbSql.toString(), params, getFunName());
	}

	/**
	 * 方法描述 ：获取未开票的提车单数据 <br/>
	 * 
	 * @return
	 * @author wangsongwei
	 */
	public List<Map<String, Object>> getOrderNovoList(String[] orderIds, AclUserBean logonUser) throws Exception {
//		String poseId = logonUser.getPoseId().toString();
//		String orderId = "";
//		if (orderIds != null)
//		{
//			for (String oid : orderIds)
//			{
//				orderId += oid + ",";
//			}
//			orderId = orderId.substring(0, orderId.length() - 1);
//		}
//
//		StringBuffer sbSql = new StringBuffer();
//
//		sbSql.append("WITH Q_ZHZK AS (\n");
//		sbSql.append("     SELECT " + Constant.ORDER_INVOICE_TYPE_01 + " AS ORDER_DJ_TYPE,\n");
//		sbSql.append("            A2.ORDER_ID,\n");
//		sbSql.append("            (SUM(NVL(A2.CHK_PRICE, 0) * NVL(A2.CHECK_AMOUNT, 0)) -\n");
//		sbSql.append("            (SUM((NVL(A2.CHK_PRICE, 0) * NVL(A2.CHECK_AMOUNT, 0) *\n");
//		sbSql.append("                  ((100 - A2.DISCOUNT_RATE) / 100))) - A1.REBATE_PRICE)) /\n");
//		sbSql.append("            SUM(NVL(A2.CHK_PRICE, 0) * NVL(A2.CHECK_AMOUNT, 0)) ZH_RATE -- 综合折扣率\n");
//		sbSql.append("       FROM TT_VS_ORDER A1, TT_VS_ORDER_DETAIL A2\n");
//		sbSql.append("      WHERE A1.ORDER_ID = A2.ORDER_ID\n");
//		sbSql.append("        AND NVL(A2.CHK_PRICE, 0) <> 0\n");
//		sbSql.append("        AND NVL(A2.CHECK_AMOUNT, 0) <> 0\n");
//		sbSql.append("      GROUP BY A2.ORDER_ID, A1.REBATE_PRICE\n");
//		sbSql.append("     UNION ALL\n");
//		sbSql.append("     SELECT " + Constant.ORDER_INVOICE_TYPE_02 + " AS ORDER_DJ_TYPE,\n");
//		sbSql.append("            A1.ORDER_ID,\n");
//		sbSql.append("            (SUM(NVL(A2.SINGLE_PRICE, 0) * NVL(A2.OUT_AMOUNT, 0)) -\n");
//		sbSql.append("            (SUM((NVL(A2.SINGLE_PRICE, 0) * NVL(A2.OUT_AMOUNT, 0) *\n");
//		sbSql.append("                  ((100 - A2.DISCOUNT_RATE) / 100))) - A1.REBATE_PRICE)) /\n");
//		sbSql.append("            SUM(NVL(A2.SINGLE_PRICE, 0) * NVL(A2.OUT_AMOUNT, 0)) ZH_RATE -- 综合折扣率\n");
//		sbSql.append("       FROM TT_OUT_ORDER A1, TT_OUT_ORDER_DETAIL A2\n");
//		sbSql.append("      WHERE A1.ORDER_ID = A2.ORDER_ID\n");
//		sbSql.append("        AND NVL(A2.SINGLE_PRICE, 0) <> 0\n");
//		sbSql.append("        AND NVL(A2.OUT_AMOUNT, 0) <> 0\n");
//		sbSql.append("      GROUP BY A1.ORDER_ID, A1.REBATE_PRICE\n");
//		sbSql.append("),\n");
//		sbSql.append("Q_DDZJ AS (\n");
//		sbSql.append("     SELECT " + Constant.ORDER_INVOICE_TYPE_01 + " AS ORDER_DJ_TYPE,\n");
//		sbSql.append("            A2.ORDER_ID,\n");
//		sbSql.append("            SUM(NVL(A2.CHK_PRICE, 0) * NVL(A2.CHECK_AMOUNT, 0)) ORDER_ZQ_PRICE -- 订单折前总价\n");
//		sbSql.append("       FROM TT_VS_ORDER A1, TT_VS_ORDER_DETAIL A2\n");
//		sbSql.append("      WHERE A1.ORDER_ID = A2.ORDER_ID\n");
//		sbSql.append("        AND NVL(A2.CHK_PRICE, 0) <> 0\n");
//		sbSql.append("        AND NVL(A2.CHECK_AMOUNT, 0) <> 0\n");
//		sbSql.append("      GROUP BY A2.ORDER_ID\n");
//		sbSql.append("     UNION ALL\n");
//		sbSql.append("     SELECT " + Constant.ORDER_INVOICE_TYPE_02 + " AS ORDER_DJ_TYPE,\n");
//		sbSql.append("            A1.ORDER_ID,\n");
//		sbSql.append("            SUM(NVL(A2.SINGLE_PRICE, 0) * NVL(A2.OUT_AMOUNT, 0)) ORDER_ZQ_PRICE -- 订单折前总价\n");
//		sbSql.append("       FROM TT_OUT_ORDER A1, TT_OUT_ORDER_DETAIL A2\n");
//		sbSql.append("      WHERE A1.ORDER_ID = A2.ORDER_ID\n");
//		sbSql.append("        AND NVL(A2.SINGLE_PRICE, 0) <> 0\n");
//		sbSql.append("        AND NVL(A2.OUT_AMOUNT, 0) <> 0\n");
//		sbSql.append("      GROUP BY A1.ORDER_ID\n");
//		sbSql.append("),\n");
//		sbSql.append("ALL_ORDER_DETAIL AS (\n");
//		sbSql.append("     SELECT " + Constant.ORDER_INVOICE_TYPE_01 + " ORDER_DJ_TYPE,\n");
//		sbSql.append("            A1.ORDER_ID,\n");
//		sbSql.append("            (A3.SERIES_NAME || '/' || A3.MODEL_CODE) SERIES_NAME, -- 车系名称\n");
//		sbSql.append("            A3.PACKAGE_CODE, -- 配置名称\n");
//		sbSql.append("			  SUM(NVL(A2.CHECK_AMOUNT, 0)) CHK_NUM, -- 提报数量\n");
//		sbSql.append("            TO_CHAR(A4.ZH_RATE*100, 'FM990.000') ZHDIS_RATE, -- 综合折扣率\n");
//		sbSql.append("            TO_CHAR(A5.ORDER_ZQ_PRICE*round(A4.ZH_RATE,5)/1.17, 'FM999999990.00') DIS_ORDER_PRICE, -- 不含税折扣额\n");
//		sbSql.append("            TO_CHAR(A5.ORDER_ZQ_PRICE*0.17/1.17,'FM999999990.00') S_ORDER_PRICE, -- 税额\n");
//		sbSql.append("            TO_CHAR(A5.ORDER_ZQ_PRICE*ROUND(A4.ZH_RATE,5)*0.17/1.17,'FM999999990.00') SDIS_ORDER_PRICE, -- 含税折扣额\n");
//		sbSql.append("            TO_CHAR(A5.ORDER_ZQ_PRICE/1.17,'FM999999990.00') ORDER_PRICE -- 订单金税总价\n");
//		sbSql.append("       FROM TT_VS_ORDER A1, TT_VS_ORDER_DETAIL A2, VW_MATERIAL_GROUP_MAT A3, Q_ZHZK A4, Q_DDZJ A5\n");
//		sbSql.append("      WHERE A1.ORDER_ID = A2.ORDER_ID\n");
//		sbSql.append("        AND A2.MATERIAL_ID = A3.MATERIAL_ID\n");
//		sbSql.append("        AND A1.ORDER_ID = A4.ORDER_ID\n");
//		sbSql.append("        AND A1.ORDER_ID = A5.ORDER_ID\n");
//		sbSql.append("        AND A5.ORDER_DJ_TYPE = " + Constant.ORDER_INVOICE_TYPE_01 + "\n");
//		sbSql.append("        AND A4.ORDER_DJ_TYPE = " + Constant.ORDER_INVOICE_TYPE_01 + "\n");
//		sbSql.append("		GROUP BY A1.ORDER_ID,A3.SERIES_NAME,A3.MODEL_CODE,A3.PACKAGE_CODE,A4.ZH_RATE,A5.ORDER_ZQ_PRICE\n");
//		sbSql.append("     UNION ALL\n");
//		sbSql.append("     SELECT " + Constant.ORDER_INVOICE_TYPE_02 + " ORDER_DJ_TYPE,\n");
//		sbSql.append("            I1.ORDER_ID,\n");
//		sbSql.append("            (I3.SERIES_NAME || '/' || I3.MODEL_CODE) SERIES_NAME, -- 车系名称\n");
//		sbSql.append("            I3.PACKAGE_CODE, -- 配置代码\n");
//		sbSql.append("            SUM(NVL(I2.OUT_AMOUNT, 0)) CHK_NUM, -- 总的提报数量\n");
//		sbSql.append("            TO_CHAR(A4.ZH_RATE*100, 'FM990.000') ZHDIS_RATE, -- 综合折扣率\n");
//		sbSql.append("            TO_CHAR(A5.ORDER_ZQ_PRICE*round(A4.ZH_RATE,5)/1.17, 'FM999999990.00') DIS_ORDER_PRICE, -- 不含税折扣额\n");
//		sbSql.append("            TO_CHAR(A5.ORDER_ZQ_PRICE*0.17/1.17,'FM999999990.00') S_ORDER_PRICE, -- 税额\n");
//		sbSql.append("            TO_CHAR(A5.ORDER_ZQ_PRICE*ROUND(A4.ZH_RATE,5)*0.17/1.17,'FM999999990.00') SDIS_ORDER_PRICE, -- 含税折扣额\n");
//		sbSql.append("            TO_CHAR(A5.ORDER_ZQ_PRICE/1.17,'FM999999990.00') ORDER_PRICE -- 订单金税总价\n");
//		sbSql.append("       FROM TT_OUT_ORDER          I1,\n");
//		sbSql.append("            TT_OUT_ORDER_DETAIL   I2,\n");
//		sbSql.append("            VW_MATERIAL_GROUP_MAT I3,\n");
//		sbSql.append("             Q_ZHZK A4,\n");
//		sbSql.append("             Q_DDZJ A5\n");
//		sbSql.append("      WHERE I1.ORDER_ID = I2.ORDER_ID\n");
//		sbSql.append("        AND I2.MATERIAL_ID = I3.MATERIAL_ID\n");
//		sbSql.append("        AND I1.ORDER_ID = A4.ORDER_ID\n");
//		sbSql.append("        AND I1.ORDER_ID = A5.ORDER_ID\n");
//		sbSql.append("        AND A4.ORDER_DJ_TYPE = " + Constant.ORDER_INVOICE_TYPE_02 + "\n");
//		sbSql.append("        AND A5.ORDER_DJ_TYPE = " + Constant.ORDER_INVOICE_TYPE_02 + "\n");
//		sbSql.append("      GROUP BY I1.ORDER_ID,\n");
//		sbSql.append("               I2.DISCOUNT_RATE,\n");
//		sbSql.append("               I3.SERIES_NAME,I3.MODEL_CODE,\n");
//		sbSql.append("               I3.PACKAGE_CODE,A4.ZH_RATE,A5.ORDER_ZQ_PRICE\n");
//		sbSql.append("),\n");
//		sbSql.append("REBATE_MESSAGE AS (\n");
//		sbSql.append("      SELECT TSOR.ORDER_ID,\n");
//		sbSql.append("             WM_CONCAT(TSR.DIS_ITEM || '使用返利' || TO_CHAR(TSOR.USE_AMOUNT)) CREMARK\n");
//		sbSql.append("        FROM TT_SALES_ORDER_REB TSOR, TT_SALES_REBATE TSR\n");
//		sbSql.append("       WHERE TSR.REB_ID = TSOR.REB_ID\n");
//		sbSql.append("       GROUP BY TSOR.ORDER_ID\n");
//		sbSql.append("),\n");
//		sbSql.append("PROMISE_MESSAGE AS (\n");
//		sbSql.append("SELECT TO_CHAR(TSP.PRO_DATE, 'YYYY-MM-DD') PRO_DATE,\n");
//		sbSql.append("       TVO.ORDER_ID,\n");
//		sbSql.append("       " + Constant.ORDER_INVOICE_TYPE_01 + " AS ORDER_DJ_TYPE\n");
//		sbSql.append("  FROM TT_SALES_PROMISE     TSP,\n");
//		sbSql.append("       TT_SALES_PROMISE_DET TSPD,\n");
//		sbSql.append("       TT_PROM_ORD_USE      TPOU,\n");
//		sbSql.append("       TT_VS_ORDER_DETAIL   TVOD,\n");
//		sbSql.append("       TT_VS_ORDER          TVO\n");
//		sbSql.append(" WHERE TSP.PRO_ID = TSPD.PRO_ID\n");
//		sbSql.append("   AND TSPD.DETAIL_ID = TPOU.DETAIL_ID\n");
//		sbSql.append("   AND TPOU.ORD_DET_ID = TVOD.DETAIL_ID\n");
//		sbSql.append("   AND TVOD.ORDER_ID = TVO.ORDER_ID\n");
//		sbSql.append("   AND TVO.IS_PROMISE = " + Constant.IF_TYPE_YES + "\n");
//		sbSql.append(" GROUP BY TSP.PRO_DATE, TVO.ORDER_ID\n");
//		sbSql.append("UNION ALL\n");
//		sbSql.append("SELECT TO_CHAR(TSP.PRO_DATE, 'YYYY-MM-DD') PRO_DATE,\n");
//		sbSql.append("       TOO.ORDER_ID,\n");
//		sbSql.append("       " + Constant.ORDER_INVOICE_TYPE_02 + " AS ORDER_DJ_TYPE\n");
//		sbSql.append("  FROM TT_SALES_PROMISE     TSP,\n");
//		sbSql.append("       TT_SALES_PROMISE_DET TSPD,\n");
//		sbSql.append("       TT_PROM_ORD_USE      TPOU,\n");
//		sbSql.append("       TT_OUT_ORDER_DETAIL   TOOD,\n");
//		sbSql.append("       TT_OUT_ORDER          TOO\n");
//		sbSql.append(" WHERE TSP.PRO_ID = TSPD.PRO_ID\n");
//		sbSql.append("   AND TSPD.DETAIL_ID = TPOU.DETAIL_ID\n");
//		sbSql.append("   AND TPOU.ORD_DET_ID = TOOD.DETAIL_ID\n");
//		sbSql.append("   AND TOOD.ORDER_ID = TOO.ORDER_ID\n");
//		sbSql.append("   AND TOO.FUND_TYPE_ID = " + Constant.ACCOUNT_TYPE_05 + "\n");
//		sbSql.append(" GROUP BY TSP.PRO_DATE, TOO.ORDER_ID");
//		sbSql.append("),\n");
//		sbSql.append("ORDER_SINGLE AS (\n");
//		sbSql.append("     SELECT " + Constant.ORDER_INVOICE_TYPE_01 + " ORDER_DJ_TYPE,\n");
//		sbSql.append("            B1.ORDER_ID,\n");
//		sbSql.append("            B1.ORDER_NO,\n");
//		sbSql.append("            B1.DEALER_ID,\n");
//		sbSql.append("            B1.AREA_ID,\n");
//		sbSql.append("            B1.ORDER_YF_PRICE,\n");
//		sbSql.append("            B1.ORDER_PRICE,\n");
//		sbSql.append("            B2.ERP_CODE, -- 开票名称\n");
//		sbSql.append("            B2.TAXES_NO, -- 税号\n");
//		sbSql.append("            B2.INVOICE_ADD || B2.INVOICE_PHONE ADD_AND_PHONE,\n");
//		sbSql.append("            B2.BANK || ' ' || B2.INVOICE_ACCOUNT INVOICE_BANK,\n");
//		sbSql.append("            B1.ORDER_NO || ' ' || B3.CODE_DESC ORDER_INFO\n");
//		sbSql.append("       FROM TT_VS_ORDER B1, TM_DEALER B2, TC_CODE B3\n");
//		sbSql.append("      WHERE B1.DEALER_ID = B2.DEALER_ID\n");
//		sbSql.append("        AND B1.FUND_TYPE_ID = B3.CODE_ID(+)\n");
//		sbSql.append("        AND B1.ORDER_STATUS = ?\n");
//		sbSql.append("        AND B1.INVO_TYPE = ?\n");
//		sbSql.append("     UNION ALL\n");
//		sbSql.append("     SELECT " + Constant.ORDER_INVOICE_TYPE_02 + " ORDER_DJ_TYPE,\n");
//		sbSql.append("            B1.ORDER_ID,\n");
//		sbSql.append("            B1.ORDER_NO,\n");
//		sbSql.append("            B1.DEALER_ID,\n");
//		sbSql.append("            B1.AREA_ID,\n");
//		sbSql.append("            B1.ORDER_YF_PRICE,\n");
//		sbSql.append("            B1.ORDER_PRICE,\n");
//		sbSql.append("            B2.ERP_CODE, -- 开票名称\n");
//		sbSql.append("            B2.TAXES_NO, -- 税号\n");
//		sbSql.append("            B2.INVOICE_ADD || B2.INVOICE_PHONE ADD_AND_PHONE,\n");
//		sbSql.append("            B2.BANK || ' ' || B2.INVOICE_ACCOUNT INVOICE_BANK,\n");
//		sbSql.append("            B1.ORDER_NO || ' ' || B3.CODE_DESC ORDER_INFO\n");
//		sbSql.append("       FROM TT_OUT_ORDER B1, TM_DEALER B2, TC_CODE B3\n");
//		sbSql.append("      WHERE B1.DEALER_ID = B2.DEALER_ID\n");
//		sbSql.append("        AND B1.FUND_TYPE_ID = B3.CODE_ID(+)\n");
//		sbSql.append("        AND B1.ORDER_STATUS = ?\n");
//		sbSql.append("        AND B1.INVO_TYPE = ?\n");
//		sbSql.append(")\n");
//		sbSql.append("SELECT H1.ORDER_ID,\n");
//		sbSql.append("       H1.ORDER_NO,\n");
//		sbSql.append("       H1.ERP_CODE,\n");
//		sbSql.append("       H1.TAXES_NO,\n");
//		sbSql.append("       H1.ADD_AND_PHONE,\n");
//		sbSql.append("       H1.INVOICE_BANK,\n");
//		sbSql.append("       H1.ORDER_INFO || ' ' ||H3.CREMARK ORDER_INFO,\n");
//		sbSql.append("		 H4.PRO_DATE,\n");
//		sbSql.append("       H2.SERIES_NAME,\n");
//		sbSql.append("       H2.PACKAGE_CODE,\n");
//		sbSql.append("       H2.CHK_NUM,\n");
//		sbSql.append("       H2.ORDER_PRICE,\n");
//		sbSql.append("       H2.DIS_ORDER_PRICE,\n");
//		sbSql.append("       H2.SDIS_ORDER_PRICE,\n");
//		sbSql.append("       H2.S_ORDER_PRICE,\n");
//		sbSql.append("       H2.ZHDIS_RATE\n");
//		sbSql.append("  FROM ORDER_SINGLE H1, ALL_ORDER_DETAIL H2, REBATE_MESSAGE H3,PROMISE_MESSAGE H4\n");
//		sbSql.append(" WHERE H1.ORDER_ID = H2.ORDER_ID\n");
//		sbSql.append("   AND H1.ORDER_DJ_TYPE = H2.ORDER_DJ_TYPE(+)\n");
//		sbSql.append("   AND H1.ORDER_ID = H3.ORDER_ID(+)");
//		sbSql.append("   AND H1.ORDER_ID = H4.ORDER_ID(+)\n");
//		sbSql.append("   AND H1.ORDER_DJ_TYPE = H4.ORDER_DJ_TYPE(+)\n");
//		sbSql.append(MaterialGroupManagerDao.getPoseIdBusinessSql("H1.AREA_ID", poseId)); // 产地查看权限限制
//		sbSql.append(MaterialGroupManagerDao.getOrgDealerLimitSql("H1.DEALER_ID", logonUser)); // 组织机构权限控制
//
//		List<Object> params = new ArrayList<Object>();
//
//		params.add(Constant.ORDER_STATUS_06);
//		params.add(Constant.ORDER_INVO_STATUS_01);
//		params.add(Constant.OUT_ORDER_CHECK_STATUS_05);
//		params.add(Constant.ORDER_INVO_STATUS_01);
//
//		if (!"".equals(orderId))
//		{
//			sbSql.append(" AND H1.ORDER_ID IN (" + orderId + ")");
//			// params.add(orderId);
//		}
//
//		return dao.pageQuery(sbSql.toString(), params, getFunName());
		return null;
	}

	/**
	 * 方法描述 ：获取未开票的提车单数据统计 <br/>
	 * 
	 * @return
	 * @author wangsongwei
	 */
	public int getOrderNovoCount(String[] orderIds, AclUserBean logonUser) throws Exception {
//		String poseId = logonUser.getPoseId().toString();
//
//		StringBuffer sbSql = new StringBuffer();
//
//		sbSql.append("WITH \n");
//		sbSql.append("ALL_ORDER_DETAIL AS (\n");
//		sbSql.append("     SELECT A1.ORDER_ID, A1.AREA_ID, A1.DEALER_ID\n");
//		sbSql.append("       FROM TT_VS_ORDER A1\n");
//		sbSql.append("      WHERE A1.ORDER_STATUS = ? AND A1.INVO_TYPE = ?\n");
//		sbSql.append("     UNION ALL\n");
//		sbSql.append("     SELECT I1.ORDER_ID, I1.AREA_ID, I1.DEALER_ID\n");
//		sbSql.append("       FROM TT_OUT_ORDER I1 \n");
//		sbSql.append("      WHERE I1.ORDER_STATUS = ? AND I1.INVO_TYPE = ?\n");
//		sbSql.append(")\n");
//		sbSql.append("SELECT COUNT(1) ALL_COUNT \n");
//		sbSql.append("  FROM ALL_ORDER_DETAIL H1\n");
//		sbSql.append(" WHERE 1 = 1 \n");
//		sbSql.append(MaterialGroupManagerDao.getPoseIdBusinessSql("H1.AREA_ID", poseId)); // 产地查看权限限制
//		sbSql.append(MaterialGroupManagerDao.getOrgDealerLimitSql("H1.DEALER_ID", logonUser)); // 组织机构权限控制
//
//		List<Object> params = new ArrayList<Object>();
//
//		params.add(Constant.ORDER_STATUS_06);
//		params.add(Constant.ORDER_INVO_STATUS_01);
//		params.add(Constant.OUT_ORDER_CHECK_STATUS_05);
//		params.add(Constant.ORDER_INVO_STATUS_01);
//
//		if (!"".equals(orderIds) && orderIds != null)
//		{
//			sbSql.append(" AND H1.ORDER_ID IN (" + getSqlBuffer(orderIds, params) + ")");
//			// params.add(orderId);
//		}
//
//		Map<String, Object> countMap = dao.pageQueryMap(sbSql.toString(), params, getFunName());
//
//		int a = countMap == null ? 0 : Integer.parseInt(countMap.get("ALL_COUNT").toString());
//
//		return a;
		return 0;
	}

	/**
	 * 方法描述 : 获取订单的返利使用明细表内容<br/>
	 * 
	 * @param orderid
	 * @return
	 * @author wangsongwei
	 */
	public List<Map<String, Object>> getOrderRebateList(String orderId) throws Exception {
		List<Object> params = new ArrayList<Object>();
		params.add(orderId);

		StringBuffer sbSql = new StringBuffer();

		sbSql.append("SELECT\n");
		sbSql.append("            TSR.REB_ID,                         -- 返利主键\n");
		sbSql.append("            TSR.REB_NO,                         -- 返利编号\n");
		sbSql.append("            TSR.VER,                            -- 返利版本\n");
		sbSql.append("            TO_CHAR(TSR.CREATE_DATE,'yyyy-mm-dd hh24:mi:ss') CREATE_DATE,                    -- 返利日期\n");
		sbSql.append("            TSR.TOTAL_AMOUNT,                   -- 返利额度\n");
		sbSql.append("			  TSR.REMARK,									\n");
		sbSql.append("            NVL(TSR.TOTAL_AMOUNT,0) - NVL(TSR.USED_AMOUNT,0) REB_AMOUNT, -- 可用额度\n");
		sbSql.append("            NVL(TSR.USED_AMOUNT,0) USED_AMOUNT,                    -- 已用额度\n");
		sbSql.append("            TSOR.USE_AMOUNT,                    -- 明细使用额度\n");
		sbSql.append("            DIS.DISCOUNT_RATE,                  -- 订单车系折扣率\n");
		sbSql.append("            NVL(TSOR.DIS_AMOUNT,0) DISCOUNT_REBATE -- 折后额度\n");
		sbSql.append("FROM\n");
		sbSql.append("            TT_SALES_REBATE TSR,                -- 返利主表\n");
		sbSql.append("            TT_SALES_ORDER_REB TSOR,            -- 订单返利明细表\n");
		sbSql.append("            (\n");
		sbSql.append("                SELECT VMG.SERIES_ID, TVO.ORDER_ID, NVL(TVOD.DISCOUNT_RATE,0) DISCOUNT_RATE\n");
		sbSql.append("                FROM\n");
		sbSql.append("                        TT_VS_ORDER TVO,                    -- 订单主表\n");
		sbSql.append("                        TT_VS_ORDER_DETAIL TVOD,            -- 订单明细表\n");
		sbSql.append("                        TM_VHCL_MATERIAL_GROUP TVMG,\n");
		sbSql.append("                        TM_VHCL_MATERIAL_GROUP_R TVMGR,\n");
		sbSql.append("                        vw_material_group VMG               -- 车辆配置组\n");
		sbSql.append("                WHERE\n");
		sbSql.append("                        TVO.ORDER_ID = TVOD.ORDER_ID\n");
		sbSql.append("                        AND\n");
		sbSql.append("                        TVOD.MATERIAL_ID = TVMGR.MATERIAL_ID\n");
		sbSql.append("                        AND\n");
		sbSql.append("                        TVMGR.GROUP_ID = TVMG.GROUP_ID\n");
		sbSql.append("                        AND\n");
		sbSql.append("                        TVMG.GROUP_ID = VMG.PACKAGE_ID\n");
		sbSql.append("                GROUP BY VMG.SERIES_ID, TVO.ORDER_ID, DISCOUNT_RATE\n");
		sbSql.append("            ) DIS\n");
		sbSql.append("WHERE\n");
		sbSql.append("            TSR.REB_ID = TSOR.REB_ID\n");
		sbSql.append("            AND\n");
		sbSql.append("            TSOR.ORDER_ID = DIS.ORDER_ID\n");
		sbSql.append("            AND\n");
		sbSql.append("            TSOR.ORDER_ID = ?");

		return dao.pageQuery(sbSql.toString(), params, getFunName());
	}

	/**
	 * 方法描述 ： 返利使用明细分页查询 <br/>
	 * 
	 * @param infoMap
	 * @return
	 * @author wangsongwei
	 */
	public PageResult<Object> getRebateDetail(Map<String, Object> infoMap, Integer curPage, Integer pageSize) throws Exception {
		StringBuffer sbSql = new StringBuffer();

		sbSql.append("SELECT TSOR.DETAIL_ID,\n");
		sbSql.append("       TSOR.ORDER_ID,\n");
		sbSql.append("       TVO.ORDER_NO,\n");
		sbSql.append("       TSOR.USE_AMOUNT,\n");
		sbSql.append("       TSOR.DIS_AMOUNT,\n");
		sbSql.append("       TSOR.USE_DATE， TSOR.ORDER_TYPE\n");
		sbSql.append("  FROM TT_SALES_REBATE    TSR, -- 返利表哦\n");
		sbSql.append("       TT_SALES_ORDER_REB TSOR, -- 返利使用明细表\n");
		sbSql.append("       TT_VS_ORDER        TVO\n");
		sbSql.append(" WHERE TSR.REB_ID = TSOR.REB_ID\n");
		sbSql.append("   AND TSOR.ORDER_ID = TVO.ORDER_ID\n");

		List<Object> params = new ArrayList<Object>();

		String rebateId = infoMap.get("REBATE_ID").toString();

		if (!rebateId.equals(""))
		{
			sbSql.append("AND TSR.REB_ID = " + rebateId);
		}

		return pageQueryObject(sbSql.toString(), params, pageSize, curPage);
	}

	/**
	 * 方法描述 ： 查询订单主表信息,用于显示订单信息使用<br/>
	 * 
	 * @param orderId
	 *            订单ID
	 * @return
	 * @author wangsongwei
	 */
	public Map<String, Object> getOrderSubmissionInfo(String orderId) throws Exception {
		StringBuffer sbSql = new StringBuffer();
		List<Object> params=new ArrayList<Object>();
		params.add(orderId);
		sbSql.append("\r\n");
		sbSql.append("-- 订单的基本信息查询\r\n");
		sbSql.append("SELECT TO_CHAR(T.EXPECT_DATE,'YYYY-MM-DD') EXPECT_DATE,TA.SPORADIC_CUSTOMER_CODE,TA.SPORADIC_CUSTOMER_NAME,\r\n");
		sbSql.append("       TO_CHAR(T.EXPECT_LAST_DATE,'YYYY-MM-DD') EXPECT_LAST_DATE,\r\n");
		sbSql.append("       TO_CHAR(T.PLAN_LAST_DATE,'YYYY-MM-DD') PLAN_LAST_DATE,\r\n");
		sbSql.append("       TO_CHAR(T.PLAN_SEND_DATE,'YYYY-MM-DD') PLAN_SEND_DATE,\r\n");
		sbSql.append("       TO_CHAR(T.PLAN_DELIVER_DATE,'YYYY-MM-DD') PLAN_DELIVER_DATE,\r\n");
		sbSql.append("       T.FUND_TYPE_ID,\r\n");
		sbSql.append("       F_GET_TCCODE_DESC(T.FUND_TYPE_ID) FUND_TYPE_NAME,\r\n");
		sbSql.append("       F_GET_TCCODE_DESC(T.INVO_TYPE) INVO_TYPE_NAME,\r\n");
		sbSql.append("       T.REC_DEALER_ID,\r\n");
		sbSql.append("       T.DEALER_ID,\r\n");
		sbSql.append("       T.AREA_ID,\r\n");
		sbSql.append("       T.ORDER_NO,\r\n");
		sbSql.append("       TA.ORDER_NO S_ORDER_NO,\r\n"); // 2015.8.28 艾春添加 销售商订单号
		sbSql.append("       T.ORDER_ID,\r\n");
		sbSql.append("       T3.DEALER_CODE,\r\n");
		sbSql.append("       T3.DEALER_NAME,\r\n");
		sbSql.append("       T1.DEALER_CODE REC_DEALER_CODE,\r\n");
		sbSql.append("       T1.DEALER_NAME REC_DEALER_NAME,\r\n");
		sbSql.append("       T.ORDER_TYPE,\r\n");
		sbSql.append("       F_GET_TCCODE_DESC(T.ORDER_TYPE) ORDER_TYPE_NAME,\r\n");
		sbSql.append("       T.DELIVERY_TYPE,\r\n");
		sbSql.append("       F_GET_TCCODE_DESC(T.DELIVERY_TYPE) DELIVERY_TYPE_NAME,\r\n");
		sbSql.append("       T.FLEET_ID,\r\n");
		sbSql.append("       T.FLEET_ADDRESS,\r\n");
		sbSql.append("       T2.ADDRESS,\r\n");
		sbSql.append("       T2.LINK_MAN,\r\n");
		sbSql.append("       T2.TEL,\r\n");
		sbSql.append("       T.RES_REMARK,\r\n");
		sbSql.append("       TO_CHAR(T.RAISE_DATE,'YYYY-MM-DD') RAISE_DATE,\r\n");
		sbSql.append("       T.ORDER_PRICE,\r\n");
		sbSql.append("       T.ORDER_YF_PRICE,\r\n");
		sbSql.append("       T.REBATE_PRICE,\r\n");
		sbSql.append("       T.ORDER_REMARK\r\n");
		sbSql.append("  FROM TT_VS_ORDER T,TT_SA_ORDER TA, TM_DEALER T1, TM_VS_ADDRESS T2, TM_DEALER T3\r\n");
		sbSql.append(" WHERE T.DELIV_ADD_ID = T2.ID\r\n");
		sbSql.append("   AND T.REC_DEALER_ID = T1.DEALER_ID\r\n");
		sbSql.append("   AND T.WR_ORDER_ID = TA.ORDER_ID\r\n");
		sbSql.append("   AND T.DEALER_ID = T3.DEALER_ID\r\n");
		sbSql.append("   AND T.ORDER_ID = ?"); 
		return dao.pageQueryMap(sbSql.toString(), params, getFunName());
	}

	/**
	 * 方法描述 ：获取生产订单主表数据 <br/>
	 * 
	 * @param createId
	 * @return
	 * @author wangsongwei
	 */
	public Map<String, Object> getProductOrderInfo(String createId) throws Exception {

		StringBuffer sbSql = new StringBuffer();

		sbSql.append("SELECT\n");
		sbSql.append("       TUO.CUS_ORDER_ID,\n");
		sbSql.append("		 TUO.CUS_TYPE,\n");
		sbSql.append("		 TUO.CUS_NUM,\n");
		sbSql.append("       TUO.STATUS,\n");
		sbSql.append("       TUO.YIELDLY AS AREA_ID,\n");
		sbSql.append("		(SELECT TBA.AREA_NAME FROM TM_BUSINESS_AREA TBA WHERE TBA.AREA_ID = TUO.YIELDLY) AREA_NAME,\n");
		sbSql.append("       TUO.DEALER_ID,\n");
		sbSql.append("       TVSR.PRE_AMOUNT,\n");
		sbSql.append("		 TUO.FIN_TYPE,\n");
		sbSql.append("       TVSR.ACCOUNT_TYPE_ID,\n");
		sbSql.append("       TVSR.FLEET_ID,\n");
		sbSql.append("       (SELECT TF.FLEET_NAME FROM TM_FLEET TF WHERE TF.FLEET_ID = TVSR.FLEET_ID) FLEET_NAME\n");
		sbSql.append("FROM\n");
		sbSql.append("       TM_CUS_ORDER TUO,\n");
		sbSql.append("       TM_PRO_ORDER TPO,\n");
		sbSql.append("       TT_VS_SPECIAL_REQ TVSR\n");
		sbSql.append(" WHERE\n");
		sbSql.append("       TUO.CUS_ORDER_ID = " + createId + "\n");
		sbSql.append("       AND TUO.CUS_ORDER_ID = TPO.CUS_ORDER_ID(+)\n");
		sbSql.append("       AND TUO.CUS_NEED_ID = TVSR.REQ_ID(+)");

		return dao.pageQueryMap(sbSql.toString(), null, getFunName());
	}

	/**
	 * 方法描述 ：获取生产订单物料数据 <br/>
	 * 
	 * @param createId
	 * @return
	 * @author wangsongwei
	 */
	public List<Map<String, Object>> getProductOrderMaterailList(String createId, String orderId) throws Exception {
		List<Object> params = new ArrayList<Object>();
		StringBuffer sbSql = new StringBuffer();

		String specalRate = "0";
		Map<String, Object> seriesRateMap = null;
		Map<String, Object> modelRateMap = null;
		boolean isf = false;

		// 引入特殊折扣率
		sbSql.append("SELECT NVL(B.DIS_RATE,0)   DIS_RATE, NVL(B.IS_F,0) IS_F\n");
		sbSql.append("  FROM TT_VS_ORDER    TVO,\n");
		sbSql.append("       TT_VS_ORDER_DETAIL    TVOD,\n");
		sbSql.append("       VW_MATERIAL_GROUP_MAT A1,\n");
		sbSql.append("       TT_SPECIAL_RATE_SET   B\n");
		sbSql.append(" WHERE TVOD.MATERIAL_ID = A1.MATERIAL_ID\n");
		sbSql.append("   AND TVO.ORDER_ID = TVOD.ORDER_ID\n");
		sbSql.append("   AND A1.SERIES_ID = B.GROUP_ID\n");
		sbSql.append("   AND B.GROUP_LEVEL = ?\n");
		// sbSql.append("   AND NVL(B.DIS_RATE,0) <> 0\n");
		sbSql.append("   AND B.FIN_TYPE = TVO.FUND_TYPE_ID\n");
		sbSql.append(" GROUP BY B.DIS_RATE,B.IS_F");

		params.add(2);
		seriesRateMap = dao.pageQueryMap(sbSql.toString(), params, getFunName());

		if (seriesRateMap != null)
		{
			specalRate = seriesRateMap.get("DIS_RATE").toString();
			isf = seriesRateMap.get("IS_F").toString().equals("0") ? false : true;
		}
		else
		{
			sbSql.delete(0, sbSql.length());
			params.clear();
			params.add(3);

			sbSql.append("SELECT NVL(B.DIS_RATE,0)   DIS_RATE, NVL(B.IS_F,0) IS_F\n");
			sbSql.append("  FROM TT_VS_ORDER    TVO,\n");
			sbSql.append("       TT_VS_ORDER_DETAIL    TVOD,\n");
			sbSql.append("       VW_MATERIAL_GROUP_MAT A1,\n");
			sbSql.append("       TT_SPECIAL_RATE_SET   B\n");
			sbSql.append(" WHERE TVOD.MATERIAL_ID = A1.MATERIAL_ID\n");
			sbSql.append("   AND TVO.ORDER_ID = TVOD.ORDER_ID\n");
			sbSql.append("   AND A1.MODEL_ID = B.GROUP_ID\n");
			sbSql.append("   AND B.GROUP_LEVEL = ?\n");
			// sbSql.append("   AND NVL(B.DIS_RATE,0) <> 0\n");
			sbSql.append("   AND B.FIN_TYPE = TVO.FUND_TYPE_ID\n");
			sbSql.append(" GROUP B.DIS_RATE,B.IS_F");

			modelRateMap = dao.pageQueryMap(sbSql.toString(), params, getFunName());

			if (modelRateMap != null)
			{
				specalRate = modelRateMap.get("DIS_RATE").toString();
				isf = modelRateMap.get("IS_F").toString().equals("0") ? false : true;
			}
		}

		sbSql.delete(0, sbSql.length());
		sbSql.append("WITH ");
		sbSql.append(ResourceSqlDao.getAsSqlSpecialResourceQuery(createId));
		sbSql.append(",\n");
		sbSql.append(ResourceSqlDao.getAsSqlCusOrderDetailQuery(orderId));
		sbSql.append("SELECT TCO.CUS_ORDER_ID,\n");
		sbSql.append("		 TVM.MATERIAL_ID,\n");
		sbSql.append("		 VMG.SERIES_ID,\n");
		sbSql.append("       VMG.SERIES_CODE,\n");
		sbSql.append("       VMG.SERIES_NAME,\n");
		sbSql.append("       VMG.MODEL_CODE,\n");
		sbSql.append("       VMG.MODEL_NAME,\n");
		sbSql.append("       VMG.PACKAGE_CODE,\n");
		sbSql.append("       VMG.PACKAGE_NAME,\n");
		sbSql.append("       VMG.MATERIAL_CODE,\n");
		sbSql.append("       VMG.MATERIAL_NAME,\n");
		sbSql.append("		 TVM.COLOR_NAME,\n");
		sbSql.append("       TCOD.DETAIL_ID,\n");
		sbSql.append("       TCOD.NUM CUS_NUM,\n");
		sbSql.append("		 NVL(TCOD.ORDER_NUM,0) - NVL(COD.ORDER_AMOUNT,0) ORDER_NUM,\n");
		sbSql.append("		 NVL(COD.ORDER_AMOUNT,0) ORDER_A,");
		sbSql.append("       NVL(TCOD.STAND_PRICE,0) C_PRICE,\n");
		// K 机
		// sbSql.append("       DECODE(VMG.MATERIAL_CODE, 'CH6390T2', 5.5,");
		if ((seriesRateMap != null || modelRateMap != null) && isf)
		{
			sbSql.append("		 " + specalRate + " + ");
			sbSql.append("		 NVL((" + ResourceSqlDao.getSqlFinAccRateQuery("COD.FUND_TYPE_ID", "TCO.YIELDLY") + "),0)) DIS_VALUE,");
		}
		else if ((seriesRateMap != null || modelRateMap != null) && !isf)
		{
			sbSql.append("		 " + specalRate + " AS DIS_VALUE,");
		}
		else
		{
			sbSql.append("		 (NVL((" + ResourceSqlDao.getSqlSeriesRateQuery("VMG.SERIES_ID", "TCO.YIELDLY") + "),0) + NVL(("
					+ ResourceSqlDao.getSqlFinAccRateQuery("COD.FUND_TYPE_ID", "TCO.YIELDLY") + "),0))) DIS_VALUE,");
		}
		sbSql.append("       NVL(VVOR.RESOURCE_AMOUNT, 0) RESOURCE_AMOUNT,\n");
		sbSql.append("       NVL(VVOR.RESOURCE_AMOUNT, 0) RESOURCE_AMOUNT_STATUS\n");
		sbSql.append("  FROM VW_MATERIAL_GROUP_MAT VMG,\n");
		sbSql.append("       TM_VHCL_MATERIAL      TVM,\n");
		sbSql.append("       VEHICLE_STOCK_ALL  VVOR,\n");
		sbSql.append("       TM_CUS_ORDER TCO,\n");
		sbSql.append("       TM_CUS_ORDER_DETAIL TCOD,\n");
		sbSql.append("       CUS_ORDER_DETIAL COD\n");
		sbSql.append("WHERE TCO.CUS_ORDER_ID = TCOD.CUS_ORDER_ID\n");
		sbSql.append("   AND TCOD.MAI_ID = TVM.MATERIAL_ID\n");
		sbSql.append("   AND TVM.MATERIAL_ID = VMG.MATERIAL_ID\n");
		sbSql.append("   AND VMG.MATERIAL_ID = VVOR.MATERIAL_ID(+)\n");
		sbSql.append("   AND TCOD.DETAIL_ID = COD.CUS_DETAIL_ID(+)\n");
		sbSql.append("   AND TCO.CUS_ORDER_ID = " + createId + "\n");

		return dao.pageQuery(sbSql.toString(), null, getFunName());
	}

	/**
	 * 方法描述 ： 获取车系的折扣率（车系折扣率 + 资金账户折扣率） <br/>
	 * 承诺类型的资金账户默认使用承兑的折扣率 修改： 2013-10-08 承诺类型的折扣率使用具体的资金类型折扣率
	 * 
	 * @param seriesId
	 *            车系ID
	 * @param yieldId
	 *            产地ID
	 * @param finType
	 *            资金账户类型
	 * @return map.get("DIS_VALUE")
	 * @author wangsongwei
	 */
	public Map<String, Object> getSeriesDiscount(String seriesId, String materialCode, String yieldId, String finType) throws Exception {
		List<Object> params = new ArrayList<Object>();

		String specalRate = "0";

		Map<String, Object> modelRateMap = null;
		Map<String, Object> seriesRateMap = null;
		boolean isf = false;

		StringBuffer sbSql = new StringBuffer();

		sbSql.append("SELECT NVL(B.DIS_RATE,0)     DIS_RATE, NVL(B.IS_F,0) IS_F\n");
		sbSql.append("  FROM TM_VHCL_MATERIAL      A,\n");
		sbSql.append("       VW_MATERIAL_GROUP_MAT A1,\n");
		sbSql.append("       TT_SPECIAL_RATE_SET   B\n");
		sbSql.append(" WHERE A.MATERIAL_ID = A1.MATERIAL_ID\n");
		sbSql.append("   AND A1.SERIES_ID = B.GROUP_ID\n");
		sbSql.append("   AND B.GROUP_LEVEL = ?\n");
		// sbSql.append("   AND NVL(B.DIS_RATE,0) <> 0\n");
		sbSql.append("   AND B.FIN_TYPE = ?\n");
		sbSql.append("   AND A1.MATERIAL_CODE = ?\n");
		sbSql.append(" GROUP BY B.DIS_RATE, B.IS_F");

		params.add(2);
		params.add(finType);
		params.add(materialCode);
		seriesRateMap = dao.pageQueryMap(sbSql.toString(), params, getFunName());

		if (seriesRateMap != null)
		{
			specalRate = seriesRateMap.get("DIS_RATE").toString();
			isf = seriesRateMap.get("IS_F").toString().equals("0") ? false : true;
		}
		else
		{
			sbSql.delete(0, sbSql.length());
			params.clear();
			params.add(3);
			params.add(finType);
			params.add(materialCode);

			sbSql.append("SELECT NVL(B.DIS_RATE,0)     DIS_RATE, NVL(B.IS_F,0) IS_F\n");
			sbSql.append("  FROM TM_VHCL_MATERIAL      A,\n");
			sbSql.append("       VW_MATERIAL_GROUP_MAT A1,\n");
			sbSql.append("       TT_SPECIAL_RATE_SET   B\n");
			sbSql.append(" WHERE A.MATERIAL_ID = A1.MATERIAL_ID\n");
			sbSql.append("   AND A1.MODEL_ID = B.GROUP_ID\n");
			sbSql.append("   AND B.GROUP_LEVEL = ?\n");
			// sbSql.append("   AND NVL(B.DIS_RATE,0) <> 0\n");
			sbSql.append("   AND B.FIN_TYPE = ?\n");
			sbSql.append("   AND A1.MATERIAL_CODE = ?\n");
			sbSql.append(" GROUP BY B.DIS_RATE, B.IS_F");

			modelRateMap = dao.pageQueryMap(sbSql.toString(), params, getFunName());

			if (modelRateMap != null)
			{
				specalRate = modelRateMap.get("DIS_RATE").toString();
				isf = modelRateMap.get("IS_F").toString().equals("0") ? false : true;
			}
		}

		// finType = finType.equals(Constant.ACCOUNT_TYPE_05.toString()) ?
		// Constant.ACCOUNT_TYPE_02.toString() : finType;

		sbSql.delete(0, sbSql.length());
		sbSql.append("-- 车系的折扣率(资金账户折扣 + 车系折扣)										\n");
		sbSql.append("SELECT																	\n");
		if ((seriesRateMap != null || modelRateMap != null) && isf)
		{
			sbSql.append("     " + specalRate + " +				\n");
			sbSql.append("     NVL(TMP1.DIS_VALUE,0) DIS_VALUE				\n");
		}
		else if ((seriesRateMap != null || modelRateMap != null) && !isf)
		{
			sbSql.append("     " + specalRate + " AS  DIS_VALUE				\n");
		}
		else
		{
			sbSql.append("     NVL(TMP1.DIS_VALUE,0) + NVL(TMP2.DIS_VALUE,0) DIS_VALUE				\n");
		}

		sbSql.append("FROM																		\n");
		sbSql.append("     (																	\n");
		sbSql.append("         SELECT TSFD.DIS_VALUE, TSFD.YIELDLY 								\n");
		sbSql.append("         FROM   TT_SALES_FIN_DIS TSFD										\n");
		sbSql.append("         WHERE															\n");
		sbSql.append("                TSFD.FIN_TYPE = ?											\n");
		sbSql.append("                AND TSFD.YIELDLY = ?										\n");
		sbSql.append("     ) TMP1																\n");
		sbSql.append("	   FULL JOIN															\n");
		sbSql.append("     (																	\n");
		sbSql.append("         SELECT TSD.DIS_VALUE, TSD.YIELDLY								\n");
		sbSql.append("         FROM   TT_SALES_DIS TSD											\n");
		sbSql.append("         WHERE															\n");
		sbSql.append("                TSD.YIELDLY = ?											\n");
		sbSql.append("                AND TSD.GROUP_ID = ?										\n");
		sbSql.append("     ) TMP2 																\n");
		sbSql.append("	   ON 																	\n");
		sbSql.append("	   TMP1.YIELDLY = TMP2.YIELDLY											\n");

		params.clear();
		params.add(finType);
		params.add(yieldId);
		params.add(yieldId);
		params.add(seriesId);

		return dao.pageQueryMap(sbSql.toString(), params, getFunName());
	}

	protected PO wrapperPO(ResultSet rs, int idx) {
		return null;
	}

	/**
	 * 方法描述 ：查询开票历史修改记录 <br/>
	 * 
	 * @param paramsMap
	 * @param curPage
	 * @param pageSize
	 * @return
	 * @author wangsongwei
	 */
	public PageResult<Map<String, Object>> getInvoHistory(Map<String, Object> map, Integer curPage, Integer pageSize) throws Exception {
		String orderNo = CommonUtils.checkNull(map.get("orderNo"));

		StringBuffer sbSql = new StringBuffer();

		sbSql.append("SELECT T.ORDER_TYPE, T.ORDER_NO, TO_CHAR(T.INVO_DATE,'YYYY-MM-DD') INVO_DATE, T.INVO_LAST_NO, T.INVO_LAST_VER, T.INVO_NO, T.INVO_VER, TO_CHAR(T.LAST_INVO_DATE,'YYYY-MM-DD') LAST_INVO_DATE\n");
		sbSql.append("  FROM TT_VS_ORDER_INVOLOG T\n");
		sbSql.append(" WHERE T.ORDER_NO = '" + orderNo + "'");

		return dao.pageQuery(sbSql.toString(), null, getFunName(), 10, 1);
	}

	public String getSqlQueryCondition(String queryString) {
		if (queryString != null && !"".equals(queryString))
		{
			String[] splitString = queryString.split(",");
			String splitConnectString = "";
			for (int i = 0; i < splitString.length; i++)
			{
				splitConnectString += "'" + splitString[i] + "'" + ",";
			}
			splitConnectString = splitConnectString.substring(0, splitConnectString.length() - 1);

			return splitConnectString;
		}
		else
		{
			return "''";
		}
	}

	/**
	 * 查询条件有IN的，将参数带入原来的查询SQL中
	 * 
	 * @param queryString
	 * @param params
	 * @return
	 */
	public String getSqlBuffer(String queryString, List<Object> params) {
		if (queryString != null && !"".equals(queryString))
		{
			String[] splitString = queryString.split(",");
			String splitConnectString = "";
			for (int i = 0; i < splitString.length; i++)
			{
				splitConnectString += "?,";
				params.add(splitString[i]);
			}
			splitConnectString = splitConnectString.substring(0, splitConnectString.length() - 1);

			return splitConnectString;
		}
		else
		{
			return "''";
		}
	}

	/**
	 * 查询条件有IN的，将参数带入原来的查询SQL中
	 * 
	 * @param queryString
	 * @param params
	 * @return
	 */
	public String getSqlBuffer(String[] splitString, List<Object> params) {
		if (splitString != null)
		{
			String splitConnectString = "";
			for (int i = 0; i < splitString.length; i++)
			{
				splitConnectString += "?,";
				params.add(splitString[i]);
			}
			splitConnectString = splitConnectString.substring(0, splitConnectString.length() - 1);

			return splitConnectString;
		}
		else
		{
			return "''";
		}
	}

	/**
	 * 方法描述 ： 查询所有的订单数据<br/>
	 * 
	 * @param orderId
	 * @return
	 * @author wangsongwei
	 */
	public Map<String, Object> getAllOrderInfo(String orderId) {
		List<Object> params = new ArrayList<Object>();
		params.add(orderId);
		params.add(orderId);

		StringBuffer sbSql = new StringBuffer();

		sbSql.append("SELECT " + Constant.ORDER_INVOICE_TYPE_01 + " AS ORDER_TYPE_DJ, A.ORDER_STATUS FROM TT_VS_ORDER A WHERE A.ORDER_ID = ?\n");
		sbSql.append("UNION ALL\n");
		sbSql.append("SELECT " + Constant.ORDER_INVOICE_TYPE_02 + " AS ORDER_TYPE_DJ, A.ORDER_STATUS FROM TT_OUT_ORDER A WHERE A.ORDER_ID = ?");

		return dao.pageQueryMap(sbSql.toString(), params, getFunName());
	}

	/**
	 * 方法描述 ： 实时库存信息查询<br/>
	 * 
	 * @param map
	 * @param curPage
	 * @param pageSize
	 * @return
	 * @author wangsongwei
	 */
	public PageResult<Map<String, Object>> getResourceAmountList(Map<String, Object> map, Integer curPage, Integer pageSize) throws Exception {
		List<Object> params = new ArrayList<Object>();

		String materialCode = CommonUtils.checkNull(map.get("materialCode"));
		String groupCode = CommonUtils.checkNull(map.get("groupCode"));
		String modelCode = CommonUtils.checkNull(map.get("modelCode"));
		String packageCode = CommonUtils.checkNull(map.get("packageCode"));
		String colorName = CommonUtils.checkNull(map.get("colorName"));
		String poseId = CommonUtils.checkNull(map.get("poseId"));
		String orgId = CommonUtils.checkNull(map.get("orgId"));

		StringBuffer sbSql = new StringBuffer();

		sbSql.append("-- 车厂未出库的库存，已经按照省份进行了分组\n");
		sbSql.append("-- 注意：带入查询条件需要加入产地查询权限\n");
		sbSql.append("WITH VEHICLE_STOCK_ALL AS (\n");
		sbSql.append("    SELECT\n");
		sbSql.append("           TV.MATERIAL_ID,\n");
		sbSql.append("           TV.PROV_ID ORG_ID,\n");
		sbSql.append("           TV.YIELDLY,\n");
		sbSql.append("           COUNT(TV.VEHICLE_ID) STOCK_AMOUNT -- 物料ID、车辆统计\n");
		sbSql.append("      FROM TM_VEHICLE TV\n");
		sbSql.append("     WHERE TV.LIFE_CYCLE = 10321002 -- 车厂库存（生命周期 - TC_CODE）\n");
		sbSql.append("       AND TV.LOCK_STATUS IN (10241001,10241008) -- 正常状态和配车锁定状态\n");
		sbSql.append("       AND TV.PROV_ID = -2222222222\n");
		sbSql.append("     GROUP BY TV.MATERIAL_ID, TV.YIELDLY,TV.PROV_ID\n");
		sbSql.append("    UNION ALL\n");
		sbSql.append("    SELECT\n");
		sbSql.append("           TV.MATERIAL_ID,\n");
		sbSql.append("           TV.PROV_ID ORG_ID,\n");
		sbSql.append("           TV.YIELDLY,\n");
		sbSql.append("           COUNT(TV.VEHICLE_ID) STOCK_AMOUNT -- 物料ID、车辆统计\n");
		sbSql.append("      FROM TM_VEHICLE TV\n");
		sbSql.append("     WHERE TV.LIFE_CYCLE = 10321002 --车厂库存（生命周期 - TC_CODE）\n");
		sbSql.append("       AND TV.LOCK_STATUS IN (10241001,10241008) --正常状态（锁定状态 - TC_CODE）\n");
		sbSql.append("       AND TV.PROV_ID <> -2222222222\n");
		sbSql.append("     GROUP BY TV.MATERIAL_ID, TV.YIELDLY,TV.PROV_ID\n");
		sbSql.append("),\n");
		sbSql.append("ORDER_INVO_STOCK AS (\n");
		sbSql.append("    -- 订单已经分配资源并且车辆没有出库的明细\n");
		sbSql.append("    SELECT A1.MATERIAL_ID,\n");
		sbSql.append("           A1.ORG_ID,\n");
		sbSql.append("           NVL(SUM(A1.AMOUNT), 0) SUB_AMOUNT,\n");
		sbSql.append("           NVL(SUM(A2.OUT_NUM), 0) OUT_AMOUNT,\n");
		sbSql.append("           NVL(SUM(A1.AMOUNT), 0) - NVL(SUM(A2.OUT_NUM), 0) INVO_AMOUNT\n");
		sbSql.append("      FROM TT_VS_ORDER_RESOURCE_RESERVE A1,\n");
		sbSql.append("           (SELECT A2.OR_DE_ID, A4.PROV_ID, COUNT(1) OUT_NUM\n");
		sbSql.append("              FROM TT_SALES_BO_DETAIL A2,\n");
		sbSql.append("                   TT_SALES_ALLOCA_DE A3,\n");
		sbSql.append("                   TM_VEHICLE         A4\n");
		sbSql.append("             WHERE A2.IS_ENABLE = 10041001\n");
		sbSql.append("               AND A3.BO_DE_ID = A2.BO_DE_ID\n");
		sbSql.append("               AND A3.VEHICLE_ID = A4.VEHICLE_ID\n");
		sbSql.append("				 AND A3.IS_OUT = 10041001\n");
		sbSql.append("             GROUP BY A2.OR_DE_ID, A4.PROV_ID) A2\n");
		sbSql.append("     WHERE A1.ORDER_DETAIL_ID = A2.OR_DE_ID(+)\n");
		sbSql.append("       AND A1.ORG_ID = A2.PROV_ID(+)\n");
		sbSql.append("       AND A1.RESERVE_STATUS = 11581001\n");
		sbSql.append("     GROUP BY A1.MATERIAL_ID, A1.ORG_ID\n");
		sbSql.append("),\n");
		sbSql.append("COMP_KEEP AS (\n");
		sbSql.append("     SELECT H.MATERIAL_ID,\n");
		sbSql.append("            H.ORG_ID,\n");
		sbSql.append("            NVL(H.NUM, 0) NUM\n");
		sbSql.append("       FROM TT_COMP_RES_KEEP H\n");
		sbSql.append("      WHERE H.NUM <> 0\n");
		sbSql.append(")\n");
		sbSql.append("SELECT VMT.SERIES_NAME,\n");
		sbSql.append("       VMT.SERIES_ID,\n");
		sbSql.append("       VMT.MODEL_NAME,\n");
		sbSql.append("       VMT.PACKAGE_NAME,\n");
		sbSql.append("       VMT.COLOR_NAME,\n");
		sbSql.append("       VMT.MATERIAL_NAME,\n");
		sbSql.append("       VMT.MATERIAL_ID,\n");
		sbSql.append("       VMT.MATERIAL_CODE,\n");
		sbSql.append("       VSA.YIELDLY,\n");
		sbSql.append("       VSA.ORG_ID,\n");
		sbSql.append("       DECODE(VSA.ORG_ID, -2222222222, '销售部', TORG.ORG_NAME) ORG_NAME,\n");
		sbSql.append("       NVL(VSA.STOCK_AMOUNT, 0) STOCK_AMOUNT, -- 可发车\n");
		sbSql.append("       NVL(OIS.INVO_AMOUNT, 0) INVO_AMOUNT, -- 已开票\n");
		sbSql.append("       NVL(CK.NUM, 0) KEEP_AMOUNT, -- 已锁定状态\n");
		sbSql.append("       NVL(VSA.STOCK_AMOUNT, 0) - NVL(OIS.INVO_AMOUNT, 0) - NVL(CK.NUM, 0) RES_AMOUNT\n");
		sbSql.append("  FROM VEHICLE_STOCK_ALL     VSA,\n");
		sbSql.append("       ORDER_INVO_STOCK      OIS,\n");
		sbSql.append("       COMP_KEEP             CK,\n");
		sbSql.append("       TM_ORG                TORG,\n");
		sbSql.append("       VW_MATERIAL_GROUP_MAT VMT\n");
		sbSql.append(" WHERE VSA.ORG_ID = TORG.ORG_ID(+)\n");
		sbSql.append("   AND VSA.MATERIAL_ID = VMT.MATERIAL_ID\n");
		sbSql.append("   AND VSA.ORG_ID = OIS.ORG_ID(+)\n");
		sbSql.append("   AND VSA.MATERIAL_ID = OIS.MATERIAL_ID(+)\n");
		sbSql.append("   AND VSA.ORG_ID = CK.ORG_ID(+)\n");
		sbSql.append("   AND VSA.MATERIAL_ID = CK.MATERIAL_ID(+)\n");

		sbSql.append(" -- 产地查询条件限制\n");
		sbSql.append(MaterialGroupManagerDao.getPoseIdBusinessSql("VSA.YIELDLY", poseId));

		if (!materialCode.equals(""))
		{
			sbSql.append(" AND VMT.MATERIAL_CODE IN (" + getSqlBuffer(materialCode, params) + ")\n");
		}

		if (!groupCode.equals(""))
		{
			sbSql.append(" AND VMT.SERIES_CODE IN (" + getSqlBuffer(groupCode, params) + ")\n");
		}

		if (!modelCode.equals(""))
		{
			sbSql.append(" AND VMT.MODEL_CODE IN (" + getSqlBuffer(modelCode, params) + ")\n");
		}

		if (!packageCode.equals(""))
		{
			sbSql.append(" AND VMT.PACKAGE_CODE IN (" + getSqlBuffer(packageCode, params) + ")\n");
		}

		if (!colorName.equals(""))
		{
			sbSql.append(" AND VMT.COLOR_NAME = ?");
			params.add(colorName);
		}

		if (!orgId.equals(""))
		{
			sbSql.append(" AND VSA.ORG_ID = ?\n");
			params.add(orgId);
		}

		sbSql.append(" ORDER BY VMT.SERIES_NAME, VMT.MODEL_NAME, VMT.PACKAGE_NAME, VMT.COLOR_NAME, VSA.ORG_ID, VMT.MATERIAL_ID");

		return dao.pageQuery(sbSql.toString(), params, getFunName(), pageSize, curPage);
	}

	/**
	 *  实时库存信息查询NEW去掉省份
	 * @author liufazhong
	 */
	public PageResult<Map<String, Object>> getResourceAmountDetailList(Map<String, Object> map, Integer curPage, Integer pageSize) throws Exception {
		List<Object> params = new ArrayList<Object>();

		String materialCode = CommonUtils.checkNull(map.get("materialCode"));
		String groupCode = CommonUtils.checkNull(map.get("groupCode"));
		String modelCode = CommonUtils.checkNull(map.get("modelCode"));
		String packageCode = CommonUtils.checkNull(map.get("packageCode"));
		String colorName = CommonUtils.checkNull(map.get("colorName"));
		String poseId = CommonUtils.checkNull(map.get("poseId"));
		String orgId = CommonUtils.checkNull(map.get("orgId"));

		StringBuffer sbSql = new StringBuffer();

		sbSql.append("-- 车厂未出库的库存，已经按照省份进行了分组\n");
		sbSql.append("-- 注意：带入查询条件需要加入产地查询权限\n");
		sbSql.append("WITH VEHICLE_STOCK_ALL AS (\n");
		sbSql.append("    SELECT\n");
		sbSql.append("           TV.MATERIAL_ID,\n");
		sbSql.append("           TV.YIELDLY,\n");
		sbSql.append("           COUNT(TV.VEHICLE_ID) STOCK_AMOUNT -- 物料ID、车辆统计\n");
		sbSql.append("      FROM TM_VEHICLE TV\n");
		sbSql.append("     WHERE TV.LIFE_CYCLE IN (10321002,10321008,10321009) -- 车厂库存,车辆出库,已生成运单（生命周期 - TC_CODE）\n");
		sbSql.append("       AND TV.LOCK_STATUS IN (10241001,10241008) -- 正常状态和配车锁定状态\n");
		sbSql.append("     GROUP BY TV.MATERIAL_ID, TV.YIELDLY),\n");
		sbSql.append("ORDER_INVO_STOCK AS (\n");
		sbSql.append("    -- 订单已经分配资源并且车辆没有出库的明细\n");
		sbSql.append("    SELECT A1.MATERIAL_ID,\n");
		sbSql.append("           NVL(SUM(A1.AMOUNT), 0) SUB_AMOUNT,\n");
		sbSql.append("           NVL(SUM(A2.OUT_NUM), 0) OUT_AMOUNT,\n");
		sbSql.append("           NVL(SUM(A1.AMOUNT), 0) - NVL(SUM(A2.OUT_NUM), 0) INVO_AMOUNT\n");
		sbSql.append("      FROM TT_VS_ORDER_RESOURCE_RESERVE A1,\n");
		sbSql.append("           (SELECT A2.OR_DE_ID, A4.PROV_ID, COUNT(1) OUT_NUM\n");
		sbSql.append("              FROM TT_SALES_BO_DETAIL A2,\n");
		sbSql.append("                   TT_SALES_ALLOCA_DE A3,\n");
		sbSql.append("                   TM_VEHICLE         A4\n");
		sbSql.append("             WHERE A2.IS_ENABLE = 10041001\n");
		sbSql.append("               AND A3.BO_DE_ID = A2.BO_DE_ID\n");
		sbSql.append("               AND A3.VEHICLE_ID = A4.VEHICLE_ID\n");
		sbSql.append("				 AND A3.IS_OUT = 10041001\n");
		sbSql.append("             GROUP BY A2.OR_DE_ID, A4.PROV_ID) A2\n");
		sbSql.append("     WHERE A1.ORDER_DETAIL_ID = A2.OR_DE_ID(+)\n");
		sbSql.append("       AND A1.ORG_ID = A2.PROV_ID(+)\n");
		sbSql.append("       AND A1.RESERVE_STATUS = 11581001\n");
		sbSql.append("     GROUP BY A1.MATERIAL_ID\n");
		sbSql.append("),\n");
		sbSql.append("COMP_KEEP AS (\n");
		sbSql.append("     SELECT H.MATERIAL_ID,\n");
		sbSql.append("            NVL(H.NUM, 0) NUM\n");
		sbSql.append("       FROM TT_COMP_RES_KEEP H\n");
		sbSql.append("      WHERE H.NUM <> 0\n");
		sbSql.append(")\n");
		sbSql.append("SELECT VMT.SERIES_NAME,\n");
		sbSql.append("       VMT.SERIES_ID,\n");
		sbSql.append("       VMT.MODEL_NAME,\n");
		sbSql.append("       VMT.PACKAGE_NAME,\n");
		sbSql.append("       VMT.COLOR_NAME,\n");
		sbSql.append("       VMT.MATERIAL_NAME,\n");
		sbSql.append("       VMT.MATERIAL_ID,\n");
		sbSql.append("       VMT.MATERIAL_CODE,\n");
		sbSql.append("       VSA.YIELDLY,\n");
		sbSql.append("       NVL(VSA.STOCK_AMOUNT, 0) STOCK_AMOUNT, -- 可发车\n");
		sbSql.append("       NVL(OIS.INVO_AMOUNT, 0) INVO_AMOUNT, -- 已开票\n");
		sbSql.append("       NVL(CK.NUM, 0) KEEP_AMOUNT, -- 已锁定状态\n");
		sbSql.append("       NVL(VSA.STOCK_AMOUNT, 0) - NVL(OIS.INVO_AMOUNT, 0) - NVL(CK.NUM, 0) RES_AMOUNT\n");
		sbSql.append("  FROM VEHICLE_STOCK_ALL     VSA,\n");
		sbSql.append("       ORDER_INVO_STOCK      OIS,\n");
		sbSql.append("       COMP_KEEP             CK,\n");
		sbSql.append("       VW_MATERIAL_GROUP_MAT VMT\n");
		sbSql.append(" WHERE VSA.MATERIAL_ID = VMT.MATERIAL_ID\n");
		sbSql.append("   AND VSA.MATERIAL_ID = OIS.MATERIAL_ID(+)\n");
		sbSql.append("   AND VSA.MATERIAL_ID = CK.MATERIAL_ID(+)\n");
		sbSql.append("   AND VMT.IS_SHOW = "+Constant.IF_TYPE_YES+"\n");

		sbSql.append(" -- 产地查询条件限制\n");
		sbSql.append(MaterialGroupManagerDao.getPoseIdBusinessSql("VSA.YIELDLY", poseId));

		if (!materialCode.equals(""))
		{
			sbSql.append(" AND VMT.MATERIAL_CODE IN (" + getSqlBuffer(materialCode, params) + ")\n");
		}

		if (!groupCode.equals(""))
		{
			sbSql.append(" AND VMT.SERIES_CODE IN (" + getSqlBuffer(groupCode, params) + ")\n");
		}

		if (!modelCode.equals(""))
		{
			sbSql.append(" AND VMT.MODEL_CODE IN (" + getSqlBuffer(modelCode, params) + ")\n");
		}

		if (!packageCode.equals(""))
		{
			sbSql.append(" AND VMT.PACKAGE_CODE IN (" + getSqlBuffer(packageCode, params) + ")\n");
		}

		if (!colorName.equals(""))
		{
			sbSql.append(" AND VMT.COLOR_NAME = ?");
			params.add(colorName);
		}

		sbSql.append(" ORDER BY VMT.SERIES_NAME, VMT.MODEL_NAME, VMT.PACKAGE_NAME, VMT.COLOR_NAME, VMT.MATERIAL_ID");

		return dao.pageQuery(sbSql.toString(), params, getFunName(), pageSize, curPage);
	}
	
	public List<Map<String, Object>> exportResourceAmountRepertory(Map<String, Object> map){
		List<Object> params = new ArrayList<Object>();

		String materialCode = CommonUtils.checkNull(map.get("materialCode"));
		String groupCode = CommonUtils.checkNull(map.get("groupCode"));
		String modelCode = CommonUtils.checkNull(map.get("modelCode"));
		String packageCode = CommonUtils.checkNull(map.get("packageCode"));
		String colorName = CommonUtils.checkNull(map.get("colorName"));
		String poseId = CommonUtils.checkNull(map.get("poseId"));
		String orgId = CommonUtils.checkNull(map.get("orgId"));
		String yieldly = CommonUtils.checkNull(map.get("yieldly"));//产地ID

		StringBuffer sbSql = new StringBuffer();

		sbSql.append("-- 车厂未出库的库存，已经按照省份进行了分组\n");
		sbSql.append("-- 注意：带入查询条件需要加入产地查询权限\n");
		sbSql.append("WITH VEHICLE_STOCK_ALL AS (\n");
		sbSql.append("    SELECT\n");
		sbSql.append("           TV.MATERIAL_ID,\n");
		sbSql.append("           TV.YIELDLY,\n");
		sbSql.append("           COUNT(TV.VEHICLE_ID) STOCK_AMOUNT -- 物料ID、车辆统计\n");
		sbSql.append("      FROM TM_VEHICLE TV\n");
		sbSql.append("     WHERE TV.LIFE_CYCLE = 10321002 -- 车厂库存（生命周期 - TC_CODE）\n");
		//sbSql.append("       AND TV.LOCK_STATUS IN (10241001,10241007,10241008) -- 正常状态和配车锁定状态\n");
		sbSql.append("     GROUP BY TV.MATERIAL_ID, TV.YIELDLY),\n");
		
		sbSql.append(" VEHICLE_ALL_AMOUNT AS (\n");
		sbSql.append("    SELECT\n");
		sbSql.append("           TV.MATERIAL_ID,\n");
		sbSql.append("           TV.YIELDLY,\n");
		sbSql.append("           COUNT(TV.VEHICLE_ID) STOCK_AMOUNT -- 物料ID、车辆统计\n");
		sbSql.append("      FROM TM_VEHICLE TV\n");
		sbSql.append("     WHERE TV.LIFE_CYCLE IN (10321002,10321008,10321009) -- 车厂库存,车辆出库,已生成运单（生命周期 - TC_CODE）\n");
		sbSql.append("      GROUP BY TV.MATERIAL_ID, TV.YIELDLY),\n");
		
		sbSql.append(" VEHICLE_STOCK_ENABLED AS (\n");
		sbSql.append("    SELECT\n");
		sbSql.append("           TV.MATERIAL_ID,\n");
		sbSql.append("           TV.YIELDLY,\n");
		sbSql.append("           COUNT(TV.VEHICLE_ID) STOCK_AMOUNT -- 物料ID、车辆统计\n");
		sbSql.append("      FROM TM_VEHICLE TV\n");
		sbSql.append("     WHERE TV.LIFE_CYCLE IN (10321002,10321008,10321009) -- 车厂库存,车辆出库,已生成运单（生命周期 - TC_CODE）\n");
		sbSql.append("       AND TV.LOCK_STATUS IN (10241001,10241008) -- 正常状态和配车锁定状态\n");
		sbSql.append("     GROUP BY TV.MATERIAL_ID, TV.YIELDLY),\n");
		sbSql.append("ORDER_INVO_STOCK AS (\n");
		sbSql.append("    -- 订单已经分配资源并且车辆没有出库的明细\n");
		sbSql.append("    SELECT A1.MATERIAL_ID,\n");
		sbSql.append("           NVL(SUM(A1.AMOUNT), 0) SUB_AMOUNT,\n");
		sbSql.append("           NVL(SUM(A2.OUT_NUM), 0) OUT_AMOUNT,\n");
		sbSql.append("           NVL(SUM(A1.AMOUNT), 0) - NVL(SUM(A2.OUT_NUM), 0) INVO_AMOUNT\n");
		sbSql.append("      FROM TT_VS_ORDER_RESOURCE_RESERVE A1,\n");
		sbSql.append("           (SELECT A2.OR_DE_ID, A4.PROV_ID, COUNT(1) OUT_NUM\n");
		sbSql.append("              FROM TT_SALES_BO_DETAIL A2,\n");
		sbSql.append("                   TT_SALES_ALLOCA_DE A3,\n");
		sbSql.append("                   TM_VEHICLE         A4\n");
		sbSql.append("             WHERE A2.IS_ENABLE = 10041001\n");
		sbSql.append("               AND A3.BO_DE_ID = A2.BO_DE_ID\n");
		sbSql.append("               AND A3.VEHICLE_ID = A4.VEHICLE_ID\n");
		sbSql.append("				 AND A3.IS_OUT = 10041001\n");
		sbSql.append("             GROUP BY A2.OR_DE_ID, A4.PROV_ID) A2\n");
		sbSql.append("     WHERE A1.ORDER_DETAIL_ID = A2.OR_DE_ID(+)\n");
		sbSql.append("       AND A1.ORG_ID = A2.PROV_ID(+)\n");
		sbSql.append("       AND A1.RESERVE_STATUS = 11581001\n");
		sbSql.append("     GROUP BY A1.MATERIAL_ID\n");
		sbSql.append("),\n");
		sbSql.append("COMP_KEEP AS (\n");
		sbSql.append("     SELECT H.MATERIAL_ID,\n");
		sbSql.append("            NVL(H.NUM, 0) NUM\n");
		sbSql.append("       FROM TT_COMP_RES_KEEP H\n");
		sbSql.append("      WHERE H.NUM <> 0\n");
		sbSql.append("),\n");
		sbSql.append("RESERVE_AMOUNT AS (SELECT TV.MATERIAL_ID,\n");
		sbSql.append("        COUNT(TV.VEHICLE_ID) RESERVE_AMOUNT\n");
		sbSql.append("   FROM TM_VEHICLE TV\n");
		sbSql.append("  WHERE TV.LIFE_CYCLE = 10321002\n");
		sbSql.append("    AND TV.LOCK_STATUS = 10241003\n");
		sbSql.append("    GROUP BY TV.MATERIAL_ID,TV.YIELDLY),\n");
		
		sbSql.append("MATTER_AMOUNT AS (SELECT TV.MATERIAL_ID,\n");
		sbSql.append("        COUNT(TV.VEHICLE_ID) MATTER_AMOUNT\n");
		sbSql.append("   FROM TM_VEHICLE TV\n");
		sbSql.append("  WHERE TV.LIFE_CYCLE = 10321002\n");
		sbSql.append("    AND TV.LOCK_STATUS = 10241004\n");
		sbSql.append("    GROUP BY TV.MATERIAL_ID,TV.YIELDLY),\n"); 
		
		sbSql.append("BORROW_A AS (SELECT TV.MATERIAL_ID,\n");
		sbSql.append("        COUNT(TV.VEHICLE_ID) BORROW_AMOUNT\n");
		sbSql.append("   FROM TM_VEHICLE TV\n");
		sbSql.append("  WHERE TV.LIFE_CYCLE = 10321002\n");
		sbSql.append("    AND TV.LOCK_STATUS = 10241007\n");
		sbSql.append("    GROUP BY TV.MATERIAL_ID,TV.YIELDLY)\n"); 
		sbSql.append("SELECT DECODE(GROUPING_ID(VMT.MODEL_NAME), 1, VMT.SERIES_NAME, DECODE(GROUPING_ID(VMT.PACKAGE_NAME), 1, VMT.SERIES_NAME, VMT.SERIES_NAME)) SERIES_NAME,\n");
//		sbSql.append("       VMT.SERIES_ID,\n");
		sbSql.append("       DECODE(GROUPING_ID(VMT.MODEL_NAME), 1, '合计', DECODE(GROUPING_ID(VMT.PACKAGE_NAME), 1,VMT.MODEL_NAME||'小计', VMT.MODEL_NAME)) MODEL_NAME,\n");
		sbSql.append("       VMT.PACKAGE_NAME,\n");
		sbSql.append("       VMT.COLOR_NAME,\n");
//		sbSql.append("       VMT.MATERIAL_NAME,\n");
		sbSql.append("       VMT.MATERIAL_ID,\n");
		sbSql.append("       VMT.MATERIAL_CODE,\n");
		sbSql.append("       VAA.YIELDLY,\n");
		sbSql.append("       TBA.AREA_NAME WAREHOUSE_NAME,\n");
		//sbSql.append("       DECODE(GROUPING_ID(VMT.MODEL_NAME), 1, sum(NVL(AA.ALL_AMOUNT,0)), DECODE(GROUPING_ID(VMT.PACKAGE_NAME), 1, sum(NVL(AA.ALL_AMOUNT,0)), sum(NVL(AA.ALL_AMOUNT,0)))) ALL_AMOUNT,\n");
		sbSql.append("       DECODE(GROUPING_ID(VMT.MODEL_NAME), 1,sum(NVL(BA.BORROW_AMOUNT,0)), DECODE(GROUPING_ID(VMT.PACKAGE_NAME), 1, sum(NVL(BA.BORROW_AMOUNT,0)), sum(NVL(BA.BORROW_AMOUNT,0)))) BORROW_AMOUNT,\n");
		sbSql.append("       DECODE(GROUPING_ID(VMT.MODEL_NAME), 1, sum(NVL(RA.RESERVE_AMOUNT,0)), DECODE(GROUPING_ID(VMT.PACKAGE_NAME), 1, sum(NVL(RA.RESERVE_AMOUNT,0)), sum(NVL(RA.RESERVE_AMOUNT,0)))) RESERVE_AMOUNT,\n");
		sbSql.append("       DECODE(GROUPING_ID(VMT.MODEL_NAME), 1, sum(NVL(MA.MATTER_AMOUNT,0)), DECODE(GROUPING_ID(VMT.PACKAGE_NAME), 1, sum(NVL(MA.MATTER_AMOUNT,0)), sum(NVL(MA.MATTER_AMOUNT,0)))) MATTER_AMOUNT,\n");
		//sbSql.append("       DECODE(GROUPING_ID(VMT.MODEL_NAME), 1,sum(NVL(TA.TRANSIT_AMOUNT,0)), DECODE(GROUPING_ID(VMT.PACKAGE_NAME), 1, sum(NVL(TA.TRANSIT_AMOUNT,0)), sum(NVL(TA.TRANSIT_AMOUNT,0)))) TRANSIT_AMOUNT,\n");
		//sbSql.append("       DECODE(GROUPING_ID(VMT.MODEL_NAME), 1, sum(NVL(PA.PROVIDER_AMOUNT,0)), DECODE(GROUPING_ID(VMT.PACKAGE_NAME), 1, sum(NVL(PA.PROVIDER_AMOUNT,0)), sum(NVL(PA.PROVIDER_AMOUNT,0)))) PROVIDER_AMOUNT,\n");
		//sbSql.append("       DECODE(GROUPING_ID(VMT.MODEL_NAME), 1, sum(NVL(VSE.STOCK_AMOUNT, 0)), DECODE(GROUPING_ID(VMT.PACKAGE_NAME), 1,sum(NVL(VSE.STOCK_AMOUNT, 0)), sum(NVL(VSE.STOCK_AMOUNT, 0)))) STOCK_AMOUNT, -- 可发车\n");
		sbSql.append(
		"DECODE(GROUPING_ID(VMT.MODEL_NAME), 1, sum(NVL(BA.BORROW_AMOUNT,0)+NVL(MA.MATTER_AMOUNT,0)+NVL(CK.NUM, 0)\n" +
		"       +NVL(VSE.STOCK_AMOUNT, 0) - NVL(OIS.INVO_AMOUNT, 0) - NVL(CK.NUM, 0)), DECODE(GROUPING_ID(VMT.PACKAGE_NAME), 1, sum(NVL(BA.BORROW_AMOUNT,0)+NVL(MA.MATTER_AMOUNT,0)+NVL(CK.NUM, 0)\n" + 
		"       +NVL(VSE.STOCK_AMOUNT, 0) - NVL(OIS.INVO_AMOUNT, 0) - NVL(CK.NUM, 0)), sum(NVL(BA.BORROW_AMOUNT,0)+NVL(MA.MATTER_AMOUNT,0)+NVL(CK.NUM, 0)\n" + 
		"       +NVL(VSE.STOCK_AMOUNT, 0) - NVL(OIS.INVO_AMOUNT, 0) - NVL(CK.NUM, 0)))) STOCK_AMOUNT, -- 可发车");

		sbSql.append("       DECODE(GROUPING_ID(VMT.MODEL_NAME), 1, sum(NVL(OIS.INVO_AMOUNT, 0)), DECODE(GROUPING_ID(VMT.PACKAGE_NAME), 1, sum(NVL(OIS.INVO_AMOUNT, 0)), sum(NVL(OIS.INVO_AMOUNT, 0)))) INVO_AMOUNT, -- 已开票\n");
		sbSql.append("       DECODE(GROUPING_ID(VMT.MODEL_NAME), 1, sum(NVL(CK.NUM, 0)), DECODE(GROUPING_ID(VMT.PACKAGE_NAME), 1, sum(NVL(CK.NUM, 0)), sum(NVL(CK.NUM, 0)))) KEEP_AMOUNT, -- 已锁定状态\n");
		sbSql.append("       DECODE(GROUPING_ID(VMT.MODEL_NAME), 1,sum(NVL(VSE.STOCK_AMOUNT, 0) - NVL(OIS.INVO_AMOUNT, 0) - NVL(CK.NUM, 0)), DECODE(GROUPING_ID(VMT.PACKAGE_NAME), 1, sum(NVL(VSE.STOCK_AMOUNT, 0) - NVL(OIS.INVO_AMOUNT, 0) - NVL(CK.NUM, 0)), sum(NVL(VSE.STOCK_AMOUNT, 0) - NVL(OIS.INVO_AMOUNT, 0) - NVL(CK.NUM, 0)))) RES_AMOUNT,\n");
		sbSql.append("       DECODE(GROUPING_ID(VMT.MODEL_NAME), 1,sum(NVL(VAA.STOCK_AMOUNT,0)), DECODE(GROUPING_ID(VMT.PACKAGE_NAME), 1,sum(NVL(VAA.STOCK_AMOUNT,0)), sum(NVL(VAA.STOCK_AMOUNT,0)))) TAL_AMOUNT,--总库存\n");
		sbSql.append("       DECODE(GROUPING_ID(VMT.MODEL_NAME), 1,sum(NVL(VSA.STOCK_AMOUNT,0)), DECODE(GROUPING_ID(VMT.PACKAGE_NAME), 1,sum(NVL(VSA.STOCK_AMOUNT,0)), sum(NVL(VSA.STOCK_AMOUNT,0)))) SJ_AMOUNT --实际库存\n");
		sbSql.append("  FROM VEHICLE_ALL_AMOUNT VAA,\n");
		sbSql.append("       ORDER_INVO_STOCK      OIS,\n");
		sbSql.append("       COMP_KEEP             CK,\n");
		sbSql.append("       VEHICLE_STOCK_ENABLED VSE,\n");
		sbSql.append("       VW_MATERIAL_GROUP_MAT VMT,\n");
		//sbSql.append("       ALL_AMOUNT AA,\n");
		sbSql.append("       RESERVE_AMOUNT RA,\n");
		sbSql.append("       MATTER_AMOUNT MA,\n");
		sbSql.append("       VEHICLE_STOCK_ALL     VSA,\n");
		sbSql.append("       TM_BUSINESS_AREA      TBA,\n");
		sbSql.append("       BORROW_A BA");
		//sbSql.append("       TRANSIT_AMOUNT TA,\n");
		//sbSql.append("       PROVIDER_AMOUNT PA\n");
		sbSql.append(" WHERE VAA.MATERIAL_ID = VMT.MATERIAL_ID\n");
		sbSql.append("   AND VAA.MATERIAL_ID = OIS.MATERIAL_ID(+)\n");
		sbSql.append("   AND VAA.MATERIAL_ID = CK.MATERIAL_ID(+)\n");
		//sbSql.append("   AND VAA.MATERIAL_ID = AA.MATERIAL_ID(+)\n");
		sbSql.append("   AND VAA.MATERIAL_ID = RA.MATERIAL_ID(+)\n");
		sbSql.append("   AND VAA.MATERIAL_ID = MA.MATERIAL_ID(+)\n");
		//sbSql.append("   AND VAA.MATERIAL_ID = TA.MATERIAL_ID(+)\n");
		//sbSql.append("   AND VAA.MATERIAL_ID = PA.MATERIAL_ID(+)\n");
		sbSql.append("   AND VAA.MATERIAL_ID = BA.MATERIAL_ID(+)\n");
		sbSql.append("   AND VAA.MATERIAL_ID = VSE.MATERIAL_ID(+)\n");
		sbSql.append("   AND VAA.MATERIAL_ID = VSA.MATERIAL_ID(+)\n");
		sbSql.append("   AND VAA.YIELDLY=TBA.AREA_ID\n");
		//sbSql.append("   AND VMT.IS_SHOW = "+Constant.IF_TYPE_YES+"\n");
		sbSql.append(" -- 产地查询条件限制\n");
		sbSql.append(MaterialGroupManagerDao.getPoseIdBusinessSql("VAA.YIELDLY", poseId));

		if (!materialCode.equals(""))
		{
			sbSql.append(" AND VMT.MATERIAL_CODE IN (" + getSqlBuffer(materialCode, params) + ")\n");
		}

		if (!groupCode.equals(""))
		{
			sbSql.append(" AND VMT.SERIES_CODE IN (" + getSqlBuffer(groupCode, params) + ")\n");
		}

		if (!modelCode.equals(""))
		{
			sbSql.append(" AND VMT.MODEL_CODE IN (" + getSqlBuffer(modelCode, params) + ")\n");
		}

		if (!packageCode.equals(""))
		{
			sbSql.append(" AND VMT.PACKAGE_CODE IN (" + getSqlBuffer(packageCode, params) + ")\n");
		}

		if (!colorName.equals(""))
		{
			sbSql.append(" AND VMT.COLOR_NAME = ?");
			params.add(colorName);
		}
		if (!yieldly.equals(""))//根据产地筛选
		{
			sbSql.append(" AND VAA.YIELDLY= ?");
			params.add(yieldly);
			//sbSql.append("AND TDW.WAREHOUSE_NAME LIKE '%"+wareHouseName+"%'\n");
			//params.add(wareHouseName);
		}
		//sbSql.append("  group by ROLLUP((VMT.SERIES_NAME,VMT.MODEL_NAME),(VMT.PACKAGE_NAME,VMT.COLOR_NAME,VAA.YIELDLY,VMT.MATERIAL_ID,VMT.MATERIAL_CODE,VAA.WAREHOUSE_ID,TDW.WAREHOUSE_NAME))\n");
		sbSql.append("   group by ROLLUP((VAA.YIELDLY,VMT.SERIES_NAME,VMT.MODEL_NAME),(VMT.PACKAGE_NAME,VMT.COLOR_NAME,VMT.MATERIAL_ID,VMT.MATERIAL_CODE,TBA.AREA_NAME))\n");
		sbSql.append(" ORDER BY VMT.SERIES_NAME, VMT.MODEL_NAME, VMT.PACKAGE_NAME, VMT.COLOR_NAME, VMT.MATERIAL_ID");
		return pageQuery(sbSql.toString(), params, getFunName());
	
	}
	/**
	 * 车厂库存查询
	 * @author liufazhong
	 */
	public PageResult<Map<String, Object>> getResourceAmountRepertory(Map<String, Object> map, Integer curPage, Integer pageSize) throws Exception {
		List<Object> params = new ArrayList<Object>();

		String materialCode = CommonUtils.checkNull(map.get("materialCode"));
		String groupCode = CommonUtils.checkNull(map.get("groupCode"));
		String modelCode = CommonUtils.checkNull(map.get("modelCode"));
		String packageCode = CommonUtils.checkNull(map.get("packageCode"));
		String colorName = CommonUtils.checkNull(map.get("colorName"));
		String poseId = CommonUtils.checkNull(map.get("poseId"));
		String orgId = CommonUtils.checkNull(map.get("orgId"));
		String yieldly = CommonUtils.checkNull(map.get("yieldly"));//产地ID

		StringBuffer sbSql = new StringBuffer();

		sbSql.append("-- 车厂未出库的库存，已经按照省份进行了分组\n");
		sbSql.append("-- 注意：带入查询条件需要加入产地查询权限\n");
		sbSql.append("WITH VEHICLE_STOCK_ALL AS (\n");
		sbSql.append("    SELECT\n");
		sbSql.append("           TV.MATERIAL_ID,\n");
		sbSql.append("           TV.YIELDLY,\n");
		sbSql.append("           COUNT(TV.VEHICLE_ID) STOCK_AMOUNT -- 物料ID、车辆统计\n");
		sbSql.append("      FROM TM_VEHICLE TV\n");
		sbSql.append("     WHERE TV.LIFE_CYCLE = 10321002 -- 车厂库存（生命周期 - TC_CODE）\n");
		//sbSql.append("       AND TV.LOCK_STATUS IN (10241001,10241007,10241008) -- 正常状态和配车锁定状态\n");
		sbSql.append("     GROUP BY TV.MATERIAL_ID, TV.YIELDLY),\n");
		
		sbSql.append(" VEHICLE_ALL_AMOUNT AS (\n");
		sbSql.append("    SELECT\n");
		sbSql.append("           TV.MATERIAL_ID,\n");
		sbSql.append("           TV.YIELDLY,\n");
		sbSql.append("           COUNT(TV.VEHICLE_ID) STOCK_AMOUNT -- 物料ID、车辆统计\n");
		sbSql.append("      FROM TM_VEHICLE TV\n");
		sbSql.append("     WHERE TV.LIFE_CYCLE IN (10321002,10321008,10321009) -- 车厂库存,车辆出库,已生成运单（生命周期 - TC_CODE）\n");
		sbSql.append("      GROUP BY TV.MATERIAL_ID, TV.YIELDLY),\n");
		
		sbSql.append(" VEHICLE_STOCK_ENABLED AS (\n");
		sbSql.append("    SELECT\n");
		sbSql.append("           TV.MATERIAL_ID,\n");
		sbSql.append("           TV.YIELDLY,\n");
		sbSql.append("           COUNT(TV.VEHICLE_ID) STOCK_AMOUNT -- 物料ID、车辆统计\n");
		sbSql.append("      FROM TM_VEHICLE TV\n");
		sbSql.append("     WHERE TV.LIFE_CYCLE IN (10321002,10321008,10321009) -- 车厂库存,车辆出库,已生成运单（生命周期 - TC_CODE）\n");
		sbSql.append("       AND TV.LOCK_STATUS IN (10241001,10241008) -- 正常状态和配车锁定状态\n");
		sbSql.append("     GROUP BY TV.MATERIAL_ID, TV.YIELDLY),\n");
		sbSql.append("ORDER_INVO_STOCK AS (\n");
		sbSql.append("    -- 订单已经分配资源并且车辆没有出库的明细\n");
		sbSql.append("    SELECT A1.MATERIAL_ID,\n");
		sbSql.append("           NVL(SUM(A1.AMOUNT), 0) SUB_AMOUNT,\n");
		sbSql.append("           NVL(SUM(A2.OUT_NUM), 0) OUT_AMOUNT,\n");
		sbSql.append("           NVL(SUM(A1.AMOUNT), 0) - NVL(SUM(A2.OUT_NUM), 0) INVO_AMOUNT\n");
		sbSql.append("      FROM TT_VS_ORDER_RESOURCE_RESERVE A1,\n");
		sbSql.append("           (SELECT A2.OR_DE_ID, A4.PROV_ID, COUNT(1) OUT_NUM\n");
		sbSql.append("              FROM TT_SALES_BO_DETAIL A2,\n");
		sbSql.append("                   TT_SALES_ALLOCA_DE A3,\n");
		sbSql.append("                   TM_VEHICLE         A4\n");
		sbSql.append("             WHERE A2.IS_ENABLE = 10041001\n");
		sbSql.append("               AND A3.BO_DE_ID = A2.BO_DE_ID\n");
		sbSql.append("               AND A3.VEHICLE_ID = A4.VEHICLE_ID\n");
		sbSql.append("				 AND A3.IS_OUT = 10041001\n");
		sbSql.append("             GROUP BY A2.OR_DE_ID, A4.PROV_ID) A2\n");
		sbSql.append("     WHERE A1.ORDER_DETAIL_ID = A2.OR_DE_ID(+)\n");
		sbSql.append("       AND A1.ORG_ID = A2.PROV_ID(+)\n");
		sbSql.append("       AND A1.RESERVE_STATUS = 11581001\n");
		sbSql.append("     GROUP BY A1.MATERIAL_ID\n");
		sbSql.append("),\n");
		sbSql.append("COMP_KEEP AS (\n");
		sbSql.append("     SELECT H.MATERIAL_ID,\n");
		sbSql.append("            NVL(H.NUM, 0) NUM\n");
		sbSql.append("       FROM TT_COMP_RES_KEEP H\n");
		sbSql.append("      WHERE H.NUM <> 0\n");
		sbSql.append("),\n");
		sbSql.append("RESERVE_AMOUNT AS (SELECT TV.MATERIAL_ID,\n");
		sbSql.append("        COUNT(TV.VEHICLE_ID) RESERVE_AMOUNT\n");
		sbSql.append("   FROM TM_VEHICLE TV\n");
		sbSql.append("  WHERE TV.LIFE_CYCLE = 10321002\n");
		sbSql.append("    AND TV.LOCK_STATUS = 10241003\n");
		sbSql.append("    GROUP BY TV.MATERIAL_ID,TV.YIELDLY),\n");
		
		sbSql.append("MATTER_AMOUNT AS (SELECT TV.MATERIAL_ID,\n");
		sbSql.append("        COUNT(TV.VEHICLE_ID) MATTER_AMOUNT\n");
		sbSql.append("   FROM TM_VEHICLE TV\n");
		sbSql.append("  WHERE TV.LIFE_CYCLE = 10321002\n");
		sbSql.append("    AND TV.LOCK_STATUS = 10241004\n");
		sbSql.append("    GROUP BY TV.MATERIAL_ID,TV.YIELDLY),\n"); 
		
		sbSql.append("BORROW_A AS (SELECT TV.MATERIAL_ID,\n");
		sbSql.append("        COUNT(TV.VEHICLE_ID) BORROW_AMOUNT\n");
		sbSql.append("   FROM TM_VEHICLE TV\n");
		sbSql.append("  WHERE TV.LIFE_CYCLE = 10321002\n");
		sbSql.append("    AND TV.LOCK_STATUS = 10241007\n");
		sbSql.append("    GROUP BY TV.MATERIAL_ID,TV.YIELDLY)\n"); 
		sbSql.append("SELECT DECODE(GROUPING_ID(VMT.MODEL_NAME), 1, '<font color=\"#FF0000\" style=\"font-weight:bold\">'||VMT.SERIES_NAME||'</font>', DECODE(GROUPING_ID(VMT.PACKAGE_NAME), 1, '<font color=\"#33CC00\" style=\"font-weight:bold\">'||VMT.SERIES_NAME||'</font>', VMT.SERIES_NAME)) SERIES_NAME,\n");
//		sbSql.append("       VMT.SERIES_ID,\n");
		sbSql.append("       DECODE(GROUPING_ID(VMT.MODEL_NAME), 1, '<font color=\"#FF0000\" style=\"font-weight:bold\">合计</font>', DECODE(GROUPING_ID(VMT.PACKAGE_NAME), 1, '<font color=\"#33CC00\" style=\"font-weight:bold\">'||VMT.MODEL_NAME||'小计</font>', VMT.MODEL_NAME)) MODEL_NAME,\n");
		sbSql.append("       VMT.PACKAGE_NAME,\n");
		sbSql.append("       VMT.COLOR_NAME,\n");
//		sbSql.append("       VMT.MATERIAL_NAME,\n");
		sbSql.append("       VMT.MATERIAL_ID,\n");
		sbSql.append("       VMT.MATERIAL_CODE,\n");
		sbSql.append("       VAA.YIELDLY,\n");
		sbSql.append("       TBA.AREA_NAME WAREHOUSE_NAME,\n");
		//sbSql.append("       DECODE(GROUPING_ID(VMT.MODEL_NAME), 1, '<font color=\"#FF0000\" style=\"font-weight:bold\">'||sum(NVL(AA.ALL_AMOUNT,0))||'</font>', DECODE(GROUPING_ID(VMT.PACKAGE_NAME), 1, '<font color=\"#33CC00\" style=\"font-weight:bold\">'||sum(NVL(AA.ALL_AMOUNT,0))||'</font>', sum(NVL(AA.ALL_AMOUNT,0)))) ALL_AMOUNT,\n");
		sbSql.append("       DECODE(GROUPING_ID(VMT.MODEL_NAME), 1, '<font color=\"#FF0000\" style=\"font-weight:bold\">'||sum(NVL(BA.BORROW_AMOUNT,0))||'</font>', DECODE(GROUPING_ID(VMT.PACKAGE_NAME), 1, '<font color=\"#33CC00\" style=\"font-weight:bold\">'||sum(NVL(BA.BORROW_AMOUNT,0))||'</font>', sum(NVL(BA.BORROW_AMOUNT,0)))) BORROW_AMOUNT,\n");
		sbSql.append("       DECODE(GROUPING_ID(VMT.MODEL_NAME), 1, '<font color=\"#FF0000\" style=\"font-weight:bold\">'||sum(NVL(RA.RESERVE_AMOUNT,0))||'</font>', DECODE(GROUPING_ID(VMT.PACKAGE_NAME), 1, '<font color=\"#33CC00\" style=\"font-weight:bold\">'||sum(NVL(RA.RESERVE_AMOUNT,0))||'</font>', sum(NVL(RA.RESERVE_AMOUNT,0)))) RESERVE_AMOUNT,\n");
		sbSql.append("       DECODE(GROUPING_ID(VMT.MODEL_NAME), 1, '<font color=\"#FF0000\" style=\"font-weight:bold\">'||sum(NVL(MA.MATTER_AMOUNT,0))||'</font>', DECODE(GROUPING_ID(VMT.PACKAGE_NAME), 1, '<font color=\"#33CC00\" style=\"font-weight:bold\">'||sum(NVL(MA.MATTER_AMOUNT,0))||'</font>', sum(NVL(MA.MATTER_AMOUNT,0)))) MATTER_AMOUNT,\n");
		//sbSql.append("       DECODE(GROUPING_ID(VMT.MODEL_NAME), 1, '<font color=\"#FF0000\" style=\"font-weight:bold\">'||sum(NVL(TA.TRANSIT_AMOUNT,0))||'</font>', DECODE(GROUPING_ID(VMT.PACKAGE_NAME), 1, '<font color=\"#33CC00\" style=\"font-weight:bold\">'||sum(NVL(TA.TRANSIT_AMOUNT,0))||'</font>', sum(NVL(TA.TRANSIT_AMOUNT,0)))) TRANSIT_AMOUNT,\n");
		//sbSql.append("       DECODE(GROUPING_ID(VMT.MODEL_NAME), 1, '<font color=\"#FF0000\" style=\"font-weight:bold\">'||sum(NVL(PA.PROVIDER_AMOUNT,0))||'</font>', DECODE(GROUPING_ID(VMT.PACKAGE_NAME), 1, '<font color=\"#33CC00\" style=\"font-weight:bold\">'||sum(NVL(PA.PROVIDER_AMOUNT,0))||'</font>', sum(NVL(PA.PROVIDER_AMOUNT,0)))) PROVIDER_AMOUNT,\n");
		//sbSql.append("       DECODE(GROUPING_ID(VMT.MODEL_NAME), 1, '<font color=\"#FF0000\" style=\"font-weight:bold\">'||sum(NVL(VSE.STOCK_AMOUNT, 0))||'</font>', DECODE(GROUPING_ID(VMT.PACKAGE_NAME), 1, '<font color=\"#33CC00\" style=\"font-weight:bold\">'||sum(NVL(VSE.STOCK_AMOUNT, 0))||'</font>', sum(NVL(VSE.STOCK_AMOUNT, 0)))) STOCK_AMOUNT, -- 可发车\n");
		sbSql.append(
		"DECODE(GROUPING_ID(VMT.MODEL_NAME), 1, '<font color=\"#FF0000\" style=\"font-weight:bold\">'||sum(NVL(BA.BORROW_AMOUNT,0)+NVL(MA.MATTER_AMOUNT,0)+NVL(CK.NUM, 0)\n" +
		"       +NVL(VSE.STOCK_AMOUNT, 0) - NVL(OIS.INVO_AMOUNT, 0) - NVL(CK.NUM, 0))||'</font>', DECODE(GROUPING_ID(VMT.PACKAGE_NAME), 1, '<font color=\"#33CC00\" style=\"font-weight:bold\">'||sum(NVL(BA.BORROW_AMOUNT,0)+NVL(MA.MATTER_AMOUNT,0)+NVL(CK.NUM, 0)\n" + 
		"       +NVL(VSE.STOCK_AMOUNT, 0) - NVL(OIS.INVO_AMOUNT, 0) - NVL(CK.NUM, 0))||'</font>', sum(NVL(BA.BORROW_AMOUNT,0)+NVL(MA.MATTER_AMOUNT,0)+NVL(CK.NUM, 0)\n" + 
		"       +NVL(VSE.STOCK_AMOUNT, 0) - NVL(OIS.INVO_AMOUNT, 0) - NVL(CK.NUM, 0)))) STOCK_AMOUNT, -- 可发车");

		sbSql.append("       DECODE(GROUPING_ID(VMT.MODEL_NAME), 1, '<font color=\"#FF0000\" style=\"font-weight:bold\">'||sum(NVL(OIS.INVO_AMOUNT, 0))||'</font>', DECODE(GROUPING_ID(VMT.PACKAGE_NAME), 1, '<font color=\"#33CC00\" style=\"font-weight:bold\">'||sum(NVL(OIS.INVO_AMOUNT, 0))||'</font>', sum(NVL(OIS.INVO_AMOUNT, 0)))) INVO_AMOUNT, -- 已开票\n");
		sbSql.append("       DECODE(GROUPING_ID(VMT.MODEL_NAME), 1, '<font color=\"#FF0000\" style=\"font-weight:bold\">'||sum(NVL(CK.NUM, 0))||'</font>', DECODE(GROUPING_ID(VMT.PACKAGE_NAME), 1, '<font color=\"#33CC00\" style=\"font-weight:bold\">'||sum(NVL(CK.NUM, 0))||'</font>', sum(NVL(CK.NUM, 0)))) KEEP_AMOUNT, -- 已锁定状态\n");
		sbSql.append("       DECODE(GROUPING_ID(VMT.MODEL_NAME), 1, '<font color=\"#FF0000\" style=\"font-weight:bold\">'||sum(NVL(VSE.STOCK_AMOUNT, 0) - NVL(OIS.INVO_AMOUNT, 0) - NVL(CK.NUM, 0))||'</font>', DECODE(GROUPING_ID(VMT.PACKAGE_NAME), 1, '<font color=\"#33CC00\" style=\"font-weight:bold\">'||sum(NVL(VSE.STOCK_AMOUNT, 0) - NVL(OIS.INVO_AMOUNT, 0) - NVL(CK.NUM, 0))||'</font>', sum(NVL(VSE.STOCK_AMOUNT, 0) - NVL(OIS.INVO_AMOUNT, 0) - NVL(CK.NUM, 0)))) RES_AMOUNT,\n");
		sbSql.append("       DECODE(GROUPING_ID(VMT.MODEL_NAME), 1, '<font color=\"#FF0000\" style=\"font-weight:bold\">'||sum(NVL(VAA.STOCK_AMOUNT,0))||'</font>', DECODE(GROUPING_ID(VMT.PACKAGE_NAME), 1, '<font color=\"#33CC00\" style=\"font-weight:bold\">'||sum(NVL(VAA.STOCK_AMOUNT,0))||'</font>', sum(NVL(VAA.STOCK_AMOUNT,0)))) TAL_AMOUNT,--总库存\n");
		sbSql.append("       DECODE(GROUPING_ID(VMT.MODEL_NAME), 1, '<font color=\"#FF0000\" style=\"font-weight:bold\">'||sum(NVL(VSA.STOCK_AMOUNT,0))||'</font>', DECODE(GROUPING_ID(VMT.PACKAGE_NAME), 1, '<font color=\"#33CC00\" style=\"font-weight:bold\">'||sum(NVL(VSA.STOCK_AMOUNT,0))||'</font>', sum(NVL(VSA.STOCK_AMOUNT,0)))) SJ_AMOUNT --实际库存\n");
		sbSql.append("  FROM VEHICLE_ALL_AMOUNT VAA,\n");
		sbSql.append("       ORDER_INVO_STOCK      OIS,\n");
		sbSql.append("       COMP_KEEP             CK,\n");
		sbSql.append("       VEHICLE_STOCK_ENABLED VSE,\n");
		sbSql.append("       VW_MATERIAL_GROUP_MAT VMT,\n");
		//sbSql.append("       ALL_AMOUNT AA,\n");
		sbSql.append("       RESERVE_AMOUNT RA,\n");
		sbSql.append("       MATTER_AMOUNT MA,\n");
		sbSql.append("       VEHICLE_STOCK_ALL     VSA,\n");
		sbSql.append("       TM_BUSINESS_AREA      TBA,\n");
		sbSql.append("       BORROW_A BA");
		//sbSql.append("       TRANSIT_AMOUNT TA,\n");
		//sbSql.append("       PROVIDER_AMOUNT PA\n");
		sbSql.append(" WHERE VAA.MATERIAL_ID = VMT.MATERIAL_ID\n");
		sbSql.append("   AND VAA.MATERIAL_ID = OIS.MATERIAL_ID(+)\n");
		sbSql.append("   AND VAA.MATERIAL_ID = CK.MATERIAL_ID(+)\n");
		//sbSql.append("   AND VAA.MATERIAL_ID = AA.MATERIAL_ID(+)\n");
		sbSql.append("   AND VAA.MATERIAL_ID = RA.MATERIAL_ID(+)\n");
		sbSql.append("   AND VAA.MATERIAL_ID = MA.MATERIAL_ID(+)\n");
		//sbSql.append("   AND VAA.MATERIAL_ID = TA.MATERIAL_ID(+)\n");
		//sbSql.append("   AND VAA.MATERIAL_ID = PA.MATERIAL_ID(+)\n");
		sbSql.append("   AND VAA.MATERIAL_ID = BA.MATERIAL_ID(+)\n");
		sbSql.append("   AND VAA.MATERIAL_ID = VSE.MATERIAL_ID(+)\n");
		sbSql.append("   AND VAA.MATERIAL_ID = VSA.MATERIAL_ID(+)\n");
		sbSql.append("   AND VAA.YIELDLY=TBA.AREA_ID\n");
		//sbSql.append("   AND VMT.IS_SHOW = "+Constant.IF_TYPE_YES+"\n");--特殊订单新增物料用
		sbSql.append(" -- 产地查询条件限制\n");
		sbSql.append(MaterialGroupManagerDao.getPoseIdBusinessSql("VAA.YIELDLY", poseId));

		if (!materialCode.equals(""))
		{
			sbSql.append(" AND VMT.MATERIAL_CODE IN (" + getSqlBuffer(materialCode, params) + ")\n");
		}

		if (!groupCode.equals(""))
		{
			sbSql.append(" AND VMT.SERIES_CODE IN (" + getSqlBuffer(groupCode, params) + ")\n");
		}

		if (!modelCode.equals(""))
		{
			sbSql.append(" AND VMT.MODEL_CODE IN (" + getSqlBuffer(modelCode, params) + ")\n");
		}

		if (!packageCode.equals(""))
		{
			sbSql.append(" AND VMT.PACKAGE_CODE IN (" + getSqlBuffer(packageCode, params) + ")\n");
		}

		if (!colorName.equals(""))
		{
			sbSql.append(" AND VMT.COLOR_NAME = ?");
			params.add(colorName);
		}
		if (!yieldly.equals(""))//根据产地筛选
		{
			sbSql.append(" AND VAA.YIELDLY= ?");
			params.add(yieldly);
			//sbSql.append("AND TDW.WAREHOUSE_NAME LIKE '%"+wareHouseName+"%'\n");
			//params.add(wareHouseName);
		}
		//sbSql.append("  group by ROLLUP((VMT.SERIES_NAME,VMT.MODEL_NAME),(VMT.PACKAGE_NAME,VMT.COLOR_NAME,VAA.YIELDLY,VMT.MATERIAL_ID,VMT.MATERIAL_CODE,VAA.WAREHOUSE_ID,TDW.WAREHOUSE_NAME))\n");
		sbSql.append("   group by ROLLUP((VAA.YIELDLY,VMT.SERIES_NAME,VMT.MODEL_NAME),(VMT.PACKAGE_NAME,VMT.COLOR_NAME,VMT.MATERIAL_ID,VMT.MATERIAL_CODE,TBA.AREA_NAME))\n");
		sbSql.append(" ORDER BY VMT.SERIES_NAME, VMT.MODEL_NAME, VMT.PACKAGE_NAME, VMT.COLOR_NAME, VMT.MATERIAL_ID");

		return dao.pageQuery(sbSql.toString(), params, getFunName(), pageSize, curPage);
	}
	
	/**
	 * 方法描述 ： 实时库存信息查询<br/>
	 * 
	 * @param map
	 * @param curPage
	 * @param pageSize
	 * @return
	 * @author wangsongwei
	 */
	public PageResult<Map<String, Object>> getResourceAmountCountList(Map<String, Object> map, Integer curPage, Integer pageSize) throws Exception {
		List<Object> params = new ArrayList<Object>();

		String materialCode = CommonUtils.checkNull(map.get("materialCode"));
		String groupCode = CommonUtils.checkNull(map.get("groupCode"));
		String poseId = CommonUtils.checkNull(map.get("poseId"));
		String orgId = CommonUtils.checkNull(map.get("orgId"));

		StringBuffer sbSql = new StringBuffer();

		sbSql.append("SELECT VMT.SERIES_ID,\n");
		sbSql.append("       VMT.SERIES_NAME,\n");
		sbSql.append("       SUM(C.STORE_AMOUNT) STORE_AMOUNT,\n");
		sbSql.append("		 SUM(NVL(A.INVO_AMOUNT, 0)) INVO_AMOUNT,\n");
		sbSql.append("		 SUM(NVL(D.KEEP_AMOUNT, 0)) KEEP_AMOUNT,\n");
		sbSql.append("		 SUM(NVL(C.STORE_AMOUNT, 0) - NVL(A.INVO_AMOUNT, 0) - NVL(D.KEEP_AMOUNT, 0)) RES_AMOUNT");
		sbSql.append("  FROM (\n");
		sbSql.append("-- 订单已经分配资源并且车辆没有出库的明细\n");
		sbSql.append("SELECT A1.MATERIAL_ID,\n");
		sbSql.append("       NVL(SUM(A1.AMOUNT), 0) SUB_AMOUNT,\n");
		sbSql.append("       NVL(SUM(A2.OUT_NUM), 0) OUT_AMOUNT,\n");
		sbSql.append("       NVL(SUM(A1.AMOUNT), 0) - NVL(SUM(A2.OUT_NUM), 0) INVO_AMOUNT\n");
		sbSql.append("  FROM TT_VS_ORDER_RESOURCE_RESERVE A1,\n");
		sbSql.append("       (SELECT A2.OR_DE_ID, A4.PROV_ID, COUNT(1) OUT_NUM\n");
		sbSql.append("          FROM TT_SALES_BO_DETAIL A2,\n");
		sbSql.append("               TT_SALES_ALLOCA_DE A3,\n");
		sbSql.append("               TM_VEHICLE         A4\n");
		sbSql.append("         WHERE A2.IS_ENABLE = 10041001\n");
		sbSql.append("           AND A3.BO_DE_ID = A2.BO_DE_ID\n");
		sbSql.append("           AND A3.VEHICLE_ID = A4.VEHICLE_ID\n");
		sbSql.append("			 AND A3.IS_OUT = 10041001\n");
		sbSql.append("         GROUP BY A2.OR_DE_ID, A4.PROV_ID) A2\n");
		sbSql.append(" WHERE A1.ORDER_DETAIL_ID = A2.OR_DE_ID(+)\n");
		sbSql.append("   AND A1.ORG_ID = A2.PROV_ID(+)\n");
		sbSql.append("   AND A1.RESERVE_STATUS = 11581001\n");
		sbSql.append(" GROUP BY A1.MATERIAL_ID) A,\n");
		sbSql.append("       (SELECT A3.MATERIAL_ID, COUNT(1) STORE_AMOUNT\n");
		sbSql.append("          FROM TM_VEHICLE A3\n");
		sbSql.append("         WHERE A3.LIFE_CYCLE IN(10321002,10321008,10321009)\n");
		sbSql.append("           AND A3.LOCK_STATUS IN (10241001,10241008)\n");

		//		if (!orgId.equals(""))
		//		{
		//			sbSql.append(" AND P3.PROV_ID = ?\n");
		//			params.add(orgId);
		//		}

		sbSql.append(MaterialGroupManagerDao.getPoseIdBusinessSql("A3.YIELDLY", poseId));
		sbSql.append("         GROUP BY A3.MATERIAL_ID) C,\n");
		sbSql.append("       (SELECT A4.MATERIAL_ID, SUM(NVL(A4.NUM, 0)) KEEP_AMOUNT\n");
		sbSql.append("          FROM TT_COMP_RES_KEEP A4\n");
		sbSql.append("         GROUP BY A4.MATERIAL_ID) D,\n");
		sbSql.append("       VW_MATERIAL_GROUP_MAT VMT\n");
		sbSql.append(" WHERE C.MATERIAL_ID = D.MATERIAL_ID(+)\n");
		sbSql.append("   AND C.MATERIAL_ID = A.MATERIAL_ID(+)\n");
		sbSql.append("   AND C.MATERIAL_ID = VMT.MATERIAL_ID\n");
		sbSql.append("   AND VMT.IS_SHOW = "+Constant.IF_TYPE_YES+"\n");

		if (!groupCode.equals(""))
		{
			sbSql.append(" AND VMT.SERIES_CODE IN (" + getSqlBuffer(groupCode, params) + ")\n");

		}

		sbSql.append("GROUP BY VMT.SERIES_ID, VMT.SERIES_NAME");

		return dao.pageQuery(sbSql.toString(), params, getFunName(), pageSize, curPage);
	}

	/**
	 * 方法描述 ： 实时库存信息查询<br/>
	 * 
	 * @param map
	 * @param curPage
	 * @param pageSize
	 * @return
	 * @author wangsongwei
	 */
	public PageResult<Map<String, Object>> getFinancialPromiseQueryList(Map<String, Object> map, AclUserBean logonUser, Integer curPage, Integer pageSize) throws Exception {

		String orderNo = CommonUtils.checkNull(map.get("orderNo")); // 订单编号
		String orgCode = CommonUtils.checkNull(map.get("orgCode"));
		String dealerCode = CommonUtils.checkNull(map.get("dealerCode")); // 经销商代码
		String startdate = CommonUtils.checkNull(map.get("startdate")); // 提报日期
		String endDate = CommonUtils.checkNull(map.get("endDate")); // 提报日期
		String groupCode = CommonUtils.checkNull(map.get("groupCode")); // 物料组编号
		String payStatus = CommonUtils.checkNull(map.get("payStatus")); // 还款状态

		StringBuffer sbSql = new StringBuffer();

		sbSql.append("WITH ORDER_DETAIL AS (\n");
		sbSql.append("     SELECT " + Constant.ORDER_INVOICE_TYPE_01 + " AS ORDER_DJ_TYPE,\n");
		sbSql.append("            TVO.ORDER_ID,\n");
		sbSql.append("            TVO.ORDER_NO,\n");
		sbSql.append("            TVO.DEALER_ID,\n");
		sbSql.append("            TVO.AREA_ID,\n");
		sbSql.append("            TVO.RAISE_DATE,\n");
		sbSql.append("            TORG.ORG_ID,\n");
		sbSql.append("            VMG.SERIES_NAME,\n");
		sbSql.append("            VMG.MODEL_NAME,\n");
		sbSql.append("            VMG.PACKAGE_NAME,VMG.PACKAGE_CODE,\n");
		sbSql.append("            TSP.PRO_TYPE,\n");
		sbSql.append("            TSP.APPLY_NUM,\n");
		sbSql.append("            TSP.USE_NUM USE_NUM_ALL,\n");
		sbSql.append("            TVO.INVO_DATE,\n");
		sbSql.append("            TVO.INVOICE_NO,\n");
		sbSql.append("            TO_CHAR(TSP.PRO_DATE, 'YYYY-MM-DD') PRO_DATE,\n");
		sbSql.append("            TPOU.STATUS,\n");
		sbSql.append("            TVO.ORDER_YF_PRICE,\n");
		sbSql.append("            ((NVL(TVOD.CHK_PRICE,0)*NVL(TVO.SUB_NUM,0)-NVL(TVO.ORDER_YF_PRICE, 0)) / (NVL(TVOD.CHK_PRICE,0)*NVL(TVO.SUB_NUM,0)))*100 ZHDIS_RATE_CHECK,\n");
		sbSql.append("            (SUM(NVL(TPOU.USE_AMOUNT, 0))-NVL(TVO.REBATE_PRICE,0)) USE_AMOUNT,\n");
		sbSql.append("            SUM(NVL(TPOU.USE_NUM, 0)) USE_NUM,\n");
		sbSql.append("            TO_CHAR(TPOU.USE_DATE, 'YYYY-MM-DD') USE_DATE\n");
		sbSql.append("       FROM TT_VS_ORDER            TVO,\n");
		sbSql.append("            TT_VS_ORDER_DETAIL     TVOD,\n");
		sbSql.append("            VW_MATERIAL_GROUP_MAT  VMG,\n");
		sbSql.append("            TT_PROM_ORD_USE        TPOU,\n");
		sbSql.append("            TT_SALES_PROMISE_DET   TSPD,\n");
		sbSql.append("            TT_SALES_PROMISE       TSP,\n");
		sbSql.append("            TM_DEALER_ORG_RELATION TORGR,\n");
		sbSql.append("            TM_ORG                 TORG\n");
		sbSql.append("      WHERE TVO.ORDER_ID = TVOD.ORDER_ID\n");
		sbSql.append("        AND TVOD.DETAIL_ID = TPOU.ORD_DET_ID\n");
		sbSql.append("        AND TPOU.DETAIL_ID = TSPD.DETAIL_ID\n");
		sbSql.append("        AND TSPD.PRO_ID = TSP.PRO_ID\n");
		sbSql.append("        AND TVOD.MATERIAL_ID = VMG.MATERIAL_ID(+)\n");
		sbSql.append("        AND TVO.DEALER_ID = TORGR.DEALER_ID(+)\n");
		sbSql.append("        AND TORGR.ORG_ID = TORG.ORG_ID(+)\n");
		sbSql.append("        AND TPOU.ORDER_TYPE = " + Constant.ORDER_INVOICE_TYPE_01 + "\n");
		if (!groupCode.equals(""))
		{
			sbSql.append(MaterialGroupManagerDao.getGroupQuerySql("TVO.ORDER_ID", groupCode, Constant.ORDER_INVOICE_TYPE_01.toString()));
		}
		sbSql.append("      GROUP BY TVO.ORDER_ID,\n");
		sbSql.append("               TVO.ORDER_NO,\n");
		sbSql.append("               TVO.DEALER_ID,\n");
		sbSql.append("               TVO.AREA_ID,\n");
		sbSql.append("               TVO.RAISE_DATE,\n");
		sbSql.append("               TORG.ORG_ID,\n");
		sbSql.append("               VMG.SERIES_NAME,\n");
		sbSql.append("               VMG.MODEL_NAME,\n");
		sbSql.append("               VMG.PACKAGE_NAME,VMG.PACKAGE_CODE,\n");
		sbSql.append("               TSP.PRO_TYPE,\n");
		sbSql.append("               TSP.APPLY_NUM,\n");
		sbSql.append("               TSP.USE_NUM,\n");
		sbSql.append("               TVO.INVO_DATE,\n");
		sbSql.append("               TVO.INVOICE_NO,\n");
		sbSql.append("               TSP.PRO_DATE,\n");
		sbSql.append("               TPOU.STATUS,\n");
		sbSql.append("               TPOU.USE_DATE,\n");
		sbSql.append("               TVOD.CHK_PRICE,\n");
		sbSql.append("               TVO.SUB_NUM,\n");
		sbSql.append("               TVO.ORDER_YF_PRICE,TVO.REBATE_PRICE\n");
		sbSql.append("     UNION ALL\n");
		sbSql.append("     SELECT " + Constant.ORDER_INVOICE_TYPE_02 + " AS ORDER_DJ_TYPE,\n");
		sbSql.append("            TOO.ORDER_ID,\n");
		sbSql.append("            TOO.ORDER_NO,\n");
		sbSql.append("            TOO.DEALER_ID,\n");
		sbSql.append("            TOO.AREA_ID,\n");
		sbSql.append("            TOO.RAISE_DATE,\n");
		sbSql.append("            TORG.ORG_ID,\n");
		sbSql.append("            VMG.SERIES_NAME,\n");
		sbSql.append("            VMG.MODEL_NAME,\n");
		sbSql.append("            VMG.PACKAGE_NAME,VMG.PACKAGE_CODE,\n");
		sbSql.append("            TSP.PRO_TYPE,\n");
		sbSql.append("            TSP.APPLY_NUM,\n");
		sbSql.append("            TSP.USE_NUM USE_NUM_ALL,\n");
		sbSql.append("            TOO.INVO_DATE,\n");
		sbSql.append("            TOO.INVOICE_NO,\n");
		sbSql.append("            TO_CHAR(TSP.PRO_DATE, 'YYYY-MM-DD') PRO_DATE,\n");
		sbSql.append("            TPOU.STATUS,\n");
		sbSql.append("            TOO.ORDER_YF_PRICE,\n");
		sbSql.append("            (NVL(TOOD.SINGLE_PRICE,0)*SUM(NVL(TOOD.OUT_AMOUNT,0))-NVL(TOO.ORDER_YF_PRICE, 0)) / (NVL(TOOD.SINGLE_PRICE,0)*SUM(NVL(TOOD.OUT_AMOUNT,0)))*100 ZHDIS_RATE_CHECK,\n");
		sbSql.append("            (SUM(NVL(TPOU.USE_AMOUNT, 0))-NVL(TOO.REBATE_PRICE,0)) USE_AMOUNT,\n");
		sbSql.append("            SUM(NVL(TPOU.USE_NUM, 0)) USE_NUM,\n");
		sbSql.append("            TO_CHAR(TPOU.USE_DATE, 'YYYY-MM-DD') USE_DATE\n");
		sbSql.append("       FROM TT_OUT_ORDER           TOO,\n");
		sbSql.append("            TT_OUT_ORDER_DETAIL    TOOD,\n");
		sbSql.append("            VW_MATERIAL_GROUP_MAT  VMG,\n");
		sbSql.append("            TT_PROM_ORD_USE        TPOU,\n");
		sbSql.append("            TT_SALES_PROMISE_DET   TSPD,\n");
		sbSql.append("            TT_SALES_PROMISE       TSP,\n");
		sbSql.append("            TM_DEALER_ORG_RELATION TORGR,\n");
		sbSql.append("            TM_ORG                 TORG\n");
		sbSql.append("      WHERE TOO.ORDER_ID = TOOD.ORDER_ID\n");
		sbSql.append("        AND TOOD.DETAIL_ID = TPOU.ORD_DET_ID\n");
		sbSql.append("        AND TPOU.DETAIL_ID = TSPD.DETAIL_ID\n");
		sbSql.append("        AND TSPD.PRO_ID = TSP.PRO_ID\n");
		sbSql.append("        AND TOOD.MATERIAL_ID = VMG.MATERIAL_ID(+)\n");
		sbSql.append("        AND TOO.DEALER_ID = TORGR.DEALER_ID(+)\n");
		sbSql.append("        AND TORGR.ORG_ID = TORG.ORG_ID(+)\n");
		sbSql.append("        AND TPOU.ORDER_TYPE = " + Constant.ORDER_INVOICE_TYPE_02 + "\n");
		if (!groupCode.equals(""))
		{
			sbSql.append(MaterialGroupManagerDao.getGroupQuerySql("TOO.ORDER_ID", groupCode, Constant.ORDER_INVOICE_TYPE_02.toString()));
		}
		sbSql.append("      GROUP BY TOO.ORDER_ID,\n");
		sbSql.append("               TOO.ORDER_NO,\n");
		sbSql.append("               TOO.DEALER_ID,\n");
		sbSql.append("               TOO.AREA_ID,\n");
		sbSql.append("               TOO.RAISE_DATE,\n");
		sbSql.append("               TORG.ORG_ID,\n");
		sbSql.append("               VMG.SERIES_NAME,\n");
		sbSql.append("               VMG.MODEL_NAME,\n");
		sbSql.append("               VMG.PACKAGE_NAME,VMG.PACKAGE_CODE,\n");
		sbSql.append("               TSP.PRO_TYPE,\n");
		sbSql.append("               TSP.APPLY_NUM,\n");
		sbSql.append("               TSP.USE_NUM,\n");
		sbSql.append("               TOO.INVO_DATE,\n");
		sbSql.append("               TOO.INVOICE_NO,\n");
		sbSql.append("               TSP.PRO_DATE,\n");
		sbSql.append("               TPOU.STATUS,\n");
		sbSql.append("               TPOU.USE_DATE,\n");
		sbSql.append("               TOOD.SINGLE_PRICE,\n");
		sbSql.append("               TOO.ORDER_YF_PRICE,TOO.REBATE_PRICE\n");
		sbSql.append(")\n");
		sbSql.append("SELECT OO.*,TD.DEALER_NAME FROM ORDER_DETAIL OO, TM_DEALER TD WHERE OO.DEALER_ID = TD.DEALER_ID\n");

		sbSql.append(MaterialGroupManagerDao.getPoseIdBusinessSql("OO.AREA_ID", logonUser.getPoseId().toString()));
		sbSql.append(MaterialGroupManagerDao.getOrgDealerLimitSql("OO.DEALER_ID", logonUser));

		if (!payStatus.equals(""))
		{
			sbSql.append("   AND OO.STATUS = " + payStatus + "\n");
		}

		if (!orgCode.equals(""))
		{
			sbSql.append(MaterialGroupManagerDao.getOrgQuerySql("OO.ORG_ID", orgCode));
		}

		if (!dealerCode.equals(""))
		{
			sbSql.append("   AND TD.DEALER_CODE IN (" + getSqlQueryCondition(dealerCode) + ")\n");
		}

		if (!startdate.equals(""))
		{
			sbSql.append("   AND OO.RAISE_DATE >= TO_DATE('" + startdate + "','YYYY-MM-DD HH24:MI:SS')\n");
		}

		if (!endDate.equals(""))
		{
			sbSql.append("   AND OO.RAISE_DATE <= TO_DATE('" + endDate + "','YYYY-MM-DD HH24:MI:SS')\n");
		}

		if (!orderNo.equals(""))
		{
			sbSql.append("   AND OO.ORDER_NO LIKE '%" + orderNo + "%'\n");
		}

		sbSql.append(" ORDER BY OO.RAISE_DATE");

		// sbSql.append("SELECT A.USE_DATE, -- 使用时间\n");
		// sbSql.append("		 E.ORDER_ID, \n");
		// sbSql.append("		 E.ORDER_NO, \n");
		// sbSql.append("       TD.DEALER_SHORTNAME, -- 经销商\n");
		// sbSql.append("       A.USE_AMOUNT, -- 使用金额\n");
		// sbSql.append("       A.USE_NUM, -- 使用数量\n");
		// sbSql.append("       B.GROUP_ID, -- 车系ID\n");
		// sbSql.append("       F.SERIES_NAME, --车系名称\n");
		// sbSql.append("       F.MODEL_NAME, -- 车型名称\n");
		// sbSql.append("       F.PACKAGE_NAME, -- 配置名称\n");
		// sbSql.append("		 F.MATERIAL_NAME,\n");
		// sbSql.append("       D.CHK_PRICE SINGLE_PRICE, -- 折前单价\n");
		// sbSql.append("       D.DISCOUNT_RATE, -- 折扣率\n");
		// sbSql.append("       D.TOTAL_PRICE, -- 折后总价\n");
		// sbSql.append("       D.DISCOUNT_PRICE, -- 折扣总额\n");
		// sbSql.append("       A.ORDER_TYPE,-- 单据类型\n");
		// sbSql.append("		 E.FUND_TYPE_ID,-- 开票类型\n");
		// sbSql.append("       TO_CHAR(E.INVO_DATE,'YYYY-MM-DD HH24:MI:SS') INVO_DATE, -- 开票日期\n");
		// sbSql.append("       E.INVOICE_NO, -- 开票号\n");
		// sbSql.append("		 A.STATUS, -- 还款状态\n");
		// sbSql.append("		 C.PRO_TYPE,\n");
		// sbSql.append("		 C.APPLY_NUM,\n");
		// sbSql.append("		 C.USE_NUM USE_NUM_ALL,\n");
		// sbSql.append("       TO_CHAR(C.PRO_DATE,'YYYY-MM-DD') PRO_DATE -- 承诺还款时间\n");
		// sbSql.append("  FROM TT_PROM_ORD_USE       A,\n");
		// sbSql.append("       TT_SALES_PROMISE_DET  B,\n");
		// sbSql.append("       TT_SALES_PROMISE      C,\n");
		// sbSql.append("       TT_VS_ORDER_DETAIL    D,\n");
		// sbSql.append("       TT_VS_ORDER           E,\n");
		// sbSql.append("       VW_MATERIAL_GROUP_MAT F,\n");
		// sbSql.append("		 TM_DEALER 			   TD,\n");
		// sbSql.append("       TM_ORG                 TORG,\n");
		// sbSql.append("       TM_DEALER_ORG_RELATION TORGR\n");
		// sbSql.append(" WHERE A.DETAIL_ID = B.DETAIL_ID\n");
		// sbSql.append("   AND B.PRO_ID = C.PRO_ID\n");
		// sbSql.append("   AND B.GROUP_ID = F.SERIES_ID\n");
		// sbSql.append("   AND D.MATERIAL_ID = F.MATERIAL_ID");
		// sbSql.append("   AND E.DEALER_ID = TD.DEALER_ID\n");
		// sbSql.append("   AND A.ORD_DET_ID = D.DETAIL_ID\n");
		// sbSql.append("   AND D.ORDER_ID = E.ORDER_ID\n");
		// sbSql.append("	 AND TD.DEALER_ID = TORGR.DEALER_ID\n");
		// sbSql.append("	 AND TORGR.ORG_ID = TORG.ORG_ID");
		// sbSql.append("   AND A.STATUS NOT IN(" +
		// Constant.PROMISE_PAY_STATUS_00 + "," + Constant.PROMISE_PAY_STATUS_01
		// + ")\n");
		// sbSql.append("   AND C.STATUS = " + Constant.PROMISE_STATUS_07 +
		// "\n");
		// sbSql.append("	 AND A.ORDER_TYPE = " + Constant.ORDER_INVOICE_TYPE_01
		// + "\n");
		// //sbSql.append("   AND TO_CHAR(C.PRO_DATE,'YYYY-MM-DD') >= TO_CHAR(SYSDATE,'YYYY-MM-DD')\n");
		// sbSql.append(MaterialGroupManagerDao.getPoseIdBusinessSql("C.YIELDLY",
		// logonUser.getPoseId().toString()));
		// sbSql.append(MaterialGroupManagerDao.getOrgDealerLimitSql("E.DEALER_ID",
		// logonUser));
		//
		// if (!payStatus.equals("")) {
		// sbSql.append("   AND A.STATUS = " + payStatus + "\n");
		// }
		//
		// if (!orgCode.equals(""))
		// {
		// sbSql.append(MaterialGroupManagerDao.getOrgQuerySql("TORG.ORG_ID",
		// orgCode));
		// }
		//
		// if (!dealerCode.equals(""))
		// {
		// sbSql.append("   AND TD.DEALER_CODE IN (" +
		// getSqlQueryCondition(dealerCode) + ")\n");
		// }
		//
		// if (!startdate.equals(""))
		// {
		// sbSql.append("   AND E.RAISE_DATE >= TO_DATE('" + startdate +
		// "','YYYY-MM-DD HH24:MI:SS')\n");
		// }
		//
		// if (!endDate.equals(""))
		// {
		// sbSql.append("   AND E.RAISE_DATE <= TO_DATE('" + endDate +
		// "','YYYY-MM-DD HH24:MI:SS')\n");
		// }
		//
		// if (!orderNo.equals(""))
		// {
		// sbSql.append("       AND E.ORDER_NO LIKE '%" + orderNo + "%'\n");
		// }
		//
		// if (!groupCode.equals(""))
		// {
		// sbSql.append(MaterialGroupManagerDao.getGroupQuerySql("E.ORDER_ID",
		// groupCode,
		// Constant.ORDER_INVOICE_TYPE_01.toString()));
		// }
		// sbSql.append("UNION ALL \n");
		//
		// sbSql.append("SELECT A.USE_DATE, -- 使用时间\n");
		// sbSql.append("		 E.ORDER_ID, \n");
		// sbSql.append("		 E.ORDER_NO, \n");
		// sbSql.append("       TD.DEALER_SHORTNAME, -- 经销商\n");
		// sbSql.append("       A.USE_AMOUNT, -- 使用金额\n");
		// sbSql.append("       A.USE_NUM, -- 使用数量\n");
		// sbSql.append("       B.GROUP_ID, -- 车系ID\n");
		// sbSql.append("       F.SERIES_NAME, --车系名称\n");
		// sbSql.append("       F.MODEL_NAME, -- 车型名称\n");
		// sbSql.append("       F.PACKAGE_NAME, -- 配置名称\n");
		// sbSql.append("		 F.MATERIAL_NAME,\n");
		// sbSql.append("       D.SINGLE_PRICE, -- 折前单价\n");
		// sbSql.append("       D.DISCOUNT_RATE, -- 折扣率\n");
		// sbSql.append("       D.TOTAL_PRICE, -- 折后总价\n");
		// sbSql.append("       D.DISCOUNT_PRICE, -- 折扣总额\n");
		// sbSql.append("       A.ORDER_TYPE,-- 单据类型\n");
		// sbSql.append("		 E.FUND_TYPE_ID,-- 开票类型\n");
		// sbSql.append("       TO_CHAR(E.INVO_DATE,'YYYY-MM-DD HH24:MI:SS') INVO_DATE, -- 开票日期\n");
		// sbSql.append("       E.INVOICE_NO, -- 开票号\n");
		// sbSql.append("		 A.STATUS, -- 还款状态\n");
		// sbSql.append("		 C.PRO_TYPE,\n");
		// sbSql.append("		 C.APPLY_NUM,\n");
		// sbSql.append("		 C.USE_NUM USE_NUM_ALL,\n");
		// sbSql.append("       TO_CHAR(C.PRO_DATE,'YYYY-MM-DD') PRO_DATE -- 承诺还款时间\n");
		// sbSql.append("  FROM TT_PROM_ORD_USE       A,\n");
		// sbSql.append("       TT_SALES_PROMISE_DET  B,\n");
		// sbSql.append("       TT_SALES_PROMISE      C,\n");
		// sbSql.append("       TT_OUT_ORDER_DETAIL    D,\n");
		// sbSql.append("       TT_OUT_ORDER           E,\n");
		// sbSql.append("       VW_MATERIAL_GROUP_MAT F,\n");
		// sbSql.append("		 TM_DEALER 			   TD,\n");
		// sbSql.append("       TM_ORG                 TORG,\n");
		// sbSql.append("       TM_DEALER_ORG_RELATION TORGR\n");
		// sbSql.append(" WHERE A.DETAIL_ID = B.DETAIL_ID\n");
		// sbSql.append("   AND B.PRO_ID = C.PRO_ID\n");
		// sbSql.append("   AND B.GROUP_ID = F.SERIES_ID\n");
		// sbSql.append("   AND D.MATERIAL_ID = F.MATERIAL_ID");
		// sbSql.append("   AND E.DEALER_ID = TD.DEALER_ID\n");
		// sbSql.append("   AND A.ORD_DET_ID = D.DETAIL_ID\n");
		// sbSql.append("   AND D.ORDER_ID = E.ORDER_ID\n");
		// sbSql.append("	 AND TD.DEALER_ID = TORGR.DEALER_ID\n");
		// sbSql.append("	 AND TORGR.ORG_ID = TORG.ORG_ID");
		// sbSql.append("   AND A.STATUS NOT IN(" +
		// Constant.PROMISE_PAY_STATUS_00 + "," + Constant.PROMISE_PAY_STATUS_01
		// + ")\n");
		// sbSql.append("   AND C.STATUS = " + Constant.PROMISE_STATUS_07 +
		// "\n");
		// sbSql.append("	 AND A.ORDER_TYPE = " + Constant.ORDER_INVOICE_TYPE_02
		// + "\n");
		// //sbSql.append("   AND TO_CHAR(C.PRO_DATE,'YYYY-MM-DD') >= TO_CHAR(SYSDATE,'YYYY-MM-DD')\n");
		// sbSql.append(MaterialGroupManagerDao.getPoseIdBusinessSql("C.YIELDLY",
		// logonUser.getPoseId().toString()));
		// sbSql.append(MaterialGroupManagerDao.getOrgDealerLimitSql("E.DEALER_ID",
		// logonUser));
		//
		// if (!payStatus.equals("")) {
		// sbSql.append("   AND A.STATUS = " + payStatus + "\n");
		// }
		//
		// if (!orgCode.equals(""))
		// {
		// sbSql.append(MaterialGroupManagerDao.getOrgQuerySql("TORG.ORG_ID",
		// orgCode));
		// }
		//
		// if (!dealerCode.equals(""))
		// {
		// sbSql.append("   AND TD.DEALER_CODE IN (" +
		// getSqlQueryCondition(dealerCode) + ")\n");
		// }
		//
		// if (!startdate.equals(""))
		// {
		// sbSql.append("   AND E.RAISE_DATE >= TO_DATE('" + startdate +
		// "','YYYY-MM-DD HH24:MI:SS')\n");
		// }
		//
		// if (!endDate.equals(""))
		// {
		// sbSql.append("   AND E.RAISE_DATE <= TO_DATE('" + endDate +
		// "','YYYY-MM-DD HH24:MI:SS')\n");
		// }
		//
		// if (!orderNo.equals(""))
		// {
		// sbSql.append("       AND E.ORDER_NO LIKE '%" + orderNo + "%'\n");
		// }
		//
		// if (!groupCode.equals(""))
		// {
		// sbSql.append(MaterialGroupManagerDao.getGroupQuerySql("E.ORDER_ID",
		// groupCode,
		// Constant.ORDER_INVOICE_TYPE_02.toString()));
		// }

		return dao.pageQuery(sbSql.toString(), null, getFunName(), pageSize, curPage);
	}

	/**
	 * 查询资源的分配情况
	 * 
	 * @param dataMap
	 * @return
	 */
	public PageResult<Map<String, Object>> queryResourceShared(Map<String, Object> dataMap, AclUserBean logonUser, Integer curPage, Integer pageSize) throws Exception {

		StringBuffer sbSql = new StringBuffer();

		sbSql.append("SELECT A5.MATERIAL_CODE,\n");
		sbSql.append("       A5.MATERIAL_NAME,\n");
		sbSql.append("       A1.AMOUNT,\n");
		sbSql.append("       A3.BOARD_NUMBER,\n");
		sbSql.append("       A3.DELIVERY_AMOUNT,\n");
		sbSql.append("       A3.MATCH_AMOUNT,\n");
		sbSql.append("       A3.OUT_AMOUNT,\n");
		sbSql.append("       A4.ORDER_NO,\n");
		sbSql.append("       A4.ORDER_STATUS,\n");
		sbSql.append("       F_GET_TCCODE_DESC(A4.ORDER_STATUS) ORDER_STATUS_DESC,\n");
		sbSql.append("       A6.DEALER_SHORTNAME,\n");
		sbSql.append("		 A7.SERIES_NAME,A7.MODEL_NAME,A7.PACKAGE_NAME,A7.COLOR_NAME\n");
		sbSql.append("  FROM TT_VS_ORDER_RESOURCE_RESERVE A1,\n");
		sbSql.append("       TT_VS_ORDER_DETAIL           A3,\n");
		sbSql.append("       TT_VS_ORDER                  A4,\n");
		sbSql.append("       TM_VHCL_MATERIAL             A5,\n");
		sbSql.append("       TM_DEALER                    A6,\n");
		sbSql.append("       VW_MATERIAL_GROUP_MAT        A7\n");
		sbSql.append(" WHERE A1.ORDER_DETAIL_ID = A3.DETAIL_ID\n");
		sbSql.append("   AND A3.ORDER_ID = A4.ORDER_ID\n");
		sbSql.append("   AND A1.MATERIAL_ID = A5.MATERIAL_ID\n");
		sbSql.append("   AND A4.DEALER_ID = A6.DEALER_ID\n");
		sbSql.append("   AND A1.MATERIAL_ID = A7.MATERIAL_ID\n");

		String dealerCode = CommonUtils.checkNull(dataMap.get("dealerCode"));
		//String orgCode = CommonUtils.checkNull(dataMap.get("orgCode"));
		String groupCode = CommonUtils.checkNull(dataMap.get("groupCode"));

		if (!dealerCode.equals(""))
		{
			sbSql.append("AND A6.DEALER_CODE IN (" + getSqlQueryCondition(dealerCode) + ")\n");
		}

//		if (!orgCode.equals(""))
//		{
//			sbSql.append("AND A2.ORG_ID = " + orgCode + "\n");
//		}

		if (!groupCode.equals(""))
		{
			sbSql.append("AND A7.SERIES_ID = " + groupCode + "\n");
		}

		sbSql.append(MaterialGroupManagerDao.getOrgDealerLimitSql("A6.DEALER_ID", logonUser));

		return dao.pageQuery(sbSql.toString(), null, getFunName(), pageSize, curPage);
	}

	/**
	 * 查询资源的分配情况
	 * 
	 * @param dataMap
	 * @return
	 */
	public List<Map<String, Object>> queryResourceShared(String orderId) throws Exception {

		StringBuffer sbSql = new StringBuffer();

		sbSql.append("WITH V_STOCK AS (");
		sbSql.append("SELECT SUM(1) SYS_NUM, A.PROV_ID AS ORG_ID, A.MATERIAL_ID\n");
		sbSql.append("  FROM TM_VEHICLE A, TM_ORG B, TM_VHCL_MATERIAL C\n");
		sbSql.append(" WHERE A.PROV_ID = B.ORG_ID(+)\n");
		sbSql.append("   AND A.MATERIAL_ID = C.MATERIAL_ID\n");
		sbSql.append("   AND A.LIFE_CYCLE = 10321002\n");
		sbSql.append("   AND A.LOCK_STATUS = 10241001\n");
		sbSql.append(" GROUP BY A.MATERIAL_ID, A.PROV_ID\n");
		sbSql.append(" ORDER BY A.PROV_ID, A.MATERIAL_ID");
		sbSql.append(")");
		sbSql.append("SELECT D.MATERIAL_NAME,\n");
		sbSql.append("       D.MATERIAL_CODE,\n");
		sbSql.append("       DECODE(C.ORG_ID, -2222222222, '销售部', E.ORG_NAME) ORG_NAME,\n");
		sbSql.append("       A.ORDER_AMOUNT,\n");
		sbSql.append("       NVL(F.SYS_NUM,0) STOCK,\n");
		sbSql.append("       C.AMOUNT\n");
		sbSql.append("  FROM TT_VS_ORDER_DETAIL           A,\n");
		sbSql.append("       TT_VS_ORDER                  B,\n");
		sbSql.append("       TT_VS_ORDER_RESOURCE_RESERVE C,\n");
		sbSql.append("       VW_MATERIAL_GROUP_MAT        D,\n");
		sbSql.append("       TM_ORG                       E,\n");
		sbSql.append("		 V_STOCK					  F\n");
		sbSql.append(" WHERE A.ORDER_ID = B.ORDER_ID\n");
		sbSql.append("   AND A.DETAIL_ID = C.ORDER_DETAIL_ID(+)\n");
		sbSql.append("   AND C.ORG_ID = E.ORG_ID(+)\n");
		sbSql.append("   AND A.MATERIAL_ID = D.MATERIAL_ID\n");
		sbSql.append("   AND C.MATERIAL_ID = F.MATERIAL_ID(+)\n");
		sbSql.append("   AND C.ORG_ID = F.ORG_ID(+)\n");
		sbSql.append("   AND B.ORDER_ID = ?");

		List<Object> params = new ArrayList<Object>();
		params.add(orderId);

		return dao.pageQuery(sbSql.toString(), params, getFunName());
	}

	/**
	 * 查询资源的分配情况
	 * 
	 * @param dataMap
	 * @return
	 */
	public String queryResourceSharedPlan(String orderId) throws Exception {

		List<Object> params = new ArrayList<Object>();
		StringBuffer sbSql = new StringBuffer();
		StringBuffer htmlStr = new StringBuffer();

		sbSql.append("SELECT B.PACKAGE_NAME,\n");
		sbSql.append("       B.PACKAGE_CODE,\n");
		sbSql.append("       B.COLOR_CODE,\n");
		sbSql.append("       B.COLOR_NAME,\n");
		sbSql.append("       B.MATERIAL_CODE,\n");
		sbSql.append("       A.ORDER_AMOUNT ORDER_NUM,\n");
		sbSql.append("       NVL(C.AMOUNT, 0) CHECK_NUM\n");
		sbSql.append("  FROM TT_VS_ORDER_DETAIL           A,\n");
		sbSql.append("       VW_MATERIAL_GROUP_MAT        B,\n");
		sbSql.append("       TT_VS_ORDER_RESOURCE_RESERVE C\n");
		sbSql.append(" WHERE A.MATERIAL_ID = B.MATERIAL_ID\n");
		sbSql.append("   AND A.DETAIL_ID = C.ORDER_DETAIL_ID(+)\n");
		sbSql.append("   AND A.ORDER_ID = ?");

		params.add(orderId);
		List<Map<String, Object>> materialList = dao.pageQuery(sbSql.toString(), params, getFunName());

		htmlStr.append("<table width=\"100%\" class=\"table_list\">");

		htmlStr.append("<tr><th>配置名称</th><th>颜色</th><th>物料编码</th><th>申请数量</th><th>已分配数量</th></tr>");

		for (Map<String, Object> map : materialList)
		{
			htmlStr.append("<tr class='table_list_row2'>");
			htmlStr.append("<td>" + map.get("PACKAGE_NAME") + "</td>");
			htmlStr.append("<td>" + map.get("COLOR_NAME") + "</td>");
			htmlStr.append("<td>" + map.get("MATERIAL_CODE") + "</td>");
			htmlStr.append("<td>" + map.get("ORDER_NUM") + "</td>");
			htmlStr.append("<td>" + map.get("CHECK_NUM") + "</td>");
			htmlStr.append("</tr>");

			htmlStr.append("</tr>");
		}
		htmlStr.append("</table>");

		return htmlStr.toString();
	}

	/**
	 * 中转出库车辆明细跟踪查询
	 * 
	 * @param infoMap
	 * @param curPage
	 * @param pageSize
	 * @return
	 *
	 * @author wangsongwei
	 */
	public PageResult<Map<String, Object>> getZhongZhuanVechicleList(Map<String, Object> map, Integer curPage,
					Integer pageSize) throws Exception
	{
		List<Object> params = new ArrayList<Object>();
		
		String orderNo = CommonUtils.checkNull(map.get("orderNo")); // 订单编号
		String orgCode = CommonUtils.checkNull(map.get("orgCode"));
		String dealerCode = CommonUtils.checkNull(map.get("dealerCode")); // 经销商代码
		String startdate = CommonUtils.checkNull(map.get("startdate")); // 提报日期
		String endDate = CommonUtils.checkNull(map.get("endDate")); // 提报日期
		String groupCode = CommonUtils.checkNull(map.get("groupCode")); // 物料组编号
		String vin = CommonUtils.checkNull(map.get("vin")); // 订单状态
		String vstatus = CommonUtils.checkNull(map.get("vstatus")); // 不需要查询的订单状态
		String paystatus = CommonUtils.checkNull(map.get("paystatus"));

		AclUserBean logonUser = (AclUserBean) map.get("logonUser");
		
		StringBuffer sbSql = new StringBuffer();
		
		sbSql.append("SELECT E.VEHICLE_ID,\n");
		sbSql.append("       E.VIN,\n");
		sbSql.append("       E.LOCK_STATUS,\n");
		sbSql.append("       DECODE(E.LIFE_CYCLE,'10321005','已发运','10321003','已验收','10321008','已出库','10321004','已实销') LIFE_CYCLE,\n");
		sbSql.append("       DECODE(E.OUT_STATUS,'97151001','未还款','971510012','未还款','97151003','已还款') OUT_STATUS,\n");
		sbSql.append("       A.ORDER_NO,\n");
		sbSql.append("		 TO_CHAR(A.RAISE_DATE,'YYYY-MM-DD HH24:MI:SS') RAISE_DATE,");
		sbSql.append("       A.ORDER_ID,\n");
		sbSql.append("       F.DEALER_NAME\n");
		sbSql.append("  FROM TT_VS_ORDER        A,\n");
		sbSql.append("       TT_VS_ORDER_DETAIL B,\n");
		sbSql.append("       TT_SALES_BO_DETAIL C,\n");
		sbSql.append("       TT_SALES_ALLOCA_DE D,\n");
		sbSql.append("       TM_VEHICLE         E,\n");
		sbSql.append("       TM_DEALER          F,\n");
		sbSql.append("       TM_ORG             G,\n");
		sbSql.append("       TM_DEALER_ORG_RELATION  H,\n");
		sbSql.append("       VW_MATERIAL_GROUP_MAT  VMT\n");
		sbSql.append(" WHERE A.ORDER_ID = B.ORDER_ID\n");
		sbSql.append("   AND B.DETAIL_ID = C.OR_DE_ID\n");
		sbSql.append("   AND C.BO_DE_ID = D.BO_DE_ID\n");
		sbSql.append("   AND D.VEHICLE_ID = E.VEHICLE_ID\n");
		sbSql.append("   AND F.DEALER_ID = A.DEALER_ID\n");
		sbSql.append("   AND F.DEALER_ID = H.DEALER_ID\n");
		sbSql.append("   AND B.MATERIAL_ID = VMT.MATERIAL_ID\n");
		sbSql.append("   AND H.ORG_ID = G.ORG_ID\n");
		sbSql.append("   AND A.ORDER_TYPE = 10201004\n");
		sbSql.append("   AND E.LIFE_CYCLE IN (10321003, 10321004, 10321005, 10321008)\n");
		sbSql.append("   AND A.DEALER_ID = F.DEALER_ID\n"); 
		sbSql.append(MaterialGroupManagerDao.getPoseIdBusinessSql("A.AREA_ID", logonUser.getPoseId().toString())); // 产地查看权限限制
		sbSql.append(MaterialGroupManagerDao.getOrgDealerLimitSql("A.DEALER_ID", logonUser));// 组织机构限制
		
		if(!orderNo.equals("")) {
			sbSql.append(" AND A.ORDER_NO = ?");
			params.add(orderNo);
		}
		
		if(!orgCode.equals("")) {
			sbSql.append(" AND G.ORG_CODE = ?");
			params.add(orgCode);
		}
		
		if(!dealerCode.equals("")) {
			sbSql.append(" AND F.DEALER_CODE = ?");
			params.add(dealerCode);
		}
		
		if(!startdate.equals("")) {
			sbSql.append(" AND TRUNC(A.RAISE_DATE) >= TO_DATE(?,'YYYY-MM-DD')");
			params.add(startdate);
		}
		
		if(!endDate.equals("")) {
			sbSql.append(" AND TRUNC(A.RAISE_DATE) <= TO_DATE(?,'YYYY-MM-DD')");
			params.add(endDate);
		}
		
		if(!vin.equals("")) {
			sbSql.append(" AND E.VIN =?");
			params.add(vin);
		}
		
		if(!vstatus.equals("")) {
			sbSql.append(" AND E.LIFE_CYCLE = ?");
			params.add(vstatus);
		}
		
		if(!paystatus.equals("")) {
			sbSql.append(" AND E.OUT_STATUS = ?");
			params.add(paystatus);
		}
		
		if(!groupCode.equals("")) {
			sbSql.append(" AND VMT.SERIES_CODE = ?");
			params.add(groupCode);
		}
		
		return dao.pageQuery(sbSql.toString(), params, getFunName(), pageSize, curPage);
	}
	/**
	 * 获取省份调价
	 * 
	 * @param cityId 城市ID
	 * @return
	 */
	public String getProvAmout(String cityId) throws Exception {

		List<Object> params = new ArrayList<Object>();
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("select nvl(t.amount, 0) amount\r\n");
		sbSql.append("  from TT_SALES_AREA_PRICE t\r\n");
		sbSql.append(" where t.area_id = ?\r\n");
		sbSql.append("   and t.status = 10011001\r\n");
		sbSql.append("   and t.yieldly = 2010010100000001"); 
		params.add(cityId);
		Map<String, Object> provAmoutMap = dao.pageQueryMap(sbSql.toString(), params, getFunName());
		if(provAmoutMap!=null){
			return provAmoutMap.get("amount").toString();
		}

		return "0";
	}
	/**
	 * 返利使用(查询)
	 * 
	 * @param 可使用多少返利(经销商,资金大小)
	 * @return
	 */
	public String getFLAmout(String dealerId,String price) throws Exception {
		List<Object> params = new ArrayList<Object>();
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("--大于0使用\r\n");
		sbSql.append("select  sum(nvl(t.reb_amount,0)) reb_amount_sum\r\n");
		sbSql.append("  from TT_SALES_REBATE t\r\n");
		sbSql.append(" WHERE T.DEALER_ID = ?"); 
		params.add(dealerId);
		Map<String, Object> provAmoutMap = dao.pageQueryMap(sbSql.toString(), params, getFunName());
		if(provAmoutMap!=null){
			if(Double.parseDouble(provAmoutMap.get("REB_AMOUNT_SUM").toString())>0){
				//获取返利系数（0.1现在默认是返利系数）
				if(Double.parseDouble(provAmoutMap.get("REB_AMOUNT_SUM").toString())>=Double.parseDouble(price)*0.01){
					//以后根据返利系数表查询该金额可以使用多少返利
					//返利预扣款=销售清单订单金额*可使用的返利系数
					return String.valueOf(Double.parseDouble(price)*0.01);
				}else{
					return "0";
				}
			}else{
				return "0";
			}
		}
		return "0";
		/*List<Object> params1= new ArrayList<Object>();
		sbSql.append("--负前正后\r\n");
		sbSql.append("select last_amount\r\n");
		sbSql.append("  from (select round(t.total_amount) - round(t.used_amount) last_amount\r\n");
		sbSql.append("          from TT_SALES_REBATE t\r\n");
		sbSql.append("         WHERE T.DEALER_ID = ?) s\r\n");
		sbSql.append(" where s.last_amount != 0\r\n");
		sbSql.append(" order by s.last_amount asc"); 
		params1.add(dealerId);
		//如果返利预扣款低于单笔返利，使用一部分，如果超出，则继续使用下一笔，规则类推；负返利不使用。
		List<Object> params2= new ArrayList<Object>();
		sbSql.append("--负前正后\r\n");
		sbSql.append("select last_amount\r\n");
		sbSql.append("  from (select round(t.total_amount) - round(t.used_amount) last_amount\r\n");
		sbSql.append("          from TT_SALES_REBATE t\r\n");
		sbSql.append("         WHERE T.DEALER_ID = 2013082010000535) s\r\n");
		sbSql.append(" where s.last_amount != 0\r\n");
		sbSql.append(" order by s.last_amount asc"); 
		params2.add(dealerId);*/
	}
	/**
	 * 返利使用更新
	 * 
	 * @param 可使用多少返利(经销商,资金大小)
	 * @return
	 */
	public String updateFLAmout(String dealerId,String flAmount,String orderId,String userId) throws Exception {
		StringBuffer sbSql = new StringBuffer();
		List<Object> params1= new ArrayList<Object>();
		sbSql.append("--负前正后\r\n");
		sbSql.append("select t.dealer_id, t.reb_id, nvl(t.reb_amount,0) reb_amount\r\n");
		sbSql.append("  from TT_SALES_REBATE t\r\n");
		sbSql.append(" WHERE T.DEALER_ID = ?\r\n");
		sbSql.append("   and t.reb_amount != 0\r\n");
		sbSql.append("   and t.status = 99301003\r\n");
		sbSql.append(" order by t.reb_amount, t.audit_date asc"); 
		params1.add(dealerId);
		//如果返利预扣款低于单笔返利，使用一部分，如果超出，则继续使用下一笔，规则类推；负返利不使用。
		List<Map<String, Object>> list = dao.pageQuery(sbSql.toString(), params1, getFunName());
		double ff=Double.parseDouble(flAmount);
		if(list!=null){
			for(int i=0;i<list.size();i++){
				Map<String, Object> map=list.get(i);
				if(Double.parseDouble(map.get("REB_AMOUNT").toString())<ff){//小于返利时只用一部分
					//回写返利主表表
					rebackAmout(map.get("REB_ID").toString(),ff,orderId,userId);
					break;
				}else if(Double.parseDouble(map.get("REB_AMOUNT").toString())==ff){//等于所有用完
					//回写返利主表表
					rebackAmout(map.get("REB_ID").toString(),Double.parseDouble(map.get("REB_AMOUNT").toString()),orderId,userId);
					//添加返利使用表一条数据
					break;
				}else{//大于继续使用下一笔
					//回写返利主表表
					rebackAmout(map.get("REB_ID").toString(),Double.parseDouble(map.get("REB_AMOUNT").toString()),orderId,userId);
					//添加返利使用表一条数据
					ff=ff-Double.parseDouble(map.get("REB_AMOUNT").toString());
				}
			}
		}
		return "0";
	}
	/**
	 * 回写数据
	 * 
	 * @param (经销商,资金大小)
	 * @return
	 */
	public void rebackAmout(String rebId,double rebAmout,String orderId,String userId) throws Exception {
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("insert into TT_SALES_ORDER_REB\r\n");
		sbSql.append("  (detail_id,\r\n");
		sbSql.append("   Reb_Id,\r\n");
		sbSql.append("   Order_Id,\r\n");
		sbSql.append("   Use_Amount,\r\n");
		sbSql.append("   Use_Date,\r\n");
		sbSql.append("   Use_Per,\r\n");
		sbSql.append("   Create_By,\r\n");
		sbSql.append("   Create_Date,\r\n");
		sbSql.append("   Status)\r\n");
		sbSql.append("values\r\n");
		sbSql.append("  (f_getid, "+rebId+", "+orderId+", "+rebAmout+", sysdate, "+userId+", "+userId+", sysdate, 10011001)"); 
		dao.insert(sbSql.toString());
		sbSql.delete(0, sbSql.length());
		List<Object> params1= new ArrayList<Object>();
		sbSql.append("--负前正后\r\n");
		sbSql.append("update TT_SALES_REBATE s\r\n");
		sbSql.append("   set s.reb_amount = s.total_amount - ?, s.used_amount = ?\r\n");
		sbSql.append(" where s.reb_id = ?"); 
		params1.add(rebAmout);
		params1.add(rebAmout);
		params1.add(rebId);
		dao.update(sbSql.toString(), params1);
		

	}
	
	/**
	 * 查询返利使用明细
	 * @author liufazhong
	 */
	public List<Map<String, Object>> queryOrderRebByOrderId(String vsOrderId){
		StringBuffer sql = new StringBuffer("SELECT a.*,to_char(a.use_date,'yyyy-mm-dd') use_time,b.order_no s_order_no,c.order_no" +
				" FROM tt_sales_order_reb a LEFT JOIN tt_vs_order b ON a.order_id=b.order_id" +
				" LEFT JOIN tt_sa_order c ON b.wr_order_id=c.order_id WHERE 1=1");
		List<Object> params = new ArrayList<Object>();
		if (!XHBUtil.IsNull(vsOrderId)) {
			sql.append(" and a.order_id = ?");
			params.add(vsOrderId);
		}
		return pageQuery(sql.toString(), params, getFunName());
	}
	/**
	 * 方法描述 ： 获取经销商的账户信息<br/>
	 * 
	 * @param dealerId
	 *            经销商ID
	 * @param yieldlyId
	 *            产地ID
	 * @param finType
	 *            资金账户类型ID
	 * @return TT_SALES_FIN_ACC
	 * @author wangsongwei
	 */
	public Map<String, Object> getDealerAccountInfo(String dealerId, String yieldlyId, String finType, String bookId) throws Exception
	{
		List<Object> params = new ArrayList<Object>();
		StringBuffer sbSql = new StringBuffer();
		
//		if(finType.equals(Constant.ACCOUNT_TYPE_03.toString()))
//		{
//			sbSql.append("SELECT TSOA.VER,\n");
//			sbSql.append("       TSOA.AMOUNT - TSOA.FREEZE_AMOUNT AMOUNT,\n");
//			sbSql.append("       TSOA.CRE_DETAIL_ID AS ACC_ID,\n");
//			sbSql.append("       TSOA.FREEZE_AMOUNT\n");
//			sbSql.append("  FROM TT_SALES_OTHER_ACCEPT TSOA\n");
//			sbSql.append(" WHERE TSOA.STATUS = 16011003\n");
//			sbSql.append("   AND TSOA.CRE_DETAIL_ID = ?\n");
//			sbSql.append("   AND "+ Constant.ACCOUNT_TYPE_03 +" = ?"); 
//			params.add(bookId);
//			params.add(finType);
//		}
//		else
//		{
			sbSql.append("SELECT NVL(ACC.VER, 0) VER,\n");
			sbSql.append(" 	  CASE WHEN ACC.FIN_TYPE=10251001 THEN \n");
			sbSql.append("       	    NVL(ACC.AMOUNT, 0) + \n");
			sbSql.append("              NVL((SELECT CREDIT_AMOUNT\n");
			sbSql.append("                 FROM TT_SALES_DEALER_CREDIT_LIMIT\n");
			sbSql.append("                WHERE DEALER_ID = ?\n");
			sbSql.append("                  AND STATUS = 10011001\n");
			sbSql.append("                  AND TRUNC(TERMINATION_DATE) >= TRUNC(SYSDATE)\n");
			sbSql.append("                  AND TRUNC(EFFECT_DATE) <= TRUNC(SYSDATE)),0) \n");
			sbSql.append("	  WHEN  ACC.FIN_TYPE=10251014 THEN \n");
			sbSql.append(" 		      NVL(ACC.AMOUNT, 0) + \n");
			sbSql.append(" 			  NVL((SELECT CREDIT_AMOUNT FROM TT_DEALER_VEHICLE_MODEL_CREDIT \n");
			sbSql.append("            WHERE DEALER_ID = ?  AND STATUS = 10041001 AND AUDIT_STATUS=12881003 \n");
			sbSql.append("                  AND TRUNC(END_DATE) >= TRUNC(SYSDATE) AND TRUNC(START_DATE) <= TRUNC(SYSDATE)),0)\n");
			sbSql.append(" 	  ELSE \n");
			sbSql.append(" 	       ACC.AMOUNT\n");
			sbSql.append(" 	  END AS AMOUNT, \n");
			sbSql.append("       ACC.ACC_ID,\n");
			sbSql.append("       NVL(ACC.FREEZE_AMOUNT, 0) FREEZE_AMOUNT\n");
			sbSql.append("  FROM TT_SALES_FIN_ACC ACC\n");
			sbSql.append(" WHERE ACC.DEALER_ID = ?\n");
			sbSql.append("   AND ACC.YIELDLY = ?\n");
			sbSql.append("   AND ACC.FIN_TYPE = ?\n");
			params.add(dealerId);
			params.add(dealerId);
			params.add(dealerId);
			params.add(yieldlyId);
			params.add(finType);
//		}
		
		return dao.pageQueryMap(sbSql.toString(), params, getFunName());
	}
	
	/**
	 * 获取经销商指定配置的价格
	 * 
	 * @param dealerId
	 * @param yieldly
	 * @param string
	 */
	public Map<String, Object> getPackagePrice(String dealerId, String yieldly, String packageId) {
		List<Object> params = new ArrayList<Object>();
		StringBuffer sbSql = new StringBuffer();
		
		sbSql.append("SELECT T.LAST_SETTLE_AMOUNT, T.SETTLE_AMOUNT SINGLE_PRICE,\n");
		sbSql.append("       NVL((SELECT DIS_AMOUNT\n");
		sbSql.append("             FROM TT_SALES_DEALER_DISRATE\n");
		sbSql.append("            WHERE DEALER_ID = ?),\n");
		sbSql.append("           0) DEALER_PRICE,\n");
		sbSql.append("       T.LAST_SETTLE_AMOUNT - NVL((SELECT DIS_AMOUNT\n");
		sbSql.append("                                    FROM TT_SALES_DEALER_DISRATE\n");
		sbSql.append("                                   WHERE DEALER_ID = ?),\n");
		sbSql.append("                                  0) DIS_SINGLE_PRICE\n");
		sbSql.append("  FROM TT_SALES_MODLE_DISRATE_DEALER T\n");
		sbSql.append(" WHERE T.STATUS = 10011001\n");
		sbSql.append("   AND T.AUDIT_STATUS = 99301003\n");
		sbSql.append("   AND T.PACKAGE_ID = ?\n");
		sbSql.append("   AND T.DEALER_ID  = ?"); 
 
		params.add(dealerId);
		params.add(dealerId);
		params.add(packageId);
		params.add(dealerId);
		return dao.pageQueryMap(sbSql.toString(), params, getFunName());
	}
}
