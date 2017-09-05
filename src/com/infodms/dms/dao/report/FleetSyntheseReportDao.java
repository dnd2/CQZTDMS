package com.infodms.dms.dao.report;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.dao.report.jcafterservicereport.SpecialCostReportDao;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.po3.bean.PO;

/**
 * 集团客户综合查询DAO
 * @author HXY
 * 2012-04-12
 * */
public class FleetSyntheseReportDao extends BaseDao<PO> {

public static Logger logger = Logger.getLogger(SpecialCostReportDao.class);
	
	
	private static FleetSyntheseReportDao dao = new FleetSyntheseReportDao() ;
	
	public FleetSyntheseReportDao() {
		
	};
	
	public static FleetSyntheseReportDao getInstance() {
		return dao;
	}
	
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	   * 集团客户综合查询
	   * @param map 参数列表
	   * 2012-04-09
	   * HXY
	   * */
//	  public List<Map<String,Object>> getFleetSyntheseReport(Map<String, String> map) {
//		  StringBuffer sql = new StringBuffer();
//		  sql.append("select distinct /*+ all_rows */ tf.series_id, --报备id\n");
//		  sql.append("                vod.root_org_name, --区域事业部\n");  
//		  sql.append("                tr2.region_name as province, --省\n");  
//		  sql.append("                tf.submit_date, --提报日期\n");  
//		  sql.append("                td.dealer_name, --提报单位\n");  
//		  sql.append("                tf.fleet_name, --客户名称\n");  
//		  sql.append("                tf.fleet_type, --客户类型\n");  
//		  sql.append("                tf.main_business, --主营业务\n");  
//		  sql.append("                tf.zip_code, --邮编\n");  
//		  sql.append("                tr.region_name as region, --区域\n");  
//		  sql.append("                tf.address, --详细地址\n");  
//		  sql.append("                tf.main_linkman, --主联系人\n");  
//		  sql.append("                tf.main_job, --职务\n");  
//		  sql.append("                tf.main_phone, --电话\n");  
//		  sql.append("                tvmg2.group_name as cars, --车系\n");  
//		  sql.append("                tf.series_count, --数量\n");  
//		  sql.append("                tff.follow_remark, --备注\n");  
//		  sql.append("                tf.status, --确认状态\n");  
//		  sql.append("                tf.req_remark, --确认说明\n");  
//		  sql.append("                tu.name, --确认人\n");  
//		  sql.append("                tf.audit_date, --确认时间\n");  
//		  sql.append("                tff.follow_date, --跟进时间\n");  
//		  sql.append("                tff.follow_remark, --跟进内容\n");  
//		  sql.append("                tfc.check_date, --签约时间\n");  
//		  sql.append("                tfc.start_date, --有效期起\n");  
//		  sql.append("                tfc.end_date, --有效期止\n");  
//		  sql.append("                tvmg.group_name, --签约车系\n");  
//		  sql.append("                tfin.intent_count, --数量\n");  
//		  sql.append("                count(tdas.order_id) as actCount --实销审核数量\n");  
//		  sql.append("  from VW_ORG_DEALER          vod,\n");  
//		  sql.append("       TM_DEALER              td,\n");  
//		  sql.append("       TM_FLEET               tf,\n");  
//		  sql.append("       (select tff3.follow_date, tff3.follow_remark, tff3.fleet_id from tm_fleet_follow tff3,\n");  
//		  sql.append("               (select tff2.fleet_id,max(rowid) as follow_date from tm_fleet_follow tff2\n");  
//		  sql.append("               where tff2.fleet_id = tff2.fleet_id\n");  
//		  sql.append("               group by tff2.fleet_id) tmp\n");  
//		  sql.append("        where tff3.fleet_id = tmp.fleet_id) tff,\n");  
//		  sql.append("       TT_FLEET_CONTRACT      tfc,\n");  
//		  sql.append("       TT_DEALER_ACTUAL_SALES tdas,\n");  
//		  sql.append("       TT_FLEET_INTENT_NEW    tfin,\n");  
//		  sql.append("       TM_VHCL_MATERIAL_GROUP tvmg,\n");  
//		  sql.append("       TM_VHCL_MATERIAL_GROUP tvmg2,\n");  
//		  sql.append("       tc_user                tu,\n");  
//		  sql.append("       tm_region              tr,\n");  
//		  sql.append("       tm_region              tr2,\n");  
//		  sql.append("       tm_company             tc\n");  
//		  sql.append(" where td.company_id(+) = tf.dlr_company_id\n");  
//		  sql.append("   and vod.dealer_id in td.dealer_id\n");  
//		  sql.append("   and tf.series_id = tvmg2.group_id(+)\n");  
//		  sql.append("   and tfin.series_id = tvmg.group_id(+)\n");  
//		  sql.append("   and tfin.contract_id(+) = tfc.contract_id\n");  
//		  sql.append("   and tu.user_id(+) = tf.audit_user_id\n");  
//		  sql.append("   and tdas.contract_id(+) = tfc.contract_id\n");  
//		  sql.append("   and tfc.fleet_id(+) = tf.fleet_id\n");  
//		  sql.append("   and tff.fleet_id(+) = tf.fleet_id\n");  
//		  sql.append("   and tr.region_code(+) = tf.region\n");  
//		  sql.append("   and tc.company_id(+) = tf.dlr_company_id\n");  
//		  sql.append("   and tc.province_id = tr2.region_code\n");  
//		  sql.append(" group by tf.series_id,\n");  
//		  sql.append("          vod.root_org_name,\n");  
//		  sql.append("          tr2.region_name,\n");  
//		  sql.append("          tf.submit_date,\n");  
//		  sql.append("          td.dealer_name,\n");  
//		  sql.append("          tf.fleet_name,\n");  
//		  sql.append("          tf.fleet_type,\n");  
//		  sql.append("          tf.main_business,\n");  
//		  sql.append("          tf.zip_code,\n");  
//		  sql.append("          tr.region_name,\n");  
//		  sql.append("          tf.address,\n");  
//		  sql.append("          tf.main_linkman,\n");  
//		  sql.append("          tf.main_job,\n");  
//		  sql.append("          tf.main_phone,\n");  
//		  sql.append("          tvmg.group_name,\n");  
//		  sql.append("          tf.series_count,\n");  
//		  sql.append("          tff.follow_remark,\n");  
//		  sql.append("          tf.status,\n");  
//		  sql.append("          tf.req_remark,\n");  
//		  sql.append("          tu.name,\n");  
//		  sql.append("          tf.audit_date,\n");  
//		  sql.append("          tff.follow_date,\n");  
//		  sql.append("          tff.follow_remark,\n");  
//		  sql.append("          tfc.check_date,\n");  
//		  sql.append("          tfc.start_date,\n");  
//		  sql.append("          tfc.end_date,\n");  
//		  sql.append("          tvmg.group_name,\n");  
//		  sql.append("          tvmg2.group_name,\n");  
//		  sql.append("          tfin.intent_count,\n");  
//		  sql.append("          tf.series_count\n");  
//		  sql.append(" order by vod.root_org_name, tr2.region_name\n");
//		  return  dao.pageQuery(sql.toString(), null, dao.getFunName());
//	  }
	
	
	public List<Map<String,Object>> getFleetSyntheseReportPartOne(Map<String, String> map) {
		//新增报备起止确认时间
		String startTime = map.get("startTime");
		String endTime = map.get("endTime");
		//新增省份
		String region = map.get("region");
		//新增集团客户名称			
		String fleetName = map.get("fleetName");
		//新增经销商
		String dealerCode = map.get("dealerCode");
		//新增区域(分销中心)
		String orgCode = map.get("orgCode");
		//大区
		String orgId = map.get("orgId");
		StringBuffer sql = new StringBuffer();
		sql.append("select distinct /*+ all_rows */\n");
		sql.append("       tfin.contract_id,\n");
		sql.append("       tf.series_id,   --报备id\n");  
		sql.append("       vod.root_org_name, --大区\n");  
		sql.append("       tr2.region_name as province, --省份\n");  
		sql.append("       tf.submit_date, --提报日期\n");  
		sql.append("       td.dealer_name, --提报单位\n");  
		sql.append("       tf.fleet_name, --客户名称\n");  
		sql.append("       tc_code.code_desc as fleet_type, --客户类型\n");  
		sql.append("       tf.main_business, --主营业务\n");  
		sql.append("       tf.zip_code, --邮编\n");  
		sql.append("       tr.region_name as region, --区域\n");  
		sql.append("       tf.address, --详细地址\n");  
		sql.append("       tf.main_linkman, --主联系人\n");  
		sql.append("       tf.main_job, --职务\n");  
		sql.append("       tf.main_phone, --电话\n");  
		sql.append("       tvmg2.group_name as cars, --车系\n");  
		sql.append("       tf.series_count, --报备数量\n");  
		sql.append("       tff.follow_remark, --备注\n");  
		sql.append("       tf.status, --确认状态\n");  
		sql.append("       tf.req_remark, --确认说明\n");  
		sql.append("       tu.name, --确认人\n");  
		sql.append("       tf.audit_date, --确认时间\n");  
		sql.append("       tff.follow_date, ---跟进时间\n");  
		sql.append("       tff.follow_remark, ---跟进内容\n");  
		sql.append("       tfin.check_date, ----签约时间\n");  
		sql.append("       tfin.start_date, ----有效期起\n");  
		sql.append("       tfin.end_date, ----有效期止\n");  
		/*sql.append("       tvmg.group_name, ----签约车系\n"); */ 
		sql.append("       tfin.intent_count ----合同数量\n");  
		sql.append("from tm_fleet tf,\n");
		/*sql.append("       TT_FLEET_CONTRACT      tfc,\n");  */
		sql.append("       tc_user tu,\n");  
		sql.append("       tm_company  tc,\n");  
		sql.append("       tm_vhcl_material_group tvmg2,\n");  
		sql.append("  /*     TM_VHCL_MATERIAL_GROUP tvmg,*/\n");  
		sql.append("       tm_dealer td,\n");  
		sql.append("       tc_code,\n");  
		sql.append("       tm_region tr,\n");  
		sql.append("       tm_region tr2,\n");  
		sql.append("(select distinct(tff3.fleet_id), tff3.follow_date, tff3.follow_remark, tff3.rowid from tm_fleet_follow tff3,\n");
		sql.append("              (select tff2.fleet_id, max(rowid) as follow_date from tm_fleet_follow tff2\n");  
		sql.append("              where tff2.fleet_id = tff2.fleet_id\n");  
		sql.append("              group by tff2.fleet_id) tmp\n");  
		sql.append("       where tff3.fleet_id = tmp.fleet_id\n");  
		sql.append("         and tmp.follow_date = tff3.rowid) tff,\n");
		sql.append("        (select tfc.fleet_id, tfc.contract_id, tfc.check_date, tfc.start_date, tfc.end_date, nvl(sum(tfin2.intent_count),0) as intent_count from\n");  
		sql.append("        TT_FLEET_CONTRACT      tfc,\n");  
		sql.append("        TT_FLEET_INTENT_NEW    tfin2\n");  
		sql.append("        where tfin2.contract_id(+) = tfc.contract_id\n");
		sql.append("		and tfc.status = 13521003\n");
		sql.append("        group by tfc.fleet_id,tfc.contract_id, tfc.check_date, tfc.start_date, tfc.end_date) tfin,\n");  
		sql.append("       VW_ORG_DEALER vod\n");  
		sql.append("       where tf.dlr_company_id = td.company_id\n");  
		sql.append("       and vod.dealer_id in td.dealer_id\n");  
		sql.append("       and tf.series_id = tvmg2.group_id(+)\n");  
		sql.append("       and tc.province_id = tr2.region_code\n");  
		sql.append("       and tf.dlr_company_id = tc.company_id\n");  
		sql.append("       and tf.region = tr.region_code\n");  
		sql.append("       and tf.fleet_id = tff.fleet_id(+)\n");  
		sql.append("       and tf.audit_user_id = tu.user_id\n");  
		sql.append("       /*and tfin.series_id = tvmg.group_id(+)*/\n");  
		/*sql.append("       and tfc.contract_id = tfin.contract_id\n"); */ 
		sql.append("       and tf.fleet_id = tfin.fleet_id(+)\n");  
		sql.append("  and tc_code.code_id(+) = tf.fleet_type\n");
		sql.append("	   and tf.fleet_id >= 0\n");
		//sql.append("	   and tfc.contract_id >= 0\n");
		sql.append("	   and tf.status = 11021003\n");
		if(!CommonUtils.isNullString(endTime)) {
			if(CommonUtils.isNullString(startTime)) {
				sql.append("	and tf.audit_date <= to_date('");
				sql.append(endTime);
				sql.append("', 'yyyy-MM-dd HH24:MI:SS')\n");
			} else {
				sql.append("	and tf.audit_date >= to_date('");
				sql.append(startTime);
				sql.append("', 'yyyy-MM-dd HH24:MI:SS')\n");
				sql.append("	and tf.audit_date <= to_date('");
				sql.append(endTime);
				sql.append("', 'yyyy-MM-dd HH24:MI:SS')\n");
			}
		}
		if(!CommonUtils.isNullString(dealerCode)) {
			sql.append("and VOD.DEALER_CODE in (");
			String[] arr = dealerCode.split(",");
			StringBuffer dc = new StringBuffer();
			for(int i=0; i<arr.length; i++) {
				dc.append("'").append(arr[i]).append("'");
				if(i != arr.length-1) {
					dc.append(",");
				}
			}
			sql.append(dc.toString());
			sql.append(")\n");
		}
		if(!CommonUtils.isNullString(orgCode)) {
			sql.append("and VOD.ROOT_ORG_CODE = '");
			/*String[] arr = orgCode.split(",");
			StringBuffer roc = new StringBuffer();
			for(int i=0; i<arr.length; i++) {
				roc.append("'").append(arr[i]).append("'");
			}
			sql.append(roc.toString());*/
			sql.append(orgCode);
			sql.append("'\n");
		}
		if(!CommonUtils.isNullString(fleetName)) {
			sql.append("and tf.fleet_name like '%");
			sql.append(fleetName);
			sql.append("%'\n");
		}
		if(!CommonUtils.isNullString(region)) {
			sql.append("and tr2.region_code = '");
			sql.append(region);
			sql.append("'\n");
		}
		if(!CommonUtils.isNullString(orgId)) {
			sql.append("and vod.Root_Org_Id = '");
			sql.append(orgId);
			sql.append("'\n");
		}
		sql.append("       order by vod.root_org_name, tr2.region_name\n");
		return  dao.pageQuery(sql.toString(), null, dao.getFunName());
	}
	
	public List<Map<String,Object>> getFleetSyntheseReportPartTwo(Map<String, String> map) {
		StringBuffer sql = new StringBuffer();
		sql.append("select distinct /*+ all_rows */ tfc.contract_id,\n");
		sql.append("                count(tdas.order_id) as actCount ------实销审核数量\n");  
		sql.append("  from TM_FLEET tf, TT_FLEET_CONTRACT tfc, TT_DEALER_ACTUAL_SALES tdas\n");  
		sql.append(" where tdas.contract_id = tfc.contract_id\n");  
		sql.append("   and tfc.fleet_id = tf.fleet_id\n");  
		sql.append(" group by tfc.contract_id\n");
		return  dao.pageQuery(sql.toString(), null, dao.getFunName());
	}
}
