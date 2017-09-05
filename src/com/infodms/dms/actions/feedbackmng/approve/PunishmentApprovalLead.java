/**********************************************************************
* <pre>
* FILE : PunishmentApprovalLead.java
* CLASS : PunishmentApprovalLead
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
 * $Id: PunishmentApprovalLead.java,v 1.1 2010/08/16 01:44:22 yuch Exp $
 */
package com.infodms.dms.actions.feedbackmng.approve;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.PunishmentApplyMantainBean;
import com.infodms.dms.bean.PunishmentApprovalLeadBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.getCompanyId.GetOemcompanyId;
import com.infodms.dms.dao.feedbackMng.PunishmentApplyMantainDao;
import com.infodms.dms.dao.feedbackMng.PunishmentApprovalLeadDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtIfRewardAuditPO;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

/**
 * Function       :  奖惩审批表轿车公司审核
 * @author        :  PGM
 * CreateDate     :  2010-05-19
 * @version       :  0.1
 */
public class PunishmentApprovalLead {
	private Logger logger = Logger.getLogger(PunishmentApprovalLead.class);
	private PunishmentApprovalLeadDao dao = PunishmentApprovalLeadDao.getInstance();
	private PunishmentApplyMantainDao punishDao = PunishmentApplyMantainDao.getInstance();
	private ActionContext act = ActionContext.getContext();//获取ActionContext
	private AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
	private final String ApprovalLeadInit = "/jsp/feedbackMng/approve/punishmentApprovalLeadSearch.jsp";//查询页面
	private final String ApprovalLeadInitUpdate = "/jsp/feedbackMng/approve/punishmentApprovalLeadDo.jsp";//审核页面
	private final String ApprovalLeadinfo = "/jsp/feedbackMng/approve/punishmentLeadDetail.jsp";//详细页面
	
	/**
	 * Function       :  奖惩审批表轿车公司审核页面初始化
	 * @param         :  
	 * @return        :  奖惩
	 * @throws        :  Exception
	 * LastUpdate     :  2010-05-19
	 */
	public void punishmentApprovalLeadInit(){
		try {
			act.setForword(ApprovalLeadInit);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"奖惩审批表售后服务部审核");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * Function       :  根据条件查询奖惩审批表中符合条件的信息，其中包括 ：服务大区已上报
	 * @param         :  request-工单号、服务中心名称、创建时间、类型
	 * @return        :  奖惩审批表轿车公司审核
	 * @throws        :  Exception
	 * LastUpdate     :  2010-05-19
	 */
	public void punishmentApprovalLeadQuery(){
		try {
			Long companyId=GetOemcompanyId.getOemCompanyId(logonUser);
			RequestWrapper request = act.getRequest();
			String orderId = request.getParamValue("orderId");             //工单号
			String dealerCode = request.getParamValue("dealerCode");       //经销商代码
			String dealerName = request.getParamValue("dealerName");       //经销商名称
			String linkMan = request.getParamValue("name");                //服务大区（当前系统登录人）
			String rewardType = request.getParamValue("rewardType");       //类型
			String beginTime = request.getParamValue("beginTime");         //提报开始时间
			String endTime = request.getParamValue("endTime");             //提报结束始时间
			String rewardStatus = request.getParamValue("rewardStatus");    //工单状态
			//当开始时间和结束时间相同时
			if(null!=beginTime&&!"".equals(beginTime)&&null!=endTime&&!"".equals(endTime)){
					beginTime = beginTime+" 00:00:00";
					endTime = endTime+" 23:59:59";
			}
			PunishmentApprovalLeadBean ApprovalLeadBean =new PunishmentApprovalLeadBean();
			ApprovalLeadBean.setOrderId(orderId);
			ApprovalLeadBean.setDealerCode(dealerCode);
			ApprovalLeadBean.setDealerName(dealerName);
			ApprovalLeadBean.setLinkMan(linkMan);
			ApprovalLeadBean.setRewardType(rewardType);
			ApprovalLeadBean.setBeginTime(beginTime);
			ApprovalLeadBean.setEndTime(endTime);
			ApprovalLeadBean.setCompanyId(companyId);
			ApprovalLeadBean.setRewardStatus(rewardStatus);
			Integer curPage = request.getParamValue("curPage") !=null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps = dao.getAllpunishmentApprovalLeadInfo(ApprovalLeadBean,curPage,Constant.PAGE_SIZE );
			act.setOutData("ps", ps);
			act.setForword(ApprovalLeadInit);
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"奖惩审批表售后服务部审核");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * Function       :  奖惩审批表轿车公司审核---查询超链接奖惩详细信息/审核页面详细信息
	 * @param         :  request-工单号
	 * @return        :  奖惩审批信息
	 * @throws        :  Exception
	 * LastUpdate     :  2010-05-19
	 */
	public void getOrderIdInfo(){ 
		RequestWrapper request = act.getRequest();
		String orderId=request.getParamValue("orderId");//工单号
		String Approval=request.getParamValue("Approval");//审核明细
		try {
			PunishmentApprovalLeadBean ApprovalLead=dao.getOrderIdInfo(orderId);
			List<PunishmentApprovalLeadBean> ApprovalLeadList=dao.getOrderIdInfoList(orderId);
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
						 ApprovalLead.setDealerName(dealerName.toString());
					}
			}
			request.setAttribute("ApprovalLead", ApprovalLead);
			request.setAttribute("ApprovalLeadList", ApprovalLeadList);
			dealerName.delete(0, dealerName.length());
			if("Approval".equals(Approval)){//审核页面明细
			 act.setForword(ApprovalLeadInitUpdate);
			}else{
			 act.setForword(ApprovalLeadinfo);
			}
		} catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"奖惩工单详细信息");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * Function       :  审核通过/驳回奖惩细信息
	 * @param         :  request-工单号
	 * @return        :  奖惩审批信息
	 * @throws        :  Exception
	 * LastUpdate     :  2010-05-19
	 */
	public void punishmentApprovalLeadPass(){ 
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
				RewardAuditPO.setAuditStatus(new Integer(Constant.AWARD_PUNISH_STATUS_CAR_PASS));//审批状态:10151005,表示：轿车公司审核通过
				}else{//驳回:returns
				RewardAuditPO.setAuditStatus(new Integer(Constant.AWARD_PUNISH_STATUS_CAR_REJECT));//审批状态:10151006,表示：轿车公司审核驳回
				}
				RewardAuditPO.setAuditContent(auditContent);//审批意见
				RewardAuditPO.setOrgId(logonUser.getOrgId());//组织ID
				dao.punishmentApprovalLeadPass(orderIdArray,RewardAuditPO);
				act.setForword(ApprovalLeadInit);
			}
		  }catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.DELETE_FAILURE_CODE,"售后服务部审核奖惩细信息");
			logger.error(logonUser,e1);
		 	act.setException(e1);
		  }
	}
}