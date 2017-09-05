package com.infodms.dms.dao.customerRelationships;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.util.StringUtil;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;



@SuppressWarnings("unchecked")
public class ComplaintTypeReportformsDao extends BaseDao{

	private static final ComplaintTypeReportformsDao dao = new ComplaintTypeReportformsDao();
	
	public static final ComplaintTypeReportformsDao getInstance() {
		return dao;
	}
	
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		return null;
	}
	
	//按来源部门统计
	public PageResult<Map<String,Object>> queryComplaintTypeReportformsByDealOrg(String dealName,String dateStart,String dateEnd,String tmorg,String smalltmorg,int tatal, String model,int pageSize,int curPage){
		String sql = returnComplaintTypeReportformsByDealOrgSql(dealName, dateStart, dateEnd, tmorg, smalltmorg, tatal,model);

		return (PageResult<Map<String, Object>>)this.pageQuery(sql,
				null,
				this.getFunName(),
				pageSize,
				curPage);
	}
	
	//按来源部门统计 List
	public List<Map<String,Object>> queryComplaintTypeReportformsByDealOrgList(String dealName,String dateStart,String dateEnd,String tmorg,String smalltmorg,int tatal, String model){
		String sql = returnComplaintTypeReportformsByDealOrgSql(dealName, dateStart, dateEnd, tmorg, smalltmorg, tatal,model);

		return (List<Map<String, Object>>)this.pageQuery(sql,
				null,
				this.getFunName());
	}
	
	//来源部门统计SQL
	private String returnComplaintTypeReportformsByDealOrgSql(String dealName,String dateStart,String dateEnd,String tmorg,String smalltmorg,int tatal,String model){
		StringBuffer sbSql = new StringBuffer();

		sbSql.append("select b.org_name TYPENAME,\r\n");
		sbSql.append("       nvl(count(*),0) COUNTDESC,\r\n");
		sbSql.append("       nvl(decode(instr(to_char(round(count(*) / "+tatal+" * 100, 4)),'.'),1,0||to_char(round(count(*) / "+tatal+" * 100, 4)),to_char(round(count(*) / "+tatal+" * 100, 4))),0) COUNTRATE\r\n");
		sbSql.append("  from TT_CRM_COMPLAINT a\r\n");
		sbSql.append("  left join tm_org_custom b\r\n");
		sbSql.append("    on a.cp_source_custom = b.org_id\r\n");
		sbSql.append("  left join tc_user f\r\n");
		sbSql.append("    on a.cp_acc_user = f.user_id"); 

		sbSql.append("   where a.CP_BIZ_TYPE = "+Constant.TYPE_COMPLAIN+"\r\n");
		sbSql = appendSpecilWhere(sbSql, tmorg, smalltmorg, dealName, dateStart, dateEnd,model);
		sbSql.append(" group by b.org_name \r\n");
		sbSql.append(" union all\r\n");
		sbSql.append(" select '合计', "+tatal+", '' from dual"); 
		
		return sbSql.toString();
	}
	
	//按车种统计
	public PageResult<Map<String,Object>> queryComplaintTypeReportformsBySeries(String dealName,String dateStart,String dateEnd,String tmorg,String smalltmorg,int tatal, String model,int pageSize,int curPage){
		String sql = returnComplaintTypeReportformsBySeriesSql(dealName, dateStart, dateEnd, tmorg, smalltmorg, tatal, model); 

		return (PageResult<Map<String, Object>>)this.pageQuery(sql,
				null,
				this.getFunName(),
				pageSize,
				curPage);
	}
	
	//按车种统计List
	public List<Map<String,Object>> queryComplaintTypeReportformsBySeriesList(String dealName,String dateStart,String dateEnd,String tmorg,String smalltmorg,int tatal, String model){
		String sql = returnComplaintTypeReportformsBySeriesSql(dealName, dateStart, dateEnd, tmorg, smalltmorg, tatal, model);

		return (List<Map<String, Object>>)this.pageQuery(sql,
				null,
				this.getFunName());
	}
	
	//车种统计SQL
	private String returnComplaintTypeReportformsBySeriesSql(String dealName,String dateStart,String dateEnd,String tmorg,String smalltmorg,int tatal, String model){
		StringBuffer sbSql = new StringBuffer();

		sbSql.append("select b.group_name TYPENAME,\r\n");
		sbSql.append("       nvl(count(*),0) COUNTDESC,\r\n");
		sbSql.append("       nvl(decode(instr(to_char(round(count(*) / "+tatal+" * 100, 4)),'.'),1,0||to_char(round(count(*) / "+tatal+" * 100, 4)),to_char(round(count(*) / "+tatal+" * 100, 4))),0) COUNTRATE\r\n");
		sbSql.append("  from TT_CRM_COMPLAINT a\r\n");
		sbSql.append("  left join tm_vhcl_material_group b\r\n");
		sbSql.append("    on a.cp_series_id = b.group_id\r\n");
		sbSql.append("  left join tc_user f\r\n");
		sbSql.append("    on a.cp_acc_user = f.user_id"); 

		sbSql.append("   where a.CP_BIZ_TYPE = "+Constant.TYPE_COMPLAIN+"\r\n");
		sbSql = appendSpecilWhere(sbSql, tmorg, smalltmorg, dealName, dateStart, dateEnd, model);
		sbSql.append(" group by b.group_name\r\n");
		sbSql.append(" union all\r\n");
		sbSql.append(" select '合计', "+tatal+", '' from dual"); 
		
		return sbSql.toString();
	}
	
	//按行驶里程统计
	public PageResult<Map<String,Object>> queryComplaintTypeReportformsByMileageRange(String dealName,String dateStart,String dateEnd,String tmorg,String smalltmorg,int tatal, String model,int pageSize,int curPage){
		String sql = returnComplaintTypeReportformsByMileageRangeSql(dealName, dateStart, dateEnd, tmorg, smalltmorg, tatal, model);

		return (PageResult<Map<String, Object>>)this.pageQuery(sql.toString(),
				null,
				this.getFunName(),
				pageSize,
				curPage);
	}
	
	
	//按里程范围统计 List
	public List<Map<String,Object>> queryComplaintTypeReportformsByMileageRangeList(String dealName,String dateStart,String dateEnd,String tmorg,String smalltmorg,int tatal, String model){
		String sql = returnComplaintTypeReportformsByMileageRangeSql(dealName, dateStart, dateEnd, tmorg, smalltmorg, tatal, model);
		
		return (List<Map<String, Object>>)this.pageQuery(sql,
				null,
				this.getFunName());
	}
	
	//按里程范围统计SQL
	private String returnComplaintTypeReportformsByMileageRangeSql(String dealName,String dateStart,String dateEnd,String tmorg,String smalltmorg,int tatal, String model){
		StringBuffer sbSql = new StringBuffer();

		sbSql.append("select nvl(b.code_desc,'无效里程范围') TYPENAME,\r\n");
		sbSql.append("       nvl(count(*),0) COUNTDESC,\r\n");
		sbSql.append("       nvl(decode(instr(to_char(round(count(*) / "+tatal+" * 100, 4)),'.'),1,0||to_char(round(count(*) / "+tatal+" * 100, 4)),to_char(round(count(*) / "+tatal+" * 100, 4))),0) COUNTRATE\r\n");
		sbSql.append("  from TT_CRM_COMPLAINT a\r\n");
		sbSql.append("  left join tc_code b\r\n");
		sbSql.append("    on a.cp_mileage_range = b.code_id\r\n");
		sbSql.append("  left join tc_user f\r\n");
		sbSql.append("    on a.cp_acc_user = f.user_id"); 

		sbSql.append("   where a.CP_BIZ_TYPE = "+Constant.TYPE_COMPLAIN+"\r\n");
		sbSql = appendSpecilWhere(sbSql, tmorg, smalltmorg, dealName, dateStart, dateEnd, model);
		sbSql.append(" group by b.code_desc\r\n");
		sbSql.append(" union all\r\n");
		sbSql.append(" select '合计', "+tatal+", '' from dual"); 
		
		return sbSql.toString();
	}
	
	//按报怨类型统计
	public PageResult<Map<String,Object>> queryComplaintTypeReportformsByBizContent(String dealName,String dateStart,String dateEnd,String tmorg,String smalltmorg,int tatal, String model,int pageSize,int curPage){
		String sql = returnComplaintTypeReportformsByBizContentSql(dealName, dateStart, dateEnd, tmorg, smalltmorg, tatal, model);
		
		return (PageResult<Map<String, Object>>)this.pageQuery(sql,
				null,
				this.getFunName(),
				pageSize,
				curPage);
	}
	
	//按报怨类型统计 List
	public List<Map<String,Object>> queryComplaintTypeReportformsByBizContentList(String dealName,String dateStart,String dateEnd,String tmorg,String smalltmorg,int tatal, String model){
		String sql = returnComplaintTypeReportformsByBizContentSql(dealName, dateStart, dateEnd, tmorg, smalltmorg, tatal, model);

		return (List<Map<String, Object>>)this.pageQuery(sql,
				null,
				this.getFunName());
	}
	
	//按报怨类型统计SQL
	private String returnComplaintTypeReportformsByBizContentSql(String dealName,String dateStart,String dateEnd,String tmorg,String smalltmorg,int tatal, String model){
		StringBuffer sbSql = new StringBuffer();

		sbSql.append("select b.code_desc TYPENAME,\r\n");
		sbSql.append("       nvl(count(*),0) COUNTDESC,\r\n");
		sbSql.append("       nvl(decode(instr(to_char(round(count(*) / "+tatal+" * 100, 4)),'.'),1,0||to_char(round(count(*) / "+tatal+" * 100, 4)),to_char(round(count(*) / "+tatal+" * 100, 4))),0) COUNTRATE\r\n");
		sbSql.append("  from TT_CRM_COMPLAINT a\r\n");
		sbSql.append("  left join tc_code b\r\n");
		sbSql.append("    on a.cp_biz_content = b.code_id\r\n");
		sbSql.append("  left join tc_user f\r\n");
		sbSql.append("    on a.cp_acc_user = f.user_id"); 

		sbSql.append("   where a.CP_BIZ_TYPE = "+Constant.TYPE_COMPLAIN+"\r\n");
		sbSql = appendSpecilWhere(sbSql, tmorg, smalltmorg, dealName, dateStart, dateEnd, model);
		sbSql.append(" group by b.code_desc\r\n");
		sbSql.append(" union all\r\n");
		sbSql.append(" select '合计', "+tatal+", '' from dual"); 
		
		return sbSql.toString();
	}
	
	//按报怨级别统计
	public PageResult<Map<String,Object>> queryComplaintTypeReportformsByLevel(String dealName,String dateStart,String dateEnd,String tmorg,String smalltmorg,int tatal, String model,int pageSize,int curPage){
		String sql = returnComplaintTypeReportformsByLevelSql(dealName, dateStart, dateEnd, tmorg, smalltmorg, tatal, model);

		return (PageResult<Map<String, Object>>)this.pageQuery(sql,
				null,
				this.getFunName(),
				pageSize,
				curPage);
	}
	
	//按报怨级别统计 List
	public List<Map<String,Object>> queryComplaintTypeReportformsByLevelList(String dealName,String dateStart,String dateEnd,String tmorg,String smalltmorg,int tatal, String model){
		String sql = returnComplaintTypeReportformsByLevelSql(dealName, dateStart, dateEnd, tmorg, smalltmorg, tatal, model);

		return (List<Map<String, Object>>)this.pageQuery(sql,
				null,
				this.getFunName());
	}
	
	//按报怨级别统计 SQL
	private String returnComplaintTypeReportformsByLevelSql(String dealName,String dateStart,String dateEnd,String tmorg,String smalltmorg,int tatal, String model){
		StringBuffer sbSql = new StringBuffer();

		sbSql.append("select b.code_desc TYPENAME,\r\n");
		sbSql.append("       nvl(count(*),0) COUNTDESC,\r\n");
		sbSql.append("       nvl(decode(instr(to_char(round(count(*) / "+tatal+" * 100, 4)),'.'),1,0||to_char(round(count(*) / "+tatal+" * 100, 4)),to_char(round(count(*) / "+tatal+" * 100, 4))),0) COUNTRATE\r\n");
		sbSql.append("  from TT_CRM_COMPLAINT a\r\n");
		sbSql.append("  left join tc_code b\r\n");
		sbSql.append("    on a.cp_level = b.code_id\r\n");
		sbSql.append("  left join tc_user f\r\n");
		sbSql.append("    on a.cp_acc_user = f.user_id"); 

		sbSql.append("   where a.CP_BIZ_TYPE = "+Constant.TYPE_COMPLAIN+"\r\n");
		sbSql = appendSpecilWhere(sbSql, tmorg, smalltmorg, dealName, dateStart, dateEnd, model);
		sbSql.append(" group by b.code_desc\r\n");
		sbSql.append(" union all\r\n");
		sbSql.append(" select '合计', "+tatal+", '' from dual"); 

		return sbSql.toString();
	}
	
	//按故障部件统计
	public PageResult<Map<String,Object>> queryComplaintTypeReportformsByFaultPart(String dealName,String dateStart,String dateEnd,String tmorg,String smalltmorg,int tatal, String model,int pageSize,int curPage){
		String sql = returnComplaintTypeReportformsByFaultPartSql(dealName, dateStart, dateEnd, tmorg, smalltmorg, tatal, model);

		return (PageResult<Map<String, Object>>)this.pageQuery(sql,
				null,
				this.getFunName(),
				pageSize,
				curPage);
	}
	
	
	//按故障部件统计 List
	public List<Map<String,Object>> queryComplaintTypeReportformsByFaultPartList(String dealName,String dateStart,String dateEnd,String tmorg,String smalltmorg,int tatal, String model){
		String sql = returnComplaintTypeReportformsByFaultPartSql(dealName, dateStart, dateEnd, tmorg, smalltmorg, tatal, model);

		return (List<Map<String, Object>>)this.pageQuery(sql,
				null,
				this.getFunName());
	}
	
	//按故障部件统计 SQL
	private String returnComplaintTypeReportformsByFaultPartSql(String dealName,String dateStart,String dateEnd,String tmorg,String smalltmorg,int tatal, String model){
		StringBuffer sbSql = new StringBuffer();

		sbSql.append("select nvl(b.code_desc,'历史投诉') TYPENAME,\r\n");
		sbSql.append("       nvl(count(*),0) COUNTDESC,\r\n");
		sbSql.append("       nvl(decode(instr(to_char(round(count(*) / "+tatal+" * 100, 4)),'.'),1,0||to_char(round(count(*) / "+tatal+" * 100, 4)),to_char(round(count(*) / "+tatal+" * 100, 4))),0) COUNTRATE\r\n");
		sbSql.append("  from TT_CRM_COMPLAINT a\r\n");
		sbSql.append("  left join tc_code b\r\n");
		sbSql.append("    on a.fault_part = b.code_id\r\n");
		sbSql.append("  left join tc_user f\r\n");
		sbSql.append("    on a.cp_acc_user = f.user_id"); 

		sbSql.append("   where a.CP_BIZ_TYPE = "+Constant.TYPE_COMPLAIN+"\r\n");
		sbSql = appendSpecilWhere(sbSql, tmorg, smalltmorg, dealName, dateStart, dateEnd, model);
		sbSql.append(" group by b.code_desc\r\n");
		sbSql.append(" union all\r\n");
		sbSql.append(" select '合计', "+tatal+", '' from dual"); 

		return sbSql.toString();
	}
	
	//按大区统计
	public PageResult<Map<String,Object>> queryComplaintTypeReportformsByTmOrg(String dealName,String dateStart,String dateEnd,String tmorg,String smalltmorg,int tatal, String model,int pageSize,int curPage){
		String sql = returnComplaintTypeReportformsByTmOrgSql(dealName, dateStart, dateEnd, tmorg, smalltmorg, tatal, model);

		return (PageResult<Map<String, Object>>)this.pageQuery(sql,
				null,
				this.getFunName(),
				pageSize,
				curPage);
	}
	
	
	//按大区统计 List
	public List<Map<String,Object>> queryComplaintTypeReportformsByTmOrgList(String dealName,String dateStart,String dateEnd,String tmorg,String smalltmorg,int tatal, String model){
		String sql = returnComplaintTypeReportformsByTmOrgSql(dealName, dateStart, dateEnd, tmorg, smalltmorg, tatal, model);

		return (List<Map<String, Object>>)this.pageQuery(sql,
				null,
				this.getFunName());
	}
	
	private String returnComplaintTypeReportformsByTmOrgSql(String dealName,String dateStart,String dateEnd,String tmorg,String smalltmorg,int tatal, String model){
		StringBuffer sbSql = new StringBuffer();

		sbSql.append("select nvl(b.org_name,l.org_name) TYPENAME,\r\n");
		sbSql.append("       nvl(count(*),0) COUNTDESC,\r\n");
		sbSql.append("       nvl(decode(instr(to_char(round(count(*) / "+tatal+" * 100, 4)),'.'),1,0||to_char(round(count(*) / "+tatal+" * 100, 4)),to_char(round(count(*) / "+tatal+" * 100, 4))),0) COUNTRATE\r\n");
		sbSql.append("  from TT_CRM_COMPLAINT a\r\n");
		sbSql.append("  left join tm_org b \r\n");
		sbSql.append("    on b.org_id = a.CP_DEAL_ORG \r\n");
		sbSql.append("  left join tm_org_custom l\r\n");
		sbSql.append("    on l.org_id = a.CP_DEAL_ORG\r\n");
		sbSql.append("  left join tc_user f\r\n");
		sbSql.append("    on a.cp_acc_user = f.user_id"); 

		sbSql.append("   where a.CP_BIZ_TYPE = "+Constant.TYPE_COMPLAIN+"\r\n");
		sbSql = appendSpecilWhere(sbSql, tmorg, smalltmorg, dealName, dateStart, dateEnd, model);
		sbSql.append(" group by nvl(b.org_name,l.org_name)\r\n");
		sbSql.append(" union all\r\n");
		sbSql.append(" select '合计', "+tatal+", '' from dual"); 
		
		return sbSql.toString();
	}
	
	
	//按省分统计
	public PageResult<Map<String,Object>> queryComplaintTypeReportformsBySmalltmorg(String dealName,String dateStart,String dateEnd,String tmorg,String smalltmorg,int tatal, String model,int pageSize,int curPage){
		String sql = returnComplaintTypeReportformsBySmalltmorgSql(dealName, dateStart, dateEnd, tmorg, smalltmorg, tatal, model);

		return (PageResult<Map<String, Object>>)this.pageQuery(sql,
				null,
				this.getFunName(),
				pageSize,
				curPage);
	}
	
	//按省分统计 List
	public List<Map<String,Object>> queryComplaintTypeReportformsBySmalltmorgList(String dealName,String dateStart,String dateEnd,String tmorg,String smalltmorg,int tatal, String model){
		String sql = returnComplaintTypeReportformsBySmalltmorgSql(dealName, dateStart, dateEnd, tmorg, smalltmorg, tatal, model);

		return (List<Map<String, Object>>)this.pageQuery(sql,
				null,
				this.getFunName());
	}
	//按省分统计SQL
	private String returnComplaintTypeReportformsBySmalltmorgSql(String dealName,String dateStart,String dateEnd,String tmorg,String smalltmorg,int tatal, String model){
		StringBuffer sbSql = new StringBuffer();

		sbSql.append("select nvl(b.REGION_NAME,'历史投诉') TYPENAME,\r\n");
		sbSql.append("       nvl(count(*),0) COUNTDESC,\r\n");
		sbSql.append("       nvl(decode(instr(to_char(round(count(*) / "+tatal+" * 100, 4)),'.'),1,0||to_char(round(count(*) / "+tatal+" * 100, 4)),to_char(round(count(*) / "+tatal+" * 100, 4))),0) COUNTRATE\r\n");
		sbSql.append("  from TT_CRM_COMPLAINT a\r\n");
		sbSql.append("  left join tm_region b\r\n");
		sbSql.append("    on b.REGION_CODE = a.CP_PROVINCE_ID\r\n");
		sbSql.append("  left join tc_user f\r\n");
		sbSql.append("    on a.cp_acc_user = f.user_id"); 

		sbSql.append("   where a.CP_BIZ_TYPE = "+Constant.TYPE_COMPLAIN+"\r\n");
		sbSql = appendSpecilWhere(sbSql, tmorg, smalltmorg, dealName, dateStart, dateEnd, model);
		sbSql.append(" group by b.REGION_NAME\r\n");
		sbSql.append(" union all\r\n");
		sbSql.append(" select '合计', "+tatal+", '' from dual");
		
		return sbSql.toString();
	}
	
	//购买期限统计
	public PageResult<Map<String,Object>> queryComplaintTypeReportformsByBuyDateRange(String dealName,String dateStart,String dateEnd,String tmorg,String smalltmorg,int tatal, String model,int pageSize,int curPage){
		String sql = returnComplaintTypeReportformsByBuyDateRangeSql(dealName, dateStart, dateEnd, tmorg, smalltmorg, tatal, model);
		
		return (PageResult<Map<String, Object>>)this.pageQuery(sql,
				null,
				this.getFunName(),
				pageSize,
				curPage);
	}
	
	
	//购买期限统计 List
	public List<Map<String,Object>> queryComplaintTypeReportformsByBuyDateRangeList(String dealName,String dateStart,String dateEnd,String tmorg,String smalltmorg,int tatal, String model){
		String sql = returnComplaintTypeReportformsByBuyDateRangeSql(dealName, dateStart, dateEnd, tmorg, smalltmorg, tatal, model);

		return (List<Map<String, Object>>)this.pageQuery(sql,
				null,
				this.getFunName());
	}
	////购买期限统计SQL
	private String returnComplaintTypeReportformsByBuyDateRangeSql(String dealName,String dateStart,String dateEnd,String tmorg,String smalltmorg,int tatal, String model){
		StringBuffer sbSql = new StringBuffer();

		sbSql.append("select nvl(PKG_SALE_SERVICE.F_BUYDATE_RANGE(a.cp_buy_date),'未销售车辆') TYPENAME,\r\n");
		sbSql.append("       nvl(count(*),0) COUNTDESC,\r\n");
		sbSql.append("       nvl(decode(instr(to_char(round(count(*) / "+tatal+" * 100, 4)),'.'),1,0||to_char(round(count(*) / "+tatal+" * 100, 4)),to_char(round(count(*) / "+tatal+" * 100, 4))),0) COUNTRATE\r\n");
		sbSql.append("  from TT_CRM_COMPLAINT a\r\n");
		sbSql.append("  left join tc_user f\r\n");
		sbSql.append("    on a.cp_acc_user = f.user_id"); 

		sbSql.append("   where a.CP_BIZ_TYPE = "+Constant.TYPE_COMPLAIN+"\r\n");
		sbSql = appendSpecilWhere(sbSql, tmorg, smalltmorg, dealName, dateStart, dateEnd, model);
		sbSql.append(" group by PKG_SALE_SERVICE.F_BUYDATE_RANGE(a.cp_buy_date)\r\n");
		sbSql.append(" union all\r\n");
		sbSql.append(" select '合计', "+tatal+", '' from dual"); 

		return sbSql.toString();
	}
	
	
	//按处理时长统计
	public PageResult<Map<String,Object>> queryComplaintTypeReportformsByDealRange(String dealName,String dateStart,String dateEnd,String tmorg,String smalltmorg,int tatal, String model,int pageSize,int curPage){
		String sql = returnComplaintTypeReportformsByDealRangeSql(dealName, dateStart, dateEnd, tmorg, smalltmorg, tatal, model);

		return (PageResult<Map<String, Object>>)this.pageQuery(sql,
				null,
				this.getFunName(),
				pageSize,
				curPage);
	}
	
	
	//按处理时长统计 List
	public List<Map<String,Object>> queryComplaintTypeReportformsByDealRangeList(String dealName,String dateStart,String dateEnd,String tmorg,String smalltmorg,int tatal, String model){
		String sql = returnComplaintTypeReportformsByDealRangeSql(dealName, dateStart, dateEnd, tmorg, smalltmorg, tatal, model);

		return (List<Map<String, Object>>)this.pageQuery(sql,
				null,
				this.getFunName());
	}
	
	private String returnComplaintTypeReportformsByDealRangeSql(String dealName,String dateStart,String dateEnd,String tmorg,String smalltmorg,int tatal, String model){
		StringBuffer sbSql = new StringBuffer();

		sbSql.append("select nvl(PKG_SALE_SERVICE.F_DEAL_RANGE(a.cp_turn_date,X.BACK_DATE),'未处理或处理中投诉') TYPENAME,\r\n");
		sbSql.append("       nvl(count(*),0) COUNTDESC,\r\n");
		sbSql.append("       nvl(decode(instr(to_char(round(count(*) / "+tatal+" * 100, 4)),'.'),1,0||to_char(round(count(*) / "+tatal+" * 100, 4)),to_char(round(count(*) / "+tatal+" * 100, 4))),0) COUNTRATE\r\n");
		sbSql.append("  from TT_CRM_COMPLAINT a\r\n");
		sbSql.append("  left join tc_user f\r\n");
		sbSql.append("    on a.cp_acc_user = f.user_id"); 
		
		sbSql.append("    LEFT JOIN (SELECT TO_CHAR(MAX(DR.CD_DATE), 'yyyy-MM-dd hh24:mi:ss') LASTDEALDATE, DR.CP_ID, MAX(DECODE(DR.CP_STATUS,"+Constant.COMPLAINT_STATUS_DOING_ALREADY+",DR.CD_DATE,NULL)) BACK_DATE\n"); 
		sbSql.append("      FROM TT_CRM_COMPLAINT_DEAL_RECORD DR GROUP BY DR.CP_ID) X ON X.CP_ID = a.CP_ID");
		
		sbSql.append("   where a.CP_BIZ_TYPE = "+Constant.TYPE_COMPLAIN+"\r\n");
		sbSql = appendSpecilWhere(sbSql, tmorg, smalltmorg, dealName, dateStart, dateEnd, model);
		sbSql.append(" group by PKG_SALE_SERVICE.F_DEAL_RANGE(a.cp_turn_date,X.BACK_DATE)\r\n");
		sbSql.append(" union all\r\n");
		sbSql.append(" select '合计', "+tatal+", '' from dual"); 

		return sbSql.toString();
	}
	
	private StringBuffer appendSpecilWhere(StringBuffer sbSql,String tmorg,String smalltmorg,String dealName,
			String dateStart,String dateEnd, String model){

		if(StringUtil.notNull(dealName)){
			sbSql.append(" and f.name like '%"+dealName+"%' \r\n");
		}

		if(StringUtil.notNull(dateStart)){
			sbSql.append(" and a.cp_acc_date >= to_date('"+dateStart+"','yyyy-MM-dd')  \r\n");
		}
		if(StringUtil.notNull(dateEnd)){
			sbSql.append(" and a.cp_acc_date <= to_date('"+dateEnd+" 23:59:59','yyyy-MM-dd hh24:mi:ss')\r\n");
		}
		if(StringUtil.notNull(tmorg)){
			sbSql.append(" and a.CP_DEAL_ORG = "+tmorg+"\r\n");
		}
		if(StringUtil.notNull(model)){
			sbSql.append(" and a.CP_MODEL_ID = "+model+"\r\n");
		}
		if(StringUtil.notNull(smalltmorg)){
			sbSql.append(" and a.CP_PROVINCE_ID = "+smalltmorg+"\r\n");
		}

		return sbSql;
	}
	
	
	//查询投诉总量
	public int getTotalComplaintTypeReportforms(String dealName,String dateStart,String dateEnd,String tmorg,String smalltmorg,String model){
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("select nvl(count(*),0) VAL\r\n");
		sbSql.append("  from TT_CRM_COMPLAINT a\r\n");
		sbSql.append("  left join tc_user f\r\n");
		sbSql.append("    on a.cp_acc_user = f.user_id\r\n");

		sbSql.append(" where a.CP_BIZ_TYPE = "+Constant.TYPE_COMPLAIN+" \r\n");
		sbSql = appendSpecilWhere(sbSql, tmorg, smalltmorg, dealName, dateStart, dateEnd, model);
		return Integer.parseInt(this.pageQueryMap(sbSql.toString(),
			null,
			this.getFunName()).get("VAL").toString());
	}
	


}
