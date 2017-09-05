package com.infodms.dms.dao.claim.auditing;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.infodms.dms.actions.claim.auditing.rule.custom.RuleVO;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TtAsWrApplicationPO;
import com.infodms.dms.po.TtAsWrClaimBalancePO;
import com.infodms.dms.po.TtAsWrRuleitemPO;
import com.infodms.dms.po.TtAsWrRulemappingPO;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.po3.bean.PO;

/**
 * 结算审核授权规则
 */
@SuppressWarnings("unchecked")
public class BalanceAuditingDao extends BaseDao {

	private static BalanceAuditingDao auditiongDao;
	
	private BalanceAuditingDao(){
		super();
	}
	
	public static BalanceAuditingDao getInstance(){
		if(auditiongDao==null){
			auditiongDao = new BalanceAuditingDao();
		}
		return auditiongDao;
	}
	
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
    /**
     * 查询授权规则明细（将对应规则的条件明细封装到授权规则对象中）
     * @param wrGroupId 车型组ID
     * @param companyId 所属公司ID区分轿车和微车
     * @param type 0 技术授权 1 结算授权
     * @return Map<String,RuleVO>
     */
    public Map<String,RuleVO> loadAuthRuleDetail(Long companyId,String type){
    	
    	Map<String,RuleVO> resultMap = new HashMap<String, RuleVO>();//存在规则
    	
    	//查询授权规则信息
    	List<PO> ruleList = this.queryAuthRule(companyId,type);
    	
    	if(ruleList==null || ruleList.size()<=0)
    		return resultMap;
    	//查询授权规则对应明细信息
    	List<TtAsWrRuleitemPO> ruleItemList = this.queryAuthRuleItem(companyId,type);
    	
    	if(ruleItemList==null || ruleItemList.size()<=0)
    		return resultMap;
    	
    	Map<String,Map<Integer,TtAsWrRuleitemPO>> tempMap = new HashMap<String, Map<Integer,TtAsWrRuleitemPO>>();
    	//将规则对应的条件明细封装到对应规则下
    	for (TtAsWrRuleitemPO ttAsWrRuleitemPO : ruleItemList) {
			String roNO = ttAsWrRuleitemPO.getRuleNo().toString();//规则号
			Map<Integer,TtAsWrRuleitemPO> temp = null;//存在规则明细
			if(tempMap.containsKey(roNO)){
				temp = tempMap.get(roNO);
			}else{
				temp = new TreeMap<Integer, TtAsWrRuleitemPO>();//要求带排序
				tempMap.put(roNO, temp);
			}
			temp.put(ttAsWrRuleitemPO.getElementPosition(), ttAsWrRuleitemPO);
		}
    	
    	//将规则同条件明细封装到一起
    	for (PO po : ruleList) {
			TtAsWrRulemappingPO rulePO = (TtAsWrRulemappingPO)po;
    		String roNO = rulePO.getRuleElement().toString();
			RuleVO ruleVO = new RuleVO();
			ruleVO.setRuleVO(rulePO);
			if(tempMap.containsKey(roNO))
				ruleVO.setConditiongMap(tempMap.get(roNO));
			
			resultMap.put(rulePO.getRuleElement().toString(), ruleVO);
    	}
    	
    	return resultMap;
    }
	
    /**
     * 查询授权规则对应条件明细
     * 注：结算审核授权规则不关联车型组
     * @param wrGroupId 车型组ID
     * @param companyId 所属公司ID区分轿车和微车
     * @param type 0 技术授权 1 结算授权
     * @return List<TtAsWrRuleitemPO>
     */
    public List<TtAsWrRuleitemPO> queryAuthRuleItem(Long companyId,String type){
    	
    	//查询规则对应条件信息
    	String sql = "SELECT B.* \n"+
						" FROM TT_AS_WR_RULEMAPPING A,TT_AS_WR_RULEITEM B\n"+
						" WHERE 1=1\n"+
						" AND A.RULE_ELEMENT = B.RULE_NO\n"+
						" AND A.TYPE = ?\n" +
						" ORDER BY B.RULE_NO,B.ELEMENT_POSITION";
    	
    	List<Object> paramList = new ArrayList<Object>();
    	paramList.add(CommonUtils.checkNull(type));
    	List<TtAsWrRuleitemPO> ruleItemList = this.select(TtAsWrRuleitemPO.class, sql, paramList);
    	
    	return ruleItemList;
    }

    /**
     * 查询授权规则
     * 注：结算审核授权规则不关联车型组
     * @param wrGroupId 车型组ID
     * @param companyId 所属公司ID区分轿车和微车
     * @param type 0 技术授权 1 结算授权
     * @return List<PO>
     */
    public List<PO> queryAuthRule(Long companyId,String type){
    	
    	//查询规则信息
    	TtAsWrRulemappingPO conditionPO = new TtAsWrRulemappingPO();
    	conditionPO.setOemCompanyId(companyId);
    	conditionPO.setType(CommonUtils.checkNull(type));
    	
    	List<PO> ruleList = this.select(conditionPO);
    	return ruleList;
    }
    
    /**
     * 将索赔单结算审核状态记录到结算单同索赔单关系表中
     * 状态使用有效无效(TC_CODE中维护)。
     * @param claimId 索赔单ID
     * @param status 状态
     */
    public void updateClaimStatus(Long claimId,Integer status){
    	StringBuilder sql= new StringBuilder();
    	sql.append("UPDATE TR_BALANCE_CLAIM\n" );
    	sql.append("   SET STATUS = ?\n" );
    	sql.append(" WHERE 1=1\n" );
    	sql.append("   AND CLAIM_ID = ?");

    	List<Object> paramList = new ArrayList<Object>();
    	paramList.add(status);
    	paramList.add(claimId);
    	
    	this.update(sql.toString(), paramList);
    }
    
    /**
     * 将需要人工审核的级别写入索赔单信息中
     * @param updateId 更新记录人员
     * @param claimId 索赔申请单ID
     * @param firstRole 第一个需要审核的角色
     * @param advice 审批建议
     * @return
     */
    public int updateClaimOrderStatus(String updateId,
    		String claimId,String firstRole,String advice){
    	
    	int res = 0;
    	TtAsWrApplicationPO conditionPO = new TtAsWrApplicationPO();
    	conditionPO.setId(Long.parseLong(claimId));
    	TtAsWrApplicationPO parameterPO = new TtAsWrApplicationPO();
    	parameterPO.setUpdateBy(Long.parseLong(updateId));
    	parameterPO.setUpdateDate(new Date());
    	parameterPO.setAuthCode(firstRole);
    	parameterPO.setOemOption(CommonUtils.checkNull(advice));
    	
    	res = this.update(conditionPO, parameterPO);
    	return res;
    }
    
	/**
	 * 查询对应结算单下的索赔单数
	 * @param balanceId 结算单ID
	 * @param status 索赔单是否结算审核通过（使用TC_CODE中的有效、无效状态）
	 * @return List<Map<String,Object>>
	 *         键名：CLAIM_ID
	 */
	public List<Map<String,Object>> queryClaimByBalanceId(Long balanceId,Integer status){
		
		StringBuilder sql= new StringBuilder();
		sql.append("SELECT CLAIM_ID\n" );
		sql.append("FROM TR_BALANCE_CLAIM\n" );
		sql.append("WHERE 1=1\n" );
		sql.append("AND BALANCE_ID = ?");
		

		List<Object> paramList = new ArrayList<Object>();
		paramList.add(balanceId);
		
		if(status!=null){
			sql.append("AND STATUS = ?");
			paramList.add(status);
		}
			
		return this.pageQuery(sql.toString(), paramList, this.getFunName());
	}
	
	/**
	 * 修改结算单状态
	 * @param balanceId 结算单ID
	 * @param status 目标状态
	 * @param userId 修改人信息
	 */
	public void updateBalanceStatus(Long balanceId,Integer status,Long userId){
		
		TtAsWrClaimBalancePO balancePO = new TtAsWrClaimBalancePO();
		balancePO.setStatus(status);
		balancePO.setUpdateBy(userId);
		balancePO.setUpdateDate(new Date());
		
		TtAsWrClaimBalancePO condition = new TtAsWrClaimBalancePO();
		condition.setId(balanceId);
		this.update(condition, balancePO);
	}
}
