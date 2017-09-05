/**********************************************************************
 * <pre>
 *  FILE : ActionSysRole.java
 *  CLASS : ActionSysRole
 *  
 *  AUTHOR : ChenLiang
 * 
 *  FUNCTION : 系统角色维护的Action.
 * 
 * 
 * ======================================================================
 *  CHANGE HISTORY LOG
 * ----------------------------------------------------------------------
 *  MOD. NO.| DATE     |    NAME    | REASON | CHANGE REQ.
 * ----------------------------------------------------------------------
 *          |2009-08-03| ChenLiang  | Created |
 *  DESCRIPTION:
 * </pre>
 ***********************************************************************/

package com.infodms.dms.actions.sysmng.sysrole;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.MsgCarrier;
import com.infodms.dms.common.ValidateCodeConstant;
import com.infodms.dms.dao.common.CommonDAO;
import com.infodms.dms.dao.sysrole.SysRoleDAO;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TcFuncPO;
import com.infodms.dms.po.TcFuncPO2;
import com.infodms.dms.po.TcRolePO;
import com.infodms.dms.po.TrRoleBinsPO;
import com.infodms.dms.po.TrRoleFuncPO;
import com.infodms.dms.util.ActionUtil;
import com.infodms.dms.util.businessUtil.Validate;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.POFactory;
import com.infoservice.po3.POFactoryBuilder;
import com.infoservice.po3.bean.PageResult;

import edu.emory.mathcs.backport.java.util.LinkedList;

public class ActionSysRole {
	
	public Logger logger = Logger.getLogger(ActionSysRole.class);
	private POFactory factory = POFactoryBuilder.getInstance();
	private final String querySysRoleInitUrl = "/jsp/systemMng/userMng/sysRoleSearch.jsp";    // 角色查询页面URL
	private final String addSysRoleInitUrl = "/jsp/systemMng/userMng/sysRoleSearchAdd.jsp";   // 添加角色页面URL
	private final String viewSysRoleInit = "/jsp/systemMng/userMng/sysRoleNameDetail.jsp";    // 修改角色页面URL
	
	/**
	 * 系统角色维护页面初始化
	 * 
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public void querySysRoleInit()
	{
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword(querySysRoleInitUrl);
		}
		catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "系统角色维护查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 新增系统角色页面初始化
	 * 
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public void addSysRoleInit()
	{
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword(addSysRoleInitUrl);
		}
		catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "系统角色维护新增");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 构造功能的树形菜单
	 * 
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public void initFunTree()
	{
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			act.getResponse().setContentType("application/json");
			String rootId = request.getParamValue("tree_root_id"); // 得到树的根节点ID
			String roleType = request.getParamValue("ROLE_TYPE"); // 角色类别 
			
			List<MsgCarrier> vlist = new ArrayList<MsgCarrier>();
			vlist.add(MsgCarrier.getInstance(ValidateCodeConstant.DIGIT_PATTERN, "功能ID", 0,
							Constant.Length_Check_Char_10, rootId));
			Validate.doValidate(act, vlist);
			
			List<TcFuncPO> funList = CommonDAO.getFunc(rootId, roleType); // 查询树的叶子节点
			List<TcFuncPO2> tmp = new LinkedList();
			for (int i = 0; i < funList.size(); i++)
			{
				TcFuncPO po1 = funList.get(i);
				TcFuncPO2 po2 = new TcFuncPO2();
				po2.setFuncCode(po1.getFuncCode());
				po2.setFuncId(po1.getFuncId().toString());
				po2.setFuncName(po1.getFuncName());
//				po2.setFuncType(po1.getFuncType().toString());
				po2.setParFuncId(po1.getParFuncId().toString());
				tmp.add(po2);
			}
			act.setOutData("funlist", tmp);
		}
		catch (BizException e) {
			logger.error(logonUser, e);
			act.setException(e);
		}
		catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "系统角色维护功能树初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 查看系统角色页面
	 * 
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public void viewSysRoleInit()
	{
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			Long roleId = new Long(request.getParamValue("roleId")); // 查看的角色ID
			
//			List<MsgCarrier> vlist = new ArrayList<MsgCarrier>();
//			vlist.add(MsgCarrier.getInstance(ValidateCodeConstant.DIGIT_PATTERN,"角色ID",1,Constant.Length_Check_Char_10,String.valueOf(roleId)));
//			Validate.doValidate(act, vlist);
			
			TcRolePO rolePO = new TcRolePO();
			rolePO.setRoleId(roleId);
			List<TcRolePO> list = factory.select(rolePO); // 通过ID得到角色对象
			rolePO = list.get(0);
			
			String funls = "";
			TrRoleFuncPO roleFunctionPO = new TrRoleFuncPO();
			roleFunctionPO.setRoleId(roleId);
			List<TrRoleFuncPO> funList = factory.select(roleFunctionPO); // 查询查询角色的所有功能
			if (funList != null && funList.size() > 0) {
				for (int i = 0; i < funList.size(); i++) {
					funls += funList.get(i).getFuncId() + ",";
				}
			}
			
			if (!"".equals(funls)) {
				funls = funls.substring(0, funls.length() - 1);
			}
			
			String gjzw = "";
//			if(rolePO.getRoleType().equals(Constant.SYS_USER_DEALER)) {
//				TrRoleBinsPO trRoleBinsPO = new TrRoleBinsPO();
//				trRoleBinsPO.setRoleId(new Long(roleId));
//				List<TrRoleBinsPO> gjzwList = factory.select(trRoleBinsPO); // 得到角色关键职位
//				if(gjzwList!=null &&  gjzwList.size()>0) {
//					for (int i = 0; i < gjzwList.size(); i++) {
//						gjzw += gjzwList.get(i).getBinsCodeId() + ",";
//					}
//				}
//				if(!"".equals(gjzw)) {
//					gjzw = gjzw.substring(0, gjzw.length()-1);
//				}
//			}
			
			act.setOutData("rolePO", rolePO);
			act.setOutData("gjzw", gjzw);
			act.setOutData("funList", funls);
			act.setForword(viewSysRoleInit);
		}
		catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "系统角色维护功能树初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 查看系统角色得到对应功能
	 * 
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public void viewSysRole()
	{
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			Long roleId = new Long(request.getParamValue("roleId")); // 查看的角色ID
			
			List<MsgCarrier> list = new ArrayList<MsgCarrier>();
			list.add(MsgCarrier.getInstance(ValidateCodeConstant.DIGIT_PATTERN, "角色ID", 1,
							Constant.Length_Check_Char_10, String.valueOf(roleId)));
			Validate.doValidate(act, list);
			
			TrRoleFuncPO roleFunctionPO = new TrRoleFuncPO();
			roleFunctionPO.setRoleId(roleId);
			List<TrRoleFuncPO> funList = factory.select(roleFunctionPO); // 得到角色对应的功能
			
			act.setOutData("funList", funList);
			act.setForword(viewSysRoleInit);
		}
		catch (BizException e) {
			logger.error(logonUser, e);
			act.setException(e);
		}
		catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "系统角色维护得到功能");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 添加角色
	 * 
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public void sysRoleAdd()
	{
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			act.getResponse().setContentType("application/json");
			// 用户选择的功能列表
			String funList = request.getParamValue("FUNS");
			// 角色名称
			String roleDesc = request.getParamValue("ROLE_DESC");
			// 角色代码
			String roleName = request.getParamValue("ROLE_NAME");
			// 角色状态
			String roleStatus = request.getParamValue("ROLE_STATUS");
			// 角色类别
			String roleType = request.getParamValue("ROLE_TYPE");
			
			List<MsgCarrier> list = new ArrayList<MsgCarrier>();
			list.add(MsgCarrier.getInstance(ValidateCodeConstant.DIGIT_LETTER_PATTERN, "角色代码", 1, Constant.Length_Check_Char_30, roleName));
			list.add(MsgCarrier.getInstance(ValidateCodeConstant.DIGIT_LETTER_CN_PATTERN, "角色名称", 1, Constant.Length_Check_Char_30, roleDesc));
			Validate.doValidate(act, list);
			
			TcRolePO rolePO1 = new TcRolePO();
			rolePO1.setRoleDesc(roleDesc);
			List<TcRolePO> list1 = factory.select(rolePO1);
			if (list1 != null && list1.size() > 0) {
				act.setOutData("st", "roleDesc_error");
				return;
			}
			
			TcRolePO rolePO2 = new TcRolePO();
			rolePO2.setRoleName(roleName);
			List<TcRolePO> list2 = factory.select(rolePO2);
			if (list2 != null && list2.size() > 0) {
				act.setOutData("st", "roleName_error");
				return;
			}
			
			TcRolePO rolePO = new TcRolePO();
			rolePO.setCreateBy(null);
			rolePO.setRoleId(factory.getLongPK(new TcRolePO()));
			rolePO.setRoleDesc(roleDesc);
			rolePO.setRoleName(roleName);
			rolePO.setRoleStatus(Integer.valueOf(roleStatus));
			rolePO.setRoleType(Integer.valueOf(roleType));
			rolePO.setOemCompanyId(logonUser.getCompanyId());
			rolePO.setCreateBy(logonUser.getUserId());
			rolePO.setCreateDate(new Date());
			rolePO.setUpdateBy(logonUser.getUserId());
			rolePO.setUpdateDate(new Date());
			
			factory.insert(rolePO); // 保存角色对象
			
			/*
			 * 角色类型为经销商时插入该数据，不明用途，暂时屏蔽 by wangsw
			 * 
			String gjyw = request.getParamValue("GG");
			if (roleType.equals(Constant.SYS_USER_DEALER)) {
				if (gjyw != null && !"".equals(gjyw)) {
					String[] temp = gjyw.split(",");
					for (int i = 0; i < temp.length; i++) {
						TrRoleBinsPO trRoleBinsPO = new TrRoleBinsPO();
						trRoleBinsPO.setRoleBinsId(factory.getLongPK(new TrRoleBinsPO()));
						trRoleBinsPO.setRoleId(new Long(rolePO.getRoleId()));
						trRoleBinsPO.setBinsCodeId(new Long(temp[i]));
						factory.insert(trRoleBinsPO);
					}
				}
			}
			*/
			
			String[] funs = new String[0];
			if (funList != null && !"".equals(funList)) {
				funs = funList.split(",");
			}
			for (int i = 0; i < funs.length; i++) { // 保存角色对应的功能
				TrRoleFuncPO roleFunctionPO = new TrRoleFuncPO();
				roleFunctionPO.setCreateBy(null);
				roleFunctionPO.setFuncId(new Long(funs[i]));
				roleFunctionPO.setRoleId(rolePO.getRoleId());
				roleFunctionPO.setRoleFuncId(Long.valueOf(SequenceManager.getSequence(null)));
				factory.insert(roleFunctionPO);
			}
			
			act.setOutData("st", "succeed");
		}
		catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.ADD_FAILURE_CODE, "系统角色维护添加角色");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 修改角色
	 * 
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public void sysRolemodfi()
	{
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			act.getResponse().setContentType("application/json");
			// 用户选择的功能列表
			String funList = request.getParamValue("FUNS");
			// 角色名称
			String roleDesc = request.getParamValue("ROLE_DESC");
			// 角色代码
			String roleName = request.getParamValue("ROLE_NAME");
			// 角色状态
			String roleStatus = request.getParamValue("ROLE_STATUS");
			// 角色类别
			String roleType = request.getParamValue("ROLE_TYPE");
			// 角色ID
			Long roleId = new Long(request.getParamValue("ROLE_ID"));
			//companyId
			Long companyId = logonUser.getCompanyId();
			String gjyw = request.getParamValue("GG");
			
			List<MsgCarrier> vlist = new ArrayList<MsgCarrier>();
			vlist.add(MsgCarrier.getInstance(ValidateCodeConstant.DIGIT_LETTER_CN_PATTERN, "角色名称", 1,
							Constant.Length_Check_Char_30, roleDesc));
			Validate.doValidate(act, vlist);
			
			List<TcRolePO> list = SysRoleDAO.getRoleByRoleIdOrRoleDesc(roleDesc, roleId, Integer.valueOf(roleType),
							companyId);
			if (list != null && list.size() > 0) {
				act.setOutData("st", "roleDesc_error");
				return;
			}
			
			TcRolePO rolePO = new TcRolePO();
			TcRolePO rolePO2 = new TcRolePO();
			rolePO2.setRoleId(roleId);
			rolePO.setCreateBy(null);
			rolePO.setRoleDesc(roleDesc);
			rolePO.setRoleName(roleName);
			rolePO.setRoleStatus(Integer.valueOf(roleStatus));
			rolePO.setRoleType(Integer.valueOf(roleType));
			rolePO.setUpdateBy(logonUser.getUserId());
			rolePO.setUpdateDate(new Date());
			factory.update(rolePO2, rolePO); // 修改角色信息
			TrRoleFuncPO funcPO = new TrRoleFuncPO();
			funcPO.setRoleId(roleId);
			factory.delete(funcPO); // 修改角色对应的功能时先删除其所有功能再添加
			
			if (roleType.equals(Constant.SYS_USER_DEALER)) {
				TrRoleBinsPO trRoleBinsPO2 = new TrRoleBinsPO();
				trRoleBinsPO2.setRoleId(new Long(roleId));
				factory.delete(trRoleBinsPO2);
			}
			
			String[] funs = new String[0];
			if (funList != null && !"".equals(funList)) {
				funs = funList.split(",");
			}
			
			for (int i = 0; i < funs.length; i++) { // 插入角色对应的功能
				TrRoleFuncPO roleFunctionPO = new TrRoleFuncPO();
				roleFunctionPO.setCreateBy(null);
				roleFunctionPO.setFuncId(new Long(funs[i]));
				roleFunctionPO.setRoleId(rolePO2.getRoleId());
				roleFunctionPO.setRoleFuncId(Long.valueOf(SequenceManager.getSequence(null)));
				factory.insert(roleFunctionPO);
			}
			
			if (gjyw != null && !"".equals(gjyw) && roleType.equals(Constant.SYS_USER_DEALER)) {
				String[] temp = gjyw.split(",");
				for (int i = 0; i < temp.length; i++) {
					TrRoleBinsPO trRoleBinsPO = new TrRoleBinsPO();
					trRoleBinsPO.setRoleBinsId(factory.getLongPK(new TrRoleBinsPO()));
					trRoleBinsPO.setRoleId(new Long(rolePO2.getRoleId()));
					trRoleBinsPO.setBinsCodeId(new Long(temp[i]));
					factory.insert(trRoleBinsPO);
				}
			}
			act.setOutData("st", "succeed");
		}
		catch (BizException e) {
			logger.error(logonUser, e);
			act.setException(e);
		}
		catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "系统角色维护修改角色");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 查询角色信息
	 * 
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public void sysRoleQuery()
	{
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			act.getResponse().setContentType("application/json");
			if ("1".equals(request.getParamValue("COMMAND"))) { // 表格请求处理
				// 角色代码
				String roleName = request.getParamValue("ROLE_NAME");
				// 角色名称
				String roleDesc = request.getParamValue("ROLE_DESC");
				// 角色类型
				String roleType = request.getParamValue("ROLE_TYPE");
				
				List<MsgCarrier> list = new ArrayList<MsgCarrier>();
				list.add(MsgCarrier.getInstance(ValidateCodeConstant.NOQUOTATION_PATTERN, "角色代码", 0,
								Constant.Length_Check_Char_100, roleName));
				list.add(MsgCarrier.getInstance(ValidateCodeConstant.NOQUOTATION_PATTERN, "角色名称", 0,
								Constant.Length_Check_Char_100, roleDesc));
				list.add(MsgCarrier.getInstance(ValidateCodeConstant.NOQUOTATION_PATTERN, "角色类型", 0,
								Constant.Length_Check_Char_100, roleType));
				Validate.doValidate(act, list);
				Long companyId = logonUser.getCompanyId();
				String orderName = request.getParamValue("orderCol");
				String da = request.getParamValue("order");
				
				/*********add zhumingwei 2014-2-17************控制sa用户和不是sa用户登录进来时候是否可以修改sa账户的权限***************************/
				Long userId = logonUser.getUserId();
				Long poseId = logonUser.getPoseId();
				/*********add zhumingwei 2014-2-17************控制sa用户和不是sa用户登录进来时候是否可以修改sa账户的权限*********************/
				
				Integer curPage = request.getParamValue("curPage") != null ? Integer
								.parseInt(request.getParamValue("curPage"))
								: 1; // 处理当前页
				PageResult<Map<String, Object>> ps = SysRoleDAO.sysRoleQuery(roleDesc, roleName, roleType, companyId,
								ActionUtil.getPageSize(request), curPage, orderName, da,userId,poseId); // 按条件查询返回角色list  
				act.setOutData("ps", ps);
				// 调整列宽功能 by chenyub@yonyou.com
				ActionUtil.setResizeColumnWidthFlag(act, true);
				// 表格列排序功能 by chenyub@yonyou.com
				ActionUtil.setTableSortFlag(act, true);
				// 表格交换列功能 by chenyub@yonyou.com
				ActionUtil.setSwapColumnFlag(act, true);
				// 自定义每页大小 by chenyub@yonyou.com
				ActionUtil.setCustomPageSizeFlag(act, true);
			}
		}
		catch (BizException e) {
			logger.error(logonUser, e);
			act.setException(e);
		}
		catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "系统角色维护查询角色信息");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
}
