/**   
* @Title: ClaimManMain.java 
* @Package com.infodms.dms.actions.claim.authorization 
* @Description: TODO(授权人员管理action) 
* @author  wangjinbao   
* @date 2010-6-11 上午08:45:27 
* @version V1.0   
*/
package com.infodms.dms.actions.claim.authorization;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.common.ClaimCommonAction;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.common.getCompanyId.GetOemcompanyId;
import com.infodms.dms.dao.claim.authorization.ClaimManDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TcUserPO;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

/** 
 * @ClassName: ClaimManMain 
 * @Description: TODO(授权人员管理action) 
 * @author wangjinbao 
 * @date 2010-6-11 上午08:45:27 
 *  
 */
public class ClaimManMain {
	private Logger logger = Logger.getLogger(ClaimManMain.class);
	private final ClaimManDao dao = ClaimManDao.getInstance();
	private final String CLAIMMAN_URL = "/jsp/claim/authorization/claimManIndex.jsp";//主页面（查询）
	private final String CLAIMMAN_UPDATE_URL = "/jsp/claim/authorization/claimManModify.jsp";//增加（修改）页面
	private final ClaimCommonAction claimCommon = ClaimCommonAction.getInstance();
	
	/**
	 * 
	* @Title: claimManInit 
	* @Description: TODO(索赔结算,授权人员管理查询初始化) 
	* @param    
	* @return void  
	* @throws
	 */
	public void claimManInit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			Long oemCompanyId = GetOemcompanyId.getOemCompanyId(logonUser);
			//第二位标志 1索赔结算 2索赔授权
			act.setOutData("levellist",claimCommon.getWrLevelList("1",String.valueOf(Constant.AUDIT_TYPE_02),oemCompanyId));//授权级别列表 
			act.setOutData("aduitFlag", Constant.AUDIT_TYPE_02);
			act.setForword(CLAIMMAN_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"授权人员管理");
			logger.error(logonUser,e1);
			act.setException(e1);
		}		
	}
	
	/**
	 * 
	* @Title: claimManInit 
	* @Description: TODO(索赔结算,授权人员管理查询初始化) 
	* @param    
	* @return void  
	* @throws
	 */
	public void newClaimManInit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			Long oemCompanyId = GetOemcompanyId.getOemCompanyId(logonUser);
			//第二位标志 1索赔结算 2索赔授权
			act.setOutData("levellist",claimCommon.getWrLevelList("1",String.valueOf(Constant.AUDIT_TYPE_01),oemCompanyId));//授权级别列表 
			act.setOutData("aduitFlag", Constant.AUDIT_TYPE_01);
			act.setForword(CLAIMMAN_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"授权人员管理");
			logger.error(logonUser,e1);
			act.setException(e1);
		}		
	}
	/**
	 * 
	* @Title: claimManQuery 
	* @Description: TODO(索赔授权,授权人员管理查询) 
	* @param    
	* @return void  
	* @throws
	 */
	public void claimManQuery() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		HashMap<String, Comparable> map = null;
		try {
			RequestWrapper request = act.getRequest();
			act.getResponse().setContentType("application/json");
			if("1".equals(request.getParamValue("COMMAND"))){ //开始查询
				// 从session 中取得公司id
				Long oemCompanyId = GetOemcompanyId.getOemCompanyId(logonUser);
				
				String userName = CommonUtils.checkNull(request.getParamValue("USER_NAME"));//用户姓名参数
				String level = CommonUtils.checkNull(request.getParamValue("APPROVAL_LEVEL_CODE"));//授权级别
				String aduitFlag = CommonUtils.checkNull(request.getParamValue("aduitFlag"));//授权，结算的类别
				Integer curPage = request.getParamValue("curPage") != null ? Integer
						.parseInt(request.getParamValue("curPage"))
						: 1; // 处理当前页
				map = new HashMap<String, Comparable>();		
				map.put("NAME", userName);//用户姓名
				map.put("LEVEL", level);//授权级别
				map.put("COMPANYID", logonUser.getCompanyId());//公司id
				map.put("OEMCOMPANYID", oemCompanyId);//授权级别
				map.put("aduitFlag", aduitFlag);
				
				PageResult<Map<String, Object>> ps = dao.claimManQuery(Constant.PAGE_SIZE,curPage,map);
				act.setOutData("ps", ps);
			}
		} catch(BizException e){
			logger.error(logonUser,e);
			act.setException(e);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"授权人员管理");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	* @Title: claimManUpdateInit 
	* @Description: TODO(授权人员管理修改初始化) 
	* @param    
	* @return void  
	* @throws
	 */
	public void claimManUpdateInit() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		Map<String, Object> map = null;
		try {
			RequestWrapper request = act.getRequest();
			String id = request.getParamValue("ID");//用户表的主键user_id
			String auditFlag = request.getParamValue("auditFlag");//结算，授权的标志
			//根据主键获得要修改的人员列表，对应tc_user表
			map = dao.getUserById(id);
		    String code = map.get("APPROVAL_LEVEL_CODE") == null ? "" : map.get("APPROVAL_LEVEL_CODE").toString();
		    Long oemCompanyId = GetOemcompanyId.getOemCompanyId(logonUser);
			act.setOutData("levellist",claimCommon.getWrLevelListCallBack(code,auditFlag, oemCompanyId));//授权级别列表
			act.setOutData("auditFlag", auditFlag);//结算，授权的标志
			act.setOutData("SELMAP", map);//待修改的用户列表
			act.setForword(CLAIMMAN_UPDATE_URL);
		}  catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"授权人员管理");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	* @Title: claimManUpdate 
	* @Description: TODO(授权人员管理修改) 
	* @param    
	* @return void  
	* @throws
	 */
	public void claimManUpdate() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		TcUserPO selpo = null;
		TcUserPO updatepo = null;
		try {
			RequestWrapper request = act.getRequest();
			String id = request.getParamValue("ID");//用户表的主键user_id
			String personCode = CommonUtils.checkId(request.getParamValue("PERSON_CODE"));//授权代码
			String approvalLevelCode = request.getParamValue("APPROVAL_LEVEL_CODE");//授权级别
			String auditFlag = request.getParamValue("auditFlag");
			//修改条件po
			selpo = new TcUserPO();
			selpo.setUserId(Utility.getLong(id));
			//修改po
			updatepo = new TcUserPO();
			updatepo.setUpdateBy(logonUser.getUserId());
			updatepo.setUpdateDate(new Date());
			if(auditFlag.equals(String.valueOf(Constant.AUDIT_TYPE_01))){
				updatepo.setPersonCode(personCode);
				updatepo.setApprovalLevelCode(Utility.getInt(approvalLevelCode));
			}else{
				updatepo.setBalanceLevelCode(Utility.getInt(approvalLevelCode));
			}
			
			dao.updateUser(selpo, updatepo);
			act.setOutData("success", "true");
		}  catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.UPDATE_FAILURE_CODE,"授权人员管理");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}

}
