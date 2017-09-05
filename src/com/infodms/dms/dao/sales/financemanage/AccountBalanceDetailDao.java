/**********************************************************************
* <pre>
* FILE : AccountBalanceDetailDao.java
* CLASS : AccountBalanceDetailDao
* AUTHOR : 
* FUNCTION : 财务管理
*======================================================================
* CHANGE HISTORY LOG
*----------------------------------------------------------------------
* MOD. NO.|   DATE   |    NAME    | REASON  |  CHANGE REQ.
*----------------------------------------------------------------------
*         |2010-05-31|            | Created |
* DESCRIPTION:
* </pre>
***********************************************************************/
package com.infodms.dms.dao.sales.financemanage;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.sales.ordermanage.orderreport.LowWeekGeneralOrderCall;
import com.infodms.dms.actions.sales.ordermanage.orderreport.UrgentOrderReport;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.dao.relation.DealerRelationDAO;
import com.infodms.dms.dao.sales.ordermanage.orderreport.OrderReportDao;
import com.infodms.dms.po.TmDealerPO;
import com.infodms.dms.po.TtVsAccountFreezePO;
import com.infodms.dms.po.TtVsAccountLogPO;
import com.infodms.dms.po.TtVsAccountPO;
import com.infodms.dms.po.TtVsAccountTypePO;
import com.infodms.dms.po.TtVsDlvryReqGeneralDtlPO;
import com.infodms.dms.po.TtVsDlvryReqPO;
import com.infodms.dms.po.TtVsOrderDetailPO;
import com.infodms.dms.po.TtVsOrderPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.PersisUtil;
import com.infodms.dms.util.StringUtil;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;
import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;
/**
 * @Title: 财务管理DAO
 * @Description:InfoFrame3.0.V01
 * @Copyright: Copyright (c) 2010
 * @Company: www.infoservice.com.cn
 * @Date: 2010-5-31
 * @author 
 * @mail   
 * @version 1.0
 * @remark 
 */
/**
 * @author Administrator
 *
 */
public class AccountBalanceDetailDao extends BaseDao{
	
	public static Logger logger = Logger.getLogger(AccountBalanceDetailDao.class);
	private static final AccountBalanceDetailDao dao = new AccountBalanceDetailDao ();
	public static final AccountBalanceDetailDao getInstance() {
		return dao;
	}
	
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
//	/**
//	 * Function         : OEM账户余额查询
//	 * @param           : 经销商CODE
//	 * @param           : 分页参数
//	 * @return          : 经销商账户信息
//	 * @throws          : Exception
//	 * LastUpdate       : 2010-05-31
//	 */
//	public PageResult<Map<String, Object>> oemAccountBalanceQuery(String dealerCode,Long companyId,int curPage,int pageSize) throws Exception{
//		List<Object> params = new LinkedList<Object>();
//		StringBuffer sql= new StringBuffer();
//		sql.append("SELECT DEALER_ID,\n" );
//		sql.append("       DEALER_CODE,\n" );
//		sql.append("       DEALER_NAME,\n" );
//		sql.append("       ZY_YE,\n" );
//		sql.append("       ZY_YK,\n" );
//		sql.append("       ZY_KY,\n" );
//		sql.append("       XY_YE,\n" );
//		sql.append("       XY_YK,\n" );
//		sql.append("       XY_KY,\n" );
//		sql.append("       JR_YE,\n" );
//		sql.append("       JR_YK,\n" );
//		sql.append("       JR_KY,\n" );
//		sql.append("       ZY_KY + XY_KY + JR_KY AS KYHJ\n" );
//		sql.append("  FROM (SELECT TMD.DEALER_ID,\n" );
//		sql.append("               TMD.DEALER_CODE,\n" );
//		sql.append("               TMD.DEALER_SHORTNAME AS DEALER_NAME,\n" );
//		sql.append("               MAX(DECODE(TSAT.TYPE_CODE,\n" );
//		sql.append("                          'ZYZJ',\n" );
//		sql.append("                          TSA.BALANCE_AMOUNT,\n" );
//		sql.append("                          NULL)) AS ZY_YE,\n" );
//		sql.append("               MAX(DECODE(TSAT.TYPE_CODE,\n" );
//		sql.append("                           'ZYZJ',\n" );
//		sql.append("                          TSA.FREEZE_AMOUNT,\n" );
//		sql.append("                          NULL)) AS ZY_YK,\n" );
//		sql.append("               MAX(DECODE(TSAT.TYPE_CODE,\n" );
//		sql.append("                          'ZYZJ',\n" );
//		sql.append("                          TSA.AVAILABLE_AMOUNT,\n" );
//		sql.append("                          NULL)) AS ZY_KY,\n" );
//		sql.append("               MAX(DECODE(TSAT.TYPE_CODE,\n" );
//		sql.append("                          'XYXS',\n" );
//		sql.append("                          TSA.BALANCE_AMOUNT,\n" );
//		sql.append("                          NULL)) AS XY_YE,\n" );
//		sql.append("               MAX(DECODE(TSAT.TYPE_CODE,\n" );
//		sql.append("                          'XYXS',\n" );
//		sql.append("                          TSA.FREEZE_AMOUNT,\n" );
//		sql.append("                          NULL)) AS XY_YK,\n" );
//		sql.append("               MAX(DECODE(TSAT.TYPE_CODE,\n" );
//		sql.append("                          'XYXS',\n" );
//		sql.append("                          TSA.AVAILABLE_AMOUNT,\n" );
//		sql.append("                          NULL)) AS XY_KY,\n" );
//		sql.append("               MAX(DECODE(TSAT.TYPE_CODE,\n" );
//		sql.append("                          'JRFW',\n" );
//		sql.append("                          TSA.BALANCE_AMOUNT,\n" );
//		sql.append("                          NULL)) AS JR_YE,\n" );
//		sql.append("               MAX(DECODE(TSAT.TYPE_CODE,\n" );
//		sql.append("                          'JRFW',\n" );
//		sql.append("                          TSA.FREEZE_AMOUNT,\n" );
//		sql.append("                          NULL)) AS JR_YK,\n" );
//		sql.append("               MAX(DECODE(TSAT.TYPE_CODE,\n" );
//		sql.append("                          'JRFW',\n" );
//		sql.append("                          TSA.AVAILABLE_AMOUNT,\n" );
//		sql.append("                          NULL)) AS JR_KY\n" );
//		sql.append("          FROM TT_VS_ACCOUNT TSA, TM_DEALER TMD,TT_VS_ACCOUNT_TYPE TSAT\n" );
//		sql.append("         WHERE TSA.DEALER_ID = TMD.DEALER_ID\n" );
//		sql.append("          AND  TSA.ACCOUNT_TYPE_ID = TSAT.TYPE_ID\n" );
//		sql.append("          AND  TMD.OEM_COMPANY_ID = "+companyId+"\n" );
//		if (!"".equals(dealerCode)&&null!=dealerCode){
//			sql.append(Utility.getConSqlByParamForEqual(dealerCode, params, "TMD", "DEALER_CODE"));
//		}
//		sql.append("           AND TMD.STATUS = "+Constant.STATUS_ENABLE+"\n" );
//		sql.append("         GROUP BY TMD.DEALER_ID, TMD.DEALER_CODE, TMD.DEALER_SHORTNAME)");
//		PageResult<Map<String, Object>> rs = dao.pageQuery(sql.toString(), params, getFunName(),pageSize, curPage);
//		return rs;
//	}
	/**
	 * Function         : OEM账户余额查询
	 * @param           : 经销商CODE
	 * @return          : 经销商账户详细信息
	 * @throws          : Exception
	 * LastUpdate       : 2010-05-31
	 */
	public List oemAccountBalanceQueryInfo(String dealerId,Long companyId) throws Exception{
		List<Object> params = new LinkedList<Object>();
		StringBuffer sql= new StringBuffer();
		sql.append("select TSAT.TYPE_NAME,TSA.BALANCE_AMOUNT,TSA.FREEZE_AMOUNT,TSA.AVAILABLE_AMOUNT,(TSA.BALANCE_AMOUNT+TSA.FREEZE_AMOUNT+TSA.AVAILABLE_AMOUNT)hj\n" );
		sql.append("from TT_VS_ACCOUNT TSA, TM_DEALER TMD, TT_VS_ACCOUNT_TYPE TSAT\n" );
		sql.append(" where TMD.DEALER_ID=TSA.DEALER_ID\n" );
		sql.append(" AND TSA.ACCOUNT_TYPE_ID=TSAT.TYPE_ID\n" );
		if(dealerId!=null&&dealerId!=""){
			sql.append(" AND TMD.DEALER_ID="+dealerId);
		}
		//sql.append("GROUP BY TMD.DEALER_ID, TMD.DEALER_CODE, TMD.DEALER_SHORTNAME)");
		List rs =dao.pageQuery(sql.toString(), params, getFunName());
		return rs;
	}
	/*
	 * 求合计
	 */
	public Map<String, Object> getAccountHj(String dealerId,Long companyId) throws Exception{
		List<Object> params = new LinkedList<Object>();
		Map<String, Object> map = new  HashMap<String, Object>();
		StringBuffer sql= new StringBuffer();
		sql.append("select sum( TSA.BALANCE_AMOUNT) BALANCE_AMOUNT,sum(TSA.FREEZE_AMOUNT) FREEZE_AMOUNT,sum( TSA.AVAILABLE_AMOUNT) AVAILABLE_AMOUNT\n" );
		sql.append("from TT_VS_ACCOUNT TSA, TM_DEALER TMD, TT_VS_ACCOUNT_TYPE TSAT\n" );
		sql.append(" where TMD.DEALER_ID=TSA.DEALER_ID\n" );
		sql.append(" AND TSA.ACCOUNT_TYPE_ID=TSAT.TYPE_ID\n" );
		if(dealerId!=null&&dealerId!=""){
			sql.append(" AND TMD.DEALER_ID="+dealerId);
		}
		List rs =dao.pageQuery(sql.toString(), params, getFunName());
		if(rs.size()>0){
			map=(Map<String, Object>) rs.get(0);
		}
		return map;
		
	}
	/*
	 * 获取经销商数据
	 */
	public List<Map<String, Object>> getAccountListInfo(String dealerId,Long companyId) throws Exception{
		StringBuffer sql= new StringBuffer();
		sql.append("select  TSA.Account_Id,TMD.DEALER_NAME,TMD.DEALER_CODE,TSAT.TYPE_NAME,TSA.BALANCE_AMOUNT,TSA.FREEZE_AMOUNT,TSA.AVAILABLE_AMOUNT,(TSA.BALANCE_AMOUNT+TSA.FREEZE_AMOUNT+TSA.AVAILABLE_AMOUNT)hj\n" );
		sql.append(" from TT_VS_ACCOUNT TSA, TM_DEALER TMD, TT_VS_ACCOUNT_TYPE TSAT\n" );
		sql.append(" where TMD.DEALER_ID=TSA.DEALER_ID\n" );
		sql.append(" AND TSA.ACCOUNT_TYPE_ID=TSAT.TYPE_ID\n" );
		if(dealerId!=null&&dealerId!=""){
			sql.append(" AND TMD.DEALER_ID in("+dealerId+")");
		}
		List<Map<String, Object>> list= dao.pageQuery(sql.toString(), null, getFunName());
		return list;
	}
	/*
	 * 获取经销商
	 */
	public List<Map<String, Object>> getAccountDealer(String dealerId,Long companyId) throws Exception{
		StringBuffer sql= new StringBuffer();
		sql.append("select  TMD.DEALER_CODE,TMD.DEALER_NAME,sum( TSA.BALANCE_AMOUNT) BALANCE_AMOUNT,sum(TSA.FREEZE_AMOUNT) FREEZE_AMOUNT,sum( TSA.AVAILABLE_AMOUNT)AVAILABLE_AMOUNT\n" );
		sql.append(" from TT_VS_ACCOUNT TSA, TM_DEALER TMD, TT_VS_ACCOUNT_TYPE TSAT\n" );
		sql.append(" where TMD.DEALER_ID=TSA.DEALER_ID\n" );
		sql.append(" AND TSA.ACCOUNT_TYPE_ID=TSAT.TYPE_ID\n" );
		if(dealerId!=null&&dealerId!=""){
			sql.append(" AND TMD.DEALER_ID in("+dealerId+")");
		}
		sql.append(" group by TMD.DEALER_CODE,TMD.DEALER_NAME");
		List<Map<String, Object>> list= dao.pageQuery(sql.toString(), null, getFunName());
		return list;
	}
	
	
	
	/**
	 * Function         : OEM账户余额下载
	 * @param           : 经销商CODE
	 * @return          : 经销商账户信息
	 * @throws          : Exception
	 * LastUpdate       : 2010-05-31
	 */
	public PageResult<Map<String,Object>> oemAccountBalanceDownLoad(String dealerCode,Long companyId,int curPage,int pageSize) throws Exception{
		List<Object> params = new LinkedList<Object>();
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT DEALER_ID,\n" );
		sql.append("       DEALER_CODE,\n" );
		sql.append("       DEALER_NAME,\n" );
		sql.append("       ZY_YE,\n" );
		sql.append("       ZY_YK,\n" );
		sql.append("       ZY_KY,\n" );
		sql.append("       XY_YE,\n" );
		sql.append("       XY_YK,\n" );
		sql.append("       XY_KY,\n" );
		sql.append("       JR_YE,\n" );
		sql.append("       JR_YK,\n" );
		sql.append("       JR_KY,\n" );
		sql.append("       ZY_KY + XY_KY + JR_KY AS KYHJ\n" );
		sql.append("  FROM (SELECT TMD.DEALER_ID,\n" );
		sql.append("               TMD.DEALER_CODE,\n" );
		sql.append("               TMD.DEALER_SHORTNAME AS DEALER_NAME,\n" );
		sql.append("               MAX(DECODE(TSAT.TYPE_CODE,\n" );
		sql.append("                          'ZYZJ',\n" );
		sql.append("                          TSA.BALANCE_AMOUNT,\n" );
		sql.append("                          NULL)) AS ZY_YE,\n" );
		sql.append("               MAX(DECODE(TSAT.TYPE_CODE,\n" );
		sql.append("                           'ZYZJ',\n" );
		sql.append("                          TSA.FREEZE_AMOUNT,\n" );
		sql.append("                          NULL)) AS ZY_YK,\n" );
		sql.append("               MAX(DECODE(TSAT.TYPE_CODE,\n" );
		sql.append("                          'ZYZJ',\n" );
		sql.append("                          TSA.AVAILABLE_AMOUNT,\n" );
		sql.append("                          NULL)) AS ZY_KY,\n" );
		sql.append("               MAX(DECODE(TSAT.TYPE_CODE,\n" );
		sql.append("                          'XYXS',\n" );
		sql.append("                          TSA.BALANCE_AMOUNT,\n" );
		sql.append("                          NULL)) AS XY_YE,\n" );
		sql.append("               MAX(DECODE(TSAT.TYPE_CODE,\n" );
		sql.append("                          'XYXS',\n" );
		sql.append("                          TSA.FREEZE_AMOUNT,\n" );
		sql.append("                          NULL)) AS XY_YK,\n" );
		sql.append("               MAX(DECODE(TSAT.TYPE_CODE,\n" );
		sql.append("                          'XYXS',\n" );
		sql.append("                          TSA.AVAILABLE_AMOUNT,\n" );
		sql.append("                          NULL)) AS XY_KY,\n" );
		sql.append("               MAX(DECODE(TSAT.TYPE_CODE,\n" );
		sql.append("                          'JRFW',\n" );
		sql.append("                          TSA.BALANCE_AMOUNT,\n" );
		sql.append("                          NULL)) AS JR_YE,\n" );
		sql.append("               MAX(DECODE(TSAT.TYPE_CODE,\n" );
		sql.append("                          'JRFW',\n" );
		sql.append("                          TSA.FREEZE_AMOUNT,\n" );
		sql.append("                          NULL)) AS JR_YK,\n" );
		sql.append("               MAX(DECODE(TSAT.TYPE_CODE,\n" );
		sql.append("                          'JRFW',\n" );
		sql.append("                          TSA.AVAILABLE_AMOUNT,\n" );
		sql.append("                          NULL)) AS JR_KY\n" );
		sql.append("          FROM TT_VS_ACCOUNT TSA, TM_DEALER TMD,TT_VS_ACCOUNT_TYPE TSAT\n" );
		sql.append("         WHERE TSA.DEALER_ID = TMD.DEALER_ID\n" );
		sql.append("          AND  TSA.ACCOUNT_TYPE_ID = TSAT.TYPE_ID\n" );
		sql.append("          AND  TMD.OEM_COMPANY_ID = "+companyId+"\n" );
		if (!"".equals(dealerCode)&&null!=dealerCode){
			sql.append(Utility.getConSqlByParamForEqual(dealerCode, params, "TMD", "DEALER_CODE"));
		}
		sql.append("           AND TMD.STATUS = "+Constant.STATUS_ENABLE+"\n" );
		sql.append("         GROUP BY TMD.DEALER_ID, TMD.DEALER_CODE, TMD.DEALER_SHORTNAME)");
		PageResult<Map<String,Object>> ps = dao.pageQuery(sql.toString(), params, getFunName(),pageSize, curPage);
		return ps;
	}
	/**
	 * Function         : DLR账户余额查询
	 * @param           : 经销商ID
	 * @param           : 分页参数
	 * @return          : 经销商账户信息
	 * @throws          : Exception
	 * LastUpdate       : 2010-05-31
	 */
	public PageResult<Map<String, Object>> dlrAccountBalanceQuery(String dealerId,String areaIds,Long oemCompanyId,int curPage,int pageSize) throws Exception{
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT DEALER_ID,\n" );
		sql.append("       DEALER_CODE,\n" );
		sql.append("       DEALER_NAME,\n" );
		sql.append("       ZY_YE,\n" );
		sql.append("       ZY_YK,\n" );
		sql.append("       ZY_KY,\n" );
		sql.append("       XY_YE,\n" );
		sql.append("       XY_YK,\n" );
		sql.append("       XY_KY,\n" );
		sql.append("       JR_YE,\n" );
		sql.append("       JR_YK,\n" );
		sql.append("       JR_KY,\n" );
		sql.append("       ZY_KY + XY_KY + JR_KY AS KYHJ\n" );
		sql.append("  FROM (SELECT TMD.DEALER_ID,\n" );
		sql.append("               TMD.DEALER_CODE,\n" );
		sql.append("               TMD.DEALER_SHORTNAME AS DEALER_NAME,\n" );
		sql.append("               MAX(DECODE(TSAT.TYPE_CODE,\n" );
		sql.append("                          'ZYZJ',\n" );
		sql.append("                          TSA.BALANCE_AMOUNT,\n" );
		sql.append("                          NULL)) AS ZY_YE,\n" );
		sql.append("               MAX(DECODE(TSAT.TYPE_CODE,\n" );
		sql.append("                           'ZYZJ',\n" );
		sql.append("                          TSA.FREEZE_AMOUNT,\n" );
		sql.append("                          NULL)) AS ZY_YK,\n" );
		sql.append("               MAX(DECODE(TSAT.TYPE_CODE,\n" );
		sql.append("                          'ZYZJ',\n" );
		sql.append("                          TSA.AVAILABLE_AMOUNT,\n" );
		sql.append("                          NULL)) AS ZY_KY,\n" );
		sql.append("               MAX(DECODE(TSAT.TYPE_CODE,\n" );
		sql.append("                          'XYXS',\n" );
		sql.append("                          TSA.BALANCE_AMOUNT,\n" );
		sql.append("                          NULL)) AS XY_YE,\n" );
		sql.append("               MAX(DECODE(TSAT.TYPE_CODE,\n" );
		sql.append("                          'XYXS',\n" );
		sql.append("                          TSA.FREEZE_AMOUNT,\n" );
		sql.append("                          NULL)) AS XY_YK,\n" );
		sql.append("               MAX(DECODE(TSAT.TYPE_CODE,\n" );
		sql.append("                          'XYXS',\n" );
		sql.append("                          TSA.AVAILABLE_AMOUNT,\n" );
		sql.append("                          NULL)) AS XY_KY,\n" );
		sql.append("               MAX(DECODE(TSAT.TYPE_CODE,\n" );
		sql.append("                          'JRFW',\n" );
		sql.append("                          TSA.BALANCE_AMOUNT,\n" );
		sql.append("                          NULL)) AS JR_YE,\n" );
		sql.append("               MAX(DECODE(TSAT.TYPE_CODE,\n" );
		sql.append("                          'JRFW',\n" );
		sql.append("                          TSA.FREEZE_AMOUNT,\n" );
		sql.append("                          NULL)) AS JR_YK,\n" );
		sql.append("               MAX(DECODE(TSAT.TYPE_CODE,\n" );
		sql.append("                          'JRFW',\n" );
		sql.append("                          TSA.AVAILABLE_AMOUNT,\n" );
		sql.append("                          NULL)) AS JR_KY\n" );
		sql.append("          FROM TT_VS_ACCOUNT TSA, TM_DEALER TMD,TT_VS_ACCOUNT_TYPE TSAT,TM_DEALER_BUSINESS_AREA TDBA\n" );
		sql.append("         WHERE TSA.DEALER_ID = TMD.DEALER_ID\n" );
		sql.append("          AND  TSA.ACCOUNT_TYPE_ID = TSAT.TYPE_ID\n" );
		sql.append("          AND  TMD.DEALER_ID = TDBA.DEALER_ID\n" );
		sql.append("          AND  TMD.STATUS = "+Constant.STATUS_ENABLE+"\n" );
		sql.append("          AND  TMD.OEM_COMPANY_ID = "+oemCompanyId+"\n" );
		if(!"".equals(areaIds)&&areaIds!=null){
			sql.append("          AND  TDBA.AREA_ID IN("+areaIds+")\n" );
		}
		if(!"".equals(dealerId)&&dealerId!=null){
			sql.append("           AND TMD.DEALER_ID IN( "+dealerId+")\n" );
		}
		sql.append("         GROUP BY TMD.DEALER_ID, TMD.DEALER_CODE, TMD.DEALER_SHORTNAME)");
		PageResult<Map<String, Object>> rs = dao.pageQuery(sql.toString(), null, getFunName(),pageSize, curPage);
		return rs;
	}
	
	/**
	 * Function         : 账户类型查询
	 * @return          : 账户类型信息
	 * @throws          : Exception
	 * LastUpdate       : 2010-05-25
	 */
	public List<Map<String, Object>> getAccountType()throws Exception{
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT  TSAT.TYPE_ID,TSAT.TYPE_NAME\n" );
		sql.append("  FROM TT_VS_ACCOUNT_TYPE TSAT\n" );
		sql.append(" WHERE  TSAT.STATUS ="+Constant.STATUS_ENABLE+"\n" );
		List<Map<String, Object>> list= dao.pageQuery(sql.toString(), null, getFunName());
		return list;
	}
	
	/**
	 * Function         : OEM账户明细查询
	 * @param           : 开始时间
	 * @param           : 结束时间
	 * @param           : 经销商CODE
	 * @param           : 账户类型
	 * @param           : 操作类型
	 * @param           : 分页参数
	 * @return          : 经销商账户信息
	 * @throws          : Exception
	 * LastUpdate       : 2010-05-31
	 */
	public PageResult<Map<String, Object>> oemAccountDetailQuery(String orgId, String areaIds,String areaId, String startDate,String endDate,String dealerCode,String accountType,String changeType,int curPage,int pageSize) throws Exception{
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT TSAC.CHNG_ID,\n" );
		sql.append("       TO_CHAR(TSAC.CHNG_DATE,'YYYY-MM-DD') AS CHNG_DATE,\n" );
		sql.append("       TSAT.TYPE_NAME,\n" );
		sql.append("       TSA.BALANCE_AMOUNT,\n" );
		sql.append("       TSA.ACCOUNT_NAME,\n" );
		sql.append("       TSAC.CHNG_TYPE,\n" );
		sql.append("       TSAC.ERP_DOC_NO,\n" );
		sql.append("       TSAC.GOLDEN_INVOICE_NO,\n" );
		sql.append("       TSAC.EXTERNAL_DOC_NO,\n" );
		sql.append("       TSAC.REMARK,\n" );
		sql.append("       MAX((CASE WHEN TSAC.CHNG_AMOUNT >0 THEN TSAC.CHNG_AMOUNT END ))AS CHANG_AMOUNT1,\n" );
		sql.append("       MAX((CASE WHEN TSAC.CHNG_AMOUNT <0 THEN TSAC.CHNG_AMOUNT END ))AS CHANG_AMOUNT2,\n" );
		sql.append("       TMD.DEALER_CODE,\n");
		sql.append("       TMD.DEALER_SHORTNAME\n");
		sql.append("  FROM TT_VS_ACCOUNT TSA, TT_VS_ACCOUNT_CHNG TSAC, TT_VS_ACCOUNT_TYPE TSAT,TM_DEALER TMD,VW_ORG_DEALER VOD,TM_DEALER_BUSINESS_AREA A\n" );
		sql.append(" WHERE TSAC.ACCOUNT_ID = TSA.ACCOUNT_ID\n" );
		sql.append("   AND TSA.ACCOUNT_TYPE_ID = TSAT.TYPE_ID\n" );
		sql.append("   AND TSA.DEALER_ID = TMD.DEALER_ID\n" );
		sql.append("   AND TMD.DEALER_ID = A.DEALER_ID\n");
		sql.append("   AND TMD.DEALER_ID = VOD.DEALER_ID\n");
		if(!"".equals(startDate)&&startDate!=null&&!"".equals(endDate)&&endDate!=null){
			sql.append("   AND TSAC.CHNG_DATE BETWEEN TO_DATE('"+startDate+" 00:00:00','YYYY-MM-DD HH24:MI:SS')  AND TO_DATE('"+endDate+" 23:59:59','YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(!"".equals(dealerCode)&&dealerCode!=null){
			sql.append("   AND TMD.DEALER_CODE = '"+dealerCode+"'\n");
		}
		if(!"-1".equals(accountType)&&!"".equals(accountType)&&accountType!=null){
			sql.append("   AND TSAT.TYPE_ID = "+accountType+"\n");
		}
		if(!"-1".equals(changeType)&&!"".equals(changeType)&&changeType!=null){
			sql.append("   AND TSAC.CHNG_TYPE = "+changeType+"\n");
		}
		if (!"".equals(areaId)) {
			sql.append("   AND A.AREA_ID = "+areaId+"\n");
		}
		if (!"".equals(areaIds) && "".equals(areaId)) {
			sql.append("   AND A.AREA_ID IN ("+areaIds+")\n");
		}
		if(!CommonUtils.isNullString(orgId)) {
			sql.append("   AND VOD.ROOT_ORG_ID IN ("+orgId+")\n");
		}
		sql.append("   GROUP BY TSAC.CHNG_ID,\n" );
		sql.append("       TSAC.CHNG_DATE,\n" );
		sql.append("       TSAT.TYPE_NAME,\n" );
		sql.append("       TSA.BALANCE_AMOUNT,\n" );
		sql.append("       TSA.ACCOUNT_NAME,\n" );
		sql.append("       TSAC.CHNG_TYPE,\n" );
		sql.append("       TSAC.ERP_DOC_NO,\n" );
		sql.append("       TSAC.GOLDEN_INVOICE_NO,\n" );
		sql.append("       TSAC.EXTERNAL_DOC_NO,\n" );
		sql.append("       TSAC.REMARK,TMD.DEALER_CODE,TMD.DEALER_SHORTNAME\n" );
		sql.append("       ORDER BY CHNG_ID\n");
		PageResult<Map<String, Object>> rs = dao.pageQuery(sql.toString(), null, getFunName(),pageSize, curPage);
		return rs;
	}
	
	/**
	 * Function         : OEM账户明细查询--下载
	 * @param           : 开始时间
	 * @param           : 结束时间
	 * @param           : 经销商CODE
	 * @param           : 账户类型
	 * @param           : 操作类型
	 * @param           : 分页参数
	 * @return          : 经销商账户信息
	 * @throws          : Exception
	 * LastUpdate       : 2010-05-31
	 */
	public PageResult<Map<String, Object>> oemAccountDetailDownLoad(String orgId, String areaIds,String areaId,String startDate,String endDate,String dealerCode,String accountType,String changeType,int curPage,int pageSize) throws Exception{
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT TSAC.CHNG_ID,\n" );
		sql.append("       TO_CHAR(TSAC.CHNG_DATE,'YYYY-MM-DD') AS CHNG_DATE,\n" );
		sql.append("       TSAT.TYPE_NAME,\n" );
		sql.append("       TSA.ACCOUNT_NAME,\n" );
		sql.append("       TCC.CODE_DESC CHNG_TYPE,\n" );
		sql.append("       TSAC.ERP_DOC_NO,\n" );
		sql.append("       TSAC.GOLDEN_INVOICE_NO,\n" );
		sql.append("       TSAC.EXTERNAL_DOC_NO,\n" );
		sql.append("       TSAC.REMARK,\n" );
		sql.append("        MAX((CASE WHEN TSAC.CHNG_AMOUNT >0 THEN TSAC.CHNG_AMOUNT END ))AS CHANG_AMOUNT1,\n" );
		sql.append("       MAX((CASE WHEN TSAC.CHNG_AMOUNT <0 THEN TSAC.CHNG_AMOUNT END ))AS CHANG_AMOUNT2,\n" );
		sql.append("       TMD.DEALER_CODE,\n");
		sql.append("       TMD.DEALER_SHORTNAME\n");
		sql.append("  FROM TT_VS_ACCOUNT TSA, TT_VS_ACCOUNT_CHNG TSAC, TT_VS_ACCOUNT_TYPE TSAT,TM_DEALER TMD,VW_ORG_DEALER VOD, TC_CODE TCC,TM_DEALER_BUSINESS_AREA A\n" );
		sql.append(" WHERE TSAC.ACCOUNT_ID = TSA.ACCOUNT_ID\n" );
		sql.append("   AND TSA.ACCOUNT_TYPE_ID = TSAT.TYPE_ID\n" );
		sql.append("   AND TSAC.CHNG_TYPE = TCC.CODE_ID\n" );
		sql.append("   AND TSA.DEALER_ID = TMD.DEALER_ID\n" );
		sql.append("   AND TMD.DEALER_ID = A.DEALER_ID\n");
		sql.append("   AND TMD.DEALER_ID = VOD.DEALER_ID\n");
		if (!"".equals(areaId)) {
			sql.append("   AND A.AREA_ID = "+areaId+"\n");
		}
		if (!"".equals(areaIds) && "".equals(areaId)) {
			sql.append("   AND A.AREA_ID IN ("+areaIds+")\n");
		}
		if(!"".equals(startDate)&&startDate!=null&&!"".equals(endDate)&&endDate!=null){
			sql.append("   AND TSAC.CHNG_DATE BETWEEN TO_DATE('"+startDate+" 00:00:00','YYYY-MM-DD HH24:MI:SS')  AND TO_DATE('"+endDate+" 23:59:59','YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(!"".equals(dealerCode)&&dealerCode!=null){
			sql.append("   AND TMD.DEALER_CODE = '"+dealerCode+"'\n");
		}
		if(!"-1".equals(accountType)&&!"".equals(accountType)&&accountType!=null){
			sql.append("   AND TSAT.TYPE_ID = "+accountType+"\n");
		}
		if(!"-1".equals(changeType)&&!"".equals(changeType)&&changeType!=null){
			sql.append("   AND TSAC.CHNG_TYPE = "+changeType+"\n");
		}
		if(!CommonUtils.isNullString(orgId)) {
			sql.append("   AND VOD.ROOT_ORG_ID IN ("+orgId+")\n");
		}
		sql.append("   GROUP BY TSAC.CHNG_ID,\n" );
		sql.append("       TSAC.CHNG_DATE,\n" );
		sql.append("       TSAT.TYPE_NAME,\n" );
		sql.append("       TSA.ACCOUNT_NAME,\n" );
		sql.append("       TCC.CODE_DESC,\n" );
		sql.append("       TSAC.ERP_DOC_NO,\n" );
		sql.append("       TSAC.GOLDEN_INVOICE_NO,\n" );
		sql.append("       TSAC.EXTERNAL_DOC_NO,\n" );
		sql.append("       TSAC.REMARK,TMD.DEALER_CODE,TMD.DEALER_SHORTNAME\n" );
		sql.append("       ORDER BY CHNG_ID\n");
		PageResult<Map<String, Object>> rs = dao.pageQuery(sql.toString(), null, getFunName(),pageSize, curPage);
		return rs;
	}
	/**
	 * Function         : 期初余额查询
	 * @param			: 经销商CODE
	 * @param			: 期初时间
	 * @param			: 账户类型
	 * @return          : 期初余额信息
	 * @throws          : Exception
	 * LastUpdate       : 2010-05-25
	 */
	public Map<String, Object> getOemStartAccount(String startDate,String dealerCode,String accountType)throws Exception{
		Map<String, Object> map = new  HashMap<String, Object>();
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT NVL(TTSA.BALANCE_AMOUNT,0)- NVL(TSACD.CHNG_AMOUNT, 0) AMOUNT1\n" );
		sql.append("  FROM TT_VS_ACCOUNT TTSA,\n" );
		sql.append("       TT_VS_ACCOUNT_TYPE TSAT,\n" );
		sql.append("       TM_DEALER TMD,\n" );
		sql.append("       (SELECT SUM(TSAC.CHNG_AMOUNT) CHNG_AMOUNT, TSAC.ACCOUNT_ID\n" );
		sql.append("          FROM TT_VS_ACCOUNT TSA, TT_VS_ACCOUNT_CHNG TSAC\n" );
		sql.append("         WHERE TSA.ACCOUNT_ID = TSAC.ACCOUNT_ID\n" );
		if(!"".equals(startDate)&&startDate!=null){
			sql.append("           AND TSAC.CHNG_DATE BETWEEN TO_DATE('"+startDate+" 00:00:00', 'YYYY-MM-DD HH24:MI:SS') AND\n" );
			sql.append("               SYSDATE\n" );
		}
		sql.append("         GROUP BY TSAC.ACCOUNT_ID) TSACD\n" );
		sql.append(" WHERE TTSA.ACCOUNT_ID = TSACD.ACCOUNT_ID(+)\n" );
		if(!"".equals(dealerCode)&&dealerCode!=null){
			sql.append("   AND TMD.DEALER_CODE = '"+dealerCode+"'\n" );
		}
		sql.append("   AND TMD.DEALER_ID = TTSA.DEALER_ID\n" );
		if(!"".equals(accountType)&&accountType!=null){
			sql.append("   AND TSAT.TYPE_ID = "+accountType+"\n" );
		}
		sql.append("   AND TTSA.ACCOUNT_TYPE_ID = TSAT.TYPE_ID");
		List<Map<String, Object>> list= dao.pageQuery(sql.toString(), null, getFunName());
		if(list.size()>0){
			map = list.get(0);
		}
		return map;
	}
	
	/**
	 * Function         : 期末余额查询
	 * @param			: 经销商CODE
	 * @param			: 期末时间
	 * @param			: 账户类型
	 * @return          : 期末余额信息
	 * @throws          : Exception
	 * LastUpdate       : 2010-05-25
	 */
	public Map<String, Object> getOemEndAccount(String endDate,String dealerCode,String accountType)throws Exception{
		Map<String, Object> map = new  HashMap<String, Object>();
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT NVL(TTSA.BALANCE_AMOUNT,0) - NVL(TSACD.CHNG_AMOUNT, 0) AMOUNT2\n" );
		sql.append("  FROM TT_VS_ACCOUNT TTSA,\n" );
		sql.append("       TT_VS_ACCOUNT_TYPE TSAT,\n" );
		sql.append("       TM_DEALER TMD,\n" );
		sql.append("       (SELECT SUM(TSAC.CHNG_AMOUNT) CHNG_AMOUNT, TSAC.ACCOUNT_ID\n" );
		sql.append("          FROM TT_VS_ACCOUNT TSA, TT_VS_ACCOUNT_CHNG TSAC\n" );
		sql.append("         WHERE TSA.ACCOUNT_ID = TSAC.ACCOUNT_ID\n" );
		if(!"".equals(endDate)&&endDate!=null){
			sql.append("           AND TSAC.CHNG_DATE BETWEEN TO_DATE('"+endDate+" 00:00:00', 'YYYY-MM-DD HH24:MI:SS')+1 AND\n" );
			sql.append("               SYSDATE\n" );
		}
		sql.append("         GROUP BY TSAC.ACCOUNT_ID) TSACD\n" );
		sql.append(" WHERE TTSA.ACCOUNT_ID = TSACD.ACCOUNT_ID(+)\n" );
		if(!"".equals(dealerCode)&&dealerCode!=null){
			sql.append("   AND TMD.DEALER_CODE = '"+dealerCode+"'\n" );
		}
		sql.append("   AND TMD.DEALER_ID = TTSA.DEALER_ID\n" );
		if(!"".equals(accountType)&&accountType!=null){
			sql.append("   AND TSAT.TYPE_ID = "+accountType+"\n" );
		}
		sql.append("   AND TTSA.ACCOUNT_TYPE_ID = TSAT.TYPE_ID");
		List<Map<String, Object>> list= dao.pageQuery(sql.toString(), null, getFunName());
		if(list.size()>0){
			map = list.get(0);
		}
		return map;
	}
	
	/**
	 * Function         : 经销商查询
	 * @param			: 经销商CODE
	 * @return          : 经销商信息
	 * @throws          : Exception
	 * LastUpdate       : 2010-05-25
	 */
	public Map<String, Object> getDealerDetail(String dealerCode)throws Exception{
		Map<String, Object> map = new  HashMap<String, Object>();
		StringBuffer sql= new StringBuffer();
		sql.append("         select tmd.dealer_shortname dealerName\n" );
		sql.append("          from tm_dealer tmd\n" );
		sql.append("          where tmd.status= "+Constant.STATUS_ENABLE+"\n" );
		if (!"".equals(dealerCode)&&null!=dealerCode){
		    sql.append("      and tmd.dealer_code ='"+ dealerCode +"'\n");
		}
		List<Map<String, Object>> list= dao.pageQuery(sql.toString(), null, getFunName());
		if(list.size()>0){
			map = list.get(0);
		}
		return map;
	}
	
	/**
	 * Function         : 经销商查询
	 * @param			: 经销商Id
	 * @return          : 经销商信息
	 * @throws          : Exception
	 * LastUpdate       : 2010-05-25
	 */
	public Map<String, Object> getDealerMintain(String dealerId)throws Exception{
		Map<String, Object> map = new  HashMap<String, Object>();
		StringBuffer sql= new StringBuffer();
		sql.append("         select tmd.dealer_shortname dealerName,\n" );
		sql.append("          tmd.dealer_code dealerCode\n" );
		sql.append("          from tm_dealer tmd\n" );
		sql.append("          where tmd.status= "+Constant.STATUS_ENABLE+"\n" );
		if (!"".equals(dealerId)&&null!=dealerId){
		    sql.append("      and tmd.dealer_id in("+ dealerId +")\n");
		}
		List<Map<String, Object>> list= dao.pageQuery(sql.toString(), null, getFunName());
		if(list.size()>0){
			map = list.get(0);
		}
		return map;
	}
	/**
	 * Function         : DLR账户明细查询
	 * @param           : 开始时间
	 * @param           : 结束时间
	 * @param           : 经销商CODE
	 * @param           : 账户类型
	 * @param           : 操作类型
	 * @param           : 分页参数
	 * @return          : 经销商账户信息
	 * @throws          : Exception
	 * LastUpdate       : 2010-05-31
	 */
	public PageResult<Map<String, Object>> dlrAccountDetailQuery(String startDate,String endDate,String dealerId,String accountType,String changeType,Long oemCompanyId,int curPage,int pageSize) throws Exception{
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT TSAC.CHNG_ID,\n" );
		sql.append("       TO_CHAR(TSAC.CHNG_DATE,'YYYY-MM-DD') AS CHNG_DATE,\n" );
		sql.append("       TSAT.TYPE_NAME,\n" );
		sql.append("       TSA.ACCOUNT_NAME,\n" );
		sql.append("       TSAC.CHNG_TYPE,\n" );
		sql.append("       TSAC.ERP_DOC_NO,\n" );
		sql.append("       TSAC.GOLDEN_INVOICE_NO,\n" );
		sql.append("       TSAC.EXTERNAL_DOC_NO,\n" );
		sql.append("       TSAC.REMARK,\n" );
		sql.append("        MAX((CASE WHEN TSAC.CHNG_AMOUNT >0 THEN TSAC.CHNG_AMOUNT END ))AS CHANG_AMOUNT1,\n" );
		sql.append("       MAX((CASE WHEN TSAC.CHNG_AMOUNT <0 THEN TSAC.CHNG_AMOUNT END ))AS CHANG_AMOUNT2\n" );
		sql.append("  FROM TT_VS_ACCOUNT TSA, TT_VS_ACCOUNT_CHNG TSAC, TT_VS_ACCOUNT_TYPE TSAT,TM_DEALER TMD\n" );
		sql.append(" WHERE TSAC.ACCOUNT_ID = TSA.ACCOUNT_ID\n" );
		sql.append("   AND TSA.ACCOUNT_TYPE_ID = TSAT.TYPE_ID\n" );
		sql.append("   AND TSA.DEALER_ID = TMD.DEALER_ID\n" );
		sql.append("   AND TMD.OEM_COMPANY_ID ="+oemCompanyId+"\n" );
		if(!"".equals(startDate)&&startDate!=null&&!"".equals(endDate)&&endDate!=null){
			sql.append("   AND TSAC.CHNG_DATE BETWEEN TO_DATE('"+startDate+" 00:00:00','YYYY-MM-DD HH24:MI:SS')  AND TO_DATE('"+endDate+" 23:59:59','YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(!"".equals(dealerId)&&dealerId!=null){
			sql.append("   AND TMD.DEALER_ID IN("+dealerId+")\n");
		}
		if(!"-1".equals(accountType)&&!"".equals(accountType)&&accountType!=null){
			sql.append("   AND TSAT.TYPE_ID = "+accountType+"\n");
		}
		if(!"-1".equals(changeType)&&!"".equals(changeType)&&changeType!=null){
			sql.append("   AND TSAC.CHNG_TYPE = "+changeType+"\n");
		}
		sql.append("   GROUP BY TSAC.CHNG_ID,\n" );
		sql.append("       TSAC.CHNG_DATE,\n" );
		sql.append("       TSAT.TYPE_NAME,\n" );
		sql.append("       TSA.ACCOUNT_NAME,\n" );
		sql.append("       TSAC.CHNG_TYPE,\n" );
		sql.append("       TSAC.ERP_DOC_NO,\n" );
		sql.append("       TSAC.GOLDEN_INVOICE_NO,\n" );
		sql.append("       TSAC.EXTERNAL_DOC_NO,\n" );
		sql.append("       TSAC.REMARK\n" );
		sql.append("       ORDER BY CHNG_ID\n");
		PageResult<Map<String, Object>> rs = dao.pageQuery(sql.toString(), null, getFunName(),pageSize, curPage);
		return rs;
	}
	
	/**
	 * Function         : DLR账户明细查询--下载
	 * @param           : 开始时间
	 * @param           : 结束时间
	 * @param           : 经销商CODE
	 * @param           : 账户类型
	 * @param           : 操作类型
	 * @param           : 分页参数
	 * @return          : 经销商账户信息
	 * @throws          : Exception
	 * LastUpdate       : 2010-05-31
	 */
	public PageResult<Map<String, Object>> dlrAccountDetailDownLoad(String startDate,String endDate,String dealerId,String accountType,String changeType,Long oemCompanyId,int curPage,int pageSize) throws Exception{
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT TSAC.CHNG_ID,\n" );
		sql.append("       TO_CHAR(TSAC.CHNG_DATE,'YYYY-MM-DD') AS CHNG_DATE,\n" );
		sql.append("       TSAT.TYPE_NAME,\n" );
		sql.append("       TSA.ACCOUNT_NAME,\n" );
		sql.append("       TCC.CODE_DESC CHNG_TYPE,\n" );
		sql.append("       TSAC.ERP_DOC_NO,\n" );
		sql.append("       TSAC.GOLDEN_INVOICE_NO,\n" );
		sql.append("       TSAC.EXTERNAL_DOC_NO,\n" );
		sql.append("       TSAC.REMARK,\n" );
		sql.append("        MAX((CASE WHEN TSAC.CHNG_AMOUNT >0 THEN TSAC.CHNG_AMOUNT END ))AS CHANG_AMOUNT1,\n" );
		sql.append("       MAX((CASE WHEN TSAC.CHNG_AMOUNT <0 THEN TSAC.CHNG_AMOUNT END ))AS CHANG_AMOUNT2\n" );
		sql.append("  FROM TT_VS_ACCOUNT TSA, TT_VS_ACCOUNT_CHNG TSAC, TT_VS_ACCOUNT_TYPE TSAT,TM_DEALER TMD,TC_CODE TCC\n" );
		sql.append(" WHERE TSAC.ACCOUNT_ID = TSA.ACCOUNT_ID\n" );
		sql.append("   AND TSA.ACCOUNT_TYPE_ID = TSAT.TYPE_ID\n" );
		sql.append("   AND TSAC.CHNG_TYPE = TCC.CODE_ID\n" );
		sql.append("   AND TSA.DEALER_ID = TMD.DEALER_ID\n" );
		sql.append("   AND TMD.OEM_COMPANY_ID ="+oemCompanyId+"\n" );
		if(!"".equals(startDate)&&startDate!=null&&!"".equals(endDate)&&endDate!=null){
			sql.append("   AND TSAC.CHNG_DATE BETWEEN TO_DATE('"+startDate+" 00:00:00','YYYY-MM-DD HH24:MI:SS')  AND TO_DATE('"+endDate+" 23:59:59','YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(!"".equals(dealerId)&&dealerId!=null){
			sql.append("   AND TMD.DEALER_ID IN ("+dealerId+")\n");
		}
		if(!"-1".equals(accountType)&&!"".equals(accountType)&&accountType!=null){
			sql.append("   AND TSAT.TYPE_ID = "+accountType+"\n");
		}
		if(!"-1".equals(changeType)&&!"".equals(changeType)&&changeType!=null){
			sql.append("   AND TSAC.CHNG_TYPE = "+changeType+"\n");
		}
		sql.append("   GROUP BY TSAC.CHNG_ID,\n" );
		sql.append("       TSAC.CHNG_DATE,\n" );
		sql.append("       TSAT.TYPE_NAME,\n" );
		sql.append("       TSA.ACCOUNT_NAME,\n" );
		sql.append("       TCC.CODE_DESC,\n" );
		sql.append("       TSAC.ERP_DOC_NO,\n" );
		sql.append("       TSAC.GOLDEN_INVOICE_NO,\n" );
		sql.append("       TSAC.EXTERNAL_DOC_NO,\n" );
		sql.append("       TSAC.REMARK\n" );
		sql.append("       ORDER BY CHNG_ID\n");
		PageResult<Map<String, Object>> rs = dao.pageQuery(sql.toString(), null, getFunName(),pageSize, curPage);
		return rs;
	}
	/**
	 * Function         : 期初余额查询
	 * @param			: 经销商CODE
	 * @param			: 期初时间
	 * @param			: 账户类型
	 * @return          : 期初余额信息
	 * @throws          : Exception
	 * LastUpdate       : 2010-05-25
	 */
	public Map<String, Object> getDlrStartAccount(String startDate,String dealerId,String accountType,Long oemCompanyId)throws Exception{
		Map<String, Object> map = new  HashMap<String, Object>();
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT NVL(TTSA.BALANCE_AMOUNT,0) - NVL(TSACD.CHNG_AMOUNT, 0) AMOUNT1\n" );
		sql.append("  FROM TT_VS_ACCOUNT TTSA,\n" );
		sql.append("       TT_VS_ACCOUNT_TYPE TSAT,\n" );
		sql.append("       TM_DEALER TMD,\n" );
		sql.append("       (SELECT SUM(TSAC.CHNG_AMOUNT) CHNG_AMOUNT, TSAC.ACCOUNT_ID\n" );
		sql.append("          FROM TT_VS_ACCOUNT TSA, TT_VS_ACCOUNT_CHNG TSAC\n" );
		sql.append("         WHERE TSA.ACCOUNT_ID = TSAC.ACCOUNT_ID\n" );
		if(!"".equals(startDate)&&startDate!=null){
			sql.append("           AND TSAC.CHNG_DATE BETWEEN TO_DATE('"+startDate+" 00:00:00', 'YYYY-MM-DD HH24:MI:SS') AND\n" );
			sql.append("               SYSDATE\n" );
		}
		sql.append("         GROUP BY TSAC.ACCOUNT_ID) TSACD\n" );
		sql.append(" WHERE TTSA.ACCOUNT_ID = TSACD.ACCOUNT_ID(+)\n" );
		if(!"".equals(dealerId)&&dealerId!=null){
			sql.append("   AND TMD.DEALER_ID IN ("+dealerId+")\n" );
		}
		sql.append("   AND TMD.DEALER_ID = TTSA.DEALER_ID\n" );
		sql.append("   AND TMD.OEM_COMPANY_ID = "+oemCompanyId+"\n" );
		if(!"".equals(accountType)&&accountType!=null){
			sql.append("   AND TSAT.TYPE_ID = "+accountType+"\n" );
		}
		sql.append("   AND TTSA.ACCOUNT_TYPE_ID = TSAT.TYPE_ID");
		List<Map<String, Object>> list= dao.pageQuery(sql.toString(), null, getFunName());
		if(list.size()>0){
			map = list.get(0);
		}
		return map;
	}
	
	/**
	 * Function         : 期末余额查询
	 * @param			: 经销商CODE
	 * @param			: 期末时间
	 * @param			: 账户类型
	 * @return          : 期末余额信息
	 * @throws          : Exception
	 * LastUpdate       : 2010-05-25
	 */
	public Map<String, Object> getDlrEndAccount(String endDate,String dealerId,String accountType,Long oemCompanyId)throws Exception{
		Map<String, Object> map = new  HashMap<String, Object>();
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT NVL(TTSA.BALANCE_AMOUNT,0) - NVL(TSACD.CHNG_AMOUNT, 0) AMOUNT2\n" );
		sql.append("  FROM TT_VS_ACCOUNT TTSA,\n" );
		sql.append("       TT_VS_ACCOUNT_TYPE TSAT,\n" );
		sql.append("       TM_DEALER TMD,\n" );
		sql.append("       (SELECT SUM(TSAC.CHNG_AMOUNT) CHNG_AMOUNT, TSAC.ACCOUNT_ID\n" );
		sql.append("          FROM TT_VS_ACCOUNT TSA, TT_VS_ACCOUNT_CHNG TSAC\n" );
		sql.append("         WHERE TSA.ACCOUNT_ID = TSAC.ACCOUNT_ID\n" );
		if(!"".equals(endDate)&&endDate!=null){
			sql.append("           AND TSAC.CHNG_DATE BETWEEN TO_DATE('"+endDate+" 00:00:00', 'YYYY-MM-DD HH24:MI:SS')+1 AND\n" );
			sql.append("               SYSDATE\n" );
		}
		sql.append("         GROUP BY TSAC.ACCOUNT_ID) TSACD\n" );
		sql.append(" WHERE TTSA.ACCOUNT_ID = TSACD.ACCOUNT_ID(+)\n" );
		if(!"".equals(dealerId)&&dealerId!=null){
			sql.append("   AND TMD.DEALER_ID IN("+dealerId+")\n" );
		}
		sql.append("   AND TMD.DEALER_ID = TTSA.DEALER_ID\n" );
		sql.append("   AND TMD.OEM_COMPANY_ID = "+oemCompanyId+"\n" );
		if(!"".equals(accountType)&&accountType!=null){
			sql.append("   AND TSAT.TYPE_ID = "+accountType+"\n" );
		}
		sql.append("   AND TTSA.ACCOUNT_TYPE_ID = TSAT.TYPE_ID");
		List<Map<String, Object>> list= dao.pageQuery(sql.toString(), null, getFunName());
		if(list.size()>0){
			map = list.get(0);
		}
		return map;
	}
	
	
	/**
	 * Function         : OEM账户余额查询
	 * @param           : 经销商CODE
	 * @param           : 分页参数
	 * @return          : 经销商账户信息
	 * @throws          : Exception
	 * LastUpdate       : 2010-05-31
	 */
	public PageResult<Map<String, Object>> oemAccountBalanceQuery(String orgId, String areaIds,String areaId, String dealerCode,Long companyId,int curPage,int pageSize){
		
		List<Object> params = new LinkedList<Object>();
		
		List typeList = getAccountTypeByCompanyId(companyId);
		
		StringBuffer sql = new StringBuffer();
		
		sql.append("SELECT TEMP.DEALER_ID,\n");
		sql.append("       TEMP.DEALER_CODE,\n");  
		sql.append("       TEMP.DEALER_NAME,\n");  
		String sumStr = "";
		for(int i = 0; i < typeList.size(); i++){
			TtVsAccountTypePO po = (TtVsAccountTypePO)typeList.get(i);
			sumStr += "TEMP."+po.getTypeName();
			if(i != typeList.size() -1){
				sumStr += "+";
			}
			sql.append("       TEMP."+po.getTypeName()+",\n");  
		}
		sql.append("       "+sumStr+" KYHJ\n");  
		sql.append("  FROM (SELECT D.DEALER_ID,\n"); 
		int num = addCondition(sql,companyId); 
		sql.append("               D.DEALER_CODE,\n");  
		sql.append("               D.DEALER_NAME\n");  
		sql.append("          FROM TT_VS_ACCOUNT ACC, TT_VS_ACCOUNT_TYPE ATP, TM_DEALER D, VW_ORG_DEALER VOD,TM_DEALER_BUSINESS_AREA E\n");  
		sql.append("         WHERE ACC.DEALER_ID = D.DEALER_ID\n"); 
		sql.append("           AND E.DEALER_ID=D.DEALER_ID\n"); 
		sql.append("           AND D.DEALER_ID = VOD.DEALER_ID\n"); 
		sql.append("           AND ACC.ACCOUNT_TYPE_ID = ATP.TYPE_ID\n");  
		//sql.append("           AND D.DEALER_ID = A.DEALER_ID\n");
		sql.append("           AND ACC.COMPANY_ID = "+companyId+"\n");  
		if (!"".equals(areaId)) {
			sql.append("           AND E.AREA_ID = "+areaId+"\n");
		}
		if (!"".equals(areaIds) && "".equals(areaId)) {
			sql.append("           AND E.AREA_ID IN ("+areaIds+")\n");
		}
		if (!"".equals(dealerCode)&& null!=dealerCode){
			sql.append(Utility.getConSqlByParamForEqual(dealerCode, params, "D", "DEALER_CODE"));
		}
		if(!CommonUtils.isNullString(orgId)) {
			sql.append("           AND VOD.ROOT_ORG_ID IN ("+orgId+")\n");
		}
		sql.append("         GROUP BY D.DEALER_ID, D.DEALER_CODE, D.DEALER_NAME) TEMP");

		//动态列时，如果直接用getFunName当列数变化时，系统会报错，故传入getFunName()+num
		PageResult<Map<String, Object>> rs = dao.pageQuery(sql.toString(), params, getFunName()+num,pageSize, curPage);
		return rs;

	}
	
		
	/**
	 * 根据COMPANY_ID对应的账户类型动态生成SQL
	 * @param sql
	 * @param companyId
	 * @return
	 */
	private int addCondition(StringBuffer sql,Long companyId){
		List typeList = getAccountTypeByCompanyId(companyId);
		for(Iterator<TtVsAccountTypePO> itor = typeList.iterator();itor.hasNext();){
			TtVsAccountTypePO po = (TtVsAccountTypePO) itor.next();
			sql.append("   DECODE(MAX(DECODE(ACC.ACCOUNT_TYPE_ID, "+po.getTypeId()+", NVL(ACC.AVAILABLE_AMOUNT,0), 0)),0,MIN(DECODE(ACC.ACCOUNT_TYPE_ID, "+po.getTypeId()+", NVL(ACC.AVAILABLE_AMOUNT,0), 0)),MAX(DECODE(ACC.ACCOUNT_TYPE_ID, "+po.getTypeId()+", NVL(ACC.AVAILABLE_AMOUNT,0), 0))) AS "+po.getTypeName()+",\n"); //YH 2011.2.13
		}
		if(typeList != null){
			return typeList.size();
		}
		
		return 0;
		
	}
	
	/**
	 * 通过COMPANY_ID得到对应的账户类型
	 * @param companyId
	 * @return
	 */
	public List<TtVsAccountTypePO> getAccountTypeByCompanyId(Long companyId){
		TtVsAccountTypePO queryPo  = new TtVsAccountTypePO();
		queryPo.setCompanyId(companyId);
		List<TtVsAccountTypePO> list = select(queryPo);
		return list;
	}
	
	
	//---------------------------------------资金账户公用方法--------------------------------------------
	//CREATE_BY Wangliang @100913
	

	
	/**
	 * 通过经销商ID取得<b>非折扣</b>账户信息
	 * @param dealerId
	 * @return
	 */
	public List<Map<String,Object>> getNoDiscountAccountInfoByDealerId(String dealerId){

		StringBuffer sql = new StringBuffer();
		sql.append("SELECT ATP.TYPE_CODE,\n");
		sql.append("       ATP.TYPE_NAME,\n");  
		sql.append("       ATP.TYPE_ID,\n"); 
		sql.append("       A.ACCOUNT_ID,\n");
		sql.append("       NVL(A.BALANCE_AMOUNT,0) BALANCE_AMOUNT,\n");  
		sql.append("       NVL(A.FREEZE_AMOUNT,0) FREEZE_AMOUNT,\n");  
		sql.append("       NVL(A.AVAILABLE_AMOUNT,0) AVAILABLE_AMOUNT,\n");  
		sql.append("       NVL(ATP.IS_USE_ORDER_ACCOUNT,0) IS_USE_ORDER_ACCOUNT\n");
		sql.append("  FROM TT_VS_ACCOUNT A, TT_VS_ACCOUNT_TYPE ATP\n");  
		sql.append(" WHERE A.ACCOUNT_TYPE_ID = ATP.TYPE_ID\n");
		//sql.append("   AND ATP.IS_DISCOUNT = 0\n");
		sql.append("   AND ATP.TYPE_CLASS IS NOT NULL\n");
		sql.append("   AND ATP.TYPE_CODE NOT IN ('2001')\n");
		sql.append("   AND a.DEALER_ID = "+dealerId);
		
		List<Map<String,Object>> list = dao.pageQuery(sql.toString(), null, getFunName());
		return list;

	}
	
	/**
	 * 通过经销商ID取得<b>折扣</b>账户信息
	 * @param dealerId
	 * @return
	 */
	public List<Map<String,Object>> getDiscountAccountInfoByDealerId(String dealerId){

		StringBuffer sql = new StringBuffer();
		sql.append("SELECT ATP.TYPE_CODE,\n");
		sql.append("       ATP.TYPE_NAME,\n"); 
		sql.append("       ATP.TYPE_ID,\n");
		sql.append("       A.ACCOUNT_ID,\n");
		sql.append("       NVL(A.BALANCE_AMOUNT,0) BALANCE_AMOUNT,\n");  
		sql.append("       NVL(A.FREEZE_AMOUNT,0) FREEZE_AMOUNT,\n");  
		sql.append("       NVL(A.AVAILABLE_AMOUNT,0) AVAILABLE_AMOUNT,\n");   
		sql.append("       DECODE(ATP.IS_USE_ORDER_ACCOUNT, NULL, 0, 1) IS_USE_ORDER_ACCOUNT\n");
		sql.append("  FROM TT_VS_ACCOUNT A, TT_VS_ACCOUNT_TYPE ATP\n");  
		sql.append(" WHERE A.ACCOUNT_TYPE_ID = ATP.TYPE_ID\n");
		sql.append("   AND ATP.IS_DISCOUNT = 1\n");
		sql.append("   AND ATP.TYPE_CLASS IS NOT NULL\n");
		sql.append("   AND a.DEALER_ID = "+dealerId);
		
		List<Map<String,Object>> list = dao.pageQuery(sql.toString(), null, getFunName());
		return list;

	}
	
	/**
	 * 通过经销商ID取得<b>全部</b>账户信息
	 * @param dealerId
	 * @return
	 */
	public List<Map<String,Object>> getAllAccountInfoByDealerId(String dealerId){

		StringBuffer sql = new StringBuffer();
		sql.append("SELECT ATP.TYPE_CODE,\n");
		sql.append("       ATP.TYPE_NAME,\n"); 
		sql.append("       ATP.TYPE_ID,\n");
		sql.append("       ATP.IS_DISCOUNT,\n");
		sql.append("       A.ACCOUNT_ID,\n");
		sql.append("       NVL(A.BALANCE_AMOUNT,0) BALANCE_AMOUNT,\n");  
		sql.append("       NVL(A.FREEZE_AMOUNT,0) FREEZE_AMOUNT,\n");  
		sql.append("       NVL(A.AVAILABLE_AMOUNT,0) AVAILABLE_AMOUNT\n");   
		sql.append("  FROM TT_VS_ACCOUNT A, TT_VS_ACCOUNT_TYPE ATP\n");  
		sql.append(" WHERE A.ACCOUNT_TYPE_ID = ATP.TYPE_ID\n");
		sql.append("   AND ATP.TYPE_CLASS IS NOT NULL\n");
		sql.append("   AND a.DEALER_ID = "+dealerId);
		
		List<Map<String,Object>> list = dao.pageQuery(sql.toString(), null, getFunName());
		return list;

	}
	
	/**
	 * 根据发运申请得到该发运申请被冻结的资金记录
	 * @param reqId
	 * @return
	 */
	public List<TtVsAccountFreezePO> getFreezeRecordsByReqId(String reqId){
		return getFreezedRecordsByReqId(reqId);
	}
	
	/**
	 * 根据账户Id得到该账户被冻结的资金记录
	 * @param accountId
	 * @return
	 */
	public List<TtVsAccountFreezePO> getFreezeRecordsByAccountId(String accountId){
		TtVsAccountFreezePO queryPO = new TtVsAccountFreezePO();
		queryPO.setAccountId(Long.valueOf(accountId));
		queryPO.setStatus(Constant.ACCOUNT_FREEZE_STATUS_01);
		List<TtVsAccountFreezePO> list = select(queryPO);

		return list;
	}
	
	
	/**
	 * 传入<b>发运申请 ID</b>将释放此发运申请之前冻结的金额
	 * 如果使用了折让也将会释放折让金额
	 * 同时将原冻结记录置为"已释放"
	 * ！销售订单调整确认选择取消时需调用此方法
	 * @param reqId 发运申请 ID
	 * @param userId 当前操作用户ID
	 */
	public void releaseAllFreezeAmountByReqId(String reqId,String userId){
		List<TtVsAccountFreezePO> freezeRecords = getFreezedRecordsByReqId(reqId);
		
		if(freezeRecords != null){
			for(Iterator<TtVsAccountFreezePO> itor = freezeRecords.iterator();itor.hasNext();){
				TtVsAccountFreezePO tmpPo = (TtVsAccountFreezePO) itor.next();
				//释放原冻结金额,认为新的冻结金额为"0"
				syncAccountFreeze(reqId,tmpPo.getAccountId().toString(),new BigDecimal(0),userId,true);
			}
		} 
	}
	
	/**
	 * 当账户金额变化时，同步账户冻结记录及对应的账户可用余额等信息
	 * 当某次业务操作后，有N个账户发生了变化，需要调用此方法N次<br>
	 * ！当发运申请生成后，根据系统对资金的校验参数设置，来判断是否调用此方法<br>
	 * ！如参数设置为审核时校验资金，那么将在订单资源审核处调用此方法<br>
	 * ！如参数设置为提报时校验资金，那么要在补充订单提报，常规订单发运申请，销售订单预审，订单资源审核时调用此方法<br>
	 * @param reqId  发运申请ID
	 * @param accountId 账户ID
	 * @param freezeAmount 新的冻结金额
	 * @param userId 当前操作用户ID
	 */
	public void syncAccountFreeze(String reqId,String accountId,BigDecimal freezeAmount,String userId){
		syncAccountFreeze(reqId,accountId,freezeAmount,userId,false);
	}
	
	//-------------------------------------------private Method----------------------------------
	
	/**
	 * 当冻结发生变化时，同步账户冻结记录及对应的账户可用余额
	 * <br>
	 * 1.根据所传入的<b>reqId</b>来查找是否已经有冻结记录
	 *   1.1 不存在-->新增冻结记录，扣除对应账户的available_amout,增加对应账户的freeze_amount
	 *   1.2 存在     -->判断之前已冻结记录的账户是否包含本次所传入的冻结账户
	 *       1.2.1 包含     -->根据本次冻结金额  与 原冻结金额 的大小情况，来释放或扣除 差异部分的金额，更新原冻结记录
	 *       1.2.2 不包含-->判断此次的账户是否为“折扣账户”
	 *             1.2.2.1 是折扣账户      -->新增一条折扣账户冻结记录，冻结折扣账户相应金额
	 *             1.2.2.2 不是折扣账户 -->将原账户所冻结的金额全部释放，并更新原冻结记录，并冻结新账户的资金
	 * 
	 * @param reqId  发运申请ID
	 * @param accountId 账户ID
	 * @param freezeAmount 新的冻结金额
	 * @param userId 当请操作用户
	 * @param isPayAll 是否要释放全部金额(true将更新TT_VS_ACCOUNT_FREEZE.STATUS 为已释放)
	 */
	private void syncAccountFreeze(String reqId,String accountId,BigDecimal freezeAmount, String userId,boolean isPayAll){
		if(StringUtil.isNull(reqId) || StringUtil.isNull(accountId) || freezeAmount == null || freezeAmount.intValue() < 0 || StringUtil.isNull(userId)) {
			throw new RuntimeException ("调用更新账户方法参数错误！");
		}
		TtVsAccountPO accountPo = geTtVsAccountPOById(Long.valueOf(accountId));
		TtVsDlvryReqPO dlvryReqPo = geTtVsDlvryReqPOById(Long.valueOf(reqId));
		
		if(null == accountPo){
			throw new RuntimeException("此账户ID: " + accountId + " 对应的账户记录不存在！");
		}
		
		if(null == dlvryReqPo){
			throw new RuntimeException("此申请ID：" + reqId + "对应的发运申请记录不存在！");
		}
		
		String dealerId = null;
		String companyId = null;
		
		//记录日志所需字段
		Double oldAvaillableAmount = accountPo.getAvailableAmount();
		Double oldFreezeAmount = accountPo.getFreezeAmount();
		Long operateGroupId = Long.valueOf(SequenceManager.getSequence(null));
		
		Map dlrMap = getDealerIdAndCompanyIdByAccountId(accountId);
		if(dlrMap != null){
			dealerId = ((BigDecimal) dlrMap.get("DEALER_ID")).toString();
			companyId = ((BigDecimal) dlrMap.get("COMPANY_ID")).toString();
		}else{
			throw new RuntimeException("根据此发运申请ID找不到对应的经销商ID或公司ID!");
		}
		
		freezeAccountForUpdate(accountId);//锁定资金账户
		
		List<TtVsAccountFreezePO> freezeRecords = getFreezedRecordsByReqId(reqId, dealerId);
		//1.1 不存在-->新增冻结记录，扣除对应账户的available_amout,增加对应账户的freeze_amount
		if(freezeRecords == null) {
			Long freezeId = newFreezeRecord(reqId,accountId,freezeAmount,Long.valueOf(userId), Long.valueOf(dealerId),Long.valueOf(companyId));
			updateAccountAmount(freezeAmount.doubleValue(), 0,accountPo,userId);//认为原冻结额为'0'
			newAccountLog(freezeId,userId,accountPo,oldFreezeAmount,oldAvaillableAmount,freezeAmount.doubleValue(),operateGroupId);
		}else{
			TtVsAccountFreezePO comparePo = new TtVsAccountFreezePO();
			comparePo.setAccountId(Long.valueOf(accountId));
			comparePo.setReqId(Long.valueOf(reqId));

			TtVsAccountFreezePO existsFreeze = getExistFreeze(freezeRecords,comparePo);
			//1.2.1 包含     -->根据本次冻结金额  与 原冻结金额 的大小情况，来释放或扣除 差异部分的金额，更新原冻结记录
			if(existsFreeze != null){
				double freezeAmountOld  = existsFreeze.getFreezeAmount();
				updateAccountAmount(freezeAmount.doubleValue(),freezeAmountOld, accountPo,userId);
				updateFreezeRecord(freezeAmount, Long.valueOf(userId), existsFreeze ,null,isPayAll);
				newAccountLog(existsFreeze.getFreezeId(),userId,accountPo,oldFreezeAmount,oldAvaillableAmount,freezeAmount.doubleValue(),operateGroupId);
			} else{	
				//1.2.2.1 是折扣账户      -->新增一条折扣账户冻结记录，冻结折扣账户相应金额
				if(isDiscountAccount(accountId)){
					Long freezeId = newFreezeRecord(reqId,accountId,freezeAmount,Long.valueOf(userId), Long.valueOf(dealerId),Long.valueOf(companyId));
					updateAccountAmount(freezeAmount.doubleValue(), 0,accountPo,userId);//认为原冻结额为'0'
					newAccountLog(freezeId,userId,accountPo,oldFreezeAmount,oldAvaillableAmount,freezeAmount.doubleValue(),operateGroupId);
				}else{
				//1.2.2.2 不是折扣账户 -->将原账户所冻结的金额全部释放，并更新原冻结记录，并冻结新账户的资金
					Long freezeId = releaseNoDiscountAccountFreezeByReqId(reqId,userId,operateGroupId);					
						
					if(freezeId != null){
						
						TtVsAccountFreezePO tmpo = new TtVsAccountFreezePO();
						tmpo.setFreezeId(Long.valueOf(freezeId));
						TtVsAccountFreezePO existsFreezeOld = getExistFreeze(freezeRecords,tmpo);
						updateFreezeRecord(freezeAmount, Long.valueOf(userId), existsFreezeOld, Long.valueOf(accountId),isPayAll);
						updateAccountAmount(freezeAmount.doubleValue(), 0,accountPo,userId);
						newAccountLog(freezeId,userId,accountPo,oldFreezeAmount,oldAvaillableAmount,freezeAmount.doubleValue(),operateGroupId);
						
					}
				}
			}
			
		}
		
		
	}
	
	/**
	 * 在操作账户前,将此账户记录锁住,用来避免发生并发时资金操作错误
	 * @param accountId
	 */
	private void freezeAccountForUpdate(String accountId){
		StringBuffer sql = new StringBuffer();
		sql.append("select * from tt_vs_account va where va.account_id = "+ accountId +" for update");
		List<Map<String,Object>> list = dao.pageQuery(sql.toString(), null, getFunName());
	}

	/**
	 * 更新账户冻结记录
	 * @param freezeAmount
	 * @param userId
	 * @param existsFreeze
	 * @param accountId //传入参数为空时冻结账户不变
	 */
	private void updateFreezeRecord(BigDecimal freezeAmount, Long userId,
			TtVsAccountFreezePO existsFreeze,Long accountId,boolean isPay) {
		existsFreeze.setFreezeAmount(freezeAmount.doubleValue());
		existsFreeze.setUpdateDate(new Date());
		existsFreeze.setUpdateBy(userId);
		if(accountId != null){//传入参数为空时冻结账户不变
			existsFreeze.setAccountId(accountId);
		}
		if(isPay && freezeAmount.intValue() == 0){//要还款时，将状态置为已释放
			existsFreeze.setStatus(Constant.ACCOUNT_FREEZE_STATUS_02);
		}
		TtVsAccountFreezePO queryPo = new TtVsAccountFreezePO();
		queryPo.setFreezeId(existsFreeze.getFreezeId());
		update(queryPo, existsFreeze);
	}

	/**
	 * 更新账户冻结信息
	 * 新的冻结金额与老的冻结金额都是针对某一笔冻结(FREEZE_ID)而言的，
	 * 并不是指整个账户的冻结金额
	 * @param newFreezeAmount 新的冻结数额
	 * @param oldFreezeAmount 老的冻结数额
	 * @param accountPo 已经经过select的PO
	 */
	private void updateAccountAmount(double newFreezeAmount,double oldFreezeAmount,
			TtVsAccountPO accountPo,String userId) {
		
		double changeAmount = newFreezeAmount - oldFreezeAmount;
		logger.info("accountId:" + accountPo.getAccountId() +"," +
				"变化金额："+changeAmount+"," +
						"原冻结金额："+accountPo.getFreezeAmount()+"," +
								"原可用额度"+accountPo.getAvailableAmount());
		accountPo.setFreezeAmount(add(accountPo.getFreezeAmount(),changeAmount,2));
		accountPo.setAvailableAmount(subtract(accountPo.getAvailableAmount(),changeAmount,2));
		
		Double availableAmount = new Double(0) ;
		availableAmount = accountPo.getAvailableAmount() ;
		
		if(UrgentOrderReport.chkBinCai(null, accountPo.getAccountId().toString())) {	// 新增对兵财帐户资金扣款时验证
			/**++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		     * 描述:银翔原型报错修复
		     * 发运申请相关
		     * urgentDlvryReq 第三参数“new OrderReportDao()” --> “OrderReportDao.getInstance()”
		     * Date:2017-06-29
		     */
			OrderReportDao ord = OrderReportDao.getInstance() ;
			Map<String, Object> map = ord.getBinCaiAvailable(accountPo.getDealerId().toString(), accountPo.getAccountTypeId()) ;
			availableAmount = Double.parseDouble(map.get("AVAILABLE_AMOUNT").toString()) ;
		}
		
		Long dlrId = accountPo.getDealerId() ;
		TmDealerPO td = new TmDealerPO() ;
		td.setDealerId(dlrId) ;
		
		td = (TmDealerPO)dao.select(td).get(0) ;
		
		if(!("-" + Constant.STATUS_DISABLE).equals(td.getErpCode())){
			if(availableAmount < 0 && changeAmount > 0){
				//增加只有新的冻结额度大于原冻结额度才判断账户余额的逻辑,
				//来解决资金余额为负时，不能取消订单的情况@101030
				throw new RuntimeException("账户没有足够的额度！");
			}
		}
		accountPo.setUpdateBy(Long.valueOf(userId));
		accountPo.setCreateDate(new Date());
		TtVsAccountPO accountQueryPO = new TtVsAccountPO();		
		accountQueryPO.setAccountId(Long.valueOf(accountPo.getAccountId()));
		update(accountQueryPO, accountPo);
		logger.info("更新成功！accountId:" + accountPo.getAccountId() +"," +
				"变化金额："+changeAmount+"," +
						"新冻结金额："+accountPo.getFreezeAmount()+"," +
								"新可用额度"+accountPo.getAvailableAmount());
	}
	
	
	/**
	 * 得到传入发运申请ID所对应的全部冻结历史
	 * @param reqId
	 * @return
	 */
	public List<TtVsAccountFreezePO> getFreezedRecordsByReqId(String reqId){
		TtVsAccountFreezePO queryPO = new TtVsAccountFreezePO();
		queryPO.setReqId(Long.valueOf(reqId));
		queryPO.setStatus(Constant.ACCOUNT_FREEZE_STATUS_01);
		List<TtVsAccountFreezePO> list = select(queryPO);
		
		if(list == null || list.size() < 1){
			return null;
		}
		return list;
	}
	
	/**
	 * 得到传入发运申请ID所对应的全部冻结历史
	 * @param reqId
	 * @return
	 */
	public List<TtVsAccountFreezePO> getFreezedRecordsByReqId(String reqId, String dealerId){
		TtVsAccountFreezePO queryPO = new TtVsAccountFreezePO();
		queryPO.setReqId(Long.valueOf(reqId));
		queryPO.setDealerId(Long.parseLong(dealerId)) ;
		queryPO.setStatus(Constant.ACCOUNT_FREEZE_STATUS_01);
		List<TtVsAccountFreezePO> list = select(queryPO);
		
		if(list == null || list.size() < 1){
			return null;
		}
		return list;
	}
	
	/**
	 * 从缓存在List中得到与传入参数中freeze_id相同或(account_id 与  req_id相同)的PO
	 * @param freezeRecords
	 * @param po
	 * @return
	 */
	private TtVsAccountFreezePO getExistFreeze(List<TtVsAccountFreezePO> freezeRecords,TtVsAccountFreezePO po){
		for(Iterator<TtVsAccountFreezePO> itor = freezeRecords.iterator();itor.hasNext();){
			TtVsAccountFreezePO existPo = itor.next();
			if(existPo.equals(po)){
				return existPo;
			}
		}
		
		return null;
	}
	
	/**
	 * 新增账户冻结记录 (ONLY insert)
	 * 
	 * @param reqId
	 * @param accountId
	 * @param freezeAmount
	 */
	private Long newFreezeRecord(String reqId, String accountId,
			BigDecimal freezeAmount, Long userId, Long dealerId, Long companyId) {
		
		if(freezeAmount.intValue() > 0){
			//只有当新增的冻结金额大于零时，才生成新的记录	
			Long freezeId = Long.valueOf(SequenceManager.getSequence(null));	
			TtVsAccountFreezePO insertPo = new TtVsAccountFreezePO();
			insertPo.setFreezeId(freezeId);
			insertPo.setAccountId(Long.valueOf(accountId));
			insertPo.setFreezeAmount(freezeAmount.doubleValue());
			insertPo.setCompanyId(companyId);
			insertPo.setDealerId(dealerId);
			insertPo.setFreezeDate(new Date());
			insertPo.setReqId(Long.valueOf(reqId));
			insertPo.setStatus(Constant.ACCOUNT_FREEZE_STATUS_01);
			insertPo.setCreateBy(userId);
			insertPo.setCreateDate(new Date());
			
			if(UrgentOrderReport.chkBinCai(null, accountId)) {
				insertPo.setIsCsgc(0) ;	// 0表示冻结资金为兵财
			} else {
				insertPo.setIsCsgc(1) ;	// 1表示冻结资金为非兵财
			}

			insertPo.setErpCode(OrderReportDao.getOrderDlrErpCode(reqId)) ;	// 通过发运申请id查询订单采购方erpcode
	
			insert(insertPo);
			
			
			return freezeId;
			
		}
		return null;
	}
	
	
	/**
	 * 新增账户变化记录日志信息
	 * @param freezeId
	 * @param userId
	 * @param nPO
	 * @param oFreezeAmount
	 * @param oAvailableAmount
	 * @param newFreezeAmount
	 * @param operateGroupId
	 */
	private void newAccountLog(Long freezeId,String userId, 
			TtVsAccountPO nPO,Double oFreezeAmount,Double oAvailableAmount,
				Double newFreezeAmount,Long operateGroupId) {
		
		if(freezeId != null) {
			TtVsAccountLogPO newPo = new TtVsAccountLogPO();
			newPo.setLogId(Long.valueOf(SequenceManager.getSequence(null)));
			newPo.setAccountId(nPO.getAccountId());
			newPo.setFreezeId(freezeId);
			newPo.setnAvailableAmount(nPO.getAvailableAmount());
			newPo.setnFreezeAmount(nPO.getFreezeAmount());
			newPo.setoAvailableAmount(oAvailableAmount);
			newPo.setoFreezeAmount(oFreezeAmount);
			newPo.settFreezeAmount(newFreezeAmount);//本次冻结金额
			newPo.setUserId(Long.valueOf(userId));
			newPo.setOpdate(new Date());
			newPo.setOperateGroupId(operateGroupId);
			newPo.calculateChangeAmount();//计算变化数量
			insert(newPo);
		}
	}
	
	private String returnStringValue(Object o){
		if(o != null){
			return o.toString();
		}else{
			return null;
		}
	
	}
	
	
	private TtVsAccountPO geTtVsAccountPOById(Long accountId) {
		TtVsAccountPO po = new TtVsAccountPO();
		po.setAccountId(accountId);
		List<PO> list = select(po);
		return list.size() != 0 ? (TtVsAccountPO) list.get(0) : null;
	}
	
	private TtVsDlvryReqPO geTtVsDlvryReqPOById(Long reqId) {
		TtVsDlvryReqPO po = new TtVsDlvryReqPO();
		po.setReqId(reqId);
		List<PO> list = select(po);
		return list.size() != 0 ? (TtVsDlvryReqPO) list.get(0) : null;
	}
		
	
	/**
	 * 判断该账户是否为折扣账户
	 * @param accountId
	 * @return
	 */
	private boolean isDiscountAccount(String accountId){

		StringBuffer sql = new StringBuffer();
		sql.append("SELECT ATP.IS_DISCOUNT\n");
		sql.append("  FROM TT_VS_ACCOUNT A, TT_VS_ACCOUNT_TYPE ATP\n");  
		sql.append(" WHERE A.ACCOUNT_TYPE_ID = ATP.TYPE_ID\n");  
		sql.append("   AND A.ACCOUNT_ID = " + accountId);
		List<Map<String,Object>> list = dao.pageQuery(sql.toString(), null, getFunName());
		
		//调用此方法前已判断账户是否为空,故此处无需判断是否为空
		Map record = (Map) list.get(0);
		BigDecimal isDiscount = (BigDecimal)record.get("IS_DISCOUNT");
		if(isDiscount.intValue() == 1){
			return true;
		}else{
			return false;
		}	

	}
	
	/**
	 * 得到传入发运申请ID所对应的非折扣账户
	 * @param reqId
	 * @return
	 */
	private List getFreezedNoDiscountAccountByReqId(String reqId){

		StringBuffer sql = new StringBuffer();
		sql.append("SELECT AF.FREEZE_ID,\n");
		sql.append("       AF.ACCOUNT_ID,\n");  
		sql.append("       AF.FREEZE_AMOUNT,\n");  
		sql.append("       A.FREEZE_AMOUNT TOTAL_FREEZE_AMOUNT,\n");  
		sql.append("       A.AVAILABLE_AMOUNT\n");  
		sql.append("  FROM TT_VS_ACCOUNT_FREEZE AF, TT_VS_ACCOUNT A, TT_VS_ACCOUNT_TYPE ATP\n");  
		sql.append(" WHERE AF.ACCOUNT_ID = A.ACCOUNT_ID\n");  
		sql.append("   AND ATP.TYPE_ID = A.ACCOUNT_TYPE_ID\n");  
		sql.append("   AND ATP.IS_DISCOUNT = 0\n");  
		sql.append("   AND AF.REQ_ID =" + reqId);
		
		List<Map<String,Object>> list = dao.pageQuery(sql.toString(), null, getFunName());
		if(list != null && list.size() >0){
			return list;
		}
		return null;
	}
	
	/**
	 * 将发运申请ID所对应的非折扣账户之前全部的冻结金额全部释放
	 * @param reqId
	 * @return
	 */
	private Long releaseNoDiscountAccountFreezeByReqId(String reqId,String userId,Long operateGroupId){
		List list  = getFreezedNoDiscountAccountByReqId(reqId);
		if(list != null){
			Map record = (Map) list.get(0);//一个REQ_ID只可能对应一个非折扣账户的冻结金额
			Long accountId = ((BigDecimal) record.get("ACCOUNT_ID")).longValue();
			double freezedAmount = ((BigDecimal)record.get("FREEZE_AMOUNT")).doubleValue();	
			Long freezeId = ((BigDecimal) record.get("FREEZE_ID")).longValue();
			TtVsAccountPO ap = geTtVsAccountPOById(accountId);
			Double oldFreezeAmount = ap.getFreezeAmount();
			Double oldAvaillableAmount = ap.getAvailableAmount();
			
			updateAccountAmount(0,freezedAmount,ap,userId);//释放原账户全部冻结金额
			
			newAccountLog(freezeId,userId,ap,oldFreezeAmount,oldAvaillableAmount,new Double(0),operateGroupId);
			return freezeId;
		}
		logger.error("release freeze records error!");
		return null;
	}
	
	/**
	 * 根据ReqId得到经销商ID(DEALER_ID)和车厂公司ID(COMPANY_ID)所组成的<code>Map</code>
	 * @param reqId
	 * @return
	 */
	private Map getDealerIdAndCompanyIdByReqId(String reqId){

		StringBuffer sql = new StringBuffer();
		sql.append("SELECT O.BILLING_ORG_ID DEALER_ID, O.COMPANY_ID\n");  
		sql.append("  FROM TT_VS_DLVRY_REQ RQ, TT_VS_ORDER O\n");  
		sql.append(" WHERE RQ.ORDER_ID = O.ORDER_ID\n");  
		sql.append("   AND RQ.REQ_ID ="+ reqId);
		
		List<Map<String,Object>> list = dao.pageQuery(sql.toString(), null, getFunName());
		if(list != null && list.size() >0){
			return list.get(0);
		}
		
		return null;

	}
	
	/**
	 * 根据AccountId得到经销商ID(DEALER_ID)和车厂公司ID(COMPANY_ID)所组成的<code>Map</code>
	 * @param reqId
	 * @return
	 */
	private Map getDealerIdAndCompanyIdByAccountId(String accountId){

		StringBuffer sql = new StringBuffer("\n");
		sql.append("select tmd.dealer_id, tmd.oem_company_id company_id\n");
		sql.append("  from tm_dealer tmd, tt_vs_account tva\n");  
		sql.append(" where tmd.dealer_id = tva.dealer_id\n");  
		sql.append("   and tva.account_id = ").append(accountId).append("\n");

		
		List<Map<String,Object>> list = dao.pageQuery(sql.toString(), null, getFunName());
		if(list != null && list.size() >0){
			return list.get(0);
		}
		
		return null;

	}
	
	
	// -----------------------------------------商业用java浮点数精度计算------------------------------------------------
	/**
	 * 提供精确的乘法运算
	 * 
	 * @param v1
	 * @param v2
	 * @param scale
	 *            :保留精度，BigDecimal.ROUND_HALF_EVEN：财务常用的四舍六入五取偶，BigDecimal.ROUND_HALF_UP
	 *            ，包括>=.5就进位，比如3.455保留两位为3.46
	 * @return 两个参数的积
	 */
	public static double multiply(double v1, double v2, int scale) {
		return (BigDecimal.valueOf(v1).multiply(BigDecimal.valueOf(v2)))
				.setScale(scale, BigDecimal.ROUND_HALF_EVEN).doubleValue();
	}

	/**
	 * 提供精确的加法运算
	 * 
	 * @param v1
	 * @param v2
	 * @param scale
	 *            :保留精度
	 * @return 两个参数的和
	 */
	public static double add(double v1, double v2, int scale) {
		return (BigDecimal.valueOf(v1).add(BigDecimal.valueOf(v2))).setScale(
				scale, BigDecimal.ROUND_HALF_EVEN).doubleValue();
	}

	/**
	 * 提供精确的减法运算
	 * 
	 * @param v1
	 * @param v2
	 * @param scale
	 *            :保留精度
	 * @return 两个参数的差
	 */
	public static double subtract(double v1, double v2, int scale) {
		return (BigDecimal.valueOf(v1).subtract(BigDecimal.valueOf(v2)))
				.setScale(scale, BigDecimal.ROUND_HALF_EVEN).doubleValue();
	}
	
	
	/**
	 * 更新TT_VS_ACCOUNT_FREEZE：冻结
	 * @param v1 String accountId 账户ID
	 * @param v2 String buzId 需求ID
	 * @param v3 String freeze_amount 金额
	 * @param v4 String userId 登录用户的userId
	 * @param v5 int useType 定做车预扣款
	 * */
	public void accountFreeze(String accountId, String buzId, String freezeAmount, String userId, int useType){
		commonUpdateAccountFreeze(accountId, buzId, freezeAmount, userId, useType, Constant.ACCOUNT_FREEZE_STATUS_01);
	}
	/**
	 * 更新TT_VS_ACCOUNT_FREEZE：释放
	 * @param v1 String accountId 账户ID
	 * @param v2 String buzId 需求ID
	 * @param v3 String freeze_amount 金额
	 * @param v4 String userId 登录用户的userId
	 * @param v5 int useType 定做车预扣款
	 * */
	public void accountRelease(String accountId, String buzId, String freezeAmount, String userId, int useType){
		commonUpdateAccountFreeze(accountId, buzId, freezeAmount, userId, useType, Constant.ACCOUNT_FREEZE_STATUS_02);
	}
	/**
	 * 更新TT_VS_ACCOUNT_FREEZE共用方法
	 * @param v1 String accountId 账户ID
	 * @param v2 String buzId 需求ID
	 * @param v3 String freeze_amount 金额
	 * @param v4 String userId 登录用户的userId
	 * @param v5 int useType 定做车预扣款
	 * @param v6 int accountFreezeStatus 账户冻结状态
	 * */
	public void commonUpdateAccountFreeze(String accountId, String buzId, String freezeAmount, String userId, int useType, int accountFreezeStatus){
		TtVsAccountFreezePO accountFreezePO = new TtVsAccountFreezePO();
		String path_ = "com.infodms.dms.dao.sales.financemanage.AccountBalanceDetailDao.commonUpdateAccountFreeze()：";
		if(null == accountId || "".equals(accountId)){
			throw new RuntimeException (path_+"账户ID不能为空！");
		}
		if(null == buzId || "".equals(buzId)){
			throw new RuntimeException (path_+"需求ID不能为空！");
		}
		if(0 == useType){
			throw new RuntimeException (path_+"使用冻结类型不能为空！");
		}
		
		accountFreezePO.setAccountId(Long.parseLong(accountId));
		accountFreezePO.setUseType(useType);
		accountFreezePO.setBuzId(Long.parseLong(buzId));
		
		List<PO> fList = dao.select(accountFreezePO);
		//如果有记录则update，否则insert
		if (null != fList && fList.size()>0) {
			TtVsAccountFreezePO valuePO = new TtVsAccountFreezePO();
			//如果修改冻结金额
			if (accountFreezeStatus == Constant.ACCOUNT_FREEZE_STATUS_01) {
				valuePO.setFreezeAmount(Double.parseDouble(freezeAmount));
			}else{
				valuePO.setPayAmount(Double.parseDouble(freezeAmount));
				valuePO.setPayDate(new Date());
			}
			valuePO.setStatus(accountFreezeStatus);
			valuePO.setUpdateDate(new Date());
			valuePO.setUpdateBy(Long.parseLong(userId));
			dao.update(accountFreezePO, valuePO);
		}else{
			TtVsAccountPO ttVsAccountPO = new TtVsAccountPO();
			ttVsAccountPO.setAccountId(Long.parseLong(accountId));
			List<PO> aList = dao.select(ttVsAccountPO);
			if (null != aList && aList.size()>0) {
				TtVsAccountPO accountPO = (TtVsAccountPO)aList.get(0);
				
				accountFreezePO.setFreezeId(Long.parseLong(SequenceManager.getSequence("")));
				accountFreezePO.setCompanyId(accountPO.getCompanyId());
				accountFreezePO.setDealerId(accountPO.getDealerId());
				accountFreezePO.setFreezeAmount(Double.parseDouble(freezeAmount));
				accountFreezePO.setStatus(accountFreezeStatus);
				accountFreezePO.setFreezeDate(new Date());
				accountFreezePO.setCreateDate(new Date());
				accountFreezePO.setCreateBy(Long.parseLong(userId));
				dao.insert(accountFreezePO);
			}
		}
	}
	public PageResult<Map<String, Object>> getFreezeList(String dealerId  , int pageSize, int curPage){
		StringBuffer sql = new StringBuffer();
		/*sql.append("SELECT D.DEALER_SHORTNAME, --开票单位\n");
		sql.append("       O.ORDER_NO, --订单号\n");  
		sql.append("       TO_CHAR(O.RAISE_DATE, 'yyyy-MM-dd') RAISE_DATE, --订单提报日期\n");  
		sql.append("       T.TYPE_NAME, --资金类型\n");  
		sql.append("       F.FREEZE_AMOUNT, --冻结金额\n");  
		sql.append("       D.DEALER_CODE,\n");
		sql.append("       O.ORDER_STATUS\n");
		sql.append("  FROM TT_VS_ACCOUNT_FREEZE F,\n");  
		sql.append("       TT_VS_DLVRY_REQ      R,\n");  
		sql.append("       TT_VS_ORDER          O,\n");  
		sql.append("       TM_DEALER            D,\n");  
		sql.append("       TT_VS_ACCOUNT_TYPE   T\n");  
		sql.append(" WHERE F.REQ_ID = R.REQ_ID\n");  
		sql.append("   AND R.ORDER_ID = O.ORDER_ID\n");  
		sql.append("   AND O.BILLING_ORG_ID = D.DEALER_ID\n");  
		sql.append("   AND R.FUND_TYPE = T.TYPE_ID\n");  
		sql.append("   AND F.STATUS = " + Constant.ACCOUNT_FREEZE_STATUS_01 + "\n");
		sql.append("   AND F.DEALER_ID in ("+dealerId+")\n");  
		sql.append(" ORDER BY T.TYPE_NAME,O.RAISE_DATE DESC\n");*/
		
		sql.append("SELECT *\n");
		sql.append("  FROM (SELECT D.DEALER_SHORTNAME, --开票单位\n");  
		sql.append("               O.ORDER_NO, --订单号\n");  
		sql.append("               TO_CHAR(O.RAISE_DATE, 'yyyy-MM-dd') RAISE_DATE, --订单提报日期\n");  
		sql.append("               T.TYPE_NAME, --资金类型\n");  
		sql.append("               F.FREEZE_AMOUNT, --冻结金额\n");  
		sql.append("               D.DEALER_CODE,\n");  
		sql.append("               O.ORDER_STATUS,\n");  
		sql.append("               '订单扣款' FREEZE_TYPE\n");  
		sql.append("          FROM TT_VS_ACCOUNT_FREEZE F,\n");  
		sql.append("               TT_VS_DLVRY_REQ      R,\n");  
		sql.append("               TT_VS_ORDER          O,\n");  
		sql.append("               TM_DEALER            D,\n");  
		sql.append("               TT_VS_ACCOUNT_TYPE   T,TT_VS_ACCOUNT TVA\n");  
		sql.append("         WHERE F.REQ_ID = R.REQ_ID\n");  
		sql.append("           AND R.ORDER_ID = O.ORDER_ID   AND TVA.ACCOUNT_ID=F.ACCOUNT_ID\n");  
		sql.append("           AND O.BILLING_ORG_ID = D.DEALER_ID\n");  
		sql.append("           AND TVA.ACCOUNT_TYPE_ID=T.TYPE_ID\n");  
		sql.append("           AND F.STATUS = " + Constant.ACCOUNT_FREEZE_STATUS_01 + "\n");  
		sql.append("           AND F.DEALER_ID = "+dealerId+"\n");  
		sql.append("        UNION\n");  
		sql.append("        SELECT D.DEALER_SHORTNAME, --开票单位\n");  
		sql.append("               '' ORDER_NO,\n");  
		sql.append("               TO_CHAR(R.REQ_DATE, 'yyyy-MM-dd') RAISE_DATE, --需求提报日期\n");  
		sql.append("               T.TYPE_NAME, --资金类型\n");  
		sql.append("               F.FREEZE_AMOUNT, --冻结金额\n");  
		sql.append("               D.DEALER_CODE,\n");  
		sql.append("               R.REQ_STATUS ORDER_STATUS,\n");  
		sql.append("               '订做车预扣款' FREEZE_TYPE\n");  
		sql.append("          FROM TT_VS_ACCOUNT_FREEZE F,\n");  
		sql.append("               TT_VS_SPECIAL_REQ    R,\n");  
		sql.append("               TM_DEALER            D,\n");  
		sql.append("               TT_VS_ACCOUNT_TYPE   T\n");  
		sql.append("         WHERE F.BUZ_ID = R.REQ_ID\n");  
		sql.append("           AND R.DEALER_ID = D.DEALER_ID\n");  
		sql.append("           AND R.ACCOUNT_TYPE_ID = T.TYPE_ID\n");  
		sql.append("           AND F.STATUS = " + Constant.ACCOUNT_FREEZE_STATUS_01 + "\n");  
		sql.append("           AND F.DEALER_ID = "+dealerId+") TEMP\n");  
		sql.append(" ORDER BY TEMP.FREEZE_TYPE, TEMP.TYPE_NAME, TEMP.RAISE_DATE DESC");


		return dao.pageQuery(sql.toString(), null, dao.getFunName(), pageSize,curPage);
	}
	public List<Map<String, Object>> getFreezeList(String dealerId ){
		StringBuffer sql = new StringBuffer();
		/*sql.append("SELECT D.DEALER_SHORTNAME, --开票单位\n");
		sql.append("       O.ORDER_NO, --订单号\n");  
		sql.append("       TO_CHAR(O.RAISE_DATE, 'yyyy-MM-dd') RAISE_DATE, --订单提报日期\n");  
		sql.append("       T.TYPE_NAME, --资金类型\n");  
		sql.append("       F.FREEZE_AMOUNT, --冻结金额\n");  
		sql.append("       D.DEALER_CODE,\n");
		sql.append("       O.ORDER_STATUS\n");
		sql.append("  FROM TT_VS_ACCOUNT_FREEZE F,\n");  
		sql.append("       TT_VS_DLVRY_REQ      R,\n");  
		sql.append("       TT_VS_ORDER          O,\n");  
		sql.append("       TM_DEALER            D,\n");  
		sql.append("       TT_VS_ACCOUNT_TYPE   T\n");  
		sql.append(" WHERE F.REQ_ID = R.REQ_ID\n");  
		sql.append("   AND R.ORDER_ID = O.ORDER_ID\n");  
		sql.append("   AND O.BILLING_ORG_ID = D.DEALER_ID\n");  
		sql.append("   AND R.FUND_TYPE = T.TYPE_ID\n");  
		sql.append("   AND F.STATUS = " + Constant.ACCOUNT_FREEZE_STATUS_01 + "\n");
		sql.append("   AND F.DEALER_ID in ("+dealerId+")\n");  
		sql.append(" ORDER BY D.DEALER_CODE\n");*/

		sql.append("SELECT *\n");
		sql.append("  FROM (SELECT D.DEALER_SHORTNAME, --开票单位\n");  
		sql.append("               O.ORDER_NO, --订单号\n");  
		sql.append("               TO_CHAR(O.RAISE_DATE, 'yyyy-MM-dd') RAISE_DATE, --订单提报日期\n");  
		sql.append("               T.TYPE_NAME, --资金类型\n");  
		sql.append("               F.FREEZE_AMOUNT, --冻结金额\n");  
		sql.append("               D.DEALER_CODE,\n");  
		sql.append("               O.ORDER_STATUS,\n");  
		sql.append("               '订单扣款' FREEZE_TYPE\n");  
		sql.append("          FROM TT_VS_ACCOUNT_FREEZE F,\n");  
		sql.append("               TT_VS_DLVRY_REQ      R,\n");  
		sql.append("               TT_VS_ORDER          O,\n");  
		sql.append("               TM_DEALER            D,\n");  
		sql.append("               TT_VS_ACCOUNT_TYPE   T\n");  
		sql.append("         WHERE F.REQ_ID = R.REQ_ID\n");  
		sql.append("           AND R.ORDER_ID = O.ORDER_ID\n");  
		sql.append("           AND O.BILLING_ORG_ID = D.DEALER_ID\n");  
		sql.append("           AND R.FUND_TYPE = T.TYPE_ID\n");  
		sql.append("           AND F.STATUS = " + Constant.ACCOUNT_FREEZE_STATUS_01 + "\n");  
		sql.append("           AND F.DEALER_ID IN ("+dealerId+")\n");  
		sql.append("        UNION\n");  
		sql.append("        SELECT D.DEALER_SHORTNAME, --开票单位\n");  
		sql.append("               '' ORDER_NO,\n");  
		sql.append("               TO_CHAR(R.REQ_DATE, 'yyyy-MM-dd') RAISE_DATE, --需求提报日期\n");  
		sql.append("               T.TYPE_NAME, --资金类型\n");  
		sql.append("               F.FREEZE_AMOUNT, --冻结金额\n");  
		sql.append("               D.DEALER_CODE,\n");  
		sql.append("               R.REQ_STATUS ORDER_STATUS,\n");  
		sql.append("               '订做车预扣款' FREEZE_TYPE\n");  
		sql.append("          FROM TT_VS_ACCOUNT_FREEZE F,\n");  
		sql.append("               TT_VS_SPECIAL_REQ    R,\n");  
		sql.append("               TM_DEALER            D,\n");  
		sql.append("               TT_VS_ACCOUNT_TYPE   T\n");  
		sql.append("         WHERE F.BUZ_ID = R.REQ_ID\n");  
		sql.append("           AND R.DEALER_ID = D.DEALER_ID\n");  
		sql.append("           AND R.ACCOUNT_TYPE_ID = T.TYPE_ID\n");  
		sql.append("           AND F.STATUS = " + Constant.ACCOUNT_FREEZE_STATUS_01 + "\n");  
		sql.append("           AND F.DEALER_ID IN ("+dealerId+")) TEMP\n");  
		sql.append(" ORDER BY TEMP.FREEZE_TYPE, TEMP.TYPE_NAME, TEMP.RAISE_DATE DESC");

		return dao.pageQuery(sql.toString(), null, dao.getFunName());
	}

	public Map<String,String> oldIsCaibing (String oldFundTypeId){
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT NVL(T.IS_USE_ORDER_ACCOUNT, 0) IS_USE_ORDER_ACCOUNT\n");
		sql.append("  FROM TT_VS_ACCOUNT_TYPE T\n");  
		sql.append(" WHERE T.TYPE_ID = "+oldFundTypeId+"\n");

		return dao.pageQueryMap(sql.toString(), null, dao.getFunName());
	}
	
	
	public List<Map<String, Object>> getSpecialReqAccountFreezeInfo(String dealerId) throws Exception{
		StringBuffer sql= new StringBuffer();

		sql.append("SELECT D.DEALER_SHORTNAME, --开票单位\n");
		sql.append("       TO_CHAR(R.REQ_DATE, 'yyyy-MM-dd') RAISE_DATE, --需求提报日期\n");  
		sql.append("       T.TYPE_NAME, --资金类型\n");  
		sql.append("       F.FREEZE_AMOUNT, --冻结金额\n");  
		sql.append("       D.DEALER_CODE,\n");  
		sql.append("       R.REQ_STATUS\n");  
		sql.append("  FROM TT_VS_ACCOUNT_FREEZE F,\n");  
		sql.append("       TT_VS_SPECIAL_REQ    R,\n");  
		sql.append("       TM_DEALER            D,\n");  
		sql.append("       TT_VS_ACCOUNT_TYPE   T\n");  
		sql.append(" WHERE F.BUZ_ID = R.REQ_ID\n");  
		sql.append("   AND R.DEALER_ID = D.DEALER_ID\n");  
		sql.append("   AND R.ACCOUNT_TYPE_ID = T.TYPE_ID\n");  
		sql.append("   AND F.STATUS = "+Constant.ACCOUNT_FREEZE_STATUS_01+"\n");  
		sql.append("   AND F.DEALER_ID = "+dealerId+"\n");  
		sql.append(" ORDER BY T.TYPE_NAME, R.REQ_DATE DESC");

		List<Map<String, Object>> list= dao.pageQuery(sql.toString(), null, getFunName());
		return list;
	}
	
	
	//-------------------------------------------同步结算中心下级经销商订单 START ------------------------------------
	
	/**
	 * 结算中心下属经销商的订单要根据情况进行调整,并将变化情况发送至DFS系统,
	 * 使结算中心的下级经销商在资金系统中的预扣款得到调整<br>
	 * 1.根据结算中心的情况同步其下级经销商的订单【订单取消,同步确认,发运指令取消】<br>
	 * 2.直接对下级经销商的原始订单进行修改【下级订单审核-通过,下级订单审核-驳回,下级订单审核-库存车满足】<br>
	 * 		此种情况时一定要在CHECK_AMOUNT更新完毕后调用此方法
	 * 
	 * @param orderId 订单ID,如果是结算中心订单的话,isJSZXOrder赋值为<code>true</code>
	 * @param isJSZXOrder 是否是结算中心订单,<code>true</code>为结算中心订单
	 * @param userId   操作用户ID
	 * @param isCancel 是否为取消操作,<code>true</code>为取消订单操作
	 */
	public void syncOrderToDFS(Long orderId,boolean isJSZXOrder,Long userId,boolean isCancel){
		if(orderId == null ){
			throw new RuntimeException (orderId+"订单ID不能为空！");
		} 				
		
		if(isJSZXOrder){
			syncOrderToDfsByJszxOrderID(orderId,userId,isCancel);
		} else{
			syncOrderToDfsByOrderID(orderId,userId,isCancel);
		}		
	}
	
	/**
	 * 根据原始订单的ID直接同步订单信息
	 * @param orderId
	 * @param userId
	 */
	private void syncOrderToDfsByOrderID(Long orderId,Long userId, boolean isCancel){
		TtVsOrderPO po = getOrderPOById(orderId);
		if(po == null) throw new RuntimeException("订单ID找不到对应订单数据");
		if(isCancel){//订单取消时
			callDfsChangeProcToWriteInterface(orderId,2);
		}else{
			List<Map<String, Object>> changedOrderDetailList = getChangedOrderByOrderId(orderId);
			/* 当下级订单改变后，只有CHECK_AMOUNT变化,但总的金额TOTAL_PRICE未变化,
			 * 所以根据(OD.SINGLE_PRICE * OD.CHECK_AMOUNT) 与  TOTAL_PRICE 是否相等来判断是否发生改变*/
			if(changedOrderDetailList != null && 
					changedOrderDetailList.size()>0){//资金发生变化时,才同步订单
				updateChangedOrder(orderId,userId);
				callDfsChangeProcToWriteInterface(orderId,1);
			}
		}
		
	}
	
	public void syncReqToDFS(Long reqId, boolean isCancel) {
		if(reqId == null ){
			throw new RuntimeException(reqId +"发运申请ID不能为空！");
		} 
		
		TtVsDlvryReqPO tvr = new TtVsDlvryReqPO() ;
		tvr.setReqId(reqId) ;
		tvr = (TtVsDlvryReqPO)dao.select(tvr).get(0) ;
		
		Map<String, Object> map = DealerRelationDAO.getInstance().getFirstDlr(tvr.getOrderDealerId().toString()) ;
		
		if(CommonUtils.isNullMap(map)) {
			logger.info(reqId + ":无法查询到开票单位经销商类型!") ;
			return ;
		}
		
		String dlrType = map.get("DEALER_TYPE").toString() ;
		
		TtVsOrderPO tvo = new TtVsOrderPO() ;
		tvo.setOrderId(tvr.getOrderId());
		tvo = (TtVsOrderPO)dao.select(tvo).get(0) ;
		
		String orderType = tvo.getOrderType().toString() ;
		
		if(Constant.DEALER_TYPE_JSZX == Integer.parseInt(dlrType) && Constant.ORDER_TYPE_01.intValue() == Integer.parseInt(orderType)) {
			if(isCancel){//订单取消时
				callDfsChangeProcReqToWriteInterface(reqId, 2);
			}else{
				List<Map<String, Object>> updateList = this.chkUpdateAct(reqId.toString()) ;
				if(!CommonUtils.isNullList(updateList)) {
					this.updateDtl(updateList) ;
					
					callDfsChangeProcReqToWriteInterface(reqId, 1);
				}
			}
		} else {
			return ;
		}
	}
	
	private void callDfsChangeProcReqToWriteInterface(Long orderId,int interfaceFlag) {
		List<Object> ins = new LinkedList<Object>();
		ins.add(orderId);
		ins.add(interfaceFlag);//1 为 更改 , 2为 取消
		dao.callProcedure("P_INSERTDATA_TO_DFS_CG_JS_V2", ins, null);//调用存储过程将变更发送至DFS系统
	}
	
	/**
	 * 根据结算中心订单来同步下级经销商订单
	 * @param orderId
	 * @param userId
	 * @param isCancel
	 */
	private int syncOrderToDfsByJszxOrderID(Long orderId,Long userId,boolean isCancel){
		TtVsOrderPO jszxOrderpo = getOrderPOById(orderId);
		if(jszxOrderpo == null) 
			throw new RuntimeException("订单ID:"+orderId+" 找不到对应订单数据");
		/**++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	     * 描述:银翔原型报错修复
	     * 结算中心采购订单
	     * urgentDlvryReq 第三参数“jszxOrderpo.getOldOrderId()” --> “1L”
	     * Date:2017-06-29
	     */
		TtVsOrderPO dealerOrderPo = getOrderPOById(1L);
		if(dealerOrderPo == null) 
			//throw new RuntimeException("结算订单ID:"+jszxOrderpo.getOldOrderId()+" 找不到对应下级订单数据");
			return 0;//无对应下级订单数据时,不处理,直接退出
		
		if(isCancel){//订单取消时
			callDfsChangeProcToWriteInterface(dealerOrderPo.getOrderId(),2);
		}else{
			List<Map<String, Object>> list = getChangedOrderNewDetailList(orderId);
			for(Iterator<Map<String, Object>> itor = list.iterator();itor.hasNext();){
				Map<String, Object> record = (Map<String, Object>) itor.next();
				syncDlrOrderDetailRecord(record,userId);			
			}
			callDfsChangeProcToWriteInterface(dealerOrderPo.getOrderId(),1);
		}
		
		return 1;
	}
	
	/**
	 * 为每一条原始订单明细记录同步信息
	 * @param record
	 * @param userId
	 * @return
	 */
	private int syncDlrOrderDetailRecord(Map<String, Object> record,Long userId){
		Long detailId = ((BigDecimal) record.get("DETAIL_ID")).longValue();
		Long newOrderAmount = ((BigDecimal) record.get("REQ_AMOUNT")).longValue();
		Long oldOrderAmount = ((BigDecimal) record.get("CHECK_AMOUNT")).longValue();
		Double singlePrice = ((BigDecimal) record.get("SALES_PRICE")).doubleValue();
		
		if(singlePrice.intValue() == -1){
			throw new RuntimeException("下级经销商的价格列表中该物料没有维护价格！");
		}
		
		if(detailId == 0){//detail_id=0表示经销商提报时没有这种产品的记录，需新增记录
			newOrderDetailRecord(record,userId);			
		}else {//原始订单中就包含本产品,产品单价不会发生变化
			if(oldOrderAmount.longValue() == newOrderAmount.longValue()){
				return 0;//新老订单数量无变化相等无需处理
			}else {
				updateOrderDetailRecord(record,userId);
			}
		}
		return 1;
	}
	
	/**
	 * 新增原始订单明细记录
	 * @param record
	 * @param userId
	 */
	private void newOrderDetailRecord(Map<String, Object> record,Long userId){
		Long detailId = Long.parseLong(SequenceManager.getSequence(null));
		Integer newOrderAmount = ((BigDecimal) record.get("REQ_AMOUNT")).intValue();
		Long orderId = ((BigDecimal) record.get("ORDER_ID")).longValue();
		Long materialId = ((BigDecimal) record.get("MATERIAL_ID")).longValue();
		Double singlePrice = ((BigDecimal) record.get("SALES_PRICE")).doubleValue();
		Double totalPrice = ((BigDecimal) record.get("TOTAL_PRICE")).doubleValue();
		TtVsOrderDetailPO newDetailPO = new TtVsOrderDetailPO();
		
		newDetailPO.setDetailId(detailId);
		newDetailPO.setOrderId(orderId);
		newDetailPO.setMaterialId(materialId);
		newDetailPO.setOrderAmount(0);//原始提报量为0
		newDetailPO.setCheckAmount(newOrderAmount);
		newDetailPO.setSinglePrice(singlePrice);
		//银翔原型报错修复：注释newDetailPO.setDiscountSPrice(singlePrice);
		newDetailPO.setTotalPrice(totalPrice);
		newDetailPO.setCreateBy(userId);
		newDetailPO.setCreateDate(new Date());
		dao.insert(newDetailPO);
		
	}
	
	/**
	 * 更新原始订单明细记录
	 * @param record
	 * @param userId
	 */
	private void updateOrderDetailRecord(Map record,Long userId){
		Long detailId = ((BigDecimal) record.get("DETAIL_ID")).longValue();
		Integer newOrderAmount = ((BigDecimal) record.get("REQ_AMOUNT")).intValue();
		Double totalPrice = ((BigDecimal) record.get("TOTAL_PRICE")).doubleValue();
		TtVsOrderDetailPO queryPo = new TtVsOrderDetailPO();
		queryPo.setDetailId(detailId);
		
		TtVsOrderDetailPO valuePo = new TtVsOrderDetailPO();
		valuePo.setCheckAmount(newOrderAmount);		
		valuePo.setTotalPrice(totalPrice);
		valuePo.setUpdateBy(userId);
		valuePo.setUpdateDate(new Date());
		dao.update(queryPo,valuePo);
	}
	
	/**
	 * 调用写入接口表的存储过程
	 * @param orderId 应该为下级经销商的订单ID
	 * @param interfaceFlag 1 为 更改 , 2为 取消
	 */
	private void callDfsChangeProcToWriteInterface(Long orderId,int interfaceFlag) {
		List<Object> ins = new LinkedList<Object>();
		ins.add(orderId);
		ins.add(interfaceFlag);//1 为 更改 , 2为 取消
		dao.callProcedure("p_insertdata_to_dfs_js_v2", ins, null);//调用存储过程将变更发送至DFS系统
	}
	
	/**
	 * 当下级订单改变后，只有CHECK_AMOUNT变化,但总的金额TOTAL_PRICE未变化,
	 * 所以根据(OD.SINGLE_PRICE * OD.CHECK_AMOUNT) 与  TOTAL_PRICE 是否相等来判断是否发生改变
	 * @param orderId
	 * @return
	 */
	private List<Map<String, Object>> getChangedOrderByOrderId(Long orderId){
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT OD.DETAIL_ID,\n");
		sql.append("       (OD.SINGLE_PRICE * nvl(OD.CHECK_AMOUNT, 0)) NEW_PRICE,\n");  
		sql.append("       OD.TOTAL_PRICE\n");  
		sql.append("  FROM TT_VS_ORDER_DETAIL OD\n");  
		sql.append(" WHERE OD.ORDER_ID = "+orderId+"\n");  
		sql.append("   AND (OD.SINGLE_PRICE * nvl(OD.CHECK_AMOUNT, 0)) <> TOTAL_PRICE");
		List<Map<String, Object>> list= dao.pageQuery(sql.toString(), null, getFunName());
		return list;
		
	}
	
	/**
	 * 将指定订单的订单总价格进行同步
	 * @param orderId
	 * @param userId
	 */
	private void updateChangedOrder(Long orderId,Long userId){
		List params = new ArrayList();
		StringBuffer sql = new StringBuffer();
		sql.append("UPDATE TT_VS_ORDER_DETAIL OD\n");
		sql.append("   SET OD.TOTAL_PRICE = OD.SINGLE_PRICE * nvl(OD.CHECK_AMOUNT, 0),\n"); 
		sql.append("   	   OD.UPDATE_DATE = SYSDATE,\n"); 
		sql.append("   	   OD.UPDATE_BY = ?\n");
		params.add(userId);
		sql.append(" WHERE (OD.SINGLE_PRICE * nvl(OD.CHECK_AMOUNT, 0)) <> TOTAL_PRICE\n");  
		sql.append("   AND OD.ORDER_ID = ?");
		params.add(orderId);
		dao.update(sql.toString(), params);

	}
	
	/**
	 * 根据结算中心订单来ID得到对应原始订单的变化情况
	 * @param jszxOrderId 结算中心订单ID
	 * @return
	 */
	private List<Map<String, Object>> getChangedOrderNewDetailList(Long jszxOrderId){
		List params = new LinkedList();
		
		StringBuffer strSql = new StringBuffer("\n") ;
		strSql.append("     SELECT oo.price_id\n");  
		strSql.append("            FROM TT_VS_ORDER OO, TT_VS_ORDER NWO\n");  
		strSql.append("           WHERE NWO.ORDER_ID = "+jszxOrderId+"\n");  
		strSql.append("             AND OO.ORDER_ID = NWO.OLD_ORDER_ID\n"); 

		Map<String, Object> map = super.pageQueryMap(strSql.toString(), null, super.getFunName()) ;
		
		StringBuffer sql1 = new StringBuffer();
		sql1.append("INSERT INTO Z --全局变量临时表,提高查询速度\n");
		sql1.append("  (PRICE_ID, MATERIAL_ID, SALES_PRICE)\n");  
		sql1.append("  SELECT VP.PRICE_ID, GR.MATERIAL_ID, VPT.SALES_PRICE\n");  
		sql1.append("    FROM vw_TT_VS_PRICE VP, vw_TT_VS_PRICE_DTL VPT, TM_VHCL_MATERIAL_GROUP_R GR\n");  
		sql1.append("   WHERE VP.PRICE_ID = VPT.PRICE_ID\n");  
		sql1.append("     AND GR.GROUP_ID = VPT.GROUP_ID\n");  
		sql1.append("      and vp.price_id = ").append(map.get("PRICE_ID").toString()).append("") ;

//		sql1.append("     AND EXISTS (SELECT 1\n");  
//		sql1.append("            FROM TT_VS_ORDER OO, TT_VS_ORDER NWO\n");  
//		sql1.append("           WHERE NWO.ORDER_ID = "+jszxOrderId+"\n");  
//		sql1.append("             AND OO.ORDER_ID = NWO.OLD_ORDER_ID\n");  
//		sql1.append("             AND VP.PRICE_ID = OO.PRICE_ID)");
		dao.update(sql1.toString(), null);
		
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT NVL(X.DETAIL_ID,0) DETAIL_ID,--原始订单明细ID\n");  
		sql.append("       Y.ORDER_ID,\n"); 
		sql.append("       NVL(X.MATERIAL_ID, Y.MATERIAL_ID) MATERIAL_ID,\n");  
		sql.append("       Y.REQ_AMOUNT,--结算中心的订单数量\n");  
		sql.append("       Z.PRICE_ID,--原始订单对应的价格ID\n");  
		sql.append("       NVL(X.CHECK_AMOUNT, 0) CHECK_AMOUNT,--原始订单的订单数量\n");  
		sql.append("       NVL(Z.SALES_PRICE, -1) SALES_PRICE,--原始订单单价\n");  
		sql.append("       (Z.SALES_PRICE * Y.REQ_AMOUNT) TOTAL_PRICE--变化后原始订单的合计价格\n");  
		sql.append("  FROM (SELECT DLRO.ORDER_ID,\n");  
		sql.append("               JSZXO.OLD_ORDER_ID,\n");  
		sql.append("               VDRD.MATERIAL_ID,\n");  
		sql.append("               DLRO.PRICE_ID,\n");  
		sql.append("               vdrd.reserve_amount REQ_AMOUNT\n");  
		sql.append("          FROM TT_VS_DLVRY_REQ VDR,\n");  
		sql.append("               TT_VS_DLVRY_REQ_DTL VDRD,\n");  
		sql.append("               TT_VS_ORDER DLRO,\n");  
		sql.append("               TT_VS_ORDER JSZXO\n");  
		sql.append("         WHERE VDR.REQ_ID = VDRD.REQ_ID\n");  
		sql.append("           AND VDR.ORDER_ID = JSZXO.ORDER_ID\n");  
		sql.append("           AND JSZXO.OLD_ORDER_ID = DLRO.ORDER_ID\n");  
		sql.append("           AND JSZXO.ORDER_ID = ?) Y\n"); 
		params.add(jszxOrderId);  
		sql.append("  LEFT JOIN (SELECT DLROD.ORDER_ID,\n");  
		sql.append("                    DLROD.DETAIL_ID,\n");  
		sql.append("                    DLROD.MATERIAL_ID,\n");  
		sql.append("                    DLROD.CHECK_AMOUNT\n");  
		sql.append("               FROM TT_VS_ORDER_DETAIL DLROD) X\n");  
		sql.append("    ON X.ORDER_ID = Y.ORDER_ID\n");  
		sql.append("   AND Y.MATERIAL_ID = X.MATERIAL_ID(+)\n");  
		sql.append("  LEFT JOIN Z\n");  
		sql.append("    ON Y.PRICE_ID = Z.PRICE_ID\n");  
		sql.append("   AND Y.MATERIAL_ID = Z.MATERIAL_ID");
		List<Map<String, Object>> list= dao.pageQuery(sql.toString(), params, getFunName());
		
		return list;

	}
	
	
	/**
	 * 根据订单ID获得TtVsOrderPO
	 * @param orderId
	 * @return
	 */
	private TtVsOrderPO getOrderPOById(Long orderId){
		TtVsOrderPO po = new TtVsOrderPO();
		po.setOrderId(orderId);
		List<PO> list = select(po);
		return list.size() != 0 ? (TtVsOrderPO) list.get(0) : null;
	}
	
	//-------------------------------------------同步结算中心下级经销商订单 END ------------------------------------
	
	
	/*********************************************** new accountOpera*************************************************/
	
	public static String getAccountIdMap(String dlrId, String accountId_kp) {
		String accountId = "-1" ;

		List<Object> params = new ArrayList<Object>() ;

		StringBuffer sql = new StringBuffer("\n") ;

		sql.append("select nvl(tva_cg.account_id, -1) o_account_id\n");
		sql.append("  from tt_vs_account tva_cg, tt_vs_account tva_kp\n");  
		sql.append(" where tva_cg.account_type_id = tva_kp.account_type_id\n");  
		sql.append("   and tva_cg.dealer_id = ?\n"); 
		params.add(dlrId) ;
		
		sql.append("   and tva_kp.account_id = ?\n");
		params.add(accountId_kp) ;

		Map<String, Object> accountMap = dao.pageQueryMap(sql.toString(), params, dao.getFunName()) ;

		if(accountMap != null) {
			accountId = accountMap.get("O_ACCOUNT_ID").toString() ;
		}

		return accountId ;
	}
	
	public static String getAvailableAmount(String accountId) {
		String availableAmount = "-1" ;

		List<Object> params = new ArrayList<Object>() ;

		StringBuffer sql = new StringBuffer("\n") ;

		sql.append("select tva.available_amount from tt_vs_account tva where tva.account_id = ?\n");
		params.add(accountId) ;

		Map<String, Object> accountMap = dao.pageQueryMap(sql.toString(), params, dao.getFunName()) ;

		if(accountMap != null) {
			availableAmount = accountMap.get("AVAILABLE_AMOUNT").toString() ;
		} 

		return availableAmount ;
	}
	
	public static Map<String, Object> getOrderDlr(String reqId) {
		List<Object> params = new ArrayList<Object>() ;

		StringBuffer sql = new StringBuffer("\n") ;

		sql.append("\n");
		sql.append("select nvl(tmd.erp_code, -1) erp_code,\n");  
		sql.append("       nvl(tmd.dealer_id, -1) dlr_kp,\n");  
		sql.append("       nvl(decode(tvdr.call_leavel, ").append(Constant.DEALER_LEVEL_02).append(", tmd2.erp_code, tmd1.erp_code), -2) er_code,\n");
		sql.append("       nvl(decode(tvdr.call_leavel, ").append(Constant.DEALER_LEVEL_02).append(", tmd2.dealer_id, tmd1.dealer_id), -2) dlr_cg\n");
		sql.append("  from tt_vs_dlvry_req tvdr,\n");  
		sql.append("       tt_vs_order     tvo,\n");  
		sql.append("       tm_dealer       tmd,\n");  
		sql.append("       tm_dealer       tmd1,\n");  
		sql.append("       tm_dealer       tmd2\n");  
		sql.append(" where tvdr.order_id = tvo.order_id\n");  
		sql.append("   and tvo.billing_org_id = tmd.dealer_id\n");  
		sql.append("   and tvo.order_org_id = tmd1.dealer_id\n");  
		sql.append("   and tvdr.order_dealer_id = tmd2.dealer_id(+)\n");  
		sql.append("   and tvdr.req_id = ?\n");
		params.add(reqId) ;

		Map<String, Object> orderMap = dao.pageQueryMap(sql.toString(), params, dao.getFunName()) ;

		return orderMap ;
	}
	
	public static String getAccountTypeClass(String accountId) {
		String typeClass = "-1" ;

		List<Object> params = new ArrayList<Object>() ;

		StringBuffer sql = new StringBuffer("\n") ;

		sql.append("select tvat.type_id, nvl(tvat.type_class, -1) type_class\n");
		sql.append("  from tt_vs_account tva, tt_vs_account_type tvat\n");  
		sql.append("  where tva.account_type_id = tvat.type_id\n");
		sql.append("   and tva.account_id = ?\n");
		params.add(accountId) ;

		Map<String, Object> accTypeMap = dao.pageQueryMap(sql.toString(), params, dao.getFunName()) ;

		if(accTypeMap != null) {
			typeClass = accTypeMap.get("TYPE_CLASS").toString() ;
		}

		return typeClass ;
	}
	
	public static String getAllLowAccount(String accountId) {
		String price = "0" ;
		
		List<Object> params = new ArrayList<Object>() ;

		StringBuffer sql = new StringBuffer("\n") ;
		
		sql.append("select nvl(sum(nvl(tva1.available_amount, 0)), 0) available_amount\n");
		sql.append("  from tt_vs_account tva1, tt_vs_account tva2, tm_dealer tmd\n");  
		sql.append(" where tva1.dealer_id = tmd.dealer_id\n");  
		sql.append("   and tva2.dealer_id = tmd.parent_dealer_d\n");  
		sql.append("   and tva1.account_type_id = tva2.account_type_id\n");  
		sql.append("   and tva2.account_id = ?\n");
		params.add(accountId) ;

		Map<String, Object> accountMap = dao.pageQueryMap(sql.toString(), params, dao.getFunName()) ;
		
		if(accountMap != null) {
			price = accountMap.get("AVAILABLE_AMOUNT").toString() ;
		}
		
		return price;
	}
	
	public static Map<String, Object> getAccountInfo(String accountId) {
		List<Object> params = new ArrayList<Object>() ;

		StringBuffer sql = new StringBuffer("\n") ;
		
		sql.append("select tva.dealer_id, tva.account_type_id from tt_vs_account tva where tva.account_id = ?\n");
		params.add(accountId) ;
		
		Map<String, Object> infoMap = dao.pageQueryMap(sql.toString(), params, dao.getFunName()) ;
		
		return infoMap ;
	}
	
	public static Map<String, Object> getFreezeAmountByReqId(String reqId, String accountId, String status) {
		List<Object> params = new ArrayList<Object>() ;

		StringBuffer sql = new StringBuffer("\n") ;
		
		sql.append("select nvl(tvaf.freeze_amount, 0) freeze_amount\n");
		sql.append("  from tt_vs_account_freeze tvaf\n");  
		sql.append("  where tvaf.req_id = ?\n");  
		params.add(reqId) ;
		
		sql.append("   and tvaf.account_id = ?\n"); 
		params.add(accountId) ;
		
		sql.append("   and tvaf.status = ?\n");
		params.add(status) ;
		
		Map<String, Object> fAmountMap = dao.pageQueryMap(sql.toString(), params, dao.getFunName()) ;
		
		return fAmountMap ;
	}
	
	public Map<String, Object> getDlrPrice(String dlrId) {
		StringBuffer sql = new StringBuffer("\n") ;
		
		sql.append("select vtdpr.PRICE_ID\n");
		sql.append("  from vw_tm_dealer_price_relation vtdpr\n");  
		sql.append(" where vtdpr.dealer_id = ").append(dlrId).append("\n");  
		sql.append("   and vtdpr.IS_DEFAULT = ").append(Constant.IF_TYPE_YES).append("\n");

		return super.pageQueryMap(sql.toString(), null, super.getFunName()) ;
	}
	
	public List<Map<String, Object>> getTotalPrice(String priceId, String reqId) {
		StringBuffer sql = new StringBuffer("\n") ;
		
		sql.append("select tvmgr.group_id,\n");
		sql.append("       tvdrd.material_id,\n");  
		sql.append("       nvl(tvdrd.reserve_amount, tvdrd.req_amount) amount,\n");  
		sql.append("       nvl(vtvpd.SALES_PRICE, -1) SALES_PRICE,\n");  
		sql.append("       nvl(tvdrd.reserve_amount, tvdrd.req_amount) *\n");  
		sql.append("       nvl(vtvpd.SALES_PRICE, -1) price\n");  
		sql.append("  from tt_vs_dlvry_req_dtl      tvdrd,\n");  
		sql.append("       tm_vhcl_material_group_r tvmgr,\n");  
		sql.append("       vw_tt_vs_price_dtl       vtvpd\n");  
		sql.append(" where tvdrd.material_id = tvmgr.material_id\n");  
		sql.append("   and tvmgr.group_id = vtvpd.GROUP_ID(+)\n");  
		sql.append("   and vtvpd.PRICE_ID = ").append(priceId).append("\n");  
		sql.append("   and tvdrd.req_id = ").append(reqId).append("\n");  

		return super.pageQuery(sql.toString(), null, super.getFunName()) ;
	}
	
	public static void syncAccountOpera(String reqId,String accountId,BigDecimal freezeAmount, String userId,boolean isPayAll) {
		AccountBalanceDetailDao abdd  = new AccountBalanceDetailDao() ;
		abdd.syncAccountFreeze(reqId, accountId, freezeAmount, userId, isPayAll) ;
	}
	
	public int getFirstToDfsHeadReq(String reqId) {
		StringBuffer sql = new StringBuffer("\n") ;
		
		sql.append("select count(1) headCount\n");
		sql.append("  from dms_orderhead_v dohv\n");  
		sql.append(" where dohv.doc_id = ").append(reqId).append("\n");

		Map<String, Object> map =  super.pageQueryMap(sql.toString(), null, super.getFunName()) ;
		
		if(null == map) {
			return 0 ;
		} else {
			return Integer.parseInt(map.get("HEADCOUNT").toString()) ;
		}
	}
	
	public int getFirstToDfs2HeadReq(String reqId) {
		StringBuffer sql = new StringBuffer("\n") ;
		
		sql.append("select count(1) headCount\n");
		sql.append("  from DMS_ORDERHEAD_JS_V dohv\n");  
		sql.append(" where dohv.doc_id = ").append(reqId).append("\n");

		Map<String, Object> map =  super.pageQueryMap(sql.toString(), null, super.getFunName()) ;
		
		if(null == map) {
			return 0 ;
		} else {
			return Integer.parseInt(map.get("HEADCOUNT").toString()) ;
		}
	}
	
	public int getFirstToDfsLineReq(String reqId) {
		StringBuffer sql = new StringBuffer("\n") ;
		
		sql.append("select count(1) lineCount\n");
		sql.append("  from dms_orderline_v dohv\n");  
		sql.append(" where dohv.delivery_id = ").append(reqId).append("\n");

		Map<String, Object> map =  super.pageQueryMap(sql.toString(), null, super.getFunName()) ;
		
		if(null == map) {
			return 0 ;
		} else {
			return Integer.parseInt(map.get("LINECOUNT").toString()) ;
		}
	}
	
	public int getFirstToDfs2LineReq(String reqId) {
		StringBuffer sql = new StringBuffer("\n") ;
		
		sql.append("select count(1) lineCount\n");
		sql.append("  from dms_orderline_js_v dohv\n");  
		sql.append(" where dohv.delivery_id = ").append(reqId).append("\n");

		Map<String, Object> map =  super.pageQueryMap(sql.toString(), null, super.getFunName()) ;
		
		if(null == map) {
			return 0 ;
		} else {
			return Integer.parseInt(map.get("LINECOUNT").toString()) ;
		}
	}
	
	public List<Map<String, Object>> getFirstToDfsDtl(String reqId) {
		StringBuffer sql = new StringBuffer("\n") ;
		
		sql.append("select dohv.material_code, dohv.amount\n");
		sql.append("  from dms_orderline_v dohv\n");  
		sql.append(" where dohv.delivery_id = ").append(reqId).append("\n");  
		sql.append(" order by rowid desc\n");

		return super.pageQuery(sql.toString(), null, super.getFunName()) ;
	}
	
	public List<Map<String, Object>> getFirstToDfs2Dtl(String reqId) {
		StringBuffer sql = new StringBuffer("\n") ;
		
		sql.append("select dohv.material_code, dohv.amount\n");
		sql.append("  from dms_orderline_js_v dohv\n");  
		sql.append(" where dohv.delivery_id = ").append(reqId).append("\n");  
		sql.append(" order by rowid desc\n");

		return super.pageQuery(sql.toString(), null, super.getFunName()) ;
	}
	
	public List<Map<String, Object>> getDmsDtl(String reqId) {
		String priceId = this.getPriceId(this.getParentDlrId(reqId)) ;
		
		StringBuffer sql = new StringBuffer("\n") ;
		
		sql.append("SELECT m.material_code, vtvpd.sales_price * tvdrd.req_amount total_amount\n");
		sql.append("  FROM tt_vs_dlvry_req_dtl      tvdrd,\n");  
		sql.append("       tm_vhcl_material         m,\n");  
		sql.append("       tm_vhcl_material_group_r tvmgr,\n");  
		sql.append("       Vw_Tt_Vs_Price_Dtl       vtvpd\n");  
		sql.append(" WHERE tvdrd.material_id = m.material_id\n");  
		sql.append("   and m.material_id = tvmgr.material_id\n");  
		sql.append("   and tvmgr.group_id = vtvpd.group_id\n");  
		sql.append("   and vtvpd.price_id = ").append(priceId).append("\n");  
		sql.append("   AND tvdrd.req_id = ").append(reqId).append("\n"); 

		return super.pageQuery(sql.toString(), null, super.getFunName()) ;
	}
	
	public String getParentDlrId(String reqId) {
		StringBuffer sql = new StringBuffer("\n") ;
		
		sql.append("select tmd.parent_dealer_d\n");
		sql.append("  from tt_vs_order tvo, tt_vs_dlvry_req tvdr, tm_dealer tmd\n");  
		sql.append(" where tvo.order_id = tvdr.order_id\n");  
		sql.append("   and tvo.order_org_id = tmd.dealer_id\n");  
		sql.append("   and tmd.dealer_level = 10851002\n");  
		sql.append("   and tvdr.req_id = ").append(reqId).append("\n");  

		Map<String, Object> map =  super.pageQueryMap(sql.toString(), null, super.getFunName()) ;
		
		return map.get("PARENT_DEALER_D").toString() ;
	}
	
	public String getPriceId(String dlrId) {
		StringBuffer sql = new StringBuffer("\n") ;
		
		sql.append("select vtdpr.price_id\n");
		sql.append("  from vw_tm_dealer_price_relation vtdpr\n");  
		sql.append(" where vtdpr.dealer_id = ").append(dlrId).append("\n");  
		sql.append("   and vtdpr.is_default = 10041001\n");
		
		Map<String, Object> map =  super.pageQueryMap(sql.toString(), null, super.getFunName()) ;

		return map.get("PRICE_ID").toString() ;
	}
	
	public List<Map<String, Object>> chkUpdate(String reqId) {
		LowWeekGeneralOrderCall lwgoc = new LowWeekGeneralOrderCall() ;
		String priceId = lwgoc.getPirceId(reqId) ;
		
		StringBuffer sql = new StringBuffer("\n") ;
		
		sql.append("select tvdrgd.detail_id,\n");
		sql.append("       tvdrd.req_amount,\n");  
		sql.append("       tvdrd.material_id,\n");  
		sql.append("       tvdrd.ver,\n");  
		sql.append("       nvl(vtvpd.SALES_PRICE, -1) single_price,\n");  
		sql.append("       tvdrd.req_amount * nvl(vtvpd.SALES_PRICE, -1) total_price,\n");  
		sql.append("       0,\n");  
		sql.append("       ROUND(nvl(VTVPD.SALES_PRICE, -1)) DISCOUNT_S_PRICE,\n");  
		sql.append("       0 DISCOUNT_PRICE,\n");  
		sql.append("       tvdrd.update_by\n");  
		sql.append("  from tt_vs_dlvry_req_general_dtl tvdrgd,\n");  
		sql.append("       tt_vs_dlvry_req_dtl tvdrd,\n");  
		sql.append("       tm_vhcl_material_group_r tvmgr,\n");  
		sql.append("       (select vtvpd.GROUP_ID, vtvpd.SALES_PRICE\n");  
		sql.append("         from vw_tt_vs_price_dtl vtvpd\n");  
		sql.append("        where vtvpd.PRICE_ID = ").append(priceId).append(") vtvpd\n");  
		sql.append("        where tvdrgd.old_detail_id(+) = tvdrd.detail_id\n");  
		sql.append("          and tvmgr.group_id = vtvpd.GROUP_ID(+)\n");  
		sql.append("          and tvdrd.material_id = tvmgr.material_id\n");  
		sql.append("          and tvdrd.req_id = ").append(reqId).append("\n");  
		sql.append("          and tvdrgd.total_price <> tvdrd.req_amount * nvl(vtvpd.SALES_PRICE, -1)\n");

		List<Map<String, Object>> list = super.pageQuery(sql.toString(), null, super.getFunName()) ;
		
		return list ;
	}
	
	public List<Map<String, Object>> chkUpdateAct(String reqId) {
		List<Map<String, Object>> list = this.chkUpdate(reqId) ;
		
		if(CommonUtils.isNullList(list)) {
			return null ;
		} else {
			return list ;
		}
	}
	
	public void updateDtl(List<Map<String, Object>> list) {
		int len = list.size() ;
		for(int i=0; i<len; i++) {
			TtVsDlvryReqGeneralDtlPO tvrgd = new TtVsDlvryReqGeneralDtlPO() ;
			tvrgd.setDetailId(Long.parseLong(list.get(i).get("DETAIL_ID").toString())) ;
		
			TtVsDlvryReqGeneralDtlPO tvrgdNew = new TtVsDlvryReqGeneralDtlPO() ;
			tvrgdNew.setDetailId(Long.parseLong(list.get(i).get("DETAIL_ID").toString())) ;
			tvrgdNew.setReqAmount(Integer.parseInt(list.get(i).get("REQ_AMOUNT").toString())) ;
			tvrgdNew.setVer(Integer.parseInt(list.get(i).get("VER").toString())) ;
			tvrgdNew.setSinglePrice(Double.parseDouble(list.get(i).get("SINGLE_PRICE").toString())) ;
			tvrgdNew.setTotalPrice(Double.parseDouble(list.get(i).get("TOTAL_PRICE").toString())) ;
			tvrgdNew.setDiscountPrice(Double.parseDouble(list.get(i).get("DISCOUNT_PRICE").toString())) ;
			tvrgdNew.setUpdateDate(new Date(System.currentTimeMillis())) ;
			tvrgdNew.setUpdateBy(Long.parseLong(list.get(i).get("UPDATE_BY").toString())) ;
			
			super.update(tvrgd, tvrgdNew) ;
		}
	}
	
	public Map<String, Object> getJSZXNormalPrice(Map<String, String> map) {
		String orderId = map.get("orderId") ;
		
		StringBuffer sql = new StringBuffer("\n") ;

		sql.append("select tvdgd.req_id oldOrderId,\n") ;
		sql.append("       sum(tvdgd.total_price) oldPrice\n") ;
		sql.append("  from tt_vs_dlvry_req_general_dtl tvdgd\n") ;
		sql.append(" where tvdrd.req_id = ").append(orderId).append("\n") ;
		sql.append(" group by tvdgd.req_id") ;
		
		return super.pageQueryMap(sql.toString(), null, super.getFunName()) ;
	}
	
	public Map<String, Object> getJSZXUrgentPrice(Map<String, String> map) {
		String orderId = map.get("orderId") ;
		
		StringBuffer sql = new StringBuffer("\n") ;

		sql.append("select tvo.old_order_id,\n") ;
		sql.append("       sum(otvod.total_price) oldPrice\n") ;
		sql.append("  from tt_vs_order tvo, tt_vs_order_detail otvod\n") ;
		sql.append(" where tvo.old_order_id = otvod.order_id\n") ;
		sql.append("   and tvo.order_id = ").append(orderId).append("\n") ;
		sql.append(" group by tvo.old_order_id") ;
		
		return super.pageQueryMap(sql.toString(), null, super.getFunName()) ;
	}
	
	public Map<String, Object> getOldNormalPrice(String orderId) {
		StringBuffer sql = new StringBuffer("\n") ;

		sql.append("select tvdr.price_id\n") ;
		sql.append("  from tt_vs_dlvry_req tvdr\n") ;
		sql.append(" where tvdr.req_id = ").append(orderId).append("") ;

		return super.pageQueryMap(sql.toString(), null, super.getFunName()) ;
	}
	
	public Map<String, Object> getOldUrgentPrice(String orderId) {
		StringBuffer sql = new StringBuffer("\n") ;

		sql.append("select otvo.price_id\n") ;
		sql.append("  from tt_vs_order ntvo, tt_vs_order otvo\n") ;
		sql.append(" where ntvo.old_order_id = otvo.order_id\n") ;
		sql.append("   and ntvo.order_id = ").append(orderId).append("") ;

		return super.pageQueryMap(sql.toString(), null, super.getFunName()) ;
	}
	
	public Map<String, Object> getSingleCountPrice(String priceId, String matId, String orderType) {
		StringBuffer sql = new StringBuffer("\n") ;

		/*sql.append("select vd.sales_price\n") ;
		sql.append("  from vw_tt_vs_price_dtl vd, tm_vhcl_material_group_r tvmgr\n") ;
		sql.append(" where vd.group_id = tvmgr.group_id\n") ;
		sql.append("   and vd.price_id = ").append(priceId).append("\n") ;
		sql.append("   and tvmgr.material_id = ").append(matId).append("") ;*/
		
		sql.append("select F_GET_PRICE("+ priceId +", " + matId + ", "+ orderType +") sales_price\n") ;
		sql.append("  from dual tvmgr\n") ;

		return super.pageQueryMap(sql.toString(), null, super.getFunName()) ;
	}
	
	/**
	 * 通过经销商CODE取得账户信息（车厂审核时候用）
	 * @param dealerId
	 * @return
	 */
	public List<Map<String,Object>> getNoDiscountAccountInfoByDealerCode(String dealerCode){
		StringBuilder sql= new StringBuilder();
		sql.append("SELECT TAA.ACCOUNT_CODE TYPE_CODE,\n" );
		sql.append("        TAA.ACCOUNT_CODE TYPE_NAME,\n" );
		sql.append("       ATP.TYPE_ID,\n" );
		sql.append("       0 ACCOUNT_ID,\n" );
		sql.append("       TAA.BALANCE_AMOUNT           BALANCE_AMOUNT,\n" );
		sql.append("       0                     FREEZE_AMOUNT,\n" );
		sql.append("       TAA.BALANCE_AMOUNT    AVAILABLE_AMOUNT,\n" );
		sql.append("       null                     IS_USE_ORDER_ACCOUNT\n" );
		sql.append("  FROM CUX_DMS_acount_V@Dms2ebs2 TAA,TT_VS_ACCOUNT_TYPE ATP\n" );
		sql.append(" WHERE ATP.STATUS=10011001\n" );
		sql.append(" 	 TAA.ACCOUNT_CODE(+)=ATP.TYPE_CODE");
		sql.append("   AND TAA.DEALER_CODE = '"+dealerCode+"'");

		
		List<Map<String,Object>> list = dao.pageQuery(sql.toString(), null, getFunName());
		return list;
	}
}
