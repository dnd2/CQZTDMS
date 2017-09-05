
/**********************************************************************
* <pre>
* FILE : DealerSysUser.java
* CLASS : DealerSysUser
* 
* AUTHOR : ChenLiang
*
* FUNCTION : 经销商用户维护Action.
*
*
*======================================================================
* CHANGE HISTORY LOG
*----------------------------------------------------------------------
* MOD. NO.| DATE     |    NAME    | REASON | CHANGE REQ.
*----------------------------------------------------------------------
*         |2009-09-10| ChenLiang  | Created |
* DESCRIPTION:
* </pre>
***********************************************************************/

package com.infodms.dms.actions.sysmng.usemng;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.MsgCarrier;
import com.infodms.dms.common.ValidateCodeConstant;
import com.infodms.dms.dao.common.CommonDAO;
import com.infodms.dms.dao.usermng.UserMngDAO;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TcPosePO;
import com.infodms.dms.po.TcUserPO;
import com.infodms.dms.po.TmOrgPO;
import com.infodms.dms.po.TrUserPosePO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.businessUtil.Validate;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.POFactory;
import com.infoservice.po3.POFactoryBuilder;
import com.infoservice.po3.bean.PageResult;

public class DealerSysUser {

	public Logger logger = Logger.getLogger(DealerSysUser.class);
	private POFactory factory = POFactoryBuilder.getInstance();
	private final String queryDealerSysUserInitUrl = "/jsp/systemMng/userMng/dealerSysUserSearch.jsp";
	private final String addDealerSysUserInitUrl = "/jsp/systemMng/userMng/dealerSysUserAdd.jsp";
	private final String modfiDealerSysUserInitUrl = "/jsp/systemMng/userMng/dealerSysUserDetail.jsp";
	private final String queryPoseInitUrl = "/jsp/systemMng/userMng/dealerPoseSearch.jsp";

	/**
	 * 查询经销商用户初始化
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public void queryDealerSysUserInit() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword(queryDealerSysUserInitUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"经销商用户");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 添加经销商用户初始化
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public void addDealerSysUserInit() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword(addDealerSysUserInitUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"经销商用户");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 添加职位查询初始化
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public void addPoseQueryInit() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String poseIds = CommonUtils.checkNull(request.getParamValue("POSE_IDS"));
			act.setOutData("poseIds", poseIds);
			act.setForword(queryPoseInitUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"经销商用户");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 构造SGM的部门树形菜单
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public void initOrgTree() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			act.getResponse().setContentType("application/json");
			String rootId = request.getParamValue("tree_root_id"); // 得到树的根节点ID
			
			List<MsgCarrier> vlist = new ArrayList<MsgCarrier>();
			vlist.add(MsgCarrier.getInstance(ValidateCodeConstant.DIGIT_PATTERN,"部门ID",0,Constant.Length_Check_Char_10,rootId));
			Validate.doValidate(act, vlist);
			
			List<TmOrgPO> funList = CommonDAO.createDeptTree(logonUser.getCompanyId(), rootId,logonUser.getUserType(),true);
			act.setOutData("funlist", funList);
		} catch(BizException e){
			logger.error(logonUser,e);
			act.setException(e);
		} catch (Exception e) {
			act.setException(e);
			act.setForword("/error.jsp");
		}
	}
	
	/**
	 * 经销商用户修改初始化
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public void modfiDealerSysUserInit() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String userId = request.getParamValue("userId");
			
			List<MsgCarrier> vlist = new ArrayList<MsgCarrier>();
			vlist.add(MsgCarrier.getInstance(ValidateCodeConstant.DIGIT_PATTERN,"用户ID",1,Constant.Length_Check_Char_10,userId));
			Validate.doValidate(act, vlist);
			
			TcUserPO tcUserPO = new TcUserPO();
			tcUserPO.setUserId(new Long(userId));
			tcUserPO = factory.select(tcUserPO).get(0);
			
			TrUserPosePO trUserPosePO = new TrUserPosePO();
			trUserPosePO.setUserId(new Long(userId));
			List<TrUserPosePO> list = factory.select(trUserPosePO);
			String poseIds = "";
			for (int i = 0; i < list.size(); i++) {
				TrUserPosePO trUserPosePO2 = list.get(i);
				poseIds += trUserPosePO2.getPoseId()+",";
			}
			
			if(!"".equals(poseIds) && poseIds.length()>0) {
				poseIds = poseIds.substring(0, poseIds.length()-1);
			}
			
			act.setOutData("user", tcUserPO);
			act.setOutData("poseIds", poseIds);
			act.setForword(modfiDealerSysUserInitUrl);
		} catch(BizException e){
			logger.error(logonUser,e);
			act.setException(e);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"经销商用户");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 经销商用户修改
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public void modfiDealerSysUser() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			act.getResponse().setContentType("application/json");
			String userId = CommonUtils.checkNull(request.getParamValue("USER_ID"));
			String poseids = CommonUtils.checkNull(request.getParamValue("POSE_IDS"));       // 用户职位ID 
			String name = CommonUtils.checkNull(request.getParamValue("NAME"));              // 姓名
			String empNum = CommonUtils.checkNull(request.getParamValue("EMP_NUM"));         // 用户名
			String gender = CommonUtils.checkNull(request.getParamValue("GENDER"));          // 性别
			String handPhone = CommonUtils.checkNull(request.getParamValue("HAND_PHONE"));   // 手机
			String phone = CommonUtils.checkNull(request.getParamValue("PHONE"));            // 电话
			String email = CommonUtils.checkNull(request.getParamValue("EMAIL"));            // EMail
			String userStatus = CommonUtils.checkNull(request.getParamValue("USER_STATUS"));     // 状态
			
			List<MsgCarrier> vlist = new ArrayList<MsgCarrier>();
			vlist.add(MsgCarrier.getInstance(ValidateCodeConstant.DIGIT_PATTERN,"用户ID",1,Constant.Length_Check_Char_10,userId));
			vlist.add(MsgCarrier.getInstance(ValidateCodeConstant.LETTER_CN_PATTERN,"姓名",1,Constant.Length_Check_Char_6,name));
			vlist.add(MsgCarrier.getInstance(ValidateCodeConstant.DIGIT_PATTERN,"手机",0,Constant.Length_Check_Char_20,handPhone));
			vlist.add(MsgCarrier.getInstance(ValidateCodeConstant.EMAIL_PATTERN,"EMail",0,Constant.Length_Check_Char_50,email));
			Validate.doValidate(act, vlist);
			
			TcUserPO tcUserPO = new TcUserPO();
			tcUserPO.setUserId(new Long(userId));
			
			TcUserPO tcUserPO2 = new TcUserPO();
			tcUserPO2.setName(name);
			tcUserPO2.setGender(Integer.valueOf(gender));
			tcUserPO2.setHandPhone(handPhone);
			tcUserPO2.setEmpNum(empNum);
			tcUserPO2.setPhone(phone);
			tcUserPO2.setEmail(email);
			tcUserPO2.setUserStatus(Integer.valueOf(userStatus));
			factory.update(tcUserPO, tcUserPO2);
			
			TrUserPosePO trUserPosePO = new TrUserPosePO();
			trUserPosePO.setUserId(new Long(userId));
			factory.delete(trUserPosePO);
			
			if(poseids != null && !"".equals(poseids)) {
				String[] poseIds = poseids.split(",");
				TrUserPosePO trUserPosePO2 = null;
				for (int i = 0; i < poseIds.length; i++) { // 存入用户对应的职位
					trUserPosePO2 = new TrUserPosePO();
					trUserPosePO2.setUserPoseId(factory.getLongPK(new TrUserPosePO()));
					trUserPosePO2.setUserId(tcUserPO.getUserId());
					trUserPosePO2.setPoseId(new Long(poseIds[i]));
					factory.insert(trUserPosePO2);
				}
			}
			
			act.setOutData("st", "succeed");
		} catch(BizException e){
			logger.error(logonUser,e);
			act.setException(e);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"经销商用户");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 添加经销商用户
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public void addDealerSysUser() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			act.getResponse().setContentType("application/json");
			String poseids = request.getParamValue("POSE_IDS");       // 用户职位ID 
			String acnt = request.getParamValue("ACNT");              // 用户帐号(认证中心帐号)
			String empNum = request.getParamValue("EMP_NUM");         // 用户名
			String name = request.getParamValue("NAME");              // 姓名
			String gender = request.getParamValue("GENDER");          // 性别
			String handPhone = request.getParamValue("HAND_PHONE");   // 手机
			String phone = request.getParamValue("PHONE");            // 电话
			String email = request.getParamValue("EMAIL");            // EMail
			String userStatus = request.getParamValue("USER_STATUS");     // 状态
			
			List<MsgCarrier> vlist = new ArrayList<MsgCarrier>();
			vlist.add(MsgCarrier.getInstance(ValidateCodeConstant.DIGIT_LETTER_PATTERN,"用户帐号",1,Constant.Length_Check_Char_20,acnt));
			vlist.add(MsgCarrier.getInstance(ValidateCodeConstant.DIGIT_LETTER_PATTERN,"用户名",0,Constant.Length_Check_Char_20,empNum));
			vlist.add(MsgCarrier.getInstance(ValidateCodeConstant.LETTER_CN_PATTERN,"姓名",1,Constant.Length_Check_Char_6,name));
			vlist.add(MsgCarrier.getInstance(ValidateCodeConstant.DIGIT_PATTERN,"手机",0,Constant.Length_Check_Char_20,handPhone));
			vlist.add(MsgCarrier.getInstance(ValidateCodeConstant.EMAIL_PATTERN,"EMail",0,Constant.Length_Check_Char_50,email));
			Validate.doValidate(act, vlist);
			
			TcUserPO tcUserPO1 = new TcUserPO();
			tcUserPO1.setAcnt(acnt);
			List<TcUserPO> userlist = factory.select(tcUserPO1);
			if(userlist!=null && userlist.size()>0) {
				act.setOutData("st", "acnt_error");
				return ;
			}
			
			if(empNum != null && !"".equals(empNum)) {
				TcUserPO tcUserPO2 = new TcUserPO();
				tcUserPO2.setEmpNum(empNum);
				List<TcUserPO> userlist2 = factory.select(tcUserPO2);
				if(userlist2!=null && userlist2.size()>0) {
					act.setOutData("st", "empNum_error");
					return ;
				}
			}
			
			Long dealerId = logonUser.getCompanyId();
			
			TcUserPO tcUserPO = new TcUserPO();
			tcUserPO.setUserId(factory.getLongPK(new TcUserPO()));
			tcUserPO.setCompanyId(new Long(dealerId));
			tcUserPO.setAcnt(acnt);
			tcUserPO.setEmpNum(empNum);
			tcUserPO.setName(name);
			tcUserPO.setGender(Integer.valueOf(gender));
			tcUserPO.setHandPhone(handPhone);
			tcUserPO.setPhone(phone);
			tcUserPO.setEmail(email);
			tcUserPO.setUserStatus(Integer.valueOf(userStatus));
			factory.insert(tcUserPO); // 将用户存入系统
			
			if(poseids != null && !"".equals(poseids)) {
				String[] poseIds = poseids.split(",");
				TrUserPosePO trUserPosePO = null;
				for (int i = 0; i < poseIds.length; i++) { // 存入用户对应的职位
					trUserPosePO = new TrUserPosePO();
					trUserPosePO.setUserPoseId(factory.getLongPK(new TrUserPosePO()));
					trUserPosePO.setUserId(tcUserPO.getUserId());
					trUserPosePO.setPoseId(new Long(poseIds[i]));
					factory.insert(trUserPosePO);
				}
			}
			
			act.setOutData("st", "succeed");
		} catch(BizException e){
			logger.error(logonUser,e);
			act.setException(e);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"经销商用户");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 经销商添加职位查询
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public void addPoseQuery() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			act.getResponse().setContentType("application/json");
			String poseCode = request.getParamValue("POSE_CODE");
			String poseName = request.getParamValue("POSE_NAME");
			String poseIds = request.getParamValue("poseIds"); // 用户当前的职位ID
			
			List<MsgCarrier> vlist = new ArrayList<MsgCarrier>();
			vlist.add(MsgCarrier.getInstance(ValidateCodeConstant.NOQUOTATION_PATTERN,"职位代码",0,Constant.Length_Check_Char_20,poseCode));
			vlist.add(MsgCarrier.getInstance(ValidateCodeConstant.NOQUOTATION_PATTERN,"职位名称",0,Constant.Length_Check_Char_20,poseName));
			Validate.doValidate(act, vlist);
			
			Long dealerId = logonUser.getCompanyId();
			List<TcPosePO> ps = UserMngDAO.getDRLPoseByCompanyId(dealerId,poseCode, poseName,poseIds);
			act.setOutData("ps", ps);
		} catch(BizException e){
			logger.error(logonUser,e);
			act.setException(e);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"经销商用户");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 经销商添加职位查询
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public void addPoseQueryByIPoseIds() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			act.getResponse().setContentType("application/json");
			String poseIds = request.getParamValue("POSE_IDS");
			Long dealerId = logonUser.getCompanyId();
			List<TcPosePO> ps = UserMngDAO.getDRLPoseByPoseIds(dealerId,poseIds);
			act.setOutData("ps", ps);
		} catch(BizException e){
			logger.error(logonUser,e);
			act.setException(e);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"经销商用户");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 经销商查询用户
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public void dealerSysUserQuery() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			act.getResponse().setContentType("application/json");
			if("1".equals(request.getParamValue("COMMAND"))) {
				// 部门
				String deptId = request.getParamValue("DEPT_ID");
				// 用户名
				String empNum = request.getParamValue("EMP_NUM");
				// 姓名
				String name = request.getParamValue("NAME");
				
				List<MsgCarrier> vlist = new ArrayList<MsgCarrier>();
				vlist.add(MsgCarrier.getInstance(ValidateCodeConstant.NOQUOTATION_PATTERN,"部门ID",0,Constant.Length_Check_Char_100,deptId));
				vlist.add(MsgCarrier.getInstance(ValidateCodeConstant.NOQUOTATION_PATTERN,"用户名",0,Constant.Length_Check_Char_100,empNum));
				vlist.add(MsgCarrier.getInstance(ValidateCodeConstant.NOQUOTATION_PATTERN,"姓名",0,Constant.Length_Check_Char_100,name));
				Validate.doValidate(act, vlist);
				
				String orderName = request.getParamValue("orderCol");
				String da = request.getParamValue("order");
				
				Long dealerId = logonUser.getCompanyId();
				Integer curPage = request.getParamValue("curPage") != null ? Integer
						.parseInt(request.getParamValue("curPage"))
						: 1; // 处理当前页
				PageResult<TcUserPO> ps = UserMngDAO.drlSysUserQuery(dealerId,deptId, empNum, name, Constant.PAGE_SIZE,curPage,orderName,da); // 按条件查询系统用户  
				act.setOutData("ps", ps);
			}
		} catch(BizException e){
			logger.error(logonUser,e);
			act.setException(e);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"经销商用户");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
}
