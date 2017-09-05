/**********************************************************************
* <pre>
* FILE : IndividualTask.java
* CLASS : IndividualTask
*
* AUTHOR : wry
*
* FUNCTION : 个人任务清单
*
*
*======================================================================
* CHANGE HISTORY LOG
*----------------------------------------------------------------------
* MOD. NO.| DATE     | NAME | REASON | CHANGE REQ.
*----------------------------------------------------------------------
*         |2009-09-08| wry  | Created |
* DESCRIPTION:
* </pre>
***********************************************************************/
package com.infodms.dms.actions.individualInfo.individualTask;

import java.util.List;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.individualTask.IndividualTaskDAO;
import com.infodms.dms.dao.menu.MenuDAO;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TcFuncPO;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.POFactory;
import com.infoservice.po3.POFactoryBuilder;

/**
 * function:个人任务清单
 * author: wry
 * CreateDate: 2009-09-08
 * @version:0.1
 */
public class IndividualTask {
	public Logger logger = Logger.getLogger(IndividualTask.class);
	private POFactory factory = POFactoryBuilder.getInstance();
	/**
	 * function:个人任务清单 
	 * @return: 得到符合条件的任务清单内容
	 * @throws BizException
	 * author: wry
	 * date: 2009-09-08
	 */
	public void getTaskInfo(){
		ActionContext act = ActionContext.getContext();
		AclUserBean  logonUser = null;
		try{
			RequestWrapper request = act.getRequest();
			logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
			act.setForword("/jsp/individualInfo/individuaTask/individualTask.jsp");
			
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
			logger.debug("funcs-------"+funcs);
			//得到回访结果
			List wayOfVisitingList = IndividualTaskDAO.wayOfVisiting(request.getRequestURI(), logonUser, funcs);
			act.setOutData("wayOfVisitingList", wayOfVisitingList);
			//得到审批结果
			List examineApproveList = IndividualTaskDAO.examineApprove(request.getRequestURI(), logonUser, funcs);
			act.setOutData("examineApproveList", examineApproveList);
			
			//得到代办结果
//			整备开始
			List pendinRequestList = IndividualTaskDAO.pendinRequest(request.getRequestURI(), logonUser, funcs);
			act.setOutData("pendinRequestList", pendinRequestList);		
//			车辆出库
			List vhclOutList = IndividualTaskDAO.outRequest(request.getRequestURI(), logonUser, funcs);
			act.setOutData("vhclOutList", vhclOutList);
//			车辆入库
			List InComeList = IndividualTaskDAO.getNotInCome(request.getRequestURI(), logonUser, funcs);
			act.setOutData("InComeList", InComeList);
//			销售上报
			List saleList = IndividualTaskDAO.saleRequest(request.getRequestURI(), logonUser, funcs);
			act.setOutData("saleList", saleList);
//			车辆过户
			List vhclList = IndividualTaskDAO.vhclRequest(request.getRequestURI(), logonUser, funcs);
			act.setOutData("vhclList", vhclList);
//			预留取消
			List cancelList = IndividualTaskDAO.obligateCancel(request.getRequestURI(), logonUser, funcs);
			act.setOutData("cancelList", cancelList);
//			认证申请
			List attestationList = IndividualTaskDAO.attestation(request.getRequestURI(), logonUser, funcs);
			act.setOutData("attestationList", attestationList);
//			来访处理
			List visitingDisposalList = IndividualTaskDAO.visitingDisposal(request.getRequestURI(), logonUser, funcs);
			act.setOutData("visitingDisposalList", visitingDisposalList);
			
			int pendin =pendinRequestList != null ? pendinRequestList.size(): 0;
			int vhclOut =vhclOutList != null ? vhclOutList.size(): 0;
			int InCome =InComeList != null ? InComeList.size(): 0;
			int sale =saleList != null ? saleList.size(): 0;
			int vhcl =vhclList != null ? vhclList.size(): 0;
			int cancel =cancelList != null ? cancelList.size(): 0;
			int attestation =attestationList != null ? attestationList.size(): 0;
			int visitingDisposal =visitingDisposalList != null ? visitingDisposalList.size(): 0;
			int allsum = pendin+vhclOut+InCome+sale+vhcl+cancel+attestation+visitingDisposal;
			act.setOutData("allsum", allsum);
		}catch(BizException e){
			logger.error(logonUser,e);
			act.setException(e);
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.FAILURE_CODE,"个人任务清单操作");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * function:任务跳转处理信息 
	 * @throws BizException
	 * author: wry
	 * date: 2009-09-11
	 */
	public void turnTaskStat(){
		ActionContext act = ActionContext.getContext();
		AclUserBean  logonUser = null;
		try{
			logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
			RequestWrapper request = act.getRequest();
			String orIdHF = request.getParamValue("orIdHF");
			String procUrlHF = request.getParamValue("procUrlHF");
			String orIdSP = request.getParamValue("orIdSP");
			String procUrlSP = request.getParamValue("procUrlSP");
			String orIdDB = request.getParamValue("orIdDB");
			String procUrlDB = request.getParamValue("procUrlDB");
			
			String falt = request.getParamValue("falt");
			if(falt.equals("HF")){
				System.err.println("-------------come on hui fang---procUrlHF---------------- "+procUrlHF);
				System.err.println("orIdHF--------------- "+orIdHF);
//				TcOperateRemindPO conditionPO = new TcOperateRemindPO();
//				conditionPO.setOperateRemindId(orIdHF);
//				
//				TcOperateRemindPO trPO = new TcOperateRemindPO();
//				trPO.setActionStat(Constant.ACCEPT_RES_ACCEPT);
//				
//				factory.update(conditionPO, trPO);
				//act.setForword(procUrlHF.substring(0,procUrlHF.length()-1));
				act.setRedirect(procUrlHF);
			}else if(falt.equals("SP")){
				System.err.println("-------------come on shen pi------------------- ");
//				TcOperateRemindPO conditionPO = new TcOperateRemindPO();
//				conditionPO.setOperateRemindId(orIdSP);
//				
//				TcOperateRemindPO trPO = new TcOperateRemindPO();
//				trPO.setActionStat(Constant.ACCEPT_RES_ACCEPT);
//				
//				factory.update(conditionPO, trPO);
				act.setRedirect(procUrlSP);
			}else if(falt.equals("DB")){
				System.err.println("-------------come on dai ban ------------------- ");
//				TcOperateRemindPO conditionPO = new TcOperateRemindPO();
//				conditionPO.setOperateRemindId(orIdDB);
//				
//				TcOperateRemindPO trPO = new TcOperateRemindPO();
//				trPO.setActionStat(Constant.ACCEPT_RES_ACCEPT);
//				
//				factory.update(conditionPO, trPO);
				act.setRedirect(procUrlDB);
			}
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.FAILURE_CODE,"任务跳转处理信息");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	
}
