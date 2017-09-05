package com.infodms.dms.actions.sales.fleetmanage.fleetInfoManage;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.FleetInfoBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.sales.fleetmanage.fleetinfomanage.FleetEditLogDao;
import com.infodms.dms.dao.sales.fleetmanage.fleetinfomanage.FleetInfoAppDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmFleetPO;
import com.infodms.dms.po.TtFleetEditLogPO;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

/**
 * 
 * <p>Title:FleetInfoModifyApp.java</p>
 *
 * <p>Description: 集团客户报备更改审核业务逻辑层</p>
 *
 * <p>Copyright: Copyright (c) 2010</p>
 *
 * <p>Company: www.infoservice.com.cn</p>
 * <p>Date:2010-6-17</p>
 *
 * @author zouchao
 * @version 1.0
 * @remark
 */
public class FleetInfoModifyCheck {

	private static Logger logger = Logger.getLogger(FleetInfoModifyCheck.class);
	ActionContext act = ActionContext.getContext();
	AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
	RequestWrapper request = act.getRequest();
	
	// 集团客户报备更改审核初始化页面
	private final String fleetModifyCheckInitUrl = "/jsp/sales/fleetmanage/fleetinfomanage/fleetInfoModifyCheckOEM.jsp";
	
	
	// 集团客户报备更改审核页面
	private final String fleetModifyAppUrl = "/jsp/sales/fleetmanage/fleetinfomanage/checkInfoModifyEditOEM.jsp";
	
	/**
	 * 集团客户报备更改审核初始化
	 */
	public void checkModifyInit(){
		try{
			act.setForword(fleetModifyCheckInitUrl);
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ACTION_NAME_ERROR_CODE,"集团客户报备更改审核");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	
	/**
	 * 集团客户报备更改审核查询
	 */
	public void queryInfoForModify(){
		try{
			//CommonUtils.checkNull() 校验是否为空
			String fleetName = CommonUtils.checkNull(request.getParamValue("fleetName"));  	    //客户名称
			String fleetType = CommonUtils.checkNull(request.getParamValue("fleetType"));	    //客户类型
			String beginTime = CommonUtils.checkNull(request.getParamValue("beginTime"));       //报备开始日期
			String endTime = CommonUtils.checkNull(request.getParamValue("endTime"));           //报备结束日期
//			String dlrCompanyId = CommonUtils.checkNull(request.getParamValue("dealerId"));     //经销商公司ID
			String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode"));     //经销商公司CODE
			
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
			// 传入查询条件
			FleetInfoBean fibean = new FleetInfoBean();
			fibean.setFleetName(fleetName);
			fibean.setFleetType(fleetType);
			fibean.setBeginTime(beginTime);
			fibean.setEndTime(endTime);
			fibean.setDutyType(logonUser.getDutyType());
			fibean.setOrgId(String.valueOf(logonUser.getOrgId()));
//			fibean.setDlrCompanyId(dlrCompanyId);
			fibean.setDlrCompanyCode(dealerCode);
			String isDel = "0";
			fibean.setIsDel(isDel);
			// 查询出审核状态为待确认的数据进行审核
			fibean.setStatus(String.valueOf(Constant.FLEET_INFO_TYPE_02));
			
			FleetEditLogDao eldao = new FleetEditLogDao();
			//分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage"))	: 1; // 处理当前页	
				
			PageResult<Map<String, Object>> ps = eldao.queryInfoForModify(fibean,Constant.PAGE_SIZE,curPage);
			//分页方法 end
			
			//向前台传的list 名称是固定的不可改必须用 ps
			act.setOutData("ps", ps);     
			
			act.setForword(fleetModifyCheckInitUrl);
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"集团客户报备更改审核");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	
	
	/**
	 * 集团客户报备更改审核初始化
	 */
	public void checkModifyInfo(){
		try{
			// 取得页面传入的需更新对象的Id
			String editId = CommonUtils.checkNull(request.getParamValue("editId"));  
			
			FleetEditLogDao eldao = new FleetEditLogDao();
			
			// 根据ID查询集团客户信息
			Map<String, Object> fleetMap = eldao.getModifyInfobyId(editId);
			
			act.setOutData("fleetMap", fleetMap);
			
			
			act.setForword(fleetModifyAppUrl);
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"集团客户报备更改审核");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 集团客户报备更改审核
	 */
	public void auditInfoModify(){
		try{
			String editId = CommonUtils.checkNull(request.getParamValue("editId"));            // 集团客户更改记录ID
			
			String auditRemark = CommonUtils.checkNull(request.getParamValue("auditRemark"));  // 审核意见
			
			String mark =  CommonUtils.checkNull(request.getParamValue("mark"));               // 通过和驳回的标识，1为通过，0为驳回
			
			FleetEditLogDao eldao = new FleetEditLogDao();
			
			Map<String, Object> logMap = eldao.getModifyInfobyId(editId);
			String fleetId = CommonUtils.checkNull(logMap.get("FLEET_ID"));
			String fleetType = CommonUtils.checkNull(logMap.get("FLEET_TYPE")); 
			String mainBusiness = CommonUtils.checkNull(logMap.get("MAIN_BUSINESS"));
			String fundSize = CommonUtils.checkNull(logMap.get("FUND_SIZE"));
			String staffSize = CommonUtils.checkNull(logMap.get("STAFF_SIZE"));
			String purpose = CommonUtils.checkNull(logMap.get("PURPOSE"));
			String region = CommonUtils.checkNull(logMap.get("REGION"));
			String zipCode = CommonUtils.checkNull(logMap.get("ZIP_CODE"));
			String address = CommonUtils.checkNull(logMap.get("ADDRESS"));
			String mainLinkman = CommonUtils.checkNull(logMap.get("MAIN_LINKMAN"));
			String mainJob = CommonUtils.checkNull(logMap.get("MAIN_JOB"));
			String mainPhone = CommonUtils.checkNull(logMap.get("MAIN_PHONE"));
			String mainEmail = CommonUtils.checkNull(logMap.get("MAIN_EMAIL"));
			String otherLinkman = CommonUtils.checkNull(logMap.get("OTHER_LINKMAN"));
			String otherJob = CommonUtils.checkNull(logMap.get("OTHER_JOB"));
			String otherPhone = CommonUtils.checkNull(logMap.get("OTHER_PHONE"));
			String otherEmail = CommonUtils.checkNull(logMap.get("OTHER_EMAIL"));
			String reqRemark = CommonUtils.checkNull(logMap.get("REQ_REMARK"));
			String seriesId = CommonUtils.checkNull(logMap.get("SERIES_ID"));
			String seriesCount = CommonUtils.checkNull(logMap.get("SERIES_COUNT"));
			String visitDate = CommonUtils.checkNull(logMap.get("VISIT_DATE"));
			
			TmFleetPO fleetPo1 = new TmFleetPO();
			fleetPo1.setFleetId(new Long(fleetId));
			
			TmFleetPO fleetPo2 = new TmFleetPO();
			Date currTime = new Date(System.currentTimeMillis());
			
			fleetPo2.setSeriesId(Long.parseLong(seriesId));
			fleetPo2.setSeriesCount(Integer.parseInt(seriesCount));
			if(null!=fleetType&&!"".equals(fleetType)){
				fleetPo2.setFleetType(new Integer(fleetType));
			}
			
			if(null!=mainBusiness&&!"".equals(mainBusiness)){
				fleetPo2.setMainBusiness(new Integer(mainBusiness));
			}
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			if(!"".equals(visitDate)){
				fleetPo2.setVisitDate(formatter.parse(visitDate));
			}
			
			if(null!=fundSize&&!"".equals(fundSize)){
				fleetPo2.setFundSize(new Integer(fundSize));
			}
			
			if(null!=staffSize&&!"".equals(staffSize)){
				fleetPo2.setStaffSize(new Integer(staffSize));
			}
			
			fleetPo2.setZipCode(zipCode);
			fleetPo2.setAddress(address);
			
			if(null!=purpose&&!"".equals(purpose)){
				fleetPo2.setPurpose(new Integer(purpose));
			}
			
			fleetPo2.setRegion(region);
			fleetPo2.setMainLinkman(mainLinkman);
			fleetPo2.setMainJob(mainJob);
			fleetPo2.setMainPhone(mainPhone);
			fleetPo2.setMainEmail(mainEmail);
			fleetPo2.setOtherLinkman(otherLinkman);
			fleetPo2.setOtherJob(otherJob);
			fleetPo2.setOtherPhone(otherPhone);
			fleetPo2.setOtherEmail(otherEmail);
			fleetPo2.setReqRemark(reqRemark);
			fleetPo2.setUpdateBy(logonUser.getUserId());
			fleetPo2.setUpdateDate(currTime);
			
			
			FleetInfoAppDao appDao = new FleetInfoAppDao();
			
			
			TtFleetEditLogPO po1 = new TtFleetEditLogPO();
			po1.setEditId(new Long(editId));
			
			TtFleetEditLogPO po2 = new TtFleetEditLogPO();
			po2.setAuditDate(currTime);
			po2.setAuditUserId(logonUser.getUserId());
			po2.setAuditRemark(auditRemark);
			
			if(mark.equals("1")){
				po2.setStatus(Constant.FLEET_INFO_TYPE_03);
				appDao.update(fleetPo1, fleetPo2);//把log表里的数据更改到真实表中
			}
			if(mark.equals("0")){
				po2.setStatus(Constant.FLEET_INFO_TYPE_04);
			}
			
			
			// 根据集团客户ID更新状态
			eldao.update(po1, po2);//更改log状态
			act.setForword(fleetModifyCheckInitUrl);
			
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.UPDATE_FAILURE_CODE,"集团客户报备更改审核");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
}
