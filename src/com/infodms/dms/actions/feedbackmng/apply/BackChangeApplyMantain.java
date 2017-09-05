/**********************************************************************
* <pre>
* FILE : BackChangeApplyMantain.java
* CLASS : BackChangeApplyMantain
* 
* AUTHOR : WangJinBao
*
* FUNCTION : 退换车申请书维护Action.
*
*
*======================================================================
* CHANGE HISTORY LOG
*----------------------------------------------------------------------
* MOD. NO.| DATE     |    NAME    | REASON | CHANGE REQ.
*----------------------------------------------------------------------
*         |2010-05-17| WangJinBao  | Created |
* DESCRIPTION:
* </pre>
***********************************************************************/
package com.infodms.dms.actions.feedbackmng.apply;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.common.FileUploadManager;
import com.infodms.dms.actions.feedbackmng.InfoFeedBackMng;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.BackChangeApplyMantainBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.common.getCompanyId.GetOemcompanyId;
import com.infodms.dms.dao.feedbackMng.BackChangeApplyMantainDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.FsFileuploadPO;
import com.infodms.dms.po.TtIfExchangeAuditPO;
import com.infodms.dms.po.TtIfExchangePO;
import com.infodms.dms.po.TtIfExchangePOImp;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

/**
 * Function       :  退换车申请书维护
 * @author        :  wangjinbao
 * CreateDate     :  2010-05-17
 * @version       :  0.1
 */
public class BackChangeApplyMantain {
	private Logger logger = Logger.getLogger(BackChangeApplyMantain.class);
	private final BackChangeApplyMantainDao dao = BackChangeApplyMantainDao.getInstance();
	
	private final String BACK_CHANGE_APPLY_URL = "/jsp/feedbackMng/apply/backChangeApplyMantain.jsp";//主页面
	private final String BACK_CHANGE_APPLY_ADD_URL = "/jsp/feedbackMng/apply/backChangeApplyAdd.jsp";//新增页面
	private final String BACK_CHANGE_APPLY_UPDATE_URL = "/jsp/feedbackMng/apply/backChangeApplyUpdate.jsp";//修改页面
	private final String BACK_CHANGE_APPLY_DETAIL_URL = "/jsp/feedbackMng/apply/backChangeApplylDetail.jsp";//明细页面
	/**
	 * 退换车申请书维护查询初始化
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public void backChangeApplyInit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			act.setOutData("seriesList",InfoFeedBackMng.getVehicleSeriesByDealerId());
			act.setForword(BACK_CHANGE_APPLY_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"退换车申请书维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 查询退换车申请书
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public void backChangeApplyQuery() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		List<Object> params = new LinkedList<Object>();
		StringBuffer sb = new StringBuffer();
		try {
			RequestWrapper request = act.getRequest();
			act.getResponse().setContentType("application/json");
			if("1".equals(request.getParamValue("COMMAND"))){ //开始查询
				// 工单号
				String orderId = request.getParamValue("ORDER_ID");
				//VIN码
				String vin = request.getParamValue("VIN");
				//创建开始时间
				String beginTime = request.getParamValue("beginTime");
				//创建结束时间
				String endTime = request.getParamValue("endTime");
				//经销商id
				//modify by xiayanpeng 从SESSION 中取DEALER_ID返回STRING
				String  dealerid = logonUser.getDealerId();
				//车系
				String seriesid = request.getParamValue("vehicleSeriesList");
				//当开始时间和结束时间相同时
				if(null!=beginTime&&!"".equals(beginTime)&&null!=endTime&&!"".equals(endTime)){
//					if(beginTime.equals(endTime)){
						beginTime = beginTime+" 00:00:00";
						endTime = endTime+" 23:59:59";
//					}
				}
				//拼一下sql的查询条件
				if (orderId != null && !"".equals(orderId.trim())) {
					sb.append(" and order_id like ? ");
					params.add("%"+orderId+"%");
				}
				if (vin != null && !"".equals(vin.trim())) {
					//modify by xiayanpeng begin 原为and vin，输入查询条件提示未定义明确列 
					sb.append(" and v.vin like ? ");
					//modify by xiayanpeng end 
					params.add("%"+vin+"%");
				}
				if (seriesid != null && !"".equals(seriesid.trim()) && !"-1".equals(seriesid.trim())) {
					sb.append(" and v.series_id = ? ");
					params.add(seriesid);
				}
				//modify by xiayanpeng begin dealer_id 改为String
				if (dealerid != null && !"".equals(dealerid)) {
					sb.append(" and t.dealer_id = ? ");
					params.add(dealerid);
				}
				//modify by xiayanpeng end 
				//modify by xiayanpeng begin creeate_date,未指定具体表
				if (beginTime != null && !"".equals(beginTime.trim())) {
					sb.append(" and t.create_date >= to_date(?,'yyyy-mm-dd hh24:mi:ss') ");
					params.add(beginTime);
				}
				if (endTime != null && !"".equals(endTime.trim())) {
					sb.append(" and t.create_date <= to_date(?,'yyyy-mm-dd hh24:mi:ss') ");
					params.add(endTime);
				}	
				//modify by xiayanpeng end 
				Integer curPage = request.getParamValue("curPage") != null ? Integer
						.parseInt(request.getParamValue("curPage"))
						: 1; // 处理当前页
				
				PageResult<TtIfExchangePOImp> ps = dao.backChangeApplyQuery(Constant.PAGE_SIZE, curPage,sb.toString(),params);
				
				act.setOutData("ps", ps);
			}
		} catch(BizException e){
			logger.error(logonUser,e);
			act.setException(e);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"退换车申请书");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 新增退换车申请书初始化
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public void backChangeApplyAddInit() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword(BACK_CHANGE_APPLY_ADD_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"退换车申请书维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 新增退换车申请书
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public void addBackChangeApply() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		Long companyId=GetOemcompanyId.getOemCompanyId(logonUser);
		try {
			RequestWrapper request = act.getRequest();
			String linkManager = request.getParamValue("LINK_MANAGER"); // 服务中心经理
			String linkMan = request.getParamValue("LINK_MAN"); // 服务中心经办人
			String exType = Utility.getString(request.getParamValue("EX_TYPE")); // 退换类型
			String vin = Utility.getString(request.getParamValue("VIN")); // vin码
			String problemDescribe = CommonUtils.checkNull(request.getParamValue("question_content"));// 问题描述
			String userRequest = CommonUtils.checkNull(request.getParamValue("user_content"));// 用户要求如何
			String adviceDealMode = CommonUtils.checkNull(request.getParamValue("deal_content"));// 建议处理方式
			String costDetail = CommonUtils.checkNull(request.getParamValue("cost_detail"));// 费用明细
			String orderId = SequenceManager.getSequence("THAO");   //新建工单号
		
			TtIfExchangePO ttIfExchangePO = new TtIfExchangePO(); 
			ttIfExchangePO.setCreateBy(logonUser.getUserId());
			ttIfExchangePO.setCreateDate(new Date());
			//modify by xiayanpeng begin 从SESSION中取DEALER_ID 返回String 
			ttIfExchangePO.setDealerId(new Long(logonUser.getDealerId()));
			//moidfy by xiayanpeng end
//			ttIfExchangePO.setExContent(exContent);
			ttIfExchangePO.setExStatus(Constant.MARKET_BACK_STATUS_UNREPORT);//待上报
			ttIfExchangePO.setExType(Integer.parseInt(exType));
			ttIfExchangePO.setIsDel(0);                                                        //有效
			ttIfExchangePO.setLinkMan(linkMan);
			ttIfExchangePO.setLinkManager(linkManager);
			ttIfExchangePO.setOrderId(orderId);
			ttIfExchangePO.setVin(vin);
			ttIfExchangePO.setProblemDescribe(problemDescribe);
			ttIfExchangePO.setUserRequest(userRequest);
			ttIfExchangePO.setAdviceDealMode(adviceDealMode);
			ttIfExchangePO.setCostDetail(costDetail);
			ttIfExchangePO.setCompanyId(companyId);
			String ywzj = dao.insertBackApply(ttIfExchangePO);
			//附近功能：
			String[] fjids = request.getParamValues("fjid");
			FileUploadManager.fileUploadByBusiness(ywzj, fjids, logonUser);	
			backChangeApplyInit();
		}  catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"退换车申请书");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 修改退换车申请书初始化
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void modfiBackChangeApplyInit() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String orderId = request.getParamValue("ORDER_ID");
			BackChangeApplyMantainBean bcamBean = dao.queryDetailByOrderId(orderId);//根据orderid获得待查询的退换车申请书信息
			String id = String.valueOf(bcamBean.getId());
			List<FsFileuploadPO> fileList=dao.queryAttachFileInfo(id);
			act.setOutData("fileList", fileList);
			request.setAttribute("MantainBean", bcamBean);
			act.setForword(BACK_CHANGE_APPLY_UPDATE_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"退换车申请书");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 修改退换车申请书
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public void modifyBackChangeApply() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String id = request.getParamValue("id");
			String orderId = Utility.getString(request.getParamValue("ORDER_ID")); // 工单号
			String linkManager = request.getParamValue("LINK_MANAGER"); // 服务中心经理
			String linkMan = request.getParamValue("LINK_MAN"); // 服务中心经办人
			String exType = Utility.getString(request.getParamValue("EX_TYPE")); // 退换类型
			String vin = Utility.getString(request.getParamValue("VIN")); // vin码
			
			String problemDescribe = CommonUtils.checkNull(request.getParamValue("question_content"));// 问题描述
			String userRequest = CommonUtils.checkNull(request.getParamValue("user_content"));// 用户要求如何
			String adviceDealMode = CommonUtils.checkNull(request.getParamValue("deal_content"));// 建议处理方式
			String costDetail = CommonUtils.checkNull(request.getParamValue("cost_detail"));// 费用明细
			
			TtIfExchangePO ttIfExchangePO = new TtIfExchangePO();
			ttIfExchangePO.setOrderId(orderId);
			
			TtIfExchangePO ttIfExchangePO2 = new TtIfExchangePO();
			ttIfExchangePO2.setLinkMan(linkMan);
			ttIfExchangePO2.setLinkManager(linkManager);
			ttIfExchangePO2.setVin(vin);
//			ttIfExchangePO2.setExContent(exContent);
			ttIfExchangePO2.setProblemDescribe(problemDescribe);
			ttIfExchangePO2.setUserRequest(userRequest);
			ttIfExchangePO2.setAdviceDealMode(adviceDealMode);
			ttIfExchangePO2.setCostDetail(costDetail);
			ttIfExchangePO2.setExType(Integer.parseInt(exType));
			ttIfExchangePO2.setUpdateBy(logonUser.getUserId());
			ttIfExchangePO2.setUpdateDate(new Date());
			dao.updateBackApply(ttIfExchangePO,ttIfExchangePO2);
			//附近功能：
			String ywzj = id;
			String[] fjids = request.getParamValues("fjid");
			//String[] uploadFjid = request.getParamValues("uploadFjid");
			FileUploadManager.delAllFilesUploadByBusiness(ywzj,fjids);//删除附件
			FileUploadManager.fileUploadByBusiness(ywzj, fjids, logonUser);	//新添加附件
			backChangeApplyInit();
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"退换车申请书");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 删除退换车申请书
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public void deleteBackChangeApply() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String orderIds = request.getParamValue("orderIds");//要删除的工单号，以,隔开
			System.out.println(orderIds);
			if (orderIds != null && !"".equals(orderIds.trim())) {
				String [] orderIdArray = orderIds.split(","); //取得所有orderId放在数组中
				for (int i = 0;i<orderIdArray.length;i++) {
					TtIfExchangePO ttIfExchangePO = new TtIfExchangePO();
					ttIfExchangePO.setOrderId(orderIdArray[i]);
					TtIfExchangePO ttIfExchangePO2 = new TtIfExchangePO();
					ttIfExchangePO2.setIsDel(1);              //将工单状态置为“无效”
					ttIfExchangePO2.setUpdateBy(logonUser.getUserId());
					ttIfExchangePO2.setUpdateDate(new Date());
					dao.updateBackApply(ttIfExchangePO,ttIfExchangePO2);
				}
			}
			act.setOutData("success", "true");
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"退换车申请书");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 上报退换车申请书
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public void submitBackChangeApply() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			act.getResponse().setContentType("application/json");
			
			String orderIds = request.getParamValue("orderIds");//要上报的工单号，以,隔开
			
			if (orderIds!=null&&!"".equals(orderIds)) {
				String [] orderIdArray = orderIds.split(","); //取得所有orderId放在数组中
				for (int i = 0;i<orderIdArray.length;i++) {
					TtIfExchangePO ttIfExchangePO = new TtIfExchangePO();
					ttIfExchangePO.setOrderId(orderIdArray[i]);
					TtIfExchangePO ttIfExchangePO2 = new TtIfExchangePO();
					ttIfExchangePO2.setExStatus(Constant.MARKET_BACK_STATUS_REPORTED);//工单状态变为“已上报”
					ttIfExchangePO2.setExDate(new Date());//上报时间
					ttIfExchangePO2.setUpdateBy(logonUser.getUserId());
					ttIfExchangePO2.setUpdateDate(new Date());
					TtIfExchangeAuditPO tIfExchangeAuditPO = new TtIfExchangeAuditPO();
					tIfExchangeAuditPO.setOrderId(orderIdArray[i]);
					tIfExchangeAuditPO.setAuditBy(logonUser.getUserId());
					tIfExchangeAuditPO.setAuditDate(new Date());
					tIfExchangeAuditPO.setAuditStatus(Constant.MARKET_BACK_STATUS_REPORTED);
					tIfExchangeAuditPO.setId(Long.parseLong(SequenceManager.getSequence("")));
					tIfExchangeAuditPO.setOrgId(logonUser.getOrgId());
					dao.submitBackApply(ttIfExchangePO,ttIfExchangePO2,tIfExchangeAuditPO);
				}
			}
			act.setOutData("success", "true");
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"退换车申请书");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 退换车申请书详细页面
	 * @param null
	 * @return void
	 * @throws Exception 
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void detailBackChangeApply() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String orderId = request.getParamValue("ORDER_ID");//要显示详细信息的工单号
			BackChangeApplyMantainBean bcamBean = dao.queryDetailByOrderId(orderId); //取得基本信息
			List<BackChangeApplyMantainBean> MantainList=dao.getAuditInfoList(orderId);//取得审批信息
			request.setAttribute("MantainBean", bcamBean);
			request.setAttribute("MantainList", MantainList);			
			String id = String.valueOf(bcamBean.getId());
			List<FsFileuploadPO> fileList=dao.queryAttachFileInfo(id);
			act.setOutData("fileList", fileList);
			act.setForword(BACK_CHANGE_APPLY_DETAIL_URL);
		} catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"退换车申请书");
			logger.error(logonUser,e1);
			act.setException(e1);
		}

	}

}
