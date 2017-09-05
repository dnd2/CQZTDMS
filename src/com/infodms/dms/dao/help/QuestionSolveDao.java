package com.infodms.dms.dao.help;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.common.Constant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TtQuestionPO;
import com.infoservice.po3.POFactory;
import com.infoservice.po3.POFactoryBuilder;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

public class QuestionSolveDao extends BaseDao{
	public static Logger logger = Logger.getLogger(QuestionSolveDao.class);
	
	public static QuestionSolveDao dao = new QuestionSolveDao();
	public static POFactory factory = POFactoryBuilder.getInstance();
	public static QuestionSolveDao getInstance(){
		return dao;
	}
	protected PO wrapperPO(ResultSet rs, int idx) {
		return null;
	}
	public PageResult<Map<String, Object>> getQuestions(Map<String, String> map,int pageSize,int curPage){
		PageResult<Map<String, Object>> ps = null;
		String questionClass_solve = map.get("questionClass_solve");
		String questionDescr = map.get("questionDescr");
		String[] qdesrc = questionDescr.split(" ");
		int len = qdesrc.length;
		
		//String status_solve = map.get("status_solve");
		
		StringBuffer sql = new StringBuffer();
		sql.append("select tq.question_id,\n");
		sql.append("       tq.question_type,\n");  
		sql.append("       tq.question_module,\n");  
		sql.append("       tq.question_no,\n");  
		sql.append("       tq.question_describe,\n");  
		sql.append("       tq.question_status,\n");  
		sql.append("       tq.is_common,\n");  
		sql.append("       rownum \n"); 
		sql.append("  from tt_question tq\n");  
		sql.append(" where 1 = 1\n");
		if(Utility.testString(questionClass_solve)){
			sql.append(" and tq.question_type = "+questionClass_solve+"\n");
		}
			sql.append(" and tq.question_status ="+Constant.QUETION_STATUS_2+"\n");
			for (int i = 0; i < len; i++) {
				if (i == 0) {
					sql.append(" and (tq.question_describe like'%" + qdesrc[i] + "%' \n");
				} else {
					sql.append(" or tq.question_describe like'%" + qdesrc[i] + "%' \n");
				}

				if (i == len - 1) {
					sql.append(" ) \n");
				}
			}
		ps = super.pageQuery(sql.toString(), null, getFunName(),pageSize, curPage);
		return ps;
	}
	
	/***********得到问题明细信息************/
	
	public List<Map<String, Object>> detail(Map<String, String> mapdetail){
		List<Map<String, Object>> detail = null;
		String QId = mapdetail.get("QId");
		StringBuffer sql = new StringBuffer();
		sql.append("select tq.QUESTION_ID,tq.QUESTION_DESCRIBE,tq.ANSWER,tq.IS_COMMON,case tq.QUESTION_TYPE when "+Constant.QUETION_TYPE_1+" then '销售问题' else '售后问题' end QUESTION_TYPE\n");
		sql.append("  from tt_question tq\n");  
		sql.append(" where 1 = 1\n");
		if(Utility.testString(QId)){
			sql.append("and tq.question_id="+QId+"\n");
		}
		detail = super.pageQuery(sql.toString(), null, getFunName());
		return detail;
	}
	/*************更新问题******************/
	public void updateQuestion(Map<String, String> mapUpdate){
		String QId=mapUpdate.get("QId");
		String answerContent=mapUpdate.get("answerContent");
		String userId=mapUpdate.get("userId");
		
		StringBuffer sql = new StringBuffer();
		sql.append("update tt_question tq\n");
		if(Utility.testString(answerContent)){
			sql.append("   set tq.answer = '"+answerContent+"', \n");
		}
		if(Utility.testString(userId)){
			sql.append("   tq.answer_by = "+userId+"\n");
		} 
		sql.append(" where 1 = 1 \n");  
		if(Utility.testString(QId)){
			sql.append(" and where tq.question_id = "+QId+"\n");
		}
		super.update(sql.toString(), null);
	}
	/********************回答问题
	 * @return ************************/
	public static void updateAnswer(TtQuestionPO poKey,TtQuestionPO poValue){
		factory.update(poKey, poValue);
	}
}
