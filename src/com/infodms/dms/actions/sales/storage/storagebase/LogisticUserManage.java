
/**********************************************************************
* <pre>
* FILE : SgmSysUser.java
* CLASS : SgmSysUser
* 
* AUTHOR : ChenLiang
*
* FUNCTION : 系统用户维护(SGM)Action.
*
*
*======================================================================
* CHANGE HISTORY LOG
*----------------------------------------------------------------------
* MOD. NO.| DATE     |    NAME    | REASON | CHANGE REQ.
*----------------------------------------------------------------------
*         |2009-09-02| ChenLiang  | Created |
* DESCRIPTION:
* </pre>
***********************************************************************/

package com.infodms.dms.actions.sales.storage.storagebase;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.MD5Util;
import com.infodms.dms.common.MsgCarrier;
import com.infodms.dms.common.Utility;
import com.infodms.dms.common.ValidateCodeConstant;
import com.infodms.dms.dao.common.CommonDAO;
import com.infodms.dms.dao.usermng.UserMngDAO;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TcPosePO;
import com.infodms.dms.po.TcUserPO;
import com.infodms.dms.po.TmDealerPO;
import com.infodms.dms.po.TmOrgPO;
import com.infodms.dms.po.TrUserPosePO;
import com.infodms.dms.po.TtAsWrSpecialChargeAuditPO;
import com.infodms.dms.po.TtAsWrSpecialChargePO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.businessUtil.Validate;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.POFactory;
import com.infoservice.po3.POFactoryBuilder;
import com.infoservice.po3.bean.PageResult;

public class LogisticUserManage {

	public Logger logger = Logger.getLogger(LogisticUserManage.class);
	private POFactory factory = POFactoryBuilder.getInstance();
	private final String querySgmSysUserInitUrl = "/jsp/sales/storage/storagebase/logistics/sgmSysUserSearch.jsp";
	private final String specialInt = "/jsp/claim/authorization/specialIndex.jsp";
	private final String specialMager = "/jsp/claim/authorization/specialMager.jsp";
	private final String addSgmSysUserInitUrl = "/jsp/sales/storage/storagebase/logistics/sgmSysUserAdd.jsp";
	private final String modfiSgmSysUserInitUrl = "/jsp/sales/storage/storagebase/logistics/sgmSysUserDetail.jsp";
	private final String queryPoseInitUrl = "/jsp/sales/storage/storagebase/logistics/poseSearch2.jsp";

	/**
	 * 查询系统用户初始化
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public void querySgmSysUserInit() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword(querySgmSysUserInitUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"系统用户");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 添加系统用户初始化
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public void addSgmSysUserInit() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword(addSgmSysUserInitUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"系统用户");
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
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"系统用户");
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
			String dealerId = CommonUtils.checkNull(request.getParamValue("DEALER_ID"));
			
			List<MsgCarrier> vlist = new ArrayList<MsgCarrier>();
			vlist.add(MsgCarrier.getInstance(ValidateCodeConstant.DIGIT_PATTERN,"部门ID",0,Constant.Length_Check_Char_10,rootId));
			Validate.doValidate(act, vlist);
			
			Long companyId = null;
			Integer type;
			if("".equals(dealerId)) { // 查询SGM部门
				companyId = logonUser.getCompanyId();
				type = logonUser.getUserType();
			} else { // 查询经销商部门
				TmDealerPO PO = new TmDealerPO();
				PO.setDealerId(new Long(dealerId));
				companyId = factory.select(PO).get(0).getCompanyId();
				type = Constant.SYS_USER_DEALER;
			}
			List<TmOrgPO> funList = null;
			if(type.equals(Constant.SYS_USER_DEALER))
			{
				funList = CommonDAO.createDealerOrgTree(companyId);
			}else if(type.equals(Constant.SYS_USER_SGM))
			{
				funList = CommonDAO.createDeptTree(companyId, rootId,type,true);	
			}
			act.setOutData("funlist", funList);
		} catch(BizException e){
			logger.error(logonUser,e);
			act.setException(e);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"系统用户");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 系统用户修改初始化
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public void modfiSgmSysUserInit() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String userId = request.getParamValue("userId"); // 用户ID
//			
//			List<MsgCarrier> vlist = new ArrayList<MsgCarrier>();
//			vlist.add(MsgCarrier.getInstance(ValidateCodeConstant.DIGIT_PATTERN,"用户ID",1,Constant.Length_Check_Char_10,userId));
//			Validate.doValidate(act, vlist);
//			
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
			act.setForword(modfiSgmSysUserInitUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"系统用户");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 系统用户修改
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public void modfiSgmSysUser() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			act.getResponse().setContentType("application/json");
			RequestWrapper request = act.getRequest();
			String userId = CommonUtils.checkNull(request.getParamValue("USER_ID"));
			String poseids = CommonUtils.checkNull(request.getParamValue("POSE_IDS"));       // 用户职位ID 
			String name = CommonUtils.checkNull(request.getParamValue("NAME"));              // 姓名
			String gender = CommonUtils.checkNull(request.getParamValue("GENDER"));          // 性别
			String handPhone = CommonUtils.checkNull(request.getParamValue("HAND_PHONE"));   // 手机
			String phone = CommonUtils.checkNull(request.getParamValue("PHONE"));            // 电话
			String email = CommonUtils.checkNull(request.getParamValue("EMAIL"));            // EMail
			String userStatus = CommonUtils.checkNull(request.getParamValue("USER_STATUS"));     // 状态
			String isLock = CommonUtils.checkNull(request.getParamValue("isLock"));     // 新增是否锁定 2012-07-10 韩晓宇
			
			//modify by xiayapeng begin 维护用户时加入 密码维护项
			String password = request.getParamValue("PASSWORD");      //密码
			//modify by xiayanpeng end 
			
			List<MsgCarrier> vlist = new ArrayList<MsgCarrier>();
			//vlist.add(MsgCarrier.getInstance(ValidateCodeConstant.DIGIT_PATTERN,"用户ID",1,Constant.Length_Check_Char_10,userId));
			vlist.add(MsgCarrier.getInstance(ValidateCodeConstant.LETTER_CN_PATTERN,"姓名",1,Constant.Length_Check_Char_10,name));
			vlist.add(MsgCarrier.getInstance(ValidateCodeConstant.DIGIT_PATTERN,"手机",0,Constant.Length_Check_Char_20,handPhone));
			vlist.add(MsgCarrier.getInstance(ValidateCodeConstant.EMAIL_PATTERN,"EMail",0,Constant.Length_Check_Char_50,email));
			Validate.doValidate(act, vlist);
			
			TcUserPO tcUserPO = new TcUserPO();
			tcUserPO.setUserId(new Long(userId));
			
			TcUserPO tcUserPO2 = new TcUserPO();
			tcUserPO2.setName(name);
			tcUserPO2.setGender(Integer.valueOf(gender));
			tcUserPO2.setHandPhone(handPhone);
			tcUserPO2.setPhone(phone);
			tcUserPO2.setEmail(email);
			tcUserPO2.setUserStatus(Integer.valueOf(userStatus));
			tcUserPO2.setIsLock(Integer.valueOf(isLock)); //新增是否锁定更新 2012-07-10 韩晓宇
			//modify by xiayapeng begin 维护用户时加入 密码维护项
			if(password!=null&&password!=""){
			tcUserPO2.setPassword(MD5Util.MD5Encryption(password));
				Calendar cal = Calendar.getInstance();
				if(poseids.indexOf("2010082600119486")>-1){
					cal.add(Calendar.MONTH, 3);
				}else{
					cal.add(Calendar.MONTH, 6);
				}
				tcUserPO2.setOverDate(cal.getTime());
				//TODO 更新密码时自动解锁 2012-07-13 韩晓宇
				tcUserPO2.setIsLock(0);
				//TODO END
			}
			tcUserPO2.setUpdateDate(new Date()); // YH 2010.12.20
			tcUserPO2.setUpdateBy(logonUser.getUserId()); // YH 2010.12.20
			//modify by xiayanpeng end 
			factory.update(tcUserPO, tcUserPO2);
			
			TrUserPosePO trUserPosePO = new TrUserPosePO();
			trUserPosePO.setUserId(new Long(userId));
			factory.delete(trUserPosePO);
			
			String[] poseIds = poseids.split(",");
			TrUserPosePO trUserPosePO2 = null;
			for (int i = 0; i < poseIds.length; i++) { // 存入用户对应的职位
				trUserPosePO2 = new TrUserPosePO();
				trUserPosePO2.setUserPoseId(factory.getLongPK(new TrUserPosePO()));
				trUserPosePO2.setUserId(tcUserPO.getUserId());
				trUserPosePO2.setPoseId(new Long(poseIds[i]));
				trUserPosePO2.setCreateDate(new Date()); //YH 2010.12.20
				trUserPosePO2.setUpdateDate(new Date()); //YH 2010.12.20
				trUserPosePO2.setCreateBy(logonUser.getUserId()); //YH 2010.12.20
				trUserPosePO2.setUpdateBy(logonUser.getUserId()); //YH 2010.12.20
				factory.insert(trUserPosePO2);
			}
			
			act.setOutData("st", "succeed");
		} catch(BizException e){
			logger.error(logonUser,e);
			act.setException(e);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"系统用户");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 添加SGM用户
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public void addSgmSysUser() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			act.getResponse().setContentType("application/json");
			RequestWrapper request = act.getRequest();
			String poseids = request.getParamValue("POSE_IDS");       // 用户职位ID 
			String acnt = request.getParamValue("ACNT").toUpperCase();// 用户帐号(认证中心帐号)
			//String empNum = request.getParamValue("EMP_NUM");         // 用户名
			String name = request.getParamValue("NAME");              // 姓名
			String gender = request.getParamValue("GENDER");          // 性别
			String handPhone = request.getParamValue("HAND_PHONE");   // 手机
			String phone = request.getParamValue("PHONE");            // 电话
			String email = request.getParamValue("EMAIL");            // EMail
			String userStatus = request.getParamValue("USER_STATUS");     // 状态
			//modify by xiayapeng begin 维护用户时加入 密码维护项
			String password = request.getParamValue("PASSWORD");      //密码
			//modify by xiayanpeng end 
			List<MsgCarrier> vlist = new ArrayList<MsgCarrier>();
			vlist.add(MsgCarrier.getInstance(ValidateCodeConstant.DIGIT_LETTER_PATTERN,"用户帐号",1,Constant.Length_Check_Char_20,acnt));
			//vlist.add(MsgCarrier.getInstance(ValidateCodeConstant.DIGIT_LETTER_PATTERN,"用户名",1,Constant.Length_Check_Char_20,empNum));
			vlist.add(MsgCarrier.getInstance(ValidateCodeConstant.LETTER_CN_PATTERN,"姓名",1,Constant.Length_Check_Char_10,name));
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
			
//			TcUserPO tcUserPO2 = new TcUserPO();
//			tcUserPO2.setAcnt(acnt);
//			List<TcUserPO> userlist2 = factory.select(tcUserPO2);
//			if(userlist2!=null && userlist2.size()>0) {
//				act.setOutData("st", "acnt_error");
//				return ;
//			}
			
			TcUserPO tcUserPO = new TcUserPO();
			tcUserPO.setUserId(factory.getLongPK(new TcUserPO()));
			tcUserPO.setCompanyId(new Long(logonUser.getCompanyId()));
			tcUserPO.setAcnt(acnt);
			//tcUserPO.setEmpNum(empNum);
			tcUserPO.setName(name);
			tcUserPO.setGender(Integer.valueOf(gender));
			tcUserPO.setHandPhone(handPhone);
			tcUserPO.setPhone(phone);
			tcUserPO.setEmail(email);
			tcUserPO.setUserStatus(Integer.valueOf(userStatus));
			tcUserPO.setCreateDate(new Date()); // YH 2010.12.20
			tcUserPO.setUpdateDate(new Date()); // YH 2010.12.20
			Calendar cal = Calendar.getInstance();
			if(poseids.indexOf("2010082600119486")>-1){
				cal.add(Calendar.MONTH, 3);
			}else{
				cal.add(Calendar.MONTH, 6);
			}
			tcUserPO.setOverDate(cal.getTime());
			tcUserPO.setCreateBy(logonUser.getUserId()); // YH 2010.12.20
			tcUserPO.setUpdateBy(logonUser.getUserId()); // YH 2010.12.20
			//modify by xiayapeng begin 维护用户时加入 密码维护项
			tcUserPO.setPassword(MD5Util.MD5Encryption(password));
			//modify by xiayanpeng end 
			tcUserPO.setUserType(Constant.SYS_USER_SGM);
			factory.insert(tcUserPO); // 将用户存入系统
			
			String[] poseIds = poseids.split(",");
			TrUserPosePO trUserPosePO = null;
			for (int i = 0; i < poseIds.length; i++) { // 存入用户对应的职位
				trUserPosePO = new TrUserPosePO();
				trUserPosePO.setUserPoseId(factory.getLongPK(new TrUserPosePO()));
				trUserPosePO.setUserId(tcUserPO.getUserId());
				trUserPosePO.setPoseId(new Long(poseIds[i]));
				trUserPosePO.setCreateDate(new Date()); //YH 2010.12.20
				trUserPosePO.setUpdateDate(new Date()); //YH 2010.12.20
				trUserPosePO.setCreateBy(logonUser.getUserId()); //YH 2010.12.20
				trUserPosePO.setUpdateBy(logonUser.getUserId()); //YH 2010.12.20
				factory.insert(trUserPosePO);
			}
			
			act.setOutData("st", "succeed");
		} catch(BizException e){
			logger.error(logonUser,e);
			act.setException(e);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"系统用户");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 添加职位查询
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public void addPoseQuery() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			act.getResponse().setContentType("application/json");
			RequestWrapper request = act.getRequest();
			String poseCode = request.getParamValue("POSE_CODE"); // 职位代码
			String poseName = request.getParamValue("POSE_NAME"); // 职位名称
			String poseIds = logonUser.getPoseId().toString(); // 用户当前的职位ID
			String poseBusType = logonUser.getPoseBusType().toString();
			
			List<MsgCarrier> vlist = new ArrayList<MsgCarrier>();
			vlist.add(MsgCarrier.getInstance(ValidateCodeConstant.NOQUOTATION_PATTERN,"职位代码",0,Constant.Length_Check_Char_20,poseCode));
			vlist.add(MsgCarrier.getInstance(ValidateCodeConstant.NOQUOTATION_PATTERN,"职位名称",0,Constant.Length_Check_Char_20,poseName));
			Validate.doValidate(act, vlist);

			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
			
		///	List<TcPosePO> ps = UserMngDAO.getSGMPoseByCompanyId(poseCode,logonUser.getCompanyId(), poseName,poseIds);
		List<TcPosePO> ps = UserMngDAO.getSGMPoseByCompanyId2(poseCode,logonUser.getCompanyId(), poseName,poseIds,poseBusType);
			act.setOutData("ps", ps);
		} catch(BizException e){
			logger.error(logonUser,e);
			act.setException(e);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"系统用户");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 添加职位查询
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public void addPoseQueryByIPoseIds() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			act.getResponse().setContentType("application/json");
			RequestWrapper request = act.getRequest();
			String poseIds = request.getParamValue("POSE_IDS");
			
			List<TcPosePO> ps = UserMngDAO.getSGMPoseByPoseIds(poseIds,logonUser.getCompanyId());
			act.setOutData("ps", ps);
		} catch(BizException e){
			logger.error(logonUser,e);
			act.setException(e);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"系统用户");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 
	 */
	public void specialInt()
	{
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword(specialInt);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"特殊费用");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void specialQuery()
	{
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String acnt = request.getParamValue("ACNT");
			String name = request.getParamValue("NAME");
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页
			PageResult<Map<String,Object>> ps = UserMngDAO.specialQuery(acnt,name, Constant.PAGE_SIZE,curPage); // 按条件查询系统用户  
			act.setOutData("ps", ps);
			
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"特殊费用查询失败");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void  specialManger()
	{
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
            String userId = request.getParamValue("userId");
			List<TtAsWrSpecialChargePO> list = UserMngDAO.getPosition(userId);
			act.setOutData("kdid", userId);
			act.setOutData("bean", list);
			act.setForword(specialMager);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"特殊费用");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void  specialUpdate()
	{
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String userId = request.getParamValue("kdid");
			TtAsWrSpecialChargeAuditPO auditPO = new TtAsWrSpecialChargeAuditPO();
			auditPO.setUserId(Long.parseLong(userId));
			UserMngDAO dao = new UserMngDAO();
			dao.delete(auditPO);
			String[] speIds = request.getParamValues("checkId");
			if(speIds != null && speIds.length > 0)
			{
				for(String speId : speIds)
				{
					long id = Utility.getLong(SequenceManager.getSequence(""));
					TtAsWrSpecialChargeAuditPO auditPO2 = new TtAsWrSpecialChargeAuditPO();
					auditPO2.setSpaId(id);
					auditPO2.setSpeId(Long.parseLong(speId) );
					auditPO2.setUserId(Long.parseLong(userId));
					dao.insert(auditPO2);
				}
				act.setOutData("retype",1);
				
			}
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"特殊费用");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	
	/**
	 * 查询系统用户
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public void sgmSysUserQuery() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			act.getResponse().setContentType("application/json");
			RequestWrapper request = act.getRequest();

			if("1".equals(request.getParamValue("COMMAND"))) {
				// 部门ID
				String orgId = request.getParamValue("DEPT_ID");
				// 账号
				String acnt = request.getParamValue("ACNT");
				// 姓名
				String name = request.getParamValue("NAME");
				// 物流商名称
				String logiName = request.getParamValue("logiName");
				
//				List<MsgCarrier> vlist = new ArrayList<MsgCarrier>();
//				vlist.add(MsgCarrier.getInstance(ValidateCodeConstant.NOQUOTATION_PATTERN,"部门ID",0,Constant.Length_Check_Char_100,orgId));
//				vlist.add(MsgCarrier.getInstance(ValidateCodeConstant.NOQUOTATION_PATTERN,"账号",0,Constant.Length_Check_Char_100,acnt));
//				vlist.add(MsgCarrier.getInstance(ValidateCodeConstant.NOQUOTATION_PATTERN,"姓名",0,Constant.Length_Check_Char_100,name));
//				Validate.doValidate(act, vlist);
				
				String orderName = request.getParamValue("orderCol");
				String da = request.getParamValue("order");
				
				Integer curPage = request.getParamValue("curPage") != null ? Integer
						.parseInt(request.getParamValue("curPage"))
						: 1; // 处理当前页
				// PageResult<TcUserPO> ps = UserMngDAO.sgmSysUserQuery(orgId,logonUser.getCompanyId(), acnt,name, Constant.PAGE_SIZE,curPage,orderName,da); // 按条件查询系统用户
				PageResult<Map<String,Object>> ps = UserMngDAO.sgmSysUserQueryA2(logiName,logonUser.getPoseBusType(),logonUser.getCompanyId(), acnt,name, Constant.PAGE_SIZE,curPage,orderName,da,logonUser.getUserId(),logonUser.getPoseId()); // 按条件查询系统用户  
				act.setOutData("ps", ps);
			}
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"系统用户");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	
}
