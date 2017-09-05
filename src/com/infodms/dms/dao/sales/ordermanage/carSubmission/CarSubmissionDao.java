package com.infodms.dms.dao.sales.ordermanage.carSubmission;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.common.AjaxSelectDao;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.dao.sales.storage.sendManage.SendAssignmentDao;
import com.infodms.dms.exception.UserException;
import com.infodms.dms.po.TmDealerPO;
import com.infodms.dms.po.TmVhclMaterialGroupPO;
import com.infodms.dms.po.TmpVsOrderResourceReservePO;
import com.infodms.dms.po.TtSaleFundsRoseDetailPO;
import com.infodms.dms.po.TtSalesOrderAccPO;
import com.infodms.dms.po.TtSalesOrderRebPO;
import com.infodms.dms.po.TtSalesVhclLimmitPO;
import com.infodms.dms.po.TtSalesXpLimitPo;
import com.infodms.dms.po.TtVsBoOrderPO;
import com.infodms.dms.po.TtVsOrderCheckPO;
import com.infodms.dms.po.TtVsOrderDetailPO;
import com.infodms.dms.po.TtVsOrderDtlSplitPO;
import com.infodms.dms.po.TtVsOrderPO;
import com.infodms.dms.po.TtVsOrderResourceReservePO;
import com.infodms.dms.po.TtVsOrderSplitPO;
import com.infodms.dms.po.TtVsOrderSplitRelPO;
import com.infodms.dms.po.XpKxztsjPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.StringUtil;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infodms.dms.util.vehicle.AsSqlUtils;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.POFactory;
import com.infoservice.po3.POFactoryBuilder;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

/**
 * @Description
 * @author yudanwen
 * @date 2013-4-9 上午10:55:56
 * @version 2.0
 */
public class CarSubmissionDao extends BaseDao<PO> {
	
	private static final CarSubmissionDao dao = new CarSubmissionDao();
	private static POFactory factory = POFactoryBuilder.getInstance();
	private static final CarSubmissionQueryDao queryDao = CarSubmissionQueryDao.getInstance();
	private static final SendAssignmentDao sendAssignmentDao = SendAssignmentDao.getInstance();
	
	public static final CarSubmissionDao getInstance() {
		return dao;
	}
	private CarSubmissionDao() {}
	
	/**
	 * 方法描述 ： 资源重新分配处理<br/>
	 * 
	 * @param valueOf
	 * @param remark
	 * @param logonUser
	 * @author wangsongwei
	 */
	public void saveResourceChange(String orderId, AclUserBean logonUser) throws Exception
	{
		
		// 获取订单主表数据
		TtVsOrderPO tvop_sql = new TtVsOrderPO();
		tvop_sql.setOrderId(Long.valueOf(orderId));
		
		List<?> orderList = dao.select(tvop_sql);
		
		if (orderList.size() > 0)
		{
			StringBuffer sbSql = new StringBuffer();
			
			sbSql.append("SELECT NVL(T.AMOUNT,0) AMOUNT, NVL(T.ALLOCA_NUM,0) ALLOCA_NUM FROM TT_VS_ORDER_RESOURCE_RESERVE T\n");
			sbSql.append("WHERE\n");
			sbSql.append("       T.ORDER_DETAIL_ID IN\n");
			sbSql.append("       (\n");
			sbSql.append("            SELECT\n");
			sbSql.append("                   TVOD.DETAIL_ID\n");
			sbSql.append("            FROM\n");
			sbSql.append("                   TT_VS_ORDER TVO,\n");
			sbSql.append("                   TT_VS_ORDER_DETAIL TVOD\n");
			sbSql.append("            WHERE\n");
			sbSql.append("                   TVO.ORDER_ID = TVOD.ORDER_ID\n");
			sbSql.append("                   AND\n");
			sbSql.append("                   TVO.ORDER_ID = " + orderId + "\n");
			sbSql.append("       ) AND T.RESERVE_STATUS = " + Constant.RESOURCE_RESERVE_STATUS_01);
			
			List<Map<String, Object>> resTmpList = dao.pageQuery(sbSql.toString(), null, getFunName());
			
			boolean flag = true;
			
			if (resTmpList.size() == 0)
			{
				throw new RuntimeException("无效的订单，该订单没有对应的资源分配!");
			}
			else
			{
				for (Map<String, Object> map : resTmpList)
				{
					Integer alloca = Integer.parseInt(map.get("ALLOCA_NUM").toString());
					
					if (alloca != 0)
					{
						flag = false;
					}
				}
			}
			
			if (!flag)
			{
				throw new RuntimeException("资源已配车，无法重新分配!");
			}
			
			sbSql.delete(0, sbSql.length());
			
			sbSql.append("SELECT * FROM TMP_VS_ORDER_RESOURCE_RESERVE T\n");
			sbSql.append("WHERE\n");
			sbSql.append("       T.ORDER_DETAIL_ID IN\n");
			sbSql.append("       (\n");
			sbSql.append("            SELECT\n");
			sbSql.append("                   TVOD.DETAIL_ID\n");
			sbSql.append("            FROM\n");
			sbSql.append("                   TT_VS_ORDER TVO,\n");
			sbSql.append("                   TT_VS_ORDER_DETAIL TVOD\n");
			sbSql.append("            WHERE\n");
			sbSql.append("                   TVO.ORDER_ID = TVOD.ORDER_ID\n");
			sbSql.append("                   AND\n");
			sbSql.append("                   TVO.ORDER_ID = " + orderId + "\n");
			sbSql.append("       )");
			
			resTmpList = dao.pageQuery(sbSql.toString(), null, getFunName());
			
			if (resTmpList.size() == 0)
			{
				// 保存审核意见
				saveCheckResult(Long.valueOf(orderId), "资源调整", Constant.FLEET_SUPPORT_CHECK_STATUS_01, Constant.OUT_ORDER_AUDIT_TYPE_01, logonUser);
				
				return;
			}
			
			// 回滚提车单的所有数据
			rollbackOrderReource(Long.valueOf(orderId), logonUser.getUserId());
			
			sbSql.delete(0, sbSql.length());
			sbSql.append("SELECT TPD.PLAN_DETAIL_ID\n");
			sbSql.append("  FROM TT_VS_ORDER  		 TVO,\n");
			sbSql.append("  	 TT_VS_ORDER_DETAIL  TVOD,\n");
			sbSql.append("       TM_CUS_ORDER        TCO,\n");
			sbSql.append("       TM_CUS_ORDER_DETAIL TCOD,\n");
			sbSql.append("       TM_PRO_DETAIL       TMPD,\n");
			sbSql.append("       TM_PLAN_DETAIL      TPD\n");
			sbSql.append(" WHERE TVOD.CUS_DETAIL_ID = TCOD.DETAIL_ID\n");
			sbSql.append("   AND TVO.ORDER_ID = TVOD.ORDER_ID\n");
			sbSql.append("   AND TCOD.CUS_ORDER_ID = TCO.CUS_ORDER_ID\n");
			sbSql.append("   AND TCO.PRO_ORDER_ID = TMPD.PRO_ORDER_ID\n");
			sbSql.append("   AND TMPD.PLAN_DETAIL_ID = TPD.PLAN_DETAIL_ID\n");
			sbSql.append("   AND TVO.ORDER_ID = ? GROUP BY TPD.PLAN_DETAIL_ID");
			
			List<Object> params = new ArrayList<Object>();
			params.add(orderId);
			
			Map<String, Object> mapData = dao.pageQueryMap(sbSql.toString(), params, getFunName());
			
			Long palnDetailId = mapData == null ? null : Long.valueOf(CommonUtils.checkNull(mapData.get("PLAN_DETAIL_ID")));
			
			for (int i = 0; i < resTmpList.size(); i++)
			{
				Map<String, Object> resTmp = resTmpList.get(i);
				
				TtVsOrderResourceReservePO res = new TtVsOrderResourceReservePO();
				
				res.setAmount(Integer.parseInt(resTmp.get("AMOUNT").toString()));
				res.setMaterialId(Long.valueOf(resTmp.get("MATERIAL_ID").toString()));
				res.setOrderDetailId(Long.valueOf(resTmp.get("ORDER_DETAIL_ID").toString()));
				res.setOrgId(Long.valueOf(resTmp.get("ORG_ID").toString()));
				res.setReserveStatus(Constant.RESOURCE_RESERVE_STATUS_01);
				res.setCreateBy(logonUser.getUserId());
				res.setCreateDate(new Date());
				res.setUpdateBy(logonUser.getUserId());
				res.setUpdateDate(new Date());
				res.setCreateId(palnDetailId);
				
				res.setReserveId(Long.valueOf(SequenceManager.getSequence("")));
				dao.insert(res);
			}
			
			// 更新资源预留数据
			// saveOrderReourceAfterCheck(Long.valueOf(orderId),
			// logonUser.getUserId());
			
			// 保存审核意见
			saveCheckResult(Long.valueOf(orderId), "资源调整", Constant.FLEET_SUPPORT_CHECK_STATUS_01, Constant.OUT_ORDER_AUDIT_TYPE_01, logonUser);
			
		}
	}
	
	/**
	 * 方法描述 : 清除订单返利使用明细表 <br/>
	 * <b>注意：根据业务需要手动调用返利数据回写方法</b>
	 * 
	 * @param orderId
	 *            返利主表ID
	 * @param goodsIds
	 *            不需要清除的返利明细表ID
	 * @param logonUser
	 *            当前操作用户
	 * @author wangsongwei
	 */
	public void cleanOrderSalesRebate(Long orderId, String[] rebateDetailIds, Long userId)
	{
		if (null == rebateDetailIds)
		{
			String sql = "DELETE FROM TT_SALES_ORDER_REB  T WHERE T.ORDER_ID = " + orderId;
			
			dao.delete(sql, null);
		}
		else
		{
			TtSalesOrderRebPO tsorp_sql = new TtSalesOrderRebPO();
			tsorp_sql.setOrderId(orderId);
			
			List<?> detailList = dao.select(tsorp_sql);
			
			for (int i = 0; i < detailList.size(); i++)
			{
				TtSalesOrderRebPO tsorp = (TtSalesOrderRebPO) detailList.get(i);
				
				boolean flag = false;
				
				for (int j = 0; j < rebateDetailIds.length; j++)
				{
					String temp = CommonUtils.checkNull(rebateDetailIds[j]);
					if (!temp.equals(""))
					{
						Long rebateIdTemp = Long.valueOf(temp);
						
						if (tsorp.getDetailId() == rebateIdTemp)
						{
							flag = true;
							break;
						}
					}
				}
				
				if (!flag)
				{
					String sql = "DELETE FROM TT_SALES_ORDER_REB  T WHERE T.DETAIL_ID = " + tsorp.getDetailId();
					dao.delete(sql, null);
				}
			}
		}
	}
	
	
	/**
	 * 方法描述 ： 审核不通过
	 * 
	 * @param orderId
	 *            提车单ID
	 * @param remark
	 *            审核备注说明
	 * @param logonUser
	 *            当前登陆用户
	 * @author wangsongwei
	 */
/*	public void orderSubmitUnPass(Map<String, Object> map, AclUserBean logonUser) throws Exception
	{
		String orderId = CommonUtils.checkNull(map.get("orderId"));
		String remark = CommonUtils.checkNull(map.get("remark"));
		String auditDep = CommonUtils.checkNull(map.get("auditDep"));
		// 获取订单主表数据
		TtVsOrderPO tvop_sql = new TtVsOrderPO();
		tvop_sql.setOrderId(Long.valueOf(orderId));
		
		List<?> orderList = dao.select(tvop_sql);
		
		if (orderList.size() > 0)
		{
			TtVsOrderPO tvop = (TtVsOrderPO) orderList.get(0);
			
			if (tvop.getBoOrderId().equals("-1"))
			{
				throw new RuntimeException("BO单转提车单不允许驳回!");
			}
			
			int lastStatus = tvop.getOrderStatus(); // 当前的流程状态
			
			// 更新订单状态
			tvop_sql.setVer(tvop.getVer());
			tvop.setAuditDpt(Integer.parseInt(auditDep));
			tvop.setUpdateBy(logonUser.getUserId());
			tvop.setUpdateDate(new Date());
			tvop.setVer(tvop.getVer() + 1);
			
			dao.update(tvop_sql, tvop);
			
			if (auditDep.equals(Constant.OUT_ORDER_AUDIT_TYPE_01.toString()))
			{
				if (lastStatus != Constant.ORDER_STATUS_02.intValue())
				{
					throw new RuntimeException("订单状态与实际流程状态不符,非法流程操作!");
				}
				
				 --------- 计划处驳回 --------- 
				tvop.setOrderStatus(Constant.ORDER_STATUS_03);
				
				// 回退资金账户数据
				rollbackDealerAccount(Long.valueOf(orderId), tvop.getFundTypeId(), logonUser.getUserId());
				
				rollbackOrderReource(Long.valueOf(orderId), logonUser.getUserId());
			}
			else
			{
				throw new RuntimeException("订单已通过审核主流程,不允许驳回!只能作废!");
			}
			
			// 保存审核意见
			saveCheckResult(tvop.getOrderId(), remark, Constant.FLEET_SUPPORT_CHECK_STATUS_02, Integer.parseInt(auditDep), logonUser);
		}
	}*/
	
	/**
	 * 方法描述 ： 订单信息查询<br/>
	 * 
	 * @param map
	 *            查询参数集
	 * @param curPage
	 *            当前页
	 * @param pageSize
	 *            每页总记录数
	 * @return java.util.Map
	 * @author wangsongwei
	 */
	public PageResult<Map<String, Object>> getCarSubmissionOrderList(Map<String, Object> map, Integer curPage, Integer pageSize) throws Exception
	{
		String orderNo = CommonUtils.checkNull(map.get("orderNo"));// 车场ID
		String startdate = CommonUtils.checkNull(map.get("startdate"));// 开始时间
		String endDate = CommonUtils.checkNull(map.get("endDate"));// 结束时间
		String finType = CommonUtils.checkNull(map.get("finType"));// 资金类型
		String yieldly = CommonUtils.checkNull(map.get("yieldly"));// 产地
		String orderType = CommonUtils.checkNull(map.get("orderType"));// 订单类型
		String orderStatus = CommonUtils.checkNull(map.get("orderStatus"));// 订单状态
		String reqOrderStatus = CommonUtils.checkNull(map.get("reqOrderStatus"));
		String dealerId = CommonUtils.checkNull(map.get("dealerId"));// 经销商ID
		String poseId = CommonUtils.checkNull(map.get("poseId"));
		String expOrderType = CommonUtils.checkNull(map.get("expOrderType"));
		
		List<Object> params = new ArrayList<Object>();
		StringBuffer sbSql = new StringBuffer();
		
		sbSql.append("-- 经销商提车单处理查询\n");
		sbSql.append("SELECT C.DEALER_NAME, -- 订货经销商名称\n");
		sbSql.append("       D.DEALER_NAME REC_DEALER_NAME, -- 收货经销商名称\n");
		sbSql.append("       A.ORDER_ID,\n");
		sbSql.append("       A.ORDER_NO,\n");
		sbSql.append("       TO_CHAR(A.RAISE_DATE, 'YYYY-MM-DD HH24:MI:SS') RAISE_DATE,\n");
		sbSql.append("       A.ORDER_TYPE, -- 订单类型\n");
		sbSql.append("       A.FUND_TYPE_ID, -- 资金类型\n");
		sbSql.append("       A.ORDER_YF_PRICE, -- 订单应付金额\n");
		sbSql.append("       A.SUB_NUM,\n");
		sbSql.append("       A.ORDER_STATUS\n");
		sbSql.append("  FROM TT_VS_ORDER A, TM_DEALER C, TM_DEALER D\n");
		sbSql.append(" WHERE A.DEALER_ID = C.DEALER_ID\n");
		sbSql.append("   AND A.REC_DEALER_ID = D.DEALER_ID");
		
		sbSql.append(MaterialGroupManagerDao.getDealerBusinessSql("A.AREA_ID", poseId) + "\n");
		
		if (!expOrderType.equals(""))
		{
			sbSql.append("AND A.ORDER_TYPE <> ?\n");
			params.add(expOrderType);
		}
		
		if (orderNo != null && !"".equals(orderNo))
		{
			sbSql.append("AND A.ORDER_NO LIKE ?\n");
			params.add("%" + orderNo + "%");
		}
		
		if (startdate != null && !"".equals(startdate))
		{
			sbSql.append("AND TRUNC(A.CREATE_DATE) >= TO_DATE(?,'yyyy-MM-dd')\n");
			params.add(startdate);
		}
		
		if (endDate != null && !"".equals(endDate))
		{
			sbSql.append("AND TRUNC(A.CREATE_DATE) <= TO_DATE(?,'yyyy-MM-dd')\n");
			params.add(endDate);
		}
		
		if (yieldly != null && !"".equals(yieldly))
		{
			sbSql.append("AND A.AREA_ID = ?\n");
			params.add(yieldly);
		}
		
		if (finType != null && !"".equals(finType))
		{
			sbSql.append("AND A.FUND_TYPE_ID = ?\n");
			params.add(finType);
		}
		
		if (orderType != null && !"".equals(orderType))
		{
			sbSql.append("AND A.ORDER_TYPE = ?\n");
			params.add(orderType);
		}
		
		if (orderStatus != null && !"".equals(orderStatus))
		{
			sbSql.append("AND A.ORDER_STATUS = ?\n");
			params.add(orderStatus);
		}
		
		if (!reqOrderStatus.equals(""))
		{
			sbSql.append("AND A.ORDER_STATUS IN (" + StringUtil.getSqlCondition(reqOrderStatus, params) + ")\n");
		}
		
		if (dealerId != null && !"".equals(dealerId))
		{
			sbSql.append("AND A.DEALER_ID = ?\n");
			params.add(dealerId);
		}
		
		sbSql.append(" ORDER BY A.CREATE_DATE DESC");
		
		return pageQuery(sbSql.toString(), params, getFunName(), pageSize, curPage);
	}
	
	/**
	 * 方法描述 ： 获取经销商的物料信息 加入限制条件为订单<br/>
	 * 
	 * @param dealerId
	 *            经销商id
	 * @param yieldly
	 *            产地
	 * @param finType
	 *            资金账户类型
	 * @param materialCode
	 *            物料编码
	 * @return
	 * @throws RuntimeException
	 * @author wangsongwei
	 */
	public Map<String, Object> getDealerMaterialResourceSaved(String dealerId, String yieldly, String finType, String materialId, String orderId) throws RuntimeException
	{
		
		StringBuffer sbSql = new StringBuffer();
		
		sbSql.append("-- 查询物料信息																				\n");
		sbSql.append("SELECT																					\n");
		sbSql.append("        TVM.MATERIAL_ID,          -- 物料ID												\n");
		sbSql.append("        TVM.MATERIAL_CODE,        -- 物料CODE												\n");
		sbSql.append("        TVM.MATERIAL_NAME,        -- 物料名称												\n");
		sbSql.append("        TVM.COLOR_NAME,           -- 物料颜色												\n");
		sbSql.append("        TVO.SUB_NUM,          	-- 提交数量												\n");
		sbSql.append("        NVL(TMP1.PRO_PRICE, 0) + TVM.VHCL_PRICE AMOUNT,      -- 物料的标准价格 + 省份调整价格  \n");
		sbSql.append("        NVL(TMP2.DIS_VALUE, 0) + NVL(TMP3.DIS_VALUE, 0) DIS_VALUE,     -- 折扣率			\n");
		sbSql.append("        VMG.SERIES_NAME,          -- 车系													\n");
		sbSql.append("        VMG.MODEL_NAME,           -- 车型													\n");
		sbSql.append("        NVL(VVOR.RESOURCE_AMOUNT, 0) RESOURCE_AMOUNT,   -- 物料可用数量即车辆的数量			\n");
		sbSql.append("        -- 可用数量统计																		\n");
		sbSql.append("        CASE WHEN NVL(VVOR.RESOURCE_AMOUNT, 0) > 50										\n");
		sbSql.append("             THEN '有'																		\n");
		sbSql.append("             ELSE TO_CHAR(NVL(VVOR.RESOURCE_AMOUNT, 0))									\n");
		sbSql.append("        END RESOURCE_AMOUNT_STATUS														\n");
		sbSql.append("FROM																						\n");
		sbSql.append("        TT_VS_ORDER   TVO,																\n");
		sbSql.append("        TT_VS_ORDER_DETAIL TVOD,  														\n");
		sbSql.append("        TM_VHCL_MATERIAL TVM,																\n");
		sbSql.append("        TM_VHCL_MATERIAL_GROUP_R TVMG,													\n");
		sbSql.append("        VW_MATERIAL_GROUP VMG,															\n");
		sbSql.append("        VW_VS_ORDER_RESOURCE VVOR,														\n");
		sbSql.append("        (																					\n");
		sbSql.append("             SELECT   TR.REGION_ID,														\n");
		sbSql.append("                      TR.REGION_NAME,														\n");
		sbSql.append("                      TD.DEALER_ID,														\n");
		sbSql.append("                      TD.PROVINCE_ID,														\n");
		sbSql.append("                      TVM.MATERIAL_ID,													\n");
		sbSql.append("                      NVL(TSAP.AMOUNT, 0) PRO_PRICE										\n");
		sbSql.append("             FROM																			\n");
		sbSql.append("                      TM_VHCL_MATERIAL              TVM,									\n");
		sbSql.append("                      TM_REGION                     TR,									\n");
		sbSql.append("                      TT_SALES_AREA_PRICE           TSAP,									\n");
		sbSql.append("                      TM_DEALER                     TD									\n");
		sbSql.append("             WHERE																		\n");
		sbSql.append("                      TR.REGION_ID = TSAP.AREA_ID(+)										\n");
		sbSql.append("                      AND TR.REGION_TYPE = 10541002										\n");
		sbSql.append("                      AND TD.PROVINCE_ID = TR.REGION_CODE									\n");
		sbSql.append("        ) TMP1,       -- 省份价格调整表														\n");
		sbSql.append("        (																					\n");
		sbSql.append("             SELECT   TVM.MATERIAL_ID,													\n");
		sbSql.append("                      VMG.SERIES_ID,  -- 车系ID											\n");
		sbSql.append("                      TSD.YIELDLY,    -- 产地												\n");
		sbSql.append("                      TSD.DIS_VALUE   -- 折扣率											\n");
		sbSql.append("             FROM																			\n");
		sbSql.append("                      TM_VHCL_MATERIAL              TVM,									\n");
		sbSql.append("                      TM_VHCL_MATERIAL_GROUP_R      TVMG,									\n");
		sbSql.append("                      VW_MATERIAL_GROUP             VMG,									\n");
		sbSql.append("                      TT_SALES_DIS                  TSD									\n");
		sbSql.append("             WHERE																		\n");
		sbSql.append("                      TVM.MATERIAL_ID = TVMG.MATERIAL_ID									\n");
		sbSql.append("                      AND TVMG.GROUP_ID = VMG.PACKAGE_ID									\n");
		sbSql.append("                      AND VMG.SERIES_ID = TSD.GROUP_ID									\n");
		sbSql.append("       ) TMP2,        -- 车系折扣率调整表													\n");
		sbSql.append("       (																					\n");
		sbSql.append("             SELECT   TVM.MATERIAL_ID,													\n");
		sbSql.append("                      TSFD.DIS_VALUE,   -- 折扣率											\n");
		sbSql.append("                      TSFD.YIELDLY,     -- 产地											\n");
		sbSql.append("                      TSFD.FIN_TYPE     -- 资金账户类型										\n");
		sbSql.append("             FROM																			\n");
		sbSql.append("                      TM_VHCL_MATERIAL       TVM,											\n");
		sbSql.append("                      TT_SALES_FIN_DIS       TSFD											\n");
		sbSql.append("       ) TMP3         -- 资金类型折扣率														\n");
		sbSql.append("WHERE																						\n");
		sbSql.append("       TVM.MATERIAL_ID = TMP1.MATERIAL_ID(+)												\n");
		sbSql.append("       AND TVM.MATERIAL_ID = TMP2.MATERIAL_ID(+)											\n");
		sbSql.append("       AND TVM.MATERIAL_ID = TMP3.MATERIAL_ID(+)											\n");
		sbSql.append("       AND TVM.MATERIAL_ID = TVMG.MATERIAL_ID												\n");
		sbSql.append("       AND TVM.MATERIAL_ID = VVOR.MATERIAL_ID(+)											\n");
		sbSql.append("       AND TVMG.GROUP_ID = VMG.PACKAGE_ID													\n");
		sbSql.append("       AND TMP2.YIELDLY = TMP3.YIELDLY													\n");
		sbSql.append("       AND TVM.MATERIAL_ID = TVOD.MATERIAL_ID(+)											\n");
		sbSql.append("       AND TVOD.ORDER_ID = TVO.ORDER_ID													\n");
		sbSql.append("       AND TVM.MATERIAL_ID = " + materialId + "											\n");
		sbSql.append("       AND TMP1.DEALER_ID = " + dealerId + "												\n");
		sbSql.append("       AND TMP2.YIELDLY = " + yieldly + "													\n");
		sbSql.append("       AND TMP3.FIN_TYPE = " + finType + "												\n");
		sbSql.append("		 AND TVO.ORDER_ID = " + orderId + "													\n");
		
		Map<String, Object> map = null;
		
		try
		{
			map = dao.pageQueryMap(sbSql.toString(), null, getFunName());
		}
		catch (Exception _ex)
		{
			throw new RuntimeException("查询物料数据失败!");
		}
		
		return map;
		
	}
	
	/**
	 * 方法描述 :
	 * 
	 * @param dealerId
	 * @param string
	 * @param string2
	 * @author wangsongwei
	 */
	public List<Map<String, Object>> getDealerRebateList(Long dealerId, Long yieldlyId, Long finType, String useType)
	{
		StringBuffer sql = new StringBuffer();
		
		sql.append("SELECT ");
		sql.append("       T.REB_ID,\n");
		sql.append("       T.REB_NO,\n");
		sql.append("       T.USE_TYPE,\n");
		sql.append("       T.TOTAL_AMOUNT,\n");
		sql.append("       NVL(T.TOTAL_AMOUNT,0) - NVL(T.USED_AMOUNT,0) REB_AMOUNT ,\n");
		sql.append("       T.USED_AMOUNT,\n");
		sql.append("       T.Create_Date,\n");
		sql.append("       T.Remark\n");
		sql.append(" FROM TT_SALES_REBATE T\n");
		sql.append(" WHERE 1 = 1");
		
		if (dealerId != null)
		{
			sql.append(" AND T.DEALER_ID = " + dealerId);
		}
		
		if (useType != null)
		{
			sql.append(" AND T.USE_TYPE = " + useType);
		}
		
		List<Map<String, Object>> ps = dao.pageQuery(sql.toString(), null, getFunName());
		
		return ps;
	}
	
	/**
	 * 方法描述 ： 通过返利的主键ID获取返利信息<br/>
	 * 
	 * @param ids
	 *            返利表主键 - xx,xx,xx
	 * @return List&ltMap&ltString, Object&gt&gt
	 * @author wangsongwei
	 */
	public List<Map<String, Object>> getDealerRebateList(String ids)
	{
		
		StringBuffer sql = new StringBuffer();
		
		sql.append("SELECT ");
		sql.append("       T.REB_ID,\n");
		sql.append("       T.REB_NO,\n");
		sql.append("	   T.DIS_ITEM,\n");
		sql.append("       T.USE_TYPE,\n");
		sql.append("       T.YIELDLY,\n");
		sql.append("	   NVL(T.VER,0) VER,								\n");
		sql.append("       NVL(T.TOTAL_AMOUNT,0) TOTAL_AMOUNT,\n");
		sql.append("       NVL(T.TOTAL_AMOUNT,0) - NVL(T.USED_AMOUNT,0) REB_AMOUNT ,\n");
		sql.append("       NVL(T.USED_AMOUNT,0) USED_AMOUNT,\n");
		sql.append("       T.CREATE_DATE,\n");
		sql.append("       T.REMARK\n");
		sql.append(" FROM TT_SALES_REBATE T\n");
		sql.append(" WHERE 1 = 1");
		
		if (null != ids && !"".equals(ids))
		{
			String[] array = ids.split(",");
			sql.append("   AND T.REB_ID IN ( \n");
			if (array.length > 0)
			{
				for (int i = 0; i < array.length; i++)
				{
					sql.append("'" + array[i] + "'");
					if (i != array.length - 1)
					{
						sql.append(",");
					}
				}
			}
			else
			{
				sql.append("''");// 放空置，防止in里面报错
			}
			sql.append(")\n");
			
		}
		
		List<Map<String, Object>> ps = dao.pageQuery(sql.toString(), null, getFunName());
		
		return ps;
	}
	
	/**
	 * 方法描述 ： 获取订单的下个状态<br/>
	 * 
	 * @param map
	 * @return
	 * @author wangsongwei
	 */
	public Integer getNextOrderStatus(String orderId) throws Exception
	{
		
		Integer status = -1;
		
		try
		{
			Map<String, Object> orderMap = queryDao.getOrderSubmissionInfo(orderId);
			
			String curstatus = orderMap.get("ORDER_STATUS").toString();
			String orderType = orderMap.get("ORDER_TYPE").toString();
			String finType = orderMap.get("FUND_TYPE_ID").toString();
			
			if (curstatus.equals(Constant.ORDER_STATUS_02.toString()))
			{
				status = Constant.ORDER_STATUS_03;
			}
			else if (curstatus.equals(Constant.ORDER_STATUS_03.toString()))
			{
				status = Constant.ORDER_STATUS_04;
			}
			else if (curstatus.equals(Constant.ORDER_STATUS_04.toString()))
			{
				status = Constant.ORDER_STATUS_05;
			}
			else if (curstatus.equals(Constant.ORDER_STATUS_05.toString()))
			{
			}
			else if (curstatus.equals(Constant.ORDER_STATUS_06.toString()))
			{
				status = Constant.ORDER_STATUS_07;
			}
			else if (curstatus.equals(Constant.ORDER_STATUS_07.toString()))
			{
				status = Constant.ORDER_STATUS_08;
			}
			else if (curstatus.equals(Constant.ORDER_STATUS_08.toString()))
			{
				status = Constant.ORDER_STATUS_09;
			}
			else if (curstatus.equals(Constant.ORDER_STATUS_12.toString()))
			{
				status = Constant.ORDER_STATUS_02;
			}
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			throw new NullPointerException("获取订单之后的状态失败!");
		}
		
		return status;
	}
	
	/**
	 * 方法描述 ： 检查是否存在负返利的情况<br/>
	 * 
	 * @param rebids
	 * @return
	 * @author wangsongwei
	 * @param rebatePrice
	 */
	public boolean getRebateCheckResult(String[] rebids, String[] useAmount, String dealerId, Double rebatePrice, String yieldly) throws Exception
	{
		
		StringBuffer sbSql = new StringBuffer();
		List<Object> params = new ArrayList<Object>();
		
		if (rebids == null)
			return true;
		if (rebids.length == 0)
			return true;
		if (rebatePrice <= 0.0)
		{
			
			sbSql.append("SELECT 1\n");
			sbSql.append("  FROM (SELECT NVL(AUDIT_DATE, TO_DATE('1799-01-01', 'YYYY-MM-DD')) AUDIT_DATE, USE_TYPE\n");
			sbSql.append("          FROM TT_SALES_REBATE\n");
			sbSql.append("         WHERE REB_ID IN (" + queryDao.getSqlBuffer(rebids, params) + ")) A,\n");
			sbSql.append("       (\n");
			sbSql.append("\n");
			sbSql.append("        SELECT NVL(AUDIT_DATE, TO_DATE('1799-01-01', 'YYYY-MM-DD')) AUDIT_DATE, USE_TYPE\n");
			sbSql.append("          FROM TT_SALES_REBATE\n");
			sbSql.append("         WHERE DEALER_ID IN (SELECT DEALER_ID\n");
			sbSql.append("                               FROM TT_SALES_REBATE\n");
			sbSql.append("                              WHERE REB_ID IN (" + queryDao.getSqlBuffer(rebids, params) + ")\n");
			sbSql.append("                              GROUP BY DEALER_ID)\n");
			sbSql.append("           AND REB_ID NOT IN (" + queryDao.getSqlBuffer(rebids, params) + ")\n");
			sbSql.append("           AND STATUS = 99301003\n");
			sbSql.append("			 AND YIELDLY = ?\n");
			params.add(yieldly);
			sbSql.append("           AND NVL(TOTAL_AMOUNT, 0) - NVL(USED_AMOUNT, 0) < 0) B\n");
			sbSql.append(" WHERE A.AUDIT_DATE > B.AUDIT_DATE AND A.USE_TYPE = B.USE_TYPE");
			
			Map<String, Object> mapList = dao.pageQueryMap(sbSql.toString(), params, getFunName());
			
			if (mapList != null)
			{
				throw new RuntimeException("返利的使用请按照返利审核通过的时间先后顺序使用!");
			}
			
			return true;
		}
		
		// 检查是否存在负返利 - 后冲
		sbSql.delete(0, sbSql.length());
		params.clear();
		sbSql.append("SELECT      TSR.REB_ID,  NVL(TSR.TOTAL_AMOUNT,0) - NVL(TSR.USED_AMOUNT,0) REB_AMOUNT\n");
		sbSql.append("FROM\n");
		sbSql.append("            TT_SALES_REBATE TSR\n");
		sbSql.append("WHERE\n");
		sbSql.append("			  TSR.DEALER_ID = ?\n");
		params.add(dealerId);
		sbSql.append("			  AND TSR.YIELDLY = ?\n");
		params.add(yieldly);
		sbSql.append("			  AND TSR.REB_ID NOT IN (" + queryDao.getSqlBuffer(rebids, params) + ")\n");
		sbSql.append("            AND (NVL(TSR.TOTAL_AMOUNT,0) - NVL(TSR.USED_AMOUNT,0)) < 0 AND TSR.STATUS = " + Constant.REBATE_STATUS_03 + "");
		
		boolean flag = false;
		
		List<Map<String, Object>> list = dao.pageQuery(sbSql.toString(), params, getFunName());
		
		if (list.size() == 0)
		{
			flag = true;
			
			// 判断使用的4笔返利中有没有负返利并且是否已经使用完
			sbSql.delete(0, sbSql.length());
			params.clear();
			
			sbSql.append("SELECT TSR.REB_ID,NVL(TSR.TOTAL_AMOUNT,0)-NVL(TSR.USED_AMOUNT,0) REB_AMOUNT FROM TT_SALES_REBATE TSR WHERE TSR.REB_ID IN (" + queryDao.getSqlBuffer(rebids, params)
							+ ") AND (NVL(TSR.TOTAL_AMOUNT,0) - NVL(TSR.USED_AMOUNT,0) <= 0)");
			
			List<Map<String, Object>> rebateList = dao.pageQuery(sbSql.toString(), params, getFunName());
			
			for (Map<String, Object> rebateMap : rebateList)
			{
				String rebId = rebateMap.get("REB_ID").toString();
				Double rebAmount = Double.valueOf(rebateMap.get("REB_AMOUNT").toString());
				for (int i = 0; i < rebids.length; i++)
				{
					if (rebids[i].equals(rebId) && !Double.valueOf(useAmount[i]).equals(rebAmount))
					{
						flag = false;
					}
				}
			}
		}
		else
		{
			flag = false;
		}
		
		/*
		 * if (list.size() > 0) { for (Map<String, Object> map : list) { String
		 * rebId = map.get("REB_ID").toString(); String rebAmount =
		 * map.get("REB_AMOUNT").toString(); for (int i = 0; i < rebids.length;
		 * i++) { // 如果正在使用的返利中在存在系统中已有的负返利，则开始比较负返利的金额是否已经使用完 if
		 * (rebids[i].equals(rebId) && !"".equals(useAmount[i]) &&
		 * !"0".equals(useAmount[i])) { if
		 * (Double.valueOf(rebAmount).doubleValue() !=
		 * Double.valueOf(useAmount[i]).doubleValue()) { // 负返利没有使用完 flag =
		 * false; } else { // 负返利已经使用完，但是要看返利是否按照了正常的时间顺序在使用 sbSql.delete(0,
		 * sbSql.length()); sbSql.append("SELECT 1\n"); sbSql.append(
		 * "  FROM (SELECT NVL(AUDIT_DATE, TO_DATE('1799-01-01', 'YYYY-MM-DD')) AUDIT_DATE, USE_TYPE\n"
		 * ); sbSql.append("          FROM TT_SALES_REBATE\n");
		 * sbSql.append("         WHERE REB_ID IN (" +
		 * queryDao.getSqlBuffer(rebids, params) + ")) A,\n");
		 * sbSql.append("       (\n"); sbSql.append("\n"); sbSql.append(
		 * "        SELECT NVL(AUDIT_DATE, TO_DATE('1799-01-01', 'YYYY-MM-DD')) AUDIT_DATE, USE_TYPE\n"
		 * ); sbSql.append("          FROM TT_SALES_REBATE\n");
		 * sbSql.append("         WHERE DEALER_ID IN (SELECT DEALER_ID\n");
		 * sbSql
		 * .append("                               FROM TT_SALES_REBATE\n");
		 * sbSql.append("                              WHERE REB_ID IN (" +
		 * queryDao.getSqlBuffer(rebids, params) + ")\n");
		 * sbSql.append("                              GROUP BY DEALER_ID)\n");
		 * sbSql.append("           AND REB_ID NOT IN (" +
		 * queryDao.getSqlBuffer(rebids, params) + ")\n");
		 * sbSql.append("           AND STATUS = 99301003\n");
		 * sbSql.append("			 AND YIELDLY = " + yieldly + "\n"); sbSql.append(
		 * "           AND NVL(TOTAL_AMOUNT, 0) - NVL(USED_AMOUNT, 0) <> 0) B\n"
		 * ); sbSql.append(
		 * " WHERE A.AUDIT_DATE > B.AUDIT_DATE AND A.USE_TYPE = B.USE_TYPE");
		 * Map<String, Object> mapList = dao.pageQueryMap(sbSql.toString(),
		 * params, getFunName()); if (mapList != null) { throw new
		 * RuntimeException("返利的使用请按照返利审核通过的时间先后顺序使用!"); } flag = true; break; }
		 * } } } } else { flag = true; }
		 */
		
		return flag;
	}
	
	/**
	 * 方法描述 ： 通过返利的主键ID获取返利信息<br/>
	 * 
	 * @param ids
	 *            返利表主键 - xx,xx,xx
	 * @return List&ltMap&ltString, Object&gt&gt
	 * @author wangsongwei
	 */
	public PageResult<Map<String, Object>> getSalesRebateList(Long dealerId, String rebid, Long yieldly, String useType, String rebNo, Integer curPage, Integer pageSize)
	{
		
		StringBuffer sql = new StringBuffer();
		
		sql.append("SELECT T.REB_ID,									\n");
		sql.append("       T.REB_NO,									\n");
		sql.append("       T.DIS_ITEM,									\n");
		// sql.append("       T.EVIDENCE_NO,								\n");
		sql.append("	   NVL(AUDIT_DATE, TO_DATE('1799-01-01', 'YYYY-MM-DD')) AUDIT_DATE, \n");
		sql.append("	   T.YIELDLY, 									\n");
		sql.append("	   T.VER,								\n");
		sql.append("       DECODE(T.USE_TYPE, " + Constant.WRITE_DOWNS_WAY_STATUS_01 + ", '前冲', '后冲') USE_TYPE, ---冲减方式		\n");
		sql.append("       NVL(T.TOTAL_AMOUNT,0) - NVL(T.USED_AMOUNT,0) REB_AMOUNT,								\n");
		sql.append("       NVL(T.USED_AMOUNT,0) USED_AMOUNT,								\n");
		sql.append("       T.REMARK,									\n");
		sql.append("       T.TOTAL_AMOUNT								\n");
		sql.append("  FROM TT_SALES_REBATE T							\n");
		sql.append(" WHERE T.STATUS =  " + Constant.REBATE_STATUS_03 + "\n");
		sql.append(" 	   AND ABS(NVL(T.TOTAL_AMOUNT,0) - NVL(T.USED_AMOUNT,0)) > 0	\n");
		
		if (dealerId != null)
		{
			sql.append(" AND T.DEALER_ID = " + dealerId + "\n");
		}
		
		if (null != rebid && !"".equals(rebid))
		{
			String[] array = rebid.split(",");
			
			if (array.length > 0)
			{
				sql.append("   AND T.REB_ID NOT IN ( \n");
				for (int i = 0; i < array.length; i++)
				{
					sql.append("'" + array[i] + "'");
					if (i != array.length - 1)
					{
						sql.append(",");
					}
				}
				sql.append(")\n");
			}
			else
			{
				sql.append("''");// 放空置，防止in里面报错
			}
		}
		
		if (yieldly != null && !"".equals(yieldly))
		{
			sql.append("\n AND T.YIELDLY = '" + yieldly + "' \n");
		}
		
		if (useType != null && !"".equals(useType))
		{
			sql.append("\n AND T.USE_TYPE = '" + useType + "' \n");
		}
		
		if (rebNo != null && !"".equals(rebNo))
		{
			sql.append("\n AND T.REB_NO = '" + rebNo + "' \n");
		}
		
		sql.append("ORDER BY T.USE_TYPE, NVL(AUDIT_DATE, TO_DATE('1799-01-01', 'YYYY-MM-DD')) ASC ");
		
		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
		return ps;
	}
	
	/**
	 * 方法描述 ： 回滚经销商资金账户<br/>
	 * 
	 * @param orderId
	 * @param userId
	 * @author wangsongwei
	 * @param userId
	 */
	public void rollbackDealerAccount(Long orderId, Long accType, Long userId)
	{
		List<Object> params = new ArrayList<Object>();
		StringBuffer sbSql = new StringBuffer();
		
		if (accType.intValue() == Constant.ACCOUNT_TYPE_03.intValue())
		{
			sbSql.append("UPDATE TT_SALES_OTHER_ACCEPT T\n");
			sbSql.append("   SET T.FREEZE_AMOUNT = NVL(T.FREEZE_AMOUNT, 0) -\n");
			sbSql.append("                         (SELECT TSOA.USE_AMOUNT\n");
			sbSql.append("                            FROM TT_SALES_ORDER_ACC TSOA\n");
			sbSql.append("                           WHERE TSOA.ORDER_ID = ?)\n");
			sbSql.append("WHERE T.CRE_DETAIL_ID IN (SELECT A.THREE_CREDIT FROM TT_VS_ORDER A WHERE A.ORDER_ID = ?)");
			
			params.add(orderId);
			params.add(orderId);
			
			dao.update(sbSql.toString(), params);
		}
		else
		{
			// 先回写资金数据到经销商资金账户表
			sbSql.append("UPDATE TT_SALES_FIN_ACC T\n");
			sbSql.append("   SET T.AMOUNT        = NVL(T.AMOUNT, 0) +\n");
			sbSql.append("                         NVL((SELECT NVL(TSOA.USE_AMOUNT, 0)\n");
			sbSql.append("                               FROM TT_SALES_ORDER_ACC TSOA\n");
			sbSql.append("                              WHERE TSOA.ACC_ID = T.ACC_ID\n");
			sbSql.append("                                AND TSOA.ORDER_ID = ?),\n");
			sbSql.append("                             0),\n");
			sbSql.append("       T.FREEZE_AMOUNT = NVL(T.FREEZE_AMOUNT, 0) -\n");
			sbSql.append("                         NVL((SELECT NVL(TSOA.USE_AMOUNT, 0)\n");
			sbSql.append("                               FROM TT_SALES_ORDER_ACC TSOA\n");
			sbSql.append("                              WHERE TSOA.ACC_ID = T.ACC_ID\n");
			sbSql.append("                                AND TSOA.ORDER_ID = ?),\n");
			sbSql.append("                             0),\n");
			sbSql.append("       T.VER           = NVL(T.VER, 0) + 1,\n");
			sbSql.append("       T.UPDATE_BY     = ?,\n");
			sbSql.append("       T.UPDATE_DATE   = SYSDATE\n");
			sbSql.append(" WHERE T.ACC_ID IN (SELECT TSOA.ACC_ID\n");
			sbSql.append("                     FROM TT_SALES_ORDER_ACC TSOA\n");
			sbSql.append("                    WHERE TSOA.ORDER_ID = ?)");
			
			params.add(orderId);
			params.add(orderId);
			params.add(userId);
			params.add(orderId);
			dao.update(sbSql.toString(), null);
		}
		
		sbSql.delete(0, sbSql.length());
		
		params.clear();
		params.add(orderId);
		// 将资金账户的使用明细状态设置为已取消
		sbSql.append("DELETE TT_SALES_ORDER_ACC T WHERE T.ORDER_ID = ?");
		
		dao.update(sbSql.toString(), params);
	}
	
	/**
	 * 方法描述 ： 回滚经销商冻结资金账户<br/>
	 * 
	 * @param orderId
	 * @param userId
	 * @author wangsongwei
	 */
	public void rollbackDealerAccountFreeze(Long orderId, Long userId)
	{
		StringBuffer sbSql = new StringBuffer();
		
		// 先回写资金数据到经销商资金账户表
		
		sbSql.append("UPDATE TT_SALES_FIN_ACC T							\n");
		sbSql.append("SET T.AMOUNT = NVL(T.AMOUNT,0) + (				\n");
		sbSql.append("      SELECT NVL(TSOA.USE_AMOUNT,0)				\n");
		sbSql.append("      FROM TT_SALES_ORDER_ACC TSOA				\n");
		sbSql.append("      WHERE										\n");
		sbSql.append("             TSOA.ACC_ID = T.ACC_ID				\n");
		sbSql.append("             AND									\n");
		sbSql.append("             TSOA.ORDER_ID = " + orderId + "		\n");
		sbSql.append("			   AND (TSOA.USE_STATUS = " + Constant.ORDER_ACCOUNT_STATUS_02);
		sbSql.append(" 			   		OR   TSOA.USE_STATUS = " + Constant.ORDER_ACCOUNT_STATUS_03 + ")");
		sbSql.append("),												\n");
		sbSql.append("T.FREEZE_AMOUNT = NVL(T.FREEZE_AMOUNT,0) + (	\n");
		sbSql.append("      SELECT NVL(TSOA.USE_AMOUNT,0)				\n");
		sbSql.append("      FROM TT_SALES_ORDER_ACC TSOA				\n");
		sbSql.append("      WHERE										\n");
		sbSql.append("             TSOA.ACC_ID = T.ACC_ID				\n");
		sbSql.append("             AND									\n");
		sbSql.append("             TSOA.ORDER_ID = " + orderId + "		\n");
		sbSql.append("			   AND (TSOA.USE_STATUS = " + Constant.ORDER_ACCOUNT_STATUS_02);
		sbSql.append(" 			   		OR   TSOA.USE_STATUS = " + Constant.ORDER_ACCOUNT_STATUS_03 + ")");
		sbSql.append("),												\n");
		sbSql.append("T.VER = NVL(T.VER,0) + 1,							\n");
		sbSql.append("T.UPDATE_BY = " + userId + ",						\n");
		sbSql.append("T.UPDATE_DATE = SYSDATE							\n");
		sbSql.append("WHERE												\n");
		sbSql.append("      T.ACC_ID = (								\n");
		sbSql.append("           SELECT TSOA.ACC_ID						\n");
		sbSql.append("           FROM TT_SALES_ORDER_ACC TSOA			\n");
		sbSql.append("           WHERE TSOA.ORDER_ID = " + orderId + "  \n");
		sbSql.append("			   AND (TSOA.USE_STATUS = " + Constant.ORDER_ACCOUNT_STATUS_02);
		sbSql.append(" 			   		OR   TSOA.USE_STATUS = " + Constant.ORDER_ACCOUNT_STATUS_03 + ")");
		sbSql.append("      )");
		
		dao.update(sbSql.toString(), null);
		
		sbSql.delete(0, sbSql.length());
		
		// 将资金账户的使用明细状态设置为已取消
		sbSql.append("UPDATE TT_SALES_ORDER_ACC T SET T.USE_STATUS = " + Constant.ORDER_ACCOUNT_STATUS_01 + " WHERE T.ORDER_ID = " + orderId);
		
		dao.update(sbSql.toString(), null);
	}
	
	/**
	 * 方法描述 ： 回滚订单承诺明细使用表数据<br/>
	 * 
	 * @param orderId
	 *            订单ID
	 * @param userId
	 *            用户ID
	 * @author wangsongwei
	 */
	private void rollbackOrderPromiseUse(Long orderId, Long userId, Integer status) throws Exception
	{
		List<Object> params = new ArrayList<Object>();
		params.add(orderId);
		
		StringBuffer sbSql = new StringBuffer();
		
		sbSql.append("SELECT 1\n");
		sbSql.append("  FROM TT_PROM_ORD_USE    T1,\n");
		sbSql.append("       TT_PROMISE_DEDUCT  T2,\n");
		sbSql.append("       TT_VS_ORDER        TVO,\n");
		sbSql.append("       TT_VS_ORDER_DETAIL TVOD\n");
		sbSql.append(" WHERE T1.USE_ID = T2.USE_ID\n");
		sbSql.append("   AND T1.ORD_DET_ID = TVOD.DETAIL_ID\n");
		sbSql.append("   AND TVOD.ORDER_ID = TVO.ORDER_ID\n");
		sbSql.append("   AND TVO.ORDER_ID = ?");
		
		List<Map<String, Object>> list = dao.pageQuery(sbSql.toString(), params, getFunName());
		
		if (list.size() > 0)
		{
			throw new RuntimeException("承诺已经扣款,不允许驳回或作废订单!");
		}
		
		// 回写承诺主表 - 统计承诺明细的数据回写到承诺主表
		sbSql.delete(0, sbSql.length());
		
		sbSql.append("SELECT za_concat(AA.DETAIL_ID) DETAIL_ID\n");
		sbSql.append("  FROM TT_PROM_ORD_USE AA, TT_VS_ORDER_DETAIL BB\n");
		sbSql.append(" WHERE AA.ORD_DET_ID = BB.DETAIL_ID\n");
		sbSql.append("   AND BB.ORDER_ID = ?");
		
		Map<String, Object> dMap = dao.pageQueryMap(sbSql.toString(), params, getFunName());
		String detailSplit = CommonUtils.checkNull(dMap.get("DETAIL_ID"));
		
		sbSql.delete(0, sbSql.length());
		
		sbSql.append("DELETE FROM TT_PROM_ORD_USE T\n");
		sbSql.append("WHERE \n");
		sbSql.append("       T.ORD_DET_ID IN\n");
		sbSql.append("       (SELECT TVOD.DETAIL_ID\n");
		sbSql.append("         FROM TT_VS_ORDER_DETAIL TVOD, TT_VS_ORDER TVO\n");
		sbSql.append("        WHERE TVOD.ORDER_ID = TVO.ORDER_ID\n");
		sbSql.append("         AND TVO.ORDER_ID = ?) \n");
		
		dao.delete(sbSql.toString(), params);
		
		if (status.intValue() == Constant.ORDER_STATUS_03.intValue() || status.intValue() == Constant.ORDER_STATUS_13.intValue())
		{
			params.clear();
			params.add(userId);
			
			sbSql.delete(0, sbSql.length());
			
			sbSql.append("UPDATE TT_SALES_PROMISE_DET T\n");
			sbSql.append("   SET T.USE_NUM     = NVL((SELECT SUM(NVL(TPOU.USE_NUM, 0))\n");
			sbSql.append("                          FROM TT_PROM_ORD_USE TPOU\n");
			sbSql.append("                         WHERE TPOU.DETAIL_ID = T.DETAIL_ID),0),\n");
			sbSql.append("       T.UPDATE_BY   = ?,\n");
			sbSql.append("       T.UPDATE_DATE = SYSDATE\n");
			sbSql.append(" WHERE T.DETAIL_ID IN (" + detailSplit + ")\n");
			
			dao.update(sbSql.toString(), params);
			
			sbSql.delete(0, sbSql.length());
			
			sbSql.append("UPDATE TT_SALES_PROMISE T\n");
			sbSql.append("   SET T.USE_NUM = (SELECT SUM(NVL(TSPD.USE_NUM, 0))\n");
			sbSql.append("                      FROM TT_SALES_PROMISE_DET TSPD\n");
			sbSql.append("                     WHERE TSPD.PRO_ID = T.PRO_ID),\n");
			sbSql.append("       T.UPDATE_BY   = ?,\n");
			sbSql.append("       T.UPDATE_DATE = SYSDATE\n");
			sbSql.append(" WHERE 1 = 1\n");
			sbSql.append("   AND EXISTS (SELECT 1\n");
			sbSql.append("          FROM TT_SALES_PROMISE_DET D\n");
			sbSql.append("         WHERE D.PRO_ID = T.PRO_ID\n");
			sbSql.append("           AND D.DETAIL_ID IN (" + detailSplit + "))");
			
			dao.update(sbSql.toString(), params);
		}
	}
	
	/**
	 * 方法描述 ： 通过订单表回滚返利数据<br/>
	 * <li>只回滚已提报、资源审核完成和室主任审核完成3个状态的订单返利</li>
	 * 
	 * @param orderId
	 * @param userId
	 * @author wangsongwei
	 */
	private void rollbackOrderRebate(Long orderId, Long userId)
	{
		List<Object> params = new ArrayList<Object>();
		
		StringBuffer sbSql = new StringBuffer();
		
		/* =================================== */
		sbSql.append("SELECT za_concat(T.REB_ID) REB_ID FROM TT_SALES_ORDER_REB T WHERE T.ORDER_ID = ? ORDER BY T.REB_ID");
		params.add(orderId);
		Map<String, Object> map = dao.pageQueryMap(sbSql.toString(), params, getFunName());
		if (map.get("REB_ID") == null)
		{
			return;
		}
		String rebId = map.get("REB_ID").toString();
		
		/* =================================== */
		sbSql.delete(0, sbSql.length());
		sbSql.append("UPDATE TT_SALES_ORDER_REB T\n");
		sbSql.append("   SET T.STATUS = ?, T.UPDATE_BY = ?, T.UPDATE_DATE = SYSDATE\n");
		sbSql.append(" WHERE T.ORDER_ID = ?");
		
		params.clear();
		params.add(Constant.STATUS_DISABLE);
		params.add(userId);
		params.add(orderId);
		
		dao.update(sbSql.toString(), params);
		
		sbSql.delete(0, sbSql.length());
		params.clear();
		/* =================================== */
		sbSql.append("UPDATE TT_SALES_REBATE T\n");
		sbSql.append("   SET T.USED_AMOUNT = NVL((SELECT SUM(NVL(TSOR.USE_AMOUNT, 0))\n");
		sbSql.append("                          FROM TT_SALES_ORDER_REB TSOR\n");
		sbSql.append("                         WHERE TSOR.REB_ID = T.REB_ID\n");
		sbSql.append("                          AND  TSOR.STATUS = ?),0),\n");
		sbSql.append("		 T.VER         = NVL(T.VER, 0) + 1,");
		sbSql.append("       T.UPDATE_BY   = ?,\n");
		sbSql.append("       T.UPDATE_DATE = SYSDATE\n");
		sbSql.append(" WHERE T.REB_ID IN (" + rebId + ") \n");
		
		params.add(Constant.STATUS_ENABLE);
		params.add(userId);
		
		dao.update(sbSql.toString(), params);
	}
	
	/**
	 * 方法描述 ：回滚资源预留表数据 <br/>
	 * 
	 * @param orderId
	 * @param userId
	 * @author wangsongwei
	 */
	private void rollbackOrderReource(Long orderId, Long userId)
	{
		
		StringBuffer sbSql = new StringBuffer();
		
		sbSql.append("-- 取消订单预留的资源															\n");
		sbSql.append("DELETE FROM  TT_VS_ORDER_RESOURCE_RESERVE T									\n");
		sbSql.append("WHERE																			\n");
		sbSql.append("       T.ORDER_DETAIL_ID IN (													\n");
		sbSql.append("            SELECT															\n");
		sbSql.append("                     TVOD.DETAIL_ID											\n");
		sbSql.append("            FROM																\n");
		sbSql.append("                     TT_VS_ORDER TVO,											\n");
		sbSql.append("                     TT_VS_ORDER_DETAIL TVOD									\n");
		sbSql.append("            WHERE																\n");
		sbSql.append("                     TVO.ORDER_ID = TVOD.ORDER_ID								\n");
		sbSql.append("                     AND TVOD.DETAIL_ID = T.ORDER_DETAIL_ID					\n");
		sbSql.append("                     AND TVO.ORDER_ID =" + orderId + "						\n");
		sbSql.append("       )");
		
		dao.delete(sbSql.toString(), null);
	}
	
	/**
	 * 方法描述 ：保存审核信息 <br/>
	 * 
	 * @param orderId
	 * @param remark
	 * @param logonUser
	 * @author wangsongwei
	 */
	public void saveCheckResult(Long orderId, String remark, Integer checkStatus, Integer checkType, AclUserBean logonUser) throws Exception
	{
		
		TtVsOrderCheckPO tvocp = new TtVsOrderCheckPO();
		tvocp.setCheckId(Long.parseLong(SequenceManager.getSequence("")));
		tvocp.setCheckStatus(checkStatus);
		tvocp.setCheckType(checkType);
		tvocp.setOrderId(orderId);
		tvocp.setCheckDesc(remark);
		tvocp.setCheckOrgId(logonUser.getOrgId());
		tvocp.setCheckPositionId(logonUser.getPoseId());
		tvocp.setCheckUserId(logonUser.getUserId());
		tvocp.setCheckDate(new Date());
		tvocp.setCreateBy(logonUser.getUserId());
		tvocp.setCreateDate(new Date());
		tvocp.setUpdateBy(logonUser.getUserId());
		tvocp.setUpdateDate(new Date());
		
		dao.insert(tvocp);
	}
	
	/**
	 * 方法描述 ： 保存提车单资金账户使用明细<br/>
	 * 
	 * @param valueOf
	 * @param orderId
	 * @author wangsongwei
	 */
	private void saveDealerAccountUseDetail(Long accId, Long orderId, Long userId) throws Exception
	{
		
		TtSalesOrderAccPO tsoap = new TtSalesOrderAccPO();
		
		tsoap.setDetailId(Long.parseLong(SequenceManager.getSequence("")));
		tsoap.setAccId(Long.valueOf(accId));
		tsoap.setOrderId(orderId);
		tsoap.setOrderType(Constant.ORDER_INVOICE_TYPE_01.longValue());
		tsoap.setUseDate(new Date());
		tsoap.setUsePer(userId);
		tsoap.setUseStatus(Constant.ORDER_ACCOUNT_STATUS_02.longValue());
		tsoap.setCreateBy(userId);
		tsoap.setCreateDate(new Date());
		tsoap.setUpdateBy(userId);
		tsoap.setUpdateDate(new Date());
		
		dao.insert(tsoap);
		
	}
	
	/**
	 * 方法描述 : 保存资源审核意见
	 * 
	 * @param logonUser
	 *            操作用户
	 * @param orderId
	 *            订单ID
	 * @param invoType
	 *            开票类型
	 * @author wangsongwei
	 * @deprecated
	 */
	public void saveOrderAuditResult(AclUserBean logonUser, String orderId, String invoType, String remark)
	{
		
		TtVsOrderPO tvo_sql = new TtVsOrderPO();
		tvo_sql.setOrderId(Long.valueOf(orderId));
		
		List<?> sopList = dao.select(tvo_sql);
		if (sopList.size() > 0)
		{
			TtVsOrderPO tvo = (TtVsOrderPO) sopList.get(0);
			tvo.setOrderStatus(123);
			
			// 保存审核明细表数据
			TtVsOrderCheckPO tvocp = new TtVsOrderCheckPO();
			
			tvocp.setOrderId(Long.valueOf(orderId));
			tvocp.setCheckOrgId(logonUser.getOrgId());
			tvocp.setCheckPositionId(logonUser.getPoseId());
			tvocp.setCheckUserId(logonUser.getUserId());
			tvocp.setCheckDate(new Date());
			tvocp.setCheckStatus(0);
			tvocp.setCheckDesc(remark);
			
		}
	}
	
	/**
	 * 方法描述 ： 资源审核完成后调用该方法，重新设置资源保留的数量<br/>
	 * 
	 * @param valueOf
	 * @param userId
	 * @author wangsongwei
	 */
	private void saveOrderReourceAfterCheck(Long orderId, Long userId, String planDetailId) throws Exception
	{
		Long pdi = planDetailId == null ? null : Long.valueOf(planDetailId);
		
		StringBuffer sbSql = new StringBuffer();
		
		sbSql.append("SELECT * FROM TMP_VS_ORDER_RESOURCE_RESERVE T\n");
		sbSql.append("WHERE\n");
		sbSql.append("       T.ORDER_DETAIL_ID IN\n");
		sbSql.append("       (\n");
		sbSql.append("            SELECT\n");
		sbSql.append("                   TVOD.DETAIL_ID\n");
		sbSql.append("            FROM\n");
		sbSql.append("                   TT_VS_ORDER TVO,\n");
		sbSql.append("                   TT_VS_ORDER_DETAIL TVOD\n");
		sbSql.append("            WHERE\n");
		sbSql.append("                   TVO.ORDER_ID = TVOD.ORDER_ID\n");
		sbSql.append("                   AND\n");
		sbSql.append("                   TVO.ORDER_ID = " + orderId + "\n");
		sbSql.append("       )");
		
		List<Map<String, Object>> resTmpList = dao.pageQuery(sbSql.toString(), null, getFunName());
		
		for (int i = 0; i < resTmpList.size(); i++)
		{
			Map<String, Object> resTmp = resTmpList.get(i);
			
			TtVsOrderResourceReservePO tvorrp_sql = new TtVsOrderResourceReservePO();
			tvorrp_sql.setOrderDetailId(Long.valueOf(resTmp.get("ORDER_DETAIL_ID").toString()));
			tvorrp_sql.setMaterialId(Long.valueOf(resTmp.get("MATERIAL_ID").toString()));
			tvorrp_sql.setOrgId(Long.valueOf(resTmp.get("ORG_ID").toString()));
			
			List<?> list = dao.select(tvorrp_sql);
			if (list.size() > 0)
			{
				TtVsOrderResourceReservePO res = (TtVsOrderResourceReservePO) list.get(0);
				res.setAmount(Integer.parseInt(resTmp.get("AMOUNT").toString()));
				res.setUpdateBy(userId);
				res.setUpdateDate(new Date());
				res.setReserveStatus(Constant.RESOURCE_RESERVE_STATUS_01);
				res.setCreateId(pdi);
				tvorrp_sql.setReserveId(res.getReserveId());
				dao.update(tvorrp_sql, res);
			}
			else
			{
				
				TtVsOrderResourceReservePO res = new TtVsOrderResourceReservePO();
				res.setAmount(Integer.parseInt(resTmp.get("AMOUNT").toString()));
				res.setMaterialId(Long.valueOf(resTmp.get("MATERIAL_ID").toString()));
				res.setOrderDetailId(Long.valueOf(resTmp.get("ORDER_DETAIL_ID").toString()));
				res.setOrgId(Long.valueOf(resTmp.get("ORG_ID").toString()));
				res.setReserveStatus(Constant.RESOURCE_RESERVE_STATUS_01);
				res.setCreateBy(userId);
				res.setCreateDate(new Date());
				res.setUpdateBy(userId);
				res.setUpdateDate(new Date());
				res.setCreateId(pdi);
				
				res.setReserveId(Long.valueOf(SequenceManager.getSequence("")));
				dao.insert(res);
			}
		}
	}
	
	/**
	 * 方法描述 : 资源预留表缓存 <br/>
	 * 
	 * @param detailId
	 * @param materialIds
	 * @param privs
	 * @param aumont
	 * @author wangsongwei
	 */
	public void saveReserve(String detailId, String[] privs, String[] amount, String[] materialId, AclUserBean logonUser) throws Exception
	{
		// 保存之前先清空数据
		String sbSql = "DELETE TT_VS_ORDER_RESOURCE_RESERVE T WHERE T.ORDER_DETAIL_ID = " + detailId;
		
		dao.delete(sbSql, null);
		
		for (int i = 0; i < amount.length; i++)
		{
			int amount_n = amount[i] == null ? 0 : Integer.valueOf(amount[i]);
			
			if (amount_n == 0)
			{
				continue;
			}
			
			TtVsOrderResourceReservePO tvorrp = new TtVsOrderResourceReservePO();
			
			tvorrp.setAmount(amount_n);
			tvorrp.setOrderDetailId(Long.valueOf(detailId));
			tvorrp.setOrgId(Long.valueOf(privs[i]));
			tvorrp.setMaterialId(Long.valueOf(materialId[i]));
			tvorrp.setReserveStatus(Constant.RESOURCE_RESERVE_STATUS_01);
			
			tvorrp.setCreateBy(logonUser.getUserId());
			tvorrp.setCreateDate(new Date());
			tvorrp.setUpdateBy(logonUser.getUserId());
			tvorrp.setUpdateDate(new Date());
			
			Long _id = Long.parseLong(SequenceManager.getSequence(""));
			
			tvorrp.setReserveId(_id);
			
			dao.insert(tvorrp);
		}
	}
	
	/**
	 * 方法描述 : 资源预留表缓存 <br/>
	 * 
	 * @param detailId
	 * @param materialIds
	 * @param privs
	 * @param aumont
	 * @author wangsongwei
	 */
	public void saveReserveTmp(String detailId, String[] privs, String[] amount, String[] materialId, AclUserBean logonUser)
	{
		// 保存之前先清空数据
		String sbSql = "DELETE TMP_VS_ORDER_RESOURCE_RESERVE T WHERE T.ORDER_DETAIL_ID = " + detailId;
		dao.delete(sbSql, null);
		
		for (int i = 0; i < amount.length; i++)
		{
			int amount_n = amount[i] == null ? 0 : Integer.valueOf(amount[i]);
			
			if (amount_n == 0)
			{
				continue;
			}
			
			TmpVsOrderResourceReservePO tvorrp_sql = new TmpVsOrderResourceReservePO();
			tvorrp_sql.setMaterialId(Long.parseLong(materialId[i]));
			tvorrp_sql.setOrderDetailId(Long.parseLong(detailId));
			tvorrp_sql.setOrgId(Long.parseLong(privs[i]));
			
			List list = dao.select(tvorrp_sql);
			
			TmpVsOrderResourceReservePO tvorrp = new TmpVsOrderResourceReservePO();
			
			if (list.size() > 0)
			{
				tvorrp = (TmpVsOrderResourceReservePO) list.get(0);
				tvorrp.setAmount(amount_n);
				tvorrp.setUpdateBy(logonUser.getUserId());
				tvorrp.setUpdateDate(new Date());
				
				dao.update(tvorrp_sql, tvorrp);
			}
			else
			{
				tvorrp.setAmount(amount_n);
				tvorrp.setOrderDetailId(Long.valueOf(detailId));
				tvorrp.setOrgId(Long.valueOf(privs[i]));
				tvorrp.setMaterialId(Long.valueOf(materialId[i]));
				
				tvorrp.setCreateBy(logonUser.getUserId());
				tvorrp.setCreateDate(new Date());
				tvorrp.setUpdateBy(logonUser.getUserId());
				tvorrp.setUpdateDate(new Date());
				
				Long _id = Long.parseLong(SequenceManager.getSequence(""));
				
				tvorrp.setReserveId(_id);
				
				dao.insert(tvorrp);
			}
		}
	}
	
	/**
	 * 方法描述 ： 保存提车单数据,保存返利明细时不计算主表的合计以及资金承诺相关内容<br/>
	 * 
	 * @param logonUser
	 * @param request
	 * @author wangsongwei
	 */
/*	public String saveSubmission(AclUserBean logonUser, RequestWrapper request) throws Exception
	{
		// 提交状态
		String submitStatus = CommonUtils.checkNull(request.getParamValue("submitStatus"));
		Integer orderStatus = submitStatus.equals("1") ? Constant.ORDER_STATUS_02 : Constant.ORDER_STATUS_01;
		
		// 页面订单ID
		String orderId = CommonUtils.checkNull(request.getParamValue("orderId")); // 订单ID
		
		// 获取订单相关参数
		String yieldly = CommonUtils.checkNull(request.getParamValue("yieldId")); // 产地
		String fundType = CommonUtils.checkNull(request.getParamValue("finType")); // 资金账户类型
		String orderType = CommonUtils.checkNull(request.getParamValue("orderType")); // 提车单类型
		String deliveryType = CommonUtils.checkNull(request.getParamValue("deliveryType")); // 运输方式
		String deliveryAddress = CommonUtils.checkNull(request.getParamValue("deliveryAddress")); // 发运地址
		String district = CommonUtils.checkNull(request.getParamValue("district")); // 发运到区县
		String finVer = CommonUtils.checkNull(request.getParamValue("finVer")); // 资金账户版本
		String book = CommonUtils.checkNull(request.getParamValue("book")); // 三方信贷银行汇票号
		
		if (deliveryAddress.equals("") && StringUtils.isEmpty(district))
			throw new UserException("发运地址不能为空！");
		
		String linkMan = CommonUtils.checkNull(request.getParamValue("linkMan")); // 联系人
		String tel = CommonUtils.checkNull(request.getParamValue("tel")); // 联系电话
		String orderRemark = CommonUtils.checkNull(request.getParamValue("orderRemark")); // 备注
		String recDealerId = CommonUtils.checkNull(request.getParamValue("recDealerId")); // 收货经销商ID
		String receiveOrg = CommonUtils.checkNull(request.getParamValue("receiveOrg")); // 收车单位
		String orderVer = CommonUtils.checkNull(request.getParamValue("orderVer")); // 订单的版本号
		
		// 获取订单物料数据
//		String[] materialId = request.getParamValues("materialId") == null ? new String[] {} : request.getParamValues("materialId"); // 物料id
		String[] packageId = request.getParamValues("packageId") == null ? new String[] {} : request.getParamValues("packageId"); // 物料id
		String[] xpcode = request.getParamValues("xpcode") == null ? new String[] {} : request.getParamValues("xpcode"); // 选配代码
		String[] xpname = request.getParamValues("xpname") == null ? new String[] {} : request.getParamValues("xpname"); // 选配名称
		String[] colorCode = request.getParamValues("colorCode") == null ? new String[] {} : request.getParamValues("colorCode"); // 外饰颜色代码
		String[] colorName = request.getParamValues("colorName") == null ? new String[] {} : request.getParamValues("colorName"); // 外饰颜色名称
		String[] xpgyscode = request.getParamValues("xpgys") == null ? new String[] {} : request.getParamValues("xpgys"); // 供应商代码
		String[] xpgysname = request.getParamValues("xpgysname") == null ? new String[] {} : request.getParamValues("xpgysname"); // 供应商名称
		String[] amount = request.getParamValues("subAmount") == null ? new String[] {} : request.getParamValues("subAmount"); // 提报数量
		String[] jsdj = request.getParamValues("jsdj") == null ? new String[] {} : request.getParamValues("jsdj"); // 结算单价
		if(null == xpcode || !(xpcode.length>0)){
			throw new UserException("选配项未加载,请强制刷新页面后再试!");
		}else{
			for(int i=0;i<xpcode.length;i++){
				if(xpcode[i].toString().contains("-1") || 
						"undefined".equals(xpcode[i].toString())||
						"undefined".equals(xpname[i].toString())||
						xpname[i].toString().contains("请选择")||
						xpname[i].toString().contains("undefined")){
					throw new UserException("存在未加载完整的选配项,请强制刷新页面后再试!");
				}else{
					if(xpcode[i].contains(colorCode[i])&&xpname[i].contains(colorName[i])){
						// 新选配数量
						String[] xpCodeArray = xpcode[i].split(",");
						
						// 默认选配项数量
						// 物料组KSID取得
						TmVhclMaterialGroupPO tmVhclMaterialGroupPO = new TmVhclMaterialGroupPO();
						tmVhclMaterialGroupPO.setGroupId(Long.parseLong(packageId[i]));
						tmVhclMaterialGroupPO.setStatus(Constant.STATUS_ENABLE);
						List<PO> tmVhclMaterialGroupPOLst = dao.select(tmVhclMaterialGroupPO);
						if (tmVhclMaterialGroupPOLst == null || tmVhclMaterialGroupPOLst.isEmpty()) {
							throw new UserException("获取物料组信息失败!");
						}
						String ksid = ((TmVhclMaterialGroupPO)tmVhclMaterialGroupPOLst.get(0)).getKsid();
						
						// 默认选配项CODES取得
						StringBuffer sql = new StringBuffer();
						sql.append(arg0)
						XpKxztsjPO xpKxztsjPO = new XpKxztsjPO();
						xpKxztsjPO = new XpKxztsjPO(); 
						xpKxztsjPO.setKsid(ksid);
						xpKxztsjPO.setStatus("1");
						xpKxztsjPO.setIsdef("1");
						//xpKxztsjPO.setSalesStatus("0");
						List<PO> xpKxztsjPOLst = dao.select(xpKxztsjPO);
						
						// 取得基本选配的所有ITCODE，然后判断订单上的所有车型都有基本选配的编码
						StringBuffer sql = new StringBuffer();
						sql.append("SELECT F.ICCODE,F.ICNAME, \n");
						sql.append("  	   LTRIM(max(sys_connect_by_path(F.ITCODE,',')) KEEP (DENSE_RANK last order by f.pnum),',') as  ITCODE \n");
						sql.append("FROM ( \n");
						sql.append(" 	SELECT T.ICCODE,T.ICNAME,T.ITCODE,row_number() over(partition BY T.ICCODE  order by T.ITCODE) as pnum,\n");
						sql.append(" 		   row_number() over(partition BY T.ICCODE  order by T.ITCODE)-1 as Lnum\n");
						sql.append(" 	FROM ( \n");
						sql.append(" 		SELECT XK.ICCODE,XK.ICNAME,XK.ITCODE \n");
						sql.append(" 		FROM XP_KXZTSJ XK WHERE XK.KSID='").append(ksid).append("' AND XK.STATUS=1 \n");
						sql.append(" 			 AND XK.ICCODE NOT IN ( SELECT DISTINCT XK1.ICCODE FROM XP_KXZTSJ XK1 WHERE XK1.ITNAME IS NULL OR XK1.ITNAME = 'NULL' OR XK1.ITNAME = ' ')) T \n");
						sql.append(" 	) F \n");
						sql.append("GROUP BY F.ICCODE,F.ICNAME \n");
						sql.append("CONNECT BY F.LNUM=PRIOR F.PNUM AND F.ICCODE=PRIOR F.ICCODE \n");
						sql.append("START WITH F.PNUM=1 \n");
						List<Map<String, Object>> baseXpList = dao.pageQuery(sql.toString(), null, this.getFunName());
						for (Map<String, Object> map : baseXpList) {
							String itCodes = (String) map.get("ITCODE");
							String icName = (String) map.get("ICNAME");
							String[] baseItCode = itCodes.split(",");
							boolean flg =false;
							for (int k = 0; k < baseItCode.length; k++) {
								if (xpcode[i].contains(baseItCode[k])) {
									flg = true;
								}
							}
							if (!flg) {
								throw new UserException("基本选配项"+icName+"加载失败，请点击选配项重新加载，或者强制刷新页面!");
							}
						}
						if(len != xpKxztsjPOLst.size()){
							throw new UserException("选配项加载失败，请点击选配项重新加载，或者强制刷新页面!");
						}
					}else{
						throw new UserException("选配项和外饰颜色不匹配,请强制刷新页面后再试!");
					}
				}
			}
		}
		TmDealerPO dealerPo = new TmDealerPO();
		dealerPo.setDealerId(Long.parseLong(logonUser.getDealerId()));
		if(recDealerId==null || "".equals(recDealerId))
			recDealerId = logonUser.getDealerId();
		dealerPo.setDealerId(Long.parseLong(recDealerId));
		TmDealerPO dealerInfo = (TmDealerPO) dao.select(dealerPo).get(0);
		
		Map<String, Integer> suvAndMpvOrderAmount = getPackageAmount(packageId,amount,logonUser.getDealerId());
		Integer suvOrderAmount = suvAndMpvOrderAmount.get("suvAmount");
		Integer mpvOrderAmount = suvAndMpvOrderAmount.get("mpvAmount");
		if (suvOrderAmount > 0) {
			getDealerVILimit(logonUser.getDealerId(),suvOrderAmount);
		}
		if (mpvOrderAmount > 0) {
			getDealerVILimitMpv(logonUser.getDealerId(),mpvOrderAmount);
		}
		
		// 如果是专款账户,验证所有车辆是否为专款账户允许的车辆类型
		if (Constant.ACCOUNT_TYPE_14.toString().equals(fundType)) {
			// 查询专款账户所能提单的车辆
			StringBuffer sql = new StringBuffer();
			sql.append("SELECT TDVMC.VEHICLE_MODEL_ID FROM TT_DEALER_VEHICLE_MODEL_CREDIT TDVMC \n");
			sql.append("WHERE TDVMC.DEALER_ID=").append(logonUser.getDealerId()).append(" \n");
			sql.append("	  AND TDVMC.FIN_TYPE=").append(Constant.ACCOUNT_TYPE_14).append(" \n");
			sql.append("	  AND TDVMC.STATUS=").append(Constant.IF_TYPE_YES).append(" \n");
			sql.append("	  AND TRUNC(TDVMC.START_DATE) <= TRUNC(SYSDATE) \n");
			sql.append("	  AND TRUNC(TDVMC.END_DATE) >= TRUNC(SYSDATE) \n");
			List<Map<String, Object>> vehicleLst = dao.pageQuery(sql.toString(), null, this.getFunName());
			if (vehicleLst == null || vehicleLst.isEmpty() || vehicleLst.size() != 1) {
				throw new UserException("专款账户不存在,请联系厂商人员!");
			}
			String vehicleModelIdS = vehicleLst.get(0).get("VEHICLE_MODEL_ID").toString();

			TmVhclMaterialGroupPO tmVhclMaterialGroupPO1 = null;
			for (int i = 0; i < packageId.length; i++) {
				// 如果是专款账户,验证所有车辆是否为专款账户允许的车辆类型
				tmVhclMaterialGroupPO1 = new TmVhclMaterialGroupPO();
				tmVhclMaterialGroupPO1.setGroupId(Long.parseLong(packageId[i]));
				List<PO> tmVhclMaterialGroupLst = dao.select(tmVhclMaterialGroupPO1);
				tmVhclMaterialGroupPO1 = (TmVhclMaterialGroupPO) tmVhclMaterialGroupLst.get(0);
				
				if (vehicleModelIdS.indexOf(tmVhclMaterialGroupPO1.getParentGroupId().toString()) < 0) {
					throw new UserException("专款账户只能提相应车型,请修改订单后重新提报!");
				}
			}
		}
		
		*//** 临时使用 *//*
		boolean isRepeat = false;
		for (int i = 0; i < packageId.length - 1; i++)
		{
			String m1 = packageId[i];
			for (int j = i + 1; j < packageId.length; j++)
			{
				if (m1.equals(packageId[j]))
				{
					if (CommonUtils.checkNull(xpcode[i]).equals(CommonUtils.checkNull(xpcode[j])))
					{
						isRepeat = true;
						break;
					}
				}
			}
		}
		if (isRepeat)
			throw new UserException("不能出现同一配置同一选配的情况!请合并数量!");
		
		// 物料组CHECK\经销商、物料组限制\选配项CHECK\经销商、选配项限制
		xpCodeCheck(packageId, xpcode, Long.parseLong(logonUser.getDealerId()));
		
		// 判断账户数据是否是最新的
		Map<String, Object> accMap = queryDao.getDealerAccountInfo(logonUser.getDealerId(), yieldly, fundType, book); // 获取账户数据
		
		String accVer = accMap == null ? "0" : accMap.get("VER").toString(); // 获取账户版本号
		
		//如果版本号不一致并且不是三方信贷
		
		if (!accVer.equals(finVer) && !fundType.equals(Constant.ACCOUNT_TYPE_03.toString()))
		{
			throw new UserException("账户数据过期,请刷新页面重新提交!");
		}
		
		if (!accVer.equals(finVer) && (fundType.equals(Constant.ACCOUNT_TYPE_01.toString())||fundType.equals(Constant.ACCOUNT_TYPE_02.toString())))
		{
			throw new UserException("账户数据过期,请刷新页面重新提交!");
		}
		
		// 保存或者更新订单信息
		TtVsOrderPO tvop = new TtVsOrderPO();
		TtVsOrderPO tvop_sql = new TtVsOrderPO();
		
		if (!orderId.equals(""))
		{
			tvop_sql.setOrderId(Long.parseLong(orderId));
			List<?> orderList = dao.select(tvop_sql);
			
			if (orderList.size() == 1){
				tvop = (TtVsOrderPO) orderList.get(0);
			}
			else
				throw new UserException("无效提车单!");
		}
		
		tvop.setAreaId(Long.parseLong(yieldly)); // 产地 - 在选择物料的时候进行绑定
		tvop.setOrderType(Integer.parseInt(orderType)); // 订单类型
		tvop.setRaiseDate(new Date()); // 提报日期
		tvop.setDealerId(Long.parseLong(logonUser.getDealerId())); // 发货经销商
		tvop.setOrderRemark(orderRemark); // 提车单备注
		tvop.setFundTypeId(Long.parseLong(fundType)); // 资金账户类型
		tvop.setDeliveryType(Integer.parseInt(deliveryType)); // 发运方式
		if (StringUtils.isNotEmpty(deliveryAddress)) {
			tvop.setDelivAddId(Long.parseLong(deliveryAddress)); // 发送地址
		} else {
			tvop.setDelivAddId(Long.parseLong(district));  // 网销时保存发车区县,审核时指定发运地址
		}
//		tvop.setRecDealerAdd(receiveOrg);
		tvop.setRecDealerId(Long.valueOf(recDealerId)); // 收货经销商ID
//		tvop.setRecDealerAdd(receiveOrg); // 收车单位
		tvop.setLinkMan(linkMan); // 联系人
		tvop.setTel(tel); // 联系电话
		tvop.setUpdateBy(logonUser.getUserId());
		tvop.setUpdateDate(new Date());
		
		// 冗余经销商信息
		tvop.setRecDealerName(dealerInfo.getDealerName());
		tvop.setRecDealerShortname(dealerInfo.getDealerShortname());
		
		dealerPo.setDealerId(Long.parseLong(logonUser.getDealerId()));
		System.out.println(logonUser.getDealerId());
		TmDealerPO dealerInfo2 = (TmDealerPO)dao.select(dealerPo).get(0);
		
		tvop.setDealerName(dealerInfo2.getDealerName());
		tvop.setDealerShortname(dealerInfo2.getDealerShortname());
		
//		if(fundType.equals(Constant.ACCOUNT_TYPE_03.toString())) {
//			if(book.equals("")) throw new UserException("请选择三方信贷单据!");
//			tvop.setThreeCredit(Long.parseLong(book)); // 三方信贷单
//			tvop.setBankId(Long.parseLong(bank)); // 三方信贷银行
//		}
		
		if (!orderId.equals(""))
		{
			if (!tvop.getVer().toString().equals(orderVer))
				throw new UserException("订单信息已更新, 请刷新页面后重新保存! ");
			
			if (tvop.getOrderStatus().intValue() != Constant.ORDER_STATUS_01.intValue() && tvop.getOrderStatus().intValue() != Constant.ORDER_STATUS_03.intValue())
				throw new UserException("订单已提交,不允许修改!");
			
			tvop.setOrderStatus(orderStatus); // 订单状态
			tvop.setVer(tvop.getVer() + 1); // 订单版本号
			dao.update(tvop_sql, tvop);
		}
		else
		{
			orderId = SequenceManager.getSequence(""); // 订单ID
			
			tvop.setOrderStatus(orderStatus); // 订单状态
			tvop.setOrderNo(CommonUtils.getBusNo(Constant.NOCRT_ORDER_NO, Long.valueOf(yieldly))); // 生成订单流水号
			tvop.setCreateBy(logonUser.getUserId());
			tvop.setCreateDate(new Date());
			tvop.setOrderId(Long.valueOf(orderId));
			tvop.setVer(0); // 订单版本号
			
			dao.insert(tvop);
		}
		
		List<Object> params = new ArrayList<Object>();
		StringBuffer sbSql = new StringBuffer();
		
		// 先清空订单明细表数据
		sbSql.append("DELETE FROM TT_VS_ORDER_DETAIL WHERE ORDER_ID = ?");
		params.add(tvop.getOrderId());
		
		dao.delete(sbSql.toString(), params);
		
		// 计算订单明细的总价，验证通过后进行保存
		List<TtVsOrderDetailPO> orderDetailPoList = new ArrayList<TtVsOrderDetailPO>();
		Double orderPrice = 0.0d;
		for (int i = 0; i < packageId.length; i++)
		{
			// 查询出物料详细价格信息
			Map<String, Object> map = queryDao.getPackagePrice(logonUser.getDealerId(), yieldly, packageId[i]);
			
			if(map == null) throw new UserException("结算价或者料品价格未定!暂不能保存或提交订单!");
			
			if("".equals(jsdj[i].toString())){
				throw new UserException("结算价获取失败,请刷新页面后再试!");
			}
			// 获取物料单价(包含标准价格+省份调整价格)
			Double disSinglePrice = Double.valueOf(map.get("DIS_SINGLE_PRICE").toString());
			
			// 物料组KSID取得
			TmVhclMaterialGroupPO tmVhclMaterialGroupPO = new TmVhclMaterialGroupPO();
			tmVhclMaterialGroupPO.setGroupId(Long.parseLong(packageId[i]));
			tmVhclMaterialGroupPO.setStatus(Constant.STATUS_ENABLE);
			List<PO> tmVhclMaterialGroupPOLst = dao.select(tmVhclMaterialGroupPO);
			if (tmVhclMaterialGroupPOLst == null || tmVhclMaterialGroupPOLst.isEmpty()) {
				throw new UserException("获取物料组信息失败!");
			}
			String ksid = ((TmVhclMaterialGroupPO)tmVhclMaterialGroupPOLst.get(0)).getKsid();

			// 默认选配项CODES取得
			StringBuffer defaultItCodes = new StringBuffer();
			XpKxztsjPO xpKxztsjPO = new XpKxztsjPO();
			xpKxztsjPO = new XpKxztsjPO(); 
			xpKxztsjPO.setKsid(ksid);
			xpKxztsjPO.setStatus("1");
			xpKxztsjPO.setIsdef("1");
			List<PO> xpKxztsjPOLst = dao.select(xpKxztsjPO);
			if (xpKxztsjPOLst == null || xpKxztsjPOLst.isEmpty()) {
				throw new UserException("获取配置价格失败,请与管理员联系!");
			}
			for (PO po : xpKxztsjPOLst) {
				defaultItCodes.append(((XpKxztsjPO)po).getItcode()).append(",");
			}
			
			// 当前选配项是否和默认选配项相同,如果相同则按默认选配价格标识，如果不同则重新计算选配价格
			String newItCodeArray[] = xpcode[i].split(",");
			List<String> newItCodes = new ArrayList<String>();
			boolean flag = true;
			for (int k = 0; k < newItCodeArray.length; k++) {
				if (defaultItCodes.indexOf(newItCodeArray[k]) == -1 && !StringUtils.equals(newItCodeArray[k].substring(0, 2), "97")) {
					newItCodes.add(newItCodeArray[k]);
					flag = false;
				}
			}
			// 重新计算结算价
			if (!flag) {
				// 取得新配置的价格
				for (String itcode : newItCodes) {
					
					// 取默认配置的价格
					String iccode = itcode.substring(0, 2);
					Map<String, Object> params1 = new HashMap<String, Object>();
					params1.put("dealerId", logonUser.getDealerId());
					params1.put("ksid", ksid);
					params1.put("iccode", iccode);
					List<Map<String, Object>> xpPriceLst = AjaxSelectDao.getInstance().getSalesXpPrice(params1);
					if (xpPriceLst == null || xpPriceLst.isEmpty()) {
						throw new UserException("获取默认配置价格失败,请于管理员联系!");
					}
					BigDecimal defaultXpPrice = (BigDecimal) xpPriceLst.get(0).get("AMOUNT");
					if (defaultXpPrice == null) {
						throw new UserException("获取默认配置价格失败,请于管理员联系!");
					}
					
					// 取新配置的价格
					params1.clear();
					params1.put("dealerId", logonUser.getDealerId());
					params1.put("ksid", ksid);
					params1.put("xpcode", itcode);
					xpPriceLst = AjaxSelectDao.getInstance().getSalesXpPrice(params1);
					if (xpPriceLst == null || xpPriceLst.isEmpty()) {
						throw new UserException("获取新配置价格失败,请于管理员联系!");
					}
					BigDecimal newXpPrice = (BigDecimal) xpPriceLst.get(0).get("AMOUNT");
					if (newXpPrice == null) {
						throw new UserException("获取新配置价格失败,请于管理员联系!");
					}
					// 计算最终结算价格
					disSinglePrice = new BigDecimal(disSinglePrice).add(newXpPrice.subtract(defaultXpPrice)).doubleValue();
				}
			}
			

			Double singlePrice = Double.valueOf(map.get("SINGLE_PRICE").toString());
			Double dealerPrice = Double.valueOf(map.get("DEALER_PRICE").toString());
			Double modelPrice = 0.0;// Double.valueOf(map.get("MODEL_PRICE").toString());
			
			if ((disSinglePrice == 0d || disSinglePrice==800d) && orderStatus.intValue() == Constant.ORDER_STATUS_02.intValue())
				throw new UserException("物料价格不能为0!");
			
			//if(!jsdj[i].toString().equals(disSinglePrice.toString()))
			if(Double.valueOf(jsdj[i].toString()).compareTo(disSinglePrice)!=0){
				throw new UserException("结算价验证失败,请清空页面缓存重新提交订单!");
			}
			disSinglePrice = Double.parseDouble(jsdj[i].toString());
			if(null == xpcode[i] || "".equals(xpcode[i])){
				throw new UserException("选配项未加载完整,请强制刷新页面后再试!");
			}
			TtVsOrderDetailPO tvodp = new TtVsOrderDetailPO();
			
			tvodp.setOrderId(tvop.getOrderId());
			tvodp.setPreMaterialId(-1L);
			tvodp.setPackageId(Long.parseLong(packageId[i])); // 配置ID
			tvodp.setXpCode(xpcode[i]);
			tvodp.setXpName(xpname[i]);
			tvodp.setColorCode(colorCode[i]);
			tvodp.setColorName(colorName[i]);
			tvodp.setXpGysCode(CommonUtils.checkNull(xpgyscode[i]));
			tvodp.setXpGysName(CommonUtils.checkNull(xpgysname[i]));
			tvodp.setOrderAmount(Integer.parseInt(amount[i])); // 提报数量
			tvodp.setSinglePrice(singlePrice); // 物料单价
			tvodp.setModelPrice(modelPrice); // 车型折扣价
			tvodp.setDealerPrice(dealerPrice); // 经销商折扣价
			tvodp.setDisSinglePrice(disSinglePrice); // 折后单价
			tvodp.setCreateDate(new Date());
			tvodp.setCreateBy(logonUser.getUserId());
			tvodp.setUpdateBy(logonUser.getUserId());
			tvodp.setUpdateDate(new Date());
			
			// 判断当前配置+选配状态是否有唯一的物料对应
			String materialId = dao.getMaterialIdByPackageAndXp(packageId[i], xpcode[i]);
			
			if(null != materialId && !materialId.equals("")) {
				tvodp.setMaterialId(Long.parseLong(materialId));
				tvodp.setXpSendStatus("1");
				//判断当前物料是否可以提报
				String sql = "select ORDER_FLAG,MATERIAL_NAME from tm_vhcl_material where material_id="+materialId;
				List<Map<String, Object>> materialList = queryDao.pageQuery(sql, null, this.getFunName());
				String orderFlag = "";
				if(null != materialList && materialList.size()>0){
					orderFlag = materialList.get(0).get("ORDER_FLAG").toString();
				}
				if(orderFlag.equals(Constant.NASTY_ORDER_REPORT_TYPE_02.toString())){
					throw new UserException("物料"+materialList.get(0).get("MATERIAL_NAME").toString()+"当前为不可提报状态,请联系车厂!");
				}
				
			} else {
				tvodp.setXpSendStatus("0");
			}
			
			orderDetailPoList.add(tvodp);
			
			// 明细总价汇总
			orderPrice = CommonUtils.add(orderPrice, CommonUtils.multiply(disSinglePrice, Double.parseDouble(amount[i])));
		}
		//System.out.println("orderPrice=="+orderPrice+"--------ACC="+accMap.get("AMOUNT").toString());
		if (orderStatus.intValue() == Constant.ORDER_STATUS_02.intValue() 
						&& orderPrice > Double.parseDouble(accMap.get("AMOUNT").toString())) {
			throw new UserException("当前可用余额不足!请保存提车单后重新提交!");
		}
		
		for (TtVsOrderDetailPO tvodp : orderDetailPoList)
		{
			tvodp.setDetailId(Long.parseLong(SequenceManager.getSequence("")));
			dao.insert(tvodp);
		}
		
		sbSql.delete(0, sbSql.length());
		sbSql.append("UPDATE TT_VS_ORDER\n");
		sbSql.append("   SET ORDER_YF_PRICE = (SELECT SUM(A.ORDER_AMOUNT * A.DIS_SINGLE_PRICE)\n");
		sbSql.append("                           FROM TT_VS_ORDER_DETAIL A\n");
		sbSql.append("                          WHERE A.ORDER_ID = ?),\n");
		sbSql.append("		        SUB_NUM	= (SELECT SUM(A.ORDER_AMOUNT)\n");
		sbSql.append("                           FROM TT_VS_ORDER_DETAIL A\n");
		sbSql.append("                          WHERE A.ORDER_ID = ?)\n");
		sbSql.append(" WHERE ORDER_ID = ?");
		params.clear();
		params.add(tvop.getOrderId());
		params.add(tvop.getOrderId());
		params.add(tvop.getOrderId());
		
		dao.update(sbSql.toString(), params);
		
		// 如果订单状态是已提报，就需要先冻结经销商的资金或者三方信贷
		if (orderStatus.intValue() == Constant.ORDER_STATUS_02.intValue())
		{
			if (orderPrice > Double.parseDouble(accMap.get("AMOUNT").toString()))
			{
				throw new UserException("当前可用余额不足!请保存提车单后重新提交!");
			}
			
			// 冻结资金
			sbSql.delete(0, sbSql.length());
			params.clear();
			
			sbSql.append("UPDATE TT_SALES_FIN_ACC T\n");
			sbSql.append("   SET T.AMOUNT        = NVL(T.AMOUNT, 0) - (SELECT A.ORDER_YF_PRICE FROM TT_VS_ORDER A WHERE A.ORDER_ID = ?),\n");
			sbSql.append("       T.FREEZE_AMOUNT = NVL(T.FREEZE_AMOUNT, 0) + (SELECT A.ORDER_YF_PRICE FROM TT_VS_ORDER A WHERE A.ORDER_ID = ?)\n");
			sbSql.append(" WHERE T.ACC_ID = ?");
			params.add(tvop.getOrderId());
			params.add(tvop.getOrderId());
			params.add(accMap.get("ACC_ID"));
			
			dao.update(sbSql.toString(), params);

			orderFinAccFlow(tvop,logonUser);//财务扣款流水明细

			
			// 冻结完资金后再执行一次物料数据的同步
			sbSql.delete(0, sbSql.length());
			params.clear();
			
			sbSql.append("UPDATE TT_VS_ORDER_DETAIL T\n");
			sbSql.append("   SET T.MATERIAL_ID    = (SELECT A.MATERIAL_ID\n");
			sbSql.append("                             FROM TM_VHCL_MATERIAL_ERP_R   A,\n");
			sbSql.append("                                  TM_VHCL_MATERIAL_GROUP   B,\n");
			sbSql.append("                                  TM_VHCL_MATERIAL_GROUP_R C\n");
			sbSql.append("                            WHERE A.MATERIAL_ID = C.MATERIAL_ID\n");
			sbSql.append("                              AND C.GROUP_ID = B.GROUP_ID\n");
			sbSql.append("                              AND B.GROUP_LEVEL = 4\n");
			sbSql.append("                              AND B.GROUP_ID = T.PACKAGE_ID\n");
			sbSql.append("                              AND A.XP_CODE = T.XP_CODE),\n");
			sbSql.append("       T.XP_SEND_STATUS = '1'"); 
			sbSql.append(" WHERE t.ORDER_ID = ?"); 
			sbSql.append("   AND EXISTS (SELECT A.MATERIAL_ID\n");
			sbSql.append("                             FROM TM_VHCL_MATERIAL_ERP_R   A,\n");
			sbSql.append("                                  TM_VHCL_MATERIAL_GROUP   B,\n");
			sbSql.append("                                  TM_VHCL_MATERIAL_GROUP_R C\n");
			sbSql.append("                            WHERE A.MATERIAL_ID = C.MATERIAL_ID\n");
			sbSql.append("                              AND C.GROUP_ID = B.GROUP_ID\n");
			sbSql.append("                              AND B.GROUP_LEVEL = 4\n");
			sbSql.append("                              AND B.GROUP_ID = T.PACKAGE_ID\n");
			sbSql.append("                              AND A.XP_CODE = T.XP_CODE)\n");
			
			params.add(tvop.getOrderId());
			dao.update(sbSql.toString(), params);
			

		}
		
		return tvop.getOrderNo();
	}*/
	
	/**
	 * 选配项存在CHECK
	 * 
	 * @return true：选配项存在，false 选配项不存在
	 */
	private void xpCodeCheck(String[] packageIdArray, String[] xpcodeArray, Long dealerId) {
		TmVhclMaterialGroupPO tmVhclMaterialGroupPO = null;
		TtSalesVhclLimmitPO ttSalesVhclLimmitPO = null; 
		XpKxztsjPO xpKxztsjPO = null;
		TtSalesXpLimitPo ttSalesXpLimitPO = null;
		for (int i = 0; i < packageIdArray.length; i++) {
			Long packageId = Long.parseLong(packageIdArray[i]);
			// 物料组存在CHECK
			tmVhclMaterialGroupPO = new TmVhclMaterialGroupPO();
			tmVhclMaterialGroupPO.setGroupId(packageId);
			tmVhclMaterialGroupPO.setStatus(Constant.STATUS_ENABLE);
			tmVhclMaterialGroupPO.setSalesFlag(Constant.STATUS_ENABLE);
			List<PO> tmVhclMaterialGroupPOLst = dao.select(tmVhclMaterialGroupPO);
			if (tmVhclMaterialGroupPOLst == null || tmVhclMaterialGroupPOLst.isEmpty()) {
				throw new UserException("物料组不存在,请联系厂商!");
			}
			
			// 物料组、经销商限制CHECK
			ttSalesVhclLimmitPO = new TtSalesVhclLimmitPO(); 
			ttSalesVhclLimmitPO.setDealerId(dealerId);
			ttSalesVhclLimmitPO.setPackageId(packageId);
			ttSalesVhclLimmitPO.setSalesStatus(Constant.STATUS_ENABLE);
			List<PO> ttSalesVhclLimmitPOLst = dao.select(ttSalesVhclLimmitPO);
			if (ttSalesVhclLimmitPOLst == null || ttSalesVhclLimmitPOLst.isEmpty()) {
				throw new UserException("您已经不能再选配该物料组,请联系厂商!");
			}
			
			// 选配项存在CHECK
			String ksid = ((TmVhclMaterialGroupPO) tmVhclMaterialGroupPOLst.get(0)).getKsid();
			String[] xpcodes = xpcodeArray[i].split(",");
			for (int k = 0; k < xpcodes.length; k++) {
				xpKxztsjPO = new XpKxztsjPO(); 
				xpKxztsjPO.setKsid(ksid);
				xpKxztsjPO.setItcode(xpcodes[k]);
				xpKxztsjPO.setStatus("1");
				List<PO> xpKxztsjPOLst = dao.select(xpKxztsjPO);
				if (xpKxztsjPOLst == null || xpKxztsjPOLst.isEmpty()) {
					throw new UserException("选配项不存在,请联系厂商!");
				}
				
				// 选配项、经销商限制CHECK
				Long xpDetailId = ((XpKxztsjPO)xpKxztsjPOLst.get(0)).getDetailId();
				ttSalesXpLimitPO = new TtSalesXpLimitPo(); 
				ttSalesXpLimitPO.setDealerId(dealerId);
				ttSalesXpLimitPO.setStatus(Constant.STATUS_ENABLE);
				ttSalesXpLimitPO.setXpDetailId(xpDetailId);
				List<PO> ttSalesXpLimitPOLst = dao.select(ttSalesXpLimitPO);
				if (ttSalesXpLimitPOLst == null || ttSalesXpLimitPOLst.isEmpty()) {
					throw new UserException("您已经不能再选配该选配项,请联系厂商!");
				}
			}
		}
	}
	
	/**
	 * @method 订单扣款财务流水
	 * @description
	 * @author wizard_lee
	 * @date 2014-09-19
	 */
/*	private void orderFinAccFlow(TtVsOrderPO tvop,AclUserBean logonUser){
		Logger logger = Logger.getLogger(CarSubmissionDao.class);
		TtVsOrderPO tvopNew = new TtVsOrderPO();
		tvopNew.setOrderId(tvop.getOrderId());
		List<?> orderList =  dao.select(tvopNew);
		TtVsOrderPO tvopData = (TtVsOrderPO) orderList.get(0);
		TtSaleFundsRoseDetailPO fundDetailPO = new TtSaleFundsRoseDetailPO();
		Double orderPrice = tvopData.getOrderYfPrice();
		fundDetailPO.setRemark(tvopData.getOrderNo()+"增加冻结");
		fundDetailPO.setDealerId(tvopData.getDealerId());
		fundDetailPO.setDetailId(Long.parseLong(SequenceManager.getSequence("")));
		fundDetailPO.setAmount(orderPrice);
		fundDetailPO.setCreateBy(logonUser.getUserId());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		fundDetailPO.setCreateDate(sdf.format(new Date()));
		fundDetailPO.setFinType(Integer.parseInt(tvopData.getFundTypeId().toString()));
		fundDetailPO.setFinInType(Integer.parseInt(tvopData.getFundTypeId().toString()));
		fundDetailPO.setIsType(6);
		dao.insert(fundDetailPO);
		fundDetailPO.setDetailId(Long.parseLong(SequenceManager.getSequence("")));
		fundDetailPO.setRemark(tvopData.getOrderNo()+"扣除可用余额");
		logger.info("当前订单冻结价格=="+orderPrice);
		logger.info("当前订单扣除价格=="+-1*orderPrice);
		fundDetailPO.setAmount(-orderPrice);//扣除可用余额(-)
		
		fundDetailPO.setIsType(5);
		sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		fundDetailPO.setCreateDate(sdf.format(new Date()));
		dao.insert(fundDetailPO);
	}*/
	
	/**
	 * 
	 * @param packageId
	 * @param xpcode
	 * @return
	 */
	public String getMaterialIdByPackageAndXp(String packageId, String xpcode)
	{
		
		List<Object> params = new ArrayList<Object>();
		StringBuffer sbSql = new StringBuffer();
		/*
		 * 通过物料关系表，将配置的物料获取回去更新到订单明细表物料ID
		 * */
		sbSql.append("SELECT A.*\n");
		sbSql.append("  FROM TM_VHCL_MATERIAL_ERP_R A, TM_VHCL_MATERIAL_GROUP_R B\n");
		sbSql.append(" WHERE A.MATERIAL_ID = B.MATERIAL_ID\n");
		sbSql.append("   AND B.GROUP_ID = "+packageId+"\n");
		sbSql.append("   AND A.XP_CODE = '"+xpcode+"'"); 
		//params.add(packageId); 
		//params.add("'"+xpcode+"'");
		
		List<Map<String, Object>> list = dao.pageQuery(sbSql.toString(), null, getFunName());
		
		String materialId = "";
		if(list.size() > 0) {
			materialId = list.get(0).get("MATERIAL_ID").toString();
		}
		
		return materialId;
	}

	/**
	 * 方法描述 ： 根据订单数据冻结经销商资金账户<br/>
	 * 
	 * @param valueOf
	 * @param userId
	 * @author wangsongwei
	 */
	public void updateDealerAccountByOrder(Long orderId, Long userId, Integer djType)
	{
		
		StringBuffer sbSql = new StringBuffer();
		
		if (djType.intValue() == Constant.ORDER_INVOICE_TYPE_01.intValue())
		{
			// 将资金账户的使用明细状态设置为已冻结
			sbSql.append("UPDATE TT_SALES_ORDER_ACC T																\n");
			sbSql.append("SET																						\n");
			sbSql.append("       T.USE_STATUS = " + Constant.ORDER_ACCOUNT_STATUS_02 + ",							\n");
			sbSql.append("       T.USE_AMOUNT = (																	\n");
			sbSql.append("            SELECT TVO.ORDER_YF_PRICE FROM TT_VS_ORDER TVO WHERE TVO.ORDER_ID = T.ORDER_ID\n");
			sbSql.append("       ),																					\n");
			sbSql.append("       T.UPDATE_BY = " + userId + ",														\n");
			sbSql.append("       T.UPDATE_DATE = SYSDATE															\n");
			sbSql.append("WHERE T.ORDER_ID =" + orderId + " AND T.USE_STATUS = " + Constant.ORDER_ACCOUNT_STATUS_02 + "\n");
			sbSql.append("       AND T.ORDER_TYPE = " + djType + "													\n");
			
			dao.update(sbSql.toString(), null);
		}
		else if (djType.intValue() == Constant.ORDER_INVOICE_TYPE_02.intValue())
		{
			// 将资金账户的使用明细状态设置为已冻结
			sbSql.append("UPDATE TT_SALES_ORDER_ACC T																\n");
			sbSql.append("SET																						\n");
			sbSql.append("       T.USE_STATUS = " + Constant.ORDER_ACCOUNT_STATUS_02 + ",							\n");
			sbSql.append("       T.USE_AMOUNT = (																	\n");
			sbSql.append("            SELECT TVO.ORDER_YF_PRICE FROM TT_OUT_ORDER TVO WHERE TVO.ORDER_ID = T.ORDER_ID\n");
			sbSql.append("       ),																					\n");
			sbSql.append("       T.UPDATE_BY = " + userId + ",														\n");
			sbSql.append("       T.UPDATE_DATE = SYSDATE															\n");
			sbSql.append("WHERE T.ORDER_ID =" + orderId + " AND T.USE_STATUS = " + Constant.ORDER_ACCOUNT_STATUS_02 + "\n");
			sbSql.append("       AND T.ORDER_TYPE = " + djType + "													\n");
			
			dao.update(sbSql.toString(), null);
		}
		
		sbSql.delete(0, sbSql.length());
		
		// 冻结经销商资金账户表
		sbSql.append("UPDATE TT_SALES_FIN_ACC T								\n");
		sbSql.append("SET T.FREEZE_AMOUNT = NVL(T.FREEZE_AMOUNT,0) + (		\n");
		sbSql.append("      SELECT NVL(TSOA.USE_AMOUNT,0)					\n");
		sbSql.append("      FROM TT_SALES_ORDER_ACC TSOA					\n");
		sbSql.append("      WHERE											\n");
		sbSql.append("             TSOA.ACC_ID = T.ACC_ID					\n");
		sbSql.append("             AND										\n");
		sbSql.append("             TSOA.ORDER_ID = " + orderId + "			\n");
		sbSql.append("			   AND TSOA.USE_STATUS = " + Constant.ORDER_ACCOUNT_STATUS_02 + "");
		sbSql.append("),													\n");
		sbSql.append("T.AMOUNT = NVL(T.AMOUNT,0) - (						\n");
		sbSql.append("      SELECT NVL(TSOA.USE_AMOUNT,0)					\n");
		sbSql.append("      FROM TT_SALES_ORDER_ACC TSOA					\n");
		sbSql.append("      WHERE											\n");
		sbSql.append("             TSOA.ACC_ID = T.ACC_ID					\n");
		sbSql.append("             AND										\n");
		sbSql.append("             TSOA.ORDER_ID = " + orderId + "			\n");
		sbSql.append("			   AND TSOA.USE_STATUS = " + Constant.ORDER_ACCOUNT_STATUS_02 + "");
		sbSql.append("),													\n");
		sbSql.append("T.VER = NVL(T.VER,0) + 1,								\n");
		sbSql.append("T.UPDATE_BY = " + userId + ",							\n");
		sbSql.append("T.UPDATE_DATE = SYSDATE								\n");
		sbSql.append("WHERE													\n");
		sbSql.append("     T.ACC_ID IN (										\n");
		sbSql.append("          SELECT TSOA.ACC_ID							\n");
		sbSql.append("          FROM TT_SALES_ORDER_ACC TSOA				\n");
		sbSql.append("          WHERE										\n");
		sbSql.append("             TSOA.ORDER_ID = " + orderId + "			\n");
		sbSql.append("			   AND TSOA.USE_STATUS = " + Constant.ORDER_ACCOUNT_STATUS_02 + "");
		sbSql.append("     )");
		
		dao.update(sbSql.toString(), null);
		
	}
	
	/**
	 * 方法描述 ： 根据订单数据扣减经销商的冻结资金账户<br/>
	 * 
	 * @param valueOf
	 * @param userId
	 * @author wangsongwei
	 */
	public void updateDealerAccountByOrderSub(Long orderId, Long userId, Integer djType) throws Exception
	{
		
		StringBuffer sbSql = new StringBuffer();
		
		if (djType.intValue() == Constant.ORDER_INVOICE_TYPE_01.intValue())
		{
			// 将资金账户的使用明细状态设置为已扣减
			sbSql.append("UPDATE TT_SALES_ORDER_ACC T																\n");
			sbSql.append("SET																						\n");
			sbSql.append("       T.USE_STATUS = " + Constant.ORDER_ACCOUNT_STATUS_03 + ",							\n");
			sbSql.append("       T.USE_AMOUNT = (																	\n");
			sbSql.append("            SELECT TVO.ORDER_YF_PRICE FROM TT_VS_ORDER TVO WHERE TVO.ORDER_ID = T.ORDER_ID\n");
			sbSql.append("       ),																					\n");
			sbSql.append("       T.UPDATE_BY = " + userId + ",														\n");
			sbSql.append("       T.UPDATE_DATE = SYSDATE															\n");
			sbSql.append("WHERE T.ORDER_ID =" + orderId + " AND T.USE_STATUS = " + Constant.ORDER_ACCOUNT_STATUS_02 + "\n");
			sbSql.append("       AND T.ORDER_TYPE = " + djType + "													\n");
			
			dao.update(sbSql.toString(), null);
		}
		else if (djType.intValue() == Constant.ORDER_INVOICE_TYPE_02.intValue())
		{
			// 将资金账户的使用明细状态设置为已扣减
			sbSql.append("UPDATE TT_SALES_ORDER_ACC T																\n");
			sbSql.append("SET																						\n");
			sbSql.append("       T.USE_STATUS = " + Constant.ORDER_ACCOUNT_STATUS_03 + ",							\n");
			sbSql.append("       T.USE_AMOUNT = (																	\n");
			sbSql.append("            SELECT TVO.ORDER_YF_PRICE FROM TT_OUT_ORDER TVO WHERE TVO.ORDER_ID = T.ORDER_ID\n");
			sbSql.append("       ),																					\n");
			sbSql.append("       T.UPDATE_BY = " + userId + ",														\n");
			sbSql.append("       T.UPDATE_DATE = SYSDATE															\n");
			sbSql.append("WHERE T.ORDER_ID =" + orderId + " AND T.USE_STATUS = " + Constant.ORDER_ACCOUNT_STATUS_02 + "\n");
			sbSql.append("       AND T.ORDER_TYPE = " + djType + "												    \n");
			
			dao.update(sbSql.toString(), null);
		}
		
		sbSql.delete(0, sbSql.length());
		
		// 冻结经销商资金账户表
		sbSql.append("UPDATE TT_SALES_FIN_ACC T								\n");
		sbSql.append("SET T.FREEZE_AMOUNT = NVL(T.FREEZE_AMOUNT,0) - (		\n");
		sbSql.append("      SELECT NVL(TSOA.USE_AMOUNT,0)					\n");
		sbSql.append("      FROM TT_SALES_ORDER_ACC TSOA					\n");
		sbSql.append("      WHERE											\n");
		sbSql.append("             TSOA.ACC_ID = T.ACC_ID					\n");
		sbSql.append("             AND										\n");
		sbSql.append("             TSOA.ORDER_ID = " + orderId + "			\n");
		sbSql.append("			   AND TSOA.USE_STATUS = " + Constant.ORDER_ACCOUNT_STATUS_03 + "");
		sbSql.append("),													\n");
		sbSql.append("T.VER = NVL(T.VER,0) + 1,								\n");
		sbSql.append("T.UPDATE_BY = " + userId + ",							\n");
		sbSql.append("T.UPDATE_DATE = SYSDATE								\n");
		sbSql.append("WHERE													\n");
		sbSql.append("     T.ACC_ID IN (										\n");
		sbSql.append("          SELECT TSOA.ACC_ID							\n");
		sbSql.append("          FROM TT_SALES_ORDER_ACC TSOA				\n");
		sbSql.append("          WHERE										\n");
		sbSql.append("             TSOA.ORDER_ID = " + orderId + "			\n");
		sbSql.append("			   AND TSOA.USE_STATUS = " + Constant.ORDER_ACCOUNT_STATUS_03 + "");
		sbSql.append("     )");
		
		dao.update(sbSql.toString(), null);
	}
	
	/**
	 * 方法描述 ： 更新订单表统计数据<br/>
	 * <ul>
	 * <li>提报数量 = 明细提报数量总和</li>
	 * <li>订单总价 = 明细折后总额的总和</li>
	 * <li>订单应付 = 订单总价 - 返利折后总额 - 预付款</li>
	 * </ul>
	 * 
	 * @param orderId
	 * @author wangsongwei
	 */
	public void updateOrderData(Long orderId, Long userId)
	{
		
		StringBuffer sbSql = new StringBuffer();
		
		sbSql.append("-- 统计提车订单统计信息\n");
		sbSql.append("UPDATE TT_VS_ORDER T\n");
		sbSql.append("SET\n");
		sbSql.append("   T.SUB_NUM          = (SELECT SUM(D.ORDER_AMOUNT) FROM TT_VS_ORDER_DETAIL D WHERE D.ORDER_ID = T.ORDER_ID), 		-- 提报数量\n");
		sbSql.append("   T.ORDER_PRICE      = (SELECT SUM(D.TOTAL_PRICE) FROM TT_VS_ORDER_DETAIL D WHERE D.ORDER_ID = T.ORDER_ID),    		-- 订单总价\n");
		sbSql.append("	 T.ORDER_YF_PRICE   = ( 																									  \n");
		sbSql.append("								SELECT																							  \n");
		sbSql.append("        							NVL(A.TOTAL_PRICE,0) - NVL(B.DIS_AMOUNT,0) - NVL(T.PRE_PRICE,0)			  					  \n");
		sbSql.append("								FROM																							  \n");
		sbSql.append("        				  			(																							  \n");
		sbSql.append("            							SELECT																					  \n");
		sbSql.append("              								SUM(D.TOTAL_PRICE)  TOTAL_PRICE											   		  \n");
		sbSql.append("            							FROM																					  \n");
		sbSql.append("              								TT_VS_ORDER_DETAIL D															  \n");
		sbSql.append("            							WHERE D.ORDER_ID = " + orderId + "														  \n");
		sbSql.append("        							) A,	-- 订单折后总额																		  \n");
		sbSql.append("        							(																							  \n");
		sbSql.append("            							SELECT SUM(TSOR.DIS_AMOUNT) DIS_AMOUNT													  \n");
		sbSql.append("            							FROM																					  \n");
		sbSql.append("            									TT_SALES_ORDER_REB TSOR															  \n");
		sbSql.append("            							WHERE TSOR.ORDER_ID = " + orderId + "													  \n");
		sbSql.append("        							) B		-- 订单返利总额																		  \n");
		sbSql.append("	 						), 																						  			  \n");
		sbSql.append("   T.REBATE_PRICE     = (SELECT SUM(NVL(TSOR.DIS_AMOUNT,0)) FROM TT_SALES_ORDER_REB TSOR WHERE TSOR.ORDER_ID = T.ORDER_ID), 	-- 反利总额\n");
		sbSql.append("   T.DISCOUNT         = (SELECT SUM(D.DISCOUNT_PRICE) FROM TT_VS_ORDER_DETAIL D WHERE D.ORDER_ID = T.ORDER_ID),   	-- 折让总额\n");
		sbSql.append("   T.CHK_NUM          = (SELECT SUM(D.CHECK_AMOUNT) FROM TT_VS_ORDER_DETAIL D WHERE D.ORDER_ID = T.ORDER_ID),    		-- 审核数量\n");
		sbSql.append("   T.VER          	= NVL(T.VER,0) + 1,    																			-- 审核数量\n");
		sbSql.append("   T.UPDATE_DATE = sysdate,																									  \n");
		sbSql.append("   T.UPDATE_BY   = " + userId + "																								  \n");
		sbSql.append("WHERE  T.ORDER_ID = " + orderId + " 																						      \n");
		
		dao.update(sbSql.toString(), null);
	}
	
	/**
	 * 方法描述 ： 更新订单表统计数据<br/>
	 * 
	 * @param orderId
	 * @author wangsongwei
	 */
	public void updateOrderDataAfterCheck(Long orderId, Long userId)
	{
		
		StringBuffer sbSql = new StringBuffer();
		
		sbSql.append("																																  \n");
		sbSql.append("-- 统计提车订单统计信息																											  \n");
		sbSql.append("UPDATE TT_VS_ORDER T																											  \n");
		sbSql.append("SET																														      \n");
		sbSql.append("   T.ORDER_PRICE      = (SELECT SUM(D.DIS_AMOUNT) FROM TT_VS_ORDER_DETAIL D WHERE D.ORDER_ID = T.ORDER_ID),    		-- 订单总价\n");
		sbSql.append("	 T.ORDER_YF_PRICE   = ( 																							-- 应付金额 \n");
		sbSql.append("								SELECT																							  \n");
		sbSql.append("        							NVL(A.TOTAL_PRICE,0) - NVL(B.DIS_AMOUNT,0)								  					  \n");
		sbSql.append("								FROM																							  \n");
		sbSql.append("        				  			(																							  \n");
		sbSql.append("            							SELECT																					  \n");
		sbSql.append("              								SUM(D.TOTAL_PRICE)  TOTAL_PRICE											   		  \n");
		sbSql.append("            							FROM																					  \n");
		sbSql.append("              								TT_VS_ORDER_DETAIL D															  \n");
		sbSql.append("            							WHERE D.ORDER_ID = " + orderId + "														  \n");
		sbSql.append("        							) A,	-- 订单折后总额																		  \n");
		sbSql.append("        							(																							  \n");
		sbSql.append("            							SELECT SUM(TSOR.DIS_AMOUNT) DIS_AMOUNT													  \n");
		sbSql.append("            							FROM																					  \n");
		sbSql.append("            									TT_SALES_ORDER_REB TSOR															  \n");
		sbSql.append("            							WHERE TSOR.ORDER_ID = " + orderId + "													  \n");
		sbSql.append("        							) B		-- 订单返利总额																		  \n");
		sbSql.append("	 						), 																						  			  \n");
		sbSql.append("   T.DISCOUNT         = (SELECT SUM(D.DISCOUNT_PRICE) FROM TT_VS_ORDER_DETAIL D WHERE D.ORDER_ID = T.ORDER_ID),   	-- 折让总额\n");
		sbSql.append("   T.CHK_NUM          = (SELECT SUM(D.CHECK_AMOUNT) FROM TT_VS_ORDER_DETAIL D WHERE D.ORDER_ID = T.ORDER_ID),    		-- 审核数量\n");
		// sbSql.append("	 T.PRE_PRICE 		= () 							\n");
		sbSql.append("   T.VER          	= NVL(T.VER,0) + 1,    																			-- 审核数量\n");
		sbSql.append("   T.UPDATE_DATE = sysdate,																									  \n");
		sbSql.append("   T.UPDATE_BY   = " + userId + "																								  \n");
		sbSql.append("WHERE  T.ORDER_ID = " + orderId + " 																						      \n");
		
		dao.update(sbSql.toString(), null);
	}
	
	// /**
	// * 方法描述 ： <br/>
	// *
	// * @param valueOf
	// * @param userId
	// * @author wangsongwei
	// */
	// private void updateOrderDataWithCheck(Long orderId, Long userId) {
	//
	// StringBuffer sbSql = new StringBuffer();
	//
	// sbSql.append("-- 统计提车订单统计信息\n");
	// sbSql.append("UPDATE TT_VS_ORDER T\n");
	// sbSql.append("SET\n");
	// sbSql.append("   T.ORDER_PRICE      = (SELECT SUM(D.DIS_AMOUNT) FROM TT_VS_ORDER_DETAIL D WHERE D.ORDER_ID = T.ORDER_ID),    		-- 订单总价\n");
	// sbSql.append("	 T.ORDER_YF_PRICE   = ( 																									  \n");
	// sbSql.append("								SELECT																							  \n");
	// sbSql.append("        								NVL(A.TOTAL_PRICE,0) - NVL(B.USE_AMOUNT,0)*NVL(C.DISCOUNT_RATE,0)						  \n");
	// sbSql.append("								FROM																							  \n");
	// sbSql.append("        								(																						  \n");
	// sbSql.append("            								SELECT																			      \n");
	// sbSql.append("              								SUM(D.TOTAL_PRICE)  TOTAL_PRICE										\n");
	// sbSql.append("            								FROM																\n");
	// sbSql.append("              								TT_VS_ORDER_DETAIL D\n");
	// sbSql.append("            								WHERE D.ORDER_ID = " + orderId +
	// "												\n");
	// sbSql.append("        								) A,		-- 订单的折扣总额												\n");
	// sbSql.append("        								(													\n");
	// sbSql.append("            								SELECT SUM(TSOR.USE_AMOUNT) USE_AMOUNT\n");
	// sbSql.append("            								FROM\n");
	// sbSql.append("            									TT_SALES_ORDER_REB TSOR\n");
	// sbSql.append("            								WHERE TSOR.ORDER_ID = " + orderId +
	// "\n");
	// sbSql.append("       							 	) B,		-- 订单使用的返利总额							\n");
	// sbSql.append("        								(						\n");
	// sbSql.append("            								SELECT 1- TVOD.DISCOUNT_RATE/100 DISCOUNT_RATE\n");
	// sbSql.append("            								FROM					\n");
	// sbSql.append("            								TT_VS_ORDER_DETAIL TVOD				\n");
	// sbSql.append("            								WHERE TVOD.ORDER_ID = " + orderId +
	// " GROUP BY TVOD.DISCOUNT_RATE\n");
	// sbSql.append("        								) C 		-- 物料折扣率或返利的折扣率 \n");
	// sbSql.append("							), 																									  \n");
	// sbSql.append("   T.REBATE_PRICE     = (SELECT SUM(TSOR.USE_AMOUNT) FROM TT_SALES_ORDER_REB TSOR WHERE TSOR.ORDER_ID = T.ORDER_ID), 	-- 反利总额\n");
	// sbSql.append("   T.DISCOUNT         = (SELECT SUM(D.DISCOUNT_PRICE) FROM TT_VS_ORDER_DETAIL D WHERE D.ORDER_ID = T.ORDER_ID),   	-- 折让总额\n");
	// sbSql.append("   T.CHK_NUM          = (SELECT SUM(D.CHECK_AMOUNT) FROM TT_VS_ORDER_DETAIL D WHERE D.ORDER_ID = T.ORDER_ID),    		-- 审核数量\n");
	// sbSql.append("   T.VER          	= NVL(T.VER,0) + 1,    																			-- 审核数量\n");
	// sbSql.append("   T.UPDATE_DATE = sysdate,																									  \n");
	// sbSql.append("   T.UPDATE_BY   = " + userId +
	// "																								  \n");
	// sbSql.append("WHERE  T.ORDER_ID = " + orderId +
	// " 																						      \n");
	//
	// dao.update(sbSql.toString(), null);
	//
	// }
	
	/**
	 * 方法描述 ： 更新全部订单表统计数据<br/>
	 * 
	 * @deprecated
	 * @param orderId
	 * @author wangsongwei
	 */
	public void updateOrderDataAll(Long orderId, Long userId)
	{
		
		StringBuffer sbSql = new StringBuffer();
		
		sbSql.append("-- 统计提车订单统计信息\n");
		sbSql.append("UPDATE TT_VS_ORDER T\n");
		sbSql.append("SET\n");
		sbSql.append("   T.SUB_NUM          = (SELECT SUM(D.ORDER_AMOUNT) FROM TT_VS_ORDER_DETAIL D WHERE D.ORDER_ID = T.ORDER_ID), 		-- 提报数量\n");
		sbSql.append("   T.ORDER_PRICE      = (SELECT SUM(D.DIS_AMOUNT) FROM TT_VS_ORDER_DETAIL D WHERE D.ORDER_ID = T.ORDER_ID),    		-- 订单总价\n");
		sbSql.append("   T.ORDER_YF_PRICE   = (SELECT SUM(D.TOTAL_PRICE) FROM TT_VS_ORDER_DETAIL D WHERE D.ORDER_ID = T.ORDER_ID),  		-- 订单应付\n");
		sbSql.append("   T.Rebate_Price     = (SELECT SUM(TSOR.USE_AMOUNT) FROM TT_SALES_ORDER_REB TSOR WHERE TSOR.ORDER_ID = T.ORDER_ID), 	-- 反利总额\n");
		sbSql.append("   T.Discount         = (SELECT SUM(D.DISCOUNT_PRICE) FROM TT_VS_ORDER_DETAIL D WHERE D.ORDER_ID = T.ORDER_ID),   	-- 折让总额\n");
		sbSql.append("   T.Chk_Num          = (SELECT SUM(D.CHECK_AMOUNT) FROM TT_VS_ORDER_DETAIL D WHERE D.ORDER_ID = T.ORDER_ID),    		-- 审核数量\n");
		sbSql.append("   T.VER          	= NVL(T.VER,0) + 1,    																			-- 审核数量\n");
		sbSql.append("   T.UPDATE_DATE = sysdate,																									  \n");
		sbSql.append("   T.UPDATE_BY   = " + userId + "																								  \n");
		
		dao.update(sbSql.toString(), null);
	}
	
	/**
	 * 方法描述 ： 计算订单明细表的折扣总额,折后总额,折后单价,折扣总额
	 * 
	 * @param orderId
	 * @author wangsongwei
	 */
	public void updateOrderDetail(Long orderId, Long userId)
	{
		
		StringBuffer sbSql = new StringBuffer();
		
		sbSql.append("-- 根据提报的物料数据更新物料表统计信息															   \n");
		sbSql.append("UPDATE TT_VS_ORDER_DETAIL T																   \n");
		sbSql.append("SET																						   \n");
		sbSql.append("   T.DIS_AMOUNT        = T.ORDER_AMOUNT * T.SINGLE_PRICE,                          -- 折前总额\n");
		sbSql.append("   T.TOTAL_PRICE       = T.ORDER_AMOUNT * (1-T.DISCOUNT_RATE/100)*T.SINGLE_PRICE,  -- 折后总额\n");
		sbSql.append("   T.DISCOUNT_S_PRICE  = (1-T.DISCOUNT_RATE/100)*T.SINGLE_PRICE,                   -- 折后单价\n");
		sbSql.append("   T.DISCOUNT_PRICE    = T.ORDER_AMOUNT *(T.DISCOUNT_RATE/100)*T.SINGLE_PRICE,     -- 折扣总额\n");
		sbSql.append("   T.UPDATE_DATE = sysdate,																   \n");
		sbSql.append("   T.UPDATE_BY   = " + userId + "															   \n");
		sbSql.append("WHERE																						   \n");
		sbSql.append("   T.ORDER_ID = " + orderId);
		
		dao.update(sbSql.toString(), null);
	}
	
	/**
	 * 方法描述 ： 资源审核后使用审核价格和审核数量重新计算订单明细表的折扣总额,折后总额,折后单价,折扣总额
	 * 
	 * @param orderId
	 * @author wangsongwei
	 */
	public void updateOrderDetailAfterCheck(Long orderId, Long userId)
	{
		
		StringBuffer sbSql = new StringBuffer();
		
		sbSql.append("																   							   \n");
		sbSql.append("-- 根据审核后的数据更新物料表统计信息															   \n");
		sbSql.append("UPDATE TT_VS_ORDER_DETAIL T																   \n");
		sbSql.append("SET																						   \n");
		sbSql.append("   T.DIS_AMOUNT        = T.CHECK_AMOUNT * T.CHK_PRICE,                          	 -- 折前总额\n");
		sbSql.append("   T.TOTAL_PRICE       = T.CHECK_AMOUNT * (1-T.DISCOUNT_RATE/100)*T.CHK_PRICE,  	 -- 折后总额\n");
		sbSql.append("   T.DISCOUNT_S_PRICE  = (1-T.DISCOUNT_RATE/100)*T.CHK_PRICE,                   	 -- 折后单价\n");
		sbSql.append("   T.DISCOUNT_PRICE    = T.CHECK_AMOUNT *(T.DISCOUNT_RATE/100)*T.CHK_PRICE,     	 -- 折扣总额\n");
		sbSql.append("   T.UPDATE_DATE = sysdate,																   \n");
		sbSql.append("   T.UPDATE_BY   = " + userId + "															   \n");
		sbSql.append("WHERE																						   \n");
		sbSql.append("   T.ORDER_ID = " + orderId);
		
		dao.update(sbSql.toString(), null);
	}
	
	/**
	 * 方法描述 ： 更新全部订单明细表统计数据<br/>
	 * 
	 * @deprecated
	 * @param orderId
	 * @author wangsongwei
	 */
	public void updateOrderDetailDataAll(Long orderId, Long userId)
	{
		
		StringBuffer sbSql = new StringBuffer();
		
		sbSql.append("-- 根据提报的物料数据更新物料表统计信息															   \n");
		sbSql.append("UPDATE TT_VS_ORDER_DETAIL T																   \n");
		sbSql.append("SET																						   \n");
		sbSql.append("   T.DIS_AMOUNT        = T.ORDER_AMOUNT * T.SINGLE_PRICE,                          -- 折前总额\n");
		sbSql.append("   T.TOTAL_PRICE       = T.ORDER_AMOUNT * (1-T.DISCOUNT_RATE/100)*T.SINGLE_PRICE,  -- 折后总额\n");
		sbSql.append("   T.DISCOUNT_S_PRICE  = (1-T.DISCOUNT_RATE/100)*T.SINGLE_PRICE,                   -- 折后单价\n");
		sbSql.append("   T.DISCOUNT_PRICE    = T.ORDER_AMOUNT *(T.DISCOUNT_RATE/100)*T.SINGLE_PRICE,     -- 折扣总额\n");
		sbSql.append("   T.UPDATE_DATE = sysdate,																   \n");
		sbSql.append("   T.UPDATE_BY   = " + userId + "															   \n");
		
		dao.update(sbSql.toString(), null);
	}
	
	protected PO wrapperPO(ResultSet rs, int idx)
	{
		return null;
	}
	
	/**
	 * 方法描述 ： 订单作废业务逻辑处理<br/>
	 * 
	 * @param valueOf
	 * @param remark
	 * @param logonUser
	 * @author wangsongwei
	 */
	public void orderNullify(Long orderId, String remark, AclUserBean logonUser) throws Exception
	{
		List<Object> params = new ArrayList<Object>();
		
		TtVsOrderPO tvop_sql = new TtVsOrderPO();
		tvop_sql.setOrderId(orderId);
		
		List<?> orderList = dao.select(tvop_sql);
		
		if (orderList.size() > 0)
		{
			TtVsOrderPO tvop = (TtVsOrderPO) orderList.get(0);
			
			// 查询订单是否已经流转到储运阶段
			StringBuffer sbSql = new StringBuffer();
			
			sbSql.append("SELECT 1 FROM TT_VS_ORDER TVO WHERE TVO.ORDER_ID = " + orderId + " AND TVO.ORDER_STATUS = " + Constant.ORDER_STATUS_11);
			
			params.add(orderId);
			
			Map<String, Object> map = dao.pageQueryMap(sbSql.toString(), null, getFunName());
			
			// 如果订单已经流转到储运阶段则不能作废订单
			if (map != null)
			{
				throw new RuntimeException("订单已生效,不允许作废订单!");
			}
			
			// 回滚资源预留表数据
			rollbackOrderReource(orderId, logonUser.getUserId());
			
			// 如果是订做车必须要回写订做车的提报数量
			if (tvop.getOrderType().intValue() == Constant.ORDER_TYPE_02.intValue())
			{
				/* 先查询出需要作废订单关联的订做车明细ID */
				sbSql.delete(0, sbSql.length());
				sbSql.append("SELECT za_concat(T.CUS_DETAIL_ID) CUS_DETAIL_ID FROM TT_VS_ORDER_DETAIL T WHERE T.ORDER_ID = " + orderId + " GROUP BY T.CUS_DETAIL_ID");
				Map<String, Object> dMap = dao.pageQueryMap(sbSql.toString(), null, getFunName());
				String details = CommonUtils.checkNull(dMap.get("CUS_DETAIL_ID"));
				
				sbSql.delete(0, sbSql.length());
				sbSql.append("UPDATE TT_VS_ORDER_DETAIL T SET T.CUS_DETAIL_ID = NULL WHERE T.ORDER_ID = " + orderId);
				dao.update(sbSql.toString(), null);
				
				sbSql.delete(0, sbSql.length());
				
				sbSql.append("UPDATE TM_CUS_ORDER_DETAIL T\n");
				sbSql.append("   SET T.ORDER_NUM = NVL((SELECT SUM(A.ORDER_AMOUNT)\n");
				sbSql.append("                           FROM TT_VS_ORDER_DETAIL A\n");
				sbSql.append("                          WHERE A.CUS_DETAIL_ID = T.DETAIL_ID),0)\n");
				sbSql.append(" WHERE T.DETAIL_ID IN (" + details + ")");
				
				dao.update(sbSql.toString(), null);
				
				sbSql.delete(0, sbSql.length());
				
				sbSql.append("SELECT TCO.CUS_ORDER_ID\n");
				sbSql.append("  FROM TM_CUS_ORDER_DETAIL TCOD, TM_CUS_ORDER TCO\n");
				sbSql.append(" WHERE TCOD.CUS_ORDER_ID = TCO.CUS_ORDER_ID\n");
				sbSql.append("   AND TCOD.DETAIL_ID IN (" + details + ")\n");
				sbSql.append(" GROUP BY TCO.CUS_ORDER_ID");
				
				map = dao.pageQueryMap(sbSql.toString(), null, getFunName());
			}
			
			// 回退资金账户数据
			// rollbackDealerAccount(orderId, logonUser.getUserId());
			
			// 保存审核意见
			saveCheckResult(orderId, "订单作废", Constant.FLEET_SUPPORT_CHECK_STATUS_02, Constant.OUT_ORDER_AUDIT_TYPE_03, logonUser);
			
			// 更新订单状态
			tvop.setOrderStatus(Constant.ORDER_STATUS_13);
			tvop.setUpdateBy(logonUser.getUserId());
			tvop.setUpdateDate(new Date());
			tvop.setVer(tvop.getVer() + 1);
			
			dao.update(tvop_sql, tvop);
			
			sbSql.delete(0, sbSql.length());
			// 先作废原先已经放置到同步表的数据
			sbSql.append("UPDATE TT_VS_ORDER_INVO SET IS_DEL = '1' WHERE ORDER_NO = '" + tvop.getOrderNo() + "'");
			dao.update(sbSql.toString(), null);
			
			sbSql.delete(0, sbSql.length());
			// 保存开票信息到接口数据
			sbSql.append("INSERT INTO TT_VS_ORDER_INVO\n");
			sbSql.append("  (ID,INVOICE_DEP,DEALER_CODE,DEALER_NAME,ORDER_NO,INVOICE_NO,INVOICE_DATE,INVOICE_NO_VER,INVOICE_REMARK,\n");
			sbSql.append("   S_NAME,P_CODE,G_JSJE,G_JSSE,G_ZKJE,G_ZKSE,DEAL_FLAG,ORDER_FLAG,INVOICE_NUM,FOND_NAME,IS_DEL,G_JSSL,INVO_TYPE)\n");
			sbSql.append("  SELECT F_GETID,A.INVOICE_DEP,C.DEALER_CODE,C.DEALER_NAME,A.ORDER_NO,A.INVOICE_NO,\n");
			sbSql.append("         A.INVOICE_DATE,A.INVOICE_NO_VER,A.INVOICE_REMARK,A.S_NAME,A.P_CODE,A.G_JSJE, A.G_JSSE,A.G_ZKJE,A.G_ZKSE,\n");
			sbSql.append("         '0' AS DEAL_FLAG,\n");
			sbSql.append("         '-1' AS ORDER_FLAG,\n");
			sbSql.append("         A.INVOICE_NUM,\n");
			sbSql.append("         D.CODE_DESC AS FOND_NAME,\n");
			sbSql.append("         '0' AS IS_DEL,\n");
			sbSql.append("         A.G_JSSL,\n");
			sbSql.append("         '01' AS INVO_TYPE\n");
			sbSql.append("    FROM TT_VS_ORDER_INVO A, TT_VS_ORDER B, TM_DEALER C, TC_CODE D\n");
			sbSql.append("   WHERE A.ORDER_NO = B.ORDER_NO\n");
			sbSql.append("     AND B.DEALER_ID = C.DEALER_ID\n");
			sbSql.append("     AND B.FUND_TYPE_ID = D.CODE_ID");
			sbSql.append("     AND B.ORDER_ID = " + tvop.getOrderId());
			
			dao.insert(sbSql.toString());
		}
	}
	
	/**
	 * 方法描述 ： 根据区域统计提车单的财务数据 <br/>
	 * 
	 * @param infoMap
	 * @param curPage
	 * @param pageSize
	 * @return
	 * @author wangsongwei
	 */
	public PageResult<Map<String, Object>> CountFinancialByArea(Map<String, Object> infoMap, Integer curPage, Integer pageSize) throws Exception
	{
		String orderNo = infoMap.get("orderNo").toString();
		String invoNo = infoMap.get("invoNo").toString();
		String areaCode = infoMap.get("areaCode").toString();
		
		// 大区、经销商名称、车系、车型、配置、审核数量、单价、总价、折扣率、提单时间、提车单号
		StringBuffer sbSql = new StringBuffer();
		
		sbSql.append("SELECT BORG.ORG_NAME,\n");
		sbSql.append("       BORG.ORG_ID,\n");
		sbSql.append("       VMGM.SERIES_NAME,\n");
		sbSql.append("       VMGM.MODEL_NAME,\n");
		sbSql.append("       VMGM.PACKAGE_NAME,\n");
		sbSql.append("       NVL(SUM(TVOD.CHECK_AMOUNT), 0) CHECK_AMOUNT, -- 审核数量\n");
		sbSql.append("       NVL(SUM(TVOD.CHK_PRICE), 0) CHK_PRICE, -- 审核单价\n");
		sbSql.append("       NVL(SUM(TVOD.DISCOUNT_RATE), 0) DISCOUNT_RATE, -- 折扣率\n");
		sbSql.append("       NVL(SUM(TVOD.DISCOUNT_S_PRICE), 0) DISCOUNT_S_PRICE, -- 折后单价\n");
		sbSql.append("       NVL(SUM(TVOD.DISCOUNT_PRICE), 0) DISCOUNT_PRICE, -- 折扣额\n");
		sbSql.append("       NVL(SUM(TVOD.TOTAL_PRICE), 0) TOTAL_PRICE -- 折后总价\n");
		sbSql.append("  FROM TM_ORG TORG, TM_ORG BORG, TM_ORG XORG\n");
		sbSql.append("  LEFT JOIN TM_DEALER_ORG_RELATION TORGR ON XORG.ORG_ID = TORGR.ORG_ID\n");
		sbSql.append("  LEFT JOIN TM_DEALER TD ON TORGR.DEALER_ID = TD.DEALER_ID\n");
		sbSql.append("  LEFT JOIN TT_VS_ORDER TVO ON TVO.DEALER_ID = TD.DEALER_ID\n");
		sbSql.append("                           AND TVO.ORDER_STATUS NOT IN (11)\n");
		sbSql.append("  LEFT JOIN TT_VS_ORDER_DETAIL TVOD ON TVOD.ORDER_ID = TVO.ORDER_ID,\n");
		sbSql.append(" VW_MATERIAL_GROUP_MAT VMGM\n");
		sbSql.append("  LEFT JOIN TT_VS_ORDER_DETAIL T1 ON T1.MATERIAL_ID = VMGM.MATERIAL_ID\n");
		sbSql.append(" WHERE TORG.PARENT_ORG_ID = -1\n");
		sbSql.append("   AND BORG.PARENT_ORG_ID = TORG.ORG_ID\n");
		sbSql.append("   AND XORG.PARENT_ORG_ID = BORG.ORG_ID\n");
		
		if (!areaCode.equals(""))
		{
			String[] splitString = areaCode.split(",");
			areaCode = "";
			for (int i = 0; i < splitString.length; i++)
			{
				areaCode += "'" + splitString[i] + "',";
			}
			
			areaCode = areaCode.substring(0, areaCode.length() - 1);
			
			sbSql.append("	AND (XORG.ORG_CODE IN (" + areaCode + ") OR BORG.ORG_CODE IN (" + areaCode + "))");
		}
		
		if (!orderNo.equals(""))
		{
			
		}
		
		sbSql.append(" GROUP BY BORG.ORG_NAME,\n");
		sbSql.append("          BORG.ORG_ID,\n");
		sbSql.append("          VMGM.SERIES_NAME,\n");
		sbSql.append("          VMGM.MODEL_NAME,\n");
		sbSql.append("          VMGM.PACKAGE_NAME\n");
		sbSql.append(" ORDER BY BORG.ORG_ID");
		
		return dao.pageQuery(sbSql.toString(), null, getFunName(), pageSize, curPage);
	}
	
	/**
	 * 方法描述 ： 根据经销商统计提车单的财务数据 <br/>
	 * 
	 * @param infoMap
	 * @param curPage
	 * @param pageSize
	 * @return
	 * @author wangsongwei
	 */
	public PageResult<Map<String, Object>> CountFinancialByDealer(Map<String, Object> infoMap, Integer curPage, Integer pageSize) throws Exception
	{
		String orderNo = CommonUtils.checkNull(infoMap.get("orderNo"));
		String invoNo = CommonUtils.checkNull(infoMap.get("invoNo"));
		String dealerCode = CommonUtils.checkNull(infoMap.get("dealerCode"));
		
		// 大区、经销商名称、车系、车型、配置、审核数量、单价、总价、折扣率、提单时间、提车单号
		StringBuffer sbSql = new StringBuffer();
		
		sbSql.append("SELECT TD.DEALER_ID, -- 经销商ID\\n\");\n");
		sbSql.append("       TD.DEALER_SHORTNAME, -- 经销商简称\\n\");\n");
		sbSql.append("       VMGM.SERIES_NAME, -- 车系名称\n");
		sbSql.append("       VMGM.MODEL_NAME,\n");
		sbSql.append("       VMGM.PACKAGE_NAME,\n");
		sbSql.append("       NVL(SUM(TVOD.CHECK_AMOUNT), 0) CHECK_AMOUNT, -- 审核数量\n");
		sbSql.append("       NVL(SUM(TVOD.CHK_PRICE), 0) CHK_PRICE, -- 审核单价\n");
		sbSql.append("       NVL(SUM(TVOD.DISCOUNT_RATE), 0) DISCOUNT_RATE, -- 折扣率\n");
		sbSql.append("       NVL(SUM(TVOD.DISCOUNT_S_PRICE), 0) DISCOUNT_S_PRICE, -- 折后单价\n");
		sbSql.append("       NVL(SUM(TVOD.DISCOUNT_PRICE), 0) DISCOUNT_PRICE, -- 折扣总额\n");
		sbSql.append("       NVL(SUM(TVOD.TOTAL_PRICE), 0) TOTAL_PRICE -- 折后总价\n");
		sbSql.append("  FROM TM_DEALER TD\n");
		sbSql.append("  LEFT JOIN TT_VS_ORDER TVO ON TD.DEALER_ID = TVO.DEALER_ID\n");
		sbSql.append("                           AND TVO.ORDER_STATUS <> 11\n");
		sbSql.append("  LEFT JOIN TT_VS_ORDER_DETAIL TVOD ON TVO.ORDER_ID = TVOD.ORDER_ID,\n");
		sbSql.append(" VW_MATERIAL_GROUP_MAT VMGM\n");
		sbSql.append("  LEFT JOIN TT_VS_ORDER_DETAIL T1 ON T1.MATERIAL_ID = VMGM.MODEL_ID\n");
		
		if (!dealerCode.equals(""))
		{
			String[] splitString = dealerCode.split(",");
			dealerCode = "";
			for (int i = 0; i < splitString.length; i++)
			{
				dealerCode += "'" + splitString[i] + "',";
			}
			
			dealerCode = dealerCode.substring(0, dealerCode.length() - 1);
			sbSql.append("WHERE TD.DEALER_CODE IN(" + dealerCode + ")");
		}
		
		sbSql.append(" GROUP BY TD.DEALER_ID, -- 经销商ID\n");
		sbSql.append("          TD.DEALER_SHORTNAME, -- 经销商简称\n");
		sbSql.append("          VMGM.SERIES_NAME, -- 车系名称\n");
		sbSql.append("          VMGM.MODEL_NAME,\n");
		sbSql.append("          VMGM.PACKAGE_NAME\n");
		sbSql.append(" ORDER BY TD.DEALER_ID");
		
		return dao.pageQuery(sbSql.toString(), null, getFunName(), pageSize, curPage);
	}
	
	/**
	 * 订单审核通过
	 * 
	 * @param map
	 * @param logonUser
	 * @author wangsongwei
	 */
/*	public void orderAuditPass(Map<String, Object> map, AclUserBean logonUser) throws Exception
	{
		String[] checkAmount = (String[]) map.get("checkAmount");
		String[] materialId = (String[]) map.get("materialId");
		String orderId = CommonUtils.checkNull(map.get("orderId"));
		String orderVer = CommonUtils.checkNull(map.get("orderVer"));
		String remark = CommonUtils.checkNull(map.get("remark"));
		String warehouseCode = CommonUtils.checkNull(map.get("warehouseCode"));
		String[] newMaterialId = (String[])map.get("newMaterialId");
		
		*//**
		 * 在上完选配以后，可能存在物料id为空或者为0的情况，这种情况要限制审核
		 * 检查是否存在没有物料id的明细在审核信息中 
		 *//*
		List<Object> oParams = new ArrayList<Object>();
		oParams.add(orderId);
		StringBuffer sql1 = new StringBuffer("");
		sql1.append("select detail_id \n");
		sql1.append("  from tt_vs_order_detail\n");
		sql1.append(" where order_id = ?\n");
		sql1.append("   and (material_id is null or material_id = 0)"); 
		List<Map<String, Object>> dtlCountList = dao.pageQuery(sql1.toString(), oParams, getFunName());
		if(dtlCountList.size()>0){
			throw new UserException("有资源还在获取选配系统项中,请稍后再审核!");
		}
		
		TtVsOrderPO tvopSqlPo = new TtVsOrderPO();
		tvopSqlPo.setOrderId(Long.parseLong(orderId));
		TtVsOrderPO tvop = (TtVsOrderPO) (dao.select(tvopSqlPo).get(0));
		
		List<Object> params = new ArrayList<Object>();
		StringBuffer sbSql = new StringBuffer();
		
		// 删除资源预留的数据
		params.add(orderId);
		sbSql.append("DELETE FROM TT_VS_ORDER_RESOURCE_RESERVE T\n");
		sbSql.append(" WHERE T.ORDER_DETAIL_ID IN\n");
		sbSql.append("       (SELECT DETAIL_ID FROM TT_VS_ORDER_DETAIL WHERE ORDER_ID = ?)");
		dao.delete(sbSql.toString(), params);
		
		// 根据订单ID查询该订单所有的物料信息（包括资源情况）
		sbSql.delete(0, sbSql.length());
		params.clear();
		if (warehouseCode.equals("8809"))
		{
			sbSql.append("WITH " + AsSqlUtils.getAsSqlVehicleResourceBuffer());
		}
		else
		{
			sbSql.append("WITH " + AsSqlUtils.getAsSqlVehicleResourceByZydBuffer());
		}
		sbSql.append("SELECT A.MATERIAL_ID, NVL(B.VEHICLE_AMOUNT,0) VEHICLE_AMOUNT, A.ORDER_AMOUNT\n");
		sbSql.append("  FROM TT_VS_ORDER_DETAIL A, VEHICLE_STOCK B\n");
		sbSql.append(" WHERE A.MATERIAL_ID = B.MATERIAL_ID(+)\n");
		sbSql.append("   AND A.ORDER_ID = ?");
		params.add(orderId);
		List<Map<String, Object>> materialList = dao.pageQuery(sbSql.toString(), params, getFunName());
		
		// 判断提交的审核数量是否超过了实际的资源数量（防止数据并发导致资源数不足的情况）
		boolean isAll = true;
		for (int i = 0; i < materialList.size(); i++)
		{
			Map<String, Object> data = materialList.get(i);
			String mid = data.get("MATERIAL_ID").toString();
			boolean disabled = true;
			for (int j = 0; j < materialId.length; j++)
			{
				int checkNumber = Integer.parseInt(CommonUtils.checkNullNum(checkAmount[j]));
				if (materialId[j].equals(mid))
				{
					if (checkNumber > Integer.parseInt(data.get("VEHICLE_AMOUNT").toString()))
					{
						throw new UserException("当前订单的资源发生变化,请刷新页面后重新提交!");
					}
					disabled = false; // 有效的物料
					if (checkNumber != Integer.parseInt(data.get("ORDER_AMOUNT").toString()))
					{
						isAll = false; // 未完全分配的物料
						throw new UserException("资源分配数量必须等于提报数量!");
					}
					// 更新所有的物料明细审核数据
					sbSql.delete(0, sbSql.length());
					params.clear();
					params.add(checkAmount[i]);
					params.add(orderId);
					params.add(materialId[i]);
					sbSql.append("UPDATE TT_VS_ORDER_DETAIL SET CHECK_AMOUNT = ? WHERE ORDER_ID = ? AND MATERIAL_ID = ?");
					dao.update(sbSql.toString(), params);
				}
			}
			if (disabled)
				throw new UserException("无效物料信息!");
		}
		
		// 插入资源预留表数据
		sbSql.delete(0, sbSql.length());
		sbSql.append("INSERT INTO TT_VS_ORDER_RESOURCE_RESERVE\n");
		sbSql.append("  (RESERVE_ID,\n");
		sbSql.append("   ORDER_DETAIL_ID,\n");
		sbSql.append("   MATERIAL_ID,\n");
		sbSql.append("   AMOUNT,\n");
		sbSql.append("   CREATE_BY,\n");
		sbSql.append("   CREATE_DATE, WAREHOUSE_CODE)\n");
		sbSql.append("  SELECT F_GETID, DETAIL_ID, MATERIAL_ID, CHECK_AMOUNT, " + logonUser.getUserId() + ", SYSDATE, '" + warehouseCode + "'\n");
		sbSql.append("    FROM TT_VS_ORDER_DETAIL\n");
		sbSql.append("   WHERE ORDER_ID = " + orderId);
		dao.insert(sbSql.toString());
		
		// 如果资源只是部分得到了分配，需要保留资源到资源表并生成BO单
		if (!isAll)
		{
			// 1、如果当前订单不是由经销商提上来的单子，而是由BO单转换，则不允许出现这种不分配完资源的情况
			// 2、如果当前订单不是由BO单转换，则对应生成一个BO单
			if (tvop.getBoOrderId().longValue() != -1L)
				throw new UserException("所有资源必须全部分配才能提交订单!");
			else
			{
				String boOrderId = SequenceManager.getSequence("");
				
				sbSql.delete(0, sbSql.length());
				sbSql.append("INSERT INTO TT_VS_BO_ORDER (BO_ORDER_ID,PRE_ORDER_ID,ORDER_ID,AREA_ID,ORDER_NO,ORDER_TYPE,\n");
				sbSql.append("   RAISE_DATE,DEALER_ID,REC_DEALER_ID,FUND_TYPE_ID,THREE_CREDIT,REC_DEALER_ADD,\n");
				sbSql.append("   IS_HANDOVER,DELIVERY_TYPE,DELIV_ADD_ID,LINK_MAN,TEL,ORDER_REMARK,ORDER_STATUS,VER,CREATE_BY,\n");
				sbSql.append("   CREATE_DATE,UPDATE_BY,UPDATE_DATE,IS_RETAIL,DEALER_NAME,DEALER_SHORTNAME,REC_DEALER_NAME,REC_DEALER_SHORTNAME)\n");
				sbSql.append("SELECT " + boOrderId + ",ORDER_ID AS PRE_ORDER_ID,ORDER_ID,AREA_ID,'" + CommonUtils.getBusNo(Constant.NOCRT_BO_ORDER_NO, Long.valueOf(tvop.getAreaId()))
								+ "',ORDER_TYPE,RAISE_DATE,\n");
				sbSql.append("       DEALER_ID,REC_DEALER_ID,FUND_TYPE_ID,THREE_CREDIT,REC_DEALER_ADD,\n");
				sbSql.append("       IS_HANDOVER,DELIVERY_TYPE,DELIV_ADD_ID,LINK_MAN,TEL,ORDER_REMARK," + Constant.ORDER_STATUS_01 + " AS ORDER_STATUS,VER,\n");
				sbSql.append("       " + logonUser.getUserId() + ",CREATE_DATE,UPDATE_BY,UPDATE_DATE,IS_RETAIL,DEALER_NAME,DEALER_SHORTNAME,REC_DEALER_NAME,REC_DEALER_SHORTNAME\n");
				sbSql.append("  FROM TT_VS_ORDER\n");
				sbSql.append("WHERE ORDER_ID = " + orderId);
				dao.insert(sbSql.toString());
				
				// BO单明细表数据
				sbSql.delete(0, sbSql.length());
				sbSql.append("INSERT INTO TT_VS_BO_ORDER_DETAIL (DETAIL_ID,BO_ORDER_ID,MATERIAL_ID,PRE_MATERIAL_ID,ORDER_AMOUNT,\n");
				sbSql.append("   SINGLE_PRICE,MODEL_PRICE,DEALER_PRICE,DIS_SINGLE_PRICE,TOTAL_PRICE,CREATE_BY,CREATE_DATE)\n");
				sbSql.append("  SELECT DETAIL_ID,\n");
				sbSql.append("         "+boOrderId+",\n");
				sbSql.append("         MATERIAL_ID,\n");
				sbSql.append("         MATERIAL_ID AS PRE_MATERIAL_ID,\n");
				sbSql.append("         NVL(ORDER_AMOUNT,0) - NVL(CHECK_AMOUNT,0) ORDER_AMOUNT,\n");
				sbSql.append("         SINGLE_PRICE,\n");
				sbSql.append("         MODEL_PRICE,\n");
				sbSql.append("         DEALER_PRICE,\n");
				sbSql.append("         DIS_SINGLE_PRICE,\n");
				sbSql.append("         (NVL(ORDER_AMOUNT,0) - NVL(CHECK_AMOUNT,0)) * DIS_SINGLE_PRICE,\n");
				sbSql.append("         " + logonUser.getUserId() + ",\n");
				sbSql.append("         SYSDATE\n");
				sbSql.append("    FROM TT_VS_ORDER_DETAIL");
				sbSql.append("   WHERE NVL(CHECK_AMOUNT,0) <> NVL(ORDER_AMOUNT,0) AND ORDER_ID = " + orderId);
				dao.insert(sbSql.toString());
				
				// 更新BO订单主表数据
				sbSql.delete(0, sbSql.length()); params.clear(); 
				params.add(boOrderId); params.add(boOrderId); params.add(boOrderId);
				sbSql.append("UPDATE TT_VS_BO_ORDER T\n");
				sbSql.append("   SET T.SUB_NUM        = (SELECT SUM(ORDER_AMOUNT)\n");
				sbSql.append("                             FROM TT_VS_BO_ORDER_DETAIL\n");
				sbSql.append("                            WHERE BO_ORDER_ID = ?),\n");
				sbSql.append("       T.ORDER_YF_PRICE = (SELECT SUM(TOTAL_PRICE)\n");
				sbSql.append("                             FROM TT_VS_BO_ORDER_DETAIL\n");
				sbSql.append("                            WHERE BO_ORDER_ID = ?)\n");
				sbSql.append(" WHERE BO_ORDER_ID = ?");
				dao.update(sbSql.toString(), params);
			}
		}
		
		// 更新订单状态
		sbSql.delete(0, sbSql.length());
		params.clear();
		sbSql.append("UPDATE TT_VS_ORDER\n");
		sbSql.append("   SET ORDER_STATUS = ?, VER = NVL(VER,0) + 1, PLAN_CHK_DATE = SYSDATE, WAREHOUSE_CODE = ?,\n");
		params.add(Constant.ORDER_STATUS_04);
		params.add(warehouseCode);
		if (!remark.equals(""))
		{
			sbSql.append("   PLAN_CHK_REMARK = ?,\n");
			params.add(remark);
		}
		sbSql.append("       CHK_NUM      = (SELECT SUM(CHECK_AMOUNT)\n");
		sbSql.append("                         FROM TT_VS_ORDER_DETAIL\n");
		sbSql.append("                        WHERE ORDER_ID = ?)\n");
		sbSql.append(" WHERE ORDER_ID = ? AND VER = ?");
		
		params.add(orderId);
		params.add(orderId);
		params.add(orderVer);
		int updateStatus = dao.update(sbSql.toString(), params);
		
		if (updateStatus != 1)
			throw new UserException("订单状态已更新,请刷新页面后重试!");
		
		// 保存审核历史记录
		dao.saveCheckResult(Long.parseLong(orderId), remark, Constant.FLEET_SUPPORT_CHECK_STATUS_01, Constant.OUT_ORDER_AUDIT_TYPE_01, logonUser);
	}*/
	

	/**
	 * 订单审核通过--网销流程
	 */
/*	public void orderAuditPassInternetSales(Map<String, Object> map, AclUserBean logonUser) throws Exception
	{
		String[] checkAmount = (String[]) map.get("checkAmount");
		String[] materialId = (String[]) map.get("materialId");
		String orderId = CommonUtils.checkNull(map.get("orderId"));
		String orderVer = CommonUtils.checkNull(map.get("orderVer"));
		String recDealerId = CommonUtils.checkNull(map.get("recDealerId"));
		String deliveryAddress = CommonUtils.checkNull(map.get("deliveryAddress"));
		String linkMan = CommonUtils.checkNull(map.get("linkMan"));
		String tel = CommonUtils.checkNull(map.get("tel"));
		String remark = CommonUtils.checkNull(map.get("remark"));
		String warehouseCode = CommonUtils.checkNull(map.get("warehouseCode"));
		String[] newMaterialId = (String[])map.get("newMaterialId");
		
		*//**
		 * 在上完选配以后，可能存在物料id为空或者为0的情况，这种情况要限制审核
		 * 检查是否存在没有物料id的明细在审核信息中 
		 *//*
		List<Object> oParams = new ArrayList<Object>();
		oParams.add(orderId);
		StringBuffer sql1 = new StringBuffer("");
		sql1.append("select detail_id \n");
		sql1.append("  from tt_vs_order_detail\n");
		sql1.append(" where order_id = ?\n");
		sql1.append("   and (material_id is null or material_id = 0)"); 
		List<Map<String, Object>> dtlCountList = dao.pageQuery(sql1.toString(), oParams, getFunName());
		if(dtlCountList.size()>0){
			throw new UserException("有资源还在获取选配系统项中,请稍后再审核!");
		}
		
		TtVsOrderPO tvopSqlPo = new TtVsOrderPO();
		tvopSqlPo.setOrderId(Long.parseLong(orderId));
		TtVsOrderPO tvop = (TtVsOrderPO) (dao.select(tvopSqlPo).get(0));
		
		List<Object> params = new ArrayList<Object>();
		StringBuffer sbSql = new StringBuffer();
		
		// 删除资源预留的数据
		params.add(orderId);
		sbSql.append("DELETE FROM TT_VS_ORDER_RESOURCE_RESERVE T\n");
		sbSql.append(" WHERE T.ORDER_DETAIL_ID IN\n");
		sbSql.append("       (SELECT DETAIL_ID FROM TT_VS_ORDER_DETAIL WHERE ORDER_ID = ?)");
		dao.delete(sbSql.toString(), params);
		
		// 根据订单ID查询该订单所有的物料信息（包括资源情况）
		sbSql.delete(0, sbSql.length());
		params.clear();
		if (warehouseCode.equals("8809"))
		{
			sbSql.append("WITH " + AsSqlUtils.getAsSqlVehicleResourceBuffer());
		}
		else
		{
			sbSql.append("WITH " + AsSqlUtils.getAsSqlVehicleResourceByZydBuffer());
		}
		sbSql.append("SELECT A.MATERIAL_ID, NVL(B.VEHICLE_AMOUNT,0) VEHICLE_AMOUNT, A.ORDER_AMOUNT\n");
		sbSql.append("  FROM TT_VS_ORDER_DETAIL A, VEHICLE_STOCK B\n");
		sbSql.append(" WHERE A.MATERIAL_ID = B.MATERIAL_ID(+)\n");
		sbSql.append("   AND A.ORDER_ID = ?");
		params.add(orderId);
		List<Map<String, Object>> materialList = dao.pageQuery(sbSql.toString(), params, getFunName());
		
		// 判断提交的审核数量是否超过了实际的资源数量（防止数据并发导致资源数不足的情况）
		boolean isAll = true;
		for (int i = 0; i < materialList.size(); i++)
		{
			Map<String, Object> data = materialList.get(i);
			String mid = data.get("MATERIAL_ID").toString();
			boolean disabled = true;
			for (int j = 0; j < materialId.length; j++)
			{
				int checkNumber = Integer.parseInt(CommonUtils.checkNullNum(checkAmount[j]));
				if (materialId[j].equals(mid))
				{
					if (checkNumber > Integer.parseInt(data.get("VEHICLE_AMOUNT").toString()))
					{
						throw new UserException("当前订单的资源发生变化,请刷新页面后重新提交!");
					}
					disabled = false; // 有效的物料
					if (checkNumber != Integer.parseInt(data.get("ORDER_AMOUNT").toString()))
					{
						isAll = false; // 未完全分配的物料
						throw new UserException("资源分配数量必须等于提报数量!");
					}
					// 更新所有的物料明细审核数据
					sbSql.delete(0, sbSql.length());
					params.clear();
					params.add(checkAmount[i]);
					params.add(orderId);
					params.add(materialId[i]);
					sbSql.append("UPDATE TT_VS_ORDER_DETAIL SET CHECK_AMOUNT = ? WHERE ORDER_ID = ? AND MATERIAL_ID = ?");
					dao.update(sbSql.toString(), params);
				}
			}
			if (disabled)
				throw new UserException("无效物料信息!");
		}
		
		// 插入资源预留表数据
		sbSql.delete(0, sbSql.length());
		sbSql.append("INSERT INTO TT_VS_ORDER_RESOURCE_RESERVE\n");
		sbSql.append("  (RESERVE_ID,\n");
		sbSql.append("   ORDER_DETAIL_ID,\n");
		sbSql.append("   MATERIAL_ID,\n");
		sbSql.append("   AMOUNT,\n");
		sbSql.append("   CREATE_BY,\n");
		sbSql.append("   CREATE_DATE, WAREHOUSE_CODE)\n");
		sbSql.append("  SELECT F_GETID, DETAIL_ID, MATERIAL_ID, CHECK_AMOUNT, " + logonUser.getUserId() + ", SYSDATE, '" + warehouseCode + "'\n");
		sbSql.append("    FROM TT_VS_ORDER_DETAIL\n");
		sbSql.append("   WHERE ORDER_ID = " + orderId);
		dao.insert(sbSql.toString());
		
		// 如果资源只是部分得到了分配，需要保留资源到资源表并生成BO单
		if (!isAll)
		{
			// 1、如果当前订单不是由经销商提上来的单子，而是由BO单转换，则不允许出现这种不分配完资源的情况
			// 2、如果当前订单不是由BO单转换，则对应生成一个BO单
			if (tvop.getBoOrderId().longValue() != -1L)
				throw new UserException("所有资源必须全部分配才能提交订单!");
			else
			{
				String boOrderId = SequenceManager.getSequence("");
				
				sbSql.delete(0, sbSql.length());
				sbSql.append("INSERT INTO TT_VS_BO_ORDER (BO_ORDER_ID,PRE_ORDER_ID,ORDER_ID,AREA_ID,ORDER_NO,ORDER_TYPE,\n");
				sbSql.append("   RAISE_DATE,DEALER_ID,REC_DEALER_ID,FUND_TYPE_ID,THREE_CREDIT,REC_DEALER_ADD,\n");
				sbSql.append("   IS_HANDOVER,DELIVERY_TYPE,DELIV_ADD_ID,LINK_MAN,TEL,ORDER_REMARK,ORDER_STATUS,VER,CREATE_BY,\n");
				sbSql.append("   CREATE_DATE,UPDATE_BY,UPDATE_DATE,IS_RETAIL,DEALER_NAME,DEALER_SHORTNAME,REC_DEALER_NAME,REC_DEALER_SHORTNAME)\n");
				sbSql.append("SELECT " + boOrderId + ",ORDER_ID AS PRE_ORDER_ID,ORDER_ID,AREA_ID,'" + CommonUtils.getBusNo(Constant.NOCRT_BO_ORDER_NO, Long.valueOf(tvop.getAreaId()))
								+ "',ORDER_TYPE,RAISE_DATE,\n");
				sbSql.append("       DEALER_ID,REC_DEALER_ID,FUND_TYPE_ID,THREE_CREDIT,REC_DEALER_ADD,\n");
				sbSql.append("       IS_HANDOVER,DELIVERY_TYPE,DELIV_ADD_ID,LINK_MAN,TEL,ORDER_REMARK," + Constant.ORDER_STATUS_01 + " AS ORDER_STATUS,VER,\n");
				sbSql.append("       " + logonUser.getUserId() + ",CREATE_DATE,UPDATE_BY,UPDATE_DATE,IS_RETAIL,DEALER_NAME,DEALER_SHORTNAME,REC_DEALER_NAME,REC_DEALER_SHORTNAME\n");
				sbSql.append("  FROM TT_VS_ORDER\n");
				sbSql.append("WHERE ORDER_ID = " + orderId);
				dao.insert(sbSql.toString());
				
				// BO单明细表数据
				sbSql.delete(0, sbSql.length());
				sbSql.append("INSERT INTO TT_VS_BO_ORDER_DETAIL (DETAIL_ID,BO_ORDER_ID,MATERIAL_ID,PRE_MATERIAL_ID,ORDER_AMOUNT,\n");
				sbSql.append("   SINGLE_PRICE,MODEL_PRICE,DEALER_PRICE,DIS_SINGLE_PRICE,TOTAL_PRICE,CREATE_BY,CREATE_DATE)\n");
				sbSql.append("  SELECT DETAIL_ID,\n");
				sbSql.append("         "+boOrderId+",\n");
				sbSql.append("         MATERIAL_ID,\n");
				sbSql.append("         MATERIAL_ID AS PRE_MATERIAL_ID,\n");
				sbSql.append("         NVL(ORDER_AMOUNT,0) - NVL(CHECK_AMOUNT,0) ORDER_AMOUNT,\n");
				sbSql.append("         SINGLE_PRICE,\n");
				sbSql.append("         MODEL_PRICE,\n");
				sbSql.append("         DEALER_PRICE,\n");
				sbSql.append("         DIS_SINGLE_PRICE,\n");
				sbSql.append("         (NVL(ORDER_AMOUNT,0) - NVL(CHECK_AMOUNT,0)) * DIS_SINGLE_PRICE,\n");
				sbSql.append("         " + logonUser.getUserId() + ",\n");
				sbSql.append("         SYSDATE\n");
				sbSql.append("    FROM TT_VS_ORDER_DETAIL");
				sbSql.append("   WHERE NVL(CHECK_AMOUNT,0) <> NVL(ORDER_AMOUNT,0) AND ORDER_ID = " + orderId);
				dao.insert(sbSql.toString());
				
				// 更新BO订单主表数据
				sbSql.delete(0, sbSql.length()); params.clear(); 
				params.add(boOrderId); params.add(boOrderId); params.add(boOrderId);
				sbSql.append("UPDATE TT_VS_BO_ORDER T\n");
				sbSql.append("   SET T.SUB_NUM        = (SELECT SUM(ORDER_AMOUNT)\n");
				sbSql.append("                             FROM TT_VS_BO_ORDER_DETAIL\n");
				sbSql.append("                            WHERE BO_ORDER_ID = ?),\n");
				sbSql.append("       T.ORDER_YF_PRICE = (SELECT SUM(TOTAL_PRICE)\n");
				sbSql.append("                             FROM TT_VS_BO_ORDER_DETAIL\n");
				sbSql.append("                            WHERE BO_ORDER_ID = ?)\n");
				sbSql.append(" WHERE BO_ORDER_ID = ?");
				dao.update(sbSql.toString(), params);
			}
		}
		
		// 更新订单状态
		sbSql.delete(0, sbSql.length());
		params.clear();
		sbSql.append("UPDATE TT_VS_ORDER\n");
		sbSql.append("   SET ORDER_STATUS = ?, VER = NVL(VER,0) + 1, PLAN_CHK_DATE = SYSDATE, WAREHOUSE_CODE = ?,\n");
		params.add(Constant.ORDER_STATUS_04);
		params.add(warehouseCode);
		if (!remark.equals(""))
		{
			sbSql.append("   PLAN_CHK_REMARK = ?,\n");
			params.add(remark);
		}
		sbSql.append("       CHK_NUM      = (SELECT SUM(CHECK_AMOUNT)\n");
		sbSql.append("                         FROM TT_VS_ORDER_DETAIL\n");
		sbSql.append("                        WHERE ORDER_ID = ?),\n");
		params.add(orderId);
		sbSql.append("  REC_DEALER_ID=?,DELIV_ADD_ID=?,LINK_MAN=?,TEL=?,");
		params.add(recDealerId);
		params.add(deliveryAddress);
		params.add(linkMan);
		params.add(tel);
		sbSql.append("  REC_DEALER_NAME=(SELECT TD.DEALER_NAME FROM TM_DEALER TD WHERE TD.DEALER_ID=").append(recDealerId).append("),");
		sbSql.append("  REC_DEALER_SHORTNAME=(SELECT TD.DEALER_SHORTNAME FROM TM_DEALER TD WHERE TD.DEALER_ID=").append(recDealerId).append(")");
		sbSql.append(" WHERE ORDER_ID = ? AND VER = ?");
		params.add(orderId);
		params.add(orderVer);
		int updateStatus = dao.update(sbSql.toString(), params);
		
		if (updateStatus != 1)
			throw new UserException("订单状态已更新,请刷新页面后重试!");
		
		// 保存审核历史记录
		dao.saveCheckResult(Long.parseLong(orderId), remark, Constant.FLEET_SUPPORT_CHECK_STATUS_01, Constant.OUT_ORDER_AUDIT_TYPE_01, logonUser);
	}*/
	
	/**
	 * 订单拆分逻辑判断
	 */
/*	public String splitOrderAudit(Map<String, Object> map, AclUserBean logonUser)throws Exception{
		String[] checkAmount = (String[])map.get("checkAmount");
		String[] materialId = (String[])map.get("materialId");
		String orderId = CommonUtils.checkNull(map.get("orderId"));
		String orderVer = CommonUtils.checkNull(map.get("orderVer"));
		String remark = CommonUtils.checkNull(map.get("remark"));
		String warehouseCode = CommonUtils.checkNull(map.get("warehouseCode"));
		String[] newMaterialId = (String[])map.get("newMaterialId");
		String[] subAmount = (String[])map.get("subAmount");
		String[] detailId = (String[])map.get("detailId");
		String[] xpCode = (String[])map.get("xpCode");
		String[] newXpCode = (String[])map.get("newXpCode");
		String[] newXpName = (String[])map.get("newXpName");
		String[] packageId = (String[])map.get("packageId");
		if(null==newMaterialId){
			throw new UserException("当前订单未拆分,请刷新页面后重新拆分提交!");
		}else {
				if(newMaterialId.length==0){
					throw new UserException("当前订单未拆分,请刷新页面后重新拆分提交!");
			}
		}
		if(CommonUtils.array_unique(newXpCode).length!=materialId.length){//H2存在同一个选配码(颜色)有多个配置
			if(CommonUtils.array_unique(newMaterialId).length!=materialId.length){//如果新旧物料数量有重复的
				throw new UserException("当前订单多个物料不能有相同物料,请刷新页面后重新拆分提交!");
			}
		}
		boolean isRepeat = false;
		for (int i = 0; i < packageId.length - 1; i++)
		{
			String m1 = packageId[i];
			for (int j = i + 1; j < packageId.length; j++)
			{
				if (m1.equals(packageId[j]))
				{
					if (CommonUtils.checkNull(newXpCode[i]).equals(CommonUtils.checkNull(newXpCode[j])))
					{
						isRepeat = true;
						break;
					}
				}
			}
		}
		if (isRepeat)
			throw new UserException("不能出现同一配置同一选配的情况!");
		//return splitCheck(logonUser,newMaterialId,materialId,orderId,warehouseCode,checkAmount,subAmount,remark,orderVer);
		return splitOrder(logonUser,newMaterialId,materialId,orderId,
				warehouseCode,checkAmount,subAmount,remark,orderVer,detailId,xpCode,newXpCode,newXpName,packageId);
	}*/
	/**
	 * 开始拆分订单信息
	 * @param logonUser
	 * @param newMaterial
	 * @param oldMaterial
	 * @param orderId
	 * @param warehouseCode
	 * @param checkAmount
	 * @param subAmount
	 * @param remark
	 * @param orderVer
	 * @param detailId
	 * @return
	 */
/*	public String splitOrder(AclUserBean logonUser,String[] newMaterial,String[] oldMaterial,String orderId,
			String warehouseCode,String[] checkAmount,String[] subAmount,String remark,String orderVer,String[] detailId,
			String[] xpCode,String[] newXpCode,String[] newXpName,String[] packageId){
		//判断每个行信息，如果颜色未变并且数量未变，则该行不需要分单
		//判断分单的行信息，是否每行都分单，如果每行都分单则判断是否存在某行是非全量分单
		
		//先检查是否所有新的物料颜色都提交拆分
		HashMap<String,String> materialMap = new HashMap<String,String>();
		HashMap<String,String> newMaterialMap = new HashMap<String,String>();
		HashMap<String,String> checkAmountMap = new HashMap<String,String>();
		HashMap<String,String> subAmountMap = new HashMap<String,String>();
		HashMap<String,String> detailIdMap = new HashMap<String,String>();
		HashMap<String,String> xpMap = new HashMap<String,String>();
		HashMap<String,String> oldXpMap = new HashMap<String,String>();
		HashMap<String,String> xpName = new HashMap<String,String>();
		boolean splitAllFlag = true;
		for(int i=0;i<xpCode.length;i++){
			xpCode[i] = packageId[i]+"@"+xpCode[i];
			newXpCode[i] = packageId[i]+"@"+newXpCode[i];
		}
		//如果该标识为假则更新订单主表;如果为真则需要判断最后哪个资源留在原订单
		if(newMaterial.length==oldMaterial.length){
			for(int i=0;i<newMaterial.length;i++){
				if((newMaterial[i].equals(oldMaterial[i]) && subAmount[i].equals(checkAmount[i])) || "0".equals(checkAmount[i])){
					//如果新旧两组物料id一致并且该行审核数和提报数一致则该行不需要分单
					//或者审核数量为0,该行物料也不需要分单
					splitAllFlag = false;
					continue;
				}else{
					//记录拆分的订单物料信息和拆分信息 Cn
					materialMap.put(oldMaterial[i], newMaterial[i]);//将旧新物料放入map
					checkAmountMap.put(newMaterial[i], checkAmount[i]);//将分单物料数量放入map
					subAmountMap.put(newMaterial[i], subAmount[i]);		//将提报数量放入map
					newMaterialMap.put(newMaterial[i], oldMaterial[i]); //新旧物料放入map
					detailIdMap.put(oldMaterial[i], detailId[i]);
					*//**
					 * 未上选配系统之前的分单参数设置
					 *//*
					oldXpMap.put(oldMaterial[i], xpCode[i]); //新选配项对照旧物料表
					xpMap.put(xpCode[i], newXpCode[i]);   //新旧选配对照表
					xpName.put(newXpCode[i], newXpName[i]);
					checkAmountMap.put(newXpCode[i], checkAmount[i]);//将分单物料数量放入map
					subAmountMap.put(newXpCode[i], subAmount[i]);		//将提报数量放入map
					newMaterialMap.put(newXpCode[i], oldMaterial[i]); //新旧物料放入map
					detailIdMap.put(oldMaterial[i], detailId[i]);
					if(!subAmount[i].equals(checkAmount[i])){//判断是否存在分单
						splitAllFlag = false;
					}
				}
			}
		}
		if(!splitAllFlag){//存在不需要分单的物料行信息，保留有原始订单数据
			//更新
		}else{//更新订单明细数据即可,不需要新增订单和明细
			
		}
		return doXPSplit(logonUser, materialMap,newMaterialMap,
				checkAmountMap,subAmountMap,splitAllFlag,
				orderId,orderVer,remark,detailIdMap,xpMap,oldXpMap,xpName
				);
	}*/
	
	/**
	 * 根据拆单标识拆分订单
	 * @param logonUser
	 * @param materialMap
	 * @param newMaterialMap
	 * @param checkAmountMap
	 * @param subAmountMap
	 * @param flag
	 * @param orderId
	 * @param orderVer
	 * @param remark
	 * @param detailIdMap
	 * @return
	 */
/*	public String doXPSplit(AclUserBean logonUser,HashMap<String,String> materialMap,HashMap<String,String> newMaterialMap,
			HashMap<String,String> checkAmountMap,HashMap<String,String> subAmountMap,boolean flag,
			String orderId,String orderVer,String remark,HashMap<String,String> detailIdMap,HashMap<String,String> xpMap,
			HashMap<String,String> oldXpMap,HashMap<String,String> xpName
			){
		StringBuffer res = new StringBuffer("订单");
		if(materialMap.size()>0){
			TtVsOrderPO tvopSqlPo = new TtVsOrderPO();
			tvopSqlPo.setOrderId(Long.parseLong(orderId));
			TtVsOrderPO tvop = (TtVsOrderPO)(dao.select(tvopSqlPo).get(0));
			//该PO用于更新老订单的主数据 
			TtVsOrderPO oldOrderPO = new TtVsOrderPO();
			if(!orderVer.equals(tvop.getVer().toString())){
				throw new UserException("当前订单版本号发生变更,请刷新页面后重新分单!");
			}
			res.append(tvop.getOrderNo()).append("更新信息如下:");
			saveOldOrderInfo(tvop);
			saveOldOrderDtlInfo(tvop.getOrderId().toString());
			
			Iterator iter = materialMap.entrySet().iterator();//遍历物料map
			int icount = materialMap.size();
			int i = 0;
			Double orderYfPrice = tvop.getOrderYfPrice();
			int subNum = tvop.getSubNum();
			int ver = 0;
			if(null!=tvop.getVer()){
				ver = tvop.getVer();
			}
			Integer index = 0;
			Long newOrderId = null;
			*//**
			 * materialMap.put(oldMaterial[i], newMaterial[i]);//将旧新物料放入map
				oldXpMap.put(oldMaterial[i], xpCode[i]); //新选配项对照旧物料表
				xpMap.put(xpCode[i], newXpCode[i]);   //新旧选配对照表
				checkAmountMap.put(newXpCode[i], checkAmount[i]);//将分单物料数量放入map
				subAmountMap.put(newXpCode[i], subAmount[i]);		//将提报数量放入map
				newMaterialMap.put(newXpCode[i], oldMaterial[i]); //新旧物料放入map
				detailIdMap.put(oldMaterial[i], detailId[i]);
			 *//*
			String packageId = "0";
			while (iter.hasNext()) {
				Map.Entry entry = (Map.Entry) iter.next();
				String key = (entry.getKey()==null)?"":entry.getKey().toString();
				String val = (entry.getValue()==null)?"":entry.getValue().toString();
				
				Object oldXpCode = oldXpMap.get(key);
				String newXpCode = xpMap.get(oldXpCode);
				String xpNameKey = xpMap.get(oldXpCode);
				String ckAmount = checkAmountMap.get(newXpCode);
				int ickAmout = Integer.parseInt(ckAmount);
				String subAmount1 = subAmountMap.get(newXpCode);
				int isubAmount = Integer.parseInt(subAmount1);
				String[] newLineInfo = newXpCode.split("@");
				String[] oldLineInfo = oldXpCode.toString().split("@");
				packageId = oldLineInfo[0];
				oldXpCode = oldLineInfo[1];
				newXpCode = newLineInfo[1];
				i++;
				if(flag){//整单改资源颜色即可
					TtVsOrderDetailPO tVDtlPo = new TtVsOrderDetailPO();
					tVDtlPo.setOrderId(Long.parseLong(orderId));
					tVDtlPo.setDetailId(Long.parseLong(detailIdMap.get(key)));
					tVDtlPo.setMaterialId(Long.parseLong(key));
					TtVsOrderDetailPO tvopd = (TtVsOrderDetailPO)(dao.select(tVDtlPo).get(0));
					TtVsOrderDetailPO tmpVOD = new TtVsOrderDetailPO();
					tmpVOD = convertDtlPo(tmpVOD,tvopd);
					if(null != val && !val.equals(""))
						tmpVOD.setMaterialId(Long.parseLong(val));
					else{
						//如果没找到对应的物料id则设置物料id为空且设置状态为未同步tms
						tmpVOD.setXpSendStatus("0");
						//tmpVOD.setMaterialId(0L);
						tmpVOD.setMaterialId(0L);
					}
					tmpVOD.setXpCode(newXpCode.toString());
					tmpVOD.setXpName(xpName.get(xpNameKey));
					tmpVOD.setColorCode(newXpCode.substring(newXpCode.length()-5, newXpCode.length()));
					//获取选配项的名称??
					XpKxztsjPO xpPO = new XpKxztsjPO();
					//xpPO.setKsid(ksid);
					xpPO.setItcode(tmpVOD.getColorCode());
					xpPO.setBigcode("09");
					xpPO.setIccode("97");
					List<PO> xpList = dao.select(xpPO);
					XpKxztsjPO tXpPO = (XpKxztsjPO)xpList.get(0);
					tmpVOD.setColorName(tXpPO.getItname());
					
					tmpVOD.setUpdateBy(logonUser.getUserId());
					tmpVOD.setUpdateDate(new Date());
					if(i<icount){
						orderYfPrice = orderYfPrice-ickAmout*tmpVOD.getDisSinglePrice();
						subNum = subNum-ickAmout;
						//新订单数据
						//更新订单明细表的订单ID和订单明细表的物料ID
						tmpVOD.setDetailId(Long.parseLong(SequenceManager.getSequence("")));
						TtVsOrderPO tmpTVOP = new TtVsOrderPO();
						tmpTVOP = convertOrderPo(tmpTVOP,tvop);//订单主数据
						if(newOrderId==null)
							index = getOrderIndex(tvop.getOrderId());
						tmpTVOP.setOrderNo(tvop.getOrderNo()+"C"+index.toString());//分单订单号
						tmpTVOP.setChkNum(0);//审核数量0
						tmpTVOP.setSubNum(ickAmout);
						tmpTVOP.setOrderYfPrice(ickAmout*tmpVOD.getDisSinglePrice());
						tmpTVOP.setUpdateBy(logonUser.getUserId());
						tmpTVOP.setUpdateDate(new Date());
						
						if(null !=remark && !"".equals(remark))
							tmpTVOP.setOrderRemark(remark);
						tmpVOD.setOrderId(tmpTVOP.getOrderId());
						=====================================================
						//将拆分的订单合并到一个订单里面，如果需要每行一个订单就去掉这里的逻辑判断
						if(newOrderId != null){
							TtVsOrderPO tvOrderPO = new TtVsOrderPO();
							tvOrderPO.setOrderId(newOrderId);
							List<PO> t = dao.select(tvOrderPO);
							TtVsOrderPO tPO = (TtVsOrderPO)t.get(0); 
							tmpTVOP.setOrderYfPrice(tPO.getOrderYfPrice()+tmpTVOP.getOrderYfPrice());
							tmpTVOP.setSubNum(tPO.getSubNum()+tmpTVOP.getSubNum());
							tmpTVOP.setOrderId(newOrderId);
							dao.update(tvOrderPO, tmpTVOP);
						}else{
							tmpTVOP.setOrderId(Long.parseLong(SequenceManager.getSequence("")));
							dao.insert(tmpTVOP);
							newOrderId = tmpTVOP.getOrderId();
							res.append(tmpTVOP.getOrderNo()).append(";");
						}
						=====================================================
						//更新明细表物料信息
						dao.update(tVDtlPo, tmpVOD);
						//写入分单明细信息
						setSplitRelation(tvop.getOrderId(),tmpTVOP.getOrderId(),key,
								val,ickAmout,tmpTVOP.getOrderNo(),tvop.getOrderNo(),index,
								remark,tmpVOD.getDisSinglePrice(),logonUser);
						
					}else{//如果是最后一行数据，保留一个物料信息给老的订单
						oldOrderPO.setOrderYfPrice(orderYfPrice);
						oldOrderPO.setSubNum(subNum);
						oldOrderPO.setVer(ver+1);
						oldOrderPO.setUpdateBy(logonUser.getUserId());
						oldOrderPO.setUpdateDate(new Date());
						if(null !=remark && !"".equals(remark))
							oldOrderPO.setOrderRemark(remark);
						dao.update(tVDtlPo, tmpVOD);//更新订单明细的提报数和物料数据
						dao.update(tvopSqlPo, oldOrderPO);//更新原始订单数据
					}
					res.append("更新物料信息成功,未产生新的订单号");
				}else{//不规则分单信息
					//明细数据
					TtVsOrderDetailPO tVDtlPo = new TtVsOrderDetailPO();
					tVDtlPo.setOrderId(Long.parseLong(orderId));
					tVDtlPo.setMaterialId(Long.parseLong(key));
					tVDtlPo.setDetailId(Long.parseLong(detailIdMap.get(key)));
					TtVsOrderDetailPO tvopd = (TtVsOrderDetailPO)(dao.select(tVDtlPo).get(0));
					TtVsOrderDetailPO tmpVOD = new TtVsOrderDetailPO();
					tmpVOD = convertDtlPo(tmpVOD,tvopd);
					if(null != val && !val.equals(""))
						tmpVOD.setMaterialId(Long.parseLong(val));
					else{
						//如果没找到对应的物料id则设置物料id为空且设置状态为未同步tms
						tmpVOD.setXpSendStatus("0");
						//tmpVOD.setMaterialId(0L);
						tmpVOD.setMaterialId(0L);
					}
					tmpVOD.setXpCode(newXpCode.toString());
					tmpVOD.setXpName(xpName.get(xpNameKey));
					tmpVOD.setColorCode(newXpCode.substring(newXpCode.length()-5, newXpCode.length()));
					//获取选配项的名称??
					//tmpVOD.setMaterialId(Long.parseLong(val.toString()));
					XpKxztsjPO xpPO = new XpKxztsjPO();
					xpPO.setItcode(tmpVOD.getColorCode());
					xpPO.setBigcode("09");
					xpPO.setIccode("97");
					List<PO> xpList = dao.select(xpPO);
					XpKxztsjPO tXpPO = (XpKxztsjPO)xpList.get(0);
					tmpVOD.setColorName(tXpPO.getItname());
					tmpVOD.setUpdateBy(logonUser.getUserId());
					tmpVOD.setUpdateDate(new Date());
					tmpVOD.setOrderAmount(ickAmout);
					//主数据
					TtVsOrderPO tmpTVOP = new TtVsOrderPO();
					tmpTVOP = convertOrderPo(tmpTVOP,tvop);//订单主数据
					//tmpTVOP.setOrderId(Long.parseLong(SequenceManager.getSequence("")));
					if(newOrderId==null)
						index = getOrderIndex(tvop.getOrderId());
					tmpTVOP.setOrderNo(tvop.getOrderNo()+"C"+index.toString());//分单订单号
					tmpTVOP.setChkNum(0);//审核数量0
					tmpTVOP.setSubNum(ickAmout);
					tmpTVOP.setOrderYfPrice(ickAmout*tmpVOD.getDisSinglePrice());
					tmpTVOP.setUpdateBy(logonUser.getUserId());
					tmpTVOP.setUpdateDate(new Date());
					tmpTVOP.setVer(0);
					if(ickAmout==isubAmount){//明细表行数据如果分单数和提报数一样,则更新物料信息和订单id
						subNum = subNum-ickAmout;
						orderYfPrice = orderYfPrice-ickAmout*tmpVOD.getDisSinglePrice();
						=====================================================
						//将拆分的订单合并到一个订单里面，如果需要每行一个订单就去掉这里的逻辑判断
						if(newOrderId != null){
							TtVsOrderPO tvOrderPO = new TtVsOrderPO();
							tvOrderPO.setOrderId(newOrderId);
							List<PO> t = dao.select(tvOrderPO);
							TtVsOrderPO tPO = (TtVsOrderPO)t.get(0); 
							tmpTVOP.setOrderYfPrice(tPO.getOrderYfPrice()+tmpTVOP.getOrderYfPrice());
							tmpTVOP.setSubNum(tPO.getSubNum()+tmpTVOP.getSubNum());
							tmpTVOP.setOrderId(newOrderId);
							dao.update(tvOrderPO, tmpTVOP);
						}else{
							tmpTVOP.setOrderId(Long.parseLong(SequenceManager.getSequence("")));
							dao.insert(tmpTVOP);
							newOrderId = tmpTVOP.getOrderId();
							res.append(tmpTVOP.getOrderNo()).append(";");
						}
						=====================如果需要每行一个分单的订单则放开下面注释================================
						//dao.insert(tmpTVOP);//插入订单主数据
						tmpVOD.setOrderId(tmpTVOP.getOrderId());
						dao.update(tVDtlPo, tmpVOD);//更新明细数据
						//写入分单明细信息,保留新订单和原始订单的关系
						setSplitRelation(tvop.getOrderId(),tmpTVOP.getOrderId(),key,
								val,ickAmout,tmpTVOP.getOrderNo(),tvop.getOrderNo(),index,
								remark,tmpVOD.getDisSinglePrice(),logonUser);
					}else{//如果是部分分单,则插入新的订单明细数据和订单主数据,更新订单主数据
						subNum = subNum-ickAmout;
						orderYfPrice = orderYfPrice-ickAmout*tmpVOD.getDisSinglePrice();
						
						TtVsOrderDetailPO oldDtl = new TtVsOrderDetailPO();
						oldDtl.setOrderAmount(tvopd.getOrderAmount()-ickAmout);
						oldDtl.setMaterialId(tVDtlPo.getMaterialId());
						dao.update(tVDtlPo, oldDtl);//更新订单明细表老物料数据
						=====================================================
						//将拆分的订单合并到一个订单里面，如果需要每行一个订单就去掉这里的逻辑判断
						if(newOrderId != null){
							TtVsOrderPO tvOrderPO = new TtVsOrderPO();
							tvOrderPO.setOrderId(newOrderId);
							List<PO> t = dao.select(tvOrderPO);
							TtVsOrderPO tPO = (TtVsOrderPO)t.get(0); 
							tmpTVOP.setOrderYfPrice(tPO.getOrderYfPrice()+tmpTVOP.getOrderYfPrice());
							tmpTVOP.setSubNum(tPO.getSubNum()+tmpTVOP.getSubNum());
							tmpTVOP.setOrderId(newOrderId);
							dao.update(tvOrderPO, tmpTVOP);
						}else{
							tmpTVOP.setOrderId(Long.parseLong(SequenceManager.getSequence("")));
							dao.insert(tmpTVOP);
							newOrderId = tmpTVOP.getOrderId();
							res.append(tmpTVOP.getOrderNo()).append(";");
						}
						=====================如果需要每行一个分单的订单则放开下面注释================================
						//dao.insert(tmpTVOP);
						tmpVOD.setOrderId(tmpTVOP.getOrderId());
						tmpVOD.setDetailId(Long.parseLong(SequenceManager.getSequence("")));
						dao.insert(tmpVOD);
						//写入分单明细信息,保留新订单和原始订单的关系
						setSplitRelation(tvop.getOrderId(),tmpTVOP.getOrderId(),key,
								val,ickAmout,tmpTVOP.getOrderNo(),tvop.getOrderNo(),index,
								remark,tmpVOD.getDisSinglePrice(),logonUser);
					}
					if(i==icount){
						//如果是最后一行数据，保留一个物料信息给老的订单
						oldOrderPO.setOrderYfPrice(orderYfPrice);
						oldOrderPO.setSubNum(subNum);
						oldOrderPO.setVer(ver+1);
						oldOrderPO.setUpdateBy(logonUser.getUserId());
						oldOrderPO.setUpdateDate(new Date());
						if(null !=remark && !"".equals(remark))
							oldOrderPO.setOrderRemark(remark);
						dao.update(tvopSqlPo, oldOrderPO);//更新原始订单数据
					}
				}
			}
		}
		updateOrderDetail(orderId);
		return res.toString();
	}*/
	
	/**
	 * 根据ORDER ID更新一次选配项，避免时间差导致再去接口那边获取一次
	 * @param orderId
	 */
	public void updateOrderDetail(String orderId){
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("UPDATE TT_VS_ORDER_DETAIL T\n");
		sbSql.append("   SET T.MATERIAL_ID    = (SELECT A.MATERIAL_ID\n");
		sbSql.append("                             FROM TM_VHCL_MATERIAL_ERP_R        A,\n");
		sbSql.append("                                  TM_VHCL_MATERIAL_GROUP   B,\n");
		sbSql.append("                                  TM_VHCL_MATERIAL_GROUP_R C\n");
		sbSql.append("                            WHERE A.MATERIAL_ID = C.MATERIAL_ID\n");
		sbSql.append("                              AND C.GROUP_ID = B.GROUP_ID\n");
		sbSql.append("                              AND B.GROUP_LEVEL = 4\n");
		sbSql.append("                              AND B.GROUP_ID = T.PACKAGE_ID\n");
		sbSql.append("                              AND A.XP_CODE = T.XP_CODE AND ROWNUM=1),\n");
		sbSql.append("       T.XP_SEND_STATUS = '1'"); 
		sbSql.append(" WHERE t.ORDER_ID = ?"); 
		sbSql.append("   AND EXISTS (SELECT A.MATERIAL_ID\n");
		sbSql.append("                             FROM TM_VHCL_MATERIAL_ERP_R         A,\n");
		sbSql.append("                                  TM_VHCL_MATERIAL_GROUP   B,\n");
		sbSql.append("                                  TM_VHCL_MATERIAL_GROUP_R C\n");
		sbSql.append("                            WHERE A.MATERIAL_ID = C.MATERIAL_ID\n");
		sbSql.append("                              AND C.GROUP_ID = B.GROUP_ID\n");
		sbSql.append("                              AND B.GROUP_LEVEL = 4\n");
		sbSql.append("                              AND B.GROUP_ID = T.PACKAGE_ID\n");
		sbSql.append("                              AND A.XP_CODE = T.XP_CODE AND ROWNUM=1)\n");
		List<Object> params = new ArrayList<Object>();
		params.add(orderId);
		dao.update(sbSql.toString(), params);
	}
	
	public void setSplitRelation(Long orderId,Long newOrderId,String materialId,
			String newMaterialId,int number,String newOrderNo,String oldOrderNo,int index,
			String remark,Double vhclPrice,AclUserBean logonUser){
		//设置订单和子订单的关系
		long newMaterialIdL = 0L;
		TtVsOrderSplitRelPO splitRelPO = new TtVsOrderSplitRelPO();
		splitRelPO.setOrderId(orderId);
		splitRelPO.setChildId(newOrderId);
		splitRelPO.setChildOrderno(newOrderNo);
		splitRelPO.setCreateBy(logonUser.getUserId());
		splitRelPO.setCreateDate(new Date());
		if(!"".equals(newMaterialId)){
			newMaterialIdL = Long.parseLong(newMaterialId);
		}
		splitRelPO.setNewMaterialId(newMaterialIdL);
		splitRelPO.setOldMaterialId(Long.parseLong(materialId));
		splitRelPO.setOrderNo(oldOrderNo);
		splitRelPO.setRemark(remark);
		splitRelPO.setSplitId(Long.parseLong(SequenceManager.getSequence("")));
		splitRelPO.setSplitIndex(index);
		splitRelPO.setSplitNum(number);
		splitRelPO.setVhclPrice(vhclPrice);
		dao.insert(splitRelPO);
	}
	/**
	 * 获取新拆订单的标识
	 * @return
	 */
	public int getOrderIndex(Long orderId){
		List<Object> params = new ArrayList<Object>();
		
		
		StringBuffer sbSql = new StringBuffer("select max(nvl(SPLIT_INDEX,0)) SPLIT_INDEX from TT_VS_ORDER_SPLIT_REL where order_id=?");
		params.add(orderId);
		List<Map<String, Object>> list = dao.pageQuery(sbSql.toString(), params, getFunName());
		if(list!=null && list.size()>0){
			if(null!=list.get(0) && null!=list.get(0).get("SPLIT_INDEX")){
				return Integer.parseInt(list.get(0).get("SPLIT_INDEX").toString())+1;
			}
		}
		return 1;
	}
	
	/**
	 * 组建明细订单资源信息
	 * @param tmpVOD
	 * @param oldVOD
	 * @return
	 */
/*	public TtVsOrderDetailPO convertDtlPo(TtVsOrderDetailPO tmpVOD,TtVsOrderDetailPO oldVOD){
		if(null != tmpVOD && null != oldVOD){
			tmpVOD.setAccAmount(oldVOD.getAccAmount());
			tmpVOD.setBoardNumber(oldVOD.getBoardNumber());
			tmpVOD.setCreateBy(oldVOD.getCreateBy());
			tmpVOD.setCreateDate(oldVOD.getCreateDate());
			tmpVOD.setDealerPrice(oldVOD.getDealerPrice());
			tmpVOD.setDeliveryAmount(oldVOD.getDeliveryAmount());
			tmpVOD.setDetailId(oldVOD.getDetailId());
			tmpVOD.setDisSinglePrice(oldVOD.getDisSinglePrice());
			tmpVOD.setModelPrice(oldVOD.getModelPrice());
			tmpVOD.setOrderId(oldVOD.getOrderId());//原始订单订单ID
			tmpVOD.setOutAmount(oldVOD.getOutAmount());
			tmpVOD.setPackageId(oldVOD.getPackageId());
			tmpVOD.setPreMaterialId(oldVOD.getPreMaterialId());
			tmpVOD.setSinglePrice(oldVOD.getSinglePrice());
			tmpVOD.setTotalPrice(oldVOD.getTotalPrice());
			//tmpVOD.setXpcode(oldVOD.getXpcode());
			//tmpVOD.setXpname(oldVOD.getXpname());
		}
		return tmpVOD;
	}*/
	
/*	public TtVsOrderPO convertOrderPo(TtVsOrderPO tmpVO,TtVsOrderPO oldVO){
		if(null != tmpVO && null != oldVO){
			tmpVO.setAreaId(oldVO.getAreaId());
			tmpVO.setAssNum(oldVO.getAssNum());
			tmpVO.setAuditDpt(oldVO.getAuditDpt());
			tmpVO.setBankId(oldVO.getBankId());
			tmpVO.setBoOrderId(oldVO.getBoOrderId());
			tmpVO.setChkNum(oldVO.getChkNum());//设置审核数量
			tmpVO.setCreateBy(oldVO.getCreateBy());
			tmpVO.setCreateDate(oldVO.getCreateDate());
			tmpVO.setCreateType(oldVO.getCreateType());
			tmpVO.setDealerId(oldVO.getDealerId());
			tmpVO.setDealerName(oldVO.getDealerName());
			tmpVO.setDealerShortname(oldVO.getDealerShortname());
			tmpVO.setDelivAddId(oldVO.getDelivAddId());
			tmpVO.setDeliveryType(oldVO.getDeliveryType());
			tmpVO.setFundTypeId(oldVO.getFundTypeId());
			tmpVO.setIsHandover(oldVO.getIsHandover());
			tmpVO.setIsRetail(oldVO.getIsRetail());
			tmpVO.setIsStatus(oldVO.getIsStatus());
			tmpVO.setLinkMan(oldVO.getLinkMan());
			tmpVO.setOrderId(oldVO.getOrderId());
			tmpVO.setOrderNo(oldVO.getOrderNo());//订单号
			tmpVO.setOrderPrice(oldVO.getOrderPrice());
			tmpVO.setOrderRemark(oldVO.getOrderNo()+"订单拆分");//描述
			tmpVO.setOrderStatus(Constant.ORDER_STATUS_02);
			tmpVO.setOrderType(oldVO.getOrderType());
			tmpVO.setOrderYfPrice(oldVO.getOrderYfPrice());
			//tmpVO.setPlanChkDate(new Date());
			tmpVO.setPreOrderId(oldVO.getPreOrderId());
			tmpVO.setRaiseDate(oldVO.getRaiseDate());
			tmpVO.setRecDealerAdd(oldVO.getRecDealerAdd());
			tmpVO.setRecDealerId(oldVO.getRecDealerId());
			tmpVO.setRecDealerName(oldVO.getRecDealerName());
			tmpVO.setRecDealerShortname(oldVO.getRecDealerShortname());
			tmpVO.setSubNum(oldVO.getSubNum());//提报数量
			tmpVO.setTel(oldVO.getTel());
			tmpVO.setThreeCredit(oldVO.getThreeCredit());
			tmpVO.setUpdateBy(oldVO.getUpdateBy());//更新人
			tmpVO.setUpdateDate(oldVO.getUpdateDate());//更新时间
			tmpVO.setVer(1);
		}
		return tmpVO;
	}*/
	
	/**
	 * 将旧的订单数据转移备份一份到订单备份信息表
	 * @param vop
	 */
/*	public void saveOldOrderInfo(TtVsOrderPO vop){
		TtVsOrderSplitPO vopSplit = new TtVsOrderSplitPO();
		vopSplit.setAreaId(vop.getAreaId());
		vopSplit.setAssNum(vop.getAssNum());
		vopSplit.setAuditDpt(vop.getAuditDpt());
		vopSplit.setBankId(vop.getBankId());
		vopSplit.setBoOrderId(vop.getBoOrderId());
		vopSplit.setChkNum(vop.getChkNum());
		vopSplit.setCreateBy(vop.getCreateBy());
		vopSplit.setCreateDate(vop.getCreateDate());
		vopSplit.setCreateType(vop.getCreateType());
		vopSplit.setDealerId(vop.getDealerId());
		vopSplit.setDealerName(vop.getDealerName());
		vopSplit.setDealerShortname(vop.getDealerShortname());
		vopSplit.setDelivAddId(vop.getDelivAddId());
		vopSplit.setDeliveryType(vop.getDeliveryType());
		vopSplit.setFundTypeId(vop.getFundTypeId());
		vopSplit.setIsHandover(vop.getIsHandover());
		vopSplit.setIsRetail(vop.getIsRetail());
		vopSplit.setIsStatus(vop.getIsStatus());
		vopSplit.setLinkMan(vop.getLinkMan());
		vopSplit.setOrderId(vop.getOrderId());
		vopSplit.setOrderNo(vop.getOrderNo());
		vopSplit.setOrderPrice(vop.getOrderPrice());
		vopSplit.setOrderRemark(vop.getOrderRemark());
		vopSplit.setOrderStatus(vop.getOrderStatus());
		vopSplit.setOrderType(vop.getOrderType());
		vopSplit.setOrderYfPrice(vop.getOrderYfPrice());
		vopSplit.setPlanChkDate(vop.getPlanChkDate());
		vopSplit.setPlanChkRemark(vop.getPlanChkRemark());
		vopSplit.setPreOrderId(vop.getPreOrderId());
		vopSplit.setRecDealerId(vop.getRecDealerId());
		vopSplit.setRaiseDate(vop.getRaiseDate());
		vopSplit.setRecDealerAdd(vop.getRecDealerAdd());
		vopSplit.setRecDealerName(vop.getRecDealerName());
		vopSplit.setRecDealerShortname(vop.getDealerShortname());
		vopSplit.setSubNum(vop.getSubNum());
		vopSplit.setTel(vop.getTel());
		vopSplit.setThreeCredit(vop.getThreeCredit());
		vopSplit.setUpdateBy(vop.getUpdateBy());
		vopSplit.setUpdateDate(vop.getUpdateDate());
		vopSplit.setVer(vop.getVer());
		dao.insert(vopSplit);
	}*/
	
/*	public void saveOldOrderDtlInfo(String orderId){
		TtVsOrderDetailPO tVDtlPo = new TtVsOrderDetailPO();
		tVDtlPo.setOrderId(Long.parseLong(orderId));
		List<PO> tvopdList = dao.select(tVDtlPo);
		for(int i=0;i<tvopdList.size();i++){
			TtVsOrderDetailPO tmpVOD = (TtVsOrderDetailPO)tvopdList.get(i);
			TtVsOrderDtlSplitPO newVODtl = new TtVsOrderDtlSplitPO();
			newVODtl.setAccAmount(tmpVOD.getAccAmount());
			newVODtl.setAssAmount(tmpVOD.getAccAmount());
			newVODtl.setBoardNumber(tmpVOD.getBoardNumber());
			newVODtl.setCheckAmount(tmpVOD.getCheckAmount());
			newVODtl.setCreateBy(tmpVOD.getCreateBy());
			newVODtl.setCreateDate(tmpVOD.getCreateDate());
			newVODtl.setDealerPrice(tmpVOD.getDealerPrice());
			newVODtl.setDeliveryAmount(tmpVOD.getDeliveryAmount());
			newVODtl.setDetailId(tmpVOD.getDetailId());
			newVODtl.setDisSinglePrice(tmpVOD.getDisSinglePrice());
			newVODtl.setMaterialId(tmpVOD.getMaterialId());
			newVODtl.setModelPrice(tmpVOD.getModelPrice());
			newVODtl.setOrderId(tmpVOD.getOrderId());
			newVODtl.setOrderAmount(tmpVOD.getOrderAmount());
			newVODtl.setPackageId(tmpVOD.getPackageId());
			newVODtl.setPreMaterialId(tmpVOD.getPreMaterialId());
			newVODtl.setSinglePrice(tmpVOD.getSinglePrice());
			newVODtl.setTotalPrice(tmpVOD.getTotalPrice());
			newVODtl.setUpdateBy(tmpVOD.getUpdateBy());
			newVODtl.setUpdateDate(tmpVOD.getUpdateDate());
			dao.insert(newVODtl);
			//newVODtl.setDisSinglePrice(disSinglePrice)
		}
		//TtVsOrderDetailPO tmpVOD = new TtVsOrderDetailPO();
	}*/
	/**
	 * 分单获取新物料的资源信息
	 * @param warehouseCode
	 * @param materialId
	 * @return
	 */
	public List<Map<String,Object>> getMaterialVehlCount(String warehouseCode,String materialId){
		List<Object> params = new ArrayList<Object>();
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("SELECT A.MATERIAL_ID,\n");
		sbSql.append("       C.KEEP_ID,\n");
		sbSql.append("       NVL(A.STOCK_AMOUNT, 0) STOCK_AMOUNT,\n");
		sbSql.append("       NVL(B.RESAVE_AMOUNT, 0) RESAVE_AMOUNT,\n");
		sbSql.append("       NVL(C.LOCK_AMOUNT,0) LOCK_AMOUNT,\n");
		sbSql.append("       NVL(A.STOCK_AMOUNT, 0) - NVL(B.RESAVE_AMOUNT, 0) - NVL(C.LOCK_AMOUNT,0) VEHICLE_AMOUNT\n");
		sbSql.append("  FROM (SELECT TV.MATERIAL_ID, COUNT(1) STOCK_AMOUNT\n");
		sbSql.append("          FROM TM_VEHICLE TV\n");
		sbSql.append("         WHERE TV.LIFE_CYCLE = 10321002\n");
		sbSql.append("           AND TV.LOCK_STATUS = 10241001\n");
		sbSql.append("           AND TV.WAREHOUSE_ID = ?\n");
		sbSql.append("         GROUP BY TV.MATERIAL_ID) A,\n");
		sbSql.append("       (SELECT TVORR.MATERIAL_ID,\n");
		sbSql.append("               SUM(NVL(TVORR.AMOUNT, 0) - NVL(TVORR.ALLOCA_NUM, 0)) RESAVE_AMOUNT\n");
		sbSql.append("          FROM TT_VS_ORDER_RESOURCE_RESERVE TVORR WHERE TVORR.WAREHOUSE_CODE = ?\n");
		sbSql.append("         GROUP BY TVORR.MATERIAL_ID) B,\n");
		sbSql.append("     (SELECT TCRK.MATERIAL_ID,TCRK.KEEP_ID,\n");
		sbSql.append("               SUM(NVL(TCRK.NUM, 0)) LOCK_AMOUNT\n");
		sbSql.append("          FROM TT_COMP_RES_KEEP TCRK\n");
		sbSql.append("         WHERE TCRK.NUM <> 0 AND WAREHOUSE_CODE = ?\n");
		sbSql.append("         GROUP BY TCRK.MATERIAL_ID,TCRK.KEEP_ID) C  WHERE A.MATERIAL_ID = B.MATERIAL_ID(+)\n");
		sbSql.append("   AND A.MATERIAL_ID = C.MATERIAL_ID(+) \n"); 
		sbSql.append("	AND A.MATERIAL_ID=?"); 
		
		params.add(warehouseCode);
		params.add(warehouseCode);
		params.add(warehouseCode);
		params.add(materialId);
		List<Map<String,Object>> materialCountList = dao.pageQuery(sbSql.toString(), params, dao.getFunName());
		return materialCountList;
	}
	/**
	 * 订单审核驳回
	 * 
	 * @param map
	 * @param logonUser
	 * @author wangsongwei
	 */
/*	public void orderAuditUnPass(Map<String, Object> map, AclUserBean logonUser) throws Exception
	{
		String orderId = CommonUtils.checkNull(map.get("orderId"));
		String orderVer = CommonUtils.checkNull(map.get("orderVer"));
		String remark = CommonUtils.checkNull(map.get("remark"));
		
		List<Object> params = new ArrayList<Object>();
		StringBuffer sbSql = new StringBuffer();
		
		TtVsOrderPO tvopSqlPo = new TtVsOrderPO();
		tvopSqlPo.setOrderId(Long.parseLong(orderId));
		TtVsOrderPO tvop = (TtVsOrderPO) (dao.select(tvopSqlPo).get(0));
		
		// 判断当前的订单是否已经跨月，如果是跨月订单不能驳回
//		 Calendar cal1 = Calendar.getInstance();
//	     cal1.setTime(new Date());
//	     Calendar cal2 = new GregorianCalendar(cal1.get(Calendar.YEAR), cal1.get(Calendar.MONTH), cal1.getActualMinimum((Calendar.DAY_OF_MONTH)));
//	     Date firstDayOfMonth = cal2.getTime();
	        
		Date firstDayOfMonth = CommonUtils.getFirstDateOfMonth(new Date());
	   
		Date raiseDate = tvop.getRaiseDate();
		String orderNo = tvop.getOrderNo();
		if(!orderNo.equals("DD20141224028922")&&!orderNo.equals("DD20150113001722")&&
		!orderNo.equals("DD20150120002957")&&!orderNo.equals("DD20150121003189")&&!orderNo.equals("DD20150121003192")&&
		!orderNo.equals("DD20150122003264")&& !orderNo.equals("DD20150123003775")&&!orderNo.equals("DD20150127004436")&&
		!orderNo.equals("DD20150127004441")&&
		!orderNo.equals("DD20150128004651")&&
		!orderNo.equals("DD20150128004652")&&
		!orderNo.equals("DD20150128004655")&&
		!orderNo.equals("DD20150128004656")&&
		!orderNo.equals("DD20150128004663")&&
		!orderNo.equals("DD20150128004664")&&
		!orderNo.equals("DD20150128004665")&&
		!orderNo.equals("DD20150128004671")&&
		!orderNo.equals("DD20150128004676")&&
		!orderNo.equals("DD20150128004677")&&
		!orderNo.equals("DD20150128004684")&&
		!orderNo.equals("DD20150128004687")&&
		!orderNo.equals("DD20150128004690")&&
		!orderNo.equals("DD20150128004691")&&
		!orderNo.equals("DD20150128005097")&&
		!orderNo.equals("DD20150128005098")&&
		!orderNo.equals("DD20150128005099")&&
		!orderNo.equals("DD20150128005100")&&
		!orderNo.equals("DD20150129005150")&&
		!orderNo.equals("DD20150131006239")&&
		!orderNo.equals("DD20150131006240")&&
		!orderNo.equals("DD20150201006549")&&
		!orderNo.equals("DD20150201006550")&&
		!orderNo.equals("DD20150201006551")&&
		!orderNo.equals("DD20150203006932")&&
		!orderNo.equals("DD20150203006933")&&
		!orderNo.equals("DD20150204007034")&&
		!orderNo.equals("DD20150206007353")&&
		!orderNo.equals("DD20150206007356")&&
		!orderNo.equals("DD20150206007357")&&
		!orderNo.equals("DD20150206007358")&&
		!orderNo.equals("DD20150207007593")&&
		!orderNo.equals("DD20150207007594")&&
		!orderNo.equals("DD20150207007596")&&
		!orderNo.equals("DD20150214008847")&&
		!orderNo.equals("DD20150214008848")&&
		!orderNo.equals("DD20150214008856")&&
		!orderNo.equals("DD20150214008857")&&
		!orderNo.equals("DD20150214008861")&&
		!orderNo.equals("DD20150214008862")&&
		!orderNo.equals("DD20150214008863")&&
		!orderNo.equals("DD20150214008864")&&
		!orderNo.equals("DD20150214008867")&&
		!orderNo.equals("DD20150215008908")&&
		!orderNo.equals("DD20150215008909")&&
		!orderNo.equals("DD20150215008910"))
		if(!orderNo.equals("DD20150214008868")&&
		!orderNo.equals("DD20150214008869")&&
		!orderNo.equals("DD20150214008871")&&
		!orderNo.equals("DD20150214008872")&&
		!orderNo.equals("DD20150214008873")&&
		!orderNo.equals("DD20150214008874")&&
		!orderNo.equals("DD20150214008875")
		)
		//if(!orderNo.equals("DD20150331014416"))
		if(raiseDate.before(firstDayOfMonth)) throw new UserException("跨月订单不能驳回!");
		
		// 如果提车单是由BO单转换的不允许驳回
		if (tvop.getBoOrderId().longValue() != -1L)
			throw new UserException("BO单转提车单不允许驳回!");
		
		// 删除资源预留的数据
		sbSql.delete(0, sbSql.length());
		params.clear();
		params.add(orderId);
		sbSql.append("DELETE FROM TT_VS_ORDER_RESOURCE_RESERVE T\n");
		sbSql.append(" WHERE T.ORDER_DETAIL_ID IN\n");
		sbSql.append("       (SELECT DETAIL_ID FROM TT_VS_ORDER_DETAIL WHERE ORDER_ID = ?)");
		dao.delete(sbSql.toString(), params);
		
		// 更新明细审核数据为0
		sbSql.delete(0, sbSql.length());
		params.clear();
		params.add(orderId);
		sbSql.append("UPDATE TT_VS_ORDER_DETAIL SET CHECK_AMOUNT = 0 WHERE ORDER_ID = ?");
		dao.update(sbSql.toString(), params);
		
//		if(tvop.getFundTypeId().intValue() != Constant.ACCOUNT_TYPE_03.intValue())
//		{
			// 回滚资金冻结金额
			sbSql.delete(0, sbSql.length());
			params.clear();
			params.add(orderId);
			params.add(orderId);
			params.add(orderId);
			sbSql.append("UPDATE TT_SALES_FIN_ACC T\n");
			sbSql.append("   SET T.FREEZE_AMOUNT = NVL(T.FREEZE_AMOUNT, 0) -\n");
			sbSql.append("                         (SELECT A.ORDER_YF_PRICE\n");
			sbSql.append("                            FROM TT_VS_ORDER A\n");
			sbSql.append("                           WHERE A.ORDER_ID = ?),\n");
			sbSql.append("       T.AMOUNT        = NVL(T.AMOUNT, 0) +\n");
			sbSql.append("                         (SELECT A.ORDER_YF_PRICE\n");
			sbSql.append("                            FROM TT_VS_ORDER A\n");
			sbSql.append("                           WHERE A.ORDER_ID = ?),\n");
			sbSql.append("       T.VER           = NVL(T.VER, 0) + 1\n");
			sbSql.append(" WHERE T.ACC_ID IN (");
			sbSql.append("	select b.acc_id\n");
			sbSql.append("    from tt_vs_order a, tt_sales_fin_acc b\n");
			sbSql.append(" 	 where a.dealer_id = b.dealer_id\n");
			sbSql.append("     and a.fund_type_id = b.fin_type\n");
			sbSql.append("     and a.area_id = b.yieldly and a.order_id = ?)");
			
			dao.update(sbSql.toString(), params);
			TtSaleFundsRoseDetailPO fundDetailPO = new TtSaleFundsRoseDetailPO();
			fundDetailPO.setRemark(tvop.getOrderNo()+"被驳回释放冻结");
			fundDetailPO.setDealerId(tvop.getDealerId());
			fundDetailPO.setDetailId(Long.parseLong(SequenceManager.getSequence("")));
			fundDetailPO.setAmount(-tvop.getOrderYfPrice());//释放冻结金额(-)
			fundDetailPO.setCreateBy(logonUser.getUserId());
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			fundDetailPO.setCreateDate(sdf.format(new Date()));
			fundDetailPO.setFinType(Integer.parseInt(tvop.getFundTypeId().toString()));
			fundDetailPO.setFinInType(Integer.parseInt(tvop.getFundTypeId().toString()));
			fundDetailPO.setIsType(6);
			dao.insert(fundDetailPO);
			fundDetailPO.setDetailId(Long.parseLong(SequenceManager.getSequence("")));
			fundDetailPO.setRemark(tvop.getOrderNo()+"被驳回恢复可用余额");
			fundDetailPO.setAmount(tvop.getOrderYfPrice());
			fundDetailPO.setIsType(5);
			sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			fundDetailPO.setCreateDate(sdf.format(new Date()));
			dao.insert(fundDetailPO);
//		}
//		else
//		{
//			// 回滚资金冻结金额
//			sbSql.delete(0, sbSql.length()); params.clear();
//			params.add(orderId); params.add(orderId);
//			sbSql.append("UPDATE TT_SALES_OTHER_ACCEPT\n");
//			sbSql.append("   SET FREEZE_AMOUNT = FREEZE_AMOUNT - (SELECT ORDER_YF_PRICE\n");
//			sbSql.append("                                          FROM TT_VS_ORDER\n");
//			sbSql.append("                                         WHERE ORDER_ID = ?)\n");
//			sbSql.append(" WHERE CRE_DETAIL_ID =\n");
//			sbSql.append("       (SELECT THREE_CREDIT FROM TT_VS_ORDER WHERE ORDER_ID = ?)"); 
//			dao.update(sbSql.toString(), params);
//		}
		
		// 更新订单状态为驳回
		sbSql.delete(0, sbSql.length());
		params.clear();
		params.add(Constant.ORDER_STATUS_03);
		params.add(orderId);
		params.add(orderVer);
		sbSql.append("UPDATE TT_VS_ORDER SET ORDER_STATUS = ? WHERE ORDER_ID = ? AND VER = ?");
		int updateStatus = dao.update(sbSql.toString(), params);
		
		if (updateStatus != 1)
			throw new UserException("驳回失败!订单已作其他修改,请刷新页面后重试!");
		
		// 保存审核记录
		saveCheckResult(Long.parseLong(orderId), remark, Constant.FLEET_SUPPORT_CHECK_STATUS_02, Constant.OUT_ORDER_AUDIT_TYPE_01, logonUser);
	}
	
	*//**
	 * 保存订单资源预留
	 * 
	 * @param map
	 * @param logonUser
	 * @author wangsongwei
	 *//*
	public void saveOrderResource(Map<String, Object> map, AclUserBean logonUser)
	{
		String[] checkAmount = (String[]) map.get("checkAmount");
		String[] materialId = (String[]) map.get("materialId");
		String orderId = CommonUtils.checkNull(map.get("orderId"));
		String orderVer = CommonUtils.checkNull(map.get("orderVer"));
		
		List<Object> params = new ArrayList<Object>();
		StringBuffer sbSql = new StringBuffer();
		
		sbSql.append("WITH " + AsSqlUtils.getAsSqlVehicleResourceBuffer());
		sbSql.append("SELECT A.MATERIAL_ID, B.VEHICLE_AMOUNT\n");
		sbSql.append("  FROM TT_VS_ORDER_DETAIL A, VEHICLE_STOCK B\n");
		sbSql.append(" WHERE A.MATERIAL_ID = B.MATERIAL_ID(+)\n");
		sbSql.append("   AND A.ORDER_ID = ?");
		params.add(orderId);
		List<Map<String, Object>> materialList = dao.pageQuery(sbSql.toString(), params, getFunName());
		
		for (int i = 0; i < materialList.size(); i++)
		{
			Map<String, Object> data = materialList.get(i);
			String mid = data.get("MATERIAL_ID").toString();
			boolean disabled = true;
			for (int j = 0; j < materialId.length; j++)
			{
				if (materialId[i].equals(mid))
				{
					if (Integer.parseInt(checkAmount[i]) > Integer.parseInt(data.get("VEHICLE_AMOUNT").toString()))
					{
						throw new UserException("当前订单的资源发生变化,请刷新页面后重新保存!");
					}
					else
					{
						sbSql.delete(0, sbSql.length());
						params.clear();
						params.add(checkAmount[i]);
						params.add(orderId);
						params.add(materialId[i]);
						sbSql.append("UPDATE TT_VS_ORDER_DETAIL SET CHECK_AMOUNT = ? WHERE ORDER_ID = ? AND MATERIAL_ID = ?");
						
						dao.update(sbSql.toString(), params);
						disabled = false;
					}
				}
			}
			if (disabled)
				throw new UserException("无效物料信息!");
		}
		
		// 1、先将当前资源表里面预留的资源清空
		// 2、然后将资源插入到资源预留表
		sbSql.delete(0, sbSql.length());
		params.clear();
		params.add(orderId);
		sbSql.append("DELETE FROM TT_VS_ORDER_RESOURCE_RESERVE T\n");
		sbSql.append(" WHERE T.ORDER_DETAIL_ID IN\n");
		sbSql.append("       (SELECT DETAIL_ID FROM TT_VS_ORDER_DETAIL WHERE ORDER_ID = ?)");
		dao.delete(sbSql.toString(), params);
		
		sbSql.delete(0, sbSql.length());
		sbSql.append("INSERT INTO TT_VS_ORDER_RESOURCE_RESERVE\n");
		sbSql.append("  (RESERVE_ID,\n");
		sbSql.append("   ORDER_DETAIL_ID,\n");
		sbSql.append("   MATERIAL_ID,\n");
		sbSql.append("   AMOUNT,\n");
		sbSql.append("   CREATE_BY,\n");
		sbSql.append("   CREATE_DATE)\n");
		sbSql.append("  SELECT F_GETID, DETAIL_ID, MATERIAL_ID, CHECK_AMOUNT, " + logonUser.getUserId() + ", SYSDATE\n");
		sbSql.append("    FROM TT_VS_ORDER_DETAIL\n");
		sbSql.append("   WHERE ORDER_ID = " + orderId);
		dao.insert(sbSql.toString());
		
		// 更新订单状态
		sbSql.delete(0, sbSql.length());
		params.clear();
		params.add(orderId);
		params.add(orderId);
		params.add(orderVer);
		sbSql.append("UPDATE TT_VS_ORDER SET VER = NVL(VER,0) + 1,CHK_NUM = (SELECT SUM(CHECK_AMOUNT) FROM TT_VS_ORDER_DETAIL WHERE ORDER_ID = ?) WHERE ORDER_ID = ? AND VER = ?");
		
		int updateStatus = dao.update(sbSql.toString(), params);
		
		if (updateStatus != 1)
			throw new UserException("订单已更改,请刷新页面后重试!");
	}*/

	/**
	 * 保存订单资源预留--网销流程
	 * 
	 * @param map
	 * @param logonUser
	 * @author wangsongwei
	 */
	public void saveOrderResourceInternetSales(Map<String, Object> map, AclUserBean logonUser)
	{
		String[] checkAmount = (String[]) map.get("checkAmount");
		String[] materialId = (String[]) map.get("materialId");
		String orderId = CommonUtils.checkNull(map.get("orderId"));
		String orderVer = CommonUtils.checkNull(map.get("orderVer"));
		String recDealerId = CommonUtils.checkNull(map.get("recDealerId"));
		String deliveryAddress = CommonUtils.checkNull(map.get("deliveryAddress"));
		String linkMan = CommonUtils.checkNull(map.get("linkMan"));
		String tel = CommonUtils.checkNull(map.get("tel"));
		
		List<Object> params = new ArrayList<Object>();
		StringBuffer sbSql = new StringBuffer();
		
		sbSql.append("WITH " + AsSqlUtils.getAsSqlVehicleResourceBuffer());
		sbSql.append("SELECT A.MATERIAL_ID, B.VEHICLE_AMOUNT\n");
		sbSql.append("  FROM TT_VS_ORDER_DETAIL A, VEHICLE_STOCK B\n");
		sbSql.append(" WHERE A.MATERIAL_ID = B.MATERIAL_ID(+)\n");
		sbSql.append("   AND A.ORDER_ID = ?");
		params.add(orderId);
		List<Map<String, Object>> materialList = dao.pageQuery(sbSql.toString(), params, getFunName());
		
		for (int i = 0; i < materialList.size(); i++)
		{
			Map<String, Object> data = materialList.get(i);
			String mid = data.get("MATERIAL_ID").toString();
			boolean disabled = true;
			for (int j = 0; j < materialId.length; j++)
			{
				if (materialId[i].equals(mid))
				{
					if (Integer.parseInt(checkAmount[i]) > Integer.parseInt(data.get("VEHICLE_AMOUNT").toString()))
					{
						throw new UserException("当前订单的资源发生变化,请刷新页面后重新保存!");
					}
					else
					{
						sbSql.delete(0, sbSql.length());
						params.clear();
						params.add(checkAmount[i]);
						params.add(orderId);
						params.add(materialId[i]);
						sbSql.append("UPDATE TT_VS_ORDER_DETAIL SET CHECK_AMOUNT = ? WHERE ORDER_ID = ? AND MATERIAL_ID = ?");
						
						dao.update(sbSql.toString(), params);
						disabled = false;
					}
				}
			}
			if (disabled)
				throw new UserException("无效物料信息!");
		}
		
		// 1、先将当前资源表里面预留的资源清空
		// 2、然后将资源插入到资源预留表
		sbSql.delete(0, sbSql.length());
		params.clear();
		params.add(orderId);
		sbSql.append("DELETE FROM TT_VS_ORDER_RESOURCE_RESERVE T\n");
		sbSql.append(" WHERE T.ORDER_DETAIL_ID IN\n");
		sbSql.append("       (SELECT DETAIL_ID FROM TT_VS_ORDER_DETAIL WHERE ORDER_ID = ?)");
		dao.delete(sbSql.toString(), params);
		
		sbSql.delete(0, sbSql.length());
		sbSql.append("INSERT INTO TT_VS_ORDER_RESOURCE_RESERVE\n");
		sbSql.append("  (RESERVE_ID,\n");
		sbSql.append("   ORDER_DETAIL_ID,\n");
		sbSql.append("   MATERIAL_ID,\n");
		sbSql.append("   AMOUNT,\n");
		sbSql.append("   CREATE_BY,\n");
		sbSql.append("   CREATE_DATE)\n");
		sbSql.append("  SELECT F_GETID, DETAIL_ID, MATERIAL_ID, CHECK_AMOUNT, " + logonUser.getUserId() + ", SYSDATE\n");
		sbSql.append("    FROM TT_VS_ORDER_DETAIL\n");
		sbSql.append("   WHERE ORDER_ID = " + orderId);
		dao.insert(sbSql.toString());
		
		// 更新订单状态
		sbSql.delete(0, sbSql.length());
		params.clear();
		params.add(orderId);
		params.add(recDealerId);
		params.add(deliveryAddress);
		params.add(linkMan);
		params.add(tel);
		params.add(orderId);
		params.add(orderVer);
		sbSql.append("UPDATE TT_VS_ORDER SET VER = NVL(VER,0) + 1,CHK_NUM = (SELECT SUM(CHECK_AMOUNT) FROM TT_VS_ORDER_DETAIL WHERE ORDER_ID = ?), ");
		sbSql.append("  REC_DEALER_ID=?,DELIV_ADD_ID=?,LINK_MAN=?,TEL=?,");
		sbSql.append("  REC_DEALER_NAME=(SELECT TD.DEALER_NAME FROM TM_DEALER TD WHERE TD.DEALER_ID=").append(recDealerId).append("),");
		sbSql.append("  REC_DEALER_SHORTNAME=(SELECT TD.DEALER_SHORTNAME FROM TM_DEALER TD WHERE TD.DEALER_ID=").append(recDealerId).append(")");
		sbSql.append("WHERE ORDER_ID = ? AND VER = ? ");
		
		int updateStatus = dao.update(sbSql.toString(), params);
		
		if (updateStatus != 1)
			throw new UserException("订单已更改,请刷新页面后重试!");
	}

	/**
	 * bo单转提车单
	 * 
	 * @param map
	 * @param logonUser
	 */
	public void boOrderAuditPass(Map<String, Object> map, AclUserBean logonUser) throws Exception
	{
		
		String boOrderId = CommonUtils.checkNull(map.get("boOrderId"));
		String[] checkAmount = (String[]) map.get("checkAmount");
		String[] materialId = (String[]) map.get("materialId");
		String[] preMaterialId = (String[]) map.get("prematerialId");
		
		StringBuffer sbSql = new StringBuffer();
		List<Object> params = new ArrayList<Object>();
		
		// 更新物料明细
		for (int i = 0; i < preMaterialId.length; i++)
		{
			sbSql.delete(0, sbSql.length());
			sbSql.append("UPDATE TT_VS_BO_ORDER_DETAIL SET CHECK_AMOUNT = ?, MATERIAL_ID = ? WHERE BO_ORDER_ID = ? AND PRE_MATERIAL_ID = ?");
			params.clear();
			params.add(CommonUtils.checkNullNum(checkAmount[i]));
			params.add(materialId[i]);
			params.add(boOrderId);
			params.add(preMaterialId[i]);
			dao.update(sbSql.toString(), params);
		}
		
		// 通过boOrderId找到具体的BO单数据
		TtVsBoOrderPO tvbopSqlPo = new TtVsBoOrderPO();
		tvbopSqlPo.setBoOrderId(Long.parseLong(boOrderId));
		
		TtVsBoOrderPO tvbop = (TtVsBoOrderPO) dao.select(tvbopSqlPo).get(0);
		
		String orderId = SequenceManager.getSequence("");
		
		sbSql.delete(0, sbSql.length());
		sbSql.append("INSERT INTO TT_VS_ORDER(ORDER_ID,PRE_ORDER_ID,BO_ORDER_ID,AREA_ID,ORDER_NO,ORDER_TYPE,RAISE_DATE,\n");
		sbSql.append("       DEALER_ID,REC_DEALER_ID,FUND_TYPE_ID,THREE_CREDIT,REC_DEALER_ADD,\n");
		sbSql.append("       IS_HANDOVER,DELIVERY_TYPE,DELIV_ADD_ID,LINK_MAN,TEL,ORDER_REMARK,ORDER_STATUS,\n");
		sbSql.append("       CREATE_BY,CREATE_DATE,IS_RETAIL,DEALER_NAME,DEALER_SHORTNAME,REC_DEALER_NAME,REC_DEALER_SHORTNAME)\n");
		sbSql.append("SELECT " + orderId + " AS ORDER_ID,PRE_ORDER_ID,BO_ORDER_ID,AREA_ID,'" + CommonUtils.getBusNo(Constant.NOCRT_ORDER_NO, tvbop.getAreaId())
						+ "',ORDER_TYPE,SYSDATE AS RAISE_DATE,\n");
		sbSql.append("       DEALER_ID,REC_DEALER_ID,FUND_TYPE_ID,THREE_CREDIT,REC_DEALER_ADD,\n");
		sbSql.append("       IS_HANDOVER,DELIVERY_TYPE,DELIV_ADD_ID,LINK_MAN,TEL,ORDER_REMARK," + Constant.ORDER_STATUS_02 + " AS ORDER_STATUS,\n");
		sbSql.append("       CREATE_BY,CREATE_DATE,IS_RETAIL,DEALER_NAME,DEALER_SHORTNAME,REC_DEALER_NAME,REC_DEALER_SHORTNAME\n");
		sbSql.append("  FROM TT_VS_BO_ORDER WHERE BO_ORDER_ID = " + boOrderId);
		dao.insert(sbSql.toString());
		
		sbSql.delete(0, sbSql.length());
		sbSql.append("INSERT INTO TT_VS_ORDER_DETAIL (DETAIL_ID,ORDER_ID,PRE_MATERIAL_ID,MATERIAL_ID,ORDER_AMOUNT,\n");
		sbSql.append("   SINGLE_PRICE,MODEL_PRICE,DEALER_PRICE,DIS_SINGLE_PRICE,CREATE_BY,CREATE_DATE)\n");
		sbSql.append("  SELECT F_GETID,\n");
		sbSql.append("         " + orderId + ",\n");
		sbSql.append("         PRE_MATERIAL_ID,\n");
		sbSql.append("         MATERIAL_ID,\n");
		sbSql.append("         NVL(CHECK_AMOUNT,0) ORDER_AMOUNT,\n");
		sbSql.append("         SINGLE_PRICE,\n");
		sbSql.append("         MODEL_PRICE,\n");
		sbSql.append("         DEALER_PRICE,\n");
		sbSql.append("         DIS_SINGLE_PRICE,\n");
		sbSql.append("         " + logonUser.getUserId() + ",\n");
		sbSql.append("         SYSDATE\n");
		sbSql.append("    FROM TT_VS_BO_ORDER_DETAIL");
		sbSql.append("   WHERE NVL(CHECK_AMOUNT,0) <> 0 AND BO_ORDER_ID = " + boOrderId);
		dao.insert(sbSql.toString());
		
		sbSql.delete(0, sbSql.length());
		params.clear();
		sbSql.append("UPDATE TT_VS_ORDER\n");
		sbSql.append("   SET SUB_NUM        = (SELECT SUM(ORDER_AMOUNT)\n");
		sbSql.append("                           FROM TT_VS_ORDER_DETAIL\n");
		sbSql.append("                          WHERE ORDER_ID = ?),\n");
		sbSql.append("       ORDER_YF_PRICE = (SELECT SUM(DIS_SINGLE_PRICE * ORDER_AMOUNT)\n");
		sbSql.append("                           FROM TT_VS_ORDER_DETAIL\n");
		sbSql.append("                          WHERE ORDER_ID = ?)\n");
		sbSql.append(" WHERE ORDER_ID = ?");
		params.add(orderId);
		params.add(orderId);
		params.add(orderId);
		dao.update(sbSql.toString(), params);
		
		sbSql.delete(0, sbSql.length());
		params.clear();
		sbSql.append("UPDATE TT_VS_BO_ORDER_DETAIL SET ORDER_AMOUNT = ORDER_AMOUNT - NVL(CHECK_AMOUNT,0), CHECK_AMOUNT = 0 WHERE BO_ORDER_ID = ?");
		params.add(boOrderId);
		dao.update(sbSql.toString(), params);
		
		sbSql.delete(0, sbSql.length());
		params.clear();
		sbSql.append("DELETE FROM TT_VS_BO_ORDER_DETAIL WHERE BO_ORDER_ID = ? AND ORDER_AMOUNT = 0");
		params.add(boOrderId);
		dao.delete(sbSql.toString(), params);
		
		sbSql.delete(0, sbSql.length());
		params.clear();
		sbSql.append("SELECT * FROM TT_VS_BO_ORDER_DETAIL WHERE BO_ORDER_ID = ?");
		params.add(boOrderId);
		List detailList = dao.pageQuery(sbSql.toString(), params, getFunName());
		
		if (detailList.size() == 0)
		{
			// dao.delete("delete from tt_vs_bo_order where bo_order_id = " +
			// boOrderId, null);
			params.clear();
			params.add(Constant.ORDER_STATUS_04);
			params.add(boOrderId);
			dao.update("update tt_vs_bo_order set order_status = ? where bo_order_id = ?", params);
		}
		else
		{
			sbSql.delete(0, sbSql.length());
			params.clear();
			sbSql.append("UPDATE TT_VS_BO_ORDER\n");
			sbSql.append("   SET SUB_NUM        = (SELECT SUM(ORDER_AMOUNT)\n");
			sbSql.append("                           FROM TT_VS_BO_ORDER_DETAIL\n");
			sbSql.append("                          WHERE BO_ORDER_ID = ?),\n");
			sbSql.append("       ORDER_YF_PRICE = (SELECT SUM(DIS_SINGLE_PRICE * ORDER_AMOUNT)\n");
			sbSql.append("                           FROM TT_VS_BO_ORDER_DETAIL\n");
			sbSql.append("                          WHERE BO_ORDER_ID = ?)\n");
			sbSql.append(" WHERE BO_ORDER_ID = ?");
			params.add(boOrderId);
			params.add(boOrderId);
			params.add(boOrderId);
			dao.update(sbSql.toString(), params);
		}
	}
	
	/**
	 * 获取经销商的经营类型是否是直营店类型
	 * 
	 * @param dealerId
	 * @return
	 */
	public boolean getDealerSalesType(String dealerId)
	{
		StringBuffer sbSql = new StringBuffer();
		List<Object> params = new ArrayList<Object>();
		sbSql.append("SELECT A.SHOP_TYPE FROM TM_DEALER_DETAIL A WHERE A.FK_DEALER_ID = ?");
		params.add(dealerId);
		Map<String, Object> dstMap = dao.pageQueryMap(sbSql.toString(), params, getFunName());
		
		if (dstMap != null)
		{
			if (dstMap.get("SHOP_TYPE").toString().equals("直营"))
			{
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * 根据经销商和本次订单suv的数量检测形象店支持商务政策是否还继续享受
	 * @param dealerId
	 * @param suvOrderAmount
	 * @return
	 * @throws Exception
	 */
	public boolean getDealerVILimit(String dealerId,int suvOrderAmount)throws Exception{
		//StringBuffer 
		//先判断第一年的形象店支持时间是 否已经失效
		//如果第一年政策已经失效，就判断第二年的形象店是否维护
		//如果第二年政策未维护，则提示经销商联系车厂重新维护结算价或者第二年的vi政策
		//如果第二年政策有维护，就判断有效日期和台次是否达到继续有效
		//如果第二年政策有效，那么订单正常提报
		//如果第二年已经达到上限，提示经销商联系车厂第二年形象店政策已经达到上限，需联系车厂修改结算价
		//第一年已经结束，如果未维护第二年的形象店数据，提示经销商第一年政策结束，联系订单员维护第二年的商务政策和结算价（要么取消商务政策，要么维护第二年的vi数据）
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("select t.support_sdate,t.support_edate,t.dealer_id, t.DEPLOY_ID, t.STATUS\n");
		sbSql.append("  from tt_vi_construct_detail t,\n");
		sbSql.append("       tt_vi_construct_main t1 \n");
		sbSql.append(" where t.year_flag = 1\n");
		sbSql.append("   and t.id = t1.id \n");
		sbSql.append("   and t.state = 10011011\n");
		sbSql.append("   and t.dealer_id=?"); 
		List<Object> params = new ArrayList<Object>();
		params.add(dealerId);
		sbSql.append("   and t1.VEHICLE_SERIES_ID=2014032694231206");
		Map<String, Object> dstMap = dao.pageQueryMap(sbSql.toString(), params, getFunName());
		
		if (dstMap != null)
		{
			String supportSDate = dstMap.get("SUPPORT_SDATE").toString();
			String supportEDate = dstMap.get("SUPPORT_EDATE").toString();
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			Date d1 = df.parse(supportSDate);
			Date d2 = df.parse(supportEDate);
			// 第一年时间未过期
			if(CommonUtils.compareDateToDaysWithNoAbs(new Date(), d2)>=0){
				// VI审核通过时，结算价必须存在
				if (Constant.VI_CONSTRUCT_STATUS_02.toString().equals(dstMap.get("STATUS").toString())) {
					Map<String, Object> result = getDealerDeploy(dealerId,0,new Long("2014032694231206"));
					if (result == null) {
						throw new UserException("第一年政策处于正常状态,请联系车厂添加结算价政策!");
					} else {
						return true;
					}
				
				// VI处理终止或者未审核通过时，必须取消结算价
				} else {
					Map<String, Object> result = getDealerDeploy(dealerId,0,new Long("2014032694231206"));
					if (result != null) {
						throw new UserException("第一年政策处于非正常状态,请联系车厂取消结算价政策!");
					} else {
						return true;
					}
				}
			}else{
				//第一年已经失效，判断第二年是否已经维护
				sbSql.delete(0, sbSql.length());
				sbSql.append("select t.support_sdate,t.support_edate,t.dealer_id,trunc(nvl(FLOOR(t.year_rent*(100-t.scale)/100/t.AMOUNT),0)) suvMaxAmount,t.DEPLOY_ID,t.STATUS,t.SUPPORT_NUMBER\n");
				sbSql.append("  from tt_vi_construct_detail t,\n");
				sbSql.append("       tt_vi_construct_main t1 \n");
				sbSql.append(" where t.year_flag = 2\n");
				sbSql.append("   and t.id = t1.id \n");
				sbSql.append("   and t.state = 10011011\n");
				sbSql.append("   and t.dealer_id=?"); 
				sbSql.append("   and t1.VEHICLE_SERIES_ID=2014032694231206");
				Map<String, Object> sndDtlMap = dao.pageQueryMap(sbSql.toString(), params, getFunName());
				if(sndDtlMap != null){
					//如果第二年已经维护,查看第二年的台次和有效期是否继续有效
					String suvMaxAmount = sndDtlMap.get("SUVMAXAMOUNT").toString();
					String addSupportNumber = sndDtlMap.get("SUPPORT_NUMBER").toString();
					supportSDate = sndDtlMap.get("SUPPORT_SDATE").toString();
					supportEDate = sndDtlMap.get("SUPPORT_EDATE").toString();
					d1 = df.parse(supportSDate);
					d2 = df.parse(supportEDate);
					if(CommonUtils.compareDateToDaysWithNoAbs(d1,new Date())>=0.0 && CommonUtils.compareDateToDaysWithNoAbs(new Date(),d2)>=0.0){
						//如果在有效期内
						// VI审核通过时，结算价必须存在
						if (Constant.VI_CONSTRUCT_STATUS_02.toString().equals(sndDtlMap.get("STATUS").toString())) {
							Map<String, Object> result = getDealerDeploy(dealerId,1,new Long("2014032694231206"));
							if (result == null) {
								throw new UserException("第二年政策处于正常状态,请联系车厂添加第二年结算价政策!");
							} else {
								if(null != suvMaxAmount && !"".equals(suvMaxAmount)){
									sbSql.delete(0, sbSql.length());
									sbSql.append("select nvl(sum(b.order_amount),0) orderAmount\n");
									sbSql.append("  from tt_vs_order a, tt_vs_order_detail b\n");
									sbSql.append(" where a.order_id = b.order_id\n");
									sbSql.append("   and a.order_status in (10211011, 10211002, 10211004)\n");
									sbSql.append("   and a.dealer_id = ?\n");
									sbSql.append("   and b.package_id in\n");
									sbSql.append("       (select group_id\n");
									sbSql.append("          from tm_vhcl_material_group\n");
									sbSql.append("         where parent_group_id in\n");
									sbSql.append("               (select group_id\n");
									sbSql.append("                  from tm_vhcl_material_group\n");
									sbSql.append("                 where parent_group_id = 2014032694231206))\n");
									sbSql.append("    and trunc(a.raise_date)>=?");
									sbSql.append("    and trunc(a.raise_date)<=?");
									params.clear();
									params.add(dealerId);
									params.add(d1);
									params.add(d2);
									Map<String, Object> orderAmountMap = dao.pageQueryMap(sbSql.toString(), params, getFunName());
									if(null != orderAmountMap){
										String orderAmount = orderAmountMap.get("ORDERAMOUNT").toString();
										if (Integer.parseInt(orderAmount) > (Integer.parseInt(suvMaxAmount) + Integer.parseInt(addSupportNumber))) {
											throw new UserException("支持台数已经到达上限,请联系车厂终止第二年政策支持,再进行提单!");
										} else if(Integer.parseInt(orderAmount)+suvOrderAmount>(Integer.parseInt(suvMaxAmount) + Integer.parseInt(addSupportNumber))){
											int disCount = Integer.parseInt(suvMaxAmount) + Integer.parseInt(addSupportNumber) -Integer.parseInt(orderAmount);
											int maxAmount = Integer.parseInt(suvMaxAmount) + Integer.parseInt(addSupportNumber);
											throw new UserException("本次订单SUV数量最多还有"+disCount+"台即达到上限"+maxAmount+"台,在达到上限后请联系车厂维护结算价!");
										}else{
											return true;
										}
									}
								}else{//如果没有维护第二年的形象店的租金数据
									throw new UserException("请联系车厂重新维护形象支持数据的年租金!");
								}
							}
						
						// VI处理终止或者未审核通过时，必须取消结算价
						} else {
							Map<String, Object> result = getDealerDeploy(dealerId,1,new Long("2014032694231206"));
							if (result != null) {
								throw new UserException("第二年政策处于非正常状态,请联系车厂取消第二年结算价政策!");
							} else {
								return true;
							}
						}
					}else{
						//如果第二年时间已经过期或者未到vi政策开始时间，则提示结算价上面不能有建店支持的政策绑定
						Map<String, Object> result = getDealerDeploy(dealerId,2,new Long("2014032694231206"));
						if (result != null) {
							throw new UserException("不在第二年建店支持时间范围内,请联系车厂取消第一年、第二年结算价政策!");
						}
						return true;
					}
				}else{
					//如果未维护第二年的形象店，则查看结算价应该取消了商务政策
					Map<String, Object> result = getDealerDeploy(dealerId,2,new Long("2014032694231206"));
					if (result != null) {
						throw new UserException("第一年建店支持已经结束,第二年建店支持未建立,请联系车厂取消第一年、第二年结算价政策!");
					}
					return true;
				}
			}
			
		// 形象店未建立,不能有结算价政策
		}  else {
			Map<String, Object> result = settlePriceCheck(dealerId, "2014032694231206");
			if (!(result == null || result.get("TOTALCOUNT") == null || result.get("TOTALCOUNT").toString().equals("0"))) {
				throw new UserException("形象店未建立,请取消SUV的所有结算价政策!"); 
			}
		}
		return true;
	}

	/**
	 * 根据经销商和本次订单mpv的数量检测形象店支持商务政策是否还继续享受
	 * @param dealerId
	 * @param suvOrderAmount
	 * @return
	 * @throws Exception
	 */
	public boolean getDealerVILimitMpv(String dealerId,int mpvOrderAmount)throws Exception{
		//StringBuffer 
		//先判断第一年的形象店支持时间是 否已经失效
		//如果第一年政策已经失效，就判断第二年的形象店是否维护
		//如果第二年政策未维护，则提示经销商联系车厂重新维护结算价或者第二年的vi政策
		//如果第二年政策有维护，就判断有效日期和台次是否达到继续有效
		//如果第二年政策有效，那么订单正常提报
		//如果第二年已经达到上限，提示经销商联系车厂第二年形象店政策已经达到上限，需联系车厂修改结算价
		//第一年已经结束，如果未维护第二年的形象店数据，提示经销商第一年政策结束，联系订单员维护第二年的商务政策和结算价（要么取消商务政策，要么维护第二年的vi数据）
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("select t.support_sdate,t.support_edate,t.dealer_id, t.DEPLOY_ID, t.STATUS\n");
		sbSql.append("  from tt_vi_construct_detail t,\n");
		sbSql.append("       tt_vi_construct_main t1 \n");
		sbSql.append(" where t.year_flag = 1\n");
		sbSql.append("   and t.id = t1.id \n");
		sbSql.append("   and t.state = 10011011\n");
		sbSql.append("   and t.dealer_id=?"); 
		List<Object> params = new ArrayList<Object>();
		params.add(dealerId);
		sbSql.append("   and t1.VEHICLE_SERIES_ID=2015011508783002");
		Map<String, Object> dstMap = dao.pageQueryMap(sbSql.toString(), params, getFunName());
		
		if (dstMap != null) {
			String supportSDate = dstMap.get("SUPPORT_SDATE").toString();
			String supportEDate = dstMap.get("SUPPORT_EDATE").toString();
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			Date d1 = df.parse(supportSDate);
			Date d2 = df.parse(supportEDate);
			if(CommonUtils.compareDateToDaysWithNoAbs(new Date(), d2)>=0){
				// VI审核通过时，结算价必须存在
				if (Constant.VI_CONSTRUCT_STATUS_02.toString().equals(dstMap.get("STATUS").toString())) {
					Map<String, Object> result = getDealerDeploy(dealerId,0,new Long("2015011508783002"));
					if (result == null) {
						throw new UserException("第一年政策处于正常状态,请联系车厂添加结算价政策!");
					} else {
						return true;
					}
				
				// VI处理终止或者未审核通过时，必须取消结算价
				} else {
					Map<String, Object> result = getDealerDeploy(dealerId,0,new Long("2015011508783002"));
					if (result != null) {
						throw new UserException("第一年政策处于非正常状态,请联系车厂取消结算价政策!");
					} else {
						return true;
					}
				}
			}else{
				// 第一例失效、第二年未
				//第一年已经失效，判断第二年是否已经维护
				sbSql.delete(0, sbSql.length());
				sbSql.append("select t.support_sdate,t.support_edate,t.dealer_id,trunc(nvl(FLOOR(t.year_rent*(100-t.scale)/100/t.AMOUNT),0)) mpvMaxAmount, t.DEPLOY_ID,t.STATUS,t.SUPPORT_NUMBER\n");
				sbSql.append("  from tt_vi_construct_detail t,\n");
				sbSql.append("       tt_vi_construct_main t1 \n");
				sbSql.append(" where t.year_flag = 2\n");
				sbSql.append("   and t.id = t1.id \n");
				sbSql.append("   and t.state = 10011011\n");
				sbSql.append("   and t.dealer_id=?"); 
				sbSql.append("   and t1.VEHICLE_SERIES_ID=2015011508783002");
				Map<String, Object> sndDtlMap = dao.pageQueryMap(sbSql.toString(), params, getFunName());
				if(sndDtlMap != null){
					//如果第二年已经维护,查看第二年的台次和有效期是否继续有效
					String mpvMaxAmount = sndDtlMap.get("MPVMAXAMOUNT").toString();
					String addSupportNumber = sndDtlMap.get("SUPPORT_NUMBER").toString();
					supportSDate = sndDtlMap.get("SUPPORT_SDATE").toString();
					supportEDate = sndDtlMap.get("SUPPORT_EDATE").toString();
					d1 = df.parse(supportSDate);
					d2 = df.parse(supportEDate);
					if(CommonUtils.compareDateToDaysWithNoAbs(d1,new Date())>=0.0 && CommonUtils.compareDateToDaysWithNoAbs(new Date(),d2)>=0.0){
						//如果在有效期内
						// VI审核通过时，结算价必须存在
						if (Constant.VI_CONSTRUCT_STATUS_02.toString().equals(dstMap.get("STATUS").toString())) {
							Map<String, Object> result = getDealerDeploy(dealerId,1,new Long("2015011508783002"));
							if (result == null) {
								throw new UserException("第二年政策处于正常状态,请联系车厂添加第二年结算价政策!");
							} else {
								if(null != mpvMaxAmount && !"".equals(mpvMaxAmount)){
									sbSql.delete(0, sbSql.length());
									sbSql.append("select nvl(sum(b.order_amount),0) orderAmount\n");
									sbSql.append("  from tt_vs_order a, tt_vs_order_detail b\n");
									sbSql.append(" where a.order_id = b.order_id\n");
									sbSql.append("   and a.order_status in (10211011, 10211002, 10211004)\n");
									sbSql.append("   and a.dealer_id = ?\n");
									sbSql.append("   and b.package_id in\n");
									sbSql.append("       (select group_id\n");
									sbSql.append("          from tm_vhcl_material_group\n");
									sbSql.append("         where parent_group_id in\n");
									sbSql.append("               (select group_id\n");
									sbSql.append("                  from tm_vhcl_material_group\n");
									sbSql.append("                 where parent_group_id = 2015011508783002 AND GROUP_ID <> 2015080721782025))\n");
									sbSql.append("    and trunc(a.raise_date)>=?");
									sbSql.append("    and trunc(a.raise_date)<=?");
									params.clear();
									params.add(dealerId);
									params.add(d1);
									params.add(d2);
									Map<String, Object> orderAmountMap = dao.pageQueryMap(sbSql.toString(), params, getFunName());
									if(null != orderAmountMap){
										String orderAmount = orderAmountMap.get("ORDERAMOUNT").toString();
										if (Integer.parseInt(orderAmount) > (Integer.parseInt(mpvMaxAmount) + Integer.parseInt(addSupportNumber))) {
											throw new UserException("支持台数已经到达上限,请联系车厂终止第二年政策支持,再进行提单!");
										} else if(Integer.parseInt(orderAmount)+mpvOrderAmount>(Integer.parseInt(mpvMaxAmount) + Integer.parseInt(addSupportNumber))){
											int disCount = Integer.parseInt(mpvMaxAmount) + Integer.parseInt(addSupportNumber) -Integer.parseInt(orderAmount);
											int maxAmount = Integer.parseInt(mpvMaxAmount) + Integer.parseInt(addSupportNumber);
											throw new UserException("本次订单MPV数量最多还有"+disCount+"台即达到上限"+maxAmount+"台,在达到上限后请联系车厂维护结算价!");
										}else{
											return true;
										}
									}
								}else{//如果没有维护第二年的形象店的租金数据
									throw new UserException("请联系车厂重新维护形象支持数据的年租金!");
								}
							}
						
						// VI处理终止或者未审核通过时，必须取消结算价
						} else {
							Map<String, Object> result = getDealerDeploy(dealerId,1,new Long("2015011508783002"));
							if (result != null) {
								throw new UserException("第二年政策处于非正常状态,请联系车厂取消第二年结算价政策!");
							} else {
								return true;
							}
						}
					}else{
						//如果第二年时间已经过期或者未到vi政策开始时间，则提示结算价上面不能有建店支持的政策绑定
						Map<String, Object> result = getDealerDeploy(dealerId,2,new Long("2015011508783002"));
						if (result != null) {
							throw new UserException("不在第二年建店支持时间范围内,请联系车厂取消第一年、第二年结算价政策!");
						}
						return true;
					}
				}else{
					//如果未维护第二年的形象店，则查看结算价应该取消了商务政策
					Map<String, Object> result = getDealerDeploy(dealerId,2,new Long("2015011508783002"));
					if (result != null) {
						throw new UserException("第一年建店支持已经结束,第二年建店支持未建立,请联系车厂取消第一年,第二年结算价政策!");
					}
					return true;
				}
			}
			
		// 形象店未建立，结算价不能加政策
		} else {
			Map<String, Object> result = settlePriceCheck(dealerId, "2015011508783002");
			if (!(result == null || result.get("TOTALCOUNT") == null || result.get("TOTALCOUNT").toString().equals("0"))) {
				throw new UserException("请联系厂商,MPV形象店未建立,请取消MPV的所有结算价政策!"); 
			}
		}
		return true;
	}
	
	/**
	 * 形象店未建立，结算价不能加第一年第二年政策CHECK
	 */
	private Map<String, Object> settlePriceCheck(String dealerId, String suvOrMpvGroupId) {
		StringBuffer sql = new StringBuffer();
		List<Object> params = new ArrayList<Object>();
		sql.append("SELECT COUNT(1) TOTALCOUNT FROM TT_SALES_PRICE_LIST_R TSPLR WHERE TSPLR.DISRATE_ID IN ( \n");
		sql.append(" 	SELECT TSMDD.DISRATE_ID FROM TT_SALES_MODLE_DISRATE_DEALER TSMDD\n");
		sql.append(" 	INNER JOIN TM_VHCL_MATERIAL_GROUP TVMG ON TVMG.GROUP_ID=TSMDD.PACKAGE_ID\n");
		sql.append(" 	INNER JOIN TM_VHCL_MATERIAL_GROUP TVMG1 ON TVMG.PARENT_GROUP_ID=TVMG1.GROUP_ID\n");
		sql.append(" 	WHERE TSMDD.DEALER_ID=?\n");
		params.add(dealerId);
		sql.append(" 		  AND TVMG1.PARENT_GROUP_ID=?\n");
		params.add(suvOrMpvGroupId);
		sql.append(" 		  AND TSMDD.STATUS=10011001 AND TSMDD.AUDIT_STATUS=99301003)\n");
		sql.append(" 	AND DEPLOY_ID IN (2015010308169526,2015022810377031) \n");
		return dao.pageQueryMap(sql.toString(), params, getFunName());
	}
	
	/**
	 * 根据经销商和标识检查该经销商的suv对应政策情况
	 * @param dealerId
	 * @param deployFlag
	 * @return
	 */
	public Map<String, Object> getDealerDeploy(String dealerId,int deployFlag, Long suvOrMpv){
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("select *\n");
		sbSql.append("  from TT_SALES_PRICE_LIST_R r\n");
		if(deployFlag==0)
			sbSql.append(" where r.deploy_id in(2015010308169526) and \n");
		else if (deployFlag==1)
			sbSql.append(" where r.deploy_id in(2015022810377031) and \n");
		else 
			sbSql.append(" where r.deploy_id in(2015022810377031,2015010308169526) and \n");
		sbSql.append(" r.DISRATE_ID in (with tab_dea as (select t.dealer_id,\n");
		sbSql.append("                                          t.dealer_type,\n");
		sbSql.append("                                          t.dealer_code,\n");
		sbSql.append("                                          t.dealer_shortname,\n");
		sbSql.append("                                          t.dealer_name\n");
		sbSql.append("                                     from tm_dealer t\n");
		sbSql.append("                                    where t.dealer_type = 10771001\n");
		sbSql.append("                                      and (t.dealer_id = ?)\n");
		sbSql.append("                                      and t.status = 10011001\n");
		sbSql.append("                                      and t.dealer_level = 10851001\n");
		sbSql.append("\n");
		sbSql.append("                                   )\n");
		sbSql.append("  select d.disrate_id\n");
		sbSql.append("    from (select * from vw_material_group a, tab_dea b) c,\n");
		sbSql.append("         TT_SALES_MODLE_DISRATE_DEALER d\n");
		sbSql.append("   where c.package_id = d.package_id(+)\n");
		sbSql.append("     and c.dealer_id = d.dealer_id(+)\n");
		sbSql.append("     and c.PACKAGE_ID in\n");
		sbSql.append("         (select group_id\n");
		sbSql.append("            from tm_vhcl_material_group\n");
		sbSql.append("           where parent_group_id in\n");
		sbSql.append("                 (select group_id\n");
		sbSql.append("                    from tm_vhcl_material_group\n");
		sbSql.append("                   where parent_group_id = ?))\n");
		List<Object> params = new ArrayList<Object>();
		params.add(dealerId);
		params.add(suvOrMpv);
		sbSql.append("     and (d.status is null or d.status = 10011001))"); 
		Map<String, Object> dtlMap = dao.pageQueryMap(sbSql.toString(), params, getFunName());
//		if(deployFlag==0){//如果没有维护形象店数据，但是维护了商务政策
//			if(dtlMap == null){
//				throw new UserException("第一年政策已经逾期,请联系车厂重新维护结算价关联的商务政策或者形象支持数据!");
//			}
//		}else if(deployFlag==1){//如果第二年有维护形象店,但是未维护结算价的商务政策
//			if(dtlMap == null){
//				throw new UserException("第二年政策未完全兑现,请联系车厂重新维护结算价关联的商务政策或者形象支持数据!");
//			}
//		}else if(deployFlag==2){//如果第二年有维护形象店,但是形象店vi维护已经到期或者未开始，但是结算价继续有效的时候提示
//			if(dtlMap != null){
//				throw new UserException("第二年政策已经逾期,请联系车厂重新维护结算价关联的商务政策或者形象支持数据!");
//			}
//		}
		return dtlMap;
	}
	
	/**
	 * CHECK结算价是否已经维护（SUV和MPV的所有物料组对应的结算价）
	 */
	private boolean isModifySettlePrice(String dealerId, String deployId) {
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("SELECT COUNT(TSMDD.PACKAGE_ID) PACKAGE_COUNT \n");
		sbSql.append("  FROM TT_SALES_MODLE_DISRATE_DEALER TSMDD \n");
		sbSql.append("  INNER JOIN  TT_SALES_PRICE_LIST_R TSPLR ON TSMDD.disrate_id=TSPLR.disrate_id \n");
		sbSql.append("  INNER JOIN  TT_SALES_POLICY_DEPLOY TSPD ON TSPD.DEPLOY_ID = TSPLR.DEPLOY_ID \n");
		sbSql.append("  WHERE TSMDD.AUDIT_STATUS = 99301003 \n");
		sbSql.append("    AND TRUNC(SYSDATE) >= TRUNC(TSPD.START_DATE) AND TRUNC(SYSDATE) <= TRUNC(TSPD.STOP_DATE)\n");
		sbSql.append("    AND TSPD.DEPLOY_STATUS=1 \n");
		sbSql.append("    AND (TSMDD.STATUS=10011001 OR TSMDD.STATUS IS NULL) \n");
		sbSql.append("    AND TSMDD.DEALER_ID=?\n");
		sbSql.append("    AND TSPLR.DEPLOY_ID=?\n");
		List<Object> params = new ArrayList<Object>();
		params.add(dealerId);
		params.add(deployId);
		Map<String, Object> map = dao.pageQueryMap(sbSql.toString(), params, getFunName());
		if (map != null && ((BigDecimal)map.get("PACKAGE_COUNT")).intValue() != 0) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * 统计SUV或者MPV的提单总数量
	 */
	public Map<String, Integer> getPackageAmount(String[] packageId,String[] subAmount, String dealerId){
		Map<String, Integer> resultMap = new HashMap<String, Integer>();
		StringBuffer sbSql = new StringBuffer();
		int suvAmount = 0;
		int mpvAmount = 0;
		sbSql.append("SELECT TVMG.PARENT_GROUP_ID PARENT_GROUP_ID1,TVMG1.PARENT_GROUP_ID \n");
		sbSql.append("FROM TM_VHCL_MATERIAL_GROUP TVMG \n");
		sbSql.append("INNER JOIN TM_VHCL_MATERIAL_GROUP TVMG1 ON TVMG.PARENT_GROUP_ID=TVMG1.GROUP_ID \n");
		sbSql.append(" WHERE TVMG.GROUP_ID=? \n");
		List<Object> params = new ArrayList<Object>();
		String pGId = "";
		for(int i=0;i<packageId.length;i++){
			params.clear();
			params.add(packageId[i]);
			Map<String, Object> pMap = dao.pageQueryMap(sbSql.toString(), params, getFunName());
			if(null != pMap){
				// 如果是H2E,就不进行返利优惠
				String h2eGroupId = pMap.get("PARENT_GROUP_ID1").toString();
				if (!h2eGroupId.equals("2015080721782025")) {
					pGId = pMap.get("PARENT_GROUP_ID").toString();
					if("2014032694231206".equals(pGId)){
						suvAmount = suvAmount+Integer.parseInt(subAmount[i]);
					} else if ("2015011508783002".equals(pGId)) {
						mpvAmount = mpvAmount + Integer.parseInt(subAmount[i]);
					}
					
				// 如果提单中包含有H2E,则CHECK该结算价不能有政策
				} else {
					if (!h2eSettlePriceCheck(dealerId, packageId[i])) {
						throw new UserException("请联系厂商,H2E车型结算价不能有建店政策优惠!"); 
					}
				}
			}
		}
		resultMap.put("suvAmount", suvAmount);
		resultMap.put("mpvAmount", mpvAmount);
		return resultMap;
	}
	
	/**
	 * H2E结算价不能有建店政策CHECK
	 */
	private boolean h2eSettlePriceCheck(String dealerId, String packageId) {
		StringBuffer sql = new StringBuffer();
		List<Object> params = new ArrayList<Object>();
		sql.append("SELECT COUNT(1) TOTALCOUNT FROM TT_SALES_PRICE_LIST_R TSPLR WHERE TSPLR.DISRATE_ID IN ( \n");
		sql.append(" 	SELECT TSMDD.DISRATE_ID FROM TT_SALES_MODLE_DISRATE_DEALER TSMDD\n");
		sql.append(" 	INNER JOIN TM_VHCL_MATERIAL_GROUP TVMG ON TVMG.GROUP_ID=TSMDD.PACKAGE_ID\n");
		sql.append(" 	WHERE TSMDD.DEALER_ID=?\n");
		params.add(dealerId);
		sql.append(" 		  AND TVMG.PARENT_GROUP_ID=2015080721782025\n");
		sql.append(" 		  AND TSMDD.STATUS=10011001 AND TSMDD.AUDIT_STATUS=99301003\n");
		sql.append("		  AND TSMDD.PACKAGE_ID=?)"); 
		params.add(packageId);
		sql.append(" 	AND TSPLR.DEPLOY_ID IN (2015010308169526,2015022810377031)\n");
		Map<String, Object> map = dao.pageQueryMap(sql.toString(), params, getFunName());
		if (map == null || map.get("TOTALCOUNT") == null || map.get("TOTALCOUNT").toString().equals("0")) {
			return true;
		} else {
			return false;
		}
	}
	
	/**++++++++++++++++++++++++++++++++++++++++++++++++++++++++
     * 描述:银翔原型报错修复
     * 新增方法
     * Date:2017-06-29
     * java：CreateZhongZhuanBill。java
     */
	public void updateOrderRebate(Object o1, Object o2) {
		
	}
	
	/**++++++++++++++++++++++++++++++++++++++++++++++++++++++++
     * 描述:银翔原型报错修复
     * 新增方法
     * Date:2017-06-29
     * java：ManagerZhongZhuanBill。java
     */
	public void rollbackDealerAccount(Object o1, Object o2) {
		
	}
	
	public static Double getRemitAmountByID(Long dealerId){

//		List<Object> params = new ArrayList<Object>();
//		StringBuffer sbSql = new StringBuffer();
//		
//		sbSql.append("select\n");
//		sbSql.append("      nvl((select sum(t.remit_amount) from tt_out_remittance t where t.dealer_id = ? and t.status = 10011001),0)\n");
//		sbSql.append("      -\n");
//		sbSql.append("      nvl((select sum(decode(vmt.model_type,12421001,"+Constant.OUT_MODEL_PRICE_7000.intValue()+",12421002,"+Constant.OUT_MODEL_PRICE_5000.intValue()+",0)*b.order_amount)\n");
//		sbSql.append("        from tt_vs_order a, tt_vs_order_detail b,vw_material_group_mat vmt\n");
//		sbSql.append("       where a.order_id = b.order_id\n");
//		sbSql.append("         and b.material_id = vmt.material_id\n");
//		sbSql.append("         and a.order_type = 10201004\n");
//		sbSql.append("         and a.order_status not in (10211000, 10211001, 10211003, 10211013)\n");
//		sbSql.append("         and a.dealer_id = ?),0)\n");
//		sbSql.append("      +\n");
//		sbSql.append("      nvl((select sum(decode(vmt.model_type,12421001,"+Constant.OUT_MODEL_PRICE_7000.intValue()+",12421002,"+Constant.OUT_MODEL_PRICE_5000.intValue()+",0))\n");
//		sbSql.append("        from tt_vs_order        a,\n");
//		sbSql.append("             tt_vs_order_detail b,\n");
//		sbSql.append("             tt_sales_bo_detail c,\n");
//		sbSql.append("             tt_sales_alloca_de d,\n");
//		sbSql.append("             tm_vehicle         e,\n");
//		sbSql.append("             vw_material_group_mat vmt\n");
//		sbSql.append("       where a.order_id = b.order_id\n");
//		sbSql.append("         and b.detail_id = c.or_de_id\n");
//		sbSql.append("         and c.bo_de_id = d.bo_de_id\n");
//		sbSql.append("         and d.vehicle_id = e.vehicle_id\n");
//		sbSql.append("         and b.material_id = vmt.material_id\n");
//		sbSql.append("         and a.order_type = 10201004\n");
//		sbSql.append("         and a.order_status = 10211011\n");
//		sbSql.append("         and e.out_status = 97151003\n");
//		sbSql.append("         and a.dealer_id = ?),0) REMIT_AMOUNT\n");
//		sbSql.append("    from dual"); 
//		params.add(dealerId); params.add(dealerId); params.add(dealerId);
//		
//		Map<String,Object> priceMap = dao.pageQueryMap(sbSql.toString(), params, dao.getFunName());
		
//		return Double.parseDouble(priceMap.get("REMIT_AMOUNT").toString());
		return 0.0;
	}
}
