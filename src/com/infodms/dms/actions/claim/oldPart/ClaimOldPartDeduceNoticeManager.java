package com.infodms.dms.actions.claim.oldPart;

import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.DeduceNoticeBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.common.getCompanyId.GetOemcompanyId;
import com.infodms.dms.dao.claim.oldPart.ClaimOldPartDeduceNoticeDao;
import com.infodms.dms.exception.BizException;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

/**
 * 旧件抵扣通知单查询
 * @author XZM
 */
public class ClaimOldPartDeduceNoticeManager {
	
	public Logger logger = Logger.getLogger(ClaimOldPartDeduceNoticeManager.class);
	/** 旧件抵扣通知单查询首页 */
	private final String DEDUCTION_NOTICE_TOP = "/jsp/claim/oldPart/oldPartDeductionForm.jsp";
	
	/**
	 * 呈现旧件抵扣通知单查询首页
	 */
	public void queryListPage(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try{
			act.setForword(this.DEDUCTION_NOTICE_TOP);
		} catch (Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"旧件抵扣通知单");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 查询经销商对应的抵扣单
	 * 注：根据登陆用户确定用户所属经销商，同时并查询对应经销商的抵扣单
	 */
	public void queryOldPartDeduceNotice(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		ClaimOldPartDeduceNoticeDao noticeDao = ClaimOldPartDeduceNoticeDao.getInstance();
		Long companyId=GetOemcompanyId.getOemCompanyId(logonUser);
		try{
			//取得查询参数
			RequestWrapper request = act.getRequest();
			String transportNO = request.getParamValue("TRANSPORT_NO");//抵扣单号
			String noticeDateS = request.getParamValue("NOTICE_DATE_START");//抵扣通知时间范围（开始）
			String noticeDateE = request.getParamValue("NOTICE_DATE_END");//抵扣通知时间范围（结束）
			String dealerId = logonUser.getDealerId();//经销商ID
			String deductStatus = request.getParamValue("DEDUCT_STATUS");//抵扣单状态
			Integer curPage = request.getParamValue("curPage")==null?1
					:Integer.parseInt(request.getParamValue("curPage"));//分页首页代码
			
			if(Utility.testString(noticeDateS))
				noticeDateS = noticeDateS + " 00:00:00";
			if(Utility.testString(noticeDateE))
				noticeDateE = noticeDateE + " 23:59:59";
			
			DeduceNoticeBean conditionBean = new DeduceNoticeBean();
			conditionBean.setTransportNO(transportNO);
			conditionBean.setNoticeDateE(noticeDateE);
			conditionBean.setNoticeDateS(noticeDateS);
			conditionBean.setDealerId(dealerId);
			conditionBean.setDeductStatus(deductStatus);
			conditionBean.setCompanyId(companyId);
			//查询抵扣通知单
			PageResult<Map<String,Object>> result = noticeDao.queryOldPartDeduceNotice(conditionBean,
					curPage, Constant.PAGE_SIZE);
			
			act.setOutData("ps", result);
		} catch (Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"旧件抵扣通知单");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 查询某一抵扣单明细
	 */
	public void queryDeduceNoticeDetail(){
		
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		ClaimOldPartDeduceNoticeDao noticeDao = ClaimOldPartDeduceNoticeDao.getInstance();
		try{
			//取得查询参数
			RequestWrapper request = act.getRequest();
			String deduceId = request.getParamValue("ID");//抵扣单ID
			Integer curPage = request.getParamValue("curPage")==null?1
					:Integer.parseInt(request.getParamValue("curPage"));//分页首页代码
			
			//查询抵扣通知单
			PageResult<Map<String,Object>> result = noticeDao.queryDeduceNoticeDetail(deduceId,
					curPage, Constant.PAGE_SIZE);
			act.setOutData("ps", result);
		} catch (Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"旧件抵扣通知单明细");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
}
