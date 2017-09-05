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
public class ComplaintCloseReportformsDao extends BaseDao{

	private static final ComplaintCloseReportformsDao dao = new ComplaintCloseReportformsDao();
	
	public static final ComplaintCloseReportformsDao getInstance() {
		return dao;
	}
	
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		return null;
	}

	
	//按省份统计 
	public PageResult<Map<String,Object>> queryComplaintCloseReportformsBySmallOrg(String dealName,String dateStart,String dateEnd,int pageSize,int curPage){
		String typeName = "nvl(j.REGION_NAME,'历史投诉')";
		String typeValue = "a.CP_PROVINCE_ID";
		String sql  = returnCloseSql(typeName,typeValue,dealName,dateStart,dateEnd);
		return (PageResult<Map<String, Object>>)this.pageQuery(sql.toString(),
				null,
				this.getFunName(),
				pageSize,
				curPage);
	} 
	
	//按省份统计  List
	public List<Map<String,Object>> queryComplaintCloseReportformsBySmallOrgList(String dealName,String dateStart,String dateEnd){
		String typeName = "j.REGION_NAME";
		String typeValue = "a.CP_PROVINCE_ID";
		String sql  = returnCloseSql(typeName,typeValue,dealName,dateStart,dateEnd);
		return (List<Map<String, Object>>)this.pageQuery(sql.toString(),
				null,
				this.getFunName());
	}
	
	//按受理部门统计 
	public PageResult<Map<String,Object>> queryComplaintCloseReportformsByCustom(String dealName,String dateStart,String dateEnd,int pageSize,int curPage){
		String typeName = "e.org_name";
		String typeValue = "a.cp_source_custom";
		String sql  = returnCloseSql(typeName,typeValue,dealName,dateStart,dateEnd);
		return (PageResult<Map<String, Object>>)this.pageQuery(sql.toString(),
				null,
				this.getFunName(),
				pageSize,
				curPage);
	} 
	
	//按受理部门统计  List
	public List<Map<String,Object>> queryComplaintCloseReportformsByCustomList(String dealName,String dateStart,String dateEnd){
		String typeName = "e.org_name";
		String typeValue = "a.cp_source_custom";
		String sql  = returnCloseSql(typeName,typeValue,dealName,dateStart,dateEnd);
		return (List<Map<String, Object>>)this.pageQuery(sql.toString(),
				null,
				this.getFunName());
	}


	
	//按大区统计 
	public PageResult<Map<String,Object>> queryComplaintCloseReportformsByTmOrg(String dealName,String dateStart,String dateEnd,int pageSize,int curPage){
		// 艾春 2013.11.24 修改大区如果未转出，则统一归口为服务营销处
		String typeName = "NVL(k.org_name,'服务营销处')";
//		String typeName = "k.org_name";
		// 艾春 2013.11.24 修改大区如果未转出，则统一归口为服务营销处
		String typeValue = "a.CP_DEAL_ORG";
		String sql  = returnCloseSql(typeName,typeValue,dealName,dateStart,dateEnd);
		return (PageResult<Map<String, Object>>)this.pageQuery(sql.toString(),
				null,
				this.getFunName(),
				pageSize,
				curPage);
	} 
	
	//按大区统计  List
	public List<Map<String,Object>> queryComplaintCloseReportformsByTmOrgList(String dealName,String dateStart,String dateEnd){
		// 艾春 2013.11.24 修改大区如果未转出，则统一归口为服务营销处
		String typeName = "NVL(k.org_name,'服务营销处')";
//		String typeName = "k.org_name";
		// 艾春 2013.11.24 修改大区如果未转出，则统一归口为服务营销处
		String typeValue = "a.CP_DEAL_ORG";
		String sql  = returnCloseSql(typeName,typeValue,dealName,dateStart,dateEnd);
		System.out.println(sql);
		return (List<Map<String, Object>>)this.pageQuery(sql.toString(),
				null,
				this.getFunName());
	}
	

	private String returnCloseSql(String typeName,String typeValue,String dealName,
									String dateStart,String dateEnd){
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
		sql.append(" SUM(CASE WHEN 1=1\r\n" );
		sql = appendDate(sql, dateStart, dateEnd, 1);
		sql.append(" THEN 1 else 0 END) ACCC,\r\n" );
		sql.append("\r\n" );
		sql.append(" sum(case when  a.CP_STATUS not in ("+Constant.COMPLAINT_STATUS_ALREADY_CLOSE+","+Constant.COMPLAINT_STATUS_CLOSE+") then\r\n" );
		sql.append("          case when sysdate<=nvl(a.Cp_Cl_Date,a.CP_TURN_DATE + NVL(a.CP_LIMIT,0)) \r\n" );
		sql = appendDate(sql, dateStart, dateEnd, 0);
		sql.append("    then 1  else 0 end  \r\n");
		sql.append("     else case when a.Cp_Status ="+Constant.COMPLAINT_STATUS_CLOSE+" \r\n" );
		sql = appendDate(sql, dateStart, dateEnd, 0);
		sql.append("  then 1 ");
		sql.append("               when  a.Cp_Status = "+Constant.COMPLAINT_STATUS_ALREADY_CLOSE+" and a.Cp_Cl_Once_Date > a.CP_TURN_DATE + NVL(a.CP_LIMIT,0) then 0 \r\n" );
		sql.append("               when  a.Cp_Status = "+Constant.COMPLAINT_STATUS_ALREADY_CLOSE+" and trunc(X.BACK_DATE) <= trunc(nvl(a.Cp_Cl_Date,a.CP_TURN_DATE + NVL(a.CP_LIMIT,0))) \r\n" );
		sql = appendDate(sql, dateStart, dateEnd, 0);
		sql.append("    then 1  else 0 end  \r\n");
		sql.append("     end\r\n" );
		sql.append(" ) TIMELYCLOSEC,\r\n" );
		sql.append("\r\n" );
		sql.append(" sum(case when a.cp_status in("+Constant.COMPLAINT_STATUS_ALREADY_CLOSE+","+Constant.COMPLAINT_STATUS_CLOSE+") \r\n" );
		sql = appendDate(sql, dateStart, dateEnd, 0);
		sql.append(" then 1 else 0 end) CLOSEDC, \r\n");
		sql.append("\r\n" );
		sql.append(" sum(case when a.cp_cl_status = "+Constant.PASS_Manager_03+" and a.cp_status in("+Constant.COMPLAINT_STATUS_ALREADY_CLOSE+","+Constant.COMPLAINT_STATUS_CLOSE+") \r\n" );
		sql = appendDate(sql, dateStart, dateEnd, 0);
		sql.append(" then 1 else 0 end) DELAYC, \r\n");
		sql.append("\r\n" );
		
		String timerCloseSum = " SUM(case when a.CP_STATUS not in ("+Constant.COMPLAINT_STATUS_ALREADY_CLOSE+","+Constant.COMPLAINT_STATUS_CLOSE+") then case when sysdate<=nvl(a.Cp_Cl_Date,a.CP_TURN_DATE + NVL(a.CP_LIMIT,0))"+appendDate(null, dateStart, dateEnd, 0)+" then 1  else 0 end" +
								 " else case when a.Cp_Status ="+Constant.COMPLAINT_STATUS_CLOSE+" "+appendDate(null, dateStart, dateEnd, 0)+" then 1 when  a.Cp_Status = "+Constant.COMPLAINT_STATUS_ALREADY_CLOSE+" and a.Cp_Cl_Once_Date > a.CP_TURN_DATE + NVL(a.CP_LIMIT,0) then 0 " +
								 " when  a.Cp_Status = "+Constant.COMPLAINT_STATUS_ALREADY_CLOSE+" and trunc(X.BACK_DATE) <= trunc(nvl(a.Cp_Cl_Date,a.CP_TURN_DATE + NVL(a.CP_LIMIT,0)))"+appendDate(null, dateStart, dateEnd, 0)+" then 1 else 0 end end) ";
		String totalSum = " SUM(CASE WHEN 1=1 "+appendDate(null, dateStart, dateEnd, 0)+"THEN 1 else 0 END) ";
		
		String colcolseSum = " sum(case when a.cp_status in("+Constant.COMPLAINT_STATUS_ALREADY_CLOSE+","+Constant.COMPLAINT_STATUS_CLOSE +") "+appendDate(null, dateStart, dateEnd, 0) + "  then 1 else 0 end ) ";
		
		// 艾春 2013.12.2 修改及时关闭率统计基数
		sql = appendDecodeRate(sql, timerCloseSum,totalSum);
//		sql = appendDecodeRate(sql, timerCloseSum,colcolseSum);
		// 艾春 2013.12.2 修改及时关闭率统计基数
		
		sql.append(" TIMELYCLOSER,\r\n" );
		sql.append("\r\n" );
		sql = appendDecodeRate(sql, "SUM(case when a.Cp_Is_Once_Sf = "+Constant.IF_TYPE_YES+" and a.cp_cl_date is null "+appendDate(null, dateStart, dateEnd, 0)+" then 1 else 0 end)",
		// 艾春 2013.12.2 修改 一次关闭率统计基数
				totalSum);
//				colcolseSum);
		// 艾春 2013.12.2 修改 一次关闭率统计基数
		
		// 艾春 2013.11.23 修改一次关闭率名称
		sql.append(" ONCETIMELYCLOSEC,\r\n" );
//		sql.append(" CLOSER,\r\n" );
		// 艾春 2013.11.23 修改一次关闭率名称
		
		sql.append("\r\n" );
		sql = appendDecodeRate(sql, "SUM(case when a.cp_status in("+Constant.COMPLAINT_STATUS_ALREADY_CLOSE+","+Constant.COMPLAINT_STATUS_CLOSE+") "+appendDate(null, dateStart, dateEnd, 0)+" then 1 else 0 end)",
				totalSum);
		
		// 艾春 2013.11.23 修改关闭率名称
		sql.append(" CLOSER\r\n" );
//		sql.append(" ONCETIMELYCLOSEC\r\n" );
		// 艾春 2013.11.23 修改关闭率名称
		sql.append("\r\n" );
		sql.append("from TT_CRM_COMPLAINT a\r\n" );
		sql.append("left join tm_org_custom e on e.org_id = a.cp_source_custom\r\n" );
		sql.append("left join tc_user f on a.cp_acc_user = f.user_id\r\n" );
		sql.append("left join tm_region j on j.REGION_CODE = a.CP_PROVINCE_ID\r\n" );
		sql.append("left join (SELECT ORG_ID,ORG_NAME FROM TM_ORG  WHERE ORG_LEVEL = 2 UNION SELECT ORG_ID, ORG_NAME FROM TM_ORG_CUSTOM) k on k.org_id = a.CP_DEAL_ORG\r\n" );
		sql.append("LEFT JOIN (SELECT TO_CHAR(MAX(DR.CD_DATE), 'YYYY-MM-DD HH:MI:SS') LASTDEALDATE, DR.CP_ID, MAX(DECODE(DR.CP_STATUS,"+Constant.COMPLAINT_STATUS_DOING_ALREADY+",DR.CD_DATE,NULL)) BACK_DATE\r\n" );
		sql.append("FROM TT_CRM_COMPLAINT_DEAL_RECORD DR GROUP BY DR.CP_ID) X ON X.CP_ID = a.CP_ID\r\n" );
		sql.append("\r\n" );
		sql.append("where A.CP_BIZ_TYPE = "+Constant.TYPE_COMPLAIN+"\r\n" );
		sql= appendSpecilWhere(sql, dealName);
		sql = appendDate(sql, dateStart, dateEnd, 4);
		sql.append("group by rollup("+typeValue+") ");

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
			  sql.append("      and ((a.CP_TURN_DATE <= to_date('"+dateEnd+" 23:59:59','yyyy-MM-dd hh24:mi:ss') AND " );
		      sql.append("        ((a.cp_cl_date is null and (a.CP_TURN_DATE+a.cp_limit) <= to_date('"+dateEnd+" 23:59:59','yyyy-MM-dd hh24:mi:ss') and (a.CP_TURN_DATE+a.cp_limit) >= to_date('"+dateStart+"','yyyy-MM-dd')) " );
		      sql.append("          or (a.cp_cl_date is not null and a.cp_cl_date <= to_date('"+dateEnd+" 23:59:59','yyyy-MM-dd hh24:mi:ss') and a.cp_cl_date >= to_date('"+dateStart+"','yyyy-MM-dd'))))) ");
		      
			
//			sql.append("      or(a.CP_TURN_DATE >= to_date('"+dateStart+"','yyyy-MM-dd') and a.CP_TURN_DATE <= to_date('"+dateEnd+" 23:59:59','yyyy-MM-dd hh24:mi:ss') AND\r\n" );
//			sql.append("        ((a.cp_cl_date is null and (a.CP_TURN_DATE+a.cp_limit) <= to_date('"+dateEnd+" 23:59:59','yyyy-MM-dd hh24:mi:ss'))\r\n" );
//			sql.append("          or (a.cp_cl_date is not null and a.cp_cl_date <= to_date('"+dateEnd+" 23:59:59','yyyy-MM-dd hh24:mi:ss')))))");
			// 艾春 13.11.23 修改 需考核时间
		}else if(dateType == 1){
			sql.append(" and a.cp_acc_date >= to_date('"+dateStart+"','yyyy-MM-dd') ");
			sql.append(" and a.cp_acc_date <= to_date('"+dateEnd+" 23:59:59','yyyy-MM-dd hh24:mi:ss') ");
		}else if(dateType == 2){
			sql.append(" and (a.CP_TURN_DATE < to_date('"+dateStart+"','yyyy-MM-dd') " );
			sql.append(" and ((a.cp_cl_date is null and (a.CP_TURN_DATE+a.cp_limit) >= to_date('"+dateStart+"','yyyy-MM-dd')) " );
			sql.append(" or (a.cp_cl_date is not null and a.cp_cl_date >= to_date('"+dateStart+"','yyyy-MM-dd') ) ) )");
		}else if(dateType == 3){
			sql.append(" and a.CP_TURN_DATE <= to_date('"+dateEnd+" 23:59:59','yyyy-MM-dd hh24:mi:ss') " );
			sql.append(" and ((a.cp_cl_date is null and (a.CP_TURN_DATE+a.cp_limit) > to_date('"+dateEnd+" 23:59:59','yyyy-MM-dd hh24:mi:ss')) " );
			sql.append(" or (a.cp_cl_date is not null and a.cp_cl_date > to_date('"+dateEnd+" 23:59:59','yyyy-MM-dd hh24:mi:ss'))) ");
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
			sql.append("and ((a.cp_cl_date is null and (a.CP_TURN_DATE+a.cp_limit) >= to_date('"+dateStart+"','yyyy-MM-dd')) " );
			sql.append(" or (a.cp_cl_date is not null and a.cp_cl_date >= to_date('"+dateStart+"','yyyy-MM-dd'))) ");
			sql.append("     )  or ( 1=1  and a.cp_acc_date >= to_date('"+dateStart+"','yyyy-MM-dd') " );
			sql.append("                  and a.cp_acc_date <= to_date('"+dateEnd+" 23:59:59','yyyy-MM-dd hh24:mi:ss')))) " );
			// 艾春 13.11.23 修改需考核总时间控制
		}else if(dateType == 5){
			sql.append("and a.cp_acc_date >= to_date('"+dateStart+"','yyyy-MM-dd') " );
			sql.append("and a.cp_acc_date <= to_date('"+dateEnd+" 23:59:59','yyyy-MM-dd hh24:mi:ss') " );
			sql.append("and (a.cp_turn_date is null OR A.CP_TURN_DATE > to_date('"+dateEnd+" 23:59:59','yyyy-MM-dd hh24:mi:ss')) ");
		}else if(dateType == 6){
			sql.append("and a.CP_TURN_DATE < to_date('"+dateStart+"','yyyy-MM-dd')\r\n" );
			sql.append("and ((a.cp_cl_date is null and (a.CP_TURN_DATE+a.cp_limit) > to_date('"+dateEnd+" 23:59:59','yyyy-MM-dd hh24:mi:ss')) " );
			sql.append(" or (a.cp_cl_date is not null and a.cp_cl_date > to_date('"+dateEnd+" 23:59:59','yyyy-MM-dd hh24:mi:ss')) ) ");
		}
		return sql;
	}
	

	
	//查询投诉总量
	public int getTotalComplaintCloseReportforms(String dealName,String dateStart,String dateEnd){
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("select count(*) VAL\r\n");
		sbSql.append("  from TT_CRM_COMPLAINT a\r\n");
		sbSql.append("  left join tc_user f\r\n");
		sbSql.append("    on a.cp_acc_user = f.user_id\r\n");
		sbSql.append(" where a.CP_BIZ_TYPE = "+Constant.TYPE_COMPLAIN+" \r\n");
		sbSql= appendSpecilWhere(sbSql, dealName);
		sbSql = appendDate(sbSql, dateStart, dateEnd, 4);

		return Integer.parseInt(this.pageQueryMap(sbSql.toString(),
			null,
			this.getFunName()).get("VAL").toString());
	}
	


}
