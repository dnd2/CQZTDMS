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
public class ConsultTypeReportformsDao extends BaseDao{

	private static final ConsultTypeReportformsDao dao = new ConsultTypeReportformsDao();
	
	public static final ConsultTypeReportformsDao getInstance() {
		return dao;
	}
	
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		return null;
	}
	
	public PageResult<Map<String,Object>> queryConsultTypeReportforms(String dealName,String dateStart,String dateEnd,int tatal,int pageSize,int curPage){
		String sql = returnConsultTypeReportformsSql(dealName, dateStart, dateEnd, tatal);

		return (PageResult<Map<String, Object>>)this.pageQuery(sql,
				null,
				this.getFunName(),
				pageSize,
				curPage);
	}
	
	public List<Map<String,Object>> queryConsultTypeReportformsList(String dealName,String dateStart,String dateEnd,int tatal){
		String sql = returnConsultTypeReportformsSql(dealName, dateStart, dateEnd, tatal);

		return (List<Map<String, Object>>)this.pageQuery(sql,
				null,
				this.getFunName());
	}
	
	private String returnConsultTypeReportformsSql(String dealName,String dateStart,String dateEnd,int tatal){
		StringBuffer sbSql = new StringBuffer();

		sbSql.append("SELECT ROWNUM ID, TYPENAME, BIZCONTENT, nvl(COUNTDESC,0) COUNTDESC, nvl(COUNTRATE,0) COUNTRATE\n");
		sbSql.append("  FROM (SELECT B.TYPE_NAME TYPENAME,\n"); 
		sbSql.append("               B.CODE_DESC BIZCONTENT,\n"); 
		sbSql.append("               COUNT(NVL(B.CODE_DESC, 0)) COUNTDESC,\n"); 
		sbSql.append("               DECODE(INSTR(TO_CHAR(ROUND(COUNT(NVL(B.CODE_DESC, 0)) / "+tatal+" * 100,4)),'.'),\n"); 
		sbSql.append("                      1,0 ||TO_CHAR(ROUND(COUNT(NVL(B.CODE_DESC, 0)) / "+tatal+" * 100, 4)),\n"); 
		sbSql.append("                      TO_CHAR(ROUND(COUNT(NVL(B.CODE_DESC, 0)) / "+tatal+" * 100, 4))) COUNTRATE\n"); 
		sbSql.append("          FROM TT_CRM_COMPLAINT A\n"); 
		sbSql.append("          LEFT JOIN TC_CODE B ON A.CP_BIZ_CONTENT = B.CODE_ID\n"); 
		sbSql.append("          LEFT JOIN TC_USER F ON A.CP_ACC_USER = F.USER_ID\n");
		sbSql.append("         where a.CP_BIZ_TYPE = "+Constant.TYPE_CONSULT+"\r\n");
		if(StringUtil.notNull(dealName)){
			sbSql.append(" and f.name like '%"+dealName+"%' \r\n");
		}

		if(StringUtil.notNull(dateStart)){
			sbSql.append(" and a.cp_acc_date >= to_date('"+dateStart+"','yyyy-MM-dd')\r\n");
		}
		if(StringUtil.notNull(dateEnd)){
			sbSql.append(" and a.cp_acc_date <= to_date('"+dateEnd+" 23:59:59','yyyy-MM-dd hh24:mi:ss')\r\n");
		}
		sbSql.append("         group by b.type_name, b.code_desc\r\n");
		sbSql.append("         order by 1, 3 desc)\r\n");
		sbSql.append("        union all\r\n");
		sbSql.append("        select "+tatal+",'合计', '', "+tatal+",'' \r\n");
		sbSql.append("          from dual \r\n");
		
		return sbSql.toString();
	}
	
	public int getTotalConsultTypeReportforms(String dealName,String dateStart,String dateEnd){
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("  select nvl(count(*),0) VAL \r\n");
		sbSql.append("                        from TT_CRM_COMPLAINT d\r\n");
		sbSql.append(" 						  left join tc_user e on d.cp_acc_user = e.user_id	\r\n");
		sbSql.append("                       where d.CP_BIZ_TYPE = "+Constant.TYPE_CONSULT+" \r\n");
		if(StringUtil.notNull(dealName)){
			sbSql.append(" and e.name like '%"+dealName+"%' \r\n");
		}

		if(StringUtil.notNull(dateStart)){
			sbSql.append(" and d.cp_acc_date >= to_date('"+dateStart+"','yyyy-MM-dd')\r\n");
		}
		if(StringUtil.notNull(dateEnd)){
			sbSql.append(" and d.cp_acc_date <= to_date('"+dateEnd+" 23:59:59','yyyy-MM-dd hh24:mi:ss')\r\n");
		}
		return Integer.parseInt(this.pageQueryMap(sbSql.toString(),
			null,
			this.getFunName()).get("VAL").toString());
	}
	


}
