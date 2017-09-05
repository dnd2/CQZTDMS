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

public class SettingDao extends BaseDao{

	
public static Logger logger = Logger.getLogger(SettingDao.class);
	
	public static SettingDao dao = new SettingDao();
	public static POFactory factory = POFactoryBuilder.getInstance();
	public static SettingDao getInstance(){
		return dao;
	}
	
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}

	public PageResult<Map<String, Object>> getSettingMessage(Map<String, Object> map,int pageSize,int curPage){
		String questionClass_set = (String) map.get("questionClass_set");
		String isCommon_set = (String) map.get("isCommon_set");
		String questionDescr_set = (String) map.get("questionDescr_set");
		String[] des = questionDescr_set.split(" ");
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
		sql.append("   and (tq.question_status = "+Constant.QUETION_STATUS_3+" or tq.question_status = "+Constant.QUETION_STATUS_4+")\n");
		if(Utility.testString(questionClass_set)){
			sql.append("   and tq.question_type = "+questionClass_set+"\n");  
		}
		if(Utility.testString(isCommon_set)){
			sql.append("   and tq.is_common = "+isCommon_set+"\n");  
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
	
	/**********新增数据************/
	public static void inserttqpValue(TtQuestionPO po){
		factory.insert(po);
	}
	
	

	/**********新增数据************/
	public static void updateValue(TtQuestionPO poKey,TtQuestionPO poValue){
		factory.update(poKey, poValue);
	}
	
	public List<Map<String, Object>> getCommonSetMessages(Map<String, String> map){
		List<Map<String, Object>> list = null;
		String QID = map.get("QID");
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
			sql.append("     and tq.question_id="+QID+"\n"); 
		}
		list = super.pageQuery(sql.toString(), null, getFunName());
		return list;
	}
}
