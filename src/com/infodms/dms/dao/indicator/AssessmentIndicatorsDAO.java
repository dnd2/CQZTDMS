package com.infodms.dms.dao.indicator;

import java.sql.ResultSet;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;


public class AssessmentIndicatorsDAO extends BaseDao<PO>{

	
	public static Logger logger = Logger.getLogger(AssessmentIndicatorsDAO.class);
	private static AssessmentIndicatorsDAO dao = new AssessmentIndicatorsDAO() ;
	
	private AssessmentIndicatorsDAO() {
		
	};
	
	public static AssessmentIndicatorsDAO getInstance() {
		return dao ;
	}
	
	public PageResult<Map<String, Object>> indicatorQuery(Map<String, String> map, int pageSize, int curPage) {
		String startDate = map.get("startDate");
		String endDate = map.get("endDate");
		String dealerId = map.get("dealerId");
		StringBuffer sql = new StringBuffer();
		/*sql.append("select tag.upload_date,\n");
		sql.append("       t1.ic001,\n");  
		sql.append("       t1.isale_car,\n");  
		sql.append("       t1.ics,\n");  
		sql.append("       tag.c003,\n");  
		sql.append("       tag.c002,\n");  
		sql.append("       (tag.c003 / decode(tag.c002,0,1,tag.c002)) p1,\n");  
		sql.append("       (tag.c005 + tag.c007 + tag.c009 + tag.c011 + tag.c013 + tag.c021) s1,\n");  
		sql.append("       (tag.c004 + tag.c006 + tag.c008 + tag.c010 + tag.c012 + tag.c020) s2,\n");  
		sql.append("       ((tag.c005 + tag.c007 + tag.c009 + tag.c011 + tag.c013 + tag.c021) / decode((tag.c004 + tag.c006 + tag.c008 + tag.c010 + tag.c012 + tag.c020),0,1,(tag.c004 + tag.c006 + tag.c008 + tag.c010 + tag.c012 + tag.c020))) p2,\n");  
		sql.append("       tag.c015,\n");  
		sql.append("       tag.c014,\n");  
		sql.append("       (tag.c015 / decode(tag.c014,0,1, tag.c014)) p3,\n");  
		//sql.append("       tag.c019,\n");  
		sql.append("       tag.claim_sheet_count,\n");  
		sql.append("       (tag.c019 / decode(tag.claim_sheet_count,0,1, tag.claim_sheet_count)) p4,\n");  
		sql.append("       t1.ic017,\n");  
		sql.append("       t1.isale_car tsc,\n");  
		sql.append("       t1.ics2,\n");  
		sql.append("       tag.c018,\n");  
		sql.append("       tag.c001,\n");  
		sql.append("       tag.c016,\n");  
		sql.append("       tag.c019,\n"); 
		sql.append("	   tag.c018 + tag.c001 + tag.c016 + tag.c019 total,\n");
		sql.append("	   rownum\n");
		sql.append("  from tt_assess_guideline tag\n");  
		sql.append(" right outer join (select sum(tt.c001) ic001,\n");  
		sql.append("                          sum(tt.sales_car) isale_car,\n");  
		sql.append("                          sum(tt.c017) ic017,\n");  
		sql.append("                          (sum(tt.c001) / decode(sum(tt.sales_car),0, 1, sum(tt.sales_car))) ics,\n"); 
		sql.append("						  (sum(tt.c017) / decode(sum(tt.sales_car),0, 1, sum(tt.sales_car))) ics2,\n");
		sql.append("                          tt.company_id ici \n");  
		sql.append("                     from tt_assess_guideline tt\n");  
		sql.append("                    group by trunc((to_char(tt.upload_date, 'mm') - 1) / 3),\n");  
		sql.append("                             tt.company_id) t1 on t1.ici = tag.company_id\n");
		sql.append("where 1=1\n");*/
		
		
		
		/*sql.append("select sum(tag.c001) ic001,\n");
		sql.append("       sum(tag.sales_car) isale_car,\n");  
		sql.append("       (sum(tag.sales_car) / decode(sum(tag.c001),0,1,sum(tag.c001))\n");  
		sql.append("       ) ics,\n");  
		sql.append("       sum(tag.c003) c003,\n");  
		sql.append("       sum(tag.c002) c002,\n");  
		sql.append("       (sum(tag.c003) / decode(sum(tag.c002), 0, 1, sum(tag.c002))) p1,\n");  
		sql.append("       (sum(tag.c005) + sum(tag.c007) + sum(tag.c009) + sum(tag.c011) +\n");  
		sql.append("       sum(tag.c013) + sum(tag.c021)) s1,\n");  
		sql.append("       (sum(tag.c004) + sum(tag.c006) + sum(tag.c008) + sum(tag.c010) +\n");  
		sql.append("       sum(tag.c012) + sum(tag.c020)) s2,\n");  
		sql.append("       ((sum(tag.c005) + sum(tag.c007) + sum(tag.c009) + sum(tag.c011) +\n");  
		sql.append("       sum(tag.c013) + sum(tag.c021)) /\n");  
		sql.append("       decode((sum(tag.c004) + sum(tag.c006) + sum(tag.c008) +\n");  
		sql.append("               sum(tag.c010) + sum(tag.c012) + sum(tag.c020)),\n");  
		sql.append("               0,\n");  
		sql.append("               1,\n");  
		sql.append("               (sum(tag.c004) + sum(tag.c006) + sum(tag.c008) +\n");  
		sql.append("               sum(tag.c010) + sum(tag.c012) + sum(tag.c020)))) p2,\n");  
		sql.append("       sum(tag.c015) c015,\n");  
		sql.append("       sum(tag.c014) c014,\n");  
		sql.append("       (sum(tag.c015) / decode(sum(tag.c014), 0, 1, sum(tag.c014))) p3,\n");  
		sql.append("       sum(tag.claim_sheet_count) claim_sheet_count,\n");  
		sql.append("       (sum(tag.claim_sheet_count) / decode(sum(tag.c019),0,1,sum(tag.c019))) p4,\n");  
		sql.append("       sum(tag.c017) ic017,\n");  
		sql.append("       sum(tag.sales_car) tsc,\n");  
		sql.append("       (sum(tag.sales_car) / decode(sum(tag.c017),0,1,sum(tag.c017)) ) ics2,\n");  
		sql.append("       sum(tag.c018) c018,\n");  
		sql.append("       sum(tag.c001) c001,\n");  
		sql.append("       sum(tag.c016) c016,\n");  
		sql.append("       sum(tag.c019) c019,\n");  
		sql.append("       sum(tag.c018) + sum(tag.c001) + sum(tag.c016) + sum(tag.c019) total,\n");  
		sql.append("       tmc.company_name\n");  
		sql.append("  from tt_assess_guideline tag, tm_company tmc\n");
  
		sql.append(" where 1 = 1\n");  
		sql.append("   and tag.company_id = tmc.company_id\n");  
		
		if(!CommonUtils.isNullString(endDate)) {
			if(CommonUtils.isNullString(startDate)) {
				sql.append("	and tag.upload_date <= to_date('");
				sql.append(endDate);
				sql.append("', 'yyyy-MM-dd HH24:MI:SS')\n");
			} else {
				sql.append("	and tag.upload_date >= to_date('");
				sql.append(startDate);
				sql.append("', 'yyyy-MM-dd HH24:MI:SS')\n");
				sql.append("	and tag.upload_date <= to_date('");
				sql.append(endDate);
				sql.append("', 'yyyy-MM-dd HH24:MI:SS')\n");
			}
		}
		if(!CommonUtils.isNullString(dealerId)) {
			sql.append("and tag.company_id in (");
			sql.append(dealerId);
			sql.append(")\n");
		}
		
		sql.append(" group by tmc.company_name\n");*/
		
		//TODO 2012-06-18 修改SQL
		sql.append("with sales as\n");
		sql.append(" (select tdas.dlr_company_id company_id, count(tdas.order_id) sales_amount\n");  
		sql.append("    from tt_dealer_actual_sales tdas\n");  
		sql.append("   where 1 = 1\n");  
		sql.append("        /*and tdas.dlr_company_id = ?*/\n");  
		if(!CommonUtils.isNullString(endDate)) {
			if(CommonUtils.isNullString(startDate)) {
				sql.append("     and tdas.sales_date <= to_date('\n");
				sql.append(endDate);
				sql.append(" 235959', 'yyyy-MM-dd HH24:MI:SS')\n");
			} else {
				sql.append("     and tdas.sales_date >= to_date('\n"); 
				sql.append(startDate);
				sql.append(" 000000', 'yyyy-MM-dd HH24:MI:SS')\n");
				sql.append("     and tdas.sales_date <=to_date('\n");
				sql.append(endDate);
				sql.append(" 235959', 'yyyy-MM-dd HH24:MI:SS')\n");
			}
		}
		sql.append("   group by tdas.dlr_company_id),\n");  
		sql.append("returns as\n");  
		sql.append(" (select tmd.company_id, count(tvasr.return_id) return_amount\n");  
		sql.append("    from tt_vs_actual_sales_return tvasr, tm_dealer tmd\n");  
		sql.append("   where 1 = 1\n");  
		sql.append("     and tvasr.dealer_id = tmd.dealer_id\n");  
		sql.append("        /*and tmd.company_id = ?*/\n"); 
		if(!CommonUtils.isNullString(endDate)) {
			if(CommonUtils.isNullString(startDate)) {
				sql.append("     and tvasr.update_date <= to_date('\n");  
				sql.append(endDate);
				sql.append(" 235959', 'yyyy-MM-dd HH24:MI:SS')\n");
			} else {
				sql.append("     and tvasr.update_date >= to_date('\n");
				sql.append(startDate);
				sql.append(" 000000', 'yyyy-MM-dd HH24:MI:SS')\n");
				sql.append("     and tvasr.update_date <= to_date('\n");  
				sql.append(endDate);
				sql.append(" 235959', 'yyyy-MM-dd HH24:MI:SS')\n");
			}
		}
		sql.append("   group by tmd.company_id),\n");  
		sql.append("tags as\n");  
		sql.append(" (select tag.company_id,\n");  
		sql.append("         nvl(tag.c001, 0) c001,\n");  
		sql.append("         nvl(tag.c002, 0) c002,\n");  
		sql.append("         nvl(tag.c003, 0) c003,\n");  
		sql.append("         nvl(tag.c004, 0) c004,\n");  
		sql.append("         nvl(tag.c005, 0) c005,\n");  
		sql.append("         nvl(tag.c006, 0) c006,\n");  
		sql.append("         nvl(tag.c007, 0) c007,\n");  
		sql.append("         nvl(tag.c008, 0) c008,\n");  
		sql.append("         nvl(tag.c009, 0) c009,\n");  
		sql.append("         nvl(tag.c010, 0) c010,\n");  
		sql.append("         nvl(tag.c011, 0) c011,\n");  
		sql.append("         nvl(tag.c012, 0) c012,\n");  
		sql.append("         nvl(tag.c013, 0) c013,\n");  
		sql.append("         nvl(tag.c014, 0) c014,\n");  
		sql.append("         nvl(tag.c015, 0) c015,\n");  
		sql.append("         nvl(tag.c016, 0) c016,\n");  
		sql.append("         nvl(tag.c017, 0) c017,\n");  
		sql.append("         nvl(tag.c018, 0) c018,\n");  
		sql.append("         nvl(tag.c019, 0) c019,\n");  
		sql.append("         nvl(tag.c020, 0) c020,\n");  
		sql.append("         nvl(tag.c021, 0) c021,\n");  
		sql.append("         nvl(tag.claim_sheet_count, 0) claim_sheet_count\n");  
		sql.append("    from tt_assess_guideline tag\n");  
		sql.append("   where 1 = 1\n");  
		if(!CommonUtils.isNullString(endDate)) {
			if(CommonUtils.isNullString(startDate)) {
				sql.append("    and tag.upload_date <= to_date('\n");  
				sql.append(endDate);
				sql.append(" 235959', 'yyyy-MM-dd HH24:MI:SS')\n");
			} else {
				sql.append("     and tag.upload_date >= to_date('\n");
				sql.append(startDate);
				sql.append(" 000000', 'yyyy-MM-dd HH24:MI:SS')\n");
				sql.append("    and tag.upload_date <= to_date('\n");  
				sql.append(endDate);
				sql.append(" 235959', 'yyyy-MM-dd HH24:MI:SS')\n");
			}
		}
		sql.append(")");
		sql.append("select sum(tag.c001) ic001,\n");  
		sql.append("       nvl(temp.sale_amount, 0) isale_car,\n");  
		sql.append("       (nvl(temp.sale_amount, 0) /\n");  
		sql.append("       decode(nvl(sum(tag.c001), 0), 0, 1, sum(tag.c001))) ics,\n");  
		sql.append("       sum(tag.c003) c003,\n");  
		sql.append("       sum(tag.c002) c002,\n");  
		sql.append("       (sum(tag.c003) / decode(sum(tag.c002), 0, 1, sum(tag.c002))) p1,\n");  
		sql.append("       (sum(tag.c005) + sum(tag.c007) + sum(tag.c009) + sum(tag.c011) +\n");  
		sql.append("       sum(tag.c013) + sum(tag.c021)) s1,\n");  
		sql.append("       (sum(tag.c004) + sum(tag.c006) + sum(tag.c008) + sum(tag.c010) +\n");  
		sql.append("       sum(tag.c012) + sum(tag.c020)) s2,\n");  
		sql.append("       ((sum(tag.c005) + sum(tag.c007) + sum(tag.c009) + sum(tag.c011) +\n");  
		sql.append("       sum(tag.c013) + sum(tag.c021)) /\n");  
		sql.append("       decode((sum(tag.c004) + sum(tag.c006) + sum(tag.c008) +\n");  
		sql.append("               sum(tag.c010) + sum(tag.c012) + sum(tag.c020)),\n");  
		sql.append("               0,\n");  
		sql.append("               1,\n");  
		sql.append("               (sum(tag.c004) + sum(tag.c006) + sum(tag.c008) +\n");  
		sql.append("               sum(tag.c010) + sum(tag.c012) + sum(tag.c020)))) p2,\n");  
		sql.append("       sum(tag.c015) c015,\n");  
		sql.append("       sum(tag.c014) c014,\n");  
		sql.append("       (sum(tag.c015) / decode(sum(tag.c014), 0, 1, sum(tag.c014))) p3,\n");  
		sql.append("       sum(tag.claim_sheet_count) claim_sheet_count,\n");  
		sql.append("       (sum(tag.claim_sheet_count) /\n");  
		sql.append("       decode(sum(tag.c019), 0, 1, sum(tag.c019))) p4,\n");  
		sql.append("       sum(tag.c017) ic017,\n");  
		sql.append("       nvl(temp.sale_amount, 0) tsc,\n");  
		sql.append("       (nvl(temp.sale_amount, 0) /\n");  
		sql.append("       decode(sum(tag.c017), 0, 1, sum(tag.c017))) ics2,\n");  
		sql.append("       sum(tag.c018) c018,\n");  
		sql.append("       sum(tag.c001) c001,\n");  
		sql.append("       sum(tag.c016) c016,\n");  
		sql.append("       sum(tag.c019) c019,\n");  
		sql.append("       sum(tag.c018) + sum(tag.c001) + sum(tag.c016) + sum(tag.c019) total,\n");  
		sql.append("       tmc.company_name\n");  
		sql.append("  from tags tag,\n");  
		sql.append("       tm_company tmc,\n");  
		sql.append("       ti_dealer_relation tdr,\n");  
		sql.append("       (select nvl(sales.sales_amount, 0) - nvl(returns.return_amount, 0) sale_amount,\n");  
		sql.append("               nvl(sales.company_id, returns.company_id) company_id\n");  
		sql.append("          from sales\n");  
		sql.append("          full outer join returns\n");  
		sql.append("            on sales.company_id = returns.company_id) temp\n");  
		sql.append(" where 1 = 1\n");  
		if(!CommonUtils.isNullString(dealerId)) {
			sql.append("and tag.company_id in (");
			sql.append(dealerId);
			sql.append(")\n");
		}
		sql.append("   and tdr.dcs_code = tmc.company_code\n");  
		sql.append("   and tmc.company_id = tag.company_id(+)\n");  
		sql.append("   and tmc.company_id = temp.company_id(+)\n");  
		sql.append("   and tdr.status = \n");  
		sql.append(Constant.STATUS_ENABLE);
		sql.append(" group by tmc.company_name, temp.sale_amount\n");

		
		return  dao.pageQuery(sql.toString(), null, dao.getFunName(), pageSize, curPage);
	}

	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}
