package com.infodms.dms.dao.common;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.ResultSet;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import com.infodms.dms.common.Constant;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.sales.ordermanage.carSubmission.CarSubmissionDao;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.vehicle.AsSqlUtils;
import com.infoservice.po3.bean.PO;

/**
 * <ul>
 * <li>文件名称: AjaxSelectDao.java</li>
 * <li>文件描述:</li>
 * <li>版权所有: 版权所有(C)2012-2013</li>
 * <li>内容摘要:</li>
 * <li>完成日期: 2013-4-27 上午10:46:37</li>
 * <li>修改记录:</li>
 * </ul>
 * 
 * @version 1.0
 * @author wangsongwei
 */
public class AjaxSelectDao extends BaseDao {
	
	private static final AjaxSelectDao dao = new AjaxSelectDao();
	
	public static final AjaxSelectDao getInstance()
	{
		return dao;
	}
	
	/**
	 * 方法描述 :　计算经销商的某个产地所有资金类型总和
	 * 
	 * @param string
	 * @param yieldId
	 * @return
	 * @author wangsongwei
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> countFinAccount(String dealerId, String yieldId) throws Exception
	{
		Map<String, Object> map = null;
		
		try
		{
			StringBuffer sbSql = new StringBuffer();
			
			sbSql.append("\n -- 资金账户总额查询                        \n");
			sbSql.append("SELECT                                        \n");
			sbSql.append("    SUM(NVL(ACC.AMOUNT,0)) ALL_MONEY\n");
			sbSql.append("FROM                                           \n");
			sbSql.append("    TT_SALES_FIN_ACC ACC                       \n");
			sbSql.append("WHERE                                          \n");
			sbSql.append("    ACC.DEALER_ID = " + dealerId + "           \n");
			sbSql.append("    AND                                        \n");
			sbSql.append("    ACC.YIELDLY =  " + yieldId);
			
			// sql.append(" select nvl(TEMP1.amount,0) amount  ,nvl(TEMP2.sumamount,0) sumamount from\n");
			// sql.append(" (select  sum(t.amount-t.freeze_amount) amount from tt_sales_fin_acc t \n");
			// sql.append(" where t.dealer_id ='" + dealerId +
			// "' and t.yieldly='" + yieldId + "' and t.fin_type='" + funType
			// + "')TEMP1,\n");
			// sql.append(" (select  sum(t.amount-t.freeze_amount)  sumamount  from tt_sales_fin_acc t\n");
			// sql.append("  where t.dealer_id ='" + dealerId +
			// "' and t.yieldly='" + yieldId + "' and t.fin_type like '"
			// + Constant.ACCOUNT_TYPE + "%')TEMP2\n");
			
			map = dao.pageQueryMap(sbSql.toString(), null, getFunName());
		}
		catch (Exception ex)
		{
			throw new RuntimeException("查询经销商" + dealerId + " 对应产地( " + yieldId + ")的账户总额失败!");
		}
		
		return map;
	}
	
	/**
	 * 方法描述 :　获取经销商具体某个产地的某个账户的资金信息
	 * 
	 * @param string
	 * @param yieldId
	 * @param finType
	 * @return
	 * @author wangsongwei
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getAccCountInfo(String dealerId, String yieldId, String finType)
	{
		Map<String, Object> map = null;
		
		try
		{
			StringBuffer sbSql = new StringBuffer();
			
			sbSql.append("\n -- 资金账户总额查询                                                                \n");
			sbSql.append("SELECT DECODE(ACC.FIN_TYPE,\n");
			sbSql.append("              10251001,\n");
			sbSql.append("              NVL(ACC.AMOUNT, 0) +\n");
			sbSql.append("              NVL((SELECT CREDIT_AMOUNT\n");
			sbSql.append("                 FROM TT_SALES_DEALER_CREDIT_LIMIT\n");
			sbSql.append("                WHERE DEALER_ID = ?\n");
			sbSql.append("                  AND STATUS = 10011001\n");
			sbSql.append("                  AND TRUNC(TERMINATION_DATE) >= TRUNC(SYSDATE)\n");
			sbSql.append("                  AND TRUNC(EFFECT_DATE) <= TRUNC(SYSDATE)),0),\n");
			sbSql.append("              NVL(ACC.AMOUNT, 0)) REMAIN, -- 可用余额    \n");
			sbSql.append("       NVL(ACC.AMOUNT, 0) + NVL(ACC.FREEZE_AMOUNT, 0) AMOUNT,\n");
			sbSql.append("       NVL(ACC.VER, 0) VER\n");
			sbSql.append("  FROM TT_SALES_FIN_ACC ACC\n");
			sbSql.append("WHERE                                          \n");
			sbSql.append("    ACC.DEALER_ID = ?           \n");
			sbSql.append("    AND ACC.YIELDLY =  ?		     \n");
			sbSql.append("    AND ACC.FIN_TYPE =  ?		     \n");
			
			List<Object> params = new ArrayList<Object>();
			
			params.add(dealerId);
			params.add(dealerId);
			params.add(yieldId);
			params.add(finType);
			
			map = dao.pageQueryMap(sbSql.toString(), params, getFunName());
		}
		catch (Exception ex)
		{
			throw new RuntimeException("查询经销商" + dealerId + " 对应产地( " + yieldId + ")的账户总额失败!");
		}
		
		return map;
	}
	

	/**
	 * 方法描述 :　获取经销商具体某个产地的某个账户的资金信息--H2E专款账户类型
	 * 
	 * @param string
	 * @param yieldId
	 * @param finType
	 * @return
	 * @author JIANGRP
	 */
	public Map<String, Object> getAccCountInfoH2E(String dealerId, String yieldId, String finType) {
		Map<String, Object> map = null;
		try {
			StringBuffer sbSql = new StringBuffer();
			
			sbSql.append("\n -- 资金账户总额查询                                                                \n");
			sbSql.append("SELECT NVL(ACC.AMOUNT, 0) + \n");
			sbSql.append(" 			NVL((SELECT CREDIT_AMOUNT FROM TT_DEALER_VEHICLE_MODEL_CREDIT \n");
			sbSql.append("			    WHERE DEALER_ID = ? \n");
			sbSql.append("                  AND STATUS = 10041001 AND AUDIT_STATUS=12881003 \n");
			sbSql.append("                  AND TRUNC(END_DATE) >= TRUNC(SYSDATE)\n");
			sbSql.append("                  AND TRUNC(START_DATE) <= TRUNC(SYSDATE)),0) REMAIN, -- 可用余额\n");
			sbSql.append("       NVL(ACC.AMOUNT, 0) + NVL(ACC.FREEZE_AMOUNT, 0) AMOUNT,\n");
			sbSql.append("       NVL(ACC.VER, 0) VER\n");
			sbSql.append("  FROM TT_SALES_FIN_ACC ACC\n");
			sbSql.append("WHERE                                          \n");
			sbSql.append("    ACC.DEALER_ID = ?           \n");
			sbSql.append("    AND ACC.YIELDLY =  ?		     \n");
			sbSql.append("    AND ACC.FIN_TYPE =  ?		     \n");
			
			List<Object> params = new ArrayList<Object>();
			
			params.add(dealerId);
			params.add(dealerId);
			params.add(yieldId);
			params.add(finType);
			
			map = dao.pageQueryMap(sbSql.toString(), params, getFunName());
		} catch (Exception ex) {
			throw new RuntimeException("查询经销商" + dealerId + " 对应产地( " + yieldId + ")的账户总额失败!");
		}
		
		return map;
	}
	
	/**
	 * 方法描述 : 获取经销商地址的详细信息
	 * 
	 * @param addressId
	 * @return
	 * @author wangsongwei
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getAddressInfo(String addressId) throws Exception
	{
		Map<String, Object> map = null;
		
		try
		{
			StringBuffer sql = new StringBuffer();
			
			sql.append("\n -- 地址详细信息查询 \n");
			sql.append("SELECT TA.ID, TA.DEALER_ID, TA.ADDRESS, TA.LINK_MAN, TA.TEL, TA.RECEIVE_ORG FROM TM_VS_ADDRESS TA WHERE 1=1 ");
			sql.append("AND TA.STATUS='" + Constant.STATUS_ENABLE + "'");
			if (!addressId.equals(""))
			{
				sql.append(" AND TA.ID = " + addressId);
			}
			else
			{
				sql.append(" AND TA.ID IS NULL");
			}
			
			map = pageQueryMap(sql.toString(), null, getFunName());
			
			if (map == null)
			{
				map = new HashMap<String, Object>();
				map.put("ADDRESS", "");
				map.put("LINK_MAN", "");
				map.put("TEL", "");
				map.put("RECEIVE_ORG", "");
			}
		}
		catch (Exception ex)
		{
			throw ex;
		}
		
		return map;
	}
	
	/**
	 * 方法描述 : 查询经销商的地址列表 <br/>
	 * 
	 * @param dealerId
	 *            经销商ID
	 * @return
	 * @throws Exception
	 * @author wangsongwei
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getDealerAddressList(String dealerId) throws Exception
	{
		List<Map<String, Object>> list = null;
		
		try
		{
			StringBuffer sql = new StringBuffer("\n");
			
			sql.append("\n-- 经销商地址查询  \n");
			sql.append("select  tva.id, tva.address, tva.city_id 	\n");
			sql.append("from tm_vs_address tva					\n");
			sql.append(" where 1 = 1							\n");
			if (dealerId != null && !"".equals(dealerId))
			{
				sql.append("   and tva.dealer_id =" + dealerId + "	\n");
			}
			sql.append("   and tva.status =" + Constant.STATUS_ENABLE + "\n");
			
			list = pageQuery(sql.toString(), null, getFunName());
		}
		catch (Exception ex)
		{
			throw ex;
		}
		
		return list;
	}
	
	/**
	 * 方法描述 : 查询下级经销商列表
	 * 
	 * @param dealerId
	 *            - 本级经销商Id
	 * @param dealerIds
	 *            - 上级经销商Id
	 * @param dealerLevel
	 *            - 需查询经销商级别
	 * @param dealerType
	 *            - 经销商类型
	 * @return
	 * @author wangsongwei
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getDelearList(String dealerId) throws Exception
	{
		List<Map<String, Object>> list = null;
		
		try
		{
			StringBuffer sql = new StringBuffer();
			
			sql.append("\n -- 查询下级经销商列表 \n");
			sql.append("select '①'||TD.DEALER_SHORTNAME DEALER_NAME,\n");
			sql.append("TD.DEALER_CODE,\n");
			sql.append("TD.DEALER_ID\n");
			sql.append("from tm_dealer TD where 1=1 \n");
			sql.append("AND TD.DEALER_ID=" + dealerId + "\n AND DEALER_LEVEL=" + Constant.DEALER_LEVEL_01 + "\n");
			sql.append("AND TD.DEALER_TYPE=" + Constant.DEALER_TYPE_DVS + "\n");
			sql.append("union ALL\n");
			sql.append(" SELECT  '②'||TD.DEALER_SHORTNAME DEALER_NAME , \n");
			sql.append("		TD.DEALER_CODE, \n");
			sql.append("		TD.DEALER_ID \n");
			sql.append("FROM	TM_DEALER TD \n");
			sql.append("WHERE	TD.STATUS = " + Constant.STATUS_ENABLE + " \n");
			sql.append("AND PARENT_DEALER_D=" + dealerId + " AND DEALER_LEVEL=" + Constant.DEALER_LEVEL_02 + "\n");
			sql.append("AND TD.DEALER_TYPE=" + Constant.DEALER_TYPE_DVS + "\n");
			sql.append(" AND TD.SECEND_AUTID_STATUS="+Constant.DEALER_SECEND_STATUS_07+" \n");
			list = dao.pageQuery(sql.toString(), null, getFunName());
		}
		catch (Exception ex)
		{
			throw ex;
		}
		
		return list;
	}
	
	protected PO wrapperPO(ResultSet rs, int idx)
	{
		return null;
	}
	
	/**
	 * 方法描述 ： 获取服务器时间并进行格式化 - yyyy-mm-dd HH:mm:ss<br/>
	 * 
	 * @return
	 * @author wangsongwei
	 */
	public String getCurrentServerTime() throws Exception
	{
		return dao.pageQueryMap("select to_char(sysdate,'yyyy-mm-dd hh24:mi:ss') current_time from dual", null, getFunName()).get("CURRENT_TIME").toString();
	}
	
	/**
	 * 方法描述 ： 获取服务器时间并进行格式化 - yyyy-mm-dd HH:mm:ss<br/>
	 * 
	 * @return
	 * @author wangsongwei
	 */
	public String getSimpleCurrentServerTime() throws Exception
	{
		return dao.pageQueryMap("select to_char(sysdate,'yyyy-mm-dd') current_time from dual", null, getFunName()).get("CURRENT_TIME").toString();
	}
	
	/**
	 * 方法描述 ： 查询区域组织信息<br/>
	 * 
	 * @param level
	 *            2 大区， 3 小区
	 * @param type
	 *            10191001 主机厂、10191002 经销商
	 * @return
	 * @author wangsongwei
	 */
	public List<Map<String, Object>> getOrgList(int level, Integer type) throws Exception
	{
		StringBuffer sbSql = new StringBuffer();
		
		sbSql.append("SELECT T.ORG_ID, T.ORG_CODE,T.ORG_NAME FROM TM_ORG T WHERE T.ORG_TYPE = " + type + " AND T.ORG_LEVEL = " + level + " AND T.STATUS = " + Constant.STATUS_ENABLE
						+ " ORDER BY T.name_sort");
		
		return dao.pageQuery(sbSql.toString(), null, getFunName());
	}
	
	public List<Map<String, Object>> getOrgList(int level, Integer type,long OrgId) throws Exception
	{
		StringBuffer sbSql = new StringBuffer();
		if(OrgId==Long.parseLong(Constant.OEM_COM_JC)){
			sbSql.append("SELECT T.ORG_ID, T.ORG_CODE,T.ORG_NAME FROM TM_ORG T "
					+ "WHERE T.ORG_TYPE = " + type + " AND T.ORG_LEVEL = " + level + " AND "
					+ "T.STATUS = " + Constant.STATUS_ENABLE + " ORDER BY T.ORG_ID");
		}else{
			sbSql.append("SELECT T.ORG_ID, T.ORG_CODE,T.ORG_NAME FROM TM_ORG T "
					+ "WHERE T.ORG_TYPE = " + type + " AND T.ORG_LEVEL = " + level + " AND "
					+ "T.STATUS = " + Constant.STATUS_ENABLE + "AND T.ORG_ID = " + OrgId + " ORDER BY T.ORG_ID");
		}

		
		return dao.pageQuery(sbSql.toString(), null, getFunName());
	}

	/**
	 * 方法描述 ： 查询区域组织信息<br/>
	 * 
	 * @param level 2 大区， 3 小区
	 * @param type 10191001 主机厂、10191002 经销商
	 * @return
	 * @author wangsongwei
	 */
	public List<Map<String, Object>> getOrgList(String level, String parentOrgId) throws Exception
	{
		List<Object> params = new ArrayList<Object>();
		StringBuffer sbSql = new StringBuffer();
		params.add(level);
		
		sbSql.append("SELECT T.ORG_ID, T.ORG_CODE,T.ORG_NAME FROM TM_ORG T WHERE T.ORG_TYPE = 10191001 AND T.ORG_LEVEL = ? AND T.STATUS = " + Constant.STATUS_ENABLE + "\n");
		if(!parentOrgId.equals("")) {
			sbSql.append(" AND PARENT_ORG_ID = ?");
			params.add(parentOrgId);
		}
		
		sbSql.append("ORDER BY T.ORG_ID");
		
		return dao.pageQuery(sbSql.toString(), params, getFunName());
	}
	/**
	 * 方法描述 ： 查询车系数据用于下拉列表的显示<br/>
	 * 
	 * @param i
	 * @return
	 * @author wangsongwei
	 */
	public List<Map<String, Object>> getSerisList(String poseId, int level) throws Exception
	{
		StringBuffer sbSql = new StringBuffer();
		
		sbSql.append("SELECT A.GROUP_ID, A.GROUP_CODE, A.GROUP_NAME, A.GROUP_LEVEL\n");
		sbSql.append("  FROM TM_VHCL_MATERIAL_GROUP A,TM_AREA_GROUP B\n");
		sbSql.append(" WHERE A.GROUP_ID = B.MATERIAL_GROUP_ID AND A.GROUP_LEVEL = " + level + " \n");
		sbSql.append(MaterialGroupManagerDao.getPoseIdBusinessSql("B.AREA_ID", poseId));
		
		return dao.pageQuery(sbSql.toString(), null, getFunName());
	}
	
	/**
	 * 方法描述 ： 创建下载模板<br/>
	 * 
	 * @param content
	 * @param os
	 * @throws ParseException
	 * @author wangsongwei
	 */
	public void createXlsFileNomal(List<List<Object>> content, OutputStream os, String sheetName) throws Exception
	{
		try
		{
			WritableWorkbook workbook = Workbook.createWorkbook(os);
			WritableSheet sheet = workbook.createSheet(sheetName, 0);
			
			for (int i = 0; i < content.size(); i++)
			{
				for (int j = 0; j < content.get(i).size(); j++)
				{
					// 添加单元格
					sheet.addCell(new Label(j, i, content.get(i).get(j) != null ? content.get(i).get(j).toString() : ""));
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
	 * 获取三方信贷的银行数据
	 * 
	 * @param dealerId
	 * @return
	 * @author wangsongwei
	 */
	public List<Map<String, Object>> getThreedBankInfo(String dealerId)
	{
		List<Object> params = new ArrayList<Object>();
		StringBuffer sbSql = new StringBuffer();
		
		sbSql.append("SELECT T.BANK_NAME || '(' || T.BANK_NO || ')' BANK_NAME, TO_CHAR(T.CRE_ID) CRE_ID\n");
		sbSql.append("  FROM TT_SALES_OTHER_CREDIT T\n");
		sbSql.append(" WHERE T.STATUS = 10011001\n");
		sbSql.append("   AND T.DEAER_ID = ?");
		params.add(dealerId);
		
		return dao.pageQuery(sbSql.toString(), params, getFunName());
	}
	
	/**
	 * 获取三方信贷银行汇票
	 * 
	 * @param bankId
	 * @return
	 * @author wangsongwei
	 */
	public List<Map<String, Object>> getBankBookInfo(String bankId)
	{
		List<Object> params = new ArrayList<Object>();
		StringBuffer sbSql = new StringBuffer();
		
		sbSql.append("SELECT TO_CHAR(T.CRE_DETAIL_ID) CRE_DETAIL_ID, T.ACCEPT_NO, NVL(T.AMOUNT,0) - NVL(T.FREEZE_AMOUNT,0) AMOUNT\n");
		sbSql.append("  FROM TT_SALES_OTHER_ACCEPT T\n");
		sbSql.append(" WHERE T.CRE_ID = ?\n");
		sbSql.append("   AND T.STATUS = 16011003");
		
		params.add(bankId);
		
		return dao.pageQuery(sbSql.toString(), params, getFunName());
	}
	
	/**
	 * 获取经销商品牌选择数据
	 * 
	 * @param hsMap
	 * @return
	 */
	public List<Map<String, Object>> getOrderBrandList(Map<String, Object> hsMap)
	{
		List<Object> params = new ArrayList<Object>();
		StringBuffer sbSql = new StringBuffer();
		String queryFlag = (String) hsMap.get("queryFlag");
		
		sbSql.append("select to_char(t.group_id) group_id, t.group_name," +
				" t.group_code from tm_vhcl_material_group t where t.status = 10011001 and t.group_level = 1 ");
		if ("requireForcast".equals(queryFlag)) {
			sbSql.append("AND t.FORCAST_FLAG = '0'");
		} else {
			sbSql.append("AND t.SALES_FLAG = 10011001");
		}
		return dao.pageQuery(sbSql.toString(), params, getFunName());
	}
	
	/**
	 * 根据GROUP_ID和经销商获取哪些经销商可以提哪些配置的车
	 * @param hsMap
	 * @return
	 */
	public String getSalesLimit(Map<String, Object> hsMap){
		StringBuffer sbSql = new StringBuffer();
		if(null != hsMap && hsMap.size()>0){
			sbSql.append("and t.group_id in (select package_id\n");
			sbSql.append("                           from TT_SALES_VHCL_LIMMIT\n");
			sbSql.append("                          where dealer_id = "+hsMap.get("dealerId").toString()+"\n");
			sbSql.append("                            and sales_status = 10011001) \n"); 
		}
		return sbSql.toString();
	}
	/**
	 * 获取经销商车系选择数据
	 * 
	 * @param hsMap
	 * @return
	 */
	public List<Map<String, Object>> getOrderSeriesList(Map<String, Object> hsMap)
	{
		
		String dealerId = CommonUtils.checkNull(hsMap.get("dealerId"));
		String groupId = CommonUtils.checkNull(hsMap.get("groupId"));
		List<Object> params = new ArrayList<Object>();
		StringBuffer sbSql = new StringBuffer();
		String queryFlag = (String) hsMap.get("queryFlag");
		
		sbSql.append("select to_char(t.group_id) group_id, t.group_name, t.group_code\n");
		sbSql.append("  from tm_vhcl_material_group t\n");
		sbSql.append(" where t.status = 10011001\n");
		sbSql.append("   and t.parent_group_id = ?\n");
		sbSql.append("   and t.group_level = 2\n");
		sbSql.append("   and exists (select 1\n");
		sbSql.append("          from tt_sales_contract a, tt_sales_contract_dtl b\n");
		sbSql.append("         where a.contract_id = b.contract_id and a.dealer_id = ?\n");
		sbSql.append("           and b.group_id = t.group_id and b.CONTRACT_NO <> 0) \n");
		params.add(groupId);
		params.add(dealerId);
		if ("requireForcast".equals(queryFlag)) {
			sbSql.append("AND t.FORCAST_FLAG = '0'");
		} else {
			sbSql.append("AND t.SALES_FLAG = 10011001");
		}
		return dao.pageQuery(sbSql.toString(), params, getFunName());
	}
	
	/**
	 * 获取经销商车型选择数据
	 * 
	 * @param hsMap
	 * @return
	 */
	public List<Map<String, Object>> getOrderModelList(Map<String, Object> hsMap)
	{
		
		String dealerId = CommonUtils.checkNull(hsMap.get("dealerId"));
		String groupId = CommonUtils.checkNull(hsMap.get("groupId"));
		List<Object> params = new ArrayList<Object>();
		StringBuffer sbSql = new StringBuffer();
		String queryFlag = (String) hsMap.get("queryFlag");
		
		sbSql.append("select to_char(t.group_id) group_id, t.group_name, t.group_code\n");
		sbSql.append("  from tm_vhcl_material_group t\n");
		sbSql.append(" where t.status = 10011001\n");
		sbSql.append("   and t.group_level = 3 and t.parent_group_id = ?\n");
		// sbSql.append("   and exists (select 1\n");
		// sbSql.append("          from tt_sales_contract      a,\n");
		// sbSql.append("               tt_sales_contract_dtl  b,\n");
		// sbSql.append("               tm_vhcl_material_group c\n");
		// sbSql.append("         where a.contract_id = b.contract_id\n");
		// sbSql.append("           and a.dealer_id = ?\n");
		// sbSql.append("           and b.group_id = c.group_id\n");
		// sbSql.append("         start with c.group_level = 2\n");
		// sbSql.append("        connect by prior c.parent_group_id = c.group_id and t.parent_group_id = c.group_id)");
		params.add(groupId);
		// params.add(dealerId);

		if ("requireForcast".equals(queryFlag)) {
			sbSql.append("AND t.FORCAST_FLAG = '0'");
		} else {
			sbSql.append("AND t.SALES_FLAG = 10011001");
		}
		return dao.pageQuery(sbSql.toString(), params, getFunName());
	}
	
	/**
	 * 获取经销商配置选择数据
	 * 
	 * @param hsMap
	 * @return
	 */
	public List<Map<String, Object>> getOrderPackageList(Map<String, Object> hsMap)
	{
		
		String dealerId = CommonUtils.checkNull(hsMap.get("dealerId"));
		String groupId = CommonUtils.checkNull(hsMap.get("groupId"));
		List<Object> params = new ArrayList<Object>();
		StringBuffer sbSql = new StringBuffer();
		String queryFlag = (String) hsMap.get("queryFlag");
		
		sbSql.append("select to_char(t.group_id) group_id, t.group_name, t.group_code\n");
		sbSql.append("  from tm_vhcl_material_group t\n");
		sbSql.append(" where t.status = 10011001\n");
		sbSql.append("   and t.group_level = 4 and t.parent_group_id = ?\n");
		sbSql.append(getSalesLimit(hsMap));
		// sbSql.append("   and exists (select 1\n");
		// sbSql.append("          from tt_sales_contract      a,\n");
		// sbSql.append("               tt_sales_contract_dtl  b,\n");
		// sbSql.append("               tm_vhcl_material_group c\n");
		// sbSql.append("         where a.contract_id = b.contract_id\n");
		// sbSql.append("           and a.dealer_id = ?\n");
		// sbSql.append("           and b.group_id = c.group_id\n");
		// sbSql.append("         start with c.group_level = 2\n");
		// sbSql.append("        connect by t.parent_group_id = c.group_id)");
		params.add(groupId);
		if ("requireForcast".equals(queryFlag)) {
			sbSql.append("AND t.FORCAST_FLAG = '0'");
		} else {
			sbSql.append("AND t.SALES_FLAG = 10011001");
		}
		return dao.pageQuery(sbSql.toString(), params, getFunName());
	}
	
	public List<Map<String, Object>> getColorList(String packageId)
	{
		List<Object> params = new ArrayList<Object>();
		StringBuffer sbSql = new StringBuffer();
		
		sbSql.append("select vmt.COLOR_CODE GROUP_ID, vmt.COLOR_NAME GROUP_NAME\n");
		sbSql.append("  from vw_material_group_mat vmt\n");
		sbSql.append(" where vmt.PACKAGE_ID = ?\n");
		sbSql.append("	 and vmt.ORDER_FLAG = 10331001 \n");
		sbSql.append("	 and vmt.status = 10011001 \n");
		sbSql.append(" group by vmt.COLOR_CODE, vmt.COLOR_NAME");
		params.add(packageId);
		
		return dao.pageQuery(sbSql.toString(), params, getFunName());
	}
	
	public List<Map<String, Object>> getTrimList(String packageId, String colorCode)
	{
		List<Object> params = new ArrayList<Object>();
		StringBuffer sbSql = new StringBuffer();
		
		sbSql.append("select vmt.TRIM_CODE, vmt.TRIM_NAME, vmt.MATERIAL_ID\n");
		sbSql.append("  from vw_material_group_mat vmt\n");
		sbSql.append(" where vmt.COLOR_CODE = ?\n");
		sbSql.append("   and vmt.PACKAGE_ID = ?\n");
		sbSql.append(" group by vmt.TRIM_CODE, vmt.TRIM_NAME,vmt.MATERIAL_ID");
		params.add(colorCode);
		params.add(packageId);
		
		return dao.pageQuery(sbSql.toString(), params, getFunName());
	}
	
	/**
	 * 全国经销商物料配置结算价格查询
	 * 
	 * @param packageId
	 * @param dealerId
	 * @return
	 */
	public Double getOrderMaterialPrice(String packageId, String dealerId)
	{
		List<Object> params = new ArrayList<Object>();
		StringBuffer sbSql = new StringBuffer();
		
		sbSql.append("select\n");
		sbSql.append("  nvl((select nvl(a.last_settle_amount, 0)\n");
		sbSql.append("    from TT_SALES_MODLE_DISRATE_DEALER a\n");
		sbSql.append("   where a.status = 10011001 and a.dealer_id = ?\n");
		sbSql.append("     and a.audit_status = 99301003 and a.package_id = ?),0)\n");
		sbSql.append("  -\n");
		sbSql.append("  nvl((select dis_amount from TT_SALES_DEALER_DISRATE where dealer_id = ?),0) MATERIAL_PRICE\n");
		sbSql.append("from dual");
		params.add(dealerId);
		params.add(packageId);
		params.add(dealerId);
		
		Map<String, Object> map = dao.pageQueryMap(sbSql.toString(), params, getFunName());
		
		return Double.parseDouble(map.get("MATERIAL_PRICE").toString());
	}
	
	/**
	 * 物料资源数量
	 * 
	 * @param materialId
	 * @return
	 */
	public String getOrderMaterialResource(String materialId)
	{
		List<Object> params = new ArrayList<Object>();
		StringBuffer sbSql = new StringBuffer();
		
		sbSql.append("select nvl((" + AsSqlUtils.getAsSqlVehicleResourceBuffer(materialId) + "),0) VEHICLE_AMOUNT from dual");
		params.add(materialId);
		
		Map<String, Object> map = dao.pageQueryMap(sbSql.toString(), params, getFunName());
		
		return CommonUtils.checkNull(map.get("VEHICLE_AMOUNT"));
	}
	
	/**
	 * 获取物料信息，主要包括 ： 物料结算单价 = 经销商结算价 + 经销商折扣 物料的资源数量 = 车厂实际库存 - 已提报的订单明细汇总数
	 * 
	 * @param packageId
	 * @param colorCode
	 * @param dealerId
	 * @return
	 */
	public List<Map<String, Object>> getMaterialMessage(String packageId, String colorCode, String dealerId)
	{
		List<Object> params = new ArrayList<Object>();
		StringBuffer sbSql = new StringBuffer();
		
		sbSql.append("WITH " + AsSqlUtils.getAsSqlVehicleResourceBuffer());
		sbSql.append("SELECT A2.MATERIAL_ID,\n");
		sbSql.append("       NVL(A1.VEHICLE_AMOUNT, 0) VEHICLE_AMOUNT,\n");
		sbSql.append("       A2.TRIM_NAME,\n");
		sbSql.append("       NVL((SELECT A5.LAST_SETTLE_AMOUNT\n");
		sbSql.append("             FROM TT_SALES_MODLE_DISRATE_DEALER A5\n");
		sbSql.append("            WHERE A4.GROUP_ID = A5.PACKAGE_ID AND A5.STATUS = 10011001 AND\n");
		sbSql.append("             A5.AUDIT_STATUS = 99301003 AND TRUNC(SYSDATE) >= TRUNC(A5.START_DATE) AND TRUNC(SYSDATE) <= TRUNC(A5.STOP_DATE) AND A5.DEALER_ID = ?\n");
		sbSql.append("            GROUP BY A5.LAST_SETTLE_AMOUNT),\n");
		sbSql.append("           0) LAST_SETTLE_AMOUNT\n");
		sbSql.append("  FROM VEHICLE_STOCK            A1,\n");
		sbSql.append("       TM_VHCL_MATERIAL         A2,\n");
		sbSql.append("       TM_VHCL_MATERIAL_GROUP_R A3,\n");
		sbSql.append("       TM_VHCL_MATERIAL_GROUP   A4\n");
		sbSql.append(" WHERE A2.MATERIAL_ID = A1.MATERIAL_ID(+)\n");
		sbSql.append("   AND A2.MATERIAL_ID = A3.MATERIAL_ID\n");
		sbSql.append("   AND A3.GROUP_ID = A4.GROUP_ID\n");
		sbSql.append("   AND A4.GROUP_ID = ?\n");
		sbSql.append("   AND A2.COLOR_CODE = ?");
		params.add(dealerId);
		params.add(packageId);
		params.add(colorCode);
		
		return dao.pageQuery(sbSql.toString(), params, getFunName());
	}
	
	/**
	 * @param packageId
	 *            配置明细
	 * @param dealerId
	 *            经销商ID
	 * @return
	 */
	public List<Map<String, Object>> getDealerPackagePrice(String packageId, String dealerId)
	{
		List<Object> params = new ArrayList<Object>();
		StringBuffer sbSql = new StringBuffer();
		
		sbSql.append("SELECT NVL(B.LAST_SETTLE_AMOUNT, 0) LAST_SETTLE_AMOUNT, \n");
		sbSql.append("       NVL(B.DIS_RATE, 0) DIS_RATE,  \n");
		sbSql.append(" 	     B.DISRATE_ID \n");
		sbSql.append("  FROM TM_VHCL_MATERIAL_GROUP A, TT_SALES_MODLE_DISRATE_DEALER B\n");
		sbSql.append(" WHERE A.GROUP_ID = B.PACKAGE_ID(+)\n");
		sbSql.append("   AND A.GROUP_LEVEL = 4\n");
		sbSql.append("   AND A.GROUP_ID = ?\n");
		sbSql.append("   AND B.STATUS = 10011001\n");
		sbSql.append("   AND B.AUDIT_STATUS = 99301003\n");
		sbSql.append("   AND B.DEALER_ID = ? \n");
		sbSql.append("   AND to_char(to_char(B.START_DATE, 'yyyy-MM-dd') || ' 00:00:00') <= to_char(SYSDATE, 'yyyy-mm-dd hh24:mi:ss') \n");
		sbSql.append("   AND to_char(to_char(B.STOP_DATE,  'yyyy-MM-dd') || ' 23:59:59') >= to_char(SYSDATE, 'yyyy-mm-dd hh24:mi:ss') \n");
		params.add(packageId);
		params.add(dealerId);
		return dao.pageQuery(sbSql.toString(), params, getFunName());
	}
	
	/**
	 * 取得结算价对应的商务政策ID
	 */
	public List<Map<String, Object>> getDeployId(String disrateId) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT DEPLOY_ID FROM TT_SALES_PRICE_LIST_R R WHERE R.DISRATE_ID=").append(disrateId);
		return dao.pageQuery(sql.toString(), null, getFunName());
	}
	
	/**
	 * 取得结算价对应的商务政策
	 */
	public List<Map<String, Object>> getDeploy(String deployId) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT D.DEPLOY_ID FROM TT_SALES_POLICY_DEPLOY D WHERE D.DEPLOY_STATUS='1' AND D.DEPLOY_ID=").append(deployId).append(" \n");
		sql.append(" AND to_char(to_char(D.START_DATE, 'yyyy-MM-dd') || ' 00:00:00') <= to_char(SYSDATE, 'yyyy-mm-dd hh24:mi:ss') \n");
		sql.append(" AND to_char(to_char(D.STOP_DATE,  'yyyy-MM-dd') || ' 23:59:59') >= to_char(SYSDATE, 'yyyy-mm-dd hh24:mi:ss') ");
		return dao.pageQuery(sql.toString(), null, getFunName());
	}
	
	/**
	 * 选配项价格
	 * 
	 * @param params 参数
	 * @return 选配项价格列表
	 */
	public List<Map<String, Object>> getSalesXpPrice(Map<String, Object> params) {
		String ksid = (String) params.get("ksid");
		String dealerId = (String) params.get("dealerId");
		String xpcode = (String) params.get("xpcode");
		String iccode = (String) params.get("iccode");
		
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT NVL(SUM(TSXP.AMOUNT), 0) AMOUNT \n");
		sql.append(" FROM TT_SALES_XP_PRICE TSXP \n");
		sql.append(" INNER JOIN XP_KXZTSJ XK ON XK.DETAIL_ID = TSXP.XP_DETAIL_ID");
		sql.append(" WHERE TSXP.KSID = '").append(ksid).append("' \n");
		sql.append("   AND TSXP.DEALER_ID = ").append(dealerId).append(" \n");
		sql.append("   AND TSXP.STATUS = 10011001 \n");
		sql.append("   AND to_char(to_char(TSXP.START_DATE, 'yyyy-MM-dd') || ' 00:00:00') <= to_char(SYSDATE, 'yyyy-mm-dd hh24:mi:ss') \n");
		sql.append("   AND to_char(to_char(TSXP.END_DATE,  'yyyy-MM-dd') || ' 23:59:59') >= to_char(SYSDATE, 'yyyy-mm-dd hh24:mi:ss') \n");
		sql.append("   AND TSXP.AUDIT_STATUS = 99301003 \n");
		if (xpcode != null) {
			sql.append("   AND TSXP.XP_CODE = ").append(xpcode).append("\n");
		}
		if (iccode != null) {
			sql.append("   AND XK.ICCODE = ").append(iccode).append("\n");
			sql.append("   AND XK.ISDEF = '1' \n");
		}
		
		return dao.pageQuery(sql.toString(), null, getFunName());
	}
	
	/**
	 * 获取选配系统中的外饰颜色列表
	 * 
	 * @param packageId
	 * @return
	 */
	public List<Map<String, Object>> getXpColorList(String packageId, String dealerId, String queryFlag)
	{
		List<Object> params = new ArrayList<Object>();
		StringBuffer sbSql = new StringBuffer();
		
		sbSql.append("SELECT B.ITCODE GROUP_ID, b.ITNAME GROUP_NAME\n");
		sbSql.append("  FROM TM_VHCL_MATERIAL_GROUP A, XP_KXZTSJ B\n");
		sbSql.append(" WHERE A.KSID = B.KSID\n");
		sbSql.append("   AND B.ICNAME = '外饰颜色'\n");
		sbSql.append("   AND A.GROUP_LEVEL = 4\n");
		sbSql.append("   AND A.GROUP_ID = ?\n");
		sbSql.append("   AND B.STATUS = '1'\n");
		if ("addOrder".equals(queryFlag)) {
			sbSql.append("   AND (B.SALES_STATUS IS NULL OR B.SALES_STATUS=0)\n");
		} else if ("requireForcast".equals(queryFlag)){
			sbSql.append("   AND B.FORECAST_FLAG = '0' \n");
		}
		sbSql.append(AsSqlUtils.getDealerPackageXpBuffer("B", dealerId));
		sbSql.append("GROUP BY B.ITCODE, b.ITNAME \n");
		params.add(packageId);
		return dao.pageQuery(sbSql.toString(), params, getFunName());
	}

	/**
	 * 获取选配系统中某一种车型配置的默认选配状态
	 * 
	 * @param packageId
	 * @return
	 */
	public List<Map<String, Object>> getXpDefaultList(String packageId, String colorCode, String queryFlag, String dealerId)
	{
		List<Object> params = new ArrayList<Object>();
		StringBuffer sbSql = new StringBuffer();
		
		sbSql.append("SELECT * FROM TM_VHCL_MATERIAL_GROUP WHERE GROUP_ID = ?");
		params.add(packageId);
		
		Map<String, Object> map = dao.pageQueryMap(sbSql.toString(), params, getFunName());
		
		String ksid = CommonUtils.checkNull(map.get("KSID"));
		String icode = colorCode.substring(0, 2);
		
		sbSql.delete(0, sbSql.length());
		params.clear();
		sbSql.append("WITH XPSJ AS (\n");
		sbSql.append("SELECT *\n");
		sbSql.append("  FROM XP_KXZTSJ\n");
		sbSql.append(" WHERE ITCODE IN (SELECT XZCODE\n");
		sbSql.append("                    FROM XP_ZTXZSJ\n");
		sbSql.append("                   WHERE ITCODE IN (SELECT ITCODE\n");
		sbSql.append("                                      FROM XP_KXZTSJ\n");
		sbSql.append("                                     WHERE KSID = ?\n");
		sbSql.append("                                       AND ICCODE <> ?\n");
		sbSql.append("                                       AND ISDEF = '1' \n");
		sbSql.append("                                       AND ITNAME IS NOT NULL AND ITNAME <> 'NULL' AND ITNAME <> ' ')\n");
		sbSql.append("                     AND KSID = ?\n");
		sbSql.append("                     AND FLAG = '1')\n");
		sbSql.append("   AND KSID = ?\n");
		if ("addOrder".equals(queryFlag)) {
			sbSql.append("   AND (SALES_STATUS = '0' OR SALES_STATUS IS NULL) \n");
		} else if ("requireForcast".equals(queryFlag)){
			sbSql.append("   AND FORECAST_FLAG = '0' \n");
		}
		sbSql.append(AsSqlUtils.getDealerPackageXpBuffer(null, dealerId));
		sbSql.append("UNION ALL\n");
		sbSql.append("SELECT *\n");
		sbSql.append("  FROM XP_KXZTSJ\n");
		sbSql.append(" WHERE KSID = ?\n");
		sbSql.append("   AND ICCODE <> ?\n");
		sbSql.append("   AND ITNAME IS NOT NULL AND ITNAME <> 'NULL' AND ITNAME <> ' ' \n");
		sbSql.append("   AND ISDEF = '1'\n");
		if ("addOrder".equals(queryFlag)) {
			sbSql.append("   AND (SALES_STATUS = '0' OR SALES_STATUS IS NULL) \n");
		} else if ("requireForcast".equals(queryFlag)){
			sbSql.append("   AND FORECAST_FLAG = '0' \n");
		}
		sbSql.append(AsSqlUtils.getDealerPackageXpBuffer(null, dealerId));
		sbSql.append("UNION ALL\n");
		sbSql.append("SELECT *\n");
		sbSql.append("  FROM XP_KXZTSJ\n");
		sbSql.append(" WHERE KSID = ?\n");
		sbSql.append("   AND ITCODE = ?\n");
		if ("addOrder".equals(queryFlag)) {
			sbSql.append("   AND (SALES_STATUS = '0' OR SALES_STATUS IS NULL) \n");
		} else if ("requireForcast".equals(queryFlag)){
			sbSql.append("   AND FORECAST_FLAG = '0' \n");
		}
		sbSql.append(AsSqlUtils.getDealerPackageXpBuffer(null, dealerId));
		sbSql.append(") SELECT * FROM XPSJ where status=1 ORDER BY ITCODE");
		
		params.add(ksid);
		params.add(icode);
		params.add(ksid);
		params.add(ksid);
		params.add(ksid);
		params.add(icode);
		params.add(ksid);
		params.add(colorCode);
		
		return dao.pageQuery(sbSql.toString(), params, getFunName());
	}

	/**
	 * 获取选配状态限制
	 * 
	 * @param packageId
	 * @param xpCode
	 * @return
	 */
	public List<Map<String, Object>> getXpLimitCodesList(String ksid, String xpCode)
	{
		List<Object> params = new ArrayList<Object>();
		StringBuffer sbSql = new StringBuffer();
		
		sbSql.append("SELECT * FROM XP_ZTXZSJ WHERE KSID = ? AND ITCODE = ?"); 
		params.add(ksid); params.add(xpCode);
		
		return dao.pageQuery(sbSql.toString(), params, getFunName());
	}
	
	public List<Map<String, Object>> getAddressListShowByDealerId(
			String dealerId,String orderId) throws Exception {

		List<Map<String, Object>> list = null;
		
		try
		{
			StringBuffer sql = new StringBuffer("\n");
			
			sql.append("\n-- 经销商地址查询  \n");
			sql.append("select  tva.id, tva.address, tva.city_id 	\n");
			sql.append("from tm_vs_address tva,tt_sa_order ord					\n");
			sql.append(" where 1 = 1 and tva.id  = ord.address_id	\n");
			if (dealerId != null && !"".equals(dealerId))
			{
				sql.append("   and tva.dealer_id =" + dealerId + "	\n");
			}
			if (orderId != null && !"".equals(orderId))
			{
				sql.append("   and ord.ORDER_ID =" + orderId + "	\n");
			}
			
			sql.append("   and tva.status =" + Constant.STATUS_ENABLE + "\n");
			
			list = pageQuery(sql.toString(), null, getFunName());
		}
		catch (Exception ex)
		{
			throw ex;
		}
		
		return list;
	}
	
	/**
	 * 获取乘商用车的价格
	 * @param seriesId
	 * @return
	 */
	public Double getModelTypePrice(String seriesId) {
		/*List<Object> params = new ArrayList<Object>();
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("select decode(model_type,12421001,"+Constant.OUT_MODEL_PRICE_7000.intValue()+",12421002,"+Constant.OUT_MODEL_PRICE_5000.intValue()+",0) MODEL_PRICE from tm_vhcl_material_group where group_id = ?");
		params.add(seriesId);
		String modelPrice = CommonUtils.checkNullNum(dao.pageQueryMap(sbSql.toString(), params, getFunName()).get("MODEL_PRICE").toString());
		return Double.parseDouble(modelPrice);*/
		return 0.0;
	}
	
	/**
	 * 获取经销商可用保证金以及额度
	 * @param dealerId 
	 * 
	 * @return
	 */
	public Map<String,Object> getDealerEdAndBzj(String dealerId) {
		
		Double remitAmount = CarSubmissionDao.getInstance().getRemitAmountByID(Long.parseLong(dealerId));
		
		List<Object> params = new ArrayList<Object>();
		StringBuffer sbSql = new StringBuffer();
		
		/*sbSql.append("select nvl((select nvl(t.model_type_c,0)");
		sbSql.append("             from tt_out_credence t\n");
		sbSql.append("            where t.dealer_id = ?\n");
		sbSql.append("              and t.status = 10011001),\n");
		sbSql.append("           0) - nvl((select sum(b.order_amount)\n");
		sbSql.append("                      from tt_vs_order a, tt_vs_order_detail b,vw_material_group_mat vmt\n");
		sbSql.append("                     where a.order_id = b.order_id\n");
		sbSql.append("                       and a.order_type = 10201004\n");
		sbSql.append("                       and b.material_id = vmt.material_id\n");
		sbSql.append("                       and vmt.model_type = "+Constant.MODEL_TYPE_C+"\n");
		sbSql.append("                       and a.order_status not in\n");
		sbSql.append("                           (10211000, 10211001, 10211003, 10211013)\n");
		sbSql.append("                       and a.dealer_id = ?),\n");
		sbSql.append("                    0) + nvl((select count(1)\n");
		sbSql.append("                               from tt_vs_order        a,\n");
		sbSql.append("                                    tt_vs_order_detail b,\n");
		sbSql.append("                                    tt_sales_bo_detail c,\n");
		sbSql.append("                                    tt_sales_alloca_de d,\n");
		sbSql.append("                                    tm_vehicle         e,\n");
		sbSql.append("                                    vw_material_group_mat         vmt\n");
		sbSql.append("                              where a.order_id = b.order_id\n");
		sbSql.append("                       		  and b.material_id = vmt.material_id\n");
		sbSql.append("                                and b.detail_id = c.or_de_id\n");
		sbSql.append("                                and c.bo_de_id = d.bo_de_id\n");
		sbSql.append("                                and d.vehicle_id = e.vehicle_id\n");
		sbSql.append("                                and a.order_type = 10201004\n");
		sbSql.append("                                and a.order_status = 10211011\n");
		sbSql.append("                                and e.out_status = 97151003\n");
		sbSql.append("                       		  and vmt.model_type = "+Constant.MODEL_TYPE_C+"\n");
		sbSql.append("                                and a.dealer_id = ?),\n");
		sbSql.append("                             0) MODEL_TYPE_C,\n");
		sbSql.append(" nvl((select nvl(t.model_type_s,0)");
		sbSql.append("             from tt_out_credence t\n");
		sbSql.append("            where t.dealer_id = ?\n");
		sbSql.append("              and t.status = 10011001),\n");
		sbSql.append("           0) - nvl((select sum(b.order_amount)\n");
		sbSql.append("                      from tt_vs_order a, tt_vs_order_detail b,vw_material_group_mat vmt\n");
		sbSql.append("                     where a.order_id = b.order_id\n");
		sbSql.append("                       and a.order_type = 10201004\n");
		sbSql.append("                       and b.material_id = vmt.material_id\n");
		sbSql.append("                       and vmt.model_type = "+Constant.MODEL_TYPE_S+"\n");
		sbSql.append("                       and a.order_status not in\n");
		sbSql.append("                           (10211000, 10211001, 10211003, 10211013)\n");
		sbSql.append("                       and a.dealer_id = ?),\n");
		sbSql.append("                    0) + nvl((select count(1)\n");
		sbSql.append("                               from tt_vs_order        a,\n");
		sbSql.append("                                    tt_vs_order_detail b,\n");
		sbSql.append("                                    tt_sales_bo_detail c,\n");
		sbSql.append("                                    tt_sales_alloca_de d,\n");
		sbSql.append("                                    tm_vehicle         e,\n");
		sbSql.append("                                    vw_material_group_mat         vmt\n");
		sbSql.append("                              where a.order_id = b.order_id\n");
		sbSql.append("                       		  and b.material_id = vmt.material_id\n");
		sbSql.append("                                and b.detail_id = c.or_de_id\n");
		sbSql.append("                                and c.bo_de_id = d.bo_de_id\n");
		sbSql.append("                                and d.vehicle_id = e.vehicle_id\n");
		sbSql.append("                                and a.order_type = 10201004\n");
		sbSql.append("                                and a.order_status = 10211011\n");
		sbSql.append("                                and e.out_status = 97151003\n");
		sbSql.append("                       		  and vmt.model_type = "+Constant.MODEL_TYPE_S+"\n");
		sbSql.append("                                and a.dealer_id = ?),\n");
		sbSql.append("                             0) MODEL_TYPE_S\n");
		sbSql.append("  from dual");*/
		params.add(dealerId);params.add(dealerId);params.add(dealerId);
		params.add(dealerId);params.add(dealerId);params.add(dealerId);
		
		Map<String, Object> map = dao.pageQueryMap(sbSql.toString(), params, getFunName());
		map.put("REMIT_AMOUNT", remitAmount);
		
		return map;
	}
}
