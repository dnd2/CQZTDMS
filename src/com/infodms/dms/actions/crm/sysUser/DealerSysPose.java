package com.infodms.dms.actions.crm.sysUser;

/**********************************************************************
* <pre>
* FILE : SysPosition.java
* CLASS : SysPosition
* 
* AUTHOR : ChenLiang
*
* FUNCTION : 系统职位维护Action.
*
*
*======================================================================
* CHANGE HISTORY LOG
*----------------------------------------------------------------------
* MOD. NO.| DATE     |    NAME    | REASON | CHANGE REQ.
*----------------------------------------------------------------------
*         |2009-08-23| ChenLiang  | Created |
* DESCRIPTION:
* </pre>
***********************************************************************/


import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.MsgCarrier;
import com.infodms.dms.common.ValidateCodeConstant;
import com.infodms.dms.dao.common.CommonDAO;
import com.infodms.dms.dao.crm.sysUser.DealerSysPoseDao;
import com.infodms.dms.dao.sales.customerInfoManage.SalesReportDAO;
import com.infodms.dms.dao.sales.dealer.DealerInfoDao;
import com.infodms.dms.dao.sysposition.SysPositionDAO;
import com.infodms.dms.dao.usermng.UserMngDAO;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TcDataAuthPO;
import com.infodms.dms.po.TcFuncPO;
import com.infodms.dms.po.TcFuncPO2;
import com.infodms.dms.po.TcPosePO;
import com.infodms.dms.po.TcRolePO;
import com.infodms.dms.po.TmBusinessAreaPO;
import com.infodms.dms.po.TmCompanyPO;
import com.infodms.dms.po.TmDealerPO;
import com.infodms.dms.po.TmOrgPO;
import com.infodms.dms.po.TmPoseBusinessAreaPO;
import com.infodms.dms.po.TrPoseBinsPO;
import com.infodms.dms.po.TrRoleBinsPO;
import com.infodms.dms.po.TrRoleFuncPO;
import com.infodms.dms.po.TrRolePosePO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.businessUtil.Validate;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.POFactory;
import com.infoservice.po3.POFactoryBuilder;
import com.infoservice.po3.bean.PageResult;

public class DealerSysPose {

	public Logger logger = Logger.getLogger(DealerSysPose.class);
	private POFactory factory = POFactoryBuilder.getInstance();
	private DealerSysPoseDao dao=DealerSysPoseDao.getInstance();
	private final String querySysPositionInitUrl = "/jsp/crm/sysUser/dealerSysPoseInit.jsp";
	private final String addSysPositionInitUrl = "/jsp/crm/sysUser/dealerSysPoseAdd.jsp";
	private final String addSysPositionRoleInit = "/jsp/systemMng/userMng/sysPoseRoleSearch.jsp";
	private final String viewSysRoleInitUrl = "/jsp/crm/sysUser/dealerSysPoseModify.jsp";
	private final String toPoseListURL = "/jsp/crm/sysUser/poseList.jsp";
	/**
	 * 系统职位维护页面初始化
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public void doInit() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String poseName = request.getParamValue("POSE_NAME");
			//yin 获取pose_type
			String pose_type="";
			if("10431005".equals(logonUser.getDutyType().toString())){
				pose_type="10021002";
			}else{
				pose_type="10021001";
			}
//			TmDealerPO td =new TmDealerPO();
//			td.setDealerId(new Long(logonUser.getDealerId()));
//			td=factory.select(td).get(0);
//			act.setOutData("dealer", td);
			act.setOutData("DEALER_ID", logonUser.getDealerId());
			act.setOutData("dutyType", logonUser.getDutyType());
			act.setOutData("POSE_TYPE", pose_type);
			//end
			act.setOutData("POSE_NAME", poseName);
			
		
			act.setForword(querySysPositionInitUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"系统职位");
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
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"系统职位");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 添加系统职位时选择角色
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public void addSysPositionRoleSerch() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			act.getResponse().setContentType("application/json");
			String rolep = request.getParamValue("rolep");
			String ROLE_DESC = request.getParamValue("ROLE_DESC");
			Long companyId=logonUser.getCompanyId();
			//yin
			TmCompanyPO tm=new TmCompanyPO();
			tm.setCompanyId(new Long(companyId));
			tm=factory.select(tm).get(0);
			companyId=new Long(tm.getOemCompanyId());
			//end
			
			List<TcRolePO> ps = SysPositionDAO.sysRoleQuery(rolep, ROLE_DESC,companyId); // 按条件查询返回角色list  
			act.setOutData("ps", ps);
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
	 * 添加职位验证
	 *//*
	public void addPositionValide(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		String poseName=request.getParamValue("POSE_NAME");
		TcPosePO tcPosePO=new TcPosePO();
		tcPosePO.setPoseName(poseName);
		if(factory.select(tcPosePO).size()>0){
			act.setOutData("poseCode",1);
		}else{
			act.setOutData("poseCode",0);
		}
	}
	*//**
	 * 修改 职位验证
	 *//*
	public void modifyPositionValide(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		String poseName=request.getParamValue("POSE_NAME");
		TcPosePO tcPosePO=new TcPosePO();
		tcPosePO.setPoseName(poseName);
		String poseCode=factory.select(tcPosePO).get(0).getPoseCode();//判断名字相同的职位是否存在
		if(poseCode!=null && poseCode!=""){
			act.setOutData("poseCode",1);
		}else{
			act.setOutData("poseCode",0);
		}
	}*/
	
	/**
	 * 添加系统职位
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public void addPosition() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			act.getResponse().setContentType("application/json");
			String orgId = request.getParamValue("DEPT_ID"); // 部门ID
			String dealerId = request.getParamValue("DEALER_ID"); // 经销商ID
			String poseType = request.getParamValue("POSE_TYPE"); // 职位类型
			String poseCode = request.getParamValue("POSE_CODE"); // 职位代码
			String poseName = request.getParamValue("POSE_NAME"); // 职位名称
			String poseStatus = request.getParamValue("POSE_STATUS"); // 职位状态
			String funsh = request.getParamValue("FUNSH"); // 功能所对应的数据权限类型
			String myfun = request.getParamValue("MYFUNS"); // 职位对应的功能
			String poseBusType = request.getParamValue("POSE_BUS_TYPE"); //职位类型
			String[] roleIds = request.getParamValues("ROLE_ID"); // 职位对应的功能
			String poseRank= request.getParamValue("POSE_RANK"); //职位级别
			String parPoseId=request.getParamValue("par_pose_id");//上级职位
			
			String gjyw = request.getParamValue("GG");
		
			/*List<MsgCarrier> vlist = new ArrayList<MsgCarrier>();
			vlist.add(MsgCarrier.getInstance(ValidateCodeConstant.DIGIT_LETTER_PATTERN,"职位代码",1,Constant.Length_Check_Char_60,poseCode));
			vlist.add(MsgCarrier.getInstance(ValidateCodeConstant.DIGIT_LETTER_CN_PATTERN,"职位名称",1,Constant.Length_Check_Char_30,poseName));
			Validate.doValidate(act, vlist);*/
			Long dealerOrgId = null;
			Long companyId = null;
			if(poseType.equals(String.valueOf(Constant.SYS_USER_DEALER)))
			{
				String tmp=logonUser.getCompanyId().toString();
				if(null==tmp||"".equals(tmp)){
					throw new BizException(act,ErrorCodeConstant.QUERY_FAILURE_CODE,"系统职位");
				}
				companyId=new Long(tmp);
				TmOrgPO orgPo=new TmOrgPO();
				orgPo.setCompanyId(companyId);
				dealerOrgId=factory.select(orgPo).get(0).getOrgId();
				/**
				 * modified by andy.ten@tom.com
				 */
				/*TmDealerPO dealerPO = new TmDealerPO();
				dealerPO.setDealerId(new Long(dealerId));
				companyId = factory.select(dealerPO).get(0).getCompanyId();
				TmDealerOrgRelationPO relPO = new TmDealerOrgRelationPO();
				relPO.setDealerId(new Long(dealerId));
				dealerOrgId = factory.select(dealerPO).get(0).getDealerOrgId();*/
				// end
			}
			TcPosePO posePO3 = new TcPosePO();
			posePO3.setPoseCode(poseCode);
			if(poseType.equals(String.valueOf(Constant.SYS_USER_SGM))) {
				posePO3.setCompanyId(logonUser.getCompanyId());
			} else {
				posePO3.setCompanyId(companyId);
			}
			List<TcPosePO> list = factory.select(posePO3);
			if(list!=null && list.size()>0) {
				act.setOutData("st", "poseCode_error");
				return ;
			}
			
			TcPosePO posePO4 = new TcPosePO();
			posePO4.setPoseName(poseName);
			if(poseType.equals(String.valueOf(Constant.SYS_USER_SGM))) {
				posePO4.setCompanyId(logonUser.getCompanyId());
			} else {
				posePO4.setCompanyId(companyId);
			}
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
			Long poseId = factory.getLongPK(new TcPosePO());
			posePO.setPoseId(poseId);

			posePO.setPoseType(Integer.valueOf(poseType));
			posePO.setPoseCode(poseCode);
			posePO.setPoseName(poseName);
			posePO.setPoseStatus(Integer.valueOf(poseStatus));
			posePO.setPoseBusType(Integer.valueOf(poseBusType));
			posePO.setParPoseId(parPoseId==null?null:new Long(parPoseId));
			posePO.setPoseRank(poseRank==null?null:new Long(poseRank));
			//新增创建人与创建时间
			posePO.setCreateBy(logonUser.getUserId());
			posePO.setCreateDate(new Date());
		//	posePO.setUpdateBy(logonUser.getUserId());
		//	posePO.setUpdateDate(new Date());
			
			if(poseType.equals(String.valueOf(Constant.SYS_USER_SGM))) { // 创建SGM职位
				posePO.setCompanyId(logonUser.getCompanyId());
				posePO.setOrgId(new Long(orgId));

				//modify by xiayanpeng begin 职位业务范围，应取对应表
				//posePO.setOemPositionArea(brandId);
				//modify by xiayanpeng end
				
			} else { // 创建经销商职位
				posePO.setCompanyId(companyId);
				posePO.setOrgId(dealerOrgId);	
			}
			factory.insert(posePO);
			//modify by xiayanpeng begin 功能，经销商和车厂职位都含有业务范围
			String brandId = "";          //品牌ID
			String brandList[] = null;
			Enumeration enu = request.getParamNames();
			while(enu.hasMoreElements())
			{
				String pot = (String) enu.nextElement();
				if(pot.startsWith("BRAND")){
					brandList = request.getParamValues(pot);
				}
			}
			if(brandList != null && brandList.length > 0)
			{
				
				for(int i = 0;i < brandList.length;i++)
				{
					//modify by xiayanpeng BEGIN 根据在职位维护中选择的业务范围，存入职位和业务范围关系表中tm_pose_business_area
					TmPoseBusinessAreaPO areaPO = new TmPoseBusinessAreaPO();
					areaPO.setRelationId(factory.getLongPK(new TmPoseBusinessAreaPO()));
					areaPO.setPoseId(poseId);
					areaPO.setAreaId(new Long(brandList[i]));
					areaPO.setCreateDate(new Date());
					areaPO.setCreateBy(logonUser.getUserId());
					factory.insert(areaPO);
					//modify by xiayanpeng end 
					//modify by xiayanpeng BEGIN 删除原有逻辑
//					if(brandList[i] != null && !"".equals(brandList[i]) )
//					{
//						if(i < brandList.length - 1)
//						{
//							brandId = brandId+brandList[i] + ",";
//						}else
//						{
//							brandId = brandId+brandList[i];
//						}
//					}
					//modify by xiayanpeng end 
				}
			}else
			{
				brandId = "";
			}
			//modify by xiayapeng end
			String[] myfuns = new String[0];
			if(myfun!=null && !"".equals(myfuns)) {
				myfuns = myfun.split(",");
			}
			
			
			for(int i=0; i<roleIds.length; i++) { // 保存职位对应的功能
				/**
				 * added by andy.ten@tom.com 向 tr_role_pose表中保存数据
				 */
				TrRolePosePO poseRolePO = new TrRolePosePO();
				poseRolePO.setRolePoseId(factory.getLongPK(new TrRolePosePO()));
				poseRolePO.setRoleId(new Long(roleIds[i]));
				poseRolePO.setPoseId(new Long(poseId));
				factory.insert(poseRolePO);
				// end
//				
//				TrPoseFuncPO poseFuncPO = new TrPoseFuncPO();
//				poseFuncPO.setPoseFuncId(factory.getLongPK(new TrPoseFuncPO()));
//				poseFuncPO.setFuncId(new Long(myfuns[i]));
//				poseFuncPO.setPoseId(posePO.getPoseId());
//				factory.insert(poseFuncPO);
				//modify by xiayanpeng begin 删除TrPoseFuncDataAuthPO表功能
//				if(hashMap.containsKey(myfuns[i])) { // 该功能设置了数据权限
//					String[] ts = hashMap.get(myfuns[i]);
//					for(int j=0; j<ts.length; j++) {
//						TrPoseFuncDataAuthPO trPoseFuncDataAuthPO = new TrPoseFuncDataAuthPO();
//						trPoseFuncDataAuthPO.setPoseFuncDataAuthId(factory.getLongPK(new TrPoseFuncDataAuthPO()));
//						trPoseFuncDataAuthPO.setFuncId(new Long(myfuns[i]));
//						trPoseFuncDataAuthPO.setPoseId(posePO.getPoseId());
//						trPoseFuncDataAuthPO.setDataAuthId(new Long(ts[j]));
//						trPoseFuncDataAuthPO.setPossieFuncId(poseFuncPO.getPoseFuncId());
//						factory.insert(trPoseFuncDataAuthPO);
//					}
//				} else {
//					Long tety; //根据职位类型赋予默认的数据权限
//					if(poseType.equals(String.valueOf(Constant.SYS_USER_SGM))) {
//						tety = Constant.SGM_BZZJYX;
//					} else {
//						tety = Constant.DRL_BZZJYX;
//					}
//					TrPoseFuncDataAuthPO trPoseFuncDataAuthPO = new TrPoseFuncDataAuthPO();
//					trPoseFuncDataAuthPO.setPoseFuncDataAuthId(factory.getLongPK(new TrPoseFuncDataAuthPO()));
//					trPoseFuncDataAuthPO.setFuncId(new Long(myfuns[i]));
//					trPoseFuncDataAuthPO.setPoseId(posePO.getPoseId());
//					trPoseFuncDataAuthPO.setDataAuthId(tety);
//					trPoseFuncDataAuthPO.setPossieFuncId(poseFuncPO.getPoseFuncId());
//					factory.insert(trPoseFuncDataAuthPO);
//				}
				//modify by xiayanpeng end 删除TrPoseFuncDataAuthPO表功能
			}
			
			if(gjyw != null && !"".equals(gjyw) && poseType.equals(String.valueOf(Constant.SYS_USER_DEALER))) {
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
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"系统职位");
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
			
//			String pose_id = request.getParamValue("POSE_ID");
//			System.out.println(pose_id);
//			if (null != pose_id && !"".equals(pose_id)) {
//				for (int i = 0; i < roleids.length; i++) {
//					TrRolePosePO posePO = new TrRolePosePO();
//					posePO.setRolePoseId(Long.parseLong(SequenceManager.getSequence("")));
//					posePO.setPoseId(Long.parseLong(pose_id));
//					posePO.setRoleId(Long.parseLong(roleids[i]));
//					factory.insert(posePO);
//				}
//			}
			
			List<TrRoleFuncPO> list = SysPositionDAO.sysFunsByRoleIdsQuery(roleids);
			act.setOutData("funs", list);
			//added by andy.ten@tom.com
			List<TcRolePO> roelList = SysPositionDAO.sysRolesByRoleIdsQuery(roleids);
//          List<TcRolePO> roelList = SysPositionDAO.sysRolesByRoleIdsQueryByPoseId(pose_id);
			act.setOutData("roelList", roelList);
			//end
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
	 * 新增系统职位页面初始化
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public void addSysPositionInit() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			//modify by xiayanpeng begin 删除TmBrandPO表
			//List<TmBrandPO> brandList = CommonUtils.findAllBrands();
			//act.setOutData("brandList", brandList);
			//modify by xiayanpeng end
			//modify by xiayanpeng begin TmBusinessAreaPO表中获取业务范围 为了
			Long companyId=logonUser.getCompanyId();
			List<TmBusinessAreaPO> brandList = CommonUtils.findAllBusinessAreaPO(companyId);
			//yin
			String pose_type="";
			if("10431005".equals(logonUser.getDutyType().toString())){
				pose_type="10021002";
			}else{
				pose_type="10021001";
			}
//			TmDealerPO td =new TmDealerPO();
//			td.setDealerId(new Long(logonUser.getDealerId()));
//			td=factory.select(td).get(0);
//			act.setOutData("dealer", td);
			act.setOutData("DEALER_ID", logonUser.getDealerId());
			act.setOutData("dutyType", logonUser.getDutyType());
			act.setOutData("POSE_TYPE", pose_type);
			//end
			act.setOutData("brandList", brandList);	
			//modify by xiayanpeng end
			act.setForword(addSysPositionInitUrl);
		}  catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"系统职位");
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
			String type = request.getParamValue("type");
			
			List<MsgCarrier> vlist = new ArrayList<MsgCarrier>();
			vlist.add(MsgCarrier.getInstance(ValidateCodeConstant.DIGIT_PATTERN,"职位类型ID",1,Constant.Length_Check_Char_10,type));
			Validate.doValidate(act, vlist);
			
			TcDataAuthPO authPO = new TcDataAuthPO();
			authPO.setDataAuthDesc(type);
			List<TcDataAuthPO> list = factory.select(authPO);
			act.setOutData("dataAuth",list);
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
	 * 
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public void initConmpanyDeptTree() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			act.getResponse().setContentType("application/json");
			RequestWrapper request = act.getRequest();
			String rootId = request.getParamValue("tree_root_id");
			String type = request.getParamValue("POSE_TYPE");
			String dealerID = request.getParamValue("DEALER_ID");
			
			List<MsgCarrier> vlist = new ArrayList<MsgCarrier>();
			vlist.add(MsgCarrier.getInstance(ValidateCodeConstant.DIGIT_PATTERN,"部门ID",0,Constant.Length_Check_Char_10,rootId));
			Validate.doValidate(act, vlist);
			
			TmOrgPO orgPO = new TmOrgPO();
			//companyPO.setCompanyType(Constant.COMPANY_TYPE_DEALER);
			List<TmOrgPO> funList = null;
			if(type.equals(Constant.SYS_USER_SGM)) {
				funList = CommonDAO.createDeptTree(logonUser.getCompanyId(), rootId,Integer.valueOf(type),true);
				orgPO.setCompanyId(logonUser.getCompanyId());
			} else {
				funList = CommonDAO.createDeptTree(new Long(dealerID), rootId,null,true);
				orgPO.setCompanyId(new Long(dealerID));
			}
			List<TmOrgPO> list = factory.select(orgPO);
			act.setOutData("pardId", list.get(0).getOrgId());
			act.setOutData("funlist", funList);
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
			List<TcFuncPO2> tmp = new LinkedList();
			for(int i = 0 ; i < funList.size() ; i ++)
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
		} catch(BizException e){
			logger.error(logonUser,e);
			act.setException(e);
		} catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"系统职位");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 查看系统职位页面
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public void viewSysRoleInit() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String poseId = request.getParamValue("poseId"); // 查看的职位ID
			TcPosePO posePO = new TcPosePO();
			posePO.setPoseId(new Long(poseId));
			List<TcPosePO> poseList = factory.select(posePO); // 通过ID得到职位对象
			posePO = poseList.get(0);
			
			String gjPose = "";
			//modify by xiayanpeng begin 修改去职位业务范围方式
			//String poseArea = posePO.getOemPositionArea();
			//modify by xiayanpeng end
			//moidfy by xiayanpeng begin 取位置业务范围
			Long companyId=logonUser.getCompanyId();
			TmPoseBusinessAreaPO areaPO = new TmPoseBusinessAreaPO();
			List<TmBusinessAreaPO> areaList = SysPositionDAO.findPoseArea(poseId);
			act.setOutData("relList", areaList);
			//modify by xiayanpeng end 
			if(posePO.getPoseType().equals(Constant.SYS_USER_DEALER)) {
//				TrPoseBinsPO trPoseBinsPO = new TrPoseBinsPO();
//				trPoseBinsPO.setPoseId(poseId);
//				
//				List<TrPoseBinsPO> list = factory.select(trPoseBinsPO);
//				if(list != null && list.size() > 0) {
//					for (int i = 0; i < list.size(); i++) {
//						gjPose += list.get(i).getBinsCodeId()+",";
//					}
//				}
//				if(!"".equals(gjPose)) {
//					gjPose = gjPose.substring(0, gjPose.length() - 1);
//				}
				/**
				 * modified by andy.ten@tom.com
				 */
//				TmDealerOrgRelationPO relPO = new TmDealerOrgRelationPO();
//				relPO.setOrgId(posePO.getOrgId());
//				relPO = factory.select(relPO).get(0);
//				TmDealerPO delPO = new TmDealerPO();
//				delPO.setDealerId(relPO.getDealerId());
//				delPO = factory.select(delPO).get(0);
				TmCompanyPO companyPo=new TmCompanyPO();
				companyPo.setCompanyId(posePO.getCompanyId());
				TmCompanyPO companyValPo=factory.select(companyPo).get(0);
				act.setOutData("COMPANY_NAME", companyValPo.getCompanyName());
				act.setOutData("COMPANY_ID", companyValPo.getCompanyId());
				/*TmDealerPO delPO = new TmDealerPO();
				delPO.setDealerOrgId(posePO.getOrgId());
				delPO = factory.select(delPO).get(0);
				//modify by xiayapeng begin tm_dealer表修改 
				act.setOutData("dealerName", delPO.getDealerName());
				//modify by xiayanpeng end 
				act.setOutData("dealerId", delPO.getDealerId());*/
				
			}else
			{

				//modify by xiayapeng begin 删除原取业务范围方式
//				if(poseArea != null && !"".equals(poseArea))
//				{
//					List<TmBrandPO> relList = new LinkedList<TmBrandPO>();
//					String[] brandIds = poseArea.split(",");
//					for(int i = 0 ; i < brandIds.length ; i ++ )
//					{
//						TmBrandPO brandPO = new TmBrandPO();
//						brandPO.setBrandId(new Long(brandIds[i]));
//						relList.add(brandPO);
//					}
//					act.setOutData("relList", relList);
//				}
				//modify by xiayanpeng end
			}
			
			TmOrgPO orgPO = new TmOrgPO();
			orgPO.setOrgId(posePO.getOrgId());
			orgPO = factory.select(orgPO).get(0);
			//modify by xiayapeng begin 删除原取全部业务范围方式
			//List<TmBrandPO> brandList = CommonUtils.findAllBrands();
			//modify by xiayanpeng end
			//modify by xiayapeng begin 加入取全部业务范围方式
			List<TmBusinessAreaPO> brandList = CommonUtils.findAllBusinessAreaPO(companyId);
			if(posePO.getParPoseId()!=null&&!"0".equals(posePO.getParPoseId().toString())){
				TcPosePO tp=new TcPosePO();
				tp.setPoseId(posePO.getParPoseId());
				tp=(TcPosePO) dao.select(tp).get(0);
				act.setOutData("par_pose_name", tp.getPoseName());
			}else{
				act.setOutData("par_pose_name", "");
			}
			act.setOutData("brandList", brandList);
			act.setOutData("vpose", posePO);
			act.setOutData("gjzw", gjPose);
			act.setOutData("orgName", orgPO.getOrgName());
			act.setOutData("poseId", poseId);
			
			act.setForword(viewSysRoleInitUrl);
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
	public void getSysPoseVal() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			act.getResponse().setContentType("application/json");
			RequestWrapper request = act.getRequest();
			String poseId = request.getParamValue("poseId"); // 查看的职位ID
			
//			List<MsgCarrier> vlist = new ArrayList<MsgCarrier>();
//			vlist.add(MsgCarrier.getInstance(ValidateCodeConstant.DIGIT_PATTERN,"职位ID",1,Constant.Length_Check_Char_10,poseId));
//			Validate.doValidate(act, vlist);
//			
			TcPosePO posePO = new TcPosePO();
			posePO.setPoseId(new Long(poseId));
			List<TcPosePO> poseList = factory.select(posePO); // 通过ID得到职位对象
			posePO = poseList.get(0);
			
			TmOrgPO orgPO = new TmOrgPO();
			orgPO.setOrgId(posePO.getOrgId());
			List<TmOrgPO> deptList = factory.select(orgPO); // 通过orgid得到部门名称
			orgPO = deptList.get(0);
			
//			TrPoseFuncPO trPoseFuncPO = new TrPoseFuncPO();
//			trPoseFuncPO.setPoseId(posePO.getPoseId());
//			List<TrPoseFuncPO> poseFuncList = factory.select(trPoseFuncPO); // 得到角色对应的功能
			
//			
			//modify by xiayapeng begin 删除TrPoseFuncDataAuthPO逻辑
//			TrPoseFuncDataAuthPO trPoseFuncDataAuthPO = new TrPoseFuncDataAuthPO(); // 得到角色对应的功能
//			trPoseFuncDataAuthPO.setPoseId(posePO.getPoseId());
//			List<TrPoseFuncDataAuthPO> poseFuncDataList = factory.select(trPoseFuncDataAuthPO);
//			act.setOutData("poseFuncDataList", poseFuncDataList);
			//modify by xiayanpeng end 
			
			//added by andy.ten@tom.com
			List<TcRolePO> roleList = SysPositionDAO.sysRolesByPoseIdQuery(poseId);
			act.setOutData("roleList", roleList);
			List<TrRoleFuncPO> roleFuncList = SysPositionDAO.sysFuncByPoseIdQuery(poseId); // 得到角色对应的功能
			act.setOutData("roleFuncList", roleFuncList);
			//end
			
			act.setOutData("vorgName", orgPO);
			//act.setOutData("poseFuncDataList", poseFuncList);
			
			
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
	 * 修改职位
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public void sysRolemodfi() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			act.getResponse().setContentType("application/json");
			String deptId = request.getParamValue("DEPT_ID"); // 部门ID
			String dealerId = request.getParamValue("DEALER_ID"); // 公司ID
			String poseType = request.getParamValue("POSE_TYPE"); // 职位类别
			String poseBusType = request.getParamValue("POSE_BUS_TYPE"); // 职位类别
			String poseCode = request.getParamValue("POSE_CODE"); // 职位代码
			String poseName = request.getParamValue("POSE_NAME"); // 职位名称
			String poseStatus = request.getParamValue("POSE_STATUS"); // 职位状态
			String poseId = request.getParamValue("POSE_ID"); // 职位ID
			String funsh = request.getParamValue("FUNSH"); // 功能所对应的数据权限类型
			//String myfun = request.getParamValue("MYFUNS"); // 职位对应的功能
			String poseRank= request.getParamValue("POSE_RANK"); //职位级别
			String parPoseId=request.getParamValue("par_pose_id");//上级职位
			
			String gjyw = request.getParamValue("GG");
			
			String[] roleIds = request.getParamValues("ROLE_ID"); // 职位对应的功能
			
			/*List<MsgCarrier> vlist = new ArrayList<MsgCarrier>();
			vlist.add(MsgCarrier.getInstance(ValidateCodeConstant.DIGIT_LETTER_PATTERN,"职位代码",1,Constant.Length_Check_Char_60,poseCode));
			vlist.add(MsgCarrier.getInstance(ValidateCodeConstant.DIGIT_LETTER_CN_PATTERN,"职位名称",1,Constant.Length_Check_Char_30,poseName));
			Validate.doValidate(act, vlist);*/
			Long dealerOrgId = null;
			Long companyId = null;
			if(poseType.equals(String.valueOf(Constant.SYS_USER_DEALER)))
			{
				companyId = Long.parseLong(request.getParamValue("COMPANY_ID")) ;
				
				TmDealerPO dealerPO = new TmDealerPO();
				dealerPO.setCompanyId(companyId) ;
				companyId = factory.select(dealerPO).get(0).getCompanyId();
				//TmDealerOrgRelationPO relPO = new TmDealerOrgRelationPO();
				//relPO.setDealerId(new Long(dealerId));
				dealerOrgId = factory.select(dealerPO).get(0).getDealerOrgId();
			}
			TcPosePO posePO4 = new TcPosePO();
			posePO4.setPoseName(poseName);
			if(poseType.equals(String.valueOf(Constant.SYS_USER_SGM))) {
				posePO4.setCompanyId(logonUser.getCompanyId());
			} else {
				posePO4.setCompanyId(companyId);
			}
			List<TcPosePO> list2 = factory.select(posePO4);
			posePO4.setPoseId(new Long(poseId));
			List<TcPosePO> list3 = factory.select(posePO4);
			if(list2!=null && list2.size()>0 && (list3 == null || list3.size() < 1)) {
				act.setOutData("st", "该职位名称已存在,请更改职位名称!");
				return ;
			}
			String[] funshs = null;
			
			if(funsh !=null && funsh.length()>0) {
				funsh = funsh.substring(0, funsh.length()-1);
				funshs = funsh.split("#");
			}
			
			String[] t1; 
			String[] t2;
			HashMap<String, String[]> hashMap = new HashMap<String, String[]>(); // 将功能权限保存在map中
			if(funshs != null) {
				for(int i=0; i<funshs.length; i++) { // 保存功能权限
					t1 = funshs[i].split(":"); // 功能ID
					if(t1.length>1) {
						t2 = funshs[i].split(":")[1].split(","); // 功能对应数据权限
						hashMap.put(t1[0], t2);
					}
				}
			}
			 
			TcPosePO posePO = new TcPosePO(); // 待保存的职位对象
			
			posePO.setPoseType(Integer.valueOf(poseType));
			posePO.setPoseCode(poseCode);
			posePO.setPoseName(poseName);
			posePO.setUpdateBy(logonUser.getUserId());
			posePO.setUpdateDate(new Date());
			posePO.setPoseBusType(Integer.valueOf(poseBusType));
			posePO.setPoseStatus(Integer.valueOf(poseStatus));
			posePO.setCompanyId(logonUser.getCompanyId());
			posePO.setParPoseId(parPoseId==null?null:new Long(parPoseId));
			posePO.setPoseRank(poseRank==null?null:new Long(poseRank));
			if(poseType.equals(String.valueOf(Constant.SYS_USER_SGM))) { // 创建SGM职位
				posePO.setOrgId(new Long(deptId));
				posePO.setCompanyId(logonUser.getCompanyId());
				//modify by xiayanpeng BEGIN 删除原有逻辑
				//posePO.setOemPositionArea(brandId);
				//modify by xiayanpeng end 
					
			} else { // 创建经销商职位
				
				posePO.setCompanyId(companyId);
				posePO.setOrgId(dealerOrgId);
			}
			TcPosePO posePO2 = new TcPosePO();
			posePO2.setPoseId(new Long(poseId));
			factory.update(posePO2, posePO);
			
			//modify by xiayanpeng begin 经销商职位也维护业务范围
			String brandId = "";          //品牌ID
			String brandList[] = null;
			Enumeration enu = request.getParamNames();
			while(enu.hasMoreElements())
			{
				String pot = (String) enu.nextElement();
				if(pot.startsWith("BRAND")){
					brandList = request.getParamValues(pot);
				}
			}
			if(brandList != null && brandList.length > 0)
			{
				//modify by xiayanpeng BEGIN 根据职位ID删除职位与业务范围关系表
				TmPoseBusinessAreaPO areaDelPO = new TmPoseBusinessAreaPO();
				areaDelPO.setPoseId(new Long(poseId));
				factory.delete(areaDelPO);
				//modify by xiayanpeng end 
				for(int i = 0;i < brandList.length;i++)
				{
					//modify by xiayanpeng BEGIN 根据在职位维护中选择的业务范围，存入职位和业务范围关系表中tm_pose_business_area
					TmPoseBusinessAreaPO areaPO = new TmPoseBusinessAreaPO();
					areaPO.setRelationId(factory.getLongPK(new TmPoseBusinessAreaPO()));
					areaPO.setPoseId(new Long(poseId));
					areaPO.setAreaId(new Long(brandList[i]));
					areaPO.setCreateDate(new Date());
					areaPO.setCreateBy(logonUser.getUserId());
					factory.insert(areaPO);
					//modify by xiayanpeng end 
					//modify by xiayanpeng BEGIN 删除原有逻辑
//					if(brandList[i] != null && !"".equals(brandList[i]) )
//					{
//						if(i < brandList.length - 1)
//						{
//							brandId = brandId+brandList[i] + ",";
//						}else
//						{
//							brandId = brandId+brandList[i];
//						}
//					}
					//modify by xiayanpeng end 
				}
			}else
			{
				brandId = "";
			}
			//modify by xiayapeng end 
			
			//modified by andy.ten@tom.com 
//			TrPoseFuncDataAuthPO trPoseFuncDataAuthPO2 = new TrPoseFuncDataAuthPO();
//			trPoseFuncDataAuthPO2.setPoseId(new Long(poseId));
//			factory.delete(trPoseFuncDataAuthPO2);
			
//			TrPoseFuncPO trPoseFuncPO = new TrPoseFuncPO();
//			trPoseFuncPO.setPoseId(new Long(poseId));
//			factory.delete(trPoseFuncPO);
			
			//end
			
//			String[] myfuns = new String[0];
//			if(myfun!=null && !"".equals(myfuns)) {
//				myfuns = myfun.split(",");
//			}
			
			TrRolePosePO trPoseRoelPO = new TrRolePosePO();
			trPoseRoelPO.setPoseId(new Long(poseId));
			factory.delete(trPoseRoelPO);
			
			for(int i=0; i<roleIds.length; i++) { // 保存职位对应的功能
				TrRolePosePO poseRolePO = new TrRolePosePO();
				poseRolePO.setRolePoseId(factory.getLongPK(new TrRolePosePO()));
				poseRolePO.setRoleId(new Long(roleIds[i]));
				poseRolePO.setPoseId(new Long(poseId));
				factory.insert(poseRolePO);
				//modify by xiayanpeng begin 删除TrPoseFuncDataAuthPO表逻辑
//				if(hashMap.containsKey(myfuns[i])) { // 该功能设置了数据权限
//					String[] ts = hashMap.get(myfuns[i]);
//					for(int j=0; j<ts.length; j++) {
//						TrPoseFuncDataAuthPO trPoseFuncDataAuthPO = new TrPoseFuncDataAuthPO();
//						trPoseFuncDataAuthPO.setPoseFuncDataAuthId(factory.getLongPK(new TrPoseFuncDataAuthPO()));
//						trPoseFuncDataAuthPO.setFuncId(new Long(myfuns[i]));
//						trPoseFuncDataAuthPO.setPoseId(new Long(poseId));
//						trPoseFuncDataAuthPO.setDataAuthId(new Long(ts[j]));
//						trPoseFuncDataAuthPO.setPossieFuncId(poseFuncPO.getPoseFuncId());
//						factory.insert(trPoseFuncDataAuthPO);
//					}
//				} else {
//					Long tety; //根据职位类型赋予默认的数据权限
//					if(poseType.equals(Constant.SYS_USER_SGM)) {
//						tety = Constant.SGM_BZZJYX;
//					} else {
//						tety = Constant.DRL_BZZJYX;
//					}
//					TrPoseFuncDataAuthPO trPoseFuncDataAuthPO = new TrPoseFuncDataAuthPO();
//					trPoseFuncDataAuthPO.setPoseFuncDataAuthId(factory.getLongPK(new TrPoseFuncDataAuthPO()));
//					trPoseFuncDataAuthPO.setFuncId(new Long(myfuns[i]));
//					trPoseFuncDataAuthPO.setPoseId(new Long(poseId));
//					trPoseFuncDataAuthPO.setDataAuthId(tety);
//					trPoseFuncDataAuthPO.setPossieFuncId(poseFuncPO.getPoseFuncId());
//					factory.insert(trPoseFuncDataAuthPO);
//				}
				//modify by xiayanpeng end 
			}
			
			if(poseType.equals(Constant.SYS_USER_DEALER)) {
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
			}
			
			act.setOutData("st", "succeed");
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"系统职位");
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
				String deptId = request.getParamValue("DEPT_ID");
				String dcode = request.getParamValue("DRLCODE");
				String dsname = request.getParamValue("DELSNAME");
				
				String orderName = request.getParamValue("orderCol2");
				String da = request.getParamValue("order2");
				
				Integer curPage = request.getParamValue("curPage2") != null ? Integer
						.parseInt(request.getParamValue("curPage2"))
						: 1; // 处理当前页
						PageResult<TmDealerPO> ps = UserMngDAO.getDRLByDeptId(deptId,dcode,dsname,logonUser.getCompanyId(), Constant.PAGE_SIZE,curPage,orderName,da);  
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
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"SGM维护经销商用户");
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
			RequestWrapper request = act.getRequest();
			act.getResponse().setContentType("application/json");
			if("1".equals(request.getParamValue("COMMAND"))){ // 表格请求处理
				// 职位名称
				String poseName = CommonUtils.checkNull(request.getParamValue("POSE_NAME"));
				// 职位代码
				String poseCode = CommonUtils.checkNull(request.getParamValue("POSE_CODE"));
				// 职位类型
				String poseType = CommonUtils.checkNull(request.getParamValue("POSE_TYPE"));
				// 经销商ID
				String dealerId = CommonUtils.checkNull(request.getParamValue("DEALER_ID"));
				// 组织ID
				String deptId = CommonUtils.checkNull(request.getParamValue("DEPT_ID"));
				// 经销商公司ID
				String companyId = CommonUtils.checkNull(request.getParamValue("COMPANY_ID"));
				//车厂公司ID
				Long oemCompanyId = logonUser.getCompanyId();
				//角色代码
				String roleName = CommonUtils.checkNull(request.getParamValue("ROLE_NAME"));
				
				String poseStatus = CommonUtils.checkNull(request.getParamValue("POSE_STATUS"));
				// 经销商公司ID
				String parPoseId = CommonUtils.checkNull(request.getParamValue("par_pose_id"));
				// 经销商公司ID
				String poseRank = CommonUtils.checkNull(request.getParamValue("POSE_RANK"));
				
				/*List<MsgCarrier> vlist = new ArrayList<MsgCarrier>();
				vlist.add(MsgCarrier.getInstance(ValidateCodeConstant.NOQUOTATION_PATTERN,"职位名称",0,Constant.Length_Check_Char_100,poseName));
				vlist.add(MsgCarrier.getInstance(ValidateCodeConstant.NOQUOTATION_PATTERN,"职位代码",0,Constant.Length_Check_Char_100,poseCode));
				Validate.doValidate(act, vlist);*/
				
				String orderName = request.getParamValue("orderCol");
				String da = request.getParamValue("order");
				
				Integer curPage = request.getParamValue("curPage") != null ? Integer
						.parseInt(request.getParamValue("curPage"))
						: 1; // 处理当前页
				PageResult<Map<String, Object>> ps = dao.sysPoseQuery(parPoseId,poseRank,companyId,poseCode, poseName, poseType, dealerId, deptId, roleName, Constant.PAGE_SIZE, curPage,orderName,da,oemCompanyId,poseStatus); // 按条件查询返回职位list  
				act.setOutData("ps", ps);
			}
		} catch(BizException e){
			logger.error(logonUser,e);
			act.setException(e);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"系统职位");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void getDelList() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			
			String comId = request.getParamValue("comId") ;
			
			List<Map<String, Object>> delList = DealerInfoDao.getInstance().getDelList(comId) ;
			
			act.setOutData("delList", delList) ;
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"系统职位");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void getAreaList() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			
			String delId = request.getParamValue("delId") ;
			
			List<Map<String, Object>> areaList = DealerInfoDao.getInstance().getAreaList(delId) ;
			
			act.setOutData("areaList", areaList) ;
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"系统职位");
			logger.error(logonUser,e1);
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
	public void toPoseList() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			act.getSession().get(Constant.LOGON_USER);
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
	public void getPoseList() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = null;
		try {
			RequestWrapper request = act.getRequest();
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			act.getSession().get(Constant.LOGON_USER);
			Long companyId = new Long(logonUser.getCompanyId());
			String poseCode = CommonUtils.checkNull(request.getParamValue("poseCode"));
			String poseName = CommonUtils.checkNull(request.getParamValue("poseName"));
			Map<String,String> map=new HashMap<String,String>();
			map.put("companyId", companyId.toString());
			map.put("poseCode", poseCode);
			map.put("poseName", poseName);
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
			PageResult<Map<String, Object>> ps = dao.getPoseList(map,Constant.PAGE_SIZE, curPage);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "实销信息上报:查询销售顾问列表结果展示");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
}

