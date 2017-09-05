/**********************************************************************
* <pre>
* FILE : PunishmentApproval.java
* CLASS : PunishmentApproval
*
* AUTHOR : PGM
*
* FUNCTION : 奖惩审批表售后服务部审核.
*
*
*======================================================================
* CHANGE HISTORY LOG
*----------------------------------------------------------------------
* MOD. NO.| DATE     | NAME | REASON | CHANGE REQ.
*----------------------------------------------------------------------
*         |2010-05-19| PGM  | Created |
* DESCRIPTION:
* </pre>
***********************************************************************/
/**
 * $Id: PunishmentApproval.java,v 1.1 2010/08/16 01:44:22 yuch Exp $
 */
package com.infodms.dms.actions.feedbackmng.approve;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.PunishmentApplyMantainBean;
import com.infodms.dms.bean.PunishmentApprovalBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.getCompanyId.GetOemcompanyId;
import com.infodms.dms.dao.feedbackMng.PunishmentApplyMantainDao;
import com.infodms.dms.dao.feedbackMng.PunishmentApprovalDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtIfRewardAuditPO;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

/**
 * Function       :  奖惩审批表售后服务部审核
 * @author        :  PGM
 * CreateDate     :  2010-05-19
 * @version       :  0.1
 */
public class PunishmentApproval {
	private Logger logger = Logger.getLogger(PunishmentApproval.class);
	private PunishmentApprovalDao dao = PunishmentApprovalDao.getInstance();
	private PunishmentApplyMantainDao punishDao = PunishmentApplyMantainDao.getInstance();
	private ActionContext act = ActionContext.getContext();//获取ActionContext
	private AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
	private final String ApprovalInit = "/jsp/feedbackMng/approve/punishmentApprovalServiceSearch.jsp";//查询页面
	private final String ApprovalinfoUpdate = "/jsp/feedbackMng/approve/punishmentApprovalServiceDo.jsp";//审核页面
	private final String Approvalinfo = "/jsp/feedbackMng/approve/punishmentServiceDetail.jsp";//详细页面
	
	/**
	 * Function       :  奖惩审批表售后服务部审核页面初始化
	 * @param         :  
	 * @return        :  奖惩
	 * @throws        :  Exception
	 * LastUpdate     :  2010-05-19
	 */
	public void punishmentApprovalInit(){
		try {
			act.setForword(ApprovalInit);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"奖惩审批表售后服务部审核");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * Function       :  根据条件查询奖惩审批表中符合条件的信息，其中包括 ：服务大区已上报
	 * @param         :  request-工单号、经销商ID、经销商名称、服务大区（当前系统登录人）、创建时间、类型、工单状态
	 * @return        :  奖惩审批表售后服务部审核
	 * @throws        :  Exception
	 * LastUpdate     :  2010-05-19
	 */
	public void punishmentApprovalQuery(){
		try {
			Long companyId=GetOemcompanyId.getOemCompanyId(logonUser);
			RequestWrapper request = act.getRequest();
			String orderId = request.getParamValue("orderId");             //工单号
			String dealerCode = request.getParamValue("dealerCode");       //经销商CODE
			String dealerName = request.getParamValue("dealerName");       //经销商名称
			String linkMan = request.getParamValue("name");               //服务大区（当前系统登录人）
			String rewardType = request.getParamValue("rewardType");       //类型
			String beginTime = request.getParamValue("beginTime");         //提报开始时间
			String endTime = request.getParamValue("endTime");             //提报结束始时间
			String rewardStatus = request.getParamValue("rewardStatus");    //工单状态
			//当开始时间和结束时间相同时
			if(null!=beginTime&&!"".equals(beginTime)&&null!=endTime&&!"".equals(endTime)){
					beginTime = beginTime+" 00:00:00";
					endTime = endTime+" 23:59:59";
			}
			PunishmentApprovalBean  ApprovalBean =new PunishmentApprovalBean();
			ApprovalBean.setOrderId(orderId);
			ApprovalBean.setDealerCode(dealerCode);
			ApprovalBean.setDealerName(dealerName);
			ApprovalBean.setLinkMan(linkMan);
			ApprovalBean.setRewardType(rewardType);
			ApprovalBean.setBeginTime(beginTime);
			ApprovalBean.setEndTime(endTime);
			ApprovalBean.setCompanyId(companyId);
			ApprovalBean.setRewardStatus(rewardStatus);
			Integer curPage = request.getParamValue("curPage") !=null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps = dao.getAllpunishmentApprovalInfo(ApprovalBean,curPage,Constant.PAGE_SIZE );
			act.setOutData("ps", ps);
			act.setForword(ApprovalInit);
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"奖惩审批表售后服务部审核");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * Function       :  奖惩审批表售后服务部审核---查询超链接奖惩详细信息/审核页面详细信息
	 * @param         :  request-工单号、审核明细
	 * @return        :  奖惩审批信息
	 * @throws        :  Exception
	 * LastUpdate     :  2010-05-19
	 */
	public void getOrderIdInfo(){ 
		RequestWrapper request = act.getRequest();
		String orderId=request.getParamValue("orderId");//工单号
		String Approval=request.getParamValue("Approval");//审核明细
		try {
			PunishmentApprovalBean ApprovalBean=dao.getOrderIdInfo(orderId);
			List<PunishmentApprovalBean> ApprovalList=dao.getOrderIdInfoList(orderId);
			List<PunishmentApplyMantainBean> dealerList = punishDao.getDealerName(orderId);
			StringBuffer dealerName = new StringBuffer("");
			if(null!=dealerList&&dealerList.size()!=0){
					for(PunishmentApplyMantainBean  dealerNameBean:dealerList){
						if(null!=dealerNameBean.getDealerName()){
							dealerName.append(dealerNameBean.getDealerName()+",");
						}
					}
					if(null!=dealerName){
						 dealerName.deleteCharAt(dealerName.length()-1);
						 ApprovalBean.setDealerName(dealerName.toString());
					}
			}
			request.setAttribute("ApprovalBean", ApprovalBean);
			request.setAttribute("ApprovalList", ApprovalList);
			dealerName.delete(0, dealerName.length());
			if("Approval".equals(Approval)){//审核页面明细
			 act.setForword(ApprovalinfoUpdate);
			}else{
			 act.setForword(Approvalinfo);
			}
		} catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"奖惩工单详细信息");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * Function       :  审核通过/驳回奖惩细信息
	 * @param         :  request-工单号、审核意见
	 * @return        :  奖惩审批信息
	 * @throws        :  Exception
	 * LastUpdate     :  2010-05-19
	 */
	public void punishmentApprovalPass(){ 
		RequestWrapper request = act.getRequest();
		String orderId=request.getParamValue("orderIds");//工单号
		String auditContent=request.getParamValue("auditContent");//审核意见
		String pass=request.getParamValue("type");//判断审核通过/驳回:returns;通过：Pass
		TtIfRewardAuditPO  RewardAuditPO =new TtIfRewardAuditPO();
		try{
			if (orderId!=null&&!"".equals(orderId)) {
				String [] orderIdArray = orderId.split(","); //取得所有orderId放在数组中
				RewardAuditPO.setAuditDate(new Date());//审核时间
				RewardAuditPO.setAuditBy(logonUser.getUserId());//审核人
				if("pass".equals(pass)){//通过：Pass
				RewardAuditPO.setAuditStatus(Constant.AWARD_PUNISH_STATUS_SERVICE_PASS);//审批状态:10151003,表示： 售后服务部审核通过
				}else{//驳回:returns
				RewardAuditPO.setAuditStatus(Constant.AWARD_PUNISH_STATUS_SERVICE_REJECT);//审批状态:10151004,表示： 售后服务部审核驳回
				}
				RewardAuditPO.setAuditContent(auditContent);//审批意见
				RewardAuditPO.setOrgId(logonUser.getOrgId());//组织ID
				PunishmentApprovalDao.punishmentApprovalPass(orderIdArray,RewardAuditPO);
				act.setForword(ApprovalInit);
			}
		  }catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.DELETE_FAILURE_CODE,"售后服务部审核奖惩细信息");
			logger.error(logonUser,e1);
		 	act.setException(e1);
		  }
	}
}