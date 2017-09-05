package com.infodms.dms.dao.report.jcafterservicereport;

import java.sql.ResultSet;
import java.util.Map;

import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.util.StringUtil;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

public class BalanceTrackingDao extends BaseDao {
	private BalanceTrackingDao(){}
	public static BalanceTrackingDao getInstance(){
		return new BalanceTrackingDao() ;
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public PageResult<Map<String,Object>> query4JC(String con,int pageSize,int curPage){
		StringBuffer sql = new StringBuffer("\n") ;
		sql.append("select rownum,d.dealer_code,\n");
		sql.append("       d.dealer_name,   s.root_dealer_code,       s.root_dealer_name,\n");  
		sql.append("       s.root_org_name,\n");  
		sql.append("       r.region_name,\n");  
		sql.append("       tc.code_desc yieldly,\n");  
		sql.append("       b.balance_no,\n");  
		sql.append("       b.note_amount,\n");  
		sql.append("       to_char(b.start_date, 'yyyy-MM-dd') || '至' ||\n");  
		sql.append("       to_char(b.end_date, 'yyyy-MM-dd') ro_date,\n");  
		sql.append("       (select to_char(max(wgb.sign_date), 'yyyy-MM-dd')\n");  
		sql.append("          from tt_as_wr_gather_balance wgb, tr_gather_balance tgb\n");  
		sql.append("         where tgb.gather_id = wgb.id\n");  
		sql.append("           and tgb.balance_id = b.id and rownum=1) sign_date,\n");  
		sql.append("       (select max(uu.name)\n");  
		sql.append("          from tt_as_wr_gather_balance gb, tr_gather_balance rgb, tc_user uu\n");  
		sql.append("         where gb.id = rgb.gather_id\n");  
		sql.append("           and rgb.balance_id = b.id\n");  
		sql.append("           and uu.user_id = gb.sign_person and rownum=1) sign_person,\n");  
		sql.append("       to_char(b.review_application_time, 'yyyy-MM-dd') reaudit_date,\n");  
		sql.append("       (select max(a.auth_person_name)\n");  
		sql.append("          from tt_as_wr_balance_authitem a\n");  
		sql.append("         where a.balance_id = b.id\n");  
		sql.append("           and a.auth_status = 11861006 and rownum=1) reaudit_person,\n");  
		sql.append("       (select to_char(max(ba.auth_time), 'yyyy-MM-dd')\n");  
		sql.append("          from tt_as_wr_balance_authitem ba\n");  
		sql.append("         where ba.balance_id = b.id\n");  
		sql.append("           and ba.auth_status = 11861007 and rownum=1) fance_date,\n");  
		sql.append("       (select max(a.auth_person_name)\n");  
		sql.append("          from tt_as_wr_balance_authitem a\n");  
		sql.append("         where a.balance_id = b.id\n");  
		sql.append("           and a.auth_status = 11861007 and rownum=1) fance_person,\n");  
		sql.append("       (select to_char(max(sa.auth_time), 'yyyy-MM-dd')\n");  
		sql.append("          from tt_taxable_sum_authitem sa, tt_labor_list_detail sd\n");  
		sql.append("         where sa.balance_id = sd.report_id\n");  
		sql.append("           and sd.balance_id = b.id\n");  
		sql.append("           and sa.auth_status = 11881001 and rownum=1) get_date,\n");  
		sql.append("       (select uuu.name\n");  
		sql.append("          from tt_taxable_sum_authitem aa,\n");  
		sql.append("               tt_labor_list_detail    ld,\n");  
		sql.append("               tc_user                 uuu\n");  
		sql.append("         where ld.report_id = aa.balance_id\n");  
		sql.append("           and aa.auth_status = 11881001\n");  
		sql.append("           and ld.balance_id = b.id\n");  
		sql.append("           and aa.auth_person_id = uuu.user_id and rownum=1) get_person,\n");  
		sql.append("       (select to_char(max(sa.auth_time), 'yyyy-MM-dd')\n");  
		sql.append("          from tt_taxable_sum_authitem sa, tt_labor_list_detail sd\n");  
		sql.append("         where sa.balance_id = sd.report_id\n");  
		sql.append("           and sd.balance_id = b.id\n");  
		sql.append("           and sa.auth_status = 11881002 and rownum=1) credit_date,\n");  
		sql.append("       (select uuu.name\n");  
		sql.append("          from tt_taxable_sum_authitem aa,\n");  
		sql.append("               tt_labor_list_detail    ld,\n");  
		sql.append("               tc_user                 uuu\n");  
		sql.append("         where ld.report_id = aa.balance_id\n");  
		sql.append("           and aa.auth_status = 11881002\n");  
		sql.append("           and ld.balance_id = b.id\n");  
		sql.append("           and aa.auth_person_id = uuu.user_id and rownum=1) credit_person,\n");  
		sql.append("       b.apply_amount,\n");  
		sql.append("       b.balance_amount\n");  
		sql.append("  from vw_org_dealer_service  s,\n");  
		sql.append("       tt_as_wr_claim_balance b,\n");  
		sql.append("       tm_dealer              d,\n");  
		sql.append("       tm_region              r,\n");  
		sql.append("       tc_code                tc\n");  
		sql.append(" where d.dealer_id = s.dealer_id\n");  
		sql.append("   and b.dealer_id = d.dealer_id\n");  
		sql.append("   and b.yieldly = tc.code_id\n");  
		sql.append("   and d.province_id = r.region_code\n");
		if(StringUtil.notNull(con))
			sql.append(con) ;
		return this.pageQuery(sql.toString(), null, this.getFunName(), pageSize, curPage);
	}
	
	public PageResult<Map<String,Object>> query4JC2(String con,Long poseId,int pageSize,int curPage){
		StringBuffer sql = new StringBuffer("\n") ;
		sql.append("select rownum,d.dealer_code,\n");
		sql.append("       d.dealer_name, s.root_dealer_code,       s.root_dealer_name,\n");  
		sql.append("       s.root_org_name,\n");  
		sql.append("       r.region_name,\n");  
		sql.append("       tc.code_desc yieldly,\n");  
		sql.append("       b.balance_no,\n");  
		sql.append("       b.note_amount,\n");  
		sql.append("       to_char(b.start_date, 'yyyy-MM-dd') || '至' ||\n");  
		sql.append("       to_char(b.end_date, 'yyyy-MM-dd') ro_date,\n");  
		sql.append("       (select to_char(max(wgb.sign_date), 'yyyy-MM-dd')\n");  
		sql.append("          from tt_as_wr_gather_balance wgb, tr_gather_balance tgb\n");  
		sql.append("         where tgb.gather_id = wgb.id\n");  
		sql.append("           and tgb.balance_id = b.id and rownum=1) sign_date,\n");  
		sql.append("       (select max(uu.name)\n");  
		sql.append("          from tt_as_wr_gather_balance gb, tr_gather_balance rgb, tc_user uu\n");  
		sql.append("         where gb.id = rgb.gather_id\n");  
		sql.append("           and rgb.balance_id = b.id\n");  
		sql.append("           and uu.user_id = gb.sign_person and rownum=1) sign_person,\n");  
		sql.append("       to_char(b.review_application_time, 'yyyy-MM-dd') reaudit_date,\n");  
		sql.append("       (select max(a.auth_person_name)\n");  
		sql.append("          from tt_as_wr_balance_authitem a\n");  
		sql.append("         where a.balance_id = b.id\n");  
		sql.append("           and a.auth_status = 11861006 and rownum=1) reaudit_person,\n");  
		sql.append("       (select to_char(max(ba.auth_time), 'yyyy-MM-dd')\n");  
		sql.append("          from tt_as_wr_balance_authitem ba\n");  
		sql.append("         where ba.balance_id = b.id\n");  
		sql.append("           and ba.auth_status = 11861007 and rownum=1) fance_date,\n");  
		sql.append("       (select max(a.auth_person_name)\n");  
		sql.append("          from tt_as_wr_balance_authitem a\n");  
		sql.append("         where a.balance_id = b.id\n");  
		sql.append("           and a.auth_status = 11861007 and rownum=1) fance_person,\n");  
		sql.append("       (select to_char(max(sa.auth_time), 'yyyy-MM-dd')\n");  
		sql.append("          from tt_taxable_sum_authitem sa, tt_labor_list_detail sd\n");  
		sql.append("         where sa.balance_id = sd.report_id\n");  
		sql.append("           and sd.balance_id = b.id\n");  
		sql.append("           and sa.auth_status = 11881001 and rownum=1) get_date,\n");  
		sql.append("       (select uuu.name\n");  
		sql.append("          from tt_taxable_sum_authitem aa,\n");  
		sql.append("               tt_labor_list_detail    ld,\n");  
		sql.append("               tc_user                 uuu\n");  
		sql.append("         where ld.report_id = aa.balance_id\n");  
		sql.append("           and aa.auth_status = 11881001\n");  
		sql.append("           and ld.balance_id = b.id\n");  
		sql.append("           and aa.auth_person_id = uuu.user_id and rownum=1) get_person,\n");  
		sql.append("       (select to_char(max(sa.auth_time), 'yyyy-MM-dd')\n");  
		sql.append("          from tt_taxable_sum_authitem sa, tt_labor_list_detail sd\n");  
		sql.append("         where sa.balance_id = sd.report_id\n");  
		sql.append("           and sd.balance_id = b.id\n");  
		sql.append("           and sa.auth_status = 11881002 and rownum=1) credit_date,\n");  
		sql.append("       (select uuu.name\n");  
		sql.append("          from tt_taxable_sum_authitem aa,\n");  
		sql.append("               tt_labor_list_detail    ld,\n");  
		sql.append("               tc_user                 uuu\n");  
		sql.append("         where ld.report_id = aa.balance_id\n");  
		sql.append("           and aa.auth_status = 11881002\n");  
		sql.append("           and ld.balance_id = b.id\n");  
		sql.append("           and aa.auth_person_id = uuu.user_id and rownum=1) credit_person,\n");  
		sql.append("       b.apply_amount,\n");  
		sql.append("       b.balance_amount\n");  
		sql.append("  from vw_org_dealer_service  s,\n");  
		sql.append("       tt_as_wr_claim_balance b,\n");  
		sql.append("       tm_dealer              d,\n");  
		sql.append("       tm_region              r,\n");  
		sql.append("       tc_code                tc\n");  
		sql.append(" where d.dealer_id = s.dealer_id\n");  
		sql.append("   and b.dealer_id = d.dealer_id\n");  
		sql.append("   and b.yieldly = tc.code_id\n");  
		sql.append("   and d.province_id = r.region_code\n");
		
		sql.append(" and b.yieldly in ("+this.businessArea(poseId)+")");
		if(StringUtil.notNull(con))
			sql.append(con) ;
		return this.pageQuery(sql.toString(), null, this.getFunName(), pageSize, curPage);
	}
	//查询业务范围的SQL
	public String businessArea(long poseId){
		String area="select distinct  ba.produce_base from tm_pose_business_area pa, tm_business_area ba where pa.area_id = ba.area_id  and pa.pose_id = "+poseId+" and ba.status = 10011001";
		return area;
	}
	public PageResult<Map<String,Object>> query4WC(String con,int pageSize,int curPage){
		StringBuffer sql = new StringBuffer("\n") ;
		sql.append("select rownum,d.dealer_code,\n");
		sql.append("       d.dealer_name,\n");  
		sql.append("       s.root_org_name,\n");  
		sql.append("       r.region_name,\n");  
		sql.append("       tc.code_desc yieldly,\n");  
		sql.append("       b.balance_no,\n");  
		sql.append("       to_char(b.start_date, 'yyyy-MM-dd') || '至' ||\n");  
		sql.append("       to_char(b.end_date, 'yyyy-MM-dd') ro_date,\n");  
		sql.append("       (select to_char(max(wgb.sign_date), 'yyyy-MM-dd')\n");  
		sql.append("          from tt_as_wr_gather_balance wgb, tr_gather_balance tgb\n");  
		sql.append("         where tgb.gather_id = wgb.id\n");  
		sql.append("           and tgb.balance_id = b.id) sign_date,\n");  
		sql.append("       (select max(uu.name)\n");  
		sql.append("          from tt_as_wr_gather_balance gb, tr_gather_balance rgb, tc_user uu\n");  
		sql.append("         where gb.id = rgb.gather_id\n");  
		sql.append("           and rgb.balance_id = b.id\n");  
		sql.append("           and uu.user_id = gb.sign_person) sign_person,\n");  
		sql.append("       to_char(b.review_application_time, 'yyyy-MM-dd') reaudit_date,\n");  
		sql.append("       (select max(a.auth_person_name)\n");  
		sql.append("          from tt_as_wr_balance_authitem a\n");  
		sql.append("         where a.balance_id = b.id\n");  
		sql.append("           and a.auth_status = 11861006) reaudit_person,\n");  
		sql.append("       (select to_char(max(ba.auth_time), 'yyyy-MM-dd')\n");  
		sql.append("          from tt_as_wr_balance_authitem ba\n");  
		sql.append("         where ba.balance_id = b.id\n");  
		sql.append("           and ba.auth_status = 11861007) fance_date,\n");  
		sql.append("       (select max(a.auth_person_name)\n");  
		sql.append("          from tt_as_wr_balance_authitem a\n");  
		sql.append("         where a.balance_id = b.id\n");  
		sql.append("           and a.auth_status = 11861007) fance_person,\n");  
		sql.append("       (select to_char(max(sa.auth_time), 'yyyy-MM-dd')\n");  
		sql.append("          from tt_taxable_sum_authitem sa, tt_labor_list_detail sd\n");  
		sql.append("         where sa.balance_id = sd.report_id\n");  
		sql.append("           and sd.balance_id = b.id\n");  
		sql.append("           and sa.auth_status = 11881001) get_date,\n");  
		sql.append("       (select uuu.name\n");  
		sql.append("          from tt_taxable_sum_authitem aa,\n");  
		sql.append("               tt_labor_list_detail    ld,\n");  
		sql.append("               tc_user                 uuu\n");  
		sql.append("         where ld.report_id = aa.balance_id\n");  
		sql.append("           and aa.auth_status = 11881001\n");  
		sql.append("           and ld.balance_id = b.id\n");  
		sql.append("           and aa.auth_person_id = uuu.user_id) get_person,\n");  
		sql.append("       (select to_char(max(sa.auth_time), 'yyyy-MM-dd')\n");  
		sql.append("          from tt_taxable_sum_authitem sa, tt_labor_list_detail sd\n");  
		sql.append("         where sa.balance_id = sd.report_id\n");  
		sql.append("           and sd.balance_id = b.id\n");  
		sql.append("           and sa.auth_status = 11881002) credit_date,\n");  
		sql.append("       (select uuu.name\n");  
		sql.append("          from tt_taxable_sum_authitem aa,\n");  
		sql.append("               tt_labor_list_detail    ld,\n");  
		sql.append("               tc_user                 uuu\n");  
		sql.append("         where ld.report_id = aa.balance_id\n");  
		sql.append("           and aa.auth_status = 11881002\n");  
		sql.append("           and ld.balance_id = b.id\n");  
		sql.append("           and aa.auth_person_id = uuu.user_id) credit_person,\n");  
		sql.append("       b.apply_amount,\n");  
		sql.append("       b.balance_amount\n");  
		sql.append("  from vw_org_dealer_service  s,\n");  
		sql.append("       tt_as_wr_claim_balance b,\n");  
		sql.append("       tm_dealer              d,\n");  
		sql.append("       tm_region              r,\n");  
		sql.append("       tc_code                tc\n");  
		sql.append(" where d.dealer_id = s.dealer_id\n");  
		sql.append("   and b.dealer_id = d.dealer_id\n");  
		sql.append("   and b.yieldly = tc.code_id\n");  
		sql.append("   and d.province_id = r.region_code\n");
		if(StringUtil.notNull(con))
			sql.append(con) ;
		return this.pageQuery(sql.toString(), null, this.getFunName(), pageSize, curPage);
	}
}
