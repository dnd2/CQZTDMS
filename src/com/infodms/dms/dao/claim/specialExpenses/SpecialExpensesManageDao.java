/**
 * 
 */
package com.infodms.dms.dao.claim.specialExpenses;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.SpeciaExpensesBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.common.tag.DaoFactory;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TcCodePO;
import com.infodms.dms.po.TtAsWrSpefeeClaimPO;
import com.infodms.dms.util.StringUtil;
import com.infodms.dms.util.UserProvinceRelation;
import com.infodms.dms.util.businessUtil.GetVinUtil;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

/**
 * @Title: SpecialExpensesManageDao.java
 *
 * @Description:CHANADMS
 *
 * @Copyright: Copyright (c) 2010
 *
 * @Company: www.infoservice.com.cn
 * @Date: 2010-9-18
 *
 * @author lishuai 
 * @mail   lishuai103@yahoo.cn	
 * @version 1.0
 * @remark 
 */
public class SpecialExpensesManageDao extends BaseDao<PO>{
	public static Logger logger = Logger.getLogger(SpecialExpensesManageDao.class);
	private static final SpecialExpensesManageDao dao = new SpecialExpensesManageDao();
	public static final SpecialExpensesManageDao getInstance() {
		return dao;
	}
	
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public Map<String, Object> getDealerInfo(String dealerId){
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT DEALER_CODE, DEALER_SHORTNAME, DEALER_NAME,\n" );
		sql.append("       TO_CHAR(SYSDATE, 'YYYY-MM-DD HH24:mm:ss') SDATE\n" );
		sql.append("FROM TM_DEALER\n" );
		sql.append("WHERE DEALER_ID = ").append(dealerId);

		Map<String, Object> map = pageQueryMap(sql.toString(), null, getFunName());
		return map;
	}
	
	public PageResult<Map<String, Object>> querySpeciaExpenses(SpeciaExpensesBean bean,int curPage, int pageSize){
		StringBuffer sql= new StringBuffer("\n");
		sql.append("SELECT ROWNUM NUM, A.ID, A.FEE_NO, A.DECLARE_SUM,A.declare_sum1, A.FEE_TYPE, B.AREA_NAME AS YIELD,\n" );
		sql.append("       TO_CHAR(A.CREATE_DATE, 'YYYY-MM-DD') CREATE_DATE, A.STATUS\n" );
		sql.append("FROM TT_AS_WR_SPEFEE A ,TM_BUSINESS_AREA B WHERE A.YIELD=B.AREA_ID \n" );
		/*sql.append("WHERE STATUS IN (");
		sql.append(Constant.SPEFEE_STATUS_01).append(",");
		sql.append(Constant.SPEFEE_STATUS_03).append(",");
		sql.append(Constant.SPEFEE_STATUS_05);
		sql.append(")\n");*/
		sql.append(" AND ( A.status=").append(Constant.SPEFEE_STATUS_01).append("\n");
		sql.append(" OR A.status=").append(Constant.SPEFEE_STATUS_03).append("\n");
		sql.append(" OR A.status=").append(Constant.SPEFEE_STATUS_05).append("\n");
		sql.append(" OR A.status=").append(Constant.SPEFEE_STATUS_07).append(") \n");
		
		if(Utility.testString(bean.getBeginTime())){
			sql.append("AND A.CREATE_DATE >= TO_DATE('").append(bean.getBeginTime()).append("', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(Utility.testString(bean.getEndTime())){
			sql.append("AND A.CREATE_DATE <= TO_DATE('").append(bean.getEndTime()).append("', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(Utility.testString(bean.getYieldly())){
			sql.append("AND B.AREA_ID = ").append(bean.getYieldly()).append("\n");
		}
		if(Utility.testString(bean.getFeeType())){
			sql.append("AND A.FEE_TYPE = ").append(bean.getFeeType()).append("\n");
		}
		if(Utility.testString(bean.getFeeNo())){
			sql.append("AND A.FEE_NO LIKE '%").append(bean.getFeeNo()).append("%'\n");
		}
		sql.append("AND A.COMPANY_ID = ").append(bean.getCompanyId()).append("\n");  
		sql.append("AND A.DEALER_ID = ").append(bean.getDealerId()).append("\n"); 
		sql.append("order by A.fee_no desc\n");
		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
		return ps;
	}
	
	public List<Map<String, Object>> open_supplier(String PART_OLDCODE)
	{
		StringBuffer sql= new StringBuffer();
		sql.append("select d.part_oldcode, d.part_cname, vd.vender_code, vd.vender_name\n" );
		sql.append("  from tt_part_define d, tt_part_vender_define vd, tt_part_buy_price p\n" );
		sql.append(" where p.part_id = d.part_id\n" );
		sql.append("   and p.vender_id = vd.vender_id\n" );
		sql.append("   and d.part_oldcode = '"+PART_OLDCODE+"'");
		List<Map<String, Object>> list= dao.pageQuery(sql.toString(), null, this.getFunName());
		return list;
	}
	
	public PageResult<Map<String, Object>> get_part(String PART_CODE, String PART_NAME,int curPage, int pageSize){
		StringBuffer sql= new StringBuffer();
		sql.append("select * from tt_part_define t where 1=1 ");
		if(Utility.testString(PART_CODE))
		{
			sql.append(" and t.part_oldcode like '%"+PART_CODE+"%'");
		}
		
		if(Utility.testString(PART_NAME))
		{
			sql.append(" and t.part_cname like '%"+PART_NAME+"%'");
		}
		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
		return ps;
	}
	
	public PageResult<Map<String, Object>> get_supplier(String supplier_code, String supplier_name,String part_code,int curPage, int pageSize){
		StringBuffer sql= new StringBuffer();
		PageResult<Map<String, Object>> ps =null;
		try {
			if(Utility.testString(part_code)){
				sql.append("select m.MAKER_CODE,min(m.MAKER_SHOTNAME) as MAKER_NAME\n" );
				sql.append("  from tt_part_define d, tt_part_maker_define m, tt_part_maker_relation r\n" );
				sql.append(" where d.part_id = r.part_id\n" );
				sql.append("   and r.maker_id = m.maker_id and m.maker_code not in ('902306','902309','902311')\n" );
				DaoFactory.getsql(sql, "m.MAKER_CODE", supplier_code, 2);
				DaoFactory.getsql(sql, "m.MAKER_NAME", supplier_name, 2);
				//DaoFactory.getsql(sql, "d.part_oldcode", part_code, 1);
				sql.append(" and d.part_oldcode in (substr('"+part_code+"',0,length('"+part_code+"')-3)||'B0Y',substr('"+part_code+"',0,length('"+part_code+"')-3)||'000',substr('"+part_code+"',0,length('"+part_code+"')-3)||'B00')\n");
				sql.append(" GROUP by m.MAKER_CODE");
			}else{
				sql.append("select m.MAKER_CODE,min(m.MAKER_SHOTNAME) as MAKER_NAME\n" );
				sql.append("  from  tt_part_maker_define m, tt_part_maker_relation r\n" );
				sql.append(" where \n" );
				sql.append("    r.maker_id = m.maker_id and m.maker_code not in ('902306','902309','902311')\n" );
				DaoFactory.getsql(sql, "m.MAKER_CODE", supplier_code, 2);
				DaoFactory.getsql(sql, "m.MAKER_NAME", supplier_name, 2);
				sql.append(" GROUP by m.MAKER_CODE");
			}
			
			ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
			
			//zyw 2014-5-16 添加当没有制造商的时候出现999999的供应商
			if(Utility.testString(part_code)){
				if(ps==null || ps.getRecords()==null){
					StringBuffer sb= new StringBuffer();
					sb.append("SELECT C.MAKER_CODE,C.MAKER_SHOTNAME AS MAKER_NAME FROM TT_PART_MAKER_DEFINE  C WHERE C.MAKER_CODE='902307'");
					ps=pageQuery(sb.toString(), null, getFunName(), pageSize, curPage);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ps;
	}
	
	
	public PageResult<Map<String, Object>> queryDealerSpeciaExpenses(SpeciaExpensesBean bean,int curPage, int pageSize){
		StringBuffer sql= new StringBuffer();
		sql.append("select B.ID,\n" );
		sql.append("       B.FEE_NO,\n" );
		sql.append("       D.DEALER_CODE,\n" );
		sql.append("       D.DEALER_NAME,\n" );
		sql.append("       to_char(B.Create_Date, 'yyyy-mm-dd') AS Create_Date,\n" );
		sql.append("       to_char(B.make_DATE, 'yyyy-mm-dd') AS make_DATE,\n" );
		sql.append("       C.AREA_NAME,\n" );
		sql.append("       B.FEE_TYPE,\n" );
		sql.append("       B.Declare_Sum1 AS DECLARE_SUM1,\n" );
		sql.append("       B.Declare_Sum AS DECLARE_SUM,\n" );
		sql.append("       B.Status,\n" );
		sql.append("       B.O_STATUS,\n" );
		sql.append("       B.AUDITING_OPINION,\n" );
		sql.append("       B.section_AUDITING_OPINION ,\n" );
		sql.append("       B.maneger_AUDITING_OPINION,\n" );
		sql.append("       B.office_AUDITING_OPINION,\n" );
		sql.append("       B.director_AUDITING_OPINION, \n" );
		sql.append( "(SELECT X.CODE_DESC FROM TT_AS_WR_SPECIAL_CHARGE S, TC_CODE X\n" +
				"         WHERE s.spe_level = x.code_id AND X.TYPE = 1190 AND B.DECLARE_SUM1 >= S.MIN_AMOUNT\n" + 
				"           AND (B.DECLARE_SUM1 < S.MAX_AMOUNT OR S.MAX_AMOUNT IS NULL)) N_LEVEL");
		sql.append("  from  TT_AS_WR_SPEFEE B, Tm_Business_Area C, TM_DEALER D \n" );
		sql.append("  where 1=1 AND C.AREA_ID = B.Yield AND B.DEALER_ID = D.DEALER_ID");
		
		if(Utility.testString(bean.getBeginTime())){
			sql.append("  AND(  B.make_DATE >= TO_DATE('"+bean.getBeginTime()+"', 'YYYY-MM-DD HH24:MI:SS') AND\n");  
		}
		if(Utility.testString(bean.getEndTime())){
			sql.append("    B.make_DATE <= TO_DATE('"+bean.getEndTime()+"', 'YYYY-MM-DD HH24:MI:SS')) \n");  
		}
		if(Utility.testString(bean.getCREATE_DATE_S())){
			sql.append("  AND(  B.Create_Date >= TO_DATE('"+bean.getCREATE_DATE_S()+"', 'YYYY-MM-DD HH24:MI:SS') AND\n");  
		}
		if(Utility.testString(bean.getCREATE_DATE_D())){
			sql.append("    B.Create_Date <= TO_DATE('"+bean.getCREATE_DATE_D()+"', 'YYYY-MM-DD HH24:MI:SS')) \n");  
		}
		if(Utility.testString(bean.getYieldly())){
			sql.append("  AND C.AREA_ID = ").append(bean.getYieldly()).append("\n");
		}
		if(Utility.testString(bean.getStatus())){
			sql.append("  AND B.Status = ").append(bean.getStatus()).append("\n");
		}
		
		if(Utility.testString(bean.getFeeType())){
			sql.append("   AND B.FEE_TYPE = ").append(bean.getFeeType()).append("\n");
		}

		if(Utility.testString(bean.getDealerCode())){
			sql.append("  AND B.DEALER_CODE IN (").append(bean.getDealerCode()).append(")\n");
		}
		
		if(Utility.testString(bean.getFeeNo())){
			sql.append(" AND B.FEE_NO LIKE '%").append(bean.getFeeNo()).append("%'\n");
		}  
		sql.append("  AND B.COMPANY_ID = ").append(bean.getCompanyId()).append("\n");  
		sql.append("  AND B.DEALER_ID = ").append(bean.getDealerId()).append("\n");
		sql.append("  order by B.fee_no desc");
		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
		return ps;
	}
	
	
	
	
	public PageResult<Map<String, Object>> auditQuerySpeciaExpenses(SpeciaExpensesBean bean,int curPage, int pageSize){
		StringBuffer sql= new StringBuffer();
		/*sql.append("SELECT ROWNUM NUM, A.ID, A.FEE_NO, A.DECLARE_SUM, A.FEE_TYPE, A.YIELD,NVL(A.PASS_FEE,0) PASS_FEE,NVL(A.TRAFFIC_FEE,0) TRAFFIC_FEE,NVL(A.QUARTER_FEE,0) QUARTER_FEE,NVL(A.EAT_FEE,0) EAT_FEE,NVL(A.PERSON_SUBSIDE,0) PERSON_SUBSIDE,A.CLAIMBALANCE_ID,\n" );
		sql.append("       TO_CHAR(A.CREATE_DATE, 'YYYY-MM-DD') CREATE_DATE, A.STATUS,\n" );
		sql.append("	   B.DEALER_CODE, B.DEALER_SHORTNAME\n");
		sql.append("FROM(\n" );
		sql.append("      SELECT ID, FEE_NO, DECLARE_SUM, FEE_TYPE, YIELD,PASS_FEE,TRAFFIC_FEE,QUARTER_FEE,EAT_FEE,PERSON_SUBSIDE,CLAIMBALANCE_ID,\n" );
		sql.append("             CREATE_DATE, STATUS, DEALER_ID\n" );
		sql.append("      FROM TT_AS_WR_SPEFEE\n" );
		sql.append("      WHERE FEE_TYPE = ").append(Constant.FEE_TYPE_01).append("\n");
		sql.append("      AND STATUS = ").append(Constant.SPEFEE_STATUS_02).append("\n");
		sql.append("      AND CLAIMBALANCE_ID IS NOT NULL\n");
		sql.append("	  AND COMPANY_ID = ").append(bean.getCompanyId()).append("\n");
		sql.append("      UNION ALL\n" );
		sql.append("      SELECT ID, FEE_NO, DECLARE_SUM, FEE_TYPE, YIELD,PASS_FEE,TRAFFIC_FEE,QUARTER_FEE,EAT_FEE,PERSON_SUBSIDE,CLAIMBALANCE_ID,\n" );
		sql.append("             CREATE_DATE, STATUS, DEALER_ID\n" );
		sql.append("      FROM TT_AS_WR_SPEFEE\n" );
		sql.append("      WHERE FEE_TYPE = ").append(Constant.FEE_TYPE_02).append("\n");
		sql.append("      AND STATUS = ").append(Constant.SPEFEE_STATUS_04).append("\n");
		sql.append("      AND CLAIMBALANCE_ID IS NOT NULL\n");
		sql.append("	  AND COMPANY_ID = ").append(bean.getCompanyId()).append("\n");
		sql.append("  ) A, TM_DEALER B\n" );
		sql.append("WHERE A.DEALER_ID = B.DEALER_ID\n");*/
		/*sql.append("SELECT ROWNUM NUM,\n");
		sql.append("       A.ID,\n");  
		sql.append("       A.FEE_NO,\n");  
		sql.append("       A.DECLARE_SUM,a.declare_sum1,\n");  
		sql.append("       A.FEE_TYPE,\n");  
		sql.append("       A.YIELD,\n");  
		sql.append("       NVL(A.PASS_FEE, 0) PASS_FEE,\n");  
		sql.append("       NVL(A.TRAFFIC_FEE, 0) TRAFFIC_FEE,\n");  
		sql.append("       NVL(A.QUARTER_FEE, 0) QUARTER_FEE,\n");  
		sql.append("       NVL(A.EAT_FEE, 0) EAT_FEE,\n");  
		sql.append("       NVL(A.PERSON_SUBSIDE, 0) PERSON_SUBSIDE,\n");  
		sql.append("       A.CLAIMBALANCE_ID,\n");  
		sql.append("       TO_CHAR(A.CREATE_DATE, 'YYYY-MM-DD') CREATE_DATE,\n");  
		sql.append("       A.STATUS,\n");  
		sql.append("       B.DEALER_CODE,\n");  
		sql.append("       B.DEALER_SHORTNAME\n");  
		sql.append("  FROM (SELECT ID,\n");  
		sql.append("               FEE_NO,\n");  
		sql.append("               DECLARE_SUM,declare_sum1,\n");  
		sql.append("               FEE_TYPE,\n");  
		sql.append("               YIELD,\n");  
		sql.append("               PASS_FEE,\n");  
		sql.append("               TRAFFIC_FEE,\n");  
		sql.append("               QUARTER_FEE,\n");  
		sql.append("               EAT_FEE,\n");  
		sql.append("               PERSON_SUBSIDE,\n");  
		sql.append("               CLAIMBALANCE_ID,\n");  
		sql.append("               CREATE_DATE,\n");  
		sql.append("               STATUS,\n");  
		sql.append("               DEALER_ID\n");  
		sql.append("          FROM TT_AS_WR_SPEFEE\n");  
		sql.append("         WHERE FEE_TYPE = ").append(Constant.FEE_TYPE_01).append("\n");  
		sql.append("           AND STATUS = ").append(Constant.SPEFEE_STATUS_02).append("\n");  
		sql.append("           AND CLAIMBALANCE_ID IS NOT NULL\n");  
		sql.append("           AND COMPANY_ID = ").append(bean.getCompanyId()).append("\n");  
		sql.append("        UNION ALL\n");  
		sql.append("        SELECT ID,\n");  
		sql.append("               FEE_NO,\n");  
		sql.append("               DECLARE_SUM,declare_sum1,\n");  
		sql.append("               FEE_TYPE,\n");  
		sql.append("               YIELD,\n");  
		sql.append("               PASS_FEE,\n");  
		sql.append("               TRAFFIC_FEE,\n");  
		sql.append("               QUARTER_FEE,\n");  
		sql.append("               EAT_FEE,\n");  
		sql.append("               PERSON_SUBSIDE,\n");  
		sql.append("               CLAIMBALANCE_ID,\n");  
		sql.append("               CREATE_DATE,\n");  
		sql.append("               STATUS,\n");  
		sql.append("               DEALER_ID\n");  
		sql.append("          FROM TT_AS_WR_SPEFEE s\n");  
		sql.append("         WHERE FEE_TYPE = ").append(Constant.FEE_TYPE_02).append("\n");  
		sql.append("           AND STATUS = ").append(Constant.SPEFEE_STATUS_04).append("\n");  
		sql.append("           AND CLAIMBALANCE_ID IS NOT NULL\n");  
		sql.append("           AND COMPANY_ID = ").append(bean.getCompanyId()).append(") A,\n");  
		sql.append("       TM_DEALER B,\n");  
		sql.append("       tt_as_wr_claim_balance x,\n");  
		sql.append("       tr_gather_balance y,\n");  
		sql.append("       tt_as_wr_gather_balance z\n");  
		sql.append(" WHERE A.DEALER_ID = B.DEALER_ID\n");  
		sql.append("   and a.claimbalance_id = x.id\n");  
		sql.append("   and x.id = y.balance_id\n");  
		sql.append("   and y.gather_id = z.id\n");  
		sql.append("   and z.status = ").append(Constant.BALANCE_GATHER_TYPE_03).append("\n");  
		sql.append("AND A.YIELD IN (").append(bean.getYieldlys()).append(")\n");*/
		sql.append("SELECT w.ID,\n");
		sql.append("       w.FEE_NO,\n");  
		sql.append("       w.DECLARE_SUM,\n");  
		sql.append("       declare_sum1,\n");  
		sql.append("       w.FEE_TYPE,\n");  
		sql.append("       w.YIELD,\n");  
		sql.append("       w.PASS_FEE,\n");  
		sql.append("       w.TRAFFIC_FEE,\n");  
		sql.append("       w.QUARTER_FEE,\n");  
		sql.append("       w.EAT_FEE,\n");  
		sql.append("       w.PERSON_SUBSIDE,\n");  
		sql.append("       w.CLAIMBALANCE_ID,\n");  
		sql.append("       w.CREATE_DATE,\n");  
		sql.append("       w.STATUS,\n");  
		sql.append("       w.DEALER_ID,\n");  
		sql.append("       (select s.dealer_code\n");  
		sql.append("          from tm_dealer s\n");  
		sql.append("         where s.dealer_id = w.dealer_id\n");  
		sql.append("           and rownum <= 1) as dealer_code,\n");  
		sql.append("       (select s.dealer_name\n");  
		sql.append("          from tm_dealer s\n");  
		sql.append("         where s.dealer_id = w.dealer_id\n");  
		sql.append("           and rownum <= 1) as dealer_name\n");  
		sql.append("  FROM TT_AS_WR_SPEFEE w,tm_dealer b\n");  
		sql.append(" WHERE w.dealer_id = b.dealer_id and ((w.FEE_TYPE = "+Constant.FEE_TYPE_01+" AND w.STATUS = "+Constant.SPEFEE_STATUS_02+") OR\n");  
		sql.append("       (w.FEE_TYPE = "+Constant.FEE_TYPE_02+" AND w.STATUS = "+Constant.SPEFEE_STATUS_04+"))\n");  
		sql.append("   AND w.CLAIMBALANCE_ID IS NOT NULL\n");  
		sql.append("   and exists (select 1\n");  
		sql.append("          from Tt_As_Wr_Claim_Balance c\n");  
		sql.append("         where c.id = w.claimbalance_id\n");  
		sql.append("           and c.status = "+Constant.ACC_STATUS_01+")\n");


		if(Utility.testString(bean.getBeginTime())){
			sql.append("   AND ((w.fee_type = "+Constant.FEE_TYPE_01+" and\n");
			sql.append("    w.make_DATE >= TO_DATE('"+bean.getBeginTime()+"', 'YYYY-MM-DD HH24:MI:SS')) or\n");  
			sql.append("    (w.fee_type = "+Constant.FEE_TYPE_02+"\n");  
			sql.append("        and exists\n");
			sql.append("        (select 1\n");  
			sql.append("            from tt_as_wr_spefee_auditing a\n");  
			sql.append("           where a.status = "+Constant.SPEFEE_STATUS_04+"\n");  
			sql.append("             and a.fee_id = w.id\n");  
			sql.append("             and a.auditing_date >=\n");  
			sql.append("                 TO_DATE('"+bean.getBeginTime()+"', 'YYYY-MM-DD HH24:MI:SS'))))\n");
		}
		if(Utility.testString(bean.getEndTime())){
			sql.append("   AND ((w.fee_type = "+Constant.FEE_TYPE_01+" and\n");
			sql.append("    w.make_DATE <= TO_DATE('"+bean.getEndTime()+"', 'YYYY-MM-DD HH24:MI:SS')) or\n");  
			sql.append("    (w.fee_type = "+Constant.FEE_TYPE_02+"\n");  
			sql.append("     and exists\n");
			sql.append("        (select 1\n");  
			sql.append("            from tt_as_wr_spefee_auditing a\n");  
			sql.append("           where a.status = "+Constant.SPEFEE_STATUS_04+"\n");  
			sql.append("             and a.fee_id = w.id\n");  
			sql.append("             and a.auditing_date <=\n");  
			sql.append("                 TO_DATE('"+bean.getEndTime()+"', 'YYYY-MM-DD HH24:MI:SS'))))\n");
		}
		if(Utility.testString(bean.getYieldly())){
			sql.append("AND w.YIELD = ").append(bean.getYieldly()).append("\n");
		}else{
			sql.append("and w.yield in (").append(bean.getYieldlys()).append(")\n");
		}
		if(Utility.testString(bean.getFeeType())){
			sql.append("AND w.FEE_TYPE = ").append(bean.getFeeType()).append("\n");
		}
		if(Utility.testString(bean.getFeeNo())){
			sql.append("AND w.FEE_NO LIKE '%").append(bean.getFeeNo()).append("%'\n");
		}
		if(Utility.testString(bean.getDealerCode())){
			sql.append("AND B.DEALER_CODE IN (").append(bean.getDealerCode()).append(")\n");
		}
		sql.append("  order by w.fee_no desc \n");
		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
		return ps;
	}
	
	/**
	 * 特殊费用逐条审核查询  writed by yx 20110111
	 * @param bean
	 * @param curPage
	 * @param pageSize
	 * @return
	 */
	public List<Map<String, Object>> auditQuerySpeciaExpensesByOne(SpeciaExpensesBean bean){
		StringBuffer sql= new StringBuffer();
		/*sql.append("SELECT ROWNUM NUM, A.ID, A.FEE_NO, A.DECLARE_SUM, A.FEE_TYPE, A.YIELD,NVL(A.PASS_FEE,0) PASS_FEE,NVL(A.TRAFFIC_FEE,0) TRAFFIC_FEE,NVL(A.QUARTER_FEE,0) QUARTER_FEE,NVL(A.EAT_FEE,0) EAT_FEE,NVL(A.PERSON_SUBSIDE,0) PERSON_SUBSIDE,A.CLAIMBALANCE_ID,\n" );
		sql.append("       TO_CHAR(A.CREATE_DATE, 'YYYY-MM-DD') CREATE_DATE, A.STATUS,\n" );
		sql.append("	   B.DEALER_CODE, B.DEALER_SHORTNAME\n");
		sql.append("FROM(\n" );
		sql.append("      SELECT ID, FEE_NO, DECLARE_SUM, FEE_TYPE, YIELD,PASS_FEE,TRAFFIC_FEE,QUARTER_FEE,EAT_FEE,PERSON_SUBSIDE,CLAIMBALANCE_ID,\n" );
		sql.append("             CREATE_DATE, STATUS, DEALER_ID\n" );
		sql.append("      FROM TT_AS_WR_SPEFEE\n" );
		sql.append("      WHERE FEE_TYPE = ").append(Constant.FEE_TYPE_01).append("\n");
		sql.append("      AND STATUS = ").append(Constant.SPEFEE_STATUS_02).append("\n");
		sql.append("      AND CLAIMBALANCE_ID IS NOT NULL\n");
		sql.append("	  AND COMPANY_ID = ").append(bean.getCompanyId()).append("\n");
		sql.append("      UNION ALL\n" );
		sql.append("      SELECT ID, FEE_NO, DECLARE_SUM, FEE_TYPE, YIELD,PASS_FEE,TRAFFIC_FEE,QUARTER_FEE,EAT_FEE,PERSON_SUBSIDE,CLAIMBALANCE_ID,\n" );
		sql.append("             CREATE_DATE, STATUS, DEALER_ID\n" );
		sql.append("      FROM TT_AS_WR_SPEFEE\n" );
		sql.append("      WHERE FEE_TYPE = ").append(Constant.FEE_TYPE_02).append("\n");
		sql.append("      AND STATUS = ").append(Constant.SPEFEE_STATUS_04).append("\n");
		sql.append("      AND CLAIMBALANCE_ID IS NOT NULL\n");
		sql.append("	  AND COMPANY_ID = ").append(bean.getCompanyId()).append("\n");
		sql.append("  ) A, TM_DEALER B\n" );
		sql.append("WHERE A.DEALER_ID = B.DEALER_ID\n");*/
		/*sql.append("SELECT ROWNUM NUM,\n");
		sql.append("       A.ID,\n");  
		sql.append("       A.FEE_NO,\n");  
		sql.append("       A.DECLARE_SUM,a.declare_sum1,\n");  
		sql.append("       A.FEE_TYPE,\n");  
		sql.append("       A.YIELD,\n");  
		sql.append("       NVL(A.PASS_FEE, 0) PASS_FEE,\n");  
		sql.append("       NVL(A.TRAFFIC_FEE, 0) TRAFFIC_FEE,\n");  
		sql.append("       NVL(A.QUARTER_FEE, 0) QUARTER_FEE,\n");  
		sql.append("       NVL(A.EAT_FEE, 0) EAT_FEE,\n");  
		sql.append("       NVL(A.PERSON_SUBSIDE, 0) PERSON_SUBSIDE,\n");  
		sql.append("       A.CLAIMBALANCE_ID,\n");  
		sql.append("       TO_CHAR(A.CREATE_DATE, 'YYYY-MM-DD') CREATE_DATE,\n");  
		sql.append("       A.STATUS,\n");  
		sql.append("       B.DEALER_CODE,\n");  
		sql.append("       B.DEALER_SHORTNAME\n");  
		sql.append("  FROM (SELECT ID,\n");  
		sql.append("               FEE_NO,\n");  
		sql.append("               DECLARE_SUM,declare_sum1,\n");  
		sql.append("               FEE_TYPE,\n");  
		sql.append("               YIELD,\n");  
		sql.append("               PASS_FEE,\n");  
		sql.append("               TRAFFIC_FEE,\n");  
		sql.append("               QUARTER_FEE,\n");  
		sql.append("               EAT_FEE,\n");  
		sql.append("               PERSON_SUBSIDE,\n");  
		sql.append("               CLAIMBALANCE_ID,\n");  
		sql.append("               CREATE_DATE,\n");  
		sql.append("               STATUS,\n");  
		sql.append("               DEALER_ID\n");  
		sql.append("          FROM TT_AS_WR_SPEFEE\n");  
		sql.append("         WHERE FEE_TYPE = ").append(Constant.FEE_TYPE_01).append("\n");  
		sql.append("           AND STATUS = ").append(Constant.SPEFEE_STATUS_02).append("\n");  
		sql.append("           AND CLAIMBALANCE_ID IS NOT NULL\n");  
		sql.append("           AND COMPANY_ID = ").append(bean.getCompanyId()).append("\n");  
		sql.append("        UNION ALL\n");  
		sql.append("        SELECT ID,\n");  
		sql.append("               FEE_NO,\n");  
		sql.append("               DECLARE_SUM,declare_sum1,\n");  
		sql.append("               FEE_TYPE,\n");  
		sql.append("               YIELD,\n");  
		sql.append("               PASS_FEE,\n");  
		sql.append("               TRAFFIC_FEE,\n");  
		sql.append("               QUARTER_FEE,\n");  
		sql.append("               EAT_FEE,\n");  
		sql.append("               PERSON_SUBSIDE,\n");  
		sql.append("               CLAIMBALANCE_ID,\n");  
		sql.append("               CREATE_DATE,\n");  
		sql.append("               STATUS,\n");  
		sql.append("               DEALER_ID\n");  
		sql.append("          FROM TT_AS_WR_SPEFEE s\n");  
		sql.append("         WHERE FEE_TYPE = ").append(Constant.FEE_TYPE_02).append("\n");  
		sql.append("           AND STATUS = ").append(Constant.SPEFEE_STATUS_04).append("\n");  
		sql.append("           AND CLAIMBALANCE_ID IS NOT NULL\n");  
		sql.append("           AND COMPANY_ID = ").append(bean.getCompanyId()).append(") A,\n");  
		sql.append("       TM_DEALER B,\n");  
		sql.append("       tt_as_wr_claim_balance x,\n");  
		sql.append("       tr_gather_balance y,\n");  
		sql.append("       tt_as_wr_gather_balance z\n");  
		sql.append(" WHERE A.DEALER_ID = B.DEALER_ID\n");  
		sql.append("   and a.claimbalance_id = x.id\n");  
		sql.append("   and x.id = y.balance_id\n");  
		sql.append("   and y.gather_id = z.id\n");  
		sql.append("   and z.status = ").append(Constant.BALANCE_GATHER_TYPE_03).append("\n");  
		sql.append("AND A.YIELD IN (").append(bean.getYieldlys()).append(")\n");*/
		sql.append("SELECT w.ID,\n");
		sql.append("       w.FEE_NO,\n");  
		sql.append("       w.DECLARE_SUM,\n");  
		sql.append("       declare_sum1,\n");  
		sql.append("       w.FEE_TYPE,\n");  
		sql.append("       w.YIELD,\n");  
		sql.append("       w.PASS_FEE,\n");  
		sql.append("       w.TRAFFIC_FEE,\n");  
		sql.append("       w.QUARTER_FEE,\n");  
		sql.append("       w.EAT_FEE,\n");  
		sql.append("       w.PERSON_SUBSIDE,\n");  
		sql.append("       w.CLAIMBALANCE_ID,\n");  
		sql.append("       w.CREATE_DATE,\n");  
		sql.append("       w.STATUS,\n");  
		sql.append("       w.DEALER_ID,\n");  
		sql.append("       (select s.dealer_code\n");  
		sql.append("          from tm_dealer s\n");  
		sql.append("         where s.dealer_id = w.dealer_id\n");  
		sql.append("           and rownum <= 1) as dealer_code,\n");  
		sql.append("       (select s.dealer_name\n");  
		sql.append("          from tm_dealer s\n");  
		sql.append("         where s.dealer_id = w.dealer_id\n");  
		sql.append("           and rownum <= 1) as dealer_name\n");  
		sql.append("  FROM TT_AS_WR_SPEFEE w,tm_dealer b\n");  
		sql.append(" WHERE w.dealer_id = b.dealer_id and ((w.FEE_TYPE = "+Constant.FEE_TYPE_01+" AND w.STATUS = "+Constant.SPEFEE_STATUS_02+") OR\n");  
		sql.append("       (w.FEE_TYPE = "+Constant.FEE_TYPE_02+" AND w.STATUS = "+Constant.SPEFEE_STATUS_04+"))\n");  
		sql.append("   AND w.CLAIMBALANCE_ID IS NOT NULL\n");  
		sql.append("   and exists (select 1\n");  
		sql.append("          from Tt_As_Wr_Claim_Balance c\n");  
		sql.append("         where c.id = w.claimbalance_id\n");  
		sql.append("           and c.status = "+Constant.ACC_STATUS_01+")\n");


		if(Utility.testString(bean.getBeginTime())){
			sql.append("   AND ((w.fee_type = "+Constant.FEE_TYPE_01+" and\n");
			sql.append("    w.make_DATE >= TO_DATE('"+bean.getBeginTime()+"', 'YYYY-MM-DD HH24:MI:SS')) or\n");  
			sql.append("    (w.fee_type = "+Constant.FEE_TYPE_02+"\n");  
			sql.append("        and exists\n");
			sql.append("        (select 1\n");  
			sql.append("            from tt_as_wr_spefee_auditing a\n");  
			sql.append("           where a.status = "+Constant.SPEFEE_STATUS_04+"\n");  
			sql.append("             and a.fee_id = w.id\n");  
			sql.append("             and a.auditing_date >=\n");  
			sql.append("                 TO_DATE('"+bean.getBeginTime()+"', 'YYYY-MM-DD HH24:MI:SS'))))\n");
		}
		if(Utility.testString(bean.getEndTime())){
			sql.append("   AND ((w.fee_type = "+Constant.FEE_TYPE_01+" and\n");
			sql.append("    w.make_DATE <= TO_DATE('"+bean.getEndTime()+"', 'YYYY-MM-DD HH24:MI:SS')) or\n");  
			sql.append("    (w.fee_type = "+Constant.FEE_TYPE_02+"\n");  
			sql.append("     and exists\n");
			sql.append("        (select 1\n");  
			sql.append("            from tt_as_wr_spefee_auditing a\n");  
			sql.append("           where a.status = "+Constant.SPEFEE_STATUS_04+"\n");  
			sql.append("             and a.fee_id = w.id\n");  
			sql.append("             and a.auditing_date <=\n");  
			sql.append("                 TO_DATE('"+bean.getEndTime()+"', 'YYYY-MM-DD HH24:MI:SS'))))\n");
		}
		if(Utility.testString(bean.getYieldly())){
			sql.append("AND w.YIELD = ").append(bean.getYieldly()).append("\n");
		}else{
			sql.append("and w.yield in (").append(bean.getYieldlys()).append(")\n");
		}
		if(Utility.testString(bean.getFeeType())){
			sql.append("AND w.FEE_TYPE = ").append(bean.getFeeType()).append("\n");
		}
		if(Utility.testString(bean.getFeeNo())){
			sql.append("AND w.FEE_NO LIKE '%").append(bean.getFeeNo()).append("%'\n");
		}
		if(Utility.testString(bean.getDealerCode())){
			sql.append("AND B.DEALER_CODE IN (").append(bean.getDealerCode()).append(")\n");
		}
		sql.append("  order by w.fee_no desc \n");
		List<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName());
		return ps;
	}
	
	public PageResult<Map<String, Object>> auditQuerySpeciaExpenses2(SpeciaExpensesBean bean,int curPage, int pageSize){
		StringBuffer sql= new StringBuffer();		
		sql.append("SELECT w.ID,\n");
		sql.append("       w.FEE_NO,\n");  
		sql.append("       w.DECLARE_SUM,\n");  
		sql.append("       declare_sum1,\n");  
		sql.append("       w.FEE_TYPE,\n");  
		sql.append("       w.YIELD,\n");  
		sql.append("       w.PASS_FEE,\n");  
		sql.append("       w.TRAFFIC_FEE,\n");  
		sql.append("       w.QUARTER_FEE,\n");  
		sql.append("       w.EAT_FEE,\n");  
		sql.append("       w.PERSON_SUBSIDE,\n");  
		sql.append("       w.CLAIMBALANCE_ID,\n");  
		sql.append("       w.CREATE_DATE,\n");  
		sql.append("       w.STATUS,\n");  
		sql.append("       w.DEALER_ID,\n");  
		sql.append("       (select s.dealer_code\n");  
		sql.append("          from tm_dealer s\n");  
		sql.append("         where s.dealer_id = w.dealer_id\n");  
		sql.append("           and rownum <= 1) as dealer_code,\n");  
		sql.append("       (select s.dealer_name\n");  
		sql.append("          from tm_dealer s\n");  
		sql.append("         where s.dealer_id = w.dealer_id\n");  
		sql.append("           and rownum <= 1) as dealer_name\n");  
		sql.append("  FROM TT_AS_WR_SPEFEE w,tm_dealer b\n");  
		sql.append(" WHERE w.dealer_id = b.dealer_id and ((w.FEE_TYPE = "+Constant.FEE_TYPE_01+" AND w.STATUS = "+Constant.SPEFEE_STATUS_02+") OR\n");  
		sql.append("       (w.FEE_TYPE = "+Constant.FEE_TYPE_02+" AND w.STATUS = "+Constant.SPEFEE_STATUS_04+"))\n");  
		sql.append("   AND w.CLAIMBALANCE_ID IS NOT NULL\n");  
		sql.append("   and exists (select 1\n");  
		sql.append("          from Tt_As_Wr_Claim_Balance c\n");  
		sql.append("         where c.id = w.claimbalance_id\n");  
		sql.append("           and c.status = "+Constant.ACC_STATUS_01+")\n");


		if(Utility.testString(bean.getBeginTime())){
			sql.append("AND w.UPDATE_DATE >= TO_DATE('").append(bean.getBeginTime()).append("', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(Utility.testString(bean.getEndTime())){
			sql.append("AND w.UPDATE_DATE <= TO_DATE('").append(bean.getEndTime()).append("', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(Utility.testString(bean.getYieldly())){
			sql.append("AND w.YIELD = ").append(bean.getYieldly()).append("\n");
		}
		if(Utility.testString(bean.getFeeType())){
			sql.append("AND w.FEE_TYPE = ").append(bean.getFeeType()).append("\n");
		}
		if(Utility.testString(bean.getFeeNo())){
			sql.append("AND w.FEE_NO LIKE '%").append(bean.getFeeNo()).append("%'\n");
		}
		if(Utility.testString(bean.getDealerCode())){
			sql.append("AND B.DEALER_CODE IN (").append(bean.getDealerCode()).append(")\n");
		}
		sql.append("  order by w.fee_no desc \n");
		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
		return ps;
	}
	
	public PageResult<Map<String, Object>> auditQuerySpeciaExpenses3(SpeciaExpensesBean bean,int curPage, int pageSize){
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT w.ID,\n");
		sql.append("       w.FEE_NO,\n");  
		sql.append("       w.DECLARE_SUM,\n");  
		sql.append("       declare_sum1,\n");  
		sql.append("       w.FEE_TYPE,\n");  
		sql.append("       w.YIELD,\n");  
		sql.append("       w.PASS_FEE,\n");  
		sql.append("       w.TRAFFIC_FEE,\n");  
		sql.append("       w.QUARTER_FEE,\n");  
		sql.append("       w.EAT_FEE,\n");  
		sql.append("       w.PERSON_SUBSIDE,\n");  
		sql.append("       w.CLAIMBALANCE_ID,\n");  
		sql.append("       w.CREATE_DATE,\n");  
		sql.append("       w.STATUS,\n");  
		sql.append("       w.DEALER_ID,\n");  
		sql.append("       (select s.dealer_code\n");  
		sql.append("          from tm_dealer s\n");  
		sql.append("         where s.dealer_id = w.dealer_id\n");  
		sql.append("           and rownum <= 1) as dealer_code,\n");  
		sql.append("       (select s.dealer_name\n");  
		sql.append("          from tm_dealer s\n");  
		sql.append("         where s.dealer_id = w.dealer_id\n");  
		sql.append("           and rownum <= 1) as dealer_name\n");  
		sql.append("  FROM TT_AS_WR_SPEFEE w,tm_dealer b\n");  
		sql.append(" WHERE w.dealer_id = b.dealer_id and ((w.FEE_TYPE = "+Constant.FEE_TYPE_01+" AND w.STATUS = "+Constant.SPEFEE_STATUS_02+") OR\n");  
		sql.append("       (w.FEE_TYPE = "+Constant.FEE_TYPE_02+" AND w.STATUS = "+Constant.SPEFEE_STATUS_04+"))\n");  
		sql.append("   AND w.CLAIMBALANCE_ID IS NOT NULL\n");  
		sql.append("   and exists (select 1\n");  
		sql.append("          from Tt_As_Wr_Claim_Balance c\n");  
		sql.append("         where c.id = w.claimbalance_id\n");  
		sql.append("           and c.status = "+Constant.ACC_STATUS_01+")\n");


		if(Utility.testString(bean.getBeginTime())){
			sql.append("AND w.CREATE_DATE >= TO_DATE('").append(bean.getBeginTime()).append("', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(Utility.testString(bean.getEndTime())){
			sql.append("AND w.CREATE_DATE <= TO_DATE('").append(bean.getEndTime()).append("', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(Utility.testString(bean.getYieldly())){
			sql.append("AND w.YIELD = ").append(bean.getYieldly()).append("\n");
		}
		if(Utility.testString(bean.getFeeType())){
			sql.append("AND w.FEE_TYPE = ").append(bean.getFeeType()).append("\n");
		}
		if(Utility.testString(bean.getFeeNo())){
			sql.append("AND w.FEE_NO LIKE '%").append(bean.getFeeNo()).append("%'\n");
		}
		if(Utility.testString(bean.getDealerCode())){
			sql.append("AND B.DEALER_CODE IN (").append(bean.getDealerCode()).append(")\n");
		}
		if(Utility.testString(bean.getBalanceId())){
			  sql.append(" and w.claimbalance_id=").append(bean.getBalanceId()).append("\n");
			}
		sql.append("  order by w.fee_no desc \n");
		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
		return ps;
	}
	public PageResult<Map<String, Object>> queryOmeSpeciaExpenses(SpeciaExpensesBean bean,int curPage, int pageSize){
		StringBuffer sql= new StringBuffer();
		sql.append("select B.ID,\n" );
		sql.append("       B.FEE_NO,\n" );
		sql.append("       D.DEALER_CODE,\n" );
		sql.append("       D.DEALER_NAME,\n" );
		sql.append("       to_char(B.Create_Date, 'yyyy-mm-dd') AS Create_Date,\n" );
		sql.append("       to_char(B.make_DATE, 'yyyy-mm-dd') AS make_DATE,\n" );
		sql.append("       to_char((select max(a.auditing_date) from Tt_As_Wr_Spefee_Auditing a where a.fee_id=b.id ),'yyyy-mm-dd') AS AUDITING_DATE,\n" );
		sql.append("       C.AREA_NAME,\n" );
		sql.append("       B.FEE_TYPE,\n" );
		sql.append("       B.Declare_Sum1 AS DECLARE_SUM,\n" );
		sql.append("       (select t.code_desc from tc_code t where t.code_id=B.Status) as Status,(select t.code_desc from tc_code t where t.code_id=B.o_Status) as o_Status,\n" );
		sql.append("       B.AUDITING_OPINION,\n" );
		sql.append("       B.section_AUDITING_OPINION ,\n" );
		sql.append("       B.maneger_AUDITING_OPINION,\n" );
		sql.append("       B.office_AUDITING_OPINION,\n" );
		sql.append("       B.director_AUDITING_OPINION, \n" );
		sql.append( "(SELECT X.CODE_DESC FROM TT_AS_WR_SPECIAL_CHARGE S, TC_CODE X\n" +
					"         WHERE s.spe_level = x.code_id AND X.TYPE = 1190 AND B.DECLARE_SUM1 >= S.MIN_AMOUNT\n" + 
					"           AND (B.DECLARE_SUM1 < S.MAX_AMOUNT OR S.MAX_AMOUNT IS NULL)) N_LEVEL");
		sql.append("  from  TT_AS_WR_SPEFEE B, Tm_Business_Area C, TM_DEALER D \n" );
		sql.append("  where C.AREA_ID = B.Yield AND B.DEALER_ID = D.DEALER_ID\n");
		
		if(Utility.testString(bean.getBeginTime())){
			sql.append(" AND (  B.make_DATE >= TO_DATE('"+bean.getBeginTime()+"', 'YYYY-MM-DD HH24:MI:SS') AND\n");  
		}
		if(Utility.testString(bean.getEndTime())){
			sql.append("    B.make_DATE <= TO_DATE('"+bean.getEndTime()+"', 'YYYY-MM-DD HH24:MI:SS')) \n");  
		}
		
		if(Utility.testString(bean.getCREATE_DATE_S())){
			sql.append(" AND (  B.Create_Date >= TO_DATE('"+bean.getCREATE_DATE_S()+"', 'YYYY-MM-DD HH24:MI:SS') AND\n");  
		}
		if(Utility.testString(bean.getCREATE_DATE_D())){
			sql.append("    B.Create_Date <= TO_DATE('"+bean.getCREATE_DATE_D()+"', 'YYYY-MM-DD HH24:MI:SS')) \n");  
		}
		if(Utility.testString(bean.getYieldly())){
			sql.append("  AND C.AREA_ID = ").append(bean.getYieldly()).append("\n");
		}
		if(Utility.testString(bean.getStatus())){
			sql.append("  AND B.Status = ").append(bean.getStatus()).append("\n");
		}
		
		if(Utility.testString(bean.getFeeType())){
			sql.append("   AND B.FEE_TYPE = ").append(bean.getFeeType()).append("\n");
		}

		if(Utility.testString(bean.getDealerCode())){
			sql.append(" AND B.DEALER_ID IN (  select d.DEALER_ID  from tm_dealer d  start with d.DEALER_ID in ("+bean.getDealerCode()+")   connect by PRIOR d.dealer_id = d.parent_dealer_d  ").append(")\n");
		}
		
		if(Utility.testString(bean.getFeeNo())){
			sql.append(" AND B.FEE_NO LIKE '%").append(bean.getFeeNo()).append("%'\n");
		}  
		
		sql.append(" order by B.create_date desc\n");

		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
		return ps;
	}
	public PageResult<Map<String, Object>> queryOmeSpeciaExpenses2(SpeciaExpensesBean bean,int curPage, int pageSize){
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT ROWNUM NUM, A.ID, A.FEE_NO, A.DECLARE_SUM,declare_sum1, A.FEE_TYPE, A.YIELD,\n" );
		sql.append("       TO_CHAR(A.CREATE_DATE, 'YYYY-MM-DD') CREATE_DATE, A.STATUS,\n" );
		sql.append("	   B.DEALER_CODE, B.DEALER_SHORTNAME\n");
		sql.append("FROM(\n" );
		sql.append("      SELECT ID, FEE_NO, DECLARE_SUM,declare_sum1, FEE_TYPE, YIELD,\n" );
		sql.append("             CREATE_DATE, STATUS, DEALER_ID,UPDATE_DATE\n" );
		sql.append("      FROM TT_AS_WR_SPEFEE\n" );
		sql.append("      WHERE FEE_TYPE = ").append(Constant.FEE_TYPE_01).append("\n");
		sql.append("	  AND YIELD IN (").append(bean.getYieldlys()).append(")\n"); 
		sql.append("	  AND COMPANY_ID = ").append(bean.getCompanyId()).append("\n"); 
		sql.append("      AND STATUS <> ").append(Constant.SPEFEE_STATUS_01).append("\n");
		sql.append("      UNION ALL\n" );
		sql.append("      SELECT ID, FEE_NO, DECLARE_SUM,declare_sum1, FEE_TYPE, YIELD,\n" );
		sql.append("             CREATE_DATE, STATUS, DEALER_ID,UPDATE_DATE\n" );
		sql.append("      FROM TT_AS_WR_SPEFEE\n" );
		sql.append("      WHERE FEE_TYPE = ").append(Constant.FEE_TYPE_02).append("\n");
		sql.append("	  AND YIELD IN (").append(bean.getYieldlys()).append(")\n"); 
		sql.append("	  AND COMPANY_ID = ").append(bean.getCompanyId()).append("\n"); 
		sql.append("      AND STATUS <> ").append(Constant.SPEFEE_STATUS_01).append("\n");
		sql.append("  ) A, TM_DEALER B, TM_DEALER_ORG_RELATION C\n" );
		sql.append("WHERE A.DEALER_ID = B.DEALER_ID\n");
		sql.append("AND B.DEALER_ID = C.DEALER_ID\n");
		if(Utility.testString(bean.getBeginTime())){
			sql.append("AND A.UPDATE_DATE >= TO_DATE('").append(bean.getBeginTime()).append("', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(Utility.testString(bean.getEndTime())){
			sql.append("AND A.UPDATE_DATE <= TO_DATE('").append(bean.getEndTime()).append("', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(Utility.testString(bean.getYieldly())){
			sql.append("AND A.YIELD = ").append(bean.getYieldly()).append("\n");
		}
		if(Utility.testString(bean.getFeeType())){
			sql.append("AND A.FEE_TYPE = ").append(bean.getFeeType()).append("\n");
		}
		if(Utility.testString(bean.getDealerCode())){
			sql.append("AND B.DEALER_CODE IN (").append(bean.getDealerCode()).append(")\n");
		}
		if(Utility.testString(bean.getDutyType())){
			if(bean.getDutyType().equals(String.valueOf(Constant.DUTY_TYPE_LARGEREGION))){
				if(Utility.testString(bean.getOrgId())){
					sql.append("AND C.ORG_ID = ").append(bean.getOrgId()).append("\n");
					sql.append("AND A.FEE_TYPE = ").append(Constant.FEE_TYPE_02).append("\n");
				}
			}
		}
		if(Utility.testString(bean.getFeeNo())){
			sql.append("AND A.FEE_NO LIKE '%").append(bean.getFeeNo()).append("%'\n");
		}
		sql.append(" order by a.create_date desc\n");

		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
		return ps;
	}
	public PageResult<Map<String, Object>> areaAuditQuerySpeciaExpenses(Long userId , SpeciaExpensesBean bean,int curPage, int pageSize){
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT ROWNUM NUM, A.ID, A.FEE_NO, A.DECLARE_SUM,a.declare_sum1, A.FEE_TYPE, A.YIELD,\n" );
		sql.append("       TO_CHAR(A.MAKE_DATE, 'YYYY-MM-DD') MAKE_DATE, A.STATUS, B.DEALER_CODE, DEALER_SHORTNAME\n" );
		sql.append("FROM TT_AS_WR_SPEFEE A, TM_DEALER B, VW_ORG_DEALER_SERVICE V\n" );
		sql.append("WHERE A.DEALER_ID = B.DEALER_ID\n");
		sql.append("and b.dealer_id = v.dealer_id\n");
		//查询tc_code 8008 区分轿微车
		TcCodePO code = new TcCodePO() ;
		code.setType("8008") ;
		List list = dao.select(code) ;
		if(list.size()>0)
			code = (TcCodePO)list.get(0);
		if(code.getCodeId().equals("80081001")){
			sql.append("and V.ROOT_ORG_ID = ").append(bean.getOrgId()).append("\n");
		}else{
			if(!bean.getOrgId().equals("2010010100070674")){
				sql.append("and V.ROOT_ORG_ID = ").append(bean.getOrgId()).append("\n");
			}
		}
		
		sql.append("AND A.FEE_TYPE = ").append(Constant.FEE_TYPE_02).append("\n");
		/******mod by liuxh 20101116 特殊外出费用大区审核时不需已结算*******/
		//sql.append("AND A.CLAIMBALANCE_ID IS NOT NULL \n");
		/******mod by liuxh 20101116 特殊外出费用大区审核时不需已结算*******/
		sql.append("AND A.STATUS = ").append(Constant.SPEFEE_STATUS_02).append("\n");
		sql.append("AND A.YIELD IN (").append(bean.getYieldlys()).append(")\n");
		//sql.append("AND A.DEALER_ID IN ( SELECT DEALER_ID FROM TM_DEALER_ORG_RELATION WHERE ORG_ID = ").append(bean.getOrgId()).append(")\n");
		
		if(Utility.testString(bean.getBeginTime())){
			sql.append("AND A.CREATE_DATE >= TO_DATE('").append(bean.getBeginTime()).append("', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(Utility.testString(bean.getEndTime())){
			sql.append("AND A.CREATE_DATE <= TO_DATE('").append(bean.getEndTime()).append("', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(Utility.testString(bean.getYieldly())){
			sql.append("AND A.YIELD = ").append(bean.getYieldly()).append("\n");
		}
		if(Utility.testString(bean.getFeeNo())){
			sql.append("AND A.FEE_NO LIKE '%").append(bean.getFeeNo()).append("%'\n");
		}
		if(Utility.testString(bean.getDealerCode())){
			sql.append("AND B.DEALER_CODE IN (").append(bean.getDealerCode()).append(")\n");
		}
		sql.append("AND A.COMPANY_ID = ").append(bean.getCompanyId()).append("\n"); 
		//zhumingwei 2011-03-07
		sql.append(UserProvinceRelation.getDealerIds(userId, "B")); 
		//zhumingwei 2011-03-07
		sql.append(" order by a.fee_no desc\n");
		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
		return ps;
	}
	
	public Map<String, Object> getSpeciaExpensesInfo(String id){
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT   nvl(a.DECLARE_SUM,0) as DECLARE_SUM,A.*,B.DEALER_CODE, B.DEALER_SHORTNAME, C.NAME, A.ACTIVITY_PROJECT,A.YIELD AS YIELD,A.FEE_TYPE ,\n" );
		sql.append("	   TO_CHAR(A.CREATE_DATE, 'YYYY-MM-DD HH24') CREATE_DATE1,\n");
		sql.append("	   TO_CHAR(A.START_DATE, 'YYYY-MM-DD HH24:MI') STARTDATE,\n");
		sql.append("	   TO_CHAR(A.END_DATE, 'YYYY-MM-DD HH24:MI') ENDDATE,d.activity_name,\n");
		sql.append("  A.balance_fee_type,A.part_code ,A.part_name,A.supplier_name,A.claim_No  ");
		sql.append("FROM TT_AS_WR_SPEFEE A, TM_DEALER B, TC_USER C,tt_as_activity d\n" );
		sql.append("WHERE A.DEALER_ID = B.DEALER_ID\n");
		sql.append(" and a.activity_id=d.activity_id(+)\n");
		sql.append("AND A.CREATE_BY = C.USER_ID\n");
		//sql.append("AND A.STATUS <> ").append(Constant.SPEFEE_STATUS_07).append("\n");
		sql.append("AND A.ID = ").append(id);

		Map<String, Object> map = pageQueryMap(sql.toString(), null, getFunName());
		return map;
	}
	
	public Map<String, Object> getSpeciaExpensesPrint(String id)
	{
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT B.DEALER_CODE, -- 经销商代码\n" );
		sql.append("       B.DEALER_NAME, -- 经销商名称\n" );
		sql.append("       '特殊费用' claim_type, -- 索赔单类型\n" );
		sql.append("       D.CODE_DESC FEE_TYPE, -- 索赔类型\n" );
		sql.append("       A.FEE_NO, -- 索赔单号\n" );
		sql.append("       G.GROUP_NAME SERIES_NAME, -- 车系\n" );
		sql.append("       a.v_model model_name, -- 车型\n" );
		sql.append("       p.group_name package_name, -- 车型状态\n" );
		sql.append("       a.vin, -- 车架号\n" );
		sql.append("       v.engine_no, -- 发动机号\n" );
		sql.append("       v.license_no, -- 牌照号\n" );
		sql.append("       a.linkman, -- 联系人\n" );
		sql.append("       a.linkman_tel, -- 联系电话\n" );
		sql.append("       a.balance_fee_type,");
		sql.append("       to_char(v.product_date,'yyyy-mm-dd') product_date, -- 购车日期\n" );
		sql.append("        to_char(v.FACTORY_DATE,'yyyy-mm-dd') FACTORY_DATE," );
		sql.append("       a.linkman user_name, -- 用户姓名\n" );
		sql.append("       a.linkman_tel user_tel, -- 用户电话\n" );
		sql.append("       to_char(a.make_date,'yyyy-mm-dd') make_date, -- 上报日期\n" );
		sql.append("       a.part_code, -- 主因件代码\n" );
		sql.append("       a.part_name, -- 主因件名称\n" );
		sql.append("       a.supplier_name, -- 制造商名称\n" );
		sql.append("       '' app_date, -- 授权时间\n" );
		sql.append("       v.mileage, -- 行驶里程\n" );
		sql.append("       nvl(z.total,0) total, -- 维修次数\n" );
		sql.append("       m.material_name mat_name, -- 装配代码\n" );
		sql.append("       b.ADDRESS, -- 用户地址\n" );
		sql.append("       a.apply_content remark, -- 申请费用原因\n" );
		sql.append("       a.o_status, -- 最终授权人\n" );
		sql.append("      (SELECT C.CTM_NAME FROM TT_DEALER_ACTUAL_SALES B,TT_CUSTOMER C WHERE B.CTM_ID=C.CTM_ID AND B.VEHICLE_ID=V.VEHICLE_ID) AS CUS_NAME, ");
		sql.append("      (SELECT C.MAIN_PHONE FROM TT_DEALER_ACTUAL_SALES B,TT_CUSTOMER C WHERE B.CTM_ID=C.CTM_ID AND B.VEHICLE_ID=V.VEHICLE_ID) AS MAIN_PHONE, ");
		sql.append("       decode(a.balance_fee_type,94141001,a.declare_sum1,0) lf_amount, -- 劳务费\n" );
		sql.append("       decode(a.balance_fee_type,94141002,a.declare_sum1,0) cl_amount, -- 材料费\n" );
		sql.append("       a.declare_sum1 amount, -- 总费用\n" );
		sql.append("       a.declare_sum declare_sum, -- 总审核费用\n" );
		sql.append("       ba.area_name AS YIELD -- 结算产地\n" );
		sql.append("  FROM TT_AS_WR_SPEFEE A\n" );
		sql.append("  LEFT JOIN tm_business_area ba ON a.yield = ba.area_id\n" );
		sql.append("  LEFT JOIN TM_DEALER B ON A.DEALER_ID = B.DEALER_ID\n" );
		sql.append("  LEFT JOIN TT_AS_ACTIVITY s ON A.ACTIVITY_ID = s.ACTIVITY_ID\n" );
		sql.append("  LEFT JOIN TC_CODE D ON D.CODE_ID = A.FEE_TYPE AND D.TYPE = 1183\n" );
		sql.append("  LEFT JOIN TM_VEHICLE V ON A.VIN = V.VIN\n" );
		sql.append("  LEFT JOIN TM_VHCL_MATERIAL_GROUP G ON V.SERIES_ID = G.GROUP_ID AND G.GROUP_LEVEL = 2\n" );
		sql.append("  LEFT JOIN TM_VHCL_MATERIAL_GROUP p ON V.Package_Id = p.group_id AND p.GROUP_LEVEL = 4\n" );
		sql.append("  LEFT JOIN TM_VHCL_MATERIAL m ON V.Material_Id = m.material_id\n" );
		sql.append("  LEFT JOIN (SELECT r.vin, r.part_code, max(r.cur_times) total\n" );
		sql.append("              FROM tt_as_wr_vin_part_repair_times r GROUP BY r.vin, r.part_code ) z ON z.vin = a.vin AND z.part_code = a.part_code\n" );
		sql.append(" WHERE A.ID = " + id);
		Map<String, Object> map = pageQueryMap(sql.toString(), null, getFunName());
		return map;
	}
	
	public List<Map<String, Object>> getSpeciaExpensesAudit(String id){
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT TO_CHAR(A.AUDITING_DATE, 'YYYY-MM-DD')  AUDITING_DATE,\n" );
		sql.append("       A.AUDITING_PERSON, B.ORG_NAME, (select tc.code_desc from tc_code tc where tc.code_id=A.STATUS) as STATUS, A.AUDITING_OPINION\n" );
		sql.append("FROM TT_AS_WR_SPEFEE_AUDITING A, TM_ORG B\n" );
		sql.append("WHERE A.PRESON_DEPT = B.ORG_ID\n" );
		//sql.append("AND A.STATUS <> ").append(Constant.SPEFEE_STATUS_02).append("\n");
		sql.append("AND A.FEE_ID = ").append(id).append("\n");
		sql.append("ORDER BY A.AUDITING_DATE ");

		List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
		return list;
	}
	
	public List<Map<String, Object>> getFeeClaimList(String id){
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT ROWNUM NUM, A.*, B.BALANCE_AMOUNT,B.YIELDLY,\n" );
		sql.append("       TO_CHAR(A.PRODUCT_DATE, 'YYYY-MM-DD') PRODUCT_DATE,\n" );
		sql.append("	   TO_CHAR(A.CLAIM_DATE, 'YYYY-MM-DD') CLAIM_DATE,\n");
		sql.append("	   B.REMARK\n");
		sql.append("FROM TT_AS_WR_SPEFEE_CLAIM A, TT_AS_WR_APPLICATION B\n" );
		sql.append("WHERE CLAIM_ID = B.ID ");
		sql.append("AND A.FEE_ID = ").append(id);

		List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
		return list;
	}
	
	public List<Map<String, Object>> getFeeRoList(String id){
		StringBuffer sql= new StringBuffer();
		sql.append("select rownum num,a.*,\n");
		sql.append("       to_char(a.product_date, 'yyyy-MM-dd') product_date,\n");  
		sql.append("       to_char(a.claim_date, 'yyyy-MM-dd') claim_date,\n");  
		sql.append("       v.yieldly\n");  
		sql.append("  from tt_as_wr_spefee_claim a, tt_as_repair_order b, tm_vehicle v\n");  
		sql.append(" where a.claim_id = b.id\n");  
		sql.append("   and b.vin = v.vin\n");  
		sql.append("   and a.fee_id =").append(id).append("\n");
		sql.append(" order by b.ro_no desc\n");

		List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
		return list;
	}
	
	public String getUserName(String id){
		String name = "";
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT NAME\n" );
		sql.append("FROM TC_USER\n" );
		sql.append("WHERE USER_ID = ").append(id);
		Map<String, Object> map = pageQueryMap(sql.toString(), null, getFunName());
		if(map!=null&&map.size()>0){
			name = String.valueOf(map.get("NAME"));
		}
		return name;
	}
	
	 public PageResult<Map<String, Object>> queryApplication (Map<String, String> map, int pageSize, int curPage) {
			StringBuffer sqlStr = new StringBuffer();
			sqlStr.append(" select a.*,TO_CHAR(C.PRODUCT_DATE, 'YYYY-MM-DD') PRODUCTDATE, \n");
			sqlStr.append("		   TO_CHAR(a.CREATE_DATE, 'YYYY-MM-DD') CREATEDATE");
			sqlStr.append(" from TT_AS_WR_APPLICATION a   \n");
			sqlStr.append(" left outer join TM_DEALER b on a.dealer_id=b.dealer_id \n");
			sqlStr.append(" left outer join tt_as_repair_order v  on a.ro_no=v.ro_no ");
			sqlStr.append(" left outer join TM_VEHICLE C  on a.VIN = C.VIN ");
			sqlStr.append(" where A.ID NOT IN (SELECT CLAIM_ID FROM TT_AS_WR_SPEFEE_CLAIM) ");
			// 经销商代码
			if (Utility.testString(map.get("dealerId"))) {
				sqlStr.append(" AND A.DEALER_ID='" + map.get("dealerId") + "' \n");
			}
			// 索赔单号
			if (Utility.testString(map.get("claimNo"))) {
				sqlStr.append(" AND A.CLAIM_NO LIKE '%" + map.get("claimNo")+ "%' \n");
			}
			// 车辆VIN码
			if (Utility.testString(map.get("vin"))) {// VIN涓嶄负绌�
				sqlStr.append(GetVinUtil.getVins(map.get("vin").replaceAll("'","\''"), "A"));
			}
			// 工单开始时间
			if (Utility.testString(map.get("roStartdate"))) {
				sqlStr.append(" AND A.CREATE_DATE >= to_date('"+ map.get("roStartdate")+ "', 'yyyy-mm-dd hh24:mi:ss') \n");
			}
			// 工单结束时间
			if (Utility.testString(map.get("roEnddate"))) {
				sqlStr.append(" AND A.CREATE_DATE <= to_date('"+ map.get("roEnddate")+ " 23:59:59', 'yyyy-mm-dd hh24:mi:ss') \n");
			}
			if(Utility.testString(map.get("channel"))){
				if(Constant.FEE_CHANNEL_01.equals((String)(map.get("channel")))){
					sqlStr.append(" and v.repair_type_code=").append(Constant.REPAIR_TYPE_03).append("\n");
					// 公司ID
					if (Utility.testString(map.get("yieldly"))) {
						sqlStr.append(" AND A.yieldly='" + map.get("yieldly")+ "' \n");
					}
				}else{
					sqlStr.append(" and v.repair_type_code in (").append(Constant.REPAIR_TYPE_01);
					sqlStr.append(",").append(Constant.REPAIR_TYPE_04).append(",").append(Constant.REPAIR_TYPE_05);
					sqlStr.append(")\n");
				}
			}
			//sqlStr.append(" AND A.STATUS='" + map.get("status") + "' \n");

			sqlStr.append(" ORDER BY a.claim_no DESC ");
			logger.info("-----------------------"+sqlStr.toString());
			PageResult<Map<String, Object>> ps = pageQuery(sqlStr.toString(), null, getFunName(),pageSize, curPage);
			return ps;
		}
	 
	 public PageResult<Map<String, Object>> queryRO (Map<String, String> map, int pageSize, int curPage) {
			StringBuffer sql = new StringBuffer("\n");
			sql.append("select a.*, v.yieldly,v.purchased_date\n");
			sql.append("  from tt_as_repair_order a, tm_vehicle v,tm_dealer d\n");  
			sql.append(" where 1 = 1\n");  
			sql.append("   and a.vin = v.vin\n");
			sql.append("   and a.dealer_code=d.dealer_code\n");
			sql.append("   and A.ID NOT IN (SELECT CLAIM_ID FROM TT_AS_WR_SPEFEE_CLAIM c\n");
			sql.append("       ,tt_as_wr_spefee s\n");
			sql.append("       where c.fee_id = s.id\n");  
			sql.append("       and s.status not in ("+Constant.SPEFEE_STATUS_05+","+Constant.SPEFEE_STATUS_03+"))\n");
			if (Utility.testString(map.get("vin"))) {
				sql.append(GetVinUtil.getVins(map.get("vin").replaceAll("'","\''"), "A"));
			}
			if (Utility.testString(map.get("dealerId"))) {
				sql.append("   AND d.DEALER_id='" + map.get("dealerId") + "' \n");
			}
			if (Utility.testString(map.get("roNo"))) {
				sql.append("   AND A.ro_NO LIKE '%" + map.get("roNo")+ "%' \n");
			}
			if (Utility.testString(map.get("roStartdate"))) {
				sql.append("   AND A.CREATE_DATE >= to_date('"+ map.get("roStartdate")+ "', 'yyyy-mm-dd hh24:mi:ss') \n");
			}
			if (Utility.testString(map.get("roEnddate"))) {
				sql.append("   AND A.CREATE_DATE <= to_date('"+ map.get("roEnddate")+ " 23:59:59', 'yyyy-mm-dd hh24:mi:ss') \n");
			}
			if(Utility.testString(map.get("channel"))){
				if(Constant.FEE_CHANNEL_01.equals((String)(map.get("channel")))){
					sql.append("   and a.repair_type_code=").append(Constant.REPAIR_TYPE_03).append("\n");
					// 公司ID
					if (Utility.testString(map.get("yieldly"))) {
						sql.append("   AND v.yieldly='" + map.get("yieldly")+ "' \n");
					}
				}else{
					sql.append("   and a.repair_type_code in (").append(Constant.REPAIR_TYPE_01);
					sql.append(",").append(Constant.REPAIR_TYPE_04).append(",").append(Constant.REPAIR_TYPE_05);
					sql.append(")\n");
				}
			}
			sql.append("   and (a.order_valuable_type = "+Constant.RO_PRO_STATUS_01+" or a.order_valuable_type is null)\n");
			sql.append("   and a.ro_status = "+Constant.RO_STATUS_02+"\n");
			sql.append(" order by a.ro_no desc \n");
			PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(),pageSize, curPage);
			return ps;
		}
	 
	 public List<Map<String, Object>> getClaimList(String id){
		 StringBuffer sql= new StringBuffer();
		 sql.append("SELECT A.ID CLAIM_ID, A.CLAIM_NO, A.CREATE_DATE, A.SERIES_CODE, A.MODEL_CODE,\n" );
		 sql.append("       A.VIN, A.ENGINE_NO, B.PRODUCT_DATE, A.IN_MILEAGE, A.BALANCE_AMOUNT\n" );
		 sql.append("FROM TT_AS_WR_APPLICATION A, TM_VEHICLE B\n" );
		 sql.append("WHERE A.VIN = B.VIN\n" );
		 sql.append("AND A.ID IN (").append(id).append(")\n"); 

		 List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
		 return list;
	 }
	 
	 public List<Map<String,Object>> getRoList(String id){
		 StringBuffer sql= new StringBuffer();
		 sql.append("SELECT A.ID ro_id, A.ro_NO, to_char(A.CREATE_DATE,'yyyy-MM-dd hh24:mi:ss') create_date, A.SERIES, A.MODEL,\n" );
		 sql.append("       A.VIN, A.ENGINE_NO, B.PRODUCT_DATE, A.IN_MILEAGE\n" );
		 sql.append("FROM TT_AS_repair_order A, TM_VEHICLE B\n" );
		 sql.append("WHERE A.VIN = B.VIN\n" );
		 sql.append("AND A.ID IN (").append(id).append(")\n"); 

		 List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
		 return list;
	 }
	 
	 public Map<String, Object> getModel(String vin){
		 StringBuffer sql= new StringBuffer();
		 sql.append("SELECT B.GROUP_ID, B.GROUP_NAME FROM TM_VEHICLE A, TM_VHCL_MATERIAL_GROUP B\n" );
		 sql.append("WHERE A.MODEL_ID = B.GROUP_ID\n" );
		 sql.append("AND A.VIN = '").append(vin).append("'\n");
		 Map<String, Object> map = pageQueryMap(sql.toString(), null, getFunName());
		 return map;
	 }
	 
	 public Integer getCount(String id){
		 Integer i = 0;
		 StringBuffer sql= new StringBuffer();
		 sql.append("SELECT COUNT(*) COUNT FROM TT_AS_WR_SPEFEE WHERE CLAIMBALANCE_ID = ").append(id);
		 Map<String, Object> map = pageQueryMap(sql.toString(), null, getFunName());
		 i = Integer.parseInt(String.valueOf(map.get("COUNT")));
		 return i;
	 }
	 
	 public PageResult<Map<String,Object>> getActivity(String con,String code,int pageSize,int curPage){
		 StringBuffer sql = new StringBuffer("\n");
		 sql.append("select a.activity_id,a.activity_code, a.activity_name, a.activity_type\n");
		 sql.append("  from tt_as_activity a\n");  
		 sql.append(" where a.activity_id in\n");  
		 sql.append("       (select d.activity_id\n");  
		 sql.append("          from tt_as_activity_dealer d\n");  
		 sql.append("         where d.dealer_id =\n");  
		 sql.append("               (select td.dealer_id\n");  
		 sql.append("                  from tm_dealer td\n");  
		 sql.append("                 where td.dealer_code = '"+code+"'))\n");
		 sql.append(" and a.startdate<=sysdate\n");
		 sql.append("  and a.enddate>=sysdate\n");
		 sql.append("   and a.is_special = 1 \n");
		 if(StringUtil.notNull(con)){
			 sql.append(con);
		 }
		 sql.append("order by a.activity_code asc\n");
		 return this.pageQuery(sql.toString(),null,this.getFunName(),pageSize , curPage);
	 }
	
	 public PageResult<Map<String,Object>> getCruise(String con,int pageSize,int curPage){
		 StringBuffer sql = new StringBuffer("\n");
		 sql.append("select * from tt_as_wr_cruise c\n");
		 sql.append(" where 1=1\n");
		 if(StringUtil.notNull(con))
			 sql.append(con);
		 sql.append(" order by c.cr_no asc\n");
		 return this.pageQuery(sql.toString(),null,this.getFunName(),pageSize , curPage);
	 }
	 
	 public List getVinNum(String id){
		 StringBuffer sql = new StringBuffer("\n");
		 sql.append("select c.vin from tt_as_wr_spefee_claim c where c.fee_id="+id+" group by c.vin");
		 return this.select(sql.toString(), null, TtAsWrSpefeeClaimPO.class);
	 }
	 
	 public PageResult<Map<String, Object>> reauditQuerySpeciaExpenses(SpeciaExpensesBean bean,int curPage, int pageSize){
			StringBuffer sql= new StringBuffer();
			sql.append("SELECT w.ID,\n");
			sql.append("       w.FEE_NO,\n");  
			sql.append("       w.DECLARE_SUM,\n");  
			sql.append("       declare_sum1,\n");  
			sql.append("       w.FEE_TYPE,\n");  
			sql.append("       w.YIELD,\n");  
			sql.append("       w.PASS_FEE,\n");  
			sql.append("       w.TRAFFIC_FEE,\n");  
			sql.append("       w.QUARTER_FEE,\n");  
			sql.append("       w.EAT_FEE,\n");  
			sql.append("       w.PERSON_SUBSIDE,\n");  
			sql.append("       w.CLAIMBALANCE_ID,\n");  
			sql.append("       to_char(w.CREATE_DATE,'yyyy-MM-dd') create_date,\n");  
			sql.append("       w.STATUS,\n");  
			sql.append("       w.DEALER_ID,\n");  
			sql.append("       (select s.dealer_code\n");  
			sql.append("          from tm_dealer s\n");  
			sql.append("         where s.dealer_id = w.dealer_id\n");  
			sql.append("           and rownum <= 1) as dealer_code,\n");  
			sql.append("       (select s.dealer_name\n");  
			sql.append("          from tm_dealer s\n");  
			sql.append("         where s.dealer_id = w.dealer_id\n");  
			sql.append("           and rownum <= 1) as dealer_name\n");  
			sql.append("  FROM TT_AS_WR_SPEFEE w,tm_dealer b\n");  
			sql.append(" WHERE w.dealer_id = b.dealer_id and (w.status = "+Constant.SPEFEE_STATUS_05+" or w.status = "+Constant.SPEFEE_STATUS_05+")\n");  
			sql.append("   AND w.CLAIMBALANCE_ID IS NOT NULL\n");  
			sql.append("   and exists (select 1\n");  
			sql.append("          from Tt_As_Wr_Claim_Balance c\n");  
			sql.append("         where c.id = w.claimbalance_id\n");  
			sql.append("           and c.status = "+Constant.ACC_STATUS_01+")\n");


			if(Utility.testString(bean.getBeginTime())){ 
				sql.append("  and w.update_date >=\n");  
				sql.append("     TO_DATE('"+bean.getBeginTime()+"', 'YYYY-MM-DD HH24:MI:SS')\n");
			}
			if(Utility.testString(bean.getEndTime())){
				sql.append("  and w.update_date <=\n");  
				sql.append("     TO_DATE('"+bean.getEndTime()+"', 'YYYY-MM-DD HH24:MI:SS')\n");
			}
			if(Utility.testString(bean.getYieldly())){
				sql.append("AND w.YIELD = ").append(bean.getYieldly()).append("\n");
			}else{
				sql.append("and w.yield in (").append(bean.getYieldlys()).append(")\n");
			}
			if(Utility.testString(bean.getFeeType())){
				sql.append("AND w.FEE_TYPE = ").append(bean.getFeeType()).append("\n");
			}
			if(Utility.testString(bean.getFeeNo())){
				sql.append("AND w.FEE_NO LIKE '%").append(bean.getFeeNo()).append("%'\n");
			}
			if(Utility.testString(bean.getDealerCode())){
				sql.append("AND B.DEALER_CODE IN (").append(bean.getDealerCode()).append(")\n");
			}
			sql.append("  order by w.fee_no desc \n");
			PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
			return ps;
		}
	 /*************************************add by kevinyin 20110418********************************************/
	 /**
	  * 市场，活动，特殊费用工单大区审核查询
	  */
		public PageResult<Map<String, Object>> areaAuditQuerySpeciaExpensesWC(Long userId , SpeciaExpensesBean bean,int curPage, int pageSize){
			StringBuffer sql= new StringBuffer();
			sql.append("SELECT ROWNUM NUM, A.ID, A.FEE_NO, A.DECLARE_SUM,a.declare_sum1, A.FEE_TYPE, A.YIELD,\n" );
			sql.append("       TO_CHAR(A.MAKE_DATE, 'YYYY-MM-DD') MAKE_DATE, A.STATUS, B.DEALER_CODE, DEALER_SHORTNAME\n" );
			sql.append("FROM TT_AS_WR_SPEFEE A, TM_DEALER B, VW_ORG_DEALER_SERVICE V\n" );
			sql.append("WHERE A.DEALER_ID = B.DEALER_ID\n");
			sql.append("and b.dealer_id = v.dealer_id\n");
			sql.append("and V.ROOT_ORG_ID = ").append(bean.getOrgId()).append("\n");
			
			/******mod by liuxh 20101116 特殊外出费用大区审核时不需已结算*******/
			sql.append("AND A.CLAIMBALANCE_ID IS NULL \n");
			/******mod by liuxh 20101116 特殊外出费用大区审核时不需已结算*******/
			sql.append("AND A.STATUS = ").append(Constant.SPEFEE_STATUS_02).append("\n");
			sql.append("AND A.YIELD IN (").append(bean.getYieldlys()).append(")\n");
			//sql.append("AND A.DEALER_ID IN ( SELECT DEALER_ID FROM TM_DEALER_ORG_RELATION WHERE ORG_ID = ").append(bean.getOrgId()).append(")\n");
			
			if(Utility.testString(bean.getBeginTime())){
				sql.append("AND A.CREATE_DATE >= TO_DATE('").append(bean.getBeginTime()).append("', 'YYYY-MM-DD HH24:MI:SS')\n");
			}
			if(Utility.testString(bean.getEndTime())){
				sql.append("AND A.CREATE_DATE <= TO_DATE('").append(bean.getEndTime()).append("', 'YYYY-MM-DD HH24:MI:SS')\n");
			}
			if(Utility.testString(bean.getYieldly())){
				sql.append("AND A.YIELD = ").append(bean.getYieldly()).append("\n");
			}
			if(Utility.testString(bean.getFeeNo())){
				sql.append("AND A.FEE_NO LIKE '%").append(bean.getFeeNo()).append("%'\n");
			}
			if(Utility.testString(bean.getDealerCode())){
				sql.append("AND B.DEALER_CODE IN (").append(bean.getDealerCode()).append(")\n");
			}
			if(Utility.testString(bean.getFeeType())){
				sql.append("AND A.FEE_TYPE = ").append(bean.getFeeType()).append("\n");
				if(Integer.valueOf(bean.getFeeType()).intValue()==Integer.valueOf(Constant.FEE_TYPE_01).intValue()){
					if(Utility.testString(bean.getFeeChannel())){
						sql.append("AND A.FEE_CHANNEL = (").append(bean.getFeeChannel()).append(")\n");
					}
				}else{
					if(Utility.testString(bean.getFeeChannel1())){
						sql.append("AND A.FEE_CHANNEL = (").append(bean.getFeeChannel1()).append(")\n");
					}
				}
				
				
			}
			sql.append("AND A.COMPANY_ID = ").append(bean.getCompanyId()).append("\n"); 
			//zhumingwei 2011-03-07
			sql.append(UserProvinceRelation.getDealerIds(userId, "B")); 
			//zhumingwei 2011-03-07
			sql.append(" order by a.fee_no desc\n");
			PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
			return ps;
		}
		
		/**
		 * 特殊费用工单结算室查询（微车）
		 * @param bean
		 * @param curPage
		 * @param pageSize
		 * @return
		 */
		public PageResult<Map<String, Object>> auditQuerySpeciaExpensesWC(Long userId,SpeciaExpensesBean bean,int curPage, int pageSize){
			StringBuffer sql= new StringBuffer();
			sql.append("SELECT w.ID,\n");
			sql.append("       w.FEE_NO,\n");  
			sql.append("       w.DECLARE_SUM,\n");  
			sql.append("       declare_sum1,\n");  
			sql.append("       w.FEE_TYPE,\n");  
			sql.append("       w.YIELD,\n");  
			sql.append("       w.PASS_FEE,\n");  
			sql.append("       w.TRAFFIC_FEE,\n");  
			sql.append("       w.QUARTER_FEE,\n");  
			sql.append("       w.EAT_FEE,\n");  
			sql.append("       w.PERSON_SUBSIDE,\n");  
			sql.append("       w.CLAIMBALANCE_ID,\n");  
			sql.append("       w.CREATE_DATE,\n");  
			sql.append("       w.STATUS,\n");  
			sql.append("       w.DEALER_ID,\n");  
			sql.append("       (select s.dealer_code\n");  
			sql.append("          from tm_dealer s\n");  
			sql.append("         where s.dealer_id = w.dealer_id\n");  
			sql.append("           and rownum <= 1) as dealer_code,\n");  
			sql.append("       (select s.dealer_name\n");  
			sql.append("          from tm_dealer s\n");  
			sql.append("         where s.dealer_id = w.dealer_id\n");  
			sql.append("           and rownum <= 1) as dealer_name\n");  
			sql.append("  FROM TT_AS_WR_SPEFEE w,tm_dealer b,TC_USER_REGION_RELATION j\n");  
			sql.append(" WHERE w.dealer_id = b.dealer_id and B.PROVINCE_ID = J.REGION_CODE and (w.FEE_TYPE = "+Constant.FEE_TYPE_01+" AND w.STATUS = "+Constant.SPEFEE_STATUS_04+" AND w.FEE_CHANNEL="+Constant.FEE_TYPE1_01+") \n");  
//			sql.append("       (w.FEE_TYPE = "+Constant.FEE_TYPE_02+" AND w.STATUS = "+Constant.SPEFEE_STATUS_04+"))\n");  
			sql.append("   and J.USER_ID="+userId+" AND w.CLAIMBALANCE_ID IS NULL\n");  
//			sql.append("   and exists (select 1\n");  
//			sql.append("          from Tt_As_Wr_Claim_Balance c\n");  
//			sql.append("         where c.id = w.claimbalance_id\n");  
//			sql.append("           and c.status = "+Constant.ACC_STATUS_01+")\n");


			if(Utility.testString(bean.getBeginTime())){
				sql.append("   AND \n");
				sql.append("    w.make_DATE >= TO_DATE('"+bean.getBeginTime()+"', 'YYYY-MM-DD HH24:MI:SS') \n");  
//				sql.append("    (w.fee_type = "+Constant.FEE_TYPE_02+"\n");  
//				sql.append("        and exists\n");
//				sql.append("        (select 1\n");  
//				sql.append("            from tt_as_wr_spefee_auditing a\n");  
//				sql.append("           where a.status = "+Constant.SPEFEE_STATUS_04+"\n");  
//				sql.append("             and a.fee_id = w.id\n");  
//				sql.append("             and a.auditing_date >=\n");  
//				sql.append("                 TO_DATE('"+bean.getBeginTime()+"', 'YYYY-MM-DD HH24:MI:SS'))))\n");
			}
			if(Utility.testString(bean.getEndTime())){
				sql.append("   AND \n");
				sql.append("    w.make_DATE <= TO_DATE('"+bean.getEndTime()+"', 'YYYY-MM-DD HH24:MI:SS') \n");  
//				sql.append("    (w.fee_type = "+Constant.FEE_TYPE_02+"\n");  
//				sql.append("     and exists\n");
//				sql.append("        (select 1\n");  
//				sql.append("            from tt_as_wr_spefee_auditing a\n");  
//				sql.append("           where a.status = "+Constant.SPEFEE_STATUS_04+"\n");  
//				sql.append("             and a.fee_id = w.id\n");  
//				sql.append("             and a.auditing_date <=\n");  
//				sql.append("                 TO_DATE('"+bean.getEndTime()+"', 'YYYY-MM-DD HH24:MI:SS'))))\n");
			}
			if(Utility.testString(bean.getYieldly())){
				sql.append("AND w.YIELD = ").append(bean.getYieldly()).append("\n");
			}else{
				sql.append("and w.yield in (").append(bean.getYieldlys()).append(")\n");
			}
			if(Utility.testString(bean.getFeeType())){
				sql.append("AND w.FEE_TYPE = ").append(bean.getFeeType()).append("\n");
			}
			if(Utility.testString(bean.getFeeNo())){
				sql.append("AND w.FEE_NO LIKE '%").append(bean.getFeeNo()).append("%'\n");
			}
			if(Utility.testString(bean.getDealerCode())){
				sql.append("AND B.DEALER_CODE IN (").append(bean.getDealerCode()).append(")\n");
			}
			sql.append("  order by w.fee_no desc \n");
			PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
			return ps;
		}
		
		/**
		 * 特殊费用逐条审核查询  writed by yx 20110111
		 * @param bean
		 * @param curPage
		 * @param pageSize
		 * @return
		 */
		public List<Map<String, Object>> auditQuerySpeciaExpensesByOneWC(Long userId,SpeciaExpensesBean bean){
			StringBuffer sql= new StringBuffer();
			sql.append("SELECT w.ID,\n");
			sql.append("       w.FEE_NO,\n");  
			sql.append("       w.DECLARE_SUM,\n");  
			sql.append("       declare_sum1,\n");  
			sql.append("       w.FEE_TYPE,\n");  
			sql.append("       w.YIELD,\n");  
			sql.append("       w.PASS_FEE,\n");  
			sql.append("       w.TRAFFIC_FEE,\n");  
			sql.append("       w.QUARTER_FEE,\n");  
			sql.append("       w.EAT_FEE,\n");  
			sql.append("       w.PERSON_SUBSIDE,\n");  
			sql.append("       w.CLAIMBALANCE_ID,\n");  
			sql.append("       w.CREATE_DATE,\n");  
			sql.append("       w.STATUS,\n");  
			sql.append("       w.DEALER_ID,\n");  
			sql.append("       (select s.dealer_code\n");  
			sql.append("          from tm_dealer s\n");  
			sql.append("         where s.dealer_id = w.dealer_id\n");  
			sql.append("           and rownum <= 1) as dealer_code,\n");  
			sql.append("       (select s.dealer_name\n");  
			sql.append("          from tm_dealer s\n");  
			sql.append("         where s.dealer_id = w.dealer_id\n");  
			sql.append("           and rownum <= 1) as dealer_name\n");  
			sql.append("  FROM TT_AS_WR_SPEFEE w,tm_dealer b,TC_USER_REGION_RELATION j\n");  
			sql.append(" WHERE w.dealer_id = b.dealer_id and B.PROVINCE_ID = J.REGION_CODE and (w.FEE_TYPE = "+Constant.FEE_TYPE_01+" AND w.STATUS = "+Constant.SPEFEE_STATUS_04+" AND w.FEE_CHANNEL="+Constant.FEE_TYPE1_01+") \n");  
			sql.append("   and J.USER_ID="+userId+" AND w.CLAIMBALANCE_ID IS NULL\n");  
//			sql.append("   and exists (select 1\n");  
//			sql.append("          from Tt_As_Wr_Claim_Balance c\n");  
//			sql.append("         where c.id = w.claimbalance_id\n");  
//			sql.append("           and c.status = "+Constant.ACC_STATUS_01+")\n");


			if(Utility.testString(bean.getBeginTime())){
				sql.append("   AND \n");
				sql.append("    w.make_DATE >= TO_DATE('"+bean.getBeginTime()+"', 'YYYY-MM-DD HH24:MI:SS') \n");  
			}
			if(Utility.testString(bean.getEndTime())){
				sql.append("   AND \n");
				sql.append("    w.make_DATE <= TO_DATE('"+bean.getEndTime()+"', 'YYYY-MM-DD HH24:MI:SS') \n");  
			}
			if(Utility.testString(bean.getYieldly())){
				sql.append("AND w.YIELD = ").append(bean.getYieldly()).append("\n");
			}else{
				sql.append("and w.yield in (").append(bean.getYieldlys()).append(")\n");
			}
			if(Utility.testString(bean.getFeeType())){
				sql.append("AND w.FEE_TYPE = ").append(bean.getFeeType()).append("\n");
			}
			
			
			if(Utility.testString(bean.getFeeNo())){
				sql.append("AND w.FEE_NO LIKE '%").append(bean.getFeeNo()).append("%'\n");
			}
			if(Utility.testString(bean.getDealerCode())){
				sql.append("AND B.DEALER_CODE IN (").append(bean.getDealerCode()).append(")\n");
			}
			sql.append("  order by w.fee_no desc \n");
			List<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName());
			return ps;
		}
		 public PageResult<Map<String, Object>> reauditQuerySpeciaExpensesWC(SpeciaExpensesBean bean,int curPage, int pageSize){
				StringBuffer sql= new StringBuffer();
				sql.append("SELECT w.ID,\n");
				sql.append("       w.FEE_NO,\n");  
				sql.append("       w.DECLARE_SUM,\n");  
				sql.append("       declare_sum1,\n");  
				sql.append("       w.FEE_TYPE,\n");  
				sql.append("       w.YIELD,\n");  
				sql.append("       w.PASS_FEE,\n");  
				sql.append("       w.TRAFFIC_FEE,\n");  
				sql.append("       w.QUARTER_FEE,\n");  
				sql.append("       w.EAT_FEE,\n");  
				sql.append("       w.PERSON_SUBSIDE,\n");  
				sql.append("       w.CLAIMBALANCE_ID,\n");  
				sql.append("       to_char(w.CREATE_DATE,'yyyy-MM-dd') create_date,\n");  
				sql.append("       w.STATUS,\n");  
				sql.append("       w.DEALER_ID,\n");  
				sql.append("       (select s.dealer_code\n");  
				sql.append("          from tm_dealer s\n");  
				sql.append("         where s.dealer_id = w.dealer_id\n");  
				sql.append("           and rownum <= 1) as dealer_code,\n");  
				sql.append("       (select s.dealer_name\n");  
				sql.append("          from tm_dealer s\n");  
				sql.append("         where s.dealer_id = w.dealer_id\n");  
				sql.append("           and rownum <= 1) as dealer_name\n");  
				sql.append("  FROM TT_AS_WR_SPEFEE w,tm_dealer b\n");  
				sql.append(" WHERE w.dealer_id = b.dealer_id \n");  
				
			
				if(Utility.testString(bean.getBeginTime())){ 
					sql.append("  and w.update_date >=\n");  
					sql.append("     TO_DATE('"+bean.getBeginTime()+"', 'YYYY-MM-DD HH24:MI:SS')\n");
				}
				if(Utility.testString(bean.getEndTime())){
					sql.append("  and w.update_date <=\n");  
					sql.append("     TO_DATE('"+bean.getEndTime()+"', 'YYYY-MM-DD HH24:MI:SS')\n");
				}
				if(Utility.testString(bean.getYieldly())){
					sql.append("AND w.YIELD = ").append(bean.getYieldly()).append("\n");
				}else{
					sql.append("and w.yield in (").append(bean.getYieldlys()).append(")\n");
				}
				if(Utility.testString(bean.getFeeType())){
					sql.append("AND w.FEE_TYPE = ").append(bean.getFeeType()).append("\n");
					if(Integer.valueOf(bean.getFeeType()).intValue()==Integer.valueOf(Constant.FEE_TYPE_01).intValue()){
						if(Utility.testString(bean.getFeeChannel())){
							sql.append("AND w.FEE_CHANNEL = (").append(bean.getFeeChannel()).append(")\n");
						}
					}else{
						if(Utility.testString(bean.getFeeChannel1())){
							sql.append("AND w.FEE_CHANNEL = (").append(bean.getFeeChannel1()).append(")\n");
						}
					}
					
					
				}
				if(Utility.testString(bean.getFeeNo())){
					sql.append("AND w.FEE_NO LIKE '%").append(bean.getFeeNo()).append("%'\n");
				}
				if(Utility.testString(bean.getDealerCode())){
					sql.append("AND B.DEALER_CODE IN (").append(bean.getDealerCode()).append(")\n");
				}
				if(Utility.testString(bean.getSpefeeStatus())){
					sql.append("AND w.status IN (").append(bean.getSpefeeStatus()).append(")\n");
				}
				if(Integer.valueOf(bean.getSpefeeStatus())==Integer.valueOf(Constant.SPEFEE_STATUS_05)){
					sql.append("   AND w.CLAIMBALANCE_ID IS NULL \n");  
					
				}
			
				
				
				sql.append("  order by w.fee_no desc \n");
				PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
				return ps;
			}
		 
			public PageResult<Map<String, Object>> queryOmeSpeciaExpensesWC(SpeciaExpensesBean bean,int curPage, int pageSize){
				StringBuffer sql= new StringBuffer();
				sql.append("SELECT ROWNUM NUM, A.ID, A.FEE_NO, A.DECLARE_SUM,declare_sum1, A.FEE_TYPE,A.FEE_CHANNEL, A.YIELD,\n" );
				sql.append("       TO_CHAR(A.CREATE_DATE, 'YYYY-MM-DD') CREATE_DATE, A.STATUS,\n" );
				sql.append("	   B.DEALER_CODE, B.DEALER_SHORTNAME\n");
				sql.append("FROM(\n" );
				sql.append("      SELECT ID, FEE_NO, DECLARE_SUM,declare_sum1, FEE_TYPE, FEE_CHANNEL,YIELD,\n" );
				sql.append("             CREATE_DATE, STATUS, DEALER_ID,make_date\n" );
				sql.append("      FROM TT_AS_WR_SPEFEE\n" );
				sql.append("      WHERE FEE_TYPE = ").append(Constant.FEE_TYPE_01).append("\n");
				sql.append("	  AND YIELD IN (").append(bean.getYieldlys()).append(")\n"); 
				sql.append("	  AND COMPANY_ID = ").append(bean.getCompanyId()).append("\n"); 
				sql.append("      AND STATUS <> ").append(Constant.SPEFEE_STATUS_01).append("\n");
				//sql.append("	  AND STATUS <> ").append(Constant.SPEFEE_STATUS_07).append("\n");
				sql.append("      UNION ALL\n" );
				sql.append("      SELECT ID, FEE_NO, DECLARE_SUM,declare_sum1, FEE_TYPE,FEE_CHANNEL, YIELD,\n" );
				sql.append("             CREATE_DATE, STATUS, DEALER_ID,make_date\n" );
				sql.append("      FROM TT_AS_WR_SPEFEE\n" );
				sql.append("      WHERE FEE_TYPE = ").append(Constant.FEE_TYPE_02).append("\n");
				sql.append("	  AND YIELD IN (").append(bean.getYieldlys()).append(")\n"); 
				sql.append("	  AND COMPANY_ID = ").append(bean.getCompanyId()).append("\n"); 
				sql.append("      AND STATUS <> ").append(Constant.SPEFEE_STATUS_01).append("\n");
				//sql.append("	  AND STATUS <> ").append(Constant.SPEFEE_STATUS_07).append("\n");
				sql.append("  ) A, TM_DEALER B, vw_org_dealer_service C\n" );
				sql.append("WHERE A.DEALER_ID = B.DEALER_ID\n");
				sql.append("AND B.DEALER_ID = C.DEALER_ID\n");
				if(Utility.testString(bean.getBeginTime())){
					sql.append("   AND ((a.fee_type = "+Constant.FEE_TYPE_01+" and\n");
					sql.append("    a.make_DATE >= TO_DATE('"+bean.getBeginTime()+"', 'YYYY-MM-DD HH24:MI:SS')) or\n");  
					sql.append("    (a.fee_type = "+Constant.FEE_TYPE_02+"\n");  
					sql.append("        and exists\n");
					sql.append("        (select 1\n");  
					sql.append("            from tt_as_wr_spefee_auditing ab\n");  
					sql.append("           where ab.status = "+Constant.SPEFEE_STATUS_04+"\n");  
					sql.append("             and ab.fee_id = a.id\n");  
					sql.append("             and ab.auditing_date >=\n");  
					sql.append("                 TO_DATE('"+bean.getBeginTime()+"', 'YYYY-MM-DD HH24:MI:SS'))))\n");
				}
				if(Utility.testString(bean.getEndTime())){
					sql.append("   AND ((a.fee_type = "+Constant.FEE_TYPE_01+" and\n");
					sql.append("    a.make_DATE <= TO_DATE('"+bean.getEndTime()+"', 'YYYY-MM-DD HH24:MI:SS')) or\n");  
					sql.append("    (a.fee_type = "+Constant.FEE_TYPE_02+"\n");  
					sql.append("     and exists\n");
					sql.append("        (select 1\n");  
					sql.append("            from tt_as_wr_spefee_auditing ab\n");  
					sql.append("           where ab.status = "+Constant.SPEFEE_STATUS_04+"\n");  
					sql.append("             and ab.fee_id = a.id\n");  
					sql.append("             and ab.auditing_date <=\n");  
					sql.append("                 TO_DATE('"+bean.getEndTime()+"', 'YYYY-MM-DD HH24:MI:SS'))))\n");
				}
				if(Utility.testString(bean.getYieldly())){
					sql.append("AND A.YIELD = ").append(bean.getYieldly()).append("\n");
				}
				if(Utility.testString(bean.getFeeType())){
					sql.append("AND A.FEE_TYPE = ").append(bean.getFeeType()).append("\n");
				}
				if(Utility.testString(bean.getFeeChannel())){
					sql.append("AND A.FEE_CHANNEL = ").append(bean.getFeeChannel()).append("\n");
				}
				if(Utility.testString(bean.getDealerCode())){
					sql.append("AND B.DEALER_CODE IN (").append(bean.getDealerCode()).append(")\n");
				}
//				if(Utility.testString(bean.getDutyType())){
//					if(bean.getDutyType().equals(String.valueOf(Constant.DUTY_TYPE_LARGEREGION))){
//						if(Utility.testString(bean.getOrgId())){
//							sql.append("AND C.ROOT_ORG_ID = ").append(bean.getOrgId()).append("\n");
//							sql.append("AND A.FEE_TYPE = ").append(Constant.FEE_TYPE_02).append("\n");
//						}
//					}
//				}
				if(Utility.testString(bean.getFeeNo())){
					sql.append("AND A.FEE_NO LIKE '%").append(bean.getFeeNo()).append("%'\n");
				}
				sql.append(" order by a.create_date desc\n");

				PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
				return ps;
			}
			
			public Map<String, Object> getSpeciaExpensesInfoWC(String id){
				StringBuffer sql= new StringBuffer();
				sql.append("SELECT A.*,B.DEALER_CODE, B.DEALER_SHORTNAME, C.NAME,A.ACTIVITY_PROJECT,\n" );
				sql.append("	   TO_CHAR(A.CREATE_DATE, 'YYYY-MM-DD HH24') CREATE_DATE1,\n");
				sql.append("	   TO_CHAR(A.START_DATE, 'YYYY-MM-DD HH24:MI') STARTDATE,\n");
				sql.append("	   TO_CHAR(A.END_DATE, 'YYYY-MM-DD HH24:MI') ENDDATE,d.activity_name\n");
				sql.append("FROM TT_AS_WR_SPEFEE A, TM_DEALER B, TC_USER C,tt_as_activity d\n" );
				sql.append("WHERE A.DEALER_ID = B.DEALER_ID\n");
				sql.append(" and a.activity_id=d.activity_id(+)\n");
				sql.append("AND A.CREATE_BY = C.USER_ID\n");
				//sql.append("AND A.STATUS <> ").append(Constant.SPEFEE_STATUS_07).append("\n");
				sql.append("AND A.ID = ").append(id);

				Map<String, Object> map = pageQueryMap(sql.toString(), null, getFunName());
				return map;
			}	
		/*************************************add by kevinyin 20110418********************************************/
			
		/******************轿车结算室审核特殊费用变更 add by kevinyin 20110530*************************/
	//主页面查询  被重构zyw 2014-9-12
	public PageResult<Map<String, Object>> auditQuerySpeciaExpensesJC(SpeciaExpensesBean bean,AclUserBean logonUser,int curPage, int pageSize){
		StringBuffer sql= new StringBuffer();
		sql.append("select A.Id AS AID,\n" );
		sql.append("       B.Id AS FID,\n" );
		sql.append("       B.FEE_NO,\n" );
		sql.append("       C.DEALER_CODE,\n" );
		sql.append("       C.dealer_shortname DEALER_NAME,\n" );
		sql.append("       to_char(B.create_date,'yyyy-mm-dd') AS create_date,\n" );
		sql.append("       to_char(B.MAKE_DATE,'yyyy-mm-dd') AS make_date,\n" );
		sql.append("       D.area_name,\n" );
		sql.append("       B.Fee_Type,\n" );
		sql.append("       B.Status,\n" );//这个是申请到哪一步的状态
		sql.append("       B.Declare_Sum1,\n" );
		sql.append("  B.O_STATUS ");//这个是定义需要最终到多少的状态
		sql.append("  from  ( SELECT F.FEE_ID FEE_ID, min( F.Id) ID,min(F.Status) Status  FROM   TT_AS_WR_SPEFEE_AUDITING  F GROUP by  F.FEE_ID ) A,\n" );
		sql.append("       TT_AS_WR_SPEFEE          B,\n" );
		sql.append("       TM_DEALER                C,\n" );
		sql.append("       Tm_Business_Area         D\n" );
		sql.append(" where A.FEE_ID = B.ID\n" );
		sql.append("   AND B.DEALER_ID = C.DEALER_ID\n" );
		sql.append("   AND B.Yield = D.AREA_ID ");
		sql.append(bean.getMsg());
	    DaoFactory.getsql(sql, "B.MAKE_DATE", bean.getBeginTime(), 3);
	    DaoFactory.getsql(sql, "B.MAKE_DATE", bean.getEndTime(), 4);
	    DaoFactory.getsql(sql, "B.CREATE_DATE", bean.getCREATE_DATE_S(), 3);
	    DaoFactory.getsql(sql, "B.CREATE_DATE", bean.getCREATE_DATE_D(), 4);
	    DaoFactory.getsql(sql, "D.AREA_ID", bean.getYieldly(),1);
	    DaoFactory.getsql(sql, "B.FEE_TYPE", bean.getFeeType(),1);
	    DaoFactory.getsql(sql, "B.FEE_NO", bean.getFeeNo(),2);
	    DaoFactory.getsql(sql, "C.DEALER_CODE", bean.getDealerCode(),6);
        sql.append("  order by B.fee_no desc \n");
        PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
	    return ps;
	}
			
			
			public PageResult<Map<String, Object>> auditQuerySpeciaExpensesJCDirector(SpeciaExpensesBean bean,int curPage, int pageSize){
				StringBuffer sql= new StringBuffer();
				sql.append("select A.Id AS AID,\n" );
				sql.append("       B.Id AS FID,\n" );
				sql.append("       B.FEE_NO,\n" );
				sql.append("       C.DEALER_CODE,\n" );
				sql.append("       C.dealer_shortname DEALER_NAME,\n" );
				sql.append("       to_char(B.create_date,'yyyy-mm-dd') AS create_date,\n" );
				sql.append("       to_char(B.MAKE_DATE,'yyyy-mm-dd') AS make_date,\n" );
				sql.append("       D.area_name,\n" );
				sql.append("       B.Fee_Type,\n" );
				sql.append("       B.Status,\n" );
				sql.append("       B.Declare_Sum1,\n" );
				sql.append("  B.O_STATUS ");
				sql.append("  from  (  SELECT t.FEE_ID,max(t.ID) ID  from TT_AS_WR_SPEFEE_AUDITING t  group by  t.FEE_ID ) A,\n" );
				sql.append("       TT_AS_WR_SPEFEE          B,\n" );
				sql.append("       TM_DEALER                C,\n" );
				sql.append("       Tm_Business_Area         D\n" );
				sql.append(" where A.FEE_ID = B.ID  AND B.Status = 11841009 \n" );
				
				sql.append("   AND B.DEALER_ID = C.DEALER_ID\n" );
				sql.append("   AND B.Yield = D.AREA_ID");
				
				if(Utility.testString(bean.getBeginTime())){
					sql.append("   AND (B.MAKE_DATE >= \n");
					sql.append("                 TO_DATE('"+bean.getBeginTime()+"', 'YYYY-MM-DD HH24:MI:SS'))\n");
					
				}
				if(Utility.testString(bean.getEndTime())){
					sql.append("   AND (B.MAKE_DATE <= \n");
					sql.append("                 TO_DATE('"+bean.getEndTime()+"', 'YYYY-MM-DD HH24:MI:SS'))\n");
				}
				
				if(Utility.testString(bean.getCREATE_DATE_S())){
					sql.append("   AND (B.create_date >= \n");
					sql.append("                 TO_DATE('"+bean.getCREATE_DATE_S()+"', 'YYYY-MM-DD HH24:MI:SS'))\n");
					
				}
				if(Utility.testString(bean.getCREATE_DATE_D())){
					sql.append("   AND (B.create_date <= \n");
					sql.append("                 TO_DATE('"+bean.getCREATE_DATE_D()+"', 'YYYY-MM-DD HH24:MI:SS'))\n");
				}
				if(Utility.testString(bean.getYieldly())){
					sql.append(" AND D.AREA_ID = ").append(bean.getYieldly()).append("\n");
				}
				
				if(Utility.testString(bean.getFeeType())){
					sql.append(" AND B.FEE_TYPE = ").append(bean.getFeeType()).append("\n");
				}
				if(Utility.testString(bean.getFeeNo())){
					sql.append(" AND B.FEE_NO LIKE '%").append(bean.getFeeNo()).append("%'\n");
				}
				if(Utility.testString(bean.getDealerCode())){
					sql.append(" AND C.DEALER_CODE IN (  select d.DEALER_CODE  from tm_dealer d  start with d.DEALER_CODE in ("+bean.getDealerCode()+")   connect by PRIOR d.dealer_id = d.parent_dealer_d  ").append(")\n");
				}
				sql.append("  order by B.fee_no desc \n");
				PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
				return ps;
			}
			
			
			/**
			 * 特殊费用逐条审核查询  writed by yx 20110111
			 * @param bean
			 * @param curPage
			 * @param pageSize
			 * @return
			 */
			public List<Map<String, Object>> auditQuerySpeciaExpensesByOneJC(SpeciaExpensesBean bean){
				StringBuffer sql= new StringBuffer();
				sql.append("SELECT w.ID,\n");
				sql.append("       w.FEE_NO,\n");  
				sql.append("       w.DECLARE_SUM,\n");  
				sql.append("       declare_sum1,\n");  
				sql.append("       w.FEE_TYPE,\n");  
				sql.append("       w.YIELD,\n");  
				sql.append("       w.PASS_FEE,\n");  
				sql.append("       w.TRAFFIC_FEE,\n");  
				sql.append("       w.QUARTER_FEE,\n");  
				sql.append("       w.EAT_FEE,\n");  
				sql.append("       w.PERSON_SUBSIDE,\n");  
				sql.append("       w.CLAIMBALANCE_ID,\n");  
				sql.append("       w.CREATE_DATE,\n");  
				sql.append("       w.STATUS,\n");  
				sql.append("       w.DEALER_ID,\n");  
				sql.append("       (select s.dealer_code\n");  
				sql.append("          from tm_dealer s\n");  
				sql.append("         where s.dealer_id = w.dealer_id\n");  
				sql.append("           and rownum <= 1) as dealer_code,\n");  
				sql.append("       (select s.dealer_name\n");  
				sql.append("          from tm_dealer s\n");  
				sql.append("         where s.dealer_id = w.dealer_id\n");  
				sql.append("           and rownum <= 1) as dealer_name\n");  
				sql.append("  FROM TT_AS_WR_SPEFEE w,tm_dealer b\n");  
				sql.append(" WHERE w.dealer_id = b.dealer_id and ((w.FEE_TYPE = "+Constant.FEE_TYPE_01+" AND w.STATUS = "+Constant.SPEFEE_STATUS_02+") OR\n");  
				sql.append("       (w.FEE_TYPE = "+Constant.FEE_TYPE_02+" AND w.STATUS = "+Constant.SPEFEE_STATUS_04+"))\n");  
				sql.append("   AND w.CLAIMBALANCE_ID IS  NULL\n");  
				sql.append("   and not exists (select 1\n");  
				sql.append("          from Tt_As_Wr_Claim_Balance c\n");  
				sql.append("         where c.id = w.claimbalance_id\n");  
				sql.append("           and c.status = "+Constant.ACC_STATUS_01+")\n");


				if(Utility.testString(bean.getBeginTime())){
					sql.append("   AND ((w.fee_type = "+Constant.FEE_TYPE_01+" and\n");
					sql.append("    w.make_DATE >= TO_DATE('"+bean.getBeginTime()+"', 'YYYY-MM-DD HH24:MI:SS')) or\n");  
					sql.append("    (w.fee_type = "+Constant.FEE_TYPE_02+"\n");  
					sql.append("        and exists\n");
					sql.append("        (select 1\n");  
					sql.append("            from tt_as_wr_spefee_auditing a\n");  
					sql.append("           where a.status = "+Constant.SPEFEE_STATUS_04+"\n");  
					sql.append("             and a.fee_id = w.id\n");  
					sql.append("             and a.auditing_date >=\n");  
					sql.append("                 TO_DATE('"+bean.getBeginTime()+"', 'YYYY-MM-DD HH24:MI:SS'))))\n");
				}
				if(Utility.testString(bean.getEndTime())){
					sql.append("   AND ((w.fee_type = "+Constant.FEE_TYPE_01+" and\n");
					sql.append("    w.make_DATE <= TO_DATE('"+bean.getEndTime()+"', 'YYYY-MM-DD HH24:MI:SS')) or\n");  
					sql.append("    (w.fee_type = "+Constant.FEE_TYPE_02+"\n");  
					sql.append("     and exists\n");
					sql.append("        (select 1\n");  
					sql.append("            from tt_as_wr_spefee_auditing a\n");  
					sql.append("           where a.status = "+Constant.SPEFEE_STATUS_04+"\n");  
					sql.append("             and a.fee_id = w.id\n");  
					sql.append("             and a.auditing_date <=\n");  
					sql.append("                 TO_DATE('"+bean.getEndTime()+"', 'YYYY-MM-DD HH24:MI:SS'))))\n");
				}
				if(Utility.testString(bean.getYieldly())){
					sql.append("AND w.YIELD = ").append(bean.getYieldly()).append("\n");
				}else{
					sql.append("and w.yield in (").append(bean.getYieldlys()).append(")\n");
				}
				if(Utility.testString(bean.getFeeType())){
					sql.append("AND w.FEE_TYPE = ").append(bean.getFeeType()).append("\n");
				}
				if(Utility.testString(bean.getFeeNo())){
					sql.append("AND w.FEE_NO LIKE '%").append(bean.getFeeNo()).append("%'\n");
				}
				if(Utility.testString(bean.getDealerCode())){
					sql.append("AND B.DEALER_CODE IN (").append(bean.getDealerCode()).append(")\n");
				}
				sql.append("  order by w.fee_no desc \n");
				List<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName());
				return ps;
			}
			/******************轿车结算室审核特殊费用变更 add by kevinyin 20110530*************************/
			public List<Map<String,Object>> maintaimHistory(String vin){ 
				ActionContext act = ActionContext.getContext();
			    //AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			    StringBuilder sql= new StringBuilder();
			    
			    sql.append("select b.dealer_code,b.dealer_name,a.fee_no,\n" );
			    sql.append("(select c.code_desc from tc_code c where c.code_id = a.status) status,\n" );
			    sql.append("a.vin,a.make_date,a.declare_sum1,a.declare_sum,a.apply_content,\n" );
			    sql.append("(select max(d.auditing_person) from tt_as_wr_spefee_auditing d where d.fee_id=a.id and d.status=11841004) person\n" );
			    sql.append("from TT_AS_WR_SPEFEE a, tm_dealer b\n" );
			    sql.append("where a.dealer_id = b.dealer_id and a.status in(11841006,11841007) and a.fee_type=11831001 AND a.vin='"+vin+"'\n" );
			    sql.append("ORDER BY a.make_date");
			    return this.pageQuery(sql.toString(), null, this.getFunName());
			}
			/**
			 * 查询特殊费用关联索赔单选择的数据
			 * @param request
			 * @param currDealerId
			 * @param currPage
			 * @param pageSize
			 * @return
			 */
			public PageResult<Map<String, Object>> showClaimNoData(RequestWrapper request, Long currDealerId,Integer currPage, Integer pageSize) {
				String VIN=request.getParamValue("VIN");
				String claim_no=request.getParamValue("claim_no");
				StringBuffer sb=new StringBuffer();
				sb.append("select a.* from Tt_As_Wr_Application a where a.dealer_id="+currDealerId);
				
				//String sql="select a.* from Tt_As_Wr_Application a where a.dealer_id="+currDealerId;
				if(VIN!=null){
					sb.append("and a.VIN like '%"+VIN+"%'");
				}
				if(claim_no!=null){
					sb.append("and a.claim_no like '%"+claim_no+"%'");
				}
				PageResult<Map<String, Object>> list=pageQuery(sb.toString(), null,getFunName(), pageSize, currPage);
				return list;
			}
}
