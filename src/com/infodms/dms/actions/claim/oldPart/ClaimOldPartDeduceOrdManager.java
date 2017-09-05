package com.infodms.dms.actions.claim.oldPart;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.DeduceNoticeBean;
import com.infodms.dms.bean.DeductClaimInfoBean;
import com.infodms.dms.bean.DeductClaimInfoListBean;
import com.infodms.dms.bean.DeductDetailListBean;
import com.infodms.dms.bean.DeductVinInfoBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.common.getCompanyId.GetOemcompanyId;
import com.infodms.dms.dao.claim.oldPart.ClaimOldPartDeduceNoticeDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

/**
 * 类说明：索赔旧件管理--索赔旧件抵扣单维护
 * 作者：  赵伦达、XZM
 */
public class ClaimOldPartDeduceOrdManager {
	public Logger logger = Logger.getLogger(ClaimOldPartDeduceOrdManager.class);
	private ActionContext act = null;
	private AclUserBean logonUserBean = null;
	private RequestWrapper request=null;
	private ClaimOldPartDeduceNoticeDao noticeDao=null;
	/** 抵扣单维护查询首页 */
	private final String DEDUCTION_NOTICE_TOP = "/jsp/claim/oldPart/oldPartDeducts.jsp";
	private final String deduct_claim_maintain = "/jsp/claim/oldPart/deductClaimListMaintain.jsp";
	private final String deduct_info_maintain = "/jsp/claim/oldPart/deductInfoListMaintain.jsp";
	/**
	 * Function：索赔旧件抵扣单维护--初始化
	 * @param  ：	
	 * @return:		 
	 * @throw：	
	 * LastUpdate：	2010-7-12
	 */
	public void queryListPage(){
		act = ActionContext.getContext();
		logonUserBean = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try{
			act.setForword(this.DEDUCTION_NOTICE_TOP);
		} catch (Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"索赔旧件抵扣单维护--初始化");
			logger.error(logonUserBean,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * Function：索赔旧件抵扣单维护--条件查询
	 * @param  ：	
	 * @return:		 
	 * @throw：	
	 * LastUpdate：	2010-7-12
	 */
	public void queryOldPartDeduce(){
		act = ActionContext.getContext();
		logonUserBean = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		Long companyId=GetOemcompanyId.getOemCompanyId(logonUserBean);
		noticeDao = ClaimOldPartDeduceNoticeDao.getInstance();
		try{
			//取得查询参数
			request = act.getRequest();
			String transportNO = request.getParamValue("TRANSPORT_NO");//抵扣单号
			String noticeDateS = request.getParamValue("NOTICE_DATE_START");//抵扣通知时间范围（开始）
			String noticeDateE = request.getParamValue("NOTICE_DATE_END");//抵扣通知时间范围（结束）
			String dealerCodes = request.getParamValue("dealerCode");//经销商代码集合（以","分隔）
			String dealerName = request.getParamValue("dealerName");//经销商名称
			String deductStatus = request.getParamValue("DEDUCT_STATUS");//抵扣单状态
			String yieldlys = CommonUtils.findYieldlyByPoseId(logonUserBean.getPoseId());       //该用户拥有的产地权限
			
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
			conditionBean.setDealerCodes(dealerCodes);
			conditionBean.setDealerName(dealerName);
			conditionBean.setDeductStatus(deductStatus);
			conditionBean.setCompanyId(companyId);
			conditionBean.setYieldlys(yieldlys);
			
			//查询抵扣通知单
			PageResult<Map<String,Object>> result = noticeDao.queryOldPartDeduce(conditionBean,
					curPage, Constant.PAGE_SIZE);
			
			act.setOutData("ps", result);
		} catch (Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"索赔旧件抵扣单维护--条件查询");
			logger.error(logonUserBean,e1);
			act.setException(e1);
		}
	}
	/**
	 * Function：索赔旧件抵扣单维护--维护功能
	 *           根据抵扣单信息查询相应索赔单信息
	 * @param  ：
	 * @return:		 
	 * @throw：	
	 * LastUpdate：	2010-6-26
	 */
	@SuppressWarnings("unchecked")
	public void queryDeductOrdMaintainInfo(){
		try {
			act=ActionContext.getContext();
			request = act.getRequest();
			logonUserBean=(AclUserBean) act.getSession().get(Constant.LOGON_USER);
			noticeDao = ClaimOldPartDeduceNoticeDao.getInstance();
			Integer curPage = request.getParamValue("curPage")==null?1
					:Integer.parseInt(request.getParamValue("curPage"));//分页首页代码
			Map params=new HashMap();
			params.put("deduct_id",request.getParamValue("deduct_id"));
			//获得抵扣索赔明细列表
			PageResult<DeductClaimInfoListBean> ps=noticeDao.getClaimDeductOrdInfo(params,curPage, Constant.PAGE_SIZE);
			request.setAttribute("deduct_id",request.getParamValue("deduct_id"));
			act.setOutData("ps", ps);
			act.setForword(deduct_claim_maintain);
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔旧件抵扣单维护--维护功能");
			logger.error(logonUserBean, e1);
			act.setException(e1);
		}
	}
	/**
	 * Function：索赔旧件抵扣单维护--抵扣详细信息查询
	 * @param  ：	
	 * @return:		 
	 * @throw：	
	 * LastUpdate：	2010-6-26
	 */
	@SuppressWarnings("unchecked")
	public void queryDeductInfoById(){
		try {
			act=ActionContext.getContext();
			request = act.getRequest();
			logonUserBean=(AclUserBean) act.getSession().get(Constant.LOGON_USER);
			noticeDao = ClaimOldPartDeduceNoticeDao.getInstance();
			Map params=new HashMap();
			params.put("deduct_id",request.getParamValue("deduct_id"));//参数--抵扣单主键
			params.put("claim_no",request.getParamValue("claim_no"));//参数--索赔单工单号
			//获得索赔单明细
			DeductClaimInfoBean claimInfoBean=noticeDao.getDeductClaimInfo(params);
			request.setAttribute("claimInfoBean", claimInfoBean);
			//通过vin获得车辆信息
			DeductVinInfoBean vinBean=noticeDao.getVinInfo(params);
			request.setAttribute("vinBean", vinBean);
			//获得索赔配件抵扣列表
			List<DeductDetailListBean> deductPartList=noticeDao.getOldPartDeductInfoList(params,Constant.PRE_AUTH_ITEM_02);
			act.setOutData("deductPartList", deductPartList);
			//获得索赔工时列表
			List<DeductDetailListBean> deductHourList=noticeDao.getOldPartDeductInfoList(params,Constant.PRE_AUTH_ITEM_01);
			act.setOutData("deductHourList", deductHourList);
			//获得索赔其他项目列表
			List<DeductDetailListBean> deductOtherList=noticeDao.getOldPartDeductInfoList(params,Constant.PRE_AUTH_ITEM_03);
			act.setOutData("deductOtherList", deductOtherList);
			request.setAttribute("deduct_main_id", request.getParamValue("deduct_id"));//抵扣主表id返回给抵扣页面
			act.setForword(deduct_info_maintain);
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔旧件抵扣单维护--抵扣详细信息查询");
			logger.error(logonUserBean, e1);
			act.setException(e1);
		}
	}
	/**
	 * Function：索赔旧件抵扣单维护--抵扣操作
	 * @param  ：	
	 * @return:		 
	 * @throw：	
	 * LastUpdate：	2010-6-28
	 */
	@SuppressWarnings("unchecked")
	public void deductOper(){
		try {
			act=ActionContext.getContext();
			request = act.getRequest();
			logonUserBean=(AclUserBean) act.getSession().get(Constant.LOGON_USER);
			noticeDao = ClaimOldPartDeduceNoticeDao.getInstance();
			Map map=noticeDao.deductMaintainDataOper(request,logonUserBean.getUserId());
			act.setOutData("retCode", map.get("retCode"));
			act.setOutData("part_total_amount", map.get("part_total_amount"));
			act.setOutData("hour_total_amount", map.get("hour_total_amount"));
			act.setOutData("other_total_amount", map.get("other_total_amount"));
			act.setOutData("failure_item_code", map.get("failure_item_code"));
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔旧件抵扣单维护--抵扣操作");
			logger.error(logonUserBean, e1);
			act.setException(e1);
		}
	}
	/**
	 * Function：索赔旧件抵扣单维护--取消抵扣操作
	 * @param  ：	
	 * @return:		 
	 * @throw：	
	 * LastUpdate：	2010-6-28
	 */
	@SuppressWarnings("unchecked")
	public void delDeductInfoOper(){
		try {
			act=ActionContext.getContext();
			request = act.getRequest();
			logonUserBean=(AclUserBean) act.getSession().get(Constant.LOGON_USER);
			noticeDao = ClaimOldPartDeduceNoticeDao.getInstance();
			Map params=new HashMap();
			params.put("deduct_id",request.getParamValue("deduct_id"));
			params.put("logonUserBean",logonUserBean);
			String retCode=noticeDao.delSingleDeductInfoDataOper(params);
			act.setOutData("retCode",retCode);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔旧件抵扣单维护--取消抵扣操作");
			logger.error(logonUserBean, e1);
			act.setException(e1);
		}
	}
}
