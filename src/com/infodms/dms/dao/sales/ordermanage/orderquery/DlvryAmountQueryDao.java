package com.infodms.dms.dao.sales.ordermanage.orderquery;

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

public class DlvryAmountQueryDao  extends BaseDao{

	public static Logger logger = Logger.getLogger(CheckVehicleDAO.class);
	private static final DlvryAmountQueryDao dao = new DlvryAmountQueryDao ();
	public static final DlvryAmountQueryDao getInstance() {
		return dao;
	}
	
	public static PageResult <Map<String,Object>> getAmountQueryList(String regionId, String dutyType,String pageOrgId, Long orgId, String deliveryStatus,String areaId,String orderOrgCode,String billingOrgCode,String startDate,String endDate , String beginDate, String overDate,int pageSize,int curPage){
		boolean orgFlag = Constant.DUTY_TYPE_LARGEREGION.toString().equals(dutyType) || (!CommonUtils.isNullString(pageOrgId) && Constant.DUTY_TYPE_COMPANY.toString().equals(dutyType) && !orgId.toString().equals(pageOrgId)) ;
		
		StringBuffer sql = new StringBuffer("");
		
		sql.append("SELECT TMD.DEALER_CODE BILLINGORGCODE, --开票经销商代码,\n");
		sql.append("       TMD.DEALER_SHORTNAME BILLINGORGNAME, --开票经销商名称,\n");  
		sql.append("       TMD.CREATE_DATE, --系统开通时间,\n");  
		sql.append("       DECODE(TVDR.CALL_LEAVEL, ").append(Constant.DEALER_LEVEL_02).append(", OMD.DEALER_CODE, SOD.DEALER_CODE) ORDERORGCODE, --订货经销商代码,\n");  
		sql.append("       DECODE(TVDR.CALL_LEAVEL, ").append(Constant.DEALER_LEVEL_02).append(", OMD.DEALER_SHORTNAME, SOD.DEALER_SHORTNAME) ORDERORGNAME, --订货经销商名称,\n"); 
		sql.append("       DECODE(TVDR.CALL_LEAVEL, ").append(Constant.DEALER_LEVEL_02).append(", OMD.CREATE_DATE, SOD.CREATE_DATE) ORDER_CREATE_DATE, --订货系统开通时间,\n");
		sql.append("       MG.GROUP_CODE STATUSCODE, --状态代码,\n");  
		sql.append("       TO_CHAR(D.DELIVERY_DATE,'YYYY-MM-DD') DELIVERY_DATE,\n");
		sql.append("	   BA.AREA_NAME AREA_ID,\n");
		sql.append("       SUM(CASE\n");  
		sql.append("             WHEN D.DELIVERY_STATUS = "+Constant.DELIVERY_STATUS_01+" THEN\n");  
		sql.append("              NVL(DT.DELIVERY_AMOUNT, 0)\n");  
		sql.append("             ELSE\n");  
		sql.append("              0\n");  
		sql.append("           END) FINANCE, --待财务确认,\n");  
		sql.append("       SUM(CASE\n");  
		sql.append("             WHEN D.DELIVERY_STATUS = "+Constant.DELIVERY_STATUS_06+" THEN\n");  
		sql.append("              NVL(DT.DELIVERY_AMOUNT, 0)\n");  
		sql.append("             ELSE\n");  
		sql.append("              0\n");  
		sql.append("           END) ERP, --ERP待导入,\n");  
		sql.append("       SUM(CASE\n");  
		sql.append("             WHEN D.DELIVERY_STATUS = "+Constant.DELIVERY_STATUS_07+" THEN\n");  
		sql.append("              NVL(DT.DELIVERY_AMOUNT, 0)\n");  
		sql.append("             ELSE\n");  
		sql.append("              0\n");  
		sql.append("           END) ORDERSALES, --销售订单生成\n");  
		sql.append("SUM(CASE\n");
		sql.append("      WHEN D.DELIVERY_STATUS = "+Constant.DELIVERY_STATUS_04+" THEN\n");  
		sql.append("       NVL(DT.DELIVERY_AMOUNT, 0)\n");  
		sql.append("      ELSE\n");  
		sql.append("       0\n");  
		sql.append("    END) HASBILL, --已开票\n");  
		sql.append("SUM(CASE\n");  
		sql.append("      WHEN (D.DELIVERY_STATUS = "+Constant.DELIVERY_STATUS_05+" OR D.DELIVERY_STATUS = "+Constant.DELIVERY_STATUS_10+") THEN\n");  
		sql.append("       NVL(DT.DELIVERY_AMOUNT, 0)\n");  
		sql.append("      ELSE\n");  
		sql.append("       0\n");  
		sql.append("    END) HASOUT, --已出库\n");  
		sql.append("SUM(CASE\n");  
		sql.append("      WHEN (D.DELIVERY_STATUS = "+Constant.DELIVERY_STATUS_11+" OR D.DELIVERY_STATUS = "+Constant.DELIVERY_STATUS_12+") THEN\n");  
		sql.append("       NVL(DT.DELIVERY_AMOUNT, 0)\n");  
		sql.append("      ELSE\n");  
		sql.append("       0\n");  
		sql.append("    END) HASCHECK, --已验收\n");
		sql.append("VOD.ROOT_ORG_NAME ORG_NAME\n");

		sql.append("  FROM TT_VS_DLVRY              D,\n");  
		sql.append("       TT_VS_DLVRY_DTL          DT,\n");
		sql.append("       TT_VS_DLVRY_REQ          TVDR,\n"); 
		sql.append("       TM_DEALER                OMD,\n");  
		sql.append("       TM_DEALER                TMD,\n");  
		sql.append("       TM_DEALER                SOD,\n");  
		sql.append("       TM_VHCL_MATERIAL         M,\n");  
		sql.append("       TM_VHCL_MATERIAL_GROUP_R GR,\n");  
		sql.append("       TM_VHCL_MATERIAL_GROUP   MG,\n");  
		sql.append("       TT_VS_ORDER              O,\n");  
		sql.append("       TM_BUSINESS_AREA  BA,\n");
		sql.append("       TM_DEALER_ORG_RELATION   DOR,\n");
		sql.append("	   vw_org_dealer VOD");
		//sql.append("       TM_ORG                   TMO\n");

		sql.append(" WHERE D.DELIVERY_ID = DT.DELIVERY_ID\n");  
		sql.append("   AND D.REQ_ID = TVDR.REQ_ID\n");  
		sql.append("   AND TVDR.ORDER_DEALER_ID = OMD.DEALER_ID(+)\n");  
		sql.append("   AND D.BILLING_SIDE = TMD.DEALER_ID\n");  
		sql.append("   AND D.ORDER_ID = O.ORDER_ID \n");  
		sql.append("   AND O.ORDER_ORG_ID = SOD.DEALER_ID\n");  
		sql.append("   AND MG.GROUP_ID = GR.GROUP_ID\n");  
		sql.append("   AND M.MATERIAL_ID = GR.MATERIAL_ID\n");  
		sql.append("   AND M.MATERIAL_ID = DT.MATERIAL_ID\n");  
		sql.append("   AND BA.AREA_ID = O.AREA_ID\n");
		sql.append("   AND VOD.DEALER_ID=TMD.DEALER_ID \n");
		sql.append("   AND VOD.ROOT_DEALER_ID = DOR.DEALER_ID\n");
		sql.append("   AND DOR.ORG_ID =  VOD.ROOT_ORG_ID\n");

		if(orgFlag) {
			sql.append("AND O.BILLING_ORG_ID = VOD.DEALER_ID\n");
			
			if(!CommonUtils.isNullString(pageOrgId)) {
				orgId = Long.parseLong(pageOrgId) ;
			}
			
			sql.append("AND VOD.ROOT_ORG_ID = ").append(orgId).append("\n");
		}
		
		if (!CommonUtils.isNullString(regionId)) {
			sql.append("AND TMD.PROVINCE_ID = ").append(regionId).append("\n");
		}
		
		if (null != areaId && !"".equals(areaId)) {
			sql.append("   AND O.AREA_ID in ("+areaId+")\n");

		}
		sql.append("   AND D.DELIVERY_STATUS IN ("+Constant.DELIVERY_STATUS_01+","+Constant.DELIVERY_STATUS_04+","+Constant.DELIVERY_STATUS_05+", "+Constant.DELIVERY_STATUS_06+", "+Constant.DELIVERY_STATUS_07+","+Constant.DELIVERY_STATUS_10+","+Constant.DELIVERY_STATUS_11+","+Constant.DELIVERY_STATUS_12+")\n");  
		if (null != startDate && !"".equals(startDate)) {
			sql.append("   AND D.DELIVERY_DATE >= TRUNC(TO_DATE('"+startDate+"', 'yyyy-mm-dd'))\n");
		}
		if (null != endDate && !"".equals(endDate)) {
			sql.append("   AND D.DELIVERY_DATE <=  (TRUNC(TO_DATE('"+endDate+"', 'yyyy-mm-dd')) + 86399 / 86400)\n");
		}
		
		if (null != beginDate && !"".equals(beginDate)) {
			sql.append("   AND D.BILLING_DATE >= TRUNC(TO_DATE('"+beginDate+"', 'yyyy-mm-dd'))\n");
		}
		if (null != overDate && !"".equals(overDate)) {
			sql.append("   AND D.BILLING_DATE <=  (TRUNC(TO_DATE('"+overDate+"', 'yyyy-mm-dd')) + 86399 / 86400)\n");
		}

		if (null != orderOrgCode && !"".equals(orderOrgCode)) {
			String[] array = orderOrgCode.split(",");
			sql.append("   AND ( SOD.DEALER_CODE IN (\n");
			for (int i = 0; i < array.length; i++) {
				sql.append("'" + array[i] + "'");
				if (i != array.length - 1) {
					sql.append(",");
				}
			}
			sql.append(")\n");
			sql.append("   OR OMD.DEALER_CODE IN (\n");
			for (int i = 0; i < array.length; i++) {
				sql.append("'" + array[i] + "'");
				if (i != array.length - 1) {
					sql.append(",");
				}
			}
			sql.append("))\n");
			
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
		if (null != deliveryStatus && !"".equals(deliveryStatus)) {
			sql.append("   AND D.DELIVERY_STATUS = "+deliveryStatus+"\n");
		}
		
		sql.append(" GROUP BY TMD.DEALER_CODE,\n");  
		sql.append("          TMD.DEALER_SHORTNAME,\n");  
		sql.append("          TMD.CREATE_DATE,\n");  
		sql.append("          MG.GROUP_CODE,\n");  
		sql.append("          DECODE(TVDR.CALL_LEAVEL, ").append(Constant.DEALER_LEVEL_02).append(", OMD.DEALER_CODE, SOD.DEALER_CODE),\n");  
		sql.append("          DECODE(TVDR.CALL_LEAVEL, ").append(Constant.DEALER_LEVEL_02).append(", OMD.DEALER_SHORTNAME, SOD.DEALER_SHORTNAME),\n");  
		sql.append("          DECODE(TVDR.CALL_LEAVEL, ").append(Constant.DEALER_LEVEL_02).append(", OMD.CREATE_DATE, SOD.CREATE_DATE),\n");  
		sql.append("          VOD.ROOT_ORG_NAME,\n");
		sql.append("		  D.DELIVERY_DATE ,BA.AREA_NAME\n");
		sql.append(" ORDER BY TMD.DEALER_CODE, DECODE(TVDR.CALL_LEAVEL, ").append(Constant.DEALER_LEVEL_02).append(", OMD.DEALER_CODE, SOD.DEALER_CODE), MG.GROUP_CODE\n");

		return dao.pageQuery(sql.toString(), null,dao.getFunName(),pageSize ,curPage );
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
	
    public  PageResult <Map<String,Object>> queryAllProvinceInvoiceDetail(String dealerCode,String beginTime,String endTime,int pageSize,int curPage){  //YH 2010.12.6 启票明细查询 
		
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT DISTINCT TO_CHAR(TVDD.BILLING_DATE,'YYYY-MM-DD') BILLING_DATE,\n");
		sql.append("TVO.ORDER_NO,\n");
		sql.append("TVD.ERP_ORDER,\n");
		sql.append("TVDD.BILLING_AMOUNT,\n");
		sql.append("TVM.MATERIAL_CODE,\n");
		sql.append("TVDD.DELIVERY_AMOUNT,\n");
		sql.append("TVA.ADDRESS,\n");
		sql.append("TMD.DEALER_NAME DEALER_NAME1,\n");
		sql.append("TMD1.DEALER_NAME DEALER_NAME2,\n");
		sql.append("TMD2.DEALER_NAME DEALER_NAME3,\n");
		sql.append("DECODE(TBA.ERP_CODE,726,'重庆',82,'重庆',142,'河北',197,'南京') CODE_DESC\n");
		sql.append("FROM TT_VS_ORDER      TVO,\n");
		sql.append(" TT_VS_DLVRY_DTL  TVDD,\n");
		sql.append("TT_VS_DLVRY      TVD,\n");
		sql.append(" TM_VHCL_MATERIAL TVM,\n");
		sql.append("TM_VS_ADDRESS TVA,\n");
		sql.append("TM_DEALER TMD,\n");
		sql.append("TM_DEALER TMD1,\n");
		sql.append("TM_DEALER TMD2,\n");
		sql.append("TM_BUSINESS_AREA tba \n");
		sql.append("WHERE TVO.ORDER_ID = TVD.ORDER_ID\n");
		sql.append("AND TVD.DELIVERY_ID = TVDD.Delivery_Id\n");
//		sql.append("AND TRUNC(TVDD.BILLING_DATE) >= TO_DATE('2010-11-01', 'yyyy-mm-dd')\n");
//		sql.append("AND TRUNC(TVDD.BILLING_DATE) <= TO_DATE('2010-11-30', 'yyyy-mm-dd')\n");
		sql.append("AND TVM.MATERIAL_ID = TVDD.MATERIAL_ID\n");
		sql.append("-- AND TVD.ADDRESS_ID=TVA.Id(+)\n");
		sql.append("AND TVO.DELIVERY_ADDRESS=TVA.ID(+)\n");
		sql.append("AND TMD.DEALER_ID=TVD.RECEIVER\n");
		sql.append(" AND TMD1.DEALER_ID=TVO.BILLING_ORG_ID\n");
		sql.append("AND TMD2.DEALER_ID= TVO.ORDER_ORG_ID\n");
		sql.append("and TVD.DELIVERY_STATUS in (10281004,10281005,10281010,10281011,10281012)\n");
		sql.append("and TVO.AREA_ID=TBA.AREA_ID\n");
		
		if(null != dealerCode && !"".equals(dealerCode)){
			sql.append("  AND TMD2.DEALER_CODE LIKE '%"+dealerCode+"%'");
		}
		if (null != beginTime && !"".equals(beginTime)) {
			sql.append("  AND TRUNC(TVDD.BILLING_DATE) >= TO_DATE('"+beginTime+"', 'yyyy-mm-dd') \n");
		}
		if (null != endTime && !"".equals(endTime)) {
			sql.append("  AND TRUNC(TVDD.BILLING_DATE) <= TO_DATE('"+endTime+"', 'yyyy-mm-dd') \n");
		}
		
		sql.append("   ORDER BY BILLING_DATE DESC");
		
		return dao.pageQuery(sql.toString(), null,dao.getFunName(),pageSize ,curPage );
	}	
	
    public List<Map<String, Object>> dlvryDtlDownload(Map<String, String> map) {
    	String billingStartDate = map.get("billingStartDate") ;
    	String billingEndDate = map.get("billingEndDate") ;
    	String dlvryStartDate = map.get("dlvryStartDate") ;
    	String dlvryEndDate = map.get("dlvryEndDate") ;
    	String areaId = map.get("areaId") ;
    	String orgId = map.get("orgId") ;
    	String regionCode = map.get("regionCode") ;
    	String billingDlrCode = map.get("billingDlrCode") ;
    	String orderDlrCode = map.get("orderDlrCode") ;
    	String deliveryStatus = map.get("deliveryStatus") ;
    	
    	StringBuffer sql = new StringBuffer("\n") ;
    	
    	sql.append("SELECT BA.AREA_NAME,\n");
    	sql.append("       VOD.ROOT_ORG_NAME,\n");  
    	sql.append("       TMD.DEALER_SHORTNAME bill_shortname,\n");  
    	sql.append("       TMD.DEALER_CODE BILL_CODE,\n");  
    	sql.append("       TMD.CREATE_DATE BILL_DATE,\n");  
    	sql.append("       DECODE(TVDR.CALL_LEAVEL, ").append(Constant.DEALER_LEVEL_02).append(", OMD.DEALER_CODE, SOD.DEALER_CODE) ORDER_CODE,\n");  
    	sql.append("       DECODE(TVDR.CALL_LEAVEL,\n");  
    	sql.append("              ").append(Constant.DEALER_LEVEL_02).append(",\n");  
    	sql.append("              OMD.DEALER_SHORTNAME,\n");  
    	sql.append("              SOD.DEALER_SHORTNAME) ORDER_SHORTNAME,\n");  
    	sql.append("       DECODE(TVDR.CALL_LEAVEL, ").append(Constant.DEALER_LEVEL_02).append(", OMD.CREATE_DATE, SOD.CREATE_DATE) ORDER_DATE,\n");  
    	sql.append("       d.delivery_no,\n");  
    	sql.append("       d.erp_order,\n");  
    	sql.append("       decode(o.order_type,\n");  
    	sql.append("              ").append(Constant.ORDER_TYPE_01).append(",\n");  
    	sql.append("              '常规订单',\n");  
    	sql.append("              ").append(Constant.ORDER_TYPE_02).append(",\n");  
    	sql.append("              '补充订单',\n");  
    	sql.append("              ").append(Constant.ORDER_TYPE_03).append(",\n");  
    	sql.append("              '订做车订单') ORDER_TYPE,\n");  
    	sql.append("       M.MATERIAL_CODE,\n");  
    	sql.append("       TO_CHAR(D.DELIVERY_DATE, 'YYYY-MM-DD') DELIVERY_DATE,\n");  
    	sql.append("       SUM(CASE\n");  
    	sql.append("             WHEN D.DELIVERY_STATUS = ").append(Constant.DELIVERY_STATUS_01).append(" THEN\n");  
    	sql.append("              NVL(DT.DELIVERY_AMOUNT, 0)\n");  
    	sql.append("             ELSE\n");  
    	sql.append("              0\n");  
    	sql.append("           END) CWDQR,\n");  
    	sql.append("       SUM(CASE\n");  
    	sql.append("             WHEN D.DELIVERY_STATUS = ").append(Constant.DELIVERY_STATUS_06).append(" THEN\n");  
    	sql.append("              NVL(DT.DELIVERY_AMOUNT, 0)\n");  
    	sql.append("             ELSE\n");  
    	sql.append("              0\n");  
    	sql.append("           END) ERPDDR,\n");  
    	sql.append("       SUM(CASE\n");  
    	sql.append("             WHEN D.DELIVERY_STATUS = ").append(Constant.DELIVERY_STATUS_07).append(" THEN\n");  
    	sql.append("              NVL(DT.DELIVERY_AMOUNT, 0)\n");  
    	sql.append("             ELSE\n");  
    	sql.append("              0\n");  
    	sql.append("           END) XSDDSC,\n");  
    	sql.append("       SUM(CASE\n");  
    	sql.append("             WHEN D.DELIVERY_STATUS = ").append(Constant.DELIVERY_STATUS_04).append(" THEN\n");  
    	sql.append("              NVL(DT.DELIVERY_AMOUNT, 0)\n");  
    	sql.append("             ELSE\n");  
    	sql.append("              0\n");  
    	sql.append("           END) YKP,\n");  
    	sql.append("       SUM(CASE\n");  
    	sql.append("             WHEN (D.DELIVERY_STATUS = ").append(Constant.DELIVERY_STATUS_05).append(" OR D.DELIVERY_STATUS = ").append(Constant.DELIVERY_STATUS_10).append(") THEN\n");  
    	sql.append("              NVL(DT.DELIVERY_AMOUNT, 0)\n");  
    	sql.append("             ELSE\n");  
    	sql.append("              0\n");  
    	sql.append("           END) YCK,\n");  
    	sql.append("       SUM(CASE\n");  
    	sql.append("             WHEN (D.DELIVERY_STATUS = ").append(Constant.DELIVERY_STATUS_11).append(" OR D.DELIVERY_STATUS = ").append(Constant.DELIVERY_STATUS_12).append(") THEN\n");  
    	sql.append("              NVL(DT.DELIVERY_AMOUNT, 0)\n");  
    	sql.append("             ELSE\n");  
    	sql.append("              0\n");  
    	sql.append("           END) YYS\n");  
    	sql.append("\n");  
    	sql.append("  FROM TT_VS_DLVRY            D,\n");  
    	sql.append("       TT_VS_DLVRY_DTL        DT,\n");  
    	sql.append("       TT_VS_DLVRY_REQ        TVDR,\n");  
    	sql.append("       TM_DEALER              OMD,\n");  
    	sql.append("       TM_DEALER              TMD,\n");  
    	sql.append("       TM_DEALER              SOD,\n");  
    	sql.append("       TM_VHCL_MATERIAL       M,\n");  
    	sql.append("       TT_VS_ORDER            O,\n");  
    	sql.append("       TM_BUSINESS_AREA       BA,\n");  
    	sql.append("       TM_DEALER_ORG_RELATION DOR,\n");  
    	sql.append("       vw_org_dealer          VOD\n");  
    	sql.append(" WHERE D.DELIVERY_ID = DT.DELIVERY_ID\n");  
    	sql.append("   AND D.REQ_ID = TVDR.REQ_ID\n");  
    	sql.append("   AND TVDR.ORDER_DEALER_ID = OMD.DEALER_ID(+)\n");  
    	sql.append("   AND D.BILLING_SIDE = TMD.DEALER_ID\n");  
    	sql.append("   AND D.ORDER_ID = O.ORDER_ID\n");  
    	sql.append("   AND O.ORDER_ORG_ID = SOD.DEALER_ID\n");  
    	sql.append("   AND M.MATERIAL_ID = DT.MATERIAL_ID\n");  
    	sql.append("   AND BA.AREA_ID = O.AREA_ID\n");  
    	sql.append("   AND VOD.DEALER_ID = TMD.DEALER_ID\n");  
    	sql.append("   AND VOD.ROOT_DEALER_ID = DOR.DEALER_ID\n");  
    	sql.append("   AND DOR.ORG_ID = VOD.ROOT_ORG_ID\n");  
    	sql.append("   AND D.DELIVERY_STATUS IN (").append(deliveryStatus).append(")\n");  
    	
    	if(!CommonUtils.isNullString(orgId)) {
    		sql.append("   AND VOD.ROOT_ORG_ID = ").append(orgId).append("\n");
    	}
    	
    	if(!CommonUtils.isNullString(billingDlrCode)) {
    		sql.append("   AND TMD.DEALER_CODE IN (").append(billingDlrCode).append(")\n");
    	}
    	
    	if(!CommonUtils.isNullString(orderDlrCode)) {
    		sql.append("   AND (SOD.DEALER_CODE IN (").append(orderDlrCode).append(") OR OMD.DEALER_CODE IN (").append(orderDlrCode).append("))\n");
    	}
    	
    	if(!CommonUtils.isNullString(regionCode)) {
    		sql.append("   AND TMD.PROVINCE_ID = ").append(regionCode).append("\n");
    	}
    	
    	if(!CommonUtils.isNullString(areaId)) {
    		sql.append("   AND O.AREA_ID in (").append(areaId).append(")\n");
    	} else {
    		sql.append("   AND O.AREA_ID = ").append("-1").append("\n");  //"-1"表示不存在对应业务范围
    	}
    	
    	if(!CommonUtils.isNullString(dlvryStartDate)) {
    		sql.append("   AND D.DELIVERY_DATE >= TO_DATE('").append(dlvryStartDate).append("', 'yyyy-mm-dd')\n");
    	}
    	
    	if(!CommonUtils.isNullString(dlvryEndDate)) {
    		sql.append("   AND D.DELIVERY_DATE <=\n");
    		sql.append("       (TO_DATE('").append(dlvryEndDate).append("', 'yyyy-mm-dd') + 86399 / 86400)\n");
    	}
    	
    	if(!CommonUtils.isNullString(billingStartDate)) {
    		sql.append("   AND D.BILLING_DATE >= TO_DATE('").append(billingStartDate).append("', 'yyyy-mm-dd')\n");  
    	}
    	
    	if(!CommonUtils.isNullString(billingEndDate)) {
	    	sql.append("   AND D.BILLING_DATE <=\n");  
	    	sql.append("       (TO_DATE('").append(billingEndDate).append("', 'yyyy-mm-dd') + 86399 / 86400)\n");  
    	}
    	
    	sql.append(" GROUP BY TMD.DEALER_CODE,\n");  
    	sql.append("          TMD.DEALER_SHORTNAME,\n");  
    	sql.append("          TMD.CREATE_DATE,\n");  
    	sql.append("          M.MATERIAL_CODE,\n");  
    	sql.append("          d.delivery_no,\n");  
    	sql.append("          d.erp_order,\n");  
    	sql.append("          o.order_type,\n");  
    	sql.append("          DECODE(TVDR.CALL_LEAVEL,\n");  
    	sql.append("                 ").append(Constant.DEALER_LEVEL_02).append(",\n");  
    	sql.append("                 OMD.DEALER_CODE,\n");  
    	sql.append("                 SOD.DEALER_CODE),\n");  
    	sql.append("          DECODE(TVDR.CALL_LEAVEL,\n");  
    	sql.append("                 ").append(Constant.DEALER_LEVEL_02).append(",\n");  
    	sql.append("                 OMD.DEALER_SHORTNAME,\n");  
    	sql.append("                 SOD.DEALER_SHORTNAME),\n");  
    	sql.append("          DECODE(TVDR.CALL_LEAVEL,\n");  
    	sql.append("                 ").append(Constant.DEALER_LEVEL_02).append(",\n");  
    	sql.append("                 OMD.CREATE_DATE,\n");  
    	sql.append("                 SOD.CREATE_DATE),\n");  
    	sql.append("          VOD.ROOT_ORG_NAME,\n");  
    	sql.append("          D.DELIVERY_DATE,\n");  
    	sql.append("          BA.AREA_NAME\n");  
    	sql.append(" ORDER BY TMD.DEALER_CODE,\n");  
    	sql.append("          DECODE(TVDR.CALL_LEAVEL, ").append(Constant.DEALER_LEVEL_02).append(", OMD.DEALER_CODE, SOD.DEALER_CODE),\n");
    	sql.append("          M.MATERIAL_CODE\n");

    	return super.pageQuery(sql.toString(), null, super.getFunName()) ;
	}
}
