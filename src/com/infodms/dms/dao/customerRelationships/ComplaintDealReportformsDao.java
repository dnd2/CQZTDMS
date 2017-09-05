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
public class ComplaintDealReportformsDao extends BaseDao{

	private static final ComplaintDealReportformsDao dao = new ComplaintDealReportformsDao();
	
	public static final ComplaintDealReportformsDao getInstance() {
		return dao;
	}
	
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		return null;
	}

	
	//按大区统计
	public PageResult<Map<String,Object>> queryComplaintDealReportformsByTmOrg(String dealName,String dateStart,String dateEnd,int pageSize,int curPage){
		String typeName = "k.org_name";
		String typeValue = "a.CP_DEAL_ORG";
		String sql  = returnDealSql(typeName, typeValue, dealName, dateStart, dateEnd);
		return (PageResult<Map<String, Object>>)this.pageQuery(sql.toString(),
				null,
				this.getFunName(),
				pageSize,
				curPage);
	}
	
	
	//按大区统计 List
	public List<Map<String,Object>> queryComplaintDealReportformsByTmOrgList(String dealName,String dateStart,String dateEnd){
		String typeName = "k.org_name";
		String typeValue = "a.CP_DEAL_ORG";
		String sql  = returnDealSql(typeName, typeValue, dealName, dateStart, dateEnd);
		return (List<Map<String, Object>>)this.pageQuery(sql.toString(),
				null,
				this.getFunName());
	}
	
	
	
	//按省分统计
	public PageResult<Map<String,Object>> queryComplaintDealReportformsBySmalltmorg(String dealName,String dateStart,String dateEnd,int pageSize,int curPage){
		String typeName = "nvl(j.REGION_NAME,'历史投诉')";
		String typeValue = "a.CP_PROVINCE_ID";
		String sql  = returnDealSql(typeName, typeValue, dealName, dateStart, dateEnd);
		return (PageResult<Map<String, Object>>)this.pageQuery(sql.toString(),
				null,
				this.getFunName(),
				pageSize,
				curPage);
	}
	
	//按省分统计 List
	public List<Map<String,Object>> queryComplaintDealReportformsBySmalltmorgList(String dealName,String dateStart,String dateEnd){
		String typeName = "nvl(j.REGION_NAME,'历史投诉')";
		String typeValue = "a.CP_PROVINCE_ID";
		String sql  = returnDealSql(typeName, typeValue, dealName, dateStart, dateEnd);
		return (List<Map<String, Object>>)this.pageQuery(sql,
				null,
				this.getFunName());
	}	
	
	
	
	//按商家统计
	public PageResult<Map<String,Object>> queryComplaintDealReportformsByObject(String dealName,String dateStart,String dateEnd,int pageSize,int curPage){
		String typeName = "nvl(h.DEALER_NAME,r.org_name)";
		String typeValue = "a.cp_object";
		String sql  = returnDealSql(typeName, typeValue, dealName, dateStart, dateEnd);
		return (PageResult<Map<String, Object>>)this.pageQuery(sql.toString(),
				null,
				this.getFunName(),
				pageSize,
				curPage);
	}
	
	//按商家统计 List
	public List<Map<String,Object>> queryComplaintDealReportformsByObjectList(String dealName,String dateStart,String dateEnd){
		String typeName = "nvl(h.DEALER_NAME,r.org_name)";
		String typeValue = "a.cp_object";
		String sql  = returnDealSql(typeName, typeValue, dealName, dateStart, dateEnd);
		return (List<Map<String, Object>>)this.pageQuery(sql,
				null,
				this.getFunName());
	}	
	
	
	private String returnDealSql(String typeName,String typeValue,String dealName,String dateStart,String dateEnd){
		StringBuffer sql = new StringBuffer();
		
		sql.append("select\r\n" );
		// 艾春 2013.11.23 修改分组对象名称
		sql.append(" decode(grouping("+typeValue+"), 0, NVL(max("+typeName+"),'服务营销处'), null) TYPENAME,\r\n" );
//		sql.append(" max("+typeName+") TYPENAME,\r\n" );
		// 艾春 2013.11.23 修改分组对象名称
		sql.append(" SUM(CASE WHEN 1=1 \r\n" );
		sql = appendDate(sql, dateStart, dateEnd, 0);
		sql.append(" THEN 1 else 0 END) TOTAL,\r\n" );
		sql.append("\r\n" );
		sql.append(" sum(case when a.cp_status in ("+Constant.COMPLAINT_STATUS_ALREADY_CLOSE+", "+Constant.COMPLAINT_STATUS_CLOSE+") \r\n" );
		sql = appendDate(sql, dateStart, dateEnd, 0);
		sql.append(" then 1 else 0 end) CLOSED, \r\n");
		sql.append("\r\n" );
		sql.append(" sum(case when a.cp_status = "+Constant.COMPLAINT_STATUS_ALREADY_CLOSE+"  \r\n" );
		sql = appendDate(sql, dateStart, dateEnd, 0);
		sql.append(" then 1 else 0 end) GOODC, \r\n");
		sql.append("\r\n" );
		sql.append(" sum(case when a.Cp_Is_Once_Sf = '"+Constant.IF_TYPE_YES+"'  and  a.cp_status in ("+Constant.COMPLAINT_STATUS_ALREADY_CLOSE+", "+Constant.COMPLAINT_STATUS_CLOSE+") and a.cp_cl_date is null \r\n" );
		sql = appendDate(sql, dateStart, dateEnd, 0);
		sql.append(" then 1 else 0 end) ONCEGOODC, \r\n");
		sql.append("\r\n" );
		sql.append(" sum(case when a.cp_sf = "+Constant.YAWP+" and a.cp_status = "+Constant.COMPLAINT_STATUS_CLOSE+"  \r\n" );
		sql = appendDate(sql, dateStart, dateEnd, 0);
		sql.append(" then 1 else 0 end) BADC, \r\n");
		sql.append("\r\n" );
		// 张宇 2013.11.24 满意率，一次性满意率，不满意率 算法用 关闭数量 / 满意率，一次性满意率，不满意率
		sql = appendDecodeRate(sql, "SUM(case when a.cp_status = "+Constant.COMPLAINT_STATUS_ALREADY_CLOSE+"   "+appendDate(null, dateStart, dateEnd, 0)+" then 1 else 0 end)",
					"SUM(CASE WHEN 1=1  and   a.cp_status in ("+Constant.COMPLAINT_STATUS_ALREADY_CLOSE+", "+Constant.COMPLAINT_STATUS_CLOSE+")   "+appendDate(null, dateStart, dateEnd, 0)+" THEN 1 else 0 END)");
		
		sql.append(" GOODR,\r\n" );
		sql.append("\r\n" );
		sql = appendDecodeRate(sql, "SUM(case when a.Cp_Is_Once_Sf = "+Constant.IF_TYPE_YES+"  and  a.cp_status in ("+Constant.COMPLAINT_STATUS_ALREADY_CLOSE+", "+Constant.COMPLAINT_STATUS_CLOSE+")  "+appendDate(null, dateStart, dateEnd, 0)+" then 1 else 0 end)",
					"SUM(CASE WHEN 1=1  and a.cp_status in ("+Constant.COMPLAINT_STATUS_ALREADY_CLOSE+", "+Constant.COMPLAINT_STATUS_CLOSE+")  "+appendDate(null, dateStart, dateEnd, 0)+" THEN 1 else 0 END)" );
		
		sql.append(" OGOODR,\r\n" );
		sql.append("\r\n" );
		sql = appendDecodeRate(sql, "SUM(case when a.cp_sf = "+Constant.YAWP+" and a.cp_status = "+Constant.COMPLAINT_STATUS_CLOSE+"   "+appendDate(null, dateStart, dateEnd, 0)+" then 1 else 0 end)",
					"SUM(CASE WHEN 1=1  and a.cp_status in ("+Constant.COMPLAINT_STATUS_ALREADY_CLOSE+", "+Constant.COMPLAINT_STATUS_CLOSE+")  "+appendDate(null, dateStart, dateEnd, 0)+" THEN 1 else 0 END)" );
		
		sql.append(" BADR\r\n" );
		sql.append("\r\n" );
		sql.append("from TT_CRM_COMPLAINT a\r\n" );
		sql.append("left join tc_user f on a.cp_acc_user = f.user_id\r\n" );
		sql.append("left join tm_region j on j.REGION_CODE = a.CP_PROVINCE_ID\r\n" );
		sql.append("left join (SELECT ORG_ID,ORG_NAME FROM TM_ORG  WHERE ORG_LEVEL = 2 UNION SELECT ORG_ID, ORG_NAME FROM TM_ORG_CUSTOM) k on k.org_id = a.CP_DEAL_ORG\r\n" );
		sql.append("  LEFT JOIN TM_DEALER H ON A.CP_OBJECT = H.DEALER_ID\r\n" );
		sql.append("  LEFT JOIN (SELECT T.ORG_ID, T.ORG_NAME FROM TM_ORG T WHERE T.ORG_LEVEL = 2 UNION SELECT ORG_ID, ORG_NAME FROM TM_ORG_CUSTOM) R ON A.CP_OBJECT = R.ORG_ID\r\n" );
		sql.append("LEFT JOIN (SELECT TO_CHAR(MAX(DR.CD_DATE), 'YYYY-MM-DD HH:MI:SS') LASTDEALDATE, DR.CP_ID, MAX(DECODE(DR.CP_STATUS,"+Constant.COMPLAINT_STATUS_DOING_ALREADY+",DR.CD_DATE,NULL)) BACK_DATE\r\n" );
		sql.append("FROM TT_CRM_COMPLAINT_DEAL_RECORD DR GROUP BY DR.CP_ID) X ON X.CP_ID = a.CP_ID\r\n" );
		sql.append("\r\n" );
		sql.append("where A.CP_BIZ_TYPE = "+Constant.TYPE_COMPLAIN+"\r\n" );
		sql= appendSpecilWhere(sql, dealName);
		// 艾春 2013.11.23 修改抱怨处理满意率统计总数限制条件，因为无实际受理抱怨总数
		sql = appendDate(sql, dateStart, dateEnd, 0);
//		sql = appendDate(sql, dateStart, dateEnd, 4);
		// 艾春 2013.11.23 修改抱怨处理满意率统计总数限制条件，因为无实际受理抱怨总数
		sql.append("group by rollup("+typeValue+")");
		
		return sql.toString();
	}
	
	/**
	 * 拼接 除数率 并排除除数为0 sum->除数  totalSum->被除数
	 */
	private StringBuffer appendDecodeRate(StringBuffer sql,String sum,String totalSum){
		sql.append(" case when "+totalSum+" <> 0 then \r\n");
		sql.append(" decode(instr(TO_CHAR(ROUND( " +sum+ "/"+totalSum+" * 100,4)), \r\n");
		sql.append(" '.'),1,0||TO_CHAR(ROUND( " +sum+ "/"+totalSum+" * 100,4)), \r\n");
		sql.append(" TO_CHAR(ROUND(" +sum+ "/"+totalSum+" * 100,4)) ) \r\n");
		sql.append(" else '0' end \r\n");
		return sql;
	}
	
	private StringBuffer appendSpecilWhere(StringBuffer sql,String dealName){
		if(StringUtil.notNull(dealName)){
			sql.append(" and f.name like '%"+dealName+"%' \r\n");
		}
		return sql;
	}
	
	/**
	 * dateType ：0 -> 需核实时间  1 ->实际受理时间  2 -> 移入时间  3 ->移出时间 4 ->总计时间
	 *            5->当月未转交或次月转交   6->当月前跨月数量
	 */
	private StringBuffer appendDate(StringBuffer sql,String dateStart,String dateEnd,int dateType){
		if(sql == null) sql = new StringBuffer();
		if(dateType == 0){
//			sql.append(" and ((a.CP_TURN_DATE < to_date('"+dateStart+"','yyyy-MM-dd')\r\n" );
//			sql.append("      and ((a.cp_cl_date is null and (a.CP_TURN_DATE+a.cp_limit) > to_date('"+dateEnd+" 23:59:59','yyyy-MM-dd hh24:mi:ss')) or\r\n" );
//			sql.append("           (a.cp_cl_date is not null and a.cp_cl_date > to_date('"+dateEnd+" 23:59:59','yyyy-MM-dd hh24:mi:ss'))))\r\n" );
//			// 艾春 13.11.23 修改 需考核时间
			sql.append("      and ((a.CP_TURN_DATE <= to_date('"+dateEnd+" 23:59:59','yyyy-MM-dd hh24:mi:ss') AND " );
			sql.append("        ((a.cp_cl_date is null and (a.CP_TURN_DATE+a.cp_limit) <= to_date('"+dateEnd+" 23:59:59','yyyy-MM-dd hh24:mi:ss') and (a.CP_TURN_DATE+a.cp_limit) >= to_date('"+dateStart+"','yyyy-MM-dd')) " );
			sql.append("          or (a.cp_cl_date is not null and a.cp_cl_date <= to_date('"+dateEnd+" 23:59:59','yyyy-MM-dd hh24:mi:ss') and a.cp_cl_date >= to_date('"+dateStart+"','yyyy-MM-dd'))))) ");
			// 艾春 13.11.23 修改 需考核时间
		}else if(dateType == 1){
			sql.append(" and a.cp_acc_date >= to_date('"+dateStart+"','yyyy-MM-dd')  \r\n");
			sql.append(" and a.cp_acc_date <= to_date('"+dateEnd+" 23:59:59','yyyy-MM-dd hh24:mi:ss')\r\n");
		}else if(dateType == 2){
			sql.append(" and (a.CP_TURN_DATE < to_date('"+dateStart+"','yyyy-MM-dd')\r\n" );
			sql.append(" and ((a.cp_cl_date is null and (a.CP_TURN_DATE+a.cp_limit) >= to_date('"+dateStart+"','yyyy-MM-dd'))\r\n" );
			sql.append(" or (a.cp_cl_date is not null and a.cp_cl_date >= to_date('"+dateStart+"','yyyy-MM-dd') ) ) )");
		}else if(dateType == 3){
			sql.append(" and a.CP_TURN_DATE <= to_date('"+dateEnd+" 23:59:59','yyyy-MM-dd hh24:mi:ss')\r\n" );
			sql.append(" and ((a.cp_cl_date is null and (a.CP_TURN_DATE+a.cp_limit) > to_date('"+dateEnd+" 23:59:59','yyyy-MM-dd hh24:mi:ss'))\r\n" );
			sql.append(" or (a.cp_cl_date is not null and a.cp_cl_date > to_date('"+dateEnd+" 23:59:59','yyyy-MM-dd hh24:mi:ss')))");
		}else if(dateType == 4){
			// 艾春 13.11.23 修改需考核总时间控制
//			sql.append("and (1=1 and ((a.CP_TURN_DATE < to_date('"+dateStart+"','yyyy-MM-dd')\r\n" );
//			sql.append("      and (\r\n" );
//			sql.append("           (a.cp_cl_date is null and (a.CP_TURN_DATE+a.cp_limit) > to_date('"+dateEnd+" 23:59:59','yyyy-MM-dd hh24:mi:ss')) or\r\n" );
//			sql.append("           (a.cp_cl_date is not null and a.cp_cl_date > to_date('"+dateEnd+" 23:59:59','yyyy-MM-dd hh24:mi:ss'))\r\n" );
//			sql.append("          )\r\n" );
//			sql.append("      )\r\n" );
//			sql.append("      or\r\n" );
//			sql.append("      (a.CP_TURN_DATE >= to_date('"+dateStart+"','yyyy-MM-dd') and a.CP_TURN_DATE <= to_date('"+dateEnd+" 23:59:59','yyyy-MM-dd hh24:mi:ss') AND\r\n" );
//			sql.append("        (\r\n" );
//			sql.append("         (a.cp_cl_date is null and (a.CP_TURN_DATE+a.cp_limit) <= to_date('"+dateEnd+" 23:59:59','yyyy-MM-dd hh24:mi:ss'))\r\n" );
//			sql.append("          or (a.cp_cl_date is not null and a.cp_cl_date <= to_date('"+dateEnd+" 23:59:59','yyyy-MM-dd hh24:mi:ss'))\r\n" );
//			sql.append("        )\r\n" );
//			sql.append("      )\r\n" );
//			sql.append("     )  or ( 1=1  and a.cp_acc_date >= to_date('"+dateStart+"','yyyy-MM-dd')\r\n" );
//			sql.append("                  and a.cp_acc_date <= to_date('"+dateEnd+" 23:59:59','yyyy-MM-dd hh24:mi:ss')\r\n" );
//			sql.append("            )\r\n" );
//			sql.append(")");
			sql.append("and (1=1 and ((a.CP_TURN_DATE <= to_date('"+dateEnd+" 23:59:59','yyyy-MM-dd hh24:mi:ss') " );
			sql.append("     )  or ( 1=1  and a.cp_acc_date >= to_date('"+dateStart+"','yyyy-MM-dd') " );
			sql.append("                  and a.cp_acc_date <= to_date('"+dateEnd+" 23:59:59','yyyy-MM-dd hh24:mi:ss')))) " );
		}else if(dateType == 5){
			sql.append("and a.cp_acc_date >= to_date('"+dateStart+"','yyyy-MM-dd')\r\n" );
			sql.append("and a.cp_acc_date <= to_date('"+dateEnd+" 23:59:59','yyyy-MM-dd hh24:mi:ss')\r\n" );
			sql.append("and (a.cp_turn_date is null OR A.CP_TURN_DATE > to_date('"+dateEnd+" 23:59:59','yyyy-MM-dd hh24:mi:ss'))");
		}else if(dateType == 6){
			sql.append("and a.CP_TURN_DATE < to_date('"+dateStart+"','yyyy-MM-dd')\r\n" );
			sql.append("and ((a.cp_cl_date is null and (a.CP_TURN_DATE+a.cp_limit) > to_date('"+dateEnd+" 23:59:59','yyyy-MM-dd hh24:mi:ss'))\r\n" );
			sql.append(" or (a.cp_cl_date is not null and a.cp_cl_date > to_date('"+dateEnd+" 23:59:59','yyyy-MM-dd hh24:mi:ss')) )");
		}
		return sql;
	}
	
	
	//查询投诉总量
	public int getTotalComplaintDealReportforms(String dealName,String dateStart,String dateEnd){
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("select nvl(count(*),0) VAL\r\n");
		sbSql.append("  from TT_CRM_COMPLAINT a\r\n");
		sbSql.append("left join tc_user f on a.cp_acc_user = f.user_id\r\n" );
		sbSql.append(" where a.CP_BIZ_TYPE = "+Constant.TYPE_COMPLAIN+" \r\n");
		
		sbSql= appendSpecilWhere(sbSql, dealName);
		sbSql = appendDate(sbSql, dateStart, dateEnd, 4);

		return Integer.parseInt(this.pageQueryMap(sbSql.toString(),
			null,
			this.getFunName()).get("VAL").toString());
	}
	


}
