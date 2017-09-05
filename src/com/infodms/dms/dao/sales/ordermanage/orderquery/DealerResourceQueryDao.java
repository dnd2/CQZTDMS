package com.infodms.dms.dao.sales.ordermanage.orderquery;

import java.sql.ResultSet;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.dao.sales.ordermanage.reqquery.ReqQueryDao;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

public class DealerResourceQueryDao extends BaseDao{

	public static Logger logger = Logger.getLogger(DealerResourceQueryDao.class);
	private static final DealerResourceQueryDao dao = new DealerResourceQueryDao ();
	public static final DealerResourceQueryDao getInstance() {
		return dao;
	}
	
	public static PageResult <Map<String,Object>> getRsourceList(String orgId, String areaId,String orderOrgCode,String billingOrgCode,String startDate,String endDate ,int pageSize,int curPage){
		StringBuffer sql = new StringBuffer("");
		
		sql.append("SELECT SOD.DEALER_CODE ORDERORGCODE, --订货经销商代码,\n");
		sql.append("       SOD.DEALER_SHORTNAME ORDERORGNAME, --订货经销商名称,\n");  
		sql.append("       TMD.DEALER_CODE BILLINGORGCODE, --开票经销商代码,\n");  
		sql.append("       TMD.DEALER_SHORTNAME BILLINGORGNAME, --开票经销商名称,\n");  
		sql.append("       MG.GROUP_CODE STATUSCODE, --状态代码,\n");  
		sql.append("       SUM(CASE\n");  
		sql.append("             WHEN (D.REQ_STATUS = "+Constant.ORDER_REQ_STATUS_01+" OR D.REQ_STATUS = "+Constant.ORDER_REQ_STATUS_05+") THEN\n");  
		sql.append("              NVL(DT.REQ_AMOUNT, 0)\n");  
		sql.append("             ELSE\n");  
		sql.append("              0\n");  
		sql.append("           END) HAS_COMMIT, --已提报(已提报,驳回初审)\n"); 
		sql.append("       SUM(CASE\n");  
		sql.append("             WHEN D.REQ_STATUS = "+Constant.ORDER_REQ_STATUS_08+" THEN\n");  
		sql.append("              NVL(DT.RESERVE_AMOUNT, 0)\n");  
		sql.append("             ELSE\n");  
		sql.append("              0\n");  
		sql.append("           END) DJC_CHECK, --代交车审核\n"); 
		sql.append("       SUM(CASE\n");  
		sql.append("             WHEN D.REQ_STATUS = "+Constant.ORDER_REQ_STATUS_02+" THEN\n");  
		sql.append("              NVL(DT.RESERVE_AMOUNT, 0)\n");  
		sql.append("             ELSE\n");  
		sql.append("              0\n");  
		sql.append("           END) DEALERCOMMIT, --经销商待确认\n");  
		sql.append("       SUM(CASE\n");  
		sql.append("             WHEN D.REQ_STATUS = "+Constant.ORDER_REQ_STATUS_03+" THEN\n");  
		sql.append("              NVL(DT.RESERVE_AMOUNT, 0)\n");  
		sql.append("             ELSE\n");  
		sql.append("              0\n");  
		sql.append("           END) PRE_CHECK, --初审完成\n");  
		
		sql.append("       SUM(CASE\n");  
		sql.append("             WHEN (D.REQ_STATUS = "+Constant.ORDER_REQ_STATUS_04+" OR D.REQ_STATUS = "+Constant.ORDER_REQ_STATUS_06+") THEN\n");  
		sql.append("              NVL(DT.RESERVE_AMOUNT, 0)\n");  
		sql.append("             ELSE\n");  
		sql.append("              0\n");  
		sql.append("           END) HAS_CHECK, --审核完成\n");  
		sql.append("  TMO.ORG_NAME\n");
		sql.append("  FROM TT_VS_DLVRY_REQ          D,\n");  
		sql.append("       TT_VS_DLVRY_REQ_DTL      DT,\n");  
		sql.append("       TM_DEALER                TMD,\n");  
		sql.append("       TM_DEALER                SOD,\n");  
		sql.append("       TM_VHCL_MATERIAL         M,\n");  
		sql.append("       TM_VHCL_MATERIAL_GROUP_R GR,\n");  
		sql.append("       TM_VHCL_MATERIAL_GROUP   MG,\n");  
		sql.append("       TT_VS_ORDER              O,\n");  
		sql.append("       TM_BUSINESS_AREA  		BA,\n");
		sql.append("       TM_DEALER_ORG_RELATION   DOR,\n");
		sql.append("       TM_ORG                   TMO\n");

		sql.append(" WHERE D.REQ_ID = DT.REQ_ID\n");  
		sql.append("   AND D.ORDER_ID = O.ORDER_ID\n");  
		sql.append("   AND O.BILLING_ORG_ID = TMD.DEALER_ID\n");  
		sql.append("   AND O.ORDER_ORG_ID = SOD.DEALER_ID\n");  
		sql.append("   AND MG.GROUP_ID = GR.GROUP_ID\n");  
		sql.append("   AND M.MATERIAL_ID = GR.MATERIAL_ID\n");  
		sql.append("   AND M.MATERIAL_ID = DT.MATERIAL_ID\n");  
		sql.append("   AND BA.AREA_ID = O.AREA_ID\n");
		sql.append("   AND TMD.DEALER_ID = DOR.DEALER_ID\n");
		sql.append("   AND DOR.ORG_ID = TMO.ORG_ID\n");

		sql.append("   AND D.REQ_STATUS IN ("+Constant.ORDER_REQ_STATUS_01+","+Constant.ORDER_REQ_STATUS_02+","+Constant.ORDER_REQ_STATUS_03+","+Constant.ORDER_REQ_STATUS_04+","+Constant.ORDER_REQ_STATUS_05+","+Constant.ORDER_REQ_STATUS_06+","+Constant.ORDER_REQ_STATUS_08+")\n");  
		if (null != orderOrgCode && !"".equals(orderOrgCode)) {
			String[] array = orderOrgCode.split(",");
			sql.append("   AND SOD.DEALER_CODE IN (\n");
			for (int i = 0; i < array.length; i++) {
				sql.append("'" + array[i] + "'");
				if (i != array.length - 1) {
					sql.append(",");
				}
			}
			sql.append(")\n");
		}
		if (null != billingOrgCode && !"".equals(billingOrgCode)) {
			String[] array = billingOrgCode.split(",");
			sql.append("   AND TMD.DEALER_CODE IN (\n");
			for (int i = 0; i < array.length; i++) {
				sql.append("'" + array[i] + "'");
				if (i != array.length - 1) {
					sql.append(",");
				}
			}
			sql.append(")\n");
		}
		if (null != areaId && !"".equals(areaId)) {
			sql.append("   AND O.AREA_ID ="+areaId+"\n");

		}
		if (null != startDate && !"".equals(startDate)) {
			sql.append("   AND D.REQ_DATE >= TO_DATE('"+startDate+" 00:00:00','yyyy-MM-dd HH24:MI:SS')\n");
		}
		if (null != endDate && !"".equals(endDate)) {
			sql.append("   AND D.REQ_DATE <= TO_DATE('"+endDate+" 23:59:59','yyyy-MM-dd HH24:MI:SS')\n");
		}
		
		if(!CommonUtils.isNullString(orgId)) {
			sql.append(ReqQueryDao.limitOrg("TMD", "dealer_id", orgId)) ;
		}
		
		sql.append(" GROUP BY TMD.DEALER_CODE,\n");  
		sql.append("          TMD.DEALER_SHORTNAME,\n");  
		sql.append("          MG.GROUP_NAME,\n");  
		sql.append("          SOD.DEALER_CODE,\n");  
		sql.append("          SOD.DEALER_SHORTNAME,\n");
		sql.append("          TMO.ORG_NAME,MG.GROUP_CODE\n");

		return dao.pageQuery(sql.toString(), null,dao.getFunName(),pageSize ,curPage );
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}

}
