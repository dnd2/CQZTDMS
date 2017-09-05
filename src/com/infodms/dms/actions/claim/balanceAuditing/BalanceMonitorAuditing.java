package com.infodms.dms.actions.claim.balanceAuditing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.claim.auditing.ClaimOrderVO;
import com.infodms.dms.actions.claim.auditing.rule.custom.AuditingVO;
import com.infodms.dms.actions.claim.auditing.rule.custom.ElementCompare;
import com.infodms.dms.actions.claim.auditing.rule.custom.RuleVO;
import com.infodms.dms.actions.claim.auditing.rule.custom.elment.AbstractElement;
import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.claim.auditing.BalanceAuditingDao;
import com.infodms.dms.dao.claim.auditing.ClaimAuditingDao;
import com.infodms.dms.po.TcCodePO;
import com.infodms.dms.po.TtAsWrRuleitemPO;
import com.infoservice.po3.bean.PO;

/**
 * 自定义授权规则处理
 * 使用 索赔结算管理 中 结算自动审核规则维护 中定义的规则
 * @author XZM
 */
public class BalanceMonitorAuditing {
	
	private Logger logger = Logger.getLogger(BalanceMonitorAuditing.class);
		
	public String getRuleDesc() {
		return "自定义授权规则审核";
	}

	/**
	 * 根据用户自定义的授权规则，对索赔单进行审核，提取其中需要通过的审核级别
	 * @param orderVO
	 * @return
	 */
	public AuditingVO deal(ClaimOrderVO orderVO) {
		
		AuditingVO resultVO = new AuditingVO();
		
		try{
			ClaimAuditingDao auditingDao = ClaimAuditingDao.getInstance();
			BalanceAuditingDao balanceAuditingDao = BalanceAuditingDao.getInstance();
			
			//查询授权规则
			Map<String,RuleVO> ruleMap = balanceAuditingDao.loadAuthRuleDetail(orderVO.getCompanyId(),
					Constant.AUDIT_TYPE_02.toString());//结算授权规则
			
			if(ruleMap==null || ruleMap.size()<=0)//情况二：没有维护对应授权规则
				return resultVO;
			
			//查询索赔申请单授权项
			List<PO> elementList = auditingDao.loadAuthItem(Constant.CLAIM_AUTH_TYPE.toString());
			
			//根据授权项初始化自动审核需要的值（AbstractElement）
			Map<String,Object> dataMap = new HashMap<String, Object>();//授权项ID（KEY）和授权项对应索赔单中的值
			Map<String,String> itemMap = new HashMap<String, String>();//授权项ID（KEY）和授权项名称
			for (PO po : elementList) {
				TcCodePO codePO = (TcCodePO)po;
				//根据授权项ID初始要处理的索赔单中的值
				AbstractElement element = AbstractElement.getElement(codePO.getCodeId());
				if(element!=null){
					Object obj = element.getPackElementValue(orderVO);
					dataMap.put(codePO.getCodeId(), obj);
				}
				//记录授权项ID和授权项名称
				itemMap.put(codePO.getCodeId(), codePO.getCodeDesc());
			}
			
			List<Integer> typeList = new ArrayList<Integer>();
			typeList.add(Constant.COMP_TYPE);//算术比较符
			typeList.add(Constant.LOGIC_TYPE);//逻辑比较符
			//加载授权规则运算符
			Map<String,String> compareOperMap = auditingDao.loadCompareOperator(typeList);
			
			//根据规则检测对应索赔申请单是否需要人工审核，和需要人工审核的审核级别
			StringBuilder roleBuilder = new StringBuilder();//存在需要人工审核的角色
			StringBuilder reasonBuilder = new StringBuilder();//存在需要人工审核的原因
			//应用规则验证数据
			this.applyRule(roleBuilder, reasonBuilder, ruleMap, dataMap, itemMap,compareOperMap);
			
			resultVO.setRoles(roleBuilder.toString());
			resultVO.setReasions(reasonBuilder.toString());
		}catch(Exception e){
			e.printStackTrace();
		}
		return resultVO;
	}
	
	/**
	 * 应用规则验证对应索赔申请单数据
	 * @param roleBuilder 临时存储角色
	 * @param reasonBuilder 临时存储原因
	 * @param ruleMap 规则集合
	 * @param dataMap 授权项对应数据集合
	 * @param itemMap 授权项明细
	 * @param compareOperMap 授权规则运算符
	 */
	private void applyRule(StringBuilder roleBuilder,StringBuilder reasonBuilder,
			Map<String,RuleVO> ruleMap,Map<String,Object> dataMap,
			Map<String,String> itemMap,Map<String,String> compareOperMap){
		
		Iterator<String> iterator = ruleMap.keySet().iterator();
		ElementCompare eCompare = new ElementCompare();
		//遍历并验证对应车型组的所有规则
		while(iterator.hasNext()){
			String ruleKey = iterator.next();
			RuleVO ruleVO = ruleMap.get(ruleKey);
			Map<Integer, TtAsWrRuleitemPO> conditionMap = ruleVO.getConditiongMap();
			
			if(conditionMap==null || conditionMap.size()<=0)//没有条件则直接跳出
				continue;
			
			Iterator<Integer> citer = conditionMap.keySet().iterator();
			boolean status = true;
			
			while(citer.hasNext()){
				try{
					Integer riKey = citer.next();
					TtAsWrRuleitemPO ruleItemPO = conditionMap.get(riKey);
					Object value = dataMap.get(ruleItemPO.getElementNo().toString());
					String comOP = ruleItemPO.getComparisonOp();
					String target = ruleItemPO.getElementValue();
					
					if(compareOperMap.containsKey(comOP))//将运算符ID转换成对应符号
						comOP = compareOperMap.get(comOP);
					
					logger.info("授权项号：" + ruleItemPO.getElementNo() +
							" 授权项名称：" + 
							(itemMap.containsKey(ruleItemPO.getElementNo())?itemMap.get(ruleItemPO.getElementNo()):""));
					
					boolean tempFlag = eCompare.elementCompare(value, comOP, target);
					status = eCompare.booleanCompare(status, ruleItemPO.getBooleanComparison(), tempFlag);
				}catch(Exception e){
					e.printStackTrace();
					logger.error(e.getMessage());
					continue;
				}
			}
			
			if(status){//对应一条规则索赔条件都验证通过
				roleBuilder.append(ruleVO.getRuleVO().getRole()).append(",");
				//2010-06-23 UPDATE XZM 规则原因不用再现统计，从规则表中直接加载
				reasonBuilder.append(ruleVO.getRuleVO().getPriorLevel()).append(" ");
				//reasonBuilder.append(ruleVO.getRuleReason(itemMap,compareOperMap)).append(" ");
			}	
		}
	}
}
