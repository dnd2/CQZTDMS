
/**********************************************************************
* <pre>
* FILE : SgmOrgMng.java
* CLASS : SgmOrgMng
* 
* AUTHOR : ChenLiang
*
* FUNCTION : SGM部门设定Action.
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

package com.infodms.dms.actions.sysmng.orgmng;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.CompanyBean;
import com.infodms.dms.bean.OrgBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.MD5Util;
import com.infodms.dms.common.MsgCarrier;
import com.infodms.dms.common.ValidateCodeConstant;
import com.infodms.dms.dao.common.CommonDAO;
import com.infodms.dms.dao.orgmng.CompanyInfoDAO;
import com.infodms.dms.dao.orgmng.DealerDeptMngDAO;
import com.infodms.dms.dao.orgmng.SgmOrgMngDAO;
import com.infodms.dms.dao.sysposition.SysPositionDAO;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TcDataAuthPO;
import com.infodms.dms.po.TcPosePO;
import com.infodms.dms.po.TcRolePO;
import com.infodms.dms.po.TcUserPO;
import com.infodms.dms.po.TmCompanyPO;
import com.infodms.dms.po.TmOrgBusinessAreaPO;
import com.infodms.dms.po.TmOrgPO;
import com.infodms.dms.po.TrRoleFuncPO;
import com.infodms.dms.po.TrRolePosePO;
import com.infodms.dms.po.TrUserPosePO;
import com.infodms.dms.util.ActionUtil;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.businessUtil.Validate;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.POFactory;
import com.infoservice.po3.POFactoryBuilder;
import com.infoservice.po3.bean.PageResult;

public class SgmOrgMng {

	public Logger logger = Logger.getLogger(SgmOrgMng.class);
	private POFactory factory = POFactoryBuilder.getInstance();
	private final String sgmOrgMngQueryInitUrl = "/jsp/systemMng/userMng/sgmOrgMngSearch.jsp";
	private final String sgmOrgMngAddInitUrl = "/jsp/systemMng/userMng/sgmOrgMngAdd.jsp";
	private final String modfiSgmOrgMngInitUrl = "/jsp/systemMng/userMng/sgmOrgMngDetail.jsp";
	private final String orgCompanyInitUrl = "/jsp/systemMng/orgMng/orgCompanyInit.jsp";
	private final String orgCompanyModifyUrl = "/jsp/systemMng/orgMng/orgCompanyModify.jsp";
	private final String orgCompanyAddUrl = "/jsp/systemMng/orgMng/orgCompanyAdd.jsp";

	/**
	 * 查询sgm部门页面初始化
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public void sgmOrgMngQueryInit() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword(sgmOrgMngQueryInitUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"SGM部门");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 新增SGM部门初始化
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public void sgmOrgMngAddInit() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword(sgmOrgMngAddInitUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"SGM部门");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 修改SGM部门初始化
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public void modfiSgmOrgMngInit() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String deptId = request.getParamValue("deptId");
			
			List<MsgCarrier> vlist = new ArrayList<MsgCarrier>();
			/*vlist.add(MsgCarrier.getInstance(ValidateCodeConstant.DIGIT_PATTERN,"部门ID",1,Constant.Length_Check_Char_10,deptId));
			Validate.doValidate(act, vlist);*/
			
			TmOrgPO deptPO = new TmOrgPO(); // 待修改的部门对象
			deptPO.setOrgId(new Long(deptId));
			List<TmOrgPO> list = factory.select(deptPO);
			deptPO = list.get(0);
		
			TmOrgPO deptPO2 = new TmOrgPO();
			deptPO2.setOrgId(deptPO.getParentOrgId());
			list = factory.select(deptPO2);
			if(list.size()>0)
			{
				deptPO2 = list.get(0);	
			}
			/**
			 * add by yx 20110105
			 * 添加根据组织ID查询业务范围
			 */
			String orgId = deptPO.getOrgId().toString();
			List<Map<String, Object>> ps = SgmOrgMngDAO.getOrgBusiness(orgId,logonUser.getCompanyId());
			
			act.setOutData("ps", ps);
			
			act.setOutData("orgobj", deptPO);
			act.setOutData("porgname", deptPO2.getOrgName());
			act.setForword(modfiSgmOrgMngInitUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"SGM部门");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 修改SGM部门
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public void modfiSgmOrgMng() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			act.getResponse().setContentType("application/json");
			String deptPid = request.getParamValue("DEPT_ID"); // 上级部门
			String deptCode = request.getParamValue("DEPT_CODE"); // 部门代码
			String deptName = request.getParamValue("DEPT_NNAME"); // 部门名称
			String deptStat = request.getParamValue("DEPT_STAT"); // 部门状态
			String orgType = request.getParamValue("ORG_TYPE"); // 部门类型
			System.out.println("orgType="+orgType);
			String deptDesc = request.getParamValue("DEPT_DESC"); // 部门备注
			Long orgId = new Long(request.getParamValue("D_ID")); // 部门ID
			String thisDeptStat = request.getParamValue("THIS_DEPT_STAT"); // 修改前的状态
			String areaIds=request.getParamValue("AREA_IDS"); // 业务范围Id
			
			List<MsgCarrier> vlist = new ArrayList<MsgCarrier>();
			/*vlist.add(MsgCarrier.getInstance(ValidateCodeConstant.DIGIT_PATTERN,"上级部门",1,Constant.Length_Check_Char_10,deptPid));*/
			vlist.add(MsgCarrier.getInstance(ValidateCodeConstant.DIGIT_LETTER_PATTERN,"部门代码",1,Constant.Length_Check_Char_60,deptCode));
			vlist.add(MsgCarrier.getInstance(ValidateCodeConstant.DIGIT_LETTER_CN_PATTERN,"部门名称",1,Constant.Length_Check_Char_60,deptName));
			Validate.doValidate(act, vlist);
			
			List<TmOrgPO> list1 = SgmOrgMngDAO.getOrgIdByOrgName(logonUser.getCompanyId(), deptName, deptCode, factory);
			if(list1 != null && list1.size() > 0) {
				act.setOutData("st", "deptName_error");
				return ;
			}
			
			if(deptDesc==null) {
				deptDesc = "";
			}
			
			TmOrgPO deptPO = new TmOrgPO();
			deptPO.setOrgId(orgId);
			
			TmOrgPO deptPO2 = new TmOrgPO();
			TmOrgPO deptPO3 = factory.select(deptPO).get(0);
			if(deptPO3.getParentOrgId().toString().equals(deptPid)) 
			{ // 上级部门没有修改
				deptPO2.setParentOrgId(new Long(deptPid));
				deptPO2.setOrgCode(deptCode);
				deptPO2.setOrgName(deptName);
				deptPO2.setOrgType(Constant.ORG_TYPE_OEM);
				deptPO2.setDutyType(Integer.valueOf(orgType));
				deptPO2.setStatus(Integer.valueOf(deptStat));
				deptPO2.setOrgDesc(deptDesc);
				factory.update(deptPO, deptPO2);
			} else { // 上级部门已做修改
				Long ydpid = deptPO3.getParentOrgId(); // 待修改部门的原上级部门
				
				TmOrgPO orgPOLevel = new TmOrgPO(); //取groupLevel等级PO
				List<TmOrgPO> list2 = DealerDeptMngDAO.getOrgLevel(deptPid, factory);
				if(list2!=null&&list2.size()>0)
				{
					orgPOLevel=list2.get(0);	
				}
				Integer orgLevel=new Integer(99);
				if(orgPOLevel!=null)
				{
					orgLevel=	orgPOLevel.getOrgLevel()+1;
				}
				deptPO2.setParentOrgId(new Long(deptPid));
				deptPO2.setOrgCode(deptCode);
				deptPO2.setOrgName(deptName);
				deptPO2.setOrgType(Constant.ORG_TYPE_OEM);
				deptPO2.setDutyType(Integer.valueOf(orgType));
				deptPO2.setStatus(Integer.valueOf(deptStat));
				deptPO2.setOrgLevel(orgLevel);
				deptPO2.setOrgDesc(deptDesc);
				deptPO2.setTreeCode("");
				factory.update(deptPO, deptPO2);
				
				TmOrgPO deptPO4 = new TmOrgPO();
				deptPO4.setParentOrgId(orgId);
				List<TmOrgPO> list = factory.select(deptPO4); // 把原来上级部门
				if(list != null && list.size()>0) { // 如果待修改的部门有下级部门则将其所有下级部门的上级部门改为现在待修改部门的上级部门
					for (int i = 0; i < list.size(); i++) {
						TmOrgPO deptPO5 = new TmOrgPO();
						deptPO5.setOrgId(list.get(i).getOrgId());
						
						TmOrgPO deptPO6 = list.get(i);
						deptPO6.setParentOrgId(ydpid);
						deptPO6.setOrgLevel(orgLevel+1);
						deptPO6.setTreeCode("");
						factory.update(deptPO5, deptPO6);
					}
				}
				
				
			}
			
			if(!thisDeptStat.equals(deptStat)) { // 用户修改了部门的状态
				String sjbm = getXjbm(orgId,logonUser.getCompanyId());
				String[] sjbms = sjbm.split(",");
				for (int i = 0; i < sjbms.length; i++) {
					TmOrgPO deptPO4 = new TmOrgPO();
					TmOrgPO deptPO5 = new TmOrgPO();
					deptPO4.setOrgId(new Long(sjbms[i]));
					deptPO5.setStatus(Integer.valueOf(deptStat));
					factory.update(deptPO4, deptPO5);
				}
			}
			 //删除原业务范围
			TmOrgBusinessAreaPO tp=new TmOrgBusinessAreaPO();
			tp.setOrgId(orgId);
			factory.delete(tp);
			//插入新的业务范围
			if(!"".equals(areaIds)&&areaIds!=null)
			{
				String[] area = areaIds.split(",");
				for (int i = 0; i < area.length; i++) 
				{
					TmOrgBusinessAreaPO tp1=new TmOrgBusinessAreaPO();
					Long areaId=Long.valueOf(area[i]);
					String relationId=SequenceManager.getSequence("");
					tp1.setOrgId(orgId);
					tp1.setRelationId(new Long(relationId));
					tp1.setAreaId(areaId);
					tp1.setCreateBy(logonUser.getUserId());
					tp1.setCreateDate(new Date());
					factory.insert(tp1);
				}
			}
			
			act.setOutData("st", "succeed");
		} catch(BizException e){
			logger.error(logonUser,e);
			act.setException(e);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"SGM部门");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 得到用户当前部门及所有下级部门
	 * @param deptid
	 * @param companyId
	 * @return
	 */
	private String getXjbm(Long orgId,Long companyId) {
		String bm = orgId+",";
		List<TmOrgPO> list = new ArrayList<TmOrgPO>();
		List<TmOrgPO> zDept = CommonDAO.getXjbm(orgId,companyId, list);
		if(zDept != null && zDept.size() > 0) {
			for (int i = 0; i < zDept.size(); i++) {
				bm += zDept.get(i).getOrgId()+",";
			}
		}
		return bm.substring(0, bm.length()-1);
	}
	
	/**
	 * 添加SGM部门
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public void addSgmOrg() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			act.getResponse().setContentType("application/json");
			String deptPid = request.getParamValue("DEPT_ID"); // 上级部门
			String deptCode = request.getParamValue("DEPT_CODE"); // 部门代码
			String deptName = request.getParamValue("DEPT_NNAME"); // 部门名称
			String deptStat = request.getParamValue("DEPT_STAT"); // 部门状态
			String orgType = request.getParamValue("ORG_TYPE"); // 部门类型
			String deptDesc = request.getParamValue("DEPT_DESC"); // 部门备注
			
			List<MsgCarrier> vlist = new ArrayList<MsgCarrier>();
			// 暂时去掉数据长度校验
			/*vlist.add(MsgCarrier.getInstance(ValidateCodeConstant.DIGIT_PATTERN,"上级部门",1,Constant.Length_Check_Char_10,deptPid));*/
			vlist.add(MsgCarrier.getInstance(ValidateCodeConstant.DIGIT_LETTER_PATTERN,"部门代码",1,Constant.Length_Check_Char_60,deptCode));
			vlist.add(MsgCarrier.getInstance(ValidateCodeConstant.DIGIT_LETTER_CN_PATTERN,"部门名称",1,Constant.Length_Check_Char_60,deptName));
			Validate.doValidate(act, vlist);
			
			if(deptDesc==null) {
				deptDesc = "";
			}
			
			List<TmOrgPO> list = DealerDeptMngDAO.isDeptCode(logonUser.getCompanyId(), deptCode, factory);
			if(list!=null && list.size()>0) {
				act.setOutData("st", "deptCode_error");
				act.setOutData("msg", "部门代码不能重复");
				return ;
			}
			
			List<TmOrgPO> list1 = DealerDeptMngDAO.isDeptName(logonUser.getCompanyId(), deptName, factory);
			if(list1!=null && list1.size()>0) {
				act.setOutData("st", "deptName_error");
				act.setOutData("msg", "部门名称不能重复");
				return ;
			}
			TmOrgPO orgPO1 = new TmOrgPO(); //取groupLevel等级PO
			List<TmOrgPO> list2 = DealerDeptMngDAO.getOrgLevel(deptPid, factory);
			if(list2!=null&&list2.size()>0)
			{
				orgPO1=list2.get(0);	
			}
			TmOrgPO orgPO = new TmOrgPO(); // 保存部门信息到部门表
			String orgId=SequenceManager.getSequence("");
			String treeCode=DealerDeptMngDAO.getOrgTreeCode(deptPid);
			orgPO.setOrgId(new Long(orgId));
			orgPO.setParentOrgId(new Long(deptPid));
			orgPO.setCompanyId(logonUser.getCompanyId());
			orgPO.setOrgCode(deptCode);
			orgPO.setOrgName(deptName);
			orgPO.setStatus(Integer.valueOf(deptStat));
			orgPO.setOrgDesc(deptDesc);
			orgPO.setDutyType(Integer.valueOf(orgType));
			orgPO.setOrgType(Integer.valueOf(Constant.ORG_TYPE_OEM));
			orgPO.setTreeCode(treeCode);
			if(orgPO1!=null)
			{
				orgPO.setOrgLevel(orgPO1.getOrgLevel()+1);	
			}
			
			factory.insert(orgPO);
			
//			TrCompanyDeptPO companyDeptPO = new TrCompanyDeptPO(); // 将SGM和部门关联起来
//			companyDeptPO.setCompanyDeptId(factory.getStringPK(new TrCompanyDeptPO()));
//			companyDeptPO.setCompanyId(logonUser.getCompanyId());
//			companyDeptPO.setDeptId(String.valueOf(deptPO2.getOrgId()));
//			factory.insert(companyDeptPO);
			
			act.setOutData("st", "succeed");
			
		} catch(BizException e){
			logger.error(logonUser,e);
			act.setException(e);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"SGM部门");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 添加系统职位时根据角色得到对应的功能
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public void getFunsByRoleIds() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			act.getResponse().setContentType("application/json");
			String roleId = request.getParamValue("roleIds");
			String[] roleids = roleId.split(",");
			
			List<TrRoleFuncPO> list = SysPositionDAO.sysFunsByRoleIdsQuery(roleids);
			act.setOutData("funs", list);
		} catch(BizException e){
			logger.error(logonUser,e);
			act.setException(e);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"SGM部门");
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
			
			List<TmOrgPO> funList = CommonDAO.createDeptTree(logonUser.getCompanyId(), rootId,logonUser.getUserType(),false);
			act.setOutData("funlist", funList);
		} catch(BizException e){
			logger.error(logonUser,e);
			act.setException(e);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"SGM部门");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 根据职位类型选择数据权限类型
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public void getDataAuth() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			act.getResponse().setContentType("application/json");
			String type = request.getParamValue("type"); // 职位类型
			TcDataAuthPO authPO = new TcDataAuthPO();
			authPO.setDataAuthDesc(type);
			List<TcDataAuthPO> list = factory.select(authPO);
			act.setOutData("dataAuth",list);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"SGM部门");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 查询SGM部门
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public void sgmOrgMngQuery() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			act.getResponse().setContentType("application/json");
			if("1".equals(request.getParamValue("COMMAND"))){ // 表格请求处理
				// 上级部门
				String porgId = request.getParamValue("DEPT_ID");
				String deptLevel = CommonUtils.checkNull(request.getParamValue("deptLevel"));
				
				List<MsgCarrier> vlist = new ArrayList<MsgCarrier>();
				//Constant.Length_Check_Char_10 原来的校验，但常量只设置10位 17位，没有16位。请开发人员自定义
				vlist.add(MsgCarrier.getInstance(ValidateCodeConstant.DIGIT_PATTERN,"部门ID",0,16,porgId));
				Validate.doValidate(act, vlist);
				
				String orderName = request.getParamValue("orderCol");
				String da = request.getParamValue("order");
				
				Integer curPage = request.getParamValue("curPage") != null ? Integer
						.parseInt(request.getParamValue("curPage"))
						: 1; // 处理当前页
				
				PageResult<OrgBean> ps = SgmOrgMngDAO.sgmOrgMngQuery(porgId,logonUser.getCompanyId(), ActionUtil.getPageSize(request), curPage,orderName,da,deptLevel);
				
				act.setOutData("ps", ps);
				ActionUtil.setCustomPageSizeFlag(act, true);
			}
		} catch(BizException e){
			logger.error(logonUser,e);
			act.setException(e);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"SGM部门");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	public void getOrgBusiness()
	{
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			act.getResponse().setContentType("application/json");
			String orgId = request.getParamValue("D_ID");	
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页
			
			List<Map<String, Object>> ps = SgmOrgMngDAO.getOrgBusiness(orgId,logonUser.getCompanyId());
			act.setOutData("ps", ps);
		/*} catch(BizException e){
			logger.error(logonUser,e);
			act.setException(e);*/
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"SGM部门");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 车厂公司维护初始化页面
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public void orgCompanyInit() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword(orgCompanyInitUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"SGM部门");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	public void queryOemCompanyInfo()
	{
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			act.getResponse().setContentType("application/json");
			String companyCode=request.getParamValue("COMPANY_CODE");
			String companyName=request.getParamValue("COMPANY_NAME");
			Long companyId=logonUser.getCompanyId();
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页
			
			PageResult<Map<String, Object>> ps = SgmOrgMngDAO.getOrgCompany(companyCode,companyName,companyId, Constant.PAGE_SIZE, curPage);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"SGM部门");
			logger.error(logonUser,e1);
			act.setException(e1);
		}	
	}
	//修改明细页面
	public void getOemCompanyInfo()
	{
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			act.getResponse().setContentType("application/json");
			String companyId=request.getParamValue("companyId");
			CompanyBean companyBean = new CompanyBean();
			List<CompanyBean> companyList = CompanyInfoDAO.getCompanyInfoItem(Long.valueOf(companyId));
			if(companyList.size()>0)
			companyBean = companyList.get(0);
			act.setOutData("COMPANY_BEAN", companyBean);
			act.setForword(orgCompanyModifyUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"SGM部门");
			logger.error(logonUser,e1);
			act.setException(e1);
		}	
	}
	public void updateOemCompanyInfo()
	{
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			act.getResponse().setContentType("application/json");
			String companyId=CommonUtils.checkNull(request.getParamValue("COMPANY_ID"));//公司ID
			String companyCode=CommonUtils.checkNull(request.getParamValue("COMPANY_CODE"));//公司代码
			String companyName=CommonUtils.checkNull(request.getParamValue("COMPANY_NAME"));//公司名称
			String companyShortname=CommonUtils.checkNull(request.getParamValue("COMPANY_SHORTNAME"));//公司简称
			String provinceId=CommonUtils.checkNull(request.getParamValue("PROVINCE_ID"));//省ID
			String cityId=CommonUtils.checkNull(request.getParamValue("CITY_ID"));//城市ID
			String phone=CommonUtils.checkNull(request.getParamValue("PHONE"));//电话
			String zipCode=CommonUtils.checkNull(request.getParamValue("ZIP_CODE"));//邮编	
			String fax=CommonUtils.checkNull(request.getParamValue("FAX"));//传真
			String address=CommonUtils.checkNull(request.getParamValue("ADDRESS"));//地址
			String status=CommonUtils.checkNull(request.getParamValue("STATUS"));//状态
			String companyType=Constant.COMPANY_TYPE_SGM;
			Date currTime = new Date(System.currentTimeMillis());
			Long userId=logonUser.getUserId();
			TmCompanyPO po=new TmCompanyPO();
			TmCompanyPO contionPo=new TmCompanyPO();
			contionPo.setCompanyId(Long.valueOf(companyId));
			po.setAddress(address);
			if(!"".equals(cityId))
			{
				po.setCityId(Long.valueOf(cityId));	
			}
			if(!"".equals(provinceId))
			{
				po.setProvinceId(Long.valueOf(provinceId));	
			}
			po.setCompanyCode(companyCode);
			po.setCompanyName(companyName);
			po.setCompanyShortname(companyShortname);
			po.setCompanyType(new Integer(companyType));
			po.setPhone(phone);
			po.setUpdateBy(userId);
			po.setUpdateDate(currTime);
			po.setFax(fax);
			po.setZipCode(zipCode);
			po.setStatus(new Integer(status));
			factory.update(contionPo, po);
			act.setOutData("ACTION_RESULT", 1);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"SGM部门");
			logger.error(logonUser,e1);
			act.setException(e1);
		}	
	}
	/**
	 * 车厂公司维护新增页面
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public void addNewOemCompany() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword(orgCompanyAddUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"SGM部门");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 车厂公司维护新增页面
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public void addOemCompanyInfo() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			act.getResponse().setContentType("application/json");
			
			String companyCode=CommonUtils.checkNull(request.getParamValue("COMPANY_CODE"));//公司代码
			String companyName=CommonUtils.checkNull(request.getParamValue("COMPANY_NAME"));//公司名称
			String companyShortname=CommonUtils.checkNull(request.getParamValue("COMPANY_SHORTNAME"));//公司简称
			String provinceId=CommonUtils.checkNull(request.getParamValue("PROVINCE_ID"));//省ID
			String cityId=CommonUtils.checkNull(request.getParamValue("CITY_ID"));//城市ID
			String phone=CommonUtils.checkNull(request.getParamValue("PHONE"));//电话
			String zipCode=CommonUtils.checkNull(request.getParamValue("ZIP_CODE"));//邮编	
			String fax=CommonUtils.checkNull(request.getParamValue("FAX"));//传真
			String address=CommonUtils.checkNull(request.getParamValue("ADDRESS"));//地址
			String status=CommonUtils.checkNull(request.getParamValue("STATUS"));//状态
			String orgCode=CommonUtils.checkNull(request.getParamValue("ORG_CODE"));//组织代码
			String orgName=CommonUtils.checkNull(request.getParamValue("ORG_NAME"));//组织名称
			String userName=CommonUtils.checkNull(request.getParamValue("USER_NAME"));//用户名
			String passWord=CommonUtils.checkNull(request.getParamValue("PASSWORD"));//密码
			String companyType=Constant.COMPANY_TYPE_SGM;
			String companyId=SequenceManager.getSequence("");
			Date currTime = new Date(System.currentTimeMillis());
			Long userId=logonUser.getUserId();
			TmCompanyPO po=new TmCompanyPO();
			//插入公司
			Long oemCompanyId=logonUser.getCompanyId();
			po.setCompanyId(Long.valueOf(companyId));
//			po.setAddress(address);
//			if(!"".equals(cityId))
//			{
//				po.setCityId(Long.valueOf(cityId));	
//			}
//			if(!"".equals(provinceId))
//			{
//				po.setProvinceId(Long.valueOf(provinceId));	
//			}
			po.setCompanyCode(companyCode);
			po.setCompanyName(companyName);
			po.setCompanyShortname(companyShortname);
			po.setCompanyType(new Integer(companyType));
			po.setPhone(phone);
			po.setCreateBy(userId);
			po.setCreateDate(currTime);
			po.setFax(fax);
//			po.setZipCode(zipCode);
			po.setStatus(new Integer(status));
			po.setOemCompanyId(Long.valueOf(companyId));
			factory.insert(po);
			//插入组织
			Long orgId=Long.valueOf(SequenceManager.getSequence(""));
			SgmOrgMngDAO dao=new SgmOrgMngDAO();
			String treeCode="";
			Map<String, Object> map=dao.getOrgTreeCode();
			if(map!=null)
			{
				treeCode=String.valueOf(map.get("NEW_TREECODE"));
			}
			TmOrgPO orgpo=new TmOrgPO();
			orgpo.setCompanyId(Long.valueOf(companyId));
			orgpo.setOrgName(orgName);
			orgpo.setCreateBy(userId);
			orgpo.setCreateDate(currTime);
			orgpo.setDutyType(Constant.DUTY_TYPE_COMPANY);
			orgpo.setOrgCode(orgCode);
			orgpo.setOrgId(orgId);
			orgpo.setOrgLevel(new Integer(1));
			orgpo.setOrgType(Constant.ORG_TYPE_OEM);
			orgpo.setParentOrgId(new Long(-1));
			orgpo.setStatus(Constant.STATUS_ENABLE);
			orgpo.setTreeCode(treeCode);
			factory.insert(orgpo);
			
			//插入用户
			/*TcUserPO userpo=new TcUserPO();
			Long newUserId=Long.valueOf(SequenceManager.getSequence(""));
			if(!"".equals(userName))
			{
				userName=userName.toUpperCase();
			}
			userpo.setAcnt(userName);
			userpo.setCompanyId(Long.valueOf(companyId));
			userpo.setCreateBy(userId);
			userpo.setCreateDate(currTime);
			userpo.setName(userName);
			userpo.setUserId(newUserId);
			userpo.setUserStatus(Constant.STATUS_ENABLE);
			//userpo.setPersonCode(userName);
			if(!"".equals(passWord))
			{
				userpo.setPassword(MD5Util.MD5Encryption(passWord));	
			}
			factory.insert(userpo);*/
			
			/**
			 * edit by wangsw 20140218 
			 * there's no need to add user
			 * 
			// 插入角色,职位及其对应关系
			List<Map<String, Object>> list=dao.getroleList(companyId);
			String poseId="";
			String roleId="";
			if(list.size()>0)
			{
				
				Map<String, Object> map1=list.get(0);
			    roleId=String.valueOf(map1.get("ROLE_ID"));
				List<Map<String, Object>> poseList=dao.getposeList(roleId);
				if(poseList.size()>0)
				{
					Map<String, Object> map2=poseList.get(0);
					poseId=String.valueOf(map2.get("POSE_ID"));	
				}else
				{
				poseId=insetPoseAndRelation(companyId,userId,orgId,currTime,roleId);	
				}
				
			}else
			{
			roleId=SequenceManager.getSequence("");
			TcRolePO tcpo=new TcRolePO();
			tcpo.setCreateBy(userId);
			tcpo.setCreateDate(currTime);
			tcpo.setRoleDesc("系统自动创建的角色");
			tcpo.setRoleId(Long.valueOf(roleId));
			tcpo.setRoleName("超级用户");
			tcpo.setRoleStatus(Constant.STATUS_ENABLE);
			tcpo.setRoleType(Constant.SYS_USER_SGM);
			tcpo.setOemCompanyId(Long.valueOf(companyId));
			factory.insert(tcpo);
			List<Map<String, Object>> funcList=dao.getfuncList();
				if(funcList.size()>0)
				{
					for(int i=0;i<funcList.size();i++)
					{
						TrRoleFuncPO trfpo=new TrRoleFuncPO();
						Map<String, Object> map3=funcList.get(i);
						String funcId=String.valueOf(map3.get("FUNC_ID"));
						trfpo.setCreateBy(userId);
						trfpo.setCreateDate(currTime);
						trfpo.setFuncId(Long.valueOf(funcId));
						trfpo.setRoleFuncId(Long.valueOf(SequenceManager.getSequence("")));
						trfpo.setRoleId(Long.valueOf(roleId));
						factory.insert(trfpo);
					}
				}
				poseId=insetPoseAndRelation(companyId,userId,orgId,currTime,roleId);	
			}
			//插入用户与职位关联表
			TrUserPosePO tuppo=new TrUserPosePO();
			tuppo.setCreateBy(userId);
			tuppo.setCreateDate(currTime);
			tuppo.setPoseId(Long.valueOf(poseId));
			tuppo.setUserId(newUserId);
			tuppo.setUserPoseId(Long.valueOf(SequenceManager.getSequence("")));
			factory.insert(tuppo);
			//插入businessPara
			dao.insertBusinessPara(userId, oemCompanyId, companyId);
			//插入variablePara
			dao.insertVariablePara(userId, oemCompanyId, companyId);
			*/
			
			act.setOutData("ACTION_RESULT", 1);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"SGM部门");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	public  String insetPoseAndRelation(String companyId,Long userId,Long orgId,Date currTime,String roleId)
	{
		//插入职位
		String poseId=SequenceManager.getSequence("");
		TcPosePO tcpo=new TcPosePO();
		tcpo.setCompanyId(Long.valueOf(companyId));
		tcpo.setCreateBy(userId);
		tcpo.setCreateDate(currTime);
		tcpo.setOrgId(orgId);
		tcpo.setPoseBusType(Constant.POSE_BUS_TYPE_SYS);
		tcpo.setPoseCode("SUPER_POSE");
		tcpo.setPoseName("超级用户");
		tcpo.setPoseStatus(Constant.STATUS_ENABLE);
		tcpo.setPoseType(Constant.SYS_USER_SGM);
		tcpo.setRemark("系统自动创建的职位");
		tcpo.setPoseId(Long.valueOf(poseId));
		factory.insert(tcpo);
		//插入职位与角色对应关系
		TrRolePosePO trrpPo=new TrRolePosePO();
		trrpPo.setCreateBy(userId);
		trrpPo.setCreateDate(currTime);
		trrpPo.setPoseId(Long.valueOf(poseId));
		trrpPo.setRoleId(Long.valueOf(roleId));
		trrpPo.setRolePoseId(Long.valueOf(SequenceManager.getSequence("")));
		factory.insert(trrpPo);
		return poseId;
	}
}
