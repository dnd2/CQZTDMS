package com.infodms.dms.actions.customerRelationships.complaint;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.ComplaintDisposalBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.claim.dealerClaimMng.ClaimBillMaintainDAO;
import com.infodms.dms.dao.customerRelationships.ComplaintAuditDao;
import com.infodms.dms.dao.customerRelationships.ComplaintDisposalDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtCrComplaintsAuditPO;
import com.infodms.dms.po.TtCrComplaintsPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.dms.chana.actions.OSC31;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

/**
 * 
 * <p>Title:ComplaintDisposalOEM.java</p>
 *
 * <p>Description: 客户投诉处理(区域)</p>
 *
 * <p>Copyright: Copyright (c) 2010</p>
 *
 * <p>Company: www.infoservice.com.cn</p>
 * <p>Date:2010-6-5</p>
 *
 * @author zouchao
 * @version 1.0
 * @remark
 */
public class ComplaintDisposalArea {
	
	private static Logger logger = Logger.getLogger(ComplaintDisposalArea.class);
	ActionContext act = ActionContext.getContext();
	AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
	RequestWrapper request = act.getRequest();
	
	// 客户投诉处理(区域)初始化页面
	private final String complainDisposalAreaInitUrl = "/jsp/customerRelationships/complaintDisposal/complaintDisposalArea.jsp";
	
	
	// 客户投诉处理(区域)处理页面
	private final String complainDisposalAssignUrl = "/jsp/customerRelationships/complaintDisposal/complaintDisposalAssignArea.jsp";
	
	static {
		ClaimBillMaintainDAO cd = ClaimBillMaintainDAO.getInstance();
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		System.out.println(logonUser);
	}
	/**
	 * 客户投诉处理(区域)初始化
	 */
	public void complaintDisposalAreaInit(){
		try{
			String orgId = String.valueOf(logonUser.getOrgId());
			act.setOutData("orgId",orgId);
			act.setForword(complainDisposalAreaInitUrl);
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ACTION_NAME_ERROR_CODE,"客户投诉处理(区域)");
			logger.error(logonUser,e1);
			act.setException(e1);
		}

	}
	
	/**
	 * 客户投诉处理(区域)查询
	 */
	public void queryComplaintDisposalArea(){
		try{
			//CommonUtils.checkNull() 校验是否为空
			String linkman = CommonUtils.checkNull(request.getParamValue("linkman"));  				//客户姓名
			String tel = CommonUtils.checkNull(request.getParamValue("tel"));	            		//客户电话
			String beginTime = CommonUtils.checkNull(request.getParamValue("beginTime"));   		//投诉时间
			String endTime = CommonUtils.checkNull(request.getParamValue("endTime"));       		//投诉时间
			String modelCode = CommonUtils.checkNull(request.getParamValue("modelCode"));   		//车型
			String ownOrgId = CommonUtils.checkNull(request.getParamValue("ownOrgId"));     		//所属区域
			String compDealerId = CommonUtils.checkNull(request.getParamValue("compDealerId")); 	//投诉经销商ID
			String compDealerCode = CommonUtils.checkNull(request.getParamValue("compDealerCode")); //投诉经销商CODE
			String compSource = CommonUtils.checkNull(request.getParamValue("compSource")); 		//投诉来源
			String compLevel = CommonUtils.checkNull(request.getParamValue("compLevel"));   		//投诉等级
			String compType = CommonUtils.checkNull(request.getParamValue("compType"));     		//投诉类型
			String compType2 = CommonUtils.checkNull(request.getParamValue("compType2"));     		//投诉类型2
			String compStatus = CommonUtils.checkNull(request.getParamValue("compStatus")); 		//投诉状态
			String auditResult = CommonUtils.checkNull(request.getParamValue("auditResult")); 		//处理中状态
			String compCode = CommonUtils.checkNull(request.getParamValue("compCode")); 			//投诉编号
			String licenseNo = CommonUtils.checkNull(request.getParamValue("licenseNo")); 			//车牌号
			
			String orgId = String.valueOf(logonUser.getOrgId());
			
			ComplaintDisposalBean cdbean = new ComplaintDisposalBean();
			cdbean.setLinkman(linkman);
			cdbean.setTel(tel);
			cdbean.setBeginTime(beginTime);
			cdbean.setEndTime(endTime);
			cdbean.setModelCode(modelCode);
			cdbean.setOwnOrgId(ownOrgId);
			cdbean.setCompDealerCode(compDealerCode);
			cdbean.setCompDealerId(compDealerId);
			cdbean.setCompSource(compSource);
			cdbean.setCompLevel(compLevel);
			cdbean.setCompType(compType);
			cdbean.setCompType2(compType2);
			cdbean.setStatus(compStatus);
			cdbean.setOrgId(orgId);
			cdbean.setAuditResult(auditResult);
			cdbean.setCompCode(compCode);
			cdbean.setLicenseNo(licenseNo);
			
			ComplaintDisposalDao cddao = new ComplaintDisposalDao();
			//分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage"))	: 1; // 处理当前页	
				
			PageResult<Map<String, Object>> ps = cddao.queryComplaintDisposalByOrg(cdbean,Constant.PAGE_SIZE,curPage);
			//分页方法 end
			
			//向前台传的list 名称是固定的不可改必须用 ps
			act.setOutData("ps", ps);     
			
		}catch(Exception e) {
			//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"客户投诉处理(区域)");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	

	
	
	
	/**
	 * 关闭投诉(区域)
	 */
	public void closeComplaintByArea(){
		try{
			
			// 取得页面的id和code
			String compId = CommonUtils.checkNull(request.getParamValue("compId")); 
			// 处理明细
			String strAction = "";
			Date currTime = new Date();
			/*String[] auditActions = request.getParamValues("auditAction");                     //取得页面的checkbox动作
			if(auditActions!=null&&auditActions.length>0){
				for(int i=0;i<auditActions.length;i++){
					strAction+=","+auditActions[i];
					String actions = strAction.replaceFirst(",", "");
					String auditResult = CommonUtils.checkNull(request.getParamValue("auditResult"));  //处理结果
					String partCode = CommonUtils.checkNull(request.getParamValue("partCode"));        //备件编码
					String supplier = CommonUtils.checkNull(request.getParamValue("supplier"));        //上级保供单位
					String auditContent = CommonUtils.checkNull(request.getParamValue("auditContent"));//跟进结果描述       
					String auditId = SequenceManager.getSequence("");// 新增取得投诉明细表的ID
					ComplaintAuditDao cadao = new ComplaintAuditDao();
					
					TtCrComplaintsAuditPO capo = new TtCrComplaintsAuditPO(); 
					capo.setId(new Long(auditId));
					capo.setCompId(Long.parseLong(compId));
					capo.setOrgId(logonUser.getOrgId());
					capo.setAuditAction(actions);
					capo.setAuditResult(new Integer(auditResult));
					capo.setAuditStatus(Constant.AUDIT_STATUS_TYPE_01);
					capo.setAuditContent(auditContent);
					capo.setPartCode(partCode);
					capo.setSupplier(supplier);
					capo.setCreateBy(logonUser.getUserId());
					capo.setCreateDate(currTime);
					capo.setAuditDate(currTime);
					
					// 插入客户投诉处理明细表TT_CR_COMPLAINTS_AUDIT
					cadao.inserLog(capo);
				}
			}*/

			ComplaintDisposalDao cddao = new ComplaintDisposalDao();
			
			
			TtCrComplaintsPO po = new TtCrComplaintsPO();
			po.setStatus(Constant.COMP_STATUS_TYPE_01);
			po.setUpdateDate(currTime);
			po.setUpdateBy(logonUser.getUserId());
			TtCrComplaintsPO po1 = new TtCrComplaintsPO();
			po1.setCompId(new Long(compId));
			// 更新主表TT_CR_COMPLAINTS的状态
			cddao.update(po1, po);
			
			

			act.setRedirect(complainDisposalAreaInitUrl);	
			
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ACTION_NAME_ERROR_CODE,"客户投诉处理(区域)");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 客户投诉处理(区域)处理初始化
	 */
	public void disposalComplaintArea(){
		try{
			String orgId = String.valueOf(logonUser.getOrgId());
			
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
			act.setOutData("orgId",orgId);
			act.setForword(complainDisposalAssignUrl);
			
			
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ACTION_NAME_ERROR_CODE,"客户投诉处理(区域)");
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
			String auditId = CommonUtils.checkNull(request.getParamValue("auditId"));
			String compId = CommonUtils.checkNull(request.getParamValue("compId"));          //取得页面传入comid
			
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

			
		
			//保存客户投诉处理明细表TT_CR_COMPLAINTS_AUDIT 更新后的信息
			ComplaintAuditDao cadao = new ComplaintAuditDao();
			TtCrComplaintsAuditPO capo = new TtCrComplaintsAuditPO(); 
			Date currTime = new Date(System.currentTimeMillis());
			
			TtCrComplaintsPO tc = new TtCrComplaintsPO();
			TtCrComplaintsPO to = new TtCrComplaintsPO();
			tc.setUpdateBy(logonUser.getUserId());
			tc.setUpdateDate(currTime);
			to.setCompId(Long.parseLong(compId));
			cadao.update(to, tc);
			
			if(!"".equals(actions)&&!"".equals(auditResult)){
				capo.setAuditAction(actions);
				capo.setAuditResult(new Integer(auditResult));
				capo.setAuditContent(auditContent);
				//capo.setUpdateBy(logonUser.getUserId());
				capo.setCompId(Long.parseLong(compId));
				capo.setPartCode(partCode);
				capo.setSupplier(supplier);
				auditId = SequenceManager.getSequence("");
				capo.setId(new Long(auditId));
				capo.setAuditStatus(Constant.AUDIT_STATUS_TYPE_02);
				//capo.setDealerId(Long.valueOf(logonUser.getDealerId()));
				capo.setCreateDate(currTime);
				capo.setCreateBy(logonUser.getUserId());
				capo.setAuditDate(currTime);
				capo.setAuditAction(actions);
				capo.setAuditResult(new Integer(auditResult));
				capo.setOrgId(logonUser.getOrgId());
				cadao.inserLog(capo);
//				if(null==auditId||"".equals(auditId)){
//					auditId = SequenceManager.getSequence("");
//					capo.setId(new Long(auditId));
//					capo.setAuditStatus(Constant.AUDIT_STATUS_TYPE_02);
//					//capo.setDealerId(Long.valueOf(logonUser.getDealerId()));
//					capo.setCreateDate(currTime);
//					capo.setCreateBy(logonUser.getUserId());
//					capo.setAuditDate(currTime);
//					capo.setAuditAction(actions);
//					capo.setAuditResult(new Integer(auditResult));
//					
//					cadao.inserLog(capo);
//				}else{
//					TtCrComplaintsAuditPO capo1 = new TtCrComplaintsAuditPO(); 
//					capo1.setId(new Long(auditId));
//					
//					capo.setUpdateDate(currTime);
//					capo.setAuditDate(currTime);
//					
//					cadao.update(capo1, capo);
//				
//				}
			}
			
			
			act.setRedirect(complainDisposalAreaInitUrl);
			//this.disposalComplaintArea();
			
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.SAVE_FAILURE_CODE,"客户投诉处理(区域)");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 区域批量分配投诉到经销商
	 */
	public void assignComplaintToDealer(){
		try{
			// 获取页面投诉的ID
			String[] compIds = request.getParamValues("compIds");
			
			// 取得页面分配的经销商ID
			String dealerId = CommonUtils.checkNull(request.getParamValue("dealerId"));
			
			
			ComplaintDisposalDao cddao = new ComplaintDisposalDao();
			ComplaintAuditDao cadao = new ComplaintAuditDao();
			
			List<TtCrComplaintsPO> pos = new ArrayList<TtCrComplaintsPO>();

			
			Date currTime = new Date(System.currentTimeMillis());
			
			// 通过ID进行状态的更改同时往TT_CR_COMPLAINTS_AUDIT表中插入记录，实现分配
			if(compIds != null && !compIds.equals("")){
				for(int i=0;i<compIds.length;i++){
					TtCrComplaintsPO po = new TtCrComplaintsPO();
					TtCrComplaintsPO newPo = new TtCrComplaintsPO();
					
					//更新TT_CR_COMPLAINTS中的状态
					po.setCompId(new Long(compIds[i]));
					newPo.setStatus(Constant.COMP_STATUS_TYPE_02);
					newPo.setDealerId(new Long(dealerId));
					newPo.setUpdateDate(currTime);
					newPo.setUpdateBy(logonUser.getUserId());
					
					cddao.update(po, newPo);
					
					//供接口调用
					Map<String, Object> complaintMap = cddao.getComplaintById(compIds[i]);
					if(null!=complaintMap&&complaintMap.size()!=0){
						TtCrComplaintsPO inpo = new TtCrComplaintsPO();
						inpo.setCompId(new Long(compIds[i]));
						inpo.setCompCode(complaintMap.get("COMP_CODE").toString());
						inpo.setDealerId(newPo.getDealerId());
						pos.add(inpo);
					}
					/*
					 *  分配的时候 不往子表里插记录
					Map<String, Object> auditMap = cadao.getAuditDetailByCompId(compIds[i], String.valueOf(Constant.AUDIT_STATUS_TYPE_02));
					
					if(null!=auditMap&&auditMap.size()!=0){
						// 往TT_CR_COMPLAINTS_AUDIT表中插入记录
						TtCrComplaintsAuditPO auditPO = new TtCrComplaintsAuditPO();
						auditPO.setAuditAction(CommonUtils.checkNull(auditMap.get("AUDIT_ACTION")));
						auditPO.setAuditContent(CommonUtils.checkNull(auditMap.get("AUDIT_CONTENT")));
						String auditResult = CommonUtils.checkNull(auditMap.get("AUDIT_RESULT"));
						if(!"".equals(auditResult)){
							auditPO.setAuditResult(Integer.valueOf(auditResult));
						}
						auditPO.setPartCode(CommonUtils.checkNull(auditMap.get("PART_CODE")));
						auditPO.setSupplier(CommonUtils.checkNull(auditMap.get("SUPPLIER")));
						String intStatus = CommonUtils.checkNull(auditMap.get("INT_STATUS"));
						if(!"".equals(intStatus)){
							auditPO.setIntStatus(Integer.valueOf(intStatus));
						}
						auditPO.setCreateBy(logonUser.getUserId());
						auditPO.setCreateDate(currTime);
						auditPO.setAuditDate(currTime);
						auditPO.setDealerId(new Long(dealerId));
						auditPO.setCompId(new Long(compIds[i]));
						auditPO.setId(Long.valueOf(SequenceManager.getSequence("")));
						auditPO.setAuditStatus(Constant.AUDIT_STATUS_TYPE_03);
						cadao.insert(auditPO);
					}
					*/
				}
				/**接口下发**/
				OSC31 os = new OSC31();
				os.execute(pos);
				/**接口下发结束**/
			}
			
			act.setOutData("pos", pos);
			act.setOutData("returnValue", 1);
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.SAVE_FAILURE_CODE,"客户投诉处理(区域)批量分配");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	
	/**
	 * 区域分配客户投诉到经销商(处理页面中的分配按钮响应事件)
	 */
	public void assignComplaint(){
		
		try{	
			
			String compId = CommonUtils.checkNull(request.getParamValue("compId"));   	// 取得页面传参comId
			String dealerId = CommonUtils.checkNull(request.getParamValue("dealerId")); // 取得页面传参dealerId
			
			List<TtCrComplaintsPO> pos = new ArrayList<TtCrComplaintsPO>();             // 供接口调用
			
			// 处理明细
			String strAction = "";
			String[] auditActions = request.getParamValues("auditAction");                     //取得页面的checkbox动作
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
			String assignContent = CommonUtils.checkNull(request.getParamValue("assignContent"));//分配意见
			
			Date currTime = new Date(System.currentTimeMillis());
			
			// 根据投诉id插入主表的dealerId
			ComplaintDisposalDao cddao = new ComplaintDisposalDao();
			TtCrComplaintsPO po1 = new TtCrComplaintsPO();
			po1.setCompId(new Long(compId));
			TtCrComplaintsPO po = new TtCrComplaintsPO();
			po.setDealerId(new Long(dealerId));
			po.setUpdateBy(logonUser.getUserId());
			po.setUpdateDate(currTime);
			cddao.update(po1, po);
			
			// 接口调用
			Map<String, Object> complaintMap = cddao.getComplaintById(compId);
			if(null!=complaintMap&&complaintMap.size()!=0){
				TtCrComplaintsPO inpo = new TtCrComplaintsPO();
				inpo.setCompId(new Long(compId));
				inpo.setCompCode(complaintMap.get("COMP_CODE").toString());
				inpo.setDealerId(po.getDealerId());
				pos.add(inpo);
			}
			
			// 插入客户投诉处理明细表TT_CR_COMPLAINTS_AUDIT
			ComplaintAuditDao cadao = new ComplaintAuditDao();
			TtCrComplaintsAuditPO capo = new TtCrComplaintsAuditPO(); 
			/*
			 * 分配的时候不往子表里插记录
			if(!"".equals(actions)&&!"".equals(auditResult)){
				String auditId = SequenceManager.getSequence("");
				capo.setAuditAction(actions);
				capo.setAuditResult(new Integer(auditResult));
				capo.setAuditStatus(Constant.AUDIT_STATUS_TYPE_03);
				capo.setAuditContent(auditContent);
				capo.setPartCode(partCode);
				capo.setSupplier(supplier);
				capo.setAssignContent(assignContent);
				capo.setDealerId(new Long(dealerId));
				capo.setCreateDate(currTime);
				capo.setCreateBy(logonUser.getUserId());
				capo.setAuditDate(currTime);
				capo.setId(new Long(auditId));
				capo.setCompId(new Long(compId));
				
				cadao.insert(capo);
			}
			*/
			/**接口下发开始**/
			OSC31 os = new OSC31();
			os.execute(pos);
			/**接口下发结束**/
			
			act.setRedirect(complainDisposalAreaInitUrl);
			
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ACTION_NAME_ERROR_CODE,"客户投诉处理(区域)分配");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}

}
