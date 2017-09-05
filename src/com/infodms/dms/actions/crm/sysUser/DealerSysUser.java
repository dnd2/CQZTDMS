package com.infodms.dms.actions.crm.sysUser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.sysmng.usemng.SgmDealerSysUser;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.MD5Util;
import com.infodms.dms.common.MsgCarrier;
import com.infodms.dms.common.ValidateCodeConstant;
import com.infodms.dms.common.getCompanyId.GetOemcompanyId;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.common.CommonDAO;
import com.infodms.dms.dao.crm.sysUser.UserDAO;
import com.infodms.dms.dao.usermng.UserMngDAO;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TPcGroupPO;
import com.infodms.dms.po.TcPosePO;
import com.infodms.dms.po.TcUserPO;
import com.infodms.dms.po.TmCompanyPO;
import com.infodms.dms.po.TmDealerPO;
import com.infodms.dms.po.TmOrgPO;
import com.infodms.dms.po.TmPtPartTypePO;
import com.infodms.dms.po.TmPtSupplierPO;
import com.infodms.dms.po.TrUserPosePO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.businessUtil.Validate;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.POFactory;
import com.infoservice.po3.POFactoryBuilder;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

public class DealerSysUser {
	
	public Logger logger = Logger.getLogger(SgmDealerSysUser.class);
	private POFactory factory = POFactoryBuilder.getInstance();
	private final String querySgmDealerSysUserInitUrl = "/jsp/crm/sysUser/dealerSysUserInit.jsp";
	private final String addSgmDealerSysUserInitUrl = "/jsp/crm/sysUser/dealerSysUserAdd.jsp";
	private final String modfiSgmDealerSysUserInitUrl = "/jsp/crm/sysUser/dealerSysUserModify.jsp";
	private final String queryPoseInitUrl = "/jsp/crm/sysUser/sgmDealerPoseSearch.jsp";
	private final String toPoseListURL = "/jsp/crm/sysUser/userList.jsp";
	private UserDAO dao = UserDAO.getInstance();

	/**
	 * 维护经销商用户查询初始化
	 * 
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public void doInit() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			//yin
			act.setOutData("dutyType", logonUser.getDutyType());
			act.setOutData("companyId", logonUser.getCompanyId());
			//end
			TPcGroupPO tgp=new TPcGroupPO();
			tgp.setDealerId(new Long(logonUser.getDealerId()));
			tgp.setStatus(new Long(Constant.STATUS_ENABLE));
			List<PO> tgpList=dao.select(tgp);
			act.setOutData("tgpList", tgpList);
			act.setForword(querySgmDealerSysUserInitUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "SGM维护经销商用户");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * SGM维护经销商用户添加初始化
	 * 
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public void addSgmDealerSysUserInit() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			//获取经销商所有的组 start  2014-10-30
			TPcGroupPO tgp=new TPcGroupPO();
			tgp.setDealerId(new Long(logonUser.getDealerId()));
			tgp.setStatus(new Long(Constant.STATUS_ENABLE));
			List<PO> tgpList=dao.select(tgp);
			TmCompanyPO companyPO = new TmCompanyPO();
			companyPO.setCompanyId(logonUser.getCompanyId());
			companyPO = factory.select(companyPO).get(0);
			act.setOutData("companyPO",companyPO );
			act.setOutData("tgpList", tgpList);
			//end
			act.setForword(addSgmDealerSysUserInitUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "SGM维护经销商用户");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * SGM维护经销商用户修改初始化
	 * 
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public void uealerUserModifyInit() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String userId = request.getParamValue("userId");

			List<MsgCarrier> vlist = new ArrayList<MsgCarrier>();
			vlist.add(MsgCarrier.getInstance(ValidateCodeConstant.DIGIT_PATTERN, "用户ID", 1, Constant.Length_Check_Char_20, userId));
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
				poseIds += trUserPosePO2.getPoseId() + ",";
			}

			if (!"".equals(poseIds) && poseIds.length() > 0) {
				poseIds = poseIds.substring(0, poseIds.length() - 1);
			}
			TmCompanyPO companyPO = new TmCompanyPO();
			companyPO.setCompanyId(tcUserPO.getCompanyId());
			companyPO = factory.select(companyPO).get(0);
			TcUserPO t=new TcUserPO();
			if(tcUserPO.getParUserId()!=null&&tcUserPO.getParUserId().longValue()!=0){
				t.setUserId(tcUserPO.getParUserId());
				t=(TcUserPO) dao.select(t).get(0);
			}
			//获取经销商所有的组 start  2014-10-30
			TPcGroupPO tgp=new TPcGroupPO();
			tgp.setDealerId(new Long(logonUser.getDealerId()));
			tgp.setStatus(new Long(Constant.STATUS_ENABLE));
			List<PO> tgpList=dao.select(tgp);
			act.setOutData("tgpList", tgpList);
			//end
			
			//获取职位级别性别状态
			String poseRank=null;
			String gender=null;
			String status=null;
			if(tcUserPO.getPoseRank()!=null&&!"".equals(tcUserPO.getPoseRank().toString())&&!"0".equals(tcUserPO.getPoseRank().toString())){
				poseRank=CommonUtils.getCodeDesc(tcUserPO.getPoseRank().toString());
				act.setOutData("poseRank", poseRank);
			}
			if(tcUserPO.getGender()!=null&&!"".equals(tcUserPO.getGender().toString())){
				gender=CommonUtils.getCodeDesc(tcUserPO.getGender().toString());
				act.setOutData("gender", gender);
			}
			if(tcUserPO.getUserStatus()!=null&&!"".equals(tcUserPO.getUserStatus().toString())){
				status=CommonUtils.getCodeDesc(tcUserPO.getUserStatus().toString());
				act.setOutData("status", status);
			}
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
			String entryDate=null;
			if(tcUserPO.getEntryDate()!=null&&!"".equals(tcUserPO.getEntryDate())){
				entryDate=sdf.format(tcUserPO.getEntryDate());
			}
			act.setOutData("user", tcUserPO);
			act.setOutData("parUser", t);
			act.setOutData("poseIds", poseIds);
			act.setOutData("companyName", companyPO.getCompanyShortname());
			act.setOutData("entryDate", entryDate);
			act.setForword(modfiSgmDealerSysUserInitUrl);
		} catch (BizException e) {
			logger.error(logonUser, e);
			act.setException(e);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "SGM维护经销商用户");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 添加职位查询初始化
	 * 
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public void addPoseQueryInit() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String comapnyID = request.getParamValue("companyId"); // 公司ID
			String poseIds = CommonUtils.checkNull(request.getParamValue("POSE_IDS"));
			// modify by  begin先生成ID格式 不满足一下校验逻辑
			List<MsgCarrier> vlist = new ArrayList<MsgCarrier>();
			vlist.add(MsgCarrier.getInstance(ValidateCodeConstant.DIGIT_PATTERN, "公司ID", 1, Constant.Length_Check_Char_10, comapnyID));

			act.setOutData("poseIds", poseIds);
			act.setOutData("comapnyID", comapnyID);
			act.setForword(queryPoseInitUrl);
		} catch (BizException e) {
			logger.error(logonUser, e);
			act.setException(e);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "SGM维护经销商用户");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 构造SGM的部门树形菜单
	 * 
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public void initOrgTree() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			act.getResponse().setContentType("application/json");
			String rootId = request.getParamValue("tree_root_id"); // 得到树的根节点ID
			String orgId = request.getParamValue("ORGID");
			String regCode = request.getParamValue("regCode");
			List<MsgCarrier> vlist = new ArrayList<MsgCarrier>();
			// vlist.add(MsgCarrier.getInstance(ValidateCodeConstant.DIGIT_PATTERN,"部门ID",0,Constant.Length_Check_Char_10,rootId));
			Validate.doValidate(act, vlist);

			List<TmOrgPO> funList = CommonDAO.createDeptTree(GetOemcompanyId.getOemCompanyId(logonUser), orgId, logonUser.getUserType(), true);
			act.setOutData("funlist", funList);
		} catch (BizException e) {
			logger.error(logonUser, e);
			act.setException(e);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "SGM维护经销商用户");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 构造SGM的部门树形菜单 只显示登录用户所在大区及所属小区
	 * 
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public void initOrgTree3() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			act.getResponse().setContentType("application/json");
			String rootId = request.getParamValue("tree_root_id"); // 得到树的根节点ID
			String orgId = request.getParamValue("ORGID");
			String regCode = request.getParamValue("regCode");
			List<MsgCarrier> vlist = new ArrayList<MsgCarrier>();
			// vlist.add(MsgCarrier.getInstance(ValidateCodeConstant.DIGIT_PATTERN,"部门ID",0,Constant.Length_Check_Char_10,rootId));
			Validate.doValidate(act, vlist);

			List<TmOrgPO> funList = CommonDAO.createDeptTree3(GetOemcompanyId.getOemCompanyId(logonUser), orgId, logonUser.getUserType(), true);
			act.setOutData("funlist", funList);
		} catch (BizException e) {
			logger.error(logonUser, e);
			act.setException(e);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "SGM维护经销商用户");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 构造SGM的部门树形菜单 只显示登录用户所在大区及所属小区
	 * 
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public void initOrgTree5() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			act.getResponse().setContentType("application/json");
			String rootId = request.getParamValue("tree_root_id"); // 得到树的根节点ID
			String orgId = request.getParamValue("ORGID");
			String regCode = request.getParamValue("regCode");
			List<MsgCarrier> vlist = new ArrayList<MsgCarrier>();
			// vlist.add(MsgCarrier.getInstance(ValidateCodeConstant.DIGIT_PATTERN,"部门ID",0,Constant.Length_Check_Char_10,rootId));
			Validate.doValidate(act, vlist);

			List<TmOrgPO> funList = CommonDAO.createDeptTree4(GetOemcompanyId.getOemCompanyId(logonUser), orgId, logonUser.getUserType(), true);
			act.setOutData("funlist", funList);
		} catch (BizException e) {
			logger.error(logonUser, e);
			act.setException(e);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "SGM维护经销商用户");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	public void initOrgTreeDealer() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			act.getResponse().setContentType("application/json");
			String rootId = request.getParamValue("tree_root_id"); // 得到树的根节点ID
			String orgId = request.getParamValue("ORGID");
			String regCode = request.getParamValue("regCode");
			List<MsgCarrier> vlist = new ArrayList<MsgCarrier>();
			// vlist.add(MsgCarrier.getInstance(ValidateCodeConstant.DIGIT_PATTERN,"部门ID",0,Constant.Length_Check_Char_10,rootId));
			Validate.doValidate(act, vlist);

			List<TmOrgPO> funList = CommonDAO.createDeptTree(GetOemcompanyId.getOemCompanyId(logonUser), orgId, logonUser.getUserType(), true);
			act.setOutData("funlist", funList);
		} catch (BizException e) {
			logger.error(logonUser, e);
			act.setException(e);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "SGM维护经销商用户");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	
	/**
	 * SGM维护经销商用户修改
	 * 
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public void modfiSgmDealerSysUser() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			act.getResponse().setContentType("application/json");
			String userId = CommonUtils.checkNull(request.getParamValue("USER_ID"));
			String poseids = CommonUtils.checkNull(request.getParamValue("POSE_IDS")); // 用户职位ID
			String name = CommonUtils.checkNull(request.getParamValue("NAME")); // 姓名
			String gender = CommonUtils.checkNull(request.getParamValue("GENDER")); // 性别
			String handPhone = CommonUtils.checkNull(request.getParamValue("HAND_PHONE")); // 手机
			String phone = CommonUtils.checkNull(request.getParamValue("PHONE")); // 电话
			String email = CommonUtils.checkNull(request.getParamValue("EMAIL")); // EMail
			String userStatus = CommonUtils.checkNull(request.getParamValue("USER_STATUS")); // 状态
			String groupId=CommonUtils.checkNull(request.getParamValue("groupId")); // 用户组

			// modify by xiayapeng begin 维护用户时加入 密码维护项
			String password = request.getParamValue("PASSWORD"); // 密码
			// modify by xiayanpeng end

			List<MsgCarrier> vlist = new ArrayList<MsgCarrier>();
			vlist.add(MsgCarrier.getInstance(ValidateCodeConstant.DIGIT_PATTERN, "用户ID", 1, Constant.Length_Check_Char_10, userId));
			vlist.add(MsgCarrier.getInstance(ValidateCodeConstant.LETTER_CN_PATTERN, "姓名", 1, Constant.Length_Check_Char_10, name));
			vlist.add(MsgCarrier.getInstance(ValidateCodeConstant.DIGIT_PATTERN, "手机", 0, Constant.Length_Check_Char_20, handPhone));
			vlist.add(MsgCarrier.getInstance(ValidateCodeConstant.EMAIL_PATTERN, "EMail", 0, Constant.Length_Check_Char_50, email));
			Validate.doValidate(act, vlist);

			TcUserPO tcUserPO = new TcUserPO();
			tcUserPO.setUserId(new Long(userId));

			TcUserPO tcUserPO2 = new TcUserPO();
			tcUserPO2.setGroupId("".endsWith(groupId)?null:new Long(groupId));
			tcUserPO2.setName(name);
			tcUserPO2.setGender(Integer.valueOf(gender));
			tcUserPO2.setHandPhone(handPhone);
			tcUserPO2.setPhone(phone);
			tcUserPO2.setEmail(email);
			tcUserPO2.setUserStatus(Integer.valueOf(userStatus));

			// modify by begin 维护用户时加入 密码维护项
			tcUserPO2.setPassword(MD5Util.MD5Encryption(password));
			tcUserPO2.setUpdateDate(new Date()); // YH 2010.12.20
			tcUserPO2.setUpdateBy(logonUser.getUserId()); // YH 2010.12.20
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.MONTH, 6);
			tcUserPO2.setOverDate(cal.getTime());
			// modify by  end
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
				trUserPosePO2.setCreateDate(new Date()); // YH 2010.12.20
				trUserPosePO2.setUpdateDate(new Date()); // YH 2010.12.20
				trUserPosePO2.setCreateBy(logonUser.getUserId()); // YH
				trUserPosePO2.setUpdateBy(logonUser.getUserId()); // YH
				factory.insert(trUserPosePO2);
			}

			act.setOutData("st", "succeed");
		} catch (BizException e) {
			logger.error(logonUser, e);
			act.setException(e);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "SGM维护经销商用户");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * SGM维护经销商用户添加
	 * 
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public void addSgmDealerSysUser() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			act.getResponse().setContentType("application/json");
			String poseids = request.getParamValue("POSE_IDS"); // 用户职位ID
			String acnt = request.getParamValue("ACNT");// 用户帐号(认证中心帐号)
			acnt = acnt.toUpperCase();//账号转行为大写
			String name = request.getParamValue("NAME"); // 姓名
			String gender = request.getParamValue("GENDER"); // 性别
			String handPhone = request.getParamValue("HAND_PHONE"); // 手机
			String phone = request.getParamValue("PHONE"); // 电话
			String email = request.getParamValue("EMAIL"); // EMail
			String userStatus = request.getParamValue("USER_STATUS"); // 状态
			String companyId = request.getParamValue("COMPANY_ID"); // 经销商ID

			// modify by  begin 维护用户时加入 密码维护项
			String password = request.getParamValue("PASSWORD"); // 密码
			String groupId=CommonUtils.checkNull(request.getParamValue("groupId"));
			String par_user_id=CommonUtils.checkNull(request.getParamValue("par_user_id"));
			String poseRank=CommonUtils.checkNull(request.getParamValue("poseRank"));
			String entryDate=CommonUtils.checkNull(request.getParamValue("entryDate"));
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-mm-dd");
			// modify by  end

			List<MsgCarrier> vlist = new ArrayList<MsgCarrier>();
			vlist.add(MsgCarrier.getInstance(ValidateCodeConstant.DIGIT_LETTER_PATTERN, "用户帐号", 1, Constant.Length_Check_Char_20, acnt));
			vlist.add(MsgCarrier.getInstance(ValidateCodeConstant.LETTER_CN_PATTERN, "姓名", 1, Constant.Length_Check_Char_10, name));
			vlist.add(MsgCarrier.getInstance(ValidateCodeConstant.DIGIT_PATTERN, "手机", 0, Constant.Length_Check_Char_20, handPhone));
			vlist.add(MsgCarrier.getInstance(ValidateCodeConstant.EMAIL_PATTERN, "EMail", 0, Constant.Length_Check_Char_50, email));
			Validate.doValidate(act, vlist);

			TcUserPO tcUserPO1 = new TcUserPO();
			tcUserPO1.setAcnt(acnt);
			List<TcUserPO> userlist = factory.select(tcUserPO1);
			if (userlist != null && userlist.size() > 0) {
				act.setOutData("st", "acnt_error");
				return;
			}
			TcUserPO tcUserPO = new TcUserPO();
			tcUserPO.setUserId(factory.getLongPK(new TcUserPO()));
			tcUserPO.setCompanyId(new Long(companyId));
			tcUserPO.setDealerId(new Long(logonUser.getDealerId()));
			tcUserPO.setAcnt(acnt);
			tcUserPO.setName(name);
			tcUserPO.setGender(Integer.valueOf(gender));
			tcUserPO.setHandPhone(handPhone);
			tcUserPO.setPhone(phone);
			tcUserPO.setEmail(email);
			tcUserPO.setUserStatus(Integer.valueOf(userStatus));
			tcUserPO.setCreateDate(new Date()); // YH 2010.12.20
			tcUserPO.setUpdateDate(new Date()); // YH 2010.12.20
			tcUserPO.setEntryDate(sdf.parse(entryDate));
			if(groupId!=null&&!"".equals(groupId)){
				tcUserPO.setGroupId(new Long(groupId));
			}
			if(par_user_id!=null&&!"".equals(par_user_id)){
				tcUserPO.setParUserId(new Long(par_user_id));
			}
			if(poseRank!=null&&!"".equals(poseRank)){
				tcUserPO.setPoseRank(new Long(poseRank));
			}
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.MONTH, 6);
			tcUserPO.setOverDate(cal.getTime());
			tcUserPO.setCreateBy(logonUser.getUserId()); // YH 2010.12.20
			tcUserPO.setUpdateBy(logonUser.getUserId()); // YH 2010.12.20
			tcUserPO.setPassword(MD5Util.MD5Encryption(password));
			tcUserPO.setUserType(Constant.SYS_USER_DEALER);
			factory.insert(tcUserPO); // 将用户存入系统

			if (poseids != null && !"".equals(poseids)) {
				String[] poseIds = poseids.split(",");
				TrUserPosePO trUserPosePO = null;
				for (int i = 0; i < poseIds.length; i++) { // 存入用户对应的职位
					trUserPosePO = new TrUserPosePO();
					trUserPosePO.setUserPoseId(factory.getLongPK(new TrUserPosePO()));
					trUserPosePO.setUserId(tcUserPO.getUserId());
					trUserPosePO.setPoseId(new Long(poseIds[i]));
					trUserPosePO.setCreateDate(new Date()); // YH 2010.12.20
					trUserPosePO.setUpdateDate(new Date()); // YH 2010.12.20
					trUserPosePO.setCreateBy(logonUser.getUserId()); // YH
					// 2010.12.20
					trUserPosePO.setUpdateBy(logonUser.getUserId()); // YH
					// 2010.12.20
					factory.insert(trUserPosePO);
				}
			}

			act.setOutData("st", "succeed");
		} catch (BizException e) {
			logger.error(logonUser, e);
			act.setException(e);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "SGM维护经销商用户");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * SGM经销商用户修改
	 * 
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public void modfiDealerSysUser() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			act.getResponse().setContentType("application/json");
			String userId = CommonUtils.checkNull(request.getParamValue("USER_ID"));
			String poseids = CommonUtils.checkNull(request.getParamValue("POSE_IDS")); // 用户职位ID
			String name = CommonUtils.checkNull(request.getParamValue("NAME")); // 姓名
			String gender = CommonUtils.checkNull(request.getParamValue("GENDER")); // 性别
			String handPhone = CommonUtils.checkNull(request.getParamValue("HAND_PHONE")); // 手机
			String phone = CommonUtils.checkNull(request.getParamValue("PHONE")); // 电话
			String email = CommonUtils.checkNull(request.getParamValue("EMAIL")); // EMail
			String userStatus = CommonUtils.checkNull(request.getParamValue("USER_STATUS")); // 状态
			String password = CommonUtils.checkNull(request.getParamValue("PASSWORD")); // 密码
			String isLock = CommonUtils.checkNull(request.getParamValue("isLock")); // 新增是否锁定
			String groupId=CommonUtils.checkNull(request.getParamValue("groupId"));
			String par_user_id=CommonUtils.checkNull(request.getParamValue("par_user_id"));
			String poseRank=CommonUtils.checkNull(request.getParamValue("poseRank"));
			String entryDate=CommonUtils.checkNull(request.getParamValue("entryDate"));

			List<MsgCarrier> vlist = new ArrayList<MsgCarrier>();
			vlist.add(MsgCarrier.getInstance(ValidateCodeConstant.DIGIT_PATTERN, "用户ID", 1, Constant.Length_Check_Char_20, userId));
			// vlist.add(MsgCarrier.getInstance(ValidateCodeConstant.LETTER_CN_PATTERN,"姓名",1,Constant.Length_Check_Char_10,name));
			vlist.add(MsgCarrier.getInstance(ValidateCodeConstant.DIGIT_PATTERN, "手机", 0, Constant.Length_Check_Char_20, handPhone));
			vlist.add(MsgCarrier.getInstance(ValidateCodeConstant.EMAIL_PATTERN, "EMail", 0, Constant.Length_Check_Char_50, email));
			Validate.doValidate(act, vlist);

			TcUserPO tcUserPO = new TcUserPO();
			tcUserPO.setUserId(new Long(userId));

			TcUserPO tcUserPO2 = new TcUserPO();
			if(groupId!=null&&!"".equals(groupId)){
				tcUserPO2.setGroupId(new Long(groupId));
			}else{
				tcUserPO2.setGroupId(new Long(0));
			}
			if(par_user_id!=null&&!"".equals(par_user_id)){
				tcUserPO2.setParUserId(new Long(par_user_id));
			}else{
				tcUserPO2.setParUserId(new Long(0));
			}
			if(poseRank!=null&&!"".equals(poseRank)){
				tcUserPO2.setPoseRank(new Long(poseRank));
			}
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-mm-dd");
			if(entryDate!=null&&!"".equals(entryDate)){
				tcUserPO2.setEntryDate(sdf.parse(entryDate));
			}
			tcUserPO2.setName(name);
			tcUserPO2.setGender(Integer.valueOf(gender));
			tcUserPO2.setHandPhone(handPhone);
			tcUserPO2.setPhone(phone);
			tcUserPO2.setEmail(email);
			tcUserPO2.setUserStatus(Integer.valueOf(userStatus));
			tcUserPO2.setIsLock(Integer.valueOf(isLock)); // 新增是否锁定更新
			// 
			if (password != null && password != "") {
				tcUserPO2.setPassword(MD5Util.MD5Encryption(password));// 密码
				Calendar cal = Calendar.getInstance();
				cal.add(Calendar.MONTH, 6);
				tcUserPO2.setOverDate(cal.getTime());
				// TODO  韩晓宇
				tcUserPO2.setIsLock(0);
				// TODO END
			}
			tcUserPO2.setUpdateDate(new Date()); // YH 2010.12.20
			tcUserPO2.setUpdateBy(logonUser.getUserId()); // YH 2010.12.20
			factory.update(tcUserPO, tcUserPO2);

			TrUserPosePO trUserPosePO = new TrUserPosePO();
			trUserPosePO.setUserId(new Long(userId));
			factory.delete(trUserPosePO);

			if (poseids != null && !"".equals(poseids)) {
				String[] poseIds = poseids.split(",");
				TrUserPosePO trUserPosePO2 = null;
				for (int i = 0; i < poseIds.length; i++) { // 存入用户对应的职位
					trUserPosePO2 = new TrUserPosePO();
					trUserPosePO2.setUserPoseId(factory.getLongPK(new TrUserPosePO()));
					trUserPosePO2.setUserId(tcUserPO.getUserId());
					trUserPosePO2.setPoseId(new Long(poseIds[i]));
					trUserPosePO2.setCreateDate(new Date()); // YH 2010.12.20
					trUserPosePO2.setUpdateDate(new Date()); // YH 2010.12.20
					trUserPosePO2.setCreateBy(logonUser.getUserId()); // YH
					// 2010.12.20
					trUserPosePO2.setUpdateBy(logonUser.getUserId()); // YH
					// 2010.12.20
					factory.insert(trUserPosePO2);
				}
			}

			//  begin 修改用户表里面公司ID
			String COMPANY_ID = CommonUtils.checkNull(request.getParamValue("COMPANY_ID"));
			TcUserPO tcUserPO1 = new TcUserPO();
			tcUserPO1.setUserId(Long.parseLong(userId));
			TcUserPO tcUserPO1Value = new TcUserPO();
			tcUserPO1Value.setCompanyId(Long.parseLong(COMPANY_ID));
			tcUserPO1Value.setUpdateBy(logonUser.getUserId());
			tcUserPO1Value.setUpdateDate(new Date());
			factory.update(tcUserPO1, tcUserPO1Value);
			//  add 2011-6-21 end

			act.setOutData("st", "succeed");
		} catch (BizException e) {
			logger.error(logonUser, e);
			act.setException(e);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商用户");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * SGM维护经销商用户选择经销商查询
	 * 
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public void drlQuery() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			act.getResponse().setContentType("application/json");

			if ("1".equals(request.getParamValue("COMMAND"))) {
				String orgId = request.getParamValue("DEPT_ID");
				String dcode = request.getParamValue("DRLCODE");
				String dsname = request.getParamValue("DELSNAME");

				String orderName = request.getParamValue("orderCol2");
				String da = request.getParamValue("order2");

				Integer curPage = request.getParamValue("curPage2") != null ? Integer.parseInt(request.getParamValue("curPage2")) : 1; // 处理当前页
				PageResult<TmDealerPO> ps = UserMngDAO.getDRLByDeptId(orgId, dcode, dsname, GetOemcompanyId.getOemCompanyId(logonUser), Constant.PAGE_SIZE, curPage, orderName, da);
				act.setOutData("ps", ps);
			}
		} catch (BizException e) {
			logger.error(logonUser, e);
			act.setException(e);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "SGM维护经销商用户");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * SGM维护经销商用户选择经销商查询
	 * 
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public void allDrlQuery() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			act.getResponse().setContentType("application/json");

			if ("1".equals(request.getParamValue("COMMAND"))) {
				String orgId = request.getParamValue("DEPT_ID");
				String dcode = request.getParamValue("DRLCODE");
				String dsname = request.getParamValue("DELSNAME");
				String isDealerType = request.getParamValue("isDealerType");
				String orderName = request.getParamValue("orderCol2");
				String da = request.getParamValue("order2");
				String inputOrgId = request.getParamValue("ORGID");// 页面输入框的组织ID，由操作人自己控制。
				// added by andy.ten@tom.com
				String isMulti = request.getParamValue("isMulti");
				int page_size = Constant.PAGE_SIZE_MAX;// Constant.PAGE_SIZE,Constant.PAGE_SIZE_MAX;
				// //;
				// added by hxy 2012-04-23 新增经销商评级为退网时不显示
				String isLogoutDealerClass = request.getParamValue("isLogoutDealerClass");// 是否为退网经销商评级
				if (isLogoutDealerClass == null) {
					isLogoutDealerClass = String.valueOf(Constant.DEALER_CLASS_TYPE_13);
				}
				if (isMulti != null && !"".equals(isMulti)) {
					page_size = 20;
				}

				String provinceId = request.getParamValue("downtown");
				if (provinceId == null) {
					provinceId = "";
				}
				String dealerClass = request.getParamValue("dealerClass");
				if (dealerClass == null) {
					dealerClass = "";
				}

				String dealerType = request.getParamValue("dealerType");
				if (dealerType == null) {
					dealerType = "";
				}

				String isAllLevel = request.getParamValue("isAllLevel");
				if (isAllLevel == null || isAllLevel.equals("") || isAllLevel.equals("null")) {
					isAllLevel = "false";
				}
				String isAllArea = request.getParamValue("isAllArea");
				if (isAllArea == null || isAllArea.equals("") || isAllArea.equals("null")) {
					isAllArea = "false";
				}
				String areaId = request.getParamValue("AREA_ID");

				// end
				Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
				// modify by zhaojinyu 2010-06-12 经销商树中售后只查询售后的经销商，销售只查询销售的经销商。
				Integer poseBusType = logonUser.getPoseBusType();
				Long poseId = logonUser.getPoseId();
				PageResult<TmDealerPO> ps = UserMngDAO.getAllDRLByDeptId(areaId, isDealerType, orgId, dcode, dsname, GetOemcompanyId.getOemCompanyId(logonUser), provinceId, dealerClass, dealerType, page_size, curPage, orderName, da, inputOrgId, poseBusType, poseId, isAllLevel, isAllArea, isLogoutDealerClass);
				act.setOutData("ps", ps);
			}
		} catch (BizException e) {
			logger.error(logonUser, e);
			act.setException(e);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "SGM维护经销商用户");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * SGM维护经销商用户选择经销商查询
	 * 
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public void allDealerQuery() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			act.getResponse().setContentType("application/json");

			String orgId = request.getParamValue("DEPT_ID");
			if (orgId == null) {
				orgId = request.getParamValue("ORGID");
			}
			String dcode = request.getParamValue("DRLCODE");
			String dsname = request.getParamValue("DELSNAME");
			String isMulti = request.getParamValue("isMulti");

			int page_size = Constant.PAGE_SIZE_MAX;// Constant.PAGE_SIZE,Constant.PAGE_SIZE_MAX;

			if (isMulti != null && !"".equals(isMulti)) {
				page_size = 20;
			}

			String isAllLevel = request.getParamValue("isAllLevel");

			Integer curPage = request.getParamValue("curPage2") != null ? Integer.parseInt(request.getParamValue("curPage2")) : 1; // 处理当前页
			PageResult<TmDealerPO> ps = UserMngDAO.getAllDRLByDeptId2(orgId, dcode, dsname, page_size, curPage, isAllLevel);
			act.setOutData("ps", ps);
		} catch (BizException e) {
			logger.error(logonUser, e);
			act.setException(e);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "SGM维护经销商用户");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * SGM维护经销商用户选择经销商查询
	 * 
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public void allDrlQuery3() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			act.getResponse().setContentType("application/json");

			if ("1".equals(request.getParamValue("COMMAND"))) {
				String orgId = request.getParamValue("DEPT_ID");
				String dcode = request.getParamValue("DRLCODE");
				String dsname = request.getParamValue("DELSNAME");
				String isDealerType = request.getParamValue("isDealerType");
				String orderName = request.getParamValue("orderCol2");
				String da = request.getParamValue("order2");
				String inputOrgId = request.getParamValue("ORGID");// 页面输入框的组织ID，由操作人自己控制。
				// added by andy.ten@tom.com
				String isMulti = request.getParamValue("isMulti");
				int page_size = Constant.PAGE_SIZE_MAX;// Constant.PAGE_SIZE,Constant.PAGE_SIZE_MAX;
				// //;
				// added by hxy 2012-04-23 新增经销商评级为退网时不显示
				String isLogoutDealerClass = request.getParamValue("isLogoutDealerClass");// 是否为退网经销商评级
				if (isLogoutDealerClass == null) {
					isLogoutDealerClass = String.valueOf(Constant.DEALER_CLASS_TYPE_13);
				}
				if (isMulti != null && !"".equals(isMulti)) {
					page_size = 20;
				}

				String provinceId = request.getParamValue("downtown");
				if (provinceId == null) {
					provinceId = "";
				}
				String dealerClass = request.getParamValue("dealerClass");
				if (dealerClass == null) {
					dealerClass = "";
				}

				String dealerType = request.getParamValue("dealerType");
				if (dealerType == null) {
					dealerType = "";
				}

				String isAllLevel = request.getParamValue("isAllLevel");
				if (isAllLevel == null || isAllLevel.equals("") || isAllLevel.equals("null")) {
					isAllLevel = "false";
				}
				String isAllArea = request.getParamValue("isAllArea");
				if (isAllArea == null || isAllArea.equals("") || isAllArea.equals("null")) {
					isAllArea = "false";
				}
				String areaId = request.getParamValue("AREA_ID");

				// end
				Integer curPage = request.getParamValue("curPage2") != null ? Integer.parseInt(request.getParamValue("curPage2")) : 1; // 处理当前页
				// modify by zhaojinyu 2010-06-12 经销商树中售后只查询售后的经销商，销售只查询销售的经销商。
				Integer poseBusType = logonUser.getPoseBusType();
				Long poseId = logonUser.getPoseId();
				PageResult<TmDealerPO> ps = UserMngDAO.getAllDRLByDeptId3(areaId, isDealerType, orgId, dcode, dsname, GetOemcompanyId.getOemCompanyId(logonUser), provinceId, dealerClass, dealerType, page_size, curPage, orderName, da, inputOrgId, poseBusType, poseId, isAllLevel, isAllArea, isLogoutDealerClass);
				act.setOutData("ps", ps);
			}
		} catch (BizException e) {
			logger.error(logonUser, e);
			act.setException(e);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "SGM维护经销商用户");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	public void allDrlQuery4() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			act.getResponse().setContentType("application/json");

			if ("1".equals(request.getParamValue("COMMAND"))) {
				String orgId = request.getParamValue("DEPT_ID");
				String dcode = request.getParamValue("DRLCODE");
				String dsname = request.getParamValue("DELSNAME");
				String isDealerType = request.getParamValue("isDealerType");
				String orderName = request.getParamValue("orderCol2");
				String da = request.getParamValue("order2");
				String inputOrgId = request.getParamValue("ORGID");// 页面输入框的组织ID，由操作人自己控制。
				// added by andy.ten@tom.com
				String isMulti = request.getParamValue("isMulti");
				int page_size = Constant.PAGE_SIZE_MAX;// Constant.PAGE_SIZE,Constant.PAGE_SIZE_MAX;
				// //;
				// added by hxy 2012-04-23 新增经销商评级为退网时不显示
				String isLogoutDealerClass = request.getParamValue("isLogoutDealerClass");// 是否为退网经销商评级
				if (isLogoutDealerClass == null) {
					isLogoutDealerClass = String.valueOf(Constant.DEALER_CLASS_TYPE_13);
				}
				if (isMulti != null && !"".equals(isMulti)) {
					page_size = 20;
				}

				String provinceId = request.getParamValue("downtown");
				if (provinceId == null) {
					provinceId = "";
				}
				String dealerClass = request.getParamValue("dealerClass");
				if (dealerClass == null) {
					dealerClass = "";
				}

				String dealerType = request.getParamValue("dealerType");
				if (dealerType == null) {
					dealerType = "";
				}

				String isAllLevel = request.getParamValue("isAllLevel");
				if (isAllLevel == null || isAllLevel.equals("") || isAllLevel.equals("null")) {
					isAllLevel = "false";
				}
				String isAllArea = request.getParamValue("isAllArea");
				if (isAllArea == null || isAllArea.equals("") || isAllArea.equals("null")) {
					isAllArea = "false";
				}
				String areaId = request.getParamValue("AREA_ID");

				// end
				Integer curPage = request.getParamValue("curPage2") != null ? Integer.parseInt(request.getParamValue("curPage2")) : 1; // 处理当前页
				// modify by zhaojinyu 2010-06-12 经销商树中售后只查询售后的经销商，销售只查询销售的经销商。
				Integer poseBusType = logonUser.getPoseBusType();
				Long poseId = logonUser.getPoseId();
				PageResult<TmDealerPO> ps = UserMngDAO.getAllDRLByDeptId4(areaId, isDealerType, orgId, dcode, dsname, GetOemcompanyId.getOemCompanyId(logonUser), provinceId, dealerClass, dealerType, page_size, curPage, orderName, da, inputOrgId, poseBusType, poseId, isAllLevel, isAllArea, isLogoutDealerClass);
				act.setOutData("ps", ps);
			}
		} catch (BizException e) {
			logger.error(logonUser, e);
			act.setException(e);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "SGM维护经销商用户");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	public void allDrlQueryCon() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			act.getResponse().setContentType("application/json");

			if ("1".equals(request.getParamValue("COMMAND"))) {

				String userId = request.getParamValue("userId");

				String orgId = request.getParamValue("DEPT_ID");
				String dcode = request.getParamValue("DRLCODE");
				String dsname = request.getParamValue("DELSNAME");
				String isDealerType = request.getParamValue("isDealerType");
				String orderName = request.getParamValue("orderCol2");
				String da = request.getParamValue("order2");
				String inputOrgId = request.getParamValue("ORGID");// 页面输入框的组织ID，由操作人自己控制。
				// added by andy.ten@tom.com
				String isMulti = request.getParamValue("isMulti");
				int page_size = Constant.PAGE_SIZE_MAX;// Constant.PAGE_SIZE,Constant.PAGE_SIZE_MAX;
				// //;
				if (isMulti != null && !"".equals(isMulti)) {
					page_size = 20;
				}

				String provinceId = request.getParamValue("downtown");
				if (provinceId == null) {
					provinceId = "";
				}
				String dealerClass = request.getParamValue("dealerClass");
				if (dealerClass == null) {
					dealerClass = "";
				}
				String isAllLevel = request.getParamValue("isAllLevel");
				if (isAllLevel == null || isAllLevel.equals("") || isAllLevel.equals("null")) {
					isAllLevel = "false";
				}
				String isAllArea = request.getParamValue("isAllArea");
				if (isAllArea == null || isAllArea.equals("") || isAllArea.equals("null")) {
					isAllArea = "false";
				}
				String areaId = request.getParamValue("AREA_ID");

				// end
				Integer curPage = request.getParamValue("curPage2") != null ? Integer.parseInt(request.getParamValue("curPage2")) : 1; // 处理当前页
				// modify by zhaojinyu 2010-06-12 经销商树中售后只查询售后的经销商，销售只查询销售的经销商。
				Integer poseBusType = logonUser.getPoseBusType();
				Long poseId = logonUser.getPoseId();
				System.out.println(poseId);
				PageResult<TmDealerPO> ps = UserMngDAO.getAllDRLByDeptIdCon(userId, areaId, isDealerType, orgId, dcode, dsname, GetOemcompanyId.getOemCompanyId(logonUser), provinceId, dealerClass, page_size, curPage, orderName, da, inputOrgId, poseBusType, poseId, isAllLevel, isAllArea);
				act.setOutData("ps", ps);
			}
		} catch (BizException e) {
			logger.error(logonUser, e);
			act.setException(e);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "SGM维护经销商用户");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	public void allDrlQueryZWCon() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			act.getResponse().setContentType("application/json");

			if ("1".equals(request.getParamValue("COMMAND"))) {

				String userId = request.getParamValue("userId");

				String orgId = request.getParamValue("DEPT_ID");
				String dcode = request.getParamValue("DRLCODE");
				String dsname = request.getParamValue("DELSNAME");
				String isDealerType = request.getParamValue("isDealerType");
				String orderName = request.getParamValue("orderCol2");
				String da = request.getParamValue("order2");
				String inputOrgId = request.getParamValue("ORGID");// 页面输入框的组织ID，由操作人自己控制。
				// added by andy.ten@tom.com
				String isMulti = request.getParamValue("isMulti");
				int page_size = Constant.PAGE_SIZE_MAX;// Constant.PAGE_SIZE,Constant.PAGE_SIZE_MAX;
				// //;
				if (isMulti != null && !"".equals(isMulti)) {
					page_size = 20;
				}

				String provinceId = request.getParamValue("downtown");
				if (provinceId == null) {
					provinceId = "";
				}
				String dealerClass = request.getParamValue("dealerClass");
				if (dealerClass == null) {
					dealerClass = "";
				}
				String isAllLevel = request.getParamValue("isAllLevel");
				if (isAllLevel == null || isAllLevel.equals("") || isAllLevel.equals("null")) {
					isAllLevel = "false";
				}
				String isAllArea = request.getParamValue("isAllArea");
				if (isAllArea == null || isAllArea.equals("") || isAllArea.equals("null")) {
					isAllArea = "false";
				}
				String areaId = request.getParamValue("AREA_ID");

				// end
				Integer curPage = request.getParamValue("curPage2") != null ? Integer.parseInt(request.getParamValue("curPage2")) : 1; // 处理当前页
				// modify by zhaojinyu 2010-06-12 经销商树中售后只查询售后的经销商，销售只查询销售的经销商。
				Integer poseBusType = logonUser.getPoseBusType();
				String dealerId = logonUser.getDealerId();
				System.out.println(dealerId);
				PageResult<TmDealerPO> ps = UserMngDAO.getAllDRLByDeptIdZWCon(userId, areaId, isDealerType, orgId, dcode, dsname, GetOemcompanyId.getOemCompanyId(logonUser), provinceId, dealerClass, page_size, curPage, orderName, da, inputOrgId, poseBusType, dealerId, isAllLevel, isAllArea);
				act.setOutData("ps", ps);
			}
		} catch (BizException e) {
			logger.error(logonUser, e);
			act.setException(e);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "SGM维护经销商用户");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	public void allDrlQueryNew() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			act.getResponse().setContentType("application/json");

			if ("1".equals(request.getParamValue("COMMAND"))) {
				String orgId = request.getParamValue("DEPT_ID");
				String dcode = request.getParamValue("DRLCODE");
				String dsname = request.getParamValue("DELSNAME");
				String isDealerType = request.getParamValue("isDealerType");
				String orderName = request.getParamValue("orderCol2");
				String da = request.getParamValue("order2");
				String inputOrgId = request.getParamValue("ORGID");// 页面输入框的组织ID，由操作人自己控制。
				// added by andy.ten@tom.com
				String isMulti = request.getParamValue("isMulti");
				int page_size = Constant.PAGE_SIZE_MAX;// Constant.PAGE_SIZE,Constant.PAGE_SIZE_MAX;
				// //;
				if (isMulti != null && !"".equals(isMulti)) {
					page_size = 20;
				}

				String provinceId = request.getParamValue("downtown");
				if (provinceId == null) {
					provinceId = "";
				}
				String dealerClass = request.getParamValue("dealerClass");
				if (dealerClass == null) {
					dealerClass = "";
				}
				String isAllLevel = request.getParamValue("isAllLevel");
				if (isAllLevel == null || isAllLevel.equals("") || isAllLevel.equals("null")) {
					isAllLevel = "false";
				}
				String isAllArea = request.getParamValue("isAllArea");
				if (isAllArea == null || isAllArea.equals("") || isAllArea.equals("null")) {
					isAllArea = "false";
				}
				String areaId = request.getParamValue("AREA_ID");

				// end
				Integer curPage = request.getParamValue("curPage2") != null ? Integer.parseInt(request.getParamValue("curPage2")) : 1; // 处理当前页
				// modify by zhaojinyu 2010-06-12 经销商树中售后只查询售后的经销商，销售只查询销售的经销商。
				Integer poseBusType = logonUser.getPoseBusType();
				Long poseId = logonUser.getPoseId();
				PageResult<TmDealerPO> ps = UserMngDAO.getAllDRLByDeptIdNew(areaId, isDealerType, orgId, dcode, dsname, GetOemcompanyId.getOemCompanyId(logonUser), provinceId, dealerClass, page_size, curPage, orderName, da, inputOrgId, poseBusType, poseId, isAllLevel, isAllArea);
				act.setOutData("ps", ps);
			}
		} catch (BizException e) {
			logger.error(logonUser, e);
			act.setException(e);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "SGM维护经销商用户");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * SGM维护经销商用户选择省份经销商查询
	 * 
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public void allRegDrlQuery() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			act.getResponse().setContentType("application/json");

			if ("1".equals(request.getParamValue("COMMAND"))) {
				String orgId = request.getParamValue("DEPT_ID");
				String dcode = request.getParamValue("DRLCODE");
				String regCode = request.getParamValue("regCode");
				String dsname = request.getParamValue("DELSNAME");
				String isDealerType = request.getParamValue("isDealerType");
				String orderName = request.getParamValue("orderCol2");
				String da = request.getParamValue("order2");
				String inputOrgId = request.getParamValue("ORGID");// 页面输入框的组织ID，由操作人自己控制。
				String isMulti = request.getParamValue("isMulti");
				int page_size = Constant.PAGE_SIZE_MAX;// Constant.PAGE_SIZE,Constant.PAGE_SIZE_MAX;
				// //;
				if (isMulti != null && !"".equals(isMulti)) {
					page_size = 20;
				}

				String provinceId = request.getParamValue("downtown");
				if (provinceId == null) {
					provinceId = "";
				}
				String dealerClass = request.getParamValue("dealerClass");
				if (dealerClass == null) {
					dealerClass = "";
				}
				String isAllLevel = request.getParamValue("isAllLevel");
				if (isAllLevel == null || isAllLevel.equals("") || isAllLevel.equals("null")) {
					isAllLevel = "false";
				}
				String isAllArea = request.getParamValue("isAllArea");
				if (isAllArea == null || isAllArea.equals("") || isAllArea.equals("null")) {
					isAllArea = "false";
				}
				String areaId = request.getParamValue("AREA_ID");

				// end
				Integer curPage = request.getParamValue("curPage2") != null ? Integer.parseInt(request.getParamValue("curPage2")) : 1; // 处理当前页
				Integer poseBusType = logonUser.getPoseBusType();
				Long poseId = logonUser.getPoseId();
				PageResult<TmDealerPO> ps = dao.getAllDRLByDeptIdReg(regCode, areaId, isDealerType, orgId, dcode, dsname, GetOemcompanyId.getOemCompanyId(logonUser), provinceId, dealerClass, page_size, curPage, orderName, da, inputOrgId, poseBusType, poseId, isAllLevel, isAllArea);
				act.setOutData("ps", ps);
			}
		} catch (BizException e) {
			logger.error(logonUser, e);
			act.setException(e);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "SGM维护经销商用户");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * SGM维护选择省份
	 * 
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public void allRegionQuery() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			act.getResponse().setContentType("application/json");
			String regionCode = request.getParamValue("regCode");// 页面输入框的组织ID，由操作人自己控制。
			String regionName = request.getParamValue("regName");
			String org_id = request.getParamValue("orgId");
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
			/*
			 * StringBuffer con = new StringBuffer(); // 大区代码 if (regionCode !=
			 * null && !"".equals(regionCode)) { con.append(" and r.region_code
			 * ='" + regionCode + "' "); // } //经销商 if (regionName != null &&
			 * !"".equals(regionName)) { con.append(" and r.region_name like '%" +
			 * regionName + "%' "); // } //大区 if (org_id != null &&
			 * !"".equals(org_id) && !"null".equals(org_id)) { con.append(" and
			 * oda.ROOT_ORG_ID ='" + org_id + "' "); // }
			 */
			Map<String, String> map = new HashMap<String, String>();
			map.put("regionCode", regionCode);
			map.put("regionName", regionName);
			map.put("org_id", org_id);

			PageResult<Map<String, Object>> ps = dao.getAllRegion(map, curPage, Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "SGM维护经销商用户");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * SGM维护选择组织
	 * 
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public void allOrgQuery() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String areaId = request.getParamValue("areaId");
			act.getResponse().setContentType("application/json");
			// if(MaterialGroupManagerDao.getPoseIdBusiness(String.valueOf(logonUser.getPoseId())).size()>0){
			// areaId=MaterialGroupManagerDao.getPoseIdBusiness(String.valueOf(logonUser.getPoseId())).get(0).get("AREA_ID").toString();
			// }
			if ("1".equals(request.getParamValue("COMMAND"))) {
				String orgId = request.getParamValue("orgId");
				String orgCode = request.getParamValue("orgCode");
				String orgName = request.getParamValue("orgName");
				String inputOrgId = request.getParamValue("ORGID");// 页面输入框的组织ID，由操作人自己控制。
				Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
				PageResult<TmOrgPO> ps = UserMngDAO.getAllOrg(areaId, orgId, orgCode, orgName, logonUser.getCompanyId(), Constant.PAGE_SIZE, curPage, inputOrgId,false);
				act.setOutData("ps", ps);
			}
		} catch (BizException e) {
			logger.error(logonUser, e);
			act.setException(e);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "SGM维护经销商用户");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * SGM维护选择组织
	 * 
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public void allOrgQuery3() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			act.getResponse().setContentType("application/json");
			// if(MaterialGroupManagerDao.getPoseIdBusiness(String.valueOf(logonUser.getPoseId())).size()>0){
			// areaId=MaterialGroupManagerDao.getPoseIdBusiness(String.valueOf(logonUser.getPoseId())).get(0).get("AREA_ID").toString();
			// }
			if ("1".equals(request.getParamValue("COMMAND"))) {
				String orgId = request.getParamValue("orgId");
				String orgCode = request.getParamValue("orgCode");
				String orgName = request.getParamValue("orgName");
				String inputOrgId = request.getParamValue("ORGID");// 页面输入框的组织ID，由操作人自己控制。
				Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
				PageResult<TmOrgPO> ps = UserMngDAO.getAllOrg3(orgId, orgCode, orgName, logonUser.getCompanyId(), Constant.PAGE_SIZE, curPage, inputOrgId);
				act.setOutData("ps", ps);
			}
		} catch (BizException e) {
			logger.error(logonUser, e);
			act.setException(e);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "SGM维护经销商用户");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	// zhumingwei add by 2011-02-25
	public void allOrgQuery111() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String areaId = request.getParamValue("areaId");
			act.getResponse().setContentType("application/json");
			if ("1".equals(request.getParamValue("COMMAND"))) {
				String orgId = request.getParamValue("orgId");
				String orgCode = request.getParamValue("orgCode");
				String orgName = request.getParamValue("orgName");
				String inputOrgId = request.getParamValue("ORGID");// 页面输入框的组织ID，由操作人自己控制。
				Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
				PageResult<TmOrgPO> ps = UserMngDAO.getAllOrg111(areaId, orgId, orgCode, orgName, logonUser.getCompanyId(), Constant.PAGE_SIZE, curPage, inputOrgId);
				act.setOutData("ps", ps);
			}
		} catch (BizException e) {
			logger.error(logonUser, e);
			act.setException(e);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "SGM维护经销商用户");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * SGM维护选择组织
	 * 
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public void allAreaDealerQuery() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String areaId = "";
			act.getResponse().setContentType("application/json");
			if (MaterialGroupManagerDao.getPoseIdBusiness(String.valueOf(logonUser.getPoseId())).size() > 0) {
				areaId = MaterialGroupManagerDao.getPoseIdBusiness(String.valueOf(logonUser.getPoseId())).get(0).get("AREA_ID").toString();
			}
			if ("1".equals(request.getParamValue("COMMAND"))) {
				String inputOrgId = request.getParamValue("ORGID");// 页面输入框的组织ID，由操作人自己控制。
				Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
				PageResult<TmDealerPO> ps = UserMngDAO.getAllAreaDealer(areaId, inputOrgId, curPage, Constant.PAGE_SIZE);
				act.setOutData("ps", ps);
			}
		} catch (BizException e) {
			logger.error(logonUser, e);
			act.setException(e);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "SGM维护经销商用户");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 经销商添加职位查询
	 * 
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public void addPoseQuery() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			act.getResponse().setContentType("application/json");
			String poseCode = request.getParamValue("POSE_CODE");
			String poseName = request.getParamValue("POSE_NAME");
			String companyId = logonUser.getDealerId();//request.getParamValue("companyId");
			String poseIds = request.getParamValue("poseIds"); // 用户当前的职位ID

			List<MsgCarrier> vlist = new ArrayList<MsgCarrier>();
			vlist.add(MsgCarrier.getInstance(ValidateCodeConstant.NOQUOTATION_PATTERN, "职位代码", 0, Constant.Length_Check_Char_20, poseCode));
			vlist.add(MsgCarrier.getInstance(ValidateCodeConstant.NOQUOTATION_PATTERN, "职位名称", 0, Constant.Length_Check_Char_20, poseName));
			Validate.doValidate(act, vlist);

			List<TcPosePO> ps = UserMngDAO.getDealerPoseByCompanyId(new Long(companyId), poseCode, poseName, poseIds);
			act.setOutData("ps", ps);
		} catch (BizException e) {
			logger.error(logonUser, e);
			act.setException(e);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "SGM维护经销商用户");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 经销商添加职位查询
	 * 
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public void addPoseQueryByIPoseIds() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			act.getResponse().setContentType("application/json");
			String poseIds = request.getParamValue("POSE_IDS");
			String companyId = request.getParamValue("COMPANY_ID");
			List<TcPosePO> ps = UserMngDAO.getDRLPoseByPoseIds(new Long(companyId), poseIds);
			act.setOutData("ps", ps);
		} catch (BizException e) {
			logger.error(logonUser, e);
			act.setException(e);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "SGM维护经销商用户");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * SGM维护经销商用户查询
	 * 
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public void sgmDealerSysUserQuery() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			act.getResponse().setContentType("application/json");

			if ("1".equals(request.getParamValue("COMMAND"))) {
				// 部门
				String deptId = request.getParamValue("DEPT_ID");
				// 经销商账号
				String acnt = request.getParamValue("ACNT");
				// 姓名
				String name = request.getParamValue("NAME");
				// 经销商ID
				String companyId = request.getParamValue("COMPANY_ID");
				// 职位代码
				String poseCode = request.getParamValue("POSE_CODE");
				// 职位代码
				String poseRank = request.getParamValue("POSE_RANK");
				String groupId=request.getParamValue("groupId");
				String userStatus=request.getParamValue("userStatus");
				String isLock=request.getParamValue("isLock");
				Long oemCompanyId = logonUser.getCompanyId();
				List<MsgCarrier> vlist = new ArrayList<MsgCarrier>();
				vlist.add(MsgCarrier.getInstance(ValidateCodeConstant.NOQUOTATION_PATTERN, "部门ID", 0, Constant.Length_Check_Char_100, deptId));
				vlist.add(MsgCarrier.getInstance(ValidateCodeConstant.NOQUOTATION_PATTERN, "经销商账号", 0, Constant.Length_Check_Char_100, acnt));
				vlist.add(MsgCarrier.getInstance(ValidateCodeConstant.NOQUOTATION_PATTERN, "姓名", 0, Constant.Length_Check_Char_100, name));
				vlist.add(MsgCarrier.getInstance(ValidateCodeConstant.NOQUOTATION_PATTERN, "经销商ID", 0, Constant.Length_Check_Char_100, companyId));
				vlist.add(MsgCarrier.getInstance(ValidateCodeConstant.NOQUOTATION_PATTERN, "职位代码", 0, Constant.Length_Check_Char_100, poseCode));
				Validate.doValidate(act, vlist);

				String orderName = request.getParamValue("orderCol");
				String da = request.getParamValue("order");

				Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
				// PageResult<TcUserPO> ps =
				// UserMngDAO.sgmDrlSysUserQuery(companyId,oemCompanyId,deptId,
				// acnt, name, poseCode,
				// Constant.PAGE_SIZE,curPage,orderName,da); // 按条件查询系统用户
				PageResult<Map<String, Object>> ps = UserMngDAO.getDlrSysUserQueryA(companyId, oemCompanyId, deptId, acnt, name, poseCode, Constant.PAGE_SIZE, curPage, orderName, da,poseRank,groupId,userStatus,isLock); // 按条件查询系统用户
				act.setOutData("ps", ps);
			}
		} catch (BizException e) {
			logger.error(logonUser, e);
			act.setException(e);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "SGM维护经销商用户");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 经销商添加供应商查询
	 * 
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public void allSuppQuery() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String suppCode = CommonUtils.checkNull(request.getParamValue("suppCode")); // 供应商代码
			String suppName = CommonUtils.checkNull(request.getParamValue("suppName")); // 供应商名称
			// 分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
			PageResult<TmPtSupplierPO> ps = UserMngDAO.allSuppQuery(suppCode, suppName, curPage, Constant.PAGE_SIZE);
			// 分页方法 end
			act.setOutData("ps", ps); // 向前台传的list 名称是固定的不可改必须用 ps
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "配件大类");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 经销商添加配件大类查询
	 * 
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public void allPartTypeQuery() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String suppCode = CommonUtils.checkNull(request.getParamValue("suppCode")); // 配件大类代码
			String suppName = CommonUtils.checkNull(request.getParamValue("suppName")); // 配件大类名称
			// 分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
			PageResult<TmPtPartTypePO> ps = UserMngDAO.allPartTypeQuery(suppCode, suppName, curPage, Constant.PAGE_SIZE);
			// 分页方法 end
			act.setOutData("ps", ps); // 向前台传的list 名称是固定的不可改必须用 ps
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "配件大类");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * FUNCTION : 实销信息上报:查询销售顾问列表
	 * 
	 * @param :
	 * @return :
	 * @throws :
	 *             LastUpdate : 2010-6-18
	 */
	public void toUserList() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = null;
		RequestWrapper request = act.getRequest();
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			String userId=request.getParamValue("userId");
			String poseRank=request.getParamValue("poseRank");
			String  parRank=null;
			if(poseRank!=null&&!"".equals(poseRank)){
				parRank=(Integer.parseInt(poseRank)-1)+"";
			}
			if(Constant.DEALER_USER_LEVEL_05.toString().equals(poseRank)){
				parRank=Constant.DEALER_USER_LEVEL_01.toString();
			}
			act.setOutData("userId", userId);
			act.setOutData("poseRank", parRank);
			act.setForword(toPoseListURL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "实销信息上报:查询销售顾问列表");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * FUNCTION : 实销信息上报:查询大客户列表结果展示
	 * 
	 * @param :
	 * @return :
	 * @throws :
	 *             LastUpdate : 2010-6-18
	 */
	public void getUserList() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = null;
		try {
			RequestWrapper request = act.getRequest();
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			act.getSession().get(Constant.LOGON_USER);
			Long companyId = new Long(logonUser.getCompanyId());
			String userCode = CommonUtils.checkNull(request.getParamValue("userCode"));
			String userName = CommonUtils.checkNull(request.getParamValue("userName"));
			String poseRank = CommonUtils.checkNull(request.getParamValue("poseRank"));
			
			//职位减去1得到上级职位
			String userId=request.getParamValue("userId");
			Map<String,String> map=new HashMap<String,String>();
			map.put("companyId", companyId.toString());
			map.put("userId", userId);
			map.put("userCode", userCode);
			map.put("userName", userName);
			map.put("poseRank", poseRank);
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
			PageResult<Map<String, Object>> ps = dao.getUserList(map,Constant.PAGE_SIZE, curPage);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "实销信息上报:查询销售顾问列表结果展示");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 维护经销商用户查询初始化
	 * 
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public void getParId() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try {
			String company=logonUser.getCompanyId().toString();
			String poseRank = CommonUtils.checkNull(request.getParamValue("poseRank"));
			String groupId = CommonUtils.checkNull(request.getParamValue("groupId"));
			//跟进poserank得到上级
			if(poseRank!=null&&!"".equals(poseRank)&&!Constant.DEALER_USER_LEVEL_05.toString().equals(poseRank)){
				poseRank=(Integer.parseInt(poseRank)-1)+"";
			}
			//如果poserank表示dcrc直接得到总经理
			if(poseRank!=null&&!"".equals(poseRank)&&Constant.DEALER_USER_LEVEL_05.toString().equals(poseRank)){
				poseRank=Constant.DEALER_USER_LEVEL_01.toString();
			}
			TcUserPO tu=new TcUserPO();
			tu.setCompanyId(new Long(company));
			//如果是有groupId就跟据groupId和poseRank确认上级人员
			if(groupId!=null&&!"".equals(groupId)){
				tu.setGroupId(new Long(groupId));
			}
			if(poseRank!=null&&!"".equals(poseRank)){
				tu.setPoseRank(new Long(poseRank));
				//职位级别必须有值才可以获取值
				if(dao.select(tu).size()>0){
					tu=(TcUserPO) dao.select(tu).get(0);
				}
			}
			
			
			act.setOutData("flag", 1);
			act.setOutData("tu", tu);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "SGM维护经销商用户");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

}
