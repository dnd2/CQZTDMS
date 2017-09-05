/**
 * 
 */
package com.infodms.dms.actions.feedbackmng.approve;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.ServiceInfoBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.getCompanyId.GetOemcompanyId;
import com.infodms.dms.dao.feedbackMng.ServiceInfoApplyDao;
import com.infodms.dms.dao.feedbackMng.ServiceInfoApproveDao;
import com.infodms.dms.dao.feedbackMng.ServiceInfoDetailDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtIfServiceInfoAuditPO;
import com.infodms.dms.po.TtIfServiceInfoDetailPO;
import com.infodms.dms.po.TtIfServiceInfoPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.StringUtil;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

/**
 * 
 * <p>Title:ServiceInfoApproveManager.java</p>
 *
 * <p>Description:  服务资料审批表服务总部审批业务逻辑处理</p>
 *
 * <p>Copyright: Copyright (c) 2010</p>
 *
 * <p>Company: www.infoservice.com.cn</p>
 * <p>Date:2010-5-22</p>
 *
 * @author zouchao
 * @version 1.0
 * @remark
 */
public class ServiceInfoApproveManager {
	
	private static Logger logger = Logger.getLogger(ServiceInfoApproveManager.class);
	ActionContext act = ActionContext.getContext();
	AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
	RequestWrapper request = act.getRequest();
	// 审批初始化页面
	private final String serviceInfoApproveInitUrl = "/jsp/feedbackMng/approve/serviceInfoApprove.jsp";
	// 审批详细页面
	private final String serviceInfoApproveEditUrl = "/jsp/feedbackMng/approve/serviceInfoApproveEdit.jsp";
	
	/**
	 * 服务资料审批初始化
	 */
	public void serviceInfoApproveInit(){
		try{
			act.setForword(serviceInfoApproveInitUrl);
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ACTION_NAME_ERROR_CODE,"服务资料售后服务部审核");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
		
	}
	
	
	/**
	 * 服务资料售后服务部审核查询
	 */
	public void queryServiceInfoApprove(){
		
		try {
			//CommonUtils.checkNull() 校验是否为空
			Long companyId=GetOemcompanyId.getOemCompanyId(logonUser);
			String orderId = CommonUtils.checkNull(request.getParamValue("orderId"));  		//取工单号
			String beginTime = CommonUtils.checkNull(request.getParamValue("beginTime"));	//创建开始时间
			String endTime = CommonUtils.checkNull(request.getParamValue("endTime"));		//创建结束时间
			String dealerId = CommonUtils.checkNull(request.getParamValue("dealerId"));	    //取经销商ID
			String mailType = CommonUtils.checkNull(request.getParamValue("mailType"));     //邮费方式
			String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode"));     //供应商代码
			//当开始时间和结束时间相同时
			if(null!=beginTime&&!"".equals(beginTime)&&null!=endTime&&!"".equals(endTime)){
					beginTime = beginTime+" 00:00:00";
					endTime = endTime+" 23:59:59";
			}
			ServiceInfoBean bean = new ServiceInfoBean();
			bean.setOrderId(orderId);
			bean.setBeginTime(beginTime);
			bean.setEndTime(endTime);
			bean.setDealerId(dealerId);
			bean.setMailType(mailType);
			bean.setDealerCode(dealerCode);
			bean.setCompanyId(companyId);
			ServiceInfoApproveDao appDao = new ServiceInfoApproveDao();
			//分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页	
			PageResult<Map<String, Object>> ps = appDao.queryServiceInfoApprove(logonUser,bean,Constant.PAGE_SIZE,curPage);
			//分页方法 end
			
			//向前台传的list 名称是固定的不可改必须用 ps
			act.setOutData("ps", ps);     
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"服务资料售后服务部审核");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
		
	}
	
	/**
	 * 服务资料售后服务部审核
	 */
	public void serviceInfoApprove(){
		try{
			
			String orderId = request.getParamValue("orderId");    //工单号
			
			ServiceInfoApplyDao applyDao = new ServiceInfoApplyDao();
			ServiceInfoApproveDao approveDao = new ServiceInfoApproveDao();
			
			Map<String, Object> infops = applyDao.searchServiceInfoBaseById(orderId);     //查询详细
			List<Map<String, Object>> detailList = applyDao.getInfoDetail(orderId);
			List<Map<String, Object>> list = approveDao.getAuditInfo(orderId);
			
			act.setOutData("ps", infops);
			act.setOutData("detailList", detailList);
			act.setOutData("auditList", list);
			act.setForword(serviceInfoApproveEditUrl);
			
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ACTION_NAME_ERROR_CODE,"服务资料售后服务部审核");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
		
	}
	
	/**
	 * 服务资料审批
	 */
	public void serviceInfoApproveEdit(){
		try {
			String orderId = request.getParamValue("orderId");    //工单号
			String content = CommonUtils.checkNull(request.getParamValue("content"));   // 审批内容
			String flag = request.getParamValue("flag");
			String[] dataIds = request.getParamValues("sure_data_id"); //服务资料ID
			String[] amountStr = request.getParamValues("sure_amount"); //审核同意数量
			String[] priceStr = request.getParamValues("auditsum"); //审核通过时填写的单价
			
			
			ServiceInfoApproveDao approveDao = new ServiceInfoApproveDao();
			ServiceInfoApplyDao infoDao = new ServiceInfoApplyDao();
			ServiceInfoDetailDao detailDao = new ServiceInfoDetailDao();
			
			TtIfServiceInfoAuditPO po = new TtIfServiceInfoAuditPO();
			po.setId(Long.valueOf(SequenceManager.getSequence("")));
			po.setOrderId(orderId);			//工单号
			po.setAuditContent(content);	//审批意见
			po.setAuditDate(new Date(System.currentTimeMillis()));  //审批时间
			
			
			
			if(flag.equals("1")){
				po.setAuditStatus(Constant.SERVICEINFO_VIP_SERVICE_STATUS_PASS);   //总部通过
				//更改工单主表状态
				TtIfServiceInfoPO ts = new TtIfServiceInfoPO();
				TtIfServiceInfoPO tp = new TtIfServiceInfoPO();
				ts.setOrderId(orderId);
				tp.setUpdateBy(logonUser.getUserId());
				tp.setUpdateDate(new Date());
				tp.setStatus(Constant.SERVICEINFO_VIP_SERVICE_STATUS_PASS);
				
				infoDao.update(ts, tp);
				
				// 更改详细表的审批价格与审批数量
				for(int i=0;i<dataIds.length;i++){
					TtIfServiceInfoDetailPO dpo = new TtIfServiceInfoDetailPO();
					dpo.setId(Long.parseLong(dataIds[i]));
					TtIfServiceInfoDetailPO dpo2 = detailDao.select(dpo).get(0) ;
					if(StringUtil.notNull(amountStr[i]))
						dpo2.setAmount(Integer.parseInt(amountStr[i]));
					if(StringUtil.notNull(priceStr[i]))
						dpo2.setPrice(Double.parseDouble(priceStr[i]));
					dpo2.setUpdateBy(logonUser.getUserId());
					dpo2.setUpdateDate(new Date());
					
					detailDao.update(dpo,dpo2);
				}
				/*String str = request.getParamValue("str");
				if(null!=str&&!"".equals(str)){
					String strTemp = str.replaceFirst(",", "");
					String[] strArr = strTemp.split(",");
					for(int i=0;i<strArr.length;i++){
						if(!"".equals(strArr[i])){
							
							String[] strArr2 = strArr[i].split("@");
							TtIfServiceInfoDetailPO dePo = new TtIfServiceInfoDetailPO();
							dePo.setOrderId(orderId);
							dePo.setInfoId(Long.valueOf(strArr2[0]));
							TtIfServiceInfoDetailPO dePo2 = new TtIfServiceInfoDetailPO();
							dePo2.setPrice(Double.valueOf(strArr2[1]));
							Integer amount = 0;
							if(StringUtil.notNull(amountStr))
								amount = Integer.parseInt(amountStr);
							dePo2.setAmount(amount);
							detailDao.update(dePo, dePo2);
						}
					}
				}*/
				
			}else{
				po.setAuditStatus(Constant.SERVICEINFO_VIP_SERVICE_STATUS_REJECT);   //总部驳回
				//更改工单主表状态
				TtIfServiceInfoPO ts = new TtIfServiceInfoPO();
				TtIfServiceInfoPO tp = new TtIfServiceInfoPO();
				ts.setOrderId(orderId);
				tp.setStatus(Constant.SERVICEINFO_VIP_SERVICE_STATUS_REJECT);
				
				infoDao.update(ts, tp);
			}
			po.setAuditBy(logonUser.getUserId());	//审批人
			po.setOrgId(logonUser.getOrgId());		//审批组织ID
			approveDao.insert(po);		            //插入审批明细表
			serviceInfoApproveInit();
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"服务资料售后服务部审核");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
}
