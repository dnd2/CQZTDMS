/**   
* @Title: RuleMain.java 
* @Package com.infodms.dms.actions.claim.authorization 
* @Description: TODO(授权规则维护Action) 
* @author wangjinbao   
* @date 2010-6-12 上午09:35:24 
* @version V1.0   
*/
package com.infodms.dms.actions.claim.authorization;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.common.ClaimCommonAction;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.common.getCompanyId.GetOemcompanyId;
import com.infodms.dms.dao.claim.authorization.RuleDao2;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtAsWrModelGroupPO;
import com.infodms.dms.po.TtAsWrRuleitemPO;
import com.infodms.dms.po.TtAsWrRulemappingPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

/** 
 * @ClassName: RuleMain 
 * @Description: TODO(授权规则维护Action) 
 * @author wangjinbao 
 * @date 2010-6-12 上午09:35:24 
 * 注：主要给结算审核用 复制 索赔授权规则20100828
 */
public class RuleMain2 {
	private Logger logger = Logger.getLogger(RuleMain2.class);
	private final RuleDao2 dao = RuleDao2.getInstance(); 
	private final String RULEMAIN_URL = "/jsp/claim/authorization/ruleIndex2.jsp";//主页面（查询）
	private final String RULEMAIN_ADD_URL = "/jsp/claim/authorization/ruleAdd2.jsp";//增加页面
	private final String RULE_MODEL_URL = "/jsp/claim/authorization/ruleModelQuery.jsp";//车型选择页面
	private final String RULE_CLAIM_TYPE_URL = "/jsp/claim/authorization/ruleClaimTypeQuery.jsp";//索赔类型选择页面
	private final String RULEMAIN_UPDATE_URL = "/jsp/claim/authorization/ruleModify2.jsp";//修改页面
	private final ClaimCommonAction claimCommon = ClaimCommonAction.getInstance();
	/**
	 * 
	* @Title: ruleInit 
	* @Description: TODO(授权规则维护初始化) 
	* @param    
	* @return void  
	* @throws
	 */
	public void ruleInit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			Long oemCompanyId = GetOemcompanyId.getOemCompanyId(logonUser);
			List list = dao.getLevelList("0",oemCompanyId);
			act.setOutData("LEVELLIST", list);
			act.setOutData("wrmodelgrouplist",claimCommon.getWrModelGroupList("1",Constant.WR_MODEL_GROUP_TYPE_01,oemCompanyId));
			act.setForword(RULEMAIN_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"授权规则维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}		
		
	}
	/**
	 * 
	* @Title: ruleQuery 
	* @Description: TODO(授权规则查询方法) 
	* @param    
	* @return void  
	* @throws
	 */
	public void ruleQuery() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			act.getResponse().setContentType("application/json");
			if("1".equals(request.getParamValue("COMMAND"))){ //开始查询
				
				Long oemCompanyId = GetOemcompanyId.getOemCompanyId(logonUser);
				
				//结算授权条件
				String wrgroupId = request.getParamValue("WRGROUP_ID");//结算授权条件
				//分页信息			
				Integer curPage = request.getParamValue("curPage") != null ? Integer
						.parseInt(request.getParamValue("curPage"))
						: 1; // 处理当前页
				PageResult<Map<String, Object>> ps = dao.ruleQuery(Constant.PAGE_SIZE, curPage,wrgroupId,oemCompanyId);
				act.setOutData("ps", ps);
			}
		} catch(BizException e){
			logger.error(logonUser,e);
			act.setException(e);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"授权规则维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 
	* @Title: ruleAddInit 
	* @Description: TODO(授权规则维护新增初始化) 
	* @param    
	* @return void  
	* @throws
	 */
	public void ruleAddInit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			Long oemCompanyId = GetOemcompanyId.getOemCompanyId(logonUser);
			List list = dao.getLevelList("1",oemCompanyId);
			act.setOutData("LEVELLIST", list);
			act.setOutData("wrmodelgrouplist",claimCommon.getWrModelGroupList("0",Constant.WR_MODEL_GROUP_TYPE_01,oemCompanyId));
			act.setForword(RULEMAIN_ADD_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"授权规则维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}		
		
	}
	/**
	 * 
	* @Title: ruleModelSelect 
	* @Description: TODO(车型组对应的车型选择方法) 
	* @param    
	* @return void  
	* @throws
	 */
	public void ruleModelSelect(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		HashMap<String, Object> map = null;
		try {
			RequestWrapper request = act.getRequest();
			
			String groupid = request.getParamValue("ID");
			String rowNum = request.getParamValue("r");//取得现在是第几行
			TtAsWrModelGroupPO po = dao.getModelGroupPo(groupid);//车型组信息
			String groupcode = CommonUtils.checkNull(request.getParamValue("groupcode"));//车型代码
			String groupname = CommonUtils.checkNull(request.getParamValue("groupname"));//车型名称
			map = new HashMap<String, Object>();
			
			map.put("groupname", groupname);//车型名称
			map.put("groupcode", groupcode);//授权级别
			map.put("groupid", groupid);//车型组id
			//获得车型组对应的车型列表
			List ps = dao.getModelById(groupid,map);
			act.setOutData("ADDLIST", ps);
			act.setOutData("MODELID", groupid);
			act.setOutData("MODELGROUP", po);
			act.setOutData("ROWNUM", rowNum);//存回显的行数，id用
			act.setForword(RULE_MODEL_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"授权规则维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}	
	}
	/**
	 * 
	* @Title: claimTypeSelect 
	* @Description: TODO(索赔类型的选择方法) 
	* @param    
	* @return void  
	* @throws
	 */
	public void claimTypeSelect(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String rowNum = request.getParamValue("r");//取得现在是第几行
			//获得所有索赔类型的列表
			List ps = dao.getAllClaimType(Constant.CLA_TYPE);
			act.setOutData("ADDLIST", ps);
			act.setOutData("ROWNUM", rowNum);//存回显的行数，id用
			act.setForword(RULE_CLAIM_TYPE_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"授权规则维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}	
	}	
	/**
	 * 
	* @Title: ruleAdd 
	* @Description: TODO(授权规则添加) 
	* @param    
	* @return void  
	* @throws
	 */
	@SuppressWarnings("unchecked")
	public void ruleAdd(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			
			Long oemCompanyId = GetOemcompanyId.getOemCompanyId(logonUser);
			String wrGroupId = request.getParamValue("WRGROUP_ID");//车型组id
			String priorLevel = request.getParamValue("PRIOR_LEVEL");//授权规则明细条件
			String[] elementIds = request.getParamValues("ELEMENT_ID");//授权项
			String[] comparisonOPs = request.getParamValues("COMPARISON_OP");//授权项（算术/逻辑）比较符
			String[] values = request.getParamValues("ELEMENT_VALUE");//值列
			String[] codes = request.getParamValues("ELEMENT_CODE");//值列对应的code（用于索赔类型）
			String[] elementRelations = request.getParamValues("BOOLEAN_COMPARISION");//授权关系
			String roles = request.getParamValue("AUTHROLE");//授权角色
			String[] elementPositions = request.getParamValues("ELEMENT_POSITION");//检查顺序
		
			
			//索赔授权规则
			TtAsWrRulemappingPO ttWrRulemappingPO = new TtAsWrRulemappingPO();
			Long mapId = Long.parseLong(SequenceManager.getSequence(""));
			ttWrRulemappingPO.setRuleElement(mapId);  //规则编号
			ttWrRulemappingPO.setWarrantyGroup(new Long(-100));//索赔授权车型组
			ttWrRulemappingPO.setType(String.valueOf(Constant.AUDIT_TYPE_02)); //判断是结算室审核规则
			ttWrRulemappingPO.setRole(roles);//对应的角色
			
			ttWrRulemappingPO.setPriorLevel(priorLevel);//授权规则明细条件
			ttWrRulemappingPO.setCreateBy(logonUser.getUserId());
			ttWrRulemappingPO.setCreateDate(new Date());
			ttWrRulemappingPO.setOemCompanyId(oemCompanyId);
			//往索赔授权规则表插入一条记录
			dao.insert(ttWrRulemappingPO);

			int len = elementIds.length;
			int index = 0;
			for (int i = 0; i < len; i++) {
				//索赔授权规则明细
				TtAsWrRuleitemPO ttWrRuleitemPO = new TtAsWrRuleitemPO();
				ttWrRuleitemPO.setId(Long.parseLong(SequenceManager.getSequence("")));//主键id
				ttWrRuleitemPO.setRuleNo(mapId);//规则编号
				//布尔比较符
				if ("0".equals(elementPositions[i]))
					ttWrRuleitemPO.setBooleanComparison("");
				else
					ttWrRuleitemPO.setBooleanComparison(elementRelations[index++].trim());
				ttWrRuleitemPO.setComparisonOp(comparisonOPs[i].trim());//比较符
				ttWrRuleitemPO.setElementNo(Long.parseLong(elementIds[i].trim()));//元素编号
				
					ttWrRuleitemPO.setElementValue(values[i].trim());//元素值
				ttWrRuleitemPO.setElementPosition(new Integer(elementPositions[i]));//元素位置
				ttWrRuleitemPO.setCreateBy(logonUser.getUserId());
				ttWrRuleitemPO.setCreateDate(new Date());
				//往索赔授权规则明细表插入一条记录
				dao.insert(ttWrRuleitemPO);
			}
			act.setOutData("success", "true");
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"授权规则维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}		
		
	}
	/**
	 * 
	* @Title: ruleUpdateInit 
	* @Description: TODO(授权规则维护修改初始化) 
	* @param    
	* @return void  
	* @throws
	 */
	public void ruleUpdateInit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			// 从session中取得车厂公司id
			Long oemCompanyId = GetOemcompanyId.getOemCompanyId(logonUser);
			List list = dao.getLevelList("1",oemCompanyId);//不包括“自动拒绝”的授权级别
			String wid = request.getParamValue("WID");//对应车型组id
			String ruleElement = request.getParamValue("ID");//对应授权规则元素编号
			//根据主键查找规则
			List ruleList = dao.getAuthRuleById(ruleElement);
			List itemList = dao.getRuleItemById(ruleElement);
			HashMap hm = dao.ruleQueryById(ruleElement,oemCompanyId);
			act.setOutData("SELMAP", hm);//对应的授权角色map
			act.setOutData("wid", wid);//车型组id
			act.setOutData("LEVELLIST", list);//授权级别列表
			act.setOutData("TT_AUTH_RULE_LIST", ruleList);//授权规则
			act.setOutData("TT_AUTH_ITEM_LIST", itemList);//元素明细
			//索赔车型组回显
			//act.setOutData("wrmodelgrouplist",claimCommon.getWrModelGroupListCallBack(wid,Constant.WR_MODEL_GROUP_TYPE_01,oemCompanyId));
			act.setForword(RULEMAIN_UPDATE_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"授权规则维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}		
		
	}
	/**
	 * 
	* @Title: ruleUpdate 
	* @Description: TODO(授权规则维护修改) 
	* @param    
	* @return void  
	* @throws
	 */
	@SuppressWarnings("unchecked")
	public void ruleUpdate(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String wrGroupId = request.getParamValue("WRGROUP_ID");//车型组id
			String priorLevel = request.getParamValue("PRIOR_LEVEL");//授权规则明细条件
			String ruleId = request.getParamValue("RULEID");//带修改的授权规则ID
			String[] elementIds = request.getParamValues("ELEMENT_ID");//授权项
			String[] comparisonOPs = request.getParamValues("COMPARISON_OP");//授权项（算术/逻辑）比较符
			String[] values = request.getParamValues("ELEMENT_VALUE");//值列
			String[] codes = request.getParamValues("ELEMENT_CODE");//值列对应的code（用于索赔类型）
			String[] elementRelations = request.getParamValues("BOOLEAN_COMPARISION");//授权关系
			String roles = request.getParamValue("AUTHROLE");//授权角色
			String[] elementPositions = request.getParamValues("ELEMENT_POSITION");//检查顺序
			
			//索赔授权规则
			//索赔授权规则修改的条件po
			TtAsWrRulemappingPO tawrpo = new TtAsWrRulemappingPO();
			tawrpo.setRuleElement(Utility.getLong(ruleId));  //待修改的规则编号
			//索赔授权规则修改的po
			TtAsWrRulemappingPO updatetawrpo = new TtAsWrRulemappingPO();
			updatetawrpo.setWarrantyGroup(new Long(-100));//索赔授权车型组
			updatetawrpo.setRole(roles);//对应的角色
			updatetawrpo.setPriorLevel(priorLevel);//授权规则明细
			updatetawrpo.setUpdateBy(logonUser.getUserId());
			updatetawrpo.setUpdateDate(new Date());
			//索赔授权规则表修改
			dao.update(tawrpo, updatetawrpo);

			//先清空索赔授权规则明细，然后再执行插入
			TtAsWrRuleitemPO deltepo = new TtAsWrRuleitemPO();
			deltepo.setRuleNo(Utility.getLong(ruleId));
			dao.delete(deltepo);
			
			int len = elementIds.length;
			int index = 0;
			for (int i = 0; i < len; i++) {
				//索赔授权规则明细
				TtAsWrRuleitemPO ttWrRuleitemPO = new TtAsWrRuleitemPO();
				ttWrRuleitemPO.setId(Long.parseLong(SequenceManager.getSequence("")));//主键id
				ttWrRuleitemPO.setRuleNo(Utility.getLong(ruleId));//规则编号
				//布尔比较符
				if ("0".equals(elementPositions[i]))
					ttWrRuleitemPO.setBooleanComparison("");
				else
					ttWrRuleitemPO.setBooleanComparison(elementRelations[index++].trim());
				ttWrRuleitemPO.setComparisonOp(comparisonOPs[i].trim());//比较符
				ttWrRuleitemPO.setElementNo(Long.parseLong(elementIds[i].trim()));//元素编号
				//授权项是：索赔类型
				ttWrRuleitemPO.setElementValue(values[i].trim());//元素值				
				ttWrRuleitemPO.setElementPosition(new Integer(elementPositions[i]));//元素位置
				ttWrRuleitemPO.setCreateBy(logonUser.getUserId());
				ttWrRuleitemPO.setCreateDate(new Date());
				//往索赔授权规则明细表插入一条记录
				dao.insert(ttWrRuleitemPO);
			}
			act.setOutData("success", "true");
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.UPDATE_FAILURE_CODE,"授权规则维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}		
		
	}
	/**
	 * 
	* @Title: ruleDel 
	* @Description: TODO(授权规则维护删除) 
	* @param    
	* @return void  
	* @throws
	 */
	@SuppressWarnings("unchecked")
	public void ruleDel(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String ruleElement = request.getParamValue("ID");//对应授权规则元素编号
			
			//根据授权规则元素编号： 删除规则项
			//先删规则明细表
			TtAsWrRuleitemPO ttWrRuleitemPO = new TtAsWrRuleitemPO();
			ttWrRuleitemPO.setRuleNo(Utility.getLong(ruleElement));
			dao.delete(ttWrRuleitemPO);
			//在删授权规则表
			TtAsWrRulemappingPO tawrpo = new TtAsWrRulemappingPO();
			tawrpo.setRuleElement(Utility.getLong(ruleElement));
			dao.delete(tawrpo);
			act.setOutData("success", "true");
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.DELETE_FAILURE_CODE,"授权规则维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}		
		
	}	
	
}
