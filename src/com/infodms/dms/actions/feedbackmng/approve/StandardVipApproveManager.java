package com.infodms.dms.actions.feedbackmng.approve;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.feedbackmng.InfoFeedBackMng;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.StandardVipApplyManagerBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.getCompanyId.GetOemcompanyId;
import com.infodms.dms.dao.feedbackMng.StandardVipApplyManagerDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.FsFileuploadPO;
import com.infodms.dms.po.TtIfStandardAuditPO;
import com.infodms.dms.po.TtIfStandardPO;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

/**
 * @Title: StandardVipApproveManager.java
 *
 * @Description:CHANADMS
 *
 * @Copyright: Copyright (c) 2010
 *
 * @Company: www.infoservice.com.cn
 * @Date: 2010-5-22
 *
 * @author lishuai 
 * @mail   lishuai103@yahoo.cn	
 * @version 1.0
 * @remark 
 */
public class StandardVipApproveManager {
	public static Logger logger = Logger.getLogger(StandardVipApplyManagerDao.class);
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	
	private final String initUrl = "/jsp/feedbackMng/approve/queryStanderVipApprove.jsp";
	private final String approveUrl = "/jsp/feedbackMng/approve/approveStanderVip.jsp";
	
	/**
	 * 合格证/VIP审核查询页初始化
	 */
	public void standardVipApproveManagerInit(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			act.setOutData("seriesList",InfoFeedBackMng.getVehicleSeriesByDealerId()); //取车系
			act.setForword(initUrl);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"合格证/VIP卡申请表大区审核初始化");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 合格证/VIP审核查询
	 */
	public void queryStandardVipApprove(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		Long companyId=GetOemcompanyId.getOemCompanyId(logonUser);
		try {
			//CommonUtils.checkNull() 校验是否为空
			String dutyType = logonUser.getDutyType();
			String orderId = CommonUtils.checkNull(request.getParamValue("orderId"));  		//取工单号
			String vin = CommonUtils.checkNull(request.getParamValue("vin"));		   		//VIN
			String stType = CommonUtils.checkNull(request.getParamValue("stType"));	   		//申请的类型， 合格证或VIP
			String stAction = CommonUtils.checkNull(request.getParamValue("stAction"));		//操作类型，补办，修复，更换
			String beginTime = CommonUtils.checkNull(request.getParamValue("beginTime"));	//创建开始时间
			String endTime = CommonUtils.checkNull(request.getParamValue("endTime"));		//创建结束时间
			String vehicleModel = CommonUtils.checkNull(request.getParamValue("vehicleSeriesList"));   //车系
			String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode"));			//取经销商代码
			String dealerName = CommonUtils.checkNull(request.getParamValue("dealerName"));			//取经销商名称
			String orgId = String.valueOf(logonUser.getOrgId());							//组织ID
			//当开始时间和结束时间相同时
			if(null!=beginTime&&!"".equals(beginTime)&&null!=endTime&&!"".equals(endTime)){
					beginTime = beginTime+" 00:00:00";
					endTime = endTime+" 23:59:59";
			}
			if(!dealerCode.equals("")){////截串加单引号
				String[] supp = dealerCode.split(",");
				dealerCode = "";
				for(int i=0;i<supp.length;i++){
					supp[i] = "'"+supp[i]+"'";
					if(!dealerCode.equals("")){
						dealerCode += "," + supp[i];
					}else{
						dealerCode = supp[i];
					}
				}
			}
			StandardVipApplyManagerBean smb = new StandardVipApplyManagerBean();
			smb.setOrderId(orderId);
			smb.setVin(vin);
			smb.setStType(stType);
			smb.setStAction(stAction);
			smb.setBeginTime(beginTime);
			smb.setEndTime(endTime);
			smb.setDealerCode(dealerCode);
			smb.setDealerName(dealerName);
			smb.setVehicleModel(vehicleModel);
			smb.setDutyType(dutyType);
			smb.setOrgId(orgId);
			smb.setCompanyId(companyId);
			StandardVipApplyManagerDao smDao = new StandardVipApplyManagerDao();
			//分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页	
			PageResult<Map<String, Object>> ps = smDao.queryStandardVipApprove(logonUser.getUserId(),smb,curPage,Constant.PAGE_SIZE);
			//分页方法 end
			act.setOutData("ps", ps);     //向前台传的list 名称是固定的不可改必须用 ps
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"合格证/VIP卡申请表大区审核");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 合格证/VIP审核
	 */
	public void approveStandardVipApply(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String orderId = request.getParamValue("orderId");    //工单号
			StandardVipApplyManagerDao smDao = new StandardVipApplyManagerDao();
			Map<String, Object> svInfo = smDao.getStandardVipInfo(orderId);     //查询详细
			List<Map<String, Object>> list = smDao.getAuditInfo(orderId);
			String id = String.valueOf(svInfo.get("ID"));
			List<FsFileuploadPO> fileList=smDao.queryAttachFileInfo(id);
			act.setOutData("fileList", fileList);
			act.setOutData("svInfo", svInfo);
			act.setOutData("auditList", list);
			act.setForword(approveUrl);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"合格证/VIP卡申请表大区审核");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 合格证/VIP审核操作
	 */
	public void approveStandardVip(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String orderId = request.getParamValue("orderId");    //工单号
			String content = CommonUtils.checkNull(request.getParamValue("content"));
			String flag = request.getParamValue("flag");
			StandardVipApplyManagerDao smDao = new StandardVipApplyManagerDao();
			TtIfStandardAuditPO po = new TtIfStandardAuditPO();
			po.setOrderId(orderId);			//工单号
			po.setAuditContent(content);	//审批意见
			po.setAuditDate(new Date(System.currentTimeMillis()));  //审批时间
			if(flag.equals("1")){
				po.setAuditStatus(Constant.SERVICEINFO_VIP_AREA_STATUS_PASS);   //大区通过
				//更改工单主表状态
				TtIfStandardPO ts = new TtIfStandardPO();
				TtIfStandardPO tp = new TtIfStandardPO();
				ts.setOrderId(orderId);
				tp.setStStatus(Constant.SERVICEINFO_VIP_AREA_STATUS_PASS);
				smDao.updateOrderStatus(ts, tp);
			}else{
				po.setAuditStatus(Constant.SERVICEINFO_VIP_AREA_STATUS_REJECT);   //大区驳回
				//更改工单主表状态
				TtIfStandardPO ts = new TtIfStandardPO();
				TtIfStandardPO tp = new TtIfStandardPO();
				ts.setOrderId(orderId);
				tp.setStStatus(Constant.SERVICEINFO_VIP_AREA_STATUS_REJECT);
				smDao.updateOrderStatus(ts, tp);
			}
			po.setAuditBy(logonUser.getUserId());	//审批人
			po.setOrgId(logonUser.getOrgId());		//审批组织ID
			smDao.insertApprovePass(po);		//插入审批明细表
			standardVipApproveManagerInit();
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"合格证/VIP卡申请表大区审核");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
}
