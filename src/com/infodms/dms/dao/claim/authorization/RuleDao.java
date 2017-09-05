/**   
* @Title: RuleDao.java 
* @Package com.infodms.dms.dao.claim.authorization 
* @Description: TODO(授权规则维护DAO) 
* @author wangjinbao   
* @date 2010-6-12 上午09:59:01 
* @version V1.0   
*/
package com.infodms.dms.dao.claim.authorization;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.common.Constant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TtAsWrModelGroupPO;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

/** 
 * @ClassName: RuleDao 
 * @Description: TODO(授权规则维护DAO) 
 * @author wangjinbao 
 * @date 2010-6-12 上午09:59:01 
 *  
 */
public class RuleDao extends BaseDao {
	public static Logger logger = Logger.getLogger(RuleDao.class);
	private static final RuleDao dao = new RuleDao ();
	public static final RuleDao getInstance() {
		return dao;
	}
	/**
	 * 
	* @Title: getLevelList 
	* @Description: TODO(取所有的授权级别列表)包括：自动拒绝
	* @param @param  type : 是否区分“自动拒绝”，1：区分
	* @param @return   
	* @return List  
	* @throws
	 */
	@SuppressWarnings("unchecked")
	public List getLevelList(String type,Long oemCompanyId){
		StringBuffer sql= new StringBuffer();
		sql.append(" select tawa.approval_level_code,tawa.approval_level_name,tawa.approval_level_tier\n " );
		sql.append(" from tt_as_wr_authinfo tawa\n " );
		sql.append("where 1=1\n");
		sql.append(" and tawa.type="+Constant.AUDIT_TYPE_01+""); //技术室审核
		if("1".equals(type)){
			sql.append(" and tawa.approval_level_code<>100\n " );//此条件是区分是否是包括自动拒绝
		}
		sql.append(" order by tawa.approval_level_tier ");
		
		List<Map<String, Object>> list = pageQuery(sql.toString(), null,getFunName());
		return list;
	}
	/**
	 * 
	* @Title: ruleQuery 
	* @Description: TODO(授权规则查询方法:) 
	* @param @param pageSize
	* @param @param curPage
	* @param @param id             : 索赔车型组id
	* @param @return
	* @param @throws Exception   
	* @return PageResult<Map<String,Object>>  
	* @throws
	 */
	@SuppressWarnings("unchecked")
	public   PageResult<Map<String, Object>> ruleQuery(int pageSize, int curPage, String id,Long oemCompanyId) throws Exception {
		PageResult<Map<String, Object>> result = null;
		List list = dao.getLevelList("0",oemCompanyId);//获得授权级别列表
		StringBuffer sql= new StringBuffer();
		//查询列：从左至右：规则编号，索赔车型组代码，优先权值和对应的角色。
		sql.append(" select tawr.rule_element,tawmg.wrgroup_code,tawr.prior_level,\n" );
		sql.append(" tawr.role,tawmg.wrgroup_id \n" );
		//根据授权级别进行拼列，动态列，按照“，”打开列显示
		for(int i = 0 ; i < list.size(); i++){
			HashMap tcpo = (HashMap)list.get(i);
			sql.append(" ,CASE WHEN instr(tawr.role,'"+tcpo.get("APPROVAL_LEVEL_CODE")+"')>0 then '"+tcpo.get("APPROVAL_LEVEL_CODE")+"' else '' end \""+tcpo.get("APPROVAL_LEVEL_CODE")+"\"");
		}
		//索赔授权规则表TT_AS_WR_RULEMAPPING，索赔车型组表：tt_as_wr_model_group，根据车型组id关联
		sql.append(" from TT_AS_WR_RULEMAPPING tawr, tt_as_wr_model_group tawmg  where tawr.TYPE ="+Constant.AUDIT_TYPE_01+" and tawr.warranty_group= tawmg.wrgroup_id(+)\n " );
		if(Utility.testString(id)){
			sql.append(" and tawr.prior_level like '%"+id+"%'");
		}
		//按照索赔授权规则表的规则编号进行倒序排序
		sql.append(" order by tawr.rule_element desc " );
		result = (PageResult<Map<String, Object>>) pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
		//result = getRuleByRuleElement((PageResult<Map<String, Object>>) pageQuery(sql.toString(), null, getFunName(), pageSize, curPage));
		return result;
	}
	
	/**
	 * 
	* @Title: getRuleByRuleElement 
	* @Description: TODO(根据授权规则编号拼授权条件列RULE_DESC) 
	* @param @param rs
	* @param @return   
	* @return PageResult<Map<String,Object>>  
	* @throws
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	private  PageResult<Map<String, Object>> getRuleByRuleElement(PageResult<Map<String, Object>> rs) {
		if (rs.getRecords() != null) {
			List<Map<String, Object>> list = rs.getRecords(); // 取出授权规则列表
			String ruleDesc = null;
			StringBuffer sql= new StringBuffer();
			//查询列，从左至右依次：元素编号对应的tc_code解释，比较符对应的tc_code解释,元素值，布尔比较符
			sql.append(" select d.code_desc as ELEMENT_DESC, c.code_desc as COMPARISON_OP,a.ELEMENT_VALUE,a.BOOLEAN_COMPARISON,");
			//元素编号，规则编号
			sql.append(" a.ELEMENT_NO,a.RULE_NO,\n" );
			//元素位置
			sql.append(" a.ELEMENT_POSITION \n" );
			//索赔授权规则明细表：TT_AS_WR_RULEITEM，索赔授权规则表：TT_AS_WR_RULEMAPPING
			sql.append(" FROM TT_AS_WR_RULEITEM a,TT_AS_WR_RULEMAPPING b\n" );
			sql.append(" ,tc_code c,tc_code d\n" );
			//根据规则编号关联
			sql.append(" where a.RULE_NO = b.RULE_ELEMENT\n" );
			//比较符关联tc_code
			sql.append(" and a.comparison_op = c.code_id\n" );
			//元素编号关联tc_code
			sql.append(" and a.element_no = d.code_id\n " );
			
			for (int i = 0; i < list.size() && list != null; i++) {
				ruleDesc = "";
				//取每一条的规则
				HashMap rule = (HashMap)list.get(i);
				StringBuffer sql1= new StringBuffer();
				sql1.append(sql.toString());
				//根据规则编号拼授权条件列
				sql1.append(" and b.RULE_ELEMENT ='" + rule.get("RULE_ELEMENT") + "' ");
				//按照规则编号和元素位置进行排序
				sql1.append(" ORDER BY a.RULE_NO,a.ELEMENT_POSITION ");
				List<Map<String, Object>> ruleList = dao.pageQuery(sql1.toString(), null,getFunName());
				if(ruleList != null && ruleList.size() > 0){
					for(int j=0;j<ruleList.size();j++){
						HashMap temp = (HashMap)ruleList.get(j);
						if (j == 0) {
							//条件拼成：元素编号+比较符+元素值：
								ruleDesc +=temp.get("ELEMENT_DESC").toString()+temp.get("COMPARISON_OP").toString()
								           +temp.get("ELEMENT_VALUE").toString();
						} else {
							//条件拼成：空格+布尔比较符+空格+元素编号解释+比较符+元素值
							ruleDesc +=" "+temp.get("BOOLEAN_COMPARISON").toString()
							           +" "
									   +temp.get("ELEMENT_DESC").toString()
							           +temp.get("COMPARISON_OP").toString()
							           +temp.get("ELEMENT_VALUE").toString();
						}							
					}
				}
				//将拼完的字符串存入到授权条件列（RULE_DESC）中
				rule.put("RULE_DESC", ruleDesc.toString());
				list.set(i, rule);
			}
			rs.setRecords(list);
		}
		return rs;
	}
	/**
	 * 
	* @Title: getModelGroupPo 
	* @Description: TODO(根据车型组id取相应的车型组信息) 
	* @param @param id  ：车型组id
	* @param @return   
	* @return TtAsWrModelGroupPO  
	* @throws
	 */
	@SuppressWarnings("unchecked")
	public TtAsWrModelGroupPO getModelGroupPo(String id ){
		TtAsWrModelGroupPO re = null;
		List<Object> params = new LinkedList<Object>();
		StringBuffer sql= new StringBuffer();
		sql.append(" select * from tt_as_wr_model_group t\n" );
		sql.append(" where t.wrgroup_id = ? ");
		params.add(id);
		List<TtAsWrModelGroupPO> list = select(TtAsWrModelGroupPO.class,sql.toString(), params);
		if(list != null && list.size() > 0){
			re = (TtAsWrModelGroupPO)list.get(0);
		}
		return re;
	}
	/**
	 * 
	* @Title: getModelById 
	* @Description: TODO(弹出页查询车型方法) 
	* @param @param id   : 车型组id
	* @param @param map  ：查寻条件：车型代码和车型名称（都可以模糊查询）
	* @param @return   
	* @return List  
	* @throws
	 */
	@SuppressWarnings("unchecked")
	public List getModelById(String id,HashMap map){
		StringBuffer sql= new StringBuffer();
		List<Object> params = new LinkedList<Object>();
		sql.append(" select tvmg.group_code,tvmg.group_name from TM_VHCL_MATERIAL_GROUP tvmg where  tvmg.group_id in (\n " );
		sql.append(" select tawmi.model_id\n" );
		sql.append(" from TT_AS_WR_MODEL_GROUP tawmg left outer join TT_AS_WR_MODEL_ITEM tawmi\n " );
		sql.append(" on tawmg.wrgroup_id = tawmi.wrgroup_id\n " );
		sql.append(" where tawmg.wrgroup_id = ? \n " );
		params.add(id);
		sql.append(" ) \n");
		if(Utility.testString(map.get("groupcode").toString())){
			sql.append(" and tvmg.group_code like ? ");
			params.add("%"+map.get("groupcode")+"%");
		}
		if(Utility.testString(map.get("groupname").toString())){
			sql.append(" and tvmg.group_name like ? ");
			params.add("%"+map.get("groupname")+"%");
		}		
		List<Map<String, Object>> list = pageQuery(sql.toString(), params,getFunName());
		return list;
	}
	/**
	 * 
	* @Title: getAllClaimType 
	* @Description: TODO(获取所有的索赔类型) 
	* @param @param type  ：索赔类型对应的tc_code type
	* @param @return   
	* @return List  
	* @throws
	 */
	@SuppressWarnings("unchecked")
	public List getAllClaimType(Integer type){
		List<Object> params = new LinkedList<Object>();
		StringBuffer sql= new StringBuffer();
		sql.append(" select tt.code_id,tt.type,tt.type_name,\n" );
		sql.append(" tt.code_desc,tt.is_down,tt.can_modify\n" );
		sql.append(" from tc_code tt\n" );
		sql.append(" where tt.type = ? \n" );
		params.add(type);
		sql.append(" and tt.status = ?\n" );
		params.add(Constant.STATUS_ENABLE);
		sql.append(" order by num");		
		List<Map<String, Object>> list = pageQuery(sql.toString(), params,getFunName());
		return list;
	}
	/**
	 * 
	* @Title: getAllClaimType 
	* @Description: TODO(获取所有的车型大类（车型组）) 
	* @param @param type  ：tc_code type
	* @param @return   
	* @return List  
	* @throws
	 */
	@SuppressWarnings("unchecked")
	public List getAllModeType(){
		List<Object> params = new LinkedList<Object>();
		StringBuffer sql= new StringBuffer();
		sql.append("select  p.wrgroup_id,p.wrgroup_code,p.wrgroup_name from tt_as_wr_model_group p\n");
		sql.append("where p.wrgroup_type=").append(Constant.WR_MODEL_GROUP_TYPE_01).append("\n");		
		List<Map<String, Object>> list = pageQuery(sql.toString(), params,getFunName());
		return list;
	}		
	/**
	 * 
	* @Title: getAuthRuleById 
	* @Description: TODO(根据授权规则编号查询车型组信息) 
	* @param @param id
	* @param @return   
	* @return List  
	* @throws
	 */
	@SuppressWarnings("unchecked")
	public List getAuthRuleById(String id){
		List<Object> params = new LinkedList<Object>();
		StringBuffer sql= new StringBuffer();
		sql.append(" SELECT a.* from TT_AS_WR_RULEMAPPING a\n " );
		sql.append(" WHERE 1=1\n " );
		sql.append(" and a.RULE_ELEMENT = ? ");
		params.add(id);
		List<Map<String, Object>> list = pageQuery(sql.toString(), params,getFunName());
		return list;
	}
	/**
	 * 
	* @Title: getRuleItemById 
	* @Description: TODO(根据授权规则编号查询规则明细列表) 
	* @param @param id
	* @param @return   
	* @return List  
	* @throws
	 */
	@SuppressWarnings("unchecked")
	public List getRuleItemById(String id ){
		List<Object> params = new LinkedList<Object>();
		StringBuffer sql= new StringBuffer();
		sql.append(" select d.code_desc as ELEMENT_DESC,  a.COMPARISON_OP,a.ELEMENT_VALUE,a.BOOLEAN_COMPARISON,\n" );
		sql.append(" a.ELEMENT_NO,a.RULE_NO,a.ELEMENT_POSITION\n" );
		sql.append(" FROM TT_AS_WR_RULEITEM a,TT_AS_WR_RULEMAPPING b,tc_code c,tc_code d\n" );
		sql.append(" where a.RULE_NO = b.RULE_ELEMENT\n" );
		sql.append(" and a.comparison_op = c.code_id\n" );
		sql.append(" and a.element_no = d.code_id\n" );
		sql.append(" and b.RULE_ELEMENT = ? \n" );
		params.add(id);
		sql.append("ORDER BY a.RULE_NO,a.ELEMENT_POSITION");
		List<Map<String, Object>> list = pageQuery(sql.toString(), params,getFunName());
		return list;

	}
	/**
	 * 
	* @Title: ruleQueryById 
	* @Description: TODO(根据授权规则编号查询授权规则明细) 
	* @param @param id         ：授权规则编号
	* @param @return
	* @param @throws Exception   
	* @return HashMap  
	* @throws
	 */
	@SuppressWarnings("unchecked")
	public   HashMap ruleQueryById(String id,Long oemCompanyId) throws Exception {
		List list = dao.getLevelList("1",oemCompanyId);
		HashMap map = new HashMap();
		StringBuffer sb = new StringBuffer();
		sb.append(" select tawr.rule_element,tawmg.wrgroup_code,tawr.prior_level, ");
		sb.append(" tawr.role,tawmg.wrgroup_id ");
		for(int i = 0 ; i < list.size(); i++){
			HashMap tcpo = (HashMap)list.get(i);
			sb.append(" ,CASE WHEN instr(tawr.role,'"+tcpo.get("APPROVAL_LEVEL_CODE")+"')>0 then '"+tcpo.get("APPROVAL_LEVEL_CODE")+"' else '' end \""+tcpo.get("APPROVAL_LEVEL_CODE")+"\"");
		}		
		sb.append(" from TT_AS_WR_RULEMAPPING tawr, tt_as_wr_model_group tawmg  where tawr.warranty_group= tawmg.wrgroup_id(+) ");
		sb.append(" and tawr.rule_element ="+id+" ");
		List<Map<String, Object>> relist = pageQuery(sb.toString(), null,getFunName());
		if(relist != null && relist.size() > 0){
			map = (HashMap)relist.get(0);
		}
		return map;
	}

	/* (非 Javadoc) 
	 * <p>Title: wrapperPO</p> 
	 * <p>Description: </p> 
	 * @param rs
	 * @param idx
	 * @return 
	 * @see com.infodms.dms.dao.common.BaseDao#wrapperPO(java.sql.ResultSet, int) 
	 */
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO 自动生成方法存根
		return null;
	}

}
