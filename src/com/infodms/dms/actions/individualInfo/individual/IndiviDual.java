
/**********************************************************************
* <pre>
* FILE : IndiviDual.java
* CLASS : IndiviDual
* 
* AUTHOR : ChenLiang
*
* FUNCTION : 个人资料Action.
*
*
*======================================================================
* CHANGE HISTORY LOG
*----------------------------------------------------------------------
* MOD. NO.| DATE     |    NAME    | REASON | CHANGE REQ.
*----------------------------------------------------------------------
*         |2009-09-14| ChenLiang  | Created |
* DESCRIPTION:
* </pre>
***********************************************************************/
package com.infodms.dms.actions.individualInfo.individual;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.POFactory;
import com.infoservice.po3.POFactoryBuilder;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.MsgCarrier;
import com.infodms.dms.common.ValidateCodeConstant;
import com.infodms.dms.dao.common.CommonDAO;
import com.infodms.dms.dao.individualTask.IndividualDAO;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TcFuncPO;
import com.infodms.dms.po.TcPosePO;
import com.infodms.dms.po.TcUserPO;
import com.infodms.dms.po.TmOrgPO;
import com.infodms.dms.po.TrPoseFuncPO;
import com.infodms.dms.util.businessUtil.Validate;

public class IndiviDual {

	public Logger logger = Logger.getLogger(IndiviDual.class);
	private POFactory factory = POFactoryBuilder.getInstance();
	private final String IndiviDualInitURL = "/jsp/individualInfo/individual/individual.jsp";
	private final String funcTreeURL = "/jsp/individualInfo/individual/individual_funlist.jsp";

	/**
	 * 个人资料页面初始化
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public void IndiviDualInit() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			
			TcUserPO tcUserPO = new TcUserPO();
			tcUserPO.setUserId(logonUser.getUserId());
			tcUserPO = factory.select(tcUserPO).get(0); // 获取用户信息
			
			TcPosePO posePO = new TcPosePO();
			posePO.setPoseId(logonUser.getPoseId());
			posePO = factory.select(posePO).get(0);
			
			TmOrgPO orgPO = new TmOrgPO();
			orgPO.setOrgId(posePO.getOrgId());
			orgPO = factory.select(orgPO).get(0);
			
			act.setOutData("thisPose", logonUser.getPoseId());
			act.setOutData("user", tcUserPO);
			act.setOutData("deptName", orgPO.getOrgName());
			act.setOutData("poseNmae", posePO.getPoseName());
			act.setForword(IndiviDualInitURL);
		} catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"个人资料");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 根据用户ID得到用户职位列表
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public void getPoseByUserId() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String userId = request.getParamValue("userId");
			List<TcPosePO> list = IndividualDAO.getPosebyUserId(userId);
			act.setOutData("ps", list);
		} catch(BizException e){
			logger.error(logonUser,e);
			act.setException(e);
		} catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"个人资料");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 根据用户ID得到用户功能列表
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public void getFunByUserId() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			List<TcFuncPO> list = CommonDAO.getFuncByUserId(logonUser.getUserId());
			act.setOutData("funids", list);
		} catch(BizException e){
			logger.error(logonUser,e);
			act.setException(e);
		} catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"个人资料");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void showFunInit() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String poseId = request.getParamValue("poseid"); 
			String userfuns = "";
			TrPoseFuncPO trPoseFuncPO = new TrPoseFuncPO();
			trPoseFuncPO.setPoseId(new Long(poseId));
			List<TrPoseFuncPO> list = factory.select(trPoseFuncPO);
			if(list != null) {
				for(int i=0; i<list.size(); i++) {
					userfuns += list.get(i).getFuncId()+",";
				}
			}
			if(userfuns.length()>0) {
				userfuns = userfuns.substring(0, userfuns.length()-1);
			}
			
			act.setOutData("poseId", poseId);
			act.setOutData("userfuns", userfuns);
			act.setForword(funcTreeURL);
		} catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"个人资料");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	
	
	/**
	 * 构造功能的树形菜单
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public void initFunTree() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			act.getResponse().setContentType("application/json");
			String rootId = request.getParamValue("tree_root_id"); // 得到树的根节点ID
			
			List<MsgCarrier> vlist = new ArrayList<MsgCarrier>();
			vlist.add(MsgCarrier.getInstance(ValidateCodeConstant.DIGIT_PATTERN,"功能ID",0,Constant.Length_Check_Char_10,rootId));
			Validate.doValidate(act, vlist);
			
			List<TcFuncPO> funList = CommonDAO.getFunc(rootId); // 查询树的叶子节点
			act.setOutData("funlist", funList);
		} catch(BizException e){
			logger.error(logonUser,e);
			act.setException(e);
		} catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"个人资料");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
}
