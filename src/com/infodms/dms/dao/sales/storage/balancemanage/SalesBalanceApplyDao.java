
package com.infodms.dms.dao.sales.storage.balancemanage;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.infodms.dms.common.Constant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.dao.sales.storage.StorageUtil;
import com.infodms.dms.po.TtSalesAssignPO;
import com.infodms.dms.po.TtSalesBoardPO;
import com.infodms.dms.po.TtVsOrderPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

/**
 * 运费结算申请
 * @author shuyh
 *
 */
public class SalesBalanceApplyDao extends BaseDao<PO> {
	private static final SalesBalanceApplyDao dao = new SalesBalanceApplyDao();
	public static final SalesBalanceApplyDao getInstance()
	{
		return dao;
	}
	
	@Override
	protected PO wrapperPO(ResultSet rs, int idx)
	{
		return null;
	}
	public PageResult<Map<String, Object>> getBalanceAuditQuery(Map<String, Object> map, int curPage, int pageSize) throws Exception
	{
		String applyNo = (String) map.get("applyNo"); //申请单号
		String logiId = (String) map.get("logiId"); // 物流商ID
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT TSB.APPLY_ID,\n" );
		sql.append("       TSL.LOGI_NAME,\n" );
		sql.append("       TSB.BAL_COUNT,\n" );
		sql.append("       TSB.BAL_AMOUNT,\n" );
		sql.append("       TSB.DEDUCT_MONEY,\n" );
		sql.append("       TSB.OTHER_MONEY,\n" );
		sql.append("       TSB.SUPPLY_MONEY,\n" );
		sql.append("       DECODE(TSS.IS_CHANGE,\n" );
		sql.append("              10041001,\n" );
		sql.append("              '<font color=red>' || TSB.APPLY_NO || '</font>',\n" );
		sql.append("              TSB.APPLY_NO) APPLY_NO2,TSB.APPLY_NO,\n" );
		sql.append("       NVL(TSB.BAL_AMOUNT, 0) + NVL(TSB.SUPPLY_MONEY, 0) -\n" );
		sql.append("       NVL(TSB.DEDUCT_MONEY, 0) + NVL(TSB.OTHER_MONEY, 0) SUM_AMOUNT --结算合计\n" );
		sql.append("  FROM TT_SALES_BAL_APPLY TSB,\n" );
		sql.append("       TT_SALES_LOGI TSL,\n" );
		sql.append("       (SELECT MIN(TSW.IS_CHANGE) IS_CHANGE, TSW.APPLY_ID\n" );
		sql.append("          FROM TT_SALES_WAYBILL TSW\n" );
		sql.append("         WHERE TSW.APPLY_ID IS NOT NULL\n" );
		sql.append("         group by TSW.APPLY_ID) TSS\n" );
		sql.append(" WHERE TSB.LOGI_ID = TSL.LOGI_ID\n" );
		sql.append("   AND TSS.APPLY_ID = TSB.APPLY_ID\n" );
		sql.append("   AND TSB.STATUS = "+Constant.BAL_ORDER_STATUS_02+" --已申请\n");
		List<Object> params = new LinkedList<Object>();
		if (applyNo != null && !"".equals(applyNo))
		{//申请单号
			
			sql.append("   AND TSB.APPLY_NO like ?\n");
			params.add("%"+applyNo+"%");
		}
		if (logiId != null && !"".equals(logiId))
		{//物流商
			sql.append("   AND TSB.LOGI_ID= ?\n" );
			params.add(logiId);
		}
		sql.append("ORDER BY TSB.CREATE_DATE DESC"); 
		PageResult<Map<String, Object>> ps= dao.pageQuery(sql.toString(),params, getFunName(),pageSize,curPage);
		return ps;
	}
	/**
	 * 发票补录列表查询
	 * @param map
	 * @param curPage
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	public PageResult<Map<String, Object>> getBalanceSupplyQuery(Map<String, Object> map, int curPage, int pageSize) throws Exception
	{
		String applyNo = (String) map.get("applyNo"); //申请单号
		String logiId = (String) map.get("logiId"); // 物流商ID
		String posBusType = (String) map.get("posBusType"); //职位类型
		BigDecimal logiIdU = (BigDecimal) map.get("logiIdU");//当前职位所属承运商ID
		
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT TSB.APPLY_ID,\n" );
		sql.append("       TSL.LOGI_NAME,\n" );
		sql.append("       TSB.BAL_COUNT,\n" );
		sql.append("       TSB.BAL_AMOUNT,\n" );
		sql.append("       TSB.DEDUCT_MONEY,\n" );
		sql.append("       TSB.OTHER_MONEY,\n" );
		sql.append("       TSB.SUPPLY_MONEY,\n" );
		sql.append("       TSB.APPLY_NO,\n" );
		sql.append("       NVL(TSB.BAL_AMOUNT, 0) + NVL(TSB.SUPPLY_MONEY, 0) -\n" );
		sql.append("       NVL(TSB.DEDUCT_MONEY, 0) + NVL(TSB.OTHER_MONEY, 0) SUM_AMOUNT --结算合计\n" );
		sql.append("  FROM TT_SALES_BAL_APPLY TSB,\n" );
		sql.append("       TT_SALES_LOGI TSL\n" );
		sql.append(" WHERE TSB.LOGI_ID = TSL.LOGI_ID\n" );
		sql.append("   AND TSB.STATUS = "+Constant.BAL_ORDER_STATUS_03+" --已审核\n");
		List<Object> params = new LinkedList<Object>();
		if (applyNo != null && !"".equals(applyNo))
		{//申请单号
			
			sql.append("   AND TSB.APPLY_NO like ?\n");
			params.add("%"+applyNo+"%");
		}
		if(posBusType.equals(String.valueOf(Constant.POSE_BUS_TYPE_WL))){//物流商
			sql.append("   AND TSB.LOGI_ID= ?\n" );
			params.add(logiIdU);
		}else{
			if (logiId != null && !"".equals(logiId))
			{//物流商
				sql.append("   AND TSB.LOGI_ID= ?\n" );
				params.add(logiId);
			}
		}
		
		sql.append("ORDER BY TSB.CREATE_DATE DESC"); 
		PageResult<Map<String, Object>> ps= dao.pageQuery(sql.toString(),params, getFunName(),pageSize,curPage);
		return ps;
	}
	/**
	 * 付款列表查询
	 * @param map
	 * @param curPage
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	public PageResult<Map<String, Object>> getBalancePayQuery(Map<String, Object> map, int curPage, int pageSize) throws Exception
	{
		String applyNo = (String) map.get("applyNo"); //申请单号
		String invoiceNo = (String) map.get("invoiceNo"); //发票号
		String logiId = (String) map.get("logiId"); // 物流商ID
		
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT TSB.APPLY_ID,\n" );
		sql.append("       TSL.LOGI_NAME,\n" );
		sql.append("       TSB.BAL_COUNT,\n" );
		sql.append("       TSB.BAL_AMOUNT,\n" );
		sql.append("       TSB.DEDUCT_MONEY,\n" );
		sql.append("       TSB.OTHER_MONEY,\n" );
		sql.append("       TSB.SUPPLY_MONEY,\n" );
		sql.append("       TSB.APPLY_NO,TSB.INVOICE_NO,\n" );
		sql.append("       NVL(TSB.BAL_AMOUNT, 0) + NVL(TSB.SUPPLY_MONEY, 0) -\n" );
		sql.append("       NVL(TSB.DEDUCT_MONEY, 0) + NVL(TSB.OTHER_MONEY, 0) SUM_AMOUNT --结算合计\n" );
		sql.append("  FROM TT_SALES_BAL_APPLY TSB,\n" );
		sql.append("       TT_SALES_LOGI TSL\n" );
		sql.append(" WHERE TSB.LOGI_ID = TSL.LOGI_ID\n" );
		sql.append("   AND TSB.STATUS = "+Constant.BAL_ORDER_STATUS_04+" --已补录\n");
		List<Object> params = new LinkedList<Object>();
		if (applyNo != null && !"".equals(applyNo))
		{//申请单号
			
			sql.append("   AND TSB.APPLY_NO like ?\n");
			params.add("%"+applyNo+"%");
		}
		if (invoiceNo != null && !"".equals(invoiceNo))
		{//发票号
			
			sql.append("   AND TSB.INVOICE_NO like ?\n");
			params.add("%"+invoiceNo+"%");
		}
		if (logiId != null && !"".equals(logiId))
		{//物流商
			sql.append("   AND TSB.LOGI_ID= ?\n" );
			params.add(logiId);
		}
		
		sql.append("ORDER BY TSB.CREATE_DATE DESC"); 
		PageResult<Map<String, Object>> ps= dao.pageQuery(sql.toString(),params, getFunName(),pageSize,curPage);
		return ps;
	}
	/**
	 * 结算申请查询
	 * @param map
	 * @param curPage
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	public PageResult<Map<String, Object>> getBalanceApplySubQuery(Map<String, Object> map, int curPage, int pageSize) throws Exception
	{
		String applyNo = (String) map.get("applyNo"); //申请单号
		String logiId = (String) map.get("logiId"); // 物流商ID
		String posBusType = (String) map.get("posBusType"); //职位类型
		BigDecimal logiIdU = (BigDecimal) map.get("logiIdU");//当前职位所属承运商ID
		
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT TSB.APPLY_ID,\n" );
		sql.append("       TSL.LOGI_NAME,\n" );
		sql.append("       TSB.BAL_COUNT,\n" );
		sql.append("       TSB.BAL_AMOUNT,\n" );
		sql.append("       TSB.DEDUCT_MONEY,\n" );
		sql.append("       TSB.OTHER_MONEY,\n" );
		sql.append("       TSB.SUPPLY_MONEY,\n" );
		sql.append("       TSB.APPLY_NO,\n" );
		sql.append("       NVL(TSB.BAL_AMOUNT, 0) + NVL(TSB.SUPPLY_MONEY, 0) -\n" );
		sql.append("       NVL(TSB.DEDUCT_MONEY, 0) + NVL(TSB.OTHER_MONEY, 0) SUM_AMOUNT, --结算合计\n" );
		sql.append("       TSB.AUDIT_REMARK,\n" );
		sql.append("       TO_CHAR(TSB.AUDIT_TIME, 'yyyy-mm-dd') AUDIT_TIME,\n" );
		sql.append("       (SELECT TU.NAME FROM TC_USER TU WHERE TU.USER_ID = TSB.AUDIT_BY) AUDIT_BY\n" );
		sql.append("  FROM TT_SALES_BAL_APPLY TSB, TT_SALES_LOGI TSL\n" );
		sql.append(" WHERE TSB.LOGI_ID = TSL.LOGI_ID\n" );
		sql.append("   AND (TSB.STATUS = 96101007 or TSB.STATUS = 96101006) --已保存或已驳回的\n");

		List<Object> params = new LinkedList<Object>();
		if (applyNo != null && !"".equals(applyNo))
		{//申请单号
			
			sql.append("   AND TSB.APPLY_NO like ?\n");
			params.add("%"+applyNo+"%");
		}
		if(posBusType.equals(String.valueOf(Constant.POSE_BUS_TYPE_WL))){//物流商
			sql.append("   AND TSB.LOGI_ID= ?\n" );
			params.add(logiIdU);
		}else{
			if (logiId != null && !"".equals(logiId))
			{//物流商
				sql.append("   AND TSB.LOGI_ID= ?\n" );
				params.add(logiId);
			}
			
		}
		sql.append("ORDER BY TSB.CREATE_DATE DESC"); 
		PageResult<Map<String, Object>> ps= dao.pageQuery(sql.toString(),params, getFunName(),pageSize,curPage);
		return ps;
	}
	public PageResult<Map<String, Object>> getBalanceApplyQuery(Map<String, Object> map, int curPage, int pageSize) throws Exception
	{
		Object[] objArr=getsbSql(map);
		PageResult<Map<String, Object>> ps= dao.pageQuery(String.valueOf(objArr[0]), (List<Object>)objArr[1], getFunName(),pageSize,curPage);
		return ps;
	}
	/**
	 * 结算申请查询sql
	 * @param map
	 * @return
	 */
	public Object[] getsbSql(Map<String, Object> map){
		/****************************** 页面查询字段start **************************/
		String balNo = (String) map.get("balNo"); // 对账单号	
		String logiId = (String) map.get("logiId"); // 物流商ID
		String isChange = (String) map.get("isChange"); //是否调整
		String poseId = (String) map.get("poseId"); //职位ID
		String posBusType = (String) map.get("posBusType"); //职位类型
		BigDecimal logiIdU = (BigDecimal) map.get("logiIdU");//当前职位所属承运商ID
		
		/****************************** 页面查询字段end ***************************/
		List<Object> params = new LinkedList<Object>();
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT TSW.BILL_ID,\n" );
		sql.append("       TSW.BILL_NO,\n" );
		sql.append("       TSBA.BAL_ID,\n" );
		sql.append("       TSBA.BAL_NO, --挂帐单号\n" );
		sql.append("       TSBA.BAL_MONTH, --挂账月份\n" );
		sql.append("       TSL.LOGI_NAME, --承运商\n" );
		sql.append("       '1' BAL_COUNT, --运输总量\n" );
		sql.append("       NVL(TSW.BILL_AMOUNT, 0) BAL_AMOUNT, --挂账合计\n" );
		sql.append("       NVL(TSW.SUPPLY_MONEY, 0) SUPPLY_MONEY, --补充金额\n" );
		sql.append("       NVL(TSW.DEDUCT_MONEY, 0) DEDUCT_MONEY, --扣款合计\n" );
		sql.append("       NVL(TSW.OTHER_MONEY, 0) OTHER_MONEY, --其他合计\n" );
		sql.append("       NVL(TSW.BILL_AMOUNT, 0) + NVL(TSW.SUPPLY_MONEY, 0) -\n" );
		sql.append("       NVL(TSW.DEDUCT_MONEY, 0) + NVL(TSW.OTHER_MONEY, 0) SUM_AMOUNT, --结算合计\n" );
		sql.append("       (SELECT TC.CODE_DESC\n" );
		sql.append("          FROM TC_CODE TC\n" );
		sql.append("         WHERE TC.CODE_ID = TSW.IS_CHANGE) IS_CHANGE_TXT, --是否调整\n" );
		sql.append("      TSW.BAL_PROV_ID,\n" );
		sql.append("      TSW.BAL_CITY_ID,\n" );
		sql.append("      TSW.BAL_COUNTY_ID,\n" );
//		sql.append("      BAL.PROV_NAME,\n" );
//		sql.append("      BAL.CITY_NAME,\n" );
//		sql.append("      BAL.COUNTY_NAME,\n" );
		sql.append("      BAL.PROV_NAME||BAL.CITY_NAME||BAL.COUNTY_NAME DLV_BAL_ADDR,\n" );
		sql.append("      TSW.APPLY_REMARK\n" );
		sql.append("  FROM TT_SALES_BALANCE TSBA,\n" );
		sql.append("       TT_SALES_LOGI TSL,\n" );
		sql.append("       TT_SALES_WAYBILL TSW,\n" );
		sql.append("       (SELECT TR1.REGION_CODE PROV_CODE,\n" );
		sql.append("               TR2.REGION_CODE CITY_CODE,\n" );
		sql.append("               TR3.REGION_CODE COUNTY_CODE,\n" );
		sql.append("               TR1.REGION_NAME PROV_NAME,\n" );
		sql.append("               TR2.REGION_NAME CITY_NAME,\n" );
		sql.append("               TR3.REGION_NAME COUNTY_NAME\n" );
		sql.append("          FROM TM_REGION TR1, TM_REGION TR2, TM_REGION TR3\n" );
		sql.append("         WHERE TR1.REGION_ID = TR2.PARENT_ID\n" );
		sql.append("           AND TR2.REGION_ID = TR3.PARENT_ID\n" );
		sql.append("           AND TR1.REGION_TYPE = 10541002\n" );
		sql.append("           AND TR2.REGION_TYPE = 10541003\n" );
		sql.append("           AND TR3.REGION_TYPE = 10541004) BAL\n" );
		sql.append(" WHERE TSBA.LOGI_ID = TSL.LOGI_ID\n" );
		sql.append("   AND TSBA.BAL_ID = TSW.BAL_ID\n" );
//		sql.append("   AND TSW.BAL_PROV_ID = BAL.PROV_CODE\n" );
//		sql.append("   AND TSW.BAL_CITY_ID = BAL.CITY_CODE\n" );
//		sql.append("   AND TSW.BAL_COUNTY_ID = BAL.COUNTY_CODE\n");
		sql.append("   AND TSW.DLV_BAL_PROV_ID = BAL.PROV_CODE\n" );
		sql.append("   AND TSW.DLV_BAL_CITY_ID = BAL.CITY_CODE\n" );
		sql.append("   AND TSW.DLV_BAL_COUNTY_ID = BAL.COUNTY_CODE\n");
		sql.append("   AND TSW.APPLY_ID IS NULL\n");//未生成结算申请单的
		//sql.append("   AND TSBA.STATUS = "+Constant.BAL_ORDER_STATUS_01+" --已挂账\n");
		
		if (balNo != null && !"".equals(balNo))
		{//挂帐单号
			
			sql.append("   AND TSBA.BAL_NO like ?\n");
			params.add("%"+balNo+"%");
		}
		if (isChange != null && !"".equals(isChange))
		{//是否调整
			sql.append("   AND TSW.IS_CHANGE =?\n");
			params.add(isChange);
		}
		if(posBusType.equals(String.valueOf(Constant.POSE_BUS_TYPE_WL))){//物流商
			sql.append("   AND TSBA.LOGI_ID= ?\n" );
			params.add(logiIdU);
		}else{
			if (logiId != null && !"".equals(logiId))
			{//物流商
				sql.append("   AND TSBA.LOGI_ID= ?\n" );
				params.add(logiId);
			}
			
		}
			
		sql.append("ORDER BY TSBA.BAL_MONTH"); 
		Object[] obj=new Object[2];
		obj[0]=sql;
		obj[1]=params;
		return obj;
	}
	public Map<String,Object> getWayBillSum(String billIds){
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT MAX(TSW.LOGI_ID) LOGI_ID,\n" );
		sql.append("       COUNT(TSW.BILL_ID) BAL_COUNT,\n" );
		sql.append("       SUM(TSW.BILL_AMOUNT) BAL_AMOUNT,\n" );
		sql.append("       SUM(TSW.DEDUCT_MONEY) DEDUCT_MONEY,\n" );
		sql.append("       SUM(TSW.OTHER_MONEY) OTHER_MONEY,\n" );
		sql.append("       SUM(TSW.SUPPLY_MONEY) SUPPLY_MONEY\n" );
		sql.append("  FROM TT_SALES_WAYBILL TSW\n" );
		sql.append(" WHERE TSW.BILL_ID IN ("+billIds+")");
		return dao.pageQueryMap(sql.toString(), null, getFunName());
	}
	/**
	 * 更新结算申请ID到交接单主表
	 * @param billIds
	 * @param applyId
	 * @param userId
	 * @return
	 */
	public int updateWayBill(String billIds,String applyId,String userId){
		StringBuffer sql= new StringBuffer();
		sql.append("UPDATE TT_SALES_WAYBILL TSW\n" );
		sql.append("   SET TSW.APPLY_ID = '"+applyId+"',\n" );
		sql.append("       TSW.UPDATE_BY = '"+userId+"',\n" );
		sql.append("       TSW.UPDATE_DATE = SYSDATE\n" );
		sql.append(" WHERE TSW.BILL_ID IN ("+billIds+")");
		return dao.update(sql.toString(), null);
	}
	
	public int updateWayBillApply(String applyId,String userId){
		StringBuffer sql= new StringBuffer();
		sql.append("UPDATE TT_SALES_WAYBILL TT\n" );
		sql.append("   SET TT.APPLY_ID      = null,\n" );
		sql.append("       TT.BAL_PROV_ID   = TT.DLV_BAL_PROV_ID,\n" );
		sql.append("       TT.BAL_CITY_ID   = TT.DLV_BAL_CITY_ID,\n" );
		sql.append("       TT.BAL_COUNTY_ID = TT.DLV_BAL_COUNTY_ID,\n" );
		sql.append("       TT.IS_CHANGE     = 10041002,\n" );
		sql.append("       TT.DEDUCT_MONEY  = 0,\n" );
		sql.append("       TT.SUPPLY_MONEY  = 0,\n" );
		sql.append("       TT.OTHER_MONEY   = 0,\n" );
		sql.append("       TT.APPLY_REMARK   = null,\n" );
		sql.append("       TT.UPDATE_BY     = '"+userId+"',\n" );
		sql.append("       TT.UPDATE_DATE   = SYSDATE\n" );
		sql.append(" WHERE TT.APPLY_ID = '"+applyId+"'");

		return dao.update(sql.toString(), null);
	}
	/**
	 * 获取结算申请修改信息
	 * @param applyId
	 * @return
	 */
	public List<Map<String,Object>> getWayBillModify(String applyId){
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT TSW.BILL_ID,\n" );
		sql.append("       TSW.BILL_NO,\n" );
		sql.append("       TSW.BAL_PROV_ID,\n" );
		sql.append("       TSW.BAL_CITY_ID,\n" );
		sql.append("       TSW.BAL_COUNTY_ID,\n" );
		sql.append("       BAL.PROV_NAME,\n" );
		sql.append("       BAL.CITY_NAME,\n" );
		sql.append("       BAL.COUNTY_NAME,\n" );
		sql.append("       TSW.OTHER_MONEY,\n" );
		sql.append("       TSB.APPLY_NO,TSW.APPLY_REMARK\n" );
		sql.append("  FROM TT_SALES_WAYBILL TSW,\n" );
		sql.append("       TT_SALES_BAL_APPLY TSB,\n" );
		sql.append("       (SELECT TR1.REGION_CODE PROV_CODE,\n" );
		sql.append("               TR2.REGION_CODE CITY_CODE,\n" );
		sql.append("               TR3.REGION_CODE COUNTY_CODE,\n" );
		sql.append("               TR1.REGION_NAME PROV_NAME,\n" );
		sql.append("               TR2.REGION_NAME CITY_NAME,\n" );
		sql.append("               TR3.REGION_NAME COUNTY_NAME\n" );
		sql.append("          FROM TM_REGION TR1, TM_REGION TR2, TM_REGION TR3\n" );
		sql.append("         WHERE TR1.REGION_ID = TR2.PARENT_ID\n" );
		sql.append("           AND TR2.REGION_ID = TR3.PARENT_ID\n" );
		sql.append("           AND TR1.REGION_TYPE = 10541002\n" );
		sql.append("           AND TR2.REGION_TYPE = 10541003\n" );
		sql.append("           AND TR3.REGION_TYPE = 10541004) BAL\n" );
		sql.append(" WHERE 1 = 1\n" );
		sql.append("   AND TSW.APPLY_ID = TSB.APPLY_ID\n" );
		sql.append("   AND TSW.DLV_BAL_PROV_ID = BAL.PROV_CODE\n" );
		sql.append("   AND TSW.DLV_BAL_CITY_ID = BAL.CITY_CODE\n" );
		sql.append("   AND TSW.DLV_BAL_COUNTY_ID = BAL.COUNTY_CODE\n" );
		sql.append("   AND TSW.APPLY_ID = '"+applyId+"'");
		return dao.pageQuery(sql.toString(), null, getFunName());
	}
	public Map<String,Object> getBalBillById(String applyId){
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT TSB.APPLY_ID, --申请ID\n" );
		sql.append("       TSL.LOGI_NAME, --承运商\n" );
		sql.append("       TSB.BAL_COUNT, --运输总量\n" );
		sql.append("       TSB.BAL_AMOUNT, --挂账合计\n" );
		sql.append("       TSB.DEDUCT_MONEY, --扣款合计\n" );
		sql.append("       TSB.OTHER_MONEY, --其他合计\n" );
		sql.append("       TSB.SUPPLY_MONEY, --补款合计\n" );
		sql.append("       TSB.APPLY_NO,TSB.INVOICE_NO, --结算单号\n" );
		sql.append("       NVL(TSB.BAL_AMOUNT, 0) + NVL(TSB.SUPPLY_MONEY, 0) -\n" );
		sql.append("       NVL(TSB.DEDUCT_MONEY, 0) + NVL(TSB.OTHER_MONEY, 0) SUM_AMOUNT, --结算合计\n" );
		sql.append("       TO_CHAR(TSB.Create_Date, 'yyyy-mm-dd') CREATE_DATE\n" );
		sql.append("  FROM TT_SALES_BAL_APPLY TSB, TT_SALES_LOGI TSL\n" );
		sql.append(" WHERE TSB.LOGI_ID = TSL.LOGI_ID\n" );
		sql.append(" AND TSB.APPLY_ID='"+applyId+"'");

		return dao.pageQueryMap(sql.toString(), null, getFunName());
	}
	/**
	 * 根据结算申请ID获取运费明细
	 * @param applyId
	 * @return
	 */
	public List<Map<String,Object>> getWayBillMainByAid(String applyId){
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT TSW.BILL_ID,\n" );
		sql.append("       TSW.BILL_NO, --交接单号\n" );
		sql.append("       TSB.BAL_NO, --挂账单号\n" );
		sql.append("       TO_CHAR(TSW.LAST_CAR_DATE, 'yyyy-mm-dd') LAST_CAR_DATE, --最后交车日期\n" );
		sql.append("       (SELECT TC.CODE_DESC\n" );
		sql.append("          FROM TC_CODE TC\n" );
		sql.append("         WHERE TC.CODE_ID = TSW.DLV_SHIP_TYPE) DLV_SHIP_TYPE, --发运方式\n" );
		sql.append("       DECODE(TD.DEALER_SHORTNAME,\n" );
		sql.append("              NULL,\n" );
		sql.append("              SH.WAREHOUSE_NAME,\n" );
		sql.append("              TD.DEALER_SHORTNAME) DEALER_NAME, --经销商或收货仓库\n" );
		sql.append("       TSW.ADDRESS_INFO, --订单收货地\n" );
		sql.append("       BAL.BAL_ADDR, --发运结算地\n" );
		sql.append("       BAL2.BAL_ADDR BAL_ADDR_M, --财务结算地\n" );
		sql.append("       TSW.BILL_AMOUNT, --挂账金额\n" );
		sql.append("       TSW.SUPPLY_MONEY, --补款金额\n" );
		sql.append("       TSW.DEDUCT_MONEY, --扣款金额\n" );
		sql.append("       TSW.OTHER_MONEY, --其他金额\n" );
		sql.append("       (SELECT TC.CODE_DESC FROM TC_CODE TC WHERE TC.CODE_ID = TSW.IS_CHANGE) IS_CHANGE --是否调整\n" );
		sql.append("  FROM TT_SALES_WAYBILL TSW,\n" );
		sql.append("       TM_WAREHOUSE SH,\n" );
		sql.append("       TM_DEALER TD,\n" );
		sql.append("       TT_SALES_BALANCE TSB,\n" );
		sql.append("       (SELECT TR1.REGION_CODE PROV_CODE,\n" );
		sql.append("               TR2.REGION_CODE CITY_CODE,\n" );
		sql.append("               TR3.REGION_CODE COUNTY_CODE,\n" );
		sql.append("               TR1.REGION_NAME || TR2.REGION_NAME || TR3.REGION_NAME BAL_ADDR\n" );
		sql.append("          FROM TM_REGION TR1, TM_REGION TR2, TM_REGION TR3\n" );
		sql.append("         WHERE TR1.REGION_ID = TR2.PARENT_ID\n" );
		sql.append("           AND TR2.REGION_ID = TR3.PARENT_ID\n" );
		sql.append("           AND TR1.REGION_TYPE = 10541002\n" );
		sql.append("           AND TR2.REGION_TYPE = 10541003\n" );
		sql.append("           AND TR3.REGION_TYPE = 10541004) BAL,\n" );
		sql.append("       (SELECT TR1.REGION_CODE PROV_CODE,\n" );
		sql.append("               TR2.REGION_CODE CITY_CODE,\n" );
		sql.append("               TR3.REGION_CODE COUNTY_CODE,\n" );
		sql.append("               TR1.REGION_NAME || TR2.REGION_NAME || TR3.REGION_NAME BAL_ADDR\n" );
		sql.append("          FROM TM_REGION TR1, TM_REGION TR2, TM_REGION TR3\n" );
		sql.append("         WHERE TR1.REGION_ID = TR2.PARENT_ID\n" );
		sql.append("           AND TR2.REGION_ID = TR3.PARENT_ID\n" );
		sql.append("           AND TR1.REGION_TYPE = 10541002\n" );
		sql.append("           AND TR2.REGION_TYPE = 10541003\n" );
		sql.append("           AND TR3.REGION_TYPE = 10541004) BAL2\n" );
		sql.append(" WHERE 1 = 1\n" );
		sql.append("   AND TSW.BAL_ID = TSB.BAL_ID\n" );
		sql.append("   AND TSW.OR_DEALER_ID = TD.DEALER_ID(+)\n" );
		sql.append("   AND TSW.OR_DEALER_ID = SH.WAREHOUSE_ID(+)\n" );
		sql.append("   AND TSW.DLV_BAL_PROV_ID = BAL.PROV_CODE\n" );
		sql.append("   AND TSW.DLV_BAL_CITY_ID = BAL.CITY_CODE\n" );
		sql.append("   AND TSW.DLV_BAL_COUNTY_ID = BAL.COUNTY_CODE\n" );
		sql.append("   AND TSW.BAL_PROV_ID = BAL2.PROV_CODE\n" );
		sql.append("   AND TSW.BAL_CITY_ID = BAL2.CITY_CODE\n" );
		sql.append("   AND TSW.BAL_COUNTY_ID = BAL2.COUNTY_CODE\n" );
		sql.append("   and TSW.APPLY_ID = '"+applyId+"'");

		return dao.pageQuery(sql.toString(), null, getFunName());
	}
	/**
	 * 根据结算单ID获取附件列表
	 * @param applyId
	 * @return
	 */
	public List<Map<String,Object>> getFileListByAId(String applyId){
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT T.FILEURL FILE_PATH, T.FILENAME FILE_NAME, T.*\n" );
		sql.append("  FROM fs_fileupload T\n" );
		sql.append(" WHERE T.STATUS = "+Constant.STATUS_ENABLE+"\n" );
		sql.append("   AND T.YWZJ = '"+applyId+"'");
		return dao.pageQuery(sql.toString(), null, getFunName());
	}
	/**
	 * 根据结算申请ID获取车辆明细
	 * @param applyId
	 * @return
	 */
	public List<Map<String,Object>> getWayBillDtlByAid(String applyId){
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT TSBA.BAL_ID,\n" );
		sql.append("       TSBA.BAL_NO, --对账单号\n" );
		sql.append("       TSL.LOGI_NAME, --承运商\n" );
		sql.append("       (SELECT TC.CODE_DESC\n" );
		sql.append("          FROM TC_CODE TC\n" );
		sql.append("         WHERE TC.CODE_ID = TSWB.DLV_IS_SD) DLV_IS_SD, --是否散单\n" );
		sql.append("       TSW.BILL_NO, --交接单号\n" );
		sql.append("       TO_CHAR(TSW.LAST_CAR_DATE, 'yyyy-mm-dd') LAST_CAR_DATE, --最后交车日期\n" );
		sql.append("       FY.WAREHOUSE_NAME, --发运仓库\n" );
		sql.append("       DECODE(TD.DEALER_SHORTNAME,\n" );
		sql.append("              NULL,\n" );
		sql.append("              SH.WAREHOUSE_NAME,\n" );
		sql.append("              TD.DEALER_SHORTNAME) DEALER_NAME, --经销商或收货仓库\n" );
		sql.append("       TSW.ADDRESS_INFO, --订单收货地\n" );
		sql.append("       BAL.BAL_ADDR, --发运结算地\n" );
		sql.append("       (SELECT TC.CODE_DESC\n" );
		sql.append("          FROM TC_CODE TC\n" );
		sql.append("         WHERE TC.CODE_ID = TSWB.DLV_IS_ZZ) DLV_IS_ZZ,TSWB.DLV_IS_ZZ IS_ZZ,--是否中转\n" );
		sql.append("       ZZ.ZZ_ADDR, --中转地址\n" );
		sql.append("       (SELECT TC.CODE_DESC\n" );
		sql.append("          FROM TC_CODE TC\n" );
		sql.append("         WHERE TC.CODE_ID = TSW.DLV_SHIP_TYPE) DLV_SHIP_TYPE, --发运方式\n" );
		sql.append("       VMGM.SERIES_NAME, --车系\n" );
		sql.append("       VMGM.MODEL_NAME, --车型\n" );
		sql.append("       VMGM.PACKAGE_NAME, --配置\n" );
		sql.append("       VMGM.COLOR_NAME, --颜色\n" );
		sql.append("       TSWB.VIN, --车架号\n" );
		sql.append("       TSWB.MILEAGE,TSWB.MILEAGE_ZZ, --运送里程\n" );
		sql.append("       TSWB.PRICE,TSWB.PRICE_ZZ, --单价\n" );
		sql.append("		TSWB.ONE_BILL_AMOUNT, --挂账运费\n" );
		sql.append("           TSWB.NEW_PRICE,\n" );
		sql.append("           TSWB.NEW_MILEAGE,\n" );
		sql.append("           TSWB.NEW_AMOUNT\n");
		sql.append("  FROM TT_SALES_BALANCE TSBA,\n" );
		sql.append("       TT_SALES_WAYBILL TSW,\n" );
		sql.append("       TT_SALES_WAY_BILL_DTL TSWB,\n" );
		sql.append("       TT_SALES_LOGI TSL,\n" );
		sql.append("       TM_WAREHOUSE FY,\n" );
		sql.append("       TM_WAREHOUSE SH,\n" );
		sql.append("       TM_DEALER TD,\n" );
		sql.append("       (SELECT TR1.REGION_CODE PROV_CODE,\n" );
		sql.append("               TR2.REGION_CODE CITY_CODE,\n" );
		sql.append("               TR3.REGION_CODE COUNTY_CODE,\n" );
		sql.append("               TR1.REGION_NAME || TR2.REGION_NAME || TR3.REGION_NAME BAL_ADDR\n" );
		sql.append("          FROM TM_REGION TR1, TM_REGION TR2, TM_REGION TR3\n" );
		sql.append("         WHERE TR1.REGION_ID = TR2.PARENT_ID\n" );
		sql.append("           AND TR2.REGION_ID = TR3.PARENT_ID\n" );
		sql.append("           AND TR1.REGION_TYPE = 10541002\n" );
		sql.append("           AND TR2.REGION_TYPE = 10541003\n" );
		sql.append("           AND TR3.REGION_TYPE = 10541004) BAL,\n" );
		sql.append("       (SELECT TR1.REGION_CODE PROV_CODE,\n" );
		sql.append("               TR2.REGION_CODE CITY_CODE,\n" );
		sql.append("               TR3.REGION_CODE COUNTY_CODE,\n" );
		sql.append("               TR1.REGION_NAME || TR2.REGION_NAME || TR3.REGION_NAME ZZ_ADDR\n" );
		sql.append("          FROM TM_REGION TR1, TM_REGION TR2, TM_REGION TR3\n" );
		sql.append("         WHERE TR1.REGION_ID = TR2.PARENT_ID\n" );
		sql.append("           AND TR2.REGION_ID = TR3.PARENT_ID\n" );
		sql.append("           AND TR1.REGION_TYPE = 10541002\n" );
		sql.append("           AND TR2.REGION_TYPE = 10541003\n" );
		sql.append("           AND TR3.REGION_TYPE = 10541004) ZZ,\n" );
		sql.append("       VW_MATERIAL_GROUP_MAT VMGM\n" );
		sql.append(" WHERE TSBA.BAL_ID = TSW.BAL_ID\n" );
		sql.append("   AND TSW.BILL_ID = TSWB.BILL_ID\n" );
		sql.append("   AND TSBA.LOGI_ID = TSL.LOGI_ID\n" );
		sql.append("   AND TSWB.DLV_WH_ID = FY.WAREHOUSE_ID\n" );
		sql.append("   AND TSW.OR_DEALER_ID = TD.DEALER_ID(+)\n" );
		sql.append("   AND TSW.OR_DEALER_ID = SH.WAREHOUSE_ID(+)\n" );
		sql.append("   AND TSW.DLV_BAL_PROV_ID = BAL.PROV_CODE\n" );
		sql.append("   AND TSW.DLV_BAL_CITY_ID = BAL.CITY_CODE\n" );
		sql.append("   AND TSW.DLV_BAL_COUNTY_ID = BAL.COUNTY_CODE\n" );
		sql.append("   AND TSWB.DLV_ZZ_PROV_ID = ZZ.PROV_CODE(+)\n" );
		sql.append("   AND TSWB.DLV_ZZ_CITY_ID = ZZ.CITY_CODE(+)\n" );
		sql.append("   AND TSWB.DLV_ZZ_COUNTY_ID = ZZ.COUNTY_CODE(+)\n" );
		sql.append("   AND VMGM.MATERIAL_ID = TSWB.MAT_ID\n" );
		sql.append("   AND TSW.APPLY_ID = '"+applyId+"'");

		return dao.pageQuery(sql.toString(), null, getFunName());
	}
	/**
	 * 根据物料ID获取运费系数
	 * @param materialId
	 * @return
	 */
	public Map<String,Object> getRatioNumByMat(String materialId){
		StringBuffer sql= new StringBuffer();
		sql.append("select R.RATIO_NUM\n" );
		sql.append("  from tm_fare_ratio r, vw_material_group_mat mt\n" );
		sql.append(" where r.series_id = mt.SERIES_ID\n" );
		sql.append("   and mt.MATERIAL_ID = '"+materialId+"'");

		return dao.pageQueryMap(sql.toString(), null, getFunName());
	}
	/**
	 * 根据发运仓库、财务结算区县、发运方式获取城市里程信息
	 * @param dlvWhId
	 * @param balCountyId
	 * @param shipType
	 * @return
	 */
	public Map<String,Object> getDisInfo(String dlvWhId,String balCountyId,String shipType){
		StringBuffer sql= new StringBuffer();
		sql.append("select DS.HAND_PRICE, DS.DISTANCE, DS.SINGLE_PLACE\n" );
		sql.append("  from tt_sales_city_dis ds\n" );
		sql.append(" where ds.yieldly = '"+dlvWhId+"'\n" );
		sql.append("   and ds.city_id = '"+balCountyId+"'\n" );
		sql.append("   and ds.trans_way = '"+shipType+"'");
		
		return dao.pageQueryMap(sql.toString(), null, getFunName());
	}
	public List<Map<String, Object>> getBalanceListQueryExp(Map<String, Object> map){
		Object[] obj=getSQL(map);
		List<Map<String, Object>> list= dao.pageQuery(obj[0].toString(), (List<Object>)obj[1], getFunName());
		return list;
	}
	public Object[] getSQL(Map<String, Object> map){
		String applyNo = (String) map.get("applyNo"); //申请单号
		String balNo = (String) map.get("balNo"); // 对账单号	
		String invoiceNo = (String) map.get("invoiceNo"); //发票号
		String logiId = (String) map.get("logiId"); // 物流商ID
		String isChange = (String) map.get("isChange"); //是否调整
		String status = (String) map.get("status"); //状态
		String posBusType = (String) map.get("posBusType"); //职位类型
		BigDecimal logiIdU = (BigDecimal) map.get("logiIdU");//当前职位所属承运商ID
		
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT TSB.APPLY_ID,\n" );
		sql.append("       TSL.LOGI_NAME,\n" );
		sql.append("       TSB.BAL_COUNT,\n" );
		sql.append("       TSB.BAL_AMOUNT,\n" );
		sql.append("       TSB.DEDUCT_MONEY,\n" );
		sql.append("       TSB.OTHER_MONEY,\n" );
		sql.append("       TSB.SUPPLY_MONEY,\n" );
		sql.append("       DECODE(TSS.IS_CHANGE,\n" );
		sql.append("              10041001,\n" );
		sql.append("              '<font color=red>' || TSB.APPLY_NO || '</font>',\n" );
		sql.append("              TSB.APPLY_NO) APPLY_NO2,\n" );
		sql.append("       TSB.APPLY_NO,\n" );
		sql.append("       TSB.INVOICE_NO,\n" );
		sql.append("       (SELECT TC.CODE_DESC FROM TC_CODE TC WHERE TC.CODE_ID=TSB.STATUS) STATUS_TXT,\n" );
		sql.append("       NVL(TSB.BAL_AMOUNT, 0) + NVL(TSB.SUPPLY_MONEY, 0) -\n" );
		sql.append("       NVL(TSB.DEDUCT_MONEY, 0) + NVL(TSB.OTHER_MONEY, 0) SUM_AMOUNT --结算合计\n" );
		sql.append("  FROM TT_SALES_BAL_APPLY TSB,\n" );
		sql.append("       TT_SALES_LOGI TSL,\n" );
		sql.append("       (SELECT MIN(TSW.IS_CHANGE) IS_CHANGE, TSW.APPLY_ID\n" );
		sql.append("          FROM TT_SALES_WAYBILL TSW\n" );
		sql.append("         WHERE TSW.APPLY_ID IS NOT NULL\n" );
		sql.append("         group by TSW.APPLY_ID) TSS\n" );
		sql.append(" WHERE TSB.LOGI_ID = TSL.LOGI_ID\n" );
		sql.append("   AND TSS.APPLY_ID = TSB.APPLY_ID\n");
		List<Object> params = new LinkedList<Object>();
		if (applyNo != null && !"".equals(applyNo))
		{//申请单号
			
			sql.append("   AND TSB.APPLY_NO like ?\n");
			params.add("%"+applyNo+"%");
		}
		if (balNo != null && !"".equals(balNo))
		{//挂帐单号
			
			sql.append("AND TSB.APPLY_ID IN (SELECT TSWS.APPLY_ID\n" );
			sql.append("                          FROM TT_SALES_WAYBILL TSWS, TT_SALES_BALANCE TSBS\n" );
			sql.append("                         WHERE TSWS.BAL_ID = TSBS.BAL_ID\n" );
			sql.append("                           AND TSBS.BAL_NO LIKE ?)");
			params.add("%"+balNo+"%");
		}
		if (invoiceNo != null && !"".equals(invoiceNo))
		{//发票号
			
			sql.append("   AND TSB.INVOICE_NO like ?\n");
			params.add("%"+invoiceNo+"%");
		}
		if (isChange != null && !"".equals(isChange))
		{//是否调整
			sql.append("   AND TSS.IS_CHANGE =?\n");
			params.add(isChange);
		}
		if (status != null && !"".equals(status))
		{//状态
			sql.append("   AND TSB.STATUS =?\n");
			params.add(status);
		}
		if(posBusType.equals(String.valueOf(Constant.POSE_BUS_TYPE_WL))){//物流商
			sql.append("   AND TSB.LOGI_ID= ?\n" );
			params.add(logiIdU);
		}else{
			if (logiId != null && !"".equals(logiId))
			{//物流商
				sql.append("   AND TSB.LOGI_ID= ?\n" );
				params.add(logiId);
			}
			
		}
		sql.append("ORDER BY TSB.CREATE_DATE DESC"); 
		Object[] arr=new Object[2];
		arr[0]=sql;
		arr[1]=params;
		return arr;
	}
	/**
	 * 结算列表查询
	 * @param map
	 * @param curPage
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	public PageResult<Map<String, Object>> getBalanceListQuery(Map<String, Object> map, int curPage, int pageSize) throws Exception
	{
		Object[] obj=getSQL(map);
		PageResult<Map<String, Object>> ps= dao.pageQuery(obj[0].toString(),(List<Object>)obj[1], getFunName(),pageSize,curPage);
		return ps;
	}
}

