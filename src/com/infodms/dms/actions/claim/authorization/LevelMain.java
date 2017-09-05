/**   
* @Title: LevelMain.java 
* @Package com.infodms.dms.actions.claim.authorization 
* @Description: TODO(授权级别维护Action) 
* @author wangjinbao   
* @date 2010-6-8 上午09:09:19 
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
import com.infodms.dms.dao.claim.authorization.LevelDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmBusinessChngCodePO;
import com.infodms.dms.po.TtAsWrAuthinfoPO;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

/** 
 * @ClassName: LevelMain 
 * @Description: TODO(授权级别维护Action) 
 * @author wangjinbao 
 * @date 2010-6-8 上午09:09:19 
 *  
 */
public class LevelMain {
	private Logger logger = Logger.getLogger(LevelMain.class);
	private final LevelDao dao = LevelDao.getInstance();
	/** 授权审核 首页面 */
	private final String LEVEL_URL = "/jsp/claim/authorization/levelMainIndex.jsp";
	/** 结算审核 首页面 */
	private final String BALANCE_LEVEL_URL = "/jsp/claim/authorization/balanceLevelMainIndex.jsp";
	private final String LEVEL_UPDATE_URL = "/jsp/claim/authorization/levelModify.jsp";//增加（修改）页面
	
	/**
	 * 
	* @Title: levelInit 
	* @Description: TODO(授权级别维护查询初始化) 
	* @param    
	* @return void  
	* @throws
	 */
	public void levelInit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword(LEVEL_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"授权级别维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}		
		
	}
	
	/**
	 * 
	* @Title: levelInit 
	* @Description: TODO(授权级别维护查询初始化) 
	* @param    
	* @return void  
	* @throws
	 */
	public void balanceLevelInit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword(this.BALANCE_LEVEL_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"授权级别维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}		
		
	}
	
	/**
	 * 
	* @Title: levelQuery 
	* @Description: TODO(授权级别维护查询) 
	* @param    
	* @return void  
	* @throws
	 */
	public void levelQuery() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			act.getResponse().setContentType("application/json");
			//TYPE 1:技术室审核 0:结算室审核
			String type = request.getParamValue("TYPE");
			if(type==null || "".equals(type))
				type = Constant.AUDIT_TYPE_01.toString();
			// 从session中取得车厂公司id
			Long oemCompanyId = GetOemcompanyId.getOemCompanyId(logonUser);
			if("1".equals(request.getParamValue("COMMAND"))){ //开始查询				
				Integer curPage = request.getParamValue("curPage") != null ? Integer
						.parseInt(request.getParamValue("curPage"))
						: 1; // 处理当前页
				
				PageResult<Map<String, Object>> ps = dao.levelQuery(oemCompanyId,Constant.PAGE_SIZE, curPage,type);
				act.setOutData("ps", ps);
			}
		} catch(BizException e){
			logger.error(logonUser,e);
			act.setException(e);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"授权级别维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	* @Title: levelUpdateInit 
	* @Description: TODO(授权级别维护修改初始化) 
	* @param    
	* @return void  
	* @throws
	 */
	@SuppressWarnings("unchecked")
	public void levelUpdateInit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		TtAsWrAuthinfoPO selpo = null;
		List list = null;
		try {
			RequestWrapper request = act.getRequest();
			String approvalLevelTier = request.getParamValue("ID");//授权级别等级
			selpo = new TtAsWrAuthinfoPO();
			selpo.setApprovalLevelTier(Utility.getInt(approvalLevelTier));
			list = dao.select(selpo);
			TtAsWrAuthinfoPO result = new TtAsWrAuthinfoPO();
			if(list != null && list.size() > 0){
				result = (TtAsWrAuthinfoPO)list.get(0);
			}
			request.setAttribute("AUTHINFO", result);
			act.setForword(LEVEL_UPDATE_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.UPDATE_FAILURE_CODE,"授权级别维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}		
		
	}
	/**
	 * 
	* @Title: levelUpdate 
	* @Description: TODO(授权级别维护修改) 
	* @param    
	* @return void  
	* @throws
	 */
	public void levelUpdate() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		TtAsWrAuthinfoPO selpo = null;
		TtAsWrAuthinfoPO updatepo = null;
		try {
			RequestWrapper request = act.getRequest();
			String type = request.getParamValue("TYPE");
			String approvalLevelTier = request.getParamValue("APPROVAL_LEVEL_TIER"); //授权级别等级
			String approvalLevelName = request.getParamValue("APPROVAL_LEVEL_NAME"); // 授权级别名称
			
			selpo = new TtAsWrAuthinfoPO();
			selpo.setApprovalLevelTier(Utility.getInt(approvalLevelTier));
			
			updatepo = new TtAsWrAuthinfoPO();
			updatepo.setApprovalLevelName(approvalLevelName);
			updatepo.setUpdateBy(logonUser.getUserId());
			updatepo.setUpdateDate(new Date());
			
			
			dao.updateLevel(selpo,updatepo);
			act.setOutData("success", "true");
			act.setOutData("type", type);
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.UPDATE_FAILURE_CODE,"授权级别维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}

}
