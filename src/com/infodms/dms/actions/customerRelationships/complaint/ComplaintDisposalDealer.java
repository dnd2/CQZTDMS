package com.infodms.dms.actions.customerRelationships.complaint;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.feedbackmng.InfoFeedBackMng;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.ComplaintDisposalBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.customerRelationships.ComplaintAuditDao;
import com.infodms.dms.dao.customerRelationships.ComplaintDisposalDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtCrComplaintsAuditPO;
import com.infodms.dms.po.TtCrComplaintsPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

/**
 * 
 * <p>ComplaintDisposalDealer.java</p>
 *
 * <p>Description: 客户投诉处理(服务站)</p>
 *
 * <p>Copyright: Copyright (c) 2010</p>
 *
 * <p>Company: www.infoservice.com.cn</p>
 * <p>Date:2010-6-7</p>
 *
 * @author zouchao
 * @version 1.0
 * @remark
 */
public class ComplaintDisposalDealer {
	
	private static Logger logger = Logger.getLogger(ComplaintDisposalDealer.class);
	ActionContext act = ActionContext.getContext();
	AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
	RequestWrapper request = act.getRequest();
	
	// 客户投诉处理(服务站)初始化页面
	private final String complainDisposalDealerInitUrl = "/jsp/customerRelationships/complaintDisposal/complaintDisposalDealer.jsp";
	
	
	// 客户投诉处理(服务站)处理页面
	private final String complainDisposalDealerAssignUrl = "/jsp/customerRelationships/complaintDisposal/complaintDisposalAssignDealer.jsp";
	
	
	/**
	 * 客户投诉处理(服务站)初始化
	 */
	public void complaintDisposalDealerInit(){
		try{
			act.setForword(complainDisposalDealerInitUrl);
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ACTION_NAME_ERROR_CODE,"客户投诉处理(服务站)");
			logger.error(logonUser,e1);
			act.setException(e1);
		}

	}
	
	/**
	 * 客户投诉处理(服务站)查询
	 */
	public void queryComplaintDisposalDealer(){
		try{
			//CommonUtils.checkNull() 校验是否为空
			String linkman = CommonUtils.checkNull(request.getParamValue("linkman"));  				//客户姓名
			String tel = CommonUtils.checkNull(request.getParamValue("tel"));	           	 		//客户电话
			String beginTime = CommonUtils.checkNull(request.getParamValue("beginTime"));   		//投诉时间
			String endTime = CommonUtils.checkNull(request.getParamValue("endTime"));       		//投诉时间
			String modelCode = CommonUtils.checkNull(request.getParamValue("modelCode"));   		//车型
			String ownOrgId = CommonUtils.checkNull(request.getParamValue("ownOrgId"));     		//所属区域
			String compDealerId = CommonUtils.checkNull(request.getParamValue("compDealerId")); 	//投诉经销商ID
			String compDealerCode = CommonUtils.checkNull(request.getParamValue("compDealerCode")); //投诉经销商code
			String compSource = CommonUtils.checkNull(request.getParamValue("compSource")); 		//投诉来源
			String compLevel = CommonUtils.checkNull(request.getParamValue("compLevel"));   		//投诉等级
			String compType = CommonUtils.checkNull(request.getParamValue("compType"));     		//投诉类型
			String compType2 = CommonUtils.checkNull(request.getParamValue("compType2"));     		//投诉类型2
			String compStatus = CommonUtils.checkNull(request.getParamValue("compStatus")); 		//投诉状态
			String auditResult = CommonUtils.checkNull(request.getParamValue("auditResult")); 		//处理中状态
			String licenseNo = CommonUtils.checkNull(request.getParamValue("licenseNo")); 			//车牌号
			String dealerId = logonUser.getDealerId();
			String compCode = CommonUtils.checkNull(request.getParamValue("compCode")); 			//投诉编号
			ComplaintDisposalBean cdbean = new ComplaintDisposalBean();
			cdbean.setLinkman(linkman);
			cdbean.setTel(tel);
			cdbean.setBeginTime(beginTime);
			cdbean.setEndTime(endTime);
			cdbean.setModelCode(modelCode);
			cdbean.setOwnOrgId(ownOrgId);
			cdbean.setCompDealerId(compDealerId);
			cdbean.setCompDealerCode(compDealerCode);
			cdbean.setCompSource(compSource);
			cdbean.setCompLevel(compLevel);
			cdbean.setCompType(compType);
			cdbean.setCompType2(compType2);
			cdbean.setStatus(compStatus);
			cdbean.setDealerId(dealerId);
			cdbean.setAuditResult(auditResult);
			cdbean.setLicenseNo(licenseNo);
			cdbean.setCompCode(compCode);
			ComplaintDisposalDao cddao = new ComplaintDisposalDao();
			//分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage"))	: 1; // 处理当前页	
				
			PageResult<Map<String, Object>> ps = cddao.queryComplaintDisposal(cdbean,Constant.PAGE_SIZE,curPage);
			//分页方法 end
			
			//向前台传的list 名称是固定的不可改必须用 ps
			act.setOutData("ps", ps);     
			
		}catch(Exception e) {
			//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"客户投诉处理(服务站)");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	

	
	
	
	
	
	/**
	 * 客户投诉处理(服务站)处理初始化
	 */
	public void disposalComplaintDealer(){
		try{
			String compId = CommonUtils.checkNull(request.getParamValue("compId"));
			
			ComplaintDisposalDao cddao = new ComplaintDisposalDao();
			
			// 取得客户投诉表的基本信息
			Map<String, Object> complaintMap = cddao.getComplaintById(compId); 
			
			ComplaintAuditDao  cadao = new ComplaintAuditDao();
			
			Map<String, Object> detailMap =null;
			String auditAction = "";
			
			// 根据取得的compId
			detailMap = cadao.getAuditDetailByCompId(compId,String.valueOf(Constant.AUDIT_STATUS_TYPE_03));
			if(null==detailMap){
				detailMap = cadao.getAuditDetailByCompId(compId,String.valueOf(Constant.AUDIT_STATUS_TYPE_02));
			}
			
			if(null!=detailMap&&detailMap.size()!=0){
				// 解析发生动作的字符串
				auditAction = (String)detailMap.get("AUDIT_ACTION");
				if(auditAction!=null&&!auditAction.equals("")){
					String[] actionArr = auditAction.split(",");
					List actionList =  new ArrayList();
					for (int i = 0;i<actionArr.length;i++){
						actionList.add(actionArr[i]);
						act.setOutData("actionList", actionList);
					}
				}
			}
			
			
			
			List<Map<String, Object>> listCheckBox = cddao.getCheckBox();   //多选框
			
			List<Map<String, Object>> detailList = cadao.getAllDetailByCompId(compId); // 通过compId查询所有的处理明细历史
			
			act.setOutData("ListCheckBox", listCheckBox);
			act.setOutData("detailList", detailList);
			act.setOutData("complaintMap", complaintMap);
			if(detailMap!=null){
				act.setOutData("detailMap", detailMap);
			}
		
			act.setForword(complainDisposalDealerAssignUrl);
			
			
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ACTION_NAME_ERROR_CODE,"客户投诉处理(服务站)");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 处理页面中的保存按钮响应
	 */
	public void saveModify(){
			try{
				//CommonUtils.checkNull() 校验是否为空
				String auditId = CommonUtils.checkNull(request.getParamValue("auditId"));        //取得投诉详细ID
				
				String compId = CommonUtils.checkNull(request.getParamValue("compId")); 
				
				// 处理明细
				String strAction = "";
				String[] auditActions = request.getParamValues("auditAction");                   //取得页面的checkbox动作
				if(auditActions!=null){
					for(int i=0;i<auditActions.length;i++){
						strAction+=","+auditActions[i];
					}
				}
				
				String actions = strAction.replaceFirst(",", "");
				String auditResult = CommonUtils.checkNull(request.getParamValue("auditResult"));  //处理结果
				String partCode = CommonUtils.checkNull(request.getParamValue("partCode"));        //备件编码
				String supplier = CommonUtils.checkNull(request.getParamValue("supplier"));        //上级保供单位
				String auditContent = CommonUtils.checkNull(request.getParamValue("auditContent"));//跟进结果描述       

				Date currTime = new Date(System.currentTimeMillis());
				ComplaintAuditDao cadao = new ComplaintAuditDao();
				
				TtCrComplaintsPO tc = new TtCrComplaintsPO();
				TtCrComplaintsPO to = new TtCrComplaintsPO();
				tc.setUpdateBy(logonUser.getUserId());
				tc.setUpdateDate(currTime);
				to.setCompId(Long.parseLong(compId));
				cadao.update(to, tc);
				
				//保存客户投诉处理明细表TT_CR_COMPLAINTS_AUDIT 更新后的信息
				
				TtCrComplaintsAuditPO capo = new TtCrComplaintsAuditPO(); 
				capo.setAuditAction(actions);
				capo.setAuditResult(new Integer(auditResult));
				capo.setAuditContent(auditContent);
				capo.setPartCode(partCode);
				capo.setSupplier(supplier);
				capo.setAuditDate(currTime);
				capo.setCompId(Long.parseLong(compId));
				auditId = SequenceManager.getSequence("");
				capo.setId(new Long(auditId));
				capo.setAuditStatus(Constant.AUDIT_STATUS_TYPE_03);
				capo.setDealerId(Long.valueOf(logonUser.getDealerId()));
				capo.setCreateDate(currTime);
				capo.setCreateBy(logonUser.getUserId());
				capo.setDealerId(Long.parseLong(logonUser.getDealerId()));
				cadao.inserLog(capo);
//				if(null==auditId||"".equals(auditId)){
//					auditId = SequenceManager.getSequence("");
//					capo.setId(new Long(auditId));
//					capo.setAuditStatus(Constant.AUDIT_STATUS_TYPE_03);
//					capo.setDealerId(Long.valueOf(logonUser.getDealerId()));
//					capo.setCreateDate(currTime);
//					capo.setCreateBy(logonUser.getUserId());
//					
//					cadao.inserLog(capo);
//				}else{
//					TtCrComplaintsAuditPO capo1 = new TtCrComplaintsAuditPO(); 
//					capo1.setId(new Long(auditId));
//					capo.setUpdateBy(logonUser.getUserId());
//					capo.setUpdateDate(currTime);
//					
//					cadao.updateAudit(capo1, capo);
//				}
				
				
				act.setRedirect(complainDisposalDealerInitUrl);
				
			}catch(Exception e){
				BizException e1 = new BizException(act,e,ErrorCodeConstant.SAVE_FAILURE_CODE,"客户投诉处理(服务站)");
				logger.error(logonUser,e1);
				act.setException(e1);
			}
	
	}
	
	
	
	/**
	 * 关闭投诉(服务站)
	 */
	public void closeComplaintByDealer(){
		try{
			
			String compId = CommonUtils.checkNull(request.getParamValue("compId"));            // 取得页面的id
			// 处理明细
			String strAction = "";
			Date currTime = new Date();
			/*String[] auditActions = request.getParamValues("auditAction");                     //取得页面的checkbox动作
			if(auditActions!=null&&auditActions.length>0){
				for(int i=0;i<auditActions.length;i++){
					String auditResult = CommonUtils.checkNull(request.getParamValue("auditResult"));  //处理结果
					String partCode = CommonUtils.checkNull(request.getParamValue("partCode"));        //备件编码
					String supplier = CommonUtils.checkNull(request.getParamValue("supplier"));        //上级保供单位
					String auditContent = CommonUtils.checkNull(request.getParamValue("auditContent"));//跟进结果描述       
					String auditId = SequenceManager.getSequence("");// 新增取得投诉明细表的ID
					strAction+=","+auditActions[i];
					String actions = strAction.replaceFirst(",", "");
					ComplaintAuditDao cadao = new ComplaintAuditDao();
					TtCrComplaintsAuditPO capo = new TtCrComplaintsAuditPO(); 
					capo.setId(new Long(auditId));
					capo.setCompId(new Long(compId));
					capo.setDealerId(new Long(logonUser.getDealerId()));
					capo.setAuditAction(actions);
					capo.setAuditResult(new Integer(auditResult));
					capo.setAuditStatus(Constant.AUDIT_STATUS_TYPE_03);
					capo.setAuditContent(auditContent);
					capo.setPartCode(partCode);
					capo.setSupplier(supplier);
					capo.setCreateBy(logonUser.getUserId());
					capo.setCreateDate(currTime);
					capo.setAuditDate(currTime);
					
					// 插入客户投诉处理明细表TT_CR_COMPLAINTS_AUDIT
					cadao.insert(capo);
				}
			}*/
			
			ComplaintDisposalDao cddao = new ComplaintDisposalDao();
			TtCrComplaintsPO po = new TtCrComplaintsPO();
			po.setStatus(Constant.COMP_STATUS_TYPE_01);
			po.setUpdateBy(logonUser.getUserId());
			po.setUpdateDate(currTime);
			TtCrComplaintsPO po1 = new TtCrComplaintsPO();
			po1.setCompId(new Long(compId));
			
			// 更新主表TT_CR_COMPLAINTS的状态
			cddao.update(po1, po);
			
			act.setForword(complainDisposalDealerInitUrl);	
			
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ACTION_NAME_ERROR_CODE,"客户投诉处理(服务站)");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
}
