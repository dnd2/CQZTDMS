package com.infodms.dms.actions.feedbackmng.apply;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.getCompanyId.GetOemcompanyId;
import com.infodms.dms.dao.feedbackMng.ServiceDataDao;
import com.infodms.dms.dao.feedbackMng.ServiceInfoApplyDao;
import com.infodms.dms.dao.feedbackMng.ServiceInfoApproveDao;
import com.infodms.dms.dao.feedbackMng.ServiceInfoDetailDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtIfServiceInfoAuditPO;
import com.infodms.dms.po.TtIfServiceInfoDetailPO;
import com.infodms.dms.po.TtIfServiceInfoPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

/**
 * 
 * <p>Title:ServiceInfoApplyManager.java</p>
 *
 * <p>Description: 服务资料审批表业务逻辑处理</p>
 *
 * <p>Copyright: Copyright (c) 2010</p>
 *
 * <p>Company: www.infoservice.com.cn</p>
 * <p>Date:2010-07-27</p>
 *
 * @author zouchao
 * @version 1.0
 * @remark
 */
public class ServiceInfoApplyManager {
	
	private static Logger logger = Logger.getLogger(ServiceInfoApplyManager.class);
	ActionContext act = ActionContext.getContext();
	AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
	RequestWrapper request = act.getRequest();
	
	private final String serviceInfoInitUrl = "/jsp/feedbackMng/apply/serviceInfoApply.jsp";
	private final String serviceInfoAddUrl = "/jsp/feedbackMng/apply/serviceInfoApplyAdd.jsp";
	private final String serviceInfoDetailUrl = "/jsp/feedbackMng/apply/serviceInfoDetail.jsp";
	private final String serviceDataAddInitUrl = "/jsp/feedbackMng/apply/serviceDataSelect.jsp";
	private final String serviceDataAddInitUrl2 = "/jsp/feedbackMng/apply/serviceDataSelect2.jsp";
	private final String serviceInfoModInitUrl = "/jsp/feedbackMng/apply/serviceInfoApplyModify.jsp";
	
	
	/**
	 * 服务资料审批表初始化页面
	 */
	public void serviceInfoApplyInit(){
		try{
			act.setForword(serviceInfoInitUrl);
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ACTION_NAME_ERROR_CODE,"服务资料审批表");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
		
	}
	
	/**
	 * 服务资料审批表查询功能
	 */
	public void queryServiceInfo(){
		try {
			// 从Session中获取dealerId
			String dealerId = String.valueOf(logonUser.getDealerId());
			
			// 获取页面查询条件
			String orderID = request.getParamValue("orderId");
			String startDate = request.getParamValue("beginTime");
			String endDate = request.getParamValue("endTime");
			//当开始时间和结束时间相同时
			if(null!=startDate&&!"".equals(startDate)&&null!=endDate&&!"".equals(endDate)){
//				if(startDate.equals(endDate)){
					startDate = startDate+" 00:00:00";
					endDate = endDate+" 23:59:59";
//				}
			}
			// 处理当前页
			Integer curPage = request.getParamValue("curPage") != null ? Integer .parseInt(request.getParamValue("curPage")): 1;
			ServiceInfoApplyDao infoDao = new ServiceInfoApplyDao();
			
			// 分页查询
			PageResult<Map<String, Object>> ps = infoDao.queryServiceInfo(dealerId,orderID,startDate,endDate,Constant.PAGE_SIZE,curPage);
			act.setOutData("ps", ps);
			act.setForword(serviceInfoInitUrl);
		}catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"服务资料审批表");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	} 
	
	
	
	/**
	 * 服务资料审批表明细页面
	 */
	public void serviceInfoDetailView(){
		try{
			// 获取页面传入id
			String orderId = request.getParamValue("orderId");
			
			ServiceInfoApplyDao infoDao = new ServiceInfoApplyDao();
			ServiceInfoApproveDao appDao = new ServiceInfoApproveDao();
			
			// 根据ID取得TT_IF_SERVICE_INFO表中的基本信息
			Map<String, Object> infops = infoDao.searchServiceInfoBaseById(orderId);
			
			// 取得TT_IF_SERVICE_INFO_DETAIL 表中的详细信息
			List<Map<String, Object>> detailList = infoDao.getInfoDetail(orderId);
			
			// 取得TT_IF_SERVICE_INFO_AUDIT 表中的审批明细
			List<Map<String, Object>> auditList = appDao.getAuditInfo(orderId);
			
			act.setOutData("ps", infops);
			act.setOutData("detailList", detailList);
			act.setOutData("auditList", auditList);
			act.setForword(serviceInfoDetailUrl);
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ACTION_NAME_ERROR_CODE,"服务资料审批表");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
		
	}
	
	/**
	 * 服务资料审批表上报
	 */
	public void serviceInfoApplySubmit(){
		try{
			
			// 获取页面需要上报数据的数组
			String[] orderIds = request.getParamValues("orderIds");
			
			ServiceInfoApplyDao infoDao = new ServiceInfoApplyDao();
			ServiceInfoApproveDao infoApproveDao = new ServiceInfoApproveDao();
			
			// 通过ID进行状态的更改同时往TT_IF_SERVICE_INFO_AUDIT表中插入记录，实现上报
			if(orderIds != null && !orderIds.equals("")){
				for(int i=0;i<orderIds.length;i++){
					TtIfServiceInfoPO po = new TtIfServiceInfoPO();
					TtIfServiceInfoPO newPo = new TtIfServiceInfoPO();
					TtIfServiceInfoAuditPO auditPO = new TtIfServiceInfoAuditPO();
					
					//更新TT_IF_SERVICE_INFO中的状态
					po.setOrderId(orderIds[i]);
					newPo.setStatus(Constant.SERVICEINFO_VIP_STATUS_REPORTED);
					newPo.setAppDate(new Date(System.currentTimeMillis()));
					infoDao.update(po, newPo);
					
					// 往TT_IF_SERVICE_INFO_AUDIT表中插入记录
					auditPO.setAuditBy(logonUser.getUserId());
					auditPO.setAuditDate(new Date(System.currentTimeMillis()));
					auditPO.setOrderId(orderIds[i]);
					//modify by xiayanpeng begin 日志表插入ORG_ID
					auditPO.setOrgId(logonUser.getOrgId());
					//modify by xiayanpeng end
					auditPO.setId(Long.valueOf(SequenceManager.getSequence("")));
					auditPO.setAuditStatus(Constant.SERVICEINFO_VIP_STATUS_REPORTED);
					infoApproveDao.insert(auditPO);
				}
			}
			act.setOutData("returnValue", 1);
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.UPDATE_FAILURE_CODE,"服务资料审批表");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 服务资料审批表删除
	 */
	public void serviceInfoApplyDel(){
		try{
			
			String[] orderIds = request.getParamValues("orderIds");
			
			ServiceInfoApplyDao infoDao = new ServiceInfoApplyDao();
			
			if(orderIds != null && !orderIds.equals("")){
				for(int i=0;i<orderIds.length;i++){
					TtIfServiceInfoPO po = new TtIfServiceInfoPO();
					TtIfServiceInfoPO newPo = new TtIfServiceInfoPO();
					po.setOrderId(orderIds[i]);
					newPo.setIsDel(Integer.valueOf(Constant.IS_DEL_01));
					newPo.setUpdateDate(new Date(System.currentTimeMillis()));
					newPo.setUpdateBy(logonUser.getUserId());
					infoDao.update(po, newPo);
				}
			}
			act.setOutData("returnValue", 1);
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.UPDATE_FAILURE_CODE,"服务资料审批表");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	
	/**
	 * 服务资料审批表新增初始化
	 */
	public void serviceInfoApplyAdd(){
		try{
			// 从Session中获取dealerId
			String dealerId = String.valueOf(logonUser.getDealerId());
			
			ServiceInfoApplyDao infoDao = new ServiceInfoApplyDao();
			Map<String,Object>  map = infoDao.getDealerInfo(dealerId);
			act.setOutData("map", map);
			act.setForword(serviceInfoAddUrl);
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ACTION_NAME_ERROR_CODE,"服务资料审批表");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
		
	}
	
	
	/**
	 * 服务资料审批表新增保存
	 */
	public void saveServiceInfo(){
		try{
			// 从Session中获取dealerId
			String dealerId = logonUser.getDealerId();
			
			ServiceInfoApplyDao infoDao = new ServiceInfoApplyDao();
			Long companyId=GetOemcompanyId.getOemCompanyId(logonUser);
			// 获取页面基本信息部分数据
			String linkMan = CommonUtils.checkNull(request.getParamValue("linkman"));
			String tel = CommonUtils.checkNull(request.getParamValue("tel"));
			String fax = CommonUtils.checkNull(request.getParamValue("fax"));
			String mailType = CommonUtils.checkNull(request.getParamValue("mailType"));
			String mailAddress = CommonUtils.checkNull(request.getParamValue("mailAddress"));
			String selContent = CommonUtils.checkNull(request.getParamValue("selContent"));
			String orderId = CommonUtils.checkNull(SequenceManager.getSequence("FZAO"));
			String zipCode = request.getParamValue("zipCode");
			
			TtIfServiceInfoPO po = new TtIfServiceInfoPO();
			po.setOrderId(orderId);
			po.setDealerId(new Long(dealerId));
			po.setStatus(Constant.SERVICEINFO_VIP_STATUS_UNREPORT);// 待上报
			po.setLinkMan(linkMan);
			po.setTel(tel);
			po.setFax(fax);
			po.setZipCode(zipCode);
			po.setMailType(Integer.valueOf(mailType));
			po.setMailAddress(mailAddress);
			po.setSeContent(selContent);
			po.setCompanyId(companyId);
			po.setCreateBy(logonUser.getUserId());
			po.setCreateDate(new Date(System.currentTimeMillis()));
			//modify by xiayanpeng begin 新增时插入IS_DEL=0 
			po.setIsDel(new Integer(Constant.IS_DEL_00));
			//modify by xiayanpeng end 
			
			infoDao.saveServiceInfo(po);
			
			// 获取页面资料明细部分数据
			String dataIds = request.getParamValue("str");
			String infoId = "";
			Integer amount = 0;
			String remark = "";
			
			ServiceInfoDetailDao detailDao = new ServiceInfoDetailDao();
			if(dataIds!=null&& !dataIds.equals("")){
				
				String dataIdStr = dataIds.replaceFirst(",", "");
				String[] dataArr = dataIdStr.split(",");
				
				for(int i=0;i<dataArr.length;i++){
				  if(!"".equals(dataArr[i])){
					    TtIfServiceInfoDetailPO detailPO = new TtIfServiceInfoDetailPO();
						
						detailPO.setId(Long.valueOf(SequenceManager.getSequence("")));
						detailPO.setOrderId(orderId);
						detailPO.setStatus(Constant.STATUS_ENABLE);
						detailPO.setCreateDate(new Date(System.currentTimeMillis()));
						detailPO.setCreateBy(logonUser.getUserId());
						
						for(int j=0;j<dataArr.length;j++){
							String[] dataArr2 = dataArr[i].split("@");
							if(dataArr2.length<3){
								String[] dataArr3 = new String[3];
								
								for(int t=0;t<3;dataArr3[t++]="");
								
								dataArr3[0]=dataArr2[0];
								dataArr3[1]=dataArr2[1];
								
								infoId = dataArr3[0];
								amount = Integer.valueOf(dataArr3[1]);
								remark = dataArr3[2];
							}else{
								infoId = dataArr2[0];
								amount = Integer.valueOf(dataArr2[1]);
								remark = dataArr2[2];
							}
							detailPO.setInfoId(Long.valueOf(infoId));
							detailPO.setAmount(amount);
							detailPO.setRemark(remark);
						}
						
						detailDao.saveDetail(detailPO);
				   }
			     }
			}
			
			act.setRedirect(serviceInfoInitUrl);
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.SAVE_FAILURE_CODE,"服务资料审批表");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 修改服务资料审批表初始化
	 */
	public void modifyServiceInfoInit() {
		try{
			String orderId = CommonUtils.checkNull(request.getParamValue("orderId"));
			ServiceInfoApplyDao infoDao = new ServiceInfoApplyDao();
			Map<String,Object>  infoMap = infoDao.searchServiceInfoBaseById(orderId);
			List<Map<String, Object>> detailList = infoDao.getInfoDetail(orderId);
			act.setOutData("map", infoMap);
			act.setOutData("detailList", detailList);
			act.setForword(serviceInfoModInitUrl);
			
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.UPDATE_FAILURE_CODE,"服务资料审批表");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
		
	}
	
	/**
	 * 修改服务资料审批表
	 */
	public void modifyServiceInfo() {
		
		try {
			String dealerId = logonUser.getDealerId();
			
			// 获取页面基本信息部分数据
			String linkMan = CommonUtils.checkNull(request.getParamValue("linkman"));
			String tel = CommonUtils.checkNull(request.getParamValue("tel"));
			String fax = CommonUtils.checkNull(request.getParamValue("fax"));
			String mailType = CommonUtils.checkNull(request.getParamValue("mailType"));
			String mailAddress = CommonUtils.checkNull(request.getParamValue("mailAddress"));
			String selContent = CommonUtils.checkNull(request.getParamValue("selContent"));
			// 获取页面传入的orderId
			String orderId = CommonUtils.checkNull(request.getParamValue("orderId"));

			ServiceInfoApplyDao infoDao = new ServiceInfoApplyDao();
			
			//根据orderId取得该条记录
			TtIfServiceInfoPO po = new TtIfServiceInfoPO();
			po.setOrderId(orderId);
			
			// 更新TT_IF_SERVICE_INFO表中的该条记录
			TtIfServiceInfoPO po2 = new TtIfServiceInfoPO();
			po2.setLinkMan(linkMan);
			po2.setTel(tel);
			po2.setDealerId(new Long(dealerId));
			po2.setStatus(Constant.SERVICEINFO_VIP_STATUS_UNREPORT);// 待上报
			po2.setFax(fax);
			po2.setMailType(Integer.valueOf(mailType));
			po2.setMailAddress(mailAddress);
			po2.setSeContent(selContent);
			po2.setUpdateBy(logonUser.getUserId());
			po2.setUpdateDate(new Date(System.currentTimeMillis()));
			
			infoDao.update(po, po2);
			
			// 根据orderId对TT_IF_SERVICE_INFO_DETAIL表中相关数据处理
			ServiceInfoDetailDao detailDao = new ServiceInfoDetailDao();
			
			// 获取页面拼好的字符串
			String dataIds = request.getParamValue("str");
			
			String infoId = "";
			Integer amount = 0;
			String remark = "";
			String flag = "";
			
			if(dataIds!=null&& !dataIds.equals("")){
				// 分割字符串
				String dataIdStr = dataIds.replaceFirst(",", "");
				String[] dataArr = dataIdStr.split(",");
				
				for(int i=0;i<dataArr.length;i++){
				  if(!"".equals(dataArr[i])){
					    TtIfServiceInfoDetailPO detailPO = new TtIfServiceInfoDetailPO();
					    TtIfServiceInfoDetailPO detailPO2 = new TtIfServiceInfoDetailPO();
						
					    detailPO2.setStatus(Constant.STATUS_ENABLE);
					
						for(int j=0;j<dataArr.length;j++){
							String[] dataArr2 = dataArr[i].split("@");
							if(dataArr2.length<4){
								String[] dataArr3 = new String[4];
								
								for(int t=0;t<4;dataArr3[t++]="");
								dataArr3[0]=dataArr2[0];
								dataArr3[1]=dataArr2[1];
								dataArr3[2]=dataArr2[2];
								
								infoId = dataArr3[0];
								amount = Integer.valueOf(dataArr3[1]);
								flag = dataArr3[2];
								remark = dataArr3[3];
							}else{
								infoId = dataArr2[0];
								amount = Integer.valueOf(dataArr2[1]);
								flag = dataArr2[2];
								remark = dataArr2[3];
							}
							
							
						}
						
						// 如果存在则更新,不存在则插入
						if(flag.equals("1")){
							detailPO.setOrderId(orderId);
							detailPO.setInfoId(Long.valueOf(infoId));
							detailPO2.setAmount(amount);
							detailPO2.setRemark(remark);
							detailPO2.setUpdateBy(logonUser.getUserId());
							detailPO2.setUpdateDate(new Date(System.currentTimeMillis()));
							
							detailDao.update(detailPO, detailPO2);
						}else{
							detailPO2.setId(Long.valueOf(SequenceManager.getSequence("")));
							detailPO2.setOrderId(orderId);
							detailPO2.setInfoId(Long.valueOf(infoId));
							
							detailPO2.setCreateDate(new Date(System.currentTimeMillis()));
							detailPO2.setCreateBy(logonUser.getUserId());
							detailPO2.setAmount(amount);
							detailPO2.setRemark(remark);
							detailDao.saveDetail(detailPO2);
						}
						
				  }
					
				}
			}
			act.setRedirect(serviceInfoInitUrl);	
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.UPDATE_FAILURE_CODE,"服务资料审批表");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 新增服务资料初始化
	 */
	public void addServiceDataInit(){
		try{
			act.setForword(serviceDataAddInitUrl);
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"服务资料明细增加初始化");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 新增服务资料初始化
	 * 多选
	 */
	public void addServiceDataInit2(){
		try{
			act.setForword(serviceDataAddInitUrl2);
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"服务资料明细增加初始化");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	
	/**
	 * 查询服务资料表供用户选择
	 */
	public void queryServiceData(){
		try{
			ServiceDataDao dataDao = new ServiceDataDao();
			String dataname = CommonUtils.checkNull(request.getParamValue("dataname"));
			// 处理当前页
			Integer curPage = request.getParamValue("curPage") != null ? Integer .parseInt(request.getParamValue("curPage")): 1;
			PageResult<Map<String, Object>> ps = dataDao.queryServiceData(dataname,Constant.PAGE_SIZE,curPage);
			act.setOutData("ps", ps);
			act.setForword(serviceDataAddInitUrl);
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"服务资料明细增加");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 对于修改页面删除功能的处理
	 */
	public void serviceInfoDetailDel(){
		try{
			String orderId = request.getParamValue("orderId");
			String delStr = request.getParamValue("delstr");
			if(null!=delStr&&!"".equals(delStr)){
				String dataStr = delStr.replaceFirst(",", "");
				if(!"".equals(dataStr)){
					String[] dataIdArr = dataStr.split(",");
					for(int i=0;i<dataIdArr.length;i++){
						ServiceInfoDetailDao detailDao = new ServiceInfoDetailDao();
						TtIfServiceInfoDetailPO detailPO = new TtIfServiceInfoDetailPO();
						detailPO.setOrderId(orderId);
						detailPO.setInfoId(Long.valueOf(dataIdArr[i]));
						
						detailDao.delete(detailPO);
						
					}
				}
			}
			
			act.setOutData("returnValue", 1);
			
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.DELETE_FAILURE_CODE,"服务资料明细表");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
}
