package com.infodms.dms.dao.customerRelationships;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.util.StringUtil;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;



@SuppressWarnings("unchecked")
public class YawpAnswerReportformsDao extends BaseDao{

	private static final YawpAnswerReportformsDao dao = new YawpAnswerReportformsDao();
	
	public static final YawpAnswerReportformsDao getInstance() {
		return dao;
	}
	
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		return null;
	}

	
	
	public PageResult<Map<String,Object>> queryYawpAnswerReportforms(String dealName,String dateStart,String dateEnd,String modeId,int pageSize,int curPage){

		String sql  = returnSql(dealName,dateStart,dateEnd,modeId);
		return (PageResult<Map<String, Object>>)this.pageQuery(sql.toString(),
				null,
				this.getFunName(),
				pageSize,
				curPage);
	} 
	
	
	public List<Map<String,Object>> queryYawpAnswerReportformsList(String dealName,String dateStart,String dateEnd,String modeId){

		String sql  = returnSql(dealName,dateStart,dateEnd,modeId);
		return (List<Map<String, Object>>)this.pageQuery(sql.toString(),
				null,
				this.getFunName());
	}
	

	

	private String returnSql(String dealName,String dateStart,String dateEnd,String modelId){
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("select distinct b.qd_que_answer ANSWER, count(*) TOTAL\r\n");
		sbSql.append("  from TT_CRM_QUE_DETAIL a\r\n");
		sbSql.append("  left join TT_CRM_QUE_ANSWER b\r\n");
		sbSql.append("    on a.qr_id = b.qr_id\r\n");
		sbSql.append("   and a.qd_no = b.qd_no\r\n");
		sbSql.append("  left join tc_user c on b.rd_user_id = c.user_id\r\n");
		sbSql.append("  left join tt_crm_return_visit d on d.qr_id = a.qr_id\r\n");
		sbSql.append("  left join tm_vehicle e on d.vin = e.vin\r\n");
		sbSql.append(" where a.qd_question like '%不满意%'\r\n");
		if(StringUtil.notNull(dealName)){
			sbSql.append(" and c.name like '%"+dealName+"%' \r\n");
		}
		if(StringUtil.notNull(dateStart)){
			sbSql.append(" and b.RD_DATE >= to_date('"+dateStart+"','yyyy-MM-dd') \r\n");
		}
		if(StringUtil.notNull(dateEnd)){
			sbSql.append(" and b.RD_DATE <= to_date('"+dateEnd+" 23:59:59','yyyy-MM-dd hh24:mi:ss')\r\n");
		}
		if(StringUtil.notNull(modelId)){
			sbSql.append(" and e.model_id = "+modelId+"\r\n");
		}
		sbSql.append("\r\n");
		sbSql.append(" group by a.qr_id, a.qd_no, b.qd_que_answer"); 

		return sbSql.toString();
	}

}
