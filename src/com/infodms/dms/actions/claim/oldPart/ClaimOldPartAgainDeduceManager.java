package com.infodms.dms.actions.claim.oldPart;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.ClaimDeductOtherItemListBean;
import com.infodms.dms.bean.ClaimLabourItemListBean;
import com.infodms.dms.bean.ClaimOldPartAgainDeductListBean;
import com.infodms.dms.bean.ClaimOrderBean;
import com.infodms.dms.bean.DeductClaimInfoBean;
import com.infodms.dms.bean.DeductVinInfoBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.common.getCompanyId.GetOemcompanyId;
import com.infodms.dms.dao.claim.oldPart.ClaimOldPartDeduceManagerDao;
import com.infodms.dms.dao.claim.oldPart.ClaimOldPartDeduceNoticeDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

/**
 * 索赔旧件管理--二次抵扣功能
 * @author 赵伦达
 */
public class ClaimOldPartAgainDeduceManager {

	public Logger logger = Logger.getLogger(ClaimOldPartDeduceNoticeManager.class);
	private ActionContext act = null;
	private AclUserBean logonUserBean = null;
	private RequestWrapper request=null;
	private ClaimOldPartDeduceManagerDao deduceDao=null;
	private ClaimOldPartDeduceNoticeDao deduceAgainDao=null;
	
	/** 索赔二次旧件抵扣初始页 */
	private final String SECOND_DEDUCTION_TOP = "/jsp/claim/oldPart/oldPartDeductsec.jsp";
	private final String queryOldPartAgainDeduceUrl = "/jsp/claim/oldPart/againDeduceOldPart.jsp";
	
	/**
	 * Function：索赔旧件二次抵扣--初始化
	 * @param  ：	
	 * @return:		 
	 * @throw：	
	 * LastUpdate：	2010-7-12
	 */
	public void queryListPage(){
		try{
			act = ActionContext.getContext();
			logonUserBean = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Calendar calandar = Calendar.getInstance();
			calandar.add(Calendar.DAY_OF_MONTH, 1-calandar.get(Calendar.DAY_OF_MONTH));
			String startDate = sdf.format(calandar.getTime());
			calandar.add(Calendar.DAY_OF_MONTH, calandar.getActualMaximum(Calendar.DAY_OF_MONTH)-1);
			String endDate = sdf.format(calandar.getTime());
			
			act.setOutData("startDate", startDate);
			act.setOutData("endDate", endDate);
			act.setForword(this.SECOND_DEDUCTION_TOP);
		} catch (Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"索赔旧件二次抵扣--初始化");
			logger.error(logonUserBean,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * Function：索赔旧件二次抵扣--查询索赔申请单信息
	 * @param  ：	
	 * @return:		 
	 * @throw：	
	 * LastUpdate：	2010-7-12
	 */
	public void queryDeductClaim(){
	    act = ActionContext.getContext();
	    logonUserBean = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
	    Long companyId=GetOemcompanyId.getOemCompanyId(logonUserBean);
	   
	    deduceAgainDao=ClaimOldPartDeduceNoticeDao.getInstance();
		try{
			//取得查询参数
			request = act.getRequest();
			String dealerCode = request.getParamValue("dealerCode");//经销商代码（多个）
			String dealerName = request.getParamValue("DEALER_NAME");//经销商名称
			String claimNo = request.getParamValue("CLAIM_NO");//索赔申请单号
			String roNo = request.getParamValue("RO_NO");//工单号
			String lineNo = request.getParamValue("LINE_NO");//行号
			String claimType = request.getParamValue("CLAIM_TYPE");//索赔类型
			String vin = request.getParamValue("VIN");//车辆唯一标识码
			String applyStartDate = request.getParamValue("APPLY_DATE_START");//申请日期范围（开始时间）
			String applyEndDate = request.getParamValue("APPLY_DATE_END");//申请日期范围（结束时间）
			String claimStatus = request.getParamValue("CLAIM_STATUS");//状态 固定为 "已结算"
			
			String barcodeNo = request.getParamValue("barcode_no");//状态 固定为 "已结算"
			String yieldlys = CommonUtils.findYieldlyByPoseId(logonUserBean.getPoseId());       //该用户拥有的产地权限
			
			Integer curPage = request.getParamValue("curPage")==null?1
					:Integer.parseInt(request.getParamValue("curPage"));//分页首页代码
			
			if(Utility.testString(applyStartDate))
				applyStartDate = applyStartDate + " 00:00:00";
			
			if(Utility.testString(applyEndDate))
				applyEndDate = applyEndDate + " 23:59:59";
			
			ClaimOrderBean orderBean = new ClaimOrderBean();
			orderBean.setDealerCodes(dealerCode);
			orderBean.setDealerName(dealerName);
			orderBean.setClaimNo(claimNo);
			orderBean.setRoNo(roNo);
			orderBean.setLineNo(lineNo);
			orderBean.setClaimType(claimType);
			orderBean.setVin(vin);
			orderBean.setApplyEndDate(applyEndDate);
			orderBean.setApplyStartDate(applyStartDate);
			orderBean.setClaimStatus(claimStatus);
			orderBean.setCompanyId(companyId);
			orderBean.setYieldlys(yieldlys);
			orderBean.setBarcodeNo(barcodeNo);
			//查询索赔申请单
			PageResult<Map<String,Object>> result = deduceAgainDao.queryDeductClaim(orderBean,curPage,
															Constant.PAGE_SIZE);
			
			act.setOutData("ps", result);
		} catch (Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"索赔旧件二次抵扣--查询索赔申请单信息");
			logger.error(logonUserBean,e1);
			act.setException(e1);
		}	
	}
	/**
	 * Function：索赔旧件二次抵扣--查询索赔单是否已结算
	 * @param  ：	
	 * @return:		 
	 * @throw：	
	 * LastUpdate：	2010-6-25
	 */
	@SuppressWarnings("unchecked")
	public void isBalanceOper(){
		try {
			act=ActionContext.getContext();
			request = act.getRequest();
			logonUserBean=(AclUserBean) act.getSession().get(Constant.LOGON_USER);
			deduceDao=ClaimOldPartDeduceManagerDao.getInstance();
			Map params=new HashMap();
			params.put("claim_no", request.getParamValue("claim_no"));
			String retCode=deduceDao.getIsBalanceFlag(params);
			act.setOutData("isBalance", retCode);//retCode为1代表已结算，0代表为未结算
			act.setOutData("claim_no", request.getParamValue("claim_no"));
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔旧件二次抵扣--查询索赔单是否已结算");
			logger.error(logonUserBean, e1);
			act.setException(e1);
		}
	}
	/**
	 * Function：索赔旧件二次抵扣--抵扣展现
	 * @param  ：	
	 * @return:		 
	 * @throw：	
	 * LastUpdate：	2010-6-25
	 */
	@SuppressWarnings("unchecked")
	public void deducePreViewInfo(){
		try {
			
			act=ActionContext.getContext();
			request = act.getRequest();
			logonUserBean=(AclUserBean) act.getSession().get(Constant.LOGON_USER);
			deduceDao=ClaimOldPartDeduceManagerDao.getInstance();
			deduceAgainDao=ClaimOldPartDeduceNoticeDao.getInstance();
			Map params=new HashMap<String,String>();
			params.put("claim_no", request.getParamValue("claim_no"));//索赔单号
			//获得索赔单明细
			DeductClaimInfoBean claimInfoBean=deduceAgainDao.getDeductClaimInfo(params);
			request.setAttribute("claimInfoBean", claimInfoBean);
			//通过vin获得车辆信息
			DeductVinInfoBean vinBean=deduceAgainDao.getVinInfo(params);
			request.setAttribute("vinBean", vinBean);
			//获得索赔单申请内容
			//TtAsWrApplicationExtPO tawep=deduceDao.queryApplicationDetailById(params);
			//request.setAttribute("application", tawep);
			//获得索赔配件抵扣列表
			List<ClaimOldPartAgainDeductListBean> deductPartList=deduceAgainDao.getClaimOldPartDeductInfoList(params);
			act.setOutData("deductPartList", deductPartList);
			//获得索赔工时列表
			List<ClaimLabourItemListBean> deductHourList=deduceAgainDao.getClaimLabourItemList(params);
			act.setOutData("deductHourList", deductHourList);
			//获得索赔其他项目列表
			List<ClaimDeductOtherItemListBean> deductOtherList=deduceAgainDao.getClaimDeductOtherItemList(params);
			act.setOutData("deductOtherList", deductOtherList);
			request.setAttribute("claim_back_id",request.getParamValue("claim_back_id"));
			act.setForword(queryOldPartAgainDeduceUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔旧件二次抵扣--抵扣展现");
			logger.error(logonUserBean, e1);
			act.setException(e1);
		}
	}
	/**
	 * Function：索赔旧件二次抵扣--二次抵扣操作
	 * @param  ：	
	 * @return:		 
	 * @throw：	
	 * LastUpdate：	2010-6-28
	 */
	public void againDeductOper(){
		try {
			act=ActionContext.getContext();
			request = act.getRequest();
			logonUserBean=(AclUserBean) act.getSession().get(Constant.LOGON_USER);
			deduceAgainDao=ClaimOldPartDeduceNoticeDao.getInstance();
			Long companyId=GetOemcompanyId.getOemCompanyId(logonUserBean);
			String retCode=deduceAgainDao.againDeDuctDataOper(request,logonUserBean.getUserId(),companyId);
			act.setOutData("retCode", retCode);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔旧件二次抵扣--二次抵扣操作");
			logger.error(logonUserBean, e1);
			act.setException(e1);
		}
	}
}
