package com.infodms.dms.dao.sales.financemanage;

import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.common.Constant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.dao.common.BaseDao;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

public class DealerQuotaDao  extends BaseDao  {
	
	public static Logger logger = Logger.getLogger(DealerQuotaDao.class);
	private static final DealerQuotaDao dao = new DealerQuotaDao ();
	public static final DealerQuotaDao getInstance() {
		return dao;
	}
	
	
	/**
	 * Function         : 发运指令查询
	 * @param           : 订单开始年周
	 * @param           : 订单结束年周
	 * @param           : 发运起始时间
	 * @param           : 发运结束时间
	 * @param           : 订单类型
	 * @param           : 订单号码
	 * @param			: 选择类型
	 * @param           : 经销商CODE
	 * @param           : 组织CODE
	 * @param           : 物料组CODE
	 * @param           : 运送方式
	 * @param           : 发运状态
	 * @param           : 当前页码
	 * @param           : 每页显示记录数
	 * @return          : 满足条件的信息，包含分页
	 * @throws          : Exception
	 * LastUpdate       : 2010-05-24
	 */
	public PageResult<Map<String, Object>> getDealerQueryList(String dealerIds,String  startDate,String endDate,String dealerCode, String orderNo,Long companyId,int curPage,int pageSize) throws Exception{
		List <Object>params = new LinkedList<Object>();
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT \n");
		sql.append("TMD2.DEALER_NAME,\n");
		sql.append("TMD1.DEALER_NAME MYDEALER_NAME,\n");
		sql.append("TVD.BILLING_DATE,\n");
		sql.append("TVD.DELIVERY_NO,\n");
		sql.append("TVO.ORDER_STATUS,\n");
		sql.append("TMD2.DEALER_CODE,");
		sql.append("TVO.IS_FLEET,\n");
		sql.append("TMF.FLEET_NAME,\n");
		sql.append("TVO.FLEET_ADDRESS,\n");
		sql.append("TVD.DELIVERY_DATE,\n");
		sql.append("SUM(TVDD.DELIVERY_AMOUNT) DELIVERY_AMOUNT\n");
		sql.append("FROM TT_VS_ORDER     TVO,\n");
		sql.append("TM_DEALER       TMD1,\n");
		sql.append("TM_DEALER       TMD2,\n");
		sql.append("TT_VS_DLVRY     TVD,\n");
		sql.append("TM_FLEET        TMF,\n");
		//sql.append("TM_DEALER       TMD3,\n");
		sql.append("TT_VS_DLVRY_DTL TVDD,TT_VS_DLVRY_REQ TVDR\n");
		sql.append("WHERE TVO.BILLING_ORG_ID = TMD1.DEALER_ID\n");
		sql.append("AND TVO.ORDER_ORG_ID = TMD2.DEALER_ID\n");
		sql.append("AND TVD.ORDER_ID = TVO.ORDER_ID\n");
		sql.append("AND TMF.FLEET_ID(+) = TVO.FLEET_ID\n");
		//sql.append("AND TVO.RECEIVER = TMD3.DEALER_ID\n");
		sql.append("AND TVDD.REQ_ID=TVDR.REQ_ID\n");
		//sql.append("AND TVDD.DELIVERY_ID=TVD.DELIVERY_ID\n");
		sql.append("AND TVDD.DELIVERY_ID=TVD.DELIVERY_ID\n");
		sql.append("AND TVO.IS_FLEET=1\n");
		if (null != startDate && !"".equals(startDate)) {
			sql.append("   AND TVD.DELIVERY_DATE >= TO_DATE('"+startDate+" 00:00:00','yyyy-MM-dd HH24:MI:SS')\n");
		}
		if (null != endDate && !"".equals(endDate)) {
			sql.append("   AND TVD.DELIVERY_DATE <= TO_DATE('"+endDate+" 23:59:59','yyyy-MM-dd HH24:MI:SS')\n");
		}
		if (null != orderNo && !"".equals(orderNo)) {
			sql.append("AND TVD.DELIVERY_NO LIKE '%"+orderNo+"%'");
		}
		if (null != dealerCode && !"".equals(dealerCode)) {
			sql.append("AND TMD2.DEALER_CODE LIKE '%"+dealerCode+"%'");
		}
		sql.append("AND TVDR.RECEIVER IN("+dealerIds+")\n");
		sql.append("GROUP BY TMD2.DEALER_CODE,\n");
		sql.append("TMD2.DEALER_NAME,\n");
		sql.append("TMD1.DEALER_NAME,\n");
		sql.append("TVD.BILLING_DATE,\n");
		sql.append("TVD.DELIVERY_NO,\n");
		sql.append("TVO.ORDER_STATUS,\n");
		sql.append("TVO.IS_FLEET,\n");
		sql.append("TMF.FLEET_NAME,\n");
		sql.append("TVO.FLEET_ADDRESS,\n");
		sql.append("TVD.DELIVERY_DATE,\n");
		sql.append("TVD.DELIVERY_DATE\n");
		
		
		
		
		
		//sql.append("          TVOD.SPECIAL_BATCH_NO");
		PageResult<Map<String, Object>> ps =pageQuery(sql.toString(),null, getFunName(), pageSize, curPage);
		return ps;
	}

	
	
	public List<Map<String, Object>> getDealerQueryLoad(String dealerIds,String  startDate,String endDate,String dealerCode, String orderNo,Long companyId,int curPage,int pageSize) throws Exception{
		List <Object>params = new LinkedList<Object>();
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT \n");
		sql.append("TMD2.DEALER_NAME,\n");
		sql.append("TMD1.DEALER_NAME MYDEALER_NAME,\n");
		sql.append("TVD.BILLING_DATE,\n");
		sql.append("TVD.DELIVERY_NO,\n");
		sql.append("TC.CODE_DESC,\n");
		sql.append("TVO.ORDER_STATUS,\n");
		sql.append("TMD2.DEALER_CODE,");
		sql.append("TVO.IS_FLEET,\n");
		sql.append("TMF.FLEET_NAME,\n");
		sql.append("TVO.FLEET_ADDRESS,\n");
		sql.append("TVD.DELIVERY_DATE,\n");
		sql.append("SUM(TVDD.DELIVERY_AMOUNT) DELIVERY_AMOUNT\n");
		sql.append("FROM TT_VS_ORDER     TVO,\n");
		sql.append("TM_DEALER       TMD1,\n");
		sql.append("TM_DEALER       TMD2,\n");
		sql.append("TT_VS_DLVRY     TVD,\n");
		sql.append("TM_FLEET        TMF,\n");
		//sql.append("TM_DEALER       TMD3,\n");
		sql.append("TT_VS_DLVRY_DTL TVDD,TC_CODE TC,TT_VS_DLVRY_REQ TVDR\n");
		sql.append("WHERE TVO.BILLING_ORG_ID = TMD1.DEALER_ID\n");
		sql.append("AND TVO.ORDER_ORG_ID = TMD2.DEALER_ID\n");
		sql.append("AND TVD.ORDER_ID = TVO.ORDER_ID\n");
		sql.append("AND TMF.FLEET_ID(+) = TVO.FLEET_ID\n");
		//sql.append("AND TVO.RECEIVER = TMD3.DEALER_ID\n");
		sql.append("AND TVDD.REQ_ID=TVDR.REQ_ID\n");
		sql.append("AND TC.CODE_ID=TVO.ORDER_STATUS\n");
		sql.append("AND TVDD.DELIVERY_ID=TVD.DELIVERY_ID\n");
		sql.append("AND TVO.IS_FLEET=1\n");
		if (null != startDate && !"".equals(startDate)) {
			sql.append("   AND TVD.DELIVERY_DATE >= TO_DATE('"+startDate+" 00:00:00','yyyy-MM-dd HH24:MI:SS')\n");
		}
		if (null != endDate && !"".equals(endDate)) {
			sql.append("   AND TVD.DELIVERY_DATE <= TO_DATE('"+endDate+" 23:59:59','yyyy-MM-dd HH24:MI:SS')\n");
		}
		if (null != orderNo && !"".equals(orderNo)) {
			sql.append("AND TVD.DELIVERY_NO LIKE '%"+orderNo+"%'");
		}
		if (null != dealerCode && !"".equals(dealerCode)) {
			sql.append("AND TMD2.DEALER_CODE LIKE '%"+dealerCode+"%'");
		}
		sql.append("AND TVDR.RECEIVER IN("+dealerIds+")\n");
		sql.append("GROUP BY TMD2.DEALER_CODE,\n");
		sql.append("TMD2.DEALER_NAME,\n");
		sql.append("TMD1.DEALER_NAME,\n");
		sql.append("TVD.BILLING_DATE,\n");
		sql.append("TVD.DELIVERY_NO,\n");
		sql.append("TVO.ORDER_STATUS,\n");
		sql.append("TVO.IS_FLEET,\n");
		sql.append("TMF.FLEET_NAME,\n");
		sql.append("TVO.FLEET_ADDRESS,\n");
		sql.append("TVD.DELIVERY_DATE,\n");
		sql.append("TVD.DELIVERY_DATE,TC.CODE_DESC\n");
		
		List<Map<String, Object>> results=pageQuery(sql.toString(), params, getFunName());
//		PageResult<Map<String, Object>> ps =pageQuery(sql.toString(),null, getFunName(), pageSize, curPage);
		return results;
	}

	
	
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
}
