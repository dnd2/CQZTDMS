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

public class CommonQuestionDao extends BaseDao{
public static Logger logger = Logger.getLogger(CommonQuestionDao.class);
	
	public static CommonQuestionDao dao = new CommonQuestionDao();
	public static POFactory factory = POFactoryBuilder.getInstance();
	public static CommonQuestionDao getInstance(){
		return dao;
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}


	/*************常规问题查询，仅能查询出标示为“是”常规问题的数据，状态为“已回答”***************/
	
	public PageResult<Map<String, Object>> getCommonQuestions(Map<String, String> map,int pageSize,int curPage){
		PageResult<Map<String, Object>> ps = null;
		String questionClass_common = map.get("questionClass_common");
		String questionDesc_common = map.get("questionDesc_common");
		int len = 0;
		
		String[] des = null ;
		
		if(Utility.testString(questionDesc_common)){
			des = questionDesc_common.split(" "); 
			len = des.length;
		}
		
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
		sql.append(" and tq.is_common = " + Constant.IF_TYPE_YES + "\n");
		if (Utility.testString(questionClass_common)) {
			sql.append(" and tq.question_type = " + questionClass_common + "\n");
		}
		sql.append(" and tq.question_status = " + Constant.QUETION_STATUS_2 + "\n");
		
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
	
	/****************得到常规问题明细数据*********************/
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
