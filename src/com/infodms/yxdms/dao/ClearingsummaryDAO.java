package com.infodms.yxdms.dao;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;


import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.util.CommonUtils;
import com.infodms.yxdms.utils.DaoFactory;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

@SuppressWarnings("rawtypes")
public class ClearingsummaryDAO extends IBaseDao{
	
	private static ClearingsummaryDAO dao = new ClearingsummaryDAO();
	public static final ClearingsummaryDAO getInstance(){
		dao = (dao==null)?new ClearingsummaryDAO():dao;
		return dao;
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> findDate(RequestWrapper request,AclUserBean loginUser, Integer pageSizeMax, Integer currPage) {
		    String dealerId = loginUser.getDealerId();//经销商ID
		    String ORG_NAME = CommonUtils.checkNull(request.getParamValue("ORG_NAME"));		//产地代码
		    String startDate = CommonUtils.checkNull(request.getParamValue("startDate"));	//结算单号
		    String endDate = CommonUtils.checkNull(request.getParamValue("endDate"));		    //结算单装备
		    String DEALER_NAME = CommonUtils.checkNull(request.getParamValue("DEALER_NAME"));	//创建开始时间
		    String DEALER_CODE = CommonUtils.checkNull(request.getParamValue("DEALER_CODE"));		//创建结束时间
			
			StringBuffer sql= new StringBuffer();
			sql.append("select * from (\n" );
			sql.append("\n" );
			sql.append("select M.*, K.Return_Date,k.Sign_Date,k.IN_WARHOUSE_DATE,p.create_date,p.COLLECT_TICKETS_DATE,p.CHECK_TICKETS_DATE,p.TRANSFER_TICKETS_DATE, p.creat_dates from (\n" );
			sql.append("\n" );
			sql.append("select max( a.dealer_id ) dealer_id ,max(a.dealer_code) dealer_code , max(c.org_name) org_name, max(a.dealer_shortname) dealer_name ,to_date( to_char( t.sub_date,'yyyy-mm') || '-01','yyyy-mm-dd' ) k_sub_date,\n" );
			sql.append(" to_date( to_char( last_day(max(t.sub_date ) ),'yyyy-mm-dd'),'yyyy-mm-dd') j_sub_date , sum(nvl(t.repair_total,0 )) repair_total,\n" );
			sql.append(" sum(nvl(  decode ( t.claim_type ,10791005, 0,10791006,0,t.balance_amount),0)) balance_amount, count(1) CLAIMCOUNT\n" );
			sql.append("from tt_as_wr_application t ,tm_dealer a ,vw_org_dealer_service  b , tm_org c   where t.sub_date < to_date('2015-01-01','yyyy-mm-dd')\n" );
			sql.append("and a.dealer_id = b.dealer_id and c.org_id = b.root_org_id  and t.dealer_id = a.dealer_id  and a.dealer_id != 2014042694293110\n" );
			sql.append("group by  to_char( t.sub_date,'yyyy-mm') ,t.dealer_id   ) M\n" );
			sql.append("\n" );
			sql.append("left join (select a.dealer_id ,to_date(a.wr_start_date,'yyyy-mm-dd') wr_start_date,\n" );
			sql.append("a.return_end_date ,c.RETURN_DATE,c.Sign_Date ,  c.IN_WARHOUSE_DATE\n" );
			sql.append("from   tt_as_wr_returned_order a , Tr_Return_Logistics b, tt_as_wr_old_returned c\n" );
			sql.append("where a.id = b.return_id and b.logictis_id = c.id    and a.RETURN_TYPE = 10731002 and  a.dealer_id != 2014042694293110)    K on\n" );
			sql.append("k.dealer_id = M.dealer_id and k.wr_start_date <= M.k_sub_date and k.return_end_date >= M.j_sub_date\n" );
			sql.append("left join (select a.dealer_id,a.create_date , a.START_DATE,a.END_DATE, b.* from tt_as_wr_claim_balance a left join    (\n" );
			sql.append("select\n" );
			sql.append("   max(COLLECT_TICKETS_DATE) COLLECT_TICKETS_DATE,\n" );
			sql.append("   max(CHECK_TICKETS_DATE) CHECK_TICKETS_DATE,\n" );
			sql.append("   max(TRANSFER_TICKETS_DATE) TRANSFER_TICKETS_DATE,\n" );
			sql.append("   balance_oder, b.creat_date as creat_dates\n" );
			sql.append(" from tt_as_payment b  group by b.balance_oder, b.creat_date\n" );
			sql.append(") B\n" );
			sql.append("on  A.Remark = b.balance_oder ) P on P.dealer_id = M.dealer_id\n" );
			sql.append("and p.START_DATE = M.k_sub_date and P.END_DATE = M.j_sub_date\n" );
			sql.append("union all\n" );
			sql.append("select M.*, K.Return_Date,k.Sign_Date,k.IN_WARHOUSE_DATE,p.create_date,p.COLLECT_TICKETS_DATE,p.CHECK_TICKETS_DATE,p.TRANSFER_TICKETS_DATE, p.creat_dates from (\n" );
			sql.append("\n" );
			sql.append("select\n" );
			sql.append("    max( a.dealer_id ) dealer_id ,\n" );
			sql.append("    max(a.dealer_code) dealer_code ,\n" );
			sql.append("    max(c.org_name) org_name,\n" );
			sql.append("    max(a.dealer_shortname) dealer_name ,\n" );
			sql.append("    to_date(   to_char( add_months(to_date(t.sub_date,'yyyy-mm-dd'),-1),'yyyy-mm')   || '-26','yyyy-mm-dd' ) k_sub_date,\n" );
			sql.append("    to_date(  t.sub_date ,'yyyy-mm-dd') j_sub_date ,\n" );
			sql.append("   sum(nvl(t.repair_total,0 )) repair_total,\n" );
			sql.append(" sum(nvl(  decode(t.status,10791005,0 ,10791006,0,t.balance_amount) ,0)) balance_amount, count(1) CLAIMCOUNT\n" );
			sql.append("from\n" );
			sql.append(" (\n" );
			sql.append("  select\n" );
			sql.append("      case when\n" );
			sql.append("      to_number(to_char( t.report_date,'dd'))  < = 25\n" );
			sql.append("      then to_char(t.report_date,'yyyy-mm') || + '-25'\n" );
			sql.append("      else\n" );
			sql.append("      to_char( trunc(add_months(t.report_date, 1)),'yyyy-mm' ) || + '-25'\n" );
			sql.append("      end  sub_date,\n" );
			sql.append("      t.dealer_id,\n" );
			sql.append("      t.status ,\n" );
			sql.append("      t.balance_amount ,\n" );
			sql.append("      t.repair_total\n" );
			sql.append("      from tt_as_wr_application t\n" );
			sql.append("      where  t.sub_date > to_date('2015-01-01','yyyy-mm-dd')\n" );
			sql.append("      and t.report_date >= to_date('2014-12-26','yyyy-mm-dd') ) t ,tm_dealer a ,vw_org_dealer_service  b , tm_org c\n" );
			sql.append("  where  a.dealer_id = b.dealer_id and c.org_id = b.root_org_id  and t.dealer_id = a.dealer_id  and a.dealer_id != 2014042694293110\n" );
			sql.append("group by   t.sub_date ,t.dealer_id  ) M\n" );
			sql.append("\n" );
			sql.append("left join (select a.dealer_id ,to_date(a.wr_start_date,'yyyy-mm-dd') wr_start_date,\n" );
			sql.append("a.return_end_date ,c.RETURN_DATE,c.Sign_Date ,  c.IN_WARHOUSE_DATE\n" );
			sql.append("from   tt_as_wr_returned_order a , Tr_Return_Logistics b, tt_as_wr_old_returned c\n" );
			sql.append("where a.id = b.return_id and b.logictis_id = c.id    and a.RETURN_TYPE = 10731002 and  a.dealer_id != 2014042694293110)    K on\n" );
			sql.append("k.dealer_id = M.dealer_id and k.wr_start_date <= M.k_sub_date and k.return_end_date >= M.j_sub_date\n" );
			sql.append("left join (select a.dealer_id,a.create_date , a.START_DATE,a.END_DATE, b.* from tt_as_wr_claim_balance a left join    (\n" );
			sql.append("select\n" );
			sql.append("   max(COLLECT_TICKETS_DATE) COLLECT_TICKETS_DATE,\n" );
			sql.append("   max(CHECK_TICKETS_DATE) CHECK_TICKETS_DATE,\n" );
			sql.append("   max(TRANSFER_TICKETS_DATE) TRANSFER_TICKETS_DATE,\n" );
			sql.append("   balance_oder, b.creat_date as creat_dates\n" );
			sql.append(" from tt_as_payment b  group by b.balance_oder, b.creat_date\n" );
			sql.append(") B\n" );
			sql.append(" on  A.Remark = b.balance_oder ) P on P.dealer_id = M.dealer_id\n" );
			sql.append("and p.START_DATE = M.k_sub_date and P.END_DATE = M.j_sub_date ) M where 1=1 " );
			if(Utility.testString(dealerId)){
				sql.append( " and M.dealer_id =  "+dealerId );
			}else
			{
				if (this.getPoseRoleId(loginUser.getPoseId().toString()).equals(Constant.FWJL_ROLE_ID)) {
				       sql.append(CommonUtils.getOrgDealerLimitSqlByPose("M", loginUser));
				}
			}	
			if(Utility.testString(ORG_NAME))
				sql.append( " and M.ORG_NAME like '%"+ORG_NAME+"%' " );
			if(Utility.testString(DEALER_NAME))
				sql.append( " and M.DEALER_NAME like '%"+DEALER_NAME+"%' " );
			if(Utility.testString(DEALER_CODE))
				sql.append( " and M.DEALER_CODE like '%"+DEALER_CODE+"%' " );
			DaoFactory.getsql(sql, "M.K_SUB_DATE", startDate, 3);
			DaoFactory.getsql(sql, "M.J_SUB_DATE", endDate, 4);
			DaoFactory.getsql(sql, "M.CREAT_DATES", DaoFactory.getParam(request, "Creat_Date_start"), 3);//发票上报日期
			DaoFactory.getsql(sql, "M.CREAT_DATES", DaoFactory.getParam(request, "Creat_Date_end"), 4);
			sql.append(" order by M.k_sub_date\n\n" );
			

			PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSizeMax,  currPage);
			return ps;
	    }
	
	/**
	 * 通过职位ID获取角色ID
	 * @param poseId 职位ID
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String getPoseRoleId(String poseId) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT * FROM tr_role_pose p WHERE p.pose_id=" + poseId + "");
		List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
		if (null == list || list.size() <= 0 || list.get(0) == null || list.get(0).get("ROLE_ID") == null) {
			return "0";
		}
		return CommonUtils.checkNull(list.get(0).get("ROLE_ID"));
	}
	
	

}
