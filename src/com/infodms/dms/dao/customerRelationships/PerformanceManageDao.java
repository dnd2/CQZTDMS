package com.infodms.dms.dao.customerRelationships;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TtCrmComplaintRulePO;
import com.infodms.dms.po.TtCrmExamDetailPO;
import com.infodms.dms.po.TtCrmExamPO;
import com.infodms.dms.po.TtCrmQueAnswerPO;
import com.infodms.dms.po.TtCrmReturnRulePO;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

public class PerformanceManageDao  extends BaseDao {

	private static final PerformanceManageDao dao = new PerformanceManageDao();
	private static final Object[][] String = null;
	public static final PerformanceManageDao getInstance() {
		return dao;
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> complaintAssessRuleInfo(AclUserBean logonUser)
	{
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT RR.CU_ID,RR.CU_ITEM,RR.CU_LEVEL,RR.CU_WEIGHT,RR.MIN_CU_TARGET, RR.MAX_CU_TARGET , tc.code_desc item_name ,tc.code_id code_id_1 , l.code_id code_id_2,l.code_desc culevel FROM TT_CRM_COMPLAINT_RULE RR  ");
		sql.append(" join tc_code tc on tc.code_id = rr.cu_item ");
		sql.append(" join tc_code l on l.code_id = rr.cu_level ");
		sql.append(" order by rr.cu_level , rr.cu_item");
		List<Map<String, Object>> list= pageQuery(sql.toString(), null, getFunName());
		return list;
	}
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> reviewAssessRuleInfo(AclUserBean logonUser)
	{
		StringBuilder sql = new StringBuilder();
		sql.append(" select * from ( \n");
		sql.append(" select b.code_desc item_desc,b.code_id item_id from \n");
		sql.append(" (select * from tc_code where type=9535) b ) d \n");
		sql.append(" left join TT_CRM_RETURN_RULE c on  c.ru_item = d.item_id \n");
		sql.append(" order by d.item_id");
		List<Map<String, Object>> list= pageQuery(sql.toString(), null, getFunName());
		return list;
	}
	
	/**
	 * 新增-更新投诉绩效规则
	 * @param targets
	 * @param weights
	 * @param logonUser
	 */
	public void saveComplaintRule(String[]levels,String[]items,String[]mintarget,String[] maxtarget,String[]weights,AclUserBean logonUser){
		TtCrmComplaintRulePO po = new TtCrmComplaintRulePO();
		Map<String,Object> num = isExist("complaint");
		if(mintarget!=null&&mintarget.length>0){
			if(num!=null && Integer.parseInt(num.get("NUM").toString())==0){//新增
				for(int i=0;i<mintarget.length;i++){
					po.setCuId(Long.parseLong(SequenceManager.getSequence("")));
					if(levels!=null && levels.length>0){
						po.setCuLevel(Integer.parseInt(levels[i].toString()));
					}
					if(items!=null && items.length>0){
						po.setCuItem(Integer.parseInt(items[i].toString()));
					}
					po.setMinCuTarget(Double.parseDouble(mintarget[i].toString()));
					po.setMaxCuTarget(Double.parseDouble(maxtarget[i].toString()));
					po.setCuWeight(Double.parseDouble(weights[i].toString()));
					po.setCreateBy(logonUser.getUserId());
					po.setCreateDate(new Date());
					dao.insert(po);
				}
			}
		}
	}
	
	/**
	 * 验证表中是否已有规则
	 * @param type
	 * @return
	 */
	public Map<String,Object> isExist(String type){
		Map<String,Object>map = null;
		StringBuffer sql = new StringBuffer();
		if("complaint".equals(type)){
			sql.append(" select count(*) num from tt_crm_complaint_rule");
		}else if("review".equals(type)){
			sql.append(" select count(*) num from tt_crm_return_rule");
		}
		map = pageQueryMap(sql.toString(),null,getFunName());
		return map;
	}
	/**
	 * 回访考核规则保存
	 * @param targets
	 * @param weights
	 * @param logonUser
	 */
	public void saveReviewRule(String[]levels,String[]items,String[]targets,String[]weights,AclUserBean logonUser,String[]ids){
		TtCrmReturnRulePO po = new TtCrmReturnRulePO();
		Map<String,Object> num = isExist("review");
		if(targets!=null&&targets.length>0){
			for(int i=0;i<targets.length;i++){
				po.setRuId(Long.parseLong(SequenceManager.getSequence("")));
//					if(levels!=null && levels.length>0){
//						po.setRuLevel(Integer.parseInt(levels[i].toString()));
//					}
				if(items!=null && items.length>0){
					po.setRuItem(Integer.parseInt(items[i].toString()));
				}
				po.setRuTarget(Double.parseDouble(targets[i].toString()));
				po.setRuWeight(Double.parseDouble(weights[i].toString()));
				po.setCreateBy(logonUser.getUserId());
				po.setCreateDate(new Date());
				dao.insert(po);
			}
		}
	}
	/**
	 * 查询考核等级
	 * @return
	 */
	public List<Map<String,Object>> getAssessLevel(){
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from tc_code where type = 9537 order by code_id");
		List<Map<String,Object>> result = dao.pageQuery(sql.toString(), null, getFunName());
		return result;
	}
	/**
	 * 查询投诉考核项目
	 * @return
	 */
	public List<Map<String,Object>> getComplaintAssessItem(){
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from tc_code where type = 9534 order by code_id");
		List<Map<String,Object>> result = dao.pageQuery(sql.toString(), null, getFunName());
		return result;
	}
	/**
	 * 查询回访考核项目
	 * @return
	 */
	public List<Map<String,Object>> getReviewAssessItem(){
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from tc_code where type = 9535 order by code_id");
		List<Map<String,Object>> result = dao.pageQuery(sql.toString(), null, getFunName());
		return result;
	}
}
