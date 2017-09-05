/**********************************************************************
* <pre>
* FILE : PunishmentApplyDealerMantain.java
* CLASS : PunishmentApplyDealerMantain
*
* AUTHOR : PGM
*
* FUNCTION : 奖惩审批表查询(经销商端).
*
*
*======================================================================
* CHANGE HISTORY LOG
*----------------------------------------------------------------------
* MOD. NO.| DATE     | NAME | REASON | CHANGE REQ.
*----------------------------------------------------------------------
*         |2010-05-21| PGM  | Created |
* DESCRIPTION:
* </pre>
***********************************************************************/
/**
 * $Id: PunishmentApplyDealerMantain.java,v 1.1 2010/08/16 01:43:18 yuch Exp $
 */
package com.infodms.dms.actions.feedbackmng.query;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.PunishmentApplyDealerMantainBean;
import com.infodms.dms.bean.PunishmentApplyMantainBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.feedbackMng.PunishmentApplyDealerMantainDao;
import com.infodms.dms.dao.feedbackMng.PunishmentApplyMantainDao;
import com.infodms.dms.exception.BizException;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

/**
 * Function       :  奖惩审批表查询(经销商端)
 * @author        :  PGM
 * CreateDate     :  2010-05-21
 * @version       :  0.1
 */
public class PunishmentApplyDealerMantain {
	private Logger logger = Logger.getLogger(PunishmentApplyDealerMantain.class);
	private PunishmentApplyDealerMantainDao dao = PunishmentApplyDealerMantainDao.getInstance();
	private PunishmentApplyMantainDao punishmentDao = PunishmentApplyMantainDao.getInstance();
	private ActionContext act = ActionContext.getContext();//获取ActionContext
	private AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
	private final String DealerMantainInit = "/jsp/feedbackMng/query/punishmentApplyDealerSearch.jsp";//查询页面
	private final String DealerMantainInfo = "/jsp/feedbackMng/query/punishmentDealerDetail.jsp";//详细页面
	
	/**
	 * Function       :  奖惩审批表查询页面初始化
	 * @param         :  
	 * @return        :  奖惩审批表
	 * @throws        :  Exception
	 * LastUpdate     :  2010-05-21
	 */
	public void punishmentApplyDealerMantainInit(){
		try {
			act.setForword(DealerMantainInit);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"奖惩审批表");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * Function       :  根据条件查询奖惩审批表中符合条件的信息，其中包括：服务大区已上报、售后服务部审核通过、售后服务部审核驳回、轿车公司审核通过、轿车公司审核驳回
	 * @param         :  request-工单号、经销商名称、提报开始时间、提报结束始时间、类型、申请单位（默认系统登录人）
	 * @return        :  奖惩审批信息
	 * @throws        :  Exception
	 * LastUpdate     :  2010-05-21
	 */
	public void punishmentApplyDealerMantainQuery(){
		try {
			RequestWrapper request = act.getRequest();
			String orderId = request.getParamValue("orderId");             //工单号
			String dealerName = request.getParamValue("dealerName");       //经销商名称
			String beginTime = request.getParamValue("beginTime");         //提报开始时间
			String endTime = request.getParamValue("endTime");             //提报结束始时间
			String rewardType = request.getParamValue("rewardType");       //类型
			String linkMan=request.getParamValue("name");               //申请单位（默认系统登录人）
			String userId=request.getParamValue("userId"); //ID
//			Long  dealerId=logonUser.getUserId();//经销商ID
			String  dealerId=logonUser.getDealerId();
			//当开始时间和结束时间相同时
			if(null!=beginTime&&!"".equals(beginTime)&&null!=endTime&&!"".equals(endTime)){
					beginTime = beginTime+" 00:00:00";
					endTime = endTime+" 23:59:59";
			}
			PunishmentApplyDealerMantainBean DealerMantainBean = new PunishmentApplyDealerMantainBean();
			DealerMantainBean.setOrderId(orderId);
			DealerMantainBean.setDealerName(dealerName);
			DealerMantainBean.setBeginTime(beginTime);
			DealerMantainBean.setEndTime(endTime);
			DealerMantainBean.setRewardType(rewardType);
			DealerMantainBean.setLinkMan(linkMan);
			DealerMantainBean.setUserId(userId);
			if(!"".equals(dealerId)&&null!=dealerId){
			DealerMantainBean.setDealerId(dealerId);
			}
			Integer curPage = request.getParamValue("curPage") !=null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps = dao.getAllPunishmentApplyDealerMantainInfo(DealerMantainBean,curPage,Constant.PAGE_SIZE );
			act.setOutData("ps", ps);
			act.setForword(DealerMantainInit);
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"奖惩审批查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * Function       :  查询奖惩详细信息
	 * @param         :  request-工单号
	 * @return        :  奖惩审批信息
	 * @throws        :  Exception
	 * LastUpdate     :  2010-05-21
	 */
	public void getOrderIdInfo(){ 
		RequestWrapper request = act.getRequest();
		String orderId=request.getParamValue("orderId");//工单号
		try {
			PunishmentApplyDealerMantainBean DealerMantainBean=dao.getOrderIdInfo(orderId);
			List<PunishmentApplyDealerMantainBean> DealerMantainList=dao.getOrderIdInfoList(orderId);
			List<PunishmentApplyMantainBean> dealerList = punishmentDao.getDealerName(orderId);
			StringBuffer dealerName = new StringBuffer("");
			if(null!=dealerList&&dealerList.size()!=0){
					for(PunishmentApplyMantainBean  dealerNameBean:dealerList){
						if(null!=dealerNameBean.getDealerName()){
							dealerName.append(dealerNameBean.getDealerName()+",");
						}
					}
					if(null!=dealerName){
						 dealerName.deleteCharAt(dealerName.length()-1);
						 DealerMantainBean.setDealerName(dealerName.toString());
					}
			}
			request.setAttribute("DealerMantainBean", DealerMantainBean);
			request.setAttribute("DealerMantainList", DealerMantainList);
			dealerName.delete(0, dealerName.length());
			act.setForword(DealerMantainInfo);
		} catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"奖惩工单详细信息");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
}