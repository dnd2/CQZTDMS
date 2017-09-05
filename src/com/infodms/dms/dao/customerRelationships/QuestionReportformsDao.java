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
public class QuestionReportformsDao extends BaseDao{

	private static final QuestionReportformsDao dao = new QuestionReportformsDao();
	
	public static final QuestionReportformsDao getInstance() {
		return dao;
	}
	
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		return null;
	}

	
	
	public PageResult<Map<String,Object>> queryQuestionReportforms(String qrId,String dateStart,String dateEnd,int pageSize,int curPage){

		String sql  = returnSql(qrId,dateStart,dateEnd);
		return (PageResult<Map<String, Object>>)this.pageQuery(sql.toString(),
				null,
				this.getFunName(),
				pageSize,
				curPage);
	} 
	
	
	public List<Map<String,Object>> queryQuestionReportformsList(String qrId,String dateStart,String dateEnd){

		String sql  = returnSql(qrId,dateStart,dateEnd);
		return (List<Map<String, Object>>)this.pageQuery(sql.toString(),
				null,
				this.getFunName());
	}
	

	

	private String returnSql(String qrId,String dateStart,String dateEnd){
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("select max(t.QUESTION) QUESTION, t.ANSWER ANSWER, sum(t.TOTAL) TOTAL\r\n");
		sbSql.append("  from (select a.qd_question QUESTION,\r\n");
		sbSql.append("               b.qd_que_answer ANSWER,\r\n");
		sbSql.append("               count(*) TOTAL\r\n");
		sbSql.append("          from TT_CRM_QUE_DETAIL a\r\n");
		sbSql.append("          left join TT_CRM_QUE_ANSWER b\r\n");
		sbSql.append("            on a.qr_id = b.qr_id\r\n");
		sbSql.append("           and a.qd_no = b.qd_no"); 

		sbSql.append("  where a.qr_id = "+qrId+"\r\n");
		if(StringUtil.notNull(dateStart)){
			sbSql.append(" and b.RD_DATE >= to_date('"+dateStart+"','yyyy-MM-dd') \r\n");
		}
		if(StringUtil.notNull(dateEnd)){
			sbSql.append(" and b.RD_DATE <= to_date('"+dateEnd+" 23:59:59','yyyy-MM-dd hh24:mi:ss')\r\n");
		}
		sbSql.append("\r\n");
		sbSql.append("		  group by a.qd_question, b.qd_que_answer\r\n");
		sbSql.append("        order by a.qd_question, b.qd_que_answer) t\r\n");
		sbSql.append("group by rollup(t.ANSWER)"); 


		return sbSql.toString();
	}
	
	public List<Map<String,Object>> getQuestionList(){
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("select distinct a.qr_name, a.qr_id\r\n");
		sbSql.append("  from TT_CRM_QUESTIONNAIRE a\r\n");
        sbSql.append("  where A.QR_STATUS = "+Constant.QR_STATUS_1); 

		return (List<Map<String, Object>>)this.pageQuery(sbSql.toString(),
				null,
				this.getFunName());
	}

}
