package com.infodms.dms.dao.help;

import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.common.Constant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TtAsWrApplicationPO;
import com.infodms.dms.po.TtQuestionPO;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.po3.POFactory;
import com.infoservice.po3.POFactoryBuilder;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

public class QuestionReportedDao extends BaseDao{
	public static Logger logger = Logger.getLogger(QuestionReportedDao.class);
	private static POFactory factory = POFactoryBuilder.getInstance();
	public static QuestionReportedDao dao = new QuestionReportedDao();
	public static QuestionReportedDao getInstance(){
		return dao;
	}
	protected PO wrapperPO(ResultSet rs, int idx) {
		return null;
	}
	public List<Map<String, Object>> getStatusTarget(){
		List<Map<String, Object>> list = null;
		StringBuffer sql = new StringBuffer();
		sql.append("select tq.question_id,\n");
		sql.append("       case tq.question_type\n");  
		sql.append("         when "+Constant.QUETION_TYPE_1+" then\n");  
		sql.append("          '销售问题'\n");  
		sql.append("         else\n");  
		sql.append("          '售后问题'\n");  
		sql.append("       end question_type,\n");  
		sql.append("       case tq.question_status\n");  
		sql.append("         when "+Constant.QUETION_STATUS_1+" then\n");  
		sql.append("          '已保存'\n");  
		sql.append("         when "+Constant.QUETION_STATUS_2+" then\n");  
		sql.append("          '已提交'\n");  
		sql.append("         when "+Constant.QUETION_STATUS_3+" then\n");  
		sql.append("          '已回答'\n");  
		sql.append("         else\n");  
		sql.append("          '已取消'\n");  
		sql.append("       end question_status,\n");  
		sql.append("       case tq.is_common\n");  
		sql.append("         when "+Constant.IF_TYPE_YES+" then\n");  
		sql.append("          '是'\n");  
		sql.append("         else\n");  
		sql.append("          '否'\n");  
		sql.append("       end is_common,\n");  
		sql.append("       case tqc.operate_type\n");  
		sql.append("         when "+Constant.OPERATE_TYPE_1+" then\n");  
		sql.append("          '取消'\n");  
		sql.append("         else\n");  
		sql.append("          '回答变更'\n");  
		sql.append("       end operate_type\n");  
		sql.append("  from tt_question tq, tt_question_change tqc\n");  
		sql.append(" where tq.question_id = tqc.question_id\n");

		list = super.pageQuery(sql.toString(), null, getFunName());
		return list;
	}
	
	public PageResult<Map<String, Object>> getDetaliQuestionMessage(Map<String, String> map,int pageSize,int curPage){
		PageResult<Map<String, Object>> ps = null;
		String questionClass = map.get("questionClass");
		String questionDesc = map.get("questionDesc");
		String answerBy = map.get("answerBy");
		int len = 0;
		
		String[] des = null ;
		
		if(Utility.testString(questionDesc)){
			des = questionDesc.split(" "); 
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
		if (Utility.testString(questionClass)) {
			sql.append(" and tq.question_type = " + questionClass + "\n");
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
	
	public static void insertTq(TtQuestionPO po){
		factory.insert(po);
	}
	public List<Map<String, Object>> getReportedDetail(Map<String, String> map){
		List<Map<String, Object>> list = null;
		String QID=map.get("QID");
		StringBuffer sql = new StringBuffer();
		sql.append("select tq.question_id,\n");
		sql.append("       case tq.question_type when "+Constant.QUETION_TYPE_1+" then '销售问题' else '售后问题' end question_type,\n");  
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
			sql.append("   and tq.question_id = "+QID+"\n");
		}
		
		list = super.pageQuery(sql.toString(), null, getFunName());
		return list;
	}
	
	
	/********从数据库中得到已保存问题数据*********/
	
	public PageResult<Map<String, Object>> getSaveUpdateData(Map<String, String> map,int pageSize,int curPage){
		PageResult<Map<String, Object>> ps = null;
		String questionClass_su = map.get("questionClass_su");
		String questionDesc_su = map.get("questionDesc_su");
		int len = 0;
		
		String[] des = null ;
		
		if(Utility.testString(questionDesc_su)){
			des = questionDesc_su.split(" "); 
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
		if (Utility.testString(questionClass_su)) {
			sql.append(" and tq.question_type = " + questionClass_su + "\n");
		}
		sql.append(" and tq.question_status = " + Constant.QUETION_STATUS_1 + "\n");
		
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
	
	
	
/********从数据库中得到已保存问题数据*********/
	
	public PageResult<Map<String, Object>> getReportedSearch(Map<String, String> map,int pageSize,int curPage){
		PageResult<Map<String, Object>> ps = null;
		String questionClass_rs = map.get("questionClass_rs");
		String questionDesc_rs = map.get("questionDesc_rs");
		int len = 0;
		
		String[] des = null ;
		
		if(Utility.testString(questionDesc_rs)){
			des = questionDesc_rs.split(" "); 
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
		if (Utility.testString(questionClass_rs)) {
			sql.append(" and tq.question_type = " + questionClass_rs + "\n");
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
	
	
	public void deleteReported(TtQuestionPO po){
		  this.delete(po);
	}
	
	/****************删除已经保存的数据*****************/
	public static void deleteTq(Map<String, String> map,List<TtQuestionPO> po){
		String QID = map.get("QID");
		//long qid = Long.parseLong("QID");
		StringBuffer sql = new StringBuffer();
		if(Utility.testString(QID)){
			sql.append("delete tt_question tq where  tq.question_id="+QID+"");
		}
		
		factory.delete(sql.toString(), po);
	}
}
