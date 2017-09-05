package com.infodms.dms.dao.sales.accountsmanage;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.sales.planmanage.PlanUtil.PlanUtil;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TmCompanyPO;
import com.infodms.dms.po.TmDealerPO;
import com.infodms.dms.po.TmPublicMaterialPO;
import com.infodms.dms.po.TmVhclMaterialGroupPO;
import com.infodms.dms.po.TtPurchaseInvoicesProductPO;
import com.infodms.dms.po.TtVsAccountTypePO;
import com.infodms.dms.po.TtVsDlvryReqPO;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.po3.POFactory;
import com.infoservice.po3.POFactoryBuilder;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

public class InvoiceManageDAO extends BaseDao{
	public static final Logger logger = Logger.getLogger(InvoiceManageDAO.class);
	private static final InvoiceManageDAO dao = new InvoiceManageDAO();
	private static  POFactory factory = POFactoryBuilder.getInstance();
	
	public static InvoiceManageDAO getInstance() {
		return dao;
	}
	
	//发票管理初始化页面查询(经销商端) 2013-2-27
	public  PageResult<Map<String, Object>> invoiceManageInitQuery(String con,int curpage,int pagesize){
		StringBuffer sql= new StringBuffer();
		sql.append("select \n");
		sql.append(" tpi.INVOICE_ID,\n");
		sql.append(" tpi.PURCHASE_INVOICE_NO,\n");
		sql.append(" td.DEALER_NAME,\n");
		sql.append(" TO_CHAR(tpi.TICKET_DATE,'yyyy-MM-dd') TICKET_DATE,\n");
//		sql.append(" tvdr.DLVRY_REQ_NO,\n");
		sql.append(" td.DEALER_CODE,\n");
		sql.append(" tpi.DLvRy_CODE,\n");
		sql.append(" td.DEALER_SHORTNAME\n");
		sql.append(" from TT_Purchase_Invoice tpi,TM_DEALER td  --,TT_VS_DLVRY_REQ tvdr\n");
		sql.append(" where tpi.RECEIVE_DEPT_ID =td.DEALER_ID\n");
//		sql.append(" and tvdr.REQ_ID = tpi.DLVRY_REQ_ID\n");
		if (con!=null&&!("").equals(con)){
			sql.append(con);
		}
		sql.append(" order by TICKET_DATE,tpi.CREATE_DATE desc ");
		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(),pagesize, curpage);
		return ps;
	}
	
	//发票管理初始化页面查询(车厂端) 2013-3-1
	public  PageResult<Map<String, Object>> invoiceManageDeptInitQuery(String con,String status,Long orgId,int curpage,int pagesize){
		StringBuffer sql= new StringBuffer();
		sql.append("select \n");
		sql.append(" tpi.INVOICE_ID,\n");
		sql.append(" tpi.PURCHASE_INVOICE_NO,\n");
		sql.append(" td.DEALER_NAME,\n");
		sql.append(" TO_CHAR(tpi.TICKET_DATE,'yyyy-MM-dd') TICKET_DATE,\n");
		//sql.append(" tvdr.DLVRY_REQ_NO,\n");
		sql.append(" td.DEALER_CODE,\n");
		sql.append(" tpi.DLvRy_CODE,\n");
		sql.append(" td.DEALER_SHORTNAME\n");
		sql.append(" from TT_Purchase_Invoice tpi,VW_ORG_DEALER_ALL_NEW vod,TM_DEALER td --,TT_VS_DLVRY_REQ tvdr\n");
		sql.append(" where vod.ROOT_DEALER_NAME = td.DEALER_NAME\n");
		sql.append(" and vod.DEALER_ID = td.DEALER_ID\n");
		sql.append(" and tpi.RECEIVE_DEPT_ID =td.DEALER_ID\n");
		//sql.append(" and tvdr.REQ_ID = tpi.DLVRY_REQ_ID\n");

		if(status.equals(Constant.DUTY_TYPE_LARGEREGION.toString())){ //大区
			sql.append("  AND vod.ROOT_ORG_ID="+orgId);
		}
		if(status.equals(Constant.DUTY_TYPE_SMALLREGION.toString())){ //小区
			sql.append("  AND vod.PQ_ORG_ID="+orgId);
		}
		if (con!=null&&!("").equals(con)){
			sql.append(con);
		}
		sql.append(" order by TICKET_DATE,tpi.CREATE_DATE desc ");
		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(),pagesize, curpage);
		return ps;
	}
	
	//根据采购发票信息id获取多条产品信息对象 一对多关系
	public List<TtPurchaseInvoicesProductPO> getProductByInvoiceId(String invoiceId){
		Long invoiceId1 = Long.parseLong(invoiceId);
		TtPurchaseInvoicesProductPO tpip = new TtPurchaseInvoicesProductPO(); //采购产品信息
		tpip.setInvoiceId(invoiceId1);
		List<TtPurchaseInvoicesProductPO> list = dao.select(tpip);
		return list;
	}

	//根据开票单位ID获取开票单位名称
	public String getBillDeptNameById(Long BillDeptId){
//		TmDealerPO td = new TmDealerPO();
//		td.setDealerId(BillDeptId);
//		List<Object> list = new ArrayList<Object>();
//		list = dao.select(td);
//		TmDealerPO td1 = new TmDealerPO();
//		td1 = (TmDealerPO)list.get(0);
//		String billDeptName = td1.getDealerName();
//		return billDeptName;
		
		TmCompanyPO td = new TmCompanyPO();
		td.setCompanyId(BillDeptId);
		List<Object> list = new ArrayList<Object>();
		list = dao.select(td);
		String billDeptName = "";
		if(list.size() > 0){
			TmCompanyPO td1 = new TmCompanyPO();
			td1 = (TmCompanyPO)list.get(0);
			billDeptName = td1.getCompanyName();
		}
		return billDeptName;		
	}
	
	//根据收票单位ID获取收票单位名称
	public String getReceiveDeptNameById(Long receiveDeptId){
		TmDealerPO td = new TmDealerPO();
		td.setDealerId(receiveDeptId);
		List<Object> list = new ArrayList<Object>();
		list = dao.select(td);
		TmDealerPO td1 = new TmDealerPO();
		td1 = (TmDealerPO)list.get(0);
		String receiveDeptName = td1.getDealerName();
		return receiveDeptName;
	}
	
	//根据销售订单ID获取销售订单编号
	public String getDlvryReqNoById(Long receiveDeptId){
		TtVsDlvryReqPO tvdr = new TtVsDlvryReqPO();
		tvdr.setReqId(receiveDeptId);
		List<Object> list = new ArrayList<Object>();
		list = dao.select(tvdr);
		TtVsDlvryReqPO tvdr1 = new TtVsDlvryReqPO();
		tvdr1 = (TtVsDlvryReqPO)list.get(0);
		String dlvryReqNo = tvdr1.getDlvryReqNo();;
		return dlvryReqNo;
	}
	
	//根据产品ID获取产品编码 PRODUCT_CODE
	public String getProductCodeById(Long productId){
		String code ="";
		String code1 ="";
		TmVhclMaterialGroupPO tvmg = new TmVhclMaterialGroupPO();
		tvmg.setGroupId(productId);
		List<Object> list = new ArrayList<Object>();
		list = dao.select(tvmg);
	    if(list.size()>0){
	    	TmVhclMaterialGroupPO tvmg1 = new TmVhclMaterialGroupPO();
			tvmg1 = (TmVhclMaterialGroupPO)list.get(0);
			code = tvmg1.getGroupCode();
		}else{
			TmPublicMaterialPO tpm = new TmPublicMaterialPO();
			tpm.setPublicMaterialId(productId);
			List<Object> list1 = new ArrayList<Object>();
			list1 = dao.select(tpm);
			TmPublicMaterialPO tpm1 = new TmPublicMaterialPO();
			tpm1 = (TmPublicMaterialPO)list1.get(0);
			code1 = tpm1.getPublicMaterialCode();
		}
		if(code!=null&&code!=""){
			return code;
		}else{
			return CommonUtils.checkNull(code1);
		}
	}
	
	//根据产品ID获取产品名称
	public String getProductNameById(Long productId){
		String name = "";
		String name1 = "";
		TmVhclMaterialGroupPO tvmg = new TmVhclMaterialGroupPO();
		tvmg.setGroupId(productId);
		List<Object> list = new ArrayList<Object>();
		list = dao.select(tvmg);
		if(list.size()>0){
			TmVhclMaterialGroupPO tvmg1 = new TmVhclMaterialGroupPO();
			tvmg1 = (TmVhclMaterialGroupPO)list.get(0);
			name = tvmg1.getGroupName();
		}else{
			TmPublicMaterialPO tpm = new TmPublicMaterialPO();
			tpm.setPublicMaterialId(productId);
			List<Object> list1 = new ArrayList<Object>();
			list1 = dao.select(tpm);
			TmPublicMaterialPO tpm1 = new TmPublicMaterialPO();
			tpm1 = (TmPublicMaterialPO)list1.get(0);
			name1 = tpm1.getPublicMaterialName();
		}
		if(name!=null&&name!=""){
			return name;
		}else{
			return CommonUtils.checkNull(name1);
		}
	}
	
	//根据经销商ID获得经销商Code
	public String getDlrCodeById(Long dealerId){
		TmDealerPO td = new TmDealerPO();
		td.setDealerId(dealerId);
		List<Object> list = new ArrayList<Object>();
		list = dao.select(td);
		TmDealerPO td1 = new TmDealerPO();
		td1 = (TmDealerPO)list.get(0);
		String dealerCode = td1.getDealerCode();
		return dealerCode;
	}
	
	//根据账户类型ID获得账户类型名称
	public String getAccountNameById(Long accountTypeId){
		TtVsAccountTypePO tvat = new TtVsAccountTypePO();
		tvat.setTypeId(accountTypeId);
		List<Object> list = new ArrayList<Object>();
		list = dao.select(tvat);
		TtVsAccountTypePO tvat1 = new TtVsAccountTypePO();
		tvat1 = (TtVsAccountTypePO) list.get(0);
		String accountTypeName = tvat1.getTypeName();
		return accountTypeName;
	}
	
	
	//获得车厂名称信息
	public TmCompanyPO getBillDeptNameInfo(){			
			TmCompanyPO td = new TmCompanyPO();
			td.setCompanyType(10531001);
			List<Object> list = new ArrayList<Object>();
			list = dao.select(td);
			if(list.size() > 0){
				TmCompanyPO td1 = new TmCompanyPO();
				td1 = (TmCompanyPO)list.get(0);
				return td1;
			}
			return null;		
		}
		
	
	//得到公司姓名与ID
	public List<Map<String, Object>> getDealerInfoByID(String id){	
		StringBuffer sql= new StringBuffer();
		sql.append("select  t.dealer_id,t.company_id,t.dealer_type,t.dealer_code,t.dealer_name from TM_DEALER t where t.dealer_id =  (select req.dealer_id from tt_vs_dlvry_req  req where req.dlvry_req_no ="+ id+")");
		return this.pageQuery(sql.toString(), null, getFunName());			
	}
	
	//得到帐户类型ID
	public List<Map<String, Object>> getAccountTypeInfo(String invoiceId){
		StringBuffer sql= new StringBuffer();
		sql.append("select a.type_id,a.type_code,a.type_name from Tt_Vs_Account_Type a where a.type_id = (select req.fund_type from tt_vs_dlvry_req req where req.dlvry_req_no ="+ invoiceId+")");
		return this.pageQuery(sql.toString(), null, getFunName());	
	}
	
	//得到帐户类型ID
	public List<Map<String, Object>> getRebateMoneyInfo(String invoiceId){
			StringBuffer sql= new StringBuffer();
			sql.append("select t.rebate_amount from tt_vs_dlvry_req t where t.dlvry_req_no ="+ invoiceId);
			return this.pageQuery(sql.toString(), null, getFunName());	
	}
	
	//得到帐户类型ID
	public List<Map<String, Object>> getMaterialAmountInfo(String invoiceId){
		StringBuffer sql= new StringBuffer();
		sql.append(
				"select   dtl.material_id, dtl.reserve_amount,dtl.bill_amount from tt_vs_dlvry_req_dtl dtl where dtl.req_id = (\n" +
						"select t.req_id from tt_vs_dlvry_req t where t.dlvry_req_no ="+invoiceId+"\n" + 
						")");
		return this.pageQuery(sql.toString(), null, getFunName());	
	}
		
	
	//发票管理查询
	public PageResult<Map<String, Object>> getOrderResourceReserveQuery(Map<String, Object> map, int curPage, int pageSize) throws Exception {

		String groupCode = (String) map.get("groupCode");
		String dealerCode = (String) map.get("dealerCode");
		String orderType = (String) map.get("orderType");
		String orderNo = (String) map.get("orderNo");
		String companyId = (String) map.get("companyId");
		String reqStatus = (String) map.get("reqStatus");
		String orgCode = (String) map.get("orgCode");
		String beginTime = (String) map.get("beginTime");
		String endTime = (String) map.get("endTime");
		String operateType=(String)map.get("operateType");
		String userId=(String)map.get("userId");
		List<Object> params = new LinkedList<Object>();

		StringBuffer sql = new StringBuffer();

		sql.append("SELECT TVO.ORDER_ID,\n");
		sql.append("       TVDR.REQ_ID,\n");
		sql.append("       TMD.DEALER_CODE,\n");
		sql.append("       TMD.DEALER_SHORTNAME DEALER_NAME,\n");
		sql.append("       DECODE(TVDR.CALL_LEAVEL, " + Constant.DEALER_LEVEL_02 + ", TMD2.DEALER_CODE, TMD1.DEALER_CODE) ORDER_DEALER_CODE,\n");

		sql.append("       DECODE(TVDR.CALL_LEAVEL, " + Constant.DEALER_LEVEL_02 + ", TMD2.DEALER_SHORTNAME, TMD1.DEALER_SHORTNAME) ORDER_DEALER_NAME,\n");

		sql.append("       TVO.ORDER_NO,\n");
		sql.append("       TO_CHAR(TVO.RAISE_DATE, 'YYYY-MM-DD') RAISE_DATE,\n");
		sql.append("       TVO.ORDER_YEAR,\n");
		sql.append("       TVO.ORDER_WEEK,\n");
		sql.append("       TVO.ORDER_TYPE,\n");
		sql.append("       TC2.CODE_DESC REQ_STATUS,\n");
		sql.append("       NVL(TVO.VER, 0) VER,\n");
		sql.append("       TVDR.REQ_TOTAL_AMOUNT,\n");
		sql.append("       TVDR.REQ_TOTAL_PRICE,\n");
		sql.append("tvdr.F_AUDIT_TIME,\n");

		sql.append("       TVAT.TYPE_NAME FUND_TYPE,TVDR.DLVRY_REQ_NO,\n");
		sql.append("       NVL(SUM(TVDRD.RESERVE_AMOUNT), 0) RESERVE_AMOUNT,\n");
		sql.append("       NVL(SUM(TVDRD.BILL_AMOUNT), 0) BILL_AMOUNT\n");
		sql.append("  FROM TT_VS_ORDER              TVO,\n");
		sql.append("       TT_VS_ORDER_DETAIL       TVOD,\n");
		sql.append("       TT_VS_DLVRY_REQ          TVDR,\n");
		sql.append("       TT_VS_DLVRY_REQ_DTL      TVDRD,\n");
		sql.append("       TM_DEALER                TMD,\n");
		sql.append("       TM_DEALER                TMD1,\n");
		sql.append("       TM_DEALER                TMD2,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP_R TVMGR,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP   TVMG,\n");
		sql.append("       TM_DEALER_ORG_RELATION   TDOR,\n");
		sql.append("       TT_VS_ACCOUNT_TYPE       TVAT,TC_CODE TC2\n");
		sql.append(" WHERE TVO.ORDER_ID = TVOD.ORDER_ID\n");
		sql.append("   AND TVO.ORDER_ID = TVDR.ORDER_ID  AND TC2.CODE_ID=TVDR.REQ_STATUS\n");
		sql.append("   AND TVDR.REQ_ID = TVDRD.REQ_ID\n");
		sql.append("   AND TVOD.DETAIL_ID = TVDRD.ORDER_DETAIL_ID\n");
		sql.append("   AND TVO.BILLING_ORG_ID = TMD.DEALER_ID\n");
		sql.append("   AND TVDR.ORDER_DEALER_ID = TMD2.DEALER_ID(+)\n");
		sql.append("   AND TVO.ORDER_ORG_ID = TMD1.DEALER_ID\n");
		sql.append("   AND TVOD.MATERIAL_ID = TVMGR.MATERIAL_ID\n");
		sql.append("   AND TVMGR.GROUP_ID = TVMG.GROUP_ID\n");
		sql.append("   AND TMD.DEALER_ID = TDOR.DEALER_ID\n");
		sql.append("   AND TVDR.FUND_TYPE = TVAT.TYPE_ID\n");
		sql.append("   AND TVDR.REQ_STATUS IN (" + Constant.ORDER_REQ_STATUS_CYQR + ")\n");
		sql.append("   AND TVO.COMPANY_ID = " + companyId + "\n");
		if (orderType != null && !"".equals(orderType)) {
			sql.append("   AND TVO.ORDER_TYPE = " + orderType + "\n");
		}
		if (orderNo != null && !"".equals(orderNo)) {
			sql.append("   AND TVO.ORDER_NO LIKE '%" + orderNo + "%'\n");
		}
		if (reqStatus != null && !"".equals(reqStatus)) {
			sql.append("   AND TVDR.REQ_STATUS = " + reqStatus + "\n");
		}
		if (groupCode != null && !"".equals(groupCode)) {
			sql.append(Utility.getConSqlByParamForEqual(groupCode, params, "TVMG", "GROUP_CODE"));
		}
		if (dealerCode != null && !"".equals(dealerCode)) {
			sql.append(Utility.getConSqlByParamForEqual(dealerCode, params, "TMD", "DEALER_CODE"));
		}
		if (orgCode != null && !"".equals(orgCode)) {
			sql.append("   AND TVO.BILLING_ORG_ID IN\n");
			sql.append("       (SELECT M.DEALER_ID\n");
			sql.append("          FROM TM_DEALER M\n");
			sql.append("        CONNECT BY PRIOR M.DEALER_ID = M.PARENT_DEALER_D\n");
			sql.append("         START WITH M.STATUS = " + Constant.STATUS_ENABLE + "\n");
			sql.append("                AND M.DEALER_ID IN\n");
			sql.append("                    (SELECT REL.DEALER_ID\n");
			sql.append("                       FROM TM_DEALER_ORG_RELATION REL, TM_ORG ORG\n");
			sql.append("                      WHERE REL.ORG_ID = ORG.ORG_ID\n");
			sql.append("                        AND ORG.ORG_CODE IN\n");
			sql.append("                            (" + PlanUtil.createSqlStr(orgCode) + ")))");

		}

		if (beginTime != null && !beginTime.equals("")) {
			sql.append("   AND TVO.RAISE_DATE >= TO_DATE('" + beginTime + " 00:00:00','YYYY-MM-DD HH24:mi:ss') \n");
		}
		if (endTime != null && !endTime.equals("")) {
			sql.append("   AND TVO.RAISE_DATE < =TO_DATE('" + endTime + " 23:59:59','YYYY-MM-DD HH24:mi:ss')\n");

		}
		sql.append(" AND  EXISTS ( SELECT 1 FROM TT_VS_DLVRY_REQ_DTL D WHERE TVDR.REQ_ID=D.REQ_ID AND (NVL(D.RESERVE_AMOUNT,0)-NVL(D.BILL_AMOUNT,0))>0   )\n");
		sql.append(" GROUP BY TVO.ORDER_ID,\n");
		sql.append("          TVDR.REQ_ID,\n");
		sql.append("          TMD.DEALER_CODE,\n");
		sql.append("          TMD.DEALER_SHORTNAME,\n");
		sql.append("          DECODE(TVDR.CALL_LEAVEL, " + Constant.DEALER_LEVEL_02 + ", TMD2.DEALER_CODE, TMD1.DEALER_CODE),\n");

		sql.append("          DECODE(TVDR.CALL_LEAVEL, " + Constant.DEALER_LEVEL_02 + ", TMD2.DEALER_SHORTNAME, TMD1.DEALER_SHORTNAME),\n");

		sql.append("          TVO.ORDER_NO,\n");
		sql.append("          TVO.RAISE_DATE,\n");
		sql.append("          TVO.ORDER_YEAR,\n");
		sql.append("          TVO.ORDER_WEEK,TVDR.DLVRY_REQ_NO,\n");
		sql.append("          TVO.ORDER_TYPE,\n");
		sql.append("          TC2.CODE_DESC,\n");
		sql.append("          TVO.VER,\n");
		sql.append("          TVDR.REQ_TOTAL_AMOUNT,");
		sql.append("       	  TVDR.REQ_TOTAL_PRICE,\n");
		sql.append("          TVAT.TYPE_NAME,\n");
		sql.append("		tvdr.F_AUDIT_TIME\n");
		sql.append("   		  ORDER BY tvdr.F_AUDIT_TIME DESC,TVO.RAISE_DATE DESC	");
		PageResult<Map<String, Object>> ps = dao.pageQuery(sql.toString(), params, getFunName(), pageSize, curPage);
		return ps;
	}
	
	
	//发票管理查询
		public Map<String, Object> getInvoiceInfoByReqId(String reqId) throws Exception {

			StringBuffer sql = new StringBuffer();

			sql.append("SELECT DECODE(TVDR.CALL_LEAVEL, " + Constant.DEALER_LEVEL_02 + ",TMD5.DEALER_SHORTNAME, TMD1.DEALER_SHORTNAME) DEALER_NAME1,\n");
			sql.append("       TMD1.DEALER_CODE,\n");
			sql.append("       TMD2.DEALER_TYPE,\n");
			sql.append("       TVDR.REQ_TOTAL_PRICE,\n");
			sql.append("       TMD2.DEALER_SHORTNAME DEALER_NAME2,\n");
			sql.append("       TMD3.DEALER_SHORTNAME DEALER_NAME3,\n");
			sql.append("       TVO.ORDER_NO,\n");
			sql.append("       NVL(TVDR.VER, 0) VER,\n");
			sql.append("       TVO.ORDER_YEAR,\n");
			sql.append("       TVO.ORDER_WEEK,\n");
			sql.append("       TVO.ORDER_TYPE,\n");
			sql.append("       TVO.PRODUCT_COMBO_ID,\n");
			sql.append("       TVDR.AREA_ID,\n");
			//sql.append("       TBA.AREA_NAME,\n");
			sql.append("       TVDR.REQ_STATUS,\n");
			sql.append("       TVDR.REQ_TOTAL_PRICE,\n");
			sql.append("       TVDR.REBATE_AMOUNT,\n");
			sql.append("       TVDR.DELIVERY_TYPE,\n");
			sql.append("       TMF.FLEET_NAME,\n");
			sql.append("       TVDR.FLEET_ADDRESS,\n");
			sql.append("       TVDR.ADDRESS_ID,\n");
			sql.append("       TVA.ADDRESS,\n");
			sql.append("       TVA.RECEIVE_ORG,\n");
			sql.append("       TCC1.CODE_DESC ORDER_TYPE_NAME,\n");
			sql.append("       TCC2.CODE_DESC DELIVERY_TYPE_NAME,\n");
			sql.append("       TVAT.TYPE_NAME,\n");
			sql.append("       TVO.REFIT_REMARK,\n");
			sql.append("       TVO.PAY_REMARK,\n");
			sql.append("       TVDR.REQ_REMARK ORDER_REMARK,\n");
			sql.append("       TVDR.FUND_TYPE FUND_TYPE_ID,\n");
			sql.append("       TVO.ORDER_PRICE,\n");
			sql.append("       TVP.PRICE_DESC,\n");
			sql.append("       TVDR.PRICE_ID,\n");
			sql.append("        '' PRICE_DESC,\n");
			sql.append("       TVDR.OTHER_PRICE_REASON,\n");
			sql.append("       TVDR.RECEIVER,\n");
			sql.append("       TVDR.LINK_MAN,\n");
			sql.append("       TVDR.TEL,\n");
			sql.append("       TVDR.DISCOUNT,\n");
			sql.append("       TVO.ORDER_ORG_ID,\n");
			sql.append("       TVO.BILLING_ORG_ID,\n");
			sql.append("       TVDR.WAREHOUSE_ID,\n");
			sql.append("       TW.WAREHOUSE_NAME,\n");
			sql.append("       TVO.IS_FLEET,\n");
			sql.append("       TVDR.REQ_TOTAL_AMOUNT,\n");
			sql.append("       TVDR.IS_REBATE,\n");
			sql.append("       TVDR.REBATE_AMOUNT,\n");
			sql.append("       TVO.IS_CUSTOM_ADDR,\n");
			sql.append("       TR1.REGION_NAME PROVICE,\n");
			sql.append("       TR2.REGION_NAME CITY,\n");
			sql.append("       TR3.REGION_NAME TOWN,\n");
			sql.append("       TVO.CUSTOM_ADDR,\n");
			sql.append("       TVO.CUSTOM_LINK_MAN,\n");
			sql.append("       TVO.CUSTOM_TEL,\n");
			sql.append("       TOR.ORG_NAME,\n");
			sql.append("       TO_CHAR(TVO.RAISE_DATE, 'YYYY-MM-DD') RAISE_DATE,\n");
			sql.append("       NVL(SUM(TVOD.ORDER_AMOUNT), 0) ORDER_AMOUNT,\n");
			sql.append("       NVL(SUM(TVOD.CHECK_AMOUNT), 0) CHECK_AMOUNT,\n");
			sql.append("       NVL(SUM(TVDRD.RESERVE_AMOUNT), 0) RESERVE_AMOUNT,\n");
			sql.append("       NVL(TVDR.TMP_LICENSE_AMOUNT, 0) TMP_LICENSE_AMOUNT\n");
			sql.append("  FROM TT_VS_ORDER            TVO,\n");
			sql.append("       TT_VS_ORDER_DETAIL     TVOD,\n");
			sql.append("       TT_VS_DLVRY_REQ        TVDR,\n");
			sql.append("       TT_VS_DLVRY_REQ_DTL    TVDRD,\n");
			sql.append("       TM_DEALER              TMD1,\n");
			sql.append("       TM_DEALER              TMD2,\n");
			sql.append("       TM_DEALER              TMD3,\n");
			sql.append("       TM_DEALER              TMD5,\n");
			sql.append("       TC_CODE                TCC1,\n");
			sql.append("       TC_CODE                TCC2,\n");
			sql.append("       TT_VS_PRICE         TVP,\n");
			sql.append("       TM_FLEET               TMF,\n");
			sql.append("       TM_VS_ADDRESS          TVA,\n");
			sql.append("       TT_VS_ACCOUNT_TYPE       TVAT,\n");
			sql.append("       TM_DEALER_ORG_RELATION TDOR,\n");
			sql.append("       TM_REGION              TR1,\n");
			sql.append("        TM_REGION              TR2,\n");
			sql.append("        TM_REGION              TR3,\n");
			sql.append("       TM_WAREHOUSE			  TW,\n");
			sql.append("       TM_ORG                 TOR\n");
			sql.append(" WHERE TVO.ORDER_ID = TVOD.ORDER_ID\n");
			sql.append("   AND TVO.ORDER_ID = TVDR.ORDER_ID\n");
			sql.append("   AND TVDR.REQ_ID = TVDRD.REQ_ID\n");
			sql.append("   AND TVOD.DETAIL_ID = TVDRD.ORDER_DETAIL_ID\n");
			sql.append("   AND TVO.ORDER_ORG_ID = TMD1.DEALER_ID\n");
			sql.append("   AND TVDR.ORDER_DEALER_ID = TMD5.DEALER_ID(+)\n");
			sql.append("   AND TVO.BILLING_ORG_ID = TMD2.DEALER_ID\n");
			sql.append("   AND TVDR.RECEIVER = TMD3.DEALER_ID(+)\n");
			sql.append("   AND TVO.ORDER_TYPE = TCC1.CODE_ID\n");
			sql.append("   AND TVDR.FUND_TYPE = TVAT.TYPE_ID\n");
			sql.append("   AND TVDR.DELIVERY_TYPE = TCC2.CODE_ID\n");
			sql.append("   AND TVA.ID(+) = TVDR.ADDRESS_ID\n");
			sql.append("   AND TMF.FLEET_ID(+) = TVDR.FLEET_ID\n");
			sql.append("   AND TVDR.price_id = tvp.price_id\n");
			sql.append("   AND TVO.BILLING_ORG_ID = TDOR.DEALER_ID\n");
			sql.append("   AND TDOR.ORG_ID = TOR.ORG_ID\n");
			sql.append("   AND TVO.PROVINCE_ID=TR1.REGION_CODE(+)\n");
			sql.append("    AND TVO.CITY_ID=TR2.REGION_CODE(+)\n");
			sql.append("   	AND TVO.TOWN_ID=TR3.REGION_CODE(+)\n");
			sql.append("   AND TVDR.WAREHOUSE_ID = TW.WAREHOUSE_ID(+)\n");
			sql.append("   AND TVDR.REQ_ID = " + reqId + "\n");
			sql.append(" GROUP BY DECODE(TVDR.CALL_LEAVEL, " + Constant.DEALER_LEVEL_02 + ",TMD5.DEALER_SHORTNAME, TMD1.DEALER_SHORTNAME),\n");
			sql.append("          TMD1.DEALER_CODE,\n");
			sql.append("          TMD2.DEALER_TYPE,\n");
			sql.append("          TVDR.REQ_TOTAL_PRICE,\n");
			sql.append("          TMD2.DEALER_SHORTNAME,\n");
			sql.append("          TMD3.DEALER_SHORTNAME,\n");
			sql.append("          TVO.PRODUCT_COMBO_ID,\n");
			sql.append("          TVO.ORDER_NO,\n");
			sql.append("          TVDR.VER,\n");
			sql.append("          TVO.ORDER_YEAR,\n");
			sql.append("          TVO.ORDER_WEEK,\n");
			sql.append("          TVO.ORDER_TYPE,\n");
			sql.append("          TVDR.AREA_ID,\n");
			sql.append("          TVP.PRICE_DESC,\n");
			sql.append("          TVDR.DELIVERY_TYPE,\n");
			sql.append("          TMF.FLEET_NAME,\n");
			sql.append("          TVDR.FLEET_ADDRESS,\n");
			sql.append("          TVDR.ADDRESS_ID,\n");
			sql.append("          TVA.ADDRESS,\n");
			sql.append("          TVA.RECEIVE_ORG,\n");
			sql.append("          TCC1.CODE_DESC,\n");
			sql.append("          TCC2.CODE_DESC,\n");
			sql.append("          TVAT.TYPE_NAME,\n");
			sql.append("          TVO.REFIT_REMARK,\n");
			sql.append("          TVO.PAY_REMARK,\n");
			sql.append("          TVDR.REQ_REMARK,\n");
			sql.append("          TVDR.FUND_TYPE,\n");
			sql.append("          TVO.ORDER_PRICE,\n");
			sql.append("          TVDR.PRICE_ID,\n");
			sql.append("       TVDR.REQ_STATUS,\n");
			sql.append("       TVDR.REQ_TOTAL_PRICE,\n");
			sql.append("       TVDR.REBATE_AMOUNT,\n");
			sql.append("          TVDR.OTHER_PRICE_REASON,\n");
			sql.append("          TVDR.RECEIVER,\n");
			sql.append("          TVDR.LINK_MAN,\n");
			sql.append("          TVDR.TEL,\n");
			sql.append("          TVDR.DISCOUNT,\n");
			sql.append("          TVO.ORDER_ORG_ID,\n");
			sql.append("          TVO.BILLING_ORG_ID,\n");
			sql.append("          TVDR.WAREHOUSE_ID,\n");
			sql.append("       	  TW.WAREHOUSE_NAME,\n");
			sql.append("          TVO.IS_FLEET,\n");
			sql.append("          TVDR.REQ_TOTAL_AMOUNT,\n");
			sql.append("          TOR.ORG_NAME,\n");
			sql.append("          TVDR.TMP_LICENSE_AMOUNT,\n");
			sql.append("       	  TVDR.IS_REBATE,\n");
			sql.append("          TVDR.REBATE_AMOUNT,\n");
			sql.append("          TVO.IS_CUSTOM_ADDR,\n");
			sql.append("          TR1.REGION_NAME,\n");
			sql.append("          TR2.REGION_NAME,\n");
			sql.append("          TR3.REGION_NAME,\n");
			sql.append("          TVO.CUSTOM_ADDR,\n");
			sql.append("          TVO.CUSTOM_LINK_MAN,\n");
			sql.append("          TVO.CUSTOM_TEL,\n");
			sql.append("          TVO.RAISE_DATE");
			List<Map<String, Object>> list = dao.pageQuery(sql.toString(), null, getFunName());
			Map<String, Object> map = new HashMap<String, Object>();
			if (list.size() > 0) {
				map = list.get(0);
			}
			return map;
		}
	
		/**
		 * 发票管理明细
		 * */
		public List<Map<String, Object>> getorderResourceReserveDetailList(String warehouse_id,String reqId, String orderType, String companyId) throws Exception {
			StringBuffer sql = new StringBuffer();
			sql.append("SELECT *                                                                            \n");
			sql.append("  FROM (SELECT TVDRD.DETAIL_ID,                                                     \n");
			sql.append("               TVDR.Dealer_Id,                                                      \n");
			sql.append("               VMI.series_id,                                                       \n");
			sql.append("               TVDRD.MATERIAL_ID,                                                   \n");
			sql.append("               TVM.MATERIAL_CODE,                                                   \n");
			sql.append("               TVM.MATERIAL_NAME,                                                   \n");
			sql.append("               TVDR.WAREHOUSE_ID,                                                   \n");
			sql.append("               decode(TVDRD.PATCH_NO, 'null', '', TVDRD.PATCH_NO) SPECIAL_BATCH_NO, \n");
			sql.append("               NVL(TVDRD.REQ_AMOUNT, 0) REQ_AMOUNT,                                 \n");
			sql.append("               NVL(TVDRD.DELIVERY_AMOUNT, 0) DELIVERY_AMOUNT,                       \n");
			sql.append("               TVDRD.ORDER_DETAIL_ID,                                               \n");
			//sql.append("               NVL(TVDRD.RESERVE_AMOUNT, 0) RESERVE_AMOUNT,                         \n");
			sql.append("               NVL(TVDRD.SINGLE_PRICE, 0) SINGLE_PRICE,                             \n");
			sql.append("               NVL(TVDRD.TOTAL_PRICE, 0) + NVL(TVDRD.DISCOUNT_PRICE, 0) TOTAL_PRICE,\n");
			sql.append("               NVL(TVDRD.DISCOUNT_RATE, 0) DISCOUNT_RATE,                           \n");
			sql.append("               NVL(TVDRD.DISCOUNT_S_PRICE, 0) DISCOUNT_S_PRICE,                     \n");
			sql.append("               NVL(TVDRD.DISCOUNT_PRICE, 0) DISCOUNT_PRICE,                         \n");
			sql.append("               TVDRD.VER,                                                           \n");
			sql.append("               NVL(VVR.STOCK_AMOUNT, 0) - NVL(VVR.LOCK_AMOUT, 0) AVA_STOCK,         \n");
			sql.append("               NVL(VVR.GENERAL_AMOUNT, 0) -                                         \n");
			sql.append("               NVL(VVR.satisfy_general_order, 0) GENERAL_AMOUNT,                    \n");
			sql.append("               NVL(VVR.STOCK_AMOUNT, 0) - NVL(VVR.LOCK_AMOUT, 0) WARHOUSE_STOCK,    \n");
			sql.append("               TVDR.DLVRY_REQ_NO DLVRY_REQ_NO,                                       \n");
			sql.append("               NVL(TVDRD.RESERVE_AMOUNT,0) RESERVE_AMOUNT,               \n");
			sql.append("                NVL(TVDRD.BILL_AMOUNT,0)  BILL_AMOUNT,                \n");
			sql.append("               TVDRD.REBATE_AMOUNT                                       \n");
			sql.append("          FROM TT_VS_ORDER_DETAIL  TVOD,                                            \n");
			sql.append("               TT_VS_DLVRY_REQ     TVDR,                                            \n");
			sql.append("               TM_VHCL_MATERIAL    TVM,                                             \n");
			sql.append("               VW_MATERIAL_INFO    VMI,                                             \n");
			sql.append("               TT_VS_DLVRY_REQ_DTL TVDRD                                            \n");
			sql.append("          LEFT JOIN (SELECT *                                                       \n");
			sql.append("                      FROM VW_VS_RESOURCE_ENTITY_WEEK_NEW                           \n");
			sql.append("                     WHERE WAREHOUSE_ID =                                           \n");
			sql.append("                           (SELECT WAREHOUSE_ID                                     \n");
			sql.append("                              FROM TT_VS_DLVRY_REQ                                  \n");
			sql.append("                             WHERE REQ_ID = "+reqId+")) VVR                         \n");
			sql.append("            ON TVDRD.MATERIAL_ID = VVR.MATERIAL_ID                                  \n");
			sql.append("           AND VVR.COMPANY_ID = "+companyId+"                                       \n");
			sql.append("         WHERE TVDR.REQ_ID = TVDRD.REQ_ID                                           \n");
			sql.append("           AND TVOD.DETAIL_ID = TVDRD.ORDER_DETAIL_ID                               \n");
			sql.append("           AND TVDRD.MATERIAL_ID = TVM.MATERIAL_ID                                  \n");
			sql.append("           AND TVDRD.REQ_ID = "+reqId+"                                             \n");
			sql.append("           AND TVDRD.MATERIAL_ID = VMI.material_id) INFO                            \n");
			sql.append("  LEFT JOIN VW_VS_PLAN_REQ_AMOUNT VPR                                               \n");
			sql.append("    ON INFO.DEALER_ID = VPR.DEALER_ID                                               \n");
			sql.append("   AND INFO.SERIES_ID = VPR.SERIES_ID                                               \n");
			List<Map<String, Object>> list = dao.pageQuery(sql.toString(), null, getFunName());
			return list;
		}
		
		
	public int updateBalance(String accountTypeId,String dealerId,Double totalAmount){
		StringBuffer sql=new StringBuffer();
		sql.append(" update tt_vs_account t set t.update_date=sysdate,t.balance_amount=t.balance_amount-"+totalAmount+" where t.account_type_id="+accountTypeId+"and dealer_id="+dealerId);
		return dao.update(sql.toString(), null);
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}

}
