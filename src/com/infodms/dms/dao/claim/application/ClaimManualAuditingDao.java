package com.infodms.dms.dao.claim.application;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import com.infodms.dms.actions.report.dmsReport.ApplicationDao;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.ClaimOrderBean;
import com.infodms.dms.bean.auditBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.dao.common.CommonDAO;
import com.infodms.dms.po.TcPosePO;
import com.infodms.dms.po.TcUserPO;
import com.infodms.dms.po.TmDealerPO;
import com.infodms.dms.po.TrPoseDealerPO;
import com.infodms.dms.po.TtAsActivityPO;
import com.infodms.dms.po.TtAsWrApplicationExtPO;
import com.infodms.dms.po.TtAsWrApplicationPO;
import com.infodms.dms.po.TtAsWrClaimBalancePO;
import com.infodms.dms.po.TtAsWrLabouritemPO;
import com.infodms.dms.po.TtAsWrNetitemPO;
import com.infodms.dms.po.TtAsWrPartsitemPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.StringUtil;
import com.infodms.dms.util.businessUtil.GetVinUtil;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

/**
 * 索赔申请单审核
 * 主要功能：
 *    1、要审核的索赔申请单查询
 *    2、索赔申请单审核
 * @author XZM
 *
 */
@SuppressWarnings("unchecked")
public class ClaimManualAuditingDao extends BaseDao {
	
	private static final ClaimManualAuditingDao dao = new ClaimManualAuditingDao();
	public static final ClaimManualAuditingDao getInstance(){
		if (dao == null) {
			return new ClaimManualAuditingDao();
		}
		return dao;
	}
	
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		return null;
	}
	public PageResult<Map<String,Object>> administrative_charge(String BALANCE_ODER,String status,String dealercode,Integer curPage,Integer pageSize)
	{
		StringBuffer sql= new StringBuffer();
		sql.append("select B.DEALER_NAME,\n" );
		sql.append("       B.DEALER_CODE,\n" );
		sql.append("       nvl(A.DATUM_SUM,0) DATUM_SUM,\n" );
		sql.append("       nvl(A.LABOUR_SUM,0) LABOUR_SUM,\n" );
		sql.append("       to_char(A.creat_date,'YYYY-MM-DD hh24:mi:ss') as create_date,\n" );
		sql.append("       A.BALANCE_ODER, A.STATUS \n" );
		sql.append("  from tt_as_wr_administrative_charge A, TM_DEALER B\n" );
		sql.append(" where A.DEALERID = B.DEALER_ID");
		if(Utility.testString(BALANCE_ODER))
		{
			sql.append(" and A.BALANCE_ODER like '%"+BALANCE_ODER+"%' ");
		}
		if(Utility.testString(status))
		{
			sql.append(" and  A.STATUS ="+ status);
		}
		
		if(Utility.testString(dealercode))
		{
			sql.append(" and  B.DEALER_CODE like '%"+dealercode+"%'");
		}
		
		PageResult<Map<String,Object>> ps= (PageResult<Map<String, Object>>) this.pageQuery(sql.toString(),
				null,
				this.getClass().getName()+".queryClaim()",
				pageSize,
				curPage);
		return ps;
	}
	
	/**
	 * 查询索赔申请单信息
	 * 注意：现在语句只支持二级经销商,需要支持多级以上则需要调整
	 * @param crBean 查询条件
	 * @param curPage 当前页
	 * @param pageSize 每页记录数
	 * @return PageResult<Map<String,Object>> 索赔申请单信息
	 */
	public PageResult<Map<String,Object>> queryClaimAudit(ClaimOrderBean orderBean,Integer curPage,Integer pageSize){
		
		StringBuilder sBuilder = new StringBuilder();
		List<Object> paramList = new ArrayList<Object>();
		
		//paramList.add(orderBean.getCompanyId());
		paramList.add(orderBean.getOrgId());
	    //paramList.add(orderBean.getAreaIds());
		
		//初始化索赔申请单查询条件
		this.createWhereMap("YIELDLY" ,"IN" ,orderBean.getYieldlys() , sBuilder, paramList,2,"","A");
		this.createWhereMap("YIELDLY" ,"=" ,orderBean.getYieldly() , sBuilder, paramList,2,"","A");
		this.createWhereMap("CLAIM_NO" ,"LIKE" ,orderBean.getClaimNo() , sBuilder, paramList,2,"","A");
		this.createWhereMap("RO_NO" ,"LIKE" ,orderBean.getRoNo() , sBuilder, paramList,2,"","A");
		this.createWhereMap("LINE_NO","=" ,orderBean.getLineNo(), sBuilder, paramList,2,"","A");
		this.createWhereMap("CLAIM_TYPE","=" ,orderBean.getClaimType() , sBuilder, paramList,2,"","A");
		this.createWhereMap("AUTH_CODE" ,"=" ,orderBean.getAuthCode(), sBuilder, paramList,2,"","A");
		this.createWhereMap("PROVINCE_ID" ,"=" ,orderBean.getProvinceCode(), sBuilder, paramList,2,"","B");
		this.createWhereMap("DEALER_CODE" ,"IN" ,orderBean.getDealerCodes(), sBuilder, paramList,2,"","B");
		this.createWhereMap("DEALER_SHORTNAME" ,"LIKE" ,orderBean.getDealerName(), sBuilder, paramList,2,"","B");
		this.createWhereMap("STATUS", "=" ,orderBean.getClaimStatus() , sBuilder, paramList,2,"","A");
		this.createWhereMap("CREATE_DATE", ">=" ,orderBean.getApplyStartDate(), sBuilder, paramList,1,"YYYY-MM-DD HH24:MI:SS","A");
		this.createWhereMap("CREATE_DATE","<=" ,orderBean.getApplyEndDate() , sBuilder, paramList,1,"YYYY-MM-DD HH24:MI:SS","A");
		
		String whereMap = sBuilder.toString();
		
		PageResult<Map<String, Object>> result = null;
		String sql = "";
		StringBuilder sqlBulider = new StringBuilder();
		sqlBulider.append(" SELECT /*+ INDEX (A IDX_TT_APPLICATION_STATUS) */ A.CLAIM_NO, B.DEALER_CODE,B.DEALER_SHORTNAME DEALER_NAME,\n" );
		sqlBulider.append("      A.RO_NO AS RO_NO,A.LINE_NO,A.CLAIM_TYPE,A.VIN,\n" );
		sqlBulider.append("      TO_CHAR(A.CREATE_DATE, 'YYYY-MM-DD') CREATE_DATE,A.STATUS,\n" );
		sqlBulider.append("      A.ID,A.REPAIR_TOTAL\n" );
		sqlBulider.append(" FROM TT_AS_WR_APPLICATION A,\n" );
		sqlBulider.append("      TM_DEALER B,\n" );
		//sqlBulider.append("      TC_USER_REGION_RELATION J,\n" );
		sqlBulider.append("            vw_org_dealer_service vw, \n" );
		sqlBulider.append("      (SELECT ORG_ID\n" );
		sqlBulider.append("                 FROM TM_ORG ORG\n" );
		sqlBulider.append("                WHERE ORG.STATUS = "+Constant.STATUS_ENABLE+"\n" );
		sqlBulider.append("                START WITH ORG.ORG_ID = ?\n" );
		sqlBulider.append("               CONNECT BY PRIOR ORG.ORG_ID = ORG.PARENT_ORG_ID) ORG\n" );
		sqlBulider.append("WHERE 1 = 1\n" );
		sqlBulider.append("  AND A.DEALER_ID = B.DEALER_ID\n" );
		//sqlBulider.append("  AND B.PROVINCE_ID = J.REGION_CODE\n");
		sqlBulider.append("  and a.dealer_id = vw.dealer_id AND ORG.ORG_ID = vw.root_org_id\n" );
		sqlBulider.append("  AND EXISTS (SELECT 1\n" );
		sqlBulider.append("       FROM TM_DEALER_BUSINESS_AREA TDBA\n" );
		sqlBulider.append("      WHERE TDBA.DEALER_ID = A.DEALER_ID\n" );
		sqlBulider.append("        AND TDBA.AREA_ID IN ("+orderBean.getAreaIds()+")\n)");
		sqlBulider.append(whereMap + "\n");
		sqlBulider.append(GetVinUtil.getVins(orderBean.getVin(), "A") + "\n");
		//sqlBulider.append(UserProvinceRelation.getDealerIds(Long.parseLong(orderBean.getUserId()), "b")) ;
		//sqlBulider.append(" AND J.USER_ID = ").append(orderBean.getUserId());
		sqlBulider.append("ORDER BY A.ID DESC");
		
		sql = sqlBulider.toString();
		
		result = (PageResult<Map<String, Object>>)this.pageQuery(sql,
																paramList,
																this.getClass().getName()+".queryClaim()",
																pageSize,
																curPage);
		return result;
	}
	
	
	public PageResult<Map<String,Object>> queryClaimAuditDelete(ClaimOrderBean orderBean,Integer curPage,Integer pageSize){
		StringBuilder sBuilder = new StringBuilder();
		List<Object> paramList = new ArrayList<Object>();
		
		paramList.add(orderBean.getOrgId());
		
		//初始化索赔申请单查询条件
		this.createWhereMap("YIELDLY" ,"IN" ,orderBean.getYieldlys() , sBuilder, paramList,2,"","A");
		this.createWhereMap("YIELDLY" ,"=" ,orderBean.getYieldly() , sBuilder, paramList,2,"","A");
		this.createWhereMap("CLAIM_NO" ,"LIKE" ,orderBean.getClaimNo() , sBuilder, paramList,2,"","A");
		this.createWhereMap("RO_NO" ,"LIKE" ,orderBean.getRoNo() , sBuilder, paramList,2,"","A");
		this.createWhereMap("LINE_NO","=" ,orderBean.getLineNo(), sBuilder, paramList,2,"","A");
		this.createWhereMap("CLAIM_TYPE","=" ,orderBean.getClaimType() , sBuilder, paramList,2,"","A");
		this.createWhereMap("PROVINCE_ID" ,"=" ,orderBean.getProvinceCode(), sBuilder, paramList,2,"","B");
		this.createWhereMap("DEALER_CODE" ,"IN" ,orderBean.getDealerCodes(), sBuilder, paramList,2,"","B");
		this.createWhereMap("DEALER_SHORTNAME" ,"LIKE" ,orderBean.getDealerName(), sBuilder, paramList,2,"","B");
		this.createWhereMap("CREATE_DATE", ">=" ,orderBean.getApplyStartDate(), sBuilder, paramList,1,"YYYY-MM-DD HH24:MI:SS","A");
		this.createWhereMap("CREATE_DATE","<=" ,orderBean.getApplyEndDate() , sBuilder, paramList,1,"YYYY-MM-DD HH24:MI:SS","A");
		
		String whereMap = sBuilder.toString();
		
		PageResult<Map<String, Object>> result = null;
		String sql = "";
		StringBuilder sqlBulider = new StringBuilder();
		sqlBulider.append(" SELECT /*+ INDEX (A IDX_TT_APPLICATION_STATUS) */ A.CLAIM_NO, B.DEALER_CODE,B.DEALER_SHORTNAME DEALER_NAME,\n" );
		sqlBulider.append("      A.RO_NO AS RO_NO,A.LINE_NO,A.CLAIM_TYPE,A.VIN,B.PHONE,\n" );
		sqlBulider.append("      TO_CHAR(A.CREATE_DATE, 'YYYY-MM-DD') CREATE_DATE,A.STATUS,\n" );
		sqlBulider.append("      A.ID,A.REPAIR_TOTAL\n" );
		sqlBulider.append(" FROM tt_as_wr_application_backup A,\n" );
		sqlBulider.append("      TM_DEALER B,\n" );
		sqlBulider.append("      TM_DEALER_ORG_RELATION REL,\n" );
		sqlBulider.append("      (SELECT ORG_ID\n" );
		sqlBulider.append("                 FROM TM_ORG ORG\n" );
		sqlBulider.append("                WHERE ORG.STATUS = "+Constant.STATUS_ENABLE+"\n" );
		sqlBulider.append("                START WITH ORG.ORG_ID = ?\n" );
		sqlBulider.append("               CONNECT BY PRIOR ORG.ORG_ID = ORG.PARENT_ORG_ID) ORG\n" );
		sqlBulider.append("WHERE 1 = 1 and A.status in('"+Constant.CLAIM_APPLY_ORD_TYPE_04+"','"+Constant.CLAIM_APPLY_ORD_TYPE_05+"','"+Constant.CLAIM_APPLY_ORD_TYPE_06+"')\n" );
		sqlBulider.append("  AND A.DEALER_ID = B.DEALER_ID\n" );
		sqlBulider.append("  AND (REL.DEALER_ID = B.DEALER_ID OR REL.DEALER_ID = B.PARENT_DEALER_D)\n" );
		sqlBulider.append("  AND ORG.ORG_ID=REL.ORG_ID\n" );
		sqlBulider.append("  AND EXISTS (SELECT 1\n" );
		sqlBulider.append("       FROM TM_DEALER_BUSINESS_AREA TDBA\n" );
		sqlBulider.append("      WHERE TDBA.DEALER_ID = A.DEALER_ID\n" );
		sqlBulider.append("        AND TDBA.AREA_ID IN ("+orderBean.getAreaIds()+")\n)");
		sqlBulider.append(whereMap + "\n");
		sqlBulider.append(GetVinUtil.getVins(orderBean.getVin(), "A") + "\n");
		sqlBulider.append("ORDER BY A.ID DESC");
		
		sql = sqlBulider.toString();
		System.out.println("sql=="+sql);
		result = (PageResult<Map<String, Object>>)this.pageQuery(sql,
																paramList,
																this.getClass().getName()+".queryClaim()",
																pageSize,
																curPage);
		return result;
	}
	
	public PageResult<Map<String,Object>> queryClaimAuditDelete1(ClaimOrderBean orderBean,Integer curPage,Integer pageSize){
		StringBuilder sBuilder = new StringBuilder();
		List<Object> paramList = new ArrayList<Object>();
		
		paramList.add(orderBean.getOrgId());
		
		//初始化索赔申请单查询条件
		this.createWhereMap("YIELDLY" ,"IN" ,orderBean.getYieldlys() , sBuilder, paramList,2,"","A");
		this.createWhereMap("YIELDLY" ,"=" ,orderBean.getYieldly() , sBuilder, paramList,2,"","A");
		this.createWhereMap("CLAIM_NO" ,"LIKE" ,orderBean.getClaimNo() , sBuilder, paramList,2,"","A");
		this.createWhereMap("RO_NO" ,"LIKE" ,orderBean.getRoNo() , sBuilder, paramList,2,"","A");
		this.createWhereMap("LINE_NO","=" ,orderBean.getLineNo(), sBuilder, paramList,2,"","A");
		this.createWhereMap("CLAIM_TYPE","=" ,orderBean.getClaimType() , sBuilder, paramList,2,"","A");
		this.createWhereMap("PROVINCE_ID" ,"=" ,orderBean.getProvinceCode(), sBuilder, paramList,2,"","B");
		this.createWhereMap("DEALER_CODE" ,"IN" ,orderBean.getDealerCodes(), sBuilder, paramList,2,"","B");
		this.createWhereMap("DEALER_SHORTNAME" ,"LIKE" ,orderBean.getDealerName(), sBuilder, paramList,2,"","B");
		this.createWhereMap("CREATE_DATE", ">=" ,orderBean.getApplyStartDate(), sBuilder, paramList,1,"YYYY-MM-DD HH24:MI:SS","A");
		this.createWhereMap("CREATE_DATE","<=" ,orderBean.getApplyEndDate() , sBuilder, paramList,1,"YYYY-MM-DD HH24:MI:SS","A");
		
		String whereMap = sBuilder.toString();
		
		PageResult<Map<String, Object>> result = null;
		String sql = "";
		StringBuilder sqlBulider = new StringBuilder();
		sqlBulider.append(" SELECT /*+ INDEX (A IDX_TT_APPLICATION_STATUS) */ A.CLAIM_NO, B.DEALER_CODE,B.DEALER_SHORTNAME DEALER_NAME,\n" );
		sqlBulider.append("      A.RO_NO AS RO_NO,A.LINE_NO,A.CLAIM_TYPE,A.VIN,B.PHONE,\n" );
		sqlBulider.append("      TO_CHAR(A.CREATE_DATE, 'YYYY-MM-DD') CREATE_DATE,A.STATUS,\n" );
		sqlBulider.append("      A.ID,A.REPAIR_TOTAL\n" );
		sqlBulider.append(" FROM TT_AS_WR_APPLICATION A,\n" );
		sqlBulider.append("      TM_DEALER B,\n" );
		sqlBulider.append("      TC_USER_REGION_RELATION J,\n" );
		sqlBulider.append("      TM_DEALER_ORG_RELATION REL,\n" );
		sqlBulider.append("      (SELECT ORG_ID\n" );
		sqlBulider.append("                 FROM TM_ORG ORG\n" );
		sqlBulider.append("                WHERE ORG.STATUS = "+Constant.STATUS_ENABLE+"\n" );
		sqlBulider.append("                START WITH ORG.ORG_ID = ?\n" );
		sqlBulider.append("               CONNECT BY PRIOR ORG.ORG_ID = ORG.PARENT_ORG_ID) ORG\n" );
		sqlBulider.append("WHERE 1 = 1 and a.application_del='"+Constant.RO_APP_STATUS_03.toString()+"'\n" );
		sqlBulider.append("  AND A.DEALER_ID = B.DEALER_ID\n" );
		sqlBulider.append("  AND B.PROVINCE_ID = J.REGION_CODE\n");
		sqlBulider.append("  AND (REL.DEALER_ID = B.DEALER_ID OR REL.DEALER_ID = B.PARENT_DEALER_D)\n" );
		sqlBulider.append("  AND ORG.ORG_ID=REL.ORG_ID\n" );
		sqlBulider.append("  AND EXISTS (SELECT 1\n" );
		sqlBulider.append("       FROM TM_DEALER_BUSINESS_AREA TDBA\n" );
		sqlBulider.append("      WHERE TDBA.DEALER_ID = A.DEALER_ID\n" );
		sqlBulider.append("        AND TDBA.AREA_ID IN ("+orderBean.getAreaIds()+")\n)");
		sqlBulider.append(whereMap + "\n");
		sqlBulider.append(GetVinUtil.getVins(orderBean.getVin(), "A") + "\n");
		sqlBulider.append(" AND J.USER_ID = ").append(orderBean.getUserId());
		sqlBulider.append("ORDER BY A.ID DESC");
		sql = sqlBulider.toString();
		System.out.println("sql==="+sql);
		result = (PageResult<Map<String, Object>>)this.pageQuery(sql,
																paramList,
																this.getClass().getName()+".queryClaim()",
																pageSize,
																curPage);
		return result;
	}
	
	
	public PageResult<Map<String,Object>> queryClaimAuditDelete2(ClaimOrderBean orderBean,Integer curPage,Integer pageSize){
	    StringBuilder sBuilder = new StringBuilder();
	    List<Object> paramList = new ArrayList<Object>();
	    
	    paramList.add(orderBean.getOrgId());
	    
	    //初始化索赔申请单查询条件
	    this.createWhereMap("YIELDLY" ,"IN" ,orderBean.getYieldlys() , sBuilder, paramList,2,"","A");
	    this.createWhereMap("YIELDLY" ,"=" ,orderBean.getYieldly() , sBuilder, paramList,2,"","A");
	    this.createWhereMap("CLAIM_NO" ,"LIKE" ,orderBean.getClaimNo() , sBuilder, paramList,2,"","A");
	    this.createWhereMap("RO_NO" ,"LIKE" ,orderBean.getRoNo() , sBuilder, paramList,2,"","A");
	    this.createWhereMap("LINE_NO","=" ,orderBean.getLineNo(), sBuilder, paramList,2,"","A");
	    this.createWhereMap("CLAIM_TYPE","=" ,orderBean.getClaimType() , sBuilder, paramList,2,"","A");
	    this.createWhereMap("PROVINCE_ID" ,"=" ,orderBean.getProvinceCode(), sBuilder, paramList,2,"","B");
	    this.createWhereMap("DEALER_CODE" ,"IN" ,orderBean.getDealerCodes(), sBuilder, paramList,2,"","B");
	    this.createWhereMap("DEALER_SHORTNAME" ,"LIKE" ,orderBean.getDealerName(), sBuilder, paramList,2,"","B");
	    this.createWhereMap("CREATE_DATE", ">=" ,orderBean.getApplyStartDate(), sBuilder, paramList,1,"YYYY-MM-DD HH24:MI:SS","A");
	    this.createWhereMap("CREATE_DATE","<=" ,orderBean.getApplyEndDate() , sBuilder, paramList,1,"YYYY-MM-DD HH24:MI:SS","A");
	    
	    String whereMap = sBuilder.toString();
	    
	    PageResult<Map<String, Object>> result = null;
	    String sql = "";
	    StringBuilder sqlBulider = new StringBuilder();
	    sqlBulider.append(" SELECT /*+ INDEX (A IDX_TT_APPLICATION_STATUS) */ A.CLAIM_NO, B.DEALER_CODE,B.DEALER_SHORTNAME DEALER_NAME,\n" );
	    sqlBulider.append("      A.RO_NO AS RO_NO,A.LINE_NO,A.CLAIM_TYPE,A.VIN,B.PHONE,\n" );
	    sqlBulider.append("      TO_CHAR(A.CREATE_DATE, 'YYYY-MM-DD') CREATE_DATE,A.STATUS,\n" );
	    sqlBulider.append("      A.ID,A.REPAIR_TOTAL\n" );
	    sqlBulider.append(" FROM TT_AS_WR_APPLICATION A,\n" );
	    sqlBulider.append("      TM_DEALER B,\n" );
	    sqlBulider.append("      TC_USER_REGION_RELATION J,\n" );
	    sqlBulider.append("      TM_DEALER_ORG_RELATION REL,\n" );
	    sqlBulider.append("      (SELECT ORG_ID\n" );
	    sqlBulider.append("                 FROM TM_ORG ORG\n" );
	    sqlBulider.append("                WHERE ORG.STATUS = "+Constant.STATUS_ENABLE+"\n" );
	    sqlBulider.append("                START WITH ORG.ORG_ID = ?\n" );
	    sqlBulider.append("               CONNECT BY PRIOR ORG.ORG_ID = ORG.PARENT_ORG_ID) ORG\n" );
	    sqlBulider.append("WHERE 1 = 1 and a.application_del='"+Constant.CLAIM_APPLY_ORD_TYPE_08+"'\n" );
	    sqlBulider.append("  AND A.ID not in ( SELECT d.claim_id FROM Tt_As_Wr_Old_Returned r,Tt_As_Wr_Old_Returned_Detail d  WHERE r.id = d.return_id  GROUP BY d.claim_id )  ");
	    sqlBulider.append("  AND A.DEALER_ID = B.DEALER_ID and A.IS_INVOICE = 0  \n" );
	    sqlBulider.append("  AND B.PROVINCE_ID = J.REGION_CODE\n");
	    sqlBulider.append("  AND (REL.DEALER_ID = B.DEALER_ID OR REL.DEALER_ID = B.PARENT_DEALER_D)\n" );
	    sqlBulider.append("  AND ORG.ORG_ID=REL.ORG_ID\n" );
	    sqlBulider.append("  AND EXISTS (SELECT 1\n" );
	    sqlBulider.append("       FROM TM_DEALER_BUSINESS_AREA TDBA\n" );
	    sqlBulider.append("      WHERE TDBA.DEALER_ID = A.DEALER_ID\n" );
	    sqlBulider.append("        AND TDBA.AREA_ID IN ("+orderBean.getAreaIds()+")\n)");
	    sqlBulider.append(whereMap + "\n");
	    sqlBulider.append(GetVinUtil.getVins(orderBean.getVin(), "A") + "\n");
	    sqlBulider.append(" AND J.USER_ID = ").append(orderBean.getUserId());
	    sqlBulider.append("ORDER BY A.ID DESC");
	    sql = sqlBulider.toString();
	    System.out.println("sql==="+sql);
	    result = (PageResult<Map<String, Object>>)this.pageQuery(sql,
	                                paramList,
	                                this.getClass().getName()+".queryClaim()",
	                            		pageSize,
																	curPage);
			return result;
		}
		
	public PageResult<Map<String,Object>> queryClaimAudit2(Map<String, String> map,Integer curPage,Integer pageSize){
		String sql = "";
		StringBuilder sqlBulider = new StringBuilder();
		sqlBulider.append(" SELECT /*+ INDEX (A IDX_TT_APPLICATION_STATUS) */DISTINCT A.CLAIM_NO, b.DEALER_CODE,b.DEALER_SHORTNAME DEALER_NAME,vm.ROOT_ORG_NAME,\n" );
		sqlBulider.append("      A.RO_NO AS RO_NO,A.CLAIM_TYPE,A.VIN,a.submit_times,\n" );
		sqlBulider.append("      TO_CHAR(A.sub_date, 'yyyy-mm-dd hh24:mi') CREATE_DATE,A.STATUS,\n" );
		sqlBulider.append("      A.ID,A.REPAIR_TOTAL\n" );
		sqlBulider.append(" FROM TT_AS_WR_APPLICATION A,\n" );
		sqlBulider.append("      TM_DEALER B,\n" );
		sqlBulider.append("      Tt_As_Wr_Partsitem TAWP,\n" );
		sqlBulider.append("      VW_ORG_DEALER_SERVICE  vm,\n" );
		sqlBulider.append("      Tt_As_Wr_Labouritem TAWL\n" );
		sqlBulider.append("WHERE 1 = 1\n" );
		sqlBulider.append("  AND A.DEALER_ID = B.DEALER_ID\n" );
		sqlBulider.append("  AND A.DEALER_ID = vm.DEALER_ID\n" );
		sqlBulider.append("  AND A.ID = TAWP.ID(+)\n" );
		sqlBulider.append("  AND A.ID = TAWL.ID(+)\n" );
		sqlBulider.append("  AND a.STATUS in ( "+Constant.CLAIM_APPLY_ORD_TYPE_08+"  )  \n" );
		sqlBulider.append("  AND A.ID not in ( SELECT d.claim_id FROM Tt_As_Wr_Old_Returned r,Tt_As_Wr_Old_Returned_Detail d  WHERE r.id = d.return_id  GROUP BY d.claim_id )  ");
		sqlBulider.append("  AND A.DEALER_ID = B.DEALER_ID and A.IS_INVOICE = 0  \n" );
		if(Utility.testString(map.get("dealerId"))){
			sqlBulider.append("  AND B.dealer_id in ("+map.get("dealerId")+")\n" );
		}
		if(Utility.testString(map.get("dealerName"))){
			sqlBulider.append("  AND b.DEALER_SHORTNAME like '%"+map.get("dealerName")+"%'\n" );
		}
		if(Utility.testString(map.get("roNo"))){
			sqlBulider.append("  AND a.ro_no like '%"+map.get("roNo")+"%'\n" );
		}
		if(Utility.testString(map.get("claimType"))){
			sqlBulider.append("  AND a.claim_type="+map.get("claimType")+"\n" );
		}
		if(Utility.testString(map.get("vin"))){
			sqlBulider.append("  AND a.vin like '%"+map.get("vin")+"%'\n" );
		}
		if(Utility.testString(map.get("applyStartDate"))){
			sqlBulider.append("  AND a.sub_date>=to_date('"+map.get("applyStartDate")+"','yyyy-mm-dd')\n" );
		}
		if(Utility.testString(map.get("applyEndDate"))){
			sqlBulider.append("  AND a.sub_date<=to_date('"+map.get("applyEndDate")+"','yyyy-mm-dd')\n" );
		}
		if(Utility.testString(map.get("claimNo"))){
			sqlBulider.append("  AND a.claim_no like '%"+map.get("claimNo")+"%'\n" );
		}
		//零件代码
		if(Utility.testString(map.get("partCode"))){
			sqlBulider.append("  AND TAWP.PART_CODE like '%"+map.get("partCode")+"%'\n" );
		}
		//作业代码
		if(Utility.testString(map.get("wrLabourCode"))){
			sqlBulider.append("  AND TAWL.WR_LABOURCODE like '%"+map.get("wrLabourCode")+"%'\n" );
		}
		sqlBulider.append(" ORDER BY A.ID DESC");
		sql = sqlBulider.toString();
		PageResult<Map<String,Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
		return ps;
	}
	
	
	public PageResult<Map<String,Object>> queryClaimAudit1(Map<String, String> map,Integer curPage,Integer pageSize){
		String sql = "";
		StringBuilder sqlBulider = new StringBuilder();
		sqlBulider.append(" SELECT /*+ INDEX (A IDX_TT_APPLICATION_STATUS) */ A.CLAIM_NO, B.DEALER_CODE,A.DEALER_SHORTNAME DEALER_NAME,\n" );
		sqlBulider.append("      A.RO_NO AS RO_NO,A.CLAIM_TYPE,A.VIN,a.submit_times,\n" );
		sqlBulider.append("      TO_CHAR(A.sub_date, 'yyyy-mm-dd hh24:mi') CREATE_DATE,A.STATUS,\n" );
		sqlBulider.append("      A.ID,A.REPAIR_TOTAL,B.Root_Org_Name\n" );
		sqlBulider.append(" FROM TT_AS_WR_APPLICATION A,\n" );
		sqlBulider.append("      VW_ORG_DEALER_SERVICE B,tm_dealer tm \n" );
		sqlBulider.append("WHERE 1 = 1\n" );
		sqlBulider.append("  AND A.DEALER_ID = B.DEALER_ID and tm.DEALER_ID=A.DEALER_ID\n" );
		sqlBulider.append("  AND a.STATUS in ( "+Constant.CLAIM_APPLY_ORD_TYPE_03+" ,"+Constant.CLAIM_APPLY_ORD_TYPE_02+"  )  \n" );
		
		if(Utility.testString(map.get("dealerId"))){
			sqlBulider.append("  AND B.dealer_id in ("+map.get("dealerId")+")\n" );
		}
		
		if(Utility.testString(map.get("STATUS"))){
			sqlBulider.append("  AND a.STATUS like '%"+map.get("STATUS")+"%'\n" );
		}
		if(Utility.testString(map.get("dealerName"))){
			sqlBulider.append("  AND a.DEALER_SHORTNAME like '%"+map.get("dealerName")+"%'\n" );
		}
		if(Utility.testString(map.get("roNo"))){
			sqlBulider.append("  AND a.ro_no like '%"+map.get("roNo")+"%'\n" );
		}
		if(Utility.testString(map.get("claimType"))){
			sqlBulider.append("  AND a.claim_type="+map.get("claimType")+"\n" );
		}
		if(Utility.testString(map.get("vin"))){
			sqlBulider.append("  AND a.vin like '%"+map.get("vin")+"%'\n" );
		}
		if(Utility.testString(map.get("applyStartDate"))){
			sqlBulider.append("  AND a.sub_date>=to_date('"+map.get("applyStartDate")+"','yyyy-mm-dd')\n" );
		}
		if(Utility.testString(map.get("applyEndDate"))){
			sqlBulider.append("  AND a.sub_date<=to_date('"+map.get("applyEndDate")+"','yyyy-mm-dd')\n" );
		}
		if(Utility.testString(map.get("claimNo"))){
			sqlBulider.append("  AND a.claim_no like '%"+map.get("claimNo")+"%'\n" );
		}
		if(Utility.testString(map.get("orgId"))){
			sqlBulider.append("  AND b.root_org_id = "+map.get("orgId")+"\n" );
		}
		if(Utility.testString(map.get("pose_id"))){
			sqlBulider.append("  AND tm.dealer_org_id in ( SELECT r.region_id FROM Tr_Pose_Region r where r.pose_id = "+map.get("pose_id")+") \n" );
		}
		sqlBulider.append(" ORDER BY A.ID DESC");
		sql = sqlBulider.toString();
		PageResult<Map<String,Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
		return ps;
	}
	
	
	public Integer getUserRegionCount(Long userId){
		Integer i = 0;
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT COUNT(*) COUNT FROM TC_USER_REGION_RELATION WHERE USER_ID = ").append(userId);

		Map<String, Object> map = pageQueryMap(sql.toString(), null, getFunName());
		i = Integer.parseInt(String.valueOf(map.get("COUNT")));
		return i;
	}
	
	/**
	 * 查询索赔申请单信息
	 * 注意：现在语句只支持二级经销商,需要支持多级以上则需要调整
	 * @param crBean 查询条件
	 * @param curPage 当前页
	 * @param pageSize 每页记录数
	 * @return PageResult<Map<String,Object>> 索赔申请单信息
	 */
	public PageResult<Map<String,Object>> queryClaim(ClaimOrderBean orderBean,Integer curPage,Integer pageSize){
		
		StringBuilder sBuilder1 = new StringBuilder();
		StringBuilder sBuilder = new StringBuilder();
		List<Object> paramList = new ArrayList<Object>();
		
		//初始化索赔申请单查询条件
		this.createWhereMap("YIELDLY" ,"IN" ,orderBean.getYieldlys() , sBuilder1, paramList,2,"","A1");
		this.createWhereMap("YIELDLY" ,"=" ,orderBean.getYieldly() , sBuilder1, paramList,2,"","A1");
		this.createWhereMap("STATUS" ,"=" ,orderBean.getStatus() , sBuilder1, paramList,2,"","A1");
		this.createWhereMap("DEGRADATION_TYPE", "=", orderBean.getClaimZhishun(), sBuilder1, paramList, 2, "", "A1");
		this.createWhereMap("CLAIM_NO" ,"LIKE" ,orderBean.getClaimNo() , sBuilder1, paramList,2,"","A1");
		this.createWhereMap("RO_NO" ,"LIKE" ,orderBean.getRoNo() , sBuilder1, paramList,2,"","A1");
		this.createWhereMap("LINE_NO","=" ,orderBean.getLineNo(), sBuilder1, paramList,2,"","A1");
		this.createWhereMap("CLAIM_TYPE","=" ,orderBean.getClaimType() , sBuilder1, paramList,2,"","A1");
		this.createWhereMap("AUTH_CODE" ,"=" ,orderBean.getAuthCode(), sBuilder1, paramList,2,"","A1");
		this.createWhereMap("CREATE_DATE", ">=" ,orderBean.getApplyStartDate(), sBuilder1, paramList,1,"YYYY-MM-DD HH24:MI:SS","A1");
		this.createWhereMap("CREATE_DATE","<=" ,orderBean.getApplyEndDate() , sBuilder1, paramList,1,"YYYY-MM-DD HH24:MI:SS","A1");
		this.createWhereMap("DEALER_CODE" ,"IN" ,orderBean.getDealerCodes(), sBuilder, paramList,2,"","B");
		this.createWhereMap("DEALER_SHORTNAME" ,"LIKE" ,orderBean.getDealerName(), sBuilder, paramList,2,"","B");
		
		PageResult<Map<String, Object>> result = null;
		String sql = "";
		StringBuilder sqlBulider= new StringBuilder();
		sqlBulider.append("SELECT /*+ INDEX (A IDX_TT_APPLICATION_STATUS) */\n" );
		sqlBulider.append(" A.AUDITING_DATE, A.CLAIM_NO, B.DEALER_CODE, B.DEALER_SHORTNAME DEALER_NAME, A.RO_NO AS RO_NO, A.LINE_NO, A.CLAIM_TYPE,\n" );
		sqlBulider.append(" A.VIN, TO_CHAR(A.CREATE_DATE, 'YYYY-MM-DD') CREATE_DATE, A.STATUS, A.ID, A.REPAIR_TOTAL,\n" );
		sqlBulider.append(" (SELECT U.NAME\n" );
		sqlBulider.append("   FROM TC_USER U\n" );
		sqlBulider.append("   WHERE U.USER_ID = (SELECT W.CREATE_BY\n" );
		sqlBulider.append("                      FROM TT_AS_WR_APPAUTHITEM W\n" );
		sqlBulider.append("                      WHERE W.CREATE_DATE = (SELECT MAX(WA.CREATE_DATE) FROM TT_AS_WR_APPAUTHITEM WA WHERE WA.ID = A.ID) AND\n" );
		sqlBulider.append("                            W.ID = A.ID AND ROWNUM = 1)) AS AUTH_PERSON\n" );
		sqlBulider.append("FROM (SELECT *\n" );
		sqlBulider.append("       FROM TT_AS_WR_APPLICATION A1\n" );
		sqlBulider.append("       WHERE EXISTS (SELECT 1\n" );
		sqlBulider.append("              FROM TM_DEALER_BUSINESS_AREA TDBA\n" );
		sqlBulider.append("              WHERE TDBA.DEALER_ID = A1.DEALER_ID AND\n" );
		sqlBulider.append("                    TDBA.AREA_ID IN ("+orderBean.getAreaIds()+"))\n" );
		sqlBulider.append("       AND A1.STATUS IN ("+Constant.CLAIM_APPLY_ORD_TYPE_03+","+Constant.CLAIM_APPLY_ORD_TYPE_04+")\n");
		sqlBulider.append("       "+GetVinUtil.getVins(orderBean.getVin(), "A1") + "\n");
		if(StringUtil.notNull(orderBean.getApproveDate())){
			sqlBulider.append("       AND A1.auditing_date >= to_date('").append(orderBean.getApproveDate());
			sqlBulider.append(" 00:00:00','yyyy-MM-dd hh24:mi:ss')\n");
		}
		if(StringUtil.notNull(orderBean.getApproveDate2())){
			sqlBulider.append("       AND A1.auditing_date <= to_date('").append(orderBean.getApproveDate2());
			sqlBulider.append(" 23:59:59','yyyy-MM-dd hh24:mi:ss')\n");
		}
		if(StringUtil.notNull(orderBean.getApproveName())){
			sqlBulider.append("       AND A1.ID in\n");
			sqlBulider.append("      (select id\n");  
			sqlBulider.append("         from tt_as_wr_appauthitem wa\n");  
			sqlBulider.append("        where wa.create_by in\n");  
			sqlBulider.append("              (select u.user_id from tc_user u where u.name like '%"+orderBean.getApproveName()+"%'))\n");
		}
		sqlBulider.append("             "+sBuilder1.toString()+") A\n" );
		sqlBulider.append("             , TM_DEALER B\n" );
		sqlBulider.append("             , TM_DEALER_ORG_RELATION REL,\n" );
		sqlBulider.append("     (SELECT ORG_ID\n" );
		sqlBulider.append("       FROM TM_ORG ORG\n" );
		sqlBulider.append("       WHERE ORG.STATUS = "+Constant.STATUS_ENABLE+"\n" );
		sqlBulider.append("       START WITH ORG.ORG_ID = "+orderBean.getOrgId()+"\n" );
		sqlBulider.append("       CONNECT BY PRIOR ORG.ORG_ID = ORG.PARENT_ORG_ID) ORG\n" );
		sqlBulider.append("WHERE 1 = 1\n" );
		sqlBulider.append("AND A.DEALER_ID = B.DEALER_ID\n" );
		sqlBulider.append("AND (REL.DEALER_ID = B.DEALER_ID OR REL.DEALER_ID = B.PARENT_DEALER_D)\n" );
		sqlBulider.append("AND ORG.ORG_ID = REL.ORG_ID\n");
		sqlBulider.append(""+sBuilder.toString()+"");
		//sqlBulider.append("ORDER BY A.ID DESC");
		
		sql = sqlBulider.toString();
		
		result = (PageResult<Map<String, Object>>)this.pageQuery(sql,
																paramList,
																this.getClass().getName()+".queryClaim()",
																pageSize,
																curPage);
		return result;
	}
	
	/**
	 * 拼查询条件，如果页面查询过来不为空，则拼装到查询条件中
	 * @param param 参数列 对应数据库中字段
	 * @param value 参数值
	 * @param oper 操作符
	 * @param sBuilder 拼装条件容器
	 * @param paramList 参数列表
	 * @param dataType 数据类型
	 *        1 : 时间
	 *        2 ：其他
	 * @param dataFormat 数据格式，现在只有时间类型需要添加格式
	 * @param table 标明表名或别名
	 * @return
	 */
	private void createWhereMap(String param,String oper,String value,
			StringBuilder sBuilder,List<Object> paramList,int dataType,
			String dataFormat,String table){
		if(Utility.testString(value)){
			param = table + "." + param;
			if(dataType==1) {//时间
				sBuilder.append(" AND ").append(param).append(" ")
				.append(oper).append(" TO_DATE(" +"?" + ",'" + dataFormat + "')");
				paramList.add(value);
			}else if("IN".equalsIgnoreCase(oper)){
				StringTokenizer st = new StringTokenizer(value,",");
				
				boolean flag = true;
				while(st.hasMoreTokens()){
					String tempValue = st.nextToken();
					if(Utility.testString(tempValue)){
						if(flag)//保证只加一次AND COL IN (
							sBuilder.append(" AND ").append(param).append(" ").append(oper).append(" (");
						flag = false;
						sBuilder.append("?,");
						paramList.add(tempValue);
					}
				}
				if(!flag)
					sBuilder.append("'')");//加入后半个括号，同时多加一个空''
			}else{//其他
				sBuilder.append(" AND ").append(param).append(" ").append(oper).append(" ?");
				if("LIKE".equalsIgnoreCase(oper)){//模糊查询
					paramList.add("%" +value +"%");
				} else{
					paramList.add(value);
				}
			}
		}
		//sBuilder.append("\n");
	}
	
	/**
	 * 根据用户ID查询用户信息
	 * @param userId 用户ID
	 * @return TcUserPO
	 */
	public TcUserPO queryUserById(Long userId){
		TcUserPO conditionPO = new TcUserPO();
		conditionPO.setUserId(userId);
		List<PO> userList = this.select(conditionPO);
		
		TcUserPO resultPO = null;
		if(userList!=null && userList.size()>0){
			resultPO = (TcUserPO) userList.get(0);
		}
		return resultPO;
	}
	
	/**
	 * 更新索赔之配件信息
	 * @param partPO 需要更新的信息
	 * @param partId 索赔之配件ID
	 * @return
	 */
	public int updatePartsInfo(TtAsWrPartsitemPO partPO,Long partId){
		
		TtAsWrPartsitemPO conditionPO = new TtAsWrPartsitemPO();
		conditionPO.setPartId(partId);
		
		return this.update(conditionPO, partPO);
	}
	
	/**
	 * 更新索赔之工时信息
	 * @param labourPO 需要更新的信息
	 * @param labourId 索赔之工时ID
	 * @return
	 */
	public int updateLabourInfo(TtAsWrLabouritemPO labourPO,Long labourId){
		
		TtAsWrLabouritemPO conditionPO = new TtAsWrLabouritemPO();
		conditionPO.setLabourId(labourId);
		
		return this.update(conditionPO, labourPO);
	}
	
	/**
	 * 更新索赔之其他项目信息
	 * @param otherPO 需要更新的信息
	 * @param otherId 索赔之其他项目ID
	 * @return
	 */
	public int updateOtherInfo(TtAsWrNetitemPO otherPO,Long otherId){
		
		TtAsWrNetitemPO conditionPO = new TtAsWrNetitemPO();
		conditionPO.setNetitemId(otherId);
		
		return this.update(conditionPO, otherPO);
	}
	
	/**
	 * 更新索赔申请单信息
	 * @param claimPO 需要更新的信息
	 * @param claimId 索赔申请单ID
	 * @return
	 */
	public int updateClaimInfo(TtAsWrApplicationPO claimPO,Long claimId){
		
		TtAsWrApplicationPO conditionPO = new TtAsWrApplicationPO();
		conditionPO.setId(claimId);
		
		return this.update(conditionPO, claimPO);
	}
	
	
	//退回删除条码
	public void deleteBarcode(String id){
		String sql="delete from tt_as_wr_partsitem_barcode t1 where t1.part_id in(select part_id from tt_as_wr_partsitem t2 where t2.id="+id+")";
		delete(sql, null);
	}
	
	public PageResult<Map<String, Object>> queryAccAuditList(auditBean bean,int curPage, int pageSize){
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT ROWNUM NUM,r.region_name, a.ID, a.BALANCE_NO, a.DEALER_CODE, a.DEALER_NAME, a.CLAIM_COUNT,a.RETURN_AMOUNT,\n" );
		sql.append("       TO_CHAR(a.CREATE_DATE, 'YYYY-MM-DD') CREATE_DATE, a.STATUS, a.YIELDLY,a.BALANCE_AMOUNT,a.AMOUNT_SUM,TO_CHAR(t.BEGIN_BALANCE_DATE,'YYYY-MM-DD') AS BEGIN_BALANCE_DATE,TO_CHAR(t.END_BALANCE_DATE,'YYYY-MM-DD') AS END_BALANCE_DATE,\n" );
		sql.append("TO_CHAR(a.START_DATE,'YYYY-MM-DD') AS START_DATE,TO_CHAR(a.END_DATE,'YYYY-MM-DD') AS END_DATE,\n");
		sql.append("(SELECT COUNT(1) FROM TT_AS_WR_SPEFEE D WHERE D.CLAIMBALANCE_ID=A.ID) AS SP_COUNT,\n");
		sql.append("(SELECT COUNT(1) FROM TT_AS_WR_SPEFEE D WHERE D.CLAIMBALANCE_ID=A.ID AND D.STATUS IN("+Constant.SPEFEE_STATUS_06+","+Constant.SPEFEE_STATUS_05+")) AS SP_COUNT_AL\n");
		sql.append(" FROM TT_AS_WR_CLAIM_BALANCE a ,tm_dealer t,tm_region r\n");
		sql.append("WHERE 1=1\n");
		sql.append(" AND a.YIELDLY IN (").append(bean.getYieldlys()).append(") and a.dealer_id=t.dealer_id\n");
		sql.append(" and t.province_id = r.region_code\n");
		if(Utility.testString(bean.getDealerCode())){
			//用于对多个查询多个经销商，不可模糊sql.append("AND a.DEALER_CODE IN (").append(bean.getDealerCode()).append(")\n");
			sql.append("AND a.DEALER_CODE LIKE '%").append(bean.getDealerCode()).append("%'\n");
		}
		
		if(Utility.testString(bean.getStartDate())){
			sql.append("AND a.CREATE_DATE >= TO_DATE('").append(bean.getStartDate()).append("', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(Utility.testString(bean.getEndDate())){
			sql.append("AND a.CREATE_DATE <= TO_DATE('").append(bean.getEndDate()).append("', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(Utility.testString(bean.getYieldly())){
			sql.append("AND a.YIELDLY = ").append(bean.getYieldly()).append("\n");
		}
		if(Utility.testString(bean.getStatus())){
			sql.append("AND a.STATUS = ").append(bean.getStatus()).append("\n");
		}else{
			sql.append("and a.status not in ("+Constant.ACC_STATUS_07+","+Constant.ACC_STATUS_08+","+Constant.ACC_STATUS_09+")\n");
		}
		if(Utility.testString(bean.getBalanceNo())){
			sql.append("AND a.BALANCE_NO LIKE '%").append(bean.getBalanceNo()).append("%'\n");
		}

		sql.append("ORDER BY a.ID DESC");
		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
		if(ps.getTotalRecords()>0){
			List list=ps.getRecords();
			String sql2="SELECT /*+RULE*/ COUNT (1) as count FROM tt_as_wr_application b, tr_balance_claim c WHERE b.ID = c.claim_id and c.balance_id=?";
			String sql3="SELECT /*+RULE*/ COUNT (1) as count FROM tt_as_wr_application b, tr_balance_claim c WHERE b.ID = c.claim_id AND b.status = "+Constant.CLAIM_APPLY_ORD_TYPE_07+" AND c.balance_id = ? ";
			for(int i=0;i<list.size();i++)
			{
				Map map=(Map)list.get(i);
				List listPar=new ArrayList();
				listPar.add(((BigDecimal)map.get("ID")).longValue());
				Map map2=this.pageQueryMap(sql2, listPar, this.getFunName());
				Map map3=this.pageQueryMap(sql3, listPar, this.getFunName());
				map.put("COUNT_APPL",((BigDecimal)map2.get("COUNT")).longValue());
				map.put("CLAIM_COUNT_AL",((BigDecimal)map3.get("COUNT")).longValue());
			}
		}
		return ps;
	}

	public PageResult<Map<String, Object>> queryAccAuditById(auditBean bean,int curPage, int pageSize){
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT ROWNUM NUM, A.ID CLAIM_ID, A.CLAIM_NO, A.YIELDLY,\n" );
		sql.append("       TO_CHAR(A.REPORT_DATE, 'YYYY-MM-DD') REPORT_DATE,\n" );
		sql.append("       TO_CHAR(A.AUDITING_DATE, 'YYYY-MM-DD') AUDITING_DATE,\n" );
		sql.append("       A.SERIES_NAME, A.MODEL_NAME, A.VIN, C.BALANCE_ID,\n" );
		//sql.append("     B.DEALER_SHORTNAME DEALER_NAME,A.CLAIM_TYPE,A.STATUS,A.AUTH_CODE,A.BALANCE_AMOUNT,A.REPAIR_TOTAL,\n" );
		/******MOD BY LIUXH 20101203 索赔单申请费用加上追加工时******/
		sql.append("     B.DEALER_SHORTNAME DEALER_NAME,A.CLAIM_TYPE,A.STATUS,A.AUTH_CODE,A.BALANCE_AMOUNT,(NVL(A.REPAIR_TOTAL,0)) AS REPAIR_TOTAL,\n" );
		/******MOD BY LIUXH 20101203 索赔单申请费用加上追加工时******/
		sql.append("     (CASE WHEN A.STATUS = "+Constant.CLAIM_APPLY_ORD_TYPE_08+" AND A.AUTH_CODE = '"+CommonUtils.checkNull(bean.getAuthCodeOrder())+"' THEN 1 ELSE 2 END) ORDERSTR\n" );
		sql.append("FROM TT_AS_WR_APPLICATION A, TM_DEALER B, TR_BALANCE_CLAIM C\n" );
		sql.append("WHERE A.DEALER_ID = B.DEALER_ID\n" );
		sql.append("AND A.ID = C.CLAIM_ID\n" );
		
		List<Object> params = new ArrayList<Object>();
		if(Utility.testString(bean.getDealerCode())){
			sql.append(Utility.getConSqlByParamForEqual(bean.getDealerCode(), params, "B", "DEALER_CODE"));
		}
		if(Utility.testString(bean.getVin())){
			sql.append("AND A.VIN LIKE '%").append(bean.getVin()).append("%'\n");
		}
		if(Utility.testString(bean.getClaimNo())){
			sql.append("AND A.CLAIM_NO LIKE UPPER('%").append(bean.getClaimNo()).append("%')\n");
		}
		if(Utility.testString(bean.getModelCode())){
			sql.append("AND A.MODEL_CODE LIKE '%").append(bean.getModelCode()).append("%'\n");
		}
		if(Utility.testString(bean.getClaimStatus())){
			sql.append("AND A.STATUS = '").append(bean.getClaimStatus()).append("'\n");
		}
		if(Utility.testString(bean.getAuthCode())){
			sql.append("AND A.AUTH_CODE = '").append(bean.getAuthCode()).append("'\n");
		}
		if(Utility.testString(bean.getClaimType())){
			sql.append("AND A.CLAIM_TYPE = '").append(bean.getClaimType()).append("'\n");
		}
		if(Utility.testString(bean.getStartDate())){
			sql.append("AND A.CREATE_DATE >= TO_DATE('").append(bean.getStartDate()).append("', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(Utility.testString(bean.getEndDate())){
			sql.append("AND A.CREATE_DATE <= TO_DATE('").append(bean.getEndDate()).append("', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(!CommonUtils.checkNull(bean.getCantNotAudit()).equals("")){
            sql.append("AND A.ID NOT IN("+bean.getCantNotAudit()+") ");
        }
		sql.append("AND C.BALANCE_ID = ").append(bean.getId()).append("\n");
		sql.append("ORDER BY A.CLAIM_NO,A.CLAIM_TYPE , a.AUDITING_DATE ASC");

		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), params, getFunName(), pageSize, curPage);
		return ps;
	}
	public PageResult<Map<String, Object>> queryAccAuditSkipById(auditBean bean,int curPage, int pageSize){
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT ROWNUM NUM, A.ID CLAIM_ID, A.CLAIM_NO, A.YIELDLY,\n" );
		sql.append("       TO_CHAR(A.REPORT_DATE, 'YYYY-MM-DD') REPORT_DATE,\n" );
		sql.append("       TO_CHAR(A.AUDITING_DATE, 'YYYY-MM-DD') AUDITING_DATE,\n" );
		sql.append("       A.SERIES_NAME, A.MODEL_NAME, A.VIN, C.BALANCE_ID,\n" );
		//sql.append("     B.DEALER_SHORTNAME DEALER_NAME,A.CLAIM_TYPE,A.STATUS,A.AUTH_CODE,A.BALANCE_AMOUNT,A.REPAIR_TOTAL,\n" );
		/******MOD BY LIUXH 20101203 索赔单申请费用加上追加工时******/
		sql.append("     B.DEALER_SHORTNAME DEALER_NAME,A.CLAIM_TYPE,A.STATUS,A.AUTH_CODE,A.BALANCE_AMOUNT,(NVL(A.REPAIR_TOTAL,0)+NVL(A.APPENDLABOUR_AMOUNT,0)) AS REPAIR_TOTAL,\n" );
		/******MOD BY LIUXH 20101203 索赔单申请费用加上追加工时******/
		sql.append("     (CASE WHEN A.STATUS = "+Constant.CLAIM_APPLY_ORD_TYPE_08+" AND A.AUTH_CODE = '"+CommonUtils.checkNull(bean.getAuthCodeOrder())+"' THEN 1 ELSE 2 END) ORDERSTR\n" );
		sql.append("FROM TT_AS_WR_APPLICATION A, TM_DEALER B, TR_BALANCE_CLAIM C\n" );
		sql.append("WHERE A.DEALER_ID = B.DEALER_ID\n" );
		sql.append("AND A.ID = C.CLAIM_ID\n" );
		
		List<Object> params = new ArrayList<Object>();
		if(Utility.testString(bean.getDealerCode())){
			sql.append(Utility.getConSqlByParamForEqual(bean.getDealerCode(), params, "B", "DEALER_CODE"));
		}
		if(Utility.testString(bean.getVin())){
			sql.append("AND A.VIN LIKE '%").append(bean.getVin()).append("%'\n");
		}
		if(Utility.testString(bean.getClaimNo())){
			sql.append("AND A.CLAIM_NO LIKE UPPER('%").append(bean.getClaimNo()).append("%')\n");
		}
		if(Utility.testString(bean.getModelCode())){
			sql.append("AND A.MODEL_CODE LIKE '%").append(bean.getModelCode()).append("%'\n");
		}
		if(Utility.testString(bean.getClaimStatus())){
			sql.append("AND A.STATUS = '").append(bean.getClaimStatus()).append("'\n");
		}
		if(Utility.testString(bean.getAuthCode())){
			sql.append("AND A.AUTH_CODE = '").append(bean.getAuthCode()).append("'\n");
		}
		if(Utility.testString(bean.getClaimType())){
			sql.append("AND A.CLAIM_TYPE = '").append(bean.getClaimType()).append("'\n");
		}
		if(Utility.testString(bean.getStartDate())){
			sql.append("AND A.CREATE_DATE >= TO_DATE('").append(bean.getStartDate()).append("', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(Utility.testString(bean.getEndDate())){
			sql.append("AND A.CREATE_DATE <= TO_DATE('").append(bean.getEndDate()).append("', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(!bean.getCantNotAudit().equals("")){
			sql.append("AND A.ID NOT IN("+bean.getCantNotAudit()+")");
		}
		sql.append("AND C.BALANCE_ID = ").append(bean.getId()).append("\n");
		sql.append("ORDER BY a.AUDITING_DATE ASC");

		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), params, getFunName(), pageSize, curPage);
		return ps;
	}
	
	public String getClaimIds(String id){
		String climeId = "";
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT CLAIM_ID\n" );
		sql.append("FROM TR_BALANCE_CLAIM\n" );
		sql.append("WHERE BALANCE_ID = ").append(id).append("\n");
		List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
		if(list!=null&&list.size()>0){
			for(int i = 0 ; i < list.size(); i++){
				if(!climeId.equals("")){
					climeId = "," + list.get(i).get("CLAIM_ID");
				}else{
					climeId = String.valueOf(list.get(i).get("CLAIM_ID"));
				}
			}
		}
		return climeId;
	}
	
	public void updateClaimStatus(String cliamId){
		StringBuffer sql= new StringBuffer();
		sql.append("UPDATE TT_AS_WR_APPLICATION\n" );
		sql.append("SET STATUS = ").append(Constant.CLAIM_APPLY_ORD_TYPE_07).append("\n"); 
		sql.append("WHERE ID IN (").append(cliamId).append(")\n");

		update(sql.toString(), null);
	}
	
	/**
	 * 批量审核通过，将对应结算单中的索赔单批量通过
	 * 同时标识对应结算单审核通过
	 * @param balanceId 结算单ID
	 */
	@Deprecated
	public void modifyClaimStatus(Long balanceId){
		//更新索赔单状态
		StringBuilder sql= new StringBuilder();
		sql.append("UPDATE TT_AS_WR_APPLICATION A\n" );
		sql.append("   SET A.STATUS = ?\n" );
		sql.append(" WHERE 1 = 1\n" );
		sql.append("   AND EXISTS (SELECT B.ID\n" );
		sql.append("          FROM TR_BALANCE_CLAIM B\n" );
		sql.append("         WHERE B.CLAIM_ID = A.ID\n" );
		sql.append("           AND B.BALANCE_ID = ?)");

		List<Object> paramList = new ArrayList<Object>();
		paramList.add(Constant.CLAIM_APPLY_ORD_TYPE_07);
		paramList.add(balanceId);
		
		this.update(sql.toString(), paramList);
		
		//更新结算单同索赔单中关系状态
		StringBuilder sql2= new StringBuilder();
		sql2.append("UPDATE TR_BALANCE_CLAIM\n" );
		sql2.append("   SET STATUS = ?\n" );
		sql2.append(" WHERE 1 = 1\n" );
		sql2.append("   AND BALANCE_ID = ?");
		
		List<Object> paramList2 = new ArrayList<Object>();
		paramList2.add(Constant.STATUS_ENABLE);
		paramList2.add(balanceId);

		this.update(sql2.toString(), paramList2);
	}
	
	/**
	 * 查询结算单信息
	 * @param balanceId 结算单ID
	 * @return
	 */
	public TtAsWrClaimBalancePO queryBalanceOrderById(Long balanceId){
		TtAsWrClaimBalancePO resultPO = new TtAsWrClaimBalancePO();
		TtAsWrClaimBalancePO conditionPO = new TtAsWrClaimBalancePO();
		conditionPO.setId(balanceId);
		List<PO> resultList = this.select(conditionPO);
		if(resultList!=null && resultList.size()>0)
			resultPO = (TtAsWrClaimBalancePO)resultList.get(0);
		return resultPO;
	}
	
	/**
	 * 更新索赔单中工时、配件和其他项目的结算金额
	 * 注：只有对应工时和配件、其他项目单项审核同意才能修改
	 * @param claimId 索赔单ID
	 * @param isCheckAgreeStatus 是否判断 授权审核 状态（IS_AGREE） true :使用，false 不使用
	 */
	public void modifyClaimBalanceDetailAmount(Long claimId,boolean isCheckAgreeStatus){
		
		StringBuilder sql= new StringBuilder();
		sql.append("UPDATE TT_AS_WR_APPLICATION A\n" );
		sql.append("   SET A.BALANCE_LABOUR_AMOUNT  = (SELECT NVL(SUM(B.BALANCE_AMOUNT), 0)\n" );
		sql.append("                                     FROM TT_AS_WR_LABOURITEM B\n" );
		sql.append("                                    WHERE B.ID = A.ID\n" );
		if(isCheckAgreeStatus)
			sql.append("                                    AND B.IS_AGREE = "+Constant.IF_TYPE_YES +"\n");
		sql.append("),\n");
		sql.append("       A.BALANCE_PART_AMOUNT    = (SELECT NVL(SUM(B.BALANCE_AMOUNT), 0)\n" );
		sql.append("                                     FROM TT_AS_WR_PARTSITEM B\n" );
		sql.append("                                    WHERE B.ID = A.ID\n" );
		if(isCheckAgreeStatus)
			sql.append("                                    AND B.IS_AGREE = "+Constant.IF_TYPE_YES +"\n");
		sql.append("),\n");
		sql.append("       A.BALANCE_NETITEM_AMOUNT = (SELECT NVL(SUM(B.BALANCE_AMOUNT), 0) FROM TT_AS_WR_NETITEM B\n" );
		sql.append("                                    WHERE B.ID = A.ID\n" );
		if(isCheckAgreeStatus)
			sql.append("                                    AND B.IS_AGREE = "+Constant.IF_TYPE_YES +"\n");
		sql.append(")\n");
		sql.append("WHERE 1=1\n" );
		sql.append("AND A.ID = ?");
		
		List<Object> params = new ArrayList<Object>();
		params.add(claimId);
		
		this.update(sql.toString(), params);
	}
	
public void modifyClaimBalanceDetailAmount2(Long claimId,boolean isCheckAgreeStatus){
		
		StringBuilder sql= new StringBuilder();
		sql.append("UPDATE TT_AS_WR_APPLICATION A\n" );
		sql.append("   SET A.BALANCE_LABOUR_AMOUNT  = (SELECT NVL(SUM(B.BALANCE_AMOUNT), 0)\n" );
		sql.append("                                     FROM TT_AS_WR_LABOURITEM B\n" );
		sql.append("                                    WHERE B.ID = A.ID\n" );
		if(isCheckAgreeStatus)
			sql.append("                                    AND B.IS_AGREE = "+Constant.IF_TYPE_YES +"\n");
		sql.append("),\n");
		sql.append("       A.BALANCE_PART_AMOUNT    = (SELECT NVL(SUM(B.BALANCE_AMOUNT), 0)\n" );
		sql.append("                                     FROM TT_AS_WR_PARTSITEM B\n" );
		sql.append("                                    WHERE B.ID = A.ID\n" );
		if(isCheckAgreeStatus)
			sql.append("                                    AND B.IS_AGREE = "+Constant.IF_TYPE_YES +"\n");
		sql.append("),\n");
		sql.append("       A.BALANCE_NETITEM_AMOUNT = (SELECT NVL(SUM(B.BALANCE_AMOUNT), 0) FROM TT_AS_WR_NETITEM B\n" );
		sql.append("                                    WHERE B.ID = A.ID\n" );
		if(isCheckAgreeStatus)
			sql.append("                                    AND B.IS_AGREE = "+Constant.IF_TYPE_YES +"\n");
		sql.append(")\n");
		sql.append("WHERE 1=1\n" );
		sql.append("AND A.ID = ?");
		
		List<Object> params = new ArrayList<Object>();
		params.add(claimId);
		
		this.update(sql.toString(), params);
	}
	
	/**
	 * 更新索赔单的结算金额
	 * @param claimId 索赔单ID
	 * @param isCheckAgreeStatus 是否判断 授权审核 状态（IS_AGREE） true :使用，false 不使用
	 */
	public void modifyClaimBalanceAmount(Long claimId,boolean isCheckAgreeStatus){
		
		List<Object> params = new ArrayList<Object>();
		params.add(claimId);
		
		StringBuilder sql1= new StringBuilder();
		//更新索赔单中配件、工时和其他项目的结算金额和数量
		sql1.append("UPDATE TT_AS_WR_APPLICATION A\n" );
		sql1.append("   SET (A.BALANCE_LABOUR_AMOUNT, A.LABOUR_HOURS) = (SELECT NVL(SUM(B.BALANCE_AMOUNT),0),NVL(SUM(B.BALANCE_QUANTITY),0)\n" );
		sql1.append("                                                                             FROM TT_AS_WR_LABOURITEM B\n" );
		sql1.append("                                    WHERE B.ID = A.ID\n" );
		if(isCheckAgreeStatus)
			sql1.append("                                    AND B.IS_AGREE = "+Constant.IF_TYPE_YES +"\n");
		sql1.append("),\n");
		sql1.append("       (A.BALANCE_PART_AMOUNT, A.PARTS_COUNT) = (SELECT NVL(SUM(C.BALANCE_AMOUNT),0),NVL(SUM(C.BALANCE_QUANTITY),0)\n" );
		sql1.append("                                                                          FROM TT_AS_WR_PARTSITEM C\n" );
		sql1.append("                                    WHERE C.ID = A.ID\n" );
		if(isCheckAgreeStatus)
			sql1.append("                                    AND C.IS_AGREE = "+Constant.IF_TYPE_YES +"\n");
		sql1.append("),\n");sql1.append("       A.BALANCE_NETITEM_AMOUNT = (SELECT NVL(SUM(D.BALANCE_AMOUNT), 0)\n" );
		sql1.append("                                     FROM TT_AS_WR_NETITEM D\n" );
		sql1.append("                                    WHERE D.ID = A.ID\n" );
		if(isCheckAgreeStatus)
			sql1.append("                                    AND D.IS_AGREE = "+Constant.IF_TYPE_YES +"\n");
		sql1.append(")\n");
		sql1.append(" WHERE A.ID = ?");
		this.update(sql1.toString(), params);
		
		StringBuilder sql= new StringBuilder();
		sql.append("UPDATE TT_AS_WR_APPLICATION A\n" );
		sql.append("   SET A.BALANCE_AMOUNT = (NVL(A.BALANCE_LABOUR_AMOUNT, 0) +\n" );
		sql.append("                          NVL(A.BALANCE_PART_AMOUNT, 0) +\n" );
		sql.append("                          NVL(A.BALANCE_NETITEM_AMOUNT, 0) +\n" );
		sql.append("                          NVL(A.FREE_M_PRICE, 0) +\n" );
		sql.append("                          NVL(A.CAMPAIGN_FEE, 0) +\n" );
		sql.append("                          NVL(A.APPENDLABOUR_AMOUNT, 0))\n" );
		sql.append(" WHERE A.ID = ?");
        
		this.update(sql.toString(), params);
	}
	
	
	public void modifyClaimBalanceAmount2(Long claimId,boolean isCheckAgreeStatus){
		List<Object> params = new ArrayList<Object>();
		params.add(claimId);
		StringBuilder sql2= new StringBuilder();
		sql2.append("update TT_AS_WR_APPLICATION wa \n");
		sql2.append("   set wa.labour_amount  = wa.balance_labour_amount,\n" );
		sql2.append("       wa.part_amount    = wa.balance_part_amount,\n" );
		sql2.append("       wa.netitem_amount = wa.balance_netitem_amount,\n" );
		sql2.append("       wa.repair_total = wa.balance_amount,\n" );
		sql2.append("wa.std_labour_amount = wa.balance_labour_amount \n");
		sql2.append("       where wa.id=?\n" );
		sql2.append("\n" );
		sql2.append("");
		this.update(sql2.toString(), params);
		
	}
	/**
	 * 根据索赔单Id查询索赔单明细
	 * @param claimId 索赔单ID
	 * @return
	 */
	public TtAsWrApplicationExtPO queryClaimOrderDetailById(String claimId){
		StringBuilder sql= new StringBuilder();
		sql.append("SELECT A.*,\n" );
		sql.append("       B.DEALER_CODE,\n" );
		sql.append("       F.ACTIVITY_NAME,\n" );
		sql.append("       E.CODE_DESC CLAIM_NAME,\n" );
		sql.append("       D.GROUP_NAME PACKAGE_NAME,C.PURCHASED_DATE PURCHASED_DATE,\n" );
		sql.append("       c.product_date,c.color,b.is_dqv,b.phone,C.LICENSE_NO,o.model");
		sql.append("  FROM TT_AS_WR_APPLICATION   A,\n" );
		sql.append("       TM_DEALER              B,\n" );
		sql.append("       TM_VEHICLE             C,\n" );
		sql.append("       TM_VHCL_MATERIAL_GROUP D,\n" );
		sql.append("  TT_AS_ACTIVITY F, ");
		sql.append("       TC_CODE                E,tt_as_repair_order o \n" );
		sql.append(" WHERE 1 = 1 and o.ro_no = a.ro_no \n" );
		sql.append("   AND nvl(a.second_dealer_id,a.dealer_id) = B.DEALER_ID(+)\n" );
		sql.append("   AND A.VIN = C.VIN(+)\n" );
		sql.append("   and A.CAMPAIGN_CODE = F.ACTIVITY_CODE(+) ");
		sql.append("   AND C.PACKAGE_ID = D.GROUP_ID(+)\n" );
		sql.append("   AND A.CLAIM_TYPE = E.CODE_ID(+)\n" );
		sql.append("   AND A.ID = ?");

		List<Object> params = new ArrayList<Object>();
		params.add(CommonUtils.checkNull(claimId));
		
		List<TtAsWrApplicationExtPO> claimList = this.select(TtAsWrApplicationExtPO.class,sql.toString(), params);
		TtAsWrApplicationExtPO resultPO = new TtAsWrApplicationExtPO();
		
		if(claimList!=null && claimList.size()>0)
			resultPO = claimList.get(0);
		
		return resultPO;
	}
	
	//zhumingwei 2011-03-04
	public List<Map<String, Object>> getAuthInfo(String id){
		StringBuffer sql= new StringBuffer();
		sql.append("select a.approval_person\n" );
		sql.append("from tt_as_wr_appauthitem a\n" );
		sql.append("where a.approval_date =\n" );
		sql.append("(select max(b.approval_date) from tt_as_wr_appauthitem b where b.approval_result="+Constant.CLAIM_APPLY_ORD_TYPE_04+" and a.id=b.id) and a.id="+id+"\n" );
		List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
		return list;
	}
	
	//zhumingwei 2011-03-04
	public String countRepairTimes(String id){
		StringBuffer sql= new StringBuffer();
		sql.append("select count(1) as repair_times from Tt_As_Wr_Application wa where wa.vin = (select vin from Tt_As_Wr_Application where id="+id+")\n" );
		sql.append("and wa.create_date<=(select create_date from Tt_As_Wr_Application where id="+id+" and claim_type in (10661001,10661007,10661009))");
		List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
		String count = ((Map)list.get(0)).get("REPAIR_TIMES").toString();
		return count;
	}
	
	//查询服务活动代码与名称
	public List getActivity(String id){
		StringBuffer sql = new StringBuffer("\n");
		sql.append("select a.activity_code,a.activity_name\n");
		sql.append("  from tt_as_activity a, tt_as_wr_application b, tt_as_repair_order c\n");  
		sql.append(" where b.ro_no = c.ro_no\n");  
		sql.append("   and c.cam_code = a.activity_code\n");  
		sql.append("   and b.id ="+id);
		return  this.select(TtAsActivityPO.class,sql.toString(),null);
	}
	
	/**
	 * Iverson 2010-11-25
	 * @param claimId 索赔单ID
	 * @return
	 */
	public TtAsWrApplicationExtPO queryClaimOrderDetailById11(String claimId){
		StringBuilder sql= new StringBuilder();
		sql.append("SELECT A.*,\n" );
		sql.append("       B.DEALER_CODE,\n" );
		sql.append("       E.CODE_DESC CLAIM_NAME,\n" );
		sql.append("       D.GROUP_NAME PACKAGE_NAME,C.PURCHASED_DATE PURCHASED_DATE\n" );
		sql.append("  FROM tt_as_wr_application_backup   A,\n" );
		sql.append("       TM_DEALER              B,\n" );
		sql.append("       TM_VEHICLE             C,\n" );
		sql.append("       TM_VHCL_MATERIAL_GROUP D,\n" );
		sql.append("       TC_CODE                E\n" );
		sql.append(" WHERE 1 = 1\n" );
		sql.append("   AND A.DEALER_ID = B.DEALER_ID(+)\n" );
		sql.append("   AND A.VIN = C.VIN(+)\n" );
		sql.append("   AND C.PACKAGE_ID = D.GROUP_ID(+)\n" );
		sql.append("   AND A.CLAIM_TYPE = E.CODE_ID(+)\n" );
		sql.append("   AND A.ID = ?");

		List<Object> params = new ArrayList<Object>();
		params.add(CommonUtils.checkNull(claimId));
		
		List<TtAsWrApplicationExtPO> claimList = this.select(TtAsWrApplicationExtPO.class,sql.toString(), params);
		TtAsWrApplicationExtPO resultPO = new TtAsWrApplicationExtPO();
		
		if(claimList!=null && claimList.size()>0)
			resultPO = claimList.get(0);
		
		return resultPO;
	}
	
	/**
	 * 当索赔单类型为"审核退回"将子表金额和状态更新到是和申请时金额
	 */
	public void modifyBackClaimDetail(Long claimId){
		StringBuilder sql= new StringBuilder();
		//将配件信息更新回去
		sql.append("UPDATE TT_AS_WR_PARTSITEM A\n" );
		sql.append("SET A.BALANCE_AMOUNT = A.AMOUNT,\n" );
		sql.append("A.BALANCE_QUANTITY = A.QUANTITY,\n" );
		sql.append("A.IS_AGREE = "+Constant.IF_TYPE_YES+"\n");
		sql.append("WHERE 1=1\n" );
		sql.append("AND EXISTS (SELECT B.ID FROM TT_AS_WR_APPLICATION B WHERE B.ID = A.ID\n" );
		sql.append("AND B.STATUS = "+Constant.CLAIM_APPLY_ORD_TYPE_06+")\n" );
		sql.append("AND A.ID = "+claimId);
		//将工时信息更新回去
		StringBuilder sql1= new StringBuilder();
		sql1.append("UPDATE TT_AS_WR_LABOURITEM A\n" );
		sql1.append("SET A.BALANCE_AMOUNT = A.LABOUR_AMOUNT,\n" );
		sql1.append("A.BALANCE_QUANTITY = A.LABOUR_HOURS,\n" );
		sql1.append("A.IS_AGREE = "+Constant.IF_TYPE_YES+"\n");
		sql1.append("WHERE 1=1\n" );
		sql1.append("AND EXISTS (SELECT B.ID FROM TT_AS_WR_APPLICATION B WHERE B.ID = A.ID\n" );
		sql1.append("AND B.STATUS = "+Constant.CLAIM_APPLY_ORD_TYPE_06+")\n" );
		sql1.append("AND A.ID = "+claimId);
		//将其他项目信息更新回去
		StringBuilder sql2= new StringBuilder();
		sql2.append("UPDATE TT_AS_WR_NETITEM A\n" );
		sql2.append("SET A.BALANCE_AMOUNT = A.AMOUNT,\n" );
		sql2.append("A.IS_AGREE = "+Constant.IF_TYPE_YES+"\n");
		sql2.append("WHERE 1=1\n" );
		sql2.append("AND EXISTS (SELECT B.ID FROM TT_AS_WR_APPLICATION B WHERE B.ID = A.ID\n" );
		sql2.append("AND B.STATUS = "+Constant.CLAIM_APPLY_ORD_TYPE_06+")\n" );
		sql2.append("AND A.ID = "+claimId);

		this.update(sql.toString(), null);
		this.update(sql1.toString(), null);
		this.update(sql2.toString(), null);
	}
	
	public void modifyBackClaimDetail2(Long claimId){
		StringBuilder sql= new StringBuilder();
		//将配件信息更新回去
		sql.append("UPDATE TT_AS_WR_PARTSITEM A\n" );
		sql.append("SET A.BALANCE_AMOUNT = A.AMOUNT,\n" );
		sql.append("A.BALANCE_QUANTITY = A.QUANTITY\n" );
		//sql.append("A.IS_AGREE = "+Constant.IF_TYPE_NO+"\n");
		sql.append("WHERE 1=1\n" );
		sql.append("AND EXISTS (SELECT B.ID FROM TT_AS_WR_APPLICATION B WHERE B.ID = A.ID\n" );
		sql.append("AND B.STATUS = "+Constant.CLAIM_APPLY_ORD_TYPE_06+")\n" );
		sql.append("AND A.ID = "+claimId);
		//将工时信息更新回去
		StringBuilder sql1= new StringBuilder();
		sql1.append("UPDATE TT_AS_WR_LABOURITEM A\n" );
		sql1.append("SET A.BALANCE_AMOUNT = A.LABOUR_AMOUNT,\n" );
		sql1.append("A.BALANCE_QUANTITY = A.LABOUR_HOURS\n" );
	//	sql1.append("A.IS_AGREE = "+Constant.IF_TYPE_NO+"\n");
		sql1.append("WHERE 1=1\n" );
		sql1.append("AND EXISTS (SELECT B.ID FROM TT_AS_WR_APPLICATION B WHERE B.ID = A.ID\n" );
		sql1.append("AND B.STATUS = "+Constant.CLAIM_APPLY_ORD_TYPE_06+")\n" );
		sql1.append("AND A.ID = "+claimId);
		//将其他项目信息更新回去
		StringBuilder sql2= new StringBuilder();
		sql2.append("UPDATE TT_AS_WR_NETITEM A\n" );
		sql2.append("SET A.BALANCE_AMOUNT = A.AMOUNT \n" );
		//sql2.append("A.IS_AGREE = "+Constant.IF_TYPE_NO+"\n");
		sql2.append("WHERE 1=1\n" );
		sql2.append("AND EXISTS (SELECT B.ID FROM TT_AS_WR_APPLICATION B WHERE B.ID = A.ID\n" );
		sql2.append("AND B.STATUS = "+Constant.CLAIM_APPLY_ORD_TYPE_06+")\n" );
		sql2.append("AND A.ID = "+claimId);

		this.update(sql.toString(), null);
		this.update(sql1.toString(), null);
		this.update(sql2.toString(), null);
	}
	
	/**
	 * Iverson add with 2010-12-16
	 * 废弃索赔单是首先查询此索赔单已经被生成结算单没有
	 * @Title: isCount
	 */
	public int isCount(String id){
		int count = 0;
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT COUNT(*) COUNT FROM tr_balance_claim WHERE claim_id ='"+id+"' ");
		Map<String, Object> map = pageQueryMap(sql.toString(), null, getFunName());
		count = Integer.parseInt(String.valueOf(map.get("COUNT")));
		return count;
	}
	/**************************微车流程修改keviyin*****************************************/
	/*******************************add by kevinyin 20110413****************************************************/
	/**
	 * 查询所有符合条件的索赔单
	 */
	public PageResult<Map<String, Object>> queryAuditingClaim(auditBean bean,String areaIds,String invoiceStatus,int curPage, int pageSize){
		StringBuffer sql= new StringBuffer();
		if(bean.getClaimStatus()==null){
			sql.append("SELECT  /*+ORDERED*/ROWNUM NUM, A.ID CLAIM_ID, A.CLAIM_NO, A.YIELDLY,\n" );
		}
		if(bean.getClaimStatus()!=null&&bean.getClaimStatus().equals("10791008")){
		sql.append("SELECT  /*+RULE+*/ROWNUM NUM, A.ID CLAIM_ID, A.CLAIM_NO, A.YIELDLY,\n" );
		}
		if(bean.getClaimStatus()!=null&&bean.getClaimStatus().equals("10791007")&&bean.getDealerCode()==null){
			sql.append("    SELECT  /*+ORDERED+*/ROWNUM NUM, A.ID CLAIM_ID, A.CLAIM_NO, A.YIELDLY,\n" );
		}
		if(bean.getClaimStatus()!=null&&bean.getClaimStatus().equals("10791007")&&bean.getDealerCode()!=null){
			sql.append("SELECT  /*+ORDERED   */ROWNUM NUM, A.ID CLAIM_ID, A.CLAIM_NO, A.YIELDLY,\n" );
		}
		sql.append("       TO_CHAR(A.REPORT_DATE, 'YYYY-MM-DD') REPORT_DATE,\n" );
		sql.append("       TO_CHAR(A.AUDITING_DATE, 'YYYY-MM-DD') AUDITING_DATE,\n" );
		sql.append("       A.SERIES_NAME, A.MODEL_NAME, A.VIN,A.IS_INVOICE,\n" );
		//sql.append("     B.DEALER_SHORTNAME DEALER_NAME,A.CLAIM_TYPE,A.STATUS,A.AUTH_CODE,A.BALANCE_AMOUNT,A.REPAIR_TOTAL,\n" );
		/******MOD BY LIUXH 20101203 索赔单申请费用加上追加工时******/
		sql.append("     B.DEALER_SHORTNAME DEALER_NAME,A.CLAIM_TYPE,A.STATUS,A.AUTH_CODE,A.BALANCE_AMOUNT,(NVL(A.REPAIR_TOTAL,0)) AS REPAIR_TOTAL,\n" );
		/******MOD BY LIUXH 20101203 索赔单申请费用加上追加工时******/
		sql.append("     (CASE WHEN A.STATUS = "+Constant.CLAIM_APPLY_ORD_TYPE_08+" AND A.AUTH_CODE = '"+CommonUtils.checkNull(bean.getAuthCode())+"' THEN 1 ELSE 2 END) ORDERSTR\n" );
		sql.append("FROM tc_user t,TM_DEALER B,TT_AS_WR_APPLICATION A \n" );
		sql.append("WHERE A.DEALER_ID = B.DEALER_ID  \n" );
		sql.append("and t.user_id="+bean.getAuthCodeOrder()+"\n");
		sql.append("and  exists  (select * from TC_USER_REGION_RELATION r where r.user_id=t.user_id and b.province_id = r.region_code)\n");
		List<Object> params = new ArrayList<Object>();
		if(Utility.testString(bean.getDealerCode())){
			sql.append(Utility.getConSqlByParamForEqual(bean.getDealerCode(), params, "B", "DEALER_CODE"));
		}
		if(Utility.testString(bean.getVin())){
			sql.append("AND A.VIN LIKE '%").append(bean.getVin()).append("%'\n");
		}
		if(Utility.testString(bean.getClaimNo())){
			sql.append("AND A.CLAIM_NO LIKE UPPER('").append(bean.getClaimNo()).append("%')\n");
		}
		if(Utility.testString(bean.getModelCode())){
			sql.append("AND A.MODEL_CODE LIKE '%").append(bean.getModelCode()).append("%'\n");
		}
		if(Utility.testString(bean.getClaimStatus())){
			sql.append("AND A.STATUS = ").append(bean.getClaimStatus()).append("\n");
		}else{
			sql.append("AND A.STATUS IN ('"+Constant.CLAIM_APPLY_ORD_TYPE_07+"','"+Constant.CLAIM_APPLY_ORD_TYPE_08+"')\n" );
		}
		if(Utility.testString(bean.getClaimType())){
			sql.append("AND A.CLAIM_TYPE = ").append(bean.getClaimType()).append("\n");
		}
		if(Utility.testString(bean.getStartDate())){
			sql.append("AND A.AUDITING_DATE >= TO_DATE('").append(bean.getStartDate()).append("', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(Utility.testString(bean.getEndDate())){
			sql.append("AND A.AUDITING_DATE <= TO_DATE('").append(bean.getEndDate()).append("', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(!CommonUtils.checkNull(bean.getCantNotAudit()).equals("")){
            sql.append("AND A.ID NOT IN("+bean.getCantNotAudit()+") ");
        }
		if(!CommonUtils.checkNull(invoiceStatus).equals("")){
            sql.append("AND A.IS_INVOICE="+invoiceStatus+"\n ");
        }		
		sql.append("  AND exists (SELECT 1\n" );
        sql.append("       FROM tm_business_area TDBA\n" );
		sql.append("      WHERE TDBA.area_id = a.yieldly\n" );
		sql.append("        AND TDBA.AREA_ID IN ("+areaIds+")\n)");
		
		//sql.append("AND C.BALANCE_ID = ").append(bean.getId()).append("\n");
		if(Utility.testString(bean.getClaimStatus())){
			if(Constant.CLAIM_APPLY_ORD_TYPE_07.toString().equals(bean.getClaimStatus())){
				sql.append("ORDER BY a.account_date desc");
			}else{
				sql.append("ORDER BY a.AUDITING_DATE asc,a.VIN ASC");
			}
		}else{
			sql.append("ORDER BY a.AUDITING_DATE asc,a.VIN ASC");
		}
		

		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), params, getFunName(), pageSize, curPage);
		return ps;
	}
	/*******************************add by kevinyin 20110413****************************************************/
	
	/*******************************add by kevinyin 20110414****************************************************/
	/**
	 * 查询审核人员
	 */
	public List<TcUserPO> queryAuditingUser(){
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT U.USER_ID, U.ACNT, U.NAME, U.BALANCE_LEVEL_CODE\n");
		sql.append("  FROM TC_USER U\n");
		sql.append(" WHERE U.BALANCE_LEVEL_CODE IS NOT NULL\n");
		sql.append("   AND (U.USER_TYPE = "+Constant.SYS_USER_SGM+"\n");
		sql.append(" OR U.USER_TYPE IS NULL)\n");


		List<TcUserPO> listUser = this.pageQuery(sql.toString(), null, this.getFunName());
		return listUser;
	}
	
	/**
	 * 查询审核索赔单
	 */
	public PageResult<Map<String, Object>> queryClaimAccAuditById(auditBean bean,int curPage, int pageSize){
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT ROWNUM NUM, A.ID CLAIM_ID, A.CLAIM_NO, A.YIELDLY,\n" );
		sql.append("       TO_CHAR(A.REPORT_DATE, 'YYYY-MM-DD') REPORT_DATE,\n" );
		sql.append("       TO_CHAR(A.AUDITING_DATE, 'YYYY-MM-DD') AUDITING_DATE,\n" );
		sql.append("       A.SERIES_NAME, A.MODEL_NAME, A.VIN, \n" );
		//sql.append("     B.DEALER_SHORTNAME DEALER_NAME,A.CLAIM_TYPE,A.STATUS,A.AUTH_CODE,A.BALANCE_AMOUNT,A.REPAIR_TOTAL,\n" );
		/******MOD BY LIUXH 20101203 索赔单申请费用加上追加工时******/
		sql.append("     B.DEALER_SHORTNAME DEALER_NAME,A.CLAIM_TYPE,A.STATUS,A.AUTH_CODE,A.BALANCE_AMOUNT,(NVL(A.REPAIR_TOTAL,0)) AS REPAIR_TOTAL,\n" );
		/******MOD BY LIUXH 20101203 索赔单申请费用加上追加工时******/
		sql.append("     (CASE WHEN A.STATUS = "+Constant.CLAIM_APPLY_ORD_TYPE_08+" AND A.AUTH_CODE = '"+CommonUtils.checkNull(bean.getAuthCodeOrder())+"' THEN 1 ELSE 2 END) ORDERSTR\n" );
		sql.append("FROM TT_AS_WR_APPLICATION A, TM_DEALER B\n" );
		sql.append("WHERE A.DEALER_ID = B.DEALER_ID\n" );
		
		List<Object> params = new ArrayList<Object>();
		if(Utility.testString(bean.getDealerCode())){
			sql.append(Utility.getConSqlByParamForEqual(bean.getDealerCode(), params, "B", "DEALER_CODE"));
		}
		if(Utility.testString(bean.getVin())){
			sql.append("AND A.VIN LIKE '%").append(bean.getVin()).append("%'\n");
		}
		if(Utility.testString(bean.getClaimNo())){
			sql.append("AND A.CLAIM_NO LIKE UPPER('%").append(bean.getClaimNo()).append("%')\n");
		}
		if(Utility.testString(bean.getModelCode())){
			sql.append("AND A.MODEL_CODE LIKE '%").append(bean.getModelCode()).append("%'\n");
		}
		if(Utility.testString(bean.getClaimStatus())){
			sql.append("AND A.STATUS = '").append(bean.getClaimStatus()).append("'\n");
		}
		if(Utility.testString(bean.getAuthCode())){
			sql.append("AND A.AUTH_CODE = '").append(bean.getAuthCode()).append("'\n");
		}
		if(Utility.testString(bean.getClaimType())){
			sql.append("AND A.CLAIM_TYPE = '").append(bean.getClaimType()).append("'\n");
		}
		if(Utility.testString(bean.getStartDate())){
			sql.append("AND A.AUDITING_DATE >= TO_DATE('").append(bean.getStartDate()).append("', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(Utility.testString(bean.getEndDate())){
			sql.append("AND A.AUDITING_DATE <= TO_DATE('").append(bean.getEndDate()).append("', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(!CommonUtils.checkNull(bean.getCantNotAudit()).equals("")){
            sql.append("AND A.ID NOT IN("+bean.getCantNotAudit()+") ");
        }
		//sql.append("AND C.BALANCE_ID = ").append(bean.getId()).append("\n");
		sql.append("ORDER BY a.AUDITING_DATE asc,a.VIN asc");

		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), params, getFunName(), pageSize, curPage);
		return ps;
	}
	//逐条审核查询SQL 
	public List<Map<String, Object>> queryClaimAuditById(auditBean bean,String areaId,int curPage, int pageSize){
		StringBuffer sql= new StringBuffer();
		sql.append("select * from ( SELECT /*+rule*/ROWNUM NUM, A.ID CLAIM_ID, A.CLAIM_NO, A.YIELDLY,\n" );
		sql.append("       TO_CHAR(A.REPORT_DATE, 'YYYY-MM-DD') REPORT_DATE,\n" );
		sql.append("       TO_CHAR(A.AUDITING_DATE, 'YYYY-MM-DD') AUDITING_DATE,\n" );
		sql.append("       A.SERIES_NAME, A.MODEL_NAME, A.VIN, \n" );
		//sql.append("     B.DEALER_SHORTNAME DEALER_NAME,A.CLAIM_TYPE,A.STATUS,A.AUTH_CODE,A.BALANCE_AMOUNT,A.REPAIR_TOTAL,\n" );
		/******MOD BY LIUXH 20101203 索赔单申请费用加上追加工时******/
		sql.append("     B.DEALER_SHORTNAME DEALER_NAME,A.CLAIM_TYPE,A.STATUS,A.AUTH_CODE,A.BALANCE_AMOUNT,(NVL(A.REPAIR_TOTAL,0)) AS REPAIR_TOTAL,\n" );
		/******MOD BY LIUXH 20101203 索赔单申请费用加上追加工时******/
		sql.append("     (CASE WHEN A.STATUS = "+Constant.CLAIM_APPLY_ORD_TYPE_08+" AND A.AUTH_CODE = '"+CommonUtils.checkNull(bean.getAuthCodeOrder())+"' THEN 1 ELSE 2 END) ORDERSTR\n" );
		sql.append("FROM TM_DEALER B,tc_user t,TT_AS_WR_APPLICATION A \n" );
		sql.append("WHERE A.DEALER_ID = B.DEALER_ID\n" );
		sql.append("and T.user_id="+bean.getAuthCodeOrder()+"\n");
		sql.append("and  exists  (select * from TC_USER_REGION_RELATION r where r.user_id=t.user_id and b.province_id = r.region_code)\n");
		List<Object> params = new ArrayList<Object>();
		if(Utility.testString(bean.getDealerCode())){
			sql.append(Utility.getConSqlByParamForEqual(bean.getDealerCode(), params, "B", "DEALER_CODE"));
		}
		if(Utility.testString(bean.getDealerName())){
			sql.append(" and B.DEALER_NAME LIKE '%"+bean.getDealerName()+"%'\n");
		}
		if(Utility.testString(bean.getVin())){
			sql.append("AND A.VIN LIKE '%").append(bean.getVin()).append("%'\n");
		}
		if(Utility.testString(bean.getClaimNo())){
			sql.append("AND A.CLAIM_NO LIKE UPPER('%").append(bean.getClaimNo()).append("%')\n");
		}
		//if(Utility.testString(bean.getModelCode())){
			//sql.append("AND A.MODEL_CODE LIKE '%").append(bean.getModelCode()).append("%'\n");
		//}
		if(bean.getModelCode()!=null&&!"".equals(bean.getModelCode())){
			String[] temp=bean.getModelCode().split(",");
			String str="";
			if(temp.length>0){
				sql.append(" and (");
				for(int count=0;count<temp.length;count++){
					str+=" A.MODEL_CODE='"+temp[count].replaceAll("'", "\''")+"' or\n";
				}
				sql.append(str.substring(0, str.length()-3));
				sql.append(")\n");
			}
		}
		if(Utility.testString(bean.getClaimStatus())){
			sql.append("AND A.STATUS = '").append(bean.getClaimStatus()).append("'\n");
		}
		if(Utility.testString(bean.getAuthCode())){
			sql.append("AND A.AUTH_CODE = '").append(bean.getAuthCode()).append("'\n");
		}
		if(Utility.testString(bean.getClaimType())){
			sql.append("AND A.CLAIM_TYPE = '").append(bean.getClaimType()).append("'\n");
		}
		if(Utility.testString(bean.getStartDate())){
			sql.append("AND A.AUDITING_DATE >= TO_DATE('").append(bean.getStartDate()).append("', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(Utility.testString(bean.getEndDate())){
			sql.append("AND A.AUDITING_DATE <= TO_DATE('").append(bean.getEndDate()).append("', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(!CommonUtils.checkNull(bean.getCantNotAudit()).equals("")){
            sql.append("AND A.ID NOT IN("+bean.getCantNotAudit()+") ");
        }
		
		sql.append("AND exists (SELECT 1\n");
		sql.append("           FROM tm_business_area TDBA\n");
		sql.append("\t\t     WHERE TDBA.produce_base = a.yieldly\n");
		sql.append("\t      AND TDBA.AREA_ID IN ("+areaId+")) \n");
		sql.append(" ");
		sql.append("ORDER BY a.AUDITING_DATE asc ,A.VIN ASC) where  ROWNUM<=1");

		//PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), params, getFunName(), pageSize, curPage);
		return this.pageQuery(sql.toString(), params, getFunName());
		
		//return ps;
	}
	//新索赔单跳过审核下一条
	public List<Map<String, Object>> queryClaimAccAuditSkipById(auditBean bean,String areaId,int curPage, int pageSize){
		StringBuffer sql= new StringBuffer();
		sql.append("select * from (SELECT /*+rule*/ROWNUM NUM, A.ID CLAIM_ID, A.CLAIM_NO, A.YIELDLY,\n" );
		sql.append("       TO_CHAR(A.REPORT_DATE, 'YYYY-MM-DD') REPORT_DATE,\n" );
		sql.append("       TO_CHAR(A.AUDITING_DATE, 'YYYY-MM-DD') AUDITING_DATE,\n" );
		sql.append("       A.SERIES_NAME, A.MODEL_NAME, A.VIN, \n" );
		//sql.append("     B.DEALER_SHORTNAME DEALER_NAME,A.CLAIM_TYPE,A.STATUS,A.AUTH_CODE,A.BALANCE_AMOUNT,A.REPAIR_TOTAL,\n" );
		/******MOD BY LIUXH 20101203 索赔单申请费用加上追加工时******/
		sql.append("     B.DEALER_SHORTNAME DEALER_NAME,A.CLAIM_TYPE,A.STATUS,A.AUTH_CODE,A.BALANCE_AMOUNT,(NVL(A.REPAIR_TOTAL,0)) AS REPAIR_TOTAL,\n" );
		/******MOD BY LIUXH 20101203 索赔单申请费用加上追加工时******/
		sql.append("     (CASE WHEN A.STATUS = "+Constant.CLAIM_APPLY_ORD_TYPE_08+" AND A.AUTH_CODE = '"+CommonUtils.checkNull(bean.getAuthCodeOrder())+"' THEN 1 ELSE 2 END) ORDERSTR\n" );
		sql.append("FROM TM_DEALER B,tc_user t,TT_AS_WR_APPLICATION A\n" );
		sql.append("WHERE A.DEALER_ID = B.DEALER_ID\n" );
		sql.append("and T.user_id="+bean.getAuthCodeOrder()+" \n");
		sql.append("and  exists  (select * from TC_USER_REGION_RELATION r where r.user_id=t.user_id and b.province_id = r.region_code)\n");
		List<Object> params = new ArrayList<Object>();
		if(Utility.testString(bean.getDealerCode())){
			sql.append(Utility.getConSqlByParamForEqual(bean.getDealerCode(), params, "B", "DEALER_CODE"));
		}
		if(Utility.testString(bean.getVin())){
			sql.append("AND A.VIN LIKE '%").append(bean.getVin()).append("%'\n");
		}
		if(Utility.testString(bean.getClaimNo())){
			sql.append("AND A.CLAIM_NO LIKE UPPER('%").append(bean.getClaimNo()).append("%')\n");
		}
		if(Utility.testString(bean.getModelCode())){
			sql.append("AND A.MODEL_CODE LIKE '%").append(bean.getModelCode()).append("%'\n");
		}
		if(Utility.testString(bean.getClaimStatus())){ 
			sql.append("AND A.STATUS = '").append(bean.getClaimStatus()).append("'\n");
		}
		if(Utility.testString(bean.getAuthCode())){
			sql.append("AND A.AUTH_CODE = '").append(bean.getAuthCode()).append("'\n");
		}
		if(Utility.testString(bean.getClaimType())){
			sql.append("AND A.CLAIM_TYPE = '").append(bean.getClaimType()).append("'\n");
		}
		if(Utility.testString(bean.getStartDate())){
			sql.append("AND A.AUDITING_DATE >= TO_DATE('").append(bean.getStartDate()).append("', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(Utility.testString(bean.getEndDate())){
			sql.append("AND A.AUDITING_DATE <= TO_DATE('").append(bean.getEndDate()).append("', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(!CommonUtils.checkNull(bean.getCantNotAudit()).equals("")){ 
            sql.append("AND A.ID NOT IN("+bean.getCantNotAudit()+") ");
        }
		
		sql.append("AND exists (SELECT 1\n");
		sql.append("           FROM tm_business_area TDBA\n");
		sql.append("\t\t     WHERE TDBA.produce_base = a.yieldly\n");
		sql.append("\t      AND TDBA.AREA_ID IN ("+areaId+"))\n");
		sql.append("ORDER BY A.AUDITING_DATE asc , A.VIN ASC) where ROWNUM<=1");
		return this.pageQuery(sql.toString(), params, getFunName());
		//PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), params, getFunName(), pageSize, curPage);
		//return ps;
	}
	/*******************************add by kevinyin 20110414****************************************************/
	/**************************微车流程修改keviyin*****************************************/
	
	public PageResult<Map<String, Object>> pageApplicattion(String dealer_id,String userType,String claimStatus,String balance_yieldly,String claim_type,String report_date,String admin,String CLAIM_NO,int pageSize,int curPage)
	{
		StringBuffer sql= new StringBuffer();
		sql.append("select A.*,(A.REPAIR_TOTAL - A.BALANCE_AMOUNT) cont,decode(A.STATUS,10791006,0,A.BALANCE_AMOUNT) DIS_BALANCE_AMOUNT,  B.DEALER_NAME,to_char(C.PURCHASED_DATE,'yyyy-mm-dd') PURCHASED_DATE,to_char(C.PRODUCT_DATE,'yyyy-mm-dd') PRODUCT_DATE \n" );
		sql.append(" , (A.REPAIR_TOTAL - nvl(A.PART_AMOUNT , 0) -\n" );
		sql.append("      decode(A.CLAIM_TYPE, 10661002, mg.part_price, 0) -\n" );
		sql.append("      decode(A.CLAIM_TYPE, 10661009, A.NETITEM_AMOUNT, 0)) labr_count,\n" );
		sql.append("\n" );
		sql.append("      decode(A.CLAIM_TYPE, 10661009, A.NETITEM_AMOUNT, 0) netitem_count,\n" );
		sql.append("\n" );
		sql.append("      (nvl(A.PART_AMOUNT,0) + decode(A.CLAIM_TYPE, 10661002, mg.part_price, 0)) part_count,\n" );
		sql.append("\n" );
		sql.append("      decode(A.STATUS,\n" );
		sql.append("             10791006,\n" );
		sql.append("             0,\n" );
		sql.append("             A.BALANCE_AMOUNT - nvl(A.BALANCE_PART_AMOUNT,0) -\n" );
		sql.append("                    decode(A.CLAIM_TYPE, 10661002, mg.part_price, 0)\n" );
		sql.append("                    -  decode(A.CLAIM_TYPE, 10661009, A.BALANCE_NETITEM_AMOUNT, 0)\n" );
		sql.append("                    ) BALANCE_labr_count   ,\n" );
		sql.append("      decode(A.STATUS,\n" );
		sql.append("             10791006,\n" );
		sql.append("             0,\n" );
		sql.append("             decode ( A.CLAIM_TYPE, 10661009, A.BALANCE_NETITEM_AMOUNT, 0)\n" );
		sql.append("             ) BALANCE_netitem_count   ,\n" );
		sql.append("\n" );
		sql.append("     decode(A.STATUS,\n" );
		sql.append("             10791006,\n" );
		sql.append("             0,\n" );
		sql.append("            nvl( A.BALANCE_PART_AMOUNT,0) + decode(A.CLAIM_TYPE, 10661002, mg.part_price, 0)\n" );
		sql.append("             ) BALANCE_part_count");

		
		sql.append("  from tt_as_wr_application A,TM_DEALER B,TM_VEHICLE C  , TT_AS_WR_MODEL_ITEM mi, tm_vehicle v ,tt_as_wr_model_group mg \n" );
		sql.append(" where A.DEALER_ID = B.DEALER_ID \n" );
		sql.append(" and v.package_id = mi.model_id\n" );
		sql.append("  and mg.wrgroup_id = mi.wrgroup_id\n" );
		sql.append("  and v.vin=A.VIN\n" );
		sql.append(" AND  MG.WRGROUP_TYPE=10451001");
		sql.append("   AND A.DEALER_ID = "+dealer_id+"\n" );
		sql.append("  AND A.VIN = C.VIN");
		
		sql.append(" AND A.REPORT_DATE >= " ).append("to_date('"+report_date.substring(0,7)+"','yyyy-mm')" ).append("\n");
		sql.append(" AND A.REPORT_DATE <= " ).append("(LAST_DAY (to_date('"+report_date+"-01','yyyy-mm-dd'))+1)" ).append("\n");
		
		if(userType.equals("0"))
		{
			sql.append(" AND  A.CLAIM_TYPE = "+Constant.CLA_TYPE_06 );
			sql.append(" AND A.CAMPAIGN_CODE  not in (SELECT D.ACTIVITY_CODE from TT_AS_ACTIVITY D,TT_AS_ACTIVITY_SUBJECT E where D.SUBJECT_ID = E.SUBJECT_ID and E.ACTIVITY_TYPE = "+Constant.SERVICEACTIVITY_TYPE_01+")");
		}else
		{
			if(admin != null && admin.length() > 0)
			{
				sql.append(" AND A.STATUS IN ('"+10791006+"','"+10791007+"','"+10791010+"','"+10791011+"','"+10791012+"','"+10791014+"')\n" );
				if(Utility.testString(CLAIM_NO))
				{
					String[] CLAIM_NOS = CLAIM_NO.split(",");
					CLAIM_NO = "(";
					for(int i = 0 ;i < CLAIM_NOS.length; i++)
					{
						if(i == CLAIM_NOS.length-1)
						{
							CLAIM_NO = CLAIM_NO + "'"+CLAIM_NOS[i] +"'"+")";
						}else
						{
							CLAIM_NO = CLAIM_NO + "'"+CLAIM_NOS[i] +"'"+",";
						}
					}
					sql.append(" AND A.CLAIM_NO in  " + CLAIM_NO +"  ");
				}
			}else
			{
				sql.append("and  A.STATUS = "+claimStatus+"");
				if(!((claim_type.equals("10661002")) || (claim_type.equals("10661006")) || (claim_type.equals("10661011"))))
				{
					sql.append("  AND A.CLAIM_TYPE != 10661002  AND  A.CLAIM_TYPE != "+Constant.CLA_TYPE_06 );
				}else if(claim_type.equals("10661002"))
				{
					sql.append("  AND A.CLAIM_TYPE = 10661002 " );
				}else if(claim_type.equals("10661006"))
				{
					sql.append(" AND  A.CLAIM_TYPE = "+Constant.CLA_TYPE_06 );
					sql.append(" AND A.CAMPAIGN_CODE  in (SELECT D.ACTIVITY_CODE from TT_AS_ACTIVITY D,TT_AS_ACTIVITY_SUBJECT E where D.SUBJECT_ID = E.SUBJECT_ID and E.SUBJECT_NO != 'PDI' and E.ACTIVITY_TYPE = "+Constant.SERVICEACTIVITY_TYPE_01+")");
				}else {
					sql.append(" AND  A.CLAIM_TYPE = "+Constant.CLA_TYPE_06 );
					sql.append(" AND A.CAMPAIGN_CODE  in (SELECT D.ACTIVITY_CODE from TT_AS_ACTIVITY D,TT_AS_ACTIVITY_SUBJECT E where D.SUBJECT_ID = E.SUBJECT_ID and E.SUBJECT_NO = 'PDI' and E.ACTIVITY_TYPE = "+Constant.SERVICEACTIVITY_TYPE_01+")");
				}
			}
			
			sql.append(" AND A.IS_LETTER = 1 ");
			sql.append("   AND A.BALANCE_YIELDLY = "+balance_yieldly+"\n" );
			sql.append("  ORDER by A.SECOND_DEALER_ID  desc ,A.CLAIM_NO,cont desc  ");
			
			
		}
		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
		return ps;
	}
	
	
	public PageResult<Map<String, Object>> dealerpageApplicattion(String dealer_id,String report_date,String balance_yieldly,int type,String subjectno,String  activtycode,String RO_NO,int pageSize,int curPage)
	{
		StringBuffer sql= new StringBuffer();
		PageResult<Map<String, Object>> ps = null;
	    sql.append("  select m.* from ("); 
		sql.append("select A.*,d.dealer_level, (nvl(A.REPAIR_TOTAL,0)-nvl(A.PART_AMOUNT,0)) LABOUR_AMOUNT1,(nvl(decode(A.STATUS , 10791006,0,A.BALANCE_AMOUNT),0)-nvl(decode(A.STATUS , 10791006,0,A.BALANCE_PART_AMOUNT),0)) BALANCE_LABOUR,to_char(b.in_warhouse_date,'yyyy-mm-dd') in_warhouse_date ,to_char(e.ENDDATE,'yyyy-mm-dd') tickets_ENDDATE,to_char(A.DIRECTOR_DATE,'yyyy-mm-dd') DIRECTOR_DATE1,to_char(A.SECTION_DATE,'yyyy-mm-dd') SECTION_DATE1 \n" );
		sql.append("  from tt_as_wr_application A\n" );
		sql.append(" left join TM_DEALER D  on  NVL(A.SECOND_DEALER_ID, A.DEALER_ID) = D.DEALER_ID ");
		sql.append("left join (select  max(r.in_warhouse_date) in_warhouse_date,t.CLAIM_NO CLAIM_NO\n" );
		sql.append(" from tt_as_wr_old_returned r,tt_as_wr_old_returned_detail d ,TT_AS_WR_APPLICATION t\n" );
		sql.append(" where r.id = d.return_id\n" );
		sql.append("and t.CLAIM_NO = d.CLAIM_NO group by t.CLAIM_NO) b on A.CLAIM_NO = b.CLAIM_NO ");
		
		sql.append("left join tt_as_wr_tickets e\n" );
		sql.append("on  to_char(A.REPORT_DATE,'yyyymm') = substr(e.GOODSNUM,length(e.GOODSNUM)-5,length(e.GOODSNUM))\n" );
		sql.append("and A.BALANCE_YIELDLY = e.BALANCE_YIELDLY\n" );
		sql.append(" and A.DEALER_ID = e.DEALERID ");

		sql.append("  where   1 = 1 AND (A.DEALER_ID = "+dealer_id+" OR A.SECOND_DEALER_ID = "+dealer_id+")\n" );
		sql.append(" ");
		sql.append(" AND A.REPORT_DATE >= " ).append("to_date('"+report_date.substring(0,7)+"','yyyy-mm')" ).append("\n");
		sql.append(" AND A.REPORT_DATE <= " ).append("(LAST_DAY (to_date('"+report_date+"-01','yyyy-mm-dd'))+1)" ).append("\n");
		sql.append(" and A.CLAIM_TYPE != 10661006  ");
		if(Utility.testString(balance_yieldly))
		{
			sql.append(" and A.balance_yieldly = "+balance_yieldly); 
		}
		if(Utility.testString(RO_NO))
		{
			sql.append(" and A.CLAIM_NO LIKE '%"+RO_NO+ "%' "); 
		}
		if(Utility.testString(subjectno) || Utility.testString(activtycode))
		{
			sql.append(" and  1!=1 ");
		}
    	if(type == 1)
    	{
    		sql.append(" UNION all ");
    		sql.append("select A.*,d.dealer_level, (nvl(A.REPAIR_TOTAL,0)-nvl(A.PART_AMOUNT,0)) LABOUR_AMOUNT1,(nvl(decode(A.STATUS , 10791006,0, A.BALANCE_AMOUNT),0)-nvl(decode(A.STATUS , 10791006,0,A.BALANCE_PART_AMOUNT),0)) BALANCE_LABOUR,to_char(b.in_warhouse_date,'yyyy-mm-dd') in_warhouse_date ,to_char(e.ENDDATE,'yyyy-mm-dd') tickets_ENDDATE,to_char(A.DIRECTOR_DATE,'yyyy-mm-dd') DIRECTOR_DATE1,to_char(A.SECTION_DATE,'yyyy-mm-dd') SECTION_DATE1 \n" );
    		sql.append("  from tt_as_wr_application A \n" );
    		sql.append(" left join TM_DEALER D  on  NVL(A.SECOND_DEALER_ID, A.DEALER_ID) = D.DEALER_ID ");
    		sql.append("left join (select  max(r.in_warhouse_date) in_warhouse_date,t.CLAIM_NO CLAIM_NO\n" );
    		sql.append(" from tt_as_wr_old_returned r,tt_as_wr_old_returned_detail d ,TT_AS_WR_APPLICATION t\n" );
    		sql.append(" where r.id = d.return_id\n" );
    		sql.append("and t.CLAIM_NO = d.CLAIM_NO group by t.CLAIM_NO) b on A.CLAIM_NO = b.CLAIM_NO ");
    		
    		sql.append("left join tt_as_wr_tickets e\n" );
    		sql.append("on  to_char(A.REPORT_DATE,'yyyymm') = substr(e.GOODSNUM,length(e.GOODSNUM)-5,length(e.GOODSNUM))\n" );
    		sql.append("and A.BALANCE_YIELDLY = e.BALANCE_YIELDLY\n" );
    		sql.append(" and A.DEALER_ID = e.DEALERID ");

    		sql.append("  where   1 = 1 AND (A.DEALER_ID = "+dealer_id+" OR A.SECOND_DEALER_ID = "+dealer_id+")\n" );
    		sql.append(" ");
    		sql.append(" AND A.REPORT_DATE >= " ).append("to_date('"+report_date.substring(0,7)+"','yyyy-mm')" ).append("\n");
    		sql.append(" AND A.REPORT_DATE <= " ).append("(LAST_DAY (to_date('"+report_date+"-01','yyyy-mm-dd'))+1)" ).append("\n");
    		sql.append(" and A.CLAIM_TYPE = 10661006  ");
    		sql.append(" AND A.CAMPAIGN_CODE  in (SELECT D.ACTIVITY_CODE from TT_AS_ACTIVITY D,TT_AS_ACTIVITY_SUBJECT E where D.SUBJECT_ID = E.SUBJECT_ID ");
        	if(Utility.testString(subjectno))
        	{
        		sql.append(" and E.SUBJECT_NO like '%"+subjectno+"%' ");
        	}
        	sql.append( "  ) ");
        	if(Utility.testString(activtycode))
        	{
        		sql.append(" and A.CAMPAIGN_CODE like '%"+activtycode+"%'  " );
        	}
        	if(Utility.testString(RO_NO))
    		{
    			sql.append(" and A.CLAIM_NO LIKE '%"+RO_NO+ "%' "); 
    		}
    	}else
    	{
    		sql.append(" UNION all ");
    		sql.append("select A.*,d.dealer_level, (nvl(A.REPAIR_TOTAL,0)-nvl(A.PART_AMOUNT,0)) LABOUR_AMOUNT1,(nvl(decode(A.STATUS , 10791006,0, A.BALANCE_AMOUNT),0)-nvl(decode(A.STATUS , 10791006,0,A.BALANCE_PART_AMOUNT),0)) BALANCE_LABOUR,to_char(b.in_warhouse_date,'yyyy-mm-dd') in_warhouse_date ,to_char(e.ENDDATE,'yyyy-mm-dd') tickets_ENDDATE,to_char(A.DIRECTOR_DATE,'yyyy-mm-dd') DIRECTOR_DATE1,to_char(A.SECTION_DATE,'yyyy-mm-dd') SECTION_DATE1 \n" );
    		sql.append("  from tt_as_wr_application A \n" );
    		sql.append(" left join TM_DEALER D  on  NVL(A.SECOND_DEALER_ID, A.DEALER_ID) = D.DEALER_ID ");
    		sql.append("left join (select  max(r.in_warhouse_date) in_warhouse_date,t.CLAIM_NO CLAIM_NO\n" );
    		sql.append(" from tt_as_wr_old_returned r,tt_as_wr_old_returned_detail d ,TT_AS_WR_APPLICATION t\n" );
    		sql.append(" where r.id = d.return_id\n" );
    		sql.append("and t.CLAIM_NO = d.CLAIM_NO group by t.CLAIM_NO) b on A.CLAIM_NO = b.CLAIM_NO ");
    		
    		sql.append("left join tt_as_wr_tickets e\n" );
    		sql.append("on  to_char(A.REPORT_DATE,'yyyymm') = substr(e.GOODSNUM,length(e.GOODSNUM)-5,length(e.GOODSNUM))\n" );
    		sql.append("and A.BALANCE_YIELDLY = e.BALANCE_YIELDLY\n" );
    		sql.append(" and A.DEALER_ID = e.DEALERID ");

    		sql.append("  where   1 = 1 AND (A.DEALER_ID = "+dealer_id+" OR A.SECOND_DEALER_ID = "+dealer_id+")\n" );
    		sql.append(" ");
    		sql.append(" AND A.REPORT_DATE >= " ).append("to_date('"+report_date.substring(0,7)+"','yyyy-mm')" ).append("\n");
    		sql.append(" AND A.REPORT_DATE <= " ).append("(LAST_DAY (to_date('"+report_date+"-01','yyyy-mm-dd'))+1)" ).append("\n");
    		sql.append(" and A.CLAIM_TYPE = 10661006  ");
        	sql.append(" AND A.CAMPAIGN_CODE  in (SELECT D.ACTIVITY_CODE from TT_AS_ACTIVITY D,TT_AS_ACTIVITY_SUBJECT E where D.SUBJECT_ID = E.SUBJECT_ID ");
        	if(Utility.testString(subjectno))
        	{
        		sql.append(" and E.SUBJECT_NO like '%"+subjectno+"%' ");
        	}
        	 sql.append( "  and E.ACTIVITY_TYPE = "+Constant.SERVICEACTIVITY_TYPE_01+")");
        	if(Utility.testString(activtycode))
        	{
        		sql.append(" and A.CAMPAIGN_CODE  like '%"+activtycode+"%'  " );
        	}
        	if(Utility.testString(balance_yieldly))
    		{
    			sql.append(" and A.balance_yieldly = "+balance_yieldly); 
    		}
        	if(Utility.testString(RO_NO))
    		{
    			sql.append(" and A.CLAIM_NO LIKE '%"+RO_NO+ "%' "); 
    		}
    	}
    	
    	sql.append(")m ORDER BY m.dealer_level, m.RO_NO DESC");
		ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
			
		
		
		return ps;
	}
	
	
	public List<Map<String, Object>> SettlementSummaryDelExport(String dealer_id,String report_date,String balance_yieldly,String subjectno,String  activtycode)
	{
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT ds.DEALER_CODE,\n" );
		sql.append("       a.CLAIM_NO,\n" );
		sql.append("        g.CODE_DESC,\n" );
		sql.append("       a.VIN,\n" );
		sql.append("       a.REPAIR_TOTAL,\n" );
		sql.append("       decode(a.STATUS, 10791006, 0, 10791008, 0, a.BALANCE_AMOUNT) BALANCE_AMOUNT,\n" );
		sql.append("        h.CODE_DESC CODE_DESC1,\n" );
		sql.append("       to_char(a.REPORT_DATE,'yyyy-mm-dd') REPORT_DATE ,\n" );
		sql.append("       a.CAMPAIGN_CODE\n" );
		sql.append("  from TC_CODE g ,TC_CODE h  , TT_AS_WR_APPLICATION a\n" );
		sql.append("  JOIN vw_org_dealer_service ds ON NVL(a.SECOND_DEALER_ID, a.dealer_id) =\n" );
		sql.append("                                   ds.dealer_id\n" );
		sql.append("                               AND ds.dealer_type = 10771002\n" );
		
		sql.append(" where a.DEALER_ID = "+dealer_id+"    and g.CODE_ID = a.CLAIM_TYPE  and h.CODE_ID = a.STATUS \n" );
		sql.append("   AND A.REPORT_DATE >= to_date('"+report_date+"', 'yyyy-mm')\n" );
		sql.append("   AND A.REPORT_DATE <=\n" );
		sql.append("       last_day(to_date('"+report_date+"-01', 'yyyy-mm-dd')) + 1 - 1 / 24 / 60 / 60\n" );
		if(Utility.testString(balance_yieldly))
		{
			sql.append("   AND A.BALANCE_YIELDLY = \n" +balance_yieldly );
		}
		
		if(Utility.testString(activtycode))
		{
			sql.append(" and a.CAMPAIGN_CODE like '%"+activtycode+"%'\n" );
		}
		
		if(Utility.testString(subjectno))
		{
			sql.append(" and a.CAMPAIGN_CODE in (SELECT t.ACTIVITY_CODE from TT_AS_ACTIVITY t ,TT_AS_ACTIVITY_SUBJECT f where t.SUBJECT_ID = f.SUBJECT_ID and f.SUBJECT_NO like '%"+subjectno+"%' )");
		}
		List<Map<String, Object>> list= super.pageQuery(sql.toString(), null, this.getFunName());
        return list;
	}
	
	
	public PageResult<Map<String, Object>> dealerpageApplicattionyx(String dealer_id,String report_date,int pageSize,int curPage)
	{
		StringBuffer sql= new StringBuffer();
		PageResult<Map<String, Object>> ps = null;
		sql.append("select A.*,to_char(b.in_warhouse_date,'yyyy-mm-dd') in_warhouse_date ,to_char(e.ENDDATE,'yyyy-mm-dd') tickets_ENDDATE,to_char(A.DIRECTOR_DATE,'yyyy-mm-dd') DIRECTOR_DATE1,to_char(A.SECTION_DATE,'yyyy-mm-dd') SECTION_DATE1 \n" );
		sql.append("  from tt_as_wr_application A\n" );
		sql.append("left join (select  max(r.in_warhouse_date) in_warhouse_date,t.CLAIM_NO CLAIM_NO\n" );
		sql.append(" from tt_as_wr_old_returned r,tt_as_wr_old_returned_detail d ,TT_AS_WR_APPLICATION t\n" );
		sql.append(" where r.id = d.return_id\n" );
		sql.append("and t.CLAIM_NO = d.CLAIM_NO group by t.CLAIM_NO) b on A.CLAIM_NO = b.CLAIM_NO ");
		
		sql.append("left join tt_as_wr_tickets e\n" );
		sql.append("on  to_char(A.REPORT_DATE,'yyyymm') = substr(e.GOODSNUM,length(e.GOODSNUM)-5,length(e.GOODSNUM))\n" );
		sql.append("and A.BALANCE_YIELDLY = e.BALANCE_YIELDLY\n" );
		sql.append(" and A.DEALER_ID = e.DEALERID ");

		sql.append("  where   1 = 1 AND A.DEALER_ID = "+dealer_id+"\n" );
		sql.append(" ");
		sql.append(" AND A.REPORT_DATE >= " ).append("to_date('"+report_date.substring(0,7)+"','yyyy-mm')" ).append("\n");
		sql.append(" AND A.REPORT_DATE <= " ).append("(LAST_DAY (to_date('"+report_date+"','yyyy-mm'))+1)" ).append("\n");
		sql.append(" and A.CLAIM_TYPE = 10661006 ");
    	sql.append(" AND A.CAMPAIGN_CODE  not in (SELECT D.ACTIVITY_CODE from TT_AS_ACTIVITY D,TT_AS_ACTIVITY_SUBJECT E where D.SUBJECT_ID = E.SUBJECT_ID  and E.ACTIVITY_TYPE = "+Constant.SERVICEACTIVITY_TYPE_01+")");
		ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
		
		
		
		return ps;
	}
	
	
	
	
	
	public PageResult<Map<String, Object>> pageApplicattionYX(String dealer_id,String claimStatus,String subject_id,String VIN,String ACTIVITYCODE ,int pageSize,int curPage)
	{
		StringBuffer sql= new StringBuffer();
		sql.append("select A.VIN,to_char(A.REPORT_DATE,'yyyy-mm-dd') REPORT_DATE,A.REPAIR_TOTAL,G.SUBJECT_NAME,A.ID,B.DEALER_NAME,A.CLAIM_NO,A.BALANCE_AMOUNT,A.STATUS,A.IS_INVOICE,A.AUDIT_OPINION \n" );
		sql.append("  from tt_as_wr_application A,TM_DEALER B,TT_AS_ACTIVITY F,TT_AS_ACTIVITY_SUBJECT G\n" );
		sql.append(" where A.DEALER_ID = B.DEALER_ID and  A.STATUS = "+claimStatus+"\n" );
		sql.append("   AND A.DEALER_ID = "+dealer_id+"\n" );
		sql.append(" AND F.SUBJECT_ID = G.SUBJECT_ID");
		sql.append(" AND F.ACTIVITY_CODE = A.CAMPAIGN_CODE");
		sql.append(" AND G.SUBJECT_ID="+subject_id);
		sql.append(" AND  A.CLAIM_TYPE = "+Constant.CLA_TYPE_06 );
		sql.append(" AND A.CAMPAIGN_CODE not in (SELECT D.ACTIVITY_CODE from TT_AS_ACTIVITY D,TT_AS_ACTIVITY_SUBJECT E where D.SUBJECT_ID = E.SUBJECT_ID and E.ACTIVITY_TYPE = "+Constant.SERVICEACTIVITY_TYPE_01+")");
		if(Utility.testString(VIN))
		{
			String[] vins = VIN.split(",");
			String sqlb = "(";
			for(int i = 0 ;i < vins.length;i++  )
			{
				if(i == vins.length-1)
				{
					sqlb =sqlb+ "'"+ vins[i]+"')";
				}else
				{
					sqlb =sqlb+ "'"+ vins[i]+"',";
				}
			}
			sql.append("  AND A.VIN in   "+  sqlb);
		}
		
		if(Utility.testString(ACTIVITYCODE))
		{
			String[] ACTIVITYCODES = ACTIVITYCODE.split(",");
			String sqlb = "(";
			for(int i = 0 ;i < ACTIVITYCODES.length;i++  )
			{
				if(i == ACTIVITYCODES.length-1)
				{
					sqlb =sqlb+ "'"+ ACTIVITYCODES[i]+"')";
				}else
				{
					sqlb =sqlb+ "'"+ ACTIVITYCODES[i]+"',";
				}
			}
			sql.append("  AND A.CLAIM_NO in   "+  sqlb);
		}
		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
		return ps;
	}
	
	
	public List<TtAsWrApplicationPO> judeDeale(String dealer_id,String userType,String claimStatus,String balance_yieldly,String claim_type,String report_date)
	{
		StringBuffer sql= new StringBuffer();
		sql.append("select *\n" );
		sql.append("  from tt_as_wr_application A\n" );
		sql.append(" where A.STATUS = "+claimStatus+"\n" );
		sql.append("   AND A.DEALER_ID = "+dealer_id+"\n" );
		sql.append(" AND  to_char(A.report_date,'yyyy-mm')='" + report_date+"'");
		if(userType.equals("0"))
		{
			sql.append(" AND  A.CLAIM_TYPE = "+Constant.CLA_TYPE_06 );
			sql.append(" AND A.CAMPAIGN_CODE  not in (SELECT D.ACTIVITY_CODE from TT_AS_ACTIVITY D,TT_AS_ACTIVITY_SUBJECT E where D.SUBJECT_ID = E.SUBJECT_ID and E.ACTIVITY_TYPE = "+Constant.SERVICEACTIVITY_TYPE_01+")");
		}else
		{
			sql.append(" AND A.IS_LETTER = 1 ");
			sql.append("   AND A.BALANCE_YIELDLY = "+balance_yieldly+"\n" );
			if(!((claim_type.equals("10661002")) || (claim_type.equals("10661006")) || (claim_type.equals("10661011"))))
			{
				sql.append("  AND A.CLAIM_TYPE != 10661002  AND  A.CLAIM_TYPE != "+Constant.CLA_TYPE_06 );
			}else if(claim_type.equals("10661002"))
			{
				sql.append("  AND A.CLAIM_TYPE = 10661002 " );
			}else if(claim_type.equals("10661006"))
			{
				sql.append(" AND  A.CLAIM_TYPE = "+Constant.CLA_TYPE_06 );
				sql.append(" AND A.CAMPAIGN_CODE  in (SELECT D.ACTIVITY_CODE from TT_AS_ACTIVITY D,TT_AS_ACTIVITY_SUBJECT E where D.SUBJECT_ID = E.SUBJECT_ID and E.SUBJECT_NO != 'PDI' and E.ACTIVITY_TYPE = "+Constant.SERVICEACTIVITY_TYPE_01+")");
			}else {
				sql.append(" AND  A.CLAIM_TYPE = "+Constant.CLA_TYPE_06 );
				sql.append(" AND A.CAMPAIGN_CODE  in (SELECT D.ACTIVITY_CODE from TT_AS_ACTIVITY D,TT_AS_ACTIVITY_SUBJECT E where D.SUBJECT_ID = E.SUBJECT_ID and E.SUBJECT_NO = 'PDI' and E.ACTIVITY_TYPE = "+Constant.SERVICEACTIVITY_TYPE_01+")");
			}
			
		}
		sql.append(" ORDER by A.SECOND_DEALER_ID  desc ,A.CLAIM_NO  ");
		
		List<TtAsWrApplicationPO> list = super.select(TtAsWrApplicationPO.class, sql.toString(),null);
		return list;
	}
	
	
	
	public List<TtAsWrApplicationPO> judeDealeYX(String dealer_id,String claimStatus,String claim_type)
	{
		StringBuffer sql= new StringBuffer();
		sql.append("select A.*\n" );
		sql.append("  from tt_as_wr_application A,TT_AS_ACTIVITY_SUBJECT B,TT_AS_ACTIVITY C\n" );
		sql.append(" where A.STATUS = "+claimStatus+"\n" );
		sql.append("   AND A.DEALER_ID = "+dealer_id+"\n" );
		sql.append(" AND  B.SUBJECT_ID = C.SUBJECT_ID");
		sql.append(" AND C.ACTIVITY_CODE = A.CAMPAIGN_CODE");
		sql.append(" AND  B.SUBJECT_ID = "+ claim_type);
		sql.append(" AND  A.CLAIM_TYPE = "+Constant.CLA_TYPE_06 );
		sql.append(" AND A.CAMPAIGN_CODE  not in (SELECT D.ACTIVITY_CODE from TT_AS_ACTIVITY D,TT_AS_ACTIVITY_SUBJECT E where D.SUBJECT_ID = E.SUBJECT_ID and E.ACTIVITY_TYPE = "+Constant.SERVICEACTIVITY_TYPE_01+")");
		List<TtAsWrApplicationPO> list = super.select(TtAsWrApplicationPO.class, sql.toString(),null);
		return list;
	}
	
	/**************************微车流程修改keviyin*****************************************/
	/*******************************add by kevinyin 20110413****************************************************/
	/**
	 * 查询所有符合条件的索赔单
	 */
	public PageResult<Map<String, Object>> queryAuditingClaimJC(auditBean bean,String areaIds,String invoiceStatus,String dealerName,String yieldly,int userType,long userID,String bansorg,int curPage, int pageSize){
		StringBuffer judesql= new StringBuffer();
		judesql.append("\n" );
		judesql.append("SELECT C.DEALER_ID\n" );
		judesql.append("  FROM TM_DEALER_ORG_RELATION C\n" );
		judesql.append(" where C.ORG_ID in\n" );
		judesql.append("       (SELECT B.ORG_ID\n" );
		judesql.append("          from tc_pose B\n" );
		judesql.append("         where B.POSE_ID in (SELECT A.POSE_ID\n" );
		judesql.append("                               from Tr_User_Pose A\n" );
		judesql.append("                              where A.USER_ID ="+userID+")\n" );
		judesql.append("           and B.POSE_BUS_TYPE =" + Constant.POSE_BUS_TYPE_WR+")\n" );
		judesql.append("   AND C.DEALER_ID not in (SELECT D.DEALER_ID from tr_pose_dealer D)\n" );
		judesql.append("UNION\n" );
		judesql.append("SELECT D.DEALER_ID\n" );
		judesql.append("  from tr_pose_dealer D  ,tc_pose F \n" );
		judesql.append(" where  F.POSE_ID = D.POSE_ID AND F.POSE_BUS_TYPE ="+Constant.POSE_BUS_TYPE_WR+"   AND  D.POSE_ID in\n" );
		judesql.append("       (SELECT E.POSE_ID from Tr_User_Pose E where E.USER_ID = "+userID+")");
	    List<TrPoseDealerPO> poList = super.select(TrPoseDealerPO.class, judesql.toString(), null);
	    
		StringBuffer sql= new StringBuffer();
		if(bean.getClaimStatus()!=null&&bean.getClaimStatus().equals("10791007")){
		sql.append("SELECT A.ID CLAIM_ID, A.CLAIM_NO, C.AREA_NAME,\n" );
		}
		else{
			sql.append("SELECT  /*+rule   */ROWNUM NUM, A.ID CLAIM_ID, A.CLAIM_NO, C.AREA_NAME,\n" );
		}
		sql.append("       TO_CHAR(A.REPORT_DATE, 'YYYY-MM-DD') REPORT_DATE,\n" );
		sql.append("       TO_CHAR(A.AUDITING_DATE, 'YYYY-MM-DD') AUDITING_DATE,\n" );
		sql.append("       TO_CHAR(A.ACCOUNT_DATE, 'YYYY-MM-DD') ACCOUNT_DATE,\n" );
		sql.append("       A.SERIES_NAME, T.GROUP_NAME, A.VIN,A.IS_INVOICE,\n" );
		//sql.append("     B.DEALER_SHORTNAME DEALER_NAME,A.CLAIM_TYPE,A.STATUS,A.AUTH_CODE,A.BALANCE_AMOUNT,A.REPAIR_TOTAL,\n" );
		/******MOD BY LIUXH 20101203 索赔单申请费用加上追加工时******/
		sql.append("     B.DEALER_SHORTNAME DEALER_NAME,A.CLAIM_TYPE,A.STATUS,A.AUTH_CODE,A.BALANCE_AMOUNT,(NVL(A.REPAIR_TOTAL,0)) AS REPAIR_TOTAL,\n" );
		/******MOD BY LIUXH 20101203 索赔单申请费用加上追加工时******/
		sql.append("     (CASE WHEN A.STATUS = "+Constant.CLAIM_APPLY_ORD_TYPE_08+" AND A.AUTH_CODE = '"+CommonUtils.checkNull(bean.getAuthCode())+"' THEN 1 ELSE 2 END) ORDERSTR\n" );
		sql.append("FROM tc_user t,TM_DEALER B,TT_AS_WR_APPLICATION A, Tm_Business_Area C,TM_VHCL_MATERIAL_GROUP T \n" );
		sql.append(" WHERE A.DEALER_ID = B.DEALER_ID and C.AREA_ID = A.YIELDLY  AND T.GROUP_CODE = A.MODEL_CODE \n" );
		sql.append(" and t.user_id="+bean.getAuthCodeOrder()+"\n");
		sql.append(" and  exists  (select * from TC_USER_REGION_RELATION r where r.user_id=t.user_id and b.province_id = r.region_code)\n");
		if(userType == 0)
		{
			sql.append(" AND  A.CLAIM_TYPE = "+Constant.CLA_TYPE_06);
			sql.append(" AND A.CAMPAIGN_CODE  in (SELECT D.ACTIVITY_CODE from TT_AS_ACTIVITY D,TT_AS_ACTIVITY_SUBJECT E where D.SUBJECT_ID = E.SUBJECT_ID and E.ACTIVITY_TYPE != "+Constant.SERVICEACTIVITY_TYPE_01+")");
		}
		else 
		{
			if(poList != null && poList.size() >0)
			{
				String append = "";
				for(int i = 0 ; i<poList.size() ; i++)
				{
					if(i == poList.size()-1)
					{
						append = append + poList.get(i).getDealerId();
					}else{
						append = append + poList.get(i).getDealerId()+",";
					}
				}
				sql.append(" AND  A.DEALER_ID in("+append+")");
			}else 
			{
				sql.append(" AND 1=0 ");
			}
			sql.append(" AND A.IS_LETTER = 1   AND  A.BALANCE_YIELDLY= " + bansorg);
			sql.append(" AND  A.CLAIM_TYPE != "+Constant.CLA_TYPE_06);
		}
		List<Object> params = new ArrayList<Object>();
		if(Utility.testString(bean.getDealerCode())){
			sql.append(Utility.getConSqlByParamForEqual(bean.getDealerCode(), params, "B", "DEALER_CODE"));
		}
		if(Utility.testString(dealerName)){
			sql.append(" AND B.DEALER_NAME LIKE '%"+dealerName+"%'\n");
		}
    	if(Utility.testString(yieldly)){
  		sql.append(" AND A.YIELDLY LIKE '%"+yieldly+"%'\n");
		}
		if(Utility.testString(bean.getVin())){
			sql.append("AND A.VIN LIKE '%").append(bean.getVin()).append("%'\n");
		}
		if(Utility.testString(bean.getClaimNo())){
			sql.append("AND A.CLAIM_NO LIKE UPPER('").append(bean.getClaimNo()).append("%')\n");
		}
		
		if(bean.getModelCode()!=null&&!"".equals(bean.getModelCode())){
			String[] temp=bean.getModelCode().split(",");
			String str="";
			if(temp.length>0){
				sql.append(" and (");
				for(int count=0;count<temp.length;count++){
					str+=" A.MODEL_CODE='"+temp[count].replaceAll("'", "\''")+"' or\n";
				}
				sql.append(str.substring(0, str.length()-3));
				sql.append(")\n");
			}
		}
		if(Utility.testString(bean.getClaimStatus())){
			sql.append(" AND A.STATUS = ").append(bean.getClaimStatus()).append("\n");
		}else{
				sql.append(" AND A.STATUS =" +Constant.CLAIM_APPLY_ORD_TYPE_07  + "\n" );
		}
		if(Utility.testString(bean.getClaimType())){
			sql.append("AND A.CLAIM_TYPE = ").append(bean.getClaimType()).append("\n");
		} 
		if(Utility.testString(bean.getStartDate())){
			sql.append("AND A.REPORT_DATE >= TO_DATE('").append(bean.getStartDate()).append("', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(Utility.testString(bean.getEndDate())){
			sql.append("AND A.REPORT_DATE <= TO_DATE('").append(bean.getEndDate()).append("', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(Utility.testString(bean.getBalanceSatartDate())){
			sql.append("AND a.account_date >= TO_DATE('").append(bean.getBalanceSatartDate()).append("', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(Utility.testString(bean.getBalanceEndtDate())){
			sql.append("AND A.ACCOUNT_DATE <= TO_DATE('").append(bean.getBalanceEndtDate()).append("', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(!CommonUtils.checkNull(bean.getCantNotAudit()).equals("")){
            sql.append("AND A.ID NOT IN("+bean.getCantNotAudit()+") ");
        }
		if(!CommonUtils.checkNull(invoiceStatus).equals("")){
            sql.append("AND A.IS_INVOICE="+invoiceStatus+"\n ");
        }		
		sql.append("  AND exists (SELECT 1\n" );
        sql.append("       FROM tm_business_area TDBA\n" );
		sql.append("      WHERE TDBA.AREA_ID = a.yieldly\n" );
		sql.append("        AND TDBA.AREA_ID IN ("+areaIds+")\n)");
		
		
		if(userType == 1)
		{
			sql.append(" UNION ");
			if(bean.getClaimStatus()!=null&&bean.getClaimStatus().equals("10791007")){
				sql.append("SELECT A.ID CLAIM_ID, A.CLAIM_NO, C.AREA_NAME,\n" );
				}
				else{
					sql.append("SELECT  /*+rule   */ROWNUM NUM, A.ID CLAIM_ID, A.CLAIM_NO, C.AREA_NAME,\n" );
				}
				sql.append("       TO_CHAR(A.REPORT_DATE, 'YYYY-MM-DD') REPORT_DATE,\n" );
				sql.append("       TO_CHAR(A.AUDITING_DATE, 'YYYY-MM-DD') AUDITING_DATE,\n" );
				sql.append("       TO_CHAR(A.ACCOUNT_DATE, 'YYYY-MM-DD') ACCOUNT_DATE,\n" );
				sql.append("       A.SERIES_NAME, T.GROUP_NAME, A.VIN,A.IS_INVOICE,\n" );
				//sql.append("     B.DEALER_SHORTNAME DEALER_NAME,A.CLAIM_TYPE,A.STATUS,A.AUTH_CODE,A.BALANCE_AMOUNT,A.REPAIR_TOTAL,\n" );
				/******MOD BY LIUXH 20101203 索赔单申请费用加上追加工时******/
				sql.append("     B.DEALER_SHORTNAME DEALER_NAME,A.CLAIM_TYPE,A.STATUS,A.AUTH_CODE,A.BALANCE_AMOUNT,(NVL(A.REPAIR_TOTAL,0)) AS REPAIR_TOTAL,\n" );
				/******MOD BY LIUXH 20101203 索赔单申请费用加上追加工时******/
				sql.append("     (CASE WHEN A.STATUS = "+Constant.CLAIM_APPLY_ORD_TYPE_08+" AND A.AUTH_CODE = '"+CommonUtils.checkNull(bean.getAuthCode())+"' THEN 1 ELSE 2 END) ORDERSTR\n" );
				sql.append("FROM tc_user t,TM_DEALER B,TT_AS_WR_APPLICATION A, Tm_Business_Area C,TM_VHCL_MATERIAL_GROUP T \n" );
				sql.append(" WHERE A.DEALER_ID = B.DEALER_ID and C.AREA_ID = A.YIELDLY  AND T.GROUP_CODE = A.MODEL_CODE \n" );
				sql.append(" and t.user_id="+bean.getAuthCodeOrder()+"\n");
				sql.append(" and  exists  (select * from TC_USER_REGION_RELATION r where r.user_id=t.user_id and b.province_id = r.region_code)\n");
				if(userType == 0)
				{
					sql.append(" AND  A.CLAIM_TYPE = "+Constant.CLA_TYPE_06);
					sql.append(" AND A.CAMPAIGN_CODE not in (SELECT D.ACTIVITY_CODE from TT_AS_ACTIVITY D,TT_AS_ACTIVITY_SUBJECT E where D.SUBJECT_ID = E.SUBJECT_ID and E.ACTIVITY_TYPE != "+Constant.SERVICEACTIVITY_TYPE_02+")");
				}
				else 
				{
					if(poList != null && poList.size() >0)
					{
						String append = "";
						for(int i = 0 ; i<poList.size() ; i++)
						{
							if(i == poList.size()-1)
							{
								append = append + poList.get(i).getDealerId();
							}else{
								append = append + poList.get(i).getDealerId()+",";
							}
						}
						sql.append(" AND  A.DEALER_ID in("+append+")");
					}else 
					{
						sql.append(" AND 1=0 ");
					}
					sql.append(" AND A.IS_LETTER = 1   AND  A.BALANCE_YIELDLY= " + bansorg);
					sql.append(" AND  A.CLAIM_TYPE = "+Constant.CLA_TYPE_06);
					sql.append(" AND A.CAMPAIGN_CODE  in (SELECT D.ACTIVITY_CODE from TT_AS_ACTIVITY D,TT_AS_ACTIVITY_SUBJECT E where D.SUBJECT_ID = E.SUBJECT_ID and E.ACTIVITY_TYPE = "+Constant.SERVICEACTIVITY_TYPE_01+")");
				}
				List<Object> params1 = new ArrayList<Object>();
				if(Utility.testString(bean.getDealerCode())){
					sql.append(Utility.getConSqlByParamForEqual(bean.getDealerCode(), params1, "B", "DEALER_CODE"));
				}
				if(Utility.testString(dealerName)){
					sql.append(" AND B.DEALER_NAME LIKE '%"+dealerName+"%'\n");
				}
		    	if(Utility.testString(yieldly)){
		  		sql.append(" AND A.YIELDLY LIKE '%"+yieldly+"%'\n");
				}
				if(Utility.testString(bean.getVin())){
					sql.append("AND A.VIN LIKE '%").append(bean.getVin()).append("%'\n");
				}
				if(Utility.testString(bean.getClaimNo())){
					sql.append("AND A.CLAIM_NO LIKE UPPER('").append(bean.getClaimNo()).append("%')\n");
				}
				
				if(bean.getModelCode()!=null&&!"".equals(bean.getModelCode())){
					String[] temp=bean.getModelCode().split(",");
					String str="";
					if(temp.length>0){
						sql.append(" and (");
						for(int count=0;count<temp.length;count++){
							str+=" A.MODEL_CODE='"+temp[count].replaceAll("'", "\''")+"' or\n";
						}
						sql.append(str.substring(0, str.length()-3));
						sql.append(")\n");
					}
				}
				if(Utility.testString(bean.getClaimStatus())){
					sql.append(" AND A.STATUS = ").append(bean.getClaimStatus()).append("\n");
				}else{
					sql.append(" AND A.STATUS =" +Constant.CLAIM_APPLY_ORD_TYPE_07  + "\n" );
				}
				if(Utility.testString(bean.getClaimType())){
					sql.append("AND A.CLAIM_TYPE = ").append(bean.getClaimType()).append("\n");
				} 
				if(Utility.testString(bean.getStartDate())){
					sql.append("AND A.REPORT_DATE >= TO_DATE('").append(bean.getStartDate()).append("', 'YYYY-MM-DD HH24:MI:SS')\n");
				}
				if(Utility.testString(bean.getEndDate())){
					sql.append("AND A.REPORT_DATE <= TO_DATE('").append(bean.getEndDate()).append("', 'YYYY-MM-DD HH24:MI:SS')\n");
				}
				if(Utility.testString(bean.getBalanceSatartDate())){
					sql.append("AND a.account_date >= TO_DATE('").append(bean.getBalanceSatartDate()).append("', 'YYYY-MM-DD HH24:MI:SS')\n");
				}
				if(Utility.testString(bean.getBalanceEndtDate())){
					sql.append("AND A.ACCOUNT_DATE <= TO_DATE('").append(bean.getBalanceEndtDate()).append("', 'YYYY-MM-DD HH24:MI:SS')\n");
				}
				if(!CommonUtils.checkNull(bean.getCantNotAudit()).equals("")){
		            sql.append("AND A.ID NOT IN("+bean.getCantNotAudit()+") ");
		        }
				if(!CommonUtils.checkNull(invoiceStatus).equals("")){
		            sql.append("AND A.IS_INVOICE="+invoiceStatus+"\n ");
		        }		
				sql.append("  AND exists (SELECT 1\n" );
		        sql.append("       FROM tm_business_area TDBA\n" );
				sql.append("      WHERE TDBA.AREA_ID = a.yieldly\n" );
				sql.append("        AND TDBA.AREA_ID IN ("+areaIds+")\n)");
				
		}
		
		

		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), params, getFunName(), pageSize, curPage);
		return ps;
	}
	
	
	public PageResult<Map<String, Object>> queryAuditingClaimJC1(auditBean bean,String areaIds,String invoiceStatus,String dealerName,String yieldly,int userType,long userID,String bansorg,int curPage, int pageSize){
		
	    
		StringBuffer sql= new StringBuffer();
		if(bean.getClaimStatus()!=null&&bean.getClaimStatus().equals("10791007")){
		sql.append("SELECT A.ID CLAIM_ID, A.CLAIM_NO, C.AREA_NAME,\n" );
		}
		else{
			sql.append("SELECT  /*+rule   */ROWNUM NUM, A.ID CLAIM_ID, A.CLAIM_NO, C.AREA_NAME,\n" );
		}
		sql.append("       TO_CHAR(A.REPORT_DATE, 'YYYY-MM-DD') REPORT_DATE,\n" );
		sql.append("       TO_CHAR(A.AUDITING_DATE, 'YYYY-MM-DD') AUDITING_DATE,\n" );
		sql.append("       TO_CHAR(A.ACCOUNT_DATE, 'YYYY-MM-DD') ACCOUNT_DATE,\n" );
		sql.append("       A.SERIES_NAME, T.GROUP_NAME, A.VIN,A.IS_INVOICE,\n" );
		//sql.append("     B.DEALER_SHORTNAME DEALER_NAME,A.CLAIM_TYPE,A.STATUS,A.AUTH_CODE,A.BALANCE_AMOUNT,A.REPAIR_TOTAL,\n" );
		/******MOD BY LIUXH 20101203 索赔单申请费用加上追加工时******/
		sql.append("     B.DEALER_SHORTNAME DEALER_NAME,A.CLAIM_TYPE,A.STATUS,A.AUTH_CODE,A.BALANCE_AMOUNT,(NVL(A.REPAIR_TOTAL,0)) AS REPAIR_TOTAL,\n" );
		/******MOD BY LIUXH 20101203 索赔单申请费用加上追加工时******/
		sql.append("     (CASE WHEN A.STATUS = "+Constant.CLAIM_APPLY_ORD_TYPE_08+" AND A.AUTH_CODE = '"+CommonUtils.checkNull(bean.getAuthCode())+"' THEN 1 ELSE 2 END) ORDERSTR\n" );
		sql.append("FROM tc_user t,TM_DEALER B,TT_AS_WR_APPLICATION A, Tm_Business_Area C,TM_VHCL_MATERIAL_GROUP T \n" );
		sql.append(" WHERE A.DEALER_ID = B.DEALER_ID and C.AREA_ID = A.YIELDLY  AND T.GROUP_CODE = A.MODEL_CODE \n" );
		sql.append(" and t.user_id="+bean.getAuthCodeOrder()+"\n");
		sql.append(" and  exists  (select * from TC_USER_REGION_RELATION r where r.user_id=t.user_id and b.province_id = r.region_code)\n");
		if(userType == 0)
		{
			sql.append(" AND  A.CLAIM_TYPE = "+Constant.CLA_TYPE_06);
			sql.append(" AND A.CAMPAIGN_CODE  not  in (SELECT D.ACTIVITY_CODE from TT_AS_ACTIVITY D,TT_AS_ACTIVITY_SUBJECT E where D.SUBJECT_ID = E.SUBJECT_ID and E.ACTIVITY_TYPE = "+Constant.SERVICEACTIVITY_TYPE_01+")");
		}
		else 
		{
			sql.append(" AND A.IS_LETTER = 1   AND  A.BALANCE_YIELDLY= " + bansorg);
			sql.append(" AND  A.CLAIM_TYPE != "+Constant.CLA_TYPE_06);
		}
		List<Object> params = new ArrayList<Object>();
		if(Utility.testString(bean.getDealerCode())){
			sql.append(Utility.getConSqlByParamForEqual(bean.getDealerCode(), params, "B", "DEALER_CODE"));
		}
		if(Utility.testString(dealerName)){
			sql.append(" AND B.DEALER_NAME LIKE '%"+dealerName+"%'\n");
		}
    	if(Utility.testString(yieldly)){
  		sql.append(" AND A.YIELDLY LIKE '%"+yieldly+"%'\n");
		}
		if(Utility.testString(bean.getVin())){
			sql.append("AND A.VIN LIKE '%").append(bean.getVin()).append("%'\n");
		}
		if(Utility.testString(bean.getClaimNo())){
			sql.append("AND A.CLAIM_NO LIKE UPPER('").append(bean.getClaimNo()).append("%')\n");
		}
		
		if(bean.getModelCode()!=null&&!"".equals(bean.getModelCode())){
			String[] temp=bean.getModelCode().split(",");
			String str="";
			if(temp.length>0){
				sql.append(" and (");
				for(int count=0;count<temp.length;count++){
					str+=" A.MODEL_CODE='"+temp[count].replaceAll("'", "\''")+"' or\n";
				}
				sql.append(str.substring(0, str.length()-3));
				sql.append(")\n");
			}
		}
		if(Utility.testString(bean.getClaimStatus())){
			sql.append(" AND A.STATUS = ").append(bean.getClaimStatus()).append("\n");
		}else{
			sql.append(" AND A.STATUS =" +Constant.CLAIM_APPLY_ORD_TYPE_11  + "\n" );
		}
		if(Utility.testString(bean.getClaimType())){
			sql.append("AND A.CLAIM_TYPE = ").append(bean.getClaimType()).append("\n");
		} 
		if(Utility.testString(bean.getStartDate())){
			sql.append("AND A.REPORT_DATE >= TO_DATE('").append(bean.getStartDate()).append("', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(Utility.testString(bean.getEndDate())){
			sql.append("AND A.REPORT_DATE <= TO_DATE('").append(bean.getEndDate()).append("', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(Utility.testString(bean.getBalanceSatartDate())){
			sql.append("AND a.account_date >= TO_DATE('").append(bean.getBalanceSatartDate()).append("', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(Utility.testString(bean.getBalanceEndtDate())){
			sql.append("AND A.ACCOUNT_DATE <= TO_DATE('").append(bean.getBalanceEndtDate()).append("', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(!CommonUtils.checkNull(bean.getCantNotAudit()).equals("")){
            sql.append("AND A.ID NOT IN("+bean.getCantNotAudit()+") ");
        }
		if(!CommonUtils.checkNull(invoiceStatus).equals("")){
            sql.append("AND A.IS_INVOICE="+invoiceStatus+"\n ");
        }		
		sql.append("  AND exists (SELECT 1\n" );
        sql.append("       FROM tm_business_area TDBA\n" );
		sql.append("      WHERE TDBA.AREA_ID = a.yieldly\n" );
		sql.append("        AND TDBA.AREA_ID IN ("+areaIds+")\n)");
		
		
		if(userType == 1)
		{
			sql.append(" UNION ");
			if(bean.getClaimStatus()!=null&&bean.getClaimStatus().equals("10791007")){
				sql.append("SELECT A.ID CLAIM_ID, A.CLAIM_NO, C.AREA_NAME,\n" );
				}
				else{
					sql.append("SELECT  /*+rule   */ROWNUM NUM, A.ID CLAIM_ID, A.CLAIM_NO, C.AREA_NAME,\n" );
				}
				sql.append("       TO_CHAR(A.REPORT_DATE, 'YYYY-MM-DD') REPORT_DATE,\n" );
				sql.append("       TO_CHAR(A.AUDITING_DATE, 'YYYY-MM-DD') AUDITING_DATE,\n" );
				sql.append("       TO_CHAR(A.ACCOUNT_DATE, 'YYYY-MM-DD') ACCOUNT_DATE,\n" );
				sql.append("       A.SERIES_NAME, T.GROUP_NAME, A.VIN,A.IS_INVOICE,\n" );
				//sql.append("     B.DEALER_SHORTNAME DEALER_NAME,A.CLAIM_TYPE,A.STATUS,A.AUTH_CODE,A.BALANCE_AMOUNT,A.REPAIR_TOTAL,\n" );
				/******MOD BY LIUXH 20101203 索赔单申请费用加上追加工时******/
				sql.append("     B.DEALER_SHORTNAME DEALER_NAME,A.CLAIM_TYPE,A.STATUS,A.AUTH_CODE,A.BALANCE_AMOUNT,(NVL(A.REPAIR_TOTAL,0)) AS REPAIR_TOTAL,\n" );
				/******MOD BY LIUXH 20101203 索赔单申请费用加上追加工时******/
				sql.append("     (CASE WHEN A.STATUS = "+Constant.CLAIM_APPLY_ORD_TYPE_08+" AND A.AUTH_CODE = '"+CommonUtils.checkNull(bean.getAuthCode())+"' THEN 1 ELSE 2 END) ORDERSTR\n" );
				sql.append("FROM tc_user t,TM_DEALER B,TT_AS_WR_APPLICATION A, Tm_Business_Area C,TM_VHCL_MATERIAL_GROUP T \n" );
				sql.append(" WHERE A.DEALER_ID = B.DEALER_ID and C.AREA_ID = A.YIELDLY  AND T.GROUP_CODE = A.MODEL_CODE \n" );
				sql.append(" and t.user_id="+bean.getAuthCodeOrder()+"\n");
				sql.append(" and  exists  (select * from TC_USER_REGION_RELATION r where r.user_id=t.user_id and b.province_id = r.region_code)\n");
				if(userType == 0)
				{
					sql.append(" AND  A.CLAIM_TYPE = "+Constant.CLA_TYPE_06);
					sql.append(" AND A.CAMPAIGN_CODE not in (SELECT D.ACTIVITY_CODE from TT_AS_ACTIVITY D,TT_AS_ACTIVITY_SUBJECT E where D.SUBJECT_ID = E.SUBJECT_ID and E.ACTIVITY_TYPE != "+Constant.SERVICEACTIVITY_TYPE_02+")");
				}
				else 
				{
					sql.append(" AND  A.CLAIM_TYPE = "+Constant.CLA_TYPE_06);
					sql.append(" AND A.IS_LETTER = 1   AND  A.BALANCE_YIELDLY= " + bansorg);
					sql.append(" AND A.CAMPAIGN_CODE  in (SELECT D.ACTIVITY_CODE from TT_AS_ACTIVITY D,TT_AS_ACTIVITY_SUBJECT E where D.SUBJECT_ID = E.SUBJECT_ID and E.ACTIVITY_TYPE = "+Constant.SERVICEACTIVITY_TYPE_01+")");
				}
				List<Object> params1 = new ArrayList<Object>();
				if(Utility.testString(bean.getDealerCode())){
					sql.append(Utility.getConSqlByParamForEqual(bean.getDealerCode(), params1, "B", "DEALER_CODE"));
				}
				if(Utility.testString(dealerName)){
					sql.append(" AND B.DEALER_NAME LIKE '%"+dealerName+"%'\n");
				}
		    	if(Utility.testString(yieldly)){
		  		sql.append(" AND A.YIELDLY LIKE '%"+yieldly+"%'\n");
				}
				if(Utility.testString(bean.getVin())){
					sql.append("AND A.VIN LIKE '%").append(bean.getVin()).append("%'\n");
				}
				if(Utility.testString(bean.getClaimNo())){
					sql.append("AND A.CLAIM_NO LIKE UPPER('").append(bean.getClaimNo()).append("%')\n");
				}
				
				if(bean.getModelCode()!=null&&!"".equals(bean.getModelCode())){
					String[] temp=bean.getModelCode().split(",");
					String str="";
					if(temp.length>0){
						sql.append(" and (");
						for(int count=0;count<temp.length;count++){
							str+=" A.MODEL_CODE='"+temp[count].replaceAll("'", "\''")+"' or\n";
						}
						sql.append(str.substring(0, str.length()-3));
						sql.append(")\n");
					}
				}
				if(Utility.testString(bean.getClaimStatus())){
					sql.append(" AND A.STATUS = ").append(bean.getClaimStatus()).append("\n");
				}else{
					sql.append(" AND A.STATUS =" +Constant.CLAIM_APPLY_ORD_TYPE_11  + "\n" );
				}
				if(Utility.testString(bean.getClaimType())){
					sql.append("AND A.CLAIM_TYPE = ").append(bean.getClaimType()).append("\n");
				} 
				if(Utility.testString(bean.getStartDate())){
					sql.append("AND A.REPORT_DATE >= TO_DATE('").append(bean.getStartDate()).append("', 'YYYY-MM-DD HH24:MI:SS')\n");
				}
				if(Utility.testString(bean.getEndDate())){
					sql.append("AND A.REPORT_DATE <= TO_DATE('").append(bean.getEndDate()).append("', 'YYYY-MM-DD HH24:MI:SS')\n");
				}
				if(Utility.testString(bean.getBalanceSatartDate())){
					sql.append("AND a.account_date >= TO_DATE('").append(bean.getBalanceSatartDate()).append("', 'YYYY-MM-DD HH24:MI:SS')\n");
				}
				if(Utility.testString(bean.getBalanceEndtDate())){
					sql.append("AND A.ACCOUNT_DATE <= TO_DATE('").append(bean.getBalanceEndtDate()).append("', 'YYYY-MM-DD HH24:MI:SS')\n");
				}
				if(!CommonUtils.checkNull(bean.getCantNotAudit()).equals("")){
		            sql.append("AND A.ID NOT IN("+bean.getCantNotAudit()+") ");
		        }
				if(!CommonUtils.checkNull(invoiceStatus).equals("")){
		            sql.append("AND A.IS_INVOICE="+invoiceStatus+"\n ");
		        }		
				sql.append("  AND exists (SELECT 1\n" );
		        sql.append("       FROM tm_business_area TDBA\n" );
				sql.append("      WHERE TDBA.AREA_ID = a.yieldly\n" );
				sql.append("        AND TDBA.AREA_ID IN ("+areaIds+")\n)");
				
				//sql.append("AND C.BALANCE_ID = ").append(bean.getId()).append("\n");
		}
		
		

		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), params, getFunName(), pageSize, curPage);
		return ps;
	}
	
	/**************************微车流程修改keviyin*****************************************/
	/*******************************add by kevinyin 20110413****************************************************/
	/**
	 * 查询所有符合条件的索赔单
	 */
	public PageResult<Map<String, Object>> queryAuditingClaimJC(auditBean bean,String dealerName,int userType,long userID,String bansorg,AclUserBean logonUser,int curPage, int pageSize){
	    StringBuffer sql= new StringBuffer();
	    
	    sql.append("SELECT D.DEALER_NAME,\n" );
	    sql.append("       D.DEALER_ID,\n" );
	    sql.append("       D.BALANCE_AMOUNT BALANCE_AMOUNT,\n" );
	    sql.append("       D.REPAIR_TOTAL,\n" );
	    sql.append("       D.TOTAL,\n" );
	    sql.append("       D.STATUS,\n" );
	    sql.append("       D.REPORT_DATE,\n" );
	    sql.append("       D.BALANCE_YIELDLY,\n" );
	    sql.append("       D.CLAIM_TYPE\n" );
	    sql.append("   from (  ");
	    
	    sql.append("SELECT M.DEALER_NAME,\n" );
	    sql.append("       M.DEALER_ID,\n" );
	    sql.append("       SUM(M.BALANCE_AMOUNT) BALANCE_AMOUNT,\n" );
	    sql.append("       SUM(M.REPAIR_TOTAL) REPAIR_TOTAL,\n" );
	    sql.append("       COUNT(*) TOTAL,\n" );
	    sql.append("       M.STATUS,\n" );
	    sql.append("       M.REPORT_DATE,\n" );
	    sql.append("       MIN(M.BALANCE_YIELDLY) BALANCE_YIELDLY,\n" );
	    sql.append("       M.CLAIM_TYPE\n" );
	    sql.append("FROM (  SELECT B.DEALER_SHORTNAME DEALER_NAME,\n" );
	    sql.append("               A.DEALER_ID,\n" );
	    sql.append("               A.BALANCE_AMOUNT,\n" );
	    sql.append("               NVL(A.REPAIR_TOTAL, 0) REPAIR_TOTAL,\n" );
	    sql.append("               A.STATUS,\n" );
	    sql.append("               TO_CHAR(A.REPORT_DATE, 'YYYY-MM') REPORT_DATE,\n" );
	    sql.append("               A.BALANCE_YIELDLY BALANCE_YIELDLY,\n" );
	    sql.append("               CASE WHEN A.CLAIM_TYPE NOT IN (10661002, 10661006) THEN 10661001 /*维修类*/\n" );
	    sql.append("                    WHEN A.CLAIM_TYPE = 10661002 THEN 10661002 /*保养类*/\n" );
	    sql.append("                    WHEN D.ACTIVITY_CODE IS NOT NULL AND A.CLAIM_TYPE = 10661006\n" );
	    sql.append("                         AND E.SUBJECT_NO = 'PDI' AND E.ACTIVITY_TYPE = 10561001 THEN 10661011 /*PDI*/\n" );
	    sql.append("                    WHEN D.ACTIVITY_CODE IS NOT NULL AND A.CLAIM_TYPE = 10661006\n" );
	    sql.append("                         AND E.SUBJECT_NO <> 'PDI' AND E.ACTIVITY_TYPE = 10561001 THEN 10661006 /*服务活动除PDI*/\n" );
	    sql.append("                    ELSE 0 END CLAIM_TYPE\n" );
	    sql.append("          FROM TM_DEALER B\n" );
	    sql.append("          JOIN TT_AS_WR_APPLICATION A ON A.DEALER_ID = B.DEALER_ID\n" );
	    sql.append("          JOIN TM_VHCL_MATERIAL_GROUP T ON T.GROUP_CODE = A.MODEL_CODE\n" );
	    sql.append("          LEFT JOIN TT_AS_ACTIVITY D ON A.CAMPAIGN_CODE = D.ACTIVITY_CODE\n" );
	    sql.append("          LEFT JOIN TT_AS_ACTIVITY_SUBJECT E ON D.SUBJECT_ID = E.SUBJECT_ID\n" );
	    sql.append("         WHERE A.IS_LETTER = 1\n" );
	    sql.append("           AND A.BALANCE_YIELDLY = "+bansorg+"\n" );
	    if(Utility.testString(bean.getClaimStatus())){
			sql.append(" AND A.STATUS = ").append(bean.getClaimStatus()).append("\n");
		}else{
			sql.append(" AND A.STATUS IN ('"+Constant.CLAIM_APPLY_ORD_TYPE_08+"')\n" );
		}
		if(bansorg.equals("95411001"))
		{
			
			    MaterialGroupManagerDao managerDao = new MaterialGroupManagerDao();
			 	sql.append(managerDao.getOrgDealerLimitSqlByServiceA("A", logonUser));
		}
		if(Utility.testString(dealerName)){
			sql.append(" AND B.DEALER_NAME LIKE '%"+dealerName+"%'\n");
		}
		sql.append("  AND A.CREATE_DATE >= to_date(?,'yyyy-mm-dd') ");
		if(Utility.testString(bean.getDealerCode()))
		{
			sql.append(" AND B.DEALER_CODE LIKE '%"+bean.getDealerCode()+"%'\n");
		}
		if(Utility.testString(bean.getReviewBeginDate()))
		{

			sql.append(" AND A.REPORT_DATE >= " ).append("to_date('"+bean.getReviewBeginDate().substring(0,7)+"','yyyy-mm')" ).append("\n");
			sql.append(" AND A.REPORT_DATE <= " ).append("(LAST_DAY (to_date('"+bean.getReviewBeginDate()+"','yyyy-mm-dd'))+1)" ).append("\n");
		}
		sql.append("                    ) M\n");
		sql.append("   where M.CLAIM_TYPE != 0  GROUP BY M.DEALER_ID, M.DEALER_NAME, M.STATUS, M.REPORT_DATE, M.CLAIM_TYPE");
		sql.append("  ) D ");
		
		 if(  (!Utility.testString(bean.getClaimStatus())) || (Utility.testString(bean.getClaimStatus()) &&  bean.getClaimStatus().equals("10791008")) )
		 {
			sql.append(" ,(  SELECT A.DEALER_ID DEALER_ID,\n" );
			sql.append("       to_char(A.REPORT_DATE, 'yyyy-mm') REPORT_DATE,\n" );
			sql.append("       min( case when B.IS_RETURN = 95361002 then 1\n" );
			sql.append("       when B.IS_RETURN = 95361001  and A.IS_OLD_AUDIT != 0 then 1\n" );
			sql.append("       when B.IS_RETURN is null then 1\n" );
			sql.append("       else 0  end ) status\n" );
			sql.append("  from TT_AS_WR_APPLICATION A  left join  TT_AS_WR_PARTSITEM B on A.ID = B.ID\n" );
			sql.append(" WHERE\n" );
			sql.append("   A.REPORT_DATE > to_date('2013-08-24', 'yyyy-mm-dd')\n" );
			sql.append("   and A.BALANCE_YIELDLY = \n" +  bansorg );
			sql.append("   AND A.STATUS = 10791008\n" );
			if(bansorg.equals("95411001"))
			{
				
				    MaterialGroupManagerDao managerDao = new MaterialGroupManagerDao();
				 	sql.append(managerDao.getOrgDealerLimitSqlByServiceA("A", logonUser));
			}
			
			sql.append(" group by A.DEALER_ID, to_char(A.REPORT_DATE, 'yyyy-mm')\n" );
			sql.append("    ) RES");
			sql.append("  WHERE \n" );
			sql.append("    D.DEALER_ID = res.DEALER_ID\n" );
			sql.append("  AND D.REPORT_DATE = res.REPORT_DATE\n" );
			sql.append("  AND res.status != 0\n" );
		 }
		
		
		
		
		List<Object> params = new ArrayList<Object>();
		params.add("2013-08-01");
		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), params, getFunName(), pageSize, curPage);
		return ps;
	}
	
	
	
	
	public PageResult<Map<String, Object>> queryAuditingClaimYXJC(auditBean bean,String dealerName,int userType,long userID,String bansorg,String subject_id,int curPage, int pageSize){
		StringBuffer sql= new StringBuffer();
		sql.append("select DEALER_NAME,\n" );
		sql.append("       DEALER_ID,\n" );
		sql.append("       STATUS,\n" );
		sql.append(" SUBJECT_ID, SUBJECT_NAME,");
		sql.append("       sum(BALANCE_AMOUNT) as BALANCE_AMOUNT,\n" );
		sql.append("       sum(REPAIR_TOTAL) as REPAIR_TOTAL,min(BALANCE_YIELDLY) as BALANCE_YIELDLY, \n" );
		sql.append("       sum(TOTAL) as TOTAL ,min(CLAIM_TYPE) CLAIM_TYPE from (");

		sql.append(" SELECT  B.DEALER_SHORTNAME DEALER_NAME,A.DEALER_ID,F.SUBJECT_NAME,F.SUBJECT_ID,SUM(A.BALANCE_AMOUNT) BALANCE_AMOUNT,SUM(NVL(A.REPAIR_TOTAL,0)) AS REPAIR_TOTAL,COUNT(*) AS TOTAL,A.STATUS,\n" );
		/******MOD BY LIUXH 20101203 索赔单申请费用加上追加工时******/
		sql.append(" min(A.BALANCE_YIELDLY) BALANCE_YIELDLY,min(A.CLAIM_TYPE) as CLAIM_TYPE");
		sql.append(" FROM TM_DEALER B,TT_AS_WR_APPLICATION A,TM_VHCL_MATERIAL_GROUP T, TT_AS_ACTIVITY G ,TT_AS_ACTIVITY_SUBJECT F \n" );
		sql.append(" WHERE  A.DEALER_ID = B.DEALER_ID  AND  A.CLAIM_TYPE != 10661002 AND T.GROUP_CODE = A.MODEL_CODE \n" );
		sql.append(" AND G.SUBJECT_ID =  F.SUBJECT_ID ");
		sql.append(" AND A.CAMPAIGN_CODE = G.ACTIVITY_CODE "); 
		sql.append(" AND  A.CLAIM_TYPE = "+Constant.CLA_TYPE_06 );
		sql.append(" AND A.CAMPAIGN_CODE not in (SELECT D.ACTIVITY_CODE from TT_AS_ACTIVITY D,TT_AS_ACTIVITY_SUBJECT E where D.SUBJECT_ID = E.SUBJECT_ID and E.ACTIVITY_TYPE = "+Constant.SERVICEACTIVITY_TYPE_01+")");
		if(Utility.testString(dealerName)){
			sql.append(" AND B.DEALER_NAME LIKE '%"+dealerName+"%'\n");
		}
		
		if(Utility.testString(subject_id)){
			sql.append(" AND F.SUBJECT_NO like '"+subject_id+"'\n");
		}
    	
		if(Utility.testString(bean.getClaimStatus())){
			sql.append(" AND A.STATUS = ").append(bean.getClaimStatus()).append("\n");
		}else{
			sql.append(" AND A.STATUS IN ('"+Constant.CLAIM_APPLY_ORD_TYPE_08+"')\n" );
		}
		sql.append(" GROUP BY A.DEALER_ID,B.DEALER_SHORTNAME,A.STATUS,F.SUBJECT_ID,F.SUBJECT_NAME ");
		sql.append(" ) group by DEALER_ID, DEALER_NAME, STATUS,CLAIM_TYPE ,SUBJECT_NAME, SUBJECT_ID ");
		
		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
		return ps;
	}
	
	public PageResult<Map<String, Object>> queryDealerAuditingClaim(String report_date, String YIELDLY_TYPE, String dealerId,int curPage, int pageSize){
		 
		StringBuffer sql= new StringBuffer();
		// 艾春 9.12 添加
		sql.append( "SELECT B.DEALER_SHORTNAME DEALER_NAME,\n");
		sql.append( "       b.DEALER_ID,\n"); 
		sql.append( "       SUM(DECODE(A.STATUS, 10791006, 0, 10791008, 0, A.BALANCE_AMOUNT)) AS BALANCE_AMOUNT,\n"); 
		sql.append( "       SUM(NVL(A.REPAIR_TOTAL, 0)) AS REPAIR_TOTAL,\n"); 
		sql.append( "       COUNT(*) AS TOTAL,\n"); 
		sql.append( "       TO_CHAR(A.REPORT_DATE, 'yyyy-mm') REPORT_DATE,\n"); 
		sql.append( "       MIN(A.BALANCE_YIELDLY) BALANCE_YIELDLY,\n"); 
		sql.append( "       DECODE(MIN(A.CLAIM_TYPE), 0, 10661001, 10661001) AS CLAIM_TYPE\n"); 
		sql.append( "  FROM TT_AS_WR_APPLICATION A\n"); 
		sql.append( "  JOIN TM_DEALER B ON  A.DEALER_ID = B.DEALER_ID \n"); 
		sql.append(" where  (  NVL(A.Second_Dealer_Id ,A.DEALER_ID) in  (select d.dealer_id  from tm_dealer d  start with d.dealer_id = "+dealerId+"   connect by PRIOR d.dealer_id = d.parent_dealer_d)) \n");
		if(Utility.testString(report_date)){
			sql.append(" AND A.REPORT_DATE >= to_date('").append(report_date.substring(0,7)).append("-01','yyyy-mm-dd')\n");
			sql.append(" AND A.REPORT_DATE <= last_day(to_date('").append(report_date.substring(0,7)).append("-01','yyyy-mm-dd')) + 1 - 1/24/60/60 \n");
		}else{
			sql.append(" and A.REPORT_DATE is not null \n");
		}
		if(Utility.testString(YIELDLY_TYPE)){
			sql.append(" AND A.BALANCE_YIELDLY = '").append(YIELDLY_TYPE).append("'\n");
		}
		sql.append(" GROUP BY B.DEALER_ID,B.DEALER_SHORTNAME,to_char(A.REPORT_DATE,'yyyy-mm') ");
		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
		return ps;
	}
	
	public PageResult<Map<String, Object>> querycarparkAuditingClaim(String report_date,String last_report_date,String DIRECTOR_DATE,String LAST_DIRECTOR_DATE,String dealerId,String balance_yieldly,Long userID,AclUserBean logonUser,int curPage, int pageSize){
		
		 MaterialGroupManagerDao managerDao = new MaterialGroupManagerDao();
		  String judesql  =  managerDao.getOrgDealerLimitSqlByServiceA("A", logonUser);
		  String judesq  =  managerDao.getOrgDealerLimitSqlByServiceB("A", logonUser);
	    List<TmDealerPO> dlist= super.select(TmDealerPO.class, judesq, null);
		
		StringBuffer sql= new StringBuffer();
		sql.append("select min(DEALER_NAME) DEALER_NAME,\n" );
		sql.append("       DEALER_ID,min(DEALER_CODE) DEALER_CODE,to_char(max(hhcreat_date),'yyyy-mm-dd') hhcreat_date,to_char(max(DIRECTOR_DATE),'yyyy-mm-dd') DIRECTOR_DATE,to_char(max(REVIEW_APPLICATION_TIME),'yyyy-mm-dd') REVIEW_APPLICATION_TIME,to_char(max(CREATE_DATE),'yyyy-mm-dd') CREATE_DATE,\n" );
		sql.append(" REPORT_DATE,max(tickets_ENDDATE) tickets_ENDDATE, ");
		sql.append("       sum(BALANCE_AMOUNT) as BALANCE_AMOUNT,\n" );
		sql.append("       sum(REPAIR_TOTAL) as REPAIR_TOTAL,min(BALANCE_YIELDLY) as BALANCE_YIELDLY, \n" );
		sql.append("       sum(TOTAL) as TOTAL  from (");

		sql.append(" SELECT  min(B.DEALER_SHORTNAME) DEALER_NAME,min(B.DEALER_CODE) DEALER_CODE,A.DEALER_ID,max(hh.creat_date ) hhcreat_date ,max(A.DIRECTOR_DATE) DIRECTOR_DATE,max(g.CREATE_DATE) CREATE_DATE , max(g.REVIEW_APPLICATION_TIME)  REVIEW_APPLICATION_TIME ,sum( decode(A.STATUS,10791006,0,10791008,0,A.BALANCE_AMOUNT)) as BALANCE_AMOUNT,SUM(NVL(A.REPAIR_TOTAL,0)) AS REPAIR_TOTAL,COUNT(*) AS TOTAL,\n" );
		/******MOD BY LIUXH 20101203 索赔单申请费用加上追加工时******/
		sql.append(" to_char(A.REPORT_DATE,'yyyy-mm') REPORT_DATE,min(A.BALANCE_YIELDLY) BALANCE_YIELDLY,decode(min(A.CLAIM_TYPE),0,10661001,10661001) as CLAIM_TYPE");
		sql.append("  ,to_char(max(e.ENDDATE),'yyyy-mm-dd') tickets_ENDDATE  FROM TM_DEALER B,TM_VHCL_MATERIAL_GROUP T,TT_AS_WR_APPLICATION A \n" );
		
	    
	    sql.append("left join tt_as_wr_tickets e\n" );
	    sql.append("on  to_char(A.REPORT_DATE,'yyyymm') = substr(e.GOODSNUM,length(e.GOODSNUM)-5,length(e.GOODSNUM))\n" );
	    sql.append("and A.BALANCE_YIELDLY = e.BALANCE_YIELDLY\n" );
	    sql.append(" and A.DEALER_ID = e.DEALERID ");
	    
	    sql.append("    left join (SELECT max(t.START_DATE)  START_DATE , max(t.END_DATE)  END_DATE ,max(t.CREATE_DATE)  CREATE_DATE,max(t.DEALER_ID)  DEALER_ID ,max(t.REVIEW_APPLICATION_TIME) REVIEW_APPLICATION_TIME,max(t.REMARK) REMARK from  tt_as_wr_claim_balance t group by t.DEALER_ID,t.START_DATE  )  g   on  to_char(g.END_DATE,'yyyy-mm') =  to_char(A.REPORT_DATE,'yyyy-mm')\n" );
	    sql.append("and A.DEALER_ID = g.DEALER_ID");
	    sql.append(" left join TT_AS_payment hh  on hh.BALANCE_ODER = g.REMARK");


	    
		sql.append(" WHERE  A.DEALER_ID = B.DEALER_ID   AND T.GROUP_CODE = A.MODEL_CODE \n" );
		sql.append(" and A.REPORT_DATE is not null");
    	if(Utility.testString(dealerId))
    	{
    		sql.append(" and A.DEALER_ID = "+dealerId );
    	}
    	if(dlist.size() > 0)
    	{
    		sql.append(judesql);
    	}
    	sql.append(" and A.CLAIM_TYPE != 10661006 ");
    	sql.append(" and A.BALANCE_YIELDLY = "+ balance_yieldly);
		if(Utility.testString(report_date)){
			sql.append(" AND A.REPORT_DATE >= " ).append("to_date('"+report_date.substring(0,7)+"','yyyy-mm')" ).append("\n");
		}
		if(Utility.testString(last_report_date))
		{
			sql.append(" AND A.REPORT_DATE <= " ).append("(LAST_DAY (to_date('"+last_report_date+"','yyyy-mm-dd'))+1)" ).append("\n");
		}
		sql.append(" AND A.REPORT_DATE >= to_date('2013-08-01','yyyy-mm-dd')  ");
		if(Utility.testString(DIRECTOR_DATE)){
			sql.append(" AND A.DIRECTOR_DATE >= " ).append("to_date('"+DIRECTOR_DATE.substring(0,7)+"','yyyy-mm')" ).append("\n");
		}
		if(Utility.testString(LAST_DIRECTOR_DATE))
		{
			
			sql.append(" AND A.DIRECTOR_DATE <= " ).append("(LAST_DAY (to_date('"+LAST_DIRECTOR_DATE+"','yyyy-mm-dd'))+1)" ).append("\n");
		}
		if(Utility.testString(DIRECTOR_DATE) || Utility.testString(LAST_DIRECTOR_DATE))
		{
			sql.append(" and (A.STATUS = 10791013 or  A.STATUS = 10791016)  ");
		}
		
		sql.append(" GROUP BY A.DEALER_ID,to_char(A.REPORT_DATE,'yyyy-mm') ");
		
		sql.append(" UNION all ");
		
		sql.append(" SELECT  min(B.DEALER_SHORTNAME) DEALER_NAME,min(B.DEALER_CODE) DEALER_CODE,A.DEALER_ID,max(hh.creat_date ) hhcreat_date,max(A.DIRECTOR_DATE) DIRECTOR_DATE,max(g.CREATE_DATE) CREATE_DATE , max(g.REVIEW_APPLICATION_TIME)  REVIEW_APPLICATION_TIME ,sum( decode(A.STATUS,10791006,0,10791008,0,A.BALANCE_AMOUNT)) as BALANCE_AMOUNT,SUM(NVL(A.REPAIR_TOTAL,0)) AS REPAIR_TOTAL,COUNT(*) AS TOTAL,\n" );
		/******MOD BY LIUXH 20101203 索赔单申请费用加上追加工时******/
		sql.append(" to_char(A.REPORT_DATE,'yyyy-mm') REPORT_DATE,min(A.BALANCE_YIELDLY) BALANCE_YIELDLY,decode(min(A.CLAIM_TYPE),0,10661001,10661001) as CLAIM_TYPE");
		sql.append(" ,to_char(max(e.ENDDATE),'yyyy-mm-dd') tickets_ENDDATE FROM TM_DEALER B,TM_VHCL_MATERIAL_GROUP T,TT_AS_WR_APPLICATION A \n" );
		
	    
	    sql.append("left join tt_as_wr_tickets e\n" );
	    sql.append("on  to_char(A.REPORT_DATE,'yyyymm') = substr(e.GOODSNUM,length(e.GOODSNUM)-5,length(e.GOODSNUM))\n" );
	    sql.append("and A.BALANCE_YIELDLY = e.BALANCE_YIELDLY\n" );
	    sql.append(" and A.DEALER_ID = e.DEALERID ");
	    
	    sql.append("    left join (SELECT max(t.START_DATE)  START_DATE ,  max(t.END_DATE) END_DATE ,max(t.CREATE_DATE)  CREATE_DATE,max(t.DEALER_ID)  DEALER_ID ,max(t.REVIEW_APPLICATION_TIME) REVIEW_APPLICATION_TIME,max(t.REMARK) REMARK  from tt_as_wr_claim_balance t group by t.DEALER_ID,t.START_DATE  )  g   on  to_char(g.END_DATE,'yyyy-mm') =  to_char(A.REPORT_DATE,'yyyy-mm')\n" );
	    sql.append("and A.DEALER_ID = g.DEALER_ID");
	    sql.append(" left join TT_AS_payment hh  on hh.BALANCE_ODER = g.REMARK");
	    
		sql.append(" WHERE  A.DEALER_ID = B.DEALER_ID   AND T.GROUP_CODE = A.MODEL_CODE \n" );
		sql.append(" and A.REPORT_DATE is not null");
    	if(Utility.testString(dealerId))
    	{
    		sql.append(" and A.DEALER_ID = "+dealerId );
    	}
    	if(dlist.size() > 0)
    	{
    		sql.append(judesql);
    	}
    	sql.append(" and A.CLAIM_TYPE = 10661006 ");
    	sql.append(" AND A.CAMPAIGN_CODE  in (SELECT D.ACTIVITY_CODE from TT_AS_ACTIVITY D,TT_AS_ACTIVITY_SUBJECT E where D.SUBJECT_ID = E.SUBJECT_ID  and E.ACTIVITY_TYPE = "+Constant.SERVICEACTIVITY_TYPE_01+")");
    	sql.append(" and A.BALANCE_YIELDLY = "+ balance_yieldly);
		if(Utility.testString(report_date)){
				sql.append(" AND A.REPORT_DATE >= " ).append("to_date('"+report_date.substring(0,7)+"','yyyy-mm')" ).append("\n");
		}
		if(Utility.testString(last_report_date))
		{
			sql.append(" AND A.REPORT_DATE <= " ).append("(LAST_DAY (to_date('"+last_report_date+"','yyyy-mm-dd'))+1)" ).append("\n");
		}
		if(Utility.testString(DIRECTOR_DATE)){
			sql.append(" AND A.DIRECTOR_DATE >= " ).append("to_date('"+DIRECTOR_DATE.substring(0,7)+"','yyyy-mm')" ).append("\n");
		}
		sql.append(" AND A.REPORT_DATE >= to_date('2013-08-01','yyyy-mm-dd')  ");
		if(Utility.testString(LAST_DIRECTOR_DATE))
		{
			
			sql.append(" AND A.DIRECTOR_DATE <= " ).append("(LAST_DAY (to_date('"+LAST_DIRECTOR_DATE+"','yyyy-mm-dd'))+1)" ).append("\n");
		}
		if(Utility.testString(DIRECTOR_DATE) || Utility.testString(LAST_DIRECTOR_DATE))
		{
			sql.append(" and (A.STATUS = 10791013 or  A.STATUS = 10791016)  ");
		}
		sql.append(" GROUP BY A.DEALER_ID,to_char(A.REPORT_DATE,'yyyy-mm') ");
		
		sql.append(") group by DEALER_ID, REPORT_DATE order by REPORT_DATE desc");
		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
		return ps;
	}
	
	public PageResult<Map<String, Object>> querycarparkAuditingClaimyx(String report_date,String last_report_date,String dealerId,int curPage, int pageSize){
		 
		StringBuffer sql= new StringBuffer();
		sql.append("select DEALER_NAME,\n" );
		sql.append("       DEALER_ID,min(DEALER_CODE) DEALER_CODE,\n" );
		sql.append(" REPORT_DATE,");
		sql.append("       sum(BALANCE_AMOUNT) as BALANCE_AMOUNT,\n" );
		sql.append("       sum(REPAIR_TOTAL) as REPAIR_TOTAL,min(BALANCE_YIELDLY) as BALANCE_YIELDLY, \n" );
		sql.append("       sum(TOTAL) as TOTAL  from (");
		sql.append(" SELECT  B.DEALER_SHORTNAME DEALER_NAME,min(B.DEALER_CODE) DEALER_CODE,A.DEALER_ID,sum( decode(A.STATUS,10791006,0,10791008,0,A.BALANCE_AMOUNT)) as BALANCE_AMOUNT,SUM(NVL(A.REPAIR_TOTAL,0)) AS REPAIR_TOTAL,COUNT(*) AS TOTAL,\n" );
		/******MOD BY LIUXH 20101203 索赔单申请费用加上追加工时******/
		sql.append(" to_char(A.REPORT_DATE,'yyyy-mm') REPORT_DATE,min(A.BALANCE_YIELDLY) BALANCE_YIELDLY,decode(min(A.CLAIM_TYPE),0,10661001,10661001) as CLAIM_TYPE");
		sql.append(" FROM TM_DEALER B,TT_AS_WR_APPLICATION A,TM_VHCL_MATERIAL_GROUP T \n" );
		sql.append(" WHERE  A.DEALER_ID = B.DEALER_ID   AND T.GROUP_CODE = A.MODEL_CODE \n" );
		sql.append(" and A.REPORT_DATE is not null");
    	if(Utility.testString(dealerId))
    	{
    		sql.append(" and A.DEALER_ID = "+dealerId );
    	}
    	sql.append(" and A.CLAIM_TYPE = 10661006 ");
    	sql.append(" AND A.CAMPAIGN_CODE  not in (SELECT D.ACTIVITY_CODE from TT_AS_ACTIVITY D,TT_AS_ACTIVITY_SUBJECT E where D.SUBJECT_ID = E.SUBJECT_ID  and E.ACTIVITY_TYPE = "+Constant.SERVICEACTIVITY_TYPE_01+")");
		if(Utility.testString(report_date)){
			sql.append(" AND A.REPORT_DATE >= " ).append("to_date('"+report_date.substring(0,7)+"','yyyy-mm')" ).append("\n");
		}
		if(Utility.testString(last_report_date))
		{
			sql.append(" AND A.REPORT_DATE <= " ).append("(LAST_DAY (to_date('"+last_report_date+"','yyyy-mm-dd'))+1)" ).append("\n");
		}
		
		
		sql.append(" GROUP BY A.DEALER_ID,B.DEALER_SHORTNAME,to_char(A.REPORT_DATE,'yyyy-mm') ");
		
		sql.append(") group by DEALER_ID, DEALER_NAME, REPORT_DATE");
		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
		return ps;
	}
	
	
	public PageResult<Map<String, Object>> queryAuditingClaimJC1(auditBean bean,String dealerName,int userType,long userID,String bansorg,String person,int curPage, int pageSize){
		 
		StringBuffer sql= new StringBuffer();
		sql.append("select DEALER_NAME,\n" );
		sql.append("       DEALER_ID,\n" );
		sql.append(" REPORT_DATE,");
		sql.append("       sum(BALANCE_AMOUNT) as BALANCE_AMOUNT,\n" );
		sql.append("       sum(REPAIR_TOTAL) as REPAIR_TOTAL,min(BALANCE_YIELDLY) as BALANCE_YIELDLY, \n" );
		sql.append("       sum(TOTAL) as TOTAL  from (");

		sql.append(" SELECT  B.DEALER_SHORTNAME DEALER_NAME,A.DEALER_ID,SUM(decode(A.STATUS,10791006,0,A.BALANCE_AMOUNT)) BALANCE_AMOUNT,SUM(NVL(A.REPAIR_TOTAL,0)) AS REPAIR_TOTAL,COUNT(*) AS TOTAL,\n" );
		/******MOD BY LIUXH 20101203 索赔单申请费用加上追加工时******/
		sql.append(" to_char(A.REPORT_DATE,'yyyy-mm') REPORT_DATE,min(A.BALANCE_YIELDLY) BALANCE_YIELDLY,decode(min(A.CLAIM_TYPE),0,10661001,10661001) as CLAIM_TYPE");
		sql.append(" FROM TM_DEALER B,TT_AS_WR_APPLICATION A,TM_VHCL_MATERIAL_GROUP T \n" );
		sql.append(" WHERE  A.DEALER_ID = B.DEALER_ID  AND T.GROUP_CODE = A.MODEL_CODE \n" );
		if(userType == 0)
		{
			sql.append(" AND  A.CLAIM_TYPE = "+Constant.CLA_TYPE_06 );
			sql.append(" AND A.CAMPAIGN_CODE not in (SELECT D.ACTIVITY_CODE from TT_AS_ACTIVITY D,TT_AS_ACTIVITY_SUBJECT E where D.SUBJECT_ID = E.SUBJECT_ID and E.ACTIVITY_TYPE = "+Constant.SERVICEACTIVITY_TYPE_01+")");
		}
		else 
		{
			sql.append(" AND A.IS_LETTER = 1   AND  A.BALANCE_YIELDLY= " + bansorg);
			sql.append(" AND   A.CLAIM_TYPE != "+Constant.CLA_TYPE_06 );
		}
		
		if(Utility.testString(dealerName)){
			sql.append(" AND B.DEALER_CODE LIKE '%"+dealerName+"%'\n");
		}
    	
		if(Utility.testString(bean.getClaimStatus())){
			sql.append(" AND A.REPORT_DATE >= " ).append("to_date('"+bean.getClaimStatus().substring(0,7)+"','yyyy-mm')" ).append("\n");
			sql.append(" AND A.REPORT_DATE <= " ).append("(LAST_DAY (to_date('"+bean.getClaimStatus()+"','yyyy-mm-dd'))+1)" ).append("\n");
		}
		sql.append(" AND A.STATUS IN ('"+10791007+"','"+10791010+"','"+10791011+"','"+10791012+"','"+10791014+"')\n" );
		sql.append(" GROUP BY A.DEALER_ID,B.DEALER_SHORTNAME,to_char(A.REPORT_DATE,'yyyy-mm') ");		
		if(userType == 1)
		{
			sql.append(" UNION");
			sql.append(" SELECT  B.DEALER_SHORTNAME DEALER_NAME,A.DEALER_ID,SUM(decode(A.STATUS,10791006,0,A.BALANCE_AMOUNT)) BALANCE_AMOUNT,SUM(NVL(A.REPAIR_TOTAL,0)) AS REPAIR_TOTAL,COUNT(*) AS TOTAL,\n" );
			/******MOD BY LIUXH 20101203 索赔单申请费用加上追加工时******/
			sql.append(" to_char(A.REPORT_DATE,'yyyy-mm') REPORT_DATE,min(A.BALANCE_YIELDLY) BALANCE_YIELDLY,min(A.CLAIM_TYPE) as CLAIM_TYPE ");
			sql.append("FROM TM_DEALER B,TT_AS_WR_APPLICATION A,TM_VHCL_MATERIAL_GROUP T \n" );
			sql.append(" WHERE A.DEALER_ID = B.DEALER_ID and A.BALANCE_YIELDLY= "+bansorg+" and  A.IS_LETTER = 1  AND T.GROUP_CODE = A.MODEL_CODE \n" );
			sql.append(" AND   A.CLAIM_TYPE = "+Constant.CLA_TYPE_06 );
			sql.append(" AND A.CAMPAIGN_CODE  in (SELECT D.ACTIVITY_CODE from TT_AS_ACTIVITY D,TT_AS_ACTIVITY_SUBJECT E where D.SUBJECT_ID = E.SUBJECT_ID  and  E.ACTIVITY_TYPE = "+Constant.SERVICEACTIVITY_TYPE_01+")");
			if(Utility.testString(dealerName)){
				sql.append(" AND B.DEALER_CODE LIKE '%"+dealerName+"%'\n");
			}
	    	
			if(Utility.testString(bean.getClaimStatus())){
				sql.append(" AND A.REPORT_DATE >= " ).append("to_date('"+bean.getClaimStatus().substring(0,7)+"','yyyy-mm')" ).append("\n");
				sql.append(" AND A.REPORT_DATE <= " ).append("(LAST_DAY (to_date('"+bean.getClaimStatus()+"','yyyy-mm-dd'))+1)" ).append("\n");
			}
			sql.append(" AND A.STATUS IN ('"+10791007+"','"+10791010+"','"+10791011+"','"+10791012+"','"+10791014+"')\n" );
			sql.append(" GROUP BY A.DEALER_ID,B.DEALER_SHORTNAME,A.STATUS,to_char(A.REPORT_DATE,'yyyy-mm')");
			
			
			sql.append(" UNION");
			sql.append(" SELECT  B.DEALER_SHORTNAME DEALER_NAME,A.DEALER_ID,SUM(decode(A.STATUS,10791006,0,A.BALANCE_AMOUNT)) BALANCE_AMOUNT,SUM(NVL(A.REPAIR_TOTAL,0)) AS REPAIR_TOTAL,COUNT(*) AS TOTAL,\n" );
			/******MOD BY LIUXH 20101203 索赔单申请费用加上追加工时******/
			sql.append(" to_char(A.REPORT_DATE,'yyyy-mm') REPORT_DATE,min(A.BALANCE_YIELDLY) BALANCE_YIELDLY,decode(min(A.CLAIM_TYPE),0,10661006,10661011) CLAIM_TYPE ");
			sql.append("FROM TM_DEALER B,TT_AS_WR_APPLICATION A,TM_VHCL_MATERIAL_GROUP T \n" );
			sql.append(" WHERE A.DEALER_ID = B.DEALER_ID and A.BALANCE_YIELDLY= "+bansorg+" and  A.IS_LETTER = 1  AND T.GROUP_CODE = A.MODEL_CODE \n" );
			sql.append(" AND   A.CLAIM_TYPE = "+Constant.CLA_TYPE_06 );
			sql.append(" AND A.CAMPAIGN_CODE  in (SELECT D.ACTIVITY_CODE from TT_AS_ACTIVITY D,TT_AS_ACTIVITY_SUBJECT E where D.SUBJECT_ID = E.SUBJECT_ID   and E.ACTIVITY_TYPE = "+Constant.SERVICEACTIVITY_TYPE_01+")");
			if(Utility.testString(dealerName)){
				sql.append(" AND B.DEALER_CODE LIKE '%"+dealerName+"%'\n");
			}
	    	
			if(Utility.testString(bean.getClaimStatus())){
				sql.append(" AND A.REPORT_DATE >= " ).append("to_date('"+bean.getClaimStatus().substring(0,7)+"','yyyy-mm')" ).append("\n");
				sql.append(" AND A.REPORT_DATE <= " ).append("(LAST_DAY (to_date('"+bean.getClaimStatus()+"','yyyy-mm-dd'))+1)" ).append("\n");
			}
			sql.append(" AND A.STATUS IN ('"+10791006+"')\n" );
			sql.append(" and ( (A.DIRECTOR_DATE is null) or (A.section_date > A.DIRECTOR_DATE )  )");
			sql.append(" GROUP BY A.DEALER_ID,B.DEALER_SHORTNAME,to_char(A.REPORT_DATE,'yyyy-mm')");
			
			sql.append(" UNION");
			sql.append(" SELECT  B.DEALER_SHORTNAME DEALER_NAME,A.DEALER_ID,SUM(decode(A.STATUS,10791006,0,A.BALANCE_AMOUNT)) BALANCE_AMOUNT,SUM(NVL(A.REPAIR_TOTAL,0)) AS REPAIR_TOTAL,COUNT(*) AS TOTAL,\n" );
			/******MOD BY LIUXH 20101203 索赔单申请费用加上追加工时******/
			sql.append(" to_char(A.REPORT_DATE,'yyyy-mm') REPORT_DATE,min(A.BALANCE_YIELDLY) BALANCE_YIELDLY,decode(min(A.CLAIM_TYPE),0,10661006,10661011) CLAIM_TYPE ");
			sql.append("FROM TM_DEALER B,TT_AS_WR_APPLICATION A,TM_VHCL_MATERIAL_GROUP T \n" );
			sql.append(" WHERE A.DEALER_ID = B.DEALER_ID and A.BALANCE_YIELDLY= "+bansorg+" and  A.IS_LETTER = 1  AND T.GROUP_CODE = A.MODEL_CODE \n" );
			sql.append(" AND   A.CLAIM_TYPE != "+Constant.CLA_TYPE_06 );
			if(Utility.testString(dealerName)){
				sql.append(" AND B.DEALER_CODE LIKE '%"+dealerName+"%'\n");
			}
	    	
			if(Utility.testString(bean.getClaimStatus())){
				sql.append(" AND A.REPORT_DATE >= " ).append("to_date('"+bean.getClaimStatus().substring(0,7)+"','yyyy-mm')" ).append("\n");
				sql.append(" AND A.REPORT_DATE <= " ).append("(LAST_DAY (to_date('"+bean.getClaimStatus()+"','yyyy-mm-dd'))+1)" ).append("\n");
			}
			sql.append(" AND A.STATUS IN ('"+10791006+"')\n" );
			sql.append(" and ( (A.DIRECTOR_DATE is null) or (A.section_date > A.DIRECTOR_DATE )  )");
			sql.append(" GROUP BY A.DEALER_ID,B.DEALER_SHORTNAME,to_char(A.REPORT_DATE,'yyyy-mm')");
		}
		sql.append(") ");
		if(Utility.testString(person))
		{
			List<TcPosePO> poseList = CommonDAO.queryUserPosition(Long.parseLong(person));
			if(poseList != null && poseList.size() > 0)
			{
				Long poseId= poseList.get(0).getPoseId();
				
				sql.append("  where DEALER_ID in ( "+MaterialGroupManagerDao.getOrgDealerLimitSqlByServiceD("", poseId)+")");
			}
		}
		sql.append(" group by DEALER_ID, DEALER_NAME, REPORT_DATE ");
		
		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
		return ps;
	}
	/*******************************add by kevinyin 20110413****************************************************/
	
	/*******************************add by kevinyin 20110414****************************************************/
	/**
	 * 查询审核人员
	 */
	public List<TcUserPO> queryAuditingUserJC(){
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT U.USER_ID, U.ACNT, U.NAME, U.BALANCE_LEVEL_CODE\n");
		sql.append("  FROM TC_USER U\n");
		sql.append(" WHERE U.BALANCE_LEVEL_CODE IS NOT NULL\n");
		sql.append("   AND (U.USER_TYPE = "+Constant.SYS_USER_SGM+"\n");
		sql.append(" OR U.USER_TYPE IS NULL)\n");


		List<TcUserPO> listUser = this.pageQuery(sql.toString(), null, this.getFunName());
		return listUser;
	}
	
	/**
	 * 查询审核索赔单
	 */
	public PageResult<Map<String, Object>> queryClaimAccAuditByIdJC(auditBean bean,int curPage, int pageSize){
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT ROWNUM NUM, A.ID CLAIM_ID, A.CLAIM_NO, A.YIELDLY,\n" );
		sql.append("       TO_CHAR(A.REPORT_DATE, 'YYYY-MM-DD') REPORT_DATE,\n" );
		sql.append("       TO_CHAR(A.AUDITING_DATE, 'YYYY-MM-DD') AUDITING_DATE,\n" );
		sql.append("       A.SERIES_NAME, A.MODEL_NAME, A.VIN, \n" );
		//sql.append("     B.DEALER_SHORTNAME DEALER_NAME,A.CLAIM_TYPE,A.STATUS,A.AUTH_CODE,A.BALANCE_AMOUNT,A.REPAIR_TOTAL,\n" );
		/******MOD BY LIUXH 20101203 索赔单申请费用加上追加工时******/
		sql.append("     B.DEALER_SHORTNAME DEALER_NAME,A.CLAIM_TYPE,A.STATUS,A.AUTH_CODE,A.BALANCE_AMOUNT,(NVL(A.REPAIR_TOTAL,0)) AS REPAIR_TOTAL,\n" );
		/******MOD BY LIUXH 20101203 索赔单申请费用加上追加工时******/
		sql.append("     (CASE WHEN A.STATUS = "+Constant.CLAIM_APPLY_ORD_TYPE_08+" AND A.AUTH_CODE = '"+CommonUtils.checkNull(bean.getAuthCodeOrder())+"' THEN 1 ELSE 2 END) ORDERSTR\n" );
		sql.append("FROM TT_AS_WR_APPLICATION A, TM_DEALER B\n" );
		sql.append("WHERE A.DEALER_ID = B.DEALER_ID\n" );
		
		List<Object> params = new ArrayList<Object>();
		if(Utility.testString(bean.getDealerCode())){
			sql.append(Utility.getConSqlByParamForEqual(bean.getDealerCode(), params, "B", "DEALER_CODE"));
		}
		if(Utility.testString(bean.getVin())){
			sql.append("AND A.VIN LIKE '%").append(bean.getVin()).append("%'\n");
		}
		if(Utility.testString(bean.getClaimNo())){
			sql.append("AND A.CLAIM_NO LIKE UPPER('%").append(bean.getClaimNo()).append("%')\n");
		}
		if(Utility.testString(bean.getModelCode())){
			sql.append("AND A.MODEL_CODE LIKE '%").append(bean.getModelCode()).append("%'\n");
		}
		if(Utility.testString(bean.getClaimStatus())){
			sql.append("AND A.STATUS = '").append(bean.getClaimStatus()).append("'\n");
		}
		if(Utility.testString(bean.getAuthCode())){
			sql.append("AND A.AUTH_CODE = '").append(bean.getAuthCode()).append("'\n");
		}
		if(Utility.testString(bean.getClaimType())){
			sql.append("AND A.CLAIM_TYPE = '").append(bean.getClaimType()).append("'\n");
		}
		if(Utility.testString(bean.getStartDate())){
			sql.append("AND A.AUDITING_DATE >= TO_DATE('").append(bean.getStartDate()).append("', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(Utility.testString(bean.getEndDate())){
			sql.append("AND A.AUDITING_DATE <= TO_DATE('").append(bean.getEndDate()).append("', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(!CommonUtils.checkNull(bean.getCantNotAudit()).equals("")){
            sql.append("AND A.ID NOT IN("+bean.getCantNotAudit()+") ");
        }
		//sql.append("AND C.BALANCE_ID = ").append(bean.getId()).append("\n");
		sql.append("ORDER BY a.AUDITING_DATE asc,a.VIN asc");

		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), params, getFunName(), pageSize, curPage);
		return ps;
	}
	//逐条审核查询SQL 
	public List<Map<String, Object>> queryClaimAuditByIdJC(auditBean bean,String areaId,int curPage, int pageSize){
		StringBuffer sql= new StringBuffer();
		sql.append("select * from ( SELECT /*+ORDERED*/ROWNUM NUM, A.ID CLAIM_ID, A.CLAIM_NO, A.YIELDLY,\n" );
		sql.append("       TO_CHAR(A.REPORT_DATE, 'YYYY-MM-DD') REPORT_DATE,\n" );
		sql.append("       TO_CHAR(A.AUDITING_DATE, 'YYYY-MM-DD') AUDITING_DATE,\n" );
		sql.append("       A.SERIES_NAME, A.MODEL_NAME, A.VIN, \n" );
		//sql.append("     B.DEALER_SHORTNAME DEALER_NAME,A.CLAIM_TYPE,A.STATUS,A.AUTH_CODE,A.BALANCE_AMOUNT,A.REPAIR_TOTAL,\n" );
		/******MOD BY LIUXH 20101203 索赔单申请费用加上追加工时******/
		sql.append("     B.DEALER_SHORTNAME DEALER_NAME,A.CLAIM_TYPE,A.STATUS,A.AUTH_CODE,A.BALANCE_AMOUNT,(NVL(A.REPAIR_TOTAL,0)) AS REPAIR_TOTAL,\n" );
		/******MOD BY LIUXH 20101203 索赔单申请费用加上追加工时******/
		sql.append("     (CASE WHEN A.STATUS = "+Constant.CLAIM_APPLY_ORD_TYPE_08+" AND A.AUTH_CODE = '"+CommonUtils.checkNull(bean.getAuthCodeOrder())+"' THEN 1 ELSE 2 END) ORDERSTR\n" );
		sql.append("FROM TM_DEALER B,tc_user t,TT_AS_WR_APPLICATION A \n" );
		sql.append("WHERE A.DEALER_ID = B.DEALER_ID\n" );
		sql.append("and T.user_id="+bean.getAuthCodeOrder()+"\n");
		sql.append("and  exists  (select * from TC_USER_REGION_RELATION r where r.user_id=t.user_id and b.province_id = r.region_code)\n");
		List<Object> params = new ArrayList<Object>();
		if(Utility.testString(bean.getDealerCode())){
			sql.append(Utility.getConSqlByParamForEqual(bean.getDealerCode(), params, "B", "DEALER_CODE"));
		}
		if(Utility.testString(bean.getVin())){
			sql.append("AND A.VIN LIKE '%").append(bean.getVin()).append("%'\n");
		}
		if(Utility.testString(bean.getClaimNo())){
			sql.append("AND A.CLAIM_NO LIKE UPPER('%").append(bean.getClaimNo()).append("%')\n");
		}
		if(Utility.testString(bean.getModelCode())){
			sql.append("AND A.MODEL_CODE LIKE '%").append(bean.getModelCode()).append("%'\n");
		}
		if(Utility.testString(bean.getClaimStatus())){
			sql.append("AND A.STATUS = '").append(bean.getClaimStatus()).append("'\n");
		}
		if(Utility.testString(bean.getAuthCode())){
			sql.append("AND A.AUTH_CODE = '").append(bean.getAuthCode()).append("'\n");
		}
		if(Utility.testString(bean.getClaimType())){
			sql.append("AND A.CLAIM_TYPE = '").append(bean.getClaimType()).append("'\n");
		}
		if(Utility.testString(bean.getStartDate())){
			sql.append("AND A.AUDITING_DATE >= TO_DATE('").append(bean.getStartDate()).append("', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(Utility.testString(bean.getEndDate())){
			sql.append("AND A.AUDITING_DATE <= TO_DATE('").append(bean.getEndDate()).append("', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(!CommonUtils.checkNull(bean.getCantNotAudit()).equals("")){
            sql.append("AND A.ID NOT IN("+bean.getCantNotAudit()+") ");
        }
		
		sql.append("AND exists (SELECT 1\n");
		sql.append("           FROM tm_business_area TDBA\n");
		sql.append("\t\t     WHERE TDBA.produce_base = a.yieldly\n");
		sql.append("\t      AND TDBA.AREA_ID IN ("+areaId+")) \n");
		sql.append(" ");
		sql.append("ORDER BY a.AUDITING_DATE asc ,A.VIN ASC) where  ROWNUM<=1");

		//PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), params, getFunName(), pageSize, curPage);
		return this.pageQuery(sql.toString(), params, getFunName());
		
		//return ps;
	}
	//新索赔单跳过审核下一条
	public List<Map<String, Object>> queryClaimAccAuditSkipByIdJC(auditBean bean,String areaId,int curPage, int pageSize){
		StringBuffer sql= new StringBuffer();
		sql.append("select * from (SELECT /*+RULE*/ROWNUM NUM, A.ID CLAIM_ID, A.CLAIM_NO, A.YIELDLY,\n" );
		sql.append("       TO_CHAR(A.REPORT_DATE, 'YYYY-MM-DD') REPORT_DATE,\n" );
		sql.append("       TO_CHAR(A.AUDITING_DATE, 'YYYY-MM-DD') AUDITING_DATE,\n" );
		sql.append("       A.SERIES_NAME, A.MODEL_NAME, A.VIN, \n" );
		//sql.append("     B.DEALER_SHORTNAME DEALER_NAME,A.CLAIM_TYPE,A.STATUS,A.AUTH_CODE,A.BALANCE_AMOUNT,A.REPAIR_TOTAL,\n" );
		/******MOD BY LIUXH 20101203 索赔单申请费用加上追加工时******/
		sql.append("     B.DEALER_SHORTNAME DEALER_NAME,A.CLAIM_TYPE,A.STATUS,A.AUTH_CODE,A.BALANCE_AMOUNT,(NVL(A.REPAIR_TOTAL,0)) AS REPAIR_TOTAL,\n" );
		/******MOD BY LIUXH 20101203 索赔单申请费用加上追加工时******/
		sql.append("     (CASE WHEN A.STATUS = "+Constant.CLAIM_APPLY_ORD_TYPE_08+" AND A.AUTH_CODE = '"+CommonUtils.checkNull(bean.getAuthCodeOrder())+"' THEN 1 ELSE 2 END) ORDERSTR\n" );
		sql.append("FROM TM_DEALER B,tc_user t,TT_AS_WR_APPLICATION A\n" );
		sql.append("WHERE A.DEALER_ID = B.DEALER_ID\n" );
		sql.append("and T.user_id="+bean.getAuthCodeOrder()+" \n");
		sql.append("and  exists  (select * from TC_USER_REGION_RELATION r where r.user_id=t.user_id and b.province_id = r.region_code)\n");
		List<Object> params = new ArrayList<Object>();
		if(Utility.testString(bean.getDealerCode())){
			sql.append(Utility.getConSqlByParamForEqual(bean.getDealerCode(), params, "B", "DEALER_CODE"));
		}
		if(Utility.testString(bean.getDealerName())){
			sql.append("and B.DEALER_NAME LIKE '%"+bean.getDealerName()+"%'\n");
		}
		if(Utility.testString(bean.getVin())){
			sql.append("AND A.VIN LIKE '%").append(bean.getVin()).append("%'\n");
		}
		if(Utility.testString(bean.getClaimNo())){
			sql.append("AND A.CLAIM_NO LIKE UPPER('%").append(bean.getClaimNo()).append("%')\n");
		}
		if(Utility.testString(bean.getModelCode())){
			sql.append("AND A.MODEL_CODE LIKE '%").append(bean.getModelCode()).append("%'\n");
		}
		if(Utility.testString(bean.getClaimStatus())){ 
			sql.append("AND A.STATUS = '").append(bean.getClaimStatus()).append("'\n");
		}
		if(Utility.testString(bean.getAuthCode())){
			sql.append("AND A.AUTH_CODE = '").append(bean.getAuthCode()).append("'\n");
		}
		if(Utility.testString(bean.getClaimType())){
			sql.append("AND A.CLAIM_TYPE = '").append(bean.getClaimType()).append("'\n");
		}
		if(Utility.testString(bean.getStartDate())){
			sql.append("AND A.AUDITING_DATE >= TO_DATE('").append(bean.getStartDate()).append("', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(Utility.testString(bean.getEndDate())){
			sql.append("AND A.AUDITING_DATE <= TO_DATE('").append(bean.getEndDate()).append("', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(!CommonUtils.checkNull(bean.getCantNotAudit()).equals("")){ 
            sql.append("AND A.ID NOT IN("+bean.getCantNotAudit()+") ");
        }
		
		sql.append("AND exists (SELECT 1\n");
		sql.append("           FROM tm_business_area TDBA\n");
		sql.append("\t\t     WHERE TDBA.produce_base = a.yieldly\n");
		sql.append("\t      AND TDBA.AREA_ID IN ("+areaId+"))\n");
		sql.append("ORDER BY A.AUDITING_DATE asc , A.VIN ASC) where ROWNUM<=1");
		return this.pageQuery(sql.toString(), params, getFunName());
		//PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), params, getFunName(), pageSize, curPage);
		//return ps;
	}
	/*******************************add by kevinyin 20110414****************************************************/
	/**************************微车流程修改keviyin*****************************************/
	
	
	
	
	
}
