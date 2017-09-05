
/**********************************************************************
* <pre>
* FILE : SgmDealerSysUser.java
* CLASS : SgmDealerSysUser
* 
* AUTHOR : ChenLiang
*
* FUNCTION : SGM维护经销商用户Action.
*
*
*======================================================================
* CHANGE HISTORY LOG
*----------------------------------------------------------------------
* MOD. NO.| DATE     |    NAME    | REASON | CHANGE REQ.
*----------------------------------------------------------------------
*         |2009-09-11| ChenLiang  | Created |
* DESCRIPTION:
* </pre>
***********************************************************************/

package com.infodms.dms.actions.sysmng.usemng;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.StandardVipApplyManagerBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.MD5Util;
import com.infodms.dms.common.MsgCarrier;
import com.infodms.dms.common.ValidateCodeConstant;
import com.infodms.dms.common.getCompanyId.GetOemcompanyId;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.common.CommonDAO;
import com.infodms.dms.dao.feedbackMng.StandardVipApplyManagerDao;
import com.infodms.dms.dao.sales.storageManage.CheckVehicleDAO;
import com.infodms.dms.dao.usermng.UserMngDAO;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TcPosePO;
import com.infodms.dms.po.TcUserPO;
import com.infodms.dms.po.TmCompanyPO;
import com.infodms.dms.po.TmDealerPO;
import com.infodms.dms.po.TmOrgPO;
import com.infodms.dms.po.TmPtPartTypePO;
import com.infodms.dms.po.TmPtSupplierPO;
import com.infodms.dms.po.TrUserPosePO;
import com.infodms.dms.po.TtVsDlvryReqPO;
import com.infodms.dms.po.VwMaterialInfoPO;
import com.infodms.dms.util.ActionUtil;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.businessUtil.Validate;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.POFactory;
import com.infoservice.po3.POFactoryBuilder;
import com.infoservice.po3.bean.PageResult;

public class SgmDealerSysUser {

	public Logger logger = Logger.getLogger(SgmDealerSysUser.class);
	private POFactory factory = POFactoryBuilder.getInstance();
	private final String querySgmDealerSysUserInitUrl = "/jsp/systemMng/userMng/sgmDealerSysUserSearch.jsp";
	private final String addSgmDealerSysUserInitUrl = "/jsp/systemMng/userMng/sgmDealerSysUserAdd.jsp";
	private final String modfiSgmDealerSysUserInitUrl = "/jsp/systemMng/userMng/sgmDealerSysUserDetail.jsp";
	private final String queryPoseInitUrl = "/jsp/systemMng/userMng/sgmDealerPoseSearch.jsp";
    private UserMngDAO dao = UserMngDAO.getInstance();
    
    /**
     * 修改数据库中，经销商拼音
     */
    public void cscs(){
    	UserMngDAO.selectAllDealer();
    }
    
	/**
	 * SGM维护经销商用户查询初始化
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public void querySgmDealerSysUserInit() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword(querySgmDealerSysUserInitUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"SGM维护经销商用户");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * SGM维护经销商用户添加初始化
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public void addSgmDealerSysUserInit() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword(addSgmDealerSysUserInitUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"SGM维护经销商用户");
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
			String comapnyID = request.getParamValue("companyId"); // 公司ID
//			String dealerId = request.getParamValue("dealerId");
			String poseIds = CommonUtils.checkNull(request.getParamValue("POSE_IDS"));
			//modify by xiayapeng begin先生成ID格式 不满足一下校验逻辑
			List<MsgCarrier> vlist = new ArrayList<MsgCarrier>();
			vlist.add(MsgCarrier.getInstance(ValidateCodeConstant.DIGIT_PATTERN,"公司ID",1,Constant.Length_Check_Char_10,comapnyID));
			
			//Validate.doValidate(act, vlist);
			//modify bu xiayanpeng end
			
			act.setOutData("poseIds", poseIds);
			act.setOutData("comapnyID", comapnyID);
			act.setForword(queryPoseInitUrl);
		} catch(BizException e){
			logger.error(logonUser,e);
			act.setException(e);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"SGM维护经销商用户");
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
			String orgId = CommonUtils.checkNull(request.getParamValue("ORGID"));
			String regCode = request.getParamValue("regCode");
			List<MsgCarrier> vlist = new ArrayList<MsgCarrier>();
			Validate.doValidate(act, vlist);
			
			List<TmOrgPO> funList = CommonDAO.createDeptTree(GetOemcompanyId.getOemCompanyId(logonUser), orgId,logonUser.getUserType(),true);
			act.setOutData("funlist", funList);
			
		} catch(BizException e){
			logger.error(logonUser,e);
			act.setException(e);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"SGM维护经销商用户");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	
	/** zxf
	 * 构造SGM的部门树形菜单
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public void initOrgTreeArea() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			act.getResponse().setContentType("application/json");
			String rootId = request.getParamValue("tree_root_id"); // 得到树的根节点ID
			String orgId= request.getParamValue("ORGID");
			String regCode = request.getParamValue("regCode");
			List<MsgCarrier> vlist = new ArrayList<MsgCarrier>();
			//vlist.add(MsgCarrier.getInstance(ValidateCodeConstant.DIGIT_PATTERN,"部门ID",0,Constant.Length_Check_Char_10,rootId));
			Validate.doValidate(act, vlist);
			
			List<TmOrgPO> funList = CommonDAO.createDeptTree(GetOemcompanyId.getOemCompanyId(logonUser), orgId,logonUser.getUserType(),true);
			act.setOutData("funlist", funList);
		} catch(BizException e){
			logger.error(logonUser,e);
			act.setException(e);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"SGM维护经销商用户");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	
	public void initOrgTreeDealer() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			act.getResponse().setContentType("application/json");
			String rootId = request.getParamValue("tree_root_id"); // 得到树的根节点ID
			String orgId= request.getParamValue("ORGID");
			String regCode = request.getParamValue("regCode");
			List<MsgCarrier> vlist = new ArrayList<MsgCarrier>();
			//vlist.add(MsgCarrier.getInstance(ValidateCodeConstant.DIGIT_PATTERN,"部门ID",0,Constant.Length_Check_Char_10,rootId));
			Validate.doValidate(act, vlist);
			
			List<TmOrgPO> funList = CommonDAO.createDeptTree(GetOemcompanyId.getOemCompanyId(logonUser), orgId,logonUser.getUserType(),true);
			act.setOutData("funlist", funList);
		} catch(BizException e){
			logger.error(logonUser,e);
			act.setException(e);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"SGM维护经销商用户");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * SGM维护经销商用户修改初始化
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public void modfiSgmDealerSysUserInit() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String userId = request.getParamValue("userId");
			
			List<MsgCarrier> vlist = new ArrayList<MsgCarrier>();
			vlist.add(MsgCarrier.getInstance(ValidateCodeConstant.DIGIT_PATTERN,"用户ID",1,Constant.Length_Check_Char_20,userId));
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
			/***************modify xieyj***************/
//			TmCompanyPO companyPO = new TmCompanyPO();
//			companyPO.setCompanyId(tcUserPO.getCompanyId());
//			companyPO = factory.select(companyPO).get(0);
			TmDealerPO dealerPo = new TmDealerPO();
			dealerPo.setDealerId(tcUserPO.getDealerId());
			dealerPo = factory.select(dealerPo).get(0);
			act.setOutData("dealerPo", dealerPo);
			/******************************************/
			act.setOutData("user", tcUserPO);
			act.setOutData("poseIds", poseIds);
			//act.setOutData("companyName", companyPO.getCompanyShortname());//modify xieyj
			act.setForword(modfiSgmDealerSysUserInitUrl);
		} catch(BizException e){
			logger.error(logonUser,e);
			act.setException(e);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"SGM维护经销商用户");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * SGM维护经销商用户修改
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public void modfiSgmDealerSysUser() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			act.getResponse().setContentType("application/json");
			String userId = CommonUtils.checkNull(request.getParamValue("USER_ID"));
			String poseids = CommonUtils.checkNull(request.getParamValue("POSE_IDS"));       // 用户职位ID 
			String name = CommonUtils.checkNull(request.getParamValue("NAME"));              // 姓名
			String gender = CommonUtils.checkNull(request.getParamValue("GENDER"));          // 性别
			String handPhone = CommonUtils.checkNull(request.getParamValue("HAND_PHONE"));   // 手机
			String phone = CommonUtils.checkNull(request.getParamValue("PHONE"));            // 电话
			String email = CommonUtils.checkNull(request.getParamValue("EMAIL"));            // EMail
			String userStatus = CommonUtils.checkNull(request.getParamValue("USER_STATUS"));     // 状态
			
			
			//modify by xiayapeng begin 维护用户时加入 密码维护项
			String password = request.getParamValue("PASSWORD");      //密码
			//modify by xiayanpeng end 
			
			List<MsgCarrier> vlist = new ArrayList<MsgCarrier>();
			vlist.add(MsgCarrier.getInstance(ValidateCodeConstant.DIGIT_PATTERN,"用户ID",1,Constant.Length_Check_Char_10,userId));
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
			
			//modify by xiayapeng begin 维护用户时加入 密码维护项
			tcUserPO2.setPassword(MD5Util.MD5Encryption(password));
			tcUserPO2.setUpdateDate(new Date()); // YH 2010.12.20
			tcUserPO2.setUpdateBy(logonUser.getUserId()); // YH 2010.12.20
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.MONTH, 6);
			tcUserPO2.setOverDate(cal.getTime());
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
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"SGM维护经销商用户");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * SGM维护经销商用户添加
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public void addSgmDealerSysUser() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			act.getResponse().setContentType("application/json");
			String poseids = request.getParamValue("POSE_IDS");       // 用户职位ID 
			String acnt = request.getParamValue("ACNT").toUpperCase();// 用户帐号(认证中心帐号)
			String name = request.getParamValue("NAME");              // 姓名
			String gender = request.getParamValue("GENDER");          // 性别
			String handPhone = request.getParamValue("HAND_PHONE");   // 手机
			String phone = request.getParamValue("PHONE");            // 电话
			String email = request.getParamValue("EMAIL");            // EMail
			String isDcs = request.getParamValue("IS_DCS");     // 是否店面系统用户
			String userStatus = request.getParamValue("USER_STATUS");     // 状态
//			String companyId = request.getParamValue("COMPANY_ID");     // 经销商ID modify xieyj
			String companyId = request.getParamValue("dealerId");  //dealerId
			//modify by xiayapeng begin 维护用户时加入 密码维护项
			String password = request.getParamValue("PASSWORD");      //密码
			//modify by xiayanpeng end 
			String epcPower = CommonUtils.checkNull(request.getParamValue("epc_power")); 	//epc订单权限 wizard_lee 2014-06-06
			
			
			List<MsgCarrier> vlist = new ArrayList<MsgCarrier>();
			vlist.add(MsgCarrier.getInstance(ValidateCodeConstant.DIGIT_LETTER_PATTERN,"用户帐号",1,Constant.Length_Check_Char_20,acnt));
			vlist.add(MsgCarrier.getInstance(ValidateCodeConstant.LETTER_CN_PATTERN,"姓名",1,Constant.Length_Check_Char_10,name));
			vlist.add(MsgCarrier.getInstance(ValidateCodeConstant.DIGIT_PATTERN,"手机",0,Constant.Length_Check_Char_20,handPhone));
			vlist.add(MsgCarrier.getInstance(ValidateCodeConstant.EMAIL_PATTERN,"EMail",0,Constant.Length_Check_Char_50,email));
//			Validate.doValidate(act, vlist);
			
			TcUserPO tcUserPO1 = new TcUserPO();
			tcUserPO1.setAcnt(acnt);
			List<TcUserPO> userlist = factory.select(tcUserPO1);
			if(userlist!=null && userlist.size()>0) {
				act.setOutData("st", "acnt_error");
				return ;
			}
			TcUserPO tcUserPO = new TcUserPO();
			tcUserPO.setUserId(factory.getLongPK(new TcUserPO()));
			/********************modify xieyj**********************/
			TmDealerPO dealerPo = new TmDealerPO();
			dealerPo.setDealerId(Long.valueOf(companyId));
			dealerPo = factory.select(dealerPo).get(0);
			tcUserPO.setCompanyId(dealerPo.getCompanyId());
			/************************************************/
//			tcUserPO.setCompanyId(new Long(companyId));
			tcUserPO.setDealerId(Long.valueOf(companyId));
			tcUserPO.setAcnt(acnt);
			tcUserPO.setUserType(Constant.SYS_USER_DEALER);
			tcUserPO.setName(name);
			tcUserPO.setGender(Integer.valueOf(gender));
			tcUserPO.setHandPhone(handPhone);
			tcUserPO.setPhone(phone);
			tcUserPO.setEmail(email);
			tcUserPO.setIsDcs(Integer.valueOf(isDcs));
			tcUserPO.setUserStatus(Integer.valueOf(userStatus));
			if(epcPower!=null){
				if(!"".equals(epcPower))
					tcUserPO.setQueryOnly(Long.parseLong(epcPower.toString()));//更新epc权限
			}
			tcUserPO.setCreateDate(new Date()); // YH 2010.12.20
			tcUserPO.setUpdateDate(new Date()); // YH 2010.12.20
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.MONTH, 6);
			tcUserPO.setOverDate(cal.getTime());
			tcUserPO.setCreateBy(logonUser.getUserId()); // YH 2010.12.20
			tcUserPO.setUpdateBy(logonUser.getUserId()); // YH 2010.12.20
			//modify by xiayapeng begin 维护用户时加入 密码维护项
			tcUserPO.setPassword(MD5Util.MD5Encryption(password));
			//modify by xiayanpeng end 
			factory.insert(tcUserPO); // 将用户存入系统
			
			if(poseids != null && !"".equals(poseids)) {
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
			}
			
			act.setOutData("st", "succeed");
		} catch(BizException e){
			logger.error(logonUser,e);
			act.setException(e);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"SGM维护经销商用户");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * SGM经销商用户修改
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
			String gender = CommonUtils.checkNull(request.getParamValue("GENDER"));          // 性别
			String handPhone = CommonUtils.checkNull(request.getParamValue("HAND_PHONE"));   // 手机
			String phone = CommonUtils.checkNull(request.getParamValue("PHONE"));            // 电话
			String email = CommonUtils.checkNull(request.getParamValue("EMAIL"));            // EMail
			String isDcs = request.getParamValue("IS_DCS");     // 是否店面系统用户
			String userStatus = CommonUtils.checkNull(request.getParamValue("USER_STATUS"));     // 状态
			String password = CommonUtils.checkNull(request.getParamValue("PASSWORD"));     // 密码
			String isLock = CommonUtils.checkNull(request.getParamValue("isLock"));     // 新增是否锁定 2012-07-10 韩晓宇
			String epcPower = CommonUtils.checkNull(request.getParamValue("epc_power")); 	//epc订单权限 wizard_lee 2014-06-06
			
			List<MsgCarrier> vlist = new ArrayList<MsgCarrier>();
			vlist.add(MsgCarrier.getInstance(ValidateCodeConstant.DIGIT_PATTERN,"用户ID",1,Constant.Length_Check_Char_30,userId));
			//vlist.add(MsgCarrier.getInstance(ValidateCodeConstant.LETTER_CN_PATTERN,"姓名",1,Constant.Length_Check_Char_10,name));
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
//			tcUserPO.setIsDcs(Integer.valueOf(isDcs));
			tcUserPO2.setUserStatus(Integer.valueOf(userStatus));
			tcUserPO2.setIsLock(Integer.valueOf(isLock)); //新增是否锁定更新 2012-07-10 韩晓宇
			if(epcPower!=null){
				if(!"".equals(epcPower))
					tcUserPO2.setQueryOnly(Long.parseLong(epcPower.toString()));//更新epc权限
			}
			
			if(password!=null&&password!=""){
				tcUserPO2.setPassword(MD5Util.MD5Encryption(password));//密码
				Calendar cal = Calendar.getInstance();
				cal.add(Calendar.MONTH, 6);
				tcUserPO2.setOverDate(cal.getTime());
				//TODO 更新密码时自动解锁 2012-07-13 韩晓宇
				tcUserPO2.setIsLock(0);
				//TODO END
			}
			tcUserPO2.setUpdateDate(new Date()); // YH 2010.12.20
			tcUserPO2.setUpdateBy(logonUser.getUserId()); // YH 2010.12.20
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
					trUserPosePO2.setCreateDate(new Date()); //YH 2010.12.20
					trUserPosePO2.setUpdateDate(new Date()); //YH 2010.12.20
					trUserPosePO2.setCreateBy(logonUser.getUserId()); //YH 2010.12.20
					trUserPosePO2.setUpdateBy(logonUser.getUserId()); //YH 2010.12.20
					factory.insert(trUserPosePO2);
				}
			}
			
			//zhumingwei add 2011-6-21 begin 修改用户表里面公司ID
			//String COMPANY_ID = CommonUtils.checkNull(request.getParamValue("COMPANY_ID"));//modify xieyj
			String dealerId = CommonUtils.checkNull(request.getParamValue("dealerId"));
			TcUserPO tcUserPO1 = new TcUserPO();
			tcUserPO1.setUserId(Long.parseLong(userId));
			TcUserPO tcUserPO1Value = new TcUserPO();
			TmDealerPO dealer=new TmDealerPO();
			dealer.setDealerId(Long.valueOf(dealerId));
			dealer=factory.select(dealer).get(0);
			tcUserPO1Value.setCompanyId(dealer.getCompanyId());
			tcUserPO1Value.setUpdateBy(logonUser.getUserId());
			tcUserPO1Value.setUpdateDate(new Date());
			factory.update(tcUserPO1, tcUserPO1Value);
			//zhumingwei add 2011-6-21 end
			
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
	 * SGM维护经销商用户选择经销商查询
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public void drlQuery() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			act.getResponse().setContentType("application/json");
			
			if("1".equals(request.getParamValue("COMMAND"))) {
				String orgId = request.getParamValue("DEPT_ID");
				String dcode = request.getParamValue("DRLCODE");
				String dsname = request.getParamValue("DELSNAME");
				
				String orderName = request.getParamValue("orderCol2");
				String da = request.getParamValue("order2");
				
				Integer curPage = request.getParamValue("curPage2") != null ? Integer
						.parseInt(request.getParamValue("curPage2"))
						: 1; // 处理当前页
						PageResult<TmDealerPO> ps = UserMngDAO.getDRLByDeptId(orgId,dcode,dsname,GetOemcompanyId.getOemCompanyId(logonUser), Constant.PAGE_SIZE,curPage,orderName,da);  
				act.setOutData("ps", ps);
			}
		} catch(BizException e){
			logger.error(logonUser,e);
			act.setException(e);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"SGM维护经销商用户");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void allDrlQueryForZotye() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			act.getResponse().setContentType("application/json");
			
			if("1".equals(request.getParamValue("COMMAND"))) {
				String orgId = request.getParamValue("DEPT_ID");
				String dcode = request.getParamValue("DRLCODE");
				String dsname = request.getParamValue("DELSNAME");
				String isDealerType=request.getParamValue("isDealerType");
				String orderName = request.getParamValue("orderCol2");
				String da = request.getParamValue("order2");
				String inputOrgId = request.getParamValue("ORGID");//页面输入框的组织ID，由操作人自己控制。
				String isMulti = request.getParamValue("isMulti");
				int page_size = ActionUtil.getPageSize(request);
				String isLogoutDealerClass = request.getParamValue("isLogoutDealerClass");//是否为退网经销商评级
				if(isMulti != null && !"".equals(isMulti))
				{
					page_size = 20;
				}
				
				String provinceId = request.getParamValue("downtown") ;
				if (provinceId == null) {
					provinceId = "" ;
				}
				String dealerClass = request.getParamValue("dealerClass") ;
				if (dealerClass == null) {
					dealerClass = "" ;
				}
				
				String dealerType = request.getParamValue("dealerType") ;
				if (dealerType == null) {
					dealerType = "" ;
				}
				
				String isAllLevel = request.getParamValue("isAllLevel");
				if(isAllLevel == null || isAllLevel.equals("") || isAllLevel.equals("null") || "true".equals(isAllLevel)){
					isAllLevel = "";
				}
				String isAllArea = request.getParamValue("isAllArea");
				if(isAllArea == null || isAllArea.equals("") || isAllArea.equals("null")){
					isAllArea = "false";
				}
				String areaId=CommonUtils.checkNull(request.getParamValue("AREA_ID"));
				
				Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
				//modify by zhaojinyu 2010-06-12 经销商树中售后只查询售后的经销商，销售只查询销售的经销商。
				Integer poseBusType =logonUser.getPoseBusType();
				Long poseId = logonUser.getPoseId();
				PageResult<TmDealerPO> ps = UserMngDAO.getAllDRLByDeptIdForZotye(areaId,isDealerType,orgId,dcode,dsname,2010010100070674L, provinceId, dealerClass,dealerType, page_size,curPage,orderName,da,inputOrgId,poseBusType,poseId, isAllLevel, isAllArea, isLogoutDealerClass,logonUser);  
				act.setOutData("ps", ps);
				// 复选框翻页选中 by chenyub@yonyou.com
				ActionUtil.setCheckedValueToOutData(act);
				// 自定义每页大小 by chenyub@yonyou.com
				ActionUtil.setCustomPageSizeFlag(act, true);
				// 调整列宽功能 by chenyub@yonyou.com
				ActionUtil.setResizeColumnWidthFlag(act, true);
				// 表格列排序功能 by chenyub@yonyou.com
				ActionUtil.setTableSortFlag(act, true);
				// 表格交换列功能 by chenyub@yonyou.com
				ActionUtil.setSwapColumnFlag(act, true);
				
			}
		} catch(BizException e){
			logger.error(logonUser,e);
			act.setException(e);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"SGM维护经销商用户");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	
	/*
	 * 根据销售订单编号得到物料信息
	 * */
	public void getMaterialInfo(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		
		try {
			RequestWrapper request = act.getRequest();
			act.getResponse().setContentType("application/json");
			String invoiceId = request.getParamValue("invoiceNum");
			
			Integer curPage = request.getParamValue("curPage2") != null ? Integer
					.parseInt(request.getParamValue("curPage2"))
					: 1; // 处理当前页
		    int page_size = ActionUtil.getPageSize(request);	
		    String materialCode =  request.getParamValue("materialCode");
		    String materialName = request.getParamValue("materialName");
			PageResult<VwMaterialInfoPO> ps = UserMngDAO.getMaterialInfo_dlvry(invoiceId,materialCode,materialName,curPage,page_size);
//			TtVsDlvryReqPO TtVsDlvryReqPO1 = new TtVsDlvryReqPO(); //关掉返利
//			TtVsDlvryReqPO1.setDlvryReqNo(invoiceId);
//			List<Object> dlvryList = dao.select(TtVsDlvryReqPO1);
//			if(dlvryList.size()>0){
//				TtVsDlvryReqPO TtVsDlvryReqPO = (TtVsDlvryReqPO)dlvryList.get(0);
//				if(TtVsDlvryReqPO.getIsRebate().intValue()==Constant.IF_TYPE_YES.intValue()&&TtVsDlvryReqPO.getIsRebateBill()!=Constant.IF_TYPE_YES.intValue()){ //如果有返利
//					VwMaterialInfoPO materialInfo = new VwMaterialInfoPO();
//					materialInfo.setSeriesCode("1");
//					materialInfo.setMaterialCode("FL");
//					materialInfo.setMaterialName("返利");
//					List<VwMaterialInfoPO> info = ps.getRecords();
//					if(info!=null){
//						info.add(materialInfo);
//						ps.setRecords(info);
//					}
//				}
//			}
			
			act.setOutData("ps", ps);
			// 复选框翻页选中 by chenyub@yonyou.com
			ActionUtil.setCheckedValueToOutData(act);
			// 自定义每页大小 by chenyub@yonyou.com
			ActionUtil.setCustomPageSizeFlag(act, true);
			// 调整列宽功能 by chenyub@yonyou.com
			ActionUtil.setResizeColumnWidthFlag(act, true);
			// 表格列排序功能 by chenyub@yonyou.com
			ActionUtil.setTableSortFlag(act, true);
			// 表格交换列功能 by chenyub@yonyou.com
			ActionUtil.setSwapColumnFlag(act, true);
			
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"SGM维护经销商用户");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * SGM维护经销商用户选择经销商查询
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public void allDrlQuery() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			act.getResponse().setContentType("application/json");
			
			if("1".equals(request.getParamValue("COMMAND"))) {
				String orgId = request.getParamValue("DEPT_ID");
				String dcode = request.getParamValue("DRLCODE");
				String dsname = request.getParamValue("DELSNAME");
				String isDealerType=request.getParamValue("isDealerType");
				String orderName = request.getParamValue("orderCol2");
				String da = request.getParamValue("order2");
				String inputOrgId = request.getParamValue("ORGID");//页面输入框的组织ID，由操作人自己控制。
				//added by andy.ten@tom.com
				String isMulti = request.getParamValue("isMulti");
				int page_size = ActionUtil.getPageSize(request);
				//added by hxy 2012-04-23 新增经销商评级为退网时不显示
				String isLogoutDealerClass = request.getParamValue("isLogoutDealerClass");//是否为退网经销商评级
//				if(isLogoutDealerClass == null) {
//					isLogoutDealerClass = String.valueOf(Constant.DEALER_CLASS_TYPE_13);
//				}
				if(isMulti != null && !"".equals(isMulti))
				{
					page_size = 20;
				}
				
				String provinceId = request.getParamValue("downtown") ;
				if (provinceId == null) {
					provinceId = "" ;
				}
				String dealerClass = request.getParamValue("dealerClass") ;
				if (dealerClass == null) {
					dealerClass = "" ;
				}
				
				String dealerType = request.getParamValue("dealerType") ;
				if (dealerType == null) {
					dealerType = "" ;
				}
				
				String isAllLevel = request.getParamValue("isAllLevel");
				if(isAllLevel == null || isAllLevel.equals("") || isAllLevel.equals("null")){
					isAllLevel = "false";
				}
				String isAllArea = request.getParamValue("isAllArea");
				if(isAllArea == null || isAllArea.equals("") || isAllArea.equals("null")){
					isAllArea = "false";
				}
				String areaId=CommonUtils.checkNull(request.getParamValue("AREA_ID"));
				
				//end
				Integer curPage = request.getParamValue("curPage") != null ? Integer
						.parseInt(request.getParamValue("curPage"))
						: 1; // 处理当前页
				//modify by zhaojinyu 2010-06-12 经销商树中售后只查询售后的经销商，销售只查询销售的经销商。
						Integer poseBusType =logonUser.getPoseBusType();
						Long poseId = logonUser.getPoseId();
						PageResult<TmDealerPO> ps = UserMngDAO.getAllDRLByDeptId(areaId,isDealerType,orgId,dcode,dsname,2010010100070674L, provinceId, dealerClass,dealerType, page_size,curPage,orderName,da,inputOrgId,poseBusType,poseId, isAllLevel, isAllArea, isLogoutDealerClass);  
				act.setOutData("ps", ps);
				// 复选框翻页选中 by chenyub@yonyou.com
				ActionUtil.setCheckedValueToOutData(act);
				// 自定义每页大小 by chenyub@yonyou.com
				ActionUtil.setCustomPageSizeFlag(act, true);
				// 调整列宽功能 by chenyub@yonyou.com
				ActionUtil.setResizeColumnWidthFlag(act, true);
				// 表格列排序功能 by chenyub@yonyou.com
				ActionUtil.setTableSortFlag(act, true);
				// 表格交换列功能 by chenyub@yonyou.com
				ActionUtil.setSwapColumnFlag(act, true);
				
			}
		} catch(BizException e){
			logger.error(logonUser,e);
			act.setException(e);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"SGM维护经销商用户");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void allDrlQueryForSCSJ() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			act.getResponse().setContentType("application/json");
			
			if("1".equals(request.getParamValue("COMMAND"))) {
				String orgId = request.getParamValue("DEPT_ID");
//				String DEALER_ID = request.getParamValue("inputId");
				CheckVehicleDAO dao = CheckVehicleDAO.getInstance();
				String orgCode = dao.getOrgCode(logonUser.getDealerId());
				String dcode = request.getParamValue("DRLCODE");
				String dsname = request.getParamValue("DELSNAME");
				String isDealerType=request.getParamValue("isDealerType");
				String orderName = request.getParamValue("orderCol2");
				String da = request.getParamValue("order2");
				String inputOrgCode = request.getParamValue("ORGCODE");//页面输入框的组织ID，由操作人自己控制。
				//added by andy.ten@tom.com
				String isMulti = request.getParamValue("isMulti");
				int page_size = Constant.PAGE_SIZE;//Constant.PAGE_SIZE,Constant.PAGE_SIZE_MAX; //;
				//added by hxy 2012-04-23 新增经销商评级为退网时不显示
				String isLogoutDealerClass = request.getParamValue("isLogoutDealerClass");//是否为退网经销商评级
//				if(isLogoutDealerClass == null) {
//					isLogoutDealerClass = String.valueOf(Constant.DEALER_CLASS_TYPE_13);
//				}
				if(isMulti != null && !"".equals(isMulti))
				{
					page_size = 20;
				}
				
				String provinceId = request.getParamValue("downtown") ;
				if (provinceId == null) {
					provinceId = "" ;
				}
				String dealerClass = request.getParamValue("dealerClass") ;
				if (dealerClass == null) {
					dealerClass = "" ;
				}
				
				String dealerType = request.getParamValue("dealerType") ;
				if (dealerType == null) {
					dealerType = "" ;
				}
				
				String isAllLevel = request.getParamValue("isAllLevel");
				if(isAllLevel == null || isAllLevel.equals("") || isAllLevel.equals("null")){
					isAllLevel = "false";
				}
				String isAllArea = request.getParamValue("isAllArea");
				if(isAllArea == null || isAllArea.equals("") || isAllArea.equals("null")){
					isAllArea = "false";
				}
				String areaId=CommonUtils.checkNull(request.getParamValue("AREA_ID"));
				
				//end
				Integer curPage = request.getParamValue("curPage2") != null ? Integer
						.parseInt(request.getParamValue("curPage2"))
						: 1; // 处理当前页
				//modify by zhaojinyu 2010-06-12 经销商树中售后只查询售后的经销商，销售只查询销售的经销商。
						Integer poseBusType =logonUser.getPoseBusType();
						Long poseId = logonUser.getPoseId();
						PageResult<TmDealerPO> ps = UserMngDAO.getAllDRLByDeptIdForSCSJ(areaId,isDealerType,orgId,dcode,dsname,2010010100070674L, provinceId, dealerClass,dealerType, page_size,curPage,orderName,da,inputOrgCode,poseBusType,poseId, isAllLevel, isAllArea, isLogoutDealerClass,orgCode);  
				act.setOutData("ps", ps);
			}
		} catch(BizException e){
			logger.error(logonUser,e);
			act.setException(e);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"SGM维护经销商用户");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}

	/**
	 * 新闻公告发布专用
	 */
	public void allDrlQueryByNews() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			act.getResponse().setContentType("application/json");
			
			if("1".equals(request.getParamValue("COMMAND"))) {
				String orgId = request.getParamValue("DEPT_ID");
				String dcode = request.getParamValue("DRLCODE");
				String dsname = request.getParamValue("DELSNAME");
				String isDealerType=request.getParamValue("isDealerType");
				String dealerLevel=request.getParamValue("dealerLevel");
				String orderName = request.getParamValue("orderCol2");
				String da = request.getParamValue("order2");
				String inputOrgId = request.getParamValue("ORGID");//页面输入框的组织ID，由操作人自己控制。
				//added by andy.ten@tom.com
				String isMulti = request.getParamValue("isMulti");
				int page_size =200;// Constant.PAGE_SIZE;//Constant.PAGE_SIZE,Constant.PAGE_SIZE_MAX; //;
				//added by hxy 2012-04-23 新增经销商评级为退网时不显示
				String isLogoutDealerClass = request.getParamValue("isLogoutDealerClass");//是否为退网经销商评级
//				if(isLogoutDealerClass == null) {
//					isLogoutDealerClass = String.valueOf(Constant.DEALER_CLASS_TYPE_13);
//				}
				if(isMulti != null && !"".equals(isMulti))
				{
					page_size = 20;
				}
				
				String provinceId = request.getParamValue("downtown") ;
				if (provinceId == null) {
					provinceId = "" ;
				}
				String dealerClass = request.getParamValue("dealerClass") ;
				if (dealerClass == null) {
					dealerClass = "" ;
				}
				
				String dealerType = request.getParamValue("dealerType") ;
				if (dealerType == null) {
					dealerType = "" ;
				}
				
				String isAllLevel = request.getParamValue("isAllLevel");
				if(isAllLevel == null || isAllLevel.equals("") || isAllLevel.equals("null")){
					isAllLevel = "false";
				}
				String isAllArea = request.getParamValue("isAllArea");
				if(isAllArea == null || isAllArea.equals("") || isAllArea.equals("null")){
					isAllArea = "false";
				}
				String areaId=CommonUtils.checkNull(request.getParamValue("AREA_ID"));
				
				//end
				Integer curPage = request.getParamValue("curPage2") != null ? Integer
						.parseInt(request.getParamValue("curPage2"))
						: 1; // 处理当前页
				//modify by zhaojinyu 2010-06-12 经销商树中售后只查询售后的经销商，销售只查询销售的经销商。
						Integer poseBusType =logonUser.getPoseBusType();
						Long poseId = logonUser.getPoseId();
						PageResult<TmDealerPO> ps = UserMngDAO.getAllDRLByDeptIdByNews(areaId,isDealerType,orgId,dcode,dsname,GetOemcompanyId.getOemCompanyId(logonUser), provinceId, dealerClass,dealerType, page_size,curPage,orderName,da,inputOrgId,poseBusType,poseId, isAllLevel, isAllArea, isLogoutDealerClass,dealerLevel);  
				act.setOutData("ps", ps);
			}
		} catch(BizException e){
			logger.error(logonUser,e);
			act.setException(e);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"SGM维护经销商用户");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void allDrlQueryCon() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			act.getResponse().setContentType("application/json");
			
			if("1".equals(request.getParamValue("COMMAND"))) {
				
				String userId = request.getParamValue("userId");
				
				String orgId = request.getParamValue("DEPT_ID");
				String dcode = request.getParamValue("DRLCODE");
				String dsname = request.getParamValue("DELSNAME");
				String isDealerType=request.getParamValue("isDealerType");
				String orderName = request.getParamValue("orderCol2");
				String da = request.getParamValue("order2");
				String inputOrgId = request.getParamValue("ORGID");//页面输入框的组织ID，由操作人自己控制。
				//added by andy.ten@tom.com
				String isMulti = request.getParamValue("isMulti");
				int page_size = Constant.PAGE_SIZE_MAX;//Constant.PAGE_SIZE,Constant.PAGE_SIZE_MAX; //;
				if(isMulti != null && !"".equals(isMulti))
				{
					page_size = 20;
				}
				
				String provinceId = request.getParamValue("downtown") ;
				if (provinceId == null) {
					provinceId = "" ;
				}
				String dealerClass = request.getParamValue("dealerClass") ;
				if (dealerClass == null) {
					dealerClass = "" ;
				}
				String isAllLevel = request.getParamValue("isAllLevel");
				if(isAllLevel == null || isAllLevel.equals("") || isAllLevel.equals("null")){
					isAllLevel = "false";
				}
				String isAllArea = request.getParamValue("isAllArea");
				if(isAllArea == null || isAllArea.equals("") || isAllArea.equals("null")){
					isAllArea = "false";
				}
				String areaId=request.getParamValue("AREA_ID");
				
				//end
				Integer curPage = request.getParamValue("curPage2") != null ? Integer
						.parseInt(request.getParamValue("curPage2"))
						: 1; // 处理当前页
				//modify by zhaojinyu 2010-06-12 经销商树中售后只查询售后的经销商，销售只查询销售的经销商。
						Integer poseBusType =logonUser.getPoseBusType();
						Long poseId = logonUser.getPoseId();
//						System.out.println(poseId);
						PageResult<TmDealerPO> ps = UserMngDAO.getAllDRLByDeptIdCon(userId,areaId,isDealerType,orgId,dcode,dsname,GetOemcompanyId.getOemCompanyId(logonUser), provinceId, dealerClass, page_size,curPage,orderName,da,inputOrgId,poseBusType,poseId, isAllLevel, isAllArea);  
				act.setOutData("ps", ps);
			}
		} catch(BizException e){
			logger.error(logonUser,e);
			act.setException(e);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"SGM维护经销商用户");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void allDrlQueryZWCon() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			act.getResponse().setContentType("application/json");
			
			if("1".equals(request.getParamValue("COMMAND"))) {
				
				String userId = request.getParamValue("userId");
				
				String orgId = request.getParamValue("DEPT_ID");
				String dcode = request.getParamValue("DRLCODE");
				String dsname = request.getParamValue("DELSNAME");
				String isDealerType=request.getParamValue("isDealerType");
				String orderName = request.getParamValue("orderCol2");
				String da = request.getParamValue("order2");
				String inputOrgId = request.getParamValue("ORGID");//页面输入框的组织ID，由操作人自己控制。
				//added by andy.ten@tom.com
				String isMulti = request.getParamValue("isMulti");
				int page_size = Constant.PAGE_SIZE_MAX;//Constant.PAGE_SIZE,Constant.PAGE_SIZE_MAX; //;
				if(isMulti != null && !"".equals(isMulti))
				{
					page_size = 20;
				}
				
				String provinceId = request.getParamValue("downtown") ;
				if (provinceId == null) {
					provinceId = "" ;
				}
				String dealerClass = request.getParamValue("dealerClass") ;
				if (dealerClass == null) {
					dealerClass = "" ;
				}
				String isAllLevel = request.getParamValue("isAllLevel");
				if(isAllLevel == null || isAllLevel.equals("") || isAllLevel.equals("null")){
					isAllLevel = "false";
				}
				String isAllArea = request.getParamValue("isAllArea");
				if(isAllArea == null || isAllArea.equals("") || isAllArea.equals("null")){
					isAllArea = "false";
				}
				String areaId=request.getParamValue("AREA_ID");
				
				//end
				Integer curPage = request.getParamValue("curPage2") != null ? Integer
						.parseInt(request.getParamValue("curPage2"))
						: 1; // 处理当前页
				//modify by zhaojinyu 2010-06-12 经销商树中售后只查询售后的经销商，销售只查询销售的经销商。
						Integer poseBusType =logonUser.getPoseBusType();
						String dealerId = logonUser.getDealerId();
//						System.out.println(dealerId);
						PageResult<TmDealerPO> ps = UserMngDAO.getAllDRLByDeptIdZWCon(userId,areaId,isDealerType,orgId,dcode,dsname,GetOemcompanyId.getOemCompanyId(logonUser), provinceId, dealerClass, page_size,curPage,orderName,da,inputOrgId,poseBusType,dealerId, isAllLevel, isAllArea);  
				act.setOutData("ps", ps);
			}
		} catch(BizException e){
			logger.error(logonUser,e);
			act.setException(e);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"SGM维护经销商用户");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void allDrlQueryNew() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			act.getResponse().setContentType("application/json");
			
			if("1".equals(request.getParamValue("COMMAND"))) {
				String orgId = request.getParamValue("DEPT_ID");
				String dcode = request.getParamValue("DRLCODE");
				String dsname = request.getParamValue("DELSNAME");
				String isDealerType=request.getParamValue("isDealerType");
				String orderName = request.getParamValue("orderCol2");
				String da = request.getParamValue("order2");
				String inputOrgId = request.getParamValue("ORGID");//页面输入框的组织ID，由操作人自己控制。
				//added by andy.ten@tom.com
				String isMulti = request.getParamValue("isMulti");
				int page_size = Constant.PAGE_SIZE_MAX;//Constant.PAGE_SIZE,Constant.PAGE_SIZE_MAX; //;
				if(isMulti != null && !"".equals(isMulti))
				{
					page_size = 20;
				}
				
				String provinceId = request.getParamValue("downtown") ;
				if (provinceId == null) {
					provinceId = "" ;
				}
				String dealerClass = request.getParamValue("dealerClass") ;
				if (dealerClass == null) {
					dealerClass = "" ;
				}
				String isAllLevel = request.getParamValue("isAllLevel");
				if(isAllLevel == null || isAllLevel.equals("") || isAllLevel.equals("null")){
					isAllLevel = "false";
				}
				String isAllArea = request.getParamValue("isAllArea");
				if(isAllArea == null || isAllArea.equals("") || isAllArea.equals("null")){
					isAllArea = "false";
				}
				String areaId=request.getParamValue("AREA_ID");
				
				//end
				Integer curPage = request.getParamValue("curPage2") != null ? Integer
						.parseInt(request.getParamValue("curPage2"))
						: 1; // 处理当前页
				//modify by zhaojinyu 2010-06-12 经销商树中售后只查询售后的经销商，销售只查询销售的经销商。
						Integer poseBusType =logonUser.getPoseBusType();
						Long poseId = logonUser.getPoseId();
						PageResult<TmDealerPO> ps = UserMngDAO.getAllDRLByDeptIdNew(areaId,isDealerType,orgId,dcode,dsname,GetOemcompanyId.getOemCompanyId(logonUser), provinceId, dealerClass, page_size,curPage,orderName,da,inputOrgId,poseBusType,poseId, isAllLevel, isAllArea);  
				act.setOutData("ps", ps);
			}
		} catch(BizException e){
			logger.error(logonUser,e);
			act.setException(e);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"SGM维护经销商用户");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * SGM维护经销商用户选择省份经销商查询
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public void allRegDrlQuery() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			act.getResponse().setContentType("application/json");
			
			if("1".equals(request.getParamValue("COMMAND"))) {
				String orgId = request.getParamValue("DEPT_ID");
				String dcode = request.getParamValue("DRLCODE");
				String regCode = request.getParamValue("regCode");
				String dsname = request.getParamValue("DELSNAME");
				String isDealerType=request.getParamValue("isDealerType");
				String orderName = request.getParamValue("orderCol2");
				String da = request.getParamValue("order2");
				String inputOrgId = request.getParamValue("ORGID");//页面输入框的组织ID，由操作人自己控制。
				String isMulti = request.getParamValue("isMulti");
				int page_size = Constant.PAGE_SIZE_MAX;//Constant.PAGE_SIZE,Constant.PAGE_SIZE_MAX; //;
				if(isMulti != null && !"".equals(isMulti))
				{
					page_size = 20;
				}
				
				String provinceId = request.getParamValue("downtown") ;
				if (provinceId == null) {
					provinceId = "" ;
				}
				String dealerClass = request.getParamValue("dealerClass") ;
				if (dealerClass == null) {
					dealerClass = "" ;
				}
				String isAllLevel = request.getParamValue("isAllLevel");
				if(isAllLevel == null || isAllLevel.equals("") || isAllLevel.equals("null")){
					isAllLevel = "false";
				}
				String isAllArea = request.getParamValue("isAllArea");
				if(isAllArea == null || isAllArea.equals("") || isAllArea.equals("null")){
					isAllArea = "false";
				}
				String areaId = request.getParamValue("AREA_ID");
				
				//end
				Integer curPage = request.getParamValue("curPage2") != null ? Integer
						.parseInt(request.getParamValue("curPage2"))
						: 1; // 处理当前页
						Integer poseBusType =logonUser.getPoseBusType();
						Long poseId = logonUser.getPoseId();
						PageResult<TmDealerPO> ps = dao.getAllDRLByDeptIdReg(regCode,areaId,isDealerType,orgId,dcode,dsname,GetOemcompanyId.getOemCompanyId(logonUser), provinceId, dealerClass, page_size,curPage,orderName,da,inputOrgId,poseBusType,poseId, isAllLevel, isAllArea);  
				act.setOutData("ps", ps);
			}
		} catch(BizException e){
			logger.error(logonUser,e);
			act.setException(e);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"SGM维护经销商用户");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * SGM维护选择省份
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public void allRegionQuery() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			act.getResponse().setContentType("application/json");
				String regionCode = request.getParamValue("regCode");//页面输入框的组织ID，由操作人自己控制。
				String regionName = request.getParamValue("regName");
				String org_id = request.getParamValue("orgId");
				String dealer_type = request.getParamValue("DEALER_TYPE");
				Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页			
				Map<String, String> map = new HashMap<String, String>() ;
				map.put("regionCode", regionCode) ;
				map.put("regionName", regionName) ;
				map.put("org_id", org_id) ;
				map.put("dealer_type", dealer_type) ;
				
				PageResult<Map<String,Object>> ps = dao.getAllRegion(map,curPage,ActionUtil.getPageSize(request));
				act.setOutData("ps", ps);	
				// 复选框翻页选中 by chenyub@yonyou.com
				ActionUtil.setCheckedValueToOutData(act);
				// 自定义每页大小 by chenyub@yonyou.com
				ActionUtil.setCustomPageSizeFlag(act, true);
				// 调整列宽功能 by chenyub@yonyou.com
				ActionUtil.setResizeColumnWidthFlag(act, true);
				// 表格列排序功能 by chenyub@yonyou.com
				ActionUtil.setTableSortFlag(act, true);
				// 表格交换列功能 by chenyub@yonyou.com
				ActionUtil.setSwapColumnFlag(act, true);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"SGM维护经销商用户");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void allCityQuery() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			act.getResponse().setContentType("application/json");
				String regionCode = request.getParamValue("regCode");//页面输入框的组织ID，由操作人自己控制。
				String regionName = request.getParamValue("regName");
				String CityName = request.getParamValue("CityName");
				String CityCode = request.getParamValue("CityCode");
				String org_id = request.getParamValue("orgId");
				String dealer_type = request.getParamValue("DEALER_TYPE");
				Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页			
				Map<String, String> map = new HashMap<String, String>() ;
				map.put("regionCode", regionCode) ;
				map.put("regionName", regionName) ;
				map.put("org_id", org_id) ;
				map.put("dealer_type", dealer_type) ;
				map.put("CityName", CityName) ;
				map.put("CityCode", CityCode) ;
				
				PageResult<Map<String,Object>> ps = dao.getAllCity(map,curPage,Constant.PAGE_SIZE);
				act.setOutData("ps", ps);	
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"SGM维护经销商用户");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * SGM维护选择组织
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public void allOrgQuery() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String areaId=request.getParamValue("areaId");
			act.getResponse().setContentType("application/json");
//			if(MaterialGroupManagerDao.getPoseIdBusiness(String.valueOf(logonUser.getPoseId())).size()>0){
//			areaId=MaterialGroupManagerDao.getPoseIdBusiness(String.valueOf(logonUser.getPoseId())).get(0).get("AREA_ID").toString();
//			}
			if("1".equals(request.getParamValue("COMMAND"))) {
				String orgId = request.getParamValue("orgId");
				String orgCode = request.getParamValue("orgCode");
				String orgName = request.getParamValue("orgName");
				String inputOrgId = request.getParamValue("ORGID");//页面输入框的组织ID，由操作人自己控制。
				Integer curPage = request.getParamValue("curPage") != null ? Integer
						.parseInt(request.getParamValue("curPage"))
						: 1; // 处理当前页
						PageResult<TmOrgPO> ps = UserMngDAO.getAllOrg(areaId,orgId,orgCode,orgName,logonUser.getCompanyId(),300,curPage,inputOrgId,false);  
				act.setOutData("ps", ps);
			}
		} catch(BizException e){
			logger.error(logonUser,e);
			act.setException(e);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"SGM维护经销商用户");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	
	/** zxf
	 * SGM维护选择组织
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public void allOrgQueryArea() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String areaId=request.getParamValue("areaId");
			act.getResponse().setContentType("application/json");
//			if(MaterialGroupManagerDao.getPoseIdBusiness(String.valueOf(logonUser.getPoseId())).size()>0){
//			areaId=MaterialGroupManagerDao.getPoseIdBusiness(String.valueOf(logonUser.getPoseId())).get(0).get("AREA_ID").toString();
//			}
			if("1".equals(request.getParamValue("COMMAND"))) {
				String orgId = request.getParamValue("orgId");
				String orgCode = request.getParamValue("orgCode");
				String orgName = request.getParamValue("orgName");
				String inputOrgId = request.getParamValue("ORGID");//页面输入框的组织ID，由操作人自己控制。
				Integer curPage = request.getParamValue("curPage") != null ? Integer
						.parseInt(request.getParamValue("curPage"))
						: 1; // 处理当前页
						PageResult<TmOrgPO> ps = UserMngDAO.getAllOrg(areaId,orgId,orgCode,orgName,logonUser.getCompanyId(),300,curPage,inputOrgId,true);  
				act.setOutData("ps", ps);
			}
		} catch(BizException e){
			logger.error(logonUser,e);
			act.setException(e);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"SGM维护经销商用户");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	//zhumingwei add by 2011-02-25
	public void allOrgQuery111() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String areaId=request.getParamValue("areaId");
			act.getResponse().setContentType("application/json");
			if("1".equals(request.getParamValue("COMMAND"))) {
				String orgId = request.getParamValue("orgId");
				String orgCode = request.getParamValue("orgCode");
				String orgName = request.getParamValue("orgName");
				String inputOrgId = request.getParamValue("ORGID");//页面输入框的组织ID，由操作人自己控制。
				Integer curPage = request.getParamValue("curPage") != null ? Integer
						.parseInt(request.getParamValue("curPage"))
						: 1; // 处理当前页
						PageResult<TmOrgPO> ps = UserMngDAO.getAllOrg111(areaId,orgId,orgCode,orgName,logonUser.getCompanyId(), Constant.PAGE_SIZE,curPage,inputOrgId);  
				act.setOutData("ps", ps);
			}
		} catch(BizException e){
			logger.error(logonUser,e);
			act.setException(e);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"SGM维护经销商用户");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * SGM维护选择组织
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public void allAreaDealerQuery() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String areaId="";
			act.getResponse().setContentType("application/json");
			if(MaterialGroupManagerDao.getPoseIdBusiness(String.valueOf(logonUser.getPoseId())).size()>0){
			areaId=MaterialGroupManagerDao.getPoseIdBusiness(String.valueOf(logonUser.getPoseId())).get(0).get("AREA_ID").toString();
			}
			if("1".equals(request.getParamValue("COMMAND"))) {
				String inputOrgId = request.getParamValue("ORGID");//页面输入框的组织ID，由操作人自己控制。
				Integer curPage = request.getParamValue("curPage") != null ? Integer
						.parseInt(request.getParamValue("curPage"))
						: 1; // 处理当前页
						PageResult<TmDealerPO> ps = UserMngDAO.getAllAreaDealer(areaId, inputOrgId, curPage, Constant.PAGE_SIZE);  
				act.setOutData("ps", ps);
			}
		} catch(BizException e){
			logger.error(logonUser,e);
			act.setException(e);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"SGM维护经销商用户");
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
			String companyId = request.getParamValue("companyId");
			String poseIds = request.getParamValue("poseIds"); // 用户当前的职位ID
			
			List<MsgCarrier> vlist = new ArrayList<MsgCarrier>();
			vlist.add(MsgCarrier.getInstance(ValidateCodeConstant.NOQUOTATION_PATTERN,"职位代码",0,Constant.Length_Check_Char_20,poseCode));
			vlist.add(MsgCarrier.getInstance(ValidateCodeConstant.NOQUOTATION_PATTERN,"职位名称",0,Constant.Length_Check_Char_20,poseName));
			Validate.doValidate(act, vlist);
			
			List<TcPosePO> ps = UserMngDAO.getDRLPoseByCompanyId(new Long(companyId),poseCode, poseName,poseIds);
			act.setOutData("ps", ps);
		} catch(BizException e){
			logger.error(logonUser,e);
			act.setException(e);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"SGM维护经销商用户");
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
			String companyId = request.getParamValue("dealerId");
			List<TcPosePO> ps = null;
			//2017-7-20 当companyId 为空，代表是车厂建经销职位，不为空，代表经销商建自己的职位
			if (CommonUtils.isEmpty(companyId)) {
				ps = UserMngDAO.getDRLPoseByPoseIds(new Long(logonUser.getDealerId()),poseIds);
			} else {
				ps = UserMngDAO.getDRLPoseByPoseIds(new Long(companyId),poseIds);
			}
			act.setOutData("ps", ps);
		} catch(BizException e){
			logger.error(logonUser,e);
			act.setException(e);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"SGM维护经销商用户");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * SGM维护经销商用户查询
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public void sgmDealerSysUserQuery() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			act.getResponse().setContentType("application/json");
			
			if("1".equals(request.getParamValue("COMMAND"))) {
				// 部门
				String deptId = request.getParamValue("DEPT_ID");
				// 经销商账号
				String acnt = request.getParamValue("ACNT");
				// 姓名
				String name = request.getParamValue("NAME");
				// 经销商ID
				String companyId = request.getParamValue("COMPANY_ID");
				String dealerId = request.getParamValue("dealerId");
				// 职位代码
				String poseCode = request.getParamValue("POSE_CODE");
				Long oemCompanyId = logonUser.getCompanyId();
				List<MsgCarrier> vlist = new ArrayList<MsgCarrier>();
				vlist.add(MsgCarrier.getInstance(ValidateCodeConstant.NOQUOTATION_PATTERN,"部门ID",0,Constant.Length_Check_Char_100,deptId));
				vlist.add(MsgCarrier.getInstance(ValidateCodeConstant.NOQUOTATION_PATTERN,"经销商账号",0,Constant.Length_Check_Char_100,acnt));
				vlist.add(MsgCarrier.getInstance(ValidateCodeConstant.NOQUOTATION_PATTERN,"姓名",0,Constant.Length_Check_Char_100,name));
				vlist.add(MsgCarrier.getInstance(ValidateCodeConstant.NOQUOTATION_PATTERN,"经销商ID",0,Constant.Length_Check_Char_100,companyId));
				vlist.add(MsgCarrier.getInstance(ValidateCodeConstant.NOQUOTATION_PATTERN,"职位代码",0,Constant.Length_Check_Char_100,poseCode));
				Validate.doValidate(act, vlist);
				
				String orderName = request.getParamValue("orderCol");
				String da = request.getParamValue("order");
				
				Integer curPage = request.getParamValue("curPage") != null ? Integer
						.parseInt(request.getParamValue("curPage"))
						: 1; // 处理当前页
						// PageResult<TcUserPO> ps = UserMngDAO.sgmDrlSysUserQuery(companyId,oemCompanyId,deptId, acnt, name, poseCode, Constant.PAGE_SIZE,curPage,orderName,da); // 按条件查询系统用户
				PageResult<Map<String, Object>> ps = UserMngDAO.sgmDrlSysUserQueryA(dealerId,oemCompanyId,deptId, acnt, name, poseCode, ActionUtil.getPageSize(request),curPage,orderName,da); // 按条件查询系统用户
				act.setOutData("ps", ps);
				ActionUtil.setCustomPageSizeFlag(act, true);
			}
		} catch(BizException e){
			logger.error(logonUser,e);
			act.setException(e);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"SGM维护经销商用户");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 经销商添加供应商查询
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public void allSuppQuery() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String suppCode = CommonUtils.checkNull(request.getParamValue("suppCode"));  		//供应商代码
			String suppName = CommonUtils.checkNull(request.getParamValue("suppName"));		   	//供应商名称
			//分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页	
			PageResult<TmPtSupplierPO> ps = UserMngDAO.allSuppQuery(suppCode,suppName, curPage,Constant.PAGE_SIZE);
			//分页方法 end
			act.setOutData("ps", ps);     //向前台传的list 名称是固定的不可改必须用 ps
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"配件大类");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
/**
 * 经销商添加配件大类查询
 * @param null
 * @return void
 * @throws Exception
 */
public void allPartTypeQuery() {
	ActionContext act = ActionContext.getContext();
	AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
	try {
		RequestWrapper request = act.getRequest();
		String suppCode = CommonUtils.checkNull(request.getParamValue("suppCode"));  		//配件大类代码
		String suppName = CommonUtils.checkNull(request.getParamValue("suppName"));		   	//配件大类名称
		//分页方法 begin
		Integer curPage = request.getParamValue("curPage") != null ? Integer
				.parseInt(request.getParamValue("curPage"))
				: 1; // 处理当前页	
		PageResult<TmPtPartTypePO> ps = UserMngDAO.allPartTypeQuery(suppCode,suppName, curPage,Constant.PAGE_SIZE);
		//分页方法 end
		act.setOutData("ps", ps);     //向前台传的list 名称是固定的不可改必须用 ps
	}catch(Exception e) {//异常方法
		BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"配件大类");
		logger.error(logonUser,e1);
		act.setException(e1);
	}
  }


	/**
	 * 根据经销商代码批量生成对应用户及职位
	 * 
	 * @author chenyub@yonyou.com
	 */
	public void batchAddSgmDealerUser(){

		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		String hint = null; 
		try {
			RequestWrapper request = act.getRequest();
			String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode"));  		//批量生成类型
			if(dealerCode.endsWith("-S")||dealerCode.endsWith("-F")){
				UserMngDAO.batchAddUserByDealerCode(dealerCode);
			} else {
				throw new RuntimeException("经销商代码不是正确的格式!");
			}
			hint = "生成成功";
			act.setOutData("hint", hint);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ADD_FAILURE_CODE,"经销商用户批量生成");
			logger.error(logonUser,e1);
			hint = "操作失败!" + e.getMessage();
			act.setOutData("hint", hint);
		}
	}


}