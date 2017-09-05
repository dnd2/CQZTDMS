package com.infodms.dms.dao.sales.ordermanage.audit;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.dao.sales.storageManage.CheckVehicleDAO;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

public class MonthOrderCheckDao extends BaseDao{
	public static Logger logger = Logger.getLogger(CheckVehicleDAO.class);
	private static final MonthOrderCheckDao dao = new MonthOrderCheckDao ();
	public static final MonthOrderCheckDao getInstance() {
		return dao;
	}
	
	public static PageResult <Map<String,Object>> monthOrderPreCheckQuery(String year, String month, String areaId, String dealerId, String orderNO, int pageSize,int curPage){
		StringBuffer sql = new StringBuffer("\n");
		
		sql.append("SELECT TTO.ORDER_ID,\n");
		sql.append("       TMD.DEALER_CODE,\n");  
		sql.append("       TMD.DEALER_NAME,\n");  
		sql.append("       TTO.ORDER_NO,\n");  
		sql.append("       TO_CHAR(TTO.RAISE_DATE, 'yyyy-MM-dd') RAISE_DATE,\n");  
		sql.append("       SUM(TTOD.ORDER_AMOUNT) AS ORDER_AMOUNT\n");  
		sql.append("  FROM TT_VS_ORDER TTO, TM_DEALER TMD, TT_VS_ORDER_DETAIL TTOD\n");  

		sql.append(" WHERE TTO.ORDER_ORG_ID = TMD.DEALER_ID\n");  
		sql.append("   AND TTO.ORDER_ID = TTOD.ORDER_ID\n"); 


		
		if (null != areaId && !"".equals(areaId)) {
			sql.append("   AND TTO.AREA_ID ="+areaId+"\n");  
		}
		
		if (null != orderNO && !"".equals(orderNO)) {
			sql.append("   AND TTO.ORDER_NO LIKE '%"+orderNO.trim()+"%'\n");  
		}
		sql.append("   AND TTO.ORDER_YEAR = "+year+"\n");  
		sql.append("   AND TTO.ORDER_MONTH = "+month+"\n");  
		sql.append("   AND TTO.BILLING_ORG_ID in ("+dealerId+")\n"); 
		sql.append("   AND TTO.ORDER_STATUS = "+Constant.ORDER_STATUS_02+"\n");  
		sql.append(" GROUP BY TTO.ORDER_ID,\n");  
		sql.append("          TMD.DEALER_CODE,\n");  
		sql.append("          TMD.DEALER_NAME,\n");  
		sql.append("          TTO.ORDER_NO,\n");  
		sql.append("          TTO.RAISE_DATE\n");  
		sql.append(" ORDER BY TTO.ORDER_ID\n");
		return dao.pageQuery(sql.toString(), null,dao.getFunName(),pageSize ,curPage );
	}
	
	/**
	 * 月度订单审核：查询可审核的订单列表(事业部)
	 * */
	public static PageResult <Map<String,Object>> monthOrderCheck_BUS_QueryList(Long orgId,String year, String month, String areaId, String dealerCode, String orderNO, int pageSize,int curPage){
		StringBuffer sql = new StringBuffer("");
		
		sql.append("SELECT TTO.ORDER_ID,\n");
		sql.append("       TMD.DEALER_CODE,\n");  
		sql.append("       TMD.DEALER_NAME,\n");  
		sql.append("       TTO.ORDER_NO,\n");  
		sql.append("       TO_CHAR(TTO.RAISE_DATE, 'yyyy-MM-dd') RAISE_DATE,\n");  
		sql.append("       SUM(TTOD.ORDER_AMOUNT) AS ORDER_AMOUNT\n");  
		sql.append("  FROM TT_VS_ORDER TTO, TM_DEALER TMD, TT_VS_ORDER_DETAIL TTOD,\n");  
		sql.append("(SELECT TDOR.DEALER_ID\n");
		sql.append("          FROM vw_org_dealer TDOR\n");  
		if(null != orgId) 
			sql.append("               WHERE TDOR.ROOT_ORG_ID = "+orgId+"\n");  
		
		sql.append(") Xx\n") ;

		sql.append(" WHERE TTO.ORDER_ORG_ID = TMD.DEALER_ID\n");  
		sql.append("   AND TTO.ORDER_ID = TTOD.ORDER_ID\n");  
		sql.append("   AND TMD.DEALER_ID = XX.DEALER_ID\n");


		
		if (null != areaId && !"".equals(areaId)) {
			sql.append("   AND TTO.AREA_ID ="+areaId+"\n");  
		}
		if(null != dealerCode && !"".equals(dealerCode)) {
			String[] array = dealerCode.split(",");
			sql.append("   AND TMD.DEALER_CODE IN (\n");
			for (int i = 0; i < array.length; i++) {
				sql.append("'" + array[i] + "'");
				if (i != array.length - 1) {
					sql.append(",");
				}
			}
			sql.append(")\n");
		}
		if (null != orderNO && !"".equals(orderNO)) {
			sql.append("   AND TTO.ORDER_NO LIKE '%"+orderNO.trim()+"%'\n");  
		}
		sql.append("   AND TTO.ORDER_YEAR = "+year+"\n");  
		sql.append("   AND TTO.ORDER_MONTH = "+month+"\n");  
		sql.append("   AND TTO.ORDER_STATUS = "+Constant.ORDER_STATUS_08+"\n");  
		sql.append(" GROUP BY TTO.ORDER_ID,\n");  
		sql.append("          TMD.DEALER_CODE,\n");  
		sql.append("          TMD.DEALER_NAME,\n");  
		sql.append("          TTO.ORDER_NO,\n");  
		sql.append("          TTO.RAISE_DATE\n");  
		sql.append(" ORDER BY TTO.ORDER_ID\n");
		return dao.pageQuery(sql.toString(), null,dao.getFunName(),pageSize ,curPage );
	}
	
	/**
	 * 月度订单审核：查询可审核的订单列表(销售部)
	 * */
	public static PageResult <Map<String,Object>> monthOrderCheck_SAL_QueryList(String year, String month, String areaId, String dealerCode, String orderNO, int pageSize,int curPage){
		StringBuffer sql = new StringBuffer("");
		
		sql.append("SELECT TTO.ORDER_ID,\n");
		sql.append("       TMD.DEALER_CODE,\n");  
		sql.append("       TMD.DEALER_NAME,\n");  
		sql.append("       TTO.ORDER_NO,\n");  
		sql.append("       TO_CHAR(TTO.RAISE_DATE, 'yyyy-MM-dd') RAISE_DATE,\n");  
		sql.append("       SUM(TTOD.ORDER_AMOUNT) AS ORDER_AMOUNT\n");  
		sql.append("  FROM TT_VS_ORDER TTO, TM_DEALER TMD, TT_VS_ORDER_DETAIL TTOD\n");  
		sql.append(" WHERE TTO.ORDER_ORG_ID = TMD.DEALER_ID\n");  
		sql.append("   AND TTO.ORDER_ID = TTOD.ORDER_ID\n");  
		if (null != areaId && !"".equals(areaId)) {
			sql.append("   AND TTO.AREA_ID ="+areaId+"\n");  
		}
		if(null != dealerCode && !"".equals(dealerCode)) {
			String[] array = dealerCode.split(",");
			sql.append("   AND TMD.DEALER_CODE IN (\n");
			for (int i = 0; i < array.length; i++) {
				sql.append("'" + array[i] + "'");
				if (i != array.length - 1) {
					sql.append(",");
				}
			}
			sql.append(")\n");
		}
		if (null != orderNO && !"".equals(orderNO)) {
			sql.append("   AND TTO.ORDER_NO LIKE '%"+orderNO.trim()+"%'\n");  
		}
		sql.append("   AND TTO.ORDER_YEAR = "+year+"\n");  
		sql.append("   AND TTO.ORDER_MONTH = "+month+"\n");  
		sql.append("   AND TTO.ORDER_STATUS = "+Constant.ORDER_STATUS_09+"\n");  
		sql.append("   AND TTO.ORDER_TYPE = "+Constant.ORDER_TYPE_01+"\n");
		sql.append(" GROUP BY TTO.ORDER_ID,\n");  
		sql.append("          TMD.DEALER_CODE,\n");  
		sql.append("          TMD.DEALER_NAME,\n");  
		sql.append("          TTO.ORDER_NO,\n");  
		sql.append("          TTO.RAISE_DATE\n");  
		sql.append(" ORDER BY TTO.ORDER_ID\n");
		return dao.pageQuery(sql.toString(), null,dao.getFunName(),pageSize ,curPage );
	}
	
	public static Map<String, Object> getOrderInfo(String orderId) {
		StringBuffer sql = new StringBuffer();

		sql.append("SELECT TMD.DEALER_CODE,\n");
		sql.append("       TMD.DEALER_SHORTNAME,\n");  
		sql.append("       TTO.ORDER_NO,\n");  
		sql.append("       TTO.ORDER_YEAR || '年' || TTO.ORDER_MONTH || '月' AS ORDERMONTH,\n");  
		sql.append("       TTO.ORDER_ID\n");
		sql.append("  FROM TT_VS_ORDER TTO, TM_DEALER TMD\n");  
		sql.append(" WHERE TTO.ORDER_ORG_ID = TMD.DEALER_ID\n");  
		sql.append("   AND TTO.ORDER_ID = "+orderId+"\n");


		Map<String, Object> orderInfo = dao.pageQueryMap(sql.toString(), null,dao.getFunName());
		return orderInfo;
	}
	
	public static List<Map<String, Object>> getDetailOrderList(String orderId) {

		StringBuffer sql = new StringBuffer();

		sql.append("SELECT TTOD.DETAIL_ID,\n");
		sql.append("       TVM.MATERIAL_CODE,\n");  
		sql.append("       TVM.MATERIAL_NAME,\n");  
		sql.append("       TTOD.ORDER_AMOUNT,\n");  
		sql.append("       G3.GROUP_NAME\n");  
		sql.append("  FROM TT_VS_ORDER_DETAIL       TTOD,\n");  
		sql.append("       TM_VHCL_MATERIAL         TVM,\n");  
		sql.append("       TM_VHCL_MATERIAL_GROUP_R R,\n");  
		sql.append("       TM_VHCL_MATERIAL_GROUP   G1,\n");  
		sql.append("       TM_VHCL_MATERIAL_GROUP   G2,\n");  
		sql.append("       TM_VHCL_MATERIAL_GROUP   G3\n");  
		sql.append(" WHERE TTOD.MATERIAL_ID = TVM.MATERIAL_ID\n");  
		sql.append("   AND TVM.MATERIAL_ID = R.MATERIAL_ID\n");  
		sql.append("   AND R.GROUP_ID = G1.GROUP_ID\n");  
		sql.append("   AND G1.PARENT_GROUP_ID = G2.GROUP_ID\n");  
		sql.append("   AND G2.PARENT_GROUP_ID = G3.GROUP_ID\n");  
		sql.append("   AND TTOD.ORDER_ID = "+orderId+"\n");  
		sql.append(" ORDER BY TTOD.DETAIL_ID\n");

		List<Map<String, Object>> list = dao.pageQuery(sql.toString(), null,dao.getFunName());
		return list;
	}
	
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
}
