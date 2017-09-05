package com.infodms.dms.dao.help;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.common.Constant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.dao.common.BaseDao;
import com.infoservice.po3.POFactory;
import com.infoservice.po3.POFactoryBuilder;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

public class QuestionSearchDao extends BaseDao{
	public static Logger logger = Logger.getLogger(QuestionSearchDao.class);
	private static POFactory factory = POFactoryBuilder.getInstance();
	public static QuestionSearchDao dao = new QuestionSearchDao();
	public static QuestionSearchDao getInstance(){
		return dao;
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
	/*************查询已经保存了的数据***************/
	
	public PageResult<Map<String, Object>> getSaveQuestions(Map<String, String> map,int pageSize,int curPage){
		String questionClass__search = (String) map.get("questionClass__search");
		String isCommon__search = (String) map.get("isCommon__search");
		String satatus_search = (String) map.get("satatus_search");
		String questionDescr_search = (String) map.get("questionDescr_search");
		String[] des = questionDescr_search.split(" ");
		int len =des.length;
		PageResult<Map<String, Object>> ps = null;
		StringBuffer sql = new StringBuffer();
		sql.append("select tq.question_id,\n");
		sql.append("       tq.question_type,\n");  
		sql.append("       tq.question_module,\n");  
		sql.append("       tq.question_no,\n");  
		sql.append("       tq.question_describe,\n");  
		sql.append("       tq.answer,\n");  
		sql.append("       tq.answer_by,\n");  
		sql.append("       tq.question_status,\n");  
		sql.append("       tq.is_common,\n");  
		sql.append("       tq.create_by,\n");  
		sql.append("       tq.create_date,\n");  
		sql.append("       tq.update_by,\n");  
		sql.append("       tq.update_date,\n");  
		sql.append("       rownum \n"); 
		sql.append("  from tt_question tq\n");  
		sql.append(" where 1 = 1\n");  
		//sql.append("   and tq.question_status = "+Constant.QUETION_STATUS_2+" or tq.question_status = "+Constant.QUETION_STATUS_3+" or tq.question_status = "+Constant.QUETION_STATUS_4+"\n");
		sql.append("   and tq.question_status not in( "+Constant.QUETION_STATUS_1+")\n");
		if(Utility.testString(questionClass__search)){
			sql.append("   and tq.question_type = "+questionClass__search+"\n");  
		}
		if(Utility.testString(isCommon__search)){
			sql.append("   and tq.is_common = "+isCommon__search+"\n");  
		}
		if(Utility.testString(satatus_search)){
			sql.append("   and tq.question_status = "+satatus_search+"\n");  
		}
		for (int i = 0; i < len; i++) {
			if (i == 0) {
				sql.append(" and (tq.question_describe like'%" + des[i] + "%' \n");
			} else {
				sql.append(" or tq.question_describe like'%" + des[i] + "%' \n");
			}

			if (i == len - 1) {
				sql.append(" ) \n");
			}
		}
		
		ps = super.pageQuery(sql.toString(), null, getFunName(),pageSize, curPage);
		return ps;
	}
	
	
	

	/****************得到问题查询（车厂）明细数据*********************/
	public List<Map<String, Object>> detail(Map<String, String> map){
		List<Map<String, Object>> list = null;
		String QID = (String) map.get("QID");
		StringBuffer sql = new StringBuffer();
		
		sql.append("select tq.question_id,\n");
		sql.append("      case  tq.question_type when "+Constant.QUETION_TYPE_1+" then '销售问题' else '售后问题' end question_type,\n");  
		sql.append("       tq.question_module,\n");  
		sql.append("       tq.question_no,\n");  
		sql.append("       tq.question_describe,\n");  
		sql.append("       tq.answer,\n");  
		sql.append("       tq.answer_by,\n");  
		sql.append("       tq.question_status,\n");  
		sql.append("       tq.is_common,\n");  
		sql.append("       tq.create_by,\n");  
		sql.append("       tq.create_date,\n");  
		sql.append("       tq.update_by,\n");  
		sql.append("       tq.update_date,\n");  
		sql.append("       rownum \n"); 
		sql.append("  from tt_question tq\n");  
		sql.append(" where 1 = 1\n");
		if(Utility.testString(QID)){
			sql.append(" and tq.question_id = " + QID + "\n");
		}
		list = super.pageQuery(sql.toString(), null, getFunName());
		return list;
	} 
}
