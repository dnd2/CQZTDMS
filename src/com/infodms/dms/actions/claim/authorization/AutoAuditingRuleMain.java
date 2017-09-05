/**   
* @Title: AutoAuditingRuleMain.java 
* @Package com.infodms.dms.actions.claim.authorization 
* @Description: TODO(索赔自动审核规则管理action) 
* @author wangjinbao   
* @date 2010-6-11 上午10:13:48 
* @version V1.0   
*/
package com.infodms.dms.actions.claim.authorization;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.common.getCompanyId.GetOemcompanyId;
import com.infodms.dms.dao.claim.authorization.AutoAuditingRuleDao;
import com.infodms.dms.dao.claim.authorization.ClaimLaborWatchDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtAsWrClaimAutoPO;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

/** 
 * @ClassName: AutoAuditingRuleMain 
 * @Description: TODO(索赔自动审核规则管理action) 
 * @author wangjinbao 
 * @date 2010-6-11 上午10:13:48 
 *  
 */
public class AutoAuditingRuleMain {
	private Logger logger = Logger.getLogger(AutoAuditingRuleMain.class);
	private final AutoAuditingRuleDao dao = AutoAuditingRuleDao.getInstance();
	private final String AutoAuditingRule_URL = "/jsp/claim/authorization/autoAuditingRuleIndex.jsp";//主页面（查询）
	/** 修改授权功能 */
	private final String MODIFY_AUTH_PAGE = "/jsp/claim/authorization/autoAuditingRuleAuthModify.jsp";
	
	/**
	 * 
	* @Title: autoAuditingRuleInit 
	* @Description: TODO(索赔自动审核规则管理初始化) 
	* @param    
	* @return void  
	* @throws
	 */
	public void autoAuditingRuleInit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword(AutoAuditingRule_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"索赔自动审核规则管理");
			logger.error(logonUser,e1);
			act.setException(e1);
		}		
		
	}
	/**
	 * 
	* @Title: autoAuditingRuleQuery 
	* @Description: TODO(索赔自动审核规则管理查询) 
	* @param    
	* @return void  
	* @throws
	 */
	public void autoAuditingRuleQuery(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			act.getResponse().setContentType("application/json");
			// add by zouchao 2010-07-19 begin
			Long oemCompanyId = GetOemcompanyId.getOemCompanyId(logonUser);
			// 2010-07-19 end
			if("1".equals(request.getParamValue("COMMAND"))){ //开始查询				
				Integer curPage = request.getParamValue("curPage") != null ? Integer
						.parseInt(request.getParamValue("curPage"))
						: 1; // 处理当前页
				
				PageResult<Map<String, Object>> ps = dao.autoAuditingRuleQuery(String.valueOf(oemCompanyId),Constant.PAGE_SIZE, curPage);
				act.setOutData("ps", ps);
			}
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"索赔自动审核规则管理");
			logger.error(logonUser,e1);
			act.setException(e1);
		}		
		
	}
	/**
	 * 
	* @Title: autoAuditingRuleUpdate 
	* @Description: TODO(索赔自动审核规则管理修改) 
	* @param    
	* @return void  
	* @throws
	 */
	@SuppressWarnings("unchecked")
	public void autoAuditingRuleUpdate(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		TtAsWrClaimAutoPO selpo = null;
		TtAsWrClaimAutoPO updatepo = null;
		try {
			RequestWrapper request = act.getRequest();
			act.getResponse().setContentType("application/json");
			String autoId = request.getParamValue("AUTO_ID");//主键id
			String status = request.getParamValue("STATUS");//状态，启用，停用
			
			selpo = new TtAsWrClaimAutoPO();
			selpo.setAutoId(Utility.getLong(autoId));
			
			updatepo = new TtAsWrClaimAutoPO();
			updatepo.setStatus(Utility.getInt(status));
			updatepo.setUpdateBy(logonUser.getUserId());
			updatepo.setUpdateDate(new Date());
			
			dao.update(selpo, updatepo);
			act.setOutData("success", "true");
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.UPDATE_FAILURE_CODE,"索赔自动审核规则管理");
			logger.error(logonUser,e1);
			act.setException(e1);
		}		
		
	}

	/**
	 * 修改 "人工规则" 对应需要授权的级别 页面初始
	 */
	@SuppressWarnings("unchecked")
	public void updateAutoRuleAuthInit(){
		
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String id = request.getParamValue("ID");
			ClaimLaborWatchDao labourWatchDao = ClaimLaborWatchDao.getInstance();
			Long companyId = GetOemcompanyId.getOemCompanyId(logonUser);//用户所属公司
			
			List<?> levelList = labourWatchDao.getLevelList(companyId, Constant.AUDIT_TYPE_01.toString());
			TtAsWrClaimAutoPO conditionPO = new TtAsWrClaimAutoPO();
			conditionPO.setAutoId(Long.parseLong(id));
			
			List<PO> autoList = this.dao.select(conditionPO);
			TtAsWrClaimAutoPO autoPO = new TtAsWrClaimAutoPO();

			if(autoList!=null && autoList.size()>0)
				autoPO = (TtAsWrClaimAutoPO)autoList.get(0);
			
			act.setOutData("AUTO_ID", id);
			act.setOutData("levelList", levelList);
			act.setOutData("AUTOPO", autoPO);
			act.setForword(this.MODIFY_AUTH_PAGE);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.UPDATE_FAILURE_CODE,"自动规则授权角色维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}	
	}
	
	/**
	 * 修改 "人工规则" 对应需要授权的级别
	 */
	@SuppressWarnings("unchecked")
	public void updateAutoRuleAuth(){
		
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String autoId = request.getParamValue("AUTO_ID");//规则ID
			//需要授权的角色   格式：授权代码$授权描述
			String authCode[] = request.getParamValues("APPROVAL_LEVEL_CODE");
			
			String authCodes = "";
			String authDescs = "";
			String splitStr = "@";
			
			for (int i = 0; i < authCode.length; i++) {
				String temp = authCode[i];
				if(temp!=null && !"".equals(temp)){
					String tempArray[] = temp.split(splitStr);
					if(tempArray.length>1){
						authCodes = authCodes + tempArray[0] + ",";
						authDescs = authDescs + tempArray[1] + ",";
					}	
				}
			}
			
			System.out.println(authCode);
			TtAsWrClaimAutoPO conditionPO = new TtAsWrClaimAutoPO();
			conditionPO.setAutoId(CommonUtils.parseLong(autoId));
			
			TtAsWrClaimAutoPO targetPO = new TtAsWrClaimAutoPO();
			targetPO.setAuthCode(authCodes);
			targetPO.setAuthDesc(authDescs);
			this.dao.update(conditionPO, targetPO);
			
			String result="SUCESS";
			act.setOutData("RESULT", result);
			act.setOutData("AUTO_ID", autoId);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.UPDATE_FAILURE_CODE,"自动规则授权角色维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}	
	}
}
