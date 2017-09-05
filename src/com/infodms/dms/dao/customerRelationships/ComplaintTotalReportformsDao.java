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
public class ComplaintTotalReportformsDao extends BaseDao{

	private static final ComplaintTotalReportformsDao dao = new ComplaintTotalReportformsDao();
	
	public static final ComplaintTotalReportformsDao getInstance() {
		return dao;
	}
	
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		return null;
	}
	
	//按受理商家统计 
	public PageResult<Map<String,Object>> queryComplaintTotalReportformsByCpObject(String dateYear,String smallTmOrg,String dealName,String dateStart,String dateEnd,String cpObject,int pageSize,int curPage){
		String typeName = "nvl(h.dealer_name,r.ORG_NAME)";
		String typeValue = "a.cp_object";
		String sql  = returnTotalSql(typeName,typeValue,dateYear,smallTmOrg,dealName,dateStart,dateEnd,cpObject);
		return (PageResult<Map<String, Object>>)this.pageQuery(sql.toString(),
				null,
				this.getFunName(),
				pageSize,
				curPage);
	} 
	
	//按受理商家统计 List
	public List<Map<String,Object>> queryComplaintTotalReportformsByCpObjectList(String dateYear,String smallTmOrg,String dealName,String dateStart,String dateEnd,String cpObject){
		String typeName = "nvl(h.dealer_name,r.ORG_NAME)";
		String typeValue = "a.cp_object";
		String sql  = returnTotalSql(typeName,typeValue,dateYear,smallTmOrg,dealName,dateStart,dateEnd,cpObject);
		return (List<Map<String, Object>>)this.pageQuery(sql.toString(),
				null,
				this.getFunName());
	}
	
	//按省份统计 
	public PageResult<Map<String,Object>> queryComplaintTotalReportformsBySmallOrg(String dateYear,String smallTmOrg,String dealName,String dateStart,String dateEnd,String cpObject,int pageSize,int curPage){
		String typeName = "nvl(j.REGION_NAME,'历史投诉')";
		String typeValue = "a.CP_PROVINCE_ID";
		String sql  = returnTotalSql(typeName,typeValue,dateYear,smallTmOrg,dealName,dateStart,dateEnd,cpObject);
		return (PageResult<Map<String, Object>>)this.pageQuery(sql.toString(),
				null,
				this.getFunName(),
				pageSize,
				curPage);
	} 
	
	//按省份统计  List
	public List<Map<String,Object>> queryComplaintTotalReportformsBySmallOrgList(String dateYear,String smallTmOrg,String dealName,String dateStart,String dateEnd,String cpObject){
		String typeName = "nvl(j.REGION_NAME,'历史投诉')";
		String typeValue = "a.CP_PROVINCE_ID";
		String sql  = returnTotalSql(typeName,typeValue,dateYear,smallTmOrg,dealName,dateStart,dateEnd,cpObject);
		return (List<Map<String, Object>>)this.pageQuery(sql.toString(),
				null,
				this.getFunName());
	}
	
	//按受理部门统计 
	public PageResult<Map<String,Object>> queryComplaintTotalReportformsByCustom(String dateYear,String smallTmOrg,String dealName,String dateStart,String dateEnd,String cpObject,int pageSize,int curPage){
		String typeName = "e.org_name";
		String typeValue = "a.cp_source_custom";
		String sql  = returnTotalSql(typeName,typeValue,dateYear,smallTmOrg,dealName,dateStart,dateEnd,cpObject);
		return (PageResult<Map<String, Object>>)this.pageQuery(sql.toString(),
				null,
				this.getFunName(),
				pageSize,
				curPage);
	} 
	
	//按受理部门统计  List
	public List<Map<String,Object>> queryComplaintTotalReportformsByCustomList(String dateYear,String smallTmOrg,String dealName,String dateStart,String dateEnd,String cpObject){
		String typeName = "e.org_name";
		String typeValue = "a.cp_source_custom";
		String sql  = returnTotalSql(typeName,typeValue,dateYear,smallTmOrg,dealName,dateStart,dateEnd,cpObject);
		return (List<Map<String, Object>>)this.pageQuery(sql.toString(),
				null,
				this.getFunName());
	}
	
	//按内容类型统计 
	public PageResult<Map<String,Object>> queryComplaintTotalReportformsByBizContent(String dateYear,String smallTmOrg,String dealName,String dateStart,String dateEnd,String cpObject,int pageSize,int curPage){
		// 艾春 2013.11.24 修改内容类型如果为空，则统一归口为历史投诉
		String typeName = "NVL(i.code_desc,'历史投诉')";
//		String typeName = "i.code_desc";
		// 艾春 2013.11.24 修改内容类型如果为空，则统一归口为历史投诉
		String typeValue = "a.cp_biz_content";
		String sql  = returnTotalSql(typeName,typeValue,dateYear,smallTmOrg,dealName,dateStart,dateEnd,cpObject);
		return (PageResult<Map<String, Object>>)this.pageQuery(sql.toString(),
				null,
				this.getFunName(),
				pageSize,
				curPage);
	} 
	
	//按内容类型统计 List
	public List<Map<String,Object>> queryComplaintTotalReportformsByBizContentList(String dateYear,String smallTmOrg,String dealName,String dateStart,String dateEnd,String cpObject){
		// 艾春 2013.11.24 修改内容类型如果为空，则统一归口为历史投诉
		String typeName = "NVL(i.code_desc,'历史投诉')";
//		String typeName = "i.code_desc";
		// 艾春 2013.11.24 修改内容类型如果为空，则统一归口为历史投诉
		
		String typeValue = "a.cp_biz_content";
		String sql  = returnTotalSql(typeName,typeValue,dateYear,smallTmOrg,dealName,dateStart,dateEnd,cpObject);
		return (List<Map<String, Object>>)this.pageQuery(sql.toString(),
				null,
				this.getFunName());
	}
	
	//按年份统计 
	public PageResult<Map<String,Object>> queryComplaintTotalReportformsByYear(String dateYear,String smallTmOrg,String dealName,String dateStart,String dateEnd,String cpObject,int pageSize,int curPage){
		String typeName = "to_char(Extract(year from a.cp_acc_date))";
		String typeValue = "Extract(year from a.cp_acc_date)";
		String sql  = returnTotalSql(typeName,typeValue,dateYear,smallTmOrg,dealName,dateStart,dateEnd,cpObject);
		return (PageResult<Map<String, Object>>)this.pageQuery(sql.toString(),
				null,
				this.getFunName(),
				pageSize,
				curPage);
	} 
	
	//按年份统计  List
	public List<Map<String,Object>> queryComplaintTotalReportformsByYearList(String dateYear,String smallTmOrg,String dealName,String dateStart,String dateEnd,String cpObject){
		String typeName = "to_char(Extract(year from a.cp_acc_date))";
		String typeValue = "Extract(year from a.cp_acc_date)";
		String sql  = returnTotalSql(typeName,typeValue,dateYear,smallTmOrg,dealName,dateStart,dateEnd,cpObject);
		return (List<Map<String, Object>>)this.pageQuery(sql.toString(),
				null,
				this.getFunName());
	}
	
	//按大区统计 
	public PageResult<Map<String,Object>> queryComplaintTotalReportformsByTmOrg(String dateYear,String smallTmOrg,String dealName,String dateStart,String dateEnd,String cpObject,int pageSize,int curPage){
		// 艾春 2013.11.24 修改大区如果未转出，则统一归口为服务营销处
		String typeName = "NVL(k.org_name,'服务营销处')";
//		String typeName = "k.org_name";
		// 艾春 2013.11.24 修改大区如果未转出，则统一归口为服务营销处
		String typeValue = "a.CP_DEAL_ORG";
		String sql  = returnTotalSql(typeName,typeValue,dateYear,smallTmOrg,dealName,dateStart,dateEnd,cpObject);
		return (PageResult<Map<String, Object>>)this.pageQuery(sql.toString(),
				null,
				this.getFunName(),
				pageSize,
				curPage);
	} 
	
	//按大区统计  List
	public List<Map<String,Object>> queryComplaintTotalReportformsByTmOrgList(String dateYear,String smallTmOrg,String dealName,String dateStart,String dateEnd,String cpObject){
		// 艾春 2013.11.24 修改大区如果未转出，则统一归口为服务营销处
		String typeName = "NVL(k.org_name,'服务营销处')";
//		String typeName = "k.org_name";
		// 艾春 2013.11.24 修改大区如果未转出，则统一归口为服务营销处
		String typeValue = "a.CP_DEAL_ORG";
		String sql  = returnTotalSql(typeName,typeValue,dateYear,smallTmOrg,dealName,dateStart,dateEnd,cpObject);
		return (List<Map<String, Object>>)this.pageQuery(sql.toString(),
				null,
				this.getFunName());
	}
	

	private String returnTotalSql(String typeName,String typeValue,
									String dateYear,String tmSmallOrg,String dealName,
									String dateStart,String dateEnd,String cpObject){
		StringBuffer sql = new StringBuffer();

		sql.append("SELECT\r\n" );
		// 艾春 2013.11.23 修改分组对象名称
		sql.append(" decode(grouping("+typeValue+"), 0, max("+typeName+"), null) TYPENAME,\r\n" );
//		sql.append(" max("+typeName+") TYPENAME,\r\n" );
		// 艾春 2013.11.23 修改分组对象名称
		sql.append(" SUM(CASE WHEN 1=1 \r\n" );
		sql = appendDate(sql, dateStart, dateEnd, 7);
		sql.append(" THEN 1 else 0 END) TOTAL,\r\n" );
		sql.append("\r\n" );
		sql.append(" SUM(CASE WHEN 1=1 \r\n" );
		sql = appendDate(sql, dateStart, dateEnd, 1);
		sql.append(" THEN 1 else 0 END) ACCTOTAL,\r\n" );
		sql.append(" SUM(CASE WHEN 1=1 \r\n" );
		sql = appendDate(sql, dateStart, dateEnd, 8);
		sql.append(" THEN 1 else 0 END) YACCTOTAL,\r\n" );
		sql.append(" SUM(CASE WHEN 1=1 \r\n" );
		sql = appendDate(sql, dateStart, dateEnd, 9);
		sql.append(" THEN 1 else 0 END) NACCTOTAL,\r\n" );
		sql.append("\r\n" );
		sql.append(" SUM(CASE WHEN 1=1 \r\n" );
		sql = appendDate(sql, dateStart, dateEnd, 2);
		sql.append(" THEN 1 else 0 END) INTOTAL,\r\n" );
		sql.append("\r\n" );
		sql.append(" SUM(CASE WHEN 1=1\r\n" );
		sql = appendDate(sql, dateStart, dateEnd, 3);
		sql.append("  THEN 1 else 0 END) OUTTOTAL,\r\n" );
		sql.append("\r\n" );
		sql.append(" SUM(CASE WHEN 1=1\r\n" );
		sql = appendDate(sql, dateStart, dateEnd, 5);
		sql.append("  THEN 1 else 0 END) UNTURNTOTAL,\r\n" );
		sql.append("\r\n" );
		sql.append(" SUM(CASE WHEN 1=1\r\n" );
		sql = appendDate(sql, dateStart, dateEnd, 6);
		sql.append("  THEN 1 else 0 END) UPTOTAL,\r\n" );
		sql.append("\r\n" );
		sql.append(" SUM(CASE WHEN  A.CP_STATUS NOT IN ("+Constant.COMPLAINT_STATUS_ALREADY_CLOSE+", "+Constant.COMPLAINT_STATUS_CLOSE+")\r\n" );
		sql = appendDate(sql, dateStart, dateEnd, 0);
		sql.append(" THEN 1 else 0 END) DOINGC,\r\n" );
		sql.append("\r\n" );
		sql.append(" SUM(CASE WHEN  A.CP_STATUS IN ("+Constant.COMPLAINT_STATUS_ALREADY_CLOSE+", "+Constant.COMPLAINT_STATUS_CLOSE+")\r\n" );
		sql = appendDate(sql, dateStart, dateEnd, 0);
		sql.append(" THEN 1 else 0 END) CLOSEDC,\r\n" );
		sql.append("\r\n" );
		sql.append(" SUM(CASE WHEN  A.CP_STATUS = '"+Constant.COMPLAINT_STATUS_CLOSE+"'\r\n" );
		sql = appendDate(sql, dateStart, dateEnd, 0);
		sql.append(" THEN 1 else 0 END) FORCECLOSEDC,\r\n" );
		sql.append("\r\n" );
		sql.append(" SUM(CASE WHEN  A.CP_SF = '"+Constant.PLEASED+"'\r\n" );
		sql = appendDate(sql, dateStart, dateEnd, 0);
		sql.append(" THEN 1 else 0 END) GOODC,\r\n" );
		sql.append("\r\n" );
		
		sql = appendDecodeRate(sql, "SUM(CASE WHEN  A.CP_STATUS IN ("+Constant.COMPLAINT_STATUS_ALREADY_CLOSE+", "+Constant.COMPLAINT_STATUS_CLOSE+")"+appendDate(null, dateStart, dateEnd, 0).toString()+" THEN 1 else 0 END)",
					"SUM(CASE WHEN 1=1"+appendDate(null, dateStart, dateEnd, 0).toString()+" THEN 1 else 0 END) ");
		sql.append(" CLOSEDR,\r\n ");
		sql.append("\r\n" );		
		
		// 艾春 2013.12.2 修改抱怨处理满意率逻辑 = 正常关闭投诉 / 已关闭投诉(正常关闭 + 强制关闭)
		sql = appendDecodeRate(sql, "SUM(CASE WHEN  A.CP_STATUS = "+ Constant.COMPLAINT_STATUS_ALREADY_CLOSE + " " +appendDate(null, dateStart, dateEnd, 0).toString()+" THEN 1 else 0 END)",
				"SUM(CASE WHEN A.CP_STATUS IN ("+Constant.COMPLAINT_STATUS_ALREADY_CLOSE+", "+Constant.COMPLAINT_STATUS_CLOSE+") "+appendDate(null, dateStart, dateEnd, 0).toString()+" THEN 1 else 0 END) ");
		sql.append(" GOODR,\r\n ");
		sql.append("\r\n" );
		
		// 艾春 2013.12.2 修改 平均时长 计算逻辑
//		sql = appendDecodeRate(sql, "SUM(CASE WHEN  A.CP_STATUS IN ("+Constant.COMPLAINT_STATUS_ALREADY_CLOSE+", "+Constant.COMPLAINT_STATUS_CLOSE+") "+appendDate(null, dateStart, dateEnd, 0).toString()+" THEN (X.BACK_DATE-A.CP_TURN_DATE)*24*60 else 0 END)",
//				"SUM(CASE WHEN 1=1"+appendDate(null, dateStart, dateEnd, 0).toString()+" THEN (X.BACK_DATE-A.CP_TURN_DATE)*24*60 else 0 END) ");
//		sql.append(" AVGTOTAL,\r\n ");
//		sql.append("\r\n" );
//		
//		sql = appendDecodeRate(sql, "SUM(CASE WHEN  A.CP_STATUS IN ("+Constant.COMPLAINT_STATUS_ALREADY_CLOSE+", "+Constant.COMPLAINT_STATUS_CLOSE+") AND A.CP_LIMIT = 1 "+appendDate(null, dateStart, dateEnd, 0).toString()+" THEN (X.BACK_DATE-A.CP_TURN_DATE)*24*60 else 0 END)",
//				"SUM(CASE WHEN 1=1"+appendDate(null, dateStart, dateEnd, 0).toString()+" THEN (X.BACK_DATE-A.CP_TURN_DATE)*24*60 else 0 END) ");
//		sql.append(" AVGONEDAY,\r\n ");
//		sql.append("\r\n" );
//		
//		sql = appendDecodeRate(sql, "SUM(CASE WHEN  A.CP_STATUS IN ("+Constant.COMPLAINT_STATUS_ALREADY_CLOSE+", "+Constant.COMPLAINT_STATUS_CLOSE+") AND A.CP_LIMIT = 3 "+appendDate(null, dateStart, dateEnd, 0).toString()+" THEN (X.BACK_DATE-A.CP_TURN_DATE)*24*60 else 0 END)",
//				"SUM(CASE WHEN 1=1"+appendDate(null, dateStart, dateEnd, 0).toString()+" THEN (X.BACK_DATE-A.CP_TURN_DATE)*24*60 else 0 END) ");
//		sql.append(" AVGTHREEDAY,\r\n ");
//		sql.append("\r\n" );
//		
//		sql = appendDecodeRate(sql, "SUM(CASE WHEN  A.CP_STATUS IN ("+Constant.COMPLAINT_STATUS_ALREADY_CLOSE+", "+Constant.COMPLAINT_STATUS_CLOSE+") AND A.CP_LIMIT = 7 "+appendDate(null, dateStart, dateEnd, 0).toString()+" THEN (X.BACK_DATE-A.CP_TURN_DATE)*24*60 else 0 END)",
//				"SUM(CASE WHEN 1=1"+appendDate(null, dateStart, dateEnd, 0).toString()+" THEN (X.BACK_DATE-A.CP_TURN_DATE)*24*60 else 0 END) ");
//		sql.append(" AVGSEVENDAY \r\n ");
		
		sql = appendDecodeAvg(sql, "SUM(CASE WHEN  A.CP_STATUS IN ("+Constant.COMPLAINT_STATUS_ALREADY_CLOSE+", "+Constant.COMPLAINT_STATUS_CLOSE+") "+appendDate(null, dateStart, dateEnd, 0).toString()+" THEN (X.BACK_DATE-A.CP_TURN_DATE)*24 else 0 END)",
				"SUM(CASE WHEN A.CP_STATUS IN ("+Constant.COMPLAINT_STATUS_ALREADY_CLOSE+", "+Constant.COMPLAINT_STATUS_CLOSE+") "+appendDate(null, dateStart, dateEnd, 0).toString()+" THEN 1 else 0 END) ");
		sql.append(" AVGTOTAL,\r\n ");
		sql.append("\r\n" );
		
		sql = appendDecodeAvg(sql, "SUM(CASE WHEN  A.CP_STATUS IN ("+Constant.COMPLAINT_STATUS_ALREADY_CLOSE+", "+Constant.COMPLAINT_STATUS_CLOSE+") AND A.CP_LIMIT = 1 "+appendDate(null, dateStart, dateEnd, 0).toString()+" THEN (X.BACK_DATE-A.CP_TURN_DATE)*24 else 0 END)",
				"SUM(CASE WHEN A.CP_LIMIT = 1 and A.CP_STATUS IN ("+Constant.COMPLAINT_STATUS_ALREADY_CLOSE+", "+Constant.COMPLAINT_STATUS_CLOSE+") "+appendDate(null, dateStart, dateEnd, 0).toString()+" THEN 1 else 0 END) ");
		sql.append(" AVGONEDAY,\r\n ");
		sql.append("\r\n" );
		
		sql = appendDecodeAvg(sql, "SUM(CASE WHEN  A.CP_STATUS IN ("+Constant.COMPLAINT_STATUS_ALREADY_CLOSE+", "+Constant.COMPLAINT_STATUS_CLOSE+") AND A.CP_LIMIT = 3 "+appendDate(null, dateStart, dateEnd, 0).toString()+" THEN (X.BACK_DATE-A.CP_TURN_DATE)*24 else 0 END)",
				"SUM(CASE WHEN A.CP_LIMIT = 3 and A.CP_STATUS IN ("+Constant.COMPLAINT_STATUS_ALREADY_CLOSE+", "+Constant.COMPLAINT_STATUS_CLOSE+") "+appendDate(null, dateStart, dateEnd, 0).toString()+" THEN 1 else 0 END) ");
		sql.append(" AVGTHREEDAY,\r\n ");
		sql.append("\r\n" );
		
		sql = appendDecodeAvg(sql, "SUM(CASE WHEN  A.CP_STATUS IN ("+Constant.COMPLAINT_STATUS_ALREADY_CLOSE+", "+Constant.COMPLAINT_STATUS_CLOSE+") AND A.CP_LIMIT = 7 "+appendDate(null, dateStart, dateEnd, 0).toString()+" THEN (X.BACK_DATE-A.CP_TURN_DATE)*24 else 0 END)",
				"SUM(CASE WHEN A.CP_LIMIT = 7 and A.CP_STATUS IN ("+Constant.COMPLAINT_STATUS_ALREADY_CLOSE+", "+Constant.COMPLAINT_STATUS_CLOSE+") "+appendDate(null, dateStart, dateEnd, 0).toString()+" THEN 1 else 0 END) ");
		sql.append(" AVGSEVENDAY \r\n ");
		// 艾春 2013.12.2 修改 平均时长 计算逻辑
		
		sql.append("  FROM TT_CRM_COMPLAINT A\r\n" );
		sql.append("  LEFT JOIN TM_ORG_CUSTOM E ON E.ORG_ID = A.CP_SOURCE_CUSTOM\r\n" );
		sql.append("  LEFT JOIN TC_USER F ON A.CP_ACC_USER = F.USER_ID\r\n" );
		sql.append("  LEFT JOIN TM_DEALER H ON A.CP_OBJECT = H.DEALER_ID\r\n" );
		sql.append("  LEFT JOIN (SELECT T.ORG_ID, T.ORG_NAME FROM TM_ORG T WHERE T.ORG_LEVEL = 2 UNION SELECT ORG_ID, ORG_NAME FROM TM_ORG_CUSTOM) R ON A.CP_OBJECT = R.ORG_ID\r\n" );
		sql.append("  LEFT JOIN TM_REGION J ON J.REGION_CODE = A.CP_PROVINCE_ID\r\n" );
		sql.append("  LEFT JOIN TC_CODE I ON I.CODE_ID = A.CP_BIZ_CONTENT\r\n" );
		sql.append("  LEFT JOIN (SELECT T.ORG_ID, T.ORG_NAME FROM TM_ORG T WHERE T.ORG_LEVEL = 2 UNION SELECT ORG_ID, ORG_NAME FROM TM_ORG_CUSTOM) K ON K.ORG_ID = A.CP_DEAL_ORG\r\n" );
		sql.append("  LEFT JOIN (SELECT DR.CP_ID, MAX(DECODE(DR.CP_STATUS,"+Constant.COMPLAINT_STATUS_DOING_ALREADY+",DR.CD_DATE,NULL)) BACK_DATE\r\n" );
		sql.append("    FROM TT_CRM_COMPLAINT_DEAL_RECORD DR GROUP BY DR.CP_ID) X ON X.CP_ID = A.CP_ID\r\n" );
		sql.append("\r\n" );
		sql.append("  WHERE A.CP_BIZ_TYPE = "+Constant.TYPE_COMPLAIN+"\r\n" );
		sql = appendDate(sql, dateStart, dateEnd, 4);
		// sql = appendSpecilWhere(sql, dateYear, tmSmallOrg, dealName, cpObject);
		sql.append(" GROUP BY rollup("+typeValue+")");
//		sql.append("having (grouping("+typeValue+") = 0 and "+typeValue+" <> 2013070519381899) or grouping("+typeValue+") = 1");
        System.out.println("-----"+sql.toString()+"-----");
		return sql.toString();
	}
	
	private StringBuffer appendDecodeRate(StringBuffer sql,String sum,String totalSum){
		sql.append(" case when "+totalSum+" <> 0 then ");
		sql.append(" decode(instr(TO_CHAR(ROUND( " +sum+ "/"+totalSum+" * 100,4)), ");
		sql.append(" '.'),1,0||TO_CHAR(ROUND( " +sum+ "/"+totalSum+" * 100,4)), ");
		sql.append(" TO_CHAR(ROUND(" +sum+ "/"+totalSum+" * 100,4)) ) ");
		sql.append(" else '0' end ");
		return sql;
	}
	
	// 艾春 2013.12.2 添加平均时长计算公用方法
	private StringBuffer appendDecodeAvg(StringBuffer sql,String sum,String totalSum){
		sql.append(" case when "+totalSum+" <> 0 then ");
		sql.append(" decode(instr(TO_CHAR(ROUND( " +sum+ "/"+totalSum+" * 100,4)), ");
		sql.append(" '.'),1,0||TO_CHAR(ROUND( " +sum+ "/"+totalSum+" * 100,4)), ");
		sql.append(" TO_CHAR(ROUND(" +sum+ "/"+totalSum+",2)) ) ");
		sql.append(" else '0' end ");
		return sql;
	}
	

	private StringBuffer appendSpecilWhere(StringBuffer sql,String dateYear,String tmSmallOrg,String dealName,
			String cpObject){
		if(StringUtil.notNull(dateYear)){
			sql.append(" and Extract(year from a.cp_acc_date) = '"+dateYear+"' \r\n");
		}
		if(StringUtil.notNull(tmSmallOrg)){
			sql.append(" and a.CP_PROVINCE_ID = '"+tmSmallOrg+"' \r\n");
		}
		if(StringUtil.notNull(dealName)){
			sql.append(" and f.name like '%"+dealName+"%' \r\n");
		}
		
		if(StringUtil.notNull(cpObject)){
			sql.append(" and (h.dealer_name like '%"+cpObject+"%' or r.org_name like '%"+cpObject+"%' ) \r\n");
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
//			sql.append(" and ((a.CP_TURN_DATE < to_date('"+dateStart+"','yyyy-MM-dd') " );
//			sql.append("      and ((a.cp_cl_date is null and (a.CP_TURN_DATE+a.cp_limit) > to_date('"+dateEnd+" 23:59:59','yyyy-MM-dd hh24:mi:ss')) or" );
//			sql.append("           (a.cp_cl_date is not null and a.cp_cl_date > to_date('"+dateEnd+" 23:59:59','yyyy-MM-dd hh24:mi:ss')))) " );
//			// 艾春 13.11.23 修改 需考核时间
			sql.append("      and ((a.CP_TURN_DATE <= to_date('"+dateEnd+" 23:59:59','yyyy-MM-dd hh24:mi:ss') AND " );
			sql.append("        ((a.cp_cl_date is null and (a.CP_TURN_DATE+NVL(a.cp_limit,0)) <= to_date('"+dateEnd+" 23:59:59','yyyy-MM-dd hh24:mi:ss') and (a.CP_TURN_DATE+NVL(a.cp_limit,0)) >= to_date('"+dateStart+"','yyyy-MM-dd')) " );
			sql.append("          or (a.cp_cl_date is not null and a.cp_cl_date <= to_date('"+dateEnd+" 23:59:59','yyyy-MM-dd hh24:mi:ss') and a.cp_cl_date >= to_date('"+dateStart+"','yyyy-MM-dd'))))) ");
//			sql.append("      or(a.CP_TURN_DATE >= to_date('"+dateStart+"','yyyy-MM-dd') and a.CP_TURN_DATE <= to_date('"+dateEnd+" 23:59:59','yyyy-MM-dd hh24:mi:ss') AND\r\n" );
//			sql.append("        ((a.cp_cl_date is null and (a.CP_TURN_DATE+a.cp_limit) <= to_date('"+dateEnd+" 23:59:59','yyyy-MM-dd hh24:mi:ss'))\r\n" );
//			sql.append("          or (a.cp_cl_date is not null and a.cp_cl_date <= to_date('"+dateEnd+" 23:59:59','yyyy-MM-dd hh24:mi:ss')))))");
			// 艾春 13.11.23 修改 需考核时间
		}else if(dateType == 1){
			sql.append(" and a.cp_acc_date >= to_date('"+dateStart+"','yyyy-MM-dd')  ");
			sql.append(" and a.cp_acc_date <= to_date('"+dateEnd+" 23:59:59','yyyy-MM-dd hh24:mi:ss') ");
		}else if(dateType == 2){
			sql.append(" and (a.CP_TURN_DATE < to_date('"+dateStart+"','yyyy-MM-dd') " );
			sql.append(" and ((a.cp_cl_date is null and (a.CP_TURN_DATE+NVL(a.cp_limit,0)) >= to_date('"+dateStart+"','yyyy-MM-dd')) " );
			sql.append(" or (a.cp_cl_date is not null and a.cp_cl_date >= to_date('"+dateStart+"','yyyy-MM-dd') ) ) ) ");
		}else if(dateType == 3){
			sql.append(" and a.CP_TURN_DATE <= to_date('"+dateEnd+" 23:59:59','yyyy-MM-dd hh24:mi:ss')\r\n" );
			sql.append(" and ((a.cp_cl_date is null and (a.CP_TURN_DATE+NVL(a.cp_limit,0)) > to_date('"+dateEnd+" 23:59:59','yyyy-MM-dd hh24:mi:ss')) " );
			sql.append(" or (a.cp_cl_date is not null and a.cp_cl_date > to_date('"+dateEnd+" 23:59:59','yyyy-MM-dd hh24:mi:ss'))) ");
		}else if(dateType == 4){
			// 艾春 13.11.23 修改需考核总时间控制
			sql.append("and (1=1 and ((a.CP_TURN_DATE <= to_date('"+dateEnd+" 23:59:59','yyyy-MM-dd hh24:mi:ss') " );
			sql.append("and ((a.cp_cl_date is null and (a.CP_TURN_DATE+NVL(a.cp_limit,0)) >= to_date('"+dateStart+"','yyyy-MM-dd')) " );
			sql.append(" or (a.cp_cl_date is not null and a.cp_cl_date >= to_date('"+dateStart+"','yyyy-MM-dd'))) ");
			sql.append("   ) or ( 1=1  and a.cp_acc_date >= to_date('"+dateStart+"','yyyy-MM-dd')\n" );
			sql.append("     and a.cp_acc_date <= to_date('"+dateEnd+" 23:59:59','yyyy-MM-dd hh24:mi:ss')))) ");
			// 艾春 13.11.23 修改需考核总时间控制

		}else if(dateType == 5){
			sql.append("and a.cp_acc_date >= to_date('"+dateStart+"','yyyy-MM-dd') " );
			sql.append("and a.cp_acc_date <= to_date('"+dateEnd+" 23:59:59','yyyy-MM-dd hh24:mi:ss') " );
			sql.append("and (a.cp_turn_date is null OR A.CP_TURN_DATE > to_date('"+dateEnd+" 23:59:59','yyyy-MM-dd hh24:mi:ss')) ");
		}else if(dateType == 6){
			sql.append("and a.CP_TURN_DATE < to_date('"+dateStart+"','yyyy-MM-dd') " );
			sql.append("and ((a.cp_cl_date is null and (a.CP_TURN_DATE+NVL(a.cp_limit,0)) > to_date('"+dateEnd+" 23:59:59','yyyy-MM-dd hh24:mi:ss')) " );
			sql.append(" or (a.cp_cl_date is not null and a.cp_cl_date > to_date('"+dateEnd+" 23:59:59','yyyy-MM-dd hh24:mi:ss')) ) ");
		}else if(dateType == 7)
		{
			sql.append("      and ((a.CP_TURN_DATE <= to_date('"+dateEnd+" 23:59:59','yyyy-MM-dd hh24:mi:ss') AND " );
			sql.append("        ((a.cp_cl_date is null and (a.CP_TURN_DATE+NVL(a.cp_limit,0)) <= to_date('"+dateEnd+" 23:59:59','yyyy-MM-dd hh24:mi:ss') and (a.CP_TURN_DATE+NVL(a.cp_limit,0)) >= to_date('"+dateStart+"','yyyy-MM-dd')) " );
			sql.append("          or (a.cp_cl_date is not null and a.cp_cl_date <= to_date('"+dateEnd+" 23:59:59','yyyy-MM-dd hh24:mi:ss') and a.cp_cl_date >= to_date('"+dateStart+"','yyyy-MM-dd'))))) "); 
		}else if(dateType == 8)
		{	
			// 艾春 2013.12.1添加 当月前受理当月转交当月考核数
			sql.append("      and ((a.cp_acc_date < to_date('"+dateStart+"','yyyy-MM-dd') AND a.CP_TURN_DATE >= to_date('"+dateStart+"','yyyy-MM-dd') and " );
			sql.append("        ((a.cp_cl_date is null and (a.CP_TURN_DATE+NVL(a.cp_limit,0)) <= to_date('"+dateEnd+" 23:59:59','yyyy-MM-dd hh24:mi:ss') and (a.CP_TURN_DATE+NVL(a.cp_limit,0)) >= to_date('"+dateStart+"','yyyy-MM-dd')) " );
			sql.append("          or (a.cp_cl_date is not null and a.cp_cl_date <= to_date('"+dateEnd+" 23:59:59','yyyy-MM-dd hh24:mi:ss') and a.cp_cl_date >= to_date('"+dateStart+"','yyyy-MM-dd'))))) "); 
		}else if(dateType == 9)
		{	
			// 艾春 2013.12.1添加 当月前受理当月转交下月考核数
			sql.append("      and ((a.cp_acc_date < to_date('"+dateStart+"','yyyy-MM-dd') AND a.CP_TURN_DATE >= to_date('"+dateStart+"','yyyy-MM-dd') and " );
			sql.append("        ((a.cp_cl_date is null and (a.CP_TURN_DATE+NVL(a.cp_limit,0)) > to_date('"+dateEnd+" 23:59:59','yyyy-MM-dd hh24:mi:ss')) " );
			sql.append("          or (a.cp_cl_date is not null and a.cp_cl_date > to_date('"+dateEnd+" 23:59:59','yyyy-MM-dd hh24:mi:ss'))))) "); 
		}
		
		return sql;
	}
	
	
	

	
	//查询投诉总量
	public int getTotalComplaintDealReportforms(String year,String tmSmallOrg,String dealName,String dateStart,String dateEnd,String cpObject){
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("select count(*) VAL\r\n");
		sbSql.append("  FROM TT_CRM_COMPLAINT A\r\n" );
		sbSql.append("  LEFT JOIN TC_USER F ON A.CP_ACC_USER = F.USER_ID\r\n" );
		sbSql.append("  LEFT JOIN TM_DEALER H ON A.CP_OBJECT = H.DEALER_ID\r\n" );
		sbSql.append("  LEFT JOIN (SELECT T.ORG_ID, T.ORG_NAME FROM TM_ORG T WHERE T.ORG_LEVEL = 2 UNION SELECT ORG_ID, ORG_NAME FROM TM_ORG_CUSTOM) R ON A.CP_OBJECT = R.ORG_ID\r\n" );
		sbSql.append(" where a.CP_BIZ_TYPE = "+Constant.TYPE_COMPLAIN+" \r\n");
		sbSql = appendDate(sbSql, dateStart, dateEnd, 4);
		sbSql = appendSpecilWhere(sbSql, year, tmSmallOrg, dealName, cpObject);

		return Integer.parseInt(this.pageQueryMap(sbSql.toString(),
			null,
			this.getFunName()).get("VAL").toString());
	}
	


}
