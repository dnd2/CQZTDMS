/**********************************************************************
* <pre>
* FILE : PunishmentApplyCarMantain.java
* CLASS : PunishmentApplyCarMantain
*
* AUTHOR : PGM
*
* FUNCTION : 奖惩审批表查询（车厂端）.
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
 * $Id: PunishmentApplyCarMantain.java,v 1.1 2010/08/16 01:43:17 yuch Exp $
 */
package com.infodms.dms.actions.feedbackmng.query;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.PunishmentApplyCarMantainBean;
import com.infodms.dms.bean.PunishmentApplyMantainBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.getCompanyId.GetOemcompanyId;
import com.infodms.dms.dao.feedbackMng.PunishmentApplyCarMantainDao;
import com.infodms.dms.dao.feedbackMng.PunishmentApplyMantainDao;
import com.infodms.dms.exception.BizException;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

/**
 * Function       :  奖惩审批表查询（车厂端）
 * @author        :  PGM
 * CreateDate     :  2010-05-21
 * @version       :  0.1
 */
public class PunishmentApplyCarMantain {
	private Logger logger = Logger.getLogger(PunishmentApplyCarMantain.class);
	private PunishmentApplyCarMantainDao dao = PunishmentApplyCarMantainDao.getInstance();
	private PunishmentApplyMantainDao punishdao = PunishmentApplyMantainDao.getInstance();
	private ActionContext act = ActionContext.getContext();//获取ActionContext
	private AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
	private final String CarMantainInit = "/jsp/feedbackMng/query/punishmentApplySearch.jsp";//查询页面
	private final String CarMantaininfo = "/jsp/feedbackMng/query/punishmentCarDetail.jsp";//详细页面
	
	/**
	 * Function       :  奖惩审批表查询页面初始化
	 * @param         :  
	 * @return        :  奖惩审批表
	 * @throws        :  Exception
	 * LastUpdate     :  2010-05-21
	 */
	public void punishmentApplyCarMantainInit(){
		try {
			act.setForword(CarMantainInit);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"奖惩审批表");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * Function       :  根据条件查询奖惩审批表中符合条件的信息，其中包括：服务大区已上报、售后服务部审核通过、售后服务部审核驳回、轿车公司审核通过、轿车公司审核驳回
	 * @param         :  request-工单号、经销商ID、经销商名称、提报开始时间、提报结束始时间、类型、申请单位（默认系统登录人）
	 * @return        :  奖惩审批信息
	 * @throws        :  Exception
	 * LastUpdate     :  2010-05-21
	 */
	public void punishmentApplyCarMantainQuery(){
		try {
			RequestWrapper request = act.getRequest();
			logonUser=(AclUserBean) act.getSession().get(Constant.LOGON_USER);
			Long companyId=GetOemcompanyId.getOemCompanyId(logonUser);
			String orderId = request.getParamValue("orderId");             //工单号
			String dealerCode = request.getParamValue("dealerCode");          //经销商代码
			String dealerName = request.getParamValue("dealerName");        //经销商名称
			String beginTime = request.getParamValue("beginTime");         //提报开始时间
			String endTime = request.getParamValue("endTime");             //提报结束始时间
			String rewardType = request.getParamValue("rewardType");       //类型
			String linkMan=request.getParamValue("name");               //申请单位（默认系统登录人）
			PunishmentApplyCarMantainBean CarMantainBean =new PunishmentApplyCarMantainBean();
			//当开始时间和结束时间相同时
			if(null!=beginTime&&!"".equals(beginTime)&&null!=endTime&&!"".equals(endTime)){
					beginTime = beginTime+" 00:00:00";
					endTime = endTime+" 23:59:59";
			}
			CarMantainBean.setOrderId(orderId);
			CarMantainBean.setDealerCode(dealerCode);
			CarMantainBean.setBeginTime(beginTime);
			CarMantainBean.setEndTime(endTime);
			CarMantainBean.setCompanyId(companyId);
			CarMantainBean.setRewardType(rewardType);
			CarMantainBean.setLinkMan(linkMan);
			CarMantainBean.setDealerName(dealerName);
			Integer curPage = request.getParamValue("curPage") !=null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps = dao.getAllpunishmentApplyCarMantainInfo(CarMantainBean,curPage,Constant.PAGE_SIZE );
			act.setOutData("ps", ps);
			act.setForword(CarMantainInit);
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
	 * LastUpdate     :  2010-08-12
	 */
	public void getOrderIdInfo(){ 
		RequestWrapper request = act.getRequest();
		String orderId=request.getParamValue("orderId");//工单号
		try {
			PunishmentApplyCarMantainBean MantainBean=dao.getOrderIdInfo(orderId);
			List<PunishmentApplyCarMantainBean> CarMantainList=dao.getOrderIdInfoList(orderId);
			List<PunishmentApplyMantainBean> dealerList = punishdao.getDealerName(orderId);
			StringBuffer dealerName = new StringBuffer("");
			if(null!=dealerList&&dealerList.size()!=0){
					for(PunishmentApplyMantainBean  dealerNameBean:dealerList){
						if(null!=dealerNameBean.getDealerName()){
							dealerName.append(dealerNameBean.getDealerName()+",");
						}
					}
					if(null!=dealerName){
						 dealerName.deleteCharAt(dealerName.length()-1);
						 MantainBean.setDealerName(dealerName.toString());
					}
			}
			request.setAttribute("MantainBean", MantainBean);
			request.setAttribute("CarMantainList", CarMantainList);
			dealerName.delete(0, dealerName.length());
			act.setForword(CarMantaininfo);
		} catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"奖惩工单详细信息");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
}