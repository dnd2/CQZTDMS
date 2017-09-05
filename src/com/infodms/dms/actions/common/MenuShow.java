/**********************************************************************
* <pre>
* FILE   : MenuShow.java
* CLASS  : MenuShow
*
* AUTHOR : chenliang
*
* FUNCTION : 用户菜单展示
*
*
*======================================================================
* CHANGE HISTORY LOG
*----------------------------------------------------------------------
* MOD. NO.| DATE     | NAME       | REASON | CHANGE REQ.
*----------------------------------------------------------------------
*         |2009-09-18| chenliang  | Created |
* DESCRIPTION:
* </pre>
***********************************************************************/
/**
 * $MenuShow,v0.1 2009/09/18  chenliang  用户菜单展示$
 */

package com.infodms.dms.actions.common;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.sysmng.notice.dealerNotice.DealerNoticeAction;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.OperateRemindBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.dao.claim.basicData.HomePageNewsDao;
import com.infodms.dms.dao.claim.dealerClaimMng.ClaimBillMaintainDAO;
import com.infodms.dms.dao.common.CommonDAO;
import com.infodms.dms.dao.individualTask.IndividualTaskDAO;
import com.infodms.dms.dao.menu.MenuDAO;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TcFuncPO;
import com.infodms.dms.po.TcFuncPO2;
import com.infodms.dms.po.TcPosePO;
import com.infodms.dms.po.TcUserPO;
import com.infodms.dms.po.TmDealerPO;
import com.infodms.dms.po.TmOrgPO;
import com.infodms.dms.po.TtCrmSeatsPO;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.po3.POFactory;
import com.infoservice.po3.POFactoryBuilder;
import com.infoservice.po3.core.callback.DAOCallback;
import com.infoservice.po3.core.exceptions.DAOException;

import edu.emory.mathcs.backport.java.util.LinkedList;

/**
 * Function       :  菜单展示
 * @author        :  chenliang
 * CreateDate     :  2009-09-18
 * @version       :  0.1
 */
public class MenuShow { 
	private static Logger logger = Logger.getLogger(MenuShow.class);
	private final HomePageNewsDao newsDao = HomePageNewsDao.getInstance();
	private POFactory factory = POFactoryBuilder.getInstance();
	private boolean firstLogin = true;
	
	public void menuDisplay() throws Exception {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try 
		{
			Long orgId = new Long(act.getRequest().getParamValue("deptId"));
			Long poseId = new Long(act.getRequest().getParamValue("poseId"));
			//modified by andy.ten@tom.com 2010-05-19 增加获取poseType参数
			String sPoseType = act.getRequest().getParamValue("poseType");
			String sPoseBusType = act.getRequest().getParamValue("poseBusType");
			String state = act.getRequest().getParamValue("state");
			act.setOutData("deptId", orgId);
			act.setOutData("poseId", poseId);
			act.setOutData("poseType", sPoseType);
			act.setOutData("poseBusType", sPoseBusType);
			act.setOutData("state", state);
			
			//TODO 添加首次登陆，解决TIPDIV第一次显示问题 2012-12-17 HXY
			if(!CommonUtils.checkIsNullStr(act.getRequest().getParamValue("fid"))) {
				firstLogin = false;
			}
			//TODO END
			//MODIFY by XZM 当sPoseType不为空时更新改信息(更正职位切换后经销商信息不正确问题)
			if(Utility.testString(sPoseType))
				logonUser.setPoseType(Integer.valueOf(sPoseType));
			if(Utility.testString(sPoseBusType))
				logonUser.setPoseBusType(Integer.valueOf(sPoseBusType));
			
			/**
			if(sPoseType == null || "".equals(sPoseType))
				sPoseType = logonUser.getUserType().toString();
			if(sPoseType == null || "".equals(sPoseType))
                sPoseType = getUserTyoe(poseId).toString();
             */
			if(sPoseType == null || "".equals(sPoseType))
				sPoseType = logonUser.getPoseType().toString();
			if(sPoseBusType == null || "".equals(sPoseBusType))
				sPoseBusType = logonUser.getPoseBusType().toString();
			Integer poseType = new Integer(sPoseType);
			String fId = act.getRequest().getParamValue("fid");
			/**
			 * del by andy.ten@tom.com 2010-05-19 校验没有意义
			
			List<MsgCarrier> vlist = new ArrayList<MsgCarrier>();
			vlist.add(MsgCarrier.getInstance(ValidateCodeConstant.DIGIT_PATTERN,"部门ID",1,Constant.Length_Check_Char_10,String.valueOf(orgId)));
			vlist.add(MsgCarrier.getInstance(ValidateCodeConstant.DIGIT_PATTERN,"职位ID",1,Constant.Length_Check_Char_10,String.valueOf(poseId)));
			Validate.doValidate(act, vlist);
			 */
			if(logonUser.getOrgId()==null||logonUser.getOrgId() ==0 ||"".equals(logonUser.getOrgId())) {
				if(CommonDAO.isLogUserOnline(logonUser.getUserId())) {
					CommonDAO.disableUserOnlineInfo(logonUser.getUserId());
				}
			}else if(!logonUser.getOrgId().equals(orgId)) {
				CommonDAO.updateUserOnlineInfo(logonUser,orgId);
			}
			logonUser.setOrgId(orgId);
			TmOrgPO conOrg = new TmOrgPO();
			conOrg.setOrgId(orgId);
			List<TmOrgPO> orgList = factory.select(conOrg);
			if(orgList.size()>0 && orgList.get(0).getOrgType()!=null)
			{
				logonUser.setOrgType(orgList.get(0).getOrgType());
				//added by andy.ten@tom.com
				logonUser.setDutyType(orgList.get(0).getDutyType().toString());
				logonUser.setParentOrgId(orgList.get(0).getParentOrgId().toString());
				//end
			}
			logonUser.setPoseId(poseId);
			logonUser.setUID(String.valueOf(poseId));
			//added by andy.ten@tom.com 2010-05-19 userType使用的是TM_POSE表的POSE_TYPE begin
			//根据poseType，如果poseType是经销商，则设置logonUser中的dealer_id
			logonUser.setUserType(poseType);
			if(logonUser.getUserType().equals(Constant.SYS_USER_DEALER))
			{
				HashMap<String, String> hm = new HashMap<String, String>();
				/****modify xieyj*****/
				TcPosePO pose_po = new TcPosePO();
				pose_po.setPoseId(poseId);
				pose_po = factory.select(pose_po).get(0);
				getUserDearId(pose_po.getCompanyId(), sPoseBusType, hm);
				/*********************/
		//		getUserDearId(orgId ,sPoseBusType ,hm); //modify xieyj
				logonUser.setDealerId(hm.get("DEALER_IDS"));
				//add by zhaojinyu 2010-06-12 
				logonUser.setOemCompanyId(hm.get("OEM_COMPANY_ID"));
				if(hm.get("COMPANY") != null && !"".equals(hm.get("COMPANY")))
					logonUser.setCompanyId(Long.valueOf(hm.get("COMPANY")));
				logonUser.setDealerOrgId(hm.get("DEALER_ORG_ID"));
				logonUser.setDealerCode(hm.get("DEALER_CODE"));
				logonUser.setDealerType(Integer.valueOf(hm.get("DEALER_TYPE")));
				//end
			}
			//logonUser.setUserType(getUserTyoe(poseId));
			// end
			if(logonUser.getUserType().equals(Constant.SYS_USER_SGM))
			{
				logonUser.setOemPositionArea(getOemUserArea(poseId));
			}
			logonUser.setBxjDept(getXjbm(orgId, logonUser.getCompanyId()));
			/*****数据权限设置类型add by liuxh *****/
			TcPosePO pose=new TcPosePO();
			pose.setPoseId(poseId);
			pose=factory.select(pose).get(0);
			logonUser.setChooseDealer(pose.getChooseDealer());
			act.getSession().set(Constant.LOGON_USER, logonUser);
			
			String dealerId = logonUser.getDealerId();
			String partAddPer = "0";
			if (Utility.testString(dealerId)) {
				String [] temp = dealerId.split(",");
				if(temp.length>1) {
					
				}else {
				ClaimBillMaintainDAO cd = ClaimBillMaintainDAO.getInstance();
				partAddPer = cd.getPartAddPer(dealerId);
				}
			}
			act.getSession().set(Constant.PART_ADD_PER, partAddPer);
			if(fId==null||fId.length()<=0) {
				if(!CommonDAO.isLogUserOnline(logonUser.getUserId())) {
					//CommonDAO.addUserOnlineInfo(logonUser,act.getRequest().getRemoteAddr());
					logger.info("用户成功登录，登陆用户信息：用户ID="+logonUser.getUserId()+"、用户名="+logonUser.getName()+"、经销商ID="+logonUser.getCompanyId()+"、部门ID="+logonUser.getOrgId());
					//TODO 添加首次登陆，解决TIPDIV第一次显示问题 2012-12-17 HXY
					firstLogin = true;
					//TODO END
				}
			}
			//验证是否是当前用户操作,避免同台机器登陆不同用户时出现后权限错乱问题
			//add hxy 2012-04-28
			
			act.setOutData("userId", logonUser.getUserId());
			TcUserPO userPO = new TcUserPO();
			userPO.setUserId(logonUser.getUserId());
			logonUser.setActn(factory.select(userPO).get(0).getAcnt());
			if(logonUser.getAddr() == null)
			{
					logonUser.setAddr(state);
			}else if (!logonUser.getAddr().equals("1")){
				logonUser.setAddr(state);
			}
			TtCrmSeatsPO seatsPO = new TtCrmSeatsPO();
			seatsPO.setSeSeatsNo(factory.select(userPO).get(0).getAcnt());
			List<TtCrmSeatsPO> seatList= factory.select(seatsPO);
			// // 艾春 9.17 修改得到客户端的IP地址
			String addr = act.getRequest().getRemoteAddr();
			Integer txtExt = getSeatsTxtExt(addr);
			
			if(seatList.size() > 0 && null != txtExt)
			{
				  
				int seIsManamger= seatList.get(0).getSeIsManamger();
				if(seIsManamger == 95221001)
				{
					logonUser.setTxtExt(""+seatList.get(0).getSeExt());
					act.setOutData("txtExt",seatList.get(0).getSeExt());
				}else
				{
					logonUser.setTxtExt(txtExt.toString());
					act.setOutData("txtExt",txtExt);
				}
//				logonUser.setTxtExt(""+seatList.get(0).getSeExt());
//				act.setOutData("txtExt",seatList.get(0).getSeExt() );
				// 艾春 9.17 修改 设置分机号到Session
				
			}
			
			//如果是经销商登录则显示经销商名称
			if(logonUser.getDealerId() != null && !"".equals(logonUser.getDealerId())) {
				TmDealerPO t = new TmDealerPO();
				t.setDealerId(Long.parseLong(logonUser.getDealerId()));
				List<TmDealerPO> listPo = factory.select(t);
				if(listPo != null && !listPo.isEmpty()) {
					act.setOutData("isDealerLogin", "true");
					TmDealerPO po = listPo.get(0);
					act.setOutData("dealerName", po.getDealerName());
					
					/*
					//获取职位名称
					TcPosePO poseTmp = new TcPosePO();
					poseTmp.setPoseId(logonUser.getPoseId());
					List<TcPosePO> listPose = factory.select(poseTmp);
					if(listPose != null && !listPose.isEmpty()) {
						TcPosePO pos = listPose.get(0);
						act.setOutData("poseName", pos.getPoseName());
					}
					*/
				} else {
					act.setOutData("isDealerLogin", "false");
				}
			} else {
				act.setOutData("isDealerLogin", "false");
			}
			
			act.setOutData("sessionId", act.getSession().getId());
			act.setOutData("user", logonUser);
			act.setOutData("firstLogin", firstLogin);
			//登陆时增加用户未读信息提示  ranke 20150408
			//Integer newsTab = (Integer)act.getSession().get("loginTab");
			//if(newsTab != null && newsTab == 1){
			//	Long newsNum = newsDao.queryNewsNum(logonUser, dealerId);
			//	act.getSession().set("newsNum", newsNum);
			//	act.getSession().remove("loginTab");
			//}
			//end
			if( "old".equals( act.getRequest().getParamValue("ver") ) ){
				act.setForword("menu");
			}else{
				new DealerNoticeAction().queryNotice();
				act.setForword("menu");
			}
			
		} catch(BizException e){
			logger.error(logonUser,e);
			act.setException(e);
		} catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"菜单展示");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 通过IP地址查询得到坐席分机号
	 * @param addr
	 * @return
	 */
	private Integer getSeatsTxtExt(String addr){
		int ext = 0;
		String sql = "SELECT EXT FROM tt_crm_ip_address t WHERE t.ip = '"+addr+"'";
		try {
			List<HashMap> list = factory.select(sql.toString(), null, new DAOCallback<HashMap>(){
				
				public HashMap wrapper(ResultSet rs, int idx) {
					// TODO Auto-generated method stub
					Map map = null;
					try {
						if(null != rs){
							ResultSetMetaData rsd = rs.getMetaData();
							int columnCount = rsd.getColumnCount();
							map = new HashMap();
							for (int i=1; i<=columnCount; i++){
								map.put(rsd.getColumnName(i).toString(),rs.getObject(i));
							}
						}
					} catch (SQLException e) {
						e.printStackTrace();
					}
					return (HashMap) map;
				}
			
			});
			if(list.size() > 0){
				ext = Integer.parseInt(list.get(0).get("EXT").toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ext;
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
	

	public void getUserSysFun(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			List<TcFuncPO> list = MenuDAO.getFuncTreeByPoseId(logonUser.getPoseId());// 得到系统功能
			List<TcFuncPO2> tmp = new LinkedList();
			act.setOutData("sysfun", list);
		} catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"用户菜单显示");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
		
	/**
	 * 得到系统功能
	 */
	public void getSysFun() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			List<TcFuncPO> list = CommonDAO.getFunc("");// 得到系统功能
			List<TcFuncPO2> tmp = new LinkedList();
			for(int i = 0 ; i < list.size() ; i ++)
			{
				TcFuncPO po1 = list.get(i);
				TcFuncPO2 po2 = new TcFuncPO2();
				po2.setFuncCode(po1.getFuncCode());
				po2.setFuncId(po1.getFuncId().toString());
				po2.setFuncName(po1.getFuncName());
//				po2.setFuncType(po1.getFuncType().toString());
				po2.setParFuncId(po1.getParFuncId().toString());
				tmp.add(po2);
			}
			act.setOutData("sysfun", tmp);
		} catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"用户菜单显示");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 得到用户待处理任务
	 */
	public void getTaskLink() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			
			List<TcFuncPO> list = MenuDAO.getFuncByPoseId(logonUser.getPoseId());   // 得到当前用户所有功能
			String funcs = "";
			for(TcFuncPO po:list) {
				funcs+="'"+po.getFuncCode().substring(0,po.getFuncCode().lastIndexOf("/")).trim()+"',";
			}
			if(funcs.length()>0) {
				funcs = "("+funcs.substring(0,funcs.length()-1)+")";
			}else {
				funcs = "()";
			}
			
			List<OperateRemindBean> wayOfVisitingList = IndividualTaskDAO.wayOfVisiting("/DMSUC/individualInfo/individualTask/IndividualTask/getTaskInfo.do", logonUser, funcs); //回访
			List<OperateRemindBean> examineApproveList = IndividualTaskDAO.examineApprove("/DMSUC/individualInfo/individualTask/IndividualTask/getTaskInfo.do", logonUser, funcs); //审批
			List<OperateRemindBean> pendinRequestList = IndividualTaskDAO.pendinRequest("/DMSUC/individualInfo/individualTask/IndividualTask/getTaskInfo.do", logonUser, funcs); // 待办
			List<OperateRemindBean> vhclOutList = IndividualTaskDAO.outRequest("/DMSUC/individualInfo/individualTask/IndividualTask/getTaskInfo.do", logonUser, funcs);
			
			List<OperateRemindBean> InComeList = IndividualTaskDAO.getNotInCome("/DMSUC/individualInfo/individualTask/IndividualTask/getTaskInfo.do", logonUser, funcs);
//			销售上报
			List<OperateRemindBean> saleList = IndividualTaskDAO.saleRequest("/DMSUC/individualInfo/individualTask/IndividualTask/getTaskInfo.do", logonUser, funcs);
//			车辆过户
			List<OperateRemindBean> ivhclList = IndividualTaskDAO.vhclRequest("/DMSUC/individualInfo/individualTask/IndividualTask/getTaskInfo.do", logonUser, funcs);
//			预留取消
			List<OperateRemindBean> cancelList = IndividualTaskDAO.obligateCancel("/DMSUC/individualInfo/individualTask/IndividualTask/getTaskInfo.do", logonUser, funcs);
			
			act.setOutData("hflist", wayOfVisitingList);
			act.setOutData("splist", examineApproveList);
			act.setOutData("dblist1", pendinRequestList);
			act.setOutData("dblist2", vhclOutList);
			act.setOutData("dblist3", InComeList);
			act.setOutData("dblist4", saleList);
			act.setOutData("dblist5", ivhclList);
			act.setOutData("dblist6", cancelList);
		} catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"用户菜单显示");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void getShortCutMenu(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		
		try {
			List<TcFuncPO> list = MenuDAO.getShortCutMenu(logonUser.getPoseId(),logonUser.getUserId());   // 得到当前用户所有功能
			
			List<TcFuncPO2> tmp = new LinkedList();
			for(int i = 0 ; i < list.size() ; i ++)
			{
				TcFuncPO po1 = list.get(i);
				TcFuncPO2 po2 = new TcFuncPO2();
				po2.setFuncCode(po1.getFuncCode());
				po2.setFuncId(po1.getFuncId().toString());
				po2.setFuncName(po1.getFuncName());
//				po2.setFuncType(po1.getFuncType().toString());
		//		po2.setParFuncId(po1.getParFuncId().toString());
				tmp.add(po2);
			}
			act.setOutData("ShortFuns", tmp);
		} catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"用户菜单显示");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 得到用户所有功能
	 */
	public void getUserFuns() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			List<TcFuncPO> list = MenuDAO.getFuncByPoseId(logonUser.getPoseId());   // 得到当前用户所有功能
			
			List<TcFuncPO2> tmp = new LinkedList();
			for(int i = 0 ; i < list.size() ; i ++)
			{
				TcFuncPO po1 = list.get(i);
				TcFuncPO2 po2 = new TcFuncPO2();
				po2.setFuncCode(po1.getFuncCode());
				po2.setFuncId(po1.getFuncId().toString());
				po2.setFuncName(po1.getFuncName());
//				po2.setFuncType(po1.getFuncType().toString());
				po2.setParFuncId(po1.getParFuncId().toString());
				tmp.add(po2);
			}
			act.setOutData("userfuns", tmp);
		} catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"用户菜单显示");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 根据职位ID查询用户类型
	 * @param pose
	 * @return
	 */
	private Integer getUserTyoe(Long poseId) {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			TcPosePO posePO = new TcPosePO();
			posePO.setPoseId(poseId);
			return factory.select(posePO).get(0).getPoseType();
		} catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"用户菜单显示");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
		return null;
	}
	
	
	/**
	 * 根据职位ID查询用户类型
	 * @param pose
	 * @return
	 */
	//modify by xiayanpeng begin 取消职位表中冗余业务分类，在业务表中获取 此方法隐藏
//	private String getOemUserArea(Long poseId) {
//		ActionContext act = ActionContext.getContext();
//		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
//		try {
//			TcPosePO posePO = new TcPosePO();
//			posePO.setPoseId(poseId);
//			return factory.select(posePO).get(0).getOemPositionArea();
//		} catch(Exception e){
//			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"用户菜单显示");
//			logger.error(logonUser,e1);
//			act.setException(e1);
//		}
//		return null;
//	}
	//modify by xiayanpeng end
	/**
	 * 取登录用户，职位业务范围
	 * @param poseId
	 * @return
	 */
	//modify by xiayanpeng begin 取职位业务范围
	public String getOemUserArea(Long poseId){
		StringBuffer sql = new StringBuffer("");
		sql.append("select ba.area_id\n");
		sql.append("  from tm_pose_business_area pa, tm_business_area ba\n");  
		sql.append(" where pa.area_id = ba.area_id\n"); 
		sql.append("   and pa.pose_id = "+poseId);
		sql.append("   and ba.status ="+Constant.STATUS_ENABLE);
		List list =  factory.select(sql.toString(), null, new DAOCallback<HashMap>() {
		
			public HashMap wrapper(ResultSet rs, int idx) {
				// TODO Auto-generated method stub
				Map map = new HashMap();
				try {
					ResultSetMetaData rsd = rs.getMetaData();
					int columnCount = rsd.getColumnCount();
					map = new HashMap();
					for (int i=1; i<=columnCount; i++){
						map.put(rsd.getColumnName(i).toString(),rs.getObject(i));
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
				return (HashMap) map;
			}
		
		});
		String s = "(";
		for (int i = 0; i < list.size(); i++) {
			Map map = (Map)list.get(i);
			s=s+map.get("AREA_ID");
			if(i<list.size()-1){
				s=s+",";
			}
		}
		return s+")";
	}
	
	/**
	 * 根据职位ID查询经销商dearId
	 * @param pose
	 * @return
	 */
	public void getUserDearId(Long orgId ,String pose_bus_type ,HashMap<String, String> hm) 
	{
		String dealer_id = "";
		String dealer_org_id = "";
		String oem_company_id = "";
		String company_id = "";
		String dealer_code="";
		String dealer_type = "";
		switch (Integer.valueOf(pose_bus_type))
		{
			case Constant.POSE_BUS_TYPE_WL: //物流商
				dealer_type = Constant.DEALER_TYPE_DVS +"";
				break;
			case Constant.POSE_BUS_TYPE_DVS:
				dealer_type = Constant.DEALER_TYPE_DVS +"";
				break;
			case Constant.POSE_BUS_TYPE_DWR:
				dealer_type = Constant.DEALER_TYPE_DWR + "";
				break;
			case Constant.POSE_BUS_TYPE_JSZX:
				dealer_type = Constant.DEALER_TYPE_JSZX + "";
				break;
			default:
				break;
		}
		StringBuffer sql = new StringBuffer("");
		//modify by andy.ten@tom.com begin
		//2010-8-2 查询条件加上STATUS=10011001
		sql.append(" SELECT D.DEALER_ID,D.DEALER_CODE,D.DEALER_TYPE,D.DEALER_ORG_ID,D.OEM_COMPANY_ID,D.COMPANY_ID FROM TM_DEALER D WHERE D.DEALER_ID = "+orgId+"\n");
	//	sql.append(" WHERE D.DEALER_ORG_ID = "+orgId+" AND D.DEALER_TYPE IN ("+dealer_type+") AND D.STATUS="+Constant.STATUS_ENABLE); //modify xieyj
		//end
		List<Object> params = new ArrayList<Object>();
		//params.add(orgId);
		//params.add(dealer_type);
  		List<TmDealerPO> list = factory.select(sql.toString(), params,
				new DAOCallback<TmDealerPO>()
				{
					public TmDealerPO wrapper(ResultSet rs, int idx) 
					{
						TmDealerPO tmDealerPO = new TmDealerPO();
						try 
						{
							tmDealerPO.setDealerId(rs.getLong("DEALER_ID"));
							tmDealerPO.setDealerOrgId(rs.getLong("DEALER_ORG_ID"));
							tmDealerPO.setOemCompanyId(rs.getLong("OEM_COMPANY_ID"));
							tmDealerPO.setCompanyId(rs.getLong("COMPANY_ID"));
							tmDealerPO.setDealerCode(rs.getString("DEALER_CODE"));
							tmDealerPO.setDealerType(rs.getInt("DEALER_TYPE"));
						} catch (SQLException e) 
						{
							throw new DAOException(e);
						}
						return tmDealerPO;
					}
				});
  		if(list.size() > 0 && list.get(0) != null)
  		{
  			/**
  			 * added by andy.ten@tom.com 一个orgId对应多个dealerId ，中间用","分开
  			 * 只对整车销售职位有该情况，对于售后，一个orgId还是对应一个dealerId，没有业务范围之分
  			 */
  			TmDealerPO dealerPO = (TmDealerPO) list.get(0);
			oem_company_id = dealerPO.getOemCompanyId().toString();
			dealer_org_id = dealerPO.getDealerOrgId().toString();
			company_id = dealerPO.getCompanyId().toString();
			dealer_code =dealerPO.getDealerCode().toString();
			dealer_type = dealerPO.getDealerType().toString();
			
  			if(list.size() == 1)
  			{
  				 dealer_id = dealerPO.getDealerId().toString(); 
  			}	 
  			else 
  			{
			   for (TmDealerPO dPO : list)
			   {
				   if(dealer_id.length() > 0 )
					   dealer_id += "," + dPO.getDealerId();
				   else
					   dealer_id = dPO.getDealerId().toString(); 
			   }
			}
  			// end
  		}
		
  		hm.put("DEALER_IDS", dealer_id);
  		hm.put("OEM_COMPANY_ID", oem_company_id);
  		hm.put("DEALER_ORG_ID", dealer_org_id);
  		hm.put("COMPANY", company_id);
 		hm.put("DEALER_CODE", dealer_code);
 		hm.put("DEALER_TYPE", dealer_type);
 		
	}
}
