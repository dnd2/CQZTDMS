
/**********************************************************************
* <pre>
* FILE : DealerPosition.java
* CLASS : DealerPosition
* 
* AUTHOR : ChenLiang
*
* FUNCTION : 经销商职位维护Action.
*
*
*======================================================================
* CHANGE HISTORY LOG
*----------------------------------------------------------------------
* MOD. NO.| DATE     |    NAME    | REASON | CHANGE REQ.
*----------------------------------------------------------------------
*         |2009-08-26| ChenLiang  | Created |
* DESCRIPTION:
* </pre>
***********************************************************************/

package com.infodms.dms.actions.sysmng.dealerposition;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.MsgCarrier;
import com.infodms.dms.common.ValidateCodeConstant;
import com.infodms.dms.dao.common.CommonDAO;
import com.infodms.dms.dao.dealerposition.DealerPositionDAO;
import com.infodms.dms.dao.sysposition.SysPositionDAO;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TcDataAuthPO;
import com.infodms.dms.po.TcFuncPO;
import com.infodms.dms.po.TcPosePO;
import com.infodms.dms.po.TcRolePO;
import com.infodms.dms.po.TmOrgPO;
import com.infodms.dms.po.TrPoseBinsPO;
import com.infodms.dms.po.TrPoseFuncDataAuthPO;
import com.infodms.dms.po.TrPoseFuncPO;
import com.infodms.dms.po.TrRoleBinsPO;
import com.infodms.dms.po.TrRoleFuncPO;
import com.infodms.dms.util.businessUtil.Validate;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.POFactory;
import com.infoservice.po3.POFactoryBuilder;
import com.infoservice.po3.bean.PageResult;

public class DealerPosition {

	public Logger logger = Logger.getLogger(DealerPosition.class);
	private POFactory factory = POFactoryBuilder.getInstance();
	private final String querySysPositionInitUrl = "/jsp/systemMng/userMng/dealerPositionSearch.jsp";
	private final String addSysPositionInitUrl = "/jsp/systemMng/userMng/dealerPsoitionAdd.jsp";
	private final String addSysPositionRoleInit = "/jsp/systemMng/userMng/dealerPsoitionDetail.jsp";
	private final String viewSysRoleInitUrl = "/jsp/systemMng/userMng/dealerPsoitionDetail.jsp";
	private final String addDealerRoleInitUrl = "/jsp/systemMng/userMng/dealerPoseRoleSearch.jsp";

	/**
	 * 经销商职位查询页面初始化
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public void querySysPositionInit() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword(querySysPositionInitUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"经销商职位");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 经销商职位管理添加角色初始化
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public void addDealerRoleInit() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword(addDealerRoleInitUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"经销商职位");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 添加经销商职位时选择角色
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public void addSysPositionRoleSerch() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			act.getResponse().setContentType("application/json");
			RequestWrapper request = act.getRequest();
			String rolep = request.getParamValue("rolep");
			String ROLE_DESC = request.getParamValue("ROLE_DESC");
			Long companyId=logonUser.getCompanyId();
			List<TcRolePO> ps = SysPositionDAO.sysRoleQuery(rolep, ROLE_DESC,companyId);
			act.setOutData("ps", ps);
		} catch(BizException e){
			logger.error(logonUser,e);
			act.setException(e);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"经销商职位");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 系统职位管理添加角色初始化
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public void addSysPositionRoleInit() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String poseType = request.getParamValue("poseType");
			act.setOutData("poseType", poseType);
			act.setForword(addSysPositionRoleInit);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"经销商职位");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 添加经销商职位
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public void addPosition() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			act.getResponse().setContentType("application/json");
			RequestWrapper request = act.getRequest();
			
			String orgId = request.getParamValue("DEPT_ID"); // 组织或部门ID
			Integer poseType = Constant.SYS_USER_DEALER; // 职位类型
			String poseCode = request.getParamValue("POSE_CODE"); // 职位代码
			String poseName = request.getParamValue("POSE_NAME"); // 职位名称
			String poseStatus = request.getParamValue("POSE_STATUS"); // 职位状态
			String funsh = request.getParamValue("FUNSH"); // 功能所对应的数据权限类型
			String myfun = request.getParamValue("MYFUNS"); // 职位对应的功能
			
			String gjyw = request.getParamValue("GG");
		
			List<MsgCarrier> vlist = new ArrayList<MsgCarrier>();
			vlist.add(MsgCarrier.getInstance(ValidateCodeConstant.DIGIT_PATTERN,"部门ID",1,Constant.Length_Check_Char_10,orgId));
			vlist.add(MsgCarrier.getInstance(ValidateCodeConstant.DIGIT_LETTER_PATTERN,"职位代码",1,Constant.Length_Check_Char_60,poseCode));
			vlist.add(MsgCarrier.getInstance(ValidateCodeConstant.DIGIT_LETTER_CN_PATTERN,"职位名称",1,Constant.Length_Check_Char_30,poseName));
			Validate.doValidate(act, vlist);
			
			TcPosePO posePO3 = new TcPosePO();
			posePO3.setPoseCode(poseCode);
			posePO3.setCompanyId(new Long(logonUser.getCompanyId()));
			List<TcPosePO> list = factory.select(posePO3);
			if(list!=null && list.size()>0) {
				act.setOutData("st", "poseCode_error");
				return ;
			}
			
			TcPosePO posePO4 = new TcPosePO();
			posePO4.setPoseName(poseName);
			posePO4.setCompanyId(new Long(logonUser.getCompanyId()));
			List<TcPosePO> list2 = factory.select(posePO4);
			if(list2!=null && list2.size()>0) {
				act.setOutData("st", "poseName_error");
				return ;
			}
			
			String[] funshs = new String[0];
			if(funsh !=null && funsh.length()>0) {
				funsh = funsh.substring(0, funsh.length()-1);
				funshs = funsh.split("#");
			}
			
			String[] t1; 
			String[] t2;
			HashMap<String, String[]> hashMap = new HashMap<String, String[]>(); // 将功能权限保存在map中
			for(int i=0; i<funshs.length; i++) { // 保存功能权限
				t1 = funshs[i].split(":"); // 功能ID
				if(t1.length>1) {
					t2 = funshs[i].split(":")[1].split(","); // 功能对应数据权限
					hashMap.put(t1[0], t2);
				}
			}
			
			TcPosePO posePO = new TcPosePO(); // 待保存的职位对象
			posePO.setPoseId(factory.getLongPK(new TcPosePO()));
			posePO.setOrgId(new Long(orgId));
			posePO.setPoseType(Integer.valueOf(poseType));
			posePO.setPoseCode(poseCode);
			posePO.setPoseName(poseName);
			posePO.setPoseStatus(Integer.valueOf(poseStatus));
			posePO.setCompanyId(new Long(logonUser.getCompanyId()));
			factory.insert(posePO);
			
			String[] myfuns = new String[0];
			if(myfun!=null && !"".equals(myfuns)) {
				myfuns = myfun.split(",");
			}
			for(int i=0; i<myfuns.length; i++) { // 保存职位对应的功能
				TrPoseFuncPO poseFuncPO = new TrPoseFuncPO();
				poseFuncPO.setPoseFuncId(factory.getLongPK(new TrPoseFuncPO()));
				poseFuncPO.setFuncId(new Long(myfuns[i]));
				poseFuncPO.setPoseId(posePO.getPoseId());
				factory.insert(poseFuncPO);
				
				if(hashMap.containsKey(myfuns[i])) { // 该功能设置了数据权限
					String[] ts = hashMap.get(myfuns[i]);
					for(int j=0; j<ts.length; j++) {
						TrPoseFuncDataAuthPO trPoseFuncDataAuthPO = new TrPoseFuncDataAuthPO();
						trPoseFuncDataAuthPO.setPoseFuncDataAuthId(factory.getLongPK(new TrPoseFuncDataAuthPO()));
						trPoseFuncDataAuthPO.setFuncId(new Long(myfuns[i]));
						trPoseFuncDataAuthPO.setPoseId(posePO.getPoseId());
						trPoseFuncDataAuthPO.setDataAuthId(new Long(ts[j]));
						trPoseFuncDataAuthPO.setPossieFuncId(poseFuncPO.getPoseFuncId());
						factory.insert(trPoseFuncDataAuthPO);
					}
				} else {
					TrPoseFuncDataAuthPO trPoseFuncDataAuthPO = new TrPoseFuncDataAuthPO();
					trPoseFuncDataAuthPO.setPoseFuncDataAuthId(factory.getLongPK(new TrPoseFuncDataAuthPO()));
					trPoseFuncDataAuthPO.setFuncId(new Long(myfuns[i]));
					trPoseFuncDataAuthPO.setPoseId(posePO.getPoseId());
					trPoseFuncDataAuthPO.setDataAuthId(Constant.DRL_BZZJYX);
					trPoseFuncDataAuthPO.setPossieFuncId(poseFuncPO.getPoseFuncId());
					factory.insert(trPoseFuncDataAuthPO);
				}
			}
			
			if(gjyw != null && !"".equals(gjyw)) {
				String[] temp = gjyw.split(",");
				for(int i=0; i<temp.length; i++) {
					TrPoseBinsPO trPoseBinsPO = new TrPoseBinsPO();
					trPoseBinsPO.setPoseBinsId(factory.getStringPK(new TrPoseBinsPO()));
					trPoseBinsPO.setPoseId(String.valueOf(posePO.getPoseId()));
					trPoseBinsPO.setBinsCodeId(temp[i]);
					factory.insert(trPoseBinsPO);
				}
			}
			
			act.setOutData("st", "succeed");
		} catch(BizException e){
			logger.error(logonUser,e);
			act.setException(e);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"经销商职位");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 添加经销商职位时根据角色得到对应的功能
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public void getFunsByRoleIds() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			act.getResponse().setContentType("application/json");
			RequestWrapper request = act.getRequest();
			String roleId = request.getParamValue("roleIds");
			String[] roleids = roleId.split(",");
			
			List<TrRoleFuncPO> list = SysPositionDAO.sysFunsByRoleIdsQuery(roleids);
			act.setOutData("funs", list);
		} catch(BizException e){
			logger.error(logonUser,e);
			act.setException(e);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"经销商职位");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 构造功能树
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
			String rootId = request.getParamValue("tree_root_id");
			
			List<MsgCarrier> vlist = new ArrayList<MsgCarrier>();
			vlist.add(MsgCarrier.getInstance(ValidateCodeConstant.DIGIT_PATTERN,"功能ID",0,Constant.Length_Check_Char_10,rootId));
			Validate.doValidate(act, vlist);
			
			TcFuncPO roleFunctionPO = new TcFuncPO();
			if(rootId != null && !"".equals(rootId)) {
				roleFunctionPO.setParFuncId(new Long(rootId));
			}
			List<TcFuncPO> funList = factory.select(roleFunctionPO);
			act.setOutData("funlist", funList);
		} catch(BizException e){
			logger.error(logonUser,e);
			act.setException(e);
		} catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"经销商职位");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 新增经销商职位页面初始化
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public void addSysPositionInit() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword(addSysPositionInitUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"经销商职位");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public void initDealerDeptTree() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			act.getResponse().setContentType("application/json");
			RequestWrapper request = act.getRequest();
			String rootId = request.getParamValue("tree_root_id");
			
			List<MsgCarrier> vlist = new ArrayList<MsgCarrier>();
			vlist.add(MsgCarrier.getInstance(ValidateCodeConstant.DIGIT_PATTERN,"部门ID",0,Constant.Length_Check_Char_10,rootId));
			Validate.doValidate(act, vlist);
			
			TmOrgPO orgPO = new TmOrgPO();
			orgPO.setCompanyId(new Long(logonUser.getCompanyId()));
			List<TmOrgPO> list = factory.select(orgPO);
			List<TmOrgPO> funList = CommonDAO.createDeptTree(logonUser.getCompanyId(),rootId,logonUser.getUserType(),true);
			act.setOutData("pardId", list.get(0).getOrgId());
			act.setOutData("funlist", funList);
		} catch(BizException e){
			logger.error(logonUser,e);
			act.setException(e);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"经销商职位");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 根据经销商职位类型选择数据权限类型
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public void getDataAuth() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			act.getResponse().setContentType("application/json");
			TcDataAuthPO authPO = new TcDataAuthPO();
			authPO.setDataAuthDesc(String.valueOf(Constant.SYS_USER_DEALER));
			List<TcDataAuthPO> list = factory.select(authPO);
			act.setOutData("dataAuth", list);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"经销商职位");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 查看经销商职位初始化
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public void viewDealerPoseInit() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String poseId = request.getParamValue("poseId"); // 查看的职位ID
			
			List<MsgCarrier> vlist = new ArrayList<MsgCarrier>();
			vlist.add(MsgCarrier.getInstance(ValidateCodeConstant.DIGIT_PATTERN,"职位ID",1,Constant.Length_Check_Char_10,poseId));
			Validate.doValidate(act, vlist);
			
			TcPosePO posePO = new TcPosePO();
			posePO.setPoseId(new Long(poseId));
			List<TcPosePO> poseList = factory.select(posePO); // 通过ID得到职位对象
			posePO = poseList.get(0);
			
			TrPoseBinsPO trPoseBinsPO = new TrPoseBinsPO();
			trPoseBinsPO.setPoseId(poseId);
			String gjPose = "";
			List<TrPoseBinsPO> list = factory.select(trPoseBinsPO);
			if(list != null && list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					gjPose += list.get(i).getBinsCodeId()+",";
				}
			}
			if(!"".equals(gjPose)) {
				gjPose = gjPose.substring(0, gjPose.length() - 1);
			}
			
			act.setOutData("vpose", posePO);
			act.setOutData("gjzw", gjPose);
			act.setOutData("poseId", poseId);
			act.setForword(viewSysRoleInitUrl);
		} catch(BizException e){
			logger.error(logonUser,e);
			act.setException(e);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"经销商职位");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 添加系统职位时根据角色得到关键职位
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public void getGjzwByRoldIds() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			act.getResponse().setContentType("application/json");
			String roleIds = request.getParamValue("roleIds");
			
			List<TrRoleBinsPO> list = SysPositionDAO.gjPoseByRoleIds(roleIds);
			act.setOutData("gjzwlist", list);
		} catch(BizException e){
			logger.error(logonUser,e);
			act.setException(e);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"系统职位");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * init职位
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public void getDealerPoseVal() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			act.getResponse().setContentType("application/json");
			RequestWrapper request = act.getRequest();
			String poseId = request.getParamValue("poseId"); // 查看的职位ID
			
			List<MsgCarrier> vlist = new ArrayList<MsgCarrier>();
			vlist.add(MsgCarrier.getInstance(ValidateCodeConstant.DIGIT_PATTERN,"职位ID",1,Constant.Length_Check_Char_10,poseId));
			Validate.doValidate(act, vlist);
			
			TcPosePO posePO = new TcPosePO();
			posePO.setPoseId(new Long(poseId));
			List<TcPosePO> poseList = factory.select(posePO); // 通过ID得到职位对象
			posePO = poseList.get(0);
			
			TmOrgPO deptPO = new TmOrgPO();
			deptPO.setOrgId(new Long(posePO.getOrgId()));
			List<TmOrgPO> deptList = factory.select(deptPO); // 通过orgid得到部门名称
			deptPO = deptList.get(0);
			
			TrPoseFuncPO trPoseFuncPO = new TrPoseFuncPO();
			trPoseFuncPO.setPoseId(posePO.getPoseId());
			List<TrPoseFuncPO> poseFuncList = factory.select(trPoseFuncPO); // 得到角色对应的功能
			
			TrPoseFuncDataAuthPO trPoseFuncDataAuthPO = new TrPoseFuncDataAuthPO(); // 得到角色对应的功能
			trPoseFuncDataAuthPO.setPoseId(posePO.getPoseId());
			List<TrPoseFuncDataAuthPO> poseFuncDataList = factory.select(trPoseFuncDataAuthPO);
			
			act.setOutData("vorgName", deptPO);
			act.setOutData("poseFuncList", poseFuncList);
			act.setOutData("poseFuncDataList", poseFuncDataList);
		} catch(BizException e){
			logger.error(logonUser,e);
			act.setException(e);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"经销商职位");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 经销商修改职位
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public void sysDealerPoseModfi() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			act.getResponse().setContentType("application/json");
			RequestWrapper request = act.getRequest();
			
			String orgId = request.getParamValue("DEPT_ID"); // 部门ID
			Integer poseType = Constant.SYS_USER_DEALER; // 职位类型
			String poseCode = request.getParamValue("POSE_CODE"); // 职位代码
			String poseName = request.getParamValue("POSE_NAME"); // 职位名称
			String poseStatus = request.getParamValue("POSE_STATUS"); // 职位状态
			String poseId = request.getParamValue("POSE_ID"); // 职位ID
			String funsh = request.getParamValue("FUNSH"); // 功能所对应的数据权限类型
			String myfun = request.getParamValue("MYFUNS"); // 职位对应的功能
			
			String gjyw = request.getParamValue("GG");
			
			List<MsgCarrier> vlist = new ArrayList<MsgCarrier>();
			vlist.add(MsgCarrier.getInstance(ValidateCodeConstant.DIGIT_PATTERN,"部门ID",1,Constant.Length_Check_Char_10,orgId));
			vlist.add(MsgCarrier.getInstance(ValidateCodeConstant.DIGIT_LETTER_PATTERN,"职位代码",1,Constant.Length_Check_Char_60,poseCode));
			vlist.add(MsgCarrier.getInstance(ValidateCodeConstant.DIGIT_LETTER_CN_PATTERN,"职位名称",1,Constant.Length_Check_Char_30,poseName));
			Validate.doValidate(act, vlist);
		
			List<TcPosePO> list = DealerPositionDAO.getDealerPoseByDealerIdOrPoseCode(logonUser.getCompanyId(), poseCode, poseName);
			if(list!=null && list.size()>0) {
				act.setOutData("st", "poseName_error");
				return ;
			}
			
			if(funsh !=null && funsh.length()>0) {
				funsh = funsh.substring(0, funsh.length()-1);
			}
			
			String[] funshs = funsh.split("#");
			
			String[] t1; 
			String[] t2;
			HashMap<String, String[]> hashMap = new HashMap<String, String[]>(); // 将功能权限保存在map中
			for(int i=0; i<funshs.length; i++) { // 保存功能权限
				t1 = funshs[i].split(":"); // 功能ID
				if(t1.length>1) {
					t2 = funshs[i].split(":")[1].split(","); // 功能对应数据权限
					hashMap.put(t1[0], t2);
				}
			}
			
			TcPosePO posePO = new TcPosePO(); // 待保存的职位对象
			posePO.setOrgId(new Long(orgId));
			posePO.setPoseType(Integer.valueOf(poseType));
			posePO.setPoseCode(poseCode);
			posePO.setPoseName(poseName);
			posePO.setPoseStatus(Integer.valueOf(poseStatus));
			if(poseType.equals(Constant.SYS_USER_DEALER)) {
				posePO.setCompanyId(new Long(logonUser.getCompanyId()));
			}
			
			TcPosePO posePO2 = new TcPosePO();
			posePO2.setPoseId(new Long(poseId));
			factory.update(posePO2, posePO);
			
			TrPoseFuncDataAuthPO trPoseFuncDataAuthPO2 = new TrPoseFuncDataAuthPO();
			trPoseFuncDataAuthPO2.setPoseId(new Long(poseId));
			factory.delete(trPoseFuncDataAuthPO2);
			
			TrPoseFuncPO trPoseFuncPO = new TrPoseFuncPO();
			trPoseFuncPO.setPoseId(new Long(poseId));
			factory.delete(trPoseFuncPO);
			
			String[] myfuns = new String[0];
			if(myfun!=null && !"".equals(myfuns)) {
				myfuns = myfun.split(",");
			}
			for(int i=0; i<myfuns.length; i++) { // 保存职位对应的功能
				TrPoseFuncPO poseFuncPO = new TrPoseFuncPO();
				poseFuncPO.setPoseFuncId(factory.getLongPK(new TrPoseFuncPO()));
				poseFuncPO.setFuncId(new Long(myfuns[i]));
				poseFuncPO.setPoseId(new Long(poseId));
				factory.insert(poseFuncPO);
				
				if(hashMap.containsKey(myfuns[i])) { // 该功能设置了数据权限
					String[] ts = hashMap.get(myfuns[i]);
					for(int j=0; j<ts.length; j++) {
						TrPoseFuncDataAuthPO trPoseFuncDataAuthPO = new TrPoseFuncDataAuthPO();
						trPoseFuncDataAuthPO.setPoseFuncDataAuthId(factory.getLongPK(new TrPoseFuncDataAuthPO()));
						trPoseFuncDataAuthPO.setFuncId(new Long(myfuns[i]));
						trPoseFuncDataAuthPO.setPoseId(new Long(poseId));
						trPoseFuncDataAuthPO.setDataAuthId(new Long(ts[j]));
						trPoseFuncDataAuthPO.setPossieFuncId(poseFuncPO.getPoseFuncId());
						factory.insert(trPoseFuncDataAuthPO);
					}
				} else {
					TrPoseFuncDataAuthPO trPoseFuncDataAuthPO = new TrPoseFuncDataAuthPO();
					trPoseFuncDataAuthPO.setPoseFuncDataAuthId(factory.getLongPK(new TrPoseFuncDataAuthPO()));
					trPoseFuncDataAuthPO.setFuncId(new Long(myfuns[i]));
					trPoseFuncDataAuthPO.setPoseId(new Long(poseId));
					trPoseFuncDataAuthPO.setDataAuthId(Constant.DRL_BZZJYX);
					trPoseFuncDataAuthPO.setPossieFuncId(poseFuncPO.getPoseFuncId());
					factory.insert(trPoseFuncDataAuthPO);
				}
			}
			
			TrPoseBinsPO trPoseBinsPO2 = new TrPoseBinsPO();
			trPoseBinsPO2.setPoseId(poseId);
			factory.delete(trPoseBinsPO2);
			
			if(gjyw != null && !"".equals(gjyw)) {
				String[] temp = gjyw.split(",");
				for(int i=0; i<temp.length; i++) {
					TrPoseBinsPO trPoseBinsPO = new TrPoseBinsPO();
					trPoseBinsPO.setPoseBinsId(factory.getStringPK(new TrPoseBinsPO()));
					trPoseBinsPO.setPoseId(poseId);
					trPoseBinsPO.setBinsCodeId(temp[i]);
					factory.insert(trPoseBinsPO);
				}
			}
			
			act.setOutData("st", "succeed");
		} catch(BizException e){
			logger.error(logonUser,e);
			act.setException(e);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"经销商职位");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 查询职位信息
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public void sysPositionQuery() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			act.getResponse().setContentType("application/json");
			RequestWrapper request = act.getRequest();

			if("1".equals(request.getParamValue("COMMAND"))){ // 表格请求处理
				// 职位名称
				String poseName = request.getParamValue("POSE_NAME");
				// 职位代码
				String poseCode = request.getParamValue("POSE_CODE");
				
				List<MsgCarrier> vlist = new ArrayList<MsgCarrier>();
				vlist.add(MsgCarrier.getInstance(ValidateCodeConstant.NOQUOTATION_PATTERN,"职位名称",0,Constant.Length_Check_Char_100,poseName));
				vlist.add(MsgCarrier.getInstance(ValidateCodeConstant.NOQUOTATION_PATTERN,"职位代码",0,Constant.Length_Check_Char_100,poseCode));
				Validate.doValidate(act, vlist);
				
				String orderName = request.getParamValue("orderCol");
				String da = request.getParamValue("order");
				
				Integer curPage = request.getParamValue("curPage") != null ? Integer
						.parseInt(request.getParamValue("curPage"))
						: 1; // 处理当前页
				PageResult<TcPosePO> ps = DealerPositionDAO.sysPoseQuery(poseCode, poseName, logonUser.getOrgId(), Constant.PAGE_SIZE, curPage,orderName,da); // 按条件查询返回职位list  
				act.setOutData("ps", ps);
			}
		} catch(BizException e){
			e.printStackTrace();
			logger.error(logonUser,e);
			act.setException(e);
		} catch (Exception e) {
			e.printStackTrace();
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"经销商职位");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
}
